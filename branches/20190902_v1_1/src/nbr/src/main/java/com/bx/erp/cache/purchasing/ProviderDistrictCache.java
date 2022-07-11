package com.bx.erp.cache.purchasing;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.ProviderDistrict;

@Component("providerDistrictCache")
@Scope("prototype")
public class ProviderDistrictCache extends BaseCache {
	private Log logger = LogFactory.getLog(ProviderDistrictCache.class);

	public Hashtable<String, BaseModel> htProviderDistrict;

	@Resource
	private ProviderDistrictBO providerDistrictBO;

	public ProviderDistrictCache() {
		sCacheName = "供应商区域";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_ProviderDistrict.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return providerDistrictBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return super.getSlaveBaseModel(master);
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return providerDistrict;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htProviderDistrict;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htProviderDistrict = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_ProviderDistrictCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
