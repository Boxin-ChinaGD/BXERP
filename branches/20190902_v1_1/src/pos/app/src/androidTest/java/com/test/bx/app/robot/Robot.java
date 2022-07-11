package com.test.bx.app.robot;

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
import com.bx.erp.bo.PackageUnitHttpBO;
import com.bx.erp.bo.PackageUnitSQLiteBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PosSQLiteBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.PurchasingOrderHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.ReturnCommoditySheetHttpBO;
import com.bx.erp.bo.ReturnCommoditySheetSQLiteBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.VipCategoryHttpBO;
import com.bx.erp.bo.VipCategorySQLiteBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.bo.WarehousingHttpBO;
import com.bx.erp.bo.WarehousingSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
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
import com.bx.erp.event.PurchasingOrderHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
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
import com.bx.erp.event.UI.VipCategorySQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipCategoryHttpEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.event.WarehousingHttpEvent;
import com.bx.erp.event.WarehousingSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Brand;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Ntp;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.PoiUtils;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.client.ClientHandler;
import com.test.bx.app.robot.program.PosLogin;
import com.test.bx.app.robot.program.Program;
import com.test.bx.app.robot.program.QueryRetailTrade;
import com.test.bx.app.robot.program.RetailTrade;
import com.test.bx.app.robot.program.RetailTradeAggregation;
import com.test.bx.app.robot.program.SyncData;
import com.test.bx.app.robot.protocol.Header;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class Robot {
    /**
     * 专门给机器人开机同步使用的EVENT ID
     */
    public final static int ROBOT_EVENT_ID_STARTUP_SYNC = 10000;//这个ID不能为0，因为int默认为0
    public final static int ROBOT_EVENT_ID_NTP = ROBOT_EVENT_ID_STARTUP_SYNC + 1;

    public final static int RobotActivity1 = 1;

    private static NtpHttpBO ntpHttpBO = null;
    private static NtpHttpEvent ntpHttpEvent = null;
    //
    private static BrandSQLiteBO brandSQLiteBO = null;
    private static BrandHttpBO brandHttpBO = null;
    private static BrandSQLiteEvent brandSQLiteEvent = null;
    private static BrandHttpEvent brandHttpEvent = null;

    public final int SHIFT_START_HOUR = 8;
    public final int SHIFT_END_HOUR = 20;
    protected boolean bRunInRandomMode;

    protected Program[] programs;
    protected Date startDatetime;
    protected Date endDatetime;
    //    protected RetailTradeSQLiteBO retailTradeSQLiteBO;
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static ReturnCommoditySheetSQLiteEvent returnCommoditySheetSQLiteEvent = null;
    private static ReturnCommoditySheetSQLiteBO returnCommoditySheetSQLiteBO = null;
    private static ReturnCommoditySheetHttpBO returnCommoditySheetHttpBO = null;
    private static ReturnCommoditySheetHttpEvent returnCommoditySheetHttpEvent = null;
    private static WarehousingSQLiteEvent warehousingSQLiteEvent = null;
    private static WarehousingSQLiteBO warehousingSQLiteBO = null;
    private static WarehousingHttpEvent warehousingHttpEvent = null;
    private static WarehousingHttpBO warehousingHttpBO = null;
    private static PurchasingOrderHttpEvent purchasingOrderHttpEvent = null;
    private static PurchasingOrderHttpBO purchasingOrderHttpBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static VipSQLiteEvent vipSQLiteEvent = null;
    private static VipHttpEvent vipHttpEvent = null;
    private static VipSQLiteBO vipSQLiteBO = null;
    private static VipHttpBO vipHttpBO = null;
    private static BarcodesHttpEvent barcodesHttpEvent = null;
    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
    private static BarcodesHttpBO barcodesHttpBO = null;
    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
    //
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    private static SmallSheetHttpEvent smallSheetHttpEvent = null;
    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    //
    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
    //
    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
    private static PackageUnitHttpBO packageUnitHttpBO = null;
    //
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
    //
    private static PosSQLiteBO posSQLiteBO = null;
    private static PosHttpBO posHttpBO = null;
    private static PosSQLiteEvent posSQLiteEvent = null;
    private static PosHttpEvent posHttpEvent = null;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
    private static VipCategorySQLiteBO vipCategorySQLiteBO = null;
    private static VipCategoryHttpBO vipCategoryHttpBO = null;
    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = null;
    private static VipCategoryHttpEvent vipCategoryHttpEvent = null;
    //
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    //
    private static WXPayHttpEvent wxPayHttpEvent = null;
    private static WXPayHttpBO wxPayHttpBO = null;
    //
    private static SmallSheetFramePresenter smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
    private static SmallSheetTextPresenter smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
    //
    private static LogoutHttpBO logoutHttpBO = null;
    private static LogoutHttpEvent logoutHttpEvent = null;
    //
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionHttpEvent promotionHttpEvent = null;

    public static final int Event_ID_Robot = 100000;

    public static boolean isDone = false;

    public String getErrorInfo() {
        return errorInfo.toString();
    }

    protected StringBuilder errorInfo;

    protected  PoiUtils poiXls;

    public Robot() {
        initBOAndEvent();
    }

    private void initBOAndEvent() {
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
//            retailTradeHttpEvent.setId(Event_ID_Robot);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//            retailTradeSQLiteEvent.setId(Event_ID_Robot);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);

        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(Event_ID_Robot);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_Robot);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        //
        if (returnCommoditySheetHttpEvent == null) {
            returnCommoditySheetHttpEvent = new ReturnCommoditySheetHttpEvent();
            returnCommoditySheetHttpEvent.setId(Event_ID_Robot);
        }
        if (returnCommoditySheetSQLiteEvent == null) {
            returnCommoditySheetSQLiteEvent = new ReturnCommoditySheetSQLiteEvent();
            returnCommoditySheetSQLiteEvent.setId(Event_ID_Robot);
        }
        if (returnCommoditySheetHttpBO == null) {
            returnCommoditySheetHttpBO = new ReturnCommoditySheetHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (returnCommoditySheetSQLiteBO == null) {
            returnCommoditySheetSQLiteBO = new ReturnCommoditySheetSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        returnCommoditySheetHttpEvent.setSqliteBO(returnCommoditySheetSQLiteBO);
        returnCommoditySheetHttpEvent.setHttpBO(returnCommoditySheetHttpBO);
        returnCommoditySheetSQLiteEvent.setHttpBO(returnCommoditySheetHttpBO);
        returnCommoditySheetSQLiteEvent.setSqliteBO(returnCommoditySheetSQLiteBO);
        //
        if (warehousingHttpEvent == null) {
            warehousingHttpEvent = new WarehousingHttpEvent();
            warehousingHttpEvent.setId(Event_ID_Robot);
        }
        if (warehousingSQLiteEvent == null) {
            warehousingSQLiteEvent = new WarehousingSQLiteEvent();
            warehousingSQLiteEvent.setId(Event_ID_Robot);
        }
        if (warehousingHttpBO == null) {
            warehousingHttpBO = new WarehousingHttpBO(GlobalController.getInstance().getContext(), warehousingSQLiteEvent, warehousingHttpEvent);
        }
        if (warehousingSQLiteBO == null) {
            warehousingSQLiteBO = new WarehousingSQLiteBO(GlobalController.getInstance().getContext(), warehousingSQLiteEvent, warehousingHttpEvent);
        }
        warehousingHttpEvent.setHttpBO(warehousingHttpBO);
        warehousingHttpEvent.setSqliteBO(warehousingSQLiteBO);
        warehousingSQLiteEvent.setHttpBO(warehousingHttpBO);
        warehousingSQLiteEvent.setSqliteBO(warehousingSQLiteBO);
        //
        if (purchasingOrderHttpEvent == null) {
            purchasingOrderHttpEvent = new PurchasingOrderHttpEvent();
            purchasingOrderHttpEvent.setId(Event_ID_Robot);
        }
        if (purchasingOrderHttpBO == null) {
            purchasingOrderHttpBO = new PurchasingOrderHttpBO(GlobalController.getInstance().getContext(), null, purchasingOrderHttpEvent);
        }
        purchasingOrderHttpBO.setHttpEvent(purchasingOrderHttpEvent);
        //
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(Event_ID_Robot);
        }
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        //
        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(Event_ID_Robot);
        }
        if (vipSQLiteEvent == null) {
            vipSQLiteEvent = new VipSQLiteEvent();
            vipSQLiteEvent.setId(Event_ID_Robot);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        if (vipSQLiteBO == null) {
            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, vipHttpEvent);
        }
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        vipHttpEvent.setHttpBO(vipHttpBO);
        vipHttpEvent.setSqliteBO(vipSQLiteBO);
        //
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        //
        commoditySQLiteEvent.setTimeout(Shared.UNIT_TEST_TimeOut);

        if (ntpHttpEvent == null) {
            ntpHttpEvent = new NtpHttpEvent();
        }
        if (ntpHttpBO == null) {
            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
        }
        ntpHttpBO.setHttpEvent(ntpHttpEvent);
        //
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(Event_ID_Robot);
        }
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(Event_ID_Robot);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        //
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(Event_ID_Robot);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(Event_ID_Robot);
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
        if (packageUnitHttpEvent == null) {
            packageUnitHttpEvent = new PackageUnitHttpEvent();
            packageUnitHttpEvent.setId(Event_ID_Robot);
        }
        if (packageUnitSQLiteEvent == null) {
            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
            packageUnitSQLiteEvent.setId(Event_ID_Robot);
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
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(Event_ID_Robot);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(Event_ID_Robot);
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
        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(Event_ID_Robot);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(Event_ID_Robot);
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
            vipCategorySQLiteEvent.setId(Event_ID_Robot);
        }
        if (vipCategoryHttpEvent == null) {
            vipCategoryHttpEvent = new VipCategoryHttpEvent();
            vipCategoryHttpEvent.setId(Event_ID_Robot);
        }
        if (vipCategorySQLiteBO == null) {
            vipCategorySQLiteBO = new VipCategorySQLiteBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        if (vipCategoryHttpBO == null) {
            vipCategoryHttpBO = new VipCategoryHttpBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
        }
        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);
        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
        //
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_Robot);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(Event_ID_Robot);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(Event_ID_Robot);
        }
        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
        }
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_Robot);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(Event_ID_Robot);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(Event_ID_Robot);
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
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_Robot);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_Robot);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
    }

    public Robot(Date startDatetime, Date endDatetime, final RobotConfig rc, boolean bRunInRandomMode) {
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.bRunInRandomMode = bRunInRandomMode;
        setPrograms(startDatetime, endDatetime, rc, bRunInRandomMode);
    }

    public void setPrograms(Date startDatetime, Date endDatetime, final RobotConfig rc, boolean bRunInRandomMode) {
        List<String> programUnit = new ArrayList<String>();
        programs = new Program[Program.EnumProgramType.values().length];
        //
        programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()] = new RetailTrade(startDatetime, endDatetime, rc, retailTradeHttpEvent, retailTradeSQLiteBO, wxPayHttpBO, retailTradeSQLiteEvent, bRunInRandomMode);
        ((RetailTrade) programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()]).setMaxTradePerDay(200);//...
        programs[Program.EnumProgramType.EPT_QueryRetailTrade.getIndex()] = new QueryRetailTrade(startDatetime, endDatetime, rc, retailTradeHttpEvent, retailTradeSQLiteBO, retailTradeHttpBO, retailTradeSQLiteEvent, bRunInRandomMode);
        //以下活动并不需要在一天中重复做
        programs[Program.EnumProgramType.EPT_PosLogin.getIndex()] = new PosLogin(startDatetime, endDatetime, rc, posLoginHttpBO, staffLoginHttpBO, logoutHttpBO, bRunInRandomMode);
        programs[Program.EnumProgramType.EPT_SyncData.getIndex()] = new SyncData(startDatetime, endDatetime, rc, retailTradeSQLiteBO, retailTradeSQLiteEvent, smallSheetSQLiteEvent, retailTradeAggregationSQLiteEvent, bRunInRandomMode);
        programs[Program.EnumProgramType.EPT_RetailTradeAggregation.getIndex()] = new RetailTradeAggregation(startDatetime, endDatetime, rc, retailTradeAggregationSQLiteBO, retailTradeSQLiteEvent, bRunInRandomMode);
//        if (!bRunInRandomMode && runRobotMode != Program.ReadExcelMode) {
//            programs[Program.EnumProgramType.EPT_PosLogin.getIndex()].loadProgramUnit();
//        }
//        if (!bRunInRandomMode && runRobotMode != Program.ReadExcelMode) {
//            programs[Program.EnumProgramType.EPT_SyncData.getIndex()].loadProgramUnit();
//        }
//        if (!bRunInRandomMode && runRobotMode != Program.ReadExcelMode) {
//            programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()].loadProgramUnit();
//        }
        // 读取Excel模式的
        if (!bRunInRandomMode) {
            try {
                poiXls = new PoiUtils(Program.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 读取Excel模式的
            int sheetNO = poiXls.readExcelSheetNO(Program.file);
            // 把Excel表的所有数据读到内存
            for (int i = 0; i < sheetNO; i++) {
                String sheetName = poiXls.readExcelSheetName(Program.file, i);
                System.out.println(sheetName);
                Map<String, List<String>> mapBaseModel = readExcelSheet(i);
                Program.mapBaseModels.put(sheetName, mapBaseModel);
            }
            //
            programs[Program.EnumProgramType.EPT_PosLogin.getIndex()].loadProgramUnit();
            programs[Program.EnumProgramType.EPT_SyncData.getIndex()].loadProgramUnit();
            programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()].loadProgramUnit();
        }
    }

    private Date getStartOfToday(Date currentDatetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDatetime);
        cal.set(Calendar.HOUR_OF_DAY, SHIFT_START_HOUR);

        return cal.getTime();
    }

    private Date getEndOfToday(Date currentDatetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDatetime);
        cal.set(Calendar.HOUR_OF_DAY, SHIFT_END_HOUR);

        return cal.getTime();
    }

    public boolean run() throws Exception {
        // 售卖指定商品，根据时间点执行指定活动
        if (!bRunInRandomMode) {
            // 告诉nbr，pos启动成功，并把pos名字发给nbr
            Header header = new Header();
            header.setCommand(Header.EnumCommandType.ECT_DoneStartProgram.getIndex());
            header.setBodyLength(0);
            header.setPosName(Config.MyPosName);
            StringBuilder sb = new StringBuilder();
            sb.append(header.toBufferString());
            ShopRobotTest.clientSession.write(sb.toString());

//            // 店员登录
//            programs[Program.EnumProgramType.EPT_PosLogin.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode);
//            // 机器人创建商品前，Pos可能没同步完Nbr的商品，所以先确保同步完所有商品
//            if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//                Assert.assertTrue("RN请求需要同步的Commodity失败！", false);
//            }
//            // 登录后session不为null，才能查询SQLite数据库
//            new RobotConfig().load();
            //
            errorInfo = new StringBuilder();
            while (!ClientHandler.timeToCloseConnected) {
                // 店员登录
                if (!programs[Program.EnumProgramType.EPT_PosLogin.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode)) {
                    return false;
                }
                if (!programs[Program.EnumProgramType.EPT_SyncData.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode)) {
                    //...
                    return false;
                }
                if (!programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode)) {//活动1（收银）
                    //...
                    return false;
                }
                if (isDone) {
                    break;
                }
            }
        } else {
            Date currentDatetime = startDatetime;
            errorInfo = new StringBuilder();
            // 店员登录
            programs[Program.EnumProgramType.EPT_PosLogin.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode);
            while (currentDatetime.getTime() <= endDatetime.getTime()) {
                //设置上班时间、下班时间
                Date startOfToday = getStartOfToday(currentDatetime);
                Date endOfToday = getEndOfToday(currentDatetime);
                System.out.println("上班时间:" + Constants.getSimpleDateFormat2().format(startOfToday));

                //每天上班开机后，POS都会先做开机同步
                try {
                    programs[Program.EnumProgramType.EPT_SyncData.getIndex()].run(startOfToday, errorInfo, programs, bRunInRandomMode);
                    waitSyncDone();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Assert.assertTrue("开机同步失败！", false);
                }

                ((RetailTradeAggregation) programs[Program.EnumProgramType.EPT_RetailTradeAggregation.getIndex()]).setWorkTimeStart(startOfToday);//记下上班时间，收银汇总需要使用
                Date now = (Date) startOfToday.clone();
                while (now.getTime() < endOfToday.getTime()) { //模拟一天内收银员及其它人的主要活动
                    now = DatetimeUtil.addMinutes(now, new Random().nextInt(60));//时间逐渐流逝,以分钟为单位 TODO
                    Random random = new Random();
                    for (int i = 0; i < programs.length; i++) {
                        if (i != Program.EnumProgramType.EPT_RetailTradeAggregation.getIndex() && i != Program.EnumProgramType.EPT_SyncData.getIndex() //
                                && i != Program.EnumProgramType.EPT_PosLogin.getIndex()) { //活动7(收银汇总)和8(开机同步)不需要在一天中重复做

                            if (random.nextBoolean()) {
                                if (!programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()].run(now, errorInfo, programs, bRunInRandomMode)) {//活动1（收银）
                                    //...
                                }
                            } else {
                                ((QueryRetailTrade) programs[Program.EnumProgramType.EPT_QueryRetailTrade.getIndex()]).setListRTForQuery(((RetailTrade) programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()]).getListRTIsFromNBR());
                                if (!programs[Program.EnumProgramType.EPT_QueryRetailTrade.getIndex()].run(now, errorInfo, programs, bRunInRandomMode)) {//活动5（查单）
                                    //...
                                }
                            }
                            //...其它活动
                        }
                    }
                    //...循环调用program并写入报表给BA。打印报表
                }

                GlobalController.getInstance().getRetailTradePresenter().retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, retailTradeSQLiteEvent);
                long lTimeOut1 = UNIT_TEST_TimeOut;
                while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                        retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut1-- > 0) {
                    Thread.sleep(1000);
                }
                if (retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
                    Assert.assertTrue("retrieveNAsync超时！retailTradeSQLiteEvent的状态为：" + retailTradeSQLiteEvent.getStatus(), false);
                }
                Assert.assertTrue("retrieveNAsync会返回所有的数据", retailTradeSQLiteEvent.getListMasterTable().size() > 0);

                //...上传残余的零售单
                retailTradeRetrieveNToUpload();

                //获取本日生成的零售单并生成收银汇总
                ((RetailTradeAggregation) programs[Program.EnumProgramType.EPT_RetailTradeAggregation.getIndex()]).setListRT(((RetailTrade) programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()]).getListRT());
                if (!programs[Program.EnumProgramType.EPT_RetailTradeAggregation.getIndex()].run(new Date(), errorInfo, programs, bRunInRandomMode)) {
                    //...
                }
                ((RetailTrade) programs[Program.EnumProgramType.EPT_RetailTrade.getIndex()]).resetForNextDay();
                //
                System.out.println("店员" + BaseActivity.retailTradeAggregation.getStaffID() + "退出登录");
                if (!logoutHttpBO.logoutAsync()) {
                    Assert.assertTrue("退出登录失败!", false);
                }
                //
                System.out.println("下班时间:" + Constants.getSimpleDateFormat2().format(now));
                System.out.println("一天的错误信息：" + errorInfo.toString());

                currentDatetime = DatetimeUtil.addDays(currentDatetime, 1);
            }//结束了当天的活动
        }
        return true;
    }

    /**
     * 等待pos和staff登录后的数据同步完成。
     * 数据同步中，VIP的同步是最后一个，所以以其结束为准。
     *
     * @throws InterruptedException
     */
    public static void waitSyncDone() throws InterruptedException {//...同步是否成功？
        long lTimeout = 60; // 使等待时间更长些....

        while (promotionSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                promotionSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteBO.getSqLiteEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue("Promotion同步超时!", false);
        }
//
//        while (vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
//                && vipSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {//...变量命名和注释
//            Thread.sleep(1000);
//        }
//        //...处理超时
//        vipHttpEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
//        if (vipSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
//            System.out.println("vipHttpEvent.getStatus(): " + vipHttpEvent.getStatus() + "   vipSQLiteEvent.getStatus(): " + vipSQLiteEvent.getStatus());
//            Assert.assertTrue("请求超时！", false);
//        }
////        if (vipHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done || vipSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//        vipHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        }
    }

    public static void syncTime() {
        long timeStamp = new Date().getTime();
        ntpHttpEvent.setId(ROBOT_EVENT_ID_NTP);
        if (!ntpHttpBO.syncTime(timeStamp)) {
            Assert.assertTrue("POS机启动时，同步Ntp失败！", false);
        }
    }

    public static void retrieveNCBarcodes(Barcodes barcodes) {
        barcodesSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        if (!barcodesHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, barcodes)) {
            Assert.assertTrue("POS机启动时，同步Barcodes失败！", false);
        }
    }

    public static void retrieveNCBrand(Brand brand) {
        brandSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        if (!brandHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, brand)) {
            Assert.assertTrue("POS机启动时，同步Brand失败！", false);
        }
    }

    public static void retrieveNCCommodityCategory(CommodityCategory commodityCategory) {
        commodityCategorySQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        commodityCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodityCategory)) {
            Assert.assertTrue("POS机启动时，同步CommodityCategory失败！", false);
        }
    }

    public static void retrieveNCPackageUnit(PackageUnit packageUnit) {
        packageUnitSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            Assert.assertTrue("POS机启动时，同步CommodityCategory失败！", false);
        }
    }

    public static void retrieveNCCommodity(Commodity commodity) {
        commoditySQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        if (!commodityHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, commodity)) {
            Assert.assertTrue("POS机启动时，同步Commodity失败！", false);
        }
    }

    public static void retrieveNCConfigGeneral() throws InterruptedException {
        configGeneralSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigCacheSize_RefreshByServerDataAsyncC_Done);
        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步ConfigGeneral失败！", false);
        }
    }

    public static void retrieveNPos() {
        posSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Pos失败！", false);
        }
    }

    public static void feedbackPos(String ids) {
        posHttpBO.getHttpEvent().setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!posHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Pos失败！！", false);
        }
    }

    public static void feedbackRetailTrade(String ids) {
        retailTradeHttpBO.getHttpEvent().setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!retailTradeHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback RetailTrade失败！！", false);
        }
    }

    public static void retrieveNSmallSheet() {
        smallSheetSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步SmallSheet失败！", false);
        }
    }

    public static void feedbackSmallSheet(String ids) {
        smallSheetHttpBO.getHttpEvent().setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!smallSheetHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback SmallSheet失败！！", false);
        }
    }

    public static void retrieveNVipCategory() {
        vipCategorySQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        vipCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsync_Done);
        if (!vipCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步VipCategory失败！", false);
        }
    }

    public static void feedbackVipCategory(String ids) {
        vipCategorySQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!vipCategoryHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback VipCategory失败！", false);
        }
    }

    public static void retrieveNVip() {
        vipSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RetrieveNAsync);
        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Vip失败！", false);
        }
    }

    public static void feedbackVip(String ids) {
        vipHttpBO.getHttpEvent().setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!vipHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Vip失败", false);
        }
    }

    public static void retrieveNPromotion() {
        promotionSQLiteEvent.setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("POS机启动时，同步Promotion失败！", false);
        }
    }

    public static void feedbackPromotion(String ids) {
        promotionHttpBO.getHttpEvent().setId(ROBOT_EVENT_ID_STARTUP_SYNC);
        if (!promotionHttpBO.feedback(ids)) {
            Assert.assertTrue("POS机启动时，feedback Promotion失败！", false);
        }
    }

    /**
     * 上传SQLite中RetailTradeAggregation的临时数据
     */
    public static void uploadRetailTradeAggregationTempData(com.bx.erp.model.RetailTradeAggregation retailTradeAggregation) {
        retailTradeAggregation.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(retailTradeAggregation.getSyncType())) {
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setBaseModel1(retailTradeAggregation);

            if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
                Assert.assertTrue("上传收银汇总失败！", false);
            }
        }
    }

    /**
     * 上传SQLite中SmallSheet的临时数据
     */
    public static void uploadSmallSheetTempData(SmallSheetFrame smallSheetFrame) {
        //创建小票格式int1代表返回对象
        smallSheetFrame.setReturnObject(1);
        if (BasePresenter.SYNC_Type_C.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteEvent.setTmpMasterTableObj(smallSheetFrame);
            if (!smallSheetHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                junit.framework.Assert.assertTrue("启动时上传小票格式创建失败！", false);
            }
        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
            if (!smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                junit.framework.Assert.assertTrue("启动时上传小票格式修改失败！", false);
            }
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
            if (!smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                junit.framework.Assert.assertTrue("启动时上传小票格式删除失败！", false);
            }
        }
    }

    //TODO 将以下3个和读取xls表的方法移动到新类中

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的SmallSheet的临时数据
     *
     * @return
     */
    public static List<SmallSheetFrame> retrieveNSmallSheetTempDataInSQLite() {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload, smallSheetFrame);//找到主表List
        return frameList;
    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的RetailTradeAggregation的临时数据
     */
    public static List<com.bx.erp.model.RetailTradeAggregation> retrieveNRetailTradeAggregationTempDataInSQLite() {
        com.bx.erp.model.RetailTradeAggregation retailTradeAggregation = new com.bx.erp.model.RetailTradeAggregation();
        List<com.bx.erp.model.RetailTradeAggregation> retailTradeAggregationList = new ArrayList<com.bx.erp.model.RetailTradeAggregation>();
        retailTradeAggregation.setSql("where F_SyncDatetime = ?");
        retailTradeAggregation.setConditions(new String[]{"0"});
        retailTradeAggregationList = (List<com.bx.erp.model.RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation);
        return retailTradeAggregationList;
    }

    public static void retailTradeRetrieveNToUpload() {
        try {
            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                Assert.assertTrue("查询临时零售单失败", false);
            }

            long lTimeOut = Shared.UNIT_TEST_TimeOut;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                Assert.assertTrue("同步服务器返回的一批零售单失败。 原因：超时！", false);
            }
            System.out.println("====同步服务器返回的一批零售单，" + retailTradeSQLiteBO.getHttpEvent().getListMasterTable());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.assertTrue("同步服务器返回的一批零售单失败。", false);
        }
    }

    protected List<List<String>> readExcelSheetForActivityUnit() {
        List<List<String>> listRows = new ArrayList<List<String>>(); // sheet表的所有数据
        List<List<String>> listProgramUnit = new ArrayList<List<String>>(); // 用于保存nbr机器餐返回的数据
        //
        List<String> listCell = poiXls.readExcelCell(Program.file, "活动1", 0); // 第0列的所有数据
        for (int i = 0; i < listCell.size(); i++) { // 遍历所有列，获取sheet表的所有数据
            List<String> listRow = poiXls.readExcelRow(Program.file, "活动1", i); // 第i行的所有数据
            listRows.add(listRow);
        }
        for (int activitySequence = 1; activitySequence < listRows.size(); activitySequence++) { // 第n个活动，机器餐是什么，机器餐为null则这个时间点无活动
            List<String> listR = listRows.get(activitySequence);
            String row = listR.get(2);
            if (row != null) {
                List<String> programUnit = readExcelSheetForPOSMachineMeal(Integer.valueOf(row));
                listProgramUnit.add(programUnit);
            }
        }
        return listProgramUnit;
    }

    protected List<String> readExcelSheetForNBRMachineMeal(int activitySequence) {
        List<String> listRow = poiXls.readExcelRow(Program.file, "nbr机器餐", activitySequence);
        return listRow;
    }

    protected List<String> readExcelSheetForPOSMachineMeal(int activitySequence) {
        List<String> listRow = poiXls.readExcelRow(Program.file, "pos1机器餐", activitySequence);
        return listRow;
    }

    protected Map<String, List<String>> readExcelSheet(int sheetAtNO) {
        List<String> row = new ArrayList<String>();
        Map<String, List<String>> mapBaseModel = new HashMap<String, List<String>>();
        List<String> listCell = poiXls.readExcelCell(Program.file, sheetAtNO, 0); // 第0列的所有数据
        for (int i = 0; i < listCell.size(); i++) {
            row = poiXls.readExcelRow(Program.file, sheetAtNO, i);
            mapBaseModel.put(row.get(0), row);
        }
        return mapBaseModel;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                event.setRequestType(null);
                Ntp ntp = (Ntp) event.getBaseModel1();
                NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
            }
        }
    }
}
