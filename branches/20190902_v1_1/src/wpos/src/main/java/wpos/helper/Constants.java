package wpos.helper;

import org.apache.commons.logging.Log;
import wpos.bo.NtpHttpBO;
import wpos.model.BaseModel;
import wpos.model.Staff;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
    private Constants() {
    }

    public static final String SHADOW = "Kim.Lee@bxit.vip";

    /**
     * 目前只支持一个仓库
     */
    public static final int WAREHOUSE_ID_Default = 1;

    public static final int preSale_Role_ID = 6;

    public static final int cashier_Role_ID = 1;

    public static String BASE_URL;

    public static final String URL_FILE = "urlFile";

    public static final String DATE_FORMAT_Default = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_FORMAT_Default2 = "yyyy-MM-dd HH:mm:ss SSS";

    public static final String DATE_FORMAT_Default3 = "yyyy/MM/dd";

    public static final String DATE_FORMAT_Default4 = "yyyy-MM-dd";

    public static final String DATE_FORMAT_Default5 = "yyyyMMdd";

    public static final String DATE_FORMAT_Default6 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_Default7 = "MM/dd HH:mm:ss";

    public static final String TIME_FORMAT_ConfigGeneral = "HH:mm:ss";

    public static final String DATE_FORMAT_RetailTradeSN = "yyyyMMddHHmmss"; // 零售单SN中的日期格式

    public static final String VersionName = "1.0.0";

    public static final int VersionCode = 1; // 当前版本

    public static final String env = "SITaa";

    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT_Default, Locale.CHINA);
    }

    public static SimpleDateFormat getSimpleDateFormat2() {
        return new SimpleDateFormat(DATE_FORMAT_Default2, Locale.CHINA);
    }

    public static SimpleDateFormat getSimpleDateFormat3() {
        return new SimpleDateFormat(DATE_FORMAT_Default3, Locale.CHINA);
    }

    public static SimpleDateFormat getSimpleDateFormat4() {
        return new SimpleDateFormat(DATE_FORMAT_Default4, Locale.CHINA);
    }

    public static SimpleDateFormat getSimpleDateFormat5() {
        return new SimpleDateFormat(DATE_FORMAT_Default5, Locale.CHINA);
    }

    /*
    * 当一天接近结束时，系统用于对收银汇总进行上传的目标时间  TODO 待giggs Review时重命名
    * */
    public static final String START_TIME_UploadRetailTradeAggregation = "23:59:30";

    /*
    * 当一天接近结束时,系统弹出一个AlertDialog限制操作者不能进行任何操作，经过WAITING_TIME_NEAR_DAY_END倒计时后自动消失
    *在此时间内系统做上传零售汇总操作
    * START_TIME_UploadRetailTradeAggregation + WAITING_TIME_UploadRetailTradeAggregation 时间为第二天的时间
    * */
    public static final int WAITING_TIME_UploadRetailTradeAggregation = 60;
    /*
    * LOGIN_NotAllowed_TimeStart -- LOGIN_NotAllowed_TimeEnd 时间段内禁止用户登录
    * */
    public static final String LOGIN_NotAllowed_TimeStart = "23:59:00";
    public static final String LOGIN_NotAllowed_TimeEnd = "23:59:59";
    /**
     * TODO 待review
     */
    private static Staff currentStaff = new Staff();

    public static Staff getCurrentStaff() {
        return currentStaff;
    }

    public static void setCurrentStaff(Staff currentStaff) {
        Constants.currentStaff = currentStaff;
    }

    /**
     * 在测试代码中，为了模拟其它Pos做操作，会写本变量。测试完后恢复原来的值。
     * 如果测试过程中出现crash，本值将不被恢复，可能导致后续测试不通过
     */
//    public static String MyPosSN = AppApplication.getPOS_SN();
    public static String MyPosSN = ""; //TODO
    public static String MyCompanySN = "";

    /**
     * 微信支付使用的子商户号
     */
    public static String submchid;
    /**
     * 本机PosID，输入公司编号获得posID，用户登录成功后，设置于收银汇总中
     */
    public static int posID;

    public static int shopID;

    public static final class SPKey {
        public static final String KEY_BASE_URL = "EFAD44325E8408E4A7B24A1F9AB49556";
    }

    public static boolean isNetworkAvailable = true;//当网络发生变化时，值改变（网络可用为true，不可用为false）

    public static Date getDefaultSyncDatetime2() throws ParseException {
        return getSimpleDateFormat2().parse("1970-01-01 08:00:00 000");
    }

    public static Date getDefaultSyncDatetime() throws ParseException {
        return getSimpleDateFormat().parse("1970/01/01 08:00:00");
    }

    public static void checkModelLog(Log log, BaseModel bm, String errMsg) {
        StackTraceElement ste = new Exception().getStackTrace()[1];
        log.info(new SimpleDateFormat(DATE_FORMAT_Default6).format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) + "：" + ste.getClassName() + "." + ste.getMethodName() + "() " +
                bm.getClass().getName() + "字段验证错误信息：" + errMsg);
    }

}

