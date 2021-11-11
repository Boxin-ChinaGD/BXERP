package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.utils.Shared;
//import com.bx.erp.view.activity.SmallSheetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_ConfigGeneral_UpdateByServerData;
import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;


public class ConfigGeneralPresenterTest extends BaseAndroidTestCase {
    private static ConfigGeneralPresenter configGeneralPresenter;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;

    private static final int Event_ID_ConfigGeneralPresenterTest = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        EventBus.getDefault().register(this);

        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        if (configGeneralSQLiteEvent == null){
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(Event_ID_ConfigGeneralPresenterTest);
        }
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if(event.getId() == Event_ID_ConfigGeneralPresenterTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
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
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        ConfigGeneral cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", cg.compareTo(cgRetrieve1) == 0);

        //异常case：重复插入（插入失败）
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        ConfigGeneral bm2 = DataInput.getConfigGeneral();
        bm2.setID(cgRetrieve1.getID());
        bm2.setName(null);
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!", configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        ConfigGeneral cg = DataInput.getConfigGeneral();
        configGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        ConfigGeneral cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("retrieve1测试失败,原因:返回错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", cg.compareTo(cgRetrieve1) == 0);

        //异常case：根据ID查找，ID为null（找不到数据）
        cg.setID(null);
        cgRetrieve1 = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("retrieve1错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1失败！", cgRetrieve1 == null);
    }

    @Test
    public void test_c_retrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNSync错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", configGeneralList.size() > 0);
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Value = ?");
        configGeneral.setConditions(new String[]{"10"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", configGeneralList.size() > 0);

        //异常Case：一个通配符多个值
        configGeneral.setSql("where F_Value = ?");
        configGeneral.setConditions(new String[]{"10", "123"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：多个通配符一个值
        configGeneral.setSql("where F_Value = ? and F_Name = ?");
        configGeneral.setConditions(new String[]{"10"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", configGeneralList.size() >= 0);

        //异常Case：多个通配符多个值
        configGeneral.setSql("where F_Value = ? and F_Name = ?");
        configGeneral.setConditions(new String[]{"10", "SmallSheetNumber"});
        configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", configGeneralList.size() > 0);
    }

    @Test
    public void test_d_createNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) DataInput.getConfigGeneralList();
        configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (ConfigGeneral configGeneral : configGeneralList) {
            ConfigGeneral cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
            Assert.assertTrue("retrieve1Sync错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("retrieve1Sync失败！", cg.compareTo(configGeneral) == 0);
        }

        //异常case：重复插入（插入失败）
        configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        configGeneralList = (List<ConfigGeneral>) DataInput.getConfigGeneralList();
        configGeneralList.get(0).setValue(null);
        configGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", configGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e_updateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case
        ConfigGeneral configGeneral = DataInput.getConfigGeneral();
        configGeneral.setID(Long.valueOf(ConfigGeneral.PurchasingTimeoutTaskFlag));
        configGeneral.setValue("20");
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Update超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("update错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        ConfigGeneral cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue("retrieve1错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", cg.compareTo(configGeneral) == 0);
        //接收不一样的event
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Update超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("update错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue("retrieve1错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", cg.compareTo(configGeneral) == 0);

        //异常case：update不允许为null的数据为null
        configGeneral.setValue(null);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Update超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("update错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
        //
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        configGeneralPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Update超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("update错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：update不存在的数据
        configGeneral.setID(88888l);
        //
        cg = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        Assert.assertTrue("retrieve1错误码不正确！", configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", cg == null);
        //
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateByServerDataAsync);
        configGeneralPresenter.updateObjectAsync(CASE_ConfigGeneral_UpdateByServerData, configGeneral, configGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Update超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("update错误码不正确！", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        configGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, (List<BaseModel>) configGeneralSQLiteEvent.getListMasterTable(), configGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", configGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

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
        lTimeOut = UNIT_TEST_TimeOut;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", configGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 查询本地T_ConfigGeneral表的总条数
     */
    @Test
    public void test_retrieveNConfigGeneral() throws Exception {
        Shared.printTestMethodStartInfo();

        ConfigGeneralPresenter configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        Integer total = configGeneralPresenter.retrieveCount();
        System.out.println("T_ConfigGeneral表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
