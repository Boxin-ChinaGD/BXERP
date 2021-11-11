package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.SubCommodityMapper;
import com.bx.erp.model.BaseModel;

@Component("subCommodityBO")
@Scope("prototype")
public class SubCommodityBO extends BaseBO {
	protected final String SP_Subcommodity_Create = "SP_Subcommodity_Create";
	protected final String SP_Subcommodity_RetrieveN = "SP_Subcommodity_RetrieveN";
	protected final String SP_Subcommodity_Delete = "SP_Subcommodity_Delete";
	@Resource
	private SubCommodityMapper scMapper;

	@Override
	public void setMapper() {
		this.mapper = scMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Subcommodity_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Subcommodity_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Subcommodity_RetrieveN);
		}
	}

}
