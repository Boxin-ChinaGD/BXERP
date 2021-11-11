package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.BarcodesSyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

@Component("barcodesSyncCacherDispatcherBO")
@Scope("prototype")
public class BarcodesSyncCacheDispatcherBO extends BaseBO {
	protected final String SP_BarcodesSyncCacheDispatcher_UpdatePOSStatus = "SP_BarcodesSyncCacheDispatcher_UpdatePOSStatus";
	
	@Resource
	private BarcodesSyncCacheDispatcherMapper barcodesSyncCacheDispatcherMapper;

	@Override
	protected void setMapper() {
		this.mapper = barcodesSyncCacheDispatcherMapper;
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_BarcodesSyncCacheDispatcher_UpdatePOSStatus);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

}
