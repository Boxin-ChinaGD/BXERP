package com.bx.erp.action.bo.report;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeDailyReportByCategoryParentMapper;
import com.bx.erp.model.BaseModel;

@Component("etailTradeDailyReportByCategoryParentBO")
@Scope("prototype")
public class RetailTradeDailyReportByCategoryParentBO extends BaseBO {
	protected final String SP_RetailTradeDailyReportByCategoryParent_Create = "SP_RetailTradeDailyReportByCategoryParent_Create";
	protected final String SP_RetailTradeDailyReportByCategoryParent_RetrieveN = "SP_RetailTradeDailyReportByCategoryParent_RetrieveN";

	@Resource
	private RetailTradeDailyReportByCategoryParentMapper retailTradeDailyReportByCategoryParentMapper;

	@Override
	protected void setMapper() {
		// TODO 自动生成的方法存根
		this.mapper = retailTradeDailyReportByCategoryParentMapper;
	}

	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportByCategoryParent_RetrieveN);
		}
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeDailyReportByCategoryParent_Create);
		}
	}

}
