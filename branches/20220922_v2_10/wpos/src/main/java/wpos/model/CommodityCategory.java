package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity()
@Table(name = "T_CommodityCategory")
public class CommodityCategory extends BaseModel {
    public static final String ACTION_ERROR_UpdateDelete1 = "默认的分类不能修改或删除！";
    public static final String FIELD_ERROR_name = "小类名字为中英文的组合，长度1到10";
    public static final String FIELD_ERROR_parentID = "parentID必须大于0";
    public static final String FIELD_ERROR_checkUniqueField = "非法的值";
    public static final int MAX_LENGTH_CategoryName = 10;
    public static final int MIN_LENGTH_CategoryName = 0;

    //@Transient
    public static final String HTTP_CATEGORY_Create = "categorySync/createEx.bx";
    //@Transient
    public static final String feedbackURL_FRONT = "categorySync/feedbackEx.bx?sID=";
    //@Transient
    public static final String feedbackURL_BEHINE = "&errorCode=EC_NoError";
    //@Transient
    public static final String HTTP_CATEGORY_RetrieveN = "categorySync/retrieveNEx.bx";
    //@Transient
    public static final String HTTP_Categoty_RETRIEVENC_PageIndex = "category/retrieveAllEx.bx?pageIndex=";
    //@Transient
    public static final String HTTP_Categoty_RETRIEVENC_PageSize = "&pageSize=";
    //@Transient
    public static final String HTTP_Categoty_RETRIEVENC_TypeStatus = "&type=-1&status=-1";
    //@Transient
    public static final String HTTP_CATEGORY_Update = "categorySync/updateEx.bx";

    public static final CommodityCategoryField field = new CommodityCategoryField();

    @Id()
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    //@NotNull
    @Column(name = "F_ParentID")
    protected int parentID;

    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

    @Column(name = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Column(name = "F_SyncType")
    protected String syncType;

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

    //@Generated(hash = 307946986)
    public CommodityCategory() {
    }


    //@Generated(hash = 1606679643)
//    public CommodityCategory(Long ID, //@NotNull String name, int parentID, Date createDatetime, Date updateDatetime, String syncType,
//                             Date syncDatetime) {
//        this.ID = ID;
//        this.name = name;
//        this.parentID = parentID;
//        this.createDatetime = createDatetime;
//        this.updateDatetime = updateDatetime;
//        this.syncType = syncType;
//        this.syncDatetime = syncDatetime;
//    }

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

    public int getParentID() {
        return this.parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行CommodotyCategory的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            name = jo.getString(field.getFIELD_NAME_name());
            parentID = Integer.valueOf(jo.getString(field.getFIELD_NAME_parentID()));
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());

            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }

            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }

            syncType = jo.getString(field.getFIELD_NAME_syncType());
            return this;
        } catch (Exception e) {
            System.out.println("执行doParse1异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行CommodotyCategory的parse1，s=" + s);

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
        System.out.println("正在执行CommodotyCategory的parseN，jsonArray1=" + (jsonArray1 == null ? null : jsonArray1));

        List<BaseModel> categoryList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray1.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                CommodityCategory c = new CommodityCategory();
                c.doParse1(jsonObject1);
                categoryList.add(c);
            }
        } catch (Exception e) {
            System.out.println("执行parseN异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return categoryList;
    }


    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        CommodityCategory c = new CommodityCategory();
        c.setID(this.getID());
        c.setName(new String(this.getName()));
        c.setParentID(this.getParentID());
        c.setReturnObject(this.getReturnObject());

        return c;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        CommodityCategory c = (CommodityCategory) arg0;
        if ((ignoreIDInComparision == true ? true : (c.getID() == (this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && c.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
                && c.getParentID() == this.getParentID() && printComparator(field.getFIELD_NAME_parentID()) //
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "CommodityCategory{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", parentID=" + parentID +
                '}';
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

    public String getSyncType() {
        return this.syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }


    public Date getSyncDatetime() {
        return this.syncDatetime;
    }


    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryName && name.length() <= MAX_LENGTH_CategoryName))//
        {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_parentID(), FIELD_ERROR_parentID, sbError) && !FieldFormat.checkID(parentID)) {
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

        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCategoryName(name) && name.length() > MIN_LENGTH_CategoryName && name.length() <= MAX_LENGTH_CategoryName))//
        {
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_parentID(), FIELD_ERROR_parentID, sbError) && !FieldFormat.checkID(parentID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
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

        switch (iUseCaseID) {
            default:
                if (!StringUtils.isEmpty(name)) {
                    if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(name.length() <= MAX_LENGTH_CategoryName)) {
                        return sbError.toString();
                    }
                }
                break;
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }
        return "";
    }
}
