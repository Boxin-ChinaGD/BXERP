package wpos.UT;import org.testng.Assert;import org.testng.annotations.AfterClass;import org.testng.annotations.BeforeClass;import org.testng.annotations.Test;import wpos.SIT.StaffSIT;import wpos.allEnum.ThreadMode;import wpos.base.BaseHttpTestCase;import wpos.bo.*;import wpos.event.*;import wpos.event.UI.StaffSQLiteEvent;import wpos.helper.Configuration;import wpos.listener.Subscribe;import wpos.model.BaseModel;import wpos.model.ErrorInfo;import wpos.model.Staff;import wpos.utils.Shared;import wpos.utils.StringUtils;import java.util.ArrayList;import java.util.List;import static wpos.bo.BaseSQLiteBO.CASE_Staff_RetrieveNByConditions;public class StaffJUnit extends BaseHttpTestCase {    private static StaffSQLiteBO staffSQLiteBO = null;    private static StaffHttpBO staffHttpBO = null;    private static StaffLoginHttpBO staffLoginHttpBO = null;    private static PosLoginHttpBO posLoginHttpBO = null;    private static StaffSQLiteEvent staffSQLiteEvent = null;    private static StaffHttpEvent staffHttpEvent = null;    private static PosHttpEvent posHttpEvent = null;    private static PosSQLiteEvent posSQLiteEvent = null;    private static PosLoginHttpEvent posLoginHttpEvent = null;    private static StaffLoginHttpEvent staffLoginHttpEvent = null;    private static PosHttpBO posHttpBO = null;    private static PosSQLiteBO posSQLiteBO = null;    private static final int EVENT_ID_StaffJUnit = 10000;    static List<Staff> staffList = new ArrayList<Staff>();    @Override    @BeforeClass    public void setUp() {        super.setUp();        //        if (posLoginHttpEvent == null) {            posLoginHttpEvent = new PosLoginHttpEvent();            posLoginHttpEvent.setId(EVENT_ID_StaffJUnit);        }        if (staffLoginHttpEvent == null) {            staffLoginHttpEvent = new StaffLoginHttpEvent();            staffLoginHttpEvent.setId(EVENT_ID_StaffJUnit);        }        if (posLoginHttpBO == null) {            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);        }        if (staffLoginHttpBO == null) {            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);        }        posLoginHttpEvent.setHttpBO(posLoginHttpBO);        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);        staffLoginHttpEvent.setStaffPresenter(staffPresenter);        //        if (staffSQLiteEvent == null) {            staffSQLiteEvent = new StaffSQLiteEvent();            staffSQLiteEvent.setId(EVENT_ID_StaffJUnit);        }        if (staffHttpEvent == null) {            staffHttpEvent = new StaffHttpEvent();            staffHttpEvent.setId(EVENT_ID_StaffJUnit);            staffHttpEvent.setStaffPresenter(staffPresenter);        }        if (posHttpEvent == null) {            posHttpEvent = new PosHttpEvent();            posHttpEvent.setId(EVENT_ID_StaffJUnit);        }        if (posSQLiteEvent == null) {            posSQLiteEvent = new PosSQLiteEvent();            posSQLiteEvent.setId(EVENT_ID_StaffJUnit);        }        //        if (staffSQLiteBO == null) {            staffSQLiteBO = new StaffSQLiteBO(staffSQLiteEvent, staffHttpEvent);            staffSQLiteBO.setStaffPresenter(staffPresenter);        }        if (staffHttpBO == null) {            staffHttpBO = new StaffHttpBO(staffSQLiteEvent, staffHttpEvent);        }        if (posHttpBO == null) {            posHttpBO = new PosHttpBO(posSQLiteEvent, posHttpEvent);        }        if (posSQLiteBO == null) {            posSQLiteBO = new PosSQLiteBO(posSQLiteEvent, posHttpEvent);        }        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);        staffSQLiteEvent.setHttpBO(staffHttpBO);        staffHttpEvent.setSqliteBO(staffSQLiteBO);        staffHttpEvent.setHttpBO(staffHttpBO);        posHttpEvent.setHttpBO(posHttpBO);        posHttpEvent.setSqliteBO(posSQLiteBO);        posSQLiteEvent.setHttpBO(posHttpBO);        posSQLiteEvent.setSqliteBO(posSQLiteBO);        //        logoutHttpEvent.setId(EVENT_ID_StaffJUnit);        logoutHttpBO.setHttpEvent(logoutHttpEvent);        logoutHttpEvent.setHttpBO(logoutHttpBO);    }    @Override    @AfterClass    public void tearDown() {        super.tearDown();    }    @BeforeClass    public static void beforeClass() {        Shared.printTestClassStartInfo();    }    @AfterClass    public static void afterClass() {        Shared.printTestClassEndInfo();    }    @Subscribe(threadMode = ThreadMode.ASYNC)    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {        if (event.getId() == EVENT_ID_StaffJUnit) {            event.onEvent();        } else {            StackTraceElement ste = new Exception().getStackTrace()[1];            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }    }    @Subscribe(threadMode = ThreadMode.ASYNC)    public void onStaffHttpEvent(StaffHttpEvent event) {        if (event.getId() == EVENT_ID_StaffJUnit) {            event.onEvent();        } else {            StackTraceElement ste = new Exception().getStackTrace()[1];            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }    }    @Subscribe(threadMode = ThreadMode.ASYNC)    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {        if (event.getId() == EVENT_ID_StaffJUnit) {            event.onEvent();        } else {            StackTraceElement ste = new Exception().getStackTrace()[1];            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }    }    @Subscribe(threadMode = ThreadMode.ASYNC)    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {        if (event.getId() == EVENT_ID_StaffJUnit) {            event.onEvent();        } else {            StackTraceElement ste = new Exception().getStackTrace()[1];            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }    }    @Subscribe(threadMode = ThreadMode.ASYNC)    public void onLogoutHttpEvent(LogoutHttpEvent event) {        if (event.getId() == EVENT_ID_StaffJUnit) {            event.onEvent();        } else {            StackTraceElement ste = new Exception().getStackTrace()[1];            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }    }    @Test    public void test_a_RN() throws Exception {        Shared.printTestMethodStartInfo();        //1.因为需要pos登录, 服务器才能取到pos sessionID,才能进行创建STAFF        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);        //3.自定义一个Staff对象, 进行getToken        Staff staff = StaffSIT.DataInput.getStaffInput();        staffLoginHttpBO.staffGetToken(staff, true);        //        long lTimeOut = 40;        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {            Assert.assertTrue(false, "currentStaff getToken超时!");        }        //4.创建一个staff        staff.setSalt(Shared.getFakedSalt());//getToken后salt设置为null,所以在创建的时候需要重新给它设置一个salt以便通过字段验证        staffHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {            Assert.assertTrue(false, "创建失败!");        }        //        lTimeOut = 30;        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {            Assert.assertTrue(false, "请求服务器创建Staff超时!");        }        logOut();        //5.POS STAFF登录        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        //6.发送RN请求, 服务器应该返回刚刚请求创建的Staff        System.out.println("-------------------------------POS2 RN1");        Staff s = new Staff();        s.setPageIndex("1");        s.setPageSize(Shared.PAGE_SIZE_DEFAULT_MAX);        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {            Assert.assertTrue(false, "staff同步失败!");        }        lTimeOut = 50;        while (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//                staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//                staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {            Assert.assertTrue(false, "staff同步超时!");        }        staffList = (List<Staff>) staffSQLiteBO.getHttpEvent().getListMasterTable();        Assert.assertTrue(staffList.size() != 0, "");        staff.setSql("where F_Phone = %s");        staff.setConditions(new String[]{staff.getPhone()});        staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);        List<Staff> new_staff = (List<Staff>) staffPresenter.retrieveNObjectSync(CASE_Staff_RetrieveNByConditions, staff);        Assert.assertTrue(new_staff.size() > 0, "查询到的staff为空！");        //        Assert.assertTrue(staff.getName().equals(new_staff.get(0).getName()), "服务器返回的数据插入SQLite后，从SQLite中找不到POS1插入的数据");        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问        logOut();    }//    @Test//    public void test_b_Feedback() throws InterruptedException {//        System.out.println("-------------------------------Feedback");//        String staffIDs = "";//        for (int i = 0; i < staffList.size(); i++) {//            staffIDs = staffIDs + "," + staffList.get(i).getID();//        }//        staffIDs = staffIDs.substring(1, staffIDs.length());//        staffHttpBO.feedback(staffIDs);//        //等待处理完毕//        long lTimeOut = 50;//        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {//            Thread.sleep(1000);//        }//        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {//            Assert.assertTrue("测试失败！原因：超时", false);//        }////        //RN//        System.out.println("-------------------------------RN1");//        if (!staffHttpBO.retrieveNAsync(null)) {//            Assert.assertTrue("staff同步失败!", false);//        }//        lTimeOut = 50;//        staffSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);//        while (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {//            Thread.sleep(1000);//        }//        if (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {//            Assert.assertTrue("staff同步超时!", false);//        }//        List<Staff> staffList2 = (List<Staff>) staffSQLiteBO.getHttpEvent().getListMasterTable();//        Assert.assertTrue("", staffList2.size() == 0);//    }    @Test    public void test_c_Update() throws Exception {        System.out.println("------------------------------- Update currentStaff ----------------------------------");        Shared.printTestMethodStartInfo();        //1.因为需要pos登录, 服务器才能取到pos sessionID,才能进行创建STAFF        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        Staff staff2 = StaffSIT.DataInput.getStaffInput();        staff2.setID(staffList.get(0).getID());        if (!staffHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, staff2)) {            Assert.assertTrue(false, "staff修改失败!");        }        long lTimeOut = Shared.UNIT_TEST_TimeOut;        staffHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//                staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {            Assert.assertTrue(false, "staff同步超时!");        }        Assert.assertTrue(staffHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "修改失败！");        logOut();        //5.POS STAFF登录        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        //6.发送RN请求, 服务器应该返回刚刚请求创建的Staff        System.out.println("-------------------------------RN1");        staffSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);        Staff s = new Staff();        s.setPageIndex("1");        s.setPageSize(Shared.PAGE_SIZE_DEFAULT_MAX);        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {            Assert.assertTrue(false, "staff同步失败!");        }        lTimeOut = Shared.UNIT_TEST_TimeOut;        while (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//                staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//                staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {            Assert.assertTrue(false, "staff同步超时!");        }        staffList = (List<Staff>) staffSQLiteBO.getHttpEvent().getListMasterTable();        Assert.assertTrue(staffList.size() > 0, "POS2 RN无数据!");        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问        logOut();    }    @Test    public void test_d_Delete() throws Exception {        System.out.println("------------------------------- Delete currentStaff ----------------------------------");        Shared.printTestMethodStartInfo();        //1.因为需要pos登录, 服务器才能取到pos sessionID,才能进行创建STAFF        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        Staff s = new Staff();        s.setID(staffList.get(0).getID());        if (!staffHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, s)) {            Assert.assertTrue(false, "staff删除失败!");        }        long lTimeOut = Shared.UNIT_TEST_TimeOut;        staffHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {            Assert.assertTrue(false, "staff同步超时!");        }        logOut();        //5.POS STAFF登录        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        //6.发送RN请求        System.out.println("-------------------------------RN1");        staffSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);        s.setPageIndex("1");        s.setPageSize(Shared.PAGE_SIZE_DEFAULT_MAX);        if (!staffHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, s)) {            Assert.assertTrue(false, "staff同步失败!");        }        lTimeOut = Shared.UNIT_TEST_TimeOut;        while (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {            Assert.assertTrue(false, "staff同步超时!");        }        staffList = (List<Staff>) staffSQLiteBO.getHttpEvent().getListMasterTable();        s.setSql("where F_ID = %s");        s.setConditions(new String[]{String.valueOf(s.getID())});        s.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);        List<Staff> delete_staff = (List<Staff>) staffPresenter.retrieveNObjectSync(CASE_Staff_RetrieveNByConditions, s);        Assert.assertTrue(delete_staff.size() == 0, "");        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问        logOut();    }    @Test    public void test_d_RetrieveResigned() throws Exception {        System.out.println("------------------------------- RetrieveResigned ----------------------------------");        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {            Assert.assertTrue(false, "登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());        }        if (!staffHttpBO.retrieveResigned(null)) {            Assert.assertTrue(false, "staff删除失败!");        }        long lTimeOut = Shared.UNIT_TEST_TimeOut;        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {            Thread.sleep(1000);        }        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {            Assert.assertTrue(false, "staff同步超时!");        }        String IDs = (String) staffSQLiteEvent.getData();        Integer[] iaStaffID = StringUtils.toIntArray(IDs);        if (iaStaffID != null) {            for (int i = 0; i < iaStaffID.length; i++) {                Staff staff = new Staff();                staff.setID(iaStaffID[i]);                Staff s = (Staff) staffPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staff);                Assert.assertTrue(staffPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1返回的错误码不正确!");                Assert.assertTrue(s == null, "设置的ID存在于SQLite中!");            }        }        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问        logOut();    }}