package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BarcodesSyncCacheDispatcherBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;

@Component("barcodesSyncCache")
@Scope("prototype")
public class BarcodesSyncCache extends SyncCache {
//	private Log logger = LogFactory.getLog(BarcodesSyncCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private BarcodesSyncCacheBO barcodesSyncCacheBO;

	@Resource
	private BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO;

	public BarcodesSyncCache() {
		sCacheName = "同步缓存块(条形码)";
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return barcodesSyncCacheDispatcherBO;
	}

	@Override
	protected BaseModel getNewSyncCacheModel() {
		return (BaseModel) new com.bx.erp.model.BarcodesSyncCache();
	}

	@Override
	protected BaseModel getNewSyncCacheDispatcherModel() {
		return (BaseModel) new com.bx.erp.model.BarcodesSyncCacheDispatcher();
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return barcodesSyncCacheBO;
	}

	@Override
	protected int getSyncCacheType() {
		return EnumSyncCacheType.ESCT_BarcodesSyncInfo.getIndex();
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
		if (o instanceof com.bx.erp.model.BarcodesSyncCache) {
			super.doDelete1(o);
		} else if (o instanceof Barcodes) {
			List<com.bx.erp.model.BarcodesSyncCache> lsPointer = new ArrayList<com.bx.erp.model.BarcodesSyncCache>();

			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
				com.bx.erp.model.BarcodesSyncCache syncCache = (com.bx.erp.model.BarcodesSyncCache) getCache().get(it.next());
				if (syncCache.getSyncData_ID() == o.getID()) {
					lsPointer.add(syncCache);
				}
			}

			for (com.bx.erp.model.BarcodesSyncCache rtsi : lsPointer) {
				getCache().remove(String.valueOf(rtsi.getID()));
			}
		}
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
