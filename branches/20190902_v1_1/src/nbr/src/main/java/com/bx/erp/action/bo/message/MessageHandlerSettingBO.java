package com.bx.erp.action.bo.message;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.message.MessageHandlerSettingMapper;
import com.bx.erp.model.BaseModel;

@Component("messageHandlerSettingBO")
@Scope("prototype")
public class MessageHandlerSettingBO extends BaseBO {
	protected final String SP_MessageHandlerSetting_Create = "SP_MessageHandlerSetting_Create";
	protected final String SP_MessageHandlerSetting_Update = "SP_MessageHandlerSetting_Update";
	protected final String SP_MessageHandlerSetting_Retrieve1 = "SP_MessageHandlerSetting_Retrieve1";
	protected final String SP_MessageHandlerSetting_RetrieveN = "SP_MessageHandlerSetting_RetrieveN";
	protected final String SP_MessageHandlerSetting_Delete = "SP_MessageHandlerSetting_Delete";

	@Resource
	private MessageHandlerSettingMapper messageHandlerSettingMapper;

	@Override
	protected void setMapper() {
		this.mapper = messageHandlerSettingMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageHandlerSetting_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageHandlerSetting_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageHandlerSetting_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageHandlerSetting_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageHandlerSetting_RetrieveN);
		}
	}

}
