package com.bx.erp.model;


import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Shop extends BaseModel {
    private static final int Zero = 0;
    private static final int MAX_LENGTH_Name = 20;
    private static final int MAX_LENGTH_Address = 30;
    private static final int MAX_LENGTH_Key = 32;
    private static final int MAX_LENGTH_Remark = 128;
    public static final String FIELD_ERROR_longitude = "经度不能为" + Zero;
    public static final String FIELD_ERROR_latitude = "纬度不能为" + Zero;
    public static final String FIELD_ERROR_remark = "备注的长度为(" + Zero + ", " + MAX_LENGTH_Remark + "]";
    public static final String FIELD_ERROR_key = "钥匙的长度为(" + Zero + ", " + MAX_LENGTH_Key + "]";
    public static final String FIELD_ERROR_name = "门店名称长度为(" + Zero + ", " + MAX_LENGTH_Name + "]";
    public static final String FIELD_ERROR_address = "门店地址长度为(" + Zero + ", " + MAX_LENGTH_Address + "]";
    public static final String FIELD_ERROR_checkUniqueField = "非法的值";
    public static final String FIELD_ERROR_districtID = "区域ID必须大于" + Zero;
    public static final String FIELD_ERROR_companyID = "公司ID必须大于" + Zero;
    public static final String FIELD_ERROR_bxStaffID = "业务经理ID必须大于" + Zero;
    public static final String FIELD_ERROR_status = "门店的状态码只能是" + EnumStatusShop.ESS_Online.index + "、" + EnumStatusShop.ESS_Offline.index + "、" //
            + EnumStatusShop.ESS_DisconnectionLocking.index + "、" + EnumStatusShop.ESS_ArrearageLocking.index + "、" + EnumStatusShop.ESS_ToGetFinancialApproval.index + "、";


    public static final String HTTP_Shop_Create = "shop/createEx.bx";
    public static final String HTTP_Shop_Delete = "shop/deleteEx.bx";
    public static final String SHOPID = "shopID";
    public static final ShopField field = new ShopField();

    protected long ID;
    protected String name;

    protected int bxStaffID;

    protected String remark;

    protected int districtID;


    protected long companyID;

    protected String address;

    protected int status;

    protected double longitude;

    protected double latitude;

    protected String key;

    public long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(long companyID) {
        this.companyID = companyID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBxStaffID() {
        return bxStaffID;
    }

    public void setBxStaffID(int bxStaffID) {
        this.bxStaffID = bxStaffID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    @Generated(hash = 924622302)
    public Shop(long ID, String name, int bxStaffID, String remark, int districtID, long companyID, String address, int status, double longitude, double latitude,
            String key) {
        this.ID = ID;
        this.name = name;
        this.bxStaffID = bxStaffID;
        this.remark = remark;
        this.districtID = districtID;
        this.companyID = companyID;
        this.address = address;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.key = key;
    }

    @Generated(hash = 633476670)
    public Shop() {
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", bxStaffID=" + bxStaffID +
                ", remark='" + remark + '\'' +
                ", districtID=" + districtID +
                ", companyID=" + companyID +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", key='" + key + '\'' +
                '}';
    }

    @Override
    public BaseModel clone() {
        Shop obj = new Shop();
        obj.setID(getID());
        obj.setName(new String(getName()));
        obj.setBxStaffID(getBxStaffID());
        obj.setRemark(getRemark());
        obj.setDistrictID(getDistrictID());
        obj.setCompanyID(getCompanyID());
        obj.setAddress(new String(getAddress()));
        obj.setStatus(getStatus());
        obj.setLongitude(getLongitude());
        obj.setLatitude(getLatitude());
        obj.setKey(new String(getKey()));

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        Shop s = (Shop) arg0;
        if ((ignoreIDInComparision == true ? true : (s.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))) //
                && s.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())//
                && s.getBxStaffID() == this.getBxStaffID() && printComparator(field.getFIELD_NAME_name())//
                && s.getRemark().equals(this.getRemark()) && printComparator(field.getFIELD_NAME_name())//
                && s.getDistrictID() == this.getDistrictID() && printComparator(field.getFIELD_NAME_name())//
                && s.getCompanyID() == this.getCompanyID() && printComparator(field.getFIELD_NAME_companyID())//
                && s.getAddress().equals(this.getAddress()) && printComparator(field.getFIELD_NAME_address())//
                && s.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status())//
                && Math.abs(GeneralUtil.sub(s.getLatitude(), this.getLatitude())) < TOLERANCE && printComparator(field.getFIELD_NAME_latitude()) //
                && Math.abs(GeneralUtil.sub(s.getLongitude(), this.getLongitude())) < TOLERANCE && printComparator(field.getFIELD_NAME_longitude()) //
                && s.getKey().equals(this.getKey()) && printComparator(field.getFIELD_NAME_key())) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行 Shop.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            name = object.getString(field.getFIELD_NAME_name());
            ID = object.getInt(field.getFIELD_NAME_ID());
            bxStaffID = object.getInt(field.getFIELD_NAME_bxStaffID());
            remark = object.getString(field.getFIELD_NAME_remark());
            districtID = object.getInt(field.getFIELD_NAME_districtID());
            companyID = object.getLong(field.getFIELD_NAME_companyID());
            address = object.getString(field.getFIELD_NAME_address());
            status = object.getInt(field.getFIELD_NAME_status());
            latitude = object.getInt(field.getFIELD_NAME_latitude());
            longitude = object.getInt(field.getFIELD_NAME_longitude());
            key = object.getString(field.getFIELD_NAME_key());

            return this;
        } catch (JSONException e) {
            System.out.println("Shop.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 Shop.parse1，s=" + s);

        Shop shop = new Shop();
        try {
            JSONObject joPos = new JSONObject(s);
            shop = (Shop) doParse1(joPos);
            if (shop == null) {
                return null;
            }
            return shop;
        } catch (JSONException e) {
            System.out.println("Shop.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkShopName(name)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && !FieldFormat.checkShopAddress(address)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && StringUtils.isEmpty(key) || key.length() > MAX_LENGTH_Key) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && !StringUtils.isEmpty(remark) && remark.length() > MAX_LENGTH_Remark) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_longitude(), FIELD_ERROR_longitude, sbError) && longitude < Zero) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_latitude(), FIELD_ERROR_latitude, sbError) && latitude < Zero) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusShop.ESS_Online.index //
                || status == EnumStatusShop.ESS_Offline.index || status == EnumStatusShop.ESS_DisconnectionLocking.index //
                || status == EnumStatusShop.ESS_ArrearageLocking.index || status == EnumStatusShop.ESS_ToGetFinancialApproval.index)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && !FieldFormat.checkID(companyID)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && !FieldFormat.checkID(districtID)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_bxStaffID(), FIELD_ERROR_bxStaffID, sbError) && !FieldFormat.checkID(bxStaffID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if(printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkShopName(name)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_address(), FIELD_ERROR_address, sbError) && !FieldFormat.checkShopAddress(address)) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && key.length() > MAX_LENGTH_Key) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && !StringUtils.isEmpty(remark) && remark.length() > MAX_LENGTH_Remark) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_longitude(), FIELD_ERROR_longitude, sbError) && longitude < Zero) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_latitude(), FIELD_ERROR_latitude, sbError) && latitude < Zero) {
            return sbError.toString();
        }

        if(printCheckField(field.getFIELD_NAME_districtID(), FIELD_ERROR_districtID, sbError) && !FieldFormat.checkID(districtID)) {
            return sbError.toString();
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
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if(printCheckField(field.getFIELD_NAME_companyID(), FIELD_ERROR_companyID, sbError) && !FieldFormat.checkID(companyID)) {
            return sbError.toString();
        }

        return "";
    }

    public enum EnumStatusShop {
        ESS_Online("Online", 0), // 营业中
        ESS_Offline("Offline", 1), // 离线中
        ESS_DisconnectionLocking("DisconnectionLocking", 2), // 断网锁定
        ESS_ArrearageLocking("ArrearageLocking", 3), // 欠费锁定
        ESS_ToGetFinancialApproval("ToGetFinancialApproval", 4); // 财务待审核

        private String name;
        private int index;

        private EnumStatusShop(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusShop c : EnumStatusShop.values()) {
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
