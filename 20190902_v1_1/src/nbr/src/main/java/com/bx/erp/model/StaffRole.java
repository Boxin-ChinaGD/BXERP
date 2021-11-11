package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StaffRole extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_ERROR_staffID = "店员ID必须大于0";
	public static final String FIELD_ERROR_IDAndStaffID = "ID和staffID不能同时为0";
	public static final String FIELD_ERROR_roleID = "角色ID必须大于0";
	public static final String FIELD_ERROR_status = "角色的状态只能为0或1";
	public static final String FIELD_ERROR_operator = "操作者只能是" + EnumBoolean.EB_NO.getIndex() + "或" + EnumBoolean.EB_Yes.getIndex(); // 0代表老板操作，1代表OP操作

	public static final StaffRoleField field = new StaffRoleField();

	protected int staffID;

	protected int roleID;

	protected String salt;

	private String phone;

	private String name;

	private int IDInPOS;

	private String POS_SN;

	private String weChat;

	private String openid;// 用户关注公众号的唯一标识

	private String unionid;// 只有将公众号绑定到微信开放平台帐号后，才会出现该字段。

	private String ICID;

	private Date passwordExpireDate;

	private int isFirstTimeLogin;

	private String q1;

	private String a1;

	private String q2;

	private String a2;

	private String q3;

	private String a3;

	private int shopID;

	private int departmentID;

	private int status;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIDInPOS() {
		return IDInPOS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIDInPOS(int iDInPOS) {
		IDInPOS = iDInPOS;
	}

	public String getPOS_SN() {
		return POS_SN;
	}

	public void setPOS_SN(String pOS_SN) {
		POS_SN = pOS_SN;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getICID() {
		return ICID;
	}

	public void setICID(String iCID) {
		ICID = iCID;
	}

	public Date getPasswordExpireDate() {
		return passwordExpireDate;
	}

	public void setPasswordExpireDate(Date passwordExpireDate) {
		this.passwordExpireDate = passwordExpireDate;
	}

	public int getIsFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setIsFirstTimeLogin(int isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
	}

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public String getA1() {
		return a1;
	}

	public void setA1(String a1) {
		this.a1 = a1;
	}

	public String getQ2() {
		return q2;
	}

	public void setQ2(String q2) {
		this.q2 = q2;
	}

	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}

	public String getQ3() {
		return q3;
	}

	public void setQ3(String q3) {
		this.q3 = q3;
	}

	public String getA3() {
		return a3;
	}

	public void setA3(String a3) {
		this.a3 = a3;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	protected String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	// 用于判断是OP操作还是Boss操作
	protected int operator;

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "StaffRole [staffID=" + staffID + ", roleID=" + roleID + ", salt=" + salt + ", phone=" + phone + ", name=" + name + ", IDInPOS=" + IDInPOS + ", POS_SN=" + POS_SN + ", weChat=" + weChat + ", openid=" + openid + ", unionid="
				+ unionid + ", ICID=" + ICID + ", passwordExpireDate=" + passwordExpireDate + ", isFirstTimeLogin=" + isFirstTimeLogin + ", q1=" + q1 + ", a1=" + a1 + ", q2=" + q2 + ", a2=" + a2 + ", q3=" + q3 + ", a3=" + a3 + ", shopID="
				+ shopID + ", departmentID=" + departmentID + ", status=" + status + ", operator=" + operator + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		StaffRole obj = new StaffRole();
		obj.setID(ID);
		obj.setName(name);
		obj.setPhone(phone);
		obj.setICID(ICID);
		obj.setWeChat(weChat);
		obj.setOpenid(openid);
		obj.setUnionid(unionid);
		obj.setSalt(salt);
		obj.setStaffID(staffID);
		obj.setRoleID(roleID);
		obj.setIDInPOS(IDInPOS);
		obj.setPOS_SN(POS_SN);
		obj.setIsFirstTimeLogin(isFirstTimeLogin);
		obj.setPasswordExpireDate(passwordExpireDate == null ? null : (Date) passwordExpireDate.clone());
		obj.setQ1(q1);
		obj.setA1(a1);
		obj.setQ2(q2);
		obj.setA2(a2);
		obj.setQ3(q3);
		obj.setA3(a3);
		obj.setShopID(shopID);
		obj.setDepartmentID(departmentID);
		obj.setStatus(status);
		obj.setIgnoreIDInComparision(ignoreIDInComparision);
		obj.setOperator(operator);

		return obj;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			ICID = jo.getString(field.getFIELD_NAME_ICID());
			IDInPOS = jo.getInt(field.getFIELD_NAME_IDInPOS());
			POS_SN = jo.getString(field.getFIELD_NAME_POS_SN());
			departmentID = jo.getInt(field.getFIELD_NAME_departmentID());
			isFirstTimeLogin = jo.getInt(field.getFIELD_NAME_isFirstTimeLogin());
			name = jo.getString(field.getFIELD_NAME_name());
			phone = jo.getString(field.getFIELD_NAME_phone());
			weChat = jo.getString(field.getFIELD_NAME_weChat());
			openid = jo.getString(field.getFIELD_NAME_openid());
			unionid = jo.getString(field.getFIELD_NAME_unionid());
			salt = jo.getString(field.getFIELD_NAME_salt());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			roleID = jo.getInt(field.getFIELD_NAME_roleID());
			roleName = jo.getString(field.getFIELD_NAME_roleName());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			status = jo.getInt(field.getFIELD_NAME_status());
			//
			String tmp = jo.getString(field.getFIELD_NAME_passwordExpireDate());
			SimpleDateFormat sDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			if (!"".equals(tmp)) {
				passwordExpireDate = sDateFormat.parse(tmp);
			}
			//

			tmp = jo.getString(field.getFIELD_NAME_createDatetime());
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
		List<BaseModel> staffRoleList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				StaffRole staffRole = new StaffRole();
				staffRole.doParse1(jsonObject);
				staffRoleList.add(staffRole);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return staffRoleList;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		StaffRole sr = (StaffRole) bm;
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_roleID(), sr.getRoleID());
			params.put(field.getFIELD_NAME_status(), sr.getStatus());
			params.put(field.getFIELD_NAME_operator(), sr.getOperator());
			params.put(field.getFIELD_NAME_iPageIndex(), sr.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), sr.getPageSize());
			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		StaffRole sr = (StaffRole) bm;
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_ID(), sr.getID());
			params.put(field.getFIELD_NAME_staffID(), sr.getStaffID());
			break;
		}

		return params;
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_roleID(), FIELD_ERROR_roleID, sbError) && roleID != BaseAction.INVALID_ID && !FieldFormat.checkID(roleID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == BaseAction.INVALID_STATUS //
				|| status == EnumStatusStaff.ESS_Incumbent.getIndex() || status == EnumStatusStaff.ESS_Resigned.getIndex())) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_operator(), FIELD_ERROR_operator, sbError) && !FieldFormat.checkBoolean(operator)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!(ID == 0 && staffID == 0)) {// ID和staffID不能同时为0
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && ID != 0 && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && staffID != 0 && !FieldFormat.checkID(staffID)) {
				return sbError.toString();
			}
		} else {
			return FIELD_ERROR_IDAndStaffID;
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}
}
