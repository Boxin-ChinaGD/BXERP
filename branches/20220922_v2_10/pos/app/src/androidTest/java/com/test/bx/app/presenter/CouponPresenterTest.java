package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.CouponPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class CouponPresenterTest extends BaseAndroidTestCase {
    private static final int Event_ID_CouponPresenterTest = 10000;
    private static CouponPresenter couponPresenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        couponPresenter = GlobalController.getInstance().getCouponPresenter();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        EventBus.getDefault().unregister(this);
    }

    public static class DataInput {
        private static Coupon couponInput = null;

        public static final Coupon getCoupon() throws CloneNotSupportedException {
            couponInput = new Coupon();
            couponInput.setStatus(Coupon.EnumCouponStatus.ECS_Normal.getIndex());
            couponInput.setType(Coupon.EnumCouponCardType.ECCT_CASH.getIndex());
            couponInput.setBonus(0);
            couponInput.setLeastAmount(10.000000d);
            couponInput.setReduceAmount(5.000000d);
            couponInput.setDiscount(1.000000d);
            couponInput.setTitle("现金券满10减5");
            couponInput.setColor("xxxxxxxxxx");
            couponInput.setDescription("1、xxxxxxxxxxxxxxxxxxxx");
            couponInput.setPersonalLimit(1);
            couponInput.setType(1);
            couponInput.setBeginTime("00:00:00");
            couponInput.setEndTime("23:59:59");
            couponInput.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
            couponInput.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
            couponInput.setWeekDayAvailable(0);
            couponInput.setQuantity(1000);
            couponInput.setRemainingQuantity(1000);
            couponInput.setScope(0);

            return (Coupon) couponInput.clone();
        }
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Coupon couponD1 = new Coupon();
        couponD1.setID(19L);
        Coupon couponD2 = new Coupon();
        couponD2.setID(17L);
        couponPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, couponD1);
//        couponPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, couponD2);

        Coupon coupon = DataInput.getCoupon();
        Coupon couponCreate = (Coupon) couponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon);

        Assert.assertTrue("创建失败",couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        coupon.setIgnoreIDInComparision(true);
        Assert.assertTrue("和DB数据不一致", coupon.compareTo(couponCreate) == 0);
        //
        Coupon coupon2 = DataInput.getCoupon();
//        coupon2.setID(400L);
        Coupon couponCreate2 = (Coupon) couponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon2);

        Assert.assertTrue("创建失败",couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        coupon2.setIgnoreIDInComparision(true);
        Assert.assertTrue("和DB数据不一致", coupon2.compareTo(couponCreate2) == 0);
        // 验证是否自增
        System.out.println("++++++ couponCreate.getID()：" + couponCreate.getID() + ", couponCreate2.getID()：" + couponCreate2.getID());
        Assert.assertTrue("ID不是自增",couponCreate.getID() + 1 == couponCreate2.getID());


    }

    @Test
    public void test_b_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Coupon coupon = DataInput.getCoupon();
        Coupon couponCreate = (Coupon) couponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon);

        Assert.assertTrue("创建失败", couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        coupon.setIgnoreIDInComparision(true);
        Assert.assertTrue("和DB数据不一致",coupon.compareTo(couponCreate) == 0);

        Coupon queryCoupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, couponCreate);
        Assert.assertTrue("查找失败",couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("创建的数据跟查出来的数据不一致",queryCoupon.compareTo(couponCreate) == 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == Event_ID_CouponPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}
