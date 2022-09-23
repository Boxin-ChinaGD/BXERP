package com.bx.erp.cache.trade.promotion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheDispatcherBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.trade.Promotion;

@Component("PromotionSyncCache")
@Scope("prototype")
public class PromotionSyncCache extends SyncCache {
	// private Log logger = LogFactory.getLog(PromotionSyncCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private PromotionSyncCacheBO promotionSyncCacheBO;

	@Resource
	private PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO;

	public PromotionSyncCache() {
		sCacheName = "同步缓存块(促销)";
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return promotionSyncCacheDispatcherBO;
	}

	@Override
	protected BaseModel getNewSyncCacheModel() {
		return (BaseModel) new com.bx.erp.model.trade.PromotionSyncCache();
	}

	@Override
	protected BaseModel getNewSyncCacheDispatcherModel() {
		return (BaseModel) new com.bx.erp.model.trade.PromotionSyncCacheDispatcher();
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return promotionSyncCacheBO;
	}

	@Override
	protected int getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PromotionSyncInfo.getIndex();
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
		if (o instanceof com.bx.erp.model.trade.PromotionSyncCache) {
			super.doDelete1(o);
		} else if (o instanceof Promotion) {
			List<com.bx.erp.model.trade.PromotionSyncCache> lsPointer = new ArrayList<com.bx.erp.model.trade.PromotionSyncCache>();

			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
				com.bx.erp.model.trade.PromotionSyncCache rsInfo = (com.bx.erp.model.trade.PromotionSyncCache) getCache().get(it.next());
				if (rsInfo.getSyncData_ID() == o.getID()) {
					lsPointer.add(rsInfo);
				}
			}

			for (com.bx.erp.model.trade.PromotionSyncCache rtsi : lsPointer) {
				getCache().remove(String.valueOf(rtsi.getID()));
			}
		}
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		// ErrorInfo ecOut = new ErrorInfo();
		// ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName,
		// EnumCacheType.ECT_ConfigCacheSize).read1(PromotionSync_CACHESIZE_ID, staffID,
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
