package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.PromotionMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("promotionBO")
@Scope("prototype")
public class PromotionBO extends BaseBO {
	protected final String SP_Promotion_Create = "SP_Promotion_Create";
	protected final String SP_Promotion_Delete = "SP_Promotion_Delete";
	protected final String SP_Promotion_RetrieveN = "SP_Promotion_RetrieveN";
	protected final String SP_Promotion_Retrieve1 = "SP_Promotion_Retrieve1";
	protected final String SP_Promotion_Update = "SP_Promotion_Update";

	@Resource
	private PromotionMapper promotionMapper;

	@Override
	public void setMapper() {
		this.mapper = promotionMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Promotion_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Promotion_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Promotion_RetrieveN);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Promotion_Retrieve1);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Promotion_Update);
		}
	}

	@Override
	protected boolean doCheckRetrieveN(int iUseCaseID, BaseModel s) {
		// 非网页请求不需要检查PageSize。
		if (s.getPosID() == BaseAction.INVALID_ID) {
			if (!s.checkPageSize(s)) {
				lastErrorCode = EnumErrorCode.EC_Hack;
				lastErrorMessage = BaseModel.FIELD_ERROR_pageSize;
				return false;
			}
		}

		String err = s.checkRetrieveN(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}
}
