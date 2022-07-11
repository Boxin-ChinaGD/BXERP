package com.bx.erp.action.bo.commodity;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CategorySyncCacheMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
@Component("categorySyncCacheBO")
@Scope("prototype")
public class CategorySyncCacheBO extends BaseBO{
	protected final String SP_CategorySyncCache_RetrieveN = "SP_CategorySyncCache_RetrieveN";
	
	@Resource
	private CategorySyncCacheMapper categorySyncCacheMapper;
	
	@Override
	protected void setMapper() {
		this.mapper = categorySyncCacheMapper;
	}

	@Override
	protected BaseModel doDelete(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_X_DeleteAllCategorySyncCache:
			Map<String, Object> params1 = s.getDeleteParam(iUseCaseID, s);
			BaseModel bm1 = ((CategorySyncCacheMapper)mapper).deleteAll(params1);

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
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_CategorySyncCache_RetrieveN);
		}
	}
}
