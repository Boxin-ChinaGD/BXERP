package com.bx.erp.cache.warehousing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.warehousing.WarehouseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.warehousing.Warehouse;

@Component("warehouseCache")
@Scope("prototype")
public class WarehouseCache extends BaseCache {
	private Log logger = LogFactory.getLog(WarehouseCache.class);

	public Hashtable<String, BaseModel> htWarehouse;

	@Resource
	private WarehouseBO warehouseBO;

	public WarehouseCache() {
		sCacheName = "仓库";
	}

//	public void load(String dbName) {
//		doLoad(dbName);
//	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");
		
		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}
	
	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Warehouse.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return warehouseBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Warehouse warehouse = new Warehouse();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(warehouse.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		warehouse.setPageSize(Integer.parseInt(ccs.getValue()));

		return warehouse;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htWarehouse;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htWarehouse = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_WarehouseCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
