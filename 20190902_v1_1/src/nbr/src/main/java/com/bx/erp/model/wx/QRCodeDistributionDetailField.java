package com.bx.erp.model.wx;

import com.bx.erp.model.BaseModelField;

public class QRCodeDistributionDetailField extends BaseModelField{

	protected String FIELD_NAME_card_id;
	
	public String getFIELD_NAME_card_id() {
		return "card_id";
	}
	
	protected String FIELD_NAME_code;
	
	public String getFIELD_NAME_code() {
		return "code";
	}
	
	protected String FIELD_NAME_openid;
	
	public String getFIELD_NAME_openid() {
		return "openid";
	}
	
	protected String FIELD_NAME_is_unique_code;
	
	public String getFIELD_NAME_is_unique_code() {
		return "is_unique_code";
	}
	
	protected String FIELD_NAME_outer_str;
	
	public String getFIELD_NAME_outer_str() {
		return "outer_str";
	}
}
