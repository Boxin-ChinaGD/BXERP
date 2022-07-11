package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

public class Department extends BaseModel {
	private static final long serialVersionUID = 2174199857401079016L;
	public static final DepartmentField field = new DepartmentField();

	protected String departmentName;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public String toString() {
		return "Department [departmentName=" + departmentName + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Department d = (Department) arg0;
		if ((ignoreIDInComparision == true ? true : (d.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& d.getDepartmentName().equals(departmentName) && printComparator(field.getFIELD_NAME_departmentName())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() {
		Department obj = new Department();
		obj.setID(ID);
		obj.setDepartmentName(departmentName);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Department d = (Department) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), d.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), d.getPageSize());

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
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
	public String checkUpdate(int iUserCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
