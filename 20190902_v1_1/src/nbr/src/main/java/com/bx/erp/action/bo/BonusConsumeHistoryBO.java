package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.BonusConsumeHistoryMapper;
import com.bx.erp.model.BaseModel;

@Component("bonusConsumeHistoryBO")
@Scope("prototype")
public class BonusConsumeHistoryBO extends BaseBO {

	@Resource
	protected BonusConsumeHistoryMapper bonusConsumeHistoryMapper;

	@Override
	public void setMapper() {
		this.mapper = bonusConsumeHistoryMapper;
	}

	@Override
	public boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true; // TODO 页面和小程序都会请求到该接口，所以暂时先返回true。将来或许需要单独判断VIP有无权限
	}

}
