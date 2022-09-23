package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.CompanyMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

@Component("companyBO")
@Scope("prototype")
public class CompanyBO extends BaseBO {
	protected final String SP_Company_Create = "SP_Company_Create";

	protected final String SP_Company_Retrieve1 = "SP_Company_Retrieve1";

	protected final String SP_Company_RetrieveN = "SP_Company_RetrieveN";

	protected final String SP_Company_Delete = "SP_Company_Delete";

	protected final String SP_Company_Update = "SP_Company_Update";

	protected final String SP_Company_UpdateSubmchid = "SP_Company_UpdateSubmchid";
	
	protected final String SP_Company_MatchVip = "SP_Company_MatchVip";

	@Resource
	protected CompanyMapper companyMapper;

	@Override
	protected void setMapper() {
		this.mapper = companyMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			// return checkStaffPermission(staffID, SP_Company_Create);
			return true; // ...
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			// return checkStaffPermission(staffID, SP_Company_Retrieve1);
			return true; // ...
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		// BXstaff还没有相应的检查权限功能
		default:
			// return checkStaffPermission(staffID, SP_Company_Update);
			return true; // ...
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			// return checkStaffPermission(staffID, SP_Company_Delete);
			return true; // ...
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			// return checkStaffPermission(staffID, SP_Company_RetrieveN);
			return true; // ...
		}
	}
	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			// return checkStaffPermission(staffID, SP_Company_RetrieveN);
			return true; // ...
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Company company = (Company) s;
		Map<String, Object> params = company.getUpdateParam(iUseCaseID, company);
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_UpdateSubmchid:
			BaseModel bmAfterUpdateSubmchid = ((CompanyMapper) mapper).updateSubmchid(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return bmAfterUpdateSubmchid;
		case BaseBO.CASE_Company_updateVipSystemTip:
			BaseModel bmAfterUpdateVipSystemTip = ((CompanyMapper) mapper).updateVipSystemTip(params);
			
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			
			return bmAfterUpdateVipSystemTip;
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return super.doRetrieve1(iUseCaseID, s);
		}
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Company company = (Company) s;
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = company.getRetrieveNParam(iUseCaseID, s);
			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
			((CompanyMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		case BaseBO.CASE_Company_retrieveNByVipMobile:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<BaseModel> ls =((CompanyMapper)mapper).retrieveNByVipMobile(params);

			if (params.get(BaseAction.SP_OUT_PARAM_iTotalRecord) != null) {
				setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
			} else {
				setTotalRecord(0);
			}

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}
	
	@Override
	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_matchVip:
			Map<String, Object> paramsMatchVip = s.getRetrieveNParamEx(iUseCaseID, s);
			List<List<BaseModel>> ls2 = ((CompanyMapper)mapper).matchVip(paramsMatchVip);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsMatchVip.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsMatchVip.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls2;
		default:
			return super.doRetrieveNEx(iUseCaseID, s);
		}
		
	}
}