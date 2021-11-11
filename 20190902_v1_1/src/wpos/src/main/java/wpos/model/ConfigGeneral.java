package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Keep;
//import org.greenrobot.greendao.annotation.NotNull;
//import org.greenrobot.greendao.annotation.Column;
//import org.greenrobot.greendao.annotation.Transient;
//import org.json.JSONArray;
//import org.json.JSONObject;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_ConfigGeneral")
public class ConfigGeneral extends BaseModel {
    // 以下ID和DB里的F_ID必须相等
    public static final int PAGESIZE = 1;
    public static final int PurchasingTimeoutTaskScanStartTime = 2;
    public static final int PurchasingTimeoutTaskScanEndTime = 3;
    public static final int PurchasingTimeoutTaskFlag = 4;
    public static final int UsalableCommodityTaskScanStartTime = 5;
    public static final int UsalableCommodityTaskScanEndTime = 6;
    public static final int UsalableCommodityTaskFlag = 7;
    public static final int ShelfLifeTaskScanStartTime = 8;
    public static final int ShelfLifeTaskScanEndTime = 9;
    public static final int ShelfLifeTaskFlag = 10;
    public static final int MaxVicePackageUnit = 11;
    public static final int MaxProviderNOPerCommodity = 12;
    public static final int MaxPurchasingOrderCommodityNO = 13;
    public static final int CommodityLogoVolumeMax = 14;
    public static final int MaxWarehousingCommodityNO = 15;
    public static final int RetailTradeDailyReportSummaryTaskScanStartTime = 16;
    public static final int RetailTradeDailyReportSummaryTaskScanEndTime = 17;
    public static final int RetailTradeMonthlyReportSummaryTaskScanStartTime = 18;
    public static final int RetailTradeMonthlyReportSummaryTaskScanEndTime = 19;
    public static final int RetailTradeDailyReportByCategoryParentTaskScanStartTime = 20;
    public static final int RetailTradeDailyReportByCategoryParentTaskScanEndTime = 21;
    public static final int RetailTradeDailyReportByStaffTaskScanStartTime = 22;
    public static final int RetailTradeDailyReportByStaffTaskScanEndTime = 23;
    public static final int ACTIVE_SMALLSHEET_ID = 24;

    public static final String TIME_FORMAT_ConfigGeneral = "HH:mm:ss";

    public static final String FIELD_ERROR_value1 = "非法时间格式！正确的时间格式应为：" + TIME_FORMAT_ConfigGeneral;
    public static final String FIELD_ERROR_value2 = "非法正整数！";
    public static final String FIELD_ERROR_NoValue = "T_ConfigGeneral里不存在的配置项";

    /**
     * TODO 需要重构NBR端。
     */
    public static final String Config_SmallSheetNumber = "SmallSheetNumber";
    public static final String Config_SmallSheetNumber_Value = "10";
//    public static final String Config_SmallSheetLogoVolume = "SmallSheetLogoVolume";
//    public static final String Config_SmallSheetLogoVolume_Value = "163840";

    //@Transient
    public static final String HTTP_ConfigGeneral_RETRIEVEN = "configGeneral/retrieveNEx.bx?isRequestFromPos=1";//设置isRequestFromPos = 1返回的是pos需要的记录，其他pos用不到的就不返回
    //@Transient
    public static final String HTTP_ConfigGeneral_UPDATE = "configGeneral/updateEx.bx";
    public static final ConfigGeneralField field = new ConfigGeneralField();
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

    @Transient
    protected int isRequestFromPos;

//    @Keep
//    public ConfigGeneral(Long ID, //@NotNull String name, //@NotNull String value) {
//        this.ID = ID;
//        this.name = name;
//        this.value = value;
//    }

    //@Generated(hash = 133025456)
//    public ConfigGeneral() {
//    }

    //@Generated(hash = 1288131032)
//    public ConfigGeneral(Long ID, //@NotNull String name, //@NotNull String value, //@NotNull Date syncDatetime) {
//        this.ID = ID;
//        this.name = name;
//        this.value = value;
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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigGeneral{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", ID=" + ID +
                '}';
    }

    @Override
    public BaseModel clone() {
        ConfigGeneral obj = new ConfigGeneral();
        obj.setID(this.getID());
        obj.setName(this.getName());
        obj.setValue(this.getValue());

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        ConfigGeneral configGeneral = (ConfigGeneral) arg0;
        if ((ignoreIDInComparision == true ? true : (configGeneral.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))//
                && configGeneral.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name())//
                && configGeneral.getValue().equals(this.getValue()) && printComparator(field.getFIELD_NAME_value())//
        ) {
            return 0;
        }
        return -1;
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行ConfigGeneral的parseN，ja=" + (ja == null ? null : ja.toString()));


        List<BaseModel> configGeneralList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                ConfigGeneral configGeneral = new ConfigGeneral();
                if (configGeneral.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                configGeneralList.add(configGeneral);
            }
            return configGeneralList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ConfigGeneral.parseN()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行ConfigGeneral的parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ConfigGeneral.parse1()出错： " + e.getMessage());
            return null;
        }
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行ConfigGeneral的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            name = jo.getString(field.getFIELD_NAME_name());
            value = jo.getString(field.getFIELD_NAME_value());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ConfigGeneral.doParse1()出错：" + e.getMessage());
            return null;
        }
        return this;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        switch (Integer.valueOf(String.valueOf(ID))) {
            case PurchasingTimeoutTaskScanStartTime:
            case PurchasingTimeoutTaskScanEndTime:
            case UsalableCommodityTaskScanStartTime:
            case UsalableCommodityTaskScanEndTime:
            case RetailTradeDailyReportSummaryTaskScanStartTime:
            case RetailTradeDailyReportSummaryTaskScanEndTime:
            case RetailTradeMonthlyReportSummaryTaskScanStartTime:
            case RetailTradeMonthlyReportSummaryTaskScanEndTime:
            case RetailTradeDailyReportByCategoryParentTaskScanStartTime:
            case RetailTradeDailyReportByCategoryParentTaskScanEndTime:
            case RetailTradeDailyReportByStaffTaskScanStartTime:
            case RetailTradeDailyReportByStaffTaskScanEndTime:
            case ShelfLifeTaskScanStartTime:
            case ShelfLifeTaskScanEndTime:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_ConfigGeneral);
                try {
                    simpleDateFormat.parse(value);
                } catch (Exception e) {
                    return FIELD_ERROR_value1;
                }
                break;
            case PAGESIZE:
            case ACTIVE_SMALLSHEET_ID:
            case PurchasingTimeoutTaskFlag:
            case UsalableCommodityTaskFlag:
            case ShelfLifeTaskFlag:
            case MaxVicePackageUnit:
            case MaxProviderNOPerCommodity:
            case MaxPurchasingOrderCommodityNO:
            case CommodityLogoVolumeMax:
            case MaxWarehousingCommodityNO:
                try {
                    int v = Integer.parseInt(value);
                    if (v <= 0) {
                        return FIELD_ERROR_value2;
                    }
                } catch (Exception e) {
                    return FIELD_ERROR_value2;
                }
                break;
            default:
                return FIELD_ERROR_NoValue;
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
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions:
                int total = StringUtils.contain(sql, "%s");
                if (this.printCheckField(field.getFIELD_NAME_sql(), FIELD_ERROR_SQL, sbError) && total != conditions.length) {
                    return sbError.toString();
                }
                return "";
            default:
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        return "";
    }
}
