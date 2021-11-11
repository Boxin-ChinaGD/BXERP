package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.List;

import com.bx.erp.model.BaseWxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CouponStatistics extends BaseWxModel {

    private static final long serialVersionUID = 1L;

    public static final CouponStatisticsField field = new CouponStatisticsField();

    protected String ref_date;

    protected int view_cnt;

    protected int view_user;

    protected int receive_cnt;

    protected int receive_user;

    protected int verify_cnt;

    protected int verify_user;

    protected int given_cnt;

    protected int given_user;

    protected int expire_cnt;

    protected int expire_user;

    protected String card_id;

    protected int card_type;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getRef_date() {
        return ref_date;
    }

    public void setRef_date(String ref_date) {
        this.ref_date = ref_date;
    }

    public int getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(int view_cnt) {
        this.view_cnt = view_cnt;
    }

    public int getView_user() {
        return view_user;
    }

    public void setView_user(int view_user) {
        this.view_user = view_user;
    }

    public int getReceive_cnt() {
        return receive_cnt;
    }

    public void setReceive_cnt(int receive_cnt) {
        this.receive_cnt = receive_cnt;
    }

    public int getReceive_user() {
        return receive_user;
    }

    public void setReceive_user(int receive_user) {
        this.receive_user = receive_user;
    }

    public int getVerify_cnt() {
        return verify_cnt;
    }

    public void setVerify_cnt(int verify_cnt) {
        this.verify_cnt = verify_cnt;
    }

    public int getVerify_user() {
        return verify_user;
    }

    public void setVerify_user(int verify_user) {
        this.verify_user = verify_user;
    }

    public int getGiven_cnt() {
        return given_cnt;
    }

    public void setGiven_cnt(int given_cnt) {
        this.given_cnt = given_cnt;
    }

    public int getGiven_user() {
        return given_user;
    }

    public void setGiven_user(int given_user) {
        this.given_user = given_user;
    }

    public int getExpire_cnt() {
        return expire_cnt;
    }

    public void setExpire_cnt(int expire_cnt) {
        this.expire_cnt = expire_cnt;
    }

    public int getExpire_user() {
        return expire_user;
    }

    public void setExpire_user(int expire_user) {
        this.expire_user = expire_user;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    @Override
    public String toString() {
        return "CouponStatistics [ref_date=" + ref_date + ", view_cnt=" + view_cnt + ", view_user=" + view_user + ", receive_cnt=" + receive_cnt + ", receive_user=" + receive_user + ", verify_cnt=" + verify_cnt + ", verify_user="
                + verify_user + ", given_cnt=" + given_cnt + ", given_user=" + given_user + ", expire_cnt=" + expire_cnt + ", expire_user=" + expire_user + ", card_id=" + card_id + ", card_type=" + card_type + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        try {
            ref_date = jo.getString(field.getFIELD_NAME_ref_date());
            view_cnt = jo.getInt(field.getFIELD_NAME_view_cnt());
            view_user = jo.getInt(field.getFIELD_NAME_view_user());
            receive_cnt = jo.getInt(field.getFIELD_NAME_receive_cnt());
            receive_user = jo.getInt(field.getFIELD_NAME_receive_user());
            verify_cnt = jo.getInt(field.getFIELD_NAME_verify_cnt());
            verify_user = jo.getInt(field.getFIELD_NAME_verify_user());
            given_cnt = jo.getInt(field.getFIELD_NAME_given_cnt());
            given_user = jo.getInt(field.getFIELD_NAME_given_user());
            expire_cnt = jo.getInt(field.getFIELD_NAME_expire_cnt());
            expire_user = jo.getInt(field.getFIELD_NAME_expire_user());
            card_id = jo.getString(field.getFIELD_NAME_card_id());
            card_type = jo.getInt(field.getFIELD_NAME_card_type());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> couponStatisticsList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CouponStatistics couponStatistics = new CouponStatistics();
                couponStatistics.doParse1(jsonObject);
                couponStatisticsList.add(couponStatistics);
            }
            return couponStatisticsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
