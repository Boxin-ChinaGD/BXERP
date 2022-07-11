package com.bx.erp.model;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.utils.FieldFormat;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class PromotionShopScope extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ERROR_shopID = "shopID必须大于0";
    public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";
    public static final PromotionShopScopeField field = new PromotionShopScopeField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_PromotionID")
    protected int promotionID;

//   public String getFIELD_NAME_promotionID() {
//        return "promotionID";
//    }

    @Property(nameInDb = "F_ShopID")
    protected int shopID;

//    public String getFIELD_NAME_commodityID() {
//        return "commodityID";
//    }

    @Property(nameInDb = "F_ShopName")
    protected String shopName;

//    public String getFIELD_NAME_commodityName() {
//        return "commodityName";
//    }

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

//    public String getFIELD_NAME_syncType() {
//        return "syncType";
//    }
//
//    @Transient
//    protected String FIELD_NAME_syncType;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

//    public String getFIELD_NAME_syncDatetime() {
//        return "syncDatetime";
//    }
//
//    @Transient
//    protected String FIELD_NAME_syncDatetime;

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getPromotionID() {
        return this.promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    public int getShopID() {
        return this.shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getSyncType() {
        return this.syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    public PromotionShopScope() {
    }

    @Generated(hash = 1402808138)
    public PromotionShopScope(Long ID, int promotionID, int shopID, String shopName, String syncType, Date syncDatetime) {
        this.ID = ID;
        this.promotionID = promotionID;
        this.shopID = shopID;
        this.shopName = shopName;
        this.syncType = syncType;
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String toString() {
        return "promotionShopScope{" +
                "ID=" + ID + '\'' +
                ", promotionID='" + promotionID + '\'' +
                "commodityID=" + shopID + '\'' +
                '}';
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行PromotionShopScope.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            shopID = jo.getInt(field.getFIELD_NAME_shopID());
            promotionID = jo.getInt(field.getFIELD_NAME_promotionID());
            syncSequence = jo.getInt(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType()); //...
            shopName = jo.getString(field.getFIELD_NAME_shopName());

            return this;
        } catch (JSONException e) {
            System.out.println("PromotionShopScope.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行PromotionShopScope.parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("PromotionShopScope.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行PromotionShopScope.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PromotionShopScope ps = new PromotionShopScope();
                if (ps.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(ps);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println("PromotionShopScope.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        PromotionShopScope p = new PromotionShopScope();
        p.setID(this.getID());
        p.setShopID(this.getShopID());
        p.setPromotionID(this.getPromotionID());
        p.setReturnObject(this.getReturnObject());

        return p;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        PromotionShopScope p = (PromotionShopScope) arg0;
        if ((ignoreIDInComparision == true ? true : p.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))//
                && p.getPromotionID() == this.getPromotionID() && printComparator(field.getFIELD_NAME_promotionID()) //
                && p.getShopID() == this.getShopID() && printComparator(field.getFIELD_NAME_shopID()) //
                ) {
            return 0;
        }
        return -1;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
        {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_PromotionShopScope_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_Int:
                        if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(Integer.valueOf(conditions[0]))) //
                        {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Ignore:
                        break;
                }
                break;
            default:
                return "";
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        return "";
    }
}
