package com.bx.erp.model.wx.wxopen;

import com.bx.erp.model.BaseModelField;

public class WxOpenAccessTokenField extends BaseModelField {

	protected String FIELD_NAME_componentAccessToken;

	public String getFIELD_NAME_componentAccessToken() {
		return "component_access_token";
	}
	
	protected String FIELD_NAME_authorizerAccessToken;
	
	public String getFIELD_NAME_authorizerAccessToken() {
		return "authorizer_access_token";
	}

	protected String FIELD_NAME_expiresin;

	public String getFIELD_NAME_expiresin() {
		return "expires_in";
	}
}
