package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.StringUtils;
import com.google.gson.Gson;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "RetailTradePromotingFlow")
public class RetailTradePromotingFlow extends BaseModel {
    public static final String FIELD_ERROR_processFlow = "记录过程不能为null或空字符串";
    public static final String FIELD_ERROR_retailTradePromotingID = "retailTradePromotingID必须大于0";
    public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";
    public static final RetailTradePromotingFlowField field = new RetailTradePromotingFlowField();
    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    @NotNull
    @Property(nameInDb = "F_RetailTradePromotingID")
    protected int retailTradePromotingID;

    public int getRetailTradePromotingID() {
        return retailTradePromotingID;
    }

    public void setRetailTradePromotingID(int retailTradePromotingID) {
        this.retailTradePromotingID = retailTradePromotingID;
    }

    @Property(nameInDb = "F_PromotionID")
    protected int promotionID;

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }

    @Property(nameInDb = "F_ProcessFlow")
    protected String processFlow;

    public String getProcessFlow() {
        return processFlow;
    }

    public void setProcessFlow(String processFlow) {
        this.processFlow = processFlow;
    }

    @Property(nameInDb = "F_CreateDatetime")
    protected Date createDatetime;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @Generated(hash = 1555322964)
    public RetailTradePromotingFlow(Long ID, int retailTradePromotingID, int promotionID, String processFlow, Date createDatetime, Date syncDatetime,
            String syncType) {
        this.ID = ID;
        this.retailTradePromotingID = retailTradePromotingID;
        this.promotionID = promotionID;
        this.processFlow = processFlow;
        this.createDatetime = createDatetime;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
    }

    @Generated(hash = 1311722722)
    public RetailTradePromotingFlow() {
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);

        return json;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
        rtpf.setID(this.getID());
        rtpf.setRetailTradePromotingID(this.getRetailTradePromotingID());
        rtpf.setPromotionID(this.getPromotionID());
        rtpf.setProcessFlow(this.getProcessFlow());
        rtpf.setCreateDatetime(this.createDatetime);
        rtpf.setIgnoreIDInComparision(this.ignoreIDInComparision);
        return rtpf;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) arg0;
        if ((ignoreIDInComparision == true ? true : (rtpf.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID())))//
                && rtpf.getRetailTradePromotingID() == this.getRetailTradePromotingID() && printComparator(field.getFIELD_NAME_retailTradePromotingID()) //
                && rtpf.getPromotionID() == this.getPromotionID() && printComparator(field.getFIELD_NAME_promotionID()) //
                && rtpf.getProcessFlow().equals(this.getProcessFlow()) && printComparator(field.getFIELD_NAME_processFlow()) //
            // 不比较创建时间
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "RetailTradePromotingFlow{" +
                "ID=" + ID +
                ", retailTradePromotingID=" + retailTradePromotingID +
                ", promotionID=" + promotionID +
                ", processFlow='" + processFlow + '\'' +
                ", createDatetime=" + createDatetime +
                ", syncDatetime=" + syncDatetime +
                '}';
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 RetailTradePromotingFlow.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            retailTradePromotingID = jo.getInt(field.getFIELD_NAME_retailTradePromotingID());
            promotionID = jo.getInt(field.getFIELD_NAME_promotionID());
            processFlow = jo.getString(field.getFIELD_NAME_processFlow());
            String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
                }
            }

            tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }
            return this;
        } catch (Exception e) {
            System.out.println("RetailTradePromotingFlow.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 RetailTradePromotingFlow.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
                if (rtpf.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(rtpf);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println("RetailTradePromotingFlow.parseN 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 RetailTradePromotingFlow.parse1，s=" + s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            return doParse1(jsonObject);
        } catch (JSONException e) {
            System.out.println("RetailTradePromotingFlow.parse1 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Date getSyncDatetime() {
        return syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_retailTradePromotingID(), FIELD_ERROR_retailTradePromotingID, sbError) && !FieldFormat.checkID(retailTradePromotingID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && !FieldFormat.checkID(promotionID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_processFlow(), FIELD_ERROR_processFlow, sbError) && StringUtils.isEmpty(processFlow)) {
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

        if (this.printCheckField(field.getFIELD_NAME_retailTradePromotingID(), FIELD_ERROR_retailTradePromotingID, sbError) && !FieldFormat.checkID(retailTradePromotingID)) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        return "";
    }
}
