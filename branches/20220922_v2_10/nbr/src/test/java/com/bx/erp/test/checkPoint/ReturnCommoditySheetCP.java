package com.bx.erp.test.checkPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ReturnCommoditySheetCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.commodity.CommodityMapper;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheet.EnumStatusReturnCommoditySheet;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class ReturnCommoditySheetCP {

	public static final int NotModify = 0;
	public static final int Modified = 1;

	// 1、检查数据库T_ReturnCommoditySheet，查看退货单A是否创建。
	// 2、检查数据库T_ReturnCommoditySheetCommodity，查看退货单A相关的退货商品是否创建。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet) throws Exception {
		// 检查数据库T_ReturnCommoditySheet，查看退货单A是否创建。(2检查数据库T_ReturnCommoditySheetCommodity)
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(returnCommoditySheetDB != null, "创建退货单返回的对象为null");
		//
		String error = returnCommoditySheetDB.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "数据库中的数据不合法:" + error);
		// 检查主从表
		ReturnCommoditySheet returnCommoditySheetCreate = (ReturnCommoditySheet) bmCreateObjet.clone();
		List<ReturnCommoditySheetCommodity> rcsCommodityReqList = getRCSCommodityListFromReq(mr, returnCommoditySheetDB.getID());
		returnCommoditySheetCreate.setListSlave1(rcsCommodityReqList);
		returnCommoditySheetCreate.setIgnoreIDInComparision(true);
		Assert.assertTrue(returnCommoditySheetCreate.compareTo(returnCommoditySheetDB) == 0, "返回的对象信息有误");

		return true;
	}

	// 1、检查数据库T_ReturnCommoditySheet，查看退货单A是否修改。
	// 2、检查数据库T_ReturnCommoditySheetCommodity，查看退货单A相关的退货商品是否修改。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmUpdateObjet) throws Exception {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(returnCommoditySheetDB != null, "更新退货单返回的对象为null");
		String error = returnCommoditySheetDB.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "数据库中的数据不合法:" + error);
		// 检查主从表
		ReturnCommoditySheet returnCommoditySheetUpdate = (ReturnCommoditySheet) bmUpdateObjet.clone();
		List<ReturnCommoditySheetCommodity> rcsCommodityReqList = getRCSCommodityListFromReq(mr, returnCommoditySheetDB.getID());
		returnCommoditySheetUpdate.setListSlave1(rcsCommodityReqList);
		returnCommoditySheetUpdate.setIgnoreIDInComparision(true);
		Assert.assertTrue(returnCommoditySheetUpdate.compareTo(returnCommoditySheetDB) == 0, "返回的对象信息有误");
		return true;
	}

	// 1、检查退货单A的退货商品缓存是否更新。
	// 2、检查数据库T_ReturnCommoditySheet，查看退货单A的F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查退货单A数据是否正确修改。
	// 3、检查数据库T_ReturnCommoditySheetCommodity，如果审核前有修改操作，查看退货单A相关的退货商品是否修改。
	// 4、检查数据库T_Commodity，查看退货单A的退货商品库存是否正确计算，F_CurrentWarehousingID是否更新。
	// 5、检查退货单A的退货商品，查看该商品的入库单可售数量是否正确计算。
	@SuppressWarnings("unchecked")
	public static boolean verifyApprove(MvcResult mr, BaseModel bmApproveObjet, CommodityMapper commodityMapper, List<Commodity> commListBeforeApprove, ReturnCommoditySheetCommodityBO returnCommoditySheetCommodityBO, String dbName,
			List<Warehousing> warehousingList, WarehousingMapper warehousingMapper) throws Exception {
		// 2、检查数据库T_ReturnCommoditySheet、状态(3、检查数据库T_ReturnCommoditySheetCommodity)
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		//
		Assert.assertTrue(returnCommoditySheetDB != null, "更新退货单返回的对象为null");
		String error = returnCommoditySheetDB.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "数据库中的数据不合法:" + error);
		// 检查是否为已审核状态
		Assert.assertTrue(returnCommoditySheetDB.getStatus() == EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex(), "退货单不是已审核状态");
		ReturnCommoditySheet returnCommoditySheetApprove = (ReturnCommoditySheet) bmApproveObjet.clone();
		// 审核时没有修改操作,不用检查主从表
		if (returnCommoditySheetApprove.getbReturnCommodityListIsModified() == NotModify) {
			// 审核后没有返回从表，需要从数据库获取
			verifyReturnCommoditySheetAndListSlave(returnCommoditySheetDB, returnCommoditySheetApprove, returnCommoditySheetCommodityBO, dbName);
			//
			List<ReturnCommoditySheetCommodity> rcsCommodityList = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetApprove.getListSlave1();
			// 4、检查数据库T_Commodity和普通缓存
			verifyCommodityFromDbANDCache(rcsCommodityList, commListBeforeApprove, commodityMapper, dbName, warehousingList, warehousingMapper);
		}
		// 审核时有修改操作，判断是否正确修改
		else if (returnCommoditySheetApprove.getbReturnCommodityListIsModified() == Modified) {
			List<ReturnCommoditySheetCommodity> rcsCommodityReqList = getRCSCommodityListFromReq(mr, returnCommoditySheetDB.getID());
			returnCommoditySheetApprove.setListSlave1(rcsCommodityReqList);
			// 审核后没有返回从表，需要重新查询从表
			verifyReturnCommoditySheetAndListSlave(returnCommoditySheetDB, returnCommoditySheetApprove, returnCommoditySheetCommodityBO, dbName);
			// 4、检查数据库T_Commodity和普通缓存
			verifyCommodityFromDbANDCache(rcsCommodityReqList, commListBeforeApprove, commodityMapper, dbName, warehousingList, warehousingMapper);
		}
		return true;
	}

	private static List<ReturnCommoditySheetCommodity> getRCSCommodityListFromReq(MvcResult mr, int returnCommoditySheetID) {
		List<ReturnCommoditySheetCommodity> rcsCommodityReqList = new ArrayList<>();
		String commIDs = mr.getRequest().getParameter("commIDs");
		String rcscNOs = mr.getRequest().getParameter("rcscNOs");
		String commPrices = mr.getRequest().getParameter("commPrices");
		String rcscSpecifications = mr.getRequest().getParameter("rcscSpecifications");
		String barcodeIDs = mr.getRequest().getParameter("barcodeIDs");
		//
		Integer[] arCommIDs = GeneralUtil.toIntArray(commIDs);// ...
		Integer[] arRcscNOs = GeneralUtil.toIntArray(rcscNOs);
		Double[] arCommPrices = GeneralUtil.toDoubleArray(commPrices);
		String[] arRcscSpecifications = rcscSpecifications.split(",");
		Integer[] arBarcodeIDs = GeneralUtil.toIntArray(barcodeIDs);
		//
		for (int i = 0; i < arCommIDs.length; i++) {
			ReturnCommoditySheetCommodity rctComm = new ReturnCommoditySheetCommodity();
			rctComm.setCommodityID(arCommIDs[i]);
			rctComm.setNO(arRcscNOs[i]);
			rctComm.setPurchasingPrice(arCommPrices[i]);
			rctComm.setSpecification(arRcscSpecifications[i]);
			rctComm.setBarcodeID(arBarcodeIDs[i]);
			rctComm.setReturnCommoditySheetID(returnCommoditySheetID);
			rcsCommodityReqList.add(rctComm);
		}

		return rcsCommodityReqList;
	}

	private static void verifyCommodityFromDbANDCache(List<ReturnCommoditySheetCommodity> rcsCommodityList, List<Commodity> commListBeforeApprove, CommodityMapper commodityMapper, String dbName, List<Warehousing> warehousingList,
			WarehousingMapper warehousingMapper) {
		Commodity commodity = new Commodity();
		for (int i = 0; i < rcsCommodityList.size(); i++) {
			ReturnCommoditySheetCommodity rcsComm = rcsCommodityList.get(i);
			commodity.setID(rcsComm.getCommodityID());
			// 从DB查询商品，检查库存数量
			Map<String, Object> paramsRetrieve = commodity.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, commodity);
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> commR1 = commodityMapper.retrieve1Ex(paramsRetrieve);// ...
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieve.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(commR1.size() > 0, "查询失败");
			//
			Commodity commApproved = Commodity.fetchCommodityFromResultSet(commR1);
			//
			for (int j = 0; j < commListBeforeApprove.size(); j++) {
				// 商品库存有没有正确计算
				Commodity commBeforeApprove = commListBeforeApprove.get(j);
				if (commApproved.getID() == commBeforeApprove.getID()) {
					Assert.assertTrue(commApproved.getNO() == (commBeforeApprove.getNO() - rcsComm.getNO()), "审核退货单后，商品的库存没有正确计算");
					//
					Warehousing wsBeforeApprove = warehousingList.get(j);
					// 如果该商品有对应的已审核的入库单,则判断F_CurrentWarehousingID是否更新。否则判断库存是否改变（未审核不改变）
					if (wsBeforeApprove != null) {
						verifyWarehousingCommodityNoSale(commApproved, wsBeforeApprove, warehousingMapper, dbName, rcsComm);
					}
					break;
				}
			}
			// 检查商品普通缓存
			ErrorInfo ecOut = new ErrorInfo();
			Commodity commCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rcsComm.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commCache == null) {
				Assert.assertTrue(false, "查找商品失败！");
			}
			Assert.assertTrue(commApproved.compareTo(commCache) == 0, "缓存的商品信息没有更新");
		}

	}

	@SuppressWarnings("unchecked")
	private static void verifyReturnCommoditySheetAndListSlave(ReturnCommoditySheet returnCommoditySheetDB, ReturnCommoditySheet returnCommoditySheetApprove, ReturnCommoditySheetCommodityBO returnCommoditySheetCommodityBO, String dbName) {
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(returnCommoditySheetDB.getID());
		rcsc.setPageIndex(1);
		rcsc.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<ReturnCommoditySheetCommodity> rcscList = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, rcsc);
		//
		Assert.assertTrue(returnCommoditySheetCommodityBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "查询退货单商品失败，失败信息：" + returnCommoditySheetCommodityBO.getLastErrorMessage());
		Assert.assertTrue(rcscList.size() > 0, "退货单商品为空");
		//
		returnCommoditySheetDB.setListSlave1(rcscList);
		returnCommoditySheetApprove.setIgnoreIDInComparision(true);
		Assert.assertTrue(returnCommoditySheetApprove.compareTo(returnCommoditySheetDB) == 0, "审核退货单返回的信息与审核前的信息不一致");
	}

	@SuppressWarnings("unchecked")
	private static void verifyWarehousingCommodityNoSale(Commodity commApproved, Warehousing wsBeforeApprove, WarehousingMapper warehousingMapper, String dbName, ReturnCommoditySheetCommodity rcsComm) {
		int oldWarehousingID = wsBeforeApprove.getID();// 审核退货前的入库单ID
		// Assert.assertTrue(oldWarehousingID > 0, "当值入库ID必须大于0");
		int noSaleReduce = 0;// DB中减少的入库单商品可售数量
		int oldNoSalable = 0;// 审核退货单前，商品对应的入库单商品的可售数量
		List<WarehousingCommodity> whCommListBeforeApprove = (List<WarehousingCommodity>) wsBeforeApprove.getListSlave1();
		for (int b = 0; b < whCommListBeforeApprove.size(); b++) {
			WarehousingCommodity whCommBeforeApprove = whCommListBeforeApprove.get(b);
			if (whCommBeforeApprove.getCommodityID() == commApproved.getID()) {
				oldNoSalable = whCommBeforeApprove.getNoSalable();// 得到审核退货单前，商品对应的入库单商品的可售数量
			}
		}
		Warehousing warehousingSheet = new Warehousing();
		// 从审核退货单前的入库单ID开始，到审核退货单后的入库单ID进行遍历，计算入库单商品的减少的可售数量是否等于退货数量
		int currentWarehousingID = commApproved.getCurrentWarehousingID();// 审核退货单后的入库单ID
		// 存在一种情况，就是入库没审核就零售，此时的当值入库ID不存在，入库单可售数量不应该改变
		int maxWarehousingID = currentWarehousingID == 0 ? oldWarehousingID : currentWarehousingID;
		for (int k = oldWarehousingID; k <= maxWarehousingID; k++) {
			ErrorInfo ecOut = new ErrorInfo();
			warehousingSheet.setID(k);
			//
			Map<String, Object> params = warehousingSheet.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, warehousingSheet);
			DataSourceContextHolder.setDbName(dbName);
			// 从DB中获取入库单及入库商品
			List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(params);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			if (bmList.get(0).size() > 0) {
				Warehousing wsFromDB = (Warehousing) bmList.get(0).get(0);
				List<BaseModel> whCommBmList = bmList.get(1);
				List<WarehousingCommodity> whCommDBList = new ArrayList<>();
				for (BaseModel bm : whCommBmList) {
					WarehousingCommodity wsComm = (WarehousingCommodity) bm;
					whCommDBList.add(wsComm);
				}
				wsFromDB.setListSlave1(whCommDBList);
				// 缓存的入库单商品的NoSale与数据库的不一致，审核退货单后缓存没有更新
				Warehousing ws = (Warehousing) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(k, BaseBO.SYSTEM, ecOut, dbName);
				Assert.assertTrue(ws.compareTo(wsFromDB) == 0, "缓存的数据与DB不一致");
				if (wsFromDB != null) {
					List<WarehousingCommodity> whCommList = (List<WarehousingCommodity>) wsFromDB.getListSlave1();
					for (int a = 0; a < whCommList.size(); a++) {
						WarehousingCommodity whComm = whCommList.get(a);
						if (whComm.getCommodityID() == commApproved.getID()) {
							// 审核退货单前的入库单对应的商品，计算减少的可售商品数量为F_OldNoSale-F_NewNoSale
							if (whComm.getWarehousingID() == oldWarehousingID) {
								noSaleReduce += oldNoSalable - whComm.getNoSalable();
							}
							// 审核退货单前后的入库单对应的商品，计算减少的可售商品数量为F_NO - F_NoSale
							else {
								noSaleReduce += whComm.getNO() - whComm.getNoSalable();

							}
						}
					}
				}
			}
		}
		// 5、检查退货单A的退货商品，查看该商品的入库单可售数量是否正确计算。
		if (currentWarehousingID != 0) {
			Assert.assertTrue(noSaleReduce == rcsComm.getNO(), "入库单的可售数量没有正确计算," + noSaleReduce + "!=" + rcsComm.getNO());
		} else {
			Assert.assertTrue(noSaleReduce == 0, "入库单的可售数量没有正确计算,未审核的可售数量不应该改变。但是此时可售数量减少了 " + noSaleReduce);
		}
	}
}
