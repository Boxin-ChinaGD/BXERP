package com.bx.erp.cache;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffPermissionBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.model.CacheType.EnumCacheType;

/** 权限的查询需要支持根据staffID查询，也要支持根据staffID和sp名查询。后者用于检查特定用户有无特定权限，操作频繁，需要快速查询到 */
@Component("staffPermissionCache")
@Scope("prototype")
public class StaffPermissionCache extends BaseCache {
	private Log logger = LogFactory.getLog(BaseCache.class);

	/** key：staffID+sp名 <br />
	 * value：StaffPermission */
	public Hashtable<String, BaseModel> ht;

	@Resource
	private StaffPermissionBO staffPermissionBO;

	public StaffPermissionCache() {
		sCacheName = "门店人员权限信息";
	}

	protected int getCacheType() {
		return EnumCacheType.ECT_StaffPermission.getIndex();
	}

	protected BaseBO getMasterBO() {
		return staffPermissionBO;
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
		StaffPermission sp = new StaffPermission();
		sp.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return sp;
	}

	// @Override
	// public void load(String dbName) {
	// doLoad(dbName);
	// logger.info("权限视图被重置！");
	// }

	@Override
	protected BaseModel getMasterModel(String dbName) {
		StaffPermission staffPermission = new StaffPermission();
		staffPermission.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return staffPermission;
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
			ht.put(String.valueOf(((StaffPermission) bm).getStaffID()) + ((StaffPermission) bm).getSp(), bm);
		}

		return ht;
	}

	@Override
	protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		return null;
	}

	public StaffPermission read1(int staffID, String permissionName, ErrorInfo ecOut) {
		StaffPermission bmToRead = null;

		lock.readLock().lock();

		String key = String.valueOf(staffID) + permissionName;
		if (ht.containsKey(key)) {
			bmToRead = (StaffPermission) ht.get(key);
			ecOut.setErrorCode(EnumErrorCode.EC_NoError);
		} else {
			bmToRead = null;
			ecOut.setErrorCode(EnumErrorCode.EC_NoSuchData);
			logger.debug("无此权限。key=" + key);
		}

		lock.readLock().unlock();

		return bmToRead;
	}

	@Override
	public BaseModel read1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		throw new RuntimeException("StaffPermissionCache类不支持此操作");
		// BaseModel bmToRead = null;
		// //
		// lock.readLock().lock();
		// try {
		// bmToRead = doRead1(id, staffID, ecOut, dbName);
		// } catch (Exception e) {
		// logger.error("read1出错，信息：" + e.getMessage() + "\nID=" + id);
		// }
		// lock.readLock().unlock();
		//
		// return bmToRead;
	}

	// /** 对StaffPermissionCache来说，id是要查询的staff的id，不是BaseCache里面的F_ID TODO
	// 重构：这样查找太低效
	// * //... */
	// @Override
	// protected BaseModel doRead1(int id, int staffID, ErrorInfo ecOut, String
	// dbName) {
	// ecOut.setErrorCode(EnumErrorCode.EC_NoError);
	// ht = getCache();
	// System.out.println("----" + ht);
	// for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
	// StaffPermission sp = (StaffPermission) getCache().get(it.next());
	// if (sp.getStaffID() == id) {
	// return sp;
	// }
	// }
	//
	//
	// return null;
	// }

	// protected BaseModel doRead1(int id, int staffID, ErrorInfo ecOut) {
	// ecOut.setErrorCode(EnumErrorCode.EC_NoError);
	// for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
	// StaffPermission sp = (StaffPermission) getCache().get(it.next());
	// if (sp.getStaffID() == id) {
	// return sp;
	// }
	// }
	//
	//
	// return null;

	// List<BaseModel> list = (List<BaseModel>) getCache();
	// for (BaseModel bm : list) {
	// (StaffPermission) bm;
	// }
	// 从缓存中查找该行数据并返回之
	// if (getCache().containsKey(String.valueOf(id))) {
	// ++iReadCacheHitNO;
	// try {
	// return getCache().get(String.valueOf(id)).clone();
	// } catch (CloneNotSupportedException e) {
	// logger.info("doRead1()异常" + e.getMessage());
	// }
	// }

	// // 缓存中没有该行数据，则让子类从DB中获取并加入缓存
	// BaseModel ret = null;
	// BaseModel bm = retrieve1(id, staffID, ecOut);
	// if (bm != null) {
	// if (getCache().size() + 1 > getMaxCacheNumber()) { // 避免将最近添加的值remove掉
	// for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
	// getCache().remove(it.next());
	// break;
	// }
	// }
	// getCache().put(String.valueOf(bm.getID()), bm);
	// try {
	// ret = bm.clone();
	// } catch (CloneNotSupportedException e) {
	// e.printStackTrace();
	// }
	// }

	// return ret;
	// }
}
