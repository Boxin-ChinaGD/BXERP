package wpos.SIT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarcodesSIT extends BaseHttpTestCase {
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesSIT = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesSIT);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (sqLiteEvent == null) {
            sqLiteEvent = new BarcodesSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (httpEvent == null) {
            httpEvent = new BarcodesHttpEvent();
            httpEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(sqLiteEvent, httpEvent);
            sqLiteBO.setBarcodesPresenter(barcodesPresenter);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(sqLiteEvent, httpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);

        logoutHttpEvent.setId(EVENT_ID_BarcodesSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {

        protected static Barcodes getBarcodesInput() throws CloneNotSupportedException, ParseException {
            Barcodes barcodesInput = new Barcodes();
            barcodesInput.setBarcode(String.valueOf((int) (Math.random() * 10000000) + 10000000));
            barcodesInput.setCommodityID(7);
            barcodesInput.setSyncType("C");
            barcodesInput.setInt1(1);
            barcodesInput.setOperatorStaffID(1);
            barcodesInput.setSyncDatetime(Constants.getDefaultSyncDatetime());

            return (Barcodes) barcodesInput.clone();
        }

        protected static List<BaseModel> getBarcodesList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> barcodesList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                barcodesList.add(getBarcodesInput());
            }
            return barcodesList;
        }
    }

    @Test
    public void testBarcodes() throws InterruptedException, CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1,2步，登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        Barcodes barcodes = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.fail("创建失败!");
        }
        Thread.sleep(10 * 1000);

        long lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("请求服务器创建Barcodes超时!");
        }

        //RN前登陆另一台POS，使其有同步数据
        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("退出登录失败! ");
        }
        lTimeOut = 50;
        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("退出登录超时!");
        }
        //
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.fail("同步失败");
        }

        lTimeOut = 50;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("Barcodes同步超时!");
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(barcodesList.size() > 0, "ListMasterTable为空");
        System.out.println("第一次RN后同步了的barcodesList为：" + barcodesList);
        String barcodesIDs = "";
        for (Barcodes value : barcodesList) {
            barcodesIDs = barcodesIDs + "," + value.getID();
        }
        barcodesIDs = barcodesIDs.substring(1);
        System.out.println("把barcodesList分割后得到的barcodesIDs = " + barcodesIDs);

        //6.调用feedbackEx。
        System.out.println("第一次调用feedback方法");
        if (!httpBO.feedback(barcodesIDs)) {
            Assert.fail("同步失败");
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("Barcodes Feedback超时!");
        }

//        7.调用RN（无同步数据）
        System.out.println("第二次调用RN方法");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.fail("同步失败");
        }
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("Barcodes同步超时!");
        }

//        8.模拟网页创建一个Barcodes
        Barcodes barcodes2 = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, barcodes2)) {
            Assert.fail("创建失败!");
        }

        lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("请求服务器创建barcodes超时!");
        }


//        9.退出登录
        if (!logoutHttpBO.logoutAsync()) {
            Assert.fail("退出登录失败! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("创建超时!");
        }
        //
        System.out.println(logoutHttpBO.getHttpEvent().getLastErrorCode());
        Assert.assertSame(logoutHttpBO.getHttpEvent().getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "退出登录,服务器返回的错误码不正确");
    }

}
