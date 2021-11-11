package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.RetailTradeCommoditySQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeCommodity;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RetailTradeCommodityPresenterTest extends BasePresenterTest {

    private static RetailTradeCommoditySQLiteEvent retailTradeCommoditySQLiteEvent;
//    private static long createAsyncID = 0l;
//    private static final int EVENT_ID1_CreateAsync = 1;
//    private static final int EVENT_ID2_CreateAsync = 2;
//    private static final int EVENT_ID3_CreateAsync = 3;
//
//    private static final int EVENT_ID1_RetrieveNAsync = 5;
//    private static final int EVENT_ID2_RetrieveNAsync = 6;
//    private static final int EVENT_ID3_RetrieveNAsync = 7;
//    private static final int EVENT_ID4_RetrieveNAsync = 8;
//
//    private static final int EVENT_ID1_DeleteAsync = 9;
//    private static final int EVENT_ID2_DeleteAsync = 10;
//    private static final int EVENT_ID3_DeleteAsync = 11;
//
//    private static final int EVENT_ID_CreateNAsync = 12;
//    private static final long Timeout = 30 * 1000;

    @BeforeClass
    public void setUp() {
        super.setUp();

        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeCommodityEvent(RetailTradeCommoditySQLiteEvent event) {
        event.onEvent();
    }

    public static class DataInput {
        private static RetailTradeCommodity rtcInput = new RetailTradeCommodity();

        protected static final RetailTradeCommodity getRetailTradeCommodity() {
            Random ran = new Random();
            rtcInput = new RetailTradeCommodity();
            rtcInput.setTradeID(1L);
            rtcInput.setCommodityID(ran.nextInt(5) + 1);
            rtcInput.setNO(ran.nextInt(999) + 1);
            rtcInput.setPriceOriginal(ran.nextInt(999) + 1);
            rtcInput.setDiscount(0.5f);
            rtcInput.setBarcodeID(1);
            rtcInput.setPriceReturn(1.0);
            rtcInput.setCommodityName("xxxxxxxxxx");

            return (RetailTradeCommodity) rtcInput.clone();
        }

        protected static final List<BaseModel> getRetailTradeCommodityList() throws Exception {
            List<BaseModel> commList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                commList.add(getRetailTradeCommodity());
            }
            return commList;
        }
    }

    @Test
    public void test_a_getMaxId() throws CloneNotSupportedException {

        RetailTradeCommodity text = DataInput.getRetailTradeCommodity();
        text = (RetailTradeCommodity) retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, text);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        long maxID = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);

        Assert.assertTrue(text.getID() == (maxID - 1), "获取的最大ID不正确！");
    }

    /**
     * 需要在测试前清空表时，请重命名函数名为test_a1_DeleteAllSync
     */
    @Test
    public void test_b_DeleteAllSync() {
        retailTradeCommodityPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        List<RetailTradeCommodity> tradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(tradeCommodityList.size() == 0, "RetrieveNSync搜索到的数据数量应该=0!");
    }

    @Test
    public void test_c_CreateNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<BaseModel> commodityList = DataInput.getRetailTradeCommodityList();
        List<?> commodityCreateList = retailTradeCommodityPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityList);

        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync1测试失败,原因:返回错误码不正确!");
        //
        for (int i = 0; i < commodityCreateList.size(); i++) {
            RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) commodityCreateList.get(i));
            Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync测试失败,原因retrieve1ObjectSync时返回的错误码不正确！");
            Assert.assertTrue(((BaseModel) commodityCreateList.get(i)).compareTo(retailTradeCommodity) == 0, "CreateNSync测试失败,原因:所插入数据与查询到的不一致!");
        }
    }

    @Test
    public void test_d_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeCommodity retailTradeCommodity = DataInput.getRetailTradeCommodity();
        RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity);

        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        RetailTradeCommodity comm1Retrieve1 = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodityCreate);
        //
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
        Assert.assertTrue(retailTradeCommodityCreate.compareTo(comm1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //异常case: 插入重复ID
        RetailTradeCommodity retailTradeCommodity1 = DataInput.getRetailTradeCommodity();
        retailTradeCommodity1.setID(comm1Retrieve1.getID());
        retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity1);

        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");

    }

    @Test
    public void test_e_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<RetailTradeCommodity> RetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(RetailTradeCommodityList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");

        //根据条件查询 //...将来增加case：输入更多的查询条件
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        retailTradeCommodity.setSql("where F_TradeID = %s");
        retailTradeCommodity.setConditions(new String[]{"1"});
        RetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(RetailTradeCommodityList.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>=0!");

        //正常的case,一个通配符多个值,只会获取第一个值
        RetailTradeCommodity retailTradeCommodity1 = new RetailTradeCommodity();
        retailTradeCommodity1.setSql("where F_TradeID = %s");
        retailTradeCommodity1.setConditions(new String[]{"1", "2"});
        retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity1);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常的case,多个通配符一个值
        RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
        retailTradeCommodity2.setSql("where F_ID = %s and F_TradeID = %s");
        retailTradeCommodity2.setConditions(new String[]{"1"});
        retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity2);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常的case,多个通配符多个值
        RetailTradeCommodity retailTradeCommodity3 = new RetailTradeCommodity();
        retailTradeCommodity3.setSql("where F_ID= %s and F_TradeID= %s");
        retailTradeCommodity3.setConditions(new String[]{"1", "1"});
        retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, retailTradeCommodity3);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

    }

    @Test
    public void test_f_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        RetailTradeCommodity retailTradeCommodity = DataInput.getRetailTradeCommodity();
        RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity);

        //正常的case
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        RetailTradeCommodity retailTradeCommodity1 = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodityCreate);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(retailTradeCommodity1 != null, "Retrieve1 测试失败,查询的数据不正确!");

        //异常的case,根据不存在的ID查询
        RetailTradeCommodity retailTradeCommodity2 = DataInput.getRetailTradeCommodity();
        retailTradeCommodity2.setID(Shared.SQLITE_ID_Infinite);
        RetailTradeCommodity retailTradeCommodity3 = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity2);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(retailTradeCommodity3 == null, "Retrieve1 测试失败,返回的basemodel不为null");
    }

    @Test
    public void test_g_DeleteSync() throws CloneNotSupportedException {

        RetailTradeCommodity retailTradeCommodity = DataInput.getRetailTradeCommodity();
        RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity);

        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        RetailTradeCommodity retailTradeCommodity1 = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodityCreate);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(retailTradeCommodity1 != null, "Retrieve1 测试失败,查询的数据不正确!");

        //正常的case
        RetailTradeCommodity retailTradeCommodity2 = DataInput.getRetailTradeCommodity();
        retailTradeCommodity2.setID(retailTradeCommodityCreate.getID());
        retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity2);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync 测试失败,原因返回错误码不正确!");
        RetailTradeCommodity retailTradeCommodity3 = (RetailTradeCommodity) retailTradeCommodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity2);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(retailTradeCommodity3 == null, "Retrieve1 测试失败,查询的数据不正确!");

        //删除一个ID不存在的对象
        RetailTradeCommodity retailTradeCommodity4 = DataInput.getRetailTradeCommodity();
        retailTradeCommodity4.setID(Shared.SQLITE_ID_Infinite);
        retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity4);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "DeleteSync 测试失败,原因返回错误码不正确!");

        //删除的对象的ID为NULL
        retailTradeCommodity4.setID(null);
        retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCommodity4);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "DeleteSync 测试失败,原因返回错误码不正确!");
    }

//    @Test
//    public void test_h_CreateAsync() throws CloneNotSupportedException {
//        Shared.printTestMethodStartInfo();
//
//        RetailTradeCommoditySQLiteEvent event = new RetailTradeCommoditySQLiteEvent();
//        event.setTimeout(Timeout);
//        event.setId(EVENT_ID1_CreateAsync);
//        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_CreateAsync);
//        RetailTradeCommodity bm = DataInput.getRetailTradeCommodity();
//        if (!rtcp.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Create bm1时错误码应该为：EC_NoError，Create失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//            Assert.assertTrue("插入数据后对象不应该为Null！", bmCreateAsync != null);
//            BaseModel bmCreate = rtcp.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
//            createAsyncID = bmCreate.getID();
//            Assert.assertTrue("插入后的对象与查找到的对象不一致，插入失败！", bmCreateAsync.compareTo(bmCreate) == 0);
//        }
//
//        //无ID
//        event.setId(EVENT_ID2_CreateAsync);
//        RetailTradeCommodity bm2 = DataInput.getRetailTradeCommodity();
//        if (!rtcp.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Create bm1时错误码应该为：EC_NoError，Create失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//            Assert.assertTrue("插入数据后对象不应该为Null！", bmCreateAsync != null);
//            BaseModel bmCreate = rtcp.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateAsync);
//            createAsyncID = bmCreate.getID();
//            Assert.assertTrue("插入后的对象与查找到的对象不一致，插入失败！", bmCreateAsync.compareTo(bmCreate) == 0);
//        }
//        //主键ID冲突
//        event.setId(EVENT_ID3_CreateAsync);
//        RetailTradeCommodity bm3 = DataInput.getRetailTradeCommodity();
//        bm3.setID(1l);
//        if (!rtcp.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("CreateAsync bm1测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Create bm1时错误码应该为：EC_NoError，Create失败", ErrorInfo.EnumErrorCode.EC_OtherError.equals(event.getLastErrorCode()));
//        }
//    }
//
//    @Test
//    public void test_i_RetrieveNAsync() {
//        Shared.printTestMethodStartInfo();
//
//        RetailTradeCommoditySQLiteEvent event = new RetailTradeCommoditySQLiteEvent();
//        event.setTimeout(Timeout);
//        event.setId(EVENT_ID1_RetrieveNAsync);
//        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_RetrieveNAsync);
//        if (!rtcp.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, event)) {
//            Assert.assertTrue("First RetrieveNAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("First RetrieveNAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First RetrieveN时错误码应该为：EC_NoError，RetrieveN失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//        }
//
//        event.setId(EVENT_ID2_RetrieveNAsync);
//        BaseModel baseModel1 = new BaseModel();
//        baseModel1.setSql("where F_TradeID = ?");
//        baseModel1.setConditions(new String[]{"0"});
//        if (!rtcp.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, baseModel1, event)) {
//            Assert.assertTrue("Second RetrieveNAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Second RetrieveNAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First RetrieveN时错误码应该为：EC_NoError，RetrieveN失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//            //... TODO
//        }
//
//        event.setId(EVENT_ID3_RetrieveNAsync);
//        BaseModel baseModel2 = new BaseModel();
//        baseModel2.setSql("where F_TradeID = ?");
//        baseModel2.setConditions(new String[]{"0", "1"});
//        if (!rtcp.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, baseModel2, event)) {
//            Assert.assertTrue("Thrid RetrieveNAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Thrid RetrieveNAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Thrid RetrieveN时错误码不应该为：EC_NoError，RetrieveN失败", !ErrorInfo.EnumErrorCode.EC_OtherError.equals(event.getLastErrorCode()));
//        }
//
//        event.setId(EVENT_ID4_RetrieveNAsync);
//        BaseModel baseModel3 = new BaseModel();
//        baseModel3.setSql("where F_TradeID = ? and F_CommodityID = ?");
//        baseModel3.setConditions(new String[]{"1"});
//        if (!rtcp.retrieveNObjectAsync(BaseSQLiteBO.CASE_RetailTradeCommodity_RetrieveNByConditions, baseModel3, event)) {
//            Assert.assertTrue("Fourth RetrieveNAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Fourth RetrieveNAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First RetrieveN时错误码应该为：EC_NoError，RetrieveN失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//        }
//    }
//
//    @Test
//    public void test_j_DeleteAsync() {
//        Shared.printTestMethodStartInfo();
//
//        RetailTradeCommoditySQLiteEvent event = new RetailTradeCommoditySQLiteEvent();
//        event.setTimeout(Timeout);
//        event.setId(EVENT_ID1_DeleteAsync);
//        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_DeleteAsync);
//        RetailTradeCommodity bm = DataInput.getRetailTradeCommodity();
//        bm.setID(createAsyncID);
//
//        if (!rtcp.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First Delete时错误码应该为：EC_NoError，DeleteA失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//        }
//
//        //不存在的ID
//        event.setId(EVENT_ID2_DeleteAsync);
//        RetailTradeCommodity bm2 = DataInput.getRetailTradeCommodity();
//        bm2.setID(-1l);
//        if (!rtcp.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First Delete时错误码应该为：EC_NoError，DeleteA失败", ErrorInfo.EnumErrorCode.EC_NoError.equals(event.getLastErrorCode()));
//        }
//
//        //传空
//        event.setId(EVENT_ID3_DeleteAsync);
//        if (!rtcp.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("Fourth DeleteAsync测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("First Delete时错误码应该为：EC_NoError，DeleteA失败", ErrorInfo.EnumErrorCode.EC_OtherError.equals(event.getLastErrorCode()));
//        }
//    }
//
//    @Test
//    public void test_k_CreateNAsync() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        RetailTradeCommoditySQLiteEvent event = new RetailTradeCommoditySQLiteEvent();
//        event.setId(EVENT_ID_CreateNAsync);
//        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeCommodity_CreateNAsync);
//        List<BaseModel> BarcodesList = DataInput.getRetailTradeCommodityList();
//        if (!rtcp.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList, event)) {
//            Assert.assertTrue("CreateNAsync测试失败!", false);
//        }
//
//        if (!waitForEventProcessed(event)) {
//            Assert.assertTrue("CreateNAsync测试失败!原因:超时", false);
//        } else {
//            Assert.assertTrue("CreateNAsync返回错误码不正确", event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//            for (int i = 0; i < BarcodesList.size(); i++) {
//                Assert.assertTrue("CreateNAsync的数据与原数据不符", BarcodesList.get(i).compareTo(rtcp.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateNAsync.get(i))) == 0);
//            }
//        }
//    }

    /**
     * 查询本地retailTradeCommodity表的总条数
     */
    @Test
    public void test_retrieveNRetailTradeCommodity() throws Exception {
        Shared.printTestMethodStartInfo();

//        RetailTradeCommodityPresenter retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        Integer total = retailTradeCommodityPresenter.retrieveCount();
        System.out.println("retailTradeCommodity表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
