package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allController.BaseController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WarehousingJUnit extends BaseHttpTestCase {
    private static WarehousingHttpBO warehousingHttpBO;
    private static WarehousingHttpEvent warehousingHttpEvent;
    private static WarehousingSQLiteEvent warehousingSQLiteEvent;
    private static WarehousingSQLiteBO warehousingSQLiteBO;
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

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();

        if (warehousingHttpEvent == null) {
            warehousingHttpEvent = new WarehousingHttpEvent();
            warehousingHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (warehousingSQLiteEvent == null) {
            warehousingSQLiteEvent = new WarehousingSQLiteEvent();
            warehousingSQLiteEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (warehousingHttpBO == null) {
            warehousingHttpBO = new WarehousingHttpBO(warehousingSQLiteEvent, warehousingHttpEvent);
        }
        if (warehousingSQLiteBO == null) {
            warehousingSQLiteBO = new WarehousingSQLiteBO(warehousingSQLiteEvent, warehousingHttpEvent);
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
            purchasingOrderHttpBO = new PurchasingOrderHttpBO(purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        if (purchasingOrderSQLiteBO == null) {
            purchasingOrderSQLiteBO = new PurchasingOrderSQLiteBO(purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
        }
        purchasingOrderHttpEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderHttpEvent.setSqliteBO(purchasingOrderSQLiteBO);
        purchasingOrderSQLiteEvent.setHttpBO(purchasingOrderHttpBO);
        purchasingOrderSQLiteEvent.setSqliteBO(purchasingOrderSQLiteBO);
//        if (barcodesSQLiteBO == null){
//            barcodesSQLiteBO =  new BarcodesSQLiteBO( null, null);
//        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_WarehousingJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_WarehousingJUnit);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        logoutHttpEvent.setId(Event_ID_WarehousingJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        try {
            Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWarehousingHttpEvent(WarehousingHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPurchasingOrderHttpEvent(PurchasingOrderHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodeSQLiteEvent(BarcodesSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_WarehousingJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

//    @Test
//    public void test_a_RetrieveN() throws InterruptedException {
//        Warehousing warehousing = new Warehousing();
//        warehousing.setPurchasingOrderID(1);
//        if (!warehousingHttpBO.retrieveNAsyncC(warehousing)){
//            Assert.assertTrue("??????RetrieveN????????????", false);
//        }
//
//        long lTimeout = 50;
//        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeout-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("??????retrieveNAsyncC??????!", false);
//        }
//
//        Assert.assertTrue("????????????", warehousingHttpBO.getHttpEvent().getListMasterTable().size() > 0);
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
        purchasingOrderCommodity.setCommodityName("?????????");
        purchasingOrderCommodity.setCommodityNO(10);
        purchasingOrderCommodity.setBarcodeID(1);
        purchasingOrderCommodity.setPriceSuggestion(10);
        purchasingOrderCommodity.setPurchasingOrderID(random.nextInt(10) + 1);

        List<PurchasingOrderCommodity> purchasingOrderCommodities = new ArrayList<>();
        purchasingOrderCommodities.add(purchasingOrderCommodity);

        purchasingOrder.setListSlave1(purchasingOrderCommodities);

        if (!purchasingOrderHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, purchasingOrder)) {
            Assert.fail("?????????????????????????????????");
        }

        long lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("????????????!");
        }

        purchasingOrder = (PurchasingOrder) purchasingOrderHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertNotNull(purchasingOrder, "?????????????????????????????????");

        purchasingOrderHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        purchasingOrder.setApproverID(BaseController.retailTradeAggregation.getStaffID());
        if (!purchasingOrderHttpBO.updateAsync(BaseHttpBO.CASE_PurchasingOrder_Approve, purchasingOrder)) {
            Assert.fail("???????????????");
        }

        lTimeout = 50;
        while (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                purchasingOrderHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("????????????!");
        }

        Assert.assertEquals(ErrorInfo.EnumErrorCode.EC_NoError, purchasingOrderHttpBO.getHttpEvent().getLastErrorCode(), "??????????????????");

        return purchasingOrder;
    }

    @Test
    public void test_b_Create() throws Exception {
//        Assert.assertTrue("???????????????",Shared.login(1l, posLoginHttpBO, staffLoginHttpBO));
        Random r = new Random();

        //?????????????????????????????????
        List<WarehousingCommodity> warehousingCommodities = new ArrayList<>();
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

        warehousingCommodity.setNO(r.nextInt(20));
        warehousingCommodity.setPrice((r.nextDouble()));
        warehousingCommodity.setCommodityID(10);//?????????hardcode??????nbr?????????????????????????????????????????????
        warehousingCommodity.setBarcodeID(14);//???????????????
        warehousingCommodity.setAmount(r.nextDouble() + 1000);
        warehousingCommodity.setCommodityName("?????????????????????");
        warehousingCommodity.setWarehousingID(1);
        warehousingCommodity.setShelfLife(10);
        warehousingCommodities.add(warehousingCommodity);

        Warehousing warehousing = new Warehousing();
        warehousing.setListSlave1(warehousingCommodities);
        //
        warehousing.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        PurchasingOrder purchasing = getPurchasingOrder();
        warehousing.setPurchasingOrderID(purchasing.getID());
        warehousing.setWarehouseID(Constants.WAREHOUSE_ID_Default);
        warehousing.setProviderID((long) Constants.WAREHOUSE_ID_Default);
        //
        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, warehousing)) {
            Assert.fail("??????Warehousing_Create??????");
        }

        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("??????createAsync??????!");
        }

        Warehousing warehousing2 = (Warehousing) warehousingHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertNotNull(warehousing2, "?????????????????????????????????????????????");

        warehousingID = warehousing2.getID();
    }

    @Test
    public void test_c_Approver() throws Exception {
        PurchasingOrder purchasingOrder = getPurchasingOrder();
        Warehousing warehousing = new Warehousing();
        warehousing.setID(createWarehousing().getID());
        warehousing.setPurchasingOrderID(purchasingOrder.getID());
        warehousing.setApproverID(4);

        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.updateAsync(BaseHttpBO.CASE_Warehousing_Approve, warehousing)) {
            Assert.fail("warehousing?????? ????????????");
        }

        long lTimeout = 50;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("??????createAsync??????!");
        }

        Assert.assertEquals(ErrorInfo.EnumErrorCode.EC_NoError, warehousingHttpBO.getHttpEvent().getLastErrorCode(), "??????????????????");
    }

    private Warehousing createWarehousing() throws Exception {
        Random r = new Random();

        //?????????????????????????????????
        List<WarehousingCommodity> warehousingCommodities = new ArrayList<>();
        WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

        warehousingCommodity.setNO(r.nextInt(20));
        warehousingCommodity.setPrice((r.nextDouble()));
        warehousingCommodity.setCommodityID(10);//?????????hardcode??????nbr?????????????????????????????????????????????
        warehousingCommodity.setBarcodeID(14);//???????????????
        warehousingCommodity.setAmount(r.nextDouble() + 1000);
        warehousingCommodity.setWarehousingID(1);
        warehousingCommodity.setShelfLife(10);
        warehousingCommodity.setCommodityName("?????????????????????");
        warehousingCommodities.add(warehousingCommodity);

        Warehousing warehousing = new Warehousing();
        warehousing.setListSlave1(warehousingCommodities);
        //
        warehousing.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        PurchasingOrder purchasing = getPurchasingOrder();
        warehousing.setPurchasingOrderID(purchasing.getID());
        warehousing.setWarehouseID(Constants.WAREHOUSE_ID_Default);
        warehousing.setProviderID((long) Constants.WAREHOUSE_ID_Default);
        //
        warehousingHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!warehousingHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, warehousing)) {
            Assert.fail("??????Warehousing_Create??????" + warehousingHttpBO.getHttpEvent().printErrorInfo());
        }

        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                warehousingHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("??????createAsync??????!");
        }

        Warehousing warehousing2 = (Warehousing) warehousingHttpBO.getHttpEvent().getBaseModel1();
        Assert.assertNotNull(warehousing2, "?????????????????????????????????????????????");

        return warehousing2;
    }
}
