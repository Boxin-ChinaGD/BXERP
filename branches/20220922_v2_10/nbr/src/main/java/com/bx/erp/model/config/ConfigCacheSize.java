package com.bx.erp.model.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

public class ConfigCacheSize extends Config {
	private static final long serialVersionUID = 1L;
	public static final ConfigCacheSizeField field = new ConfigCacheSizeField();

	public static int Max_LengthValue = 20;
	public static final String FIELD_ERROR_value = "value不能为空并且长度必须小于等于" + Max_LengthValue + "位";

	protected String name;

	protected String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConfigCacheSize [name=" + name + ", value=" + value + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ConfigCacheSize c = new ConfigCacheSize();
		c.setID(ID);
		c.setName(new String(name));
		c.setValue(new String(value));

		return c;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		ConfigCacheSize c = (ConfigCacheSize) arg0;
		if ((ignoreIDInComparision == true ? true : (c.getName() == this.getName() && printComparator(field.getFIELD_NAME_name()))) //
				&& c.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ConfigCacheSize c = (ConfigCacheSize) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());
		params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ConfigCacheSize c = (ConfigCacheSize) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_value(), c.getValue() == null ? "" : c.getValue());

		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)//
				&& this.printCheckField(field.getFIELD_NAME_value(), FIELD_ERROR_value, sbError) && !StringUtils.isEmpty(value) && value.length() <= Max_LengthValue) {
			return "";
		}

		return sbError.toString();
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	// SP中没有传入参数，所以不用检查
	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
