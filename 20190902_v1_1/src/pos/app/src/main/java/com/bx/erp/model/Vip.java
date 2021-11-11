package com.bx.erp.model;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
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
public class Vip extends BaseModel {
    public static final int MAX_LENGTH_String1 = 30;
    public static final int MAX_LENGTH_District = 30;
    public static final int MIN_LENGTH_Mobile = 6;
    public static final int LENGTH_ICID = 18;
    public static final int MAX_LENGTH_Wechat = 20;
    public static final int MIN_LENGTH_Wechat = 5;
    public static final int MAX_LENGTH_QQ = 10;
    public static final int MIN_LENGTH_QQ = 5;
    public static final int CASE_CheckPhone = 1; // 检查手机号
    public static final int CASE_CheckICID = 2; // 检查ICID
    public static final int CASE_CheckWechat = 3; // 检查微信
    public static final int CASE_CheckQQ = 4; // 检查QQ
    public static final int CASE_CheckEmail = 5; // 检查邮箱
    public static final int CASE_CheckAccount = 6; // 检查登录账号
    public static final int Max_LENGTH_VipCardSN = 16;
    public static final String FIELD_ERROR_RNByFields_mobile = "搜索的手机号长度为[" + MIN_LENGTH_Mobile + ", " + FieldFormat.LENGTH_Mobile + "]";
    public static final String FIELD_ERROR_mobile = "会员手机号格式错误";
    public static final String FIELD_ERROR_CheckMobile = "检查的手机号长度为" + FieldFormat.LENGTH_Mobile + "位";
    public static final String FIELD_ERROR_icid = "VIP的身份证号码的格式有误";
    public static final String FIELD_ERROR_password = "VIP密码的长度为[" + FieldFormat.MIN_LENGTH_Password + ", " + FieldFormat.MAX_LENGTH_Password + "]";
    public static final String FIELD_ERROR_name = "VIP名字的长度必须在1和" + FieldFormat.MAX_LENGTH_HumanName + "之间";
    public static final String FIELD_ERROR_ICID = "身份证的长度必须是" + LENGTH_ICID + "位";
    public static final String FIELD_ERROR_Wechat = "微信号的长度为[" + MIN_LENGTH_Wechat + ", " + MAX_LENGTH_Wechat + "]";
    public static final String FIELD_ERROR_QQ = "QQ号的长度为[" + MIN_LENGTH_QQ + ", " + MAX_LENGTH_QQ + "]的正整数";
    public static final String FIELD_ERROR_Email = "邮箱的格式不对并且邮箱最大长度为" + MAX_LENGTH_String1;
    public static final String FIELD_ERROR_Account = "账号最大长度为" + MAX_LENGTH_String1;
    public static final String FIELD_ERROR_int1 = "int1只能是" + CASE_CheckPhone + "或" + CASE_CheckICID + "或" + CASE_CheckWechat + "或" + CASE_CheckQQ + "或" + CASE_CheckEmail + "或" + CASE_CheckAccount;
    public static final String FIELD_ERROR_IDInPOS = "IDInPOS必须大于0";
    public static final String FEILD_ERROR_category = "会员分类ID必须大于0";
    public static final String FIELD_ERROR_status = "会员的状态只能是" + EnumStatusVip.ESV_Normal.index + "或" + EnumStatusVip.ESV_LossDeclaration.index + "或" + EnumStatusVip.ESV_Deleted.index;
    public static final String FIELD_ERROR_district = "会员区域的长度为(0, " + MAX_LENGTH_District + "]";
    public static final String FIELD_ERROR_consumeTimes = "创建会员时总消费次数必须等于0";
    public static final String FIELD_ERROR_consumeAmount = "创建会员时总消费金额必须等于0";
    public static final String FIELD_ERROR_DateTime = "时间格式必须为:" + Constants.DATE_FORMAT_Default + "或者为:" + Constants.DATE_FORMAT_Default6;
    public static final String FIELD_ERROR_bonus = "会员积分只能大于或等于0";
    public static final String FIELD_ERROR_cardID = "会员卡ID必须大于0";
    public static final String FIELD_ERROR_vipCardSN = "会员卡号必须是" + Max_LENGTH_VipCardSN + "位";
    public static final String FIELD_ERROR_RetrieveNByMobileOrVipCardSN = "会员手机号或会员卡号至少要提供一个";

    public static final VipField field = new VipField();

    @Transient
    public static final String HTTP_Vip_CREEATE = "vipSync/createEx.bx";
    @Transient
    public static final String HTTP_Vip_UPDATE = "vipSync/updateEx.bx";
    @Transient
    public static final String HTTP_Vip_RETRIEVEN = "vipSync/retrieveNEx.bx";
    @Transient
    public static final String HTTP_Vip_RETRIEVENC = "vip/retrieveNEx.bx";
    @Transient
    public static final String HTTP_Vip_RetrieveNByMobileOrVipCardSNEx = "vip/retrieveNByMobileOrVipCardSNEx.bx";
    @Transient
    public static final String HTTP_Vip_FEEDBACK_URL_FRONT = "vipSync/feedbackEx.bx?sID=";
    @Transient
    public static final String HTTP_Vip_FEEDBACK_URL_BEHINE = "&errorCode= EC_NoError";
    @Transient
    public static final String HTTP_Vip_DELETE = "vipSync/deleteEx.bx?ID=";
    @Transient
    public static final String HTTP_Vip_retrieveNVipConsumeHistoryEx = "vip/retrieveNVipConsumeHistoryEx.bx?ID=";
    @Transient
    public static final String HTTP_Vip_retrieveNVipConsumeHistoryEx_bQuerySmallerThanStartID = "&bQuerySmallerThanStartID=";
    @Transient
    public static final String HTTP_Vip_retrieveNVipConsumeHistoryEx_startRetailTreadeIDInSQLite = "&startRetailTreadeIDInSQLite=";

    @Transient
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Transient
    protected int number;//使用@Transient注解的属性不会被存入数据库作为字段

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_ICID")
    protected String iCID;

    protected int startRetailTreadeIDInSQLite;

    @NotNull
    @Property(nameInDb = "F_Mobile")
    protected String mobile;

    @NotNull
    @Property(nameInDb = "F_Name")
    protected String name;

    @NotNull
    @Property(nameInDb = "F_Email")
    protected String email;

    @NotNull
    @Property(nameInDb = "F_ConsumeTimes")
    protected int consumeTimes;//总消费次数

    @NotNull
    @Property(nameInDb = "F_ConsumeAmount")
    protected double consumeAmount;//总消费金额

    @NotNull
    @Property(nameInDb = "F_District")
    protected String district;

    @NotNull
    @Property(nameInDb = "F_Category")
    protected int category;

    @NotNull
    @Property(nameInDb = "F_Birthday")
    protected Date birthday;

    @NotNull
    @Property(nameInDb = "F_Bonus")
    protected int bonus;//当前积分

    @NotNull
    @Property(nameInDb = "F_LastConsumeDatetime")
    protected Date lastConsumeDatetime;//上次消费日期时间

    @NotNull
    @Property(nameInDb = "F_LocalPosSN")
    protected String localPosSN;//POS的

    @Transient
    List<Vip> vipList;
    @Transient
    public boolean isSelect = false;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @Property(nameInDb = "F_CardID")
    protected int cardID;

    @Transient
    protected String cardCode;

    @Transient
    private String vipCardSN;

    @Transient
    protected int bQuerySmallerThanStartID; // 非数据库字段，如果为0,查询大于起始值的零售单ID,如果为1,则查询小于起始值的零售单ID

    @Generated(hash = 213402806)
    public Vip(Long ID, @NotNull String iCID, int startRetailTreadeIDInSQLite, @NotNull String mobile, @NotNull String name, @NotNull String email, int consumeTimes, double consumeAmount, @NotNull String district, int category, @NotNull Date birthday, int bonus,
            @NotNull Date lastConsumeDatetime, @NotNull String localPosSN, Date syncDatetime, String syncType, int cardID) {
        this.ID = ID;
        this.iCID = iCID;
        this.startRetailTreadeIDInSQLite = startRetailTreadeIDInSQLite;
        this.mobile = mobile;
        this.name = name;
        this.email = email;
        this.consumeTimes = consumeTimes;
        this.consumeAmount = consumeAmount;
        this.district = district;
        this.category = category;
        this.birthday = birthday;
        this.bonus = bonus;
        this.lastConsumeDatetime = lastConsumeDatetime;
        this.localPosSN = localPosSN;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
        this.cardID = cardID;
    }

    @Generated(hash = 794774875)
    public Vip() {
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVipCardSN() {
        return vipCardSN;
    }

    public void setVipCardSN(String vipCardSN) {
        this.vipCardSN = vipCardSN;
    }

    @Override
    public Long getID() {
        return this.ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getICID() {
        return this.iCID;
    }

    public void setiCID(String iCID) {
        this.iCID = iCID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getConsumeTimes() {
        return this.consumeTimes;
    }

    public void setConsumeTimes(int consumeTimes) {
        this.consumeTimes = consumeTimes;
    }

    public double getConsumeAmount() {
        return this.consumeAmount;
    }

    public void setConsumeAmount(double consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getLastConsumeDatetime() {
        return this.lastConsumeDatetime;
    }

    public void setLastConsumeDatetime(Date lastConsumeDatetime) {
        this.lastConsumeDatetime = lastConsumeDatetime;
    }

    public String getLocalPosSN() {
        return localPosSN;
    }

    public void setLocalPosSN(String localPosSN) {
        this.localPosSN = localPosSN;
    }

    public int getbQuerySmallerThanStartID() {
        return bQuerySmallerThanStartID;
    }

    public void setbQuerySmallerThanStartID(int bQuerySmallerThanStartID) {
        this.bQuerySmallerThanStartID = bQuerySmallerThanStartID;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String toString() {
        return "Vip  ID=" + getID() + ", syncDatetime=" + getSyncDatetime() + ", icid=" + getICID() +
                ", name=" + getName() + ", email=" + getEmail() +
                ", consumeTimes=" + getConsumeTimes() + ", consumeAmount=" + getConsumeAmount() + ", district=" + getDistrict() +
                ", category=" + getCategory() + ", birthday=" + getBirthday() +
                ", point=" + getBonus() + ", lastConsumeDatetime=" + getLastConsumeDatetime()  + ", MyPosSN=" + getLocalPosSN();
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        Vip vip = (Vip) arg0;
        if ((ignoreIDInComparision == true ? true : (vip.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))  //
                && vip.getMobile().equals(this.getMobile()) && printComparator("mobile")//
                && vip.getICID().equals(this.getICID()) && printComparator("icid")//
                && vip.getName().equals(this.getName()) && printComparator("name")//
                && vip.getEmail().equals(this.getEmail()) && printComparator("email")//
                && vip.getConsumeTimes() == this.getConsumeTimes() && printComparator("consumeTimes")//
                && Math.abs(GeneralUtil.sub(vip.getConsumeAmount(), this.getConsumeAmount())) < TOLERANCE && printComparator("consumeAmount")//
                && vip.getDistrict().equals(this.getDistrict()) && printComparator("district")//
                && vip.getCategory() == this.getCategory() && printComparator("category")//
                // && vip.getBirthday() == this.getBirthday() && printComparator("birthday")//
                // && vip.getExpireDatetime() == this.getExpireDatetime() && printComparator("expireDatetime")//
                && Math.abs(GeneralUtil.sub(vip.getBonus(), this.getBonus())) < TOLERANCE && printComparator("point")//
                // && vip.getLastConsumeDatetime() == this.getLastConsumeDatetime() && printComparator("lastConsumeDatetime")//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Vip vip = new Vip();
        vip.setID(this.getID());
        vip.setMobile(this.getMobile());
        vip.setLocalPosSN(this.getLocalPosSN());
        vip.setiCID(this.getICID());
        vip.setName(this.getName());
        vip.setEmail(this.getEmail());
        vip.setConsumeTimes(this.getConsumeTimes());
        vip.setConsumeAmount(this.getConsumeAmount());
        vip.setDistrict(this.getDistrict());
        vip.setCategory(this.getCategory());
        vip.setBirthday((Date) this.getBirthday().clone());
        vip.setBonus(this.getBonus());
        vip.setLastConsumeDatetime((Date) this.getLastConsumeDatetime().clone());
        vip.setReturnObject(this.getReturnObject());
        vip.setCardID(this.getCardID());
        return vip;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行 Vip.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            ID = object.getLong(field.getFIELD_NAME_ID());
            mobile = object.getString(field.getFIELD_NAME_mobile());
            localPosSN = object.getString(field.getFIELD_NAME_localPosSN());
            iCID = object.getString(field.getFIELD_NAME_iCID());
            name = object.getString(field.getFIELD_NAME_name());
            email = object.getString(field.getFIELD_NAME_email());
            consumeTimes = object.getInt(field.getFIELD_NAME_consumeTimes());
            consumeAmount = Double.parseDouble(object.getString(field.getFIELD_NAME_consumeAmount()));
            district = object.getString(field.getFIELD_NAME_district());
            category = object.getInt(field.getFIELD_NAME_category());
            bonus = object.getInt(field.getFIELD_NAME_bonus());
            //
            String tmpBirthday = object.getString(field.getFIELD_NAME_birthday());
            if (!StringUtils.isEmpty(tmpBirthday)) {
                birthday = Constants.getSimpleDateFormat2().parse(tmpBirthday);
                if (birthday == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_birthday() + "=" + tmpBirthday);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_birthday() + "=" + tmpBirthday);
                }
            }
            //
            String tmpLastConsumeDatetime = object.getString(field.getFIELD_NAME_lastConsumeDatetime());
            if (!StringUtils.isEmpty(tmpLastConsumeDatetime)) {
                lastConsumeDatetime = Constants.getSimpleDateFormat2().parse(tmpLastConsumeDatetime);
                if (lastConsumeDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_lastConsumeDatetime() + "=" + tmpLastConsumeDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_lastConsumeDatetime() + "=" + tmpLastConsumeDatetime);
                }
            }
            //
            String tmpSyncDatetime = object.getString(field.getFIELD_NAME_syncDatetime());
           if(!StringUtils.isEmpty(tmpSyncDatetime)){
               syncDatetime = Constants.getSimpleDateFormat2().parse(tmpSyncDatetime);
               if (syncDatetime == null) {
                   System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                   throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
               }
           }
            //
            syncType = object.getString(field.getFIELD_NAME_syncType());
            syncSequence = object.getInt(field.getFIELD_NAME_syncSequence());
            return this;
        } catch (Exception e) {
            System.out.println("Vip.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 Vip.parse1，s=" + s);

        try {
            JSONObject joVIP = new JSONObject(s);
            Vip vip = (Vip) doParse1(joVIP);
            if (vip == null) {
                return null;
            }
            return vip;
        } catch (JSONException e) {
            System.out.println("Vip.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行 Vip.parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> vipList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                Vip vip = new Vip();
                if (vip.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                vipList.add(vip);
            }
            return vipList;
        } catch (Exception e) {
            System.out.println("Vip.parseN 出现异常，错误信息为" + e.getMessage());
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

    @Override
    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public void setICID(String iCID) {
        this.iCID = iCID;
    }

    public int getStartRetailTreadeIDInSQLite() {
        return this.startRetailTreadeIDInSQLite;
    }

    public void setStartRetailTreadeIDInSQLite(int startRetailTreadeIDInSQLite) {
        this.startRetailTreadeIDInSQLite = startRetailTreadeIDInSQLite;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !FieldFormat.checkMobile(mobile)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_localPosSN(), Pos.FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(localPosSN) && !FieldFormat.checkPosSN(localPosSN)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(iCID) && iCID.length() != LENGTH_ICID) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_iCID(), FIELD_ERROR_icid, sbError) && !FieldFormat.checkICID(iCID)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && StringUtils.isEmpty(name) || !FieldFormat.checkHumanName(name)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && !StringUtils.isEmpty(email) && !FieldFormat.checkEmail(email)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_consumeTimes(), FIELD_ERROR_consumeTimes, sbError) && consumeTimes != 0) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_consumeAmount(), FIELD_ERROR_consumeAmount, sbError) && consumeAmount != 0) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_district(), FIELD_ERROR_district, sbError) && !StringUtils.isEmpty(district) && district.length() > MAX_LENGTH_District) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && !FieldFormat.checkID(category)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_birthday(), FIELD_ERROR_DateTime, sbError) && birthday != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
            if (!FieldFormat.checkDate(sdf.format(birthday))) {
                return sbError.toString();
            }
        }
        if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_bonus, sbError) && bonus != 0) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_cardID(), FIELD_ERROR_cardID, sbError) && cardID <= 0) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_email(), FIELD_ERROR_Email, sbError) && !StringUtils.isEmpty(email) && (email.length() > MAX_LENGTH_String1 || !FieldFormat.checkEmail(email))) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_birthday(), FIELD_ERROR_DateTime, sbError) && birthday != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
            if (!FieldFormat.checkDate(sdf.format(birthday))) {
                return sbError.toString();
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
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Vip_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_Long:
                        if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) //
                                && !(Long.valueOf(conditions[0]) == EnumStatusVip.ESV_Normal.getIndex() || Long.valueOf(conditions[0]) == EnumStatusVip.ESV_LossDeclaration.getIndex() || Long.valueOf(conditions[0]) == EnumStatusVip.ESV_Deleted.getIndex())) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long_String:
                        if (conditions.length == 2) {
                            if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) //
                                    && !(Long.valueOf(conditions[0]) == EnumStatusVip.ESV_Normal.getIndex() || Long.valueOf(conditions[0]) == EnumStatusVip.ESV_LossDeclaration.getIndex() || Long.valueOf(conditions[0]) == EnumStatusVip.ESV_Deleted.getIndex())) {
                                return sbError.toString();
                            }

                            if (printCheckField(field.getFIELD_NAME_localPosSN(), Pos.FIELD_ERROR_PosSN, sbError) && !StringUtils.isEmpty(conditions[1]) && !FieldFormat.checkPosSN(conditions[1])) {
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
            case BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx:
                // 根据mobile或者CardSN查询
                if(!StringUtils.isEmpty(mobile)) {
                    if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !FieldFormat.checkMobile(mobile)) {
                        return sbError.toString();
                    }
                } else if(!StringUtils.isEmpty(vipCardSN)) {
                    if (printCheckField(field.getFIELD_NAME_vipCardSN(), FIELD_ERROR_vipCardSN, sbError) && vipCardSN.length() != Max_LENGTH_VipCardSN) {
                        return sbError.toString();
                    }
                } else{
                    return FIELD_ERROR_RetrieveNByMobileOrVipCardSN;
                }
                return "";
            default:
                if (printCheckField(field.getFIELD_NAME_category(), FEILD_ERROR_category, sbError) && category != BaseSQLiteBO.INVALID_INT_ID && !FieldFormat.checkID(category)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_mobile(), FIELD_ERROR_mobile, sbError) && !FieldFormat.checkMobile(mobile)) {
                    return sbError.toString();
                }
                break;
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

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getCardID() {
        return this.cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public enum EnumStatusVip {
        ESV_Normal("normal", 0), //
        ESV_LossDeclaration("loss declaration", 1), //
        ESV_Deleted("deleted", 2);

        private String name;
        private int index;

        private EnumStatusVip(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusVip c : EnumStatusVip.values()) {
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
