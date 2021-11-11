package com.bx.erp.test.task;

import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.TaskType.EnumTaskType;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.task.BonusTaskThread;
import com.bx.erp.task.PurchasingTaskThread;
import com.bx.erp.task.RetailTradeDailyReportByCategoryParentTaskThread;
import com.bx.erp.task.RetailTradeDailyReportByStaffTaskThread;
import com.bx.erp.task.RetailTradeDailyReportSummaryTaskThread;
import com.bx.erp.task.RetailTradeMonthlyReportSummaryTaskThread;
import com.bx.erp.task.ShelfLifeTaskThread;
import com.bx.erp.task.TaskManager;
import com.bx.erp.task.TaskThread;
import com.bx.erp.task.UnsalableCommodityTaskThread;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.TaskCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

public class TaskSchedulerTest extends BaseActionTest {

	// 2030-01-01 日报表结果验证
	private final static int TotalNO = 8;
	private final static double PricePurchase = 2833.33d;
	private final static double TotalAmount = 3582.46d;
	private final static double AverageAmountOfCustomer = 447.8075d;
	private final static double TotalGross = 749.13d;
	private final static double RatioGrossMargin = 0.20911d;
	private final static int TopSaleCommodityID = 217;
	private final static int TopSaleCommodityNO = 34;
	private final static double TopSaleCommodityAmount = 2094.06d;

	// 216 = T_Commodity.F_ID
	private final static String Barcode_216 = "6821423302343";
	private final static String Name_216 = "长虹剑";
	private final static String Specification_216 = "kg";
	private final static double TotalPurchasingAmount_216 = 203.13d;
	private final static int NO_216 = 117;
	private final static double TotalAmount_216 = 384.72d;
	private final static double GrossMargin_216 = 181.59d;
	private final static int ID_216 = 216;

	// 217 = T_Commodity.F_ID
	private final static String Barcode_217 = "6821423302344";
	private final static String Name_217 = "冰魄剑";
	private final static String Specification_217 = "kg";
	private final static double TotalPurchasingAmount_217 = 1770.04d;
	private final static int NO_217 = 83;
	private final static double TotalAmount_217 = 2094.06d;
	private final static double GrossMargin_217 = 324.02d;
	private final static int ID_217 = 217;

	// 218 = T_Commodity.F_ID
	private final static String Barcode_218 = "6821423302345";
	private final static String Name_218 = "紫云剑";
	private final static String Specification_218 = "kg";
	private final static double TotalPurchasingAmount_218 = 860.16d;
	private final static int NO_218 = 94;
	private final static double TotalAmount_218 = 1103.68d;
	private final static double GrossMargin_218 = 243.52d;
	private final static int ID_218 = 218;

	private int iRoundOldPurchasingTaskThread;
	private int iRoundOldShelfLifeTaskThread;
	private int iRoundOldUnsalableCommodityTaskThread;
	private int iRoundOldRetailTradeDailyReportSummaryTaskThread;
	private int iRoundOldRetailTradeMonthlyReportSummaryTaskThread;
	private int iRoundOldRetailTradeDailyReportByCategoryParentTaskThread;
	private int iRoundOldRetailTradeDailyReportByStaffTaskThread;
	private int iRoundOldBonusTaskThread;

	private Date targetDate;
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
	private SimpleDateFormat sdf2 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);

	private PurchasingTaskThread pttRoundOld;
	private ShelfLifeTaskThread sttRoundOld;
	private UnsalableCommodityTaskThread ucRoundOld;
	private RetailTradeDailyReportSummaryTaskThread drRoundOld;
	private RetailTradeMonthlyReportSummaryTaskThread mrRoundOld;
	private RetailTradeDailyReportByCategoryParentTaskThread cpRoundOld;
	private RetailTradeDailyReportByStaffTaskThread rbsRoundOld;
	private BonusTaskThread bttRoundOld;

	@BeforeClass
	public void setup() {
		super.setUp();

		pttRoundOld = (com.bx.erp.task.PurchasingTaskThread) TaskManager.getCache(EnumTaskType.ETT_PurchasingTimeout);
		System.out.println("pttRoundOld hashcode=" + pttRoundOld.hashCode());
		iRoundOldPurchasingTaskThread = pttRoundOld.getTaskStatusForTest().get();

		sttRoundOld = (com.bx.erp.task.ShelfLifeTaskThread) TaskManager.getCache(EnumTaskType.ETT_ShelfLifeTaskThread);
		System.out.println("sttRoundOld hashcode=" + sttRoundOld.hashCode());
		iRoundOldShelfLifeTaskThread = sttRoundOld.getTaskStatusForTest().get();

		ucRoundOld = (com.bx.erp.task.UnsalableCommodityTaskThread) TaskManager.getCache(EnumTaskType.ETT_UnsalableCommodity);
		System.out.println("ucRoundOld hashcode=" + ucRoundOld.hashCode());
		iRoundOldUnsalableCommodityTaskThread = ucRoundOld.getTaskStatusForTest().get();

		drRoundOld = (RetailTradeDailyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
		System.out.println("drRoundOld hashcode=" + drRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportSummaryTaskThread = drRoundOld.getTaskStatusForTest().get();

		bttRoundOld = (BonusTaskThread) TaskManager.getCache(EnumTaskType.ETT_BonusTaskThread);
		System.out.println("bttRoundOld hashcode=" + bttRoundOld.hashCode());
		iRoundOldBonusTaskThread = bttRoundOld.getTaskStatusForTest().get();

		try {
			targetDate = sdf2.parse(REPORT_DATE_END);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mrRoundOld = (RetailTradeMonthlyReportSummaryTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
		if (targetDate != null) {
			mrRoundOld.setTargetDate(targetDate);
		}
		System.out.println("mrRoundOld hashcode=" + mrRoundOld.hashCode());
		iRoundOldRetailTradeMonthlyReportSummaryTaskThread = mrRoundOld.getTaskStatusForTest().get();

		cpRoundOld = (RetailTradeDailyReportByCategoryParentTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByCategoryParentTaskThread);
		System.out.println("cpRoundOld hashcode=" + cpRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportByCategoryParentTaskThread = cpRoundOld.getTaskStatusForTest().get();

		rbsRoundOld = (RetailTradeDailyReportByStaffTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByStaffTaskThread);
		System.out.println("rbsRoundOld hashcode=" + rbsRoundOld.hashCode());
		iRoundOldRetailTradeDailyReportByStaffTaskThread = rbsRoundOld.getTaskStatusForTest().get();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@SuppressWarnings("static-access")
	@Test(timeOut = 180000)
	public void PurchasingTaskThreadTest() {
		Shared.printTestMethodStartInfo();

		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【商品采购超时检查】");

		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.PurchasingTimeoutTaskScanStartTime);
		cgStart.setName("PurchasingTimeoutTaskStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		pttRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		int iRoundNow = iRoundOldPurchasingTaskThread;
		PurchasingTaskThread pttRoundNow = null;
		while (iRoundNow - iRoundOldPurchasingTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			PurchasingTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			PurchasingTaskThread.runOnce = true;
			pttRoundNow = (com.bx.erp.task.PurchasingTaskThread) TaskManager.getCache(EnumTaskType.ETT_PurchasingTimeout);
			System.out.println("RoundNow hashcode=" + pttRoundNow.hashCode());
			iRoundNow = pttRoundNow.getTaskStatusForTest().get();
			System.out.println("等待采购超时检查线程执行一次。iRoundNow=" + iRoundNow + "\tiRoundOldPurchasingTaskThread=" + iRoundOldPurchasingTaskThread);
		}
		System.out.println("采购超时检查线程" + pttRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNow=" + iRoundNow + "\tiRoundOldPurchasingTaskThread=" + iRoundOldPurchasingTaskThread);

		System.out.println("【商品采购超时检查】已经完毕，正在恢复定时任务的最初配置······");
		//
		PurchasingTaskThread.reportDateForTest = null;
		PurchasingTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

	@SuppressWarnings("static-access")
	@Test(timeOut = 180000)
	public void ShelfLifeTaskThreadTest() {
		Shared.printTestMethodStartInfo();

		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【商品保质期检查】");

		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.ShelfLifeTaskScanStartTime);
		cgStart.setName("ShelfLifeTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		sttRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		ShelfLifeTaskThread sttRoundNow = null;
		int iRoundNow = iRoundOldShelfLifeTaskThread;
		while (iRoundNow - iRoundOldShelfLifeTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			ShelfLifeTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			ShelfLifeTaskThread.runOnce = true;
			sttRoundNow = (com.bx.erp.task.ShelfLifeTaskThread) TaskManager.getCache(EnumTaskType.ETT_ShelfLifeTaskThread);
			System.out.println("RoundNow hashcode=" + sttRoundNow.hashCode());
			iRoundNow = sttRoundNow.getTaskStatusForTest().get();
			System.out.println("等待商品保质期检查线程执行一次。iRoundNow=" + iRoundNow + "\tiRoundOldShelfLifeTaskThread=" + iRoundOldShelfLifeTaskThread);
		}
		System.out.println("商品保质期检查线程" + sttRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNow=" + iRoundNow + "\tiRoundOldShelfLifeTaskThread=" + iRoundOldShelfLifeTaskThread);

		System.out.println("【商品保质期检查】已经完毕，正在恢复定时任务的最初配置······");
		//
		ShelfLifeTaskThread.reportDateForTest = null;
		ShelfLifeTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

	@SuppressWarnings("static-access")
	@Test(timeOut = 180000)
	public void UsalableCommodityTaskThreadTest() {
		Shared.printTestMethodStartInfo();

		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【商品滞销检查】");
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.UsalableCommodityTaskScanStartTime);
		cgStart.setName("UsalableCommodityTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		ucRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		UnsalableCommodityTaskThread ucRoundNow = null;
		int iRoundNow = iRoundOldUnsalableCommodityTaskThread;
		while (iRoundNow - iRoundOldUnsalableCommodityTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			UnsalableCommodityTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			UnsalableCommodityTaskThread.runOnce = true;
			ucRoundNow = (com.bx.erp.task.UnsalableCommodityTaskThread) TaskManager.getCache(EnumTaskType.ETT_UnsalableCommodity);
			System.out.println("RoundNow hashcode=" + ucRoundNow.hashCode());
			iRoundNow = ucRoundNow.getTaskStatusForTest().get();
			System.out.println("等待商品滞销检查线程执行一次。iRoundNow=" + iRoundNow + "\tiRoundOldUnsalableCommodityTaskThread=" + iRoundOldUnsalableCommodityTaskThread);
		}
		System.out.println("采购超时检查线程" + ucRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNow=" + iRoundNow + "\tiRoundOldUnsalableCommodityTaskThread=" + iRoundOldUnsalableCommodityTaskThread);

		System.out.println("【商品滞销检查】已经完毕，正在恢复定时任务的最初配置······");
		//
		UnsalableCommodityTaskThread.reportDateForTest = null;
		UnsalableCommodityTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Test(timeOut = 360000)
	public void RetailTradeDailyReportSummaryTaskThreadTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 设置生成哪一天的报表
		String date = REPORT_DATE;
		try {
			RetailTradeDailyReportSummaryTaskThread.reportDateForTest = sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		//
		TaskThread.deleteReportOfDateRun = 1;
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
				Thread.currentThread().sleep(5000);
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
		TaskThread.deleteReportOfDateRun = 0;
		RetailTradeDailyReportSummaryTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		// 从数据库中获取刚生成的日报表，用检查点进行检查
		SimpleDateFormat sdfOfDateTime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);
		RetailTradeDailyReportSummary retailTradeDailyReportSummary = new RetailTradeDailyReportSummary();
		try {
			retailTradeDailyReportSummary.setDatetimeStart(sdfOfDateTime.parse("2030/01/01 00:00:00"));
			retailTradeDailyReportSummary.setDatetimeEnd(sdfOfDateTime.parse("2030/01/01 23:59:59"));
			retailTradeDailyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportSummary> bmList = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);
		if (retailTradeDailyReportSummaryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询日报表出错，错误信息：" + retailTradeDailyReportSummaryBO.printErrorInfo());
		}
		assertTrue(bmList != null, "查询报表有误");
		assertTrue(bmList.size() == 1, "查询报表有误" + bmList.size());
		//
		RetailTradeDailyReportSummary dailyReportSummary = new RetailTradeDailyReportSummary();
		dailyReportSummary.setTotalNO(TotalNO);
		dailyReportSummary.setPricePurchase(PricePurchase);
		dailyReportSummary.setTotalAmount(TotalAmount);
		dailyReportSummary.setAverageAmountOfCustomer(AverageAmountOfCustomer);
		dailyReportSummary.setTotalGross(TotalGross);
		dailyReportSummary.setRatioGrossMargin(RatioGrossMargin);
		dailyReportSummary.setTopSaleCommodityID(TopSaleCommodityID);
		dailyReportSummary.setTopSaleCommodityNO(TopSaleCommodityNO);
		dailyReportSummary.setTopSaleCommodityAmount(TopSaleCommodityAmount);
		dailyReportSummary.setShopID(Shared.DEFAULT_Shop_ID);

		TaskCP.verifyRetailTradeDailyReportSummaryTask(bmList.get(0), dailyReportSummary);

		// 从数据库中获取刚生成的商品报表，用检查点进行检查
		RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity = new RetailTradeDailyReportByCommodity();
		try {
			retailTradeDailyReportByCommodity.setDatetimeStart(sdfOfDateTime.parse("2030/01/01 00:00:00"));
			retailTradeDailyReportByCommodity.setDatetimeEnd(sdfOfDateTime.parse("2030/01/01 23:59:59"));
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		retailTradeDailyReportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportByCommodity> retailTradeDailyReportByCommodityList = (List<RetailTradeDailyReportByCommodity>) retailTradeDailyReportByCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
				retailTradeDailyReportByCommodity);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询商品报表出错，错误信息：" + retailTradeDailyReportByCommodityBO.printErrorInfo());
		}

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();

		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = new RetailTradeDailyReportByCommodity();
		dailyReportByCommodity1.setBarcode(Barcode_216);
		dailyReportByCommodity1.setName(Name_216);
		dailyReportByCommodity1.setSpecification(Specification_216);
		dailyReportByCommodity1.setNO(NO_216);
		dailyReportByCommodity1.setTotalPurchasingAmount(TotalPurchasingAmount_216);
		dailyReportByCommodity1.setTotalAmount(TotalAmount_216);
		dailyReportByCommodity1.setGrossMargin(GrossMargin_216);
		dailyReportByCommodity1.setCommodityID(ID_216);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = new RetailTradeDailyReportByCommodity();
		dailyReportByCommodity2.setBarcode(Barcode_217);
		dailyReportByCommodity2.setName(Name_217);
		dailyReportByCommodity2.setSpecification(Specification_217);
		dailyReportByCommodity2.setNO(NO_217);
		dailyReportByCommodity2.setTotalPurchasingAmount(TotalPurchasingAmount_217);
		dailyReportByCommodity2.setTotalAmount(TotalAmount_217);
		dailyReportByCommodity2.setGrossMargin(GrossMargin_217);
		dailyReportByCommodity2.setCommodityID(ID_217);
		dailyReportByCommodityList.add(dailyReportByCommodity2);

		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = new RetailTradeDailyReportByCommodity();
		dailyReportByCommodity3.setBarcode(Barcode_218);
		dailyReportByCommodity3.setName(Name_218);
		dailyReportByCommodity3.setSpecification(Specification_218);
		dailyReportByCommodity3.setNO(NO_218);
		dailyReportByCommodity3.setTotalPurchasingAmount(TotalPurchasingAmount_218);
		dailyReportByCommodity3.setTotalAmount(TotalAmount_218);
		dailyReportByCommodity3.setGrossMargin(GrossMargin_218);
		dailyReportByCommodity3.setCommodityID(ID_218);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		TaskCP.verifyRetailTradeDailyReportByCommodityTask(retailTradeDailyReportByCommodityList, dailyReportByCommodityList);
	}

	@SuppressWarnings("unchecked")
	@Test(dependsOnMethods = "RetailTradeDailyReportSummaryTaskThreadTest", timeOut = 180000)
	public void RetailTradeMonthlyReportSummaryTaskThreadTest_CaseNormal() throws Exception {
		// 生成月报表
		RetailTradeMonthlyReportSummaryTaskThreadTest(targetDate);
		//
		RetailTradeMonthlyReportSummary retailTradeMonthlyReportSummary = new RetailTradeMonthlyReportSummary();
		retailTradeMonthlyReportSummary.setDatetimeStart(sdf.parse("2030-01-01 00:00:00"));
		retailTradeMonthlyReportSummary.setDatetimeEnd(sdf.parse("2030-01-31 00:00:00"));

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeMonthlyReportSummary> rtmReport = (List<RetailTradeMonthlyReportSummary>) retailTradeMonthlyReportSummaryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeMonthlyReportSummary);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询月报表错误，错误信息：" + retailTradeDailyReportByCommodityBO.printErrorInfo());
		}
		assertTrue(rtmReport != null && rtmReport.size() > 0, "查询的月报表有误！");
		//
		TaskCP.verifyRetailTradeMonthlyReportSummaryTask(rtmReport.get(0));
	}

	@Test(timeOut = 180000)
	public void RetailTradeMonthlyReportSummaryTaskThreadTest_CaseSpecialDay() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		Date targetDate = sdf.parse("1970-04-06 00:00:00");
		RetailTradeMonthlyReportSummaryTaskThreadTest(targetDate);
	}

	@SuppressWarnings("static-access")
	public void RetailTradeMonthlyReportSummaryTaskThreadTest(Date targetDate) throws Exception {
		Shared.printTestMethodStartInfo();

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
				Thread.currentThread().sleep(5000);
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
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Test(timeOut = 180000)
	public void RetailTradeDailyReportByCategoryParentTaskThreadTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 设置生成哪一天的报表
		String date = REPORT_DATE;
		try {
			RetailTradeDailyReportByCategoryParentTaskThread.reportDateForTest = sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【销售日大类金额报表推送】");

		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeDailyReportByCategoryParentTaskScanStartTime);
		cgStart.setName("RetailTradeDailyReportByCategoryParentTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		cpRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeDailyReportByCategoryParentTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeDailyReportByCategoryParentTaskThread;
		while (iRoundNew - iRoundOldRetailTradeDailyReportByCategoryParentTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportByCategoryParentTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			RetailTradeDailyReportByCategoryParentTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportByCategoryParentTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByCategoryParentTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送销售日大类金额报报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportByCategoryParentTaskThread=" + iRoundOldRetailTradeDailyReportByCategoryParentTaskThread);
		}
		System.out.println("夜间推送销售日大类金额报报表线程" + drRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportByCategoryParentTaskThread=" + iRoundOldRetailTradeDailyReportByCategoryParentTaskThread);

		System.out.println("【夜间推送销售日大类金额报报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeDailyReportByCategoryParentTaskThread.reportDateForTest = null;
		RetailTradeDailyReportByCategoryParentTaskThread.runOnce = false;
		// 从数据库中获取刚生成的员工业绩表，用检查点进行检查
		SimpleDateFormat sdfOfDateTime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		RetailTradeDailyReportByCategoryParent retailTradeDailyReportByCategoryParent = new RetailTradeDailyReportByCategoryParent();
		try {
			retailTradeDailyReportByCategoryParent.setDatetimeStart(sdfOfDateTime.parse("2030/01/01 00:00:00"));
			retailTradeDailyReportByCategoryParent.setDatetimeEnd(sdfOfDateTime.parse("2030/01/01 23:59:59"));
			retailTradeDailyReportByCategoryParent.setShopID(Shared.DEFAULT_Shop_ID);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售大类报表】日期格式转换异常" + e.toString());
			return;
		}
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportByCategoryParent> retailTradeDailyReportByStaffList = (List<RetailTradeDailyReportByCategoryParent>) retailTradeDailyReportByCategoryParentBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
				retailTradeDailyReportByCategoryParent);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询日销售大类表出错，错误信息：" + retailTradeDailyReportByStaffBO.printErrorInfo());
		}
		TaskCP.verifyRetailTradeDailyReportByCategoryParentTask(retailTradeDailyReportByStaffList);
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));

	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Test(timeOut = 180000)
	public void RetailTradeDailyReportByStaffTaskThreadTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 设置生成哪一天的报表
		String date = REPORT_DATE;
		try {
			RetailTradeDailyReportByStaffTaskThread.reportDateForTest = sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送员工业绩报表】日期格式转换异常" + e.toString());
			return;
		}
		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【员工业绩报表推送】");
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.RetailTradeDailyReportByStaffTaskScanStartTime);
		cgStart.setName("RetailTradeDailyReportByStaffTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		rbsRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		RetailTradeDailyReportByStaffTaskThread drRoundNow = null;
		int iRoundNew = iRoundOldRetailTradeDailyReportByStaffTaskThread;
		while (iRoundNew - iRoundOldRetailTradeDailyReportByStaffTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RetailTradeDailyReportByStaffTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			RetailTradeDailyReportByStaffTaskThread.runOnce = true;
			drRoundNow = (RetailTradeDailyReportByStaffTaskThread) TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByStaffTaskThread);
			System.out.println("RoundNow hashcode=" + drRoundNow.hashCode());
			iRoundNew = drRoundNow.getTaskStatusForTest().get();
			System.out.println("夜间推送员工业绩报表线程执行一次。iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportByStaffTaskThread=" + iRoundOldRetailTradeDailyReportByStaffTaskThread);
		}
		System.out.println("夜间推送员工业绩报表线程" + drRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNew=" + iRoundNew + "\tiRoundOldRetailTradeDailyReportByStaffTaskThread=" + iRoundOldRetailTradeDailyReportByStaffTaskThread);

		System.out.println("【夜间推送员工业绩报表】已经完毕，正在恢复定时任务的最初配置······");
		//
		RetailTradeDailyReportByStaffTaskThread.reportDateForTest = null;
		RetailTradeDailyReportByStaffTaskThread.runOnce = false;

		// 从数据库中获取刚生成的员工业绩表，用检查点进行检查
		SimpleDateFormat sdfOfDateTime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		try {
			retailTradeDailyReportByStaff.setDatetimeStart(sdfOfDateTime.parse("2030/01/01 00:00:00"));
			retailTradeDailyReportByStaff.setDatetimeEnd(sdfOfDateTime.parse("2030/01/01 23:59:59"));
			retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		} catch (ParseException e) {
			System.out.println("夜间检查任务【推送日销售报表】日期格式转换异常" + e.toString());
			return;
		}
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportByStaff> retailTradeDailyReportByStaffList = (List<RetailTradeDailyReportByStaff>) retailTradeDailyReportByStaffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询员工业绩表出错，错误信息：" + retailTradeDailyReportByStaffBO.printErrorInfo());
		}
		TaskCP.verifyRetailTradeDailyReportByStaffTask(retailTradeDailyReportByStaffList);
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

	@SuppressWarnings("static-access")
	@Test(timeOut = 180000)
	public void BonusTaskThreadTest() {
		Shared.printTestMethodStartInfo();

		System.out.println("在缓存中修改定时任务执行时间段的配置，令定时任务现在开始执行【商品滞销检查】");
		ConfigGeneral cgStart = new ConfigGeneral();
		cgStart.setID(BaseCache.BonusTaskScanStartTime);
		cgStart.setName("BonusTaskScanStartTime");
		cgStart.setValue(DatetimeUtil.getTimeInString(new Date(), 0));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).write1(cgStart, Shared.DBName_Test, BaseBO.SYSTEM);
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
		//
		bttRoundOld.setTaskStatusForTest(new AtomicInteger(0));
		BonusTaskThread bttRoundNow = null;
		int iRoundNow = iRoundOldBonusTaskThread;
		while (iRoundNow - iRoundOldBonusTaskThread < 1) {// 必须等待上面的代码将配置的时间改变，才能令线程内读到的时间等于上面设置的时间而非服务器启动时加载的时间
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BonusTaskThread.reportDateForTest = DatetimeUtil.getDays(new Date(), 2);// 默认执行时间是明天的凌晨00:00，所以跑report的date应该是后天
			BonusTaskThread.runOnce = true;
			bttRoundNow = (BonusTaskThread) TaskManager.getCache(EnumTaskType.ETT_BonusTaskThread);
			System.out.println("RoundNow hashcode=" + bttRoundNow.hashCode());
			iRoundNow = bttRoundNow.getTaskStatusForTest().get();
			System.out.println("等待积分清零线程执行一次。iRoundNow=" + iRoundNow + "\tiRoundOldBonusTaskThread=" + iRoundOldBonusTaskThread);
		}
		System.out.println("积分清零检查线程" + bttRoundNow.getName() + "已经执行一次！！！！！！！！iRoundNow=" + iRoundNow + "\tiRoundOldBonusTaskThread=" + iRoundOldBonusTaskThread);

		System.out.println("【积分清零检查】已经完毕，正在恢复定时任务的最初配置······");
		//
		BonusTaskThread.reportDateForTest = null;
		BonusTaskThread.runOnce = false;
		//
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigGeneral).readN(false, false));
	}

}
