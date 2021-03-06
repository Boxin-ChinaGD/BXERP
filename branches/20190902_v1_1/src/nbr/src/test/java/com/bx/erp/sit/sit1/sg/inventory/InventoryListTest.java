package com.bx.erp.sit.sit1.sg.inventory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

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
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.InventorySheet.EnumStatusInventorySheet;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.InventorySheetCP;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class InventoryListTest extends BaseActionTest {

	private AtomicInteger order;
	private AtomicLong barcode;
	private AtomicInteger commodityOrder;

	protected String bossPhone = "";
	protected String bossPassword = "";
	protected String companySN = "";
	protected String dbName = "";
	protected final String CommonCommodity = "????????????";

	private String commodityID;
	private String inventorySheetID;
	private String inventorySheetListSlaveID;
	private Map<String, List<String>> inventorySheetIDMap = new HashMap<String, List<String>>();
	private Map<String, BaseModel> inventorySheetMap;

	@SuppressWarnings("unused")
	private InventorySheet is = new InventorySheet();
	private String KEY_CommListID = "commListID";
	private String KEY_BarcodeIDs = "barcodeIDs";
	private String KEY_CommNOReals = "commNOReals";
	private String KEY_InventoryCommodityIDs = "inventoryCommodityIDs";

	/** ??????staff???????????????????????????????????? */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	/** ??????????????????1????????????MvcResult ?????????company????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????company???????????? */
	private static MvcResult mvcResult_Company;

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(77);
		barcode = new AtomicLong();
		barcode.set(6821423302512L);
		inventorySheetMap = new HashMap<String, BaseModel>();

		try {
			sessionBoss = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

	@Test
	public void createShopAndCreateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????????????????????????????");
		// ???????????????????????????????????????????????????
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Company companyCreate = new Company();
		companyCreate = (Company) companyCreate.parse1(jsonObject.getString(BaseAction.KEY_Object));
		company.setSN(companyCreate.getSN());
		companySN = companyCreate.getSN();
		inventorySheetMap.put("company", company);
		// ????????????????????????????????????????????????????????????????????????????????????MvcResult??????????????????????????????????????????????????????
		mvcResult_Company = mr1;
	}

	@Test(dependsOnMethods = "createShopAndCreateCommodity")
	public void preSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "??????????????????CRUD???????????????");

		Company company = (Company) inventorySheetMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, company.getSN());
		// ???????????????
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), "1") //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(InventorySheet.EnumScopeInventorySheet.ESIS_WholeShop.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(getStaffFromSession(sessionBoss).getID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), "...") //
						.param(KEY_CommListID, "1") //
						.param(KEY_BarcodeIDs, "1") //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		//

		// ...?????????????????????????????????????????????UD????????????

		// ???????????????????????????
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// ??????????????????????????????
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// ?????????????????????session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	@Test(dependsOnMethods = "preSale")
	public void createInventoryAndOpenPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????1???????????????????????????");

		// ??????????????????
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo("DDDDDDDDDDDD;" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c1, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c1, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity1 = (Commodity) c1.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("commodity1", commodity1);
		commodityID = String.valueOf(commodity1.getID());
		// ??????barcode?????????
		MvcResult mr = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity1.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o2 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> listBarcodes = JsonPath.read(o2, "$.barcodesList[*]");
		Assert.assertTrue(listBarcodes.size() > 0, "?????????????????????~");
		Barcodes barcode1 = new Barcodes();
		barcode1 = (Barcodes) barcode1.parse1(listBarcodes.get(0).toString());
		inventorySheetMap.put("barcode1", barcode1);
		// ???????????????1
		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("???????????????1");
		MvcResult mr2 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark()) //
						// .param(InventorySheet.field.getFIELD_NAME_int1(),
						// String.valueOf(inventorySheet.getInt1()))//
						// ???InventorySheet???InventorySheetAction???????????????int1????????????????????????????????????????????????
						.param(KEY_CommListID, String.valueOf(commodity1.getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(barcode1.getID())) //
						.param(KEY_CommNOReals, "10")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyCreate(mr2, inventorySheet, dbName);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		//
		getInventoryIDAndInventoryCommodityID(mr2);
		inventorySheetMap.put("inventorySheet1", inventorySheet);
		inventorySheetIDMap.put("?????????1", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndOpenPage")
	public void createInventoryAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????2?????????????????????2");

		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("???????????????2");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet, dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject.getString(BaseAction.KEY_Object));
		//
		getInventoryIDAndInventoryCommodityID(mr);
		inventorySheetIDMap.put("?????????2", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
		//
		// ???????????????2
		inventorySheet.setRemark("???????????????2");

		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}

		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet, dbName);
		inventorySheetMap.put("inventorySheet2", inventorySheet);

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndUpdate")
	public void createInventoryDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????3?????????????????????3????????????????????????3");

		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("???????????????3");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		getInventoryIDAndInventoryCommodityID(mr);
		// ????????????
		InventorySheetCP.verifyCreate(mr, inventorySheet, dbName);

		// ???????????????3
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet.setRemark("???????????????3");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ????????????
		InventorySheetCP.verifyUpdate(mr2, inventorySheet, dbName);

		MvcResult mr3 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// ????????????
		InventorySheetCP.verifyDelete(inventorySheet, inventorySheetMapper, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndUpdate")
	public void createInventoryIdentical() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????4?????????????????????????????????????????????5");

		InventorySheet inventorySheet4 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet4.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet4.setRemark("???????????????4");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet4.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet4.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet4.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet4.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet4, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet4 = (InventorySheet) inventorySheet4.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetIDMap.put("?????????4", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
		inventorySheetMap.put("inventorySheet4", inventorySheet4);
		//
		InventorySheet inventorySheet5 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet5.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet5.setRemark("???????????????5????????????4??????");
		MvcResult mr2 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet5.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet5.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet5.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet5.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyCreate(mr2, inventorySheet5, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		inventorySheet5 = (InventorySheet) inventorySheet5.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet5", inventorySheet5);
		inventorySheetIDMap.put("?????????5", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryIdentical")
	public void submitInventory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "??????????????????6????????????????????????????????????");
		// ???????????????
		InventorySheet inventorySheet6 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet6.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet6.setRemark("???????????????6");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet6.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet6.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet6.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet6.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet6, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet6 = (InventorySheet) inventorySheet6.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet6.setRemark("???????????????6");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet6.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet6.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet6, dbName);

		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet6.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject2 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		inventorySheet6 = (InventorySheet) inventorySheet6.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet6", inventorySheet6);
		// ????????????
		inventorySheet6.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet6, dbName);
	}

	@Test(dependsOnMethods = "submitInventory")
	public void approveInventory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "??????????????????????????????6?????????????????????????????????");

		InventorySheet inventorySheet6 = (InventorySheet) inventorySheetMap.get("inventorySheet6");
		inventorySheet6.setRemark("???????????????6");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet6.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(inventorySheet6.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);  //????????????????????????????????????????????????????????????????????????????????????
		// ????????????
		for (Object bm : inventorySheet6.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet6.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet6.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet6, messageBO, dbName);
	}

	@Test(dependsOnMethods = "approveInventory")
	public void alreadyDeleted() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????");

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "alreadyDeleted")
	public void createInventoryAndSumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????7?????????????????????7");

		// ???????????????
		InventorySheet inventorySheet7 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet7.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet7.setRemark("???????????????7");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet7.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet7.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet7.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet7.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet7, dbName);
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet7 = (InventorySheet) inventorySheet7.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet7.setRemark("???????????????6");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet7.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet7.setRemark("???????????????7");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet7.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet7.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet7, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndSumbit")
	public void createInventoryAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????8?????????????????????8");

		// ???????????????
		InventorySheet inventorySheet8 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet8.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet8.setRemark("???????????????8");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet8.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet8.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet8.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet8.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ...????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet8, dbName);
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet8 = (InventorySheet) inventorySheet8.parse1(jsonObject.getString(BaseAction.KEY_Object));

		// ???????????????
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyDelete(inventorySheet8, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndDelete")
	public void createInventoryAndUpdate_Sumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????9?????????????????????9?????????????????????9");

		// ???????????????
		InventorySheet inventorySheet9 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet9.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet9.setRemark("???????????????9");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet9.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet9.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet9.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet9.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet9, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet9 = (InventorySheet) inventorySheet9.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet9.setRemark("???????????????9");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet9.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet9.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet9, dbName);

		// ???????????????
		inventorySheet9.setRemark("???????????????9");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet9.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ... ????????????
		Shared.checkJSONErrorCode(mr3);
		inventorySheet9.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet9, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndUpdate_Sumbit")
	public void inventorySheetBucket() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????10?????????????????????10??????????????????10?????????????????????10");

		// ???????????????
		InventorySheet inventorySheet10 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet10.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet10.setRemark("???????????????10");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet10.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet10.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet10.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet10.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet10, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet10 = (InventorySheet) inventorySheet10.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet10.setRemark("???????????????10");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet10.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet10, dbName);

		// ???????????????
		inventorySheet10.setRemark("???????????????10");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		inventorySheet10.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet10, dbName);

		// ???????????????
		inventorySheet10.setRemark("???????????????10");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet10.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		// ... ????????????
		for (Object bm : inventorySheet10.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet10.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet10.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet10, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "inventorySheetBucket")
	public void inventorySheetBucketAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????11?????????????????????11??????????????????11??????????????????11?????????????????????11");

		// ???????????????
		InventorySheet inventorySheet11 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet11.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet11.setRemark("???????????????11");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet11.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet11.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet11.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet11.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet11, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet11 = (InventorySheet) inventorySheet11.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet11.setRemark("???????????????11");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet11.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet11.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet11, dbName);

		// ???????????????
		inventorySheet11.setRemark("???????????????11");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet11.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		inventorySheet11.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet11, dbName);

		// ???????????????
		inventorySheet11.setRemark("???????????????11");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet11.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		// ????????????
		for (Object bm : inventorySheet11.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet11.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet11.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet11, messageBO, dbName);

		// ???????????????
		MvcResult mr5 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "inventorySheetBucketAndDelete")
	public void openPageAndSumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????????????????1???????????????????????????????????????????????????????????????");

		// ???????????????????????????2??????
		List<String> idList = inventorySheetIDMap.get("?????????1");
//		mvc.perform(//
//				get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + idList.get(0))//???????????????????????????????????????????????????
//						.session((MockHttpSession) sessionBoss)//
//		)//
//				.andExpect(status().isOk());

		// ???????????????
		InventorySheet inventorySheet1 = (InventorySheet) inventorySheetMap.get("inventorySheet1");
		inventorySheet1.setRemark("???????????????1");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet1.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet1.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet1.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet1, dbName);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		inventorySheet1 = (InventorySheet) inventorySheet1.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet1", inventorySheet1);
	}

	@Test(dependsOnMethods = "openPageAndSumbit")
	public void openPageAndApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????????????????1?????????????????????????????????????????????1??????????????????");

		// ???????????????????????????2??????
		List<String> idList = inventorySheetIDMap.get("?????????1");
//		mvc.perform(//
//				get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + idList.get(0))//
//						.session((MockHttpSession) sessionBoss)//
//		)//
//				.andExpect(status().isOk());

		// ???????????????
		InventorySheet inventorySheet1 = (InventorySheet) inventorySheetMap.get("inventorySheet1");
		inventorySheet1.setRemark("???????????????1");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet1.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// ????????????
		for (Object bm : inventorySheet1.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet1.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet1.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet1, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "openPageAndApprove")
	public void updateAndClosePage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????2???????????????????????????");

		List<String> idList = inventorySheetIDMap.get("?????????2");
		InventorySheet inventorySheet2 = (InventorySheet) inventorySheetMap.get("inventorySheet2");
		inventorySheet2.setRemark("???????????????2");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet2.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet2.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), String.valueOf(inventorySheet2.getID()))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet2, dbName);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet2 = (InventorySheet) inventorySheet2.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet2", inventorySheet2);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndClosePage")
	public void updateAndSumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????2?????????????????????2");

		List<String> idList = inventorySheetIDMap.get("?????????2");
		InventorySheet inventorySheet2 = (InventorySheet) inventorySheetMap.get("inventorySheet2");
		inventorySheet2.setRemark("????????????????????????2");
		// ???????????????
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet2.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet2.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet2, dbName);

		// ???????????????
		inventorySheet2.setRemark("???????????????2");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet2.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet2.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet2, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit")
	public void updateAndSumbit_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????4?????????????????????4??????????????????4");

		List<String> idList = inventorySheetIDMap.get("?????????4");
		InventorySheet inventorySheet4 = (InventorySheet) inventorySheetMap.get("inventorySheet4");
		// ???????????????
		inventorySheet4.setRemark("???????????????4");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet4.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet4.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet4, dbName);
		// ???????????????
		inventorySheet4.setRemark("???????????????4");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet4.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet4.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet4, dbName);
		// ???????????????
		inventorySheet4.setRemark("???????????????4");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet4.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ????????????
		for (Object bm : inventorySheet4.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet4.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet4.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet4, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit_Approve")
	public void updateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????5?????????????????????5");

		List<String> idList = inventorySheetIDMap.get("?????????5");
		InventorySheet inventorySheet5 = (InventorySheet) inventorySheetMap.get("inventorySheet5");
		// ???????????????
		inventorySheet5.setRemark("???????????????5");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet5.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet5.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), idList.get(0))//
						.param(KEY_InventoryCommodityIDs, idList.get(1))//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet5, dbName);
		// ???????????????
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheet5.getID())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyDelete(inventorySheet5, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit_Approve")
	public void createInvenotryAndUpdate_Sumbit_Delete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????????????????12??????????????????12?????????????????????12??????????????????12");

		// ???????????????
		InventorySheet inventorySheet12 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet12.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet12.setRemark("???????????????12");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet12.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet12.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet12.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet12.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet12, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet12 = (InventorySheet) inventorySheet12.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet12.setRemark("???????????????12");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet12.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet12, dbName);

		// ???????????????
		inventorySheet12.setRemark("???????????????12");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet12.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		inventorySheet12.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet12, dbName);

		// ???????????????
		MvcResult mr4 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheet12.getID())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInvenotryAndUpdate_Sumbit_Delete")
	public void invenotryBucket() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????13??????????????????13?????????????????????13??????????????????13??????????????????13");

		// ???????????????
		InventorySheet inventorySheet13 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet13.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet13.setRemark("???????????????13");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet13.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet13.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet13.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet13.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet13, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet13 = (InventorySheet) inventorySheet13.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet13.setRemark("???????????????13");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet13.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet13.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet13, dbName);

		// ???????????????
		inventorySheet13.setRemark("???????????????13");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet13.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		inventorySheet13.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet13, dbName);

		// ???????????????
		inventorySheet13.setRemark("???????????????13");
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet13.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		for (Object bm : inventorySheet13.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet13.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet13.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet13, messageBO, dbName);

		// ???????????????
		MvcResult mr5 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheet13.getID())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInvenotryAndUpdate_Sumbit_Delete")
	public void createInventoryAndSumbit_Close() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????14??????????????????14???????????????????????????");

		// ???????????????
		InventorySheet inventorySheet14 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet14.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet14.setRemark("???????????????14");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet14.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet14.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet14.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet14.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet14, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet14 = (InventorySheet) inventorySheet14.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet14.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet14.setRemark("???????????????14");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet14.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet14.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet14, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInvenotryAndUpdate_Sumbit_Delete")
	public void createInventoryAndSumbit_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????15??????????????????15?????????????????????15");

		// ???????????????
		InventorySheet inventorySheet15 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet15.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet15.setRemark("???????????????15");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet15.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet15.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet15.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet15.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet15, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet15 = (InventorySheet) inventorySheet15.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet15.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet15.setRemark("???????????????15");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet15.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet15.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet15, dbName);

		// ???????????????
		inventorySheet15.setRemark("???????????????15");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet15.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ????????????
		for (Object bm : inventorySheet15.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet15.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet15.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet15, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Delete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????16??????????????????16?????????????????????16");

		// ???????????????
		InventorySheet inventorySheet16 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet16.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet16.setRemark("???????????????16");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet16.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet16.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet16.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet16.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet16, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet16 = (InventorySheet) inventorySheet16.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet16.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet16.setRemark("???????????????16");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet16.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet16.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet16, dbName);

		// ???????????????
		MvcResult mr3 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Approve_Delete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????17??????????????????17?????????????????????17??????????????????17");

		// ???????????????
		InventorySheet inventorySheet17 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet17.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet17.setRemark("???????????????17");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet17.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet17.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet17.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet17.getRemark()) //
						.param(KEY_CommListID, String.valueOf(inventorySheetMap.get("commodity1").getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet17, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet17 = (InventorySheet) inventorySheet17.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet17.setRemark("???????????????17");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet17.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet17.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet17.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet17, dbName);

		// ???????????????
		inventorySheet17.setRemark("???????????????17");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet17.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ....????????????

		// ???????????????
		MvcResult mr4 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Approve2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????18??????????????????18?????????????????????18???????????????????????????");

		// ???????????????
		InventorySheet inventorySheet18 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet18.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet18.setRemark("???????????????18");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet18.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet18.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet18.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet18.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet18, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet18 = (InventorySheet) inventorySheet18.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet18.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet18.setRemark("???????????????18");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet18.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet18.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet18, dbName);

		// ???????????????
		inventorySheet18.setRemark("???????????????18");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet18.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		for (Object bm : inventorySheet18.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet18.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet18.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet18, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Approve_Delete2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????19??????????????????19?????????????????????19?????????????????????19");

		// ???????????????
		InventorySheet inventorySheet19 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet19.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet19.setRemark("???????????????19");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet19.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet19.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet19.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet19.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet19, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet19 = (InventorySheet) inventorySheet19.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet19.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet19.setRemark("???????????????19");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet19.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet19.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet19, dbName);

		// ???????????????
		inventorySheet19.setRemark("???????????????19");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet19.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr3);
		for (Object bm : inventorySheet19.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet19.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet19.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet19, messageBO, dbName);

		// ???????????????
		MvcResult mr4 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve_Delete2")
	public void createInventoryAndDelete_CloseAndOpen() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????20??????????????????20???????????????????????????");

		// ???????????????
		InventorySheet inventorySheet20 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet20.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet20.setRemark("???????????????20");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet20.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet20.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet20.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet20.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet20, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet20 = (InventorySheet) inventorySheet20.parse1(jsonObject.getString(BaseAction.KEY_Object));
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ????????????
		InventorySheetCP.verifyDelete(inventorySheet20, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve_Delete2")
	public void createInventoryAndUpdate_Cancel() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????21?????????????????????21?????????????????????");

		// ???????????????
		InventorySheet inventorySheet21 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet21.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet21.setRemark("???????????????21");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet21.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet21.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet21.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet21.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet21, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet21 = (InventorySheet) inventorySheet21.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet21.setRemark("???????????????21");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet21.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/updateEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet21.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet21, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndUpdate_Cancel")
	public void createInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????22????????????????????????");

		// ???????????????
		InventorySheet inventorySheet22 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet22.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet22.setRemark("???????????????22");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet22.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet22.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet22.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet22.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet22, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet22 = (InventorySheet) inventorySheet22.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet22", inventorySheet22);

		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ... ????????????
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndRetrieveN")
	public void sumbitInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????22????????????????????????");

		InventorySheet inventorySheet22 = (InventorySheet) inventorySheetMap.get("inventorySheet22");
		// ???????????????
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet22.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet22.setRemark("???????????????22");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet22.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet22 = (InventorySheet) inventorySheet22.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet22", inventorySheet22);

		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ... ????????????
	}

	@Test(dependsOnMethods = "sumbitInventoryAndRetrieveN")
	public void approveInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????22????????????????????????");

		InventorySheet inventorySheet22 = (InventorySheet) inventorySheetMap.get("inventorySheet22");
		// ???????????????
		inventorySheet22.setRemark("???????????????22");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet22.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		for (Object bm : inventorySheet22.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet22.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet22.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet22, messageBO, dbName);

		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ... ????????????
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "approveInventoryAndRetrieveN")
	public void createInventoryAndSumbit_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????23?????????????????????23?????????????????????");

		// ???????????????
		InventorySheet inventorySheet23 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet23.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet23.setRemark("???????????????23");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet23.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet23.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet23.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet23.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet23, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet23 = (InventorySheet) inventorySheet23.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet23.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet23.setRemark("???????????????23");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet23.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ????????????
		inventorySheet23.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet23, dbName);

		// ??????????????????
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// ... ????????????

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_RetrieveN")
	public void createInventoryAndSumbitApprove_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????24?????????????????????24??????????????????24?????????????????????");
		// ???????????????
		InventorySheet inventorySheet24 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet24.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet24.setRemark("???????????????24");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet24.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet24.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet24.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet24.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet24, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet24 = (InventorySheet) inventorySheet24.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet24.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet24.setRemark("???????????????24");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet24.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ????????????
		inventorySheet24.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet24, dbName);

		// ???????????????
		inventorySheet24.setRemark("???????????????13");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet24.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ????????????
		for (Object bm : inventorySheet24.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet24.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet24.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet24, messageBO, dbName);

		// ??????????????????
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		// ... ????????????
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitApprove_RetrieveN")
	public void createInventoryAndDelete_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????25?????????????????????25?????????????????????");

		// ???????????????
		InventorySheet inventorySheet25 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet25.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet25.setRemark("???????????????25");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet25.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet25.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet25.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet25.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet25, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet25 = (InventorySheet) inventorySheet25.parse1(jsonObject.getString(BaseAction.KEY_Object));
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ????????????
		InventorySheetCP.verifyDelete(inventorySheet25, inventorySheetMapper, dbName);
		// ??????????????????
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Deleted.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test(dependsOnMethods = "createInventoryAndDelete_RetrieveN")
	public void createInventoryAndSumbit_RetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????26??????????????????26?????????????????????");

		// ...???createInventoryAndSumbit_RetrieveN()????????????????????????
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_RetrieveN2")
	public void createInventoryAndSumbitApproveDelete_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????27??????????????????27??????????????????27??????????????????27?????????????????????");

		// ???????????????
		InventorySheet inventorySheet27 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet27.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet27.setRemark("???????????????27");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet27.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet27.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet27.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet27.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet27, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// ???????????????
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet27 = (InventorySheet) inventorySheet27.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet27.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet27.setRemark("???????????????27");
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet27.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr2);
		inventorySheet27.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet27, dbName);

		// ???????????????
		inventorySheet27.setRemark("???????????????27");
		MvcResult mr3 = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet27.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ????????????
		for (Object bm : inventorySheet27.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet27.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet27.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet27, messageBO, dbName);

		// ???????????????
		MvcResult mr4 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);

		// ??????????????????
		MvcResult mr5 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Deleted.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
		// ... ????????????
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitApproveDelete_RetrieveN")
	public void createInventoryAndRetrieveNByCommodityName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????28????????????????????????");

		// ???????????????
		InventorySheet inventorySheet28 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet28.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet28.setRemark("???????????????28");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet28.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet28.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet28.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet28.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet28, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet28 = (InventorySheet) inventorySheet28.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet28", inventorySheet28);

		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndRetrieveNByCommodityName")
	public void createInventoryAndSumbitRetrieveNByStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????28????????????????????????");

		// ???????????????
		InventorySheet inventorySheet28 = (InventorySheet) inventorySheetMap.get("inventorySheet28");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet28.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet28.setRemark("???????????????28");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/submitEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet28.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.param(KEY_InventoryCommodityIDs, inventorySheetListSlaveID)//
						.param(KEY_CommNOReals, commNOReals)//
						.param(KEY_CommListID, commListID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		inventorySheet28.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr, inventorySheet28, dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet28 = (InventorySheet) inventorySheet28.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet28", inventorySheet28);

		// ???????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Submitted.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ...????????????
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitRetrieveNByStatus")
	public void createInventoryAndApproveRetrieveNByStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????28????????????????????????");

		// ???????????????
		InventorySheet inventorySheet28 = (InventorySheet) inventorySheetMap.get("inventorySheet28");
		inventorySheet28.setRemark("???????????????28");
		MvcResult mr = mvc.perform(//
				post("/inventorySheet/approveEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet28.getRemark())//
						.param(InventorySheet.field.getFIELD_NAME_ID(), inventorySheetID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr);
		for (Object bm : inventorySheet28.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "???????????????????????????????????????:" + ecOut.getErrorCode() + ",???????????????" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "?????????????????????????????????null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet28.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet28.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet28, messageBO, dbName);

		// ???????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ...????????????
	}

	@Test(dependsOnMethods = "createInventoryAndApproveRetrieveNByStatus")
	public void createInventoryAndDeleteAndRetrieveNByCommodityName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " ???????????????29??????????????????29?????????????????????");

		// ???????????????
		InventorySheet inventorySheet29 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet29.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet29.setRemark("???????????????29");
		MvcResult mr = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet29.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet29.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet29.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet29.getRemark()) //
						.param(KEY_CommListID, commodityID) //
						.param(KEY_BarcodeIDs, String.valueOf(inventorySheetMap.get("barcode1").getID())) //
						.param(KEY_CommNOReals, "10").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet29, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet29 = (InventorySheet) inventorySheet29.parse1(jsonObject.getString(BaseAction.KEY_Object));

		// ???????????????
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ????????????
		InventorySheetCP.verifyDelete(inventorySheet29, inventorySheetMapper, dbName);
		// ????????????
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		// ...????????????

		JSONObject o2 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o2, "$.objectList[*].ID");
		Assert.assertTrue(!list1.contains(inventorySheetID));
	}

	@Test(dependsOnMethods = "createInventoryAndDeleteAndRetrieveNByCommodityName")
	public void RetrieveNByStatusInventoryAndretrieveNByFieldsTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " ?????????????????????????????????????????????");

		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// ...????????????

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> commodityNameList = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");

		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> commodityNameList2 = JsonPath.read(o2, "$.objectList[*].listSlave1[*].commodityName");
		commodityNameList.retainAll(commodityNameList2);// ???????????????????????????commodityNameList???size??????0
		Assert.assertTrue(commodityNameList.size() > 0, "?????????commodityNameList???size????????????0");
	}

	// ?????????????????????????????????????????????
	@Test(dependsOnMethods = "RetrieveNByStatusInventoryAndretrieveNByFieldsTest1")
	public void RetrieveNByStatusInventoryAndretrieveNByFieldsTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " ?????????????????????????????????????????????");

		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> commodityNameList = JsonPath.read(o2, "$.objectList[*].listSlave1[*].commodityName");

		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// ...????????????

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> commodityNameList2 = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");
		commodityNameList.retainAll(commodityNameList2);// ???????????????????????????commodityNameList???size??????0
		Assert.assertTrue(commodityNameList.size() > 0, "?????????commodityNameList???size????????????0");
	}

	@Test(dependsOnMethods = "RetrieveNByStatusInventoryAndretrieveNByFieldsTest2")
	public void PageDownAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "??????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ...??????

		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????

		// ...??????

		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "2")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		// ...??????
	}

	@Test(dependsOnMethods = "PageDownAndRetrieveNByFields")
	public void retrieveStatusAndPageDownAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ...??????

		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/inventorySheet/retrieveNEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()))//
						.param(InventorySheet.field.getFIELD_NAME_pageIndex(), "1")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// ...????????????

		// ...????????????

		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????

		// ...??????
	}

	@Test(dependsOnMethods = "retrieveStatusAndPageDownAndRetrieveNByFields")
	public void retrieveNByFieldsAndPaging() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "?????????????????????????????????????????????");

		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "??????")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...????????????

		// ...??????
	}

	@Test(dependsOnMethods = "retrieveNByFieldsAndPaging")
	public void inTwoMachineToDo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");
		// ...????????????????????????
	}

	@Test(dependsOnMethods = "inTwoMachineToDo")
	public void inTwoMachineToDo2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");
		// ...????????????????????????
	}

	/** ????????????????????????????????????ID??????????????????ID */
	public void getInventoryIDAndInventoryCommodityID(MvcResult mr) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		JSONObject jsonObject = JsonPath.read(o, "$.object");
		InventorySheet inventorySheet = (InventorySheet) JSONObject.toBean(jsonObject, InventorySheet.class);
		assertTrue(inventorySheet != null && inventorySheet.getID() > 0, "????????????????????????????????????ID????????????0????????????????????????null");
		inventorySheetID = String.valueOf(inventorySheet.getID());
		//
		JSONObject inventorycommodityJson = JSONObject.fromObject(inventorySheet.getListSlave1().get(0));
		InventoryCommodity inventoryCommodity = (InventoryCommodity) JSONObject.toBean(inventorycommodityJson, InventoryCommodity.class);
		inventorySheetListSlaveID = String.valueOf(inventoryCommodity.getID());
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("???????????????staff=" + staff);

		return staff;
	}
}
