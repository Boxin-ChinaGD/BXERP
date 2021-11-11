package com.bx.erp.model.commodity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONObject;

public class PackageUnit extends BaseModel {
	private static final long serialVersionUID = -8197141776205319869L;

	public static final int MAX_LENGTH_Name = 8;
	public static final int MIN_LENGTH_Name = 1;
	public static final String FIELD_ERROR_name = "名称长度为["+ MIN_LENGTH_Name + ", " + MAX_LENGTH_Name + "]";
	
	public static final PackageUnitField field = new PackageUnitField();
	protected String name;
	protected int normalCommodityID;
	/**
	 * 判断多包装商品具体修改了哪些字段
	 */
	protected int whichPropertyIsChanged;
	
	public int getWhichPropertyIsChanged() {
		return whichPropertyIsChanged;
	}

	public void setWhichPropertyIsChanged(int whichPropertyIsChanged) {
		this.whichPropertyIsChanged = whichPropertyIsChanged;
	}

	public int getNormalCommodityID() {
		return normalCommodityID;
	}

	public void setNormalCommodityID(int normalCommodityID) {
		this.normalCommodityID = normalCommodityID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "PackageUnit [name=" + name + ", ID=" + ID + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		PackageUnit pu = (PackageUnit) arg0;
		if ((ignoreIDInComparision == true ? true : (pu.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& pu.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		PackageUnit obj = new PackageUnit();
		obj.setID(ID);
		obj.setName(name);

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		PackageUnit pu = (PackageUnit) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iNormalCommodityID", pu.getNormalCommodityID());
		params.put(field.getFIELD_NAME_iPageIndex(), pu.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), pu.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		PackageUnit pu = (PackageUnit) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), pu.getName() == null ? "" : pu.getName());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		PackageUnit pu = (PackageUnit) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), pu.getID());
		params.put(field.getFIELD_NAME_name(), pu.getName() == null ? "" : pu.getName());
		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm);
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError)//
				&& name.length() > MAX_LENGTH_Name || !FieldFormat.checkName(name)) {
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		
		if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}
		
		if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) //
				 && !FieldFormat.checkName(name) || name.length() > MAX_LENGTH_Name) {
			return sbError.toString();
		}
		
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	protected int commodityID;
	
	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
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
