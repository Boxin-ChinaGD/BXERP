package com.bx.erp.model.commodity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CommodityHistory extends BaseModel {
	private static final long serialVersionUID = -7904940833394643L;
	public static final CommodityHistoryField field = new CommodityHistoryField();

	public static final int MAX_LENGTH_queryKeyword = 64;
	public static final int MAX_LENGTH_fieldName = 20;
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字的长度必须是(0, " + MAX_LENGTH_queryKeyword + "]";
	public static final String FIELD_ERROR_fieldName = "字段名称长度是(0, " + MAX_LENGTH_fieldName + "]";
	public static final String FIELD_ERROR_priceRetail = "商品的零售价为非负浮点数";
	public static final String FIELD_ERROR_staffID = "店员ID必须大于0";
	public static final String FIELD_ERROR_commodityID = "商品ID必须大于0";

	protected int commodityID;

	protected String fieldName;

	protected String oldValue;

	protected String newValue;

	protected int staffID;

	protected int bySystem;

	protected Date datetime; // 非数据表数据

	protected String barcodes; // 非数据表数据
	
	protected int shopID;
	
	protected String shopName;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getBySystem() {
		return bySystem;
	}

	public void setBySystem(int bySystem) {
		this.bySystem = bySystem;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	protected String barCodes;

	public String getBarcodes() {
		return barCodes;
	}

	public void setBarcodes(String barCodes) {
		this.barCodes = barCodes;
	}

	protected String commodityName;

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	@Override
	public String toString() {
		return "CommodityHistory [commodityID=" + commodityID + ", fieldName=" + fieldName + ", oldValue=" + oldValue + ", newValue=" + newValue + ", staffID=" + staffID + ", bySystem=" + bySystem + ", datetime=" + datetime + ", ID=" + ID
				+ ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime + "]";
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			fieldName = jo.getString(field.getFIELD_NAME_fieldName());
			oldValue = jo.getString(field.getFIELD_NAME_oldValue());
			newValue = jo.getString(field.getFIELD_NAME_newValue());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			bySystem = jo.getInt(field.getFIELD_NAME_bySystem());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			shopName = jo.getString(field.getFIELD_NAME_shopName());
			String tmp = jo.getString(field.getFIELD_NAME_datetime());
			if (!"".equals(tmp)) {
				datetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (datetime == null) {
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
		List<BaseModel> commodityHistoryList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				CommodityHistory commodityHistory = new CommodityHistory();
				commodityHistory.doParse1(jsonObject);
				commodityHistoryList.add(commodityHistory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return commodityHistoryList;
	}

	public CommodityHistory() {
		super();

		staffID = BaseAction.INVALID_ID;
		commodityID = BaseAction.INVALID_ID;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		CommodityHistory obj = new CommodityHistory();
		obj.setID(ID);
		obj.setCommodityID(commodityID);
		obj.setFieldName(new String(fieldName));
		obj.setOldValue(new String(oldValue));
		obj.setNewValue(new String(newValue));
		obj.setStaffID(staffID);
		obj.setBySystem(bySystem);
		obj.setShopID(shopID);
		obj.setShopName(shopName);
		obj.setDatetime(datetime);
		obj.setDate1((Date) date1.clone());
		obj.setDate2((Date) date2.clone());

		return obj;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		checkParameterInput(bm);

		CommodityHistory ch = (CommodityHistory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_queryKeyword(), ch.getQueryKeyword() == null ? "" : ch.getQueryKeyword());
		params.put(field.getFIELD_NAME_commodityID(), ch.getCommodityID() == 0 ? BaseAction.INVALID_ID : ch.getCommodityID());
		params.put(field.getFIELD_NAME_fieldName(), ch.getFieldName() == null ? "" : ch.getFieldName());
		params.put(field.getFIELD_NAME_staffID(), ch.getStaffID() == 0 ? BaseAction.INVALID_ID : ch.getStaffID());
		params.put(field.getFIELD_NAME_shopID(), ch.getShopID() == 0 ? BaseAction.INVALID_ID : ch.getShopID());
		params.put("dtStart", (ch.getDate1() == null ? null : sdf.format(ch.getDate1())));
		params.put("dtEnd", (ch.getDate2() == null ? null : sdf.format(ch.getDate2())));
		params.put(field.getFIELD_NAME_iPageIndex(), ch.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), ch.getPageSize());

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		// if (string1 == null) {
		// // ...检查其它字段
		// } else if (string1.length() > MAX_LENGTH_string1) {
		// return String.format(FIELD_ERROR_string1, string1.length());
		// }
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && !StringUtils.isEmpty(queryKeyword) && queryKeyword.length() > MAX_LENGTH_queryKeyword) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_fieldName(), FIELD_ERROR_fieldName, sbError) && !(StringUtils.isEmpty(fieldName)) && fieldName.length() > MAX_LENGTH_fieldName) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && staffID != BaseAction.INVALID_ID //
				&& !FieldFormat.checkID(staffID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && commodityID != BaseAction.INVALID_ID //
				&& !FieldFormat.checkID(commodityID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
}
