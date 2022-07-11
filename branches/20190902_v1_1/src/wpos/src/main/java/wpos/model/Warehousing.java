package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;
//import com.google.gson.Gson;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.NotNull;
//import org.greenrobot.greendao.annotation.Property;
//import org.greenrobot.greendao.annotation.Transient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

////@Entity(nameInDb = "warehousing")
public class Warehousing extends BaseModel {

    public static final int MaxStringLength = 32;

    public static final String FIELD_ERROR_string1 = "string1的长度不能大于" + MaxStringLength;
    public static final String FIELD_ERROR_status = "状态必须是-1," + EnumStatusWarehousing.ESW_ToApprove.getIndex() + "或" + EnumStatusWarehousing.ESW_Approved.getIndex();
    public static final String FIELD_ERROR_date1AndDate2 = "非法时间格式！正确的时间格式应为：" + Constants.DATE_FORMAT_Default;
    public static final String FIELD_ERROR_staffID = "staffID必须大于0";
    public static final String FIELD_ERROR_warehouseID = "warehouseID必须大于0";
    public static final String FIELD_ERROR_purchasingOrderID = "purchasingOrderID必须大于0";
    public static final String FIELD_ERROR_approverID = "approverID必须大于0";
    public static final String FIELD_ERROR_providerID = "providerID必须大于0";

    //    public static final String HTTP_WAREHOUSING_RETRIEVENCOMM = "warehousing/retrieveNCommEx.bx";
    public static final String HTTP_WAREHOUSING_CREATE = "warehousing/createEx.bx";
    public static final String HTTP_WAREHOUSING_APPROVE = "warehousing/approveEx.bx";
    public static final WarehousingField field = new WarehousingField();
    //@Id
    ////@Property(nameInDb = "F_ID")
    protected Integer ID;

    //@NotNull
    //@Property(nameInDb = "F_ProviderID")
    protected Long providerID;

    //@NotNull
    //@Property(nameInDb = "F_Status")
    protected int status;

    //@NotNull
    //@Property(nameInDb = "F_WarehouseID")
    protected int warehouseID;

    //@NotNull
    //@Property(nameInDb = "F_StaffID")
    protected int staffID;

    //@Property(nameInDb = "F_ApproverID")
    protected int approverID;

    //@NotNull
    //@Property(nameInDb = "F_CreateDatetime")
    protected Date createDatetime;

    //@NotNull
    //@Property(nameInDb = "F_PurchasingOrderID")
    protected int purchasingOrderID;

    //@NotNull
    //@Property(nameInDb = "F_SN")
    protected String sn;

    //@Transient
    protected int isModified;


    //@Generated(hash = 1424696455)
//    public Warehousing(Long ID, //@NotNull Long providerID, int status, int warehouseID, int staffID, int approverID, //@NotNull Date createDatetime, int purchasingOrderID,
//            //@NotNull String sn) {
//        this.ID = ID;
//        this.providerID = providerID;
//        this.status = status;
//        this.warehouseID = warehouseID;
//        this.staffID = staffID;
//        this.approverID = approverID;
//        this.createDatetime = createDatetime;
//        this.purchasingOrderID = purchasingOrderID;
//        this.sn = sn;
//    }

    //@Generated(hash = 1409027642)
//    public Warehousing() {
//    }


    public int getApproverID() {
        return approverID;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

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

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getPurchasingOrderID() {
        return purchasingOrderID;
    }

    public void setPurchasingOrderID(int purchasingOrderID) {
        this.purchasingOrderID = purchasingOrderID;
    }

    public int getIsModified() {
        return isModified;
    }

    public void setIsModified(int isModified) {
        this.isModified = isModified;
    }

    public BaseModel clone() {
        Warehousing obj = new Warehousing();
        obj.setID(ID);
        obj.setStatus(status);
        obj.setWarehouseID(warehouseID);
        obj.setStaffID(staffID);
        obj.setApproverID(approverID);
        obj.setCreateDatetime((Date) createDatetime.clone());
        obj.setPurchasingOrderID(purchasingOrderID);
        obj.setSn(sn);
        obj.setIsModified(isModified);
        //
        if (this.listSlave1 != null) {
            List<WarehousingCommodity> list = new ArrayList<WarehousingCommodity>();
            for (Object o : listSlave1) {
                WarehousingCommodity wc = (WarehousingCommodity) o;
                list.add((WarehousingCommodity) wc.clone());
            }
            obj.setListSlave1(list);
        }

        return obj;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 Warehousing.parse1，s=" + s);

        try {
            JSONObject jo = JSONObject.parseObject(s);
            Warehousing ws = (Warehousing) doParse1(jo);
            if (ws == null) {
                return null;
            }
            JSONArray array = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
            if (array != null) {
                WarehousingCommodity wc = new WarehousingCommodity();
                List<WarehousingCommodity> list = (List<WarehousingCommodity>) wc.parseN(array);
                if (list == null) {
                    return null;
                }
                ws.setListSlave1(list);  //非常关键！！
            }
            return ws;
        } catch (JSONException e) {
            System.out.println("Warehousing.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 Warehousing.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            sn = jo.getString(field.getFIELD_NAME_sn());
            status = jo.getInteger(field.getFIELD_NAME_status());
            warehouseID = jo.getInteger(field.getFIELD_NAME_warehouseID());
            staffID = jo.getInteger(field.getFIELD_NAME_staffID());
            approverID = jo.getInteger(field.getFIELD_NAME_approverID());
            providerID = Long.valueOf(jo.getString(field.getFIELD_NAME_providerID()));
            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println(field.getFIELD_NAME_createDatetime() + "解析失败！" + "=" + tmpCreateDatetime);
                    throw new RuntimeException(field.getFIELD_NAME_createDatetime() + "解析失败！" + "=" + tmpCreateDatetime);
                }
            }
            purchasingOrderID = jo.getInteger(field.getFIELD_NAME_purchasingOrderID());

            return this;
        } catch (Exception e) {
            System.out.println("Warehousing.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 Warehousing.parseN，s=" + s);

        List<BaseModel> warehousingList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray = JSONArray.parseArray(s);
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Warehousing warehousing = (Warehousing) doParse1(jsonObject1);
                if (warehousing != null) {
                    warehousingList.add(warehousing);
                }
            }
        } catch (Exception e) {
            System.out.println("Warehousing.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return warehousingList;
    }

    public Long getProviderID() {
        return this.providerID;
    }

    public void setProviderID(Long providerID) {
        this.providerID = providerID;
    }

    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_warehouseID(), FIELD_ERROR_warehouseID, sbError) && !FieldFormat.checkID(warehouseID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) {
            return sbError.toString();
        }

        if (listSlave1 != null) {
            List<WarehousingCommodity> wcList = (List<WarehousingCommodity>) listSlave1;
            for (WarehousingCommodity wc : wcList) {
                System.out.println("￥￥￥准备检查从表字段");
                String err = wc.checkCreate(iUseCaseID);
                if (!"".equals(err)) {
                    return err;
                }
            }
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            case BaseHttpBO.CASE_Warehousing_Approve:
                if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }
                if (this.printCheckField(field.getFIELD_NAME_approverID(), FIELD_ERROR_approverID, sbError) && !FieldFormat.checkID(approverID)) {
                    return sbError.toString();
                }
                return "";
            default:
                if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_warehouseID(), FIELD_ERROR_warehouseID, sbError) && !FieldFormat.checkID(warehouseID)) {
                    return sbError.toString();
                }

                if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
                    return sbError.toString();
                }
                return "";
        }
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
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

    @Override
    public String toString() {
        return "Warehousing{" +
                "ID=" + ID +
                ", providerID=" + providerID +
                ", FIELD_NAME_providerID='" + field.FIELD_NAME_providerID + '\'' +
                ", status=" + status +
                ", FIELD_NAME_status='" + field.FIELD_NAME_status + '\'' +
                ", warehouseID=" + warehouseID +
                ", FIELD_NAME_warehouseID='" + field.FIELD_NAME_warehouseID + '\'' +
                ", staffID=" + staffID +
                ", FIELD_NAME_staffID='" + field.FIELD_NAME_staffID + '\'' +
                ", approverID=" + approverID +
                ", FIELD_NAME_approverID='" + field.FIELD_NAME_approverID + '\'' +
                ", createDatetime=" + createDatetime +
                ", FIELD_NAME_createDatetime='" + field.FIELD_NAME_createDatetime + '\'' +
                ", purchasingOrderID=" + purchasingOrderID +
                ", FIELD_NAME_purchasingOrderID='" + field.FIELD_NAME_purchasingOrderID + '\'' +
                ", sn='" + sn + '\'' +
                ", FIELD_NAME_sn='" + field.FIELD_NAME_sn + '\'' +
                ", isModified=" + isModified +
                ", FIELD_NAME_isModified='" + field.FIELD_NAME_isModified + '\'' +
                '}';
    }

    public void setApproverID(int approverID) {
        this.approverID = approverID;
    }

    public enum EnumStatusWarehousing {
        ESW_ToApprove("ToApprove", 0), //
        ESW_Approved("Approved", 1);

        private String name;
        private int index;

        private EnumStatusWarehousing(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static boolean inBoundForRetrieveN(int index) {
            if (index != BaseSQLiteBO.INVALID_STATUS && index != ESW_ToApprove.getIndex() && index != ESW_Approved.getIndex()) {
                return false;
            }

            return true;
        }

        public static boolean inBound(int index) {
            if (index < ESW_ToApprove.getIndex() || index > ESW_Approved.getIndex()) {
                return false;
            }
            return true;
        }

        public static String getName(int index) {
            for (EnumStatusWarehousing c : EnumStatusWarehousing.values()) {
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
