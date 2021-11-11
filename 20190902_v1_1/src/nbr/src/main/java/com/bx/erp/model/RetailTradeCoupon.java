package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RetailTradeCoupon extends BaseModel {
	private static final long serialVersionUID = -5662742449866455682L;

	public static RetailTradeCouponField field = new RetailTradeCouponField();

	protected int retailTradeID;
	protected int couponCodeID;

	public int getRetailTradeID() {
		return retailTradeID;
	}

	public void setRetailTradeID(int retailTradeID) {
		this.retailTradeID = retailTradeID;
	}

	public int getCouponCodeID() {
		return couponCodeID;
	}

	public void setCouponCodeID(int couponCodeID) {
		this.couponCodeID = couponCodeID;
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			retailTradeID = jo.getInt(field.getFIELD_NAME_retailTradeID());
			couponCodeID = jo.getInt(field.getFIELD_NAME_couponCodeID());
			String tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
			if (!StringUtils.isEmpty(tmp)) {
				syncDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (syncDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
				}
			}
			return this;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<?> parseN(JSONArray jsonArray) {
		try {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
				if (retailTradeCoupon.parse1(jsonObject.toString()) == null) {
					return null;
				}
				list.add(retailTradeCoupon);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		RetailTradeCoupon retailtradeCoupon = (RetailTradeCoupon) bm;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(field.getFIELD_NAME_retailTradeID(), retailtradeCoupon.getRetailTradeID());
		param.put(field.getFIELD_NAME_couponCodeID(), retailtradeCoupon.getCouponCodeID());
		return param;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		RetailTradeCoupon retailtradeCoupon = (RetailTradeCoupon) bm;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(field.getFIELD_NAME_retailTradeID(), (retailtradeCoupon.getRetailTradeID() == 0 ? BaseAction.INVALID_ID : retailtradeCoupon.getRetailTradeID()));
		param.put(field.getFIELD_NAME_iPageIndex(), retailtradeCoupon.getPageIndex());
		param.put(field.getFIELD_NAME_iPageSize(), retailtradeCoupon.getPageSize());
		return param;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		RetailTradeCoupon rtc = (RetailTradeCoupon) arg0;
		if ((ignoreIDInComparision == true ? true : (rtc.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rtc.getRetailTradeID() == retailTradeID && printComparator(field.getFIELD_NAME_retailTradeID()) //
				&& rtc.getCouponCodeID() == couponCodeID && printComparator(field.getFIELD_NAME_couponCodeID()) //
//				&& DatetimeUtil.compareDate(rtc.getSyncDatetime(), syncDatetime) && printComparator(field.getFIELD_NAME_syncDatetime()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public BaseModel clone() {
		RetailTradeCoupon rtc = new RetailTradeCoupon();
		rtc.setID(ID);
		rtc.setRetailTradeID(retailTradeID);
		rtc.setCouponCodeID(couponCodeID);
		if (syncDatetime != null) {
			rtc.setSyncDatetime((Date) syncDatetime.clone());
		}
		return rtc;
	}

	@Override
	public String toString() {
		return "RetailTradeCoupon [retailTradeID=" + retailTradeID + ", couponCodeID=" + couponCodeID + ", ID=" + ID + "]";
	}
}
