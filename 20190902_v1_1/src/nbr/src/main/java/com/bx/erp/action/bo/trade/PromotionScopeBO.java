package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.PromotionScopeMapper;
import com.bx.erp.model.BaseModel;

@Component("promotionScopeBO")
@Scope("prototype")
public class PromotionScopeBO extends BaseBO {
	protected final String SP_PromotionScope_Create = "SP_PromotionScope_Create";
	protected final String SP_PromotionScope_RetrieveN = "SP_PromotionScope_RetrieveN";

	@Resource
	private PromotionScopeMapper promotionScopeMapperMapper;

	@Override
	protected void setMapper() {
		this.mapper = promotionScopeMapperMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PromotionScope_Create);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PromotionScope_RetrieveN);
		}
	}

}
