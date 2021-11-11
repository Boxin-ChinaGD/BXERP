package com.bx.erp.model.commodity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.model.BaseModel;

public class CommodityProperty extends BaseModel {

	private static final long serialVersionUID = 1L;
	public static final CommodityPropertyField field = new CommodityPropertyField();

	public static final int Max_LengthName = 16;
	public static final String FIELD_ERROR_name = "属性名称不能超过" + Max_LengthName + "个字符";

	@Override
	public String toString() {
		return "CommodityProperty [name1=" + name1 + ", name2=" + name2 + ", name3=" + name3 + ", name4=" + name4 + ", ID=" + ID + "]";
	}

	protected String name1;

	protected String name2;

	protected String name3;

	protected String name4;

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(name1)) {
			if (this.printCheckField(field.getFIELD_NAME_name1(), FIELD_ERROR_name, sbError) && name1.length() > Max_LengthName) //
			{
				return sbError.toString();
			}
		}

		if (!StringUtils.isEmpty(name2)) {
			if (this.printCheckField(field.getFIELD_NAME_name2(), FIELD_ERROR_name, sbError) && name2.length() > Max_LengthName) //
			{
				return sbError.toString();
			}
		}

		if (!StringUtils.isEmpty(name3)) {
			if (this.printCheckField(field.getFIELD_NAME_name3(), FIELD_ERROR_name, sbError) && name3.length() > Max_LengthName) //
			{
				return sbError.toString();
			}
		}

		if (!StringUtils.isEmpty(name4)) {
			if (this.printCheckField(field.getFIELD_NAME_name4(), FIELD_ERROR_name, sbError) && name4.length() > Max_LengthName) //
			{
				return sbError.toString();
			}
		}

		return "";
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CommodityProperty cp = (CommodityProperty) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name1(), cp.getName1() == null ? "" : cp.getName1());
		params.put(field.getFIELD_NAME_name2(), cp.getName2() == null ? "" : cp.getName2());
		params.put(field.getFIELD_NAME_name3(), cp.getName3() == null ? "" : cp.getName3());
		params.put(field.getFIELD_NAME_name4(), cp.getName4() == null ? "" : cp.getName4());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		CommodityProperty cp = (CommodityProperty) arg0;
		if ((ignoreIDInComparision == true ? true : (cp.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
		) {
			return 0;
		}
		return -1;
	}

}
