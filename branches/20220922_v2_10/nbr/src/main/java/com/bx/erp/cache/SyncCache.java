package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;

/** SyncCache在项目启动后，必须是全部加载的，因为所有Pos在收银员登录后需要做同步的动作 */
@Component("syncCache")
@Scope("prototype")
public abstract class SyncCache extends BaseCache {
	private Log logger = LogFactory.getLog(SyncCache.class);

	public static final String SYNC_Type_C = "C"; // 创建型同步块
	public static final String SYNC_Type_U = "U"; // 更新型同步块
	public static final String SYNC_Type_D = "D"; // 删除型同步块

	// @Override
	// public void load(String dbName) {
	// doLoad(dbName);
	// }

	@Override
	public void doLoad(String dbName) {
		logger.info("加载同步块缓存（" + sCacheName + "）...");

		List<BaseModel> list = new ArrayList<BaseModel>();

		DataSourceContextHolder.setDbName(dbName);
		List<?> bmList1 = getSyncCacheBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, getNewSyncCacheModel());
		if (getSyncCacheBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("加载失败！请重启服务器！！");
			throw new RuntimeException(BaseModel.ERROR_Tag);
		}

		List<com.bx.erp.model.BaseSyncCache> vl = (List<com.bx.erp.model.BaseSyncCache>) bmList1;

		switch (getSyncCacheBO().getLastErrorCode()) {
		case EC_NoError:
			DataSourceContextHolder.setDbName(dbName);
			List<?> bmList2 = getSyncCacheDispatcherBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, getNewSyncCacheDispatcherModel());
			if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("加载失败！请重启服务器！！错误信息=" + getSyncCacheDispatcherBO().getLastErrorCode() + "\t" + getSyncCacheDispatcherBO().getLastErrorMessage());
				throw new RuntimeException(BaseModel.ERROR_Tag);
			}
			// 对于每个同步块，将块和已经同步此块的POS机调度信息放进同存中
			for (com.bx.erp.model.BaseSyncCache syncCache : vl) {
				List<BaseSyncCacheDispatcher> vscdList = ((List<BaseSyncCacheDispatcher>) bmList2);

				List<BaseSyncCacheDispatcher> listDisp = new ArrayList<BaseSyncCacheDispatcher>();
				for (BaseSyncCacheDispatcher vscd : vscdList) {
					if (vscd.getSyncCacheID() == syncCache.getID()) {
						listDisp.add(vscd);
					}
					syncCache.setListSlave1(listDisp);
				}
				list.add(syncCache);
			}
			break;
		default:
			logger.info("加载失败！请重启服务器！！错误信息=" + getSyncCacheBO().getLastErrorCode() + "\t" + getSyncCacheBO().getLastErrorMessage());
			throw new RuntimeException(BaseModel.ERROR_Tag);
		}

		writeN((List<BaseModel>) list);
		// try {
		// SyncCacheManager.register(dbName, getSyncCacheType(), (SyncCache)
		// this.clone()); // 实际缓存的对象，是本对象的一个副本
		// deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
		// } catch (CloneNotSupportedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	protected int getCacheType() {
		return getSyncCacheType();
	}

	protected int getSyncCacheType() {
		throw new RuntimeException("尚未实现的方法！");
	}

	/** 注册内存，以让后面可以正常读写 */
	@Override
	protected void register(String dbName) {
		try {
			SyncCacheManager.register(dbName, EnumSyncCacheType.values()[getCacheType()], (BaseCache) this.clone()); // 实际缓存的对象，是本对象的一个副本
			deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	protected BaseBO getSyncCacheDispatcherBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseModel getNewSyncCacheModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseModel getNewSyncCacheDispatcherModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getSyncCacheBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 查询posID是否同步了块blockID <br />
	 * 前提条件：所有同存都已经加载在内存中。 <br />
	 * 因为所有同存一开始就全部加载到了内存中，后来也会不断更新，所以这个前提条件是满足的
	 * 
	 * @param blockID
	 *            块的ID
	 * @return */
	public boolean checkIfPosSyncThisBlock(int blockID, int posID) {
		boolean bSync = false;

		System.out.println("checkIfPosSyncThisBlock正在加锁....");
		lock.readLock().lock();

		// TODO:
		// 目前hashtable存储的是syncInfo，无法根据块的ID迅速定位到块及其从表信息。将来重构成去掉syncInfo，将块ID作为hashtable的key。而块内部增加一个成员，其值为从表的指针，通过主表能拿到它全部从表的信息
		for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
			BaseSyncCache syncCache = (BaseSyncCache) getCache().get(it.next());
			if (syncCache.getID() == blockID) {
				System.out.println(syncCache.getListSlave1());
				if (syncCache.getListSlave1() == null) {
					break;
				}
				for (Object o : syncCache.getListSlave1()) {
					if (((BaseSyncCacheDispatcher) o).getPos_ID() == posID) {
						bSync = true;
						break;
					}
				}
			}
		}

		lock.readLock().unlock();
		System.out.println("checkIfPosSyncThisBlock已经解锁....");

		return bSync;
	}
}
