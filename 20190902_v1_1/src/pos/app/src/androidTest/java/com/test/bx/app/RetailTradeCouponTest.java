package com.test.bx.app;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RetailTradeCouponTest extends BaseModelTest{
    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testCheckCreate() throws Exception {
        Shared.printTestMethodStartInfo();

        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();

        String errorMsg = "";
        // case 1：零售单ID小于0
        retailTradeCoupon.setRetailTradeID(-1);
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertTrue(errorMsg, RetailTradeCoupon.FIELD_ERROR_RETAILTRADEID.equals(errorMsg));
        retailTradeCoupon.setRetailTradeID(1);

        //  case2: 券ID小于0
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertTrue(errorMsg, RetailTradeCoupon.FIELD_ERROR_COUPONCODEID.equals(errorMsg));
        retailTradeCoupon.setCouponCodeID(BaseSQLiteBO.INVALID_CASE_ID);

        //case5: 正常的数据
        retailTradeCoupon.setCouponCodeID(1);
        errorMsg = retailTradeCoupon.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertTrue(errorMsg, "".equals(errorMsg));
    }
}
