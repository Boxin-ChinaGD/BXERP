package com.bx.erp.model.commodity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class CategoryParent extends BaseModel {
	private static final long serialVersionUID = -5696039012578062132L;
	public static final String ACTION_ERROR_UpdateDelete1 =  "默认分类不能修改或删除";
	public static final String FIELD_ERROR_name = "大类名字为中英文的组合，长度1到10";
	private static int MAX_LENGTH_CategoryPanentName = 10;
	private static int MIN_LENGTH_CategoryPanentName = 0;

	public static final CategoryField field = new CategoryField();
	
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "CategoryParent [name=" + name + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		CategoryParent categoryParent = (CategoryParent) arg0;
		if ((ignoreIDInComparision == true ? true : (categoryParent.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& categoryParent.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		CategoryParent obj = new CategoryParent();
		obj.setID(ID);
		obj.setName(new String(name));

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CategoryParent c = (CategoryParent) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CategoryParent c = (CategoryParent) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CategoryParent c = (CategoryParent) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());
		params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());

		return params;
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(name)) {
			if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_CategoryPanentName)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !(FieldFormat.checkID(ID)))//
		{
			return sbError.toString();
		}
		
		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryPanentName && name.length() <= MAX_LENGTH_CategoryPanentName))//
		{
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryPanentName && name.length() <= MAX_LENGTH_CategoryPanentName))//
		{
			return sbError.toString();
		}
		
		return "";
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
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
