package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class RetrieveCommodityInventoryJUnit extends BaseHttpTestCase {
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
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    private RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    private RetailTradeHttpBO retailTradeHttpBO;
//    private static RetailTradePresenter retailTradePresenter = null;
//    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    //
//    private static BarcodesPresenter barcodesPresenter = null;

    private List<Commodity> commodityList = new ArrayList<>();
    private static final int EVENT_ID_RetrieveCommodityInventoryJUnit = 10000;

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

        commoditySQliteEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        commodityHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commoditySQLiteBO.setSqLiteEvent(commoditySQliteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQliteEvent);
        commoditySQliteEvent.setHttpBO(commodityHttpBO);
        commoditySQliteEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        //
        retailTradeSQLiteEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        retailTradeHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
//        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();

//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();

        posLoginHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_RetrieveCommodityInventoryJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetrieveCommodityInventoryJUnit.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetrieveCommodityInventoryJUnit.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        System.out.println("#########################################################RetailTradeJUnit onRetailTradeSQLiteEvent");
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("RetrieveCommodityInventoryJUnit.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，RetrieveCommodityInventoryJUnit.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_RetrieveCommodityInventoryJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    /**
     * 1.登录
     * 2.生成一个临时零售单对象
     * 3.查询零售单中，商品的库存
     * 4.创建零售单出售商品
     * 5.发送条形码，查询库存
     *
     * @throws Exception
     */
    @Test
    public void test_a_retrieveInventory() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.登录
        Assert.assertTrue(Shared.login(2, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        //先将临时零售单上传
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue(false,"查询临时零售单失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"超时");
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue( false,"解析服务器返回的RetailTrade出错！");
        }
        //2.生成一个临时零售单对象
        Integer maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
        Assert.assertTrue( retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
        Assert.assertTrue(retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        RetailTrade rt = RetailTradeJUnit.DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite);

        //3.查询零售单中，商品的库存(在条形码表中找到对应的商品条形码)
        int[] commodityNOs1 = new int[rt.getListSlave1().size()];
        for (int i = 0; i < rt.getListSlave1().size(); i++) {
            retrieveCommodityInventory(searchBarcode(((RetailTradeCommodity) rt.getListSlave1().get(i)).getCommodityID()));
            commodityNOs1[i] = commodityList.get(0).getNO();
        }

        //4.创建零售单出售商品
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateMasterSlaveAsync_Done);
        if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
            Assert.assertTrue( false,"创建失败！");
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue(false,"测试失败！原因：超时");
        }

        RetailTrade master = (RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue( master != null,"服务器返回的对象为空！");

        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        rt.setIgnoreIDInComparision(true);
        Assert.assertTrue(rt.compareTo(master) == 0,"服务器返回的对象和上传的对象不相等！");

        //3.查询零售单中，商品的库存(在条形码表中找到对应的商品条形码)
        int[] commodityNOs2 = new int[rt.getListSlave1().size()];
        for (int i = 0; i < rt.getListSlave1().size(); i++) {
            retrieveCommodityInventory(searchBarcode(((RetailTradeCommodity) rt.getListSlave1().get(i)).getCommodityID()));
            commodityNOs2[i] = commodityList.get(0).getNO();
        }

        for (int i = 0; i < commodityNOs1.length; i++) {
            Assert.assertTrue(commodityNOs1[i] - ((RetailTradeCommodity) rt.getListSlave1().get(i)).getNO() == commodityNOs2[i],"库存变化不正确，查询失败");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_retrieveInventory() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(Shared.login(2, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());

        //异常case：传到服务器的条形码为空串
        retrieveCommodityInventory("");
        Assert.assertTrue( commodityList.size() == 0,"返回的list长度应该为0,check()不通过");//在UI层做了限制，不输入条形码不会发送请求到服务器

        //异常Case：传到服务器的条形码为错误的条形码
        retrieveCommodityInventory("00000000");
        Assert.assertTrue(commodityList.size() == 0,"返回的list长度应该为0");

        //异常case：传到服务器的条形码长度为1-6
        retrieveCommodityInventoryError("696563");
        Assert.assertTrue( commodityList.size() == 0,"返回10条Commodity");//在UI层做了限制，输入条形码长度小于7，不会发送请求到服务器

        //异常case：传到服务器的条形码长度大于7
        retrieveCommodityInventory("6965636");
        Assert.assertTrue( commodityList.size() > 0,"返回10条Commodity");
    }

    /**
     * 在barcode表中找到对应的商品条形码
     */
    private String searchBarcode(long commodityID) {
        Barcodes barcodes = new Barcodes();
        barcodes.setSql("where F_CommodityID = %s");
        barcodes.setConditions(new String[]{String.valueOf(commodityID)});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
        return barcodesList.get(0).getBarcode();
    }

    private void retrieveCommodityInventory(String barcode) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setBarcode(barcode);
        commoditySQliteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, commodity)) {
            System.out.println("输入条形码查询商品库存失败！");
            commodityList = new ArrayList<>();
            return;
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                commoditySQliteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue( lTimeOut > 0,"根据条形码查库存超时！");
        commodityList = (List<Commodity>) commoditySQliteEvent.getListMasterTable();
    }

    private void retrieveCommodityInventoryError(String barcode) throws InterruptedException {
        Commodity commodity = new Commodity();
        commodity.setBarcode(barcode);
        commoditySQliteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_Commodity_RetrieveInventory, commodity)) {
            System.out.println("输入条形码查询商品库存失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue( commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo,"根据条形码查库存超时！");
        commodityList = (List<Commodity>) commodityHttpEvent.getListMasterTable();
    }
}
