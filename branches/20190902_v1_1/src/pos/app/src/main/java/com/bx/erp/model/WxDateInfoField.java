package com.bx.erp.model;

import com.bx.erp.model.BaseModelField;

public class WxDateInfoField extends BaseModelField {
	protected String FIELD_NAME_type;

	public String getFIELD_NAME_type() {
		return "type";
	}

	protected String FIELD_NAME_begin_timestamp;
	public String getFIELD_NAME_begin_timestamp() {
		return "begin_timestamp";
	}

	protected String FIELD_NAME_end_timestamp;
	public String getFIELD_NAME_end_timestamp() {
		return "end_timestamp";
	}

	protected String FIELD_NAME_fixed_term;
	public String getFIELD_NAME_fixed_term() {
		return "fixed_term";
	}

	protected String FIELD_NAME_fixed_begin_term;
	public String getFIELD_NAME_fixed_begin_term() {
		return "fixed_begin_term";
	}
}
