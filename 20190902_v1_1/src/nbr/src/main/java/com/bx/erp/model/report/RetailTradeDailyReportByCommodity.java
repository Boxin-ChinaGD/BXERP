package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

public class RetailTradeDailyReportByCommodity extends BaseModel {
	private static final long serialVersionUID = 254976556768978563L;

	public static final RetailTradeDailyReportByCommodityField field = new RetailTradeDailyReportByCommodityField();

	public static final int MAX_LENGTH_Barcodes = 32;
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字最大的长度为" + MAX_LENGTH_Barcodes;
	public static final String FIELD_ERROR_isASC = "升序只能是0或1";
	public static final String FIELD_ERROR_iOrderBy = "排序只能是0或1或2";
	public static final String FIELD_ERROR_bIgnoreZeroNO = "bIgnoreZeroNO只能是0或1";
	public static final String FIELD_ERROR_iCategoryID = "iCategoryID必须大于0";
	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;
	
	protected String TradeIDs;

	protected String barcode;

	protected String name;

	protected String specification;

	protected String packageUnitName;
	
	protected int shopID;

	protected int NO;

	protected double totalAmount;

	protected double averagePrice;

	protected double grossMargin;

	protected Date datetime;

	protected int commodityID;

	protected double totalPurchasingAmount;

	protected int bIgnoreZeroNO;

	protected int iCategoryID;

	protected Date saleDatetime;

	protected Date datetimeStart;

	protected Date datetimeEnd;

	public Date getDatetimeStart() {
		return datetimeStart;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeStart(Date datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

	public Date getSaleDatetime() {
		return saleDatetime;
	}

	public void setSaleDatetime(Date saleDatetime) {
		this.saleDatetime = saleDatetime;
	}

	public int getiCategoryID() {
		return iCategoryID;
	}

	public void setiCategoryID(int iCategoryID) {
		this.iCategoryID = iCategoryID;
	}

	public int getbIgnoreZeroNO() {
		return bIgnoreZeroNO;
	}

	public void setbIgnoreZeroNO(int bIgnoreZeroNO) {
		this.bIgnoreZeroNO = bIgnoreZeroNO;
	}

	public String getTradeIDs() {
		return TradeIDs;
	}

	public void setTradeIDs(String tradeIDs) {
		TradeIDs = tradeIDs;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public double getGrossMargin() {
		return grossMargin;
	}

	public void setGrossMargin(double grossMargin) {
		this.grossMargin = grossMargin;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public double getTotalPurchasingAmount() {
		return totalPurchasingAmount;
	}

	public void setTotalPurchasingAmount(double totalPurchasingAmount) {
		this.totalPurchasingAmount = totalPurchasingAmount;
	}
	
	

	// @Override
	// public int compareTo(final BaseModel arg0) {
	// if (arg0 == null) {
	// return -1;
	// }
	// RetailTradeDailyReportByCommodity rtbc = new
	// RetailTradeDailyReportByCommodity();
	// if(
	// (ignoreIDInComparision == true ? true : rtbc.getID() == ID &&
	// printComparator(getFIELD_NAME_ID()))//
	// && rtbc.getID() == ID && printComparator(getFIELD_NAME_ID()) //
	// && rtbc.getBarcode().equals(barcode) &&
	// printComparator(getFIELD_NAME_barcode()) //
	// && rtbc.getName().equals(name) && printComparator(getFIELD_NAME_name()) //
	// && rtbc.getSpecification().equals(specification) &&
	// printComparator(getFIELD_NAME_specification()) //
	// && rtbc.getPackageUnitName().equals(packageUnitName) &&
	// printComparator(getFIELD_NAME_packageUnitName())//
	// && Math.abs(rtbc.getTotalAmount() - totalAmount) < TOLERANCE &&
	// printComparator(getFIELD_NAME_totalAmount())//
	// && Math.abs(rtbc.getAveragePrice() - averagePrice) < TOLERANCE &&
	// printComparator(getFIELD_NAME_averagePrice())//
	// && Math.abs(rtbc.getGrossMargin() - grossMargin) < TOLERANCE &&
	// printComparator(getFIELD_NAME_grossMargin())//
	// && DatetimeUtil.compareDate(rtbc.getDatetime(), datetime) &&
	// printComparator(getFIELD_NAME_datetime())//
	// )//
	// {
	// return 0;
	// }
	// return -1;
	// }

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RetailTradeDailyReportByCommodity [TradeIDs=" + TradeIDs + ", barcode=" + barcode + ", name=" + name + ", specification=" + specification + ", packageUnitName=" + packageUnitName + ", NO=" + NO + ", totalAmount="
				+ totalAmount + ", averagePrice=" + averagePrice + ", grossMargin=" + grossMargin + ", datetime=" + datetime + ", commodityID=" + commodityID + ", totalPurchasingAmount=" + totalPurchasingAmount + ", bIgnoreZeroNO="
				+ bIgnoreZeroNO + ", iCategoryID=" + iCategoryID + ", saleDatetime=" + saleDatetime + ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RetailTradeDailyReportByCommodity rtbc = new RetailTradeDailyReportByCommodity();
		rtbc.setID(ID);
		rtbc.setShopID(shopID);
		rtbc.setBarcode(barcode);
		rtbc.setName(name);
		rtbc.setSpecification(specification);
		rtbc.setPackageUnitName(packageUnitName);
		rtbc.setNO(this.getNO());
		rtbc.setTotalAmount(totalAmount);
		rtbc.setAveragePrice(averagePrice);
		rtbc.setGrossMargin(grossMargin);
		rtbc.setDatetime(datetime);
		rtbc.setCommodityID(commodityID);
		rtbc.setTotalPurchasingAmount(totalPurchasingAmount);
		rtbc.setDatetime(datetime);
		rtbc.setSaleDatetime(saleDatetime);
		rtbc.setDatetimeStart((Date) datetimeStart.clone());
		rtbc.setDatetimeEnd((Date) datetimeEnd.clone());
		rtbc.setIsASC(isASC);
		rtbc.setiOrderBy(iOrderBy);
		rtbc.setiCategoryID(iCategoryID);
		rtbc.setPageIndex(pageIndex);
		rtbc.setPageSize(pageSize);

		return rtbc;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		checkParameterInput(bm);

		RetailTradeDailyReportByCommodity rtbc = (RetailTradeDailyReportByCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_shopID(), rtbc.getShopID());
		params.put(field.getFIELD_NAME_datetimeStart(), rtbc.getDatetimeStart() == null ? null : sdf.format(rtbc.getDatetimeStart()));
		params.put(field.getFIELD_NAME_datetimeEnd(), rtbc.getDatetimeEnd() == null ? null : sdf.format(rtbc.getDatetimeEnd()));
		params.put(field.getFIELD_NAME_queryKeyword(), rtbc.getQueryKeyword() == null ? "" : rtbc.getQueryKeyword());
		params.put(field.getFIELD_NAME_isASC(), rtbc.getIsASC());
		params.put(field.getFIELD_NAME_iOrderBy(), rtbc.getiOrderBy());
		params.put(field.getFIELD_NAME_iPageIndex(), rtbc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rtbc.getPageSize());
		params.put(field.getFIELD_NAME_bIgnoreZeroNO(), rtbc.getbIgnoreZeroNO());
		params.put(field.getFIELD_NAME_iCategoryID(), rtbc.getiCategoryID());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		if (printCheckField(field.getFIELD_NAME_datetimeStart(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeStart))) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_datetimeEnd(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(datetimeEnd))) {
			return sbError.toString();
		}

		// queryKeyword可以代表搜索的因子为：商品名称、零售商品ID、商品的条形码。其中商品的条形码最长，所以queryKeyword最长能传Barcodes.MAX_LENGTH_Barcodes位
		if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && !StringUtils.isEmpty(queryKeyword) && queryKeyword.length() > MAX_LENGTH_Barcodes) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_isASC(), FIELD_ERROR_isASC, sbError) && !(isASC == 0 || isASC == 1)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_iOrderBy(), FIELD_ERROR_iOrderBy, sbError) && !(iOrderBy == 0 || iOrderBy == 1 || iOrderBy == 2)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_bIgnoreZeroNO(), FIELD_ERROR_bIgnoreZeroNO, sbError) && !(bIgnoreZeroNO == 0 || bIgnoreZeroNO == 1)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_iCategoryID(), FIELD_ERROR_iCategoryID, sbError) && iCategoryID != BaseAction.INVALID_ID && !FieldFormat.checkID(iCategoryID)) {
			return sbError.toString();
		}
		
		if (printCheckField(field.getFIELD_NAME_shopID(), FieldFormat.FIELD_ERROR_ID_ForRetrieveN, sbError) && iCategoryID != BaseAction.INVALID_ID && !FieldFormat.checkID(iCategoryID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
