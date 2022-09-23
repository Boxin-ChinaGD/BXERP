package com.test.bx.app;


import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.AppApplication;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.bo.BrandSQLiteBO;
import com.bx.erp.bo.CommodityCategoryHttpBO;
import com.bx.erp.bo.CommodityCategorySQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PosSQLiteBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipCategoryHttpBO;
import com.bx.erp.bo.VipCategorySQLiteBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.VipCategorySQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipCategoryHttpEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.model.Staff;
import com.bx.erp.model.VipCategory;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.NetworkUtils;
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
import java.util.Random;

public class SyncDataSIT extends BaseHttpAndroidTestCase {
    private static final String BarcodesData = "barcodes";
    private static final String BrandData = "brand";
    private static final String CommodityCategoryData = "commodityCategory";
    private static final String CommodityData = "commodity";
    private static final String PromotionData = "promotion";
    private static final String PosData = "pos";
    private static final String SmallSheetData = "smallSheet";
    private static final String VipCategoryData = "vipCategory";
    private static final String VipData = "vip";
    private static final String ConfigGeneralData = "configGeneral";
    private static final String ConfigCacheSizeData = "configCache";

    private static NetworkUtils networkUtils = new NetworkUtils();

    private List<SmallSheetFrame> tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();//用于保存POS机启动时在SQLite找到的需要上传的SmallSheet数据
    private List<BaseModel> tempRetailTradeList = new ArrayList<BaseModel>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTrade数据
    private List<RetailTradeAggregation> tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTradeAggregation数据

    private String syncBarcodesIDs = ""; //保存同步的Barcodes的ID，用于feedback时传参    下面定义的变量也是一样的作用
    private String syncBrandIDs = "";
    private String syncCommodityCategoryIDs = "";
    private String syncCommodityIDs = "";
    private String syncPromotionIDs = "";
    private String syncPosIDs = "";
    private String syncSmallSheetIDs = "";
    private String syncVipCategoryIDs = "";
    private String syncVipIDs = "";

    private long lTimeOut = Shared.UNIT_TEST_TimeOut;

    private boolean isRun = true;//在SmallSheet中，有创建和开机同步接收到Event，当为true时，是开机同步的情况，false时，是普通创建
    private long newPosID = 0;//用于记录新创建的POS的ID
    private static long loginPosID = 0;//用于记录当前登录的POS的ID

    private static SmallSheetFramePresenter smallSheetFramePresenter = null;
    private static SmallSheetTextPresenter smallSheetTextPresenter = null;
    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private static SmallSheetHttpEvent smallSheetHttpEvent = null;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    //
    private static RetailTradePresenter retailTradePresenter = null;
    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    //
    private static NtpHttpBO ntpHttpBO = null;
    //
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    //
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    //
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    //
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    //
    private static PosHttpBO posHttpBO = null;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosHttpEvent posHttpEvent = null;
    private static PosSQLiteEvent posSQLiteEvent = null;
    //
    private static VipCategoryHttpBO vipCategoryHttpBO = null;
    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = null;
    //
    private static VipHttpBO vipHttpBO = null;
    private static VipSQLiteEvent vipSQLiteEvent = null;
    //
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    //
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    //
    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    //
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static NtpHttpEvent ntpHttpEvent = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    private static VipCategoryHttpEvent vipCategoryHttpEvent = null;
    private static VipHttpEvent vipHttpEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static PosSQLiteBO posSQLiteBO = null;
    private static VipCategorySQLiteBO vipCategorySQLiteBO = null;
    private static VipSQLiteBO vipSQLiteBO = null;
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;

    private static final int EVENT_ID_SyncDataSIT = 10000;

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

        if (smallSheetFramePresenter == null) {
            smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        }
        if (smallSheetTextPresenter == null) {
            smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        }
        if (retailTradePresenter == null) {
            retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }
        if (retailTradeAggregationPresenter == null) {
            retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
        }
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (vipCategorySQLiteEvent == null) {
            vipCategorySQLiteEvent = new VipCategorySQLiteEvent();
            vipCategorySQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (vipCategoryHttpEvent == null) {
            vipCategoryHttpEvent = new VipCategoryHttpEvent();
            vipCategoryHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        //
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        if (commodityCategoryHttpBO == null) {
            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        if (commodityCategorySQLiteBO == null) {
            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (posHttpBO == null) {
            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (posSQLiteBO == null) {
            posSQLiteBO = new PosSQLiteBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (vipCategoryHttpBO == null) {
            vipCategoryHttpBO = new VipCategoryHttpBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        if (vipCategorySQLiteBO == null) {
            vipCategorySQLiteBO = new VipCategorySQLiteBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (configGeneralSQLiteBO == null) {
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_SyncDataSIT);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        //
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        ntpHttpEvent.setHttpBO(ntpHttpBO);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        vipHttpEvent.setHttpBO(vipHttpBO);
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Create  // 区分是上传小票还是同步小票(C/U/D)
                    || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Update
                    || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Delete) {
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) { // 如果上传失败，就跳过该小票格式
                    if (tempSmallSheetFrameList.size() > 0) {
                        tempSmallSheetFrameList.remove(0);
                    }
                    if (tempSmallSheetFrameList.size() == 0) {
                        //上传RetailTrade
                        if (tempRetailTradeList != null) {
                            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                Assert.assertTrue("查询临时零售单失败，后续同步操作终止！", false);
                            }
                        }
                    } else {
                        //上传tempSmallSheetFrameList的第一个元素
                        uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                    }
                } else {
                    System.out.println("上传或同步小票成功，小票为：" + event.getBaseModel1());
                }
            } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_RetrieveNC) {
                System.out.println("ERT_SmallSheet_RetrieveN已完成后");
            } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) { // 同步小票
                afterFeedback(event, VipCategoryData);
            } else {
                System.out.println("未处理的情况,event=" + event);
            }

        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            //开机同步SmallSheet上传部分
            if (isRun) {
                if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done) {
                    if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                        event.setEventTypeSQLite(null);
                        tempSmallSheetFrameList.remove(0);
                        if (tempSmallSheetFrameList.size() == 0) {
                            //上传RetailTrade
                            if (tempRetailTradeList != null) {
                                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                                if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                    Assert.assertTrue("查询临时零售单失败！", false);
                                }
                            }
                        } else {
                            //上传tempSmallSheetFrameList的第一个元素
                            uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                        }
                    }
                } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {//开机同步SmallSheet RetrieveN部分
                    beforeFeedback(event, syncSmallSheetIDs, SmallSheetData, VipCategoryData);
                }
            }
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("SyncDataSIT.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，SyncDataSIT.onRetailTradeHttpEvent()不用处理");
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            System.out.println("#########################################################SyncDataSIT onRetailTradeSQLiteEvent");
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                event.setEventTypeSQLite(null);
                //
                if (tempRetailTradeAggregationList.size() > 0) {
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    syncTime();
                }
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                System.out.println("SyncDataSIT.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            System.out.println("该Event已经在SyncThread中处理，SyncDataSIT.onRetailTradeSQLiteEvent()不用处理");
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                tempRetailTradeAggregationList.remove(0);
                //
                if (tempRetailTradeAggregationList.size() > 0) {
                    //继续上传收银汇总
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    syncTime();
                }
            }
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                event.setRequestType(null);
                Ntp ntp = (Ntp) event.getBaseModel1();
                NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
                retrieveNBarcodes();
            }
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, BrandData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            beforeFeedback(event, syncBarcodesIDs, BarcodesData, BrandData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, CommodityCategoryData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            beforeFeedback(event, syncBrandIDs, BrandData, CommodityCategoryData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, CommodityData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            beforeFeedback(event, syncCommodityCategoryIDs, CommodityCategoryData, CommodityData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, PromotionData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done) {
                beforeFeedback(event, syncCommodityIDs, CommodityData, PromotionData);
            }
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, PosData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            beforeFeedback(event, syncPromotionIDs, PromotionData, PosData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, SmallSheetData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            beforeFeedback(event, syncPosIDs, PosData, SmallSheetData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategoryHttpEvent(VipCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            afterFeedback(event, ConfigGeneralData);
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
            retrieveNConfigGeneral();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_SyncDataSIT) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static SmallSheetFrame frameInput = null;
        private static RetailTrade retailTradeInput = null;

        protected static final SmallSheetFrame getSmallSheetFrame(long lFrameMaxIDInSQLite, long lTextMaxIDInSQLite) throws CloneNotSupportedException {
            long lFrameStartID = lFrameMaxIDInSQLite;
            long lTextStartID = lTextMaxIDInSQLite;

            frameInput = new SmallSheetFrame();
            frameInput.setID(lFrameStartID);
            frameInput.setDelimiterToRepeat("-"); // 分隔符
            frameInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
            frameInput.setReturnObject(1);//1, 服务器返回创建的对象。0，服务器不返回创建的对象
            frameInput.setCreateDatetime(new Date());
            List<SmallSheetText> list = initSmallSheetText(lFrameStartID, lTextStartID);
            frameInput.setListSlave1(list);

            return (SmallSheetFrame) frameInput.clone();
        }

        protected static List<SmallSheetText> initSmallSheetText(long lFrameStartID, long lTextStartID) {
            List<SmallSheetText> list = new ArrayList<SmallSheetText>();

            SmallSheetText smallSheetText = new SmallSheetText();
            smallSheetText.setContent("博昕");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("单据号：12345678910");
            smallSheetText.setSize(25.f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("2018-9-14 9:40");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("总金额：￥500.0");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("支付方式：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("微信支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("支付宝支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("现金支付：￥400.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("谢谢惠顾");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("博昕POS机收银系统");
            smallSheetText.setSize(30.0f);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("博昕POS机收银系统20");
            smallSheetText.setSize(30.0f);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setID(lTextStartID + i);
                list.get(i).setFrameId(lFrameStartID);
            }

            return list;
        }

        public static RetailTrade getRetailTrade(long lRetailTradeMaxIDInSQLite, long lRtcMaxIDInSQLite, int posID) throws CloneNotSupportedException, ParseException {
            Random ran = new Random();
            RetailTrade retailTrade = new RetailTrade();
            retailTrade.setID(lRetailTradeMaxIDInSQLite);
            retailTrade.setSn(RetailTrade.generateRetailTradeSN(posID));
            retailTrade.setLocalSN((int) lRetailTradeMaxIDInSQLite);
            retailTrade.setPos_ID(Constants.posID);
            retailTrade.setLogo("11");
            retailTrade.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTrade.setStaffID(1);
            retailTrade.setPaymentType(1);
            retailTrade.setPaymentAccount("12");
            retailTrade.setStatus(1);
            retailTrade.setRemark("11111");
            retailTrade.setSourceID(-1);
            retailTrade.setAmount(2222.2);
            retailTrade.setAmountCash(retailTrade.getAmount());
            retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
            retailTrade.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setSaleDatetime(Constants.getDefaultSyncDatetime2());
            retailTrade.setWxOrderSN("wxsn" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setWxRefundSubMchID("wxid" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailTrade.setSyncType(BasePresenter.SYNC_Type_C);
            retailTrade.setReturnObject(1);

            retailTrade.setDatetimeStart(Constants.getDefaultSyncDatetime2());
            retailTrade.setDatetimeEnd(Constants.getDefaultSyncDatetime2());

            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            retailTradeCommodity.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity.setID(lRtcMaxIDInSQLite);
            retailTradeCommodity.setBarcodeID(25);
            retailTradeCommodity.setCommodityID(21);
            retailTradeCommodity.setNO(20);
            retailTradeCommodity.setPriceOriginal(222.6);
            retailTradeCommodity.setDiscount(1);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity.setPriceReturn(20);

            RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
            retailTradeCommodity2.setTradeID(lRetailTradeMaxIDInSQLite);
            retailTradeCommodity2.setID(lRtcMaxIDInSQLite + 1);
            retailTradeCommodity2.setCommodityID(22);
            retailTradeCommodity2.setBarcodeID(26);
            retailTradeCommodity2.setNO(40);
            retailTradeCommodity2.setPriceOriginal(225.6);
            retailTradeCommodity2.setDiscount(1);
            retailTradeCommodity2.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity2.setSyncType(BasePresenter.SYNC_Type_C);
            retailTradeCommodity2.setPriceReturn(20);

            List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
            listRetailTradeCommodity.add(retailTradeCommodity);
            listRetailTradeCommodity.add(retailTradeCommodity2);

            retailTrade.setListSlave1(listRetailTradeCommodity);

            return retailTrade;
        }

        public static RetailTradeAggregation getRetailTradeAggregation(long lRetailTradeAggregationID) throws CloneNotSupportedException, ParseException, InterruptedException {
            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setID(lRetailTradeAggregationID);
            retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailTradeAggregation.setPosID(Constants.posID);
//            retailTradeAggregation.setTradeNO(0);
//            retailTradeAggregation.setAmount(0);
//            retailTradeAggregation.setReserveAmount(0);
//            retailTradeAggregation.setCashAmount(0);
//            retailTradeAggregation.setWechatAmount(0);
//            retailTradeAggregation.setAlipayAmount(0);
            retailTradeAggregation.setWorkTimeStart(new Date());
            retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(retailTradeAggregation.getWorkTimeStart(), 1));

            return (RetailTradeAggregation) retailTradeAggregation.clone();
        }

        protected static final Commodity getCommodity() throws CloneNotSupportedException {
            Commodity commodityInput = new Commodity();
            commodityInput.setBarcode(String.format("%09d", System.currentTimeMillis() % 1000000000));
            commodityInput.setStatus(0);
            commodityInput.setName("可乐薯片" + System.currentTimeMillis() % 1000000);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("箱");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceRetail(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceVIP(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceWholesale(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(Math.abs(new Random().nextInt(18) + 1));
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1) + 1));//
            commodityInput.setRefCommodityID(0);
            commodityInput.setRefCommodityMultiple(1);
            commodityInput.setNO(Math.abs(new Random().nextInt(18000)));
            commodityInput.setTag("111");
            commodityInput.setType(0);
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark(""); // 期初值备注
            commodityInput.setOperatorStaffID(1); // StaffID
//            commodityInput.setString1("asdkjvhiasd" + System.currentTimeMillis() % 1000000); // 条形码
            commodityInput.setPropertyValue1("自定义内容1");
            commodityInput.setPropertyValue2("自定义内容2");
            commodityInput.setPropertyValue3("自定义内容3");
            commodityInput.setPropertyValue4("自定义内容4");
            commodityInput.setCreateDatetime(new Date());
            commodityInput.setUpdateDatetime(new Date());
            commodityInput.setCreateDate(new Date());
            commodityInput.setRefCommodityMultiple(0);
            commodityInput.setProviderIDs("1");
            commodityInput.setReturnObject(1);
            commodityInput.setMultiPackagingInfo(commodityInput.getBarcode() + ";3;10;10;10;8;8;");
            return (Commodity) commodityInput.clone();
        }
    }

    protected void syncData() throws InterruptedException {
        isRun = true;
        configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        //  TODO 下面方法，创建普通对象失败，错误码=EC_WrongFormatForInputField,错误信息=创建的小票格式的总数已经到达上限，不允许再创建新的小票格式！
        isSyncData();
        lTimeOut = 200;
        while (configGeneralSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (configGeneralSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            configGeneralSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            System.out.println("开机同步完成！--------------------------------------");
        } else {
            Assert.assertTrue("开机同步超时！" + configGeneralSQLiteEvent.getStatus(), false);
        }
    }

    protected SmallSheetFrame createSmallSheetFrameInServerAndSQLite(SmallSheetFrame tempSSF) throws InterruptedException {
        tempSSF.setReturnObject(1);
        isRun = false;
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, tempSSF)) {
            Assert.assertTrue("小票格式创建失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("创建小票格式超时！" + smallSheetSQLiteEvent.getStatus(), false);
        }
        isRun = true;
        SmallSheetFrame createSSF = (SmallSheetFrame) smallSheetHttpEvent.getBaseModel1();
        tempSSF.setIgnoreIDInComparision(true);
        Assert.assertTrue("临时的tempSSF与服务器返回的newSSF不一致！", tempSSF.compareTo(createSSF) == 0);

        return createSSF;
    }

    protected void createSmallSheetInSQLite(SmallSheetFrame tmpCreateSmallSheet) throws InterruptedException {
        tmpCreateSmallSheet.setReturnObject(1);
        isRun = false;
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, tmpCreateSmallSheet)) {
            Assert.assertTrue("小票格式创建失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("创建小票格式超时！" + smallSheetSQLiteEvent.getStatus(), false);
        }
        List<SmallSheetFrame> tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("本地C型临时SmallSheet应该存在！", tmpSmallSheetList.size() > 0);
    }

    protected void updateSmallSheetInSQLite(SmallSheetFrame smallSheetFrame) throws InterruptedException {
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_UpdateAsync);
        if (!smallSheetSQLiteBO.updateAsyncNoUpload(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame)) {
            Assert.assertTrue("创建U型临时数据失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("本地创建U型临时数据超时！", false);
        }
    }

//    protected void logout() throws InterruptedException {
//        if (!logoutHttpBO.logoutAsync()) {
//            System.out.println("退出登录失败! ");
//        }
//        lTimeOut = Shared.UNIT_TEST_TimeOut;
//        logoutHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        while (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (logoutHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            System.out.println("退出登录超时!");
//        }
//    }

    protected void updateSmallSheetInServerAndSQLite(SmallSheetFrame updateSmallSheet) throws InterruptedException {
        isRun = false;
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, updateSmallSheet)) {
            Assert.assertTrue("修改小票格式失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("修改小票格式超时！", false);
        }
        isRun = true;
        SmallSheetFrame updateSSF = (SmallSheetFrame) smallSheetHttpEvent.getBaseModel1();
        updateSmallSheet.setIgnoreIDInComparision(true);
        Assert.assertTrue("修改后的createSSF与服务器返回的updateSSF不一致！", updateSmallSheet.compareTo(updateSSF) == 0);
    }

    protected void deleteSmallSheetInSQLite(SmallSheetFrame deleteSmallSheet) throws InterruptedException {
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_DeleteAsync);
        if (!smallSheetSQLiteBO.deleteAsyncNoUpload(BaseSQLiteBO.INVALID_CASE_ID, deleteSmallSheet)) {
            Assert.assertTrue("创建D型临时SmallSheet失败！", false);
        }
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("在本地创建D型临时SmallSheet超时！", false);
        }
    }

    /**
     * 1.posA登录，自动同步数据****
     * 2.posA创建一个SmallSheet
     * 3.模拟网页增加posB
     * <p>
     * 4.posA退出登录，posB登录，自动同步数据****
     * 5.posB修改pos1创建的SmallSheet
     * <p>
     * 6.posB退出登录，posA登录，自动同步数据，检查被修改的SmallSheet是否同步成功****
     * 7.创建临时收银汇总还有临时零售单
     * <p>
     * 8.posA退出登录，posB登录，自动同步数据，查看本地SQLite是否还有临时收银汇总和临时零售单，然后进行收银******
     * 9.检查posB收银是否正确
     * <p>
     * 10.创建临时SmallSheet
     * 11.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
     * <p>
     * 12.在本地修改上一步骤创建的SmallSheet，作为临时数据
     * 13.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
     * <p>
     * 14.删除刚刚创建的SmallSheet, 在本地做标记，作为临时数据
     * 15.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
     * <p>
     * 16.创建商品，POSB退出登录，POSA登录
     * 17.开机同步，检查retrieveN返回的Commodity有无对象
     * <p>
     * 18.退出登录，POSA再次登录，检查retrieveN返回的Commodity有无对象
     */
    @Test
    public void test_a_SyncData1() throws Exception {
        Shared.printTestMethodStartInfo();

        //1.pos1登录，自动同步数据
        System.out.println("----------------------1.pos1登录，自动同步数据");
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        loginPosID = 1l;
        System.out.println("开始同步数据1111----------------------------------------");
        syncData();

        //2.pos1创建一个SmallSheet
        System.out.println("----------------------2.pos1创建一个SmallSheet");
        Long maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("查找Frame最大ID，错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Long maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("查找Text最大ID，错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame tempSSF = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        //
        SmallSheetFrame createSSF = createSmallSheetFrameInServerAndSQLite(tempSSF);

        //3.模拟网页增加pos2
//        logOut();
        System.out.println("---------------------3.模拟网页增加pos2");
        if (!Shared.login(1l, Shared.OP_Phone, Shared.PASSWORD_DEFAULT, BaseModel.EnumBoolean.EB_Yes.getIndex(), Shared.nbr_CompanySN, posLoginHttpBO, staffLoginHttpBO, BaseModel.EnumBoolean.EB_NO.getIndex())) {
            Assert.assertTrue("登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        Pos pos = PosSIT.DataInput.getPos();
        pos.setCompanySN("668866");
        pos.setReturnObject(1);
        Pos createPos = createPos(pos);
        Assert.assertTrue("create pos服务器返回的错误码不正确！posHttpEvent.getLastErrorCode() = " + posHttpEvent.getLastErrorCode(), posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("返回的对象不能为空！", createPos != null);
        //...结果验证

        //4.posA退出登录，posB登录
        System.out.println("----------------------4.posA退出登录，posB登录");
//        logOut();
        // posB登录
        System.out.println("新创建的Pos的ID为：" + newPosID + "=====" + createPos.getID());
        Pos posPaw = getPassWord(pos);
        newPosID = posPaw.getID();
        Assert.assertTrue("登录失败", login(newPosID, posPaw.getPasswordInPOS(), posLoginHttpBO, staffLoginHttpBO));
        loginPosID = newPosID;
        System.out.println("开始同步数据22222----------------------------------------");
        //查看Log是否有数据
        syncData();

        //5.posB修改pos1创建的SmallSheet
        System.out.println("--------------------5.posB修改pos1创建的SmallSheet");
        createSSF.setLogo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        createSSF.setReturnObject(1);
        //
        updateSmallSheetInServerAndSQLite(createSSF);

        //6.posB退出登录，posA登录，自动同步数据，检查被修改的SmallSheet是否同步成功
        System.out.println("----------------------6.posB退出登录，posA登录，自动同步数据，检查被修改的SmallSheet是否同步成功");
        logOut();

        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        loginPosID = 1l;
        //
        System.out.println("开始同步数据33333----------------------------------------");
        syncData();

//        //检查是否在开机同步的时候把posB修改的SmallSheet同步完成，只需要RN，判断服务器是否有返回SmallSheet即可
//        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        isRun = false;
//        System.out.println("-------检查是否在开机同步的时候把posB修改的SmallSheet同步完成，只需要RN，判断服务器是否有返回SmallSheet即可");
//        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Assert.assertTrue("POS机启动时，同步SmallSheet失败！", false);
//        }
//        lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//        if (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            Assert.assertTrue("检查被修改的SmallSheet是否在开机的时候被同步，超时！", false);
//        }
//        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetHttpEvent.getListMasterTable();
//        Assert.assertTrue("被修改的SmallSheet是否在开机的时候同步失败！", frameList.size() == 0);

        //删除测试数据避免后面运行测试创建小票格式失败，
        deleteSmallSheetFrameInServerAndSQLite(createSSF);

        //7.创建临时收银汇总还有临时零售单,然后插入本地SQLite，用于开启启动时使用
        System.out.println("---------------7.创建临时收银汇总还有临时零售单,然后插入本地SQLite，用于开启启动时使用");
        for (int i = 0; i < 3; i++) {
            long maxRetailTradeAggregationIDInSQLite = retailTradeAggregationPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation(maxRetailTradeAggregationIDInSQLite);
            retailTradeAggregation.setPosID(Constants.posID);
            BaseActivity.retailTradeAggregation.setWorkTimeStart(retailTradeAggregation.getWorkTimeStart());
            retailTradeAggregationPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
            Assert.assertTrue("创建临时收银汇总时，错误码不正确！", retailTradeAggregationPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Thread.sleep(2000);
        }

        for (int i = 0; i < 4; i++) {
            Long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("查找RetailTrade最大ID，错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            Long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            Assert.assertTrue("查找RetailTradeCommodity最大ID，错误码不正确！", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            //
            RetailTrade tempRetailTrade = DataInput.getRetailTrade(maxRetailTradeIDInSQLite, maxRetailTradeCommodityIDInSQLite, 1);
            List<RetailTradeCommodity> commList = (List<RetailTradeCommodity>) tempRetailTrade.getListSlave1();
            for (RetailTradeCommodity rtc : commList) {
                rtc.setID(++maxRetailTradeCommodityIDInSQLite);
            }
            retailTradePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, tempRetailTrade);
            Assert.assertTrue("将Retailtrade插入本地SQLite时，错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        //8.posA退出登录，posB登录，查看本地SQLite应该没有临时收银汇总和临时零售单
        configGeneralSQLiteEvent.setData(null);
        System.out.println("---------------------------8.posA退出登录，posB登录，查看本地SQLite应该没有临时收银汇总和临时零售单");
//        logOut();

        //
        Assert.assertTrue("登录失败", login(newPosID, posPaw.getPasswordInPOS(), posLoginHttpBO, staffLoginHttpBO));
        loginPosID = newPosID;
        System.out.println("开始同步数据4444444----------------------------------------");
        System.out.println("xxxxxxxxxxxx 里面是否有值：" + tempRetailTradeList);
        System.out.println("xxxxxxxxxxxx 里面是否有值：" + tempRetailTradeList.size());
        syncData();


        //9.检查posB收银是否正确
        System.out.println("------------------------9.检查posB收银是否正确");
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSql("where F_SyncDatetime = ?");
        retailTrade.setConditions(new String[]{"0"});
        retailTrade.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        retailTrade.setQueryKeyword("");
        List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
        retailTradeList = (List<RetailTrade>) retailTradePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNByConditions, retailTrade);
        Assert.assertTrue("判断收银是否正确时，错误码不正确！", retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        System.out.println("xxxxxxx   " + retailTradeList);
        Assert.assertTrue("收银失败！还有" + retailTradeList.size() + "张零售单并未上传", retailTradeList.size() == 0);

        //10.创建临时SmallSheet
        System.out.println("------------------------10.创建临时SmallSheet");
        Long maxFrameIDInSQLite2 = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("查找Frame最大ID，错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Long maxTextIDInSQLite2 = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("查找Text最大ID，错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame tmpCreateSmallSheet = DataInput.getSmallSheetFrame(maxFrameIDInSQLite2, maxTextIDInSQLite2);
        //
        smallSheetSQLiteEvent.setHttpBO(null); // 不上传nbr
        createSmallSheetInSQLite(tmpCreateSmallSheet);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO); // 恢复
        //11.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
        System.out.println("--------------------11.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在");
        logOut();

        //
        Assert.assertTrue("登录失败", login(newPosID, posPaw.getPasswordInPOS(), posLoginHttpBO, staffLoginHttpBO));
        loginPosID = newPosID;
        System.out.println("开始同步数据555555555----------------------------------------");
        syncData();
        //
        SmallSheetFrame createSmallSheet = (SmallSheetFrame) smallSheetHttpEvent.getBaseModel1();
        System.out.println("打印创建的SmallSheet：" + createSmallSheet.toString());
        //
        List<SmallSheetFrame> tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("开机同步后，本地C型临时SmallSheet不应该存在！", tmpSmallSheetList.size() == 0);

        //12.在本地修改上一步骤创建的SmallSheet，作为临时数据
        System.out.println("------------------12.在本地修改上一步骤创建的SmallSheet，作为临时数据");
        createSmallSheet.setLogo("update-update-update");
        SmallSheetFrame updateSmallSheet = createSmallSheet;
        updateSmallSheetInSQLite(updateSmallSheet);
        //
        tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("开机同步后，本地U型临时SmallSheet应该存在！", tmpSmallSheetList.size() > 0);

        //13.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
        System.out.println("--------------------13.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在");
        logOut();
        //
        Assert.assertTrue("登录失败", login(newPosID, posPaw.getPasswordInPOS(), posLoginHttpBO, staffLoginHttpBO));
        loginPosID = newPosID;
        System.out.println("开始同步数据6666666666666----------------------------------------");
        syncData();
        //
        tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("开机同步后，本地C型临时SmallSheet不应该存在！", tmpSmallSheetList.size() == 0);

        //14.删除刚刚创建的SmallSheet, 在本地做标记，作为临时数据
        System.out.println("--------------------14.删除刚刚创建的SmallSheet, 在本地做标记，作为临时数据");
        deleteSmallSheetInSQLite(updateSmallSheet);
        //
        tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("开机同步后，本地D型临时SmallSheet应该存在！", tmpSmallSheetList.size() > 0);

        //15.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在
        System.out.println("--------------------15.退出登录，posB登录，自动同步数据，判断临时SmallSheet是否存在");
        logOut();

        //
        Assert.assertTrue("登录失败", login(newPosID, posPaw.getPasswordInPOS(), posLoginHttpBO, staffLoginHttpBO));
        loginPosID = newPosID;
        System.out.println("开始同步数据6666666666666----------------------------------------");
        syncData();
        //
        tmpSmallSheetList = retrieveNSmallSheetTempDataInSQLite();
        Assert.assertTrue("开机同步后，本地C型临时SmallSheet不应该存在！", tmpSmallSheetList.size() == 0);

        System.out.println("********************Case1完成***********************");
        //修改posSN为本机的
        Constants.MyPosSN = AppApplication.getPOS_SN();

        //16.创建商品，POSB退出登录，POSA登录
        System.out.println("--------------------16.创建商品，POSB退出登录，POSA登录");
        createCommodity();
        //
        logOut();
        //
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        loginPosID = 1l;

        //17.开机同步，检查retrieveN返回的Commodity有无对象
        System.out.println("开始同步数据77777777777777----------------------------------------");
        syncData();
        //
        Assert.assertTrue("同步商品失败", commodityHttpEvent.getListMasterTable().size() > 0);

        //18.退出登录，POSA再次登录，检查retrieveN返回的Commodity有无对象
        logOut();
        //
        Shared.login(1l, posLoginHttpBO, staffLoginHttpBO);
        loginPosID = 1l;
        //
        System.out.println("开始同步数据88888888888----------------------------------------");
        syncData();
        //
        Assert.assertTrue("同步商品失败", commodityHttpEvent.getListMasterTable().size() == 0);
    }

    protected void deleteSmallSheetFrameInServerAndSQLite(SmallSheetFrame ssf) throws Exception {
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, ssf);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        // 删除小票没有返回对象，event status = EES_Http_Done
        while (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("删除小票格式超时!" + smallSheetHttpBO.getHttpEvent().getStatus(), false);
        }
        Assert.assertTrue("服务器返回的错误码不正确, 错误码：" + smallSheetHttpBO.getHttpEvent().getLastErrorCode(), smallSheetHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    private void isSyncData() throws InterruptedException {
        System.out.println("configGeneralSQLiteEvent.getData():   " + configGeneralSQLiteEvent.getData());
        if (!networkUtils.isNetworkAvalible(getContext())) {
            NtpHttpBO.TimeDifference = 0;
        } else {
            tempSmallSheetFrameList = retrieveNSmallSheetTempDataInSQLite();
            tempRetailTradeAggregationList = retrieveNREtailTradeAggregationTempDataInSQLite();
            if (tempSmallSheetFrameList.size() > 0) {
                uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
//                waitSyncDone();
            } else {
                if (tempRetailTradeList != null) {
                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                    if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                        Assert.assertTrue("查询临时零售单失败！", false);
                    }
//                    waitSyncDone();
                } else if (tempRetailTradeAggregationList.size() > 0) {
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    syncTime();//...
                }
            }
        }
    }

    //目前获取的是公司编号为668866的
    protected Pos getPassWord(Pos pos) throws InterruptedException {
        pos.setCompanySN("668866");
        Constants.MyPosSN = pos.getPos_SN();
        posHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, pos)) {
            Assert.assertTrue("调用R1失败", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        if (posHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done || posHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("请求R1失败", false);
        }

        Pos p = (Pos) posHttpBO.getHttpEvent().getBaseModel1();
        if (p != null) {
            Assert.assertTrue("在POS机本地没有明文密码的时候说明是第一次登录，请求服务器可以返回明文密码！", p.getPasswordInPOS() != null && !"".equals(p.getPasswordInPOS()));
        } else {
            Assert.assertTrue("服务器无法识别该POS机", false);
        }

        return p;
    }

    /**
     * 用于登录POS和STAFF
     */
    protected boolean login(long posID, String passwordInPOS, PosLoginHttpBO pbo, StaffLoginHttpBO sbo) throws InterruptedException {
        //1.pos登录
        Pos pos = new Pos();
        pos.setID(posID);
        pos.setPasswordInPOS(passwordInPOS);
        pbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        pbo.setPos(pos);
        pbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);
        pbo.loginAsync();
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (pbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_PosLogin && lTimeOut-- > 0) {
            if (pbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && pbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                break;
            }
            Thread.sleep(1000);
        }
        if (pbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("pos登录超时!");
            return false;
        }

        if (pbo.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            //2.staff登录
            Staff staff = new Staff();
            staff.setPhone("15854320895");
            staff.setPasswordInPOS("000000");
            sbo.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            sbo.setStaff(staff);
            sbo.getHttpEvent().setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
            sbo.loginAsync();
            //
            lTimeOut = 30;
            while (sbo.getHttpEvent().getRequestType() != HttpRequestUnit.EnumRequestType.ERT_StaffLogin && lTimeOut-- > 0) {
                if (sbo.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && sbo.getHttpEvent().getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffLogin) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (sbo.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                System.out.println("STAFF登录超时!");
                return false;
            }
        } else {
            System.out.println("pos登录失败！");
        }
        return true;
    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的SmallSheet的临时数据
     *
     * @return
     */
    private List<SmallSheetFrame> retrieveNSmallSheetTempDataInSQLite() {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload, smallSheetFrame);//找到主表List
        return frameList;
    }

    private List<RetailTradeAggregation> retrieveNREtailTradeAggregationTempDataInSQLite() {
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        List<RetailTradeAggregation> retailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
        retailTradeAggregation.setSql("where F_SyncDatetime = ?");
        retailTradeAggregation.setConditions(new String[]{"0"});
        retailTradeAggregationList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation);
        return retailTradeAggregationList;
    }

    /**
     * 上传SQLite中SmallSheet的临时数据
     */
    private void uploadSmallSheetTempData(SmallSheetFrame smallSheetFrame) {
        smallSheetFrame.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteEvent.setTmpMasterTableObj(smallSheetFrame);
            smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done);
            if (!smallSheetHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                Assert.assertTrue("启动时小票格式创建失败!" + smallSheetHttpBO.getHttpEvent().printErrorInfo(), false);
            }
        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
            if (!smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                Assert.assertTrue("启动时小票格式更新失败!", false);
            }
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
            if (!smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                Assert.assertTrue("启动时小票格式删除失败!", false);
            }
        }
    }

    /**
     * 上传SQLite中RetailTradeAggregation的临时数据
     */
    private void uploadRetailTradeAggregationTempData(RetailTradeAggregation retailTradeAggregation) {
        retailTradeAggregation.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(retailTradeAggregation.getSyncType())) {
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
            retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation);
            System.out.println("输出当前上传的临时收银汇总：" + retailTradeAggregation);
            if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
                Assert.assertTrue("创建失败！", false);
            }
        }
    }

    private void syncTime() {
        long timeStamp = new Date().getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue("POS机启动时，同步Ntp失败！", false);
        }
    }

    private void retrieveNBarcodes() {
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        if (!barcodesHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Barcodes失败！", false);
        }
    }

    private void feedbackBarcodes(String ids) {
        if (!barcodesHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Barcodes失败！", false);
        }
    }

    private void retrieveNBrand() {
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Brand失败！", false);
        }
    }

    private void feedbackBrand(String ids) {
        if (!brandHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Brand失败！", false);
        }
    }

    private void retrieveNCommodityCategory() {
        commodityCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步CommodityCategory失败！", false);
        }
    }

    private void feedbackCommodityCategory(String ids) {
        if (!commodityCategoryHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback CommodityCategory失败！", false);
        }
    }

    private void retrieveNCommodity() {
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Commodity失败！", false);
        }
    }

    private void feedbackCommodity(String ids) {
        if (!commodityHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Commodity失败！", false);
        }
    }

    private void retrieveNPromotion() {
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Promotion失败！", false);
        }
    }

    private void feedbackPromotion(String ids) {
        if (!promotionHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Promotion失败！", false);
        }
    }

    private void retrieveNPos() {
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Pos失败！", false);
        }
    }

    private void feedbackPos(String ids) {
        if (!posHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Pos失败！", false);
        }
    }

    private void retrieveNSmallSheet() {
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        ssf.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, ssf)) {
            Assert.assertTrue("POS机启动时，同步SmallSheet失败！", false);
        }
    }

    private void feedbackSmallSheet(String ids) {
        if (!smallSheetHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时， feedback SmallSheet失败！", false);
        }
    }

    private void retrieveNVipCategory() {
        vipCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsync_Done);
        VipCategory vipCategory = new VipCategory();
        vipCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        vipCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if (!vipCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vipCategory)) {
            Assert.assertTrue("POS机启动时，同步VipCategory失败！", false);
        }
    }

    private void feedbackVipCategory(String ids) {
        if (!vipCategoryHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback VipCategory失败！", false);
        }
    }

    private void retrieveNVip() {
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Vip失败！", false);
        }
    }

    private void feedbackVip(String ids) {
        if (!vipHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Vip失败", false);
        }
    }

    private void retrieveNConfigGeneral() {
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步ConfigGeneral失败！", false);
        }
    }

    /**
     * 在feedback之前做，判断是否有需要feedback的数据，若有就feedback，若无，则retrieveN下一个model
     *
     * @param event             当前sqliteEvent
     * @param syncIDs
     * @param feedbackDataCase  当前需要进行feedback的model
     * @param retrieveNDataCase 下一下需要RetrieveN的model
     */
    private void beforeFeedback(BaseSQLiteEvent event, String syncIDs, String feedbackDataCase, String retrieveNDataCase) {
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            List<BaseModel> list = (List<BaseModel>) event.getListMasterTable();
            //
            if (list != null) {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        syncIDs = syncIDs + "," + list.get(i).getID();
                    }
                    syncIDs = syncIDs.substring(1, syncIDs.length());
                    //
                    onFeedback(feedbackDataCase, syncIDs);
                }
            } else {
                onRetrieveN(retrieveNDataCase);
            }
        }
    }

    /**
     * feedback之后，在接收httpEvent之后调用该函数，初始化event的Status和requestType,并且对下一个model进行RetrieveN
     *
     * @param event             当前的HttpEvent
     * @param retrieveNDataCase feedback之后需要进行retrieveN的model
     */
    private void afterFeedback(BaseHttpEvent event, String retrieveNDataCase) {
        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
            event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            event.setRequestType(null);
            //feedback之后需要同步其他的数据
            onRetrieveN(retrieveNDataCase);
        }
    }

    private void onRetrieveN(String retrieveNDataCase) {
        switch (retrieveNDataCase) {
            case BarcodesData:
                retrieveNBarcodes();
                break;
            case BrandData:
                retrieveNBrand();
                break;
            case CommodityCategoryData:
                retrieveNCommodityCategory();
                break;
            case CommodityData:
                retrieveNCommodity();
                break;
            case PromotionData:
                retrieveNPromotion();
                break;
            case PosData:
                retrieveNPos();
                break;
            case SmallSheetData:
                retrieveNSmallSheet();
                break;
            case VipCategoryData:
                retrieveNVipCategory();
                break;
            case VipData:
                retrieveNVip();
                break;
            case ConfigGeneralData:
                retrieveNConfigGeneral();
                break;
            default:
                break;
        }
    }

    private void onFeedback(String feedbackDataCase, String ids) {
        switch (feedbackDataCase) {
            case BarcodesData:
                feedbackBarcodes(ids);
                break;
            case BrandData:
                feedbackBrand(ids);
                break;
            case CommodityCategoryData:
                feedbackCommodityCategory(ids);
                break;
            case CommodityData:
                feedbackCommodity(ids);
                break;
            case PromotionData:
                feedbackPromotion(ids);
                break;
            case PosData:
                feedbackPos(ids);
                break;
            case SmallSheetData:
                feedbackSmallSheet(ids);
                break;
            case VipCategoryData:
                feedbackVipCategory(ids);
                break;
            case VipData:
                feedbackVip(ids);
                break;
            default:
                break;
        }
    }

    private Pos createPos(Pos pos) throws InterruptedException {
        //POS create
        posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!posHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, pos)) {
            Assert.assertTrue("create Pos失败！", false);
        }
        long lTimeOut = 60;
        while (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (posHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("create pos超时！", false);
        }
        Assert.assertTrue("create pos服务器返回的错误码不正确！posHttpEvent.getLastErrorCode() = " + posHttpEvent.getLastErrorCode(), posHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Pos p = (Pos) posHttpEvent.getBaseModel1();
        Assert.assertTrue("服务器返回的对应不应该为null", p != null);
        posHttpEvent.setBaseModel1(null);
        return p;
    }

    private void createCommodity() throws InterruptedException, CloneNotSupportedException {
        Commodity commodity = DataInput.getCommodity();
        if (!commodityHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            System.out.println("Commodity的全部同步失败！");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        Assert.assertTrue("创建商品超时", commodityHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_ToDo);
        Assert.assertTrue("创建商品失败,错误码为:" + commodityHttpEvent.getLastErrorCode(), commodityHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }
}