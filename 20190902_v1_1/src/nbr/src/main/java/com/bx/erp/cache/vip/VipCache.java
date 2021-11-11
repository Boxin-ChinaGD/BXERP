package com.bx.erp.cache.vip;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;

@Component("vipCache")
@Scope("prototype")
public class VipCache extends BaseCache {
	private Log logger = LogFactory.getLog(VipCache.class);

	public Hashtable<String, BaseModel> htVip;

	@Resource
	private VipBO vipBO;

	public VipCache() {
		sCacheName = "会员";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Vip.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return vipBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Vip vip = new Vip();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(vip.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		vip.setPageSize(Integer.parseInt(ccs.getValue()));

		return vip;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htVip;
	}

	@Override
	protected BaseModel retrieve1(int id, int StaffID, ErrorInfo ecOut, String dbName) {
		BaseModel baseModel = getMasterModel(dbName);
		baseModel.setID(id);
		DataSourceContextHolder.setDbName(dbName);
		Vip vip = (Vip) getMasterBO().retrieve1Object(StaffID, BaseBO.INVALID_CASE_ID, baseModel);
		if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError || vip == null) {
			ecOut.setErrorCode(getMasterBO().getLastErrorCode());
			ecOut.setErrorMessage(getMasterBO().getLastErrorMessage());
			return null;
		}

		return vip;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htVip = listToHashtable(list);
	}

	/** 服务器启动时，加载要同步的信息到内存中 */
//	@SuppressWarnings({ "unchecked" })
//	@Override
//	protected void doLoad(String dbName) {
//		logger.info("加载缓存（会员信息）...");
//		Vip v = new Vip();
//		v.setCategory(BaseAction.INVALID_ID);
//		DataSourceContextHolder.setDbName(dbName);
//		List<?> ls = vipBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, v);
//		if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			throw new RuntimeException("加载缓存（会员信息）失败！请重启服务器！！");
//		}
//		logger.info("加载缓存（会员信息）成功！");
//		writeN((List<BaseModel>) ls);// 将DB的数据缓存进本对象的hashtable中
//	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");
		
		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}
	
	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_VipCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
