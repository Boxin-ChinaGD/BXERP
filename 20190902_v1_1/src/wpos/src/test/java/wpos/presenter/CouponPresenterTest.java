package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.bo.BaseSQLiteBO;
import wpos.model.Coupon;
import wpos.model.ErrorInfo;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.util.Date;

public class CouponPresenterTest extends BasePresenterTest{
    private static final int Event_ID_CouponPresenterTest = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @BeforeClass
    public void setUp() {
        super.setUp();
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
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

    @Test
    public void test_a_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Coupon coupon = DataInput.getCoupon();
        Coupon couponCreate = (Coupon) couponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon);

        Assert.assertTrue(couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败");
        coupon.setIgnoreIDInComparision(true);
        Assert.assertTrue(coupon.compareTo(couponCreate) == 0, "和DB数据不一致");
    }

    @Test
    public void test_b_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        Coupon coupon = DataInput.getCoupon();
        Coupon couponCreate = (Coupon) couponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon);

        Assert.assertTrue(couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败");
        coupon.setIgnoreIDInComparision(true);
        Assert.assertTrue(coupon.compareTo(couponCreate) == 0, "和DB数据不一致");

        Coupon queryCoupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, couponCreate);
        Assert.assertTrue(couponPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "查找失败");
        Assert.assertTrue(queryCoupon.compareTo(couponCreate) == 0, "创建的数据跟查出来的数据不一致");
    }
}
