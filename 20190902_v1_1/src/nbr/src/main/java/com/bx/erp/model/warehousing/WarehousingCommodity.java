package com.bx.erp.model.warehousing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class WarehousingCommodity extends BaseModel {
	private static final long serialVersionUID = -4413233549408515540L;
	public static final WarehousingCommodityField field = new WarehousingCommodityField();

	// protected static final int MaxCommodityName = 32;
	public static final double DEFAULT_VALUE_Amount = 0.000000d;

	public static final String FIELD_ERROR_no = "数量必须是大于0，小于等于" + FieldFormat.MAX_OneCommodityNO + "的整数";
	public static final String FIELD_ERROR_amount = "金额必须大于等于" + DEFAULT_VALUE_Amount;
	public static final String FIELD_ERROR_price = "进货价必须大于等于0,小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_shelfLife = "保质期只允许是正整数（自然数）";
	// public static final String FIELD_ERROR_commodityName =
	// "商品名字为中文或英文或数字的组合,长度小于" + MaxCommodityName;
	public static final String FIELD_ERROR_warehousingID = "warehousingID必须大于0";
	public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";
	public static final String FIELD_ERROR_barcodeID = "barcodeID必须大于0";

	protected int warehousingID;

	protected int commodityID;

	protected int NO;

	protected int packageUnitID;

	protected String commodityName;

	protected int barcodeID;

	protected double price;

	protected double amount;

	protected Date productionDatetime;

	protected int shelfLife;

	protected Date expireDatetime;

	protected String barcode;

	protected String packageUnitName;

	protected int noSalable;

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

	public int getWarehousingID() {
		return warehousingID;
	}

	public void setWarehousingID(int warehousingID) {
		this.warehousingID = warehousingID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public int getPackageUnitID() {
		return packageUnitID;
	}

	public void setPackageUnitID(int packageUnitID) {
		this.packageUnitID = packageUnitID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getProductionDatetime() {
		return productionDatetime;
	}

	public void setProductionDatetime(Date productionDatetime) {
		this.productionDatetime = productionDatetime;
	}

	public int getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public Date getExpireDatetime() {
		return expireDatetime;
	}

	public void setExpireDatetime(Date expireDatetime) {
		this.expireDatetime = expireDatetime;
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

	public int getNoSalable() {
		return noSalable;
	}

	public void setNoSalable(int noSalable) {
		this.noSalable = noSalable;
	}

	@Override
	public String toString() {
		return "WarehousingCommodity [warehousingID=" + warehousingID + ", commodityID=" + commodityID + ", NO=" + NO + ", packageUnitID=" + packageUnitID + ", commodityName=" + commodityName + ", barcodeID=" + barcodeID + ", price="
				+ price + ", amount=" + amount + ", FIELD_NAME_amount=" + field.FIELD_NAME_amount + ", productionDatetime=" + productionDatetime + ", shelfLife=" + shelfLife + ", expireDatetime=" + expireDatetime + ", barcode=" + barcode
				+ ", packageUnitName=" + packageUnitName + ", noSalable=" + noSalable + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		WarehousingCommodity wc = (WarehousingCommodity) arg0;
		if ((ignoreIDInComparision == true ? true
				: wc.getID() == ID && printComparator(field.getFIELD_NAME_ID())//
						&& wc.getWarehousingID() == warehousingID && printComparator(field.getFIELD_NAME_warehousingID()))//
				&& wc.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID())//
				&& wc.getNO() == NO && printComparator(field.getFIELD_NAME_NO())//
				&& wc.getBarcodeID() == barcodeID && printComparator(field.getFIELD_NAME_barcodeID())//
				&& wc.getNoSalable() == noSalable && printComparator(field.getFIELD_NAME_noSalable())//
				&& Math.abs(GeneralUtil.sub(wc.getPrice(), price)) < TOLERANCE && printComparator(field.getFIELD_NAME_price())//
				&& Math.abs(GeneralUtil.sub(wc.getAmount(), amount)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount())//
				&& wc.getShelfLife() == shelfLife && printComparator(field.getFIELD_NAME_shelfLife())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		WarehousingCommodity obj = new WarehousingCommodity();
		obj.setID(ID);
		obj.setWarehousingID(warehousingID);
		obj.setCommodityID(commodityID);
		obj.setNO(NO);
		obj.setPackageUnitID(packageUnitID);
		obj.setCommodityName(commodityName);
		obj.setBarcodeID(barcodeID);
		obj.setPrice(price);
		obj.setAmount(amount);
		obj.setShelfLife(shelfLife);
		obj.setNoSalable(noSalable);

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_warehousingID(), warehousingCommodity.getWarehousingID());
		params.put(field.getFIELD_NAME_commodityID(), warehousingCommodity.getCommodityID());
		params.put(field.getFIELD_NAME_NO(), warehousingCommodity.getNO());
		params.put(field.getFIELD_NAME_barcodeID(), warehousingCommodity.getBarcodeID());
		params.put(field.getFIELD_NAME_price(), warehousingCommodity.getPrice());
		params.put(field.getFIELD_NAME_amount(), warehousingCommodity.getAmount());
		params.put(field.getFIELD_NAME_shelfLife(), warehousingCommodity.getShelfLife());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_warehousingID(), warehousingCommodity.getWarehousingID());
		params.put(field.getFIELD_NAME_iPageIndex(), warehousingCommodity.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), warehousingCommodity.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_warehousingID(), warehousingCommodity.getWarehousingID());
		params.put(field.getFIELD_NAME_commodityID(), warehousingCommodity.getCommodityID());
		params.put(field.getFIELD_NAME_price(), warehousingCommodity.getPrice());
		params.put(field.getFIELD_NAME_NO(), warehousingCommodity.getNO());
		params.put(field.getFIELD_NAME_amount(), warehousingCommodity.getAmount());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_warehousingID(), warehousingCommodity.getWarehousingID());
		params.put(field.getFIELD_NAME_commodityID(), warehousingCommodity.getCommodityID());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (doCheckCreateUpdate(sbError, iUseCaseID).trim().length() > 0) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_barcodeID(), FIELD_ERROR_barcodeID, sbError) && !FieldFormat.checkID(barcodeID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) {
			return sbError.toString();
		}
		// if (this.printCheckField(field.getFIELD_NAME_commodityName(),
		// FIELD_ERROR_commodityName, sbError) &&
		// !FieldFormat.checkCommodityName(commodityName)) {
		// return sbError.toString();
		// }

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (doCheckCreateUpdate(sbError, iUseCaseID).trim().length() > 0) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
			return sbError.toString();
		}
		return "";
	}

	protected String doCheckCreateUpdate(StringBuilder sbError, int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckCreateForAction:
			// Action开始要检查从表，但是这时的主表ID是没有的,所以这种情况不checkCreate warehousingID
			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
				return sbError.toString();
			}
			break;
		}
		if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_no, sbError) && !FieldFormat.checkCommodityNO(NO)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_price(), FIELD_ERROR_price, sbError) && !FieldFormat.checkCommodityPrice(price)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_amount(), FIELD_ERROR_amount, sbError) && amount < DEFAULT_VALUE_Amount) {
			return sbError.toString();
		}
		return "";
	}

	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			warehousingID = jo.getInt(field.getFIELD_NAME_warehousingID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			NO = jo.getInt(field.getFIELD_NAME_NO());
			packageUnitID = jo.getInt(field.getFIELD_NAME_packageUnitID());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());
			barcodeID = jo.getInt(field.getFIELD_NAME_barcodeID());
			price = jo.getDouble(field.getFIELD_NAME_price());
			amount = jo.getDouble(field.getFIELD_NAME_amount());
			String tmpProductionDatetime = jo.getString(field.getFIELD_NAME_productionDatetime());
			if (!"".equals(tmpProductionDatetime)) {
				productionDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpProductionDatetime);
				if (productionDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_productionDatetime() + "=" + tmpProductionDatetime);
				}
			}
			this.shelfLife = jo.getInt(field.getFIELD_NAME_shelfLife());
			String tmpExpireDatetime = jo.getString(field.getFIELD_NAME_expireDatetime());
			if (!"".equals(tmpExpireDatetime)) {
				expireDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpExpireDatetime);
				if (expireDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_expireDatetime() + "=" + tmpExpireDatetime);
				}
			}
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}
			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			noSalable = jo.getInt(field.getFIELD_NAME_noSalable());
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
