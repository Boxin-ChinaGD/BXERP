package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.Coupon;
import wpos.utils.Shared;

import java.util.Date;
import java.util.List;

public class CouponJUnit extends BaseHttpTestCase {
    protected static CouponSQLiteBO couponSQLiteBO;
    protected static CouponSQLiteEvent couponSQLiteEvent;
    protected static CouponHttpEvent couponHttpEvent;
    protected static CouponHttpBO couponHttpBO;

    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;

    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_CouponJUnit = 10000;

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();

        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(Event_ID_CouponJUnit);
        }

        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(Event_ID_CouponJUnit);
        }

        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(couponSQLiteEvent, couponHttpEvent);
        }

        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(couponSQLiteEvent, couponHttpEvent);
            couponSQLiteBO.setCouponPresenter(couponPresenter);
        }

        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_CouponJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_CouponJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        logoutHttpEvent.setId(Event_ID_CouponJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    @AfterClass
    public void tearDown() {
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
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == Event_ID_CouponJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == Event_ID_CouponJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_CouponJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_CouponJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_CouponJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Test
    public void test_a_RetrieveNCEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO), "????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        // ??????????????????POS
        Coupon queryCoupon = new Coupon();
        queryCoupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        queryCoupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if (!couponHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, queryCoupon)) {
            Assert.assertTrue(false, "????????????!");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (couponSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && couponHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (couponSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "?????????????????????Coupon??????!");
        }
        List<Coupon> list = (List<Coupon>) couponSQLiteBO.getSqLiteEvent().getListMasterTable();
        System.out.println("???????????????????????????" + list);
        // ??????????????????????????????????????????????????????
        Date now = new Date();
        for (Coupon coupon : list) {
            Assert.assertTrue(now.before(coupon.getEndDateTime()), "??????????????????????????????");
        }
        // ????????????????????????testunit.xml???????????????nbr?????????????????????
        logOut();
    }
}
