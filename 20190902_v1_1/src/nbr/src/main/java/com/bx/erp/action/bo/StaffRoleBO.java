package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.StaffRoleMapper;
import com.bx.erp.model.BaseModel;

@Component("staffRoleBO")
@Scope("prototype")
public class StaffRoleBO extends BaseBO {
	protected final String SP_StaffRole_RetrieveN = "SP_StaffRole_RetrieveN";
	protected final String SP_StaffRole_Retrieve1 = "SP_StaffRole_Retrieve1";
	
	@Resource
	private StaffRoleMapper staffRoleMapper;

	@Override
	protected void setMapper() {
			this.mapper = staffRoleMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_StaffRole_RetrieveN);
		}
	}
	
	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_StaffRole_Retrieve1);
		}
	}
}
