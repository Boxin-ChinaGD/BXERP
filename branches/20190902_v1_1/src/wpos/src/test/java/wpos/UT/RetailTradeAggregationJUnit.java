package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allController.BaseController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeAggregation;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.util.Date;

public class RetailTradeAggregationJUnit extends BaseHttpTestCase {
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_RetailTradeAggregationJUnit = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
            retailTradeAggregationSQLiteBO.setRetailTradeAggregationPresenter(retailTradeAggregationPresenter);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
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
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_RetailTradeAggregationJUnit);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static RetailTradeAggregation retailTradeAggregation = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException {
            retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation == null ? 1 : BaseController.retailTradeAggregation.getStaffID());
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
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
        Assert.assertTrue(lTimeOut > 0, "????????????????????????");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "??????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    @Test
    public void test_b_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
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
        Assert.assertTrue(lTimeOut > 0, "????????????????????????");
//        Assert.assertTrue("??????????????????", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "??????????????????"); //...
        Assert.assertEquals(retailTradeAggregationHttpEvent.getLastErrorMessage(), "????????????????????????????????????", "?????????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    @Test
    public void test_c_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //case3:??????????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setReserveAmount(-1);
        Assert.assertFalse(retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation), "?????????????????????????????????????????????????????????");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "????????????????????????");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "??????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    @Test
    public void test_d_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //case4:????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // ??????????????????????????????
        Assert.assertTrue(retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation), "?????????????????????????????????????????????????????????");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "????????????????????????");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "??????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    @Test
    public void test_e_createRetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();
        // case5:????????????????????????????????????????????????????????????pos????????????????????????????????????????????????Pos????????????????????????????????????Pos?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Pos???
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        int maxID = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeAggregation.class);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()?????????");
        retailTradeAggregation.setID(maxID);
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // ??????????????????????????????
        //
        RetailTradeAggregation rtaInSQLite = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && rtaInSQLite != null, "????????????????????????????????????");
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
            Assert.fail("????????????");
        }
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.fail("????????????");
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
            Assert.fail("????????????");
        }
        if (retailTradeAggregationHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
            Assert.fail("????????????????????????????????????????????????????????????EC_Duplicated");
        }
 /*       System.out.println(retailTradeAggregationSQLiteEvent.getLastErrorCode());
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) {
            Assert.assertTrue("?????????????????????SQLite?????????????????????", false);
        }*/

        // ?????????????????????????????????retailTradeAggregationHttpEvent.getBaseModel1()??????????????????????????????
        RetailTradeAggregation retailT = (RetailTradeAggregation) retailTradeAggregationHttpEvent.getBaseModel2();
        retailT.setIgnoreIDInComparision(true);
        Assert.assertEquals(retailT.compareTo(rtaInSQLite), 0, "?????????????????????????????????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }
}
