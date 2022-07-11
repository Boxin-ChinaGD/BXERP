package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.PosLoginHttpBO;
import wpos.bo.StaffLoginHttpBO;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.model.promotion.PromotionCalculator;
import wpos.utils.NetworkUtils;
import wpos.utils.ResetBaseDataUtil;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class ResetBaseDataJUnint extends BaseHttpTestCase {
    private static NetworkUtils networkUtils = new NetworkUtils();
    //
//    private PromotionPresenter promotionPresenter;
    //
//    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    @Resource
    ResetBaseDataUtil resetBaseDataUtil;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    private static boolean isSync = false; //是否同步成功

    private static final int EVENT_ID_ResetBaseDataJUnint = 10000;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                isSync = true;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @BeforeClass
    public void setUp() {
        super.setUp();
        //
//        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
//        if(smallSheetSQLiteEvent == null){
//            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
//            smallSheetSQLiteEvent.setId(EVENT_ID_ResetBaseDataJUnint);
//        }
        posLoginHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Test
    public void test_resetBaseData() throws Exception {
        // TODO wpos断wifi
        String cmd = "ipconfig /release";
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
        //无网络情况下进行重置,提示用户现在无网络无法进行重置
//        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(false);
        Thread.sleep(10000); //等待硬件就绪
        while (isNetworkAvalible()) {
            Thread.sleep(1000);
        }
        Assert.assertTrue(!resetBaseData(),"无网络下的情况也进行了重置");
        cmd = "ipconfig /renew";
        Process p2 = Runtime.getRuntime().exec(cmd);
        p2.waitFor();
////        wifiManager.setWifiEnabled(true);
//        Thread.sleep(10000); //等待硬件就绪
        //重置第一次，随机生成商品。使用PromotionCalculator类的sell()生成一张RT1并clone。查看是否有错
        while (!isNetworkAvalible()) {
            Thread.sleep(1000);
        }
        //
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //
        Assert.assertTrue(resetBaseData(),"重置失败");
        //
        List<Commodity> commodities = Shared.getCommodityList(15, true, new StringBuilder());
        Assert.assertTrue(commodities.size() > 0,"查询本地商品失败！原因可能是重置商品资料失败！");

        List<Commodity> commodityList = Shared.getSellCommodityList(commodities, 10);
        Assert.assertTrue( commodityList.size() > 0,"获取售出商品失败！");

        List<BaseModel> promotions = (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(promotions.size() > 0,"未能查询到促销信息！");
        //
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTrade = promotionCalculator.sell(commodityList, promotions, retailTradePromoting);
        System.out.println(retailTradePromoting);
        Assert.assertTrue(retailTrade != null,"未生成零售单");

        //第二次进行重置无促销
        Assert.assertTrue( resetBaseData(),"重置失败");
        //
        List<Commodity> commodities2 = Shared.getCommodityList(15, true, new StringBuilder());
        Assert.assertTrue(commodities.size() > 0,"查询本地商品失败！原因可能是重置商品资料失败！");

        List<Commodity> commodityList2 = Shared.getSellCommodityList(commodities2, 10);
        Assert.assertTrue(commodityList.size() > 0,"获取售出商品失败！");

        List<BaseModel> promotions2 = new ArrayList<BaseModel>();
        //
        RetailTradePromoting retailTradePromoting1 = new RetailTradePromoting();
        RetailTrade retailTrade2 = promotionCalculator.sell(commodities2, promotions2, retailTradePromoting1);
        System.out.println(retailTradePromoting1);
        Assert.assertTrue(retailTrade2 != null,"未生成零售单");
    }

    private boolean isNetworkAvalible() {
        if (!networkUtils.isNetworkAvalible()) {
            return false;
        }
        return true;
    }

    //进行重置基础数据
    private boolean resetBaseData() throws Exception {
        if (!networkUtils.isNetworkAvalible()) {
            //网络不可用的情况
            return false;
        } else {
            resetBaseDataUtil.setEventID(EVENT_ID_ResetBaseDataJUnint);
            resetBaseDataUtil.initObject();
            resetBaseDataUtil.ResetBaseData(false);
            //如何判断他重置成功
            return true;
        }
    }
}
