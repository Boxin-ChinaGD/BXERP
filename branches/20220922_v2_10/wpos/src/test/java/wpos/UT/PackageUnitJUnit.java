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
            packageUnit.setName("千克");
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PackageUnitJunit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"本地创建多个PackageUnit失败！");
        //
        for (int i = 0; i < pList.size(); i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList.get(i));
            Assert.assertTrue(packageUnit != null,"插入第" + i + "条数据失败！");
            packageUnit.setIgnoreIDInComparision(true);
            Assert.assertTrue(packageUnit.compareTo(packageUnitList.get(i)) == 0, "插入的第" + i + "条数据与查询出来的不一致！");
        }

        //异常Case1：主键冲突
        System.out.println("--------------------------------test_a_createNSync异常Case1：主键冲突-------------------------------");
        packageUnitPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pList);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createSync主键冲突异常！");
    }

    @Test
    public void test_b_deleteNSync() {
        Shared.printTestMethodStartInfo();

        //正常Case
        System.out.println("--------------------------------test_b_deleteNSync正常Case-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"删除本地所有PackageUnit失败！");
        for (int i = 0; i < 5; i++) {
            PackageUnit packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnitList.get(i));
            Assert.assertTrue(packageUnit == null, "插入第" + i + "条数据失败！");
        }

        //正常Case：数据表为空时，删除所有数据，不会有异常
        System.out.println("--------------------------------test_b_deleteNSync异常Case：数据表为空时，删除所有数据-------------------------------");
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"当数据表没有数据时，删除所有数据不会有异常！");
    }

    @Test
    public void test_c_refreshByServerDataAsyncC() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //正常Case：call PackageUnitAction的RN，返回所有的数据，在本地SQLite进行同步
        System.out.println("--------------------------------test_c_refreshByServerDataAsyncC正常Case-------------------------------");

        //测试前删除所有，防止干扰
        packageUnitPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(packageUnitPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"deleteNObjectSync错误码不正确：" + packageUnitPresenter.getLastErrorCode());
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == 0,"数据表中不应该存在数据!");

        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            Assert.assertTrue(false,"call PackageUnit的RN失败");
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
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() != 0,"数据表中不应该存在数据!");
        Assert.assertTrue(packageUnitPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null).size() == packageUnits.size(),"数据表中数据的数量应该为服务器返回的一样");
    }
}
