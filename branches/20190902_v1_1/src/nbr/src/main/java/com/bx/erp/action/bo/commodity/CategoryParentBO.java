package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CategoryParentMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CategoryParent;

@Component("categoryParentBO")
@Scope("prototype")
public class CategoryParentBO extends BaseBO {
	protected final String SP_CategoryParent_Create = "SP_CategoryParent_Create";
	protected final String SP_CategoryParent_Update = "SP_CategoryParent_Update";
	protected final String SP_CategoryParent_Delete = "SP_CategoryParent_Delete";
	protected final String SP_CategoryParent_Retrieve1 = "SP_CategoryParent_Retrieve1";
	protected final String SP_CategoryParent_RetrieveN = "SP_CategoryParent_RetrieveN";

	@Resource
	protected CategoryParentMapper categoryParentMapper;

	@Override
	public void setMapper() {
		this.mapper = categoryParentMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategoryParent_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategoryParent_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategoryParent_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategoryParent_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategoryParent_RetrieveN);
		}
	}

	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = CategoryParent.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkDelete(iUseCaseID, s);
	}

	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = CategoryParent.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkUpdate(iUseCaseID, s);
	}

}
