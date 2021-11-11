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

/** 测试目标： 隔天退货
 * 
 * 1.隔天退货（部分退/全退）：单品A，多包装商品B进行售卖，隔天对多包装商品B进行退货，查看日报表是否正常生成。
 * 2.隔天退货（部分退/全退）：单品A，组合商品B进行售卖，隔天对单品A进行退货，查看日报表是否正常生成。
 * 3.隔天退货（部分退/全退）：多包装商品A，组合商品B进行售卖，隔天对组合商品B进行退货,查看日报表是否正常生成。
 * 4.隔天退货（部分退/全退）：服务商品A，组合商品B进行售卖，隔天对服务商品A进行退货,查看日报表是否正常生成。 ....
 * 
 * 测试步骤： 1、创建商品 创建创建单品A、B; 单品B增加两个多包装商品D1、D2; 创建组合商品C，子商品为A和B 创建服务型商品F
 * 2、创建采购单，采购商品A和B，审核该采购单 3、创建入库单，入库商品A和B，审核该入库单 4、今天对各种商品组合进行零售 5、隔天对商品进行退货
 * 6、运行夜间任务，生成日报表。 */
@WebAppConfiguration
public class DailyReportSIT3 extends BaseDailyReportSIT {
	private Staff staff;

	public static final String SALE_DATE = "2032-3-06";
	public static final String RETURN_DATE = "2032-3-07";
	public static final String REPORT_DATE = "2032-3-08";
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
			normalCommodityA = createNormalCommodity(10.230000d, mvc, sessionBoss);
			// 创建带多包装商品的单品
			Integer[] mulitpleList = { 10, 12 };
			Double[] mulitplePriceList = { 202.300000d, 242.760000d };
			//
			double price = 20.230000d;
			Commodity commB = BaseCommodityTest.DataInput.getCommodity();
			commB.setPriceRetail(price);
			commB.setPriceVIP(price);
			commB.setPriceWholesale(price);
			String providerIDs = "1,2,3";
			String multiPackagingInfo = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() % 1000000 + ";1,2,3;1," + mulitpleList[0] + ","
					+ mulitpleList[1] + ";4.88," + mulitplePriceList[0] + "," + mulitplePriceList[1] + ";4.88,13.76,19.14;8,8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B2" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodityB = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs, multiPackagingInfo, commB);
			List<BaseModel> commodityList = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodityB);
			Assert.assertTrue(commodityList.size() == 2, "根据普通商品B查出的多包装商品数量不等于2，不足以接下来的测试！");
			multiPackageCommodityB1 = (Commodity) commodityList.get(1);
			multiPackageCommodityB2 = (Commodity) commodityList.get(0);
			// 创建组合商品
			Commodity[] subCommodityList = { normalCommodityA, normalCommodityB };
			Integer[] subCommodityNOList = { 2, 3 };
			Double[] subCommodityPriceList = { normalCommodityA.getPriceRetail(), normalCommodityB.getPriceRetail() };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 81.150000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity = createServiceCommodity(2.000000d, mvc, sessionBoss);
			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, multiPackageCommodityB1, multiPackageCommodityB2, subCommodity, serviceCommodity });
			// 采购
			Commodity[] puCommodityList = { normalCommodityA, normalCommodityB };
			Integer[] puCommodityNOList = { 500, 500 };
			Double[] puCommodityPriceList = { 9.230000d, 19.230000d };
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

	/** 零售(单品与多包装商品混合售卖，对多包装商品部分隔天退货) */
	@Test
	public void dailyReportSITTest1() throws Exception {
		int[] iSaleCommodityNO = { 2, 3 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 0), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -461.52d/* 总进货金额 */, -485.52d/* 总销售额 */, 0d/* 客单价 */, -24d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */, 0d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 0)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(单品与组合商品混合售卖，对普通商品全部隔天退货) */
	@Test
	public void dailyReportSITTest2() throws Exception {
		int[] iSaleCommodityNO = { 2, 3 };
		Commodity[] commoditys = { normalCommodityA, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 1), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -18.460000d/* 总进货金额 */, -20.460000d/* 总销售额 */, 0.000000d/* 客单价 */, -2.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 1)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(多包装与组合商品混合售卖，对组合商品部分隔天退货) */
	@Test
	public void dailyReportSITTest3() throws Exception {
		int[] iSaleCommodityNO = { 5, 6 };
		Commodity[] commoditys = { multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 2), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -152.300000d/* 总进货金额 */, -162.300000d/* 总销售额 */, 0.000000d/* 客单价 */, -10.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 2)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(服务商品与组合商品混合售卖，对服务商品全部隔天退货) */
	@Test
	public void dailyReportSITTest4() throws Exception {
		int[] iSaleCommodityNO = { 2, 3 };
		Commodity[] commoditys = { serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 3), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, 0.000000d/* 总进货金额 */, -4.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -4.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 3)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 3)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品与多包装商品2混合售卖，对多包装商品2部分隔天退货) */
	@Test
	public void dailyReportSITTest5() throws Exception {
		int[] iSaleCommodityNO = { 45, 5 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 3 };
		Commodity[] returnCommoditys = { multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 4), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -692.280000d/* 总进货金额 */, -728.280000d/* 总销售额 */, 0.000000d/* 客单价 */, -36.000000d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 4)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品与组合商品混合售卖，对普通商品部分隔天退货) */
	@Test
	public void dailyReportSITTest6() throws Exception {
		int[] iSaleCommodityNO = { 24, 2 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 5), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -92.300000d/* 总进货金额 */, -102.300000d/* 总销售额 */, 0.000000d/* 客单价 */, -10.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 5)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 5)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品与组合商品,多包装商品1混合售卖，对组合商品全部隔天退货) */
	@Test
	public void dailyReportSITTest7() throws Exception {
		int[] iSaleCommodityNO = { 15, 12, 1 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 1 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 6), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -76.150000d/* 总进货金额 */, -81.150000d/* 总销售额 */, 0.000000d/* 客单价 */, -5.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 6)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 6)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品与组合商品,服务商品混合售卖，对服务商品部分隔天退货) */
	@Test
	public void dailyReportSITTest8() throws Exception {
		int[] iSaleCommodityNO = { 12, 2, 1 };
		Commodity[] commoditys = { normalCommodityA, serviceCommodity, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 1 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 7), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, 0.000000d/* 总进货金额 */, -2.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -2.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 7)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 7)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品与组合商品,多包装商品混合售卖，对多包装商品部分隔天退货) */
	@Test
	public void dailyReportSITTest9() throws Exception {
		int[] iSaleCommodityNO = { 25, 3, 4 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 2 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 8), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -384.600000d/* 总进货金额 */, -404.600000d/* 总销售额 */, 0.000000d/* 客单价 */, -20.000000d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 8)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A，B与服务商品,多包装商品混合售卖，对普通商品A部分隔天退货) */
	@Test
	public void dailyReportSITTest10() throws Exception {
		int[] iSaleCommodityNO = { 12, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityB, normalCommodityA, multiPackageCommodityB1, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 5 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 9), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -46.150000d/* 总进货金额 */, -51.150000d/* 总销售额 */, 0.000000d/* 客单价 */, -5.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 9)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 9)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A,B与服务商品,组合商品混合售卖，对组合商品部分隔天退货) */
	@Test
	public void dailyReportSITTest11() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityB, normalCommodityA, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 5 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 10), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -380.750000d/* 总进货金额 */, -405.750000d/* 总销售额 */, 0.000000d/* 客单价 */, -25.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 10)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 10)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品B与服务商品,组合商品，多包装商品2混合售卖，对服务商品全部隔天退货) */
	@Test
	public void dailyReportSITTest12() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityB, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 11), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, 0.000000d/* 总进货金额 */, -20.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -20.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 11)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 11)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品B,A与多包装商品1,组合商品混合售卖，对多包装商品全部隔天退货) */
	@Test
	public void dailyReportSITTest13() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityB, normalCommodityA, multiPackageCommodityB1, subCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 12), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -1923.000000d/* 总进货金额 */, -2023.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -100.000000d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 12)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 12)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A与多包装商品1,服务商品混合售卖，对普通商品全部隔天退货) */
	@Test
	public void dailyReportSITTest14() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 13), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -92.300000d/* 总进货金额 */, -102.300000d/* 总销售额 */, 0.000000d/* 客单价 */, -10.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 13)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 13)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A与组合商品,服务商品混合售卖，对组合商品全部隔天退货) */
	@Test
	public void dailyReportSITTest15() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 14), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -761.500000d/* 总进货金额 */, -811.500000d/* 总销售额 */, 0.000000d/* 客单价 */, -50.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 14)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 14)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(多包装商品1与组合商品,服务商品混合售卖，对服务商品全部隔天退货) */
	@Test
	public void dailyReportSITTest16() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10 };
		Commodity[] commoditys = { multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 15), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 15), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -0.000000d/* 总进货金额 */, -20.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -20.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 15)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 15)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对组合商品全部隔天退货) */
	@Test
	public void dailyReportSITTest17() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 16), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 16), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -761.500000d/* 总进货金额 */, -811.500000d/* 总销售额 */, 0.000000d/* 客单价 */, -50.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 16)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 16)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对服务商品全部隔天退货) */
	@Test
	public void dailyReportSITTest18() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 17), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 17), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, 0.000000d/* 总进货金额 */, -20.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -20.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 17)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 17)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对普通商品全部隔天退货) */
	@Test
	public void dailyReportSITTest19() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 18), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 18), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -92.300000d/* 总进货金额 */, -102.300000d/* 总销售额 */, 0.000000d/* 客单价 */, -10.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 18)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 18)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对多包装商品全部隔天退货) */
	@Test
	public void dailyReportSITTest20() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 19), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 19), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -1923.000000d/* 总进货金额 */, -2023.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -100.000000d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 19)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 19)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对组合商品全部隔天退货) */
	@Test
	public void dailyReportSITTest21() throws Exception {
		int[] iSaleCommodityNO = { 10, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 20), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 20), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -761.500000d/* 总进货金额 */, -811.500000d/* 总销售额 */, 0.000000d/* 客单价 */, -50.000000d/* 毛利 */, 0.061614d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 20)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 20)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对服务商品全部隔天退货) */
	@Test
	public void dailyReportSITTest22() throws Exception {
		int[] iSaleCommodityNO = { 20, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 21), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 21), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -0.000000d/* 总进货金额 */, -20.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -20.000000d/* 毛利 */, 1.000000d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 21)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 21)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对普通商品部分隔天退货) */
	@Test
	public void dailyReportSITTest23() throws Exception {
		int[] iSaleCommodityNO = { 20, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 22), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 22), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -92.300000d/* 总进货金额 */, -102.300000d/* 总销售额 */, 0.000000d/* 客单价 */, -10.000000d/* 毛利 */, 0.097752d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 22)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 22)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	/** 零售(普通商品A多包装商品1与组合商品,服务商品混合售卖，对多包装商品全部隔天退货) */
	@Test
	public void dailyReportSITTest24() throws Exception {
		int[] iSaleCommodityNO = { 20, 10, 10, 10 };
		Commodity[] commoditys = { normalCommodityA, multiPackageCommodityB1, subCommodity, serviceCommodity };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 23), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);

		int[] iReturnCommodityNO = { 10 };
		Commodity[] returnCommoditys = { multiPackageCommodityB1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 23), rt, staff, mvc, sessionBoss, iReturnCommodityNO, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(0/* 总单数 */, -1923.000000d/* 总进货金额 */, -2023.000000d/* 总销售额 */, 0.000000d/* 客单价 */, -100.000000d/* 毛利 */, 0.049432d/* 毛利率 */, 0, 0/* 畅销商品数量 */,
				0.000000d/* 畅销商品销售额 */);
		//
		runRetailTradeDailyReportSummaryTaskThread((DatetimeUtil.getDays(sdf.parse(RETURN_DATE), 23)), (DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 23)), dailyReportSummary, new ArrayList<RetailTradeDailyReportByCommodity>());
	}
}
