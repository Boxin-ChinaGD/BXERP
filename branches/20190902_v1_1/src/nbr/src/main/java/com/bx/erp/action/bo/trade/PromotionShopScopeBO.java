package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.PromotionShopScopeMapper;
import com.bx.erp.model.BaseModel;

@Component("promotionShopScopeBO")
@Scope("prototype")
public class PromotionShopScopeBO extends BaseBO {
	protected final String SP_PromotionShopScope_Create = "SP_PromotionShopScope_Create";
	protected final String SP_PromotionShopScope_RetrieveN = "SP_PromotionShopScope_RetrieveN";

	@Resource
	private PromotionShopScopeMapper promotionShopScopeMapper;

	@Override
	protected void setMapper() {
		this.mapper = promotionShopScopeMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PromotionShopScope_Create);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PromotionShopScope_RetrieveN);
		}
	}
}
