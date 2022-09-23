package com.bx.erp.cache.publiC;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.BxStaffBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.CacheType.EnumCacheType;

@Component("bxStaffCache")
@Scope("prototype")
public class BxStaffCache extends BaseCache {
	public Hashtable<String, BaseModel> htBxStaff;

	@Resource
	private BxStaffBO bxStaffBO;

	public BxStaffCache() {
		sCacheName = "博昕员工";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_BxStaff.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return bxStaffBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		return new BxStaff();
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htBxStaff;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htBxStaff = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		return 50; // ...TODO 一台云服务器支持最多50个博昕员工
	}
}
