package com.bx.erp.cache.commodity;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.config.ConfigCacheSize;

@Component("categoryCache")
@Scope("prototype")
public class CategoryCache extends BaseCache {
	private Log logger = LogFactory.getLog(CategoryCache.class);

	public Hashtable<String, BaseModel> htCategory;

	@Resource
	private CategoryBO categoryBO;

	public CategoryCache() {
		sCacheName = "商品类别";
	}

//	public void load(String dbName) {
//		doLoad(dbName);
//		
//		register(dbName);
//	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Category.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return categoryBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Category c = new Category();
		c.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return c;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htCategory;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htCategory = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_CategoryCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
