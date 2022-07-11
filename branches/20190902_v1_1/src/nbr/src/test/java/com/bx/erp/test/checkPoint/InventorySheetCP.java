package com.bx.erp.test.checkPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.warehousing.InventorySheetMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.InventorySheet.EnumStatusInventorySheet;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class InventorySheetCP {

	// 1、盘点单A普通缓存是否创建，盘点单A的ListSlave1()中采购商品数据是否正确。
	// 2、检查数据库T_InventorySheet是否有创建盘点单A的数据。
	// 3、检查数据库T_InventorySheetCommodity是否有创建盘点单A相关的盘点单商品数据。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 2、检查数据库T_InventorySheet是否有创建盘点单A的数据。(3、检查t_inventorycommodity)
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		InventorySheet inventorySheetDB = new InventorySheet();
		inventorySheetDB = (InventorySheet) inventorySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(inventorySheetDB != null, "创建盘点单返回的对象为null");
		//
		InventorySheet inventorySheetCreate = (InventorySheet) bmCreateObjet.clone();
		List<InventoryCommodity> inventoryCommReqList = getInventoryCommodityListFromReq(mr, inventorySheetDB.getID());
		inventorySheetCreate.setListSlave1(inventoryCommReqList);
		inventorySheetCreate.setIgnoreIDInComparision(true);
		Assert.assertTrue(inventorySheetCreate.compareTo(inventorySheetDB) == 0, "返回的对象信息有误");
		
		// 1、盘点单A普通缓存是否创建，盘点单A的ListSlave1()中采购商品数据是否正确。
		verifyInventorySheetCache(inventorySheetDB, dbName);

		return true;
	}

	// 1、盘点单A普通缓存是否修改，盘点单A的ListSlave1()中采购商品数据是否正确。
	// 2、检查数据库T_InventorySheet是否有修改的盘点单A数据是否正确。
	// 3、检查数据库T_InventorySheetCommodity，检查修改后的盘点单商品数据是否正确。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmUpdateObject, String dbName) throws Exception {
		// 2、检查数据库T_InventorySheet是否有修改的盘点单A数据是否正确。(3、检查t_inventorycommodity)
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		InventorySheet inventorySheetDB = new InventorySheet();
		inventorySheetDB = (InventorySheet) inventorySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(inventorySheetDB != null, "修改盘点单返回的对象为null");
		//
		InventorySheet inventorySheetUpdate = (InventorySheet) bmUpdateObject.clone();
		// 检查DB的信息是否与要更新的数据一致
		Assert.assertTrue(inventorySheetUpdate.compareTo(inventorySheetDB) == 0, "返回的对象信息有误");

		// 1、盘点单A普通缓存是否修改，盘点单A的ListSlave1()中采购商品数据是否正确。
		verifyInventorySheetCache(inventorySheetDB, dbName);
		
		return true;
	}
	
//	1、盘点单A普通缓存是否删除。
//	2、检查数据库T_InventorySheet中，盘点单A的F_Status是否为3(已删除)。
	public static boolean verifyDelete(BaseModel bmDeleteObject, InventorySheetMapper inventorySheetMapper, String dbName) throws Exception {
		//2、检查数据库T_InventorySheet中，盘点单A的F_Status是否为3(已删除)。
		InventorySheet is = (InventorySheet) bmDeleteObject.clone();
		Map<String, Object> params = is.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, is);
		DataSourceContextHolder.setDbName(dbName);
		inventorySheetMapper.retrieve1Ex(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData);
		
		//1、盘点单A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).read1(bmDeleteObject.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoSuchData) {
			Assert.assertTrue(false,"盘点单A普通缓存没有删除");
		}
		
		return true;
	}
	
//	1、盘点单A普通缓存F_Status是否为1(已提交)。如果提交前有修改操作，还需要检查盘点单A以及盘点商品是否正确修改。
//	2、检查数据库T_Inventory，盘点单A的F_Status是否为1(已审核)。如果提交前有修改操作，还需要检查采购单A是否正确修改。
//	3、检查数据库T_InventoryCommodity，如果提交前有修改操作，检查盘点商品是否正确修改。
	public static boolean verifySubmit(MvcResult mr, BaseModel bmSubmitObjet, String dbName) throws Exception {
		//2、检查数据库T_Inventory，盘点单A的F_Status是否为1(已提交)。如果提交前有修改操作，还需要检查采购单A是否正确修改。(包含步骤3)
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		InventorySheet inventorySheetDB = new InventorySheet();
		inventorySheetDB = (InventorySheet) inventorySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(inventorySheetDB != null, "提交盘点单返回的对象为null");
		//
		Assert.assertTrue(inventorySheetDB.getStatus() == EnumStatusInventorySheet.ESIS_Submitted.getIndex(), "盘点单不是已提交状态");
		InventorySheet inventorySheetSubmit = (InventorySheet) bmSubmitObjet.clone();
		Assert.assertTrue(inventorySheetSubmit.compareTo(inventorySheetDB) == 0, "返回的对象信息有误");
		
		//1、检查缓存，因为DB中的盘点单在上一个步骤已经检查过了，所以当缓存与DB中的盘点单相等时，缓存数据就是正确的
		verifyInventorySheetCache(inventorySheetDB, dbName);
		
		return true;
	}
	
//	1、盘点单A普通缓存F_Status是否为2(已审核)。如果审核前有修改盘点总结，还需要检查盘点单A的盘点总结是否正确修改。
//	2、检查数据库T_Inventory，盘点单A的F_Status是否为2(已审核)。如果审核前有修改盘点总结，还需要检查盘点单A的盘点总结是否正确修改。
//	3、如果盘点商品的实盘数量和系统数量不一致，要检查数据库T_Message是否创建了盘点单A的盘点差异报告
	public static boolean verifyApprove(MvcResult mr, BaseModel bmApproveObjet, MessageBO messageBO, String dbName) throws Exception {
		//2、检查数据库T_Inventory，盘点单A的F_Status是否为2(已审核)。如果审核前有修改盘点总结，还需要检查盘点单A的盘点总结是否正确修改。
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		InventorySheet inventorySheetDB = new InventorySheet();
		inventorySheetDB = (InventorySheet) inventorySheetDB.parse1(o.getString(BaseAction.KEY_Object));
		//检查DB状态
		Assert.assertTrue(inventorySheetDB != null, "审核盘点单返回的对象为null");
		Assert.assertTrue(inventorySheetDB.getStatus() == EnumStatusInventorySheet.ESIS_Approved.getIndex(), "盘点单不是已审核状态");
		//检查商品列表和仓库 
		List<BaseModel> listCommodity = JsonPath.read(o, "$.object.listCommodity");
		Assert.assertTrue(listCommodity != null, "未加载出商品列表");
		String warehouseName = JsonPath.read(o, "$.object.warehouse.name");
		Assert.assertTrue(warehouseName != null, "未加载出相应的仓库");
		// 检查DB的信息是否与要更新的数据是否一致
		InventorySheet inventorySheetApprove = (InventorySheet) bmApproveObjet.clone();
		Assert.assertTrue(inventorySheetApprove.compareTo(inventorySheetDB) == 0, "返回的对象信息有误");
		
		//1、检查缓存，因为DB中的盘点单在上一个步骤已经检查过了，所以当缓存与DB中的盘点单相等时，缓存数据就是正确的
		verifyInventorySheetCache(inventorySheetDB, dbName);
		
		//3、如果盘点商品的实盘数量和系统数量不一致，要检查数据库T_Message是否创建了盘点单A的盘点差异报告
		verifyMessage(inventorySheetDB, messageBO, dbName);
		
		return true;
	}

	private static List<InventoryCommodity> getInventoryCommodityListFromReq(MvcResult mr, int inventorySheetID) {
		List<InventoryCommodity> inventoryCommReqList = new ArrayList<>();
		String commListID = mr.getRequest().getParameter("commListID");
		String barcodeIDs = mr.getRequest().getParameter("barcodeIDs");
		String commNOReals = mr.getRequest().getParameter("commNOReals");
		//
		Integer[] arCommListID = GeneralUtil.toIntArray(commListID);// ...
		Integer[] arBarcodeIDs = GeneralUtil.toIntArray(barcodeIDs);
		Integer[] arCommNOReals = GeneralUtil.toIntArray(commNOReals);
		//
		for (int i = 0; i < arCommListID.length; i++) {
			InventoryCommodity itComm = new InventoryCommodity();
			itComm.setCommodityID(arCommListID[i]);
			itComm.setBarcodeID(arBarcodeIDs[i]);
			itComm.setNoReal(arCommNOReals[i]);
			itComm.setInventorySheetID(inventorySheetID);
			inventoryCommReqList.add(itComm);
		}
		return inventoryCommReqList;
	}

	private static void verifyInventorySheetCache(InventorySheet inventorySheetDB, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		InventorySheet itSheetCache = (InventorySheet) CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).read1(inventorySheetDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取盘点单失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		// 要创建的盘点单和缓存中的盘点单进行主从表的比较
		Assert.assertTrue(inventorySheetDB.compareTo(itSheetCache) == 0, "普通缓存没有该盘点单,或者普通缓存中的数据不正确");
	}
	@SuppressWarnings("unchecked")
	private static void verifyMessage(InventorySheet inventorySheetDB, MessageBO messageBO, String dbName) {
		List<InventoryCommodity> inventoryCommListDB = (List<InventoryCommodity>) inventorySheetDB.getListSlave1();
		for(int i = 0; i < inventoryCommListDB.size(); i++) {
			InventoryCommodity itComm = inventoryCommListDB.get(i);
			if(itComm.getNoReal() != itComm.getNoSystem()) {
				Assert.assertTrue(inventorySheetDB.getMessageID() > 0 , "没有创建盘点单A的盘点差异报告");
				//判断t_message是否有数据
				Message message = new Message();
				message.setID(inventorySheetDB.getMessageID());
				DataSourceContextHolder.setDbName(dbName);
				BaseModel bm = messageBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, message);
				if(messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					Assert.assertTrue(false, "查询message失败，错误码:" + messageBO.getLastErrorCode() + ",错误信息："+ messageBO.getLastErrorMessage());
				}
				Assert.assertTrue(bm != null, "t_message没有创建盘点差异报告信息");
			}
		}
	}
}
