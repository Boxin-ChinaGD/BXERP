package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipCardBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.cache.vip.VipCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.DataSourceContextHolder;

@Component("VipCardCache")
@Scope("prototype")
public class VipCardCache extends BaseCache {
	private Log logger = LogFactory.getLog(VipCache.class);

	public Hashtable<String, BaseModel> htVipCard;

	@Resource
	private VipCardBO vipCardBO;

	public VipCardCache() {
		sCacheName = "会员卡";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_VipCard.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return vipCardBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		VipCard vipCard = new VipCard();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(vipCard.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		vipCard.setPageSize(Integer.parseInt(ccs.getValue()));

		return vipCard;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htVipCard;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htVipCard = listToHashtable(list);
	}

	/** 服务器启动时，加载要同步的信息到内存中 */
	@Override
	protected void doLoad(String dbName) {
		logger.info("加载缓存（会员卡信息）（有且仅有一种）...");
		VipCard v = new VipCard();
		v.setID(BaseAction.DEFAULT_VipCardID);
		DataSourceContextHolder.setDbName(dbName);
		VipCard vc = (VipCard) vipCardBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, v);
		if (vipCardBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			throw new RuntimeException("加载缓存（会员卡信息）失败！请重启服务器！！");
		}
		logger.info("加载缓存（会员卡信息）成功！");
		List<BaseModel> ls = new ArrayList<BaseModel>();
		ls.add(vc);
		writeN(ls);// 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_VipCardCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
