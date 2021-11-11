package wpos.SIT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.UT.RetailTradeJUnit;
import wpos.Main;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.helper.NetworkBroadcastReceiverHelper;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeCommodity;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradeCommodityPresenter;
import wpos.presenter.RetailTradePresenter;
import wpos.utils.DatetimeUtil;
import wpos.utils.NetworkUtils;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RetailTradeSIT extends BaseHttpTestCase {
    private static RetailTrade createdRetailTrade = null;

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
        private static final RetailTrade retailTrade = new RetailTrade();

        public static RetailTrade getRetailTrade(RetailTradePresenter retailTradePresenter, RetailTradeCommodityPresenter retailTradeCommodityPresenter) throws ParseException {

            Random ran = new Random();
            long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            System.out.println("------------------- 拿到的临时零售单为" + maxFrameIDInSQLite);
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
            long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

            retailTrade.setID((int) maxFrameIDInSQLite);
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
            retailTradeCommodity.setTradeID(Long.valueOf(retailTrade.getID()));
            retailTradeCommodity.setID((int) maxTextIDInSQLite);
            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(0.9);
            retailTradeCommodity.setNOCanReturn(20);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(20);
            retailTradeCommodity.setCommodityName("商品："+System.currentTimeMillis());

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(Long.valueOf(retailTrade.getID()));
            retailTradeCommodity2.setID((int) (maxTextIDInSQLite + 1));
            retailTradeCommodity2.setCommodityID(2);
            retailTradeCommodity2.setBarcodeID(1);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(0.7);
            retailTradeCommodity2.setNOCanReturn(40);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(20);
            retailTradeCommodity2.setCommodityName("商品："+System.currentTimeMillis());

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        RetailTradeSQLiteEvent sqLiteEvent = new RetailTradeSQLiteEvent();
        RetailTradeHttpEvent httpEvent = new RetailTradeHttpEvent();
        if (loginHttpBO == null) {
            loginHttpBO = new LoginHttpBO(sqLiteEvent, httpEvent);
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
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
            retailTradeSQLiteBO.setRetailTradePresenter(retailTradePresenter);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);

        logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        sqLiteEvent.setTimeout(30 * 1000);

        sqLiteEvent.setEventProcessed(false);

        registerBroadcast();
        // TODO: 2020/7/14
//        Intent intent = new Intent(getContext(), NetworkStatusService.class);
//        getContext().startService(intent);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
        unregisterBroadcast();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        Runtime.getRuntime().exec("ipconfig /renew").waitFor();
        long lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.fail("wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());


        //1.POS1 Staff登录
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO), "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        isCreate = true;

        //2.关闭wifi，使网络不可用
        Runtime.getRuntime().exec("ipconfig /release").waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.fail("wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());

        //3.创建零售单，判断网络是否可用，如果可用，上传零售单到服务器，否则将零售单存在本地，等到网络可用再将其上传到服务器
        RetailTrade retailTrade = getRetailTrade();
        if (!NetworkUtils.isNetworkAvalible()) {//网络不可用
            createTmpRetailTrade(retailTrade);
        } else {
            createRetailTrade(isCreate, retailTrade);
        }

        //4.打开wifi，使网络可用
        Runtime.getRuntime().exec("ipconfig /renew").waitFor();
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.fail("wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        Main.startSyncThread();
        // TODO: 2020/7/14 监听网络变化判断是否断网，断网则停止同步线程
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        Main.exitFromSyncThread();
    }

    /**
     * 获取到需要上传的零售单
     *
     * @return
     * @throws CloneNotSupportedException
     */
    private RetailTrade getRetailTrade() throws Exception {
        maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertSame(retailTradeCommodityPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        RetailTrade retailTradeOld = RetailTradeJUnit.DataInput.getRetailTrade(maxFrameIDInSQLite, maxTextIDInSQLite);
        return retailTradeOld;
    }

    /**
     * 上传一张零售单
     *
     * @throws CloneNotSupportedException
     * @throws InterruptedException
     */
    public void createRetailTrade(boolean isCreate, RetailTrade retailTrade) throws InterruptedException {
        if (isCreate) {
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);//...
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
            if (!retailTradeHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
                Assert.fail("创建失败！");
            }

            //等待处理完毕
            long lTimeout = 50;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
                Thread.sleep(1000);
            }

            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                Assert.fail("测试失败！原因：超时");
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getHttpEvent().getBaseModel1();
            Assert.assertNotNull(master, "");

            maxFrameIDInSQLite = retailTradePresenter.getMaxId(true, BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            createdRetailTrade = master;//为查询测试提供数据
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
            Assert.assertEquals(master.getID().longValue(), maxFrameIDInSQLite, "此时Max ID (" + maxFrameIDInSQLite + ") 应该是master的ID(" + master.getID().longValue() + ")");

            //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
            retailTrade.setIgnoreIDInComparision(true);
            Assert.assertEquals(retailTrade.compareTo(master), 0, "服务器返回的对象和上传的对象不相等！");
        }
    }

    /**
     * 创建临时零售单,存于SQLite
     *
     * @throws CloneNotSupportedException
     */
    public void createTmpRetailTrade(RetailTrade retailTradeOld) {
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTradeOld)) {
            Assert.fail("创建临时数据失败！");
        }
        if (!waitForEventProcessed(retailTradeSQLiteBO.getSqLiteEvent())) {
            Assert.fail("createMasterSlave测试失败！原因：超时");
        } else {
            Assert.assertSame(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "craeteMasterSlave返回的错误码不正确！");
            RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld);
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "R1返回的错误码不正确！");
            Assert.assertTrue(retailTrade != null && retailTrade.compareTo(retailTradeOld) == 0, "插入临时数据失败！");
        }
    }

    /*
    全部同步
     */
    @Test
    public void test_b_tEverything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO), "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //本地创建一条零售单主表
        RetailTrade bmCreateSync = DataInput.getRetailTrade(retailTradePresenter, retailTradeCommodityPresenter);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,错误码不正确：" + retailTradePresenter.getLastErrorCode());

        //本地创建一条零售单商品从表
        List<RetailTradeCommodity> rtcs = (List<RetailTradeCommodity>) bmCreateSync.getListSlave1();
        for (RetailTradeCommodity rtc : rtcs) {
            retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync rtc测试失败,原因:返回错误码不正确!");
        }
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);//查询主从表信息
        //
        bmCreateSync.setIgnoreSyncTypeInComparision(true);
        Assert.assertEquals(bmCreateSync.compareTo(comm1Retrieve1), 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //POS去请求全部同步的Action并更新本地服务器
        RetailTrade rt = new RetailTrade();
        rt.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        rt.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        rt.setQueryKeyword("");
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshByServerDataAsync_Done);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, rt)) {
            Assert.fail("调用RetrieveN失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("请求服务器同步RetailTrde超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSyncC测试失败,原因:返回错误码不正确!");

        List<RetailTrade> list = (List<RetailTrade>) retailTradeHttpBO.getHttpEvent().getListMasterTable();
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.fail("全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
    }

    /*
        查询旧零售单Test
     */
    @Test
    public void test_c_RetrieveNOldRetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO), "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //创建一张
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertSame(retailTradeCommodityPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        RetailTrade ssfOld = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.fail("创建失败！");
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("测试失败！原因：超时");
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertNotNull(master, "服务器返回的对象为空！");
        //
        RetailTrade rt = new RetailTrade();
        rt.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        rt.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        rt.setQueryKeyword(master.getSn());
        rt.setDatetimeStart(DatetimeUtil.addDays(new Date(), -3));
        rt.setDatetimeEnd(DatetimeUtil.addDays(new Date(), 0));

        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, rt)) {
            Assert.fail("调用RetrieveN失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("请求服务器超时!");
        }
    }
}
