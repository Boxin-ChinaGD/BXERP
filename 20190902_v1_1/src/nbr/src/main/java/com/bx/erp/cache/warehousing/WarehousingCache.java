package com.bx.erp.cache.warehousing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;

@Component("warehousingCache")
@Scope("prototype")
public class WarehousingCache extends BaseCache {
	private Log logger = LogFactory.getLog(WarehousingCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private WarehousingBO warehousingBO;

	@Resource
	protected WarehousingCommodityBO warehousingCommodityBO;

	public WarehousingCache() {
		sCacheName = "入库单信息";
	}

	protected int getCacheType() {
		return EnumCacheType.ECT_Warehousing.getIndex();
	}

	protected BaseBO getMasterBO() {
		return warehousingBO;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Warehousing warehousing = new Warehousing();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(warehousing.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		warehousing.setPageSize(Integer.parseInt(ccs.getValue()));

		return warehousing;
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
	protected BaseBO getSlaveBO() {
		return warehousingCommodityBO;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(master.getID());
		return wc;
	}

	// @Override
	// protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String
	// dbName) {
	// logger.info("ID是：" + id);
	// Warehousing wh = new Warehousing();
	// wh.setID(id);
	// DataSourceContextHolder.setDbName(dbName);
	// List<List<BaseModel>> bmList = (List<List<BaseModel>>)
	// warehousingBO.retrieve1ObjectEx(staffID, BaseBO.INVALID_CASE_ID, wh);
	// if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList ==
	// null || bmList.get(0).size() == 0) {
	// ecOut.setErrorCode(warehousingBO.getLastErrorCode());
	// ecOut.setErrorMessage(warehousingBO.getLastErrorMessage());
	// return null;
	// }
	// // 获取入库从表数据
	// List<BaseModel> warehousingList = bmList.get(0);
	// List<BaseModel> warehousingCommodityList = (List<BaseModel>) bmList.get(1);
	// warehousingList.get(0).setListSlave1(warehousingCommodityList);
	//
	// return warehousingList.get(0);
	// }

	// /** 服务器启动时，加载要同步的信息到内存中 */
	// public void load(String dbName) {
	// logger.info("加载缓存（入库单信息）...");
	//
	// logger.info("加载缓存（入库单信息）成功！");
	// CacheManager.register(dbName, EnumCacheType.ECT_Warehousing, this);
	// writeN(new ArrayList<BaseModel>());
	// }

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_WarehousingCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

}
