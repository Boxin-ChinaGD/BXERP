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
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Pos.EnumStatusPos;

@Component("pOSCache")
@Scope("prototype")
public class POSCache extends BaseCache {
	private Log logger = LogFactory.getLog(POSCache.class);

	public Hashtable<String, BaseModel> htPos;

	@Resource
	private PosBO posBO;

	public POSCache() {
		sCacheName = "POS";
	}

	// /** 服务器启动时，加载要同步的信息到内存中 */
	// @SuppressWarnings({ "unchecked" })
	// @Override
	// protected void doLoad(String dbName) {
	// logger.info("加载缓存（POS信息）...");
	// Pos pos = new Pos();
	// pos.setStatus(EnumStatusPos.ESP_Active.getIndex());
	// pos.setShopID(BaseAction.INVALID_ID);
	// DataSourceContextHolder.setDbName(dbName);
	// List<?> ls = posBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
	// pos);
	// if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("加载缓存（POS信息）失败！请重启服务器！！");
	// }
	// logger.info("加载缓存（POS信息）成功！");
	// writeN((List<BaseModel>) ls);// 将DB的数据缓存进本对象的hashtable中
	// try {
	// CacheManager.register(dbName, getCacheType(), (BaseCache) this.clone()); //
	// 实际缓存的对象，是本对象的一个副本
	// deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
	// } catch (CloneNotSupportedException e) {
	// e.printStackTrace();
	// }
	// }

	// public void load(String dbName) {
	// doLoad(dbName);
	// }

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_POS.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return posBO;
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
	protected BaseModel getMasterModel(String dbName) {
		Pos pos = new Pos();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(pos.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		pos.setPageSize(Integer.parseInt(ccs.getValue()));
		pos.setStatus(EnumStatusPos.ESP_Active.getIndex());
		pos.setShopID(BaseAction.INVALID_ID);

		return pos;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htPos;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htPos = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_POSCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
