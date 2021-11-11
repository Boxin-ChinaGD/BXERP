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
 * 该测试类主要是针对批量上传RetailTrade（包含促销）
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

    private int count;//call 普通action，返回的商品总数
    private int commodityRunTimes = 1;//需要运行runTimes次，才能把商品全部同步下来

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
        //因为其他的测试类生成了错误的临时零售单在本地，导致本类测试出错，所以先删除本地所有的临时零售单
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
                Assert.assertTrue("查询条形码失败", (barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && barcodesList != null && barcodesList.size() > 0));
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvnet(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeJUnit2 onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
                System.out.println("RetailTradeJUnit2.onRetailTradePromotingHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradePromotingHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeJUnit2  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeJUnit2.onRetailTradePromotingSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeJUnit2.onRetailTradePromotingSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeJUnit2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 一个零售单，所有商品不参与促销
     */
//    @Test
    public void test_a_RetailTrade() throws Exception {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_a_RetailTrade");
        //为了清除其他测试生成的临时数据对该测试类的影响，运行前，清除临时数据
        //清除临时RetailTrade
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
            Assert.assertTrue("解析服务器返回的RetailTrade出错！", false);
        }
        List<RetailTrade> rList = (List<RetailTrade>) retailTradeSQLiteEvent.getListMasterTable();
        for (RetailTrade retailTrade : rList) {
            retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
            Assert.assertTrue("清除临时RetailTrade失败！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        System.out.println("--------------- 本地清除临时零售单成功");
        //清除临时RetailTradePromoting
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setSql("where F_SyncDatetime = ?");
        retailTradePromoting.setConditions(new String[]{"0"});
        List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, retailTradePromoting);
        Assert.assertTrue("retrieveNSync返回的错误码不正确", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        if (retailTradePromotingList != null && retailTradePromotingList.size() > 0) {
            for (RetailTradePromoting rtp : retailTradePromotingList) {
                for (int i = 0; i < rtp.getListSlave1().size(); i++) {
                    retailTradePromotingFlowPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (BaseModel) rtp.getListSlave1().get(i));
                    Assert.assertTrue("清除临时RetailTradePromotingFlow失败！", retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
                retailTradePromotingPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                Assert.assertTrue("清除临时RetailTradePromoting失败！", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        System.out.println("--------------- 本地清除临时计算过程成功");
        //
        rList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < rList.size(); i++) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + rList.get(i).toString());
        }
        retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < retailTradePromotingList.size(); i++) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + retailTradePromotingList.get(i).toString());
        }

        System.out.println("--------------- 开始进行登录");
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        // 重置商品数据
        Commodity resetCommodity = new Commodity();
        resetCommodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        resetCommodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);//...
        Shared.retrieveNCBaseModel(resetCommodity, commodityHttpBO, commoditySQliteEvent, commoditySQLiteBO);
        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        //根据ID找到商品，并放到List
        Commodity commodity = getCommodityInSQLiteByID(161);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("零售单中的商品有参与优惠！", retailTradePromoting.getListSlave1().size() <= 0);

        //设置RetailTradePromoting ID,TradeID。。设置RetailTradePromotingFlow ID
        setRetailTradePromotingID(retailTradePromoting);
        //RetailTrade插入本地SQLite
        createTempRetailTradeInSQLite(retailTrade);
        //RetailTradePromoting插入本地SQLite
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(false);

        promotionList.clear();
        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 多个零售单，所有零售单中的商品都参与促销
     */
//    @Test
    public void test_b_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_b_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();
        //找出本地的所有的全场促销
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }

        System.out.println("---------------------创建第一张零售单-----------------------");
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(2);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("有商品参与促销，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        System.out.println("------------------创建第二张零售单--------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("有商品参与促销，应该要有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 多个零售单，部分零售单所有商品不参与促销，部分零售单所有商品参与促销
     */
//    @Test
    public void test_c_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_c_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("---------------------创建第一张零售单--------------------");
        Commodity commodity = getCommodityInSQLiteByID(101);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(100);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("无促销参与，应该没有计算过程！", retailTradePromoting.getListSlave1().size() == 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        System.out.println("-------------------------创建第二张零售单-------------------------");
        //找出本地的所有的全场促销
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

        Assert.assertTrue("有商品参与促销，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 一个零售单，所有商品参与促销
     */
//    @Test
    public void test_d_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_d_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();
        //找出本地的所有的全场促销
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

        Assert.assertTrue("有商品参与促销，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        //设置RetailTradePromoting ID,TradeID。。设置RetailTradePromotingFlow ID
        setRetailTradePromotingID(retailTradePromoting);
        //RetailTrade插入本地SQLite
        createTempRetailTradeInSQLite(retailTrade);
        //RetailTradePromoting插入本地SQLite
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 多个零售单，所有零售单都有参与促销的商品和不参与促销的商品
     */
//    @Test
    public void test_e_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_e_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("--------------------创建第一张零售单---------------------");
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(2);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("有促销商品，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        List<Commodity> cs = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        System.out.println("--------------------创建第二张零售单----------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(44);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("有促销商品，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 一个零售单，零售单中含有参与促销的商品也有不参与促销的商品
     */
//    @Test
    public void test_f_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_f_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("有商品参与促销，应该有计算过程！", retailTradePromoting.getListSlave1().size() > 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        checkTempRetailTradeAndRetailTradePromoting(true);

        commodityList.clear();
        retailTrade = new RetailTrade();
        retailTradePromoting = new RetailTradePromoting();
    }

    /**
     * 多个零售单，所有零售单中的商品不参与促销
     */
//    @Test
    public void test_g_RetailTrade() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        System.out.println("----------------------------------------------test_g_RetailTrade");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        System.out.println("------------------创建第一张零售单--------------------");
        Commodity commodity = getCommodityInSQLiteByID(150);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(151);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("无商品参与促销，无计算过程！", retailTradePromoting.getListSlave1().size() == 0);

        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        System.out.println("--------------------创建第二张零售单-------------------");
        commodity = getCommodityInSQLiteByID(140);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(141);
        commodityList.add(commodity);

        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);

        Assert.assertTrue("无商品参与促销，无计算过程！", retailTradePromoting.getListSlave1().size() == 0);

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

        System.out.println("----------------------------------------------test_H_RetailTradeCoupon上传带有coupon的零售单");
        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        //本地创建零售单
        retailTrade = DataInput.setRetailTrade(retailTrade, commodityList);
        createTempRetailTradeInSQLite(retailTrade);
        //本地创建促销
        retailTradePromoting = new RetailTradePromoting();
        retailTradePromoting.setTradeID(retailTrade.getID().intValue());
        RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
        retailTradePromotingFlow.setProcessFlow(".....");
        List<BaseModel> retailTradePromotingFlowList = new ArrayList<>();
        retailTradePromotingFlowList.add(retailTradePromotingFlow);
        retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        //本地创建零售单优惠券使用表
        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
        retailTradeCoupon.setID(retailTradeCouponPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
        retailTradeCoupon.setCouponCodeID(1);
        retailTradeCoupon.setRetailTradeID(retailTrade.getID().intValue());
        createRetailTradeCouponInSQLite(retailTradeCoupon);
        //上传零售单
        checkTempRetailTradeAndRetailTradePromoting(true);
    }

    private void createRetailTradeCouponInSQLite(RetailTradeCoupon retailTradeCoupon) {
        RetailTradeCoupon retailTradeCouponInSQLite = (RetailTradeCoupon) retailTradeCouponSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradeCoupon_CreateSync, retailTradeCoupon);
        Assert.assertTrue("创建失败", retailTradeCouponInSQLite != null);
        retailTradeCouponInSQLite.setIgnoreIDInComparision(true);
        Assert.assertTrue(retailTradeCouponInSQLite.compareTo(retailTradeCoupon) == 0);
    }

    private List<?> createPromotionList() throws InterruptedException, CloneNotSupportedException {
        List<BaseModel> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        //
        System.out.println("---------------模拟网页创建促销：商品1满120元减20元的促销活动--------------");
        Promotion tmpPromotion = PromotionSIT.DataInput.getCashReducingPromotionSpecifiedCommodity();
        tmpPromotion.setCommodityIDs("1");
        promotion = createPromotion(tmpPromotion);
        promotionList.add(promotion);

        System.out.println("---------------------模拟网页创建促销：指定商品进行满金额折扣促销(如，指定商品A满200打7折)-----------------------");
        promotion = PromotionSIT.DataInput.getDiscountPromotionSpecifiedCommodity2();
        promotion.setCommodityIDs("119");
        promotion = createPromotion(promotion);
        promotionList.add(promotion);

        return promotionList;
    }

    /**
     * 根据ID查到商品
     *
     * @param id
     * @return
     */
    private Commodity getCommodityInSQLiteByID(long id) {
        Commodity commodity = new Commodity();
        commodity.setID(id);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue("在本地查找商品，错误码不正确！", commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        if (commodity != null) {
            commodity.setCommodityQuantity(10);
            return commodity;
        }
        return null;
    }

    /**
     * 创建单个Promotion
     *
     * @param promotion
     * @return
     * @throws InterruptedException
     */
    private Promotion createPromotion(Promotion promotion) throws InterruptedException {
        promotion.setReturnObject(1);//返回对象
        promotionHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue("创建Promotion：" + promotion + "失败!", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("服务器返回的Promotion解析失败！", false);
                break;
            }
            Thread.sleep(1000);
        }
        if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue("创建Promotion：" + promotion + "超时！", false);
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue("服务器返回的创建对象是空，或者解析为空！", p != null);
        Date d1 = DatetimeUtil.addDays(new Date(), -1);//得到昨天的日期
        p.setDatetimeStart(d1);
        return p;
    }

    /**
     * 在本地查找临时RetailTradePromoting
     */
    public List<?> retrieveNTempRetailTradePromotingInSQLite() {
        System.out.println("开始查找临时RetailTradepromoting");
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            Assert.assertTrue("在本地查找临时RetailTradePromoting失败！", false);
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
            Assert.assertTrue("在本地查找临时RetailTradePromoting超时！", false);
        }
        return retailTradePromotingSQLiteEvent.getListMasterTable();
    }

    /**
     * 为RetailTradePromotinf设置临时ID和TradeID，，，为RetailTradePromotingFlow设置ID
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
     * 在本地插入临时零售单
     *
     * @param retailTrade
     * @throws InterruptedException
     */
    public void createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue("在本地插入临时零售单失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("本地插入零售单超时", retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("在本地插入临时零售单错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);
    }

    /**
     * 将临时零售单计算过程插入本地
     *
     * @param retailTradePromoting
     * @throws InterruptedException
     */
    public void createTempRetailTradePromotingInSQLite(RetailTradePromoting retailTradePromoting) throws InterruptedException {
        if (retailTradePromoting.getListSlave1().size() > 0) {
            retailTradePromoting.setTradeID(retailTrade.getID().intValue());
            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            if (!retailTradePromotingSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting)) {
                Assert.assertTrue("在本地插入临时零售单计算过程失败！", false);
            }
            lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                    retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (lTimeOut <= 0) {
                Assert.assertTrue("在本地插入临时零售单计算过程超时！", false);
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("在本地插入临时零售单计算过程错误码不正确！", false);
            }
        }
    }

    /**
     * 使用SyncThread上传零售单和计算过程后，检查本地是否还存在临时零售单和临时零售单计算过程
     *
     * @throws InterruptedException
     */
    public void checkTempRetailTradeAndRetailTradePromoting(boolean isCheckRetailTradePromoting) throws InterruptedException {
        SyncThread.executeInstantly(true);
        //
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查询临时零售单失败！", false);
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
            Assert.assertTrue("超时", false);
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("解析服务器返回的RetailTrade出错！", false);
        }
        retailTradeList = (List<RetailTrade>) retailTradeHttpEvent.getListMasterTable();

        //
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            Assert.assertTrue("在本地查找临时RetailTradePromoting失败！", false);
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
            Assert.assertTrue("在本地查找临时RetailTradePromoting超时！", false);
        }
        if (retailTradePromotingHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradePromotingHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("解析服务器返回的retailTradePromoting出错！", false);
        }
        Assert.assertTrue("retailTradePromoting返回为空！", retailTradePromotingSQLiteEvent.getListMasterTable().size() >= 0);
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
        Assert.assertTrue("查本地临时零售单错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("本地不应该存在临时零售单", retailTradeList.size() == 0);
        //
        RetailTradePromoting rtp = new RetailTradePromoting();
        rtp.setSql("where F_SyncDatetime = ?");
        rtp.setConditions(new String[]{"0"});
        List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) retailTradePromotingPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradePromoting_RetrieveNByConditions, rtp);
        Assert.assertTrue("查本地临时零售单计算过程错误码不正确", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("本地不应该存在临时零售单计算过程", retailTradePromotings.size() == 0);
    }

    /**
     * 当修改了RetailTradePromoting的TradeID之后，
     *
     * @param bmList
     */
    public void updateNRetailTradePromoting(List<?> bmList) {
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        if (!retailTradePromotingSQLiteBO.updateNAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList)) {
            Assert.assertTrue("update本地RetailTradePromoting失败！", false);
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
            Assert.assertTrue("update本地RetailTradePromoting超时！", false);
        }
    }

    private void isuUpdateNRetailTradePromoting() {
        if (retailTradeList.size() > 0) {
            if (tempRetailTradePromotingList.size() > 0) {
                //将上传的RetailTrade对应的RetailTradePromoting的TradeID重新设置为真正的RetailTradeID
                for (int i = 0; i < retailTradeList.size(); i++) {
                    for (int j = 0; j < tempRetailTradePromotingList.size(); j++) {
                        // POS_SN存放了零售单的临时ID
                        if (retailTradeList.get(i).getLocalSN() == ((RetailTradePromoting) tempRetailTradePromotingList.get(j)).getTradeID()) {
                            ((RetailTradePromoting) tempRetailTradePromotingList.get(j)).setTradeID(retailTradeList.get(i).getID().intValue());
                        }
                    }
                }
                //得到正确的TradeID后的RetailTradePromoting，需要现在本地进行Update，然后再上传到服务器，否则，RetailTradePromoting上传失败之后，由于没有Update到数据库，会不知道真正的TradeID是多少，而且主外键关联
                updateNRetailTradePromoting(tempRetailTradePromotingList);
                uploadTempRetailTradePromotingList(tempRetailTradePromotingList);
            }
        }
    }

    /**
     * 上传临时RetailTradePromoting
     *
     * @param bmList
     */
    public void uploadTempRetailTradePromotingList(List<BaseModel> bmList) {
        //批量上传RetailTradePromoting到服务器
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!retailTradePromotingHttpBO.createNAsync(BaseHttpBO.INVALID_CASE_ID, bmList)) {
            Assert.assertTrue("批量上传RetailTradePromoting失败！", false);
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
            Assert.assertTrue("批量上传RetailTradePromoting到服务器超时！", false);
        }
    }
}
