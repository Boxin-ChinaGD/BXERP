package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BrandSQLiteEvent;
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.Brand;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.List;

public class BrandSIT extends BaseHttpTestCase {
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BrandSIT = 10000;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BrandSIT);
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
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(EVENT_ID_BrandSIT);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(brandSQLiteEvent, brandHttpEvent);
            brandSQLiteBO.setBrandPresenter(brandPresenter);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(brandSQLiteEvent, brandHttpEvent);
        }
        logoutHttpEvent.setId(EVENT_ID_BrandSIT);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
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
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        event.onEvent();
    }


    public static class DataInput {
        private static Brand brandInput = null;

        protected static final Brand getBrandInput() throws CloneNotSupportedException {
            brandInput = new Brand();
            brandInput.setName("Game" + System.currentTimeMillis() % 1000000);
            brandInput.setReturnObject(1);

            return (Brand) brandInput.clone();
        }
    }

    /*
    1.pos??????
    2.staff??????
    3.??????????????????
    4.????????????????????????brand
    5.??????RN(???????????????)
    6.??????feedbackEx???
    7.??????RN?????????????????????
    8.????????????????????????brand
    9.????????????, ????????????????????????
    10.????????????
     */

    @Test
    public void testBrand() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //4.????????????????????????brand
        System.out.println("???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "????????????!" + brandHttpBO.getHttpEvent().getLastErrorCode());
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }

        //RN??????????????????POS????????????????????????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????");
        }

        lTimeOut = 50;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand????????????!");
        }
        List<Brand> brandList = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(brandList.size() > 0, "ListMasterTable??????");
        System.out.println("?????????RN???????????????brandList??????" + brandList);
        String brandIDs = "";
        for (int i = 0; i < brandList.size(); i++) {
            brandIDs = brandIDs + "," + brandList.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());
        System.out.println("???BrandList??????????????????BrandIDs = " + brandIDs);

        //6.??????feedbackEx???
        System.out.println("???????????????feedback??????");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "????????????");
        }
        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback??????!");
        }

//        7.??????RN?????????????????????
        System.out.println("???????????????RN??????");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????");
        }
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand????????????!");
        }

//        8.????????????????????????brand
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "????????????!");
        }

        lTimeOut = 30;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }

//        9.????????????
        //
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "??????????????????! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "????????????!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "????????????,????????????????????????????????????");
    }

    //1???POS1???????????????Action??????Brand A???Brand B->???????????????????????????????????????->??????????????????Action??????Brand B->??????????????????????????????????????????
    // 2???POS2?????????->??????SyncRN??????Brand A???????????????Brand B->Feedback???
    @Test
    public void testSIT2() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //pos1??????Brand_A
        System.out.println("-------------- pos1???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos1??????Brand_B
        System.out.println("---------------- pos1???????????????create??????");
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b2 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos1??????Brand_B
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b3 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos2???staff2??????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //??????RN??????Brand_A???????????????Brand_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand????????????!");
        }
        List<Brand> list = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list??????");
        System.out.println("?????????RN???????????????brandList??????" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "????????????");
        }

        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback??????!");
        }

        //9.????????????
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "??????????????????! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "????????????!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "????????????,????????????????????????????????????");

    }

    @Test
    public void testSIT3() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //pos1??????Brand_A
        System.out.println("-------------- pos1???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos1??????Brand_B
        System.out.println("---------------- pos1???????????????create??????");
        Brand brand2 = DataInput.getBrandInput();
        if (!brandHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b2 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos1??????Brand_B
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b3 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos1??????brandB
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand4 = DataInput.getBrandInput();
        brand4.setID(b2.getID());
        if (!brandHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand4)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????????????????
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "?????????????????????Brand??????!");
        }
        Brand b4 = (Brand) brandHttpBO.getHttpEvent().getBaseModel1();
        brand4.setIgnoreIDInComparision(true);
        if (brand4.compareTo(b4) != 0) {
            Assert.assertTrue(false, "?????????????????????????????????");
        }

        //pos2???staff2??????
        Shared.login(2, posLoginHttpBO, staffLoginHttpBO);

        //??????RN??????Brand_A???????????????Brand_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false, "????????????!");
        }

        //???????????????
        brandSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "Brand????????????!");
        }
        List<Brand> list = (List<Brand>) brandSQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue(list.size() > 0, "list??????");
        System.out.println("?????????RN???????????????brandList??????" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!brandHttpBO.feedback(brandIDs)) {
            Assert.assertTrue(false, "????????????");
        }

        brandHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "Brand Feedback??????!");
        }

        //9.????????????
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue(false, "??????????????????! ");
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false, "????????????!");
        }
        //
        Assert.assertTrue(logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "????????????,????????????????????????????????????");

    }
}