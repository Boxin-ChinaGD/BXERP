package com.bx.erp.action.bo.trade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.PromotionSyncCacheDispatcherMapper;
import com.bx.erp.model.BaseModel;

@Component("promotionSyncCacheDispatcherBO")
@Scope("prototype")
public class PromotionSyncCacheDispatcherBO extends BaseBO {

	@Resource
	private PromotionSyncCacheDispatcherMapper promotionSyncCacheDispatcherMapper;

	@Override
	protected void setMapper() {
		this.mapper = promotionSyncCacheDispatcherMapper;
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
