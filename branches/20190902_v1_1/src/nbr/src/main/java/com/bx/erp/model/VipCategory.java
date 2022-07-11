package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class VipCategory extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final VipCategoryField field = new VipCategoryField();

	public static final int DEFAULT_VipCategory_ID = 1;
	public static final int MAX_LENGTH_Name = 30;
	public static final String FIELD_ERROR_name = "会员分类名称不合法并且会员分类长度为(0, " + MAX_LENGTH_Name + "]";

	private String name;

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		VipCategory obj = new VipCategory();
		obj.setID(this.getID());
		obj.setName(new String(this.getName()));

		return obj;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "VipCategory [name=" + name + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
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
			return this;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		VipCategory vc = (VipCategory) arg0;
		if ((ignoreIDInComparision == true ? true : (vc.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))) //
				&& vc.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCategory vc = (VipCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), vc.getName() == null ? "" : vc.getName());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCategory vc = (VipCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), vc.getID());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCategory vc = (VipCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), vc.getID());
		params.put(field.getFIELD_NAME_name(), vc.getName() == null ? "" : vc.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCategory vc = (VipCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), vc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), vc.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCategory vc = (VipCategory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), vc.getID());

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		default:
			if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipCategoryName(name)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipCategoryName(name)) {
			return sbError.toString();
		}

		return "";
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
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
