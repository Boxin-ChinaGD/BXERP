package com.bx.erp.utils;

import com.bx.erp.bo.BarcodesHttpBO;
import com.bx.erp.bo.BarcodesSQLiteBO;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BrandHttpBO;
import com.bx.erp.bo.BrandSQLiteBO;
import com.bx.erp.bo.CommodityCategoryHttpBO;
import com.bx.erp.bo.CommodityCategorySQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.bo.CouponSQLiteBO;
import com.bx.erp.bo.DownloadBaseDataMessageBO;
import com.bx.erp.bo.PackageUnitHttpBO;
import com.bx.erp.bo.PackageUnitSQLiteBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.DownloadBaseDataMessageEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Brand;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityCategory;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.SmallSheetFrame;

import org.apache.log4j.Logger;

public class ResetBaseDataUtil {
    private Logger log = Logger.getLogger(this.getClass());

    public ResetBaseDataUtil() {
        initObject();
    }

    public ResetBaseDataUtil(int activityEventID) {
        eventID = activityEventID;
        initObject();
    }

    public static int eventID; //标明是哪个Activity的EventID

    public String message = ""; //下载资料时的信息
    private int count;//call 普通Action的RN，返回总数
    private int runTimes = 1;//需要运行runTimes次，才能把条形码全部重置下来
    private final int UNIT_TimeOut = 600;

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
    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
    private static PackageUnitHttpBO packageUnitHttpBO = null;
    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
    //
    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    //
    private static PromotionHttpEvent promotionHttpEvent = null;
    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
    private static PromotionHttpBO promotionHttpBO = null;
    private static PromotionSQLiteBO promotionSQLiteBO = null;
    //
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
    //
    private static DownloadBaseDataMessageBO downloadBaseDataMessageBO = null;
    private static DownloadBaseDataMessageEvent downloadBaseDataMessageEvent = null;
    //
    public static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    public static SmallSheetHttpBO smallSheetHttpBO = null;
    public static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    public static SmallSheetHttpEvent smallSheetHttpEvent = null;

    public static CouponSQLiteBO couponSQLiteBO;
    public static CouponSQLiteEvent couponSQLiteEvent;
    public static CouponHttpEvent couponHttpEvent;
    public static CouponHttpBO couponHttpBO;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void initObject() {
        if (barcodesSQLiteEvent == null) {
            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
            barcodesSQLiteEvent.setId(eventID);
        }
        if (barcodesHttpEvent == null) {
            barcodesHttpEvent = new BarcodesHttpEvent();
            barcodesHttpEvent.setId(eventID);
        }
        if (barcodesHttpBO == null) {
            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        if (barcodesSQLiteBO == null) {
            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
        }
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        //
        if (brandSQLiteEvent == null) {
            brandSQLiteEvent = new BrandSQLiteEvent();
            brandSQLiteEvent.setId(eventID);
        }
        if (brandHttpEvent == null) {
            brandHttpEvent = new BrandHttpEvent();
            brandHttpEvent.setId(eventID);
        }
        if (brandHttpBO == null) {
            brandHttpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        if (brandSQLiteBO == null) {
            brandSQLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
        }
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        //
        if (commodityCategorySQLiteEvent == null) {
            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
            commodityCategorySQLiteEvent.setId(eventID);
        }
        if (commodityCategoryHttpEvent == null) {
            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
            commodityCategoryHttpEvent.setId(eventID);
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
        if (packageUnitSQLiteEvent == null) {
            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
            packageUnitSQLiteEvent.setId(eventID);
        }
        if (packageUnitHttpEvent == null) {
            packageUnitHttpEvent = new PackageUnitHttpEvent();
            packageUnitHttpEvent.setId(eventID);
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
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(eventID);
        }
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(eventID);
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
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(eventID);
        }
        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(eventID);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        //
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(eventID);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(eventID);
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
        if (downloadBaseDataMessageEvent == null) {
            downloadBaseDataMessageEvent = new DownloadBaseDataMessageEvent();
            downloadBaseDataMessageEvent.setId(eventID);
        }
        if (downloadBaseDataMessageBO == null) {
            downloadBaseDataMessageBO = new DownloadBaseDataMessageBO(GlobalController.getInstance().getContext(), downloadBaseDataMessageEvent, null);
        }
        downloadBaseDataMessageEvent.setSqliteBO(downloadBaseDataMessageBO);
        //
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(eventID);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(eventID);
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
        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(eventID);
        }
        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(eventID);
        }
        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
    }

    public void ResetBaseData(boolean isPostDetail) throws Exception {
        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodesSQLiteBO.getSqLiteEvent().setPageIndex(Barcodes.PAGEINDEX_START);
        retrieveNCBaseModel(barcodes, barcodesHttpBO, barcodesSQLiteEvent, isPostDetail);
        //
        Brand brand = new Brand();
        brand.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        brand.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        retrieveNCBaseModel(brand, brandHttpBO, brandSQLiteEvent, isPostDetail);
        //
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commodityCategory.setPageIndex(BaseHttpBO.PAGE_SIZE_LoadAll);
        retrieveNCBaseModel(commodityCategory, commodityCategoryHttpBO, commodityCategorySQLiteEvent, isPostDetail);
        //
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        retrieveNCBaseModel(packageUnit, packageUnitHttpBO, packageUnitSQLiteEvent, isPostDetail);
//        //
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);//...
        retrieveNCBaseModel(commodity, commodityHttpBO, commoditySQLiteEvent, isPostDetail);
        //
        Promotion promotion = new Promotion();
        promotion.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        promotion.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        promotion.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
        promotion.setSubStatusOfStatus(Promotion.EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
        retrieveNCBaseModel(promotion, promotionHttpBO, promotionSQLiteBO.getSqLiteEvent(), isPostDetail);
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        retrieveNCBaseModel(configGeneral, configGeneralHttpBO, configGeneralSQLiteEvent, isPostDetail);
        //
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        smallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retrieveNCBaseModel(smallSheetFrame, smallSheetHttpBO, smallSheetSQLiteEvent, isPostDetail);
        //
        Coupon coupon = new Coupon();
        coupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        coupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        couponSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Coupon_RefreshByServerDataAsyncC);
        couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retrieveNCBaseModel(coupon, couponHttpBO, couponSQLiteEvent, isPostDetail);
    }

    private void retrieveNCBaseModel(BaseModel bm, BaseHttpBO baseHttpBO, BaseSQLiteEvent baseSQLiteEvent, boolean isPostDetail) throws Exception {
        String bmName = bm.getClass().getSimpleName();
        message = "正在下载" + bmName + "中，请稍后...\n";
        log.info(message);
        if (isPostDetail) {
            postDownloadDetail(message);
        }
        runTimes = 1;
        do {
            baseSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            if (!baseHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, bm)) {
                message = "下载" + bmName + "失败。\n";
                if (isPostDetail) {
                    postDownloadDetail(message);
                }
                break;
            }
            long lTimeOut = UNIT_TimeOut;
            while (baseSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (baseSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                message = "错误：" + bmName + "下载超时。\n";
                if (isPostDetail) {
                    postDownloadDetail(message);
                }
                break;
            }
            if (baseSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                message = "错误：" + bmName + "下载失败，失败原因：" + baseHttpBO.getHttpEvent().getLastErrorMessage() + "\n";
                if (isPostDetail) {
                    postDownloadDetail(message);
                }
                break;
            }
            //
            if (baseHttpBO.getHttpEvent().getCount() != null && !"".equals(baseHttpBO.getHttpEvent().getCount())) {
                count = Integer.valueOf(baseHttpBO.getHttpEvent().getCount());
                int totalPageIndex = count % Integer.valueOf(bm.getPageSize()) != 0 ? count / Integer.valueOf(bm.getPageSize()) + 1 : count / Integer.valueOf(bm.getPageSize());//查询条形码需要totalPageIndex页才能查完

                if (runTimes < totalPageIndex) {
                    bm.setPageIndex(String.valueOf(++runTimes));
                    // 判断是否为Commodity并且是页数中的最后一页。(为了在present中删除并更新掉z最后一页的所有数据)
                    if (bm instanceof Commodity && bm.getPageIndex().equals(String.valueOf(totalPageIndex))) {
                        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_END);
                    } else if (bm instanceof Barcodes && bm.getPageIndex().equals(String.valueOf(totalPageIndex))) {
                        barcodesSQLiteBO.getSqLiteEvent().setPageIndex(Barcodes.PAGEINDEX_END);
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        } while (true);
        message = "已完成对" + bmName + "的下载...\n";
        log.info(message);
        if (isPostDetail) {
            postDownloadDetail(message);
        }
    }

    private void postDownloadDetail(String message) throws InterruptedException {
        downloadBaseDataMessageEvent.setMessage(message);
        downloadBaseDataMessageEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        downloadBaseDataMessageBO.postEvent(downloadBaseDataMessageEvent);
        long aa = 10;
        while (downloadBaseDataMessageEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                downloadBaseDataMessageEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && aa-- > 0) {
            Thread.sleep(1000);
        }
        if (downloadBaseDataMessageEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                downloadBaseDataMessageEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            log.info("超时超时超时超时超时超时超时超时~~~~~~~~~~");
        }
    }
}
