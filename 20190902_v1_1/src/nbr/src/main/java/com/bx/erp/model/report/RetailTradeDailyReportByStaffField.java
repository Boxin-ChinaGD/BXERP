package com.bx.erp.model.report;

import com.bx.erp.model.BaseModelField;

public class RetailTradeDailyReportByStaffField extends BaseModelField {
	protected String FIELD_NAME_dateTime;

	public String getFIELD_NAME_dateTime() {
		return "dateTime";
	}

	protected String FIELD_NAME_staffID;

	public String getFIELD_NAME_staffID() {
		return "staffID";
	}
	
	protected String FIELD_NAME_shopID;

	public String getFIELD_NAME_shopID() {
		return "shopID";
	}

	protected String FIELD_NAME_NO;

	public String getFIELD_NAME_NO() {
		return "NO";
	}

	protected String FIELD_NAME_totalAmount;

	public String getFIELD_NAME_totalAmount() {
		return "totalAmount";
	}

	protected String FIELD_NAME_grossMargin;

	public String getFIELD_NAME_grossMargin() {
		return "grossMargin";
	}

	protected String FIELD_NAME_saleDatetime;

	public String getFIELD_NAME_saleDatetime() {
		return "saleDatetime";
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
