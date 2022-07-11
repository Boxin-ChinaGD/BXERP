package com.bx.erp.model;

import com.bx.erp.bo.BaseHttpBO;
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

@Entity(nameInDb = "Staff")
public class Staff extends BaseAuthenticationModel {
    private static final int Zero = 0;
    private static final int MAX_LENGTH_Unionid = 100;
    private static final int MAX_LENGTH_Openid = 100;
    private static final int MAX_LENGTH_String3 = 12;
    public static final String FIELD_ERROR_string3 = "string3的长度为(" + Zero + ", " + MAX_LENGTH_String3 + "]";
    public static final String FIELD_ERROR_unionid = "unionid的长度为[" + Zero + ", " + MAX_LENGTH_Unionid + "]";
    public static final String FIELD_ERROR_openid = "openid的长度为[" + Zero + ", " + MAX_LENGTH_Openid + "]";
    public static final String FIELD_ERROR_ID = "ID不存在";
    public static final String FIELD_ERROR_name = "门店用户的名字为中，英文和数字的组合且长度不能为" + Zero;
    public static final String FIELD_ERROR_IDorPhone = "ID和手机号必须要有一个是合法的";
    public static final String FIELD_ERROR_phone = "手机格式错误，应为" + FieldFormat.LENGTH_Mobile + "位的数字";
    public static final String FIELD_ERROR_ICID = "身份证号码格式不对";
    public static final String FIELD_ERROR_weChat = "门店用户的微信号必须是5-20位的数字和英文组成的";
    public static final String FIELD_ERROR_passwordExpireDate = "门店用户的密码有效期错误";
    public static final String FIELD_ERROR_checkUniqueField = "非法的值";
    public static final String FIELD_ERROR_samePassword = "新旧密码不能相同";
    public static final String FIELD_ERROR_isFirstTimeLogin = "是否首次登陆字段的值和期望的不一样";
    public static final String FIELD_ERROR_salt = "盐值应该为32个数字或大写字母的组合";
    public static final String FIELD_ERROR_shopID = "门店ID必须大于" + Zero;
    public static final String FIELD_ERROR_departmentID = "部门ID必须大于" + Zero;
    public static final String FIELD_ERROR_roleID = "角色ID必须大于" + Zero;
    public static final String FIELD_ERROR_status = "店员的状态码只能是" + EnumStatusStaff.ESS_Incumbent.index + "或" + EnumStatusStaff.ESS_Resigned.index;

    public static final int INVOLVE_RESIGNED = 1;
    public static final int NOT_INVOLVE_RESIGNED = 0;
    public static final int All_INVOLVE_RESIGNED = 2;
    public static final String FIELD_ERROR_involvedResigned = "是否查询离职员工只能是" + INVOLVE_RESIGNED + "或" + NOT_INVOLVE_RESIGNED;
    public static final String FIELD_ERROR_RN_involvedResigned = "是否查询离职员工只能是" + INVOLVE_RESIGNED + "或" + NOT_INVOLVE_RESIGNED + "或" + All_INVOLVE_RESIGNED;
    public static final int FOR_ModifyPassword = 1;
    public static final int FOR_CreateNewStaff = 1;

    public static final int RETURN_SALT = 1;
    public static final int NOT_RETURN_SALT = 0;
    public static final String FIELD_ERROR_ReturnSalt = "是否返回盐值只能是" + RETURN_SALT + "或" + NOT_RETURN_SALT;

    public static final StaffField field = new StaffField();

    @Transient
    public static final String FIRST_LOGIN = "FIRST_LOGIN";
    @Transient
    public static final String LOGIN_FAILURE = "LOGIN_FAILURE";
    @Transient
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    @Transient
    public static final String HTTP_STAFF_Create = "staff/createEx.bx";
    @Transient
    public static final String HTTP_STAFF_RetrieveN = "staffSync/retrieveNEx.bx";
    @Transient
    public static final String HTTP_STAFF_RetrieveResigned = "staff/retrieveResigned.bx?pageIndex=";
    @Transient
    public static final String HTTP_STAFF_RetrieveNC = "staff/retrieveNEx.bx?pageIndex=";
    @Transient
    public static final String HTTP_STAFF_PageSize = "&pageSize=";
    @Transient
    public static final String HTTP_FeedbackURL_FRONT = "staffSync/feedbackEx.bx?sID=";
    @Transient
    public static final String HTTP_FeedbackURL_BEHIND = "&errorCode=EC_NoError";
    @Transient
    public static final String HTTP_STAFF_Update = "staff/updateEx.bx";
    @Transient
    public static final String HTTP_STAFF_Delete = "staff/deleteEx.bx?ID=";
    @Transient
    public static final String HTTP_STAFF_ResetMyPassword = "staff/resetMyPasswordEx.bx";
    @Transient
    public static final String HTTP_STAFF_getToken = "staff/getTokenEx.bx";
    @Transient
    public static final String HTTP_STAFF_login = "staff/loginEx.bx";
    @Transient
    public static final String HTTP_BXSTAFF_getToken = "bxStaff/getTokenEx.bx";
    @Transient
    public static final String SESSION_BxStaffMobile = "SESSION_BxStaffMobile";
    @Transient
    public static final String mobile = "mobile";
    @Transient
    public static final String HTTP_BXSTAFF_login = "bxStaff/loginEx.bx";
    @Transient
    public static final String HTTP_Ping = "staff/pingEx.bx";
    @Transient
    protected String oldPwdEncrypted;
    @Transient
    protected String newPwdEncrypted;
    /**
     * Pos获取到的离职员工的ID，以逗号隔开，形如：1,3,7。Pos收到后，将本地的员工删除
     */
    @Transient
    public static final String RESIGNED_ID = "resigned_ID";

    public String getOldPwdEncrypted() {
        return oldPwdEncrypted;
    }

    public void setOldPwdEncrypted(String oldPwdEncrypted) {
        this.oldPwdEncrypted = oldPwdEncrypted;
    }

    public String getNewPwdEncrypted() {
        return newPwdEncrypted;
    }

    public void setNewPwdEncrypted(String newPwdEncrypted) {
        this.newPwdEncrypted = newPwdEncrypted;
    }

    @Transient
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    private Long ID;

    @NotNull
    @Property(nameInDb = "F_Phone")
    private String phone;

    @NotNull
    @Property(nameInDb = "F_Name")
    private String name;

    @Property(nameInDb = "F_WeChat")
    private String weChat;

    @Property(nameInDb = "F_ICID")
    private String ICID;

    @Property(nameInDb = "F_PasswordExpireDate")
    private Date passwordExpireDate;

    @Property(nameInDb = "F_IsFirstTimeLogin")
    private int isFirstTimeLogin;

    @Property(nameInDb = "F_shopID")
    private int shopID;

    @Property(nameInDb = "F_DepartmentID")
    private int departmentID;

    @Property(nameInDb = "F_Status")
    private int status;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;


    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @Property(nameInDb = "F_PwdEncrypted")
    protected String pwdEncrypted;

    public String getPwdEncrypted() {
        return pwdEncrypted;
    }

    public void setPwdEncrypted(String pwdEncrypted) {
        this.pwdEncrypted = pwdEncrypted;
    }

    @Property(nameInDb = "F_Salt")
    protected String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Property(nameInDb = "F_passwordInPOS")
    protected String passwordInPOS;

    @Transient
    protected boolean isParesSyncDatetime = true;

    public void setParesSyncDatetime(boolean paresSyncDatetime) {
        isParesSyncDatetime = paresSyncDatetime;
    }

    @Transient
    protected String oldMD5;

    public String getOldMD5() {
        return oldMD5;
    }

    public void setOldMD5(String oldMD5) {
        this.oldMD5 = oldMD5;
    }

    @Transient
    protected String newMD5;

    public String getNewMD5() {
        return newMD5;
    }

    public void setNewMD5(String newMD5) {
        this.newMD5 = newMD5;
    }

    /**
     * 非DB字段。1=重置他人密码。0=修改自己的密码
     * **  ( pos端只有修改自己密码的接口， 故在传递该值时已默认为0, 无需再传递)
     */
    @Transient
    protected int isResetOtherPassword;

    public int getIsResetOtherPassword() {
        return isResetOtherPassword;
    }

    public void setIsResetOtherPassword(int isResetOtherPassword) {
        this.isResetOtherPassword = isResetOtherPassword;
    }

    @Transient
    protected int involvedResigned;

    public int getInvolvedResigned() {
        return involvedResigned;
    }

    public void setInvolvedResigned(int involvedResigned) {
        this.involvedResigned = involvedResigned;
    }

    @Property(nameInDb = "F_roleID")
    protected int roleID;

    public int getRoleID() {
        return this.roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    @Transient
    protected int isBXStaff;

    public int getIsBXStaff() {
        return isBXStaff;
    }

    public void setIsBXStaff(int isBXStaff) {
        this.isBXStaff = isBXStaff;
    }

    @Transient
    protected int isLoginFromPos;

    @Generated(hash = 959580165)
    public Staff(Long ID, @NotNull String phone, @NotNull String name, String weChat, String ICID, Date passwordExpireDate, int isFirstTimeLogin, int shopID, int departmentID, int status, Date syncDatetime,
            String syncType, String pwdEncrypted, String salt, String passwordInPOS, int roleID) {
        this.ID = ID;
        this.phone = phone;
        this.name = name;
        this.weChat = weChat;
        this.ICID = ICID;
        this.passwordExpireDate = passwordExpireDate;
        this.isFirstTimeLogin = isFirstTimeLogin;
        this.shopID = shopID;
        this.departmentID = departmentID;
        this.status = status;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
        this.pwdEncrypted = pwdEncrypted;
        this.salt = salt;
        this.passwordInPOS = passwordInPOS;
        this.roleID = roleID;
    }

    @Generated(hash = 1774984890)
    public Staff() {
    }

    public int getIsLoginFromPos() {
        return isLoginFromPos;
    }

    public void setIsLoginFromPos(int isLoginFromPos) {
        this.isLoginFromPos = isLoginFromPos;
    }


    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeChat() {
        return this.weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getICID() {
        return this.ICID;
    }

    public void setICID(String ICID) {
        this.ICID = ICID;
    }

    public Date getPasswordExpireDate() {
        return this.passwordExpireDate;
    }

    public void setPasswordExpireDate(Date passwordExpireDate) {
        this.passwordExpireDate = passwordExpireDate;
    }

    public int getIsFirstTimeLogin() {
        return this.isFirstTimeLogin;
    }

    public void setIsFirstTimeLogin(int isFirstTimeLogin) {
        this.isFirstTimeLogin = isFirstTimeLogin;
    }

    public int getShopID() {
        return this.shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getDepartmentID() {
        return this.departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "ID=" + ID +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", weChat='" + weChat + '\'' +
                ", ICID='" + ICID + '\'' +
                ", passwordExpireDate=" + passwordExpireDate +
                ", isFirstTimeLogin=" + isFirstTimeLogin +
                ", shopID=" + shopID +
                ", departmentID=" + departmentID +
                ", status=" + status +
                ", syncDatetime=" + syncDatetime +
                ", syncType='" + syncType + '\'' +
                ", pwdEncrypted='" + pwdEncrypted + '\'' +
                ", salt='" + salt + '\'' +
                ", passwordInPOS='" + passwordInPOS + '\'' +
                ",roleID=" + roleID + '\'' +
                '}';
    }

    public enum EnumStatusStaff {
        ESS_Incumbent("Incumbent", 0), //
        ESS_Resigned("Resigned", 1);

        private String name;
        private int index;

        private EnumStatusStaff(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusStaff c : EnumStatusStaff.values()) {
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
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行 Staff.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            ID = Long.valueOf(object.getString(field.getFIELD_NAME_ID()));
            phone = object.getString(field.getFIELD_NAME_phone());
            name = object.getString(field.getFIELD_NAME_name());
            weChat = object.getString(field.getFIELD_NAME_weChat());
            ICID = object.getString(field.getFIELD_NAME_ICID());
            passwordInPOS = object.getString(field.getFIELD_NAME_passwordInPOS());
            isFirstTimeLogin = object.getInt(field.getFIELD_NAME_isFirstTimeLogin());
            salt = object.getString(field.getFIELD_NAME_salt());
            pwdEncrypted = object.getString(field.getFIELD_NAME_pwdEncrypted());
            shopID = object.getInt(field.getFIELD_NAME_shopID());
            departmentID = object.getInt(field.getFIELD_NAME_departmentID());
            status = object.getInt(field.getFIELD_NAME_status());
            returnObject = object.getInt(field.getFIELD_NAME_returnObject());
            roleID = object.getInt(field.getFIELD_NAME_roleID());//...登录时返回改值，判断角色
//            string1 = object.getString(getFIELD_NAME_string1());
//            if (isParesSyncDatetime){
////                String tmp = object.getString(getFIELD_NAME_syncDatetime());
////                if(!StringUtils.isEmpty(tmp)){
////                    syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
////                    if (syncDatetime == null) {
////                        System.out.println("无法解析该日期：" + getFIELD_NAME_syncDatetime() + "=" + tmp);
////                        throw new RuntimeException("无法解析该日期：" + getFIELD_NAME_syncDatetime() + "=" + tmp);
////                    }
////                }
////            }
            syncType = object.getString(field.getFIELD_NAME_syncType());
            syncSequence = object.getInt(field.getFIELD_NAME_syncSequence());
            isLoginFromPos = object.getInt(field.getFIELD_NAME_isLoginFromPos());
            return this;
        } catch (Exception e) {
            System.out.println("Staff.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 Staff.parse1，s=" + s);

        Staff staff = new Staff();
        try {
            JSONObject joSTAFF = new JSONObject(s);
            staff = (Staff) doParse1(joSTAFF);
            if (staff == null) {
                return null;
            }
            return staff;
        } catch (JSONException e) {
            System.out.println("Staff.parse1 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行 Staff.parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> staffList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                Staff staff = new Staff();
                if (staff.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                staffList.add(staff);
            }
            return staffList;
        } catch (Exception e) {
            System.out.println("Staff.parseN 出现异常，错误信息为" + e.getMessage());
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
    public BaseModel clone() throws CloneNotSupportedException {
        Staff obj = new Staff();
        obj.setID(this.getID());
        obj.setPhone(new String(this.getPhone()));
        obj.setName(new String(this.getName()));
        obj.setWeChat(this.getWeChat());
        obj.setICID(this.getICID());
        obj.setPasswordInPOS(this.getPasswordInPOS());
        obj.setPasswordExpireDate(this.getPasswordExpireDate());
        obj.setIsFirstTimeLogin(this.getIsFirstTimeLogin());
        obj.setShopID(this.getShopID());
        obj.setDepartmentID(this.getDepartmentID());
        obj.setStatus(this.getStatus());
        obj.setSyncDatetime((Date) this.getSyncDatetime().clone());
        obj.setReturnObject(returnObject);
        obj.setReturnSalt(returnSalt);
        obj.setSalt(salt);
        obj.setRoleID(roleID);
        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        Staff s = (Staff) arg0;
        if ((ignoreIDInComparision == true ? true : (s.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && s.getPhone().equals(this.getPhone()) && printComparator(field.getFIELD_NAME_phone())//
                && s.getWeChat().equals(this.getWeChat()) && printComparator(field.getFIELD_NAME_weChat())//
                && s.getICID().equals(this.getICID()) && printComparator(field.getFIELD_NAME_ICID())//
                && s.getIsFirstTimeLogin() == this.getIsFirstTimeLogin() && printComparator(field.getFIELD_NAME_isFirstTimeLogin())//
                && s.getShopID() == this.getShopID() && printComparator(field.getFIELD_NAME_shopID())//
                && s.getDepartmentID() == this.getDepartmentID() && printComparator(field.getFIELD_NAME_departmentID())//
                && s.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status())//
                && s.getSyncType().equals(this.getSyncType()) && printComparator(field.getFIELD_NAME_syncType())
                && s.getRoleID() == this.getRoleID() && printComparator(field.getFIELD_NAME_roleID())
        ) {
            return 0;
        }
        return -1;
    }

    public String getPasswordInPOS() {
        return this.passwordInPOS;
    }

    public void setPasswordInPOS(String passwordInPOS) {
        this.passwordInPOS = passwordInPOS;
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

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(ICID) && !FieldFormat.checkICID(ICID)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(weChat) && !FieldFormat.checkWeChat(weChat)) {// 接受空值
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_salt, sbError) && !FieldFormat.checkSalt(salt)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && !(isFirstTimeLogin == EnumBoolean.EB_Yes.getIndex() || isFirstTimeLogin == EnumBoolean.EB_NO.getIndex())) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_departmentID(), FIELD_ERROR_departmentID, sbError) && !FieldFormat.checkID(departmentID)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_roleID(), FIELD_ERROR_roleID, sbError) && !FieldFormat.checkID(roleID)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusStaff.ESS_Incumbent.index || status == EnumStatusStaff.ESS_Resigned.index)) {
            return sbError.toString();
        }
        //
        if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_ReturnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
            return sbError.toString();
        }
        //
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        switch (iUseCaseID) {
            case BaseHttpBO.CASE_CheckStaff_ResetMyPassword:
                if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) { //
                    return sbError.toString();
                }
                // String1是旧密码，String2是新密码，这两个字段不能相同
                if (!StringUtils.isEmpty(oldMD5) && !StringUtils.isEmpty(newMD5)) {
                    if (oldMD5.equals(newMD5)) {
                        return FIELD_ERROR_samePassword;
                    }
                }
                if (printCheckField(field.getFIELD_NAME_isFirstTimeLogin(), FIELD_ERROR_isFirstTimeLogin, sbError) && isFirstTimeLogin != EnumBoolean.EB_NO.getIndex()) {//
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_ReturnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
                    return sbError.toString();
                }
                break;
            default:
                if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkHumanName(name)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(phone)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_ICID(), FIELD_ERROR_ICID, sbError) && !StringUtils.isEmpty(ICID) && !FieldFormat.checkICID(ICID)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_weChat(), FIELD_ERROR_weChat, sbError) && !StringUtils.isEmpty(weChat) && !FieldFormat.checkWeChat(weChat)) {// 接受空值
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_departmentID(), FIELD_ERROR_departmentID, sbError) && !FieldFormat.checkID(departmentID)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_ReturnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !(status == EnumStatusStaff.ESS_Incumbent.index || status == EnumStatusStaff.ESS_Resigned.index)) {
                    return sbError.toString();
                }
                //
                if (printCheckField(field.getFIELD_NAME_roleID(), FIELD_ERROR_roleID, sbError) && !FieldFormat.checkID(roleID)) {
                    return sbError.toString();
                }
                //
                break;
        }
        return "";

    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            default:
                if (ID <= 0 && !FieldFormat.checkMobile(phone == null ? "" : phone)) {
                    return FIELD_ERROR_IDorPhone;
                }
                if (printCheckField(field.getFIELD_NAME_involvedResigned(), FIELD_ERROR_involvedResigned, sbError) && !(involvedResigned == INVOLVE_RESIGNED || involvedResigned == NOT_INVOLVE_RESIGNED)) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_returnSalt(), FIELD_ERROR_ReturnSalt, sbError) && !(returnSalt == RETURN_SALT || returnSalt == NOT_RETURN_SALT)) {
                    return sbError.toString();
                }
                break;
        }
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Staff_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_String:
                        if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(conditions[0])) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long:
                        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_String_String:
                        if (printCheckField(field.getFIELD_NAME_phone(), FIELD_ERROR_phone, sbError) && !FieldFormat.checkMobile(conditions[0])) {
                            return sbError.toString();
                        }
                        if(printCheckField(field.getFIELD_NAME_salt(), FIELD_ERROR_salt, sbError) && !FieldFormat.checkSalt(conditions[1])) {
                            return sbError.toString();
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

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

}
