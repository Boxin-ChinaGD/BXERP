package com.bx.erp.model;


public class BarcodesSyncCacheDispatcher extends BaseSyncCacheDispatcher {
	private static final long serialVersionUID = -6426610848932482420L;

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
		return "";
	}
}
