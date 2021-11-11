package com.test.bx.app;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.StaffSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.utils.MD5Utils;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffSIT extends BaseHttpAndroidTestCase {
    private static StaffPresenter staffPresenter = null;
    private static StaffSQLiteBO staffSqLiteBO = null;
    private static StaffHttpBO staffHttpBO = null;
    private static StaffHttpEvent staffHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private PosSQLiteEvent posSQLiteEvent = null;
    private StaffSQLiteEvent staffSQLiteEvent = null;

    private static StaffSQLiteEvent sqLiteEvent = null;
    private static StaffHttpEvent httpEvent = null;
    private static PosHttpEvent posHttpEvent = null;
    private static NetworkUtils networkUtils = new NetworkUtils();
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static final int EVENT_ID_StaffSIT = 10000;

    private static final String mapKey = "msg";
    private static String syncIDs = "";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (staffPresenter == null) {
            staffPresenter = GlobalController.getInstance().getStaffPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (sqLiteEvent == null) {
            sqLiteEvent = new StaffSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_StaffSIT);
        }
        if (httpEvent == null) {
            httpEvent = new StaffHttpEvent();
            httpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(EVENT_ID_StaffSIT);
        }
        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(EVENT_ID_StaffSIT);
        }
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(EVENT_ID_StaffSIT);
        }
        //
        if (staffSqLiteBO == null) {
            staffSqLiteBO = new StaffSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, staffHttpEvent);
        }
        //
        sqLiteEvent.setSqliteBO(staffSqLiteBO);
        sqLiteEvent.setHttpBO(staffHttpBO);
        httpEvent.setSqliteBO(staffSqLiteBO);
        httpEvent.setHttpBO(staffHttpBO);
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

    public static class DataInput {
        private static Staff staffInput = null;

        protected static final Staff getStaffInput() throws CloneNotSupportedException {
            staffInput = new Staff();
            staffInput.setPhone("15200702" + (int) ((Math.random() * 9 + 1) * 100));
            staffInput.setName(Shared.getStaffName());//
            staffInput.setICID("431024199703241" + (int) ((Math.random() * 9 + 1) * 100)); //
            staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
            staffInput.setPasswordExpireDate(new Date());//
            staffInput.setIsFirstTimeLogin(1); //
            staffInput.setShopID(1);//
            staffInput.setReturnSalt(1);
            staffInput.setDepartmentID(1);//
            staffInput.setSyncDatetime(new Date());
            staffInput.setPasswordInPOS("000000");
            staffInput.setSalt(Shared.getFakedSalt());
            staffInput.setRoleID(1);

            return (Staff) staffInput.clone();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 1.pos和staff登录
     * 2.暂停同步线程
     * 3.自定义一个Staff对象,进行getToken
     * 4.call currentStaff/createEx.bx,令网页创建一个步骤1的Staff, 让POS同步
     * 5.POS STAFF登录
     * 6.发送RN请求, 服务器应该返回刚刚请求创建的Staff
     * 7.RN完成之后进行Feedback
     * 8.feedback之后再进行RN, 没有数据返回
     * 9.重复步骤3 4
     * 10.恢复同步线程
     * 11.同步线程RN staff进行同步
     */
    @Test
    public void test_b_Staff() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.pos getToken,拿到sessionID,用于staff登录,,,自定义一个Staff对象,进行getToken
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //2.暂停同步线程

        //3.自定义一个Staff对象,进行getToken
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff getToken超时!", false);
        }

        //4.call currentStaff/createEx.bx,令网页创建一个Staff, 让POS同步
        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("创建失败!", false);
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Staff超时!", false);
        }

        logOut();
        //5.POS STAFF登录
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //6.发送RN请求, 服务器应该返回刚刚请求创建的Staff
        System.out.println("-------------------------------RN1");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue("staff同步失败!", false);
        }
        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("staff同步超时!", false);
        }
        List<Staff> staffList = (List<Staff>) staffSqLiteBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("同步RN不应该没有数据返回", staffList.size() != 0);

//        //7.RN完成之后进行Feedback
//        System.out.println("-------------------------------Feedback");
//        String staffIDs = "";
//        for (int i = 0; i < staffList.size(); i++) {
//            staffIDs = staffIDs + "," + staffList.get(i).getID();
//        }
//        staffIDs = staffIDs.substring(1, staffIDs.length());
//        staffHttpBO.feedback(staffIDs);
//        //等待处理完毕
//        lTimeOut = 50;
//        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("测试失败！原因：超时", false);
//        }
//
//        //8.feedback之后再进行RN, 没有数据返回
//        System.out.println("-------------------------------RN2");
//        if (!staffHttpBO.retrieveNAsync(null)) {
//            Assert.assertTrue("Staff RN失败!", false);
//        }
//        lTimeOut = 50;
//        staffSqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("Staff RN超时!", false);
//        }
//        List<Staff> staffList1 = (List<Staff>) staffHttpBO.getHttpEvent().getListMasterTable();
//        Assert.assertTrue("第二次Staff RN不应该有数据返回", staffList1.size() == 0);

        logOut();
        //9.重复步骤3 4
        //3.自定义一个Staff对象,进行getToken
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        System.out.println("------------------------第二次getToken");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff2 getToken超时!", false);
        }

        //4.call currentStaff/createEx.bx,令网页创建一个Staff, 让POS同步
        System.out.println("------------------------第二次craete currentStaff");
        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue("创建失败!", false);
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Staff2超时!", false);
        }
    }

    /*
    全部同步
     */
    public void test_c_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //暂停同步线程

        //POS去请求全部同步的Action并更新本地服务器
        Staff s = new Staff();
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        s.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        staffSqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步Staff超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Staff> rtList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSyncC测试失败,原因:返回错误码不正确!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Staff> list = (List<Staff>) staffHttpBO.getHttpEvent().getListMasterTable();
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }

    @Test
    public void test_a_Login() throws Exception {
        //为确保SIT能正确运行，所以先删除Sqlite中的数据。
        staffPresenter.deleteNObjectSync(BaseHttpBO.INVALID_CASE_ID, null);
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
//        Thread.sleep(5 * 1000);
        long lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", NetworkUtils.isNetworkAvalible(this.getContext()));
        //1.新建一个老板账号，此时服务器同存产生。
        //登录POS和Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case1: 创建老板账号 --------------------");
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff getToken超时!", false);
        }

        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("调用Create失败！！", false);
        }

        lTimeOut = 50;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Staff超时!", false);
        }

        //2.老板创建一个员工账号，此时服务器同存产生。
        //登录POS和Staff
        logOut();
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case2: 创建员工账号 --------------------");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff getToken超时!", false);
        }

        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue("调用Create失败！！", false);
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Staff超时!", false);
        }

        Staff employee = (Staff) staffHttpBO.getHttpEvent().getBaseModel1(); //获取到员工的账号。（密码为''）

        //3.pos没联网时，谁登陆都失败(员工登录分为2种：1.有网情况下请求服务器进行登录。2.无网情况下在本地Sqlite中查找数据进行登录)。首次使用POS机，Sqlite中是无staff数据的.

        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", !NetworkUtils.isNetworkAvalible(this.getContext()));

        System.out.println("-------------------- case3: 首次使用pos机，没联网时谁登录都失败 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params = login(employee);
        String value = params.get(mapKey);
        Assert.assertTrue("首次没联网时POS端有数据！", "未找到该用户，请重新输入！！".equals(value));

        //4.pos联网，同步所有的数据（当前应有2条数据）,再进行feedback
        wifiManager.setWifiEnabled(true);
//        Thread.sleep(15 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", NetworkUtils.isNetworkAvalible(this.getContext()));


        //登录POS和Staff
        logOut();
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        System.out.println("-------------------- case4: 同步需要的staff --------------------");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue("调用RetrieveN失败！！", false);
        }

        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步Staff超时!", false);
        }

        Staff staff1 = new Staff();
        staff1.setSql("where F_Phone = ?");
        staff1.setConditions(new String[]{staff.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue("无法找到创建的老板账号！", list.size() > 0);

        staff1.setSql("where F_Phone = ?");
        staff1.setConditions(new String[]{staff2.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue("无法找到创建的员工账号！", list2.size() > 0);

        //5.POS没联网,老板或员工登录，应Sqlite中密码为空串，所以登录失败，提示需要联网。
        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", !NetworkUtils.isNetworkAvalible(this.getContext()));

        System.out.println("-------------------- case5: POS没联网，员工首次登录 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params2 = login(employee);
        String value2 = params2.get(mapKey);
        Assert.assertTrue("无网络且sqlite中密码为空的情况下没提示连接网络进行登录", "请连接网络进行登录！！".equals(value2));

        //6.POS联网,老板或员工登录，请求服务器进行登录。如果登录成功。会将加密的密码写入本地SQLITE中。以便断网时登录
        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", NetworkUtils.isNetworkAvalible(this.getContext()));

        System.out.println("-------------------- case6: POS联网，员工连接服务器进行登录 --------------------");
        employee.setPasswordInPOS("000000");
        Constants.setCurrentStaff(employee);
        Map<String, String> params3 = login(employee);
        String value3 = params3.get(mapKey);

        if (NetworkUtils.isNetworkAvalible(this.getContext())) {
            System.out.println("网络可用!");
        } else {
            System.out.println("网络不可用");
        }

        Assert.assertTrue("请求服务器登录失败！", ErrorInfo.EnumErrorCode.EC_NoError.toString().equals(value3));

        //7.POS没联网,老板或员工登录.从本地当中查找数据进行登录
        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", !NetworkUtils.isNetworkAvalible(this.getContext()));

        System.out.println("-------------------- case7: POS无联网，员工登录 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params4 = login(employee);
        String value4 = params4.get(mapKey);
        Assert.assertTrue("登录失败！！", "登录成功！！".equals(value4));

        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue("wifi处理超时", false);
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible(this.getContext()));
        Assert.assertTrue("网络切换失败！", NetworkUtils.isNetworkAvalible(this.getContext()));
    }

    //params: currentStaff   登录者
    public Map<String, String> login(Staff staff) throws InterruptedException {
        Map<String, String> params = new HashMap<String, String>();
        do {
            if ("".equals(staff.getPhone()) || "".equals(staff.getPasswordInPOS())) {
                System.out.println("手机号码或密码不能为空");
                params.put(mapKey, "手机号码或密码不能为空");
                break;
            }
            if (!networkUtils.isNetworkAvalible(this.getContext())) {//网络不可用
                Constants.setCurrentStaff(staff);
                staff.setSql("where F_Phone = ?");
                staff.setConditions(new String[]{staff.getPhone()});
                staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
                List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff);
                if (staffList.size() > 0) {
                    Staff s = staffList.get(0);
                    if ("".equals(s.getSalt())) {
                        System.out.println("请连接网络进行登录！！");
                        params.put(mapKey, "请连接网络进行登录！！");
                        break;
                    }
                    String md5 = MD5Utils.MD5(staff.getPasswordInPOS() + Constants.SHADOW);
                    if (!md5.equals(s.getSalt())) {
                        System.out.println("输入的密码不正确！！");
                        params.put(mapKey, "输入的密码不正确！！");
                        break;
                    } else {
                        System.out.println("登录成功！！");
                        params.put(mapKey, "登录成功！！");
                        break;
                    }
                } else {
                    System.out.println("未找到该用户，请重新输入！！");
                    params.put(mapKey, "未找到该用户，请重新输入！！");
                    break;
                }
            } else {
                staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
                staffLoginHttpBO.setStaff(staff);
                staffLoginHttpBO.loginAsync();

                long lTimeOut = Shared.UNIT_TEST_TimeOut;
                while (lTimeOut-- > 0) {
                    if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                        break;
                    }
                    Thread.sleep(1000);
                }
                if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                    System.out.println("STAFF登录超时!");
                    Assert.assertTrue("STAFF登录超时", false);
                }

                ErrorInfo.EnumErrorCode error = staffLoginHttpBO.getHttpEvent().getLastErrorCode();
                params.put(mapKey, error.toString());
            }
        } while (false);

        return params;
    }
}
