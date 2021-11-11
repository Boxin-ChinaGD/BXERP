package com.test.bx.app.model;


import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Staff;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StaffTest extends BaseAndroidTestCase {

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

        Staff s = new Staff();
        s.setPhone("12345678910");
        s.setName("收银员");
        s.setICID("44152219940925821X");
        s.setWeChat("weixin");
        s.setSalt("12345678901234567890123456789012");
        s.setIsFirstTimeLogin(1);
        s.setShopID(1);
        s.setDepartmentID(1);
        s.setRoleID(1);
        s.setStatus(0);
        s.setReturnSalt(Staff.RETURN_SALT);
        String err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试phone
        s.setPhone("12345678910aa");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
        s.setPhone("");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
        s.setPhone("12345678910");
        //测试name
        s.setName("@#$!@#$");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_name);
        s.setName("收银员");
        //测试ICID
        s.setICID("44152219940925821Xaa");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ICID);
        s.setICID("44152219940925821X");
        //测试WeChat
        s.setWeChat("@%@%$$%");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
        s.setWeChat("abcde1234567890123456");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
        s.setWeChat("weixin");
        //测试盐值
        s.setSalt("1234567890123456789012345678901234567890");
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_salt);
        s.setSalt("12345678901234567890123456789012");
        //测试是否首次登陆
        s.setIsFirstTimeLogin(2);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_isFirstTimeLogin);
        s.setIsFirstTimeLogin(1);
        //测试门店ID
        s.setShopID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
        s.setShopID(0);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
        s.setShopID(1);
        //测试部门ID
        s.setDepartmentID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
        s.setDepartmentID(0);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
        s.setDepartmentID(1);
        //测试角色ID
        s.setRoleID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
        s.setRoleID(1);
        //测试状态码
        s.setStatus(BaseSQLiteBO.INVALID_STATUS);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_status);
        s.setStatus(0);
        //测试是否返回盐值
        s.setReturnSalt(222);
        err = s.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ReturnSalt);
        s.setReturnSalt(Staff.RETURN_SALT);
    }

    @Test
    public void test_b_checkUpdate() {
        Shared.printTestMethodStartInfo();

        Staff s = new Staff();
        s.setID(1l);
        s.setPhone("12345678910");
        s.setName("收银员");
        s.setICID("44152219940925821X");
        s.setWeChat("weixin");
        s.setShopID(1);
        s.setRoleID(Staff.RETURN_SALT);
        s.setDepartmentID(1);
        s.setStatus(0);
        s.setRoleID(1);
        String err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试ID
        s.setID(BaseSQLiteBO.INVALID_ID);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        s.setID(1l);
        //测试phone
        s.setPhone("12345678910aa");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
        s.setPhone("");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
        s.setPhone("12345678910");
        //测试name
        s.setName("@#$!@#$");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_name);
        s.setName("收银员");
        //测试ICID
        s.setICID("44152219940925821Xaa");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ICID);
        s.setICID("44152219940925821X");
        //测试WeChat
        s.setWeChat("@%@%$$%");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
        s.setWeChat("abcde1234567890123456");
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_weChat);
        s.setWeChat("weixin");
        //测试门店ID
        s.setShopID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
        s.setShopID(0);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_shopID);
        s.setShopID(1);
        //测试部门ID
        s.setDepartmentID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
        s.setDepartmentID(0);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_departmentID);
        s.setDepartmentID(1);
        //测试角色ID
        s.setRoleID(BaseSQLiteBO.INVALID_INT_ID);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_roleID);
        s.setRoleID(1);
        //测试状态码
        s.setStatus(BaseSQLiteBO.INVALID_STATUS);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_status);
        s.setStatus(0);
        //测试是否返回盐值
        s.setReturnSalt(2);
        err = s.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ReturnSalt);
        s.setReturnSalt(Staff.RETURN_SALT);


        Staff s2 = new Staff();
        s2.setPhone("12345678910");
        s2.setOldMD5("123456");
        s2.setNewMD5("654321");
        s2.setIsFirstTimeLogin(0);
        s2.setReturnSalt(1);
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, "");
        //测试phone
        s2.setPhone("123456789102");
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, Staff.FIELD_ERROR_phone);
        s2.setPhone("12345678910");
        //测试string1
        s2.setOldMD5(s2.getNewMD5());
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, Staff.FIELD_ERROR_samePassword);
        s2.setOldMD5("123456");
        //测试string2
        s2.setNewMD5(s2.getOldMD5());
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, Staff.FIELD_ERROR_samePassword);
        s2.setNewMD5("654321");
        //测试是否第一次登陆
        s2.setIsFirstTimeLogin(1);
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, Staff.FIELD_ERROR_isFirstTimeLogin);
        s2.setIsFirstTimeLogin(0);
        //测试是否返回盐值
        s2.setReturnSalt(2);
        err = s2.checkUpdate(BaseHttpBO.CASE_CheckStaff_ResetMyPassword);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ReturnSalt);
        s2.setReturnSalt(1);
    }

    @Test
    public void test_c_checkRetrieve1() {
        Shared.printTestMethodStartInfo();

        Staff s = new Staff();
        s.setID(1l);
        s.setPhone("12345678910");
        s.setInt1(Staff.INVOLVE_RESIGNED);
        s.setReturnSalt(Staff.RETURN_SALT);
        String err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试ID和手机号都不合法
        s.setID(BaseSQLiteBO.INVALID_ID);
        s.setPhone("");
        err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_IDorPhone);
        //测试手机号合法，ID不合法
        s.setID(BaseSQLiteBO.INVALID_ID);
        s.setPhone("12345678910");
        err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试手机号不合法，ID合法
        s.setPhone("12345678910xx");
        s.setID(1l);
        err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试是否查询离职员工
        s.setInvolvedResigned(1000000);
        err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_involvedResigned);
        s.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
        //测试是否返回盐值
        s.setReturnSalt(1000000);
        err = s.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Staff.FIELD_ERROR_ReturnSalt);
        s.setReturnSalt(Staff.RETURN_SALT);

    }

    @Test
    public void test_d_checkDelete() {
        Shared.printTestMethodStartInfo();

        Staff s = new Staff();
        s.setID(1l);
        String err = s.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //测试ID
        s.setID(BaseSQLiteBO.INVALID_ID);
        err = s.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        s.setID(1l);
    }

    @Test
    public void test_e_checkRetrieveN() {
        Shared.printTestMethodStartInfo();

        Staff staff = new Staff();
        //
        caseLog("测试手机号");
        staff.setConditions(new String[]{"152000000012"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        String error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, Staff.FIELD_ERROR_phone);
        //
        staff.setConditions(new String[]{"15200000011"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        //
        staff.setConditions(new String[]{"1520000010"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, Staff.FIELD_ERROR_phone);
        //
        caseLog("测试ID");
        staff.setConditions(new String[]{"1"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        //
        staff.setConditions(new String[]{"0"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        staff.setConditions(new String[]{"-1"});
        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        error = staff.checkRetrieveN(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);

    }
    
}
