package com.test.bx.app;


import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PurchasingOrderHttpBO;
import com.bx.erp.bo.PurchasingOrderSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.WarehousingHttpBO;
import com.bx.erp.bo.WarehousingSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PurchasingOrderHttpEvent;
import com.bx.erp.event.PurchasingOrderSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.WarehousingHttpEvent;
import com.bx.erp.event.WarehousingSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PurchasingOrder;
import com.bx.erp.model.PurchasingOrderCommodity;
import com.bx.erp.model.Warehousing;
import com.bx.erp.model.WarehousingCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WarehousingJUnit extends BaseHttpAndroidTestCase {
    private static WarehousingHttpBO warehousingHttpBO;
    private static WarehousingHttpEvent warehousingHttpEvent;
    private static WarehousingSQLiteEvent warehousingSQLiteEvent;
    private static WarehousingSQLiteBO warehousingSQLiteBO;
    private static CommodityPresenter commodityPresenter;
    private static PurchasingOrderHttpBO purchasingOrderHttpBO = null;
    private static PurchasingOrderSQLiteBO purchasingOrderSQLiteBO = null;
    private static PurchasingOrderHttpEvent purchasingOrderHttpEvent = null;
    private static PurchasingOrderSQLiteEvent purchasingOrderSQLiteEvent = null;
    //    private static BarcodesSQLiteBO barcodesSQLiteBO;
//    private static BarcodesPresenter barcodesPresenter;
    protected static PurchasingOrder po;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;
    //
    private static List<Commodity> commodities = new ArrayList<Commodity>();
    private static long warehousingID = 0;
    private static final int purchasingOrderID = 7;

    private static final int Event_ID_WarehousingJUnit = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();

        if (warehousingHttpEvent == null) {
            warehousingHttpEvent = new WarehousingHttpEvent();
            warehousingHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (warehousingSQLiteEvent == null) {
            warehousingSQLiteEvent = new WarehousingSQLiteEvent();
            warehousingSQLiteEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (warehousingHttpBO == null) {
            warehousingHttpBO = new WarehousingHttpBO(GlobalController.getInstance().getContext(), warehousingSQLiteEvent, warehousingHttpEvent);
        }
        if (warehousingSQLiteBO == null) {
            warehousingSQLiteBO = new WarehousingSQLiteBO(GlobalController.getInstance().getContext(), warehousingSQLiteEvent, warehousingHttpEvent);
        }
        warehousingHttpEvent.setHttpBO(warehousingHttpBO);
        warehousingHttpEvent.setSqliteBO(warehousingSQLiteBO);
        warehousingSQLiteEvent.setHttpBO(warehousingHttpBO);
        warehousingSQLiteEvent.setSqliteBO(warehousingSQLiteBO);
        //
        if (purchasingOrderHttpEvent == null) {
            purchasingOrderHttpEvent = new PurchasingOrderHttpEvent();
            purchasingOrderHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (purchasingOrderSQLiteEvent == null) {
            purchasingOrderSQLiteEvent = new PurchasingOrderSQLiteEvent();
            purchasingOrderSQLiteEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (purchasingOrderHttpBO == null) {
            purchasingOrderHttpBO = new PurchasingOrderHttpBO(GlobalController.getInstance().getContext(), purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        if (purchasingOrderSQLiteBO == null) {
            purchasingOrderSQLiteBO = new PurchasingOrderSQLiteBO(GlobalController.getInstance().getContext(), purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        purchasingOrderHttpEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderHttpEvent.setSqliteBO(purchasingOrderSQLiteBO);
        purchasingOrderSQLiteEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderSQLiteEvent.setSqliteBO(purchasingOrderSQLiteBO);
//        if (barcodesSQLiteBO == null){
//            barcodesSQLiteBO =  new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), null, null);
//        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_WarehousingJUnit);
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
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWarehousingHttpEvent(WarehousingHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPurchasingOrderHttpEvent(PurchasingOrderHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodeSQLiteEvent(BarcodesSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

//    @Test
//    public void test_a_RetrieveN() throws InterruptedException {
//        Warehousing warehousing = new Warehousing();
//        warehousing.setPurchasingOrderID(1);
//        if (!warehousingHttpBO.retrieveNAsyncC(warehousing)){
//            Assert.assertTrue("调用RetrieveN失败！！", false);
//        }
//
//        long lTimeout = 50;
//        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeout-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("请求retrieveNAsyncC超时!", false);
//        }
//
//        Assert.assertTrue("无数据！", warehousingHttpBO.getHttpEvent().getListMasterTable().size() > 0);
//        commodities = (List<Commodity>) warehousingHttpBO.getHttpEvent().getListMasterTable();
//    }

    protected PurchasingOrder getPurchasingOrder() throws InterruptedException {
        Random random = new Random();
        PurchasingOrder purchasingOrder = new PurchasingOrder();
        purchasingOrder.setProviderID(1);
        purchasingOrder.setStaffID(4);
//        purchasingOrder.setString1(String.valueOf(random.nextInt(5) + 1));
//        purchasingOrder.setString2(String.valueOf(random.nextInt(10) + 1));
//        purchasingOrder.setString3(String.valueOf(random.nextDouble()));

        PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
        purchasingOrderCommodity.setCommodityID(1);
        purchasingOrderCommodity.setCommodityName("沐浴露");
        purchasingOrderCommodity.setCommodityNO(10);
        purchasingOrderCommodity.setBarcodeID(1);
        purchasingOrderCommodity.setPriceSuggestion(10);
        purchasingOrderCommodity.setPurchasingOrderID(random.nextInt(10) + 1);

        List<PurchasingOrderCommodity> purchasingOrderCommodities = new ArrayList<PurchasingOrderCommodity>();
        purchasingOrderCommodities.add(purchasingOrderCommodity);

        purchasingOrder.setListSlave1(purchasingOrderCommodities);

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)) {
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

        purchasingOrder = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的对象是空的", purchasingOrder != null);

        purchasingOrderHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        purchasingOrder.setApproverID(BaseActivity.retailTradeAggregation.getStaffID());
        if (!purchasingOrderHttpBO.updateAsync(BaseHttpBO.CASE_PurchasingOrder_Approve, purchasingOrder)) {
            Assert.assertTrue("审核失败！", false);
        }

        lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("审核超时!", false);
        }

        Assert.assertTrue("审核失败！！", purchasingOrderHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError));

        return purchasingOrder;
    }

    @Test
    public void test_b_Create() throws Exception {
//        Assert.assertTrue("登录失败！",Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        Random r = new Random();

        //有需要再生成一个类部类
        List<WarehousingCommodity> warehousingCommodities = new ArrayList<WarehousingCommodity>();
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

        warehousingCommodity.setNO(r.nextInt(20));
        warehousingCommodity.setPrice((r.nextDouble()));
        warehousingCommodity.setCommodityID(10);//数据已hardcode，若nbr数据库中此数据修改可能导致出错
        warehousingCommodity.setBarcodeID(14);//数据已写死
        warehousingCommodity.setAmount(r.nextDouble() + 1000);
        warehousingCommodity.setCommodityName("润泽玻尿酸面膜");
        warehousingCommodity.setWarehousingID(1);
        warehousingCommodity.setShelfLife(10);
        warehousingCommodities.add(warehousingCommodity);

        Warehousing warehousing = new Warehousing();
        warehousing.setListSlave1(warehousingCommodities);
        //
        warehousing.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        PurchasingOrder purchasing = getPurchasingOrder();
        warehousing.setPurchasingOrderID(purchasing.getID().intValue());
        warehousing.setWarehouseID(Constants.WAREHOUSE_ID_Default);
        warehousing.setProviderID(Long.valueOf(Constants.WAREHOUSE_ID_Default));
        //
        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, warehousing)) {
            Assert.assertTrue("调用Warehousing_Create失败", false);
        }

        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("请求createAsync超时!", false);
        }

        Warehousing warehousing2 = (Warehousing) warehousingHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的创建对象是为空！！", warehousing2 != null);

        warehousingID = warehousing2.getID();
    }

    @Test
    public void test_c_Approver() throws Exception {
        PurchasingOrder purchasingOrder = getPurchasingOrder();
        Warehousing warehousing = new Warehousing();
        warehousing.setID(createWarehousing().getID());
        warehousing.setPurchasingOrderID(purchasingOrder.getID().intValue());
        warehousing.setApproverID(4);

        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.updateAsync(BaseHttpBO.CASE_Warehousing_Approve, warehousing)) {
            Assert.assertTrue("warehousing审核 失败！！", false);
        }

        long lTimeout = 50;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("请求createAsync超时!", false);
        }

        Assert.assertTrue("审核失败！！", warehousingHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError));
    }

    private Warehousing createWarehousing() throws Exception {
        Random r = new Random();

        //有需要再生成一个类部类
        List<WarehousingCommodity> warehousingCommodities = new ArrayList<WarehousingCommodity>();
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

        warehousingCommodity.setNO(r.nextInt(20));
        warehousingCommodity.setPrice((r.nextDouble()));
        warehousingCommodity.setCommodityID(10);//数据已hardcode，若nbr数据库中此数据修改可能导致出错
        warehousingCommodity.setBarcodeID(14);//数据已写死
        warehousingCommodity.setAmount(r.nextDouble() + 1000);
        warehousingCommodity.setWarehousingID(1);
        warehousingCommodity.setShelfLife(10);
        warehousingCommodity.setCommodityName("润泽玻尿酸面膜");
        warehousingCommodities.add(warehousingCommodity);

        Warehousing warehousing = new Warehousing();
        warehousing.setListSlave1(warehousingCommodities);
        //
        warehousing.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        PurchasingOrder purchasing = getPurchasingOrder();
        warehousing.setPurchasingOrderID(purchasing.getID().intValue());
        warehousing.setWarehouseID(Constants.WAREHOUSE_ID_Default);
        warehousing.setProviderID(Long.valueOf(Constants.WAREHOUSE_ID_Default));
        //
        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, warehousing)) {
            Assert.assertTrue("调用Warehousing_Create失败", false);
        }

        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("请求createAsync超时!", false);
        }

        Warehousing warehousing2 = (Warehousing) warehousingHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("服务器返回的创建对象是为空！！", warehousing2 != null);

        return warehousing2;
    }
}
