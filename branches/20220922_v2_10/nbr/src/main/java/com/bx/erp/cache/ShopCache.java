package com.bx.erp.cache;

import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;

@Component("shopCache")
@Scope("prototype")
public class ShopCache extends BaseCache {
	private Log logger = LogFactory.getLog(ShopCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private ShopBO shopBO;

	public ShopCache() {
		sCacheName = "门店信息";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Shop.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return shopBO;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Shop shop = new Shop();
//		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(shop.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
//		shop.setPageSize(Integer.parseInt(ccs.getValue()));
		shop.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return shop;
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
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		ht = listToHashtable(list);
	}

	/** 门店信息在服务器一启动时就全部加载，不需要在运行过程中加载其中一个门店信息。<br />
	 * 如果在运行过程中，门店被增加了，会在写DB后写进缓存中。<br />
	 * 通过retrieve1寻找一个不存在的门店人员会直接return null */
	@Override
	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		return null;
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_ShopCacheSize.getIndex(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
