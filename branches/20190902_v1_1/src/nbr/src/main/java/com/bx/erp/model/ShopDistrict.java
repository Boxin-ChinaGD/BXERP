package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

public class ShopDistrict extends BaseModel {
	private static final long serialVersionUID = 3615845902887280564L;
	public static final ShopDistrictField field = new ShopDistrictField();

	private static final int MAX_LENGTH_Name = 20;
	public static final String FIELD_ERROR_name = "区域名称的长度为(0, " + MAX_LENGTH_Name + "]";

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ShopDistrict [ID=" + ID + ", name=" + name + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ShopDistrict sd = (ShopDistrict) arg0;
		if ((ignoreIDInComparision == true ? true : (sd.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& sd.getName().equals(name) && printComparator(field.getFIELD_NAME_name())) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ShopDistrict sd = new ShopDistrict();
		sd.setID(ID);
		sd.setName(name);

		return sd;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ShopDistrict sd = (ShopDistrict) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("F_Name", sd.getName() == null ? "" : sd.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ShopDistrict sd = (ShopDistrict) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), sd.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), sd.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && StringUtils.isEmpty(name) || name.length() > MAX_LENGTH_Name) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
