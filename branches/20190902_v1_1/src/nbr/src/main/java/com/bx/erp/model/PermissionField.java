package com.bx.erp.model;

public class PermissionField extends BaseModelField {
	
	protected String FIELD_NAME_SP;
	
	public String getFIELD_NAME_SP() {
		return "SP";
	}
	
	protected String FIELD_NAME_name;

	public String getFIELD_NAME_name() {
		return "name";
	}
	
	protected String FIELD_NAME_domain;
	
	public String getFIELD_NAME_domain() {
		return "domain";
	}
	
	protected String FIELD_NAME_remark;

	public String getFIELD_NAME_remark() {
		return "remark";
	}
	
	protected String FIELD_NAME_forceDelete;

	public String getFIELD_NAME_forceDelete() {
		return "forceDelete";
	}
}
