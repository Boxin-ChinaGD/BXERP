package com.bx.erp.model;

public class PosSyncCacheDispatcher extends BaseSyncCacheDispatcher{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new PosSyncCacheDispatcher();
	}
	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}
}
