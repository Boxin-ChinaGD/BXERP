package com.test.bx.app;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.StaffPresenter;
import com.base.BaseAndroidTestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class GetMaxIDJUnit extends BaseAndroidTestCase {
    private static StaffPresenter presenter = null;

    @Override
    public void setUp() throws Exception {
        if (presenter == null) {
            presenter = GlobalController.getInstance().getStaffPresenter();
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void test_a_GetMaxID() throws CloneNotSupportedException {
        //正常Case
        Staff staff = StaffSIT.DataInput.getStaffInput();
        staff.setSalt(getRandomString(32));
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        List<Staff> staffList1 = (List<Staff>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Staff staff1 = StaffSIT.DataInput.getStaffInput();
        staff1.setSalt(getRandomString(32));
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff1);
        List<Staff> staffList2 = (List<Staff>) presenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        long id1 = commodityPresenter.generateTmpRowID();
//        Assert.assertTrue("最大ID查找失败!", id1 == staff1.getID());

        //临时数据Case
        Staff staff2 = StaffSIT.DataInput.getStaffInput();
        staff2.setSalt(getRandomString(32));
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff2);
        Assert.assertTrue("错误码，不正确：" + presenter.getLastErrorCode(), presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Staff staff3 = StaffSIT.DataInput.getStaffInput();
        staff3.setID(staff2.getID() * 2 + 10000);
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff3);
//        long id2 = commodityPresenter.getMaxId(new Staff());
//        Assert.assertTrue("临时数据Case, 查找最大ID失败", id2 == staff2.getID());
    }

    // 随机生成指定长度的字符串
    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
