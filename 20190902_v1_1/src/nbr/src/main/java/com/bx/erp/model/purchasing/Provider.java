package com.bx.erp.model.purchasing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class Provider extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final ProviderField field = new ProviderField();

	public static final int Max_LengthQueryKeyword = 32;
	public static final int Max_LengthName = 32;
	public static final int Max_LengthAddress = 50;
	public static final int Max_LengthContactName = 20;
	public static final int Max_LengthMoile = 24;
	public static final int Min_LengthMoile = 7;

	public static final String ACTION_ERROR_UpdateDelete1 = "默认的供应商不能修改或删除！";
	public static final String FIELD_ERROR_address = "供应商地址长度不超过" + Max_LengthAddress + "个字符，中间允许出现空格，首尾不允许出现空格";
	public static final String FIELD_ERROR_contactName = "供应商联系人名字必须是中英文或数字，中间可以有空格，但首尾不能有空格且长度不超过" + Max_LengthContactName + "个字符";
	public static final String FIELD_ERROR_mobile = "供应商联系人电话不限制输入字符，长度为" + Min_LengthMoile + "到" + Max_LengthMoile + "个字符";
	public static final String FIELD_ERROR_name = "供应商名字不允许有空格出现且长度不超过" + Max_LengthName + "个字符";
	public static final String FIELD_ERROR_checkUniqueField = "非法的值";
	public static final String FIELD_ERROR_districtID = "districtID必须大于0";
	public static final String FIELD_ERROR_uniqueField = "输入的值不能为空,也不能超过" + Max_LengthQueryKeyword + "个字符";
	public static final String FIELD_ERROR_queryKeyword = "输入的值不能为空,也不能超过" + Max_LengthQueryKeyword + "个字符";
	public static final String FIELD_ERROR_ID = "ID不能小于0";

	public static final int CASE_CheckName = 1; // 检查供应商名称
	public static final int CASE_CheckMoblie = 2; // 检查供应商联系人电话

	protected String name;

	protected int districtID;

	protected String address;

	protected String contactName;

	protected String mobile;

	protected String sValue;
	
	/* 在excel表的行号*/ 
	protected int excelLineID;
	
	public int getExcelLineID() {
		return excelLineID;
	}

	public void setExcelLineID(int excelLineID) {
		this.excelLineID = excelLineID;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Provider obj = new Provider();
		obj.setID(ID);
		obj.setName(name);
		obj.setAddress(address);
		obj.setContactName(contactName);
		obj.setMobile(mobile);
		obj.setDistrictID(districtID);

		return obj;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		Provider p = (Provider) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), p.getID());
		params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
		params.put(field.getFIELD_NAME_districtID(), p.getDistrictID());
		params.put(field.getFIELD_NAME_address(), p.getAddress() == null ? "" : p.getAddress());
		params.put(field.getFIELD_NAME_contactName(), p.getContactName() == null ? "" : p.getContactName());
		params.put(field.getFIELD_NAME_mobile(), p.getMobile() == null ? "" : p.getMobile());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {

		Provider pd = (Provider) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), pd.getID());

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		Provider p = (Provider) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
		params.put(field.getFIELD_NAME_districtID(), p.getDistrictID());
		params.put(field.getFIELD_NAME_address(), p.getAddress() == null ? "" : p.getAddress());
		params.put(field.getFIELD_NAME_contactName(), p.getContactName() == null ? "" : p.getContactName());
		params.put(field.getFIELD_NAME_mobile(), p.getMobile() == null ? "" : p.getMobile());

		return params;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Provider p = (Provider) arg0;
		if ((ignoreIDInComparision == true ? true : (p.getID() == ID && printComparator("ID"))) //
				&& p.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& p.getDistrictID() == districtID && printComparator(field.getFIELD_NAME_districtID()) //
				&& p.getAddress().equals(address) && printComparator(field.getFIELD_NAME_address()) //
				&& p.getContactName().equals(contactName) && printComparator(field.getFIELD_NAME_contactName()) //
				&& p.getMobile().equals(mobile) && printComparator(field.getFIELD_NAME_mobile()) //
		) {
			return 0;
		}
		return -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int i) {
		this.districtID = i;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	protected String providerDistrictName;

	public String getProviderDistrictName() {
		return providerDistrictName;
	}

	public void setProviderDistrictName(String providerDistrictName) {
		this.providerDistrictName = providerDistrictName;
	}

	@Override
	public String toString() {
		return "Provider [ID=" + ID + ", name=" + name + ", districtID=" + districtID + ", address=" + address + ", contactName=" + contactName + ", mobile=" + mobile + ", createDatetime=" + createDatetime + ", updateDatetime="
				+ updateDatetime + "]";
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Provider p = (Provider) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Provider_RetrieveNByFields:
			params.put(field.getFIELD_NAME_queryKeyword(), p.getQueryKeyword());
			params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());
			break;
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), p.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), p.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_uniqueField(), p.getUniqueField());

			break;
		default:
			params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
			params.put(field.getFIELD_NAME_districtID(), p.getDistrictID() == 0 ? BaseAction.INVALID_ID : p.getDistrictID());
			params.put(field.getFIELD_NAME_address(), p.getAddress() == null ? "" : p.getAddress());
			params.put(field.getFIELD_NAME_contactName(), p.getContactName() == null ? "" : p.getContactName());
			params.put(field.getFIELD_NAME_mobile(), p.getMobile() == null ? "" : p.getMobile());
			params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());
			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
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

	@Override
	public String checkCreate(int iUseCaseID) {
		return doCheckCreateUpdate(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			switch (fieldToCheckUnique) {
			case CASE_CheckName:
				if (this.printCheckField(Provider.field.FIELD_NAME_name, FIELD_ERROR_name, sbError) // NOT NULL，不允许有空格出现，长度(0, 32]
						&& !FieldFormat.checkProviderName(uniqueField) || uniqueField.length() > Max_LengthName) {
					return sbError.toString();
				}
				break;
			case CASE_CheckMoblie:
				if (!StringUtils.isEmpty(uniqueField)) {
					if (this.printCheckField(Provider.field.FIELD_NAME_mobile, FIELD_ERROR_mobile, sbError) && (FieldFormat.checkProviderMobile(uniqueField))) {
					} else {
						return sbError.toString();
					}
				}
				break;
			default:
				return FIELD_ERROR_checkUniqueField;
			}
		case BaseBO.CASE_Provider_RetrieveNByFields:
			if (!StringUtils.isEmpty(queryKeyword)) {
				if (queryKeyword.length() > Max_LengthQueryKeyword) {
					return FIELD_ERROR_queryKeyword;
				}
			}
			break;
		default:

			if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_ID, sbError) && ID < 0) //
			{
				return sbError.toString();
			}

			if (!StringUtils.isEmpty(address)) {
				if (this.printCheckField(Provider.field.FIELD_NAME_address, FIELD_ERROR_address, sbError) && (FieldFormat.checkProviderAddress(address) && address.length() <= Max_LengthAddress)) {
				} else {
					return sbError.toString();
				}
			}

			if (!StringUtils.isEmpty(contactName)) {
				if (this.printCheckField(Provider.field.FIELD_NAME_contactName, FIELD_ERROR_contactName, sbError) && (FieldFormat.checkContactName(contactName)) && contactName.length() <= Max_LengthContactName) {
				} else {
					return sbError.toString();
				}
			}

			if (!StringUtils.isEmpty(mobile)) {
				if (this.printCheckField(Provider.field.FIELD_NAME_mobile, FIELD_ERROR_mobile, sbError) && (FieldFormat.checkProviderMobile(mobile)) && mobile.length() <= Max_LengthMoile && mobile.length() >= Min_LengthMoile) {
				} else {
					return sbError.toString();
				}
			}
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	protected String doCheckCreateUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		// sbError.append("");

		if (!StringUtils.isEmpty(address)) {
			if (this.printCheckField(Provider.field.FIELD_NAME_address, FIELD_ERROR_address, sbError) && (FieldFormat.checkProviderAddress(address) && address.length() <= Max_LengthAddress)) {
			} else {
				return sbError.toString();
			}
		}

		if (this.printCheckField(Provider.field.FIELD_NAME_districtID, FIELD_ERROR_districtID, sbError) && districtID <= 0) {
			return sbError.toString();
		}

		if (!StringUtils.isEmpty(contactName)) {
			if (this.printCheckField(Provider.field.FIELD_NAME_contactName, FIELD_ERROR_contactName, sbError) && (FieldFormat.checkContactName(contactName)) && contactName.length() <= Max_LengthContactName) {
			} else {
				return sbError.toString();
			}
		}

		if (!StringUtils.isEmpty(mobile)) {
			if (this.printCheckField(Provider.field.FIELD_NAME_mobile, FIELD_ERROR_mobile, sbError) && mobile.length() <= Max_LengthMoile && mobile.length() >= Min_LengthMoile) {
			} else {
				return sbError.toString();
			}
		}

		if (this.printCheckField(Provider.field.FIELD_NAME_name, FIELD_ERROR_name, sbError) // NOT NULL，不允许有空格出现，长度(0, 32]
				&& FieldFormat.checkProviderName(name) && name.length() <= Max_LengthName) {
		} else {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			districtID = jo.getInt(field.getFIELD_NAME_districtID());
			address = jo.getString(field.getFIELD_NAME_address());
			contactName = jo.getString(field.getFIELD_NAME_contactName());
			mobile = jo.getString(field.getFIELD_NAME_mobile());

			String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			SimpleDateFormat sDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			if (!"".equals(tmp)) {
				createDatetime = sDateFormat.parse(tmp);
			}
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = sDateFormat.parse(tmp);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getIndex();
	}
	
	
	public String doCheckCreateUpdate_returnField(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		// sbError.append("");

		if (!StringUtils.isEmpty(address)) {
			if (this.printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && (FieldFormat.checkProviderAddress(address) && address.length() <= Max_LengthAddress)) {
			} else {
				return field.getFIELD_NAME_address();
			}
		}

		if (this.printCheckField(Provider.field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && districtID <= 0) {
			return field.getFIELD_NAME_districtID();
		}

		if (!StringUtils.isEmpty(contactName)) {
			if (this.printCheckField(Provider.field.getFIELD_NAME_contactName(), FIELD_ERROR_contactName, sbError) && (FieldFormat.checkContactName(contactName)) && contactName.length() <= Max_LengthContactName) {
			} else {
				return field.getFIELD_NAME_contactName();
			}
		}

		if (!StringUtils.isEmpty(mobile)) {
			if (this.printCheckField(Provider.field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && mobile.length() <= Max_LengthMoile && mobile.length() >= Min_LengthMoile) {
			} else {
				return field.getFIELD_NAME_mobile();
			}
		}

		if (this.printCheckField(Provider.field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) // NOT NULL，不允许有空格出现，长度(0, 32]
				&& FieldFormat.checkProviderName(name) && name.length() <= Max_LengthName) {
		} else {
			return field.getFIELD_NAME_name();
		}

		return "";
	}
	
	public enum EnumProviderInfoInExcel {
		EPIIE_name("name", 0), //
		EPIIE_district("district", 1),
		EPIIE_address("address", 2),
		EPIIE_contactName("contactName", 3),
		EPIIE_mobile("mobile", 4);
		private String name;
		private int index;

		private EnumProviderInfoInExcel(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumProviderInfoInExcel c : EnumProviderInfoInExcel.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
	
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
