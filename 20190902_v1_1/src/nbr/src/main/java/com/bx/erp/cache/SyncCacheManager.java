package com.bx.erp.cache;

import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;

@Component("syncCacheManager")
@Scope("prototype")
public class SyncCacheManager {
	private static HashMap<String, HashMap<EnumSyncCacheType, BaseCache>> list = new HashMap<String, HashMap<EnumSyncCacheType, BaseCache>>();

	public static void register(String companyDBName, EnumSyncCacheType ect, BaseCache bc) {
		HashMap<EnumSyncCacheType, BaseCache> hmNow = list.get(companyDBName); 
		if (hmNow != null) {
			hmNow.put(ect, bc);
		} else {
			HashMap<EnumSyncCacheType, BaseCache> hm = new HashMap<EnumSyncCacheType, BaseCache>();
			hm.put(ect, bc);
			list.put(companyDBName, hm);
		}
	}
	

	public static BaseCache getCache(String companyDBName, EnumSyncCacheType ect) {
		HashMap<EnumSyncCacheType, BaseCache> hm = list.get(companyDBName);
		assert hm != null;
		return hm.get(ect);
	}
}
