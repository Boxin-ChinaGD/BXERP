package com.bx.erp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QRCodeDistributionDetail extends BaseWxModel {

    private static final long serialVersionUID = 1L;

    public static final QRCodeDistributionDetailField field = new QRCodeDistributionDetailField();

    protected long ID;

    protected String card_id;

    protected String code;

    protected String openid;

    protected boolean is_unique_code;

    protected String outer_str;

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public boolean getIs_unique_code() {
        return is_unique_code;
    }

    public void setIs_unique_code(boolean is_unique_code) {
        this.is_unique_code = is_unique_code;
    }

    public String getOuter_str() {
        return outer_str;
    }

    public void setOuter_str(String outer_str) {
        this.outer_str = outer_str;
    }

    @Override
    public String toString() {
        return "QRCodeDistributionDetail [card_id=" + card_id + ", code=" + code + ", openid=" + openid + ", is_unique_code=" + is_unique_code + ", outer_str=" + outer_str + "]";
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        QRCodeDistributionDetail qrCodeDistributionDetail = new QRCodeDistributionDetail();
        qrCodeDistributionDetail.setCard_id(card_id);
        qrCodeDistributionDetail.setCode(code);
        qrCodeDistributionDetail.setOpenid(openid);
        qrCodeDistributionDetail.setIs_unique_code(is_unique_code);
        qrCodeDistributionDetail.setOuter_str(outer_str);
        //
        return qrCodeDistributionDetail;
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        //
        try {
            card_id = jo.getString(field.getFIELD_NAME_card_id());
            code = jo.getString(field.getFIELD_NAME_code());
            openid = jo.getString(field.getFIELD_NAME_openid());
            is_unique_code = jo.getBoolean(field.getFIELD_NAME_is_unique_code());
            outer_str = jo.getString(field.getFIELD_NAME_outer_str());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> qrCodeDistributionDetailList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            //
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                QRCodeDistributionDetail qrCodeDistributionDetail = new QRCodeDistributionDetail();
                qrCodeDistributionDetail.doParse1(jsonObject);
                qrCodeDistributionDetailList.add(qrCodeDistributionDetail);
            }
            //
            return qrCodeDistributionDetailList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        QRCodeDistributionDetail qrCodeDistributionDetail = (QRCodeDistributionDetail) arg0;
        if (ignoreIDInComparision == true ? true
                : (qrCodeDistributionDetail.getID() == ID && printComparator(getFIELD_NAME_ID()))//
                && qrCodeDistributionDetail.getCard_id().equals(card_id) && printComparator(field.getFIELD_NAME_card_id())//
                && qrCodeDistributionDetail.getCode().equals(code) && printComparator(field.getFIELD_NAME_code())//
                && qrCodeDistributionDetail.getOpenid().equals(openid) && printComparator(field.getFIELD_NAME_openid())//
                && qrCodeDistributionDetail.getIs_unique_code() == is_unique_code && printComparator(field.getFIELD_NAME_is_unique_code())//
                && qrCodeDistributionDetail.getOuter_str().equals(outer_str) && printComparator(field.getFIELD_NAME_outer_str())//
                ) {
            return 0;
        }
        return -1;
    }
}
