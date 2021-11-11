package com.bx.erp.model.sm;

import com.bx.erp.model.BaseModelField;

public class StateMachineField extends BaseModelField {
	
	protected String FIELD_NAME_domainID;

	public String getFIELD_NAME_domainID() {
		return "domainID";
	}
	
	protected String FIELD_NAME_domainName;

	public String getFIELD_NAME_domainName() {
		return "domainName";
	}
	
	protected String FIELD_NAME_status;

	public String getFIELD_NAME_status() {
		return "status";
	}
	
	protected String FIELD_NAME_statusName;

	public String getFIELD_NAME_statusName() {
		return "statusName";
	}
	
	protected String FIELD_NAME_statusDescription;

	public String getFIELD_NAME_statusDescription() {
		return "statusDescription";
	}
	
	protected String FIELD_NAME_statusFrom;

	public String getFIELD_NAME_statusFrom() {
		return "statusFrom";
	}
	
	protected String FIELD_NAME_statusTo;

	public String getFIELD_NAME_statusTo() {
		return "statusTo";
	}
	
	protected String FIELD_NAME_forwardDescription;

	public String getFIELD_NAME_forwardDescription() {
		return "forwardDescription";
	}
}
