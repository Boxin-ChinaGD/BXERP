package com.bx.erp.action.bo.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.BxConfigGeneralMapper;
import com.bx.erp.model.BaseModel;

@Component("bxConfigGeneralBO")
@Scope("prototype")
public class BxConfigGeneralBO extends ConfigBO {
	protected final String SP_BXConfigGeneral_Retrieve1 = "SP_BXConfigGeneral_Retrieve1";
	protected final String SP_BXConfigGeneral_RetrieveN = "SP_BXConfigGeneral_RetrieveN";
	protected final String SP_BXConfigGeneral_Update = "SP_BXConfigGeneral_Update";

	@Resource
	private BxConfigGeneralMapper bxConfigGeneralMapper;

	@Override
	protected void setMapper() {
		this.mapper = bxConfigGeneralMapper;

	}
	
	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

}
