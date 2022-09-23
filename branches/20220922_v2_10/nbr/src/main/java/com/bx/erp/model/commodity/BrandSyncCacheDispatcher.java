package com.bx.erp.model.commodity;

import com.bx.erp.model.BaseSyncCacheDispatcher;

public class BrandSyncCacheDispatcher extends BaseSyncCacheDispatcher {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BrandSyncCacheDispatcher();
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return super.checkCreate(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}
}
