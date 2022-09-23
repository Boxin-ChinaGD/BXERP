package com.bx.erp.model.commodity;

import com.bx.erp.model.BaseModelField;

public class CategoryField extends BaseModelField {
	protected String FIELD_NAME_name;
	protected String FIELD_NAME_parentID;

	public String getFIELD_NAME_parentID() {
		return "parentID";
	}

	public String getFIELD_NAME_name() {
		return "name";
	}

}
