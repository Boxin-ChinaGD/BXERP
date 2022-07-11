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
import com.bx.erp.action.bo.StaffBelongingBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.StaffBelonging;

@Component("staffBelongingCache")
@Scope("prototype")
public class StaffBelongingCache extends BaseCache {
	private Log logger = LogFactory.getLog(StaffBelongingCache.class);

	/** key：openID <br />
	 * value：dbName */
	public Hashtable<String, BaseModel> ht;

	@Resource
	private StaffBelongingBO staffBelongingBO;

	public StaffBelongingCache() {
		sCacheName = "openID所归属的公司";
	}

	protected int getCacheType() {
		return EnumCacheType.ECT_StaffBelonging.getIndex();
	}

	protected BaseBO getMasterBO() {
		return staffBelongingBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	protected BaseModel getMasterModel() {
		StaffBelonging staffBelonging = new StaffBelonging();
		staffBelonging.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return staffBelonging;
	}

	// @Override
	// public void load(String dbName) {
	// doLoad(dbName);
	// logger.info("openID归属公司被重置！");
	// }

	@Override
	protected BaseModel getMasterModel(String dbName) {
		StaffBelonging staffBelonging = new StaffBelonging();
		staffBelonging.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return staffBelonging;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		listToHashtable(list);
	}

	@Override
	protected Hashtable<String, BaseModel> listToHashtable(List<BaseModel> ls) {
		if (ls == null) {
			throw new RuntimeException("参数ls为null！");
		}
		ht = new Hashtable<String, BaseModel>(ls.size()); // 每一次设置缓存，都不会、也不能污染到本对象的副本中的ht数据成员
		for (BaseModel bm : ls) {
			StaffBelonging staffBelonging = (StaffBelonging) bm;
			if (staffBelonging.getOpenId() != null) { // 用户没有关注公众号，不需要添加缓存
				ht.put(staffBelonging.getOpenId(), bm);
			}
		}
		return ht;
	}

	@Override
	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		return null;
	}

	public StaffBelonging read1(String openID, ErrorInfo ecOut) {
		StaffBelonging bmToRead = null;

		lock.readLock().lock();

		if (ht.containsKey(openID)) {
			bmToRead = (StaffBelonging) ht.get(openID);
			ecOut.setErrorCode(EnumErrorCode.EC_NoError);
		} else {
			bmToRead = null;
			ecOut.setErrorCode(EnumErrorCode.EC_NoSuchData);
			logger.debug("无此openID。key=" + openID);
		}

		lock.readLock().unlock();

		return bmToRead;
	}

	@Override
	public BaseModel read1(int id, int staffBelongingID, ErrorInfo ecOut, String dbName) {
		throw new RuntimeException("StaffBelongingCache类不支持此操作");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doLoad(String dbName) {
		// 遍历所有公司的DB名称
		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		// 存放所有公司的openID归属数据
		List<BaseModel> lsAllDB = new ArrayList<BaseModel>();
		// 遍历所有公司的openID归属到缓存
		logger.info("加载缓存（" + sCacheName + "）...");
		for (BaseModel bm : list) {
			Company com = (Company) bm;
			try {
				if (com.getDbName().equals(BaseAction.DBName_Public)) {
					continue;
				}
				StaffBelonging staffBelonging = new StaffBelonging();
				DataSourceContextHolder.setDbName(com.getDbName());
				List<BaseModel> ls = (List<BaseModel>) staffBelongingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staffBelonging);
				if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("加载缓存（" + sCacheName + "）失败！请重启服务器！！错误信息：" + getMasterBO().printErrorInfo());
					assert false;
				}
				lsAllDB.addAll(ls);
			} catch (Exception e) {
				logger.error("加载缓存异常，异常DB为：" + com.getDbName() + "\n，异常信息为：" + e.getMessage());
			}
		}
		logger.info("加载缓存（" + sCacheName + "）成功！");
		writeN((List<BaseModel>) lsAllDB); // 将DB的数据缓存进本对象的hashtable中
		// try {
		// CacheManager.register(dbName, getCacheType(), (BaseCache) this.clone()); //
		// 实际缓存的对象，是本对象的一个副本
		// deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
		// } catch (CloneNotSupportedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	protected void doDelete1(BaseModel o) {
		if (o == null) {
			logger.info("对象为null，不会删除普通缓存");
			return;
		}
		StaffBelonging staffBelonging = (StaffBelonging) o;
		getCache().remove(String.valueOf(staffBelonging.getOpenId()));
	}

	/** 用户绑定公司时，会更新T_Staff.F_openID，这里需要写进缓存 */
	@Override
	protected void doWrite1(BaseModel o, String dbName, int staffID) {
		if (o == null) {
			logger.info("对象为null，不会加入普通缓存");
			return;
		}
		StaffBelonging vb = (StaffBelonging) o;
		getCache().put(String.valueOf(vb.getOpenId()), o);
	}
}
