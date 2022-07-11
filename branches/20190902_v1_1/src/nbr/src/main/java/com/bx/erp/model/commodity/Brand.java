package com.bx.erp.model.commodity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BrandField;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Brand extends BaseModel {
	private static final long serialVersionUID = -8881982176972355100L;
	
	public static int MAX_LENGTH_BrandName = 20;
	public static final String FIELD_ERROR_name = "品牌名字为英文或数字的组合，长度为1到20";
	public static final String ACTION_ERROR_UpdateDelete1 =  "默认的品牌不能修改或删除！";
	
	public static final BrandField field = new BrandField();

	protected String name;

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Brand obj = new Brand();
		obj.setID(ID);
		obj.setName(new String(name));
		obj.setReturnObject(returnObject);

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

	public void setName(String brandName) {
		this.name = brandName;
	}

	@Override
	public String toString() {
		return "Brand [ID = " + ID + ", name=" + name + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> brandList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Brand brnad = new Brand();
				brnad.doParse1(jsonObject);
				brandList.add(brnad);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return brandList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Brand bd = (Brand) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), bd.getName() == null ? "" : bd.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Brand bd = (Brand) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), bd.getID());
		params.put(field.getFIELD_NAME_name(), bd.getName() == null ? "" : bd.getName());
		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm);
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Brand bd = (Brand) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), bd.getName() == null ? "" : bd.getName());
		params.put(field.getFIELD_NAME_iPageIndex(), bd.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), bd.getPageSize());

		return params;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Brand b = (Brand) arg0;
		if ((ignoreIDInComparision == true ? true : (b.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& b.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkBrandName(name)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkBrandName(name)) {
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
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(name)) {
			if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_BrandName)) {
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
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
