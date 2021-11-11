package com.bx.erp.model.wx.wxopen;

import com.bx.erp.model.BaseModel;

public class WxOpenAccessToken extends BaseModel{
	private static final long serialVersionUID = -893046277854679073L;

	public static final WxOpenAccessTokenField field = new WxOpenAccessTokenField();
	
	protected String componentAccessToken;

	protected String authorizerAccessToken;
	
	/** 按秒计算 */
	protected int expiresin;

	public String getComponentAccessToken() {
		return componentAccessToken;
	}

	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}
	
	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}

	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}

	public int getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(int expiresin) {
		this.expiresin = expiresin;
	}

	@Override
	public String toString() {
		return "WxOpenAccessToken [componentAccessToken=" + componentAccessToken + ", authorizerAccessToken=" + authorizerAccessToken + ", expiresin=" + expiresin + ", date1=" + date1 + ", date2=" + date2 + "]";
	}
	
}
