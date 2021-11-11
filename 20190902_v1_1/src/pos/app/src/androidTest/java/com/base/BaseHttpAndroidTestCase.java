package com.base;

import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.http.HttpRequestScheduler;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.Assert;

import java.sql.SQLOutput;
import java.util.Date;

public class BaseHttpAndroidTestCase extends BaseAndroidTestCase {
    protected HttpRequestScheduler hrs;
    //
    private static NtpHttpEvent ntpHttpEvent = null;
    private static NtpHttpBO ntpHttpBO = null;

    protected static LogoutHttpBO logoutHttpBO = null;
    protected static LogoutHttpEvent logoutHttpEvent = null;

    @Subscribe
    public void virtualMethod(BaseEvent be) {
        //因为注册了EventBus，需要写@Subscribe的方法
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        EventBus.getDefault().register(this);

        hrs = new HttpRequestScheduler();
        hrs.start();
    }

    @Override
    public void tearDown() throws Exception {
        if (GlobalController.getInstance().getSessionID() != null) {
            if (!logoutHttpBO.logoutAsync()) {
                System.out.println("xxxxx  退出登录失败");
            }
            long lTimeout = 50;
            logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                    logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                    logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
                System.out.println("xxxxxxxxxx   退出登录超时");
            }
            System.out.println("xxxxxxxxx session: " + GlobalController.getInstance().getSessionID());
        }
        hrs.stop();
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    /**
     * 同步APP的时间和服务器的时间
     */
    protected void syncTime(int activityEventID) {
        initBoAndEvent(activityEventID);
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue("查找Commodity失败！！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("同步失败！原因：超时" + ntpHttpBO.getHttpEvent().getStatus(), false);
        }
    }

    private void initBoAndEvent(int activityEventID) {
        //
        ntpHttpEvent = new NtpHttpEvent();
        ntpHttpEvent.setId(activityEventID);
        ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        ntpHttpEvent.setHttpBO(ntpHttpBO);
    }

    /**
     * 需要实现onLogoutHttpEvent
     */
    protected void logOut() throws InterruptedException {
        if (GlobalController.getInstance().getSessionID() != null) {
            if (!logoutHttpBO.logoutAsync()) {
                Assert.assertTrue("退出登录失败! ", false);
            }
            //
            long lTimeOut = 50;
            while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.assertTrue("退出超时!", false);
            }
            //
            Assert.assertTrue("退出登录,服务器返回的错误码不正确", logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
    }

}
