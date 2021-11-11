package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity(nameInDb = "retailTradeCoupon")
public class RetailTradeCoupon extends BaseModel {
    public static RetailTradeCouponField field = new RetailTradeCouponField();
    public static final int MIN_ID = 0;

    public static final String FIELD_ERROR_RETAILTRADEID = "retailTradeID必须大于" + MIN_ID;
    public static final String FIELD_ERROR_COUPONCODEID = "couponID必须大于" + MIN_ID;

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_RetailTradeID")
    protected int retailTradeID;

    @Property(nameInDb = "F_CouponCodeID")
    protected int couponCodeID;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Generated(hash = 77196312)
    public RetailTradeCoupon(Long ID, int retailTradeID, int couponCodeID, Date syncDatetime) {
        this.ID = ID;
        this.retailTradeID = retailTradeID;
        this.couponCodeID = couponCodeID;
        this.syncDatetime = syncDatetime;
    }

    @Generated(hash = 1929714746)
    public RetailTradeCoupon() {
    }

    public int getRetailTradeID() {
        return retailTradeID;
    }

    public void setRetailTradeID(int retailTradeID) {
        this.retailTradeID = retailTradeID;
    }

    public int getCouponCodeID() {
        return couponCodeID;
    }

    public void setCouponCodeID(int couponCodeID) {
        this.couponCodeID = couponCodeID;
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            retailTradeID = jo.getInt(field.getFIELD_NAME_retailTradeID());
            String tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }
            return this;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        try {
            List<BaseModel> list = new ArrayList<BaseModel>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
                if (retailTradeCoupon.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                list.add(retailTradeCoupon);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_retailTradeID(), FIELD_ERROR_RETAILTRADEID, sbError) && !FieldFormat.checkID(retailTradeID)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_couponCodeID(), FIELD_ERROR_COUPONCODEID, sbError) && !FieldFormat.checkID(couponCodeID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        RetailTradeCoupon rtc = (RetailTradeCoupon) arg0;
        if ((ignoreIDInComparision == true ? true : (rtc.getID().equals(ID) && printComparator(field.getFIELD_NAME_ID()))) //
                && rtc.getRetailTradeID() == retailTradeID && printComparator(field.getFIELD_NAME_retailTradeID()) //
                && rtc.getCouponCodeID() == couponCodeID && printComparator(field.getFIELD_NAME_couponCodeID()) //
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        RetailTradeCoupon rtc = new RetailTradeCoupon();
        rtc.setID(ID);
        rtc.setRetailTradeID(retailTradeID);
        rtc.setCouponCodeID(couponCodeID);
        if (this.getSyncDatetime() != null) {
            rtc.setSyncDatetime((Date) this.getSyncDatetime().clone());//注意此处。必须clone！
        }
        return rtc;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }
}
