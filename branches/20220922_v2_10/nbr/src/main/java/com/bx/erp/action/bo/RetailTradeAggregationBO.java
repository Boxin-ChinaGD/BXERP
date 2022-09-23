package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.RetailTradeAggregationMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeAggregationBO")
@Scope("prototype")
public class RetailTradeAggregationBO extends BaseBO {
	protected final String SP_RetailTradeAggregation_Create = "SP_RetailTradeAggregation_Create";
	protected final String SP_RetailTradeAggregation_Retrieve1 = "SP_RetailTradeAggregation_Retrieve1";
	
	@Resource
	private RetailTradeAggregationMapper retailTradeAggregationMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeAggregationMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeAggregation_Create);
		}
	}
	
	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeAggregation_Retrieve1);
		}
	}

}
