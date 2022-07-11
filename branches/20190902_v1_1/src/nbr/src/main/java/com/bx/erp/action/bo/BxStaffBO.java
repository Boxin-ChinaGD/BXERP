package com.bx.erp.action.bo;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.BxStaffMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("bxStaffBO")
@Scope("prototype")
public class BxStaffBO extends BaseAuthenticationBO {
	protected final String SP_BxStaff_Retrieve1 = "SP_BxStaff_Retrieve1";
	protected final String SP_BxStaff_RetrieveN = "SP_BxStaff_RetrieveN";

	@Resource
	private BxStaffMapper bxStaffMapper;

	@Override
	protected void setMapper() {
		this.mapper = bxStaffMapper;
	}

	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel bs) {
		switch (iUseCaseID) {
		case CASE_Login:
			Map<String, Object> params = bs.getRetrieve1Param(iUseCaseID, bs);
			BaseModel bm = ((BxStaffMapper) mapper).retrieve1(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		}
		return super.doRetrieve1(iUseCaseID, bs);
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		// switch (iUseCaseID) {
		// default:
		// return checkStaffPermission(staffID, SP_BxStaff_RetrieveN);
		// }
		// bxStaff暂时没有检查权限
		return true;
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		// switch (iUseCaseID) {
		// default:
		// return checkStaffPermission(staffID, SP_BxStaff_RetrieveN);
		// }
		// bxStaff暂时没有检查权限
		return true;
	}

	@Override
	protected boolean isActiveObject(BaseModel bm) {
		return true;
	}
}
