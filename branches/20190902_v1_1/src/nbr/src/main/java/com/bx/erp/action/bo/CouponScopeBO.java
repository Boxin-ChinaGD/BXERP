package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.CouponScopeMapper;
import com.bx.erp.model.BaseModel;

@Component("couponScopeBO")
@Scope("prototype")
public class CouponScopeBO extends BaseBO {
	protected final String SP_CouponScope_RetrieveN = "SP_CouponScope_RetrieveN";
	protected final String SP_CouponScope_Create = "SP_CouponScope_Create";

	@Resource
	private CouponScopeMapper couponScopeMapper;

	@Override
	protected void setMapper() {
		this.mapper = couponScopeMapper;
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CouponScope_RetrieveN);
		}
	}
	
	@Override
	public boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CouponScope_Create);
		}
	}
}
