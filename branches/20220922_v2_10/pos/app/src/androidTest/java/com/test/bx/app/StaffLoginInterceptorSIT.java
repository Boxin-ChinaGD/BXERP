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
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.sync.SyncThread;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.model.Vip;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
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
import java.util.Random;

import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_DuplicatedSession;
import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_NoError;

public class StaffLoginInterceptorSIT extends BaseHttpAndroidTestCase {
    private RetailTradePresenter retailTradePresenter = null;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
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
    private SmallSheetFramePresenter smallSheetFramePresenter = null;
    private SmallSheetTextPresenter smallSheetTextPresenter = null;
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
    private RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;
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
    public void setUp() throws Exception {
        super.setUp();
        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }
        if (smallSheetFramePresenter == null) {
            smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        }
        if (smallSheetTextPresenter == null) {
            smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        }
        if (retailTradeAggregationPresenter == null) {
            retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
        }
        //
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);

        }
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_StaffLoginInterceptorSIT);
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
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
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
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
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

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static class DataInput {
        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite) throws Exception {
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
            retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
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

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
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

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }

        private static SmallSheetFrame frameInput = null;

        protected static final SmallSheetFrame getSmallSheetFrame(long lFrameMaxIDInSQLite, long lTextMaxIDInSQLite) throws CloneNotSupportedException {
            frameInput = new SmallSheetFrame();
            frameInput.setID(lFrameMaxIDInSQLite);
            frameInput.setDelimiterToRepeat("-");
            frameInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
            frameInput.setInt1(1);//1, 服务器返回创建的对象。0，服务器不返回创建的对象
            List<SmallSheetText> list = initSmallSheetText(lFrameMaxIDInSQLite, lTextMaxIDInSQLite);
            frameInput.setListSlave1(list);

            return (SmallSheetFrame) frameInput.clone();
        }

        protected static List<SmallSheetText> initSmallSheetText(long lFrameStartID, long lTextStartID) {
            List<SmallSheetText> list = new ArrayList<SmallSheetText>();

            SmallSheetText smallSheetText = new SmallSheetText();
            smallSheetText.setContent("博昕");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("单据号：12345678910");
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
            smallSheetText.setContent("总金额：￥500.0");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("支付方式：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("优惠：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("应付：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("微信支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("支付宝支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("现金支付：￥400.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("谢谢惠顾");
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
//            smallSheetText.setContent("博昕POS机收银系统");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("StaffLoginInterceptorSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，StaffLoginInterceptorSIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("StaffLoginInterceptorSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，StaffLoginInterceptorSIT.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_StaffLoginInterceptorSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 1.staff在POS1进行登录操作
     * 2.staff在POS1进行收银操作
     * 3.staff在POS1不退出，在POS2进行登录
     * 4.staff在POS2登录成功之后，使用在POS1的session在进行访问nbr,判断服务器返回的错误码
     */
    @Test
    public void test_staffLoginInterceptor() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.staff在POS1进行登录操作
        if (!Shared.login(1L, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        String sessionID = GlobalController.getInstance().getSessionID();

        //2.staff在POS1进行收银操作
        long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade tempRT = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //

        createTempRetailTradeInSQLite(tempRT);
        checkTempRetailTradeAndRetailTradePromoting();

        //3.staff在POS1不退出，在POS2进行登录
        if (!Shared.login(2L, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //4.staff在POS2登录成功之后，使用在POS1的session在进行访问nbr
        GlobalController.getInstance().setSessionID(sessionID);
        //a.查询商品
        Commodity comm = new Commodity();
        comm.setBarcode("6965636");
        commoditySQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, comm)) {
            System.out.println("输入条形码查询商品库存失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        commodityHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&
                commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&
                commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("查询商品超时", lTimeOut > 0);
        Assert.assertTrue("错误码不正确，错误码：" + commodityHttpEvent.getLastErrorCode(), commodityHttpEvent.getLastErrorCode() == EC_DuplicatedSession);
        BaseHttpEvent.HttpRequestStatus = 0;

        //b.查询会员
        Vip vip = new Vip();
        vip.setMobile("13545678111");
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        if (!vipHttpBO.retrieveNAsyncC(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions, vip)) {
            System.out.println("根据电话号码查询Vip失败！");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("查询会员超时", lTimeOut > 0);
        Assert.assertTrue("错误码不正确,错误码为：" + vipHttpEvent.getLastErrorCode(), vipHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);
        BaseHttpEvent.HttpRequestStatus = 0;

        //c.上传零售单
        maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        tempRT = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        //
        createTempRetailTradeInSQLite(tempRT);
        SyncThread.executeInstantly(true);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("同步请求失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        //
        Assert.assertTrue("上传零售单超时", lTimeOut > 0);
        Assert.assertTrue("错误码不正确,错误码为：" + retailTradeHttpEvent.getLastErrorCode(), retailTradeHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);
        Assert.assertTrue("全局变量的值不正确" + BaseHttpEvent.HttpRequestStatus, BaseHttpEvent.HttpRequestStatus == 1);
        BaseHttpEvent.HttpRequestStatus = 0;
        //
        deleteTempRetailTrade(tempRT);

        //d.上传小票格式
        Long maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame tempSSF = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, tempSSF)) {
            Assert.assertTrue("创建失败！" + smallSheetSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        }
        //等待处理完毕
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("测试失败！原因：超时", lTimeOut > 0);
        Assert.assertTrue("错误码不正确！错误码：" + smallSheetHttpEvent.getLastErrorCode(), smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);
        BaseHttpEvent.HttpRequestStatus = 0;
        //
        deleteTempSmallSheet(tempSSF);

        //e.重置基础资料,重置基础资料开始的就是重置条形码，在此测试中，就用重置条形码代替重置基础资料
        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes);
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (barcodesHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("重置条形码超时", lTimeOut > 0);
        Assert.assertTrue("错误码不正确", barcodesHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);

        //f.上传收银汇总
        long lRetailTradeAggregationMaxIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation(lRetailTradeAggregationMaxIDInSQLite);
        retailTradeAggregation.setPosID(1);
        if (!retailTradeAggregationHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            Assert.assertTrue("创建失败!", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeAggregationHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeAggregationHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("上传收银汇总超时！", lTimeOut > 0);
        Assert.assertTrue("错误码不正确", retailTradeAggregationHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);
        BaseHttpEvent.HttpRequestStatus = 0;
        // 不知这里的目的是什么？上面的收银汇总是失败的，这里还要删除，导致删除失败。这里注释掉
//        deleteTempRetailTradeAggregation(retailTradeAggregation);

        //g.退出登录
        if (!logoutHttpBO.logoutAsync()) {
            System.out.println("退出登录失败! ");
        }
        lTimeOut = 50;
        logoutHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (logoutHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("退出登录超时!", lTimeOut > 0);
        Assert.assertTrue("错误码不正确", logoutHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession);
    }

    /**
     * 在本地插入临时零售单
     *
     * @param retailTrade
     * @throws
     */
    private void createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        // 只用了cash支付，PaymentType应该是创建时的1,而不是4
        // retailTrade.setPaymentType(4);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue("在本地插入临时零售单失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("本地插入零售单超时", retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done);
        Assert.assertTrue("在本地插入临时零售单错误码不正确！", retailTradeSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);
    }

    /**
     * 使用SyncThread上传零售单和计算过程后，检查本地是否还存在临时零售单和临时零售单计算过程
     *
     * @throws
     */
    private void checkTempRetailTradeAndRetailTradePromoting() throws InterruptedException {
        SyncThread.executeInstantly(true);
        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = ?");
        rt.setConditions(new String[]{"0"});
        rt.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        rt.setQueryKeyword("");
        List<RetailTrade> retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        //上传
        uploadTmpRetailTradeList();
        //
        Assert.assertTrue("查本地临时零售单错误码不正确，错误码 = " + retailTradePresenter.getLastErrorCode(), retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeList.size() > 0 && lTimeOut-- > 0) {
            Thread.sleep(1000);
            retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        }
        Assert.assertTrue("超时！", lTimeOut > 0);
        Assert.assertTrue("查本地临时零售单错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("本地不应该存在临时零售单", retailTradeList.size() == 0);
    }

    private void uploadTmpRetailTradeList() throws InterruptedException {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("同步请求失败！", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("批量上传失败！原因：超时", false);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != EC_NoError) {
            Assert.assertTrue("批量上传失败", false);
        }
    }

    /**
     * 删除测试中的临时数据，防止影响其他地方
     */
    private void deleteTempRetailTrade(RetailTrade retailTrade) {
        retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        RetailTrade rt = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade);
        Assert.assertTrue("错误码不正确", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("还能找到对象，删除失败", rt == null);
    }

    private void deleteTempSmallSheet(SmallSheetFrame smallSheetFrame) {
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        Assert.assertTrue("错误码不正确", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        for (int i = 0; i < smallSheetFrame.getListSlave1().size(); i++) {
            smallSheetTextPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, (SmallSheetText) smallSheetFrame.getListSlave1().get(i));
            Assert.assertTrue("错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        //
        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        Assert.assertTrue("错误码不正确", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("还能找到对象，删除失败", ssf == null);
    }

//    private void deleteTempRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
//        retailTradeAggregationPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
//        Assert.assertTrue("错误码不正确", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        //
//        RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
//        Assert.assertTrue("错误码不正确", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
//        Assert.assertTrue("还能找到对象，删除失败", rta == null);
//    }
}
