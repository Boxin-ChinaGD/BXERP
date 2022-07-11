package com.bx.erp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WxSubMerchantInfo extends BaseWxModel {
    private static final long serialVersionUID = 1L;

    public static final WxSubMerchantInfoField field = new WxSubMerchantInfoField();

    private int merchant_id;

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    @Override
    public String toString() {
        return "WxSubMerchantInfo [merchant_id=" + merchant_id + "]";
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        WxSubMerchantInfo wxSubMerchantInfo = new WxSubMerchantInfo();
        wxSubMerchantInfo.setMerchant_id(merchant_id);
        return wxSubMerchantInfo;
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        Object merchantID = null;
        try {
            merchantID = jo.get(field.getFIELD_NAME_merchant_id());
            if (merchantID != null) {
                merchant_id = (int) merchantID;
            }
            return this;
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
        WxSubMerchantInfo wxVipCardBaseInfo = (WxSubMerchantInfo) arg0;
        if ((ignoreIDInComparision == true ? true : wxVipCardBaseInfo.getID() == ID && printComparator(getFIELD_NAME_ID())) //
                && wxVipCardBaseInfo.getMerchant_id() == merchant_id && printComparator(field.getFIELD_NAME_merchant_id())//
                ) {
            return 0;
        }
        return -1;
    }
}
