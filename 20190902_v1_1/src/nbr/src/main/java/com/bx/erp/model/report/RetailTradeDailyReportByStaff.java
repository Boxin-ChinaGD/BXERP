package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeDailyReportByStaff extends BaseModel {
	private static final long serialVersionUID = -5429743685870616947L;

	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final RetailTradeDailyReportByStaffField field = new RetailTradeDailyReportByStaffField();

	protected Date dateTime;

	protected int staffID;
	
	protected int shopID;

	protected int NO;

	protected double totalAmount;

	protected double grossMargin;

	protected Date saleDatetime;

	protected Date datetimeStart;

	protected Date datetimeEnd;

	protected int deleteOldData;

	public int getDeleteOldData() {
		return deleteOldData;
	}

	public void setDeleteOldData(int deleteOldData) {
		this.deleteOldData = deleteOldData;
	}

	public Date getDatetimeStart() {
		return datetimeStart;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeStart(Date datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

	public Date getSaleDatetime() {
		return saleDatetime;
	}

	public void setSaleDatetime(Date saleDatetime) {
		this.saleDatetime = saleDatetime;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getGrossMargin() {
		return grossMargin;
	}

	public void setGrossMargin(double grossMargin) {
		this.grossMargin = grossMargin;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	
	@Override
	public String toString() {
		return "RetailTradeDailyReportByStaff [dateTime=" + dateTime + ", staffID=" + staffID + ", shopID=" + shopID + ", NO=" + NO + ", totalAmount=" + totalAmount + ", grossMargin=" + grossMargin + ", saleDatetime=" + saleDatetime
				+ ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd + ", deleteOldData=" + deleteOldData + ", staffName=" + staffName + "]";
	}

	public BaseModel clone() {
		RetailTradeDailyReportByStaff ds = new RetailTradeDailyReportByStaff();
		ds.setID(ID);
		if (dateTime != null) {
			ds.setDateTime((Date) dateTime.clone());
		}
		ds.setNO(NO);
		ds.setStaffID(staffID);
		ds.setShopID(shopID);
		ds.setTotalAmount(totalAmount);
		ds.setGrossMargin(grossMargin);
		ds.setDeleteOldData(deleteOldData);

		return ds;
	}

	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradeDailyReportByStaff ds = (RetailTradeDailyReportByStaff) arg0;
		if ((ignoreIDInComparision == true ? true : ds.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				// && dr.getDateTime() == dateTime && printComparator(getFIELD_NAME_dateTime())
				// //
				&& ds.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				&& ds.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID()) //
				&& ds.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& Math.abs(GeneralUtil.sub(ds.getTotalAmount(), totalAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalAmount()) //
				&& Math.abs(GeneralUtil.sub(ds.getGrossMargin(), grossMargin)) < TOLERANCE && printComparator(field.getFIELD_NAME_grossMargin()) //

		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportByStaff ds = (RetailTradeDailyReportByStaff) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(field.getFIELD_NAME_shopID(), ds.getShopID());
		params.put(field.getFIELD_NAME_saleDatetime(), ds.getSaleDatetime() == null ? null : sdf.format(ds.getSaleDatetime()));
		params.put(field.getFIELD_NAME_deleteOldData(), ds.getDeleteOldData());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportByStaff ds = (RetailTradeDailyReportByStaff) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_shopID(), ds.getShopID());
		params.put(field.getFIELD_NAME_datetimeStart(), ds.getDatetimeStart() == null ? null : sdf.format(ds.getDatetimeStart()));
		params.put(field.getFIELD_NAME_datetimeEnd(), ds.getDatetimeEnd() == null ? null : sdf.format(ds.getDatetimeEnd()));

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		if (printCheckField(field.getFIELD_NAME_saleDatetime(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(saleDatetime))) {
			return sbError.toString();
		}
		
		if (printCheckField(field.getFIELD_NAME_shopID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(shopID)) {
			return sbError.toString();
		}

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
		StringBuilder sbError = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		if (printCheckField(field.getFIELD_NAME_datetimeStart(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeStart))) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_datetimeEnd(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeEnd))) {
			return sbError.toString();
		}
		
		if (printCheckField(field.getFIELD_NAME_shopID(), FieldFormat.FIELD_ERROR_ID_ForRetrieveN, sbError) && shopID < BaseAction.INVALID_ID) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
