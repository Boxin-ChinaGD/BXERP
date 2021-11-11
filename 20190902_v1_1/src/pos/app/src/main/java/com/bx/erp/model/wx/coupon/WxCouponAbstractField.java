package com.bx.erp.model.wx.coupon;

import com.bx.erp.model.BaseModelField;

public class WxCouponAbstractField extends BaseModelField {
	protected String FIELD_NAME_advancedInfoID;

	public String getFIELD_NAME_advancedInfoID() {
		return "advancedInfoID";
	}

	protected String FIELD_NAME_abstractEx;

	public String getFIELD_NAME_abstractEx() {
		return "abstract";
	}

	public String getFIELD_NAME_ALIAS_abstractEx() {
		return "abstractEx";
	}

	protected String FIELD_NAME_iconUrlList;

	public String getFIELD_NAME_iconUrlList() {
		return "icon_url_list";
	}

	public String getFIELD_NAME_ALIAS_iconUrlList() {
		return "iconUrlList";
	}

}
