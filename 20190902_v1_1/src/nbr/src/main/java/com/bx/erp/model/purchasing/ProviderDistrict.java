package com.bx.erp.model.purchasing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProviderDistrict extends BaseModel {
	private static final long serialVersionUID = 604218879451542244L;

	public static final ProviderDistrictField field = new ProviderDistrictField();

	public static int Max_LengthName = 20;
	public static final String ACTION_ERROR_UpdateDelete1 = "默认的供应商区域不能修改或删除！";
	public static final String FIELD_ERROR_name = "供应商区域名字不能为空且长度不超过" + Max_LengthName + "个字符";

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProviderDistrict [ID=" + ID + ", name=" + name + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ProviderDistrict pd = (ProviderDistrict) arg0;
		if ((ignoreIDInComparision == true ? true : (pd.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& pd.getName().equals(name) && printComparator(field.getFIELD_NAME_name())) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ProviderDistrict pd = new ProviderDistrict();
		pd.setID(ID);
		pd.setName(name);

		return pd;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ProviderDistrict pd = (ProviderDistrict) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("F_Name", pd.getName() == null ? "" : pd.getName());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ProviderDistrict pd = (ProviderDistrict) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("F_ID", pd.getID());
		params.put("F_Name", pd.getName() == null ? "" : pd.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ProviderDistrict pd = (ProviderDistrict) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), pd.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), pd.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(ProviderDistrict.field.FIELD_NAME_name, FIELD_ERROR_name, sbError) // NOT NULL，长度(0, 20]
				&& !StringUtils.isEmpty(name) && name.length() <= Max_LengthName) {
		} else {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)) //
		{
		} else {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_name, sbError) && !StringUtils.isEmpty(name) && name.length() <= Max_LengthName) //
		{
		} else {
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
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());

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
	public List<BaseModel> parseN(String s) {
		List<BaseModel> providerDistrictList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ProviderDistrict providerDistrict = new ProviderDistrict();
				providerDistrict.doParse1(jsonObject);
				providerDistrictList.add(providerDistrict);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return providerDistrictList;
	}
	

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
