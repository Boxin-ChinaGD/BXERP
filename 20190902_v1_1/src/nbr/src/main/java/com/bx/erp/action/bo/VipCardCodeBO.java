package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.VipCardCodeMapper;
import com.bx.erp.model.BaseModel;

@Component("vipCardCodeBO")
@Scope("prototype")
public class VipCardCodeBO extends BaseBO {
	
	protected final String SP_VipCardCode_Create = "SP_VipCardCode_Create";
	
	protected final String SP_VipCardCode_RetrieveN = "SP_VipCardCode_RetrieveN";
	
	@Resource
	protected VipCardCodeMapper vipcardCodeMapper;

	@Override
	protected void setMapper() {
		this.mapper = vipcardCodeMapper;
	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VipCardCode_Create);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VipCardCode_RetrieveN);
		}
	}
}	
