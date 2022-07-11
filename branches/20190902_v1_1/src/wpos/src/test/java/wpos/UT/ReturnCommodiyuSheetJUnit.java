package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allController.BaseController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.Commodity;
import wpos.model.ErrorInfo;
import wpos.model.ReturnCommoditySheet;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReturnCommodiyuSheetJUnit extends BaseHttpTestCase {
    @Resource
    private ReturnCommoditySheetSQLiteEvent returnCommoditySheetSQLiteEvent;
    @Resource
    private ReturnCommoditySheetHttpEvent returnCommoditySheetHttpEvent;
    @Resource
    private ReturnCommoditySheetHttpBO returnCommoditySheetHttpBO;

    private Integer returnCommoditySheetID = 0;
//    private static CommodityPresenter commodityPresenter = null;
//    private static BarcodesPresenter barcodesPresenter = null;
    private final int EVENT_ID_ReturnCommodiyuSheetJUnit = 10000;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    @BeforeClass
    public void setUp() {
        super.setUp();

        posLoginHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
        }
        staffLoginHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        returnCommoditySheetSQLiteEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        returnCommoditySheetHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        returnCommoditySheetHttpBO.setHttpEvent(returnCommoditySheetHttpEvent);
        returnCommoditySheetHttpBO.setSqLiteEvent(returnCommoditySheetSQLiteEvent);
        returnCommoditySheetSQLiteEvent.setHttpBO(returnCommoditySheetHttpBO);
        returnCommoditySheetHttpEvent.setHttpBO(returnCommoditySheetHttpBO);
        //
//        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
//        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        logoutHttpEvent.setId(EVENT_ID_ReturnCommodiyuSheetJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        try {
            Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onReturnCommoditySheetHttpEvent(ReturnCommoditySheetHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ReturnCommodiyuSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }


    public static class DataInput {
        public static ReturnCommoditySheet getReturnCommoditySheet() {
            Random random = new Random();

            ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
            returnCommoditySheet.setProviderID(random.nextInt(5) + 1);
            returnCommoditySheet.setStaffID(BaseController.retailTradeAggregation.getStaffID());
            returnCommoditySheet.setCreateDate(new Date());

            return (ReturnCommoditySheet) returnCommoditySheet.clone();
        }

        protected static List<Commodity> getCommodityList() {
            Commodity commodity = new Commodity();
            commodity.setID(1);
            commodity = (Commodity) commodityPresenter.retrieve1Sync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            if (commodity == null && commodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                return null;
            }
            commodity.setBarcodeID(1);//条形码ID
            commodity.setNO(new Random().nextInt(100) + 1);

            List<Commodity> commodities = new ArrayList<Commodity>();
            commodities.add(commodity);

            return commodities;
        }
    }

    @Test
    public void test_a_Create() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        ReturnCommoditySheet returnCommoditySheet = DataInput.getReturnCommoditySheet();
        List<Commodity> commList = DataInput.getCommodityList();
        Assert.assertTrue( commList != null,"要退货的商品不能为空，不然无法进行测试！失败的原因可能是pos机没有商品数据");
        returnCommoditySheet.setListSlave1(commList);
        returnCommoditySheetHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!returnCommoditySheetHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, returnCommoditySheet)) {
            Assert.assertTrue( false,"创建失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue( false,"超时！");
        }

        ReturnCommoditySheet returnCommoditySheet1 = (ReturnCommoditySheet) returnCommoditySheetHttpBO.getHttpEvent().getBaseModel1();
        returnCommoditySheet.setIgnoreIDInComparision(true);
        if (returnCommoditySheet.compareTo(returnCommoditySheet1) != 0) {
            Assert.assertTrue( false,"创建的对象和服务器返回的不一致");
        }

        returnCommoditySheetID = returnCommoditySheet1.getID();
    }

    @Test
    public void test_b_Approver() throws InterruptedException {
        ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
        returnCommoditySheet.setID(returnCommoditySheetID);

        if (!returnCommoditySheetHttpBO.approveAsync(returnCommoditySheet)) {
            Assert.assertTrue( false,"审核失败！！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        returnCommoditySheetHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (returnCommoditySheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue( false,"超时！");
        }

        Assert.assertTrue( returnCommoditySheetHttpBO.getHttpEvent().getLastErrorCode().equals(ErrorInfo.EnumErrorCode.EC_NoError),"审核失败！！");
    }
}
