package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CouponScope extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final CouponScopeField field = new CouponScopeField();

	protected int couponID;

	protected int commodityID;

	protected String commodityName;

	protected String barcodes;

	protected double priceRetail;

	public String getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;
	}

	public double getPriceRetail() {
		return priceRetail;
	}

	public void setPriceRetail(double priceRetail) {
		this.priceRetail = priceRetail;
	}

	public int getCouponID() {
		return couponID;
	}

	public void setCouponID(int couponID) {
		this.couponID = couponID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@Override
	public String toString() {
		return "CouponScope [couponID=" + couponID + ", commodityID=" + commodityID + ", commodityName=" + commodityName + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		CouponScope couponScope = new CouponScope();
		couponScope.setID(ID);
		couponScope.setCouponID(couponID);
		couponScope.setCommodityID(commodityID);
		couponScope.setCommodityName(commodityName);

		return couponScope;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		CouponScope couponScope = (CouponScope) arg0;
		if ((ignoreIDInComparision == true ? true : couponScope.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& couponScope.getCouponID() == couponID && printComparator(field.getFIELD_NAME_couponID()) //
				&& couponScope.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			couponID = jo.getInt(field.getFIELD_NAME_couponID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());
			barcodes = jo.getString(field.getFIELD_NAME_barcodes());
			priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> couponScopeList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				CouponScope couponScope = new CouponScope();
				couponScope.doParse1(jsonObject1);
				couponScopeList.add(couponScope);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return couponScopeList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CouponScope couponScope = (CouponScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_couponID(), couponScope.getCouponID());
		params.put(field.getFIELD_NAME_commodityID(), couponScope.getCommodityID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CouponScope couponScope = (CouponScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_couponID(), couponScope.getCouponID());
		params.put(field.getFIELD_NAME_iPageIndex(), couponScope.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), couponScope.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
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

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	public enum EnumCouponScope {
		ECS_AllCommodities("All Commodities", 0), ECS_SpecifiedCommodities("Specified Commodities", 1);

		private String name;
		private int index;

		private EnumCouponScope(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBound(int index) {
			if (index < ECS_AllCommodities.getIndex() || index > ECS_SpecifiedCommodities.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumCouponScope c : EnumCouponScope.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true; // 永远都是从表，所以不需要限制
	}

}
