package wpos.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_ConfigCacheSize")
public class ConfigCacheSize extends BaseModel {
    public static final int Max_LengthValue = 20;
    public static final String FIELD_ERROR_value = "value不能为空并且长度必须小于等于" + Max_LengthValue + "位";

    //@Transient
    public static final String HTTP_ConfigCacheSize_RetrieveN = "configSync/retrieveNEx.bx";
    //传int1=1目前不知道有什么用，先注释掉
//    //@Transient
//    public static final String HTTP_ConfigCacheSize_RetrieveNC = "configSync/retrieveNEx.bx?int1=1";
    //@Transient
    public static final String HTTP_ConfigCacheSize_RetrieveNC = "configSync/retrieveNEx.bx";
    public static final ConfigCacheSizeField field = new ConfigCacheSizeField();

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    //@NotNull
    @Column(name = "F_Value")
    protected String value;

    //@Generated(hash = 1974301237)
//    public ConfigCacheSize(Long ID, //@NotNull String name, //@NotNull String value) {
//        this.ID = ID;
//        this.name = name;
//        this.value = value;
//    }

    //@Generated(hash = 347056162)
    public ConfigCacheSize() {
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigCacheSize{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", ID=" + ID +
                '}';
    }

    @Override
    public BaseModel clone() {
        ConfigCacheSize obj = new ConfigCacheSize();
        obj.setID(this.getID());
        obj.setName(new String(this.getName()));
        obj.setValue(new String(this.getValue()));

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        ConfigCacheSize configCacheSize = (ConfigCacheSize) arg0;
        if ((ignoreIDInComparision == true ? true : (configCacheSize.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))//
                && configCacheSize.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())//
                && configCacheSize.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel doParse1(JSONObject object) {
        System.out.println("执行ConfigCacheSize的doParse1,object=" + (object == null ? null : object.toString()));

        try {
            ID = Integer.valueOf(object.getString(field.getFIELD_NAME_ID()));
            name = object.getString(field.getFIELD_NAME_name());
            value = object.getString(field.getFIELD_NAME_value());
            return this;
        } catch (Exception e) {
            System.out.println("ConfigCacheSize.doParse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("执行ConfigCacheSize的parse1,s=" + s);

        ConfigCacheSize configCacheSize = new ConfigCacheSize();
        try {
            JSONObject joConfigCacheSize = JSONObject.parseObject(s);
            configCacheSize = (ConfigCacheSize) doParse1(joConfigCacheSize);
            if (configCacheSize == null) {
                return null;
            }
            return configCacheSize;
        } catch (JSONException e) {
            System.out.println("ConfigCacheSize.parse1出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("执行ConfigCacheSize的parseN,ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> configCacheSizeList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                ConfigCacheSize configCacheSize = new ConfigCacheSize();
                if (configCacheSize.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                configCacheSizeList.add(configCacheSize);
            }
            return configCacheSizeList;
        } catch (Exception e) {
            System.out.println("ConfigCacheSize.parseN出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)//
                && this.printCheckField(field.getFIELD_NAME_value(), FIELD_ERROR_value, sbError) && !StringUtils.isEmpty(value) && value.length() <= Max_LengthValue) {
            return "";
        }

        return sbError.toString();
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
