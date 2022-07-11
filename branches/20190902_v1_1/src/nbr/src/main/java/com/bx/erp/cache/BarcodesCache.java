package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.ConfigCacheSize;

@Component("barcodesCache")
@Scope("prototype")
public class BarcodesCache extends BaseCache {
	private Log logger = LogFactory.getLog(BarcodesCache.class);

	public Hashtable<String, BaseModel> htBarcodes;

	@Resource
	private BarcodesBO barcodesBO;

	public BarcodesCache() {
		sCacheName = "商品条形码";
	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Barcodes.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return barcodesBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Barcodes barcodes = new Barcodes();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(barcodes.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		barcodes.setPageSize(Integer.parseInt(ccs.getValue()));

		return barcodes;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htBarcodes;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htBarcodes = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		// ErrorInfo ecOut = new ErrorInfo();
		// ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName,
		// EnumCacheType.ECT_ConfigCacheSize).read1(BarcodesSync_CACHESIZE_ID, staffID,
		// ecOut, dbName);
		// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
		// logger.info("从缓存中读取配置信息错误！");
		// return CACHESIZE_Default;
		// }

		return BaseSyncCache.MAX_SyncCacheSize;
	}
}
