package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.ConfigGeneralSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ConfigGeneral;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigGeneralPresenterTest extends BasePresenterTest {
    private static final int Event_ID_ConfigGeneralPresenterTest = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    //    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        EventBus.getDefault().register(this);

        if (configGeneralSQLiteEvent != null) {
            configGeneralSQLiteEvent.setId(Event_ID_ConfigGeneralPresenterTest);
        }
    }

    //    @Override
    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == Event_ID_ConfigGeneralPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static ConfigGeneral configGeneral = null;

        protected static final ConfigGeneral getConfigGeneral() throws CloneNotSupportedException {
            configGeneral = new ConfigGeneral();
            configGeneral.setName(ConfigGeneral.Config_SmallSheetNumber);
            configGeneral.setValue(ConfigGeneral.Config_SmallSheetNumber_Value);

            return (ConfigGeneral) configGeneral.clone();
        }

        protected static final List<?> getConfigGeneralList() throws CloneNotSupportedException {
            List<BaseModel> configGeneralList = new ArrayList<BaseModel>();
            for (int i = 0; i < 5; i++) {
                configGeneralList.add(getConfigGeneral());
            }
            return configGeneralList;
        }
    }

    @Test
    public void test_a_createSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        ConfigGeneral cg = DataInput.getConfigGeneral();
        cg = (ConfigGeneral) configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        ConfigGeneral cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(cg.compareTo(cgRetrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //case：重复插入
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        //异常case：插入的非空字段为null（插入失败）
        ConfigGeneral bm2 = DataInput.getConfigGeneral();
        bm2.setID(cgRetrieve1.getID());
        bm2.setName(null);
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b_retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        ConfigGeneral cg = DataInput.getConfigGeneral();
        cg = (ConfigGeneral) configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        ConfigGeneral cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(cg.compareTo(cgRetrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //异常case：根据ID查找，ID为null（找不到数据）
        cg.setID(null);
        cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1错误码不正确！");
        Assert.assertTrue(cgRetrieve1 == null, "retrieve1失败！");
    }

    @Test
    public void test_c_retrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(configGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Value = '%s'");
        configGeneral.setConditions(new String[]{"10"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确!");
        Assert.assertTrue(configGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");

        //异常Case：一个通配符多个值
        configGeneral.setSql("where F_Value = '%s'");
        configGeneral.setConditions(new String[]{"10", "123"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确!");

        //异常Case：多个通配符一个值
        configGeneral.setSql("where F_Value = '%s' and F_Name = '%s'");
        configGeneral.setConditions(new String[]{"10"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确!");

        //异常Case：多个通配符多个值
        configGeneral.setSql("where F_Value = '%s' and F_Name = '%s'");
        configGeneral.setConditions(new String[]{"10", "SmallSheetNumber"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确!");
        Assert.assertTrue(configGeneralList.size() > 0, "retrieveNSync返回的List的size应该大于0");
    }

    @Test
    public void test_d_createNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) DataInput.getConfigGeneralList();
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");
        for (ConfigGeneral configGeneral : configGeneralList) {
            ConfigGeneral cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
            Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync错误码不正确！");
            Assert.assertTrue(cg.compareTo(configGeneral) == 0, "retrieve1Sync失败！");
        }

        //case：重复插入
        configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");

        //异常case：插入的非空字段为null（插入失败）
        configGeneralList = (List<ConfigGeneral>) DataInput.getConfigGeneralList();
        configGeneralList.get(0).setValue(null);
        configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNSync错误码不正确！");
    }

    @Test
    public void test_e_updateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        ConfigGeneral cg = DataInput.getConfigGeneral();
        cg.setID(ConfigGeneral.PurchasingTimeoutTaskFlag);
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        ConfigGeneral cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue(cg.compareTo(cgRetrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //正常Case
        ConfigGeneral configGeneral = DataInput.getConfigGeneral();
        configGeneral.setID(cg.getID());
        configGeneral.setValue("20");
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Update超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "update错误码不正确！");
        //
        cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1错误码不正确！");
        Assert.assertTrue(cg.compareTo(configGeneral) == 0, "update失败！");
        //接收不一样的event
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Update超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "update错误码不正确！");
        //
        cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1错误码不正确！");
        Assert.assertTrue(cg.compareTo(configGeneral) == 0, "update失败！");

        //异常case：update不允许为null的数据为null
        configGeneral.setValue(null);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Update超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "update错误码不正确！");
        //
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Update超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "update错误码不正确！");

        //异常case：update不存在的数据
        configGeneral.setID(88888);
        //
        cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue(configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1错误码不正确！");
        Assert.assertTrue(cg == null, "update失败！");
        //
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Update超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "update错误码不正确！");
    }

    @Test
    public void test_f_refreshByServerDataAsyncC() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //清空数据，以防数据污染
        configGeneralPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //正常Case
        List<BaseModel> configGeneralList = (List<BaseModel>) DataInput.getConfigGeneralList();
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList, configGeneralSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, (List<BaseModel>) configGeneralSQLiteEvent.getListMasterTable(), configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");

        //异常case：插入的非空字段为null
        List<BaseModel> cgList = new ArrayList<BaseModel>();
        List<ConfigGeneral> configGeneralList1 = (List<ConfigGeneral>) DataInput.getConfigGeneralList();
        for (ConfigGeneral configGeneral : configGeneralList1) {
            configGeneral.setValue(null);
            cgList.add(configGeneral);
        }
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList1, configGeneralSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "refreshByServerDataAsync超时！");
        Assert.assertTrue(configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync错误码不正确!");
    }

    /**
     * 查询本地T_ConfigGeneral表的总条数
     */
    @Test
    public void test_retrieveNConfigGeneral() throws Exception {
        Shared.printTestMethodStartInfo();

//        ConfigGeneralPresenter configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        Integer total = configGeneralPresenter.retrieveCount();
        System.out.println("T_ConfigGeneral表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}
