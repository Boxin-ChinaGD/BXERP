package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeDailyReportSummary extends BaseModel {
	private static final long serialVersionUID = 1465515454L;

	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final RetailTradeDailyReportSummaryField field = new RetailTradeDailyReportSummaryField();

	protected int shopID;
	
	protected Date dateTime;

	protected int totalNO;

	protected double pricePurchase;

	protected double totalAmount;

	protected double averageAmountOfCustomer;

	protected double totalGross;

	protected double ratioGrossMargin;

	protected int topSaleCommodityID;

	protected int topSaleCommodityNO;

	protected double topSaleCommodityAmount;

	protected String topPurchaseCustomerName;

	protected String commodityName;

	protected double topSalesAmount;

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

	public double getTopSalesAmount() {
		return topSalesAmount;
	}

	public void setTopSalesAmount(double topSalesAmount) {
		this.topSalesAmount = topSalesAmount;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getTotalNO() {
		return totalNO;
	}

	public void setTotalNO(int totalNO) {
		this.totalNO = totalNO;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getAverageAmountOfCustomer() {
		return averageAmountOfCustomer;
	}

	public void setAverageAmountOfCustomer(double averageAmountOfCustomer) {
		this.averageAmountOfCustomer = averageAmountOfCustomer;
	}

	public double getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(double totalGross) {
		this.totalGross = totalGross;
	}

	public double getRatioGrossMargin() {
		return ratioGrossMargin;
	}

	public void setRatioGrossMargin(double ratioGrossMargin) {
		this.ratioGrossMargin = ratioGrossMargin;
	}

	public int getTopSaleCommodityID() {
		return topSaleCommodityID;
	}

	public void setTopSaleCommodityID(int topSaleCommodityID) {
		this.topSaleCommodityID = topSaleCommodityID;
	}

	public int getTopSaleCommodityNO() {
		return topSaleCommodityNO;
	}

	public void setTopSaleCommodityNO(int topSaleCommodityNO) {
		this.topSaleCommodityNO = topSaleCommodityNO;
	}

	public String getTopPurchaseCustomerName() {
		return topPurchaseCustomerName;
	}

	public void setTopPurchaseCustomerName(String topPurchaseCustomerName) {
		this.topPurchaseCustomerName = topPurchaseCustomerName;
	}

	public double getPricePurchase() {
		return pricePurchase;
	}

	public void setPricePurchase(double pricePurchase) {
		this.pricePurchase = pricePurchase;
	}

	public double getTopSaleCommodityAmount() {
		return topSaleCommodityAmount;
	}

	public void setTopSaleCommodityAmount(double topSaleCommodityAmount) {
		this.topSaleCommodityAmount = topSaleCommodityAmount;
	}

	protected String topCommodityName;

	public String getTopCommodityName() {
		return topCommodityName;
	}

	public void setTopCommodityName(String topCommodityName) {
		this.topCommodityName = topCommodityName;
	}
	
	//	之前没getTopSalesAmount（）时使用的
	//	public static double DEFINE_GET_TopSalesAmount(double topSalesAmount) {
	//		return topSalesAmount;
	//	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public BaseModel clone() {
		RetailTradeDailyReportSummary dr = new RetailTradeDailyReportSummary();
		dr.setID(ID);
		if (dateTime != null) {
			dr.setDateTime((Date) dateTime.clone());
		}
		dr.setShopID(shopID);
		dr.setTotalNO(totalNO);
		dr.setPricePurchase(pricePurchase);
		dr.setTotalAmount(totalAmount);
		dr.setAverageAmountOfCustomer(averageAmountOfCustomer);
		dr.setTotalGross(totalGross);
		dr.setRatioGrossMargin(ratioGrossMargin);
		dr.setTopSaleCommodityID(topSaleCommodityID);
		dr.setTopSaleCommodityNO(topSaleCommodityNO);
		dr.setTopSaleCommodityAmount(topSaleCommodityAmount);
		dr.setQueryKeyword(this.queryKeyword);
		dr.setTopPurchaseCustomerName(topPurchaseCustomerName);
		dr.setTopSalesAmount(topSalesAmount);
		// dr.setString2(string2);
		// dr.setString3(string3);

		return dr;
	}

	@Override
	public String toString() {
		return "RetailTradeDailyReportSummary [shopID=" + shopID + ", dateTime=" + dateTime + ", totalNO=" + totalNO + ", pricePurchase=" + pricePurchase + ", totalAmount=" + totalAmount + ", averageAmountOfCustomer="
				+ averageAmountOfCustomer + ", totalGross=" + totalGross + ", ratioGrossMargin=" + ratioGrossMargin + ", topSaleCommodityID=" + topSaleCommodityID + ", topSaleCommodityNO=" + topSaleCommodityNO + ", topSaleCommodityAmount="
				+ topSaleCommodityAmount + ", topPurchaseCustomerName=" + topPurchaseCustomerName + ", commodityName=" + commodityName + ", topSalesAmount=" + topSalesAmount + ", saleDatetime=" + saleDatetime + ", datetimeStart="
				+ datetimeStart + ", datetimeEnd=" + datetimeEnd + ", topCommodityName=" + topCommodityName + "]";
	}

	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) arg0;
		if ((ignoreIDInComparision == true ? true : dr.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				// && dr.getDateTime() == dateTime && printComparator(getFIELD_NAME_dateTime())
				// //
				&& dr.getTotalNO() == totalNO && printComparator(field.getFIELD_NAME_totalNO()) //
				&& Math.abs(GeneralUtil.sub(dr.getPricePurchase(), pricePurchase)) < TOLERANCE && printComparator(field.getFIELD_NAME_pricePurchase()) //
				&& Math.abs(GeneralUtil.sub(dr.getTotalAmount(), totalAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalAmount()) //
				&& Math.abs(GeneralUtil.sub(dr.getAverageAmountOfCustomer(), averageAmountOfCustomer)) < TOLERANCE && printComparator(field.getFIELD_NAME_averageAmountOfCustomer()) //
				&& Math.abs(GeneralUtil.sub(dr.getTotalGross(), totalGross)) < TOLERANCE && printComparator(field.getFIELD_NAME_totalGross()) //
				&& Math.abs(GeneralUtil.sub(dr.getRatioGrossMargin(), ratioGrossMargin)) < TOLERANCE && printComparator(field.getFIELD_NAME_ratioGrossMargin()) //
				&& dr.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& dr.getTopSaleCommodityID() == topSaleCommodityID && printComparator(field.getFIELD_NAME_topSaleCommodityID()) //
				&& dr.getTopSaleCommodityNO() == topSaleCommodityNO && printComparator(field.getFIELD_NAME_topSaleCommodityNO()) //
				&& Math.abs(GeneralUtil.sub(dr.getTopSaleCommodityAmount(), topSaleCommodityAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_topSaleCommodityAmount()) //
				&& dr.getTopPurchaseCustomerName().equals(topPurchaseCustomerName) && printComparator(field.getFIELD_NAME_topPurchaseCustomerName()) //

		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(field.getFIELD_NAME_shopID(), dr.getShopID());
		params.put(field.getFIELD_NAME_datetimeStart(), dr.getDatetimeStart() == null ? null : sdf.format(dr.getDatetimeStart()));
		params.put(field.getFIELD_NAME_datetimeEnd(), dr.getDatetimeEnd() == null ? null : sdf.format(dr.getDatetimeEnd()));

		return params;
	}
	
	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReportSummary dr = (RetailTradeDailyReportSummary) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart:
			params.put(field.getFIELD_NAME_shopID(), dr.getShopID());
			params.put(field.getFIELD_NAME_datetimeStart(), dr.getDatetimeStart() == null ? null : sdf.format(dr.getDatetimeStart()));
			params.put(field.getFIELD_NAME_datetimeEnd(), dr.getDatetimeEnd() == null ? null : sdf.format(dr.getDatetimeEnd()));

			break;
		}

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
