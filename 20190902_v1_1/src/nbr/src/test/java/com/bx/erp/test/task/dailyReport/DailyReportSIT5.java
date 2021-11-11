package com.bx.erp.test.task.dailyReport;

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
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

/** 测试目标： 未入库与跨库 1.零售(创建一个单品A,未入库，直接进行售卖,当天进行部分退货) 2.
 * 零售（创建一个单品A，入库5件，售出10件（此时前五件正常售卖，后面五件的入库价按照最后一次此商品入库的价格进行计算），当天全部退货）
 * 3.零售（创建一个单品A，在入库单1入库5件，在入库单2入库10件，售出10件，每次的入库价均不同），当天部分退货）
 * 4.零售（创建一个单品A，在入库单1入库5件，在入库单2入库10件，在入库单3入库8件，售出20件，每次的入库价均不同)
 * 5.零售（创建一个组合商品，未入库，直接售卖） ....
 * 
 * 测试步骤： 1、创建商品 创建创建单品A、B、C、D; 单品B增加个多包装商品B; 创建组合商品C，子商品为C和D 创建服务型商品F
 * 2、今日创建采购单，采购商品ABCD，审核该采购单 3、创建入库单，入库商品ABCD，审核该入库单,4对各种商品组合进行零售 5、当天对商品进行退货
 * 6、运行夜间任务，生成日报表。 */
@WebAppConfiguration
public class DailyReportSIT5 extends BaseDailyReportSIT {
	private Staff staff;

	private Commodity normalCommodityA;
	private Commodity normalCommodityB;
	private Commodity normalCommodityC;
	private Commodity normalCommodityD;
	private Commodity multiPackageCommodityB;
	private Commodity subCommodity;
	private Commodity serviceCommodity;

	public static String SALE_DATE = "2028-7-01";
	public static String REPORT_DATE = "2028-7-02";
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);

	@BeforeClass
	public void setup() {
		super.setUp();
		try {
			Shared.resetPOS(mvc, 1);
			sessionBoss = Shared.getPosLoginSession(mvc, 1);

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
			normalCommodityA = createNormalCommodity(1.280000d, mvc, sessionBoss);
			// 创建带多包装商品的普通商品B
			Integer[] mulitpleList = { 2 };
			Double[] mulitplePriceList = { 20.540000d };
			double price = 20.230000d;
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
			normalCommodityC = createNormalCommodity(2.000000d, mvc, sessionBoss);
			//
			normalCommodityD = createNormalCommodity(3.560000d, mvc, sessionBoss);
			// 创建普通商品CD的组合商品
			Commodity[] subCommodityList = { normalCommodityC, normalCommodityD };
			Integer[] subCommodityNOList = { 1, 2 };
			Double[] subCommodityPriceList = { normalCommodityC.getPriceRetail(), normalCommodityD.getPriceRetail() };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 5.880000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity = createServiceCommodity(1.120000d, mvc, sessionBoss);
			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, normalCommodityC, normalCommodityD, multiPackageCommodityB, subCommodity, serviceCommodity });
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

	/** 零售(创建一个单品A,未入库，直接进行售卖,当天进行部分退货) */
	@Test
	public void dailyReportSITTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		// 零售(创建一个单品A,未入库，直接进行售卖,当天进行部分退货)
		int[] iSaleCommodityNO = { 5 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iaReturnPromotionID = { 3 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 0.000000d/* 总进货金额 */, 2.560000d/* 总销售额 */, 2.560000d/* 客单价 */, 2.560000d/* 毛利 */, 1.000000d/* 毛利率 */, normalCommodityA.getID(),
				2/* 畅销商品数量 */, 2.560000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 5/* 商品数量 */,
				0.000000d/* 总进货价 */, 2.560000d/* 总销售额 */, 2.560000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0)), dailyReportSummary, dailyReportByCommodityList);
	}

	public void purchaseAndWarehousing(Commodity[] puCommodityList, Integer[] puCommodityNOList, Double[] puCommodityPriceList) throws Exception {
		PurchasingOrder purchasingOrder = createPurchasingOrder(puCommodityList, puCommodityNOList, puCommodityPriceList, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder, mvc, sessionBoss);
		// 入库
		Warehousing warehousing = createWarehousing(purchasingOrder, mvc, sessionBoss);
		approveWarehousing(warehousing, mvc, sessionBoss);
	}

	/** 零售（创建一个单品A，入库5件，售出10件（此时前五件正常售卖，后面五件的入库价按照最后一次此商品入库的价格进行计算），当天全部退货） */
	@Test(dependsOnMethods = { "dailyReportSITTest1" })
	public void dailyReportSITTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		// 采购并入库普通商品A
		Commodity[] puCommodityList = { normalCommodityA };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 0.390000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 售卖商品A
		int[] iSaleCommodityNO = { 10 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 全部退货商品A
		int[] iaReturnPromotionID = { 10 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		// 当天进行全部退货,收入为0.不会生成
	}

	/** 零售（创建一个单品A，在入库单1入库5件，在入库单2入库10件，售出10件，每次的入库价均不同），当天部分退货） */
	@Test(dependsOnMethods = { "dailyReportSITTest2" })
	public void dailyReportSITTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品A5件
		Commodity[] puCommodityList = { normalCommodityA };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 0.390000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库商品A10件
		puCommodityList[0] = normalCommodityA;
		puCommodityNOList[0] = 10;
		puCommodityPriceList[0] = 0.310000d;
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 售卖商品A
		int[] iSaleCommodityNO = { 10 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货商品A
		int[] iaReturnPromotionID = { 4 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 2.340000d/* 总进货金额 */, 7.680000d/* 总销售额 */, 7.680000d/* 客单价 */, 5.340000d/* 毛利 */, 0.695313d/* 毛利率 */, normalCommodityA.getID(),
				6/* 畅销商品数量 */, 7.680000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				10/* 商品数量 */, 2.340000d/* 总进货价 */, 7.680000d/* 总销售额 */, 5.340000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个单品A，在入库单1入库5件，在入库单2入库10件，在入库单3入库8件，售出20件，每次的入库价均不同） */
	@Test(dependsOnMethods = { "dailyReportSITTest3" })
	public void dailyReportSITTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品A5件
		Commodity[] puCommodityList = { normalCommodityA };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 0.390000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库商品A10件
		puCommodityList[0] = normalCommodityA;
		puCommodityNOList[0] = 10;
		puCommodityPriceList[0] = 0.310000d;
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第三次采购并入库商品A10件
		puCommodityList[0] = normalCommodityA;
		puCommodityNOList[0] = 8;
		puCommodityPriceList[0] = 0.280000d;
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 售卖商品A
		int[] iSaleCommodityNO = { 20 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货商品A
		int[] iaReturnPromotionID = { 13 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 2.490000d/* 总进货金额 */, 8.960000d/* 总销售额 */, 8.960000d/* 客单价 */, 6.470000d/* 毛利 */, 0.722098d/* 毛利率 */, normalCommodityA.getID(),
				7/* 畅销商品数量 */, 8.960000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				20/* 商品数量 */, 2.490000d/* 总进货价 */, 8.960000d/* 总销售额 */, 6.470000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 3)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个组合商品，未入库，直接售卖） */
	@Test(dependsOnMethods = { "dailyReportSITTest4" })
	public void dailyReportSITTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		int[] iSaleCommodityNO = { 15 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货组合商品
		int[] iaReturnPromotionID = { 8 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 0.000000d/* 总进货金额 */, 41.160000d/* 总销售额 */, 41.160000d/* 客单价 */, 41.160000d/* 毛利 */, 1.000000d/* 毛利率 */, subCommodity.getID(),
				7/* 畅销商品数量 */, 41.160000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 15/* 商品数量 */,
				0.000000d/* 总进货价 */, 41.160000d/* 总销售额 */, 41.160000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个组合商品，入库10件，卖出20件（此时前五件正常售卖，后面15件的入库价按照最后一次此商品入库的价格进行计算）） */
	@Test(dependsOnMethods = { "dailyReportSITTest5" })
	public void dailyReportSITTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		// 入库组合商品的子商品普通商品C跟普通商品D
		// 第一次采购并入库商品C,D10件
		Commodity[] puCommodityList = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList = { 10, 10 };
		Double[] puCommodityPriceList = { 0.990000d, 1.560000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 售卖组合商品
		int[] iSaleCommodityNO = { 20 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货组合商品
		int[] iaReturnPromotionID = { 9 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 45.210000d/* 总进货金额 */, 64.680000d/* 总销售额 */, 64.680000d/* 客单价 */, 19.470000d/* 毛利 */, 0.301020d/* 毛利率 */, subCommodity.getID(),
				11/* 畅销商品数量 */, 64.680000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 20/* 商品数量 */,
				45.210000d/* 总进货价 */, 64.680000d/* 总销售额 */, 19.470000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 5)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个组合商品，在入库单1入库5件，在入库单2入库10件，售出10件,每次的入库价均不同） */
	@Test(dependsOnMethods = { "dailyReportSITTest6" })
	public void dailyReportSITTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		// 入库组合商品的子商品普通商品C跟普通商品D
		// 第一次采购并入库商品C,D5件
		Commodity[] puCommodityList = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList = { 5, 5 };
		Double[] puCommodityPriceList = { 0.990000d, 1.560000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库商品C,D10件
		Commodity[] puCommodityList2 = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList2 = { 10, 10 };
		Double[] puCommodityPriceList2 = { 0.950000d, 1.460000d };
		purchaseAndWarehousing(puCommodityList2, puCommodityNOList2, puCommodityPriceList2);
		// 售卖组合商品
		int[] iSaleCommodityNO = { 10 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货组合商品
		int[] iaReturnPromotionID = { 5 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 20.050000d/* 总进货金额 */, 29.400000d/* 总销售额 */, 29.400000d/* 客单价 */, 9.350000d/* 毛利 */, 0.318027d/* 毛利率 */, subCommodity.getID(),
				5/* 畅销商品数量 */, 29.400000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 10/* 商品数量 */,
				20.050000d/* 总进货价 */, 29.400000d/* 总销售额 */, 9.350000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 6)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个组合商品，在入库单1入库5件，在入库单2入库10件，在入库单3入库8件，售出18件,每次的入库价均不同） */
	@Test(dependsOnMethods = { "dailyReportSITTest7" })
	public void dailyReportSITTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品C,D5件
		Commodity[] puCommodityList = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList = { 5, 5 };
		Double[] puCommodityPriceList = { 0.990000d, 1.560000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库商品C,D10件
		Commodity[] puCommodityList2 = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList2 = { 10, 10 };
		Double[] puCommodityPriceList2 = { 0.950000d, 1.460000d };
		purchaseAndWarehousing(puCommodityList2, puCommodityNOList2, puCommodityPriceList2);
		// 第三次采购并入库商品C,D8件
		Commodity[] puCommodityList3 = { normalCommodityC, normalCommodityD };
		Integer[] puCommodityNOList3 = { 8, 8 };
		Double[] puCommodityPriceList3 = { 0.890000d, 1.350000d };
		purchaseAndWarehousing(puCommodityList3, puCommodityNOList3, puCommodityPriceList3);
		// 售卖组合商品
		int[] iSaleCommodityNO = { 18 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货组合商品
		int[] iaReturnPromotionID = { 6 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 46.580000d/* 总进货金额 */, 70.560000d/* 总销售额 */, 70.560000d/* 客单价 */, 23.980000d/* 毛利 */, 0.339853d/* 毛利率 */, subCommodity.getID(),
				12/* 畅销商品数量 */, 70.560000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 18/* 商品数量 */,
				46.580000d/* 总进货价 */, 70.560000d/* 总销售额 */, 23.980000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 7)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个多包装商品，未入库，直接进行售卖） */
	@Test
	public void dailyReportSITTest9() throws Exception {
		Shared.printTestMethodStartInfo();
		// 售卖组合商品
		int[] iSaleCommodityNO = { 5 };
		Commodity[] commoditys = { multiPackageCommodityB };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 部分退货组合商品
		int[] iaReturnPromotionID = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 0.000000d/* 总进货金额 */, 41.080000d/* 总销售额 */, 41.080000d/* 客单价 */, 41.080000d/* 毛利 */, 1.000000d/* 毛利率 */, multiPackageCommodityB.getID(),
				2/* 畅销商品数量 */, 41.080000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 5/* 商品数量 */, 0.000000d/* 总进货价 */, 41.080000d/* 总销售额 */, 41.080000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个多包装商品，入库5件，售出10件（此时前五件正常售卖，后面五件的入库价按照最后一次此商品入库的价格进行计算）） */
	@Test(dependsOnMethods = { "dailyReportSITTest9" })
	public void dailyReportSITTest10() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品B5件
		Commodity[] puCommodityList = { normalCommodityB };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 8.990000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 售卖多包装商品B
		int[] iSaleCommodityNO = { 10 };
		Commodity[] commoditys = { multiPackageCommodityB };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 退货商品B
		int[] iaReturnPromotionID = { 10 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		// 当天毛利为0，不生成报表
	}

	/** 零售（创建一个多包装商品，在入库单1入库5件，在入库单2入库10件，售出10件，每次的入库价均不同） */
	@Test(dependsOnMethods = { "dailyReportSITTest10" })
	public void dailyReportSITTest11() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品B5件
		Commodity[] puCommodityList = { normalCommodityB };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 8.990000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库装商品B10件
		Commodity[] puCommodityList2 = { normalCommodityB };
		Integer[] puCommodityNOList2 = { 10 };
		Double[] puCommodityPriceList2 = { 8.59000d };
		purchaseAndWarehousing(puCommodityList2, puCommodityNOList2, puCommodityPriceList2);
		// 售卖多包装商品
		int[] iSaleCommodityNO = { 10 };
		Commodity[] commoditys = { multiPackageCommodityB };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 退货多包装商品
		int[] iaReturnPromotionID = { 4 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 107.080000d/* 总进货金额 */, 123.240000d/* 总销售额 */, 123.240000d/* 客单价 */, 16.160000d/* 毛利 */, 0.131126d/* 毛利率 */,
				multiPackageCommodityB.getID(), 6/* 畅销商品数量 */, 123.240000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 10/* 商品数量 */, 107.080000d/* 总进货价 */, 123.240000d/* 总销售额 */, 16.160000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 10)), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售（创建一个多包装商品，在入库单1入库5件，在入库单2入库10件，在入库单3入库8件，售出20件，每次的入库价均不同） */
	@Test(dependsOnMethods = { "dailyReportSITTest11" })
	public void dailyReportSITTest12() throws Exception {
		Shared.printTestMethodStartInfo();
		// 第一次采购并入库商品B5件
		Commodity[] puCommodityList = { normalCommodityB };
		Integer[] puCommodityNOList = { 5 };
		Double[] puCommodityPriceList = { 8.990000d };
		purchaseAndWarehousing(puCommodityList, puCommodityNOList, puCommodityPriceList);
		// 第二次采购并入库装商品B10件
		Commodity[] puCommodityList2 = { normalCommodityB };
		Integer[] puCommodityNOList2 = { 10 };
		Double[] puCommodityPriceList2 = { 8.59000d };
		purchaseAndWarehousing(puCommodityList2, puCommodityNOList2, puCommodityPriceList2);
		// 第三次采购并入库装商品B10件
		Commodity[] puCommodityList3 = { normalCommodityB };
		Integer[] puCommodityNOList3 = { 8 };
		Double[] puCommodityPriceList3 = { 8.00000d };
		purchaseAndWarehousing(puCommodityList3, puCommodityNOList3, puCommodityPriceList3);
		// 售卖多包装商品
		int[] iSaleCommodityNO = { 20 };
		Commodity[] commoditys = { multiPackageCommodityB };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 退货多包装商品
		int[] iaReturnPromotionID = { 13 };
		Commodity[] returnCommoditys = { multiPackageCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);
		//
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/* 总单数 */, 122.260000d/* 总进货金额 */, 143.780000d/* 总销售额 */, 143.780000d/* 客单价 */, 21.520000d/* 毛利 */, 0.149673d/* 毛利率 */,
				multiPackageCommodityB.getID(), 7/* 畅销商品数量 */, 143.780000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB.getID(), multiPackageCommodityB.getBarcodes(), multiPackageCommodityB.getName(),
				multiPackageCommodityB.getSpecification(), 20/* 商品数量 */, 122.260000d/* 总进货价 */, 143.780000d/* 总销售额 */, 21.520000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);

		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 11)), dailyReportSummary, dailyReportByCommodityList);
	}
}
