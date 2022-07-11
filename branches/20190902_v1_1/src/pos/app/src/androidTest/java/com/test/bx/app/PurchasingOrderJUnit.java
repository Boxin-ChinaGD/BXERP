package com.test.bx.app;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PurchasingOrderHttpBO;
import com.bx.erp.bo.PurchasingOrderSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PurchasingOrderHttpEvent;
import com.bx.erp.event.PurchasingOrderSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PurchasingOrder;
import com.bx.erp.model.PurchasingOrderCommodity;
import com.bx.erp.utils.Shared;
import com.base.BaseHttpAndroidTestCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.bx.erp.util.StringUitl.random;

public class PurchasingOrderJUnit extends BaseHttpAndroidTestCase {

    private static PurchasingOrderHttpBO purchasingOrderHttpBO = null;
    private static PurchasingOrderSQLiteBO purchasingOrderSQLiteBO = null;
    private static PurchasingOrderHttpEvent purchasingOrderHttpEvent = null;
    private static PurchasingOrderSQLiteEvent purchasingOrderSQLiteEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_PurchasingOrderJUnit = 10000;

    private static long purchasingOrderID = 0;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (purchasingOrderHttpEvent == null){
            purchasingOrderHttpEvent = new PurchasingOrderHttpEvent();
            purchasingOrderHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        }
        if (purchasingOrderSQLiteEvent == null){
            purchasingOrderSQLiteEvent = new PurchasingOrderSQLiteEvent();
            purchasingOrderSQLiteEvent.setId(Event_ID_PurchasingOrderJUnit);
        }
        if (purchasingOrderHttpBO == null){
            purchasingOrderHttpBO = new PurchasingOrderHttpBO(GlobalController.getInstance().getContext(), purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        if (purchasingOrderSQLiteBO == null){
            purchasingOrderSQLiteBO = new PurchasingOrderSQLiteBO(GlobalController.getInstance().getContext(), purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        purchasingOrderHttpEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderHttpEvent.setSqliteBO(purchasingOrderSQLiteBO);
        purchasingOrderSQLiteEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderSQLiteEvent.setSqliteBO(purchasingOrderSQLiteBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);


        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
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
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPurchasingOrderHttpEvent(PurchasingOrderHttpEvent event){
         if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
             StackTraceElement ste = new Exception().getStackTrace()[1];
             System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PurchasingOrderJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput{
        public static PurchasingOrder getPurchasingOrder(){
            PurchasingOrder purchasingOrder = new PurchasingOrder();
            purchasingOrder.setProviderID(random.nextInt(10) + 1);
            purchasingOrder.setStaffID(4);//服务器默认用staffID=4的员工登录
//            purchasingOrder.setString1(String.valueOf(random.nextInt(10) + 1));
//            purchasingOrder.setString2(String.valueOf(random.nextInt(10) + 1));
//            purchasingOrder.setString3(String.valueOf(random.nextDouble()));
            purchasingOrder.setRemark("采购订单");

            PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
            purchasingOrderCommodity.setPurchasingOrderID(random.nextInt(10) + 1);
            purchasingOrderCommodity.setCommodityID(1);
            purchasingOrderCommodity.setCommodityName("沐浴露");
            purchasingOrderCommodity.setCommodityNO(random.nextInt(20) + 1);
            purchasingOrderCommodity.setBarcodeID(1);
            purchasingOrderCommodity.setPriceSuggestion(10f);

            List<PurchasingOrderCommodity> purchasingOrderCommodities = new ArrayList<PurchasingOrderCommodity>();
            purchasingOrderCommodities.add(purchasingOrderCommodity);

            purchasingOrder.setListSlave1(purchasingOrderCommodities);

            return purchasingOrder;
        }
    }


    @Test
    public void test_a_Create() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        //...测试多了就创建一个类部类进行获取PurchasingInfo
        PurchasingOrder purchasingOrder = DataInput.getPurchasingOrder();

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)){
            Assert.assertTrue("请求创建采购单失败！！", false);
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("创建超时!", false);
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象是空的", po != null);
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue("创建采购单失败！compareTo失败", purchasingOrder.compareTo(po) == 0);

        purchasingOrderID = po.getID();
    }

    @Test
    public void test_b_Approver() throws InterruptedException {
        Shared.printTestMethodStartInfo();
       // 创建一个采购订单用以审核
        PurchasingOrder purchasingOrder = DataInput.getPurchasingOrder();

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)){
            Assert.assertTrue("请求创建采购单失败！！", false);
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("创建超时!", false);
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象是空的", po != null);
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue("创建采购单失败！compareTo失败", purchasingOrder.compareTo(po) == 0);

        // 审核创建的采购订单
        po.setApproverID(4);
        if (!purchasingOrderHttpBO.updateAsync(BaseHttpBO.CASE_PurchasingOrder_Approve, po)){
            Assert.assertTrue("审核失败！", false);
        }

        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("审核超时!", false);
        }

        Assert.assertTrue("审核失败！！", purchasingOrderHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError));
    }

    @Test
    public void test_c_Retreive1() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        // 创建一个采购订单用以查询
        PurchasingOrder purchasingOrder = DataInput.getPurchasingOrder();

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)){
            Assert.assertTrue("请求创建采购单失败！！", false);
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("创建超时!", false);
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象是空的", po != null);
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue("创建采购单失败！compareTo失败", purchasingOrder.compareTo(po) == 0);

        //查询创建的采购订单
        if(!purchasingOrderHttpBO.retrieve1AsyncC(BaseSQLiteBO.INVALID_CASE_ID, po)){
            Assert.assertTrue("查询采购订单失败", false);
        }

        lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }

        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("创建超时!", false);
        }

        PurchasingOrder p = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("查询采购单失败！compareTo失败", po.compareTo(p) == 0);

    }

}
