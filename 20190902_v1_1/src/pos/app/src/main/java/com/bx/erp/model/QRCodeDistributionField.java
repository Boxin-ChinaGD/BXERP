package com.bx.erp.model;

import com.bx.erp.model.BaseModelField;

public class QRCodeDistributionField extends BaseModelField {

	protected String FIELD_NAME_action_name;

	public String getFIELD_NAME_action_name() {
		return "action_name";
	}
	
	protected String FIELD_NAME_expire_seconds;
	
	public String getFIELD_NAME_expire_seconds() {
		return "expire_seconds";
	}
	
	protected String FIELD_NAME_qrCodeDistributionActionInfo;
	
	public String getFIELD_NAME_qrCodeDistributionActionInfo() {
		return "action_info";
	}
	
	public String getFIELD_NAME_ALIAS_qrCodeDistributionActionInfo() {
		return "qrCodeDistributionActionInfo";
	}
}
