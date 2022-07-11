package wpos.model.promotion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.PromotionField;
import wpos.presenter.BasePresenter;
//import wpos.model.promotion.BasePromotion;
import wpos.utils.DatetimeUtil;
import wpos.utils.FieldFormat;
import wpos.utils.GeneralUtil;
import wpos.utils.StringUtils;
//import com.google.gson.Gson;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.NotNull;
//import org.greenrobot.greendao.annotation.Column;
//import org.greenrobot.greendao.annotation.Transient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import static wpos.model.Promotion.EnumTypePromotion.ETP_DecreaseOnAmount;

@Entity
@Table(name = "T_Promotion")
public class Promotion extends BaseModel {
    public static final int Max_LengthName = 32;
    public static final int Max_excecutionDiscount = 1;
    public static final int MAX_ExcecutionThreshold = 10000;
    public static final int MAX_ExcecutionAmount = 10000;

    public static final String FIELD_ERROR_startDatetime = "活动开始时间必须至少从明天开始！";
    public static final String FIELD_ERROR_excecutionThreshold = "阀值只能在1~" + MAX_ExcecutionThreshold + "之间";
    public static final String FIELD_ERROR_excecutionAmount = "满减金额只能在1~" + MAX_ExcecutionAmount + "之间";
    public static final String FIELD_ERROR_excecutionAmount_excecutionThreshold = "阀值需大于或等于满减金额！";
    public static final String FIELD_ERROR_excecutionDiscount = "折扣需大于0，小于等于" + Max_excecutionDiscount;
    public static final String FIELD_ERROR_datetimeStart_datetimeEnd = "开始时间必须在结束时间之前！";
    public static final String FIELD_ERROR_type = "不是满减满折类型,类型必须是" + EnumTypePromotion.ETP_DecreaseOnAmount.getIndex() + "或" + EnumTypePromotion.ETP_DiscountOnAmount.getIndex() + "！";
    public static final String FIELD_ERROR_name = "名称长度不能大于" + Max_LengthName + "位且不能为null或空字符串！";
    public static final String FIELD_ERROR_scope = "参与的商品的范围值必须是" + BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex() + "或" + BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities.getIndex() + "！";
    public static final String FIELD_ERROR_status = "状态必须是" + EnumStatusPromotion.ESP_Active.getIndex() + "或" + EnumStatusPromotion.ESP_Deleted.getIndex() + "！";
    public static final String FIELD_ERROR_statusForRetrieveN = "状态必须是" + EnumStatusPromotion.ESP_Active.getIndex() + "或" + EnumStatusPromotion.ESP_Deleted.getIndex() + "或" + BaseSQLiteBO.INVALID_STATUS + "！";
    public static final String FIELD_ERROR_nameForRetrieveN = "名称长度不能大于" + Max_LengthName + "位";
    public static final String FIELD_ERROR_staff = "店员ID必须大于0";

    /**
     * 返回所有状态为0的促销活动
     */
    public static final int ACTIVE = 0;
    /** 返回所有状态为0且未开始的促销活动 */
    public static final int ACTIVE_ButNotYetStarted = 10;
    /** 返回所有状态为0且进行中的促销活动 */
    public static final int ACTIVE_And_Working = 11;
    /** 返回所有状态为0且已结束的促销活动 */
    public static final int ACTIVE_ButEnded = 12;
    /** 查询进行中还有将要进行的 */
    public static final int WORKING_And_ToWork = 13;

    ////@Transient
    public static final String HTTP_Promotion_Create = "promotionSync/createEx.bx";
    ////@Transient
    public static final String feedbackURL_FRONT = "promotionSync/feedbackEx.bx?sID=";
    ////@Transient
    public static final String feedbackURL_BEHINE = "&errorCode=EC_NoError";
    ////@Transient
    public static final String HTTP_Promotion_RetrieveN = "promotionSync/retrieveNEx.bx";
    ////@Transient
    public static final String HTTP_Promotion_RetrieveNC = "promotion/retrieveNEx.bx?subStatusOfStatus=";
    ////@Transient
    public static final String HTTP_Promotion_RETRIEVENC_PageIndex = "&pageIndex=";
    ////@Transient
    public static final String HTTP_Promotion_RETRIEVENC_PageSize = "&pageSize=";
    ////@Transient
    public static final String HTTP_Promotion_Update = "promotionSync/updateEx.bx"; //...nbr已无该接口
    public static final PromotionField field = new PromotionField();
    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Type")
    protected int type;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    //@NotNull
    @Column(name = "F_Status")
    protected int status;

    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

//    //@Column(name = "F_UpdateDatetime")
//    protected Date updateDatetime;

//    //@Column(name = "F_Approver")
//    ////@Transient
//    protected String approver;

//    //@Column(name = "F_ApproveDatetime")
//    ////@Transient
//    protected Date approveDatetime;

    //@NotNull
    @Column(name = "F_DatetimeStart")
    protected Date datetimeStart;

    //@NotNull
    @Column(name = "F_DatetimeEnd")
    protected Date datetimeEnd;

    //@NotNull
    @Column(name = "F_SN")
    protected String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

//    //@Column(name = "F_ExecuteWeekday")
//    ////@Transient
//    protected String executeWeekday;


//    //@Column(name = "F_ExecuteStartDatetime")
//////@Transient
//    protected Date executeStartDatetime;

//    //@Column(name = "F_ExecuteEndDatetime")
//////@Transient
//    protected Date executeEndDatetime;

//    //@Column(name = "F_Remark")
//    ////@Transient
//    protected String remark;

//    //@Column(name = "F_DiscountUponVIP")
//////@Transient
//    protected String discountUponVIP;

//    //@Column(name = "F_SpecialOfferInvolved")
//////@Transient
//    protected String specialOfferInvolved;


//    //@Column(name = "F_Times")
//////@Transient
//    protected String times;


//    //@Column(name = "F_VIPBornDayOrMonth")
//////@Transient
//    protected String vIPBornDayOrMonth;

//    //@Column(name = "F_WholeShop")
//////@Transient
//    protected String wholeShop;


//    //@Column(name = "F_NOReached")
//    ////@Transient
//    protected String NOReached;

//    //@Column(name = "F_AmountReached")
//    ////@Transient
//    protected String amountReached;

////    //@Column(name = "F_CashExcluded")
//    ////@Transient
//    protected String cashExcluded;

//    //@Column(name = "F_WholeTradeDiscount")
//////@Transient
//    protected String wholeTradeDiscount;

    //@NotNull
    @Column(name = "F_ExcecutionDiscount")
    protected double excecutionDiscount;

//    //@Column(name = "F_NOComputationInvolved")
//////@Transient
//    protected String NOComputationInvolved;

////    //@Column(name = "F_GiftAll")
//////@Transient
//    protected String giftAll;

////    //@Column(name = "F_NODoubled")
//    ////@Transient
//    protected String NODoubled;

////    //@Column(name = "F_AmountComputationInvolved")
//////@Transient
//    protected String amountComputationInvolved;

    //@NotNull
    @Column(name = "F_ExcecutionThreshold")
    protected double excecutionThreshold;

    //@NotNull
    @Column(name = "F_ExcecutionAmount")
    protected Double excecutionAmount;

    //@NotNull
    @Column(name = "F_Scope")
    protected int scope;

    //@NotNull
    @Column(name = "F_Staff")
    protected int staff;

//    //@Column(name = "F_DiscountInvolvedForSpecialOffer")
//    protected String discountInvolvedForSpecialOffer;

    @Column(name = "F_SyncType")
    protected String syncType;

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

//    ////@Transient
//    protected String ignoreIDInComparision;

//    ////@Transient
//    protected List<PromotionScope> listPromotionScope;
//
//    public List<PromotionScope> getListPromotionScope() {
//        return listPromotionScope;
//    }
//
//    public void setListPromotionScope(List<PromotionScope> listPromotionScope) {
//        this.listPromotionScope = listPromotionScope;
//    }

//    ////@Transient
//    protected List<PromotionInfo> piList;
//
//    public String getFIELD_NAME_piList() {
//        return "poList";
//    }
//
//    public List<PromotionInfo> getPromotionInfo() {
//        return this.piList;
//    }
//
//    public void setPromotionInfo(List<PromotionInfo> pl) {
//        this.piList = pl;
//    }

    @Transient
    protected String int1;

//    @Override
//    public String getFIELD_NAME_int1() {
//        return "int1";
//    }

//    ////@Transient
//    protected String FIELD_NAME_int1;


    @Transient
    protected String commodityIDs;

    public String getCommodityIDs() {
        return commodityIDs;
    }

    public void setCommodityIDs(String commodityIDs) {
        this.commodityIDs = commodityIDs;
    }

    @Transient
    protected int subStatusOfStatus;

    //@Generated(hash = 1361325021)
//    public Promotion(Long ID, int type, //@NotNull String name, int status, Date createDatetime, //@NotNull Date datetimeStart, //@NotNull Date datetimeEnd, //@NotNull String sn, double excecutionDiscount,
//                     double excecutionThreshold, //@NotNull Double excecutionAmount, int scope, int staff, String syncType, Date syncDatetime) {
//        this.ID = ID;
//        this.type = type;
//        this.name = name;
//        this.status = status;
//        this.createDatetime = createDatetime;
//        this.datetimeStart = datetimeStart;
//        this.datetimeEnd = datetimeEnd;
//        this.sn = sn;
//        this.excecutionDiscount = excecutionDiscount;
//        this.excecutionThreshold = excecutionThreshold;
//        this.excecutionAmount = excecutionAmount;
//        this.scope = scope;
//        this.staff = staff;
//        this.syncType = syncType;
//        this.syncDatetime = syncDatetime;
//    }

    //@Generated(hash = 1959537984)
//    public Promotion() {
//    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

//    public String getApprover() {
//        return approver;
//    }
//
//    public void setApprover(String approver) {
//        this.approver = approver;
//    }
//
//    public Date getApproveDatetime() {
//        return approveDatetime;
//    }
//
//    public void setApproveDatetime(Date approveDatetime) {
//        this.approveDatetime = approveDatetime;
//    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(Date datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public Date getDatetimeEnd() {
        return datetimeEnd;
    }

    public void setDatetimeEnd(Date datetimeEnd) {
        this.datetimeEnd = datetimeEnd;
    }

//    public String getExecuteWeekday() {
//        return executeWeekday;
//    }
//
//    public void setExecuteWeekday(String executeWeekday) {
//        this.executeWeekday = executeWeekday;
//    }
//
//    public Date getExecuteStartDatetime() {
//        return executeStartDatetime;
//    }
//
//    public void setExecuteStartDatetime(Date executeStartDatetime) {
//        this.executeStartDatetime = executeStartDatetime;
//    }
//
//    public Date getExecuteEndDatetime() {
//        return executeEndDatetime;
//    }
//
//    public void setExecuteEndDatetime(Date executeEndDatetime) {
//        this.executeEndDatetime = executeEndDatetime;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public String getDiscountUponVIP() {
//        return discountUponVIP;
//    }
//
//    public void setDiscountUponVIP(String discountUponVIP) {
//        this.discountUponVIP = discountUponVIP;
//    }
//
//    public String getSpecialOfferInvolved() {
//        return specialOfferInvolved;
//    }
//
//    public void setSpecialOfferInvolved(String specialOfferInvolved) {
//        this.specialOfferInvolved = specialOfferInvolved;
//    }
//
//    public String getTimes() {
//        return times;
//    }
//
//    public void setTimes(String times) {
//        this.times = times;
//    }
//
//    public String getvIPBornDayOrMonth() {
//        return vIPBornDayOrMonth;
//    }
//
//    public void setvIPBornDayOrMonth(String vIPBornDayOrMonth) {
//        this.vIPBornDayOrMonth = vIPBornDayOrMonth;
//    }
//
//    public String getWholeShop() {
//        return wholeShop;
//    }
//
//    public void setWholeShop(String wholeShop) {
//        this.wholeShop = wholeShop;
//    }
//
//    public String getNOReached() {
//        return NOReached;
//    }
//
//    public void setNOReached(String NOReached) {
//        this.NOReached = NOReached;
//    }
//
//    public String getAmountReached() {
//        return amountReached;
//    }
//
//    public void setAmountReached(String amountReached) {
//        this.amountReached = amountReached;
//    }
//
//    public String getCashExcluded() {
//        return cashExcluded;
//    }
//
//    public void setCashExcluded(String cashExcluded) {
//        this.cashExcluded = cashExcluded;
//    }
//
//    public String getWholeTradeDiscount() {
//        return wholeTradeDiscount;
//    }
//
//    public void setWholeTradeDiscount(String wholeTradeDiscount) {
//        this.wholeTradeDiscount = wholeTradeDiscount;
//    }

    public double getExcecutionDiscount() {
        return excecutionDiscount;
    }

    public void setExcecutionDiscount(double excecutionDiscount) {
        this.excecutionDiscount = excecutionDiscount;
    }
//
//    public String getNOComputationInvolved() {
//        return NOComputationInvolved;
//    }
//
//    public void setNOComputationInvolved(String NOComputationInvolved) {
//        this.NOComputationInvolved = NOComputationInvolved;
//    }
//
//    public String getGiftAll() {
//        return giftAll;
//    }
//
//    public void setGiftAll(String giftAll) {
//        this.giftAll = giftAll;
//    }
//
//    public String getNODoubled() {
//        return NODoubled;
//    }
//
//    public void setNODoubled(String NODoubled) {
//        this.NODoubled = NODoubled;
//    }
//
//    public String getAmountComputationInvolved() {
//        return amountComputationInvolved;
//    }
//
//    public void setAmountComputationInvolved(String amountComputationInvolved) {
//        this.amountComputationInvolved = amountComputationInvolved;
//    }
//
//    public String getDiscountInvolvedForSpecialOffer() {
//        return discountInvolvedForSpecialOffer;
//    }
//
//    public void setDiscountInvolvedForSpecialOffer(String discountInvolvedForSpecialOffer) {
//        this.discountInvolvedForSpecialOffer = discountInvolvedForSpecialOffer;
//    }

//    public String getIgnoreIDInComparision(){
//        return ignoreIDInComparision;
//    }
//
//    public void setIgnoreIDInComparision(String ignoreIDInComparision){
//        this.ignoreIDInComparision=ignoreIDInComparision;
//    }

    public void setInt1(String int1) {
        this.int1 = int1;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public int getSubStatusOfStatus() {
        return subStatusOfStatus;
    }

    public void setSubStatusOfStatus(int subStatusOfStatus) {
        this.subStatusOfStatus = subStatusOfStatus;
    }


    @Override
    public String toString() {
        return "Promotion{" +
                "ID=" + ID +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", createDatetime=" + createDatetime +
                ", datetimeStart=" + datetimeStart +
                ", datetimeEnd=" + datetimeEnd +
                ", sn='" + sn + '\'' +
                ", excecutionDiscount=" + excecutionDiscount +
                ", excecutionThreshold=" + excecutionThreshold +
                ", excecutionAmount=" + excecutionAmount +
                ", scope=" + scope +
                ", currentStaff=" + staff +
                ", syncType='" + syncType + '\'' +
                ", syncDatetime=" + syncDatetime +
                '}';
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行Promotion.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            type = jo.getInteger(field.getFIELD_NAME_type());
            sn = jo.getString(field.getFIELD_NAME_sn());
            status = jo.getInteger(field.getFIELD_NAME_status());
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType());

            if (BasePresenter.SYNC_Type_C.equals(syncType) || "".equals(syncType)) {
                String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
                if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                    createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                    if (createDatetime == null) {
                        System.out.println("无法解析该日期:" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                        throw new RuntimeException("无法解析该日期:" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    }
                }
                String tmpDatetimeStart = jo.getString(field.getFIELD_NAME_datetimeStart());
                if (!StringUtils.isEmpty(tmpDatetimeStart)) {
                    datetimeStart = Constants.getSimpleDateFormat2().parse(tmpDatetimeStart);
                    if (datetimeStart == null) {
                        System.out.println("无法解析该日期:" + field.getFIELD_NAME_datetimeStart() + "=" + tmpDatetimeStart);
                        throw new RuntimeException("无法解析该日期:" + field.getFIELD_NAME_datetimeStart() + "=" + tmpDatetimeStart);
                    }
                }
                String tmpDatetimeEnd = jo.getString(field.getFIELD_NAME_datetimeEnd());
                if (!StringUtils.isEmpty(tmpDatetimeEnd)) {
                    datetimeEnd = Constants.getSimpleDateFormat2().parse(tmpDatetimeEnd);
                    if (datetimeEnd == null) {
                        System.out.println("无法解析该日期:" + field.getFIELD_NAME_datetimeEnd() + "=" + tmpDatetimeEnd);
                        throw new RuntimeException("无法解析该日期:" + field.getFIELD_NAME_datetimeEnd() + "=" + tmpDatetimeEnd);
                    }
                }
                excecutionThreshold = Double.valueOf(jo.getString(field.getFIELD_NAME_excecutionThreshold()));
                excecutionAmount = Double.valueOf(jo.getString(field.getFIELD_NAME_excecutionAmount()));
                excecutionDiscount = Double.valueOf(jo.getString(field.getFIELD_NAME_excecutionDiscount()));
                name = jo.getString(field.getFIELD_NAME_name());
                scope = Integer.valueOf(jo.getString(field.getFIELD_NAME_scope()));
            }

            return this;
        } catch (Exception e) {
            System.out.println("Promotion.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Promotion.parse1，s=" + s);

        try {
            JSONObject joP = JSONObject.parseObject(s);
            Promotion promotion = (Promotion) doParse1(joP);
            if (promotion == null) {
                return null;
            }
            JSONArray psArr = joP.getJSONArray(field.getFIELD_NAME_listSlave1());
            if (psArr != null) {
                PromotionScope rtc = new PromotionScope();
                List<PromotionScope> listPS = (List<PromotionScope>) rtc.parseN(psArr);
                if (listPS == null) {
                    return null;
                }
                promotion.setListSlave1(listPS);  //非常关键！！
            }
            JSONArray psArr2 = joP.getJSONArray(field.getFIELD_NAME_listSlave2());
            if (psArr2 != null) {
                PromotionShopScope rtc2 = new PromotionShopScope();
                List<PromotionShopScope> listPS2 = (List<PromotionShopScope>) rtc2.parseN(psArr2);
                if (listPS2 == null) {
                    return null;
                }
                promotion.setListSlave2(listPS2);  //非常关键！！
            }

            return promotion;
        } catch (JSONException e) {
            System.out.println("PromotionScope.parse1()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 Promotion.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        // TODO if jsonArray == null;

        List<BaseModel> pList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Promotion p = new Promotion();
                if (p.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                pList.add(p);
            }
            return pList;
        } catch (Exception e) {
            System.out.println("Promotion.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    public String getVIPBornDayOrMonth() {
//        return this.vIPBornDayOrMonth;
//    }
//
//    public void setVIPBornDayOrMonth(String vIPBornDayOrMonth) {
//        this.vIPBornDayOrMonth = vIPBornDayOrMonth;
//    }

    public BaseModel clone() throws CloneNotSupportedException {
        Promotion p = new Promotion();
        p.setID(ID);
        p.setName(name);
        p.setStatus(status);
        p.setType(type);
        p.setDatetimeStart(datetimeStart);
        p.setDatetimeEnd(datetimeEnd);
        p.setExcecutionThreshold(excecutionThreshold);
        p.setExcecutionAmount(excecutionAmount);
        p.setExcecutionDiscount(excecutionDiscount);
        p.setScope(scope);
        p.setStaff(staff);
        p.setCreateDatetime(createDatetime);
        p.setSn(sn);
        if (listSlave1 != null) {
            List<PromotionScope> list = new ArrayList<PromotionScope>();
            for (PromotionScope promotionScope : (List<PromotionScope>) listSlave1) {
                list.add((PromotionScope) promotionScope.clone());
            }
            p.setListSlave1(list);
        }
        if (listSlave2 != null) {
            List<PromotionShopScope> list2 = new ArrayList<>();
            for (PromotionShopScope promotionShopScope : (List<PromotionShopScope>) listSlave2) {
                list2.add((PromotionShopScope) promotionShopScope.clone());
            }
            p.setListSlave2(list2);
        }
        p.setCommodityIDs(commodityIDs);
        p.setIgnoreIDInComparision(ignoreIDInComparision);
        p.setReturnObject(returnObject); //...TODO

        return p;
    }

//    public void setFIELD_NAME_int1(String FIELD_NAME_int1) {
//        this.FIELD_NAME_int1 = FIELD_NAME_int1;
//    }


    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        Promotion p = (Promotion) arg0;
        if ((ignoreIDInComparision == true ? true : (this.getID() == p.getID())) && printComparator(field.getFIELD_NAME_ID())//
                && p.getType() == this.getType() && printComparator("Type")    //
                && p.getName().equals(this.getName()) && printComparator("name") //
                && p.getStatus() == this.getStatus() && printComparator("Status")//
//                && p.getRemark().equals(this.getRemark()) && printComparator("Remark")//
//                && p.getDiscountUponVIP() == this.getDiscountUponVIP() && printComparator("DiscountUponVIP")//
//                && p.getSpecialOfferInvolved() == this.getSpecialOfferInvolved() && printComparator("SpecialOfferInvolved")//
//                && p.getvIPBornDayOrMonth() == this.getvIPBornDayOrMonth() && printComparator("vIPBornDayOrMonth")//
//                && p.getWholeShop() == this.getWholeShop() && printComparator("WholeShop")//
//                && p.getNOReached() == this.getNOReached() && printComparator("NOReached")//
//                && p.getWholeTradeDiscount() == this.getWholeTradeDiscount() && printComparator("WholeTradeDiscount")//
//                && p.getNOComputationInvolved() == this.getNOComputationInvolved() && printComparator("NOComputationInvolved")//
//                && p.getGiftAll() == this.getGiftAll() && printComparator("GiftAll")//
//                && p.getNODoubled() == this.getNODoubled() && printComparator("NODoubled")//
//                && p.getAmountComputationInvolved() == this.getAmountComputationInvolved() && printComparator("AmountComputationInvolved")//
//                && p.getDiscountInvolvedForSpecialOffer() == this.getDiscountInvolvedForSpecialOffer() && printComparator("DiscountInvolved")//
                ) {
            return 0;
        }
        return -1;
    }

    public double getExcecutionAmount() {
        return this.excecutionAmount;
    }

    public void setExcecutionAmount(double excecutionAmount) {
        this.excecutionAmount = excecutionAmount;
    }

    public int getScope() {
        return this.scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getStaff() {
        return this.staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    public double getExcecutionThreshold() {
        return this.excecutionThreshold;
    }

    public void setExcecutionThreshold(double excecutionThreshold) {
        this.excecutionThreshold = excecutionThreshold;
    }

    public enum EnumStatusPromotion {
        ESP_Active("Active", 0), // 有效
        ESP_Deleted("Deleted", 1);// 已经删除

        private String name;
        private int index;

        private EnumStatusPromotion(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static boolean inBoundForRetrieveN(int index) {
            if (index != BaseSQLiteBO.INVALID_STATUS && index != ESP_Active.getIndex() && index != ESP_Deleted.getIndex()) {
                return false;
            }

            return true;
        }

        public static boolean inBound(int index) {
            if (index < ESP_Active.getIndex() || index > ESP_Deleted.getIndex()) {
                return false;
            }
            return true;
        }

        public static String getName(int index) {
            for (EnumStatusPromotion c : EnumStatusPromotion.values()) {
                if (c.getIndex() == index) {
                    return c.name;
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

    public enum EnumTypePromotion {
        ETP_DecreaseOnAmount("DecreaseOnAmount", 0), // 满减
        ETP_DiscountOnAmount("DiscountOnAmount", 1);// 满折

        private String name;
        private int index;

        private EnumTypePromotion(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static boolean inBound(int index) {
            if (index < ETP_DecreaseOnAmount.getIndex() || index > ETP_DiscountOnAmount.getIndex()) {
                return false;
            }
            return true;
        }

        public static String getName(int index) {
            for (EnumStatusPromotion c : EnumStatusPromotion.values()) {
                if (c.getIndex() == index) {
                    return c.name;
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

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (!EnumTypePromotion.inBound(type)) {
            return FIELD_ERROR_type;
        }
        if (StringUtils.isEmpty(name) || name.length() > Max_LengthName) {
            return FIELD_ERROR_name;
        }
        if (!BasePromotion.EnumPromotionScope.inBound(scope)) {
            return FIELD_ERROR_scope;
        }
        if (!EnumStatusPromotion.inBound(status)) {
            return FIELD_ERROR_status;
        }
        if (printCheckField(field.getFIELD_NAME_staff(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(staff)) {
            return sbError.toString();
        }
        if (!DatetimeUtil.isAfterDate(datetimeEnd, datetimeStart, 0)) {
            return FIELD_ERROR_datetimeStart_datetimeEnd;
        }
        //
        Date tomorrow = DatetimeUtil.get2ndDayStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        if (datetimeStart.compareTo(tomorrow) <= 0) {
            return FIELD_ERROR_startDatetime;
        }
        if (excecutionThreshold > MAX_ExcecutionThreshold || excecutionThreshold < 0 || Math.abs(GeneralUtil.sub(excecutionThreshold, 0)) < TOLERANCE) {
            return FIELD_ERROR_excecutionThreshold;
        }
        if (type == EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()) {
            if (excecutionAmount > MAX_ExcecutionAmount || excecutionAmount < 0 || Math.abs(GeneralUtil.sub(excecutionAmount, 0)) < TOLERANCE) {
                return FIELD_ERROR_excecutionAmount;
            }
            if (excecutionAmount > excecutionThreshold) {
                return FIELD_ERROR_excecutionAmount_excecutionThreshold;
            }
        }
        if (type == EnumTypePromotion.ETP_DiscountOnAmount.getIndex()) {
            if (excecutionDiscount > Max_excecutionDiscount || excecutionDiscount < 0 || Math.abs(GeneralUtil.sub(excecutionDiscount, 0)) < TOLERANCE) {
                return FIELD_ERROR_excecutionDiscount;
            }
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }
        if (!EnumTypePromotion.inBound(type)) {
            return FIELD_ERROR_type;
        }
        if (StringUtils.isEmpty(name) || name.length() > Max_LengthName) {
            return FIELD_ERROR_name;
        }
        if (!BasePromotion.EnumPromotionScope.inBound(scope)) {
            return FIELD_ERROR_scope;
        }
        if (!EnumStatusPromotion.inBound(status)) {
            return FIELD_ERROR_status;
        }
        if (printCheckField(field.getFIELD_NAME_staff(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(staff)) {
            return sbError.toString();
        }
        if (!DatetimeUtil.isAfterDate(datetimeEnd, datetimeStart, 0)) {
            return FIELD_ERROR_datetimeStart_datetimeEnd;
        }
        //
        Date tomorrow = DatetimeUtil.get2ndDayStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        if (datetimeStart.compareTo(tomorrow) <= 0) {
            return FIELD_ERROR_startDatetime;
        }
        if (excecutionThreshold > MAX_ExcecutionThreshold || excecutionThreshold < 0 || Math.abs(GeneralUtil.sub(excecutionThreshold, 0)) < TOLERANCE) {
            return FIELD_ERROR_excecutionThreshold;
        }
        if (type == EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()) {
            if (excecutionAmount > MAX_ExcecutionAmount || excecutionAmount < 0 || Math.abs(GeneralUtil.sub(excecutionAmount, 0)) < TOLERANCE) {
                return FIELD_ERROR_excecutionAmount;
            }
            if (excecutionAmount > excecutionThreshold) {
                return FIELD_ERROR_excecutionAmount_excecutionThreshold;
            }
        }
        if (type == EnumTypePromotion.ETP_DiscountOnAmount.getIndex()) {
            if (excecutionDiscount > Max_excecutionDiscount || excecutionDiscount < 0 || Math.abs(GeneralUtil.sub(excecutionDiscount, 0)) < TOLERANCE) {
                return FIELD_ERROR_excecutionDiscount;
            }
        }
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_statusForRetrieveN, sbError) && !EnumStatusPromotion.inBoundForRetrieveN(status)) //
        {
            return sbError.toString();
        }
        if (!StringUtils.isEmpty(queryKeyword)) { // string1可以代表搜索的因子为：商品名称、促销单号。其中商品名称最长，所以string1最长能传Max_LengthName位
            if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_nameForRetrieveN, sbError) && queryKeyword.length() > Max_LengthName) //
            {
                return sbError.toString();
            }
        }

        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    public void setExcecutionAmount(Double excecutionAmount) {
        this.excecutionAmount = excecutionAmount;
    }

    /**
     * ESP_Active类型的促销,根据查询的条件,又细分为几种
     */
    public enum EnumSubStatusPromotion {
        ESSP_ToStart("ToStart", 10), // 未开始的促销
        ESSP_Promoting("Promoting", 11),// 正在促销中
        ESSP_Terminated("Terminated", 12),// 已经结束
        ESSP_ToStartAndPromoting("ToStartAndPromoting", 13);// 正在促销中和还未开始促销的

        private String name;
        private int index;

        private EnumSubStatusPromotion(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumSubStatusPromotion c : EnumSubStatusPromotion.values()) {
                if (c.getIndex() == index) {
                    return c.name;
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
