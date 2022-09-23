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
	// 采购参数
	private static final String KEY_COMMIDS = "commIDs";
	private static final String KEY_BARCODEIDS = "barcodeIDs";
	private static final String KEY_NOS = "NOs";
	private static final String KEY_PRICESUGGESTIONS = "priceSuggestions";
	private static final String KEY_COMMPURCHASINGUNIT = "commPurchasingUnit";
	private static final String KEY_PROVIDERID = "providerID";
	private static final String KEY_SHOPID = "shopID";
	// 入库参数
	private static final String COMMIDS = "commIDs";
	private static final String COMMNOS = "commNOs";
	private static final String COMMPRICES = "commPrices";
	private static final String AMOUNTS = "amounts";
	private static final String BARCODEIDS = "barcodeIDs";

	private RetailTradeDailyReportSummaryTaskThread drRoundOld;
	private RetailTradeMonthlyReportSummaryTaskThread mrRoundOld;
	private int iRoundOldRetailTradeDailyReportSummaryTaskThread;

	// 正常零售单创建后状态为1
	protected static int retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();
	private int iRoundOldRetailTradeMonthlyReportSummaryTaskThread;

	@SuppressWarnings({ "static-access", "unchecked" })
	public void runRetailTradeMonthlyReportSummaryTaskThread(Date targetDate, RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary) throws Exception {
		mrRoundOld = (RetailTradeMonthlyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
		iRoundOldRetailTradeMonthlyReportSummaryTaskThread = mrRoundOld.getTaskStatusForTest().get();

		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【销售月报表推送】");
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
		while (iRoundNew - iRoundOldRetailTradeMonthlyReportSummaryTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(timeSpanToCheckTaskStartTime + 100); // 加多点时间以免时辰未到
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeMonthlyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);
			RetailTradeMonthlyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeMonthlyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送销售月报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeMonthlyReportSummaryTaskThread=" + iRoundOldRetailTradeMonthlyReportSummaryTaskThread);
		}
		System.out.println("夜间推送销售月报表线程" + drRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeMonthlyReportSummaryTaskThread=" + iRoundOldRetailTradeMonthlyReportSummaryTaskThread);

		System.out.println("【夜间推送销售月报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeMonthlyReportSummaryTaskThread.reportDateForTest = null; //
		RetailTradeMonthlyReportSummaryTaskThread.runOnce = false;
		// 从数据库中获取刚生成的月报，用检查点进行检查
		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(DatetimeUtil.getStartTime((DatetimeUtil.getFirstDayDateOfMonth(targetDate))));
		retailTradeMonthlyReportSummary.setDatetimeEnd(DatetimeUtil.getEndTime((DatetimeUtil.getFirstDayDateOfMonth(targetDate))));
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeMonthlyReportSummary> rtmReport = (List<RetailTradeMonthlyReportSummary>) retailTradeMonthlyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询月报表错误，错误信息：" + retailTradeMonthlyReportSummaryBO.printErrorInfo());
		}
		assertTrue(rtmReport != null && rtmReport.size() > 0, "查询的月报表有误！");
		//
		TaskCP.verifyRetailTradeMonthlyReportSummaryTask(rtmReport.get(0), expectRetailTradeMonthlyReportSummary);
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void runRetailTradeDailyReportSummaryTaskThread(Date saleDate, Date reprotDate, RetailTradeDailyReportSummary expectDailyReportSummary, List<RetailTradeDailyReportByCommodity> expectDailyReportByCommodityList) throws Exception {

		drRoundOld = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
		System.out.println("drRoundOld hashcode=" + drRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportSummaryTaskThread = drRoundOld.getTaskStatusForTest().get();
		// 设置生成哪一天的报表
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = reprotDate;
		TaskThread.deleteReportOfDateRun = EnumBoolean.EB_Yes.getIndex();
		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【销售日报表推送】");
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
		while (iRoundNew - iRoundOldRetailTradeDailyReportSummaryTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(timeSpanToCheckTaskStartTime + 100); // 加多点时间以免时辰未到
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			RetailTradeDailyReportSummaryTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送销售日报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);
		}
		System.out.println("夜间推送销售日报表线程" + drRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportSummaryTaskThread=" + iRoundOldRetailTradeDailyReportSummaryTaskThread);

		System.out.println("【夜间推送销售日报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeDailyReportSummaryTaskThread.reportDateForTest = null;
		TaskThread.deleteReportOfDateRun = EnumBoolean.EB_NO.getIndex();
		RetailTradeDailyReportSummaryTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		// 从数据库中获取刚生成的日报表，用检查点进行检查
		RetailTradeDailyReportSummary retailTradeDailyReportSummary = new RetailTradeDailyReportSummary();
		retailTradeDailyReportSummary.setDatetimeStart(DatetimeUtil.getStartTime(saleDate));
		retailTradeDailyReportSummary.setDatetimeEnd(DatetimeUtil.getEndTime(saleDate));
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportSummary> bmList = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);
		if (retailTradeDailyReportSummaryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询日报表出错，错误信息：" + retailTradeDailyReportSummaryBO.printErrorInfo());
		}
		if (expectDailyReportSummary.getRatioGrossMargin() == 0d) {
			assertTrue(bmList != null, "查询报表有误，毛利率为0不会生成报表");
		} else {
			if (bmList != null && bmList.size() == 1) {
				System.out.println("销售日报表查询结果=" + bmList);
			} else {
				retailTradeDailyReportSummaryBO.printErrorInfo(Shared.DBName_Test, retailTradeDailyReportSummary);
				assertTrue(false, "报表未成功生成！难道是DB未就绪？查询一下DB求证一下看看。");
			}
			//
			TaskCP.verifyRetailTradeDailyReportSummaryTask(bmList.get(0), expectDailyReportSummary);

			// 从数据库中获取刚生成的商品报表，用检查点进行检查
			RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity = new RetailTradeDailyReportByCommodity();
			retailTradeDailyReportByCommodity.setDatetimeStart(DatetimeUtil.getStartTime(saleDate));
			retailTradeDailyReportByCommodity.setDatetimeEnd(DatetimeUtil.getEndTime(saleDate));
			retailTradeDailyReportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<RetailTradeDailyReportByCommodity> retailTradeDailyReportByCommodityList = (List<RetailTradeDailyReportByCommodity>) retailTradeDailyReportByCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
					retailTradeDailyReportByCommodity);
			if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查询商品报表出错，错误信息：" + retailTradeDailyReportByCommodityBO.printErrorInfo());
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
			retailTrade.setReturnObject(1);// 测试不通过
			retailTrade.setWxOrderSN("");
			retailTrade.setAliPayOrderSN("");
			retailTrade.setWxRefundNO("0");
			retailTrade.setDatetimeStart(new Date());
			retailTrade.setDatetimeEnd(new Date());
			Thread.sleep(1);

			return retailTrade;
		}

	}

	/** 创建单品，供dailyReport包下的测试重复使用，不需要移动到BaseCommodityTest */
	public Commodity createNormalCommodity(double price, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPriceRetail(price);
		c.setPriceVIP(price);
		c.setPriceWholesale(price);
		c.setName("普通商品A" + System.currentTimeMillis() % 1000000);
		c.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "普通商品A" + System.currentTimeMillis() % 1000000 + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	/** 令方法支持创建一个或者多个多包装商品 ，供dailyReport包下的测试重复使用，不需要移动到BaseCommodityTest */
	public Commodity createCommodityIncludeMultiPackages(MockMvc mvc, HttpSession session, String providerIDs, String multiPackagingInfo, Commodity c) throws Exception {
		MvcResult req = uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, session);
		c.setName("普通商品" + System.currentTimeMillis() % 1000000);
		c.setProviderIDs(providerIDs);
		c.setMultiPackagingInfo(multiPackagingInfo);
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	/** 创建组合商品和子商品，供dailyReport包下的测试重复使用，不需要移动到BaseCommodityTest */
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
		combinationCommodity.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";1;1;1;8;8;" + "组合商品A" + System.currentTimeMillis() % 1000000 + ";");//
		String json = JSONObject.fromObject(combinationCommodity).toString();
		combinationCommodity.setSubCommodityInfo(json);//
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(combinationCommodity, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;

	}

	/** 创建服务型商品，供dailyReport包下的测试重复使用，不需要移动到BaseCommodityTest */
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
		c.setMultiPackagingInfo("111" + System.currentTimeMillis() % 1000000 + ";3;10;1;8;8;" + "服务商品A" + System.currentTimeMillis() % 1000000 + ";");
		//
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		return commodity;
	}

	public PurchasingOrder createPurchasingOrder(Commodity[] puCommodityList, Integer[] puCommodityNOList, Double[] puCommodityPriceList, MockMvc mvc, HttpSession session) throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;
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
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "111")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, commNOs) //
						.param(KEY_PRICESUGGESTIONS, commPrices) //
						.param(KEY_COMMPURCHASINGUNIT, "桶,桶") //
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
		// 检查点
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(purchasingOrder.getID());
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895手机号登录
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
		Assert.assertTrue(createDatetime.getTime() == (wsR1.getCreateDatetime().getTime())); // ... TODO 需要增加从表验证 创建 修改 审核

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
			// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
			Commodity commodity = new Commodity();
			commodity.setID(wc.getCommodityID());
			commList.add(BaseCommodityTest.queryCommodityCache(commodity.getID()));
			//
			commodityIDs.append(wc.getCommodityID() + ",");
			barcodeIDs.append(wc.getBarcodeID() + ",");
			commodityNOs.append(wc.getNO() + ",");
			price.append(wc.getPrice() + ",");
			amouts.append("1000,"); // 暂时先写死，不影响报表的测试.
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
		WarehousingCP.verifyApprove(mr, warehousing, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
	}

	/** @param retailTradeIn
	 *            可能是退货可能是非退货
	 * @param CommodityNOs
	 * @param saleCommodityList
	 *            退货或售出的商品列表
	 * @return */
	public RetailTrade createRetailTrade(Date saleDate, RetailTrade retailTradeIn, Staff staff, MockMvc mvc, HttpSession session, int[] CommodityNOs, Commodity[] saleCommodityList) throws Exception {
		// 计算售卖商品的总价
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
		// 生成零售单
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

		Shared.caseLog(" case1:商品创建时上传图片");
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
		Assert.assertNotNull(session.getAttribute(EnumSession.SESSION_PictureFILE.getName()), "图片文件不存在！");
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
				b.setOperatorStaffID(1); // 为了能通过下面的断言
				//
				String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
				b.setOperatorStaffID(0); // 恢复值
			}
			//
			Barcodes barcodesRetrieve = (Barcodes) bmList.get(0);
			commodity.setBarcodeID(barcodesRetrieve.getID());
			commodity.setBarcodes(barcodesRetrieve.getBarcode());
		}

	}

	/** @param totalNO
	 *            总单数
	 * @param pricePurchase
	 *            总进货金额
	 * @param totalAmount
	 *            总销售额
	 * @param averageAmountOfCustomer
	 *            客单价
	 * @param totalGross
	 *            总毛利
	 * @param ratioGrossMargin
	 *            毛利率
	 * @param saleCommodityID
	 *            最畅销商品ID
	 * @param saleCommodityNO
	 *            最畅销商品销售数量
	 * @param saleCommodityAmount
	 *            最畅销商品销售额
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
	 *            商品条形码
	 * @param name
	 *            商品名称
	 * @param specification
	 *            商品规格
	 * @param NO
	 *            商品数量
	 * @param totalPurchasingAmount
	 *            总进货价
	 * @param totalAmount
	 *            总销售额
	 * @param grossMargin
	 *            毛利 */
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
	 *            总销售额
	 * @param totalGross
	 *            毛利
	 * @return */
	protected RetailTradeMonthlyReportSummary getRetailTradeMonthlyReportSummary(double totalAmount, double totalGross) {
		RetailTradeMonthlyReportSummary monthlyReportSummary = new RetailTradeMonthlyReportSummary();
		monthlyReportSummary.setTotalAmount(totalAmount);
		monthlyReportSummary.setTotalGross(totalGross);
		return monthlyReportSummary;
	}
}
