package com.bx.erp.action.bo.warehousing;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.WarehousingCommodityMapper;
import com.bx.erp.model.BaseModel;

@Component("warehousingCommodityBO")
@Scope("prototype")
public class WarehousingCommodityBO extends BaseBO {
	protected final String SP_WarehousingCommodity_Create = "SP_WarehousingCommodity_Create";
	protected final String SP_WarehousingCommodity_RetrieveN = "SP_WarehousingCommodity_RetrieveN";
	protected final String SP_WarehousingCommodity_Update = "SP_WarehousingCommodity_Update";
	protected final String SP_WarehousingCommodity_Delete = "SP_WarehousingCommodity_Delete";

	@Resource
	private WarehousingCommodityMapper warehousingCommodityMapper;

	@Override
	public void setMapper() {
		this.mapper = warehousingCommodityMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_WarehousingCommodity_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_WarehousingCommodity_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_WarehousingCommodity_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_WarehousingCommodity_RetrieveN);
		}
	}

}
