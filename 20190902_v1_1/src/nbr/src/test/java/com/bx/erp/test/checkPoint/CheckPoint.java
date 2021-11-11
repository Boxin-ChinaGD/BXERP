package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;

public class CheckPoint {
	/** 检查同步块缓存是否存在 */
	public static List<BaseSyncCache> verifyFromCacheIfSyncCacheExists(String dbName, BaseModel bmOfDB, EnumSyncCacheType esct, String syncType) {
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
		List<BaseSyncCache> BaseSyncCacheList = new ArrayList<>();
		BaseSyncCache baseSyncCache = null;
		for (BaseModel bm : bmSyncCacheList) {
			baseSyncCache = (BaseSyncCache) bm;
			if (baseSyncCache.getSyncData_ID() == bmOfDB.getID() && baseSyncCache.getSyncType().equals(syncType)) {
				BaseSyncCacheList.add(baseSyncCache);
			}
		}
		return BaseSyncCacheList;
	}
	
	/** 检查同步块缓存是否存在 */
	public static BaseSyncCache verifyFromCacheIfSyncCacheExists(BaseModel bmOfDB, EnumSyncCacheType esct, String syncType, String dbName) {
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
		BaseSyncCache baseSyncCache = null;
		for (BaseModel bm : bmSyncCacheList) {
			baseSyncCache = (BaseSyncCache) bm;
			if (baseSyncCache.getSyncData_ID() == bmOfDB.getID() && baseSyncCache.getSyncType().equals(syncType)) {
				return baseSyncCache;
			}
		}
		return null;
	}

	/** 检查同步块缓存DB是否存在 */
	public static BaseSyncCache verifyFromDBIfSyncCacheExists(BaseBO syncCacheBO, BaseModel bmOfDB, BaseSyncCache baseSyncCache, String syncType, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		List<?> bcslist = syncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
		for (Object o : bcslist) {
			BaseSyncCache baseSyncCacheDB = (BaseSyncCache) o;
			if (baseSyncCacheDB.getSyncData_ID() == bmOfDB.getID() && baseSyncCacheDB.getSyncType().equals(syncType)) {
				return baseSyncCacheDB;
			}
		}
		return null;
	}

	/** 检查普通缓存是否存在
	 * 
	 * @param exists
	 *            true为bmOfDB对象存在缓存中，false则不存在缓存中 */
	public static void verifyFromCacheIfCacheExists(boolean exists, BaseModel bmOfDB, EnumCacheType ect, String dbName) {
		List<BaseModel> bmList = CacheManager.getCache(dbName, ect).readN(false, false);
		boolean cacheExists = false;
		System.out.println(bmOfDB.getID());
		for (BaseModel bm : bmList) {
			if (bm.getID() == bmOfDB.getID()) {
				if (!exists) {
					assertTrue(false, "对象" + (!exists == true ? "存在" : "不存在") + "缓存中");
				}
				cacheExists = true;
				break;
			}
		}
		if (!cacheExists && exists) {
			assertTrue(false, "对象不存在缓存中");
		}
	}
}
