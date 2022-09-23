package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.PromotionSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.promotion.BasePromotion;
import wpos.model.promotion.Promotion;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.util.*;

public class PromotionPresenterTest extends BasePresenterTest{
    private static final int Event_ID_Promotion_SQLiteEvent_PresenterTest = 10000;
    private static final int Event_ID_Promotion_HttpEvent_PresenterTest = 10001;

    @BeforeClass
    public void setUp() {
        super.setUp();

        EventBus.getDefault().register(this);

        if(promotionSQLiteEvent != null){
            promotionSQLiteEvent.setId(Event_ID_Promotion_SQLiteEvent_PresenterTest);
        }
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();

        EventBus.getDefault().unregister(this);
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

        private static Promotion promotion = null;
        private static Random r = new Random();

        public static final Promotion getPromotion() throws CloneNotSupportedException {
            promotion = new Promotion();
            promotion.setName(UUID.randomUUID().toString().substring(1, 7));
            promotion.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
            promotion.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            promotion.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // 活动开始时间
            promotion.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // 活动结束时间
            promotion.setExcecutionThreshold(r.nextInt(50) + 10);
            promotion.setExcecutionAmount(r.nextInt(10) + 1);
            promotion.setExcecutionDiscount(1 - r.nextDouble()); // 0<=ran.nextDouble()<1
            promotion.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
            promotion.setStaff(1);
            promotion.setSn("SN" + (System.currentTimeMillis() % 1000));
            promotion.setPageIndex("1");
            promotion.setPageSize("10");
            return (Promotion) promotion.clone();
        }

        public static final List<Promotion> getPromotionList(int size) throws Exception{
            List<Promotion> promotionList = new ArrayList<Promotion>();

            for(int i = 0; i < size; i++){
                promotionList.add(getPromotion());
            }

            return promotionList;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == Event_ID_Promotion_SQLiteEvent_PresenterTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1：正常创建多个Promotion");

        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(size);
        List<Promotion> list = (List<Promotion>) promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        Assert.assertTrue(list.size() == size, "createNObjectSync创建的数据条数不正确： size()=" + list.size());
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2：字段验证不通过");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setName("1234567890 1234567890 1234567890 1234567890");
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case3：必填字段为null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_PartSuccess, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1：正常创建");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue(promotion.compareTo(promotionCreated) == 0, "createNObjectSync失败，compareTo错误");
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2：重复创建");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue(promotion.compareTo(promotionCreated) == 0, "createNObjectSync失败，compareTo错误");

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionCreated);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case3：字段验证不通过");

        Promotion promotion = DataInput.getPromotion();
        promotion.setName(null);

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_b4_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case4：必填字段为null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_PartSuccess, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_c1_updateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm1 = createPromotion();
        bm1.setName("aaa");
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateSync返回的错误码不正确！");
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync返回的错误码不正确！");
        Assert.assertTrue(bm1.getName().equals(p.getName()), "查找到修改的对象");
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case: Update非空字段为null
        Promotion p = DataInput.getPromotion();
        p.setName(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "updateSync返回的错误码不正确！");
        //
    }

    @Test
    public void test_c3_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：update的对象ID为null
        Promotion p = DataInput.getPromotion();
        p.setID(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "updateSync返回的错误码不正确！");
    }


    @Test
    public void test_d1_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: 正常case
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm1 = DataInput.getPromotion();
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, promotionSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm1测试失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createAsync Promotion超时！" + promotionSQLiteEvent.getStatus());
        bm1.setIgnoreIDInComparision(true);
        Assert.assertTrue(bm1.compareTo(p) == 0, "插入数据后对象不应该为Null！");
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Create bm1时错误码应该为：EC_NoError，Create失败");
    }

    @Test
    public void test_d2_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case：设置某一非空字段为空，插入时产生异常
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm2 = DataInput.getPromotion();
        bm2.setName(null);

        Assert.assertFalse(promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, promotionSQLiteEvent), "createObjectAsync错误");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createAsync Promotion超时！" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "Create bm1时错误码不应该为：EC_WrongFormatForInputField，测试失败");
    }

    @Test
    public void test_d3_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        Promotion p = createPromotion();
        //case：主键冲突
        Promotion bm3 = DataInput.getPromotion();
        bm3.setID(p.getID());
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent)) {
            Assert.assertTrue(false, "CreateAsync bm3测试失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done, "createAsync Promotion超时！" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "Create bm1时错误码不应该为：EC_NoError，测试失败");

        //注意：创建成功的还要保证数据正确
    }

    @Test
    public void test_e1_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正确的修改
        BaseModel bm = DataInput.getPromotion();
        Promotion createPromotion = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        createPromotion.setName("asd");
        if (!promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue(false, "UpdateAsync bm1测试失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (!promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue(false, "UpdateAsync bm1测试失败！");
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createAsync Promotion超时！" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(createPromotion.compareTo(p) == 0, "插入数据后对象不应该为Null！");
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Create bm1时错误码应该为：EC_NoError，Create失败");
    }

    @Test
    public void test_e2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //修改非空字段为空
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion createPromotion = createPromotion();
        createPromotion.setName(null);

        Assert.assertFalse(promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent), "updateObjectAsync错误");
        //
//        if (!waitForEventProcessed(promotionSQLiteEvent)) {
//            Assert.assertTrue("UpdateAsync bm3测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Update bm3时错误码不应该为：EC_NoError，Update失败", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
//        }
    }

    @Test
    public void test_e3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //不传递ID去进行修改
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion bm3 = DataInput.getPromotion();
        bm3.setName("lalalalalala");
        //
        Assert.assertFalse(promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent), "updateObjectAsync错误");
        //
//        if (!waitForEventProcessed(promotionSQLiteEvent)) {
//            Assert.assertTrue("UpdateAsync bm2测试失败！原因:超时", false);
//        } else {
//            Assert.assertTrue("Update bm2时错误码不应该为：EC_NoError，Update失败", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
//        }
    }

    @Test
    public void test_f1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm = DataInput.getPromotion();
        Promotion createPromotion = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync返回的错误码不正确！");
        Assert.assertTrue(createPromotion != null, "查找到创建的对象");
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
        if (!promotionPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue(false, "DeleteAsync bm1测试失败！");
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Promotion删除超时!");
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "promotion删除返回的错误码不正确！");
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync返回的错误码不正确！");
        Assert.assertTrue(createPromotion == null, "应该查找不到创建的对象");
    }

    @Test
    public void test_f2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：id为null
        Promotion createPromotion = createPromotion();
        //
        createPromotion.setID(null);
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
        Assert.assertFalse(promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent), "updateObjectAsync错误");
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "Promotion删除超时!");
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "promotion删除返回的错误码不正确！");
        //
    }

    @Test
    public void test_g1_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm1 = DataInput.getPromotion();
        bm1 = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync的错误码不正确");
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync错误码不正确");
        Assert.assertTrue(p != null, "查找创建的对象成功");
        //
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        //
        p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync错误码不正确");
        Assert.assertTrue(p == null, "查找创建的对象成功");
    }

    @Test
    public void test_g2_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的对象，没有ID
        Promotion bm1 = DataInput.getPromotion();
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "Retrieve1 测试失败,原因返回错误码不正确!");
    }

    @Test
    public void test_h1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Promotion> bmList = (List<Promotion>) DataInput.getPromotionList(5);
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, promotionSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createNAsync超时!" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync返回的错误码不正确");
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync返回的错误码不正确！");
            Assert.assertTrue(promotion1 != null, "查找不到创建的对象");
        }
    }
    @Test
    public void test_h2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Promotion> bmList = (List<Promotion>) DataInput.getPromotionList(5);
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, promotionSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createNAsync超时!" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync返回的错误码不正确");
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1Sync返回的错误码不正确！");
            Assert.assertTrue(promotion1 != null, "查找不到创建的对象");
        }

        //异常case：重复插入
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, promotionList, promotionSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createNAsync超时!" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync返回的错误码不正确");
    }

    @Test
    public void test_h3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：非空字段为null
        List<Promotion> promotionList = (List<Promotion>) DataInput.getPromotionList(5);
        for (int i = 0; i < promotionList.size(); i++) {
            promotionList.get(i).setName(null);
        }
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, promotionList, promotionSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "createNAsync超时!" + promotionSQLiteEvent.getStatus());
        Assert.assertTrue(promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNAsync返回的错误码不正确");
    }

    @Test
    public void test_i1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("正常查询");

        Promotion promotion = createPromotion();
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue(promotion.compareTo(promotionR) == 0, "compareTo不正确");
    }

    @Test
    public void test_i2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("查询ID为0");

        Promotion promotion = createPromotion();
        promotion.setID(0);
        promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());

    }

    @Test
    public void test_i3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("查询不存在的ID");

        Promotion promotion = DataInput.getPromotion();
        promotion.setID(999999);
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        Assert.assertTrue(promotionR == null, "retrieve1ObjectSync查询不存在的ID不应该有数据");

    }

    @Test
    public void test_j1_retrieveNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("正常查询");

        //创建一个Promotion
        createPromotion();
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RetrieveNAsync);
        promotionPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, promotionSQLiteEvent);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue(false, "Promotion同步超时!");
        }
        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue(PromotionList.size() > 0, "ListMasterTable为空");
    }



    private Promotion createPromotion() throws Exception{
        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode());
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue(promotion.compareTo(promotionCreated) == 0, "createNObjectSync失败，compareTo错误");

        return promotionCreated;
    }

    /**
     * 查询本地Promotion表的总条数
     */
    @Test
    public void test_retrieveNPromotion() throws Exception {
        Shared.printTestMethodStartInfo();

//        PromotionPresenter promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        Integer total = promotionPresenter.retrieveCount();
        System.out.println("Promotion表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}
