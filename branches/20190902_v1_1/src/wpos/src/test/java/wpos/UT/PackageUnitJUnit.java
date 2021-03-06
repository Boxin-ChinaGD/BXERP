package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.PackageUnitSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.PackageUnit;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PackageUnitJUnit extends BaseHttpTestCase {

    private static List<PackageUnit> packageUnitList = new ArrayList<PackageUnit>();

//    private static PackageUnitPresenter packageUnitPresenter = null;
    @Resource
    private PackageUnitSQLiteEvent sqLiteEvent;
    @Resource
    private PackageUnitHttpEvent httpEvent;
    @Resource
    private PackageUnitSQLiteBO packageUnitSQLiteBO;
    @Resource
    private PackageUnitHttpBO packageUnitHttpBO;
    //
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;
    private static final int Event_ID_PackageUnitJunit = 10000;

    @BeforeClass
    public void setUp(){
        super.setUp();
        sqLiteEvent.setId(Event_ID_PackageUnitJunit);
        httpEvent.setId(Event_ID_PackageUnitJunit);
        packageUnitSQLiteBO.setHttpEvent(httpEvent);
        packageUnitSQLiteBO.setSqLiteEvent(sqLiteEvent);
        packageUnitHttpBO.setHttpEvent(httpEvent);
        packageUnitHttpBO.setSqLiteEvent(sqLiteEvent);
        sqLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        sqLiteEvent.setHttpBO(packageUnitHttpBO);
        httpEvent.setSqliteBO(packageUnitSQLiteBO);
        httpEvent.setHttpBO(packageUnitHttpBO);
        //
        posLoginHttpEvent.setId(Event_ID_PackageUnitJunit);
        staffLoginHttpEvent.setId(Event_ID_PackageUnitJunit);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(Event_ID_PackageUnitJunit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    public static class DataInput {
        public static PackageUnit getPackageUnit() {
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setName("??????");
            packageUnit.setCreateDatetime(new Date());
            packageUnit.setUpdateDatetime(new Date());

            return packageUnit;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Test
    public void test_a_createNSync() {
        Shared.printTestMethodStartInfo();

        //??????Case
        System.out.println("--------------------------------test_a_createNSync??????Case-------------------------------");
        for (int i = 0; i < 5; i++) {
            packageUnitList.add(DataInput.getPackageUnit());
        }
        List<PackageUnit> pList = (List<PackageUnit>) packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????PackageUnit?????????");
        //
        for (int i = 0; i < pList.size(); i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList.get(i));
            Assert.assertTrue(packageUnit != null,"?????????" + i + "??????????????????");
            packageUnit.setIgnoreIDInComparision(true);
            Assert.assertTrue(packageUnit.compareTo(packageUnitList.get(i)) == 0, "????????????" + i + "???????????????????????????????????????");
        }

        //??????Case1???????????????
        System.out.println("--------------------------------test_a_createNSync??????Case1???????????????-------------------------------");
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createSync?????????????????????");
    }

    @Test
    public void test_b_deleteNSync() {
        Shared.printTestMethodStartInfo();

        //??????Case
        System.out.println("--------------------------------test_b_deleteNSync??????Case-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????PackageUnit?????????");
        for (int i = 0; i < 5; i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList.get(i));
            Assert.assertTrue(packageUnit == null, "?????????" + i + "??????????????????");
        }

        //??????Case????????????????????????????????????????????????????????????
        System.out.println("--------------------------------test_b_deleteNSync??????Case??????????????????????????????????????????-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????????????????????????????????????????????????????");
    }

    @Test
    public void test_c_refreshByServerDataAsyncC() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //??????Case???call PackageUnitAction???RN????????????????????????????????????SQLite????????????
        System.out.println("--------------------------------test_c_refreshByServerDataAsyncC??????Case-------------------------------");

        //????????????????????????????????????
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"deleteNObjectSync?????????????????????" + packageUnitPresenter.getLastErrorCode());
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == 0,"?????????????????????????????????!");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            Assert.assertTrue(false,"call PackageUnit???RN??????");
        }
        long lTimeOut = 50;
        while (packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"");
        }

        List<PackageUnit> packageUnits = (List<PackageUnit>) packageUnitHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() != 0,"?????????????????????????????????!");
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == packageUnits.size(),"????????????????????????????????????????????????????????????");
    }
}
