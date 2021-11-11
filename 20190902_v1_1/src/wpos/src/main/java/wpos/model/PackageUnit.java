package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wpos.helper.Constants;
import wpos.utils.DatetimeUtil;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_PackageUnit")
public class PackageUnit extends BaseModel {
    public static final int MAX_LENGTH_Name = 8;
    public static final int MIN_LENGTH_Name = 1;
    public static final String FIELD_ERROR_name = "名称长度为["+ MIN_LENGTH_Name + ", " + MAX_LENGTH_Name + "]";

    //@Transient
    public static final String HTTP_PackageUnit_RETRIEVENC_PageIndex = "packageUnit/retrieveNEx.bx?pageIndex=";
    //@Transient
    public static final String HTTP_PackageUnit_RETRIEVENC_PageSize = "&pageSize=";
    //@Transient
    public static final String HTTP_PackageUnit_RETRIEVENC_TypeStatus = "&type=-1&status=-1";

    public static final PackageUnitField field = new PackageUnitField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    @Column(name = "F_Name")
    protected String name;

    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

    @Column(name = "F_UpdateDatetime")
    protected Date updateDatetime;

    //@Generated(hash = 2040227860)
//    public PackageUnit(Long ID, String name, Date createDatetime, Date updateDatetime) {
//        this.ID = ID;
//        this.name = name;
//        this.createDatetime = createDatetime;
//        this.updateDatetime = updateDatetime;
//    }

    //@Generated(hash = 998786031)
//    public PackageUnit() {
//    }

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
    public List<BaseModel> parseN(JSONArray jsonArray) {
        System.out.println("正在执行PackageUnit的parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> packageUnitList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PackageUnit packageUnit = new PackageUnit();
                packageUnit.doParse1(jsonObject);
                packageUnitList.add(packageUnit);
            }
        } catch (Exception e) {
            System.out.println("PackageUnit.parseN异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return packageUnitList;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行PackageUnit.parse1，s=" + s);

        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (Exception e) {
            System.out.println("PackageUnit.parse1异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行PackageUnit.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            name = jo.getString(field.getFIELD_NAME_name());
            //
            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if(createDatetime == null){
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }
            //
            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if(updateDatetime == null){
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }

            return this;
        } catch (Exception e) {
            System.out.println("PackageUnit.doParse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public BaseModel clone() {
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(this.getID());
        packageUnit.setName(this.getName());
        if (this.getCreateDatetime() != null) {
            packageUnit.setCreateDatetime((Date) this.getCreateDatetime().clone());
        }
        if (this.getUpdateDatetime() != null) {
            packageUnit.setUpdateDatetime((Date) this.getUpdateDatetime().clone());
        }
        return packageUnit;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        PackageUnit pu = (PackageUnit) arg0;
        System.out.println("正在比较两个PackageUnit对象");
        if ((ignoreIDInComparision == true ? true : (pu.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))
                && pu.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())
                && DatetimeUtil.compareDate(pu.getCreateDatetime(), this.getCreateDatetime()) && printComparator(field.getFIELD_NAME_createDatetime())
                && DatetimeUtil.compareDate(pu.getUpdateDatetime(), this.getUpdateDatetime()) && printComparator(field.getFIELD_NAME_updateDatetime())
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError)//
                && name.length() > MAX_LENGTH_Name || !FieldFormat.checkName(name)) {
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

        if(printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) //
                && !FieldFormat.checkName(name) || name.length() > MAX_LENGTH_Name) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)) //
        {
            return "";
        }

        return sbError.toString();
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)) //
        {
            return "";
        }

        return sbError.toString();
    }
}
