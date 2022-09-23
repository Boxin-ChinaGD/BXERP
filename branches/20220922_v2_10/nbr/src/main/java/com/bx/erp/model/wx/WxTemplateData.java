package com.bx.erp.model.wx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;

public class WxTemplateData {
	public static final String backgroundColor = "#696969";
	public static final WxTemplateDataField field = new WxTemplateDataField();

	protected String value;// 模板显示值

	protected String color;// 模板显示颜色

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public WxTemplateData(String value, String color) {
		super();
		this.value = value;
		this.color = color;
	}

	public WxTemplateData() {
		super();
	}

	/** 返回模板1的数据 */
	public Map<String, Object> getTemplateData1(List<List<BaseModel>> bmList) {
		RetailTradeDailyReportSummary reportSummary = (RetailTradeDailyReportSummary) bmList.get(1).get(0);
		RetailTradeDailyReport report = (RetailTradeDailyReport) bmList.get(0).get(0);

		Map<String, Object> param = new HashMap<>();
		param.put("dateTime", new WxTemplateData(String.valueOf(reportSummary.getDateTime()), backgroundColor));
		param.put("totalNO", new WxTemplateData(String.valueOf(reportSummary.getTotalNO()), backgroundColor));
		param.put("pricePurchase", new WxTemplateData(String.valueOf(reportSummary.getPricePurchase()), backgroundColor));
		param.put("totalAmount", new WxTemplateData(String.valueOf(reportSummary.getTotalAmount()), backgroundColor));
		param.put("averageAmountOfCustomer", new WxTemplateData(String.valueOf(reportSummary.getAverageAmountOfCustomer()), backgroundColor));
		param.put("totalGross", new WxTemplateData(String.valueOf(reportSummary.getTotalGross()), backgroundColor));
		param.put("ratioGrossMargin", new WxTemplateData(String.valueOf(reportSummary.getRatioGrossMargin()), backgroundColor));
		param.put("topCommodityName", new WxTemplateData(String.valueOf(report == null ? "今天无销售数据" : report.getTopCommodityName()), backgroundColor)); // 最高销售商品的名字
		param.put("topSaleCommodityNO", new WxTemplateData(String.valueOf(reportSummary.getTopSaleCommodityNO()), backgroundColor));
		param.put("topPurchaseCustomerName", new WxTemplateData(String.valueOf(reportSummary.getTopPurchaseCustomerName()), backgroundColor));

		return param;
	}

}
