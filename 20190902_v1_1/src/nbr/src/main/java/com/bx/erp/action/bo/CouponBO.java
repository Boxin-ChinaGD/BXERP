package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.CouponMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("couponBO")
@Scope("prototype")
public class CouponBO extends BaseBO {
	public static final String SP_Coupon_Retrieve1 = "SP_Coupon_Retrieve1";
	public static final String SP_Coupon_RetrieveN = "SP_Coupon_RetrieveN";
	public static final String SP_Coupon_Create = "SP_Coupon_Create";
	public static final String SP_Coupon_Delete = "SP_Coupon_Delete";

	@Resource
	protected CouponMapper couponMapper;

	@Override
	public void setMapper() {
		this.mapper = couponMapper;
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Coupon_Retrieve1);
		}
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Coupon_RetrieveN);
		}
	}

	@Override
	public boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Coupon_Create);
		}
	}

	@Override
	public boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Coupon_Delete);
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
