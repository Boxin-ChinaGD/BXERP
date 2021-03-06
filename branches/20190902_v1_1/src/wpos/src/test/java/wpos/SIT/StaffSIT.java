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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * 1.pos???staff??????
     * 2.??????????????????
     * 3.???????????????Staff??????,??????getToken
     * 4.call currentStaff/createEx.bx,???????????????????????????1???Staff, ???POS??????
     * 5.POS STAFF??????
     * 6.??????RN??????, ??????????????????????????????????????????Staff
     * 7.RN??????????????????Feedback
     * 8.feedback???????????????RN, ??????????????????
     * 9.????????????3 4
     * 10.??????????????????
     * 11.????????????RN staff????????????
     */
    @Test
    public void test_b_Staff() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.pos getToken,??????sessionID,??????staff??????,,,???????????????Staff??????,??????getToken
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //2.??????????????????

        //3.???????????????Staff??????,??????getToken
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff getToken??????!");
        }

        //4.call currentStaff/createEx.bx,?????????????????????Staff, ???POS??????
        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue(false, "????????????!");
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Staff??????!");
        }

        logOut();
        //5.POS STAFF??????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //6.??????RN??????, ??????????????????????????????????????????Staff
        System.out.println("-------------------------------RN1");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "staff????????????!");
        }
        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "staff????????????!");
        }
        List<Staff> staffList = (List<Staff>) staffSqLiteBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue(staffList.size() != 0, "??????RN???????????????????????????");

//        //7.RN??????????????????Feedback
//        System.out.println("-------------------------------Feedback");
//        String staffIDs = "";
//        for (int i = 0; i < staffList.size(); i++) {
//            staffIDs = staffIDs + "," + staffList.get(i).getID();
//        }
//        staffIDs = staffIDs.substring(1, staffIDs.length());
//        staffHttpBO.feedback(staffIDs);
//        //??????????????????
//        lTimeOut = 50;
//        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("??????????????????????????????", false);
//        }
//
//        //8.feedback???????????????RN, ??????????????????
//        System.out.println("-------------------------------RN2");
//        if (!staffHttpBO.retrieveNAsync(null)) {
//            Assert.assertTrue("Staff RN??????!", false);
//        }
//        lTimeOut = 50;
//        staffSqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("Staff RN??????!", false);
//        }
//        List<Staff> staffList1 = (List<Staff>) staffHttpBO.getHttpEvent().getListMasterTable();
//        Assert.assertTrue("?????????Staff RN????????????????????????", staffList1.size() == 0);

        logOut();
        //9.????????????3 4
        //3.???????????????Staff??????,??????getToken
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        System.out.println("------------------------?????????getToken");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff2 getToken??????!");
        }

        //4.call currentStaff/createEx.bx,?????????????????????Staff, ???POS??????
        System.out.println("------------------------?????????craete currentStaff");
        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue(false, "????????????!");
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Staff2??????!");
        }
    }

    /*
    ????????????
     */
    public void test_c_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //??????????????????

        //POS????????????????????????Action????????????????????????
        Staff s = new Staff();
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        s.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        staffSqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "??????RetrieveN????????????");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "?????????????????????Staff??????!");
        }

        //???????????????????????????sqlite??????????????????,
        List<Staff> rtList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSyncC????????????,??????:????????????????????????!");

        List<Staff> list = (List<Staff>) staffHttpBO.getHttpEvent().getListMasterTable();
        System.out.println("??????????????????" + rtList + "_______?????????????????????" + list);
        if (list.size() != rtList.size()) {
            Assert.assertTrue(false, "??????????????????????????????????????????????????????SQLite??????????????????");
        }
    }

    @Test
    public void test_a_Login() throws Exception {
        //?????????SIT?????????????????????????????????Sqlite???????????????
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
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "?????????????????????");
        //1.?????????????????????????????????????????????????????????
        //??????POS???Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case1: ?????????????????? --------------------");
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff getToken??????!");
        }

        staff.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue(false, "??????Create????????????");
        }

        lTimeOut = 50;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Staff??????!");
        }

        //2.???????????????????????????????????????????????????????????????
        //??????POS???Staff
        logOut();
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);


        System.out.println("-------------------- case2: ?????????????????? --------------------");
        Staff staff2 = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff2, true);
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Staff getToken??????!");
        }

        staff2.setSalt(Shared.getFakedSalt());
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {
            Assert.assertTrue(false, "??????Create????????????");
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Staff??????!");
        }

        Staff employee = (Staff) staffHttpBO.getHttpEvent().getBaseModel1(); //???????????????????????????????????????''???

        //3.pos?????????????????????????????????(??????????????????2??????1.?????????????????????????????????????????????2.????????????????????????Sqlite???????????????????????????)???????????????POS??????Sqlite?????????staff?????????.

//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p2 = Runtime.getRuntime().exec(cmd);
        p2.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "?????????????????????");

        System.out.println("-------------------- case3: ????????????pos???????????????????????????????????? --------------------");
        employee.setPasswordInPOS("000000"); //????????????????????????
        Map<String, String> params = login(employee);
        String value = params.get(mapKey);
        Assert.assertTrue("??????????????????????????????????????????".equals(value), "??????????????????POS???????????????");

        //4.pos?????????????????????????????????????????????2????????????,?????????feedback
        cmd = OPEN_WIFI;
        Process p3 = Runtime.getRuntime().exec(cmd);
        p3.waitFor();
//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(15 * 1000);
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "?????????????????????");


        //??????POS???Staff
        logOut();
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        System.out.println("-------------------- case4: ???????????????staff --------------------");
        Staff s = new Staff();
        s.setPageIndex("1");
        s.setPageSize(BaseHttpBO.PAGE_SIZE_MAX);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {
            Assert.assertTrue(false, "??????RetrieveN????????????");
        }

        lTimeOut = 50;
        while (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "?????????????????????Staff??????!");
        }

        Staff staff1 = new Staff();
        staff1.setSql("where F_Phone = %s");
        staff1.setConditions(new String[]{staff.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue(list.size() > 0, "????????????????????????????????????");

        staff1.setSql("where F_Phone = %s");
        staff1.setConditions(new String[]{staff2.getPhone()});
        staff1.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        List<Staff> list2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff1);
        Assert.assertTrue(list2.size() > 0, "????????????????????????????????????");

        //5.POS?????????,???????????????????????????Sqlite???????????????????????????????????????????????????????????????
//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p4 = Runtime.getRuntime().exec(cmd);
        p4.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "?????????????????????");

        System.out.println("-------------------- case5: POS?????????????????????????????? --------------------");
        employee.setPasswordInPOS("000000"); //????????????????????????
        Map<String, String> params2 = login(employee);
        String value2 = params2.get(mapKey);
        Assert.assertTrue("?????????????????????????????????".equals(value2), "????????????sqlite????????????????????????????????????????????????????????????");

        //6.POS??????,????????????????????????????????????????????????????????????????????????????????????????????????????????????SQLITE???????????????????????????
//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        cmd = OPEN_WIFI;
        Process p5 = Runtime.getRuntime().exec(cmd);
        p5.waitFor();
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "?????????????????????");

        System.out.println("-------------------- case6: POS?????????????????????????????????????????? --------------------");
        employee.setPasswordInPOS("000000");
        Constants.setCurrentStaff(employee);
        Map<String, String> params3 = login(employee);
        String value3 = params3.get(mapKey);

        if (NetworkUtils.isNetworkAvalible()) {
            System.out.println("????????????!");
        } else {
            System.out.println("???????????????");
        }

        Assert.assertTrue(ErrorInfo.EnumErrorCode.EC_NoError.toString().equals(value3), "??????????????????????????????");

        //7.POS?????????,?????????????????????.???????????????????????????????????????
//        wifiManager.setWifiEnabled(false);
//        Thread.sleep(5 * 1000);
        cmd = CLOSE_WIFI;
        Process p6 = Runtime.getRuntime().exec(cmd);
        p6.waitFor();
        lTimeOut = 10;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(!NetworkUtils.isNetworkAvalible(), "?????????????????????");

        System.out.println("-------------------- case7: POS???????????????????????? --------------------");
        employee.setPasswordInPOS("000000"); //????????????????????????
        Map<String, String> params4 = login(employee);
        String value4 = params4.get(mapKey);
        Assert.assertTrue("??????????????????".equals(value4), "??????????????????");

//        wifiManager.setWifiEnabled(true);
//        Thread.sleep(20 * 1000);
        cmd = OPEN_WIFI;
        Process p7 = Runtime.getRuntime().exec(cmd);
        p7.waitFor();
        lTimeOut = 30;
        do {
            if (lTimeOut == 0) {
                Assert.assertTrue(false, "wifi????????????");
            }
            lTimeOut--;
            Thread.sleep(1000);
        } while (!NetworkUtils.isNetworkAvalible());
        Assert.assertTrue(NetworkUtils.isNetworkAvalible(), "?????????????????????");
    }

    //params: currentStaff   ?????????
    public Map<String, String> login(Staff staff) throws InterruptedException {
        Map<String, String> params = new HashMap<String, String>();
        do {
            if ("".equals(staff.getPhone()) || "".equals(staff.getPasswordInPOS())) {
                System.out.println("?????????????????????????????????");
                params.put(mapKey, "?????????????????????????????????");
                break;
            }
            if (!networkUtils.isNetworkAvalible()) {//???????????????
                Constants.setCurrentStaff(staff);
                staff.setSql("where F_Phone = %s");
                staff.setConditions(new String[]{staff.getPhone()});
                staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
                List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff);
                if (staffList.size() > 0) {
                    Staff s = staffList.get(0);
                    if ("".equals(s.getSalt())) {
                        System.out.println("?????????????????????????????????");
                        params.put(mapKey, "?????????????????????????????????");
                        break;
                    }
                    String md5 = MD5Utils.MD5(staff.getPasswordInPOS() + Constants.SHADOW);
                    if (!md5.equals(s.getSalt())) {
                        System.out.println("??????????????????????????????");
                        params.put(mapKey, "??????????????????????????????");
                        break;
                    } else {
                        System.out.println("??????????????????");
                        params.put(mapKey, "??????????????????");
                        break;
                    }
                } else {
                    System.out.println("??????????????????????????????????????????");
                    params.put(mapKey, "??????????????????????????????????????????");
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
                    System.out.println("STAFF????????????!");
                    Assert.assertTrue(false, "STAFF????????????");
                }

                ErrorInfo.EnumErrorCode error = staffLoginHttpBO.getHttpEvent().getLastErrorCode();
                params.put(mapKey, error.toString());
            }
        } while (false);

        return params;
    }
}
