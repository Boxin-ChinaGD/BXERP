package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeReportByCommodity extends BaseModel {
	private static final long serialVersionUID = 254976556768978563L;

	public static final int MAX_LENGTH_Barcodes = 32;
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字最大的长度为" + MAX_LENGTH_Barcodes;
	public static final String FIELD_ERROR_isASC = "升序只能是0或1";
	public static final String FIELD_ERROR_iOrderBy = "排序只能是0或1或2";
	public static final String FIELD_ERROR_bIgnoreZeroNO = "bIgnoreZeroNO只能是0或1";
	public static final String FIELD_ERROR_iCategoryID = "iCategoryID必须大于0";
	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final RetailTradeReportByCommodityField field = new RetailTradeReportByCommodityField();

	protected String name;

	protected String specification;

	protected String packageUnitName;

	protected String barcode;

	protected int NO;

	protected double amount;

	protected int tradeID;

	protected double averagePrice;

	protected double grossMargin;

	protected Date saleDatetime;

	protected int bIgnoreZeroNO;

	protected int iCategoryID;

	protected int requestNO;

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

	public int getRequestNO() {
		return requestNO;
	}

	public void setRequestNO(int requestNO) {
		this.requestNO = requestNO;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTradeID() {
		return tradeID;
	}

	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
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

	public Date getSaleDatetime() {
		return saleDatetime;
	}

	public void setSaleDatetime(Date saleDatetime) {
		this.saleDatetime = saleDatetime;
	}

	@Override
	public String toString() {
		return "RetailTradeReportByCommodity [name=" + name + ", specification=" + specification + ", packageUnitName=" + packageUnitName + ", barcode=" + barcode + ", NO=" + NO + ", amount=" + amount + ", tradeID=" + tradeID
				+ ", averagePrice=" + averagePrice + ", grossMargin=" + grossMargin + ", saleDatetime=" + saleDatetime + ", bIgnoreZeroNO=" + bIgnoreZeroNO + ", iCategoryID=" + iCategoryID + ", requestNO=" + requestNO + ", datetimeStart="
				+ datetimeStart + ", datetimeEnd=" + datetimeEnd + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		// id name specification packageUnitName barcode NO amount tradeID averagePrice
		// grossMargin saleDatetime
		RetailTradeReportByCommodity r = (RetailTradeReportByCommodity) arg0;
		if ((ignoreIDInComparision == true ? true : r.getID() == ID && printComparator(field.getFIELD_NAME_ID()))//
				&& r.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& r.getSpecification().equals(specification) && printComparator(field.getFIELD_NAME_specification()) //
				&& r.getPackageUnitName().equals(packageUnitName) && printComparator(field.getFIELD_NAME_packageUnitName())//
				&& r.getBarcode().equals(barcode) && printComparator(field.getFIELD_NAME_barcode()) //
				&& r.getNO() == NO && printComparator(field.getFIELD_NAME_NO())//
				&& r.getTradeID() == tradeID && printComparator(field.getFIELD_NAME_tradeID())//
				&& Math.abs(GeneralUtil.sub(r.getAmount(), amount)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount())//
				&& Math.abs(GeneralUtil.sub(r.getAveragePrice(), averagePrice)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount())//
				&& r.getGrossMargin() == grossMargin && printComparator(field.getFIELD_NAME_grossMargin())//
				&& DatetimeUtil.compareDate(r.getSaleDatetime(), saleDatetime) && printComparator(field.getFIELD_NAME_saleDatetime())//
		)//
		{
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RetailTradeReportByCommodity obj = new RetailTradeReportByCommodity();
		obj.setID(ID);
		obj.setName(name);
		obj.setSpecification(specification);
		obj.setPackageUnitName(packageUnitName);
		obj.setBarcode(barcode);
		obj.setNO(NO);
		obj.setAmount(amount);
		obj.setTradeID(tradeID);
		obj.setAveragePrice(averagePrice);
		obj.setGrossMargin(grossMargin);
		obj.setSaleDatetime(saleDatetime == null ? null : (Date) saleDatetime.clone());
		obj.setDatetimeStart(datetimeStart == null ? null : (Date) datetimeStart.clone());
		obj.setDatetimeEnd(datetimeEnd == null ? null : (Date) datetimeEnd.clone());
		obj.setIsASC(isASC);
		obj.setiOrderBy(iOrderBy);
		obj.setiCategoryID(iCategoryID);
		obj.setPageIndex(pageIndex);
		obj.setPageSize(pageSize);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		checkParameterInput(bm);

		RetailTradeReportByCommodity r = (RetailTradeReportByCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_datetimeStart(), (r.getDatetimeStart() == null ? null : sdf.format(r.getDatetimeStart())));
		params.put(field.getFIELD_NAME_datetimeEnd(), (r.getDatetimeEnd() == null ? null : sdf.format(r.getDatetimeEnd())));
		params.put(field.getFIELD_NAME_queryKeyword(), r.getQueryKeyword() == null ? "" : r.getQueryKeyword());
		params.put(field.getFIELD_NAME_isASC(), r.getIsASC());
		params.put(field.getFIELD_NAME_iOrderBy(), r.getiOrderBy());
		params.put(field.getFIELD_NAME_iPageIndex(), r.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), r.getPageSize());
		params.put(field.getFIELD_NAME_bIgnoreZeroNO(), r.getbIgnoreZeroNO());
		params.put(field.getFIELD_NAME_iCategoryID(), r.getiCategoryID());

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

		// queryKeyword可以代表搜索的因子为：商品名称、零售ID、商品的条形码。其中商品的条形码最长，所以queryKeyword最长能传Barcodes.MAX_LENGTH_Barcodes位
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
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
