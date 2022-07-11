package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Shop extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final ShopField field = new ShopField();
	public static final int Max_LengthQueryKeyword = 32;
	private static final int Zero = 0;
	private static final int MAX_LENGTH_Name = 20;
	private static final int MAX_LENGTH_Address = 30;
	private static final int MAX_LENGTH_Key = 32;
	private static final int MAX_LENGTH_Remark = 128;
	public static final String FIELD_ERROR_longitude = "经度不能为" + Zero;
	public static final String FIELD_ERROR_latitude = "纬度不能为" + Zero;
	public static final String FIELD_ERROR_remark = "备注的长度为(" + Zero + ", " + MAX_LENGTH_Remark + "]";
	public static final String FIELD_ERROR_key = "钥匙的长度为(" + Zero + ", " + MAX_LENGTH_Key + "]";
	public static final String FIELD_ERROR_name = "门店名称长度为(" + Zero + ", " + MAX_LENGTH_Name + "]";
	public static final String FIELD_ERROR_address = "门店地址长度为(" + Zero + ", " + MAX_LENGTH_Address + "]";
	public static final String FIELD_ERROR_checkUniqueField = "非法的值";
	public static final String FIELD_ERROR_districtID = "区域ID必须大于" + Zero;
	public static final String FIELD_ERROR_companyID = "公司ID必须大于" + Zero;
	public static final String FIELD_ERROR_bxStaffID = "业务经理ID必须大于" + Zero;
	public static final String FIELD_ERROR_status = "门店的状态码只能是" + EnumStatusShop.ESS_Online.index + "、" + EnumStatusShop.ESS_Offline.index + "、" //
			+ EnumStatusShop.ESS_DisconnectionLocking.index + "、" + EnumStatusShop.ESS_ArrearageLocking.index + "、" + EnumStatusShop.ESS_ToGetFinancialApproval.index + "、";
	public static final String FIELD_ERROR_queryKeyword = "输入的值不能超过" + Max_LengthQueryKeyword + "个字符";
	public static final int CASE_CheckName = 1; // 检查门店名称

	protected Company company;
	protected List<Pos> listPos;

	protected String name;

	protected String bxStaffName;

	protected int companyID;

	protected int bxStaffID;

	protected String remark;

	protected String address;

	protected int districtID;

	protected int status;

	protected double longitude;

	protected double latitude;

	protected String key;

	public String getBxStaffName() {
		return bxStaffName;
	}

	public void setBxStaffName(String bxStaffName) {
		this.bxStaffName = bxStaffName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Pos> getListPos() {
		return listPos;
	}

	public void setListPos(List<Pos> listPos) {
		this.listPos = listPos;
	}
	
	public int getBxStaffID() {
		return bxStaffID;
	}

	public void setBxStaffID(int bxStaffID) {
		this.bxStaffID = bxStaffID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}

	protected String districtName;

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	protected String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public String toString() {
		return "Shop [company=" + company + ", listPos=" + listPos + ", name=" + name + ", bxStaffName=" + bxStaffName + ", companyID=" + companyID + ", bxStaffID=" + bxStaffID + ", remark=" + remark + ", address=" + address
				+ ", districtID=" + districtID + ", status=" + status + ", longitude=" + longitude + ", latitude=" + latitude + ", key=" + key + ", districtName=" + districtName + ", dbName=" + dbName + ", ID=" + ID + ", createDatetime="
				+ createDatetime + ", updateDatetime=" + updateDatetime + ", fieldToCheckUnique=" + fieldToCheckUnique + ", uniqueField=" + uniqueField + "]";
	}

	@Override
	public BaseModel clone() {
		Shop obj = new Shop();
		obj.setID(ID);
		obj.setName(name);
		obj.setCompanyID(companyID);
		obj.setAddress(address);
		obj.setDistrictID(districtID);
		obj.setStatus(status);
		obj.setLongitude(longitude);
		obj.setLatitude(latitude);
		obj.setKey(key);
		obj.setBxStaffID(bxStaffID);
		obj.setRemark(remark);
		obj.setUniqueField(uniqueField);
		obj.setDistrictName(districtName);
		obj.setDbName(dbName);

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Shop s = (Shop) arg0;
		if ((ignoreIDInComparision == true ? true : (s.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& s.getName().equals(name) && printComparator(field.getFIELD_NAME_name())//
				&& s.getCompanyID() == companyID && printComparator(field.getFIELD_NAME_companyID())//
				&& s.getAddress().equals(address) && printComparator(field.getFIELD_NAME_address())//
				&& s.getDistrictID() == districtID && printComparator(field.getFIELD_NAME_districtID())//
				&& s.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
				&& Math.abs(GeneralUtil.sub(s.getLatitude(), latitude)) < TOLERANCE && printComparator(field.getFIELD_NAME_latitude()) //
				&& Math.abs(GeneralUtil.sub(s.getLongitude(), longitude)) < TOLERANCE && printComparator(field.getFIELD_NAME_longitude()) //
				&& s.getKey().equals(key) && printComparator(field.getFIELD_NAME_key())//
				&& s.getRemark().equals(remark) && printComparator(field.getFIELD_NAME_remark())//
				&& s.getBxStaffID() == bxStaffID && printComparator(field.getFIELD_NAME_bxStaffID())//
		) {
			return 0;
		}
		return -1;
	}

	public Shop() {
		super();

		districtID = BaseAction.INVALID_ID;

	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Shop s = (Shop) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), s.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), s.getPageSize());
		params.put(field.getFIELD_NAME_districtID(), s.getDistrictID() == 0 ? BaseAction.INVALID_ID : s.getDistrictID());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Shop s = (Shop) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), s.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), s.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_uniqueField(), s.getUniqueField());

			break;
		case BaseBO.CASE_Shop_RetrieveNByFields:
			params.put(field.getFIELD_NAME_queryKeyword(), s.getQueryKeyword());
			params.put(field.getFIELD_NAME_districtID(), s.getDistrictID() == 0 ? BaseAction.INVALID_ID : s.getDistrictID());
			params.put(field.getFIELD_NAME_iPageIndex(), s.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), s.getPageSize());
			break;
		default:
			params.put(field.getFIELD_NAME_iPageIndex(), s.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), s.getPageSize());
			params.put(field.getFIELD_NAME_districtID(), s.getDistrictID() == 0 ? BaseAction.INVALID_ID : s.getDistrictID());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Shop s = (Shop) bm;
		switch (iUseCaseID) {
		case BaseBO.INVALID_CASE_ID:
		case BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount:
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(field.getFIELD_NAME_name(), s.getName());
			params.put(field.getFIELD_NAME_companyID(), s.getCompanyID());
			params.put(field.getFIELD_NAME_address(), s.getAddress() == null ? "" : s.getAddress());
			params.put(field.getFIELD_NAME_districtID(), s.getDistrictID() == 0 ? BaseAction.INVALID_ID : s.getDistrictID());
			params.put(field.getFIELD_NAME_status(), s.getStatus());
			params.put(field.getFIELD_NAME_longitude(), s.getLongitude());
			params.put(field.getFIELD_NAME_latitude(), s.getLatitude());
			params.put(field.getFIELD_NAME_key(), s.getKey() == null ? "" : s.getKey());
			params.put(field.getFIELD_NAME_remark(), s.getRemark() == null ? "" : s.getRemark());
			params.put(field.getFIELD_NAME_bxStaffID(), s.getBxStaffID());
			params.put(field.getFIELD_NAME_districtID(), s.getDistrictID());

			return params;
		default:
			throw new RuntimeException("未定义的CASE！");
		}

	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Shop s = (Shop) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), s.getID());
		params.put(field.getFIELD_NAME_name(), s.getName() == null ? "" : s.getName());
		params.put(field.getFIELD_NAME_address(), s.getAddress() == null ? "" : s.getAddress());
		params.put(field.getFIELD_NAME_districtID(), s.getDistrictID() == 0 ? BaseAction.INVALID_ID : s.getDistrictID());
		params.put(field.getFIELD_NAME_longitude(), s.getLongitude());
		params.put(field.getFIELD_NAME_latitude(), s.getLatitude());
		params.put(field.getFIELD_NAME_key(), s.getKey() == null ? "" : s.getKey());
		params.put(field.getFIELD_NAME_remark(), s.getRemark() == null ? "" : s.getRemark());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Shop s = (Shop) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), s.getID());
		params.put(field.getFIELD_NAME_companyID(), s.getCompanyID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		return getSimpleParam(iUseCaseID, bm);
	}

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkShopName(name)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && !FieldFormat.checkShopAddress(address)) {
			return sbError.toString();
		}

//		if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && key.length() > MAX_LENGTH_Key) {
//			return sbError.toString();
//		}

		if (printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && !StringUtils.isEmpty(remark) && remark.length() > MAX_LENGTH_Remark) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_longitude(), FIELD_ERROR_longitude, sbError) && longitude < Zero) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_latitude(), FIELD_ERROR_latitude, sbError) && latitude < Zero) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && !FieldFormat.checkID(districtID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkShopName(name)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && !FieldFormat.checkShopAddress(address)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && StringUtils.isEmpty(key) || key.length() > MAX_LENGTH_Key) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && !StringUtils.isEmpty(remark) && remark.length() > MAX_LENGTH_Remark) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_longitude(), FIELD_ERROR_longitude, sbError) && longitude < Zero) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_latitude(), FIELD_ERROR_latitude, sbError) && latitude < Zero) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusShop.ESS_Online.index //
				|| status == EnumStatusShop.ESS_Offline.index || status == EnumStatusShop.ESS_DisconnectionLocking.index //
				|| status == EnumStatusShop.ESS_ArrearageLocking.index || status == EnumStatusShop.ESS_ToGetFinancialApproval.index)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && !FieldFormat.checkID(companyID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && !FieldFormat.checkID(districtID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_bxStaffID(), FIELD_ERROR_bxStaffID, sbError) && !FieldFormat.checkID(bxStaffID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && !FieldFormat.checkID(companyID)) {
			return sbError.toString();
		}

		// if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID,
		// sbError) && ID < Zero) {
		// return sbError.toString();
		// }

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && ID < 0) {
				return sbError.toString();
			}
			switch (fieldToCheckUnique) {
			case CASE_CheckName:
				if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkShopName(uniqueField)) {
					return sbError.toString();
				}
				break;
			default:
				return FIELD_ERROR_checkUniqueField;
			}
			break;
		case BaseBO.CASE_Shop_RetrieveNByFields:
			if (!StringUtils.isEmpty(queryKeyword)) {
				if (queryKeyword.length() > Max_LengthQueryKeyword) {
					return FIELD_ERROR_queryKeyword;
				}
			}
			break;
		default:
			if (printCheckField(field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && districtID != BaseAction.INVALID_ID && !FieldFormat.checkID(districtID)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}
	
	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			bxStaffName = jo.getString(field.getFIELD_NAME_bxStaffName());
			remark = jo.getString(field.getFIELD_NAME_remark());
			address = jo.getString(field.getFIELD_NAME_address());
			districtID = jo.getInt(field.getFIELD_NAME_districtID());
			districtName = jo.getString(field.getFIELD_NAME_districtName());
			status = jo.getInt(field.getFIELD_NAME_status());
			key = jo.getString(field.getFIELD_NAME_key());
			longitude = jo.getDouble(field.getFIELD_NAME_longitude());
			latitude = jo.getDouble(field.getFIELD_NAME_latitude());
			companyID = jo.getInt(field.getFIELD_NAME_companyID());
			return this;
		} catch(Exception e) {
			return null;
		}
	}

	public enum EnumStatusShop {
		ESS_Online("Online", 0), // 营业中
		ESS_Offline("Offline", 1), // 离线中
		ESS_DisconnectionLocking("DisconnectionLocking", 2), // 断网锁定
		ESS_ArrearageLocking("ArrearageLocking", 3), // 欠费锁定
		ESS_ToGetFinancialApproval("ToGetFinancialApproval", 4); // 财务待审核

		private String name;
		private int index;

		private EnumStatusShop(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusShop c : EnumStatusShop.values()) {
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

	public static int DEFINE_SET_CompanyID(int id) {
		return id;
	}

//	@Override
//	public int getCacheSizeID() {
//		return BaseCache.Shop_CACHESIZE_ID;
//	}
	
	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}
	
	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> shopList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Shop shop = new Shop();
				shop.doParse1(jsonObject);
				shopList.add(shop);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return shopList;
	}
	
	
}
