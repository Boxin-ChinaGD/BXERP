package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.CommodityCategorySQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.CommodityCategory;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CategorySIT extends BaseHttpTestCase {
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static java.util.List<CommodityCategory> List;

    private static BaseModel bmCreateSync = null;
    private static BaseModel bmCreateAsync = null;
    private static BaseModel bmUpdateAsync = null;
    private static java.util.List<BaseModel> bmCreateNAsync = null;

    private static long createAsyncID = 0l;
    private static final int EVENT_ID1_CreateAsync = 1;
    private static final int EVENT_ID2_CreateAsync = 2;
    private static final int EVENT_ID3_CreateAsync = 3;
    private static final int EVENT_ID4_CreateAsync = 4;

    private static final int EVENT_ID1_UpdateAsync = 5;
    private static final int EVENT_ID2_UpdateAsync = 6;
    private static final int EVENT_ID3_UpdateAsync = 7;

    private static final int EVENT_ID1_DeleteAsync = 13;

    private static final int EVENT_ID_CreateNAsync = 11;

    private static final long Timeout = 30 * 1000;

    private static final int EVENT_ID_CategorySIT = 10000;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_CategorySIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_CategorySIT);
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

        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(EVENT_ID_CategorySIT);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(EVENT_ID_CategorySIT);
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
        logoutHttpEvent.setId(EVENT_ID_CategorySIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    @AfterClass
    public void tearDown()  {
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
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
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
            commodityCategoryInput.setName("Game" + System.currentTimeMillis() % 1000000);
            commodityCategoryInput.setParentID(1);
            commodityCategoryInput.setSyncType("C");
            commodityCategoryInput.setInt1(1);
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

     /*
    1.pos登录
    2.staff登录
    3.暂停同步线程
    4.模拟网页创建一个Category
    5.调用RN(有同步数据)
    6.调用feedbackEx。
    7.调用RN（无同步数据）
    8.模拟网页创建一个Category
    9.恢复线程, 线程是否顺利执行
    10.退出登录
     */

    @Test
    public void testCategory() throws InterruptedException, CloneNotSupportedException, ParseException {
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
            Assert.assertTrue(false, "请求服务器创建Brand超时!");
        }

        //RN前登陆另一台POS，使其有同步数据
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败");
        }

        lTimeOut = 50;
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category同步超时!");
        }
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(commodityCategoryList.size() > 0, "ListMasterTable为空");
        System.out.println("第一次RN后同步了的categoryList为：" + commodityCategoryList);
        String categoryIDs = "";
        for (int i = 0; i < commodityCategoryList.size(); i++) {
            categoryIDs = categoryIDs + "," + commodityCategoryList.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());
        System.out.println("把CategoryList分割后得到的CategoryIDs = " + categoryIDs);

        //6.调用feedbackEx。
        System.out.println("第一次调用feedback方法");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "同步失败");
        }
        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback超时!");
        }

//        7.调用RN（无同步数据）
        System.out.println("第二次调用RN方法");
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

//        8.模拟网页创建一个Category
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        lTimeOut = 30;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }

//        9.退出登录
        logOut();
    }

    //1）POS1：通过普通Action创建Category A和Category B->收到服务器响应，错误码正确->仍然通过普通Action修改Category B->收到服务器响应，错误码正确。
    // 2）POS2：开机->通过SyncRN同步Category A和修改后的Category B->Feedback。
    @Test
    public void testSIT2() throws Exception {
        Shared.printTestMethodStartInfo();
        //登录pos1和staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //pos1创建Category_A
        System.out.println("-------------- pos1第一次调用create方法");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }
//        CommodityCategory c = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory.setIgnoreIDInComparision(true);
//        if (commodityCategory.compareTo(c) != 0) {
//            Assert.assertTrue("创建的和预期的值不一样", false);
//        }

        //pos1创建Category_B
        System.out.println("---------------- pos1第二次调用create方法");
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }
//        CommodityCategory c2 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory2.setIgnoreIDInComparision(true);
//        if (commodityCategory2.compareTo(c2) != 0) {
//            Assert.assertTrue("创建的和预期的值不一样", false);
//        }

//        //pos1修改Category_B
//        System.out.println("-------------------- pos1第一次调用update方法");
//        CommodityCategory commodityCategory3 = DataInput.getCommodityCategoryInput();
//        commodityCategory3.setID(commodityCategory2.getID());
//        commodityCategory3.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory3.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory3)) {
//            Assert.assertTrue("创建失败!", false);
//        }
//
//        //等待响应并验证结果
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("请求服务器修改Category超时!", false);
//        }
//        CommodityCategory c3 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory3.setIgnoreIDInComparision(true);
//        if (commodityCategory3.compareTo(c3) != 0) {
//            Assert.assertTrue("修改的和预期的值不一样", false);
//        }

        //pos2和staff2登录
        Assert.assertTrue(Shared.login(2, posLoginHttpBO, staffLoginHttpBO), "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //调用RN同步Category_A和Category_B
        System.out.println("-------------------- pos2第一次调用RN方法");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败!");
        }

        //检查错误码
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category同步超时!");
        }
        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list为空");
        System.out.println("第一次RN后同步了的categoryList为：" + list);
        String categoryIDs = "";
        for (int i = 0; i < list.size(); i++) {
            categoryIDs = categoryIDs + "," + list.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //Feedback
        System.out.println("-------------------- pos2第一次调用feedback方法");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "同步失败");
        }

        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback超时!");
        }

        //9.退出登录
        logOut();
    }

    @Test
    public void testSIT3() throws Exception {
        Shared.printTestMethodStartInfo();
        //登录pos1和staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //pos1创建Category_A
        System.out.println("-------------- pos1第一次调用create方法");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }
//        CommodityCategory b = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory.setIgnoreIDInComparision(true);
//        if (commodityCategory.compareTo(b) != 0) {
//            Assert.assertTrue("创建的和预期的值不一样", false);
//        }

        //pos1创建Category_B
        System.out.println("---------------- pos1第二次调用create方法");
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "创建失败!");
        }

        //等待响应并验证结果
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "请求服务器创建Category超时!");
        }
//        CommodityCategory b2 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory2.setIgnoreIDInComparision(true);
//        if (commodityCategory2.compareTo(b2) != 0) {
//            Assert.assertTrue("创建的和预期的值不一样", false);
//        }

//        //pos1修改Category_B
//        System.out.println("-------------------- pos1第一次调用update方法");
//        CommodityCategory commodityCategory3 = DataInput.getCommodityCategoryInput();
//        commodityCategory3.setID(b2.getID());
//        commodityCategory3.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory3.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory3)) {
//            Assert.assertTrue("创建失败!", false);
//        }
//
//        //等待响应并验证结果
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("请求服务器修改Category超时!", false);
//        }
//        CommodityCategory b3 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory3.setIgnoreIDInComparision(true);
//        if (commodityCategory3.compareTo(b3) != 0) {
//            Assert.assertTrue("修改的和预期的值不一样", false);
//        }
//
//        //pos1修改CategoryB
//        System.out.println("-------------------- pos1第二次调用update方法");
//        CommodityCategory commodityCategory4 = DataInput.getCommodityCategoryInput();
//        commodityCategory4.setID(b2.getID());
//        commodityCategory4.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory4.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory4)) {
//            Assert.assertTrue("创建失败!", false);
//        }
//
//        //等待响应并验证结果
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("请求服务器修改Category超时!", false);
//        }
//        CommodityCategory c4 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory4.setIgnoreIDInComparision(true);
//        if (commodityCategory4.compareTo(c4) != 0) {
//            Assert.assertTrue("修改的和预期的值不一样", false);
//        }

        //pos2和staff2登录
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //调用RN同步Category_A和修改后的Category_B
        System.out.println("-------------------- pos2第一次调用RN方法");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "同步失败!");
        }

        //检查错误码
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category同步超时!");
        }
        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list为空");
        System.out.println("第一次RN后同步了的categoryList为：" + list);
        String categoryIDs = "";
        for (int i = 0; i < list.size(); i++) {
            categoryIDs = categoryIDs + "," + list.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //Feedback
        System.out.println("-------------------- pos2第一次调用feedback方法");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "同步失败");
        }

        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback超时!");
        }
        logOut();
    }
}
