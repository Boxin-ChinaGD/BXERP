package wpos.base;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import wpos.bo.LogoutHttpBO;
import wpos.bo.NtpHttpBO;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.LogoutHttpEvent;
import wpos.event.NtpHttpEvent;
import wpos.http.HttpRequestScheduler;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.utils.EventBus;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.Date;

public class BaseHttpTestCase extends BaseTestCase {
    protected HttpRequestScheduler hrs;
    //
    @Resource
    private NtpHttpEvent ntpHttpEvent;
    @Resource
    private NtpHttpBO ntpHttpBO;
    @Resource
    protected LogoutHttpBO logoutHttpBO;
    @Resource
    protected LogoutHttpEvent logoutHttpEvent;

    @Subscribe
    public void virtualMethod(BaseEvent be) {
        //因为注册了EventBus，需要写@Subscribe的方法
    }

    @Override
    public void setUp() {
        super.setUp();
        EventBus.getDefault().register(this);
        hrs = new HttpRequestScheduler();
        hrs.start();
        GlobalController.init();
    }

    @Override
    public void tearDown() {
        if (GlobalController.getInstance().getSessionID() != null) {
            if (!logoutHttpBO.logoutAsync()) {
                System.out.println("xxxxx  退出登录失败");
            }
            long lTimeout = 50;
            logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && //
                    logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            Assert.fail("查找Commodity失败！！");
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
            Assert.fail("同步失败！原因：超时" + ntpHttpBO.getHttpEvent().getStatus());
        }
    }

    private void initBoAndEvent(int activityEventID) {
        //
        ntpHttpEvent = new NtpHttpEvent();
        ntpHttpEvent.setId(activityEventID);
        ntpHttpBO = new NtpHttpBO(ntpHttpEvent);
        ntpHttpEvent.setHttpBO(ntpHttpBO);
    }

    /**
     * 需要实现onLogoutHttpEvent
     */
    protected void logOut() throws InterruptedException {
        if (GlobalController.getInstance().getSessionID() != null) {
            // 如果退出超时，尝试多一次
            int tryMoreTime = 1;
            do {
                if (!logoutHttpBO.logoutAsync()) {
                    Assert.fail("退出登录失败! ");
                }
                //
                long lTimeOut = 50;
                while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                    Thread.sleep(1000);
                }
            } while (tryMoreTime-- > 0 && logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done);
            if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                Assert.fail("退出超时!");
            }
            //
            Assert.assertSame(logoutHttpBO.getHttpEvent().getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "退出登录,服务器返回的错误码不正确");
        }
    }

}
