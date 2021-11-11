package com.bx.erp.model;

public class RSAInfo extends BaseModel {
	//public static final RSAInfoField field = new RSAInfoField();

	private static final long serialVersionUID = 1L;

	protected String modulus;

	protected String exponent;

	protected String pwd;

	public String getModulus() {
		return modulus;
	}

	public void setModulus(String modulus) {
		this.modulus = modulus;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return "RSA [modulus=" + modulus + ", exponent=" + exponent + ", pwd=" + pwd + "]";
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
