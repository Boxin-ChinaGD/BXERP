package com.bx.erp.cache.publiC;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.action.RetailTradeAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.cache.BarcodesCache;
import com.bx.erp.cache.BarcodesSyncCache;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.InventorySheetCache;
import com.bx.erp.cache.POSCache;
import com.bx.erp.cache.PosSyncCache;
import com.bx.erp.cache.RetailTradePromotingCache;
import com.bx.erp.cache.ShopCache;
import com.bx.erp.cache.SmallSheetCache;
import com.bx.erp.cache.StaffCache;
import com.bx.erp.cache.StaffBelongingCache;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.cache.VipCardCache;
import com.bx.erp.cache.commodity.BrandCache;
import com.bx.erp.cache.commodity.BrandSyncCache;
import com.bx.erp.cache.commodity.CategoryCache;
import com.bx.erp.cache.commodity.CategoryParentCache;
import com.bx.erp.cache.commodity.CategorySyncCache;
import com.bx.erp.cache.commodity.CommodityCache;
import com.bx.erp.cache.commodity.CommoditySyncCache;
import com.bx.erp.cache.config.ConfigGeneralCache;
import com.bx.erp.cache.config.BxConfigGeneralCache;
import com.bx.erp.cache.config.ConfigCacheSizeCache;
import com.bx.erp.cache.purchasing.ProviderCache;
import com.bx.erp.cache.purchasing.ProviderDistrictCache;
import com.bx.erp.cache.purchasing.PurchasingOrderCache;
import com.bx.erp.cache.trade.promotion.PromotionCache;
import com.bx.erp.cache.trade.promotion.PromotionSyncCache;
import com.bx.erp.cache.vip.VipCache;
import com.bx.erp.cache.vip.VipCategoryCache;
import com.bx.erp.cache.warehousing.WarehouseCache;
import com.bx.erp.cache.warehousing.WarehousingCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.task.TaskScheduler;
import com.bx.erp.util.OkHttpUtil;

@Component("companyCache")
public class CompanyCache extends BaseCache {
	private Log logger = LogFactory.getLog(CompanyCache.class);

	public Hashtable<String, BaseModel> htCompany;

	@Resource
	private CompanyBO companyBO;

	public CompanyCache() {
		sCacheName = "??????";
	}

	@Resource
	private TaskScheduler ts;

	@Resource
	private BrandCache brandCache;

	@Resource
	private BrandSyncCache brandSyncCache;

	@Resource
	private CategoryCache categoryCache;

	@Resource
	private CategoryParentCache categoryParentCache;

	@Resource
	private CategorySyncCache categorySyncCache;

	@Resource
	private CommodityCache commodityCache;

	@Resource
	private CommoditySyncCache commoditySyncCache;

	@Resource
	private ConfigGeneralCache configGeneralCache;

	@Resource
	private ConfigCacheSizeCache configCacheSizeCache;

	@Resource
	private ProviderCache providerCache;

	@Resource
	private ProviderDistrictCache providerDistrictCache;

	@Resource
	private PurchasingOrderCache purchasingOrderCache;

	@Resource
	private PromotionCache promotionCache;

	@Resource
	private PromotionSyncCache promotionSyncCache;

	@Resource
	private VipCache vipCache;

	@Resource
	private VipCategoryCache vipCategoryCache;

	@Resource
	private WarehouseCache warehouseCache;

	@Resource
	private WarehousingCache warehousingCache;

	@Resource
	private BarcodesCache barcodesCache;

	@Resource
	private BarcodesSyncCache barcodesSyncCache;

	// @Resource
	// private InventoryCommodityCache inventoryCommodityCache;

	@Resource
	private InventorySheetCache inventorySheetCache;

	@Resource
	private POSCache posCache;

	@Resource
	private PosSyncCache posSyncCache;

	@Resource
	private RetailTradePromotingCache retailTradePromotingCache;

	@Resource
	private SmallSheetCache smallSheetCache;

	@Resource
	private StaffCache staffCache;

	@Resource
	private StaffPermissionCache staffPermissionCache;

	@Resource
	private ShopCache shopCache;

	// @Resource
	// private VipBelongingCache vipBelongingCache;

	@Resource
	private StaffBelongingCache staffBelongingCache;

	@Resource
	private VipCardCache vipCardCache;

	// ??????DB
	@Resource
	private BxConfigGeneralCache bxConfigGeneralCache;

	// ??????DB
	@Resource
	private BxStaffCache bxStaffCache;

	@Value("${env}")
	private String env;

	/** ????????????DB?????????????????????????????????DB????????????????????????????????? */
	@PostConstruct
	private void load() {
		resolveCurrentEnvAndDomain();

		doLoad(BaseAction.DBName_Public);

		register(BaseAction.DBName_Public);

		// ?????????????????????DB???????????????????????????DB????????????????????????
		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);

		// ???????????????DB????????????
		for (BaseModel bm : list) {
			Company com = (Company) bm;
			if (com.getDbName().equals(BaseAction.DBName_Public)) {
				bxConfigGeneralCache.load(BaseAction.DBName_Public);
				bxStaffCache.load(BaseAction.DBName_Public);
				// vipBelongingCache.load(BaseAction.DBName_Public);
				break;
			}
		}

		// ?????????????????????DB?????????????????????????????????????????????????????????????????????DB
		for (BaseModel bm : list) {
			Company com = (Company) bm;
			try {
				if (com.getDbName().equals(BaseAction.DBName_Public)) {
					continue;
				}

				// ????????????DB??????????????????
				// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????clone?????????????????????com????????????????????????com
				// ??????????????????????????????CompanyAction.loadCacheFromPrivateDB()??????????????????
				configGeneralCache.load(com.getDbName()); // ????????????????????????
				configCacheSizeCache.load(com.getDbName());// ????????????????????????
				configCacheSizeCache.checkIfLoadOK(com.getDbName());
				brandCache.load(com.getDbName());
				brandSyncCache.load(com.getDbName());
				categoryCache.load(com.getDbName());
				categoryParentCache.load(com.getDbName());
				categorySyncCache.load(com.getDbName());
				commodityCache.load(com.getDbName());
				commoditySyncCache.load(com.getDbName());
				providerCache.load(com.getDbName());
				providerDistrictCache.load(com.getDbName());
				purchasingOrderCache.load(com.getDbName());
				promotionCache.load(com.getDbName());
				promotionSyncCache.load(com.getDbName());
				vipCache.load(com.getDbName());
				vipCategoryCache.load(com.getDbName());
				warehouseCache.load(com.getDbName());
				warehousingCache.load(com.getDbName());
				barcodesCache.load(com.getDbName());
				barcodesSyncCache.load(com.getDbName());
				inventorySheetCache.load(com.getDbName());
				posCache.load(com.getDbName());
				posSyncCache.load(com.getDbName());
				retailTradePromotingCache.load(com.getDbName());
				shopCache.load(com.getDbName());
				smallSheetCache.load(com.getDbName());
				staffCache.load(com.getDbName());
				staffPermissionCache.load(com.getDbName());
				staffBelongingCache.load(com.getDbName());
				vipCardCache.load(com.getDbName());
				// ??????????????????????????????????????????????????????
				RealTimeSalesStatisticsToday realTimeSST = new RealTimeSalesStatisticsToday(0.000000d, 0, new Date());
				RetailTradeAction.todaysSaleCache.put(com.getDbName(), realTimeSST);
				// ?????????????????????????????????CompanyAction???loadCacheFromPrivateDB()???????????????xxxCache???????????????CompanyCP.loadCache(Company company)
			} catch (Exception e) {
				logger.error("???????????????????????????DB??????" + com.getDbName() + "\n?????????????????????" + e.getMessage());
			}
		}

		// ?????????????????????????????????
		ts.start();
	}

	/** ???????????????????????????????????????????????????????????????env???????????????DEV???SIT???UAT???PROD????????????????????? */
	private void resolveCurrentEnvAndDomain() {
		if (env.equals(EnumEnv.DEV.getName())) {
			BaseAction.ENV = EnumEnv.DEV;
			BaseAction.DOMAIN = BaseAction.DEV_DOMAIN;
			OkHttpUtil.httpUrl = BaseAction.DEV_DOMAIN;
		} else if (env.equals(EnumEnv.SIT.getName())) {
			BaseAction.ENV = EnumEnv.SIT;
			BaseAction.DOMAIN = BaseAction.SIT_DOMAIN;
			OkHttpUtil.httpUrl = BaseAction.SIT_DOMAIN;
		} else if (env.equals(EnumEnv.UAT.getName())) {
			BaseAction.ENV = EnumEnv.UAT;
			BaseAction.DOMAIN = BaseAction.UAT_DOMAIN;
			OkHttpUtil.httpUrl = BaseAction.UAT_DOMAIN;
		} else if (env.equals(EnumEnv.PROD.getName())) {
			BaseAction.ENV = EnumEnv.PROD;
			BaseAction.DOMAIN = BaseAction.PROD_DOMAIN;
			OkHttpUtil.httpUrl = BaseAction.PROD_DOMAIN; // TODO ??????????????????BaseAction.PROD_DOMAIN????????????
		} else {
			BaseAction.ENV = EnumEnv.DEV;
			BaseAction.DOMAIN = BaseAction.DEV_DOMAIN;
			BaseAction.UseSandBox = true;
			OkHttpUtil.httpUrl = OkHttpUtil.URL_LocalHost; // ???????????????????????????L123????????????????????????????????????
		}
		logger.debug("?????????????????????" + BaseAction.ENV.getName());
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Company.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return companyBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Company company = new Company();
		company.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return company;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htCompany;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htCompany = listToHashtable(list);
	}

	@PreDestroy
	private void destroy() {
		ts.stop();
		
		configGeneralCache.printStatistics();
		configCacheSizeCache.printStatistics();
		configCacheSizeCache.printStatistics();
		brandCache.printStatistics();
		brandSyncCache.printStatistics();
		categoryCache.printStatistics();
		categoryParentCache.printStatistics();
		categorySyncCache.printStatistics();
		commodityCache.printStatistics();
		commoditySyncCache.printStatistics();
		providerCache.printStatistics();
		providerDistrictCache.printStatistics();
		purchasingOrderCache.printStatistics();
		promotionCache.printStatistics();
		promotionSyncCache.printStatistics();
		vipCache.printStatistics();
		vipCategoryCache.printStatistics();
		warehouseCache.printStatistics();
		warehousingCache.printStatistics();
		barcodesCache.printStatistics();
		barcodesSyncCache.printStatistics();
		inventorySheetCache.printStatistics();
		posCache.printStatistics();
		posSyncCache.printStatistics();
		retailTradePromotingCache.printStatistics();
		shopCache.printStatistics();
		smallSheetCache.printStatistics();
		staffCache.printStatistics();
		staffPermissionCache.printStatistics();
		staffBelongingCache.printStatistics();
		vipCardCache.printStatistics();
		
		printStatistics();
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		return 200; // ...TODO ??????????????????????????????200?????????DB
	}
	
	/**
	 * ?????????????????????TestNG????????????
	 * 
	 * @return
	 */
	public boolean loadForTestNGTest() {
		try {
			load();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
