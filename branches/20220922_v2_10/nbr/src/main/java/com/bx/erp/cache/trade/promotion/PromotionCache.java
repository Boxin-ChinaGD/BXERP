package com.bx.erp.cache.trade.promotion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.trade.PromotionBO;
import com.bx.erp.action.bo.trade.PromotionScopeBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;

@Component("promotionCache")
@Scope("prototype")
public class PromotionCache extends BaseCache {
	private Log logger = LogFactory.getLog(PromotionCache.class);
	
	public Hashtable<String, BaseModel> ht;

	@Resource
	private PromotionBO promotionBO;

	@Resource
	private PromotionScopeBO promotionScopeBO;

	public PromotionCache() {
		sCacheName = "促销活动";
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(master.getID());
		return ps;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return promotionScopeBO;
	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");
		
		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}
	
//	@SuppressWarnings("unchecked")
//	public void load(String dbName) {
//		logger.info("加载缓存（促销活动）...");
//		Promotion promotion = new Promotion();
//		List<BaseModel> ht = new ArrayList<BaseModel>();
//		promotion.setSubStatusOfStatus(Promotion.ACTIVE); //  返回所有状态为0的促销活动 
//		promotion.setStatus(BaseAction.INVALID_STATUS); // 返回所有的促销活动
//		promotion.setPosID(BaseAction.INVALID_ID);
//		DataSourceContextHolder.setDbName(dbName);
//		List<Promotion> pl = (List<Promotion>) promotionBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, promotion);
//		logger.info("RetrieveN promotion error code = " + promotionBO.getLastErrorCode());
//
//		switch (promotionBO.getLastErrorCode()) {
//		case EC_NoError:
//			for (Promotion p : pl) {
//				PromotionScope pm = new PromotionScope();
//				pm.setPromotionID(p.getID());
//				DataSourceContextHolder.setDbName(dbName);
//				List<PromotionScope> pil = (List<PromotionScope>) promotionScopeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pm);
//				logger.info("RetrieveN promotion_scope error code = " + promotionScopeBO.getLastErrorCode());
//				if (promotionScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//					throw new RuntimeException("加载缓存（促销活动）失败！请重启服务器！！");
//				}
//				//
//				p.setListSlave1(pil);
//				ht.add(p);
//			}
//			break;
//		default:
//			throw new RuntimeException("加载缓存（促销活动）失败！请重启服务器！！");
//		}
//		logger.info("加载缓存（促销活动）成功！");
//		writeN(ht); // 将DB的数据缓存进本对象的hashtable中
//	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Promotion.getIndex();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		ht = listToHashtable(list);
	}

//	/** 促销信息在服务器一启动时就加载，不需要在运行过程中加载其中一个促销信息 */
//	@Override
//	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
//		return null;
//	}
	@Override
	protected BaseModel getMasterModel(String dbName) {
		Promotion promotion = new Promotion();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(promotion.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		promotion.setPageSize(Integer.parseInt(ccs.getValue()));

		return promotion;
	}
	@Override
	protected BaseBO getMasterBO() {
		return promotionBO;
	}
}
