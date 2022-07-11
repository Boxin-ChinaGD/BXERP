package com.bx.erp.model.report;

import com.bx.erp.model.BaseModelField;

public class ReportSummaryWarehousingField extends BaseModelField {
	protected String FIELD_NAME_totalWarehousingNumber;

	public String getFIELD_NAME_totalWarehousingNumber() {
		return "totalWarehousingNumber";
	}

	protected String FIELD_NAME_commodityName;

	public String getFIELD_NAME_name() {
		return "commodityName";
	}

	protected String FIELD_NAME_commodityMaxAmount;

	public String getFIELD_NAME_amount() {
		return "commodityMaxAmount";
	}

	protected String FIELD_NAME_providerName;

	public String getFIELD_NAME_providerName() {
		return "providerName";
	}

	protected String FIELD_NAME_providerMaxAmount;

	public String getFIELD_NAME_providerMaxAmount() {
		return "providerMaxAmount";
	}

	protected String FIELD_NAME_totalWarehousingAmount;

	public String getFIELD_NAME_totalWarehousingAmount() {
		return "totalWarehousingAmount";
	}
}
