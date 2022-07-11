package com.bx.erp.model.report;

import com.bx.erp.model.BaseModel;

public class ReportSummaryWarehousing extends BaseModel {
	private static final long serialVersionUID = 3508928335323446246L;

	protected int totalWarehousingNumber;

	protected String commodityName;

	protected double commodityMaxAmount;

	protected String providerName;

	protected double providerMaxAmount;

	protected double totalWarehousingAmount;

	public double getTotalWarehousingAmount() {
		return totalWarehousingAmount;
	}

	public void setTotalWarehousingAmount(double totalWarehousingAmount) {
		this.totalWarehousingAmount = totalWarehousingAmount;
	}

	public int getTotalWarehousingNumber() {
		return totalWarehousingNumber;
	}

	public void setTotalWarehousingNumber(int totalWarehousingNumber) {
		this.totalWarehousingNumber = totalWarehousingNumber;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public double getCommodityMaxAmount() {
		return commodityMaxAmount;
	}

	public void setCommodityMaxAmount(double commodityMaxAmount) {
		this.commodityMaxAmount = commodityMaxAmount;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public double getProviderMaxAmount() {
		return providerMaxAmount;
	}

	public void setProviderMaxAmount(double providerMaxAmount) {
		this.providerMaxAmount = providerMaxAmount;
	}

	@Override
	public String toString() {
		return "ReportSummaryWarehousing [totalWarehousingNumber=" + totalWarehousingNumber + ", commodityName=" + commodityName + ", commodityMaxAmount=" + commodityMaxAmount + ", providerName=" + providerName + ", providerMaxAmount="
				+ providerMaxAmount + ", totalWarehousingAmount=" + totalWarehousingAmount + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		return null;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}
}
