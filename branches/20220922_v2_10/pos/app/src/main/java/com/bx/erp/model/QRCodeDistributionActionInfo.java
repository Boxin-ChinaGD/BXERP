package com.bx.erp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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
        try {
            qrCodeDistributionDetail = (QRCodeDistributionDetail) new QRCodeDistributionDetail().parse1(jo.getString(field.getFIELD_NAME_ALIAS_qrCodeDistributionDetail()));
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> qrCodeDistributionActionInfoList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            //
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                QRCodeDistributionActionInfo qrCodeDistributionActionInfo = new QRCodeDistributionActionInfo();
                qrCodeDistributionActionInfo.doParse1(jsonObject);
                qrCodeDistributionActionInfoList.add(qrCodeDistributionActionInfo);
            }
            //
            return qrCodeDistributionActionInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
