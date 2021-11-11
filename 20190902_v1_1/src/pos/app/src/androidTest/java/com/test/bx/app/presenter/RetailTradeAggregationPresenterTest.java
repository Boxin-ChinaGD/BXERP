package com.test.bx.app.presenter;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class RetailTradeAggregationPresenterTest extends BaseAndroidTestCase {
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter;

    private static final int Event_ID_RetailTradeAggregationPresenterTest = 10000;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == Event_ID_RetailTradeAggregationPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static RetailTradeAggregation retailTradeAggregation = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException {
            retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(1);
            retailTradeAggregation.setPosID(5);
            retailTradeAggregation.setTradeNO(10);
            retailTradeAggregation.setAmount(100);
            retailTradeAggregation.setReserveAmount(20);
            retailTradeAggregation.setCashAmount(10);
            retailTradeAggregation.setWechatAmount(20);
            retailTradeAggregation.setAlipayAmount(30);
            retailTradeAggregation.setWorkTimeStart(new Date());
            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(new Date(), 5));
            retailTradeAggregation.setUploadDateTime(new Date());

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }
    }

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

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_RetailTradeAggregationPresenterTest);
        }
        retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        EventBus.getDefault().unregister(this);
    }

    @Test
    public void test_a_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation bmCreateSync = DataInput.getRetailTradeAggregation();
        retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        //
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", bmCreateSync.compareTo(retailTradeAggregation) == 0);

        //异常case: 插入重复ID
        bmCreateSync.setID(retailTradeAggregation.getID());
        retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!", retailTradeAggregationPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<RetailTradeAggregation> rtcList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("RetrieveNSync搜索到的数据数量应该>=0!", rtcList.size() >= 0);
    }

    @Test
    public void test_d_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregation = createRetailTradeAggregation(rta);

        rta.setID(retailTradeAggregation.getID());
        retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm = RetailTradeAggregationPresenterTest.DataInput.getRetailTradeAggregation();
        bm.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm2 = retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Retrieve1 测试失败,返回的basemodel不为null", bm2 == null);
    }

    @Test
    public void test_e_DeleteSync() throws CloneNotSupportedException {
        RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregation = createRetailTradeAggregation(rta);
        rta.setID(retailTradeAggregation.getID());
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm = DataInput.getRetailTradeAggregation();
        bm.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm2 = retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("删除失败", bm2 == null);
    }

    @Test
    public void test_e_DeleteOutdatedASync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = "2018/10/01 00:00:00";

        try {
            RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
            rta.setWorkTimeStart(sdf.parse(date));
            rta.setWorkTimeEnd(sdf.parse(sdf.format(DatetimeUtil.addSecond(rta.getWorkTimeStart(), 5))));

            BaseModel bm = retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rta);
            Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
            Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", bm.compareTo(retailTradeAggregation) == 0);

            caseLog("删除七天前的数据");
            retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_DeleteOutdatedSync, bm);

            RetailTradeAggregation rtaDelete = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
            Assert.assertTrue("CreateSync测试失败,原因:没有正确删除过时的数据!", rtaDelete == null);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_f_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：重复插入（插入失败）
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case 1
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_RetrieveNAsync);
        retailTradeAggregationPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeAggregationSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        Assert.assertTrue("retrieveNAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会返回所有的数据", retailTradeAggregationSQLiteEvent.getListMasterTable().size() > 0);
        //
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setSql("where F_Tag = ?");
        retailTradeAggregation.setConditions(new String[]{"111"});
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_RetrieveNAsync);
        retailTradeAggregationPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("retrieveAsync超时！", false);
        }
        Assert.assertTrue("retrieveAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", retailTradeAggregationSQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_h_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("createObjectSync失败！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTradeAggregation b = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("retrieve1ObjectSync失败！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b != null);
        //
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        b = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("retrieve1ObjectSync失败！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b == null);

        //异常case：删除不存在的ID（不会出现异常）
        RetailTradeAggregation retailTradeAggregation1 = new RetailTradeAggregation();
        retailTradeAggregation1.setID(0l);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation1, retailTradeAggregationSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：删除的对象ID为null（不会出现异常）
        RetailTradeAggregation retailTradeAggregation2 = DataInput.getRetailTradeAggregation();
        retailTradeAggregation2.setID(null);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation2, retailTradeAggregationSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_i_deleteOldObjectAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        BaseModel bm = DataInput.getRetailTradeAggregation();
        bm.setID(1l);
        BaseModel bm2 = DataInput.getRetailTradeAggregation();
        bm2.setID(1l);
        ((RetailTradeAggregation) bm2).setAmount(1);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeAggregationPresenter.deleteOldObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, bm2, retailTradeAggregationSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    private RetailTradeAggregation createRetailTradeAggregation(RetailTradeAggregation reg) {
        retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, reg);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, reg);
        //
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", reg.compareTo(retailTradeAggregation) == 0);

        return retailTradeAggregation;
    }

    /**
     * 查询本地retailTradeAggregation表的总条数
     */
    @Test
    public void test_retrieveNRetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregationPresenter retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
        Integer total = retailTradeAggregationPresenter.retrieveCount();
        System.out.println("retailTradeAggregation表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

    /*
    * 更新本地retailTradeAggregation表的数据：更新多条retailTradeAggregation表的数据，测试是否更新成功
    * */
    @Test
    public void test_updateAsync() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();
        /*1.先插入一条零售单汇总*/
        RetailTradeAggregation bmUpdateSync = DataInput.getRetailTradeAggregation();
//        BaseActivity.retailTradeAggregation = bmUpdateSync;
        RetailTradeAggregation retailTradeAggregationCreated = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmUpdateSync);
        assertTrue("在SQLite创建零售单汇总失败", retailTradeAggregationCreated != null);
        RetailTradeAggregation retailTradeAggregationBeforeUpdate = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmUpdateSync);
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", bmUpdateSync.compareTo(retailTradeAggregationBeforeUpdate) == 0);
        System.out.println("更新零售单前的零售单汇总：" + retailTradeAggregationBeforeUpdate.toString());

        //下面做update：
        bmUpdateSync.setTradeNO(11);
        bmUpdateSync.setWorkTimeEnd(DatetimeUtil.addSecond(new Date(), 500));

        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        retailTradeAggregationPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmUpdateSync, retailTradeAggregationSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteEvent.getStatus(), false);
        }
        Assert.assertTrue("Update错误码不正确！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTradeAggregation retailTradeAggregationAfterUpdate = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmUpdateSync);//R1
        Assert.assertTrue("retrieve1ObjectSync错误码不正确！，错误码：" + retailTradeAggregationPresenter.getLastErrorCode(), retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update失败！", retailTradeAggregationAfterUpdate.compareTo(bmUpdateSync) == 0);

        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationAfterUpdate);
        Assert.assertTrue("delete错误码不正确！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_updateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation tmpCreateRetailTradeAggregation = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreated = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpCreateRetailTradeAggregation);
        assertTrue("在SQLite创建零售单汇总失败", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTradeAggregationCreated != null);

        RetailTradeAggregation tmpUpdateRetailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationCreated.clone();
        tmpUpdateRetailTradeAggregation.setAmount(1111);
        Assert.assertTrue("更新收银汇总失败！", retailTradeAggregationPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpUpdateRetailTradeAggregation));

        RetailTradeAggregation retailTradeAggregationR1 = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpUpdateRetailTradeAggregation);
        Assert.assertTrue("查询收银汇总失败", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTradeAggregationR1 != null);
        Assert.assertTrue("UpdateSync测试失败,原因:所更新的数据与查询到的不一致!", retailTradeAggregationR1.compareTo(tmpUpdateRetailTradeAggregation) == 0);
    }
}
