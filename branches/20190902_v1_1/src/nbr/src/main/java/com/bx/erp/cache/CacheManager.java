package com.bx.erp.cache;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.bx.erp.model.CacheType.EnumCacheType;

@Component("cacheManager")
public class CacheManager {
	/** Key: 公司DB名称 <br />
	 * Value: 1个HashMap，  其key=EnumCacheType， value=BaseCache */
	private static HashMap<String, HashMap<EnumCacheType, BaseCache>> list = new HashMap<String, HashMap<EnumCacheType, BaseCache>>();

	public static void register(String companyDBName, EnumCacheType ect, BaseCache bc) {
		HashMap<EnumCacheType, BaseCache> hmNow = list.get(companyDBName); 
		if (hmNow != null) {
			hmNow.put(ect, bc);
		} else {
			HashMap<EnumCacheType, BaseCache> hm = new HashMap<EnumCacheType, BaseCache>();
			hm.put(ect, bc);
			list.put(companyDBName, hm);
		}
	}

	public static BaseCache getCache(String companyDBName, EnumCacheType ect) {
		HashMap<EnumCacheType, BaseCache> hm = list.get(companyDBName);
		BaseCache bm = hm.get(ect);
		if(bm == null) {
			System.out.println("CacheManager意外：companyDBName=" + companyDBName + "\tect="  + ect.getName());
		}
		return hm.get(ect);
	}
}
	