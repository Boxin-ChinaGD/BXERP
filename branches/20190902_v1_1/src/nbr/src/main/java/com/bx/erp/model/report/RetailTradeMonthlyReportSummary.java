package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeMonthlyReportSummary extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final RetailTradeMonthlyReportSummaryField field = new RetailTradeMonthlyReportSummaryField();
	
	protected int shopID;
	
	protected Date dateTime;

	protected double totalAmount;

	protected double totalGross;

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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(double totalGross) {
		this.totalGross = totalGross;
	}
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RetailTradeMonthlyReportSummary [shopID=" + shopID + ", dateTime=" + dateTime + ", totalAmount=" + totalAmount + ", totalGross=" + totalGross + ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd
				+ ", deleteOldData=" + deleteOldData + "]";
	}

	@Override
	public BaseModel clone() {
		RetailTradeMonthlyReportSummary dm = new RetailTradeMonthlyReportSummary();
		dm.setID(ID);
		dm.setShopID(shopID);
		dm.setDateTime(dateTime);
		dm.setTotalAmount(totalAmount);
		dm.setTotalGross(totalGross);
		dm.setCreateDatetime(createDatetime);
		dm.setUpdateDatetime(updateDatetime);
		dm.setDeleteOldData(deleteOldData);

		return dm;
	}

	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradeMonthlyReportSummary dm = (RetailTradeMonthlyReportSummary) arg0;
		if ((ignoreIDInComparision == true ? true : dm.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& dm.getDateTime() == dateTime && printComparator(field.getFIELD_NAME_dateTime()) //
				&& dm.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& Math.abs(GeneralUtil.sub(dm.getTotalAmount(), totalAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalAmount()) //
				&& Math.abs(GeneralUtil.sub(dm.getTotalGross(), totalGross)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalGross()) //

		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		checkParameterInput(bm);

		RetailTradeMonthlyReportSummary dm = (RetailTradeMonthlyReportSummary) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(field.getFIELD_NAME_shopID(), dm.getShopID());
		params.put(field.getFIELD_NAME_datetimeEnd(), dm.getDatetimeEnd() == null ? null : sdf.format(dm.getDatetimeEnd()));
		params.put(field.getFIELD_NAME_deleteOldData(), dm.getDeleteOldData());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		checkParameterInput(bm);

		RetailTradeMonthlyReportSummary dm = (RetailTradeMonthlyReportSummary) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_shopID(), dm.getShopID());
		params.put(field.getFIELD_NAME_datetimeStart(), dm.getDatetimeStart() == null ? null : sdf.format(dm.getDatetimeStart()));
		params.put(field.getFIELD_NAME_datetimeEnd(), dm.getDatetimeEnd() == null ? null : sdf.format(dm.getDatetimeEnd()));
		return params;
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
		StringBuilder sbError = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		if (printCheckField(field.getFIELD_NAME_datetimeStart(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeStart))) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_datetimeEnd(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeEnd))) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}
}
