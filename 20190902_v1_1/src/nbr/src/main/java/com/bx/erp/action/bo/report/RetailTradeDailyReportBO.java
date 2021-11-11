package com.bx.erp.action.bo.report;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeDailyReportMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeDailyReportBO")
@Scope("prototype")
public class RetailTradeDailyReportBO extends BaseBO{
	// 日报表使用的System最高权限，哪怕BO对应的SP字段出现问题并不会出现影响
	protected final String SP_RetailTradeDailyReport_Create = "SP_RetailTradeDailyReport_Create";

	@Resource
	private RetailTradeDailyReportMapper retailTradeDailyReportMapper;

	@Override
	protected void setMapper() {
		this.mapper = retailTradeDailyReportMapper;
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReport_Create);
		}
	}
}
