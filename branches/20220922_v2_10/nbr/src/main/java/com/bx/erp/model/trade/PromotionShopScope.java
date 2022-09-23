package com.bx.erp.model.trade;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class PromotionShopScope extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final PromotionShopScopeField field = new PromotionShopScopeField();

	public static final String FIELD_ERROR_shopID = "shopID必须大于0";
	public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";

	protected int promotionID;

	protected int shopID;

	protected String shopName;

	public int getPromotionID() {
		return promotionID;
	}

	public void setPromotionID(int promotionID) {
		this.promotionID = promotionID;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Override
	public String toString() {
		return "PromotionShopScope [promotionID=" + promotionID + ", shopID=" + shopID + "]";
	}
	
	
	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		PromotionShopScope p = (PromotionShopScope) arg0;
		if ((ignoreIDInComparision == true ? true
				: p.getID() == ID && printComparator(field.getFIELD_NAME_ID())//
						&& p.getPromotionID() == promotionID && printComparator(field.getFIELD_NAME_promotionID()))//
				&& p.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
		) {
			return 0;
		}
		return -1;
	}


	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		PromotionShopScope p = (PromotionShopScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_promotionID(), p.getPromotionID());
		params.put(field.getFIELD_NAME_shopID(), p.getShopID());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		PromotionShopScope p = (PromotionShopScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_promotionID(), p.getPromotionID());
		params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());

		return params;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		PromotionShopScope obj = new PromotionShopScope();
		obj.setID(ID);
		obj.setShopID(shopID);
		obj.setIgnoreIDInComparision(ignoreIDInComparision);
		obj.setReturnObject(returnObject);
		obj.setPageIndex(pageIndex);
		obj.setPageSize(pageSize);
		obj.setPromotionID(promotionID);
//		obj.setCommodityName(commodityName);
		return obj;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
		{
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) //
		{
			return sbError.toString();
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
		{
			return sbError.toString();
		}
		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		ID = jo.getInt(field.getFIELD_NAME_ID());
		promotionID = jo.getInt(field.getFIELD_NAME_promotionID());
		shopID = jo.getInt(field.getFIELD_NAME_shopID());
//		commodityName = jo.getString(field.getFIELD_NAME_commodityName());
//		JSONObject object = jo.getJSONObject(field.getFIELD_NAME_commodity());
//		if (!object.isNullObject()) {
//			Commodity comm = (Commodity) new Commodity().parse1(object.toString());
//			commodity = comm;
//		}
		return this;
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
