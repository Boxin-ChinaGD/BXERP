package wpos.UT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.SIT.StaffSIT;
import wpos.base.BaseHttpTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.model.ErrorInfo;
import wpos.model.Staff;

import java.util.List;
import java.util.Random;

public class GetMaxIDJUnit extends BaseHttpTestCase {

    @BeforeClass
    public void setUp()  {
        super.setUp();
    }

    @AfterClass
    public void tearDown() {
    }

    @Test
    public void test_a_GetMaxID() throws CloneNotSupportedException {
        //正常Case TODO
//        Staff staff = StaffSIT.DataInput.getStaffInput();
        Staff staff = new Staff();
        staff.setSalt(getRandomString(32));
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        List<Staff> staffList1 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        Staff staff1 = StaffSIT.DataInput.getStaffInput();
        Staff staff1 = new Staff();
        staff1.setSalt(getRandomString(32));
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff1);
        List<Staff> staffList2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        long id1 = commodityPresenter.generateTmpRowID();
//        Assert.assertTrue("最大ID查找失败!", id1 == staff1.getID());

        //临时数据Case
        Staff staff2 = StaffSIT.DataInput.getStaffInput();
        staff2.setSalt(getRandomString(32));
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff2);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码，不正确：" + staffPresenter.getLastErrorCode());
//        Staff staff3 = StaffSIT.DataInput.getStaffInput();
        Staff staff3 = new Staff();
        staff3.setID(staff2.getID() * 2 + 10000);
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff3);
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
