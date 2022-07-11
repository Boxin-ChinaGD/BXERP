package com.bx.erp.test;

import org.testng.annotations.BeforeClass;

import com.bx.erp.dao.BarcodesMapper;
import com.bx.erp.dao.BarcodesSyncCacheDispatcherMapper;
import com.bx.erp.dao.BarcodesSyncCacheMapper;
import com.bx.erp.dao.BonusConsumeHistoryMapper;
import com.bx.erp.dao.BonusRuleMapper;
import com.bx.erp.dao.BxConfigGeneralMapper;
import com.bx.erp.dao.BxStaffMapper;
import com.bx.erp.dao.CompanyMapper;
import com.bx.erp.dao.ConfigCacheSizeMapper;
import com.bx.erp.dao.ConfigGeneralMapper;
import com.bx.erp.dao.CouponCodeMapper;
import com.bx.erp.dao.CouponMapper;
import com.bx.erp.dao.CouponScopeMapper;
import com.bx.erp.dao.DepartmentMapper;
import com.bx.erp.dao.PermissionMapper;
import com.bx.erp.dao.PosMapper;
import com.bx.erp.dao.PosSyncCacheDispatcherMapper;
import com.bx.erp.dao.PosSyncCacheMapper;
import com.bx.erp.dao.RetailTradeAggregationMapper;
import com.bx.erp.dao.RetailTradeCommodityMapper;
import com.bx.erp.dao.RetailTradeCommoditySourceMapper;
import com.bx.erp.dao.RetailTradeCouponMapper;
import com.bx.erp.dao.RetailTradeMapper;
import com.bx.erp.dao.ReturnCommoditySheetCommodityMapper;
import com.bx.erp.dao.ReturnCommoditySheetMapper;
import com.bx.erp.dao.ReturnRetailtradeCommoditydDestinationMapper;
import com.bx.erp.dao.RoleMapper;
import com.bx.erp.dao.RolePermissionMapper;
import com.bx.erp.dao.ShopDistrictMapper;
import com.bx.erp.dao.ShopMapper;
import com.bx.erp.dao.SmallSheetFrameMapper;
import com.bx.erp.dao.SmallSheetTextMapper;
import com.bx.erp.dao.StaffBelongingMapper;
import com.bx.erp.dao.StaffMapper;
import com.bx.erp.dao.StaffPermissionMapper;
import com.bx.erp.dao.StaffRoleMapper;
import com.bx.erp.dao.VipCardCodeMapper;
import com.bx.erp.dao.VipCardMapper;
import com.bx.erp.dao.VipCategoryMapper;
import com.bx.erp.dao.VipMapper;
import com.bx.erp.dao.VipSourceMapper;
import com.bx.erp.dao.commodity.BrandMapper;
import com.bx.erp.dao.commodity.BrandSyncCacheDispatcherMapper;
import com.bx.erp.dao.commodity.BrandSyncCacheMapper;
import com.bx.erp.dao.commodity.CategoryMapper;
import com.bx.erp.dao.commodity.CategoryParentMapper;
import com.bx.erp.dao.commodity.CategorySyncCacheDispatcherMapper;
import com.bx.erp.dao.commodity.CategorySyncCacheMapper;
import com.bx.erp.dao.commodity.CommodityHistoryMapper;
import com.bx.erp.dao.commodity.CommodityMapper;
import com.bx.erp.dao.commodity.CommodityPropertyMapper;
import com.bx.erp.dao.commodity.CommodityShopInfoMapper;
import com.bx.erp.dao.commodity.CommoditySyncCacheDispatcherMapper;
import com.bx.erp.dao.commodity.CommoditySyncCacheMapper;
import com.bx.erp.dao.commodity.PackageUnitMapper;
import com.bx.erp.dao.commodity.RefCommodityHubMapper;
import com.bx.erp.dao.commodity.SubCommodityMapper;
import com.bx.erp.dao.message.MessageCategoryMapper;
import com.bx.erp.dao.message.MessageHandlerSettingMapper;
import com.bx.erp.dao.message.MessageItemMapper;
import com.bx.erp.dao.message.MessageMapper;
import com.bx.erp.dao.purchasing.ProviderCommodityMapper;
import com.bx.erp.dao.purchasing.ProviderDistrictMapper;
import com.bx.erp.dao.purchasing.ProviderMapper;
import com.bx.erp.dao.purchasing.PurchasingOrderCommodityMapper;
import com.bx.erp.dao.purchasing.PurchasingOrderMapper;
import com.bx.erp.dao.report.RetailTradeDailyReportByCategoryParentMapper;
import com.bx.erp.dao.report.RetailTradeDailyReportByCommodityMapper;
import com.bx.erp.dao.report.RetailTradeDailyReportByStaffMapper;
import com.bx.erp.dao.report.RetailTradeDailyReportMapper;
import com.bx.erp.dao.report.RetailTradeDailyReportSummaryMapper;
import com.bx.erp.dao.report.RetailTradeMonthlyReportSummaryMapper;
import com.bx.erp.dao.report.RetailTradeReportByCommodityMapper;
import com.bx.erp.dao.report.WarehousingReportMapper;
import com.bx.erp.dao.sm.StateMachineMapper;
import com.bx.erp.dao.trade.PromotionMapper;
import com.bx.erp.dao.trade.PromotionScopeMapper;
import com.bx.erp.dao.trade.PromotionShopScopeMapper;
import com.bx.erp.dao.trade.PromotionSyncCacheDispatcherMapper;
import com.bx.erp.dao.trade.PromotionSyncCacheMapper;
import com.bx.erp.dao.trade.RetailTradePromotingFlowMapper;
import com.bx.erp.dao.trade.RetailTradePromotingMapper;
import com.bx.erp.dao.warehousing.InventoryCommodityMapper;
import com.bx.erp.dao.warehousing.InventorySheetMapper;
import com.bx.erp.dao.warehousing.WarehouseMapper;
import com.bx.erp.dao.warehousing.WarehousingCommodityMapper;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.commodity.CommodityShopInfo;

//import com.bx.erp.dao.wx.WxUserMapper;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class BaseMapperTest extends BaseTestNGSpringContextTest {

	/** 为测试提供Mapper */
	protected static CommodityMapper commodityMapper;
	protected static CommodityShopInfoMapper commodityShopInfoMapper;
	protected static BarcodesMapper barcodesMapper;
	protected static BrandMapper brandMapper;
	protected static ProviderCommodityMapper providerCommodityMapper;
	protected static PurchasingOrderCommodityMapper purchasingOrderCommodityMapper;
	protected static RetailTradeMapper retailTradeMapper;
	protected static RetailTradeCommodityMapper retailTradeCommodityMapper;
	protected static SubCommodityMapper subCommodityMapper;
	protected static CommodityHistoryMapper commodityHistoryMapper;
	protected static CommoditySyncCacheMapper commoditySyncCacheMapper;
	protected static BarcodesSyncCacheMapper barcodesSyncCacheMapper;

	protected static ReturnCommoditySheetMapper returnCommoditySheetMapper;

	protected static ReturnCommoditySheetCommodityMapper returnCommoditySheetCommodityMapper;
	protected static WarehousingMapper warehousingMapper;
	protected static WarehousingCommodityMapper warehousingCommodityMapper;
	protected static CategoryParentMapper categoryParentMapper;
	protected static CategoryMapper categoryMapper;
	protected static CategorySyncCacheMapper categorySyncCacheMapper;
	protected static CategorySyncCacheDispatcherMapper categorySyncCacheDispatcherMapper;
	protected static PosMapper posMapper;
	protected static StaffMapper staffMapper;
	protected static PackageUnitMapper packageUnitMapper;
	protected static InventoryCommodityMapper inventoryCommodityMapper;
	protected static InventorySheetMapper inventorySheetMapper;
	protected static PromotionMapper promotionMapper;
	protected static PromotionScopeMapper promotionScopeMapper;
	protected static PromotionShopScopeMapper promotionShopScopeMapper;
	protected static RetailTradePromotingFlowMapper retailTradePromotingFlowMapper;
	protected static RetailTradePromotingMapper retailTradePromotingMapper;
	protected static PurchasingOrderMapper purchasingOrderMapper;
	protected static RetailTradeDailyReportByCategoryParentMapper retailTradeDailyReportByCategoryParentMapper;
	protected static RetailTradeDailyReportMapper retailTradeDailyReportMapper;
	protected static RetailTradeDailyReportSummaryMapper retailTradeDailyReportSummaryMapper;
	protected static RetailTradeDailyReportByStaffMapper retailTradeDailyReportByStaffMapper;
	protected static RetailTradeDailyReportByCommodityMapper retailTradeDailyReportByCommodityMapper;
	protected static RetailTradeReportByCommodityMapper retailTradeReportByCommodityMapper;
	protected static WarehouseMapper warehouseMapper;
	protected static ProviderMapper providerMapper;
	protected static RetailTradeCommoditySourceMapper retailTradeCommoditySourceMapper;
	protected static VipMapper vipMapper;
	protected static CompanyMapper companyMapper;
	protected static BrandSyncCacheDispatcherMapper brandSyncCacheDispatcherMapper;
	protected static BrandSyncCacheMapper brandSyncCacheMapper;
	protected static BxConfigGeneralMapper bxConfigGeneralMapper;
	protected static BxStaffMapper bxStaffMapper;
	protected static CommodityPropertyMapper commodityPropertyMapper;
	protected static CommoditySyncCacheDispatcherMapper commoditySyncCacheDispatcherMapper;
	protected static MessageHandlerSettingMapper messageHandlerSettingMapper;
	protected static MessageItemMapper messageItemMapper;
	protected static MessageMapper messageMapper;
	protected static PermissionMapper permissionMapper;
	protected static RolePermissionMapper rolePermissionMapper;
	protected static PromotionSyncCacheDispatcherMapper promotionSyncCacheDispatcherMapper;
	protected static PromotionSyncCacheMapper promotionSyncCacheMapper;
	protected static RefCommodityHubMapper refCommodityHubMapper;
	protected static RetailTradeAggregationMapper retailTradeAggregationMapper;
	protected static RetailTradeMonthlyReportSummaryMapper retailTradeMonthlyReportSummaryMapper;
	protected static RoleMapper roleMapper;
	protected static StaffRoleMapper staffRoleMapper;
	protected static ShopDistrictMapper shopDistrictMapper;
	protected static ShopMapper shopMapper;
	protected static SmallSheetFrameMapper smallSheetFrameMapper;
	protected static SmallSheetTextMapper smallSheetTextMapper;
	protected static WarehousingReportMapper warehousingReportMapper;
//	protected static WxUserMapper wxUserMapper;
	protected static MessageCategoryMapper messageCategoryMapper;
	protected static ProviderDistrictMapper providerDistrictMapper;
	protected static DepartmentMapper departmentMapper;
	protected static BarcodesSyncCacheDispatcherMapper barcodesSyncCacheDispatcherMapper;
	protected static ConfigCacheSizeMapper configCacheSizeMapper;
	protected static ConfigGeneralMapper configGeneralMapper;
	protected static PosSyncCacheDispatcherMapper posSyncCacheDispatcherMapper;
	protected static PosSyncCacheMapper posSyncCacheMapper;
	protected static StaffPermissionMapper staffPermissionMapper;
	protected static StateMachineMapper stateMachineMapper;
	protected static VipCategoryMapper vipCategoryMapper;
	protected static ReturnRetailtradeCommoditydDestinationMapper returnRetailtradeCommoditydDestinationMapper;
	protected static StaffBelongingMapper staffBelongingMapper;
	protected static BonusRuleMapper bonusRuleMapper;
	protected static BonusConsumeHistoryMapper bonusConsumeHistoryMapper;
	protected static CouponMapper couponMapper;
	protected static CouponCodeMapper couponCodeMapper;
	protected static CouponScopeMapper couponScopeMapper;
	protected static RetailTradeCouponMapper retailTradeCouponMapper;
	protected static VipCardMapper vipCardMapper;
	protected static VipCardCodeMapper vipCardCodeMapper;
	protected static VipSourceMapper vipSourceMapper;

	/** 用于存放Mapper，有些测试需要 */
	// protected Map<String, BaseMapper> mapMapper;

	protected static boolean bMapperIsInitialized = false;

	@BeforeClass
	public void setUp() {
		super.setup();

		if (!bMapperIsInitialized) {
			initMapper();
			// initMapMapper();
			bMapperIsInitialized = true;
		}
	}

	// private void initMapMapper() {
	// mapMapper = new HashMap<String, BaseMapper>();
	// mapMapper.put(CommodityMapper.class.getSimpleName(), commodityMapper);
	// mapMapper.put(BarcodesMapper.class.getSimpleName(), barcodesMapper);
	// mapMapper.put(BrandMapper.class.getSimpleName(), brandMapper);
	// mapMapper.put(ProviderCommodityMapper.class.getSimpleName(),
	// providerCommodityMapper);
	// mapMapper.put(PurchasingOrderCommodityMapper.class.getSimpleName(),
	// purchasingOrderCommodityMapper);
	// mapMapper.put(RetailTradeMapper.class.getSimpleName(), retailTradeMapper);
	// mapMapper.put(RetailTradeCommodityMapper.class.getSimpleName(),
	// retailTradeCommodityMapper);
	// mapMapper.put(SubCommodityMapper.class.getSimpleName(), subCommodityMapper);
	// mapMapper.put(CommodityHistoryMapper.class.getSimpleName(),
	// commodityHistoryMapper);
	// mapMapper.put(PromotionMapper.class.getSimpleName(), promotionMapper);
	// mapMapper.put(PromotionScopeMapper.class.getSimpleName(),
	// promotionScopeMapper);
	// mapMapper.put(CommoditySyncCacheMapper.class.getSimpleName(),
	// commoditySyncCacheMapper);
	// mapMapper.put(BarcodesSyncCacheMapper.class.getSimpleName(),
	// barcodesSyncCacheMapper);
	// mapMapper.put(ReturnCommoditySheetCommodityMapper.class.getSimpleName(),
	// returnCommoditySheetCommodityMapper);
	// mapMapper.put(ReturnCommoditySheetMapper.class.getSimpleName(),
	// returnCommoditySheetMapper);
	// mapMapper.put(WarehousingMapper.class.getSimpleName(), warehousingMapper);
	// mapMapper.put(WarehousingCommodityMapper.class.getSimpleName(),
	// warehousingCommodityMapper);
	// mapMapper.put(CategoryParentMapper.class.getSimpleName(),
	// categoryParentMapper);
	// mapMapper.put(CategoryMapper.class.getSimpleName(), categoryMapper);
	// mapMapper.put(PosMapper.class.getSimpleName(), posMapper);
	// mapMapper.put(CategorySyncCacheDispatcherMapper.class.getSimpleName(),
	// categorySyncCacheDispatcherMapper);
	// mapMapper.put(CategorySyncCacheMapper.class.getSimpleName(),
	// categorySyncCacheMapper);
	// mapMapper.put(StaffMapper.class.getSimpleName(), staffMapper);
	// mapMapper.put(PackageUnitMapper.class.getSimpleName(), packageUnitMapper);
	// mapMapper.put(InventoryCommodityMapper.class.getSimpleName(),
	// inventoryCommodityMapper);
	// mapMapper.put(InventorySheetMapper.class.getSimpleName(),
	// inventorySheetMapper);
	// mapMapper.put(RetailTradePromotingFlowMapper.class.getSimpleName(),
	// retailTradePromotingFlowMapper);
	// mapMapper.put(RetailTradePromotingMapper.class.getSimpleName(),
	// retailTradePromotingMapper);
	// mapMapper.put(PurchasingOrderMapper.class.getSimpleName(),
	// purchasingOrderMapper);
	// mapMapper.put(RetailTradeDailyReportByCategoryParentMapper.class.getSimpleName(),
	// retailTradeDailyReportByCategoryParentMapper);
	// mapMapper.put(RetailTradeDailyReportMapper.class.getSimpleName(),
	// retailTradeDailyReportMapper);
	// mapMapper.put(RetailTradeDailyReportSummaryMapper.class.getSimpleName(),
	// retailTradeDailyReportSummaryMapper);
	// mapMapper.put(RetailTradeDailyReportByStaffMapper.class.getSimpleName(),
	// retailTradeDailyReportByStaffMapper);
	// mapMapper.put(RetailTradeDailyReportByCommodityMapper.class.getSimpleName(),
	// retailTradeDailyReportByCommodityMapper);
	// mapMapper.put(RetailTradeReportByCommodityMapper.class.getSimpleName(),
	// retailTradeReportByCommodityMapper);
	// mapMapper.put(WarehouseMapper.class.getSimpleName(), warehouseMapper);
	// mapMapper.put(ProviderMapper.class.getSimpleName(), providerMapper);
	// mapMapper.put(RetailTradeCommoditySourceMapper.class.getSimpleName(),
	// retailTradeCommoditySourceMapper);
	// mapMapper.put(VipMapper.class.getSimpleName(), vipMapper);
	// mapMapper.put(CompanyMapper.class.getSimpleName(), companyMapper);
	// mapMapper.put(BrandSyncCacheMapper.class.getSimpleName(),
	// brandSyncCacheMapper);
	// mapMapper.put(BrandSyncCacheDispatcherMapper.class.getSimpleName(),
	// brandSyncCacheDispatcherMapper);
	// mapMapper.put(BxConfigGeneralMapper.class.getSimpleName(),
	// bxConfigGeneralMapper);
	// mapMapper.put(BxStaffMapper.class.getSimpleName(), bxStaffMapper);
	// mapMapper.put(CommodityPropertyMapper.class.getSimpleName(),
	// commodityPropertyMapper);
	// mapMapper.put(CommoditySyncCacheDispatcherMapper.class.getSimpleName(),
	// commoditySyncCacheDispatcherMapper);
	// mapMapper.put(MessageHandlerSettingMapper.class.getSimpleName(),
	// messageHandlerSettingMapper);
	// mapMapper.put(MessageItemMapper.class.getSimpleName(), messageItemMapper);
	// mapMapper.put(MessageMapper.class.getSimpleName(), messageMapper);
	// mapMapper.put(PermissionMapper.class.getSimpleName(), permissionMapper);
	// mapMapper.put(RolePermissionMapper.class.getSimpleName(),
	// rolePermissionMapper);
	// mapMapper.put(PromotionSyncCacheDispatcherMapper.class.getSimpleName(),
	// promotionSyncCacheDispatcherMapper);
	// mapMapper.put(PromotionSyncCacheMapper.class.getSimpleName(),
	// promotionSyncCacheMapper);
	// mapMapper.put(RefCommodityHubMapper.class.getSimpleName(),
	// refCommodityHubMapper);
	// mapMapper.put(RetailTradeAggregationMapper.class.getSimpleName(),
	// retailTradeAggregationMapper);
	// mapMapper.put(RetailTradeMonthlyReportSummaryMapper.class.getSimpleName(),
	// retailTradeMonthlyReportSummaryMapper);
	// mapMapper.put(RoleMapper.class.getSimpleName(), roleMapper);
	// mapMapper.put(ShopDistrictMapper.class.getSimpleName(), shopDistrictMapper);
	// mapMapper.put(ShopMapper.class.getSimpleName(), shopMapper);
	// mapMapper.put(SmallSheetFrameMapper.class.getSimpleName(),
	// smallSheetFrameMapper);
	// mapMapper.put(StaffRoleMapper.class.getSimpleName(), staffRoleMapper);
	// mapMapper.put(VipCategorySyncCacheMapper.class.getSimpleName(),
	// vipCategorySyncCacheMapper);
	// mapMapper.put(VipCategorySyncCacheDispatcherMapper.class.getSimpleName(),
	// vipCategorySyncCacheDispatcherMapper);
	// mapMapper.put(WarehousingReportMapper.class.getSimpleName(),
	// warehousingReportMapper);
	// mapMapper.put(WxUserMapper.class.getSimpleName(), wxUserMapper);
	// mapMapper.put(MessageCategoryMapper.class.getSimpleName(),
	// messageCategoryMapper);
	// mapMapper.put(ProviderDistrictMapper.class.getSimpleName(),
	// providerDistrictMapper);
	// mapMapper.put(DepartmentMapper.class.getSimpleName(), departmentMapper);
	// mapMapper.put(BarcodesSyncCacheDispatcherMapper.class.getSimpleName(),
	// barcodesSyncCacheDispatcherMapper);
	// mapMapper.put(ConfigCacheSizeMapper.class.getSimpleName(),
	// configCacheSizeMapper);
	// mapMapper.put(ConfigGeneralMapper.class.getSimpleName(),
	// configGeneralMapper);
	// mapMapper.put(ConfigGeneralSyncCacheDispatcherMapper.class.getSimpleName(),
	// configGeneralSyncCacheDispatcherMapper);
	// mapMapper.put(ConfigGeneralSyncCacheMapper.class.getSimpleName(),
	// configGeneralSyncCacheMapper);
	// mapMapper.put(PosSyncCacheDispatcherMapper.class.getSimpleName(),
	// posSyncCacheDispatcherMapper);
	// mapMapper.put(PosSyncCacheMapper.class.getSimpleName(), posSyncCacheMapper);
	// mapMapper.put(RetailTradePromotingSyncCacheDispatcherMapper.class.getSimpleName(),
	// retailTradePromotingSyncCacheDispatcherMapper);
	// mapMapper.put(RetailTradePromotingSyncCacheMapper.class.getSimpleName(),
	// retailTradePromotingSyncCacheMapper);
	// mapMapper.put(SmallSheetSyncCacheMapper.class.getSimpleName(),
	// smallSheetSyncCacheMapper);
	// mapMapper.put(SmallSheetSyncCacheDispatcherMapper.class.getSimpleName(),
	// smallSheetSyncCacheDispatcherMapper);
	// mapMapper.put(StaffPermissionMapper.class.getSimpleName(),
	// staffPermissionMapper);
	// mapMapper.put(StateMachineMapper.class.getSimpleName(), stateMachineMapper);
	// mapMapper.put(VipCategoryMapper.class.getSimpleName(), vipCategoryMapper);
	// mapMapper.put(VipSyncCacheDispatcherMapper.class.getSimpleName(),
	// vipSyncCacheDispatcherMapper);
	// mapMapper.put(VipSyncCacheMapper.class.getSimpleName(), vipSyncCacheMapper);
	// mapMapper.put(ReturnRetailtradeCommoditydDestinationMapper.class.getSimpleName(),
	// returnRetailtradeCommoditydDestinationMapper);
	// mapMapper.put(StaffBelongingMapper.class.getSimpleName(),
	// staffBelongingMapper);
	// }

	private void initMapper() {
		commodityMapper = (CommodityMapper) applicationContext.getBean("commodityMapper");
		commodityShopInfoMapper = (CommodityShopInfoMapper) applicationContext.getBean("commodityShopInfoMapper");
		barcodesMapper = (BarcodesMapper) applicationContext.getBean("barcodesMapper");
		brandMapper = (BrandMapper) applicationContext.getBean("brandMapper");
		providerCommodityMapper = (ProviderCommodityMapper) applicationContext.getBean("providerCommodityMapper");
		purchasingOrderCommodityMapper = (PurchasingOrderCommodityMapper) applicationContext.getBean("purchasingOrderCommodityMapper");
		retailTradeMapper = (RetailTradeMapper) applicationContext.getBean("retailTradeMapper");
		retailTradeCommodityMapper = (RetailTradeCommodityMapper) applicationContext.getBean("retailTradeCommodityMapper");
		subCommodityMapper = (SubCommodityMapper) applicationContext.getBean("subCommodityMapper");
		commodityHistoryMapper = (CommodityHistoryMapper) applicationContext.getBean("commodityHistoryMapper");
		promotionMapper = (PromotionMapper) applicationContext.getBean("promotionMapper");
		promotionScopeMapper = (PromotionScopeMapper) applicationContext.getBean("promotionScopeMapper");
		promotionShopScopeMapper = (PromotionShopScopeMapper) applicationContext.getBean("promotionShopScopeMapper");
		commoditySyncCacheMapper = (CommoditySyncCacheMapper) applicationContext.getBean("commoditySyncCacheMapper");
		barcodesSyncCacheMapper = (BarcodesSyncCacheMapper) applicationContext.getBean("barcodesSyncCacheMapper");
		returnCommoditySheetCommodityMapper = (ReturnCommoditySheetCommodityMapper) applicationContext.getBean("returnCommoditySheetCommodityMapper");
		returnCommoditySheetMapper = (ReturnCommoditySheetMapper) applicationContext.getBean("returnCommoditySheetMapper");
		warehousingMapper = (WarehousingMapper) applicationContext.getBean("warehousingMapper");
		warehousingCommodityMapper = (WarehousingCommodityMapper) applicationContext.getBean("warehousingCommodityMapper");
		categoryParentMapper = (CategoryParentMapper) applicationContext.getBean("categoryParentMapper");
		categoryMapper = (CategoryMapper) applicationContext.getBean("categoryMapper");
		posMapper = (PosMapper) applicationContext.getBean("posMapper");
		categorySyncCacheDispatcherMapper = (CategorySyncCacheDispatcherMapper) applicationContext.getBean("categorySyncCacheDispatcherMapper");
		categorySyncCacheMapper = (CategorySyncCacheMapper) applicationContext.getBean("categorySyncCacheMapper");
		staffMapper = (StaffMapper) applicationContext.getBean("staffMapper");
		packageUnitMapper = (PackageUnitMapper) applicationContext.getBean("packageUnitMapper");
		inventoryCommodityMapper = (InventoryCommodityMapper) applicationContext.getBean("inventoryCommodityMapper");
		inventorySheetMapper = (InventorySheetMapper) applicationContext.getBean("inventorySheetMapper");
		retailTradePromotingFlowMapper = (RetailTradePromotingFlowMapper) applicationContext.getBean("retailTradePromotingFlowMapper");
		retailTradePromotingMapper = (RetailTradePromotingMapper) applicationContext.getBean("retailTradePromotingMapper");
		purchasingOrderMapper = (PurchasingOrderMapper) applicationContext.getBean("purchasingOrderMapper");
		retailTradeDailyReportByCategoryParentMapper = (RetailTradeDailyReportByCategoryParentMapper) applicationContext.getBean("retailTradeDailyReportByCategoryParentMapper");
		retailTradeDailyReportMapper = (RetailTradeDailyReportMapper) applicationContext.getBean("retailTradeDailyReportMapper");
		retailTradeDailyReportSummaryMapper = (RetailTradeDailyReportSummaryMapper) applicationContext.getBean("retailTradeDailyReportSummaryMapper");
		retailTradeDailyReportByStaffMapper = (RetailTradeDailyReportByStaffMapper) applicationContext.getBean("retailTradeDailyReportByStaffMapper");
		retailTradeDailyReportByCommodityMapper = (RetailTradeDailyReportByCommodityMapper) applicationContext.getBean("retailTradeDailyReportByCommodityMapper");
		retailTradeReportByCommodityMapper = (RetailTradeReportByCommodityMapper) applicationContext.getBean("retailTradeReportByCommodityMapper");
		warehouseMapper = (WarehouseMapper) applicationContext.getBean("warehouseMapper");
		providerMapper = (ProviderMapper) applicationContext.getBean("providerMapper");
		retailTradeCommoditySourceMapper = (RetailTradeCommoditySourceMapper) applicationContext.getBean("retailTradeCommoditySourceMapper");
		vipMapper = (VipMapper) applicationContext.getBean("vipMapper");
		companyMapper = (CompanyMapper) applicationContext.getBean("companyMapper");
		brandSyncCacheDispatcherMapper = (BrandSyncCacheDispatcherMapper) applicationContext.getBean("brandSyncCacheDispatcherMapper");
		brandSyncCacheMapper = (BrandSyncCacheMapper) applicationContext.getBean("brandSyncCacheMapper");
		bxConfigGeneralMapper = (BxConfigGeneralMapper) applicationContext.getBean("bxConfigGeneralMapper");
		bxStaffMapper = (BxStaffMapper) applicationContext.getBean("bxStaffMapper");
		commodityPropertyMapper = (CommodityPropertyMapper) applicationContext.getBean("commodityPropertyMapper");
		commoditySyncCacheDispatcherMapper = (CommoditySyncCacheDispatcherMapper) applicationContext.getBean("commoditySyncCacheDispatcherMapper");
		messageHandlerSettingMapper = (MessageHandlerSettingMapper) applicationContext.getBean("messageHandlerSettingMapper");
		messageItemMapper = (MessageItemMapper) applicationContext.getBean("messageItemMapper");
		messageMapper = (MessageMapper) applicationContext.getBean("messageMapper");
		permissionMapper = (PermissionMapper) applicationContext.getBean("permissionMapper");
		rolePermissionMapper = (RolePermissionMapper) applicationContext.getBean("rolePermissionMapper");
		promotionSyncCacheDispatcherMapper = (PromotionSyncCacheDispatcherMapper) applicationContext.getBean("promotionSyncCacheDispatcherMapper");
		promotionSyncCacheMapper = (PromotionSyncCacheMapper) applicationContext.getBean("promotionSyncCacheMapper");
		refCommodityHubMapper = (RefCommodityHubMapper) applicationContext.getBean("refCommodityHubMapper");
		retailTradeAggregationMapper = (RetailTradeAggregationMapper) applicationContext.getBean("retailTradeAggregationMapper");
		retailTradeMonthlyReportSummaryMapper = (RetailTradeMonthlyReportSummaryMapper) applicationContext.getBean("retailTradeMonthlyReportSummaryMapper");
		roleMapper = (RoleMapper) applicationContext.getBean("roleMapper");
		staffRoleMapper = (StaffRoleMapper) applicationContext.getBean("staffRoleMapper");
		shopDistrictMapper = (ShopDistrictMapper) applicationContext.getBean("shopDistrictMapper");
		shopMapper = (ShopMapper) applicationContext.getBean("shopMapper");
		smallSheetFrameMapper = (SmallSheetFrameMapper) applicationContext.getBean("smallSheetFrameMapper");
		smallSheetTextMapper = (SmallSheetTextMapper) applicationContext.getBean("smallSheetTextMapper");
		warehousingReportMapper = (WarehousingReportMapper) applicationContext.getBean("warehousingReportMapper");
//		wxUserMapper = (WxUserMapper) applicationContext.getBean("wxUserMapper");
		messageCategoryMapper = (MessageCategoryMapper) applicationContext.getBean("messageCategoryMapper");
		providerDistrictMapper = (ProviderDistrictMapper) applicationContext.getBean("providerDistrictMapper");
		departmentMapper = (DepartmentMapper) applicationContext.getBean("departmentMapper");
		barcodesSyncCacheDispatcherMapper = (BarcodesSyncCacheDispatcherMapper) applicationContext.getBean("barcodesSyncCacheDispatcherMapper");
		configCacheSizeMapper = (ConfigCacheSizeMapper) applicationContext.getBean("configCacheSizeMapper");
		configGeneralMapper = (ConfigGeneralMapper) applicationContext.getBean("configGeneralMapper");
		posSyncCacheDispatcherMapper = (PosSyncCacheDispatcherMapper) applicationContext.getBean("posSyncCacheDispatcherMapper");
		posSyncCacheMapper = (PosSyncCacheMapper) applicationContext.getBean("posSyncCacheMapper");
		staffPermissionMapper = (StaffPermissionMapper) applicationContext.getBean("staffPermissionMapper");
		stateMachineMapper = (StateMachineMapper) applicationContext.getBean("stateMachineMapper");
		vipCategoryMapper = (VipCategoryMapper) applicationContext.getBean("vipCategoryMapper");
		returnRetailtradeCommoditydDestinationMapper = (ReturnRetailtradeCommoditydDestinationMapper) applicationContext.getBean("returnRetailtradeCommoditydDestinationMapper");
		staffBelongingMapper = (StaffBelongingMapper) applicationContext.getBean("staffBelongingMapper");
		bonusRuleMapper = (BonusRuleMapper) applicationContext.getBean("bonusRuleMapper");
		bonusConsumeHistoryMapper = (BonusConsumeHistoryMapper) applicationContext.getBean("bonusConsumeHistoryMapper");
		couponMapper = (CouponMapper) applicationContext.getBean("couponMapper");
		couponCodeMapper = (CouponCodeMapper) applicationContext.getBean("couponCodeMapper");
		couponScopeMapper = (CouponScopeMapper) applicationContext.getBean("couponScopeMapper");
		retailTradeCouponMapper = (RetailTradeCouponMapper) applicationContext.getBean("retailTradeCouponMapper");
		vipCardMapper = (VipCardMapper) applicationContext.getBean("vipCardMapper");
		vipCardCodeMapper = (VipCardCodeMapper) applicationContext.getBean("vipCardCodeMapper");
		vipSourceMapper = (VipSourceMapper) applicationContext.getBean("vipSourceMapper");
	}
	
	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

}
