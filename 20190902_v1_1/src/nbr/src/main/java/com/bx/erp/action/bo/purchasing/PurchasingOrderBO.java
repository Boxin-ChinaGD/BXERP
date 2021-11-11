package com.bx.erp.action.bo.purchasing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.PurchasingOrderMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.PurchasingOrder;

@Component("purchasingOrderBO")
@Scope("prototype")
public class PurchasingOrderBO extends BaseBO {
	protected final String SP_PurchasingOrder_Approve = "SP_PurchasingOrder_Approve";
	protected final String SP_PurchasingOrder_Create = "SP_PurchasingOrder_Create";
	protected final String SP_PurchasingOrder_Delete = "SP_PurchasingOrder_Delete";
	protected final String SP_PurchasingOrder_Retrieve1 = "SP_PurchasingOrder_Retrieve1";
	protected final String SP_PurchasingOrder_RetrieveN = "SP_PurchasingOrder_RetrieveN";
	protected final String SP_PurchasingOrder_RetrieveNByFields = "SP_PurchasingOrder_RetrieveNByFields";
	protected final String SP_PurchasingOrder_Update = "SP_PurchasingOrder_Update";
	protected final String SP_PurchasingOrder_UpdateStatus = "SP_PurchasingOrder_UpdateStatus";

	@Resource
	private PurchasingOrderMapper purchasingOrderMapper;

	@Override
	public void setMapper() {
		this.mapper = purchasingOrderMapper;

	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = ((PurchasingOrder) s).getRetrieveNParam(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_PurchasingOrder_RetrieveNByNFields:
			List<?> ls = ((PurchasingOrderMapper) mapper).retrieveNByFields(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_ApprovePurchasingOrder:
			PurchasingOrder approve = ((PurchasingOrderMapper) mapper).approve(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return approve;
		case CASE_UpdatePurchasingOrderStatus:
			PurchasingOrder updateStatus = ((PurchasingOrderMapper) mapper).updateStatus(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];

			return updateStatus;

		default:
			return super.doUpdate(iUseCaseID, s);
		}

	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrder_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApprovePurchasingOrder:
			return checkStaffPermission(staffID, SP_PurchasingOrder_Approve);
		case CASE_UpdatePurchasingOrderStatus:
			return checkStaffPermission(staffID, SP_PurchasingOrder_UpdateStatus);
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrder_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrder_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrder_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_PurchasingOrder_RetrieveNByNFields:
			return checkStaffPermission(staffID, SP_PurchasingOrder_RetrieveNByFields);
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrder_RetrieveN);
		}
	}
}
