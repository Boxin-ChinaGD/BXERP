package wpos.SIT;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.model.promotion.Promotion;
import wpos.model.promotion.PromotionCalculator;
import wpos.presenter.BasePresenter;
import wpos.utils.DatetimeUtil;
import wpos.utils.GeneralUtil;
import wpos.utils.ResetBaseDataUtil;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static wpos.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync;
import static wpos.utils.Shared.UNIT_TEST_TimeOut;

public class RetailTradeAggregationSIT extends BaseHttpTestCase {
    List<RetailTrade> tempRetailTradeList = new ArrayList<RetailTrade>();
    private boolean isSyncCompleted = false;

    private static long maxFrameIDInSQLite;
    private static boolean isSync = false;

    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    //
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    //
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static PromotionSQLiteBO promotionSqLiteBO = null;
    //
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

    protected static CouponSQLiteBO couponSQLiteBO;
    protected static CouponSQLiteEvent couponSQLiteEvent;
    protected static CouponHttpEvent couponHttpEvent;
    protected static CouponHttpBO couponHttpBO;
    @Resource
    ResetBaseDataUtil resetBaseDataUtil;
    //
    long lTimeOut = UNIT_TEST_TimeOut;
    private static final int EVENT_ID_RetailTradeAggregationSIT = 10000;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();

        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
            retailTradeAggregationSQLiteBO.setRetailTradeAggregationPresenter(retailTradeAggregationPresenter);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
            retailTradeSQLiteBO.setRetailTradePresenter(retailTradePresenter);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setRetailTradePresenter(retailTradePresenter);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionSqLiteBO == null) {
            promotionSqLiteBO = new PromotionSQLiteBO(promotionSQLiteEvent, promotionHttpEvent);
        }

        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSqLiteBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
            staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(EVENT_ID_RetailTradeAggregationSIT);
        }
        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(couponSQLiteEvent, couponHttpEvent);
        }
        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(couponSQLiteEvent, couponHttpEvent);
        }
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {

        protected static RetailTradeAggregation getRetailTradeAggregation() throws CloneNotSupportedException {
            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 3));
            retailTradeAggregation.setAmount1(1L);
            retailTradeAggregation.setAmount2(20L);
            retailTradeAggregation.setAmount3(3L);
            retailTradeAggregation.setAmount4(40L);
            retailTradeAggregation.setAmount5(5L);
            //

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }

        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite) throws Exception {
            Random ran = new Random();
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID((int) lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(6)));
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
            retailTradeCommodity.setID((int) lRtcMaxIDInSQLite);
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
            retailTradeCommodity.setCommodityName("商品：" + System.currentTimeMillis());

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID((int) (lRtcMaxIDInSQLite + 1));
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
            retailTradeCommodity2.setCommodityName("商品：" + System.currentTimeMillis());

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }

        // 全场打9 折
        public static Promotion getDiscountPromotionAllCommodity() throws CloneNotSupportedException, InterruptedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 500000000L);//得到结束的的日期

            Promotion promotionInput = new Promotion();
            promotionInput.setName("全场打9折");
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
            isSyncCompleted = true;
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeAggregationSIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeAggregationSIT onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
            isSyncCompleted = true;
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeAggregationSIT.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeAggregationSIT.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEventEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeAggregationSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 1.pos登录
     * 2.staff登录
     * 3.暂停同步线程
     * 4.创建收银汇总数据在数据表
     * 5.发送请求将收银汇总数据上传到服务器
     * 6.判断服务器返回的错误码
     * 7.错误码正确,删除SQLite中对应的数据
     * 8.查找SQLite判断删除后的数据是否存在
     * 9.在SQLite中插入新的收银汇总的数据,不需要发送上传请求
     * 10.恢复线程, 线程是否顺利执行
     * 11.退出登录
     */
    @Test
    public void test_a_RetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();

        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);

        //创建临时零售单在本地
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertSame(retailTradeCommodityPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        //
        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        //
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        List<BaseModel> retailTradeList = (List<BaseModel>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
        Assert.assertTrue(retailTradeList.size() > 0 && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");

        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(ESET_RetailTrade_RetrieveNAsync);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.fail("查询临时零售单失败！");
        }

        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("上传临时零售单同步数据失败！原因：超时！" + retailTradeSQLiteBO.getSqLiteEvent().getStatus());
        }

        //4.创建收银汇总数据在数据表
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
        retailTradeAggregation.setPosID(1);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            Assert.fail("创建失败!");
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.fail("上传收银汇总失败！原因：超时！");
        }
        //7.错误码正确,删除SQLite中对应的数据
        Assert.assertTrue(retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError || retailTradeAggregationSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated, "上传收银汇总后,服务器返回的错误码不正确");
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "删除操作的错误码不正确");

        //8.查找SQLite判断删除后的数据是否存在
        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertNull(rta, "删除后的收银汇总信息仍然存在");

        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("退出登录失败! ");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("退出登录超时!");
        }
    }

    /**
     * 1.正常POS、Staff登录
     * 2.重复进行收银、退货（创建多张零售单和退货单）
     * 3.上传收银汇总，做结果验证（微信、支付宝、现金收入，总营业额，交易单数，上下班时间）
     * 4.创建临时零售单及其计算过程
     * 5.查找本地是否有临时的零售单、临时零售单计算过程和临时的收银汇总需要上传，店员退出。
     * 6.重复步骤1-5
     * 7.正常POS、Staff登录
     * 8.创建促销
     * 9.重复进行收银（参与促销）、退货（创建多张零售单和退货单）
     * 10.退出，上传收银汇总，做结果验证（准备金，微信、支付宝、现金收入，总营业额，交易单数，上下班时间）、比较没有促销时的营业额
     * 11.查找本地是否有临时的零售单、临时零售单计算过程和临时的收银汇总需要上传
     */
    @Test
    public void test_b_RetailTradeAggregation() throws Exception {
        Shared.printTestMethodStartInfo();
        // 清空数据以免造成其他影响(根据需要使用，如果清空了SQLite中的零售单，那么也需要清空MYSQL中的零售单)
//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
//        retailTradeCommodityPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        /**
         *  步骤1-5
         */
        staffLoginToLogOut(new Date());
        // 6、重复操作
        staffLoginToLogOut(new Date());
        // 7、pos、Staff登录
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        // 同步商品信息
//        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(EVENT_ID_RetailTradeAggregationSIT);
        resetBaseDataUtil.setEventID(EVENT_ID_RetailTradeAggregationSIT);
        resetBaseDataUtil.initObject();
        resetBaseDataUtil.ResetBaseData(false);
        // 8、创建促销
        Promotion p = createPromotion();
        // 9、重复收银(参与促销)
        List<RetailTrade> retailTradesWithPromotionList = new ArrayList<>();
        createRetailTradeWithPromotion(retailTradesWithPromotionList, p);
        // 10、退货单的创建
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradesWithPromotionList);
        // 11、退出，上传收银汇总，做结果验证（准备金，微信、支付宝、现金收入，总营业额，交易单数，上下班时间）、比较没有促销时的营业额
        RetailTradeAggregation retailTradeAggeration = dateRetailTradeAggeration(retailTradesWithPromotionList, returnRetailTradeList);
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggeration);
    }

    /**
     * 1.POS正常登录
     * 2.收银员1号登录，正常收银（不参与促销）和退货（创建多张零售单和退货单）但没有正常退出交班（手动测试时，我们是直接结束进程的，因此没有收银员1号的交班记录和收银汇总，然后有残余临时零售单）
     * 3.收银员2号登录，正常收银（不参与促销）和退货
     * 4.收银员2准备退出，交班，查找本地是否有临时的零售单、临时零售单计算过程和临时的收银汇总需要上传，有则上传至服务器。
     * 5.上传收银汇总，做结果验证（准备金，微信、支付宝、现金收入，总营业额，交易单数，上下班时间），然后退出登录
     * 6.查找本地是否有临时的零售单、临时零售单计算过程和临时的收银汇总，有则测试不通过，没有则测试通过
     */
    @Test
    public void test_c_RetailTradeAggregation() throws Exception {
//        0.删除所有临时零售单和收银汇总，防止数据干扰 //如果删除了SQLite中的零售单，那么也需要将MYSQL中的零售单也删除，避免服务器提示重复创建
        //retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //Assert.assertTrue("deleteNObjectSync失败，错误码：" + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //1.POS正常登录，收银员1号登录，正常收银（不参与促销）和退货（创建多张零售单和退货单）但没有正常退出交班
        String staff1 = "15016509167";
        login(1, staff1, posLoginHttpBO, staffLoginHttpBO);
        //
        //创建多张临时零售单
        List<RetailTrade> retailTradeList = new ArrayList<>(); // 记录创建出来的零售单
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList);
        }
        //
        // 创建多张退货单
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradeList);

        //2.POS登出登录，收银员2号登录，正常收银（不参与促销）和退货
        String staff2 = "15854320895";
        login(1, staff2, posLoginHttpBO, staffLoginHttpBO);
        //
        // 创建多张临时零售单
        List<RetailTrade> retailTradeList2 = new ArrayList<>(); // 记录创建出来的零售单
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList2);
        }
        // 创建多张退货单
        List<RetailTrade> returnRetailTradeList2 = createReturnSheet(retailTradeList2);

        //3.收银员2准备退出，交班，查找本地是否有临时的零售单、临时零售单计算过程和临时的收银汇总需要上传，有则上传至服务器
        retrieveRetailTradeAndUpload();

        // 4.数据汇总
        RetailTradeAggregation retailTradeAggregation = dataAggregation(retailTradeList2, returnRetailTradeList2, new Date());
        // 退出并上传收银汇总
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggregation);

        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = %s");
        rt.setConditions(new String[]{"0"});
        rt.setSn(RetailTrade.generateRetailTradeSN(Constants.posID));
        rt.setQueryKeyword(RetailTrade.generateRetailTradeSN(Constants.posID));
        rt.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        List<RetailTrade> rtList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, rt);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync失败，错误码：" + retailTradePresenter.getLastErrorCode());
        Assert.assertEquals(rtList.size(), 0, "retrieveNObjectSync失败,此时本地不应存在临时零售单");
    }

    private void retrieveRetailTradeAndUpload() throws Exception {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.fail("查询临时零售单失败！");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("上传临时零售单同步数据失败！原因：超时！");
        }
    }

    /**
     * 用于登录POS和STAFF
     */
    public static boolean login(long posID, String phone, PosLoginHttpBO pbo, StaffLoginHttpBO sbo) throws Exception {
        Constants.posID = (int) posID;//
        Constants.MyCompanySN = "668866";
        //1.pos登录
        Pos pos = new Pos();
        pos.setID((int) posID);
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
            System.out.println("pos登录超时!");
            return false;
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
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
                System.out.println("STAFF登录超时!");
                return false;
            }
        } else {
            System.out.println("pos登录失败！");
            return false;
        }
        return true;
    }

    private RetailTradeAggregation dateRetailTradeAggeration(List<RetailTrade> retailTradesWithPromotionList, List<RetailTrade> returnRetailTradeList) {
        double retailTradeAmount = 0; // 销售总金额
        double returnTradeAmount = 0; // 退货单总金额


        for (RetailTrade retailTrade : retailTradesWithPromotionList) {
            retailTradeAmount += retailTrade.getAmount();
            for (int x = 0; x < retailTrade.getListSlave1().size(); x++) {
                RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTrade.getListSlave1().get(x);
                returnTradeAmount += retailTradeCommodity.getPriceReturn();
            }
        }

        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        long maxRetailTradeAggregationIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeAggregation.class);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        retailTradeAggregation.setID((int) maxRetailTradeAggregationIDInSQLite);
        //
        retailTradeAggregation.setPosID(retailTradesWithPromotionList.get(1).getPos_ID());
        retailTradeAggregation.setTradeNO(retailTradesWithPromotionList.size() + returnRetailTradeList.size()); // 销售单数
        retailTradeAggregation.setAmount(GeneralUtil.sub(retailTradeAmount, returnTradeAmount)); //  销售总额

        retailTradeAggregation.setReserveAmount(555L); // 准备金
        retailTradeAggregation.setCashAmount(GeneralUtil.sub(retailTradeAmount, returnTradeAmount)); // 现金支付
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
        // 获取相关商品
        List<Commodity> commodityList = new ArrayList<Commodity>();
        Commodity baseModel = new Commodity();
        baseModel.setSql("where F_id in (%s,%s,%s)");
        baseModel.setConditions(new String[]{"24", "25", "26"});
        baseModel.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, baseModel);
        for (Commodity commodity1 : commodityList) {
            commodity1.setCommodityQuantity(1);
        }
        RetailTrade sellRetailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        Assert.assertNotNull(sellRetailTrade, "计算出的零售单为空");
        System.out.println("====计算出的促销后的价格" + sellRetailTrade.getAmount());
        Assert.assertTrue(sellRetailTrade.getAmount() - (commodityList.get(2).getPriceRetail() + commodityList.get(1).getPriceRetail() + commodityList.get(0).getPriceRetail()) * 0.9 <= baseModel.TOLERANCE, "验证失败");
        //
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        // 将有参与促销的商品的零售单插入本地
        for (int i = 0; i < 4; i++) {
            sellRetailTrade.setID((int) (maxRetailTradeIDInSQLite + i));
            sellRetailTrade.setSmallSheetID(1);
            sellRetailTrade.setPos_ID(1);
            sellRetailTrade.setLocalSN(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(6)));
            sellRetailTrade.setPaymentType(1);
            sellRetailTrade.setStaffID(4);
            sellRetailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            sellRetailTrade.setSourceID(-1);
            sellRetailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            sellRetailTrade.setSaleDatetime(new Date());
            sellRetailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            sellRetailTrade.setRemark("全场九折的促销零售单");
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
                retailTradeCommodity.setID((int) (maxRetailTradeCommodityIDInSQLite + x));
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
                Assert.fail("创建失败！");
            }
            //等待处理完毕
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Assert.fail("测试失败！原因：超时");
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
            Assert.assertNotNull(master, "服务器返回的对象为空！");
            retailTradesWithPromotionList.add(master);
        }
        //
    }

    private Promotion createPromotion() throws Exception {
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
        Promotion promotion = DataInput.getDiscountPromotionAllCommodity();
        if (!promotionPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, promotion, promotionSQLiteEvent)) {
            Assert.fail("CreateAsync bm1测试失败！");
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Promotion p = (Promotion) promotionSQLiteEvent.getBaseModel1();
        Assert.assertSame(promotionSQLiteEvent.getStatus(), BaseEvent.EnumEventStatus.EES_SQLite_Done, "createAsync Promotion超时！" + promotionSQLiteEvent.getStatus());
        Assert.assertEquals(promotion.compareTo(p), 0, "插入数据后对象不应该为Null！");
        Assert.assertSame(promotionSQLiteEvent.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "Create bm1时错误码应该为：EC_NoError，Create失败");
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, p)) {
            Assert.fail("创建失败!");
        }
        //
        lTimeOut = UNIT_TEST_TimeOut;
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.fail("请求服务器创建Promotion超时!");
        }
        Promotion pt = (Promotion) promotionSqLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertNotNull(pt, "服务器返回的对象为空！");
        Date dt1 = DatetimeUtil.addDays(new Date(), -1);// 为了让促销可以正常参与当前的测试，得到昨天的日期
        pt.setDatetimeStart(dt1); // 测试中将促销开始的时间修改为昨天

        return pt;
    }

    private void staffLoginToLogOut(Date date) throws Exception {
        // 1、POS.Staff登录
        Shared.login(1, posLoginHttpBO, staffLoginHttpBO);
        // 2、创建多张临时零售单
        List<RetailTrade> retailTradeList = new ArrayList<>(); // 记录创建出来的零售单
        for (int i = 0; i < 5; i++) {
            createRetailTrade(retailTradeList);
        }
        // 3、创建多张退货单
        List<RetailTrade> returnRetailTradeList = createReturnSheet(retailTradeList);
        // 4、数据汇总
        RetailTradeAggregation retailTradeAggregation = dataAggregation(retailTradeList, returnRetailTradeList, date);
        // 5、退出并上传收银汇总
        uploadRetailTradeAggregationAndLoginOut(retailTradeAggregation);
    }

    private RetailTradeAggregation dataAggregation(List<RetailTrade> retailTradeList, List<RetailTrade> returnRetailTradeList, Date date) {
        int retailTradeAmount = 0; // 销售总金额
        for (int i = 0; i < retailTradeList.size(); i++) {
            retailTradeAmount += retailTradeList.get(i).getAmount();
        }
        int returnTradeAmount = 0; // 退货单总金额
        for (int i = 0; i < returnRetailTradeList.size(); i++) {
            List<RetailTradeCommodity> returnRetailTradeCommodityList = (List<RetailTradeCommodity>) returnRetailTradeList.get(i).getListSlave1();
            for (int x = 0; x < returnRetailTradeCommodityList.size(); x++) {
                retailTradeAmount += (returnRetailTradeCommodityList.get(x).getPriceReturn() * returnRetailTradeCommodityList.get(x).getNOCanReturn());
            }
        }
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        long maxRetailTradeAggregationIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeAggregation.class);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        retailTradeAggregation.setID((int) maxRetailTradeAggregationIDInSQLite);
        //
        retailTradeAggregation.setStaffID(1);
        retailTradeAggregation.setPosID(retailTradeList.get(1).getPos_ID());
        retailTradeAggregation.setTradeNO(retailTradeList.size() + returnRetailTradeList.size()); // 销售单数
        retailTradeAggregation.setAmount(retailTradeAmount - returnTradeAmount); //  销售总额
        retailTradeAggregation.setReserveAmount(555L); // 准备金
        retailTradeAggregation.setCashAmount(retailTradeAmount - returnTradeAmount); // 现金支付
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
            Assert.fail("创建失败!");
        }

        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.fail("上传收银汇总失败！原因：超时！");
        }
        //
        Assert.assertTrue(retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError || retailTradeAggregationSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_Duplicated, "上传收银汇总后,服务器返回的错误码不正确");
        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertSame(retailTradeAggregationPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "删除操作的错误码不正确");
        //
        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        Assert.assertNull(rta, "删除后的收银汇总信息仍然存在");

        logOut();
    }

    private void createRetailTrade(List<RetailTrade> retailTradeList) throws Exception {
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertSame(retailTradeCommodityPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
            Assert.fail("创建失败！");
        }
        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.fail("测试失败！原因：超时");
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertNotNull(master, "服务器返回的对象为空！");
        retailTradeList.add(master);
    }

    private List createReturnSheet(List<RetailTrade> retailTradeList) throws InterruptedException, ParseException {
        List<RetailTrade> returnRetailTradeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RetailTrade retailTrade = retailTradeList.get(i);
            // 主表的设置
            retailTrade.setSaleAmountAlipay(retailTrade.getAmountAlipay());
            retailTrade.setSaleAmountWeChat(retailTrade.getAmountWeChat());
            retailTrade.setSourceID(retailTrade.getID().intValue());
            retailTrade.setLocalSN(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(6)));
            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            Assert.assertSame(retailTradePresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
            //
            retailTrade.setID((int) maxRetailTradeIDInSQLite);
            // 从表的设置
            List<RetailTradeCommodity> retailTradeCommoditieList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
            for (int x = 0; x < retailTradeCommoditieList.size(); x++) {
                RetailTradeCommodity retailTradeCommodity = retailTradeCommoditieList.get(x);
                long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
                Assert.assertSame(retailTradeCommodityPresenter.getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");
                retailTradeCommodity.setID((int) (maxRetailTradeCommodityIDInSQLite + x));
            }
            //
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
                Assert.fail("创建失败！");
            }
            //等待处理完毕
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Assert.fail("测试失败！原因：超时");
            }

            RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
            Assert.assertNotNull(master, "服务器返回的对象为空！");
            returnRetailTradeList.add(master);
        }
        return returnRetailTradeList;
    }
}
