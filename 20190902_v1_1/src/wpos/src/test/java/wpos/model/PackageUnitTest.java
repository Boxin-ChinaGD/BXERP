package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import java.util.Date;

public class PackageUnitTest extends BaseTestCase {
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

            packageUnit.setID(10);
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
        pu.setID(0);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10);
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
        pu.setID(0);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10);
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
        pu.setID(0);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(-99);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, errorMsg);
        //
        pu.setID(10);
        errorMsg = pu.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", errorMsg);
    }
}
