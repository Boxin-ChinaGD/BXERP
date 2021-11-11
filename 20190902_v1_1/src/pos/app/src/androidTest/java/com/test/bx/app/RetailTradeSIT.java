package com.test.bx.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.AppApplication;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LoginHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.helper.NetworkBroadcastReceiverHelper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.NetworkStatusService;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RetailTradeSIT extends BaseHttpAndroidTestCase {
    private static RetailTrade createdRetailTrade = null;

    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;

    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static LoginHttpBO loginHttpBO = null;

    private NetworkBroadcastReceiverHelper networkBroadcastReceiverHelper = null;

    private static long maxFrameIDInSQLite;

    private static boolean isSync = false;//用于判断是否同步完成
    private static boolean isCreate = false;//判断是否上传零售单

    private static final int EVENT_ID_RetailTradeSIT = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static RetailTrade retailTrade = new RetailTrade();

        public static final RetailTrade getRetailTrade() throws ParseException {
            if (retailTradePresenter == null || retailTradeCommodityPresenter == null) {
                retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
                retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
            }

            Random ran = new Random();
            long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            System.out.println("------------------- 拿到的临时零售单为" + maxFrameIDInSQLite);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            retailTrade.setID(maxFrameIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) maxFrameIDInSQLite);
            retailTrade.setPos_ID(1);
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setLogo("11");
            retailTrade.setStaffID(4);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setPaymentType(1);//支付方式为1：表示现金支付。Amount必须为其它支付方式之和
            retailTrade.setAmount(2222.2);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setWxRefundSubMchID("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundDesc("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxTradeNO("tradeno" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setVipID(2);
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
            retailTrade.setShopID(2);

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(retailTrade.getID());
            retailTradeCommodity.setID(maxTextIDInSQLite);
            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(0.9);
            retailTradeCommodity.setNOCanReturn(20);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(20);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(retailTrade.getID());
            retailTradeCommodity2.setID(maxTextIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(2);
            retailTradeCommodity2.setBarcodeID(1);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(0.7);
            retailTradeCommodity2.setNOCanReturn(40);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(20);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }

        RetailTradeSQLiteEvent sqLiteEvent = new RetailTradeSQLiteEvent();
        RetailTradeHttpEvent httpEvent = new RetailTradeHttpEvent();
        if (loginHttpBO == null) {
            loginHttpBO = new LoginHttpBO(getContext(), sqLiteEvent, httpEvent);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeSIT);
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

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        sqLiteEvent.setTimeout(30 * 1000);

        sqLiteEvent.setEventProcessed(false);

        registerBroadcast();
        Intent intent = new Intent(getContext(), NetworkStatusService.class);
        getContext().startService(intent);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        unregisterBroadcast();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeSIT onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    /**
     * 1.POS1 Staff登录
     * 2.关闭wifi，使网络不可用
     * 3.创建零售单，判断网络是否可用，如果可用，上传零售单到服务器，否则将零售单存在本地，等到网络可用再将其上传到服务器
     * 4.打开wifi，使网络可用
     * 5.直到网络可用，恢复同步线程，查询数据库中syncDatetime为1970-01-01 00:00:00 000的记录，将其上传到服务器
     *
     * @throws InterruptedException
     */
    @Test
    public void test_a_networkRetailTrade() throws Exception {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        Shared.printTestMethodStartInfo();
        wifiManager.setWifiEnabled(true);
//        Thread.sleep(5 * 1000);
        long lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));


        //1.POS1 Staff登录
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        isCreate = true;
//        Thread.sleep(5 * 1000);

        //2.关闭wifi，使网络不可用
        wifiManager.setWifiEnabled(false);
//        Thread.sleep(3 * 1000);
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));

        //3.创建零售单，判断网络是否可用，如果可用，上传零售单到服务器，否则将零售单存在本地，等到网络可用再将其上传到服务器
        RetailTrade retailTrade = getRetailTrade();
        if (!Constants.isNetworkAvailable) {//网络不可用
            createTmpRetailTrade(retailTrade);
        } else {
            createRetailTrade(isCreate, retailTrade);
        }

        //4.打开wifi，使网络可用
        wifiManager.setWifiEnabled(true);
//        Thread.sleep(18 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        networkBroadcastReceiverHelper = new NetworkBroadcastReceiverHelper(getContext(), new NetworkBroadcastReceiverHelper.OnNetworkStateChangedListener() {
            @Override
            public void onConnected() {
                //连接成功时的操作，启动同步线程
                Constants.isNetworkAvailable = true;
                System.out.println("网络连接成功！");
                if (isCreate) {
                    ((AppApplication) getContext()).startSyncThread();
//                    SyncThread.aiPause.set(SyncThread.RESUME);
                }
            }

            @Override
            public void onDisConnected() {
                //连接失败时的操作
                Constants.isNetworkAvailable = false;
                System.out.println("网络连接失败！");
//                SyncThread.aiPause.set(SyncThread.PAUSE);
                ((AppApplication) getContext()).exitFromSyncThread();
            }
        });
        networkBroadcastReceiverHelper.register();
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        networkBroadcastReceiverHelper.unregister();
    }

    /**
     * 获取到需要上传的零售单
     *
     * @return
     * @throws CloneNotSupportedException
     */
    private RetailTrade getRetailTrade() throws Exception {
        maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade retailTradeOld = RetailTradeJUnit.DataInput.getRetailTrade(maxFrameIDInSQLite, maxTextIDInSQLite);
        return retailTradeOld;
    }

    /**
     * 上传一张零售单
     *
     * @throws CloneNotSupportedException
     * @throws InterruptedException
     */
    public void createRetailTrade(boolean isCreate, RetailTrade retailTrade) throws CloneNotSupportedException, InterruptedException {
        if (isCreate) {
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);//...
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
            if (!retailTradeHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
                Assert.assertTrue("创建失败！", false);
            }

            //等待处理完毕
            long lTimeout = 50;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
                Thread.sleep(1000);
            }

            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                Assert.assertTrue("测试失败！原因：超时", false);
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getHttpEvent().getBaseModel1();
            Assert.assertTrue("", master != null);

            maxFrameIDInSQLite = retailTradePresenter.getMaxId(true, BaseModel.FIELD_NAME_syncDatetime);
            createdRetailTrade = master;//为查询测试提供数据
            Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("此时Max ID (" + maxFrameIDInSQLite + ") 应该是master的ID(" + master.getID().longValue() + ")", maxFrameIDInSQLite == master.getID().longValue());

            //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
            retailTrade.setIgnoreIDInComparision(true);
            Assert.assertTrue("服务器返回的对象和上传的对象不相等！", retailTrade.compareTo(master) == 0);
        }
    }

    /**
     * 创建临时零售单,存于SQLite
     *
     * @throws CloneNotSupportedException
     */
    public void createTmpRetailTrade(RetailTrade retailTradeOld) throws CloneNotSupportedException {
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTradeOld)) {
            Assert.assertTrue("创建临时数据失败！", false);
        }
        if (!waitForEventProcessed(retailTradeSQLiteBO.getSqLiteEvent())) {
            Assert.assertTrue("createMasterSlave测试失败！原因：超时", false);
        } else {
            Assert.assertTrue("craeteMasterSlave返回的错误码不正确！", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld);
            Assert.assertTrue("R1返回的错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入临时数据失败！", retailTrade != null && retailTrade.compareTo(retailTradeOld) == 0);
        }
    }

    /*
    全部同步
     */
    @Test
    public void test_b_tEverything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //本地创建一条零售单主表
        RetailTrade bmCreateSync = DataInput.getRetailTrade();
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync bm1测试失败,错误码不正确：" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //本地创建一条零售单商品从表
        List<RetailTradeCommodity> rtcs = (List<RetailTradeCommodity>) bmCreateSync.getListSlave1();
        for (RetailTradeCommodity rtc : rtcs) {
            retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
            Assert.assertTrue("CreateSync rtc测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);//查询主从表信息
        //
        bmCreateSync.setIgnoreSyncTypeInComparision(true);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", bmCreateSync.compareTo(comm1Retrieve1) == 0);

        //POS去请求全部同步的Action并更新本地服务器
        RetailTrade rt = new RetailTrade();
        rt.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        rt.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        rt.setQueryKeyword("");
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, rt)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("请求服务器同步RetailTrde超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSyncC测试失败,原因:返回错误码不正确!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<RetailTrade> list = (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }

    /*
        查询旧零售单Test
     */
    @Test
    public void test_c_RetrieveNOldRetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建一张
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTrade ssfOld = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("创建失败！", false);
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象为空！", master != null);
        //
        RetailTrade rt = new RetailTrade();
        rt.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        rt.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        rt.setQueryKeyword(master.getSn());
        rt.setDatetimeStart(DatetimeUtil.addDays(new Date(), -3));
        rt.setDatetimeEnd(DatetimeUtil.addDays(new Date(), 0));

        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rt)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("请求服务器超时!", false);
        }
    }
}
