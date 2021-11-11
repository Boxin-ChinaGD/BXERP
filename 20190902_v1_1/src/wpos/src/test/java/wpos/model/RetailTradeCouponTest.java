package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.BaseModelTest;
import wpos.bo.BaseSQLiteBO;
import wpos.model.RetailTradeCoupon;
import wpos.utils.Shared;

public class RetailTradeCouponTest extends BaseModelTest {
    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testCheckCreate() {
        Shared.printTestMethodStartInfo();

        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();

        String errorMsg = "";
        // case 1：零售单ID小于0
        retailTradeCoupon.setRetailTradeID(-1);
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(errorMsg, RetailTradeCoupon.FIELD_ERROR_RETAILTRADEID, errorMsg);
        retailTradeCoupon.setRetailTradeID(1);

        //  case2: 券ID小于0
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(errorMsg, RetailTradeCoupon.FIELD_ERROR_COUPONCODEID, errorMsg);
        retailTradeCoupon.setCouponCodeID(BaseSQLiteBO.INVALID_CASE_ID);

        //case5: 正常的数据
        retailTradeCoupon.setCouponCodeID(1);
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(errorMsg, "", errorMsg);
    }
}
