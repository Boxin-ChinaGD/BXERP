package com.bx.erp.action.bo;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.PosMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Pos.EnumStatusPos;

@Component("posBO")
@Scope("prototype")
public class PosBO extends BaseAuthenticationBO {
	protected final String SP_POS_Create = "SP_POS_Create";
	protected final String SP_POS_RetrieveN = "SP_POS_RetrieveN";
	protected final String SP_POS_Retrieve1 = "SP_POS_Retrieve1";
	protected final String SP_POS_Update = "SP_POS_Update";
	protected final String SP_POS_Delete = "SP_POS_Delete";
	protected final String SP_POS_Retrieve1BySN = "SP_POS_Retrieve1BySN";
	protected final String SP_POS_Reset = "SP_POS_Reset";
	protected final String SP_POS_RecycleApp = "SP_POS_RecycleApp";

	@Resource
	private PosMapper posMapper;

	@Override
	protected void setMapper() {
		this.mapper = posMapper;
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_POS_Reset:
			Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
			BaseModel bm = ((PosMapper) mapper).reset(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_POS_RecycleApp:
			Map<String, Object> paramsRecycleApp = s.getUpdateParam(iUseCaseID, s);
			BaseModel bmRecycleApp = ((PosMapper) mapper).recycleApp(paramsRecycleApp);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsRecycleApp.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsRecycleApp.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bmRecycleApp;
		}
		return super.doUpdate(iUseCaseID, s);
	}

	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Login:
			Map<String, Object> params = s.getRetrieve1Param(iUseCaseID, s);
			BaseModel bm = mapper.retrieve1(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_Pos_Retrieve1BySN:
			Map<String, Object> paramsBySN = s.getRetrieve1Param(iUseCaseID, s);
			BaseModel bm2 = posMapper.retrieve1BySN(paramsBySN);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsBySN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsBySN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm2;

		}
		return super.doRetrieve1(iUseCaseID, s);
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_POS_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_POS_Reset:
			return checkStaffPermission(SYSTEM, SP_POS_Reset);
		case CASE_POS_RecycleApp:
			return checkStaffPermission(staffID, SP_POS_RecycleApp);
		default:
			return checkStaffPermission(staffID, SP_POS_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_POS_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_POS_Retrieve1);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Pos_Retrieve1BySN:
			return checkStaffPermission(SYSTEM, SP_POS_Retrieve1BySN);
		default:
			throw new RuntimeException("Not yet implemented!");
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_POS_RetrieveN);
		}
	}

	@Override
	protected boolean isActiveObject(BaseModel bm) {
		Pos pos = (Pos) bm;
		if (pos.getStatus() == EnumStatusPos.ESP_Inactive.getIndex()) {// 不可用POS
			lastErrorCode = EnumErrorCode.EC_Hack;
			lastErrorMessage = "该POS机已处于不可用状态";

			return false;
		}
		return true;
	}

}
