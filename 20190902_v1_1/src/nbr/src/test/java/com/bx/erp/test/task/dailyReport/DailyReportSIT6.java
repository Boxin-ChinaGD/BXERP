package com.bx.erp.test.task.dailyReport;

import static org.testng.Assert.assertTrue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

//商品销售排行榜测试目标:有退货和隔天退货（部分，全部），无退货的销售商品的情况验证：销售的商品数量，销售排行最高商品名称和数量，销售排行最低商品名称和数量（排行榜只包含销售的商品，退货不影响排行榜）
public class DailyReportSIT6 extends BaseDailyReportSIT {
	private Staff staff;
	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
	public static final int RETURN_OBJECT = 1;
	public static final String SALE_DATE = "2099-04-12";
	public static final String REPORT_DATE = "2099-04-13";
	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);

	private Commodity normalCommodity1;
	private Commodity normalCommodity2;
	private Commodity normalCommodity3;
	private Commodity normalCommodity4;
	private Commodity normalCommodity5;
	private Commodity normalCommodity6;
	private Commodity normalCommodity7;
	private Commodity normalCommodity8;
	private Commodity normalCommodity9;
	private Commodity normalCommodity10;
	private Commodity normalCommodity11;
	private Commodity subCommodity;
	private Commodity multiPackageCommodity1;
	private Commodity multiPackageCommodity2;
	private Commodity serviceCommodity1;
	private Commodity serviceCommodity2;

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
			normalCommodity1 = createNormalCommodity(2.680000d, mvc, sessionBoss);
			normalCommodity2 = createNormalCommodity(3.000000d, mvc, sessionBoss);
			normalCommodity3 = createNormalCommodity(5.000000d, mvc, sessionBoss);
			normalCommodity4 = createNormalCommodity(1.000000d, mvc, sessionBoss);
			normalCommodity7 = createNormalCommodity(10.000000d, mvc, sessionBoss);
			normalCommodity8 = createNormalCommodity(20.000000d, mvc, sessionBoss);
			normalCommodity9 = createNormalCommodity(30.000000d, mvc, sessionBoss);
			normalCommodity10 = createNormalCommodity(40.000000d, mvc, sessionBoss);
			normalCommodity11 = createNormalCommodity(100.000000d, mvc, sessionBoss);
			// 创建组合商品
			Commodity[] subCommodityList = { normalCommodity2, normalCommodity3 };
			Integer[] subCommodityNOList = { 2, 3 };
			Double[] subCommodityPriceList = { 3.000000d, 5.000000d };
			subCommodity = createSubCommodity(subCommodityList, subCommodityNOList, subCommodityPriceList, 16.880000d, mvc, sessionBoss);
			// 创建服务商品
			serviceCommodity1 = createServiceCommodity(2.000000d, mvc, sessionBoss);
			serviceCommodity2 = createServiceCommodity(1.000000d, mvc, sessionBoss);
			// 创建带多包装商品的单品normalCommodity5和multiPackageCommodity1
			int mulitple = 2;
			double mulitplePrice = 23.130000d;
			double price = 8.000000d;
			Commodity commB = BaseCommodityTest.DataInput.getCommodity();
			commB.setPriceRetail(price);
			commB.setPriceVIP(price);
			commB.setPriceWholesale(price);
			String providerIDs = "1,2";
			String multiPackagingInfo = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ";1," + mulitple + ";1,2;4.88," + mulitplePrice + ";4.88,13.76;8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodity5 = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs, multiPackagingInfo, commB);
			List<BaseModel> commodityList = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodity5);
			multiPackageCommodity1 = (Commodity) commodityList.get(0);
			// 创建带多包装商品的单品normalCommodity6和multiPackageCommodity2
			int mulitple2 = 2;
			double mulitplePrice2 = 23.130000d;
			double price2 = 8.000000d;
			Commodity commB2 = BaseCommodityTest.DataInput.getCommodity();
			commB2.setPriceRetail(price2);
			commB2.setPriceVIP(price2);
			commB2.setPriceWholesale(price2);
			String providerIDs2 = "1,2";
			String multiPackagingInfo2 = "111" + System.currentTimeMillis() % 1000000 + "," + "222" + System.currentTimeMillis() % 1000000 + ";1," + mulitple2 + ";1,2;4.88," + mulitplePrice2 + ";4.88,13.76;8,8;" //
					+ "普通商品B" + System.currentTimeMillis() % 1000000 + "," + "多包装商品B1" + System.currentTimeMillis() % 1000000 + ";";
			normalCommodity6 = createCommodityIncludeMultiPackages(mvc, sessionBoss, providerIDs2, multiPackagingInfo2, commB2);
			List<BaseModel> commodityList2 = BaseCommodityTest.queryMultiPackagingCommodityListViaMapper(normalCommodity6);
			multiPackageCommodity2 = (Commodity) commodityList2.get(0);

			// 设置商品的Barcode
			Commodity[] cList = { normalCommodity1, normalCommodity2, normalCommodity3, normalCommodity4, normalCommodity5, normalCommodity6, normalCommodity7, normalCommodity8, normalCommodity9, normalCommodity10, normalCommodity11,
					subCommodity, serviceCommodity1, serviceCommodity2, multiPackageCommodity1, multiPackageCommodity2 };
			setCommodityListBarcode(cList);
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

	@Test // 1、对10种以上不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机销售（不退货），查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest1() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1, normalCommodity2 };
		int[] iSaleCommodityNO1 = { 1, 2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { normalCommodity3, subCommodity };
		int[] iSaleCommodityNO2 = { 3, 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] commoditys3 = { multiPackageCommodity1, serviceCommodity1 };
		int[] iSaleCommodityNO3 = { 3, 2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		//
		Commodity[] commoditys4 = { normalCommodity7, normalCommodity8 };
		int[] iSaleCommodityNO4 = { 1, 2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO4, commoditys4);
		//
		Commodity[] commoditys5 = { normalCommodity9, normalCommodity10, normalCommodity11 };
		int[] iSaleCommodityNO5 = { 2, 3, 6 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO5, commoditys5);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity11.getName(), multiPackageCommodity1.getName(), normalCommodity10.getName(), normalCommodity3.getName(), serviceCommodity1.getName(), normalCommodity9.getName(),
				normalCommodity8.getName(), normalCommodity2.getName(), subCommodity.getName(), normalCommodity7.getName(), normalCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 6, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1 };

		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 1), 26/*当天销售商品总数*/, normalCommodity11.getName()/*当天销售排行最高的商品*/, 6/*当天销售排行最高商品的数量*/, normalCommodity1.getName()/*当天销售排行最低的商品*/, 1/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 2、对10种以下不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机销售（不退货），查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest2() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1 };
		int[] iSaleCommodityNO1 = { 5 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { normalCommodity2, normalCommodity8 };
		int[] iSaleCommodityNO2 = { 6, 2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] commoditys3 = { normalCommodity9, normalCommodity10, normalCommodity11 };
		int[] iSaleCommodityNO3 = { 4, 7, 9 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity11.getName(), normalCommodity10.getName(), normalCommodity2.getName(), normalCommodity1.getName(), normalCommodity9.getName(), normalCommodity8.getName() };
		// 销售的商品数量
		int[] commNOList = { 9, 7, 6, 5, 4, 2 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 2), 33/*当天销售商品总数*/, normalCommodity11.getName()/*当天销售排行最高的商品*/, 9/*当天销售排行最高商品的数量*/, normalCommodity8.getName()/*当天销售排行最低的商品*/, 2/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 3、对10种不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中至少一种商品只退货（全部退货），查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest3() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1, normalCommodity2, normalCommodity3 };
		int[] iSaleCommodityNO1 = { 1, 2, 3 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { subCommodity };
		int[] iSaleCommodityNO2 = { 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] commoditys3 = { multiPackageCommodity2, serviceCommodity2 };
		int[] iSaleCommodityNO3 = { 1, 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		//
		Commodity[] commoditys4 = { normalCommodity4, normalCommodity5, normalCommodity6 };
		int[] iSaleCommodityNO4 = { 1, 6, 2 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO4, commoditys4);
		//
		int[] iReturnSaleCommodityNO = { 6 };
		Commodity[] returnCommoditys = { normalCommodity5 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		//
		Commodity[] commoditys5 = { normalCommodity10, normalCommodity11 };
		int[] iSaleCommodityNO5 = { 3, 3 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO5, commoditys5);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity5.getName(), normalCommodity11.getName(), normalCommodity10.getName(), normalCommodity3.getName(), normalCommodity6.getName(), normalCommodity2.getName(),
				multiPackageCommodity2.getName(), serviceCommodity2.getName(), subCommodity.getName(), normalCommodity4.getName(), normalCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 6, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 3), 24/*当天销售商品总数*/, normalCommodity5.getName()/*当天销售排行最高的商品*/, 6/*当天销售排行最高商品的数量*/, normalCommodity1.getName()/*当天销售排行最低的商品*/, 1/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 4、对10种不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中至少一种商品只退货（部分退货），查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest4() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1, normalCommodity2, normalCommodity3 };
		int[] iSaleCommodityNO1 = { 1, 2, 3 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { normalCommodity4, normalCommodity5, normalCommodity6 };
		int[] iSaleCommodityNO2 = { 4, 5, 6 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] commoditys3 = { normalCommodity7, normalCommodity8 };
		int[] iSaleCommodityNO3 = { 7, 8 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		//
		Commodity[] commoditys4 = { normalCommodity9, normalCommodity10 };
		int[] iSaleCommodityNO4 = { 9, 10 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO4, commoditys4);
		//
		int[] iReturnSaleCommodityNO = { 1 };
		Commodity[] returnCommoditys = { normalCommodity10 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		//
		Commodity[] commoditys5 = { normalCommodity1 };
		int[] iSaleCommodityNO5 = { 10 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO5, commoditys5);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity1.getName(), normalCommodity10.getName(), normalCommodity9.getName(), normalCommodity8.getName(), normalCommodity7.getName(), normalCommodity6.getName(), normalCommodity5.getName(),
				normalCommodity4.getName(), normalCommodity3.getName(), normalCommodity2.getName() };
		//
		int[] commNOList = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		// 销售的商品数量
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 4), 65/*当天销售商品总数*/, normalCommodity1.getName()/*当天销售排行最高的商品*/, 11/*当天销售排行最高商品的数量*/, normalCommodity2.getName()/*当天销售排行最低的商品*/, 2/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 5、对10种不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中10种商品都进行部分退货，查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest5() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1, normalCommodity2, normalCommodity3 };
		int[] iSaleCommodityNO1 = { 2, 2, 3 };
		RetailTrade rt1 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] returnCommoditys1 = { normalCommodity1, normalCommodity2, normalCommodity3 };
		int[] iReturnSaleCommodityNO1 = { 1, 1, 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt1, staff, mvc, sessionBoss, iReturnSaleCommodityNO1, returnCommoditys1);
		//
		Commodity[] commoditys2 = { normalCommodity4, normalCommodity5, normalCommodity6 };
		int[] iSaleCommodityNO2 = { 4, 5, 6 };
		RetailTrade rt2 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] returnCommoditys2 = { normalCommodity4, normalCommodity5, normalCommodity6 };
		int[] iReturnSaleCommodityNO2 = { 1, 1, 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt2, staff, mvc, sessionBoss, iReturnSaleCommodityNO2, returnCommoditys2);
		//
		Commodity[] commoditys3 = { multiPackageCommodity1 };
		int[] iSaleCommodityNO3 = { 1 };
		RetailTrade rt3 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		//
		Commodity[] returnCommoditys3 = { multiPackageCommodity1 };
		int[] iReturnSaleCommodityNO3 = { 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt3, staff, mvc, sessionBoss, iReturnSaleCommodityNO3, returnCommoditys3);
		//
		Commodity[] commoditys4 = { multiPackageCommodity2 };
		int[] iSaleCommodityNO4 = { 1 };
		RetailTrade rt4 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO4, commoditys4);
		//
		Commodity[] returnCommoditys4 = { multiPackageCommodity2 };
		int[] iReturnSaleCommodityNO4 = { 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt4, staff, mvc, sessionBoss, iReturnSaleCommodityNO4, returnCommoditys4);
		//
		Commodity[] commoditys5 = { serviceCommodity1, normalCommodity10, normalCommodity11 };
		int[] iSaleCommodityNO5 = { 3, 6, 7 };
		RetailTrade rt5 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO5, commoditys5);
		//
		Commodity[] returnCommoditys5 = { serviceCommodity1, normalCommodity10, normalCommodity11 };
		int[] iReturnSaleCommodityNO5 = { 1, 1, 6 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), rt5, staff, mvc, sessionBoss, iReturnSaleCommodityNO5, returnCommoditys5);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity11.getName(), normalCommodity6.getName(), normalCommodity10.getName(), normalCommodity5.getName(), normalCommodity4.getName(), serviceCommodity1.getName(), normalCommodity3.getName(),
				normalCommodity2.getName(), normalCommodity1.getName(), multiPackageCommodity2.getName(), multiPackageCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 7, 6, 6, 5, 4, 3, 3, 2, 2, 1, 1 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 5), 40/*当天销售商品总数*/, normalCommodity11.getName()/*当天销售排行最高的商品*/, 7/*当天销售排行最高商品的数量*/, multiPackageCommodity1.getName()/*当天销售排行最低的商品*/,
				1/*当天销售排行最低商品的数量*/, commodityNameList, commNOList);
	}

	@Test // 6、对10种不同的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中10种商品有些进行全部退货，有些部分退货，有些不退货，查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest6() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1, normalCommodity2, normalCommodity3, normalCommodity4, normalCommodity5, normalCommodity6 };
		int[] iSaleCommodityNO1 = { 1, 2, 5, 6, 3, 5 };
		staff.setID(2);
		RetailTrade rt1 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] returnCommoditys1 = { normalCommodity2, normalCommodity6 };
		int[] iReturnSaleCommodityNO1 = { 1, 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), rt1, staff, mvc, sessionBoss, iReturnSaleCommodityNO1, returnCommoditys1);
		//
		Commodity[] commoditys2 = { multiPackageCommodity1, multiPackageCommodity2, serviceCommodity1 };
		int[] iSaleCommodityNO2 = { 7, 8, 7 };
		staff.setID(4);
		RetailTrade rt2 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] returnCommoditys2 = { multiPackageCommodity1, multiPackageCommodity2 };
		int[] iReturnSaleCommodityNO2 = { 7, 8 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), rt2, staff, mvc, sessionBoss, iReturnSaleCommodityNO2, returnCommoditys2);
		//
		Commodity[] commoditys3 = { normalCommodity10, normalCommodity11, subCommodity };
		int[] iSaleCommodityNO3 = { 5, 8, 5 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO3, commoditys3);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { multiPackageCommodity2.getName(), normalCommodity11.getName(), multiPackageCommodity1.getName(), serviceCommodity1.getName(), normalCommodity4.getName(), normalCommodity6.getName(),
				subCommodity.getName(), normalCommodity10.getName(), normalCommodity3.getName(), normalCommodity5.getName(), normalCommodity2.getName(), normalCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 8, 8, 7, 7, 6, 5, 5, 5, 5, 3, 2, 1 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 6), 62/*当天销售商品总数*/, multiPackageCommodity2.getName()/*当天销售排行最高的商品*/, 8/*当天销售排行最高商品的数量*/, normalCommodity1.getName()/*当天销售排行最低的商品*/,
				1/*当天销售排行最低商品的数量*/, commodityNameList, commNOList);
	}

	@Test // 7、对10种以下的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中至少一种商品只退货（部分退货），查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest7() throws ParseException, Exception {
		Commodity[] commoditys1 = { multiPackageCommodity1, multiPackageCommodity2 };
		int[] iSaleCommodityNO1 = { 7, 8 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { serviceCommodity1, subCommodity, serviceCommodity2 };
		int[] iSaleCommodityNO2 = { 7, 5, 12 };
		RetailTrade rt2 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] returnCommoditys = { serviceCommodity1 };
		int[] iReturnSaleCommodityNO = { 1 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), rt2, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { serviceCommodity2.getName(), multiPackageCommodity2.getName(), multiPackageCommodity1.getName(), serviceCommodity1.getName(), subCommodity.getName() };
		// 销售的商品数量
		int[] commNOList = { 12, 8, 7, 7, 5 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 7), 39/*当天销售商品总数*/, serviceCommodity2.getName()/*当天销售排行最高的商品*/, 12/*当天销售排行最高商品的数量*/, subCommodity.getName()/*当天销售排行最低的商品*/, 5/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 8、对10种以下的商品（其中商品可以有单品、组合商品、多包装商品、服务型商品）进行随机退货和销售，其中有些商品进行全部退货，查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest8() throws ParseException, Exception {
		Commodity[] commoditys1 = { subCommodity, normalCommodity2, normalCommodity3 };
		int[] iSaleCommodityNO1 = { 5, 10, 10 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] commoditys2 = { serviceCommodity1, serviceCommodity2 };
		int[] iSaleCommodityNO2 = { 11, 12 };
		RetailTrade rt2 = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO2, commoditys2);
		//
		Commodity[] returnCommoditys = { serviceCommodity2 };
		int[] iReturnSaleCommodityNO = { 12 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), rt2, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { serviceCommodity2.getName(), serviceCommodity1.getName(), normalCommodity3.getName(), normalCommodity2.getName(), subCommodity.getName() };
		// 销售的商品数量
		int[] commNOList = { 12, 11, 10, 10, 5 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 8), 48/*当天销售商品总数*/, serviceCommodity2.getName()/*当天销售排行最高的商品*/, 12/*当天销售排行最高商品的数量*/, subCommodity.getName()/*当天销售排行最低的商品*/, 5/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 9、只有一种商品发生销售，查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest9() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1 };
		int[] iSaleCommodityNO1 = { 10 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 10 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 9), 10/*当天销售商品总数*/, normalCommodity1.getName()/*当天销售排行最高的商品*/, 10/*当天销售排行最高商品的数量*/, normalCommodity1.getName()/*当天销售排行最低的商品*/, 10/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 10.只有一种商品发生退货（隔天退货）
	public void retailTradeDailyReportByCommodityTest10() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity1 };
		int[] iSaleCommodityNO1 = { 10 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] returnCommoditys = { normalCommodity1 };
		int[] iReturnSaleCommodityNO = { 2 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity1.getName() };
		// 销售的商品数量
		int[] commNOList = { 10 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 10), 10/*当天销售商品总数*/, normalCommodity1.getName()/*当天销售排行最高的商品*/, 10/*当天销售排行最高商品的数量*/, normalCommodity1.getName()/*当天销售排行最低的商品*/, 10/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@Test // 11.只有一种商品发生销售和退货(当天退货)，查看数据统计和报表显示是否正常。
	public void retailTradeDailyReportByCommodityTest11() throws ParseException, Exception {
		Commodity[] commoditys1 = { normalCommodity3 };
		int[] iSaleCommodityNO1 = { 10 };
		RetailTrade rt = createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), new RetailTrade(), staff, mvc, sessionBoss, iSaleCommodityNO1, commoditys1);
		//
		Commodity[] returnCommoditys = { normalCommodity3 };
		int[] iReturnSaleCommodityNO = { 3 };
		createRetailTrade(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), rt, staff, mvc, sessionBoss, iReturnSaleCommodityNO, returnCommoditys);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		// 销售的商品名称
		String[] commodityNameList = { normalCommodity3.getName() };
		// 销售的商品数量
		int[] commNOList = { 10 };
		//
		verifyRetailTradeDailyReportByCommodity(DatetimeUtil.getDays(sdf.parse(SALE_DATE), 11), 10/*当天销售商品总数*/, normalCommodity3.getName()/*当天销售排行最高的商品*/, 10/*当天销售排行最高商品的数量*/, normalCommodity3.getName()/*当天销售排行最低的商品*/, 10/*当天销售排行最低商品的数量*/,
				commodityNameList, commNOList);
	}

	@SuppressWarnings("unchecked")
	public void verifyRetailTradeDailyReportByCommodity(Date date, int allCommodityNO, String topCommodityName, int topCommodityNO, String lowCommodityName, int lowCommodityNO, String[] commNameList, int[] commNOList) {
		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(date);
		retailTradeDailyReport.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		// 创建日报表
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeDailyReportBO.createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
		if (retailTradeDailyReportBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "创建日报表出错，错误信息：" + retailTradeDailyReportBO.printErrorInfo());
		}
		// 查询当天商品报表
		RetailTradeDailyReportByCommodity retailTradeDailyReportByCommodity = new RetailTradeDailyReportByCommodity();
		retailTradeDailyReportByCommodity.setDatetimeStart(DatetimeUtil.getStartTime(date));
		retailTradeDailyReportByCommodity.setDatetimeEnd(DatetimeUtil.getEndTime(date));
		retailTradeDailyReportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
		retailTradeDailyReportByCommodity.setiOrderBy(EnumBoolean.EB_Yes.getIndex());
		retailTradeDailyReportByCommodity.setPageSize(BaseAction.PAGE_SIZE_MAX);
		// 根据提供的数据按商品售出数量降序，如果数量相同再根据商品ID降序
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<RetailTradeDailyReportByCommodity> retailTradeDailyReportByCommodityList = (List<RetailTradeDailyReportByCommodity>) retailTradeDailyReportByCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
				retailTradeDailyReportByCommodity);
		if (retailTradeDailyReportByCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询商品报表出错，错误信息：" + retailTradeDailyReportByCommodityBO.printErrorInfo());
		}
		// 当天销售商品数量
		int totalNO = 0;
		int length = retailTradeDailyReportByCommodityList.size();
		for (int i = 0; i < length; i++) {
			totalNO += retailTradeDailyReportByCommodityList.get(i).getNO();
			System.out.println(retailTradeDailyReportByCommodityList.get(i).getName() + "\t" + commNameList[i] + "\t" + retailTradeDailyReportByCommodityList.get(i).getNO() + "\t" + commNOList[i]);
			Assert.assertTrue(retailTradeDailyReportByCommodityList.get(i).getName().equals(commNameList[i]) && retailTradeDailyReportByCommodityList.get(i).getNO() == commNOList[i], "排序有误！");
		}
		Assert.assertTrue(totalNO == allCommodityNO, "当天销售商品总数不正确！");
		Assert.assertTrue(retailTradeDailyReportByCommodityList.get(0).getName().equals(topCommodityName), "当天销售最高商品名称不正确！");
		Assert.assertTrue(retailTradeDailyReportByCommodityList.get(0).getNO() == topCommodityNO, "当天销售排行最高的商品数量不正确！");
		Assert.assertTrue(retailTradeDailyReportByCommodityList.get(length - 1).getName().equals(lowCommodityName), "当天销售最低商品名称不正确！");
		Assert.assertTrue(retailTradeDailyReportByCommodityList.get(length - 1).getNO() == lowCommodityNO, "当天销售排行最低的商品数量不正确！");
	}

}
