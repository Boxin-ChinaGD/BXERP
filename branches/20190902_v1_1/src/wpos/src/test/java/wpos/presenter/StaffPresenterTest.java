package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Staff;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class StaffPresenterTest extends BasePresenterTest {

    @Resource
    private StaffSQLiteEvent staffSQLiteEvent;

    private static final int Event_ID_StaffPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();
//        staffPresenter = GlobalController.getInstance().getStaffPresenter();
//        if (staffSQLiteEvent == null) {
//            staffSQLiteEvent = new StaffSQLiteEvent();
//            staffSQLiteEvent.setId(Event_ID_StaffPresenterTest);
//        }
        staffSQLiteEvent.setId(Event_ID_StaffPresenterTest);
        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);

        super.tearDown();
    }

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSatffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_StaffPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Staff staffInput = new Staff();

        protected static final Staff getStaff() throws Exception {
            staffInput.setPhone(getValidStaffPhone());
            Thread.sleep(10);
            staffInput.setName("店员" + Shared.generateCompanyName(6));
            Thread.sleep(10);
            staffInput.setICID("32080319970701" + System.currentTimeMillis() % 10000); //
            Thread.sleep(10);
            staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
            Thread.sleep(10);
            staffInput.setSalt("CJS66666666666666666666666666666");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//
            staffInput.setPasswordExpireDate(sdf.parse("2018/12/22 13:11:00")); //
            staffInput.setIsFirstTimeLogin(1);
            staffInput.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            staffInput.setShopID(1);
            staffInput.setDepartmentID(1);
            staffInput.setRoleID(1);
            staffInput.setReturnSalt(1);
            staffInput.setStatus(0);
            staffInput.setInvolvedResigned(1);

            return (Staff) staffInput.clone();
        }

    }

    @Test
    public void test_a1_CreateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());
        //
        Staff Staff = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffCreate.compareTo(Staff) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_b1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());

        Staff staffCreate2 = DataInput.getStaff();
        staffCreate2.setID(staffCreate.getID());
        staffCreate2.setPhone("13811111111");

        //正常修改的case：update的字段不为空
        staffPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1测试失败,原因:返回错误码不正确!");

        Staff staffRerieve1 = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync错误码不正确!");
        Assert.assertTrue(staffCreate2.compareTo(staffRerieve1) == 0, "update");
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_c1_RetrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case1：正常查询
        List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());
        Assert.assertTrue(staffList != null && staffList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");

        //case2: 根据Phone查询
        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        Staff s = new Staff();
        s.setSql("where F_Phone = %s;");
        s.setConditions(new String[]{staffCreate.getPhone()});
        s.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        List<Staff> staffList1 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, s);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(staffList1 != null && staffList1.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>=0!");
    }

    /**
     * 查询本地Staff表的总条数
     */
    @Test
    public void test_c2_retrieveNStaff() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = staffPresenter.retrieveCount();
        System.out.println("Staff表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());
        //正常查询的case
        Staff staff = new Staff();
        staff.setID(staffCreate.getID());
        Staff staff1 = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        Assert.assertTrue(staff1 != null, "Retrieve1 查询失败!");
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_e1_DeleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());

        Staff staff = new Staff();
        staff.setID(staffCreate.getID());
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteObjectSync 测试失败,原因返回错误码不正确!");

        Staff staff1 = new Staff();
        staff1.setID(staffCreate.getID());
        BaseModel bm3 = staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff1);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "删除失败；");
    }

    @Test
    public void test_f1_deleteNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Staff> staffList = new ArrayList<Staff>();
        Staff staffCreate1 = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate1);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());
        staffList.add(staffCreate1);
        //
        Staff staffCreate2 = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "创建失败,原因:返回错误码不正确!");
        staffList.add(staffCreate2);
        //
        staffPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync错误码不正确！");
        //
        List<Staff> staffList2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync错误码不正确！");
        Assert.assertTrue(staffList2 == null || staffList2.size() == 0, "retrieveNObjectSync查询出来的list不能有值");
    }

}
