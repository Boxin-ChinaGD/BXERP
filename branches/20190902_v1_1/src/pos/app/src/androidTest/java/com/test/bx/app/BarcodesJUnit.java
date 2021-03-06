package com.test.bx.app;

import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BarcodesPresenter;
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

public class BarcodesJUnit extends BaseHttpAndroidTestCase {
    private static BarcodesPresenter presenter = null;
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static List<Barcodes> List;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesJUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (presenter == null) {
            presenter = GlobalController.getInstance().getBarcodesPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
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
            sqLiteEvent = new BarcodesSQLiteEvent();
            sqLiteEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (httpEvent == null) {
            httpEvent = new BarcodesHttpEvent();
            httpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
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
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Barcodes BarcodesInput = null;

        protected static final Barcodes getBarcodesInput() throws CloneNotSupportedException, ParseException {
            BarcodesInput = new Barcodes();
            BarcodesInput.setBarcode("1111" + System.currentTimeMillis() % 1000000);
            BarcodesInput.setCommodityID(1);
            BarcodesInput.setSyncType("C");
            BarcodesInput.setInt1(1);//...
            BarcodesInput.setOperatorStaffID(1);// ??????ID
            BarcodesInput.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            return (Barcodes) BarcodesInput.clone();
        }

        protected static final List<BaseModel> getBarcodesList() throws CloneNotSupportedException, ParseException {
            List<BaseModel> BarcodesList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                BarcodesList.add(getBarcodesInput());
            }
            return BarcodesList;
        }
    }

    @Test
    public void test_a_RetrieveNEx() throws Exception {
        Shared.printTestMethodStartInfo();
        //1,2????????????POS???Staff
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.????????????????????????Barcodes
        System.out.println("???????????????create??????");
        createBarcodes();

        //POS2,STAFF2??????
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        Assert.assertTrue("??????????????????????????????", retrieveNBarcodes().size() > 0);
    }

    private void createBarcodes() throws Exception {
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, Barcodes)) {
            Assert.assertTrue("????????????!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Barcodes??????!", false);
        }
    }

    private List<Barcodes> retrieveNBarcodes() throws InterruptedException {
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Barcodes????????????!", false);
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        return barcodesList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //4.????????????????????????Barcodes
        System.out.println("???????????????create??????");
        createBarcodes();

        //POS2,STAFF2??????
        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        List = retrieveNBarcodes();
        Assert.assertTrue("??????????????????????????????", List.size() > 0);

        String BarcodesIDs = "";
        for (int i = 0; i < List.size(); i++) {
            BarcodesIDs = BarcodesIDs + "," + List.get(i).getID();
        }
        BarcodesIDs = BarcodesIDs.substring(1, BarcodesIDs.length());

        //??????Feedback,
        System.out.println("???????????????feedback??????");
        if (!httpBO.feedback(BarcodesIDs)) {
            Assert.assertTrue("????????????", false);
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Barcodes Feedback??????!", false);
        }
        //??????RN??????????????????
        System.out.println("???????????????RN??????");
        List<Barcodes> xbarcodesList = retrieveNBarcodes();
        Assert.assertTrue("feedback??????", xbarcodesList == null);
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("createAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("createAsync???????????????????????????!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
            Assert.assertTrue("retrieve1???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("??????Barcodes??????!", c.compareTo(Barcodes) == 0);
        }

        //??????Case:????????????
        sqLiteEvent.setEventProcessed(false);
        //???Barcodes???????????????SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("createAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("createSync??????????????????, ???????????????????????????OtherError!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
        }
    }

    @Test
    public void test_k_retrieveNAsync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //??????Case1: ????????????
        System.out.println("-------------------------------------RN2");
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        sqLiteBO.retrieveNAsync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("retrieveN??????!??????:??????", false);
        }
        Assert.assertTrue("retrieveN?????????????????????!", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveN ????????????????????????", sqLiteEvent.getListMasterTable().size() != 0);
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes???List
        List<Barcodes> BarcodesList = new ArrayList<Barcodes>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
            BarcodesList.add(Barcodes);
        }
        //???Barcodes???List???????????????SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList);
        Long conflictID = BarcodesList.get(1).getID();
        //
        Assert.assertTrue("createNSync???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i));
            Assert.assertTrue("retrieve1???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Assert.assertTrue("?????????BarcodesList????????????????????????", Barcodes.compareTo(BarcodesList.get(i)) == 0);
        }

        //??????Case:????????????
        List<Barcodes> BarcodesList2 = new ArrayList<Barcodes>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
            Barcodes.setID(conflictID);
            BarcodesList2.add(Barcodes);
        }
        //???Barcodes???List???????????????SQLite
        presenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList2);
        //
        Assert.assertTrue("createNSync????????????, ???????????????????????????OtherError", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Barcodes??????!", c.compareTo(Barcodes) == 0);

        //??????Case:????????????
        //????????????Barcodes???????????????SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync??????????????????, ???????????????????????????OtherError!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Barcodes??????!", c.compareTo(Barcodes) == 0);
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //????????????Barcodes
        Barcodes Barcodes = BarcodesJUnit.DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        //
        Assert.assertTrue("createSync???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Barcodes c = (Barcodes) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, Barcodes);
        Assert.assertTrue("retrieve1???????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????Barcodes??????!", c.compareTo(Barcodes) == 0);

        //????????????Barcodes
        BaseModel bm1 = BarcodesJUnit.DataInput.getBarcodesInput();
        bm1.setID(c.getID());
        presenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        BaseModel bm2 = BarcodesJUnit.DataInput.getBarcodesInput();
        bm2.setID(c.getID());
        BaseModel bm3 = presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????", bm3 == null);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = BarcodesJUnit.DataInput.getBarcodesList();
        if (!presenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync????????????!", false);
        }

        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.assertTrue("CreateNAsync????????????!??????:??????", false);
        } else {
            Assert.assertTrue("CreateNAsync????????????????????????", sqLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

            List<Barcodes> BarcodesList = (List<Barcodes>) sqLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                Barcodes c = (Barcodes) list.get(i);
                Assert.assertTrue("CreateNAsync???????????????????????????", c.compareTo(BarcodesList.get(i)) == 0);
            }
        }
    }

    //????????????
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        //??????SQLite?????????????????????????????????????????????
        Barcodes c = BarcodesJUnit.DataInput.getBarcodesInput();
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");

        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);

        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.assertTrue("???????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("?????????????????????Barcodes??????!", false);
        }

        //???????????????????????????sqlite??????????????????,
        List<Barcodes> barcodesList = (List<Barcodes>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Barcodes> list = (List<Barcodes>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != Integer.valueOf(barcodes.getPageSize())) {
            Assert.assertTrue("??????????????????????????????????????????????????????SQLite??????????????????", false);
        }
    }
}
