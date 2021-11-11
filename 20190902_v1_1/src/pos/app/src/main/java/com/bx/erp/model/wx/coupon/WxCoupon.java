package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCoupon extends BaseWxModel {

    private static final long serialVersionUID = -4275551693303239632L;

    public static final String Http_WxCoupon_RetrieveN = "wxCoupon/queryAvailableWxCouponEx.bx";

    public static final WxCouponField field = new WxCouponField();

    protected String cardID;

    protected WxCouponDetail wxCouponDetail;

    protected String status;

    protected int offset;

    protected int count;

    protected String[] status_list;

    protected int increase_stock_value;

    protected int reduce_stock_value;

    protected String begin_date;

    protected String end_date;

    protected int cond_source;

    protected String code;

    protected String reason;

    public WxCoupon() {
        offset = OFFSET_Default;
        count = COUNT_Default;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getStatus_list() {
        return status_list;
    }

    public void setStatus_list(String[] status_list) {
        this.status_list = status_list;
    }

    public int getIncrease_stock_value() {
        return increase_stock_value;
    }

    public void setIncrease_stock_value(int increase_stock_value) {
        this.increase_stock_value = increase_stock_value;
    }

    public int getReduce_stock_value() {
        return reduce_stock_value;
    }

    public void setReduce_stock_value(int reduce_stock_value) {
        this.reduce_stock_value = reduce_stock_value;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getCond_source() {
        return cond_source;
    }

    public void setCond_source(int cond_source) {
        this.cond_source = cond_source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public WxCouponDetail getWxCouponDetail() {
        return wxCouponDetail;
    }

    public void setWxCouponDetail(WxCouponDetail wxCouponDetail) {
        this.wxCouponDetail = wxCouponDetail;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "WxCoupon [cardID=" + cardID + ", wxCouponDetail=" + wxCouponDetail + ", status=" + status + ", offset=" + offset + ", count=" + count + ", status_list=" + Arrays.toString(status_list) + ", increase_stock_value="
                + increase_stock_value + ", reduce_stock_value=" + reduce_stock_value + ", begin_date=" + begin_date + ", end_date=" + end_date + ", cond_source=" + cond_source + ", code=" + code + ", reason=" + reason + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        //
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            cardID = jo.getString(field.getFIELD_NAME_ALIAS_cardID());
            if (!(jo.getString(field.getFIELD_NAME_ALIAS_wxCouponDetail()).equals("null")) && jo.getString(field.getFIELD_NAME_ALIAS_wxCouponDetail()) != null) {
                wxCouponDetail = (WxCouponDetail) new WxCouponDetail().parse1(jo.getString(field.getFIELD_NAME_ALIAS_wxCouponDetail()));
            }
            status = jo.getString(field.getFIELD_NAME_status());
            offset = jo.getInt(field.getFIELD_NAME_offset());
            count = jo.getInt(field.getFIELD_NAME_count());
            status_list = jo.getString(field.getFIELD_NAME_status()).split(",");
            increase_stock_value = jo.getInt(field.getFIELD_NAME_increase_stock_value());
            reduce_stock_value = jo.getInt(field.getFIELD_NAME_reduce_stock_value());
            begin_date = jo.getString(field.getFIELD_NAME_begin_date());
            end_date = jo.getString(field.getFIELD_NAME_end_date());
            cond_source = jo.getInt(field.getFIELD_NAME_cond_source());
            code = jo.getString(field.getFIELD_NAME_code());
            reason = jo.getString(field.getFIELD_NAME_reason());
            //
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCouponList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCoupon wxCoupon = new WxCoupon();
                wxCoupon.doParse1(jsonObject);
                wxCouponList.add(wxCoupon);
            }
            return wxCouponList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

    @Override
    public int compareTo(BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxCoupon wxCoupon = (WxCoupon) arg0;
        if (ignoreIDInComparision == true ? true
                : (wxCoupon.getID() == ID && printComparator(getFIELD_NAME_ID()))//
                && wxCoupon.getCardID().equals(cardID) && printComparator(field.getFIELD_NAME_cardID())//
                // 这里在创建时只传入cardID和status，所以暂时屏蔽
                // && wxCoupon.getWxCouponCash().compareTo(wxCouponCash) == 0 &&
                // printComparator(field.getFIELD_NAME_wxCouponCash())//
                // && wxCoupon.getWxCouponDiscount().compareTo(wxCouponDiscount) == 0 &&
                // printComparator(field.getFIELD_NAME_wxCouponDiscount())//
                && wxCoupon.getStatus().equals(status) && printComparator(field.getFIELD_NAME_status())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCoupon wxCoupon = new WxCoupon();
        wxCoupon.setID(ID);
        wxCoupon.setCardID(cardID);
        wxCoupon.setWxCouponDetail((wxCouponDetail == null ? null : (WxCouponDetail) wxCouponDetail.clone()));
        wxCoupon.setStatus(status);
        wxCoupon.setOffset(offset);
        wxCoupon.setCount(count);
        wxCoupon.setStatus_list(status_list);
        wxCoupon.setIncrease_stock_value(increase_stock_value);
        wxCoupon.setReduce_stock_value(reduce_stock_value);
        wxCoupon.setBegin_date(begin_date);
        wxCoupon.setEnd_date(end_date);
        wxCoupon.setCond_source(cond_source);
        wxCoupon.setCode(code);
        wxCoupon.setReason(reason);
        //
        return wxCoupon;
    }

}
