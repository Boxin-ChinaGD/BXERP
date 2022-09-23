package com.bx.erp.model.report;

import com.bx.erp.model.BaseModelField;

public class RetailTradeDailyReportSummaryField extends BaseModelField {
	protected String FIELD_NAME_dateTime;

	public String getFIELD_NAME_dateTime() {
		return "dateTime";
	}

	protected String FIELD_NAME_totalNO;

	public String getFIELD_NAME_totalNO() {
		return "totalNO";
	}

	protected String FIELD_NAME_pricePurchase;

	public String getFIELD_NAME_pricePurchase() {
		return "pricePurchase";
	}

	protected String FIELD_NAME_totalAmount;

	public String getFIELD_NAME_totalAmount() {
		return "totalAmount";
	}

	protected String FIELD_NAME_averageAmountOfCustomer;

	public String getFIELD_NAME_averageAmountOfCustomer() {
		return "averageAmountOfCustomer";
	}

	protected String FIELD_NAME_totalGross;

	public String getFIELD_NAME_totalGross() {
		return "totalGross";
	}

	protected String FIELD_NAME_ratioGrossMargin;

	public String getFIELD_NAME_ratioGrossMargin() {
		return "ratioGrossMargin";
	}
	
	protected String FIELD_NAME_shopID;

	public String getFIELD_NAME_shopID() {
		return "shopID";
	}

	protected String FIELD_NAME_topSaleCommodityID;

	public String getFIELD_NAME_topSaleCommodityID() {
		return "topSaleCommodityID";
	}

	protected String FIELD_NAME_topSaleCommodityNO;

	public String getFIELD_NAME_topSaleCommodityNO() {
		return "topSaleCommodityNO";
	}

	protected String FIELD_NAME_topSaleCommodityAmount;

	public String getFIELD_NAME_topSaleCommodityAmount() {
		return "topSaleCommodityAmount";
	}

	protected String FIELD_NAME_topPurchaseCustomerName;

	public String getFIELD_NAME_topPurchaseCustomerName() {
		return "topPurchaseCustomerName";
	}

	protected String FIELD_NAME_topSalesAmount;// 非DB字段

	public String getFIELD_NAME_topSalesAmount() {
		return "topSalesAmount";
	}

	protected String FIELD_NAME_datetimeStart;

	public String getFIELD_NAME_datetimeStart() {
		return "datetimeStart";
	}

	protected String FIELD_NAME_datetimeEnd;

	public String getFIELD_NAME_datetimeEnd() {
		return "datetimeEnd";
	}
}
