package com.bx.erp.model.wx;

import com.bx.erp.model.BaseModelField;

public class WxAccessTokenField extends BaseModelField {
	protected String FIELD_NAME_accessToken;

	public String getFIELD_NAME_accessToken() {
		return "access_token";
	}
	
	protected String FIELD_NAME_expiresin;

	public String getFIELD_NAME_expiresin() {
		return "expires_in";
	}
}
