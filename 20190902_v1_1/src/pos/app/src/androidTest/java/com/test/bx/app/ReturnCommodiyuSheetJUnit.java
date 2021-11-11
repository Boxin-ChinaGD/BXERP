package com.test.bx.app;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.ReturnCommoditySheetHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReturnCommodiyuSheetJUnit extends BaseHttpAndroidTestCase {
    private static ReturnCommoditySheetSQLiteEvent returnCommoditySheetSQLiteEvent = null;
    private static ReturnCommoditySheetHttpEvent returnCommoditySheetHttpEvent = null;
    private static ReturnCommoditySheetHttpBO returnCommoditySheetHttpBO = null;

    private static long returnCommoditySheetID = 0;
    private static CommodityPresenter commodityPresenter = null;
    private static BarcodesPresenter barcodesPresenter = null;
    private static final int EVENT_ID_ReturnCommodiyuSheetJUnit = 10000;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (returnCommoditySheetSQLiteEvent == null) {
            returnCommoditySheetSQLiteEvent = new ReturnCommoditySheetSQLiteEvent();
            returnCommoditySheetSQLiteEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        }
        if (returnCommoditySheetHttpEvent == null) {
            returnCommoditySheetHttpEvent = new ReturnCommoditySheetHttpEvent();
            returnCommoditySheetHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        }
        if (returnCommoditySheetHttpBO == null) {
            returnCommoditySheetHttpBO = new ReturnCommoditySheetHttpBO(GlobalController.getInstance().getContext(), returnCommoditySheetSQLiteEvent, returnCommoditySheetHttpEvent);
        }
        returnCommoditySheetSQLiteEvent.setHttpBO(returnCommoditySheetHttpBO);
        returnCommoditySheetHttpEvent.setHttpBO(returnCommoditySheetHttpBO);

        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
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
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReturnCommoditySheetHttpEvent(ReturnCommoditySheetHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    public static class DataInput {
        public static ReturnCommoditySheet getReturnCommoditySheet() {
            Random random = new Random();

            ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
            returnCommoditySheet.setProviderID(random.nextInt(5) + 1);
            returnCommoditySheet.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            returnCommoditySheet.setCreateDate(new Date());

            return (ReturnCommoditySheet) returnCommoditySheet.clone();
        }

        protected static List<Commodity> getCommodityList() {
            Commodity commodity = new Commodity();
            commodity.setID(1l);
            commodity = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            if (commodity == null && commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                return null;
            }
            commodity.setBarcodeID(1);//条形码ID
            commodity.setNO(new Random().nextInt(100) + 1);

            List<Commodity> commodities = new ArrayList<Commodity>();
            commodities.add(commodity);

            return commodities;
        }
    }

    @Test
    public void test_a_Create() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = DataInput.getReturnCommoditySheet();
        List<Commodity> commList = DataInput.getCommodityList();
        Assert.assertTrue("要退货的商品不能为空，不然无法进行测试！失败的原因可能是pos机没有商品数据", commList != null);
        returnCommoditySheet.setListSlave1(commList);
        returnCommoditySheetHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!returnCommoditySheetHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, returnCommoditySheet)) {
            Assert.assertTrue("创建失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("超时！", false);
        }

        ReturnCommoditySheet returnCommoditySheet1 = (ReturnCommoditySheet) returnCommoditySheetHttpBO.getHttpEvent().getBaseModel1();
        returnCommoditySheet.setIgnoreIDInComparision(true);
        if (returnCommoditySheet.compareTo(returnCommoditySheet1) != 0) {
            Assert.assertTrue("创建的对象和服务器返回的不一致", false);
        }

        returnCommoditySheetID = returnCommoditySheet1.getID();
    }

    @Test
    public void test_b_Approver() throws InterruptedException {
        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        returnCommoditySheet.setID(returnCommoditySheetID);

        if (!returnCommoditySheetHttpBO.approveAsync(returnCommoditySheet)) {
            Assert.assertTrue("审核失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        returnCommoditySheetHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("超时！", false);
        }

        Assert.assertTrue("审核失败！！", returnCommoditySheetHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError));
    }
}
