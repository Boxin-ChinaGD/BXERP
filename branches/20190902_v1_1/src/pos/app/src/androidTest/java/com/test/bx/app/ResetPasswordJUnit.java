package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.StaffSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ResetPasswordJUnit extends BaseHttpAndroidTestCase {
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffHttpBO staffHttpBO = null;
    private static StaffSQLiteBO staffSQLiteBO = null;
    private static StaffHttpEvent staffHttpEvent = null;

    private static final int EVENT_ID_ResetPasswordJUnit = 10000;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        if (staffSQLiteBO == null) {
            staffSQLiteBO = new StaffSQLiteBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        staffSQLiteEvent.setHttpBO(staffHttpBO);
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setSqliteBO(staffSQLiteBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffSQLiteEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    public static class DataInput {
        private static Staff staffInput = null;

        protected static final Staff getStaffInput() throws CloneNotSupportedException {
            staffInput = new Staff();
            staffInput.setPhone("15200702" + (int) ((Math.random() * 9 + 1) * 100));
            staffInput.setName(getStaffName(12));//
            staffInput.setICID("431024199703241" + (int) ((Math.random() * 9 + 1) * 100)); //
            staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
            staffInput.setPasswordExpireDate(new Date());//
            staffInput.setIsFirstTimeLogin(1); //
            staffInput.setShopID(1);//
            staffInput.setDepartmentID(1);//
            staffInput.setSyncDatetime(new Date());
            staffInput.setInt1(3);
            staffInput.setOperatorStaffID(1);
            staffInput.setRoleID(1);
            staffInput.setPasswordInPOS("000000");
            staffInput.setSalt(getRandomString(32));
            staffInput.setReturnObject(1);

            return (Staff) staffInput.clone();
        }
    }

    /**
     * 1.模拟网页创建一个Staff
     * 2.Staff登录，因为首次登录，判断服务器返回的错误码
     * 3.修改密码
     * 4.再次登录，判断服务器返回的错误码
     */
    @Test
    public void test_a_resetPassword() throws Exception {
        Shared.printTestMethodStartInfo();

        System.out.println("----------1.模拟网页创建一个Staff----------");
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        Staff staff = DataInput.getStaffInput();
        staffLoginHttpBO.staffGetToken(staff, true);
        //
        long lTimeOut = 60;
        while (staffLoginHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("currentStaff getToken超时！", false);
        }

        staff.setSalt(getRandomString(32));
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("调用Create失败！", false);
        }
        lTimeOut = 60;
        while (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("currentStaff create超时！", false);
        }

        logOut();

        System.out.println("----------2.Staff登录，因为首次登录，判断服务器返回的错误码----------");
        staff.setPasswordInPOS("000000");
        Shared.login(2, staff.getPhone(), staff.getPasswordInPOS(), 0, "668866", posLoginHttpBO, staffLoginHttpBO, 1);
        Assert.assertTrue("登录后，服务器返回的错误码不正确！" + staffLoginHttpEvent.getStaffLoginStatus(), Staff.FIRST_LOGIN.equals(staffLoginHttpEvent.getStaffLoginStatus()));

        System.out.println("----------3.修改密码-------------");
        String newPassword = "123456";
        Map map = staffLoginHttpBO.encryptedPassword(staff, false, newPassword);
        String pwdEncrypted = (String) map.get("OriginalPassword");
        String newPwdEncrypted = (String) map.get("NewPassword");
        staff.setOldPwdEncrypted(pwdEncrypted);
        staff.setNewPwdEncrypted(newPwdEncrypted);
        if (!staffHttpBO.resetMyPassword(staff)) {
            Assert.assertTrue("调用resetMypassword失败！", false);
        }
        lTimeOut = 60;
        while (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Thread.sleep(1000);
        }
        if (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("修改密码超时！", false);
        }
        Assert.assertTrue("修改密码的错误码不正确！", staffHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        logOut();

        System.out.println("-------------4.再次登录，判断服务器返回的错误码-------------");
        Shared.login(1, staff.getPhone(), newPassword, 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0);
        Assert.assertTrue("登录后，服务器返回的错误码不正确！", staffHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 1.在服务器找到不是第一次登陆的Staff
     * 2.登录
     * 3.判断错误码
     */
    @Test
    public void test_b_loginByOtherStaff() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        System.out.println("---------------1.在服务器找到不是第一次登陆的Staff---------------");
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        Staff staff = new Staff();
        staff.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        staff.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("调用RetrieveNC失败！", false);
        }
        long lTimeOut = 60;
        while (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("retrieveNC Staff 超时！", false);
        }
        Assert.assertTrue("错误码不正确！", staffSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Staff> staffList = (List<Staff>) staffHttpEvent.getListMasterTable();
        for (Staff s : staffList) {
            if (s.getIsFirstTimeLogin() == 0) {
                staff = s;
                break;
            }
        }

        logOut();

        System.out.println("---------------2.登录---------------");
        staff.setPasswordInPOS("123456");
        Shared.login(2, staff.getPhone(), staff.getPasswordInPOS(), 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0);

        System.out.println("---------------3.判断StaffLoginStatus---------------");
        Assert.assertTrue("登录失败！", Staff.LOGIN_SUCCESS.equals(staffLoginHttpEvent.getStaffLoginStatus()));
    }

    @Test
    public void test_c_loginByNonexistentStaff() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        System.out.println("---------------1.在服务器找到所有的Staff---------------");
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        Staff staff = new Staff();
        staff.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        staff.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("调用RetrieveNC失败！", false);
        }
        long lTimeOut = 60;
        while (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("retrieveNC Staff 超时！", false);
        }
        Assert.assertTrue("错误码不正确！", staffSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Staff> staffList = (List<Staff>) staffHttpEvent.getListMasterTable();

        System.out.println("---------------2.通过phone判断，如果DataInput的Staff不在List中，就登陆---------------");
        logOut();
        //
        Staff staff1 = DataInput.getStaffInput();
        boolean isExit = false;
        //
        for (Staff s : staffList) {
            if (s.getPhone() == staff1.getPhone()) {
                Assert.assertTrue("DataInput中的Staff的Phone在服务器中已经存在！", false);
            }
        }
        //
        staff.setPasswordInPOS("000000");
        Shared.login(1, staff1.getPhone(), staff1.getPasswordInPOS(), 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0);

        System.out.println("---------------1.判断StaffLoginStatus---------------");
        Assert.assertTrue("应该会登录失败！", Staff.LOGIN_FAILURE.equals(staffLoginHttpEvent.getStaffLoginStatus()));
    }

    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getStaffName(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(26);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
