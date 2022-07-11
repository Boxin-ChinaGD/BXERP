package com.bx.erp.model.wx;

import com.bx.erp.model.BaseModel;

/** 拿Date1作为Token失效时间 */
public class WxAccessToken extends BaseModel {
	private static final long serialVersionUID = -893046275560679073L;

	public static final WxAccessTokenField field = new WxAccessTokenField();

	protected String accessToken;

	protected String componentAccessToken;

	/** 按秒计算 */
	protected int expiresin;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(Integer expiresin) {
		this.expiresin = expiresin;
	}

	public String getComponentAccessToken() {
		return componentAccessToken;
	}

	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}

	@Override
	public String toString() {
		return "WxAccessToken [accessToken=" + accessToken + ", componentAccessToken=" + componentAccessToken + ", expiresin=" + expiresin + "]";
	}

}
