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


////@Entity(nameInDb = "purchasingOrder")
public class PurchasingOrder extends BaseModel {
    public static int MIN_Status = EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex();
    public static int MAX_Status = EnumStatusPurchasingOrder.ESPO_Deleted.getIndex();
    public static int MAX_LengthRemark = 128;
    public static int MAX_LengthQueryKeyword = 32;

    public static final String FIELD_ERROR_QueryKeyword = "搜索关键字不能为空并且要小于 " + MAX_LengthQueryKeyword + "个字符";
    public static final String FIELD_ERROR_StaffID = "StaffID必须>0";
    public static final String FIELD_ERROR_ApproveID = "ApproveID必须>0";
    public static final String FIELD_ERROR_ProviderID = "ProviderID必须>0";
    public static final String FIELD_ERROR_Remark = "备注的长度不能超过" + MAX_LengthRemark + "个字符";
    public static final String FIELD_ERROR_Status = "采购订单的状态只能在" + MIN_Status + "到" + MAX_Status + "之间";
    public static final String FIELD_ERROR_Date = "传入的日期时间不正确 ";

    public static final String HTTP_PURCHASINGORDER_CREATE = "purchasingOrder/createEx.bx";
    public static final String HTTP_PURCHASINGORDER_APPROVE = "purchasingOrder/approveEx.bx";
    public static final String HTTP_PURCHASINGORDER_RETRIEVE1 = "purchasingOrder/retrieve1Ex.bx?ID=";
    public static final String KEY_COMMIDS = "commIDs";
    public static final String KEY_BARCODEIDS = "barcodeIDs";
    public static final String KEY_NOS = "NOs";
    public static final String KEY_PRICESUGGESTIONS = "priceSuggestions";
    public static final PurchasingOrderField field = new PurchasingOrderField();

    /**
     * 一个采购订单能入库的最大次数
     */
    public static final int MAX_WarehousingNO = 3;
    /**
     * 记录本采购订单已经入库多少次。用于控制入库频率：一张采购订单不能太多次入库
     */
    protected int warehousingNO;

    //@Id
    ////@Property(nameInDb = "F_ID")
    protected Integer ID;
    //@NotNull
    //@Property(nameInDb = "F_Status")
    protected int status;

    //@NotNull
    //@Property(nameInDb = "F_CreateDatetime")
    private Date createDatetime;

    //@Property(nameInDb = "F_ApproveDatetime")
    private Date approveDatetime;

    //@Property(nameInDb = "F_EndDatetime")
    private Date endDatetime;

    //@NotNull
    //@Property(nameInDb = "F_ProviderID")
    protected int providerID;

    //@NotNull
    //@Property(nameInDb = "F_Remark")
    protected String remark;

    //@NotNull
    //@Property(nameInDb = "F_StaffID")
    protected int staffID;

    //@NotNull
    //@Property(nameInDb = "F_ProviderName")
    protected String providerName;

    //@Property(nameInDb = "F_ApproverID")
    protected int approverID;

    //@NotNull
    //@Property(nameInDb = "F_UpdateDatetime")
    protected Date updateDatetime;

    //@NotNull
    //@Property(nameInDb = "F_SN")
    protected String sn;

    //@Transient
    protected String totalPrices; // 在机器人中使用到，

    //@Generated(hash = 1412997819)
//    public PurchasingOrder(int warehousingNO, Long ID, int status, //@NotNull Date createDatetime, Date approveDatetime, Date endDatetime, int providerID, //@NotNull String remark, int staffID,
//            //@NotNull String providerName, int approverID, //@NotNull Date updateDatetime, //@NotNull String sn) {
//        this.warehousingNO = warehousingNO;
//        this.ID = ID;
//        this.status = status;
//        this.createDatetime = createDatetime;
//        this.approveDatetime = approveDatetime;
//        this.endDatetime = endDatetime;
//        this.providerID = providerID;
//        this.remark = remark;
//        this.staffID = staffID;
//        this.providerName = providerName;
//        this.approverID = approverID;
//        this.updateDatetime = updateDatetime;
//        this.sn = sn;
//    }

    //@Generated(hash = 755540390)
//    public PurchasingOrder() {
//    }

    public String getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(String totalPrices) {
        this.totalPrices = totalPrices;
    }

   

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public static String getHttpPurchasingorderCreate() {
        return HTTP_PURCHASINGORDER_CREATE;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getApproverID() {
        return approverID;
    }

    public void setApproverID(int approverID) {
        this.approverID = approverID;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
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

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(Date approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    public static int getMAX_WarehousingNO() {
        return MAX_WarehousingNO;
    }

    public int getWarehousingNO() {
        return warehousingNO;
    }

    public void setWarehousingNO(int warehousingNO) {
        this.warehousingNO = warehousingNO;
    }

    @Override
    public String toString() {
        return "PurchasingOrder{" +
                "warehousingNO=" + warehousingNO +
                ", ID=" + ID +
                ", status=" + status +
                ", createDatetime=" + createDatetime +
                ", approveDatetime=" + approveDatetime +
                ", endDatetime=" + endDatetime +
                ", providerID=" + providerID +
                ", remark='" + remark + '\'' +
                ", staffID=" + staffID +
                ", providerName='" + providerName + '\'' +
                ", approverID=" + approverID +
                ", updateDatetime=" + updateDatetime +
                ", sn='" + sn + '\'' +
                '}';
    }

    public BaseModel clone() {
        PurchasingOrder obj = new PurchasingOrder();
        obj.setID(this.getID());
        obj.setStatus(this.getStatus());
        obj.setCreateDatetime((Date) this.getCreateDatetime().clone());
        obj.setApproveDatetime((Date) this.getApproveDatetime().clone());
        obj.setEndDatetime((Date) this.getEndDatetime().clone());
        obj.setProviderID(this.getProviderID());
        obj.setApproverID(this.getApproverID());
        obj.setProviderName(this.getProviderName());
        obj.setRemark(this.getRemark());
        obj.setStaffID(this.getStaffID());
        obj.setSn(sn);

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        PurchasingOrder po = (PurchasingOrder) arg0;

        if ((ignoreIDInComparision == true ? true : po.getID().equals(ID) && printComparator(field.getFIELD_NAME_ID())) //
                && po.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
                && po.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID())//
                && po.getProviderID() == providerID && printComparator(field.getFIELD_NAME_providerID())//
                && po.getRemark().equals(remark) && printComparator(field.getFIELD_NAME_remark())//
                ) {
            if (!ignoreSlaveListInComparision) {
                if (listSlave1 == null && po.getListSlave1() == null) {
                    return 0;
                }
                if (listSlave1 == null && po.getListSlave1() != null) {
                    if (po.getListSlave1().size() == 0) {
                        return 0;
                    }
                    return -1;
                }
                if (listSlave1 != null && po.getListSlave1() == null) {
                    if (listSlave1.size() == 0) {
                        return 0;
                    }
                    return -1;
                }
                if (listSlave1 != null && po.getListSlave1() != null) {
                    if (listSlave1.size() != po.getListSlave1().size()) {
                        return -1;
                    }
                    for (int i = 0; i < listSlave1.size(); i++) {
                        PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) listSlave1.get(i);
                        purchasingOrderCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
                        Boolean exist = false;
                        for (int j = 0; j < po.getListSlave1().size(); j++) {
                            PurchasingOrderCommodity poc = (PurchasingOrderCommodity) po.getListSlave1().get(j);
                            if (purchasingOrderCommodity.getCommodityID() == poc.getCommodityID()) {
                                exist = true;
                                if (purchasingOrderCommodity.compareTo(poc) != 0) {
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
    public BaseModel parse1(String s) {
        System.out.println("正在执行 PurchasingOrder.parse1，s=" + s);

        try {
            JSONObject jo = JSONObject.parseObject(s);
            PurchasingOrder po = (PurchasingOrder) doParse1(jo);
            if (po == null) {
                return null;
            }
            JSONArray array = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
            if (array != null) {
                PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
                List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) poc.parseN(array);
                if (list == null) {
                    return null;
                }
                po.setListSlave1(list);  //非常关键！！
            }
            return po;
        } catch (JSONException e) {
            System.out.println("执行 PurchasingOrder.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 PurchasingOrder.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            sn = jo.getString(field.getFIELD_NAME_sn());
            status = Integer.valueOf(jo.getString(field.getFIELD_NAME_status()));
            providerID = Integer.valueOf(jo.getString(field.getFIELD_NAME_providerID()));
            remark = jo.getString(field.getFIELD_NAME_remark());
            staffID = Integer.valueOf(jo.getString(field.getFIELD_NAME_staffID()));
            providerName = jo.getString(field.getFIELD_NAME_providerName());
            approverID = Integer.valueOf(jo.getString(field.getFIELD_NAME_approverID()) == null ? "0" : jo.getString(field.getFIELD_NAME_approverID()));

            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if(!StringUtils.isEmpty(tmpCreateDatetime)){
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }

            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
            if(!StringUtils.isEmpty(tmpUpdateDatetime)){
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }

            String tmpApproveDatetime = jo.getString(field.getFIELD_NAME_approveDatetime());
            if (!StringUtils.isEmpty(tmpApproveDatetime)) {
                approveDatetime = Constants.getSimpleDateFormat2().parse(tmpApproveDatetime);
                if(approveDatetime == null){
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_approveDatetime() + "=" + tmpApproveDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_approveDatetime() + "=" + tmpApproveDatetime);
                }
            }

            String tmpEndDatetime = jo.getString(field.getFIELD_NAME_endDatetime());
            if (!StringUtils.isEmpty(tmpEndDatetime)) {
                endDatetime = Constants.getSimpleDateFormat2().parse(tmpEndDatetime);
                if(endDatetime == null){
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_endDatetime() + "=" + tmpEndDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_endDatetime() + "=" + tmpEndDatetime);
                }
            }

            return this;
        } catch (Exception e) {
            System.out.println("执行 PurchasingOrder.doParse1 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 PurchasingOrder.parseN，s=" + s);

        List<BaseModel> poList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray = JSONArray.parseArray(s);
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                PurchasingOrder po = new PurchasingOrder();
                po.doParse1(jsonObject1);
                poList.add(po);
            }
        } catch (Exception e) {
            System.out.println("执行 PurchasingOrder.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return poList;
    }


    public enum EnumStatusPurchasingOrder {
        ESPO_ToApprove("Not approved", 0), //未审核
        ESPO_Approved("Approved", 1), //已审核
        ESPO_PartWarehousing("PartWarehousing", 2), //部分入库
        ESPO_AllWarehousing("AllWarehousing", 3), //全部入库
        ESPO_Deleted("Deleted", 4);//已删除

        private String name;
        private int index;

        public static boolean inBound(int index) {
            if (index < ESPO_ToApprove.getIndex() || index > ESPO_Deleted.getIndex()) {
                return false;
            }
            return true;
        }

        private EnumStatusPurchasingOrder(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusPurchasingOrder c : EnumStatusPurchasingOrder.values()) {
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

        if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) //
        {
            return sbError.toString();
        }

        if (!StringUtils.isEmpty(remark)) {
            if (this.printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_Remark, sbError) && remark.length() > MAX_LengthRemark) //
            {
                return sbError.toString();
            }
        }

        if (listSlave1 != null){
            List<PurchasingOrderCommodity> pcList = (List<PurchasingOrderCommodity>) listSlave1;
            for (PurchasingOrderCommodity pc : pcList){
                System.out.println("￥￥￥准备检查从表字段");
                String err = pc.checkCreate(iUseCaseID);
                if (!"".equals(err)){
                    return err;
                }
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

        switch (iUseCaseID) {
            case BaseHttpBO.CASE_PurchasingOrder_Approve:
                if (this.printCheckField(field.getFIELD_NAME_approverID(), FIELD_ERROR_ApproveID, sbError) && !FieldFormat.checkID(approverID)) //
                {
                    return sbError.toString();
                }
                break;
            default:
                if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) //
                {
                    return sbError.toString();
                }

                if (!StringUtils.isEmpty(remark)) {
                    if (this.printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_Remark, sbError) && remark.length() > MAX_LengthRemark) //
                    {
                        return sbError.toString();
                    }
                }
        }
        return "";
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

        if (status != BaseSQLiteBO.INVALID_STATUS) {
            if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_Status, sbError) && !EnumStatusPurchasingOrder.inBound(status)) //
            {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(queryKeyword)) {
            if (queryKeyword.length() > MAX_LengthQueryKeyword) {
                return FIELD_ERROR_QueryKeyword;
            }
        }

        if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseSQLiteBO.INVALID_ID) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }
}
