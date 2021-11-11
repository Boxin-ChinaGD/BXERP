package com.bx.erp.action.bo.purchasing;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.ProviderCommodityMapper;
import com.bx.erp.model.BaseModel;

@Component("providerCommodityBO")
@Scope("prototype")
public class ProviderCommodityBO extends BaseBO {
	protected final String SP_ProviderCommodity_Create = "SP_ProviderCommodity_Create";
	protected final String SP_ProviderCommodity_Delete = "SP_ProviderCommodity_Delete";
	protected final String SP_ProviderCommodity_RetrieveN = "SP_ProviderCommodity_RetrieveN";

	@Resource
	private ProviderCommodityMapper providerCommodityMapper;

	@Override
	protected void setMapper() {
		this.mapper = providerCommodityMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderCommodity_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderCommodity_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ProviderCommodity_RetrieveN);
		}
	}
}
