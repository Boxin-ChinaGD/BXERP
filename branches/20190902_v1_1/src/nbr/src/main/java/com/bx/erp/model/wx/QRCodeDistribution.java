package com.bx.erp.model.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class QRCodeDistribution extends BaseWxModel {

	private static final long serialVersionUID = 1L;

	public static final QRCodeDistributionField field = new QRCodeDistributionField();

	protected String action_name;

	protected int expire_seconds;

	protected QRCodeDistributionActionInfo qrCodeDistributionActionInfo;

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public int getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public QRCodeDistributionActionInfo getQrCodeDistributionActionInfo() {
		return qrCodeDistributionActionInfo;
	}

	public void setQrCodeDistributionActionInfo(QRCodeDistributionActionInfo qrCodeDistributionActionInfo) {
		this.qrCodeDistributionActionInfo = qrCodeDistributionActionInfo;
	}

	@Override
	public String toString() {
		return "QRCodeDistribution [action_name=" + action_name + ", expire_seconds=" + expire_seconds + ", qrCodeDistributionActionInfo=" + qrCodeDistributionActionInfo + "]";
	}

	@Override
	public BaseWxModel clone() throws CloneNotSupportedException {
		QRCodeDistribution qrCodeDistribution = new QRCodeDistribution();
		qrCodeDistribution.setAction_name(action_name);
		qrCodeDistribution.setExpire_seconds(expire_seconds);
		qrCodeDistribution.setQrCodeDistributionActionInfo(qrCodeDistributionActionInfo);
		//
		return qrCodeDistributionActionInfo;
	}

	@Override
	protected BaseWxModel doParse1(JSONObject jo) {
		//
		action_name = jo.getString(field.getFIELD_NAME_action_name());
		expire_seconds = jo.getInt(field.getFIELD_NAME_expire_seconds());
		qrCodeDistributionActionInfo = (QRCodeDistributionActionInfo) new QRCodeDistributionActionInfo().parse1(jo.getString(field.getFIELD_NAME_ALIAS_qrCodeDistributionActionInfo()));
		//
		return this;
	}

	@Override
	public List<BaseWxModel> parseN(String s) {
		List<BaseWxModel> qrCodeDistributionList = new ArrayList<BaseWxModel>();
		JSONArray jsonArray = JSONArray.fromObject(s);
		//
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			QRCodeDistribution qrCodeDistribution = new QRCodeDistribution();
			qrCodeDistribution.doParse1(jsonObject);
			qrCodeDistributionList.add(qrCodeDistribution);
		}
		//
		return qrCodeDistributionList;
	}

	@Override
	public Map<String, Object> getHttpCreateParam(int iUseCaseID, BaseWxModel bwm) {
		checkParameterInput(bwm);
		QRCodeDistribution qrCodeDistribution = (QRCodeDistribution) bwm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_action_name(), qrCodeDistribution.getAction_name());
			params.put(field.getFIELD_NAME_expire_seconds(), qrCodeDistribution.getExpire_seconds());
			params.put(field.getFIELD_NAME_qrCodeDistributionActionInfo(), new QRCodeDistributionActionInfo().getHttpCreateParam(BaseAction.INVALID_ID, qrCodeDistribution.getQrCodeDistributionActionInfo()));
			break;
		}
		//
		return params;
	}
	
	public enum EnumWxQRcodeType {
		EWQRT_QR_SCENE("QR_SCENE", 0), // 临时的整型参数值
		EWQRT_QR_STR_SCENE("QR_STR_SCENE", 1), // 临时的字符串参数值
		EWQRT_QR_LIMIT_SCENE("QR_LIMIT_SCENE", 2), // 永久的整型参数值
		EWQRT_QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE", 3); // 永久的字符串参数值

		private String name;
		private int index;

		private EnumWxQRcodeType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumWxQRcodeType c : EnumWxQRcodeType.values()) {
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
