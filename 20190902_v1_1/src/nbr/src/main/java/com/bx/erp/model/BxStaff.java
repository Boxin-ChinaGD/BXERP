package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.util.FieldFormat;

public class BxStaff extends BaseAuthenticationModel {
	private static final long serialVersionUID = 7632576760139097935L;

	public static final BxStaffField field = new BxStaffField();

	public static final int Min_ID = 0;
	public static final String FIELD_ERROR_Mobile = "手机格式错误，应为" + FieldFormat.LENGTH_Mobile + "位的数字";

	protected String mobile;

	protected String pwdEncrypted;
	protected String salt;
	protected int roleID;
	protected int departmentID;

	protected String name;

	protected int sex;
	protected String ICID;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPwdEncrypted() {
		return pwdEncrypted;
	}

	public void setPwdEncrypted(String pwdEncrypted) {
		this.pwdEncrypted = pwdEncrypted;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getICID() {
		return ICID;
	}

	public void setICID(String iCID) {
		ICID = iCID;
	}

	@Override
	public String toString() {
		return "BxStaff [mobile=" + mobile + ", pwdEncrypted=" + pwdEncrypted + ", salt=" + salt + ", roleID=" + roleID + ", departmentID=" + departmentID + ", name=" + name + ", sex=" + sex + ", ICID=" + ICID + ", ID=" + ID + "]";
	}

	@Override
	public String getKey() {
		return mobile;
	}

	@Override
	public BaseModel clone() {
		BxStaff obj = new BxStaff();
		obj.setID(ID);
		obj.setName(name);
		obj.setICID(ICID);
		obj.setPwdEncrypted(pwdEncrypted);
		obj.setSex(sex);
		obj.setDepartmentID(departmentID);
		obj.setRoleID(roleID);
		obj.setSalt(salt);
		obj.setMobile(mobile);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		BxStaff bs = (BxStaff) arg0;
		if ((ignoreIDInComparision == true ? true : (bs.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& bs.getName().equals(name) && printComparator(field.getFIELD_NAME_name())//
				&& bs.getDepartmentID() == departmentID && printComparator(field.getFIELD_NAME_departmentID())//
				&& bs.getRoleID() == roleID && printComparator(field.getFIELD_NAME_roleID())//
				&& bs.getSex() == sex && printComparator(field.getFIELD_NAME_sex())//
				&& bs.getSalt().equals(salt) && printComparator(field.getFIELD_NAME_salt())//
				&& bs.getMobile().equals(mobile) && printComparator(field.getFIELD_NAME_mobile())//
				&& bs.getICID().equals(ICID) && printComparator(field.getFIELD_NAME_ICID())//
				&& bs.getPwdEncrypted().equals(pwdEncrypted) && printComparator(field.getFIELD_NAME_pwdEncrypted())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BxStaff bs = (BxStaff) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), bs.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), bs.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BxStaff bxStaff = (BxStaff) bm;

		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_ID(), bxStaff.getID());
			params.put(field.getFIELD_NAME_mobile(), bxStaff.getMobile() == null ? "" : bxStaff.getMobile());
			break;
		}
		return params;
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (ID != Min_ID) {
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
			{
				return sbError.toString();
			}
		}

		if (!StringUtils.isEmpty(mobile)) {
			if (this.printCheckField(Warehouse.field.getFIELD_NAME_phone(), FIELD_ERROR_Mobile, sbError) && !FieldFormat.checkMobile(mobile)) //
			{
				return sbError.toString();
			}
		}

		return "";
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
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
