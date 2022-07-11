package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.BrandSyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

@Component("brandSyncCacheDispatcherBO")
@Scope("prototype")
public class BrandSyncCacheDispatcherBO extends BaseBO {
	protected final String SP_BrandSyncCacheDispatcher_UpdatePOSStatus = "SP_BrandSyncCacheDispatcher_UpdatePOSStatus";
	
	@Resource
	private BrandSyncCacheDispatcherMapper brandSyncCacheDispatcherMapper;

	@Override
	protected void setMapper() {
		this.mapper = brandSyncCacheDispatcherMapper;
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
			return checkStaffPermission(staffID, SP_BrandSyncCacheDispatcher_UpdatePOSStatus);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

}
