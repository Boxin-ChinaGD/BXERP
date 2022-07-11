package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.Shared;

public class RememberLoginStaffTest extends BaseTestCase {

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
        RememberLoginStaff rememberLoginStaff = new RememberLoginStaff();
        rememberLoginStaff.setPhone("15854320895");
        rememberLoginStaff.setPassword("000000");
        rememberLoginStaff.setRemembered(true);

        //检查正确手机号
        String error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        //检查带字母手机号
        rememberLoginStaff.setPhone("158543208aa");
        error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RememberLoginStaff.PHONE_ERROR);

        //检查长度不足11位
        rememberLoginStaff.setPhone("158543208");
        error = rememberLoginStaff.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, RememberLoginStaff.PHONE_ERROR);
    }

}
