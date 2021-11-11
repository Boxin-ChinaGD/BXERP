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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
        //重复插入
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        barcodesList.get(0).setBarcode(null);
        barcodesPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //重复插入
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes.setBarcode(null);
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b.compareTo(barcodes) == 0, "查找的对象与插入的对象不一致！");
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常Case2
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b.compareTo(barcodes) == 0, "查找的对象与插入的对象不一致！");
        //正常
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(barcodes.getID());
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查找失败！");
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Barcodes barcodes2 = new Barcodes();
        barcodes2.setID(null);
        barcodes2 = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(barcodes2 == null, "查找失败！");
    }

    @Test
    public void test_c4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Barcodes barcodes3 = new Barcodes();
        barcodes3.setID(-1);
        barcodes3 = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes3);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(barcodes3 == null, "查找失败！");
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d2_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        barcodes.setSql("where F_Barcode = %s");
        barcodes.setConditions(new String[]{barcodes.getBarcode()});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d3_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //       该case属于特殊情况，但目前考虑到该case， 故保留.不运行
//        List<Barcodes> barcodesList = new ArrayList<>();
//
//        Barcodes barcodes = DataInput.getBarcodes();
//        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
//        Assert.assertTrue("createObjectSync失败！", barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        //异常Case：一个通配符多个值
//        barcodes.setSql("where F_Barcode = ?");
//        barcodes.setConditions(new String[]{barcodes.getBarcode(), "20154782"});
//        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
//        Assert.assertTrue("retrieveNSync错误码不正确！", barcodesPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d4_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //异常Case：多个通配符一个值
        barcodes.setSql("where F_Barcode = %s and F_ID = %s");
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        barcodes.setConditions(new String[]{barcodes.getBarcode()});
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确！");
    }

    @Test
    public void test_d5_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Barcodes> barcodesList = new ArrayList<Barcodes>();
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //Case：多个通配符多个值
        barcodes.setSql("where F_Barcode = %s and F_ID = '%s'");
        barcodes.setConditions(new String[]{barcodes.getBarcode(), String.valueOf(barcodes.getID())});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_e1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查询失败！");
        //
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync失败！");
        //
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查询失败！");
    }

    @Test
    public void test_e2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(100000000);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteObjectSync失败！");
    }

    @Test
    public void test_e3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(null);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_e4_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Barcodes barcodes1 = new Barcodes();
        barcodes1.setID(-1);
        barcodesPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "retrieveNObjectSync查询出来的list必须有值");
        //
        barcodesPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync错误码不正确！");
        //
        barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(barcodesList == null || barcodesList.size() == 0, "retrieveNObjectSync查询出来的list必须无值");
    }

    @Test
    public void test_g1_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
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
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
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
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
        //异常Case：重复插入(查找到重复后会删除再进行插入)
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
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！ErrorCode=" + barcodesSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_g3_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null
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
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
    }

    @Test
    public void test_h1_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
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
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_h2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case：重复插入(查找到重复后会删除再进行插入)
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
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_h3_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null
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
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_i1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
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
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_i2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBarcodesList();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");

        //case：重复插入
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_i3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        List<Barcodes> barcodesList = (List<Barcodes>) DataInput.getBarcodesList();
        for (Barcodes barcodes : barcodesList) {
            barcodes.setBarcode(null);
        }
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateNAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        barcodesPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNAsync错误码不正确！当前错误码：" + barcodesSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_j1_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
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
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //case：重复插入
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_j3_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        Barcodes barcodes1 = DataInput.getBarcodes();
        barcodes1.setBarcode(null);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1, barcodesSQLiteEvent);
        //
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync错误码不正确！");
    }

    @Test
    public void test_k1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Barcodes barcodes = DataInput.getBarcodes();
        barcodes = (Barcodes) barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Barcodes b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查询失败！");
        //
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, barcodesSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (barcodesSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");
        //
        b = (Barcodes) barcodesPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查询失败！");
    }

    @Test
    public void test_k2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（会出现异常）
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
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_k3_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null（不会出现异常）
        Barcodes barcodes2 = DataInput.getBarcodes();
        barcodes2.setID(null);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_k4_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Barcodes barcodes2 = DataInput.getBarcodes();
        barcodes2.setID(-1);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_DeleteAsync);
        barcodesSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        barcodesPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2, barcodesSQLiteEvent);

        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_l_retrieveNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = DataInput.getBarcodes();
        barcodesPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue(barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");

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
            Assert.assertTrue(false, "retrieveNAsync超时！");
        }
        Assert.assertTrue(barcodesSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNAsync错误码不正确！");
        Assert.assertTrue(barcodesSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync应该查询到所有的barcodes");
    }

    /**
     * 查询本地T_Barcodes表的总条数
     */
    @Test
    public void test_retrieveNBarcodes() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = barcodesPresenter.retrieveCount();
        System.out.println("T_Barcodes表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}
