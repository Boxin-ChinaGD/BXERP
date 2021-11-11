package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.util.FieldFormat;

public class RolePermission extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final RolePermissionField field = new RolePermissionField();

	protected int roleID;

	protected int permissionID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public int getPermissionID() {
		return permissionID;
	}

	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	}

	@Override
	public String toString() {
		return "Role_Permission [ID=" + ID + ", roleID=" + roleID + ", permissionID=" + permissionID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RolePermission obj = new RolePermission();
		obj.setID(ID);
		obj.setRoleID(roleID);
		obj.setPermissionID(permissionID);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		RolePermission r = (RolePermission) arg0;
		if ((ignoreIDInComparision == true ? true : (r.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& r.getRoleID() == roleID && printComparator(field.getFIELD_NAME_roleID()) //
				&& r.getPermissionID() == permissionID && printComparator(field.getFIELD_NAME_permissionID())) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		RolePermission r = (RolePermission) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_roleID(), r.getRoleID());

		return params;
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)){
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
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
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}
}
