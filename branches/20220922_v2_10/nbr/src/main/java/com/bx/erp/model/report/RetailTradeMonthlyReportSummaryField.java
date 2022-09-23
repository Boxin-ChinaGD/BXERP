package com.bx.erp.model.report;

import com.bx.erp.model.BaseModelField;

public class RetailTradeMonthlyReportSummaryField extends BaseModelField {
	protected String FIELD_NAME_dateTime;

	public String getFIELD_NAME_dateTime() {
		return "dateTime";
	}
	
	protected String FIELD_NAME_shopID;

	public String getFIELD_NAME_shopID() {
		return "shopID";
	}

	protected String FIELD_NAME_totalAmount;

	public String getFIELD_NAME_totalAmount() {
		return "totalAmount";
	}

	protected String FIELD_NAME_totalGross;

	public String getFIELD_NAME_totalGross() {
		return "totalGross";
	}

	protected String FIELD_NAME_datetimeStart;

	public String getFIELD_NAME_datetimeStart() {
		return "datetimeStart";
	}
	
	protected String FIELD_NAME_datetimeEnd;

	public String getFIELD_NAME_datetimeEnd() {
		return "datetimeEnd";
	}
	
	protected String FIELD_NAME_deleteOldData;

	public String getFIELD_NAME_deleteOldData() {
		return "deleteOldData";
	}
}
