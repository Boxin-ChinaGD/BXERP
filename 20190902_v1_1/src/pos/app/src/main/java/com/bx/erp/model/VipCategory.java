package com.bx.erp.model;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(nameInDb = "vipCategory")
public class VipCategory extends BaseModel {
    public static final int MAX_LENGTH_Name = 30;
    public static final String FIELD_ERROR_name = "会员分类名称不合法并且会员分类长度为(0, " + MAX_LENGTH_Name + "]";

    @Transient
    public static final String HTTP_VIPCATEGORY_CreateC = "vipCategory/createEx.bx";
    @Transient
    public static final String HTTP_VIPCategoty_RETRIEVENC_PageIndex = "vipCategory/retrieveNEx.bx?pageIndex=";
    @Transient
    public static final String HTTP_VIPCategoty_RETRIEVENC_PageSize = "&pageSize=";
    @Transient
    public static final String HTTP_VIPCATEGORY_UpdateC = "vipCategory/updateEx.bx";
    public static final VipCategoryField field = new VipCategoryField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_Name")
    protected String name;

    @Property(nameInDb = "F_CreateDatetime")
    protected Date createDatetime;

    @Property(nameInDb = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;


    @Generated(hash = 1686397196)
    public VipCategory() {
    }


    @Generated(hash = 840353036)
    public VipCategory(Long ID, @NotNull String name, Date createDatetime, Date updateDatetime, String syncType, Date syncDatetime) {
        this.ID = ID;
        this.name = name;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.syncType = syncType;
        this.syncDatetime = syncDatetime;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
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

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 VipCategory.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            name = jo.getString(field.getFIELD_NAME_name());
            syncSequence = jo.getInt(field.getFIELD_NAME_syncSequence()); //...

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
            System.out.println("VipCategory.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 VipCategory.parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("VipCategory.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray jsonArray1) {
        System.out.println("正在执行 VipCategory.parseN，jsonArray1=" + (jsonArray1 == null ? null : jsonArray1.toString()));

        List<BaseModel> vipCategoryList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                VipCategory vc = new VipCategory();
                vc.doParse1(jsonObject1);
                vipCategoryList.add(vc);
            }
        } catch (Exception e) {
            System.out.println("VipCategory.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return vipCategoryList;
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        VipCategory vc = new VipCategory();
        vc.setID(this.getID());
        vc.setName(new String(this.getName()));
        vc.setReturnObject(this.getReturnObject());

        return vc;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        VipCategory c = (VipCategory) arg0;
        if ((ignoreIDInComparision == true ? true : (c.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && c.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
                ) {
            return 0;
        }
        return -1;
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
        switch (iUseCaseID) {
            default:
                if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipCategoryName(name)) {
                    return sbError.toString();
                }
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !FieldFormat.checkVipCategoryName(name)) {
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

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

}
