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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Pos extends BaseAuthenticationModel {
    public static final int MIN_Status = EnumStatusPos.ESP_Active.getIndex();
    public static final int MAX_Status = EnumStatusPos.ESP_Inactive.getIndex();
    public static final int MAX_LengthSalt = 32;
    public static final int MAX_LengthPassWordInPos = 16;
    public static final int MAX_LengthPosSN = 32;

    public static final String FIELD_ERROR_ShopID = "门店ID不能小于0";
    public static final String FIELD_ERROR_Status = "pos的状态只能在" + MIN_Status + "到" + MAX_Status + "之间";
    public static final String FIELD_ERROR_Salt = "Salt不能为空，长度不能超过" + MAX_LengthSalt + "个字符";
    public static final String FIELD_ERROR_PassWordInPos = "pos机的密码不能为空，长度不能超过" + MAX_LengthPassWordInPos + "个字符";
    public static final String FIELD_ERROR_PosSN = "posSN码不能为空，长度不能超过" + MAX_LengthPosSN + "个字符";
    public static final String FIELD_ERROR_ShopID_RN = "RN时，ShopID只能等于-1或大于0";//-1代表查询所有
    public static final String FIELD_ERROR_Status_RN = "RN时，status只能为-1, " + MIN_Status + " ," + MAX_Status;//-1代表查询所有
    public static final String FIELD_ERROR_RawPassword = "请输入6-16位的字符,首尾不能有空格";

    @Transient
    public static final String TAG = "pos";
    @Transient
    public static final String HTTP_Pos_Create = "posSync/createEx.bx";
    @Transient
    public static final String HTTP_Pos_RetrieveN = "posSync/retrieveNEx.bx";
    @Transient
    public static final String feedbackURL_FRONT = "posSync/feedbackEx.bx?sID=";
    @Transient
    public static final String feedbackURL_BEHINE = "&errorCode=EC_NoError";
    @Transient
    public static final String HTTP_Pos_RetrieveNC = "pos/retrieveNEx.bx?pageIndex=";
    @Transient
    public static final String HTTP_Pos_PageSize = "&pageSize=";
    @Transient
    public static final String HTTP_Pos_Retrieve1BySN = "pos/retrieve1BySNEx.bx?pos_SN=";
    @Transient
    public static final String HTTP_Pos_Delete = "posSync/deleteEx.bx?ID=";

    public static final PosField field = new PosField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_POS_SN")
    protected String pos_SN;

//    public String getFIELD_NAME_localPosSN() {
//        return "pos_SN";
//    }
//
//    @Transient
//    protected String FIELD_NAME_localPosSN;

    @NotNull
    @Property(nameInDb = "F_ShopID")
    protected long shopID;

//    public String getFIELD_NAME_shopID() {
//        return "shopID";
//    }
//
//    @Transient
//    protected String FIELD_NAME_shopID;

    @Property(nameInDb = "F_PwdEncrypted")
    protected String pwdEncrypted;

//    public String getFIELD_NAME_pwdEncrypted() {
//        return "pwdEncrypted";
//    }
//
//    @Transient
//    protected String FIELD_NAME_pwdEncrypted;

    @Property(nameInDb = "F_Salt")
    protected String salt;

//    public String getFIELD_NAME_salt() {
//        return "salt";
//    }
//
//    @Transient
//    protected String FIELD_NAME_salt;

    @Property(nameInDb = "F_PasswordInPOS")
    protected String passwordInPOS;

    public String getFIELD_NAEM_passwordInPos() {
        return "passwordInPos";
    }

    @Transient
    protected String FIELD_NAEM_passwordInPos;

    @Transient
    protected boolean isSelect = false;

//    public String getFIELD_NAME_isSelect() {
//        return "isSelect";
//    }
//
//    @Transient
//    protected String FIELD_NAME_isSelect;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

//    public String getFIELD_NAME_syncDatetime() {
//        return "syncDatetime";
//    }
//
//    @Transient
//    protected String FIELD_NAME_syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

//    @Transient
//    protected String FIELD_NAME_syncType;
//
//    public String getFIELD_NAME_syncType() {
//        return "syncType";
//    }

    @NotNull
    @Property(nameInDb = "F_Status")
    protected int status;

//    @Transient
//    protected String FIELD_NAME_returnSalt;
//
//    public String getFIELD_NAME_returnSalt() {
//        return "returnSalt";
//    }

//    @Transient
//    protected String FIELD_NAME_status;
//
//    public String getFIELD_NAME_status() {
//        return "status";
//    }

    @Transient
    protected int resetPasswordInPos;

    //  public String getFIELD_NAME_resetPasswordInPos() {
    //     return "resetPasswordInPos";
    //  }

    public int getResetPasswordInPos() {
        return resetPasswordInPos;
    }

    public void setResetPasswordInPos(int resetPasswordInPos) {
        this.resetPasswordInPos = resetPasswordInPos;
    }

    @Generated(hash = 1775949575)
    public Pos() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Generated(hash = 18128964)
    public Pos(Long ID, @NotNull String pos_SN, long shopID, String pwdEncrypted, String salt, String passwordInPOS, Date syncDatetime, String syncType, int status) {
        this.ID = ID;
        this.pos_SN = pos_SN;
        this.shopID = shopID;
        this.pwdEncrypted = pwdEncrypted;
        this.salt = salt;
        this.passwordInPOS = passwordInPOS;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
        this.status = status;
    }

    @Override
    public Long getID() {
        return this.ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public long getShopID() {
        return this.shopID;
    }

    public void setShopID(long shopID) {
        this.shopID = shopID;
    }

    public String getPwdEncrypted() {
        return this.pwdEncrypted;
    }

    public void setPwdEncrypted(String pwdEncrypted) {
        this.pwdEncrypted = pwdEncrypted;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswordInPOS() {
        return this.passwordInPOS;
    }

    public void setPasswordInPOS(String passwordInPOS) {
        this.passwordInPOS = passwordInPOS;
    }

    public int getReturnSalt() {
        return this.returnSalt;
    }

    public void setReturnSalt(int returnSalt) {
        this.returnSalt = returnSalt;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "ID=" + ID +
                ", MyPosSN='" + pos_SN + '\'' +
                ", shopID=" + shopID +
                ", pwdEncrypted='" + pwdEncrypted + '\'' +
                ", salt='" + salt + '\'' +
                ", passwordInPOS='" + passwordInPOS + '\'' +
                ", syncDatetime=" + syncDatetime +
                ", status=" + status +
                '}';
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Pos p = new Pos();
        p.setID(this.getID());
        p.setPos_SN(new String(this.getPos_SN()));
        p.setShopID(this.getShopID());
        p.setSalt(new String(this.getSalt()));
        p.setPasswordInPOS(new String(this.getPasswordInPOS()));
        p.setPwdEncrypted(new String(this.getPwdEncrypted()));
//        p.setSyncDatetime((Date) this.getSyncDatetime().clone());
        p.setStatus(this.getStatus());
        p.setReturnObject(this.getReturnObject());

        return p;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        Pos p = (Pos) arg0;
        if ((ignoreIDInComparision == true ? true : (this.getID().equals(p.getID()))) //
                && p.getShopID() == this.getShopID() && printComparator(field.getFIELD_NAME_shopID())
                && p.getPos_SN().equals(this.getPos_SN()) && printComparator(field.getFIELD_NAME_pos_SN()) //
                && ((p.getSalt() == null && this.getSalt() == null) || (p.getSalt().equals(this.getSalt()))) && printComparator(field.getFIELD_NAME_salt()) //
                && ((p.getPasswordInPOS() == null && this.getPasswordInPOS() == null) || (p.getPasswordInPOS().equals(this.getPasswordInPOS()))) && printComparator(getFIELD_NAEM_passwordInPos()) //
                && ((p.getPwdEncrypted() == null && this.getPwdEncrypted() == null) || (p.getPwdEncrypted().equals(this.getPwdEncrypted()))) && printComparator(field.getFIELD_NAME_pwdEncrypted()) //
                && p.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status())//
                && p.getSyncType().equals(this.getSyncType()) && printComparator(field.getFIELD_NAME_syncType())
                ) {
            return 0;
        }

        return -1;
    }

    public void setID(long ID) {
        this.ID = ID;
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
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行Pos.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            ID = Long.valueOf(object.getString(field.getFIELD_NAME_ID()));
            pos_SN = object.getString(field.getFIELD_NAME_pos_SN());
            shopID = Integer.valueOf(object.getString(field.getFIELD_NAME_shopID()));
            status = Integer.valueOf(object.getString(field.getFIELD_NAME_status()));
            //
            String tmp = object.getString(field.getFIELD_NAME_syncDatetime());
            if(!StringUtils.isEmpty(tmp)){
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }
            returnSalt = object.getInt(field.getFIELD_NAME_returnSalt());
            syncSequence = object.getInt(field.getFIELD_NAME_syncSequence());
            syncType = object.getString(field.getFIELD_NAME_syncType());

            return this;
        } catch (Exception e) {
            System.out.println("Pos.doParse1 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel doParse1C(JSONObject object) {
        System.out.println("正在执行Pos.doParse1C，object=" + (object == null ? null : object.toString()));

        try {
            ID = object.getLong(field.getFIELD_NAME_ID());
            pos_SN = object.getString(field.getFIELD_NAME_pos_SN());
            shopID = object.getInt(field.getFIELD_NAME_shopID());
            status = object.getInt(field.getFIELD_NAME_status());

            if (ID == 0) {
                return null;
            }
            return this;
        } catch (Exception e) {
            System.out.println("Pos.doParse1C2 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public BaseModel doParse1C2(JSONObject object) {
        System.out.println("正在执行Pos.doParse1C2，object=" + (object == null ? null : object.toString()));

        try {
            ID = Long.valueOf(object.getString(field.getFIELD_NAME_ID()));
            pos_SN = object.getString(field.getFIELD_NAME_pos_SN());
            shopID = Integer.valueOf(object.getString(field.getFIELD_NAME_shopID()));
            status = Integer.valueOf(object.getString(field.getFIELD_NAME_status()));
            passwordInPOS = object.getString(field.getFIELD_NAME_passwordInPOS());

            if (ID == 0) {
                return null;
            }
            return this;
        } catch (Exception e) {
            System.out.println("Pos.doParse1C2出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1C(String s) {
        System.out.println("正在执行Pos.parse1C，s=" + s);

        Pos pos = new Pos();
        try {
            JSONObject joPos = new JSONObject(s);
            pos = (Pos) doParse1C(joPos);
            if (pos == null) {
                return null;
            }
            return pos;
        } catch (JSONException e) {
            System.out.println("Pos.parce1C2出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public BaseModel parse1C2(String s) {
        System.out.println("正在执行Pos.parse1C2，s=" + s);

        Pos pos = new Pos();
        try {
            JSONObject joPos = new JSONObject(s);
            pos = (Pos) doParse1C2(joPos);
            if (pos == null) {
                return null;
            }
            return pos;
        } catch (JSONException e) {
            System.out.println("Pos.parce1C2出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Pos.parse1，s=" + s);

        Pos pos = new Pos();
        try {
            JSONObject joPos = new JSONObject(s);
            pos = (Pos) doParse1(joPos);
            if (pos == null) {
                return null;
            }
            return pos;
        } catch (JSONException e) {
            System.out.println("Pos.parse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行Pos.parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> posList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                Pos pos = new Pos();
                if (pos.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                posList.add(pos);
            }
            return posList;
        } catch (Exception e) {
            System.out.println("Pos.parseN出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }

    public String getPos_SN() {
        return this.pos_SN;
    }

    public void setPos_SN(String pos_SN) {
        this.pos_SN = pos_SN;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public enum EnumStatusPos {
        ESP_Active("Active", 0), //
        ESP_Inactive("Inactive", 1);

        private String name;
        private int index;

        private EnumStatusPos(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusPos c : EnumStatusPos.values()) {
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

        if (this.printCheckField(field.getFIELD_NAME_pos_SN(), FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(pos_SN) && pos_SN.length() <= MAX_LengthPosSN) {
        } else {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_ShopID, sbError) && !FieldFormat.checkID(shopID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_Salt, sbError) && !StringUtils.isEmpty(salt) && salt.length() <= MAX_LengthSalt) {
        } else {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_Status, sbError) && MIN_Status <= status && status <= MAX_Status) {
        } else {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_passwordInPOS(), FIELD_ERROR_PassWordInPos, sbError) && !StringUtils.isEmpty(passwordInPOS) && passwordInPOS.length() <= MAX_LengthPassWordInPos) {
        } else {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_ShopID, sbError) && !FieldFormat.checkID(shopID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (ID != null) {// 说明：在LoginActivity.posHttpBO.retrieve1AsyncC中的对象时一个空对象，ID和POS_SN（在发给服务器时并不通过Pos端的check方法）均为null。故当ID为null时不检查
            if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                return sbError.toString();
            }
        }

        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (StringUtils.isNotEmpty(pos_SN)) {
            if (this.printCheckField(field.getFIELD_NAME_pos_SN(), FIELD_ERROR_PosSN, sbError) && pos_SN.length() > MAX_LengthPosSN) {
                return sbError.toString();
            }
        }

        if (this.printCheckField(field.getFIELD_NAME_shopID(), String.format(FIELD_ERROR_ShopID_RN, field.getFIELD_NAME_shopID()), sbError) && !(shopID == BaseSQLiteBO.INVALID_INT_ID || shopID > 0)) {//RN时shopID=-1代表查询所有
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_status(), String.format(FIELD_ERROR_Status_RN, field.getFIELD_NAME_status()), sbError) && !(status >= BaseSQLiteBO.INVALID_STATUS && status <= MAX_Status)) {//RN时status=-1代表查询所有
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
}
