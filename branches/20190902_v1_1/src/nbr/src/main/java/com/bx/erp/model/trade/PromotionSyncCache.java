package com.bx.erp.model.trade;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;

public class PromotionSyncCache extends BaseSyncCache {
	private static final long serialVersionUID = 1885479090386204281L;

	@Override
	protected BaseSyncCache getSyncCache() {
		return new PromotionSyncCache();
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new PromotionSyncCacheDispatcher();
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		switch (iUseCaseID) {
		case BaseBO.CASE_X_DeleteAllPromotionSyncCache:
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
		case BaseBO.CASE_X_DeleteAllPromotionSyncCache:
			return "";
		default:
			return super.checkDelete(iUseCaseID);
		}
	}
}
