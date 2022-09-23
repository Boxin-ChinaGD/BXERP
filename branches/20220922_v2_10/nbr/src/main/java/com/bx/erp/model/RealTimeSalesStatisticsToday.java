package com.bx.erp.model;

import java.util.Date;

public class RealTimeSalesStatisticsToday {

	// 下面3个变量用于统计今天门店的实时业绩
	protected double totalAmountToday;
	protected int totalNOToday;	// 今天的销售数目累加
	protected Date lastCreateDatetime; // 上一张零售单的saledatetime，默认为今天。即今天已经有过单。
	
	
	public RealTimeSalesStatisticsToday(double totalAmountToday, int totalNOToday, Date lastCreateDatetime) {
		this.totalAmountToday = totalAmountToday;
		this.totalNOToday = totalNOToday;
		this.lastCreateDatetime = lastCreateDatetime;
	}


	public double getTotalAmountToday() {
		return totalAmountToday;
	}


	public void setTotalAmountToday(double totalAmountToday) {
		this.totalAmountToday = totalAmountToday;
	}


	public int getTotalNOToday() {
		return totalNOToday;
	}


	public void setTotalNOToday(int totalNOToday) {
		this.totalNOToday = totalNOToday;
	}


	public Date getLastCreateDatetime() {
		return lastCreateDatetime;
	}


	public void setLastCreateDatetime(Date lastCreateDatetime) {
		this.lastCreateDatetime = lastCreateDatetime;
	}
	
	
	
}
