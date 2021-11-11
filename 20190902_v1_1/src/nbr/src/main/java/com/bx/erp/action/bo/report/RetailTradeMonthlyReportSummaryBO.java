package com.bx.erp.action.bo.report;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeMonthlyReportSummaryMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeMonthlyReportSummaryBO")
@Scope("prototype")
public class RetailTradeMonthlyReportSummaryBO extends BaseBO {
	protected final String SP_RetailTradeMonthlyReportSummary_Create = "SP_RetailTradeMonthlyReportSummary_Create";
	protected final String SP_RetailTradeMonthlyReportSummary_RetrieveN = "SP_RetailTradeMonthlyReportSummary_RetrieveN";

	@Resource
	private RetailTradeMonthlyReportSummaryMapper retailTradeMonthlyReportSummaryMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeMonthlyReportSummaryMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeMonthlyReportSummary_Create);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeMonthlyReportSummary_RetrieveN);
		}
	}

}
