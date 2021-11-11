package com.bx.erp.action.bo.message;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.message.MessageMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("messageBO")
@Scope("prototype")
public class MessageBO extends BaseBO {
	protected final String SP_Message_Create = "SP_Message_Create";
	protected final String SP_Message_Update = "SP_Message_Update";
	protected final String SP_Message_Retrieve1 = "SP_Message_Retrieve1";
	protected final String SP_Message_RetrieveN = "SP_Message_RetrieveN";
	protected final String SP_Message_RetrieveNForWx = "SP_Message_RetrieveNForWx";
	protected final String SP_Message_UpdateStatus = "SP_Message_UpdateStatus";

	@Resource
	private MessageMapper messageMapper;

	@Override
	protected void setMapper() {
		this.mapper = messageMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Message_RetrieveNForWx:
			Map<String, Object> params = s.getRetrieveNParam(CASE_Message_RetrieveNForWx, s);
			List<?> ls = ((MessageMapper) mapper).messageRetrieveNForWx(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		}
		return super.doRetrieveN(iUseCaseID, s);
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Message_UpdateStatus:
			Map<String, Object> params = s.getUpdateParam(CASE_Message_UpdateStatus, s);
			BaseModel bm = ((MessageMapper) mapper).updateStatus(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		}
		return super.doUpdate(iUseCaseID, s);
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Message_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Message_Update);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Message_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Message_RetrieveNForWx:
			return checkStaffPermission(staffID, SP_Message_RetrieveNForWx);
		default:
			return checkStaffPermission(staffID, SP_Message_RetrieveN);
		}
	}

}
