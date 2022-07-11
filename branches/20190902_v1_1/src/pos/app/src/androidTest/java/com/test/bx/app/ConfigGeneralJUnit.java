package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions;

public class ConfigGeneralJUnit extends BaseHttpAndroidTestCase {
    private static ConfigGeneralPresenter configGeneralPresenter = null;

    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static final int EVENT_ID_ConfigGeneralJUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (configGeneralPresenter == null) {
            configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        }


        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
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
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(EVENT_ID_ConfigGeneralJUnit);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
        }
        if (configGeneralSQLiteBO == null) {
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
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

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static ConfigGeneral configGeneral = null;

        protected static final ConfigGeneral getConfigGeneral() throws CloneNotSupportedException {
            configGeneral = new ConfigGeneral();
            configGeneral.setName("ACTIVE_SMALLSHEET_ID");
            configGeneral.setValue("F_ID");

            return (ConfigGeneral) configGeneral.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_a_RetrieveNCConfigGeneral() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("retrieveNC ConfigGeneral失败!", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("retrieveNC ConfigGeneral超时！", false);
        }

        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralSQLiteEvent.getListMasterTable();
        Assert.assertTrue("服务器返回的数据为空！", configGeneralList.size() > 0);

        List<ConfigGeneral> configGeneralList2 = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Assert.assertTrue("服务器返回的数目和SQLite中的数目不一致", configGeneralList.size() == configGeneralList2.size());
    }

    /**
     * 1.POS1本地update ConfigGeneral，设置SyncDatetime和SyncType
     * 2.将请求上传到服务器
     * 3.如果服务器返回EC_NoError，根据服务器返回的数据update本地SQLite，对比服务器返回的对象与跟新对象是否一致
     * 4.如果服务器返回非EC_NoError，不对本地SQLite进行Update
     *
     * @throws InterruptedException
     */
    @Test
    public void test_b_UpdateAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Name = ?");
        configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        configGeneralList.get(0).setReturnObject(1);
        configGeneralList.get(0).setValue("2");

        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList.get(0))) {
            Assert.assertTrue("update ConfigGeneral失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("update ConfigGeneral 超时！", false);
        }
        //判断修改后服务器返回的是否与我们想要的一致
        ConfigGeneral updateConfigGeneral = (ConfigGeneral) configGeneralHttpEvent.getBaseModel1();
        Assert.assertTrue("服务器返回的ConfigGeneral与想要修改的ConfigGeneral不一致！", configGeneralList.get(0).compareTo(updateConfigGeneral) == 0);
        //判断本地SQLite中的ACTIVE_SMALLSHEET_ID的Value是否与我们想要的一致
        List<ConfigGeneral> updateConfigGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue("根据条件在ConfigGeneral中查找，错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("本地SQLite中的ConfigGeneral与想要修改的ConfigGeneral不一致！", updateConfigGeneralList.get(0).compareTo(configGeneralList.get(0)) == 0);
    }

}
