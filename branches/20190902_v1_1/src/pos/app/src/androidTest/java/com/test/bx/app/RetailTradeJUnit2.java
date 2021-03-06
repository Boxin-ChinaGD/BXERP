package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.RetailTradeCouponSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.sync.SyncThread;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradeCouponPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ???????????????????????????????????????RetailTrade??????????????????
 */
public class RetailTradeJUnit2 extends BaseHttpAndroidTestCase {
    private RetailTrade retailTrade = new RetailTrade();
    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    private RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
    private List<BaseModel> promotionList = new ArrayList<BaseModel>();
    private List<Commodity> commodityList = new ArrayList<Commodity>();
    private List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
    private List<BaseModel> tempRetailTradePromotingList = new ArrayList<BaseModel>();

    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;

    private RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    //
    private RetailTradePromotingPresenter retailTradePromotingPresenter = null;
    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter = null;
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    //
    private PromotionPresenter promotionPresenter = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    //
    private static RetailTradeCouponSQLiteBO retailTradeCouponSQLiteBO = null;
    private static RetailTradeCouponPresenter retailTradeCouponPresenter = null;
    //
    private CommodityPresenter commodityPresenter = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteEvent commoditySQliteEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;

    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    private int count;//call ??????action????????????????????????
    private int commodityRunTimes = 1;//????????????runTimes???????????????????????????????????????

    long lTimeOut = Shared.UNIT_TEST_TimeOut;
    private static final int EVENT_ID_RetailTradeJUnit2 = 10000;

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
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
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
        //
        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
        retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();

        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (retailTradePromotingSQLiteBO == null) {
            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        if (retailTradePromotingHttpBO == null) {
            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        //
        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();

        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        //
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();

        if (commoditySQliteEvent == null) {
            commoditySQliteEvent = new CommoditySQLiteEvent();
            commoditySQliteEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
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

        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (retailTradeCouponSQLiteBO == null) {
            retailTradeCouponSQLiteBO = new RetailTradeCouponSQLiteBO(GlobalController.getInstance().getContext(), null, null);
        }
        retailTradeCouponPresenter = GlobalController.getInstance().getRetailTradeCouponPresenter();

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeJUnit2);
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

    public static class DataInput {
        static RetailTradePresenter retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        static RetailTradeCommodityPresenter retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        static BarcodesPresenter barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();

        public static RetailTrade setRetailTrade(RetailTrade retailTrade, List<Commodity> commodityList) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            retailTrade.setID(maxRetailTradeIDInSQLite);
            retailTrade.setVipID(0);
            retailTrade.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailTrade.setSmallSheetID(1);
            retailTrade.setLogo("");
            retailTrade.setPaymentType(1);
            retailTrade.setAmount(2222d);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSourceID(-1);
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setPaymentAccount("");
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) maxRetailTradeIDInSQLite);
            retailTrade.setRemark("......");
            //
            long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityList.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                Commodity commodity = commodityList.get(i);
                //
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + i);
                retailTradeCommodity.setTradeID(retailTrade.getID());
                retailTradeCommodity.setCommodityID(commodity.getID().intValue());
                retailTradeCommodity.setNO((commodity.getCommodityQuantity()));
                retailTradeCommodity.setPriceOriginal(10);
                retailTradeCommodity.setDiscount(0.9);
                retailTradeCommodity.setPriceReturn(10);
                retailTradeCommodity.setNOCanReturn(10);
                //
                Barcodes barcodes = new Barcodes();
                barcodes.setSql("where F_CommodityID = ?");
                barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
                barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);

                List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
                Assert.assertTrue("?????????????????????", (barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && barcodesList != null && barcodesList.size() > 0));
                retailTradeCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());
                //
                retailTradeCommodityList.add(retailTradeCommodity);
            }
            retailTrade.setListSlave1(retailTradeCommodityList);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvnet(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeJUnit2.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeJUnit2 onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeJUnit2.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradePromotingHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeJUnit2.onRetailTradePromotingHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeJUnit2  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradePromotingSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeJUnit2.onRetailTradePromotingSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
//    @Test
    public void test_a_RetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_a_RetailTrade");
        //??????????????????????????????????????????????????????????????????????????????????????????????????????
        //????????????RetailTrade
        retailTradeSQLiteEvent.setHttpBO(null);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????????????????RetailTrade?????????", false);
        }
        List<RetailTrade> rList = (List<RetailTrade>) retailTradeSQLiteEvent.getListMasterTable();
        for (RetailTrade retailTrade : rList) {
            retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue("????????????RetailTrade?????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        System.out.println("--------------- ?????????????????????????????????");
        //????????????RetailTradePromoting
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_SyncDatetime = ?");
        retailTradePromoting.setConditions(new String[]{"0"});
        List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting);
        Assert.assertTrue("retrieveNSync???????????????????????????", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        if (retailTradePromotingList != null && retailTradePromotingList.size() > 0) {
            for (RetailTradePromoting rtp : retailTradePromotingList) {
                for (int i = 0; i < rtp.getListSlave1().size(); i++) {
                    retailTradePromotingFlowPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) rtp.getListSlave1().get(i));
                    Assert.assertTrue("????????????RetailTradePromotingFlow?????????", retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
                retailTradePromotingPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                Assert.assertTrue("????????????RetailTradePromoting?????????", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        System.out.println("--------------- ????????????????????????????????????");
        //
        rList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < rList.size(); i++) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + rList.get(i).toString());
        }
        retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < retailTradePromotingList.size(); i++) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + retailTradePromotingList.get(i).toString());
        }

        System.out.println("--------------- ??????????????????");
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        // ??????????????????
        Commodity resetCommodity = new Commodity();
        resetCommodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        resetCommodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);//...
        Shared.retrieveNCBaseModel(resetCommodity, commodityHttpBO, commoditySQliteEvent, commoditySQLiteBO);
        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        //??????ID????????????????????????List
        Commodity commodity = getCommodityInSQLiteByID(161);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("???????????????????????????????????????", retailTradePromoting.getListSlave1().size() <= 0);

        //??????RetailTradePromoting ID,TradeID????????????RetailTradePromotingFlow ID
        setRetailTradePromotingID(retailTradePromoting);
        //RetailTrade????????????SQLite
        createTempRetailTradeInSQLite(retailTrade);
        //RetailTradePromoting????????????SQLite
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(false);

        promotionList.clear();
        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
//    @Test
    public void test_b_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_b_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();
        //????????????????????????????????????
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }

        System.out.println("---------------------????????????????????????-----------------------");
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(2);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        System.out.println("------------------????????????????????????--------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("???????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     */
//    @Test
    public void test_c_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_c_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("---------------------????????????????????????--------------------");
        Commodity commodity = getCommodityInSQLiteByID(101);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(100);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("?????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() == 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        System.out.println("-------------------------????????????????????????-------------------------");
        //????????????????????????????????????
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }
        commodityList.clear();

        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(105);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ??????????????????????????????????????????
     */
//    @Test
    public void test_d_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_d_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();
        //????????????????????????????????????
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }

        Commodity commodity = getCommodityInSQLiteByID(111);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        //??????RetailTradePromoting ID,TradeID????????????RetailTradePromotingFlow ID
        setRetailTradePromotingID(retailTradePromoting);
        //RetailTrade????????????SQLite
        createTempRetailTradeInSQLite(retailTrade);
        //RetailTradePromoting????????????SQLite
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
//    @Test
    public void test_e_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_e_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("--------------------????????????????????????---------------------");
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(2);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("??????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        List<Commodity> cs = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        System.out.println("--------------------????????????????????????----------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(44);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("??????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
//    @Test
    public void test_f_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_f_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
//    @Test
    public void test_g_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_g_RetailTrade");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("------------------????????????????????????--------------------");
        Commodity commodity = getCommodityInSQLiteByID(150);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(151);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("??????????????????????????????????????????", retailTradePromoting.getListSlave1().size() == 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        System.out.println("--------------------????????????????????????-------------------");
        commodity = getCommodityInSQLiteByID(140);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(141);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("??????????????????????????????????????????", retailTradePromoting.getListSlave1().size() == 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(false);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    @Test
    public void test_H_RetailTradeCoupon() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_H_RetailTradeCoupon????????????coupon????????????");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        //?????????????????????
        retailTrade = DataInput.setRetailTrade(retailTrade, commodityList);
        createTempRetailTradeInSQLite(retailTrade);
        //??????????????????
        retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setTradeID(retailTrade.getID().intValue());
        RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
        retailTradePromotingFlow.setProcessFlow(".....");
        List<BaseModel> retailTradePromotingFlowList = new ArrayList<>();
        retailTradePromotingFlowList.add(retailTradePromotingFlow);
        retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        //???????????????????????????????????????
        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
        retailTradeCoupon.setID(retailTradeCouponPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
        retailTradeCoupon.setCouponCodeID(1);
        retailTradeCoupon.setRetailTradeID(retailTrade.getID().intValue());
        createRetailTradeCouponInSQLite(retailTradeCoupon);
        //???????????????
        checkTempRetailTradeAndRetailTradePromoting(true);
    }

    private void createRetailTradeCouponInSQLite(RetailTradeCoupon retailTradeCoupon) {
        RetailTradeCoupon retailTradeCouponInSQLite = (RetailTradeCoupon) retailTradeCouponSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradeCoupon_CreateSync, retailTradeCoupon);
        Assert.assertTrue("????????????", retailTradeCouponInSQLite != null);
        retailTradeCouponInSQLite.setIgnoreIDInComparision(true);
        Assert.assertTrue(retailTradeCouponInSQLite.compareTo(retailTradeCoupon) == 0);
    }

    private List<?> createPromotionList() throws InterruptedException, CloneNotSupportedException {
        List<BaseModel> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        //
        System.out.println("---------------?????????????????????????????????1???120??????20??????????????????--------------");
        Promotion tmpPromotion = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        tmpPromotion.setCommodityIDs("1");
        promotion = createPromotion(tmpPromotion);
        promotionList.add(promotion);

        System.out.println("---------------------??????????????????????????????????????????????????????????????????(??????????????????A???200???7???)-----------------------");
        promotion = PromotionSIT.DataInput.getDiscountPromotionSpecifiedCommodity2();
        promotion.setCommodityIDs("119");
        promotion = createPromotion(promotion);
        promotionList.add(promotion);

        return promotionList;
    }

    /**
     * ??????ID????????????
     *
     * @param id
     * @return
     */
    private Commodity getCommodityInSQLiteByID(long id) {
        Commodity commodity = new Commodity();
        commodity.setID(id);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("?????????????????????????????????????????????", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        if (commodity != null) {
            commodity.setCommodityQuantity(10);
            return commodity;
        }
        return null;
    }

    /**
     * ????????????Promotion
     *
     * @param promotion
     * @return
     * @throws InterruptedException
     */
    private Promotion createPromotion(Promotion promotion) throws InterruptedException {
        promotion.setReturnObject(1);//????????????
        promotionHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue("??????Promotion???" + promotion + "??????!", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("??????????????????Promotion???????????????", false);
                break;
            }
            Thread.sleep(1000);
        }
        if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue("??????Promotion???" + promotion + "?????????", false);
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue("????????????????????????????????????????????????????????????", p != null);
        Date d1 = DatetimeUtil.addDays(new Date(), -1);//?????????????????????
        p.setDatetimeStart(d1);
        return p;
    }

    /**
     * ?????????????????????RetailTradePromoting
     */
    public List<?> retrieveNTempRetailTradePromotingInSQLite() {
        System.out.println("??????????????????RetailTradepromoting");
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            Assert.assertTrue("?????????????????????RetailTradePromoting?????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("?????????????????????RetailTradePromoting?????????", false);
        }
        return retailTradePromotingSQLiteEvent.getListMasterTable();
    }

    /**
     * ???RetailTradePromotinf????????????ID???TradeID????????????RetailTradePromotingFlow??????ID
     */
    public void setRetailTradePromotingID(RetailTradePromoting retailTradePromoting) {
        long tempRetailTradePromotionIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        long tempRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        //
        retailTradePromoting.setID(tempRetailTradePromotionIDInSQLite);
        retailTradePromoting.setTradeID(retailTrade.getID().intValue());
        for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
            ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tempRetailTradePromotingFlowIDInSQLite + i);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param retailTrade
     * @throws InterruptedException
     */
    public void createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue("???????????????????????????????????????", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("???????????????????????????", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("???????????????????????????????????????????????????", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param retailTradePromoting
     * @throws InterruptedException
     */
    public void createTempRetailTradePromotingInSQLite(RetailTradePromoting retailTradePromoting) throws InterruptedException {
        if (retailTradePromoting.getListSlave1().size() > 0) {
            retailTradePromoting.setTradeID(retailTrade.getID().intValue());
            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            if (!retailTradePromotingSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting)) {
                Assert.assertTrue("???????????????????????????????????????????????????", false);
            }
            lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                    retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (lTimeOut <= 0) {
                Assert.assertTrue("???????????????????????????????????????????????????", false);
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("???????????????????????????????????????????????????????????????", false);
            }
        }
    }

    /**
     * ??????SyncThread????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @throws InterruptedException
     */
    public void checkTempRetailTradeAndRetailTradePromoting(boolean isCheckRetailTradePromoting) throws InterruptedException {
        SyncThread.executeInstantly(true);
        //
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
        retailTradeList = (List<RetailTrade>) retailTradeHttpEvent.getListMasterTable();

        //
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            Assert.assertTrue("?????????????????????RetailTradePromoting?????????", false);
        }
        lTimeOut = 60;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("?????????????????????RetailTradePromoting?????????", false);
        }
        if (retailTradePromotingHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradePromotingHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????????????????retailTradePromoting?????????", false);
        }
        Assert.assertTrue("retailTradePromoting???????????????", retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0);
        tempRetailTradePromotingList = (List<BaseModel>) retailTradePromotingSQLiteEvent.getListMasterTable();
        if (isCheckRetailTradePromoting) {
            isuUpdateNRetailTradePromoting();
        }
        // ...
        RetailTrade rt = new RetailTrade();
        rt.setSql("WHERE F_SyncDatetime = ?");
        rt.setConditions(new String[]{"0"});
        rt.setQueryKeyword("");
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        Assert.assertTrue("??????????????????????????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????????????????????????????", retailTradeList.size() == 0);
        //
        RetailTradePromoting rtp = new RetailTradePromoting();
        rtp.setSql("where F_SyncDatetime = ?");
        rtp.setConditions(new String[]{"0"});
        List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, rtp);
        Assert.assertTrue("??????????????????????????????????????????????????????", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("????????????????????????????????????????????????", retailTradePromotings.size() == 0);
    }

    /**
     * ????????????RetailTradePromoting???TradeID?????????
     *
     * @param bmList
     */
    public void updateNRetailTradePromoting(List<?> bmList) {
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        if (!retailTradePromotingSQLiteBO.updateNAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList)) {
            Assert.assertTrue("update??????RetailTradePromoting?????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("update??????RetailTradePromoting?????????", false);
        }
    }

    private void isuUpdateNRetailTradePromoting() {
        if (retailTradeList.size() > 0) {
            if (tempRetailTradePromotingList.size() > 0) {
                //????????????RetailTrade?????????RetailTradePromoting???TradeID????????????????????????RetailTradeID
                for (int i = 0; i < retailTradeList.size(); i++) {
                    for (int j = 0; j < tempRetailTradePromotingList.size(); j++) {
                        // POS_SN???????????????????????????ID
                        if (retailTradeList.get(i).getLocalSN() == ((RetailTradePromoting) tempRetailTradePromotingList.get(j)).getTradeID()) {
                            ((RetailTradePromoting) tempRetailTradePromotingList.get(j)).setTradeID(retailTradeList.get(i).getID().intValue());
                        }
                    }
                }
                //???????????????TradeID??????RetailTradePromoting???????????????????????????Update??????????????????????????????????????????RetailTradePromoting?????????????????????????????????Update????????????????????????????????????TradeID?????????????????????????????????
                updateNRetailTradePromoting(tempRetailTradePromotingList);
                uploadTempRetailTradePromotingList(tempRetailTradePromotingList);
            }
        }
    }

    /**
     * ????????????RetailTradePromoting
     *
     * @param bmList
     */
    public void uploadTempRetailTradePromotingList(List<BaseModel> bmList) {
        //????????????RetailTradePromoting????????????
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!retailTradePromotingHttpBO.createNAsync(BaseHttpBO.INVALID_CASE_ID, bmList)) {
            Assert.assertTrue("????????????RetailTradePromoting?????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            if (retailTradePromotingHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradePromotingHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("????????????RetailTradePromoting?????????????????????", false);
        }
    }
}
