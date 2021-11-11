package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RememberLoginStaff;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.RememberLoginStaffPresenter;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class RememberLoginStaffPresenterTest extends BaseAndroidTestCase {
    private static RememberLoginStaffPresenter rememberLoginStaffPresenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        rememberLoginStaffPresenter = GlobalController.getInstance().getRememberLoginStaffPresenter();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static RememberLoginStaff rememberLoginStaff = new RememberLoginStaff();

        protected static final RememberLoginStaff getRememberLoginStaff() throws Exception {
            rememberLoginStaff.setPassword("000000");
            Thread.sleep(10);
            rememberLoginStaff.setPhone("15854320895");
            Thread.sleep(10);
            rememberLoginStaff.setRemembered(true);
            Thread.sleep(10);

            return (RememberLoginStaff) rememberLoginStaff.clone();
        }
    }

    @Test
    public void test_a_createAsync() throws Exception {
        Shared.printTestMethodStartInfo();
        RememberLoginStaff rememberLoginStaff = DataInput.getRememberLoginStaff();
        rememberLoginStaffPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, rememberLoginStaff, null);
        Thread.sleep(5 * 1000); // 5秒应该是足够的
        System.out.println("错误码=" + rememberLoginStaffPresenter.getLastErrorCode());
        Assert.assertTrue("createAsync 测试失败,原因:记住密码失败", rememberLoginStaffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_b_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        RememberLoginStaff rememberLoginStaff = DataInput.getRememberLoginStaff();
        List<?> list = rememberLoginStaffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rememberLoginStaff);
        Assert.assertTrue("上一次测试test_a_createAsync()没有成功记住密码！", list.size() > 0);
        RememberLoginStaff rls = (RememberLoginStaff) list.get(0);
        Assert.assertTrue("createAsync 测试失败,原因：查询失败", rememberLoginStaffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && "".equals(rls.checkCreate(BaseSQLiteBO.INVALID_CASE_ID)));
    }
}
