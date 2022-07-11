package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.PermissionMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("permissionBO")
@Scope("prototype")
public class PermissionBO extends BaseBO {
	protected final String SP_Permission_RetrieveN = "SP_Permission_RetrieveN";
	protected final String SP_Permission_Create = "SP_Permission_Create";
	protected final String SP_Permission_Delete = "SP_Permission_Delete";
	protected final String SP_Permission_RetrieveAlsoRoleStaff = "SP_Permission_RetrieveAlsoRoleStaff";
	
	@Resource
	protected PermissionMapper permissionMapper;

	@Override
	protected void setMapper() {
		this.mapper = permissionMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Permission_RetrieveAlsoRoleStaff:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<?> ls = ((PermissionMapper) mapper).retrieveAlsoRoleStaff(params);

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
			return checkStaffPermission(staffID, SP_Permission_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Permission_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Permission_RetrieveAlsoRoleStaff);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
//		case CASE_RetrieveCommodityByNFields:
//			return checkStaffPermission(staffID, SP_Permission_RetrieveN);
		default:
			return checkStaffPermission(staffID, SP_Permission_RetrieveN);
		}
	}
	
	
}
