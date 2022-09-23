package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BXConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.model.BXConfigGeneral;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BXConfigGeneralPresenter;
import com.bx.erp.utils.Shared;

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

/**
 * Created by BOXIN on 2019/7/12.
 */

public class BXConfigGeneralPresenterTest extends BaseAndroidTestCase {
    private static BXConfigGeneralPresenter bxConfigGeneralPresenter;
    private static BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent;

    private static final int Event_ID_BXConfigGeneralPresenterTest = 10000;

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

        bxConfigGeneralPresenter = GlobalController.getInstance().getBXConfigGeneralPresenter();
        if (bxConfigGeneralSQLiteEvent == null) {
            bxConfigGeneralSQLiteEvent = new BXConfigGeneralSQLiteEvent();
            bxConfigGeneralSQLiteEvent.setId(Event_ID_BXConfigGeneralPresenterTest);
        }
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == Event_ID_BXConfigGeneralPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    public static class DataInput {
        private static BXConfigGeneral bxConfigGeneral = null;

        protected static final BXConfigGeneral getBXConfigGeneral() throws CloneNotSupportedException {
            bxConfigGeneral = new BXConfigGeneral();
            bxConfigGeneral.setName(BXConfigGeneral.Config_SmallSheetNumber);
            bxConfigGeneral.setValue(BXConfigGeneral.Config_SmallSheetNumber_Value);
            bxConfigGeneral.setSyncDatetime(new Date());

            return (BXConfigGeneral) bxConfigGeneral.clone();
        }

        protected static final List<?> getBXConfigGeneralList() throws CloneNotSupportedException {
            List<BaseModel> bxConfigGeneralList = new ArrayList<BaseModel>();
            for (int i = 0; i < 5; i++) {
                bxConfigGeneralList.add(getBXConfigGeneral());
            }
            return bxConfigGeneralList;
        }
    }

    @Test
    public void test_a_createSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        BXConfigGeneral cg = DataInput.getBXConfigGeneral();
        BXConfigGeneral cgCreated = (BXConfigGeneral) bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("BXCreateSync bm1测试失败,原因:返回错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("BXCreateSync测试失败,原因:所插入数据与查询到的不一致!", cg.compareTo(cgCreated) == 0);

        //异常case：重复插入（插入失败）
        bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("BXCreateSync bm1测试失败,原因:返回错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        BXConfigGeneral bm2 = DataInput.getBXConfigGeneral();
        bm2.setID(cgCreated.getID());
        bm2.setName(null);
        bxConfigGeneralPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!", bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        BXConfigGeneral cg = DataInput.getBXConfigGeneral();
        cg.setID(1l);
        //
        BXConfigGeneral cgRetrieve1 = (BXConfigGeneral) bxConfigGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("retrieve1测试失败,原因:返回错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：根据ID查找，ID为null（找不到数据）
        cg.setID(null);
        cgRetrieve1 = (BXConfigGeneral) bxConfigGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, cg);
        Assert.assertTrue("retrieve1错误码不正确！", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("retrieve1失败！", cgRetrieve1 == null);
    }

    @Test
    public void test_c_retrieveNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNSync错误码不正确！", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", bxConfigGeneralList.size() > 0);
        //
        BXConfigGeneral bxConfigGeneral = new BXConfigGeneral();
        bxConfigGeneral.setSql("where F_Value = ?");
        bxConfigGeneral.setConditions(new String[]{"10"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", bxConfigGeneralList.size() > 0);

        //异常Case：一个通配符多个值
        bxConfigGeneral.setSql("where F_Value = ?");
        bxConfigGeneral.setConditions(new String[]{"10", "123"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：多个通配符一个值
        bxConfigGeneral.setSql("where F_Value = ? and F_Name = ?");
        bxConfigGeneral.setConditions(new String[]{"10"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", bxConfigGeneralList.size() >= 0);

        //异常Case：多个通配符多个值
        bxConfigGeneral.setSql("where F_Value = ? and F_Name = ?");
        bxConfigGeneral.setConditions(new String[]{"10", "SmallSheetNumber"});
        bxConfigGeneralList = (List<BXConfigGeneral>) bxConfigGeneralPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_BXConfigGeneral_RetrieveNByConditions, bxConfigGeneral);
        Assert.assertTrue("retrieveNSync错误码不正确!", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNSync返回的List的size应该大于0", bxConfigGeneralList.size() > 0);
    }

    @Test
    public void test_d_createNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BXConfigGeneral> bxConfigGeneralList = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        List<BXConfigGeneral> bxConfigGeneralListC = (List<BXConfigGeneral>) bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", bxConfigGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (int i = 0; i < bxConfigGeneralList.size(); i++) {
            Assert.assertTrue("retrieve1Sync失败！", bxConfigGeneralList.get(i).compareTo(bxConfigGeneralListC.get(i)) == 0);

        }

        //异常case：重复插入（插入失败）
        bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null（插入失败）
        bxConfigGeneralList = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        bxConfigGeneralList.get(0).setValue(null);
        bxConfigGeneralPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList);
        Assert.assertTrue("createNSync错误码不正确！", bxConfigGeneralPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }


    @Test
    public void test_f_refreshByServerDataAsyncC() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //清空数据，以防数据污染
        bxConfigGeneralPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //正常Case
        List<BaseModel> bxConfigGeneralList = (List<BaseModel>) DataInput.getBXConfigGeneralList();
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList, bxConfigGeneralSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", bxConfigGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常Case：重复插入(查找到重复后会删除再进行插入)
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, (List<BaseModel>) bxConfigGeneralSQLiteEvent.getListMasterTable(), bxConfigGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", bxConfigGeneralSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：插入的非空字段为null
        List<BaseModel> cgList = new ArrayList<BaseModel>();
        List<BXConfigGeneral> bxConfigGeneralList1 = (List<BXConfigGeneral>) DataInput.getBXConfigGeneralList();
        for (BXConfigGeneral bxConfigGeneral : bxConfigGeneralList1) {
            bxConfigGeneral.setValue(null);
            cgList.add(bxConfigGeneral);
        }
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        bxConfigGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bxConfigGeneralPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bxConfigGeneralList1, bxConfigGeneralSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("refreshByServerDataAsync超时！", bxConfigGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("refreshByServerDataAsync错误码不正确!", bxConfigGeneralSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    /**
     * 查询本地T_BXConfigGeneral表的总条数
     */
    @Test
    public void test_retrieveNBXConfigGeneral() throws Exception {
        Shared.printTestMethodStartInfo();

        BXConfigGeneralPresenter BXConfigGeneralPresenter = GlobalController.getInstance().getBXConfigGeneralPresenter();
        Integer total = BXConfigGeneralPresenter.retrieveCount();
        System.out.println("T_BXConfigGeneral表总条数：" + total);
        Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
