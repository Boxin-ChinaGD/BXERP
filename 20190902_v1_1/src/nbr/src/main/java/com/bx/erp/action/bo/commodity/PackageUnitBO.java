package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.PackageUnitMapper;
import com.bx.erp.model.BaseModel;

@Component("packageUnitBO")
@Scope("prototype")
public class PackageUnitBO extends BaseBO {
	protected final String SP_PackageUnit_RetrieveN = "SP_PackageUnit_RetrieveN";
	protected final String SP_PackageUnit_Create = "SP_PackageUnit_Create";
	protected final String SP_PackageUnit_Update = "SP_PackageUnit_Update";
	protected final String SP_PackageUnit_Delete = "SP_PackageUnit_Delete";
	protected final String SP_PackageUnit_Retrieve1 = "SP_PackageUnit_Retrieve1";

	@Resource
	private PackageUnitMapper packageUnitMapper;

	@Override
	protected void setMapper() {
		this.mapper = packageUnitMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PackageUnit_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PackageUnit_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PackageUnit_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PackageUnit_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PackageUnit_RetrieveN);
		}
	}

}
