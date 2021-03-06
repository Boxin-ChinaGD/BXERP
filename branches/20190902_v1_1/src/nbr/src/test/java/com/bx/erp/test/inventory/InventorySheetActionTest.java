package com.bx.erp.test.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.InventorySheet.EnumStatusInventorySheet;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.InventorySheetCP;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class InventorySheetActionTest extends BaseActionTest {
	public final static int BigNumber_Test = 1000000;

	private final String KEY_CommListID = "commListID";
	private final String KEY_BarcodeIDs = "barcodeIDs";
	private final String KEY_CommNOReals = "commNOReals";
	private final String KEY_InventoryCommodityIDs = "inventoryCommodityIDs";

	private InventorySheet inventorySheet = new InventorySheet();

	private Staff staff;

	@BeforeClass
	public void setup() {
		super.setUp();

		staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());

		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????tear down???????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		// ????????????????????????????????????????????????setup?????????????????????????????????????????????????????????????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	// ???????????????????????????????????????session????????????0
	// private HttpSession getLoginSession() throws Exception {
	// ???????????????
	// InventorySheet inventorySheet =
	// BaseInventorySheetTest.createInventorySheet();

	// MvcResult result = mvc.perform(//
	// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
	// + "=" + inventorySheet.getID())//
	// .session((MockHttpSession) sessionBoss)//
	// )//
	// .andExpect(status().isOk())//
	// .andReturn();

	// return result.getRequest().getSession();
	// return sessionBoss;
	// }

	// ???????????????????????????????????????session????????????????????????1
	private HttpSession getLoginSession1() throws Exception {

		MvcResult mr = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_messageID(), "1").contentType(MediaType.APPLICATION_JSON)//
						// .session((MockHttpSession) getLoginSession())//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		InventorySheet is1 = (InventorySheet) mr.getRequest().getSession().getAttribute("SESSION_InventorySheet");
		// MvcResult result = mvc.perform(//
		// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
		// + "=" + is1.getID())//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk())//
		// .andReturn();???????????????????????????

		return sessionBoss;
	}

	// ?????????2???session
	// private HttpSession getLoginSession2() throws Exception {
	// MvcResult result = mvc.perform(//
	// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
	// + "=8")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk())//
	// .andReturn();
	// return result.getRequest().getSession();
	// }

	// @Test
	// public void testRetrieveNInventory() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// // InventorySheet is = new InventorySheet();
	// mvc.perform(//
	// post("/inventorySheet/retrieveNInventory.bx")//
	// .param(InventorySheet.field.getFIELD_NAME_status(), "0")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk());// ...?????????????????????
	// }

	protected void checkBarcodes(MvcResult mr) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> barcodesList = JsonPath.read(o, "$.objectList[*].listSlave1[*].barcodes");
		for (String barcodes : barcodesList) {
			if (barcodes == null) {
				Assert.assertTrue(false, "barcodes????????????????????????");
			}
		}
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1???????????????0----------------------------");
		// InventorySheet is = new InventorySheet();
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "0")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		checkBarcodes(mr);

		System.out.println("-----------------------------case2???????????????1----------------------------");
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		checkBarcodes(mr1);

		System.out.println("-----------------------------case3???????????????2----------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		checkBarcodes(mr2);

		System.out.println("-----------------------------case4???????????????-1----------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "-1")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		System.out.println("-----------------------------case5???????????????3----------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "3")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("-----------------------------case6???????????????1000----------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1000")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("-----------------------------case7???????????????????????????---------------------------------");
//		MvcResult mr6 = mvc.perform(//
//				post("/inventorySheet/retrieveNEx.bx")//
//						.param(InventorySheet.field.getFIELD_NAME_status(), "0")//
//						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);

		System.out.println("-----------------------------case8???????????????ID??????----------------------------");
		// InventorySheet is = new InventorySheet();
		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "0")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7);

		checkBarcodes(mr7);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// System.out.println("-----------------------------case1:?????????0????????????----------------------------");
		// // InventorySheet is = new InventorySheet();
		// MvcResult mr = null;
		// mr = mvc.perform(//
		// post("/inventorySheet/submitEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
		//
		// JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// List<BaseModel> listSlave1 = JsonPath.read(o, "$.object.listSlave1");
		// Assert.assertTrue(listSlave1 != null, "??????????????????????????????");
		// //
		// List<BaseModel> listCommodity = JsonPath.read(o, "$.object.listCommodity");
		// Assert.assertTrue(listCommodity != null, "????????????????????????");
		// //
		// String warehouseName = JsonPath.read(o, "$.object.warehouse.name");
		// Assert.assertTrue(warehouseName != null, "???????????????????????????");
		//
		// System.out.println("-----------------------------case2:?????????1????????????----------------------------");
		// mr = null;
		// mr = mvc.perform(//
		// post("/inventorySheet/submitEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession1())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// System.out.println("-----------------------------case3:?????????2????????????????????????----------------------------");
		// mr = null;
		// mr = mvc.perform(//
		// post("/inventorySheet/submitEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .session((MockHttpSession) getLoginSession2())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// System.out.println("-----------------------------case4:????????????????????????----------------------------");
		// mr = null;
		// mr =
		// mvc.perform(post("/inventorySheet/submitEx.bx").contentType(MediaType.APPLICATION_JSON)//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .session((MockHttpSession) getLoginSession())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
		//
		// String s = new String();
		// for (int i = 0; i < 70; i++) {
		// s = s + String.valueOf(i);
		// }
		// System.out.println("-----------------------------case5:?????????????????????????????????----------------------------");
		// mr = null;
		// mr = mvc.perform(//
		// post("/inventorySheet/submitEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), s)//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .session((MockHttpSession) getLoginSession())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);

		// System.out.println("-----------------------------case6:int1??????0???1----------------------------");
		// MvcResult mr6 = mvc.perform(//
		// post("/inventorySheet/submitEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), s)//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "3")//
		// .session((MockHttpSession) session)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Assert.assertTrue("".equals(mr6.getResponse().getContentAsString()),
		// "????????????????????????");

		System.out.println("-----------------------------case1:?????????????????????????????????----------------------------");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			inventoryCommodityIDs += inventoryCommodity.getID() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}

		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory7.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory7.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);

		JSONObject o9 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		int staus = JsonPath.read(o9, "$.object.status");
		Assert.assertTrue(staus == 1, "?????????????????????");
		// ?????????
		createInventory7.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr7, createInventory7, Shared.DBName_Test);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		StringBuilder inventoryCommodityIDs = new StringBuilder();
		StringBuilder commNOReals = new StringBuilder();
		StringBuilder commListID = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodityIDs.append(inventoryCommodity.getID() + ",");
			commNOReals.append(new Random().nextInt(200) + ",");
			commListID.append(inventoryCommodity.getCommodityID() + ",");
		}

		System.out.println("-----------------------------case2:?????????????????????????????????----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr8 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "????????????????????????");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		StringBuilder inventoryCommodityIDs = new StringBuilder();
		StringBuilder commNOReals = new StringBuilder();
		StringBuilder commListID = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodityIDs.append(inventoryCommodity.getID() + ",");
			commNOReals.append(new Random().nextInt(200) + ",");
			commListID.append(inventoryCommodity.getCommodityID() + ",");
		}

		System.out.println("-----------------------------case3:?????????????????????????????????----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit9.setApproverID(staff.getID());
		Map<String, Object> approveParam9 = submit9.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit9);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam9);
		InventorySheet approve9 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(approveParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr9 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), approve9.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(approve9.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		StringBuilder inventoryCommodityIDs = new StringBuilder();
		StringBuilder commNOReals = new StringBuilder();
		StringBuilder commListID = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodityIDs.append(inventoryCommodity.getID() + ",");
			commNOReals.append(new Random().nextInt(200) + ",");
			commListID.append(inventoryCommodity.getCommodityID() + ",");
		}
		System.out.println("-----------------------------case4:?????????????????????????????????----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr10 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory10.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		System.out.println("-----------------------------case5:inventoryCommodityIDs ?????????----------------------------");
		InventorySheet createInventory11 = BaseInventorySheetTest.createInventorySheet();
		createInventory11.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities11 = (List<InventoryCommodity>) createInventory11.getListSlave1();
		StringBuilder inventoryCommodityIDs11 = new StringBuilder();
		StringBuilder commNOReals11 = new StringBuilder();
		StringBuilder commListID11 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities11) {
			inventoryCommodityIDs11.append("case11,");
			commNOReals11.append(new Random().nextInt(200) + ",");
			commListID11.append(inventoryCommodity.getCommodityID() + ",");
		}

		MvcResult mr11 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory11.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory11.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs11.toString())//
						.param(KEY_CommNOReals, commNOReals11.toString())//
						.param(KEY_CommListID, commListID11.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_OtherError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		System.out.println("-----------------------------case6:inventoryCommodityIDs ???????????????----------------------------");
		InventorySheet createInventory12 = BaseInventorySheetTest.createInventorySheet();
		createInventory12.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities12 = (List<InventoryCommodity>) createInventory12.getListSlave1();
		StringBuilder inventoryCommodityIDs12 = new StringBuilder();
		StringBuilder commNOReals12 = new StringBuilder();
		StringBuilder commListID12 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs12.append(inventoryCommodity.getID() + ",1,");
			commNOReals12.append(new Random().nextInt(200) + ",");
			commListID12.append(inventoryCommodity.getCommodityID() + ",");
		}

		MvcResult mr12 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory12.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs12.toString())//
						.param(KEY_CommNOReals, commNOReals12.toString())//
						.param(KEY_CommListID, commListID12.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_OtherError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSubmitEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		System.out.println("-----------------------------case7:inventoryCommodityIDs ?????????????????????ID?????????----------------------------");
		InventorySheet createInventory13 = BaseInventorySheetTest.createInventorySheet();
		createInventory13.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities13 = (List<InventoryCommodity>) createInventory13.getListSlave1();
		StringBuilder inventoryCommodityIDs13 = new StringBuilder();
		StringBuilder commNOReals13 = new StringBuilder();
		StringBuilder commListID13 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities13) {
			inventoryCommodityIDs13.append("-20,");
			commNOReals13.append(new Random().nextInt(200) + ",");
			commListID13.append(inventoryCommodity.getCommodityID() + ",");
		}
		MvcResult mr13 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory13.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory13.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs13.toString())//
						.param(KEY_CommNOReals, commNOReals13.toString())//
						.param(KEY_CommListID, commListID13.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testSubmitEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		System.out.println("-----------------------------case8:inventoryCommodityIDs ?????????----------------------------");
		InventorySheet createInventory14 = BaseInventorySheetTest.createInventorySheet();

		MvcResult mr14 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory14.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory14.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testSubmitEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		InventorySheet createInventory14 = BaseInventorySheetTest.createInventorySheet();
		System.out.println("-----------------------------case9:????????????????????????????????????----------------------------");
//		MvcResult mr15 = mvc.perform(//
//				post("/inventorySheet/submitEx.bx")//
//						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory14.getRemark())//
//						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory14.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testSubmitEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		InventorySheet createInventory14 = BaseInventorySheetTest.createInventorySheet();
		System.out.println("-----------------------------case10:???????????????????????????????????????----------------------------");
		MvcResult mr15 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory14.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory14.getID()))//
						.param(KEY_InventoryCommodityIDs, "")//
						.param(KEY_CommNOReals, "")//
						.param(KEY_CommListID, "")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testSubmitEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case11:?????????????????????????????????(??????????????????????????????)----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr8 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "????????????????????????");
	}

	@Test
	public void testSubmitEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case12:???????????????????????????(??????????????????????????????)----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit9.setApproverID(staff.getID());
		Map<String, Object> approveParam9 = submit9.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit9);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam9);
		InventorySheet approve9 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(approveParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr9 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), approve9.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(approve9.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testSubmitEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case13:?????????????????????????????????----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr10 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory10.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateSheetEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// // InventorySheet is = new InventorySheet();
		// MvcResult mr = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
		//
		// JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// List<BaseModel> listSlave1 = JsonPath.read(o, "$.object.listSlave1");
		// Assert.assertTrue(listSlave1 != null, "??????????????????????????????");
		// //
		// List<BaseModel> listCommodity = JsonPath.read(o, "$.object.listCommodity");
		// Assert.assertTrue(listCommodity != null, "????????????????????????");
		// //
		// String warehouseName = JsonPath.read(o, "$.object.warehouse.name");
		// Assert.assertTrue(warehouseName != null, "???????????????????????????");
		//
		// // ????????????????????????????????????????????????????????????
		// verifyCacheCreateResult(mr, inventorySheet,
		// EnumCacheType.ECT_InventorySheet);
		//
		// System.out.println("-----------------------------case1:????????????session----------------------------");
		// try {
		// mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .session(new MockHttpSession())//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Assert.assertTrue(false);
		// } catch (Exception e) {
		// }
		//
		// System.out.println("-----------------------------case3:??????????????????1???session----------------------------");
		// MvcResult mr3 = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession1())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print()).andReturn();
		// Shared.checkJSONErrorCode(mr3);
		// // ????????????????????????????????????????????????????????????
		// verifyCacheCreateResult(mr3, inventorySheet,
		// EnumCacheType.ECT_InventorySheet);
		//
		// System.out.println("-----------------------------case4:??????????????????2???session----------------------------");
		// MvcResult mr4 = null;
		// try {
		// mr4 = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)
		// getLoginSession2())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print()).andReturn();
		// } catch (Exception e) {
		// assertTrue(mr4 == null);
		// }
		//
		// System.out.println("-----------------------------case5:????????????????????????----------------------------");
		// try {
		// mr4 = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession1())//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// } catch (Exception e) {
		// assertTrue(mr4 == null);
		// }
		//
		// String s = new String();
		// for (int i = 0; i < 70; i++) {
		// s = s + String.valueOf(i);
		// }
		// System.out.println("-----------------------------case6:?????????????????????????????????----------------------------");
		// try {
		// mr = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(), s)//
		// .param(InventorySheet.field.getFIELD_NAME_int1(), "1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) getLoginSession())//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// } catch (Exception e) {
		// assertTrue(mr == null);
		// }

		// ???????????????????????????????????????????????????????????????????????????????????? ?????????OK

		System.out.println("-----------------------------case1:?????????????????????????????????????????????????????????????????????????????????----------------------------");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			inventoryCommodityIDs += inventoryCommodity.getID() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		//
		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory7.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory7.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString()).contentType(MediaType.APPLICATION_JSON)//
						// .param(KEY_BarcodeIDs, barcodeIDs.toString())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);
		// ?????????
		InventorySheetCP.verifyUpdate(mr7, createInventory7, Shared.DBName_Test);

		// System.out.println("-----------------------------case17:??????int1 ??????0???1
		// ----------------------------");
		// MvcResult mr17 = mvc.perform(//
		// post("/inventorySheet/updateEx.bx")//
		// .param(InventorySheet.field.getFIELD_NAME_remark(),
		// createInventory16.getRemark())//
		// .param(InventorySheet.field.getFIELD_NAME_ID(),
		// String.valueOf(createInventory16.getID()))//
		// .param(InventorySheet.field.getFIELD_NAME_int1(),
		// "3").contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Assert.assertTrue("".equals(mr17.getResponse().getContentAsString()),
		// "????????????????????????");
	}

	@Test
	public void testUpdateSheetEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// ???????????????????????????????????????????????? ?????????OK
		System.out.println("-----------------------------case2:?????????????????????????????????????????????----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		submit.setRemark("case8");
		MvcResult mr8 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr8);

		// ?????????
		createInventory8.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		createInventory8.setRemark(submit.getRemark());
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (Object bm : createInventory8.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		InventorySheetCP.verifyUpdate(mr8, createInventory8, Shared.DBName_Test);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateSheetEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		StringBuilder inventoryCommodityIDs = new StringBuilder();
		StringBuilder commNOReals = new StringBuilder();
		StringBuilder commListID = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodityIDs.append(inventoryCommodity.getID() + ",");
			commNOReals.append(new Random().nextInt(200) + ",");
			commListID.append(inventoryCommodity.getCommodityID() + ",");
		}
		// ????????????????????????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????
		System.out.println("-----------------------------case3:??????????????????????????????????????????????????????????????????????????????----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		createInventory9.setRemark("case7");
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(submit9 != null && EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit9.setRemark("case9");
		//
		MvcResult mr9 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit9.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit9.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString()).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9);

		// ?????????
		createInventory9.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		createInventory9.setRemark(submit9.getRemark());
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (Object bm : createInventory9.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		InventorySheetCP.verifyUpdate(mr9, createInventory9, Shared.DBName_Test);
	}

	@Test
	public void testUpdateSheetEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// ????????????????????????????????? ?????????EC_NoSuchData
		System.out.println("-----------------------------case4:????????????????????????????????? ----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		MvcResult mr10 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory10.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateSheetEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// ???????????????????????????????????? ?????????EC_BusinessLogicNotDefined
		System.out.println("-----------------------------case5:?????????????????????????????????. ----------------------------");
		InventorySheet createInventory11 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam11 = createInventory11.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory11);
		InventorySheet submit11 = inventorySheetMapper.submit(updateParam11);
		Assert.assertTrue(submit11.getStatus() == 1 && EnumErrorCode.values()[Integer.parseInt(updateParam11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit11.setApproverID(staff.getID());
		Map<String, Object> approveParam11 = submit11.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit11);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam11);
		InventorySheet approve11 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(approve11.getStatus() == 2 && EnumErrorCode.values()[Integer.parseInt(approveParam11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		MvcResult mr11 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), approve11.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(approve11.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateSheetEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// ???????????????inventoryCommodityIDs??????????????????????????????????????????
		System.out.println("-----------------------------case6:???????????????inventoryCommodityIDs????????????. ----------------------------");
		InventorySheet createInventory12 = BaseInventorySheetTest.createInventorySheet();
		List<InventoryCommodity> inventoryCommodities12 = (List<InventoryCommodity>) createInventory12.getListSlave1();
		StringBuilder inventoryCommodityIDs12 = new StringBuilder();
		StringBuilder commNOReals12 = new StringBuilder();
		StringBuilder commListID12 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs12.append("-1,");
			commNOReals12.append(new Random().nextInt(200) + ",");
			commListID12.append(inventoryCommodity.getCommodityID() + ",");
		}
		//
		MvcResult mr12 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory12.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs12.toString())//
						.param(KEY_CommNOReals, commNOReals12.toString())//
						.param(KEY_CommListID, commListID12.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_PartSuccess);
		// ..?????????????????????????????????????????????????????????????????????
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateSheetEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		InventorySheet createInventory12 = BaseInventorySheetTest.createInventorySheet();
		List<InventoryCommodity> inventoryCommodities12 = (List<InventoryCommodity>) createInventory12.getListSlave1();
		StringBuilder inventoryCommodityIDs12 = new StringBuilder();
		StringBuilder commNOReals12 = new StringBuilder();
		StringBuilder commListID12 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs12.append("-1,");
			commNOReals12.append(new Random().nextInt(200) + ",");
			commListID12.append(inventoryCommodity.getCommodityID() + ",");
		}
		// ???????????????inventoryCommodityIDs???????????? ?????????""
		System.out.println("-----------------------------case7:???????????????inventoryCommodityIDs?????????. ----------------------------");
		StringBuilder inventoryCommodityIDs13 = new StringBuilder();
		StringBuilder commNOReals13 = new StringBuilder();
		StringBuilder commListID13 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs13.append("case13,");
			commNOReals13.append(new Random().nextInt(200) + ",");
			commListID13.append(inventoryCommodity.getCommodityID() + ",");
		}
		//
		MvcResult mr13 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory12.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs13.toString())//
						.param(KEY_CommNOReals, commNOReals13.toString())//
						.param(KEY_CommListID, commListID13.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_OtherError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateSheetEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		InventorySheet createInventory12 = BaseInventorySheetTest.createInventorySheet();
		List<InventoryCommodity> inventoryCommodities12 = (List<InventoryCommodity>) createInventory12.getListSlave1();
		StringBuilder inventoryCommodityIDs12 = new StringBuilder();
		StringBuilder commNOReals12 = new StringBuilder();
		StringBuilder commListID12 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs12.append("-1,");
			commNOReals12.append(new Random().nextInt(200) + ",");
			commListID12.append(inventoryCommodity.getCommodityID() + ",");
		}
		// ???????????????????????????????????????????????????inventoryCommodityIDs?????????commNOReals?????????????????? ?????????""
		System.out.println("-----------------------------case8:???????????????????????????????????????????????????inventoryCommodityIDs?????????commNOReals???????????????. ----------------------------");
		StringBuilder inventoryCommodityIDs14 = new StringBuilder();
		StringBuilder commNOReals14 = new StringBuilder();
		StringBuilder commListID14 = new StringBuilder();
		for (InventoryCommodity inventoryCommodity : inventoryCommodities12) {
			inventoryCommodityIDs14.append(inventoryCommodity.getID() + ",1,");
			commNOReals14.append(new Random().nextInt(200) + ",");
			commListID14.append(inventoryCommodity.getCommodityID() + ",");
		}
		MvcResult mr14 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory12.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs14.toString())//
						.param(KEY_CommNOReals, commNOReals14.toString())//
						.param(KEY_CommListID, commListID14.toString())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUpdateSheetEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		// //sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// ????????????2??????????????? ?????????EC_NoPermission.
		System.out.println("-----------------------------case9:????????????2???????????????. ----------------------------");
		InventorySheet createInventory15 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam15 = createInventory15.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory15);
		InventorySheet submit15 = inventorySheetMapper.submit(updateParam15);
		Assert.assertTrue(submit15 != null && EnumErrorCode.values()[Integer.parseInt(updateParam15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		submit15.setRemark("case9");
//		MvcResult mr15 = mvc.perform(//
//				post("/inventorySheet/updateEx.bx")//
//						.param(InventorySheet.field.getFIELD_NAME_remark(), submit15.getRemark())//
//						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit15.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateSheetEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		// //sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 12 ??????????????????0??????????????????inventoryCommodityIDs
		System.out.println("-----------------------------case10:??????????????????0?????????????????????. ----------------------------");
		InventorySheet createInventory16 = BaseInventorySheetTest.createInventorySheet();

		MvcResult mr16 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory16.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory16.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUpdateSheetEx11() throws Exception {
		Shared.caseLog("Case11:??????A??????????????????????????????B?????????????????????????????????????????????????????????");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();

		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		//
		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory7.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory7.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString()).contentType(MediaType.APPLICATION_JSON)//
						// .param(KEY_BarcodeIDs, barcodeIDs.toString())//
						.session((MockHttpSession) sessionNewShopBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testUpdateSheetEx12() throws Exception {
		Shared.caseLog("Case12:??????A??????????????????????????????B(????????????)????????????????????????????????????");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();

		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		//
		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory7.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory7.getID()))//
						.param(KEY_InventoryCommodityIDs, inventoryCommodityIDs.toString())//
						.param(KEY_CommNOReals, commNOReals.toString())//
						.param(KEY_CommListID, commListID.toString()).contentType(MediaType.APPLICATION_JSON)//
						// .param(KEY_BarcodeIDs, barcodeIDs.toString())//
						.session((MockHttpSession) sessionNewShopBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);
	}

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();
		mvc.perform(//
				get("/inventorySheet.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testApproveEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// //sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// HttpSession session = getLoginSession();
		System.out.println("-----------------------------case1:?????????1????????????----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case7");

		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????????????????????????????????????????Wx????????????????????????
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// ?????????
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (Object bm : createInventory.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		createInventory.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		createInventory.setApproverID(4);
		createInventory.setRemark(submit.getRemark());
		InventorySheetCP.verifyApprove(mr, createInventory, messageBO, Shared.DBName_Test);

		//
		System.out.println("------------------------------ case7 ????????????????????????????????????");
		int ID = JsonPath.read(o, "$.object.ID");// ????????????????????????????????????
		MvcResult mr7 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case7");

		System.out.println("-----------------------------case2????????????ID????????????----------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case3????????????????????????????????????????????????----------------------------");
		// 2.?????????????????????????????????????????????
		InventorySheet createInventory3 = BaseInventorySheetTest.createInventorySheet();
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory3.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory3.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 4.???????????????????????????????????????.
		System.out.println("-----------------------------case4??????????????????????????????????????????----------------------------");
		InventorySheet createInventory4 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam4 = createInventory4.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory4);
		inventorySheetMapper.delete(deleteParam4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory4.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory4.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 5.????????????????????????????????????
		System.out.println("-----------------------------case5???????????????????????????????????????----------------------------");
//		InventorySheet createInventory5 = BaseInventorySheetTest.createInventorySheet();
//		MvcResult mr5 = mvc.perform(//
//				post("/inventorySheet/approveEx.bx")//
//						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory5.getRemark())//
//						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory5.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testApproveEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 6.???????????????????????????100??????.
		System.out.println("------------------------------ case6 ???????????????????????????100???------------------------");
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 70; i++) {
			s.append(String.valueOf(i));
		}

		InventorySheet createInventory6 = BaseInventorySheetTest.createInventorySheet();
		createInventory6.setRemark(s.toString());
		MvcResult mr6 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), createInventory6.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(createInventory6.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// HttpSession session = getLoginSession();
		System.out.println("-----------------------------case7:?????????1????????????,?????????????????????????????????,??????????????????????????????----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case7");

		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// Shared.checkJSONErrorCode(mr);
		// ????????????????????????????????????????????????Wx????????????????????????
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// ?????????
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (Object bm : createInventory.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		createInventory.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		createInventory.setApproverID(4);
		createInventory.setRemark(submit.getRemark());
		InventorySheetCP.verifyApprove(mr, createInventory, messageBO, Shared.DBName_Test);
		// ?????????????????????????????????,??????????????????????????????????????????????????????????????????????????????????????????
		// String msg = o.getString(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("????????????????????????!"));
	}

	@Test
	public void testApproveEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case8:????????????????????????????????????----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case8");
		// ???????????????????????????
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(submit.getID());
		//
		Map<String, Object> params = ic.getDeleteParam(BaseBO.INVALID_CASE_ID, ic);
		String err = ic.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		inventoryCommodityMapper.delete(params);
		//
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testApproveEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:??????A??????????????????????????????B?????????????????????????????????????????????????????????");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case7");
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionNewShopBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????????????????????????????????????????Wx????????????????????????
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:??????A??????????????????????????????B(????????????)??????????????????????????????????????????????????????????????????????????????????????????");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		submit.setRemark("case7");
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), submit.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(submit.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionNewShopBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????????????????????????????????????????Wx????????????????????????
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// InventorySheet is = new InventorySheet();
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ?????????
		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setWarehouseID(1);
		inventorySheet.setScope(1);
		inventorySheet.setStaffID(2);
		inventorySheet.setShopID(Shared.DEFAULT_Shop_ID);
		// ???????????????status???????????????
		// inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		inventorySheet.setRemark("....");
		inventorySheet.setMessageID(1);
		InventorySheetCP.verifyCreate(mr, inventorySheet, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case2:???commListID???-1?????????????????????????????????----------------------------");

		MvcResult mr2 = mvc.perform(post("/inventorySheet/createEx.bx") //
				.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
				.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
				.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
				.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
				.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
				.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
				.param(KEY_CommListID, "-1")//
				.param(KEY_BarcodeIDs, "1") //
				.param(KEY_CommNOReals, "1")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Assert.assertTrue("".equals(mr2.getResponse().getContentAsString()), "action ????????????????????????");
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case3:commListID????????????int????????????????????????----------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/createEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "??????")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr3.getResponse().getContentAsString()), "action ????????????????????????");
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case4???commListID?????????????????????ID???----------------------------");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, String.valueOf(Shared.BIG_ID))//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
		// ..?????????????????????????????????????????????????????????????????????
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case5??????????????????????????????----------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_ID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "1,1")//
						.param(KEY_BarcodeIDs, "1,2") //
						.param(KEY_CommNOReals, "1,2")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_PartSuccess);
		// ..?????????????????????????????????????????????????????????????????????
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case6:?????????????????????????????????ID?????????????????????ID----------------------------");
		MvcResult mr6 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateE7() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case7:???????????????????????????staffID???????????????staffID----------------------------");
		MvcResult mr7 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateE8() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????????????????
		System.out.println("-----------------------------case8:???????????????????????????----------------------------");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr8 = mvc.perform(//
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "2")//
						.param(KEY_BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "action ????????????????????????");
	}

	@Test
	public void testCreateE9() throws Exception {
		Shared.printTestMethodStartInfo();

		// 4)??????????????????????????????-1
		System.out.println("-----------------------------case9:??????????????????????????????-1----------------------------");
		String remark = "...xxx.";
		MvcResult mr9 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), remark)//
						.param(KEY_CommListID, "2")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "-1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9);

		String json = mr9.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		int id = JsonPath.read(o, "$.object.ID");

		InventorySheet is9 = new InventorySheet();
		is9.setID(id);
		Map<String, Object> retrieve1Param = is9.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, is9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> retrieve1List = inventorySheetMapper.retrieve1Ex(retrieve1Param);
		Assert.assertTrue(retrieve1List != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		if (retrieve1List.get(0).size() != 0) {
			InventorySheet is = (InventorySheet) retrieve1List.get(0).get(0);
			Assert.assertTrue(remark.equals(is.getRemark()), "???????????????????????????????????????");
		}
		// ????????????????????????????????????????????????????????????
		verifyCacheCreateResult(mr9, inventorySheet, EnumCacheType.ECT_InventorySheet);

		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setWarehouseID(1);
		inventorySheet.setScope(1);
		inventorySheet.setStaffID(2);
		// ???????????????status???????????????
		// inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		inventorySheet.setRemark(remark);
		inventorySheet.setMessageID(1);
		inventorySheet.setShopID(Shared.DEFAULT_Shop_ID);
		InventorySheetCP.verifyCreate(mr9, inventorySheet, Shared.DBName_Test);
	}

	@Test
	public void testCreateE10() throws Exception {
		Shared.printTestMethodStartInfo();

		// 5)????????????????????????????????????????????????????????????.
		System.out.println("-----------------------------case10:????????????????????????????????????????????????????????????.----------------------------");
		MvcResult mr10 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "1000")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "2,1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr10.getResponse().getContentAsString()), "action ????????????????????????");
	}

	@Test
	public void testCreateE11() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case11:?????????????????????????????????----------------------------");
		MvcResult mr11 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "1")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "??????")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr11.getResponse().getContentAsString()), "action ????????????????????????");
	}

	@Test
	public void testCreateE12() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case12:?????????????????????????????????ID?????????????????????ID(????????????)----------------------------");
		MvcResult mr12 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(BigNumber_Test)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateE13() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case13:???????????????????????????staffID???????????????staffID(????????????)----------------------------");
		MvcResult mr13 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(BigNumber_Test)) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case14???????????????????????????????????????????????????----------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_ID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "166")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ..?????????????????????????????????????????????????????????????????????
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15:????????????????????????????????????");
		// InventorySheet is = new InventorySheet();
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE15?????????????????????????????????null");
	}

	@Test
	public void testCreateE16() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case16:???????????????ID???????????????----------------------------");
		MvcResult mr13 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.VirtualShopID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17:??????A?????????????????????B?????????????????????????????????????????????");
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		// InventorySheet is = new InventorySheet();
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:??????A(????????????)?????????????????????B????????????????????????");
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		// InventorySheet is = new InventorySheet();
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2") //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "....") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testRetrieveNByFieldsEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1:?????????????????????????????????----------------------------");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "???????????????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");

		System.out.println("1=" + list);
		String string = "???????????????";
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			if (s1.equals(string)) {
				break;
			}
			if (i == list.size() - 1) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + string);
			}
		}

		checkBarcodes(mr);

		System.out.println("-----------------------------case2:??????????????????????????????????????????----------------------------");
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "???????????????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		checkBarcodes(mr1);

		System.out.println("-----------------------------case3:??????????????????(10)?????????????????????????????????----------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "PD20190605")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<String> list2 = JsonPath.read(o2, "$.objectList[*].sn");

		for (int i = 0; i < list2.size(); i++) {
			String SN = list2.get(i);
			Assert.assertTrue(SN.contains("PD20190605"), "??????????????????:" + SN + "??????????????????");
		}

		checkBarcodes(mr2);

		System.out.println("-----------------------------case4:??????????????????(10)?????????????????????????????????----------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "PD2019060")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString()); //
		List<String> list3 = JsonPath.read(o3, "$.objectList[*].sn");
		Assert.assertTrue(list3.size() == 0, "????????????,CASE4??????????????????????????????");

		checkBarcodes(mr3);

		System.out.println("-----------------------------case5:??????????????????(20)?????????????????????????????????----------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "PD20190605123451234512345")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);

		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<String> list4 = JsonPath.read(o4, "$.objectList[*].sn");
		Assert.assertTrue(list4.size() == 0, "????????????,CASE5??????????????????????????????");

		checkBarcodes(mr4);

		System.out.println("-----------------------------case6:??????????????????(20)?????????????????????????????????----------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "PD201906051234512345")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString()); //
		List<String> list5 = JsonPath.read(o5, "$.objectList[*].sn");
		Assert.assertTrue(list5.size() == 0, "????????????,CASE5??????????????????????????????");

		checkBarcodes(mr5);
	}

	@Test
	public void testRetrieveNByFieldsEx1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------Case25?????????queryKeyword??????_????????????????????????????????? -----------");
		// ????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("??????????????????1111_54???" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = retrieveNBarcodes(commodity1);
		// ?????????????????????????????????
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/createEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "4")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, String.valueOf(commodity1.getID()))//
						.param(KEY_BarcodeIDs, String.valueOf(barcodes1.getID())) //
						.param(KEY_CommNOReals, "1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr5);

		// ?????????????????????
		MvcResult mr6 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "1_54")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr6);

		JSONObject object = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<String> isListName = JsonPath.read(object, "$.objectList[*].listCommodity[*].name");
		System.out.println();
		for (String result : isListName) {
			Assert.assertTrue(result.contains("1_54???"), "??????????????????????????????_?????????");
		}
	}

	@SuppressWarnings("unchecked")
	protected Barcodes retrieveNBarcodes(Commodity commCreate) throws Exception, UnsupportedEncodingException {
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=" + commCreate.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);

		JSONObject o2 = JSONObject.fromObject(mrl2.getResponse().getContentAsString());
		JSONArray barJSONArray = o2.getJSONArray("barcodesList");
		Barcodes barcodes = new Barcodes();
		List<Barcodes> list = (List<Barcodes>) barcodes.parseN(barJSONArray);
		for (Barcodes b : list) {
			assertTrue(b.getCommodityID() == commCreate.getID());
		}

		return list.get(0);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1:?????????0????????????----------------------------");
		InventorySheet is1 = BaseInventorySheetTest.createInventorySheet();
		MvcResult mr = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + is1.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		InventorySheet itDelete = (InventorySheet) is1.clone();
		InventorySheetCP.verifyDelete(itDelete, inventorySheetMapper, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case2:?????????1????????????----------------------------");
		InventorySheet is = BaseInventorySheetTest.createInventorySheet();
		is = BaseInventorySheetTest.submitInventorySheet(is, EnumErrorCode.EC_NoError);

		MvcResult mr1 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + is.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case3:?????????2????????????----------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=8")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case4:?????????3???????????????????????????????????????7----------------------------");
		MvcResult mr3 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=11")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case5???????????????????????????----------------------------");
//		InventorySheet is2 = BaseInventorySheetTest.createInventorySheet();
//		MvcResult mr4 = mvc.perform(//
//				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + is2.getID())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	// private InventorySheet createInventorySheet() {
	// inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex());
	// inventorySheet.setRemark("aaaaaaaa1");
	// inventorySheet.setWarehouseID(1);
	// inventorySheet.setScope(1);
	// inventorySheet.setStaffID(3);
	// //
	// Map<String, Object> params =
	// inventorySheet.getCreateParam(BaseBO.INVALID_CASE_ID, inventorySheet);
	//
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// InventorySheet inventorySheet1 = (InventorySheet)
	// inventorySheetMapper.create(params);// ...
	//
	// // Assert.assertEquals(inventorySheet1.compareTo(inventorySheet), 0);
	// inventorySheet.setIgnoreIDInComparision(true);
	// if (inventorySheet.compareTo(inventorySheet1) != 0) {
	// Assert.assertTrue(false, "???????????????????????????DB??????????????????");
	// }
	// System.out.println("iIDFromCreat1???" + inventorySheet1.getID());
	// //
	// System.out.println("iErrorCode????????????" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	//
	// return inventorySheet1;
	// }

	// @Test
	// public void testDeleteListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:??????1????????????------------------------");
	// InventorySheet is1 = createInventory();
	//
	// MvcResult mr = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=" + is1.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:?????????????????????------------------------------");
	// InventorySheet is2 = createInventory();
	// InventorySheet is3 = createInventory();
	//
	// MvcResult mr2 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=" + is2.getID() +
	// "," + is3.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:????????????????????????????????????------------------------------");
	// String msg1 = "?????????" + IS_STATUS2_ID + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=9") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case4:????????????????????????????????????------------------------------");
	// String msg2 = "?????????" + 11 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=9,11") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "????????????????????????");
	//
	// System.out.println("------------------------------Case5:??????0????????????-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6???POListID???ID???????????????????????????---------------");
	// String msg = "?????????" + 999999999 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr6 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=" + 999999999) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr6, msg, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case7:????????????????????????????????????????????????------------------------------");
	// InventorySheet is4 = createInventory();
	// MvcResult mr7 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=" + IS_STATUS2_ID +
	// "," + is4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "????????????????????????");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:????????????????????????------------------------------");
	// InventorySheet is5 = createInventory();
	// String msg3 = "?????????????????????????????????<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/inventorySheet/deleteListEx.bx?inventorySheetListID=" + is5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfManager)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "????????????????????????");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();
		// InventorySheet inventorySheet = new InventorySheet();

		// System.out.println("-----------------------------case1:????????????----------------------------");
		// mvc.perform(//
		// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
		// + "=2")//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk());???????????????????????????

		// System.out.println("-----------------------------case2:????????????????????????,?????????????????????----------------------------");???????????????????????????
		// try {
		// mvc.perform(//
		// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
		// + "=11")//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk());
		// } catch (Exception e) {
		// assertTrue(true);
		// }
	}

	// ????????????????????????????????????????????????
	// @Test
	public void testApproveExSendMsgToWX() throws Exception {
		System.out.println("------------------------------case1:?????????1????????????,???????????????????????????????????????????????????------------------------------");
		// InventorySheet is = new InventorySheet();
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "11")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) getLoginSession1())//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		assertTrue(jsonObject.getInt(BaseWxModel.WX_ERRCODE) == 0 && jsonObject.getString(BaseWxModel.WX_ERRMSG).equals("ok"),
				"??????????????????????????????????????????" + jsonObject.getInt(BaseWxModel.WX_ERRCODE) + ",??????????????????" + jsonObject.getString(BaseWxModel.WX_ERRMSG) + "\t");
	}

	/** ???????????????????????????????????????????????????????????????????????????????????? */
	public void verifyCacheCreateResult(MvcResult mr, BaseModel bmCreateObjet, EnumCacheType ect) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel bmOfDB = bmCreateObjet.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(bmCreateObjet) == 0, "????????????");
		// ?????????????????????????????????C????????????
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(Shared.DBName_Test, ect).read1(bmOfDB.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm != null && bm.getID() == bmOfDB.getID(), "????????????????????????????????????????????????");
		Assert.assertTrue(!CollectionUtils.isEmpty(bm.getListSlave1()), "???????????????????????????????????????");
		for (int i = 0; i < bmOfDB.getListSlave1().size(); i++) {
			Assert.assertTrue(((InventoryCommodity) bm.getListSlave1().get(i)).getInventorySheetID() == ((InventoryCommodity) bmOfDB.getListSlave1().get(i)).getInventorySheetID(), "????????????????????????????????????????????????????????????????????????");
		}
	}
}
