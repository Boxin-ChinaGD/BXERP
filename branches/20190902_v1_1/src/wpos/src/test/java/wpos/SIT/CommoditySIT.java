package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommoditySIT extends BaseHttpTestCase {
    private static List<Commodity> commodityList;
    private static Long conflictID;
    private static Commodity comm;
    private static RetailTrade createdRetailTrade = null;


    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;

    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
    private static NtpHttpEvent ntpHttpEvent = null;
    private static NtpHttpBO ntpHttpBO = null;

    private static List<Commodity> commoditySyncList = new ArrayList<Commodity>();
    private static RetailTrade retailTrade = new RetailTrade();

    private static final String pageSize = "10";

    //先在本类定义避免混乱，商量好后一起放在Shared类
    private static final int EVENT_ID_CommoditySIT = 10000;

    public static class DataInput {
        public static RetailTrade getRetailTrade(int lRetailTradeMaxIDInSQLite, int lRtcMaxIDInSQLite, int commodityNum) throws ParseException {
            int lRetailTradeStartID = lRetailTradeMaxIDInSQLite;
            int lRtcStartID = lRtcMaxIDInSQLite;

            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeStartID);
            retailTrade.setSn(retailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) lRetailTradeStartID);
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setLogo("");
            retailTrade.setStaffID(4);
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setPaymentType(1);
            retailTrade.setPaymentAccount("");
//            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(1);
            retailTrade.setStatus(1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());

            List<Integer> barcodesIDList = new ArrayList<Integer>();
            barcodesIDList.add(1);
            barcodesIDList.add(3);
            barcodesIDList.add(5);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityNum; i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                retailTradeCommodity.setTradeID((long) lRetailTradeStartID);
                retailTradeCommodity.setID(lRtcStartID + i);
                retailTradeCommodity.setCommodityID(i + 1);
                retailTradeCommodity.setCommodityName("xxxxxxxx");
                retailTradeCommodity.setNO(2);
                retailTradeCommodity.setPriceOriginal(222.6);
                retailTradeCommodity.setDiscount(0.9);
                retailTradeCommodity.setBarcodeID(barcodesIDList.get(i));
                retailTradeCommodity.setPriceReturn(6.66);
                retailTradeCommodity.setNOCanReturn(10);
                retailTradeCommodityList.add(retailTradeCommodity);
            }

            retailTrade.setListSlave1(retailTradeCommodityList);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(commoditySQLiteEvent, commodityHttpEvent);
            commoditySQLiteBO.setCommodityPresenter(commodityPresenter);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(commoditySQLiteEvent, commodityHttpEvent);
        }
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
            retailTradeSQLiteBO.setRetailTradePresenter(retailTradePresenter);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_CommoditySIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(ntpHttpEvent);
        }
        ntpHttpEvent.setHttpBO(ntpHttpBO);
    }

    @Override
    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
//        if(event.getId() == EVENT_ID_CommoditySIT) {
        event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
//        if(event.getId() == EVENT_ID_CommoditySIT) {
        event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("CommoditySIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，CommoditySIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            System.out.println("#########################################################CommoditySIT onRetailTradeSQLiteEvent");
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("CommoditySIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，CommoditySIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
    /**
     * 1.pos1 staff登录
     * 2.上传一个零售单（商品数量减少）
     * 3.POS2 staff登录
     * 4.POS2 call RN 没有返回商品
     * 5.call普通RN，返回全部商品（删除本地SQLite所有商品信息，插入服务器返回的所有商品信息）
     */
    /**
     * 一张零售单一个商品，商品库存改变时，不会创建同步块
     */
    @Test
    public void test_a_commodity() throws Exception {
        Shared.printTestMethodStartInfo();

        //运行测试前删除临时的零售单，以免临时ID取出出现错误
        List<RetailTrade> tmpRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        for (int i = 0; i < tmpRetailTradeList.size(); i++) {
            RetailTrade rt = tmpRetailTradeList.get(i);
            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "本地的临时零售单数据删除失败");
        }

        //1.pos1 staff登录
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        // 单据流水号用时间命令，时间不能大于服务器时间，所以先同步时间差
        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(1);

        logOut();

        //3.POS2 staff登录
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //4.POS2 call RN 返回零售单中的商品
        callRN();
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse(rtc.getCommodityID() == comm.getID(), "同步RN不应该有该零售单的商品数据返回");
                }
            }
        }

        //5.call普通RN，返回全部商品（删除本地SQLite所有商品信息，插入服务器返回的所有商品信息）
        callRNC("1");
        //
        int count = Integer.parseInt(commodityHttpEvent.getCount());
        int remainder = count % Integer.valueOf(pageSize);
        int result = count / Integer.valueOf(10);
        int param = (remainder == 0 ? result : result + 1);
        for (int i = 1; i < param; i++) {
            callRNC(String.valueOf(i + 1));
        }
    }

    /**
     * 一张零售单多个商品，商品库存改变时，不会创建同步块
     *
     * @throws InterruptedException
     */
    @Test
    public void test_b_commodity() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1.pos1 staff登录
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        // 单据流水号用时间命令，时间不能大于服务器时间，所以先同步时间差
        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(3);

        logOut();

        //3.POS2 staff登录
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //4.POS2 call RN 返回零售单中的商品
        callRN();
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse(rtc.getCommodityID() == comm.getID(), "同步RN不应该有该零售单的商品数据返回");
                }
            }
        }
    }

    /**
     * 两张零售单，每张零售单一个商品
     * 对库存修改，商品是不会创建同步块的
     */
    @Test
    public void test_c_commodity() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1.pos1 staff登录
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(1);

        logOut();

        //3.POS2 staff登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //4.POS2 call RN 返回零售单中的商品
        callRN();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse(rtc.getCommodityID() == comm.getID(), "同步RN不应该有该零售单的商品数据返回");
                }
            }
        }
    }

    /**
     * 两张零售单，每张零售单多个商品
     * 对库存修改，商品是不会创建同步块的
     */
    @Test
    public void test_d_commodity() throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1.pos1 staff登录
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        syncTime();

        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(3);

        logOut();

        //3.POS2 staff登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //4.POS2 call RN 不返回商品
        callRN();
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse(rtc.getCommodityID() == comm.getID(), "同步RN不应该有该零售单的商品数据返回");
                }
            }
        }
    }

    private void callRN() throws InterruptedException {
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "RN请求需要同步的Commodity失败！");
        }
        //
        long lTimeOut = 60;
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "请求同步Commodity超时！");
        }
    }

    private void callRNC(String pageIndex) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setPageIndex(pageIndex);
        commodity.setPageSize(pageSize);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue(false, "RNC失败！");
        }
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "RNC失败！原因：超时");
        }
    }

    private void callFeedback(List<Commodity> commodityList) throws InterruptedException {
        String commodityIDs = "";
        for (int i = 0; i < commodityList.size(); i++) {
            commodityIDs = commodityIDs + "," + commodityList.get(i).getID();
        }
        commodityIDs = commodityIDs.substring(1, commodityIDs.length());
        commodityHttpBO.feedback(commodityIDs);
        //等待
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "测试失败！原因：超时");
        }
    }

    public void createdRetailTrade(int commodityNumber) throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();
//
//        SyncThread.aiPause.set(SyncThread.PAUSE);
//
//        Shared.login(1l);
        int maxRetailTradeInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        int maxCommodityInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        RetailTrade retailTradeOld = DataInput.getRetailTrade(maxRetailTradeInSQLite, maxCommodityInSQLite, commodityNumber);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld)) {
            Assert.assertTrue(false, "创建零售单失败！");
        }
        //等待处理完毕
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        long lTimeOut = 70;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "创建零售单失败！原因：超时" + retailTradeSQLiteBO.getSqLiteEvent().getStatus());
        }

        retailTrade = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue(retailTrade != null, "服务器返回的零售单为空");
        //比较主从表数据         旧对象:retailTradeOld 新对象：master
        retailTradeOld.setIgnoreIDInComparision(true);
        Assert.assertTrue(retailTradeOld.compareTo(retailTrade) == 0, "服务器返回的零售单对象和上传的零售单对象不相等！");
    }

    //查找单个 商品
    @Test
    public void test_e_Retrieve1Comm() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //1.pos1 staff登录
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        Commodity commodity = new Commodity();
        commodity.setID(1);
        if (!commodityHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue(false, "调用Retrieve1失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "测试失败！原因：超时");
        }

        Commodity commodity1 = (Commodity) commodityHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue(commodity1 != null, "查找Commodity失败！！");
    }

    /**
     * 同步APP的时间和服务器的时间
     */
    private void syncTime() {
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue(false, "查找Commodity失败！！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "同步失败！原因：超时" + ntpHttpBO.getHttpEvent().getStatus());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
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
