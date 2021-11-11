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

public class WxCouponDetail extends BaseWxModel {

    private static final long serialVersionUID = -4275551693303239632L;

    public static final WxCouponDetailField field = new WxCouponDetailField();

    protected int couponID;

    protected String card_type;

    protected WxCouponDetailPartition wxCouponDetailPartition;

    /**
     * 以下这三个成员变量，在WX的表数据中是存放在WxCouponDetailPartition中的，但是由于我们的数据结构将这两个表合并了，
     * 所以如果Mapper.xml要将数据库表字段和Model字段映射，就必须在该Model增加这三个成员变量。
     */
    protected int least_cost;

    protected int reduce_cost;

    protected int discount;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public WxCouponDetailPartition getWxCouponDetailPartition() {
        return wxCouponDetailPartition;
    }

    public void setWxCouponDetailPartition(WxCouponDetailPartition wxCouponDetailPartition) {
        this.wxCouponDetailPartition = wxCouponDetailPartition;
    }

    public int getLeast_cost() {
        return least_cost;
    }

    public void setLeast_cost(int least_cost) {
        this.least_cost = least_cost;
    }

    public int getReduce_cost() {
        return reduce_cost;
    }

    public void setReduce_cost(int reduce_cost) {
        this.reduce_cost = reduce_cost;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "WxCouponDetail [couponID=" + couponID + ", card_type=" + card_type + ", wxCouponDetailPartition=" + wxCouponDetailPartition + ", least_cost=" + least_cost + ", reduce_cost=" + reduce_cost + ", discount=" + discount + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        //
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            couponID = jo.getInt(field.getFIELD_NAME_couponID());
            card_type = jo.getString(field.getFIELD_NAME_card_type());
//            least_cost = jo.getInt(getFIELD_NAME_least_cost());
//            reduce_cost = jo.getInt(getFIELD_NAME_reduce_cost());
//            discount = jo.getInt(getFIELD_NAME_discount());
            wxCouponDetailPartition = (WxCouponDetailPartition) new WxCouponDetailPartition().parse1(jo.getString(field.getFIELD_NAME_wxCouponDetailPartition()));
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCouponDetailList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < wxCouponDetailList.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponDetail wxCouponDetail = new WxCouponDetail();
                wxCouponDetail.doParse1(jsonObject);
                wxCouponDetailList.add(wxCouponDetail);
            }
            return wxCouponDetailList;
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
        WxCouponDetail wxCouponDetail = (WxCouponDetail) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCouponDetail.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCouponDetail.getCouponID() == couponID && printComparator(field.getFIELD_NAME_couponID())//
                && wxCouponDetail.getCard_type().equals(card_type) && printComparator(field.getFIELD_NAME_card_type())//
                ) {
            return 0;
        }
        //
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponDetail wxCouponDetail = new WxCouponDetail();
        wxCouponDetail.setID(ID);
        wxCouponDetail.setCouponID(couponID);
        wxCouponDetail.setCard_type(card_type);
//        wxCouponDetail.setDiscount(discount);
//        wxCouponDetail.setLeast_cost(least_cost);
//        wxCouponDetail.setReduce_cost(reduce_cost);
        wxCouponDetail.setWxCouponDetailPartition((wxCouponDetailPartition == null ? null : (WxCouponDetailPartition) wxCouponDetailPartition.clone()));
        //
        return wxCouponDetail;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    public enum EnumCouponType {
        ECT_Cash("CASH", 0), //
        ECT_Discount("DISCOUNT", 1);

        private String name;
        private int index;

        private EnumCouponType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCouponType pt : EnumCouponType.values()) {
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
