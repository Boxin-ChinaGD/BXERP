package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.ReturnRetailtradeCommoditydDestinationMapper;
import com.bx.erp.model.BaseModel;

@Component("returnRetailtradeCommoditydDestinationBO")
@Scope("prototype")
public class ReturnRetailtradeCommoditydDestinationBO extends BaseBO {
	protected final String SP_ReturnRetailTradeCommodityDestination_RetrieveN = "SP_ReturnRetailTradeCommodityDestination_RetrieveN";

	@Resource
	private ReturnRetailtradeCommoditydDestinationMapper returnRetailtradeCommoditydDestinationMapper;

	@Override
	protected void setMapper() {
		this.mapper = returnRetailtradeCommoditydDestinationMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnRetailTradeCommodityDestination_RetrieveN);
		}
	}
}
