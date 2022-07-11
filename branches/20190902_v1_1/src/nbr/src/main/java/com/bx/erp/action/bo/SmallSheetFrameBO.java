package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.SmallSheetFrameMapper;
import com.bx.erp.model.BaseModel;

@Component("smallSheetFrameBO")
@Scope("prototype")
public class SmallSheetFrameBO extends BaseBO {
	protected final String SP_SmallSheetFrame_Retrieve1 = "SP_SmallSheetFrame_Retrieve1";
	protected final String SP_SmallSheetFrame_RetrieveN = "SP_SmallSheetFrame_RetrieveN";
	protected final String SP_SmallSheetFrame_Delete = "SP_SmallSheetFrame_Delete";
	protected final String SP_SmallSheetFrame_Create = "SP_SmallSheetFrame_Create";
	protected final String SP_SmallSheetFrame_Update = "SP_SmallSheetFrame_Update";

	@Resource
	private SmallSheetFrameMapper smallSheetFrameMapper;

	@Override
	protected void setMapper() {
		this.mapper = smallSheetFrameMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetFrame_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetFrame_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetFrame_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetFrame_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetFrame_RetrieveN);
		}
	}

}
