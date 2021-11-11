package com.bx.erp.sit.sit1.sg.warehouseStock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.dao.warehousing.WarehousingCommodityMapper;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BasePosTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.ReturnCommoditySheetCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WarehouseStockTest extends BaseActionTest {

	private AtomicInteger order;
	private AtomicInteger commodityOrder;
	private AtomicLong barcode;
	// private Integer commodityID;
	private Commodity commodity;
	private Integer retailTradeID;
	private final String commodity38barcode = "6821423302396";
	private final String commodity39barcode = "6821423302974";
	private Map<String, BaseModel> warehouseStockMap;
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected String CommonCommodity = "普通商品";
	// protected final String bossPhone = "15854320895";
	// protected final String bossPassword = "000000";
	// protected final String companySN = "668866";
	// protected String dbName = "nbr";

	private static final String CommIDs = "commIDs";
	private static final String BarcodeIDs = "barcodeIDs";
	private static final String NOs = "NOs";
	private static final String PriceSuggestions = "priceSuggestions";
	private static final String CommNOs = "commNOs";
	private static final String CommPrices = "commPrices";
	private static final String CommPurchasingUnit = "commPurchasingUnit";
	private static final String ProviderID = "providerID";
	private static final String Amounts = "amounts";

	protected WarehousingMapper warehousingMapper;
	protected WarehousingCommodityMapper warehousingCommodityMapper;

	/** 检查staff手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(80);
		barcode = new AtomicLong();
		barcode.set(6821423302515L);
		warehouseStockMap = new HashMap<String, BaseModel>();

	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

	@Test
	public void createShopAndRetriveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "新建门店后，查看数据");

		// 创建新公司并查看数据（无数据）
		Company company = BaseCompanyTest.DataInput.getCompany();
		dbName = company.getDbName();
		bossPhone = company.getBossPhone();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult createMR = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(createMR);
		//
		Company companyCreate = (Company) Shared.parse1Object(createMR, company, BaseAction.KEY_Object);
		warehouseStockMap.put("company", company);
		companySN = companyCreate.getSN();
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下下个用例再做结果验证吧
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);

		// 查看数据
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ... 结果验证
	}

	@Test(dependsOnMethods = "createShopAndRetriveN")
	public void preSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "售前人员能查看到商品信息");

		Company company = (Company) warehouseStockMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, company.getSN());
		// 查看商品
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// 用老板账号登录session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		// ...验证售前帐号已经被删除
	}

	@Test(dependsOnMethods = "preSale")
	public void retriveNCommodity1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看1(新增商品)");
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		// 创建商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("库管查询的商品1");
		c1.setMultiPackagingInfo(commodity38barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		commodity = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbName);
		// 查询商品
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		Commodity retriveCommodity = (Commodity) Shared.parse1Object(mr2, c1, BaseAction.KEY_Object);
		//
		Assert.assertTrue(commodity.getNO() == retriveCommodity.getNO(), "创建同查询出来的库存不相同！");
	}

	@Test(dependsOnMethods = "retriveNCommodity1")
	public void retriveNCommodity2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看2(修改后)");

		Commodity updateCommodity = BaseCommodityTest.DataInput.getUpdateCommodity();
		updateCommodity.setID(commodity.getID());
		updateCommodity.setName("修改库管查询的商品1");
		commodity = BaseCommodityTest.updateViaAction(commodity, updateCommodity, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), dbName);
		commodity.setBarcodeID(barcodes.getID());
		commodity.setBarcodes(barcodes.getBarcode());
	}

	@Test(dependsOnMethods = "retriveNCommodity2")
	public void retriveNCommodity3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看3(删除后)");

		BaseCommodityTest.deleteCommodityViaAction(commodity, dbName, mvc, sessionBoss, mapBO);
		// 查看商品
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ...结果检验
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoSuchData);
	}

	@Test(dependsOnMethods = "retriveNCommodity3")
	public void retriveNCommodity4() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看4(新建入库单再审核)");

		// 创建三个商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbName);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2 = BaseCommodityTest.createCommodityViaAction(c2, mvc, sessionBoss, mapBO, dbName);
		//
		Commodity c3 = BaseCommodityTest.DataInput.getCommodity();
		c3 = BaseCommodityTest.createCommodityViaAction(c3, mvc, sessionBoss, mapBO, dbName);

		// 创建采购订单
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, c1.getID() + "," + c3.getID())//
						.param(NOs, "30,30") //
						.param(PriceSuggestions, "12,12") //
						.param(CommPurchasingUnit, "瓶,瓶") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "2,4") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 创建采购订单的检查点
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// 审核采购订单5
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, c1.getID() + "," + c3.getID())//
						.param(NOs, "30,30")//
						.param(PriceSuggestions, "12,12")//
						.param(BarcodeIDs, "2,4") //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// 审核采购订单的检查点
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);

		// 新建入库单(入库单需要依赖已审核的采购订单)
		Commodity commodity1 = new Commodity();
		commodity1.setID(c1.getID());
		Commodity commodityCache1 = getCommodityCache(commodity1.getID());
		Commodity commodity2 = new Commodity();
		commodity2.setID(c3.getID());
		// commodity3.setID(10);
		Commodity commodityCache2 = getCommodityCache(commodity2.getID());
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache1);
		commList.add(commodityCache2);
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po1.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po1.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(CommIDs, c1.getID() + "," + c3.getID())//
						.param(CommNOs, "4,5") //
						.param(CommPrices, "12, 12") //
						.param(Amounts, "12.1, 12.1") //
						.param(BarcodeIDs, "2,4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		WarehousingCP.verifyCreate(mr2, w, dbName);
		Warehousing ws = new Warehousing();
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		ws = (Warehousing) ws.parse1(o1.getString(BaseAction.KEY_Object));
		// 审核入库单
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po1.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// 检查是否数量有相应的修改
		WarehousingCP.verifyApprove(mr3, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
	}

	@Test(dependsOnMethods = "retriveNCommodity4")
	public void retriveNCommodity5() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "查看商品信息5(POS机购买操作后)");

		// 创建pos机
		Pos pos = BaseTestNGSpringContextTest.DataInput.getPOS();
		pos.setCompanySN(companySN);
		//
		Pos pos1 = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, dbName);
		// Shared.resetPOS(mvc, posID);
		sessionBoss = (MockHttpSession) Shared.getPosLoginSession(mvc, pos1.getID(), bossPhone, BaseCompanyTest.bossPassword_New, companySN, pos1.getPasswordInPOS());
		// 购买商品(创建一个零售单)
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPos_ID(pos1.getID());
		rt.setVipID(0);
		rt.setStaffID(getStaffFromSession(sessionBoss).getID());
		rt.setSn(Shared.generateRetailTradeSN(rt.getPos_ID()));
		//
		List<BaseModel> list = new ArrayList<BaseModel>();
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodity.getID());
		rtc.setBarcodeID(commodity.getBarcodeID());
		list.add(rtc);
		rt.setListSlave1(list);
		// 创建操作以及检查点
		retailTradeID = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test).getID();
		// ... 商品的信息验证

		// 用回之前的session登录
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	@Test(dependsOnMethods = "retriveNCommodity5")
	public void retriveNCommodity6() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看6(审核退货单后，查看商品信息)");

		// 创建一个零售退货单
		RetailTrade tmpRetailTrade2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		tmpRetailTrade2.setSourceID(retailTradeID);
		tmpRetailTrade2.setVipID(0);
		tmpRetailTrade2.setStaffID(getStaffFromSession(sessionBoss).getID());
		tmpRetailTrade2.setSn(Shared.generateRetailTradeSN(tmpRetailTrade2.getPos_ID()));
		//
		List<BaseModel> list = new ArrayList<BaseModel>();
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodity.getID());
		rtc.setBarcodeID(commodity.getBarcodeID());
		list.add(rtc);
		tmpRetailTrade2.setListSlave1(list);
		// 创建以及检查点
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, tmpRetailTrade2, Shared.DBName_Test);
		// ... 查看商品信息
	}

	@Test(dependsOnMethods = "retriveNCommodity6")
	public void retriveNCommodity7() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看7(修改商品属性后，查看商品信息)");

		// 创建商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("库管查询的商品7");
		c1.setMultiPackagingInfo(commodity38barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		commodity = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), dbName);
		commodity.setBarcodeID(barcodes.getID());
		commodity.setBarcodes(barcodes.getBarcode());
		// 修改商品属性 (修改的是供应商名称、品牌、商品分类、单位)
		Commodity updateCommodity = BaseCommodityTest.DataInput.getUpdateCommodity();
		updateCommodity.setName("修改库管查询商品7");
		updateCommodity.setID(commodity.getID());
		updateCommodity.setProviderName("商品的信息查看7供应商");
		updateCommodity.setBrandID(1);
		updateCommodity.setCategoryID(6);
		updateCommodity.setPackageUnitID(2);
		// 修改操作以及检查点
		BaseCommodityTest.updateViaAction(commodity, updateCommodity, mvc, sessionBoss, mapBO, dbName);

		// 查看商品
		MvcResult mr3 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		Commodity retriveCommodity = (Commodity) Shared.parse1Object(mr3, updateCommodity, BaseAction.KEY_Object);
		Assert.assertTrue(updateCommodity.compareTo(retriveCommodity) == 0, "创建同查询出来的库存不相同！");
	}

	@Test(dependsOnMethods = "retriveNCommodity7")
	public void retriveNCommodity8() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "商品的信息查看8(审核退货单后，查看商品信息)");

		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setStaffID(getStaffFromSession(sessionBoss).getID());
		rt.setVipID(0);
		rt.setSn(Shared.generateRetailTradeSN(rt.getPos_ID()));
		//
		List<BaseModel> list = new ArrayList<BaseModel>();
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodity.getID());
		rtc.setBarcodeID(commodity.getBarcodeID());
		list.add(rtc);
		rt.setListSlave1(list);
		// 创建操作以及结果验证
		RetailTrade r = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, dbName);
		retailTradeID = r.getID();
		//
		RetailTrade tmpRetailTrade2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		tmpRetailTrade2.setStaffID(getStaffFromSession(sessionBoss).getID());
		tmpRetailTrade2.setSourceID(retailTradeID);
		tmpRetailTrade2.setSn(rt.getSn() + "_1");
		tmpRetailTrade2.setVipID(0);
		//
		List<BaseModel> list2 = new ArrayList<BaseModel>();
		rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodity.getID());
		rtc.setBarcodeID(commodity.getBarcodeID());
		list2.add(rtc);
		tmpRetailTrade2.setListSlave1(list2);
		// 创建退货单以及检查点
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, tmpRetailTrade2, dbName);

		// ... 查看商品信息

	}

	@Test(dependsOnMethods = "retriveNCommodity8")
	public void createCommodityAndRetrive() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "查询新数据(新增商品后查询该商品)");
		// 创建商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("查询新数据");
		c1.setMultiPackagingInfo(commodity38barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		//
		commodity = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbName);
		// 查询商品
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		Commodity retriveCommodity = (Commodity) Shared.parse1Object(mr2, c1, BaseAction.KEY_Object);
		Assert.assertTrue(commodity.compareTo(retriveCommodity) == 0, "创建同查询出来的数据不相同！");
	}

	@Test(dependsOnMethods = "createCommodityAndRetrive")
	public void updateCommodityAndRetrive() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "查询修改后的数据(修改的是名称、条码、商品分类)");

		// 修改商品信息
		Commodity updateCommodity = BaseCommodityTest.DataInput.getUpdateCommodity();
		updateCommodity.setID(commodity.getID());
		updateCommodity.setName("修改商品数据");
		updateCommodity.setProviderName("查询修改后的数据");
		updateCommodity.setCategoryID(5);
		updateCommodity.setMultiPackagingInfo(commodity39barcode + ";" + updateCommodity.getPackageUnitID() + ";" + updateCommodity.getRefCommodityMultiple() + ";" + updateCommodity.getPriceRetail() + ";" + updateCommodity.getPriceVIP()
				+ ";" + updateCommodity.getPriceWholesale() + ";" + updateCommodity.getName() + ";");

		commodity = BaseCommodityTest.updateViaAction(commodity, updateCommodity, mvc, sessionBoss, mapBO, dbName);
		Assert.assertTrue(updateCommodity.compareTo(commodity) == 0, "商品数据未修改成功");
		// 查询商品
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		Commodity retriveCommodity = (Commodity) Shared.parse1Object(mr2, updateCommodity, BaseAction.KEY_Object);
		Assert.assertTrue(commodity.compareTo(retriveCommodity) == 0, "修改后的数据同查询出来的数据不相同！");
	}

	@Test(dependsOnMethods = "updateCommodityAndRetrive")
	public void deleteCommodityAndRetrive() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "查询删除后的数据");

		// 删除商品
		BaseCommodityTest.deleteCommodityViaAction(commodity, dbName, mvc, sessionBoss, mapBO);
		// 查看商品
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果检验
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoSuchData);
	}

	@Test(dependsOnMethods = "deleteCommodityAndRetrive")
	public void CRUDInTowMachineToDo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "CRUDInTowMachineToDo")
	public void CRUDInTowMachineToDo2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	// SIT1_nbr_SG_Warehouse_16
	@Test(dependsOnMethods = "CRUDInTowMachineToDo2")
	public void enterWarehousePageAfterCreateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "新建公司后，创建一个普通商品，再查看入库页面");
		// 1、登录nbr后台系统
		// 2、新建公司门店
		// 3、登录EPR系统（登录新门店）；
		// 4、进入商品列表页面，创建一个普通商品；进入商品列表，RN三个接口
		MvcResult mr1 = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr1);
		//
		MvcResult mr11 = mvc.perform(get("/commoditySync/retrieveNEx.bx?status=-1&type=0") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr11);
		// RN时会检查条形码，条形码为空字符串后端现在是不给通过的
		// MvcResult mr12 = mvc.perform(//
		// get("/refCommodityHub/retrieveNByBarcodeEx.bx?pageIndex=1&pageSize=10&barcode=")//
		// .session((MockHttpSession) session)//
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print()).andReturn();
		// Shared.checkJSONErrorCode(mr12);
		// 创建普通商品1
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo(barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		//
		BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbName);
		// JSONObject json =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());
		// Commodity commodity = new Commodity();
		// commodity = (Commodity)
		// commodity.parse1(json.getString(BaseAction.KEY_Object));
		// 5、进入库管查询页面，查看数据；RN库管、RN商品
		MvcResult mr3 = mvc.perform(//
				post("/warehouse/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr3);
		//
		MvcResult mr31 = mvc.perform(get("/commoditySync/retrieveNEx.bx?status=-1&type=0") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr31);

		// 6、进入商品列表页面，再创建一个普通商品；
		MvcResult mr4 = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr4);
		//
		MvcResult mr41 = mvc.perform(get("/commoditySync/retrieveNEx.bx?status=-1&type=0") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr41);
		//
		// MvcResult mr42 = mvc.perform(//
		// get("/refCommodityHub/retrieveNByBarcodeEx.bx?pageIndex=1&pageSize=10&barcode=")//
		// .session((MockHttpSession) session)//
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print()).andReturn();
		// Shared.checkJSONErrorCode(mr42);
		// 创建普通商品2
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setName(CommonCommodity + getCommodityOrder());
		c2.setMultiPackagingInfo(getBarcode() + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		//
		BaseCommodityTest.createCommodityViaAction(c2, mvc, sessionBoss, mapBO, dbName);
		// 7、进入商品列表页面，创建一个普通商品；
		MvcResult mr6 = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr6);
		//
		MvcResult mr61 = mvc.perform(get("/commoditySync/retrieveNEx.bx?status=-1&type=0") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr61);
		//
		// MvcResult mr62 = mvc.perform(//
		// get("/refCommodityHub/retrieveNByBarcodeEx.bx?pageIndex=1&pageSize=10&barcode=")//
		// .session((MockHttpSession) session)//
		// .contentType(MediaType.APPLICATION_JSON)//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print()).andReturn();
		// Shared.checkJSONErrorCode(mr62);
		// 创建普通商品3
		Commodity c3 = BaseCommodityTest.DataInput.getCommodity();
		c3.setName(CommonCommodity + getCommodityOrder());
		c3.setMultiPackagingInfo(getBarcode() + ";" + c3.getPackageUnitID() + ";" + c3.getRefCommodityMultiple() + ";" + c3.getPriceRetail() + ";" + c3.getPriceVIP() + ";" + c3.getPriceWholesale() + ";" + c3.getName() + ";");
		//
		BaseCommodityTest.createCommodityViaAction(c3, mvc, sessionBoss, mapBO, dbName);
	}

	@Test(dependsOnMethods = "enterWarehousePageAfterCreateCommodity")
	public void retrieveTotalWarehouseStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_WarehouseStock_", order, "查看库管查询页面的库存总额最高的商品和库存总额，再入库商品A的数量*价格>库管页面的库存总额，并审核入库单.再查看库管查询页面的库存总额最高的商品和库存总额是否被修改");

		// 由于测试需要，将PAGE_SIZE_MAX设置成很大的数，下面有还原
		int pageSize = BaseAction.PAGE_SIZE_MAX;
		BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;

		// 进入库管查询页面，查看库存总额最高的商品和库存总额
		// 查询所有商品，把商品的最近采购价和数量拿出来，用循环计算采购价*数量的总和，算出库存总额，并找出库存总额最高的商品。
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=" + BaseAction.INVALID_ID + "&" + Commodity.field.getFIELD_NAME_pageIndex() + "=" + BaseAction.PAGE_StartIndex + "&"
						+ Commodity.field.getFIELD_NAME_pageSize() + "=" + BaseAction.PAGE_SIZE_Infinite)//
								.session((MockHttpSession) sessionBoss)//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 还原初始值
		BaseAction.PAGE_SIZE_MAX = pageSize;
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<?> commoditysJSON = JsonPath.read(jsonObject, "$.objectList[*].commodity");
		List<Commodity> commodityList = new ArrayList<Commodity>();
		Commodity commodity = new Commodity();
		double totalWarehouseStock = 0;// 所有商品的库存总额
		double totalWarehouseStockOfCommodity = 0;// 单个商品的库存总额
		double maxTotalWarehouseStockOfCommodity = 0;// 最高的商品库存总额
		Commodity commodityOfMaxTotalWarehouseStock = new Commodity();// 最高库存总额的商品
		for (int i = 0; i < commoditysJSON.size(); i++) {
			commodity = (Commodity) commodity.parse1(commoditysJSON.get(i).toString());
			totalWarehouseStockOfCommodity = commodity.getLatestPricePurchase() * commodity.getNO();// 计算商品的库存总额
			totalWarehouseStock = totalWarehouseStock + totalWarehouseStockOfCommodity;// 计算总的库存总额
			commodityList.add(commodity);
			if (totalWarehouseStockOfCommodity > maxTotalWarehouseStockOfCommodity) {// 如果商品的库存总额大于最高的商品库存总额的商品
				maxTotalWarehouseStockOfCommodity = totalWarehouseStockOfCommodity;// 则更新商品的最高库存总额
				commodityOfMaxTotalWarehouseStock = commodity;// 更新最高库存总额的商品
			}
		}
		// 创建商品A
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setName("库管查询的商品2");
		c2.setMultiPackagingInfo(commodity38barcode + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject2 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity2 = new Commodity();
		commodity2 = (Commodity) commodity2.parse1(jsonObject2.getString(BaseAction.KEY_Object));
		// 检查点
		CommodityCP.verifyCreate(mr, c2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		// 拿出该商品的Barcodes
		MvcResult mr2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity2.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> listBarcodes = JsonPath.read(o2, "$.barcodesList[*]");
		Assert.assertTrue(listBarcodes.size() > 0, "查询条形码失败~");
		Barcodes barcode2 = new Barcodes();
		barcode2 = (Barcodes) barcode2.parse1(listBarcodes.get(0).toString());
		//
		// 创建商品的采购订单
		// 采购数量*采购价格>库存总额
		double purchasingPrice = totalWarehouseStock;
		int purchasingNO = 2;
		//
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, String.valueOf(commodity2.getID()))//
						.param(NOs, String.valueOf(purchasingNO)) //
						.param(PriceSuggestions, String.valueOf(purchasingPrice)) //
						.param(CommPurchasingUnit, "桶") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, String.valueOf(barcode2.getID())) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr3);
		JSONObject object = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		purchasingOrder1 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));
		// 创建采购订单的检查点
		PurchasingOrder purchasingOrder2 = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr3, purchasingOrder2, purchasingOrderBO, dbName);
		//
		// 审核采购订单
		MvcResult mr4 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder1.getRemark())//
						.param(CommIDs, String.valueOf(commodity2.getID()))//
						.param(NOs, String.valueOf(purchasingNO))//
						.param(PriceSuggestions, String.valueOf(purchasingPrice))//
						.param(BarcodeIDs, String.valueOf(barcode2.getID()))//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr4);
		// 审核采购订单的检查点
		PurchasingOrderCP.verifyApprove(purchasingOrder1, purchasingOrderBO, providerCommodityBO, dbName);
		//
		// 进入入库页面页面，创建商品A的入库单，并审核（入库的数量*价格>库管页面的库存总额）
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(commodity2.getID());
		String commNOs = String.valueOf(purchasingNO);
		String commPrices = String.valueOf(purchasingPrice);
		String amounts = String.valueOf(purchasingNO * purchasingPrice);
		String barcodeIDs = String.valueOf(barcode2.getID());
		Warehousing warehousing1 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		//
		// 审核入库单
		approveWarehousing(warehousing1, commIDs);
		//
		// 由于测试需要，将PAGE_SIZE_MAX设置成很大的数，下面有还原
		BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;
		// 进入库管查询页面，再次查看库存总额最高的商品和库存总额；
		// 查询所有商品，把商品的最近采购价和数量拿出来，用循环计算采购价*数量的总和，算出库存总额，并找出库存总额最高的商品。
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=" + BaseAction.INVALID_ID + "&" + Commodity.field.getFIELD_NAME_pageIndex() + "=" + BaseAction.PAGE_StartIndex + "&"
						+ Commodity.field.getFIELD_NAME_pageSize() + "=" + BaseAction.PAGE_SIZE_Infinite)//
								.session((MockHttpSession) sessionBoss)//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		// 还原初始值
		BaseAction.PAGE_SIZE_MAX = pageSize;
		JSONObject jsonObject3 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		commoditysJSON = JsonPath.read(jsonObject3, "$.objectList[*].commodity");
		commodityList = new ArrayList<Commodity>();
		Commodity commodityAfter = new Commodity();
		double totalWarehouseStockAfter = 0;// 所有商品的库存总额
		double totalWarehouseStockOfCommodityAfter = 0;// 单个商品的库存总额
		double maxTotalWarehouseStockOfCommodityAfter = 0;// 最高的商品库存总额
		for (int i = 0; i < commoditysJSON.size(); i++) {
			commodityAfter = (Commodity) commodityAfter.parse1(commoditysJSON.get(i).toString());
			totalWarehouseStockOfCommodityAfter = commodityAfter.getLatestPricePurchase() * commodityAfter.getNO();// 计算商品的库存总额
			totalWarehouseStockAfter = totalWarehouseStockAfter + totalWarehouseStockOfCommodityAfter;// 计算总的库存总额
			commodityList.add(commodityAfter);
			if (totalWarehouseStockOfCommodityAfter > maxTotalWarehouseStockOfCommodityAfter) {// 如果商品的库存总额大于最高的商品库存总额的商品
				maxTotalWarehouseStockOfCommodityAfter = totalWarehouseStockOfCommodityAfter;// 则更新商品的最高库存总额
				commodityOfMaxTotalWarehouseStock = (Commodity) commodityAfter.clone();// 更新最高库存总额的商品
			}
		}
		// 结果验证
		Assert.assertTrue(totalWarehouseStockAfter > totalWarehouseStock, "库存总额出错");
		Assert.assertTrue(maxTotalWarehouseStockOfCommodityAfter > maxTotalWarehouseStockOfCommodity, "单个商品最高库存总额出错");
		Assert.assertTrue(commodityOfMaxTotalWarehouseStock.getID() == commodity2.getID(), "最高库存总额的商品出错");
	}

	/** @param bmList
	 *            RN返回的需要同步的商品List */
	public List<Commodity> getCommodityListIfAllSync(List<BaseModel> bmList) { // ..
		List<Pos> posList = Shared.getPosesFromDB(posBO, dbName);
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(true, false);
		List<Commodity> commList = new ArrayList<Commodity>();

		BaseSyncCache baseSyncCache = new BaseSyncCache();
		for (BaseModel bm : bmList) {
			Commodity commodity = (Commodity) bm;

			// 获取该商品的同步块信息
			for (BaseModel bm2 : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm2;
				if (baseSyncCache.getSyncData_ID() == commodity.getID()) {
					if (baseSyncCache.getListSlave1() != null && baseSyncCache.getListSlave1().size() == (posList.size() - 1)) {
						commodity.setIsSync(1); // 设int1为1，表示该商品同步块已经完全同步
					}
				}
			}
			commList.add(commodity);
		}

		return commList;
	}

	private Commodity getCommodityCache(int commodityID) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	protected ReturnCommoditySheet createReturnCommoditySheet(String commIDs, String rcscNOs, String commPrices, String rcscSpecifications, String barcodeIDs, ReturnCommoditySheet rcs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(rcs.getProviderID())) //
						.param("commIDs", commIDs) //
						.param("rcscNOs", rcscNOs) //
						.param("commPrices", commPrices) //
						.param("rcscSpecifications", rcscSpecifications) //
						.param("barcodeIDs", barcodeIDs) //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 创建退货单的检查点
		ReturnCommoditySheetCP.verifyCreate(mr, rcs);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		ReturnCommoditySheet rcs2 = (ReturnCommoditySheet) rcs1.parse1(jsonObject.getJSONObject(BaseAction.KEY_Object).toString());
		return rcs2;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private WarehousingCommodity getCommodityWarehousing(Commodity commodity) {
		if (commodity.getCurrentWarehousingID() == 0) {
			Warehousing warehousing = new Warehousing();
			warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 查询所有审核过的入库单
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(dbName);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			if (warehousingList == null) {
				return null;
			}
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);

			for (Warehousing ws : warehousingList) {
				// 在缓存中获取入库单主从表信息
				Warehousing warehousingOfCache = (Warehousing) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(ws.getID(), BaseBO.SYSTEM, ecOut, dbName);

				for (Object o : warehousingOfCache.getListSlave1()) {
					WarehousingCommodity wsc = (WarehousingCommodity) o;
					if (wsc.getCommodityID() == commodity.getID()) {
						return wsc;
					}
				}
			}
		} else {
			Warehousing warehousing = new Warehousing();
			warehousing.setID(commodity.getCurrentWarehousingID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);

			for (Object o : bmList.get(1)) {
				WarehousingCommodity warehousingCommodity = (WarehousingCommodity) o;
				if (warehousingCommodity.getCommodityID() == commodity.getID()) {
					return warehousingCommodity;
				}
			}
		}
		return null;
	}

	protected Warehousing createWarehousing(Warehousing w, String commIDs, String commNOs, String commPrices, String amounts, String barcodeIDs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, commIDs) //
						.param(CommNOs, commNOs) //
						.param(CommPrices, commPrices) //
						.param(Amounts, amounts) //
						.param(BarcodeIDs, barcodeIDs) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 创建入库单的检查点
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		WarehousingCP.verifyCreate(mr, w, dbName);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());

		return warehousing1;
	}

	protected Warehousing approveWarehousing(Warehousing w, String commIDs) throws Exception, UnsupportedEncodingException {
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(w.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());
		// 审核入库单的检查点
		WarehousingCP.verifyApprove(mr, w, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
		return warehousing1;
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

}
