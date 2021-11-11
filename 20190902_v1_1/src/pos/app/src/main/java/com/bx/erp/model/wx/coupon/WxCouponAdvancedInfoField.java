package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponAdvancedInfoField extends BaseModelField {

	protected String FIELD_NAME_couponDetailID;
	
	public String getFIELD_NAME_couponDetailID() {
		return "couponDetailID";
	}
	
	protected String FIELD_NAME_use_condition;
	
	public String getFIELD_NAME_use_condition() {
		return "use_condition";
	}

	public String getFIELD_NAME_ALIAS_use_condition() {
		return "use_condition";
	}
	
	protected String FIELD_NAME_abstractEx;

	public String getFIELD_NAME_abstractEx() {
		return "abstract";
	}
	
	public String getFIELD_NAME_ALIAS_abstractEx() {
		return "abstractEx";
	}
	
	protected String FIELD_NAME_text_image_list;

	public String getFIELD_NAME_text_image_list() {
		return "text_image_list";
	}
	
	protected String FIELD_NAME_time_limit;

	public String getFIELD_NAME_time_limit() {
		return "time_limit";
	}
	
	protected String FIELD_NAME_business_service;

	public String getFIELD_NAME_business_service() {
		return "business_service";
	}
	
	protected String FIELD_NAME_least_cost;

	public String getFIELD_NAME_least_cost() {
		return "least_cost";
	}
	
	protected String FIELD_NAME_object_use_for;

	public String getFIELD_NAME_object_use_for() {
		return "object_use_for";
	}
	
}
