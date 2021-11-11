package com.bx.erp.action.bo.purchasing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.ProviderMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;

@Component("providerBO")
@Scope("prototype")
public class ProviderBO extends BaseBO {
	protected final String SP_Provider_Create = "SP_Provider_Create";
	protected final String SP_Provider_Delete = "SP_Provider_Delete";
	protected final String SP_Provider_Retrieve1 = "SP_Provider_Retrieve1";
	protected final String SP_Provider_RetrieveN = "SP_Provider_RetrieveN";
	protected final String SP_Provider_RetrieveNByFields = "SP_Provider_RetrieveNByFields";
	protected final String SP_Provider_Update = "SP_Provider_Update";

	@Resource
	private ProviderMapper providerMapper;

	@Override
	public void setMapper() {
		this.mapper = providerMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Provider provider = new Provider();
		switch (iUseCaseID) {
		case CASE_Provider_RetrieveNByFields:
			Map<String, Object> params = ((Provider) s).getRetrieveNParam(iUseCaseID, s);
			List<?> ls = ((ProviderMapper) mapper).retrieveNByFields(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = provider.getRetrieveNParam(iUseCaseID, s);
			((ProviderMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Provider_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Provider_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Provider_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Provider_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Provider_RetrieveNByFields:
			return checkStaffPermission(staffID, SP_Provider_RetrieveNByFields);
		default:
			return checkStaffPermission(staffID, SP_Provider_RetrieveN);
		}
	}

	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Provider.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkDelete(iUseCaseID, s);
	}

	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Provider.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkUpdate(iUseCaseID, s);
	}

}
