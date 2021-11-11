package com.bx.erp.action.bo.commodity;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CommoditySyncCacheMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("commoditySyncCacheBO")
@Scope("prototype")
public class CommoditySyncCacheBO extends BaseBO {

	@Resource
	private CommoditySyncCacheMapper commoditySyncCacheMapper;

	@Override
	protected void setMapper() {
		this.mapper = commoditySyncCacheMapper;
	}
	
	@Override
	protected BaseModel doDelete(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
//		case CASE_SmallSheetSyncCache_DeleteSameCache:
//			Map<String, Object> params = s.getDeleteParam(s.getID());
//			BaseModel bm = ((SmallSheetSyncCacheMapper) mapper).deleteSameCache(params);
//
//			setLastErrorCode(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
//
//			return bm;
		case CASE_X_DeleteAllCommoditySyncCache:
			Map<String, Object> params1 = s.getDeleteParam(iUseCaseID, s);
			BaseModel bm1 = ((CommoditySyncCacheMapper)mapper).deleteAll(params1);

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
