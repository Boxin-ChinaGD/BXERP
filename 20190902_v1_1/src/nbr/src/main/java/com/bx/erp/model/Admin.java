package com.bx.erp.model;

public class Admin extends BaseModel {
	private static final long serialVersionUID = -8308312290885939601L;

	protected String sMobile;

	protected String sPassword;

	public String getMobile() {
		return sMobile;
	}

	public void setMobile(String sMobile) {
		this.sMobile = sMobile;
	}

	public String getPassword() {
		return sPassword;
	}

	public void setPassword(String sPassword) {
		this.sPassword = sPassword;
	}

	@Override
	public String toString() {
		return "Admin [sMobile=" + sMobile + ", sPassword=" + sPassword + "]";
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
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

}
