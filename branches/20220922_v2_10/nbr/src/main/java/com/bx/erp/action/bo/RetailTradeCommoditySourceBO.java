package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.RetailTradeCommoditySourceMapper;
import com.bx.erp.model.BaseModel;

@Component("retailTradeCommoditySourceBO")
@Scope("prototype")
public class RetailTradeCommoditySourceBO extends BaseBO{
	@Resource
	protected RetailTradeCommoditySourceMapper retailTradeCommoditySourceMapper;

	@Override
	public void setMapper() {
		this.mapper = retailTradeCommoditySourceMapper;
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return true;
		}
	}
}
