package com.bx.erp.action.bo.commodity;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CommoditySyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("commoditySyncCacheDispatcherBO")
@Scope("prototype")
public class CommoditySyncCacheDispatcherBO extends BaseBO {

	@Resource
	private CommoditySyncCacheDispatcherMapper commoditySyncCacheDispatcheMapper;

	@Override
	protected void setMapper() {
		this.mapper = commoditySyncCacheDispatcheMapper;
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
