package com.test.bx.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions;
import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_NoError;

public class RetailTradeJUnit extends BaseHttpAndroidTestCase {
    private static CommodityPresenter commodityPresenter = null;

    private static BarcodesPresenter barcodesPresenter = null;

    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter rtcPresenter = null;

    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;

    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteEvent commoditySQliteEvent = null;
    //
    private static NtpHttpEvent ntpHttpEvent = null;
    private static NtpHttpBO ntpHttpBO = null;

    private static final int EVENT_ID_RetailTradeJUnit = 10000;
    /**
     * 用来覆盖用例：创建本地零售单成功，上传服务器失败
     * 将来POS端对VIPID有限制后可能需要另外的处理。SQLite对零售单的VIPID没有限制，但是服务器对VIPID有主外键约束。
     */
    private int MaxVIPID = 99999999;
    private int currentIndex = 1;
    private List<RetailTrade> rnRetailTradeList = new ArrayList<>();
    private RetailTrade rtCondition = new RetailTrade();
    private int totalPageIndex = 100000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite) throws Exception {
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(1));
            retailTrade.setLocalSN((int) lRetailTradeMaxIDInSQLite);
            retailTrade.setPos_ID(1);
            retailTrade.setLogo("11");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setStaffID(4);//测试数据默认用店员4号登陆。若用其它staff会导致compareTo不通过
            retailTrade.setPaymentType(1);
            retailTrade.setPaymentAccount("0");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2d);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setReturnObject(1);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(25);
            retailTradeCommodity.setCommodityID(21);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(20);
            retailTradeCommodity.setNOCanReturn(20);
            retailTradeCommodity.setPriceVIPOriginal(1);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(22);
            retailTradeCommodity2.setBarcodeID(26);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(20);
            retailTradeCommodity2.setNOCanReturn(20);
            retailTradeCommodity2.setPriceVIPOriginal(1);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (commodityPresenter == null) {
            commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        }
        if (barcodesPresenter == null) {
            barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        }
        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (rtcPresenter == null) {
            rtcPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }

        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        //
        if (commoditySQliteEvent == null) {
            commoditySQliteEvent = new CommoditySQLiteEvent();
            commoditySQliteEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQliteEvent, commodityHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQliteEvent, commodityHttpEvent);
        }
        commoditySQliteEvent.setHttpBO(commodityHttpBO);
        commoditySQliteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        //
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(EVENT_ID_RetailTradeJUnit);
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        }
        ntpHttpEvent.setHttpBO(ntpHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            System.out.println("#########################################################RetailTradeJUnit onRetailTradeSQLiteEvent");
            event.onEvent();
            //并不需要重置所有的零售单。不然重置所有零售单则该测试会耗费大量时间
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                rnRetailTradeList.addAll((Collection<? extends RetailTrade>) event.getListMasterTable());
                currentIndex = totalPageIndex;
//                int count = Integer.parseInt(retailTradeHttpEvent.getString1());
//                totalPageIndex = count % Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) != 0 ? count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default) + 1 : count / Integer.valueOf(BaseHttpBO.PAGE_SIZE_Default);
//                if (currentIndex < totalPageIndex) {
//                    rtCondition.setPageIndex(String.valueOf(++currentIndex));
//                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
//                    if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
//                        Assert.assertTrue("查询旧零售单失败！", false);
//                    }
//                }
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvnet(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }

    }

    @Test
    public void test_a_Create1() throws Exception {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        Barcodes barcodes = new Barcodes();
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        Shared.retrieveNCBaseModel(barcodes, barcodesHttpBO, barcodesSQLiteEvent, barcodesSQLiteBO);
        //清空表，以免造成干扰。有需要可以反注释
//        rtcPresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);
//        retailTradePresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);

//        List<RetailTrade> retailTrades2 = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(Constants.INVALID_CASE_ID, null);

        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("创建失败！", false);
        }

        //等待处理完毕
        long lTimeout = 60;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象为空！", master != null);

        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        ssfOld.setIgnoreIDInComparision(true);
        System.out.println("ssfOld:" + ssfOld);
        System.out.println("master:" + master);
        Assert.assertTrue("服务器返回的对象和上传的对象不相等！", ssfOld.compareTo(master) == 0);

        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld);
        Assert.assertTrue("没有删除掉旧单", rt == null);

        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue("退出登录失败！", false);
        }
        lTimeout = 50;
        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("退出登录超时！", false);
        }
    }

    @Test
    public void test_a_Create2() throws Exception {
        //异常case：传到服务器的支付金额为负数
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        ssfOld.setAmount(-10);
        //
        retailTradeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        retailTradeHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, ssfOld);
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                retailTradeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("创建异常零售单超时！", lTimeout-- > 0);
        Assert.assertTrue("错误码不正确！", retailTradeHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b_CreateN() throws Exception {
        Shared.printTestMethodStartInfo();

        //清空表，以免造成干扰。有需要可以反注释
//        rtcPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        //插入俩条临时数据到SQLite中。
        List<BaseModel> retailTradeOldList = new ArrayList<BaseModel>();
        List<BaseModel> retailTradeOldList2 = new ArrayList<BaseModel>();
        for (int i = 1; i < 3; i++) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
            long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);

            RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite + i, maxRetailTradeCommodityIDInSQLite + i);
            RetailTrade retailTrade = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld);
            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) GlobalController.getInstance().getRetailTradeCommodityPresenter().createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld.getListSlave1());
            retailTrade.setListSlave1(retailTradeCommodities);

            retailTradeOldList.add(retailTrade);
            retailTradeOldList2.add(retailTrade);
        }
        //上传临时数据
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("同步请求失败！", false);
        }
        long lTimeout = 10000;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = ?");
        rt.setConditions(new String[]{"0"});
        rt.setQueryKeyword(RetailTrade.generateRetailTradeSN(Constants.posID));
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        Assert.assertTrue("上传完临时的零售单后，SQLite中还残余有临时的零售单，List为" + rtList, rtList.size() == 0);

        //结果验证(查看服务器返回的数据是否存在SQLite。查看临时数据是否被删除)
        List<RetailTrade> retailTrades = (List<RetailTrade>) retailTradeSQLiteBO.getSqLiteEvent().getListMasterTable();
        List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
        //
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == EC_NoError);
        for (int i = 0; i < retailTradeOldList2.size(); i++) {
            for (int j = 0; j < retailTradeList.size(); j++) {
                if (retailTradeList.get(j).getID() == retailTradeOldList2.get(i).getID()) {
                    Assert.assertTrue("旧数据并未删除成功！", false);
                }
            }
        }
        //
        boolean is = false;
        for (int i = 0; i < retailTrades.size(); i++) {
            for (int j = 0; j < retailTradeList.size(); j++) {
                if (retailTradeList.get(j).getID() == retailTrades.get(i).getID()) {
                    is = true;
                    break;
                }
            }
            Assert.assertTrue("新数据并没成功插入！", is);
            is = false;
        }

//        if (!logoutHttpBO.logoutAsync()) {
//            Assert.assertTrue("退出登录失败！", false);
//        }
//        lTimeout = 50;
//        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
//                logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
//                logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
//            Assert.assertTrue("退出登录超时！", false);
//        }
    }

    /**
     * 购买的商品只有多包装商品
     * 1.拿到内部类的RetailTrade
     * 2.在本地SQLite中找到多包装商品，清空RetailTrade中的商品，加入本地的多包装商品
     * 3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
     * 4.自己计算出商品单价和零售单的总价应为多少
     * 5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
     */
    @Test
    public void test_c_calculateMultiPackagingRetailPrice() throws Exception {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        // 重置商品数据
        Commodity resetCommodity = new Commodity();
        resetCommodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        resetCommodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);// ...
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);//...
        Shared.retrieveNCBaseModel(resetCommodity, commodityHttpBO, commoditySQliteEvent, commoditySQLiteBO);
        //1.拿到内部类的retailTrade
        System.out.println("----------1.拿到内部类的retailTrade---------");
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //2.在本地SQLite中找到多包装商品，将RetailTrade里面的商品改为多包装商品
        System.out.println("-------------2.在本地SQLite中找到多包装商品，将RetailTrade里面的商品改为多包装商品-------------");
        retailTrade.setListSlave1(null);
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Type = ?");
        commodity.setConditions(new String[]{"2"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地多包装商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找多包装商品失败！", commodityList.size() > 0);
        //
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        for (int i = 0; i < 2; i++) {
            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
            retailTradeCommodity.setCommodityID(commodityList.get(i).getID().intValue());
            //根据商品ID找到对应的barcodes
            retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList.get(i).getID()));
            retailTradeCommodityList.add(retailTradeCommodity);
        }

        //3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
        System.out.println("-------------------3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价-----------------");
        double retailTradeAmount = 0.000000d;
        for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
            Commodity commodity1 = new Commodity();
            commodity1.setID((long) retailTradeCommodity.getCommodityID());
            commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
            if (commodity1.getType() == 2) {
                retailTradeCommodity.setPriceOriginal(calculateMultiPackagingRetailPrice(commodity1));
            }
            retailTradeAmount = retailTradeCommodity.getPriceOriginal() * retailTradeCommodity.getNO();
        }
        retailTrade.setAmount(retailTradeAmount);

        //4.自己计算出商品单价和零售单的总价应为多少
        System.out.println("----------------------4.自己计算出商品单价和零售单的总价应为多少---------------------");
        for (Commodity commodity1 : commodityList) {
            if (commodity1.getType() == 2) {
                Commodity c = new Commodity();
                c.setID((long) commodity1.getRefCommodityID());
                //
                Commodity c2 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
                commodity1.setPriceRetail(c2.getPriceRetail() * commodity1.getRefCommodityMultiple());
            }
        }

        //5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
        System.out.println("----------------5.比较RetailTrade的总价和商品单价与上一步骤的是否一样----------------");
        double amount = 0.000000d;
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            //总价
            amount = commodityList.get(i).getPriceRetail() * retailTradeCommodityList.get(i).getNO();
            //商品单价
            BigDecimal b1 = new BigDecimal(retailTradeCommodityList.get(i).getPriceOriginal());
            BigDecimal b2 = new BigDecimal(commodityList.get(i).getPriceRetail());
            Assert.assertTrue("单价比较失败！", b1.equals(b2));
        }
        BigDecimal b1 = new BigDecimal(retailTradeAmount);
        BigDecimal b2 = new BigDecimal(amount);
        Assert.assertTrue("总价比较失败！", b1.equals(b2));
    }

    /**
     * 购买商品中有单品和多包装商品
     * 1.拿到内部类的RetailTrade
     * 2.在本地SQLite中找到多包装商品和单品，清空RetailTrade中的商品，加入本地的多包装商品和单品
     * 3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
     * 4.自己计算出商品单价和零售单的总价应为多少
     * 5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
     */
    public void test_d_calculateMultiPackagingRetailPrice() throws Exception {
        //1.拿到内部类的retailTrade
        System.out.println("----------1.拿到内部类的retailTrade---------");
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //2.在本地SQLite中找到多包装商品和单品，清空RetailTrade中的商品，加入本地的多包装商品和单品
        System.out.println("-------------2.在本地SQLite中找到多包装商品和单品，清空RetailTrade中的商品，加入本地的多包装商品和单品-------------");
        retailTrade.setListSlave1(null);
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Type = ?");
        commodity.setConditions(new String[]{"2"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList1 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地多包装商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找多包装商品失败！", commodityList1.size() > 0);
        //
        commodity.setConditions(new String[]{"1"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList2 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地单品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找单品失败！", commodityList2.size() > 0);
        //
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList1.get(0).getID().intValue());
        //根据商品ID找到对应的barcodesID
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList1.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList2.get(0).getID().intValue());
        //
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList2.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        List<Commodity> commodityList = new ArrayList<Commodity>();
        commodityList.add(commodityList1.get(0));
        commodityList.add(commodityList2.get(0));

        //3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
        System.out.println("--------------3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价---------------");
        double retailTradeAmount = 0.000000d;
        for (RetailTradeCommodity retailTradeCommodity1 : retailTradeCommodityList) {
            Commodity commodity1 = new Commodity();
            commodity1.setID((long) retailTradeCommodity1.getCommodityID());
            commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
            if (commodity1.getType() == 2) {
                retailTradeCommodity1.setPriceOriginal(calculateMultiPackagingRetailPrice(commodity1));
            } else {
                retailTradeCommodity1.setPriceOriginal(commodity1.getPriceRetail());
            }
            retailTradeAmount = retailTradeCommodity1.getPriceOriginal() * retailTradeCommodity1.getNO();
        }
        retailTrade.setAmount(retailTradeAmount);

        //4.自己计算出商品单价和零售单的总价应为多少
        System.out.println("--------------------4.自己计算出商品单价和零售单的总价应为多少----------------");
        for (Commodity commodity1 : commodityList) {
            if (commodity1.getType() == 2) {
                Commodity c = new Commodity();
                c.setID((long) commodity1.getRefCommodityID());
                //
                Commodity c2 = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, c);
                commodity1.setPriceRetail(c2.getPriceRetail() * commodity1.getRefCommodityMultiple());
            }
        }

        //5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
        System.out.println("-------------------5.比较RetailTrade的总价和商品单价与上一步骤的是否一样-------------------");
        double amount = 0.000000d;
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            //总价
            amount = commodityList.get(i).getPriceRetail() * retailTradeCommodityList.get(i).getNO();
            //商品单价
            Assert.assertTrue("单价比较失败！", retailTradeCommodityList.get(i).getPriceOriginal() - commodityList.get(i).getPriceRetail() < BaseModel.TOLERANCE);
        }
        BigDecimal b1 = new BigDecimal(retailTradeAmount);
        BigDecimal b2 = new BigDecimal(amount);
        Assert.assertTrue("总价比较失败！", b1.equals(b2));
    }

    /**
     * 购买商品中有组合商品和多包装商品
     * 1.拿到内部类的RetailTrade
     * 2.在本地SQLite中找到多包装商品和组合商品，清空RetailTrade中的商品，加入本地的多包装商品和组合商品
     * 3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
     * 4.自己计算出商品单价和零售单的总价应为多少
     * 5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
     */
    public void test_e_calculateMultiPackagingRetailPrice() throws Exception {
        //1.拿到内部类的retailTrade
        System.out.println("----------1.拿到内部类的retailTrade---------");
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //2.在本地SQLite中找到多包装商品和单品，清空RetailTrade中的商品，加入本地的多包装商品和单品
        System.out.println("-------------2.在本地SQLite中找到多包装商品和单品，清空RetailTrade中的商品，加入本地的多包装商品和单品-------------");
        retailTrade.setListSlave1(null);
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Type = ?");
        commodity.setConditions(new String[]{"2"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList1 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地多包装商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找多包装商品失败！", commodityList1.size() > 0);
        //
        commodity.setConditions(new String[]{"1"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList2 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地组合商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找组合商品失败！", commodityList2.size() > 0);
        //
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList1.get(0).getID().intValue());
        //根据商品ID找到对应的barcodesID
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList1.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList2.get(0).getID().intValue());
        //
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList2.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        List<Commodity> commodityList = new ArrayList<Commodity>();
        commodityList.add(commodityList1.get(0));
        commodityList.add(commodityList2.get(0));

        //3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
        System.out.println("------------------3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价--------------------");
        double retailTradeAmount = 0.000000d;
        for (RetailTradeCommodity retailTradeCommodity1 : retailTradeCommodityList) {
            Commodity commodity1 = new Commodity();
            commodity1.setID((long) retailTradeCommodity1.getCommodityID());
            commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
            if (commodity1.getType() == 2) {
                retailTradeCommodity1.setPriceOriginal(calculateMultiPackagingRetailPrice(commodity1));
            } else {
                retailTradeCommodity1.setPriceOriginal(commodity1.getPriceRetail());
            }
            retailTradeAmount = retailTradeCommodity1.getPriceOriginal() * retailTradeCommodity1.getNO();
        }
        retailTrade.setAmount(retailTradeAmount);

        //4.自己计算出商品单价和零售单的总价应为多少
        System.out.println("---------------------4.自己计算出商品单价和零售单的总价应为多少--------------------");
        for (Commodity commodity1 : commodityList) {
            if (commodity1.getType() == 2) {
                Commodity c = new Commodity();
                c.setID((long) commodity1.getRefCommodityID());
                //
                Commodity c2 = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, c);
                commodity1.setPriceRetail(c2.getPriceRetail() * commodity1.getRefCommodityMultiple());
            }
        }

        //5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
        System.out.println("-------------------5.比较RetailTrade的总价和商品单价与上一步骤的是否一样--------------------");
        double amount = 0.000000d;
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            //总价
            amount = commodityList.get(i).getPriceRetail() * retailTradeCommodityList.get(i).getNO();
            //商品单价
            Assert.assertTrue("单价比较失败！", retailTradeCommodityList.get(i).getPriceOriginal() - commodityList.get(i).getPriceRetail() < BaseModel.TOLERANCE);
        }
        BigDecimal b1 = new BigDecimal(retailTradeAmount);
        BigDecimal b2 = new BigDecimal(amount);
        Assert.assertTrue("总价比较失败！", b1.equals(b2));
    }

    /**
     * 购买商品中有组合商品、多包装商品和单品
     * 1.拿到内部类的RetailTrade
     * 2.在本地SQLite中找到组合商品、多包装商品和单品，清空RetailTrade中的商品，加入本地的组合商品、多包装商品和单品
     * 3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
     * 4.自己计算出商品单价和零售单的总价应为多少
     * 5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
     */
    public void test_f_calculateMultiPackagingRetailPrice() throws Exception {
        //1.拿到内部类的retailTrade
        System.out.println("----------1.拿到内部类的retailTrade---------");
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //2.在本地SQLite中找到组合商品、多包装商品和单品，清空RetailTrade中的商品，加入本地的组合商品、多包装商品和单品
        System.out.println("-------------2.在本地SQLite中找到组合商品、多包装商品和单品，将RetailTrade里面的商品改为组合商品、多包装商品和单品-------------");
        retailTrade.setListSlave1(null);
        Commodity commodity = new Commodity();
        commodity.setSql("where F_Type = ?");
        commodity.setConditions(new String[]{"2"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList1 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地多包装商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找多包装商品失败！", commodityList1.size() > 0);
        //
        commodity.setConditions(new String[]{"1"});
        commodity.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Commodity> commodityList2 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地组合商品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找组合商品失败！", commodityList2.size() > 0);
        //
        commodity.setConditions(new String[]{"1"});
        List<Commodity> commodityList3 = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, commodity);
        Assert.assertTrue("查找本地单品失败！", commodityPresenter.getLastErrorCode() == EC_NoError);
        Assert.assertTrue("本地查找单品失败！", commodityList3.size() > 0);
        //
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList1.get(0).getID().intValue());
        //根据商品ID找到对应的barcodesID
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList1.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        retailTradeCommodity = new RetailTradeCommodity();
        initRetailTradeCommodity(maxRetailTradeIDInSQLite, retailTradeCommodity);
        retailTradeCommodity.setCommodityID(commodityList2.get(0).getID().intValue());
        //
        retailTradeCommodity.setBarcodeID((int) retrieveBarcodeIDByCommodityID(commodityList2.get(0).getID()));
        retailTradeCommodityList.add(retailTradeCommodity);
        //
        List<Commodity> commodityList = new ArrayList<Commodity>();
        commodityList.add(commodityList1.get(0));
        commodityList.add(commodityList2.get(0));
        commodityList.add(commodityList3.get(0));

        //3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价
        System.out.println("---------------3.判断是否为多包装商品,如果为多包装需要重新计算单价和RetailTrade总价-------------");
        double retailTradeAmount = 0.000000d;
        for (RetailTradeCommodity retailTradeCommodity1 : retailTradeCommodityList) {
            Commodity commodity1 = new Commodity();
            commodity1.setID((long) retailTradeCommodity1.getCommodityID());
            commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity1);
            if (commodity1.getType() == 2) {
                retailTradeCommodity1.setPriceOriginal(calculateMultiPackagingRetailPrice(commodity1));
            } else {
                retailTradeCommodity1.setPriceOriginal(commodity1.getPriceRetail());
            }
            retailTradeAmount = retailTradeCommodity1.getPriceOriginal() * retailTradeCommodity1.getNO();
        }
        retailTrade.setAmount(retailTradeAmount);

        //4.自己计算出商品单价和零售单的总价应为多少
        System.out.println("------------------4.自己计算出商品单价和零售单的总价应为多少-----------------");
        for (Commodity commodity1 : commodityList) {
            if (commodity1.getType() == 2) {
                Commodity c = new Commodity();
                c.setID((long) commodity1.getRefCommodityID());
                //
                Commodity c2 = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, c);
                commodity1.setPriceRetail(c2.getPriceRetail() * commodity1.getRefCommodityMultiple());
            }
        }

        //5.比较RetailTrade的总价和商品单价与上一步骤的是否一样
        System.out.println("-----------------5.比较RetailTrade的总价和商品单价与上一步骤的是否一样----------------");
        double amount = 0.000000d;
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            //总价
            amount = commodityList.get(i).getPriceRetail() * retailTradeCommodityList.get(i).getNO();
            //商品单价
            Assert.assertTrue("单价比较失败！", retailTradeCommodityList.get(i).getPriceOriginal() - commodityList.get(i).getPriceRetail() < BaseModel.TOLERANCE);
        }
        BigDecimal b1 = new BigDecimal(retailTradeAmount);
        BigDecimal b2 = new BigDecimal(amount);
        Assert.assertTrue("总价比较失败！", b1.equals(b2));
    }

    private void initRetailTradeCommodity(long lRetailTradeMaxIDInSQLite, RetailTradeCommodity retailTradeCommodity) throws ParseException {
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
        retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite);
        retailTradeCommodity.setNO(2);
        retailTradeCommodity.setPriceOriginal(222.6);
        retailTradeCommodity.setDiscount(1);
        retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
    }

    private double calculateMultiPackagingRetailPrice(Commodity commodity) {
        //1.根据多包装商品的ref_commodityID找到单品
        Commodity c = new Commodity();
        c.setID((long) commodity.getRefCommodityID());
        //
        Commodity c2 = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, c);

        //2.得到单品的单价，多包装的单价为单品的单价乘多包装商品commodity的refCommodityMultiple（倍数）的值。
        return c2.getPriceRetail() * commodity.getRefCommodityMultiple();
    }

    private long retrieveBarcodeIDByCommodityID(long ID) {
        Barcodes barcodes = new Barcodes();
        barcodes.setSql("where F_CommodityID = ?");
        barcodes.setConditions(new String[]{String.valueOf(ID)});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
        return barcodesList.get(0).getID();
    }

    @Test
    public void test_g_returnCommRetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建零售单，确保能够接下来能够查询出至少一张有效的零售单
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //
        createRetailTradeAsync(ssfOld);

        //查询零售单A
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        retailTrade.setQueryKeyword(ssfOld.getSn());
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器下载RetailTrde超时!", false);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("下载零售单失败!", false);
        }
        //服务器返回的数据和sqlite里的数据对比,
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("查找本地零售单失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //
        List<RetailTrade> list = (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("服务器返回的数据空的不足以接下来的测试", list != null && list.size() > 0);
        if (list.size() != rtList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
        //对零售单A进行退货操作并检查是否成功
        RetailTrade retailTradeA = list.get(0);
        List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) retailTradeA.getListSlave1();
        List<RetailTradeCommodity> returnCommList = new ArrayList<RetailTradeCommodity>();//用于记录退货前的零售单商品
        double totalMoney = 0.000000d; //退货商品的总价格
        long rtcMaxID = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败", rtcPresenter.getLastErrorCode() == EC_NoError);
        for (int i = 0; i < retailTradeCommodities.size(); i++) {
            RetailTradeCommodity rtc = retailTradeCommodities.get(i);
            returnCommList.add((RetailTradeCommodity) rtc.clone());
            rtc.setID(rtcMaxID + i);
            rtc.setNO(rtc.getNOCanReturn() / 2);
            totalMoney += (rtc.getPriceReturn() * rtc.getNO());
//            returnCommList.add(rtc);
        }
//        retailTradeA.setListSlave1(returnCommList);
        retailTradeA.setAmount(GeneralUtil.round(totalMoney, 2));
        retailTradeA.setAmountCash(GeneralUtil.round(totalMoney, 2));
        retailTradeA.setSourceID(Integer.valueOf(String.valueOf(retailTradeA.getID())));
        long returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        retailTradeA.setID(returnCommRetailTradeID);
        retailTradeA.setLocalSN((int) returnCommRetailTradeID);
        retailTradeA.setPos_ID(Constants.posID);
        retailTradeA.setSaleDatetime(new Date());
        //对零售单A进行退货
        createRetailTradeAsync(retailTradeA);

        //对零售单A进行检查。检查可退货数量是否正确的改变。
        retailTradeA.setID(Long.valueOf(retailTradeA.getSourceID()));
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeA);
        Assert.assertTrue("查询1条零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError && rt != null);
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
        for (int i = 0; i < retailTradeCommodityList.size(); i++) {
            RetailTradeCommodity rtc = retailTradeCommodityList.get(i);
            for (int j = 0; j < returnCommList.size(); j++) {
                if (rtc.getCommodityID() == returnCommList.get(j).getCommodityID()) {
                    for (int k = 0; k < retailTradeA.getListSlave1().size(); k++) {
                        RetailTradeCommodity thRtc = (RetailTradeCommodity) retailTradeA.getListSlave1().get(k);
                        if (thRtc.getCommodityID() == rtc.getCommodityID()) {
                            Assert.assertTrue("原先零售单A经过退货后，零售单A的可退货数量没有发生改变！", (rtc.getNOCanReturn() == returnCommList.get(j).getNOCanReturn() - thRtc.getNO()));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void test_g_returnCommRetailTrade2() throws Exception {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        // 重复生成两张相同源单ID的退货单到林是零售单中，返回错误。
        syncTime(EVENT_ID_RetailTradeJUnit);
        //创建零售单，确保能够接下来能够查询出至少一张有效的零售单
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //
        createRetailTradeAsync(ssfOld);

        //查询零售单A
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        retailTrade.setQueryKeyword(ssfOld.getSn());
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器下载RetailTrde超时!", false);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("下载零售单失败!", false);
        }
        //服务器返回的数据和sqlite里的数据对比,
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("查找本地零售单失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //
        List<RetailTrade> list = (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("服务器返回的数据空的不足以接下来的测试", list != null && list.size() > 0);
        if (list.size() != rtList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }

        // 关闭网络
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        // 关闭wifi，使网络不可用
        wifiManager.setWifiEnabled(false);
        long lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));

        //对零售单A进行退货操作并检查是否成功
        RetailTrade retailTradeA = list.get(0);
        List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) retailTradeA.getListSlave1();
        List<RetailTradeCommodity> returnCommList = new ArrayList<RetailTradeCommodity>();//用于记录退货前的零售单商品
        double totalMoney = 0.000000d; //退货商品的总价格
        long rtcMaxID = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败", rtcPresenter.getLastErrorCode() == EC_NoError);
        for (int i = 0; i < retailTradeCommodities.size(); i++) {
            RetailTradeCommodity rtc = retailTradeCommodities.get(i);
            returnCommList.add((RetailTradeCommodity) rtc.clone());
            rtc.setID(rtcMaxID + i);
            rtc.setNO(1);
            totalMoney += (rtc.getPriceReturn() * rtc.getNO());
//            returnCommList.add(rtc);
        }
        //对零售单A进行退货，插入临时零售单
        long returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeA1 = DataInput.getRetailTrade(returnCommRetailTradeID, maxRetailTradeCommodityIDInSQLite);
        ;
        retailTradeA1.setAmount(totalMoney);
        retailTradeA1.setAmountCash(totalMoney);
        retailTradeA1.setSourceID(Integer.valueOf(String.valueOf(retailTradeA.getID())));
        retailTradeA1.setLocalSN((int) returnCommRetailTradeID);
        retailTradeA1.setPos_ID(Constants.posID);
        retailTradeA1.setSaleDatetime(new Date());
        retailTradeA1.setListSlave1(retailTradeA.getListSlave1());
        long rtcMaxIDA1 = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败", rtcPresenter.getLastErrorCode() == EC_NoError);
        for (int i = 0; i < retailTradeA1.getListSlave1().size(); i++) {
            RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeA1.getListSlave1().get(i);
            rtc.setID(rtcMaxIDA1 + i);
        }
        //
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeA1)) {
            Assert.assertTrue("创建失败！", false);
        }
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("请求服务器下载RetailTrde超时!", false);
        }


        //重复对零售单A进行退货，插入临时零售单
        returnCommRetailTradeID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeA2 = DataInput.getRetailTrade(returnCommRetailTradeID, maxRetailTradeCommodityIDInSQLite);
        retailTradeA2.setAmount(totalMoney);
        retailTradeA2.setAmountCash(totalMoney);
        retailTradeA2.setSourceID(Integer.valueOf(String.valueOf(retailTradeA.getID())));
        retailTradeA2.setLocalSN((int) returnCommRetailTradeID);
        retailTradeA2.setPos_ID(Constants.posID);
        retailTradeA2.setSaleDatetime(new Date());
        retailTradeA2.setListSlave1(retailTradeA.getListSlave1());
        long rtcMaxIDA2 = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败", rtcPresenter.getLastErrorCode() == EC_NoError);
        for (int i = 0; i < retailTradeA2.getListSlave1().size(); i++) {
            RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeA2.getListSlave1().get(i);
            rtc.setID(rtcMaxIDA2 + i);
        }
        //
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeA2)) {
            Assert.assertTrue("创建失败！", false);
        }
        lTimeout = 1000;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("请求服务器下载RetailTrde超时!", false);
        }
        //在activity中会有限制，禁止重复对一张单多次退货，但是在测试中却无法限制成功
        Assert.assertTrue("重复插入相同源单ID的退货单错误码错误", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == EC_NoError);
        //删除重复退货的单，避免线程上传临时零售单时，服务器不接收该单，导致出现错误
        RetailTrade returnRT = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, returnRT);
        Assert.assertTrue("删除重复的退货单失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        // 打开wifi
        wifiManager.setWifiEnabled(true);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
    }

    private void createRetailTradeAsync(RetailTrade retailTrade) throws InterruptedException {
        retailTrade.setDatetimeStart(new Date());
        retailTrade.setDatetimeEnd(new Date());

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue("创建失败！", false);
        }
        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("创建零售单失败！原因：超时", false);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("创建零售单失败！原因：错误码不正确", false);
        }
        //
        RetailTrade serviceReturnRetailTrade = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象为空！", serviceReturnRetailTrade != null);
        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致
        retailTrade.setIgnoreIDInComparision(true);
        retailTrade.setIgnoreSNInComparision(true);
        Assert.assertTrue("服务器返回的对象和上传的对象不相等！", retailTrade.compareTo(serviceReturnRetailTrade) == 0);
    }

    /**
     * 查询所有的零售单
     *
     * @throws InterruptedException
     */
    @Test
    public void test_h_retrieveNRetailTrade() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        // 正常case
        rtCondition.setQueryKeyword("");
        rtCondition.setDatetimeStart(Constants.getDefaultSyncDatetime());
        rtCondition.setDatetimeEnd(new Date());
        rtCondition.setPageIndex(String.valueOf(currentIndex));
        rtCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
            Assert.assertTrue("查询旧零售单失败！！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (currentIndex < totalPageIndex && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("查询旧零售单超时！", lTimeOut > 0);
        Assert.assertTrue("应该有零售单返回", rnRetailTradeList.size() > 0);

    }

    @Test
    public void test_h_retrieveNRetailTrade2() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //异常Case:datetimeStart datetimeEnd为null
        rtCondition.setQueryKeyword("");
        rtCondition.setDatetimeStart(null);
        rtCondition.setDatetimeEnd(null);
        rtCondition.setPageIndex(String.valueOf(currentIndex));
        rtCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
            Assert.assertTrue("查询旧零售单失败！！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (currentIndex < totalPageIndex && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("查询旧零售单超时！", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rnRetailTradeList = (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("应该有零售单返回", rnRetailTradeList.size() > 0);

    }

    @Test
    public void test_h_retrieveNRetailTrade3() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //异常Case:单号不完整（长度小于10）
        rtCondition.setQueryKeyword("222");
        rtCondition.setDatetimeStart(Constants.getDefaultSyncDatetime());
        rtCondition.setDatetimeEnd(new Date());
        rtCondition.setPageIndex(String.valueOf(currentIndex));
        rtCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
            Assert.assertTrue("查询旧零售单成功，预期失败: check()应不通过", true);
        } else {
            Assert.assertTrue("查询旧零售单失败，达到预期！check()应不通过", false);
        }
    }

    @Test
    public void test_h_retrieveNRetailTrade4() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //异常Case:单号不完整（长度大于20）
        rtCondition.setQueryKeyword("222222222222222222222222222222");
        rtCondition.setDatetimeStart(Constants.getDefaultSyncDatetime());
        rtCondition.setDatetimeEnd(new Date());
        rtCondition.setPageIndex(String.valueOf(currentIndex));
        rtCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
            Assert.assertTrue("查询旧零售单成功，预期失败: check()应不通过", true);
        } else {
            Assert.assertTrue("查询旧零售单失败，达到预期！check()应不通过", false);
        }
    }

    @Test
    public void test_h_retrieveNRetailTrade5() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //异常Case:单号不完整（长度大于10小于20）
        currentIndex = 1;
        totalPageIndex = 100000;
        rtCondition.setQueryKeyword("SN1234832180");
        rtCondition.setDatetimeStart(Constants.getDefaultSyncDatetime());
        rtCondition.setDatetimeEnd(new Date());
        rtCondition.setPageIndex(String.valueOf(currentIndex));
        rtCondition.setPageSize(BaseHttpBO.PAGE_SIZE_Default);

        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rtCondition)) {
            Assert.assertTrue("查询旧零售单成功，预期失败: check()应不通过", true);
        } else {
            Assert.assertTrue("查询旧零售单失败，达到预期！check()应不通过", false);
        }
    }

    @Test
    public void test_i_returnRT() throws Exception {
        Shared.printTestMethodStartInfo();

        // 关闭网络
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        // 关闭wifi，使网络不可用
        wifiManager.setWifiEnabled(false);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        ;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));
        //进行售卖一张单A
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败！", rtcPresenter.getLastErrorCode() == EC_NoError);
        //
        RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld);
        Assert.assertTrue("创建临时零售单失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //对A进行退货，
        long maxReturnRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单的临时ID失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxReturnRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！查找零售单商品的临时ID失败！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade rerturnRT = DataInput.getRetailTrade(maxReturnRetailTradeIDInSQLite, maxReturnRetailTradeCommodityIDInSQLite);
        rerturnRT.setSourceID(ssfOld.getID().intValue());
        rerturnRT.setSn(ssfOld.getSn() + "_1");
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rerturnRT);
        Assert.assertTrue("创建临时退货零售单失败", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //有网情况下进行上传
        wifiManager.setWifiEnabled(true);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
        //
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //上传
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查询临时零售单失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getLastErrorCode() != EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
                    || (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != EC_NoError)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lTimeOut < 0) {
            Assert.assertTrue("上传超时", false);
        }
        if (retailTradeSQLiteEvent.getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("上传失败", false);
        }

        // 获取服务器传来的数据
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradeSQLiteEvent.getListMasterTable();
        RetailTrade rt = retailTradeList.get(retailTradeList.size() - 2); // 让数据不受上边测试影响
        RetailTrade returnRT = retailTradeList.get(retailTradeList.size() - 1);
        Assert.assertTrue("服务器拒绝了F_SourceID不存在的退货型零售单！", returnRT.getSourceID() == rt.getID());
    }

    private RetailTrade createRetailTradeInSQLite(RetailTrade retailTrade) {
        RetailTrade retailTradeInSQLite = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("创建本地临时零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);

        return retailTradeInSQLite;
    }

    private void uploadTmpRetailTradeList() throws InterruptedException {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("同步请求失败！", false);
        }
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("批量上传失败！原因：超时", false);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("批量上传失败", false);
        }
    }

    /**
     * 检查是否将临时的全部都上传成功
     */
    private boolean checkIfTmpRetailTradeExistsInSQLite() {
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_SyncDatetime = ?");
        retailTrade.setConditions(new String[]{"0"});
        retailTrade.setQueryKeyword(RetailTrade.generateRetailTradeSN(Constants.posID));
        //
        List<?> retailTradeList = retailTradePresenter.retrieveNObjectSync(CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        if (retailTradePresenter.getLastErrorCode() == EC_NoError && retailTradeList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void test_j_CreateN1() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("case1:批量上传A,B,C三张单。都是正常的单，期望结果：服务器返回EC_NoError的错误码，SQLite成功更新A,B,C三张单");
        Assert.assertTrue("登录失败！", Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        //先将本地临时的都上传，以免影响下面的测试
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！", checkIfTmpRetailTradeExistsInSQLite());
        //在本地生成临时的ABC三张零售单。
        List<RetailTrade> retailTradeOldList = new ArrayList<RetailTrade>();
        for (int i = 0; i < 3; i++) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
            long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
            //
            RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
            RetailTrade retailTrade = createRetailTradeInSQLite(ssfOld);
            //
            retailTradeOldList.add(retailTrade);
        }
        //上传临时数据
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！", checkIfTmpRetailTradeExistsInSQLite());
        Assert.assertTrue("批量上传零售单失败，服务器没返回三张单", retailTradeSQLiteEvent.getListMasterTable().size() == 3);
        for (int i = 0; i < retailTradeSQLiteEvent.getListMasterTable().size(); i++) {
            RetailTrade retailTradeNew = (RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(i);
            for (int j = 0; j < retailTradeOldList.size(); j++) {
                if (retailTradeNew.getSn().equals(retailTradeOldList.get(j).getSn())) {
                    retailTradeNew.setIgnoreIDInComparision(true);
                    Assert.assertTrue("临时零售单和上传服务器后的数据不一致", retailTradeNew.compareTo(retailTradeOldList.get(j)) == 0);
                    break;
                }
            }
        }
    }

    @Test
    public void test_j_CreateN2() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("case2:批量上传A,B,C三张单。都是错误的单，期望结果：服务器返回EC_PartSuccess的错误码，SQLite未能更新A,B,C三张单");
        Assert.assertTrue("登录失败！", Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //先将本地临时的都上传，以免影响下面的测试
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！可能还有错误的临时数据，需要另行删除", checkIfTmpRetailTradeExistsInSQLite());
        //
        List<RetailTrade> retailTradeOldList = new ArrayList<RetailTrade>();
        for (int i = 0; i < 3; i++) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
            long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
            //
            RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
            ssfOld.setVipID(MaxVIPID + i);
            RetailTrade retailTrade = createRetailTradeInSQLite(ssfOld);
            //
            retailTradeOldList.add(retailTrade);
        }
        uploadTmpRetailTradeList();
        Assert.assertTrue("批量上传错误的零售单，服务器返回了正确的数据！可能还有错误的临时数据，需要另行删除", retailTradeSQLiteEvent.getListMasterTable().size() == 0);
        //删除错误的临时零售单
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOldList.get(i));
            Assert.assertTrue("删除临时零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        }
    }

    @Test
    public void test_j_CreateN3() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("case3:批量上传A,B,C三张单。其中零售单A为错误的，期望结果：服务器返回EC_PartSuccess的错误码，SQLite成功更新B,C零售单。A未上传成功");
        Assert.assertTrue("登录失败！", Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        //先将本地临时的都上传，以免影响下面的测试
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！可能还有错误的临时数据，需要另行删除", checkIfTmpRetailTradeExistsInSQLite());
        //
        List<RetailTrade> retailTradeOldList = new ArrayList<RetailTrade>();
        //创建错误的零售单A
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeA = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeA.setVipID(MaxVIPID);
        retailTradeA = createRetailTradeInSQLite(retailTradeA);
        retailTradeOldList.add(retailTradeA);
        //创建正确的零售单BC
        for (int i = 0; i < 2; i++) {
            maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
            maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
            //
            RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
            RetailTrade retailTrade = createRetailTradeInSQLite(ssfOld);
            //
            retailTradeOldList.add(retailTrade);
        }
        uploadTmpRetailTradeList();
        Assert.assertTrue("服务器返回的正确零售单数目不正确", retailTradeSQLiteEvent.getListMasterTable().size() == 2);
        //删除错误的临时零售单A
        retailTradeOldList.remove(retailTradeA);
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeA);
        Assert.assertTrue("删除临时零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //检查零售单BC
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            RetailTrade retailTradeInSQLite = retailTradeOldList.get(i);
            for (int j = 0; j < retailTradeSQLiteEvent.getListMasterTable().size(); j++) {
                if (retailTradeInSQLite.getSn().equals(((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)).getSn())) {
                    retailTradeInSQLite.setIgnoreIDInComparision(true);
                    Assert.assertTrue("SQLite的数据和服务器返回的数据不一致！", retailTradeInSQLite.compareTo((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)) == 0);
                    break;
                }
            }
        }
    }

    @Test
    public void test_j_CreateN4() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("case4:批量上传A,B,C三张单。其中零售单B为错误的，期望结果：服务器返回EC_PartSuccess的错误码，SQLite成功更新A,C零售单。B未上传成功");
        Assert.assertTrue("登录失败！", Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        //先将本地临时的都上传，以免影响下面的测试
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！可能还有错误的临时数据，需要另行删除", checkIfTmpRetailTradeExistsInSQLite());
        List<RetailTrade> retailTradeOldList = new ArrayList<RetailTrade>();
        //创建零售单A
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeA = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeA = createRetailTradeInSQLite(retailTradeA);
        retailTradeOldList.add(retailTradeA);
        //创建错误的零售单B
        maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeB = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeB.setVipID(MaxVIPID);
        retailTradeB = createRetailTradeInSQLite(retailTradeB);
        retailTradeOldList.add(retailTradeB);
        //创建零售单C
        maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeC = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeC = createRetailTradeInSQLite(retailTradeC);
        retailTradeOldList.add(retailTradeC);
        //
        uploadTmpRetailTradeList();
        Assert.assertTrue("服务器返回的正确零售单数目不正确", retailTradeSQLiteEvent.getListMasterTable().size() == 2);
        //删除错误的临时零售单A
        retailTradeOldList.remove(retailTradeB);
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeB);
        Assert.assertTrue("删除临时零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //检查零售单BC
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            RetailTrade retailTradeInSQLite = retailTradeOldList.get(i);
            for (int j = 0; j < retailTradeSQLiteEvent.getListMasterTable().size(); j++) {
                if (retailTradeInSQLite.getSn().equals(((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)).getSn())) {
                    retailTradeInSQLite.setIgnoreIDInComparision(true);
                    Assert.assertTrue("SQLite的数据和服务器返回的数据不一致！", retailTradeInSQLite.compareTo((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)) == 0);
                    break;
                }
            }
        }
    }

    @Test
    public void test_j_CreateN5() throws Exception {
        Shared.printTestMethodStartInfo();

        caseLog("case5:批量上传A,B,C三张单。其中零售单C为错误的，期望结果：服务器返回EC_PartSuccess的错误码，SQLite成功更新A,B零售单。C未上传成功.");
        Assert.assertTrue("登录失败！", Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime(EVENT_ID_RetailTradeJUnit);
        //先将本地临时的都上传，以免影响下面的测试
        uploadTmpRetailTradeList();
        Assert.assertTrue("上传残留的临时零售单后本地中还有！可能还有错误的临时数据，需要另行删除", checkIfTmpRetailTradeExistsInSQLite());
        //
        List<RetailTrade> retailTradeOldList = new ArrayList<RetailTrade>();
        //创建正确的零售单AB
        for (int i = 0; i < 2; i++) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
            long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
            //
            RetailTrade ssfOld = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
            RetailTrade retailTrade = createRetailTradeInSQLite(ssfOld);
            //
            retailTradeOldList.add(retailTrade);
        }
        //创建错误的零售单C
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);
        RetailTrade retailTradeC = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeC.setVipID(MaxVIPID);
        retailTradeC = createRetailTradeInSQLite(retailTradeC);
        retailTradeOldList.add(retailTradeC);
        //上传临时零售单
        uploadTmpRetailTradeList();
        Assert.assertTrue("服务器返回的正确零售单数目不正确", retailTradeSQLiteEvent.getListMasterTable().size() == 2);
        //删除错误的临时零售单C
        retailTradeOldList.remove(retailTradeC);
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeC);
        Assert.assertTrue("删除临时零售单失败！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        //检查零售单AB
        for (int i = 0; i < retailTradeOldList.size(); i++) {
            RetailTrade retailTradeInSQLite = retailTradeOldList.get(i);
            for (int j = 0; j < retailTradeSQLiteEvent.getListMasterTable().size(); j++) {
                if (retailTradeInSQLite.getSn().equals(((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)).getSn())) {
                    retailTradeInSQLite.setIgnoreIDInComparision(true);
                    Assert.assertTrue("SQLite的数据和服务器返回的数据不一致！", retailTradeInSQLite.compareTo((RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(j)) == 0);
                    break;
                }
            }
        }
    }

    @Test
    public void test_j_CreateN6() throws Exception {
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = rtcPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", rtcPresenter.getLastErrorCode() == EC_NoError);

        List<BaseModel> newList = new ArrayList<>();
        RetailTrade retailTrade1 = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTrade1.setListSlave1(null);
        newList.add(retailTrade1);

        List<BaseModel> oldList = new ArrayList<>();
        RetailTrade retailTrade2 = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        oldList.add(retailTrade2);


        retailTradeSQLiteEvent.setListMasterTable(oldList);
        retailTradeSQLiteBO.onResultCreateN(newList);
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("插入SQLite失败，超时", false);
        }
        RetailTrade retailTrade = (RetailTrade) retailTradeSQLiteEvent.getListMasterTable().get(0);
        List<RetailTradeCommodity> selectRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        if (selectRetailTradeCommodityList == null || selectRetailTradeCommodityList.size() == 0) {
            System.out.println("该零售单从表为空");
        } else {
            System.out.println("该零售单从表不为空");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
//                    event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    event.setRequestType(null);
                    Ntp ntp = (Ntp) event.getBaseModel1();
                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
                    //
                }
            } else {
                NtpHttpBO.TimeDifference = 0;
//                log.info("同步NTP时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}

