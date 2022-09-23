//package com.bx.erp.cache;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.warehousing.InventoryCommodityBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.config.ConfigCacheSize;
//import com.bx.erp.model.warehousing.InventoryCommodity;
//
//@Component("inventoryCommodityCache")
//@Scope("prototype")
//public class InventoryCommodityCache extends BaseCache {
//	private Log logger = LogFactory.getLog(InventoryCommodityCache.class);
//
//	public Hashtable<String, BaseModel> ht;
//
//	@Resource
//	private InventoryCommodityBO inventoryCommodityBO;
//
//	public InventoryCommodityCache() {
//		sCacheName = "盘点单商品";
//	}
//
//	@Override
//	protected BaseBO getSlaveBO() {
//		return null;
//	}
//	
//	@Override
//	protected BaseModel getSlaveBaseModel(BaseModel slave) {
//		return null;
//	}
//	
//	@Override
//	protected BaseModel getMasterModel(String dbName) {
//		return new InventoryCommodity();
//	}
//	
////	public void load(String dbName) {
////		logger.info("加载缓存（盘点单商品）...");
////
////		logger.info("加载缓存（盘点单商品）成功！");
////		CacheManager.register(dbName, EnumCacheType.ECT_InventoryCommodity, this);
////		writeN(new ArrayList<BaseModel>());
////	}
//
//	@Override
//	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
//		return null;
//	}
//
//	@Override
//	protected Hashtable<String, BaseModel> getCache() {
//		return ht;
//	}
//
//	@Override
//	protected BaseBO getMasterBO() {
//		return inventoryCommodityBO;
//	}
//
//	@Override
//	protected Object clone() throws CloneNotSupportedException {
//		// TODO 自动生成的方法存根
//		return super.clone();
//	}
//
//	@Override
//	protected void setCache(List<BaseModel> list) {
//		ht = listToHashtable(list);
//	}
//
//	@Override
//	protected int getMaxCacheNumber(String dbName,int staffID) {
//		ErrorInfo ecOut = new ErrorInfo();
//		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(InventoryCommodity_CACHESIZE_ID, staffID, ecOut, dbName);
//		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.info("从缓存中读取配置信息错误！");
//			return CACHESIZE_Default;
//		}
//
//		return Integer.parseInt(ccs.getValue());
//	}
//
//}
