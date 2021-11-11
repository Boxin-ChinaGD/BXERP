package wpos.utils;

import org.springframework.stereotype.Component;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.BarcodesSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.BrandSQLiteEvent;
import wpos.event.UI.CommodityCategorySQLiteEvent;
import wpos.event.UI.CommoditySQLiteEvent;
import wpos.event.UI.ConfigGeneralSQLiteEvent;
import wpos.event.UI.DownloadBaseDataMessageEvent;
import wpos.event.UI.PackageUnitSQLiteEvent;
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.model.*;
//import wpos.model.Promotion;
import wpos.model.promotion.Promotion;

import javax.annotation.Resource;

//import org.apache.log4j.Logger;

@Component("resetBaseDataUtil")
public class ResetBaseDataUtil {
//    private Log log = LogFactory.getLog(this.getClass());

    public ResetBaseDataUtil() {
//        initObject();
    }

    public ResetBaseDataUtil(int stageEventID) {
        eventID = stageEventID;
        initObject();
    }

    public static int eventID; //标明是哪个Stage的EventID

    public static void setEventID(int eventID) {
        ResetBaseDataUtil.eventID = eventID;
    }

    public String message = ""; //下载资料时的信息
    private int count;//call 普通Action的RN，返回总数
    private int runTimes = 1;//需要运行runTimes次，才能把条形码全部重置下来
    private final int UNIT_TimeOut = 600;

    @Resource
    private BarcodesSQLiteBO barcodesSQLiteBO;
    @Resource
    private BarcodesHttpBO barcodesHttpBO;
    @Resource
    private BarcodesSQLiteEvent barcodesSQLiteEvent;
    @Resource
    private BarcodesHttpEvent barcodesHttpEvent;
    //
    @Resource
    private BrandSQLiteBO brandSQLiteBO;
    @Resource
    private BrandHttpBO brandHttpBO;
    @Resource
    private BrandSQLiteEvent brandSQLiteEvent;
    @Resource
    private BrandHttpEvent brandHttpEvent;
    //
    @Resource
    private CommodityCategorySQLiteBO commodityCategorySQLiteBO;
    @Resource
    private CommodityCategoryHttpBO commodityCategoryHttpBO;
    @Resource
    private CommodityCategorySQLiteEvent commodityCategorySQLiteEvent;
    @Resource
    private CommodityCategoryHttpEvent commodityCategoryHttpEvent;
    //
    @Resource
    private PackageUnitSQLiteBO packageUnitSQLiteBO;
    @Resource
    private PackageUnitHttpBO packageUnitHttpBO;
    @Resource
    private PackageUnitSQLiteEvent packageUnitSQLiteEvent;
    @Resource
    private PackageUnitHttpEvent packageUnitHttpEvent;
    //
    @Resource
    private CommoditySQLiteBO commoditySQLiteBO;
    @Resource
    private CommodityHttpBO commodityHttpBO;
    @Resource
    private CommoditySQLiteEvent commoditySQLiteEvent;
    @Resource
    private CommodityHttpEvent commodityHttpEvent;
    //
    @Resource
    private PromotionHttpEvent promotionHttpEvent;
    @Resource
    private PromotionSQLiteEvent promotionSQLiteEvent;
    @Resource
    private PromotionHttpBO promotionHttpBO;
    @Resource
    private PromotionSQLiteBO promotionSQLiteBO;
    //
    @Resource
    private ConfigCacheSizeSQLiteBO configCacheSizeSQLiteBO;
    @Resource
    private ConfigCacheSizeHttpBO configCacheSizeHttpBO;
    @Resource
    private ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent;
    @Resource
    private ConfigCacheSizeHttpEvent configCacheSizeHttpEvent;
    //
    @Resource
    private ConfigGeneralSQLiteBO configGeneralSQLiteBO;
    @Resource
    private ConfigGeneralHttpBO configGeneralHttpBO;
    @Resource
    private ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    @Resource
    private ConfigGeneralHttpEvent configGeneralHttpEvent;
    //
    @Resource
    private DownloadBaseDataMessageBO downloadBaseDataMessageBO;
    @Resource
    private DownloadBaseDataMessageEvent downloadBaseDataMessageEvent;
    //
    @Resource
    public SmallSheetSQLiteBO smallSheetSQLiteBO;
    @Resource
    public SmallSheetHttpBO smallSheetHttpBO;
    @Resource
    public SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    @Resource
    public SmallSheetHttpEvent smallSheetHttpEvent;
    @Resource
    public CouponSQLiteBO couponSQLiteBO;
    @Resource
    public CouponSQLiteEvent couponSQLiteEvent;
    @Resource
    public CouponHttpEvent couponHttpEvent;
    @Resource
    public CouponHttpBO couponHttpBO;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void initObject() {
        barcodesSQLiteEvent.setId(eventID);
        barcodesHttpEvent.setId(eventID);
        barcodesHttpBO.setHttpEvent(barcodesHttpEvent);
        barcodesHttpBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesSQLiteBO.setHttpEvent(barcodesHttpEvent);
        barcodesSQLiteBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        //
        brandSQLiteEvent.setId(eventID);
        brandHttpEvent.setId(eventID);
        brandHttpBO.setHttpEvent(brandHttpEvent);
        brandHttpBO.setSqLiteEvent(brandSQLiteEvent);
        brandSQLiteBO.setHttpEvent(brandHttpEvent);
        brandSQLiteBO.setSqLiteEvent(brandSQLiteEvent);
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);
        //
        commodityCategorySQLiteEvent.setId(eventID);
        commodityCategoryHttpEvent.setId(eventID);
        commodityCategorySQLiteBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategorySQLiteBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategoryHttpBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategoryHttpBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
        //
        packageUnitSQLiteEvent.setId(eventID);
        packageUnitHttpEvent.setId(eventID);
        packageUnitSQLiteBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitSQLiteBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitHttpBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitHttpBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
        //
        commoditySQLiteEvent.setId(eventID);
        commodityHttpEvent.setId(eventID);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        //
        promotionSQLiteEvent.setId(eventID);
        promotionHttpEvent.setId(eventID);
        promotionSQLiteBO.setHttpEvent(promotionHttpEvent);
        promotionSQLiteBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionHttpBO.setHttpEvent(promotionHttpEvent);
        promotionHttpBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        //
        configCacheSizeSQLiteEvent.setId(eventID);
        configCacheSizeHttpEvent.setId(eventID);
        configCacheSizeSQLiteBO.setHttpEvent(configCacheSizeHttpEvent);
        configCacheSizeSQLiteBO.setSqLiteEvent(configCacheSizeSQLiteEvent);
        configCacheSizeHttpBO.setHttpEvent(configCacheSizeHttpEvent);
        configCacheSizeHttpBO.setSqLiteEvent(configCacheSizeSQLiteEvent);
        configCacheSizeSQLiteEvent.setSqliteBO(configCacheSizeSQLiteBO);
        configCacheSizeSQLiteEvent.setHttpBO(configCacheSizeHttpBO);
        configCacheSizeHttpEvent.setSqliteBO(configCacheSizeSQLiteBO);
        configCacheSizeHttpEvent.setHttpBO(configCacheSizeHttpBO);
        //
        configGeneralSQLiteEvent.setId(eventID);
        configGeneralHttpEvent.setId(eventID);
        configGeneralSQLiteBO.setHttpEvent(configGeneralHttpEvent);
        configGeneralSQLiteBO.setSqLiteEvent(configGeneralSQLiteEvent);
        configGeneralHttpBO.setHttpEvent(configGeneralHttpEvent);
        configGeneralHttpBO.setSqLiteEvent(configGeneralSQLiteEvent);
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
        //
        downloadBaseDataMessageEvent.setId(eventID);
        downloadBaseDataMessageBO.setSqLiteEvent(downloadBaseDataMessageEvent);
        downloadBaseDataMessageEvent.setSqliteBO(downloadBaseDataMessageBO);
        //
        smallSheetSQLiteEvent.setId(eventID);
        smallSheetHttpEvent.setId(eventID);
        smallSheetSQLiteBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetSQLiteBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetHttpBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetHttpBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        //
        couponSQLiteEvent.setId(eventID);
        couponHttpEvent.setId(eventID);
        couponHttpBO.setHttpEvent(couponHttpEvent);
        couponHttpBO.setSqLiteEvent(couponSQLiteEvent);
        couponSQLiteBO.setHttpEvent(couponHttpEvent);
        couponSQLiteBO.setSqLiteEvent(couponSQLiteEvent);
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
    }

    public boolean ResetBaseData(boolean isPostDetail) throws Exception {
        Barcodes barcodes = new Barcodes();
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodesSQLiteBO.getSqLiteEvent().setPageIndex(Barcodes.PAGEINDEX_START);
        if(!retrieveNCBaseModel(barcodes, barcodesHttpBO, barcodesSQLiteEvent, isPostDetail)) {
            return false;
        }
        //
        Brand brand = new Brand();
        brand.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        brand.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if(!retrieveNCBaseModel(brand, brandHttpBO, brandSQLiteEvent, isPostDetail)) {
            return false;
        }
        //
        CommodityCategory commodityCategory = new CommodityCategory();
        commodityCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commodityCategory.setPageIndex(BaseHttpBO.PAGE_SIZE_LoadAll);
        if(!retrieveNCBaseModel(commodityCategory, commodityCategoryHttpBO, commodityCategorySQLiteEvent, isPostDetail)) {
            return false;
        }
        //
        PackageUnit packageUnit = new PackageUnit();
        packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        if(!retrieveNCBaseModel(packageUnit, packageUnitHttpBO, packageUnitSQLiteEvent, isPostDetail)) {
            return false;
        }
//        //
        Commodity commodity = new Commodity();
        commodity.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        commodity.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        commoditySQLiteBO.getSqLiteEvent().setPageIndex(Commodity.PAGEINDEX_START);//...
        if(!retrieveNCBaseModel(commodity, commodityHttpBO, commoditySQLiteEvent, isPostDetail)) {
            return false;
        }
        //
        Promotion promotion = new Promotion();
        promotion.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        promotion.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        promotion.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
        promotion.setSubStatusOfStatus(Promotion.EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
        if(!retrieveNCBaseModel(promotion, promotionHttpBO, promotionSQLiteBO.getSqLiteEvent(), isPostDetail)) {
            return false;
        }
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        if(!retrieveNCBaseModel(configGeneral, configGeneralHttpBO, configGeneralSQLiteEvent, isPostDetail)) {
            return false;
        }
        //
//        不需要同步这个
//        ConfigCacheSize configCacheSize = new ConfigCacheSize();
//        retrieveNCBaseModel(configCacheSize, configCacheSizeHttpBO, configCacheSizeSQLiteEvent, isPostDetail);
        //
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        smallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if(!retrieveNCBaseModel(smallSheetFrame, smallSheetHttpBO, smallSheetSQLiteEvent, isPostDetail)) {
            return false;
        }
        //
        Coupon coupon = new Coupon();
        coupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        coupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        couponSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Coupon_RefreshByServerDataAsyncC);
        couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if(!retrieveNCBaseModel(coupon, couponHttpBO, couponSQLiteEvent, isPostDetail)) {
            return false;
        }
        return true;
    }

    private boolean retrieveNCBaseModel(BaseModel bm, BaseHttpBO baseHttpBO, BaseSQLiteEvent baseSQLiteEvent, boolean isPostDetail) throws Exception {
        String bmName = bm.getClass().getSimpleName();
        message = "正在下载" + bmName + "中，请稍后...\n";
//        log.info(message);
        if (isPostDetail) {
            postDownloadDetail(message);
        }
        runTimes = 1;
        do {
            baseSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            if (!baseHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, bm)) {
                message = "下载" + bmName + "失败。\n";
                if(baseHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                    return false;
                }
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
                    }else if(bm instanceof Barcodes && bm.getPageIndex().equals(String.valueOf(totalPageIndex))){
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
//        log.info(message);
        if (isPostDetail) {
            postDownloadDetail(message);
        }
        return true;
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
//            log.info("超时超时超时超时超时超时超时超时~~~~~~~~~~");
        }
    }
}
