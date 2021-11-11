package com.test.bx.app.model;


import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.Brand;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;
import com.test.bx.app.BaseModelTest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BrandTest extends BaseModelTest {

    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testcheck_a_Update() {
        Shared.printTestMethodStartInfo();

        Brand b = new Brand();
        String err = "";
        // 检查品牌名称,其余已在FieldFormatTest.checkBrandName()已经作了充分测试
        b.setName("小米");
        b.setID(1l);
        err = b.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("123zbc%^^");
        err = b.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName("1234512345abcdeabcde小米");
        err = b.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName(" 小米  ");
        err = b.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName("小米a1");
        b.setID(0l);
        err = b.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
    }

    @Test
    public void testcheck_b_Create() {
        Shared.printTestMethodStartInfo();

        Brand b = new Brand();
        String err = "";
        // 检查品牌名称,其余已在FieldFormatTest.checkBrandName()已经作了充分测试
        b.setName("小米");
        err = b.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("  小米  ");
        err = b.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName("@$%");
        err = b.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName("1234512345abcdeabcde小米");
        err = b.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);
    }

    @Test
    public void testcheck_c_Retrieve1() {
        Shared.printTestMethodStartInfo();

        Brand b = new Brand();
        b.setID(1l);
        String err = b.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setID(0l);
        err = b.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);

        b.setID(null);
        err = b.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
    }

    @Test
    public void testcheck_d_RetrieveN() {
        Shared.printTestMethodStartInfo();

        Brand b = new Brand();
        b.setName("小米");
        b.setPageIndex(String.valueOf(1));
        b.setPageSize(String.valueOf(10));
        String err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试name
        b.setName("12345123451234512345");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("12345123234512345111111");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Brand.FIELD_ERROR_name);

        b.setName("12345123!@@23451");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("12.345");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName(" 小米  ");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setName("!@%%");
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        b.setName("小米");
        //测试PageIndex
        b.setPageIndex(String.valueOf(0));
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
        b.setPageIndex(String.valueOf(2l));
        //测试PageSize
        b.setPageSize(String.valueOf(0));
        err = b.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
        b.setPageSize(String.valueOf(5));
    }

    @Test
    public void testcheck_e_Delete() {
        Shared.printTestMethodStartInfo();

        Brand b = new Brand();
        b.setID(1l);
        String err =  b.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        b.setID(0l);
        err =  b.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);

        b.setID(null);
        err = b.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
    }

}
