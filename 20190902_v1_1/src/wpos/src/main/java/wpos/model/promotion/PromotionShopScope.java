package wpos.model.promotion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.model.BaseModel;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_PromotionShopScope")
public class PromotionShopScope extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ERROR_shopID = "shopID必须大于0";
    public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";
    public static final PromotionShopScopeField field = new PromotionShopScopeField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    @Column(name = "F_PromotionID")
    protected int promotionID;

//   public String getFIELD_NAME_promotionID() {
//        return "promotionID";
//    }

    @Column(name = "F_ShopID")
    protected int shopID;

//    public String getFIELD_NAME_commodityID() {
//        return "commodityID";
//    }

    @Column(name = "F_ShopName")
    protected String shopName;

//    public String getFIELD_NAME_commodityName() {
//        return "commodityName";
//    }

    @Column(name = "F_SyncType")
    protected String syncType;

//    public String getFIELD_NAME_syncType() {
//        return "syncType";
//    }
//
//    //@Transient
//    protected String FIELD_NAME_syncType;

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

//    public String getFIELD_NAME_syncDatetime() {
//        return "syncDatetime";
//    }
//
//    //@Transient
//    protected String FIELD_NAME_syncDatetime;

    public Integer getID() {
        return this.ID;
    }

    public void setID(Integer ID) {
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

    //@Generated(hash = 1723534396)
//    public PromotionScope() {
//    }

    //@Generated(hash = 928851515)
//    public PromotionScope(Long ID, int promotionID, int commodityID, String commodityName, String syncType,
//            Date syncDatetime) {
//        this.ID = ID;
//        this.promotionID = promotionID;
//        this.commodityID = commodityID;
//        this.commodityName = commodityName;
//        this.syncType = syncType;
//        this.syncDatetime = syncDatetime;
//    }

    @Override
    public String toString() {
        return "promotionScope{" +
                "ID=" + ID + '\'' +
                ", promotionID='" + promotionID + '\'' +
                "shopID=" + shopID + '\'' +
                '}';
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行PromotionScope.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            shopID = jo.getInteger(field.getFIELD_NAME_shopID());
            promotionID = jo.getInteger(field.getFIELD_NAME_promotionID());
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType()); //...
            shopName = jo.getString(field.getFIELD_NAME_shopName());

            return this;
        } catch (JSONException e) {
            System.out.println("PromotionScope.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行PromotionScope.parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            System.out.println("PromotionScope.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行PromotionScope.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<BaseModel>();
        try {
            for (int i = 0;i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PromotionShopScope ps = new PromotionShopScope();
                if (ps.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(ps);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println("PromotionScope.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        PromotionShopScope p = new PromotionShopScope();
        p.setID(this.getID());
        p.setShopID(this.getShopID());
        p.setPromotionID(this.getPromotionID());
        p.setReturnObject(this.getReturnObject());
        p.setShopName(this.getShopName());
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
        if (!StringUtils.isEmpty(sql)) {
            int total = StringUtils.contain(sql, "%s");
            if (this.printCheckField(field.getFIELD_NAME_sql(), FIELD_ERROR_SQL, sbError) && conditions != null && total != conditions.length) {
                return sbError.toString();
            }
        }
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
