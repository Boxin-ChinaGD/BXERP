package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.PromotionScopePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ???????????????????????????????????????RetailTrade??????????????????
 */
public class RetailTradeSIT2 extends BaseHttpAndroidTestCase {
    private RetailTrade retailTrade = new RetailTrade();
    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    private RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
    private List<BaseModel> promotionList = new ArrayList<BaseModel>();
    private List<Commodity> commodityList = new ArrayList<Commodity>();
    private List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
    private List<BaseModel> tempRetailTradePromotingList = new ArrayList<>();

    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    //
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    //
    private static RetailTradePromotingPresenter retailTradePromotingPresenter = null;
    private static RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter = null;
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    //
    private PromotionScopePresenter promotionScopePresenter = null;
    private PromotionPresenter promotionPresenter = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
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
    //
    private static NtpHttpEvent ntpHttpEvent = null;
    private static NtpHttpBO ntpHttpBO = null;

    long lTimeOut = Shared.UNIT_TEST_TimeOut;
    private static final int EVENT_ID_RetailTradeSIT2 = 10000;

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

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
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
            retailTradePromotingSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
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
        promotionScopePresenter = GlobalController.getInstance().getPromotionScopePresenter();
        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
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
            commoditySQliteEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
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
        //
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        }
        ntpHttpEvent.setHttpBO(ntpHttpBO);

        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
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

        public static RetailTrade setRetailTrade(RetailTrade retailTrade, List<Commodity> commodityList) throws ParseException {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            retailTrade.setID(maxRetailTradeIDInSQLite);
            retailTrade.setVipID(0);
            retailTrade.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailTrade.setSmallSheetID(1);
            retailTrade.setLogo("");
            retailTrade.setSourceID(-1);
            retailTrade.setPaymentAccount("");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setSn(retailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) maxRetailTradeIDInSQLite);
            retailTrade.setRemark("......");
            retailTrade.setPaymentType(1);
            retailTrade.setAmount(222d);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setShopID(2);
            //
            long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityList.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                Commodity commodity = commodityList.get(i);
                //
                retailTradeCommodity.setTradeID(maxRetailTradeIDInSQLite);
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + i);
                retailTradeCommodity.setCommodityID(commodity.getID().intValue());
                retailTradeCommodity.setNO(commodity.getCommodityQuantity());
                retailTradeCommodity.setDiscount(0.900000d);
                retailTradeCommodity.setPriceReturn(22.000000d);
                retailTradeCommodity.setNOCanReturn(10);
                retailTradeCommodity.setPriceOriginal(12.000000d);
                //
//                retailTradeCommodity.setSql("where F_CommodityID = ?");
//                retailTradeCommodity.setConditions(new String[]{String.valueOf(commodity.getID())});
//                List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, retailTradeCommodity);
//                Assert.assertTrue("?????????????????????", barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                retailTradeCommodity.setBarcodeID(1);
                //
                retailTradeCommodityList.add(retailTradeCommodity);
            }
            retailTrade.setListSlave1(retailTradeCommodityList);

            return (RetailTrade) retailTrade.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT2.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeSIT2.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeSIT2 onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNReplacerAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                retailTradeList = (List<RetailTrade>) event.getListMasterTable();
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT2.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeSIT2.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeSIT2  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @throws InterruptedException
     * @throws CloneNotSupportedException
     */
    @Test
    public void testRetailTradeSIT() throws Exception {
        Shared.printTestMethodStartInfo();

//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //?????????????????? ?????????????????????
//        resetCommodity();
        //????????????
        promotionList = (List<BaseModel>) createPromotionList();

        //???????????????????????????????????????
        System.out.println("--------------------????????????????????????--------------------");
        Commodity commodity = getCommodityInSQLiteByID(161);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("????????????????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() == 0);
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade); //...??????????????????.retailTrade??????????????????????????????DataInput?????????????????????
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        //?????????????????????????????????????????????????????????????????????
        System.out.println("------------------????????????????????????---------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("???????????????????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        //????????????????????????????????????
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }

        //????????????????????????????????????
        System.out.println("-------------------??????????????????------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("???????????????????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        createRetailTradeListAndRetailTradePromtingList();
        retailTradeList.clear();//?????????retailTradeList???null???????????????????????????????????????????????????
        tempRetailTradePromotingList.clear();
    }

    @Test
    public void testCreateNRetailTradeForDuplicatedUpload() throws Exception {
        caseLog("?????????????????????");
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        syncTime();
        //????????????
        promotionList = (List<BaseModel>) createPromotionList();
        //????????????
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        retailTrade = DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("???????????????????????????????????????????????????????????????", retailTradePromoting.getListSlave1().size() > 0);
        //
        setRetailTradePromotingID(retailTradePromoting);
        retailTrade = createTempRetailTradeInSQLite(retailTrade);
        retailTradePromoting = createTempRetailTradePromotingInSQLite(retailTradePromoting);
        Assert.assertTrue("????????????SQLite???????????????????????????????????????????????????????????????", (retailTrade != null && retailTradePromoting != null));
        commodityList.clear();

        //???????????????
        createRetailTradeListAndRetailTradePromtingList();

        //??????????????????????????????????????????????????????????????????????????????,?????????????????????????????????????????????
        Assert.assertTrue("?????????????????????????????????", (retailTradeList != null && retailTradeList.size() > 0));
        for (RetailTrade rt : retailTradeList) {
            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) rt.getListSlave1();
            for (RetailTradeCommodity rtc : retailTradeCommodities) {
                retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
                Assert.assertTrue("??????????????????????????????", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }

            List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) rt.getListSlave2();
            if (retailTradePromotings != null) {
                for (RetailTradePromoting rtp : retailTradePromotings) {
                    Assert.assertTrue("????????????????????????????????????", (rtp.getListSlave1() != null && rtp.getListSlave1().size() > 0));
                    List<RetailTradePromotingFlow> retailTradePromotingFlows = (List<RetailTradePromotingFlow>) rtp.getListSlave1();
                    for (RetailTradePromotingFlow tradePromotingFlow : retailTradePromotingFlows) {
                        retailTradePromotingFlowPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tradePromotingFlow);
                        Assert.assertTrue("???????????????????????????????????????", retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                    }

                    retailTradePromotingPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                    Assert.assertTrue("??????????????????????????????", retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
            }

            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
            Assert.assertTrue("????????????????????????", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        //???????????????????????????????????????inser???sqlite??????????????????????????????????????????????????????????????????????????????
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        //??????????????????
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);
        //
        RetailTradePromoting retailTradePromotingB = new RetailTradePromoting();
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromotingB);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("???????????????????????????????????????????????????????????????", retailTradePromotingB.getListSlave1().size() > 0);
        //
        setRetailTradePromotingID(retailTradePromotingB);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromotingB);

        //??????????????????
        RetailTradePromoting retailTradePromotingC = new RetailTradePromoting();
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromotingC);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue("???????????????????????????????????????????????????????????????", retailTradePromotingC.getListSlave1().size() > 0);
        //
        setRetailTradePromotingID(retailTradePromotingC);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromotingC);

        //?????????????????????List
        createRetailTradeListAndRetailTradePromtingList();
        retailTradeList.clear();//?????????retailTradeList???null???????????????????????????????????????????????????
        tempRetailTradePromotingList.clear();

        //???????????????????????????
        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = ?");
        rt.setConditions(new String[]{"0"});
        List<RetailTrade> tmpRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        Assert.assertTrue("??????????????????????????????????????????", (tmpRetailTradeList == null || tmpRetailTradeList.size() == 0));

        List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) retrieveNTempRetailTradePromotingInSQLite();
        Assert.assertTrue("????????????????????????????????????????????????", (retailTradePromotings == null || retailTradePromotings.size() == 0));
    }

    private List<?> createPromotionList() throws InterruptedException, CloneNotSupportedException {
        List<BaseModel> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        //
        System.out.println("---------------?????????????????????????????????1???100??????20??????????????????--------------");
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
        promotion.setReturnObject(1);
        promotionHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue("??????Promotion???" + promotion + "??????!", false);
        }
        long lTimeOut = 60;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("??????????????????Promotion???????????????", false);
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("??????Promotion???" + promotion + "?????????", false);
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue("????????????????????????????????????????????????????????????", p != null);
        Date d1 = DatetimeUtil.addDays(new Date(), -1);//?????????????????????
        p.setDatetimeStart(d1);
        return p;
    }

    private void createRetailTradeListAndRetailTradePromtingList() {
        //??????????????????RetailTrade?????????
        if (retrieveNTempRetailTradeFromSQLiteAndUpload()) {
            //???????????????????????????????????????
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue("??????????????????????????????????????????????????????????????????????????????list=" + list, false);
            }

            tempRetailTradePromotingList = (List<BaseModel>) retrieveNTempRetailTradePromotingInSQLite();
            Assert.assertTrue("?????????????????????????????????RetailTradePromoting!tempRetailTradePromotingList=" + tempRetailTradePromotingList, tempRetailTradePromotingList.size() <= 0);
        }
    }

    public boolean retrieveNTempRetailTradeFromSQLiteAndUpload() {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("??????????????????????????????", false);
        }
        long lTimeOut = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            if ((retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//????????????Presenter???????????????????????????event????????????????????????????????????????????????????????????
//                    || (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)) {
//                break;
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????????????????RetailTrade?????????", false);
            return false;
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("????????????????????????????????????????????????????????????", false);
            return false;
        } else {
            return true;
        }
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
        long lTimeOut = 600;
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
    public RetailTrade createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue("???????????????????????????????????????", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("???????????????????????????????????????", false);
        }
        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("???????????????????????????????????????????????????", false);
        }
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);

        return (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param retailTradePromoting
     * @throws InterruptedException
     */
    public RetailTradePromoting createTempRetailTradePromotingInSQLite(RetailTradePromoting retailTradePromoting) throws InterruptedException {
        if (retailTradePromoting.getListSlave1().size() > 0) {
            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            if (!retailTradePromotingSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting)) {
                Assert.assertTrue("???????????????????????????????????????????????????", false);
            }
            lTimeOut = 60;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (lTimeOut <= 0) {
                Assert.assertTrue("???????????????????????????????????????????????????", false);
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("???????????????????????????????????????????????????????????????", false);
            }
            return (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
        }
        return null;
    }

    /**
     * ??????APP??????????????????????????????
     */
    private void syncTime() {
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue("??????Commodity????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ntpHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("??????????????????????????????" + ntpHttpBO.getHttpEvent().getStatus(), false);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
//                    event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    event.setRequestType(null);
                    Ntp ntp = (Ntp) event.getBaseModel1();
                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
                    //
                }
            } else {
                NtpHttpBO.TimeDifference = 0;
//                log.info("??????NTP????????????????????????????????????????????????????????????");
            }
        } else {
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }
}
