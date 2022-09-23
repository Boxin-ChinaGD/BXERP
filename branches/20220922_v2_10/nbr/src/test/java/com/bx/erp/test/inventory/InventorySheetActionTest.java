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

		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	// 新建一个盘点单与盘点单商品session，状态为0
	// private HttpSession getLoginSession() throws Exception {
	// 创建盘点单
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

	// 新建一个盘点单与盘点单商品session，并提交，状态为1
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
		// .andReturn();现阶段前端没有使用

		return sessionBoss;
	}

	// 状态为2的session
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
	// .andExpect(status().isOk());// ...全部检查错误码
	// }

	protected void checkBarcodes(MvcResult mr) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> barcodesList = JsonPath.read(o, "$.objectList[*].listSlave1[*].barcodes");
		for (String barcodes : barcodesList) {
			if (barcodes == null) {
				Assert.assertTrue(false, "barcodes中无商品的条形码");
			}
		}
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1：状态值为0----------------------------");
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

		System.out.println("-----------------------------case2：状态值为1----------------------------");
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

		System.out.println("-----------------------------case3：状态值为2----------------------------");
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

		System.out.println("-----------------------------case4：状态值为-1----------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "-1")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		System.out.println("-----------------------------case5：状态值为3----------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "3")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("-----------------------------case6：状态值为1000----------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1000")//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1").session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("-----------------------------case7：没有权限进行操作---------------------------------");
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

		System.out.println("-----------------------------case8：根据门店ID查询----------------------------");
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
		// System.out.println("-----------------------------case1:状态为0提交失败----------------------------");
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
		// Assert.assertTrue(listSlave1 != null, "未加载出盘点单商品表");
		// //
		// List<BaseModel> listCommodity = JsonPath.read(o, "$.object.listCommodity");
		// Assert.assertTrue(listCommodity != null, "未加载出商品列表");
		// //
		// String warehouseName = JsonPath.read(o, "$.object.warehouse.name");
		// Assert.assertTrue(warehouseName != null, "未加载出相应的仓库");
		//
		// System.out.println("-----------------------------case2:状态为1提交失败----------------------------");
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
		// System.out.println("-----------------------------case3:状态为2修改失败进行回滚----------------------------");
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
		// System.out.println("-----------------------------case4:没有传入盘点总结----------------------------");
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
		// System.out.println("-----------------------------case5:传入盘点总结大于一百位----------------------------");
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

		// System.out.println("-----------------------------case6:int1不为0或1----------------------------");
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
		// "返回的数据不正确");

		System.out.println("-----------------------------case1:使用待录入状态的盘点单----------------------------");
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
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		Assert.assertTrue(staus == 1, "盘点单提交失败");
		// 检查点
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

		System.out.println("-----------------------------case2:使用已提交状态的盘点单----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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

		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "返回的数据不正确");
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

		System.out.println("-----------------------------case3:使用已审核状态的盘点单----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit9.setApproverID(staff.getID());
		Map<String, Object> approveParam9 = submit9.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit9);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam9);
		InventorySheet approve9 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(approveParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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
		System.out.println("-----------------------------case4:使用已删除状态的盘点单----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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
		System.out.println("-----------------------------case5:inventoryCommodityIDs 带中文----------------------------");
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
		System.out.println("-----------------------------case6:inventoryCommodityIDs 维度不一致----------------------------");
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
		System.out.println("-----------------------------case7:inventoryCommodityIDs 传递的盘点商品ID不存在----------------------------");
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
		System.out.println("-----------------------------case8:inventoryCommodityIDs 不传递----------------------------");
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
		System.out.println("-----------------------------case9:使用无权限的员工进行操作----------------------------");
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
		System.out.println("-----------------------------case10:提交没有盘点单商品的盘点单----------------------------");
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

		System.out.println("-----------------------------case11:使用已提交状态的盘点单(提交时没有传商品信息)----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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

		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "返回的数据不正确");
	}

	@Test
	public void testSubmitEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case12:提交已审核的盘点单(提交时没有传商品信息)----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit9.setApproverID(staff.getID());
		Map<String, Object> approveParam9 = submit9.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit9);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam9);
		InventorySheet approve9 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(approveParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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

		System.out.println("-----------------------------case13:使用已删除状态的盘点单----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");

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
		// Assert.assertTrue(listSlave1 != null, "未加载出盘点单商品表");
		// //
		// List<BaseModel> listCommodity = JsonPath.read(o, "$.object.listCommodity");
		// Assert.assertTrue(listCommodity != null, "未加载出商品列表");
		// //
		// String warehouseName = JsonPath.read(o, "$.object.warehouse.name");
		// Assert.assertTrue(warehouseName != null, "未加载出相应的仓库");
		//
		// // 检验缓存中是否将主表中从表的数据一并写入
		// verifyCacheCreateResult(mr, inventorySheet,
		// EnumCacheType.ECT_InventorySheet);
		//
		// System.out.println("-----------------------------case1:传入空的session----------------------------");
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
		// System.out.println("-----------------------------case3:传入状态值为1的session----------------------------");
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
		// // 检验缓存中是否将主表中从表的数据一并写入
		// verifyCacheCreateResult(mr3, inventorySheet,
		// EnumCacheType.ECT_InventorySheet);
		//
		// System.out.println("-----------------------------case4:传入状态值为2的session----------------------------");
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
		// System.out.println("-----------------------------case5:没有传入盘点总结----------------------------");
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
		// System.out.println("-----------------------------case6:传入盘点总结大于一百位----------------------------");
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

		// 修改处于待录入状态的盘点单的总结和盘点单商品的实盘数量。 结果：OK

		System.out.println("-----------------------------case1:修改处于待录入状态的盘点单的总结和盘点单商品的实盘数量----------------------------");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();
		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities7) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			inventoryCommodityIDs += inventoryCommodity.getID() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		InventorySheetCP.verifyUpdate(mr7, createInventory7, Shared.DBName_Test);

		// System.out.println("-----------------------------case17:传递int1 不为0或1
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
		// "返回的信息不正确");
	}

	@Test
	public void testUpdateSheetEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 修改处于已提交状态盘点单的总结。 结果：OK
		System.out.println("-----------------------------case2:修改处于已提交状态盘点单的总结----------------------------");
		InventorySheet createInventory8 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam8 = createInventory8.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory8);
		InventorySheet submit = inventorySheetMapper.submit(updateParam8);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
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

		// 检查点
		createInventory8.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		createInventory8.setRemark(submit.getRemark());
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (Object bm : createInventory8.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 修改处于已提交状态盘点单的总结和盘点单商品的实盘数量。 结果：总结被修改。但是盘点单商品的实盘数量未被修改
		System.out.println("-----------------------------case3:修改处于已提交状态盘点单的总结和盘点单商品的实盘数量----------------------------");
		InventorySheet createInventory9 = BaseInventorySheetTest.createInventorySheet();
		createInventory9.setRemark("case7");
		Map<String, Object> updateParam9 = createInventory9.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory9);
		InventorySheet submit9 = inventorySheetMapper.submit(updateParam9);
		Assert.assertTrue(submit9 != null && EnumErrorCode.values()[Integer.parseInt(updateParam9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
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

		// 检查点
		createInventory9.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		createInventory9.setRemark(submit9.getRemark());
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (Object bm : createInventory9.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		InventorySheetCP.verifyUpdate(mr9, createInventory9, Shared.DBName_Test);
	}

	@Test
	public void testUpdateSheetEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 修改的盘点单为删除的。 结果：EC_NoSuchData
		System.out.println("-----------------------------case4:修改的盘点单为删除的。 ----------------------------");
		InventorySheet createInventory10 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam10 = createInventory10.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory10);
		inventorySheetMapper.delete(deleteParam10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");
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
		// 修改的盘点单位已审核的。 结果：EC_BusinessLogicNotDefined
		System.out.println("-----------------------------case5:修改的盘点单位已审核的. ----------------------------");
		InventorySheet createInventory11 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam11 = createInventory11.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory11);
		InventorySheet submit11 = inventorySheetMapper.submit(updateParam11);
		Assert.assertTrue(submit11.getStatus() == 1 && EnumErrorCode.values()[Integer.parseInt(updateParam11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit11.setApproverID(staff.getID());
		Map<String, Object> approveParam11 = submit11.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submit11);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParam11);
		InventorySheet approve11 = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(approve11.getStatus() == 2 && EnumErrorCode.values()[Integer.parseInt(approveParam11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "审核对象失败");
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
		// 传入的参数inventoryCommodityIDs为负数。结果：抛出运行时异常
		System.out.println("-----------------------------case6:传入的参数inventoryCommodityIDs为负数。. ----------------------------");
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
		// ..部分成功现在只需检查错误码，将来需要检查点检查
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
		// 传入的参数inventoryCommodityIDs为中文。 结果：""
		System.out.println("-----------------------------case7:传入的参数inventoryCommodityIDs为中文. ----------------------------");
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
		// 修改待录入状态的盘点单时传入的参数inventoryCommodityIDs维度和commNOReals维度不一致。 结果：""
		System.out.println("-----------------------------case8:修改待录入状态的盘点单时传入的参数inventoryCommodityIDs维度和commNOReals维度不一致. ----------------------------");
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
		// 使用员工2进行操作。 结果：EC_NoPermission.
		System.out.println("-----------------------------case9:使用员工2进行操作。. ----------------------------");
		InventorySheet createInventory15 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam15 = createInventory15.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory15);
		InventorySheet submit15 = inventorySheetMapper.submit(updateParam15);
		Assert.assertTrue(submit15 != null && EnumErrorCode.values()[Integer.parseInt(updateParam15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
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
		// 12 盘点单状态为0时未传递参数inventoryCommodityIDs
		System.out.println("-----------------------------case10:盘点单状态为0时未传递参数。. ----------------------------");
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
		Shared.caseLog("Case11:门店A的店长创建盘点，门店B的店长进行修改，修改失败，不能跨店修改");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();

		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		Shared.caseLog("Case12:门店A的店长创建盘点，门店B(虚拟总部)的店长进行修改，修改成功");
		InventorySheet createInventory7 = BaseInventorySheetTest.createInventorySheet();

		createInventory7.setRemark("case7");
		List<InventoryCommodity> inventoryCommodities7 = (List<InventoryCommodity>) createInventory7.getListSlave1();
		String inventoryCommodityIDs = "";
		String commNOReals = "";
		String commListID = "";
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		System.out.println("-----------------------------case1:状态为1审核成功----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
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
		// 如果出现部分成功错误码，大概率是Wx消息发送失败造成
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// 检查点
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (Object bm : createInventory.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		createInventory.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		createInventory.setApproverID(4);
		createInventory.setRemark(submit.getRemark());
		InventorySheetCP.verifyApprove(mr, createInventory, messageBO, Shared.DBName_Test);

		//
		System.out.println("------------------------------ case7 使用已审核的盘点进行审核");
		int ID = JsonPath.read(o, "$.object.ID");// 从成功审核的盘点单中获取
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
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit.setRemark("case7");

		System.out.println("-----------------------------case2：不传递ID进行审核----------------------------");
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

		System.out.println("-----------------------------case3：使用待录入状态的盘点单进行审核----------------------------");
		// 2.使用待录入状态的盘点单进行审核
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
		// 4.使用已删除的盘点单进行审核.
		System.out.println("-----------------------------case4：使用已删除的盘点单进行审核----------------------------");
		InventorySheet createInventory4 = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> deleteParam4 = createInventory4.getDeleteParam(BaseBO.INVALID_CASE_ID, createInventory4);
		inventorySheetMapper.delete(deleteParam4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");

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
		// 5.使用无权限的人员进行审核
		System.out.println("-----------------------------case5：使用无权限的人员进行审核----------------------------");
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
		// 6.传递的盘点总结大于100个字.
		System.out.println("------------------------------ case6 修改的盘点总结大于100字------------------------");
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
		System.out.println("-----------------------------case7:状态为1审核成功,需要发送微信消息给老板,验证消息是否发送成功----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
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
		// 如果出现部分成功错误码，大概率是Wx消息发送失败造成
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// 检查点
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (Object bm : createInventory.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		createInventory.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		createInventory.setApproverID(4);
		createInventory.setRemark(submit.getRemark());
		InventorySheetCP.verifyApprove(mr, createInventory, messageBO, Shared.DBName_Test);
		// 发送微信消息失败的验证,如果测试发送微信消息成功，需要设置白名单，注释掉下面两行代码
		// String msg = o.getString(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("发送微信消息失败!"));
	}

	@Test
	public void testApproveEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("-----------------------------case8:审核没有盘点商品的盘点单----------------------------");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit.setRemark("case8");
		// 手动删除盘点单商品
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

		Shared.caseLog("Case9:门店A的店长创建盘点，门店B的店长进行审核，审核失败，不能跨店审核");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit.setRemark("case7");
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		// 如果出现部分成功错误码，大概率是Wx消息发送失败造成
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:门店A的店长创建盘点，门店B(虚拟总部)的店长进行审核，审核成功，虚拟总部店长可以作为多个门店的店长");
		InventorySheet createInventory = BaseInventorySheetTest.createInventorySheet();
		Map<String, Object> updateParam = createInventory.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, createInventory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submit = inventorySheetMapper.submit(updateParam);
		Assert.assertTrue(submit != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "提交对象失败");
		submit.setRemark("case7");
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		// 如果出现部分成功错误码，大概率是Wx消息发送失败造成
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

		// 检查点
		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setWarehouseID(1);
		inventorySheet.setScope(1);
		inventorySheet.setStaffID(2);
		inventorySheet.setShopID(Shared.DEFAULT_Shop_ID);
		// 创建盘点单status字段没用到
		// inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		inventorySheet.setRemark("....");
		inventorySheet.setMessageID(1);
		InventorySheetCP.verifyCreate(mr, inventorySheet, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case2:因commListID为-1而使该请求造成数据回滚----------------------------");

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

		Assert.assertTrue("".equals(mr2.getResponse().getContentAsString()), "action 返回的数据不正确");
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case3:commListID转换不成int类型造成数据回滚----------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/createEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "回滚")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "1").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr3.getResponse().getContentAsString()), "action 返回的数据不正确");
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case4：commListID为商品不存在的ID。----------------------------");
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
		// ..部分成功现在只需检查错误码，将来需要检查点检查
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case5：重复创建盘点商品表----------------------------");
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
		// ..部分成功现在只需检查错误码，将来需要检查点检查
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case6:创建盘点单时传入的仓库ID为不存在的仓库ID----------------------------");
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

		System.out.println("-----------------------------case7:创建盘点单时传入的staffID为不存在的staffID----------------------------");
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

		// 接收的实盘数量为空
		System.out.println("-----------------------------case8:接收的实盘数量为空----------------------------");
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
		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "action 返回的数据不正确");
	}

	@Test
	public void testCreateE9() throws Exception {
		Shared.printTestMethodStartInfo();

		// 4)接收的实盘数量数据为-1
		System.out.println("-----------------------------case9:接收的实盘数量数据为-1----------------------------");
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
		Assert.assertTrue(retrieve1List != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");
		if (retrieve1List.get(0).size() != 0) {
			InventorySheet is = (InventorySheet) retrieve1List.get(0).get(0);
			Assert.assertTrue(remark.equals(is.getRemark()), "创建的对象和查询出的不一致");
		}
		// 检验缓存中是否将主表中从表的数据一并写入
		verifyCacheCreateResult(mr9, inventorySheet, EnumCacheType.ECT_InventorySheet);

		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setWarehouseID(1);
		inventorySheet.setScope(1);
		inventorySheet.setStaffID(2);
		// 创建盘点单status字段没用到
		// inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		inventorySheet.setRemark(remark);
		inventorySheet.setMessageID(1);
		inventorySheet.setShopID(Shared.DEFAULT_Shop_ID);
		InventorySheetCP.verifyCreate(mr9, inventorySheet, Shared.DBName_Test);
	}

	@Test
	public void testCreateE10() throws Exception {
		Shared.printTestMethodStartInfo();

		// 5)接收的实盘数量维度和其他参数的维度不一致.
		System.out.println("-----------------------------case10:接收的实盘数量维度和其他参数的维度不一致.----------------------------");
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
		Assert.assertTrue("".equals(mr10.getResponse().getContentAsString()), "action 返回的数据不正确");
	}

	@Test
	public void testCreateE11() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case11:接收的实盘数量为字符串----------------------------");
		MvcResult mr11 = mvc.perform(//
				post("/inventorySheet/createEx.bx").param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_scope(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), "2")//
						.param(InventorySheet.field.getFIELD_NAME_status(), "1")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...xxx.")//
						.param(KEY_CommListID, "1")//
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "测试")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr11.getResponse().getContentAsString()), "action 返回的数据不正确");
	}

	@Test
	public void testCreateE12() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case12:创建盘点单时传入的仓库ID为不存在的仓库ID(该值很大)----------------------------");
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

		System.out.println("-----------------------------case13:创建盘点单时传入的staffID为不存在的staffID(该值很大)----------------------------");
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

		System.out.println("-----------------------------case14：创建盘点单时，盘点商品为服务商品----------------------------");
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
		// ..部分成功现在只需检查错误码，将来需要检查点检查
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15:创建没有盘点商品的盘点单");
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
		Assert.assertTrue(json.length() == 0, "CASE15测试失败！期望的是返回null");
	}

	@Test
	public void testCreateE16() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case16:传入的门店ID为虚拟总部----------------------------");
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

		Shared.caseLog("Case17:门店A的店长创建门店B的盘点，创建失败，不能跨店创建");
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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

		Shared.caseLog("Case18:门店A(虚拟总部)的店长创建门店B的盘点，创建成功");
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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

		System.out.println("-----------------------------case1:根据商品名称查询盘点单----------------------------");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "可比克薯片")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");

		System.out.println("1=" + list);
		String string = "可比克薯片";
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			if (s1.equals(string)) {
				break;
			}
			if (i == list.size() - 1) {
				Assert.assertTrue(false, "遍历所有的商品名称都未包含:" + string);
			}
		}

		checkBarcodes(mr);

		System.out.println("-----------------------------case2:没有输入商品名称进行模糊查询----------------------------");
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "可比克薯片")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		checkBarcodes(mr1);

		System.out.println("-----------------------------case3:根据最小长度(10)的盘点单单号查询盘点单----------------------------");
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
			Assert.assertTrue(SN.contains("PD20190605"), "该盘点单单号:" + SN + "并不是期望的");
		}

		checkBarcodes(mr2);

		System.out.println("-----------------------------case4:小于最小长度(10)的盘点单单号查询盘点单----------------------------");
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
		Assert.assertTrue(list3.size() == 0, "测试失败,CASE4期望的是查询不到数据");

		checkBarcodes(mr3);

		System.out.println("-----------------------------case5:大于最大长度(20)的盘点单单号查询盘点单----------------------------");
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
		Assert.assertTrue(list4.size() == 0, "测试失败,CASE5期望的是查询不到数据");

		checkBarcodes(mr4);

		System.out.println("-----------------------------case6:等于最大长度(20)的盘点单单号查询盘点单----------------------------");
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
		Assert.assertTrue(list5.size() == 0, "测试失败,CASE5期望的是查询不到数据");

		checkBarcodes(mr5);
	}

	@Test
	public void testRetrieveNByFieldsEx1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------Case25：传入queryKeyword包含_的特殊字符进行模糊搜索 -----------");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("盘点可口可乐1111_54（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = retrieveNBarcodes(commodity1);
		// 创建盘点单和盘点单从表
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
		// 结果验证
		Shared.checkJSONErrorCode(mr5);

		// 模糊查询盘点单
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
		// 结果验正
		Shared.checkJSONErrorCode(mr6);

		JSONObject object = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<String> isListName = JsonPath.read(object, "$.objectList[*].listCommodity[*].name");
		System.out.println();
		for (String result : isListName) {
			Assert.assertTrue(result.contains("1_54（"), "查询商品名称没有包含_字符！");
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

		System.out.println("-----------------------------case1:状态为0正常删除----------------------------");
		InventorySheet is1 = BaseInventorySheetTest.createInventorySheet();
		MvcResult mr = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + is1.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 检查点
		InventorySheet itDelete = (InventorySheet) is1.clone();
		InventorySheetCP.verifyDelete(itDelete, inventorySheetMapper, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case2:状态为1删除失败----------------------------");
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

		System.out.println("-----------------------------case3:状态为2删除失败----------------------------");
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

		System.out.println("-----------------------------case4:状态为3没有进行删除操作，错误码为7----------------------------");
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

		System.out.println("-----------------------------case5：没有权限进行操作----------------------------");
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
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	// System.out.println("iIDFromCreat1：" + inventorySheet1.getID());
	// //
	// System.out.println("iErrorCode的值是：" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	//
	// return inventorySheet1;
	// }

	// @Test
	// public void testDeleteListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:删除1个盘点单------------------------");
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
	// System.out.println("-----------------------------------Case2:删除多个盘点单------------------------------");
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
	// System.out.println("-----------------------------------Case3:删除一个不可删除的盘点单------------------------------");
	// String msg1 = "盘点单" + IS_STATUS2_ID + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:删除多个不可删除的盘点单------------------------------");
	// String msg2 = "盘点单" + 11 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:删除0个盘点单-----------------------");
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
	// System.out.println("-----------------------case6：POListID的ID在盘点单中不存在。---------------");
	// String msg = "盘点单" + 999999999 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr6, msg, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case7:删除多个盘点单，其中一个不可删除------------------------------");
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
	// Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行删除------------------------------");
	// InventorySheet is5 = createInventory();
	// String msg3 = "你没有权限删除盘点单。<br />";
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
	// Shared.checkJSONMsg(mr8, msg3, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();
		// InventorySheet inventorySheet = new InventorySheet();

		// System.out.println("-----------------------------case1:正常查询----------------------------");
		// mvc.perform(//
		// get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID()
		// + "=2")//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk());现阶段前端没有使用

		// System.out.println("-----------------------------case2:查询已删除盘点单,捕获空指针异常----------------------------");现阶段前端没有使用
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

	// 发送盘点差异的微信消息给门店老板
	// @Test
	public void testApproveExSendMsgToWX() throws Exception {
		System.out.println("------------------------------case1:状态为1审核成功,生成盘点差异消息，并发送给门店老板------------------------------");
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
				"发送信息失败！！！错误码为：" + jsonObject.getInt(BaseWxModel.WX_ERRCODE) + ",错误信息为：" + jsonObject.getString(BaseWxModel.WX_ERRMSG) + "\t");
	}

	/** 检查创建出来的对象与普通缓存以及其带有从表的对象是否相同 */
	public void verifyCacheCreateResult(MvcResult mr, BaseModel bmCreateObjet, EnumCacheType ect) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel bmOfDB = bmCreateObjet.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(bmCreateObjet) == 0, "创建失败");
		// 判断普通缓存中是否存在C出的对象
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(Shared.DBName_Test, ect).read1(bmOfDB.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm != null && bm.getID() == bmOfDB.getID(), "普通缓存不存在本次创建出来的对象");
		Assert.assertTrue(!CollectionUtils.isEmpty(bm.getListSlave1()), "主表的从表数据未写入缓存！");
		for (int i = 0; i < bmOfDB.getListSlave1().size(); i++) {
			Assert.assertTrue(((InventoryCommodity) bm.getListSlave1().get(i)).getInventorySheetID() == ((InventoryCommodity) bmOfDB.getListSlave1().get(i)).getInventorySheetID(), "普通缓存中的从表数据与创建或更新时的数据不一致！");
		}
	}
}
