package com.bx.erp.model.commodity;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RefCommodityHub extends BaseModel {
	private static final long serialVersionUID = -2890386510951323242L;

	private static final int MAX_LENGTH_Barcode = 20;
	private static final int MIN_LENGTH_Barcode = 1;
	public static final String FIELD_ERROR_barcode = "输入的条形码格式不正确，仅允许英文、数值形式出现，长度为[" + MIN_LENGTH_Barcode + ", " + MAX_LENGTH_Barcode + "]";

	public static final RefCommodityHubField field = new RefCommodityHubField();

	protected String name;

	protected String barcode;

	protected String shortName;

	protected String specification;

	protected String packageUnitName;

	protected String purchasingUnit;

	protected String brandName;

	protected String categoryName;

	protected String mnemonicCode;

	protected int pricingType;

	protected int type;

	protected double pricePurchase;

	protected double priceRetail;

	protected double priceVIP;

	protected double priceWholesale;

	protected int shelfLife;

	protected int returnDays;

	protected double latestPricePurchase;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPackageUnitName() {
		return packageUnitName;
	}

	public void setPackageUnitName(String packageUnitName) {
		this.packageUnitName = packageUnitName;
	}

	public String getPurchasingUnit() {
		return purchasingUnit;
	}

	public void setPurchasingUnit(String purchasingUnit) {
		this.purchasingUnit = purchasingUnit;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMnemonicCode() {
		return mnemonicCode;
	}

	public void setMnemonicCode(String mnemonicCode) {
		this.mnemonicCode = mnemonicCode;
	}

	public int getPricingType() {
		return pricingType;
	}

	public void setPricingType(int pricingType) {
		this.pricingType = pricingType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getPricePurchase() {
		return pricePurchase;
	}

	public void setPricePurchase(double pricePurchase) {
		this.pricePurchase = pricePurchase;
	}

	public double getPriceRetail() {
		return priceRetail;
	}

	public void setPriceRetail(double priceRetail) {
		this.priceRetail = priceRetail;
	}

	public double getPriceVIP() {
		return priceVIP;
	}

	public void setPriceVIP(double priceVIP) {
		this.priceVIP = priceVIP;
	}

	public double getPriceWholesale() {
		return priceWholesale;
	}

	public void setPriceWholesale(double priceWholesale) {
		this.priceWholesale = priceWholesale;
	}

	public int getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public int getReturnDays() {
		return returnDays;
	}

	public void setReturnDays(int returnDays) {
		this.returnDays = returnDays;
	}

	public double getLatestPricePurchase() {
		return latestPricePurchase;
	}

	public void setLatestPricePurchase(double latestPricePurchase) {
		this.latestPricePurchase = latestPricePurchase;
	}

	@Override
	public String toString() {
		return "RefCommodityHub [name=" + name + ", barcode=" + barcode + ", shortName=" + shortName + ", specification=" + specification + ", packageUnitName=" + packageUnitName + ", purchasingUnit=" + purchasingUnit + ", brandName="
				+ brandName + ", categoryName=" + categoryName + ", mnemonicCode=" + mnemonicCode + ", pricingType=" + pricingType + ", type=" + type + ", pricePurchase=" + pricePurchase + ", priceRetail=" + priceRetail + ", priceVIP="
				+ priceVIP + ", priceWholesale=" + priceWholesale + ", shelfLife=" + shelfLife + ", returnDays=" + returnDays + ", latestPricePurchase=" + latestPricePurchase + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		RefCommodityHub rch = (RefCommodityHub) arg0;
		if ((ignoreIDInComparision == true ? true : (rch.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rch.getBarcode().equals(barcode) && printComparator(field.getFIELD_NAME_barcode()) //
				&& rch.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& rch.getShortName().equals(shortName) && printComparator(field.getFIELD_NAME_shortName()) //
				&& rch.getSpecification().equals(specification) && printComparator(field.getFIELD_NAME_specification()) //
				&& rch.getPackageUnitName().equals(packageUnitName) && printComparator(field.getFIELD_NAME_packageUnitName()) //
				&& rch.getPurchasingUnit().equals(purchasingUnit) && printComparator(field.getFIELD_NAME_purchasingUnit()) //
				&& rch.getBrandName().equals(brandName) && printComparator(field.getFIELD_NAME_brandName()) //
				&& rch.getCategoryName().equals(categoryName) && printComparator(field.getFIELD_NAME_categoryName()) //
				&& rch.getMnemonicCode().equals(mnemonicCode) && printComparator(field.getFIELD_NAME_mnemonicCode()) //
				&& rch.getPricingType() == pricingType && printComparator(field.getFIELD_NAME_pricingType()) //
				&& Math.abs(GeneralUtil.sub(rch.getPriceRetail(), priceRetail)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceRetail()) //
				&& Math.abs(GeneralUtil.sub(rch.getPriceVIP(), priceVIP)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceVIP()) //
				&& Math.abs(GeneralUtil.sub(rch.getPriceWholesale(), priceWholesale)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceWholesale()) //
				&& rch.getShelfLife() == shelfLife && printComparator(field.getFIELD_NAME_shelfLife()) //
				&& rch.getReturnDays() == returnDays && printComparator(field.getFIELD_NAME_returnDays()) //
				&& rch.getType() == type && printComparator(field.getFIELD_NAME_type()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RefCommodityHub obj = new RefCommodityHub();
		obj.setID(ID);
		obj.setBarcode(barcode);
		obj.setName(new String(name));
		obj.setShortName(new String(shortName));
		obj.setSpecification(new String(specification));
		obj.setPackageUnitName(packageUnitName);
		obj.setPurchasingUnit(new String(purchasingUnit));
		obj.setBrandName(brandName);
		obj.setCategoryName(categoryName);
		obj.setMnemonicCode(new String(mnemonicCode));
		obj.setPricingType(pricingType);
		obj.setPricePurchase(pricePurchase);
		obj.setPriceRetail(priceRetail);
		obj.setPriceVIP(priceVIP);
		obj.setPriceWholesale(priceWholesale);
		obj.setShelfLife(shelfLife);
		obj.setReturnDays(returnDays);
		obj.setType(type);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		RefCommodityHub rhc = (RefCommodityHub) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_barcode(), rhc.getBarcode() == null ? "" : rhc.getBarcode());
		params.put(field.getFIELD_NAME_iPageIndex(), rhc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rhc.getPageSize());

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcode, sbError) && barcode.length() > MAX_LENGTH_Barcode || !FieldFormat.checkBarcode(barcode)) {
			return sbError.toString();
		}
		return "";
	}

}
