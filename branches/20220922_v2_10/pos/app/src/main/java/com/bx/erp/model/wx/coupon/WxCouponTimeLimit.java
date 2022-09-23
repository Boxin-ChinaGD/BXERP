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

public class WxCouponTimeLimit extends BaseWxModel {
    private static final long serialVersionUID = 1L;

    public static final WxCouponTimeLimitField field = new WxCouponTimeLimitField();

    private int advancedInfoID;

    private String type;

    private int begin_hour;

    private int end_hour;

    private int begin_minute;

    private int end_minute;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBegin_hour() {
        return begin_hour;
    }

    public void setBegin_hour(int begin_hour) {
        this.begin_hour = begin_hour;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getBegin_minute() {
        return begin_minute;
    }

    public void setBegin_minute(int begin_minute) {
        this.begin_minute = begin_minute;
    }

    public int getEnd_minute() {
        return end_minute;
    }

    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }

    @Override
    public String toString() {
        return "WxCardTimeLimit [advancedInfoID=" + advancedInfoID + ", type=" + type + ", begin_Hour=" + begin_hour + ", end_hour=" + end_hour + ", begin_minute=" + begin_minute + ", end_minute=" + end_minute + ", ID=" + ID + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            advancedInfoID = jo.getInt(field.getFIELD_NAME_advancedInfoID());
            type = jo.getString(field.getFIELD_NAME_type());
            begin_hour = jo.getInt(field.getFIELD_NAME_begin_hour());
            end_hour = jo.getInt(field.getFIELD_NAME_end_hour());
            begin_minute = jo.getInt(field.getFIELD_NAME_begin_minute());
            end_minute = jo.getInt(field.getFIELD_NAME_end_minute());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCardTimeLimitList = new ArrayList<BaseWxModel>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponTimeLimit wxCardTimeLimit = new WxCouponTimeLimit();
                wxCardTimeLimit.doParse1(jsonObject);
                wxCardTimeLimitList.add(wxCardTimeLimit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return wxCardTimeLimitList;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxCouponTimeLimit wxCardTimeLimit = (WxCouponTimeLimit) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCardTimeLimit.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCardTimeLimit.getAdvancedInfoID() == advancedInfoID && printComparator(field.getFIELD_NAME_advancedInfoID())//
                && wxCardTimeLimit.getType().equals(type) && printComparator(field.getFIELD_NAME_type())//
                && wxCardTimeLimit.getBegin_hour() == begin_hour && printComparator(field.getFIELD_NAME_begin_hour())//
                && wxCardTimeLimit.getEnd_hour() == end_hour && printComparator(field.getFIELD_NAME_end_hour())//
                && wxCardTimeLimit.getBegin_minute() == begin_minute && printComparator(field.getFIELD_NAME_begin_minute())//
                && wxCardTimeLimit.getEnd_minute() == end_minute && printComparator(field.getFIELD_NAME_end_minute())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponTimeLimit wxCardTimeLimit = new WxCouponTimeLimit();
        wxCardTimeLimit.setID(ID);
        wxCardTimeLimit.setAdvancedInfoID(advancedInfoID);
        wxCardTimeLimit.setType(type);
        wxCardTimeLimit.setBegin_hour(begin_hour);
        wxCardTimeLimit.setEnd_hour(end_hour);
        wxCardTimeLimit.setBegin_minute(begin_minute);
        wxCardTimeLimit.setEnd_minute(end_minute);

        return wxCardTimeLimit;
    }

    public enum EnumDateType {
        EDT_Monday("MONDAY", 0), //
        EDT_Tuesday("TUESDAY", 1), //
        EDT_Wednesday("WEDNESDAY", 2), //
        EDT_Thursday("THURSDAY", 3), //
        EDT_Friday("FRIDAY", 4), //
        EDT_Saturday("SATURDAY", 5), //
        EDT_Sunday("SUNDAY", 6), //
        EDT_Holiday("HOLIDAY", 7);

        private String name;
        private int index;

        private EnumDateType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumDateType pt : EnumDateType.values()) {
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
