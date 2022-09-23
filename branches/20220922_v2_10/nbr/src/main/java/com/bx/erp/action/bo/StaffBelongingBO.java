package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.StaffBelongingMapper;
import com.bx.erp.model.BaseModel;

@Component("staffBelongingBO")
public class StaffBelongingBO extends BaseBO {
	@Resource
	private StaffBelongingMapper staffBelongingMapper;
	
	protected final String SP_StaffBelonging_RetrieveN = "SP_StaffBelonging_RetrieveN";
	
	@Override
	protected void setMapper() {
		this.mapper = staffBelongingMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_StaffBelonging_RetrieveN);
		}
	}
}
