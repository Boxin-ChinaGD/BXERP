package wpos.model;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.BaseModelTest;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

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
        bcg.setID(-99);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        bcg.setID(0);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        bcg.setID((BXConfigGeneral.MAX_BxConfigGeneralID + 1));
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), BXConfigGeneral.FIELD_ERROR_BxConfigGeneralID);
        //
        bcg.setID(1);
        assertEquals(bcg.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
}
