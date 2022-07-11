package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.VipPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync;

public class VIPJUnitTest extends BaseHttpAndroidTestCase {
    public static final String MOBILE = "13545678110";
    public static final String MOBILE2 = "13545678111";
    public static final String EROOR_MOBILE = "11111111111";
    public static final String VIP_CARD_SN = "6688661234567891"; // 需要数据库真实存在的

    private static VipPresenter vipPresenter = null;
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

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static Vip vipInput = null;

        protected static final Vip getVip() throws Exception {
            vipInput = new Vip();
            vipInput.setID(vipPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
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

    @Override
    public void setUp() throws Exception {
        super.setUp();

        vipPresenter = GlobalController.getInstance().getVipPresenter();
        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(Event_ID_VipJUnitTest);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_VipJUnitTest);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
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
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_VipJUnitTest);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_VipJUnitTest);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_VipJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_b1_retrieveNCAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常case
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        Vip vip = new Vip();
        vip.setCategory(1);
        vip.setMobile("13545678110");
        //
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (!vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.assertTrue("查询VIP失败！" + vipHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertTrue("查询VIP数据不符合！", bmList.size() > 0);
    }

    @Test
    public void test_b2_retrieveNCAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //异常case：查询不合法的电话号码
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);

        Vip vip = new Vip();
        vip.setCategory(1);
        vip.setMobile("1354000000000000000000000000");
        //
        vipHttpEvent.setListMasterTable(null);
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertTrue("错误码不正确", vipHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
        Assert.assertTrue("查询VIP数据不符合！", bmList == null);
    }

    @Test
    public void test_c_RetrieveNVipConsumeHistory() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Vip vip = new Vip();
        vip.setID(1l);
        //
        if (!vipHttpBO.retrieveNVipConsumeHistoryEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        List<?> bmList = vipHttpEvent.getListMasterTable();
        Assert.assertTrue("查询VIP数据不符合！", bmList.size() > 0);
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

        if (!Shared.login(2l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        if (!vipHttpBO.feedback(vipIDs)) {
            Assert.assertTrue("feedback 失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("feedback 超时！", false);
        }
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
    public void test_g1_retrieveNByMobileOrVipCardSN() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        // case1:使用手机号码查询会员相关信息
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Vip vip = new Vip();
        vip.setMobile(MOBILE);
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        Vip vipCreate1 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertTrue("查询VIP失败，vip为空！", vipCreate1 != null && vipCreate1.getMobile().equals(MOBILE));

        //查询另一个存在会员，检查BaseModel是否更新
        vip.setMobile(MOBILE2);
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }

        Vip vipCreate2 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertTrue("查询VIP失败，vip为空！", vipCreate2 != null && vipCreate2.getMobile().equals(MOBILE2));

        //查询一个不存在的会员，检查BaseModel是否为null
        vip.setMobile(EROOR_MOBILE);
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }

        Vip vipCreate3 = (Vip) vipHttpEvent.getBaseModel1();
        Assert.assertTrue("测试失败，BaseModel中残留了上次查询的结果", vipCreate3 == null);
    }

    @Test
    public void test_g2_retrieveNByMobileOrVipCardSN() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        // case2:使用VipCardSN查询会员相关信息
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Vip vip = new Vip();
        vip.setVipCardSN(VIP_CARD_SN);
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        BaseModel bm = vipHttpEvent.getBaseModel1();
        Assert.assertTrue("查询VIP失败，vip为空！", bm != null);
    }

    public void test_g3_retrieveNByMobileOrVipCardSN() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        // case3:手机号码和VipCardSN为null
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "/t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "/t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        Vip vip = new Vip();
        vip.setMobile("11111111111");
        //
        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
            Assert.assertTrue("查询VIP失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        BaseModel bm = vipHttpEvent.getBaseModel1();
        Assert.assertTrue("传一个不存在的手机号，服务器返回有数据！", bm == null);
    }
}
