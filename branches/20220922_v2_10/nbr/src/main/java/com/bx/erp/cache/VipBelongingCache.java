//package com.bx.erp.cache;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//import javax.annotation.PreDestroy;
//import javax.annotation.Resource;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Component;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.VipBelongingBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.VipBelonging;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.util.DataSourceContextHolder;
//
//@Component("vipBelongingCache")
//public class VipBelongingCache extends BaseCache {
//	private Log logger = LogFactory.getLog(BaseCache.class);
//
//	/** key：cardID <br />
//	 * value：dbName */
//	public Hashtable<String, BaseModel> ht;
//
//	@Resource
//	private VipBelongingBO vipBelongingBO;
//
//	public VipBelongingCache() {
//		sCacheName = "会员卡和优惠券归属公司";
//	}
//
//	protected EnumCacheType getCacheType() {
//		return EnumCacheType.ECT_VipBelonging;
//	}
//
//	protected BaseBO getMasterBO() {
//		return vipBelongingBO;
//	}
//
//	@Override
//	protected BaseBO getSlaveBO() {
//		return null;
//	}
//
//	@Override
//	protected BaseModel getSlaveBaseModel(BaseModel master) {
//		return null;
//	}
//
//	protected BaseModel getMasterModel() {
//		VipBelonging vb = new VipBelonging();
//		vb.setPageSize(BaseAction.PAGE_SIZE_Infinite);
//
//		return vb;
//	}
//
//	@Override
//	public void load(String dbName) {
//		doLoad(dbName);
//		logger.info("会员卡和优惠券归属公司被重置！");
//	}
//
//	@Override
//	protected BaseModel getMasterModel(String dbName) {
//		VipBelonging vb = new VipBelonging();
//		vb.setPageSize(BaseAction.PAGE_SIZE_Infinite);
//
//		return vb;
//	}
//
//	@PreDestroy
//	private void destroy() {
//		doDestroy();
//	}
//
//	@Override
//	protected Hashtable<String, BaseModel> getCache() {
//		return ht;
//	}
//
//	@Override
//	protected void setCache(List<BaseModel> list) {
//		listToHashtable(list);
//	}
//
//	@Override
//	protected Hashtable<String, BaseModel> listToHashtable(List<BaseModel> ls) {
//		if (ls == null) {
//			throw new RuntimeException("参数ls为null！");
//		}
//		ht = new Hashtable<String, BaseModel>(ls.size()); // 每一次设置缓存，都不会、也不能污染到本对象的副本中的ht数据成员
//		for (BaseModel bm : ls) {
//			ht.put(((VipBelonging) bm).getCardID(), bm);
//		}
//
//		return ht;
//	}
//
//	@Override
//	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
//		return null;
//	}
//
//	public VipBelonging read1(String cardID, ErrorInfo ecOut) {
//		VipBelonging bmToRead = null;
//
//		lock.readLock().lock();
//
//		if (ht.containsKey(cardID)) {
//			bmToRead = (VipBelonging) ht.get(cardID);
//			ecOut.setErrorCode(EnumErrorCode.EC_NoError);
//		} else {
//			bmToRead = null;
//			ecOut.setErrorCode(EnumErrorCode.EC_NoSuchData);
//			logger.debug("无此CardID。key=" + cardID);
//		}
//
//		lock.readLock().unlock();
//
//		return bmToRead;
//	}
//
//	@Override
//	public BaseModel read1(int id, int vipBelongingID, ErrorInfo ecOut, String dbName) {
//		throw new RuntimeException("VipBelongingCache类不支持此操作");
//	}
//
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void doLoad(String dbName) {
//		// 遍历所有公司的DB名称
//		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
//		// 存放所有公司的卡券归属数据
//		List<BaseModel> lsAllDB = new ArrayList<BaseModel>();
//		// 遍历所有公司的卡券归属到缓存
//		logger.info("加载缓存（" + sCacheName + "）...");
//		for (BaseModel bm : list) {
//			Company com = (Company) bm;
//			try {
//				if (com.getDbName().equals(BaseAction.DBName_Public)) {
//					continue;
//				}
//				VipBelonging vb = new VipBelonging();
//				DataSourceContextHolder.setDbName(com.getDbName());
//				List<BaseModel> ls = (List<BaseModel>) vipBelongingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vb);
//				if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
//					logger.error("加载缓存（" + sCacheName + "）失败！请重启服务器！！错误信息：" + getMasterBO().printErrorInfo());
//					assert false;
//				}
//				lsAllDB.addAll(ls);
//			} catch (Exception e) {
//				logger.error("加载缓存异常，异常DB为：" + com.getDbName() + "\n，异常信息为：" + e.getMessage());
//			}
//		}
//		logger.info("加载缓存（" + sCacheName + "）成功！");
//		writeN((List<BaseModel>) lsAllDB); // 将DB的数据缓存进本对象的hashtable中
//		try {
//			CacheManager.register(dbName, getCacheType(), (BaseCache) this.clone()); // 实际缓存的对象，是本对象的一个副本
//			deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	protected void doDelete1(BaseModel o) {
//		if (o == null) {
//			logger.info("对象为null，不会删除普通缓存");
//			return;
//		}
//		VipBelonging vb = (VipBelonging) o;
//		getCache().remove(String.valueOf(vb.getCardID()));
//	}
//
//	@Override
//	protected void doWrite1(BaseModel o, String dbName, int staffID) {
//		if (o == null) {
//			logger.info("对象为null，不会加入普通缓存");
//			return;
//		}
//		VipBelonging vb = (VipBelonging) o;
//		getCache().put(String.valueOf(vb.getCardID()), o);
//		// 由于是公共DB的缓存，所以暂时不需要限制
////		if (getCache().size() + 1 > getMaxCacheNumber(dbName, staffID)) { // 可能会将最近添加的值remove掉
////			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
////				getCache().remove(it.next());
////				break;
////			}
////		}
//	}
//}
