package wpos.UT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BarcodesJUnit extends BaseHttpTestCase {
    private static BarcodesSQLiteBO sqLiteBO = null;
    private static BarcodesHttpBO httpBO = null;
    private static BarcodesSQLiteEvent sqLiteEvent = null;
    private static BarcodesHttpEvent httpEvent = null;
    private static java.util.List<Barcodes> List;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int EVENT_ID_BarcodesJUnit = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_BarcodesJUnit);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
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

        if (sqLiteBO == null) {
            sqLiteBO = new BarcodesSQLiteBO(sqLiteEvent, httpEvent);
            sqLiteBO.setBarcodesPresenter(barcodesPresenter);
        }
        if (httpBO == null) {
            httpBO = new BarcodesHttpBO(sqLiteEvent, httpEvent);
        }
        sqLiteEvent.setSqliteBO(sqLiteBO);
        sqLiteEvent.setHttpBO(httpBO);
        httpEvent.setSqliteBO(sqLiteBO);
        httpEvent.setHttpBO(httpBO);

        logoutHttpEvent.setId(EVENT_ID_BarcodesJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_BarcodesJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //4.????????????????????????Barcodes
        System.out.println("???????????????create??????");
        createBarcodes();

        //POS2,STAFF2??????
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        Assert.assertTrue(retrieveNBarcodes().size() > 0, "??????????????????????????????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    private void createBarcodes() throws Exception {
        Barcodes Barcodes = DataInput.getBarcodesInput();
        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, Barcodes)) {
            Assert.fail("????????????!");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("?????????????????????Barcodes??????!");
        }
    }

    private List<Barcodes> retrieveNBarcodes() throws InterruptedException {
        sqLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!httpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.fail("????????????");
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("Barcodes????????????!");
        }
        List<Barcodes> barcodesList = (List<Barcodes>) sqLiteBO.getSqLiteEvent().getListMasterTable();
        return barcodesList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //4.????????????????????????Barcodes
        System.out.println("???????????????create??????");
        createBarcodes();
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
        //POS2,STAFF2??????
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        List = retrieveNBarcodes();
        Assert.assertTrue(List.size() > 0, "??????????????????????????????");

        String BarcodesIDs = "";
        for (Barcodes barcodes : List) {
            BarcodesIDs = BarcodesIDs + "," + barcodes.getID();
        }
        BarcodesIDs = BarcodesIDs.substring(1);

        //??????Feedback,
        System.out.println("???????????????feedback??????");
        if (!httpBO.feedback(BarcodesIDs)) {
            Assert.fail("????????????");
        }
        httpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (httpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("Barcodes Feedback??????!");
        }
        //??????RN??????????????????
        System.out.println("???????????????RN??????");
        List<Barcodes> xbarcodesList = retrieveNBarcodes();
        Assert.assertNull(xbarcodesList, "feedback??????");
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }

    @Test
    public void test_c_CreateASync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("createAsync????????????!??????:??????");
        } else {
            barcodes = (wpos.model.Barcodes) sqLiteEvent.getBaseModel1();
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createAsync???????????????????????????!");
            Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
            Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1???????????????????????????!");
            Assert.assertEquals(c.compareTo(barcodes), 0, "??????Barcodes??????!");
        }

        //??????Case:????????????
        sqLiteEvent.setEventProcessed(false);
        //???Barcodes???????????????SQLite
        sqLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        sqLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("createAsync????????????!??????:??????");
        } else {
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createSync??????????????????, ???????????????????????????OtherError!");
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
            Assert.fail("retrieveN??????!??????:??????");
        }
        Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieveN?????????????????????!");
        Assert.assertTrue(sqLiteEvent.getListMasterTable().size() != 0, "retrieveN ????????????????????????");
    }

    @Test
    public void test_f_CreateNSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes???List
        List<Barcodes> BarcodesList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = DataInput.getBarcodesInput();
            BarcodesList.add(Barcodes);
        }
        //???Barcodes???List???????????????SQLite
        BarcodesList = (java.util.List<Barcodes>) barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList);
        int conflictID = BarcodesList.get(1).getID();
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createNSync???????????????????????????!");
        //
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList.get(i));
            Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1???????????????????????????!");
            Assert.assertEquals(Barcodes.compareTo(BarcodesList.get(i)), 0, "?????????BarcodesList????????????????????????");
        }

        //??????Case:????????????
        List<Barcodes> BarcodesList2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Barcodes Barcodes = DataInput.getBarcodesInput();
            Barcodes.setID(conflictID);
            BarcodesList2.add(Barcodes);
        }
        //???Barcodes???List???????????????SQLite
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, BarcodesList2);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createNSync????????????, ???????????????????????????OtherError");
    }

    @Test
    public void test_g_CreateSync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //??????Case
        //????????????Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync???????????????????????????!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1???????????????????????????!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "??????Barcodes??????!");

        //??????Case:????????????
        //????????????Barcodes???????????????SQLite
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_OtherError, "createSync??????????????????, ???????????????????????????OtherError!");
    }

    @Test
    public void test_y_Retrieve1Sync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        //????????????Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync???????????????????????????!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1???????????????????????????!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "??????Barcodes??????!");
    }

    @Test
    public void test_j_DeleteSync() throws CloneNotSupportedException, ParseException {
        //????????????Barcodes
        Barcodes barcodes = DataInput.getBarcodesInput();
        //???Barcodes???????????????SQLite
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        //
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "createSync???????????????????????????!");
        //
        Barcodes c = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1???????????????????????????!");
        Assert.assertEquals(c.compareTo(barcodes), 0, "??????Barcodes??????!");

        //????????????Barcodes
        BaseModel bm1 = DataInput.getBarcodesInput();
        bm1.setID(c.getID());
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 ????????????,??????????????????????????????!");

        BaseModel bm2 = DataInput.getBarcodesInput();
        bm2.setID(c.getID());
        BaseModel bm3 = barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 ????????????,??????????????????????????????!");
        Assert.assertNull(bm3, "?????????????????????");
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> list = DataInput.getBarcodesList();
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);

        if (!barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, list, sqLiteEvent)) {
            Assert.fail("CreateNAsync????????????!");
        }

        if (!waitForEventProcessed(sqLiteEvent)) {
            Assert.fail("CreateNAsync????????????!??????:??????");
        } else {
            Assert.assertSame(sqLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateNAsync????????????????????????");

            List<Barcodes> BarcodesList = (List<Barcodes>) sqLiteEvent.getListMasterTable();
            for (int i = 0; i < list.size(); i++) {
                Barcodes c = (Barcodes) list.get(i);
                c.setIgnoreIDInComparision(true);
                Assert.assertEquals(c.compareTo(BarcodesList.get(i)), 0, "CreateNAsync???????????????????????????");
            }
        }
    }

    //????????????
    public void test_l_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        //??????SQLite?????????????????????????????????????????????
        Barcodes c = DataInput.getBarcodesInput();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);

        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");

        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);

        if (!httpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.fail("???????????????");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (sqLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("?????????????????????Barcodes??????!");
        }

        //???????????????????????????sqlite??????????????????,
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertSame(barcodesPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync????????????,??????:????????????????????????!");

        List<Barcodes> list = (List<Barcodes>) httpBO.getHttpEvent().getListMasterTable();
        if (list.size() != Integer.valueOf(barcodes.getPageSize())) {
            Assert.fail("??????????????????????????????????????????????????????SQLite??????????????????");
        }
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }
}
