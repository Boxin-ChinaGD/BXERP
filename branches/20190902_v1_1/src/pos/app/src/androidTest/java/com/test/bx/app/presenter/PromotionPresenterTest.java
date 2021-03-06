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
            promotion.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // ??????????????????
            promotion.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // ??????????????????
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1?????????????????????Promotion");

        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(size);
        List<Promotion> list = (List<Promotion>) promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync????????????????????????????????? size()=" + list.size(), list.size() == size);
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2????????????????????????");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setName("1234567890 1234567890 1234567890 1234567890");
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3??????????????????null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);
        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1???????????????");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync?????????compareTo??????", promotion.compareTo(promotionCreated) == 0);
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2???????????????");

        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync?????????compareTo??????", promotion.compareTo(promotionCreated) == 0);

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3????????????????????????");

        Promotion promotion = DataInput.getPromotion();
        promotion.setName(null);

        promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_b4_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case4??????????????????null");

//        int size = new Random().nextInt(10) + 1;
        List<Promotion> promotionList = DataInput.getPromotionList(3);
        promotionList.get(1).setSn(null);
        promotionPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotionList);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);

    }

    @Test
    public void test_c1_updateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Promotion bm1 = createPromotion();
        bm1.setName("aaa");
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("updateSync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????????????????", bm1.getName().equals(p.getName()));
    }

    @Test
    public void test_c2_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????Case: Update???????????????null
        Promotion p = DataInput.getPromotion();
        p.setName(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateSync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        //
    }

    @Test
    public void test_c3_updateSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case???update?????????ID???null
        Promotion p = DataInput.getPromotion();
        p.setID(null);
        promotionPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, p);
        Assert.assertTrue("updateSync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }


    @Test
    public void test_d1_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case: ??????case
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm1 = DataInput.getPromotion();
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, promotionSQLiteEvent)) {
            Assert.assertTrue("CreateAsync bm1???????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue("createAsync Promotion?????????" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("?????????????????????????????????Null???", bm1.compareTo(p) == 0);
        Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d2_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case?????????????????????????????????????????????????????????
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm2 = DataInput.getPromotion();
        bm2.setName(null);

        Assert.assertFalse("createObjectAsync??????", promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, promotionSQLiteEvent));
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createAsync Promotion?????????" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("Create bm1???????????????????????????EC_WrongFormatForInputField???????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d3_createAync() throws Exception {
        Shared.printTestMethodStartInfo();

        Promotion p = createPromotion();
        //case???????????????
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion bm3 = DataInput.getPromotion();
        bm3.setID(p.getID());
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent)) {
            Assert.assertTrue("CreateAsync bm3???????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createAsync Promotion?????????" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("Create bm1???????????????????????????EC_NoError???????????????", promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //????????????????????????????????????????????????
    }

    @Test
    public void test_e1_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //???????????????
        BaseModel bm = DataInput.getPromotion();
        Promotion createPromotion = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        createPromotion.setName("asd");
        if (!promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm1???????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (!promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm1???????????????", false);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue("createAsync Promotion?????????" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("?????????????????????????????????Null???", createPromotion.compareTo(p) == 0);
        Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e2_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //????????????????????????
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion createPromotion = createPromotion();
        createPromotion.setName(null);

        Assert.assertFalse("updateObjectAsync??????", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent));
        //
        if (!waitForEventProcessed(promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm3?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Update bm3???????????????????????????EC_NoError???Update??????", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
        }
    }

    @Test
    public void test_e3_updateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //?????????ID???????????????
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
        Promotion bm3 = DataInput.getPromotion();
        bm3.setName("lalalalalala");
        //
        Assert.assertFalse("updateObjectAsync??????", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm3, promotionSQLiteEvent));
        //
        if (!waitForEventProcessed(promotionSQLiteEvent)) {
            Assert.assertTrue("UpdateAsync bm2?????????????????????:??????", false);
        } else {
            Assert.assertTrue("Update bm2???????????????????????????EC_NoError???Update??????", !ErrorInfo.EnumErrorCode.EC_NoError.equals(promotionSQLiteEvent.getLastErrorCode()));
        }
    }

    @Test
    public void test_f1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Promotion bm = DataInput.getPromotion();
        Promotion createPromotion = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm);
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue("retrieve1Sync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????????????????", createPromotion != null);
        //
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
        if (!promotionPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent)) {
            Assert.assertTrue("DeleteAsync bm1???????????????", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Promotion????????????!", promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("promotion????????????????????????????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        createPromotion = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion);
        Assert.assertTrue("retrieve1Sync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????????????????", createPromotion == null);
    }

    @Test
    public void test_f2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???id???null
        Promotion createPromotion = createPromotion();
        //
        createPromotion.setID(null);
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_DeleteAsync);
        Assert.assertFalse("updateObjectAsync??????", promotionPresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, createPromotion, promotionSQLiteEvent));
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("Promotion????????????!", promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("promotion????????????????????????????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        //
    }

    @Test
    public void test_g1_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????case
        Promotion bm1 = DataInput.getPromotion();
        bm1 = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("createSync?????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Promotion p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync??????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????????????????", p != null);
        //
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        p = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1Sync??????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????????????????", p == null);
    }

    @Test
    public void test_g2_deleteSync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //??????case????????????????????????????????????ID
        Promotion bm1 = DataInput.getPromotion();
        promotionPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_h1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Promotion> bmList = (List<Promotion>) DataInput.getPromotionList(5);
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, promotionSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createNAsync??????!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync???????????????????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue("retrieve1Sync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("???????????????????????????", promotion1 != null);
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
        Assert.assertTrue("createNAsync??????!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync???????????????????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Promotion> promotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        for (Promotion promotion : promotionList) {
            Promotion promotion1 = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
            Assert.assertTrue("retrieve1Sync??????????????????????????????", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("???????????????????????????", promotion1 != null);
        }

        //??????case???????????????
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateNAsync);
        promotionPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, promotionSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("createNAsync??????!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync???????????????????????????", promotionSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case??????????????????null
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
        Assert.assertTrue("createNAsync??????!" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("createNAsync???????????????????????????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_i1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("????????????");

        Promotion promotion = createPromotion();
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        promotion.setIgnoreIDInComparision(true);
        Assert.assertTrue("compareTo?????????", promotion.compareTo(promotionR) == 0);
    }

    @Test
    public void test_i2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("??????ID???0");

        Promotion promotion = createPromotion();
        promotion.setID(0l);
        promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);

    }

    @Test
    public void test_i3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("??????????????????ID");

        Promotion promotion = DataInput.getPromotion();
        promotion.setID(999999l);
        Promotion promotionR = (Promotion) promotionPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);
        Assert.assertTrue("retrieve1ObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync??????????????????ID??????????????????", promotionR == null);

    }

    @Test
    public void test_j1_retrieveNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("????????????");

        //????????????Promotion
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
            Assert.assertTrue("Promotion????????????!", false);
        }
        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue("ListMasterTable??????", PromotionList.size() > 0);
    }



    private Promotion createPromotion() throws Exception{
        Promotion promotion = DataInput.getPromotion();
        Promotion promotionCreated = (Promotion) promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, promotion);

        Assert.assertTrue("createNObjectSync??????????????????????????????" + promotionPresenter.getLastErrorCode(), promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("createNObjectSync?????????compareTo??????", promotion.compareTo(promotionCreated) == 0);

        return promotionCreated;
    }

    /**
     * ????????????Promotion???????????????
     */
    @Test
    public void test_retrieveNPromotion() throws Exception {
        Shared.printTestMethodStartInfo();

        PromotionPresenter promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        Integer total = promotionPresenter.retrieveCount();
        System.out.println("Promotion???????????????" + total);
        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }

}
