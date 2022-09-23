package com.bx.erp.model.commodity;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;

public class CategorySyncCache extends BaseSyncCache {
	private static final long serialVersionUID = -3276102276733565588L;

	@Override
	protected BaseSyncCache getSyncCache() {
		return new CategorySyncCache();
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new CategorySyncCacheDispatcher();
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return super.checkCreate(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		switch (iUseCaseID) {
		case BaseBO.CASE_X_DeleteAllCategorySyncCache:
			Map<String, Object> params = new HashMap<String, Object>();
			return params;
		default:
			break;
		}
		return super.getDeleteParam(iUseCaseID, bm);
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.CASE_X_DeleteAllCategorySyncCache:
			return "";
		default:
			return super.checkDelete(iUseCaseID);
		}
	}
}
