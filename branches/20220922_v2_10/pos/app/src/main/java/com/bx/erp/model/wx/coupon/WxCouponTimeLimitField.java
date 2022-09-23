package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponTimeLimitField extends BaseModelField {
	protected String FIELD_NAME_advancedInfoID;

	public String getFIELD_NAME_advancedInfoID() {
		return "advancedInfoID";
	}

	protected String FIELD_NAME_type;

	public String getFIELD_NAME_type() {
		return "type";
	}

	protected String FIELD_NAME_begin_hour;

	public String getFIELD_NAME_begin_hour() {
		return "begin_hour";
	}

	protected String FIELD_NAME_end_hour;

	public String getFIELD_NAME_end_hour() {
		return "end_hour";
	}

	protected String FIELD_NAME_begin_minute;

	public String getFIELD_NAME_begin_minute() {
		return "begin_minute";
	}

	protected String FIELD_NAME_end_minute;

	public String getFIELD_NAME_end_minute() {
		return "end_minute";
	}

}
