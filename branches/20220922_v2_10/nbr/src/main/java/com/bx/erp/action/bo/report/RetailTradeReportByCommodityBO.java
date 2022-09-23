package com.bx.erp.action.bo.report;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.report.RetailTradeReportByCommodityMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("/retailTradeReportByCommodityBO")
@Scope("prototype")
public class RetailTradeReportByCommodityBO extends BaseBO {
	protected final String SP_RetailTradeReportByCommodity_RetrieveN = "SP_RetailTradeReportByCommodity_RetrieveN";

	@Resource
	private RetailTradeReportByCommodityMapper reportMapper;

	@Override
	protected void setMapper() {
		this.mapper = reportMapper;
	}

	protected List<BaseModel> doRetrieveN(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		List<BaseModel> ls = ((RetailTradeReportByCommodityMapper) mapper).retrieveN(params);

		setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
		return ls;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTradeReportByCommodity_RetrieveN);
		}
	}

}
