package com.test.bx.app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityCategoryHttpBO;
import com.bx.erp.bo.CommodityCategorySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.CommodityCategoryPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommodityCategoryJUnit extends BaseHttpAndroidTestCase {
    private static CommodityCategoryPresenter commodityCategoryPresenter = null;
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    private static java.util.List<CommodityCategory> List;
    private static final int EVENT_ID_CommodityCategoryJUnit = 10000;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (commodityCategoryPresenter == null) {
            commodityCategoryPresenter = GlobalController.getInstance().getCommodityCategoryPresenter();
        }

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if(commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if(commodityCategorySQLiteBO == null) {
            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        if(commodityCategoryHttpBO == null) {
            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
        if(logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if(logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
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
    public void onCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        }else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static CommodityCategory commodityCategoryInput = null;

        protected static final CommodityCategory getCommodityCategoryInput() throws CloneNotSupportedException, ParseException {
            commodityCategoryInput = new CommodityCategory();
            commodityCategoryInput.setName("c" + System.currentTimeMillis() % 1000000);
            commodityCategoryInput.setParentID(1);
            commodityCategoryInput.setSyncType("C");
            commodityCategoryInput.setReturnObject(1);
            commodityCategoryInput.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            return (CommodityCategory) commodityCategoryInput.clone();
        }

        protected static final List<BaseModel> getCategoryList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> categoryList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                categoryList.add(getCommodityCategoryInput());
            }
            return categoryList;
        }
    }

    @Test
    public void test_a_RetrieveNEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //4.????????????????????????Category
        System.out.println("???????????????create??????");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue("????????????!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Category??????!", false);
        }

        logOut();

        //POS2,STAFF2??????
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        commodityCategorySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }

        lTimeOut = 60;
        while (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Category????????????!", false);
        }
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("ListMasterTable??????", commodityCategoryList.size() > 0);

        List = commodityCategoryList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        String categoryIDs = "";
        for (int i = 0; i < List.size(); i++) {
            categoryIDs = categoryIDs + "," + List.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //??????Feedback,
        System.out.println("???????????????feedback??????");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue("????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("CommodityCategory Feedback??????!", false);
        }
        //??????RN??????????????????
        System.out.println("???????????????RN??????");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Category????????????!", false);
        }

        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("feedback??????", commodityCategoryList == null);
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("createAsync???????????????????????????!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
            Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);
        }

        //??????Case:????????????
        commodityCategorySQLiteEvent.setEventProcessed(false);
        //???Category???????????????SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("createSync??????????????????, ???????????????????????????OtherError!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_d_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync????????????!??????:??????", false);
        }
            Assert.assertTrue("createAsync???????????????????????????!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
            Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);


        //??????Case
        System.out.println("-------------------------------------Update1");
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
        commodityCategorySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, c);

        c.setSyncType(BasePresenter.SYNC_Type_U);
        c.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //
        CommodityCategory commodityCategoryUpdate = new CommodityCategory();
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("updateAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("updateSync???????????????????????????!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("Update??????!", commodityCategoryUpdate.compareTo(c) == 0);
        }

        //??????Case:update ID??????????????????
        System.out.println("-------------------------------------Update3");
        commodityCategorySQLiteEvent.setEventProcessed(false);
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //??????SQLite????????????ID
        commodityCategory1.setID(1000000l);
        //??????ID?????????SQLite???
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????ID?????????SQLite???!", commodityCategory2 == null);
        //???????????????Category update?????????SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
        commodityCategorySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("updateAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("updateSync ID??????????????????!??????????????????NoError", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case1: ????????????
        System.out.println("-------------------------------------RN2");
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RetrieveNAsync);
        commodityCategorySQLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("retrieveN??????!??????:??????", false);
        }
        Assert.assertTrue("retrieveN?????????????????????!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN ????????????????????????", commodityCategorySQLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category???List
        List<CommodityCategory> commodityCategoryList = new ArrayList<CommodityCategory>();
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
            commodityCategoryList.add(commodityCategory);
        }
        //???Category???List???????????????SQLite
        commodityCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList);
        Long conflictID = commodityCategoryList.get(1).getID();
        //
        Assert.assertTrue("createNSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList.get(i));
            Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("?????????categoryList????????????????????????", commodityCategory.compareTo(commodityCategoryList.get(i)) == 0);
        }

        //??????Case:????????????
        List<CommodityCategory> commodityCategoryList2 = new ArrayList<CommodityCategory>();
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
            commodityCategory.setID(conflictID);
            commodityCategoryList2.add(commodityCategory);
        }
        //???Category???List???????????????SQLite
        commodityCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList2);
        //
        Assert.assertTrue("createNSync????????????, ???????????????????????????OtherError", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //???Category???????????????SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);

        //??????Case:????????????
        //????????????Category???????????????SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync??????????????????, ???????????????????????????OtherError!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h_UpdateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //???Category???????????????SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);

        //??????????????????
        CommodityCategory bm1 = DataInput.getCommodityCategoryInput();
        System.out.println("?????????ID??????" + c.getID());
        bm1.setID(c.getID());
        bm1.setName("Name");
        bm1.setSyncType(BasePresenter.SYNC_Type_U);
        bm1.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        if (commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            bm1.setSyncType(BasePresenter.SYNC_Type_U);

            Assert.assertTrue("UpdateSync bm1????????????,??????:????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            CommodityCategory commodityCategory1Rerieve1 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
            //
            Assert.assertTrue("UpdateSync????????????,??????:??????????????????????????????!", bm1.compareTo(commodityCategory1Rerieve1) == 0);
        } else {
            Assert.assertTrue("Updateync????????????!", false);
        }

        //??????Case: Update???????????????null
        bm1.setName(null);
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);

        Assert.assertTrue("updateObjectSync?????????????????????" + commodityCategoryPresenter.getLastErrorCode(), commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //???Category???????????????SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);

        //??????????????????
        c.setName("????????????" + (int) (Math.random() * 10000));
        c.setSyncType(BasePresenter.SYNC_Type_U);
        c.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //???????????????Category Update?????????SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        //
        Assert.assertTrue("updateSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update??????!", commodityCategoryUpdate.compareTo(c) == 0);

        //??????Case:update ID??????????????????
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //??????SQLite????????????ID
        commodityCategory1.setID(888888l);
        //??????ID?????????SQLite???
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????ID?????????SQLite???!", commodityCategory2 == null);
        //????????????Update???????????????
        c.setName("????????????" + (int) (Math.random() * 10000));
        //???????????????Category update?????????SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        Assert.assertTrue("updateSync ID??????????????????!??????????????????NoError", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //????????????Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //???Category???????????????SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Category??????!", c.compareTo(commodityCategory) == 0);

        //????????????Category
        BaseModel bm1 = DataInput.getCommodityCategoryInput();
        bm1.setID(c.getID());
        commodityCategoryPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getCommodityCategoryInput();
        bm2.setID(c.getID());
        BaseModel bm3 = commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getCategoryList();
        if (!commodityCategoryPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, commodityCategorySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync????????????!", false);
        }

        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("CreateNAsync????????????????????????", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                CommodityCategory c = (CommodityCategory) list.get(i);
                Assert.assertTrue("CreateNAsync???????????????????????????", c.compareTo(commodityCategoryList.get(i)) == 0);
            }
        }
    }

    //????????????
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //??????SQLite?????????????????????????????????????????????
        CommodityCategory c = DataInput.getCommodityCategoryInput();
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_Default);

        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue("???????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("?????????????????????Category??????!", false);
        }

        //???????????????????????????sqlite??????????????????,
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategoryHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != commodityCategoryList.size()) {
            Assert.assertTrue("??????????????????????????????????????????????????????SQLite??????????????????", false);
        }
    }
}
