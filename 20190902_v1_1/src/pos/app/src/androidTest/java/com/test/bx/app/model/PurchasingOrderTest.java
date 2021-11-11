package com.test.bx.app.model;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.PurchasingOrder;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PurchasingOrderTest extends BaseAndroidTestCase {

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

        PurchasingOrder purchasingOrder = new PurchasingOrder();
        String error = "";
        //
        // checkStaffID
        purchasingOrder.setStaffID(-1);
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_StaffID);
        purchasingOrder.setStaffID(0);
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_StaffID);
        purchasingOrder.setStaffID(1);
        //
        // checkProviderID
        purchasingOrder.setProviderID(-1);
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
        purchasingOrder.setProviderID(0);
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
        purchasingOrder.setProviderID(1);
        //
        // checkRemark
        StringBuilder sb = new StringBuilder();
        String s = "1234567890";
        for (int i = 0; i < 13; i++) {
            sb.append(s);
        }
        purchasingOrder.setRemark(sb.toString());
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Remark);
        purchasingOrder.setRemark("哈哈哈");
        //
        error = purchasingOrder.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }


    @Test
    public void test_b_checkUpdate() {
        Shared.printTestMethodStartInfo();

        PurchasingOrder purchasingOrder = new PurchasingOrder();
        String error = "";

        // CASE_ApprovePurchasingOrder:
        // CheckID
        purchasingOrder.setID(-99l);
        error = purchasingOrder.checkUpdate(BaseHttpBO.CASE_PurchasingOrder_Approve);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        purchasingOrder.setID(0l);
        error = purchasingOrder.checkUpdate(BaseHttpBO.CASE_PurchasingOrder_Approve);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        purchasingOrder.setID(1l);
        //
        // CheckApproverID
        purchasingOrder.setApproverID(-99);
        error = purchasingOrder.checkUpdate(BaseHttpBO.CASE_PurchasingOrder_Approve);
        assertEquals(error, PurchasingOrder.FIELD_ERROR_ApproveID);
        purchasingOrder.setApproverID(0);
        error = purchasingOrder.checkUpdate(BaseHttpBO.CASE_PurchasingOrder_Approve);
        assertEquals(error, PurchasingOrder.FIELD_ERROR_ApproveID);
        purchasingOrder.setApproverID(1);
        error = purchasingOrder.checkUpdate(BaseHttpBO.CASE_PurchasingOrder_Approve);
        Assert.assertEquals(error, "");

        // CASE_Update
        // CheckID
        purchasingOrder.setID(-99l);
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        purchasingOrder.setID(0l);
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        purchasingOrder.setID(1l);
        //
        // CheckProviderID
        purchasingOrder.setProviderID(-1);
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
        purchasingOrder.setProviderID(0);
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
        purchasingOrder.setProviderID(1);
        //
        // checkRemark
        StringBuilder sb = new StringBuilder();
        String s = "1234567890";
        for (int i = 0; i < 13; i++) {
            sb.append(s);
        }
        purchasingOrder.setRemark(sb.toString());
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Remark);
        purchasingOrder.setRemark("哈哈哈");
        //
        error = purchasingOrder.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }


    @Test
    public void test_c_checkRetrieveN() {
        Shared.printTestMethodStartInfo();

        PurchasingOrder purchasingOrder = new PurchasingOrder();
        String error = "";
        //
        // checkStatus
        purchasingOrder.setStatus(5);
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
        purchasingOrder.setStatus(-99);
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
        purchasingOrder.setStatus(BaseSQLiteBO.INVALID_STATUS);
        //
//        // checkString1
//        purchasingOrder.setString1("4165465465465465654645645165465645456645645465645135465");
//        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
//        Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_String1);
//        purchasingOrder.setString1("");
        //
        // checkStaffID
        purchasingOrder.setStaffID(-2);
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, purchasingOrder.field.getFIELD_NAME_staffID()));
        purchasingOrder.setStaffID(0);
        //
        // checkPageIndex
        purchasingOrder.setPageIndex("-88");
        purchasingOrder.setPageSize("10");
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        purchasingOrder.setPageIndex("0");
        purchasingOrder.setPageSize("10");
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        purchasingOrder.setPageIndex("1");
        purchasingOrder.setPageSize("10");
        //
        // checkPageSize
        purchasingOrder.setPageSize("-88");
        purchasingOrder.setPageIndex("1");
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        purchasingOrder.setPageSize("0");
        purchasingOrder.setPageIndex("1");
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        purchasingOrder.setPageSize("1");
        purchasingOrder.setPageIndex("1");
        //
        error = purchasingOrder.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_d_checkRetrieve1() {
        Shared.printTestMethodStartInfo();
        PurchasingOrder p = new PurchasingOrder();

        String err = "";
        p.setID(-99l);
        err = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        p.setID(0l);
        err = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1l);
        err = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
    }

    @Test
    public void test_e_checkDelete() {
        Shared.printTestMethodStartInfo();

        PurchasingOrder p = new PurchasingOrder();
        String err = "";
        //
        p.setID(-99l);
        err = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        p.setID(0l);
        err = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1l);
        err = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
    }
}
