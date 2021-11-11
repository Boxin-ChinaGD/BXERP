package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CategorySyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

@Component("categorySyncCacheDispatcherBO")
@Scope("prototype")
public class CategorySyncCacheDispatcherBO extends BaseBO {
	protected final String SP_CategorySyncCacheDispatcher_UpdatePOSStatus = "SP_CategorySyncCacheDispatcher_UpdatePOSStatus";
	
	@Resource
	private CategorySyncCacheDispatcherMapper categorySyncCacheDispatcherMapper;

	@Override
	protected void setMapper() {
		this.mapper = categorySyncCacheDispatcherMapper;
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
			return checkStaffPermission(staffID, SP_CategorySyncCacheDispatcher_UpdatePOSStatus);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

}
