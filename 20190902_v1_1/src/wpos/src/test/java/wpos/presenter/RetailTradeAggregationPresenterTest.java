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
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeAggregation;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RetailTradeAggregationPresenterTest extends BasePresenterTest {
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    private static final int Event_ID_RetailTradeAggregationPresenterTest = 10000;

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
    public void setUp() {
        super.setUp();

        EventBus.getDefault().register(this);

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_RetailTradeAggregationPresenterTest);
        }
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

    @Test
    public void test_a_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreate = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);

        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        RetailTradeAggregation retailTradeAggregationR1 = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate);
        //
        Assert.assertTrue(retailTradeAggregationCreate.compareTo(retailTradeAggregationR1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //异常case: 插入重复ID
        retailTradeAggregation.setID(retailTradeAggregationCreate.getID());
        retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);

        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_c_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<RetailTradeAggregation> rtcList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(rtcList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");
    }

    @Test
    public void test_d_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregation = createRetailTradeAggregation(rta);

        rta.setID(retailTradeAggregation.getID());
        retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm = RetailTradeAggregationPresenterTest.DataInput.getRetailTradeAggregation();
        bm.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm2 = retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm2 == null, "Retrieve1 测试失败,返回的basemodel不为null");
    }

    @Test
    public void test_e_DeleteSync() throws CloneNotSupportedException {
        RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregation = createRetailTradeAggregation(rta);
        rta.setID(retailTradeAggregation.getID());
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rta);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm = DataInput.getRetailTradeAggregation();
        bm.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm2 = retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm2 == null, "删除失败");
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
            Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

            RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
            Assert.assertTrue(bm.compareTo(retailTradeAggregation) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

            Shared.caseLog("删除七天前的数据");
            retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_DeleteOutdatedSync, bm);

            RetailTradeAggregation rtaDelete = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
            Assert.assertTrue(rtaDelete == null, "CreateSync测试失败,原因:没有正确删除过时的数据!");


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
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");

        //异常case：重复插入（插入失败）
        retailTradeAggregation.setID(retailTradeAggregationSQLiteEvent.getBaseModel1().getID());
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError, "createAsync错误码不正确！");
    }

    @Test
    public void test_g_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case 1
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_RetrieveNAsync);
        retailTradeAggregationPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeAggregationSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync会返回所有的数据");
        //
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setSql("where F_Tag = ?");
        retailTradeAggregation.setConditions(new String[]{"111"});
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_RetrieveNAsync);
        retailTradeAggregationPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation, retailTradeAggregationSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "retrieveAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveAsync错误码不正确！");
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync会有查询到符合条件的数据");
    }

    @Test
    public void test_h_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreate = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        RetailTradeAggregation b = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查询失败！");
        //
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate, retailTradeAggregationSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");
        //
        b = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查询失败！");

        //异常case：删除不存在的ID（不会出现异常）
        RetailTradeAggregation retailTradeAggregation1 = new RetailTradeAggregation();
        retailTradeAggregation1.setID(0);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate, retailTradeAggregationSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteAsync错误码不正确！");

        //异常case：删除的对象ID为null
        RetailTradeAggregation retailTradeAggregation2 = DataInput.getRetailTradeAggregation();
        retailTradeAggregation2.setID(null);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_DeleteAsync);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation2, retailTradeAggregationSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_i_deleteOldObjectAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        BaseModel bm = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreate1 = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        BaseModel bm2 = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreate2 = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        ((RetailTradeAggregation) bm2).setAmount(1);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeAggregationPresenter.deleteOldObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate1, retailTradeAggregationCreate2, retailTradeAggregationSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(retailTradeAggregationSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    private RetailTradeAggregation createRetailTradeAggregation(RetailTradeAggregation reg) {
        RetailTradeAggregation retailTradeAggregationCreate = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, reg);

        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationCreate);
        //
        Assert.assertTrue(retailTradeAggregationCreate.compareTo(retailTradeAggregation) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        return retailTradeAggregation;
    }

    /**
     * 查询本地retailTradeAggregation表的总条数
     */
    @Test
    public void test_retrieveNRetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();

//        RetailTradeAggregationPresenter retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
        Integer total = retailTradeAggregationPresenter.retrieveCount();
        System.out.println("retailTradeAggregation表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
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
        Assert.assertTrue(retailTradeAggregationCreated != null, "在SQLite创建零售单汇总失败");
        System.out.println("更新零售单前的零售单汇总：" + retailTradeAggregationCreated.toString());

        //下面做update：
        RetailTradeAggregation retailTradeAggregationToUpdate = (RetailTradeAggregation) retailTradeAggregationCreated.clone();
        retailTradeAggregationToUpdate.setTradeNO(11);
        retailTradeAggregationToUpdate.setWorkTimeEnd(DatetimeUtil.addSecond(new Date(), 500));

        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        retailTradeAggregationPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationToUpdate, retailTradeAggregationSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteEvent.getStatus());
        }
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");
        //
        RetailTradeAggregation retailTradeAggregationAfterUpdate = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationToUpdate);//R1
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！，错误码：" + retailTradeAggregationPresenter.getLastErrorCode());
        Assert.assertTrue(retailTradeAggregationAfterUpdate.compareTo(retailTradeAggregationToUpdate) == 0, "update失败！");

        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregationAfterUpdate);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "delete错误码不正确！");
    }

    @Test
    public void test_updateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradeAggregation tmpCreateRetailTradeAggregation = DataInput.getRetailTradeAggregation();
        RetailTradeAggregation retailTradeAggregationCreated = (RetailTradeAggregation) retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpCreateRetailTradeAggregation);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTradeAggregationCreated != null, "在SQLite创建零售单汇总失败");

        RetailTradeAggregation tmpUpdateRetailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationCreated.clone();
        tmpUpdateRetailTradeAggregation.setAmount(1111);
        Assert.assertTrue(retailTradeAggregationPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpUpdateRetailTradeAggregation), "更新收银汇总失败！");

        RetailTradeAggregation retailTradeAggregationR1 = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tmpUpdateRetailTradeAggregation);
        Assert.assertTrue(retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTradeAggregationR1 != null, "查询收银汇总失败");
        Assert.assertTrue(retailTradeAggregationR1.compareTo(tmpUpdateRetailTradeAggregation) == 0, "UpdateSync测试失败,原因:所更新的数据与查询到的不一致!");
    }
}
