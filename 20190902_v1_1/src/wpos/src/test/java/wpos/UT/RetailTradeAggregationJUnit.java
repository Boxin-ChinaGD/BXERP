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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationJUnit.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeAggregationJUnit.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationJUnit.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeAggregationJUnit.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    @Test
    public void test_a_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //case1:上传到服务器的对象的TradeNO小于0
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setTradeNO(-1);
        retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "上传收银汇总超时");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "错误码不正确");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //case2：上传到服务器的对象，上班时间比下班时间晚
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
        Assert.assertTrue(lTimeOut > 0, "上传收银汇总超时");
//        Assert.assertTrue("错误码不正确", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "错误码不正确"); //...
        Assert.assertEquals(retailTradeAggregationHttpEvent.getLastErrorMessage(), "上班时间不能比下班时间晚", "错误信息不正确");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_c_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //case3:准备金为负数
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setReserveAmount(-1);
        Assert.assertFalse(retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation), "创建成功，预期为失败！字段检验不通过！");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "上传收银汇总超时");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "错误码不正确");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_d_createRetailTradeAggregation() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //case4:正常上传
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // 将上班时间设置为昨天
        Assert.assertTrue(retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation), "创建成功，预期为失败！字段检验不通过！");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done//
                && retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "上传收银汇总超时");
        Assert.assertSame(retailTradeAggregationHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_e_createRetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();
        // case5:新增一个收银汇总，在上传到服务器的时候，pos端断网，服务器创建收银汇总成功，Pos没有接收到返回信息。之后Pos端恢复网络，再次上传该收银汇总，此时服务器提示已经存在此收银汇总，直接返回该收银汇总给Pos端
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        int maxID = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeAggregation.class);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()失败！");
        retailTradeAggregation.setID(maxID);
        retailTradeAggregation.setWorkTimeStart(DatetimeUtil.getDays(new Date(), -1)); // 将上班时间设置为昨天
        //
        RetailTradeAggregation rtaInSQLite = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && rtaInSQLite != null, "创建临时的收银汇总失败！");
        // 上传本地临时的收银汇总，向服务器请求创建，会返回结果自动更新本地
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadRetailTradeAggregationTempData(rtaInSQLite, retailTradeAggregationSQLiteBO);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            if ((retailTradeAggregationSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
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
            Assert.fail("上传超时");
        }
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.fail("上传失败");
        }
        // 重复上传
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadRetailTradeAggregationTempData(rtaInSQLite, retailTradeAggregationSQLiteBO); //rtaInSQLite在内存仍然存在，但是已经不存在于SQLite中
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            if ((retailTradeAggregationSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
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
            Assert.fail("上传超时");
        }
        if (retailTradeAggregationHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
            Assert.fail("重复上传收银汇总，服务器返回的错误码不是EC_Duplicated");
        }
 /*       System.out.println(retailTradeAggregationSQLiteEvent.getLastErrorCode());
        if (retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) {
            Assert.assertTrue("重复上传并更新SQLite没导致主键冲突", false);
        }*/

        // 获取服务器传来的数据。retailTradeAggregationHttpEvent.getBaseModel1()是上传到服务器的数据
        RetailTradeAggregation retailT = (RetailTradeAggregation) retailTradeAggregationHttpEvent.getBaseModel2();
        retailT.setIgnoreIDInComparision(true);
        Assert.assertEquals(retailT.compareTo(rtaInSQLite), 0, "服务器并没有返回重复的收银汇总");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }
}
