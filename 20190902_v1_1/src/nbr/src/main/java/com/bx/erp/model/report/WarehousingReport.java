package com.bx.erp.model.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

public class WarehousingReport extends BaseModel {
	private static final long serialVersionUID = 254976556768978563L;

	public static final int MAX_LENGTH_Barcodes = 32;
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字最大的长度为" + MAX_LENGTH_Barcodes;
	public static final String FIELD_ERROR_isASC = "升序只能是0或1";
	public static final String FIELD_ERROR_iOrderBy = "排序只能是0或1或2";
	public static final String FIELD_ERROR_bIgnoreZeroNO = "bIgnoreZeroNO只能是0或1";
	public static final String FIELD_ERROR_iCategoryID = "iCategoryID必须大于0";
	public static final String FIELD_ERROR_date = "时间格式应该为" + BaseAction.DATETIME_FORMAT_Default2 + "或" + BaseAction.DATETIME_FORMAT_Default4;

	public static final WarehousingReportField field = new WarehousingReportField();

	protected String name;

	protected String barcode;

	protected String specification;

	protected String providerName;

	protected String packageUnitName;

	protected int NO;

	protected double price;

	protected double amount;

	protected Date warehousingDatetime;

	protected int bIgnoreZeroNO;

	protected int iCategoryID;

	protected int requestNO;

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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
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

	public Date getWarehousingDatetime() {
		return warehousingDatetime;
	}

	public void setWarehousingDatetime(Date warehousingDatetime) {
		this.warehousingDatetime = warehousingDatetime;
	}

	@Override
	public String toString() {
		return "WarehousingReport [name=" + name + ", barcode=" + barcode + ", specification=" + specification + ", providerName=" + providerName + ", packageUnitName=" + packageUnitName + ", NO=" + NO + ", price=" + price + ", amount="
				+ amount + ", warehousingDatetime=" + warehousingDatetime + ", iOrderBy=" + iOrderBy + ", isASC=" + isASC + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		WarehousingReport r = (WarehousingReport) arg0;
		if ((ignoreIDInComparision == true ? true : r.getID() == ID && printComparator(field.getFIELD_NAME_ID()))//
				&& r.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& r.getBarcode().equals(barcode) && printComparator(field.getFIELD_NAME_barcode()) //
				&& r.getSpecification().equals(specification) && printComparator(field.getFIELD_NAME_specification()) //
				&& r.getProviderName().equals(providerName) && printComparator(field.getFIELD_NAME_providerName()) //
				&& r.getPackageUnitName().equals(packageUnitName) && printComparator(field.getFIELD_NAME_packageUnitName()) //
				&& r.getNO() == NO && printComparator(field.getFIELD_NAME_NO())//
				&& Math.abs(GeneralUtil.sub(r.getPrice(), price)) < TOLERANCE && printComparator(field.getFIELD_NAME_price())//
				&& Math.abs(GeneralUtil.sub(r.getAmount(), amount)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount())//
		)//
		{
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		WarehousingReport obj = new WarehousingReport();
		obj.setID(ID);
		obj.setName(name);
		obj.setBarcode(barcode);
		obj.setSpecification(specification);
		obj.setProviderName(providerName);
		obj.setPackageUnitName(packageUnitName);
		obj.setNO(NO);
		obj.setSpecification(specification);
		obj.setPrice(price);
		obj.setAmount(amount);
		obj.setDate1((Date) date1.clone());
		obj.setDate2((Date) date2.clone());
		obj.setIsASC(isASC);
		obj.setiOrderBy(iOrderBy);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		checkParameterInput(bm);

		WarehousingReport r = (WarehousingReport) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtStart", (r.getDate1() == null ? null : sdf.format(r.getDate1())));
		params.put("dtEnd", (r.getDate2() == null ? null : sdf.format(r.getDate2())));
		params.put(field.getFIELD_NAME_iOrderBy(), r.getiOrderBy());
		params.put(field.getFIELD_NAME_isASC(), r.getIsASC());
		params.put("queryKeyword", r.getQueryKeyword() == null ? "" : r.getQueryKeyword());
		params.put(field.getFIELD_NAME_bIgnoreZeroNO(), r.getbIgnoreZeroNO());
		params.put(field.getFIELD_NAME_iCategoryID(), r.getiCategoryID());
		params.put(field.getFIELD_NAME_iPageIndex(), r.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), r.getPageSize());

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

		if (printCheckField(field.getFIELD_NAME_date1(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(date1))) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_date2(), FIELD_ERROR_date, sbError) && !FieldFormat.checkDate(sdf.format(date2))) {
			return sbError.toString();
		}

		// queryKeyword可以代表搜索的因子为：商品名称、商品的条形码。其中商品的条形码最长，所以queryKeyword最长能传Barcodes.MAX_LENGTH_Barcodes位
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

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		return "";
	}
}
