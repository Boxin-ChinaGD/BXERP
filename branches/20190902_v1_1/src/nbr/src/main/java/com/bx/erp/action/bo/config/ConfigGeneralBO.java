package com.bx.erp.action.bo.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.ConfigGeneralMapper;
import com.bx.erp.model.BaseModel;

@Component("configGeneralBO")
@Scope("prototype")
public class ConfigGeneralBO extends ConfigBO {
	protected final String SP_ConfigGeneral_Retrieve1 = "SP_ConfigGeneral_Retrieve1";
	protected final String SP_ConfigGeneral_RetrieveN = "SP_ConfigGeneral_RetrieveN";
	protected final String SP_ConfigGeneral_Update = "SP_ConfigGeneral_Update";

	@Resource
	private ConfigGeneralMapper configGeneralMapper;

	@Override
	protected void setMapper() {
		this.mapper = configGeneralMapper;

	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigGeneral_Update);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigGeneral_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ConfigGeneral_RetrieveN);
		}
	}

}
