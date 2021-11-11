package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BonusRule extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final BonusRuleField field = new BonusRuleField();

	public static final int MIN_amountUnit = 0;
	public static final int MIN_increaseBonus = 0;
	public static final int MIN_maxIncreaseBonus = 0;
	public static final int MIN_initIncreaseBonus = 0;

	public static final String FIELD_ERROR_amountUnit = "amountUnit必须大于" + MIN_amountUnit;
	public static final String FIELD_ERROR_increaseBonus = "increaseBonus必须大于" + MIN_increaseBonus;
	public static final String FIELD_ERROR_maxIncreaseBonus = "maxIncreaseBonus要大于或等于increaseBonus";
	public static final String FIELD_ERROR_initIncreaseBonus = "amountUnit必须大于等于" + MIN_initIncreaseBonus;

	/** 目前系统有且只有一种会员卡，一种会员卡对应一种积分规则，所以积分规则在DB里只有一行数据，其F_ID永远为1 */
	public static final int DEFAULT_BonusRule_ID = 1;

	protected int vipCardID;

	protected int amountUnit;

	protected int increaseBonus;

	protected int maxIncreaseBonus;

	protected int initIncreaseBonus;

	protected int bForceDelete;

	public int getVipCardID() {
		return vipCardID;
	}

	public void setVipCardID(int vipCardID) {
		this.vipCardID = vipCardID;
	}

	public int getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(int amountUnit) {
		this.amountUnit = amountUnit;
	}

	public int getIncreaseBonus() {
		return increaseBonus;
	}

	public void setIncreaseBonus(int increaseBonus) {
		this.increaseBonus = increaseBonus;
	}

	public int getMaxIncreaseBonus() {
		return maxIncreaseBonus;
	}

	public void setMaxIncreaseBonus(int maxIncreaseBonus) {
		this.maxIncreaseBonus = maxIncreaseBonus;
	}

	public int getInitIncreaseBonus() {
		return initIncreaseBonus;
	}

	public void setInitIncreaseBonus(int initIncreaseBonus) {
		this.initIncreaseBonus = initIncreaseBonus;
	}

	public int getbForceDelete() {
		return bForceDelete;
	}

	public void setbForceDelete(int bForceDelete) {
		this.bForceDelete = bForceDelete;
	}

	@Override
	public String toString() {
		return "BonusRule [vipCardID=" + vipCardID + ", amountUnit=" + amountUnit + ", increaseBonus=" + increaseBonus + ", maxIncreaseBonus=" + maxIncreaseBonus + ", initIncreaseBonus=" + initIncreaseBonus + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		BonusRule bonusRule = new BonusRule();
		bonusRule.setID(ID);
		bonusRule.setVipCardID(vipCardID);
		bonusRule.setAmountUnit(amountUnit);
		bonusRule.setIncreaseBonus(increaseBonus);
		bonusRule.setMaxIncreaseBonus(maxIncreaseBonus);
		bonusRule.setInitIncreaseBonus(initIncreaseBonus);
		bonusRule.setbForceDelete(bForceDelete);

		return bonusRule;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		BonusRule bonusRule = (BonusRule) arg0;
		if ((ignoreIDInComparision == true ? true : bonusRule.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& bonusRule.getVipCardID() == vipCardID && printComparator(field.getFIELD_NAME_vipCardID()) //
				&& bonusRule.getAmountUnit() == amountUnit && printComparator(field.getFIELD_NAME_amountUnit()) //
				&& bonusRule.getIncreaseBonus() == increaseBonus && printComparator(field.getFIELD_NAME_increaseBonus()) //
				&& bonusRule.getMaxIncreaseBonus() == maxIncreaseBonus && printComparator(field.getFIELD_NAME_maxIncreaseBonus()) //
				&& bonusRule.getInitIncreaseBonus() == initIncreaseBonus && printComparator(field.getFIELD_NAME_initIncreaseBonus()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			vipCardID = jo.getInt(field.getFIELD_NAME_vipCardID());
			amountUnit = jo.getInt(field.getFIELD_NAME_amountUnit());
			increaseBonus = jo.getInt(field.getFIELD_NAME_increaseBonus());
			maxIncreaseBonus = jo.getInt(field.getFIELD_NAME_maxIncreaseBonus());
			initIncreaseBonus = jo.getInt(field.getFIELD_NAME_initIncreaseBonus());

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> bonusRuleList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				BonusRule bonusRule = new BonusRule();
				bonusRule.doParse1(jsonObject1);
				bonusRuleList.add(bonusRule);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bonusRuleList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BonusRule bonusRule = (BonusRule) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipCardID(), bonusRule.getVipCardID());
		params.put(field.getFIELD_NAME_amountUnit(), bonusRule.getAmountUnit());
		params.put(field.getFIELD_NAME_increaseBonus(), bonusRule.getIncreaseBonus());
		params.put(field.getFIELD_NAME_maxIncreaseBonus(), bonusRule.getMaxIncreaseBonus());
		params.put(field.getFIELD_NAME_initIncreaseBonus(), bonusRule.getInitIncreaseBonus());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		BonusRule bonusRule = (BonusRule) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), bonusRule.getID());
		params.put(field.getFIELD_NAME_amountUnit(), bonusRule.getAmountUnit());
		params.put(field.getFIELD_NAME_increaseBonus(), bonusRule.getIncreaseBonus());
		params.put(field.getFIELD_NAME_maxIncreaseBonus(), bonusRule.getMaxIncreaseBonus());
		params.put(field.getFIELD_NAME_initIncreaseBonus(), bonusRule.getInitIncreaseBonus());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Map<String, Object> params = new HashMap<String, Object>();
		BonusRule bonusRule = (BonusRule) bm;

		params.put(field.getFIELD_NAME_ID(), bonusRule.getID());
		params.put(field.getFIELD_NAME_bForceDelete(), bonusRule.getbForceDelete());

		return params;
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
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_amountUnit(), FIELD_ERROR_amountUnit, sbError) && amountUnit <= MIN_amountUnit) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_increaseBonus(), FIELD_ERROR_increaseBonus, sbError) && increaseBonus <= MIN_increaseBonus) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_maxIncreaseBonus(), FIELD_ERROR_maxIncreaseBonus, sbError) &&  maxIncreaseBonus < increaseBonus) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_initIncreaseBonus(), FIELD_ERROR_initIncreaseBonus, sbError) && initIncreaseBonus < MIN_initIncreaseBonus) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
