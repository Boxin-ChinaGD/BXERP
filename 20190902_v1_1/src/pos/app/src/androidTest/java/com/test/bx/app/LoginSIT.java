package com.test.bx.app;


import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.CompanyHttpBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.ShopHttpBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CompanyHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.ShopHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.Date;
/**
 *该测试在UAT前先不做处理，等UAT后再来恢复测试
 * */
public class LoginSIT extends BaseHttpAndroidTestCase {
    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;

    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;

    private static PosSQLiteEvent posSQLiteEvent = null;
    private static PosHttpEvent posHttpEvent = null;
    private static PosHttpBO posHttpBO = null;

    private static StaffHttpEvent staffHttpEvent = null;
    private static StaffHttpBO staffHttpBO = null;

    private static ShopHttpBO shopHttpBO = null;
    private static ShopHttpEvent shopHttpEvent = null;
    private static CompanyHttpBO companyHttpBO = null;
    private static CompanyHttpEvent companyHttpEvent = null;

    private static final int EVENT_ID_LoginSIT = 10000;
    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(EVENT_ID_LoginSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffSQLiteEvent.setHttpBO(staffLoginHttpBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(EVENT_ID_LoginSIT);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (posHttpBO == null) {
            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        posHttpEvent.setHttpBO(posHttpBO);
        posSQLiteEvent.setHttpBO(posHttpBO);

        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);

        if (shopHttpEvent == null){
            shopHttpEvent = new ShopHttpEvent();
            shopHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (shopHttpBO == null){
            shopHttpBO = new ShopHttpBO(GlobalController.getInstance().getContext(), null, shopHttpEvent);
        }
        shopHttpEvent.setHttpBO(shopHttpBO);

        if (companyHttpEvent == null){
            companyHttpEvent = new CompanyHttpEvent();
            companyHttpEvent.setId(EVENT_ID_LoginSIT);
        }
        if (companyHttpBO == null){
            companyHttpBO = new CompanyHttpBO(GlobalController.getInstance().getContext(), null, companyHttpEvent);
        }
        companyHttpEvent.setHttpBO(companyHttpBO);
    }

    public static class DataInput {
        private static Pos posInput = null;
        private static Staff staffInput = null;
        private static Shop shopInput = null;
        private static Company companyInput = null;

        private static final Company getCompany() throws CloneNotSupportedException {
            companyInput = new Company();
            companyInput.setBusinessLicensePicture("blp" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBusinessLicenseSN("blsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBossPhone("13324544444");
            companyInput.setBossPassword("000000");
            companyInput.setDbName("db" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBossWechat("wechat" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setKey("key" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setName("name" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBossName("b" + String.valueOf(System.currentTimeMillis()).substring(4));
            companyInput.setDbUserName("root");
            companyInput.setDbUserPassword("WEF#EGEHEH$$^*DI");
            return (Company) companyInput.clone();
        }

        private static final Shop getShop() throws Exception {
            shopInput = new Shop();

            shopInput.setName("博昕" + String.valueOf(System.currentTimeMillis()).substring(6));
            shopInput.setCompanyID(1);
            shopInput.setAddress("岗顶");
            shopInput.setStatus(1);
            shopInput.setLongitude( 123.123d);
            shopInput.setLatitude( 123.12d);
            shopInput.setKey("123456");

            return (Shop) shopInput.clone();
        }

        protected static final Staff getStaff() throws Exception {
            staffInput = new Staff();
            staffInput.setPhone("15200702" + (int) ((Math.random() * 9 + 1) * 100));
            staffInput.setName("店员" + System.currentTimeMillis() % 1000000);//
            staffInput.setICID("431024199703241" + (int) ((Math.random() * 9 + 1) * 100)); //
            staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
            staffInput.setPasswordExpireDate(new Date());//
            staffInput.setIsFirstTimeLogin(1); //
            staffInput.setShopID(1);//
            staffInput.setDepartmentID(1);//
            staffInput.setSyncDatetime(new Date());
            staffInput.setInt1(1);
            staffInput.setPasswordInPOS("000000");

            return (Staff) staffInput.clone();
        }

        protected static final Pos getPos() throws CloneNotSupportedException {
            posInput = new Pos();
            posInput.setStatus(0);
            posInput.setShopID(1);
            posInput.setPos_SN("SN" + System.currentTimeMillis() % 1000000);
            posInput.setInt1(1);
            posInput.setPasswordInPOS("000000");
            posInput.setPwdEncrypted("000000");
            posInput.setID(0);

            return posInput;
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShopHttpEvent(ShopHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompanyHttpEvent(CompanyHttpEvent event) {
        if (event.getId() == EVENT_ID_LoginSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    //核心思路是，增加一个实体，然后拿POS和（或）STAFF登录成功，再删除实体，这时POS和STAFF不能再登录成功了。
//    1、对POS实现这个核心思路。
//    @Test
//    public void testPos() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        Shared.login(1l);
//        //创建一个pos并登录
//        Pos pos = DataInput.getPos();
//        Pos p = getServerRequestPos(pos);
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //删除该pos
//        Shared.login(1l);
//
//        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!posHttpBO.deleteAsync(p)) {
//            Assert.assertTrue("pos删除失败！", false);
//        }
//
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("pos删除超时！", false);
//        }
//
//        Assert.assertTrue("请求删除Pos之后，服务器返回的错误码不正确", posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //使用之前创建的pos进行登录
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_Hack);
//    }

    //    2、对STAFF实现这个核心思路。
//    @Test
//    public void test_Staff() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        Shared.login(1l);
//
//        //创建staff并登录
//        Staff currentStaff = DataInput.getCurrentStaff();
//        Staff s = getServerRequestStaff(currentStaff);
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_NoError);
//        //
//        Shared.login(1l);
//        //
//        staffHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!staffHttpBO.deleteAsync(s)) {
//            Assert.assertTrue("请求删除staff失败！", false);
//        }
//
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("请求服务器删除Staff超时!", false);
//        }
//        Assert.assertTrue("删除对象失败!", staffHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //进行登录
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_Hack);
//    }

    //    3、对SHOP实现这个核心思路。
//    @Test
//    public void testShop() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        Shared.login(1l);
//        //创建一个Shop
//        Shop shop = DataInput.getShop();
//        if (!shopHttpBO.createAsync(shop)) {
//            Assert.assertTrue("调用shop创建失败！", false);
//        }
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (shopHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (shopHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("Shop创建超时!", false);
//        }
//
//        Assert.assertTrue("创建Shop返回的错误码不正确！", shopHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Shop shop1 = (Shop) shopHttpBO.getHttpEvent().getBaseModel1();
//        Assert.assertTrue("shop1对象为空", shop1 != null);
//
//        //创建相应的pos,currentStaff
//        Pos pos = DataInput.getPos();
//        pos.setShopID(shop1.getID());
//
//        Pos p = getServerRequestPos(pos);
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //创建staff并登录
//        Shared.login(1l);
//
//        Staff currentStaff = DataInput.getCurrentStaff();
//        currentStaff.setShopID(shop1.getID().intValue());
//
//        Staff s = getServerRequestStaff(currentStaff);
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //删除Shop
//        Shared.login(1l);
//
//        shopHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!shopHttpBO.deleteAsync(shop1)) {
//            Assert.assertTrue("请求删除shop失败！", false);
//        }
//
//        lTimeOut = 30;
//        while (shopHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("Shop创建超时!", false);
//        }
//
//        Assert.assertTrue("删除shop失败！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //使用pos,staff登录(登录失败)
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_Hack);
//
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_Hack);
//    }

    //    4、对Company实现这个核心思路
//    @Test
//    public void testCompany() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        Shared.login(1, "13185246281", "000000", 1, "668866");
//
//        //创建Company
//        Company company = DataInput.getCompany();
//        if (!companyHttpBO.createAsync(company)) {
//            Assert.assertTrue("调用创建company失败！", false);
//        }
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (companyHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (companyHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("create company超时!", false);
//        }
//        Assert.assertTrue("创建Company失败！", companyHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Company company1 = (Company) companyHttpBO.getHttpEvent().getBaseModel1();
//        Assert.assertTrue("Company为空", company1 != null);
//
//        String shopID = companyHttpBO.getHttpEvent().getString1();
//        //创建POS并登录
//        Pos pos = DataInput.getPos();
//        pos.setShopID(Long.valueOf(shopID));
//        Pos p = getServerRequestPos(pos);
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //创建Staff并登录
//        Shared.login(1l);
//        Staff currentStaff = DataInput.getCurrentStaff();
//        currentStaff.setShopID(Integer.valueOf(shopID));
//        Staff s = getServerRequestStaff(currentStaff);
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_NoError);
//
//        //删除Company
//        Shared.login(1l);
//
//        companyHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!companyHttpBO.deleteAsync(company1)) {
//            Assert.assertTrue("Company调用delete失败!", false);
//        }
//        lTimeOut = 60;
//        while (companyHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (companyHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("create company超时!", false);
//        }
//        Assert.assertTrue("删除Company失败！", companyHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        //staff和pos登录
//        posLogin(p, ErrorInfo.EnumErrorCode.EC_Hack);
//        staffLogin(s, ErrorInfo.EnumErrorCode.EC_Hack);
//    }

    //请求服务器创建Pos
    public Pos getServerRequestPos(Pos pos) throws InterruptedException {
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.posGetToken(pos);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos getToken超时!", false);
        }

        //
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, pos)) {
            Assert.assertTrue("pos创建失败！", false);
        }
        //
        lTimeOut = 50;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos创建超时！", false);
        }

        Assert.assertTrue("请求创建Pos之后，服务器返回的错误码不正确", posHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Pos p = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("创建pos失败！", p != null);

        p.setPasswordInPOS("000000");
        return p;
    }

    //请求服务器创建Staff
    public Staff getServerRequestStaff(Staff staff) throws Exception {
        staffLoginHttpBO.staffGetToken(staff, true);

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("currentStaff getToken超时!", false);
        }
        //
        staffHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!staffHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, staff)) {
            Assert.assertTrue("创建失败!", false);
        }
        //
        lTimeOut = 30;
        while (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (staffHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("请求服务器创建Staff超时!", false);
        }
        Assert.assertTrue("创建对象失败!", staffHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Staff s = (Staff) staffHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("创建的staff为空！", s != null);

        s.setPasswordInPOS("000000");
        return s;
    }

    //用于登录pos并验证结果是否和预期的是否一致
    public void posLogin(Pos pos, ErrorInfo.EnumErrorCode errorCode) throws InterruptedException {
        posLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        posLoginHttpBO.setPos(pos);
        posLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        posLoginHttpBO.loginAsync();
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (posLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && posLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (posLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("pos登录超时!", false);
        }

        Assert.assertTrue("pos登录结果和预期的不一样", posLoginHttpBO.getHttpEvent().getLastErrorCode() == errorCode);
    }

    //用于登录staff并验证结果是否和预期的是否一致
    public void staffLogin(Staff staff, ErrorInfo.EnumErrorCode errorCode) throws InterruptedException {
        staff.setInt1(1);
        staffLoginHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        staffLoginHttpBO.setStaff(staff);
        staffLoginHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        staffLoginHttpBO.loginAsync();
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (staffLoginHttpBO.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
            if (staffLoginHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && staffLoginHttpBO.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (staffLoginHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("STAFF登录超时!", false);
        }

        Assert.assertTrue("staff登录结果和预期的不一致！", staffLoginHttpBO.getHttpEvent().getLastErrorCode() == errorCode);
    }
}
