package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.RoleMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("roleBO")
@Scope("prototype")
public class RoleBO extends BaseBO {
	protected final String SP_Role_RetrieveN = "SP_Role_RetrieveN";
	protected final String SP_Role_Create = "SP_Role_Create";
	protected final String SP_Role_Delete = "SP_Role_Delete";
	protected final String SP_Role_Update = "SP_Role_Update";
	protected final String SP_Role_RetrieveAlsoStaff = "SP_Role_RetrieveAlsoStaff";
	@Resource
	protected RoleMapper roleMapper;

	@Override
	protected void setMapper() {
		this.mapper = roleMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Role_RetrieveAlsoStaff:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<?> ls = ((RoleMapper) mapper).retrieveAlsoStaff(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Role_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Role_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Role_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Role_RetrieveAlsoStaff:
			return checkStaffPermission(staffID, SP_Role_RetrieveAlsoStaff);
		default:
			return checkStaffPermission(staffID, SP_Role_RetrieveN);
		}
	}

}
