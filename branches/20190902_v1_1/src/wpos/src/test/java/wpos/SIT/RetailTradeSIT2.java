package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.SIT.PromotionSIT;
import wpos.allController.BaseController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.model.promotion.Promotion;
import wpos.model.promotion.PromotionCalculator;
import wpos.presenter.*;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetailTradeSIT2 extends BaseHttpTestCase {
    private RetailTrade retailTrade = new RetailTrade();
    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    private RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
    private List<BaseModel> promotionList = new ArrayList<BaseModel>();
    private List<Commodity> commodityList = new ArrayList<Commodity>();
    private List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
    private List<BaseModel> tempRetailTradePromotingList = new ArrayList<>();

//    private static RetailTradePresenter retailTradePresenter = null;
    @Resource
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    private RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    private RetailTradeHttpBO retailTradeHttpBO;
    //
//    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    //
//    private static RetailTradePromotingPresenter retailTradePromotingPresenter = null;
//    private static RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter = null;
    @Resource
    private RetailTradePromotingHttpBO retailTradePromotingHttpBO;
    @Resource
    private RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO;
    @Resource
    private RetailTradePromotingHttpEvent retailTradePromotingHttpEvent;
    @Resource
    private RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
    //
//    private PromotionScopePresenter promotionScopePresenter = null;
//    private PromotionPresenter promotionPresenter = null;
    @Resource
    private PromotionSQLiteEvent promotionSQLiteEvent;
    @Resource
    private PromotionSQLiteBO promotionSQLiteBO;
    @Resource
    private PromotionHttpBO promotionHttpBO;
    @Resource
    private PromotionHttpEvent promotionHttpEvent;
    //
//    private CommodityPresenter commodityPresenter = null;
    @Resource
    private CommoditySQLiteBO commoditySQLiteBO;
    @Resource
    private CommodityHttpBO commodityHttpBO;
    @Resource
    private CommodityHttpEvent commodityHttpEvent;
    @Resource
    private CommoditySQLiteEvent commoditySQliteEvent;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;
    //
    @Resource
    private NtpHttpEvent ntpHttpEvent;
    @Resource
    private NtpHttpBO ntpHttpBO;

    long lTimeOut = Shared.UNIT_TEST_TimeOut;
    private static final int EVENT_ID_RetailTradeSIT2 = 10000;

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    @BeforeClass
    public void setUp() {
        super.setUp();

        posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
        }
        staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
//        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        retailTradeHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        //
//        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
//        retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
        retailTradePromotingSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        retailTradePromotingHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        retailTradePromotingSQLiteBO.setHttpEvent(retailTradePromotingHttpEvent);
        retailTradePromotingSQLiteBO.setSqLiteEvent(retailTradePromotingSQLiteEvent);
        retailTradePromotingHttpBO.setHttpEvent(retailTradePromotingHttpEvent);
        retailTradePromotingHttpBO.setSqLiteEvent(retailTradePromotingSQLiteEvent);
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        //
//        promotionScopePresenter = GlobalController.getInstance().getPromotionScopePresenter();
//        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        promotionSQLiteEvent.setId(EVENT_ID_RetailTradeSIT2);
        promotionHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        promotionSQLiteBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteBO.setHttpEvent(promotionHttpEvent);
        promotionHttpBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionHttpBO.setHttpEvent(promotionHttpEvent);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        //
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        commoditySQliteEvent.setId(EVENT_ID_RetailTradeSIT2);
        commodityHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQliteEvent.setHttpBO(commodityHttpBO);
        commoditySQliteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        //
        ntpHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        ntpHttpBO.setHttpEvent(ntpHttpEvent);
        ntpHttpEvent.setHttpBO(ntpHttpBO);
        //
//        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT2);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    public static class DataInput {
        static RetailTradePresenter retailTradePresenter = BaseHttpTestCase.retailTradePresenter;
        static RetailTradeCommodityPresenter retailTradeCommodityPresenter = BaseHttpTestCase.retailTradeCommodityPresenter;

        public static RetailTrade setRetailTrade(RetailTrade retailTrade, List<Commodity> commodityList) throws Exception {
            Integer maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            retailTrade.setID(maxRetailTradeIDInSQLite);
            retailTrade.setVipID(0);
            retailTrade.setStaffID(BaseController.retailTradeAggregation.getStaffID());
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
            Integer maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityList.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                Commodity commodity = commodityList.get(i);
                //
                retailTradeCommodity.setTradeID(Long.valueOf(maxRetailTradeIDInSQLite));
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeSIT2  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        // ?????????????????????????????????retailTradePromoting?????????????????????
        retailTrade = new RetailTrade();
        promotionCalculator = new PromotionCalculator();
        retailTradePromoting = new RetailTradePromoting();
        promotionList = new ArrayList<BaseModel>();
        commodityList = new ArrayList<Commodity>();
        retailTradeList = new ArrayList<RetailTrade>();
        tempRetailTradePromotingList = new ArrayList<>();
//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

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
        Assert.assertTrue(retailTradePromoting.getListSlave1().size() == 0,"????????????????????????????????????????????????????????????");
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
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"???????????????????????????????????????????????????????????????");
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
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"???????????????????????????????????????????????????????????????");
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
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
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
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"???????????????????????????????????????????????????????????????");
        //
        setRetailTradePromotingID(retailTradePromoting);
        retailTrade = createTempRetailTradeInSQLite(retailTrade);
        retailTradePromoting = createTempRetailTradePromotingInSQLite(retailTradePromoting);
        Assert.assertTrue( (retailTrade != null && retailTradePromoting != null),"????????????SQLite???????????????????????????????????????????????????????????????");
        commodityList.clear();

        //???????????????
        createRetailTradeListAndRetailTradePromtingList();

        //??????????????????????????????????????????????????????????????????????????????,?????????????????????????????????????????????
        Assert.assertTrue( (retailTradeList != null && retailTradeList.size() > 0),"?????????????????????????????????" + retailTradeList);
        for (RetailTrade rt : retailTradeList) {
            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) rt.getListSlave1();
            for (RetailTradeCommodity rtc : retailTradeCommodities) {
                retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
                Assert.assertTrue( retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????????????????");
            }

            List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) rt.getListSlave2();
            if (retailTradePromotings != null) {
                for (RetailTradePromoting rtp : retailTradePromotings) {
                    Assert.assertTrue((rtp.getListSlave1() != null && rtp.getListSlave1().size() > 0),"????????????????????????????????????");
                    List<RetailTradePromotingFlow> retailTradePromotingFlows = (List<RetailTradePromotingFlow>) rtp.getListSlave1();
                    for (RetailTradePromotingFlow tradePromotingFlow : retailTradePromotingFlows) {
                        retailTradePromotingFlowPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tradePromotingFlow);
                        Assert.assertTrue( retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"???????????????????????????????????????");
                    }

                    retailTradePromotingPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                    Assert.assertTrue( retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????????????????");
                }
            }

            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"????????????????????????");
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
        Assert.assertTrue( retailTradePromotingB.getListSlave1().size() > 0,"???????????????????????????????????????????????????????????????");
        //
        setRetailTradePromotingID(retailTradePromotingB);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromotingB);

        //??????????????????
        RetailTradePromoting retailTradePromotingC = new RetailTradePromoting();
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromotingC);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromotingC.getListSlave1().size() > 0,"???????????????????????????????????????????????????????????????");
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
        rt.setSql("where F_SyncDatetime = %s");
        rt.setConditions(new String[]{"0"});
        List<RetailTrade> tmpRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        Assert.assertTrue( (tmpRetailTradeList == null || tmpRetailTradeList.size() == 0),"??????????????????????????????????????????");

        List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) retrieveNTempRetailTradePromotingInSQLite();
        Assert.assertTrue( (retailTradePromotings == null || retailTradePromotings.size() == 0),"????????????????????????????????????????????????");
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
    private Commodity getCommodityInSQLiteByID(Integer id) {
        Commodity commodity = new Commodity();
        commodity.setID(id);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue( commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????????????????????????????");
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
            Assert.assertTrue( false,"??????Promotion???" + promotion + "??????!");
        }
        long lTimeOut = 60;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue( false,"??????????????????Promotion???????????????");
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"??????Promotion???" + promotion + "?????????");
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue(p != null,"????????????????????????????????????????????????????????????");
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
                Assert.assertTrue( false,"??????????????????????????????????????????????????????????????????????????????list=" + list);
            }

            tempRetailTradePromotingList = (List<BaseModel>) retrieveNTempRetailTradePromotingInSQLite();
            Assert.assertTrue(tempRetailTradePromotingList.size() <= 0,"?????????????????????????????????RetailTradePromoting!tempRetailTradePromotingList=" + tempRetailTradePromotingList);
        }
    }

    public boolean retrieveNTempRetailTradeFromSQLiteAndUpload() {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue( false,"??????????????????????????????");
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
            Assert.assertTrue(false,"????????????????????????RetailTrade?????????");
            return false;
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"????????????????????????????????????????????????????????????");
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
            Assert.assertTrue( false,"?????????????????????RetailTradePromoting?????????");
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
            Assert.assertTrue( false,"?????????????????????RetailTradePromoting?????????");
        }
        return retailTradePromotingSQLiteEvent.getListMasterTable();
    }

    /**
     * ???RetailTradePromotinf????????????ID???TradeID????????????RetailTradePromotingFlow??????ID
     */
    public void setRetailTradePromotingID(RetailTradePromoting retailTradePromoting) {
        Integer tempRetailTradePromotionIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromoting.class);
        Integer tempRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromotingFlow.class);
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
            Assert.assertTrue( false,"???????????????????????????????????????");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"???????????????????????????????????????");
        }
        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue( false,"???????????????????????????????????????????????????");
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
                Assert.assertTrue( false,"???????????????????????????????????????????????????");
            }
            lTimeOut = 60;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (lTimeOut <= 0) {
                Assert.assertTrue( false,"???????????????????????????????????????????????????");
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue( false,"???????????????????????????????????????????????????????????????");
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
            Assert.assertTrue( false,"??????Commodity????????????");
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
            Assert.assertTrue( false,"??????????????????????????????" + ntpHttpBO.getHttpEvent().getStatus());
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
