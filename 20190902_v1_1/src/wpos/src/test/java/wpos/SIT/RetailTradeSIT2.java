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
//                Assert.assertTrue("查询条形码出错", barcodesPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT2.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT2.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
                System.out.println("RetailTradeSIT2.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT2.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeSIT2  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT2) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 创建多种零售单，然后开始上传到服务器
     *
     * @throws InterruptedException
     * @throws CloneNotSupportedException
     */
    @Test
    public void testRetailTradeSIT() throws Exception {
        Shared.printTestMethodStartInfo();
        // 另一个测试方法会修改到retailTradePromoting，所以需要重置
        retailTrade = new RetailTrade();
        promotionCalculator = new PromotionCalculator();
        retailTradePromoting = new RetailTradePromoting();
        promotionList = new ArrayList<BaseModel>();
        commodityList = new ArrayList<Commodity>();
        retailTradeList = new ArrayList<RetailTrade>();
        tempRetailTradePromotingList = new ArrayList<>();
//        retailTradePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //重置所有商品 需要时可反注释
//        resetCommodity();
        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();

        //零售单中所有商品不参与促销
        System.out.println("--------------------创建第一张零售单--------------------");
        Commodity commodity = getCommodityInSQLiteByID(161);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue(retailTradePromoting.getListSlave1().size() == 0,"零售单中没有商品参与促销，计算过程为空！");
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade); //...使用全局变量.retailTrade的从表信息并不会因为DataInput中的改变而改变
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        //零售单中含有参与促销的商品也有不参与促销的商品
        System.out.println("------------------创建第二张零售单---------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"零售单中有参与促销的商品，计算过程不能为空");
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);
        commodityList.clear();

        //找出本地的所有的全场促销
        List<Promotion> pList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getScope() == 0) {
                promotionList.add(pList.get(i));
            }
        }

        //零售单中所有商品参与促销
        System.out.println("-------------------创建第三张单------------------");
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"零售单中有参与促销的商品，计算过程不能为空");
        //
        setRetailTradePromotingID(retailTradePromoting);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        createRetailTradeListAndRetailTradePromtingList();
        retailTradeList.clear();//初始化retailTradeList为null，防止误导下一次的上传，下面也一样
        tempRetailTradePromotingList.clear();
    }

    @Test
    public void testCreateNRetailTradeForDuplicatedUpload() throws Exception {
        caseLog("重复上传零售单");
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        syncTime();
        //创建促销
        promotionList = (List<BaseModel>) createPromotionList();
        //参与促销
        Commodity commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(160);
        commodityList.add(commodity);
        //
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromoting);
        retailTrade = DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromoting.getListSlave1().size() > 0,"零售单中有参与促销的商品，计算过程不能为空");
        //
        setRetailTradePromotingID(retailTradePromoting);
        retailTrade = createTempRetailTradeInSQLite(retailTrade);
        retailTradePromoting = createTempRetailTradePromotingInSQLite(retailTradePromoting);
        Assert.assertTrue( (retailTrade != null && retailTradePromoting != null),"未将插入SQLite的临时数据返回出来，无法进行接来下的测试！");
        commodityList.clear();

        //上传零售单
        createRetailTradeListAndRetailTradePromtingList();

        //将成功上传的真实零售单删除，目的是为了重复上传的时候,避免同步真实零售单时主键冲突。
        Assert.assertTrue( (retailTradeList != null && retailTradeList.size() > 0),"同步服务器的零售单为空" + retailTradeList);
        for (RetailTrade rt : retailTradeList) {
            List<RetailTradeCommodity> retailTradeCommodities = (List<RetailTradeCommodity>) rt.getListSlave1();
            for (RetailTradeCommodity rtc : retailTradeCommodities) {
                retailTradeCommodityPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtc);
                Assert.assertTrue( retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"删除零售单商品失败！");
            }

            List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) rt.getListSlave2();
            if (retailTradePromotings != null) {
                for (RetailTradePromoting rtp : retailTradePromotings) {
                    Assert.assertTrue((rtp.getListSlave1() != null && rtp.getListSlave1().size() > 0),"零售单促销表的从表为空！");
                    List<RetailTradePromotingFlow> retailTradePromotingFlows = (List<RetailTradePromotingFlow>) rtp.getListSlave1();
                    for (RetailTradePromotingFlow tradePromotingFlow : retailTradePromotingFlows) {
                        retailTradePromotingFlowPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tradePromotingFlow);
                        Assert.assertTrue( retailTradePromotingFlowPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"删除零售单促销过程表失败！");
                    }

                    retailTradePromotingPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtp);
                    Assert.assertTrue( retailTradePromotingPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"删除零售单促销失败！");
                }
            }

            retailTradePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
            Assert.assertTrue(retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"删除零售单失败！");
        }

        //将已经删除的临时零售单再次inser到sqlite中。不然上传临时零售单的方法无法找到这张重复上传的单
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromoting);

        //创建第二张单
        commodity = getCommodityInSQLiteByID(119);
        commodityList.add(commodity);
        commodity = getCommodityInSQLiteByID(1);
        commodityList.add(commodity);
        //
        RetailTradePromoting retailTradePromotingB = new RetailTradePromoting();
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromotingB);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromotingB.getListSlave1().size() > 0,"零售单中有参与促销的商品，计算过程不能为空");
        //
        setRetailTradePromotingID(retailTradePromotingB);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromotingB);

        //创建第三张单
        RetailTradePromoting retailTradePromotingC = new RetailTradePromoting();
        retailTrade = promotionCalculator.sell(commodityList, promotionList, retailTradePromotingC);
        DataInput.setRetailTrade(retailTrade, commodityList);
        Assert.assertTrue( retailTradePromotingC.getListSlave1().size() > 0,"零售单中有参与促销的商品，计算过程不能为空");
        //
        setRetailTradePromotingID(retailTradePromotingC);
        createTempRetailTradeInSQLite(retailTrade);
        createTempRetailTradePromotingInSQLite(retailTradePromotingC);

        //再次上传零售单List
        createRetailTradeListAndRetailTradePromtingList();
        retailTradeList.clear();//初始化retailTradeList为null，防止误导下一次的上传，下面也一样
        tempRetailTradePromotingList.clear();

        //本地无临时的零售单
        RetailTrade rt = new RetailTrade();
        rt.setSql("where F_SyncDatetime = %s");
        rt.setConditions(new String[]{"0"});
        List<RetailTrade> tmpRetailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, rt);
        Assert.assertTrue( (tmpRetailTradeList == null || tmpRetailTradeList.size() == 0),"本地还存在着一些临时的零售单");

        List<RetailTradePromoting> retailTradePromotings = (List<RetailTradePromoting>) retrieveNTempRetailTradePromotingInSQLite();
        Assert.assertTrue( (retailTradePromotings == null || retailTradePromotings.size() == 0),"本地还存在着一些临时的零售单促销");
    }

    private List<?> createPromotionList() throws InterruptedException, CloneNotSupportedException {
        List<BaseModel> promotionList = new ArrayList<>();
        Promotion promotion = new Promotion();
        //
        System.out.println("---------------模拟网页创建促销：商品1满100元减20元的促销活动--------------");
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
    private Commodity getCommodityInSQLiteByID(Integer id) {
        Commodity commodity = new Commodity();
        commodity.setID(id);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue( commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"在本地查找商品，错误码不正确！");
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
        promotion.setReturnObject(1);
        promotionHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue( false,"创建Promotion：" + promotion + "失败!");
        }
        long lTimeOut = 60;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue( false,"服务器返回的Promotion解析失败！");
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"创建Promotion：" + promotion + "超时！");
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue(p != null,"服务器返回的创建对象是空，或者解析为空！");
        Date d1 = DatetimeUtil.addDays(new Date(), -1);//得到昨天的日期
        p.setDatetimeStart(d1);
        return p;
    }

    private void createRetailTradeListAndRetailTradePromtingList() {
        //查找本地临时RetailTrade并上传
        if (retrieveNTempRetailTradeFromSQLiteAndUpload()) {
            //查找本地是否还有临时零售单
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue( false,"上传了所有临时零售单之后，本地不应该还有临时零售单！list=" + list);
            }

            tempRetailTradePromotingList = (List<BaseModel>) retrieveNTempRetailTradePromotingInSQLite();
            Assert.assertTrue(tempRetailTradePromotingList.size() <= 0,"到最后本地不应该有临时RetailTradePromoting!tempRetailTradePromotingList=" + tempRetailTradePromotingList);
        }
    }

    public boolean retrieveNTempRetailTradeFromSQLiteAndUpload() {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue( false,"查询临时零售单失败！");
        }
        long lTimeOut = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
//            if ((retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
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
            Assert.assertTrue(false,"解析服务器返回的RetailTrade出错！");
            return false;
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"上传临时零售单同步数据失败！原因：超时！");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 在本地查找临时RetailTradePromoting
     */
    public List<?> retrieveNTempRetailTradePromotingInSQLite() {
        System.out.println("开始查找临时RetailTradepromoting");
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            Assert.assertTrue( false,"在本地查找临时RetailTradePromoting失败！");
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
            Assert.assertTrue( false,"在本地查找临时RetailTradePromoting超时！");
        }
        return retailTradePromotingSQLiteEvent.getListMasterTable();
    }

    /**
     * 为RetailTradePromotinf设置临时ID和TradeID，，，为RetailTradePromotingFlow设置ID
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
     * 在本地插入临时零售单
     *
     * @param retailTrade
     * @throws InterruptedException
     */
    public RetailTrade createTempRetailTradeInSQLite(RetailTrade retailTrade) throws InterruptedException {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(null);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade)) {
            Assert.assertTrue( false,"在本地插入临时零售单失败！");
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue( false,"在本地插入临时零售单超时！");
        }
        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue( false,"在本地插入临时零售单错误码不正确！");
        }
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);

        return (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
    }

    /**
     * 将临时零售单计算过程插入本地
     *
     * @param retailTradePromoting
     * @throws InterruptedException
     */
    public RetailTradePromoting createTempRetailTradePromotingInSQLite(RetailTradePromoting retailTradePromoting) throws InterruptedException {
        if (retailTradePromoting.getListSlave1().size() > 0) {
            retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
            if (!retailTradePromotingSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting)) {
                Assert.assertTrue( false,"在本地插入临时零售单计算过程失败！");
            }
            lTimeOut = 60;
            while (retailTradePromotingSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (lTimeOut <= 0) {
                Assert.assertTrue( false,"在本地插入临时零售单计算过程超时！");
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue( false,"在本地插入临时零售单计算过程错误码不正确！");
            }
            return (RetailTradePromoting) retailTradePromotingSQLiteEvent.getBaseModel1();
        }
        return null;
    }

    /**
     * 同步APP的时间和服务器的时间
     */
    private void syncTime() {
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue( false,"查找Commodity失败！！");
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
            Assert.assertTrue( false,"同步失败！原因：超时" + ntpHttpBO.getHttpEvent().getStatus());
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
//                log.info("同步NTP时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}
