package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Barcodes extends BaseModel {
	private static final long serialVersionUID = -3609731824648784108L;

	public static final int MIN_LENGTH_Barcodes = 7;
	public static final int MAX_LENGTH_Barcodes = 64;

	public static final String FIELD_ERROR_barcodes = "商品条形码只能是英文或数字的组合，长度在" + MIN_LENGTH_Barcodes + "~" + MAX_LENGTH_Barcodes + "之间";
	public static final String FIELD_ERROR_StaffID = "员工ID不能小于0";
	public static final String FIELD_ERROR_CommodityID = "商品ID不能小于0";

	public static final BarcodesField field = new BarcodesField();

	protected int commodityID;

	protected String barcode;
	
	protected int shopID;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Override
	public String toString() {
		return "Barcodes [commodityID=" + commodityID + ", barcode=" + barcode + ", operatorStaffID=" + operatorStaffID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Barcodes b = new Barcodes();
		b.setID(ID);
		b.setCommodityID(commodityID);
		b.setBarcode(barcode);
		b.setOperatorStaffID(operatorStaffID);
		b.setReturnObject(returnObject);
		b.setShopID(shopID);

		return b;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		Barcodes b = (Barcodes) arg0;
		if ((ignoreIDInComparision == true ? true : b.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& b.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
				&& b.getBarcode().equals(barcode) && printComparator(field.getFIELD_NAME_barcode()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Barcodes b = (Barcodes) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), b.getCommodityID() == 0 ? BaseAction.INVALID_ID : b.getCommodityID());
		params.put(field.getFIELD_NAME_barcode(), b.getBarcode() == null ? "" : b.getBarcode());
		params.put(field.getFIELD_NAME_iPageIndex(), b.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), b.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Barcodes b = (Barcodes) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), b.getCommodityID());
		params.put(field.getFIELD_NAME_barcode(), b.getBarcode() == null ? "" : b.getBarcode());
		params.put(field.getFIELD_NAME_operatorStaffID(), b.getOperatorStaffID());
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Barcodes b = (Barcodes) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), b.getID());
		params.put(field.getFIELD_NAME_commodityID(), b.getCommodityID());
		params.put(field.getFIELD_NAME_barcode(), b.getBarcode() == null ? "" : b.getBarcode());
		params.put(field.getFIELD_NAME_operatorStaffID(), b.getOperatorStaffID());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Map<String, Object> params = new HashMap<String, Object>();
		Barcodes b = (Barcodes) bm;

		switch (iUseCaseID) {
		case BaseBO.CASE_DeleteBarcodesByCommodityID:
			params.put(field.getFIELD_NAME_commodityID(), b.getCommodityID());
			params.put(field.getFIELD_NAME_operatorStaffID(), b.getOperatorStaffID());

			break;
		default:
			params.put(field.getFIELD_NAME_ID(), b.getID());
			params.put(field.getFIELD_NAME_operatorStaffID(), b.getOperatorStaffID());

			break;
		}
		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_commodityID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_commodityID()), sbError) && commodityID < BaseAction.INVALID_ID) {
			return sbError.toString();
		}

		if (!StringUtils.isEmpty(barcode)) {
			if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		default:
			if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) //
			{
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
				return sbError.toString();
			}

			// int2用于传staffID
			if (this.printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(operatorStaffID)) //
			{
				return sbError.toString();
			}
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_DeleteBarcodesByCommodityID:
			if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) //
			{
				return sbError.toString();
			}

			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
			{
				return sbError.toString();
			}

			// int2用于传staffID
			if (this.printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(operatorStaffID)) //
			{
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
			return sbError.toString();
		}

		// int2用于传staffID
		if (this.printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(operatorStaffID)) //
		{
			return sbError.toString();
		}

		return "";
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			barcode = jo.getString(field.getFIELD_NAME_barcode());
			String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> barcodesList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				Barcodes b = new Barcodes();
				b.doParse1(jsonObject1);
				barcodesList.add(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return barcodesList;
	}
	
	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_BarcodesCacheSize.getIndex();
	}
}
