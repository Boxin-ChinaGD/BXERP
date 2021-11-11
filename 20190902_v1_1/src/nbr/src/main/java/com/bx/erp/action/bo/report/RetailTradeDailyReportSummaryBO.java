package com.bx.erp.action.bo.report;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeDailyReportSummaryMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("retailTradeDailyReportSummaryBO")
@Scope("prototype")
public class RetailTradeDailyReportSummaryBO extends BaseBO {
	// 日报表的从表使用的System最高权限，哪怕BO对应的SP字段出现问题并不会出现影响
	protected final String SP_RetailTradeDailyReportSummary_Retrieve1 = "SP_RetailTradeDailyReportSummary_Retrieve1";
	protected final String SP_RetailTradeDailyReportSummary_RetrieveNForChart = "SP_RetailTradeDailyReportSummary_RetrieveNForChart";

	@Resource
	private RetailTradeDailyReportSummaryMapper retailTradeDailyReportSummaryMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeDailyReportSummaryMapper;
	}

	@Override
	protected boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportSummary_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_RetailTradeDailyReportSummary_RetrieveNForChart:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportSummary_RetrieveNForChart);
		default:
			throw new RuntimeException("尚未实现checkRetrieveNPermission() Case!");
		}
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_RetailTradeDailyReportSummary_RetrieveNForChart:
			Map<String, Object> params = s.getRetrieveNParamEx(iUseCaseID, s);
			List<?> ls = ((RetailTradeDailyReportSummaryMapper) mapper).retrieveNForChart(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		default:
			break;
		}
		return super.doRetrieveN(iUseCaseID, s);
	}

}
