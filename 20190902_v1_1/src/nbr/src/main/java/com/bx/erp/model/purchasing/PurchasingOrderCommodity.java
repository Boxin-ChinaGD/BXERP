package com.bx.erp.model.purchasing;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class PurchasingOrderCommodity extends BaseModel {
	private static final long serialVersionUID = 7238267984478777339L;
	public static final PurchasingOrderCommodityField field = new PurchasingOrderCommodityField();

	public static final String FIELD_ERROR_PurchasingOrderID = "采购订单ID必须>0";
	public static final String FIELD_ERROR_CommodityID = "商品ID必须>0";
	public static final String FIELD_ERROR_BarcodeID = "条形码ID必须>0";
	public static final String FIELD_ERROR_CommodityNO = "数量必须是大于0，小于等于" + FieldFormat.MAX_OneCommodityNO + "的整数";
	public static final String FIELD_ERROR_PriceSuggestion = "建议采购单价必须大于或者等于0,小于等于" + FieldFormat.MAX_OneCommodityPrice;

	protected int purchasingOrderID;

	protected double priceSuggestion;

	protected int commodityID;

	protected int commodityNO;

	protected String commodityName;

	protected int barcodeID;

	protected int packageUnitID;

	protected String barcode;

	protected String packageUnitName;

	protected String noRemaining;

	protected int purchasingOrderCommodityIsInWarehouse; // 非数据库字段。1表明为已入库。0为未入库

	public int getPurchasingOrderID() {
		return purchasingOrderID;
	}

	public void setPurchasingOrderID(int purchasingOrderID) {
		this.purchasingOrderID = purchasingOrderID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getCommodityNO() {
		return commodityNO;
	}

	public void setCommodityNO(int commodityNO) {
		this.commodityNO = commodityNO;
	}

	public double getPriceSuggestion() {
		return priceSuggestion;
	}

	public void setPriceSuggestion(double d) {
		this.priceSuggestion = d;
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

	public int getPackageUnitID() {
		return packageUnitID;
	}

	public void setPackageUnitID(int packageUnitID) {
		this.packageUnitID = packageUnitID;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPackageUnitName() {
		return packageUnitName;
	}

	public void setPackageUnitName(String packageUnitName) {
		this.packageUnitName = packageUnitName;
	}

	public String getNoRemaining() {
		return noRemaining;
	}

	public void setNoRemaining(String noRemaining) {
		this.noRemaining = noRemaining;
	}

	// 已入库数量
	protected int warehousingNO;

	public int getWarehousingNO() {
		return warehousingNO;
	}

	public void setWarehousingNO(int warehousingNO) {
		this.warehousingNO = warehousingNO;
	}

	public int getPurchasingOrderCommodityIsInWarehouse() {
		return purchasingOrderCommodityIsInWarehouse;
	}

	public void setPurchasingOrderCommodityIsInWarehouse(int purchasingOrderCommodityIsInWarehouse) {
		this.purchasingOrderCommodityIsInWarehouse = purchasingOrderCommodityIsInWarehouse;
	}

	@Override
	public String toString() {
		return "PurchasingOrderCommodity [purchasingOrderID=" + purchasingOrderID + ", priceSuggestion=" + priceSuggestion + ", commodityID=" + commodityID + ", commodityNO=" + commodityNO + ", commodityName=" + commodityName
				+ ", barcodeID=" + barcodeID + ", packageUnitID=" + packageUnitID + ", barcode=" + barcode + ", packageUnitName=" + packageUnitName + ", noRemaining=" + noRemaining + ", purchasingOrderCommodityIsInWarehouse="
				+ purchasingOrderCommodityIsInWarehouse + ", warehousingNO=" + warehousingNO + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing: // 查询未入库的采购商品
			params.put(field.getFIELD_NAME_purchasingOrderID(), orderComm.getPurchasingOrderID());
			params.put("iWarehousing", 0); // ...代表未入库 TODO hardcode
			break;
		default:
			params.put(field.getFIELD_NAME_purchasingOrderID(), orderComm.getPurchasingOrderID());
			params.put(field.getFIELD_NAME_iPageIndex(), orderComm.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), orderComm.getPageSize());
			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing: // 查询已入库的采购商品
			params.put(field.getFIELD_NAME_purchasingOrderID(), orderComm.getPurchasingOrderID());
			params.put("iWarehousing", 1);// ...代表已入库 TODO hardcode
			break;
		default:
			throw new RuntimeException("PurchasingOrderCommodity：未定义getRetrieveNParamEx的Case！");
		}

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_purchasingOrderID(), orderComm.getPurchasingOrderID());
		params.put(field.getFIELD_NAME_commodityID(), orderComm.getCommodityID());
		params.put(field.getFIELD_NAME_commodityNO(), orderComm.getCommodityNO());
		params.put(field.getFIELD_NAME_barcodeID(), orderComm.getBarcodeID());
		params.put(field.getFIELD_NAME_priceSuggestion(), orderComm.getPriceSuggestion());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_purchasingOrderID(), orderComm.getPurchasingOrderID());
		params.put(field.getFIELD_NAME_commodityID(), orderComm.getCommodityID());

		return params;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) arg0;

		if ((ignoreIDInComparision == true ? true : orderComm.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& orderComm.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
				&& orderComm.getCommodityNO() == commodityNO && printComparator(field.getFIELD_NAME_commodityNO())//
				&& orderComm.getPurchasingOrderID() == purchasingOrderID && printComparator(field.getFIELD_NAME_purchasingOrderID()) //
				&& Math.abs(GeneralUtil.sub(orderComm.getPriceSuggestion(), priceSuggestion)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceSuggestion()) //
		) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		PurchasingOrderCommodity obj = new PurchasingOrderCommodity();
		obj.setID(ID);
		obj.setCommodityID(commodityID);
		obj.setCommodityName(commodityName);
		obj.setCommodityNO(commodityNO);
		obj.setPurchasingOrderID(purchasingOrderID);
		obj.setPriceSuggestion(priceSuggestion);
		obj.setBarcodeID(barcodeID);
		obj.setPurchasingOrderCommodityIsInWarehouse(purchasingOrderCommodityIsInWarehouse);
		return obj;
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			purchasingOrderID = jo.getInt(field.getFIELD_NAME_purchasingOrderID());
			priceSuggestion = jo.getDouble(field.getFIELD_NAME_priceSuggestion());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			commodityNO = jo.getInt(field.getFIELD_NAME_commodityNO());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());
			barcodeID = jo.getInt(field.getFIELD_NAME_barcodeID());
			packageUnitID = jo.getInt(field.getFIELD_NAME_packageUnitID());
			//
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}
			//
			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) //
		{
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CheckCreateForAction:
			// Action开始要检查从表，但是这时的主表ID是没有的,所以这种情况不checkCreate PurchasingOrderID
			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) {
				return sbError.toString();
			}
			break;
		}

		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_commodityNO(), FIELD_ERROR_CommodityNO, sbError) && !FieldFormat.checkCommodityNO(commodityNO)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_BarcodeID, sbError) && !FieldFormat.checkID(barcodeID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_priceSuggestion(), FIELD_ERROR_PriceSuggestion, sbError) && !FieldFormat.checkCommodityPrice(priceSuggestion)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing: // 查询已入库的采购商品
			if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) {
				return sbError.toString();
			}
			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing:
			if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) {
				return sbError.toString();
			}

			break;
		default:
			throw new RuntimeException("未定义的CASE！");
		}
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
