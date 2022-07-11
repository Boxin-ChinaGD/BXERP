package com.bx.erp.test.task.dailyReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

/*
15、创建一个单品A，入库10件，售出20件，再入库20件，售出10件，查看日报表是否正常生成。（每次的入库价均不同）
16、创建一个单品A，入库10件，售出5件，再入库10件，售出5件，查看日报表是否正常生成。（每次的入库价均不同）
17、创建一个单品A，未入库，售出10件，然后入库10件，接着进行退货（5件），查看日报表是否正常生成。
18、售卖一个已经被删除的商品（单品/多包装商品/组合商品/服务型商品），查看日报表是否正常生成。
19、创建一个商品1（单品/多包装商品/组合商品/服务型商品）和商品2（单品/多包装商品/组合商品/服务型商品），进行售卖，然后删除商品1，再对生成的零售单进行退货（全部退/部分退），查看日报表是否正常生成。
*/
@WebAppConfiguration
public class DailyReportSIT4 extends BaseDailyReportSIT {
	private Staff staff;
	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
	public static final int RETURN_OBJECT = 1;
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
	/** 销售时间，零售单创建的时间 */
	final String SALE_DATE = "2062-01-01";
	/** 运行日报表夜间任务时间，生成的是前一天的日报表 */
	final String REPORT_DATE = "2062-01-02";
	// 用来存放RetailTrade,退货时需要取出使用
	Map<String, Object> rtMap = new HashMap<String, Object>();

	private Commodity normalCommodityA;
	private Commodity normalCommodityB;
	private Commodity normalCommodityC;
	private Commodity normalCommodityD;
	private Commodity subCommodityZA;
	private Commodity subCommodityZB;
	private Commodity multiPackageCommodityDA;
	private Commodity multiPackageCommodityDB;
	private Commodity multiPackageCommodityDC;
	private Commodity serviceCommodityFA;
	private Commodity serviceCommodityFB;

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

			// 创建单品 A,B,C,D,E
			normalCommodityA = createNormalCommodity(1.390000d, mvc, sessionBoss);
			normalCommodityD = createNormalCommodity(5.210000d, mvc, sessionBoss);

			// 创建带多包装商品的单品 DA,DB,DC;将单品 normalCommodityB,normalCommodityC也进行创建
			Integer[] mulitpleList = { 2, 3, 2 };
			Double[] mulitplePriceList = { 25.150000d, 36.220000d, 15.860000d };
			double priceCommB = 13.780000d;
			Commodity commB = BaseCommodityTest.DataInput.getCommodity();
			commB.setPriceRetail(priceCommB);
			commB.setPriceVIP(priceCommB);
			commB.setPriceWholesale(priceCommB);
			commB.setnOStart(10);
			commB.setPurchasingPriceStart(5.000000d);
			String providerIDs = "1,2,3";
			String multiPackagingInfo = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() % 1000000 + ";1,2,3;1," + mulitpleList[0] + ","
					+ mulitpleList[1] + ";4.88," + mulitplePriceList[0] + "," + mulitplePriceList[1] + ";4.88,13.76,19.14;8,8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B2" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodityB = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs, multiPackagingInfo, commB);
			List<BaseModel> commodityList = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodityB);
			multiPackageCommodityDA = (Commodity) commodityList.get(1);
			multiPackageCommodityDB = (Commodity) commodityList.get(0);
			//
			double priceCommC = 9.990000d;
			Commodity commC = BaseCommodityTest.DataInput.getCommodity();
			commC.setPriceRetail(priceCommC);
			commC.setPriceVIP(priceCommC);
			commC.setPriceWholesale(priceCommC);
			String providerIDs2 = "1,3";
			String multiPackagingInfo2 = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ";1,3;1," + mulitpleList[2] + ";4.88," + mulitplePriceList[2] + ";4.88,13.76;8,8;" //
					+ "普通商品C" + System.currentTimeMillis() % 1000000 + "," + "多包装商品C" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodityC = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs2, multiPackagingInfo2, commC);
			List<BaseModel> commodityList2 = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodityC);
			multiPackageCommodityDC = (Commodity) commodityList2.get(0);

			// 创建组合商品 ZA, ZB
			Commodity[] subCommodityListA = { normalCommodityA, normalCommodityB };
			Integer[] subCommodityNOListA = { 1, 2 };
			Double[] subCommodityPriceListA = { 1.390000d, 13.780000d };
			subCommodityZA = createSubCommodity(subCommodityListA, subCommodityNOListA, subCommodityPriceListA, 19.680000d, mvc, sessionBoss);
			//
			Commodity[] subCommodityListB = { normalCommodityB, normalCommodityC };
			Integer[] subCommodityNOListB = { 2, 1 };
			Double[] subCommodityPriceListB = { 13.780000d, 9.990000d };
			subCommodityZB = createSubCommodity(subCommodityListB, subCommodityNOListB, subCommodityPriceListB, 29.280000d, mvc, sessionBoss);

			// 创建服务商品
			serviceCommodityFA = createServiceCommodity(1.550000d, mvc, sessionBoss);
			serviceCommodityFB = createServiceCommodity(1.650000d, mvc, sessionBoss);

			// 设置商品的Barcode
			setCommodityListBarcode(new Commodity[] { normalCommodityA, normalCommodityB, normalCommodityC, normalCommodityD, multiPackageCommodityDA, multiPackageCommodityDB, multiPackageCommodityDC, subCommodityZA, subCommodityZB,
					serviceCommodityFA, serviceCommodityFB });
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
		Shared.caseLog("单品P1，售出20件（零售单1），入库单品P1 10件，售出5件（零售单2），零售单1退货2件");
		case1();

		Shared.caseLog("case2: 单品P2，售出5件（零售单4），再入库单品P2 10件，售出8件（零售单5），零售单2退货1件（退货单6）");
		case2();

		Shared.caseLog("case3: 售出单品P1 3件（零售单7），售出单品P3 4件（零售单8），入库单品P3 5件，售出组合商品8件（零售单9），售出单品P3 3件（零售单10）");
		case3();

		Shared.caseLog("case4: 入库商品P2 8件，入库商品P3 5件，售卖商品P2 5件（零售单11），售卖商品P3 2件（零售单12），入库单商品P1 10件 商品P2 8件");
		case4();

		Shared.caseLog("case5: 入库商品P1 5件 商品P2 6件，售卖商品Z1 5件（零售单13）");
		case5();

		Shared.caseLog("case6: 入库单品P3 5件，售出商品P3 6件（零售单14），修改商品P3的价格，售出商品P3 5件（零售单15）");
		case6();

		Shared.caseLog("case7: 删除商品P4（POS机未同步），售出单品P4 3件（零售单16），售出单品P2 2件、单品P4 2件、组合商品Z1 2件（零售单17）");
		case7();

		Shared.caseLog("case8: 删除商品Z2（POS机未同步），售出商品Z2 1件、商品P4 2件（零售单18），售出商品P2 4件、商品D1 2件、商品F1 2件（零售单19），对零售单4进行全部退货");
		case8();

		Shared.caseLog("case9: 删除商品D3（POS机未同步），【断网】，售卖商品D3 2件、商品P3 1件、F1 1件（零售单21），售卖商品P2 2件、商品Z1 2件、商品D2 2件（零售单22），（POS机同步）对零售单21全部退货，售卖商品Z1 1件、商品D3 2件、商品F1 1件（零售单24) ");
		case9();

		Shared.caseLog("case10: 删除商品F2（POS机未同步），【断网】，售卖商品F2 2件、商品D1 3件（零售单25），对零售单24进行部分退货，对零售单25部分退货，售卖商品Z1 3件、商品D1 2件、商品D2 2件（零售单27）");
		case10();

		Shared.caseLog("case11: 纯入库 ");
		case11();

		Shared.caseLog(
				"case12: 纯售卖：售出商品P1 1件（零售单28），售出商品Z1 3件（零售单29），售出商品D1 3件（零售单30），售出商品F1 1件（零售单31）、售出商品P2 2件、商品Z1 2件（零售单32），售出商品D2 1件、商品F1 1件（零售单33），售出商品P3 3件、商品Z1 2件、商品D1 1件（零售单34），售出商品Z1 2件、商品D2 1件、商品F1 1件（零售单35），售出商品P1 2件、商品P2 3件、商品P3 5件、商品Z1 2件、商品D1 2件、商品D1 5件、商品F1 2件（零售单36）");
		case12();

		Shared.caseLog("case13: 纯退货：部分退货零售单27（微信+现金），全部退货零售单17（包含删除商品），全部退货零售单15（微信+现金），部分退货零售单12，全部退货零售单11（微信），部分退货零售单35");
		case13();

		Shared.caseLog("case14: 售卖>退货：售卖商品P1 6件、商品P2 2件、商品P3 6件（零售单42），部分退货零售单14，全部退货零售单14,部分当天退货零售42,售卖商品P1 2件、商品P3 2件、商品F1 3件（零售单46），全部当天退货零售单46");
		case14();

		Shared.caseLog("case15: 退货>售卖：部分隔天退货零售单34，售卖商品D24 3件、商品Z1 3件（零售单49），全部隔天退货零售单19，部分隔天退货零售单30，售卖商品P1 1件、商品P2 1件、商品D1 1件（零售单52），全部隔天退货零售单28");
		case15();
	}

	private void case15() throws Exception {
		int[] iaReturnPromotionID1 = { 2, 2 };
		Commodity[] returnCommoditys1 = { normalCommodityC, subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt34"), staff, mvc, sessionBoss, iaReturnPromotionID1, returnCommoditys1);
		//
		int[] iSaleCommodityNO49 = { 3, 3 };
		Commodity[] commoditys49 = { subCommodityZA, multiPackageCommodityDB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO49, commoditys49);
		//
		int[] iaReturnPromotionID2 = { 4, 2, 2 };
		Commodity[] returnCommoditys2 = { normalCommodityB, multiPackageCommodityDA, serviceCommodityFA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt18"), staff, mvc, sessionBoss, iaReturnPromotionID2, returnCommoditys2);
		//
		int[] iaReturnPromotionID3 = { 2 };
		Commodity[] returnCommoditys3 = { multiPackageCommodityDA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt30"), staff, mvc, sessionBoss, iaReturnPromotionID3, returnCommoditys3);
		//
		int[] iSaleCommodityNO52 = { 1, 1, 1 };
		Commodity[] commoditys52 = { multiPackageCommodityDA, normalCommodityA, normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO52, commoditys52);
		//
		int[] iaReturnPromotionID4 = { 1 };
		Commodity[] returnCommoditys4 = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt28"), staff, mvc, sessionBoss, iaReturnPromotionID4, returnCommoditys4);
		//
		int[] iaReturnPromotionID5 = { 4 };
		Commodity[] returnCommoditys5 = { normalCommodityC };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt8"), staff, mvc, sessionBoss, iaReturnPromotionID5, returnCommoditys5);
		//
		int[] iaReturnPromotionID6 = { 3 };
		Commodity[] returnCommoditys6 = { normalCommodityC };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt10"), staff, mvc, sessionBoss, iaReturnPromotionID6, returnCommoditys6);
		//
		int[] iaReturnPromotionID7 = { 5, 1 };
		Commodity[] returnCommoditys7 = { normalCommodityC, multiPackageCommodityDA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), (RetailTrade) rtMap.get("rt36"), staff, mvc, sessionBoss, iaReturnPromotionID7, returnCommoditys7);
		//
		int[] iSaleCommodityNO57 = { 1, 6, 1 };
		Commodity[] commoditys57 = { multiPackageCommodityDA, normalCommodityC, normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO57, commoditys57);
		//
		RetailTradeDailyReportSummary dailyReportSummary_Case15 = getRetailTradeDailyReportSummary(3/*总单数*/, -6.680000d/*总进货金额*/, -48.010000d/*总销售额*/, -16.003333d/*客单价*/, -41.330000d/*毛利*/, 0.860862d/*毛利率*/, normalCommodityC.getID(),
				6/*畅销商品数量*/, 52.680000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case15 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case151 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDB.getID(), multiPackageCommodityDB.getBarcodes(), multiPackageCommodityDB.getName(),
				multiPackageCommodityDB.getSpecification(), 3/*商品数量*/, 49.950000d/*总进货价*/, 108.660000d/*总销售额*/, 58.710000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case151);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case152 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDA.getID(), multiPackageCommodityDA.getBarcodes(), multiPackageCommodityDA.getName(),
				multiPackageCommodityDA.getSpecification(), 2/*商品数量*/, -35.140000d/*总进货价*/, 50.300000d/*总销售额*/, 85.440000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case152);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case153 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 3/*商品数量*/,
				11.440000d/*总进货价*/, 59.040000d/*总销售额*/, 47.600000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case153);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case154 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				6/*商品数量*/, -20.490000d/*总进货价*/, 52.680000d/*总销售额*/, 73.170000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case154);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case155 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				2/*商品数量*/, -12.440000d/*总进货价*/, 27.560000d/*总销售额*/, 40.000000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case155);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case156 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				1/*商品数量*/, 0.000000d/*总进货价*/, 1.300000d/*总销售额*/, 1.300000d/*毛利*/);
		dailyReportByCommodityList_Case15.add(dailyReportByCommodity_Case156);

		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 14), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 14), dailyReportSummary_Case15, dailyReportByCommodityList_Case15);
	}

	private void case14() throws Exception {
		// 修改normalCommodityA的价格为1.300000d.
		normalCommodityA.setPriceRetail(1.300000d);
		normalCommodityA.setOperatorStaffID(staff.getID());
		BaseCommodityTest.updateCommodityPrice(normalCommodityA);
		//
		int[] iSaleCommodityNO42 = { 2, 6, 6 };
		Commodity[] commoditys42 = { normalCommodityB, normalCommodityC, normalCommodityA };
		RetailTrade rt42 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO42, commoditys42);
		//
		int[] iaReturnPromotionID1 = { 2 };
		Commodity[] returnCommoditys1 = { normalCommodityC };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), (RetailTrade) rtMap.get("rt14"), staff, mvc, sessionBoss, iaReturnPromotionID1, returnCommoditys1);
		//
		int[] iaReturnPromotionID2 = { 3 };
		Commodity[] returnCommoditys2 = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), (RetailTrade) rtMap.get("rt7"), staff, mvc, sessionBoss, iaReturnPromotionID2, returnCommoditys2);
		//
		int[] iaReturnPromotionID3 = { 3 };
		Commodity[] returnCommoditys3 = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), rt42, staff, mvc, sessionBoss, iaReturnPromotionID3, returnCommoditys3);
		//
		int[] iSaleCommodityNO46 = { 2, 2, 3 };
		Commodity[] commoditys46 = { normalCommodityC, normalCommodityA, serviceCommodityFA };
		RetailTrade rt46 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO46, commoditys46);
		//
		int[] iaReturnPromotionID4 = { 2, 2, 3 };
		Commodity[] returnCommoditys4 = { normalCommodityC, normalCommodityA, serviceCommodityFA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), rt46, staff, mvc, sessionBoss, iaReturnPromotionID4, returnCommoditys4);

		RetailTradeDailyReportSummary dailyReportSummary_Case14 = getRetailTradeDailyReportSummary(2/*总单数*/, 28.930000d/*总进货金额*/, 62.680000d/*总销售额*/, 31.340000d/*客单价*/, 33.750000d/*毛利*/, 0.538449d/*毛利率*/, normalCommodityC.getID(),
				6/*畅销商品数量*/, 52.680000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case14 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case141 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				2/*商品数量*/, 11.360000d/*总进货价*/, 27.560000d/*总销售额*/, 16.200000d/*毛利*/);
		dailyReportByCommodityList_Case14.add(dailyReportByCommodity_Case141);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case142 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				8/*商品数量*/, 17.510000d/*总进货价*/, 52.680000d/*总销售额*/, 35.170000d/*毛利*/);
		dailyReportByCommodityList_Case14.add(dailyReportByCommodity_Case142);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case143 = getRetailTradeDailyReportByCommodity(serviceCommodityFA.getID(), serviceCommodityFA.getBarcodes(), serviceCommodityFA.getName(),
				serviceCommodityFA.getSpecification(), 3/*商品数量*/, 0.000000d/*总进货价*/, 0.000000d/*总销售额*/, 0.000000d/*毛利*/);
		dailyReportByCommodityList_Case14.add(dailyReportByCommodity_Case143);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case144 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				8/*商品数量*/, 0.060000d/*总进货价*/, 3.900000d/*总销售额*/, 3.840000d/*毛利*/);
		dailyReportByCommodityList_Case14.add(dailyReportByCommodity_Case144);

		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 13), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 13), dailyReportSummary_Case14, dailyReportByCommodityList_Case14);
	}

	private void case13() throws Exception {// 35,26,11,12,35
		int[] iaReturnPromotionID1 = { 1, 1, 2 };
		Commodity[] returnCommoditys1 = { multiPackageCommodityDB, multiPackageCommodityDA, subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), (RetailTrade) rtMap.get("rt26"), staff, mvc, sessionBoss, iaReturnPromotionID1, returnCommoditys1);
		//
		int[] iaReturnPromotionID2 = { 5 };
		Commodity[] returnCommoditys2 = { normalCommodityC };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), (RetailTrade) rtMap.get("rt15"), staff, mvc, sessionBoss, iaReturnPromotionID2, returnCommoditys2);
		//
		int[] iaReturnPromotionID3 = { 1 };
		Commodity[] returnCommoditys3 = { normalCommodityC };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), (RetailTrade) rtMap.get("rt12"), staff, mvc, sessionBoss, iaReturnPromotionID3, returnCommoditys3);
		//
		int[] iaReturnPromotionID4 = { 5 };
		Commodity[] returnCommoditys4 = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), (RetailTrade) rtMap.get("rt11"), staff, mvc, sessionBoss, iaReturnPromotionID4, returnCommoditys4);
		//
		int[] iaReturnPromotionID5 = { 1, 1 };
		Commodity[] returnCommoditys5 = { subCommodityZA, serviceCommodityFA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), (RetailTrade) rtMap.get("rt35"), staff, mvc, sessionBoss, iaReturnPromotionID5, returnCommoditys5);

		RetailTradeDailyReportSummary dailyReportSummary_Case13 = getRetailTradeDailyReportSummary(0/*总单数*/, -123.810000d/*总进货金额*/, -243.540000d/*总销售额*/, 0.000000d/*客单价*/, -119.730000d/*毛利*/, 0.491624d/*毛利率*/, 0, 0/*畅销商品数量*/,
				0.000000d/*畅销商品销售额*/);
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 12), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 12), dailyReportSummary_Case13, new ArrayList<RetailTradeDailyReportByCommodity>());
	}

	private void case12() throws Exception {
		int[] iSaleCommodityNO28 = { 1 };
		Commodity[] commoditys28 = { normalCommodityA };
		RetailTrade rt28 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO28, commoditys28);
		rtMap.put("rt28", rt28);
		//
		int[] iSaleCommodityNO29 = { 3 };
		Commodity[] commoditys29 = { subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO29, commoditys29);
		//
		int[] iSaleCommodityNO30 = { 3 };
		Commodity[] commoditys30 = { multiPackageCommodityDA };
		RetailTrade rt30 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO30, commoditys30);
		rtMap.put("rt30", rt30);
		//
		int[] iSaleCommodityNO31 = { 2 };
		Commodity[] commoditys31 = { serviceCommodityFA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO31, commoditys31);
		//
		int[] iSaleCommodityNO32 = { 1, 2 };
		Commodity[] commoditys32 = { normalCommodityB, subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO32, commoditys32);
		//
		int[] iSaleCommodityNO33 = { 1, 1 };
		Commodity[] commoditys33 = { multiPackageCommodityDB, serviceCommodityFA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO33, commoditys33);
		//
		int[] iSaleCommodityNO34 = { 3, 2, 1 };
		Commodity[] commoditys34 = { normalCommodityC, subCommodityZA, serviceCommodityFA };
		RetailTrade rt34 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO34, commoditys34);
		rtMap.put("rt34", rt34);
		//
		int[] iSaleCommodityNO35 = { 2, 1, 1 };
		Commodity[] commoditys35 = { subCommodityZA, serviceCommodityFA, multiPackageCommodityDB };
		RetailTrade rt35 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO35, commoditys35);
		rtMap.put("rt35", rt35);
		//
		int[] iSaleCommodityNO36 = { 5, 2, 1, 2, 5, 2, 3 };
		Commodity[] commoditys36 = { normalCommodityC, serviceCommodityFA, multiPackageCommodityDB, subCommodityZA, normalCommodityA, multiPackageCommodityDA, normalCommodityB };
		RetailTrade rt36 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO36, commoditys36);
		rtMap.put("rt36", rt36);
		//
		RetailTradeDailyReportSummary dailyReportSummary_Case12 = getRetailTradeDailyReportSummary(9/*总单数*/, 295.070000d/*总进货金额*/, 595.440000d/*总销售额*/, 66.160000d/*客单价*/, 300.370000d/*毛利*/, 0.504450d/*毛利率*/, subCommodityZA.getID(),
				11/*畅销商品数量*/, 216.480000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case12 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case121 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				6/*商品数量*/, 2.020000d/*总进货价*/, 8.340000d/*总销售额*/, 6.320000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case121);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case122 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				4/*商品数量*/, 22.420000d/*总进货价*/, 55.120000d/*总销售额*/, 32.700000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case122);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case123 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				8/*商品数量*/, 34.380000d/*总进货价*/, 70.240000d/*总销售额*/, 35.860000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case123);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case124 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 11/*商品数量*/,
				128.800000d/*总进货价*/, 216.480000d/*总销售额*/, 87.680000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case124);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case125 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDA.getID(), multiPackageCommodityDA.getBarcodes(), multiPackageCommodityDA.getName(),
				multiPackageCommodityDA.getSpecification(), 5/*商品数量*/, 55.860000d/*总进货价*/, 125.750000d/*总销售额*/, 69.890000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case125);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case126 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDB.getID(), multiPackageCommodityDB.getBarcodes(), multiPackageCommodityDB.getName(),
				multiPackageCommodityDB.getSpecification(), 3/*商品数量*/, 51.590000d/*总进货价*/, 108.660000d/*总销售额*/, 57.070000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case126);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case127 = getRetailTradeDailyReportByCommodity(serviceCommodityFA.getID(), serviceCommodityFA.getBarcodes(), serviceCommodityFA.getName(),
				serviceCommodityFA.getSpecification(), 7/*商品数量*/, 0.000000d/*总进货价*/, 10.850000d/*总销售额*/, 10.850000d/*毛利*/);
		dailyReportByCommodityList_Case12.add(dailyReportByCommodity_Case127);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 11), dailyReportSummary_Case12, dailyReportByCommodityList_Case12);
	}

	private void case11() throws Exception {
		// 采购
		Commodity[] puCommodityList13 = { normalCommodityB };
		Integer[] puCommodityNOList13 = { 6 };
		Double[] puCommodityPriceList13 = { 5.450000d };
		PurchasingOrder purchasingOrder13 = createPurchasingOrder(puCommodityList13, puCommodityNOList13, puCommodityPriceList13, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder13, mvc, sessionBoss);
		// 入库
		Warehousing warehousing13 = createWarehousing(purchasingOrder13, mvc, sessionBoss);
		approveWarehousing(warehousing13, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList14 = { normalCommodityB, normalCommodityC };
		Integer[] puCommodityNOList14 = { 5, 4 };
		Double[] puCommodityPriceList14 = { 5.770000d, 4.780000d };
		PurchasingOrder purchasingOrder14 = createPurchasingOrder(puCommodityList14, puCommodityNOList14, puCommodityPriceList14, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder14, mvc, sessionBoss);
		// 入库
		Warehousing warehousing14 = createWarehousing(purchasingOrder14, mvc, sessionBoss);
		approveWarehousing(warehousing14, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList15 = { normalCommodityA, normalCommodityB };
		Integer[] puCommodityNOList15 = { 5, 8 };
		Double[] puCommodityPriceList15 = { 0.390000d, 5.880000d };
		PurchasingOrder purchasingOrder15 = createPurchasingOrder(puCommodityList15, puCommodityNOList15, puCommodityPriceList15, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder15, mvc, sessionBoss);
		// 入库
		Warehousing warehousing15 = createWarehousing(purchasingOrder15, mvc, sessionBoss);
		approveWarehousing(warehousing15, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList16 = { normalCommodityB };
		Integer[] puCommodityNOList16 = { 6 };
		Double[] puCommodityPriceList16 = { 5.570000d };
		PurchasingOrder purchasingOrder16 = createPurchasingOrder(puCommodityList16, puCommodityNOList16, puCommodityPriceList16, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder16, mvc, sessionBoss);
		// 入库
		Warehousing warehousing16 = createWarehousing(purchasingOrder16, mvc, sessionBoss);
		approveWarehousing(warehousing16, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList17 = { normalCommodityA, normalCommodityB, normalCommodityC };
		Integer[] puCommodityNOList17 = { 5, 5, 5 };
		Double[] puCommodityPriceList17 = { 0.380000d, 5.680000d, 4.360000d };
		PurchasingOrder purchasingOrder17 = createPurchasingOrder(puCommodityList17, puCommodityNOList17, puCommodityPriceList17, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder17, mvc, sessionBoss);
		// 入库
		Warehousing warehousing17 = createWarehousing(purchasingOrder17, mvc, sessionBoss);
		approveWarehousing(warehousing17, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList18 = { normalCommodityB };
		Integer[] puCommodityNOList18 = { 8 };
		Double[] puCommodityPriceList18 = { 5.550000d };
		PurchasingOrder purchasingOrder18 = createPurchasingOrder(puCommodityList18, puCommodityNOList18, puCommodityPriceList18, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder18, mvc, sessionBoss);
		// 入库
		Warehousing warehousing18 = createWarehousing(purchasingOrder18, mvc, sessionBoss);
		approveWarehousing(warehousing18, mvc, sessionBoss);
	}

	private void case10() throws Exception {
		// 删除serviceCommodityFB
		BaseCommodityTest.deleteCommodityViaMapper(serviceCommodityFB, EnumErrorCode.EC_NoError);
		// 采购
		Commodity[] puCommodityList11 = { normalCommodityB, normalCommodityC };
		Integer[] puCommodityNOList11 = { 8, 6 };
		Double[] puCommodityPriceList11 = { 6.250000d, 4.230000d };
		PurchasingOrder purchasingOrder11 = createPurchasingOrder(puCommodityList11, puCommodityNOList11, puCommodityPriceList11, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder11, mvc, sessionBoss);
		// 入库
		Warehousing warehousing11 = createWarehousing(purchasingOrder11, mvc, sessionBoss);
		approveWarehousing(warehousing11, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO25 = { 2, 3 };
		Commodity[] commoditys25 = { serviceCommodityFB, multiPackageCommodityDA };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		RetailTrade rt25 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO25, commoditys25);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		//
		int[] iaReturnPromotionID1 = { 2, 1 };
		Commodity[] returnCommoditys1 = { serviceCommodityFB, multiPackageCommodityDA };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), rt25, staff, mvc, sessionBoss, iaReturnPromotionID1, returnCommoditys1);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		// 采购
		Commodity[] puCommodityList12 = { normalCommodityA, normalCommodityB };
		Integer[] puCommodityNOList12 = { 6, 12 };
		Double[] puCommodityPriceList12 = { 0.330000d, 5.990000d };
		PurchasingOrder purchasingOrder12 = createPurchasingOrder(puCommodityList12, puCommodityNOList12, puCommodityPriceList12, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder12, mvc, sessionBoss);
		// 入库
		Warehousing warehousing12 = createWarehousing(purchasingOrder12, mvc, sessionBoss);
		approveWarehousing(warehousing12, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO26 = { 2, 2, 3 };
		Commodity[] commoditys26 = { multiPackageCommodityDB, multiPackageCommodityDA, subCommodityZA };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		;

		RetailTrade rt26 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO26, commoditys26);
		rtMap.put("rt26", rt26);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		//
		RetailTradeDailyReportSummary dailyReportSummary_Case10 = getRetailTradeDailyReportSummary(2/*总单数*/, 121.840000d/*总进货金额*/, 232.080000d/*总销售额*/, 116.040000d/*客单价*/, 110.240000d/*毛利*/, 0.475009d/*毛利率*/,
				multiPackageCommodityDA.getID(), 4/*畅销商品数量*/, 100.600000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case10 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case101 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDA.getID(), multiPackageCommodityDA.getBarcodes(), multiPackageCommodityDA.getName(),
				multiPackageCommodityDA.getSpecification(), 5/*商品数量*/, 47.450000d/*总进货价*/, 100.600000d/*总销售额*/, 53.150000d/*毛利*/);
		dailyReportByCommodityList_Case10.add(dailyReportByCommodity_Case101);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case102 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDB.getID(), multiPackageCommodityDB.getBarcodes(), multiPackageCommodityDB.getName(),
				multiPackageCommodityDB.getSpecification(), 2/*商品数量*/, 37.500000d/*总进货价*/, 72.440000d/*总销售额*/, 34.940000d/*毛利*/);
		dailyReportByCommodityList_Case10.add(dailyReportByCommodity_Case102);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case103 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 3/*商品数量*/,
				36.890000d/*总进货价*/, 59.040000d/*总销售额*/, 22.150000d/*毛利*/);
		dailyReportByCommodityList_Case10.add(dailyReportByCommodity_Case103);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case104 = getRetailTradeDailyReportByCommodity(serviceCommodityFB.getID(), serviceCommodityFB.getBarcodes(), serviceCommodityFB.getName(),
				serviceCommodityFB.getSpecification(), 2/*商品数量*/, 0.000000d/*总进货价*/, 0.000000d/*总销售额*/, 0.000000d/*毛利*/);
		dailyReportByCommodityList_Case10.add(dailyReportByCommodity_Case104);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 9), dailyReportSummary_Case10, dailyReportByCommodityList_Case10);
	}

	private void case9() throws Exception {
		// 删除multiPackageCommodityDC
		BaseCommodityTest.deleteCommodityViaMapper(multiPackageCommodityDC, EnumErrorCode.EC_NoError);
		//
		int[] iSaleCommodityNO21 = { 2, 1, 1 };
		Commodity[] commoditys21 = { multiPackageCommodityDC, normalCommodityC, serviceCommodityFA };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();

		RetailTrade rt21 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO21, commoditys21);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		//
		int[] iSaleCommodityNO22 = { 2, 2, 2 };
		Commodity[] commoditys22 = { normalCommodityB, subCommodityZA, multiPackageCommodityDB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO22, commoditys22);
		// 采购
		Commodity[] puCommodityList11 = { normalCommodityB, normalCommodityC };
		Integer[] puCommodityNOList11 = { 5, 5 };
		Double[] puCommodityPriceList11 = { 5.660000d, 4.220000d };
		PurchasingOrder purchasingOrder11 = createPurchasingOrder(puCommodityList11, puCommodityNOList11, puCommodityPriceList11, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder11, mvc, sessionBoss);
		// 入库
		Warehousing warehousing11 = createWarehousing(purchasingOrder11, mvc, sessionBoss);
		approveWarehousing(warehousing11, mvc, sessionBoss);
		//
		int[] iaReturnPromotionID1 = { 2, 1, 1 };
		Commodity[] returnCommoditys1 = { multiPackageCommodityDC, normalCommodityC, serviceCommodityFA };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();

		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), rt21, staff, mvc, sessionBoss, iaReturnPromotionID1, returnCommoditys1);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		//
		int[] iSaleCommodityNO24 = { 1, 1, 2 };
		Commodity[] commoditys24 = { serviceCommodityFA, subCommodityZA, multiPackageCommodityDC };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO24, commoditys24);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();

		//
		RetailTradeDailyReportSummary dailyReportSummary_Case9 = getRetailTradeDailyReportSummary(3/*总单数*/, 101.130000d/*总进货金额*/, 192.310000d/*总销售额*/, 64.103333d/*客单价*/, 91.180000d/*毛利*/, 0.474130d/*毛利率*/, subCommodityZA.getID(),
				3/*畅销商品数量*/, 59.040000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case9 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case91 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				2/*商品数量*/, 12.000000d/*总进货价*/, 27.560000d/*总销售额*/, 15.560000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case91);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case92 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				1/*商品数量*/, 0.000000d/*总进货价*/, 0.000000d/*总销售额*/, 0.000000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case92);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case93 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 3/*商品数量*/,
				36.250000d/*总进货价*/, 59.040000d/*总销售额*/, 22.790000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case93);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case94 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDB.getID(), multiPackageCommodityDB.getBarcodes(), multiPackageCommodityDB.getName(),
				multiPackageCommodityDB.getSpecification(), 2/*商品数量*/, 36.000000d/*总进货价*/, 72.440000d/*总销售额*/, 36.440000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case94);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case95 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDC.getID(), multiPackageCommodityDC.getBarcodes(), multiPackageCommodityDC.getName(),
				multiPackageCommodityDC.getSpecification(), 4/*商品数量*/, 16.880000d/*总进货价*/, 31.720000d/*总销售额*/, 14.840000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case95);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case96 = getRetailTradeDailyReportByCommodity(serviceCommodityFA.getID(), serviceCommodityFA.getBarcodes(), serviceCommodityFA.getName(),
				serviceCommodityFA.getSpecification(), 2/*商品数量*/, 0.000000d/*总进货价*/, 1.550000d/*总销售额*/, 1.550000d/*毛利*/);
		dailyReportByCommodityList_Case9.add(dailyReportByCommodity_Case96);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 8), dailyReportSummary_Case9, dailyReportByCommodityList_Case9);

	}

	private void case8() throws Exception {
		// 采购
		Commodity[] puCommodityList10 = { normalCommodityB };
		Integer[] puCommodityNOList10 = { 4 };
		Double[] puCommodityPriceList10 = { 6.000000d };
		PurchasingOrder purchasingOrder10 = createPurchasingOrder(puCommodityList10, puCommodityNOList10, puCommodityPriceList10, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder10, mvc, sessionBoss);
		// 入库
		Warehousing warehousing10 = createWarehousing(purchasingOrder10, mvc, sessionBoss);
		approveWarehousing(warehousing10, mvc, sessionBoss);
		// 删除subCommodityZB
		BaseCommodityTest.deleteCommodityViaMapper(subCommodityZB, EnumErrorCode.EC_NoError);
		//
		int[] iSaleCommodityNO17 = { 1, 2 };
		Commodity[] commoditys17 = { subCommodityZB, normalCommodityC };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO17, commoditys17);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();
		//
		int[] iSaleCommodityNO18 = { 4, 2, 2 };
		Commodity[] commoditys18 = { normalCommodityB, multiPackageCommodityDA, serviceCommodityFA };
		RetailTrade rt18 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO18, commoditys18);
		rtMap.put("rt18", rt18);
		//
		int[] iaReturnPromotionID19 = { 5 };
		Commodity[] returnCommoditys19 = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), (RetailTrade) rtMap.get("rt4"), staff, mvc, sessionBoss, iaReturnPromotionID19, returnCommoditys19);
		//
		RetailTradeDailyReportSummary dailyReportSummary_Case8 = getRetailTradeDailyReportSummary(2/*总单数*/, 46.640000d/*总进货金额*/, 86.460000d/*总销售额*/, 43.230000d/*客单价*/, 39.820000d/*毛利*/, 0.460560d/*毛利率*/, normalCommodityB.getID(),
				4/*畅销商品数量*/, 55.120000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case8 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case81 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				2/*商品数量*/, 0.000000d/*总进货价*/, 19.980000d/*总销售额*/, 19.980000d/*毛利*/);
		dailyReportByCommodityList_Case8.add(dailyReportByCommodity_Case81);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case82 = getRetailTradeDailyReportByCommodity(subCommodityZB.getID(), subCommodityZB.getBarcodes(), subCommodityZB.getName(), subCommodityZB.getSpecification(), 1/*商品数量*/,
				5.670000d/*总进货价*/, 29.280000d/*总销售额*/, 23.610000d/*毛利*/);
		dailyReportByCommodityList_Case8.add(dailyReportByCommodity_Case82);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case83 = getRetailTradeDailyReportByCommodity(multiPackageCommodityDA.getID(), multiPackageCommodityDA.getBarcodes(), multiPackageCommodityDA.getName(),
				multiPackageCommodityDA.getSpecification(), 2/*商品数量*/, 24.000000d/*总进货价*/, 50.300000d/*总销售额*/, 26.300000d/*毛利*/);
		dailyReportByCommodityList_Case8.add(dailyReportByCommodity_Case83);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case84 = getRetailTradeDailyReportByCommodity(serviceCommodityFA.getID(), serviceCommodityFA.getBarcodes(), serviceCommodityFA.getName(),
				serviceCommodityFA.getSpecification(), 2/*商品数量*/, 0.000000d/*总进货价*/, 3.100000d/*总销售额*/, 3.100000d/*毛利*/);
		dailyReportByCommodityList_Case8.add(dailyReportByCommodity_Case84);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 7), dailyReportSummary_Case8, dailyReportByCommodityList_Case8);
	}

	private void case7() throws Exception {
		// 删除normalCommodityD
		BaseCommodityTest.deleteCommodityViaMapper(normalCommodityD, EnumErrorCode.EC_NoError);
		//
		int[] iSaleCommodityNO16 = { 3 };
		Commodity[] commoditys16 = { normalCommodityD };
		// 当包含已删除商品时，创建零售单的状态为2,这样做的目的是使创建零售单的compareTo通过
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO16, commoditys16);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();
		// 采购
		Commodity[] puCommodityList9 = { normalCommodityB };
		Integer[] puCommodityNOList9 = { 4 };
		Double[] puCommodityPriceList9 = { 5.670000d };
		PurchasingOrder purchasingOrder9 = createPurchasingOrder(puCommodityList9, puCommodityNOList9, puCommodityPriceList9, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder9, mvc, sessionBoss);
		// 入库
		Warehousing warehousing9 = createWarehousing(purchasingOrder9, mvc, sessionBoss);
		approveWarehousing(warehousing9, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO17 = { 2, 2, 2 };
		Commodity[] commoditys17 = { subCommodityZA, normalCommodityD, normalCommodityB };
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
		;
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO17, commoditys17);
		retailTrade_defualtStatus = RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex();
		//
		RetailTradeDailyReportSummary dailyReportSummary_Case7 = getRetailTradeDailyReportSummary(2/*总单数*/, 36.240000d/*总进货金额*/, 92.970000d/*总销售额*/, 46.485000d/*客单价*/, 56.730000d/*毛利*/, 0.610197d/*毛利率*/, normalCommodityD.getID(),
				5/*畅销商品数量*/, 26.050000d/*畅销商品销售额*/);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case7 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case71 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				2/*商品数量*/, 11.660000d/*总进货价*/, 27.560000d/*总销售额*/, 15.900000d/*毛利*/);
		dailyReportByCommodityList_Case7.add(dailyReportByCommodity_Case71);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case72 = getRetailTradeDailyReportByCommodity(normalCommodityD.getID(), normalCommodityD.getBarcodes(), normalCommodityD.getName(), normalCommodityD.getSpecification(),
				5/*商品数量*/, 0.000000d/*总进货价*/, 26.050000d/*总销售额*/, 26.050000d/*毛利*/);
		dailyReportByCommodityList_Case7.add(dailyReportByCommodity_Case72);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case73 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 2/*商品数量*/,
				24.580000d/*总进货价*/, 39.360000d/*总销售额*/, 14.780000d/*毛利*/);
		dailyReportByCommodityList_Case7.add(dailyReportByCommodity_Case73);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 6), dailyReportSummary_Case7, dailyReportByCommodityList_Case7);

	}

	private void case6() throws Exception {
		// 采购
		Commodity[] puCommodityList8 = { normalCommodityC };
		Integer[] puCommodityNOList8 = { 5 };
		Double[] puCommodityPriceList8 = { 4.210000d };
		PurchasingOrder purchasingOrder8 = createPurchasingOrder(puCommodityList8, puCommodityNOList8, puCommodityPriceList8, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder8, mvc, sessionBoss);
		// 入库
		Warehousing warehousing8 = createWarehousing(purchasingOrder8, mvc, sessionBoss);
		approveWarehousing(warehousing8, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO14 = { 6 };
		Commodity[] commoditys14 = { normalCommodityC };
		RetailTrade rt14 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO14, commoditys14);
		rtMap.put("rt14", rt14);
		// 将normalCommodityC的售卖价格改为8.780000
		normalCommodityC.setPriceRetail(8.780000d);
		normalCommodityC.setOperatorStaffID(staff.getID());
		BaseCommodityTest.updateCommodityPrice(normalCommodityC);
		//
		int[] iSaleCommodityNO15 = { 5 };
		Commodity[] commoditys15 = { normalCommodityC };
		RetailTrade rt15 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO15, commoditys15);
		rtMap.put("rt15", rt15);

		RetailTradeDailyReportSummary dailyReportSummary_Case6 = getRetailTradeDailyReportSummary(2/* 总单数 */, 46.310000d/* 总进货金额 */, 103.840000d/* 总销售额 */, 51.920000d/* 客单价 */, 57.530000d/* 毛利 */, 0.554025d/* 毛利率 */,
				normalCommodityC.getID(), 11/* 畅销商品数量 */, 103.840000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case6 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case6 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				11/* 商品数量 */, 46.310000d/* 总进货价 */, 103.840000d/* 总销售额 */, 57.530000d/* 毛利 */);
		dailyReportByCommodityList_Case6.add(dailyReportByCommodity_Case6);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 5), dailyReportSummary_Case6, dailyReportByCommodityList_Case6);
	}

	private void case5() throws Exception {
		// 采购
		Commodity[] puCommodityList7 = { normalCommodityA, normalCommodityB };
		Integer[] puCommodityNOList7 = { 5, 6 };
		Double[] puCommodityPriceList7 = { 0.350000d, 5.990000d };
		PurchasingOrder purchasingOrder7 = createPurchasingOrder(puCommodityList7, puCommodityNOList7, puCommodityPriceList7, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder7, mvc, sessionBoss);
		// 入库
		Warehousing warehousing7 = createWarehousing(purchasingOrder7, mvc, sessionBoss);
		approveWarehousing(warehousing7, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO13 = { 5 };
		Commodity[] commoditys13 = { subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO13, commoditys13);
		//
		RetailTradeDailyReportSummary dailyReportSummary_Case5 = getRetailTradeDailyReportSummary(1/* 总单数 */, 63.380000d/* 总进货金额 */, 98.400000d/* 总销售额 */, 98.400000d/* 客单价 */, 35.020000d/* 毛利 */, 0.355894d/* 毛利率 */, subCommodityZA.getID(),
				5/* 畅销商品数量 */, 98.400000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case5 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case51 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 5/* 商品数量 */,
				63.380000d/* 总进货价 */, 98.400000d/* 总销售额 */, 35.020000d/* 毛利 */);
		dailyReportByCommodityList_Case5.add(dailyReportByCommodity_Case51);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 4), dailyReportSummary_Case5, dailyReportByCommodityList_Case5);
	}

	private void case4() throws Exception {
		// 采购
		Commodity[] puCommodityList4 = { normalCommodityB };
		Integer[] puCommodityNOList4 = { 8 };
		Double[] puCommodityPriceList4 = { 6.260000d };
		PurchasingOrder purchasingOrder4 = createPurchasingOrder(puCommodityList4, puCommodityNOList4, puCommodityPriceList4, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder4, mvc, sessionBoss);
		// 入库
		Warehousing warehousing4 = createWarehousing(purchasingOrder4, mvc, sessionBoss);
		approveWarehousing(warehousing4, mvc, sessionBoss);
		// 采购
		Commodity[] puCommodityList5 = { normalCommodityC };
		Integer[] puCommodityNOList5 = { 5 };
		Double[] puCommodityPriceList5 = { 4.210000d };
		PurchasingOrder purchasingOrder5 = createPurchasingOrder(puCommodityList5, puCommodityNOList5, puCommodityPriceList5, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder5, mvc, sessionBoss);
		// 入库
		Warehousing warehousing5 = createWarehousing(purchasingOrder5, mvc, sessionBoss);
		approveWarehousing(warehousing5, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO11 = { 5 };
		Commodity[] commoditys11 = { normalCommodityB };
		RetailTrade rt11 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO11, commoditys11);
		rtMap.put("rt11", rt11);
		//
		int[] iSaleCommodityNO12 = { 2 };
		Commodity[] commoditys12 = { normalCommodityC };
		RetailTrade rt12 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO12, commoditys12);
		rtMap.put("rt12", rt12);
		// 采购
		Commodity[] puCommodityList6 = { normalCommodityA, normalCommodityB };
		Integer[] puCommodityNOList6 = { 10, 5 };
		Double[] puCommodityPriceList6 = { 0.330000d, 6.160000d };
		PurchasingOrder purchasingOrder6 = createPurchasingOrder(puCommodityList6, puCommodityNOList6, puCommodityPriceList6, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder6, mvc, sessionBoss);
		// 入库
		Warehousing warehousing6 = createWarehousing(purchasingOrder6, mvc, sessionBoss);
		approveWarehousing(warehousing6, mvc, sessionBoss);

		RetailTradeDailyReportSummary dailyReportSummary_Case4 = getRetailTradeDailyReportSummary(2/* 总单数 */, 41.160000d/* 总进货金额 */, 88.880000d/* 总销售额 */, 44.440000d/* 客单价 */, 47.720000d/* 毛利 */, 0.536904d/* 毛利率 */,
				normalCommodityB.getID(), 5/* 畅销商品数量 */, 68.900000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case4 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case41 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				5/* 商品数量 */, 30.720000d/* 总进货价 */, 68.900000d/* 总销售额 */, 38.180000d/* 毛利 */);
		dailyReportByCommodityList_Case4.add(dailyReportByCommodity_Case41);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case42 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				2/* 商品数量 */, 10.440000d/* 总进货价 */, 19.980000d/* 总销售额 */, 9.540000d/* 毛利 */);
		dailyReportByCommodityList_Case4.add(dailyReportByCommodity_Case42);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 3), dailyReportSummary_Case4, dailyReportByCommodityList_Case4);
	}

	private void case3() throws Exception {
		int[] iSaleCommodityNO7 = { 3 };
		Commodity[] commoditys7 = { normalCommodityA };
		RetailTrade rt7 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO7, commoditys7);
		rtMap.put("rt7", rt7);
		//
		int[] iSaleCommodityNO8 = { 4 };
		Commodity[] commoditys8 = { normalCommodityC };
		RetailTrade rt8 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO8, commoditys8);
		rtMap.put("rt8", rt8);
		// 采购
		Commodity[] puCommodityList3 = { normalCommodityC };
		Integer[] puCommodityNOList3 = { 5 };
		Double[] puCommodityPriceList3 = { 5.220000d };
		PurchasingOrder purchasingOrder3 = createPurchasingOrder(puCommodityList3, puCommodityNOList3, puCommodityPriceList3, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder3, mvc, sessionBoss);
		// 入库
		Warehousing warehousing3 = createWarehousing(purchasingOrder3, mvc, sessionBoss);
		approveWarehousing(warehousing3, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO9 = { 3 };
		Commodity[] commoditys9 = { subCommodityZA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO9, commoditys9);
		//
		int[] iSaleCommodityNO10 = { 3 };
		Commodity[] commoditys10 = { normalCommodityC };
		RetailTrade rt10 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO10, commoditys10);
		rtMap.put("rt10", rt10);

		RetailTradeDailyReportSummary dailyReportSummary_Case3 = getRetailTradeDailyReportSummary(4/* 总单数 */, 51.600000d/* 总进货金额 */, 133.140000d/* 总销售额 */, 33.285000d/* 客单价 */, 81.540000d/* 毛利 */, 0.612438d/* 毛利率 */,
				normalCommodityC.getID(), 7/* 畅销商品数量 */, 69.930000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case3 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case31 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				3/* 商品数量 */, 0.930000d/* 总进货价 */, 4.170000d/* 总销售额 */, 3.240000d/* 毛利 */);
		dailyReportByCommodityList_Case3.add(dailyReportByCommodity_Case31);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case32 = getRetailTradeDailyReportByCommodity(normalCommodityC.getID(), normalCommodityC.getBarcodes(), normalCommodityC.getName(), normalCommodityC.getSpecification(),
				7/* 商品数量 */, 15.660000d/* 总进货价 */, 69.930000d/* 总销售额 */, 54.270000d/* 毛利 */);
		dailyReportByCommodityList_Case3.add(dailyReportByCommodity_Case32);
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case33 = getRetailTradeDailyReportByCommodity(subCommodityZA.getID(), subCommodityZA.getBarcodes(), subCommodityZA.getName(), subCommodityZA.getSpecification(), 3/* 商品数量 */,
				35.010000d/* 总进货价 */, 59.040000d/* 总销售额 */, 24.030000d/* 毛利 */);
		dailyReportByCommodityList_Case3.add(dailyReportByCommodity_Case33);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 2), dailyReportSummary_Case3, dailyReportByCommodityList_Case3);
	}

	private void case2() throws Exception {
		int[] iSaleCommodityNO4 = { 5 };
		Commodity[] commoditys4 = { normalCommodityB };
		RetailTrade rt4 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO4, commoditys4);
		rtMap.put("rt4", rt4);
		// 采购
		Commodity[] puCommodityList2 = { normalCommodityB };
		Integer[] puCommodityNOList2 = { 10 };
		Double[] puCommodityPriceList2 = { 5.680000d };
		PurchasingOrder purchasingOrder2 = createPurchasingOrder(puCommodityList2, puCommodityNOList2, puCommodityPriceList2, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder2, mvc, sessionBoss);
		// 入库
		Warehousing warehousing2 = createWarehousing(purchasingOrder2, mvc, sessionBoss);
		approveWarehousing(warehousing2, mvc, sessionBoss);

		int[] iSaleCommodityNO5 = { 8 };
		Commodity[] commoditys5 = { normalCommodityB };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO5, commoditys5);

		int[] iaReturnPromotionID2 = { 1 };
		Commodity[] returnCommoditys2 = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), (RetailTrade) rtMap.get("rt2"), staff, mvc, sessionBoss, iaReturnPromotionID2, returnCommoditys2);

		RetailTradeDailyReportSummary dailyReportSummary_Case2 = getRetailTradeDailyReportSummary(2/* 总单数 */, 66.730000d/* 总进货金额 */, 177.750000d/* 总销售额 */, 88.875000d/* 客单价 */, 111.020000d/* 毛利 */, 0.624585d/* 毛利率 */,
				normalCommodityB.getID(), 13/* 畅销商品数量 */, 179.140000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList_Case2 = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity_Case21 = getRetailTradeDailyReportByCommodity(normalCommodityB.getID(), normalCommodityB.getBarcodes(), normalCommodityB.getName(), normalCommodityB.getSpecification(),
				13/* 商品数量 */, 67.040000d/* 总进货价 */, 179.140000d/* 总销售额 */, 112.100000d/* 毛利 */);
		dailyReportByCommodityList_Case2.add(dailyReportByCommodity_Case21);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 1), dailyReportSummary_Case2, dailyReportByCommodityList_Case2);
	}

	private void case1() throws Exception {
		int[] iSaleCommodityNO = { 20 };
		Commodity[] commoditys = { normalCommodityA };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO, commoditys);
		// 采购
		Commodity[] puCommodityList = { normalCommodityA };
		Integer[] puCommodityNOList = { 10 };
		Double[] puCommodityPriceList = { 0.310000d };
		PurchasingOrder purchasingOrder = createPurchasingOrder(puCommodityList, puCommodityNOList, puCommodityPriceList, mvc, sessionBoss);
		approvePurchasingOrder(purchasingOrder, mvc, sessionBoss);
		// 入库
		Warehousing warehousing = createWarehousing(purchasingOrder, mvc, sessionBoss);
		approveWarehousing(warehousing, mvc, sessionBoss);
		//
		int[] iSaleCommodityNO2 = { 5 };
		Commodity[] commoditys2 = { normalCommodityA };
		RetailTrade rt2 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		rtMap.put("rt2", rt2);
		//
		int[] iaReturnPromotionID = { 12 };
		Commodity[] returnCommoditys = { normalCommodityA };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), rt, staff, mvc, sessionBoss, iaReturnPromotionID, returnCommoditys);

		RetailTradeDailyReportSummary dailyReportSummary = getRetailTradeDailyReportSummary(2/* 总单数 */, 1.550000d/* 总进货金额 */, 18.070000d/* 总销售额 */, 9.035000d/* 客单价 */, 16.520000d/* 毛利 */, 0.914222d/* 毛利率 */, normalCommodityA.getID(),
				13/* 畅销商品数量 */, 18.070000d/* 畅销商品销售额 */);

		List<RetailTradeDailyReportByCommodity> dailyReportByCommodityList = new ArrayList<RetailTradeDailyReportByCommodity>();
		RetailTradeDailyReportByCommodity dailyReportByCommodity1 = getRetailTradeDailyReportByCommodity(normalCommodityA.getID(), normalCommodityA.getBarcodes(), normalCommodityA.getName(), normalCommodityA.getSpecification(),
				25/* 商品数量 */, 1.550000d/* 总进货价 */, 18.070000d/* 总销售额 */, 16.520000d/* 毛利 */);
		dailyReportByCommodityList.add(dailyReportByCommodity1);
		//
		runRetailTradeDailyReportSummaryTaskThread(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 0), DatetimeUtil.getDays(sdf.parse(REPORT_DATE), 0), dailyReportSummary, dailyReportByCommodityList);
	}

}
