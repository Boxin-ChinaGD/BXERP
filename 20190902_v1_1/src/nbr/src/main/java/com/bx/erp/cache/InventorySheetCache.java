package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.warehousing.InventoryCommodityBO;
import com.bx.erp.action.bo.warehousing.InventorySheetBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.warehousing.InventorySheet;

@Component("inventorySheetCache")
@Scope("prototype")
public class InventorySheetCache extends BaseCache {
	private Log logger = LogFactory.getLog(InventorySheetCache.class);

	public Hashtable<String, BaseModel> htInventorySheet;

	@Resource
	private InventorySheetBO inventorySheetBO;

	@Resource
	private InventoryCommodityBO inventoryCommodityBO;

	public InventorySheetCache() {
		sCacheName = "盘点单";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_InventorySheet.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return inventorySheetBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return inventoryCommodityBO;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel slave) {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		InventorySheet inventorySheet = new InventorySheet();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(inventorySheet.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		inventorySheet.setPageSize(Integer.parseInt(ccs.getValue()));

		return inventorySheet;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htInventorySheet;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htInventorySheet = listToHashtable(list);
	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_InventorySheetCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
