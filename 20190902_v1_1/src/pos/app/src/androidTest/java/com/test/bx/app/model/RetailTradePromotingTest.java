package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class RetailTradePromotingTest extends BaseAndroidTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkCreate() {
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        //
        assertEquals(retailTradePromoting.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromoting.FIELD_ERROR_tradeID);
        //
        retailTradePromoting.setTradeID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(retailTradePromoting.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromoting.FIELD_ERROR_tradeID);
        //
        retailTradePromoting.setTradeID(1);
        assertEquals(retailTradePromoting.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_checkRetrieve1() {
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        //
        assertEquals(retailTradePromoting.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        retailTradePromoting.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(retailTradePromoting.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        retailTradePromoting.setID(1l);
        assertEquals(retailTradePromoting.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

}
