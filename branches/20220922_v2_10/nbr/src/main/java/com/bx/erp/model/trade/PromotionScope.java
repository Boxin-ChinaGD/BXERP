package com.bx.erp.model.trade;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class PromotionScope extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final PromotionScopeField field = new PromotionScopeField();

	public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";
	public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";

	protected int promotionID;

	protected int commodityID;

	protected String commodityName;

	public int getPromotionID() {
		return promotionID;
	}

	public void setPromotionID(int promotionID) {
		this.promotionID = promotionID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	protected Commodity commodity;

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@Override
	public String toString() {
		return "PromotionScope [promotionID=" + promotionID + ", commodityID=" + commodityID + ", commodityName=" + commodityName + ", ID=" + ID + "]";
	}

	public String toPromotionInfoString() {
		return " 促销ID = " + promotionID + ", 商品ID = " + commodityID + ", 商品名称  = " + commodityName;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		PromotionScope p = (PromotionScope) arg0;
		if ((ignoreIDInComparision == true ? true
				: p.getID() == ID && printComparator(field.getFIELD_NAME_ID())//
						&& p.getPromotionID() == promotionID && printComparator(field.getFIELD_NAME_promotionID()))//
				&& p.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
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

		PromotionScope p = (PromotionScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_promotionID(), p.getPromotionID());
		params.put(field.getFIELD_NAME_commodityID(), p.getCommodityID());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		PromotionScope p = (PromotionScope) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_promotionID(), p.getPromotionID());
		params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());

		return params;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		PromotionScope obj = new PromotionScope();
		obj.setID(ID);
		obj.setCommodityID(commodityID);
		obj.setIgnoreIDInComparision(ignoreIDInComparision);
		obj.setReturnObject(returnObject);
		obj.setPageIndex(pageIndex);
		obj.setPageSize(pageSize);
		obj.setPromotionID(promotionID);
		obj.setCommodityName(commodityName);
		return obj;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
		{
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) //
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
		commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
		commodityName = jo.getString(field.getFIELD_NAME_commodityName());
		JSONObject object = jo.getJSONObject(field.getFIELD_NAME_commodity());
		if (!object.isNullObject()) {
			Commodity comm = (Commodity) new Commodity().parse1(object.toString());
			commodity = comm;
		}
		return this;
	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
