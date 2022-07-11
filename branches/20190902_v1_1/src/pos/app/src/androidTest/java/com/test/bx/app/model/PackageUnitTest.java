package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class PackageUnitTest extends BaseAndroidTestCase {
    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput{
        private static PackageUnit getPackageUnit(){
            PackageUnit packageUnit = new PackageUnit();

            packageUnit.setID(10l);
            packageUnit.setName("123");
            packageUnit.setCreateDatetime(new Date());
            packageUnit.setUpdateDatetime(new Date());

            return packageUnit;
        }
    }

    @Test
    public void test_a_checkCreate(){
        Shared.printTestClassStartInfo();

        PackageUnit pu = DataInput.getPackageUnit();
        String errorMsg = "";

        caseLog("检查名称");
        pu.setName("123456789");
        errorMsg = pu.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(PackageUnit.FIELD_ERROR_name, errorMsg);
        //
        pu.setName("");
        errorMsg = pu.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(PackageUnit.FIELD_ERROR_name, errorMsg);
        //
        pu.setName("1");
        errorMsg = pu.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
        //
        pu.setName("12345678");
        errorMsg = pu.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
    }

    @Test
    public void test_a_checkUpdate(){
        Shared.printTestClassStartInfo();

        PackageUnit pu = DataInput.getPackageUnit();
        String errorMsg = "";

        caseLog("检查ID");
        pu.setID(0l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);

        caseLog("检查名称");
        pu.setName("123456789");
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(PackageUnit.FIELD_ERROR_name, errorMsg);
        //
        pu.setName("");
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(PackageUnit.FIELD_ERROR_name, errorMsg);
        //
        pu.setName("1");
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
        //
        pu.setName("12345678");
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
    }

    @Test
    public void test_c_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        PackageUnit pu = DataInput.getPackageUnit();
        String errorMsg = "";

        caseLog("检查ID");
        pu.setID(0l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
    }

    @Test
    public void test_d_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        Company company = new Company();
        String error = "";
        error = company.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        org.junit.Assert.assertEquals(error, "");
    }

    @Test
    public void test_c_CheckDelete() {
        Shared.printTestMethodStartInfo();

        PackageUnit pu = DataInput.getPackageUnit();
        String errorMsg = "";

        caseLog("检查ID");
        pu.setID(0l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10l);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
    }
}
