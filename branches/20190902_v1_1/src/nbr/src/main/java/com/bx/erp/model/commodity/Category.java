package com.bx.erp.model.commodity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Category extends BaseModel {
	private static final long serialVersionUID = 2743060524128260721L;

	public static final String ACTION_ERROR_UpdateDelete1 = "默认的分类不能修改或删除！";
	public static final String FIELD_ERROR_name = "小类名字为中英文的组合，长度1到10";
	public static final String FIELD_ERROR_parentID = "parentID必须大于0";
	public static final String FIELD_ERROR_checkUniqueField = "非法的值";
	public static final int MAX_LENGTH_CategoryName = 10;
	public static final int MIN_LENGTH_CategoryName = 0;

	protected String name;
	protected int parentID;
	private final int CASE_CheckName = 1;
	public static final CategoryField field = new CategoryField();

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getParentID() {
		return parentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", parentID=" + parentID + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Category category = (Category) arg0;
		if ((ignoreIDInComparision == true ? true : (category.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& category.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& category.getParentID() == parentID && printComparator(field.getFIELD_NAME_parentID())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Category obj = new Category();
		obj.setID(ID);
		obj.setName(name);
		obj.setParentID(parentID);
		obj.setFieldToCheckUnique(fieldToCheckUnique);
		obj.setReturnObject(returnObject);

		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Category c = (Category) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());
		params.put(field.getFIELD_NAME_parentID(), c.getParentID());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Category c = (Category) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), c.getID());
		params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());
		params.put(field.getFIELD_NAME_parentID(), c.getParentID());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm);
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Category c = (Category) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_Category_RetrieveNByParent:
			params.put(field.getFIELD_NAME_ID(), c.getID());

			break;
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), c.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), c.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_uniqueField(), c.getUniqueField());

			break;
		default:
			params.put(field.getFIELD_NAME_name(), c.getName() == null ? "" : c.getName());
			params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());
			break;
		}
		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryName && name.length() <= MAX_LENGTH_CategoryName))//
		{
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_parentID(), FIELD_ERROR_parentID, sbError) && !FieldFormat.checkID(parentID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryName && name.length() <= MAX_LENGTH_CategoryName))//
		{
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_parentID(), FIELD_ERROR_parentID, sbError) && !FieldFormat.checkID(parentID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:

			switch (fieldToCheckUnique) {
			case CASE_CheckName:
				if (uniqueField == null || !(FieldFormat.checkCategoryName(uniqueField) && uniqueField.length() > MIN_LENGTH_CategoryName && uniqueField.length() <= MAX_LENGTH_CategoryName)) {
					return FIELD_ERROR_checkUniqueField;
				}
				break;
			default:
				return FIELD_ERROR_checkUniqueField;
			}
		default:
			if (!StringUtils.isEmpty(name)) {
				if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_CategoryName)) {
					return sbError.toString();
				}
			}
			break;
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

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			parentID = jo.getInt(field.getFIELD_NAME_parentID());
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
	public List<BaseModel> parseN(String s) {
		List<BaseModel> categoryList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Category category = new Category();
				category.doParse1(jsonObject);
				categoryList.add(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return categoryList;
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
