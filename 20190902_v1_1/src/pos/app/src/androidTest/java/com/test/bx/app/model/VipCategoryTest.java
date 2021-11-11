package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.VipCategory;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class VipCategoryTest extends BaseAndroidTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_CheckCreate() {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = new VipCategory();
        vipCategory.setName("超级会员");
        String error = vipCategory.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试名称");
        vipCategory.setName("^&*@&%^%");
        error = vipCategory.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
        vipCategory.setName("1234567890123456789012345678901234567890");
        error = vipCategory.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
        vipCategory.setName("超级会员");
    }

    @Test
    public void test_b_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = new VipCategory();
        vipCategory.setID(1l);
        String error = vipCategory.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试ID");
        vipCategory.setID(BaseSQLiteBO.INVALID_ID);
        error = vipCategory.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vipCategory.setID(1l);
    }

    @Test
    public void test_c_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = new VipCategory();
        vipCategory.setPageIndex("1");
        vipCategory.setPageSize("10");
        String error = vipCategory.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试PageIndex");
        vipCategory.setPageIndex("0");
        error = vipCategory.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        vipCategory.setPageIndex("1");
        caseLog("测试PageSize");
        vipCategory.setPageSize("0");
        error = vipCategory.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        vipCategory.setPageSize("10");
    }

    @Test
    public void test_d_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = new VipCategory();
        vipCategory.setID(1l);
        vipCategory.setName("超级会员");
        String error = vipCategory.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试名称");
        vipCategory.setName("^&*@&%^%");
        error = vipCategory.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
        vipCategory.setName("1234567890123456789012345678901234567890");
        error = vipCategory.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, VipCategory.FIELD_ERROR_name);
        vipCategory.setName("超级会员");
        caseLog("测试ID");
        vipCategory.setID(BaseSQLiteBO.INVALID_ID);
        error = vipCategory.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vipCategory.setID(1l);


    }

    @Test
    public void test_e_CheckDelete() {
        Shared.printTestMethodStartInfo();

        VipCategory vipCategory = new VipCategory();
        vipCategory.setID(1l);
        String error = vipCategory.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试ID");
        vipCategory.setID(BaseSQLiteBO.INVALID_ID);
        error = vipCategory.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vipCategory.setID(1l);
    }
}
