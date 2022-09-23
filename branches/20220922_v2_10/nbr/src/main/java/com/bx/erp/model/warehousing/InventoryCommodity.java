package com.bx.erp.model.warehousing;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class InventoryCommodity extends BaseModel {
	private static final long serialVersionUID = -122358298066930467L;
	public static final InventoryCommodityField field = new InventoryCommodityField();

	protected static final int MIN_ID = 0;

	public static final String FIELD_ERROR_noReal = "实盘数量必须为正负整数";
	public static final String FIELD_ERROR_inventorySheetID = "inventorySheetID必须大于0";
	public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";

	/** 代表无效的待盘点的商品的数量，包括实盘数量和系统库存。前端检查到这2个字段是这个值时，不显示任何东西给用户看 */
	protected final int INVALID_NO_INVENTORY_Initial = -10000000;

	protected int inventorySheetID;

	protected int commodityID;

	protected int noReal;

	protected int noSystem;

	protected String commodityName;

	protected String specification;

	protected int barcodeID;

	protected int packageUnitID;

	public int getInventorySheetID() {
		return inventorySheetID;
	}

	public void setInventorySheetID(int inventorySheetID) {
		this.inventorySheetID = inventorySheetID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getNoReal() {
		return noReal;
	}

	public void setNoReal(int noReal) {
		this.noReal = noReal;
	}

	public int getNoSystem() {
		return noSystem;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public void setNoSystem(int noSystem) {
		this.noSystem = noSystem;
	}

	protected String barcodes;

	public String getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;
	}

	protected String packageUnitName;

	public String getPackageUnitName() {
		return packageUnitName;
	}

	public void setPackageUnitName(String packageUnitName) {
		this.packageUnitName = packageUnitName;
	}

	@Override
	public String toString() {
		return "InventoryCommodity [inventorySheetID=" + inventorySheetID + ", commodityID=" + commodityID + ", noReal=" + noReal + ", noSystem=" + noSystem + ", commodityName=" + commodityName + ", specification=" + specification
				+ ", barcodeID=" + barcodeID + ", packageUnitID=" + packageUnitID + "]";
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		InventoryCommodity ic = (InventoryCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_inventorySheetID(), ic.getInventorySheetID());
		params.put(field.getFIELD_NAME_commodityID(), ic.getCommodityID());
		params.put(field.getFIELD_NAME_barcodeID(), ic.getBarcodeID());
		params.put(field.getFIELD_NAME_noReal(), ic.getNoReal());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		InventoryCommodity ic = (InventoryCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_UpdateInventoryCommodityNoReal:
			params.put(field.getFIELD_NAME_ID(), ic.getID());
			params.put(field.getFIELD_NAME_noReal(), ic.getNoReal());
			params.put(field.getFIELD_NAME_noSystem(), ic.getNoSystem());
			break;
		default:
			params.put(field.getFIELD_NAME_ID(), ic.getID());
			params.put(field.getFIELD_NAME_noReal(), ic.getNoReal());
			break;
		}

		return params;
	}

	@Override
	public int compareTo(BaseModel arg0) {
		InventoryCommodity its = (InventoryCommodity) arg0;
		if ((ignoreIDInComparision == true ? true
				: its.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID())//
						&& its.getInventorySheetID() == this.getInventorySheetID() && printComparator(field.getFIELD_NAME_inventorySheetID()))//
				&& its.getCommodityID() == this.getCommodityID() && printComparator(field.getFIELD_NAME_commodityID())//
				&& its.getBarcodeID() == this.getBarcodeID() && printComparator(field.getFIELD_NAME_barcodeID())//
				&& (its.getNoReal() == -1 ? true : its.getNoReal() == this.getNoReal() && printComparator(field.getFIELD_NAME_noReal()))//
				&& (its.getNoSystem() == INVALID_NO_INVENTORY_Initial ? true : its.getNoSystem() == this.getNoSystem() && printComparator(field.getFIELD_NAME_noReal()))//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		InventoryCommodity ic = (InventoryCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_inventorySheetID(), ic.getInventorySheetID());
		params.put(field.getFIELD_NAME_iPageIndex(), ic.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), ic.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		InventoryCommodity ic = (InventoryCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_inventorySheetID(), ic.getInventorySheetID());
		params.put(field.getFIELD_NAME_commodityID(), ic.getCommodityID());
		return params;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		InventoryCommodity ic = new InventoryCommodity();
		ic.setID(ID);
		ic.setInventorySheetID(inventorySheetID);
		ic.setCommodityID(commodityID);
		ic.setCommodityName(commodityName);
		ic.setSpecification(specification);
		ic.setPackageUnitID(packageUnitID);
		ic.setBarcodeID(barcodeID);
		ic.setNoReal(noReal);
		ic.setNoSystem(noSystem);

		return ic;
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			inventorySheetID = jo.getInt(field.getFIELD_NAME_inventorySheetID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());
			specification = jo.getString(field.getFIELD_NAME_specification());
			packageUnitID = jo.getInt(field.getFIELD_NAME_packageUnitID());
			barcodeID = jo.getInt(field.getFIELD_NAME_barcodeID());
			noReal = jo.getInt(field.getFIELD_NAME_noReal());
			noSystem = jo.getInt(field.getFIELD_NAME_noSystem());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) //
		{
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_inventorySheetID(), FIELD_ERROR_inventorySheetID, sbError) && !FieldFormat.checkID(inventorySheetID)) //
		{
			return sbError.toString();
		}
		return doCheckCreateUpdate(iUseCaseID);
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_inventorySheetID(), FIELD_ERROR_inventorySheetID, sbError) && !FieldFormat.checkID(inventorySheetID)) //
		{
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (commodityID <= MIN_ID) {
			if (this.printCheckField(field.getFIELD_NAME_inventorySheetID(), FIELD_ERROR_inventorySheetID, sbError) && !FieldFormat.checkID(inventorySheetID)) //
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
		return doCheckCreateUpdate(iUseCaseID);
	}

	protected String doCheckCreateUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		// sbError.append("");
		// if (this.getNoReal() != -1) {
		if (this.printCheckField(field.getFIELD_NAME_noReal(), InventoryCommodity.FIELD_ERROR_noReal, sbError) && !FieldFormat.checkInventoryCommodityNoReal(String.valueOf(noReal))) //
		{
			return sbError.toString();
		}
		// }
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
