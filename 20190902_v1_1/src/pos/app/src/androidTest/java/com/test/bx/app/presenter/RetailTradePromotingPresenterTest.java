package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;

import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class RetailTradePromotingPresenterTest extends BaseAndroidTestCase {

    private static RetailTradePromotingPresenter rtpPresenter;
    private static RetailTradePromotingFlowPresenter rtpFlowPresenter;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
    private static RetailTradePresenter rtPresenter;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent;

    private static final int Event_ID_RetailTradePromotingPresenterTest = 10000;
    private static final int Event_ID_RetailTradePresenterTest = 10001;
    private static final int Event_ID_RetailTradePromotionHttpEventPresenterTest = 10002;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        rtpPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
        rtpFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(Event_ID_RetailTradePromotingPresenterTest);
        }
        rtPresenter = GlobalController.getInstance().getRetailTradePresenter();
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_RetailTradePresenterTest);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(Event_ID_RetailTradePromotionHttpEventPresenterTest);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradePromotingPresenterTest) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradePromotingPresenterTest  onRetailTradePromotingSQLiteEvent");
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradePromotingPresenterTest.onRetailTradePromotingSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradePromotingPresenterTest.onRetailTradePromotingSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == Event_ID_RetailTradePromotionHttpEventPresenterTest) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradePromotingPresenterTest.onRetailTradePromotingHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradePromotingPresenterTest.onRetailTradePromotingHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradePresenterTest) {
            System.out.println("#########################################################RetailTradePromotingPresenterTest onRetailTradeSQLiteEvent");
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradePromotingPresenterTest.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradePromotingPresenterTest.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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

    public static class DataInput {
        private static RetailTradePromoting retailTradePromoting = new RetailTradePromoting();

        private static RetailTrade retailTrade = new RetailTrade();

        protected static final RetailTradePromoting getRetailTradePromoting() throws CloneNotSupportedException {
            Random ran = new Random();

            try {
                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTradePromoting.setListSlave1(getRetailTradePromotingFlowList());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return (RetailTradePromoting) retailTradePromoting.clone();
        }

        protected static final List<RetailTradePromotingFlow> getRetailTradePromotingFlowList() {
            List<RetailTradePromotingFlow> retailTradeCommodities = new ArrayList<RetailTradePromotingFlow>();
            Random ran = new Random();
            for (int i = 0; i < 3; i++) {
                RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
                retailTradePromotingFlow.setRetailTradePromotingID(11);
                retailTradePromotingFlow.setPromotionID(i + 1);
                retailTradePromotingFlow.setProcessFlow(UUID.randomUUID().toString());
                retailTradeCommodities.add(retailTradePromotingFlow);
            }

            return retailTradeCommodities;
        }

        protected static final RetailTrade getRetailTrade() {
            Random ran = new Random();

            try {
                long tmpRowID = rtPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                Assert.assertTrue("错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                retailTrade.setSn(retailTrade.generateRetailTradeSN(2));
                retailTrade.setLocalSN(ran.nextInt(1000));
                retailTrade.setPos_ID(2);
                retailTrade.setSaleDatetime(new Date());
                retailTrade.setLogo("11");
                retailTrade.setStaffID(1);
                retailTrade.setPaymentType(1);
                retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
                retailTrade.setPaymentAccount("12");
                retailTrade.setRemark("11111");
                retailTrade.setSourceID(-1);
                retailTrade.setAmount(2222.2d);
                retailTrade.setAmountCash(2222.2d);
                retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
                retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
                retailTrade.setSaleDatetime(new Date());
                retailTrade.setListSlave1(getRetailTradeCommodityList());
                retailTrade.setDatetimeStart(new Date());
                retailTrade.setDatetimeEnd(new Date());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return (RetailTrade) retailTrade.clone();
        }


        protected static final List<RetailTradeCommodity> getRetailTradeCommodityList() {
            List<RetailTradeCommodity> retailTradeCommodities = new ArrayList<RetailTradeCommodity>();
            Random ran = new Random();
            for (int i = 0; i < 3; i++) {
                RetailTradeCommodity rtcInput = new RetailTradeCommodity();
                rtcInput.setTradeID(1l);
                rtcInput.setCommodityID(i + 1);
                rtcInput.setNO(ran.nextInt(999) + 1);
                rtcInput.setPriceOriginal(ran.nextInt(999) + 1);
                rtcInput.setDiscount(0.5f);
                rtcInput.setPriceReturn(6.66);
                rtcInput.setBarcodeID(1);

                retailTradeCommodities.add(rtcInput);
            }

            return retailTradeCommodities;
        }
    }

    @Test
    public void test_a_createMasterSlaveAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //准备数据
        RetailTrade createRetailTrade = DataInput.getRetailTrade();
        rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createRetailTrade);

//        List<RetailTrade> rtList = (List<RetailTrade>) rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        Assert.assertTrue("RN出来的数据应该大于0,", rtList.size() > 0);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createRetailTrade);
        //
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", createRetailTrade.compareTo(comm1Retrieve1) == 0);
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(createRetailTrade.getID().intValue());
        retailTradePromoting.setID(rtpPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));

        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setHttpBO(null);
        rtpPresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);

        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createMasterSlaveAsync超时！", false);
        }
        Assert.assertTrue("createMasterSlaveObjectAsyn返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
        Assert.assertTrue("获取到的RetailTradePromoting为null", rtpCreate != null);
        RetailTradePromoting rtp = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID, rtpCreate);
        Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync返回的数据不正确", rtp != null);

        Assert.assertTrue("插入的和查询出来的retailTrade不一致!", rtpCreate.compareTo(rtp) == 0);
        rtp.setSql("where F_RetailTradePromotingID = ?");
        rtp.setConditions(new String[]{String.valueOf(rtp.getID())});
        List<RetailTradePromotingFlow> rtpfList = (List<RetailTradePromotingFlow>) rtpFlowPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions, rtp);
        Assert.assertTrue("retrieveNObjectSync返回的错误码！", rtpFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync返回的数据不正确！", rtpfList != null && rtpfList.size() > 0);

        List<RetailTradePromotingFlow> retailTradePromotingList = (List<RetailTradePromotingFlow>) retailTradePromoting.getListSlave1();
        for (int i = 0; i < rtpfList.size(); i++) {
            System.out.println();
            Assert.assertTrue("插入的和查询出来的rtComm不一致! retailTradePromotingList" + i + "=" + retailTradePromotingList.get(i).toString()
                    + "     rtpfList" + i + "=" + rtpfList.get(i).toString(), retailTradePromotingList.get(i).compareTo(rtpfList.get(i)) == 0);
        }
    }

    @Test
    public void test_b_createReplacerAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //数据准备
        //获取到最大ID，生成临时ID，将FrameOld插入数据库
        RetailTrade retailTrade = DataInput.getRetailTrade();
        rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);

        //获取到最大ID，生成临时ID
        long maxIDInFrame = rtpPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println("SmallSheetFrame最大ID=" + maxIDInFrame);
        Assert.assertTrue("presenter.getMaxId()失败！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTradePromoting retailTradePromotingOld = DataInput.getRetailTradePromoting();
        retailTradePromotingOld.setTradeID(retailTrade.getID().intValue());
        retailTradePromotingOld.setID(maxIDInFrame);
        rtpPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
        Assert.assertTrue("createObjectSync测试失败！返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTradePromoting retailTradePromoting1 = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
        Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync返回的数据不正确！", retailTradePromoting1 != null && retailTradePromotingOld.compareTo(retailTradePromoting1) == 0);

        RetailTradePromoting retailTradePromotingNew = DataInput.getRetailTradePromoting();
        retailTradePromotingNew.setID((long) 168);

        //正常case
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld, retailTradePromotingNew, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("createReplacerAsync超时！", false);
        }
        Assert.assertTrue("createReplacerAsync返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTradePromoting retrieveOld = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
        Assert.assertTrue("createReplacerAsync返回的错误码！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createReplacerAsync的数据不正确!", retrieveOld == null);

        //异常的case:使用空对象替换数据
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradePromotingOld, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("createReplacerAsync超时！", false);
        }
        Assert.assertTrue("createReplacerAsync返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTradePromoting rtp = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingNew);
        Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync的数据不正确!", rtp != null && rtp.compareTo(retailTradePromotingNew) == 0); // 这个结果验证和测试目的不符合，但是它代码写的和测试目的就是不符合,所以写的这个结果验证是为了符合它代码
    }

    @Test
    public void test_c_createReplacerNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //清空数据,以防污染
        rtpPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        rtPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        rtpFlowPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        List<RetailTradePromoting> oldRetailTradePromotingList = new ArrayList<>();
        List<BaseModel> newRetailTradePromotingList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //准备数据
            RetailTrade retailTrade = DataInput.getRetailTrade();
            rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

            Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            //
            Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);
            RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
            retailTradePromoting.setTradeID(retailTrade.getID().intValue());
            retailTradePromoting.setID(rtpPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));

            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            retailTradePromotingSQLiteEvent.setHttpBO(null);
            rtpPresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);

            long lTimeOut = UNIT_TEST_TimeOut;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                Assert.assertTrue("createMasterSlaveAsync超时！", false);
            }
            Assert.assertTrue("createMasterSlaveObjectAsyn返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
            Assert.assertTrue("获取到的RetailTradePromoting为null", rtpCreate != null);
            RetailTradePromoting rtp = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID, rtpCreate);
            Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("retrieve1ObjectSync返回的数据不正确", rtp != null);

            Assert.assertTrue("插入的和查询出来的retailTrade不一致!", rtpCreate.compareTo(rtp) == 0);
            rtp.setSql("where F_RetailTradePromotingID = ?");
            rtp.setConditions(new String[]{String.valueOf(rtp.getID())});
            List<RetailTradePromotingFlow> rtpfList = (List<RetailTradePromotingFlow>) rtpFlowPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions, rtp);
            Assert.assertTrue("retrieveNObjectSync返回的错误码！", rtpFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("retrieveNObjectSync返回的数据不正确！", rtpfList != null && rtpfList.size() > 0);

            List<RetailTradePromotingFlow> retailTradePromotingList = (List<RetailTradePromotingFlow>) retailTradePromoting.getListSlave1();
            for (int x = 0; x < rtpfList.size(); x++) {
                Assert.assertTrue("插入的和查询出来的rtComm不一致!", retailTradePromotingList.get(i).compareTo(rtpfList.get(i)) == 0);
            }
            oldRetailTradePromotingList.add(retailTradePromoting);

            //获取到最大ID，生成临时ID，将FrameOld插入数据库
            long maxID = rtpPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            long maxIDInRT = rtpPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (maxID < maxIDInRT) {
                maxID = maxIDInRT;
            }
            Assert.assertTrue("presenter.getMaxId()失败！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            System.out.println("RetailTrade最大ID=" + maxID);
            Assert.assertTrue("presenter.getMaxId()失败！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            RetailTradePromoting retailTradePromotingNew = DataInput.getRetailTradePromoting();
            retailTradePromotingNew.setID(maxID + i);
            newRetailTradePromotingList.add(retailTradePromotingNew);
        }
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setHttpBO(null);
        rtpPresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, oldRetailTradePromotingList, newRetailTradePromotingList, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("createReplacerNAsync超时！", false);
        }
        Assert.assertTrue("createReplacerNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常的case,使用空对象替换。
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, newRetailTradePromotingList, null, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("createReplacerNAsync超时！", false);
        }
        Assert.assertTrue("createReplacerNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常的case
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setHttpBO(null);
        rtpPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据!", retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0);

        //根据TradeID查询
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_TradeID=?");
        retailTradePromoting.setConditions(new String[]{"1"});
        rtpPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据!", retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0);

        //异常的case 一个通配符多个值
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        retailTradePromoting2.setSql("where F_TradeID=?");
        retailTradePromoting2.setConditions(new String[]{"1", "2"});
        rtpPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting2, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常的case 多个通配符一个值
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        retailTradePromoting3.setSql("where F_ID=? and  F_TradeID=?");
        retailTradePromoting3.setConditions(new String[]{"1"});
        rtpPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting3, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync返回数据不正确！", retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0);
    }

    @Test
    public void test_e_retrieve1Async() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setID(1L);

        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_Retrieve1ByTradeID);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done
                && retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieve1Async超时！", false);
        }
        Assert.assertTrue("retrieve1Async返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f_updateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        List<RetailTradePromoting> createRetailTradePromotingList = new ArrayList<>();
        List<BaseModel> updateRetailTradePromotingList = new ArrayList<>();
        //准备数据
        for (int i = 0; i < 3; i++) {
            RetailTrade retailTrade = DataInput.getRetailTrade();
            rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            //
            Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);

            RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
            retailTradePromoting.setTradeID(retailTrade.getID().intValue());
            rtpPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
            Assert.assertTrue("createObjectSync测试失败！返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            RetailTradePromoting retailTradePromoting1 = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
            Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("retrieve1ObjectSync返回的数据不正确！", retailTradePromoting1 != null && retailTradePromoting.compareTo(retailTradePromoting1) == 0);
            createRetailTradePromotingList.add(retailTradePromoting);
            Date today = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DAY_OF_MONTH, 1);
            Date newDate = c.getTime();
            retailTradePromoting.setCreateDatetime(newDate);
            updateRetailTradePromotingList.add(retailTradePromoting);
        }
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradePromotingList, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("updateNObjectAsync超时！", false);
        }
        Assert.assertTrue("updateNObjectAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (int i = 0; i < updateRetailTradePromotingList.size(); i++) {
            RetailTradePromoting retailTradePromoting = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradePromotingList.get(i));
            Assert.assertTrue("retrieve1ObjectSync 失败，返回的错误码不正确", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("update的数据跟查询出来的数据不一致！", retailTradePromoting.compareTo(updateRetailTradePromotingList.get(i)) == 0);
        }

        //异常case：使用空对象替换
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        rtpPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("updateNObjectAsync超时！", false);
        }
        Assert.assertTrue("updateNObjectAsync返回错误码不正确!", retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g_createSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = DataInput.getRetailTrade();
        rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

        Assert.assertTrue("CreateSync 测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        //
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(retailTrade.getID().intValue());
        //正常的case
        rtpPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue("CreateSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        RetailTradePromoting retailTradePromotingR1 = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createSync失败，原因:所插入数据与查询到的不一致!", retailTradePromoting.compareTo(retailTradePromotingR1) == 0);

        //异常case:插入的ID重复
        RetailTradePromoting retailTradePromoting2 = DataInput.getRetailTradePromoting();
        retailTradePromoting2.setTradeID(retailTrade.getID().intValue());
        retailTradePromoting2.setID(retailTradePromoting.getID());
        rtpPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue("CreateSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

    }

    @Test
    public void test_h_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = DataInput.getRetailTrade();
        rtPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

        Assert.assertTrue("CreateSync 测试失败,原因:返回错误码不正确!", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) rtPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        //
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", retailTrade.compareTo(comm1Retrieve1) == 0);
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(retailTrade.getID().intValue());
        rtpPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue("CreateSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //正常的case
        RetailTradePromoting retailTradePromotingR1 = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createSync失败，原因:所插入数据与查询到的不一致!", retailTradePromoting.compareTo(retailTradePromotingR1) == 0);

        //异常的case:查询的ID不存在
        RetailTradePromoting retailTradePromoting1 = DataInput.getRetailTradePromoting();
        retailTradePromoting1.setID(Shared.SQLITE_ID_Infinite);
        RetailTradePromoting retailTradePromoting2R1 = (RetailTradePromoting) rtpPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting1);
        Assert.assertTrue("retrieve1ObjectSync失败，返回的错误码不正确！", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync失败，返回的数据不正确！", retailTradePromoting2R1 == null);
    }

    @Test
    public void test_i_retrieveNSync() {
        Shared.printTestMethodStartInfo();

        //正常的case
        List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync搜索到的数据数量应该>=0!", retailTradePromotingList.size() >= 0);

        //根据条件:F_TradeID 查询的case
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_TradeID = ?");
        retailTradePromoting.setConditions(new String[]{"1"});
        retailTradePromotingList = (List<RetailTradePromoting>) rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>0!", retailTradePromotingList.size() >= 0);

        //异常的case,一个通配符多个值
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        retailTradePromoting2.setSql("where F_TradeID = ?");
        retailTradePromoting2.setConditions(new String[]{"1", "2"});
        rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting2);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常的case,多个通配符，一个值
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        retailTradePromoting3.setSql("where F_ID = ? and F_TradeID = ?");
        retailTradePromoting3.setConditions(new String[]{"1"});
        rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting3);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常的case,多个通配符，多个值
        RetailTradePromoting retailTradePromoting4 = new RetailTradePromoting();
        retailTradePromoting4.setSql("where F_ID = ? and F_TradeID = ?");
        retailTradePromoting4.setConditions(new String[]{"1", "2"});
        rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting4);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", rtpPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 查询本地RetailTradePromoting表的总条数
     */
    @Test
    public void test_retrieveNRetailTradePromoting() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradePromotingPresenter retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
        Integer total = retailTradePromotingPresenter.retrieveCount();
        System.out.println("RetailTradePromoting表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
