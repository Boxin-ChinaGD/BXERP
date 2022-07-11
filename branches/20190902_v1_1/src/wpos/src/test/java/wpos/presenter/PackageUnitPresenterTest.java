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
import wpos.event.UI.PackageUnitSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.PackageUnit;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PackageUnitPresenterTest extends BasePresenterTest{
    private static final int Event_ID_PackageUnitPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();
        EventBus.getDefault().register(this);
        if (packageUnitSQLiteEvent != null) {
            packageUnitSQLiteEvent.setId(Event_ID_PackageUnitPresenterTest);
        }
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
        EventBus.getDefault().unregister(this);
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
        private static PackageUnit packageUnitInput = null;

        public static final PackageUnit getPackageUnit() throws ParseException, CloneNotSupportedException, InterruptedException {
            packageUnitInput = new PackageUnit();
            packageUnitInput.setName("个");
            packageUnitInput.setCreateDatetime(new Date());
            packageUnitInput.setUpdateDatetime(new Date());

            return (PackageUnit) packageUnitInput.clone();
        }

        public static final List<PackageUnit> getPackageUnitList() throws Exception {
            List<PackageUnit> packageUnitList = new ArrayList<PackageUnit>();

            int size = new Random().nextInt(10) + 1;
            for (int i = 0; i < size; i++) {
                packageUnitList.add(getPackageUnit());
            }

            return packageUnitList;
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_PackageUnitPresenterTest){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Test
    public void test_a1_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常创建");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2: 字段验证不通过：名称超长");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitList.get(packageUnitList.size() - 1).setName("123456789");
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case3: 必填字段为null");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitList.get(packageUnitList.size() - 1).setName(null);
        try {
            packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(e.getMessage() == null, "createNObjectSync错误，异常类型不正确：" + e.getMessage());
        }
    }

    @Test
    public void test_b1_deleteNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常删除");

        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常查询");

        PackageUnit packageUnit = createPackageUnit(DataInput.getPackageUnit());
        PackageUnit p = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
        Assert.assertTrue(packageUnit.getID().equals(p.getID()), "retrieve1ObjectSync错误，查询的对象与结构不相等");
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2: 查询不存在的ID");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(999999);
        PackageUnit p = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
        Assert.assertTrue(p == null, "retrieve1ObjectSync错误，查询的对象不为null");
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: ID为-1");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(-1);
        packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常RN");

        createPackageUnit(DataInput.getPackageUnit());
        //
        List<PackageUnit> packageUnitList = (List<PackageUnit>) packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());
        Assert.assertTrue(packageUnitList.size() > 0, "retrieveNObjectSync错误，RN的对象size=" + packageUnitList.size());
    }

    @Test
    public void test_e1_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync失败，原因：超时");
        Assert.assertTrue(packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode());

    }

    @Test
    public void test_e2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync失败，原因：超时");
        Assert.assertTrue(packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode());

    }

    @Test
    public void test_e30_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync失败，原因：超时");
        Assert.assertTrue(packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode());

    }

    private PackageUnit createPackageUnit(PackageUnit packageUnit) {
        List<PackageUnit> packageUnitList = new ArrayList<PackageUnit>();
        packageUnitList.add(packageUnit);
        List<PackageUnit> packageUnitListReturn = (List<PackageUnit>) packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode());

        return packageUnitListReturn.get(0);
    }

    /**
     * 查询本地PACKAGE_UNIT表的总条数
     */
    @Test
    public void test_retrieveNPackageUnit() throws Exception {
        Shared.printTestMethodStartInfo();

//        PackageUnitPresenter packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        Integer total = packageUnitPresenter.retrieveCount();
        System.out.println("PACKAGE_UNIT表总条数：" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "查询异常！");
    }

}

