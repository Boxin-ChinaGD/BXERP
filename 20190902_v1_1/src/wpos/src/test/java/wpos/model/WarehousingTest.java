package wpos.model;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import static org.testng.AssertJUnit.assertEquals;

public class WarehousingTest extends BaseTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_Update() {
        Warehousing w = new Warehousing();
        w.setID(1);
        w.setApproverID(1);
        w.setWarehouseID(1);
        w.setProviderID(1L);

        // 检验ID
        w.setID(0);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(1);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查warehouseID
        w.setWarehouseID(0);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
        //
        w.setWarehouseID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
        //
        w.setWarehouseID(1);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // BaseHttpBO.CASE_Warehousing_Approve
        w.setID(0);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(1);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), "");
        // ApproverID检验
        w.setApproverID(0);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), Warehousing.FIELD_ERROR_approverID);
        //
        w.setApproverID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), Warehousing.FIELD_ERROR_approverID);
        //
        w.setApproverID(1);
        assertEquals(w.checkUpdate(BaseHttpBO.CASE_Warehousing_Approve), "");
        // ProviderID检查
        w.setProviderID(0L);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
        //
        w.setProviderID(Long.valueOf(BaseSQLiteBO.INVALID_ID));
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
        //
        w.setProviderID(1L);
        assertEquals(w.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_b_Create() {
        Warehousing w = new Warehousing();
        w.setStaffID(1);
        w.setProviderID(1L);
        w.setPurchasingOrderID(1);
        w.setWarehouseID(1);

        // ProviderID检查
        w.setProviderID(0L);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
        //
        w.setProviderID(Long.valueOf(BaseSQLiteBO.INVALID_ID));
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
        //
        w.setProviderID(1L);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // StaffID检查
        w.setStaffID(0);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_staffID);
        //
        w.setStaffID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_staffID);
        //
        w.setStaffID(1);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
        // WarehouseID检查
        w.setWarehouseID(0);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
        //
        w.setWarehouseID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
        //
        w.setWarehouseID(1);
        assertEquals(w.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");

    }

    @Test
    public void test_c_Delete() {
        Warehousing w = new Warehousing();
        assertEquals(w.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(w.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(1);
        assertEquals(w.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_d_RetrieveN() {
        Warehousing warehousing = new Warehousing();
        //
        // 检查page indx
        warehousing.setPageIndex("-1");
        warehousing.setPageSize("10");
        assertEquals(warehousing.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        warehousing.setPageIndex("1");
        warehousing.setPageSize("-1");
        assertEquals(warehousing.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        warehousing.setPageIndex("1");
        warehousing.setPageSize("10");
        assertEquals(warehousing.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");

    }

    @Test
    public void test_e_Retrieve1() {
        Warehousing w = new Warehousing();
        assertEquals(w.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(w.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        w.setID(1);
        assertEquals(w.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
}
