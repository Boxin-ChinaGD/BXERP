package com.test.bx.app;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.bo.BrandSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.model.Brand;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BrandPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class BrandSIT extends BaseHttpAndroidTestCase {
    private static BrandPresenter presenter = null;
    private static BrandSQLiteBO sqLiteBO = null;
    private static BrandHttpBO httpBO = null;
    private static BrandSQLiteEvent sqLiteEvent = null;
    private static BrandHttpEvent httpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BrandSIT = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getBrandPresenter();
        }
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
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (sqLiteEvent == null) {
            sqLiteEvent = new BrandSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BrandSIT);
        }
        if (httpEvent == null) {
            httpEvent = new BrandHttpEvent();
            httpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_BrandSIT);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);
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
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BrandSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //4.????????????????????????brand
        System.out.println("???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue("????????????!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }

        //RN??????????????????POS????????????????????????
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        sqLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }

        lTimeOut = 50;
        while (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }
        List<Brand> brandList = (List<Brand>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("ListMasterTable??????", brandList.size() > 0);
        System.out.println("?????????RN???????????????brandList??????" + brandList);
        String brandIDs = "";
        for (int i = 0; i < brandList.size(); i++) {
            brandIDs = brandIDs + "," + brandList.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());
        System.out.println("???BrandList??????????????????BrandIDs = " + brandIDs);

        //6.??????feedbackEx???
        System.out.println("???????????????feedback??????");
        if (!httpBO.feedback(brandIDs)) {
            Assert.assertTrue("????????????", false);
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Brand Feedback??????!", false);
        }

//        7.??????RN?????????????????????
        System.out.println("???????????????RN??????");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }

//        8.????????????????????????brand
        Brand brand2 = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue("????????????!", false);
        }

        lTimeOut = 30;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }

//        9.????????????
        //
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue("??????????????????! ", false);
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("????????????!", false);
        }
        //
        Assert.assertTrue("????????????,????????????????????????????????????", logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    //1???POS1???????????????Action??????Brand A???Brand B->???????????????????????????????????????->??????????????????Action??????Brand B->??????????????????????????????????????????
    // 2???POS2?????????->??????SyncRN??????Brand A???????????????Brand B->Feedback???
    @Test
    public void testSIT2() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //pos1??????Brand_A
        System.out.println("-------------- pos1???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos1??????Brand_B
        System.out.println("---------------- pos1???????????????create??????");
        Brand brand2 = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b2 = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos1??????Brand_B
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b3 = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos2???staff2??????
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //??????RN??????Brand_A???????????????Brand_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }
        List<Brand> list = (List<Brand>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("list??????", list.size() > 0);
        System.out.println("?????????RN???????????????brandList??????" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!httpBO.feedback(brandIDs)) {
            Assert.assertTrue("????????????", false);
        }

        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Brand Feedback??????!", false);
        }

        //9.????????????
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue("??????????????????! ", false);
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("????????????!", false);
        }
        //
        Assert.assertTrue("????????????,????????????????????????????????????", logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

    }

    @Test
    public void testSIT3() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????pos1???staff1
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        //pos1??????Brand_A
        System.out.println("-------------- pos1???????????????create??????");
        Brand brand = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand.setIgnoreIDInComparision(true);
        if (brand.compareTo(b) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos1??????Brand_B
        System.out.println("---------------- pos1???????????????create??????");
        Brand brand2 = DataInput.getBrandInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, brand2)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b2 = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand2.setIgnoreIDInComparision(true);
        if (brand2.compareTo(b2) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos1??????Brand_B
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand3 = DataInput.getBrandInput();
        brand3.setID(b2.getID());
        if (!httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand3)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b3 = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand3.setIgnoreIDInComparision(true);
        if (brand3.compareTo(b3) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos1??????brandB
        System.out.println("-------------------- pos1???????????????update??????");
        Brand brand4 = DataInput.getBrandInput();
        brand4.setID(b2.getID());
        if (!httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, brand4)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????????????????
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Brand??????!", false);
        }
        Brand b4 = (Brand) httpBO.getHttpEvent().getBaseModel1();
        brand4.setIgnoreIDInComparision(true);
        if (brand4.compareTo(b4) != 0) {
            Assert.assertTrue("?????????????????????????????????", false);
        }

        //pos2???staff2??????
        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);

        //??????RN??????Brand_A???????????????Brand_B
        System.out.println("-------------------- pos2???????????????RN??????");
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????!", false);
        }

        //???????????????
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Brand????????????!", false);
        }
        List<Brand> list = (List<Brand>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("list??????", list.size() > 0);
        System.out.println("?????????RN???????????????brandList??????" + list);
        String brandIDs = "";
        for (int i = 0; i < list.size(); i++) {
            brandIDs = brandIDs + "," + list.get(i).getID();
        }
        brandIDs = brandIDs.substring(1, brandIDs.length());

        //Feedback
        System.out.println("-------------------- pos2???????????????feedback??????");
        if (!httpBO.feedback(brandIDs)) {
            Assert.assertTrue("????????????", false);
        }

        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Brand Feedback??????!", false);
        }

        //9.????????????
        if (!logoutHttpBO.logoutAsync()) {
            Assert.assertTrue("??????????????????! ", false);
        }
        //
        lTimeOut = 50;
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("????????????!", false);
        }
        //
        Assert.assertTrue("????????????,????????????????????????????????????", logoutHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

    }
}