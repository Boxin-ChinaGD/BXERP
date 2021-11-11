package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.RetailTradePromotingFlowMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradePromotingFlowBO")
@Scope("prototype")
public class RetailTradePromotingFlowBO extends BaseBO {
	protected final String SP_RetailTradePromotingFlow_Create = "SP_RetailTradePromotingFlow_Create";
	protected final String SP_RetailTradePromotingFlow_RetrieveN = "SP_RetailTradePromotingFlow_RetrieveN";
	@Resource
	private RetailTradePromotingFlowMapper retailTradePromotingFlowMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradePromotingFlowMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradePromotingFlow_Create);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradePromotingFlow_RetrieveN);
		}
	}
}
