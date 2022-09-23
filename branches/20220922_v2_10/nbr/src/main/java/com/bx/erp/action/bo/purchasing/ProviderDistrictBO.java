package com.bx.erp.action.bo.purchasing;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.ProviderDistrictMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderDistrict;

@Component("providerDistrictBO")
@Scope("prototype")
public class ProviderDistrictBO extends BaseBO {
	protected final String SP_ProviderDistrict_Create = "SP_ProviderDistrict_Create";
	protected final String SP_ProviderDistrict_Delete = "SP_ProviderDistrict_Delete";
	protected final String SP_ProviderDistrict_Retrieve1 = "SP_ProviderDistrict_Retrieve1";
	protected final String SP_ProviderDistrict_RetrieveN = "SP_ProviderDistrict_RetrieveN";
	protected final String SP_ProviderDistrict_Update = "SP_ProviderDistrict_Update";

	@Resource
	private ProviderDistrictMapper providerDistrictMapper;

	@Override
	protected void setMapper() {
		this.mapper = providerDistrictMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderDistrict_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderDistrict_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderDistrict_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderDistrict_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderDistrict_RetrieveN);
		}
	}

	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = ProviderDistrict.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkDelete(iUseCaseID, s);
	}

	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = ProviderDistrict.ACTION_ERROR_UpdateDelete1;
			return false;
		}
		return super.checkUpdate(iUseCaseID, s);
	}

}
