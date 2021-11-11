package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.sync.SyncThread;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.WXPayInfo;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.Shared;
import com.bx.erp.utils.WXPayUtil;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class WXPaySIT extends BaseHttpAndroidTestCase {
    private static BarcodesPresenter barcodesPresenter = null;

    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;

    private static long maxFrameIDInSQLite;

    private static WXPayHttpBO wxPayHttpBO = null;
    private static WXPayHttpEvent wxPayHttpEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;

    public static AtomicInteger aiPause;

    private Map<String, String> microPayResponse = null;//微信支付responseData
    private Map<String, String> refundResponse = null;//微信退款responseData
    private RetailTrade returnRetailTradeWithRefundNO = null;

    private static final int Event_ID_WXPaySIT = 10000;

    public static class DataInput {
        protected static final RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite) throws ParseException {
            Random ran = new Random();

            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) lRetailTradeMaxIDInSQLite);
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setLogo("11");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setStaffID(4);
            retailTrade.setPaymentType(0);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2d);
            retailTrade.setAmountWeChat(2222.2d);
            retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setReturnObject(1);
            retailTrade.setPaymentType(4);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(177);
            retailTradeCommodity.setCommodityID(155);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(0.9);
            retailTradeCommodity.setPriceReturn(20);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(6.66);
            retailTradeCommodity.setNOCanReturn(20);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(157);
            retailTradeCommodity2.setBarcodeID(179);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(0.7);
            retailTradeCommodity.setPriceReturn(40);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(5.55);
            retailTradeCommodity2.setNOCanReturn(20);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }

        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_WXPaySIT);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_WXPaySIT);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(Event_ID_WXPaySIT);
        }
        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
        }
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_WXPaySIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_WXPaySIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_WXPaySIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
        microPayResponse = event.getMicroPayResponse();
        refundResponse = event.getRefundResponse();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("WXPaySIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，WXPaySIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################WXPaySIT onRetailTradeSQLiteEvent");
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("WXPaySIT.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，WXPaySIT.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 获取到需要上传的零售单
     *
     * @return
     * @throws CloneNotSupportedException
     */
    private RetailTrade getRetailTrade() throws CloneNotSupportedException, ParseException {
        maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！" +
                "", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade retailTradeOld = DataInput.getRetailTrade(maxFrameIDInSQLite, maxTextIDInSQLite); // 主表临时ID，从表ID
        return retailTradeOld;
    }

    /**
     * 上传一张零售单
     *
     * @throws CloneNotSupportedException
     * @throws InterruptedException
     */
    public void createRetailTrade(RetailTrade retailTrade) throws CloneNotSupportedException, InterruptedException {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(retailTrade); // ... 设置临时对象用于删除
        if (!retailTradeHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade)) {
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
        Assert.assertTrue("", master != null);

        Assert.assertTrue("错误码不正确！", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        retailTrade.setIgnoreIDInComparision(true);
        Assert.assertTrue("服务器返回的对象和上传的对象不相等！", retailTrade.compareTo(master) == 0);
    }

    /**
     * 创建临时零售单,存于SQLite
     *
     * @throws CloneNotSupportedException
     */
    public RetailTrade createTmpRetailTrade(RetailTrade retailTradeOld) throws CloneNotSupportedException {
        double totalMoney = 0.00d;
        for (int i = 0; i < retailTradeOld.getListSlave1().size(); i++) {
            totalMoney += ((List<RetailTradeCommodity>) retailTradeOld.getListSlave1()).get(i).getPriceOriginal() * Integer.valueOf(((List<RetailTradeCommodity>) retailTradeOld.getListSlave1()).get(i).getNO());
        }
        retailTradeOld.setAmount(totalMoney);
        retailTradeOld.setAmountWeChat(totalMoney);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTradeOld)) {
            Assert.assertTrue("创建本地零售单失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("创建本地零售单超时...", false);
        }

        Assert.assertTrue("craeteMasterSlave返回的错误码不正确！", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTrade retailTrade = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeOld);
        Assert.assertTrue("R1返回的错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入临时数据失败！", retailTrade != null && retailTrade.compareTo(retailTradeOld) == 0);

        return retailTrade;
    }

    /**
     * 创建临时退货零售单
     */
    public RetailTrade createReturnCommRetailTrade(RetailTrade returnCommRetailTrade) {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        double totalMoney = 0.00;
        //
        for (int i = 0; i < returnCommRetailTrade.getListSlave1().size(); i++) {
            totalMoney += ((List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1()).get(i).getPriceOriginal() * Integer.valueOf(((List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1()).get(i).getNO());
        }
        //
        returnCommRetailTrade.setAmount(totalMoney);
        returnCommRetailTrade.setAmountWeChat(totalMoney);
        returnCommRetailTrade.setSourceID(returnCommRetailTrade.getID().intValue());
        returnCommRetailTrade.setID(retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));// 本地创建retailtrade pos_sn
        returnCommRetailTrade.setLocalSN((int) retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
        returnCommRetailTrade.setDatetimeStart(new Date());
        returnCommRetailTrade.setDatetimeEnd(new Date());

        List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) returnCommRetailTrade.getListSlave1();
        for (int i = 0; i < rtcList.size(); i++) {
            rtcList.get(i).setID(retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime) + i);
        }

        long maxID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);

        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, returnCommRetailTrade)) {
            Assert.assertTrue("创建本地退货零售单失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("创建本地退货零售单超时...", false);
        }

        Assert.assertTrue("创建本地退货零售单失败", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("创建本地退货零售单位null", retailTradeSQLiteEvent.getBaseModel1() != null);

        return (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
    }

    /**
     * 根据零售单金额进行微信支付操作
     *
     * @throws Exception
     */
    public void wxMicroPay(RetailTrade retailTrade) {
        WXPayInfo wxPayInfo = new WXPayInfo();// 由于是沙箱环境，wxPayInfo传过去也是没意义的
        wxPayInfo.setAuth_code("134617607342397775");
        wxPayInfo.setTotal_fee("501.00");
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!wxPayHttpBO.microPayAsync(wxPayInfo)) {
            Assert.assertTrue("调用microPayAsync失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("微信支付超时...", false);
        }

        Assert.assertTrue("微信支付失败！！错误码为：" + wxPayHttpEvent.getLastErrorCode(), wxPayHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 根据退款零售单金额进行微信退款申请
     *
     * @throws Exception
     */
    public void wxRefund(RetailTrade retailTradeCreated) {
        if (!wxPayHttpBO.refundAsync(retailTradeCreated)) {
            Assert.assertTrue("调用refundAsync失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        wxPayHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("微信退款超时...", false);
        }

        Assert.assertTrue("微信退款申请失败！！" + wxPayHttpEvent.printErrorInfo(retailTradeCreated), wxPayHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 更新退货零售单(插入退款交易单号)
     *
     * @throws Exception
     */
    public RetailTrade updateRetailTrade(RetailTrade returnRetailTrade) {
        returnRetailTrade.setWxRefundNO(refundResponse.get("out_refund_no"));//退款交易单号
        System.out.println("----------" + refundResponse.get("out_refund_no"));
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        if (!retailTradeSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, returnRetailTrade)) {
            Assert.assertTrue("插入退款单号失败！！", false);
        }
        Assert.assertTrue("插入退款单号失败！！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("更新本地零售单超时...", false);
        }

        RetailTrade updateReturnRetailTrade = (RetailTrade) retailTradeSQLiteEvent.getBaseModel1();
        Assert.assertTrue("更新本地零售单返回null", updateReturnRetailTrade != null);

        return updateReturnRetailTrade;
    }

    /**
     * 查询旧零售单
     *
     * @throws Exception
     */
    public List<RetailTrade> retrieveRetailTrade(RetailTrade retailTrade) {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
            Assert.assertTrue("查询旧零售单失败！！", false);
        }
        long lTimeOut2 = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut2-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("查询旧单超时...", false);
        }

        Assert.assertTrue("查询零售单错误：", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradeSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("零售单为null!!!", rtList != null);

        System.out.println("零售单list：" + rtList);
        return rtList;
    }



    /*
    1.POS和staff登录
    2.沙箱环境下根据零售单金额进行微信支付
    3.创建SQLite零售单
    4.上传零售单
    5.查询旧零售单
    6.根据查询到的零售单创建退货零售单
    7.根据查询到的零售单创建退货零售单(选择退货商品和计算金额)
    8.更新退货零售单(插入退款交易单号)
    9.上传退货零售单
    * 微信支付SIT不含促销
     */

    // case1:零售单商品全部退货退款
    @Test
    public void test_a_WXPayWithoutPromotion() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.POS1 Staff登录
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        syncTime(Event_ID_WXPaySIT);
        // 2.沙箱环境下根据零售单金额进行微信支付
        RetailTrade retailTrade = getRetailTrade();
        RetailTrade retailTrade1 = (RetailTrade) retailTrade.clone();
        retailTrade1.setAmount(WXPayUtil.formatAmount(retailTrade1.getAmount()));
        wxMicroPay(retailTrade1);
        // 3.创建SQLite零售单
        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? Constants.submchid : microPayResponse.get("sub_mch_id"));
        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
        retailTrade.setPaymentType(4);
        RetailTrade retailTradeR1Condition = new RetailTrade();
        retailTradeR1Condition.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//        retailTrade.setWxRefundDesc(wxData.get("refund_desc") == null ? "" : wxData.get("refund_desc"));
        RetailTrade temRetailTrade = createTmpRetailTrade(retailTrade);

        // 4.上传零售单
        temRetailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
        temRetailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
        System.out.println("即将上传的零售单是：" + temRetailTrade);
        createRetailTrade(temRetailTrade);

        // 5.查询零售单
        retailTradeR1Condition.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        retailTradeR1Condition.setQueryKeyword(retailTrade.getSn());
        List<RetailTrade> rtList = retrieveRetailTrade(retailTradeR1Condition);

        // 6.根据查询到的零售单创建退货零售单(选择全部商品和计算金额)
        RetailTrade returnRetailTrade = createReturnCommRetailTrade(rtList.get(0));
        System.out.println("本地退货零售单：" + returnRetailTrade);
        System.out.println("本地退货零售单退款单号：" + returnRetailTrade.getWxRefundNO());

        // 7.根据退货零售单微信交易单号进行退款申请
        RetailTrade rt2 = (RetailTrade) returnRetailTrade.clone();
        rt2.setAmount(WXPayUtil.formatAmount(rt2.getAmount()));
        wxRefund(rt2);

        // 8.更新退货零售单(update退款交易单号)
        returnRetailTradeWithRefundNO = updateRetailTrade(returnRetailTrade);
        System.out.println("微信退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());

//        // 9.上传退货零售单
//        returnRetailTradeWithRefundNO.setDatetimeStart(new Date());
//        returnRetailTradeWithRefundNO.setDatetimeEnd(new Date());
//        createRetailTrade(returnRetailTradeWithRefundNO);

    }

    // case2:零售单商品部分退货退款
    @Test
    public void test_b_WXPayWithoutPromotion() throws Exception {
        //1.POS1 Staff登录
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        Thread.sleep(5 * 1000);
        syncTime(Event_ID_WXPaySIT);
        // 2.沙箱环境下根据零售单金额进行微信支付
        RetailTrade retailTrade = getRetailTrade();
        RetailTrade rt = (RetailTrade) retailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxMicroPay(rt);

        // 3.创建SQLite零售单
        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? Constants.submchid : microPayResponse.get("sub_mch_id"));
        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
        retailTrade.setPaymentType(4);
        RetailTrade retailTradeR1Condition = new RetailTrade();
        retailTradeR1Condition.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//        retailTrade.setWxRefundDesc(wxData.get("refund_desc") == null ? "" : wxData.get("refund_desc"));
        RetailTrade temRetailTrade = createTmpRetailTrade(retailTrade);
        // 4.上传零售单
        temRetailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
        temRetailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
        System.out.println("即将上传的零售单是：" + temRetailTrade);
        createRetailTrade(temRetailTrade);
        // 5.查询零售单
        retailTradeR1Condition.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        retailTradeR1Condition.setQueryKeyword(retailTrade.getSn());
        List<RetailTrade> rtList = retrieveRetailTrade(retailTradeR1Condition);

        // 6.根据查询到的零售单创建退货零售单(选择部分商品和计算金额)
        List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) rtList.get(0).getListSlave1();
        RetailTradeCommodity rtc = rtcList.get(0);
        rtc.setNO(10);

        rtcList.clear();
        rtcList.add(rtc);

        RetailTrade returnRetailTrade = createReturnCommRetailTrade(rtList.get(0));
        System.out.println("本地退货零售单：" + returnRetailTrade);

        RetailTradeCommodity rtc2 = (RetailTradeCommodity) returnRetailTrade.getListSlave1().get(0);
        String priceIn = String.valueOf(rtc.getNO() * rtc.getPriceOriginal());
        String priceOut = String.valueOf(rtc2.getNO() * rtc2.getPriceOriginal());
        System.out.println(priceIn + "==" + priceOut);
        Assert.assertTrue("比较选择的退货商品价格与退货零售单返回的价格不等！！！", priceIn.equals(priceOut));

        // 7.根据退货零售单微信交易单号进行退款申请
        RetailTrade rt2 = (RetailTrade) returnRetailTrade.clone();
        rt2.setAmount(WXPayUtil.formatAmount(rt2.getAmount()));
        wxRefund(rt2);

        // 8.更新退货零售单(update退款交易单号)
        returnRetailTradeWithRefundNO = updateRetailTrade(returnRetailTrade);
        System.out.println("微信退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());

        // 9.上传退货零售单
//        returnRetailTradeWithRefundNO.setDatetimeStart(new Date());
//        returnRetailTradeWithRefundNO.setDatetimeEnd(new Date());
//        List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) returnRetailTradeWithRefundNO.getListSlave1();
//        for (int i = 0; i < retailTradeCommodities.size(); i++) {
//            RetailTradeCommodity retailTradeCommodity = retailTradeCommodities.get(i);
//            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        }
//
//        createRetailTrade(returnRetailTradeWithRefundNO);

    }

    @Test
    public void test_WXMicroPay_case1() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1:支付整数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12);
        RetailTrade rt = (RetailTrade) retailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxMicroPay(rt);
        //
        Assert.assertTrue("微信支付转换的金额影响到了原零售单的金额", retailTrade.getAmount() != rt.getAmount());
    }

    @Test
    public void test_WXMicroPay_case2() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2: 支付带有俩位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.22);
        RetailTrade rt = (RetailTrade) retailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxMicroPay(rt);
        //
        Assert.assertTrue("微信支付转换的金额影响到了原零售单的金额", retailTrade.getAmount() != rt.getAmount());
    }

    @Test
    public void test_WXMicroPay_case3() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3:支付带有3位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.123);
        RetailTrade rt = (RetailTrade) retailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxMicroPay(rt);
        //
        Assert.assertTrue("微信支付转换的金额影响到了原零售单的金额", retailTrade.getAmount() != rt.getAmount());
    }

    @Test
    public void test_WXMicroPay_case4() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case4:支付带有6位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));

        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.226578);
        RetailTrade rt = (RetailTrade) retailTrade.clone();
        rt.setAmount(WXPayUtil.formatAmount(rt.getAmount()));
        wxMicroPay(rt);
        //
        Assert.assertTrue("微信支付转换的金额影响到了原零售单的金额", retailTrade.getAmount() != rt.getAmount());
    }

    @Test
    public void test_WXRefund_case1() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1:退整数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        //支付
        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(13);
        RetailTrade wxMicroPayRT = (RetailTrade) retailTrade.clone();
        wxMicroPayRT.setAmount(WXPayUtil.formatAmount(wxMicroPayRT.getAmount()));
        wxMicroPay(wxMicroPayRT);
        //退款
        RetailTrade wxRefundRT = (RetailTrade) retailTrade.clone();
        wxRefundRT.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        wxRefundRT.setWxTradeNO(microPayResponse.get("out_trade_no"));
        wxRefundRT.setWxRefundDesc(microPayResponse.get("refund_desc"));
        wxRefundRT.setWxOrderSN(microPayResponse.get("transaction_id"));
        wxRefundRT.setAmount(WXPayUtil.formatAmount(retailTrade.getAmount()));
        wxRefund(wxRefundRT);
        //
        Assert.assertTrue("微信退款时转换的金额影响到了原零售单的金额", wxRefundRT.getAmount() != retailTrade.getAmount());
    }

    @Test
    public void test_WXRefund_case2() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2:退带有俩位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        //支付
        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.22);
        RetailTrade wxMicroPayRT = (RetailTrade) retailTrade.clone();
        wxMicroPayRT.setAmount(WXPayUtil.formatAmount(wxMicroPayRT.getAmount()));
        wxMicroPay(wxMicroPayRT);
        //退款
        RetailTrade wxRefundRT = (RetailTrade) retailTrade.clone();
        wxRefundRT.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        wxRefundRT.setWxTradeNO(microPayResponse.get("out_trade_no"));
        wxRefundRT.setWxRefundDesc(microPayResponse.get("refund_desc"));
        wxRefundRT.setWxOrderSN(microPayResponse.get("transaction_id"));
        wxRefundRT.setAmount(WXPayUtil.formatAmount(retailTrade.getAmount()));
        wxRefund(wxRefundRT);
        //
        Assert.assertTrue("微信退款时转换的金额影响到了原零售单的金额", wxRefundRT.getAmount() != retailTrade.getAmount());
    }

    @Test
    public void test_WXRefund_case3() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3:退带有三位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        //支付
        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.223);
        RetailTrade wxMicroPayRT = (RetailTrade) retailTrade.clone();
        wxMicroPayRT.setAmount(WXPayUtil.formatAmount(wxMicroPayRT.getAmount()));
        wxMicroPay(wxMicroPayRT);
        //退款
        RetailTrade wxRefundRT = (RetailTrade) retailTrade.clone();
        wxRefundRT.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        wxRefundRT.setWxTradeNO(microPayResponse.get("out_trade_no"));
        wxRefundRT.setWxRefundDesc(microPayResponse.get("refund_desc"));
        wxRefundRT.setWxOrderSN(microPayResponse.get("transaction_id"));
        wxRefundRT.setAmount(WXPayUtil.formatAmount(retailTrade.getAmount()));
        wxRefund(wxRefundRT);
        //
        Assert.assertTrue("微信退款时转换的金额影响到了原零售单的金额", wxRefundRT.getAmount() != retailTrade.getAmount());

    }

    @Test
    public void test_WXRefund_case4() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case4:退带有六位小数金额");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        //支付
        RetailTrade retailTrade = getRetailTrade();
        retailTrade.setAmount(12.225655);
        RetailTrade wxMicroPayRT = (RetailTrade) retailTrade.clone();
        wxMicroPayRT.setAmount(WXPayUtil.formatAmount(wxMicroPayRT.getAmount()));
        wxMicroPay(wxMicroPayRT);
        //退款
        RetailTrade wxRefundRT = (RetailTrade) retailTrade.clone();
        wxRefundRT.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        wxRefundRT.setWxTradeNO(microPayResponse.get("out_trade_no"));
        wxRefundRT.setWxRefundDesc(microPayResponse.get("refund_desc"));
        wxRefundRT.setWxOrderSN(microPayResponse.get("transaction_id"));
        wxRefundRT.setAmount(WXPayUtil.formatAmount(retailTrade.getAmount()));
        wxRefund(wxRefundRT);
        //
        Assert.assertTrue("微信退款时转换的金额影响到了原零售单的金额", wxRefundRT.getAmount() != retailTrade.getAmount());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == Event_ID_WXPaySIT) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                    event.setRequestType(null);
                    Ntp ntp = (Ntp) event.getBaseModel1();
                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
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



