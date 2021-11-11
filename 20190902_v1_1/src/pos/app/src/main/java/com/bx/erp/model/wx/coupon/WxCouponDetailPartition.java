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

public class WxCouponDetailPartition extends BaseWxModel {

    private static final long serialVersionUID = 1L;

    public static final WxCouponDetailPartitionField field = new WxCouponDetailPartitionField();

    protected WxCouponBaseInfo base_info;

    protected WxCouponAdvancedInfo advanced_info;

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

    public WxCouponBaseInfo getBase_info() {
        return base_info;
    }

    public void setBase_info(WxCouponBaseInfo base_info) {
        this.base_info = base_info;
    }

    public WxCouponAdvancedInfo getAdvanced_info() {
        return advanced_info;
    }

    public void setAdvanced_info(WxCouponAdvancedInfo advanced_info) {
        this.advanced_info = advanced_info;
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
        return "WxCouponDetailPartition [base_info=" + base_info + ", advanced_info=" + advanced_info + ", least_cost=" + least_cost + ", reduce_cost=" + reduce_cost + ", discount=" + discount + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        //
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            base_info = (WxCouponBaseInfo) new WxCouponBaseInfo().parse1(jo.getString(field.getFIELD_NAME_base_info()));
            if (!(jo.getString(field.getFIELD_NAME_advanced_info()).equals("null")) && jo.getString(field.getFIELD_NAME_advanced_info()) != null) {
                advanced_info = (WxCouponAdvancedInfo) new WxCouponAdvancedInfo().parse1(jo.getString(field.getFIELD_NAME_advanced_info()));
            }
            //
            Object tmp = jo.get(field.getFIELD_NAME_least_cost());
            if (tmp != null) {
                least_cost = jo.getInt(field.getFIELD_NAME_least_cost());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_reduce_cost());
            if (tmp != null) {
                reduce_cost = jo.getInt(field.getFIELD_NAME_reduce_cost());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_discount());
            if (tmp != null) {
                discount = jo.getInt(field.getFIELD_NAME_discount());
            }
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> WxCouponDetailPartitionList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            //
            for (int i = 0; i < WxCouponDetailPartitionList.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponDetailPartition wxCouponDetailPartition = new WxCouponDetailPartition();
                wxCouponDetailPartition.doParse1(jsonObject);
                WxCouponDetailPartitionList.add(wxCouponDetailPartition);
            }
            //
            return WxCouponDetailPartitionList;
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
        WxCouponDetailPartition wxCouponDetailPartition = (WxCouponDetailPartition) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCouponDetailPartition.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCouponDetailPartition.getBase_info().equals(base_info) && printComparator(field.getFIELD_NAME_base_info())//
                && wxCouponDetailPartition.getAdvanced_info().equals(advanced_info) && printComparator(field.getFIELD_NAME_advanced_info())//
                && wxCouponDetailPartition.getLeast_cost() == least_cost && printComparator(field.getFIELD_NAME_least_cost())//
                && wxCouponDetailPartition.getReduce_cost() == reduce_cost && printComparator(field.getFIELD_NAME_reduce_cost())//
                ) {
            return 0;
        }
        //
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponDetailPartition wxCouponDetailPartition = new WxCouponDetailPartition();
        wxCouponDetailPartition.setID(ID);
        wxCouponDetailPartition.setBase_info((base_info == null ? null : (WxCouponBaseInfo) base_info.clone()));
        wxCouponDetailPartition.setAdvanced_info((advanced_info == null ? null : (WxCouponAdvancedInfo) advanced_info.clone()));
        wxCouponDetailPartition.setLeast_cost(least_cost);
        wxCouponDetailPartition.setReduce_cost(reduce_cost);
        wxCouponDetailPartition.setDiscount(discount);
        //
        return wxCouponDetailPartition;
    }
}
