package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

public class RetailTradeCommoditySource extends BaseModel {
	private static final long serialVersionUID = 7609801489600284854L;

	public static final RetailTradeCommoditySourceField field = new RetailTradeCommoditySourceField();

	protected int retailTradeCommodityID;
	protected int reducingCommodityID;
	protected int NO;
	protected int warehousingID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getRetailTradeCommodityID() {
		return retailTradeCommodityID;
	}

	public void setRetailTradeCommodityID(int retailTradeCommodityID) {
		this.retailTradeCommodityID = retailTradeCommodityID;
	}

	public int getReducingCommodityID() {
		return reducingCommodityID;
	}

	public void setReducingCommodityID(int reducingCommodityID) {
		this.reducingCommodityID = reducingCommodityID;
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
		return "RetailTradeCommoditySource [retailTradeCommodityID=" + retailTradeCommodityID + ", reducingCommodityID=" + reducingCommodityID + ", NO=" + NO + ", warehousingID=" + warehousingID + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RetailTradeCommoditySource obj = new RetailTradeCommoditySource();
		obj.setID(ID);
		obj.setRetailTradeCommodityID(retailTradeCommodityID);
		obj.setReducingCommodityID(reducingCommodityID);
		obj.setNO(NO);
		obj.setWarehousingID(warehousingID);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		RetailTradeCommoditySource rtcs = (RetailTradeCommoditySource) arg0;
		if ((ignoreIDInComparision == true ? true : (rtcs.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rtcs.getRetailTradeCommodityID() == retailTradeCommodityID && printComparator(field.getFIELD_NAME_retailTradeCommodityID()) //
				&& rtcs.getReducingCommodityID() == reducingCommodityID && printComparator(field.getFIELD_NAME_reducingCommodityID()) //
				&& rtcs.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				&& rtcs.getWarehousingID() == warehousingID && printComparator(field.getFIELD_NAME_warehousingID()) //
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
			reducingCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_reducingCommodityID()));
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

		RetailTradeCommoditySource bd = (RetailTradeCommoditySource) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_retailTradeCommodityID(), bd.getRetailTradeCommodityID());
		params.put(field.getFIELD_NAME_iPageIndex(), bd.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), bd.getPageSize());

		return params;
	}
	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

}
