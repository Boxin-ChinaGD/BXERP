package com.bx.erp.test;

import org.testng.annotations.BeforeClass;

import com.bx.erp.action.AuthenticationAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BarcodesSyncCacheDispatcherBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.PosSyncCacheBO;
import com.bx.erp.action.bo.PosSyncCacheDispatcherBO;
import com.bx.erp.action.bo.RetailTradeBO;
import com.bx.erp.action.bo.RetailTradeCommodityBO;
import com.bx.erp.action.bo.RetailTradeCommoditySourceBO;
import com.bx.erp.action.bo.RetailTradeCouponBO;
import com.bx.erp.action.bo.ReturnCommoditySheetCommodityBO;
import com.bx.erp.action.bo.ReturnRetailtradeCommoditydDestinationBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.SmallSheetFrameBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.action.bo.VipCategoryBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheDispatcherBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheDispatcherBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheDispatcherBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportByCategoryParentBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportByCommodityBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportByStaffBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportSummaryBO;
import com.bx.erp.action.bo.report.RetailTradeMonthlyReportSummaryBO;
import com.bx.erp.action.bo.trade.PromotionBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheDispatcherBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.model.Vip;
import com.bx.erp.model.TaskType.EnumTaskType;
import com.bx.erp.task.TaskManager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;

public class BaseActionTest extends BaseMapperTest {

	@Resource
	protected WebApplicationContext wac;
	protected MockMvc mvc;
	protected static MockHttpSession sessionBoss;
	protected static MockHttpSession sessionShopManager;
	protected static MockHttpSession sessionPreSale;
	protected static MockHttpSession sessionCashier;
	protected static MockHttpSession sessionOP;
	protected static MockHttpSession sessionVip;
	protected static MockHttpSession sessionPOS;
	/** 为测试提供BO */
	@Resource
	protected CommodityBO commodityBO;
	@Resource
	protected SubCommodityBO subCommodityBO;
	@Resource
	protected PosBO posBO;
	@Resource
	protected BarcodesBO barcodesBO;
	@Resource
	protected WarehousingBO warehousingBO;
	@Resource
	protected WarehousingCommodityBO warehousingCommodityBO;
	@Resource
	protected ProviderCommodityBO providerCommodityBO;
	@Resource
	protected PackageUnitBO packageUnitBO;
	@Resource
	protected CategoryBO categoryBO;
	@Resource
	protected CommodityHistoryBO commodityHistoryBO;
	@Resource
	protected CommoditySyncCacheBO commoditySyncCacheBO;
	@Resource
	protected BarcodesSyncCacheBO barcodesSyncCacheBO;
	@Resource
	protected BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO;
	@Resource
	protected CommoditySyncCacheDispatcherBO commoditySyncCacheDispatcherBO;
	@Resource
	protected ReturnCommoditySheetCommodityBO returnCommoditySheetCommodityBO;
	@Resource
	protected CategoryParentBO categoryParentBO;
	@Resource
	protected CategorySyncCacheBO categorySyncCacheBO;
	@Resource
	protected MessageBO messageBO;
	@Resource
	protected PromotionBO promotionBO;
	@Resource
	protected PromotionSyncCacheBO promotionSyncCacheBO;
	@Resource
	protected PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO;
	@Resource
	protected StaffBO staffBO;
	@Resource
	protected StaffRoleBO staffRoleBO;
	@Resource
	protected PurchasingOrderBO purchasingOrderBO;
	@Resource
	protected RetailTradeCommoditySourceBO retailTradeCommoditySourceBO;
	@Resource
	protected RetailTradeBO retailTradeBO;
	@Resource
	protected RetailTradePromotingBO retailTradePromotingBO;
	@Resource
	protected RetailTradeCommodityBO retailTradeCommodityBO;
	@Resource
	protected RetailTradePromotingFlowBO retailTradePromotingFlowBO;
	@Resource
	protected ShopBO shopBO;
	@Resource
	protected CompanyBO companyBO;
	@Resource
	protected PosSyncCacheBO posSyncCacheBO;
	@Resource
	protected PosSyncCacheDispatcherBO posSyncCacheDispatcherBO;
	@Resource
	protected RetailTradeDailyReportSummaryBO retailTradeDailyReportSummaryBO;
	@Resource
	protected BrandSyncCacheBO brandSyncCacheBO;
	@Resource
	protected BrandSyncCacheDispatcherBO brandSyncCacheDispatcherBO;
	@Resource
	protected BrandBO brandBO;
	@Resource
	protected CategorySyncCacheDispatcherBO categorySyncCacheDispatcherBO;
	@Resource
	protected ProviderDistrictBO providerDistrictBO;
	@Resource
	protected ProviderBO providerBO;
	@Resource
	protected RetailTradeMonthlyReportSummaryBO retailTradeMonthlyReportSummaryBO;
	@Resource
	protected RetailTradeDailyReportByCommodityBO retailTradeDailyReportByCommodityBO;
	@Resource
	protected RoleBO roleBO;
	@Resource
	protected VipBO vipBO;
	@Resource
	protected SmallSheetFrameBO smallSheetFrameBO;
	@Resource
	protected VipCategoryBO vipCategoryBO;
	@Resource
	protected RetailTradeDailyReportByCategoryParentBO retailTradeDailyReportByCategoryParentBO;
	@Resource
	protected RetailTradeDailyReportByStaffBO retailTradeDailyReportByStaffBO;
	@Resource
	protected ReturnRetailtradeCommoditydDestinationBO returnRetailtradeCommoditydDestinationBO;
	@Resource
	protected RetailTradeDailyReportBO retailTradeDailyReportBO;
	@Resource
	protected RetailTradeCouponBO retailTradeCouponBO;

	/** 用于存放BO，方便测试调用 */
	protected Map<String, BaseBO> mapBO;
	
	/** 夜间任务隔多少时间检查一下是否到达运行时间 */
	protected int timeSpanToCheckTaskStartTime;

	@BeforeClass
	public void setUp() {
		super.setUp();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		TaskManager.getTaskScheduler().setMockingMVC(true);
		timeSpanToCheckTaskStartTime = TaskManager.getCache(EnumTaskType.ETT_PurchasingTimeout).getTimeSpanToCheckTaskStartTime(); // 这里随便用一个任务的时间都OK，因为所有任务的间隔都一样
		
		try {
			AuthenticationAction.resetLoginCount(); // 防止登录过多导致测试失败。某些测试如果需要登录多次，需要在测试方法内部call此接口
			
			if (sessionBoss == null) {
				sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
			}
			if (sessionShopManager == null) {
				sessionShopManager = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
			}	
			if (sessionCashier == null) {
				sessionCashier = Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
			}
			if (sessionOP == null) {
				sessionOP = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
			}
			if (sessionVip == null) {
				Vip vip = new Vip();
				vip.setName(Shared.DEFAULT_VIP_Name);
				vip.setMobile(Shared.DEFAULT_VIP_Mobile);
				sessionVip = Shared.getVipLoginSession(mvc, vip, true);
			}
			if (sessionPOS == null) {
				sessionPOS = Shared.getPosLoginSession(mvc, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		initBOMap();
	}

	private void initBOMap() {
		mapBO = new HashMap<String, BaseBO>();
		mapBO.put(CommodityBO.class.getSimpleName(), commodityBO);
		mapBO.put(SubCommodityBO.class.getSimpleName(), subCommodityBO);
		mapBO.put(PosBO.class.getSimpleName(), posBO);
		mapBO.put(BarcodesBO.class.getSimpleName(), barcodesBO);
		mapBO.put(WarehousingBO.class.getSimpleName(), warehousingBO);
		mapBO.put(WarehousingCommodityBO.class.getSimpleName(), warehousingCommodityBO);
		mapBO.put(ProviderCommodityBO.class.getSimpleName(), providerCommodityBO);
		mapBO.put(PackageUnitBO.class.getSimpleName(), packageUnitBO);
		mapBO.put(CategoryBO.class.getSimpleName(), categoryBO);
		mapBO.put(CommodityHistoryBO.class.getSimpleName(), commodityHistoryBO);
		mapBO.put(CommoditySyncCacheBO.class.getSimpleName(), commoditySyncCacheBO);
		mapBO.put(BarcodesSyncCacheBO.class.getSimpleName(), barcodesSyncCacheBO);
		mapBO.put(BarcodesSyncCacheDispatcherBO.class.getSimpleName(), barcodesSyncCacheDispatcherBO);
		mapBO.put(CommoditySyncCacheDispatcherBO.class.getSimpleName(), commoditySyncCacheDispatcherBO);
		mapBO.put(ReturnCommoditySheetCommodityBO.class.getSimpleName(), returnCommoditySheetCommodityBO);
		mapBO.put(CategoryParentBO.class.getSimpleName(), categoryParentBO);
		mapBO.put(CategorySyncCacheBO.class.getSimpleName(), categorySyncCacheBO);
		mapBO.put(MessageBO.class.getSimpleName(), messageBO);
		mapBO.put(PromotionSyncCacheBO.class.getSimpleName(), promotionSyncCacheBO);
		mapBO.put(PromotionSyncCacheDispatcherBO.class.getSimpleName(), promotionSyncCacheDispatcherBO);
		mapBO.put(StaffBO.class.getSimpleName(), staffBO);
		mapBO.put(StaffRoleBO.class.getSimpleName(), staffRoleBO);
		mapBO.put(PurchasingOrderBO.class.getSimpleName(), purchasingOrderBO);
		mapBO.put(RetailTradeCommoditySourceBO.class.getSimpleName(), retailTradeCommoditySourceBO);
		mapBO.put(RetailTradeBO.class.getSimpleName(), retailTradeBO);
		mapBO.put(RetailTradePromotingBO.class.getSimpleName(), retailTradePromotingBO);
		mapBO.put(RetailTradeCommodityBO.class.getSimpleName(), retailTradeCommodityBO);
		mapBO.put(RetailTradePromotingFlowBO.class.getSimpleName(), retailTradePromotingFlowBO);
		mapBO.put(ShopBO.class.getSimpleName(), shopBO);
		mapBO.put(CompanyBO.class.getSimpleName(), companyBO);
		mapBO.put(PosSyncCacheBO.class.getSimpleName(), posSyncCacheBO);
		mapBO.put(PosSyncCacheDispatcherBO.class.getSimpleName(), posSyncCacheDispatcherBO);
		mapBO.put(RetailTradeDailyReportSummaryBO.class.getSimpleName(), retailTradeDailyReportSummaryBO);
		mapBO.put(BrandSyncCacheBO.class.getSimpleName(), brandSyncCacheBO);
		mapBO.put(BrandSyncCacheDispatcherBO.class.getSimpleName(), brandSyncCacheDispatcherBO);
		mapBO.put(BrandBO.class.getSimpleName(), brandBO);
		mapBO.put(CategorySyncCacheDispatcherBO.class.getSimpleName(), categorySyncCacheDispatcherBO);
		mapBO.put(ProviderDistrictBO.class.getSimpleName(), providerDistrictBO);
		mapBO.put(ProviderBO.class.getSimpleName(), providerBO);
		mapBO.put(RetailTradeMonthlyReportSummaryBO.class.getSimpleName(), retailTradeMonthlyReportSummaryBO);
		mapBO.put(RetailTradeDailyReportByCommodityBO.class.getSimpleName(), retailTradeDailyReportByCommodityBO);
		mapBO.put(RoleBO.class.getSimpleName(), roleBO);
		mapBO.put(PromotionBO.class.getSimpleName(), promotionBO);
		mapBO.put(SmallSheetFrameBO.class.getSimpleName(), smallSheetFrameBO);
		mapBO.put(VipCategoryBO.class.getSimpleName(), vipCategoryBO);
		mapBO.put(VipBO.class.getSimpleName(), vipBO);
		mapBO.put(RetailTradeDailyReportByCategoryParentBO.class.getSimpleName(), retailTradeDailyReportByCategoryParentBO);
		mapBO.put(RetailTradeDailyReportByStaffBO.class.getSimpleName(), retailTradeDailyReportByStaffBO);
		mapBO.put(ReturnRetailtradeCommoditydDestinationBO.class.getSimpleName(), returnRetailtradeCommoditydDestinationBO);
		mapBO.put(RetailTradeDailyReportBO.class.getSimpleName(), retailTradeDailyReportBO);
		mapBO.put(RetailTradeCouponBO.class.getSimpleName(), retailTradeCouponBO);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

}
