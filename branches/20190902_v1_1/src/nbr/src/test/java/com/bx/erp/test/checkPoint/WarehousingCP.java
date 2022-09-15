package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WarehousingCP {

	public static final int NotModify = 0;
	public static final int Modified = 1;

	// 1、入库单A普通缓存是否创建，入库单A的listSlave1中入库商品数据是否正确。
	// 2、检查数据库T_Warehousing是否有创建入库单A的数据。
	// 3、检查数据库T_WarehousingCommodity是否有创建入库单A相关的入库单商品数据。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing bmOfDB = new Warehousing();
		bmOfDB = (Warehousing) bmOfDB.parse1(o.getString(BaseAction.KEY_Object));
		Warehousing warhousingCreate = (Warehousing) bmCreateObjet.clone();
		warhousingCreate.setIgnoreIDInComparision(true);
		// 从请求中得到要创建的入库单商品列表
		List<WarehousingCommodity> whCommReqList = getWarehousingCommodityListParamFromReq(mr, bmOfDB.getID());
		// 要创建的入库单和创建好的入库单进行主从表的比较
		warhousingCreate.setListSlave1(whCommReqList);

		// 1、入库单A在DB是否创建，入库单A的listSlave1中入库商品数据是否正确。
		verifyObjectFromDB(bmOfDB, warhousingCreate);

		// 2、验证入库单A普通缓存
		verifyWarehousingCache(bmOfDB, dbName);

		// 3、检查数据库T_WarehousingCommodity是否有创建入库单A相关的入库单商品数据。(这个在步骤1可以检查了，不需要再RN)
		return true;
	}

	// 1、入库单A普通缓存是否修改，入库单A的ListSlave1()中采购商品数据是否正确。
	// 2、检查数据库T_Warehousing是否有修改的入库单A数据是否正确。
	// 3、检查数据库T_WarehousingCommodity，检查修改后的入库单商品数据是否正确。
	public static boolean verifyUpdateEx(MvcResult mr, BaseModel bmUpdateObjet, String dbName) throws Exception {

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		//
		Warehousing bmOfDB = new Warehousing();
		bmOfDB = (Warehousing) bmOfDB.parse1(o.getString(BaseAction.KEY_Object));
		// 从请求中获取参数
		List<WarehousingCommodity> whCommReqList = getWarehousingCommodityListParamFromReq(mr, bmOfDB.getID());
		// 设置对象默认值后进行比较
		Warehousing warehousingUpdate = (Warehousing) bmUpdateObjet.clone();
		warehousingUpdate.setListSlave1(whCommReqList);
		//
		warehousingUpdate.setIgnoreIDInComparision(true);

		// 2、检查数据库T_Warehousing是否有修改的入库单A数据是否正确。
		verifyObjectFromDB(bmOfDB, warehousingUpdate);

		// 1、入库单A普通缓存是否修改，入库单A的ListSlave1()中采购商品数据是否正确。
		verifyWarehousingCache(bmOfDB, dbName);

		// 3、检查数据库T_WarehousingCommodity，检查修改后的入库单商品数据是否正确。（这个在步骤2可以检查到）
		return true;
	}

	// 1、入库单A普通缓存是否删除。
	// 2、检查数据库T_Warehousing中，入库单A的是否已删除。
	// 3、检查数据库T_WarehousingCommodity中，入库单A相关的入库商品的是否已删除。
	public static boolean verifyDelete(BaseModel bmDeleteObjet, WarehousingBO warehousingBO, String dbName) {
		// 1、检查数据库T_Warehousing中，入库单A是否已删除
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmDeleteObjet);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个入库单失败，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		Assert.assertTrue(bmList.get(0).size() == 0 && bmList.get(1).size() == 0, "DB中的入库单没有被删除");

		// 2、检查入库单A普通缓存是否删除
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(bmDeleteObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取入库单失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		Assert.assertTrue(bm == null, "入库单普通缓存没有删除");

		// 3、检查数据库T_WarehousingCommodity中，入库单A相关的入库商品是否已删除，这点可以在步骤(1)中检查
		return true;
	}

	// 1、入库单A普通缓存F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查入库单A以及入库商品是否正确修改。
	// 2、入库单A的入库商品相关商品的缓存是否正确计算。
	// 3、检查数据库T_Warehousing，入库单A的F_Status是否为1(已审核)，如果审核前有修改操作，还需要检查入库单A是否正确修改。
	// 4、如果审核前有修改操作，需要检数据库T_WarehousingCommodity入库单A相关的入库商品是否正确修改。
	// 5、检查数据库T_Commodity，入库单A的入库商品相关商品F_NO是否正确计算。
	// 6、检查数据库T_CommodityHistory，是否创建了入库单A的入库商品相关的库存变化历史记录。
	// 7、如果入库单A的入库商品价格与关联采购单的商品价格不一致，检查数据库T_Message是否创建了采购差异报告。
	// 8、检查数据库T_PurchasingOrder，查看入库单A关联的采购单状态是否正确修改。
	@SuppressWarnings("unchecked")
	public static boolean verifyApprove(MvcResult mr, BaseModel bmApproveObject, WarehousingBO warehousingBO, PurchasingOrderBO purchasingOrderBO, List<Commodity> commList, CommodityHistoryBO commodityHistoryBO, MessageBO messageBO,
			String dbName, int shopID) throws Exception {
		// 3、检查数据库T_Warehousing，入库单A的F_Status是否为1(已审核)，如果审核前有修改操作，还需要检查入库单A是否正确修改。
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousingReturnObject = (Warehousing) new Warehousing().parse1(object.get(BaseAction.KEY_Object).toString());
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmApproveObject);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> warehousingList = bmList.get(0);
		Warehousing whDB = (Warehousing) warehousingList.get(0);
		Assert.assertTrue(whDB.getStatus() == EnumStatusWarehousing.ESW_Approved.getIndex(), "入库单A不是已审核状态！"); // ...
		//
		List<WarehousingCommodity> whCommDBList = new ArrayList<>();
		for (BaseModel bm : bmList.get(1)) {
			WarehousingCommodity whComm = (WarehousingCommodity) bm;
			whCommDBList.add(whComm);
		}
		whDB.setListSlave1(whCommDBList);

		// 1、入库单A普通缓存与DB中的是否一致
		verifyWarehousingCache(whDB, dbName);

		// 8、检查数据库T_PurchasingOrder，查看入库单A关联的采购单状态是否正确修改。(如果入库单有采购订单依赖)
		if (whDB.getPurchasingOrderID() > 0) {
			PurchasingOrder pOrder = new PurchasingOrder(); // ...
			pOrder.setID(whDB.getPurchasingOrderID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pOrder);
			if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "获取PurchasingOrder DB数据错误！, 错误码=" + purchasingOrderBO.getLastErrorCode() + "，错误信息=" + purchasingOrderBO.getLastErrorMessage());
			}
			PurchasingOrder pOrderDB = (PurchasingOrder) poList.get(0).get(0);
			// 采购商品A数量：50，采购商品B数量：100，那么审核的时候，必须审核了商品A50件和商品B100件，才算全部审核
			List<BaseModel> purchasingOrderCommodityList = poList.get(1); // ...
			List<PurchasingOrderCommodity> pOrderCommList = new ArrayList<>();
			for (BaseModel pOrderbm : purchasingOrderCommodityList) {
				PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) pOrderbm;
				pOrderCommList.add(pOrderComm);
			}
			verifyPurchasingStatus(whCommDBList, pOrderCommList, pOrderDB);

			// 7、如果入库单A的入库商品价格与关联采购单的商品价格不一致，检查数据库T_Message是否创建了采购差异报告。
			verifyMessage(warehousingReturnObject, pOrderCommList, messageBO, dbName);
		}
		// 审核前有修改操作，需要检查入库单A是否正确修改。
		Warehousing whApproveObject = (Warehousing) bmApproveObject;
		Staff staff = (Staff) mr.getRequest().getSession().getAttribute(EnumSession.SESSION_Staff.getName());
		whApproveObject.setApproverID(staff.getID());
		Warehousing whUpdate = (Warehousing) whApproveObject.clone();
		if (whApproveObject.getIsModified() == Modified) {
			// 从请求中获取参数
			List<WarehousingCommodity> whCommReqList = getWarehousingCommodityListParamFromReq(mr, bmApproveObject.getID());
			whUpdate.setListSlave1(whCommReqList);
			// 比较要更新的和从DB读出来的是否一致
			whUpdate.setIgnoreIDInComparision(true);
			Assert.assertTrue(whUpdate.compareTo(whDB) == 0, "要更新的和从DB读出来的不一致");

			// 4、如果审核前有修改操作，需要检数据库T_WarehousingCommodity入库单A相关的入库商品是否正确修改。(步骤3已检查)

			// 2、入库单A的入库商品相关商品的缓存是否正确计算。
			// 5、检查数据库T_Commodity，入库单A的入库商品相关商品F_NO是否正确计算。(步骤2和5一样)
			verifyCommodityNO(whCommReqList, commList, commodityHistoryBO, dbName, shopID);
		}
		// 审核前没有修改操作
		else if (whApproveObject.getIsModified() == NotModify) {
			// 5、检查数据库T_Commodity，入库单A的入库商品相关商品F_NO是否正确计算。
			// 获取DB中入库单A的主表和从表，
			List<WarehousingCommodity> whcDBList = (List<WarehousingCommodity>) whDB.getListSlave1();
			verifyCommodityNO(whcDBList, commList, commodityHistoryBO, dbName, shopID);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static void verifyCommodityNO(List<WarehousingCommodity> whCommApproveList, List<Commodity> commList, CommodityHistoryBO commodityHistoryBO, String dbName, int shopID) {
		ErrorInfo ecOut = new ErrorInfo();
		for (int i = 0; i < whCommApproveList.size(); i++) {
			WarehousingCommodity whc = whCommApproveList.get(i);
			Commodity comm = commList.get(i);
			Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(comm.getID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
			}
			List<CommodityShopInfo> commodityShopInfoCacheList = (List<CommodityShopInfo>) commodityCache.getListSlave2();
			CommodityShopInfo commodityShopInfoCache = null;
			for(CommodityShopInfo commodityShopInfo : commodityShopInfoCacheList) {
				if(commodityShopInfo.getShopID() == shopID) {
					commodityShopInfoCache = commodityShopInfo;
				}
			}
			List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) comm.getListSlave2();
			CommodityShopInfo commShopInfo = null;
			for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
				if(commodityShopInfo.getShopID() == shopID) {
					commShopInfo = commodityShopInfo;
				}
			}
			Assert.assertTrue(commodityShopInfoCache.getNO() == (commShopInfo.getNO() + whc.getNO()), "入库单A的入库商品相关商品F_NO没有正确计算。商品ID为" + comm.getID() + "不正确。" + commodityShopInfoCache.getNO() + "!=" + commShopInfo.getNO() + "+" + whc.getNO());
			// 6、检查数据库T_CommodityHistory，是否创建了入库单A的入库商品相关的库存变化历史记录。
			DataSourceContextHolder.setDbName(dbName);
			CommodityHistory commodityHistory = new CommodityHistory();
			commodityHistory.setCommodityID(comm.getID());
			// RN时会查询最新的，应该不用setPageSize
			List<CommodityHistory> commHistoryList = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
			if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "读取多个商品历史失败！,错误码=" + commodityHistoryBO.getLastErrorCode() + "，错误信息=" + commodityHistoryBO.getLastErrorMessage());
			}
			// 判断商品历史表F_CommodityID为i的商品历史的F_OldValue和F_NewValue
			boolean createCommodityHistory = false;
			for (CommodityHistory ch : commHistoryList) {
				// oldValue可能为空
				if(!ch.getOldValue().equals("")) {
					if (Integer.parseInt(ch.getOldValue()) == commShopInfo.getNO() && Integer.parseInt(ch.getNewValue()) == commodityShopInfoCache.getNO()) {
						createCommodityHistory = true;
						break;
					}
				}
			}
			Assert.assertTrue(createCommodityHistory, "入库单A的入库商品相关的库存变化历史记录没有创建，或创建有误！");
		}
	}

	private static void verifyPurchasingStatus(List<WarehousingCommodity> whCommDBList, List<PurchasingOrderCommodity> pOrderCommList, PurchasingOrder pOrder) {
		// 已审核的入库单商品与采购订单的商品进行比较
		boolean bAllWarehousing = true;
		for (PurchasingOrderCommodity poc : pOrderCommList) {
			boolean hasWarehousingCommodity = false; // 入库的商品可能没有一件是采购订单中的，所以不能说采购订单已经全部入库
			for (WarehousingCommodity whc : whCommDBList) {
				if (whc.getCommodityID() == poc.getCommodityID()) {
					hasWarehousingCommodity = true;
					if (whc.getNO() < poc.getCommodityNO()) { // 审核入库的数量等于待入库的数量
						bAllWarehousing = false;
					}
				}
			}
			if (!hasWarehousingCommodity) {
				bAllWarehousing = false;
				break;
			}
		}
		if (whCommDBList.size() > 0 && pOrder != null && bAllWarehousing) {
			Assert.assertTrue(pOrder.getStatus() == EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex(), "入库单A关联的采购单状态没有正确修改");
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static void verifyMessage(Warehousing wh, List<PurchasingOrderCommodity> pOrderCommList, MessageBO messageBO, String dbName) {
		// 关联采购单的商品价格
		List<WarehousingCommodity> whCommList = (List<WarehousingCommodity>) wh.getListSlave1();
		for (WarehousingCommodity whc : whCommList) {
			for (PurchasingOrderCommodity poc : pOrderCommList) {
				if (whc.getCommodityID() == poc.getCommodityID()) {
					if (whc.getPrice() != poc.getPriceSuggestion()) {
						Assert.assertTrue(wh.getMessageID() != 0, "数据库T_Message没有创建了采购差异报告");
						// 判断t_message是否有数据
						Message message = new Message();
						message.setID(wh.getMessageID());
						DataSourceContextHolder.setDbName(dbName);
						BaseModel bm = messageBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, message);
						if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
							Assert.assertTrue(false, "查询message失败，错误码:" + messageBO.getLastErrorCode() + ",错误信息：" + messageBO.getLastErrorMessage());
						}
						Assert.assertTrue(bm != null, "t_message没有创建盘点差异报告信息");
					}
				}
			}
		}
	}

	// 从请求中获取参数
	public static List<WarehousingCommodity> getWarehousingCommodityListParamFromReq(MvcResult mr, int warehousingID) {
		String commIDs = mr.getRequest().getParameter("commIDs");
		String commNOs = mr.getRequest().getParameter("commNOs");
		String commPrices = mr.getRequest().getParameter("commPrices");
		String amounts = mr.getRequest().getParameter("amounts");
		String barcodeIDs = mr.getRequest().getParameter("barcodeIDs");
		//
		Integer[] arCommID = GeneralUtil.toIntArray(commIDs);// ...
		Integer[] arCommNO = GeneralUtil.toIntArray(commNOs);
		Double[] arCommPrices = GeneralUtil.toDoubleArray(commPrices);
		Double[] arAmounts = GeneralUtil.toDoubleArray(amounts);
		Integer[] arBarcodeIDs = GeneralUtil.toIntArray(barcodeIDs);
		//
		List<WarehousingCommodity> whCommReqList = new ArrayList<>();
		for (int i = 0; i < arCommID.length; i++) {
			WarehousingCommodity whCommReq = new WarehousingCommodity();
			whCommReq.setWarehousingID(warehousingID);
			whCommReq.setCommodityID(arCommID[i]);
			whCommReq.setNO(arCommNO[i]);
			whCommReq.setNoSalable(whCommReq.getNO()); // 创建入库单时，F_NoSalable是根据F_NO来赋值的
			whCommReq.setPrice(arCommPrices[i]);
			whCommReq.setAmount(arAmounts[i]);
			whCommReq.setBarcodeID(arBarcodeIDs[i]);
			whCommReq.setShelfLife(Commodity.DEFAULT_VALUE_ShelfLife);// update时，action层给默认值
			whCommReqList.add(whCommReq);
		}
		return whCommReqList;
	}

	// 验证入库单A普通缓存
	public static void verifyWarehousingCache(Warehousing warehousingDB, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Warehousing whCache = (Warehousing) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(warehousingDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取入库单失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		// 要创建的入库单和缓存中的入库单进行主从表的比较
		Assert.assertTrue(warehousingDB.compareTo(whCache) == 0, "普通缓存没有该入库单,或者普通缓存中的数据不正确");
	}

	// 检查返回的数据库数据对象
	public static void verifyObjectFromDB(Warehousing whDB, Warehousing whCompare) throws Exception {
		Assert.assertTrue(whDB != null, "创建入库单返回的对象为null");
		Assert.assertTrue(whCompare.compareTo(whDB) == 0, "返回的对象信息有误");
	}
}
