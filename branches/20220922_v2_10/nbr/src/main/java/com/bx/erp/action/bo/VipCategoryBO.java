package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.VipCategoryMapper;
import com.bx.erp.model.BaseModel;

@Component("vipCategoryBO")
@Scope("prototype")
public class VipCategoryBO extends BaseBO {
	protected final String SP_VIPCategory_Create = "SP_VIPCategory_Create";
	protected final String SP_VIPCategory_Delete = "SP_VIPCategory_Delete";
	protected final String SP_VIPCategory_RetrieveN = "SP_VIPCategory_RetrieveN";
	protected final String SP_VIPCategory_Update = "SP_VIPCategory_Update";
	protected final String SP_VIPCategory_Retrieve1 = "SP_VIPCategory_Retrieve1";
	
	@Resource
	protected VipCategoryMapper vipCategoryMapper;

	@Override
	public void setMapper() {
		this.mapper = vipCategoryMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIPCategory_Create);
		}

	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIPCategory_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIPCategory_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIPCategory_RetrieveN);
		}
	}
	
	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIPCategory_Retrieve1);
		}
	}

}
