package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

public class StaffBelonging extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	protected String openId;
	
	protected String dbName;
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "StaffBelonging [openId=" + openId + ", dbName=" + dbName + "]";
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
	
	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}
	
	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		StaffBelonging obj = new StaffBelonging();
		obj.setID(ID);
		obj.setOpenId(openId);
		obj.setDbName(dbName);
		//
		return obj;
	}
}
