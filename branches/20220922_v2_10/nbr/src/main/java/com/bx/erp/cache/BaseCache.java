package com.bx.erp.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

/** 小块数据的缓存类。 如果需要经常读取小块缓存，偶尔更新缓存，可以使用本类的子类。 */
@Component("baseCache")
@Scope("prototype")
public abstract class BaseCache implements Cloneable {
	private Log logger = LogFactory.getLog(BaseCache.class);

	protected static final int CACHESIZE_Infinite = 1024 * 1024 * 1024;

	// ...公共DB的配置
	// 若将来增加配置项，要注意CompanyBusinessLicensePictureDir和CompanyAPICertVolumeMax被引用的地方，作相应的修改
	public static final int CompanyBusinessLicensePictureDir = 1;
	public static final int CompanyBusinessLicensePictureVolumeMax = 2;
	public static final int CommodityNOStart = 3;
	public static final int CommodityPurchasingPriceStart = 4;
	public static final int CommodityLogoDir = 5;
	public static final int CompanyAPICertDir = 6;
	public static final int CompanyAPICertVolumeMax = 7;
	public static final int MAX_SmallSheetNumber = 8;
	public static final int MAX_SmallSheetLogoVolume = 9;
	public static final int MaxRequestCountIn1Day = 10;
	public static final int MaxRequestCountIn1Min = 11;
	public static final int MaxRequestIncrementPercent = 12;
	// 若将来增加配置项，要注意CompanyLogoDir被引用的地方，作相应的修改
	public static final int CompanyLogoDir = 13;
	public static final int CompanyLogoVolumeMax = 14;
	public static final int MaxLoginCountIn1Day = 15;
	public static final int MAX_IMPORT_FILE_SIZE = 16;
	public static final int EXTRA_DISK_SPACE_SIZE = 17;

	// 以下ID和DB里的F_ID必须相等
	public static final int PAGESIZE = 1;
	public static final int PurchasingTimeoutTaskScanStartTime = 2;
	public static final int PurchasingTimeoutTaskScanEndTime = 3;
	public static final int PurchasingTimeoutTaskFlag = 4;
	public static final int UsalableCommodityTaskScanStartTime = 5;
	public static final int UsalableCommodityTaskScanEndTime = 6;
	public static final int UsalableCommodityTaskFlag = 7;
	public static final int ShelfLifeTaskScanStartTime = 8;
	public static final int ShelfLifeTaskScanEndTime = 9;
	public static final int ShelfLifeTaskFlag = 10;
	public static final int MaxVicePackageUnit = 11;
	public static final int MaxProviderNOPerCommodity = 12;
	public static final int MaxPurchasingOrderCommodityNO = 13;
	public static final int CommodityLogoVolumeMax = 14;
	public static final int MaxWarehousingCommodityNO = 15;
	public static final int RetailTradeDailyReportSummaryTaskScanStartTime = 16;
	public static final int RetailTradeDailyReportSummaryTaskScanEndTime = 17;
	public static final int RetailTradeMonthlyReportSummaryTaskScanStartTime = 18;
	public static final int RetailTradeMonthlyReportSummaryTaskScanEndTime = 19;
	public static final int RetailTradeDailyReportByCategoryParentTaskScanStartTime = 20;
	public static final int RetailTradeDailyReportByCategoryParentTaskScanEndTime = 21;
	public static final int RetailTradeDailyReportByStaffTaskScanStartTime = 22;
	public static final int RetailTradeDailyReportByStaffTaskScanEndTime = 23;
	public static final int ACTIVE_SMALLSHEET_ID = 24;
	public static final int BonusTaskScanStartTime = 25;
	public static final int BonusTaskScanEndTime = 26;
	// 若将来增加配置项，要注意PAGESIZE和ACTIVE_SMALLSHEET_ID被引用的地方，作相应的修改

	/** 默认缓存对象的个数。<br />
	 * 如果无法从DB中加载某种对象的缓存配置的个数，则采用本个数。<br />
	 * 实际上这种情况永远不会发生，因为服务器启动时就加载了这些信息。如果加载不成功，会重启服务器。 */
	public static final int CACHESIZE_Default = 10;

	protected ReentrantReadWriteLock lock;

	protected int iReadCacheHitNO = 0;

	protected String sCacheName;

	public BaseCache() {
		lock = new ReentrantReadWriteLock();
	}

	/** 每次从缓存中读取数据，都只是读取缓存的一个copy，因此需要clone缓存的操作。
	 * 
	 * @param bSort
	 *            如果需要对clone出来的data进行排序，则设为true，并将@param bm实例化
	 * @param bm
	 *            如果想升序，设置bm.setIsAsc(true)。如果想降序，设置bm.setIsAsc(false)。
	 * @return */
	public List<BaseModel> cloneData(boolean bSort, BaseModel bm) {
		List<BaseModel> ls = new ArrayList<BaseModel>(getCache().size());
		for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
			try {
				ls.add(getCache().get(it.next()).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		if (bSort) {
			Collections.sort(ls, bm);
		}

		return ls;
	}

	/** 从缓存中clone一行数据 */
	public BaseModel read1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		BaseModel bmToRead = null;
		//
		lock.readLock().lock();
		try {
			bmToRead = doRead1(id, staffID, ecOut, dbName);
		} catch (Exception e) {
			logger.error("read1出错，信息：" + e.getMessage() + "\nID=" + id);
		}
		lock.readLock().unlock();

		return bmToRead;
	}

	protected BaseModel doRead1(int id, int staffID, ErrorInfo ecOut, String dbName) {
		ecOut.setErrorCode(EnumErrorCode.EC_NoError);
		// 从缓存中查找该行数据并返回之
		if (getCache().containsKey(String.valueOf(id))) {
			++iReadCacheHitNO;
			try {
				return getCache().get(String.valueOf(id)).clone();
			} catch (CloneNotSupportedException e) {
				logger.info("doRead1()异常" + e.getMessage());
			}
		}

		// 缓存中没有该行数据，则让子类从DB中获取并加入缓存
		BaseModel ret = null;
		BaseModel bm = retrieve1(id, staffID, ecOut, dbName);
		if (bm != null) {
			if (getCache().size() + 1 > getMaxCacheNumber(dbName, staffID)) { // 避免将最近添加的值remove掉
				for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
					getCache().remove(it.next());
					break;
				}
			}
			getCache().put(String.valueOf(bm.getID()), bm);
			try {
				ret = bm.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/** 从缓存中clone出全部数据 */
	public List<BaseModel> readN(boolean bSort, boolean bAsc) {
		// System.out.println("readN正在加锁....");

		List<BaseModel> ls = null;

		lock.readLock().lock();
		try {
			ls = doReadN(bSort, bAsc);
			++iReadCacheHitNO;
		} catch (Exception e) {
			logger.error("readN出错，信息：" + e.getMessage());
		}
		lock.readLock().unlock();

		// System.out.println("readN已经解锁....");

		return ls;
	}

	protected List<BaseModel> doReadN(boolean bSort, boolean bAsc) {
		BaseModel bm = null;
		if (bSort) {
			bm = new BaseModel();
			bm.setIsASC(bAsc ? EnumBoolean.EB_Yes.getIndex() : EnumBoolean.EB_NO.getIndex());
		}
		return cloneData(bSort, bm);

	}

	/** 在缓存中删除之前的相同ID的U型 */
	public void deleteOldU(BaseSyncCache o) {
		if (o == null) {
			logger.info("对象为null，不会删除普通缓存");
			return;
		}

		System.out.println("deleteOldU正在加锁...." + lock.writeLock().getHoldCount());
		lock.writeLock().lock();
		try {
			List<String> idList = new ArrayList<String>();
			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
				String key = it.next();
				BaseSyncCache bsc = (BaseSyncCache) getCache().get(key);
				if (bsc.getSyncData_ID() == o.getSyncData_ID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_U)) {
					idList.add(key);
					logger.info("要删除的BaseSyncCache：" + bsc.toString());
				}
			}
			for (String id : idList) {
				getCache().remove(id);
			}
		} catch (Exception e) {
			logger.error("deleteOldU()异常：" + e.getMessage() + "\n对象为：" + o);
		}

		lock.writeLock().unlock();
		System.out.println("deleteOldU已经解锁" + lock.writeLock().getHoldCount());
	}

	/** 向缓存中删除一行数据 */
	public void delete1(BaseModel o) {
		System.out.println("delete1正在加锁...." + lock.writeLock().getHoldCount());
		lock.writeLock().lock();
		try {
			doDelete1(o);
		} catch (Exception e) {
			logger.error("delete1异常:" + e.getMessage() + "\n对象为：" + o);
		}
		lock.writeLock().unlock();
		System.out.println("delete1已经解锁" + lock.writeLock().getHoldCount());
	}

	protected void doDelete1(BaseModel o) {
		if (o == null) {
			logger.info("对象为null，不会删除普通缓存");
			return;
		}
		getCache().remove(String.valueOf(o.getID()));
	}

	/** 向缓存中写入一行数据。如果数据重复则更新之，否则加入这行新的数据。<br />
	 * 常犯错误：
	 * 
	 * <pre>
	 * {@code 
	 * CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdate, BaseAction.DBName_Public, staffID());
	 * companyUpdate.setKey(""); //companyUpdate被写进缓存后，又去修改companyUpdate
	 * companyUpdate.setDbUserPassword(""); //companyUpdate被写进缓存后，又去修改companyUpdate
	 * }
	 * </pre>
	 * 
	 * 正确的做法是：
	 * 
	 * <pre>
	 * {@code
	 * CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdate, BaseAction.DBName_Public, staffID());
	 * companyUpdate = (Company)companyUpdate.clone(); //clone一个对象
	 * companyUpdate.setKey("");
	 * companyUpdate.setDbUserPassword("");
	 * }
	 * </pre>
	 * </p>
	 */
	public void write1(BaseModel o, String dbName, int staffID) {
		System.out.println("write1正在加锁...." + lock.writeLock().getHoldCount());
		lock.writeLock().lock();
		try {
			// 对非Prod的对象进行一般检查。对消除难解的bug有帮助
			// if (o != null && BaseAction.ENV != BaseAction.EnumEnv.PROD) {
			// smallsheetframe.checkCreate()需要传递DB name
			// String err = o.checkCreate(BaseBO.INVALID_CASE_ID);//
			// 这里只用这个BaseBO.INVALID_CASE_ID，不检测其它case ID，会造成误会，所以下面说“疑似不健康的数据”
			// if (err.length() != 0) {
			// logger.debug("疑似不健康的数据：" + o);
			// }
			// }

			doWrite1(o, dbName, staffID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("write1异常:" + e.getMessage() + "\n对象为：" + o);
		}
		lock.writeLock().unlock();
		System.out.println("write1已经解锁" + lock.writeLock().getHoldCount());
	}

	protected void doWrite1(BaseModel o, String dbName, int staffID) {
		if (o == null) {
			logger.info("对象为null，不会加入普通缓存");
			return;
		}
		getCache().put(String.valueOf(o.getID()), o);
		if (getCache().size() + 1 > getMaxCacheNumber(dbName, staffID)) { // 可能会将最近添加的值remove掉
			for (Iterator<String> it = getCache().keySet().iterator(); it.hasNext();) {
				getCache().remove(it.next());
				break;
			}
		}
	}

	/** 某种缓存的最大缓存对象个数。默认值2^30，近似于不限个数。<br />
	 * 如果需要限制个数，需要在DB中设置值 */
	protected int getMaxCacheNumber(String dbName, int staffID) {
		return CACHESIZE_Infinite;
	}

	/** 清除所有旧的缓存，写入新的数据 <br />
	 * TODO：目前存在的问题：load()中缓存加载时，并不能确定要加载多少个对象。 */
	public void writeN(List<BaseModel> ls) {
		System.out.println("writeN正在加锁...." + lock.writeLock().getHoldCount());
		lock.writeLock().lock();
		try {
			doWriteN(ls);
		} catch (Exception e) {
			logger.error("writeN异常:" + e.getMessage());
		}
		lock.writeLock().unlock();
		System.out.println("writeN已经解锁" + lock.writeLock().getHoldCount());
	}

	protected void doWriteN(List<BaseModel> ls) {
		setCache(ls);
	}

	/** 清除所有旧的缓存，但是不写入新的数据 */
	public void deleteAll() {
		System.out.println("deleteAll正在加锁...." + lock.writeLock().getHoldCount());
		lock.writeLock().lock();
		try {
			doDeleteAll();
		} catch (Exception e) {
			logger.error("deleteAll异常:" + e.getMessage());
		}
		lock.writeLock().unlock();
		System.out.println("deleteAll已经解锁" + lock.writeLock().getHoldCount());
	}

	protected void doDeleteAll() {
		setCache(new ArrayList<BaseModel>());
	}

	// 子类通过实现本方法，获取自己的缓存
	protected abstract Hashtable<String, BaseModel> getCache();

	/** 子类通过实现本方法，设置自己的缓存 <br />
	 * 由于本类在此前已经被浅拷贝，所以实现本方法时，必须保证不污染到原来的数据成员 */
	protected abstract void setCache(List<BaseModel> list);

	public void printStatistics() {
		logger.info("缓存（读）击中次数（" + sCacheName + "）：" + iReadCacheHitNO);
	}

	/** 浅复制一个list到Hashtable中。在外部不应再对list中的元素进行任何读写操作，否则很可能会引起严重问题
	 * 
	 * @param ls
	 * @return */
	protected Hashtable<String, BaseModel> listToHashtable(List<BaseModel> ls) {
		if (ls == null) {
			throw new RuntimeException("参数ls为null！");
		}
		Hashtable<String, BaseModel> ht = new Hashtable<String, BaseModel>(ls.size());
		for (BaseModel bm : ls) {
			ht.put(String.valueOf(bm.getID()), bm);
		}

		return ht;
	}

	/** 加载从表信息 */
	protected void doLoadSlave(List<?> listMaster, String dbName) {
		if (getSlaveBO() == null) {
			return;
		}
		for (Object bm : listMaster) {
			DataSourceContextHolder.setDbName(dbName);
			List<?> slaveList = getSlaveBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, getSlaveBaseModel((BaseModel) bm));
			if (getSlaveBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("加载缓存（" + sCacheName + "）的从表失败！请重启服务器！！错误信息：" + getMasterBO().printErrorInfo());
				throw new RuntimeException(BaseModel.ERROR_Tag);
			}
			((BaseModel) bm).setListSlave1(slaveList);
		}
	}

	public void load(String dbName) {
		doLoad(dbName);

		register(dbName);
	}

	/** 注册内存，以让后面可以正常读写 */
	protected void register(String dbName) {
		try {
			CacheManager.register(dbName, EnumCacheType.values()[getCacheType()], (BaseCache) this.clone()); // 实际缓存的对象，是本对象的一个副本
			deleteAll();// 清空本对象的缓存，本对象的副本的hashtable仍然有数据，没有被清除
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected void doLoad(String dbName) {
		logger.info("加载缓存（" + sCacheName + "）...");
		BaseModel b = getMasterModel(dbName);
		DataSourceContextHolder.setDbName(dbName);
		List<?> ls = getMasterBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, b);
		if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("加载缓存（" + sCacheName + "）失败！请重启服务器！！错误信息：" + getMasterBO().printErrorInfo());
			throw new RuntimeException(BaseModel.ERROR_Tag);
		}
		doLoadSlave(ls, dbName);

		logger.info("加载缓存（" + sCacheName + "）成功！");
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	protected int getCacheType() {
		throw new RuntimeException("尚未实现的方法！");
	}

	protected BaseBO getMasterBO() {
		throw new RuntimeException("尚未实现的方法！");
	}

	protected BaseBO getSlaveBO() {
		throw new RuntimeException("尚未实现的方法！");
	}

	protected BaseModel getMasterModel(String dbName) {
		throw new RuntimeException("尚未实现的方法！");
	}

	protected BaseModel getSlaveBaseModel(BaseModel master) {
		throw new RuntimeException("尚未实现的方法！");
	}

	/** 从缓存中读不到对象时，会从DB中读取。如果该对象有从表，则其从表也会读取并设置进主表 */
	protected BaseModel retrieve1(int id, int StaffID, ErrorInfo ecOut, String dbName) {
		BaseModel baseModel = getMasterModel(dbName);
		baseModel.setID(id);
		DataSourceContextHolder.setDbName(dbName);
		if (getSlaveBO() != null) {// 有从表
			List<List<BaseModel>> bmList = getMasterBO().retrieve1ObjectEx(StaffID, BaseBO.INVALID_CASE_ID, baseModel);
			if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
				ecOut.setErrorCode(getMasterBO().getLastErrorCode());
				ecOut.setErrorMessage(getMasterBO().getLastErrorMessage());
				return null;
			}

			List<BaseModel> masterList = bmList.get(0);
			List<BaseModel> slaveList = (List<BaseModel>) bmList.get(1);
			masterList.get(0).setListSlave1(slaveList);

			return masterList.get(0);
		} else { // 没有从表
			BaseModel bm = getMasterBO().retrieve1Object(StaffID, BaseBO.INVALID_CASE_ID, baseModel);
			if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
				ecOut.setErrorCode(getMasterBO().getLastErrorCode());
				ecOut.setErrorMessage(getMasterBO().getLastErrorMessage());
				return null;
			}

			return bm;
		}
	}
}
