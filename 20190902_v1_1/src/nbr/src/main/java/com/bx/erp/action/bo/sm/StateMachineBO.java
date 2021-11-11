package com.bx.erp.action.bo.sm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.sm.StateMachineMapper;
import com.bx.erp.model.BaseModel;

@Component("stateMachineBO")
@Scope("prototype")
public class StateMachineBO extends BaseBO {
	protected final String SP_StateMachine_RetrieveN = "SP_StateMachine_RetrieveN";

	@Resource
	private StateMachineMapper stateMachineMapper;

	@Override
	public void setMapper() {
		this.mapper = stateMachineMapper;
	}

	protected boolean checkRetrieveN(int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_StateMachine_RetrieveN);
		}
	}
}
