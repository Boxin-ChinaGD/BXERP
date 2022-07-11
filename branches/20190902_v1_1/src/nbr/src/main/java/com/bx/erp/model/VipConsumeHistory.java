package com.bx.erp.model;

import java.util.Date;

public class VipConsumeHistory extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final VipConsumeHistoryField field = new VipConsumeHistoryField();

	protected int tradeID;

	protected Date datetime;

	public int getTradeID() {
		return tradeID;
	}

	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "Vip_ConsumeHistory [tradeID=" + tradeID + ", datetime=" + datetime + "]";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}
}
