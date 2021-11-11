package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.RolePermissionMapper;
import com.bx.erp.model.BaseModel;

@Component("rolePermissionBO")
@Scope("prototype")
public class RolePermissionBO extends BaseBO {
	protected final String SP_RolePermission_Delete = "SP_RolePermission_Delete";

	@Resource
	protected RolePermissionMapper rolePermissionMapper;

	@Override
	protected void setMapper() {
		this.mapper = rolePermissionMapper;
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		return false; //...暂不支持写操作。全部靠配置
//		switch (iUseCaseID) {
//		default:
//			return checkStaffPermission(staffID, SP_RolePermission_Delete);
//		}
	}

}
