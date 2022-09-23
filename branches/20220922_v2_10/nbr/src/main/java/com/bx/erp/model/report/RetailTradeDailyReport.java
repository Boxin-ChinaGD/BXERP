package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;

public class RetailTradeDailyReport extends BaseModel {
	private static final long serialVersionUID = 1L;

	protected String topCommodityName;

	protected int deleteOldData;

	protected Date saleDatetime;
	
	protected int shopID;

	public Date getSaleDatetime() {
		return saleDatetime;
	}

	public void setSaleDatetime(Date saleDatetime) {
		this.saleDatetime = saleDatetime;
	}

	public int getDeleteOldData() {
		return deleteOldData;
	}

	public void setDeleteOldData(int deleteOldData) {
		this.deleteOldData = deleteOldData;
	}

	public String getTopCommodityName() {
		return topCommodityName;
	}

	public void setTopCommodityName(String topCommodityName) {
		this.topCommodityName = topCommodityName;
	}
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RetailTradeDailyReport [topCommodityName=" + topCommodityName + ", deleteOldData=" + deleteOldData + ", saleDatetime=" + saleDatetime + ", shopID=" + shopID + "]";
	}

	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		return 0;
	}

	@Override
	public Map<String, Object> getCreateParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		checkParameterInput(bm);

		RetailTradeDailyReport dr = (RetailTradeDailyReport) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("shopID", dr.getShopID());
		params.put("saleDatetime", dr.getSaleDatetime() == null ? null : sdf.format(dr.getSaleDatetime()));
		params.put("deleteOldData", dr.getDeleteOldData()); // 为1,删除旧的数据。只能在测试代码中使用。为0=不删除旧的数据。只能在功能代码中使用。

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}
}
