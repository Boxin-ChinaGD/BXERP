package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VipCard extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final VipCardField field = new VipCardField();

	public static final String FIELD_ERROR_clearBonusDay_clearBonusDatetime = "会员卡积分清零规则天数必须为正数或清零日期必须是合法日期";
	
	public static final String FIELD_ERROR_backgroundColor = "会员卡背景颜色格式不正确";

	protected String title;

	protected String backgroundColor;

	protected int clearBonusDay;

	protected Date clearBonusDatetime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getClearBonusDay() {
		return clearBonusDay;
	}

	public void setClearBonusDay(int clearBonusDay) {
		this.clearBonusDay = clearBonusDay;
	}

	public Date getClearBonusDatetime() {
		return clearBonusDatetime;
	}

	public void setClearBonusDatetime(Date clearBonusDatetime) {
		this.clearBonusDatetime = clearBonusDatetime;
	}

	@Override
	public String toString() {
		return "VipCard [title=" + title + ", backgroundColor=" + backgroundColor + ", clearBonusDay=" + clearBonusDay + ", clearBonusDatetime=" + clearBonusDatetime + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		VipCard vipCard = new VipCard();
		vipCard.setID(ID);
		vipCard.setTitle(title);
		vipCard.setBackgroundColor(backgroundColor);
		vipCard.setClearBonusDay(clearBonusDay);
		vipCard.setClearBonusDatetime(clearBonusDatetime);
		vipCard.setCreateDatetime(createDatetime);

		return vipCard;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		VipCard vipCard = (VipCard) arg0;
		if ((ignoreIDInComparision == true ? true : vipCard.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& vipCard.getTitle().equals(title) && printComparator(field.getFIELD_NAME_title()) //
				&& vipCard.getBackgroundColor().equals(backgroundColor) && printComparator(field.getFIELD_NAME_backgroundColor()) //
				&& vipCard.getClearBonusDay() == clearBonusDay && printComparator(field.getFIELD_NAME_clearBonusDay()) //
				&& DatetimeUtil.compareDate(vipCard.getClearBonusDatetime(), clearBonusDatetime) && printComparator(field.getFIELD_NAME_clearBonusDatetime()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			title = jo.getString(field.getFIELD_NAME_title());
			backgroundColor = jo.getString(field.getFIELD_NAME_backgroundColor());
			clearBonusDay = jo.getInt(field.getFIELD_NAME_clearBonusDay());
			//
			String tmp = jo.getString(field.getFIELD_NAME_clearBonusDatetime());
			if (!"".equals(tmp)) {
				clearBonusDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (clearBonusDatetime == null) {
					System.out.println("无法解析该日期：" + field.getFIELD_NAME_clearBonusDatetime() + "=" + tmp);
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
		List<BaseModel> vipCardList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				VipCard vipCard = new VipCard();
				vipCard.doParse1(jsonObject1);
				vipCardList.add(vipCard);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return vipCardList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCard vipCard = (VipCard) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_title(), vipCard.getTitle());
		params.put(field.getFIELD_NAME_backgroundColor(), vipCard.getBackgroundColor());
		params.put(field.getFIELD_NAME_clearBonusDay(), vipCard.getClearBonusDay());
		params.put(field.getFIELD_NAME_clearBonusDatetime(), vipCard.getClearBonusDatetime());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCard vipCard = (VipCard) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), vipCard.getID());
		params.put(field.getFIELD_NAME_title(), vipCard.getTitle());
		params.put(field.getFIELD_NAME_backgroundColor(), vipCard.getBackgroundColor());
		params.put(field.getFIELD_NAME_clearBonusDay(), vipCard.getClearBonusDay());
		params.put(field.getFIELD_NAME_clearBonusDatetime(), vipCard.getClearBonusDatetime());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCard vipCard = (VipCard) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_iPageIndex(), vipCard.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), vipCard.getPageSize());

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
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_clearBonusDay() + "和" + field.FIELD_NAME_clearBonusDatetime, FIELD_ERROR_clearBonusDay_clearBonusDatetime, sbError) && (clearBonusDay < 1 && clearBonusDatetime == null)) {
			return sbError.toString();
		}
		
		if (printCheckField(field.getFIELD_NAME_backgroundColor() , FIELD_ERROR_backgroundColor, sbError) && !FieldFormat.validate2RGBColor(backgroundColor)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

}
