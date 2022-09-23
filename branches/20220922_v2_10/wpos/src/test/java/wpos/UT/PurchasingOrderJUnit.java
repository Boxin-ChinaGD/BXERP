package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.PurchasingOrder;
import wpos.model.PurchasingOrderCommodity;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static wpos.utils.StringUtils.random;

public class PurchasingOrderJUnit extends BaseHttpTestCase {
    @Resource
    private PurchasingOrderHttpBO purchasingOrderHttpBO;
    @Resource
    private PurchasingOrderSQLiteBO purchasingOrderSQLiteBO;
    @Resource
    private PurchasingOrderHttpEvent purchasingOrderHttpEvent;
    @Resource
    private PurchasingOrderSQLiteEvent purchasingOrderSQLiteEvent;
    //
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_PurchasingOrderJUnit = 10000;

    private static long purchasingOrderID = 0;

    @BeforeClass
    public void setUp() {
        super.setUp();
        purchasingOrderHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        purchasingOrderSQLiteEvent.setId(Event_ID_PurchasingOrderJUnit);
        purchasingOrderHttpBO.setHttpEvent(purchasingOrderHttpEvent);
        purchasingOrderHttpBO.setSqLiteEvent(purchasingOrderSQLiteEvent);
        purchasingOrderSQLiteBO.setHttpEvent(purchasingOrderHttpEvent);
        purchasingOrderSQLiteBO.setSqLiteEvent(purchasingOrderSQLiteEvent);
        purchasingOrderHttpEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderHttpEvent.setSqliteBO(purchasingOrderSQLiteBO);
        purchasingOrderSQLiteEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderSQLiteEvent.setSqliteBO(purchasingOrderSQLiteBO);
        //
        posLoginHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(Event_ID_PurchasingOrderJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        try {
            Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPurchasingOrderHttpEvent(PurchasingOrderHttpEvent event){
        if (event.getId() == Event_ID_PurchasingOrderJUnit){
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
            Assert.assertTrue( false,"请求创建采购单失败！！" + purchasingOrderHttpBO.getHttpEvent().getLastErrorCode());
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue( false,"创建超时!");
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue( po != null,"服务器返回的对象是空的");
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue(purchasingOrder.compareTo(po) == 0,"创建采购单失败！compareTo失败");

        purchasingOrderID = po.getID();
    }

    @Test
    public void test_b_Approver() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        // 创建一个采购订单用以审核
        PurchasingOrder purchasingOrder = DataInput.getPurchasingOrder();

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)){
            Assert.assertTrue( false,"请求创建采购单失败！！" + purchasingOrderHttpBO.getHttpEvent().getLastErrorMessage());
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue(false,"创建超时!");
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue(po != null,"服务器返回的对象是空的");
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue(purchasingOrder.compareTo(po) == 0,"创建采购单失败！compareTo失败");

        // 审核创建的采购订单
        po.setApproverID(4);
        if (!purchasingOrderHttpBO.updateAsync(BaseHttpBO.CASE_PurchasingOrder_Approve, po)){
            Assert.assertTrue( false,"审核失败！");
        }

        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue( false,"审核超时!");
        }

        Assert.assertTrue( purchasingOrderHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError),"审核失败！！");
    }

    @Test
    public void test_c_Retreive1() throws InterruptedException {
        Shared.printTestMethodStartInfo();
        // 创建一个采购订单用以查询
        PurchasingOrder purchasingOrder = DataInput.getPurchasingOrder();

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)){
            Assert.assertTrue( false,"请求创建采购单失败！！" + purchasingOrderHttpBO.getHttpEvent().getLastErrorCode());
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue(false,"创建超时!");
        }

        PurchasingOrder po = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue( po != null,"服务器返回的对象是空的");
        purchasingOrder.setIgnoreIDInComparision(true);
        List<PurchasingOrderCommodity> list = (List<PurchasingOrderCommodity>) purchasingOrder.getListSlave1();
        for (PurchasingOrderCommodity pc : list){
            pc.setPurchasingOrderID(po.getID().intValue());
        }
        Assert.assertTrue( purchasingOrder.compareTo(po) == 0,"创建采购单失败！compareTo失败");

        //查询创建的采购订单
        if(!purchasingOrderHttpBO.retrieve1AsyncC(BaseSQLiteBO.INVALID_CASE_ID, po)){
            Assert.assertTrue(false,"查询采购订单失败");
        }

        lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }

        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue( false,"创建超时!");
        }

        PurchasingOrder p = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue(po.compareTo(p) == 0,"查询采购单失败！compareTo失败");

    }

}
