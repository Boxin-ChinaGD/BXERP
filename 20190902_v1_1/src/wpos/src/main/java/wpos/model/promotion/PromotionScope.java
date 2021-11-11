package wpos.model.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.model.BaseModel;
import wpos.utils.FieldFormat;

import javax.persistence.*;

@Entity
@Table(name = "T_PromotionScope")
public class PromotionScope extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";
    public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";
    public static final PromotionScopeField field = new PromotionScopeField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    @Column(name = "F_PromotionID")
    protected int promotionID;

//   public String getFIELD_NAME_promotionID() {
//        return "promotionID";
//    }

    @Column(name = "F_CommodityID")
    protected int commodityID;

//    public String getFIELD_NAME_commodityID() {
//        return "commodityID";
//    }

    @Column(name = "F_CommodityName")
    protected String commodityName;

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

    public int getCommodityID() {
        return this.commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
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
                "commodityID=" + commodityID + '\'' +
                '}';
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行PromotionScope.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            commodityID = jo.getInteger(field.getFIELD_NAME_commodityID());
            promotionID = jo.getInteger(field.getFIELD_NAME_promotionID());
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType()); //...
            commodityName = jo.getString(field.getFIELD_NAME_commodityName());

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
                PromotionScope ps = new PromotionScope();
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
        PromotionScope p = new PromotionScope();
        p.setID(this.getID());
        p.setCommodityID(this.getCommodityID());
        p.setPromotionID(this.getPromotionID());
        p.setReturnObject(this.getReturnObject());

        return p;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        PromotionScope p = (PromotionScope) arg0;
        if ((ignoreIDInComparision == true ? true : p.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))//
                && p.getPromotionID() == this.getPromotionID() && printComparator(field.getFIELD_NAME_promotionID()) //
                && p.getCommodityID() == this.getCommodityID() && printComparator(field.getFIELD_NAME_commodityID()) //
                ) {
            return 0;
        }
        return -1;
    }

    public String getCommodityName() {
        return this.commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
        {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) //
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
        if (printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        return "";
    }
}
