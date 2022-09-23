package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.BrandMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;

@Component("brandBO")
@Scope("prototype")
public class BrandBO extends BaseBO {
	protected final String SP_Brand_RetrieveN = "SP_Brand_RetrieveN";
	protected final String SP_Brand_Create = "SP_Brand_Create";
	protected final String SP_Brand_Retrieve1 = "SP_Brand_Retrieve1";
	protected final String SP_Brand_Update = "SP_Brand_Update";
	protected final String SP_Brand_Delete = "SP_Brand_Delete";

	@Resource
	protected BrandMapper brandMapper;

	@Override
	public void setMapper() {
		this.mapper = brandMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Brand_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Brand_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Brand_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Brand_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Brand_RetrieveN);
		}
	}

	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Brand.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkUpdate(iUseCaseID, s);
	}

	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Brand.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkDelete(iUseCaseID, s);
	}
}
