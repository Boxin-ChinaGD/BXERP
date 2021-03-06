package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.bo.CouponSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.ResetBaseDataUtil;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync;
import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class RetailTradeAggregationSIT extends BaseHttpAndroidTestCase {
    List<RetailTrade> tempRetailTradeList = new ArrayList<RetailTrade>();
    private boolean isSyncCompleted = false;

    private static long maxFrameIDInSQLite;
    private static boolean isSync = false;

    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    //
    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradePromotingPresenter retailTradePromotingPresenter = null;
    //
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionPresenter promotionPresenter = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static PromotionSQLiteBO promotionSqLiteBO = null;
    //
//    private static CommodityPresenter commodityPresenter = null;
//    private static CommodityHttpBO commodityHttpBO = null;
//    private static CommoditySQLiteBO commoditySQLiteBO = null;
//    private static CommodityHttpEvent commodityHttpEvent = null;
//    private static CommoditySQLiteEvent commoditySQLiteEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    protected static CouponSQLiteBO couponSQLiteBO;
    protected static CouponSQLiteEvent couponSQLiteEvent;
    protected static CouponHttpEvent couponHttpEvent;
    protected static CouponHttpBO couponHttpBO;

    //
    long lTimeOut = UNIT_TEST_TimeOut;
    private static final int EVENT_ID_RetailTradeAggregationSIT = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();

        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (promotionPresenter == null) {
            promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        }
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionSqLiteBO == null) {
            promotionSqLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }

        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSqLiteBO);
        //
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
//        if(commoditySQLiteEvent == null) {
//            commoditySQLiteEvent = new CommoditySQLiteEvent();
//            commoditySQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
//        }
//        if(commodityHttpEvent == null) {
//            commodityHttpEvent = new CommodityHttpEvent();
//            commodityHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
//        }
//        if(commoditySQLiteBO == null) {
//            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        if(commodityHttpBO == null) {
//            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
//        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
//        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
//        commodityHttpEvent.setHttpBO(commodityHttpBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static RetailTradeAggregation retailTradeAggregation = null;
        private static Promotion promotionInput = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException, ParseException {
            retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(1);
            retailTradeAggregation.setPosID(1);
            retailTradeAggregation.setTradeNO(10);
            retailTradeAggregation.setAmount(100);
            retailTradeAggregation.setReserveAmount(20);
            retailTradeAggregation.setCashAmount(10);
            retailTradeAggregation.setWechatAmount(20);
            retailTradeAggregation.setAlipayAmount(30);
            retailTradeAggregation.setWorkTimeStart(new Date());
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 3));
            retailTradeAggregation.setAmount1(1l);
            retailTradeAggregation.setAmount2(20l);
            retailTradeAggregation.setAmount3(3l);
            retailTradeAggregation.setAmount4(40l);
            retailTradeAggregation.setAmount5(5l);
            //

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }

        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite) throws Exception {
            Random ran = new Random();
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setLogo("11");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setPaymentType(1);
            retailTrade.setStaffID(4);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(100);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(ran.nextInt(3) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setInt1(1);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(25);
            retailTradeCommodity.setCommodityID(21);
            retailTradeCommodity.setNO(5);
            retailTradeCommodity.setPriceOriginal(10);
            retailTradeCommodity.setDiscount(1);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(8);
            retailTradeCommodity.setNOCanReturn(5);
            retailTradeCommodity.setPriceVIPOriginal(1);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(22);
            retailTradeCommodity2.setBarcodeID(26);
            retailTradeCommodity2.setNO(5);
            retailTradeCommodity2.setPriceOriginal(10);
            retailTradeCommodity2.setDiscount(1);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(8);
            retailTradeCommodity2.setNOCanReturn(5);
            retailTradeCommodity2.setPriceVIPOriginal(1);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }

        // ?????????9 ???
        public static final Promotion getDiscountPromotionAllCommodity() throws CloneNotSupportedException, InterruptedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//?????????????????????
            Date dt2 = new Date(date.getTime() + 500000000L);//????????????????????????

            promotionInput = new Promotion();
            promotionInput.setName("?????????9???");
            promotionInput.setType(1);
            promotionInput.setStatus(0);
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(1);
            promotionInput.setExcecutionAmount(8);
            promotionInput.setExcecutionDiscount(0.9);
            promotionInput.setScope(0);
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            Thread.sleep(1000);
            promotionInput.setSn("CX" + System.currentTimeMillis() % 1000000);
            promotionInput.setInt1(1);

            return (Promotion) promotionInput.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
            isSyncCompleted = true;
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationSIT.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeAggregationSIT.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) throws InterruptedException {
        System.out.println("#########################################################RetailTradeAggregationSIT onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
            isSyncCompleted = true;
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationSIT.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeAggregationSIT.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEventEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                isSync = true;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * 1.pos??????
     * 2.staff??????
     * 3.??????????????????
     * 4.????????????????????????????????????
     * 5.???????????????????????????????????????????????????
     * 6.?????????????????????????????????
     * 7.???????????????,??????SQLite??????????????????
     * 8.??????SQLite????????????????????????????????????
     * 9.???SQLite????????????????????????????????????,???????????????????????????
     * 10.????????????, ????????????????????????
     * 11.????????????
     */
    @Test
    public void test_a_RetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //??????????????????????????????
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        //
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        List<BaseModel> retailTradeList = (List<BaseModel>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", retailTradeList.size() > 0 && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("????????????????????????????????????????????????????????????", false);
        }

        //4.????????????????????????????????????
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setPosID(1);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            Assert.assertTrue("????????????!", false);
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("?????????????????????????????????????????????", false);
        }
        //7.???????????????,??????SQLite??????????????????
        Assert.assertTrue("?????????????????????,????????????????????????????????????", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError || retailTradeAggregationSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated);
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("?????????????????????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //8.??????SQLite????????????????????????????????????
        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("??????????????????????????????????????????", rta == null);

        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("??????????????????! ");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("??????????????????!");
        }
    }

    /**
     * 1.??????POS???Staff??????
     * 2.??????????????????????????????????????????????????????????????????
     * 3.???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 4.???????????????????????????????????????
     * 5.???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 6.????????????1-5
     * 7.??????POS???Staff??????
     * 8.????????????
     * 9.????????????????????????????????????????????????????????????????????????????????????
     * 10.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 11.?????????????????????????????????????????????????????????????????????????????????????????????????????????
     */

    @Test
    public void test_b_RetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();
        // ????????????????????????????????????(????????????????????????????????????SQLite???????????????????????????????????????MYSQL???????????????)
//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        retailTradeCommodityPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        /** ??????1-5 */
        staffLoginToLogOut(new Date());
        // 6???????????????
        staffLoginToLogOut(new Date());
        // 7???pos???Staff??????
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        // ??????????????????
        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(EVENT_ID_RetailTradeAggregationSIT);
        resetBaseDataUtil.ResetBaseData(false);
        // 8???????????????
        Promotion p = createPromotion();
        // 9???????????????(????????????)
        List<RetailTrade> retailTradesWithPromotionList = new ArrayList<>();
        createRetailTradeWithPromotion(retailTradesWithPromotionList, p);
        // 10?????????????????????
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradesWithPromotionList);
        // 11???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        RetailTradeAggregation retailTradeAggeration = dateRetailTradeAggeration(retailTradesWithPromotionList, returnRetailTradeList);
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggeration);
    }

    /**
     * 1.POS????????????
     * 2.?????????1???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1?????????????????????????????????????????????????????????????????????
     * 3.?????????2??????????????????????????????????????????????????????
     * 4.?????????2???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 5.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 6.?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @Test
    public void test_c_RetailTradeAggregation() throws Exception {
//        0.??????????????????????????????????????????????????????????????? //???????????????SQLite????????????????????????????????????MYSQL????????????????????????????????????????????????????????????
        //retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //Assert.assertTrue("deleteNObjectSync?????????????????????" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //1.POS????????????????????????1????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String staff1 = "15016509167";
        login(1, staff1, posLoginHttpBO, staffLoginHttpBO);
        //
        //???????????????????????????
        List<RetailTrade> retailTradeList = new ArrayList<>(); // ??????????????????????????????
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList);
        }
        //
        // ?????????????????????
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradeList);

        //2.POS????????????????????????2??????????????????????????????????????????????????????
        String staff2 = "15854320895";
        login(1, staff2, posLoginHttpBO, staffLoginHttpBO);
        //
        // ???????????????????????????
        List<RetailTrade> retailTradeList2 = new ArrayList<>(); // ??????????????????????????????
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList2);
        }
        // ?????????????????????
        List<RetailTrade> returnRetailTradeList2 = createReturnSheet(retailTradeList2);

        //3.?????????2????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        retrieveRetailTradeAndUpload();

        // 4.????????????
        RetailTradeAggregation retailTradeAggregation = dataAggregation(retailTradeList2, returnRetailTradeList2, new Date());
        // ???????????????????????????
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggregation);

        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = ?");
        rt.setConditions(new String[]{"0"});
        rt.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
        rt.setQueryKeyword(RetailTrade.generateRetailTradeSN(Constants.posID));
        rt.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, rt);
        Assert.assertTrue("retrieveNObjectSync?????????????????????" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync??????,???????????????????????????????????????", rtList.size() == 0);
    }

    private void retrieveRetailTradeAndUpload() throws Exception {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("??????????????????????????????", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("????????????????????????????????????????????????????????????", false);
        }
    }

    /**
     * ????????????POS???STAFF
     */
    public static boolean login(long posID, String phone, PosLoginHttpBO pbo, StaffLoginHttpBO sbo) throws Exception {
        Constants.posID = (int) posID;//
        Constants.MyCompanySN = "668866";
        //1.pos??????
        Pos pos = new Pos();
        pos.setID(posID);
        pos.setPasswordInPOS("000000");
        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        pbo.setPos(pos);
        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        pbo.loginAsync();
        //
        long lTimeOut = UNIT_TEST_TimeOut;
        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("pos????????????!");
            return false;
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff??????
            Staff staff = new Staff();
            staff.setPhone(phone);
            staff.setPasswordInPOS("000000");
            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            sbo.setStaff(staff);
            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
            sbo.loginAsync();
            //
            lTimeOut = UNIT_TEST_TimeOut;
            while (sbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                System.out.println("STAFF????????????!");
                return false;
            }
        } else {
            System.out.println("pos???????????????");
            return false;
        }
        return true;
    }

    private RetailTradeAggregation dateRetailTradeAggeration(List<RetailTrade> retailTradesWithPromotionList, List<RetailTrade> returnRetailTradeList) {
        double retailTradeAmount = 0; // ???????????????
        double returnTradeAmount = 0; // ??????????????????


        for (int i = 0; i < retailTradesWithPromotionList.size(); i++) {
            retailTradeAmount += retailTradesWithPromotionList.get(i).getAmount();
            for (int x = 0; x < retailTradesWithPromotionList.get(i).getListSlave1().size(); x++) {
                RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTradesWithPromotionList.get(i).getListSlave1().get(x);
                returnTradeAmount += retailTradeCommodity.getPriceReturn();
            }
        }

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        long maxRetailTradeAggregationIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        retailTradeAggregation.setID(maxRetailTradeAggregationIDInSQLite);
        //
        retailTradeAggregation.setPosID(retailTradesWithPromotionList.get(1).getPos_ID());
        retailTradeAggregation.setTradeNO(retailTradesWithPromotionList.size() + returnRetailTradeList.size()); // ????????????
        retailTradeAggregation.setAmount(GeneralUtil.sub(retailTradeAmount, returnTradeAmount)); //  ????????????

        retailTradeAggregation.setReserveAmount(555l); // ?????????
        retailTradeAggregation.setCashAmount(GeneralUtil.sub(retailTradeAmount, returnTradeAmount)); // ????????????
        retailTradeAggregation.setAlipayAmount(0);
        retailTradeAggregation.setWechatAmount(0);
        retailTradeAggregation.setAmount1(0);
        retailTradeAggregation.setAmount2(0);
        retailTradeAggregation.setAmount3(0);
        retailTradeAggregation.setAmount4(0);
        retailTradeAggregation.setAmount5(0);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(new Date());

        return retailTradeAggregation;
    }

    private void createRetailTradeWithPromotion(List<RetailTrade> retailTradesWithPromotionList, Promotion p) throws ParseException, InterruptedException {
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        List<BaseModel> promotionList = new ArrayList<>();
        promotionList.add(p);
        // ??????????????????
        List<Commodity> commodityList = new ArrayList<Commodity>();
        Commodity baseModel = new Commodity();
        baseModel.setSql("where F_id in (?,?,?,?)");
        baseModel.setConditions(new String[]{"24", "25", "26"});
        baseModel.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        CommodityPresenter commodityPresenters = GlobalController.getInstance().getCommodityPresenter();
        commodityList = (List<Commodity>) commodityPresenters.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, baseModel);
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(1);
        }
        RetailTrade sellRetailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertTrue("???????????????????????????", sellRetailTrade != null);
        System.out.println("====??????????????????????????????" + sellRetailTrade.getAmount());
        Assert.assertTrue("????????????", sellRetailTrade.getAmount() - (commodityList.get(2).getPriceRetail() + commodityList.get(1).getPriceRetail() + commodityList.get(0).getPriceRetail()) * 0.9 <= baseModel.TOLERANCE);
        //
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        // ???????????????????????????????????????????????????
        for (int i = 0; i < 4; i++) {
            sellRetailTrade.setID(maxRetailTradeIDInSQLite + i);
            sellRetailTrade.setSmallSheetID(1);
            sellRetailTrade.setPos_ID(1);
            sellRetailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
            sellRetailTrade.setPaymentType(1);
            sellRetailTrade.setStaffID(4);
            sellRetailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            sellRetailTrade.setSourceID(-1);
            sellRetailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            sellRetailTrade.setSaleDatetime(new Date());
            sellRetailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            sellRetailTrade.setRemark("??????????????????????????????");
            sellRetailTrade.setAmountAlipay(sellRetailTrade.getAmount());
            sellRetailTrade.setPaymentType(2);
            sellRetailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            sellRetailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
            sellRetailTrade.setInt1(1);
            sellRetailTrade.setLogo("11111");
            sellRetailTrade.setPaymentAccount("12");
            //
            sellRetailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            sellRetailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            for (int x = 0; x < sellRetailTrade.getListSlave1().size(); x++) {
                RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) sellRetailTrade.getListSlave1().get(x);
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + x);
                retailTradeCommodity.setTradeID(maxRetailTradeIDInSQLite + i);
                retailTradeCommodity.setCommodityName(commodityList.get(x).getName());
                retailTradeCommodity.setCommodityID(commodityList.get(x).getID().intValue());
                retailTradeCommodity.setPriceOriginal(commodityList.get(x).getPriceRetail());
                retailTradeCommodity.setNOCanReturn(5);
                retailTradeCommodity.setPriceReturn(5.5);
                retailTradeCommodity.setPriceSpecialOffer(6.6);
                retailTradeCommodity.setBarcodeID(x + 1);
                retailTradeCommodity.setDiscount(5.55);
            }
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, sellRetailTrade)) {
                Assert.assertTrue("???????????????", false);
            }
            //??????????????????
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Assert.assertTrue("??????????????????????????????", false);
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
            Assert.assertTrue("?????????????????????????????????", master != null);
            retailTradesWithPromotionList.add(master);
        }
        //
    }

    private Promotion createPromotion() throws Exception {
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion promotion = DataInput.getDiscountPromotionAllCommodity();
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, promotion, promotionSQLiteEvent)) {
            Assert.assertTrue("CreateAsync bm1???????????????", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertTrue("createAsync Promotion?????????" + promotionSQLiteEvent.getStatus(), promotionSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("?????????????????????????????????Null???", promotion.compareTo(p) == 0);
        Assert.assertTrue("Create bm1????????????????????????EC_NoError???Create??????", promotionSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.assertTrue("????????????!", false);
        }
        //
        lTimeOut = UNIT_TEST_TimeOut;
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Promotion??????!", false);
        }
        Promotion pt = (Promotion) promotionSqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("?????????????????????????????????", pt != null);
        Date dt1 = DatetimeUtil.addDays(new Date(), -1);// ????????????????????????????????????????????????????????????????????????
        pt.setDatetimeStart(dt1); // ????????????????????????????????????????????????

        return pt;
    }

    private void staffLoginToLogOut(Date date) throws Exception {
        // 1???POS.Staff??????
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        // 2??????????????????????????????
        List<RetailTrade> retailTradeList = new ArrayList<>(); // ??????????????????????????????
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList);
        }
        // 3????????????????????????
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradeList);
        // 4???????????????
        RetailTradeAggregation retailTradeAggregation = dataAggregation(retailTradeList, returnRetailTradeList, date);
        // 5??????????????????????????????
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggregation);
    }

    private RetailTradeAggregation dataAggregation(List<RetailTrade> retailTradeList, List<RetailTrade> returnRetailTradeList, Date date) {
        int retailTradeAmount = 0; // ???????????????
        for (int i = 0; i < retailTradeList.size(); i++) {
            retailTradeAmount += retailTradeList.get(i).getAmount();
        }
        int returnTradeAmount = 0; // ??????????????????
        for (int i = 0; i < returnRetailTradeList.size(); i++) {
            List<RetailTradeCommodity> returnRetailTradeCommodityList = (List<RetailTradeCommodity>) returnRetailTradeList.get(i).getListSlave1();
            for (int x = 0; x < returnRetailTradeCommodityList.size(); x++) {
                retailTradeAmount += (returnRetailTradeCommodityList.get(x).getPriceReturn() * returnRetailTradeCommodityList.get(x).getNOCanReturn());
            }
        }
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        long maxRetailTradeAggregationIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        retailTradeAggregation.setID(maxRetailTradeAggregationIDInSQLite);
        //
        retailTradeAggregation.setStaffID(1);
        retailTradeAggregation.setPosID(retailTradeList.get(1).getPos_ID());
        retailTradeAggregation.setTradeNO(retailTradeList.size() + returnRetailTradeList.size()); // ????????????
        retailTradeAggregation.setAmount(retailTradeAmount - returnTradeAmount); //  ????????????
        retailTradeAggregation.setReserveAmount(555l); // ?????????
        retailTradeAggregation.setCashAmount(retailTradeAmount - returnTradeAmount); // ????????????
        retailTradeAggregation.setAlipayAmount(0);
        retailTradeAggregation.setWechatAmount(0);
        retailTradeAggregation.setAmount1(0);
        retailTradeAggregation.setAmount2(0);
        retailTradeAggregation.setAmount3(0);
        retailTradeAggregation.setAmount4(0);
        retailTradeAggregation.setAmount5(0);
        retailTradeAggregation.setWorkTimeStart(date);
        retailTradeAggregation.setWorkTimeEnd(new Date());

        return retailTradeAggregation;
    }

    private void uploadRetailTradeAggregationAndLoginOut(RetailTradeAggregation retailTradeAggregation) throws Exception {
        retailTradeAggregation.setStaffID(4);
        retailTradeAggregation.setWorkTimeStart(new Date());
        retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(new Date(), 2));
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            Assert.assertTrue("????????????!", false);
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("?????????????????????????????????????????????", false);
        }
        //
        Assert.assertTrue("?????????????????????,????????????????????????????????????", retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError || retailTradeAggregationSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated);
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("?????????????????????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertTrue("??????????????????????????????????????????", rta == null);

        logOut();
    }

    private void createRetailTrade(List<RetailTrade> retailTradeList) throws Exception {
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue("???????????????", false);
        }
        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("?????????????????????????????????", master != null);
        retailTradeList.add(master);
    }

    private List createReturnSheet(List<RetailTrade> retailTradeList) throws InterruptedException, ParseException {
        List<RetailTrade> returnRetailTradeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RetailTrade retailTrade = retailTradeList.get(i);
            // ???????????????
            retailTrade.setSaleAmountAlipay(retailTrade.getAmountAlipay());
            retailTrade.setSaleAmountWeChat(retailTrade.getAmountWeChat());
            retailTrade.setSourceID(retailTrade.getID().intValue());
            retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("?????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            retailTrade.setID(maxRetailTradeIDInSQLite);
            // ???????????????
            List<RetailTradeCommodity> retailTradeCommoditieList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
            for (int x = 0; x < retailTradeCommoditieList.size(); x++) {
                RetailTradeCommodity retailTradeCommodity = retailTradeCommoditieList.get(x);
                long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                Assert.assertTrue("?????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + x);
            }
            //
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
                Assert.assertTrue("???????????????", false);
            }
            //??????????????????
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Assert.assertTrue("??????????????????????????????", false);
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
            Assert.assertTrue("?????????????????????????????????", master != null);
            returnRetailTradeList.add(master);
        }
        return returnRetailTradeList;
    }
}
