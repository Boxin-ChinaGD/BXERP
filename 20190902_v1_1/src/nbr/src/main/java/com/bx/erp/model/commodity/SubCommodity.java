package com.bx.erp.model.commodity;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class SubCommodity extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_ERROR_commodityID = "商品ID必须大于0";
	public static final String FIELD_ERROR_subCommodityID = "子商品ID必须大于0";
	public static final String FIELD_ERROR_subCommodityNO = "子商品的数量必须是正整数";
	public static final String FIELD_ERROR_price = "子商品的价格必须是大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;

	public static final SubCommodityField field = new SubCommodityField();

	private int commodityID;

	private int subCommodityID;

	private int subCommodityNO;

	private double price;

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getSubCommodityID() {
		return subCommodityID;
	}

	public void setSubCommodityID(int subCommodityID) {
		this.subCommodityID = subCommodityID;
	}

	public int getSubCommodityNO() {
		return subCommodityNO;
	}

	public void setSubCommodityNO(int subCommodityNO) {
		this.subCommodityNO = subCommodityNO;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "SubCommodity [commodityID=" + commodityID + ", subCommodityID=" + subCommodityID + ", subCommodityNO=" + subCommodityNO + ", price=" + price + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		SubCommodity sc = (SubCommodity) arg0;
		if ((ignoreIDInComparision == true ? true
				: (sc.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
						&& sc.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()))//
				&& sc.getSubCommodityNO() == subCommodityNO && printComparator(field.getFIELD_NAME_subCommodityNO())//
				&& Math.abs(GeneralUtil.sub(sc.getPrice(), price)) < TOLERANCE && printComparator(field.getFIELD_NAME_price()) //
				&& sc.getSubCommodityID() == subCommodityID && printComparator(field.getFIELD_NAME_subCommodityID())) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		SubCommodity obj = new SubCommodity();
		obj.setID(ID);
		obj.setCommodityID(commodityID);
		obj.setSubCommodityID(subCommodityID);
		obj.setSubCommodityNO(subCommodityNO);
		obj.setPrice(price);

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		SubCommodity sc = (SubCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), sc.getCommodityID());
		params.put(field.getFIELD_NAME_subCommodityID(), sc.getSubCommodityID());
		params.put(field.getFIELD_NAME_subCommodityNO(), sc.getSubCommodityNO());
		params.put(field.getFIELD_NAME_price(), sc.getPrice());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		SubCommodity sc = (SubCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), sc.getCommodityID());
		params.put(field.getFIELD_NAME_iPageIndex(), sc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), sc.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		SubCommodity sc = (SubCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), sc.getCommodityID());
		params.put(field.getFIELD_NAME_subCommodityID(), sc.getSubCommodityID());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_subCommodityID(), FIELD_ERROR_subCommodityID, sbError) && !FieldFormat.checkID(subCommodityID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_subCommodityNO(), FIELD_ERROR_subCommodityNO, sbError) && !FieldFormat.checkNO(String.valueOf(subCommodityNO))) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_price(), FIELD_ERROR_price, sbError) && !FieldFormat.checkCommodityPrice(price)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_subCommodityID(), FIELD_ERROR_subCommodityID, sbError) && !FieldFormat.checkID(subCommodityID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			String tmp = jo.getString(field.getFIELD_NAME_ID()); // 前端不会传递ID
			if (!StringUtils.isEmpty(tmp)) {
				ID = Integer.valueOf(tmp);
			}
			//
			tmp = jo.getString(field.getFIELD_NAME_commodityID()); // 前端不会传递commodityID
			if (!StringUtils.isEmpty(tmp)) {
				commodityID = Integer.valueOf(tmp);
			}
			//
			subCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_subCommodityID()));
			subCommodityNO = Integer.valueOf(jo.getString(field.getFIELD_NAME_subCommodityNO()));
			price = jo.getDouble(field.getFIELD_NAME_price());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	protected BaseModel doParse1(com.alibaba.fastjson.JSONObject jo) {
		try {
			String tmp = jo.getString(field.getFIELD_NAME_ID()); // 前端不会传递ID
			if (!StringUtils.isEmpty(tmp)) {
				ID = Integer.valueOf(tmp);
			}
			//
			tmp = jo.getString(field.getFIELD_NAME_commodityID()); // 前端不会传递commodityID
			if (!StringUtils.isEmpty(tmp)) {
				commodityID = Integer.valueOf(tmp);
			}
			//
			subCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_subCommodityID()));
			subCommodityNO = Integer.valueOf(jo.getString(field.getFIELD_NAME_subCommodityNO()));
			price = jo.getDouble(field.getFIELD_NAME_price());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

}
