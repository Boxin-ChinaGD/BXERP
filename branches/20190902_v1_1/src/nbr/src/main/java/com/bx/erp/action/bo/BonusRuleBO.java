package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.BonusRuleMapper;
import com.bx.erp.model.BaseModel;

@Component("bonusRuleBO")
@Scope("prototype")
public class BonusRuleBO extends BaseBO {
	protected final String SP_BonusRule_Retrieve1 = "SP_BonusRule_Retrieve1";
	protected final String SP_BonusRule_Update = "SP_BonusRule_Update";

	@Resource
	private BonusRuleMapper bonusRuleMapper;

	@Override
	protected void setMapper() {
		this.mapper = bonusRuleMapper;
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_BonusRule_Retrieve1);
		}
	}

	@Override
	public boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_BonusRule_Update);
		}
	}
}
