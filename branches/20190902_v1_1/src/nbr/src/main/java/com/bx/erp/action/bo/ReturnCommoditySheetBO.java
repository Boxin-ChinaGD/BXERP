package com.bx.erp.action.bo;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.ReturnCommoditySheetMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ReturnCommoditySheet;

@Component("returnCommoditySheetBO")
@Scope("prototype")
public class ReturnCommoditySheetBO extends BaseBO {
	protected final String SP_ReturnCommoditySheet_Create = "SP_ReturnCommoditySheet_Create";
	protected final String SP_ReturnCommoditySheet_Retrieve1 = "SP_ReturnCommoditySheet_Retrieve1";
	protected final String SP_ReturnCommoditySheet_RetrieveN = "SP_ReturnCommoditySheet_RetrieveN";
	protected final String SP_ReturnCommoditySheet_Update = "SP_ReturnCommoditySheet_Update";
	protected final String SP_ReturnCommoditySheet_Approve = "SP_ReturnCommoditySheet_Approve";
	@Resource
	private ReturnCommoditySheetMapper returnCommoditySheetMapper;

	@Override
	protected void setMapper() {
		this.mapper = returnCommoditySheetMapper;

	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveReturnCommoditySheet:
			Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
			ReturnCommoditySheet approve = returnCommoditySheetMapper.approve(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return approve;
		default:
			return super.doUpdate(iUseCaseID, s);
		}

	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheet_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveReturnCommoditySheet:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheet_Approve);
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheet_Update);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheet_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheet_RetrieveN);
		}
	}

}
