package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponDetailPartitionField extends BaseModelField{
	protected String FIELD_NAME_base_info;

	public String getFIELD_NAME_base_info() {
		return "base_info";
	}

	protected String FIELD_NAME_advanced_info;

	public String getFIELD_NAME_advanced_info() {
		return "advanced_info";
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
