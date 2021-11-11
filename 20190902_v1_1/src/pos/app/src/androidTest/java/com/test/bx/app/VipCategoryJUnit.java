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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        System.out.println("PosLoginHttpEvent---------------------- ASYNC方式接收到event");
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
//        System.out.println("PosLoginHttpEvent---------------------- MAIN方式接收到event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onPosLoginHttpEvent3(PosLoginHttpEvent event) {
//        System.out.println("---------------------- POSTING方式接收到event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onPosLoginHttpEvent4(PosLoginHttpEvent event) {
//        System.out.println("---------------------- BACKGROUND方式接收到event");
//        if (event.getId() == Event_ID_VipCatgorgUnit){
//            event.onEvent();
//        } else {
//            System.out.println("未处理的Event，ID=" + event.getId());
//        }
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_VipCatgorgUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());

        }
    }

    public static class DataInput {
        private static VipCategory vipCategoryInput = null;

        protected static final VipCategory getVipCategoryInput() throws CloneNotSupportedException, ParseException {
            vipCategoryInput = new VipCategory();
            vipCategoryInput.setName("白金会员" + System.currentTimeMillis() % 1000000);
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

        //正常Case
        //创建一个VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory调用创建失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("VipCategory创建超时!", false);
        }
        Assert.assertTrue("创建后返回的错误码不正确", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("插入VipCategory失败!", vc.compareTo(vipCategory) == 0);
    }

    @Test
    public void test_c2_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常Case:主键冲突
        sqLiteEvent.setEventProcessed(false);
        //将Category插入到本地SQLite
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        VipCategory vc = createVipCategory(vipCategory);

        vipCategory.setID(vc.getID());
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory调用创建失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("VipCategory创建超时!", false);
        }
        Assert.assertTrue("创建后返回的错误码不正确", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_d_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("vipcategory调用创建失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory创建超时!", false);
        }
        Assert.assertTrue("创建后返回的错误码不正确", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("插入VipCategory失败!", vc.compareTo(vipCategory) == 0);


        //正常Case
        System.out.println("-------------------------------------Update1");
        //
        vc.setSyncType(BasePresenter.SYNC_Type_U);
        vc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_UpdateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, vc)) {
            Assert.assertTrue("调用修改失败", false);
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory修改超时!", false);
        }
        Assert.assertTrue("创建后返回的错误码不正确", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vipCategoryUpdate = (VipCategory) sqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("修改VipCategory失败!", vipCategoryUpdate.compareTo(vc) == 0);
        //
    }

    @Test
    public void test_d2_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case:update ID不存在的数据
        System.out.println("-------------------------------------Update3");
        sqLiteEvent.setEventProcessed(false);
        VipCategory vipCategory1 = DataInput.getVipCategoryInput();
        //设置SQLite不存在的ID
        vipCategory1.setID(1000000l);
        //验证ID不存在SQLite中
        VipCategory vipCategory2 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", vipCategory2 == null);
        //将修改后的Category update到本地SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_UpdateAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1)) {
            Assert.assertTrue("调用修改失败", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory修改超时!", false);
        }
        //修改并不存在的，返回的错误码也是EC_NoError
        Assert.assertTrue("修改失败！", sqLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 查询所有
        System.out.println("-------------------------------------RN2");
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RetrieveNAsync);
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!sqLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("查询失败", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory修改超时!", false);
        }
        //
        Assert.assertTrue("retrieveN的错误码不正确!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 应该要有数据返回", sqLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category的List
        List<VipCategory> vipCategoryList = new ArrayList<VipCategory>();
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = DataInput.getVipCategoryInput();
            vipCategoryList.add(vipCategory);
        }
        //将VipCategory的List插入到本地SQLite
        vipCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList);
        Long conflictID = vipCategoryList.get(1).getID();
        //
        Assert.assertTrue("createNSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的vipCategoryList没有完全插入成功", vipCategory.compareTo(vipCategoryList.get(i)) == 0);
        }

    }

    @Test
    public void test_f2_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
        //异常Case:主键冲突
        VipCategory vc = DataInput.getVipCategoryInput();
        vc = createVipCategory(vc);

        List<VipCategory> vipCategoryList2 = new ArrayList<VipCategory>();
        for (int i = 0; i < 5; i++) {
            VipCategory vipCategory = DataInput.getVipCategoryInput();
            vipCategory.setID(vc.getID());
            vipCategoryList2.add(vipCategory);
        }
        //将VipCategory的List插入到本地SQLite
        vipCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategoryList2);
        //
        Assert.assertTrue("createNSync主键冲突, 返回的错误码应该为OtherError", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //将Category插入到本地SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入VipCategory失败!", vc.compareTo(vipCategory) == 0);
    }

    @Test
    public void test_g2_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = DataInput.getVipCategoryInput();
        VipCategory vc = createVipCategory(vipCategory);
        vipCategory.setID(vc.getID());

        //异常Case:主键冲突
        //将已有的Category插入到本地SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h_UpdateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //将VipCategory插入到本地SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入VipCategory失败!", vc.compareTo(vipCategory) == 0);

        //修改商品类型
        VipCategory vc2 = DataInput.getVipCategoryInput();
        System.out.println("输出：ID是：" + vc.getID());
        vc2.setID(vc.getID());
        vc2.setName("update之后的Name");
        vc2.setSyncType(BasePresenter.SYNC_Type_U);
        vc2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        if (vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2)) {
            vc2.setSyncType(BasePresenter.SYNC_Type_U);

            Assert.assertTrue("UpdateSync vc2测试失败,原因:返回错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            VipCategory vipCategory1Rerieve1 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2);
            //
            Assert.assertTrue("UpdateSync测试失败,原因:查询到的数据没有更新!", vc2.compareTo(vipCategory1Rerieve1) == 0);
        } else {
            Assert.assertTrue("Updateync测试失败!", false);
        }

        //异常Case: Update非空字段为null
        vc2.setName(null);
        if (vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc2)) {
            Assert.assertTrue("UpdateSync测试失败!", false);
        }
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //将VipCategory插入到本地SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入VipCategory失败!", vc.compareTo(vipCategory) == 0);

        //修改属性的值
        vc.setName("修改测试" + (int) (Math.random() * 10000));
        vc.setSyncType(BasePresenter.SYNC_Type_U);
        vc.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //将修改后的VipCategory Update到本地SQLite
        vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);
        //
        Assert.assertTrue("updateSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vipCategoryUpdate = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update失败!", vipCategoryUpdate.compareTo(vc) == 0);

        //异常Case:update ID不存在的数据
        VipCategory vipCategory1 = DataInput.getVipCategoryInput();
        //设置SQLite不存在的ID
        vipCategory1.setID(9999999l);
        //验证ID不存在SQLite中
        VipCategory vipCategory2 = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", vipCategory2 == null);
        //设置需要Update的字段的值
        vc.setName("修改测试" + (int) (Math.random() * 10000));
        //将修改后的Category update到本地SQLite
        vipCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory1);
        //
        Assert.assertTrue("updateSync ID不存在的字段!错误码应该为NoError", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //创建一个VipCategory
        VipCategory vipCategory = DataInput.getVipCategoryInput();
        //将VipCategory插入到本地SQLite
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        VipCategory vc = (VipCategory) vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Category失败!", vc.compareTo(vipCategory) == 0);

        //删除一个Category
        BaseModel bm1 = DataInput.getVipCategoryInput();
        bm1.setID(vc.getID());
        vipCategoryPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getVipCategoryInput();
        bm2.setID(vc.getID());
        BaseModel bm3 = vipCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("该对象未被删除", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getVipCategoryList();
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);

        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateNAsync);
        if (!vipCategoryPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败!", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("VipCategory创建多个超时!", false);
        }

        Assert.assertTrue("CreateNAsync返回错误码不正确", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<VipCategory> vipCategoryList = (List<VipCategory>) sqLiteEvent.getListMasterTable();
        for (int i = 0; i < list.size(); i++) {
            VipCategory vc = (VipCategory) list.get(i);
            Assert.assertTrue("CreateNAsync的数据与原数据不符", vc.compareTo(vipCategoryList.get(i)) == 0);
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //本地SQLite增加一条数据。全部同步后不存在
        VipCategory vc = DataInput.getVipCategoryInput();
        vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vc);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        VipCategory vipCategory = new VipCategory();
        vipCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        vipCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsyncC_Done);
        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("同步失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步VipCategory超时!", false);
        }
        Assert.assertTrue("同步失败！", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //服务器返回的数据和sqlite里的数据对比,
        List<VipCategory> vipCategoryList = (List<VipCategory>) vipCategoryPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<VipCategory> list = (List<VipCategory>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != vipCategoryList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }

    private VipCategory createVipCategory(VipCategory vipCategory) {
        VipCategory vc = (VipCategory) vipCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, vipCategory);
        Assert.assertTrue("createObjectSync失败，错误码：" + vipCategoryPresenter.getLastErrorCode(), vipCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        return vc;
    }
}
