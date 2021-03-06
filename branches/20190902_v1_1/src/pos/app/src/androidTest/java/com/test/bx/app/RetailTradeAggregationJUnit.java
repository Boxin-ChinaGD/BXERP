package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class RetailTradeAggregationJUnit extends BaseHttpAndroidTestCase {
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
//    private static RetailTradeHttpBO retailTradeHttpBO = null;
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
//    private static RetailTradeHttpEvent retailTradeHttpEvent = null;

    private static final int EVENT_ID_RetailTradeAggregationJUnit = 10000;

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
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();

//        retailTradeSQLiteBO = GlobalController.getInstance().getRetailTradeSQLiteBO();
//        retailTradeHttpBO = GlobalController.getInstance().getRetailTradeHttpBO();
//        retailTradeSQLiteEvent = GlobalController.getInstance().getRetailTradeSQLiteEvent();
//        retailTradeHttpEvent = GlobalController.getInstance().getRetailTradeHttpEvent();
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
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

    public static class DataInput {
        private static RetailTradeAggregation retailTradeAggregation = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException {
            retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation == null ? 1 : BaseActivity.retailTradeAggregation.getStaffID());
            retailTradeAggregation.setPosID(1);
            retailTradeAggregation.setTradeNO(10);
            retailTradeAggregation.setAmount(100);
            retailTradeAggregation.setReserveAmount(20);
            retailTradeAggregation.setCashAmount(10);
            retailTradeAggregation.setWechatAmount(20);
            retailTradeAggregation.setAlipayAmount(30);
            retailTradeAggregation.setWorkTimeStart(new Date());
            retailTradeAggregation.setWorkTimeEnd(new Date());
            retailTradeAggregation.setAmount1(1l);
            retailTradeAggregation.setAmount2(20l);
            retailTradeAggregation.setAmount3(3l);
            retailTradeAggregation.setAmount4(40l);
            retailTradeAggregation.setAmount5(5l);
            //

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationJUnit.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeAggregationJUnit.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationJUnit.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeAggregationJUnit.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }


    @Test
    public void test_a_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //case1:??????????????????????????????TradeNO??????0
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setTradeNO(-1);
        retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("????????????????????????", lTimeOut > 0);
        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //case2???????????????????????????????????????????????????????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), -1));
        //
        retailTradeAggregationHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("????????????????????????", lTimeOut > 0);
//        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField); //...
        Assert.assertTrue("?????????????????????", retailTradeAggregationHttpEvent.getLastErrorMessage().equals("????????????????????????????????????"));
    }

    @Test
    public void test_c_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //case3:??????????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setReserveAmount(-1);
        Assert.assertTrue("?????????????????????????????????????????????????????????", retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation) == false);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("????????????????????????", lTimeOut > 0);
        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //case4:????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // ??????????????????????????????
        Assert.assertTrue("?????????????????????????????????????????????????????????", retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation) == true);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("????????????????????????", lTimeOut > 0);
        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e_createRetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();
        // case5:????????????????????????????????????????????????????????????pos????????????????????????????????????????????????Pos????????????????????????????????????Pos?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Pos???
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        long maxID = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("presenter.getMaxId()?????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        retailTradeAggregation.setID(maxID);
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // ??????????????????????????????
        //
        RetailTradeAggregation rtaInSQLite = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("????????????????????????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && rtaInSQLite != null);
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadRetailTradeAggregationTempData(rtaInSQLite, retailTradeAggregationSQLiteBO);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            if ((retailTradeAggregationSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//????????????Presenter???????????????????????????event????????????????????????????????????????????????????????????
                    || (retailTradeAggregationHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeAggregationHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lTimeOut < 0) {
            Assert.assertTrue("????????????", false);
        }
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????", false);
        }
        // ????????????
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadRetailTradeAggregationTempData(rtaInSQLite, retailTradeAggregationSQLiteBO); //rtaInSQLite????????????????????????????????????????????????SQLite???
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            if ((retailTradeAggregationSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//????????????Presenter???????????????????????????event????????????????????????????????????????????????????????????
                    || (retailTradeAggregationHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeAggregationHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lTimeOut < 0) {
            Assert.assertTrue("????????????", false);
        }
        if (retailTradeAggregationHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
            Assert.assertTrue("????????????????????????????????????????????????????????????EC_Duplicated", false);
        }
 /*       System.out.println(retailTradeAggregationSQLiteEvent.getLastErrorCode());
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) {
            Assert.assertTrue("?????????????????????SQLite?????????????????????", false);
        }*/

        // ?????????????????????????????????retailTradeAggregationHttpEvent.getBaseModel1()??????????????????????????????
        RetailTradeAggregation retailT = (RetailTradeAggregation) retailTradeAggregationHttpEvent.getBaseModel2();
        retailT.setIgnoreIDInComparision(true);
        Assert.assertTrue("?????????????????????????????????????????????", retailT.compareTo(rtaInSQLite) == 0);
    }
}
