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
	protected final String CommonCommodity = "????????????";
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
	// ???????????????
	static Calendar calendar = Calendar.getInstance();
	private static final String beginMonth = calendar.get(GregorianCalendar.YEAR) + "/" + (calendar.get(GregorianCalendar.MONTH) + 1) + "/01 00:00:00";
	private static final String endMonth = calendar.get(GregorianCalendar.YEAR) + "/" + (calendar.get(GregorianCalendar.MONTH) + 2) + "/01 00:00:00";// ???????????????????????????bug

	/**
	 * ??????????????????1????????????MvcResult ?????????company????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????company????????????
	 */
	private static MvcResult mvcResult_Company;

	private static HttpSession session; // ??????????????????
	private static HttpSession session1; // pos??????

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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "?????????????????????????????????");

		// ???????????????????????????????????????????????????????????????
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
		// ????????????
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
		// ????????????????????????????????????????????????????????????????????????????????????MvcResult??????????????????????????????????????????????????????????????????
		mvcResult_Company = mr;
		reportMap.put("company", company);
	}

	@Test(dependsOnMethods = "retrieveNReport", timeOut = 180000)
	public void PreSaleRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "???????????????????????????????????????");

		// ??????????????????
		Company company = (Company) reportMap.get("company");
		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(jsonObject, "$.object.SN");
		session = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, companySN_New);
		// ??????????????????
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
		// ???company???????????????
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// ??????????????????????????????
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN_New, company.getBossPhone(), company.getBossPassword());
		// ???????????????BOSS????????????SESSION
		try {
			session = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
			// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "PreSaleRetrieveN")
	public void createSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "????????????????????????????????????");
		session = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		// ??????????????????53
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName("????????????53");
		c.setMultiPackagingInfo("6821423302394" + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity53 = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		reportMap.put("commodity53", commodity53);
		// ?????????53???barcode?????????
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
		Assert.assertTrue(listBarcodes.size() != 0, "?????????????????????");
		Barcodes barcode53 = new Barcodes();
		barcode53 = (Barcodes) barcode53.parse1(listBarcodes.get(0).toString());
		reportMap.put("barcode53", barcode53);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, String.valueOf(commodity53.getID()))//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, String.valueOf(barcode53.getID())) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr2, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		po = (PurchasingOrder) po.parse1(o1.getString(BaseAction.KEY_Object));
		// ??????????????????
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
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		PurchasingOrderCP.verifyApprove(po, purchasingOrderBO, providerCommodityBO, dbName);
		//
		JSONObject o2 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		PurchasingOrder po1 = new PurchasingOrder();
		po1 = (PurchasingOrder) po1.parse1(o2.getString(BaseAction.KEY_Object));
		// ??????
		// ????????????????????????53??????
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
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		WarehousingCP.verifyCreate(mr4, wh53, dbName);
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		Warehousing warehousing53 = (Warehousing) wh53.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ????????????
		// ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing53);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "??????????????????????????????????????????????????????????????????=" + warehousingBO.getLastErrorCode() + "???????????????=" + warehousingBO.getLastErrorMessage());
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
		// ??????????????????
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
		// ?????????
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_PartSuccess);
		WarehousingCP.verifyApprove(mr5, warehousing53, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);

		// ??????pos???
		Pos pos = BaseTestNGSpringContextTest.DataInput.getPOS();
		pos.setCompanySN(companySN);

		Pos pos1 = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, dbName);
		//
		reportMap.put("pos1", pos1);
		// Shared.resetPOS(mvc, posID);
		session1 = Shared.getPosLoginSession(mvc, pos1.getID(), bossPhone, BaseCompanyTest.bossPassword_New, companySN, pos1.getPasswordInPOS());
		// ??????
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		// ??????
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
		// ????????????
		createDailyReportSummary();
		//
		System.out.println(CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		// ????????????
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
		// ????????????
		// ...
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	protected void createDailyReportSummary() {
		// ???????????????????????????????????????
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 1);
		System.out.println("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		//
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cgStart.setName("RetailTradeDailyReportSummaryTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, dbName, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		TaskThread.deleteReportOfDateRun = 1; // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????key???????????????
		//
		drRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeDailyReportSummaryTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeDailyReportSummaryTaskThread;
		while (iRoundNew - iRoundOldRetailTradeDailyReportSummaryTaskThread < 1) {// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2); // ???????????????????????????????????????
			RetailTradeDailyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("????????????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);
		}
		System.out.println("?????????????????????????????????" + drRoundOld.getName() + "??????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);

		System.out.println("???????????????????????????????????????????????????????????????????????????????????????????????????");
		//
		RetailTradeDailyReportSummaryTaskThread.runOnce = false;
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = null;
		TaskThread.deleteReportOfDateRun = 0;
		// ??????????????????????????????????????????
		RetailTradeDailyReportSummary retailTradeDailyReportSummary = new RetailTradeDailyReportSummary();
		try {
			retailTradeDailyReportSummary.setDatetimeStart(new Date()); // ?????????????????? ??????????????????
			retailTradeDailyReportSummary.setDatetimeEnd(new Date());
		} catch (Exception e) {
			System.out.println("?????????????????????????????????????????????????????????????????????" + e.toString());
			return;
		}
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradeDailyReportSummary> bmList = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);
		if (retailTradeDailyReportSummaryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "???????????????????????????????????????" + retailTradeDailyReportSummaryBO.printErrorInfo());
		}
		assertTrue(bmList != null && bmList.size() == 1, "??????????????????");
	}

	@Test(dependsOnMethods = "createSale", timeOut = 180000)
	public void RetailTradeCommodityReturn() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "????????????????????????????????????");

		// ????????????
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
		// ?????????????????????????????????
		createDailyReportSummary();
		// ????????????
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
		// ????????????
	}

	@Test(dependsOnMethods = "RetailTradeCommodityReturn")
	public void NewMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");

		// ????????????
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
		// ????????????
	}

	@Test(dependsOnMethods = "NewMachineRetrieveN")
	public void TwoPosMeanwhileDBOper() {// ???????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_OperatingStatement_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");

		// ????????????
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("???????????????staff=" + staff);

		return staff;
	}

	private Commodity getCommodityCache(int commodityID, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "???????????????????????????????????????=" + ecOut.getErrorCode() + "???????????????=" + ecOut.getErrorMessage());
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
				assertTrue(false, "???????????????????????????????????????" + ecOut.getErrorCode());
			}

			List<Commodity> listSlave2 = new ArrayList<Commodity>();
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity multiCommodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodity.getRefCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
				if (multiCommodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "???????????????????????????????????????" + ecOut.getErrorCode());
				}
				// ?????????????????????????????????listSlave2???
				listSlave2.add(multiCommodity);
				commodity.setListSlave2(listSlave2);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				// ???????????????????????????
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(commodity.getID());
				//
				DataSourceContextHolder.setDbName(dbName);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "????????????????????????????????????" + retailTradeCommoditySourceBO.getLastErrorCode());
				}

				// ?????????????????????????????????????????????????????????listSlave2???
				for (SubCommodity sc : listSubCommodity) {
					Commodity subCommodityOfCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(sc.getSubCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
					if (subCommodityOfCache == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "???????????????????????????????????????" + ecOut.getErrorCode());
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
				assertTrue(wsc != null, "???????????????????????????????????????");

				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity normalCommodity = new Commodity();
				normalCommodity.setID(commodity.getRefCommodityID());
				normalCommodity.setCurrentWarehousingID(commodity.getCurrentWarehousingID());
				//
				wsc = getCommodityWarehousing(normalCommodity);
				assertTrue(wsc != null, "???????????????????????????????????????");
				//
				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				for (Object o : commodity.getListSlave2()) {
					Commodity normalCommodity = (Commodity) o;
					//
					wsc = getCommodityWarehousing(normalCommodity);
					assertTrue(wsc != null, "???????????????????????????????????????");
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
			// ????????????????????????PAGE_SIZE_MAX???????????????????????????????????????
			int pageSize = BaseAction.PAGE_SIZE_MAX;
			BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;

			// ?????????????????????
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(dbName);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);
			// ???????????????
			BaseAction.PAGE_SIZE_MAX = pageSize;
			for (Warehousing ws : warehousingList) {
				// ??????????????????????????????????????????
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
