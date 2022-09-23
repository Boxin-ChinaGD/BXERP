package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommodityCategorySQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.CommodityCategory;
import wpos.model.ErrorInfo;
import wpos.presenter.BasePresenter;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommodityCategoryJUnit extends BaseHttpTestCase {
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
    @BeforeClass
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        }
        if (commodityCategorySQLiteBO == null) {
            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
            commodityCategorySQLiteBO.setCommodityCategoryPresenter(commodityCategoryPresenter);
        }
        if (commodityCategoryHttpBO == null) {
            commodityCategoryHttpBO = new CommodityCategoryHttpBO(commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_CommodityCategoryJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    @AfterClass
    public void tearDown() {
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
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CommodityCategoryJUnit) {
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
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //4.模拟网页创建一个Category
        System.out.println("第一次调用create方法");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "创建失败!");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }

        logOut();
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
        //POS2,STAFF2登陆
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        commodityCategorySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }

        lTimeOut = 60;
        while (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category同步超时!");
        }
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(commodityCategoryList.size() > 0, "ListMasterTable为空");

        List = commodityCategoryList;
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        String categoryIDs = "";
        for (int i = 0; i < List.size(); i++) {
            categoryIDs = categoryIDs + "," + List.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "同步失败");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback超时!");
        }
        //调用RN，返回无数据
        System.out.println("第一次调用RN方法");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category同步超时!");
        }

        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(commodityCategoryList == null, "feedback失败");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
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
        CommodityCategory commodityCategoryCreate = null;
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue(false, "createAsync测试失败!原因:超时");
        } else {
            commodityCategoryCreate = (CommodityCategory) commodityCategorySQLiteEvent.getBaseModel1();
            Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync返回的错误码不正确!");
            CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
            Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
            c.setIgnoreIDInComparision(true);
            Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");
        }

        //异常Case:主键冲突
        commodityCategorySQLiteEvent.setEventProcessed(false);
        //将Category插入到本地SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
        commodityCategorySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue(false, "createAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createSync非空字段为空, 返回的错误码应该为OtherError!");
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
            Assert.assertTrue(false, "createAsync测试失败!原因:超时");
        }
        CommodityCategory commodityCategoryCreate = (CommodityCategory) commodityCategorySQLiteEvent.getBaseModel1();
        Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync返回的错误码不正确!");
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        c.setIgnoreIDInComparision(true);
        Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");


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
            Assert.assertTrue(false, "updateAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateSync返回的错误码不正确!");
            commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
            Assert.assertTrue(commodityCategoryUpdate.compareTo(c) == 0, "Update失败!");
        }

        //正常Case:update ID不存在的数据
        System.out.println("-------------------------------------Update3");
        commodityCategorySQLiteEvent.setEventProcessed(false);
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //设置SQLite不存在的ID
        commodityCategory1.setID(1000000);
        //验证ID不存在SQLite中
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertTrue(commodityCategory2 == null, "设置的ID存在于SQLite中!");
        //将修改后的Category update到本地SQLite
        commodityCategorySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
        commodityCategorySQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue(false, "updateAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateSync ID不存在的字段!错误码应该为NoError");
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
            Assert.assertTrue(false, "retrieveN失败!原因:超时");
        }
        Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveN的错误码不正确!");
        Assert.assertTrue(commodityCategorySQLiteEvent.getListMasterTable().size() != 0, "retrieveN 应该要有数据返回");
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
        List<CommodityCategory> commodityCategoryCreateList = (java.util.List<CommodityCategory>) commodityCategoryPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryList);
        Integer conflictID = commodityCategoryCreateList.get(0).getID();
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNSync返回的错误码不正确!");
        //
        for (int i = 0; i < 5; i++) {
            CommodityCategory commodityCategory = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreateList.get(i));
            Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
            commodityCategory.setIgnoreIDInComparision(true);
            Assert.assertTrue(commodityCategory.compareTo(commodityCategoryList.get(i)) == 0, "插入的categoryList没有完全插入成功");
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
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_PartSuccess, "createNSync主键冲突, 返回的错误码应该为OtherError");
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //正常Case
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        CommodityCategory commodityCategoryCreate = (CommodityCategory) commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        c.setIgnoreIDInComparision(true);
        Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");

        //异常Case:主键冲突
        //将已有的Category插入到本地SQLite
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createSync非空字段为空, 返回的错误码应该为OtherError!");
    }

    @Test
    public void test_h_UpdateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        CommodityCategory commodityCategoryCreate = (CommodityCategory) commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        c.setIgnoreIDInComparision(true);
        Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");

        //修改商品类型
        CommodityCategory bm1 = DataInput.getCommodityCategoryInput();
        System.out.println("输出：ID是：" + c.getID());
        bm1.setID(c.getID());
        bm1.setName("Name");
        bm1.setSyncType(BasePresenter.SYNC_Type_U);
        bm1.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        if (commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1)) {
            bm1.setSyncType(BasePresenter.SYNC_Type_U);

            Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1测试失败,原因:返回错误码不正确!");
            //
            CommodityCategory commodityCategory1Rerieve1 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
            //
            Assert.assertTrue(bm1.compareTo(commodityCategory1Rerieve1) == 0, "UpdateSync测试失败,原因:查询到的数据没有更新!");
        } else {
            Assert.assertTrue(false, "Updateync测试失败!");
        }

        //异常Case: Update非空字段为null
        bm1.setName(null);
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);

        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "updateObjectSync失败，错误码：" + commodityCategoryPresenter.getLastErrorCode());
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        CommodityCategory commodityCategoryCreate = (CommodityCategory) commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        c.setIgnoreIDInComparision(true);
        Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");

        //修改属性的值
        c.setName("修改测试" + (int) (Math.random() * 10000));
        c.setSyncType(BasePresenter.SYNC_Type_U);
        c.setSyncDatetime(Constants.getDefaultSyncDatetime2());
        //将修改后的Category Update到本地SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateSync返回的错误码不正确!");
        //
        CommodityCategory commodityCategoryUpdate = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertTrue(commodityCategoryUpdate.compareTo(c) == 0, "Update失败!");

        //异常Case:update ID不存在的数据
        CommodityCategory commodityCategory1 = DataInput.getCommodityCategoryInput();
        //设置SQLite不存在的ID
        commodityCategory1.setID(888888);
        //验证ID不存在SQLite中
        CommodityCategory commodityCategory2 = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        Assert.assertTrue(commodityCategory2 == null, "设置的ID存在于SQLite中!");
        //设置需要Update的字段的值
        c.setName("修改测试" + (int) (Math.random() * 10000));
        //将修改后的Category update到本地SQLite
        commodityCategoryPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory1);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "updateSync ID不存在的字段!错误码应该为NoError");
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //创建一个Category
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        //将Category插入到本地SQLite
        CommodityCategory commodityCategoryCreate = (CommodityCategory) commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategory);
        //
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync返回的错误码不正确!");
        //
        CommodityCategory c = (CommodityCategory) commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodityCategoryCreate);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");
        c.setIgnoreIDInComparision(true);
        Assert.assertTrue(c.compareTo(commodityCategory) == 0, "插入Category失败!");

        //删除一个Category
        BaseModel bm1 = DataInput.getCommodityCategoryInput();
        bm1.setID(c.getID());
        commodityCategoryPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        BaseModel bm2 = DataInput.getCommodityCategoryInput();
        bm2.setID(c.getID());
        BaseModel bm3 = commodityCategoryPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "该对象未被删除");
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getCategoryList();
        if (!commodityCategoryPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, commodityCategorySQLiteEvent)) {
            Assert.assertTrue(false, "CreateNAsync测试失败!");
        }

        if (!waitForEventProcessed(commodityCategorySQLiteEvent)) {
            Assert.assertTrue(false, "CreateNAsync测试失败!原因:超时");
        } else {
            Assert.assertTrue(commodityCategorySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNAsync返回错误码不正确");

            List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                CommodityCategory c = (CommodityCategory) list.get(i);
                c.setIgnoreIDInComparision(true);
                Assert.assertTrue(c.compareTo(commodityCategoryList.get(i)) == 0, "CreateNAsync的数据与原数据不符");
            }
        }
    }

    //全部下载
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //本地SQLite增加一条数据。全部同步后不存在
        CommodityCategory c = DataInput.getCommodityCategoryInput();
        commodityCategoryPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_Default);

        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "同步失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "请求服务器同步Category超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategoryPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(commodityCategoryPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");

        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategoryHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != commodityCategoryList.size()) {
            Assert.assertTrue(false, "全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }
}
