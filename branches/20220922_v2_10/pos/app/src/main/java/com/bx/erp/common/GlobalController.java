package com.bx.erp.common;

import android.content.Context;

import com.bx.erp.bo.BXConfigGeneralHttpBO;
import com.bx.erp.bo.BXConfigGeneralSQLiteBO;
import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.bo.BrandSQLiteBO;
import com.bx.erp.bo.CommodityCategoryHttpBO;
import com.bx.erp.bo.CommodityCategorySQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.CompanyHttpBO;
import com.bx.erp.bo.CompanySQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.bo.DownloadBaseDataMessageBO;
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
import com.bx.erp.bo.PurchasingOrderSQLiteBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.ReturnCommoditySheetHttpBO;
import com.bx.erp.bo.ReturnCommoditySheetSQLiteBO;
import com.bx.erp.bo.ShopHttpBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.StaffSQLiteBO;
import com.bx.erp.bo.VipCategoryHttpBO;
import com.bx.erp.bo.VipCategorySQLiteBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.VipSQLiteBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.bo.WarehousingHttpBO;
import com.bx.erp.bo.WarehousingSQLiteBO;
import com.bx.erp.event.BXConfigGeneralHttpEvent;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.CompanyHttpEvent;
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
import com.bx.erp.event.PurchasingOrderSQLiteEvent;
import com.bx.erp.event.RetailTradeAggregationEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.ReturnCommoditySheetHttpEvent;
import com.bx.erp.event.ReturnCommoditySheetSQLiteEvent;
import com.bx.erp.event.ShopHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BXConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.CompanySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.DownloadBaseDataMessageEvent;
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
import com.bx.erp.event.WarehousingHttpEvent;
import com.bx.erp.event.WarehousingSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoMaster;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.MyOpenHelper;
import com.bx.erp.presenter.BXConfigGeneralPresenter;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.BrandPresenter;
import com.bx.erp.presenter.CommodityCategoryPresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.CommodityShopInfoPresenter;
import com.bx.erp.presenter.CompanyPresenter;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.presenter.CouponPresenter;
import com.bx.erp.presenter.CouponScopePresenter;
import com.bx.erp.presenter.PackageUnitPresenter;
import com.bx.erp.presenter.PosPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.PromotionScopePresenter;
import com.bx.erp.presenter.PromotionShopScopePresenter;
import com.bx.erp.presenter.RememberLoginStaffPresenter;
import com.bx.erp.presenter.RetailTradeAggregationPresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradeCouponPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.presenter.VipCategoryPresenter;
import com.bx.erp.presenter.VipPresenter;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class GlobalController {
    private static GlobalController globalController = null;

    /**
     * 在微信支付的时候，这个时间要>=nbr请求微信支付的超时时间+10秒。如果这里是60s，nbr和微信通讯的超时时间是50s或以下
     */
    public static final int HTTP_REQ_Timeout = 60;

    //设置OKHttp的超时时间
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .writeTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .build();

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getPwdEncrypted() {
        return pwdEncrypted;
    }

    public void setPwdEncrypted(String pwdEncrypted) {
        this.pwdEncrypted = pwdEncrypted;
    }

    public static List<BaseModel> getList() {
        return list;
    }

    public static void setList(List<BaseModel> list) {
        GlobalController.list = list;
    }

    /**
     * 保存POS/Saff和服务器通信的会话
     */
    protected String sessionID;

    protected String pwdEncrypted;

    protected static List<BaseModel> list = new ArrayList<BaseModel>(); //用于装载配置性数据的

//    public PosLoginHttpEvent getPosLoginHttpEvent() {
//        return posLoginHttpEvent;
//    }
//
//    public PosSQLiteEvent getPosSQLiteEvent() {
//        return posSQLiteEvent;
//    }
//
//    public StaffLoginHttpEvent getStaffLoginHttpEvent() {
//        return staffLoginHttpEvent;
//    }
//
//    public PosHttpEvent getPosHttpEvent() {
//        return posHttpEvent;
//    }
//
//    public StaffHttpEvent getStaffHttpEvent() {
//        return staffHttpEvent;
//    }
//
//    public LogoutHttpEvent getLogoutHttpEvent() {
//        return logoutHttpEvent;
//    }
//
//    public SmallSheetSQLiteEvent getSmallSheetSQLiteEvent() {
//        return smallSheetSQLiteEvent;
//    }
//
//    public SmallSheetHttpEvent getSmallSheetHttpEvent() {
//        return smallSheetHttpEvent;
//    }
//
//    public RetailTradeAggregationHttpEvent getRetailTradeAggregationHttpEvent() {
//        return retailTradeAggregationHttpEvent;
//    }
//
//    public RetailTradeHttpEvent getRetailTradeHttpEvent() {
//        return retailTradeHttpEvent;
//    }
//
//    public BrandHttpEvent getBrandHttpEvent() {
//        return brandHttpEvent;
//    }
//
//    public RetailTradePromotingHttpEvent getRetailTradePromotingHttpEvent() {
//        return retailTradePromotingHttpEvent;
//    }
//
//    public CommodityCategoryHttpEvent getCommodityCategoryHttpEvent() {
//        return commodityCategoryHttpEvent;
//    }
//
//    public PromotionHttpEvent getPromotionHttpEvent() {
//        return promotionHttpEvent;
//    }
//
//    public VipCategoryHttpEvent getVipCategoryHttpEvent() {
//        return vipCategoryHttpEvent;
//    }
//
//    public CommodityHttpEvent getCommodityHttpEvent() {
//        return commodityHttpEvent;
//    }
//
//    public BarcodesHttpEvent getBarcodesHttpEvent() {
//        return barcodesHttpEvent;
//    }
//
//    public PackageUnitHttpEvent getPackageUnitHttpEvent() {
//        return packageUnitHttpEvent;
//    }
//
//    public ConfigGeneralHttpEvent getConfigGeneralHttpEvent() {
//        return configGeneralHttpEvent;
//    }
//
//    public BXConfigGeneralHttpEvent getBXConfigGeneralHttpEvent() {
//        return bxConfigGeneralHttpEvent;
//    }
//
//    public VipHttpEvent getVipHttpEvent() {
//        return vipHttpEvent;
//    }
//
//    public ReturnCommoditySheetHttpEvent getReturnCommoditySheetHttpEvent() {
//        return returnCommoditySheetHttpEvent;
//    }
//
//    public WarehousingHttpEvent getWarehousingHttpEvent() {
//        return warehousingHttpEvent;
//    }
//
//    public ConfigCacheSizeHttpEvent getConfigCacheSizeHttpEvent() {
//        return configCacheSizeHttpEvent;
//    }
//
//    public PurchasingOrderHttpEvent getPurchasingOrderHttpEvent() {
//        return purchasingOrderHttpEvent;
//    }
//
//    public NtpHttpEvent getNtpHttpEvent() {
//        return ntpHttpEvent;
//    }
//
//    public DownloadBaseDataMessageEvent getDownloadBaseDataMessageEvent() {
//        return downloadBaseDataMessageEvent;
//    }
//    //
//
//    public StaffSQLiteEvent getStaffSQLiteEvent() {
//        return staffSQLiteEvent;
//    }
//
//    public RetailTradeSQLiteEvent getRetailTradeSQLiteEvent() {
//        return retailTradeSQLiteEvent;
//    }
//
//    public BrandSQLiteEvent getBrandSQLiteEvent() {
//        return brandSQLiteEvent;
//    }
//
//    public RetailTradePromotingSQLiteEvent getRetailTradePromotingSQLiteEvent() {
//        return retailTradePromotingSQLiteEvent;
//    }
//
//    public PromotionSQLiteEvent getPromotionSQLiteEvent() {
//        return promotionSQLiteEvent;
//    }
//
//    public CommodityCategorySQLiteEvent getCommodityCategorySQLiteEvent() {
//        return commodityCategorySQLiteEvent;
//    }
//
//    public VipCategorySQLiteEvent getVipCategorySQLiteEvent() {
//        return vipCategorySQLiteEvent;
//    }
//
//    public BarcodesSQLiteEvent getBarcodesSQLiteEvent() {
//        return barcodesSQLiteEvent;
//    }
//
//    public CommoditySQLiteEvent getCommoditySQLiteEvent() {
//        return commoditySQLiteEvent;
//    }
//
//    public RetailTradeAggregationSQLiteEvent getRetailTradeAggregationSQLiteEvent() {
//        return retailTradeAggregationSQLiteEvent;
//    }
//
//    public ReturnCommoditySheetSQLiteEvent getReturnCommoditySheetSQLiteEvent() {
//        return returnCommoditySheetSQLiteEvent;
//    }
//
//    public PackageUnitSQLiteEvent getPackageUnitSQLiteEvent() {
//        return packageUnitSQLiteEvent;
//    }
//
//    public ConfigGeneralSQLiteEvent getConfigGeneralSQLiteEvent() {
//        return configGeneralSQLiteEvent;
//    }
//
//    public BXConfigGeneralSQLiteEvent getBXConfigGeneralSQLiteEvent() {
//        return bxConfigGeneralSQLiteEvent;
//    }
//
//    public VipSQLiteEvent getVipSQLiteEvent() {
//        return vipSQLiteEvent;
//    }
//
//    public WarehousingSQLiteEvent getWarehousingSQLiteEvent() {
//        return warehousingSQLiteEvent;
//    }
//
//    public PurchasingOrderSQLiteEvent getPurchasingOrderSQLiteEvent() {
//        return purchasingOrderSQLiteEvent;
//    }
//
//    public ConfigCacheSizeSQLiteEvent getConfigCacheSizeSQLiteEvent() {
//        return configCacheSizeSQLiteEvent;
//    }
//
//    public WXPayHttpEvent getWXPayHttpEvent() {
//        return wxPayHttpEvent;
//    }
//
//    //
//
//    public PosLoginHttpBO getPosLoginHttpBO() {
//        return posLoginHttpBO;
//    }
//
//    public StaffLoginHttpBO getStaffLoginHttpBO() {
//        return staffLoginHttpBO;
//    }
//
//    public LogoutHttpBO getLogoutHttpBO() {
//        return logoutHttpBO;
//    }
//
//    public SmallSheetHttpBO getSmallSheetHttpBO() {
//        return smallSheetHttpBO;
//    }
//
//    public SmallSheetSQLiteBO getSmallSheetSQLiteBO() {
//        return smallSheetSQLiteBO;
//    }
//
//    public RetailTradeAggregationHttpBO getRetailTradeAggregationHttpBO() {
//        return retailTradeAggregationHttpBO;
//    }
//
//    public RetailTradeAggregationSQLiteBO getRetailTradeAggregationSQLiteBO() {
//        return retailTradeAggregationSQLiteBO;
//    }
//
//    public PosSQLiteBO getPosSQLiteBO() {
//        return posSQLiteBO;
//    }
//
//    public PosHttpBO getPosHttpBO() {
//        return posHttpBO;
//    }
//
//    public StaffHttpBO getStaffHttpBO() {
//        return staffHttpBO;
//    }
//
//    public StaffSQLiteBO getStaffSQLiteBO() {
//        return staffSQLiteBO;
//    }
//
//    public RetailTradeHttpBO getRetailTradeHttpBO() {
//        return retailTradeHttpBO;
//    }
//
//    public RetailTradeSQLiteBO getRetailTradeSQLiteBO() {
//        return retailTradeSQLiteBO;
//    }
//
//    public BrandHttpBO getBrandHttpBO() {
//        return brandHttpBO;
//    }
//
//    public BrandSQLiteBO getBrandSQLiteBO() {
//        return brandSQLiteBO;
//    }
//
//    public RetailTradePromotingHttpBO getRetailTradePromotingHttpBO() {
//        return retailTradePromotingHttpBO;
//    }
//
//    public RetailTradePromotingSQLiteBO getRetailTradePromotingSQLiteBO() {
//        return retailTradePromotingSQLiteBO;
//    }
//
//    public PromotionHttpBO getPromotionHttpBO() {
//        return promotionHttpBO;
//    }
//
//    public PromotionSQLiteBO getPromotionSQLiteBO() {
//        return promotionSQLiteBO;
//    }
//
//    public CommodityCategoryHttpBO getCommodityCategoryHttpBO() {
//        return commodityCategoryHttpBO;
//    }
//
//    public CommodityCategorySQLiteBO getCommodityCategorySQLiteBO() {
//        return commodityCategorySQLiteBO;
//    }
//
//    public VipCategoryHttpBO getVipCategoryHttpBO() {
//        return vipCategoryHttpBO;
//    }
//
//    public VipCategorySQLiteBO getVipCategorySQLiteBO() {
//        return vipCategorySQLiteBO;
//    }
//
//    public BarcodesHttpBO getBarcodesHttpBO() {
//        return barcodesHttpBO;
//    }
//
//    public BarcodesSQLiteBO getBarcodesSQLiteBO() {
//        return barcodesSQLiteBO;
//    }
//
//    public CommodityHttpBO getCommodityHttpBO() {
//        return commodityHttpBO;
//    }
//
//    public CommoditySQLiteBO getCommoditySQLiteBO() {
//        return commoditySQLiteBO;
//    }
//
//    public PackageUnitHttpBO getPackageUnitHttpBO() {
//        return packageUnitHttpBO;
//    }
//
//    public PackageUnitSQLiteBO getPackageUnitSQLiteBO() {
//        return packageUnitSQLiteBO;
//    }
//
//    public ConfigCacheSizeHttpBO getConfigCacheSizeHttpBO() {
//        return configCacheSizeHttpBO;
//    }
//
//    public ConfigCacheSizeSQLiteBO getConfigCacheSizeSQLiteBO() {
//        return configCacheSizeSQLiteBO;
//    }
//
//    public ConfigGeneralHttpBO getConfigGeneralHttpBO() {
//        return configGeneralHttpBO;
//    }
//
//    public ConfigGeneralSQLiteBO getConfigGeneralSQLiteBO() {
//        return configGeneralSQLiteBO;
//    }
//
//    public BXConfigGeneralHttpBO getBXConfigGeneralHttpBO() {
//        return bxConfigGeneralHttpBO;
//    }
//
//    public BXConfigGeneralSQLiteBO getBXConfigGeneralSQLiteBO() {
//        return bxConfigGeneralSQLiteBO;
//    }
//
//    public ReturnCommoditySheetHttpBO getReturnCommoditySheetHttpBO() {
//        return returnCommoditySheetHttpBO;
//    }
//
//    public ReturnCommoditySheetSQLiteBO getReturnCommoditySheetSQLiteBO() {
//        return returnCommoditySheetSQLiteBO;
//    }
//
//    public VipHttpBO getVipHttpBO() {
//        return vipHttpBO;
//    }
//
//    public VipSQLiteBO getVipSQLiteBO() {
//        return vipSQLiteBO;
//    }
//
//    public WarehousingHttpBO getWarehousingHttpBO() {
//        return warehousingHttpBO;
//    }
//
//    public WarehousingSQLiteBO getWarehousingSQLiteBO() {
//        return warehousingSQLiteBO;
//    }
//
//    public PurchasingOrderHttpBO getPurchasingOrderHttpBO() {
//        return purchasingOrderHttpBO;
//    }
//
//    public PurchasingOrderSQLiteBO getPurchasingOrderSQLiteBO() {
//        return purchasingOrderSQLiteBO;
//    }
//
//    public NtpHttpBO getNtpHttpBO() {
//        return ntpHttpBO;
//    }
//
//    public RetailTradeAggregationEvent getRetailTradeAggregationEvent() {
//        return retailTradeAggregationEvent;
//    }
//
//    public ShopHttpBO getShopHttpBO() {
//        return shopHttpBO;
//    }
//
//    public ShopHttpEvent getShopHttpEvent() {
//        return shopHttpEvent;
//    }
//
//    public WXPayHttpBO getWXPayHttpBO() {
//        return wxPayHttpBO;
//    }
//
//    public CompanyHttpBO getCompanyHttpBO() {
//        return companyHttpBO;
//    }
//
//    public CompanySQLiteBO getCompanySQLiteBO() {
//        return companySQLiteBO;
//    }
//
//    public DownloadBaseDataMessageBO getDownloadBaseDataMessageBO() {
//        return downloadBaseDataMessageBO;
//    }
//
//    public CompanyHttpEvent getCompanyHttpEvent() {
//        return companyHttpEvent;
//    }
//
//    public CompanySQLiteEvent getCompanySQLiteEvent() {
//        return companySQLiteEvent;
//    }


    Database db;

    public Database getDb() {
        return db;
    }

    protected DaoSession daoSession;
//
//    protected PosLoginHttpEvent posLoginHttpEvent;
//    protected StaffLoginHttpEvent staffLoginHttpEvent;
//    protected PosSQLiteEvent posSQLiteEvent;
//    protected PosHttpEvent posHttpEvent;
//    protected StaffHttpEvent staffHttpEvent;
//    protected LogoutHttpEvent logoutHttpEvent;
//    protected SmallSheetSQLiteEvent smallSheetSQLiteEvent;
//    protected SmallSheetHttpEvent smallSheetHttpEvent;
//    protected  retailTradeAggregationSQLiteEvent;
//    protected RetailTradeAggregatioRetailTradeAggregationSQLiteEventnHttpEvent retailTradeAggregationHttpEvent;
//    protected StaffSQLiteEvent staffSQLiteEvent;
//    protected RetailTradeSQLiteEvent retailTradeSQLiteEvent;
//    protected RetailTradeHttpEvent retailTradeHttpEvent;
//    protected BrandSQLiteEvent brandSQLiteEvent;
//    protected BrandHttpEvent brandHttpEvent;
//    protected RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
//    protected RetailTradePromotingHttpEvent retailTradePromotingHttpEvent;
//    protected PromotionSQLiteEvent promotionSQLiteEvent;
//    protected PromotionHttpEvent promotionHttpEvent;
//    protected CommodityCategorySQLiteEvent commodityCategorySQLiteEvent;
//    protected CommodityCategoryHttpEvent commodityCategoryHttpEvent;
//    protected VipCategorySQLiteEvent vipCategorySQLiteEvent;
//    protected VipCategoryHttpEvent vipCategoryHttpEvent;
//    protected BarcodesSQLiteEvent barcodesSQLiteEvent;
//    protected BarcodesHttpEvent barcodesHttpEvent;
//    protected CommodityHttpEvent commodityHttpEvent;
//    protected CommoditySQLiteEvent commoditySQLiteEvent;
//    protected PackageUnitSQLiteEvent packageUnitSQLiteEvent;
//    protected PackageUnitHttpEvent packageUnitHttpEvent;
//    protected ConfigCacheSizeHttpEvent configCacheSizeHttpEvent;
//    protected ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent;
//    protected ConfigGeneralHttpEvent configGeneralHttpEvent;
//    protected ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
//    protected BXConfigGeneralHttpEvent bxConfigGeneralHttpEvent;
//    protected BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent;
//    protected ReturnCommoditySheetHttpEvent returnCommoditySheetHttpEvent;
//    protected ReturnCommoditySheetSQLiteEvent returnCommoditySheetSQLiteEvent;
//    protected VipHttpEvent vipHttpEvent;
//    protected VipSQLiteEvent vipSQLiteEvent;
//    protected WarehousingHttpEvent warehousingHttpEvent;
//    protected WarehousingSQLiteEvent warehousingSQLiteEvent;
//    protected PurchasingOrderHttpEvent purchasingOrderHttpEvent;
//    protected PurchasingOrderSQLiteEvent purchasingOrderSQLiteEvent;
//    protected NtpHttpEvent ntpHttpEvent;
//    protected WXPayHttpEvent wxPayHttpEvent;
//    protected ShopHttpEvent shopHttpEvent;
//    protected CompanyHttpEvent companyHttpEvent;
//    protected CompanySQLiteEvent companySQLiteEvent;
//    protected DownloadBaseDataMessageEvent downloadBaseDataMessageEvent;
//
//    protected PosLoginHttpBO posLoginHttpBO;
//    protected StaffLoginHttpBO staffLoginHttpBO;
//    protected LogoutHttpBO logoutHttpBO;
//    protected SmallSheetHttpBO smallSheetHttpBO;
//    protected SmallSheetSQLiteBO smallSheetSQLiteBO;
//    protected RetailTradeAggregationHttpBO retailTradeAggregationHttpBO;
//    protected RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
//    protected StaffHttpBO staffHttpBO;
//    protected StaffSQLiteBO staffSQLiteBO;
//    protected RetailTradeHttpBO retailTradeHttpBO;
//    protected RetailTradeSQLiteBO retailTradeSQLiteBO;
//    protected BrandHttpBO brandHttpBO;
//    protected BrandSQLiteBO brandSQLiteBO;
//    protected RetailTradePromotingHttpBO retailTradePromotingHttpBO;
//    protected RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO;
//    protected ShopHttpBO shopHttpBO;
//    protected CompanyHttpBO companyHttpBO;
//    protected CompanySQLiteBO companySQLiteBO;
//    protected PromotionHttpBO promotionHttpBO;
//    protected PromotionSQLiteBO promotionSQLiteBO;
//    protected CommodityCategoryHttpBO commodityCategoryHttpBO;
//    protected CommodityCategorySQLiteBO commodityCategorySQLiteBO;
//    protected VipCategoryHttpBO vipCategoryHttpBO;
//    protected VipCategorySQLiteBO vipCategorySQLiteBO;
//    protected BarcodesHttpBO barcodesHttpBO;
//    protected BarcodesSQLiteBO barcodesSQLiteBO;
//    protected CommodityHttpBO commodityHttpBO;
//    protected CommoditySQLiteBO commoditySQLiteBO;
//    protected PosHttpBO posHttpBO;
//    protected PosSQLiteBO posSQLiteBO;
//    protected PackageUnitHttpBO packageUnitHttpBO;
//    protected PackageUnitSQLiteBO packageUnitSQLiteBO;
//    protected ConfigCacheSizeHttpBO configCacheSizeHttpBO;
//    protected ConfigCacheSizeSQLiteBO configCacheSizeSQLiteBO;
//    protected ConfigGeneralHttpBO configGeneralHttpBO;
//    protected ConfigGeneralSQLiteBO configGeneralSQLiteBO;
//    protected BXConfigGeneralHttpBO bxConfigGeneralHttpBO;
//    protected BXConfigGeneralSQLiteBO bxConfigGeneralSQLiteBO;
//    protected ReturnCommoditySheetHttpBO returnCommoditySheetHttpBO;
//    protected ReturnCommoditySheetSQLiteBO returnCommoditySheetSQLiteBO;
//    protected VipHttpBO vipHttpBO;
//    protected VipSQLiteBO vipSQLiteBO;
//    protected WarehousingHttpBO warehousingHttpBO;
//    protected WarehousingSQLiteBO warehousingSQLiteBO;
//    protected PurchasingOrderHttpBO purchasingOrderHttpBO;
//    protected PurchasingOrderSQLiteBO purchasingOrderSQLiteBO;
//    protected NtpHttpBO ntpHttpBO;
//    protected WXPayHttpBO wxPayHttpBO;
//    protected DownloadBaseDataMessageBO downloadBaseDataMessageBO;
//
//    protected RetailTradeAggregationEvent retailTradeAggregationEvent;

    protected RetailTradePresenter retailTradePresenter;
    protected RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    protected PromotionScopePresenter promotionScopePresenter;
    protected PromotionShopScopePresenter promotionShopScopePresenter;
    protected SmallSheetFramePresenter smallSheetFramePresenter;
    protected SmallSheetTextPresenter smallSheetTextPresenter;
    protected CommodityCategoryPresenter commodityCategoryPresenter;
    protected VipCategoryPresenter vipCategoryPresenter;
    protected BarcodesPresenter barcodesPresenter;
    protected BrandPresenter brandPresenter;
    protected RetailTradePromotingPresenter retailTradePromotingPresenter;
    protected RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
    protected PromotionPresenter promotionPresenter;
    protected ConfigGeneralPresenter configGeneralPresenter;
    protected BXConfigGeneralPresenter bxConfigGeneralPresenter;
    protected CommodityPresenter commodityPresenter;
    protected CommodityShopInfoPresenter commodityShopInfoPresenter;
    protected RetailTradeAggregationPresenter retailTradeAggregationPresenter;
    protected StaffPresenter staffPresenter;
    protected VipPresenter vipPresenter;
    protected PosPresenter posPresenter;
    protected PackageUnitPresenter packageUnitPresenter;
    protected CompanyPresenter companyPresenter;
    protected RetailTradeCouponPresenter retailTradeCouponPresenter;
    protected CouponPresenter couponPresenter;
    protected CouponScopePresenter couponScopePresenter;
    protected RememberLoginStaffPresenter rememberLoginStaffPresenter;

    public RememberLoginStaffPresenter getRememberLoginStaffPresenter() {
        return rememberLoginStaffPresenter;
    }

    public CouponPresenter getCouponPresenter() {
        return couponPresenter;
    }

    public CouponScopePresenter getCouponScopePresenter() {
        return couponScopePresenter;
    }

    public RetailTradePresenter getRetailTradePresenter() {
        return retailTradePresenter;
    }

    public RetailTradeCommodityPresenter getRetailTradeCommodityPresenter() {
        return retailTradeCommodityPresenter;
    }

    public PromotionScopePresenter getPromotionScopePresenter() {
        return promotionScopePresenter;
    }

    public PromotionShopScopePresenter getPromotionShopScopePresenter() {
        return promotionShopScopePresenter;
    }

    public SmallSheetFramePresenter getSmallSheetFramePresenter() {
        return smallSheetFramePresenter;
    }

    public SmallSheetTextPresenter getSmallSheetTextPresenter() {
        return smallSheetTextPresenter;
    }

    public CommodityCategoryPresenter getCommodityCategoryPresenter() {
        return commodityCategoryPresenter;
    }

    public VipCategoryPresenter getVipCategoryPresenter() {
        return vipCategoryPresenter;
    }

    public BarcodesPresenter getBarcodesPresenter() {
        return barcodesPresenter;
    }

    public BrandPresenter getBrandPresenter() {
        return brandPresenter;
    }

    public RetailTradePromotingPresenter getRetailTradePromotingPresenter() {
        return retailTradePromotingPresenter;
    }

    public RetailTradePromotingFlowPresenter getRetailTradePromotingFlowPresenter() {
        return retailTradePromotingFlowPresenter;
    }

    public PromotionPresenter getPromotionPresenter() {
        return promotionPresenter;
    }

    public CommodityPresenter getCommodityPresenter() {
        return commodityPresenter;
    }

    public CommodityShopInfoPresenter getCommodityShopInfoPresenter() {
        return commodityShopInfoPresenter;
    }

    public ConfigGeneralPresenter getConfigGeneralPresenter() {
        return configGeneralPresenter;
    }

    public BXConfigGeneralPresenter getBXConfigGeneralPresenter() {
        return bxConfigGeneralPresenter;
    }

    public RetailTradeAggregationPresenter getRetailTradeAggregationPresenter() {
        return retailTradeAggregationPresenter;
    }

    public StaffPresenter getStaffPresenter() {
        return staffPresenter;
    }

    public VipPresenter getVipPresenter() {
        return vipPresenter;
    }

    public PosPresenter getPosPresenter() {
        return posPresenter;
    }

    public PackageUnitPresenter getPackageUnitPresenter() {
        return packageUnitPresenter;
    }

    public CompanyPresenter getCompanyPresenter() {
        return companyPresenter;
    }

    public RetailTradeCouponPresenter getRetailTradeCouponPresenter() {
        return retailTradeCouponPresenter;
    }

    protected Context context;

    public Context getContext() {
        return context;
    }

    protected GlobalController(Context ctx) {
        context = ctx;
        //DB相关的初始化
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "bx-pos-db");
        DaoMaster.DevOpenHelper helper = new MyOpenHelper(ctx, "bx-pos-db");
        db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        //
        staffPresenter = new StaffPresenter(daoSession);
        retailTradePresenter = new RetailTradePresenter(daoSession);
        retailTradeCommodityPresenter = new RetailTradeCommodityPresenter(daoSession);
        promotionScopePresenter = new PromotionScopePresenter(daoSession);
        promotionShopScopePresenter = new PromotionShopScopePresenter(daoSession);
        smallSheetFramePresenter = new SmallSheetFramePresenter(daoSession);
        smallSheetTextPresenter = new SmallSheetTextPresenter(daoSession);
        brandPresenter = new BrandPresenter(daoSession);
        retailTradePromotingPresenter = new RetailTradePromotingPresenter(daoSession);
        retailTradePromotingFlowPresenter = new RetailTradePromotingFlowPresenter(daoSession);
        promotionPresenter = new PromotionPresenter(daoSession);
        commodityCategoryPresenter = new CommodityCategoryPresenter(daoSession);
        vipCategoryPresenter = new VipCategoryPresenter(daoSession);
        barcodesPresenter = new BarcodesPresenter(daoSession);
        commodityPresenter = new CommodityPresenter(daoSession);
        commodityShopInfoPresenter = new CommodityShopInfoPresenter(daoSession);
        posPresenter = new PosPresenter(daoSession);
        retailTradeAggregationPresenter = new RetailTradeAggregationPresenter(daoSession);
        staffPresenter = new StaffPresenter(daoSession);
        configGeneralPresenter = new ConfigGeneralPresenter(daoSession);
        bxConfigGeneralPresenter = new BXConfigGeneralPresenter(daoSession);
        packageUnitPresenter = new PackageUnitPresenter(daoSession);
        vipPresenter = new VipPresenter(daoSession);
        companyPresenter = new CompanyPresenter(daoSession);
        retailTradeCouponPresenter = new RetailTradeCouponPresenter(daoSession);
        couponPresenter = new CouponPresenter(daoSession);
        couponScopePresenter = new CouponScopePresenter(daoSession);
        rememberLoginStaffPresenter = new RememberLoginStaffPresenter(daoSession);

//        posLoginHttpEvent = new PosLoginHttpEvent();
//        staffLoginHttpEvent = new StaffLoginHttpEvent();
//        posHttpEvent = new PosHttpEvent();
//        posSQLiteEvent = new PosSQLiteEvent();
//        staffHttpEvent = new StaffHttpEvent();
//        logoutHttpEvent = new LogoutHttpEvent();
//        smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
//        smallSheetHttpEvent = new SmallSheetHttpEvent();
//        retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
//        retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
//        staffSQLiteEvent = new StaffSQLiteEvent();
//        staffHttpEvent = new StaffHttpEvent();
//        retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//        retailTradeHttpEvent = new RetailTradeHttpEvent();
//        brandSQLiteEvent = new BrandSQLiteEvent();
//        brandHttpEvent = new BrandHttpEvent();
//        retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
//        retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
//        promotionSQLiteEvent = new PromotionSQLiteEvent();
//        promotionHttpEvent = new PromotionHttpEvent();
//        commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
//        commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
//        vipCategorySQLiteEvent = new VipCategorySQLiteEvent();
//        vipCategoryHttpEvent = new VipCategoryHttpEvent();
//        barcodesSQLiteEvent = new BarcodesSQLiteEvent();
//        barcodesHttpEvent = new BarcodesHttpEvent();
//        commoditySQLiteEvent = new CommoditySQLiteEvent();
//        commodityHttpEvent = new CommodityHttpEvent();
//        packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
//        packageUnitHttpEvent = new PackageUnitHttpEvent();
//        configCacheSizeSQLiteEvent = new ConfigCacheSizeSQLiteEvent();
//        configCacheSizeHttpEvent = new ConfigCacheSizeHttpEvent();
//        configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
//        configGeneralHttpEvent = new ConfigGeneralHttpEvent();
//        bxConfigGeneralSQLiteEvent = new BXConfigGeneralSQLiteEvent();
//        bxConfigGeneralHttpEvent = new BXConfigGeneralHttpEvent();
//        returnCommoditySheetHttpEvent = new ReturnCommoditySheetHttpEvent();
//        returnCommoditySheetSQLiteEvent = new ReturnCommoditySheetSQLiteEvent();
//        vipHttpEvent = new VipHttpEvent();
//        vipSQLiteEvent = new VipSQLiteEvent();
//        warehousingHttpEvent = new WarehousingHttpEvent();
//        warehousingSQLiteEvent = new WarehousingSQLiteEvent();
//        purchasingOrderHttpEvent = new PurchasingOrderHttpEvent();
//        purchasingOrderSQLiteEvent = new PurchasingOrderSQLiteEvent();
//        ntpHttpEvent = new NtpHttpEvent();
//        retailTradeAggregationEvent = new RetailTradeAggregationEvent();
//        wxPayHttpEvent = new WXPayHttpEvent();
//        shopHttpEvent = new ShopHttpEvent();
//        companyHttpEvent = new CompanyHttpEvent();
//        companySQLiteEvent = new CompanySQLiteEvent();
//        downloadBaseDataMessageEvent = new DownloadBaseDataMessageEvent();
//
//        posLoginHttpBO = new PosLoginHttpBO(ctx, null, posLoginHttpEvent);
//        staffLoginHttpBO = new StaffLoginHttpBO(ctx, staffSQLiteEvent, staffLoginHttpEvent);
//        logoutHttpBO = new LogoutHttpBO(ctx, null, logoutHttpEvent);
//        smallSheetSQLiteBO = new SmallSheetSQLiteBO(ctx, smallSheetSQLiteEvent, smallSheetHttpEvent);
//        smallSheetHttpBO = new SmallSheetHttpBO(ctx, smallSheetSQLiteEvent, smallSheetHttpEvent);
//        retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(ctx, retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(ctx, retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        staffSQLiteBO = new StaffSQLiteBO(ctx, staffSQLiteEvent, staffHttpEvent);
//        staffHttpBO = new StaffHttpBO(ctx, staffSQLiteEvent, staffHttpEvent);
//        retailTradeSQLiteBO = new RetailTradeSQLiteBO(ctx, retailTradeSQLiteEvent, retailTradeHttpEvent);
//        retailTradeHttpBO = new RetailTradeHttpBO(ctx, retailTradeSQLiteEvent, retailTradeHttpEvent);
//        brandSQLiteBO = new BrandSQLiteBO(ctx, brandSQLiteEvent, brandHttpEvent);
//        brandHttpBO = new BrandHttpBO(ctx, brandSQLiteEvent, brandHttpEvent);
//        retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(ctx, retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
//        retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(ctx, retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
//        promotionSQLiteBO = new PromotionSQLiteBO(ctx, promotionSQLiteEvent, promotionHttpEvent);
//        promotionHttpBO = new PromotionHttpBO(ctx, promotionSQLiteEvent, promotionHttpEvent);
//        commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(ctx, commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        commodityCategoryHttpBO = new CommodityCategoryHttpBO(ctx, commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        vipCategorySQLiteBO = new VipCategorySQLiteBO(ctx, vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        vipCategoryHttpBO = new VipCategoryHttpBO(ctx, vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        barcodesSQLiteBO = new BarcodesSQLiteBO(ctx, barcodesSQLiteEvent, barcodesHttpEvent);
//        barcodesHttpBO = new BarcodesHttpBO(ctx, barcodesSQLiteEvent, barcodesHttpEvent);
//        commoditySQLiteBO = new CommoditySQLiteBO(ctx, commoditySQLiteEvent, commodityHttpEvent);
//        commodityHttpBO = new CommodityHttpBO(ctx, commoditySQLiteEvent, commodityHttpEvent);
//        posHttpBO = new PosHttpBO(ctx, posSQLiteEvent, posHttpEvent);
//        posSQLiteBO = new PosSQLiteBO(ctx, posSQLiteEvent, posHttpEvent);
//        packageUnitHttpBO = new PackageUnitHttpBO(ctx, packageUnitSQLiteEvent, packageUnitHttpEvent);
//        packageUnitSQLiteBO = new PackageUnitSQLiteBO(ctx, packageUnitSQLiteEvent, packageUnitHttpEvent);
//        configCacheSizeHttpBO = new ConfigCacheSizeHttpBO(ctx, configCacheSizeSQLiteEvent, configCacheSizeHttpEvent);
//        configCacheSizeSQLiteBO = new ConfigCacheSizeSQLiteBO(ctx, configCacheSizeSQLiteEvent, configCacheSizeHttpEvent);
//        configGeneralHttpBO = new ConfigGeneralHttpBO(ctx, configGeneralSQLiteEvent, configGeneralHttpEvent);
//        configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(ctx, configGeneralSQLiteEvent, configGeneralHttpEvent);
//        bxConfigGeneralHttpBO = new BXConfigGeneralHttpBO(ctx, bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
//        bxConfigGeneralSQLiteBO = new BXConfigGeneralSQLiteBO(ctx, bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
//        returnCommoditySheetHttpBO = new ReturnCommoditySheetHttpBO(ctx, returnCommoditySheetSQLiteEvent, returnCommoditySheetHttpEvent);
//        returnCommoditySheetSQLiteBO = new ReturnCommoditySheetSQLiteBO(ctx, returnCommoditySheetSQLiteEvent, returnCommoditySheetHttpEvent);
//        vipHttpBO = new VipHttpBO(ctx, vipSQLiteEvent, vipHttpEvent);
//        vipSQLiteBO = new VipSQLiteBO(ctx, vipSQLiteEvent, vipHttpEvent);
//        warehousingHttpBO = new WarehousingHttpBO(ctx, warehousingSQLiteEvent, warehousingHttpEvent);
//        warehousingSQLiteBO = new WarehousingSQLiteBO(ctx, warehousingSQLiteEvent, warehousingHttpEvent);
//        purchasingOrderHttpBO = new PurchasingOrderHttpBO(ctx, purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
//        purchasingOrderSQLiteBO = new PurchasingOrderSQLiteBO(ctx, purchasingOrderSQLiteEvent, purchasingOrderHttpEvent);
//        ntpHttpBO = new NtpHttpBO(ctx, ntpHttpEvent);
//        wxPayHttpBO = new WXPayHttpBO(ctx, null, wxPayHttpEvent);
//        shopHttpBO = new ShopHttpBO(ctx, null, shopHttpEvent);
//        companyHttpBO = new CompanyHttpBO(ctx, companySQLiteEvent, companyHttpEvent);
//        companySQLiteBO = new CompanySQLiteBO(ctx, companySQLiteEvent, companyHttpEvent);
//        downloadBaseDataMessageBO = new DownloadBaseDataMessageBO(ctx, downloadBaseDataMessageEvent, null);
//
//        logoutHttpEvent.setHttpBO(logoutHttpBO);
//        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
//        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
//        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
//        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
//        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
//        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
//        staffHttpEvent.setSqliteBO(staffSQLiteBO);
//        staffHttpEvent.setHttpBO(staffHttpBO);
//        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
//        staffSQLiteEvent.setHttpBO(staffHttpBO);
//        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
//        brandHttpEvent.setSqliteBO(brandSQLiteBO);
//        brandHttpEvent.setHttpBO(brandHttpBO);
//        brandSQLiteEvent.setHttpBO(brandHttpBO);
//        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
//        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
//        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
//        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
//        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
//        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
//        promotionHttpEvent.setHttpBO(promotionHttpBO);
//        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
//        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
//        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
//        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
//        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
//        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
//        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
//        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);
//        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
//        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
//        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
//        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
//        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
//        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
//        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
//        commodityHttpEvent.setHttpBO(commodityHttpBO);
//        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
//        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
//        posHttpEvent.setHttpBO(posHttpBO);
//        posHttpEvent.setSqliteBO(posSQLiteBO);
//        posSQLiteEvent.setSqliteBO(posSQLiteBO);
//        posSQLiteEvent.setHttpBO(posHttpBO);
//        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
//        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
//        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
//        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
//        configCacheSizeHttpEvent.setSqliteBO(configCacheSizeSQLiteBO);
//        configCacheSizeHttpEvent.setHttpBO(configCacheSizeHttpBO);
//        configCacheSizeSQLiteEvent.setSqliteBO(configCacheSizeSQLiteBO);
//        configCacheSizeSQLiteEvent.setHttpBO(configCacheSizeHttpBO);
//        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
//        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
//        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
//        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
//        bxConfigGeneralHttpEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
//        bxConfigGeneralHttpEvent.setHttpBO(bxConfigGeneralHttpBO);
//        bxConfigGeneralSQLiteEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
//        bxConfigGeneralSQLiteEvent.setHttpBO(bxConfigGeneralHttpBO);
//        returnCommoditySheetHttpEvent.setSqliteBO(returnCommoditySheetSQLiteBO);
//        returnCommoditySheetHttpEvent.setHttpBO(returnCommoditySheetHttpBO);
//        returnCommoditySheetSQLiteEvent.setSqliteBO(returnCommoditySheetSQLiteBO);
//        returnCommoditySheetSQLiteEvent.setHttpBO(returnCommoditySheetHttpBO);
//        vipHttpEvent.setSqliteBO(vipSQLiteBO);
//        vipHttpEvent.setHttpBO(vipHttpBO);
//        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
//        vipSQLiteEvent.setHttpBO(vipHttpBO);
//        warehousingHttpEvent.setSqliteBO(warehousingSQLiteBO);
//        warehousingHttpEvent.setHttpBO(warehousingHttpBO);
//        warehousingSQLiteEvent.setSqliteBO(warehousingSQLiteBO);
//        warehousingSQLiteEvent.setHttpBO(warehousingHttpBO);
//        purchasingOrderHttpEvent.setHttpBO(purchasingOrderHttpBO);
//        purchasingOrderHttpEvent.setSqliteBO(purchasingOrderSQLiteBO);
//        purchasingOrderSQLiteEvent.setSqliteBO(purchasingOrderSQLiteBO);
//        purchasingOrderSQLiteEvent.setHttpBO(purchasingOrderHttpBO);
//        ntpHttpEvent.setHttpBO(ntpHttpBO);
//        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
//        shopHttpEvent.setHttpBO(shopHttpBO);
//        companyHttpEvent.setHttpBO(companyHttpBO);
//        companyHttpEvent.setSqliteBO(companySQLiteBO);
//        companySQLiteEvent.setHttpBO(companyHttpBO);
//        companySQLiteEvent.setSqliteBO(companySQLiteBO);
//        downloadBaseDataMessageEvent.setSqliteBO(downloadBaseDataMessageBO);

//        ConfigureLog4J configureLog4J = new ConfigureLog4J();
//        configureLog4J.configure();
    }

    public static GlobalController init(Context ctx) {
        if (globalController == null) {
            globalController = new GlobalController(ctx);
        }
        return null;
    }

    public static GlobalController getInstance() {
        return globalController;
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
