package com.bx.erp.model.purchasing;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.purchasing.ProviderCommodityField;
import com.bx.erp.util.FieldFormat;

public class ProviderCommodity extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final ProviderCommodityField field = new ProviderCommodityField();

	public static final String FIELD_ERROR_CommodityID = "商品ID必须大于0";
	public static final String FIELD_ERROR_ProviderID = "供应商ID必须大于0";

	private int commodityID;

	private int providerID;

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getProviderID() {
		return providerID;
	}

	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}

	@Override
	public String toString() {
		return "ProviderCommodity [commodityID=" + commodityID + ", FIELD_NAME_commodityID=" + field.FIELD_NAME_commodityID + ", providerID=" + providerID + ", FIELD_NAME_providerID=" + field.FIELD_NAME_providerID + ", ID=" + ID +"]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ProviderCommodity pc = (ProviderCommodity) arg0;
		if ((ignoreIDInComparision == true ? true : (pc.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& pc.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID())//
				&& pc.getProviderID() == providerID && printComparator(field.getFIELD_NAME_providerID())) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ProviderCommodity pc = new ProviderCommodity();
		pc.setID(ID);
		pc.setCommodityID(commodityID);
		pc.setProviderID(providerID);

		return pc;

	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		ProviderCommodity pc = (ProviderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), pc.getCommodityID());
		params.put(field.getFIELD_NAME_providerID(), pc.getProviderID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		ProviderCommodity pc = (ProviderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), pc.getCommodityID());
		params.put(field.getFIELD_NAME_iPageIndex(), pc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), pc.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		ProviderCommodity pc = (ProviderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), pc.getCommodityID());
		params.put(field.getFIELD_NAME_providerID(), pc.getProviderID());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(ProviderCommodity.field.FIELD_NAME_commodityID, FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (this.printCheckField(ProviderCommodity.field.FIELD_NAME_providerID, FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(ProviderCommodity.field.FIELD_NAME_commodityID, FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (providerID != 0) {
			if (this.printCheckField(ProviderCommodity.field.FIELD_NAME_providerID, FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) {
				return sbError.toString();
			}
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(ProviderCommodity.field.FIELD_NAME_commodityID, FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}
		return "";
	}
}
