package com.bx.erp.action.bo.report;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeDailyReportByStaffMapper;
import com.bx.erp.model.BaseModel;


@Component("retailTradeDailyReportByStaffBO")
@Scope("prototype")
public class RetailTradeDailyReportByStaffBO extends BaseBO {
	protected final String SP_RetailTradeDailyReportByStaff_Create = "SP_RetailTradeDailyReportByStaff_Create";
	protected final String SP_RetailTradeDailyReportByStaff_RetrieveN = "SP_RetailTradeDailyReportByStaff_RetrieveN";
	
	@Resource
	private RetailTradeDailyReportByStaffMapper retailTradeDailyReportByStaffMapper;
	
	@Override
	protected void setMapper() {
		this.mapper = retailTradeDailyReportByStaffMapper;
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportByStaff_Create);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportByStaff_RetrieveN);
		}
	}
}
