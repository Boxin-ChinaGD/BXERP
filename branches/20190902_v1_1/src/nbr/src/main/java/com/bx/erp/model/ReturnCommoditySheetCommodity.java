package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class ReturnCommoditySheetCommodity extends BaseModel {
	private static final long serialVersionUID = -5205639501879577918L;
	public static final ReturnCommoditySheetCommodityfield field = new ReturnCommoditySheetCommodityfield();

	public static final int MAX_LENGTH_Specification = 8;
	public static final int MIN_LENGTH_Specification = 0;

	public static final String FIELD_ERROR_barcodeID = "barcodeID必须大于0";
	public static final String FIELD_ERROR_returnCommoditySheetID = "returnCommoditySheetID必须大于0";
	public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";
	public static final String FIELD_ERROR_no = "数量必须是大于0，小于等于" + FieldFormat.MAX_OneCommodityNO + "的整数";
	public static final String FIELD_ERROR_purchasingPrice = "采购价必须大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_specification = "规格允许以中英数值、空格形式出现，不允许仅使用空格,长度(" + MIN_LENGTH_Specification + ", " + MAX_LENGTH_Specification + "]";

	protected Commodity commodity;

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	protected int returnCommoditySheetID;

	protected int commodityID;

	protected String commodityName;

	protected int barcodeID;

	protected int NO;

	protected String specification;

	protected double purchasingPrice;

	public double getPurchasingPrice() {
		return purchasingPrice;
	}

	public void setPurchasingPrice(double purchasingPrice) {
		this.purchasingPrice = purchasingPrice;
	}

	public int getReturnCommoditySheetID() {
		return returnCommoditySheetID;
	}

	public void setReturnCommoditySheetID(int returnCommoditySheetID) {
		this.returnCommoditySheetID = returnCommoditySheetID;
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

	public int getBarcodeID() {
		return barcodeID;
	}

	public void setBarcodeID(int barcodeID) {
		this.barcodeID = barcodeID;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int NO) {
		this.NO = NO;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	protected String packageUnit;

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;

	}

	protected String barcodes;

	public String getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;

	}

	@Override
	public String toString() {
		return "ReturnCommoditySheetCommodity [returnCommoditySheetID=" + returnCommoditySheetID + ", commodityID=" + commodityID + ", commodityName=" + commodityName + ", barcodeID=" + barcodeID + ", NO=" + NO + ", specification="
				+ specification + ", purchasingPrice=" + purchasingPrice + ", commodity=" + commodity + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ReturnCommoditySheetCommodity obj = new ReturnCommoditySheetCommodity();
		obj.setReturnCommoditySheetID(this.getReturnCommoditySheetID());
		obj.setCommodityID(this.getCommodityID());
		obj.setCommodityName(this.getCommodityName());
		obj.setBarcodeID(this.getBarcodeID());
		obj.setNO(this.getNO());
		obj.setSpecification(this.getSpecification());
		obj.setPurchasingPrice(this.getPurchasingPrice());

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) arg0;
		final double EPSILON = 1E-14; // 做浮点型数的比较
		if ((ignoreIDInComparision == true ? true : (rcsc.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))) //
				&& rcsc.getReturnCommoditySheetID() == this.getReturnCommoditySheetID() && printComparator(field.getFIELD_NAME_returnCommoditySheetID()) //
				&& rcsc.getCommodityID() == this.getCommodityID() && printComparator(field.getFIELD_NAME_commodityID()) //
				&& rcsc.getBarcodeID() == this.getBarcodeID() && printComparator(field.getFIELD_NAME_barcodeID()) //
				&& rcsc.getNO() == this.getNO() && printComparator(field.getFIELD_NAME_NO()) //
				&& rcsc.getSpecification().equals(this.getSpecification()) && printComparator(field.getFIELD_NAME_specification()) //
				&& Math.abs(rcsc.getPurchasingPrice() - this.getPurchasingPrice()) < EPSILON && printComparator(field.getFIELD_NAME_purchasingPrice()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_returnCommoditySheetID(), rcsc.getReturnCommoditySheetID());
		params.put(field.getFIELD_NAME_commodityID(), rcsc.getCommodityID());
		params.put(field.getFIELD_NAME_barcodeID(), rcsc.getBarcodeID());
		params.put(field.getFIELD_NAME_NO(), rcsc.getNO());
		params.put(field.getFIELD_NAME_specification(), rcsc.getSpecification());
		params.put(field.getFIELD_NAME_purchasingPrice(), rcsc.getPurchasingPrice());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_returnCommoditySheetID(), rcsc.getReturnCommoditySheetID());
		params.put(field.getFIELD_NAME_commodityID(), rcsc.getCommodityID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_returnCommoditySheetID(), rcsc.getReturnCommoditySheetID());
		params.put(field.getFIELD_NAME_iPageIndex(), rcsc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rcsc.getPageSize());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CheckCreateForAction:
			// Action开始要检查从表，但是这时的主表ID是没有的,所以这种情况不checkCreate returnCommoditySheetID
			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_returnCommoditySheetID(), FIELD_ERROR_returnCommoditySheetID, sbError) && !FieldFormat.checkID(returnCommoditySheetID)) {
				return sbError.toString();
			}
			break;
		}

		if (this.printCheckField(field.getFIELD_NAME_barcodeID(), FIELD_ERROR_barcodeID, sbError) && !FieldFormat.checkID(barcodeID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_purchasingPrice(), FIELD_ERROR_purchasingPrice, sbError) && !FieldFormat.checkCommodityPrice(purchasingPrice)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_no, sbError) && !FieldFormat.checkCommodityNO(NO)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_specification(), FIELD_ERROR_specification, sbError)
				&& !(FieldFormat.checkSpecification(specification) && specification.length() > MIN_LENGTH_Specification && specification.length() <= MAX_LENGTH_Specification)) {//
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_returnCommoditySheetID(), FIELD_ERROR_returnCommoditySheetID, sbError) && !FieldFormat.checkID(returnCommoditySheetID)) {
			return sbError.toString();
		}

		// if (this.printCheckField(field.getFIELD_NAME_commodityID(),
		// FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
		// return sbError.toString();
		// } //因为commodityID>0 代表删除指定一行，commodityID<=0代表删除全部，故未限制

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_returnCommoditySheetID(), FIELD_ERROR_returnCommoditySheetID, sbError) && !FieldFormat.checkID(returnCommoditySheetID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			returnCommoditySheetID = jo.getInt(field.getFIELD_NAME_returnCommoditySheetID());
			barcodeID = jo.getInt(field.getFIELD_NAME_barcodeID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());
			NO = jo.getInt(field.getFIELD_NAME_NO());
			specification = jo.getString(field.getFIELD_NAME_specification());
			purchasingPrice = jo.getDouble(field.getFIELD_NAME_purchasingPrice());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}

}
