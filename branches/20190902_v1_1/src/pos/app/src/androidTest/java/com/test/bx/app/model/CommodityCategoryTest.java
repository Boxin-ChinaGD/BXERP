package com.test.bx.app.model;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;
import com.test.bx.app.BaseModelTest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommodityCategoryTest extends BaseModelTest {

    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkCreate() {
        Shared.printTestMethodStartInfo();

        // 检查小类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
        CommodityCategory c=new CommodityCategory();
        c.setName("饮料");
        c.setParentID(1);
        String err = "";
        err = c.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        c.setName("abcde123456饮料");
        err = c.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_name);

        c.setName("饮料..@#");
        err = c.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_name);

        c.setName("饮料");
        c.setParentID(-1);
        err = c.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_parentID);
    }

    @Test
    public void test_b_checkUpdate() {
        Shared.printTestMethodStartInfo();

        // 检查小类名称,其余已在FieldFormatTest.checkCategoryName()已经作了充分测试
        CommodityCategory c= new CommodityCategory();
        c.setID(1l);
        c.setParentID(1);
        c.setName("饮料");
        String err = c.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        c.setID(-1l);
        err = c.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);

        c.setID(1l);
        c.setParentID(-1);
        err = c.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_parentID);

        c.setParentID(1);
        c.setName("12345abcde饮料");
        err = c.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_name);

        c.setParentID(1);
        c.setName("饮料..%@");
        err = c.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_name);
    }

    @Test
    public void test_c_checkRetrieve1() {
        Shared.printTestMethodStartInfo();

        CommodityCategory c = new CommodityCategory();
        c.setID(1l);
        String err = c.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        c.setID(-1l);
        err = c.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);

    }


    @Test
    public void test_d_checkRetrieveN() {
        Shared.printTestMethodStartInfo();

        CommodityCategory c = new CommodityCategory();
        c.setName("饮料");
        String err = c.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        c.setName("12345678123456781234567812345678饮料");
        err = c.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, CommodityCategory.FIELD_ERROR_name);

        c.setName("!@#&$^...");
        err = c.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
    }

    @Test
    public void test_e_checkDelete() {
        Shared.printTestMethodStartInfo();

        CommodityCategory c = new CommodityCategory();
        c.setID(1l);
        String err = c.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        c.setID(-1l);
        err = c.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
    }
}
