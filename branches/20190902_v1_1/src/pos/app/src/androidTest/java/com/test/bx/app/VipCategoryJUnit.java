package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipCategoryHttpBO;
import com.bx.erp.bo.VipCategorySQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.VipCategorySQLiteEvent;
import com.bx.erp.event.VipCategoryHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.VipCategory;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.VipCategoryPresenter;
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

public class VipCategoryJUnit extends BaseHttpAndroidTestCase {
    private static VipCategoryPresenter vipCategoryPresenter = null;
    private static VipCategorySQLiteBO sqLiteBO = null;
    private static VipCategoryHttpBO httpBO = null;
    private static VipCategorySQLiteEvent sqLiteEvent = null;
    private static VipCategoryHttpEvent httpEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;
    private static List<VipCategory> List;

    private static final int Event_ID_VipCatgorgUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (vipCategoryPresenter == null) {
            vipCategoryPresenter = GlobalController.getInstance().getVipCategoryPresenter();
        }
        if (sqLiteEvent == null) {
            sqLiteEvent = new VipCategorySQLiteEvent();
            sqLiteEvent.setId(Event_ID_VipCatgorgUnit);
        }
        if (httpEvent == null) {
            httpEvent = new VipCategoryHttpEvent();
            httpEvent.setId(Event_ID_VipCatgorgUnit);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new VipCategorySQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new VipCategoryHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        sqLiteEvent.setHttpBO(httpBO);
        sqLiteEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_VipCatgorgUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_VipCatgorgUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_VipCatgorgUnit);
        }
        if (logoutHttpBO == null) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryHttpEvent(VipCategoryHttpEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        System.out.println("PosLoginHttpEvent---------------------- ASYNC???????????????event");
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
//        System.out.println("PosLoginHttpEvent---------------------- MAIN???????????????event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("????????????Event???ID=" + event.getId());
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onPosLoginHttpEvent3(PosLoginHttpEvent event) {
//        System.out.println("---------------------- POSTING???????????????event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("????????????Event???ID=" + event.getId());
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onPosLoginHttpEvent4(PosLoginHttpEvent event) {
//        System.out.println("---------------------- BACKGROUND???????????????event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("????????????Event???ID=" + event.getId());
//        }
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());

        }
    }

    public static class DataInput {
        private static VipCategory vipCategoryInput = null;

        protected static final VipCategory getVipCategoryInput() throws CloneNotSupportedException, ParseException {
            vipCategoryInput = new VipCategory();
            vipCategoryInput.setName("????????????" + System.currentTimeMillis() % 1000000);
            vipCategoryInput.setSyncType("C");
            vipCategoryInput.setReturnObject(1);
            vipCategoryInput.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            return (VipCategory) vipCategoryInput.clone();
        }

        protected static final java.util.List<BaseModel> getVipCategoryList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> categoryList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                categoryList.add(getVipCategoryInput());
            }
            return categoryList;
        }
    }

    @Test
    public void test_c1_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory?????????????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????VipCategory??????!", vc.compareTo(vipCategory) == 0);
    }

    @Test
    public void test_c2_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case:????????????
        sqLiteEvent.setEventProcessed(false);
        //???Category???????????????SQLite
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        VipCategory vc = createVipCategory(vipCategory);

        vipCategory.setID(vc.getID());
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory?????????????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_d_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory?????????????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????VipCategory??????!", vc.compareTo(vipCategory) == 0);


        //??????Case
        System.out.println("-------------------------------------Update1");
        //
        vc.setSyncType(BasePresenter.SYNC_Type_U);
        vc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_UpdateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, vc)) {
            Assert.assertTrue("??????????????????", false);
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vipCategoryUpdate = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????VipCategory??????!", vipCategoryUpdate.compareTo(vc) == 0);
        //
    }

    @Test
    public void test_d2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case:update ID??????????????????
        System.out.println("-------------------------------------Update3");
        sqLiteEvent.setEventProcessed(false);
        VipCategory vipCategory1 = DataInput.getVipCategoryInput();
        //??????SQLite????????????ID
        vipCategory1.setID(1000000l);
        //??????ID?????????SQLite???
        VipCategory vipCategory2 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????ID?????????SQLite???!", vipCategory2 == null);
        //???????????????Category update?????????SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_UpdateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1)) {
            Assert.assertTrue("??????????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        //????????????????????????????????????????????????EC_NoError
        Assert.assertTrue("???????????????", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????Case1: ????????????
        System.out.println("-------------------------------------RN2");
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RetrieveNAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory????????????!", false);
        }
        //
        Assert.assertTrue("retrieveN?????????????????????!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN ????????????????????????", sqLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category???List
        List<VipCategory> vipCategoryList = new ArrayList<VipCategory>();
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = DataInput.getVipCategoryInput();
            vipCategoryList.add(vipCategory);
        }
        //???VipCategory???List???????????????SQLite
        vipCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList);
        Long conflictID = vipCategoryList.get(1).getID();
        //
        Assert.assertTrue("createNSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList.get(i));
            Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("?????????vipCategoryList????????????????????????", vipCategory.compareTo(vipCategoryList.get(i)) == 0);
        }

    }

    @Test
    public void test_f2_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
        //??????Case:????????????
        VipCategory vc = DataInput.getVipCategoryInput();
        vc = createVipCategory(vc);

        List<VipCategory> vipCategoryList2 = new ArrayList<VipCategory>();
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = DataInput.getVipCategoryInput();
            vipCategory.setID(vc.getID());
            vipCategoryList2.add(vipCategory);
        }
        //???VipCategory???List???????????????SQLite
        vipCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList2);
        //
        Assert.assertTrue("createNSync????????????, ???????????????????????????OtherError", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Category
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //???Category???????????????SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????VipCategory??????!", vc.compareTo(vipCategory) == 0);
    }

    @Test
    public void test_g2_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = DataInput.getVipCategoryInput();
        VipCategory vc = createVipCategory(vipCategory);
        vipCategory.setID(vc.getID());

        //??????Case:????????????
        //????????????Category???????????????SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync??????????????????, ???????????????????????????OtherError!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h_UpdateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //???VipCategory???????????????SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????VipCategory??????!", vc.compareTo(vipCategory) == 0);

        //??????????????????
        VipCategory vc2 = DataInput.getVipCategoryInput();
        System.out.println("?????????ID??????" + vc.getID());
        vc2.setID(vc.getID());
        vc2.setName("update?????????Name");
        vc2.setSyncType(BasePresenter.SYNC_Type_U);
        vc2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        if (vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2)) {
            vc2.setSyncType(BasePresenter.SYNC_Type_U);

            Assert.assertTrue("UpdateSync vc2????????????,??????:????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            VipCategory vipCategory1Rerieve1 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2);
            //
            Assert.assertTrue("UpdateSync????????????,??????:??????????????????????????????!", vc2.compareTo(vipCategory1Rerieve1) == 0);
        } else {
            Assert.assertTrue("Updateync????????????!", false);
        }

        //??????Case: Update???????????????null
        vc2.setName(null);
        if (vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2)) {
            Assert.assertTrue("UpdateSync????????????!", false);
        }
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //???VipCategory???????????????SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????VipCategory??????!", vc.compareTo(vipCategory) == 0);

        //??????????????????
        vc.setName("????????????" + (int) (Math.random() * 10000));
        vc.setSyncType(BasePresenter.SYNC_Type_U);
        vc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //???????????????VipCategory Update?????????SQLite
        vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);
        //
        Assert.assertTrue("updateSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vipCategoryUpdate = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update??????!", vipCategoryUpdate.compareTo(vc) == 0);

        //??????Case:update ID??????????????????
        VipCategory vipCategory1 = DataInput.getVipCategoryInput();
        //??????SQLite????????????ID
        vipCategory1.setID(9999999l);
        //??????ID?????????SQLite???
        VipCategory vipCategory2 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????ID?????????SQLite???!", vipCategory2 == null);
        //????????????Update???????????????
        vc.setName("????????????" + (int) (Math.random() * 10000));
        //???????????????Category update?????????SQLite
        vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        //
        Assert.assertTrue("updateSync ID??????????????????!??????????????????NoError", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //????????????VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //???VipCategory???????????????SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1???????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Category??????!", vc.compareTo(vipCategory) == 0);

        //????????????Category
        BaseModel bm1 = DataInput.getVipCategoryInput();
        bm1.setID(vc.getID());
        vipCategoryPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getVipCategoryInput();
        bm2.setID(vc.getID());
        BaseModel bm3 = vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getVipCategoryList();
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateNAsync);
        if (!vipCategoryPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync????????????!", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory??????????????????!", false);
        }

        Assert.assertTrue("CreateNAsync????????????????????????", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<VipCategory> vipCategoryList = (List<VipCategory>) sqLiteEvent.getListMasterTable();
        for (int i = 0; i < list.size(); i++) {
            VipCategory vc = (VipCategory) list.get(i);
            Assert.assertTrue("CreateNAsync???????????????????????????", vc.compareTo(vipCategoryList.get(i)) == 0);
        }
    }

    //????????????
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //??????SQLite?????????????????????????????????????????????
        VipCategory vc = DataInput.getVipCategoryInput();
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");
        VipCategory vipCategory = new VipCategory();
        vipCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        vipCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsyncC_Done);
        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("???????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("?????????????????????VipCategory??????!", false);
        }
        Assert.assertTrue("???????????????", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????????????????sqlite??????????????????,
        List<VipCategory> vipCategoryList = (List<VipCategory>) vipCategoryPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<VipCategory> list = (List<VipCategory>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != vipCategoryList.size()) {
            Assert.assertTrue("??????????????????????????????????????????????????????SQLite??????????????????", false);
        }
    }

    private VipCategory createVipCategory(VipCategory vipCategory) {
        VipCategory vc = (VipCategory) vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("createObjectSync?????????????????????" + vipCategoryPresenter.getLastErrorCode(), vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        return vc;
    }
}
