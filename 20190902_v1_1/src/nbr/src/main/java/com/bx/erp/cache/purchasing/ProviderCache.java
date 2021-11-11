package com.bx.erp.cache.purchasing;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.Provider;

@Component("providerCache")
@Scope("prototype")
public class ProviderCache extends BaseCache {
	private Log logger = LogFactory.getLog(ProviderCache.class);

	public Hashtable<String, BaseModel> htProvider;

	@Resource
	private ProviderBO providerBO;

	public ProviderCache() {
		sCacheName = "供应商";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Provider.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return providerBO;
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
		Provider provider = new Provider();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(provider.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		provider.setPageSize(Integer.parseInt(ccs.getValue()));

		return provider;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htProvider;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htProvider = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
