package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.DepartmentMapper;
import com.bx.erp.model.BaseModel;

@Component("departmentBO")
@Scope("prototype")
public class DepartmentBO extends BaseBO {
	protected final String SP_Department_RetrieveN = "SP_Department_RetrieveN";

	@Resource
	private DepartmentMapper departmentMapper;

	@Override
	protected void setMapper() {
		this.mapper = departmentMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Department_RetrieveN);
		}
	}

}
