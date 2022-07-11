package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.bo.BaseSQLiteBO;
import wpos.model.ErrorInfo;
import wpos.model.RememberLoginStaff;
import wpos.utils.Shared;

import java.util.List;

public class RememberLoginStaffPresenterTest extends BasePresenterTest {

    @BeforeClass
    public void setUp() {
        super.setUp();
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
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
        Assert.assertTrue(rememberLoginStaffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createAsync 测试失败,原因:记住密码失败");
    }

    @Test
    public void test_b_retrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();
        RememberLoginStaff rememberLoginStaff = DataInput.getRememberLoginStaff();
        List<?> list = rememberLoginStaffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rememberLoginStaff);
        Assert.assertTrue(list.size() > 0, "上一次测试test_a_createAsync()没有成功记住密码！");
        RememberLoginStaff rls = (RememberLoginStaff) list.get(0);
        Assert.assertTrue(rememberLoginStaffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && "".equals(rls.checkCreate(BaseSQLiteBO.INVALID_CASE_ID)), "createAsync 测试失败,原因：查询失败");
    }
}
