package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.List;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCouponAbstract extends BaseWxModel {
    private static final long serialVersionUID = 1L;

    private int advancedInfoID;

    private String abstractEx;

    private String iconUrlList;

    public static final WxCouponAbstractField field = new WxCouponAbstractField();

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

    public String getAbstractEx() {
        return abstractEx;
    }

    public void setAbstractEx(String abstractEx) {
        this.abstractEx = abstractEx;
    }

    public String getIconUrlList() {
        return iconUrlList;
    }

    public void setIconUrlList(String iconUrlList) {
        this.iconUrlList = iconUrlList;
    }

    @Override
    public String toString() {
        return "WxCouponAbstract [advancedInfoID=" + advancedInfoID + ", abstractEx=" + abstractEx + ", iconUrlList=" + iconUrlList + ", ID=" + ID + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            advancedInfoID = jo.getInt(field.getFIELD_NAME_advancedInfoID());
            abstractEx = jo.getString(field.getFIELD_NAME_ALIAS_abstractEx());
            iconUrlList = jo.getString(field.getFIELD_NAME_ALIAS_iconUrlList());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCardAbstractList = new ArrayList<BaseWxModel>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponAbstract wxCardAbstract = new WxCouponAbstract();
                wxCardAbstract.doParse1(jsonObject);
                wxCardAbstractList.add(wxCardAbstract);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return wxCardAbstractList;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxCouponAbstract wxCardAbstract = (WxCouponAbstract) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCardAbstract.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCardAbstract.getAdvancedInfoID() == advancedInfoID && printComparator(field.getFIELD_NAME_advancedInfoID())//
                && wxCardAbstract.getAbstractEx().equals(abstractEx) && printComparator(field.getFIELD_NAME_ALIAS_abstractEx())//
                && wxCardAbstract.getIconUrlList().equals(iconUrlList) && printComparator(field.getFIELD_NAME_ALIAS_iconUrlList())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponAbstract wxCardAbstract = new WxCouponAbstract();
        wxCardAbstract.setID(ID);
        wxCardAbstract.setAdvancedInfoID(advancedInfoID);
        wxCardAbstract.setAbstractEx(abstractEx);
        wxCardAbstract.setIconUrlList(iconUrlList);

        return wxCardAbstract;
    }
}
