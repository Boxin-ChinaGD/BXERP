package com.bx.erp.action.bo.trade;

import java.util.Map;

//import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.trade.PromotionSyncCacheMapper;
import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorCode.EnumErrorCode;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("promotionSyncCacheBO")
@Scope("prototype")
public class PromotionSyncCacheBO extends BaseBO {

	@Resource
	private PromotionSyncCacheMapper promotionSyncCacheMapper;
	
	@Override
	protected void setMapper() {
		this.mapper = promotionSyncCacheMapper;
	}

	@Override
	protected BaseModel doDelete(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_X_DeleteAllPromotionSyncCache:
			Map<String, Object> params1 = s.getDeleteParam(iUseCaseID, s);
			BaseModel bm1 = ((PromotionSyncCacheMapper)mapper).deleteAll(params1);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm1;
		default:
			return super.doDelete(iUseCaseID, s);
		}

	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}
}
