package com.bx.erp.model.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class QRCodeDistributionActionInfo extends BaseWxModel {

	private static final long serialVersionUID = 1L;

	public static final QRCodeDistributionActionInfoField field = new QRCodeDistributionActionInfoField();

	protected QRCodeDistributionDetail qrCodeDistributionDetail;

	public QRCodeDistributionDetail getQrCodeDistributionDetail() {
		return qrCodeDistributionDetail;
	}

	public void setQrCodeDistributionDetail(QRCodeDistributionDetail qrCodeDistributionDetail) {
		this.qrCodeDistributionDetail = qrCodeDistributionDetail;
	}

	@Override
	public String toString() {
		return "QRCodeDistributionActionInfo [qrCodeDistributionDetail=" + qrCodeDistributionDetail + "]";
	}

	@Override
	public BaseWxModel clone() throws CloneNotSupportedException {
		QRCodeDistributionActionInfo qrCodeDistributionActionInfo = new QRCodeDistributionActionInfo();
		qrCodeDistributionActionInfo.setQrCodeDistributionDetail(qrCodeDistributionDetail);
		//
		return qrCodeDistributionActionInfo;
	}

	@Override
	protected BaseWxModel doParse1(JSONObject jo) {
		//
		qrCodeDistributionDetail = (QRCodeDistributionDetail) new QRCodeDistributionDetail().parse1(jo.getString(field.getFIELD_NAME_ALIAS_qrCodeDistributionDetail()));
		//
		return this;
	}

	@Override
	public List<BaseWxModel> parseN(String s) {
		List<BaseWxModel> qrCodeDistributionActionInfoList = new ArrayList<BaseWxModel>();
		JSONArray jsonArray = JSONArray.fromObject(s);
		//
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			QRCodeDistributionActionInfo qrCodeDistributionActionInfo = new QRCodeDistributionActionInfo();
			qrCodeDistributionActionInfo.doParse1(jsonObject);
			qrCodeDistributionActionInfoList.add(qrCodeDistributionActionInfo);
		}
		//
		return qrCodeDistributionActionInfoList;
	}

	@Override
	public Map<String, Object> getHttpCreateParam(int iUseCaseID, BaseWxModel bwm) {
		checkParameterInput(bwm);
		QRCodeDistributionActionInfo qrCodeDistributionActionInfo = (QRCodeDistributionActionInfo) bwm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_qrCodeDistributionDetail(), new QRCodeDistributionDetail().getHttpCreateParam(BaseAction.INVALID_ID, qrCodeDistributionActionInfo.getQrCodeDistributionDetail()));
			break;
		}

		return params;
	}
}
