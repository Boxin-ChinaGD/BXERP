package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.Staff;
import wpos.utils.Shared;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ResetPasswordJUnit extends BaseHttpTestCase {
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffHttpBO staffHttpBO = null;
    private static StaffSQLiteBO staffSQLiteBO = null;
    private static StaffHttpEvent staffHttpEvent = null;

    private static final int EVENT_ID_ResetPasswordJUnit = 10000;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetPasswordJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();

        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
            staffHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(EVENT_ID_ResetPasswordJUnit);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(staffSQLiteEvent, staffHttpEvent);
        }
        if (staffSQLiteBO == null) {
            staffSQLiteBO = new StaffSQLiteBO(staffSQLiteEvent, staffHttpEvent);
            staffSQLiteBO.setStaffPresenter(staffPresenter);
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
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(staffSQLiteEvent, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffSQLiteEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_ResetPasswordJUnit);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        // 从容器中获取，不用new
        logoutHttpBO.setHttpEvent(logoutHttpEvent);

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
            Assert.fail("currentStaff getToken超时！");
        }

        staff.setSalt(getRandomString(32));
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.fail("调用Create失败！");
        }
        lTimeOut = 60;
        while (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("currentStaff create超时！");
        }

        logOut();

        System.out.println("----------2.Staff登录，因为首次登录，判断服务器返回的错误码----------");
        staff.setPasswordInPOS("000000");
        Shared.login(2, staff.getPhone(), staff.getPasswordInPOS(), 0, "668866", posLoginHttpBO, staffLoginHttpBO, 1);
        Assert.assertEquals(staffLoginHttpEvent.getStaffLoginStatus(), Staff.FIRST_LOGIN, "登录后，服务器返回的错误码不正确！" + staffLoginHttpEvent.getStaffLoginStatus());

        System.out.println("----------3.修改密码-------------");
        String newPassword = "123456";
        Map map = staffLoginHttpBO.encryptedPassword(staff, false, newPassword);
        String pwdEncrypted = (String) map.get("OriginalPassword");
        String newPwdEncrypted = (String) map.get("NewPassword");
        staff.setOldPwdEncrypted(pwdEncrypted);
        staff.setNewPwdEncrypted(newPwdEncrypted);
        if (!staffHttpBO.resetMyPassword(staff)) {
            Assert.fail("调用resetMypassword失败！");
        }
        lTimeOut = 60;
        while (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Thread.sleep(1000);
        }
        if (staffHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("修改密码超时！");
        }
        Assert.assertSame(staffHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "修改密码的错误码不正确！");

        logOut();

        System.out.println("-------------4.再次登录，判断服务器返回的错误码-------------");
        Shared.login(1, staff.getPhone(), newPassword, 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0);
        Assert.assertSame(staffHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "登录后，服务器返回的错误码不正确！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
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
            Assert.fail("调用RetrieveNC失败！");
        }
        long lTimeOut = 60;
        while (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("retrieveNC Staff 超时！");
        }
        Assert.assertSame(staffSQLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
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
        Assert.assertEquals(staffLoginHttpEvent.getStaffLoginStatus(), Staff.LOGIN_SUCCESS, "登录失败！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
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
            Assert.fail("调用RetrieveNC失败！");
        }
        long lTimeOut = 60;
        while (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("retrieveNC Staff 超时！");
        }
        Assert.assertSame(staffSQLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
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
                Assert.fail("DataInput中的Staff的Phone在服务器中已经存在！");
            }
        }
        //
        staff.setPasswordInPOS("000000");
        Shared.login(1, staff1.getPhone(), staff1.getPasswordInPOS(), 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0);

        System.out.println("---------------1.判断StaffLoginStatus---------------");
        Assert.assertEquals(staffLoginHttpEvent.getStaffLoginStatus(), Staff.LOGIN_FAILURE, "应该会登录失败！");
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
