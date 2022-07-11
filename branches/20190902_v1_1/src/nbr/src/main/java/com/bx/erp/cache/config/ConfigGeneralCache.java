package com.bx.erp.cache.config;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.config.ConfigGeneralBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.config.ConfigGeneral;

@Component("configGeneralCache")
@Scope("prototype")
public class ConfigGeneralCache extends ConfigCache {
	public Hashtable<String, BaseModel> htGeneral;

	@Resource
	private ConfigGeneralBO configGeneralBO;

	public ConfigGeneralCache() {
		sCacheName = "配置表";
	}
	
	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_ConfigGeneral.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return configGeneralBO;
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
	protected BaseModel getMasterModel(String dbName) {
		ConfigGeneral cg = new ConfigGeneral();
		cg.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		
		return cg;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htGeneral;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htGeneral = listToHashtable(list);	
	}
}
