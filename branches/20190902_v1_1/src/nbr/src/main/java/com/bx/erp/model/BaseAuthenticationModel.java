package com.bx.erp.model;

public class BaseAuthenticationModel extends BaseModel {
	private static final long serialVersionUID = 7176260027700578354L;
	// public static BaseAuthenticationModelField field = new
	// BaseAuthenticationModelField();

	protected String pwdEncrypted;

	public String getPwdEncrypted() {
		return pwdEncrypted;
	}

	public void setPwdEncrypted(String pwdEncrypted) {
		this.pwdEncrypted = pwdEncrypted;
	}

	protected String salt;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	protected String passwordInPOS;

	public String getPasswordInPOS() {
		return passwordInPOS;
	}

	public void setPasswordInPOS(String passwordInPOS) {
		this.passwordInPOS = passwordInPOS;
	}

	protected String companySN;

	public String getCompanySN() {
		return companySN;
	}

	public void setCompanySN(String companySN) {
		this.companySN = companySN;
	}

	/** 用户根据什么字段和密码字段登录，则这里就返回什么字段的值 */
	public String getKey() {
		throw new RuntimeException("Not yet implemented!");
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	// @Override
	// public String checkDelete(int iUseCaseID) {
	// return "";
	// }

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	// @Override
	// public String checkRetrieve1(int iUseCaseID) {
	// return "";
	// }

	// @Override
	// public String checkRetrieveN(int iUseCaseID) {
	// return "";
	// }
}
