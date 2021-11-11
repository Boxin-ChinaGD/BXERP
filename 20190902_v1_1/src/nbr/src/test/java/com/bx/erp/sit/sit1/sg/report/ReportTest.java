package com.bx.erp.sit.sit1.sg.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.bx.erp.model.TaskType.EnumTaskType;
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
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
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
import com.bx.erp.test.BasePosTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ReportTest extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicLong barcode;
	protected AtomicInteger commodityOrder;
	//
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected Map<String, BaseModel> reportMap;
	protected final String CommonCommodity = "普通商品";
	//
	private static final String CommIDs = "commIDs";
	private static final String BarcodeIDs = "barcodeIDs";
	private static final String NOs = "NOs";
	private static final String PriceSuggestions = "priceSuggestions";
	private static final String CommNOs = "commNOs";
	private static final String CommPrices = "commPrices";
	private static final String CommPurchasingUnit = "commPurchasingUnit";
	private static final String ProviderID = "providerID";
	private static final String Amounts = "amounts";
	// 月初和月末
	static Calendar calendar = Calendar.getInstance();
	private static final String beginMonth = calendar.get(GregorianCalendar.YEAR) + "/" + (calendar.get(GregorianCalendar.MONTH) + 1) + "/01 00:00:00";
	private static final String endMonth = calendar.get(GregorianCalendar.YEAR) + "/" + (calendar.get(GregorianCalendar.MONTH) + 2) + "/01 00:00:00";// 这个会存在这一秒的bug

	/**
	 * 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证
	 */
	private static MvcResult mvcResult_Company;

	private static HttpSession session; // 老板账号登录
	private static HttpSession session1; // pos登录

	private int iRoundOldRetailTradeDailyReportSummaryTaskThread;
	private RetailTradeDailyReportSummaryTaskThread drRoundOld;

	@BeforeClass
	public void beforeClass() throws Exception {
		super.setUp();

		reportMap = new HashMap<String, BaseModel>();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(71);
		barcode = new AtomicLong();
		barcode.set(6821423302506L);
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
	public void retrieveNReport() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "新增门店时查看经营报告");

		// 创建一个新公司，查看相关数据（数据应为空）
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		companySN = ((Company) Shared.parse1Object(mr, company, BaseAction.KEY_Object)).getSN();
		//
		session = Shared.getStaffLoginSession(mvc, company.getBossPhone(), company.getBossPassword(), companySN);
		// 查看报表
		MvcResult mr1 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), beginMonth) //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), endMonth) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到售前账号的用例做完再做结果验证吧
		mvcResult_Company = mr;
		reportMap.put("company", company);
	}

	@Test(dependsOnMethods = "retrieveNReport", timeOut = 180000)
	public void PreSaleRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "售前账号无法查看到任何东西");

		// 售前账号登录
		Company company = (Company) reportMap.get("company");
		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(jsonObject, "$.object.SN");
		session = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, companySN_New);
		// 售前查看报表
		MvcResult mr = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), beginMonth) //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), endMonth) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		//
		// 做company的结果验证
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN_New, company.getBossPhone(), company.getBossPassword());
		// 使用原来的BOSS账号登录SESSION
		try {
			session = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
			// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "PreSaleRetrieveN")
	public void createSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "新增销售之后查看经营报告");
		session = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		// 创建普通商品53
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName("普通商品53");
		c.setMultiPackagingInfo("6821423302394" + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查点
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity53 = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		reportMap.put("commodity53", commodity53);
		// 把商品53的barcode查出来
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity53.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);
		JSONObject jsonObject2 = JSONObject.fromObject(mrl2.getResponse().getContentAsString());
		List<?> listBarcodes = JsonPath.read(jsonObject2, "$.barcodesList[*]");
		Assert.assertTrue(listBarcodes.size() != 0, "查询条形码失败");
		Barcodes barcode53 = new Barcodes();
		barcode53 = (Barcodes) barcode53.parse1(listBarcodes.get(0).toString());
		reportMap.put("barcode53", barcode53);
		// 创建采购订单
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, String.valueOf(commodity53.getID()))//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "瓶") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, String.valueOf(barcode53.getID())) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr2, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		po = (PurchasingOrder) po.parse1(o1.getString(BaseAction.KEY_Object));
		// 审核采购订单
		MvcResult mr3 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po.getRemark())//
						.param(CommIDs, String.valueOf(commodity53.getID()))//
						.param(BarcodeIDs, String.valueOf(barcode53.getID()))//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12.5")//
						.session((MockHttpSession) session) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 检查点
		Shared.checkJSONErrorCode(mr3);
		PurchasingOrderCP.verifyApprove(po, purchasingOrderBO, providerCommodityBO, dbName);
		//
		JSONObject o2 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		PurchasingOrder po1 = new PurchasingOrder();
		po1 = (PurchasingOrder) po1.parse1(o2.getString(BaseAction.KEY_Object));
		// 入库
		// 新建入库单使商品53入库
		Warehousing wh53 = new Warehousing();
		wh53.setProviderID(1);
		wh53.setWarehouseID(1);
		wh53.setPurchasingOrderID(po1.getID());
		wh53.setStaffID(getStaffFromSession(session).getID());
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po1.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(CommIDs, String.valueOf(commodity53.getID())) //
						.param(CommNOs, "20") //
						.param(CommPrices, "12.5") //
						.param(Amounts, "250") //
						.param(BarcodeIDs, String.valueOf(barcode53.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr4);
		WarehousingCP.verifyCreate(mr4, wh53, dbName);
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		Warehousing warehousing53 = (Warehousing) wh53.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 审核入库
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing53);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		StringBuffer commodityIDs = new StringBuffer();
		StringBuffer commodityBarcodeIDs = new StringBuffer();
		StringBuffer warehousingCommodityNOs = new StringBuffer();
		StringBuffer commodityPrices = new StringBuffer();
		StringBuffer commodityAmounts = new StringBuffer();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
			commodityIDs.append(whc.getCommodityID() + ",");
			commodityBarcodeIDs.append(whc.getBarcodeID() + ",");
			warehousingCommodityNOs.append(whc.getNO() + ",");
			commodityPrices.append(whc.getPrice() + ",");
			commodityAmounts.append(whc.getAmount() + ",");
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = getCommodityCache(whc.getCommodityID(), dbName);
			commList.add(commodityCache);
		}

		MvcResult mr5 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing53.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing53.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing53.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_PartSuccess);
		WarehousingCP.verifyApprove(mr5, warehousing53, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);

		// 创建pos机
		Pos pos = BaseTestNGSpringContextTest.DataInput.getPOS();
		pos.setCompanySN(companySN);

		Pos pos1 = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, dbName);
		//
		reportMap.put("pos1", pos1);
		// Shared.resetPOS(mvc, posID);
		session1 = Shared.getPosLoginSession(mvc, pos1.getID(), bossPhone, BaseCompanyTest.bossPassword_New, companySN, pos1.getPasswordInPOS());
		// 零售
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		// 从表
		RetailTradeCommodity retailTradeCommodity53 = new RetailTradeCommodity();
		retailTradeCommodity53.setCommodityID(commodity53.getID());
		retailTradeCommodity53.setNO(10);
		retailTradeCommodity53.setPriceOriginal(222.6);
		retailTradeCommodity53.setBarcodeID(barcode53.getID());
		retailTradeCommodity53.setNOCanReturn(retailTradeCommodity53.getNO());
		retailTradeCommodity53.setPriceReturn(retailTradeCommodity53.getPriceOriginal());
		//
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity53);
		//
		rt.setAmount(retailTradeCommodity53.getNO() * retailTradeCommodity53.getPriceOriginal());
		rt.setAmountAlipay(rt.getAmount());
		rt.setListSlave1(listRetailTradeCommodity);
		rt.setPos_ID(pos1.getID());
		rt.setStaffID(getStaffFromSession(session1).getID());
		rt.setPaymentType(2);
		rt.setVipID(0);
		rt.setAmountCash(0.00d);
		rt.setSn(Shared.generateRetailTradeSN(rt.getPos_ID()));

		RetailTrade retailTrade1 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rt, dbName);
		reportMap.put("retailTrade1", retailTrade1);
		// 生成报表
		createDailyReportSummary();
		//
		System.out.println(CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		// 查看报表
		MvcResult mr8 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), beginMonth) //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), endMonth) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr8);
		// 结果验证
		// ...
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	protected void createDailyReportSummary() {
		// 为了马上测试，马上生成报表
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 1);
		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【销售日报表推送】");
		//
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
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2); // 传入当前时间，进行报表生成
			RetailTradeDailyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送销售日报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);
		}
		System.out.println("夜间推送销售日报表线程" + drRoundOld.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);

		System.out.println("【夜间推送销售日报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeDailyReportSummaryTaskThread.runOnce = false;
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

	@Test(dependsOnMethods = "createSale", timeOut = 180000)
	public void RetailTradeCommodityReturn() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "购买退货之后查看经营报告");

		// 零售退货
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(reportMap.get("commodity53").getID());
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setPriceOriginal(222.6);
		retailTradeCommodity.setBarcodeID(reportMap.get("barcode53").getID());
		retailTradeCommodity.setNOCanReturn(10 - retailTradeCommodity.getNO());
		retailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceOriginal());
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		//
		rt.setAmount(retailTradeCommodity.getNO() * retailTradeCommodity.getPriceOriginal());
		rt.setAmountAlipay(rt.getAmount());
		rt.setListSlave1(listRetailTradeCommodity);
		rt.setPos_ID(reportMap.get("pos1").getID());
		rt.setSourceID(reportMap.get("retailTrade1").getID());
		rt.setStaffID(getStaffFromSession(session).getID());
		rt.setSn(Shared.generateRetailTradeSN(rt.getPos_ID()));
		rt.setPaymentType(2);
		rt.setAmountCash(0.00d);
		rt.setVipID(0);
		
		RetailTrade retailTrade2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rt, dbName);
		reportMap.put("retailTrade2", retailTrade2);
		// 运行夜间任务，生成报表
		createDailyReportSummary();
		// 查看报表
		MvcResult mr1 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), beginMonth) //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), endMonth) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		// 结果验证
	}

	@Test(dependsOnMethods = "RetailTradeCommodityReturn")
	public void NewMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		// 查看报表
		MvcResult mr = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), beginMonth) //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), endMonth) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// 结果验证
	}

	@Test(dependsOnMethods = "NewMachineRetrieveN")
	public void TwoPosMeanwhileDBOper() {// 后台做不到
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

		// 结果验证
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	private Commodity getCommodityCache(int commodityID, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private List<Commodity> getCommodityListBefourRetail(RetailTrade retailTrade, String dbName) {
		List<Commodity> commList = new ArrayList<Commodity>();
		Commodity commodity = new Commodity();
		ErrorInfo ecOut = new ErrorInfo();
		for (Object o : retailTrade.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;

			commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (commodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
			}

			List<Commodity> listSlave2 = new ArrayList<Commodity>();
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity multiCommodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodity.getRefCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
				if (multiCommodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
				}
				// 将多包装的单品信息放到listSlave2中
				listSlave2.add(multiCommodity);
				commodity.setListSlave2(listSlave2);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				// 获取组合商品子商品
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(commodity.getID());
				//
				DataSourceContextHolder.setDbName(dbName);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询子商品失败，错误码：" + retailTradeCommoditySourceBO.getLastErrorCode());
				}

				// 查询到组合商品的子商品，将单品信息放到listSlave2中
				for (SubCommodity sc : listSubCommodity) {
					Commodity subCommodityOfCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(sc.getSubCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
					if (subCommodityOfCache == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
					}

					listSlave2.add(subCommodityOfCache);
				}
				commodity.setListSlave2(listSlave2);
			}
			commList.add(commodity);
		}
		return commList;
	}

	@SuppressWarnings("unused")
	private List<WarehousingCommodity> getWarehousingCommodityList(List<Commodity> commList) {
		WarehousingCommodity wsc = new WarehousingCommodity();
		List<WarehousingCommodity> wscList = new ArrayList<WarehousingCommodity>();
		for (Commodity commodity : commList) {
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
				wsc = getCommodityWarehousing(commodity);
				assertTrue(wsc != null, "该商品没有入库单，不能零售");

				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity normalCommodity = new Commodity();
				normalCommodity.setID(commodity.getRefCommodityID());
				normalCommodity.setCurrentWarehousingID(commodity.getCurrentWarehousingID());
				//
				wsc = getCommodityWarehousing(normalCommodity);
				assertTrue(wsc != null, "该商品没有入库单，不能零售");
				//
				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				for (Object o : commodity.getListSlave2()) {
					Commodity normalCommodity = (Commodity) o;
					//
					wsc = getCommodityWarehousing(normalCommodity);
					assertTrue(wsc != null, "该商品没有入库单，不能零售");
					//
					wscList.add(wsc);
				}
			}
		}
		return wscList;
	}

	@SuppressWarnings("unchecked")
	private WarehousingCommodity getCommodityWarehousing(Commodity commodity) {
		if (commodity.getCurrentWarehousingID() == 0) {
			Warehousing warehousing = new Warehousing();
			warehousing.setID(BaseAction.INVALID_ID);
			warehousing.setProviderID(BaseAction.INVALID_ID);
			warehousing.setWarehouseID(BaseAction.INVALID_ID);
			warehousing.setStaffID(BaseAction.INVALID_ID);
			warehousing.setPurchasingOrderID(BaseAction.INVALID_ID);
			warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 由于测试需要，将PAGE_SIZE_MAX设置成很大的数，下面有还原
			int pageSize = BaseAction.PAGE_SIZE_MAX;
			BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;

			// 查询所有入库单
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(dbName);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);
			// 还原初始值
			BaseAction.PAGE_SIZE_MAX = pageSize;
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

}
