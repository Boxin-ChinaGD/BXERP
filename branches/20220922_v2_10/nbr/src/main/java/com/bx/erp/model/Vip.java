package com.bx.erp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Vip extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final VipField field = new VipField();

	/** 导入的会员积分。若为-1，证明不是导入而从小程序或商家平台发起创建的。 */
	public static final int BONUS_Imported = -1;
	public static final int MAX_LENGTH_VipCardSN = 16;
	public static final int MAX_LENGTH_Email = 30;
	public static final int MAX_LENGTH_Account = 30;
	public static final int MAX_LENGTH_District = 30;
	public static final int MIN_LENGTH_Mobile = 6;
	public static final int LENGTH_ICID = 18;
	public static final int MAX_LENGTH_Wechat = 20;
	public static final int MIN_LENGTH_Wechat = 5;
	public static final int MAX_LENGTH_QQ = 10;
	public static final int MIN_LENGTH_QQ = 5;
	public static final int CASE_CheckPhone = 1; // 检查手机号
	public static final int CASE_CheckICID = 2; // 检查ICID
	public static final int CASE_CheckWechat = 3; // 检查微信
	public static final int CASE_CheckQQ = 4; // 检查QQ
	public static final int CASE_CheckEmail = 5; // 检查邮箱
	public static final int CASE_CheckAccount = 6; // 检查登录账号
	public static final int MAX_LENGTH_WxNickName = 32;
	public static final int MIN_LENGTH_WxNickName = 1;
	public static final int MAX_Bonus = 10000;
	public static final int MIN_Bonus = 0;
	public static final String FIELD_ERROR_RNByMobileOrVipCardSN = "手机号码或者会员卡卡号不能都为空";
	public static final String FIELD_ERROR_RNByMobileOrVipCardSN_VipCardSN = "搜索的会员卡卡号长度不能超过" + MAX_LENGTH_VipCardSN + "位.";
	public static final String FIELD_ERROR_RNByMobileOrVipCardSN_mobile = "搜索的手机号长度为[" + MIN_LENGTH_Mobile + ", " + FieldFormat.LENGTH_Mobile + "]";
	public static final String FIELD_ERROR_mobile = "会员手机号必须是" + FieldFormat.LENGTH_Mobile + "位";
	public static final String FIELD_ERROR_CheckMobile = "检查的手机号长度为" + FieldFormat.LENGTH_Mobile + "位";
	public static final String FIELD_ERROR_icid = "VIP的身份证号码的格式有误";
	public static final String FIELD_ERROR_password = "VIP密码不能包含中文并且长度为[" + FieldFormat.MIN_LENGTH_Password + ", " + FieldFormat.MAX_LENGTH_Password + "]";
	public static final String FIELD_ERROR_name = "VIP名字的长度必须在" + MIN_LENGTH_WxNickName + "和" + MAX_LENGTH_WxNickName + "之间";
	public static final String FIELD_ERROR_ICID = "身份证的长度必须是" + LENGTH_ICID + "位";
	public static final String FIELD_ERROR_Wechat = "微信号的长度为[" + MIN_LENGTH_Wechat + ", " + MAX_LENGTH_Wechat + "]";
	public static final String FIELD_ERROR_QQ = "QQ号的长度为[" + MIN_LENGTH_QQ + ", " + MAX_LENGTH_QQ + "]的正整数";
	public static final String FIELD_ERROR_Email = "邮箱的格式不对并且邮箱最大长度为" + MAX_LENGTH_Email;
	public static final String FIELD_ERROR_Account = "账号最大长度为" + MAX_LENGTH_Account;
	public static final String FIELD_ERROR_fieldToCheckUnique = field.getFIELD_NAME_fieldToCheckUnique() + "只能是" + CASE_CheckPhone + "或" + CASE_CheckICID + "或" + CASE_CheckWechat + "或" + CASE_CheckQQ + "或" + CASE_CheckEmail + "或"
			+ CASE_CheckAccount;
	public static final String FIELD_ERROR_IDInPOS = "IDInPOS必须大于0";
	public static final String FEILD_ERROR_category = "会员分类ID必须是默认的会员类别ID";
	public static final String FIELD_ERROR_district = "会员区域的长度为(0, " + MAX_LENGTH_District + "]";
	public static final String FIELD_ERROR_consumeTimes = "创建会员时总消费次数必须等于0";
	public static final String FIELD_ERROR_consumeAmount = "创建会员时总消费金额必须等于0";
	public static final String FIELD_ERROR_DateTime = "时间格式必须为:" + BaseAction.DATETIME_FORMAT_Default2 + "或者为:" + BaseAction.DATETIME_FORMAT_Default4;
	public static final String FIELD_ERROR_bonusUpdated = "会员修改的积分只能在" + MIN_Bonus + "和" + MAX_Bonus + "之间";
	public static final String FIELD_ERROR_BonusImported = "小程序或后台创建的会员,并不需要传递Bonus";
	public static final String FIELD_ERROR_cardID = "会员卡ID必须大于0";
	public static final String FIELD_ERROR_sex = "会员性别sex必须是" + EnumSexVip.ESV_Male.getIndex() + "或者" + EnumSexVip.ESV_Female.getIndex() + "或者" + EnumSexVip.ESV_Unknown.getIndex();
	public static final String FIELD_ERROR_cardCode = "会员卡号只支持数字";
	
	private int startRetailTreadeIDInSQLite;// 查询会员消费记录的零售单ID起始值

	protected String sn;

	protected int cardID;

	protected String mobile;

	protected String localPosSN;

	protected String iCID;

	protected String name;

	protected String email;

	protected int consumeTimes;

	protected double consumeAmount;

	protected String district;

	protected int category;

	protected Date birthday;

	protected int bonus;

	protected Date lastConsumeDatetime;

	protected String remark;

	protected int sex;

	protected String logo;

	private int bQuerySmallerThanStartID; // 非数据库字段，如果为0,查询大于起始值的零售单ID,如果为1,则查询小于起始值的零售单ID

	protected String decryptSessionCode; // 非数据库字段，用于小程序网络请求传递参数

	protected String encryptedUnionID; // 非数据库字段，用于小程序网络请求传递参数

	protected String ivUnionID; // 非数据库字段，用于小程序网络请求传递参数

	protected String encryptedPhone; // 非数据库字段，用于小程序网络请求传递参数

	protected String ivPhone; // 非数据库字段，用于小程序网络请求传递参数

	protected String shopName; // 非数据库字段，用于小程序网络请求传递参数

	protected String memberCardID; // 非数据库字段，用于小程序网络请求传递参数

	protected String couponID; // 非数据库字段，用于小程序网络请求传递参数

	protected String encryptedCode; // 非数据库字段，用于小程序网络请求传递参数

	private int isIncreaseBonus;

	private int amountOnAddBonus;

	private int bonusOnMinusAmount;

	private String cardCode; // 非数据库字段，用于POS端查询会员信息/导入商家数据进行创建会员卡时.存放着商家会员的会员卡号

	private String vipCardSN; // 非数据库字段，用于POS端查找会员

	private int staffID;

	private int amount;

	private String remarkForBonusHistory;

	private int manuallyAdded;

	private String companySN;

	protected int isImported; // 非数据库字段，用于区别创建会员时,该会员是否是通过导入数据而来的
	
	/* 在excel表的行号*/ 
	protected int excelLineID;
	
	public int getExcelLineID() {
		return excelLineID;
	}

	public void setExcelLineID(int excelLineID) {
		this.excelLineID = excelLineID;
	}

	public int getIsImported() {
		return isImported;
	}

	public void setIsImported(int isImport) {
		this.isImported = isImport;
	}

	public String getCompanySN() {
		return companySN;
	}

	public void setCompanySN(String companySN) {
		this.companySN = companySN;
	}

	public String getVipCardSN() {
		return vipCardSN;
	}

	public void setVipCardSN(String vipCardSN) {
		this.vipCardSN = vipCardSN;
	}

	public String getEncryptedCode() {
		return encryptedCode;
	}

	public void setEncryptedCode(String encryptedCode) {
		this.encryptedCode = encryptedCode;
	}

	public String getMemberCardID() {
		return memberCardID;
	}

	public void setMemberCardID(String memberCardID) {
		this.memberCardID = memberCardID;
	}

	public String getCouponID() {
		return couponID;
	}

	public void setCouponID(String couponID) {
		this.couponID = couponID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getiCID() {
		return iCID;
	}

	public String getDecryptSessionCode() {
		return decryptSessionCode;
	}

	public void setDecryptSessionCode(String decryptSessionCode) {
		this.decryptSessionCode = decryptSessionCode;
	}

	public String getEncryptedUnionID() {
		return encryptedUnionID;
	}

	public void setEncryptedUnionID(String encryptedUnionID) {
		this.encryptedUnionID = encryptedUnionID;
	}

	public String getIvUnionID() {
		return ivUnionID;
	}

	public void setIvUnionID(String ivUnionID) {
		this.ivUnionID = ivUnionID;
	}

	public String getEncryptedPhone() {
		return encryptedPhone;
	}

	public void setEncryptedPhone(String encryptedPhone) {
		this.encryptedPhone = encryptedPhone;
	}

	public String getIvPhone() {
		return ivPhone;
	}

	public void setIvPhone(String ivPhone) {
		this.ivPhone = ivPhone;
	}

	public void setiCID(String iCID) {
		this.iCID = iCID;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getConsumeTimes() {
		return consumeTimes;
	}

	public void setConsumeTimes(int consumeTimes) {
		this.consumeTimes = consumeTimes;
	}

	public double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getLastConsumeDatetime() {
		return lastConsumeDatetime;
	}

	public void setLastConsumeDatetime(Date lastConsumeDatetime) {
		this.lastConsumeDatetime = lastConsumeDatetime;
	}

	public int getCardID() {
		return cardID;
	}

	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	public String getLocalPosSN() {
		return localPosSN;
	}

	public void setLocalPosSN(String localPosSN) {
		this.localPosSN = localPosSN;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getStartRetailTreadeIDInSQLite() {
		return startRetailTreadeIDInSQLite;
	}

	public void setStartRetailTreadeIDInSQLite(int startRetailTreadeIDInSQLite) {
		this.startRetailTreadeIDInSQLite = startRetailTreadeIDInSQLite;
	}

	private String vipCategoryName;

	public String getVipCategoryName() {
		return vipCategoryName;
	}

	public void setVipCategoryName(String vipCategoryName) {
		this.vipCategoryName = vipCategoryName;
	}

	public int getbQuerySmallerThanStartID() {
		return bQuerySmallerThanStartID;
	}

	public void setbQuerySmallerThanStartID(int bQuerySmallerThanStartID) {
		this.bQuerySmallerThanStartID = bQuerySmallerThanStartID;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getIsIncreaseBonus() {
		return isIncreaseBonus;
	}

	public void setIsIncreaseBonus(int isIncreaseBonus) {
		this.isIncreaseBonus = isIncreaseBonus;
	}

	public int getAmountOnAddBonus() {
		return amountOnAddBonus;
	}

	public void setAmountOnAddBonus(int amountOnAddBonus) {
		this.amountOnAddBonus = amountOnAddBonus;
	}

	public int getBonusOnMinusAmount() {
		return bonusOnMinusAmount;
	}

	public void setBonusOnMinusAmount(int bonusOnMinusAmount) {
		this.bonusOnMinusAmount = bonusOnMinusAmount;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRemarkForBonusHistory() {
		return remarkForBonusHistory;
	}

	public void setRemarkForBonusHistory(String remarkForBonusHistory) {
		this.remarkForBonusHistory = remarkForBonusHistory;
	}

	public int getManuallyAdded() {
		return manuallyAdded;
	}

	public void setManuallyAdded(int manuallyAdded) {
		this.manuallyAdded = manuallyAdded;
	}

	@Override
	public String toString() {
		return "Vip [startRetailTreadeIDInSQLite=" + startRetailTreadeIDInSQLite + ", sn=" + sn + ", cardID=" + cardID + ", mobile=" + mobile + ", localPosSN=" + localPosSN + ", iCID=" + iCID + ", name=" + name + ", email=" + email
				+ ", consumeTimes=" + consumeTimes + ", consumeAmount=" + consumeAmount + ", district=" + district + ", category=" + category + ", birthday=" + birthday + ", bonus=" + bonus + ", lastConsumeDatetime=" + lastConsumeDatetime
				+ ", remark=" + remark + ", sex=" + sex + ", logo=" + logo + ", bQuerySmallerThanStartID=" + bQuerySmallerThanStartID + ", decryptSessionCode=" + decryptSessionCode + ", encryptedUnionID=" + encryptedUnionID + ", ivUnionID="
				+ ivUnionID + ", encryptedPhone=" + encryptedPhone + ", ivPhone=" + ivPhone + ", shopName=" + shopName + ", memberCardID=" + memberCardID + ", couponID=" + couponID + ", encryptedCode=" + encryptedCode + ", isIncreaseBonus="
				+ isIncreaseBonus + ", amountOnAddBonus=" + amountOnAddBonus + ", bonusOnMinusAmount=" + bonusOnMinusAmount + ", cardCode=" + cardCode + ", vipCardSN=" + vipCardSN + ", staffID=" + staffID + ", amount=" + amount
				+ ", remarkForBonusHistory=" + remarkForBonusHistory + ", manuallyAdded=" + manuallyAdded + ", companySN=" + companySN + ", vipCategoryName=" + vipCategoryName + ", ID=" + ID + "]";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);

			ID = jo.getInt(field.getFIELD_NAME_ID());
			sn = jo.getString(field.getFIELD_NAME_sn());
			cardID = jo.getInt(field.getFIELD_NAME_cardID());
			mobile = jo.getString(field.getFIELD_NAME_mobile());
			localPosSN = jo.getString(field.getFIELD_NAME_localPosSN());
			iCID = jo.getString(field.getFIELD_NAME_iCID());
			name = jo.getString(field.getFIELD_NAME_name());
			email = jo.getString(field.getFIELD_NAME_email());
			consumeTimes = jo.getInt(field.getFIELD_NAME_consumeTimes());
			consumeAmount = jo.getDouble(field.getFIELD_NAME_consumeAmount());
			district = jo.getString(field.getFIELD_NAME_district());
			category = jo.getInt(field.getFIELD_NAME_category());
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			remark = jo.getString(field.getFIELD_NAME_remark());
			sex = jo.getInt(field.getFIELD_NAME_sex());
			logo = jo.getString(field.getFIELD_NAME_logo());
			//
			String tmp = jo.getString(field.getFIELD_NAME_birthday());
			if (!"".equals(tmp)) {
				birthday = sdf.parse(tmp);
			}
			//
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			//
			String tmp2 = jo.getString(field.getFIELD_NAME_lastConsumeDatetime());
			if (!"".equals(tmp2)) {
				lastConsumeDatetime = sdf.parse(tmp2);
			}
			//
			String tmp3 = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp3)) {
				createDatetime = sdf.parse(tmp3);
			}
			//
			String tmp4 = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp4)) {
				updateDatetime = sdf.parse(tmp4);
			}
			cardCode = jo.getString(field.getFIELD_NAME_cardCode());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> vipList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Vip vip = new Vip();
				vip.doParse1(jsonObject);
				vipList.add(vip);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return vipList;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		Vip vip = (Vip) arg0;
		if ((ignoreIDInComparision == true ? true : (vip.getCardID() == cardID && printComparator(field.getFIELD_NAME_cardID())))//
				&& vip.getMobile().equals(mobile) && printComparator(field.getFIELD_NAME_mobile()) //
				&& (StringUtils.isEmpty(vip.getiCID()) ? StringUtils.isEmpty(iCID) : vip.getiCID().equals(iCID)) && printComparator(field.getFIELD_NAME_iCID())//
				&& vip.getName().equals(name) && printComparator(field.getFIELD_NAME_name())//
				&& (StringUtils.isEmpty(vip.getEmail()) ? StringUtils.isEmpty(email) : vip.getEmail().equals(email)) && printComparator(field.getFIELD_NAME_email())//
				&& vip.getConsumeTimes() == consumeTimes && printComparator(field.getFIELD_NAME_consumeTimes())//
				&& Math.abs(GeneralUtil.sub(vip.getConsumeAmount(), consumeAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_consumeAmount())//
				&& (StringUtils.isEmpty(vip.getDistrict()) ? StringUtils.isEmpty(district) : vip.getDistrict().equals(district)) && printComparator(field.getFIELD_NAME_district())//
				&& vip.getCategory() == category && printComparator(field.getFIELD_NAME_category())//
				// && vip.getBirthday() == this.getBirthday() && printComparator("birthday")//
				// && vip.getExpireDatetime() == this.getExpireDatetime() &&
				// printComparator("expireDatetime")//
				&& vip.getBonus() == bonus && printComparator(field.getFIELD_NAME_bonus())//
				&& vip.getSex() == sex && printComparator(field.getFIELD_NAME_sex())//
		// && vip.getLastConsumeDatetime() == this.getLastConsumeDatetime() &&
		// printComparator("lastConsumeDatetime")//
		) {
			return 0;
		}
		return -1;
	}

	public Vip() {
		super();

		// IDInPOS = BaseAction.INVALID_NO;
		category = BaseAction.INVALID_ID;

	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Vip vip = new Vip();
		vip.setID(ID);
		vip.setMobile(mobile);
		vip.setCardID(cardID);
		vip.setLocalPosSN(localPosSN);
		vip.setiCID(iCID);
		vip.setName(name);
		vip.setEmail(email);
		vip.setConsumeTimes(consumeTimes);
		vip.setConsumeAmount(consumeAmount);
		vip.setDistrict(district);
		vip.setCategory(category);
		vip.setBirthday(birthday == null ? null : (Date) birthday.clone());
		vip.setBonus(bonus);
		vip.setLastConsumeDatetime(lastConsumeDatetime == null ? null : (Date) lastConsumeDatetime.clone());
		vip.setVipCategoryName(vipCategoryName);
		vip.setbQuerySmallerThanStartID(bQuerySmallerThanStartID);
		vip.setFieldToCheckUnique(fieldToCheckUnique);
		vip.setSn(sn);
		vip.setRemark(remark);
		vip.setSex(sex);
		vip.setLogo(logo);
		vip.setIsIncreaseBonus(isIncreaseBonus);
		vip.setAmountOnAddBonus(amountOnAddBonus);
		vip.setBonusOnMinusAmount(bonusOnMinusAmount);
		vip.setCardCode(cardCode);
		vip.setCreateDatetime(createDatetime);
		vip.setIsImported(isImported);
		return vip;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		Vip vip = (Vip) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_sn(), vip.getSn() == null ? "" : vip.getSn());
		params.put(field.getFIELD_NAME_mobile(), vip.getMobile() == null ? "" : vip.getMobile());
		params.put(field.getFIELD_NAME_cardID(), vip.getCardID());
		params.put(field.getFIELD_NAME_localPosSN(), vip.getLocalPosSN() == null ? "" : vip.getLocalPosSN());
		params.put(field.getFIELD_NAME_iCID(), vip.getiCID() == null ? "" : vip.getiCID());
		params.put(field.getFIELD_NAME_name(), vip.getName() == null ? "" : vip.getName());
		params.put(field.getFIELD_NAME_email(), vip.getEmail() == null ? "" : vip.getEmail());
		params.put(field.getFIELD_NAME_consumeTimes(), vip.getConsumeTimes());
		params.put(field.getFIELD_NAME_consumeAmount(), vip.getConsumeAmount());
		params.put(field.getFIELD_NAME_district(), vip.getDistrict() == null ? "" : vip.getDistrict());
		params.put(field.getFIELD_NAME_category(), vip.getCategory());
		params.put(field.getFIELD_NAME_birthday(), vip.getBirthday());
		params.put(field.getFIELD_NAME_lastConsumeDatetime(), vip.getLastConsumeDatetime());
		params.put(field.getFIELD_NAME_sex(), vip.getSex());
		params.put(field.getFIELD_NAME_logo(), vip.getLogo());
		params.put(field.getFIELD_NAME_remark(), vip.getRemark() == null ? "" : vip.getRemark());
		params.put(field.getFIELD_NAME_createDatetime(), vip.getCreateDatetime());
		params.put(field.getFIELD_NAME_bonus(), vip.getBonus());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParamEx(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		Vip vip = (Vip) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN:
			params.put(field.getFIELD_NAME_mobile(), vip.getMobile() == null ? "" : vip.getMobile());
			params.put(field.getFIELD_NAME_vipCardSN(), vip.getVipCardSN() == null ? "" : vip.getVipCardSN());
			params.put(field.getFIELD_NAME_iPageIndex(), vip.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), vip.getPageSize());

			break;
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), vip.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), vip.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_queryKeyword(), vip.getQueryKeyword());

			break;
		// case BaseBO.CASE_Vip_RetrieveNByMobileOrCardCode:
		// params.put(field.getFIELD_NAME_mobile(), vip.getMobile());
		// params.put(field.getFIELD_NAME_cardCode(), vip.getCardCode());
		//
		// break;
		default:
			params.put(field.getFIELD_NAME_cardID(), vip.getCardID());
			params.put(field.getFIELD_NAME_district(), vip.getDistrict() == null ? "" : vip.getDistrict());
			params.put(field.getFIELD_NAME_category(), vip.getCategory() == 0 ? BaseAction.INVALID_ID : vip.getCategory());
			params.put(field.getFIELD_NAME_mobile(), vip.getMobile() == null ? "" : vip.getMobile());
			params.put(field.getFIELD_NAME_iCID(), vip.getiCID() == null ? "" : vip.getiCID());
			params.put(field.getFIELD_NAME_email(), vip.getEmail() == null ? "" : vip.getEmail());
			params.put(field.getFIELD_NAME_iPageIndex(), vip.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), vip.getPageSize());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, BaseModel bm) {
		Vip vip = (Vip) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Vip_RetrieveNVipConsumeHistory:
			params.put(field.getFIELD_NAME_ID(), vip.getID());
			params.put(field.getFIELD_NAME_startRetailTreadeIDInSQLite(), vip.getStartRetailTreadeIDInSQLite());
			params.put(field.getFIELD_NAME_bQuerySmallerThanStartID(), vip.getbQuerySmallerThanStartID());
			params.put(field.getFIELD_NAME_iPageIndex(), vip.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), vip.getPageSize());

			break;
		default:
			throw new RuntimeException("没有实现Vip的getRetrieveNExParam()");
		}
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		Vip vip = (Vip) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_Vip_UpdateBonus:
			params.put(field.getFIELD_NAME_ID(), vip.getID());
			params.put(field.getFIELD_NAME_staffID(), vip.getStaffID());
			params.put(field.getFIELD_NAME_amount(), vip.getAmount());
			params.put(field.getFIELD_NAME_bonus(), vip.getBonus());
			params.put(field.getFIELD_NAME_remarkForBonusHistory(), vip.getRemarkForBonusHistory() == null ? "" : vip.getRemarkForBonusHistory());
			params.put(field.getFIELD_NAME_isIncreaseBonus(), vip.getIsIncreaseBonus());
			params.put(field.getFIELD_NAME_manuallyAdded(), vip.getManuallyAdded());
			break;
		case BaseBO.CASE_Vip_ResetBonus:
			break;
		default:
			params.put(field.getFIELD_NAME_ID(), vip.getID());
			params.put(field.getFIELD_NAME_district(), vip.getDistrict() == null ? "" : vip.getDistrict());
			params.put(field.getFIELD_NAME_category(), vip.getCategory());
			params.put(field.getFIELD_NAME_birthday(), vip.getBirthday());
			break;
		}
		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !FieldFormat.checkMobile(mobile)) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_localPosSN(), Pos.FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(localPosSN) && !FieldFormat.checkPosSN(localPosSN)) {
			return sbError.toString();
		}

		if (!StringUtils.isEmpty(iCID)) {
			if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_ICID, sbError) && iCID.length() != LENGTH_ICID) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_icid, sbError) && !FieldFormat.checkICID(iCID)) {
				return sbError.toString();
			}
		}

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipName(name)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && !StringUtils.isEmpty(email) && !FieldFormat.checkEmail(email)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_consumeTimes(), FIELD_ERROR_consumeTimes, sbError) && consumeTimes != 0) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_consumeAmount(), FIELD_ERROR_consumeAmount, sbError) && consumeAmount != 0) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_district(), FIELD_ERROR_district, sbError) && !StringUtils.isEmpty(district) && district.length() > MAX_LENGTH_District) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && category != VipCategory.DEFAULT_VipCategory_ID) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_birthday(), FIELD_ERROR_DateTime, sbError) && birthday != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			if (!FieldFormat.checkDate(sdf.format(birthday))) {
				return sbError.toString();
			}
		}

		if (printCheckField(field.getFIELD_NAME_cardID(), FIELD_ERROR_cardID, sbError) && cardID <= 0) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_sex(), FIELD_ERROR_sex, sbError) && !EnumSexVip.inBound(sex)) {
			return sbError.toString();
		}

		if (iUseCaseID == BaseBO.CASE_Vip_ImportFromOldSystem) {
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_bonusUpdated, sbError) && bonus < 0) {
				return sbError.toString();
			}
//			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_cardCode, sbError) && !FieldFormat.checkCardCode(cardCode)) {
//				return sbError.toString();
//			}
		} else {
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_BonusImported, sbError) && bonus != BONUS_Imported) {
				return sbError.toString();
			}
		}

		return "";
	}

	@Override
	public boolean setDefaultValueToCreate(int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.INVALID_CASE_ID:
			bonus = BONUS_Imported; // 小程序,后台创建会员并不需要传递积分
			return true;
		default:
			return super.setDefaultValueToCreate(iUseCaseID);
		}
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		switch (iUseCaseID) {
		default:
			break;
		}
		return super.getDeleteParam(iUseCaseID, bm);
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		switch (iUseCaseID) {
		default:
			return super.checkDelete(iUseCaseID);
		}
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_Vip_UpdateBonus:
			if(isIncreaseBonus == EnumBoolean.EB_Yes.getIndex()) {
				if (printCheckField(field.getFIELD_NAME_bonus(), Vip.FIELD_ERROR_bonusUpdated, sbError) && (bonus < MIN_Bonus || bonus > MAX_Bonus)) {
					return sbError.toString();
				}
			}
			break;
		case BaseBO.CASE_Vip_ResetBonus:
			break;
		default:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_district(), FIELD_ERROR_district, sbError) && !StringUtils.isEmpty(district) && district.length() > MAX_LENGTH_District) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && category != VipCategory.DEFAULT_VipCategory_ID) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_birthday(), FIELD_ERROR_DateTime, sbError) && birthday != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
				if (!FieldFormat.checkDate(sdf.format(birthday))) {
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
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		// case BaseBO.CASE_Vip_RetrieveNByMobileOrCardCode:
		// // TODO
		// return "";
		case BaseBO.CASE_CheckUniqueField:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && ID != 0 && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			switch (fieldToCheckUnique) {
			case CASE_CheckPhone:
				if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_CheckMobile, sbError) && StringUtils.isEmpty(queryKeyword) //
						|| !FieldFormat.checkMobile(queryKeyword)) {
					return sbError.toString();
				}
				return "";
			case CASE_CheckICID:
				if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_ICID, sbError) && StringUtils.isEmpty(queryKeyword) || //
						!FieldFormat.checkICID(queryKeyword)) {
					return sbError.toString();
				}
				return "";
			case CASE_CheckEmail:
				if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && StringUtils.isEmpty(queryKeyword) //
						|| queryKeyword.length() > MAX_LENGTH_Email || !FieldFormat.checkEmail(queryKeyword)) {
					return sbError.toString();
				}
				return "";
			default:
				return FIELD_ERROR_fieldToCheckUnique;
			}
		case BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN:
			if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_RNByMobileOrVipCardSN, sbError) && (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(vipCardSN))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_RNByMobileOrVipCardSN_mobile, sbError)
					&& (!StringUtils.isEmpty(mobile) ? (mobile.length() < MIN_LENGTH_Mobile || mobile.length() > FieldFormat.LENGTH_Mobile) : false)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_vipCardSN(), FIELD_ERROR_RNByMobileOrVipCardSN_VipCardSN, sbError) && (!StringUtils.isEmpty(vipCardSN) ? (vipCardSN.length() > MAX_LENGTH_VipCardSN) : false)) {
				return sbError.toString();
			}
			return "";
		default:
			// if(printCheckField(field.getFIELD_NAME_IDInPOS(), FIELD_ERROR_IDInPOS,
			// sbError) && IDInPOS != BaseAction.INVALID_ID &&
			// !FieldFormat.checkID(IDInPOS)) {
			// return sbError.toString();
			// }

			if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && category != BaseAction.INVALID_ID && category != VipCategory.DEFAULT_VipCategory_ID) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_district(), FIELD_ERROR_district, sbError) && !StringUtils.isEmpty(district) && district.length() > MAX_LENGTH_District) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !StringUtils.isEmpty(mobile) && mobile.length() > FieldFormat.LENGTH_Mobile) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(iCID) && iCID.length() > LENGTH_ICID) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && !StringUtils.isEmpty(email) && email.length() > MAX_LENGTH_Email) {
				return sbError.toString();
			}
			return "";
		}
	}

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		return "";
	}

	public enum EnumSexVip {
		ESV_Female("Female", 0), //
		ESV_Male("Male", 1), //
		ESV_Unknown("Unknown", 2);

		private String name;
		private int index;

		private EnumSexVip(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumSexVip c : EnumSexVip.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public static boolean inBound(int index) {
			if (index < ESV_Female.getIndex() || index > ESV_Unknown.getIndex()) {
				return false;
			}
			return true;
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
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_VipCacheSize.getIndex();
	}

	public enum EnumVipInfoInExcel {
		EVIIE_bonus("bonus", 0), //
		EVIIE_mobile("mobile", 1), //
		EVIIE_name("name", 2), //
		EVIIE_sex("sex", 3), //
		EVIIE_birthday("birthday", 4), //
		EVIIE_lastConsumeDatetime("lastConsumeDatetime", 5);
		private String name;
		private int index;

		private EnumVipInfoInExcel(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumVipInfoInExcel c : EnumVipInfoInExcel.values()) {
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

	public String checkCreate_returnField(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !FieldFormat.checkMobile(mobile)) {
			return field.getFIELD_NAME_mobile();
		}
		if (printCheckField(field.getFIELD_NAME_cardID(), FIELD_ERROR_cardID, sbError) && !FieldFormat.checkID(cardID)) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_localPosSN(), Pos.FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(localPosSN) && !FieldFormat.checkPosSN(localPosSN)) {
			return field.getFIELD_NAME_localPosSN();
		}

		if (!StringUtils.isEmpty(iCID)) {
			if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_ICID, sbError) && iCID.length() != LENGTH_ICID) {
				return field.getFIELD_NAME_iCID();
			}
			if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_icid, sbError) && !FieldFormat.checkICID(iCID)) {
				return field.getFIELD_NAME_iCID();
			}
		}

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipName(name)) {
			return field.getFIELD_NAME_name();
		}

		if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && !StringUtils.isEmpty(email) && !FieldFormat.checkEmail(email)) {
			return field.getFIELD_NAME_email();
		}

		if (printCheckField(field.getFIELD_NAME_consumeTimes(), FIELD_ERROR_consumeTimes, sbError) && consumeTimes != 0) {
			return field.getFIELD_NAME_consumeTimes();
		}

		if (printCheckField(field.getFIELD_NAME_consumeAmount(), FIELD_ERROR_consumeAmount, sbError) && consumeAmount != 0) {
			return field.getFIELD_NAME_consumeAmount();
		}

		if (printCheckField(field.getFIELD_NAME_district(), FIELD_ERROR_district, sbError) && !StringUtils.isEmpty(district) && district.length() > MAX_LENGTH_District) {
			return field.getFIELD_NAME_district();
		}

		if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && category != VipCategory.DEFAULT_VipCategory_ID) {
			return field.getFIELD_NAME_category();
		}

		if (printCheckField(field.getFIELD_NAME_birthday(), FIELD_ERROR_DateTime, sbError) && birthday != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			if (!FieldFormat.checkDate(sdf.format(birthday))) {
				return field.getFIELD_NAME_birthday();
			}
		}

		if (printCheckField(field.getFIELD_NAME_cardID(), FIELD_ERROR_cardID, sbError) && cardID <= 0) {
			return field.getFIELD_NAME_cardID();
		}

		if (printCheckField(field.getFIELD_NAME_sex(), FIELD_ERROR_sex, sbError) && !EnumSexVip.inBound(sex)) {
			return field.getFIELD_NAME_sex();
		}
		if (iUseCaseID == BaseBO.CASE_Vip_ImportFromOldSystem) {
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_bonusUpdated, sbError) && bonus < 0) {
				return field.getFIELD_NAME_bonus();
			}
			if (printCheckField(field.getFIELD_NAME_cardCode(), FIELD_ERROR_cardCode, sbError) && !FieldFormat.checkCardCode(cardCode)) {
				return field.getFIELD_NAME_cardCode();
			}
		} else {
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_BonusImported, sbError) && bonus != BONUS_Imported) {
				return field.getFIELD_NAME_bonus();
			}
		}
		return "";
	}

	public String doParse1_returnField(String s) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			JSONObject jo = JSONObject.fromObject(s);

			ID = jo.getInt(field.getFIELD_NAME_ID());
			sn = jo.getString(field.getFIELD_NAME_sn());
			cardID = jo.getInt(field.getFIELD_NAME_cardID());
			mobile = jo.getString(field.getFIELD_NAME_mobile());
			localPosSN = jo.getString(field.getFIELD_NAME_localPosSN());
			iCID = jo.getString(field.getFIELD_NAME_iCID());
			name = jo.getString(field.getFIELD_NAME_name());
			email = jo.getString(field.getFIELD_NAME_email());
			consumeTimes = jo.getInt(field.getFIELD_NAME_consumeTimes());
			consumeAmount = jo.getDouble(field.getFIELD_NAME_consumeAmount());
			district = jo.getString(field.getFIELD_NAME_district());
			category = jo.getInt(field.getFIELD_NAME_category());
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			remark = jo.getString(field.getFIELD_NAME_remark());
			sex = jo.getInt(field.getFIELD_NAME_sex());
			logo = jo.getString(field.getFIELD_NAME_logo());
			//
			String tmp = jo.getString(field.getFIELD_NAME_birthday());
			if (!"".equals(tmp)) {
				try {
					birthday = sdf.parse(tmp);
				} catch (ParseException e) {
					return field.getFIELD_NAME_birthday();
				}
			}
			//
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			//
			String tmp2 = jo.getString(field.getFIELD_NAME_lastConsumeDatetime());
			if (!"".equals(tmp2)) {
				try {
					lastConsumeDatetime = sdf.parse(tmp2);
				} catch (ParseException e) {
					return field.getFIELD_NAME_lastConsumeDatetime();
				}
			}
			//
			String tmp3 = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp3)) {
				createDatetime = sdf.parse(tmp3);
			}
			//
			String tmp4 = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp4)) {
				updateDatetime = sdf.parse(tmp4);
			}

		} catch (Exception e) {
			e.printStackTrace();
			String[] str = e.getMessage().split("\"");
			return str[1];
		}

		return "";
	}
}
