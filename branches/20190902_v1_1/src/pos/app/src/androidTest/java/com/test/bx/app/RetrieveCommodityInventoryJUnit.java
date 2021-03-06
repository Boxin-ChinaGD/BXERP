package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class RetrieveCommodityInventoryJUnit extends BaseHttpAndroidTestCase {
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteEvent commoditySQliteEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    //
    private static BarcodesPresenter barcodesPresenter = null;

    private List<Commodity> commodityList = new ArrayList<>();
    private static final int EVENT_ID_RetrieveCommodityInventoryJUnit = 10000;

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

        if (commoditySQliteEvent == null) {
            commoditySQliteEvent = new CommoditySQLiteEvent();
            commoditySQliteEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQliteEvent, commodityHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQliteEvent, commodityHttpEvent);
        }
        commoditySQliteEvent.setHttpBO(commodityHttpBO);
        commoditySQliteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);

        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();

        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
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
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetrieveCommodityInventoryJUnit.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetrieveCommodityInventoryJUnit.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeJUnit onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetrieveCommodityInventoryJUnit.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetrieveCommodityInventoryJUnit.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }


    /**
     * 1.??????
     * 2.?????????????????????????????????
     * 3.????????????????????????????????????
     * 4.???????????????????????????
     * 5.??????????????????????????????
     *
     * @throws Exception
     */
    @Test
    public void test_a_retrieveInventory() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.??????
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(2L, posLoginHttpBO, staffLoginHttpBO));
        //???????????????????????????
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("??????????????????????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????", false);
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????????????????RetailTrade?????????", false);
        }
        //2.?????????????????????????????????
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade rt = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //3.????????????????????????????????????(????????????????????????????????????????????????)
        int[] commodityNOs1 = new int[rt.getListSlave1().size()];
        for (int i = 0; i < rt.getListSlave1().size(); i++) {
            retrieveCommodityInventory(searchBarcode(((RetailTradeCommodity) rt.getListSlave1().get(i)).getCommodityID()));
            commodityNOs1[i] = commodityList.get(0).getNO();
        }

        //4.???????????????????????????
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
            Assert.assertTrue("???????????????", false);
        }

        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("?????????????????????????????????", master != null);

        //?????????????????????????????????ID????????????FrameID??????????????????????????????        //????????????ssfOld????????????master
        rt.setIgnoreIDInComparision(true);
        Assert.assertTrue("??????????????????????????????????????????????????????", rt.compareTo(master) == 0);

        //3.????????????????????????????????????(????????????????????????????????????????????????)
        int[] commodityNOs2 = new int[rt.getListSlave1().size()];
        for (int i = 0; i < rt.getListSlave1().size(); i++) {
            retrieveCommodityInventory(searchBarcode(((RetailTradeCommodity) rt.getListSlave1().get(i)).getCommodityID()));
            commodityNOs2[i] = commodityList.get(0).getNO();
        }

        for (int i = 0; i < commodityNOs1.length; i++) {
            Assert.assertTrue("????????????????????????????????????", commodityNOs1[i] - ((RetailTradeCommodity) rt.getListSlave1().get(i)).getNO() == commodityNOs2[i]);
        }
    }

    @Test
    public void test_b_retrieveInventory() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(2L, posLoginHttpBO, staffLoginHttpBO));

        //??????case???????????????????????????????????????
        retrieveCommodityInventory("");
        Assert.assertTrue("?????????list???????????????0,check()?????????", commodityList.size() == 0);//???UI??????????????????????????????????????????????????????????????????

        //??????Case???????????????????????????????????????????????????
        retrieveCommodityInventory("00000000");
        Assert.assertTrue("?????????list???????????????0", commodityList.size() == 0);

        //??????case???????????????????????????????????????1-6
        retrieveCommodityInventoryError("696563");
        Assert.assertTrue("??????10???Commodity", commodityList.size() == 0);//???UI?????????????????????????????????????????????7?????????????????????????????????

        //??????case??????????????????????????????????????????7
        retrieveCommodityInventory("6965636");
        Assert.assertTrue("??????10???Commodity", commodityList.size() > 0);
    }

    /**
     * ???barcode????????????????????????????????????
     */
    private String searchBarcode(long commodityID) {
        Barcodes barcodes = new Barcodes();
        barcodes.setSql("where F_CommodityID = ?");
        barcodes.setConditions(new String[]{String.valueOf(commodityID)});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        return barcodesList.get(0).getBarcode();
    }

    private void retrieveCommodityInventory(String barcode) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setBarcode(barcode);
        commoditySQliteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, commodity)) {
            System.out.println("??????????????????????????????????????????");
            commodityList = new ArrayList<>();
            return;
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("?????????????????????????????????", lTimeOut > 0);
        commodityList = (List<Commodity>) commoditySQliteEvent.getListMasterTable();
    }

    private void retrieveCommodityInventoryError(String barcode) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setBarcode(barcode);
        commoditySQliteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, commodity)) {
            System.out.println("??????????????????????????????????????????");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("?????????????????????????????????", commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commodityList = (List<Commodity>) commodityHttpEvent.getListMasterTable();
    }
}
