package com.bx.erp.model.trade;

import com.bx.erp.model.BaseSyncCacheDispatcher;

public class PromotionSyncCacheDispatcher extends BaseSyncCacheDispatcher {
	private static final long serialVersionUID = 4001352948640861672L;

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
}
