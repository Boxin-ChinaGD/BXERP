package com.bx.erp.cache.config;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.config.BxConfigGeneralBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.BxConfigGeneral;

@Component("bxConfigGeneralCache")
public class BxConfigGeneralCache extends ConfigCache {
	public Hashtable<String, BaseModel> htBXConfigGeneral;

	@Resource
	private BxConfigGeneralBO bxConfigGeneralBO;

	public BxConfigGeneralCache() {
		sCacheName = "公共配置表";
	}
	
	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_BXConfigGeneral.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return bxConfigGeneralBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		return new BxConfigGeneral();
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htBXConfigGeneral;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htBXConfigGeneral = listToHashtable(list);
	}
}
