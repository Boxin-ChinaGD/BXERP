package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.RememberLoginStaff;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RememberLoginStaffTest extends BaseAndroidTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkCreate(){
        Shared.printTestMethodStartInfo();
        RememberLoginStaff rememberLoginStaff = new RememberLoginStaff();
        rememberLoginStaff.setPhone("15854320895");
        rememberLoginStaff.setPassword("000000");
        rememberLoginStaff.setRemembered(true);

        //检查正确手机号
        String error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error,"");

        //检查带字母手机号
        rememberLoginStaff.setPhone("158543208aa");
        error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error,RememberLoginStaff.PHONE_ERROR);

        //检查长度不足11位
        rememberLoginStaff.setPhone("158543208");
        error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error,RememberLoginStaff.PHONE_ERROR);
    }

}
