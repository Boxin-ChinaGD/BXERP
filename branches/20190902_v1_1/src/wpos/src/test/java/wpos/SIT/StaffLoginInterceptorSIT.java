package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.UT.RetailTradeJUnit;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.sync.SyncThread;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.BasePresenter;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static wpos.model.ErrorInfo.EnumErrorCode.EC_DuplicatedSession;
import static wpos.model.ErrorInfo.EnumErrorCode.EC_NoError;

public class StaffLoginInterceptorSIT extends BaseHttpTestCase {
    private RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private RetailTradeHttpBO retailTradeHttpBO = null;
    private RetailTradeHttpEvent retailTradeHttpEvent = null;
    //
    private CommodityHttpBO commodityHttpBO = null;
    private CommodityHttpEvent commodityHttpEvent = null;
    private CommoditySQLiteEvent commoditySQLiteEvent = null;
    private CommoditySQLiteBO commoditySQLiteBO = null;
    //
    private VipHttpBO vipHttpBO = null;
    private VipHttpEvent vipHttpEvent = null;
    private VipSQLiteEvent vipSQLiteEvent = null;
    private VipSQLiteBO vipSQLiteBO = null;
    //
    private SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private SmallSheetHttpEvent smallSheetHttpEvent = null;
    private SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    private SmallSheetHttpBO smallSheetHttpBO = null;
    //
    private BarcodesHttpBO barcodesHttpBO = null;
    private BarcodesHttpEvent barcodesHttpEvent = null;
    private BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private BarcodesSQLiteBO barcodesSQLiteBO = null;
    //
    private RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    private RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    //
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static final int EVENT_ID_StaffLoginInterceptorSIT = 10000;

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    @BeforeClass
    public void setUp() {
        super.setUp();
        //
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);

        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        //
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(barcodesSQLiteEvent, barcodesHttpEvent);
            barcodesSQLiteBO.setBarcodesPresenter(barcodesPresenter);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(retailTradeSQLiteEvent, retailTradeHttpEvent);
            retailTradeSQLiteBO.setRetailTradePresenter(retailTradePresenter);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(commoditySQLiteEvent, commodityHttpEvent);
            commoditySQLiteBO.setCommodityPresenter(commodityPresenter);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(smallSheetSQLiteEvent, smallSheetHttpEvent);
            smallSheetSQLiteBO.setSmallSheetFramePresenter(smallSheetFramePresenter);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
            retailTradeAggregationSQLiteBO.setRetailTradeAggregationPresenter(retailTradeAggregationPresenter);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(vipSQLiteEvent, vipHttpEvent);
            vipSQLiteBO.setVipPresenter(vipPresenter);
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setStaffPresenter(staffPresenter);
        //
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        vipHttpEvent.setHttpBO(vipHttpBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    @Override
    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    public static class DataInput {
        public static RetailTrade getRetailTrade(int lRetailTradeMaxIDInSQLite, int lRtcMaxIDInSQLite) throws Exception {
            Random ran = new Random();
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn("");
            retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setLogo("11");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setStaffID(1);
            retailTrade.setPaymentType(1);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setInt1(1);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID((long) lRetailTradeMaxIDInSQLite);
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(25);
            retailTradeCommodity.setCommodityID(21);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(1);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(20);
            retailTradeCommodity.setNOCanReturn(20);
            retailTradeCommodity.setPriceVIPOriginal(1);
            retailTradeCommodity.setCommodityName("xxxxxxxxxx");

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID((long) lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(22);
            retailTradeCommodity2.setBarcodeID(26);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(1);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(20);
            retailTradeCommodity2.setNOCanReturn(20);
            retailTradeCommodity2.setPriceVIPOriginal(1);
            retailTradeCommodity2.setCommodityName("xxxxxxxxxx");

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }

        private static SmallSheetFrame frameInput = null;

        protected static final SmallSheetFrame getSmallSheetFrame(int lFrameMaxIDInSQLite, int lTextMaxIDInSQLite) throws CloneNotSupportedException {
            frameInput = new SmallSheetFrame();
            frameInput.setID(lFrameMaxIDInSQLite);
            frameInput.setDelimiterToRepeat("-");
            frameInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
            frameInput.setInt1(1);//1, ?????????????????????????????????0????????????????????????????????????
            List<SmallSheetText> list = initSmallSheetText(lFrameMaxIDInSQLite, lTextMaxIDInSQLite);
            frameInput.setListSlave1(list);

            return (SmallSheetFrame) frameInput.clone();
        }

        protected static List<SmallSheetText> initSmallSheetText(long lFrameStartID, int lTextStartID) {
            List<SmallSheetText> list = new ArrayList<SmallSheetText>();

            SmallSheetText smallSheetText = new SmallSheetText();
            smallSheetText.setContent("??????");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("????????????12345678910");
            smallSheetText.setSize(25.f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("2018-9-14 9:40");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("???????????????500.0");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("???????????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("?????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("?????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("??????????????????50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("?????????????????????50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("??????????????????400.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("????????????");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("1");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("2");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("3");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("4");
            smallSheetText.setSize(20.0f);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("5");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("6");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("7");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("8");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("9");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

//            smallSheetText = new SmallSheetText();
//            smallSheetText.setContent("??????POS???????????????");
//            smallSheetText.setSize(30.0f);
//            smallSheetText.setGravity(17);
//            list.add(smallSheetText);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setID(lTextStartID + i);
                list.get(i).setFrameId(lFrameStartID);
            }

            return list;
        }

        private static RetailTradeAggregation retailTradeAggregation = null;

        protected static final RetailTradeAggregation getRetailTradeAggregation(long lRetailTradeAggregationMaxIDInSQLite) throws CloneNotSupportedException {
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
            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 3));
            retailTradeAggregation.setAmount1(1l);
            retailTradeAggregation.setAmount2(20l);
            retailTradeAggregation.setAmount3(3l);
            retailTradeAggregation.setAmount4(40l);
            retailTradeAggregation.setAmount5(5l);

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("StaffLoginInterceptorSIT.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????StaffLoginInterceptorSIT.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("StaffLoginInterceptorSIT.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????StaffLoginInterceptorSIT.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * 1.staff???POS1??????????????????
     * 2.staff???POS1??????????????????
     * 3.staff???POS1???????????????POS2????????????
     * 4.staff???POS2??????????????????????????????POS1???session???????????????nbr,?????????????????????????????????
     */
    @Test
    public void test_staffLoginInterceptor() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.staff???POS1??????????????????
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        String sessionID = GlobalController.getInstance().getSessionID();

        //2.staff???POS1??????????????????
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        //
        RetailTrade tempRT = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //

        createTempRetailTradeInSQLite(tempRT);
        checkTempRetailTradeAndRetailTradePromoting();

        //3.staff???POS1???????????????POS2????????????
        if (!Shared.login(2, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false, "????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //4.staff???POS2??????????????????????????????POS1???session???????????????nbr
        GlobalController.getInstance().setSessionID(sessionID);
        //a.????????????
        Commodity comm = new Commodity();
        comm.setBarcode("6965636");
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, comm)) {
            System.out.println("??????????????????????????????????????????");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&
                commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "??????????????????");
        Assert.assertTrue(commodityHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "?????????????????????????????????" + commodityHttpEvent.getLastErrorCode());
        BaseHttpEvent.HttpRequestStatus = 0;

        //b.????????????
        Vip vip = new Vip();
        vip.setMobile("13545678111");
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        if (!vipHttpBO.retrieveNAsyncC(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, vip)) {
            System.out.println("????????????????????????Vip?????????");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "??????????????????");
        Assert.assertTrue(vipHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "??????????????????,???????????????" + vipHttpEvent.getLastErrorCode());
        BaseHttpEvent.HttpRequestStatus = 0;

        //c.???????????????
        maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        //
        tempRT = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //
        createTempRetailTradeInSQLite(tempRT);
        SyncThread.executeInstantly(true);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue(false, "?????????????????????");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        //
        Assert.assertTrue(lTimeOut > 0, "?????????????????????");
        Assert.assertTrue(retailTradeHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "??????????????????,???????????????" + retailTradeHttpEvent.getLastErrorCode());
        Assert.assertTrue(BaseHttpEvent.HttpRequestStatus == 1, "???????????????????????????" + BaseHttpEvent.HttpRequestStatus);
        BaseHttpEvent.HttpRequestStatus = 0;
        //
        deleteTempRetailTrade(tempRT);

        //d.??????????????????
        int maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        int maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == EC_NoError, "?????????????????????");
        //
        SmallSheetFrame tempSSF = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, tempSSF)) {
            Assert.assertTrue(false, "???????????????" + smallSheetSQLiteBO.getSqLiteEvent().printErrorInfo());
        }
        //??????????????????
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "??????????????????????????????");
        Assert.assertTrue(smallSheetHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "?????????????????????????????????" + smallSheetHttpEvent.getLastErrorCode());
        BaseHttpEvent.HttpRequestStatus = 0;
        //
        deleteTempSmallSheet(tempSSF);

        //e.??????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "?????????????????????");
        Assert.assertTrue(barcodesHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "??????????????????");

        //f.??????????????????
        long lRetailTradeAggregationMaxIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeAggregation.class);
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation(lRetailTradeAggregationMaxIDInSQLite);
        retailTradeAggregation.setPosID(1);
        if (!retailTradeAggregationHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            Assert.assertTrue(false, "????????????!");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeAggregationHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "???????????????????????????");
        Assert.assertTrue(retailTradeAggregationHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "??????????????????");
        BaseHttpEvent.HttpRequestStatus = 0;
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//        deleteTempRetailTradeAggregation(retailTradeAggregation);

        //g.????????????
        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("??????????????????! ");
        }
        lTimeOut = 50;
        logoutHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(lTimeOut > 0, "??????????????????!");
        Assert.assertTrue(logoutHttpEvent.getLastErrorCode() == EC_DuplicatedSession, "??????????????????");
    }

    /**
     * ??????????????????????????????
     *
     * @param retailTrade
     * @throws
     */
    private void createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        // ?????????cash?????????PaymentType?????????????????????1,?????????4
        // retailTrade.setPaymentType(4);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue(false, "???????????????????????????????????????");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done, "???????????????????????????");
        Assert.assertTrue(retailTradeSQLiteEvent.getLastErrorCode() == EC_NoError, "???????????????????????????????????????????????????");

        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);
    }

    /**
     * ??????SyncThread????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @throws
     */
    private void checkTempRetailTradeAndRetailTradePromoting() throws InterruptedException {
        SyncThread.executeInstantly(true);
        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = %s");
        rt.setConditions(new String[]{"0"});
        rt.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        rt.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        //??????
        uploadTmpRetailTradeList();
        //
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "?????????????????????????????????????????????????????? = " + retailTradePresenter.getLastErrorCode());
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeList.size() > 0 && lTimeOut-- > 0) {
            Thread.sleep(1000);
            retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        }
        Assert.assertTrue(lTimeOut > 0, "?????????");
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "??????????????????????????????????????????");
        Assert.assertTrue(retailTradeList.size() == 0, "????????????????????????????????????");
    }

    private void uploadTmpRetailTradeList() throws InterruptedException {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue(false, "?????????????????????");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false, "????????????????????????????????????");
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue(false, "??????????????????");
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    private void deleteTempRetailTrade(RetailTrade retailTrade) {
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "??????????????????");
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue(retailTradePresenter.getLastErrorCode() == EC_NoError, "??????????????????");
        Assert.assertTrue(rt == null, "?????????????????????????????????");
    }

    private void deleteTempSmallSheet(SmallSheetFrame smallSheetFrame) {
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == EC_NoError, "??????????????????");
        for (int i = 0; i < smallSheetFrame.getListSlave1().size(); i++) {
            smallSheetTextPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (SmallSheetText) smallSheetFrame.getListSlave1().get(i));
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == EC_NoError, "??????????????????");
        }
        //
        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == EC_NoError, "??????????????????");
        Assert.assertTrue(ssf == null, "?????????????????????????????????");
    }

//    private void deleteTempRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
//        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
//        Assert.assertTrue("??????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        //
//        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
//        Assert.assertTrue("??????????????????", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("?????????????????????????????????", rta == null);
//    }
}
