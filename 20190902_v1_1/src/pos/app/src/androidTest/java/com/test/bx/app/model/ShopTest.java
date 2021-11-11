package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.Shop;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShopTest extends BaseAndroidTestCase {

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

        Shop shop = new Shop();
        shop.setName("博昕小卖部");
        shop.setAddress("华南植物园");
        shop.setKey("********************");
        shop.setRemark("紫薇小卖部");
        shop.setLongitude(100f);
        shop.setLatitude(100f);
        shop.setStatus(0);
        shop.setCompanyID(1);
        shop.setDistrictID(1);
        shop.setBxStaffID(1);
        String error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试name
        shop.setName("123456781234567812345678博昕小卖部");
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_name);
        shop.setName("博昕小卖部");
        //测试address
        shop.setAddress("12345678123456781234567812345678华南植物园");
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_address);
        shop.setAddress("华南植物园");
        //测试key
        shop.setKey("12345678123456781234567812345678aa");
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_key);
        shop.setKey("********************");
        //测试Remark
        shop.setRemark("12345678123456781234567812345678123456781234567812345678123456781234567812345678"
                + "12345678123456781234567812345678123456781234567812345678123456781234567812345678");
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_remark);
        shop.setRemark("紫薇小卖部");
        //测试Longitude
        shop.setLongitude(-1);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_longitude);
        shop.setLongitude(100f);
        //测试Latitude
        shop.setLatitude(-1);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_latitude);
        shop.setLatitude(100f);
        //测试status
        shop.setStatus(BaseSQLiteBO.INVALID_STATUS);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_status);
        shop.setStatus(0);
        //测试companyID
        shop.setCompanyID(BaseSQLiteBO.INVALID_ID);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_companyID);
        shop.setCompanyID(1);
        //测试distrctID
        shop.setDistrictID(BaseSQLiteBO.INVALID_INT_ID);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_districtID);
        shop.setDistrictID(1);
        //测试bxStaffID
        shop.setBxStaffID(BaseSQLiteBO.INVALID_INT_ID);
        error = shop.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_bxStaffID);
        shop.setBxStaffID(1);
    }

    @Test
    public void test_b_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        Shop shop = new Shop();
        shop.setID(1);
        String error = shop.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试ID
        shop.setID(BaseSQLiteBO.INVALID_ID);
        error = shop.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
    }

    @Test
    public void test_c_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

    }

    @Test
    public void test_d_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        Shop shop = new Shop();
        shop.setID(1);
        shop.setName("博昕小卖部");
        shop.setAddress("华南植物园");
        shop.setKey("********************");
        shop.setRemark("紫薇小卖部");
        shop.setLongitude(100f);
        shop.setLatitude(100f);
        shop.setDistrictID(1);
        String error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试ID
        shop.setID(BaseSQLiteBO.INVALID_ID);
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        shop.setID(1);
        //测试name
        shop.setName("123456781234567812345678博昕小卖部");
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_name);
        shop.setName("博昕小卖部");
        //测试address
        shop.setAddress("12345678123456781234567812345678华南植物园");
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_address);
        shop.setAddress("华南植物园");
        //测试key
        shop.setKey("12345678123456781234567812345678aa");
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_key);
        shop.setKey("********************");
        //测试Remark
        shop.setRemark("12345678123456781234567812345678123456781234567812345678123456781234567812345678"
                + "12345678123456781234567812345678123456781234567812345678123456781234567812345678");
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_remark);
        shop.setRemark("紫薇小卖部");
        //测试Longitude
        shop.setLongitude(-1);
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_longitude);
        shop.setLongitude(100f);
        //测试Latitude
        shop.setLatitude(-1);
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_latitude);
        shop.setLatitude(100f);
        //测试distrctID
        shop.setDistrictID(BaseSQLiteBO.INVALID_INT_ID);
        error = shop.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_districtID);
        shop.setDistrictID(1);
    }

    @Test
    public void test_e_CheckDelete() {
        Shared.printTestMethodStartInfo();

        Shop shop = new Shop();
        shop.setID(1);
        shop.setCompanyID(1);
        String error = shop.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试ID
        shop.setCompanyID(BaseSQLiteBO.INVALID_INT_ID);
        error = shop.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Shop.FIELD_ERROR_companyID);
        shop.setCompanyID(1);
    }
}
