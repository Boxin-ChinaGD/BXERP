package com.test.bx.app.model;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.Warehousing;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReturnCommoditySheetTest extends BaseAndroidTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testCheckCreate() {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        returnCommoditySheet.setProviderID(1);
        returnCommoditySheet.setStaffID(1);

        // 检查ProviderID
        returnCommoditySheet.setProviderID(0);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
        //
        returnCommoditySheet.setProviderID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
        //
        returnCommoditySheet.setProviderID(1);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查StaffID
        returnCommoditySheet.setStaffID(0);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
        //
        returnCommoditySheet.setStaffID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
        //
        returnCommoditySheet.setStaffID(1);
        assertEquals(returnCommoditySheet.checkCreate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void testCheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        assertEquals(returnCommoditySheet.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(returnCommoditySheet.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(1L);
        assertEquals(returnCommoditySheet.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void testCheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();

        // 检验String1
        returnCommoditySheet.setQueryKeyword(Shared.getFakedSalt() + "000");
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_string1);
        //
        returnCommoditySheet.setQueryKeyword(null);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setQueryKeyword("213123");
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查staffID
        returnCommoditySheet.setStaffID(-2);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, returnCommoditySheet.field.getFIELD_NAME_staffID()));
        //
        returnCommoditySheet.setStaffID(0);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setStaffID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setStaffID(1);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查status
        returnCommoditySheet.setStatus(2);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_status);
        //
        returnCommoditySheet.setStatus(BaseSQLiteBO.INVALID_STATUS);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setStatus(Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex());
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查ProviderID
        returnCommoditySheet.setProviderID(-2);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN,returnCommoditySheet.field.getFIELD_NAME_providerID()));
        //
        returnCommoditySheet.setProviderID(0);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setProviderID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        returnCommoditySheet.setProviderID(1);
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查pageIndex、pageSize
        returnCommoditySheet.setPageIndex("-1");
        returnCommoditySheet.setPageSize("10");
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        returnCommoditySheet.setPageIndex("1");
        returnCommoditySheet.setPageSize("-10");
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        returnCommoditySheet.setPageIndex("1");
        returnCommoditySheet.setPageSize("10");
        assertEquals(returnCommoditySheet.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void testCheckUpdate() {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        returnCommoditySheet.setID(1L);
        returnCommoditySheet.setStaffID(1);
        returnCommoditySheet.setProviderID(1);

        // BaseSQLiteBO.CASE_ApproveReturnCommoditySheet
        // 检查ID
        returnCommoditySheet.setID(0L);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.CASE_ReturnCommoditySheet_Approve), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.CASE_ReturnCommoditySheet_Approve), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(1L);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.CASE_ReturnCommoditySheet_Approve), "");

        // BaseSQLiteBO.INVALID_CASE_ID
        // 检查ID
        returnCommoditySheet.setID(0L);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(BaseSQLiteBO.INVALID_ID);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        returnCommoditySheet.setID(1L);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查ProviderID
        returnCommoditySheet.setProviderID(0);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
        //
        returnCommoditySheet.setProviderID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
        //
        returnCommoditySheet.setProviderID(1);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");

        // 检查StaffID
        returnCommoditySheet.setStaffID(0);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
        //
        returnCommoditySheet.setStaffID(BaseSQLiteBO.INVALID_INT_ID);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
        //
        returnCommoditySheet.setStaffID(1);
        assertEquals(returnCommoditySheet.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void testCheckDelete() {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        String error = "";
        error = returnCommoditySheet.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    
}
