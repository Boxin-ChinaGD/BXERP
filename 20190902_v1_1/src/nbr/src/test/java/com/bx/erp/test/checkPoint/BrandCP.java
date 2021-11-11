package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheDispatcherBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.BrandSyncCacheDispatcher;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class BrandCP {

	// 1、检查品牌A普通缓存是否创建。
	// 2、检查品牌A是否创建了C型同步块缓存。
	// 3、检查数据库T_Brand是否创建了品牌A的数据。
	// 4、检查数据库T_BrandSyncCache是否创建了品牌A的C型同步块。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, BrandSyncCacheBO brandSyncCacheBO, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Brand bmOfDB = new Brand();
		bmOfDB = (Brand) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Brand brandDefalutValue = (Brand) bmCreateObjet.clone();
		brandDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(brandDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 1、检查品牌A普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Brand, dbName);

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查品牌A是否创建了C型同步块缓存。
			BaseSyncCache brandSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_BrandSyncInfo, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(brandSyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_BrandSyncCache是否创建了品牌A的C型同步块。
			BaseSyncCache brandSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(brandSyncCacheBO, bmOfDB, brandSyncCache, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(brandSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查品牌A普通缓存是否修改。
	// 2、检查品牌A是否创建了U型同步块缓存。
	// 3、检查数据库T_Brand是否修改了品牌A的数据。
	// 4、检查数据库T_BrandSyncCache是否创建了品牌A的U型同步块。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, BrandSyncCacheBO brandSyncCacheBO, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Brand bmOfDB = new Brand();
		bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Brand brandDefalutValue = (Brand) bmCreateObjet.clone();
		brandDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(brandDefalutValue) == 0, "修改失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		// 1、检查品牌A普通缓存是否修改。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Brand, dbName);

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查品牌A是否创建了U型同步块缓存。
			BaseSyncCache brandSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_BrandSyncInfo, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(brandSyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_BrandSyncCache是否创建了品牌A的U型同步块。
			BaseSyncCache brandSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(brandSyncCacheBO, bmOfDB, brandSyncCache, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(brandSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查品牌A普通缓存是否删除。
	// 2、检查品牌A是否创建了D型同步块缓存。
	// 3、检查数据库T_Brand是否删除了品牌A的数据。
	// 4、检查数据库T_BrandSyncCache是否创建了品牌A的D型同步块。
	public static boolean verifyDelete(BaseModel bmCreateObjet, PosBO posBO, BrandBO brandBO, BrandSyncCacheBO brandSyncCacheBO, String dbName) throws Exception {
		// 检查数据库中品牌A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		Brand bmOfDB = (Brand) brandBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (brandBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的品牌失败，错误码=" + brandBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		// 1、检查品牌A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查品牌A是否创建了D型同步块缓存。
			BaseSyncCache brandSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmCreateObjet, EnumSyncCacheType.ESCT_BrandSyncInfo, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(brandSyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_BrandSyncCache是否创建了品牌A的D型同步块。
			BaseSyncCache brandSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(brandSyncCacheBO, bmCreateObjet, brandSyncCache, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(brandSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查品牌A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
	// 2、检查数据库T_BrandSyncCacheDispatcher是否创建了品牌A的POS1同步记录。
	@SuppressWarnings("unchecked")
	public static boolean verifySyncBrand(List<Brand> brandList, int posID, PosBO posBO, BrandSyncCacheDispatcherBO cacheDispatcherBO, String dbName) throws Exception {
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		// 1、检查品牌A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
		if (list.size() > 1) {
			BrandSyncCacheDispatcher brandSyncCacheDispatcher = new BrandSyncCacheDispatcher();
			// 获取数据库中同步块数据
			DataSourceContextHolder.setDbName(dbName);
			List<BrandSyncCacheDispatcher> cscdList = (List<BrandSyncCacheDispatcher>) cacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, brandSyncCacheDispatcher);
			if (cacheDispatcherBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找同步调度表失败，错误码=" + cacheDispatcherBO.getLastErrorCode().toString());
			}

			boolean posIDInListSlave1;
			boolean posIDInSyncCacheDispatcherDB;
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BrandSyncInfo).readN(false, false);
			for (Brand b : brandList) {
				posIDInListSlave1 = false;
				posIDInSyncCacheDispatcherDB = false;

				BaseSyncCache baseSyncCache = null;
				// 如果品牌的int1为1，代表这个品牌已经完全同步，同步块主表和从表都已被删除
				if (b.getIsSync() == 1) {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == b.getID()) {
							assertTrue(false, "该同步块已经完全同步，缓存中不应该还有该同步块！");
						}
					}
				} else {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == b.getID()) {
							break;
						}
					}
					assertTrue(baseSyncCache.getSyncData_ID() == b.getID(), "同步块不存在");

					// 检查该同步块的从表信息是否有POS机同步的数据
					BrandSyncCacheDispatcher cscdOfCache = new BrandSyncCacheDispatcher();
					for (Object o : baseSyncCache.getListSlave1()) {
						cscdOfCache = (BrandSyncCacheDispatcher) o;
						if (cscdOfCache.getPos_ID() == posID) {
							posIDInListSlave1 = true;
							break;
						}
					}
					if (!posIDInListSlave1) {
						assertTrue(false, "同步块从表没有正确插入。");
					}
					// 检查数据库的数据是否和缓存中的数据相等
					for (BrandSyncCacheDispatcher bscdOfDB : cscdList) {
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
