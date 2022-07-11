package com.bx.erp.cache.commodity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheDispatcherBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;

@Component("categorySyncCache")
@Scope("prototype")
public class CategorySyncCache extends SyncCache {
	// private Log logger = LogFactory.getLog(CategorySyncCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private CategorySyncCacheBO categorySyncCacheBO;

	@Resource
	private CategorySyncCacheDispatcherBO categorySyncCacheDispatcherBO;

	public CategorySyncCache() {
		sCacheName = "同步缓存块(类别)";
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return categorySyncCacheDispatcherBO;
	}

	@Override
	protected BaseModel getNewSyncCacheModel() {
		return (BaseModel) new com.bx.erp.model.commodity.CategorySyncCache();
	}

	@Override
	protected BaseModel getNewSyncCacheDispatcherModel() {
		return (BaseModel) new com.bx.erp.model.commodity.CategorySyncCacheDispatcher();
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return categorySyncCacheBO;
	}

	@Override
	protected int getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CategorySyncInfo.getIndex();
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
	protected void doDelete1(BaseModel o) {
		if (o instanceof com.bx.erp.model.commodity.CategorySyncCache) {
			super.doDelete1(o);
		} else if (o instanceof Category) {
			List<com.bx.erp.model.commodity.CategorySyncCache> lsPointer = new ArrayList<com.bx.erp.model.commodity.CategorySyncCache>();

			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
				com.bx.erp.model.commodity.CategorySyncCache rsInfo = (com.bx.erp.model.commodity.CategorySyncCache) getCache().get(it.next());
				if (rsInfo.getSyncData_ID() == o.getID()) {
					lsPointer.add(rsInfo);
				}
			}

			for (com.bx.erp.model.commodity.CategorySyncCache rtsi : lsPointer) {
				getCache().remove(String.valueOf(rtsi.getID()));
			}
		}
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		// ErrorInfo ecOut = new ErrorInfo();
		// ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName,
		// EnumCacheType.ECT_ConfigCacheSize).read1(BrandSync_CACHESIZE_ID, staffID,
		// ecOut, dbName);
		// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
		// logger.info("从缓存中读取配置信息错误！");
		// return CACHESIZE_Default;
		// }
		//
		// return Integer.parseInt(ccs.getValue());
		return BaseSyncCache.MAX_SyncCacheSize;
	}

	/** 本函数只能用于TestNG测试！！
	 * 
	 * @return */
	public boolean loadForTestNGTest(String dbName) {
		try {
			load(dbName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
