package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
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
 * 该测试类主要是针对组合商品的退货操作
 */

public class RetailTradeSIT3 extends BaseHttpAndroidTestCase {
    private RetailTrade retailTrade = new RetailTrade();
    private List<Commodity> commodityList = new ArrayList<Commodity>();
    private List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();

    private RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
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

    long lTimeOut = 60;
    private static final int EVENT_ID_RetailTradeSIT3 = 10000;

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
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_RetailTradeSIT3);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
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
        if (commoditySQliteEvent == null) {
            commoditySQliteEvent = new CommoditySQLiteEvent();
            commoditySQliteEvent.setId(EVENT_ID_RetailTradeSIT3);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
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

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_RetailTradeSIT3);
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

        public static RetailTrade getRetailTrade(RetailTrade retailTrade, List<Commodity> commodityList) {
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            retailTrade.setID(maxRetailTradeIDInSQLite);
            retailTrade.setVipID(0);
            retailTrade.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
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
            long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
            for (int i = 0; i < commodityList.size(); i++) {
                RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                Commodity commodity = commodityList.get(i);
                //
                retailTradeCommodity.setTradeID(retailTrade.getID());
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT3.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT3.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeSIT3 onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateNReplacerAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                retailTradeList = (List<RetailTrade>) event.getListMasterTable();
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetailTradeSIT3.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetailTradeSIT3.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        RetailTradeSIT3  onRetailTradePromotingSQLiteEvent");
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetailTradeSIT3) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void testRetailTradeSIT() throws InterruptedException, CloneNotSupportedException, ParseException {
        Shared.printTestMethodStartInfo();
//        retailTradePresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);

        Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //重置所有商品
//        resetCommodity();

        System.out.println("--------------------创建零售单--------------------");
        //为防止本地还有多个零售单未上传，而导致商品数量不正确。上传零售单到服务器
        if (retrieveNTempRetailTradeInSQLite()) {
            //查找本地是否还有临时零售单
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue("上传了所有临时零售单之后，本地不应该还有临时零售单list:" + list.toString(), false);
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
        //上传零售单到服务器
        if (retrieveNTempRetailTradeInSQLite()) {
            //查找本地是否还有临时零售单
            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
            if (list.size() > 0) {
                Assert.assertTrue("上传了所有临时零售单之后，本地不应该还有临时零售单！", false);
            }
        }
        //
        Commodity newCommodityA = getCommodityInSQLiteByIDC(163);
        Commodity newCommodityB = getCommodityInSQLiteByIDC(164);
        //此零售单购买的商品ID是165(组合商品).子商品164,163,被组合的商品数量分别是3,2. 此单购买10个165.分别减少子商品164:30件，163。20件
        if (newCommodityA.getNO() != (oldCommodityA.getNO() - 20) || newCommodityB.getNO() != (oldCommodityB.getNO() - 30)) {
            Assert.assertTrue("商品库存没有正确计算！", false);
        }

        //目前pos不能够进行上传退货单
//        System.out.println("--------------------创建退货单--------------------");
//        RetailTrade returnRetailTrade = DataInput.getReturnRetailTrade(retailTradeList.get(0));
//        createTempRetailTradeInSQLite(returnRetailTrade);
//        RetailTrade tmp = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(Constants.INVALID_CASE_ID, returnRetailTrade);
//        //上传零售单到服务器
//        if (retrieveNTempRetailTradeFromSQLiteAndUpload()) {
//            //查找本地是否还有临时零售单
//            List<RetailTrade> list = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
//            if (list.size() > 0) {
//                Assert.assertTrue("上传了所有临时零售单之后，本地不应该还有临时零售单！", false);
//            }
//        }
//        oldCommodityA = getCommodityInSQLiteByIDC(163);
//        oldCommodityB = getCommodityInSQLiteByIDC(164);

//        if (oldCommodityA.getNO() != (newCommodityA.getNO() + 20) || oldCommodityB.getNO() != (newCommodityB.getNO() + 30)) {
//            Assert.assertTrue("商品库存没有正确计算！", false);
//        }
    }


    /**
     * 找到本地所有的临时RetailTrade，并上传
     */
    public boolean retrieveNTempRetailTradeInSQLite() {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查询临时零售单失败！", false);
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
            Assert.assertTrue("解析服务器返回的RetailTrade出错！", false);
            return false;
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("上传临时零售单同步数据失败！原因：超时！", false);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据ID查本地数据库商品
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
     * 重置所有的商品。
     */
    public void resetCommodity() throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);//...将来优化成，所有测试启动前，先将所有商品同步
        commoditySQliteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsyncC_Done);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("调用同步商品失败!", false);
        }
        long lTimeOut = 100;
        while (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求服务器同步Commodity超时!", false);
        }

        Assert.assertTrue("同步所有的商品失败！", commodityHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("同步SQLite的商品为空！", commoditySQliteEvent.getListMasterTable() != null);
    }

    /**
     * 根据ID查服务器数据库商品
     *
     * @return
     */
    private Commodity getCommodityInSQLiteByIDC(long ID) throws InterruptedException {
        Commodity comm = new Commodity();
        comm.setID(ID);

        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!commodityHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, comm)) {
            Assert.assertTrue("Commodity查询失败！", false);
        }
        //
        long lTimeOut = 50;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }

        comm = (Commodity) commodityHttpEvent.getBaseModel1();
        if (comm != null) {
            comm.setCommodityQuantity(10);
            return comm;
        }
        return comm;
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
        lTimeOut = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("在本地插入临时零售单超时！", false);
        }
        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("在本地插入临时零售单错误码不正确！", false);
        }
        retailTradeSQLiteBO.getSqLiteEvent().setHttpBO(retailTradeHttpBO);
    }
}
