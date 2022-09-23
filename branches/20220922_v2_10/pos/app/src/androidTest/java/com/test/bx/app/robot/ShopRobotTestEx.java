package com.test.bx.app.robot;


import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BXConfigGeneralHttpBO;
import com.bx.erp.bo.BXConfigGeneralSQLiteBO;
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
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PackageUnitHttpBO;
import com.bx.erp.bo.PackageUnitSQLiteBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosSQLiteBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffSQLiteBO;
import com.bx.erp.bo.VipCategoryHttpBO;
import com.bx.erp.bo.VipCategorySQLiteBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BXConfigGeneralHttpEvent;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.NtpHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BXConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.event.UI.VipCategorySQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipCategoryHttpEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;
//import com.bx.erp.view.activity.MainActivity;
//import com.bx.erp.view.activity.SyncDataActivity;
import com.test.bx.app.robot.client.ClientHandlerEx;
import com.test.bx.app.robot.client.RobotClientEx;
import com.test.bx.app.robot.program.RetailTradeAggregationEx;
import com.test.bx.app.robot.program.RetailTradeEx;
import com.test.bx.app.robot.program.SyncDataEx;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShopRobotTestEx extends BaseHttpAndroidTestCase {
    private static CommodityPresenter commodityPresenter = null;

    private static RetailTradeCommodityPresenter retailTradeCommodityPresenter = null;

    private static BarcodesPresenter barcodesPresenter;

    public static List<Long> idAddLis = new ArrayList<>();

    private static final String BarcodesData = "barcodes";
    private static final String BrandData = "brand";
    private static final String CommodityCategoryData = "category";
    private static final String PackageUnitData = "packageUnit";
    private static final String CommodityData = "commodity";
    private static final String PromotionData = "promotion";
    private static final String PosData = "pos";
    private static final String SmallSheetData = "smallSheet";
    private static final String VipCategoryData = "vipCategory";
    private static final String VipData = "vip";
    private static final String ConfigGeneralData = "configGeneral";


    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    private static SmallSheetHttpEvent smallSheetHttpEvent = null;
    //
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    //
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    //
    private static NtpHttpEvent ntpHttpEvent = null;
    private static NtpHttpBO ntpHttpBO = null;
    //
    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
    private static PackageUnitHttpBO packageUnitHttpBO = null;
    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
    //
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    //
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;
    //
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    //
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    //
    private static PosSQLiteBO posSQLiteBO = null;
    private static PosHttpBO posHttpBO = null;
    private static PosSQLiteEvent posSQLiteEvent = null;
    private static PosHttpEvent posHttpEvent = null;
    //
    private static VipCategorySQLiteBO vipCategorySQLiteBO = null;
    private static VipCategoryHttpBO vipCategoryHttpBO = null;
    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = null;
    private static VipCategoryHttpEvent vipCategoryHttpEvent = null;
    //
    private static VipSQLiteBO vipSQLiteBO = null;
    private static VipHttpBO vipHttpBO = null;
    private static VipSQLiteEvent vipSQLiteEvent = null;
    private static VipHttpEvent viHttpEvent = null;
    //
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
    //
    private static BXConfigGeneralSQLiteBO bxConfigGeneralSQLiteBO = null;
    private static BXConfigGeneralHttpBO bxConfigGeneralHttpBO = null;
    private static BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent = null;
    private static BXConfigGeneralHttpEvent bxConfigGeneralHttpEvent = null;
    //
//    private ConfigCacheSizeHttpBO configCacheSizeHttpBO;
//    private ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent;
    //
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionHttpEvent promotionHttpEvent = null;
    //
    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    //
    private static StaffSQLiteBO staffSQLiteBO = null;
    private static StaffHttpBO staffHttpBO = null;
    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffHttpEvent staffHttpEvent = null;
    public static final int Event_ID_ShopRobotTestEx = 100000;

    /* 开机同步是异步操作，等待开机同步完才能往下执行*/
    public static boolean bfinishedSyncData = false;
    /**
     * 上班的天数
     */
    public volatile static int dayNumber = 1;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (commodityPresenter == null) {
            commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        }
        if (retailTradeCommodityPresenter == null) {
            retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        }
        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        
        //
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        //
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradePromotingHttpBO == null) {
            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        if (retailTradePromotingSQLiteBO == null) {
            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        //
        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
            ntpHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        }
        ntpHttpEvent.setHttpBO(ntpHttpBO);
        //
        if (packageUnitSQLiteEvent == null) {
            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
            packageUnitSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (packageUnitHttpEvent == null) {
            packageUnitHttpEvent = new PackageUnitHttpEvent();
            packageUnitHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (packageUnitSQLiteBO == null) {
            packageUnitSQLiteBO = new PackageUnitSQLiteBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
        }
        if (packageUnitHttpBO == null) {
            packageUnitHttpBO = new PackageUnitHttpBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
        }
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
        //
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        //
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        //
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (commodityCategorySQLiteBO == null) {
            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        if (commodityCategoryHttpBO == null) {
            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
        }
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        //
        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (posSQLiteBO == null) {
            posSQLiteBO = new PosSQLiteBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (posHttpBO == null) {
            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        //
        if (vipCategorySQLiteEvent == null) {
            vipCategorySQLiteEvent = new VipCategorySQLiteEvent();
            vipCategorySQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (vipCategoryHttpEvent == null) {
            vipCategoryHttpEvent = new VipCategoryHttpEvent();
            vipCategoryHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (vipCategorySQLiteBO == null) {
            vipCategorySQLiteBO = new VipCategorySQLiteBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        if (vipCategoryHttpBO == null) {
            vipCategoryHttpBO = new VipCategoryHttpBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);
        //
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (viHttpEvent == null) {
            viHttpEvent = new VipHttpEvent();
            viHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, viHttpEvent);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, viHttpEvent);
        }
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        viHttpEvent.setSqliteBO(vipSQLiteBO);
        viHttpEvent.setHttpBO(vipHttpBO);
        //
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (configGeneralSQLiteBO == null) {
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
        //
        if (bxConfigGeneralSQLiteEvent == null) {
            bxConfigGeneralSQLiteEvent = new BXConfigGeneralSQLiteEvent();
            bxConfigGeneralSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (bxConfigGeneralHttpEvent == null) {
            bxConfigGeneralHttpEvent = new BXConfigGeneralHttpEvent();
            bxConfigGeneralHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (bxConfigGeneralSQLiteBO == null) {
            bxConfigGeneralSQLiteBO = new BXConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
        }
        if (bxConfigGeneralHttpBO == null) {
            bxConfigGeneralHttpBO = new BXConfigGeneralHttpBO(GlobalController.getInstance().getContext(), bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
        }
        bxConfigGeneralSQLiteEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
        bxConfigGeneralSQLiteEvent.setHttpBO(bxConfigGeneralHttpBO);
        bxConfigGeneralHttpEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
        bxConfigGeneralHttpEvent.setHttpBO(bxConfigGeneralHttpBO);
        //
//        configCacheSizeHttpBO = GlobalController.getInstance().getConfigCacheSizeHttpBO();
//        configCacheSizeSQLiteEvent = GlobalController.getInstance().getConfigCacheSizeSQLiteEvent();
        //
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        //
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);

        retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
        //
        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(Event_ID_ShopRobotTestEx);
        }
        if (staffSQLiteBO == null) {
            staffSQLiteBO = new StaffSQLiteBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    //TODO 模拟多天的上班下班。使用saledatetime做假的天数据
    @Test(timeout = 20000)
    public void testShopRobotEx() throws Exception {

        RobotClientEx robotClientEx = new RobotClientEx();
        robotClientEx.startClient();

        while (true) {
            if (robotClientEx.getClientHandlerEx().isConnectedToServer()) {
                System.out.println("pos机器人连接nbr机器人成功");
                RobotEx robotEx = new RobotEx();
                StringBuilder stringBuilder = new StringBuilder();
                robotEx.setErrorInfo(stringBuilder);
                if (!robotEx.run(robotClientEx)) {
                    System.out.println(robotEx.getErrorInfo());
                    Assert.assertFalse("机器人运行出错！错误信息：" + robotEx.getErrorInfo(), true);
                }
                break;
            }
            Thread.sleep(500);
        }
        robotClientEx.closeSession();
        System.out.println("pos机器人断开连接");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    public static List<Commodity> getCommodityList(int maxSalesNO, boolean forPurchasingOnly, StringBuilder sbError) {
        Map<String, Commodity> map = new HashMap<>();  //key=F_ID, value=Commodity
        Commodity commodity = new Commodity();
        assert maxSalesNO > 0;
        Random random = new Random();
        int commodityMaxSalesNO = random.nextInt(maxSalesNO);
        if (commodityMaxSalesNO <= 0) {
            commodityMaxSalesNO = 1;
        }
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        int maxCommodityIdInSqlite = commodityList.get(commodityList.size() - 1).getID().intValue();
        for (int i = 0; i < commodityMaxSalesNO; i++) {
            //...在本地SQLite中读取商品。R1，传递商品ID。
            // 查询所有的商品
//            commodity.setID(Long.valueOf(random.nextInt(100) + 1));
            commodity.setID(Long.valueOf(random.nextInt(maxCommodityIdInSqlite) + 1));
            Commodity commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
            if (commodity1 == null || commodity1.getStatus() == Commodity.EnumStatusCommodity.ESC_Deleted.getIndex()) { //防止拿到已经删除的商品或SQLite错误
                i -= 1;
                continue;
            }
            if (forPurchasingOnly) { //采购商品只能是单品
                while (true) {//直到找到单品为止
                    if (commodity1 != null && (commodity1.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex())) {
                        map.put(String.valueOf(commodity1.getID()), commodity1);
                        break;
                    } else {
                        commodity.setID(Long.valueOf(random.nextInt(100) + 1));
                        commodity1 = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                    }
                }
            } else if (commodity1 != null) {
                map.put(String.valueOf(commodity1.getID()), commodity1);//这里的商品可能是单品、多包装商品、组合商品、服务型商品
            }
            //... 如果只获取一个商品，然后商品ID不存在的情况下该如何处理
        }
        return new ArrayList<>(map.values());
    }


    /**
     * 购买商品随机
     *
     * @param listComm 购买的商品的列表
     * @param maxNO    购买一个商品的最大数量
     */
    public static List<RetailTradeCommodity> getRetailTradeCommodityList(List<Commodity> listComm, int maxNO, RetailTrade retailTrade, StringBuilder sbError) throws ParseException {
        List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<RetailTradeCommodity>();
        // 随机商品
        //...
        Random r = new Random();
        int totalPrices = 0; //记录这批零售单商品的总价格
        long maxTextIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            sbError.append("零售单商品表查找本地最大ID失败！");

            return null;
        }
        for (int i = 0; i < listComm.size(); i++) {
            RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
            int commodityNO = r.nextInt(maxNO);
            if (commodityNO <= 0) {
                commodityNO = 1;
            }
            retailTradeCommodity.setID(maxTextIDInSQLite + i + 1);
            retailTradeCommodity.setPriceOriginal((double) listComm.get(i).getPriceRetail());
            retailTradeCommodity.setNOCanReturn(commodityNO);
            retailTradeCommodity.setCommodityID(listComm.get(i).getID().intValue());
            retailTradeCommodity.setNO(commodityNO);
            retailTradeCommodity.setDiscount(1);
            retailTradeCommodity.setPriceSpecialOffer(0);
            retailTradeCommodity.setPriceVIPOriginal(0);

//            retailTradeCommodity.setBarcodeID(1);
            retailTradeCommodity.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            retailTradeCommodity.setTradeID(retailTrade.getID());
            retailTradeCommodity.setPriceReturn(1); //...
            // 根据商品ID查询barcodesID
            Barcodes barcodes = new Barcodes();
            barcodes.setSql("where F_CommodityID = ?");
            barcodes.setConditions(new String[]{String.valueOf(listComm.get(i).getID())});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
            retailTradeCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());
            //
            totalPrices += commodityNO * listComm.get(i).getPriceRetail();
            retailTradeCommodityList.add(retailTradeCommodity);
        }
        //
        retailTradeCommodityList.get(0).setAmountWeChat(totalPrices);//把这批零售单商品的总价格返回出去
        return retailTradeCommodityList;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if(event.getId() == RetailTradeEx.Event_ID_RetailTradeEx) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if(event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        }
        else if(event.getId() == RetailTradeEx.Event_ID_RetailTradeEx) {
            event.onEvent();
        }
        else if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                caseLog("上传零售单时出现网络故障，下次有网开机登录时再同步");
            }
        }  else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if(event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        }
        else if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
                caseLog("上传收银汇总时出现网络故障，下次有网开机登录时再同步");
            }
        }  else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        // 登录后创建的
        if (event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseActivity.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseActivity.retailTradeAggregation);
                } else {
                    ShopRobotTest.caseLog("创建临时收银汇总失败！");
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    System.out.println("更新收银汇总成功！收银汇总=" + event.getBaseModel1());
                } else {
                    ShopRobotTest.caseLog("更新收银汇总失败！");
                }
            }
        }
        // 开机同步的
        else if(event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                SyncDataEx.tempRetailTradeAggregationList.remove(0);
                //
                if (SyncDataEx.tempRetailTradeAggregationList.size() > 0) {
                    //继续上传收银汇总
                    uploadRetailTradeAggregationTempData(SyncDataEx.tempRetailTradeAggregationList.get(0));
                } else {
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);//这一句是没有用的，只是为了让阅读者知道上传收银汇总已经完成了最后一传
                    syncTime();
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
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

    /**
     * 同步APP的时间和服务器的时间
     */
    private void syncTime() {
        long timeStamp = new Date().getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            caseLog("POS机启动时，同步Ntp失败！");
        }
    }

    private void retrieveNCPackageUnit(PackageUnit packageUnit) {
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            caseLog(" 重置CommodityCategory失败！");
        }
    }

    private void retrieveNBarcodes() {
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        if (!barcodesHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Barcodes失败！");
        }
    }

    private void feedbackBarcodes(String ids) {
        if (!barcodesHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Barcodes失败！");
        }
    }

    private void retrieveNBrand() {
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Brand失败！");
        }
    }

    private void feedbackBrand(String ids) {
        if (!brandHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Brand失败！");
        }
    }

    private void retrieveNCommodityCategory() {
        commodityCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步CommodityCategory失败！");
        }
    }

    private void feedbackCommodityCategory(String ids) {
        if (!commodityCategoryHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback CommodityCategory失败！");
        }
    }

    private void retrieveNCommodity() {
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Commodity失败！");
        }
    }

    private void feedbackCommodity(String ids) {
        if (!commodityHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Commodity失败！");
        }
    }

    private void retrieveNPromotion() {
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Promotion失败！");
        }
    }

    private void feedbackPromotion(String ids) {
        if (!promotionHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Promotion失败！");
        }
    }

    private void retrieveNPos() {
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Pos失败！");
        }
    }

    private void feedbackPos(String ids) {
        if (!posHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Pos失败！");
        }
    }

    private void retrieveNSmallSheet() {
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步SmallSheet失败！");
        }
    }

    private void feedbackSmallSheet(String ids) {
        if (!smallSheetHttpBO.feedback(ids)) {
            caseLog("POS机启动时， feedback SmallSheet失败！");
        }
    }

    private void retrieveNVipCategory() {
        vipCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsync_Done);
        if (!vipCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步VipCategory失败！");
        }
    }

    private void feedbackVipCategory(String ids) {
        if (!vipCategoryHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback VipCategory失败！");
        }
    }

    private void retrieveNVip() {
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步Vip失败！");
        }
    }

    private void feedbackVip(String ids) {
        if (!vipHttpBO.feedback(ids)) {
            caseLog("POS机启动时，feedback Vip失败");
        }
    }

    private void retrieveNConfigGeneral() {
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步ConfigGeneral失败！");
        }
    }

    //    private void retrieveNConfigCacheSize() {
//        configCacheSizeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigCacheSize_RefreshByServerDataAsync_Done);
//        if (!configCacheSizeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
//            Toast.makeText(appApplication, "POS机启动时，同步ConfigCacheSize失败！", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void retrieveNBXConfigGeneral() {
        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
        if (!bxConfigGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            caseLog("POS机启动时，同步BXConfigGeneral失败！");
        }
    }

    private void retrieveResigned() {
        if (!staffHttpBO.retrieveResigned(null)) {
            caseLog("POS机启动时，同步删除已离职员工失败！");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                    event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    event.setRequestType(null);
                    Ntp ntp = (Ntp) event.getBaseModel1();
                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
                    //
                    PackageUnit packageUnit = new PackageUnit();
                    packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                    packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                    retrieveNCPackageUnit(packageUnit);
                }
            } else {
                NtpHttpBO.TimeDifference = 0;
                caseLog("同步NTP时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                caseLog("同步包装单位时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if(event.getId() == RobotEx.Event_ID_RobotEx) {
            event.onEvent();
        }
        else if(event.getId() == RetailTradeEx.Event_ID_RetailTradeEx) {
            event.onEvent();
        }
        else if (event.getId() == Event_ID_ShopRobotTestEx) {
            caseLog("#########################################################SyncDataActivity onRetailTradeSQLiteEvent");
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                event.setEventTypeSQLite(null);
                SyncDataEx.retailTradeList = (List<RetailTrade>) event.getListMasterTable();
                //查找所有临时RetailTradePromoting
                retrieveNTempRetailTradePromotingInSQLite();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    /**
     * 在本地查找临时RetailTradePromoting，根据SyncDatetime查找不是根据TradeID查找
     */
    public List<?> retrieveNTempRetailTradePromotingInSQLite() {
        caseLog("开始查找临时RetailTradepromoting");
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync);
        if (!retailTradePromotingSQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_RetailTradePtomoting_RetrieveNToUpload, null)) {
            caseLog("在本地查找临时RetailTradePromoting失败！");
        }
        return retailTradePromotingSQLiteEvent.getListMasterTable();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            caseLog("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        SyncDataActivity  onRetailTradePromotingSQLiteEvent");
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RetrieveNAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                SyncDataEx.tempRetailTradePromotingList = (List<BaseModel>) event.getListMasterTable();
                if (SyncDataEx.retailTradeList.size() > 0) {
                    if (SyncDataEx.tempRetailTradePromotingList.size() > 0) {
                        //将上传的RetailTrade对应的RetailTradePromoting的TradeID重新设置为真正的RetailTradeID
                        for (int i = 0; i < SyncDataEx.retailTradeList.size(); i++) {
                            for (int j = 0; j < SyncDataEx.tempRetailTradePromotingList.size(); j++) {
                                // POS_SN存放了零售单的临时ID
                                if (SyncDataEx.retailTradeList.get(i).getLocalSN() == ((RetailTradePromoting) SyncDataEx.tempRetailTradePromotingList.get(j)).getTradeID()) {
                                    ((RetailTradePromoting) SyncDataEx.tempRetailTradePromotingList.get(j)).setTradeID(SyncDataEx.retailTradeList.get(i).getID().intValue());
                                }
                            }
                        }
                        //得到正确的TradeID后的RetailTradePromoting，需要现在本地进行Update，然后再上传到服务器，否则，RetailTradePromoting上传失败之后，由于没有Update到数据库，会不知道真正的TradeID是多少，而且主外键关联
                        updateNRetailTradePromoting(SyncDataEx.tempRetailTradePromotingList);
                    } else {
                        if (SyncDataEx.tempRetailTradeAggregationList.size() > 0) {
                            uploadRetailTradeAggregationTempData(SyncDataEx.tempRetailTradeAggregationList.get(0));
                        } else {
                            syncTime();
                        }
                    }
                } else {
                    //可能上一次的RetailTradePromoting上传失败，应该根据SyncDatetime查找本地临时的RetailTradePromoting然后进行上传
                    if (SyncDataEx.tempRetailTradePromotingList.size() > 0) {
                        uploadTempRetailTradePromotingList(SyncDataEx.tempRetailTradePromotingList);
                    } else {
                        if (SyncDataEx.tempRetailTradeAggregationList.size() > 0) {
                            uploadRetailTradeAggregationTempData(SyncDataEx.tempRetailTradeAggregationList.get(0));
                        } else {
                            syncTime();
                        }
                    }
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                uploadTempRetailTradePromotingList(SyncDataEx.tempRetailTradePromotingList);
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerNAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                if (SyncDataEx.tempRetailTradeAggregationList.size() > 0) {
                    uploadRetailTradeAggregationTempData(SyncDataEx.tempRetailTradeAggregationList.get(0));
                } else {
                    syncTime();
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    /**
     * 当修改了RetailTradePromoting的TradeID之后，
     *
     * @param bmList
     */
    public void updateNRetailTradePromoting(List<?> bmList) {
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_UpdateNAsync);
        if (!retailTradePromotingSQLiteBO.updateNAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList)) {
            caseLog("update本地RetailTradePromoting失败！");
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
            if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
                caseLog("启动时上传临时收银汇总失败！");
            }
        }
    }

    /**
     * 上传临时RetailTradePromoting
     *
     * @param bmList
     */
    public void uploadTempRetailTradePromotingList(List<BaseModel> bmList) {
        //批量上传RetailTradePromoting到服务器
        retailTradePromotingSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!retailTradePromotingHttpBO.createNAsync(BaseHttpBO.INVALID_CASE_ID, bmList)) {
            caseLog("批量上传RetailTradePromoting失败！");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //重置Commodity
                retrieveNBarcodes();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, BrandData);
            } else {
                caseLog("同步条形码时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncBarcodesIDs, BarcodesData, BrandData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, CommodityCategoryData);
            } else {
                caseLog("同步品牌时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncBrandIDs, BrandData, CommodityCategoryData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, CommodityData);
            } else {
                caseLog("同步商品类别时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncCommodityCategoryIDs, CommodityCategoryData, CommodityData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, PromotionData);
            } else {
                caseLog("同步商品时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncCommodityIDs, CommodityData, PromotionData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, PosData);
            } else {
                caseLog("同步促销时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncPromotionIDs, PromotionData, PosData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, SmallSheetData);
            } else {
                caseLog("同步POS信息时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncPosIDs, PosData, SmallSheetData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) { // 如果上传失败，就跳过该小票格式
                    SyncDataEx.tempSmallSheetFrameList.remove(0);
                    if (SyncDataEx.tempSmallSheetFrameList.size() == 0) {
                        //上传RetailTrade
                        if (SyncDataEx.tempRetailTradeList != null) {
                            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                caseLog("查询临时零售单失败！");
                            }
                        }
                    } else {
                        //上传tempSmallSheetFrameList的第一个元素
                        uploadSmallSheetTempData(SyncDataEx.tempSmallSheetFrameList.get(0));
                    }
                }
//                afterFeedback(event, VipCategoryData);   onSmallSheetSqlite中会处理
            } else {
                caseLog("同步小票格式时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
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
                caseLog("启动时上传C型小票格式失败!");
            }
        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
            if (!smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                caseLog("启动时上传C型小票格式失败!");
            }
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
            if (!smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                caseLog("启动时上传C型小票格式失败!");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            //开机同步SmallSheet上传部分
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done) {
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    event.setEventTypeSQLite(null);
                    SyncDataEx.tempSmallSheetFrameList.remove(0);
                    if (SyncDataEx.tempSmallSheetFrameList.size() == 0) {
                        //上传RetailTrade
                        if (SyncDataEx.tempRetailTradeList != null) {
                            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                caseLog("查询临时零售单失败！");
                            }
                        }
                    } else {
                        //上传tempSmallSheetFrameList的第一个元素
                        uploadSmallSheetTempData(SyncDataEx.tempSmallSheetFrameList.get(0));
                    }
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {//开机同步SmallSheet RetrieveN部分
                beforeFeedback(event, SyncDataEx.syncSmallSheetIDs, SmallSheetData, VipCategoryData);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategoryHttpEvent(VipCategoryHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, VipData);
            } else {
                caseLog("同步会员类别时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncVipCategoryIDs, VipCategoryData, VipData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, ConfigGeneralData);
            } else {
                caseLog("同步会员时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            beforeFeedback(event, SyncDataEx.syncVipIDs, VipData, ConfigGeneralData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                caseLog("同步配置信息时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retrieveNBXConfigGeneral();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBXConfigGeneralHttpEvent(BXConfigGeneralHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                caseLog("同步公共配置信息时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBXConfigGeneralSQLiteEvent(BXConfigGeneralSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步删除已离职员工
                retrieveResigned();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                caseLog("同步员工时出现网络故障，下次有网开机登录时再同步");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == Event_ID_ShopRobotTestEx) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                //跳转到MainActivity
//                Intent intent = new Intent(SyncDataActivity.this, MainActivity.class);
//                SyncDataActivity.this.finish();
//                BaseActivity.lastRetailTradeAmount = 0.000000d;
//                BaseActivity.lastRetailTradeChangeMoney = 0.000000d;
//                BaseActivity.lastRetailTradePaymentType = "";
//                startActivity(intent);
                caseLog("同步完成");
                bfinishedSyncData = true;
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            caseLog(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
            Assert.assertFalse("未处理的eventID：" + event.getId() , true);
        }
    }
}
