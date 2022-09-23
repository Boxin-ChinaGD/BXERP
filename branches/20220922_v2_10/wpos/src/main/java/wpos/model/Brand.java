package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_Brand")
public class Brand extends BaseModel {

    public static int MAX_LENGTH_BrandName = 20;
    public static final String FIELD_ERROR_name = "品牌名字为英文或数字的组合，长度为1到20";
    public static final String ACTION_ERROR_UpdateDelete1 = "默认的品牌不能修改或删除！";

    //@Transient
    public static final String HTTP_Brand_Create = "brandSync/createEx.bx";
    //@Transient
    public static final String feedbackURL_FRONT = "brandSync/feedbackEx.bx?sID=";
    //@Transient
    public static final String feedbackURL_BEHINE = "&errorCode=EC_NoError";
    //@Transient
    public static final String HTTP_BRAND_RetrieveN = "brandSync/retrieveNEx.bx";
    //@Transient
    public static final String HTTP_BRAND_RetrieveNC = "brand/retrieveNEx.bx?pageIndex=";
    //@Transient
    public static final String HTTP_BRAND_RETRIEVENC_PageSize = "&pageSize=";
    public static final BrandField field = new BrandField();

    @Transient
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    //@Transient
    public static final String HTTP_BRAND_Update = "brandSync/updateEx.bx";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

    @Column(name = "F_SyncType")
    protected String syncType;

    //@Generated(hash = 1951151489)
//    public Brand(Long ID, //@NotNull String name, Date syncDatetime, String syncType) {
//        this.ID = ID;
//        this.name = name;
//        this.syncDatetime = syncDatetime;
//        this.syncType = syncType;
//    }

    //@Generated(hash = 128156227)
    public Brand() {
    }

    @Override
    public Integer getID() {
        return this.ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Brand{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                '}';
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            name = jo.getString(field.getFIELD_NAME_name());
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType());
            //
            String tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
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
        System.out.println("正在执行parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            System.out.println("执行parse1异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray jsonArray1) {
        System.out.println("正在执行parseN，jsonArray1=" + (jsonArray1 == null ? null : jsonArray1.toString()));

        List<BaseModel> brandList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray1.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Brand b = new Brand();
                b.doParse1(jsonObject1);
                brandList.add(b);
            }
        } catch (Exception e) {
            System.out.println("执行parseN异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return brandList;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Brand b = new Brand();
        b.setID(this.getID());
        b.setName(new String(this.getName()));
//        b.setSyncDatetime((Date) this.getSyncDatetime().clone());
        b.setReturnObject(this.getReturnObject());

        return b;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        Brand b = (Brand) arg0;
        if ((ignoreIDInComparision == true ? true : (b.getID() == (this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && b.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
                ) {
            return 0;
        }
        return -1;
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

        if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkBrandName(name)) {
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

        if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkBrandName(name)) {
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
        if (!StringUtils.isEmpty(sql)) {
            int total = StringUtils.contain(sql, "%s");
            if (this.printCheckField(field.getFIELD_NAME_sql(), FIELD_ERROR_SQL, sbError) && conditions != null && total != conditions.length) {
                return sbError.toString();
            }
        }
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_String:
                        if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_BrandName)) {
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
                            if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(conditions[0].length() <= MAX_LENGTH_BrandName)) {
                                return sbError.toString();
                            }
                            if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[1]))) {
                                return sbError.toString();
                            }
                        } else {
                            return FieldFormat.FIELD_ERROR_Parameter;
                        }
                        break;
                    case ESUC_Long_String:
                        if (conditions.length == 2) {
                            if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {
                                return sbError.toString();
                            }
                            if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(conditions[1].length() <= MAX_LENGTH_BrandName)) {
                                return sbError.toString();
                            }
                        } else {
                            return FieldFormat.FIELD_ERROR_Parameter;
                        }
                        break;
                    default:
                        throw new RuntimeException("未定义的Sub Use Case ID！");
                }
                break;
            default:
                if (!StringUtils.isEmpty(name)) {
                    if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_BrandName)) {
                        return sbError.toString();
                    }
                }
                break;
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }
        return "";
    }
}
