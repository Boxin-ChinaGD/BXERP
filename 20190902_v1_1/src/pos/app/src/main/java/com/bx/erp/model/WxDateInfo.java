package com.bx.erp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WxDateInfo extends BaseWxModel {

    private static final long serialVersionUID = 1L;

    public static final WxDateInfoField field = new WxDateInfoField();

    private String type;

    private int begin_timestamp;

    private int end_timestamp;

    private int fixed_term;

    private int fixed_begin_term;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBegin_timestamp() {
        return begin_timestamp;
    }

    public void setBegin_timestamp(int begin_timestamp) {
        this.begin_timestamp = begin_timestamp;
    }

    public int getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(int end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public int getFixed_term() {
        return fixed_term;
    }

    public void setFixed_term(int fixed_term) {
        this.fixed_term = fixed_term;
    }

    public int getFixed_begin_term() {
        return fixed_begin_term;
    }

    public void setFixed_begin_term(int fixed_begin_term) {
        this.fixed_begin_term = fixed_begin_term;
    }

    @Override
    public String toString() {
        return "WxDateInfo [type=" + type + ", begin_timestamp=" + begin_timestamp + ", end_timestamp=" + end_timestamp + ", fixed_term=" + fixed_term + ", fixed_begin_term=" + fixed_begin_term + ", ID=" + ID + "]";
    }

    @Override
    public BaseModel doParse1(JSONObject jo) {
        Object tmp = null;
        try {
            type = jo.getString(field.getFIELD_NAME_type());
            begin_timestamp = jo.getInt(field.getFIELD_NAME_begin_timestamp());
            end_timestamp = jo.getInt(field.getFIELD_NAME_end_timestamp());
            tmp = jo.get(field.getFIELD_NAME_fixed_term());
            if (tmp != null) {
                fixed_term = (int) tmp;
            }
            tmp = jo.get(field.getFIELD_NAME_fixed_begin_term());
            if (tmp != null) {
                fixed_begin_term = (int) tmp;
            }
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        List<BaseModel> wxDateInfoList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxDateInfo wxDateInfo = new WxDateInfo();
                wxDateInfo.doParse1(jsonObject);
                wxDateInfoList.add(wxDateInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return wxDateInfoList;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxDateInfo wxDateInfo = (WxDateInfo) arg0;
        if ((ignoreIDInComparision == true ? true : (wxDateInfo.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxDateInfo.getType().equals(type) && printComparator(field.getFIELD_NAME_type())//
                && wxDateInfo.getBegin_timestamp() == begin_timestamp && printComparator(field.getFIELD_NAME_begin_timestamp())//
                && wxDateInfo.getEnd_timestamp() == end_timestamp && printComparator(field.getFIELD_NAME_end_timestamp())//
                && wxDateInfo.getFixed_term() == fixed_term && printComparator(field.getFIELD_NAME_fixed_term())//
                && wxDateInfo.getFixed_begin_term() == fixed_begin_term && printComparator(field.getFIELD_NAME_fixed_begin_term())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        WxDateInfo wxDateInfo = new WxDateInfo();
        wxDateInfo.setID(ID);
        wxDateInfo.setType(type);
        wxDateInfo.setBegin_timestamp(begin_timestamp);
        wxDateInfo.setEnd_timestamp(end_timestamp);
        wxDateInfo.setFixed_term(fixed_term);
        wxDateInfo.setFixed_begin_term(fixed_begin_term);
        return wxDateInfo;
    }
}
