package com.bx.erp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "couponScope")
public class CouponScope extends BaseModel {
    public static final CouponScopeField field = new CouponScopeField();

    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_CouponID")
    protected int couponID;

    @Property(nameInDb = "F_CommodityID")
    protected int commodityID;

    @Property(nameInDb = "F_CommodityName")
    protected String commodityName;

    @Generated(hash = 306983904)
    public CouponScope(Long ID, int couponID, int commodityID, String commodityName) {
        this.ID = ID;
        this.couponID = couponID;
        this.commodityID = commodityID;
        this.commodityName = commodityName;
    }

    @Generated(hash = 214888655)
    public CouponScope() {
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    @Override
    public String toString() {
        return "CouponScope [couponID=" + couponID + ", commodityID=" + commodityID + ", commodityName=" + commodityName + "]";
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        CouponScope couponScope = new CouponScope();
        couponScope.setID(ID);
        couponScope.setCouponID(couponID);
        couponScope.setCommodityID(commodityID);
        couponScope.setCommodityName(commodityName);

        return couponScope;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        CouponScope couponScope = (CouponScope) arg0;
        if ((ignoreIDInComparision == true ? true : couponScope.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
                && couponScope.getCouponID() == couponID && printComparator(field.getFIELD_NAME_couponID()) //
                && couponScope.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
                ) {
            return 0;
        }

        return -1;
    }

    @Override
    public BaseModel doParse1(JSONObject jo) {
        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            couponID = jo.getInt(field.getFIELD_NAME_couponID());
            commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
            commodityName = jo.getString(field.getFIELD_NAME_commodityName());

            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<?> parseN(JSONArray jsonArray) {
        List<BaseModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CouponScope cs = new CouponScope();
                if (cs.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                list.add(cs);
            }
            return list;
        } catch (Exception e) {
            System.out.println("CouponScope.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        List<BaseModel> couponScopeList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                CouponScope couponScope = new CouponScope();
                couponScope.doParse1(jsonObject1);
                couponScopeList.add(couponScope);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return couponScopeList;
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
