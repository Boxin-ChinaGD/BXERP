package wpos.SIT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeCommodity;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradeCommodityPresenter;
import wpos.presenter.RetailTradePresenter;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReturnsSITTest extends BaseHttpTestCase {
    @Resource
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    private RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    private RetailTradeHttpBO retailTradeHttpBO;
    @Resource
    private StaffSQLiteEvent staffSQLiteEvent;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;

//    private static RetailTradePresenter retailTradePresenter = null;
//    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;

    private static final int EVENT_ID_ReturnsSITTest = 10000;

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    public static class DataInput {
        public static RetailTrade getRetailTrade(Integer lRetailTradeMaxIDInSQLite, Integer lRtcMaxIDInSQLite) throws Exception {
            Random ran = new Random();
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(retailTrade.generateRetailTradeSN(Constants.posID));
            retailTrade.setLocalSN((int) lRetailTradeMaxIDInSQLite);
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setLogo("11");
            retailTrade.setStaffID(1);
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setPaymentType(1);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(13476);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(new Date());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setInt1(1);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(Long.valueOf(String.valueOf(lRetailTradeMaxIDInSQLite)));
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setCommodityID(1);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(1);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(222.6);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(Long.valueOf(String.valueOf(lRetailTradeMaxIDInSQLite)));
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(2);
            retailTradeCommodity2.setBarcodeID(1);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(1);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(225.6);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }
    }

    @BeforeClass
    public void setUp() {
        super.setUp();
        retailTradeSQLiteEvent.setId(EVENT_ID_ReturnsSITTest);
        retailTradeHttpEvent.setId(EVENT_ID_ReturnsSITTest);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        staffSQLiteEvent.setId(EVENT_ID_ReturnsSITTest);
        staffLoginHttpEvent.setId(EVENT_ID_ReturnsSITTest);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpBO.setSqLiteEvent(staffSQLiteEvent);
        staffSQLiteEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        posLoginHttpEvent.setId(EVENT_ID_ReturnsSITTest);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
//        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        logoutHttpEvent.setId(EVENT_ID_ReturnsSITTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("ReturnSITTest.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????ReturnSITTest.onRetailTradeHttpEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################ReturnSITTest onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("ReturnSITTest.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
            }
            System.out.println("???Event?????????SyncThread????????????ReturnSITTest.onRetailTradeSQLiteEvent()????????????");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnsSITTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }


    @Test
    public void testReturns() throws Exception {
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        List<RetailTrade> rtLsit = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        System.out.println("-------------" + rtLsit);
        //???????????????????????????????????????????????????????????????
        Integer maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue( retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");
        Integer maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue( retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");

        RetailTrade retailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
            Assert.assertTrue( false,"???????????????");
        }

        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"??????????????????????????????");
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue( master != null,"?????????????????????????????????");

        //????????????????????????????????????????????????????????????????????????
        //CASE 1:???????????????????????????????????????????????????0
        maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue( retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");
        maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue( retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????");

        RetailTrade retailTradeReturn = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);
        retailTradeReturn.setSourceID(master.getID().intValue());

        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeReturn)) {
            Assert.assertTrue( false,"???????????????");
        }

        //??????????????????
        lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"??????????????????????????????");
        }
        Assert.assertTrue(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"?????????????????????" + retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode());

        RetailTrade master2 = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue( master2 != null,"?????????????????????????????????");
        //...
        //CASE 2?????????????????????????????????.???????????????????????????????????????0

    }
}
