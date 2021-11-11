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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        }else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if(event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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

        //1,2步，登录POS和Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //4.模拟网页创建一个Category
        System.out.println("第一次调用create方法");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue("创建失败!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Category超时!", false);
        }

        logOut();

        //POS2,STAFF2登陆
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        commodityCategorySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("同步失败", false);
        }

        lTimeOut = 60;
        while (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Category同步超时!", false);
        }
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("ListMasterTable为空", commodityCategoryList.size() > 0);

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

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue("同步失败", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("CommodityCategory Feedback超时!", false);
        }
        //调用RN，返回无数据
        System.out.println("第一次调用RN方法");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("同步失败", false);
        }
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Category同步超时!", false);
        }

        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("feedback失败", commodityCategoryList == null);
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createAsync返回的错误码不正确!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
            Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);
        }

        //异常Case:主键冲突
        commodityCategorySQLiteEvent.setEventProcessed(false);
        //将Category插入到本地SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_d_UpdateAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("createAsync测试失败!原因:超时", false);
        }
            Assert.assertTrue("createAsync返回的错误码不正确!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
            Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);


        //正常Case
        System.out.println("-------------------------------------Update1");
        //
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
        commodityCategorySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, c);

        c.setSyncType(BasePresenter.SYNC_Type_U);
        c.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //
        CommodityCategory commodityCategoryUpdate = new CommodityCategory();
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("updateAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("updateSync返回的错误码不正确!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("Update失败!", commodityCategoryUpdate.compareTo(c) == 0);
        }

        //正常Case:update ID不存在的数据
        System.out.println("-------------------------------------Update3");
        commodityCategorySQLiteEvent.setEventProcessed(false);
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //设置SQLite不存在的ID
        commodityCategory1.setID(1000000l);
        //验证ID不存在SQLite中
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", commodityCategory2 == null);
        //将修改后的Category update到本地SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
        commodityCategorySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("updateAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("updateSync ID不存在的字段!错误码应该为NoError", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常Case1: 查询所有
        System.out.println("-------------------------------------RN2");
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RetrieveNAsync);
        commodityCategorySQLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("retrieveN失败!原因:超时", false);
        }
        Assert.assertTrue("retrieveN的错误码不正确!", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN 应该要有数据返回", commodityCategorySQLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category的List
        List<CommodityCategory> commodityCategoryList = new ArrayList<CommodityCategory>();
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
            commodityCategoryList.add(commodityCategory);
        }
        //将Category的List插入到本地SQLite
        commodityCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList);
        Long conflictID = commodityCategoryList.get(1).getID();
        //
        Assert.assertTrue("createNSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList.get(i));
            Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("插入的categoryList没有完全插入成功", commodityCategory.compareTo(commodityCategoryList.get(i)) == 0);
        }

        //异常Case:主键冲突
        List<CommodityCategory> commodityCategoryList2 = new ArrayList<CommodityCategory>();
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
            commodityCategory.setID(conflictID);
            commodityCategoryList2.add(commodityCategory);
        }
        //将Category的List插入到本地SQLite
        commodityCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList2);
        //
        Assert.assertTrue("createNSync主键冲突, 返回的错误码应该为OtherError", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);

        //异常Case:主键冲突
        //将已有的Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync非空字段为空, 返回的错误码应该为OtherError!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_h_UpdateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);

        //修改商品类型
        CommodityCategory bm1 = DataInput.getCommodityCategoryInput();
        System.out.println("输出：ID是：" + c.getID());
        bm1.setID(c.getID());
        bm1.setName("Name");
        bm1.setSyncType(BasePresenter.SYNC_Type_U);
        bm1.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        if (commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            bm1.setSyncType(BasePresenter.SYNC_Type_U);

            Assert.assertTrue("UpdateSync bm1测试失败,原因:返回错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            CommodityCategory commodityCategory1Rerieve1 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
            //
            Assert.assertTrue("UpdateSync测试失败,原因:查询到的数据没有更新!", bm1.compareTo(commodityCategory1Rerieve1) == 0);
        } else {
            Assert.assertTrue("Updateync测试失败!", false);
        }

        //异常Case: Update非空字段为null
        bm1.setName(null);
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);

        Assert.assertTrue("updateObjectSync失败，错误码：" + commodityCategoryPresenter.getLastErrorCode(), commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);

        //修改属性的值
        c.setName("修改测试" + (int) (Math.random() * 10000));
        c.setSyncType(BasePresenter.SYNC_Type_U);
        c.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //将修改后的Category Update到本地SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        //
        Assert.assertTrue("updateSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Update失败!", commodityCategoryUpdate.compareTo(c) == 0);

        //异常Case:update ID不存在的数据
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //设置SQLite不存在的ID
        commodityCategory1.setID(888888l);
        //验证ID不存在SQLite中
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("设置的ID存在于SQLite中!", commodityCategory2 == null);
        //设置需要Update的字段的值
        c.setName("修改测试" + (int) (Math.random() * 10000));
        //将修改后的Category update到本地SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        Assert.assertTrue("updateSync ID不存在的字段!错误码应该为NoError", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        Assert.assertTrue("retrieve1返回的错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Category失败!", c.compareTo(commodityCategory) == 0);

        //删除一个Category
        BaseModel bm1 = DataInput.getCommodityCategoryInput();
        bm1.setID(c.getID());
        commodityCategoryPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = DataInput.getCommodityCategoryInput();
        bm2.setID(c.getID());
        BaseModel bm3 = commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("该对象未被删除", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getCategoryList();
        if (!commodityCategoryPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, commodityCategorySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败!", false);
        }

        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue("CreateNAsync测试失败!原因:超时", false);
        } else {
            Assert.assertTrue("CreateNAsync返回错误码不正确", commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                CommodityCategory c = (CommodityCategory) list.get(i);
                Assert.assertTrue("CreateNAsync的数据与原数据不符", c.compareTo(commodityCategoryList.get(i)) == 0);
            }
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //本地SQLite增加一条数据。全部同步后不存在
        CommodityCategory c = DataInput.getCommodityCategoryInput();
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_Default);

        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue("同步失败！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步Category超时!", false);
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategoryHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != commodityCategoryList.size()) {
            Assert.assertTrue("全部同步失败。服务器返回的数据和本地SQLite的数据不一致", false);
        }
    }
}
