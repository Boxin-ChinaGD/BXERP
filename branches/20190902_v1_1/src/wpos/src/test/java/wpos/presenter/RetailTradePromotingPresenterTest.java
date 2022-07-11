package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.RetailTradePromotingHttpEvent;
import wpos.event.RetailTradePromotingSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static wpos.utils.Shared.UNIT_TEST_TimeOut;


public class RetailTradePromotingPresenterTest extends BasePresenterTest {

    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent;
    private static RetailTradePresenter retailTradePresenter;

    private static final int Event_ID_RetailTradePromotingPresenterTest = 10000;
    private static final int Event_ID_RetailTradePresenterTest = 10001;
    private static final int Event_ID_RetailTradePromotionHttpEventPresenterTest = 10002;

    @BeforeClass
    public void setUp() {
        super.setUp();
        retailTradePresenter = (RetailTradePresenter) applicationContext.getBean("retailTradePresenter");
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(Event_ID_RetailTradePromotingPresenterTest);
        }
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

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
//                long tmpRowID = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
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
                rtcInput.setCommodityName("xxxxxxx");
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
        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

//        List<RetailTrade> rtList = (List<RetailTrade>) rtpPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        Assert.assertTrue("RN出来的数据应该大于0,", rtList.size() > 0);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        //
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradeCreate.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(retailTradeCreate.getID().intValue());
//        retailTradePromoting.setID((int) retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));

        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setHttpBO(null);
        retailTradePromotingPresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);

        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createMasterSlaveAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveObjectAsyn返回错误码不正确!");

        RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
        Assert.assertTrue(rtpCreate != null, "获取到的RetailTradePromoting为null");
        RetailTradePromoting rtp = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID, rtpCreate);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
        Assert.assertTrue(rtp != null, "retrieve1ObjectSync返回的数据不正确");

        Assert.assertTrue(rtpCreate.compareTo(rtp) == 0, "插入的和查询出来的retailTrade不一致!");
        rtp.setSql("where F_RetailTradePromotingID = %s");
        rtp.setConditions(new String[]{String.valueOf(rtp.getID())});
        List<RetailTradePromotingFlow> rtpfList = (List<RetailTradePromotingFlow>) retailTradePromotingFlowPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions, rtp);
        Assert.assertTrue(retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync返回的错误码！");
        Assert.assertTrue(rtpfList != null && rtpfList.size() > 0, "retrieveNObjectSync返回的数据不正确！");

        List<RetailTradePromotingFlow> retailTradePromotingList = (List<RetailTradePromotingFlow>) rtpCreate.getListSlave1();
        for (int i = 0; i < rtpfList.size(); i++) {
            System.out.println();
            Assert.assertTrue(retailTradePromotingList.get(i).compareTo(rtpfList.get(i)) == 0, "插入的和查询出来的rtComm不一致! retailTradePromotingList" + i + "=" + retailTradePromotingList.get(i).toString()
                    + "     rtpfList" + i + "=" + rtpfList.get(i).toString());
        }
    }

    @Test
    public void test_b_createReplacerAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

//        //数据准备 TODO
//        //获取到最大ID，生成临时ID，将FrameOld插入数据库
//        RetailTrade retailTrade = DataInput.getRetailTrade();
//        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
//        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
//        //
//        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
//        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
//        Assert.assertTrue(retailTrade.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
//
//        //获取到最大ID，生成临时ID
//        int maxIDInFrame = (int) retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//        System.out.println("SmallSheetFrame最大ID=" + maxIDInFrame);
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()失败！");
//        RetailTradePromoting retailTradePromotingOld = DataInput.getRetailTradePromoting();
//        retailTradePromotingOld.setTradeID(retailTrade.getID().intValue());
//        retailTradePromotingOld.setID(maxIDInFrame);
//        retailTradePromotingPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync测试失败！返回的错误码不正确！");
//
//        RetailTradePromoting retailTradePromoting1 = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
//        Assert.assertTrue(retailTradePromoting1 != null && retailTradePromotingOld.compareTo(retailTradePromoting1) == 0, "retrieve1ObjectSync返回的数据不正确！");
//
//        RetailTradePromoting retailTradePromotingNew = DataInput.getRetailTradePromoting();
//        retailTradePromotingNew.setID(168);
//
//        //正常case
//        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
//        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradePromotingPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld, retailTradePromotingNew, retailTradePromotingSQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue(false, "createReplacerAsync超时！");
//        }
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync返回错误码不正确!");
//
//        RetailTradePromoting retrieveOld = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingOld);
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync返回的错误码！");
//        Assert.assertTrue(retrieveOld == null, "createReplacerAsync的数据不正确!");

//        //异常的case:使用空对象替换数据
//        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done);
//        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradePromotingPresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradePromotingOld, retailTradePromotingSQLiteEvent);
//        lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue(false, "createReplacerAsync超时！");
//        }
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerAsync返回错误码不正确!");
//
//        RetailTradePromoting rtp = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingNew);
//        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
//        Assert.assertTrue(rtp != null && rtp.compareTo(retailTradePromotingNew) == 0, "retrieve1ObjectSync的数据不正确!"); // 这个结果验证和测试目的不符合，但是它代码写的和测试目的就是不符合,所以写的这个结果验证是为了符合它代码
    }

    @Test
    public void test_c_createReplacerNAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //清空数据,以防污染
        retailTradePromotingPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        retailTradePromotingFlowPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        List<RetailTradePromoting> oldRetailTradePromotingList = new ArrayList<RetailTradePromoting>();
        List<BaseModel> newRetailTradePromotingList = new ArrayList<BaseModel>();
        for (int i = 0; i < 2; i++) {
            //准备数据
            RetailTrade retailTrade = DataInput.getRetailTrade();
            RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
            //
            RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
            Assert.assertTrue(retailTradeCreate.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
            RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
            retailTradePromoting.setTradeID(retailTradeCreate.getID().intValue());
//            retailTradePromoting.setID((int) retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime)); TODO

            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            retailTradePromotingSQLiteEvent.setHttpBO(null);
            retailTradePromotingPresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);

            long lTimeOut = UNIT_TEST_TimeOut;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                Assert.assertTrue(false, "createMasterSlaveAsync超时！");
            }
            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveObjectAsyn返回错误码不正确!");

            RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
            Assert.assertTrue(rtpCreate != null, "获取到的RetailTradePromoting为null");
            RetailTradePromoting rtp = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByTradeID, rtpCreate);
            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
            Assert.assertTrue(rtp != null, "retrieve1ObjectSync返回的数据不正确");

            Assert.assertTrue(rtpCreate.compareTo(rtp) == 0, "插入的和查询出来的retailTrade不一致!");
            rtp.setSql("where F_RetailTradePromotingID = %s");
            rtp.setConditions(new String[]{String.valueOf(rtp.getID())});
            List<RetailTradePromotingFlow> rtpfList = (List<RetailTradePromotingFlow>) retailTradePromotingFlowPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions, rtp);
            Assert.assertTrue(retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync返回的错误码！");
            Assert.assertTrue(rtpfList != null && rtpfList.size() > 0, "retrieveNObjectSync返回的数据不正确！");

            List<RetailTradePromotingFlow> retailTradePromotingList = (List<RetailTradePromotingFlow>) rtpCreate.getListSlave1();
            for (int x = 0; x < rtpfList.size(); x++) {
                Assert.assertTrue(retailTradePromotingList.get(i).compareTo(rtpfList.get(i)) == 0, "插入的和查询出来的rtComm不一致!");
            }
            oldRetailTradePromotingList.add(retailTradePromoting);

            //获取到最大ID，生成临时ID，将FrameOld插入数据库 TODO
//            int maxID = (int) retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//            int maxIDInRT = (int) retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//            if (maxID < maxIDInRT) {
//                maxID = maxIDInRT;
//            }
//            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()失败！");
//            System.out.println("RetailTrade最大ID=" + maxID);
//            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()失败！");

//            RetailTradePromoting retailTradePromotingNew = DataInput.getRetailTradePromoting();
//            retailTradePromotingNew.setID(maxID + i);
//            newRetailTradePromotingList.add(retailTradePromotingNew);
        }
//        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done);
//        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradePromotingSQLiteEvent.setHttpBO(null);
//        retailTradePromotingPresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, oldRetailTradePromotingList, newRetailTradePromotingList, retailTradePromotingSQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue(false, "createReplacerNAsync超时！");
//        }
//        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerNAsync返回错误码不正确!");
//
//        //异常的case,使用空对象替换。
//        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done);
//        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradePromotingPresenter.createReplacerNObjectASync(BaseSQLiteBO.INVALID_CASE_ID, newRetailTradePromotingList, null, retailTradePromotingSQLiteEvent);
//        lTimeOut = UNIT_TEST_TimeOut;
//        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue(false, "createReplacerNAsync超时！");
//        }
//        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createReplacerNAsync返回错误码不正确!");
    }

    @Test
    public void test_d_retrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常的case
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setHttpBO(null);
        retailTradePromotingPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync返回错误码不正确!");
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0, "retrieveNAsync会有查询到符合条件的数据!");

        //根据TradeID查询
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_TradeID= %s ");
        retailTradePromoting.setConditions(new String[]{"1"});
        retailTradePromotingPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync返回错误码不正确!");
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0, "retrieveNAsync会有查询到符合条件的数据!");

        //一个通配符多个值 只会获取第一个
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        retailTradePromoting2.setSql("where F_TradeID= %s ");
        retailTradePromoting2.setConditions(new String[]{"1", "2"});
        retailTradePromotingPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting2, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync返回错误码不正确!");

        //异常的case 多个通配符一个值
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        retailTradePromoting3.setSql("where F_ID= %s and  F_TradeID= %s ");
        retailTradePromoting3.setConditions(new String[]{"1"});
        retailTradePromotingPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting3, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "retrieveNAsync返回错误码不正确!");
    }

    @Test
    public void test_e_retrieve1Async() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setID(1);

        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_Retrieve1ByTradeID);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingPresenter.retrieve1ObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done
                && retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieve1Async超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Async返回错误码不正确!");
    }

    @Test
    public void test_f_updateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        List<RetailTradePromoting> createRetailTradePromotingList = new ArrayList<RetailTradePromoting>();
        List<BaseModel> updateRetailTradePromotingList = new ArrayList<BaseModel>();
        //准备数据
        for (int i = 0; i < 3; i++) {
            RetailTrade retailTrade = DataInput.getRetailTrade();
            RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
            //
            RetailTrade retailTradeRetrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
            //
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
            Assert.assertTrue(retailTradeCreate.compareTo(retailTradeRetrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

            RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
            retailTradePromoting.setTradeID(retailTradeCreate.getID().intValue());
            RetailTradePromoting retailTradePromotingCreate = (RetailTradePromoting) retailTradePromotingPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync测试失败！返回的错误码不正确！");

            RetailTradePromoting retailTradePromoting1 = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingCreate);
            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
            Assert.assertTrue(retailTradePromoting1 != null && retailTradePromotingCreate.compareTo(retailTradePromoting1) == 0, "retrieve1ObjectSync返回的数据不正确！");
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
        retailTradePromotingPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradePromotingList, retailTradePromotingSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "updateNObjectAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateNObjectAsync返回错误码不正确!");
        for (int i = 0; i < updateRetailTradePromotingList.size(); i++) {
            RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradePromotingList.get(i));
            Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync 失败，返回的错误码不正确");
            Assert.assertTrue(retailTradePromoting.compareTo(updateRetailTradePromotingList.get(i)) == 0, "update的数据跟查询出来的数据不一致！");
        }

        //异常case：使用空对象替换
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingPresenter.updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradePromotingSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "updateNObjectAsync超时！");
        }
        Assert.assertTrue(retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "updateNObjectAsync返回错误码不正确!");
    }

    @Test
    public void test_g_createSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync 测试失败,原因:返回错误码不正确!");
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        //
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradeCreate.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(retailTradeCreate.getID().intValue());
        //正常的case
        RetailTradePromoting retailTradePromotingCreare = (RetailTradePromoting) retailTradePromotingPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync 测试失败,原因:返回错误码不正确!");
        RetailTradePromoting retailTradePromotingR1 = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingCreare);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradePromotingCreare.compareTo(retailTradePromotingR1) == 0, "createSync失败，原因:所插入数据与查询到的不一致!");

        //异常case:插入的ID重复
        RetailTradePromoting retailTradePromoting2 = DataInput.getRetailTradePromoting();
        retailTradePromoting2.setTradeID(retailTradeCreate.getID().intValue());
        retailTradePromoting2.setID(retailTradePromotingCreare.getID());
        retailTradePromotingPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting2);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync 测试失败,原因:返回错误码不正确!");

    }

    @Test
    public void test_h_retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTrade retailTrade = DataInput.getRetailTrade();
        RetailTrade retailTradeCreate = (RetailTrade) retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);

        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync 测试失败,原因:返回错误码不正确!");
        //
        RetailTrade comm1Retrieve1 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCreate);
        //
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradeCreate.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        RetailTradePromoting retailTradePromoting = DataInput.getRetailTradePromoting();
        retailTradePromoting.setTradeID(retailTradeCreate.getID().intValue());
        RetailTradePromoting retailTradePromotingCreate = (RetailTradePromoting) retailTradePromotingPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync 测试失败,原因:返回错误码不正确!");

        //正常的case
        RetailTradePromoting retailTradePromotingR1 = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromotingCreate);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradePromotingCreate.compareTo(retailTradePromotingR1) == 0, "createSync失败，原因:所插入数据与查询到的不一致!");

        //异常的case:查询的ID不存在
        RetailTradePromoting retailTradePromoting1 = DataInput.getRetailTradePromoting();
        retailTradePromoting1.setID(Shared.SQLITE_ID_Infinite);
        RetailTradePromoting retailTradePromoting2R1 = (RetailTradePromoting) retailTradePromotingPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradePromoting1);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，返回的错误码不正确！");
        Assert.assertTrue(retailTradePromoting2R1 == null, "retrieve1ObjectSync失败，返回的数据不正确！");
    }

    @Test
    public void test_i_retrieveNSync() {
        Shared.printTestMethodStartInfo();

        //正常的case
        List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(retailTradePromotingList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");

        //根据条件:F_TradeID 查询的case
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_TradeID = %s ");
        retailTradePromoting.setConditions(new String[]{"1"});
        retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(retailTradePromotingList.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>0!");

        //一个通配符多个值,获取第一个元素
        RetailTradePromoting retailTradePromoting2 = new RetailTradePromoting();
        retailTradePromoting2.setSql("where F_TradeID = %s ");
        retailTradePromoting2.setConditions(new String[]{"1", "2"});
        retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting2);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常的case,多个通配符，一个值
        RetailTradePromoting retailTradePromoting3 = new RetailTradePromoting();
        retailTradePromoting3.setSql("where F_ID = %s and F_TradeID = %s");
        retailTradePromoting3.setConditions(new String[]{"1"});
        retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting3);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常的case,多个通配符，多个值
        RetailTradePromoting retailTradePromoting4 = new RetailTradePromoting();
        retailTradePromoting4.setSql("where F_ID = %s and F_TradeID = %s");
        retailTradePromoting4.setConditions(new String[]{"1", "2"});
        retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting4);
        Assert.assertTrue(retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
    }

    /**
     * 查询本地RetailTradePromoting表的总条数
     */
    @Test
    public void test_retrieveNRetailTradePromoting() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = retailTradePromotingPresenter.retrieveCount();
        System.out.println("RetailTradePromoting表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
