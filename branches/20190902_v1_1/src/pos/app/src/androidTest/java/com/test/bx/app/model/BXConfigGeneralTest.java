package com.test.bx.app.model;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.BXConfigGeneral;
import com.bx.erp.utils.Shared;
import com.test.bx.app.BaseModelTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BXConfigGeneralTest extends BaseModelTest {

    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkRetrieve1() {
        Shared.printTestMethodStartInfo();
        //
        BXConfigGeneral bcg = new BXConfigGeneral();
        bcg.setID(-99L);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        bcg.setID(0L);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        bcg.setID(Long.valueOf(BXConfigGeneral.MAX_BxConfigGeneralID + 1));
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        //
        bcg.setID(1L);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
}
