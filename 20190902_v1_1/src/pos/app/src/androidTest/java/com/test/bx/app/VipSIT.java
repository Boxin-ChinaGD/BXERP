package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.VipPresenter;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class VipSIT extends BaseHttpAndroidTestCase {
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
    //
    private static final int Event_ID_VipSIT = 10000;

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
            String mobile = "15654" + System.currentTimeMillis() % 1000000;
            vipInput.setMobile(mobile.length() == 11 ? mobile : mobile + "0");
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
        if (vipHttpEvent == null){
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(Event_ID_VipSIT);
        }
        if (vipSQLiteEvent == null){
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_VipSIT);
        }
        if (vipHttpBO == null){
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null){
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        vipHttpEvent.setHttpBO(vipHttpBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_VipSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_VipSIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_VipSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_VipSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_VipSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == Event_ID_VipSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == Event_ID_VipSIT){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    /**
     * 1.pos1创建Vip
     * 2.pos1修改Vip
     * 3.pos2 RN
     * 4.pos2 feedback
     * 5.pos1删除Vip
     * 6.pos2 RN
     * 7.pos2 feedback
     */
    // @Test 不需要同步块和同步，因为VIP消费时，是直接向服务器实时拿数据的
//    public void test_vip() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
//        System.out.println("------------------------POS1 Create Vip----------------------");
//        Vip vipOld = DataInput.getVip();
//        vipOld.setReturnObject(1);
//        //
//        vipHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!vipHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, vipOld)) {
//            Assert.assertTrue("创建失败！", false);
//        }
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("创建超时！", false);
//        }
//        Vip vipNew = (Vip) vipHttpBO.getHttpEvent().getBaseModel1();
//        vipNew.setIgnoreIDInComparision(true);
//        Assert.assertTrue("创建的与服务器返回的不一致！", vipNew.compareTo(vipOld) == 0);
//
//        System.out.println("------------------------POS1 Update Vip----------------------");
//        vipNew.setName("aaaaaa");
//        vipNew.setReturnObject(1);
//        vipHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_Vip_Update);
//        vipHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!vipHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, vipNew)) {
//            Assert.assertTrue("修改失败！", false);
//        }
//        lTimeOut = 50;
//        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("修改超时！", false);
//        }
//        Assert.assertTrue("模拟网页vip修改失败", vipHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//
//        System.out.println("------------------------POS2 第一次 RetrieveN----------------------");
//        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
//        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Assert.assertTrue("查询失败！", false);
//        }
//        lTimeOut = 5000;
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("查询超时！", false);
//        }
//        List<Vip> vipList = (List<Vip>) vipHttpEvent.getListMasterTable();
//        Assert.assertTrue("POS2 RN无数据", vipList != null);
//
//        String vipIDs = "";
//        for (int i = 0; i < vipList.size(); i++) {
//            vipIDs = vipIDs + "," + vipList.get(i).getID();
//        }
//        vipIDs = vipIDs.substring(1, vipIDs.length());
//
//        System.out.println("------------------------POS2 第一次 feedback----------------------");
//        if (!vipHttpBO.feedback(vipIDs)) {
//            Assert.assertTrue("feedback 失败！", false);
//        }
//        lTimeOut = 50;
//        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("feedback 超时！", false);
//        }
//
//        System.out.println("------------------------POS2 第二次 RetrieveN 无数据----------------------");
//        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Assert.assertTrue("查询失败！", false);
//        }
//        lTimeOut = 50;
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("查询超时！", false);
//        }
//        vipList = (List<Vip>) vipSQLiteEvent.getListMasterTable();
//        Assert.assertTrue("POS2 RN无数据", vipList == null);
//
//        System.out.println("------------------------POS Delete Vip----------------------");
//        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
//        vipHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        vipHttpBO.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_Vip_Delete);
//        if (!vipHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, vipNew)) {
//            Assert.assertTrue("delete 失败！", false);
//        }
//        lTimeOut = 50;
//
//        while (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//
//        if (vipHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("delete 超时！", false);
//        }
//
//        System.out.println("------------------------POS2 第三次 RetrieveN----------------------");
//        Shared.login(2l, posLoginHttpBO, staffLoginHttpBO);
//        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Assert.assertTrue("查询失败！", false);
//        }
//        lTimeOut = 50;
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            Assert.assertTrue("查询超时！", false);
//        }
//        vipList = (List<Vip>) vipSQLiteEvent.getListMasterTable();
//        Assert.assertTrue("POS2 RN无数据", vipList != null && vipList.size() == 1);
//
//        vipIDs = "";
//        for (int i = 0; i < vipList.size(); i++) {
//            vipIDs = vipIDs + "," + vipList.get(i).getID();
//        }
//        vipIDs = vipIDs.substring(1, vipIDs.length());
//
//        System.out.println("------------------------POS2 第二次 feedback----------------------");
//        if (!vipHttpBO.feedback(vipIDs)) {
//            Assert.assertTrue("feedback 失败！", false);
//        }
//        lTimeOut = 50;
//        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("feedback 超时！", false);
//        }
//    }
}
