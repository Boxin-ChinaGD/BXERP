package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.bo.BaseBO;

public class BarcodesSyncCache extends BaseSyncCache {
	private static final long serialVersionUID = -3535517600810071171L;

	@Override
	protected BaseSyncCache getSyncCache() {
		return new BarcodesSyncCache();
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BarcodesSyncCacheDispatcher();
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
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}
	
	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.CASE_X_DeleteAllBarcodesSyncCache:
			return "";
		default:
			return "";
		}
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		switch (iUseCaseID) {
		case BaseBO.CASE_X_DeleteAllBarcodesSyncCache:
			Map<String, Object> params = new HashMap<String, Object>();
			return params;
		default:
			break;
		}
		return super.getDeleteParam(iUseCaseID, bm);
	}
}
