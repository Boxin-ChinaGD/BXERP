package com.bx.erp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.sm.StateMachineBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.sm.StateMachine;

public class StateMachineRule {
	private static Log logger = LogFactory.getLog(StateMachineRule.class);
	
	private static StateMachineBO stateMachineBO;

	private static List<?> listStateMachine;

	public static void load(final StateMachineBO bo) {
		stateMachineBO = bo;
		stateMachineBO.setMapper();
		List<?> list = stateMachineBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, new StateMachine());
		if (stateMachineBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			throw new RuntimeException("无法加载状态机信息");
		} else {
			listStateMachine = list;
			logger.info(listStateMachine);
		}
	}

	public static boolean ValidateStateChange(int iDomain, int iStatusFrom, int iStatusTo) {
		for (Object sm : listStateMachine) {
			if (sm instanceof StateMachine) {
				if (((StateMachine) sm).getDomainID() == iDomain && ((StateMachine) sm).getStatusFrom() == iStatusFrom && ((StateMachine) sm).getStatusTo() == iStatusTo) {
					return true;
				}
			}
		}

		return false;
	}
}
