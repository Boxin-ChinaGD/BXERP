package com.bx.erp.model;


import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.utils.DatetimeUtil;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "returnCommoditySheet")
public class ReturnCommoditySheet extends BaseModel {//TODO 没有对应从表的Model,后期看需求决定是否添加
    public static final int MaxStringLength = 32;

    public static final String FIELD_ERROR_staffID = "staffID必须大于0";
    public static final String FIELD_ERROR_providerID = "providerID必须大于0";
    public static final String FIELD_ERROR_string1 = "string1的长度不能大于" + MaxStringLength;
    public static final String FIELD_ERROR_status = "状态必须是-1," + EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex() + "或" + EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex();
    public static final String FIELD_ERROR_date1AndDate2 = "非法时间格式！正确的时间格式应为：" + Constants.DATE_FORMAT_Default;

    public static final String HTTP_ReturnCommoditySheety_CREATE = "returnCommoditySheet/createEx.bx";
    public static final String HTTP_ReturnCommoditySheety_APPROVE = "returnCommoditySheet/approveEx.bx";

    public static final ReturnCommoditySheetField field = new ReturnCommoditySheetField();

    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_StaffID")
    protected int staffID;

    @NotNull
    @Property(nameInDb = "F_ProviderID")
    protected int providerID;

    @NotNull
    @Property(nameInDb = "F_CreateDate")
    protected Date createDate;

    @NotNull
    @Property(nameInDb = "F_Status")
    protected int status;

    @NotNull
    @Property(nameInDb = "F_SN")
    protected String sn;

    @Generated(hash = 278235450)
    public ReturnCommoditySheet(Long ID, int staffID, int providerID, @NotNull Date createDate, int status,
            @NotNull String sn) {
        this.ID = ID;
        this.staffID = staffID;
        this.providerID = providerID;
        this.createDate = createDate;
        this.status = status;
        this.sn = sn;
    }

    @Generated(hash = 1252188068)
    public ReturnCommoditySheet() {
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    @Override
    public String toString() {
        return "ReturnCommoditySheet{" +
                "ID=" + ID +
                ", staffID=" + staffID +
                ", providerID=" + providerID +
                ", createDate=" + createDate +
                ", status=" + status +
                ", sn='" + sn + '\'' +
                '}';
    }

    public BaseModel clone() {
        ReturnCommoditySheet obj = new ReturnCommoditySheet();
        obj.setID(ID);
        obj.setProviderID(providerID);
        obj.setCreateDate((Date) createDate.clone());
        obj.setStatus(status);
        obj.setStaffID(staffID);
        obj.setSn(sn);

        return obj;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 ReturnCommoditySheet.parse1，s=" + s);

        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("执行 ReturnCommoditySheet.parse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 ReturnCommoditySheet.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {

            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            sn = jo.getString(field.getFIELD_NAME_sn());
            providerID = jo.getInt(field.getFIELD_NAME_providerID());
            status = jo.getInt(field.getFIELD_NAME_status());
            staffID = jo.getInt(field.getFIELD_NAME_staffID());

            String tmpcreateDate = jo.getString(field.getFIELD_NAME_createDate());
            if (!StringUtils.isEmpty(tmpcreateDate)) {
                createDate = Constants.getSimpleDateFormat2().parse(tmpcreateDate);
                if(createDate == null){
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpcreateDate);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpcreateDate);
                }
            }

            return this;
        } catch (Exception e) {
            System.out.println("执行 ReturnCommoditySheet.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 ReturnCommoditySheet.parseN，s=" + s);

        List<BaseModel> bmList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                ReturnCommoditySheet c = new ReturnCommoditySheet();
                c.doParse1(jsonObject1);
                bmList.add(c);
            }
        } catch (Exception e) {
            System.out.println("执行 ReturnCommoditySheet.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return bmList;
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);

        return json;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        ReturnCommoditySheet rcs = (ReturnCommoditySheet) arg0;
        if ((ignoreIDInComparision == true ? true : (rcs.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
                && rcs.getProviderID() == providerID && printComparator(field.getFIELD_NAME_providerID()) //
                && rcs.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
                && rcs.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID())//
                ) {

            return 0;
        }

        return -1;
    }

    public enum EnumStatusReturnCommoditySheet {
        ESRCS_ToApprove("ToApprove", 0), //
        ESRCS_Approved("Approved", 1);

        private String name;
        private int index;

        private EnumStatusReturnCommoditySheet(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static boolean inBoundForRetrieveN(int index) {
            if (index != BaseSQLiteBO.INVALID_STATUS && index != ESRCS_ToApprove.getIndex() && index != ESRCS_Approved.getIndex()) {
                return false;
            }
            return true;
        }

        public static String getName(int index) {
            for (EnumStatusReturnCommoditySheet c : EnumStatusReturnCommoditySheet.values()) {
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
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_ReturnCommoditySheet_Approve:
                if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }
                return "";
            default:
                if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
                    return sbError.toString();
                }
                return "";
        }
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            default:
                queryKeyword = queryKeyword == null ? "" : queryKeyword; // stirng1为null时默认是"";
                if (this.printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_string1, sbError) && queryKeyword.length() > MaxStringLength) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseSQLiteBO.INVALID_INT_ID) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !EnumStatusReturnCommoditySheet.inBoundForRetrieveN(status)) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_providerID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_providerID()), sbError) && providerID < BaseSQLiteBO.INVALID_INT_ID) {
                    return sbError.toString();
                }

                return "";
        }
    }

    @Override
    public String checkDelete(int iUseCaseID){
        return "";
    }
}
