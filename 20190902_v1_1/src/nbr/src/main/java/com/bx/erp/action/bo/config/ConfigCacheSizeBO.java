package com.bx.erp.action.bo.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.ConfigCacheSizeMapper;
import com.bx.erp.model.BaseModel;

@Component("configCacheSizeBO")
@Scope("prototype")
public class ConfigCacheSizeBO extends ConfigBO {
	protected final String SP_ConfigCacheSize_Retrieve1 = "SP_ConfigCacheSize_Retrieve1";
	protected final String SP_ConfigCacheSize_RetrieveN = "SP_ConfigCacheSize_RetrieveN";
	protected final String SP_ConfigCacheSize_Update = "SP_ConfigCacheSize_Update";

	@Resource
	private ConfigCacheSizeMapper configCacheSizeMapper;

	@Override
	protected void setMapper() {
		this.mapper = configCacheSizeMapper;
	}
	
	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigCacheSize_Update);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigCacheSize_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigCacheSize_RetrieveN);
		}
	}

}
