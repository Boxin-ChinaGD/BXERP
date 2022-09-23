package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

public class StaffPermission extends BaseModel {
	private static final long serialVersionUID = -5698326578034731747L;
	//public static final StaffPermissionField field = new StaffPermissionField();

	protected int staffID;

	protected String staffName;

	protected int roleID;

	protected String roleName;

	protected String sp;

	protected String permissionName;

	protected String remark;

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	@Override
	public String toString() {
		return "StaffPermission [staffID=" + staffID + ", staffName=" + staffName + ", roleID=" + roleID + ", roleName=" + roleName + ", sp=" + sp + ", permissionName=" + permissionName + ", remark=" + remark + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		StaffPermission obj = new StaffPermission();
		obj.setID(ID);
		obj.setStaffID(staffID);
		obj.setStaffName(staffName);
		obj.setRoleID(roleID);
		obj.setRoleName(roleName);
		obj.setSp(sp);
		obj.setPermissionName(permissionName);
		obj.setRemark(remark);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		StaffPermission staffPermission = (StaffPermission) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), staffPermission.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), staffPermission.getPageSize());

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
