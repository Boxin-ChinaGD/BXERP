package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.StaffMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Staff.EnumStatusStaff;

@Component("staffBO")
@Scope("prototype")
public class StaffBO extends BaseAuthenticationBO {
	public static final String SP_Staff_Create = "SP_Staff_Create";
	public static final String SP_Staff_Delete = "SP_Staff_Delete";
	public static final String SP_Staff_ResetPassword = "SP_Staff_ResetPassword";
	public static final String SP_Staff_Retrieve1 = "SP_Staff_Retrieve1";
	public static final String SP_Staff_RetrieveN = "SP_Staff_RetrieveN";
	public static final String SP_Staff_Update = "SP_Staff_Update";
	public static final String SP_Staff_Update_OpenidAndUnionid = "SP_Staff_Update_OpenidAndUnionid";
	public static final String SP_Staff_Update_Unsubscribe = "SP_Staff_Update_Unsubscribe";
	@Resource
	protected StaffMapper staffMapper;

	@Override
	public void setMapper() {
		this.mapper = staffMapper;
	}
	
	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Login:
			Map<String, Object> params = s.getRetrieve1Param(iUseCaseID, s);
			BaseModel bm = ((StaffMapper) mapper).retrieve1(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		}
		return super.doRetrieve1(iUseCaseID, s);
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParam(iUseCaseID, s); 
		BaseModel bm = null;
		switch (iUseCaseID) {
		case CASE_ResetOtherPassword:
			bm = ((StaffMapper) mapper).resetPassword(params);
			
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_ResetMyPassword:
			bm = ((StaffMapper) mapper).resetPassword(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_Staff_Update_OpenidAndUnionid:
			bm = staffMapper.updateOpenidAndUnionid(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_Staff_Update_Unsubscribe:
			bm = staffMapper.updateUnsubscribe(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		}
		return super.doUpdate(iUseCaseID, s);
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount:
			return true;
		default:
			return checkStaffPermission(staffID, SP_Staff_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ResetMyPassword:
			return checkStaffPermission(staffID, SP_Staff_ResetPassword);
		case CASE_ResetOtherPassword:
			return checkStaffPermission(staffID, SP_Staff_ResetPassword);
		case CASE_Staff_Update_OpenidAndUnionid:
			return checkStaffPermission(staffID, SP_Staff_Update_OpenidAndUnionid);
		case CASE_Staff_Update_Unsubscribe:
			return checkStaffPermission(staffID, SP_Staff_Update_Unsubscribe);
		default:
			return checkStaffPermission(staffID, SP_Staff_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Staff_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Staff_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Staff_RetrieveN);
		}
	}

	@Override
	protected boolean isActiveObject(BaseModel bm) {
		Staff staff = (Staff) bm;
		if (staff.getStatus() == EnumStatusStaff.ESS_Resigned.getIndex()) {// 离职员工
			lastErrorCode = EnumErrorCode.EC_Hack;
			lastErrorMessage = "该员工已处于离职状态";

			return false;
		}
		return true;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Staff staff = (Staff) s;
		switch (iUseCaseID) {
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = staff.getRetrieveNParam(iUseCaseID, s);
			((StaffMapper) mapper).checkUniqueField(paramsCheckUniqueField);
			
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}
}
