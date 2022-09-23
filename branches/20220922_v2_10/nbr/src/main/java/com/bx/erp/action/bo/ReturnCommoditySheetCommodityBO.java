package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.ReturnCommoditySheetCommodityMapper;
import com.bx.erp.model.BaseModel;

@Component("returnCommoditySheetCommodityBO")
@Scope("prototype")
public class ReturnCommoditySheetCommodityBO extends BaseBO {
	protected final String SP_ReturnCommoditySheetCommodity_Create = "SP_ReturnCommoditySheetCommodity_Create";
	protected final String SP_ReturnCommoditySheetCommodity_Delete = "SP_ReturnCommoditySheetCommodity_Delete";
	protected final String SP_ReturnCommoditySheetCommodity_RetrieveN = "SP_ReturnCommoditySheetCommodity_RetrieveN";
	
	@Resource
	private ReturnCommoditySheetCommodityMapper returnCommoditySheetCommodityMapper;
	
	@Override
	protected void setMapper() {
		this.mapper = returnCommoditySheetCommodityMapper;

	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheetCommodity_Create);
		}
	}
	
	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheetCommodity_Delete);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ReturnCommoditySheetCommodity_RetrieveN);
		}
	}

}
