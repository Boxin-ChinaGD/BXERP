package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponDetailField extends BaseModelField {

	protected String FIELD_NAME_couponID;

	public String getFIELD_NAME_couponID() {
		return "couponID";
	}

	protected String FIELD_NAME_card_type;

	public String getFIELD_NAME_card_type() {
		return "card_type";
	}

	protected String FIELD_NAME_typeModel;

	public String getFIELD_NAME_typeModel() {
		return "typeModel";
	}
	
	protected String FIELD_NAME_wxCouponDetailPartition;
	
	public String getFIELD_NAME_wxCouponDetailPartition() {
		return "wxCouponDetailPartition";
	}
	
	protected String FIELD_NAME_least_cost;

	public String getFIELD_NAME_least_cost() {
		return "least_cost";
	}

	protected String FIELD_NAME_reduce_cost;

	public String getFIELD_NAME_reduce_cost() {
		return "reduce_cost";
	}

	protected String FIELD_NAME_discount;

	public String getFIELD_NAME_discount() {
		return "discount";
	}
}
