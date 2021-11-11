package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;
import com.google.gson.JsonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCouponAdvancedInfo extends BaseWxModel {

    private static final long serialVersionUID = -4275551693303239632L;

    public static final WxCouponAdvancedInfoField field = new WxCouponAdvancedInfoField();

    private int couponDetailID;

    private WxCouponUseCondition use_condition;

    private WxCouponAbstract abstractEx;

    private List<BaseWxModel> text_image_list;

    private List<BaseWxModel> time_limit;

    private int business_service;

    private int least_cost;

    private String object_use_for;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getCouponDetailID() {
        return couponDetailID;
    }

    public void setCouponDetailID(int couponDetailID) {
        this.couponDetailID = couponDetailID;
    }

    public WxCouponUseCondition getUse_condition() {
        return use_condition;
    }

    public void setUse_condition(WxCouponUseCondition use_condition) {
        this.use_condition = use_condition;
    }

    public WxCouponAbstract getAbstractEx() {
        return abstractEx;
    }

    public void setAbstractEx(WxCouponAbstract abstractEx) {
        this.abstractEx = abstractEx;
    }

    public List<BaseWxModel> getText_image_list() {
        return text_image_list;
    }

    public void setText_image_list(List<BaseWxModel> text_image_list) {
        this.text_image_list = text_image_list;
    }

    public List<BaseWxModel> getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(List<BaseWxModel> time_limit) {
        this.time_limit = time_limit;
    }

    public int getBusiness_service() {
        return business_service;
    }

    public void setBusiness_service(int business_service) {
        this.business_service = business_service;
    }

    public int getLeast_cost() {
        return least_cost;
    }

    public void setLeast_cost(int least_cost) {
        this.least_cost = least_cost;
    }

    public String getObject_use_for() {
        return object_use_for;
    }

    public void setObject_use_for(String object_use_for) {
        this.object_use_for = object_use_for;
    }

    @Override
    public String toString() {
        return "WxCardAdvancedInfo [couponDetailID=" + couponDetailID + ", use_condition=" + use_condition + ", abstractEx=" + abstractEx + ", text_image_list=" + text_image_list + ", time_limit=" + time_limit + ", business_service="
                + business_service + ", least_cost=" + least_cost + ", object_use_for=" + object_use_for + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        Object tmp = null;
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            couponDetailID = jo.getInt(field.getFIELD_NAME_couponDetailID());
            use_condition = (WxCouponUseCondition) new WxCouponUseCondition().parse1(jo.getString(field.getFIELD_NAME_ALIAS_use_condition()));
            //
            tmp = jo.get(field.getFIELD_NAME_ALIAS_abstractEx());
            if (tmp != null && JsonNull.INSTANCE.equals(tmp)) {
                abstractEx = (WxCouponAbstract) new WxCouponAbstract().parse1(jo.getString(field.getFIELD_NAME_ALIAS_abstractEx()));
            }
            text_image_list = (List<BaseWxModel>) new WxCouponTextImage().parseN(jo.getString(field.getFIELD_NAME_text_image_list()));
            time_limit = (List<BaseWxModel>) new WxCouponTimeLimit().parseN(jo.getString(field.getFIELD_NAME_time_limit()));
            tmp = jo.get(field.getFIELD_NAME_least_cost());
            if (tmp != null) {
                least_cost = jo.getInt(field.getFIELD_NAME_least_cost());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_object_use_for());
            if (tmp != null) {
                object_use_for = jo.getString(field.getFIELD_NAME_object_use_for());
            }
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCardAdvancedInfoList = new ArrayList<BaseWxModel>();
        //
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponAdvancedInfo wxCardAdvancedInfo = new WxCouponAdvancedInfo();
                wxCardAdvancedInfo.doParse1(jsonObject);
                wxCardAdvancedInfoList.add(wxCardAdvancedInfo);
            }
            return wxCardAdvancedInfoList;
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
        //
        WxCouponAdvancedInfo wxCardAdvancedInfo = (WxCouponAdvancedInfo) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCardAdvancedInfo.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCardAdvancedInfo.getCouponDetailID() == couponDetailID && printComparator(field.getFIELD_NAME_couponDetailID())//
                && wxCardAdvancedInfo.getBusiness_service() == business_service && printComparator(field.getFIELD_NAME_business_service())//
                && wxCardAdvancedInfo.getLeast_cost() == least_cost && printComparator(field.getFIELD_NAME_least_cost())//
                && wxCardAdvancedInfo.getObject_use_for().equals(object_use_for) && printComparator(field.getFIELD_NAME_object_use_for())//
                ) {
            return 0;
        }
        //
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponAdvancedInfo wxCardAdvancedInfo = new WxCouponAdvancedInfo();
        //
        wxCardAdvancedInfo.setID(ID);
        wxCardAdvancedInfo.setCouponDetailID(couponDetailID);
        wxCardAdvancedInfo.setUse_condition((use_condition == null ? null : (WxCouponUseCondition) use_condition.clone()));
        wxCardAdvancedInfo.setAbstractEx((abstractEx == null ? null : (WxCouponAbstract) abstractEx.clone()));
        wxCardAdvancedInfo.setText_image_list(text_image_list);
        wxCardAdvancedInfo.setTime_limit(time_limit);
        wxCardAdvancedInfo.setBusiness_service(business_service);
        wxCardAdvancedInfo.setLeast_cost(least_cost);
        wxCardAdvancedInfo.setObject_use_for(object_use_for);
        //
        return wxCardAdvancedInfo;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    public enum EnumServiceType {
        ECT_Wifi("BIZ_SERVICE_FREE_WIFI", 0), //
        ECT_Pet("BIZ_SERVICE_WITH_PET", 1), //
        ECT_FreePark("BIZ_SERVICE_FREE_PARK", 2), //
        ECT_Deliver("BIZ_SERVICE_DELIVER", 3);

        private String name;
        private int index;

        private EnumServiceType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumServiceType pt : EnumServiceType.values()) {
                if (pt.getIndex() == index) {
                    return pt.name;
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
