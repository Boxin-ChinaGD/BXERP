package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarcodesPresenterTest extends BasePresenterTest {

    private List<BaseModel> bmList = new ArrayList<BaseModel>();

    private static final int Event_ID_BarcodesPresenterTest = 10000;

    @BeforeClass
    public void setUp(){
        super.setUp();

        EventBus.getDefault().register(this);

//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        if (barcodesSQLiteEvent != null) {
            barcodesSQLiteEvent.setId(Event_ID_BarcodesPresenterTest);
        }
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

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

    public static class DataInput {
        private static Barcodes barcodesInput = new Barcodes();

        protected static final Barcodes getBarcodes() throws Exception {
            Random ran = new Random();

            barcodesInput = new Barcodes();
            barcodesInput.setCommodityID(ran.nextInt(15) + 1);
            barcodesInput.setBarcode(String.valueOf(ran.nextInt(1000000000)));
            barcodesInput.setOperatorStaffID(4);

            return (Barcodes) barcodesInput.clone();
        }

        protected static final List<?> getBarcodesList() throws Exception {
            List<Barcodes> barcodesList = new ArrayList<Barcodes>();
            for (int i = 0; i < 10; i++) {
                barcodesList.add(getBarcodes());
            }
            return barcodesList;
        }
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == Event_ID_BarcodesPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
        //????????????
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case???????????????????????????null??????????????????
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesList.get(0).setBarcode(null);
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync?????????");
    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //????????????
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case???????????????????????????null??????????????????
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes.setBarcode(null);
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case1
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b.compareTo(barcodes) == 0, "?????????????????????????????????????????????");
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????Case2
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b.compareTo(barcodes) == 0, "?????????????????????????????????????????????");
        //??????
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(barcodes.getID());
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b != null, "???????????????");
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID???null?????????????????????????????????
        Barcodes barcodes2 = new Barcodes();
        barcodes2.setID(null);
        barcodes2 = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????");
        Assert.assertTrue(barcodes2 == null, "???????????????");
    }

    @Test
    public void test_c4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????ID?????????ID??????????????????????????????????????????
        Barcodes barcodes3 = new Barcodes();
        barcodes3.setID(-1);
        barcodes3 = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes3);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync?????????");
        Assert.assertTrue(barcodes3 == null, "???????????????");
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "?????????list????????????null???size??????0");
    }

    @Test
    public void test_d2_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        barcodes.setSql("where F_Barcode = %s");
        barcodes.setConditions(new String[]{barcodes.getBarcode()});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "?????????list????????????null???size??????0");
    }

    @Test
    public void test_d3_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //       ???case??????????????????????????????????????????case??? ?????????.?????????
//        List<Barcodes> barcodesList = new ArrayList<>();
//
//        Barcodes barcodes = DataInput.getBarcodes();
//        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
//        Assert.assertTrue("createObjectSync?????????", barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        //??????Case???????????????????????????
//        barcodes.setSql("where F_Barcode = ?");
//        barcodes.setConditions(new String[]{barcodes.getBarcode(), "20154782"});
//        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
//        Assert.assertTrue("retrieveNSync?????????????????????", barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d4_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //??????Case???????????????????????????
        barcodes.setSql("where F_Barcode = %s and F_ID = %s");
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        barcodes.setConditions(new String[]{barcodes.getBarcode()});
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync?????????????????????");
    }

    @Test
    public void test_d5_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //Case???????????????????????????
        barcodes.setSql("where F_Barcode = %s and F_ID = '%s'");
        barcodes.setConditions(new String[]{barcodes.getBarcode(), String.valueOf(barcodes.getID())});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync?????????????????????");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "?????????list????????????null???size??????0");
    }

    @Test
    public void test_e1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b != null, "???????????????");
        //
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync?????????");
        //
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b == null, "???????????????");
    }

    @Test
    public void test_e2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(100000000);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteObjectSync?????????");
    }

    @Test
    public void test_e3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(null);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync?????????");
    }

    @Test
    public void test_e4_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID?????????
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(-1);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync?????????");
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "retrieveNObjectSync???????????????list????????????");
        //
        barcodesPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync?????????????????????");
        //
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(barcodesList == null || barcodesList.size() == 0, "retrieveNObjectSync???????????????list????????????");
    }

    @Test
    public void test_g1_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        for (BaseModel barcodes : barcodesList) {
            barcodes.setSyncType(BasePresenter.SYNC_Type_C);
        }
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        barcodesPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????????????????");
    }

    @Test
    public void test_g2_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        for (BaseModel barcodes : barcodesList) {
            barcodes.setSyncType(BasePresenter.SYNC_Type_C);
        }
        bmList = barcodesList;
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        barcodesPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????????????????");
        //??????Case???????????????(??????????????????????????????????????????)
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        barcodesPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, barcodesSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????????????????ErrorCode=" + barcodesSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_g3_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null
        List<BaseModel> bList = new ArrayList<BaseModel>();
        List<Barcodes> barcodesList1 = (List<Barcodes>) DataInput.getBarcodesList();
        for (Barcodes barcodes : barcodesList1) {
            barcodes.setSyncType(BasePresenter.SYNC_Type_C);
            barcodes.setBarcode(null);
            bList.add(barcodes);
        }
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        barcodesPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync??????????????????????????????");
    }

    @Test
    public void test_h1_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        int barcodeID = 11;
        for (BaseModel barcodes : barcodesList) {
            barcodes.setID(--barcodeID);
            barcodes.setSyncType(BasePresenter.SYNC_Type_C);
        }
        bmList = barcodesList;
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        barcodesPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC??????????????????????????????");
    }

    @Test
    public void test_h2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????Case???????????????(??????????????????????????????????????????)
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        barcodesPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC??????????????????????????????");
    }

    @Test
    public void test_h3_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null
        List<BaseModel> bList = new ArrayList<BaseModel>();
        List<Barcodes> barcodesList1 = (List<Barcodes>) DataInput.getBarcodesList();
        for (Barcodes barcodes : barcodesList1) {
            barcodes.setSyncType(BasePresenter.SYNC_Type_C);
            barcodes.setBarcode(null);
            bList.add(barcodes);
        }
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        barcodesPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC??????????????????????????????");
    }

    @Test
    public void test_i1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
    }

    @Test
    public void test_i2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");

        //case???????????????
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
    }

    @Test
    public void test_i3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        for (Barcodes barcodes : barcodesList) {
            barcodes.setBarcode(null);
        }
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNAsync???????????????????????????????????????" + barcodesSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_j1_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????Case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
    }

    @Test
    public void test_j2_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = DataInput.getBarcodes();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
        //case???????????????
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync?????????????????????");
    }

    @Test
    public void test_j3_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case???????????????????????????null??????????????????
        Barcodes barcodes1 = DataInput.getBarcodes();
        barcodes1.setBarcode(null);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1, barcodesSQLiteEvent);
        //
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync?????????????????????");
    }

    @Test
    public void test_k1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");
        //
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b != null, "???????????????");
        //
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync?????????????????????");
        //
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync?????????");
        Assert.assertTrue(b == null, "???????????????");
    }

    @Test
    public void test_k2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case?????????????????????ID?????????????????????
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(1000000);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteAsync?????????????????????");
    }

    @Test
    public void test_k3_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID???null????????????????????????
        Barcodes barcodes2 = DataInput.getBarcodes();
        barcodes2.setID(null);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync?????????????????????");
    }

    @Test
    public void test_k4_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //??????case??????????????????ID?????????
        Barcodes barcodes2 = DataInput.getBarcodes();
        barcodes2.setID(-1);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync?????????????????????");
    }

    @Test
    public void test_l_retrieveNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync?????????");

        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "retrieveNAsync?????????");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync?????????????????????");
        Assert.assertTrue(barcodesSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync????????????????????????barcodes");
    }

    /**
     * ????????????T_Barcodes???????????????
     */
    @Test
    public void test_retrieveNBarcodes() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = barcodesPresenter.retrieveCount();
        System.out.println("T_Barcodes???????????????" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "???????????????");
    }

}
