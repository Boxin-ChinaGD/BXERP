package wpos.UT;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.RetailTradePromotingHttpBO;
import wpos.bo.RetailTradePromotingSQLiteBO;
import wpos.event.*;
import wpos.listener.Subscribe;
import wpos.model.RetailTradePromoting;
import wpos.model.RetailTradePromotingFlow;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RetailTradePromotingJUnitTest extends BaseHttpTestCase {
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;

    private static final int EVENT_ID_RetailTradePromotingJUnitTest = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();

        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(EVENT_ID_RetailTradePromotingJUnitTest);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(EVENT_ID_RetailTradePromotingJUnitTest);
        }
        if (retailTradePromotingSQLiteBO == null) {
            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        if (retailTradePromotingHttpBO == null) {
            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);

        logoutHttpEvent.setId(EVENT_ID_RetailTradePromotingJUnitTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradePromotingJUnitTest  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradePromotingJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static RetailTradePromoting retailTradePromotingInput = null;
        private static Random r = new Random();

        protected static final RetailTradePromoting getRetailTradePromotingInput() throws CloneNotSupportedException {

            retailTradePromotingInput = new RetailTradePromoting();
            retailTradePromotingInput.setTradeID(1);
            List<RetailTradePromotingFlow> list = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
                rtpf.setProcessFlow(UUID.randomUUID().toString());
                rtpf.setPromotionID((i + 1));
                list.add(rtpf);
            }
            retailTradePromotingInput.setListSlave1(list);
            return (RetailTradePromoting) retailTradePromotingInput.clone();
        }
    }

    @Test
    public void test_a_CreateEx() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        SyncThread.aiPause.set(SyncThread.PAUSE);
//        //1,2步，登录POS和Staff
//        Shared.login(1l);
//
//        //暂停同步线程
//        SyncThread.aiPause.set(SyncThread.PAUSE);
//
//        //4.模拟网页创建一个RetailTradePromoting
//        System.out.println("第一次调用create方法");
//        RetailTradePromoting retailTradePromotingCreate = RetailTradePromotingJUnitTest.DataInput.getRetailTradePromotingInput();
//        if (!retailTradePromotingHttpBO.createAsyncC(retailTradePromotingCreate)) {
//            Assert.assertTrue("创建失败!", false);
//        }
//
//        long lTimeOut = 30;
//        while (retailTradePromotingSqLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (retailTradePromotingSqLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("请求服务器创建RetailTradePromoting超时!", false);
//        }
//        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
//        retailTradePromoting.setSql("WHERE F_TradeID = ?");
//        retailTradePromoting.setConditions(new String[]{String.valueOf(retailTradePromotingCreate.getTradeID())});
//        List<RetailTradePromoting> rt = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting);
//        Assert.assertNotNull(rt.get(0));
//        rt.get(0).setIgnoreIDInComparision(true);
//        Assert.assertTrue("两个对象不相等!",rt.get(0).compareTo(retailTradePromotingCreate) == 0);
    }
}
