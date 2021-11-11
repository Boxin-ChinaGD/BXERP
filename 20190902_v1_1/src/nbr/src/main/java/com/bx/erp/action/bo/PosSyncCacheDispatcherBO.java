package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.PosSyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

@Component("posSyncCacheDispatcherBO")
@Scope("prototype")
public class PosSyncCacheDispatcherBO extends BaseBO {
	@Resource
	private PosSyncCacheDispatcherMapper posSyncCacheDispatcherMapper;

	@Override
	protected void setMapper() {
		this.mapper = posSyncCacheDispatcherMapper;
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
		return true;
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}
}
