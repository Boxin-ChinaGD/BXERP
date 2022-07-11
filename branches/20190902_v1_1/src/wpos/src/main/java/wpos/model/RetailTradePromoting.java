package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 记录一张零售单计算应收款的过程，让客户知道为什么收这么多钱。
 */
@Entity
@Table(name = "T_RetailTradePromoting")
public class RetailTradePromoting extends BaseModel {
    public static final String FIELD_ERROR_tradeID = "tradeID必须大于0";

    //@Transient
    public static final String HTTP_RetailTradePromoting_Create = "retailTradePromoting/createEx.bx";
    //@Transient
    public static final String HTTP_RetailTradePromoting_CreateN = "retailTradePromotingSync/createNEx.bx";

    public static final String HTTP_REQ_PARAMETER_JSON = "json";
    public static final RetailTradePromotingField field = new RetailTradePromotingField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_TradeID")
    protected int tradeID;

    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

//    public List<RetailTradePromotingFlow> getListRetailTradePromotingFlow() {
//        return listRetailTradePromotingFlow;
//    }
//
//    public void setListRetailTradePromotingFlow(List<RetailTradePromotingFlow> listRetailTradePromotingFlow) {
//        this.listRetailTradePromotingFlow = listRetailTradePromotingFlow;
//    }

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

    @Column(name = "F_SyncType")
    protected String syncType;

    //@Generated(hash = 867434489)
//    public RetailTradePromoting(Long ID, int tradeID, Date createDatetime, Date syncDatetime, String syncType) {
//        this.ID = ID;
//        this.tradeID = tradeID;
//        this.createDatetime = createDatetime;
//        this.syncDatetime = syncDatetime;
//        this.syncType = syncType;
//    }

    //@Generated(hash = 1203029260)
//    public RetailTradePromoting() {
//    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getTradeID() {
        return this.tradeID;
    }

    public void setTradeID(int tradeID) {
        this.tradeID = tradeID;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }


    public BaseModel clone() throws CloneNotSupportedException {
        RetailTradePromoting rp = new RetailTradePromoting();
        rp.setID(this.getID());
        rp.setTradeID(this.getTradeID());
        rp.setCreateDatetime(this.getCreateDatetime());
        rp.setIgnoreIDInComparision(ignoreIDInComparision);
        if (this.getListSlave1() != null && this.getListSlave1().size() > 0) {
            List<RetailTradePromotingFlow> list = new ArrayList<RetailTradePromotingFlow>();
            for (Object retailTradePromotingFlow : this.getListSlave1()) {
                RetailTradePromotingFlow retailTradePromoting = (RetailTradePromotingFlow) retailTradePromotingFlow;
                list.add((RetailTradePromotingFlow) retailTradePromoting.clone());
            }
            rp.setListSlave1(list);
        }
        return rp;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        RetailTradePromoting retailTradePromoting = (RetailTradePromoting) arg0;
        if ((ignoreIDInComparision == true ? true : (retailTradePromoting.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && retailTradePromoting.getTradeID() == (this.getTradeID()) && printComparator(field.getFIELD_NAME_tradeID()) //
        ) {
            return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "RetailTradePromoting [ID=" + ID + ", listSlave1=" + getListSlave1() + ", tradeID=" + tradeID + ", createDatetime=" + createDatetime + ", syncDatetime=" + syncDatetime + "]";
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 RetailTradePromoting.parse1() ，s=" + s);

        try {
            JSONObject joRT = JSONObject.parseObject(s);
            RetailTradePromoting rtp = (RetailTradePromoting) doParse1(joRT);
            if (rtp == null) {
                return null;
            }
            JSONArray rtcArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave1());//
            if (rtcArr != null && rtcArr.size() > 0) {
                RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
                List<RetailTradePromotingFlow> listRtpf = (List<RetailTradePromotingFlow>) rtpf.parseN(rtcArr);
                if (listRtpf == null) {
                    return null;
                }
                rtp.setListSlave1(listRtpf); // 非常关键！！
            }

            return rtp;
        } catch (JSONException e) {
            System.out.println("RetailTradePromoting.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 RetailTradePromoting.doParse1() ，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            tradeID = jo.getInteger(field.getFIELD_NAME_tradeID());
            String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (createDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
                }
            }

            tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }
            return this;
        } catch (Exception e) {
            System.out.println("RetailTradePromoting.doParse1() 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 RetailTradePromoting.parseN() ，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtpList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTradePromoting rtp = new RetailTradePromoting();
                if (rtp.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtpList.add(rtp);
            }
            return rtpList;
        } catch (Exception e) {
            System.out.println("RetailTradePromoting.parseN() 出现异常，错误信息为" + e.getMessage());

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

    /**
     * 用于删除一些JSON当中不需要的属性。
     * 原因：在上传一批零售单时调用HttpBO需要把List转为JSON格式，此时的JSON数据中有很多的不需要的数据。从而导致服务器会解析失败
     */
    public String removeJSONArrayofAttribute(String jsonData) {
        System.out.println("正在执行 RetailTradePromoting.removeJSONArrayofAttribute() ，jsonData=" + jsonData);

        try {
            JSONArray jsonArray = JSONArray.parseArray(jsonData);
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.remove(field.getFIELD_NAME_TOLERANCE());
                jsonObject.remove(field.getFIELD_NAME_conditions());
                jsonObject.remove(field.getFIELD_NAME_sql());
                jsonObject.remove(field.getFIELD_NAME_ignoreIDInComparision());

                JSONArray jsonArrayRTPF = jsonObject.getJSONArray(field.getFIELD_NAME_listSlave1());
                for (int j = 0; j < jsonArrayRTPF.size(); j++) {
                    JSONObject jsonObjectRTPF = jsonArrayRTPF.getJSONObject(j);
                    jsonObjectRTPF.remove(field.getFIELD_NAME_TOLERANCE());
                    jsonObjectRTPF.remove(field.getFIELD_NAME_conditions());
                    jsonObjectRTPF.remove(field.getFIELD_NAME_sql());
                    jsonObjectRTPF.remove(field.getFIELD_NAME_ignoreIDInComparision());
                }
            }
            return jsonArray.toString();
        } catch (Exception e) {
            System.out.println("RetailTradePromoting.removeJSONArrayofAttribute() 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于删除一些JSON当中不需要的属性。
     * 原因：在创建零售单时调用HttpBO需要把此时的JSON数据中有很多不需要的数据remove。从而导致服务器会解析失败
     */
    public String removeJSONofAttribute(String jsonData) {
        System.out.println("正在执行 RetailTradePromoting.removeJSONofAttribute() ，jsonData=" + jsonData);

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            jsonObject.remove(field.getFIELD_NAME_TOLERANCE());
            jsonObject.remove(field.getFIELD_NAME_conditions());
            jsonObject.remove(field.getFIELD_NAME_sql());
            jsonObject.remove(field.getFIELD_NAME_ignoreIDInComparision());

            JSONArray jsonArrayRTPF = jsonObject.getJSONArray(field.getFIELD_NAME_listSlave1());
            for (int j = 0; j < jsonArrayRTPF.size(); j++) {
                JSONObject jsonObjectRTPF = jsonArrayRTPF.getJSONObject(j);
                jsonObjectRTPF.remove(field.getFIELD_NAME_TOLERANCE());
                jsonObjectRTPF.remove(field.getFIELD_NAME_conditions());
                jsonObjectRTPF.remove(field.getFIELD_NAME_sql());
                jsonObjectRTPF.remove(field.getFIELD_NAME_ignoreIDInComparision());
            }
            return jsonObject.toString();
        } catch (Exception e) {
            System.out.println("执行removeJSONofAttribute()出现异常 ，错误信息为" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_tradeID, sbError) && !FieldFormat.checkID(tradeID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_tradeID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        return "";
    }
}
