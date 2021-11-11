package com.test.bx.app;

import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.sync.SyncThread;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarcodesSIT extends BaseHttpAndroidTestCase {
    private static BarcodesPresenter presenter = null;
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesSIT = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getBarcodesPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesSIT);
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
        if (sqLiteEvent == null) {
            sqLiteEvent = new BarcodesSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (httpEvent == null) {
            httpEvent = new BarcodesHttpEvent();
            httpEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_BarcodesSIT);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
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
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Barcodes barcodesInput = null;

        protected static final Barcodes getBarcodesInput() throws CloneNotSupportedException, ParseException {
            barcodesInput = new Barcodes();
            barcodesInput.setBarcode(String.valueOf((int) (Math.random() * 10000000) + 10000000));
            barcodesInput.setCommodityID(7);
            barcodesInput.setSyncType("C");
            barcodesInput.setInt1(1);
            barcodesInput.setOperatorStaffID(1);
            barcodesInput.setSyncDatetime(Constants.getDefaultSyncDatetime());

            return (Barcodes) barcodesInput.clone();
        }

        protected static final List<BaseModel> getBarcodesList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> barcodesList = new ArrayList<BaseModel>();
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
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //4.模拟网页创建一个Barcodes
        System.out.println("第一次调用create方法");
        Barcodes barcodes = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.assertTrue("创建失败!", false);
        }
        Thread.sleep(10 * 1000);

        long lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Barcodes超时!", false);
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
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("同步失败", false);
        }

        lTimeOut = 50;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Barcodes同步超时!", false);
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("ListMasterTable为空", barcodesList.size() > 0);
        System.out.println("第一次RN后同步了的barcodesList为：" + barcodesList);
        String barcodesIDs = "";
        for (int i = 0; i < barcodesList.size(); i++) {
            barcodesIDs = barcodesIDs + "," + barcodesList.get(i).getID();
        }
        barcodesIDs = barcodesIDs.substring(1, barcodesIDs.length());
        System.out.println("把barcodesList分割后得到的barcodesIDs = " + barcodesIDs);

        //6.调用feedbackEx。
        System.out.println("第一次调用feedback方法");
        if (!httpBO.feedback(barcodesIDs)) {
            Assert.assertTrue("同步失败", false);
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Barcodes Feedback超时!", false);
        }

//        7.调用RN（无同步数据）
        System.out.println("第二次调用RN方法");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("同步失败", false);
        }
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Barcodes同步超时!", false);
        }

//        8.模拟网页创建一个Barcodes
        Barcodes barcodes2 = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, barcodes2)) {
            Assert.assertTrue("创建失败!", false);
        }

        lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建barcodes超时!", false);
        }


//        9.退出登录
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue("退出登录失败! ", false);
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("创建超时!", false);
        }
        //
        System.out.println(logoutHttpBO.getHttpEvent().getLastErrorCode());
        Assert.assertTrue("退出登录,服务器返回的错误码不正确", logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

}
