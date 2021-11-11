package com.bx.erp.dao.report;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeDailyReportSummaryMapper")
public interface RetailTradeDailyReportSummaryMapper extends BaseMapper{
	public List<BaseModel> retrieveNForChart(Map<String, Object> params);
}
