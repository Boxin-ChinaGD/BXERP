package com.bx.erp.model.commodity;

import com.bx.erp.model.BaseSyncCacheDispatcher;

public class CategorySyncCacheDispatcher extends BaseSyncCacheDispatcher{
	private static final long serialVersionUID = 5056805524577802379L;
	
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
}
