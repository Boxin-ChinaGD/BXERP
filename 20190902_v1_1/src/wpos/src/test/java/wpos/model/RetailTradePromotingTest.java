package wpos.model;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

public class RetailTradePromotingTest extends BaseTestCase {

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
        retailTradePromoting.setID(1);
        assertEquals(retailTradePromoting.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

}
