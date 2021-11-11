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
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

/** 测试目标：正常售卖各类型的各种商品
 * 
 * 测试用例描述： 单品，组合商品，多包装商品，服务型商品进行混合售卖，即零售单有：单品、单品+组合、单品+多包装、
 * 单品+服务、单品+组合+多包装、单品+组合+服务、单品+多包装+服务、单品+组合+多包装+服务、
 * 组合商品、组合+多包装、组合+服务、组合+服务+多包装、多包装商品、多包装+服务、服务型商品，无退货，
 * 查看日报表是否正常生成。（每张单的每个类型的商品不限于1种）
 * 
 * 测试步骤： 1、创建商品 创建创建单品A、B B增加两个多包装商品B1、B2 创建组合商品C，子商品为A和B 创建服务型商品D
 * 2、创建采购单，采购商品A和B，审核该入库单 3、创建入库单，入库商品A和B，审核该入库单 4、对各种商品组合进行零售
 * 5、运行夜间任务，生成日报表。 */
@WebAppConfiguration
public class DailyReportSIT1 extends BaseDailyReportSIT {
	private Staff staff;
	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
	public static final int RETURN_OBJECT = 1;
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
	/** 销售时间，零售单创建的时间 */
	public static final String SALE_DATE = "2030-02-01";
	/** 运行日报表夜间任务时间，生成的是前一天的日报表 */
	public static final String REPORT_DATE = "2030-02-02";

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
			normalCommodityA = createNormalCommodity(3.880000d, mvc, sessionBoss);
			// 创建带多包装商品的单品
			Integer[] mulitpleList = { 2, 3 };
			Double[] mulitplePriceList = { 13.760000d, 19.140000d };
			double price = 6.3800000d;
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
			Integer[] subCommodityNOList = { 1, 1 };
			Double[] subCommodityPriceList = { 2.380000d, 4.880000d };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 7.990000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity = createServiceCommodity(1.000000d, mvc, sessionBoss);
			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, multiPackageCommodityB1, multiPackageCommodityB2, subCommodity, serviceCommodity });
			// 采购
			Commodity[] puCommodityList = { normalCommodityA, normalCommodityB };
			Integer[] puCommodityNOList = { 2000, 1000 };
			Double[] puCommodityPriceList = { 2.380000d, 4.880000d };
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

	@Test
	public void DailyReportSITTest() throws Exception {
		int[] iaPromotionID = { 17, 19 };
		Commodity[] commoditys = { normalCommodityA, normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID, commoditys);
		//
		int[] iaPromotionID1 = { 52 };
		Commodity[] commoditys1 = { subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID1, commoditys1);
		//
		int[] iaPromotionID2 = { 48, 21 };
		Commodity[] commoditys2 = { multiPackageCommodityB1, multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID2, commoditys2);
		//
		int[] iaPromotionID3 = { 9 };
		Commodity[] commoditys3 = { serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID3, commoditys3);
		//
		int[] iaPromotionID4 = { 51, 28, 8 };
		Commodity[] commoditys4 = { normalCommodityA, normalCommodityB, subCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID4, commoditys4);
		//
		int[] iaPromotionID5 = { 17, 5, 9, 11 };
		Commodity[] commoditys5 = { normalCommodityA, normalCommodityB, multiPackageCommodityB1, multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID5, commoditys5);
		//
		int[] iaPromotionID6 = { 8, 13, 3 };
		Commodity[] commoditys6 = { normalCommodityA, normalCommodityB, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID6, commoditys6);
		//
		int[] iaPromotionID7 = { 22, 5, 17 };
		Commodity[] commoditys7 = { subCommodity, multiPackageCommodityB1, multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID7, commoditys7);
		//
		int[] iaPromotionID8 = { 30, 20 };
		Commodity[] commoditys8 = { subCommodity, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID8, commoditys8);
		//
		int[] iaPromotionID9 = { 11, 8, 9 };
		Commodity[] commoditys9 = { multiPackageCommodityB1, multiPackageCommodityB2, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID9, commoditys9);
		//
		int[] iaPromotionID10 = { 20, 15, 6 };
		Commodity[] commoditys10 = { normalCommodityA, subCommodity, multiPackageCommodityB2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID10, commoditys10);
		//
		int[] iaPromotionID11 = { 4, 16, 40, 3 };
		Commodity[] commoditys11 = { normalCommodityA, normalCommodityB, subCommodity, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID11, commoditys11);
		//
		int[] iaPromotionID12 = { 19, 8, 3, 11, 20 };
		Commodity[] commoditys12 = { normalCommodityA, normalCommodityB, multiPackageCommodityB1, multiPackageCommodityB2, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID12, commoditys12);
		//
		int[] iaPromotionID13 = { 22, 19, 1 };
		Commodity[] commoditys13 = { subCommodity, multiPackageCommodityB1, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID13, commoditys13);
		//
		int[] iaPromotionID14 = { 30, 15, 28, 18 };
		Commodity[] commoditys14 = { normalCommodityA, subCommodity, multiPackageCommodityB1, serviceCommodity };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iaPromotionID14, commoditys14);
		// 创建期望的日报表数据
		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(15/*总单数*/, 4594.280000d/*总进货金额*/, 6033.700000d/*总销售额*/, 402.246667d/*客单价*/, 1439.420000d/*毛利*/, 0.238563d/*毛利率*/, subCommodity.getID(),
				204/*畅销商品数量*/, 1629.960000d/*畅销商品总销售额*/);
		// 创建期望的商品报表数据
		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(), 166/*商品数量*/,
				395.080000d/*总进货价*/, 644.080000d/*总销售额*/, 249.000000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity2 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(), 89/*商品数量*/,
				434.320000d/*总进货价*/, 567.820000d/*总销售额*/, 133.500000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity2);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity3 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB1.getID(), multiPackageCommodityB1.getBarcodes(), multiPackageCommodityB1.getName(),
				multiPackageCommodityB1.getSpecification(), 123/*商品数量*/, 1200.480000d/*总进货价*/, 1692.480000d/*总销售额*/, 492.000000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity3);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity4 = getRetailTradeDailyReportByCommodity(multiPackageCommodityB2.getID(), multiPackageCommodityB2.getBarcodes(), multiPackageCommodityB2.getName(),
				multiPackageCommodityB2.getSpecification(), 74/*商品数量*/, 1083.360000d/*总进货价*/, 1416.360000d/*总销售额*/, 333.000000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity4);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity5 = getRetailTradeDailyReportByCommodity(subCommodity.getID(), subCommodity.getBarcodes(), subCommodity.getName(), subCommodity.getSpecification(), 204/*商品数量*/,
				1481.040000d/*总进货价*/, 1629.960000d/*总销售额*/, 148.920000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity5);
		//
		RetailTradeDailyReportByCommodity dailyReportByCommodity6 = getRetailTradeDailyReportByCommodity(serviceCommodity.getID(), serviceCommodity.getBarcodes(), serviceCommodity.getName(), serviceCommodity.getSpecification(), 83/*商品数量*/,
				0.000000d/*总进货价*/, 83.000000d/*总销售额*/, 83.000000d/*毛利*/);
		dailyReportByCommodityList.add(dailyReportByCommodity6);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0), dailyReportSummary, dailyReportByCommodityList);
	}
}
