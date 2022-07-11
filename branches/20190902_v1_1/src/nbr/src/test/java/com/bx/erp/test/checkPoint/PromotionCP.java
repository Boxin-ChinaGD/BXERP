package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheDispatcherBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.dao.trade.PromotionMapper;
import com.bx.erp.dao.trade.PromotionScopeMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.PromotionSyncCacheDispatcher;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class PromotionCP {

	// 1、检查促销活动A的普通缓存是否创建，如果是指定商品促销，还需要检查ListSlave1()里的促销商品是否正确。
	// 2、检查促销活动A的C型同步块缓存是否创建。
	// 3、检查数据库T_Promotion，查看促销活动A是否正常创建。
	// 4、如果是指定商品促销，检查数据库T_PromotionScope，查看促销活动A的指定商品是否正常创建。
	// 5、检查数据库T_PromotionSyncCache，查看是否创建了促销活动A的C型同步块。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObject, String dbName, PosBO posBO, EnumSyncCacheType esct, PromotionSyncCacheBO promotionSyncCacheBO, PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO,
			int posID, PromotionScopeMapper promotionScopeMapper) throws Exception {
		// 检查数据库T_Promotion，查看促销活动A是否正常创建。
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Promotion promotionDB = new Promotion();
		promotionDB = (Promotion) promotionDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(promotionDB != null, "返回的对象为null");
		Promotion promotionCreate = (Promotion) bmCreateObject.clone();
		List<PromotionScope> ptScopeListFromReq = getPromotionScopeFromReq(mr, promotionCreate);
		promotionCreate.setListSlave1(ptScopeListFromReq);
		promotionCreate.setIgnoreIDInComparision(true);
		Assert.assertTrue(promotionCreate.compareTo(promotionDB) == 0, "创建的对象与DB读出来的不一致");
		// 检查促销活动的普通缓存
		verifyPromotionCache(promotionDB, dbName);
		// 检查促销活动A的C型同步块缓存是否创建。
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有创建出来的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == promotionDB.getID() && baseSyncCache.getSyncType().equals(SyncCache.SYNC_Type_C)) {
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存不存在创建出来的对象");
			b = false;
			// 结果验证：同步缓存DB里有无相应的对象
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcslist = promotionSyncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listID = new ArrayList<Integer>();
			for (Object obj2 : bcslist) {
				BaseSyncCache bsc = (BaseSyncCache) obj2;
				if (bsc.getSyncData_ID() == promotionDB.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_C)) {
					listID.add(bsc.getID());
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存DB不存在创建出来的对象");
			b = false;
			// 结果验证：检查同步缓存调度表（从表）的对象。因为是POS端创建的对象，所以需要作结果验证
			if (posID > 0) {
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<?> bcsdlist = promotionSyncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (Object obj3 : bcsdlist) {
					BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
					if (listID.contains(bscd.getSyncCacheID())) {
						b = true;
						break;
					}
				}
				Assert.assertTrue(b == true, "同步缓存从表DB不存在创建出来的对象");
			}
		}
		// 如果有促销活动有指定促销范围，则检查数据库T_PromotionScope是否有该促销相应的数据
		if (ptScopeListFromReq.size() > 0) {
			PromotionScope pScope = new PromotionScope();
			pScope.setPromotionID(promotionDB.getID());
			pScope.setPageIndex(1);
			pScope.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 查询创建的促销范围
			Map<String, Object> psParamsRN = pScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pScope);
			//
			DataSourceContextHolder.setDbName(dbName);
			List<BaseModel> psRN = promotionScopeMapper.retrieveN(psParamsRN);
			Assert.assertTrue(psRN.size() == promotionDB.getListSlave1().size(), "促销范围表没有正确创建");
		}
		return true;
	}

	private static void verifyPromotionCache(Promotion promotionDB, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Promotion pCache = (Promotion) CacheManager.getCache(dbName, EnumCacheType.ECT_Promotion).read1(promotionDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取促销失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		// 要创建的入库单和缓存中的入库单进行主从表的比较
		Assert.assertTrue(promotionDB.compareTo(pCache) == 0, "普通缓存没有该促销,或者普通缓存中的数据不正确");
	}

	private static List<PromotionScope> getPromotionScopeFromReq(MvcResult mr, Promotion promotionDB) {
		List<PromotionScope> ptScopeList = new ArrayList<>();
		String commIDs = mr.getRequest().getParameter(Promotion.field.getFIELD_NAME_commodityIDs());
		if (!StringUtils.isEmpty(commIDs)) { // commIDs != null && !commIDs.equals("")
			Integer[] arCommID = GeneralUtil.toIntArray(commIDs);
			for (int i = 0; i < arCommID.length; i++) {
				PromotionScope ptScope = new PromotionScope();
				ptScope.setPromotionID(promotionDB.getID());
				ptScope.setCommodityID(arCommID[i]);
				ptScopeList.add(ptScope);
			}
		}

		return ptScopeList;
	}

	// 1、检查促销活动A的普通缓存是否删除。
	// 2、检查促销活动A的D型同步块缓存是否创建。
	// 3、检查数据库T_Promotion，查看促销活动A的F_Status是否更改为1(已删除)。
	// 4、检查数据库T_PromotionSyncCache，查看是否创建了促销活动A的D型同步块。
	public static boolean verifyDelete(BaseModel bmDeleteObjet, String dbName, PromotionMapper promotionMapper, PosBO posBO, EnumSyncCacheType esctPromotionsyncinfo, PromotionSyncCacheBO promotionSyncCacheBO,
			PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO, int posID) throws Exception {
		// 检查数据库T_Promotion，查看促销活动A的F_Status是否更改为1(已删除)。
		Promotion promotionDelete = (Promotion) bmDeleteObjet.clone();
		DataSourceContextHolder.setDbName(dbName);
		Map<String, Object> promotionParam = promotionDelete.getRetrieve1Param(BaseBO.INVALID_CASE_ID, promotionDelete);
		Promotion promotionDB = (Promotion) promotionMapper.retrieve1(promotionParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(promotionParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				promotionParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(promotionDB != null, "查询出来的对象为null");
		Assert.assertTrue(promotionDB.getStatus() == EnumStatusPromotion.ESP_Deleted.getIndex(), "促销活动不是已删除状态！");
		// promotionCache没有实现R1删除的促销活动
		//		// 检查促销活动A的普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		Promotion pCache = (Promotion) CacheManager.getCache(dbName, EnumCacheType.ECT_Promotion).read1(promotionDelete.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取促销失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
//		Assert.assertTrue(pCache != null, "该促销在缓存中为null");
		// 检查促销活动A的普通缓存是否删除。
		Assert.assertTrue(pCache == null, "该促销在缓存中不是已删除状态");
		// 结果验证：如果pos机大于1，判断同步缓存中是否进行相应的D型插入且只有这个对象的1条数据
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		if (list != null && list.size() > 1) {
			// 结果验证：判断同步缓存中是否有创建出来的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esctPromotionsyncinfo).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == bmDeleteObjet.getID()) {
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存不存在创建出来的D型块");
			b = false;
			// 结果验证：判断同步DB中主表是否有相应的D型插入
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcslist = promotionSyncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listSyncCacheID = new ArrayList<Integer>();
			for (Object obj2 : bcslist) {
				BaseSyncCache bsc = (BaseSyncCache) obj2;
				if (bsc.getSyncData_ID() == bmDeleteObjet.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_D)) {
					listSyncCacheID.add(bsc.getID());
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存DB不存在创建出来的D型块");
			b = false;
			// 结果验证：如果是pos机发送的请求，判断同步缓存从表是否进行了D型的插入
			if (posID > 0) {
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<?> bcsdlist = promotionSyncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (Object obj3 : bcsdlist) {
					BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
					if (listSyncCacheID.contains(bscd.getSyncCacheID())) {
						b = true;
						break;
					}
				}
				Assert.assertTrue(b == true, "同步缓存从表DB不存在创建出来的D型块");
			}

		}
		return true;
	}

	// 1、检查促销活动A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
	// 2、检查数据库T_PromotionSyncCacheDispatcher是否创建了促销活动A的POS1同步记录。
	@SuppressWarnings("unchecked")
	public static boolean verifySyncPromotion(List<Promotion> promotionList, int posID, PosBO posBO, PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO, String dbName) {
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		// 1、检查小票格式A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
		if (list.size() > 1) {
			PromotionSyncCacheDispatcher promotionSyncCacheDispatcher = new PromotionSyncCacheDispatcher();
			// 获取数据库中同步块数据
			DataSourceContextHolder.setDbName(dbName);
			List<PromotionSyncCacheDispatcher> cscdList = (List<PromotionSyncCacheDispatcher>) promotionSyncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, promotionSyncCacheDispatcher);
			if (promotionSyncCacheDispatcherBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找同步调度表失败，错误码=" + promotionSyncCacheDispatcherBO.getLastErrorCode().toString());
			}
			//
			boolean posIDInListSlave1;
			boolean posIDInSyncCacheDispatcherDB;
			boolean existPromotionSyncCacheID;
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_PromotionSyncInfo).readN(false, false);
			for (Promotion promotion : promotionList) {
				posIDInListSlave1 = false;
				posIDInSyncCacheDispatcherDB = false;
				existPromotionSyncCacheID = false;
				BaseSyncCache baseSyncCache = null;
				// 如果小票格式的int1为1，代表这个小票格式已经完全同步，同步块主表和从表都已被删除
				if (promotion.getIsSync() == 1) {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == promotion.getID()) {
							assertTrue(false, "该同步块已经完全同步，缓存中不应该还有该同步块！");
						}
					}
				} else {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == promotion.getID()) {
							existPromotionSyncCacheID = true;
							break;
						}
					}
					assertTrue(existPromotionSyncCacheID, "同步块不存在");
					// 检查该同步块的从表信息是否有POS机同步的数据
					PromotionSyncCacheDispatcher cscdOfCache = new PromotionSyncCacheDispatcher();
					for (Object o : baseSyncCache.getListSlave1()) {
						cscdOfCache = (PromotionSyncCacheDispatcher) o;
						if (cscdOfCache.getPos_ID() == posID) {
							posIDInListSlave1 = true;
							break;
						}
					}
					if (!posIDInListSlave1) {
						assertTrue(false, "同步块从表没有正确插入。");
					}
					// 检查数据库的数据是否和缓存中的数据相等
					for (PromotionSyncCacheDispatcher bscdOfDB : cscdList) {
						if (cscdOfCache.getID() == bscdOfDB.getID() && bscdOfDB.compareTo(cscdOfCache) == 0) {
							posIDInSyncCacheDispatcherDB = true;
							break;
						}
					}
					if (!posIDInSyncCacheDispatcherDB) {
						assertTrue(false, "同步块从表没有正确插入数据库。");
					}
				}
			}
		}
		return true;
	}
}
