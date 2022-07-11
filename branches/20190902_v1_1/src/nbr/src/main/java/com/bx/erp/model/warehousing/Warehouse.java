
package com.bx.erp.model.warehousing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Warehouse extends BaseModel {
	private static final long serialVersionUID = 3860280940380963356L;
	public static final WarehouseField field = new WarehouseField();

	protected static final int MaxNameAndAddress = 32;
	
	public static final int DEFAULT_ID = 1;

	public static final String ACTION_ERROR_UpdateDelete1 = "默认的仓库不能修改或删除！";
	public static final String FIELD_ERROR_name = "仓库名字英文或数字或中文的组合，长度大于0，最大长度" + MaxNameAndAddress;
	public static final String FIELD_ERROR_address = "仓库的地址为中文或英文或数字的组合，中间允许有空格且长度大于0，最大长度" + MaxNameAndAddress;
	public static final String FIELD_ERROR_phone = "手机格式错误，应为" + FieldFormat.LENGTH_Mobile + "位的数字";
	public static final String FIELD_ERROR_staffID = "staffID必须大于0";
	public static final String FIELD_ERROR_shopID = "shopID必须大于0";


	protected String name;

	protected String address;

	protected int status;

	protected int staffID;
	
	protected String phone;
	
	protected String staffName;
	
	protected String commodityName;
	
	protected double fMaxTotalInventory;//库存总额最高的商品的库存总额,非db字段
	
	protected double fTotalInventory;//本仓库的库存总额,非db字段
	
	protected int shopID;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

//	public static double DEFINE_GET_TotalNO(double totalNO) {
//		return totalNO;
//	}

	public double getfMaxTotalInventory() {
		return fMaxTotalInventory;
	}

	public void setfMaxTotalInventory(double fMaxTotalInventory) {
		this.fMaxTotalInventory = fMaxTotalInventory;
	}
	
	
	public double getfTotalInventory() {
		return fTotalInventory;
	}

	public void setfTotalInventory(double fTotalInventory) {
		this.fTotalInventory = fTotalInventory;
	}

	@Override
	public String toString() {
		return "Warehouse [name=" + name + ", address=" + address + ", status=" + status + ", staffID=" + staffID + ", phone=" + phone + ", staffName=" + staffName + ", commodityName=" + commodityName + ", fMaxTotalInventory="
				+ fMaxTotalInventory + ", fTotalInventory=" + fTotalInventory + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Warehouse obj = new Warehouse();
		obj.setID(ID);
		obj.setName(new String(name));
		obj.setAddress(address);
		obj.setStatus(status);
		obj.setStaffID(staffID);
		obj.setPhone(phone);
		obj.setShopID(shopID);
		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Warehouse wh = (Warehouse) arg0;
		if ((ignoreIDInComparision == true ? true : (wh.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))) //
				&& wh.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
				// && wh.getBX_CustomerID() == this.getBX_CustomerID() &&
				// printComparator(getFIELD_NAME_BX_CustomerID()) //
				&& (wh.getAddress() == null ? true : wh.getAddress().equals(this.getAddress())) && printComparator(field.getFIELD_NAME_address()) //
				&& wh.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status()) //
				&& (wh.getStaffID() == 0 ? true : wh.getStaffID() == this.getStaffID()) && printComparator(field.getFIELD_NAME_staffID())//
				&& (wh.getPhone() == null ? true : wh.getPhone().equals(this.getPhone())) && printComparator(field.getFIELD_NAME_phone())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}
		Warehouse wh = (Warehouse) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put(getFIELD_NAME_BX_CustomerID(), wh.getBX_CustomerID());
		params.put(field.getFIELD_NAME_name(), wh.getName());
		params.put(field.getFIELD_NAME_address(), wh.getAddress() == "" ? null : wh.getAddress());
		params.put(field.getFIELD_NAME_status(), wh.getStatus());
		params.put(field.getFIELD_NAME_staffID(), wh.getStaffID() == 0 ? null : wh.getStaffID());
		params.put(field.getFIELD_NAME_phone(), wh.getPhone() == "" ? null : wh.getPhone());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}
		switch (iUseCaseID) {
		case BaseBO.CASE_Warehouse_RetrieveInventory:
			Warehouse wh = (Warehouse) bm;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(field.getFIELD_NAME_shopID(), wh.getShopID());
			return params;
		default:
			return getSimpleParam(iUseCaseID, bm.getID());
		}
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Warehouse wh = (Warehouse) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		// case BaseBO.CASE_RetrieveCommodityByNFields:
		// params.put("sString1", wh.getString1());
		// params.put("F_BX_CustomerID", wh.getBX_CustomerID());
		// params.put(getFIELD_NAME_iPageIndex(), wh.getPageIndex());
		// params.put(getFIELD_NAME_iPageSize(), wh.getPageSize());
		//
		// break;
		default:
			params.put(field.getFIELD_NAME_name(), wh.getName() == null ? "" : wh.getName());
			// params.put(getFIELD_NAME_BX_CustomerID(), wh.getBX_CustomerID());
			// //SP并没有以这个参数为查询条件.
			params.put(field.getFIELD_NAME_iPageIndex(), wh.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), wh.getPageSize());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Warehouse wh = (Warehouse) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), ID);
		// params.put(getFIELD_NAME_BX_CustomerID(), wh.getBX_CustomerID());
		params.put(field.getFIELD_NAME_name(), wh.getName());
		params.put(field.getFIELD_NAME_address(), wh.getAddress() == "" ? null : wh.getAddress());
		params.put(field.getFIELD_NAME_staffID(), wh.getStaffID() == 0 ? null : wh.getStaffID());
		params.put(field.getFIELD_NAME_phone(), wh.getPhone() == "" ? null : wh.getPhone());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_Warehouse_RetrieveInventory:
			if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) //
			{
				return sbError.toString();
			}
			return "";
		default:
			return super.checkRetrieve1(iUseCaseID);
		}
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(name)) {
			if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && name.length() > MaxNameAndAddress) //
			{
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

	protected String doCheckCreateUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && (FieldFormat.checkWarehouseName(name) && name.length() > 0 && name.length() <= MaxNameAndAddress)//
				&& ((this.getAddress() == null || "".equals(this.getAddress())) ? true
						: this.printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && (FieldFormat.checkAddress(address) && address.length() >= 0 && address.length() <= MaxNameAndAddress)) //
		) {
		} else {
			return sbError.toString();
		}

		if (!StringUtils.isEmpty(phone)) {
			if (this.printCheckField(field.getFIELD_NAME_phone(), Warehouse.FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) //
			{
				return sbError.toString();
			}
		}
		return "";
	}

	public enum EnumStatusWarehouse {
		ESW_Normal("Normal", 0), //
		ESW_Deleted("Deleted", 1);

		private String name;
		private int index;

		private EnumStatusWarehouse(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusWarehouse c : EnumStatusWarehouse.values()) {
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

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			address = jo.getString(field.getFIELD_NAME_address());
			status = jo.getInt(field.getFIELD_NAME_status());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			phone = jo.getString(field.getFIELD_NAME_phone());
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}
			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> warehouseList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				Warehouse wh = new Warehouse();
				wh.doParse1(jsonObject1);
				warehouseList.add(wh);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return warehouseList;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_WarehouseCacheSize.getIndex();
	}
}
