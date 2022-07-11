package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCouponUseCondition extends BaseWxModel {
    private static final long serialVersionUID = 1L;

    public static final WxCouponUseConditionField field = new WxCouponUseConditionField();

    private int advancedInfoID;

    private String accept_category;

    private String reject_category;

    private int can_use_with_other_discount;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getAdvancedInfoID() {
        return advancedInfoID;
    }

    public void setAdvancedInfoID(int advancedInfoID) {
        this.advancedInfoID = advancedInfoID;
    }

    public String getAccept_category() {
        return accept_category;
    }

    public void setAccept_category(String accept_category) {
        this.accept_category = accept_category;
    }

    public String getReject_category() {
        return reject_category;
    }

    public void setReject_category(String reject_category) {
        this.reject_category = reject_category;
    }

    public int getCan_use_with_other_discount() {
        return can_use_with_other_discount;
    }

    public void setCan_use_with_other_discount(int can_use_with_other_discount) {
        this.can_use_with_other_discount = can_use_with_other_discount;
    }

    @Override
    public String toString() {
        return "WxCardUseCondition [advancedInfoID=" + advancedInfoID + ", accept_category=" + accept_category + ", reject_category=" + reject_category + ", can_use_with_other_discount=" + can_use_with_other_discount + ", ID=" + ID + "]";
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        Object tmp = null;
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            advancedInfoID = jo.getInt(field.getFIELD_NAME_advancedInfoID());
            tmp = jo.get(field.getFIELD_NAME_accept_category());
            if (tmp != null) {
                accept_category = jo.getString(field.getFIELD_NAME_accept_category());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_reject_category());
            if (tmp != null) {
                reject_category = jo.getString(field.getFIELD_NAME_reject_category());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_can_use_with_other_discount());
            if (tmp != null) {
                can_use_with_other_discount = jo.getInt(field.getFIELD_NAME_can_use_with_other_discount());
            }
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCardUseConditionList = new ArrayList<BaseWxModel>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponUseCondition wxCardUseCondition = new WxCouponUseCondition();
                wxCardUseCondition.doParse1(jsonObject);
                wxCardUseConditionList.add(wxCardUseCondition);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return wxCardUseConditionList;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxCouponUseCondition wxCardUseCondition = (WxCouponUseCondition) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCardUseCondition.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCardUseCondition.getAdvancedInfoID() == advancedInfoID && printComparator(field.getFIELD_NAME_advancedInfoID())//
                && wxCardUseCondition.getAccept_category().equals(accept_category) && printComparator(field.getFIELD_NAME_accept_category())//
                && wxCardUseCondition.getReject_category().equals(reject_category) && printComparator(field.getFIELD_NAME_reject_category())//
                && wxCardUseCondition.getCan_use_with_other_discount() == can_use_with_other_discount && printComparator(field.getFIELD_NAME_can_use_with_other_discount())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponUseCondition wxCardUseCondition = new WxCouponUseCondition();
        wxCardUseCondition.setID(ID);
        wxCardUseCondition.setAdvancedInfoID(advancedInfoID);
        wxCardUseCondition.setAccept_category(accept_category);
        wxCardUseCondition.setReject_category(reject_category);
        wxCardUseCondition.setCan_use_with_other_discount(can_use_with_other_discount);

        return wxCardUseCondition;
    }
}
