package com.bx.erp.cache.config;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.config.ConfigCacheSizeBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.ConfigCacheSize;

@Component("configCacheSizeCache")
@Scope("prototype")
public class ConfigCacheSizeCache extends ConfigCache {
	protected Hashtable<String, BaseModel> htCacheSize;

	@Resource
	private ConfigCacheSizeBO configCacheSizeCacheBO;

	public ConfigCacheSizeCache() {
		sCacheName = "普通缓存（普存）对象个数配置";
	}
	
	/** 检查枚举的名称、ID和DB中的一致（不检查值的合法性）。若DB中忘记添加，这里会报错 */
	public void checkIfLoadOK(String dbName) {
		ConfigCacheSizeCache cache = (ConfigCacheSizeCache) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize);
		
		assert cache.getCache().size() == EnumConfigCacheSizeCache.values().length;

		for (Iterator<String> it = cache.getCache().keySet().iterator(); it.hasNext();) {
			BaseModel bm = cache.getCache().get(it.next());
			ConfigCacheSize ccs = (ConfigCacheSize) bm;
			System.out.println(ccs);
			assert EnumConfigCacheSizeCache.values()[ccs.getID() - 1].getName().equals(ccs.getName());
		}
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_ConfigCacheSize.getIndex();
	}

	protected BaseBO getMasterBO() {
		return configCacheSizeCacheBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}
	
	@Override
	protected BaseModel getMasterModel(String dbName) {
		return new ConfigCacheSize();
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htCacheSize;
	}
	
	@Override
	protected void setCache(List<BaseModel> list) {
		htCacheSize = listToHashtable(list);
	}

	private static int iEnumIndex = 1;
	public enum EnumConfigCacheSizeCache {
		ECC_BrandCacheSize("BrandCacheSize", iEnumIndex++), //
		ECC_CategoryCacheSize("CategoryCacheSize", iEnumIndex++), //
		ECC_CommodityCacheSize("CommodityCacheSize", iEnumIndex++), //
		ECC_VipCacheSize("VipCacheSize", iEnumIndex++), //
		ECC_StaffCacheSize("StaffCacheSize", iEnumIndex++), //
		ECC_POSCacheSize("POSCacheSize", iEnumIndex++), //
		ECC_InventorySheetCacheSize("InventorySheetCacheSize", iEnumIndex++), //
		ECC_ProviderCacheSize("ProviderCacheSize", iEnumIndex++), //
		ECC_ProviderDistrictCacheSize("ProviderDistrictCacheSize", iEnumIndex++), //
		ECC_PurchasingOrderCacheSize("PurchasingOrderCacheSize", iEnumIndex++), //
		ECC_WarehouseCacheSize("WarehouseCacheSize", iEnumIndex++), //
		ECC_WarehousingCacheSize("WarehousingCacheSize", iEnumIndex++), //
		ECC_RetailTradeNumberCacheSize("RetailTradeNumberCacheSize", iEnumIndex++), //
		ECC_CategoryParentCacheSize("CategoryParentCacheSize", iEnumIndex++), //
		ECC_PromotionCacheSize("PromotionCacheSize", iEnumIndex++), //
		ECC_RetailTredePromotingNumberCacheSize("RetailTredePromotingNumberCacheSize", iEnumIndex++), //
		ECC_BarcodesCacheSize("BarcodesCacheSize", iEnumIndex), //
		ECC_ShopCacheSize("ShopCacheSize", iEnumIndex), //
		ECC_VipCardCacheSize("VipCardCacheSize", iEnumIndex);//

		private String name;
		private int index;

		private EnumConfigCacheSizeCache(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumConfigCacheSizeCache c : EnumConfigCacheSizeCache.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
