package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.StringUtils;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponCode extends BaseModel {
    private static final long serialVersionUID = 1L;

    public static final String HTTP_CouponCode_RetrieveNC = "couponCode/retrieveNEx.bx";

    public static final CouponCodeField field = new CouponCodeField();

    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    protected int vipID;

    protected int couponID;

    protected int status;

    protected String SN;

    protected Date createDatetime;

    protected Date usedDatetime;

    protected int subStatus;

    public int getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(int subStatus) {
        this.subStatus = subStatus;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public Long getID() {
        return ID;
    }

    public int getVipID() {
        return vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String sN) {
        SN = sN;
    }

    public Date getUsedDatetime() {
        return usedDatetime;
    }

    public void setUsedDatetime(Date usedDatetime) {
        this.usedDatetime = usedDatetime;
    }

    @Override
    public String toString() {
        return "CouponCode{" +
                "ID=" + ID +
                ", vipID=" + vipID +
                ", couponID=" + couponID +
                ", status=" + status +
                ", SN='" + SN + '\'' +
                ", createDatetime=" + createDatetime +
                ", usedDatetime=" + usedDatetime +
                ", subStatus=" + subStatus +
                '}';
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        CouponCode couponCode = new CouponCode();
        couponCode.setID(ID);
        couponCode.setVipID(vipID);
        couponCode.setCouponID(couponID);
        couponCode.setStatus(status);
        couponCode.setSN(SN);
        couponCode.setCreateDatetime((createDatetime == null ? null : (Date) createDatetime.clone()));
        couponCode.setUsedDatetime((usedDatetime == null ? null : (Date) usedDatetime.clone()));
        couponCode.setSubStatus(subStatus);

        return couponCode;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        CouponCode couponCode = (CouponCode) arg0;
        if ((ignoreIDInComparision == true ? true : couponCode.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
                && couponCode.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID()) //
                && couponCode.getCouponID() == couponID && printComparator(field.getFIELD_NAME_couponID()) //
                && couponCode.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
                ) {
            return 0;
        }

        return -1;
    }

    @Override
    public BaseModel doParse1(JSONObject jo) {
        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            vipID = jo.getInt(field.getFIELD_NAME_vipID());
            couponID = jo.getInt(field.getFIELD_NAME_couponID());
            status = jo.getInt(field.getFIELD_NAME_status());
            SN = jo.getString(field.getFIELD_NAME_SN());
            subStatus = jo.getInt(field.getFIELD_NAME_subStatus());
            //
            String tmp = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (createDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
                }
            }
            //
            tmp = jo.getString(field.getFIELD_NAME_usedDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                usedDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (usedDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_usedDatetime() + "=" + tmp);
                }
            }

            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        List<BaseModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CouponCode c = new CouponCode();
                if (c.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                list.add(c);
            }
            return list;
        } catch (Exception e) {
            System.out.println("CouponCode.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        List<BaseModel> couponCodeList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                CouponCode couponCode = new CouponCode();
                couponCode.doParse1(jsonObject1);
                couponCodeList.add(couponCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return couponCodeList;
    }

    @Override
    public String checkRetrieveN(int iUseCaseID) {
        return "";
    }

    public enum EnumCouponCodeStatus {
        ECCS_Normal("Normal", 0), //
        ECCS_Consumed("Consumed", 1);

        private String name;
        private int index;

        private EnumCouponCodeStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCouponCodeStatus c : EnumCouponCodeStatus.values()) {
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
