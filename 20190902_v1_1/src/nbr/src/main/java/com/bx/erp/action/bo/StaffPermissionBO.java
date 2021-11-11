package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.StaffPermissionMapper;
import com.bx.erp.model.BaseModel;

@Component("staffPermissionBO")
@Scope("prototype")
public class StaffPermissionBO extends BaseBO {
	@Resource
	private StaffPermissionMapper staffPermissionMapper;

	@Override
	protected void setMapper() {
		this.mapper = staffPermissionMapper;
	}
	
	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkUpdateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkDeleteExPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		// ...
		return true;
	}
}
