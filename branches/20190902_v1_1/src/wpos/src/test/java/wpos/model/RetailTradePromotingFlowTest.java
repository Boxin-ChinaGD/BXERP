package wpos.model;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

public class RetailTradePromotingFlowTest extends BaseTestCase {

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
        Shared.printTestMethodStartInfo();

        RetailTradePromotingFlow rPromotingFlow = new RetailTradePromotingFlow();

        // 检查retailTradePromotingID
        rPromotingFlow.setPromotionID(1);
        rPromotingFlow.setProcessFlow("checkCreateTest");
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
        //
        rPromotingFlow.setRetailTradePromotingID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
        //
        rPromotingFlow.setRetailTradePromotingID(1);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查PromotionID
        rPromotingFlow.setPromotionID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_promotionID);
        //
        rPromotingFlow.setPromotionID(0);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_promotionID);
        //
        rPromotingFlow.setPromotionID(1);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查ProcessFlow
        rPromotingFlow.setProcessFlow(null);
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_processFlow);
        //
        rPromotingFlow.setProcessFlow("");
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_processFlow);
        //
        rPromotingFlow.setProcessFlow("checkCreateTest");
        assertEquals(rPromotingFlow.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_checkRetrieveN() {
        RetailTradePromotingFlow rPromotingFlow = new RetailTradePromotingFlow();

        // 检查retailTradePromotingID
        rPromotingFlow.setPromotionID(1);
        rPromotingFlow.setProcessFlow("checkCreateTest");
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
        //
        rPromotingFlow.setRetailTradePromotingID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), RetailTradePromotingFlow.FIELD_ERROR_retailTradePromotingID);
        //
        rPromotingFlow.setRetailTradePromotingID(1);
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查page indx
        rPromotingFlow.setPageIndex("-1");
        rPromotingFlow.setPageSize(Shared.PAGE_Size);
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        rPromotingFlow.setPageIndex(Shared.PAGE_Index);
        rPromotingFlow.setPageSize("-1");
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        rPromotingFlow.setPageIndex(Shared.PAGE_Index);
        rPromotingFlow.setPageSize(Shared.PAGE_Size);
        assertEquals(rPromotingFlow.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

}
