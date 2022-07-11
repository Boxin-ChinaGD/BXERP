package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PackageUnitHttpBO;
import com.bx.erp.bo.PackageUnitSQLiteBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.presenter.PackageUnitPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class
PackageUnitJUnit extends BaseHttpAndroidTestCase {
    private static List<PackageUnit> packageUnitList = new ArrayList<PackageUnit>();

    private static PackageUnitPresenter packageUnitPresenter = null;
    private static PackageUnitSQLiteEvent sqLiteEvent = null;
    private static PackageUnitHttpEvent httpEvent = null;
    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
    private static PackageUnitHttpBO packageUnitHttpBO = null;
    //
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private static final int Event_ID_PackageUnitJunit = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (packageUnitPresenter == null) {
            packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        }
        if (sqLiteEvent == null) {
            sqLiteEvent = new PackageUnitSQLiteEvent();
            sqLiteEvent.setId(Event_ID_PackageUnitJunit);
        }
        if (httpEvent == null) {
            httpEvent = new PackageUnitHttpEvent();
            httpEvent.setId(Event_ID_PackageUnitJunit);
        }
        if (packageUnitSQLiteBO == null) {
            packageUnitSQLiteBO = new PackageUnitSQLiteBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        if (packageUnitHttpBO == null) {
            packageUnitHttpBO = new PackageUnitHttpBO(GlobalController.getInstance().getContext(), sqLiteEvent, httpEvent);
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_PackageUnitJunit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_PackageUnitJunit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        //
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        httpEvent.setSqliteBO(packageUnitSQLiteBO);
        httpEvent.setHttpBO(packageUnitHttpBO);
        sqLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        sqLiteEvent.setHttpBO(packageUnitHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_PackageUnitJunit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);


    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static class DataInput {
        public static PackageUnit getPackageUnit() {
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setName("千克");
            packageUnit.setCreateDatetime(new Date());
            packageUnit.setUpdateDatetime(new Date());

            return packageUnit;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_a_createNSync() {
        Shared.printTestMethodStartInfo();

        //正常Case
        System.out.println("--------------------------------test_a_createNSync正常Case-------------------------------");
        for (int i = 0; i < 5; i++) {
            packageUnitList.add(DataInput.getPackageUnit());
        }
        List<PackageUnit> pList = (List<PackageUnit>) packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList);
        Assert.assertTrue("本地创建多个PackageUnit失败！", packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < pList.size(); i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList.get(i));
            Assert.assertTrue("插入第" + i + "条数据失败！", packageUnit != null);
            Assert.assertTrue("插入的第" + i + "条数据与查询出来的不一致！", packageUnit.compareTo(packageUnitList.get(i)) == 0);
        }

        //异常Case1：主键冲突
        System.out.println("--------------------------------test_a_createNSync异常Case1：主键冲突-------------------------------");
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList);
        Assert.assertTrue("createSync主键冲突异常！", packageUnitPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_deleteNSync() {
        Shared.printTestMethodStartInfo();

        //正常Case
        System.out.println("--------------------------------test_b_deleteNSync正常Case-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("删除本地所有PackageUnit失败！", packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (int i = 0; i < 5; i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList.get(i));
            Assert.assertTrue("插入第" + i + "条数据失败！", packageUnit == null);
        }

        //正常Case：数据表为空时，删除所有数据，不会有异常
        System.out.println("--------------------------------test_b_deleteNSync异常Case：数据表为空时，删除所有数据-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("当数据表没有数据时，删除所有数据不会有异常！", packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_c_refreshByServerDataAsyncC() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //正常Case：call PackageUnitAction的RN，返回所有的数据，在本地SQLite进行同步
        System.out.println("--------------------------------test_c_refreshByServerDataAsyncC正常Case-------------------------------");

        //测试前删除所有，防止干扰
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误码不正确：" + packageUnitPresenter.getLastErrorCode(), packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("数据表中不应该存在数据!", packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == 0);

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            Assert.assertTrue("call PackageUnit的RN失败", false);
        }
        long lTimeOut = 50;
        while (packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (packageUnitSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("", false);
        }

        List<PackageUnit> packageUnits = (List<PackageUnit>) packageUnitHttpBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("数据表中不应该存在数据!", packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() != 0);
        Assert.assertTrue("数据表中数据的数量应该为服务器返回的一样", packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == packageUnits.size());
    }
}
