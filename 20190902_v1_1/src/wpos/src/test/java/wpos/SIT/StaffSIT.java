package wpos.SIT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Staff;
import wpos.utils.MD5Utils;
import wpos.utils.NetworkUtils;
import wpos.utils.Shared;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffSIT extends BaseHttpTestCase {
    private static StaffSQLiteBO staffSqLiteBO = null;
    private static StaffHttpBO staffHttpBO = null;
    private static StaffHttpEvent staffHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private PosSQLiteEvent posSQLiteEvent = null;
    private StaffSQLiteEvent staffSQLiteEvent = null;

    private static PosHttpEvent posHttpEvent = null;
    private static NetworkUtils networkUtils = new NetworkUtils();
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static final int EVENT_ID_StaffSIT = 10000;

    private static final String mapKey = "msg";
    private static String syncIDs = "";
    private final static String OPEN_WIFI = "ipconfig /renew";
    private final static String CLOSE_WIFI = "ipconfig /release";

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
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
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
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
            staffSqLiteBO = new StaffSQLiteBO(staffSQLiteEvent, staffHttpEvent);
            staffSqLiteBO.setStaffPresenter(staffPresenter);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(staffSQLiteEvent, staffHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(staffSQLiteEvent, staffHttpEvent);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(posSQLiteEvent, staffHttpEvent);
        }
        //
        staffSQLiteEvent.setSqliteBO(staffSqLiteBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setSqliteBO(staffSqLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setStaffPresenter(staffPresenter);
        //
        logoutHttpEvent.setId(EVENT_ID_StaffSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    @Override
    @AfterClass
    public void tearDown() {
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

        public static final Staff getStaffInput() throws CloneNotSupportedException {
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

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
            Assert.assertTrue(false, "Staff getToken超时!");
        }

        //4.call currentStaff/createEx.bx,令网页创建一个Staff, 让POS同步
        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue(false, "创建失败!");
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Staff超时!");
        }

        logOut();
        //5.POS STAFF登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //6.发送RN请求, 服务器应该返回刚刚请求创建的Staff
        System.out.println("-------------------------------RN1");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "staff同步失败!");
        }
        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "staff同步超时!");
        }
        List<Staff> staffList = (List<Staff>) staffSqLiteBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue(staffList.size() != 0, "同步RN不应该没有数据返回");

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
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        System.out.println("------------------------第二次getToken");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff2 getToken超时!");
        }

        //4.call currentStaff/createEx.bx,令网页创建一个Staff, 让POS同步
        System.out.println("------------------------第二次craete currentStaff");
        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue(false, "创建失败!");
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Staff2超时!");
        }
    }

    /*
    全部同步
     */
    public void test_c_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //暂停同步线程

        //POS去请求全部同步的Action并更新本地服务器
        Staff s = new Staff();
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        s.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        staffSqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "调用RetrieveN失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "请求服务器同步Staff超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Staff> rtList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSyncC测试失败,原因:返回错误码不正确!");

        List<Staff> list = (List<Staff>) staffHttpBO.getHttpEvent().getListMasterTable();
        System.out.println("本地的数据：" + rtList + "_______服务器返回的：" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue(false, "全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
    }

    @Test
    public void test_a_Login() throws Exception {
        //为确保SIT能正确运行，所以先删除Sqlite中的数据。
        staffPresenter.deleteNObjectSync(BaseHttpBO.INVALID_CASE_ID, null);
        String cmd = OPEN_WIFI;
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
//        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(5 * 1000);
        long lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "网络切换失败！");
        //1.新建一个老板账号，此时服务器同存产生。
        //登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case1: 创建老板账号 --------------------");
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff getToken超时!");
        }

        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue(false, "调用Create失败！！");
        }

        lTimeOut = 50;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Staff超时!");
        }

        //2.老板创建一个员工账号，此时服务器同存产生。
        //登录POS和Staff
        logOut();
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case2: 创建员工账号 --------------------");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff getToken超时!");
        }

        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue(false, "调用Create失败！！");
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Staff超时!");
        }

        Staff employee = (Staff) staffHttpBO.getHttpEvent().getBaseModel1(); //获取到员工的账号。（密码为''）

        //3.pos没联网时，谁登陆都失败(员工登录分为2种：1.有网情况下请求服务器进行登录。2.无网情况下在本地Sqlite中查找数据进行登录)。首次使用POS机，Sqlite中是无staff数据的.

//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p2 = Runtime.getRuntime().exec(cmd);
        p2.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "网络切换失败！");

        System.out.println("-------------------- case3: 首次使用pos机，没联网时谁登录都失败 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params = login(employee);
        String value = params.get(mapKey);
        Assert.assertTrue("未找到该用户，请重新输入！！".equals(value), "首次没联网时POS端有数据！");

        //4.pos联网，同步所有的数据（当前应有2条数据）,再进行feedback
        cmd = OPEN_WIFI;
        Process p3 = Runtime.getRuntime().exec(cmd);
        p3.waitFor();
//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(15 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "网络切换失败！");


        //登录POS和Staff
        logOut();
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        System.out.println("-------------------- case4: 同步需要的staff --------------------");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "调用RetrieveN失败！！");
        }

        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "请求服务器同步Staff超时!");
        }

        Staff staff1 = new Staff();
        staff1.setSql("where F_Phone = %s");
        staff1.setConditions(new String[]{staff.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue(list.size() > 0, "无法找到创建的老板账号！");

        staff1.setSql("where F_Phone = %s");
        staff1.setConditions(new String[]{staff2.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue(list2.size() > 0, "无法找到创建的员工账号！");

        //5.POS没联网,老板或员工登录，应Sqlite中密码为空串，所以登录失败，提示需要联网。
//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p4 = Runtime.getRuntime().exec(cmd);
        p4.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "网络切换失败！");

        System.out.println("-------------------- case5: POS没联网，员工首次登录 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params2 = login(employee);
        String value2 = params2.get(mapKey);
        Assert.assertTrue("请连接网络进行登录！！".equals(value2), "无网络且sqlite中密码为空的情况下没提示连接网络进行登录");

        //6.POS联网,老板或员工登录，请求服务器进行登录。如果登录成功。会将加密的密码写入本地SQLITE中。以便断网时登录
//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        cmd = OPEN_WIFI;
        Process p5 = Runtime.getRuntime().exec(cmd);
        p5.waitFor();
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "网络切换失败！");

        System.out.println("-------------------- case6: POS联网，员工连接服务器进行登录 --------------------");
        employee.setPasswordInPOS("000000");
        Constants.setCurrentStaff(employee);
        Map<String, String> params3 = login(employee);
        String value3 = params3.get(mapKey);

        if (NetworkUtils.isNetworkAvalible()) {
            System.out.println("网络可用!");
        } else {
            System.out.println("网络不可用");
        }

        Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError.toString().equals(value3), "请求服务器登录失败！");

        //7.POS没联网,老板或员工登录.从本地当中查找数据进行登录
//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p6 = Runtime.getRuntime().exec(cmd);
        p6.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "网络切换失败！");

        System.out.println("-------------------- case7: POS无联网，员工登录 --------------------");
        employee.setPasswordInPOS("000000"); //模拟员工输入密码
        Map<String, String> params4 = login(employee);
        String value4 = params4.get(mapKey);
        Assert.assertTrue("登录成功！！".equals(value4), "登录失败！！");

//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        cmd = OPEN_WIFI;
        Process p7 = Runtime.getRuntime().exec(cmd);
        p7.waitFor();
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi处理超时");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "网络切换失败！");
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
            if (!networkUtils.isNetworkAvalible()) {//网络不可用
                Constants.setCurrentStaff(staff);
                staff.setSql("where F_Phone = %s");
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
                    Assert.assertTrue(false, "STAFF登录超时");
                }

                ErrorInfo.EnumErrorCode error = staffLoginHttpBO.getHttpEvent().getLastErrorCode();
                params.put(mapKey, error.toString());
            }
        } while (false);

        return params;
    }
}
