package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.ConfigGeneralSQLiteEvent;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.ConfigGeneral;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions;


public class ConfigGeneralJUnit extends BaseHttpTestCase {

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
    @BeforeClass
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
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
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(configGeneralSQLiteEvent, configGeneralHttpEvent);
            configGeneralSQLiteBO.setConfigGeneralPresenter(configGeneralPresenter);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_ConfigGeneralJUnit);
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
        private static ConfigGeneral configGeneral = null;

        protected static final ConfigGeneral getConfigGeneral() throws CloneNotSupportedException {
            configGeneral = new ConfigGeneral();
            configGeneral.setName("ACTIVE_SMALLSHEET_ID");
            configGeneral.setValue("F_ID");

            return (ConfigGeneral) configGeneral.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ConfigGeneralJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            System.out.println(posLoginHttpBO.getHttpEvent().getStatus() + "\t" + staffLoginHttpBO.getHttpEvent().getStatus());
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "retrieveNC ConfigGeneral失败!");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "retrieveNC ConfigGeneral超时！");
        }

        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralSQLiteEvent.getListMasterTable();
        Assert.assertTrue(configGeneralList.size() > 0, "服务器返回的数据为空！");

        List<ConfigGeneral> configGeneralList2 = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");

        Assert.assertTrue(configGeneralList.size() == configGeneralList2.size(), "服务器返回的数目和SQLite中的数目不一致");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
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

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Name = '%s'");
        configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        configGeneralList.get(0).setReturnObject(1);
        configGeneralList.get(0).setValue("2");

        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList.get(0))) {
            Assert.assertTrue(false, "update ConfigGeneral失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "update ConfigGeneral 超时！");
        }
        //判断修改后服务器返回的是否与我们想要的一致
        ConfigGeneral updateConfigGeneral = (ConfigGeneral) configGeneralHttpEvent.getBaseModel1();
        Assert.assertTrue(configGeneralList.get(0).compareTo(updateConfigGeneral) == 0, "服务器返回的ConfigGeneral与想要修改的ConfigGeneral不一致！");
        //判断本地SQLite中的ACTIVE_SMALLSHEET_ID的Value是否与我们想要的一致
        List<ConfigGeneral> updateConfigGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件在ConfigGeneral中查找，错误码不正确！");
        Assert.assertTrue(updateConfigGeneralList.get(0).compareTo(configGeneralList.get(0)) == 0, "本地SQLite中的ConfigGeneral与想要修改的ConfigGeneral不一致！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

}
