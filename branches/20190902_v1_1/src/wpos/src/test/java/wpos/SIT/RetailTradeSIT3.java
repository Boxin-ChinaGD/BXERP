package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allController.BaseController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.CommodityPresenter;
import wpos.presenter.RetailTradeCommodityPresenter;
import wpos.presenter.RetailTradePresenter;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetailTradeSIT3 extends BaseHttpTestCase {
    private RetailTrade retailTrade = new RetailTrade();
    private List<Commodity> commodityList = new ArrayList<Commodity>();
    private List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();

//    private RetailTradePresenter retailTradePresenter = null;
    @Resource
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    private RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    private RetailTradeHttpBO retailTradeHttpBO;
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

    long lTimeOut = 60;
    private static final int EVENT_ID_RetailTradeSIT3 = 10000;

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

//        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeSIT3);
        retailTradeHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        commoditySQliteEvent.setId(EVENT_ID_RetailTradeSIT3);
        commodityHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commoditySQliteEvent.setHttpBO(commodityHttpBO);
        commoditySQliteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        //
        posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();

        logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
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

        public static RetailTrade getRetailTrade(RetailTrade retailTrade, List<Commodity> commodityList) {
            Integer maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            retailTrade.setID(maxRetailTradeIDInSQLite);
            retailTrade.setVipID(0);
            retailTrade.setStaffID(BaseController.retailTradeAggregation.getStaffID());
            retailTrade.setSmallSheetID(1);
            retailTrade.setLogo("");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setSourceID(-1);
            retailTrade.setPaymentAccount("");
            retailTrade.setDatetimeStart(new Date());
            retailTrade.setDatetimeEnd(new Date());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setSn(retailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) maxRetailTradeIDInSQLite);
            retailTrade.setRemark("......");
            retailTrade.setPaymentType(1);
            retailTrade.setAmount(190d);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setShopID(2);
            //
            Integer maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityList.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                Commodity commodity = commodityList.get(i);
                //
                retailTradeCommodity.setTradeID(Long.valueOf(retailTrade.getID()));
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + i);
                retailTradeCommodity.setCommodityID(commodity.getID().intValue());
                retailTradeCommodity.setNO(commodity.getCommodityQuantity());
                retailTradeCommodity.setDiscount(1);
                retailTradeCommodity.setPriceOriginal(commodity.getPriceRetail());
                retailTradeCommodity.setPriceReturn(22);
                retailTradeCommodity.setNOCanReturn(10);
                retailTradeCommodity.setPriceOriginal(12);
                //
                retailTradeCommodity.setSql("where F_CommodityID = ?");
                retailTradeCommodity.setConditions(new String[]{String.valueOf(commodity.getID())});
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
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT3.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeSIT3.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeSIT3 onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNReplacerAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                retailTradeList = (List<RetailTrade>) event.getListMasterTable();
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT3.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????RetailTradeSIT3.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeSIT3  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Test
    public void testRetailTradeSIT() throws InterruptedException, CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
//        retailTradePresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);

        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //??????????????????
//        resetCommodity();

        System.out.println("--------------------???????????????--------------------");
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (retrieveNTempRetailTradeInSQLite()) {
            //???????????????????????????????????????
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue(false,"???????????????????????????????????????????????????????????????????????????list:" + list.toString());
            }
        }

        Commodity oldCommodityA = getCommodityInSQLiteByIDC(163);
        Commodity oldCommodityB = getCommodityInSQLiteByIDC(164);
        //
        Commodity commodity = getCommodityInSQLiteByID(165);
        commodityList.add(commodity);
        //
        RetailTrade retailTradeTmp = DataInput.getRetailTrade(retailTrade, commodityList);
        //
        createTempRetailTradeInSQLite(retailTradeTmp);
        //???????????????????????????
        if (retrieveNTempRetailTradeInSQLite()) {
            //???????????????????????????????????????
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue( false,"??????????????????????????????????????????????????????????????????????????????");
            }
        }
        //
        Commodity newCommodityA = getCommodityInSQLiteByIDC(163);
        Commodity newCommodityB = getCommodityInSQLiteByIDC(164);
        //???????????????????????????ID???165(????????????).?????????164,163,?????????????????????????????????3,2. ????????????10???165.?????????????????????164:30??????163???20???
        if (newCommodityA.getNO() != (oldCommodityA.getNO() - 20) || newCommodityB.getNO() != (oldCommodityB.getNO() - 30)) {
            Assert.assertTrue(false,"?????????????????????????????????");
        }

        //??????pos??????????????????????????????
//        System.out.println("--------------------???????????????--------------------");
//        RetailTrade returnRetailTrade = DataInput.getReturnRetailTrade(retailTradeList.get(0));
//        createTempRetailTradeInSQLite(returnRetailTrade);
//        RetailTrade tmp = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(Constants.INVALID_CASE_ID, returnRetailTrade);
//        //???????????????????????????
//        if (retrieveNTempRetailTradeFromSQLiteAndUpload()) {
//            //???????????????????????????????????????
//            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
//            if (list.size() > 0) {
//                Assert.assertTrue("??????????????????????????????????????????????????????????????????????????????", false);
//            }
//        }
//        oldCommodityA = getCommodityInSQLiteByIDC(163);
//        oldCommodityB = getCommodityInSQLiteByIDC(164);

//        if (oldCommodityA.getNO() != (newCommodityA.getNO() + 20) || oldCommodityB.getNO() != (newCommodityB.getNO() + 30)) {
//            Assert.assertTrue("?????????????????????????????????", false);
//        }
    }


    /**
     * ???????????????????????????RetailTrade????????????
     */
    public boolean retrieveNTempRetailTradeInSQLite() {
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
     * ??????ID????????????????????????
     *
     * @param id
     * @return
     */
    private Commodity getCommodityInSQLiteByID(Integer id) {
        Commodity commodity = new Commodity();
        commodity.setID(id);
        commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
        Assert.assertTrue(commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????????????????????????????");
        if (commodity != null) {
            commodity.setCommodityQuantity(10);
            return commodity;
        }
        return null;
    }

    /**
     * ????????????????????????
     */
    public void resetCommodity() throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);//...??????????????????????????????????????????????????????????????????
        commoditySQliteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue( false,"????????????????????????!");
        }
        long lTimeOut = 100;
        while (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"?????????????????????Commodity??????!");
        }

        Assert.assertTrue( commodityHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"??????????????????????????????");
        Assert.assertTrue( commoditySQliteEvent.getListMasterTable() != null,"??????SQLite??????????????????");
    }

    /**
     * ??????ID???????????????????????????
     *
     * @return
     */
    private Commodity getCommodityInSQLiteByIDC(Integer ID) throws InterruptedException {
        Commodity comm = new Commodity();
        comm.setID(ID);

        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, comm)) {
            Assert.assertTrue(false,"Commodity???????????????");
        }
        //
        long lTimeOut = 50;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue( false,"commodity???????????????");
        }

        comm = (Commodity) commodityHttpEvent.getBaseModel1();
        if (comm != null) {
            comm.setCommodityQuantity(10);
            return comm;
        }
        return comm;
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
            Assert.assertTrue( false,"???????????????????????????????????????");
        }
        lTimeOut = 60;
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
    }
}
