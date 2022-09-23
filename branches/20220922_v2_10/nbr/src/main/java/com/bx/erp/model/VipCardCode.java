package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.bo.BaseBO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VipCardCode extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final VipCardCodeField field = new VipCardCodeField();

	public static final int SN_LENGTH = 16;

	public static final String FIELD_ERROR_SNLength = "会员卡号长度规定为" + SN_LENGTH + "位";
	public static final String FIELD_ERROR_SN = "会员卡卡号非空";

	protected int vipID;

	protected int vipCardID;

	protected String SN;

	// 公司编号用来传入到SP中拼接成会员卡的SN
	protected String companySN;

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public int getVipCardID() {
		return vipCardID;
	}

	public void setVipCardID(int vipCardID) {
		this.vipCardID = vipCardID;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getCompanySN() {
		return companySN;
	}

	public void setCompanySN(String companySN) {
		this.companySN = companySN;
	}

	@Override
	public String toString() {
		return "VipCardCode [vipID=" + vipID + ", vipCardID=" + vipCardID + ", SN=" + SN + ", companySN=" + companySN + ", ID=" + ID + ", createDatetime=" + createDatetime + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		VipCardCode vipCardCode = new VipCardCode();
		vipCardCode.setID(ID);
		vipCardCode.setVipCardID(vipCardID);
		vipCardCode.setVipID(vipID);
		vipCardCode.setSN(SN);
		vipCardCode.setCompanySN(companySN);
		vipCardCode.setCreateDatetime(createDatetime);

		return vipCardCode;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		VipCardCode vipCardCode = (VipCardCode) arg0;
		if ((ignoreIDInComparision == true ? true : vipCardCode.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& vipCardCode.getVipCardID() == vipCardID && printComparator(field.getFIELD_NAME_vipCardID()) //
				&& vipCardCode.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID()) //
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
			vipCardID = jo.getInt(field.getFIELD_NAME_vipCardID());
			SN = jo.getString(field.getFIELD_NAME_SN());

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> vipCardCodeList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				VipCardCode vipCardCode = new VipCardCode();
				vipCardCode.doParse1(jsonObject1);
				vipCardCodeList.add(vipCardCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return vipCardCodeList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCardCode vipCardCode = (VipCardCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), vipCardCode.getVipID());
		params.put(field.getFIELD_NAME_vipCardID(), vipCardCode.getVipCardID());
		params.put(field.getFIELD_NAME_companySN(), vipCardCode.getCompanySN());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		VipCardCode vipCardCode = (VipCardCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), vipCardCode.getVipID());
		params.put(field.getFIELD_NAME_iPageIndex(), vipCardCode.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), vipCardCode.getPageSize());

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
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_VipCardCode_ImportFromOldSystem:
			if (printCheckField(field.getFIELD_NAME_SN(), FIELD_ERROR_SNLength, sbError) && (SN == null || SN.length() != SN_LENGTH)) {
				return sbError.toString();
			}
			return "";
		default:
			if (printCheckField(field.getFIELD_NAME_SN(), FIELD_ERROR_SN, sbError) && !StringUtils.isEmpty(SN)) {
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
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}

}
