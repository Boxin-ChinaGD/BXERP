package com.test.bx.app;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.ResetBaseDataUtil;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ResetBaseDataJUnint extends BaseHttpAndroidTestCase {
    private static NetworkUtils networkUtils = new NetworkUtils();
    //
    private PromotionPresenter promotionPresenter;
    //
//    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;

    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    //
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_ResetBaseDataJUnint) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //
        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
//        if(smallSheetSQLiteEvent == null){
//            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
//            smallSheetSQLiteEvent.setId(EVENT_ID_ResetBaseDataJUnint);
//        }

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_ResetBaseDataJUnint);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Test
    public void test_resetBaseData() throws Exception {
        //无网络情况下进行重置,提示用户现在无网络无法进行重置
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Thread.sleep(10000); //等待硬件就绪
        while (isNetworkAvalible()) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("无网络下的情况也进行了重置", !resetBaseData());
        wifiManager.setWifiEnabled(true);
        Thread.sleep(10000); //等待硬件就绪

        //重置第一次，随机生成商品。使用PromotionCalculator类的sell()生成一张RT1并clone。查看是否有错
        while (!isNetworkAvalible()) {
            Thread.sleep(1000);
        }
        //
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //
        Assert.assertTrue("重置失败", resetBaseData());
        //
        List<Commodity> commodities = Shared.getCommodityList(15, true, new StringBuilder());
        Assert.assertTrue("查询本地商品失败！原因可能是重置商品资料失败！", commodities.size() > 0);

        List<Commodity> commodityList = Shared.getSellCommodityList(commodities, 10);
        Assert.assertTrue("获取售出商品失败！", commodityList.size() > 0);

        List<BaseModel> promotions = (List<BaseModel>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("未能查询到促销信息！", promotions.size() > 0);
        //
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
        RetailTrade retailTrade = promotionCalculator.sell(commodityList, promotions, retailTradePromoting);
        System.out.println(retailTradePromoting);
        Assert.assertTrue("未生成零售单", retailTrade != null);

        //第二次进行重置无促销
        Assert.assertTrue("重置失败", resetBaseData());
        //
        List<Commodity> commodities2 = Shared.getCommodityList(15, true, new StringBuilder());
        Assert.assertTrue("查询本地商品失败！原因可能是重置商品资料失败！", commodities.size() > 0);

        List<Commodity> commodityList2 = Shared.getSellCommodityList(commodities2, 10);
        Assert.assertTrue("获取售出商品失败！", commodityList.size() > 0);

        List<BaseModel> promotions2 = new ArrayList<BaseModel>();
        //
        RetailTradePromoting retailTradePromoting1 = new RetailTradePromoting();
        RetailTrade retailTrade2 = promotionCalculator.sell(commodities2, promotions2, retailTradePromoting1);
        System.out.println(retailTradePromoting1);
        Assert.assertTrue("未生成零售单", retailTrade2 != null);
    }

    private boolean isNetworkAvalible() {
        if (!networkUtils.isNetworkAvalible(this.getContext())) {
            return false;
        }
        return true;
    }

    //进行重置基础数据
    private boolean resetBaseData() throws Exception {
        if (!networkUtils.isNetworkAvalible(this.getContext())) {
            //网络不可用的情况
            return false;
        } else {
            ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(EVENT_ID_ResetBaseDataJUnint);
            resetBaseDataUtil.ResetBaseData(false);
            //如何判断他重置成功
            return true;
        }
    }
}
