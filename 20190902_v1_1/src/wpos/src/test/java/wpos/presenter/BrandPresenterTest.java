package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.BrandSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.Brand;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrandPresenterTest extends BasePresenterTest {
    private List<BaseModel> bmList = new ArrayList<BaseModel>();

    private static final int Event_ID_BrandPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();
        EventBus.getDefault().register(this);

        if (brandSQLiteEvent != null) {
            brandSQLiteEvent.setId(Event_ID_BrandPresenterTest);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == Event_ID_BrandPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Brand brandInput = new Brand();

        protected static final Brand getBrand() throws Exception {
            Random ran = new Random();

            brandInput = new Brand();
            brandInput.setName("品牌" + ran.nextInt(100));
            brandInput.setOperatorStaffID(4);

            return (Brand) brandInput.clone();
        }

        protected static final List<?> getBrandList() throws Exception {
            List<Brand> brandList = new ArrayList<Brand>();
            for (int i = 0; i < 5; i++) {
                brandList.add(getBrand());
            }
            return brandList;
        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
        //case：重复插入
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync失败！");
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        brandList.get(0).setName(null);
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNObjectSync失败！");
    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常case
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //case：重复插入
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        Brand brand = DataInput.getBrand();
        brand.setName(null);
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b.compareTo(brand) == 0, "查找的对象与插入的对象不一致！");
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常Case2
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b.compareTo(brand) == 0, "查找的对象与插入的对象不一致！");
        //正常
        Brand brand2 = new Brand();
        brand2.setID(brand.getID());
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand2);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查找失败！");
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Brand brand = new Brand();
        brand.setID(null);
        brand = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(brand == null, "查找失败！");
    }

    @Test
    public void test_c4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Brand brand = new Brand();
        brand.setID(-1);
        brand = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync失败！");
        Assert.assertTrue(brand == null, "查找失败！");
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Brand> barcodesList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(barcodesList != null && barcodesList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d2_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<Brand>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        brand.setSql("where F_Name = %s");
        brand.setConditions(new String[]{brand.getName()});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(brandList != null && brandList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d3_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<Brand>();
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        brand.setSql("where F_ID = %s");
        brand.setConditions(new String[]{String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(brandList != null && brandList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d4_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<Brand>();
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        brand.setSql("where F_ID = %s and F_Name = %s");
        brand.setConditions(new String[]{String.valueOf(brand.getID()), brand.getName()});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(brandList != null && brandList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d5_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<Brand>();
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        brand.setSql("where F_Name = %s  and  F_ID = %s");
        brand.setConditions(new String[]{brand.getName(), String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNSync错误码不正确！");
        Assert.assertTrue(brandList != null && brandList.size() > 0, "查询的list不应该为null或size等于0");
    }

    @Test
    public void test_d6_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<Brand>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        brand.setSql("where F_Name = %s  and  F_ID = %s");
        brand.setConditions(new String[]{String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieveNSync错误码不正确！");
        Assert.assertTrue(brandList == null, "查询的list不应该为null或size等于0");
    }

    //...

    @Test
    public void test_e1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查询失败！");
        //
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync失败！");
        //
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查询失败！");
    }

    @Test
    public void test_e2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        Brand brand = new Brand();
        brand.setID(100000000);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteObjectSync失败！");
    }

    @Test
    public void test_e3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Brand brand = new Brand();
        brand.setID(null);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_e4_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Brand brand = new Brand();
        brand.setID(-1);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteObjectSync失败！");
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Brand> brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(brandList != null && brandList.size() > 0, "retrieveNObjectSync查询出来的list必须有值");
        //
        brandPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync错误码不正确！");
        //
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(brandList == null || brandList.size() == 0, "retrieveNObjectSync查询出来的list必须无值");
    }

    @Test
    public void test_g1_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> brandList = (List<BaseModel>) DataInput.getBrandList();
        for (BaseModel brand : brandList) {
            brand.setSyncType(BasePresenter.SYNC_Type_C);
        }
        bmList = brandList;
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        brandPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, brandList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
    }

    @Test
    public void test_g2_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case：重复插入(查找到重复后会删除再进行插入)
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        brandPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
    }

    @Test
    public void test_g3_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null
        List<Brand> bList = new ArrayList<Brand>();
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        for (Brand brand : brandList) {
            brand.setSyncType(BasePresenter.SYNC_Type_C);
            brand.setName(null);
            bList.add(brand);
        }
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        brandPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsync失败，错误码不正确！");
    }

    @Test
    public void test_h1_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> brandList = (List<BaseModel>) DataInput.getBrandList();
        for (BaseModel brand : brandList) {
            brand.setSyncType(BasePresenter.SYNC_Type_C);
        }
        bmList = brandList;
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        brandPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, brandList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_h2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case：重复插入(查找到重复后会删除再进行插入)
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        brandPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_h3_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null
        List<BaseModel> bList = new ArrayList<BaseModel>();
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        for (Brand brands : brandList) {
            brands.setSyncType(BasePresenter.SYNC_Type_C);
            brands.setName(null);
            bList.add(brands);
        }
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        brandPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false, "refreshByServerDataAsyncC超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "refreshByServerDataAsyncC失败，错误码不正确！");
    }

    @Test
    public void test_i1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> brandList = (List<BaseModel>) DataInput.getBrandList();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brandList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_i2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBrandList();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");

        //case：重复插入
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, brandSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createNAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync错误码不正确！");
    }

    @Test
    public void test_i3_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        for (Brand brand : brandList) {
            brand.setName(null);
        }
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brandList, brandSQLiteEvent);

        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNAsync错误码不正确！当前错误码：" + brandSQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_j1_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case
        Brand brand = DataInput.getBrand();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandSQLiteEvent.setHttpBO(null);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_j2_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Brand brand = DataInput.getBrand();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandSQLiteEvent.setHttpBO(null);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
        //case：重复插入
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandSQLiteEvent.setHttpBO(null);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "createAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync错误码不正确！");
    }

    @Test
    public void test_j3_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：插入的非空字段为null（插入失败）
        Brand brand = DataInput.getBrand();
        brand.setName(null);
        brandSQLiteEvent.setHttpBO(null);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        //
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createAsync错误码不正确！");
    }

    @Test
    public void test_k1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand brand = DataInput.getBrand();
        brand = (Brand) brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync失败！");
        //
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b != null, "查询失败！");
        //
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteAsync错误码不正确！");
        //
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue(brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync失败！");
        Assert.assertTrue(b == null, "查询失败！");
    }

    @Test
    public void test_k2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID
        Brand barcodes1 = new Brand();
        barcodes1.setID(1000000);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1, brandSQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue(false, "deleteAsync超时！");
        }
        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_k3_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null（不会出现异常）
        Brand barcodes2 = DataInput.getBrand();
        barcodes2.setID(null);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes2, brandSQLiteEvent);

        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync错误码不正确！");
    }

    @Test
    public void test_k4_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Brand brand = DataInput.getBrand();
        brand.setID(-1);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);

        Assert.assertTrue(brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "deleteAsync错误码不正确！");
    }

    /**
     * 查询本地BRAND表的总条数
     */
    @Test
    public void test_retrieveNBrand() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = brandPresenter.retrieveCount();
        System.out.println("BRAND表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}
