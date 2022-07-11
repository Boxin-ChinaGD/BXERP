package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CommodityPropertyMapper;
import com.bx.erp.model.BaseModel;

@Component("CommodityPropertyBO")
@Scope("prototype")
public class CommodityPropertyBO extends BaseBO {
	protected final String SP_CommodityProperty_Retrieve1 = "SP_CommodityProperty_Retrieve1";
	protected final String SP_CommodityProperty_Update = "SP_CommodityProperty_Update";

	@Resource
	protected CommodityPropertyMapper commodityPropertyMapper;

	@Override
	public void setMapper() {
		this.mapper = commodityPropertyMapper;
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CommodityProperty_Retrieve1);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CommodityProperty_Update);
		}
	}
}
