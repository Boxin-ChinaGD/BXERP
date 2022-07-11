package com.test.bx.app;

import android.test.AndroidTestCase;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RetailtradeCommodityTest extends AndroidTestCase {
    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testCreate() {
        Shared.printTestMethodStartInfo();

        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        retailTradeCommodity.setCommodityID(1);
        retailTradeCommodity.setNO(10);
        retailTradeCommodity.setPriceOriginal(222.6);
        retailTradeCommodity.setDiscount(0.9);
        retailTradeCommodity.setPriceReturn(10);
        retailTradeCommodity.setBarcodeID(1);
        String error = "";
        //
        retailTradeCommodity.setTradeID(0L);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        retailTradeCommodity.setTradeID(-66L);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        retailTradeCommodity.setTradeID(1L);
        //
        retailTradeCommodity.setCommodityID(0);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_CommodityID);
        retailTradeCommodity.setCommodityID(-66);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_CommodityID);
        retailTradeCommodity.setCommodityID(1);
        //
        retailTradeCommodity.setBarcodeID(0);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_BarcodeID);
        retailTradeCommodity.setBarcodeID(-66);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_BarcodeID);
        retailTradeCommodity.setBarcodeID(1);
        //
        retailTradeCommodity.setNO(-66);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
        retailTradeCommodity.setNO(0);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
        retailTradeCommodity.setNO(10000);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_NO);
        retailTradeCommodity.setNO(1);
        //
        retailTradeCommodity.setPriceOriginal(-1d);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_Price);
        retailTradeCommodity.setPriceOriginal(-66d);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_Price);
        retailTradeCommodity.setPriceOriginal(1.00d);
        //
        retailTradeCommodity.setPriceReturn(-0.01d);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_PriceReturn);
        retailTradeCommodity.setPriceReturn(-66d);
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_PriceReturn);
        retailTradeCommodity.setPriceReturn(0.00d);
        //
        error = retailTradeCommodity.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testRetrieve1() {
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        String error = "";
        //
        retailTradeCommodity.setTradeID(0L);
        error = retailTradeCommodity.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        //
        retailTradeCommodity.setTradeID(-2L);
        error = retailTradeCommodity.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        //
        retailTradeCommodity.setTradeID(10L);
        error = retailTradeCommodity.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testDelete() {
        RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
        String error = "";
        //
        retailTradeCommodity.setTradeID(0L);
        error = retailTradeCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        //
        retailTradeCommodity.setTradeID(-2L);
        error = retailTradeCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RetailTradeCommodity.FIELD_ERROR_TradeID);
        //
        retailTradeCommodity.setTradeID(10L);
        error = retailTradeCommodity.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }
}
