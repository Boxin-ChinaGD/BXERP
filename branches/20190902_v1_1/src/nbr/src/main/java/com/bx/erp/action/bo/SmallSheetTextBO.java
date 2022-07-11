package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.SmallSheetTextMapper;
import com.bx.erp.model.BaseModel;

@Component("smallSheetTextBO")
@Scope("prototype")
public class SmallSheetTextBO extends BaseBO {
	protected final String SP_SmallSheetText_Create = "SP_SmallSheetText_Create";
	protected final String SP_SmallSheetText_Delete = "SP_SmallSheetText_Delete";
	protected final String SP_SmallSheetText_RetrieveN = "SP_SmallSheetText_RetrieveN";
	protected final String SP_SmallSheetText_Update = "SP_SmallSheetText_Update";

	@Resource
	private SmallSheetTextMapper smallSheetTextMapper;

	@Override
	protected void setMapper() {
		this.mapper = smallSheetTextMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetText_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetText_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetText_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_SmallSheetText_RetrieveN);
		}
	}
}
