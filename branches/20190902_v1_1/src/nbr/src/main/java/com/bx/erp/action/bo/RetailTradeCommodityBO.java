package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.RetailTradeCommodityMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeCommodityBO")
@Scope("prototype")
public class RetailTradeCommodityBO extends BaseBO {
	protected final String SP_RetailTradeCommodity_UploadTrade_CreateCommodity = "SP_RetailTradeCommodity_UploadTrade_CreateCommodity";
	protected final String SP_RetailTradeCommodity_RetrieveNTradeCommodity = "SP_RetailTradeCommodity_RetrieveNTradeCommodity";
	protected final String SP_RetailTradeCommodity_delete = "SP_RetailTradeCommodity_delete";
	@Resource
	private RetailTradeCommodityMapper retailTradeCommodityMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeCommodityMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeCommodity_UploadTrade_CreateCommodity);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeCommodity_delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeCommodity_RetrieveNTradeCommodity);
		}
	}

}
