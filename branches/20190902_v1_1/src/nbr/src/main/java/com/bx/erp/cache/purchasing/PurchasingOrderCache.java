package com.bx.erp.cache.purchasing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;

@Component("purchasingOrderCache")
@Scope("prototype")
public class PurchasingOrderCache extends BaseCache {
	private Log logger = LogFactory.getLog(PurchasingOrderCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private PurchasingOrderBO purchasingOrderBO;

	@Resource
	private PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	public PurchasingOrderCache() {
		sCacheName = "采购单信息";
	}

	@Override
	protected BaseBO getSlaveBO() {
		return purchasingOrderCommodityBO;
	}

	// @Override
	// protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String
	// dbName) {
	// logger.info("ID是：" + id);
	// PurchasingOrder po = new PurchasingOrder();
	// po.setID(id);
	// DataSourceContextHolder.setDbName(dbName);
	// List<List<BaseModel>> bmList = (List<List<BaseModel>>)
	// purchasingOrderBO.retrieve1ObjectEx(staffID, BaseBO.INVALID_CASE_ID, po);
	// if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError ||
	// bmList == null || bmList.get(0).size() == 0) {
	// ecOut.setErrorCode(purchasingOrderBO.getLastErrorCode());
	// ecOut.setErrorMessage(purchasingOrderBO.getLastErrorMessage());
	// return null;
	// }
	// // 获取采购单从表数据
	// List<BaseModel> purchasingOrderList = bmList.get(0);
	// List<BaseModel> purchasingOrderCommodityList = (List<BaseModel>)
	// bmList.get(1);
	// purchasingOrderList.get(0).setListSlave1(purchasingOrderCommodityList);
	//
	// return purchasingOrderList.get(0);
	// }

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		ht = listToHashtable(list);
	}

	protected int getCacheType() {
		return EnumCacheType.ECT_PurchasingOrder.getIndex();
	}

	protected BaseBO getMasterBO() {
		return purchasingOrderBO;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(purchasingOrder.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		purchasingOrder.setPageSize(Integer.parseInt(ccs.getValue()));

		return purchasingOrder;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		PurchasingOrderCommodity pc = new PurchasingOrderCommodity();
		pc.setPurchasingOrderID(master.getID());
		return pc;
	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");
		
		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_PurchasingOrderCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
