package com.test.bx.app.presenter;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.promotion.BasePromotion;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;

import junit.framework.Assert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PromotionPresenterTest extends BaseAndroidTestCase {

    private static PromotionPresenter promotionPresenter;
    private static PromotionSQLiteEvent promotionSQLiteEvent;
    private static PromotionHttpEvent promotionHttpEvent;

    private static final int Event_ID_Promotion_SQLiteEvent_PresenterTest = 10000;
    private static final int Event_ID_Promotion_HttpEvent_PresenterTest = 10001;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        if(promotionSQLiteEvent == null){
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(Event_ID_Promotion_SQLiteEvent_PresenterTest);
        }
        if(promotionHttpEvent == null){
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(Event_ID_Promotion_HttpEvent_PresenterTest);
        }

    }

    @Override
    public void tearDown() throws Exception {
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
            List<Promotion> promotionList = new ArrayList<>();

            for(int i = 0; i < size; i++){
                promotionList.add(getPromotion());
            }

            return promotionList;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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
        caseLog("case1：正常创建多个Promotion");

        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(size);
        List<Promotion> list = (List<Promotion>) promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync创建的数据条数不正确： size()=" + list.size(), list.size() == size);
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2：字段验证不通过");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setName("1234567890 1234567890 1234567890 1234567890");
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3：必填字段为null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);
        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1：正常创建");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync失败，compareTo错误", promotion.compareTo(promotionCreated) == 0);
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2：重复创建");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync失败，compareTo错误", promotion.compareTo(promotionCreated) == 0);

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3：字段验证不通过");

        Promotion promotion = DataInput.getPromotion();
        promotion.setName(null);

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_b4_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case4：必填字段为null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_c1_updateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm1 = createPromotion();
        bm1.setName("aaa");
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("updateSync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找到修改的对象", bm1.getName().equals(p.getName()));
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常Case: Update非空字段为null
        Promotion p = DataInput.getPromotion();
        p.setName(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateSync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        //
    }

    @Test
    public void test_c3_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：update的对象ID为null
        Promotion p = DataInput.getPromotion();
        p.setID(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateSync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    @Test
    public void test_d1_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: 正常case
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm1 = DataInput.getPromotion();
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, promotionSQLiteEvent)) {
            Assert.assertTrue("CreateAsync bm1测试失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue("createAsync Promotion超时！" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("插入数据后对象不应该为Null！", bm1.compareTo(p) == 0);
        Assert.assertTrue("Create bm1时错误码应该为：EC_NoError，Create失败", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d2_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case：设置某一非空字段为空，插入时产生异常
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm2 = DataInput.getPromotion();
        bm2.setName(null);

        Assert.assertFalse("createObjectAsync错误", promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, promotionSQLiteEvent));
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createAsync Promotion超时！" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("Create bm1时错误码不应该为：EC_WrongFormatForInputField，测试失败", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d3_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        Promotion p = createPromotion();
        //case：主键冲突
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm3 = DataInput.getPromotion();
        bm3.setID(p.getID());
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent)) {
            Assert.assertTrue("CreateAsync bm3测试失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createAsync Promotion超时！" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("Create bm1时错误码不应该为：EC_NoError，测试失败", promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

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
            Assert.assertTrue("UpdateAsync bm1测试失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (!promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm1测试失败！", false);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue("createAsync Promotion超时！" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("插入数据后对象不应该为Null！", createPromotion.compareTo(p) == 0);
        Assert.assertTrue("Create bm1时错误码应该为：EC_NoError，Create失败", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //修改非空字段为空
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion createPromotion = createPromotion();
        createPromotion.setName(null);

        Assert.assertFalse("updateObjectAsync错误", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent));
        //
        if (!waitForEventProcessed(promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm3测试失败！原因:超时", false);
        } else {
            Assert.assertTrue("Update bm3时错误码不应该为：EC_NoError，Update失败", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
        }
    }

    @Test
    public void test_e3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //不传递ID去进行修改
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion bm3 = DataInput.getPromotion();
        bm3.setName("lalalalalala");
        //
        Assert.assertFalse("updateObjectAsync错误", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent));
        //
        if (!waitForEventProcessed(promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm2测试失败！原因:超时", false);
        } else {
            Assert.assertTrue("Update bm2时错误码不应该为：EC_NoError，Update失败", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
        }
    }

    @Test
    public void test_f1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm = DataInput.getPromotion();
        Promotion createPromotion = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue("retrieve1Sync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找到创建的对象", createPromotion != null);
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
        if (!promotionPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue("DeleteAsync bm1测试失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Promotion删除超时!", promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("promotion删除返回的错误码不正确！", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue("retrieve1Sync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("应该查找不到创建的对象", createPromotion == null);
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
        Assert.assertFalse("updateObjectAsync错误", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent));
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Promotion删除超时!", promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("promotion删除返回的错误码不正确！", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        //
    }

    @Test
    public void test_g1_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常case
        Promotion bm1 = DataInput.getPromotion();
        bm1 = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("createSync的错误码不正确", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync错误码不正确", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找创建的对象成功", p != null);
        //
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync错误码不正确", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找创建的对象成功", p == null);
    }

    @Test
    public void test_g2_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的对象，没有ID
        Promotion bm1 = DataInput.getPromotion();
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
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
        Assert.assertTrue("createNAsync超时!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync返回的错误码不正确", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue("retrieve1Sync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("查找不到创建的对象", promotion1 != null);
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
        Assert.assertTrue("createNAsync超时!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync返回的错误码不正确", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue("retrieve1Sync返回的错误码不正确！", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("查找不到创建的对象", promotion1 != null);
        }

        //异常case：重复插入
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, promotionSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createNAsync超时!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync返回的错误码不正确", promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
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
        Assert.assertTrue("createNAsync超时!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync返回的错误码不正确", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_i1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("正常查询");

        Promotion promotion = createPromotion();
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue("compareTo不正确", promotion.compareTo(promotionR) == 0);
    }

    @Test
    public void test_i2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("查询ID为0");

        Promotion promotion = createPromotion();
        promotion.setID(0l);
        promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_i3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("查询不存在的ID");

        Promotion promotion = DataInput.getPromotion();
        promotion.setID(999999l);
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync查询不存在的ID不应该有数据", promotionR == null);

    }

    @Test
    public void test_j1_retrieveNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("正常查询");

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
            Assert.assertTrue("Promotion同步超时!", false);
        }
        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue("ListMasterTable为空", PromotionList.size() > 0);
    }



    private Promotion createPromotion() throws Exception{
        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync失败，错误码不正确：" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync失败，compareTo错误", promotion.compareTo(promotionCreated) == 0);

        return promotionCreated;
    }

    /**
     * 查询本地Promotion表的总条数
     */
    @Test
    public void test_retrieveNPromotion() throws Exception {
        Shared.printTestMethodStartInfo();

        PromotionPresenter promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        Integer total = promotionPresenter.retrieveCount();
        System.out.println("Promotion表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
