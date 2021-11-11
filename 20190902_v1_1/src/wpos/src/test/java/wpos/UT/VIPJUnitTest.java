package wpos.UT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.event.UI.VipSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Vip;
import wpos.presenter.VipPresenter;
import wpos.utils.Shared;

import java.util.Date;
import java.util.List;

import static wpos.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync;

public class VIPJUnitTest extends BaseHttpTestCase {
    public static final String MOBILE = "13545678110";
    public static final String MOBILE2 = "13545678111";
    public static final String EROOR_MOBILE = "11111111111";
    public static final String VIP_CARD_SN = "6688661234567891"; // 需要数据库真实存在的

    private static VipHttpBO vipHttpBO = null;
    private static VipSQLiteBO vipSQLiteBO = null;
    private static VipHttpEvent vipHttpEvent = null;
    private static VipSQLiteEvent vipSQLiteEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;

    Vip vip = null;
    private static Vip vipNew = new Vip();
    private static String vipIDs = "";
    private static final int Event_ID_VipJUnitTest = 10000;

    public static class DataInput {

        protected static Vip getVip(VipPresenter vipPresenter) throws Exception {
            Vip vipInput = new Vip();
            vipInput.setID(vipPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, Vip.class));
            vipInput.setCardID(1);
            vipInput.setLocalPosSN("111111111");
            String icid = "46464619980324" + System.currentTimeMillis() % 10000;
            vipInput.setICID(Shared.getValidICID());
            vipInput.setMobile(getValidStaffPhone());
            vipInput.setName("Tommy");
            vipInput.setEmail(System.currentTimeMillis() % 1000000 + "@qq.com");
            vipInput.setConsumeTimes(0);
            vipInput.setConsumeAmount(0);
            vipInput.setDistrict("广州");
            vipInput.setCategory(1);
            vipInput.setBirthday(new Date());
            vipInput.setBonus(0);
            vipInput.setLastConsumeDatetime(new Date());
            vipInput.setSyncDatetime(new Date());
            vipInput.setReturnObject(1);

            return (Vip) vipInput.clone();
        }
    }

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();

        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(Event_ID_VipJUnitTest);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_VipJUnitTest);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(vipSQLiteEvent, vipHttpEvent);
        }
        vipHttpEvent.setHttpBO(vipHttpBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_VipJUnitTest);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_VipJUnitTest);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        logoutHttpEvent.setId(Event_ID_VipJUnitTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        System.out.println("---------------------- ASYNC方式接收到event");
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
    // TODO 是否注释？VipHttpBO.java:97抛异常
    // 不能在Pos机上创建Vip！
//    @Test
//    public void test_a_createAsync() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        //清除临时Vip数据，避免影响测试结果
//        Vip vip = new Vip();
//        vip.setSql("where F_SyncDatetime = ?");
//        vip.setConditions(new String[]{"0"});
//        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
//        List<Vip> vipList = (List<Vip>) vipPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, vip);
//        Assert.assertTrue("retrieveNSync返回的错误码不正确", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        if (vipList.size() > 0) {
//            for (Vip v : vipList) {
//                vipPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, v);
//                Assert.assertTrue("deleteSync返回的错误码不正确", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//                Vip v1 = (Vip) vipPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, v);
//                Assert.assertTrue("retrieve1Sync返回的错误码不正确", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//                Assert.assertTrue("deleteSync失败", v1 == null);
//            }
//        }
//
//        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
//            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
//        }
//        Assert.assertTrue("查找最大ID，错误码不正确！", vipPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        this.vip = DataInput.getVip();
//        this.vip.setInt1(1);
//        //
//        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
//        vipSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        if (!vipSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, this.vip)) {
//            Assert.assertTrue("创建失败！" + vipSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
//        }
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            Assert.assertTrue("创建超时！", false);
//        }
//        vipNew = (Vip) vipSQLiteEvent.getBaseModel1();
//        vipNew.setIgnoreIDInComparision(true);
//        Assert.assertTrue("创建的与服务器返回的不一致！", vipNew.compareTo(this.vip) == 0);
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_b1_retrieveNCAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常case
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        Vip vip = new Vip();
        vip.setCategory(1);
        vip.setMobile("13545678110");
        //
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (!vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.fail("查询VIP失败！" + vipHttpBO.getHttpEvent().printErrorInfo());
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertTrue(bmList.size() > 0, "查询VIP数据不符合！");
    }

    @Test
    public void test_b2_retrieveNCAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：查询不合法的电话号码
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        Vip vip = new Vip();
        vip.setCategory(1);
        vip.setMobile("1354000000000000000000000000");
        //
        vipHttpEvent.setListMasterTable(null);
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.fail("查询VIP失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertSame(vipHttpEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "错误码不正确");
        Assert.assertNull(bmList, "查询VIP数据不符合！");
    }

    @Test
    public void test_c_RetrieveNVipConsumeHistory() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        Vip vip = new Vip();
        vip.setID(1);
        //
        if (!vipHttpBO.retrieveNVipConsumeHistoryEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertTrue(bmList.size() > 0, "查询VIP数据不符合！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    // TODO 没有VipSyncAction了，是否注释
//    @Test
//    public void test_d_retrieveNAsync() throws InterruptedException {
//        Shared.printTestMethodStartInfo();
//
//        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
//            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
//        }
//
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Assert.assertTrue("查询失败！", false);
//        }
//        long lTimeOut = 1000;
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("查询超时！", false);
//        }
//        List<Vip> vipList = (List<Vip>) vipHttpEvent.getListMasterTable();
//
//        for (int i = 0; i < vipList.size(); i++) {
//            vipIDs = vipIDs + "," + vipList.get(i).getID();
//        }
//        if (vipIDs.length() > 2) {
//            vipIDs = vipIDs.substring(1, vipIDs.length());
//        }
//    }

    @Test
    public void test_e_feedback() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        if (!vipHttpBO.feedback(vipIDs)) {
            Assert.fail("feedback 失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("feedback 超时！");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    // TODO 是否注释
    // 不能在Pos机上创建Vip！
//    @Test
//    public void test_f_deleteASync() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
//            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
//        }
//
//        Vip vip = DataInput.getVip();
//        vip.setInt1(1);
//        //
//        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
//        vipSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        if (!vipSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, vip)) {
//            Assert.assertTrue("创建失败！", false);
//        }
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
//                vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            Assert.assertTrue("创建超时！", false);
//        }
//        Vip vip2 = (Vip) vipSQLiteEvent.getBaseModel1();
//        vip2.setIgnoreIDInComparision(true);
//        Assert.assertTrue("创建的与服务器返回的不一致！", vip2.compareTo(vip) == 0);
//
//
//        vipHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_Vip_Delete);
//        vipHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!vipHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, vip2)) {
//            Assert.assertTrue("delete 失败！", false);
//        }
//
//        lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("delete 超时！", false);
//        }
//        Assert.assertTrue("删除失败", vipHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//    }

    @Test
    public void test_g1_retrieveNByMobileOrVipCardSN() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        // case1:使用手机号码查询会员相关信息
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        Vip vip = new Vip();
        vip.setMobile(MOBILE);
        vip.setCategory(1);
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        Vip vipCreate1 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertTrue(vipCreate1 != null && vipCreate1.getMobile().equals(MOBILE), "查询VIP失败，vip为空！");

        //查询另一个存在会员，检查BaseModel是否更新
        vip.setMobile(MOBILE2);
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }

        Vip vipCreate2 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertTrue(vipCreate2 != null && vipCreate2.getMobile().equals(MOBILE2), "查询VIP失败，vip为空！");

        //查询一个不存在的会员，检查BaseModel是否为null
        vip.setMobile(EROOR_MOBILE);
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }

        Vip vipCreate3 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertNull(vipCreate3, "测试失败，BaseModel中残留了上次查询的结果");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_g2_retrieveNByMobileOrVipCardSN() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        // case2:使用VipCardSN查询会员相关信息
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        Vip vip = new Vip();
        vip.setVipCardSN(VIP_CARD_SN);
        vip.setCategory(1);
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        BaseModel bm = vipHttpEvent.getBaseModel1();
        Assert.assertNotNull(bm, "查询VIP失败，vip为空！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    public void test_g3_retrieveNByMobileOrVipCardSN() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        // case3:手机号码和VipCardSN为null
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.fail("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        Vip vip = new Vip();
        vip.setMobile("11111111111");
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.fail("查询VIP失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        BaseModel bm = vipHttpEvent.getBaseModel1();
        Assert.assertNull(bm, "传一个不存在的手机号，服务器返回有数据！");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }
}
