package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;

@Component("retailTradePromotingCache")
@Scope("prototype")
public class RetailTradePromotingCache extends BaseCache {
	private Log logger = LogFactory.getLog(RetailTradePromotingCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private RetailTradePromotingBO retailTradePromotingBO;

	@Resource
	private RetailTradePromotingFlowBO retailTradePromotingFlowBO;

	public RetailTradePromotingCache() {
		sCacheName = "零售单计算过程";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_RetailTradePromoting.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return retailTradePromotingBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		return new RetailTradePromoting();
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	// public void load(String dbName) {
	// logger.info("加载缓存（零售单计算过程）...");
	//
	// logger.info("加载缓存（零售单计算过程）成功！");
	// CacheManager.register(dbName, EnumCacheType.ECT_RetailTradePromoting, this);
	// writeN(new ArrayList<BaseModel>());
	// }

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		DataSourceContextHolder.setDbName(dbName);

		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp.setID(id);
		DataSourceContextHolder.setDbName(dbName);
		RetailTradePromoting rtRetrieve = (RetailTradePromoting) retailTradePromotingBO.retrieve1Object(staffID, BaseBO.INVALID_CASE_ID, rtp);
		if (retailTradePromotingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			ecOut.setErrorCode(retailTradePromotingBO.getLastErrorCode());
			ecOut.setErrorMessage(retailTradePromotingBO.getLastErrorMessage());
		}

		// 获取零售单从表数据
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setRetailTradePromotingID(rtRetrieve.getID());

		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradePromotingFlow> retailTradeCommodityList = (List<RetailTradePromotingFlow>) retailTradePromotingFlowBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, retailTradePromotingFlow);
		if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			ecOut.setErrorCode(retailTradePromotingFlowBO.getLastErrorCode());
			ecOut.setErrorMessage(retailTradePromotingFlowBO.getLastErrorMessage());
		}
		//
		rtRetrieve.setListSlave1(retailTradeCommodityList);

		return rtRetrieve;
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
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_RetailTredePromotingNumberCacheSize.getIndex(), staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
