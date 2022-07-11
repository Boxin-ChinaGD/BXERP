package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.VipSourceMapper;
import com.bx.erp.model.BaseModel;

@Component("vipSourceBO")
@Scope("prototype")
public class VipSourceBO extends BaseBO {
	protected final String SP_VipSource_Create = "SP_VipSource_Create";
	
	@Resource
	protected VipSourceMapper vipSourceMapper;

	@Override
	protected void setMapper() {
		this.mapper = vipSourceMapper;
	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VipSource_Create);
		}
	}
}
