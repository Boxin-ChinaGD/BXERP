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
	protected final String CommonCommodity = "普通商品";

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

	/** 检查staff手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	/** 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证 */
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新店开张，打开商品列表页面");
		// 创建新公司，新店开张，打开商品列表
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
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下下个用例再做结果验证吧
		mvcResult_Company = mr1;
	}

	@Test(dependsOnMethods = "createShopAndCreateCommodity")
	public void preSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "售前人员无法CRUD盘点相关的");

		Company company = (Company) inventorySheetMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, company.getSN());
		// 创建盘点单
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

		// ...刚创建的公司没有数据，无法进行UD等操作。

		// 创建公司的结果验证
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// 用老板账号登录session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	@Test(dependsOnMethods = "preSale")
	public void createInventoryAndOpenPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单1后，关闭页面再打开");

		// 创建测试商品
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
		// 把其barcode查出来
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
		Assert.assertTrue(listBarcodes.size() > 0, "查询条形码失败~");
		Barcodes barcode1 = new Barcodes();
		barcode1 = (Barcodes) barcode1.parse1(listBarcodes.get(0).toString());
		inventorySheetMap.put("barcode1", barcode1);
		// 新建盘点单1
		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("创建盘点单1");
		MvcResult mr2 = mvc.perform( //
				post("/inventorySheet/createEx.bx") //
						.param(InventorySheet.field.getFIELD_NAME_warehouseID(), String.valueOf(inventorySheet.getWarehouseID())) //
						.param(InventorySheet.field.getFIELD_NAME_scope(), String.valueOf(inventorySheet.getScope())) //
						.param(InventorySheet.field.getFIELD_NAME_staffID(), String.valueOf(inventorySheet.getStaffID())) //
						.param(InventorySheet.field.getFIELD_NAME_status(), String.valueOf(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex())) //
						.param(InventorySheet.field.getFIELD_NAME_remark(), inventorySheet.getRemark()) //
						// .param(InventorySheet.field.getFIELD_NAME_int1(),
						// String.valueOf(inventorySheet.getInt1()))//
						// 在InventorySheet和InventorySheetAction中没有用到int1，故注释（后面已的已全部删除）。
						.param(KEY_CommListID, String.valueOf(commodity1.getID())) //
						.param(KEY_BarcodeIDs, String.valueOf(barcode1.getID())) //
						.param(KEY_CommNOReals, "10")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyCreate(mr2, inventorySheet, dbName);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		//
		getInventoryIDAndInventoryCommodityID(mr2);
		inventorySheetMap.put("inventorySheet1", inventorySheet);
		inventorySheetIDMap.put("盘点单1", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndOpenPage")
	public void createInventoryAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单2后，修改盘点单2");

		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("创建盘点单2");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet, dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject.getString(BaseAction.KEY_Object));
		//
		getInventoryIDAndInventoryCommodityID(mr);
		inventorySheetIDMap.put("盘点单2", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
		//
		// 修改盘点单2
		inventorySheet.setRemark("修改盘点单2");

		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet, dbName);
		inventorySheetMap.put("inventorySheet2", inventorySheet);

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndUpdate")
	public void createInventoryDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单3后，修改盘点单3，再删除该盘点单3");

		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet.setRemark("创建盘点单3");
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
		// 结果验证
		InventorySheetCP.verifyCreate(mr, inventorySheet, dbName);

		// 修改盘点单3
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet = (InventorySheet) inventorySheet.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet.setRemark("修改盘点单3");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 结果验证
		InventorySheetCP.verifyUpdate(mr2, inventorySheet, dbName);

		MvcResult mr3 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// 结果验证
		InventorySheetCP.verifyDelete(inventorySheet, inventorySheetMapper, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndUpdate")
	public void createInventoryIdentical() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建一个盘点单4成功后，再创建一个相同的盘点单5");

		InventorySheet inventorySheet4 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet4.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet4.setRemark("创建盘点单4");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet4, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet4 = (InventorySheet) inventorySheet4.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetIDMap.put("盘点单4", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
		inventorySheetMap.put("inventorySheet4", inventorySheet4);
		//
		InventorySheet inventorySheet5 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet5.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet5.setRemark("创建盘点单5跟盘点单4一样");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyCreate(mr2, inventorySheet5, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		inventorySheet5 = (InventorySheet) inventorySheet5.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet5", inventorySheet5);
		inventorySheetIDMap.put("盘点单5", Arrays.asList(inventorySheetID, inventorySheetListSlaveID, commodityID));
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryIdentical")
	public void submitInventory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建个盘点单6后进行修改，点击提交按钮");
		// 创建盘点单
		InventorySheet inventorySheet6 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet6.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet6.setRemark("创建盘点单6");
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
		// 检查点
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet6, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet6 = (InventorySheet) inventorySheet6.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet6.setRemark("修改盘点单6");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet6.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
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
		// 结果验证
		inventorySheet6.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet6, dbName);
	}

	@Test(dependsOnMethods = "submitInventory")
	public void approveInventory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "选择个已提交的盘点单6进行修改，点击审核按钮");

		InventorySheet inventorySheet6 = (InventorySheet) inventorySheetMap.get("inventorySheet6");
		inventorySheet6.setRemark("审核盘点单6");
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

		Shared.checkJSONErrorCode(mr);  //新创建的公司，且公司老板没有绑定微信号，无需发送微信消息
		// 结果验证
		for (Object bm : inventorySheet6.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet6.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet6.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet6, messageBO, dbName);
	}

	@Test(dependsOnMethods = "approveInventory")
	public void alreadyDeleted() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "已删除该步骤！");

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "alreadyDeleted")
	public void createInventoryAndSumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单7后，提交盘点单7");

		// 创建盘点单
		InventorySheet inventorySheet7 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet7.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet7.setRemark("创建盘点单7");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet7, dbName);
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet7 = (InventorySheet) inventorySheet7.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet7.setRemark("修改盘点单6");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet7.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet7.setRemark("提交盘点单7");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet7.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet7, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndSumbit")
	public void createInventoryAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单8后，删除盘点单8");

		// 创建盘点单
		InventorySheet inventorySheet8 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet8.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet8.setRemark("创建盘点单8");
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
		// ...结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet8, dbName);
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet8 = (InventorySheet) inventorySheet8.parse1(jsonObject.getString(BaseAction.KEY_Object));

		// 删除盘点盘
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyDelete(inventorySheet8, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndDelete")
	public void createInventoryAndUpdate_Sumbit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单9后，修改盘点单9，再提交盘点单9");

		// 创建盘点单
		InventorySheet inventorySheet9 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet9.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet9.setRemark("创建盘点单9");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet9, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet9 = (InventorySheet) inventorySheet9.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet9.setRemark("修改盘点单9");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet9.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet9, dbName);

		// 提交盘点单
		inventorySheet9.setRemark("提交盘点单9");
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

		// ... 结果验证
		Shared.checkJSONErrorCode(mr3);
		inventorySheet9.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet9, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndUpdate_Sumbit")
	public void inventorySheetBucket() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单10后，修改盘点单10，提交盘点单10，再审核盘点单10");

		// 创建盘点单
		InventorySheet inventorySheet10 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet10.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet10.setRemark("创建盘点单10");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet10, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet10 = (InventorySheet) inventorySheet10.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet10.setRemark("修改盘点单10");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet10.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet10, dbName);

		// 提交盘点单
		inventorySheet10.setRemark("提交盘点单10");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		inventorySheet10.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet10, dbName);

		// 审核盘点单
		inventorySheet10.setRemark("审核盘点单10");
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
		// ... 结果验证
		for (Object bm : inventorySheet10.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet10.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet10.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet10, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "inventorySheetBucket")
	public void inventorySheetBucketAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单11后，修改盘点单11，提交盘点单11，审核盘点单11，再删除盘点单11");

		// 创建盘点单
		InventorySheet inventorySheet11 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet11.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet11.setRemark("创建盘点单11");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet11, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet11 = (InventorySheet) inventorySheet11.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet11.setRemark("修改盘点单11");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet11.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet11, dbName);

		// 提交盘点单
		inventorySheet11.setRemark("提交盘点单11");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		inventorySheet11.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet11, dbName);

		// 审核盘点单
		inventorySheet11.setRemark("审核盘点单11");
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
		// 结果验证
		for (Object bm : inventorySheet11.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet11.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet11.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet11, messageBO, dbName);

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "查看某个待录入的盘点单1详情后进行表格翻页再对该盘点单进行提交操作");

		// 查看待录入的盘点单2详情
		List<String> idList = inventorySheetIDMap.get("盘点单1");
//		mvc.perform(//
//				get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + idList.get(0))//前端已经删除此接口，所以省略本步骤
//						.session((MockHttpSession) sessionBoss)//
//		)//
//				.andExpect(status().isOk());

		// 提交盘点单
		InventorySheet inventorySheet1 = (InventorySheet) inventorySheetMap.get("inventorySheet1");
		inventorySheet1.setRemark("提交盘点单1");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet1.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "查看某个已提交的盘点单1详情后进行表格翻页再对该盘点单1进行审核操作");

		// 查看待录入的盘点单2详情
		List<String> idList = inventorySheetIDMap.get("盘点单1");
//		mvc.perform(//
//				get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + idList.get(0))//
//						.session((MockHttpSession) sessionBoss)//
//		)//
//				.andExpect(status().isOk());

		// 审核盘点单
		InventorySheet inventorySheet1 = (InventorySheet) inventorySheetMap.get("inventorySheet1");
		inventorySheet1.setRemark("审核盘点单1");
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
		// 结果验证
		for (Object bm : inventorySheet1.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet1.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet1.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet1, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "openPageAndApprove")
	public void updateAndClosePage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "修改盘点单2后，关闭页面再打开");

		List<String> idList = inventorySheetIDMap.get("盘点单2");
		InventorySheet inventorySheet2 = (InventorySheet) inventorySheetMap.get("inventorySheet2");
		inventorySheet2.setRemark("修改盘点单2");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet2.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "修改盘点单2后，提交盘点单2");

		List<String> idList = inventorySheetIDMap.get("盘点单2");
		InventorySheet inventorySheet2 = (InventorySheet) inventorySheetMap.get("inventorySheet2");
		inventorySheet2.setRemark("第二次修改盘点单2");
		// 修改盘点单
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet2.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet2, dbName);

		// 提交盘点单
		inventorySheet2.setRemark("提交盘点单2");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet2.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet2, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit")
	public void updateAndSumbit_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "修改盘点单4后，提交盘点单4，审核盘点单4");

		List<String> idList = inventorySheetIDMap.get("盘点单4");
		InventorySheet inventorySheet4 = (InventorySheet) inventorySheetMap.get("inventorySheet4");
		// 修改盘点单
		inventorySheet4.setRemark("修改盘点单4");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet4.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet4, dbName);
		// 提交盘点单
		inventorySheet4.setRemark("提交盘点单4");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet4.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet4, dbName);
		// 审核盘点单
		inventorySheet4.setRemark("审核盘点单4");
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
		// 结果验证
		for (Object bm : inventorySheet4.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet4.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet4.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet4, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit_Approve")
	public void updateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "修改盘点单5后，删除盘点单5");

		List<String> idList = inventorySheetIDMap.get("盘点单5");
		InventorySheet inventorySheet5 = (InventorySheet) inventorySheetMap.get("inventorySheet5");
		// 修改盘点单
		inventorySheet5.setRemark("修改盘点单5");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet5.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyUpdate(mr, inventorySheet5, dbName);
		// 删除盘点单
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheet5.getID())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyDelete(inventorySheet5, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "updateAndSumbit_Approve")
	public void createInvenotryAndUpdate_Sumbit_Delete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的新的盘点单12，修改盘点单12后，提交盘点单12，删除盘点单12");

		// 创建盘点单
		InventorySheet inventorySheet12 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet12.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet12.setRemark("创建盘点单12");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet12, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet12 = (InventorySheet) inventorySheet12.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet12.setRemark("修改盘点单12");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet12.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet12, dbName);

		// 提交盘点单
		inventorySheet12.setRemark("提交盘点单12");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		inventorySheet12.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet12, dbName);

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单13，修改盘点单13后，提交盘点单13，审核盘点单13，删除盘点单13");

		// 创建盘点单
		InventorySheet inventorySheet13 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet13.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet13.setRemark("创建盘点单13");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet13, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet13 = (InventorySheet) inventorySheet13.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet13.setRemark("修改盘点单13");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet13.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet13, dbName);

		// 提交盘点单
		inventorySheet13.setRemark("提交盘点单13");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		inventorySheet13.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr3, inventorySheet13, dbName);

		// 审核盘点单
		inventorySheet13.setRemark("审核盘点单13");
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
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet13.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet13.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr4, inventorySheet13, messageBO, dbName);

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单14，提交盘点单14后，关闭页面再打开");

		// 创建盘点单
		InventorySheet inventorySheet14 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet14.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet14.setRemark("创建盘点单14");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet14, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet14 = (InventorySheet) inventorySheet14.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet14.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet14.setRemark("提交盘点单14");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet14.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet14, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInvenotryAndUpdate_Sumbit_Delete")
	public void createInventoryAndSumbit_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单15，提交盘点单15后，审核盘点单15");

		// 创建盘点单
		InventorySheet inventorySheet15 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet15.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet15.setRemark("创建盘点单15");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet15, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet15 = (InventorySheet) inventorySheet15.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet15.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet15.setRemark("提交盘点单15");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet15.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet15, dbName);

		// 审核盘点单
		inventorySheet15.setRemark("审核盘点单15");
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
		// 结果验证
		for (Object bm : inventorySheet15.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet15.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet15.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet15, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Delete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单16，提交盘点单16后，删除盘点单16");

		// 创建盘点单
		InventorySheet inventorySheet16 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet16.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet16.setRemark("创建盘点单16");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet16, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet16 = (InventorySheet) inventorySheet16.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet16.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet16.setRemark("提交盘点单16");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet16.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet16, dbName);

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单17，提交盘点单17后，审核盘点单17，删除盘点单17");

		// 创建盘点单
		InventorySheet inventorySheet17 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet17.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet17.setRemark("创建盘点单17");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet17, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet17 = (InventorySheet) inventorySheet17.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet17.setRemark("提交盘点单17");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet17.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet17.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet17, dbName);

		// 审核盘点单
		inventorySheet17.setRemark("审核盘点单17");
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
		// ....结果验证

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单18，提交盘点单18后，审核盘点单18后，关闭页面再打开");

		// 创建盘点单
		InventorySheet inventorySheet18 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet18.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet18.setRemark("创建盘点单18");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet18, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet18 = (InventorySheet) inventorySheet18.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet18.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet18.setRemark("提交盘点单18");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet18.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet18, dbName);

		// 审核盘点单
		inventorySheet18.setRemark("审核盘点单18");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		for (Object bm : inventorySheet18.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet18.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet18.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet18, messageBO, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve")
	public void createInventoryAndSumbit_Approve_Delete2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单19，提交盘点单19后，审核盘点单19后，删除盘点单19");

		// 创建盘点单
		InventorySheet inventorySheet19 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet19.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet19.setRemark("创建盘点单19");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet19, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet19 = (InventorySheet) inventorySheet19.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet19.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet19.setRemark("提交盘点单19");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		inventorySheet19.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet19, dbName);

		// 审核盘点单
		inventorySheet19.setRemark("审核盘点单19");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		for (Object bm : inventorySheet19.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet19.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet19.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet19, messageBO, dbName);

		// 删除盘点单
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "创建新的盘点单20，删除盘点单20后，关闭页面再打开");

		// 创建盘点单
		InventorySheet inventorySheet20 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet20.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet20.setRemark("创建盘点单20");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet20, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 删除盘点单
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
		// 结果验证
		InventorySheetCP.verifyDelete(inventorySheet20, inventorySheetMapper, dbName);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_Approve_Delete2")
	public void createInventoryAndUpdate_Cancel() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单21后，修改盘点单21，点击取消按钮");

		// 创建盘点单
		InventorySheet inventorySheet21 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet21.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet21.setRemark("创建盘点单21");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet21, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 修改盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet21 = (InventorySheet) inventorySheet21.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheet21.setRemark("修改盘点单21");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet21.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		InventorySheetCP.verifyUpdate(mr2, inventorySheet21, dbName);
	}

	@Test(dependsOnMethods = "createInventoryAndUpdate_Cancel")
	public void createInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单22后，根据状态查询");

		// 创建盘点单
		InventorySheet inventorySheet22 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet22.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet22.setRemark("创建盘点单22");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet22, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet22 = (InventorySheet) inventorySheet22.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet22", inventorySheet22);

		// 根据状态查询
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
		// ... 结果验证
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndRetrieveN")
	public void sumbitInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "提交盘点单22后，根据状态查询");

		InventorySheet inventorySheet22 = (InventorySheet) inventorySheetMap.get("inventorySheet22");
		// 提交盘点单
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet22.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet22.setRemark("提交盘点单22");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet22 = (InventorySheet) inventorySheet22.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet22", inventorySheet22);

		// 根据状态查询
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
		// ... 结果验证
	}

	@Test(dependsOnMethods = "sumbitInventoryAndRetrieveN")
	public void approveInventoryAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "审核盘点单22后，根据状态查询");

		InventorySheet inventorySheet22 = (InventorySheet) inventorySheetMap.get("inventorySheet22");
		// 审核盘点单
		inventorySheet22.setRemark("审核盘点单22");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		for (Object bm : inventorySheet22.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet22.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet22.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet22, messageBO, dbName);

		// 根据状态查询
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
		// ... 结果验证
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "approveInventoryAndRetrieveN")
	public void createInventoryAndSumbit_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单23后，提交盘点单23，根据状态查询");

		// 创建盘点单
		InventorySheet inventorySheet23 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet23.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet23.setRemark("创建盘点单23");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet23, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet23 = (InventorySheet) inventorySheet23.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet23.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet23.setRemark("提交盘点单23");
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
		// 结果检验
		inventorySheet23.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet23, dbName);

		// 根据状态查询
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
		// ... 结果验证

	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_RetrieveN")
	public void createInventoryAndSumbitApprove_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单24后，提交盘点单24，审核盘点单24，根据状态查询");
		// 创建盘点单
		InventorySheet inventorySheet24 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet24.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet24.setRemark("创建盘点单24");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet24, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet24 = (InventorySheet) inventorySheet24.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet24.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet24.setRemark("提交盘点单24");
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
		// 结果检验
		inventorySheet24.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet24, dbName);

		// 审核盘点单
		inventorySheet24.setRemark("审核盘点单13");
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
		// 结果验证
		for (Object bm : inventorySheet24.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet24.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet24.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet24, messageBO, dbName);

		// 根据状态查询
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
		// ... 结果验证
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitApprove_RetrieveN")
	public void createInventoryAndDelete_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单25后，删除盘点单25，根据状态查询");

		// 创建盘点单
		InventorySheet inventorySheet25 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet25.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet25.setRemark("创建盘点单25");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet25, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 删除盘点单
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
		// 结果验证
		InventorySheetCP.verifyDelete(inventorySheet25, inventorySheetMapper, dbName);
		// 根据状态查询
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单26，提交盘点单26，根据状态查询");

		// ...同createInventoryAndSumbit_RetrieveN()的步骤相同。。。
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndSumbit_RetrieveN2")
	public void createInventoryAndSumbitApproveDelete_RetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单27，提交盘点单27，审核盘点单27，删除盘点单27，根据状态查询");

		// 创建盘点单
		InventorySheet inventorySheet27 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet27.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet27.setRemark("创建盘点单27");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet27, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);

		// 提交盘点单
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet27 = (InventorySheet) inventorySheet27.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet27.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet27.setRemark("提交盘点单27");
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

		// 结果检验
		Shared.checkJSONErrorCode(mr2);
		inventorySheet27.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr2, inventorySheet27, dbName);

		// 审核盘点单
		inventorySheet27.setRemark("审核盘点单27");
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
		// 结果验证
		for (Object bm : inventorySheet27.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet27.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet27.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr3, inventorySheet27, messageBO, dbName);

		// 删除盘点单
		MvcResult mr4 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);

		// 根据状态查询
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
		// ... 结果验证
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitApproveDelete_RetrieveN")
	public void createInventoryAndRetrieveNByCommodityName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "新建盘点单28后，进行模糊搜索");

		// 创建盘点单
		InventorySheet inventorySheet28 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet28.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet28.setRemark("创建盘点单28");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet28, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet28 = (InventorySheet) inventorySheet28.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.put("inventorySheet28", inventorySheet28);

		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "createInventoryAndRetrieveNByCommodityName")
	public void createInventoryAndSumbitRetrieveNByStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "提交盘点单28后，进行状态查询");

		// 提交盘点单
		InventorySheet inventorySheet28 = (InventorySheet) inventorySheetMap.get("inventorySheet28");
		List<InventoryCommodity> inventoryCommodities = (List<InventoryCommodity>) inventorySheet28.getListSlave1();
		String commNOReals = "";
		String commListID = "";
		// 提交或审核盘点单之后，都会修改系统库存数量，所以从商品缓存拿出来商品的系统库存数量，进行结果验证
		for (InventoryCommodity inventoryCommodity : inventoryCommodities) {
			inventoryCommodity.setNoReal(new Random().nextInt(200));
			commNOReals += inventoryCommodity.getNoReal() + ",";
			commListID += inventoryCommodity.getCommodityID() + ",";
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet28.setRemark("提交盘点单28");
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

		// 结果检验
		Shared.checkJSONErrorCode(mr);
		inventorySheet28.setStatus(EnumStatusInventorySheet.ESIS_Submitted.getIndex());
		InventorySheetCP.verifySubmit(mr, inventorySheet28, dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet28 = (InventorySheet) inventorySheet28.parse1(jsonObject.getString(BaseAction.KEY_Object));
		inventorySheetMap.replace("inventorySheet28", inventorySheet28);

		// 查询盘点单
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
		// ...结果验证
	}

	@Test(dependsOnMethods = "createInventoryAndSumbitRetrieveNByStatus")
	public void createInventoryAndApproveRetrieveNByStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "审核盘点单28后，进行状态查询");

		// 审核盘点单
		InventorySheet inventorySheet28 = (InventorySheet) inventorySheetMap.get("inventorySheet28");
		inventorySheet28.setRemark("审核盘点单28");
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

		// 结果验证
		Shared.checkJSONErrorCode(mr);
		for (Object bm : inventorySheet28.getListSlave1()) {
			InventoryCommodity inventoryCommodity = (InventoryCommodity) bm;
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "从缓存查询商品失败，错误码:" + ecOut.getErrorCode() + ",错误信息：" + ecOut.getErrorMessage());
			}
			Assert.assertTrue(comm != null, "从缓存查询出来的商品为null");
			inventoryCommodity.setNoSystem(comm.getNO());
		}
		inventorySheet28.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		inventorySheet28.setApproverID(getStaffFromSession(sessionBoss).getID());
		InventorySheetCP.verifyApprove(mr, inventorySheet28, messageBO, dbName);

		// 查询盘点单
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
		// ...结果验证
	}

	@Test(dependsOnMethods = "createInventoryAndApproveRetrieveNByStatus")
	public void createInventoryAndDeleteAndRetrieveNByCommodityName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " 新建盘点单29，删除盘点单29，进行模糊搜索");

		// 创建盘点单
		InventorySheet inventorySheet29 = BaseInventorySheetTest.DataInput.getInventorySheetAndInventoryCommodity();
		inventorySheet29.setStaffID(getStaffFromSession(sessionBoss).getID());
		inventorySheet29.setRemark("创建盘点单29");
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
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		InventorySheetCP.verifyCreate(mr, inventorySheet29, dbName);
		//
		getInventoryIDAndInventoryCommodityID(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		inventorySheet29 = (InventorySheet) inventorySheet29.parse1(jsonObject.getString(BaseAction.KEY_Object));

		// 删除盘点单
		MvcResult mr2 = mvc.perform(//
				get("/inventorySheet/deleteEx.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=" + inventorySheetID)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 结果验证
		InventorySheetCP.verifyDelete(inventorySheet29, inventorySheetMapper, dbName);
		// 模糊查询
		MvcResult mr4 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		// ...结果验证

		JSONObject o2 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o2, "$.objectList[*].ID");
		Assert.assertTrue(!list1.contains(inventorySheetID));
	}

	@Test(dependsOnMethods = "createInventoryAndDeleteAndRetrieveNByCommodityName")
	public void RetrieveNByStatusInventoryAndretrieveNByFieldsTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " 根据状态查询后，再进行模糊搜索");

		// 查询盘点单
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
		// ...结果验证

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> commodityNameList = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");

		// 模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> commodityNameList2 = JsonPath.read(o2, "$.objectList[*].listSlave1[*].commodityName");
		commodityNameList.retainAll(commodityNameList2);// 取二者的交集若有则commodityNameList的size大于0
		Assert.assertTrue(commodityNameList.size() > 0, "交集后commodityNameList的size应该大于0");
	}

	// 进行模糊搜索后，再根据状态查询
	@Test(dependsOnMethods = "RetrieveNByStatusInventoryAndretrieveNByFieldsTest1")
	public void RetrieveNByStatusInventoryAndretrieveNByFieldsTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, " 进行模糊搜索后，再根据状态查询");

		// 模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> commodityNameList = JsonPath.read(o2, "$.objectList[*].listSlave1[*].commodityName");

		// 查询盘点单
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
		// ...结果验证

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> commodityNameList2 = JsonPath.read(o, "$.objectList[*].listSlave1[*].commodityName");
		commodityNameList.retainAll(commodityNameList2);// 取二者的交集若有则commodityNameList的size大于0
		Assert.assertTrue(commodityNameList.size() > 0, "交集后commodityNameList的size应该大于0");
	}

	@Test(dependsOnMethods = "RetrieveNByStatusInventoryAndretrieveNByFieldsTest2")
	public void PageDownAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "进行翻页后，进行模糊搜索，再进行翻页操作，再进行条件查询，再进行翻页");

		// ...翻页

		// 模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证

		// ...翻页

		// 查询盘点单
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

		// ...翻页
	}

	@Test(dependsOnMethods = "PageDownAndRetrieveNByFields")
	public void retrieveStatusAndPageDownAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "进行翻页后，根据状态查询后，再进行翻页功能，再进行模糊搜索，再进行翻页");

		// ...翻页

		// 查询盘点单
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
		// ...结果验证

		// ...进行翻页

		// 模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证

		// ...翻页
	}

	@Test(dependsOnMethods = "retrieveStatusAndPageDownAndRetrieveNByFields")
	public void retrieveNByFieldsAndPaging() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "进行模糊搜索后，在进行分页功能");

		// 模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/inventorySheet/retrieveNByFieldsEx.bx")//
						.param(InventorySheet.field.getFIELD_NAME_queryKeyword(), "普通")//
						.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// ...结果验证

		// ...分页
	}

	@Test(dependsOnMethods = "retrieveNByFieldsAndPaging")
	public void inTwoMachineToDo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");
		// ...涉及多台机的操作
	}

	@Test(dependsOnMethods = "inTwoMachineToDo")
	public void inTwoMachineToDo2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_InventoryList_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");
		// ...涉及多台机的操作
	}

	/** 获取到创建出来的盘点单的ID以及其从表的ID */
	public void getInventoryIDAndInventoryCommodityID(MvcResult mr) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		JSONObject jsonObject = JsonPath.read(o, "$.object");
		InventorySheet inventorySheet = (InventorySheet) JSONObject.toBean(jsonObject, InventorySheet.class);
		assertTrue(inventorySheet != null && inventorySheet.getID() > 0, "创建出来的并返回的盘点单ID应该大于0且盘点单对象不为null");
		inventorySheetID = String.valueOf(inventorySheet.getID());
		//
		JSONObject inventorycommodityJson = JSONObject.fromObject(inventorySheet.getListSlave1().get(0));
		InventoryCommodity inventoryCommodity = (InventoryCommodity) JSONObject.toBean(inventorycommodityJson, InventoryCommodity.class);
		inventorySheetListSlaveID = String.valueOf(inventoryCommodity.getID());
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}
}
