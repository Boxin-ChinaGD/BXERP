package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Staff extends BaseAuthenticationModel {
	private static final long serialVersionUID = -4511056097709074881L;
	public static final StaffField field = new StaffField();

	private static final int Zero = 0;
	private static final int MAX_LENGTH_Unionid = 100;
	private static final int MAX_LENGTH_Openid = 100;
	private static final int MAX_LENGTH_queryKeyword = 12;
	public static final String FIELD_ERROR_queryKeyword = "queryKeyword的长度为(" + Zero + ", " + MAX_LENGTH_queryKeyword + "]";
	public static final String FIELD_ERROR_unionid = "unionid的长度为[" + Zero + ", " + MAX_LENGTH_Unionid + "]";
	public static final String FIELD_ERROR_openid = "openid的长度为[" + Zero + ", " + MAX_LENGTH_Openid + "]";
	public static final String FIELD_ERROR_ID = "ID不存在";
	public static final String FIELD_ERROR_name = "门店用户的名字为中英文的组合且长度不能为" + Zero;
	public static final String FIELD_ERROR_IDorPhone = "ID和手机号必须要有一个是合法的";
	public static final String FIELD_ERROR_phone = "手机格式错误，应为" + FieldFormat.LENGTH_Mobile + "位的数字";
	public static final String FIELD_ERROR_ICID = "身份证号码格式不对";
	public static final String FIELD_ERROR_weChat = "门店用户的微信号必须是5-20位的数字和英文组成的";
	public static final String FIELD_ERROR_passwordExpireDate = "门店用户的密码有效期错误";
	public static final String FIELD_ERROR_checkUniqueField = "非法的值";
	public static final String FIELD_ERROR_samePassword = "新旧密码不能相同";
	public static final String FIELD_ERROR_isFirstTimeLogin = "是否首次登陆字段的值和期望的不一样";
	public static final String FIELD_ERROR_salt = "盐值应该为32个数字或大写字母的组合";
	public static final String FIELD_ERROR_shopID = "门店ID必须大于" + Zero;
	public static final String FIELD_ERROR_departmentID = "部门ID必须大于" + Zero;
	public static final String FIELD_ERROR_roleID = "角色ID必须是" + EnumTypeRole.ETR_Cashier.getIndex() + "或" + EnumTypeRole.ETR_Boss.getIndex() + "或" + EnumTypeRole.ETR_PreSale.getIndex();
	public static final String FIELD_ERROR_status = "店员的状态码只能是" + EnumStatusStaff.ESS_Incumbent.index + "或" + EnumStatusStaff.ESS_Resigned.index;
	public static final String FIELD_ERROR_operator = "操作者只能是" + EnumBoolean.EB_NO.getIndex() + "或" + EnumBoolean.EB_Yes.getIndex(); // 0代表老板操作，1代表OP操作

	public static final int INVOLVE_RESIGNED = 1;// 包含离职+在职的
	public static final int NOT_INVOLVE_RESIGNED = 0;// 不包含离职
	public static final String FIELD_ERROR_involvedResigned = "是否查询离职员工只能是" + INVOLVE_RESIGNED + "或" + NOT_INVOLVE_RESIGNED;
	public static final String FIELD_ERROR_RN_involvedResigned = "是否查询离职员工只能是" + INVOLVE_RESIGNED + "或" + NOT_INVOLVE_RESIGNED;
	public static final int FOR_ModifyPassword = 1;
	public static final int FOR_CreateNewStaff = 1;

	public static final int RETURN_SALT = 1;
	public static final int NOT_RETURN_SALT = 0;
	public static final String FIELD_ERROR_returnSalt = "是否返回盐值只能是" + RETURN_SALT + "或" + NOT_RETURN_SALT;

	public static final int CASE_CHECKPHONE = 1; // 检查手机号
	public static final int CASE_CHECKICID = 2; // 检查ICID
	public static final int CASE_CHECKWECHAT = 3; // 检查微信
	public static final String FIELD_ERROR_fieldToCheckUnique = "fieldToCheckUnique只能是" + CASE_CHECKICID + "或" + CASE_CHECKPHONE + "或" + CASE_CHECKWECHAT;

	protected String phone;

	protected String name;

	protected String weChat;

	protected String openid;

	protected String unionid;

	protected String ICID;

	protected Date passwordExpireDate;

	protected int isFirstTimeLogin;

	protected int shopID;

	protected int departmentID;

	protected int status;

	protected String oldPassword; // 非数据库字段

	protected int involvedResigned;//

	protected int forModifyPassword;

	protected int createNewStaff;

	protected int lastLocation;// 用于接收homeAtion里从前端 / 代表用户是首次登录跳转到本页面的/ /代表用户是从员工管理跳转到本页面的/ /代表用户是点击导航跳转到本页面的/
								// 的参数
	
	protected String shopName;

	public int getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(int lastLocation) {
		this.lastLocation = lastLocation;
	}

	public int getForModifyPassword() {
		return forModifyPassword;
	}

	public int getInvolvedResigned() {
		return involvedResigned;
	}

	public void setInvolvedResigned(int involvedResigned) {
		this.involvedResigned = involvedResigned;
	}

	public void setForModifyPassword(int forModifyPassword) {
		this.forModifyPassword = forModifyPassword;
	}

	public int getCreateNewStaff() {
		return createNewStaff;
	}

	public void setCreateNewStaff(int createNewStaff) {
		this.createNewStaff = createNewStaff;
	}

	protected int isLoginFromPos;// isLoginFromPos==int3

	public int getIsLoginFromPos() {
		return isLoginFromPos;
	}

	public void setIsLoginFromPos(int isLoginFromPos) {
		this.isLoginFromPos = isLoginFromPos;
	}

	/** 非DB字段。1=重置他人密码。0=修改自己的密码 */
	protected int isResetOtherPassword;

	public int getIsResetOtherPassword() {
		return isResetOtherPassword;
	}

	public void setIsResetOtherPassword(int isResetOtherPassword) {
		this.isResetOtherPassword = isResetOtherPassword;
	}

	protected int roleID;// int1==roleID

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	protected String newPassword; // 非数据库字段

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	protected int companyID; // 非数据库字段，用于RN时接收SP返回的字段

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	protected String confirmNewPassword; // 非数据库字段

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	protected String resetNewPassword; // 非数据库字段

	public String getResetNewPassword() {
		return resetNewPassword;
	}

	protected String confirmResetNewPassword; // 非数据库字段

	public String getConfirmResetNewPassword() {
		return confirmResetNewPassword;
	}

	protected String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	protected String oldMD5;

	public String getOldMD5() {
		return oldMD5;
	}

	public void setOldMD5(String oldMD5) {
		this.oldMD5 = oldMD5;
	}

	protected String newMD5;

	public String getNewMD5() {
		return newMD5;
	}

	public void setNewMD5(String newMD5) {
		this.newMD5 = newMD5;
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
	public String getKey() {
		return phone;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	@Override
	public String toString() {
		return "Staff [phone=" + phone + ", name=" + name + ", weChat=" + weChat + ", openid=" + openid + ", unionid=" + unionid + ", ICID=" + ICID + ", passwordExpireDate=" + passwordExpireDate + ", isFirstTimeLogin=" + isFirstTimeLogin
				+ ", shopID=" + shopID + ", departmentID=" + departmentID + ", status=" + status + ", oldPassword=" + oldPassword + ", involvedResigned=" + involvedResigned + ", forModifyPassword=" + forModifyPassword + ", createNewStaff="
				+ createNewStaff + ", lastLocation=" + lastLocation + ", shopName=" + shopName + ", isLoginFromPos=" + isLoginFromPos + ", isResetOtherPassword=" + isResetOtherPassword + ", roleID=" + roleID + ", newPassword=" + newPassword
				+ ", companyID=" + companyID + ", confirmNewPassword=" + confirmNewPassword + ", resetNewPassword=" + resetNewPassword + ", confirmResetNewPassword=" + confirmResetNewPassword + ", roleName=" + roleName + ", oldMD5="
				+ oldMD5 + ", newMD5=" + newMD5 + ", operator=" + operator + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Staff s = (Staff) arg0;
		if ((ignoreIDInComparision == true ? true : (s.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& s.getPhone().equals(phone) && printComparator(field.getFIELD_NAME_phone())//
				&& (s.getWeChat() == null ? weChat == null : s.getWeChat().equals(weChat == null ? "" : weChat) && printComparator(field.getFIELD_NAME_weChat()))//
				// && s.getOpenid().equals(this.getOpenid()) &&
				// printComparator(getFIELD_NAME_openid())//
				// && s.getUnionid().equals(this.getUnionid()) &&
				// printComparator(getFIELD_NAME_unionid())//
				&& (s.getICID() == null ? ICID == null : s.getICID().equals(ICID == null ? "" : ICID) && printComparator(field.getFIELD_NAME_ICID()))//
				// && 0 == (s.getPasswordExpireDate().compareTo(this.getPasswordExpireDate()))
				// && printComparator("PasswordExpireDate")
				&& s.getIsFirstTimeLogin() == isFirstTimeLogin && printComparator(field.getFIELD_NAME_isFirstTimeLogin())//
				&& s.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID())//
				&& s.getDepartmentID() == departmentID && printComparator(field.getFIELD_NAME_departmentID())//
				&& s.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
				&& s.getOperator() == operator && printComparator(field.getFIELD_NAME_operator())//
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Staff obj = new Staff();
		obj.setID(ID);
		obj.setName(name);
		obj.setPhone(phone);
		obj.setWeChat(weChat);
		obj.setOpenid(openid);
		obj.setUnionid(unionid);
		obj.setICID(ICID);
		obj.setSalt(salt);
		obj.setIsFirstTimeLogin(isFirstTimeLogin);
		obj.setPasswordExpireDate(passwordExpireDate == null ? null : (Date) passwordExpireDate.clone());
		obj.setShopID(shopID);
		obj.setDepartmentID(departmentID);
		obj.setStatus(status);
		obj.setRoleID(roleID);
		obj.setIgnoreIDInComparision(ignoreIDInComparision);
		obj.setCompanySN(companySN);
		obj.setIsLoginFromPos(isLoginFromPos);
		obj.setForModifyPassword(forModifyPassword);
		obj.setCreateNewStaff(createNewStaff);
		obj.setInvolvedResigned(involvedResigned);
		obj.setOperator(operator);
		obj.setShopName(shopName);

		// obj.setString3(string3);

		return obj;
	}

	// int2标识是否需要返回salt
	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		Staff s = (Staff) bm;
		switch (iUseCaseID) {
		case BaseBO.INVALID_CASE_ID:
		case BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount:
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone());
			params.put(field.getFIELD_NAME_name(), s.getName() == null ? "" : s.getName());
			params.put(field.getFIELD_NAME_ICID(), s.getICID() == "" ? null : s.getICID());
			params.put(field.getFIELD_NAME_weChat(), s.getWeChat() == "" ? null : s.getWeChat());
			params.put(field.getFIELD_NAME_salt(), s.getSalt() == null ? "" : s.getSalt());
			params.put(field.getFIELD_NAME_passwordExpireDate(), s.getPasswordExpireDate());
			params.put(field.getFIELD_NAME_isFirstTimeLogin(), s.getIsFirstTimeLogin());
			params.put(field.getFIELD_NAME_shopID(), s.getShopID());
			params.put(field.getFIELD_NAME_departmentID(), s.getDepartmentID());
			params.put(field.getFIELD_NAME_roleID(), s.getRoleID());
			params.put(field.getFIELD_NAME_status(), s.getStatus());
			params.put(field.getFIELD_NAME_returnSalt(), s.getReturnSalt());

			return params;
		default:
			throw new RuntimeException("未定义的CASE！");
		}
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Staff s = (Staff) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), s.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), s.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_uniqueField(), s.getUniqueField());

			break;
		default:
			params.put(field.getFIELD_NAME_queryKeyword(), s.getQueryKeyword());
			params.put(field.getFIELD_NAME_status(), s.getStatus());
			params.put(field.getFIELD_NAME_operator(), s.getOperator());
			params.put(field.getFIELD_NAME_iPageIndex(), s.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), s.getPageSize());

			break;
		}

		return params;
	}

	// int2标识是否需要返回salt
	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Staff s = (Staff) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_returnSalt(), s.getReturnSalt());
		switch (iUseCaseID) {
		case BaseBO.CASE_ResetMyPassword:
			params.put(field.getFIELD_NAME_salt(), s.getSalt() == null ? "" : s.getSalt());
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone());
			params.put(field.getFIELD_NAME_isFirstTimeLogin(), s.getIsFirstTimeLogin());
			break;
		case BaseBO.CASE_ResetOtherPassword:
			params.put(field.getFIELD_NAME_salt(), s.getSalt() == null ? "" : s.getSalt());
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone());
			params.put(field.getFIELD_NAME_isFirstTimeLogin(), s.getIsFirstTimeLogin());
			break;
		case BaseBO.CASE_Staff_Update_OpenidAndUnionid:
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone());
			params.put(field.getFIELD_NAME_openid(), s.getOpenid() == null ? "" : s.getOpenid());
			params.put(field.getFIELD_NAME_unionid(), s.getUnionid() == null ? "" : s.getUnionid());
			break;
		case BaseBO.CASE_Staff_Update_Unsubscribe:
			params.put(field.getFIELD_NAME_ID(), s.getID());
			break;
		default:
			params.put(field.getFIELD_NAME_ID(), s.getID());
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone()); // ...== null ?
			params.put(field.getFIELD_NAME_name(), s.getName() == null ? "" : s.getName()); // ...== null ?
			params.put(field.getFIELD_NAME_ICID(), s.getICID() == "" ? null : s.getICID()); // ...== null ?
			params.put(field.getFIELD_NAME_weChat(), s.getWeChat() == "" ? null : s.getWeChat()); // ...== null ?
			params.put(field.getFIELD_NAME_passwordExpireDate(), s.getPasswordExpireDate());
			params.put(field.getFIELD_NAME_shopID(), s.getShopID());
			params.put(field.getFIELD_NAME_departmentID(), s.getDepartmentID());
			params.put(field.getFIELD_NAME_roleID(), s.getRoleID()); // RoleID
			params.put(field.getFIELD_NAME_status(), s.getStatus());
			params.put(field.getFIELD_NAME_isLoginFromPos(), s.getIsLoginFromPos());

			break;
		}

		return params;
	}

	// @Override
	// public Map<String, Object> getUpdateParamEx(int iUseCaseID, final BaseModel
	// bm) {
	// checkParameterInput(bm);
	//
	// Staff s = (Staff) bm;
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (iUseCaseID) {
	// default:
	// params.put("F_ID", s.getID());
	// params.put("F_Phone", s.getPhone() == null ? "" : s.getPhone());
	// params.put("F_Name", s.getName() == null ? "" : s.getName());
	// params.put("F_ICID", s.getICID());
	// params.put("F_WeChat", s.getWeChat());
	// params.put("F_PasswordExpireDate", s.getPasswordExpireDate());
	// params.put("F_Q1", s.getQ1());
	// params.put("F_A1", s.getA1());
	// params.put("F_Q2", s.getQ2());
	// params.put("F_A2", s.getA2());
	// params.put("F_Q3", s.getQ3());
	// params.put("F_A3", s.getA3());
	// params.put("F_ShopID", s.getShopID() == 0 ? null : s.getShopID());
	// params.put("F_DepartmentID", s.getDepartmentID() == 0 ? null :
	// s.getDepartmentID());
	// params.put("iRoleID", s.getInt1());
	// break;
	// }
	//
	// return params;
	// }

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	// int2标识是否需要返回salt
	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Staff s = (Staff) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		// case BaseBO.CASE_Login:
		// params.put("F_Phone", (s.getPhone() == null) ? "" : s.getPhone());
		// break;
		default:
			params.put(field.getFIELD_NAME_ID(), s.getID());
			params.put(field.getFIELD_NAME_phone(), s.getPhone() == null ? "" : s.getPhone());
			params.put(field.getFIELD_NAME_involvedResigned(), s.getInvolvedResigned());
			params.put(field.getFIELD_NAME_returnSalt(), s.getReturnSalt());
			break;

		}

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		// ...以下代码待简化
		case BaseBO.CASE_SpecialResultVerification:
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(ICID) && !FieldFormat.checkICID(ICID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(weChat) && !FieldFormat.checkWeChat(weChat)) {// 接受空值
				return sbError.toString();
			}
			//
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			try {
				passwordExpireDate = sdf.parse("9999/12/30 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// if (printCheckField(field.getFIELD_NAME_passwordExpireDate(),
			// FIELD_ERROR_passwordExpireDate, sbError) && passwordExpireDate != null) {
			// SimpleDateFormat sdf = new
			// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			// if (!FieldFormat.checkDate(sdf.format(passwordExpireDate))) {
			// return sbError.toString();
			// }
			// }
			//
			// if(printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_salt, sbError) &&
			// !FieldFormat.checkSalt(salt)) {
			// return sbError.toString();
			// }
			//
			if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && !(isFirstTimeLogin == EnumBoolean.EB_Yes.getIndex() || isFirstTimeLogin == EnumBoolean.EB_NO.getIndex())) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkShopID(shopID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_departmentID(), FIELD_ERROR_departmentID, sbError) && !FieldFormat.checkID(departmentID)) {
				return sbError.toString();
			}
			//
			// if(printCheckField(field.getFIELD_NAME_int1(), FIELD_ERROR_roleID, sbError)
			// && !FieldFormat.checkID(int1)) {
			// return sbError.toString();
			// }
			//
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusStaff.ESS_Incumbent.index || status == EnumStatusStaff.ESS_Resigned.index)) {
				return sbError.toString();
			}
			//
			// if(printCheckField(field.getFIELD_NAME_int2(), FIELD_ERROR_returnSalt,
			// sbError) && !(int2 == RETURN_SALT || int2 == NOT_RETURN_SALT)) {
			// return sbError.toString();
			// }
			//
			return "";
		default:
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(ICID) && !FieldFormat.checkICID(ICID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(weChat) && !FieldFormat.checkWeChat(weChat)) {// 接受空值
				return sbError.toString();
			}
			//
			SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			try {
				passwordExpireDate = sdf1.parse("9999/12/30 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// if (printCheckField(field.getFIELD_NAME_passwordExpireDate(),
			// FIELD_ERROR_passwordExpireDate, sbError) && passwordExpireDate != null) {
			// SimpleDateFormat sdf = new
			// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			// if (!FieldFormat.checkDate(sdf.format(passwordExpireDate))) {
			// return sbError.toString();
			// }
			// }
			//
			if (printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_salt, sbError) && !FieldFormat.checkSalt(salt)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && !(isFirstTimeLogin == EnumBoolean.EB_Yes.getIndex() || isFirstTimeLogin == EnumBoolean.EB_NO.getIndex())) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_departmentID(), FIELD_ERROR_departmentID, sbError) && !FieldFormat.checkID(departmentID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_roleID(), FIELD_ERROR_roleID, sbError) && !FieldFormat.checkRoleIDToCreateStaff(roleID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusStaff.ESS_Incumbent.index || status == EnumStatusStaff.ESS_Resigned.index)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_returnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
				return sbError.toString();
			}
			//
			return "";
		}

		// if (printCheckField(this.getFIELD_NAME_salt(), "盐值应该为32个数字或大写字母的组合", sbError)
		// && FieldFormat.checkSalt(this.getSalt())) {
		// // ...将来还需要检查其它字段
		// return "";
		// }
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_Staff_Update_OpenidAndUnionid:
			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {//
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_unionid(), FIELD_ERROR_unionid, sbError) && !StringUtils.isEmpty(unionid) && unionid.length() > MAX_LENGTH_Unionid) {//
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_openid(), FIELD_ERROR_openid, sbError) && !StringUtils.isEmpty(openid) && openid.length() > MAX_LENGTH_Openid) {//
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_returnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
				return sbError.toString();
			}

			break;
		case BaseBO.CASE_ResetOtherPassword:
			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) { //
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && isFirstTimeLogin != EnumBoolean.EB_Yes.getIndex()) {//
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_returnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
				return sbError.toString();
			}
			break;
		case BaseBO.CASE_ResetMyPassword:
			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) { //
				return sbError.toString();
			}

			// String1是旧密码，String2是新密码，这两个字段不能相同
			if (!StringUtils.isEmpty(oldMD5) && !StringUtils.isEmpty(newMD5)) {
				if (oldMD5.equals(newMD5)) {
					return FIELD_ERROR_samePassword;
				}
			}
			if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && isFirstTimeLogin != EnumBoolean.EB_NO.getIndex()) {//
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_returnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
				return sbError.toString();
			}
			break;
		case BaseBO.CASE_Staff_Update_Unsubscribe:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			break;
		// ...以下代码待简化
		default:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(ICID) && !FieldFormat.checkICID(ICID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(weChat) && !FieldFormat.checkWeChat(weChat)) {// 接受空值
				return sbError.toString();
			}
			//
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			try {
				passwordExpireDate = sdf.parse("9999/12/30 23:59:59");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// if (printCheckField(field.getFIELD_NAME_passwordExpireDate(),
			// FIELD_ERROR_passwordExpireDate, sbError) && passwordExpireDate != null) {
			// SimpleDateFormat sdf = new
			// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			// if (!FieldFormat.checkDate(sdf.format(passwordExpireDate))) {
			// return sbError.toString();
			// }
			// }
			//
			if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_departmentID(), FIELD_ERROR_departmentID, sbError) && !FieldFormat.checkID(departmentID)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_isLoginFromPos(), FIELD_ERROR_returnSalt, sbError) && !(isLoginFromPos == RETURN_SALT || isLoginFromPos == NOT_RETURN_SALT)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusStaff.ESS_Incumbent.index || status == EnumStatusStaff.ESS_Resigned.index)) {
				return sbError.toString();
			}
			//
			if (printCheckField(field.getFIELD_NAME_roleID(), FIELD_ERROR_roleID, sbError) && !FieldFormat.checkRoleIDToCreateStaff(roleID)) {
				return sbError.toString();
			}
			//
			break;
		}
		return "";

	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_Login:
		default:
			// if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID,
			// sbError) && ID != 0 && !FieldFormat.checkID(ID)) { //允许ID为0时去查询店员
			// return sbError.toString();
			// }
			// if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError)
			// && !StringUtils.isEmpty(phone) && !FieldFormat.checkPhone(phone)) {
			// //允许手机号为空时去查询店员
			// return sbError.toString();
			// }
			if (ID <= 0 && !FieldFormat.checkMobile(phone == null ? "" : phone)) {
				return FIELD_ERROR_IDorPhone;
			}
			if (printCheckField(field.getFIELD_NAME_involvedResigned(), FIELD_ERROR_involvedResigned, sbError) && !(involvedResigned == INVOLVE_RESIGNED || involvedResigned == NOT_INVOLVE_RESIGNED)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_returnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && ID != 0 && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_fieldToCheckUnique(), FIELD_ERROR_fieldToCheckUnique, sbError)
					&& !(fieldToCheckUnique == CASE_CHECKICID || fieldToCheckUnique == CASE_CHECKPHONE || fieldToCheckUnique == CASE_CHECKWECHAT)) {
				return sbError.toString();
			}
			switch (fieldToCheckUnique) {
			case CASE_CHECKPHONE:
				if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && StringUtils.isEmpty(uniqueField) || !FieldFormat.checkMobile(uniqueField)) {
					return sbError.toString();
				}
				return "";
			case CASE_CHECKICID:
				if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(uniqueField) && !FieldFormat.checkICID(uniqueField)) {
					return sbError.toString();
				}
				return "";
			case CASE_CHECKWECHAT:
				if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(uniqueField) && !FieldFormat.checkWeChat(uniqueField)) {
					return sbError.toString();
				}
				return "";
			default:
				return FIELD_ERROR_checkUniqueField;
			}
		default:
			if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && !StringUtils.isEmpty(queryKeyword) && queryKeyword.length() > MAX_LENGTH_queryKeyword) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_involvedResigned(), FIELD_ERROR_RN_involvedResigned, sbError) && !(involvedResigned == INVOLVE_RESIGNED || involvedResigned == NOT_INVOLVE_RESIGNED)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_operator(), FIELD_ERROR_operator, sbError) && !FieldFormat.checkBoolean(operator)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}

	public enum EnumStatusStaff {
		ESS_Incumbent("Incumbent", 0), //
		ESS_Resigned("Resigned", 1);

		private String name;
		private int index;

		private EnumStatusStaff(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusStaff c : EnumStatusStaff.values()) {
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
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			phone = jo.getString(field.getFIELD_NAME_phone());
			ICID = jo.getString(field.getFIELD_NAME_ICID());
			weChat = jo.getString(field.getFIELD_NAME_weChat());
			openid = jo.getString(field.getFIELD_NAME_openid());
			unionid = jo.getString(field.getFIELD_NAME_unionid());
			pwdEncrypted = jo.getString(field.getFIELD_NAME_pwdEncrypted());
			salt = jo.getString(field.getFIELD_NAME_salt());

			String tmp = jo.getString(field.getFIELD_NAME_passwordExpireDate());
			SimpleDateFormat sDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			if (!"".equals(tmp)) {
				passwordExpireDate = sDateFormat.parse(tmp);
			}

			isFirstTimeLogin = jo.getInt(field.getFIELD_NAME_isFirstTimeLogin());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			departmentID = jo.getInt(field.getFIELD_NAME_departmentID());
			status = jo.getInt(field.getFIELD_NAME_status());

			tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp)) {
				createDatetime = sDateFormat.parse(tmp);
			}
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = sDateFormat.parse(tmp);
			}

			roleID = jo.getInt(field.getFIELD_NAME_roleID());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> staffList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Staff staff = new Staff();
				staff.doParse1(jsonObject);
				staffList.add(staff);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return staffList;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_StaffCacheSize.getIndex();
	}

	/** 清除敏感信息。一般用来向请求者返回数据前设置 */
	public void clearSensitiveInfo() {
		salt = null;
		passwordInPOS = null;
		newPassword = null;
		oldPassword = null;
		newMD5 = null;
		oldMD5 = null;
		pwdEncrypted = null;
		openid = null;
		unionid = null;
	}
}
