package com.bx.erp.cache.vip;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipCategoryBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.VipCategory;

@Component("vipCategoryChche")
@Scope("prototype")
public class VipCategoryCache extends BaseCache {

	public Hashtable<String, BaseModel> ht;

	@Resource
	private VipCategoryBO vipCategoryBO;

	public VipCategoryCache() {
		sCacheName = "会员类别信息";
	}

	protected int getCacheType() {
		return EnumCacheType.ECT_VipCategory.getIndex();
	}

	protected BaseBO getMasterBO() {
		return vipCategoryBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	protected BaseModel getMasterModel() {
		VipCategory vipCategory = new VipCategory();
		vipCategory.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		
		return vipCategory;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}
	
	@Override
	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		ht = listToHashtable(list);
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		VipCategory vipCategory = new VipCategory();
		vipCategory.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		
		return vipCategory;
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		// ErrorInfo ecOut = new ErrorInfo();
		// ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName,
		// EnumCacheType.ECT_ConfigCacheSize).read1(VipSync_CACHESIZE_ID, staffID,
		// ecOut, dbName);
		// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
		// logger.info("从缓存中读取配置信息错误！");
		// return CACHESIZE_Default;
		// }

		return BaseSyncCache.MAX_SyncCacheSize;
	}
}
