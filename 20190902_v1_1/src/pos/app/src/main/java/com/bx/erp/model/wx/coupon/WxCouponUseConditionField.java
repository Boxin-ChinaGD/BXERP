package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponUseConditionField extends BaseModelField {
	protected String FIELD_NAME_advancedInfoID;

	public String getFIELD_NAME_advancedInfoID() {
		return "advancedInfoID";
	}
	
	protected String FIELD_NAME_accept_category;
	
	public String getFIELD_NAME_accept_category() {
		return "accept_category";
	}
	
	protected String FIELD_NAME_reject_category;
	
	public String getFIELD_NAME_reject_category() {
		return "reject_category";
	}
	
	protected String FIELD_NAME_can_use_with_other_discount;
	
	public String getFIELD_NAME_can_use_with_other_discount() {
		return "can_use_with_other_discount";
	}
}
