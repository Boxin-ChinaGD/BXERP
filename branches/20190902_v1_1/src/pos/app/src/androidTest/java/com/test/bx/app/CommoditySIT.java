package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
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
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
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

public class CommoditySIT extends BaseHttpAndroidTestCase {
    private static List<Commodity> commodityList;
    private static Long conflictID;
    private static Commodity comm;
    private static RetailTrade createdRetailTrade = null;

    private static CommodityPresenter commodityPresenter = null;
    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;

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
        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite, int commodityNum) throws ParseException {
            long lRetailTradeStartID = lRetailTradeMaxIDInSQLite;
            long lRtcStartID = lRtcMaxIDInSQLite;

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
                retailTradeCommodity.setTradeID(lRetailTradeStartID);
                retailTradeCommodity.setID(lRtcStartID + i);
                retailTradeCommodity.setCommodityID(i + 1);
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
    public void setUp() throws Exception {
        super.setUp();

        if (commodityPresenter == null) {
            commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        }
        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

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
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
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
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_CommoditySIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setSqliteBO(null);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(EVENT_ID_CommoditySIT);
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

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
//        if(event.getId() == EVENT_ID_CommoditySIT) {
        event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
//        if(event.getId() == EVENT_ID_CommoditySIT) {
        event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommoditySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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
            Assert.assertTrue("本地的临时零售单数据删除失败", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        //1.pos1 staff登录
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        // 单据流水号用时间命令，时间不能大于服务器时间，所以先同步时间差
        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(1);

        logOut();

        //3.POS2 staff登录
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
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
                    Assert.assertFalse("同步RN不应该有该零售单的商品数据返回", rtc.getCommodityID() == comm.getID());
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
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        // 单据流水号用时间命令，时间不能大于服务器时间，所以先同步时间差
        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(3);

        logOut();

        //3.POS2 staff登录
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
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
                    Assert.assertFalse("同步RN不应该有该零售单的商品数据返回", rtc.getCommodityID() == comm.getID());
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
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        syncTime();
        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(1);

        logOut();

        //3.POS2 staff登录
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //4.POS2 call RN 返回零售单中的商品
        callRN();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse("同步RN不应该有该零售单的商品数据返回", rtc.getCommodityID() == comm.getID());
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
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        syncTime();

        //2.上传一个零售单（商品数量减少）
        createdRetailTrade(3);

        logOut();

        //3.POS2 staff登录
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //4.POS2 call RN 不返回商品
        callRN();
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        // 全部测试一起运行的时候，由于之前的测试会创建商品，导致会有其他的同步块产生，所以需要精确判断
        commoditySyncList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        if (commoditySyncList != null) {
            for (Commodity comm : commoditySyncList) {
                for (Object o : retailTrade.getListSlave1()) {
                    RetailTradeCommodity rtc = (RetailTradeCommodity) o;
                    Assert.assertFalse("同步RN不应该有该零售单的商品数据返回", rtc.getCommodityID() == comm.getID());
                }
            }
        }
    }

    private void callRN() throws InterruptedException {
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
        }
        //
        long lTimeOut = 60;
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求同步Commodity超时！", false);
        }
    }

    private void callRNC(String pageIndex) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setPageIndex(pageIndex);
        commodity.setPageSize(pageSize);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("RNC失败！", false);
        }
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("RNC失败！原因：超时", false);
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
            Assert.assertTrue("测试失败！原因：超时", false);
        }
    }

    public void createdRetailTrade(int commodityNumber) throws InterruptedException, ParseException {
        Shared.printTestMethodStartInfo();
//
//        SyncThread.aiPause.set(SyncThread.PAUSE);
//
//        Shared.login(1l);
        long maxRetailTradeInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxCommodityInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade retailTradeOld = DataInput.getRetailTrade(maxRetailTradeInSQLite, maxCommodityInSQLite, commodityNumber);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld)) {
            Assert.assertTrue("创建零售单失败！", false);
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
            Assert.assertTrue("创建零售单失败！原因：超时" + retailTradeSQLiteBO.getSqLiteEvent().getStatus(), false);
        }

        retailTrade = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的零售单为空", retailTrade != null);
        //比较主从表数据         旧对象:retailTradeOld 新对象：master
        retailTradeOld.setIgnoreIDInComparision(true);
        Assert.assertTrue("服务器返回的零售单对象和上传的零售单对象不相等！", retailTradeOld.compareTo(retailTrade) == 0);
    }

    //查找单个 商品
    @Test
    public void test_e_Retrieve1Comm() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //1.pos1 staff登录
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        Commodity commodity = new Commodity();
        commodity.setID(1l);
        if (!commodityHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("调用Retrieve1失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("测试失败！原因：超时", false);
        }

        Commodity commodity1 = (Commodity) commodityHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("查找Commodity失败！！", commodity1 != null);
    }

    /**
     * 同步APP的时间和服务器的时间
     */
    private void syncTime() {
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue("查找Commodity失败！！", false);
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
            Assert.assertTrue("同步失败！原因：超时" + ntpHttpBO.getHttpEvent().getStatus(), false);
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
