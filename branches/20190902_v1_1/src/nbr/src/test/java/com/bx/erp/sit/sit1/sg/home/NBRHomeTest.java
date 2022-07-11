package com.bx.erp.sit.sit1.sg.home;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.TaskType.EnumTaskType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.task.RetailTradeDailyReportSummaryTaskThread;
import com.bx.erp.task.TaskManager;
import com.bx.erp.task.TaskThread;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class NBRHomeTest extends BaseActionTest {

	protected AtomicInteger order;

	protected String bossPhone = "13914126925";
	protected String dbName = "nbr_bxn";
	protected static String companySN;
	protected Map<String, BaseModel> nbrHomeMap;

	private static final String CommIDs = "commIDs";
	private static final String BarcodeIDs = "barcodeIDs";
	private static final String NOs = "NOs";
	private static final String PriceSuggestions = "priceSuggestions";
	private static final String CommNOs = "commNOs";
	private static final String CommPrices = "commPrices";
	private static final String CommPurchasingUnit = "commPurchasingUnit";
	private static final String ProviderID = "providerID";
	private static final String Amounts = "amounts";

	/** 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证 */
	private static MvcResult mvcResult_Company;

	private int iRoundOldRetailTradeDailyReportSummaryTaskThread;
	private RetailTradeDailyReportSummaryTaskThread drRoundOld;

	@BeforeClass
	public void beforeClass() throws Exception {
		super.setUp();

		order = new AtomicInteger();
		nbrHomeMap = new HashMap<String, BaseModel>();
		//
		drRoundOld = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
		System.out.println("drRoundOld hashcode=" + drRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportSummaryTaskThread = drRoundOld.getTaskStatusForTest().get();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

	@Test
	public void createCompanyAndViewHomePageData() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "新增门店时查看首页数据");

		// 创建新公司及门店
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Company companyCreate = new Company();
		companyCreate = (Company) companyCreate.parse1(jsonObject.getString(BaseAction.KEY_Object));
		companySN = companyCreate.getSN();
		company.setSN(companySN);
		nbrHomeMap.put("company", company);
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下个用例再做结果验证吧
		mvcResult_Company = mr1;
	}

	/** 创建公司之后一旦登录了老板账号进行首次修改密码，售前账号会被删除，所以把售前账号的测试放在创建公司后面 */
	@Test(dependsOnMethods = "createCompanyAndViewHomePageData")
	public void cannotUsePhoneOfPreSaleToCUDCommodityList() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "售前人员无法进行CUD操作");

		// 售前账号登录
		sessionPreSale = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, companySN);
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);
		//
		// 创建普通商品61
		Commodity c61 = BaseCommodityTest.DataInput.getCommodity();
		String barcode = "6821423302397";
		c61.setName("普通商品61");
		c61.setMultiPackagingInfo(barcode + ";" + c61.getPackageUnitID() + ";" + c61.getRefCommodityMultiple() + ";" + c61.getPriceRetail() + ";" + c61.getPriceVIP() + ";" + c61.getPriceWholesale() + ";" + c61.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c61, sessionPreSale) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
		//
		// ...刚创建的公司什么数据都没有，不能进行UD操作

		// 公司的检查点
		Company company = (Company) nbrHomeMap.get("company");
		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		companySN = JsonPath.read(jsonObject, "$.object.SN");
		// 结果验证
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN, company.getBossPhone(), company.getBossPassword());
		// 使用创建出来的BOSS账号登录SESSION
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	@Test(dependsOnMethods = "cannotUsePhoneOfPreSaleToCUDCommodityList", timeOut = 180000)
	public void createRetailTradeAndViewHomePageData() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "新增销售之后查看首页数据");

		// 创建商品61
		Commodity c61 = BaseCommodityTest.DataInput.getCommodity();
		String barcode = "6821423302397";
		c61.setName("普通商品61");
		c61.setMultiPackagingInfo(barcode + ";" + c61.getPackageUnitID() + ";" + c61.getRefCommodityMultiple() + ";" + c61.getPriceRetail() + ";" + c61.getPriceVIP() + ";" + c61.getPriceWholesale() + ";" + c61.getName() + ";");
		Commodity commodity61 = BaseCommodityTest.createCommodityViaAction(c61, mvc, sessionBoss, mapBO, dbName);
		nbrHomeMap.put("commodity61", commodity61);
		// 拿出该商品的Barcodes
		Barcodes barcode61 = retrieveBarcode(commodity61);
		nbrHomeMap.put("barcode61", barcode61);
		//
		// 创建采购订单
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, String.valueOf(commodity61.getID()))//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "桶") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, String.valueOf(barcode61.getID())) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查点
		Shared.checkJSONErrorCode(mr3);
		// 把采购单解析出来
		JSONObject jsonObject3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder61 = new PurchasingOrder();
		purchasingOrder61 = (PurchasingOrder) purchasingOrder61.parse1(jsonObject3.getString(BaseAction.KEY_Object));
		PurchasingOrderCP.verifyCreate(mr3, purchasingOrder61, purchasingOrderBO, dbName);
		//
		// 审核采购订单
		MvcResult mr4 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder61.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder61.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder61.getRemark())//
						.param(CommIDs, String.valueOf(commodity61.getID()))//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, String.valueOf(barcode61.getID()))//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr4);
		JSONObject jsonObject4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		purchasingOrder61 = (PurchasingOrder) purchasingOrder61.parse1(jsonObject4.getString(BaseAction.KEY_Object));
		nbrHomeMap.put("purchasingOrder61", purchasingOrder61);
		// 审核采购订单的检查点
		PurchasingOrderCP.verifyApprove(purchasingOrder61, purchasingOrderBO, providerCommodityBO, dbName);
		//
		// 创建入库单
		Warehousing w61 = new Warehousing();
		w61.setProviderID(1);
		w61.setWarehouseID(1);
		String commIDs = String.valueOf(commodity61.getID());
		String commNOs = "28";
		String commPrices = "99";
		String amounts = "2772";
		String barcodeIDs = String.valueOf(barcode61.getID());
		Warehousing warehousing61 = createWarehousing(w61, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		//
		// 审核入库单
		approveWarehousing(warehousing61, commIDs);
		//
		// 创建POS机并使用POS机登录
		Pos pos = createExPosSync();
		nbrHomeMap.put("pos", pos);
		sessionBoss = (MockHttpSession) Shared.getPosLoginSession(mvc, pos.getID(), bossPhone, BaseCompanyTest.bossPassword_New, companySN, pos.getPasswordInPOS());
		//
		// 新增销售商品61
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		// 获取零售前的商品当值入库单
		Warehousing currentWarehousing = new Warehousing();
		List<WarehousingCommodity> wscList = BaseRetailTradeTest.getWarehousingCommodityList(commList, warehousingBO, dbName);
		currentWarehousing.setListSlave1(wscList);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPos_ID(pos.getID());
		rt.setPaymentType(1);
		rt.setStaffID(getStaffFromSession(sessionBoss).getID()); // 登录的staffID
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(commodity61.getID());
		rtc.setNO(10);
		rtc.setNOCanReturn(rtc.getNO());
		rtc.setBarcodeID(barcode61.getID());
		rt.setAmount(rtc.getNO() * rtc.getPriceOriginal());
		rt.setAmountCash(rt.getAmount());
		//
		RetailTrade retailTrade61 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, dbName);
		nbrHomeMap.put("retailTrade61", retailTrade61);
		//
		// 生成报表
		createDailyReportSummary();// xxx
		// ...查看首页数据
	}

	@Test(dependsOnMethods = "createRetailTradeAndViewHomePageData", timeOut = 180000)
	public void buyAndReturnCommodityThenViewHomePageData() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "购买退货之后查看首页数据");

		// 新增商品1的购买退货
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(nbrHomeMap.get("commodity61").getID());
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setPriceOriginal(222.6);
		retailTradeCommodity.setBarcodeID(nbrHomeMap.get("barcode61").getID());
		retailTradeCommodity.setNOCanReturn(retailTradeCommodity.getNO());
		retailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceOriginal());
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		//
		rt.setListSlave1(listRetailTradeCommodity);
		rt.setPos_ID(nbrHomeMap.get("pos").getID());
		rt.setSourceID(nbrHomeMap.get("retailTrade61").getID());
		rt.setStaffID(getStaffFromSession(sessionBoss).getID());
		rt.setAmount(retailTradeCommodity.getNO() * retailTradeCommodity.getPriceOriginal());
		rt.setAmountCash(rt.getAmount());

		RetailTrade retailTrade2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, dbName);

		nbrHomeMap.put("retailTrade2", retailTrade2);
		// 运行夜间任务，生成报表
		createDailyReportSummary();// xxx

		// ...查看首页数据
	}

	@Test(dependsOnMethods = "buyAndReturnCommodityThenViewHomePageData")
	public void useLocalComputerCURDNBRHomePageThenUseOtherComputerLoginAndRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDNBRHomePageThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDNBRHomePage() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Home_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

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

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
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

	protected Commodity getCommodityCache(int commodityID) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	protected Pos createExPosSync() throws Exception, UnsupportedEncodingException {
		HttpSession sessionOP = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		Pos pos = BaseTestNGSpringContextTest.DataInput.getPOS();
		pos.setCompanySN(companySN);
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int posID = JsonPath.read(o, "$.object.ID");
		String passwordInPOS = JsonPath.read(o, "$.object.passwordInPOS");
		pos.setID(posID);
		pos.setPasswordInPOS(passwordInPOS);
		//
		Pos posCopy = (Pos) pos.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		// 创建pos的检查点
		verifySyncCreateResult(mr, pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, EnumCacheType.ECT_POS, EnumSyncCacheType.ESCT_PosSyncInfo, posID, dbName);// posID > 0代表pos机发送的请求
		//
		pos = (Pos) posCopy.clone(); // 恢复原样
		//
		return pos;
	}

	// String json = JSON.toJSONString(rt);
	// //
	// MvcResult mr = mvc.perform(//
	// post("/retailTrade/createEx.bx") //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) session) //
	// .param("retailTrade", json) //
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	// //
	// JSONObject returnJson =
	// JSONObject.fromObject(mr.getResponse().getContentAsString());
	// RetailTrade retailTrade = new RetailTrade();
	// retailTrade = (RetailTrade)
	// retailTrade.parse1(returnJson.getString(BaseAction.KEY_Object));
	// Assert.assertTrue(retailTrade != null, "返回数据为空，这不是预计的情况");
	// Map<Integer, List<Warehousing>> mapAfterSale =
	// queryWarehousingAfterSale(mapBeforeSale, dbName, warehousingBO);
	// if (retailTrade.getStatus() ==
	// RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
	// retailTrade.setIgnoreSlaveListInComparision(true);
	// }
	// // 零售单的检查点
	// RetailTradeCP.verifyCreate(retailTrade, rt, mapBeforeSale, mapAfterSale,
	// simpleCommodityList, mapBO, Shared.DBName_Test);
	//
	// return retailTrade;
	// }

	@SuppressWarnings({ "static-access", "unchecked" })
	protected void createDailyReportSummary() {
		// 为了马上测试，马上生成报表
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 1);
		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【销售日报表推送】");
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cgStart.setName("RetailTradeDailyReportSummaryTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, dbName, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		TaskThread.deleteReportOfDateRun = 1; // 让夜间任务顺利生成报表（夜间任务默认不删除旧报表。如果这里不删除测试生成的报表，夜间任务会重复插入key导致失败）
		//
		drRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeDailyReportSummaryTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeDailyReportSummaryTaskThread;
		while (iRoundNew - iRoundOldRetailTradeDailyReportSummaryTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);
			drRoundNow = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送销售日报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);
		}
		System.out.println("夜间推送销售日报表线程" + drRoundOld.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);

		System.out.println("【夜间推送销售日报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = null;
		TaskThread.deleteReportOfDateRun = 0;
		// 从数据库中获取刚生成的日报表
		RetailTradeDailyReportSummary retailTradeDailyReportSummary = new RetailTradeDailyReportSummary();
		try {
			retailTradeDailyReportSummary.setDatetimeStart(new Date()); // 根据日期查询 ，时分秒无效
			retailTradeDailyReportSummary.setDatetimeEnd(new Date());
		} catch (Exception e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradeDailyReportSummary> bmList = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);
		if (retailTradeDailyReportSummaryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询日报表出错，错误信息：" + retailTradeDailyReportSummaryBO.printErrorInfo());
		}
		assertTrue(bmList != null && bmList.size() == 1, "查询报表有误");
	}

	/** 本测试使用，用于查询商品的第一个条形码 */
	protected Barcodes retrieveBarcode(Commodity commodity) throws Exception, UnsupportedEncodingException {
		MvcResult mr2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> listBarcodes = JsonPath.read(jsonObject2, "$.barcodesList[*]");
		Assert.assertTrue(listBarcodes.size() > 0, "查询条形码失败~");
		Barcodes barcode = new Barcodes();
		barcode = (Barcodes) barcode.parse1(listBarcodes.get(0).toString());
		return barcode;
	}
}
