package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class ReturnRetailtradeCommoditydDestination extends BaseModel {

	private static final long serialVersionUID = -6464109720250737802L;
	public static final ReturnRetailtradeCommoditydDestinationField field = new ReturnRetailtradeCommoditydDestinationField();

	protected int retailTradeCommodityID;
	protected int increasingCommodityID;
	protected int NO;
	protected int warehousingID;

	public int getRetailTradeCommodityID() {
		return retailTradeCommodityID;
	}

	public void setRetailTradeCommodityID(int retailTradeCommodityID) {
		this.retailTradeCommodityID = retailTradeCommodityID;
	}

	public int getIncreasingCommodityID() {
		return increasingCommodityID;
	}

	public void setIncreasingCommodityID(int increasingCommodityID) {
		this.increasingCommodityID = increasingCommodityID;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public int getWarehousingID() {
		return warehousingID;
	}

	public void setWarehousingID(int warehousingID) {
		this.warehousingID = warehousingID;
	}

	@Override
	public String toString() {
		return "ReturnRetailtradeCommoditydDestination [increasingCommodityID=" + increasingCommodityID + ", retailTradeCommodityID=" + retailTradeCommodityID + ", NO=" + NO + ", warehousingID=" + warehousingID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ReturnRetailtradeCommoditydDestination obj = new ReturnRetailtradeCommoditydDestination();
		obj.setID(ID);
		obj.setRetailTradeCommodityID(retailTradeCommodityID);
		obj.setIncreasingCommodityID(increasingCommodityID);
		obj.setNO(NO);
		obj.setWarehousingID(warehousingID);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ReturnRetailtradeCommoditydDestination rrcd = (ReturnRetailtradeCommoditydDestination) arg0;
		if ((ignoreIDInComparision == true ? true : (rrcd.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rrcd.getRetailTradeCommodityID() == retailTradeCommodityID && printComparator(field.getFIELD_NAME_retailTradeCommodityID()) //
				&& rrcd.getIncreasingCommodityID() == increasingCommodityID && printComparator(field.getFIELD_NAME_increasingCommodityID()) //
				&& rrcd.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				&& rrcd.getWarehousingID() == warehousingID && printComparator(field.getFIELD_NAME_warehousingID()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			retailTradeCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_retailTradeCommodityID()));
			increasingCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_increasingCommodityID()));
			NO = Integer.valueOf(jo.getString(field.getFIELD_NAME_NO()));
			warehousingID = Integer.valueOf(jo.getString(field.getFIELD_NAME_warehousingID()));

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnRetailtradeCommoditydDestination bd = (ReturnRetailtradeCommoditydDestination) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_retailTradeCommodityID(), bd.getRetailTradeCommodityID());

		return params;
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

}
