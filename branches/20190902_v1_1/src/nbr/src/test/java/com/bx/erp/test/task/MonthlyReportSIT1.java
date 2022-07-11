package com.bx.erp.test.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.task.dailyReport.BaseDailyReportSIT;
import com.bx.erp.util.DatetimeUtil;

@WebAppConfiguration
public class MonthlyReportSIT1 extends BaseDailyReportSIT {
	private Staff staff;

	public static String SALE_DATE = "2088-01-01";
	public static String RETURN_DATE = "2088-01-01";
	public static String REPORT_DATE = "2088-01-02";
	public static String TARGET_DATE = "2088-01-28";

	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);

	private Commodity normalCommodityA;
	private Commodity normalCommodityB;
	private Commodity normalCommodityC;
	private Commodity normalCommodityD;
	private Commodity multiPackageCommodityB;
	private Commodity subCommodity;
	private Commodity serviceCommodity;

	// 跨月份退货时,使用该对象保存下个月要退货的源零售单
	private RetailTrade sourceRetailTrade;

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			Shared.resetPOS(mvc, 1);
			sessionBoss = Shared.getPosLoginSession(mvc, 1);
			//
			staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
			// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
			ConfigCacheSize ccs = new ConfigCacheSize();
			ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
			ccs.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
			ccs.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
			CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
			//
			ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
			ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
			ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
			CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);

			// 创建普通商品ABCD
			normalCommodityA = createNormalCommodity(1.680000d, mvc, sessionBoss);
			// 创建带多包装商品的普通商品B
			Integer[] mulitpleList = { 2 };
			Double[] mulitplePriceList = { 69.760000d };
			double price = 38.880000d;
			Commodity commB = BaseCommodityTest.DataInput.getCommodity();
			commB.setPriceRetail(price);
			commB.setPriceVIP(price);
			commB.setPriceWholesale(price);
			String providerIDs = "1,2";
			String multiPackagingInfo = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ";1,2;1," + mulitpleList[0] + ";" + "4.88," + mulitplePriceList[0] + ";4.88,13.76;8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodityB = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs, multiPackagingInfo, commB);
			List<BaseModel> commodityList = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodityB);
			Assert.assertTrue(commodityList.size() == 1, "根据普通商品B查出的多包装商品数量不等于2，不足以接下来的测试！");
			multiPackageCommodityB = (Commodity) commodityList.get(0);
			//
			normalCommodityC = createNormalCommodity(19.990000d, mvc, sessionBoss);
			//
			normalCommodityD = createNormalCommodity(10.880000d, mvc, sessionBoss);
			// 创建普通商品CD的组合商品
			Commodity[] subCommodityList = { normalCommodityC, normalCommodityD };
			Integer[] subCommodityNOList = { 1, 2 };
			Double[] subCommodityPriceList = { normalCommodityC.getPriceRetail(), normalCommodityD.getPriceRetail() };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 35.880000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity = createServiceCommodity(1.110000d, mvc, sessionBoss);
			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, normalCommodityC, normalCommodityD, multiPackageCommodityB, subCommodity, serviceCommodity });
			// 采购
			Commodity[] puCommodityList = { normalCommodityA, normalCommodityB, normalCommodityC, normalCommodityD };
			Integer[] puCommodityNOList = { 1000, 1000, 1000, 1000 };
			Double[] puCommodityPriceList = { 0.88000d, 18.990000d, 12.880000d, 5.990000d };
			PurchasingOrder purchasingOrder = createPurchasingOrder(puCommodityList, puCommodityNOList, puCommodityPriceList, mvc, sessionBoss);
			approvePurchasingOrder(purchasingOrder, mvc, sessionBoss);
			// 入库
			Warehousing warehousing = createWarehousing(purchasingOrder, mvc, sessionBoss);
			approveWarehousing(warehousing, mvc, sessionBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("50");
		ccs.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		//
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	/** 零售（1、当月没有销售数据，查看月报表数据统计是否正常和报表显示是否正确。） */
	@Test
	public void monthlyReportSITTest1() throws Exception {
		Shared.printTestClassEndInfo();

		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(0.000000d/* 总销售额 */, 0.000000d/* 总毛利 */);
		// 生成月报表，查看是否和预期结果一致
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 1、当月1号没有销售数据，2、3号销售数据为正，4、5号销售数据为负 */
	@Test(dependsOnMethods = "monthlyReportSITTest1")
	public void monthlyReportSITTest2() throws Exception {
		Shared.printTestClassEndInfo();

		// 修改变量的日期为第二个月
		int secondMonths = 1;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), secondMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), secondMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), secondMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), secondMonths)));

		Shared.caseLog("当月2号进行销售，数据为正");
		int[] iSaleCommodityNO = { 8 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade retailTradeOnTheSecondDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 7.040000d/* 总进货金额 */, 13.440000d/* 总销售额 */, 13.440000d/* 客单价 */, 6.400000d/* 毛利 */, 0.476190d/* 毛利率 */, normalCommodityA.getID(),
				8/* 畅销商品数量 */, 13.440000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 8/* 商品数量 */,
				7.040000d/* 总进货价 */, 13.440000d/* 总销售额 */, 6.400000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1)), dailyReportSummary, dailyReportByCommodityList);
		dailyReportByCommodityList.clear();

		Shared.caseLog("当月3号进行销售，数据为正");
		iSaleCommodityNO[0] = 11;
		commoditys[0] = normalCommodityB;
		RetailTrade retailTradeOnTheThirdDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 208.890000d/* 总进货金额 */, 427.680000d/* 总销售额 */, 427.680000d/* 客单价 */, 218.790000d/* 毛利 */, 0.511574d/* 毛利率 */, normalCommodityB.getID(), 11/* 畅销商品数量 */,
				427.680000d/* 畅销商品销售额 */);
		//
		dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 11/* 商品数量 */, 208.890000d/* 总进货价 */,
				427.680000d/* 总销售额 */, 218.790000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2)), dailyReportSummary, dailyReportByCommodityList);
		dailyReportByCommodityList.clear();

		Shared.caseLog("当月4号进行销售，销售数据为负");
		int[] iReturnCommodityNO = { 3 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 3), retailTradeOnTheSecondDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -2.640000d/* 总进货金额 */, -5.040000d/* 总销售额 */, -0.000000d/* 客单价 */, -2.400000d/* 毛利 */, 0.476190d/* 毛利率 */, 0, 0/* 畅销商品数量 */, 0.000000d/* 畅销商品销售额 */);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 3)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 3)), dailyReportSummary, dailyReportByCommodityList);

		Shared.caseLog("当月5号进行销售，销售数据为负");
		iReturnCommodityNO[0] = 5;
		returnCommoditys[0] = normalCommodityB;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 4), retailTradeOnTheThirdDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -94.950000d/* 总进货金额 */, -194.400000d/* 总销售额 */, -0.000000d/* 客单价 */, -99.450000d/* 毛利 */, 0.511574d/* 毛利率 */, 0, 0/* 畅销商品数量 */, 0.000000d/* 畅销商品销售额 */);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 4)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4)), dailyReportSummary, dailyReportByCommodityList);

		// 生成月报表，查看是否和预期结果一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(241.680000d/* 总销售额 */, 123.340000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 当月1、2号销售数据为正，3、4号没有销售数据，5、6、7、号销售数据为正，8号没有销售数据，9号销售数据为负，10、11销售数据为正 */
	@Test(dependsOnMethods = "monthlyReportSITTest2")
	public void monthlyReportSITTest3() throws Exception {
		Shared.printTestClassEndInfo();

		// 修改变量的日期为第三个月
		int thirdMonths = 2;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), thirdMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), thirdMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), thirdMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), thirdMonths)));

		Shared.caseLog("零售(1号销售数据为正)");
		int[] iSaleCommodityNO = { 15 };
		Commodity[] commoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary firstDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 372.900000d/* 总进货金额 */, 538.200000d/* 总销售额 */, 538.200000d/* 客单价 */, 165.300000d/* 毛利 */, 0.307135d/* 毛利率 */, subCommodity.getID(),
				15/* 畅销商品数量 */, 538.200000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> firstDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity firstDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 15/* 商品数量 */,
				372.900000d/* 总进货价 */, 538.200000d/* 总销售额 */, 165.300000d/* 毛利 */);
		firstDailyReportByCommodityList.add(firstDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0)), firstDailyReportSummary, firstDailyReportByCommodityList);

		Shared.caseLog("零售（2号销售数据为正）");
		iSaleCommodityNO[0] = 18;
		commoditys[0] = subCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary secondDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 447.480000d/* 总进货金额 */, 645.840000d/* 总销售额 */, 645.840000d/* 客单价 */, 198.360000d/* 毛利 */, 0.307135d/* 毛利率 */,
				subCommodity.getID(), 18/* 畅销商品数量 */, 645.840000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> secondDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 18/* 商品数量 */,
				447.480000d/* 总进货价 */, 645.840000d/* 总销售额 */, 198.360000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1)), secondDailyReportSummary, secondDailyReportByCommodityList);

		Shared.caseLog("零售（5号销售数据为正）");
		iSaleCommodityNO[0] = 13;
		commoditys[0] = subCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary fifthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 323.180000d/* 总进货金额 */, 466.440000d/* 总销售额 */, 466.440000d/* 客单价 */, 143.260000d/* 毛利 */, 0.307135d/* 毛利率 */, subCommodity.getID(),
				13/* 畅销商品数量 */, 466.440000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> fifthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity fifthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 13/* 商品数量 */,
				323.180000d/* 总进货价 */, 466.440000d/* 总销售额 */, 143.260000d/* 毛利 */);
		fifthDailyReportByCommodityList.add(fifthDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4)), fifthDailyReportSummary, fifthDailyReportByCommodityList);

		Shared.caseLog("零售（6号销售数据为正）");
		iSaleCommodityNO[0] = 28;
		commoditys[0] = subCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary sixthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 696.080000d/* 总进货金额 */, 1004.640000d/* 总销售额 */, 1004.640000d/* 客单价 */, 308.560000d/* 毛利 */, 0.307135d/* 毛利率 */,
				subCommodity.getID(), 28/* 畅销商品数量 */, 1004.640000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> sixthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity sixthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 28/* 商品数量 */,
				696.080000d/* 总进货价 */, 1004.640000d/* 总销售额 */, 308.560000d/* 毛利 */);
		sixthDailyReportByCommodityList.add(sixthDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 5)), sixthDailyReportSummary, sixthDailyReportByCommodityList);

		Shared.caseLog("零售（7号销售数据为正）");
		iSaleCommodityNO[0] = 28;
		commoditys[0] = subCommodity;
		RetailTrade retailTradeOnTheSeventhDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary seventhDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 696.080000d/* 总进货金额 */, 1004.640000d/* 总销售额 */, 1004.640000d/* 客单价 */, 308.560000d/* 毛利 */, 0.307135d/* 毛利率 */,
				subCommodity.getID(), 28/* 畅销商品数量 */, 1004.640000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> seventhDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity seventhDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 28/* 商品数量 */,
				696.080000d/* 总进货价 */, 1004.640000d/* 总销售额 */, 308.560000d/* 毛利 */);
		seventhDailyReportByCommodityList.add(seventhDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 6)), seventhDailyReportSummary, seventhDailyReportByCommodityList);

		Shared.caseLog("零售（9号销售数据为负）");
		int[] iReturnCommodityNO = { 9 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 8), retailTradeOnTheSeventhDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary ninthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -223.740000d/* 总进货金额 */, -322.920000d/* 总销售额 */, 0.000000d/* 客单价 */, -99.180000d/* 毛利 */, 0.307135d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 8)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8)), ninthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		Shared.caseLog("零售（10号销售数据为正）");
		iSaleCommodityNO[0] = 3;
		commoditys[0] = subCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary tenthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 74.580000d/* 总进货金额 */, 107.640000d/* 总销售额 */, 107.640000d/* 客单价 */, 33.060000d/* 毛利 */, 0.307135d/* 毛利率 */, subCommodity.getID(),
				3/* 畅销商品数量 */, 107.640000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> tenthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity tenthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/* 商品数量 */,
				74.580000d/* 总进货价 */, 107.640000d/* 总销售额 */, 33.060000d/* 毛利 */);
		tenthDailyReportByCommodityList.add(tenthDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 9)), tenthDailyReportSummary, tenthDailyReportByCommodityList);

		Shared.caseLog("零售（11号销售数据为正）");
		iSaleCommodityNO[0] = 20;
		commoditys[0] = subCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary eleventhDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 497.200000d/* 总进货金额 */, 717.600000d/* 总销售额 */, 717.600000d/* 客单价 */, 220.400000d/* 毛利 */, 0.307135d/* 毛利率 */,
				subCommodity.getID(), 20/* 畅销商品数量 */, 717.600000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> eleventhDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity eleventhDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 20/* 商品数量 */,
				497.200000d/* 总进货价 */, 717.600000d/* 总销售额 */, 220.400000d/* 毛利 */);
		eleventhDailyReportByCommodityList.add(eleventhDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 10)), eleventhDailyReportSummary, eleventhDailyReportByCommodityList);

		// 生成月报表。检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(4162.080000d/* 总销售额 */, 1278.320000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 前半个月（1-15号）销售数据为正，后半个月没有销售数据（半个月并不表示每天都得有销售数据） */
	@Test(dependsOnMethods = "monthlyReportSITTest3")
	public void monthlyReportSITTest4() throws Exception {
		Shared.printTestClassEndInfo();
		// 修改变量的日期为第四个月
		int fourthMonths = 3;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), fourthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), fourthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), fourthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), fourthMonths)));

		Shared.caseLog("零售（3号销售数据为正）");
		int[] iSaleCommodityNO = { 8 };
		Commodity[] commoditys = { multiPackageCommodityB };
		RetailTrade retailTradeOnTheThirdDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary thirdDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 303.840000d/* 总进货金额 */, 558.080000d/* 总销售额 */, 558.080000d/* 客单价 */, 254.240000d/* 毛利 */, 0.455562d/* 毛利率 */,
				multiPackageCommodityB.getID(), 8/* 畅销商品数量 */, 558.080000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> thirdDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity thirdDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 8/* 商品数量 */, 303.840000d/* 总进货价 */, 558.080000d/* 总销售额 */, 254.240000d/* 毛利 */);
		thirdDailyReportByCommodityList.add(thirdDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2)), thirdDailyReportSummary, thirdDailyReportByCommodityList);

		Shared.caseLog("零售（9号销售数据为负）");
		int[] iReturnCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), retailTradeOnTheThirdDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary ninthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -113.940000d/* 总进货金额 */, -209.280000d/* 总销售额 */, 0.000000d/* 客单价 */, -95.340000d/* 毛利 */, 0.455562d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8)), ninthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		Shared.caseLog("零售（14号销售数据为正）");
		iSaleCommodityNO[0] = 20;
		commoditys[0] = multiPackageCommodityB;
		RetailTrade retailTradeOnTheFourteenthDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 当天进行退货
		iReturnCommodityNO[0] = 10;
		returnCommoditys[0] = multiPackageCommodityB;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), retailTradeOnTheFourteenthDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary fourteenthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 379.800000d/* 总进货金额 */, 697.600000d/* 总销售额 */, 697.600000d/* 客单价 */, 317.800000d/* 毛利 */, 0.455562d/* 毛利率 */,
				multiPackageCommodityB.getID(), 10/* 畅销商品数量 */, 697.600000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> fourteenthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity fourteenthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 20/* 商品数量 */, 379.800000d/* 总进货价 */, 697.600000d/* 总销售额 */, 317.800000d/* 毛利 */);
		fourteenthDailyReportByCommodityList.add(fourteenthDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 13)), fourteenthDailyReportSummary, fourteenthDailyReportByCommodityList);

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(1046.400000d/* 总销售额 */, 476.700000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 前半个月没有销售数据，后半个月销售数据为正（半个月并不表示每天都得有销售数据） */
	@Test(dependsOnMethods = "monthlyReportSITTest4")
	public void monthlyReportSITTest5() throws Exception {
		Shared.printTestClassEndInfo();

		// 修改变量的日期为第五个月
		int fifthMonths = 4;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), fifthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), fifthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), fifthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), fifthMonths)));

		Shared.caseLog("零售（23号销售数据为正）");
		int[] iSaleCommodityNO = { 18, 15 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary twentyThirdDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 388.740000d/* 总进货金额 */, 568.440000d/* 总销售额 */, 568.440000d/* 客单价 */, 179.700000d/* 毛利 */, 0.316128d/* 毛利率 */,
				normalCommodityA.getID(), 18/* 畅销商品数量 */, 30.240000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentyThirdDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentyThirdDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				18/* 商品数量 */, 15.840000d/* 总进货价 */, 30.240000d/* 总销售额 */, 14.400000d/* 毛利 */);
		twentyThirdDailyReportByCommodityList.add(twentyThirdDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentyThirdDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 15/* 商品数量 */,
				372.90000d/* 总进货价 */, 538.200000d/* 总销售额 */, 165.300000d/* 毛利 */);
		twentyThirdDailyReportByCommodityList.add(twentyThirdDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 22)), twentyThirdDailyReportSummary, twentyThirdDailyReportByCommodityList);

		Shared.caseLog("零售（24号销售数据为正）");
		iSaleCommodityNO[0] = 8;
		iSaleCommodityNO[1] = 18;
		commoditys[0] = normalCommodityA;
		commoditys[1] = multiPackageCommodityB;
		RetailTrade retailTradeOnTheTwentyFourthDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary twentyFourthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 690.680000d/* 总进货金额 */, 1269.120000d/* 总销售额 */, 1269.120000d/* 客单价 */, 578.440000d/* 毛利 */, 0.455780d/* 毛利率 */,
				multiPackageCommodityB.getID(), 18/* 畅销商品数量 */, 1255.680000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentyFourthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentyFourthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				8/* 商品数量 */, 7.040000d/* 总进货价 */, 13.440000d/* 总销售额 */, 6.400000d/* 毛利 */);
		twentyFourthDailyReportByCommodityList.add(twentyFourthDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentyFourthDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 18/* 商品数量 */, 683.64000d/* 总进货价 */, 1255.680000d/* 总销售额 */, 572.040000d/* 毛利 */);
		twentyFourthDailyReportByCommodityList.add(twentyFourthDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 23)), twentyFourthDailyReportSummary, twentyFourthDailyReportByCommodityList);
		//
		sourceRetailTrade = retailTradeOnTheTwentyFourthDay;

		Shared.caseLog("零售（25号销售数据为正）");
		iSaleCommodityNO[0] = 6;
		iSaleCommodityNO[1] = 12;
		commoditys[0] = normalCommodityA;
		commoditys[1] = serviceCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary twentyFifthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 5.280000d/* 总进货金额 */, 23.400000d/* 总销售额 */, 23.400000d/* 客单价 */, 18.120000d/* 毛利 */, 0.774359d/* 毛利率 */,
				serviceCommodity.getID(), 12/* 畅销商品数量 */, 13.320000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentyFifthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentyFifthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				6/* 商品数量 */, 5.280000d/* 总进货价 */, 10.080000d/* 总销售额 */, 4.800000d/* 毛利 */);
		twentyFifthDailyReportByCommodityList.add(twentyFifthDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentyFifthDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				12/* 商品数量 */, 0.00000d/* 总进货价 */, 13.320000d/* 总销售额 */, 13.320000d/* 毛利 */);
		twentyFifthDailyReportByCommodityList.add(twentyFifthDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 24)), twentyFifthDailyReportSummary, twentyFifthDailyReportByCommodityList);

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(1860.960000d/* 总销售额 */, 776.260000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 前半个月（1-15号）销售数据为正，后半个月销售数据为负 */
	@Test(dependsOnMethods = "monthlyReportSITTest5")
	public void monthlyReportSITTest6() throws Exception {
		Shared.printTestClassEndInfo();

		// 修改变量的日期为第六个月
		int sixthMonths = 5;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), sixthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), sixthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), sixthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), sixthMonths)));

		Shared.caseLog("零售（2号销售数据为正）");
		int[] iSaleCommodityNO = { 5, 38 };
		Commodity[] commoditys = { subCommodity, multiPackageCommodityB };
		RetailTrade retailTradeOnTheSecondDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary secondDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 1567.540000d/* 总进货金额 */, 2830.280000d/* 总销售额 */, 2830.280000d/* 客单价 */, 1262.740000d/* 毛利 */, 0.446154d/* 毛利率 */,
				multiPackageCommodityB.getID(), 38/* 畅销商品数量 */, 2650.880000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> secondDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 5/* 商品数量 */,
				124.300000d/* 总进货价 */, 179.400000d/* 总销售额 */, 55.100000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 38/* 商品数量 */, 1443.24000d/* 总进货价 */, 2650.880000d/* 总销售额 */, 1207.640000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1)), secondDailyReportSummary, secondDailyReportByCommodityList);

		Shared.caseLog("零售（5号销售数据为正）");
		iSaleCommodityNO[0] = 2;
		iSaleCommodityNO[1] = 20;
		commoditys[0] = subCommodity;
		commoditys[1] = serviceCommodity;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary fifthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 49.720000d/* 总进货金额 */, 93.960000d/* 总销售额 */, 93.960000d/* 客单价 */, 44.240000d/* 毛利 */, 0.470839d/* 毛利率 */, serviceCommodity.getID(),
				20/* 畅销商品数量 */, 22.200000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> fifthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity fifthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/* 商品数量 */,
				49.720000d/* 总进货价 */, 71.760000d/* 总销售额 */, 22.040000d/* 毛利 */);
		fifthDailyReportByCommodityList.add(fifthDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity fifthDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				20/* 商品数量 */, 0.00000d/* 总进货价 */, 22.200000d/* 总销售额 */, 22.200000d/* 毛利 */);
		fifthDailyReportByCommodityList.add(fifthDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4)), fifthDailyReportSummary, fifthDailyReportByCommodityList);

		Shared.caseLog("零售（29号销售数据为负）");
		int[] iReturnCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), retailTradeOnTheSecondDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary twentyNinthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -113.940000d/* 总进货金额 */, -209.280000d/* 总销售额 */, 0.000000d/* 客单价 */, -95.340000d/* 毛利 */, 0.455562d/* 毛利率 */, 0,
				0/* 畅销商品数量 */, 0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 28)), twentyNinthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(2714.960000d/* 总销售额 */, 1211.640000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 前半个销售数据为负，后半个月销售数据为正 */
	@Test(dependsOnMethods = "monthlyReportSITTest6")
	public void monthlyReportSITTest7() throws Exception {
		Shared.printTestClassEndInfo();

		Assert.assertTrue(sourceRetailTrade != null, "源零售单为空,不足以接下来的测试。");
		// 修改变量的日期为第七个月
		int seventhMonths = 6;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), seventhMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), seventhMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), seventhMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), seventhMonths)));

		Shared.caseLog("零售（10号销售数据为负）");
		int[] iReturnCommodityNO = { 8 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), sourceRetailTrade, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary tenthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -303.840000d/* 总进货金额 */, -558.080000d/* 总销售额 */, 0.000000d/* 客单价 */, -254.240000d/* 毛利 */, 0.455562d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 9)), tenthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		Shared.caseLog("零售（25号销售数据为正）");
		int[] iSaleCommodityNO = { 10, 5 };
		Commodity[] commoditys = { multiPackageCommodityB, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary twentyFifthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 379.800000d/* 总进货金额 */, 703.150000d/* 总销售额 */, 703.150000d/* 客单价 */, 323.350000d/* 毛利 */, 0.459859d/* 毛利率 */,
				multiPackageCommodityB.getID(), 10/* 畅销商品数量 */, 697.600000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentyFifthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentyFifthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 10/* 商品数量 */, 379.800000d/* 总进货价 */, 697.600000d/* 总销售额 */, 317.800000d/* 毛利 */);
		twentyFifthDailyReportByCommodityList.add(twentyFifthDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentyFifthDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				5/* 商品数量 */, 0.00000d/* 总进货价 */, 5.550000d/* 总销售额 */, 5.550000d/* 毛利 */);
		twentyFifthDailyReportByCommodityList.add(twentyFifthDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 24)), twentyFifthDailyReportSummary, twentyFifthDailyReportByCommodityList);

		Shared.caseLog("零售（26号销售数据为正）");
		int[] iSaleCommodityNO2 = { 5 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 25), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		RetailTradeDailyReportSummary twentySixthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 0.000000d/* 总进货金额 */, 5.550000d/* 总销售额 */, 5.550000d/* 客单价 */, 5.550000d/* 毛利 */, 1.000000d/* 毛利率 */,
				serviceCommodity.getID(), 5/* 畅销商品数量 */, 5.550000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentySixthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentySixthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				5/* 商品数量 */, 0.00000d/* 总进货价 */, 5.550000d/* 总销售额 */, 5.550000d/* 毛利 */);
		twentySixthDailyReportByCommodityList.add(twentySixthDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 25)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 25)), twentySixthDailyReportSummary, twentySixthDailyReportByCommodityList);

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(150.620000d/* 总销售额 */, 74.660000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 某月的数据为正（因私人原因，可能一个月菜营业一天或者两三天） */
	@Test(dependsOnMethods = "monthlyReportSITTest7")
	public void monthlyReportSITTest8() throws Exception {
		Shared.printTestClassEndInfo();

		int eighthMonths = 7;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), eighthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), eighthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), eighthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), eighthMonths)));

		Shared.caseLog("零售（30号销售数据为正）");
		int[] iSaleCommodityNO = { 5, 6, 4 };
		Commodity[] commoditys = { normalCommodityB, subCommodity, multiPackageCommodityB };
		RetailTrade retailTradeOnTheThirtyFirstDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 29), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary thirtyFirstDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 396.030000d/* 总进货金额 */, 688.720000d/* 总销售额 */, 688.720000d/* 客单价 */, 292.690000d/* 毛利 */, 0.424977d/* 毛利率 */,
				subCommodity.getID(), 6/* 畅销商品数量 */, 215.280000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> thirtyFirstDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity thirtyFirstDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				5/* 商品数量 */, 94.95000d/* 总进货价 */, 194.400000d/* 总销售额 */, 99.450000d/* 毛利 */);
		thirtyFirstDailyReportByCommodityList.add(thirtyFirstDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity thirtyFirstDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 6/* 商品数量 */,
				149.16000d/* 总进货价 */, 215.280000d/* 总销售额 */, 66.120000d/* 毛利 */);
		thirtyFirstDailyReportByCommodityList.add(thirtyFirstDailyReportByCommodity2);
		RetailTradeDailyReportByCommodity thirtyFirstDailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 4/* 商品数量 */, 151.92000d/* 总进货价 */, 279.040000d/* 总销售额 */, 127.120000d/* 毛利 */);
		thirtyFirstDailyReportByCommodityList.add(thirtyFirstDailyReportByCommodity3);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 29)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 29)), thirtyFirstDailyReportSummary, thirtyFirstDailyReportByCommodityList);
		// 记录源零售单，下个月进行退货
		sourceRetailTrade = retailTradeOnTheThirtyFirstDay;

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(688.720000d/* 总销售额 */, 292.690000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 某月的数据为负（因私人原因，可能一个月中只营业一天，且这一天中只退货的情况） */
	@Test(dependsOnMethods = "monthlyReportSITTest8")
	public void monthlyReportSITTest9() throws Exception {
		Shared.printTestClassEndInfo();

		Assert.assertTrue(sourceRetailTrade != null, "源零售单为空，无法进行接下来的测试");
		int ninthMonths = 8;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), ninthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), ninthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), ninthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), ninthMonths)));

		Shared.caseLog("零售（29号销售数据为负）");
		int[] iReturnCommodityNO = { 4 };
		Commodity[] returnCommoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), sourceRetailTrade, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary twentyNinthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -75.960000d/* 总进货金额 */, -155.520000d/* 总销售额 */, 0.000000d/* 客单价 */, -79.560000d/* 毛利 */, 0.511574d/* 毛利率 */, 0,
				0/* 畅销商品数量 */, 0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 28)), twentyNinthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(-155.520000d/* 总销售额 */, -79.560000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 当月1-10号的销售数据为正，10-20号的销售数据为负，20-31号没有销售数据 */
	@Test(dependsOnMethods = "monthlyReportSITTest9")
	public void monthlyReportSITTest10() throws Exception {
		Shared.printTestClassEndInfo();

		int tenthMonths = 9;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), tenthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), tenthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), tenthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), tenthMonths)));

		Shared.caseLog("零售（2号销售数据为正）");
		int[] iSaleCommodityNO = { 2, 3, 3, 8 };
		Commodity[] commoditys = { normalCommodityB, subCommodity, multiPackageCommodityB, serviceCommodity };
		RetailTrade retailTradeOnTheSecondDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary secondDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 226.500000d/* 总进货金额 */, 403.560000d/* 总销售额 */, 403.560000d/* 客单价 */, 177.060000d/* 毛利 */, 0.438745d/* 毛利率 */,
				serviceCommodity.getID(), 8/* 畅销商品数量 */, 8.880000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> secondDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				2/* 商品数量 */, 37.98000d/* 总进货价 */, 77.760000d/* 总销售额 */, 39.780000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/* 商品数量 */,
				74.58000d/* 总进货价 */, 107.640000d/* 总销售额 */, 33.060000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity2);
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 3/* 商品数量 */, 113.94000d/* 总进货价 */, 209.280000d/* 总销售额 */, 95.340000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity3);
		RetailTradeDailyReportByCommodity secondDailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				8/* 商品数量 */, 0.00000d/* 总进货价 */, 8.880000d/* 总销售额 */, 8.880000d/* 毛利 */);
		secondDailyReportByCommodityList.add(secondDailyReportByCommodity4);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1)), secondDailyReportSummary, secondDailyReportByCommodityList);

		Shared.caseLog("零售（19号销售数据为负）");
		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18), retailTradeOnTheSecondDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary twentyNinthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -37.980000d/* 总进货金额 */, -77.760000d/* 总销售额 */, 0.000000d/* 客单价 */, -39.780000d/* 毛利 */, 0.511574d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 28)), twentyNinthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(325.800000d/* 总销售额 */, 137.280000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 当月1-10号的销售数据为负，10-20号没有销售数据，20-31号销售数据的销售数据为正 */
	@Test(dependsOnMethods = "monthlyReportSITTest10")
	public void monthlyReportSITTest11() throws Exception {
		Shared.printTestClassEndInfo();

		int eleventhMonths = 10;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), eleventhMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), eleventhMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), eleventhMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), eleventhMonths)));

		Shared.caseLog("零售（1号销售数据为正）");
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { normalCommodityB };
		RetailTrade retailTradeOnTheFirstDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary firstDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 18.990000d/* 总进货金额 */, 38.880000d/* 总销售额 */, 38.880000d/* 客单价 */, 19.890000d/* 毛利 */, 0.511574d/* 毛利率 */, normalCommodityB.getID(),
				1/* 畅销商品数量 */, 38.880000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> firstDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity firstDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				1/* 商品数量 */, 18.99000d/* 总进货价 */, 38.880000d/* 总销售额 */, 19.890000d/* 毛利 */);
		firstDailyReportByCommodityList.add(firstDailyReportByCommodity1);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0)), firstDailyReportSummary, firstDailyReportByCommodityList);

		Shared.caseLog("零售（8号销售数据为负）");
		int[] iReturnCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), retailTradeOnTheFirstDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary eighthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -18.990000d/* 总进货金额 */, -38.880000d/* 总销售额 */, 0.000000d/* 客单价 */, -19.890000d/* 毛利 */, 0.511574d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 7)), eighthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		Shared.caseLog("零售（28号销售数据为正）");
		int[] iSaleCommodityNO2 = { 200, 20 };
		Commodity[] commoditys2 = { normalCommodityA, normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 27), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		RetailTradeDailyReportSummary twentyEighthDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 555.800000d/* 总进货金额 */, 1113.600000d/* 总销售额 */, 1113.600000d/* 客单价 */, 557.800000d/* 毛利 */, 0.500898d/* 毛利率 */,
				normalCommodityA.getID(), 200/* 畅销商品数量 */, 336.000000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentyEighthDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentyEighthDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				200/* 商品数量 */, 176.00000d/* 总进货价 */, 336.000000d/* 总销售额 */, 160.000000d/* 毛利 */);
		twentyEighthDailyReportByCommodityList.add(twentyEighthDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentyEighthDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				20/* 商品数量 */, 379.80000d/* 总进货价 */, 777.600000d/* 总销售额 */, 397.800000d/* 毛利 */);
		twentyEighthDailyReportByCommodityList.add(twentyEighthDailyReportByCommodity2);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 27)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 27)), twentyEighthDailyReportSummary, twentyEighthDailyReportByCommodityList);

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(1113.600000d/* 总销售额 */, 557.800000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);
	}

	/** 当月1-10号没有销售数据，10-20的销售数据为正，20-30的销售数据为负 */
	@Test(dependsOnMethods = "monthlyReportSITTest11")
	public void monthlyReportSITTest12() throws Exception {
		Shared.printTestClassEndInfo();

		int twelfthMonths = 11;
		SALE_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(SALE_DATE), twelfthMonths));
		RETURN_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(RETURN_DATE), twelfthMonths));
		REPORT_DATE = sdf.format(DatetimeUtil.addMonths(sdf.parse(REPORT_DATE), twelfthMonths));
		TARGET_DATE = sdf.format(DatetimeUtil.getLastDayDateOfMonth(DatetimeUtil.addMonths(sdf.parse(TARGET_DATE), twelfthMonths)));

		Shared.caseLog("零售（20号销售数据为正）");
		int[] iSaleCommodityNO = { 8, 6, 9, 4, 20 };
		Commodity[] commoditys = { normalCommodityB, subCommodity, multiPackageCommodityB, serviceCommodity, normalCommodityA };
		RetailTrade retailTradeOnTheTwentiethDay = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		//
		RetailTradeDailyReportSummary twentiethDailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 660.500000d/* 总进货金额 */, 1192.200000d/* 总销售额 */, 1192.200000d/* 客单价 */, 531.700000d/* 毛利 */, 0.44598221d/* 毛利率 */,
				normalCommodityA.getID(), 20/* 畅销商品数量 */, 33.600000d/* 畅销商品销售额 */);
		//
		List<RetailTradeDailyReportByCommodity> twentiethDailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity twentiethDailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				8/* 商品数量 */, 151.92000d/* 总进货价 */, 311.040000d/* 总销售额 */, 159.120000d/* 毛利 */);
		twentiethDailyReportByCommodityList.add(twentiethDailyReportByCommodity1);
		RetailTradeDailyReportByCommodity twentiethDailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 6/* 商品数量 */,
				149.16000d/* 总进货价 */, 215.280000d/* 总销售额 */, 66.120000d/* 毛利 */);
		twentiethDailyReportByCommodityList.add(twentiethDailyReportByCommodity2);
		RetailTradeDailyReportByCommodity twentiethDailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 9/* 商品数量 */, 341.82000d/* 总进货价 */, 627.840000d/* 总销售额 */, 286.020000d/* 毛利 */);
		twentiethDailyReportByCommodityList.add(twentiethDailyReportByCommodity3);
		RetailTradeDailyReportByCommodity twentiethDailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(),
				4/* 商品数量 */, 0.00000d/* 总进货价 */, 4.440000d/* 总销售额 */, 4.440000d/* 毛利 */);
		twentiethDailyReportByCommodityList.add(twentiethDailyReportByCommodity4);
		RetailTradeDailyReportByCommodity twentiethDailyReportByCommodity5 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				20/* 商品数量 */, 17.60000d/* 总进货价 */, 33.600000d/* 总销售额 */, 16.000000d/* 毛利 */);
		twentiethDailyReportByCommodityList.add(twentiethDailyReportByCommodity5);
		// 生成日报表检验日报表是否正确
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 19)), twentiethDailyReportSummary, twentiethDailyReportByCommodityList);

		Shared.caseLog("零售（29号销售数据为负）");
		int[] iReturnCommodityNO = { 1, 2, 3, 2 };
		Commodity[] returnCommoditys = { normalCommodityB, multiPackageCommodityB, serviceCommodity, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), retailTradeOnTheTwentiethDay, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);
		//
		RetailTradeDailyReportSummary eighthDailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -144.670000d/* 总进货金额 */, -253.490000d/* 总销售额 */, 0.000000d/* 客单价 */, -108.820000d/* 毛利 */, 0.429287d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 28)), eighthDailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());

		// 生成月报表，检验是否和预期的一致
		RetailTradeMonthlyReportSummary expectRetailTradeMonthlyReportSummary = getRetailTradeMonthlyReportSummary(938.710000d/* 总销售额 */, 422.880000d/* 总毛利 */);
		runRetailTradeMonthlyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(TARGET_DATE), 0)), expectRetailTradeMonthlyReportSummary);

	}
}
