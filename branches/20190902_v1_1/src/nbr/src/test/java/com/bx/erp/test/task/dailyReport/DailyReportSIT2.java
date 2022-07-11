package com.bx.erp.test.task.dailyReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.web.WebAppConfiguration;
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

/** 测试目标：当天退货
 * 
 * 2、当天退货（部分退/全退）：单品A，服务型商品B 进行混合售卖，对单品A进行退货，查看日报表是否正常生成。
 * 3、当天退货（部分退/全退）：单品A，组合商品B进行混合售卖，对组合商品B进行退货，查看日报表是否正常生成。
 * 4、当天退货（部分退/全退）：多包装商品A，组合商品B进行混合售卖，对多包装A进行退货，查看日报表是否正常生成。
 * 5、当天退货（部分退/全退）：多包装商品A，服务型商品B进行混合售卖，对服务型商品B进行退货，查看日报表是否正常生成。
 * 
 * 测试步骤： 1、创建商品 创建创建单品A、B B增加两个多包装商品B1、B2 创建组合商品C，子商品为A和B 创建服务型商品D
 * 2、创建采购单，采购商品A和B，审核该入库单 3、创建入库单，入库商品A和B，审核该入库单 4、对各种商品组合进行零售
 * 5、运行夜间任务，生成日报表。 */
@WebAppConfiguration
public class DailyReportSIT2 extends BaseDailyReportSIT {
	private Staff staff;
	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
	public static final int RETURN_OBJECT = 1;
	public static final String SALE_DATE = "2031-04-12";
	public static final String REPORT_DATE = "2031-04-13";
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);

	private Commodity normalCommodityA;
	private Commodity normalCommodityB;
	private Commodity multiPackageCommodityB1;
	private Commodity multiPackageCommodityB2;
	private Commodity subCommodity;
	private Commodity serviceCommodity;

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

			// 创建单品
			normalCommodityA = createNormalCommodity(1.280000d, mvc, sessionBoss);
			// 创建带多包装商品的单品
			Integer[] mulitpleList = { 2, 3 };
			Double[] mulitplePriceList = { 23.130000d, 33.190000d };
			double price = 12.7700000d;
			Commodity commB = BaseCommodityTest.DataInput.getCommodity();
			commB.setPriceRetail(price);
			commB.setPriceVIP(price);
			commB.setPriceWholesale(price);
			String providerIDs = "1,2,3";
			String multiPackagingInfo = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() % 1000000 + ";1," + mulitpleList[0] + "," + mulitpleList[1]
					+ ";1,2,3;4.88," + mulitplePriceList[0] + "," + mulitplePriceList[1] + ";4.88,13.76,19.14;8,8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B2" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodityB = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs, multiPackagingInfo, commB);
			List<BaseModel> commodityList = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodityB);
			multiPackageCommodityB1 = (Commodity) commodityList.get(1);
			multiPackageCommodityB2 = (Commodity) commodityList.get(0);

			// 创建组合商品
			Commodity[] subCommodityList = { normalCommodityA, normalCommodityB };
			Integer[] subCommodityNOList = { 1, 2 };
			Double[] subCommodityPriceList = { 0.390000d, 7.110000d };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 24.590000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity = createServiceCommodity(1.120000d, mvc, sessionBoss);
			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, multiPackageCommodityB1, multiPackageCommodityB2, subCommodity, serviceCommodity });
			// 采购
			Commodity[] puCommodityList = { normalCommodityA, normalCommodityB };
			Integer[] puCommodityNOList = { 2000, 1000 };
			Double[] puCommodityPriceList = { 0.390000d, 7.110000d };
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

	/** 零售(单品与组合商品混合售卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest1() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 44.610000d/*总进货金额*/, 76.330000d/*总销售额*/, 76.330000d/*客单价*/, 31.720000d/*毛利*/, 0.415564d/*毛利率*/, subCommodity.getID(), 3/*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与多包装商品混合售卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest2() throws Exception {
		int[] iSaleCommodityNO = { 4, 2 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 29.220000d/*总进货金额*/, 48.820000d/*总销售额*/, 48.820000d/*客单价*/, 19.600000d/*毛利*/, 0.401475d/*毛利率*/, multiPackageCommodityB1.getID(),
				2/*畅销商品数量*/, 46.260000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 4/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 2/*商品数量*/, 28.440000d/*总进货价*/, 46.260000d/*总销售额*/, 17.820000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与服务商品混卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest3() throws Exception {
		int[] iSaleCommodityNO = { 5, 7 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.780000d/*总进货金额*/, 10.400000d/*总销售额*/, 10.400000d/*客单价*/, 9.620000d/*毛利*/, 0.925000d/*毛利率*/, serviceCommodity.getID(), 7/*畅销商品数量*/,
				7.840000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 5/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 7/*商品数量*/,
				0.000000d/*总进货价*/, 7.840000d/*总销售额*/, 7.840000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 3), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与服务商品混卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest4() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 30.390000d/*总进货金额*/, 53.020000d/*总销售额*/, 53.020000d/*客单价*/, 22.630000d/*毛利*/, 0.426820d/*毛利率*/, normalCommodityA.getID(), 3/*畅销商品数量*/,
				3.840000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与多包装商品售卖，对多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest5() throws Exception {
		int[] iSaleCommodityNO = { 2, 4 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 29.220000d/*总进货金额*/, 48.820000d/*总销售额*/, 48.820000d/*客单价*/, 19.600000d/*毛利*/, 0.401475d/*毛利率*/, multiPackageCommodityB1.getID(),
				2/*畅销商品数量*/, 46.260000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 2/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 4/*商品数量*/, 28.440000d/*总进货价*/, 46.260000d/*总销售额*/, 17.820000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 5), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与服务商品混合售卖，对服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest6() throws Exception {
		int[] iSaleCommodityNO = { 5, 6 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 1.950000d/*总进货金额*/, 10.880000d/*总销售额*/, 10.880000d/*客单价*/, 8.930000d/*毛利*/, 0.820772d/*毛利率*/, normalCommodityA.getID(), 5/*畅销商品数量*/,
				6.400000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 5/*商品数量*/,
				1.950000d/*总进货价*/, 6.400000d/*总销售额*/, 4.450000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 6/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 6), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与组合商品混合售卖，对单品全部当天退货) */
	@Test
	public void DailyReportSITTest7() throws Exception {
		int[] iSaleCommodityNO = { 4, 3 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 43.830000d/*总进货金额*/, 73.770000d/*总销售额*/, 73.770000d/*客单价*/, 29.940000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 3/*畅销商品数量*/,
				73.7700000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.7700000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 7), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与多包装商品混合售卖，对单品全部当天退货) */
	@Test
	public void DailyReportSITTest8() throws Exception {
		int[] iSaleCommodityNO = { 4, 5 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 106.650000d/*总进货金额*/, 165.950000d/*总销售额*/, 165.950000d/*客单价*/, 59.300000d/*毛利*/, 0.357337d/*毛利率*/, multiPackageCommodityB2.getID(),
				5/*畅销商品数量*/, 165.950000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 5/*商品数量*/, 106.650000d/*总进货价*/, 165.950000d/*总销售额*/, 59.300000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与服务商品混合售卖，对单品全部当天退货) */
	@Test
	public void DailyReportSITTest9() throws Exception {
		int[] iSaleCommodityNO = { 5, 2 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 5 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 2.240000d/*总销售额*/, 2.240000d/*客单价*/, 2.240000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 2/*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 9), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与组合商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest10() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 1.170000d/*总进货金额*/, 3.840000d/*总销售额*/, 3.840000d/*客单价*/, 2.670000d/*毛利*/, 0.695313d/*毛利率*/, normalCommodityA.getID(), 3 /*畅销商品数量*/,
				3.840000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 10), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与多包装商品售卖，对多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest11() throws Exception {
		int[] iSaleCommodityNO = { 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.780000d/*总进货金额*/, 2.560000d/*总销售额*/, 2.560000d/*客单价*/, 1.780000d/*毛利*/, 0.695313d/*毛利率*/, normalCommodityA.getID(), 2 /*畅销商品数量*/,
				2.560000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 2/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 11), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品与多包装商品售卖，对多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest12() throws Exception {
		int[] iSaleCommodityNO = { 5, 12 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 12 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 1.950000d/*总进货金额*/, 6.400000d/*总销售额*/, 6.400000d/*客单价*/, 4.450000d/*毛利*/, 0.695313d/*毛利率*/, normalCommodityA.getID(), 5 /*畅销商品数量*/,
				6.400000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 5/*商品数量*/,
				1.950000d/*总进货价*/, 6.400000d/*总销售额*/, 4.450000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 12), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与多包装商品混合售卖，对组合商品部分当天退货) */
	@Test
	public void DailyReportSITTest13() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 71.880000d/*总进货金额*/, 118.570000d/*总销售额*/, 118.570000d/*客单价*/, 46.690000d/*毛利*/, 0.393776d/*毛利率*/, multiPackageCommodityB1.getID(),
				3 /*畅销商品数量*/, 69.390000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 13), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与服务商品混合售卖，对组合商品部分当天退货) */
	@Test
	public void DailyReportSITTest14() throws Exception {
		int[] iSaleCommodityNO = { 4, 2 };
		Commodity[] commoditys = { serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 14.610000d/*总进货金额*/, 29.070000d/*总销售额*/, 29.070000d/*客单价*/, 14.460000d/*毛利*/, 0.497420d/*毛利率*/, serviceCommodity.getID(), 4 /*畅销商品数量*/,
				4.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 4/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 14), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(多包装商品与服务商品混卖，对多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest15() throws Exception {
		int[] iSaleCommodityNO = { 5, 7 };
		Commodity[] commoditys = { multiPackageCommodityB1, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 15), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 15), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 42.660000d/*总进货金额*/, 77.230000d/*总销售额*/, 77.230000d/*客单价*/, 34.570000d/*毛利*/, 0.447624d/*毛利率*/, serviceCommodity.getID(), 7 /*畅销商品数量*/,
				7.840000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 7/*商品数量*/,
				0.000000d/*总进货价*/, 7.840000d/*总销售额*/, 7.840000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 5/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 15), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 15), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与多包装商品混合售卖，对多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest16() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 16), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 16), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 72.270000d/*总进货金额*/, 120.030000d/*总销售额*/, 120.030000d/*客单价*/, 47.760000d/*毛利*/, 0.397901d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 28.440000d/*总进货价*/, 46.260000d/*总销售额*/, 17.820000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 16), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 16), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与服务商品混合售卖，对服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest17() throws Exception {
		int[] iSaleCommodityNO = { 2, 4 };
		Commodity[] commoditys = { serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 17), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 17), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 58.440000d/*总进货金额*/, 99.480000d/*总销售额*/, 99.480000d/*客单价*/, 41.040000d/*毛利*/, 0.412545d/*毛利率*/, subCommodity.getID(), 4 /*畅销商品数量*/,
				98.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				58.440000d/*总进货价*/, 98.360000d/*总销售额*/, 39.920000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 17), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 17), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(多包装商品与服务商品混卖，对服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest18() throws Exception {
		int[] iSaleCommodityNO = { 4, 6 };
		Commodity[] commoditys = { multiPackageCommodityB1, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 56.880000d/*总进货金额*/, 97.000000d/*总销售额*/, 97.000000d/*客单价*/, 40.120000d/*毛利*/, 0.413608d/*毛利率*/, serviceCommodity.getID(), 4 /*畅销商品数量*/,
				4.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 4/*商品数量*/, 56.880000d/*总进货价*/, 92.520000d/*总销售额*/, 35.640000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 6/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 18), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与多包装商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest19() throws Exception {
		int[] iSaleCommodityNO = { 4, 3 };
		Commodity[] commoditys = { multiPackageCommodityB2, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 85.320000d/*总进货金额*/, 132.760000d/*总销售额*/, 132.760000d/*客单价*/, 47.440000d/*毛利*/, 0.357337d/*毛利率*/, multiPackageCommodityB2.getID(),
				4 /*畅销商品数量*/, 132.760000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 4/*商品数量*/, 85.320000d/*总进货价*/, 132.760000d/*总销售额*/, 47.440000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 19), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与服务商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest20() throws Exception {
		int[] iSaleCommodityNO = { 4, 5 };
		Commodity[] commoditys = { serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 20), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 5 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 20), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 4.480000d/*总销售额*/, 4.480000d/*客单价*/, 4.480000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 4 /*畅销商品数量*/,
				4.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 4/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 20), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 20), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(多包装商品与服务商品混卖，对多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest21() throws Exception {
		int[] iSaleCommodityNO = { 5, 2 };
		Commodity[] commoditys = { multiPackageCommodityB2, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 21), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 5 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 21), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 2.240000d/*总销售额*/, 2.240000d/*客单价*/, 2.240000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 21), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 21), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与多包装商品混合售卖，对多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest22() throws Exception {
		int[] iSaleCommodityNO = { 3, 3 };
		Commodity[] commoditys = { multiPackageCommodityB2, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 43.830000d/*总进货金额*/, 73.770000d/*总销售额*/, 73.770000d/*客单价*/, 29.940000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 22), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品与服务商品混合售卖，对服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest23() throws Exception {
		int[] iSaleCommodityNO = { 2, 3 };
		Commodity[] commoditys = { serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 43.830000d/*总进货金额*/, 73.770000d/*总销售额*/, 73.770000d/*客单价*/, 29.940000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 23), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(多包装商品与服务商品混卖，对服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest24() throws Exception {
		int[] iSaleCommodityNO = { 5, 8 };
		Commodity[] commoditys = { multiPackageCommodityB2, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 8 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 106.650000d/*总进货金额*/, 165.950000d/*总销售额*/, 165.950000d/*客单价*/, 59.300000d/*毛利*/, 0.357337d/*毛利率*/, multiPackageCommodityB2.getID(),
				5 /*畅销商品数量*/, 165.950000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 5/*商品数量*/, 106.650000d/*总进货价*/, 165.950000d/*总销售额*/, 59.300000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 24), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 24), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest25() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 25), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 25), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 87.270000d/*总进货金额*/, 145.720000d/*总销售额*/, 145.720000d/*客单价*/, 58.450000d/*毛利*/, 0.401112d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 25), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 25), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(服务型商品、单品、组合商品混合售卖，对组合商品部分当天退货) */
	@Test
	public void DailyReportSITTest26() throws Exception {
		int[] iSaleCommodityNO = { 4, 2, 2 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityB, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 26), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 26), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 28.830000d/*总进货金额*/, 54.610000d/*总销售额*/, 54.610000d/*客单价*/, 25.780000d/*毛利*/, 0.472075d/*毛利率*/, serviceCommodity.getID(), 4 /*畅销商品数量*/,
				4.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 4/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 26), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 26), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest27() throws Exception {
		int[] iSaleCommodityNO = { 5, 3, 7 };
		Commodity[] commoditys = { multiPackageCommodityB1, normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 27), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 27), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 146.100000d/*总进货金额*/, 245.360000d/*总销售额*/, 245.360000d/*客单价*/, 99.260000d/*毛利*/, 0.404548d/*毛利率*/, subCommodity.getID(), 7/*商品数量*/ ,
				172.130000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 5/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 7/*畅销商品数量*/,
				102.270000d/*总进货价*/, 172.130000d/*总销售额*/, 69.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 27), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 27), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对单品、组合商品、多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest28() throws Exception {
		int[] iSaleCommodityNO = { 3, 5, 3 };
		Commodity[] commoditys = { multiPackageCommodityB2, normalCommodityB, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 2, 1 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2, normalCommodityB, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 93.210000d/*总进货金额*/, 153.870000d/*总销售额*/, 153.870000d/*客单价*/, 60.660000d/*毛利*/, 0.394229d/*毛利率*/, normalCommodityB.getID(), 3 /*畅销商品数量*/,
				38.310000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 66.380000d/*总销售额*/, 23.720000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 5/*商品数量*/,
				21.330000d/*总进货价*/, 38.310000d/*总销售额*/, 16.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 28), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 28), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对单品商品全部当天退货) */
	@Test
	public void DailyReportSITTest29() throws Exception {
		int[] iSaleCommodityNO = { 2, 1, 4 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 29), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 29), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 79.770000d/*总进货金额*/, 131.550000d/*总销售额*/, 131.550000d/*客单价*/, 51.780000d/*毛利*/, 0.393615d/*毛利率*/, subCommodity.getID(), 4 /*畅销商品数量*/,
				98.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 1/*商品数量*/, 21.330000d/*总进货价*/, 33.190000d/*总销售额*/, 11.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				58.440000d/*总进货价*/, 98.360000d/*总销售额*/, 39.920000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 29), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 29), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest30() throws Exception {
		int[] iSaleCommodityNO = { 5, 6, 6 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 30), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 6 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 30), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 87.270000d/*总进货金额*/, 145.180000d/*总销售额*/, 145.180000d/*客单价*/, 57.910000d/*毛利*/, 0.398884d/*毛利率*/, multiPackageCommodityB1.getID(),
				6 /*畅销商品数量*/, 138.780000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 1/*商品数量*/,
				6.400000d/*总进货价*/, 4.450000d/*总销售额*/, 11.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 6/*商品数量*/, 85.320000d/*总进货价*/, 138.780000d/*总销售额*/, 53.460000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 30), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 30), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest31() throws Exception {
		int[] iSaleCommodityNO = { 4, 3, 3 };
		Commodity[] commoditys = { multiPackageCommodityB2, normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 31), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 31), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 45.000000d/*总进货金额*/, 77.610000d/*总销售额*/, 77.610000d/*客单价*/, 32.610000d/*毛利*/, 0.420178d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 31), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 31), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与多包装商品混合售卖，对单品、组合商品、多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest32() throws Exception { // 由于毛利率为0，所以不会生成报表
		int[] iSaleCommodityNO = { 3, 5, 3 };
		Commodity[] commoditys = { multiPackageCommodityB1, normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 32), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3, 5, 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1, normalCommodityA, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 32), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 0.000000d/*总销售额*/, 0.000000d/*客单价*/, 0.000000d/*毛利*/, 0.000000d/*毛利率*/, 0, 0 /*畅销商品数量*/, 0.000000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 32), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 32), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对单品部分当天退货) */
	@Test
	public void DailyReportSITTest33() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 3 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 33), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 33), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 44.220000d/*总进货金额*/, 78.410000d/*总销售额*/, 78.410000d/*客单价*/, 34.190000d/*毛利*/, 0.436041d/*毛利率*/, serviceCommodity.getID(), 3 /*畅销商品数量*/,
				3.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				0.390000d/*总进货价*/, 1.280000d/*总销售额*/, 0.890000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 3.360000d/*总销售额*/, 3.360000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 33), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 33), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对组合商品部分当天退货) */
	@Test
	public void DailyReportSITTest34() throws Exception {
		int[] iSaleCommodityNO = { 4, 2, 4 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityB, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 34), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 34), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 28.830000d/*总进货金额*/, 54.610000d/*总销售额*/, 54.610000d/*客单价*/, 25.780000d/*毛利*/, 0.472075d/*毛利率*/, serviceCommodity.getID(), 4 /*畅销商品数量*/,
				4.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 4/*商品数量*/,
				0.000000d/*总进货价*/, 4.480000d/*总销售额*/, 4.480000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 34), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 34), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest35() throws Exception {
		int[] iSaleCommodityNO = { 5, 3, 7 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 35), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 35), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 103.440000d/*总进货金额*/, 177.090000d/*总销售额*/, 177.090000d/*客单价*/, 73.650000d/*毛利*/, 0.415890d/*毛利率*/, subCommodity.getID(), 7 /*畅销商品数量*/,
				172.130000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 5/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 7/*商品数量*/,
				102.270000d/*总进货价*/, 172.130000d/*总销售额*/, 69.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 35), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 35), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对单品、组合商品、服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest36() throws Exception {
		int[] iSaleCommodityNO = { 3, 5, 3 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityB, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 36), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 2, 2 };
		Commodity[] returnCommoditys = { serviceCommodity, normalCommodityB, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 36), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 35.940000d/*总进货金额*/, 65.140000d/*总销售额*/, 65.140000d/*客单价*/, 29.200000d/*毛利*/, 0.448265d/*毛利率*/, normalCommodityB.getID(), 3 /*畅销商品数量*/,
				38.310000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 5/*商品数量*/,
				21.330000d/*总进货价*/, 38.310000d/*总销售额*/, 16.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 36), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 36), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对单品商品全部当天退货) */
	@Test
	public void DailyReportSITTest37() throws Exception {
		int[] iSaleCommodityNO = { 5, 1, 4 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 37), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 5 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 37), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 58.440000d/*总进货金额*/, 99.480000d/*总销售额*/, 99.480000d/*客单价*/, 41.040000d/*毛利*/, 0.412545d/*毛利率*/, subCommodity.getID(), 4 /*畅销商品数量*/,
				98.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				56.490000d/*总进货价*/, 98.360000d/*总销售额*/, 41.870000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 37), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 37), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest38() throws Exception {
		int[] iSaleCommodityNO = { 5, 2, 6 };
		Commodity[] commoditys = { normalCommodityB, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 38), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 6 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 38), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 35.550000d/*总进货金额*/, 66.090000d/*总销售额*/, 66.090000d/*客单价*/, 30.540000d/*毛利*/, 0.462097d/*毛利率*/, normalCommodityB.getID(), 5 /*畅销商品数量*/,
				63.850000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 5/*商品数量*/,
				35.550000d/*总进货价*/, 63.850000d/*总销售额*/, 28.300000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 38), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 38), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest39() throws Exception {
		int[] iSaleCommodityNO = { 2, 3, 3 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 39), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 39), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 45.000000d/*总进货金额*/, 77.610000d/*总销售额*/, 77.610000d/*客单价*/, 32.610000d/*毛利*/, 0.420178d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 39), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 39), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品与服务商品混合售卖，对服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest40() throws Exception { // 由于毛利率为0，所以不会生成报表
		int[] iSaleCommodityNO = { 3, 2, 3 };
		Commodity[] commoditys = { serviceCommodity, normalCommodityB, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 40), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3, 2, 3 };
		Commodity[] returnCommoditys = { serviceCommodity, normalCommodityB, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 40), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 0.000000d/*总销售额*/, 0.000000d/*客单价*/, 0.000000d/*毛利*/, 0.000000d/*毛利率*/, 0, 0 /*畅销商品数量*/, 0.000000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 40), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 40), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对组合商品部分当天退货) */
	@Test
	public void DailyReportSITTest41() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 3 };
		Commodity[] commoditys = { multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 41), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 41), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 57.270000d/*总进货金额*/, 97.340000d/*总销售额*/, 97.340000d/*客单价*/, 40.070000d/*毛利*/, 0.411650d/*毛利率*/, serviceCommodity.getID(), 3 /*畅销商品数量*/,
				3.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 3.360000d/*总销售额*/, 3.360000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 41), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 41), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest42() throws Exception {
		int[] iSaleCommodityNO = { 5, 2, 4 };
		Commodity[] commoditys = { multiPackageCommodityB2, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 42), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 42), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 101.100000d/*总进货金额*/, 166.980000d/*总销售额*/, 166.980000d/*客单价*/, 65.880000d/*毛利*/, 0.394538d/*毛利率*/, subCommodity.getID(), 4 /*畅销商品数量*/,
				98.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 5/*商品数量*/, 42.660000d/*总进货价*/, 66.380000d/*总销售额*/, 23.720000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				58.440000d/*总进货价*/, 98.360000d/*总销售额*/, 39.920000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 42), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 42), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest43() throws Exception {
		int[] iSaleCommodityNO = { 5, 3, 7 };
		Commodity[] commoditys = { serviceCommodity, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 43), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 43), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 144.930000d/*总进货金额*/, 242.640000d/*总销售额*/, 242.640000d/*客单价*/, 97.710000d/*毛利*/, 0.402695d/*毛利率*/, subCommodity.getID(), 7 /*畅销商品数量*/,
				172.130000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 5/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 7/*商品数量*/,
				102.270000d/*总进货价*/, 172.130000d/*总销售额*/, 69.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 43), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 43), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对组合商品、多包装、服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest44() throws Exception {
		int[] iSaleCommodityNO = { 3, 5, 3 };
		Commodity[] commoditys = { serviceCommodity, multiPackageCommodityB2, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 44), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 2, 2 };
		Commodity[] returnCommoditys = { serviceCommodity, multiPackageCommodityB2, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 44), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 78.600000d/*总进货金额*/, 126.400000d/*总销售额*/, 126.400000d/*客单价*/, 47.800000d/*毛利*/, 0.378165d/*毛利率*/, multiPackageCommodityB2.getID(),
				3 /*畅销商品数量*/, 99.570000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 5/*商品数量*/, 63.990000d/*总进货价*/, 99.570000d/*总销售额*/, 35.580000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 44), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 44), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest45() throws Exception {
		int[] iSaleCommodityNO = { 2, 1, 4 };
		Commodity[] commoditys = { multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 45), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 45), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 28.440000d/*总进货金额*/, 47.380000d/*总销售额*/, 47.380000d/*客单价*/, 18.940000d/*毛利*/, 0.399747d/*毛利率*/, multiPackageCommodityB1.getID(),
				2 /*畅销商品数量*/, 46.260000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 2/*商品数量*/, 28.440000d/*总进货价*/, 46.260000d/*总销售额*/, 17.820000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 45), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 45), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对组合商品全部当天退货) */
	@Test
	public void DailyReportSITTest46() throws Exception {
		int[] iSaleCommodityNO = { 5, 2, 4 };
		Commodity[] commoditys = { multiPackageCommodityB2, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 46), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 5 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 46), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 58.440000d/*总进货金额*/, 100.600000d/*总销售额*/, 100.600000d/*客单价*/, 42.160000d/*毛利*/, 0.419085d/*毛利率*/, subCommodity.getID(), 4 /*畅销商品数量*/,
				98.360000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				58.440000d/*总进货价*/, 98.360000d/*总销售额*/, 39.920000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 46), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 46), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest47() throws Exception {
		int[] iSaleCommodityNO = { 2, 3, 3 };
		Commodity[] commoditys = { serviceCommodity, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 47), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 47), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 86.490000d/*总进货金额*/, 143.160000d/*总销售额*/, 143.160000d/*客单价*/, 56.670000d/*毛利*/, 0.395851d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 47), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 47), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(组合商品、多包装商品与服务商品混合售卖，对组合商品、多包装商品、服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest48() throws Exception { // 由于毛利率为0，所以不会生成报表
		int[] iSaleCommodityNO = { 3, 2, 3 };
		Commodity[] commoditys = { serviceCommodity, multiPackageCommodityB2, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 48), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3, 2, 3 };
		Commodity[] returnCommoditys = { serviceCommodity, multiPackageCommodityB2, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 48), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 0.000000d/*总销售额*/, 0.000000d/*客单价*/, 0.000000d/*毛利*/, 0.000000d/*毛利率*/, 0, 0 /*畅销商品数量*/, 0.000000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 48), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 48), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对单品、组合商品、多包装商品部分当天退货) */
	@Test
	public void DailyReportSITTest49() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 49), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 2, 1 };
		Commodity[] returnCommoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 49), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 44.220000d/*总进货金额*/, 77.110000d/*总销售额*/, 77.110000d/*客单价*/, 32.890000d/*毛利*/, 0.426534d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 14.220000d/*总进货价*/, 23.130000d/*总销售额*/, 8.910000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity4);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 49), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 49), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对组合商品、多包装商品、服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest50() throws Exception {
		int[] iSaleCommodityNO = { 3, 2, 2, 3 };
		Commodity[] commoditys = { normalCommodityB, multiPackageCommodityB2, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 50), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 1, 1 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2, serviceCommodity, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 50), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 71.880000d/*总进货金额*/, 121.800000d/*总销售额*/, 121.800000d/*客单价*/, 49.920000d/*毛利*/, 0.409852d/*毛利率*/, normalCommodityB.getID(), 3 /*畅销商品数量*/,
				38.310000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 3/*商品数量*/,
				21.330000d/*总进货价*/, 38.310000d/*总销售额*/, 16.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 2/*商品数量*/, 21.330000d/*总进货价*/, 33.190000d/*总销售额*/, 11.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity4);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 50), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 50), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对单品、组合商品、多包装商品、服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest51() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 51), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 1, 2, 1, 1 };
		Commodity[] returnCommoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 51), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 44.220000d/*总进货金额*/, 75.990000d/*总销售额*/, 75.990000d/*客单价*/, 31.770000d/*毛利*/, 0.418081d/*毛利率*/, subCommodity.getID(), 2 /*畅销商品数量*/,
				49.180000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 3/*商品数量*/,
				0.780000d/*总进货价*/, 2.560000d/*总销售额*/, 1.780000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 14.220000d/*总进货价*/, 23.130000d/*总销售额*/, 8.910000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity4);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 51), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 51), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对单品、组合商品、多包装商品全部当天退货) */
	@Test
	public void DailyReportSITTest52() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 52), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3, 3, 3 };
		Commodity[] returnCommoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 52), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 2.240000d/*总销售额*/, 2.240000d/*客单价*/, 2.240000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 52), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 52), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对组合商品、多包装商品、服务商品部分当天退货) */
	@Test
	public void DailyReportSITTest53() throws Exception {
		int[] iSaleCommodityNO = { 3, 2, 2, 3 };
		Commodity[] commoditys = { normalCommodityB, multiPackageCommodityB2, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 53), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 2, 2, 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2, serviceCommodity, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 53), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 21.330000d/*总进货金额*/, 38.310000d/*总销售额*/, 38.310000d/*客单价*/, 16.980000d/*毛利*/, 0.443226d/*毛利率*/, normalCommodityB.getID(), 3 /*畅销商品数量*/,
				38.310000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 3/*商品数量*/, 21.330000d/*总进货价*/, 38.310000d/*总销售额*/, 16.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 53), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 53), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(单品、组合商品、多包装商品与服务商品混合售卖，对单品、组合商品、多包装商品、服务商品全部当天退货) */
	@Test
	public void DailyReportSITTest54() throws Exception {
		int[] iSaleCommodityNO = { 3, 3, 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 54), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnSaleCommodityNO = { 3, 3, 2, 3 };
		Commodity[] returnCommoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 54), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(1/*总单数*/, 0.000000d/*总进货金额*/, 0.000000d/*总销售额*/, 0.000000d/*客单价*/, 0.000000d/*毛利*/, 0.000000d/*毛利率*/, 0, 0 /*畅销商品数量*/, 0.000000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 54), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 54), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和组合商品(形成2个零售单)，对单品进行部分退货) */
	@Test
	public void DailyReportSITTest55() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 55), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 55), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 55), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 45.000000d/*总进货金额*/, 77.610000d/*总销售额*/, 38.805000d/*客单价*/, 32.610000d/*毛利*/, 0.420178d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 4/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 55), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 55), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和组合商品(形成2个零售单)，对组合进行部分退货) */
	@Test
	public void DailyReportSITTest56() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 56), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 56), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 56), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 43.440000d/*总进货金额*/, 74.720000d/*总销售额*/, 37.360000d/*客单价*/, 31.280000d/*毛利*/, 0.418630d/*毛利率*/, subCommodity.getID(), 2 /*畅销商品数量*/,
				49.180000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 56), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 56), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和多包装商品(形成2个零售单)，对单品进行部分退货) */
	@Test
	public void DailyReportSITTest57() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 57), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 4 };
		Commodity[] commoditys2 = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 57), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 57), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 58.050000d/*总进货金额*/, 96.360000d/*总销售额*/, 48.180000d/*客单价*/, 38.31000d/*毛利*/, 0.397572d/*毛利率*/, multiPackageCommodityB1.getID(),
				4 /*畅销商品数量*/, 92.520000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 4/*商品数量*/,
				1.170000d/*总进货价*/, 3.840000d/*总销售额*/, 2.670000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 4/*商品数量*/, 56.880000d/*总进货价*/, 92.520000d/*总销售额*/, 35.640000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 57), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 57), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和多包装商品(形成2个零售单)，对多包装商品进行部分退货) */
	@Test
	public void DailyReportSITTest58() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 58), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 58), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 58), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 56.880000d/*总进货金额*/, 91.920000d/*总销售额*/, 45.960000d/*客单价*/, 35.04000d/*毛利*/, 0.381201d/*毛利率*/, multiPackageCommodityB2.getID(),
				2 /*畅销商品数量*/, 66.380000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 66.380000d/*总销售额*/, 23.720000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 58), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 58), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和服务商品(形成2个零售单)，对单品进行部分退货) */
	@Test
	public void DailyReportSITTest59() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 59), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 59), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 59), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 0.390000d/*总进货金额*/, 2.400000d/*总销售额*/, 1.200000d/*客单价*/, 2.010000d/*毛利*/, 0.837500d/*毛利率*/, serviceCommodity.getID(), 1 /*畅销商品数量*/,
				1.120000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 4/*商品数量*/,
				0.390000d/*总进货价*/, 1.280000d/*总销售额*/, 0.890000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 59), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 59), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和服务商品(形成2个零售单)，对服务商品进行部分退货) */
	@Test
	public void DailyReportSITTest60() throws Exception {
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 60), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 60), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 60), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 7.110000d/*总进货金额*/, 15.010000d/*总销售额*/, 7.505000d/*客单价*/, 7.900000d/*毛利*/, 0.526316d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 1/*商品数量*/,
				7.110000d/*总进货价*/, 12.770000d/*总销售额*/, 5.660000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 60), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 60), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和组合商品(形成2个零售单)，对单品进行全部退货) */
	@Test
	public void DailyReportSITTest61() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 61), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 61), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 4 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 61), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 43.830000d/*总进货金额*/, 73.770000d/*总销售额*/, 36.885000d/*客单价*/, 29.940000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 3/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 61), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 61), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和组合商品(形成2个零售单)，对组合进行全部退货) */
	@Test
	public void DailyReportSITTest62() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 62), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 62), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 62), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.220000d/*总进货金额*/, 25.540000d/*总销售额*/, 12.770000d/*客单价*/, 11.320000d/*毛利*/, 0.443226d/*毛利率*/, normalCommodityB.getID(), 2 /*畅销商品数量*/,
				25.540000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 62), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 62), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和多包装商品(形成2个零售单)，对单品进行部分退货)) */
	@Test
	public void DailyReportSITTest63() throws Exception {
		int[] iSaleCommodityNO = { 3 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 63), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 2 };
		Commodity[] commoditys2 = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 63), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 63), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 28.440000d/*总进货金额*/, 46.260000d/*总销售额*/, 23.130000d/*客单价*/, 17.820000d/*毛利*/, 0.385214d/*毛利率*/, multiPackageCommodityB1.getID(),
				2 /*畅销商品数量*/, 46.260000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 2/*商品数量*/, 28.440000d/*总进货价*/, 46.260000d/*总销售额*/, 17.820000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 63), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 63), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和多包装商品(形成2个零售单)，对单品进行部分退货)) */
	@Test
	public void DailyReportSITTest64() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 64), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 64), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 64), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.220000d/*总进货金额*/, 25.540000d/*总销售额*/, 12.770000d/*客单价*/, 11.320000d/*毛利*/, 0.443226d/*毛利率*/, normalCommodityB.getID(), 2 /*畅销商品数量*/,
				25.540000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 2/*商品数量*/,
				14.220000d/*总进货价*/, 25.540000d/*总销售额*/, 11.320000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 64), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 64), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和多包装商品(形成2个零售单)，对单品进行部分退货)) */
	@Test
	public void DailyReportSITTest65() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 65), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 65), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 65), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 0.000000d/*总进货金额*/, 1.120000d/*总销售额*/, 0.560000d/*客单价*/, 1.120000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 1 /*畅销商品数量*/,
				1.120000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 65), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 65), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖单品和服务商品(形成2个零售单)，对服务商品进行全部退货) */
	@Test
	public void DailyReportSITTest66() throws Exception {
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 66), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 66), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 66), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 7.110000d/*总进货金额*/, 12.770000d/*总销售额*/, 6.385000d/*客单价*/, 5.660000d/*毛利*/, 0.443226d/*毛利率*/, normalCommodityB.getID(), 1 /*畅销商品数量*/,
				12.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 1/*商品数量*/,
				7.110000d/*总进货价*/, 12.770000d/*总销售额*/, 5.660000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 66), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 66), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和多包装商品(形成2个零售单)，对组合商品进行部分退货) */
	@Test
	public void DailyReportSITTest67() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 67), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 67), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 67), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 86.490000d/*总进货金额*/, 143.160000d/*总销售额*/, 71.580000d/*客单价*/, 56.670000d/*毛利*/, 0.395851d/*毛利率*/, subCommodity.getID(), 3 /*畅销商品数量*/,
				73.770000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 4/*商品数量*/,
				43.830000d/*总进货价*/, 73.770000d/*总销售额*/, 29.940000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 69.390000d/*总销售额*/, 26.730000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 67), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 67), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和多包装商品(形成2个零售单)，对多包装商品进行部分退货) */
	@Test
	public void DailyReportSITTest68() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 68), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 68), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 68), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 71.880000d/*总进货金额*/, 115.560000d/*总销售额*/, 57.780000d/*客单价*/, 43.680000d/*毛利*/, 0.377985d/*毛利率*/, subCommodity.getID(), 2 /*畅销商品数量*/,
				49.180000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 3/*商品数量*/, 42.660000d/*总进货价*/, 66.380000d/*总销售额*/, 23.720000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 68), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 68), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和服务商品(形成2个零售单)，对组合商品进行部分退货) */
	@Test
	public void DailyReportSITTest69() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 69), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 2 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 69), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 69), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.610000d/*总进货金额*/, 26.830000d/*总销售额*/, 13.415000d/*客单价*/, 12.220000d/*毛利*/, 0.455460d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 69), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 69), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和服务商品(形成2个零售单)，对服务商品进行部分退货) */
	@Test
	public void DailyReportSITTest70() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 70), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 70), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 70), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 29.220000d/*总进货金额*/, 51.420000d/*总销售额*/, 25.710000d/*客单价*/, 22.200000d/*毛利*/, 0.431739d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 2/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 70), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 70), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖多包装商品和服务商品(形成2个零售单)，对多包装商品进行部分退货) */
	@Test
	public void DailyReportSITTest71() throws Exception {
		int[] iSaleCommodityNO = { 4 };
		Commodity[] commoditys = { multiPackageCommodityB1 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 71), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 71), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 71), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.220000d/*总进货金额*/, 24.250000d/*总销售额*/, 12.125000d/*客单价*/, 10.030000d/*毛利*/, 0.413608d/*毛利率*/, serviceCommodity.getID(), 1 /*畅销商品数量*/,
				1.120000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 4/*商品数量*/, 14.220000d/*总进货价*/, 23.130000d/*总销售额*/, 8.910000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 71), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 71), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖多包装商品和服务商品(形成2个零售单)，对服务商品进行部分退货) */
	@Test
	public void DailyReportSITTest72() throws Exception {
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 72), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 72), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 72), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 21.330000d/*总进货金额*/, 35.430000d/*总销售额*/, 17.715000d/*客单价*/, 14.100000d/*毛利*/, 0.397968d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 1/*商品数量*/, 21.330000d/*总进货价*/, 33.190000d/*总销售额*/, 11.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 3/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 72), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 72), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和多包装商品(形成2个零售单)，对组合商品进行全部退货) */
	@Test
	public void DailyReportSITTest73() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 73), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 73), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 73), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.220000d/*总进货金额*/, 23.130000d/*总销售额*/, 11.565000d/*客单价*/, 8.910000d/*毛利*/, 0.385214d/*毛利率*/, multiPackageCommodityB1.getID(),
				1 /*畅销商品数量*/, 23.130000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 1/*商品数量*/, 14.220000d/*总进货价*/, 23.130000d/*总销售额*/, 8.910000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 73), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 73), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和多包装商品(形成2个零售单)，对多包装商品进行全部退货) */
	@Test
	public void DailyReportSITTest74() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 74), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 74), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 74), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 29.220000d/*总进货金额*/, 49.180000d/*总销售额*/, 24.590000d/*客单价*/, 19.960000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 2 /*畅销商品数量*/,
				49.180000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 1/*商品数量*/,
				29.220000d/*总进货价*/, 49.180000d/*总销售额*/, 19.960000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 74), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 74), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和服务商品(形成2个零售单)，对组合商品进行全部退货) */
	@Test
	public void DailyReportSITTest75() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 75), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 2 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 75), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 75), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 0.000000d/*总进货金额*/, 2.240000d/*总销售额*/, 1.120000d/*客单价*/, 2.240000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 2 /*畅销商品数量*/,
				2.240000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 2/*商品数量*/,
				0.000000d/*总进货价*/, 2.240000d/*总销售额*/, 2.240000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 75), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 75), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖组合商品和服务商品(形成2个零售单)，对服务商品进行全部退货) */
	@Test
	public void DailyReportSITTest76() throws Exception {
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 76), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 2 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 76), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 76), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 14.610000d/*总进货金额*/, 24.590000d/*总销售额*/, 12.295000d/*客单价*/, 9.980000d/*毛利*/, 0.405856d/*毛利率*/, subCommodity.getID(), 1 /*畅销商品数量*/,
				24.590000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 1/*商品数量*/,
				14.610000d/*总进货价*/, 24.590000d/*总销售额*/, 9.980000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 76), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 76), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖多包装商品和服务商品(形成2个零售单)，对多包装商品进行全部退货) */
	@Test
	public void DailyReportSITTest77() throws Exception {
		int[] iSaleCommodityNO = { 2 };
		Commodity[] commoditys = { multiPackageCommodityB1 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 77), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 1 };
		Commodity[] commoditys2 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 77), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 77), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 0.000000d/*总进货金额*/, 1.120000d/*总销售额*/, 0.560000d/*客单价*/, 1.120000d/*毛利*/, 1.000000d/*毛利率*/, serviceCommodity.getID(), 1 /*畅销商品数量*/,
				1.120000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 1/*商品数量*/,
				0.000000d/*总进货价*/, 1.120000d/*总销售额*/, 1.120000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 77), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 77), dailyReportSummary, dailyReportByCommodityList);
	}

	/** 零售(售卖多包装商品和服务商品(形成2个零售单)，对多包装商品进行全部退货) */
	@Test
	public void DailyReportSITTest78() throws Exception {
		int[] iSaleCommodityNO = { 1 };
		Commodity[] commoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 78), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iSaleCommodityNO2 = { 3 };
		Commodity[] commoditys2 = { serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 78), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);

		int[] iReturnSaleCommodityNO = { 3 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 78), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/*总单数*/, 21.330000d/*总进货金额*/, 33.190000d/*总销售额*/, 16.595000d/*客单价*/, 11.860000d/*毛利*/, 0.357337d/*毛利率*/, multiPackageCommodityB2.getID(),
				1 /*畅销商品数量*/, 33.190000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 1/*商品数量*/, 21.330000d/*总进货价*/, 33.190000d/*总销售额*/, 11.860000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 78), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 78), dailyReportSummary, dailyReportByCommodityList);
	}
}
