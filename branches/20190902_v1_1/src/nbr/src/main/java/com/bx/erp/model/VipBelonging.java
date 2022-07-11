//package com.bx.erp.model;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class VipBelonging extends BaseModel {
//	private static final long serialVersionUID = 1L;
//
//	protected String cardID;
//
//	protected String dbName;
//
//	public String getCardID() {
//		return cardID;
//	}
//
//	public void setCardID(String cardID) {
//		this.cardID = cardID;
//	}
//
//	public String getDbName() {
//		return dbName;
//	}
//
//	public void setDbName(String dbName) {
//		this.dbName = dbName;
//	}
//
//	@Override
//	public String toString() {
//		return "VipBelonging [cardID=" + cardID + ", dbName=" + dbName + "]";
//	}
//
//	@Override
//	public BaseModel clone() throws CloneNotSupportedException {
//		VipBelonging vipBelonging = new VipBelonging();
//		vipBelonging.setCardID(cardID);
//		vipBelonging.setDbName(dbName);
//
//		return vipBelonging;
//	}
//
//	@Override
//	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
//		checkParameterInput(bm);
//
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		return params;
//	}
//
//	@Override
//	protected String doCheckRetrieveN(int iUseCaseID) {
//		return "";
//	}
//
//	@Override
//	public boolean checkPageSize(BaseModel bm) {
//		return true;
//	}
//
//}
