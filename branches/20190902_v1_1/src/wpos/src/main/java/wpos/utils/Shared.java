package wpos.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.bo.PosLoginHttpBO;
import wpos.bo.RetailTradeAggregationSQLiteBO;
import wpos.bo.RetailTradeSQLiteBO;
import wpos.bo.SmallSheetSQLiteBO;
import wpos.bo.StaffLoginHttpBO;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.StaffLoginHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.VipSQLiteEvent;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.http.sync.SyncThread;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.Commodity;
import wpos.model.CommodityType;
import wpos.model.ErrorInfo;
import wpos.model.Pos;
import wpos.model.PurchasingOrderCommodity;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeAggregation;
import wpos.model.RetailTradeCommodity;
import wpos.model.SmallSheetFrame;
import wpos.model.SmallSheetText;
import wpos.model.Staff;
import wpos.presenter.BarcodesPresenter;
import wpos.presenter.BasePresenter;
import wpos.presenter.CommodityPresenter;
import wpos.presenter.RetailTradeAggregationPresenter;
import wpos.presenter.RetailTradeCommodityPresenter;
import wpos.presenter.RetailTradePresenter;
import wpos.presenter.SmallSheetFramePresenter;
import wpos.presenter.SmallSheetTextPresenter;
//import wpos.view.stage.BaseStage;

//import junit.framework.Assert;

//import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 本类的所有函数都是用于测试的。绝对不能在功能代码中使用！
 */
public class Shared {
    private static final Log log =  LogFactory.getLog(Shared.class);
    /**
     * 单元测试中网络请求的超时值
     */
    public final static int UNIT_TEST_TimeOut = 50;

    /**
     * -1,查询数据表条数异常
     */
    public static int INVALID_CASE_TOTAL = -1;

    public final static int PAGE_SIZE_MAX = 100000000;
    public final static int SQLITE_ID_Infinite = 1000000000;
    private static CommodityPresenter commodityPresenter;
//    private static RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
//    private static BarcodesPresenter barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
//    private static SmallSheetFramePresenter smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//    private static SmallSheetTextPresenter smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();


//    private static RetailTradeHttpBO retailTradeHttpBO = GlobalController.getInstance().getRetailTradeHttpBO();
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = GlobalController.getInstance().getRetailTradeSQLiteEvent();
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = GlobalController.getInstance().getRetailTradeSQLiteBO();
//    private static SmallSheetSQLiteBO smallSheetSQLiteBO = GlobalController.getInstance().getSmallSheetSQLiteBO();
//    private static SmallSheetHttpBO smallSheetHttpBO = GlobalController.getInstance().getSmallSheetHttpBO();
//    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = GlobalController.getInstance().getSmallSheetSQLiteEvent();
//    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = GlobalController.getInstance().getRetailTradeAggregationSQLiteBO();
//    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = GlobalController.getInstance().getRetailTradeAggregationHttpBO();
//    private static BrandHttpBO brandHttpBO = GlobalController.getInstance().getBrandHttpBO();
//    private static BrandSQLiteEvent brandSQLiteEvent = GlobalController.getInstance().getBrandSQLiteEvent();
//    private static CommodityCategoryHttpBO commodityCategoryHttpBO = GlobalController.getInstance().getCommodityCategoryHttpBO();
//    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = GlobalController.getInstance().getCommodityCategorySQLiteEvent();
//    private static CommodityHttpBO commodityHttpBO = GlobalController.getInstance().getCommodityHttpBO();
//    private static CommoditySQLiteEvent commoditySQLiteEvent = GlobalController.getInstance().getCommoditySQLiteEvent();
//    private static ConfigGeneralHttpBO configGeneralHttpBO = GlobalController.getInstance().getConfigGeneralHttpBO();
//    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = GlobalController.getInstance().getConfigGeneralSQLiteEvent();
//    private static VipHttpBO vipHttpBO = GlobalController.getInstance().getVipHttpBO();
//    private static VipCategoryHttpBO vipCategoryHttpBO = GlobalController.getInstance().getVipCategoryHttpBO();
//    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = GlobalController.getInstance().getVipCategorySQLiteEvent();
//    private static PosHttpBO posHttpBO = GlobalController.getInstance().getPosHttpBO();
//    private static PosSQLiteEvent posSQLiteEvent = GlobalController.getInstance().getPosSQLiteEvent();
//    private static ConfigCacheSizeHttpBO configCacheSizeHttpBO = GlobalController.getInstance().getConfigCacheSizeHttpBO();
//    private static ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent = GlobalController.getInstance().getConfigCacheSizeSQLiteEvent();
//    private static PackageUnitHttpBO packageUnitHttpBO = GlobalController.getInstance().getPackageUnitHttpBO();
//    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = GlobalController.getInstance().getPackageUnitSQLiteEvent();
//    private static BarcodesHttpBO barcodesHttpBO = GlobalController.getInstance().getBarcodesHttpBO();
//    private static BarcodesSQLiteEvent barcodesSQLiteEvent = GlobalController.getInstance().getBarcodesSQLiteEvent();
//    private static VipHttpEvent vipHttpEvent = GlobalController.getInstance().getVipHttpEvent();
//    private static VipSQLiteEvent vipSQLiteEvent = GlobalController.getInstance().getVipSQLiteEvent();

    public static final String PASSWORD_DEFAULT = "000000";
    /**
     * OP帐号的手机号码
     */
    public static final String OP_Phone = "13185246281";
    /**
     * nbr公司的SN
     */
    public static final String nbr_CompanySN = "668866";
    public static final String SHADOW = "Kim.Lee@bxit.vip";

    public static final String PAGE_Size = "10";
    public static final String PAGE_Index = "1";
    public static final String PAGE_SIZE_DEFAULT_MAX = "50";

    /**
     * 用于判断是否上传完零售单
     */
//    public static boolean isDone = false;

    public static boolean is = true;

    public static void printTestClassStartInfo() {
        StackTraceElement ste = new Exception().getStackTrace()[1];
//        log.info(new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) + "\t\t开始运行测试类：" + ste.getClassName() + "...");
    }

    public static void printTestClassEndInfo() {
        StackTraceElement ste = new Exception().getStackTrace()[1];
//        log.info(new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) + "\t\t结束运行测试类：" + ste.getClassName() + "...");
    }

    public static void printTestMethodStartInfo() {
        StackTraceElement ste = new Exception().getStackTrace()[1];
//        log.info(new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference)) + "\t\t开始运行测试：" + ste.getClassName() + "." + ste.getMethodName() + "()...");
    }

    /**
     * 产生长度为length的字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 用于登录POS和STAFF
     *
     * @param posID
     * @throws
     */
//    public static boolean login(long posID) throws InterruptedException { //... 重载一个方法 需要传BO
//        BaseStage.posID = (int) posID;
//        Constants.MyCompanySN = "668866";
//        //1.pos登录
//        Pos pos = new Pos();
//        pos.setID(posID);
//        pos.setPasswordInPOS("000000");
//        PosLoginHttpBO pbo = GlobalController.getInstance().getPosLoginHttpBO();
//        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        pbo.setPos(pos);
//        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
//        pbo.loginAsync();
//        //
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
//            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
//        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("pos登录超时!");
//            return false;
//        }
//
//        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//            //2.staff登录
//            Staff staff = new Staff();
//            staff.setPhone("15854320895");
//            staff.setPasswordInPOS("000000");
//            StaffLoginHttpBO sbo = GlobalController.getInstance().getStaffLoginHttpBO();
//            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//            sbo.setStaff(staff);
//            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
//            sbo.loginAsync();
//            //
//            lTimeOut = UNIT_TEST_TimeOut;
//            while (sbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
//                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
//                    break;
//                }
//                Thread.sleep(1000);
//            }
//            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//                log.info("STAFF登录超时!");
//                return false;
//            }
//        } else {
//            log.info("pos登录失败！");
//            return false;
//        }
//        return true;
//    }

    /**
     * 用于登录POS和STAFF
     *
     * @param posID
     * @throws
     */
    public static boolean login(int posID, PosLoginHttpBO posLoginHttpBO, StaffLoginHttpBO staffLoginHttpBO) throws InterruptedException { //... 重载一个方法 需要传BO
        Constants.posID = (int) posID;
        Constants.MyCompanySN = "668866";
        //1.pos登录
        Pos pos = new Pos();
        pos.setID(posID);
        pos.setPasswordInPOS("000000");
        PosLoginHttpBO pbo = posLoginHttpBO;
        // 如果登录超时，尝试多一次（跑testunit.xml会出现登录超时）
        int tryMoreTime = 1;
        do {
            long lTimeOut = UNIT_TEST_TimeOut;
            pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            pbo.setPos(pos);
            pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
            pbo.loginAsync();
            //
            while (lTimeOut-- > 0) {
                if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
        } while(tryMoreTime-- > 0 && pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done);
        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            log.info("pos登录超时!" + pbo.getHttpEvent().getStatus() + "\t" + pbo.getHttpEvent().getRequestType());
            return false;
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
            Staff staff = new Staff();
            staff.setPhone("15854320895");
            staff.setPasswordInPOS("000000");
            StaffLoginHttpBO sbo = staffLoginHttpBO;
            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            sbo.setStaff(staff);
            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
            sbo.loginAsync();
            //
            long lTimeOut = UNIT_TEST_TimeOut;
            while (lTimeOut-- > 0) {
                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                log.info("STAFF登录超时!" + sbo.getHttpEvent().getStatus() + "\t" + sbo.getHttpEvent().getRequestType());
                return false;
            }
        } else {
//            log.info("pos登录失败！");
            return false;
        }
        return true;
    }


    /**
     * 用于登录POS和STAFF
     *
     * @param posID
     * @param bx    当bx不为0时，登录时call的是bxStaffAction
     * @throws
     */
//    public static boolean login(long posID, String username, String password, int bx, String nbr_CompanySN) throws InterruptedException {
//        BaseStage.posID = (int) posID;
//        Constants.MyCompanySN = nbr_CompanySN;
//        //1.pos登录
//        Pos pos = new Pos();
//        pos.setID(posID);
//        pos.setPasswordInPOS("000000");
//        PosLoginHttpBO pbo = GlobalController.getInstance().getPosLoginHttpBO();
//        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        pbo.setPos(pos);
//        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
//        pbo.loginAsync();
//        //
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
//            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
//        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("pos登录超时!");
//            return false;
//        }
//
//        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//            //2.staff登录
//            StaffLoginHttpBO staffLoginHttpBO = null;
//            staffLoginHttpBO = GlobalController.getInstance().getStaffLoginHttpBO();
//            staffLoginHttpBO.setPwdEncrypted(null);
//            staffLoginHttpBO.setStaff(null);
//            StaffLoginHttpEvent staffLoginHttpEvent = null;
//            staffLoginHttpEvent = GlobalController.getInstance().getStaffLoginHttpEvent();
//            staffLoginHttpEvent.setRequestType(null);
//            staffLoginHttpEvent.setEventProcessed(false);
//
//            Staff staff = new Staff();
//            staff.setPhone(username);
//            staff.setPasswordInPOS(password);
//            staffLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//            //staff的退回登录logout不能判断int3了
////            staff.setInt3(1);//在退出的时候用到。当int3 = 1时，说明是POS机退出，需要发送消息给Boss
//            staff.setIsBXStaff(bx);//...
//            staffLoginHttpBO.setStaff(staff);
//            staffLoginHttpBO.loginAsync();
//            //
//            lTimeOut = 30;
//            while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
//                if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
//                    break;
//                }
//                Thread.sleep(1000);
//            }
//            if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//                log.info("STAFF登录超时!");
//                return false;
//            }
//        } else {
//            log.info("pos登录失败！");
//        }
//
//        return true;
//    }
    public static boolean login(int posID, String username, String password, int bx, String companySN, PosLoginHttpBO posLoginHttpBO, StaffLoginHttpBO staffLoginHttpBO, int isLoginFromPos) throws InterruptedException {
        Constants.posID = (int) posID;
        Constants.MyCompanySN = companySN;
        //1.pos登录
        Pos pos = new Pos();
        pos.setID(posID);
        pos.setPasswordInPOS("000000");
        // 如果登录超时，尝试多一次（跑testunit.xml会出现登录超时）
        int tryMoreTime = 1;
        do {
            posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            posLoginHttpBO.setPos(pos);
            posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
            posLoginHttpBO.loginAsync();
            //
            long lTimeOut = UNIT_TEST_TimeOut;
            while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
                if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
        } while(tryMoreTime-- > 0 && posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done);
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            log.info("pos登录超时!");
            return false;
        }

        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
            staffLoginHttpBO.setPwdEncrypted(null);
            staffLoginHttpBO.setStaff(null);
            StaffLoginHttpEvent staffLoginHttpEvent = (StaffLoginHttpEvent) staffLoginHttpBO.getHttpEvent();
            staffLoginHttpEvent.setRequestType(null);
            staffLoginHttpEvent.setEventProcessed(false);

            Staff staff = new Staff();
            staff.setPhone(username);
            staff.setPasswordInPOS(password);
            staffLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            //现在Staff的退出登录logout没有用到int3了，只需要在session中查看时候是否有Pos信息
//            staff.setInt3(1);//在退出的时候用到。当int3 = 1时，说明是POS机退出，需要发送消息给Boss
            staff.setIsBXStaff(bx);
            staff.setIsLoginFromPos(isLoginFromPos);
            staffLoginHttpBO.setStaff(staff);
            staffLoginHttpBO.loginAsync();
            //
            long lTimeOut = UNIT_TEST_TimeOut;
            while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
                if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                log.info("STAFF登录超时!");
                return false;
            }
        } else {
            log.info("pos登录失败！");
        }

        return true;
    }

//    public static boolean login(Pos p, String nbr_CompanySN) throws InterruptedException {
//        Constants.MyCompanySN = nbr_CompanySN;
//        //1.pos登录
//        BaseStage.posID = p.getID().intValue();
//        Pos pos = new Pos();
//        pos.setID(p.getID());
//        pos.setResetPasswordInPos(p.getResetPasswordInPos());
//        pos.setPasswordInPOS(p.getPasswordInPOS());
//        PosLoginHttpBO pbo = GlobalController.getInstance().getPosLoginHttpBO();
//        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        pbo.setPos(pos);
//        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
//        pbo.loginAsync();
//        //
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
//            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
//                break;
//            }
//            Thread.sleep(1000);
//        }
//        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("pos登录超时!");
//            return false;
//        }
//
//        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//            //2.staff登录
//            Staff staff = new Staff();
//            staff.setPhone("15854320895");
//            staff.setPasswordInPOS("000000");
//            StaffLoginHttpBO sbo = GlobalController.getInstance().getStaffLoginHttpBO();
//            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//            sbo.setStaff(staff);
//            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
//            sbo.loginAsync();
//            //
//            lTimeOut = UNIT_TEST_TimeOut;
//            while (sbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
//                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
//                    break;
//                }
//                Thread.sleep(1000);
//            }
//            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//                log.info("STAFF登录超时!");
//                return false;
//            }
//        } else {
//            log.info("pos登录失败！");
//            return false;
//        }
//        return true;
//    }

    // ...GlobalStage的对应BO和EVENT解除联系后重载
    public static boolean login(Pos p, String companySN, PosLoginHttpBO posLoginHttpBO, StaffLoginHttpBO staffLoginHttpBO) throws InterruptedException {
        Constants.MyCompanySN = companySN;
        //1.pos登录
        Constants.posID = p.getID();
        Pos pos = new Pos();
        pos.setID(p.getID());
        pos.setResetPasswordInPos(p.getResetPasswordInPos());
        pos.setPasswordInPOS(p.getPasswordInPOS());
        PosLoginHttpBO pbo = posLoginHttpBO;
        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        pbo.setPos(pos);
        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        pbo.loginAsync();
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("pos登录超时!");
            return false;
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
            Staff staff = new Staff();
            staff.setPhone("15854320895");
            staff.setPasswordInPOS("000000");
            StaffLoginHttpBO sbo = staffLoginHttpBO;
            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            sbo.setStaff(staff);
            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
            sbo.loginAsync();
            //
            lTimeOut = UNIT_TEST_TimeOut;
            while (sbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//                log.info("STAFF登录超时!");
                return false;
            }
        } else {
//            log.info("pos登录失败！");
            return false;
        }
        return true;
    }

    /**
     * 随机选取几个可能收银的商品，包括组合商品、单品、多包装商品，不包括已经删除的商品
     *
     * @param maxNO 最大的商品数目
     * @return
     */
//    public static List<Commodity> getCommodityList(int maxNO, boolean forPurchasingOnly, StringBuilder sbError) {
//        assert maxNO > 0;
//        Random r = new Random();
//        int commodityNO = r.nextInt(maxNO);
//        if (commodityNO <= 0) {
//            commodityNO = 1;
//        }
//
//        Map<String, Commodity> map = new HashMap<String, Commodity>();  //key=F_ID, value=Commodity
//        Commodity commodity = new Commodity();
//        for (int i = 0; i < commodityNO; i++) {
//            //...在本地SQLite中读取商品。R1，传递商品ID。
//            commodity.setID(Long.valueOf(r.nextInt(100) + 1));
//            Commodity commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
//            if (commodity1 == null || commodity1.getStatus() == Commodity.EnumStatusCommodity.ESC_Deleted.getIndex()) { //防止拿到已经删除的商品或SQLite错误
//                i -= 1;
//                continue;
//            }
//            if (forPurchasingOnly) { //采购商品只能是单品
//                while (true) {//直到找到单品为止
//                    if (commodity1 != null && (commodity1.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex())) {
//                        map.put(String.valueOf(commodity1.getID()), commodity1);
//                        break;
//                    } else {
//                        commodity.setID(Long.valueOf(r.nextInt(100) + 1));
//                        commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
//                    }
//                }
//            } else if (commodity1 != null) {
//                map.put(String.valueOf(commodity1.getID()), commodity1);//这里的商品可能是单品、多包装商品、组合商品、服务型商品
//            }
//            //... 如果只获取一个商品，然后商品ID不存在的情况下该如何处理
//        }
//
//        return new ArrayList<BaseModel>(map.values());
//    }

    /**
     * 购买商品随机
     *
     * @param listComm 采购的商品的列表
     * @param maxNO    采购一个商品的最大数量
     */
//    public static List<PurchasingOrderCommodity> getPurchasingOrderCommodity(List<Commodity> listComm, int maxNO, StringBuilder sbError) {
//        Random r = new Random();
//        List<PurchasingOrderCommodity> purchasingOrderCommodityList = new ArrayList<PurchasingOrderCommodity>();
//        StringBuilder totalPrices = new StringBuilder();  //记录本次采购中每个商品的零售价
//        Barcodes barcodes = new Barcodes();
//
//        for (int i = 0; i < listComm.size(); i++) {
//            int commodityNO = r.nextInt(maxNO);
//            if (commodityNO <= 0) {
//                commodityNO = 1;
//            }
//            Commodity commodity = listComm.get(i);
//            PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
//            purchasingOrderCommodity.setCommodityID(commodity.getID().intValue());
//            purchasingOrderCommodity.setCommodityName(commodity.getName());
//            purchasingOrderCommodity.setCommodityNO(commodityNO);
//            purchasingOrderCommodity.setPriceSuggestion(commodity.getPriceRetail());
//
//            barcodes.setSql("where F_CommodityID = ?");
//            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
//            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
//            if (barcodesList == null || barcodesList.size() <= 0) {
//                sbError.append("未能查找到barcodeID");
//
//                return null;
//            }
//            purchasingOrderCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());
//            totalPrices.append(commodity.getPriceRetail() + ",");
//
//            purchasingOrderCommodityList.add(purchasingOrderCommodity);
//        }
//        purchasingOrderCommodityList.get(0).setTotalPrices(totalPrices.toString());
//
//        return purchasingOrderCommodityList;
//    }

    /**
     * 购买商品随机
     *
     * @param listComm 购买的商品的列表
     * @param maxNO    购买一个商品的最大数量
     */
//    public static List<RetailTradeCommodity> getRetailTradeCommodityList(List<Commodity> listComm, int maxNO, RetailTrade retailTrade, StringBuilder sbError) throws ParseException {
//        //...
//        Random r = new Random();
//        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
//        int totalPrices = 0; //记录这批零售单商品的总价格
//
//        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//        if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            sbError.append("零售单商品表查找本地最大ID失败！");
//
//            return null;
//        }
//        for (int i = 0; i < listComm.size(); i++) {
//            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
//            int commodityNO = r.nextInt(maxNO);
//            if (commodityNO <= 0) {
//                commodityNO = 1;
//            }
//            retailTradeCommodity.setID(maxTextIDInSQLite + i + 1);
//            retailTradeCommodity.setPriceOriginal((double) listComm.get(i).getPriceRetail());
//            retailTradeCommodity.setNOCanReturn(commodityNO);
//            retailTradeCommodity.setCommodityID(listComm.get(i).getID().intValue());
//            retailTradeCommodity.setNO(commodityNO);
//            retailTradeCommodity.setDiscount(1);
//            retailTradeCommodity.setPriceSpecialOffer(0);
//            retailTradeCommodity.setPriceVIPOriginal(0);
//            retailTradeCommodity.setBarcodeID(1);
//            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//            retailTradeCommodity.setTradeID(retailTrade.getID());
//            retailTradeCommodity.setPriceReturn(1); //...
//
//            totalPrices += commodityNO * listComm.get(i).getPriceRetail();
//            retailTradeCommodityList.add(retailTradeCommodity);
//        }
//        //
//        retailTradeCommodityList.get(0).setAmountWeChat(totalPrices);//把这批零售单商品的总价格返回出去
//
//        return retailTradeCommodityList;
//    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的SmallSheet的临时数据
     *
     * @return
     */
    public static List<SmallSheetFrame> retrieveNSmallSheetTempDataInSQLite() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        SmallSheetFramePresenter smallSheetFramePresenter = (SmallSheetFramePresenter) context.getBean("smallSheetFramePresenter");
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload, smallSheetFrame);//找到主表List
        return frameList;
    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的RetailTradeAggregation的临时数据
     */
//    public static List<RetailTradeAggregation> retrieveNRetailTradeAggregationTempDataInSQLite() {
//        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//        List<RetailTradeAggregation> retailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
//        retailTradeAggregation.setSql("where F_SyncDatetime = ?");
//        retailTradeAggregation.setConditions(new String[]{"0"});
//        retailTradeAggregationList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation);
//        return retailTradeAggregationList;
//    }

    /**
     * 上传SQlite中RetailTrade的临时数据
     */
//    public static void uploadRetailTradeTempData(RetailTrade retailTrade, RetailTradeSQLiteBO retailTradeSQLiteBO) {
//        //创建零售单int1代表返回对象
//        retailTrade.setReturnObject(1);
//        retailTrade.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//        retailTrade.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//        if (BasePresenter.SYNC_Type_C.equals(retailTrade.getSyncType())) {
//            retailTradeSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(retailTrade);
//            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
//            if (!retailTradeSQLiteBO.getHttpEvent().getHttpBO().createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
//                Assert.assertTrue("上传失败！", false);
//            }
//        }
//    }

    /**
     * 上传SQLite中SmallSheet的临时数据
     */
    public static void uploadSmallSheetTempData(SmallSheetFrame smallSheetFrame, SmallSheetSQLiteBO smallSheetSQLiteBO) {
        //创建小票格式int1代表返回对象
        smallSheetFrame.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(smallSheetFrame);
            if (!smallSheetSQLiteBO.getHttpEvent().getHttpBO().createAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Assert.assertTrue("启动时上传小票格式创建失败！", false);
            }
        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
            if (!smallSheetSQLiteBO.getHttpEvent().getHttpBO().updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Assert.assertTrue("启动时上传小票格式修改失败！", false);
            }
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
            if (!smallSheetSQLiteBO.getHttpEvent().getHttpBO().deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Assert.assertTrue("启动时上传小票格式删除失败！", false);
            }
        }
    }

    /**
     * 上传SQLite中RetailTradeAggregation的临时数据
     */
    public static void uploadRetailTradeAggregationTempData(RetailTradeAggregation retailTradeAggregation, RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO) {
        retailTradeAggregation.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(retailTradeAggregation.getSyncType())) {
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setBaseModel1(retailTradeAggregation);
            if (!retailTradeAggregationSQLiteBO.getHttpEvent().getHttpBO().createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
//                Assert.assertTrue("上传收银汇总失败！", false);
            }
        }
    }

    /**
     * 获取售出的商品.此商品包含了折扣，折后单价，小计，商品数量等
     */
    public static List<Commodity> getSellCommodityList(List<Commodity> commodities, int iNO) {
        if (commodities != null) {
            Random random = new Random();
            List<Commodity> commodityList = new ArrayList<Commodity>();
            for (int i = 0; i < commodities.size(); i++) {
                Commodity commodity = commodities.get(i);
                commodity.setAfter_discount(0.000000d);
                int no = random.nextInt(iNO) + 1;
                commodity.setCommodityQuantity(no);
                commodity.setDiscount(0.000000d);
                commodity.setSubtotal(no * commodity.getPriceRetail());
                commodityList.add(commodity);
            }

            return commodityList;
        }
        return null;
    }

    /**
     * 等待pos和staff登录后的数据同步完成。
     * 数据同步中，VIP的同步是最后一个，所以以其结束为准。
     *
     * @throws
     */
//    public static void waitSyncDone(VipSQLiteEvent vipSQLiteEvent) throws InterruptedException {//...同步是否成功？
//        long lTimeout = UNIT_TEST_TimeOut;
//
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {//...变量命名和注释
//            Thread.sleep(1000);
//        }
//        //...处理超时
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            log.info("vipHttpEvent.getStatus(): " + vipSQLiteEvent.getHttpBO().getHttpEvent().getStatus() + "   vipSQLiteEvent.getStatus(): " + vipSQLiteEvent.getStatus());
//            Assert.assertTrue("请求超时！", false);
//        }
////        if (vipHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done || vipSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//        vipSQLiteEvent.getHttpBO().getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
////        }
//    }

    /**
     * Staff\Pos\Company的salt盐值目前需要32个数字或大写字母的组合，使用UUID进行生成
     */
    public static String getFakedSalt() {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().replace("-", "");
        return key.toUpperCase();
    }

    /**
     * 生成一个合法的身份证，以320开头，共11位
     */
    public static String getValidICID() throws InterruptedException {
        String str = "%04d";
        String ICID = String.format(str, System.currentTimeMillis() % 10000);
        Thread.sleep(1);
        return "32080319970701" + ICID;
    }

    /**
     * 生成一个合法的店员名字最大长度为12
     */
    public static String getStaffName() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 12; i++) {
            int number = random.nextInt(26);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 生成一个由字母组成的字符串，长度为length
     */
    public static String generateCompanyName(int length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str = str + (char) (Math.abs(new Random().nextInt()) % 26 + 'a');
        }
        return str;
    }

    public static void caseLog(String s) {
        System.out.println("\n---------------------------------------" + s + "-----------------------------------------------");
    }

    /** 生成随机DB名称。它必须是以字母或下划线开头，和JAVA的变量命名一样 */
    public static String GenerateDBName() {
        String dbName = "nbr_test_" + UUID.randomUUID().toString().substring(1, 7);
        System.out.println("生成的DB name为：" + dbName);
        return dbName;
    }

    /** 生成一个合法的手机号码，以13开头，共11位 */
    public static String getValidStaffPhone() throws InterruptedException {
        String str = "%09d";
        String phone = String.format(str, System.currentTimeMillis() % 1000000000);
        Thread.sleep(1);
        return "13" + phone;
    }


    /**
     * 从服务器下载某个类型model的所有数据
     */
    public static String retrieveNCBaseModel(BaseModel bm, BaseHttpBO baseHttpBO, BaseSQLiteEvent baseSQLiteEvent, BaseSQLiteBO baseSQLiteBO) throws Exception {
        String bmName = bm.getClass().getSimpleName();
        String message = "正在下载" + bmName + "中，请稍后...\n";
        log.info(message);

        int runTimes = 1;
        do {
            baseSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            if (!baseHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, bm)) {
//                Assert.assertTrue("下载" + bmName + "失败，", false);
                return "下载" + bmName + "失败，";
            }
            long lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (baseSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (baseSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                Assert.assertTrue("下载" + bmName + "超时,当前SqlEvent状态为：" + baseSQLiteEvent.getStatus(), false);
                return "下载" + bmName + "超时,当前SqlEvent状态为：" + baseSQLiteEvent.getStatus();
            }
            if (baseSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                System.out.println("错误码为: " + baseSQLiteEvent.getLastErrorCode());
//                Assert.assertTrue("错误：" + bmName + "下载失败，失败原因：" + baseHttpBO.getHttpEvent().getLastErrorMessage() + "\n", false);
                return "错误：" + bmName + "下载失败，失败原因：" + baseHttpBO.getHttpEvent().getLastErrorMessage() + "\n";
            }
            //
            if (baseHttpBO.getHttpEvent().getCount() != null && !"".equals(baseHttpBO.getHttpEvent().getCount())) {
                int count = Integer.parseInt(baseHttpBO.getHttpEvent().getCount());
                int totalPageIndex = count % Integer.parseInt(bm.getPageSize()) != 0 ? count / Integer.parseInt(bm.getPageSize()) + 1 : count / Integer.parseInt(bm.getPageSize());//查询条形码需要totalPageIndex页才能查完

                if (runTimes < totalPageIndex) {
                    bm.setPageIndex(String.valueOf(++runTimes));
                    // 判断是否为Commodity并且是页数中的最后一页。(为了在present中删除并更新掉z最后一页的所有数据)
                    if (bm instanceof Commodity && bm.getPageIndex().equals(String.valueOf(totalPageIndex))) {
                        baseSQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_END);
                    }else if (bm instanceof  Barcodes && bm.getPageIndex().equals(String.valueOf(totalPageIndex))){
                        baseSQLiteBO.getSqLiteEvent().setPageIndex(Barcodes.PAGEINDEX_END);
                    }
//                    xxxxxx
                } else {
                    break;
                }
            } else {
                break;
            }
        } while (true);
        message = "已完成对" + bmName + "的下载...\n";
        log.info(message);
        return "";
    }


    /**
     * 随机选取几个可能收银的商品，包括组合商品、单品、多包装商品，不包括已经删除的商品
     *
     * @param maxNO 最大的商品数目
     * @return
     */
    public static List<Commodity> getCommodityList(int maxNO, boolean forPurchasingOnly, StringBuilder sbError) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        commodityPresenter = (CommodityPresenter) context.getBean("commodityPresenter");
        assert maxNO > 0;
        Random r = new Random();
        int commodityNO = r.nextInt(maxNO);
        if (commodityNO <= 0) {
            commodityNO = 1;
        }

        Map<String, Commodity> map = new HashMap<>();  //key=F_ID, value=Commodity
        Commodity commodity = new Commodity();
        for (int i = 0; i < commodityNO; i++) {
            //...在本地SQLite中读取商品。R1，传递商品ID。
            commodity.setID(r.nextInt(100) + 1);
            Commodity commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            if (commodity1 == null || commodity1.getStatus() == Commodity.EnumStatusCommodity.ESC_Deleted.getIndex()) { //防止拿到已经删除的商品或SQLite错误
                i -= 1;
                continue;
            }
            if (forPurchasingOnly) { //采购商品只能是单品
                while (true) {//直到找到单品为止
                    if (commodity1 != null && (commodity1.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex())) {
                        map.put(String.valueOf(commodity1.getID()), commodity1);
                        break;
                    } else {
                        commodity.setID(r.nextInt(100) + 1);
                        commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                    }
                }
            } else if (commodity1 != null) {
                map.put(String.valueOf(commodity1.getID()), commodity1);//这里的商品可能是单品、多包装商品、组合商品、服务型商品
            }
            //... 如果只获取一个商品，然后商品ID不存在的情况下该如何处理
        }

        return new ArrayList<>(map.values());
    }
}
