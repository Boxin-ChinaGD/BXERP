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
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;

@Component("staffCache")
@Scope("prototype")
public class StaffCache extends BaseCache {
	private Log logger = LogFactory.getLog(StaffCache.class);

	public Hashtable<String, BaseModel> ht;

	@Resource
	private StaffBO staffBO;

	public StaffCache() {
		sCacheName = "门店人员信息";
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Staff staff = new Staff();
		staff.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		staff.setStatus(BaseAction.INVALID_STATUS); // 加载所有的门店人员信息
		staff.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(staff.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		staff.setPageSize(Integer.parseInt(ccs.getValue()));

		return staff;
	}

	@Override
	protected BaseBO getMasterBO() {
		return staffBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Staff.getIndex();
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return ht;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		ht = listToHashtable(list);
	}

	/** 门店人员信息在服务器一启动时就全部加载，不需要在运行过程中加载其中一个门店人员信息。<br />
	 * 如果在运行过程中，门店人员被增加了，会在写DB后写进缓存中。<br />
	 * 通过retrieve1寻找一个不存在的门店人员会直接return null */
	/*	@Override
		protected BaseModel retrieve1(int id, int staffID, ErrorInfo ecOut, String dbName) {
			return null;
		}*/

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_StaffCacheSize.getIndex(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}
}
