package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class StaffPresenterTest extends BaseAndroidTestCase {
    private static StaffPresenter staffPresenter;
    private static StaffSQLiteEvent staffSQLiteEvent;

    private static final int Event_ID_StaffPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        staffPresenter = GlobalController.getInstance().getStaffPresenter();
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(Event_ID_StaffPresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSatffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_StaffPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Staff staffInput = new Staff();

        protected static final Staff getStaff() throws Exception {
            staffInput.setPhone(getValidStaffPhone());
            Thread.sleep(10);
            staffInput.setName("??????" + Shared.generateCompanyName(6));
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
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Staff Staff = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", staffCreate.compareTo(Staff) == 0);
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_b1_UpdateSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Staff staffCreate2 = DataInput.getStaff();
        staffCreate2.setID(staffCreate.getID());
        staffCreate2.setPhone("13811111111");

        //???????????????case???update??????????????????
        staffPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue("UpdateSync bm1????????????,??????:????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Staff staffRerieve1 = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue("retrieve1ObjectSync??????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("update", staffCreate2.compareTo(staffRerieve1) == 0);
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_c1_RetrieveNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        //case1???????????????
        List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync??????????????????????????????>=0!", staffList != null && staffList.size() >= 0);

        //case2: ??????Phone??????
        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Staff s = new Staff();
        s.setSql("where F_Phone = ?");
        s.setConditions(new String[]{staffCreate.getPhone()});
        s.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        List<Staff> staffList1 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, s);
        Assert.assertTrue("????????????RetrieveNSync????????????,??????:????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????RetrieveNSync??????????????????????????????>=0!", staffList1 != null && staffList1.size() >= 0);
    }

    /**
     * ????????????Staff???????????????
     */
    @Test
    public void test_c2_retrieveNStaff() throws Exception {
        Shared.printTestMethodStartInfo();

        Integer total = staffPresenter.retrieveCount();
        System.out.println("Staff???????????????" + total);
        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }

    @Test
    public void test_d1_Retrieve1Sync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue("????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //???????????????case
        Staff staff = new Staff();
        staff.setID(staffCreate.getID());
        Staff staff1 = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        Assert.assertTrue("Retrieve1 ????????????!", staff1 != null);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
    }

    @Test
    public void test_e1_DeleteSync() throws Exception {
        Shared.printTestMethodStartInfo();

        Staff staffCreate = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate);
        Assert.assertTrue("????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Staff staff = new Staff();
        staff.setID(staffCreate.getID());
        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
        Assert.assertTrue("deleteObjectSync ????????????,??????????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Staff staff1 = new Staff();
        staff1.setID(staffCreate.getID());
        BaseModel bm3 = staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("???????????????", bm3 == null);
    }

    @Test
    public void test_f1_deleteNSync() throws Exception {
        Shared.printTestMethodStartInfo();

        List<Staff> staffList = new ArrayList<>();
        Staff staffCreate1 = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate1);
        Assert.assertTrue("????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode(), staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        staffList.add(staffCreate1);
        //
        Staff staffCreate2 = DataInput.getStaff();
        staffPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffCreate2);
        Assert.assertTrue("????????????,??????:????????????????????????!", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        staffList.add(staffCreate2);
        //
        staffPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync?????????????????????", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        List<Staff> staffList2 = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync???????????????list????????????", staffList2 == null || staffList2.size() == 0);
    }

}
