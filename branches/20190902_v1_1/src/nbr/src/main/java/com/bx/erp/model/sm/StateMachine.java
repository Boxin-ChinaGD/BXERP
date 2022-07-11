package com.bx.erp.model.sm;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;

public class StateMachine extends BaseModel {

	public static final StateMachineField field = new StateMachineField();

	private static final long serialVersionUID = 1L;

	protected int domainID;

	protected String domainName;

	protected int status;

	protected String statusName;

	protected String statusDescription;

	protected int statusFrom;

	protected int statusTo;

	protected String forwardDescription;

	public int getDomainID() {
		return domainID;
	}

	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public int getStatusFrom() {
		return statusFrom;
	}

	public void setStatusFrom(int statusFrom) {
		this.statusFrom = statusFrom;
	}

	public int getStatusTo() {
		return statusTo;
	}

	public void setStatusTo(int statusTo) {
		this.statusTo = statusTo;
	}

	public String getForwardDescription() {
		return forwardDescription;
	}

	public void setForwardDescription(String forwardDescription) {
		this.forwardDescription = forwardDescription;
	}

	@Override
	public String toString() {
		return "StateMachine [domainID=" + domainID + ", domainName=" + domainName + ", status=" + status + ", statusName=" + statusName + ", statusDescription=" + statusDescription + ", statusFrom=" + statusFrom + ", statusTo=" + statusTo
				+ ", forwardDescription=" + forwardDescription + "]";
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		return new HashMap<String, Object>();
	}
}
