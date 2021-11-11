package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CommodityHistoryMapper;
import com.bx.erp.model.BaseModel;

@Component("commodityHistoryBO")
@Scope("prototype")
public class CommodityHistoryBO extends BaseBO {
	protected final String SP_CommodityHistory_RetrieveN = "SP_CommodityHistory_RetrieveN";
	@Resource
	protected CommodityHistoryMapper commodityHistoryMapper;

	@Override
	protected void setMapper() {
		this.mapper = commodityHistoryMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CommodityHistory_RetrieveN);
		}
	}

}
