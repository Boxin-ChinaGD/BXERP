package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CouponCode extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final CouponCodeField field = new CouponCodeField();

	public static final String FIELD_ERROR_VipID = "VipID必须大于0";
	public static final String FIELD_ERROR_SubStatus = "子状态不能是0，1，2，3以外的";

	protected int vipID;

	protected int couponID;

	protected int status;

	protected String SN;

	protected Date usedDatetime;

	protected int subStatus; // 非数据库字段

	public int getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(int subStatus) {
		this.subStatus = subStatus;
	}

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public int getCouponID() {
		return couponID;
	}

	public void setCouponID(int couponID) {
		this.couponID = couponID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public Date getUsedDatetime() {
		return usedDatetime;
	}

	public void setUsedDatetime(Date usedDatetime) {
		this.usedDatetime = usedDatetime;
	}

	@Override
	public String toString() {
		return "CouponCode [vipID=" + vipID + ", couponID=" + couponID + ", status=" + status + ", SN=" + SN + ", usedDatetime=" + usedDatetime + ", subStatus=" + subStatus + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		CouponCode couponCode = new CouponCode();
		couponCode.setID(ID);
		couponCode.setVipID(vipID);
		couponCode.setCouponID(couponID);
		couponCode.setStatus(status);
		couponCode.setSN(SN);
		couponCode.setCreateDatetime((createDatetime == null ? null : (Date) createDatetime.clone()));
		couponCode.setUsedDatetime((usedDatetime == null ? null : (Date) usedDatetime.clone()));
		couponCode.setSubStatus(subStatus);

		return couponCode;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		CouponCode couponCode = (CouponCode) arg0;
		if ((ignoreIDInComparision == true ? true : couponCode.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& couponCode.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID()) //
				&& couponCode.getCouponID() == couponID && printComparator(field.getFIELD_NAME_couponID()) //
				&& couponCode.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
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
			couponID = jo.getInt(field.getFIELD_NAME_couponID());
			status = jo.getInt(field.getFIELD_NAME_status());
			SN = jo.getString(field.getFIELD_NAME_SN());
			subStatus = jo.getInt(field.getFIELD_NAME_subStatus());
			//
			String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!StringUtils.isEmpty(tmp)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			//
			tmp = jo.getString(field.getFIELD_NAME_usedDatetime());
			if (!StringUtils.isEmpty(tmp)) {
				usedDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (usedDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_usedDatetime() + "=" + tmp);
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
		List<BaseModel> couponCodeList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				CouponCode couponCode = new CouponCode();
				couponCode.doParse1(jsonObject1);
				couponCodeList.add(couponCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return couponCodeList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CouponCode couponCode = (CouponCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), couponCode.getVipID());
		params.put(field.getFIELD_NAME_couponID(), couponCode.getCouponID());
		params.put(field.getFIELD_NAME_status(), EnumCouponCodeStatus.ECCS_Normal.getIndex());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CouponCode couponCode = (CouponCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNTotalByVipID:
			params.put(field.getFIELD_NAME_vipID(), couponCode.getVipID());
			break;
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			params.put(field.getFIELD_NAME_vipID(), couponCode.getVipID());
			params.put(field.getFIELD_NAME_subStatus(), couponCode.getSubStatus());
			params.put(field.getFIELD_NAME_iPageIndex(), couponCode.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), couponCode.getPageSize());
			break;
		default:
			params.put(field.getFIELD_NAME_vipID(), (couponCode.getVipID() == 0 ? BaseAction.INVALID_ID : couponCode.getVipID()));
			params.put(field.getFIELD_NAME_couponID(), (couponCode.getCouponID() == 0 ? BaseAction.INVALID_ID : couponCode.getCouponID()));
			params.put(field.getFIELD_NAME_status(), couponCode.getStatus());
			params.put(field.getFIELD_NAME_iPageIndex(), couponCode.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), couponCode.getPageSize());
		}

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CouponCode couponCode = (CouponCode) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_Consume: // 核销卡券
			params.put(field.getFIELD_NAME_ID(), couponCode.getID());
			break;
		default:
			throw new RuntimeException("尚未实现getUpdateParam()方法！");
		}

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
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			if (printCheckField(field.getFIELD_NAME_vipID(), FIELD_ERROR_VipID, sbError) && !(FieldFormat.checkID(vipID))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_subStatus(), FIELD_ERROR_SubStatus, sbError) && subStatus != EnumSubStatusCouponCode.ESSC_All.getIndex() && subStatus != EnumSubStatusCouponCode.ESSC_Consumed.getIndex()
					&& subStatus != EnumSubStatusCouponCode.ESSC_NotConsumed.getIndex() && subStatus != EnumSubStatusCouponCode.ESSC_Outdated.getIndex()) {
				return sbError.toString();
			}
			return "";
		case BaseBO.CASE_CouponCode_retrieveNTotalByVipID:
			if (printCheckField(field.getFIELD_NAME_vipID(), FIELD_ERROR_VipID, sbError) && !(FieldFormat.checkID(vipID))) {
				return sbError.toString();
			}
			return "";
		default:
			return "";
		}
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	public enum EnumCouponCodeStatus {
		ECCS_Normal("Normal", 0), //
		ECCS_Consumed("Consumed", 1);

		private String name;
		private int index;

		private EnumCouponCodeStatus(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCouponCodeStatus c : EnumCouponCodeStatus.values()) {
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

	public enum EnumSubStatusCouponCode {
		ESSC_All("All", 0), // 所有
		ESSC_NotConsumed("NotConsumed", 1), // 正常,未使用
		ESSC_Consumed("Consumed", 2), // 已使用
		ESSC_Outdated("Outdated", 3); // 已过期

		private String name;
		private int index;

		private EnumSubStatusCouponCode(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCouponCodeStatus c : EnumCouponCodeStatus.values()) {
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
}
