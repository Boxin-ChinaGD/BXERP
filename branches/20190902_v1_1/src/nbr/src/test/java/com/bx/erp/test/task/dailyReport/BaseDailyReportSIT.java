package com.bx.erp.test.task.dailyReport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.TaskType.EnumTaskType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.task.RetailTradeDailyReportSummaryTaskThread;
import com.bx.erp.task.RetailTradeMonthlyReportSummaryTaskThread;
import com.bx.erp.task.TaskManager;
import com.bx.erp.task.TaskThread;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.TaskCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseDailyReportSIT extends BaseActionTest {

	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
	public static final int RETURN_OBJECT = 1;
	public static final String SALE_DATE = "2030-01-02";
	// ????????????
	private static final String KEY_COMMIDS = "commIDs";
	private static final String KEY_BARCODEIDS = "barcodeIDs";
	private static final String KEY_NOS = "NOs";
	private static final String KEY_PRICESUGGESTIONS = "priceSuggestions";
	private static final String KEY_COMMPURCHASINGUNIT = "commPurchasingUnit";
	private static final String KEY_PROVIDERID = "providerID";
	private static final String KEY_SHOPID = "shopID";
	// ????????????
	private static final String COMMIDS = "commIDs";
	private static final String COMMNOS = "commNOs";
	private static final String COMMPRICES = "commPrices";
	private static final String AMOUNTS = "amounts";
	private static final String BARCODEIDS = "barcodeIDs";

	private RetailTradeDailyReportSummaryTaskThread drRoundOld;
	private RetailTradeMonthlyReportSummaryTaskThread mrRoundOld;
	private int iRoundOldRetailTradeDailyReportSummaryTaskThread;

	// ?????????????????????????????????1
	protected static int retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();
	private int iRoundOldRetailTradeMonthlyReportSummaryTaskThread;

	@SuppressWarnings({ "static-access", "unchecked" })
	public void runRetailTradeMonthlyReportSummaryTaskThread(Date targetDate, RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary) throws Exception {
		mrRoundOld = (RetailTradeMonthlyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
		iRoundOldRetailTradeMonthlyReportSummaryTaskThread = mrRoundOld.getTaskStatusForTest().get();

		System.out.println("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		RetailTradeMonthlyReportSummaryTaskThread.reportDateForTest = targetDate;
		//
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime);
		cgStart.setName("RetailTradeMonthlyReportSummaryTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		mrRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeMonthlyReportSummaryTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeMonthlyReportSummaryTaskThread;
		while (iRoundNew - iRoundOldRetailTradeMonthlyReportSummaryTaskThread < 1) {// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			try {
				Thread.currentThread().sleep(timeSpanToCheckTaskStartTime + 100); // ?????????????????????????????????
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeMonthlyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);
			RetailTradeMonthlyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeMonthlyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("????????????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeMonthlyReportSummaryTaskThread=" + iRoundOldRetailTradeMonthlyReportSummaryTaskThread);
		}
		System.out.println("?????????????????????????????????" + drRoundNow.getName() + "??????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeMonthlyReportSummaryTaskThread=" + iRoundOldRetailTradeMonthlyReportSummaryTaskThread);

		System.out.println("???????????????????????????????????????????????????????????????????????????????????????????????????");
		//
		RetailTradeMonthlyReportSummaryTaskThread.reportDateForTest = null; //
		RetailTradeMonthlyReportSummaryTaskThread.runOnce = false;
		// ??????????????????????????????????????????????????????????????????
		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(DatetimeUtil.getStartTime((DatetimeUtil.getFirstDayDateOfMonth(targetDate))));
		retailTradeMonthlyReportSummary.setDatetimeEnd(DatetimeUtil.getEndTime((DatetimeUtil.getFirstDayDateOfMonth(targetDate))));
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeMonthlyReportSummary> rtmReport = (List<RetailTradeMonthlyReportSummary>) retailTradeMonthlyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "???????????????????????????????????????" + retailTradeMonthlyReportSummaryBO.printErrorInfo());
		}
		assertTrue(rtmReport != null && rtmReport.size() > 0, "???????????????????????????");
		//
		TaskCP.verifyRetailTradeMonthlyReportSummaryTask(rtmReport.get(0), expectRetailTradeMonthlyReportSummary);
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void runRetailTradeDailyReportSummaryTaskThread(Date saleDate, Date reprotDate, RetailTradeDailyReportSummary expectDailyReportSummary, List<RetailTradeDailyReportByCommodity> expectDailyReportByCommodityList) throws Exception {

		drRoundOld = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
		System.out.println("drRoundOld hashcode=" + drRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportSummaryTaskThread = drRoundOld.getTaskStatusForTest().get();
		// ??????????????????????????????
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = reprotDate;
		TaskThread.deleteReportOfDateRun = EnumBoolean.EB_Yes.getIndex();
		System.out.println("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime);
		cgStart.setName("RetailTradeDailyReportSummaryTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		drRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeDailyReportSummaryTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeDailyReportSummaryTaskThread;
		while (iRoundNew - iRoundOldRetailTradeDailyReportSummaryTaskThread < 1) {// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			try {
				Thread.currentThread().sleep(timeSpanToCheckTaskStartTime + 100); // ?????????????????????????????????
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// ????????????????????????????????????00:00????????????report???date???????????????
			RetailTradeDailyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("????????????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);
		}
		System.out.println("?????????????????????????????????" + drRoundNow.getName() + "??????????????????????????????????????????iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);

		System.out.println("???????????????????????????????????????????????????????????????????????????????????????????????????");
		//
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = null;
		TaskThread.deleteReportOfDateRun = EnumBoolean.EB_NO.getIndex();
		RetailTradeDailyReportSummaryTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		// ?????????????????????????????????????????????????????????????????????
		RetailTradeDailyReportSummary retailTradeDailyReportSummary = new RetailTradeDailyReportSummary();
		retailTradeDailyReportSummary.setDatetimeStart(DatetimeUtil.getStartTime(saleDate));
		retailTradeDailyReportSummary.setDatetimeEnd(DatetimeUtil.getEndTime(saleDate));
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportSummary> bmList = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);
		if (retailTradeDailyReportSummaryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "???????????????????????????????????????" + retailTradeDailyReportSummaryBO.printErrorInfo());
		}
		if (expectDailyReportSummary.getRatioGrossMargin() == 0d) {
			assertTrue(bmList != null, "?????????????????????????????????0??????????????????");
		} else {
			if (bmList != null && bmList.size() == 1) {
				System.out.println("???????????????????????????=" + bmList);
			} else {
				retailTradeDailyReportSummaryBO.printErrorInfo(Shared.DBName_Test, retailTradeDailyReportSummary);
				assertTrue(false, "?????????????????????????????????DB????????????????????????DB?????????????????????");
			}
			//
			TaskCP.verifyRetailTradeDailyReportSummaryTask(bmList.get(0), expectDailyReportSummary);

			// ????????????????????????????????????????????????????????????????????????
			RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity = new RetailTradeDailyReportByCommodity();
			retailTradeDailyReportByCommodity.setDatetimeStart(DatetimeUtil.getStartTime(saleDate));
			retailTradeDailyReportByCommodity.setDatetimeEnd(DatetimeUtil.getEndTime(saleDate));
			retailTradeDailyReportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<RetailTradeDailyReportByCommodity> retailTradeDailyReportByCommodityList = (List<RetailTradeDailyReportByCommodity>) retailTradeDailyReportByCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
					retailTradeDailyReportByCommodity);
			if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "??????????????????????????????????????????" + retailTradeDailyReportByCommodityBO.printErrorInfo());
			}

			TaskCP.verifyRetailTradeDailyReportByCommodityTask(retailTradeDailyReportByCommodityList, expectDailyReportByCommodityList);
		}

	}

	public static class DataInput {
		private static Barcodes barcodesInput = null;

		protected static final Barcodes getBarcodes(int CommodityID) throws CloneNotSupportedException {
			barcodesInput = new Barcodes();
			barcodesInput.setCommodityID(CommodityID);
			barcodesInput.setBarcode("354" + System.currentTimeMillis() % 1000000);
			barcodesInput.setOperatorStaffID(STAFF_ID3); // staffID

			return (Barcodes) barcodesInput.clone();
		}

		public static RetailTrade getRetailTrade(Date saleDateTime, int staffID, double amount, int sourceID) throws InterruptedException {
			Random ran = new Random();

			RetailTrade retailTrade = new RetailTrade();
			retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
			Thread.sleep(1);
			retailTrade.setVipID(1);
			retailTrade.setPos_ID(1);
			retailTrade.setSn(Shared.generateRetailTradeSN(5));
			retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTrade.setSaleDatetime(saleDateTime);
			retailTrade.setLogo("11" + ran.nextInt(1000));
			retailTrade.setStaffID(staffID);
			retailTrade.setShopID(Shared.DEFAULT_Shop_ID);
			retailTrade.setPaymentType(1);
			retailTrade.setPaymentAccount("12");
			retailTrade.setRemark("11111");
			retailTrade.setSourceID(sourceID == 0 ? BaseAction.INVALID_ID : sourceID);
			retailTrade.setAmount(amount);
			retailTrade.setAmountCash(amount);
			retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
			retailTrade.setSyncDatetime(new Date());
			retailTrade.setReturnObject(1);// ???????????????
			retailTrade.setWxOrderSN("");
			retailTrade.setAliPayOrderSN("");
			retailTrade.setWxRefundNO("0");
			retailTrade.setDatetimeStart(new Date());
			retailTrade.setDatetimeEnd(new Date());
			Thread.sleep(1);

			return retailTrade;
		}

	}

	/** ??????????????????dailyReport????????????????????????????????????????????????BaseCommodityTest */
	public Commodity createNormalCommodity(double price, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPriceRetail(price);
		c.setPriceVIP(price);
		c.setPriceWholesale(price);
		c.setName("????????????A" + System.currentTimeMillis() % 1000000);
		c.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "????????????A" + System.currentTimeMillis() % 1000000 + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	/** ?????????????????????????????????????????????????????? ??????dailyReport????????????????????????????????????????????????BaseCommodityTest */
	public Commodity createCommodityIncludeMultiPackages(MockMvc mvc, HttpSession session, String providerIDs, String multiPackagingInfo, Commodity c) throws Exception {
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		c.setName("????????????" + System.currentTimeMillis() % 1000000);
		c.setProviderIDs(providerIDs);
		c.setMultiPackagingInfo(multiPackagingInfo);
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	/** ????????????????????????????????????dailyReport????????????????????????????????????????????????BaseCommodityTest */
	public Commodity createSubCommodity(Commodity[] commodityList, Integer[] subCommodityNOList, Double[] subCommodityPriceList, double price, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		Commodity combinationCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		for (int i = 0; i < commodityList.length; i++) {
			SubCommodity subCommodity = new SubCommodity();
			subCommodity.setSubCommodityNO(subCommodityNOList[i]);
			subCommodity.setSubCommodityID(commodityList[i].getID());
			subCommodity.setPrice(subCommodityPriceList[i]);
			subCommodityList.add(subCommodity);
		}
		combinationCommodity.setListSlave1(subCommodityList);
		//
		combinationCommodity.setPriceRetail(price);
		combinationCommodity.setPriceVIP(price);
		combinationCommodity.setPriceWholesale(price);
		combinationCommodity.setShelfLife(BaseAction.INVALID_ID);
		combinationCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		combinationCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		combinationCommodity.setProviderIDs("7");
		combinationCommodity.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "????????????A" + System.currentTimeMillis() % 1000000 + ";");//
		String json = JSONObject.fromObject(combinationCommodity).toString();
		combinationCommodity.setSubCommodityInfo(json);//
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(combinationCommodity, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;

	}

	/** ???????????????????????????dailyReport????????????????????????????????????????????????BaseCommodityTest */
	public Commodity createServiceCommodity(double price, MockMvc mvc, HttpSession session) throws Exception {
		Shared.printTestMethodStartInfo();
		//
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPriceRetail(price);
		c.setPriceWholesale(price);
		c.setPriceVIP(price);
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";3;10;1;8;8;" + "????????????A" + System.currentTimeMillis() % 1000000 + ";");
		//
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	public PurchasingOrder createPurchasingOrder(Commodity[] puCommodityList, Integer[] puCommodityNOList, Double[] puCommodityPriceList, MockMvc mvc, HttpSession session) throws Exception {
		Shared.printTestMethodStartInfo(); // ??????StackTraceElement????????????????????????????????????;
		PurchasingOrder p = new PurchasingOrder();
		String commIDs = "";
		String barcodeIDs = "";
		String commNOs = "";
		String commPrices = "";
		for (int i = 0; i < puCommodityList.length; i++) {
			commIDs = commIDs + puCommodityList[i].getID() + ",";
			barcodeIDs = barcodeIDs + puCommodityList[i].getBarcodeID() + ",";
			commNOs = commNOs + puCommodityNOList[i] + ",";
			commPrices = commPrices + puCommodityPriceList[i] + ",";
		}
		// ???????????????????????????????????????????????????????????????????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "111")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, commNOs) //
						.param(KEY_PRICESUGGESTIONS, commPrices) //
						.param(KEY_COMMPURCHASINGUNIT, "???,???") //
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyCreate(mr, p, purchasingOrderBO, Shared.DBName_Test);

		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());

		PurchasingOrder bmOfDB = new PurchasingOrder();
		return (PurchasingOrder) bmOfDB.parse1(returnJson.getString(BaseAction.KEY_Object));
	}

	@SuppressWarnings("unchecked")
	public PurchasingOrder approvePurchasingOrder(PurchasingOrder purchasingOrder, MockMvc mvc, HttpSession session) throws Exception {
		List<PurchasingOrderCommodity> pocList = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();

		StringBuilder commodityIDs = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		StringBuilder commodityNOs = new StringBuilder();
		StringBuilder priceSuggestion = new StringBuilder();
		for (PurchasingOrderCommodity poc : pocList) {
			commodityIDs.append(poc.getCommodityID() + ",");
			barcodeIDs.append(poc.getBarcodeID() + ",");
			commodityNOs.append(poc.getCommodityNO() + ",");
			priceSuggestion.append(poc.getPriceSuggestion() + ",");
		}

		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_COMMIDS, commodityIDs.toString())//
						.param(KEY_BARCODEIDS, barcodeIDs.toString())//
						.param(KEY_NOS, commodityNOs.toString())//
						.param(KEY_PRICESUGGESTIONS, priceSuggestion.toString())//
						.session((MockHttpSession) session) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);

		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());

		PurchasingOrder bmOfDB = new PurchasingOrder();
		return (PurchasingOrder) bmOfDB.parse1(returnJson.getString(BaseAction.KEY_Object));
	}

	@SuppressWarnings("unchecked")
	public Warehousing createWarehousing(PurchasingOrder purchasingOrder, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), purchasingOrder.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		// ?????????
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(purchasingOrder.getID());
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895???????????????
		whousing.setShopID(Shared.DEFAULT_Shop_ID);

		List<PurchasingOrderCommodity> pocList = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
		String amounts = "";
		String commIDs = "";
		String commNOs = "";
		String commPrices = "";
		String barcodes = "";
		for (PurchasingOrderCommodity poc : pocList) {
			amounts = amounts + (poc.getCommodityNO() * poc.getPriceSuggestion()) + ",";
			commIDs = commIDs + poc.getCommodityID() + ",";
			commNOs = commNOs + poc.getCommodityNO() + ",";
			commPrices = commPrices + poc.getPriceSuggestion() + ",";
			barcodes = barcodes + poc.getBarcodeID() + ",";
		}

		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(purchasingOrder.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(COMMIDS, commIDs) //
						.param(COMMNOS, commNOs) //
						.param(COMMPRICES, commPrices) //
						.param(AMOUNTS, amounts) //
						.param(BARCODEIDS, barcodes) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Integer wsID = JsonPath.read(o1, "$.object.ID");
		//
		ErrorInfo ecOut = new ErrorInfo();
		Warehousing wsR1 = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(wsID, 3, ecOut, Shared.DBName_Test);
		String wsCreateDatetime = JsonPath.read(o1, "$.object.createDatetime");
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		Date createDatetime = sdf1.parse(wsCreateDatetime);
		//
		Assert.assertTrue(createDatetime.getTime() == (wsR1.getCreateDatetime().getTime())); // ... TODO ???????????????????????? ?????? ?????? ??????

		WarehousingCP.verifyCreate(mr2, whousing, Shared.DBName_Test);
		//
		Warehousing bmOfDB = new Warehousing();
		return (Warehousing) bmOfDB.parse1(o1.getString(BaseAction.KEY_Object));
	}

	@SuppressWarnings("unchecked")
	public void approveWarehousing(Warehousing warehousing, MockMvc mvc, HttpSession session) throws Exception {
		StringBuilder commodityIDs = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		StringBuilder commodityNOs = new StringBuilder();
		StringBuilder amouts = new StringBuilder();
		StringBuilder price = new StringBuilder();
		List<Commodity> commList = new ArrayList<Commodity>();
		List<WarehousingCommodity> listwc = (List<WarehousingCommodity>) warehousing.getListSlave1();
		for (WarehousingCommodity wc : listwc) {
			// ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
			Commodity commodity = new Commodity();
			commodity.setID(wc.getCommodityID());
			commList.add(BaseCommodityTest.queryCommodityCache(commodity.getID()));
			//
			commodityIDs.append(wc.getCommodityID() + ",");
			barcodeIDs.append(wc.getBarcodeID() + ",");
			commodityNOs.append(wc.getNO() + ",");
			price.append(wc.getPrice() + ",");
			amouts.append("1000,"); // ??????????????????????????????????????????.
		}
		//
		warehousing.setIsModified(1);
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(warehousing.getIsModified()))//
						.param(COMMIDS, commodityIDs.toString()) //
						.param(COMMNOS, commodityNOs.toString()) //
						.param(COMMPRICES, price.toString()) //
						.param(AMOUNTS, amouts.toString()) //
						.param(BARCODEIDS, barcodeIDs.toString()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, warehousing, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
	}

	/** @param retailTradeIn
	 *            ?????????????????????????????????
	 * @param CommodityNOs
	 * @param saleCommodityList
	 *            ??????????????????????????????
	 * @return */
	public RetailTrade createRetailTrade(Date saleDate, RetailTrade retailTradeIn, Staff staff, MockMvc mvc, HttpSession session, int[] CommodityNOs, Commodity[] saleCommodityList) throws Exception {
		// ???????????????????????????
		double amount = 0.000000d;
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		for (int i = 0; i < saleCommodityList.length; i++) {
			RetailTradeCommodity rtc = new RetailTradeCommodity();
			rtc.setCommodityID(saleCommodityList[i].getID());
			rtc.setNO(CommodityNOs[i]);
			rtc.setPriceOriginal(saleCommodityList[i].getPriceRetail());
			rtc.setPriceReturn(saleCommodityList[i].getPriceRetail());
			rtc.setBarcodeID(saleCommodityList[i].getBarcodeID());
			rtc.setNOCanReturn(CommodityNOs[i]);
			listRetailTradeCommodity.add(rtc);

			amount = GeneralUtil.sum(amount, rtc.getNO() * rtc.getPriceReturn());
		}
		// ???????????????
		RetailTrade rt = DataInput.getRetailTrade(saleDate, staff.getID(), amount, retailTradeIn.getID());
		rt.setStatus(retailTrade_defualtStatus);
		if (rt.getSourceID() > 0) {
			rt.setSn(retailTradeIn.getSn() + "_1");
		}
		rt.setListSlave1(listRetailTradeCommodity);
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		return BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rt, company.getDbName());
	}

	private MvcResult uploadPicture(String path, String picturePath, String pictureType, MockMvc mvc, HttpSession session) throws FileNotFoundException, IOException, Exception {
		File file = new File(path + picturePath);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, pictureType, fis);

		Shared.caseLog(" case1:???????????????????????????");
		MvcResult mr1 = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		Assert.assertNotNull(session.getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName()));
		Assert.assertNotNull(session.getAttribute(EnumSession.SESSION_PictureFILE.getName()), "????????????????????????");
		return mr1;
	}

	protected void setCommodityListBarcode(Commodity[] commodityList) throws Exception {
		for (Commodity commodity : commodityList) {
			Barcodes barcodes = new Barcodes();
			barcodes.setCommodityID(commodity.getID());
			barcodes.setBarcode("");
			//
			Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> bmList = barcodesMapper.retrieveN(params);
			//
			assertTrue(bmList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
			//
			for (BaseModel bm : bmList) {
				Barcodes b = (Barcodes) bm;
				b.setOperatorStaffID(1); // ??????????????????????????????
				//
				String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
				b.setOperatorStaffID(0); // ?????????
			}
			//
			Barcodes barcodesRetrieve = (Barcodes) bmList.get(0);
			commodity.setBarcodeID(barcodesRetrieve.getID());
			commodity.setBarcodes(barcodesRetrieve.getBarcode());
		}

	}

	/** @param totalNO
	 *            ?????????
	 * @param pricePurchase
	 *            ???????????????
	 * @param totalAmount
	 *            ????????????
	 * @param averageAmountOfCustomer
	 *            ?????????
	 * @param totalGross
	 *            ?????????
	 * @param ratioGrossMargin
	 *            ?????????
	 * @param saleCommodityID
	 *            ???????????????ID
	 * @param saleCommodityNO
	 *            ???????????????????????????
	 * @param saleCommodityAmount
	 *            ????????????????????????
	 * @return */
	protected RetailTradeDailyReportSummary getRetailTradeDailyReportSummary(int totalNO, double pricePurchase, double totalAmount, double averageAmountOfCustomer, double totalGross, double ratioGrossMargin, int saleCommodityID,
			int saleCommodityNO, double saleCommodityAmount) {
		RetailTradeDailyReportSummary dailyReportSummary = new RetailTradeDailyReportSummary();
		dailyReportSummary.setTotalNO(totalNO);
		dailyReportSummary.setPricePurchase(pricePurchase);
		dailyReportSummary.setTotalAmount(totalAmount);
		dailyReportSummary.setAverageAmountOfCustomer(averageAmountOfCustomer);
		dailyReportSummary.setTotalGross(totalGross);
		dailyReportSummary.setRatioGrossMargin(ratioGrossMargin);
		dailyReportSummary.setTopSaleCommodityID(saleCommodityID);
		dailyReportSummary.setTopSaleCommodityNO(saleCommodityNO);
		dailyReportSummary.setTopSaleCommodityAmount(saleCommodityAmount);
		dailyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		return dailyReportSummary;
	}

	/** @param barcodes
	 *            ???????????????
	 * @param name
	 *            ????????????
	 * @param specification
	 *            ????????????
	 * @param NO
	 *            ????????????
	 * @param totalPurchasingAmount
	 *            ????????????
	 * @param totalAmount
	 *            ????????????
	 * @param grossMargin
	 *            ?????? */
	protected RetailTradeDailyReportByCommodity getRetailTradeDailyReportByCommodity(int commodityID, String barcodes, String name, String specification, int NO, double totalPurchasingAmount, double totalAmount, double grossMargin) {
		RetailTradeDailyReportByCommodity dailyReportByCommodity = new RetailTradeDailyReportByCommodity();
		dailyReportByCommodity.setCommodityID(commodityID);
		dailyReportByCommodity.setBarcode(barcodes);
		dailyReportByCommodity.setName(name);
		dailyReportByCommodity.setSpecification(specification);
		dailyReportByCommodity.setNO(NO);
		dailyReportByCommodity.setTotalPurchasingAmount(totalPurchasingAmount);
		dailyReportByCommodity.setTotalAmount(totalAmount);
		dailyReportByCommodity.setGrossMargin(grossMargin);
		return dailyReportByCommodity;
	}

	/** @param totalAmount
	 *            ????????????
	 * @param totalGross
	 *            ??????
	 * @return */
	protected RetailTradeMonthlyReportSummary getRetailTradeMonthlyReportSummary(double totalAmount, double totalGross) {
		RetailTradeMonthlyReportSummary monthlyReportSummary = new RetailTradeMonthlyReportSummary();
		monthlyReportSummary.setTotalAmount(totalAmount);
		monthlyReportSummary.setTotalGross(totalGross);
		return monthlyReportSummary;
	}
}
