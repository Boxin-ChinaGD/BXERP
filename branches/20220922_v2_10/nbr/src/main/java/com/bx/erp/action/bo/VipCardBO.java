package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.VipCardMapper;
import com.bx.erp.model.BaseModel;

@Component("vipCardBO")
@Scope("prototype")
public class VipCardBO extends BaseBO {
	protected final String SP_VipCard_Retrieve1 = "SP_VipCard_Retrieve1";
	protected final String SP_VipCard_Update = "SP_VipCard_Update";
	@Resource
	protected VipCardMapper vipCardMapper;

	@Override
	protected void setMapper() {
		this.mapper = vipCardMapper;
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VipCard_Update);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VipCard_Retrieve1);
		}
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true; // 只在加载缓存时使用
	}
}
