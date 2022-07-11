package com.bx.erp.model;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.StringUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
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

import static com.bx.erp.model.RetailTradeCommodity.FIELD_ERROR_CommodityID;
import static com.bx.erp.model.RetailTradeCommodity.field;


@Entity(nameInDb = "T_Barcodes")
public class Barcodes extends BaseModel {

    public static final int MIN_LENGTH_Barcodes = 7;
    public static final int MAX_LENGTH_Barcodes = 64;

    public static final String FIELD_ERROR_barcodes = "商品条形码只能是英文或数字的组合，长度在" + MIN_LENGTH_Barcodes + "~" + MAX_LENGTH_Barcodes + "之间";
    public static final String FIELD_ERROR_StaffID = "员工ID不能小于0";
    public static final String FIELD_ERROR_CommodityID = "商品ID不能小于0";

    @Transient
    public static final String HTTP_Barcodes_Create = "barcodesSync/createEx.bx";
    @Transient
    public static final String feedbackURL_FRONT = "barcodesSync/feedbackEx.bx?sID=";
    @Transient
    public static final String feedbackURL_BEHINE = "&errorCode=EC_NoError";
    @Transient
    public static final String HTTP_BARCODES_RetrieveN = "barcodesSync/retrieveNEx.bx";
    @Transient
    public static final String HTTP_Barcodes_RETRIEVENC_PageIndex = "barcodes/retrieveNEx.bx?pageIndex=";
    @Transient
    public static final String HTTP_Barcodes_RETRIEVENC_PageSize = "&pageSize=";
    @Transient
    public static final String HTTP_Barcodes_RETRIEVENC_TypeStatus = "&type=-1&status=-1";

    public static final String PAGEINDEX_START = "start";
    public static final String PAGEINDEX_END = "end";
    public static final BarcodesField field = new BarcodesField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_CommodityID")
    protected int commodityID;

    @NotNull
    @Property(nameInDb = "F_Barcode")
    protected String barcode;

    @Property(nameInDb = "F_CreateDatetime")
    protected Date createDatetime;

    @Property(nameInDb = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @Generated(hash = 1294427097)
    public Barcodes() {
    }

    @Generated(hash = 2080200417)
    public Barcodes(Long ID, int commodityID, @NotNull String barcode, Date createDatetime, Date updateDatetime, Date syncDatetime, String syncType) {
        this.ID = ID;
        this.commodityID = commodityID;
        this.barcode = barcode;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public String getBarcode() {
        return barcode;
    }

    public String toString() {
        return "Barcodes commodityID=" + commodityID + ", barcode=" + barcode + "]";
    }

    @Override
    public Long getID() {
        return this.ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Barcodes b = new Barcodes();
        b.setID(this.getID());
        b.setCommodityID(this.getCommodityID());
        b.setBarcode(new String(this.getBarcode()));
        b.setReturnObject(this.getReturnObject());
        b.setSyncType(this.getSyncType());
        b.setOperatorStaffID(operatorStaffID);

        return b;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        Barcodes b = (Barcodes) arg0;
        if ((ignoreIDInComparision == true ? true : (b.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && b.getCommodityID() == this.getCommodityID() && printComparator(field.getFIELD_NAME_commodityID()) //
                && b.getBarcode().equals(this.getBarcode()) && printComparator(field.getFIELD_NAME_barcode()) //
                && b.getSyncType().equals(this.getSyncType()) && printComparator(field.getFIELD_NAME_syncType()) //
                ) {
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


    public Date getSyncDatetime() {
        return this.syncDatetime;
    }


    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行Barcodes的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            barcode = jo.getString(field.getFIELD_NAME_barcode());
            commodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_commodityID()));
            syncSequence = jo.getInt(field.getFIELD_NAME_syncSequence());
            operatorStaffID = jo.getInt(field.getFIELD_NAME_operatorStaffID());
            syncType = jo.getString(field.getFIELD_NAME_syncType());
            //
            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }
            //
            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }
            //
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
            System.out.println("执行doParse1异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Barcodes的parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("执行parse1异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray jsonArray1) {
        System.out.println("正在执行Barcodes的parseN，jsonArray1=" + (jsonArray1 == null ? null : jsonArray1.toString()));

        List<BaseModel> barcodesList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Barcodes b = new Barcodes();
                b.doParse1(jsonObject1);
                barcodesList.add(b);
            }
        } catch (Exception e) {
            System.out.println("执行parseN异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return barcodesList;
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }


    public Date getCreateDatetime() {
        return this.createDatetime;
    }


    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }


    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }


    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            default:
                if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) //
                {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
                    return sbError.toString();
                }

                // operatorStaffID用于传staffID
                if (this.printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(operatorStaffID)) //
                {
                    return sbError.toString();
                }
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_String:
                        if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(conditions[0])) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long:
                        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_String_Long:
                        if (conditions.length == 2) {
                            if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(conditions[0])) {
                                return sbError.toString();
                            }
                            if (this.printCheckField(field.getFIELD_NAME_commodityID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[1]))) {
                                return sbError.toString();
                            }
                        } else {
                            return FieldFormat.FIELD_ERROR_Parameter;
                        }
                        break;
                    case ESUC_Ignore:
                        break;
                    default:
                        throw new RuntimeException("未定义的Sub Use Case ID！");
                }
                break;
            default:
                break;
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Barcodes_DeleteNByConditions:
                switch (subUseCaseID) {
                    case ESUC_String:
                        if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(conditions[0])) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long:
                        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_String_Long:
                        if (conditions.length == 2) {
                            if (this.printCheckField(field.getFIELD_NAME_barcode(), FIELD_ERROR_barcodes, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(conditions[0])) {
                                return sbError.toString();
                            }
                            if (this.printCheckField(field.getFIELD_NAME_commodityID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[1]))) {
                                return sbError.toString();
                            }
                        } else {
                            return FieldFormat.FIELD_ERROR_Parameter;
                        }
                        break;
                    case ESUC_Ignore:
                        break;
                    default:
                        throw new RuntimeException("未定义的Sub Use Case ID！");
                }
                break;
            default:
                if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
                {
                    return sbError.toString();
                }
                break;
        }
        return "";
    }
}
