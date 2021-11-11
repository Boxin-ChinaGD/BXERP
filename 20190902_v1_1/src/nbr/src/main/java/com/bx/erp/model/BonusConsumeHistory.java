package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BonusConsumeHistory extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final BonusConsumeHistoryField field = new BonusConsumeHistoryField();

	public static final String FIELD_ERROR_vipID = "vipID只能为" + BaseAction.INVALID_ID + "或者大于0";
	public static final String FIELD_ERROR_vipMobile = "会员手机号必须空串或者null或者是" + FieldFormat.LENGTH_Mobile + "位";
	public static final String FIELD_ERROR_vipName = "会员名称必须为空串或者null或者" + Vip.FIELD_ERROR_name;

	protected int vipID;

	protected int staffID;

	protected int bonus;

	protected int addedBonus;

	protected String remark;

	protected String vipMobile;

	protected String vipName;

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getVipMobile() {
		return vipMobile;
	}

	public void setVipMobile(String vipMobile) {
		this.vipMobile = vipMobile;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getAddedBonus() {
		return addedBonus;
	}

	public void setAddedBonus(int addedBonus) {
		this.addedBonus = addedBonus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String recordBonus) {
		this.remark = recordBonus;
	}

	@Override
	public String toString() {
		return "BonusConsumeHistory [vipID=" + vipID + ", staffID=" + staffID + ", bonus=" + bonus + ", addedBonus=" + addedBonus + ", remark=" + remark + ", vipMobile=" + vipMobile + ", vipName=" + vipName + ", staffName=" + staffName
				+ ", createDatetime=" + createDatetime + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		BonusConsumeHistory bonusConsumeDetails = new BonusConsumeHistory();
		bonusConsumeDetails.setID(ID);
		bonusConsumeDetails.setVipID(vipID);
		bonusConsumeDetails.setStaffID(staffID);
		bonusConsumeDetails.setBonus(bonus);
		bonusConsumeDetails.setAddedBonus(addedBonus);
		bonusConsumeDetails.setRemark(remark);
		bonusConsumeDetails.setCreateDatetime((Date) createDatetime.clone());

		return bonusConsumeDetails;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		BonusConsumeHistory bonusConsumeDetails = (BonusConsumeHistory) arg0;
		if ((ignoreIDInComparision == true ? true : bonusConsumeDetails.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& bonusConsumeDetails.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID()) //
				&& bonusConsumeDetails.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID()) //
				&& bonusConsumeDetails.getBonus() == bonus && printComparator(field.getFIELD_NAME_bonus()) //
				&& bonusConsumeDetails.getAddedBonus() == addedBonus && printComparator(field.getFIELD_NAME_addedBonus()) //
				&& bonusConsumeDetails.getRemark().equals(remark) && printComparator(field.getFIELD_NAME_remark()) //
				&& bonusConsumeDetails.getCreateDatetime().equals(createDatetime) && printComparator(field.getFIELD_NAME_createDatetime()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			vipID = jo.getInt(field.getFIELD_NAME_vipID());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			addedBonus = jo.getInt(field.getFIELD_NAME_addedBonus());
			remark = jo.getString(field.getFIELD_NAME_remark());
			createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(jo.getString(field.getFIELD_NAME_createDatetime()));

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> bonusConsumeDetailsList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				BonusConsumeHistory bonusConsumeDetails = new BonusConsumeHistory();
				bonusConsumeDetails.doParse1(jsonObject1);
				bonusConsumeDetailsList.add(bonusConsumeDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bonusConsumeDetailsList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BonusConsumeHistory bonusConsumeDetails = (BonusConsumeHistory) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), bonusConsumeDetails.getVipID());
		params.put(field.getFIELD_NAME_staffID(), bonusConsumeDetails.getStaffID());
		params.put(field.getFIELD_NAME_bonus(), bonusConsumeDetails.getBonus());
		params.put(field.getFIELD_NAME_addedBonus(), bonusConsumeDetails.getAddedBonus());
		params.put(field.getFIELD_NAME_remark(), bonusConsumeDetails.getRemark());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BonusConsumeHistory bonusConsumeDetails = (BonusConsumeHistory) bm;
		if (bonusConsumeDetails.getVipID() != BaseAction.INVALID_ID) {
			bonusConsumeDetails.setVipMobile("");
			bonusConsumeDetails.setVipName("");
		} else if (FieldFormat.checkMobile(bonusConsumeDetails.getVipMobile())) {
			bonusConsumeDetails.setVipID(BaseAction.INVALID_ID);
			bonusConsumeDetails.setVipName("");
		} else if (!StringUtils.isEmpty(vipName)) {
			bonusConsumeDetails.setVipID(BaseAction.INVALID_ID);
			bonusConsumeDetails.setVipMobile("");
		} else {
			bonusConsumeDetails.setVipID(BaseAction.INVALID_ID);
			bonusConsumeDetails.setVipMobile("");
			bonusConsumeDetails.setVipName("");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), bonusConsumeDetails.getVipID());
		params.put(field.getFIELD_NAME_vipMobile(), bonusConsumeDetails.getVipMobile());
		params.put(field.getFIELD_NAME_vipName(), bonusConsumeDetails.getVipName());
		params.put(field.getFIELD_NAME_iPageIndex(), bonusConsumeDetails.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), bonusConsumeDetails.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (printCheckField(field.getFIELD_NAME_vipID(), FIELD_ERROR_vipID, sbError) && (vipID != BaseAction.INVALID_ID && vipID < 0)) {
			return sbError.toString();
		}
		if (!StringUtils.isEmpty(vipMobile)) {
			if (printCheckField(field.getFIELD_NAME_vipMobile(), FIELD_ERROR_vipMobile, sbError) && !FieldFormat.checkMobile(vipMobile)) {
				return sbError.toString();
			}
		}
		if (!StringUtils.isEmpty(vipName)) {
			if (printCheckField(field.getFIELD_NAME_vipName(), FIELD_ERROR_vipName, sbError) && !FieldFormat.checkVipName(vipName)) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
