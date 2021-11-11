package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.Company;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompanyTest extends BaseAndroidTestCase {

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

        //Pos没有必要知道公司的太多信息。NBR也不会返回

        Company company = new Company();
        company.setBrandName("娃哈哈");
        String error = "";
//        caseLog("检查公司状态");
//        company.setStatus(1);
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_status);
//        company.setStatus(2);
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_status);
//        company.setStatus(-99);
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_status);
//        company.setStatus(0);

//        caseLog("检查营业执照");
//        company.setBusinessLicenseSN("123456789");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("1234567891234567");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("123456789123456789123");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("123aa6789aa3456");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("123&&6789*13456");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("123测试6789测试3456");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
//        company.setBusinessLicenseSN("123456789123456");


//        caseLog("检查营业执照路径长度");
//        String string = "12345678901234567890";
//        company.setBusinessLicensePicture(string + string + string + string + string + string + string);
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_businessLicensePicture);
//        company.setBusinessLicensePicture("");

//        caseLog("检查电话号码");
//        company.setBossPhone("123456789101");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_bossPhone);
//        company.setBossPhone("123456");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_bossPhone);
//        company.setBossPhone("");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_bossPhone);
//        company.setBossPhone("13764229543");

//        caseLog("检查密码");
//        company.setBossPassword("12345");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_Password);
//        company.setBossPassword("12345678901234567");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_Password);
//        company.setBossPassword("112123");

//        caseLog("检查微信号");
//        company.setBossWechat("a12345678901234567890");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
//        company.setBossWechat("a1156+156");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
//        company.setBossWechat("a123_456");

//        caseLog("检查数据库名称");
//        company.setDbName("还好");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
//        company.setDbName("1156156");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
//        company.setDbName("a12345678901234567890");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
//        company.setDbName("a1156+156");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
//        company.setDbName("a1156-156");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_dbName);
//        company.setDbName("a123_456");

        caseLog("检查公司名称");
        company.setName("一二三四五六七八九十一二三");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("1234567890123");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("bx一号公司");

//        caseLog("检查老板名称");
//        company.setBossName("");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
//        company.setBossName("一二三四五六七八九十一二三");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
//        company.setBossName("一二三四五六七八九十一二");
//
//        caseLog("检查数据库名称");
//        company.setDbUserName("");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("12121");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("a123 13");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("123_13");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("_________");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("a12345678901234567890");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_DBUserName);
//        company.setDbUserName("a12313");
//
//        caseLog("检查数据库密码");
//        company.setDbUserPassword("12345");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_Password);
//        company.setDbUserPassword("12345678901234567");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, FieldFormat.FIELD_ERROR_Password);
//        company.setDbUserPassword("123456");
//
//        caseLog("检查子商户号");
//        company.setSubmchid("a123465123");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_submchid);
//        company.setSubmchid("123465123");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_submchid);
//        company.setSubmchid("12311465123");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, Company.FIELD_ERROR_submchid);
//        company.setSubmchid("");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, "");
//        company.setSubmchid("1234651320");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, "");
//        company.setSubmchid(null);
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, "");
//        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        assertEquals(error, "");
        // case brandName
        company.setBrandName(null);
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("123456789012345678901");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("娃哈哈");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setBrandName("可口可乐");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setBrandName("12345678901234567890");
        error = company.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
    }

    @Test
    public void test_b_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        Company company = new Company();
        String error = "";
        error = company.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_c_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        Company company = new Company();
        String error = "";
        error = company.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_d_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        Company company = new Company();
        company.setBrandName("娃哈哈");
        String error = "";

        caseLog("检查ID");
        company.setID(0l);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        company.setID(-99l);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        company.setID(1l);

        caseLog("检查营业执照号");
        company.setBusinessLicenseSN("1234567890123456");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
        company.setBusinessLicenseSN("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_businessLicenseSN);
        company.setBusinessLicenseSN("123456789012345");

        caseLog("检查营业执照路径长度");
        String string = "12345678901234567890";
        company.setBusinessLicensePicture(string + string + string + string + string + string + string);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_businessLicensePicture);
        company.setBusinessLicensePicture("");

        caseLog("检查电话号码");
        company.setBossPhone("123456789101");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_bossPhone);
        company.setBossPhone("123456");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_bossPhone);
        company.setBossPhone("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_bossPhone);
        company.setBossPhone("13764229543");

        caseLog("检查微信号");
        company.setBossWechat("a12345678901234567890");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
        company.setBossWechat("a1156+156");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_wechat);
        company.setBossWechat("a123_456");

        caseLog("检查公司KEY");
        company.setKey("还好");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_key);
        company.setKey("1156156");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_key);
        string = "1234567890";
        company.setKey(string + string + string + "1");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_key);
        company.setKey(string + string + string + string);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_key);
        company.setKey(string + string + string + "12");

        caseLog("检查公司名称");
        company.setName("一二三四五六七八九十一二三");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("1234567890123");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_name);
        company.setName("bx一号公司");

        caseLog("检查老板名称");
        company.setBossName("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
        company.setBossName("一二三四五六七八九十一二三");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, FieldFormat.FIELD_ERROR_HumanName);
        company.setBossName("一二三四五六七八九十一二");

        caseLog("检查子商户号");
        company.setSubmchid("a123465123");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_submchid);
        company.setSubmchid("123465");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_submchid);
        company.setSubmchid("123123456465");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_submchid);
        company.setSubmchid("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setSubmchid("1234651320");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setSubmchid(null);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");

        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        // case brandName
        company.setBrandName(null);
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("123456789012345678901");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, Company.FIELD_ERROR_brandName);
        company.setBrandName("娃哈哈");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setBrandName("可口可乐");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
        company.setBrandName("12345678901234567890");
        error = company.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(error, "");
    }

    @Test
    public void test_e_CheckDelete() {
        Shared.printTestMethodStartInfo();

        Company company = new Company();
        String error = "";
        error = company.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

}
