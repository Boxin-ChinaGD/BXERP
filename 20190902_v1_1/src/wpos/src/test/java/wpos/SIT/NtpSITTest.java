package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.LogoutHttpBO;
import wpos.bo.NtpHttpBO;
import wpos.bo.PosLoginHttpBO;
import wpos.bo.StaffLoginHttpBO;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Configuration;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.Ntp;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.Date;

public class NtpSITTest extends BaseHttpTestCase {
    @Resource
    private NtpHttpBO ntpHttpBO;
    @Resource
    private NtpHttpEvent ntpHttpEvent;
    //
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    Ntp ntp = new Ntp();
    private static final int EVENT_ID_NtpSITTest = 10000;
//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    @BeforeClass
    public void setup() {
        super.setUp();
        ntpHttpEvent.setId(EVENT_ID_NtpSITTest);
        ntpHttpBO.setHttpEvent(ntpHttpEvent);
        ntpHttpEvent.setHttpBO(ntpHttpBO);
        //
        posLoginHttpEvent.setId(EVENT_ID_NtpSITTest);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(EVENT_ID_NtpSITTest);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_NtpSITTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest){
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                event.setRequestType(null);
                ntp = (Ntp) event.getBaseModel1();
                NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_NtpSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_syncTime() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)){
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        long timeStamp = new Date().getTime();
        System.out.println("-------------------------输出：正常时间 :" + timeStamp + "---------------------------");
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue(false,"同步时间出错");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        ntpHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (ntpHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        if (ntpHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"同步时间超时！");
        }
        Assert.assertTrue(Math.abs(ntp.getT1() + NtpHttpBO.TimeDifference - ntp.getT2()) < 500, "同步失败！");
        System.out.println("-------------------------httpEvent Status" + ntpHttpEvent.getStatus() + "---------------------------");
    }

}

