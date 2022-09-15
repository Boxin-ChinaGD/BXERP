package com.bx.erp.model;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.StringUtils;
import com.google.gson.JsonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(nameInDb = "retailTrade")
public class RetailTrade extends BaseModel {
    public static final String HTTP_REQ_URL_Create = "retailTrade/createEx.bx";
    public static final String HTTP_REQ_PARAMETER_Create = "retailTrade";
    public static final String HTTP_REQ_URL_RetrieveNC = "retailTrade/retrieveNEx.bx";
    public static final String HTTP_REQ_PageIndex = "?pageIndex=";
    public static final String HTTP_REQ_PageSize = "&pageSize=";
    public static final String HTTP_REQ_URL_CreateN = "retailTrade/createNEx.bx";

    //    public static final int Max_LengthLogo = 128;
    public static final boolean isForRetailTradeQueryBySN = true;
    public static final int Max_LENGTH_SN_Create = 24;
    public static final int Max_LENGTH_SN_Query = 26;
    /**
     * 根据编号搜索零售单时，输入的关键字不必是长度LENGTH_SN，其范围为[Min_LengthSN_Query, LENGTH_SN]
     */
    public static final int Min_LengthSN_Query = 10;
    public static final int Min_ID = 0;
    public static final int Min_LocalSN = 0;
    public static final int Min_paymentType = 1;
    public static final int Max_paymentType = 7;
    //    public static final int Max_LengthPaymentAccount = 20;
    public static final int Max_LengthRemark = 20;
    public static final double Min_Amount = 0.000000d;
    public static final int Max_LengthConsumerOpenID = 100;
    public static final String TEMPLATE_SN = "LS19700101";

    //    public static final String FIELD_ERROR_status = "创建零售单时其状态必须为" + EnumStatusRetailTrade.ESRT_Normal.getIndex();
    public static final String FIELD_ERROR_sn_ForQuery = "请输入正确的大于" + (Min_LengthSN_Query - 1) + "、小于" + Max_LENGTH_SN_Query + "位单号，如:" + TEMPLATE_SN;
    public static final String FIELD_ERROR_sn = "单据流水号长度必须是" + Max_LENGTH_SN_Create;
    public static final String FIELD_ERROR_LOCALSN = "localSN不能小于" + Min_LocalSN;
    //    public static final String FIELD_ERROR_VipID = "VipID不能小于" + Min_ID;
    public static final String FIELD_ERROR_POSID = "POSID不能小于" + Min_ID;
    public static final String FIELD_ERROR_paymentType = "paymentType不能小于" + Min_paymentType + ",不能大于" + Max_paymentType;
    public static final String FIELD_ERROR_StaffID = "StaffID不能小于" + Min_ID;
    //    public static final String FIELD_ERROR_logo = "logo不能为空，长度不能大于" + Max_LengthLogo;
//    public static final String FIELD_ERROR_paymentAccount = "paymentAccount不能为空，不能大于" + Max_LengthPaymentAccount + "个字符";
    public static final String FIELD_ERROR_remark = "备注不能大于" + Max_LengthRemark + "个字符";
    public static final String FIELD_ERROR_sourceID = "sourceID必须为-1";
    public static final String FIELD_ERROR_sourceID_ForReturned = "sourceID必须>0";
    public static final String FIELD_ERROR_Amount = "金额必须大于" + Min_Amount;
    public static final String FIELD_ERROR_smallSheetID = "smallSheetID必须大于" + Min_ID;
    public static final String FIELD_ERROR_datetimeStart = "datetimeStart不能为空";
    public static final String FIELD_ERROR_datetimeEnd = "datetimeEnd不能为空";
    public static final String FIELD_ERROR_syncDatetime = "syncDatetime不能为空";
    public static final String FIELD_ERROR_saleDatetime = "saleDatetime不能为空";
    public static final String FIELD_ERROR_amountTotal = "每种支付方式对应的钱加起来要等于应收款";
    public static final String FIELD_ERROR_payment = "使用某种支付方式，那么对应支付方式的钱应大于" + Min_Amount;
    public static final String FIELD_ERROR_notPayment = "不使用某种支付方式，那么对应支付方式的钱应为" + Min_Amount;
    public static final String FIELD_ERROR_saleAfterReturnForAmountAlipay = "退款失败，支付宝退款金额不能比零售时支付宝支付的金额多";
    public static final String FIELD_ERROR_saleAfterReturnForAmountWeChat = "退款失败，微信退款金额不能比零售时微信支付的金额多";
    public static final String FIELD_ERROR_listSlave1 = "从表数据中存在相同类型的商品";
    public static final String FIELD_ERROR_consumerOpenID = "openID必须为NULL或长度小于等于" + Max_LengthConsumerOpenID;

    public static final String RetailTradeSNBegin = "LS"; // 零售单SN码的开头字符
    public static final String FormatWithPosID = "%04d";  // 根据posID的数值补零，长度为4
    public static final String IgnoreJSONValue = "null"; // 忽略字段值为null的值

    public static final RetailTradeField field = new RetailTradeField();

    public enum EnumStatusRetailTrade {
        ESRT_Hold("Hold", 0), //
        ESRT_Normal("Normal", 1), //
        ESRT_IncludeDeletedCommodity("Include Deleted Commodity", 2);

        private String name;
        private int index;

        private EnumStatusRetailTrade(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusRetailTrade c : EnumStatusRetailTrade.values()) {
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

    /**
     * 退货单对应的零售单的支付宝支付金额
     */
    @Transient
    protected double saleAmountAlipay;

    /**
     * 退货单对应的零售单的微信支付金额
     */
    @Transient
    protected double saleAmountWeChat;

    @Transient
    protected boolean ignoreSyncTypeInComparision;

    @Transient
    protected boolean ignoreSNInComparision;

    public void setIgnoreSNInComparision(boolean ignoreSNInComparision) {
        this.ignoreSNInComparision = ignoreSNInComparision;
    }

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_VipID")
    protected long vipID;

    @Property(nameInDb = "F_SN")
    protected String sn;

    @Property(nameInDb = "F_LocalSN")
    protected int localSN;

    @NotNull
    @Property(nameInDb = "F_POS_ID")
    protected int pos_ID;

    @NotNull
    @Property(nameInDb = "F_Logo")
    protected String logo;

    @NotNull
    @Property(nameInDb = "F_SaleDatetime")
    protected Date saleDatetime;

    @NotNull
    @Property(nameInDb = "F_StaffID")
    protected int staffID;

    @NotNull
    @Property(nameInDb = "F_PaymentType")
    protected int paymentType;

    @NotNull
    @Property(nameInDb = "F_PaymentAccount")
    protected String paymentAccount;

    @NotNull
    @Property(nameInDb = "F_Status")
    protected int status;

    @NotNull
    @Property(nameInDb = "F_Remark")
    protected String remark;

    @NotNull
    @Property(nameInDb = "F_SourceID")
    protected int sourceID;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @NotNull
    @Property(nameInDb = "F_Amount")
    protected double amount;

    @NotNull
    @Property(nameInDb = "F_AmountPaidIn")
    protected double amountPaidIn;

    @NotNull
    @Property(nameInDb = "F_AmountChange")
    protected double amountChange;

    @Property(nameInDb = "F_AmountCash")
    protected double amountCash;

    @Property(nameInDb = "F_AmountAlipay")
    protected double amountAlipay;

    @Property(nameInDb = "F_AmountWeChat")
    protected double amountWeChat;

    @Property(nameInDb = "F_Amount1")
    protected double amount1;

    @Property(nameInDb = "F_Amount2")
    protected double amount2;

    @Property(nameInDb = "F_Amount3")
    protected double amount3;

    @Property(nameInDb = "F_Amount4")
    protected double amount4;

    @Property(nameInDb = "F_Amount5")
    protected double amount5;

    @NotNull
    @Property(nameInDb = "F_SmallSheetID")
    protected int smallSheetID;

    @Property(nameInDb = "F_AliPayOrderSN")
    protected String aliPayOrderSN;

    @Property(nameInDb = "F_WxOrderSN")
    protected String wxOrderSN;

    @Property(nameInDb = "F_WxTradeNO")
    protected String wxTradeNO;

    @Property(nameInDb = "F_WxRefundNO")
    protected String wxRefundNO;

    @Property(nameInDb = "F_WxRefundDesc")
    protected String wxRefundDesc;

    @Property(nameInDb = "F_WxRefundSubMchID")
    protected String wxRefundSubMchID;

    @Property(nameInDb = "F_CouponAmount")
    protected double couponAmount;

    @Property(nameInDb = "F_ConsumerOpenID")
    protected String consumerOpenID;

    @Property(nameInDb = "F_SlaveCreated")
    protected int SlaveCreated;

    @Property(nameInDb = "F_ShopID")
    protected int shopID;

    @Transient
    protected int num;

    @Transient
    protected Date datetimeStart;

    @Transient
    protected Date datetimeEnd;

    @Transient
    protected int excludeReturned;

    /**
     * 挂单时保存商品信息
     */
    @Transient
    protected List<Commodity> commodityListOfHeldBill;

    /**
     * 挂单时保存会员信息
     */
    @Transient
    protected Vip vip;

    /**
     * 挂单时间
     */
    @Transient
    protected Date holdBillTime;

    /**
     * 用来保存挂单零售单的第一个商品名称
     */
    @Transient
    protected String firstCommodityName;

    @Transient
    protected int number;//列表中的序号

    // TODO  其实不需要此参数。因为NBR要判断是不是Pos的请求，只需要判断会话中的PosID>0即可
    @Transient
    protected int bRequestFromApp;

    public Date getHoldBillTime() {
        return holdBillTime;
    }

    public void setHoldBillTime(Date holdBillTime) {
        this.holdBillTime = holdBillTime;
    }

    public String getFirstCommodityName() {
        return firstCommodityName;
    }

    public void setFirstCommodityName(String firstCommodityName) {
        this.firstCommodityName = firstCommodityName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Commodity> getCommodityListOfHeldBill() {
        return commodityListOfHeldBill;
    }

    public void setCommodityListOfHeldBill(List<Commodity> commodityListOfHeldBill) {
        this.commodityListOfHeldBill = commodityListOfHeldBill;
    }

    public Vip getVip() {
        return vip;
    }

    public void setVip(Vip vip) {
        this.vip = vip;
    }

    public Date getDatetimeEnd() {
        return datetimeEnd;
    }

    public void setDatetimeEnd(Date datetimeEnd) {
        this.datetimeEnd = datetimeEnd;
    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(Date datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public RetailTrade() {
    }

    @Generated(hash = 2062057936)
    public RetailTrade(Long ID, long vipID, String sn, int localSN, int pos_ID, @NotNull String logo, @NotNull Date saleDatetime, int staffID, int paymentType, @NotNull String paymentAccount, int status, @NotNull String remark, int sourceID, Date syncDatetime,
            String syncType, double amount, double amountPaidIn, double amountChange, double amountCash, double amountAlipay, double amountWeChat, double amount1, double amount2, double amount3, double amount4, double amount5, int smallSheetID,
            String aliPayOrderSN, String wxOrderSN, String wxTradeNO, String wxRefundNO, String wxRefundDesc, String wxRefundSubMchID, double couponAmount, String consumerOpenID, int SlaveCreated, int shopID) {
        this.ID = ID;
        this.vipID = vipID;
        this.sn = sn;
        this.localSN = localSN;
        this.pos_ID = pos_ID;
        this.logo = logo;
        this.saleDatetime = saleDatetime;
        this.staffID = staffID;
        this.paymentType = paymentType;
        this.paymentAccount = paymentAccount;
        this.status = status;
        this.remark = remark;
        this.sourceID = sourceID;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
        this.amount = amount;
        this.amountPaidIn = amountPaidIn;
        this.amountChange = amountChange;
        this.amountCash = amountCash;
        this.amountAlipay = amountAlipay;
        this.amountWeChat = amountWeChat;
        this.amount1 = amount1;
        this.amount2 = amount2;
        this.amount3 = amount3;
        this.amount4 = amount4;
        this.amount5 = amount5;
        this.smallSheetID = smallSheetID;
        this.aliPayOrderSN = aliPayOrderSN;
        this.wxOrderSN = wxOrderSN;
        this.wxTradeNO = wxTradeNO;
        this.wxRefundNO = wxRefundNO;
        this.wxRefundDesc = wxRefundDesc;
        this.wxRefundSubMchID = wxRefundSubMchID;
        this.couponAmount = couponAmount;
        this.consumerOpenID = consumerOpenID;
        this.SlaveCreated = SlaveCreated;
        this.shopID = shopID;
    }

    @Override
    public String toString() {
        return "RetailTrade{" +
                "saleAmountAlipay=" + saleAmountAlipay +
                ", saleAmountWeChat=" + saleAmountWeChat +
                ", ID=" + ID +
                ", vipID=" + vipID +
                ", sn='" + sn + '\'' +
                ", localSN=" + localSN +
                ", pos_ID=" + pos_ID +
                ", logo='" + logo + '\'' +
                ", saleDatetime=" + saleDatetime +
                ", staffID=" + staffID +
                ", paymentType=" + paymentType +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", sourceID=" + sourceID +
                ", syncDatetime=" + syncDatetime +
                ", syncType='" + syncType + '\'' +
                ", amountCash=" + amountCash +
                ", amount=" + amount +
                ", amount=PaidIn" + amountPaidIn +
                ", amountChange=" + amountChange +
                ", amountAlipay=" + amountAlipay +
                ", amountWeChat=" + amountWeChat +
                ", amount1=" + amount1 +
                ", amount3=" + amount3 +
                ", amount2=" + amount2 +
                ", amount5=" + amount5 +
                ", amount4=" + amount4 +
                ", smallSheetID=" + smallSheetID +
                ", aliPayOrderSN='" + aliPayOrderSN + '\'' +
                ", wxOrderSN='" + wxOrderSN + '\'' +
                ", wxTradeNO='" + wxTradeNO + '\'' +
                ", wxRefundNO='" + wxRefundNO + '\'' +
                ", wxRefundDesc='" + wxRefundDesc + '\'' +
                ", wxRefundSubMchID='" + wxRefundSubMchID + '\'' +
                ", couponAmount=" + couponAmount +
                ", consumerOpenID=" + consumerOpenID +
                ", datetimeStart=" + datetimeStart +
                ", datetimeEnd=" + datetimeEnd +
                ", listSlave1" + listSlave1 +
                '}';
    }

    @Transient
    public ErrorInfo errorInfo;

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }


//    public List<RetailTradeCommodity> getRetailTradeCommodityList() {
//        return retailTradeCommodityList;
//    }
//
//    public void setRetailTradeCommodityList(List<RetailTradeCommodity> retailTradeCommodityList) {
//        this.retailTradeCommodityList = retailTradeCommodityList;
//    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getLocalSN() {
        return localSN;
    }

    public void setLocalSN(int localSN) {
        this.localSN = localSN;
    }

    public int getPos_ID() {
        return pos_ID;
    }

    public void setPos_ID(int pos_ID) {
        this.pos_ID = pos_ID;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Date getSaleDatetime() {
        return saleDatetime;
    }

    public void setSaleDatetime(Date saleDatetime) {
        this.saleDatetime = saleDatetime;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    @Override
    public Date getSyncDatetime() {
        return syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountPaidIn() {
        return amountPaidIn;
    }

    public void setAmountPaidIn(double amountPaidIn) {
        this.amountPaidIn = amountPaidIn;
    }

    public double getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(double amountChange) {
        this.amountChange = amountChange;
    }

    public double getAmountCash() {
        return amountCash;
    }

    public void setAmountCash(double amountCash) {
        this.amountCash = amountCash;
    }

    public double getAmountAlipay() {
        return amountAlipay;
    }

    public void setAmountAlipay(double amountAlipay) {
        this.amountAlipay = amountAlipay;
    }

    public double getAmountWeChat() {
        return amountWeChat;
    }

    public void setAmountWeChat(double amountWeChat) {
        this.amountWeChat = amountWeChat;
    }

    public double getAmount1() {
        return amount1;
    }

    public void setAmount1(double amount1) {
        this.amount1 = amount1;
    }

    public double getAmount2() {
        return amount2;
    }

    public void setAmount2(double amount2) {
        this.amount2 = amount2;
    }

    public double getAmount3() {
        return amount3;
    }

    public void setAmount3(double amount3) {
        this.amount3 = amount3;
    }

    public double getAmount4() {
        return amount4;
    }

    public void setAmount4(double amount4) {
        this.amount4 = amount4;
    }

    public double getAmount5() {
        return amount5;
    }

    public void setAmount5(double amount5) {
        this.amount5 = amount5;
    }

    public double getSaleAmountAlipay() {
        return saleAmountAlipay;
    }

    public void setSaleAmountAlipay(double saleAmountAlipay) {
        this.saleAmountAlipay = saleAmountAlipay;
    }

    public double getSaleAmountWeChat() {
        return saleAmountWeChat;
    }

    public void setSaleAmountWeChat(double saleAmountWeChat) {
        this.saleAmountWeChat = saleAmountWeChat;
    }

    public int getSmallSheetID() {
        return smallSheetID;
    }

    public void setSmallSheetID(int smallSheetID) {
        this.smallSheetID = smallSheetID;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isIgnoreSyncTypeInComparision() {
        return ignoreSyncTypeInComparision;
    }

    public void setIgnoreSyncTypeInComparision(boolean ignoreSyncTypeInComparision) {
        this.ignoreSyncTypeInComparision = ignoreSyncTypeInComparision;
    }


    @Transient
    public boolean isSelect = false;

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 RetailTrade.parse1() ，s=" + s);

        try {
            JSONObject joRT = new JSONObject(s);
            RetailTrade rt = (RetailTrade) doParse1(joRT);
            if (rt == null) {
                return null;
            }

            if (rt.getErrorInfo() == null || rt.getErrorInfo().getErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                // 解析零售单商品表
                JSONArray rtcArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave1());
                if (rtcArr != null) {
                    RetailTradeCommodity rtc = new RetailTradeCommodity();
                    List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) rtc.parseN(rtcArr);
                    if (listRtc == null) {
                        return null;
                    }
                    rt.setListSlave1(listRtc);  //非常关键！！
                }

                //解析零售单优惠券使用表
                Object object = joRT.get(field.getFIELD_NAME_listSlave3());
                if (object != null && !"null".equals(object) && !JsonNull.INSTANCE.equals(object) && !JSONObject.NULL.equals(object)) {
                    JSONArray retailTradeCouponArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave3());
                    if (retailTradeCouponArr != null) {
                        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
                        List<RetailTradeCoupon> retailTradeCouponList = (List<RetailTradeCoupon>) retailTradeCoupon.parseN(retailTradeCouponArr);
                        if (retailTradeCouponList == null) {
                            return null;
                        }
                        rt.setListSlave3(retailTradeCouponList);
                    }
                }

                //解析零售单促销表
                object = joRT.get(field.getFIELD_NAME_listSlave2());
                if (object != null && !"null".equals(object) && !JsonNull.INSTANCE.equals(object) && !JSONObject.NULL.equals(object)) {
                    JSONArray rtpArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave2());
                    if (rtpArr != null) {
                        List<BaseModel> listRtp = new ArrayList<BaseModel>();
                        for (int i = 0; i < rtpArr.length(); i++) {
                            RetailTradePromoting rtp = new RetailTradePromoting();
                            JSONObject jsonObject = rtpArr.getJSONObject(i);
                            rtp = (RetailTradePromoting) rtp.parse1(jsonObject.toString());
                            if (rtp == null) {
                                return null;
                            }
                            listRtp.add(rtp);
                        }
                        rt.setListSlave2(listRtp);
                    }
                }
            }

            return rt;
        } catch (JSONException e) {
            System.out.println("RetailTrade.parse1()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 RetailTrade.doParse1() ，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));//...字段名
            vipID = Integer.valueOf(jo.getString(field.getFIELD_NAME_vipID()));
            sn = jo.getString(field.getFIELD_NAME_sn());
            localSN = jo.getInt(field.getFIELD_NAME_localSN());
            pos_ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_pos_ID()));
            logo = jo.getString(field.getFIELD_NAME_logo());
            //
            String tmp = jo.getString(field.getFIELD_NAME_saleDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                saleDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (saleDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_saleDatetime() + "=" + tmp);
                }
            }
            //
            tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }

            staffID = Integer.valueOf(jo.getString(field.getFIELD_NAME_staffID()));
            paymentType = Integer.valueOf(jo.getString(field.getFIELD_NAME_paymentType()));
            paymentAccount = jo.getString(field.getFIELD_NAME_paymentAccount());
            status = Integer.valueOf(jo.getString(field.getFIELD_NAME_status()));
            remark = jo.getString(field.getFIELD_NAME_remark());
            sourceID = Integer.valueOf(jo.getString(field.getFIELD_NAME_sourceID()));
            amount = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount()));
            amountPaidIn = Double.parseDouble(jo.getString(field.getFIELD_NAME_amountPaidIn()));
            amountChange = Double.parseDouble(jo.getString(field.getFIELD_NAME_amountChange()));
            amountCash = Double.parseDouble(jo.getString(field.getFIELD_NAME_amountCash()));
            amountAlipay = Double.parseDouble(jo.getString(field.getFIELD_NAME_amountAlipay()));
            amountWeChat = Double.parseDouble(jo.getString(field.getFIELD_NAME_amountWeChat()));
            amount1 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount1()));
            amount2 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount2()));
            amount3 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount3()));
            amount4 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount4()));
            amount5 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount5()));
            smallSheetID = Integer.valueOf(jo.getString(field.getFIELD_NAME_smallSheetID()));
            couponAmount = Double.parseDouble(jo.getString(field.getFIELD_NAME_couponAmount()));
            consumerOpenID = jo.getString(field.getFIELD_NAME_consumerOpenID());
            shopID = jo.getInt(field.getFIELD_NAME_shopID());
            tmp = jo.getString(getFIELD_NAME_aliPayOrderSN());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                aliPayOrderSN = tmp;
            }
            //
            tmp = jo.getString(getFIELD_NAME_wxOrderSN());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                wxOrderSN = tmp;
            }
            //
            tmp = jo.getString(getFIELD_NAME_wxRefundDesc());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                wxRefundDesc = tmp;
            }
            //
            tmp = jo.getString(getFIELD_NAME_wxRefundNO());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                wxRefundNO = tmp;
            }
            //
            tmp = jo.getString(getFIELD_NAME_wxRefundSubMchID());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                wxRefundSubMchID = tmp;
            }
            //
            tmp = jo.getString(getFIELD_NAME_wxTradeNO());
            if (!"".equals(tmp) && !IgnoreJSONValue.equals(tmp)) {
                wxTradeNO = tmp;
            }
            //
            syncSequence = jo.getInt(field.getFIELD_NAME_syncSequence());

            String jsonErrorInfo = jo.getString(field.getFIELD_NAME_errorInfo());
            if (!StringUtils.isEmpty(jsonErrorInfo) && !"null".equals(jsonErrorInfo)) {
                errorInfo = new ErrorInfo();
                errorInfo = (ErrorInfo) errorInfo.parse1(jsonErrorInfo);
            }

//            errorInfo = (ErrorInfo) errorInfo.parse1(jo.getString(getFIELD_NAME_errorInfo()));

            return this;
        } catch (Exception e) {
            System.out.println("RetailTrade.doParse1()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 RetailTrade.parseN() ，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTrade rt = new RetailTrade();
                if (rt.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtList.add(rt);
            }
            return rtList;
        } catch (Exception e) {
            System.out.println("RetailTrade.parseN()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 RetailTrade.parseN() ，s=" + s);

        List<BaseModel> retailTradeList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                RetailTrade c = new RetailTrade();
                c.doParse1(jsonObject1);
                retailTradeList.add(c);
            }
        } catch (Exception e) {
            System.out.println("RetailTrade.parseN()异常：" + e.getMessage());

            e.printStackTrace();
            return null;
        }

        return retailTradeList;
    }

    /**
     * 用于删除一些JSON当中不需要的属性。
     * 原因：在上传一批零售单时调用HttpBO需要把List转为JSON格式，此时的JSON数据中有很多的不需要的数据。从而导致服务器会解析失败
     */
    public String removeJSONArrayofAttribute(String jsonData) {
        System.out.println("正在执行 RetailTrade.removeJSONArrayofAttribute() ，jsonData=" + jsonData);

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.remove(field.getFIELD_NAME_ignoreSyncTypeInComparision());
                jsonObject.remove(field.getFIELD_NAME_isSelect());
                jsonObject.remove(field.getFIELD_NAME_num());
                jsonObject.remove(field.getFIELD_NAME_TOLERANCE());
                jsonObject.remove(field.getFIELD_NAME_conditions());
                jsonObject.remove(field.getFIELD_NAME_ignoreIDInComparision());
                jsonObject.remove(field.getFIELD_NAME_sql());

                JSONArray jsonArrayRTC = jsonObject.getJSONArray(field.getFIELD_NAME_listSlave1());
                for (int j = 0; j < jsonArrayRTC.length(); j++) {
                    JSONObject jsonObjectRTC = jsonArrayRTC.getJSONObject(j);
                    jsonObjectRTC.remove(field.getFIELD_NAME_isSelect());
                    jsonObjectRTC.remove(field.getFIELD_NAME_num());
                    jsonObjectRTC.remove(field.getFIELD_NAME_TOLERANCE());
                    jsonObjectRTC.remove(field.getFIELD_NAME_ignoreIDInComparision());
                }
            }

            return jsonArray.toString();
        } catch (Exception e) {
            System.out.println("执行 RetailTrade.removeJSONArrayofAttribute() 出现异常，错误信息异常" + e.getMessage());

            e.printStackTrace();
        }

        return null;
    }

    /**
     * 用于删除一些JSON当中不需要的属性。
     * 原因：在创建零售单时调用HttpBO需要把此时的JSON数据中有很多不需要的数据remove。从而导致服务器会解析失败
     */
    public String removeJSONofAttribute(String jsonData) {
        System.out.println("正在执行 RetailTrade.removeJSONofAttribute() ，jsonData=" + jsonData);

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            jsonObject.remove(field.getFIELD_NAME_ignoreSyncTypeInComparision());
            jsonObject.remove(field.getFIELD_NAME_isSelect());
            jsonObject.remove(field.getFIELD_NAME_num());
            jsonObject.remove(field.getFIELD_NAME_TOLERANCE());
            jsonObject.remove(field.getFIELD_NAME_conditions());
            jsonObject.remove(field.getFIELD_NAME_ignoreIDInComparision());
            jsonObject.remove(field.getFIELD_NAME_sql());

            JSONArray jsonArrayRTC = jsonObject.getJSONArray(field.getFIELD_NAME_listSlave1());
            for (int j = 0; j < jsonArrayRTC.length(); j++) {
                JSONObject jsonObjectRTC = jsonArrayRTC.getJSONObject(j);
                jsonObjectRTC.remove(field.getFIELD_NAME_isSelect());
                jsonObjectRTC.remove(field.getFIELD_NAME_num());
                jsonObjectRTC.remove(field.getFIELD_NAME_TOLERANCE());
                jsonObjectRTC.remove(field.getFIELD_NAME_conditions());
                jsonObjectRTC.remove(field.getFIELD_NAME_ignoreIDInComparision());
                jsonObjectRTC.remove(field.getFIELD_NAME_sql());
            }

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("执行 RetailTrade.removeJSONofAttribute() 出现异常，错误信息异常" + e.getMessage());
        }

        return null;
    }

    @Override
    public String toJson(BaseModel bm) {
        return BaseHttpBO.getGson().toJson(bm);
    }

    public BaseModel clone() {
        RetailTrade obj = new RetailTrade();
        obj.setID(this.getID());
        obj.setVipID(this.getVipID());
        obj.setSn(new String(this.getSn()));
        obj.setLocalSN(this.getLocalSN());
        obj.setPos_ID(this.getPos_ID());
        obj.setLogo(new String(this.getLogo()));
        if (this.getSaleDatetime() != null) {
            obj.setSaleDatetime((Date) this.getSaleDatetime().clone());//注意此处。必须clone！
        }
        obj.setStaffID(this.getStaffID());
        obj.setPaymentType(this.getPaymentType());
        obj.setPaymentAccount(this.getPaymentAccount());
        obj.setStatus(this.getStatus());
        obj.setRemark(new String(this.getRemark()));
        obj.setSourceID(this.getSourceID());
        if (this.getSyncDatetime() != null) {
            obj.setSyncDatetime((Date) this.getSyncDatetime().clone());
        }
        if (datetimeStart != null) {
            obj.setDatetimeStart((Date) datetimeStart.clone());
        }
        if (datetimeEnd != null) {
            obj.setDatetimeEnd((Date) datetimeEnd.clone());
        }
        obj.setAmount(this.getAmount());
        obj.setAmountPaidIn(this.getAmountPaidIn());
        obj.setAmountChange(this.getAmountChange());
        obj.setAmount1(this.getAmount1());
        obj.setAmount2(this.getAmount2());
        obj.setAmount3(this.getAmount3());
        obj.setAmount4(this.getAmount4());
        obj.setAmount5(this.getAmount5());
        obj.setAmountAlipay(this.getAmountAlipay());
        obj.setAmountCash(this.getAmountCash());
        obj.setAmountWeChat(this.getAmountWeChat());
        obj.setSmallSheetID(this.getSmallSheetID());
        obj.setAliPayOrderSN(this.getAliPayOrderSN());
        obj.setWxOrderSN(this.getWxOrderSN());
        obj.setWxRefundDesc(this.getWxRefundDesc());
        obj.setWxRefundNO(this.getWxRefundNO());
        obj.setWxRefundSubMchID(this.getWxRefundSubMchID());
        obj.setWxTradeNO(this.getWxTradeNO());
        obj.setCouponAmount(couponAmount);
        obj.setConsumerOpenID(consumerOpenID);
        obj.setShopID(shopID);
        if (listSlave1 != null) {
            List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
            for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) this.getListSlave1()) {
                RetailTradeCommodity rc = (RetailTradeCommodity) rtc.clone();
                rtcList.add(rc);
            }
            obj.setListSlave1(rtcList);
        }
        //
        obj.setReturnObject(this.getReturnObject());

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        RetailTrade rt = (RetailTrade) arg0;
        System.out.println("正在比较两个RetailTrade对象：");
        if ((ignoreIDInComparision == true ? true : (rt.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))    //
//                && (ignoreSNInComparision == true ? true : (rt.getSn().equals(this.getSn()) && printComparator(getFIELD_NAME_sn()))) //
                && rt.getLocalSN() == this.getLocalSN() && printComparator(field.getFIELD_NAME_localSN()) //
                && rt.getPos_ID() == this.getPos_ID() && printComparator(field.getFIELD_NAME_pos_ID()) //
                && rt.getLogo().equals(this.getLogo()) && printComparator(field.getFIELD_NAME_logo()) //
//                && DatetimeUtil.compareDate(rt.getSaleDatetime(), this.getSaleDatetime()) && printComparator(getFIELD_NAME_saleDatetime()) //
                && rt.getStaffID() == this.getStaffID() && printComparator(field.getFIELD_NAME_staffID()) //
//                && rt.getPaymentType() == this.getPaymentType() && printComparator(getFIELD_NAME_paymentType()) //
                && rt.getPaymentAccount().equals(this.getPaymentAccount()) && printComparator(field.getFIELD_NAME_paymentAccount()) //
                && rt.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status())//
                && rt.getRemark().equals(this.getRemark()) && printComparator(field.getFIELD_NAME_remark()) //
                && rt.getSourceID() == this.getSourceID() && printComparator(field.getFIELD_NAME_sourceID()) //
//                && DatetimeUtil.compareDate(rt.getSyncDatetime(), this.getSyncDatetime()) && printComparator(getFIELD_NAME_syncDatetime()) //
//                && (ignoreSyncTypeInComparision == true ? true : (rt.getSyncType().equals(this.getSyncType()) && printComparator(getFIELD_NAME_syncType())))
                && Math.abs(GeneralUtil.sub(rt.getAmount(), this.getAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount()) //
                && Math.abs(GeneralUtil.sub(rt.getAmountPaidIn(), this.getAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_amountPaidIn()) //
                && Math.abs(GeneralUtil.sub(rt.getAmountChange(), this.getAmountChange())) < TOLERANCE && printComparator(field.getFIELD_NAME_amountChange()) //
                && Math.abs(GeneralUtil.sub(rt.getAmountWeChat(), this.getAmountWeChat())) < TOLERANCE && printComparator(field.getFIELD_NAME_amountWeChat())//
                && Math.abs(GeneralUtil.sub(rt.getAmountCash(), this.getAmountCash())) < TOLERANCE && printComparator(field.getFIELD_NAME_amountCash())//
                && Math.abs(GeneralUtil.sub(rt.getAmountAlipay(), this.getAmountAlipay())) < TOLERANCE && printComparator(field.getFIELD_NAME_amountAlipay())//
                && Math.abs(GeneralUtil.sub(rt.getAmount1(), this.getAmount1())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount1())//
                && Math.abs(GeneralUtil.sub(rt.getAmount2(), this.getAmount2())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount2())//
                && Math.abs(GeneralUtil.sub(rt.getAmount3(), this.getAmount3())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount3())//
                && Math.abs(GeneralUtil.sub(rt.getAmount4(), this.getAmount4())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount4())//
                && Math.abs(GeneralUtil.sub(rt.getAmount5(), this.getAmount5())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount5())//
                && rt.getSmallSheetID() == this.getSmallSheetID() && printComparator(field.getFIELD_NAME_smallSheetID())//
                && (rt.getAliPayOrderSN() == null ? this.getAliPayOrderSN() == null : rt.getAliPayOrderSN().equals(this.getAliPayOrderSN())) //
                && printComparator(getFIELD_NAME_aliPayOrderSN())//
                && (rt.getWxOrderSN() == null ? this.getWxOrderSN() == null : rt.getWxOrderSN().equals(this.getWxOrderSN())) //
                && printComparator(getFIELD_NAME_wxOrderSN())//
                && (rt.getWxRefundDesc() == null ? this.getWxRefundDesc() == null : rt.getWxRefundDesc().equals(this.getWxRefundDesc())) //
                && printComparator(getFIELD_NAME_wxRefundDesc())//
                && (rt.getWxRefundNO() == null ? this.getWxRefundNO() == null : rt.getWxRefundNO().equals(this.getWxRefundNO())) //
                && printComparator(getFIELD_NAME_wxRefundNO()) //
                && (rt.getWxRefundSubMchID() == null ? this.getWxRefundSubMchID() == null : rt.getWxRefundSubMchID().equals(this.getWxRefundSubMchID())) //
                && printComparator(getFIELD_NAME_wxRefundSubMchID())//
                && (rt.getWxTradeNO() == null ? this.getWxTradeNO() == null : rt.getWxTradeNO().equals(this.getWxTradeNO()))//
                && printComparator(getFIELD_NAME_wxTradeNO())//
                && Math.abs(GeneralUtil.sub(rt.getCouponAmount(), this.getCouponAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_couponAmount())//
                && ((rt.getConsumerOpenID() == null || rt.getConsumerOpenID().equals("null")) ? consumerOpenID == null || consumerOpenID.equals("null") : rt.getConsumerOpenID().equals(consumerOpenID)) && printComparator(field.getFIELD_NAME_consumerOpenID())//
                ) {
            if (!ignoreSlaveListInComparision) {
                if (listSlave1 == null && rt.getListSlave1() == null) {
                    return 0;
                }
                if (listSlave1 == null && rt.getListSlave1() != null) {
                    if (rt.getListSlave1().size() == 0) {
                        return 0;
                    }
                    return -1;
                }
                if (listSlave1 != null && rt.getListSlave1() == null) {
                    if (listSlave1.size() == 0) {
                        return 0;
                    }
                    return -1;
                }
                if (listSlave1 != null && rt.getListSlave1() != null) {
                    if (listSlave1.size() != rt.getListSlave1().size()) {
                        return -1;
                    }
                    for (int i = 0; i < listSlave1.size(); i++) {
                        RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) listSlave1.get(i);
                        retailTradeCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
                        Boolean exist = false;
                        for (int j = 0; j < rt.getListSlave1().size(); j++) {
                            RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
                            if (retailTradeCommodity.getCommodityID() == rtc.getCommodityID()) {
                                exist = true;
                                if (retailTradeCommodity.compareTo(rtc) != 0) {
                                    return -1;
                                }
                                break;
                            }
                        }
                        if (!exist) {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }
        return -1;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    /*
    用于给datetimeStart和datetimeEnd设置为默认时间
     */
    public void setDefaultDate() {
        try {
            datetimeStart = Constants.getDefaultSyncDatetime();
            datetimeEnd = Constants.getDefaultSyncDatetime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getVipID() {
        return this.vipID;
    }

    public void setVipID(long vipID) {
        this.vipID = vipID;
    }

    public String getAliPayOrderSN() {
        return this.aliPayOrderSN;
    }

    public void setAliPayOrderSN(String aliPayOrderSN) {
        this.aliPayOrderSN = aliPayOrderSN;
    }

    public String getWxOrderSN() {
        return this.wxOrderSN;
    }

    public void setWxOrderSN(String wxOrderSN) {
        this.wxOrderSN = wxOrderSN;
    }

    public String getWxTradeNO() {
        return this.wxTradeNO;
    }

    public void setWxTradeNO(String wxTradeNO) {
        this.wxTradeNO = wxTradeNO;
    }

    public String getWxRefundNO() {
        return this.wxRefundNO;
    }

    public void setWxRefundNO(String wxRefundNO) {
        this.wxRefundNO = wxRefundNO;
    }

    public String getWxRefundDesc() {
        return this.wxRefundDesc;
    }

    public void setWxRefundDesc(String wxRefundDesc) {
        this.wxRefundDesc = wxRefundDesc;
    }

    public String getWxRefundSubMchID() {
        return this.wxRefundSubMchID;
    }

    public void setWxRefundSubMchID(String wxRefundSubMchID) {
        this.wxRefundSubMchID = wxRefundSubMchID;
    }

    public String getFIELD_NAME_wxRefundSubMchID() {
        return "wxRefundSubMchID";
    }

    public String getFIELD_NAME_wxRefundDesc() {
        return "wxRefundDesc";
    }

    public String getFIELD_NAME_wxRefundNO() {
        return "wxRefundNO";
    }

    public String getFIELD_NAME_wxTradeNO() {
        return "wxTradeNO";
    }

    public String getFIELD_NAME_wxOrderSN() {
        return "wxOrderSN";
    }

    public String getFIELD_NAME_aliPayOrderSN() {
        return "aliPayOrderSN";
    }

    public boolean getIgnoreSNInComparision() {
        return this.ignoreSNInComparision;
    }

//    public void setFIELD_NAME_ignoreSNInComparision(String FIELD_NAME_ignoreSNInComparision) {
//        this.FIELD_NAME_ignoreSNInComparision = FIELD_NAME_ignoreSNInComparision;
//    }

    @Override
    protected String doCheckRetrieveN(int iCheckCase) {
        StringBuilder sbError = new StringBuilder();

        switch (iCheckCase) {
            case BaseSQLiteBO.CASE_RetailTrade_RetrieveNForReturned:
                if (printCheckField(field.getFIELD_NAME_sourceID(), FIELD_ERROR_sourceID_ForReturned, sbError) && sourceID <= 0) {
                    return sbError.toString();
                }
                break;
            case BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC:
                if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_sn_ForQuery, sbError) && !FieldFormat.checkRetailTradeSN(isForRetailTradeQueryBySN, queryKeyword)) {
                    return sbError.toString();
                }
                break;
            default:
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);

                if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_sn_ForQuery, sbError) && !FieldFormat.checkRetailTradeSN(isForRetailTradeQueryBySN, queryKeyword)) {
                    return sbError.toString();
                }

                try {
                    if (printCheckField(field.getFIELD_NAME_datetimeStart(), FieldFormat.FIELD_ERROR_date, sbError) && datetimeStart != null && !FieldFormat.checkDate(sdf.format(datetimeStart))) {
                        return sbError.toString();
                    }

                    if (printCheckField(field.getFIELD_NAME_datetimeEnd(), FieldFormat.FIELD_ERROR_date, sbError) && datetimeEnd != null && !FieldFormat.checkDate(sdf.format(datetimeEnd))) {
                        return sbError.toString();
                    }
                } catch (Exception e) {
                    return sbError.append(FieldFormat.FIELD_ERROR_date).toString();
                }

                if (printCheckField(field.getFIELD_NAME_vipID(), FieldFormat.FIELD_ERROR_ID, sbError) && vipID != 0 && !FieldFormat.checkID(vipID)) {
                    return sbError.toString();
                }
                break;
        }

        return "";
    }

    // 根据当前时间、pos_id和随机数生成SN号
    public static String generateRetailTradeSN(int pos_id) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_RetailTradeSN);
        String dayStr = sdf.format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        StringBuffer sb = null;
        try {
            Thread.sleep(1);
            sb = new StringBuffer();
            sb.append(RetailTradeSNBegin).append(dayStr);
            sb.append(String.format(FormatWithPosID, pos_id)); // 根据posID,不够四位前面补零
            sb.append(String.valueOf((int) (Math.floor((Math.random() * 9 + 1) * 1000))));
            return sb.toString();
        } catch (Exception e) {
            System.out.println("生成零售单SN失败：" + e.getMessage());
            return null;
        }
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            default:
                if (printCheckField(field.getFIELD_NAME_sn(), FIELD_ERROR_sn, sbError) && !FieldFormat.checkRetailTradeSN(!isForRetailTradeQueryBySN, sn)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_localSN(), FIELD_ERROR_LOCALSN, sbError) && localSN <= Min_LocalSN) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_pos_ID(), FIELD_ERROR_POSID, sbError) && !FieldFormat.checkID(pos_ID)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_paymentType(), FIELD_ERROR_paymentType, sbError) && paymentType < Min_paymentType || paymentType > Max_paymentType) {
                    return sbError.toString();
                }

                if (!StringUtils.isEmpty(remark)) {
                    if (printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && remark.length() > Max_LengthRemark) {
                        return sbError.toString();
                    }
                }

                if (printCheckField(field.getFIELD_NAME_sourceID(), FIELD_ERROR_sourceID, sbError) && sourceID != BaseSQLiteBO.INVALID_INT_ID && sourceID < 1) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount()), sbError) && amount < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountCash()), sbError) && amountCash < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountAlipay()), sbError) && amountAlipay < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountWeChat()), sbError) && amountWeChat < Min_Amount) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount1()), sbError) && amount1 < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount2()), sbError) && amount2 < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount3()), sbError) && amount3 < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount4()), sbError) && amount4 < Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount5()), sbError) && amount5 < Min_Amount) {
                    return sbError.toString();
                }

                if (consumerOpenID != null) {
                    if (printCheckField(field.getFIELD_NAME_consumerOpenID(), String.format(FIELD_ERROR_consumerOpenID, field.getFIELD_NAME_consumerOpenID()), sbError) && consumerOpenID.length() > Max_LengthConsumerOpenID) {
                        return sbError.toString();
                    }
                }

                if (printCheckField(field.getFIELD_NAME_smallSheetID(), FIELD_ERROR_smallSheetID, sbError) && !FieldFormat.checkID(smallSheetID)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_datetimeStart(), FIELD_ERROR_datetimeStart, sbError) && datetimeStart == null) {//此检查防止转RetailTrade换为JSON数据时发生错误
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_datetimeEnd(), FIELD_ERROR_datetimeEnd, sbError) && datetimeEnd == null) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_saleDatetime(), FIELD_ERROR_saleDatetime, sbError) && saleDatetime == null) {
                    return sbError.toString();
                }

                // 检查：每种支付方式对应的钱加起来=应收款
                if (printCheckField(field.getFIELD_NAME_amount(), FIELD_ERROR_amountTotal, sbError) //
                        && Math.abs(GeneralUtil.sub(amount, GeneralUtil.sumN(amountCash, amountAlipay, amountWeChat, amount1, amount2, amount3, amount4, amount5))) >= TOLERANCE) {
                    return sbError.toString();
                }

                // 如果只有现金支付，则可以为0.000000d，如果混合支付带有现金支付，那么现金支付不可为0.000000d
                if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountCash(), field.getFIELD_NAME_amountCash()), sbError) && paymentType != 1 && (paymentType & 1) == 1
                        && amountCash <= Min_Amount) {
                    return sbError.toString();
                }

                // 检查：支付方式为2，对应的钱应该>0.000000d
                if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountAlipay(), field.getFIELD_NAME_amountAlipay()), sbError) && (paymentType & 2) == 2
                        && amountAlipay <= Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountWeChat(), field.getFIELD_NAME_amountWeChat()), sbError) && (paymentType & 4) == 4
                        && amountWeChat <= Min_Amount) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount1(), field.getFIELD_NAME_amount1()), sbError) && (paymentType & 8) == 8 && amount1 <= Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount2(), field.getFIELD_NAME_amount2()), sbError) && (paymentType & 16) == 16 && amount2 <= Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount3(), field.getFIELD_NAME_amount3()), sbError) && (paymentType & 32) == 32 && amount3 <= Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount4(), field.getFIELD_NAME_amount4()), sbError) && (paymentType & 64) == 64 && amount4 <= Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount5(), field.getFIELD_NAME_amount5()), sbError) && (paymentType & 128) == 128 && amount5 <= Min_Amount) {
                    return sbError.toString();
                }

                // 如果没有这种支付方式，那么这种支付金额要为0；
                if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountCash(), field.getFIELD_NAME_amountCash()), sbError) && (paymentType & 1) == 0 && amountCash != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountAlipay(), field.getFIELD_NAME_amountAlipay()), sbError) && (paymentType & 2) == 0 && amountAlipay != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountWeChat(), field.getFIELD_NAME_amountWeChat()), sbError) && (paymentType & 4) == 0 && amountWeChat != Min_Amount) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount1(), field.getFIELD_NAME_amount1()), sbError) && (paymentType & 8) == 0 && amount1 != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount2(), field.getFIELD_NAME_amount2()), sbError) && (paymentType & 16) == 0 && amount2 != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount3(), field.getFIELD_NAME_amount3()), sbError) && (paymentType & 32) == 0 && amount3 != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount4(), field.getFIELD_NAME_amount4()), sbError) && (paymentType & 64) == 0 && amount4 != Min_Amount) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount5(), field.getFIELD_NAME_amount5()), sbError) && (paymentType & 128) == 0 && amount5 != Min_Amount) {
                    return sbError.toString();
                }

                if (sourceID > 0) {
                    //如果是创建临时单在本地时，需要作以下检查。如果是上传临时单到服务器，其实不需要作以下检查，但这里还是作了检查
                    // 检查：退款时，支付宝退的钱不能多过购买时支付宝支付的钱，WX退的钱不能多过购买时WX支付的钱
                    if (printCheckField(field.getFIELD_NAME_amountAlipay(), FIELD_ERROR_saleAfterReturnForAmountAlipay, sbError) && saleAmountAlipay < amountAlipay) {
                        return sbError.toString();
                    }
                    if (printCheckField(field.getFIELD_NAME_amountWeChat(), FIELD_ERROR_saleAfterReturnForAmountWeChat, sbError) && saleAmountWeChat < amountWeChat) {
                        return sbError.toString();
                    }
                }

                if (listSlave1 != null) {
                    Integer[] iCommodityID = new Integer[listSlave1.size()];
                    for (int i = 0; i < listSlave1.size(); i++) {
                        System.out.println("￥￥￥准备检查从表字段");
                        RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) listSlave1.get(i);
                        String err = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                        if (!"".equals(err)) {
                            return err;
                        }
                        iCommodityID[i] = retailTradeCommodity.getCommodityID();
                    }

                    if (printCheckField(field.getFIELD_NAME_listSlave1(), FIELD_ERROR_listSlave1, sbError) && GeneralUtil.hasDuplicatedElement(iCommodityID)) {
                        return sbError.toString();
                    }
                }

                break;
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

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
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

    /**
     * 判断当前零售单是否全部使用现金支付
     */
    public boolean payViaCashOnly() {
        return paymentType == 1;
    }

    /**
     * 判断当前零售单是否全部使用微信支付
     */
    public boolean payViaWechatOnly() {
        return paymentType == 4;
    }

    public String getConsumerOpenID() {
        return this.consumerOpenID;
    }

    public void setConsumerOpenID(String consumerOpenID) {
        this.consumerOpenID = consumerOpenID;
    }

    public int getSlaveCreated() {
        return this.SlaveCreated;
    }

    public void setSlaveCreated(int SlaveCreated) {
        this.SlaveCreated = SlaveCreated;
    }
}
