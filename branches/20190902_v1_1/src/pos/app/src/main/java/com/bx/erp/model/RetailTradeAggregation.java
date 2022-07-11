package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(nameInDb = "retailTradeAggregation")
public class RetailTradeAggregation extends BaseModel {
    public static final int Min_ID = 0;
    public static final int Min_TradeNO = 0;
    public static final double Min_ReserveAmount = 0.000000d;
    public static final double Max_ReserveAmount = 10000.000000d;

    public static final String FIELD_ERROR_StaffID = "StaffID不能小于" + Min_ID;
    public static final String FIELD_ERROR_PosID = "PosID不能小于" + Min_ID;
    public static final String FIELD_ERROR_TradeNO = "TradeNO不能小于" + Min_TradeNO;
    public static final String FIELD_ERROR_ReserveAmount = "准备金在[" + Min_ReserveAmount + "," + Max_ReserveAmount + "]之间";
    public static final String FIELD_ERROR_workTimeStartAndEnd = "上班时间不能比下班时间晚";
    public static final String FIELD_ERROR_workTimeStart = "上班时间不能为null";
    public static final String FIELD_ERROR_workTimeEnd = "下班时间不能为null";
    public static final RetailTradeAggregationField field = new RetailTradeAggregationField();

    @Transient
    public final static String HTTP_URL_CREATE = "retailTradeAggregation/createEx.bx";

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    private Long ID;

    @NotNull
    @Property(nameInDb = "F_StaffID")
    private int staffID;

    @NotNull
    @Property(nameInDb = "F_PosID")
    private int posID;

    @NotNull
    @Property(nameInDb = "F_WorkTimeStart")
    private Date workTimeStart;

    @Property(nameInDb = "F_WorkTimeEnd")
    private Date workTimeEnd;

    @NotNull
    @Property(nameInDb = "F_TradeNO")
    private int tradeNO;

    @NotNull
    @Property(nameInDb = "F_Amount")
    private double amount;

    @NotNull
    @Property(nameInDb = "F_ReserveAmount")
    private double reserveAmount;

    @Property(nameInDb = "F_CashAmount")
    private double cashAmount;

    @Property(nameInDb = "F_WechatAmount")
    private double wechatAmount;

    @Property(nameInDb = "F_AlipayAmount")
    private double alipayAmount;

    @Property(nameInDb = "F_Amount1")
    private double amount1;

    @Property(nameInDb = "F_Amount2")
    private double amount2;

    @Property(nameInDb = "F_Amount3")
    private double amount3;

    @Property(nameInDb = "F_Amount4")
    private double amount4;

    @Property(nameInDb = "F_Amount5")
    private double amount5;

    @Property(nameInDb = "F_UploadDateTime")
    private Date uploadDateTime;

    @Property(nameInDb = "F_SyncDatetime")
    private Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    private String syncType;

    @Generated(hash = 296870070)
    public RetailTradeAggregation(Long ID, int staffID, int posID, @NotNull Date workTimeStart, Date workTimeEnd, int tradeNO, double amount, double reserveAmount, double cashAmount, double wechatAmount, double alipayAmount,
                                  double amount1, double amount2, double amount3, double amount4, double amount5, Date uploadDateTime, Date syncDatetime, String syncType) {
        this.ID = ID;
        this.staffID = staffID;
        this.posID = posID;
        this.workTimeStart = workTimeStart;
        this.workTimeEnd = workTimeEnd;
        this.tradeNO = tradeNO;
        this.amount = amount;
        this.reserveAmount = reserveAmount;
        this.cashAmount = cashAmount;
        this.wechatAmount = wechatAmount;
        this.alipayAmount = alipayAmount;
        this.amount1 = amount1;
        this.amount2 = amount2;
        this.amount3 = amount3;
        this.amount4 = amount4;
        this.amount5 = amount5;
        this.uploadDateTime = uploadDateTime;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
    }

    @Generated(hash = 41033428)
    public RetailTradeAggregation() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getPosID() {
        return posID;
    }

    public void setPosID(int posID) {
        this.posID = posID;
    }

    public Date getWorkTimeStart() {
        return workTimeStart;
    }

    public void setWorkTimeStart(Date workTimeStart) {
        this.workTimeStart = workTimeStart;
    }

    public Date getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(Date workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }

    public int getTradeNO() {
        return tradeNO;
    }

    public void setTradeNO(int tradeNO) {
        this.tradeNO = tradeNO;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getReserveAmount() {
        return reserveAmount;
    }

    public void setReserveAmount(double reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getWechatAmount() {
        return wechatAmount;
    }

    public void setWechatAmount(double wechatAmount) {
        this.wechatAmount = wechatAmount;
    }

    public double getAlipayAmount() {
        return alipayAmount;
    }

    public void setAlipayAmount(double alipayAmount) {
        this.alipayAmount = alipayAmount;
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

    public Date getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(Date uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    public String getSyncType() {
        return this.syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @Override
    public String toString() {
        return "RetailTradeAggregation{" +
                ", ID=" + ID +
                ", staffID=" + staffID +
                ", posID=" + posID +
                ", workTimeStart=" + workTimeStart +
                ", workTimeEnd=" + workTimeEnd +
                ", tradeNO=" + tradeNO +
                ", amount=" + amount +
                ", reserveAmount=" + reserveAmount +
                ", cashAmount=" + cashAmount +
                ", wechatAmount=" + wechatAmount +
                ", alipayAmount=" + alipayAmount +
                ", amount1=" + amount1 +
                ", amount2=" + amount2 +
                ", amount3=" + amount3 +
                ", amount4=" + amount4 +
                ", amount5=" + amount5 +
                ", uploadDateTime=" + uploadDateTime +
                ", syncDatetime=" + syncDatetime +
                ", syncType='" + syncType + '\'' +
                '}';
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        RetailTradeAggregation obj = new RetailTradeAggregation();
        obj.setID(this.getID());
        obj.setPosID(this.getPosID());
        obj.setStaffID(this.getStaffID());
        obj.setWorkTimeStart((Date) this.getWorkTimeStart().clone());
        obj.setWorkTimeEnd((Date) this.getWorkTimeEnd().clone());
        obj.setTradeNO(this.getTradeNO());
        obj.setAmount(this.getAmount());
        obj.setReserveAmount(this.getReserveAmount());
        obj.setCashAmount(this.getCashAmount());
        obj.setWechatAmount(this.getWechatAmount());
        obj.setAlipayAmount(this.getAlipayAmount());
        obj.setAmount1(this.getAmount1());
        obj.setAmount2(this.getAmount2());
        obj.setAmount3(this.getAmount3());
        obj.setAmount4(this.getAmount4());
        obj.setAmount5(this.getAmount5());
        return obj;
    }

    @Override
    public int compareTo(BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        RetailTradeAggregation rta = (RetailTradeAggregation) arg0;
        if ((ignoreIDInComparision == true ? true : (rta.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))  //
                && rta.getStaffID() == this.getStaffID() && printComparator(field.getFIELD_NAME_staffID()) //
                && rta.getPosID() == this.getPosID() && printComparator(field.getFIELD_NAME_posID()) //
                && DatetimeUtil.compareDate(rta.getWorkTimeStart(), this.getWorkTimeStart()) && printComparator(field.getFIELD_NAME_workTimeStart()) //
                && DatetimeUtil.compareDate(rta.getWorkTimeEnd(), this.getWorkTimeEnd()) && printComparator(field.getFIELD_NAME_workTimeEnd()) //
                && rta.getTradeNO() == this.getTradeNO() && printComparator(field.getFIELD_NAME_tradeNO()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount(), this.getAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount()) //
                && Math.abs(GeneralUtil.sub(rta.getReserveAmount(), this.getReserveAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_reserveAmount()) //
                && Math.abs(GeneralUtil.sub(rta.getCashAmount(), this.getCashAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_cashAmount()) //
                && Math.abs(GeneralUtil.sub(rta.getWechatAmount(), this.getWechatAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_wechatAmount())//
                && Math.abs(GeneralUtil.sub(rta.getAlipayAmount(), this.getAlipayAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_alipayAmount()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount1(), this.getAmount1())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount1()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount2(), this.getAmount2())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount2()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount3(), this.getAmount3())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount3()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount4(), this.getAmount4())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount4()) //
                && Math.abs(GeneralUtil.sub(rta.getAmount5(), this.getAmount5())) < TOLERANCE && printComparator(field.getFIELD_NAME_amount5()) //
                && DatetimeUtil.compareDate(rta.getUploadDateTime(), this.getUploadDateTime()) && printComparator(field.getFIELD_NAME_uploadDateTime()) //
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public List<BaseModel> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 RetailTradeAggregation.parseN() ，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> retailTradeAggregationList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
                retailTradeAggregation.doParse1(jsonObject);
                retailTradeAggregationList.add(retailTradeAggregation);
            }
        } catch (Exception e) {
            System.out.println("执行 RetailTradeAggregation.parseN() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
        return retailTradeAggregationList;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 RetailTradeAggregation.parse1() ，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(new JSONObject(s));
        } catch (Exception e) {
            System.out.println("执行 RetailTradeAggregation.parse1() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 RetailTradeAggregation.doParse1() ，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            staffID = jo.getInt(field.getFIELD_NAME_staffID());
            posID = jo.getInt(field.getFIELD_NAME_posID());
            tradeNO = jo.getInt(field.getFIELD_NAME_tradeNO());
            amount = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount()));
            reserveAmount = Double.parseDouble(jo.getString(field.getFIELD_NAME_reserveAmount()));
            cashAmount = Double.parseDouble(jo.getString(field.getFIELD_NAME_cashAmount()));
            wechatAmount = Double.parseDouble(jo.getString(field.getFIELD_NAME_wechatAmount()));
            alipayAmount = Double.parseDouble(jo.getString(field.getFIELD_NAME_alipayAmount()));
            amount1 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount1()));
            amount2 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount2()));
            amount3 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount3()));
            amount4 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount4()));
            amount5 = Double.parseDouble(jo.getString(field.getFIELD_NAME_amount5()));
            syncType = jo.getString(field.getFIELD_NAME_syncType());

            String tmpWorkTimeStart = jo.getString(field.getFIELD_NAME_workTimeStart());
            if (!StringUtils.isEmpty(tmpWorkTimeStart)) {
                workTimeStart = Constants.getSimpleDateFormat2().parse(tmpWorkTimeStart);
                if (workTimeStart == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_workTimeStart() + "=" + tmpWorkTimeStart);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_workTimeStart() + "=" + tmpWorkTimeStart);
                }
            }

            String tmpWorkTimeEnd = jo.getString(field.getFIELD_NAME_workTimeEnd());
            if (!StringUtils.isEmpty(tmpWorkTimeEnd)) {
                workTimeEnd = Constants.getSimpleDateFormat2().parse(tmpWorkTimeEnd);
                if (workTimeEnd == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_workTimeEnd() + "=" + tmpWorkTimeEnd);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_workTimeEnd() + "=" + tmpWorkTimeEnd);
                }
            }

//            String tmpUploadDatetime = jo.getString(getFIELD_NAME_uploadDateTime());
//            if (!StringUtils.isEmpty(tmpUploadDatetime)) {
//                uploadDateTime = Constants.getSimpleDateFormat2().parse(tmpUploadDatetime);
//                if (uploadDateTime == null) {
//                    throw new RuntimeException("无法解析该日期：" + getFIELD_NAME_uploadDateTime() + "=" + tmpUploadDatetime);
//                }
//            }

            String tmpSyncDatetime = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmpSyncDatetime)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmpSyncDatetime);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                }
            }
            return this;
        } catch (Exception e) {
            System.out.println("执行 RetailTradeAggregation.doParse1() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_posID(), FIELD_ERROR_PosID, sbError) && !FieldFormat.checkID(posID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_tradeNO(), FIELD_ERROR_TradeNO, sbError) && tradeNO < 0) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_workTimeStart(), FIELD_ERROR_workTimeStart, sbError) && workTimeStart == null) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeEnd, sbError) && workTimeEnd == null) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_reserveAmount(), FIELD_ERROR_ReserveAmount, sbError) && (reserveAmount < Min_ReserveAmount || reserveAmount > Max_ReserveAmount)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_workTimeStart() + "," + field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeStartAndEnd, sbError) && workTimeStart.getTime() >= workTimeEnd.getTime()) {
            return sbError.toString();
        }
        return "";
    }


    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_posID(), FIELD_ERROR_PosID, sbError) && !FieldFormat.checkID(posID)) //
        {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_tradeNO(), FIELD_ERROR_TradeNO, sbError) && tradeNO < 0) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_workTimeStart(), FIELD_ERROR_workTimeStart, sbError) && workTimeStart == null) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeEnd, sbError) && workTimeEnd == null) //
        {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_reserveAmount(), FIELD_ERROR_ReserveAmount, sbError) && (reserveAmount < Min_ReserveAmount || reserveAmount > Max_ReserveAmount)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_workTimeStart() + "," + field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeStartAndEnd, sbError) && workTimeStart.getTime() >= workTimeEnd.getTime()) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
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
