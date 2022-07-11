package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.presenter.PackageUnitPresenter;
import com.bx.erp.utils.Shared;

import junit.framework.Assert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class PackageUnitPresenterTest extends BaseAndroidTestCase {

    PackageUnitPresenter packageUnitPresenter;
    PackageUnitSQLiteEvent packageUnitSQLiteEvent;

    private static final int Event_ID_PackageUnitPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        if(packageUnitSQLiteEvent == null){
            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
            packageUnitSQLiteEvent.setId(Event_ID_PackageUnitPresenterTest);
        }
    }

    @Override
    public void tearDown() throws Exception {
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

        public static final List<PackageUnit> getPackageUnitList() throws Exception{
            List<PackageUnit> packageUnitList = new ArrayList<>();

            int size = new Random().nextInt(10) + 1;
            for(int i = 0; i < size; i++){
                packageUnitList.add(getPackageUnit());
            }

            return packageUnitList;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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
        caseLog("case1: 正常创建");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue("createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_a2_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2: 字段验证不通过：名称超长");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitList.get(packageUnitList.size() - 1).setName("123456789");
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue("createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_a3_createNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3: 必填字段为null");

        List<PackageUnit> packageUnitList = DataInput.getPackageUnitList();
        packageUnitList.get(packageUnitList.size() - 1).setName(null);
        try {
            packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        } catch (Exception e){
            Assert.assertTrue("createNObjectSync错误，异常类型不正确：" + e.getMessage(), e.getMessage().equals("Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference"));
        }
    }

    @Test
    public void test_b1_deleteNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常删除");

        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c1_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常查询");

        PackageUnit packageUnit = createPackageUnit(DataInput.getPackageUnit());
        PackageUnit p = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue("retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync错误，查询的对象与结构不相等", packageUnit.getID().equals(p.getID()));
    }

    @Test
    public void test_c2_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2: 查询不存在的ID");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(999999l);
        PackageUnit p = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue("retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync错误，查询的对象不为null", p == null);
    }

    @Test
    public void test_c3_retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: ID为-1");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setID(-1l);
        packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
        Assert.assertTrue("retrieve1ObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_d1_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常RN");

        createPackageUnit(DataInput.getPackageUnit());
        //
        List<PackageUnit> packageUnitList = (List<PackageUnit>) packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync错误，RN的对象size=" + packageUnitList.size(), packageUnitList.size() > 0);
    }

    @Test
    public void test_e1_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while(packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut --> 0){
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode(), packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

    }

    @Test
    public void test_e2_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while(packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut --> 0){
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode(), packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

    }

    @Test
    public void test_e30_refreshByServerDataAsyncC() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常创建");

        packageUnitSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        packageUnitPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, DataInput.getPackageUnitList(), packageUnitSQLiteEvent);
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while(packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                packageUnitSQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut --> 0){
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", packageUnitSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + packageUnitSQLiteEvent.getLastErrorCode(), packageUnitSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

    }

    private PackageUnit createPackageUnit(PackageUnit packageUnit){
        List<PackageUnit> packageUnitList = new ArrayList<>();
        packageUnitList.add(packageUnit);
        List<PackageUnit> packageUnitListReturn = (List<PackageUnit>) packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue("createNObjectSync错误，错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        return packageUnitListReturn.get(0);
    }

    /**
     * 查询本地PACKAGE_UNIT表的总条数
     */
    @Test
    public void test_retrieveNPackageUnit() throws Exception {
        Shared.printTestMethodStartInfo();

        PackageUnitPresenter packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        Integer total = packageUnitPresenter.retrieveCount();
        System.out.println("PACKAGE_UNIT表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}

