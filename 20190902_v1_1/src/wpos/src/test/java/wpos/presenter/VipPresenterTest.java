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
import wpos.event.UI.VipSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Vip;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static wpos.utils.Shared.UNIT_TEST_TimeOut;

public class VipPresenterTest extends BasePresenterTest {

    @Resource
    private VipSQLiteEvent vipSQLiteEvent;

    private static final int Event_ID_VipPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();
//        if (vipSQLiteEvent == null) {
//            vipSQLiteEvent = new VipSQLiteEvent();
//            vipSQLiteEvent.setId(Event_ID_VipPresenterTest);
//        }
        vipSQLiteEvent.setId(Event_ID_VipPresenterTest);
        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    public static class DataInput {
        private static Vip vip = new Vip();

        protected static final Vip getVip() throws Exception {
            Random ran = new Random();
            vip = new Vip();
            vip.setMobile("1212" + String.valueOf(System.currentTimeMillis()).substring(6));
//            vip.setCardID(1);
//            vip.setICID("320803199707" + String.valueOf(System.currentTimeMillis()).substring(7));
            vip.setICID(Shared.getValidICID());
            Thread.sleep(1);
            vip.setName(Shared.generateCompanyName(ran.nextInt(10) + 1));
            Thread.sleep(1);
            vip.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
            vip.setConsumeTimes(0);
            vip.setConsumeAmount(0);
            vip.setDistrict("广州");
            vip.setCategory(1);
            vip.setBirthday(new Date());
            vip.setBonus(0);
            vip.setLastConsumeDatetime(new Date());
//            vip.setLocalPosSN("123456");
            return (Vip) vip.clone();
        }

        protected static final List<BaseModel> getVipList() throws Exception {
            List<BaseModel> vipList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                vipList.add(getVip());
            }
            return vipList;
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == Event_ID_VipPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Test
    public void test_i_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<BaseModel> vipList = DataInput.getVipList();
        vipPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipList);

        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1测试失败,原因:返回错误码不正确!");
        //
        for (int i = 0; i < vipList.size(); i++) {
            Assert.assertTrue(vipList.get(i).compareTo(vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipList.get(i))) == 0, "CreateNSync测试失败,原因:所插入数据与查询到的不一致!");
        }
    }

    @Test
    public void test_a1_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Vip vip1Retrieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        //
        Assert.assertTrue(bmCreateSync.compareTo(vip1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
    }

    @Test
    public void test_a2_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //插入重复ID， 会直接select出来，不会报错
        Vip vip = DataInput.getVip();
        Vip bmCreateSync = createVip(vip);

        bmCreateSync.setID(bmCreateSync.getID());
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_a3_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        // 插入null作为非空字段的值
        Vip bm2 = DataInput.getVip();
        Vip bmCreateSync = createVip(bm2);

        bm2.setID(bmCreateSync.getID());
        bm2.setMobile(null);
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);

        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "CreateSync bm2测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_b1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        Vip bm1 = DataInput.getVip();
        bm1.setID(bmCreateSync.getID());
        bm1.setMobile("13144496181");

        //正常修改的case：update的字段不为空
        vipPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1测试失败,原因:返回错误码不正确!");

        Vip comm1Rerieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确!");
        Assert.assertTrue(bm1.compareTo(comm1Rerieve1) == 0, "update");
    }

    @Test
    public void test_b2_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case: Update非空字段为null

        Vip bm1 = DataInput.getVip();
        bm1.setMobile(null);
        vipPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "updateObjectSync错误码不正确！");
    }

    @Test
    public void test_c_RetrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Vip> VipList = (List<Vip>) vipPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(VipList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");

        //根据条件查询 //...将来增加case：输入更多的查询条件
        Vip vip = new Vip();
//        vip.setSql("where F_CardID = ?");
        vip.setSql("where F_CardID = %s;");
        vip.setConditions(new String[]{"0"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        VipList = (List<Vip>) vipPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, vip);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(VipList.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>=0!");
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!");

        //正常查询的case
        Vip bm1 = new Vip();
        bm1.setID(bmCreateSync.getID());
        vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
    }

    @Test
    public void test_d2_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //ID不存在的case
        BaseModel bm2 = DataInput.getVip();
        bm2.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm3 = vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "Retrieve1 测试失败,返回的basemodel不为null");
    }

    @Test
    public void test_e_DeleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败！");

        Vip bm1 = new Vip();
        bm1.setID(bmCreateSync.getID());
        vipPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync 测试失败,原因返回错误码不正确!");

        BaseModel bm2 = DataInput.getVip();
        bm2.setID(bmCreateSync.getID());
        BaseModel bm3 = vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "删除失败；");
    }

    @Test
    public void test_a1_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bm = DataInput.getVip();
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        //正常创建case
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(bm.compareTo(v) == 0, "create失败！");
    }

    @Test
    public void test_a2_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //重复创建case， 插入成功
        Vip vip = DataInput.getVip();
        Vip bmCreated = createVip(vip);
        vip.setID(bmCreated.getID());
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, vip, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_a3_CreateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        Vip bm4 = DataInput.getVip();
        bm4.setMobile(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync错误码不正确！");
    }

    @Test
    public void test_b1_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!");

        //正常修改.
        Vip bm = DataInput.getVip();
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        bm.setID(bmCreateSync.getID());
        bm.setMobile("测试修改Mobile");
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update错误码不正确！");

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确！");
        Assert.assertTrue(bm.compareTo(v) == 0, "update失败！");
    }

    @Test
    public void test_b2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //model无ID参数
        Vip bm2 = DataInput.getVip();
        bm2.setMobile("无ID参数测试");
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "Update错误码不正确！");
    }

    @Test
    public void test_b3_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //修改字段为空
        Vip vip = DataInput.getVip();
        Vip bmCreateSync = createVip(vip);
        vip.setID(bmCreateSync.getID());
        vip.setMobile(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, vip, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "Update超时");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "Update错误码不正确！");
    }

    @Test
    public void test_b4_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //空model
        Vip bm4 = null;
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        try {
            vipPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, vipSQLiteEvent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(e.getMessage() == null, "异常不正确");
        }
    }

    @Test
    public void test_c_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常查询的Case
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(vipSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync会返回所有的数据");
    }

    // TODO 会员没有状态字段了
//    @Test
//    public void test_c2_RetrieveNAsync() throws InterruptedException {
//        Shared.printTestMethodStartInfo();
//        //根据状态查询
//        Vip v = new Vip();
//        v.setSql("where F_Status = ?");
//        v.setConditions(new String[]{"0"});
//        v.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
//        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v, vipSQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            Assert.assertTrue("retrieveNAsync超时！", false);
//        }
//        Assert.assertTrue("retrieveNAsync错误码不正确！" + vipSQLiteEvent.printErrorInfo(), vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", vipSQLiteEvent.getListMasterTable().size() > 0);
//    }

    @Test
    public void test_c3_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常的case,一个通配符多个值
        Vip v2 = new Vip();
        v2.setSql("where F_Status = %s");
        v2.setConditions(new String[]{"1", "0"});
        v2.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
    }

    @Test
    public void test_c4_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        ////异常Case：多个通配符一个值
        Vip v3 = new Vip();
        v3.setSql("where F_Status = %s and F_POS_SN = %s");
        v3.setConditions(new String[]{"1"});
        v3.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v3, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(vipSQLiteEvent.getListMasterTable() == null, "retrieveNAsync会有查询到符合条件的数据");
    }

    @Test
    public void test_c5_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case：多个通配符多个值
        Vip v4 = new Vip();
        v4.setSql("where F_CardID = %s and F_LocalPosSN = %s;");
        v4.setConditions(new String[]{"1", "0"});
        v4.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, v4, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！" + vipSQLiteEvent.printErrorInfo());
        Assert.assertTrue(vipSQLiteEvent.getListMasterTable().size() >= 0, "retrieveNAsync会有查询到符合条件的数据");
    }

    @Test
    public void test_d_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip bmCreateSync = DataInput.getVip();
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        //正常的Case
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "DeleteAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteAsync错误码不正确！");

        Vip v = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteAsync失败！");
        Assert.assertTrue(v == null, "查询失败！");
    }

    @Test
    public void test_d2_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //不存在的ID, 会抛异常
        Vip v1 = DataInput.getVip();
        v1.setID(Shared.SQLITE_ID_Infinite);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, v1, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "DeleteAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "DeleteAsync错误码不正确！");
    }

    @Test
    public void test_d3_DeleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Vip v2 = DataInput.getVip();
        v2.setID(null);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_DeleteAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, v2, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "DeleteAsync超时！");
        }
        Assert.assertTrue(vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "DeleteAsync错误码不正确！");
    }

    @Test
    public void test_j_CreateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateNAsync);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        List<BaseModel> BarcodesList = DataInput.getVipList();
        vipPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList, vipSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "DeleteAsync超时！");
        }
        Assert.assertTrue( vipSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"CreateNAsync返回错误码不正确");

        for (int i = 0; i < BarcodesList.size(); i++) {
            Assert.assertTrue(BarcodesList.get(i).compareTo(vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i))) == 0, "CreateNAsync的数据与原数据不符");
        }
    }

    private Vip createVip(Vip vip) {
        vipPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vip);

        Assert.assertTrue(vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Vip vip1Retrieve1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vip);
        //
        Assert.assertTrue(vip.compareTo(vip1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        return vip1Retrieve1;
    }

    /**
     * 查询本地VIP表的总条数
     */
//    @Test
//    public void test_retrieveNVip() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        VipPresenter vipPresenter = GlobalController.getInstance().getVipPresenter();
//        Integer total = vipPresenter.retrieveCount();
//        System.out.println("VIP表总条数：" + total);
//        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
//    }

}
