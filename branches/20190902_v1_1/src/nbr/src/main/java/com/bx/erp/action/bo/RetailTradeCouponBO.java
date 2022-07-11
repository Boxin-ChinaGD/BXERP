package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.RetailTradeCouponMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeCouponBO")
@Scope("prototype")
public class RetailTradeCouponBO extends BaseBO {
	protected final String SP_RetailTradeCoupon_Create = "SP_RetailTradeCoupon_Create";
	protected final String SP_RetailTradeCoupon_RetrieveN = "SP_RetailTradeCoupon_RetrieveN";
	
	@Resource
	private RetailTradeCouponMapper retailTradeCouponMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeCouponMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeCoupon_Create);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeCoupon_RetrieveN);
		}
	}
}
