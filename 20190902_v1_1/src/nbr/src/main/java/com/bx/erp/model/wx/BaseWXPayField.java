package com.bx.erp.model.wx;

import com.bx.erp.model.BaseModelField;

public class BaseWXPayField extends BaseModelField {
	protected String FIELD_NAME_appid;

	public String getFIELD_NAME_appid() {
		return "appid";
	}

	protected String FIELD_NAME_mch_id;

	public String getFIELD_NAME_mch_id() {
		return "mch_id";
	}

	protected String FIELD_NAME_sub_much_id;

	public String getFIELD_NAME_sub_much_id() {
		return "sub_much_id";
	}

	protected String FIELD_NAME_nonce_str;

	public String getFIELD_NAME_nonce_str() {
		return "nonce_str";
	}

	protected String FIELD_NAME_sign;

	public String getFIELD_NAME_sign() {
		return "sign";
	}
	
	protected String FIELD_NAME_secret;

	public String getFIELD_NAME_secret() {
		return "secret";
	}
}
