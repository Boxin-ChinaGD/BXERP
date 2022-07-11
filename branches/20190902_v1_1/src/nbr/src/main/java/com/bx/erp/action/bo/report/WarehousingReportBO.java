package com.bx.erp.action.bo.report;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.WarehousingReportMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("warehousingReportBO")
@Scope("prototype")
public class WarehousingReportBO extends BaseBO {
	protected final String SP_Report_Warehousing = "SP_Report_Warehousing";

	@Resource
	private WarehousingReportMapper reportMapper;

	@Override
	protected void setMapper() {
		this.mapper = reportMapper;
	}

	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParamEx(iUseCaseID, s);
		List<List<BaseModel>> ls = ((WarehousingReportMapper) mapper).retrieveNEx(params);

		setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
		return ls;
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Report_Warehousing);
		}
	}
}
