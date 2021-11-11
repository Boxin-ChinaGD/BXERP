package com.test.bx.app.presenter;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Brand;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.BrandPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class BrandPresenterTest extends BaseAndroidTestCase {

    BrandPresenter brandPresenter;
    BrandSQLiteEvent brandSQLiteEvent;
    private static BrandHttpBO httpBO = null;

    private List<BaseModel> bmList = new ArrayList<BaseModel>();

    private static final int Event_ID_BrandPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        brandPresenter = GlobalController.getInstance().getBrandPresenter();
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(Event_ID_BrandPresenterTest);
        }
//        httpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), );

    }

    @Override
    public void tearDown() throws Exception {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == Event_ID_BrandPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
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
            List<Brand> brandList = new ArrayList<>();
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
        Assert.assertTrue("createNObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue("createNObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //异常case：重复插入（插入失败）
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue("createNObjectSync失败！", brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        List<Brand> brandList = (List<Brand>) DataInput.getBrandList();
        brandList.get(0).setName(null);
        brandPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brandList);
        Assert.assertTrue("createNObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_b1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常case
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //异常case：重复插入（插入失败）
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //异常case：插入的非空字段为null（插入失败）
        Brand brand = DataInput.getBrand();
        brand.setName(null);
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常Case1
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找的对象与插入的对象不一致！", b.compareTo(brand) == 0);
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //正常Case2
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找的对象与插入的对象不一致！", b.compareTo(brand) == 0);
        //正常
        Brand brand2 = new Brand();
        brand2.setID(brand.getID());
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand2);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查找失败！", b != null);
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为null（差找不到对应的数据）
        Brand brand = new Brand();
        brand.setID(null);
        brand = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查找失败！", brand == null);
    }

    @Test
    public void test_c4_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：根据ID查找，ID为负数（差找不到对应的数据）
        Brand brand = new Brand();
        brand.setID(-1l);
        brand = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查找失败！", brand == null);
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<Brand> barcodesList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null或size等于0", barcodesList != null && barcodesList.size() > 0);
    }

    @Test
    public void test_d2_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brand.setSql("where F_Name = ?");
        brand.setConditions(new String[]{brand.getName()});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null或size等于0", brandList != null && brandList.size() > 0);
    }

    @Test
    public void test_d3_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brand.setSql("where F_ID = ?");
        brand.setConditions(new String[]{String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null或size等于0", brandList != null && brandList.size() > 0);
    }

    @Test
    public void test_d4_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brand.setSql("where F_ID = ? and F_Name = ?");
        brand.setConditions(new String[]{String.valueOf(brand.getID()), brand.getName()});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null或size等于0", brandList != null && brandList.size() > 0);
    }

    @Test
    public void test_d5_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brand.setSql("where F_Name = ?  and  F_ID = ?");
        brand.setConditions(new String[]{brand.getName(), String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询的list不应该为null或size等于0", brandList != null && brandList.size() > 0);
    }

    @Test
    public void test_d6_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        List<Brand> brandList = new ArrayList<>();
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brand.setSql("where F_Name = ?  and  F_ID = ?");
        brand.setConditions(new String[]{String.valueOf(brand.getID())});
        brand.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, brand);
        Assert.assertTrue("retrieveNSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查询的list不应该为null或size等于0", brandList == null);
    }

    //...

    @Test
    public void test_e1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand brand = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b != null);
        //
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("deleteObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b == null);
    }

    @Test
    public void test_e2_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        Brand brand = new Brand();
        brand.setID(100000000l);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("deleteObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e3_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为null
        Brand brand = new Brand();
        brand.setID(null);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("deleteObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_e4_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Brand brand = new Brand();
        brand.setID(-1L);
        brandPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, brand);
        Assert.assertTrue("deleteObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_f_deleteNSync() {
        Shared.printTestMethodStartInfo();

        List<Brand> brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list必须有值", brandList != null && brandList.size() > 0);
        //
        brandPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        brandList = (List<Brand>) brandPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync查询出来的list必须无值", brandList == null || brandList.size() == 0);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsync超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsync失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_g2_refreshByServerDataAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case：重复插入(查找到重复后会删除再进行插入)
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        brandPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsync超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsync失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsync超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsync失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsyncC超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsyncC失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常Case：重复插入(查找到重复后会删除再进行插入)
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        brandPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsyncC超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsyncC失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("refreshByServerDataAsyncC超时！", false);
        }
        Assert.assertTrue("refreshByServerDataAsyncC失败，错误码不正确！", brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_i1_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> brandList = (List<BaseModel>) DataInput.getBrandList();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brandList, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_i2_createNAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        List<BaseModel> barcodesList = (List<BaseModel>) DataInput.getBrandList();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case：重复插入（插入失败）
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateNAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        brandPresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodesList, brandSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
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

        Assert.assertTrue("createNAsync错误码不正确！当前错误码：" + brandSQLiteEvent.getLastErrorCode(), brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
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
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j2_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        Brand brand = DataInput.getBrand();
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandSQLiteEvent.setHttpBO(null);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //异常case：重复插入（插入失败）
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandSQLiteEvent.setHttpBO(null);
        brandPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createAsync超时！", false);
        }
        Assert.assertTrue("createAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
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
        Assert.assertTrue("createAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_k1_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();

        //正常case
        Brand barcodes = DataInput.getBrand();
        brandPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue("createObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Brand b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b != null);
        //
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        b = (Brand) brandPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, barcodes);
        Assert.assertTrue("retrieve1ObjectSync失败！", brandPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("查询失败！", b == null);
    }

    @Test
    public void test_k2_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除不存在的ID（不会出现异常）
        Brand barcodes1 = new Brand();
        barcodes1.setID(1000000l);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, barcodes1, brandSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (brandSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("deleteAsync超时！", false);
        }
        Assert.assertTrue("deleteAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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

        Assert.assertTrue("deleteAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_k4_deleteAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        //异常case：删除的对象ID为负数
        Brand brand = DataInput.getBrand();
        brand.setID(-1l);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_DeleteAsync);
        brandSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        brandPresenter.deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, brand, brandSQLiteEvent);

        Assert.assertTrue("deleteAsync错误码不正确！", brandSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    /**
     * 查询本地BRAND表的总条数
     */
    @Test
    public void test_retrieveNBrand() throws Exception {
        Shared.printTestMethodStartInfo();

        BrandPresenter brandPresenter = GlobalController.getInstance().getBrandPresenter();
        Integer total = brandPresenter.retrieveCount();
        System.out.println("BRAND表总条数：" + total);
        Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
