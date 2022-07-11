package com.bx.erp.model.wx;

import java.util.Map;

public class BaseWXPay {
	public static final BaseWXPayField field = new BaseWXPayField();

	@Override
	public String toString() {
		return "BaseWXPay [appid=" + appid + ", mch_id=" + mch_id + ", sub_much_id=" + sub_much_id + ", nonce_str=" + nonce_str + ", sign=" + sign + "]";
	}
	/*公众账号ID*/
	protected String appid;
	/*商户号*/
	protected String mch_id;
	/*子商户号公众账号ID*/
	protected String sub_much_id;
	/*随机字符串*/
	protected String nonce_str;
	/*签名 **/
	protected String sign;
	/*   **/
	protected String secret;
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getSub_much_id() {
		return sub_much_id;
	}

	public void setSub_much_id(String sub_much_id) {
		this.sub_much_id = sub_much_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String checkCreate(int iUseCaseID) {
		throw new RuntimeException("该函数尚未实现！");
	}
	
	public Map<String, String> getHttpRetrieveNParam(int iUseCaseID, BaseWXPay bwp) {
		throw new RuntimeException("尚未实现getHttpRetrieveNParam()方法！");
	}
	
	protected void checkParameterInput(BaseWXPay bwp) {
		if (bwp == null) {
			throw new RuntimeException("传入的参数 bwp不能为null！");
		}
	}
}
