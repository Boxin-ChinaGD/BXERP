package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.utils.Shared;

import junit.framework.Assert;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.Test;

public class LoginJUnitTest extends BaseHttpAndroidTestCase {
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;

    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;

    private static final int EVENT_ID_LoginJUnitTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_LoginJUnitTest);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(EVENT_ID_LoginJUnitTest);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_LoginJUnitTest);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffLoginHttpEvent);
        }
        staffSQLiteEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_LoginJUnitTest);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_LoginJUnitTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_LoginJUnitTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    //正常的pos和staff登录
    //pos登录失败：
    //case1.当门店被锁定后pos也不能够进行登录.
    //case2:使用不存在的pos进行登录
    @Test
    public void test_a_PosLogin() throws InterruptedException {
        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //
        System.out.println("-----------------------case1: 正常的登录pos------------------------");
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        System.out.println("----------------------------------- case2: 当门店被锁定后pos也不能够进行登录-------------------------------------");
        POS_ID = 6; //该pos在数据库中已被删除
        pos.setID(POS_ID);

        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Hack) {
            Assert.assertTrue("已被删除的pos不能够再进行登录！", false);
        }

        System.out.println("----------------------------------- case3: 使用不存在的pos进行登录-------------------------------------");
        POS_ID = 9999;
        pos.setID(POS_ID);

        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoSuchData) {
            Assert.assertTrue("不存在的POS ID进行登录，返回的错误码是" + posLoginHttpBO.getHttpEvent().getLastErrorCode(), false);
        }

        System.out.println("----------------------------------- case4: 使用不正确的密码进行登录-------------------------------------");
        POS_ID = 1;
        pos.setID(POS_ID);
        pos.setPasswordInPOS("1111111");

        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoSuchData) {
            Assert.assertTrue("密码不正确也登录成功了！", false);
        }
    }

    @Test
    public void test_b_StaffLogin() throws InterruptedException {
        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 正确登录Staff -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("15854320895");
        staff.setPasswordInPOS("000000");

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", false);
        }

        System.out.println("----------------------------------- case2: 离职员工进行登录 -------------------------------------");
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        staff.setPhone("13196721886");
        staff.setPasswordInPOS("123456");
        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();

        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoSuchData) {
            Assert.assertTrue("离职员工登录成功！", false);
        }
    }

    @Test
    public void test_c_errorAccount() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 使用错误的账号登录 -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("15854300000");//Error Account
        staff.setPasswordInPOS("000000");

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData);
        }
    }

    @Test
    public void test_d_errorPassword() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 使用错误的密码登录 -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("15854320895");
        staff.setPasswordInPOS("000111");//Error password

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData);
        }
    }

    @Test
    public void test_e_errorAccountFormat() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 使用错误的密码登录 -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("158543208");//Error format
        staff.setPasswordInPOS("000000");

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData);
        }
    }

    @Test
    public void test_f_errorPasswordFormat() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 使用错误的密码格式 -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("15854320895");
        staff.setPasswordInPOS("0000");//Error format

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        }
    }

    @Test
    public void test_g_errorAccountPasswordFormat() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        long POS_ID = 1;
        Pos pos = new Pos();
        pos.setID(POS_ID);
        pos.setPasswordInPOS("000000");
        Constants.MyCompanySN = "668866";
        //pos登录
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = 30;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }
        //
        if (posLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("pos登录失败!", false);
        }

        System.out.println("----------------------------------- case1: 使用错误的密码格式 -------------------------------------");
        Staff staff = new Staff();
        staff.setPhone("158543208");//Error format
        staff.setPasswordInPOS("0000");//Error format

        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        lTimeOut = 30;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Staff登录超时！", false);
        }

        if (staffLoginHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("Staff登录失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData);
        }
    }
}
