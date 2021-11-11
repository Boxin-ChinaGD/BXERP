package wpos.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.MD5Util;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "T_Company")
public class Company extends BaseModel {

    public static final int LENGTH_BusinessLicenseSN1 = 15;
    public static final int LENGTH_BusinessLicenseSN2 = 18;
    public static final int MAX_LENGTH_brandName = 20;
    public static final int MAX_LENGTH_BusinessLicensePicture = 128;

    public static final String FIELD_ERROR_status = "创建公司时公司的状态必须为0";
    public static final String FIELD_ERROR_checkUniqueField = "非法的值";
    public static final String FIELD_ERROR_businessLicenseSN = "中国的营业执照全部是" + LENGTH_BusinessLicenseSN1 + "位或" + LENGTH_BusinessLicenseSN2 + "位，由纯数字或纯大写字母或两者混合组成，没有空格";
    public static final String FIELD_ERROR_businessLicensePicture = "营业执照路径长度不能超过" + MAX_LENGTH_BusinessLicensePicture;
    public static final String FIELD_ERROR_bossPhone = "老板的手机号码格式不正确";
    public static final String FIELD_ERROR_key = "key值格式错误";
    public static final String FIELD_ERROR_submchid = "子商户号是null或者空串或者长度为10位的数字";
    public static final String FIELD_ERROR_name = "公司名称格式应为：中英文数字，1到12个字符";
    public static final String FIELD_ERROR_DBUserName = "数据库用户名格式应为：数字、字母和下划线的组合，但首字符必须是字母，不能出现空格";
    public static final String FIELD_ERROR_authorizerAppid_authorizerRefreshToken = "authorizerAppid和authorizerRefreshToken或者都为空，或者长度都要>0";
    public static final String FIELD_ERROR_brandName = "商家招牌不能为null或空串，长度不能大于" + MAX_LENGTH_brandName;

    //@Transient
    public static final String TAG = "company";
    //@Transient
    public static final String DBUserName_Default = "root";
    //@Transient
    public static final String DBUserPassword_Default = "WEF#EGEHEH$$^*DI";

    public static final String HTTP_Company_Create = "company/createEx.bx";
    public static final String HTTP_Company_Delete = "company/deleteEx.bx?ID=";
    public static final String HTTP_Company_UploadBusinessLicensePicture = "company/uploadBusinessLicensePictureEx.bx";
    public static final CompanyField field = new CompanyField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    //@NotNull
    @Column(name = "F_SN")
    protected String SN;

    //@NotNull
    @Column(name = "F_Status")
    protected int status;

    @Column(name = "F_BusinessLicenseSN")
    protected String businessLicenseSN;

    @Column(name = "F_BusinessLicensePicture")
    protected String businessLicensePicture;

    //@NotNull
    @Column(name = "F_BossName")
    protected String bossName;

    //@NotNull
    @Column(name = "F_BossPhone")
    protected String bossPhone;

    //@NotNull
    @Column(name = "F_BossPassword")
    protected String bossPassword;

    //@NotNull
    @Column(name = "F_BossWechat")
    protected String bossWechat;

    //@NotNull
    @Column(name = "F_DBName")
    protected String dbName;

    //@NotNull
    @Column(name = "F_Key")
    protected String key;

    @Column(name = "F_Submchid")
    protected String submchid;

    //@NotNull
    @Column(name = "F_ExpireDatetime")
    protected Date expireDatetime;

    //@NotNull
    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

    //@NotNull
    @Column(name = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Column(name = "F_DBUserName")
    protected String dbUserName;

    @Column(name = "F_DBUserPassword")
    protected String dbUserPassword;

    @Column(name = "F_BrandName")
    protected String brandName;

    @Column(name = "F_Logo")
    protected String logo;

    @Override
    public String toString() {
        return "Company [businessLicenseSN=" + businessLicenseSN + ", businessLicensePicture=" + businessLicensePicture + ", SN=" + SN + ", name=" + name +
                ", bossName=" + bossName + ", expireDatetime=" + expireDatetime + ", createDatetime=" + createDatetime + ", updateDatetime=" + updateDatetime +
                ", dbUserName=" + dbUserName + ", dbUserPassword=" + dbUserPassword + ", bossPhone=" + bossPhone + ", bossPassword=" + bossPassword + ", bossWechat=" + bossWechat + ", dbName=" + dbName + ", key=" + key + ", submchid=" + submchid + ", ID=" + ID + ", status=" + status + "]";
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Company c = new Company();
        c.setID(getID());
        c.setBusinessLicenseSN(getBusinessLicenseSN());
        c.setBusinessLicensePicture(getBusinessLicensePicture());
        c.setBossPhone(getBossPhone());
        c.setBossWechat(getBossWechat());
        c.setDbName(getDbName());
        c.setKey(getKey());
        c.setSubmchid(getSubmchid());
        c.setStatus(getStatus());
        c.setName(getName());
        c.setBossName(getBossName());
        c.setDbUserName(getDbUserName());
        c.setDbUserPassword(getDbUserPassword());
        c.setBossPassword(getBossPassword());
        c.setSN(getSN());
        c.setUpdateDatetime(getUpdateDatetime());
        c.setCreateDatetime(getCreateDatetime());
        c.setExpireDatetime(getExpireDatetime());
        c.setBrandName(getBrandName());
        c.setLogo(logo);
        return c;
    }

    @Override
    public int compareTo(BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        Company c = (Company) arg0;
        if (
                (ignoreIDInComparision == true ? true : c.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID()))//
                        && c.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())
                        && c.getSN().equals(this.getSN()) && printComparator(field.getFIELD_NAME_SN())
                        && c.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status()) //
                        && c.getBusinessLicenseSN().equals(this.getBusinessLicenseSN()) && printComparator(field.getFIELD_NAME_businessLicenseSN()) //
                        && c.getBusinessLicensePicture().equals(this.getBusinessLicensePicture()) && printComparator(field.getFIELD_NAME_businessLicensePicture()) //
                        && c.getBossName().equals(this.getBossName()) && printComparator(field.getFIELD_NAME_bossName())
                        && c.getBossPhone().equals(this.getBossPhone()) && printComparator(field.getFIELD_NAME_bossPhone()) //
                        && c.getBossWechat().equals(this.getBossWechat()) && printComparator(field.getFIELD_NAME_bossWechat()) //
                        && c.getDbName().equals(this.getDbName()) && printComparator(field.getFIELD_NAME_dbName()) //
                        && c.getKey().equals(this.getKey()) && printComparator(field.getFIELD_NAME_key()) //
                        && (c.getSubmchid() == null ? (this.getSubmchid() == null) : c.getSubmchid().equals(this.getSubmchid() == null ? "" : this.getSubmchid()) && printComparator(field.getFIELD_NAME_submchid())) //
                        && (c.getBrandName().equals(this.getBrandName()) && printComparator(field.getFIELD_NAME_brandName())) //
                        && c.getLogo().equals(this.getLogo()) && printComparator(field.getFIELD_NAME_logo()) //
        ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行Company的parse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getInteger(field.getFIELD_NAME_ID()));
            name = jo.getString(field.getFIELD_NAME_name());
            SN = jo.getString(field.getFIELD_NAME_SN());
            status = jo.getInteger(field.getFIELD_NAME_status());
            businessLicenseSN = jo.getString(field.getFIELD_NAME_businessLicenseSN());
            businessLicensePicture = jo.getString(field.getFIELD_NAME_businessLicensePicture());
            bossName = jo.getString(field.getFIELD_NAME_bossName());
            bossPhone = jo.getString(field.getFIELD_NAME_bossPhone());
            bossPassword = jo.getString(field.getFIELD_NAME_bossPassword());
            bossWechat = jo.getString(field.getFIELD_NAME_bossWechat());
            dbName = jo.getString(field.getFIELD_NAME_dbName());
            key = jo.getString(field.getFIELD_NAME_key());
            submchid = jo.getString(field.getFIELD_NAME_submchid());
            dbUserName = jo.getString(field.getFIELD_NAME_dbUserName());
            dbUserPassword = jo.getString(field.getFIELD_NAME_dbUserPassword());
            brandName = jo.getString(field.getFIELD_NAME_brandName());
            logo = jo.getString(field.getFIELD_NAME_logo());

            String temp = jo.getString(field.getFIELD_NAME_expireDatetime());
            if (!StringUtils.isEmpty(temp)) {
                expireDatetime = Constants.getSimpleDateFormat2().parse(temp);
                if (expireDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_expireDatetime() + "=" + temp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_expireDatetime() + "=" + temp);
                }
            }

            temp = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(temp)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(temp);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + temp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + temp);
                }
            }

            temp = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(temp)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(temp);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + temp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + temp);
                }
            }

            return this;
        } catch (Exception e) {
            System.out.println("Company.parse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Company的parse1，s=" + s);

        Company company = new Company();
        try {
            JSONObject joPos = JSONObject.parseObject(s);
            company = (Company) doParse1(joPos);
            if (company == null) {
                return null;
            }
            return company;
        } catch (JSONException e) {
            System.out.println("Company.parse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Integer getID() {
        return this.ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSN() {
        return this.SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBusinessLicenseSN() {
        return this.businessLicenseSN;
    }

    public void setBusinessLicenseSN(String businessLicenseSN) {
        this.businessLicenseSN = businessLicenseSN;
    }

    public String getBusinessLicensePicture() {
        return this.businessLicensePicture;
    }

    public void setBusinessLicensePicture(String businessLicensePicture) {
        this.businessLicensePicture = businessLicensePicture;
    }

    public String getBossName() {
        return this.bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getBossPhone() {
        return this.bossPhone;
    }

    public void setBossPhone(String bossPhone) {
        this.bossPhone = bossPhone;
    }

    public String getBossWechat() {
        return this.bossWechat;
    }

    public void setBossWechat(String bossWechat) {
        this.bossWechat = bossWechat;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubmchid() {
        return this.submchid;
    }

    public void setSubmchid(String submchid) {
        this.submchid = submchid;
    }

    public Date getExpireDatetime() {
        return this.expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
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

    public String getDbUserName() {
        return this.dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbUserPassword() {
        return this.dbUserPassword;
    }

    public void setDbUserPassword(String dbUserPassword) {
        this.dbUserPassword = dbUserPassword;
    }

    public String getBossPassword() {
        return this.bossPassword;
    }

    public void setBossPassword(String bossPassword) {
        this.bossPassword = bossPassword;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String getSyncType() {
        throw new RuntimeException("Not yet implemented!");
    }

    @Override
    public void setSyncType(String syncType) {
        throw new RuntimeException("Not yet implemented!");
    }

    @Override
    public Date getSyncDatetime() {
        return this.getSyncDatetime();
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        throw new RuntimeException("Not yet implemented!");
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        //Pos没有必要知道公司的太多信息。NBR也不会返回
//        if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && status != 0) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(businessLicenseSN))) {
//            return sbError.toString();
//        }
//        if (businessLicensePicture != null) {
//            if (printCheckField(field.getFIELD_NAME_businessLicensePicture(), FIELD_ERROR_businessLicensePicture, sbError) && businessLicensePicture.length() > MAX_LENGTH_BusinessLicensePicture) {
//                return sbError.toString();
//            }
//        }
//        if (printCheckField(field.getFIELD_NAME_bossPhone(), FIELD_ERROR_bossPhone, sbError) && !FieldFormat.checkMobile(bossPhone)) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_bossPassword(), FieldFormat.FIELD_ERROR_Password, sbError) && !FieldFormat.checkPassword(bossPassword)) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_bossWechat(), FieldFormat.FIELD_ERROR_wechat, sbError) && (!StringUtils.isEmpty(bossWechat) && !FieldFormat.checkWeChat(bossWechat))) {// 接受空值
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_dbName(), FieldFormat.FIELD_ERROR_dbName, sbError) && !FieldFormat.checkDbName(dbName)) {
//            return sbError.toString();
//        }

//        SimpleDateFormat sdf1 = new SimpleDateFormat(Constants.DATE_FORMAT_RetailTradeSN);
//        String date1 = sdf1.format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//        key = MD5Util.MD5(date1);
//        if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && !FieldFormat.checkCompanyKey(key)) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
//            return sbError.toString();
//        }
        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(name)) {
            return sbError.toString();
        }
//        if (printCheckField(field.getFIELD_NAME_bossName(), FieldFormat.FIELD_ERROR_HumanName, sbError) && !FieldFormat.checkHumanName(bossName)) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_dbUserName(), FIELD_ERROR_DBUserName, sbError) && !FieldFormat.checkDBUserName(dbUserName)) {
//            return sbError.toString();
//        }
//        if (printCheckField(field.getFIELD_NAME_dbUserPassword(), FieldFormat.FIELD_ERROR_Password, sbError) && !FieldFormat.checkPassword(dbUserPassword)) {
//            return sbError.toString();
//        }
        if (printCheckField(field.getFIELD_NAME_brandName(), FIELD_ERROR_brandName, sbError) && (StringUtils.isEmpty(brandName) || brandName.length() > MAX_LENGTH_brandName)) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && (!FieldFormat.checkID(ID))) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_businessLicenseSN(), FIELD_ERROR_businessLicenseSN, sbError) && (!FieldFormat.checkBusinessLicenseSN(businessLicenseSN))) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_businessLicensePicture(), FIELD_ERROR_businessLicensePicture, sbError) && businessLicensePicture.length() > MAX_LENGTH_BusinessLicensePicture) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_bossPhone(), FIELD_ERROR_bossPhone, sbError) && !FieldFormat.checkMobile(bossPhone)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_bossWechat(), FieldFormat.FIELD_ERROR_wechat, sbError) && (!StringUtils.isEmpty(bossWechat) && !FieldFormat.checkWeChat(bossWechat))) {// 接受空值
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_key(), FIELD_ERROR_key, sbError) && !FieldFormat.checkCompanyKey(key)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_submchid(), FIELD_ERROR_submchid, sbError) && !FieldFormat.checkSubmchid(submchid)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkCompanyName(name)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_bossName(), FieldFormat.FIELD_ERROR_HumanName, sbError) && !FieldFormat.checkHumanName(bossName)) {
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_brandName(), FIELD_ERROR_brandName, sbError) && (StringUtils.isEmpty(brandName) || brandName.length() > MAX_LENGTH_brandName)) {
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

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
