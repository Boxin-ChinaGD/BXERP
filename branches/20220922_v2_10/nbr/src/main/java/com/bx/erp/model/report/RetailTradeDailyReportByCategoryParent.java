package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeDailyReportByCategoryParent extends BaseModel {
	private static final long serialVersionUID = -4891032616980561074L;

	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final RetailTradeDailyReportByCategoryParentField field = new RetailTradeDailyReportByCategoryParentField();

	protected Date dateTime;

	protected int categoryParentID;
	
	protected int shopID;

	protected double totalAmount;

	protected String categoryParentName;

	protected double totalAmountSummary;

	protected Date saleDatetime;

	protected Date datetimeStart;

	protected Date datetimeEnd;

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

	protected int deleteOldData;

	public int getDeleteOldData() {
		return deleteOldData;
	}

	public void setDeleteOldData(int deleteOldData) {
		this.deleteOldData = deleteOldData;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getCategoryParentID() {
		return categoryParentID;
	}

	public void setCategoryParentID(int categoryParentID) {
		this.categoryParentID = categoryParentID;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCategoryParentName() {
		return categoryParentName;
	}

	public void setCategoryParentName(String categoryParentName) {
		this.categoryParentName = categoryParentName;
	}

	public double getTotalAmountSummary() {
		return totalAmountSummary;
	}

	public void setTotalAmountSummary(double totalAmountSummary) {
		this.totalAmountSummary = totalAmountSummary;
	}
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RetailTradeDailyReportByCategoryParent [dateTime=" + dateTime + ", categoryParentID=" + categoryParentID + ", shopID=" + shopID + ", totalAmount=" + totalAmount + ", categoryParentName=" + categoryParentName
				+ ", totalAmountSummary=" + totalAmountSummary + ", saleDatetime=" + saleDatetime + ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd + ", deleteOldData=" + deleteOldData + "]";
	}

	public BaseModel clone() throws CloneNotSupportedException {
		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setID(ID);
		if (dateTime != null) {
			rdrc.setDateTime((Date) dateTime.clone());
		}
		rdrc.setCategoryParentID(categoryParentID);
		rdrc.setShopID(shopID);
		rdrc.setTotalAmount(totalAmount);

		return rdrc;
	}

	@Override
	public int compareTo(BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradeDailyReportByCategoryParent rdrc = (RetailTradeDailyReportByCategoryParent) arg0;
		if ((ignoreIDInComparision == true ? true : rdrc.getID() == ID && printComparator(field.getFIELD_NAME_ID()))//
				&& rdrc.getDateTime() == dateTime && printComparator(field.getFIELD_NAME_dateTime()) //
				&& rdrc.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& Math.abs(GeneralUtil.sub(rdrc.getCategoryParentID(), categoryParentID)) < TOLERANCE && printComparator(field.getFIELD_NAME_categoryParentID()) //
				&& Math.abs(GeneralUtil.sub(rdrc.getTotalAmount(), totalAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalAmount()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportByCategoryParent rdrc = (RetailTradeDailyReportByCategoryParent) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_shopID(), rdrc.getShopID());
		params.put(field.getFIELD_NAME_saleDatetime(), rdrc.getSaleDatetime() == null ? null : sdf.format(rdrc.getSaleDatetime()));
		params.put("deleteOldData", rdrc.getDeleteOldData()); // 为1,删除旧的数据。只能在测试代码中使用。为0=不删除旧的数据。只能在功能代码中使用。

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportByCategoryParent rdrc = (RetailTradeDailyReportByCategoryParent) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_shopID(), rdrc.getShopID());
		params.put(field.getFIELD_NAME_datetimeStart(), rdrc.getDatetimeStart() == null ? null : sdf.format(rdrc.getDatetimeStart()));
		params.put(field.getFIELD_NAME_datetimeEnd(), rdrc.getDatetimeEnd() == null ? null : sdf.format(rdrc.getDatetimeEnd()));

		return params;
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
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
