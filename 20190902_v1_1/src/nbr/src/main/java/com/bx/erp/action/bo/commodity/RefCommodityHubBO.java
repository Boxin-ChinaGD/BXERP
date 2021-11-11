package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.RefCommodityHubMapper;
import com.bx.erp.model.BaseModel;

@Component("refCommodityHubBO")
@Scope("prototype")
public class RefCommodityHubBO extends BaseBO {
	protected final String SP_RefCommodityHub_RetrieveN = "SP_RefCommodityHub_RetrieveN";

	@Resource
	protected RefCommodityHubMapper refCommodityHubMapper;

	@Override
	protected void setMapper() {
		this.mapper = refCommodityHubMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return true;  // SP_RefCommodityHub_RetrieveN的权限已经修改为所有人
		}
	}

}
