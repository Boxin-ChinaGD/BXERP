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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_CategorySIT) {
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
    1.pos??????
    2.staff??????
    3.??????????????????
    4.????????????????????????Category
    5.??????RN(???????????????)
    6.??????feedbackEx???
    7.??????RN?????????????????????
    8.????????????????????????Category
    9.????????????, ????????????????????????
    10.????????????
     */

    @Test
    public void testCategory() throws InterruptedException, CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //4.????????????????????????Category
        System.out.println("???????????????create??????");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "????????????!");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }

        //RN??????????????????POS????????????????????????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????");
        }

        lTimeOut = 50;
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category????????????!");
        }
        List<CommodityCategory> commodityCategoryList = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(commodityCategoryList.size() > 0, "ListMasterTable??????");
        System.out.println("?????????RN???????????????categoryList??????" + commodityCategoryList);
        String categoryIDs = "";
        for (int i = 0; i < commodityCategoryList.size(); i++) {
            categoryIDs = categoryIDs + "," + commodityCategoryList.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());
        System.out.println("???CategoryList??????????????????CategoryIDs = " + categoryIDs);

        //6.??????feedbackEx???
        System.out.println("???????????????feedback??????");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "????????????");
        }
        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback??????!");
        }

//        7.??????RN?????????????????????
        System.out.println("???????????????RN??????");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????");
        }
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category????????????!");
        }

//        8.????????????????????????Category
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "????????????!");
        }

        lTimeOut = 30;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Category??????!");
        }

//        9.????????????
        logOut();
    }

    //1???POS1???????????????Action??????Category A???Category B->???????????????????????????????????????->??????????????????Action??????Category B->??????????????????????????????????????????
    // 2???POS2?????????->??????SyncRN??????Category A???????????????Category B->Feedback???
    @Test
    public void testSIT2() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //pos1??????Category_A
        System.out.println("-------------- pos1???????????????create??????");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Category??????!");
        }
//        CommodityCategory c = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory.setIgnoreIDInComparision(true);
//        if (commodityCategory.compareTo(c) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

        //pos1??????Category_B
        System.out.println("---------------- pos1???????????????create??????");
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Category??????!");
        }
//        CommodityCategory c2 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory2.setIgnoreIDInComparision(true);
//        if (commodityCategory2.compareTo(c2) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

//        //pos1??????Category_B
//        System.out.println("-------------------- pos1???????????????update??????");
//        CommodityCategory commodityCategory3 = DataInput.getCommodityCategoryInput();
//        commodityCategory3.setID(commodityCategory2.getID());
//        commodityCategory3.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory3.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory3)) {
//            Assert.assertTrue("????????????!", false);
//        }
//
//        //???????????????????????????
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("?????????????????????Category??????!", false);
//        }
//        CommodityCategory c3 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory3.setIgnoreIDInComparision(true);
//        if (commodityCategory3.compareTo(c3) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

        //pos2???staff2??????
        Assert.assertTrue(Shared.login(2, posLoginHttpBO, staffLoginHttpBO), "????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //??????RN??????Category_A???Category_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category????????????!");
        }
        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list??????");
        System.out.println("?????????RN???????????????categoryList??????" + list);
        String categoryIDs = "";
        for (int i = 0; i < list.size(); i++) {
            categoryIDs = categoryIDs + "," + list.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "????????????");
        }

        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback??????!");
        }

        //9.????????????
        logOut();
    }

    @Test
    public void testSIT3() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //pos1??????Category_A
        System.out.println("-------------- pos1???????????????create??????");
        CommodityCategory commodityCategory = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Category??????!");
        }
//        CommodityCategory b = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory.setIgnoreIDInComparision(true);
//        if (commodityCategory.compareTo(b) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

        //pos1??????Category_B
        System.out.println("---------------- pos1???????????????create??????");
        CommodityCategory commodityCategory2 = DataInput.getCommodityCategoryInput();
        if (!commodityCategoryHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodityCategory2)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Category??????!");
        }
//        CommodityCategory b2 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory2.setIgnoreIDInComparision(true);
//        if (commodityCategory2.compareTo(b2) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

//        //pos1??????Category_B
//        System.out.println("-------------------- pos1???????????????update??????");
//        CommodityCategory commodityCategory3 = DataInput.getCommodityCategoryInput();
//        commodityCategory3.setID(b2.getID());
//        commodityCategory3.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory3.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory3)) {
//            Assert.assertTrue("????????????!", false);
//        }
//
//        //???????????????????????????
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("?????????????????????Category??????!", false);
//        }
//        CommodityCategory b3 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory3.setIgnoreIDInComparision(true);
//        if (commodityCategory3.compareTo(b3) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }
//
//        //pos1??????CategoryB
//        System.out.println("-------------------- pos1???????????????update??????");
//        CommodityCategory commodityCategory4 = DataInput.getCommodityCategoryInput();
//        commodityCategory4.setID(b2.getID());
//        commodityCategory4.setSyncType(Constants.SYNC_Type_U);
//        commodityCategory4.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//        if (!httpBO.updateAsync(commodityCategory4)) {
//            Assert.assertTrue("????????????!", false);
//        }
//
//        //???????????????????????????
//        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("?????????????????????Category??????!", false);
//        }
//        CommodityCategory c4 = (CommodityCategory) httpBO.getHttpEvent().getBaseModel1();
//        commodityCategory4.setIgnoreIDInComparision(true);
//        if (commodityCategory4.compareTo(c4) != 0) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }

        //pos2???staff2??????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //??????RN??????Category_A???????????????Category_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????
        commodityCategorySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategorySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Category????????????!");
        }
        List<CommodityCategory> list = (List<CommodityCategory>) commodityCategorySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list??????");
        System.out.println("?????????RN???????????????categoryList??????" + list);
        String categoryIDs = "";
        for (int i = 0; i < list.size(); i++) {
            categoryIDs = categoryIDs + "," + list.get(i).getID();
        }
        categoryIDs = categoryIDs.substring(1, categoryIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!commodityCategoryHttpBO.feedback(categoryIDs)) {
            Assert.assertTrue(false, "????????????");
        }

        commodityCategoryHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityCategoryHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "CommodityCategory Feedback??????!");
        }
        logOut();
    }
}
