package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.RetailTradePromotingMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradePromotingBO")
@Scope("prototype")
public class RetailTradePromotingBO extends BaseBO {
	protected final String SP_RetailTradePromoting_Retrieve1 = "SP_RetailTradePromoting_Retrieve1";
	protected final String SP_RetailTradePromoting_RetrieveN = "SP_RetailTradePromoting_RetrieveN";
	protected final String SP_RetailTradePromoting_Create = "SP_RetailTradePromoting_Create";
	@Resource
	private RetailTradePromotingMapper retailTradePromotingMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradePromotingMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradePromoting_Create);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradePromoting_Retrieve1);
		}
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradePromoting_RetrieveN);
		}
	}

}
