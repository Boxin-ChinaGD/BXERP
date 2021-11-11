package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.MD5Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Company extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final int LENGTH_BusinessLicenseSN1 = 15;
	public static final int LENGTH_BusinessLicenseSN2 = 18;
	public static final int MAX_LENGTH_BusinessLicensePicture = 128;
	public static final int MAX_LENGTH_Logo = 128;

	public static final int MIN_LENGTH_Name = 1;
	public static final int MAX_LENGTH_Name = 12;
	public static final int MAX_LENGTH_brandName = 20;

	public static final String FIELD_ERROR_status = "创建公司时公司的状态必须为0";
	public static final String FIELD_ERROR_checkUniqueField = "非法的值";
	public static final String FIELD_ERROR_businessLicenseSN = "中国的营业执照全部是" + LENGTH_BusinessLicenseSN1 + "位或" + LENGTH_BusinessLicenseSN2 + "位，由纯数字或纯大写字母或两者混合组成，没有空格";
	public static final String FIELD_ERROR_logo = "logo路径长度不能超过" + MAX_LENGTH_Logo;
	public static final String FIELD_ERROR_businessLicensePicture = "营业执照路径长度不能超过" + MAX_LENGTH_BusinessLicensePicture;
	public static final String FIELD_ERROR_bossPhone = "老板的手机号码格式不正确";
	public static final String FIELD_ERROR_key = "key值格式错误";
	public static final String FIELD_ERROR_name = "公司名称格式应为：只允许中英文，" + MIN_LENGTH_Name + "到" + MAX_LENGTH_Name + "个字符";
	public static final String FIELD_ERROR_DBUserName = "数据库用户名格式应为：数字、字母和下划线的组合，但首字符必须是字母，不能出现空格";
	public static final String FIELD_ERROR_submchid = "子商户号是null或者空串或者长度为10位的数字";
	public static final String FIELD_ERROR_brandName = "商家招牌不能为null或空串，长度不能大于" + MAX_LENGTH_brandName;
	public static final String FIELD_ERROR_vipName = "首次登录，必须提供合法的会员名称！";

	public static final CompanyField field = new CompanyField();

	protected String dbUserName;

	protected String dbUserPassword;
	protected String businessLicenseSN;
	protected String SN;
	protected String name;
	protected String bossName;
	protected String businessLicensePicture;
	protected String bossPhone;
	protected String bossPassword;
	protected String bossWechat;
	protected String dbName;
	protected String key;
	protected int status;
	protected String submchid;
	protected String brandName;
	protected Date expireDatetime;
	protected int showVipSystemTip;
	protected String logo;
	/** 非数据库表的字段 */
	protected int whetherRetrieveAll;
	protected int incumbent;
	protected int cacheID;
	// 下面3个字段是用来匹配所有私有DB中是否存在这样的Vip的，用于小程序里面确定一个第2次登录的Vip的合法身份
	/** Vip来源类型：微信、支付宝或其它平台 */
	protected int sourceCode;
	/** Vip的手机号码 */
	protected String mobile;
	/** Vip的miniprogram的openid */
	protected String openID;
	/** Vip的miniprogram的unionid */
	protected String unionID;
	/** vip的昵称 */
	protected String vipName;
	/** vip的性别 */
	protected int sex;

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getUnionID() {
		return unionID;
	}

	public void setUnionID(String unionID) {
		this.unionID = unionID;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getCacheID() {
		return cacheID;
	}

	public void setCacheID(int cacheID) {
		this.cacheID = cacheID;
	}

	public int getIncumbent() {
		return incumbent;
	}

	public void setIncumbent(int incumbent) {
		this.incumbent = incumbent;
	}

	public int getWhetherRetrieveAll() {
		return whetherRetrieveAll;
	}

	public void setWhetherRetrieveAll(int whetherRetrieveAll) {
		this.whetherRetrieveAll = whetherRetrieveAll;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public Date getExpireDatetime() {
		return expireDatetime;
	}

	public void setExpireDatetime(Date expireDatetime) {
		this.expireDatetime = expireDatetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBusinessLicenseSN() {
		return businessLicenseSN;
	}

	public void setBusinessLicenseSN(String businessLicenseSN) {
		this.businessLicenseSN = businessLicenseSN;
	}

	public String getBusinessLicensePicture() {
		return businessLicensePicture;
	}

	public void setBusinessLicensePicture(String businessLicensePicture) {
		this.businessLicensePicture = businessLicensePicture;
	}

	public String getBossPhone() {
		return bossPhone;
	}

	public void setBossPhone(String bossPhone) {
		this.bossPhone = bossPhone;
	}

	public String getBossWechat() {
		return bossWechat;
	}

	public void setBossWechat(String bossWechat) {
		this.bossWechat = bossWechat;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserPassword() {
		return dbUserPassword;
	}

	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}

	public String getBossPassword() {
		return bossPassword;
	}

	public void setBossPassword(String bossPassword) {
		this.bossPassword = bossPassword;
	}

	public String getSubmchid() {
		return submchid;
	}

	public void setSubmchid(String submchid) {
		this.submchid = submchid;
	}

	protected String cacheType;

	public String getCacheType() {
		return cacheType;
	}

	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	public int getShowVipSystemTip() {
		return showVipSystemTip;
	}

	public void setShowVipSystemTip(int showVipSystemTip) {
		this.showVipSystemTip = showVipSystemTip;
	}

	public int getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(int sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public static final int CASE_CheckName = 1; // 检查公司名称
	public static final int CASE_CheckBusinessLicenseSN = 2; // 检查营业执照
	// 公司营业执照照片和Key现在不由前端传进来
	// private final int CASE_CheckBusinessLicensePicture = 3; // 检查营业执照的照片
	// private final int CASE_CheckKey = 4; // 检查key
	public static final int CASE_CheckDbName = 5; // 检查DB名称
	public static final int CASE_CheckSubmchid = 6; // 检查子商户号

	@Override
	public String toString() {
		return "Company [dbUserName=" + dbUserName + ", dbUserPassword=" + dbUserPassword + ", businessLicenseSN=" + businessLicenseSN + ", SN=" + SN + ", name=" + name + ", bossName=" + bossName + ", businessLicensePicture="
				+ businessLicensePicture + ", bossPhone=" + bossPhone + ", bossPassword=" + bossPassword + ", bossWechat=" + bossWechat + ", dbName=" + dbName + ", key=" + key + ", status=" + status + ", submchid=" + submchid
				+ ", brandName=" + brandName + ", expireDatetime=" + expireDatetime + ", showVipSystemTip=" + showVipSystemTip + ", logo=" + logo + ", whetherRetrieveAll=" + whetherRetrieveAll + ", incumbent=" + incumbent + ", cacheID="
				+ cacheID + ", sourceCode=" + sourceCode + ", mobile=" + mobile + ", openID=" + openID + ", unionID=" + unionID + ", vipName=" + vipName + ", sex=" + sex + ", cacheType=" + cacheType + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Company c = new Company();
		c.setID(ID);
		c.setSN(SN);
		c.setBusinessLicenseSN(businessLicenseSN);
		c.setBusinessLicensePicture(businessLicensePicture);
		c.setBossPhone(bossPhone);
		c.setBossPassword(bossPassword);
		c.setBossWechat(bossWechat);
		c.setDbName(dbName);
		c.setKey(key);
		c.setStatus(status);
		c.setName(name);
		c.setBossName(bossName);
		c.setDbUserName(dbUserName);
		c.setDbUserPassword(dbUserPassword);
		c.setSubmchid(submchid);
		c.setFieldToCheckUnique(fieldToCheckUnique);
		c.setIncumbent(incumbent);
		c.setBrandName(brandName);
		c.setShowVipSystemTip(showVipSystemTip);
		c.setLogo(logo);
		return c;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			dbUserName = jo.getString(field.getFIELD_NAME_dbUserName());
			dbUserPassword = jo.getString(field.getFIELD_NAME_dbUserPassword());
			businessLicenseSN = jo.getString(field.getFIELD_NAME_businessLicenseSN());
			SN = jo.getString(field.getFIELD_NAME_SN());
			name = jo.getString(field.getFIELD_NAME_name());
			bossName = jo.getString(field.getFIELD_NAME_bossName());
			businessLicensePicture = jo.getString(field.getFIELD_NAME_businessLicensePicture());
			bossPhone = jo.getString(field.getFIELD_NAME_bossPhone());
			bossPassword = jo.getString(field.getFIELD_NAME_bossPassword());
			bossWechat = jo.getString(field.getFIELD_NAME_bossWechat());
			dbName = jo.getString(field.getFIELD_NAME_dbName());
			key = jo.getString(field.getFIELD_NAME_key());
			status = jo.getInt(field.getFIELD_NAME_status());
			submchid = jo.getString(field.getFIELD_NAME_submchid());
			brandName = jo.getString(field.getFIELD_NAME_brandName());
			showVipSystemTip = jo.getInt(field.getFIELD_NAME_showVipSystemTip());
			logo = jo.getString(field.getFIELD_NAME_logo());
			String tmp = jo.getString(field.getFIELD_NAME_expireDatetime());
			SimpleDateFormat sDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			if (!"".equals(tmp)) {
				expireDatetime = sDateFormat.parse(tmp);
			}
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
	public int compareTo(BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		Company c = (Company) arg0;
		if ((ignoreIDInComparision == true ? true : c.getID() == ID && printComparator(field.getFIELD_NAME_ID()))//
				&& c.getBusinessLicenseSN().equals(businessLicenseSN) && printComparator(field.getFIELD_NAME_businessLicenseSN()) //
				&& (c.getBusinessLicensePicture() == null ? businessLicensePicture == null : c.getBusinessLicensePicture().equals(businessLicensePicture == null ? "" : businessLicensePicture)) //
				&& printComparator(field.getFIELD_NAME_businessLicensePicture()) //
				&& c.getBossPhone().equals(bossPhone) && printComparator(field.getFIELD_NAME_bossPhone()) //
				&& c.getBossPassword().equals(bossPassword) && printComparator(field.getFIELD_NAME_bossPassword()) //
				&& c.getBossWechat().equals(bossWechat) && printComparator(field.getFIELD_NAME_bossWechat()) //
				&& c.getDbName().equals(dbName) && printComparator(field.getFIELD_NAME_dbName()) //
				// 在UI界面 update两个公司，key 传不过来 ，都为null 重复
				// && c.getKey().equals(key) && printComparator(field.getFIELD_NAME_key()) //
				&& c.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
				&& c.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& c.getBossName().equals(bossName) && printComparator(field.getFIELD_NAME_bossName()) //
				&& c.getDbUserName().equals(dbUserName) && printComparator(field.getFIELD_NAME_dbUserName()) //
				&& c.getDbUserPassword().equals(dbUserPassword) && printComparator(field.getFIELD_NAME_dbUserPassword()) //
				&& (c.getSubmchid() == null ? (submchid == null) : c.getSubmchid().equals(submchid == null ? "" : submchid) && printComparator(field.getFIELD_NAME_submchid())) //
				&& (c.getBrandName().equals(brandName) && printComparator(field.getFIELD_NAME_brandName())) //
				&& (c.getLogo() == null ? logo == null : c.getLogo().equals(logo == null ? "" : logo)) //

		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Company c = (Company) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_businessLicenseSN(), c.getBusinessLicenseSN());
		params.put(field.getFIELD_NAME_businessLicensePicture(), c.getBusinessLicensePicture());
		params.put(field.getFIELD_NAME_bossPhone(), c.getBossPhone());
		params.put(field.getFIELD_NAME_bossPassword(), c.getBossPassword());
		params.put(field.getFIELD_NAME_bossWechat(), c.getBossWechat());
		params.put(field.getFIELD_NAME_dbName(), c.getDbName());
		params.put(field.getFIELD_NAME_key(), c.getKey());
		params.put(field.getFIELD_NAME_name(), c.getName());
		params.put(field.getFIELD_NAME_bossName(), c.getBossName());
		params.put(field.getFIELD_NAME_dbUserName(), c.getDbUserName());
		params.put(field.getFIELD_NAME_dbUserPassword(), c.getDbUserPassword());
		params.put(field.getFIELD_NAME_submchid(), c.getSubmchid());
		params.put(field.getFIELD_NAME_brandName(), c.getBrandName());
		params.put(field.getFIELD_NAME_logo(), c.getLogo());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		switch (iUseCaseID) {
		default:
			return super.getRetrieve1Param(iUseCaseID, bm);
		}
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Company c = (Company) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), c.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), c.getFieldToCheckUnique()); //
			params.put(field.getFIELD_NAME_uniqueField(), c.getUniqueField());

			break;
		case BaseBO.CASE_Company_retrieveNByVipMobile:
			params.put(field.getFIELD_NAME_queryKeyword(), c.getQueryKeyword()); // 传VIP手机号
			params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());
			break;
		default:
			params.put(field.getFIELD_NAME_status(), c.getStatus());
			params.put(field.getFIELD_NAME_SN(), c.getSN() == null ? "" : c.getSN());
			params.put(field.getFIELD_NAME_iPageIndex(), c.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), c.getPageSize());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Company c = (Company) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_matchVip:
			params.put(field.getFIELD_NAME_sourceCode(), c.getSourceCode());
			params.put(field.getFIELD_NAME_mobile(), c.getMobile());
			params.put(field.getFIELD_NAME_openID(), (c.getOpenID() == null ? "" : c.getOpenID()));
			params.put(field.getFIELD_NAME_unionID(), (c.getUnionID() == null ? "" : c.getUnionID()));
			params.put(field.getFIELD_NAME_vipName(), c.getVipName());
			params.put(field.getFIELD_NAME_sex(), c.getSex());
			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Company c = (Company) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(field.getFIELD_NAME_ID(), c.getID());
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_UpdateSubmchid:
			params.put(field.getFIELD_NAME_submchid(), c.getSubmchid());
			break;
		case BaseBO.CASE_Company_updateVipSystemTip:
			break;
		default:
			params.put(field.getFIELD_NAME_businessLicenseSN(), c.getBusinessLicenseSN());
			params.put(field.getFIELD_NAME_businessLicensePicture(), c.getBusinessLicensePicture());
			params.put(field.getFIELD_NAME_bossPhone(), c.getBossPhone());
			params.put(field.getFIELD_NAME_bossWechat(), c.getBossWechat());
			params.put(field.getFIELD_NAME_key(), c.getKey());
			params.put(field.getFIELD_NAME_name(), c.getName());
			params.put(field.getFIELD_NAME_bossName(), c.getBossName());
			params.put(field.getFIELD_NAME_dbName(), c.getDbName());
			params.put(field.getFIELD_NAME_brandName(), c.getBrandName());
			params.put(field.getFIELD_NAME_logo(), c.getLogo());
			break;
		}
		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_UpdateSubmchid:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && (!FieldFormat.checkID(ID))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
				return sbError.toString();
			}

			return "";
		case BaseBO.CASE_Company_updateVipSystemTip:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && (!FieldFormat.checkID(ID))) {
				return sbError.toString();
			}

			return "";
		default:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && (!FieldFormat.checkID(ID))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(businessLicenseSN))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_businessLicensePicture(), FIELD_ERROR_businessLicensePicture, sbError) && businessLicensePicture != null && businessLicensePicture.length() > MAX_LENGTH_BusinessLicensePicture) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_logo(), FIELD_ERROR_logo, sbError) && logo != null && logo.length() > MAX_LENGTH_Logo) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossPhone(), FIELD_ERROR_bossPhone, sbError) && !FieldFormat.checkMobile(bossPhone)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossWechat(), FieldFormat.FIELD_ERROR_wechat, sbError) && (!StringUtils.isEmpty(bossWechat) && !FieldFormat.checkWeChat(bossWechat))) {// 接受空值
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(name)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossName(), FieldFormat.FIELD_ERROR_HumanName, sbError) && !FieldFormat.checkHumanName(bossName)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_brandName(), FIELD_ERROR_brandName, sbError) && (StringUtils.isEmpty(brandName) || brandName.length() > MAX_LENGTH_brandName)) {
				return sbError.toString();
			}
			return "";
		}
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_SpecialResultVerification:
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && status != 0) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(businessLicenseSN))) {
				return sbError.toString();
			}
			if (businessLicensePicture != null) {
				System.out.println(businessLicensePicture.length());
				if (printCheckField(field.getFIELD_NAME_businessLicensePicture(), FIELD_ERROR_businessLicensePicture, sbError) && businessLicensePicture.length() > MAX_LENGTH_BusinessLicensePicture) {
					return sbError.toString();
				}
			}
			if (logo != null) {
				if (printCheckField(field.getFIELD_NAME_logo(), FIELD_ERROR_logo, sbError) && logo.length() > MAX_LENGTH_Logo) {
					return sbError.toString();
				}
			}
			if (printCheckField(field.getFIELD_NAME_bossPhone(), FIELD_ERROR_bossPhone, sbError) && !FieldFormat.checkMobile(bossPhone)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossPassword(), FieldFormat.FIELD_ERROR_Password, sbError) && !FieldFormat.checkRawPassword(bossPassword)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossWechat(), FieldFormat.FIELD_ERROR_wechat, sbError) && (!StringUtils.isEmpty(bossWechat) && !FieldFormat.checkWeChat(bossWechat))) {// 接受空值
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_dbName(), FieldFormat.FIELD_ERROR_dbName, sbError) && !FieldFormat.checkDbName(dbName)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
				return sbError.toString();
			}
			// SimpleDateFormat sdf = new
			// SimpleDateFormat(BaseAction.TIME_FORMAT_RetailTradeSN);
			// String date = sdf.format(new Date());
			// key = MD5Util.MD5(date);
			if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && !FieldFormat.checkCompanyKey(key)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(name)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossName(), FieldFormat.FIELD_ERROR_HumanName, sbError) && !FieldFormat.checkHumanName(bossName)) {
				return sbError.toString();
			}
			// if (printCheckField(field.getFIELD_NAME_dbUserName(), FIELD_ERROR_DBUserName,
			// sbError) && !FieldFormat.checkDBUserName(dbUserName)) {
			// return sbError.toString();
			// }
			// if (printCheckField(field.getFIELD_NAME_dbUserPassword(),
			// FieldFormat.FIELD_ERROR_Password, sbError) &&
			// !FieldFormat.checkPassword(dbUserPassword)) {
			// return sbError.toString();
			// }
			// if
			// (printCheckCompanyAppField(FIELD_ERROR_authorizerAppid_authorizerRefreshToken,
			// sbError) && !FieldFormat.checkCompanyAppField(authorizerAppid,
			// authorizerRefreshToken)) {
			// return sbError.toString();
			// }
			return "";
		default:
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && status != 0) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(businessLicenseSN))) {
				return sbError.toString();
			}
			if (businessLicensePicture != null) {
				if (printCheckField(field.getFIELD_NAME_businessLicensePicture(), FIELD_ERROR_businessLicensePicture, sbError) && businessLicensePicture.length() > MAX_LENGTH_BusinessLicensePicture) {
					return sbError.toString();
				}
			}
			if (logo != null) {
				if (printCheckField(field.getFIELD_NAME_logo(), FIELD_ERROR_logo, sbError) && logo.length() > MAX_LENGTH_Logo) {
					return sbError.toString();
				}
			}
			if (printCheckField(field.getFIELD_NAME_bossPhone(), FIELD_ERROR_bossPhone, sbError) && !FieldFormat.checkMobile(bossPhone)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossPassword(), FieldFormat.FIELD_ERROR_Password, sbError) && !FieldFormat.checkRawPassword(bossPassword)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossWechat(), FieldFormat.FIELD_ERROR_wechat, sbError) && (!StringUtils.isEmpty(bossWechat) && !FieldFormat.checkWeChat(bossWechat))) {// 接受空值
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_dbName(), FieldFormat.FIELD_ERROR_dbName, sbError) && !FieldFormat.checkDbName(dbName)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
				return sbError.toString();
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
			String date1 = sdf1.format(new Date());
			key = MD5Util.MD5(date1); // TODO
			if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && !FieldFormat.checkCompanyKey(key)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(name)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bossName(), FieldFormat.FIELD_ERROR_HumanName, sbError) && !FieldFormat.checkHumanName(bossName)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_dbUserName(), FIELD_ERROR_DBUserName, sbError) && !FieldFormat.checkDBUserName(dbUserName)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_dbUserPassword(), FieldFormat.FIELD_ERROR_Password, sbError) && !FieldFormat.checkRawPassword(dbUserPassword)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_brandName(), FIELD_ERROR_brandName, sbError) && (StringUtils.isEmpty(brandName) || brandName.length() > MAX_LENGTH_brandName)) {
				return sbError.toString();
			}
			return "";
		}
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			switch (fieldToCheckUnique) {
			case CASE_CheckName:
				if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(uniqueField)) {
					return sbError.toString();
				}
				break;
			case CASE_CheckBusinessLicenseSN:
				if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(uniqueField))) {
					return sbError.toString();
				}
				break;
			// 公司营业执照照片和Key现在不由前端传进来
			// case CASE_CheckBusinessLicensePicture:
			// if (uniqueField == null) {
			// return FIELD_ERROR_checkUniqueField;
			// }
			// break;
			// case CASE_CheckKey:
			// if (uniqueField == null) {
			// return FIELD_ERROR_checkUniqueField;
			// }
			// break;
			case CASE_CheckDbName:
				if (printCheckField(field.getFIELD_NAME_dbName(), FieldFormat.FIELD_ERROR_dbName, sbError) && !FieldFormat.checkDbName(uniqueField)) {
					return sbError.toString();
				}
				break;
			case CASE_CheckSubmchid:
				if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(uniqueField)) {
					return sbError.toString();
				}
				break;
			default:
				return FIELD_ERROR_checkUniqueField;
			}
			break;
		case BaseBO.CASE_Company_retrieveNByVipMobile:
			if (printCheckField(field.getFIELD_NAME_queryKeyword(), Vip.FIELD_ERROR_CheckMobile, sbError) && !FieldFormat.checkMobile(queryKeyword)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_Company_matchVip:
			if (printCheckField(field.getFIELD_NAME_mobile(), Vip.FIELD_ERROR_CheckMobile, sbError) && !FieldFormat.checkMobile(mobile)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_openID(), FieldFormat.FIELD_ERROR_openID, sbError) && !FieldFormat.checkOpenID(openID)) {
				return sbError.toString();
			}

			if (!StringUtils.isEmpty(unionID)) {
				if (printCheckField(field.getFIELD_NAME_unionID(), FieldFormat.FIELD_ERROR_UnionID, sbError) && !FieldFormat.checkUnionID(unionID)) {
					return sbError.toString();
				}
			}

			// 当sUnionID和sOpenID都不为空串的时候，说明是首次使用小程序登录，需要更新其openid、unionid、会员名称、性别，此时，需要检查名称、性别的合法性
			if (!StringUtils.isEmpty(unionID) && !StringUtils.isEmpty(openID)) {
				if (!StringUtils.isEmpty(vipName)) {
					if (printCheckField(field.getFIELD_NAME_vipName(), Vip.FIELD_ERROR_name, sbError) && !FieldFormat.checkVipName(vipName)) {
						return sbError.toString();
					}
				} else {
					return FIELD_ERROR_vipName;
				}

				if (printCheckField(field.getFIELD_NAME_sex(), Vip.FIELD_ERROR_sex, sbError) && !EnumSexVip.inBound(sex)) {
					return sbError.toString();
				}
			}

		default:
			return "";
		}
	}

	public enum EnumCompanyCreationStatus {
		ECCS_Incumbent("ECCS_Incumbent", 0), // 已经创建成功，正在服役
		ECCS_NotIncumbent("ECCS_NotIncumbent", 1);// 正在创建，未开始服役

		private String name;
		private int index;

		private EnumCompanyCreationStatus(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCompanyCreationStatus c : EnumCompanyCreationStatus.values()) {
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
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}

	/** 清除vip不可以看到的敏感信息。一般用来向请求者返回数据前设置 */
	public void clearSensitiveInfo(BaseAction.EnumUserScope scope) {
		key = null;
		dbName = null;
		dbUserName = null;
		dbUserPassword = null;
		bossName = null;
		bossPassword = null;
		bossPhone = null;
		bossWechat = null;
		submchid = null;
		businessLicenseSN = null;
		businessLicensePicture = null;
		// 前端自动登录时，需要传SN给后端，所以SN需要传给前端
		// if (scope == BaseAction.EnumUserScope.VIP) {
		// SN = null;
		// }
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> list = new ArrayList<BaseModel>();

		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Company company = new Company();
				list.add(company.parse1(jsonObject));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return list;
	}
}
