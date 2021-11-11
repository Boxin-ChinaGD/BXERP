package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;


@Entity(nameInDb = "T_BXConfigGeneral")
public class BXConfigGeneral extends BaseModel {
    public static final int CompanyBusinessLicensePictureDir = 1;//必须与DB中的配置表一致
    public static final int MaxRequestIncrementPercent = 12;

    public static final int MIN_BxConfigGeneralID = CompanyBusinessLicensePictureDir;
    public static final int MAX_BxConfigGeneralID = MaxRequestIncrementPercent;
    public static final String FIELD_ERROR_BxConfigGeneralID = "公共配置表的ID只能在" + MIN_BxConfigGeneralID + "到" + MAX_BxConfigGeneralID + "之间";

    public static final String Config_SmallSheetNumber = "SmallSheetNumber";
    public static final String Config_SmallSheetNumber_Value = "10";
    public static final BxConfigGeneralField field = new BxConfigGeneralField();
    @Transient
    public static final String HTTP_ConfigGeneral_RETRIEVEN = "bxConfigGeneral/retrieveNEx.bx?isRequestFromPos=1";//设置isRequestFromPos= 1返回的是pos需要的记录，其他pos用不到的就不返回

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @NotNull
    @Property(nameInDb = "F_Name")
    protected String name;

    @NotNull
    @Property(nameInDb = "F_Value")
    protected String value;

    @NotNull
    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Generated(hash = 2094879613)
    public BXConfigGeneral(Long ID, @NotNull String name, @NotNull String value, @NotNull Date syncDatetime) {
        this.ID = ID;
        this.name = name;
        this.value = value;
        this.syncDatetime = syncDatetime;
    }

    @Generated(hash = 1876212846)
    public BXConfigGeneral() {
    }

    @Override
    public BaseModel clone() {
        BXConfigGeneral obj = new BXConfigGeneral();
        obj.setID(this.getID());
        obj.setName(this.getName());
        obj.setValue(this.getValue());
        obj.setSyncDatetime((Date) this.getSyncDatetime().clone());

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        BXConfigGeneral bxConfigGeneral = (BXConfigGeneral) arg0;
        if ((ignoreIDInComparision == true ? true : (bxConfigGeneral.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))//
                && bxConfigGeneral.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())//
                && bxConfigGeneral.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行BXConfigGeneral的parseN，ja=" + (ja == null ? null : ja.toString()));


        List<BaseModel> bxConfigGeneralList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                BXConfigGeneral bxConfigGeneral = new BXConfigGeneral();
                if (bxConfigGeneral.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                bxConfigGeneralList.add(bxConfigGeneral);
            }
            return bxConfigGeneralList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BXConfigGeneral.parseN()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行BXConfigGeneral的parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BXConfigGeneral.parse1()出错： " + e.getMessage());
            return null;
        }
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行BXConfigGeneral的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            name = jo.getString(field.getFIELD_NAME_name());
            value = jo.getString(field.getFIELD_NAME_value());
            //
            String tmpSyncDatetime = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmpSyncDatetime)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmpSyncDatetime);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BXConfigGeneral.doParse1()出错：" + e.getMessage());
            return null;
        }
        return this;
    }

    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String checkCreate(int iUseCaseID){
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_BxConfigGeneralID, sbError) && FieldFormat.checkID(ID) && MAX_BxConfigGeneralID >= ID) //
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
        return "";
    }
}
