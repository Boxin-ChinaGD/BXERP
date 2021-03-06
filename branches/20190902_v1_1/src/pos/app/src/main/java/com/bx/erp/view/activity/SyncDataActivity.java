//package com.bx.erp.view.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.R;
//import com.bx.erp.bo.BXConfigGeneralHttpBO;
//import com.bx.erp.bo.BXConfigGeneralSQLiteBO;
//import com.bx.erp.bo.BarcodesHttpBO;
//import com.bx.erp.bo.BarcodesSQLiteBO;
//import com.bx.erp.bo.BaseHttpBO;
//import com.bx.erp.bo.BaseSQLiteBO;
//import com.bx.erp.bo.BrandHttpBO;
//import com.bx.erp.bo.BrandSQLiteBO;
//import com.bx.erp.bo.CommodityCategoryHttpBO;
//import com.bx.erp.bo.CommodityCategorySQLiteBO;
//import com.bx.erp.bo.CommodityHttpBO;
//import com.bx.erp.bo.CommoditySQLiteBO;
//import com.bx.erp.bo.ConfigGeneralHttpBO;
//import com.bx.erp.bo.ConfigGeneralSQLiteBO;
//import com.bx.erp.bo.NtpHttpBO;
//import com.bx.erp.bo.PackageUnitHttpBO;
//import com.bx.erp.bo.PackageUnitSQLiteBO;
//import com.bx.erp.bo.PosHttpBO;
//import com.bx.erp.bo.PosSQLiteBO;
//import com.bx.erp.bo.PromotionHttpBO;
//import com.bx.erp.bo.PromotionSQLiteBO;
//import com.bx.erp.bo.RetailTradeAggregationHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
//import com.bx.erp.bo.RetailTradeHttpBO;
//import com.bx.erp.bo.RetailTradeSQLiteBO;
//import com.bx.erp.bo.SmallSheetHttpBO;
//import com.bx.erp.bo.SmallSheetSQLiteBO;
//import com.bx.erp.bo.StaffHttpBO;
//import com.bx.erp.bo.StaffSQLiteBO;
//import com.bx.erp.bo.VipCategoryHttpBO;
//import com.bx.erp.bo.VipCategorySQLiteBO;
//import com.bx.erp.bo.VipHttpBO;
//import com.bx.erp.bo.VipSQLiteBO;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BXConfigGeneralHttpEvent;
//import com.bx.erp.event.BarcodesHttpEvent;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.BaseHttpEvent;
//import com.bx.erp.event.BrandHttpEvent;
//import com.bx.erp.event.CommodityCategoryHttpEvent;
//import com.bx.erp.event.CommodityHttpEvent;
//import com.bx.erp.event.ConfigGeneralHttpEvent;
//import com.bx.erp.event.NtpHttpEvent;
//import com.bx.erp.event.PackageUnitHttpEvent;
//import com.bx.erp.event.PosHttpEvent;
//import com.bx.erp.event.PosSQLiteEvent;
//import com.bx.erp.event.PromotionHttpEvent;
//import com.bx.erp.event.PromotionSQLiteEvent;
//import com.bx.erp.event.RetailTradeAggregationHttpEvent;
//import com.bx.erp.event.RetailTradeHttpEvent;
//import com.bx.erp.event.SmallSheetHttpEvent;
//import com.bx.erp.event.StaffHttpEvent;
//import com.bx.erp.event.UI.BXConfigGeneralSQLiteEvent;
//import com.bx.erp.event.UI.BarcodesSQLiteEvent;
//import com.bx.erp.event.UI.BaseSQLiteEvent;
//import com.bx.erp.event.UI.BrandSQLiteEvent;
//import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
//import com.bx.erp.event.UI.CommoditySQLiteEvent;
//import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
//import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
//import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
//import com.bx.erp.event.UI.StaffSQLiteEvent;
//import com.bx.erp.event.UI.VipCategorySQLiteEvent;
//import com.bx.erp.event.UI.VipSQLiteEvent;
//import com.bx.erp.event.VipCategoryHttpEvent;
//import com.bx.erp.event.VipHttpEvent;
//import com.bx.erp.http.HttpRequestUnit;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.Ntp;
//import com.bx.erp.model.PackageUnit;
//import com.bx.erp.model.RetailTrade;
//import com.bx.erp.model.RetailTradeAggregation;
//import com.bx.erp.model.SmallSheetFrame;
//import com.bx.erp.model.SmallSheetText;
//import com.bx.erp.presenter.BasePresenter;
//import com.bx.erp.presenter.RetailTradeAggregationPresenter;
//import com.bx.erp.presenter.SmallSheetFramePresenter;
//import com.bx.erp.presenter.SmallSheetTextPresenter;
//import com.bx.erp.utils.NetworkUtils;
//import com.bx.erp.view.component.SyncLoadingProgress;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * ???????????????????????????????????????????????????->?????????????????????????????????->???????????????????????????->???????????????->?????????????????????????????????POS????????????????????????????????????????????????
// */
//public class SyncDataActivity extends BaseActivity {
//    private Logger log = Logger.getLogger(this.getClass());
//    @BindView(R.id.sync_progress)
//    SyncLoadingProgress syncLoadingProgress;
//    @BindView(R.id.show_details)
//    TextView showDetails;
//
//    private static final String BarcodesData = "barcodes";
//    private static final String BrandData = "brand";
//    private static final String CommodityCategoryData = "category";
//    private static final String PackageUnitData = "packageUnit";
//    private static final String CommodityData = "commodity";
//    private static final String PromotionData = "promotion";
//    private static final String PosData = "pos";
//    private static final String SmallSheetData = "smallSheet";
//    private static final String VipCategoryData = "vipCategory";
//    private static final String VipData = "vip";
//    private static final String ConfigGeneralData = "configGeneral";
//
//    private static NetworkUtils networkUtils = new NetworkUtils();
//
//    private List<SmallSheetFrame> tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();//????????????POS???????????????SQLite????????????????????????SmallSheet??????
//    private List<RetailTrade> tempRetailTradeList = new ArrayList<RetailTrade>();//????????????POS???????????????SQLite????????????????????????RetailTrade??????
//    private List<RetailTradeAggregation> tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();//????????????POS???????????????SQLite????????????????????????RetailTradeAggregation??????
//
//    private String syncBarcodesIDs = "";//???????????????Barcodes???ID?????????feedback?????????    ??????????????????????????????????????????
//    private String syncBrandIDs = "";
//    private String syncCommodityCategoryIDs = "";
//    private String syncCommodityIDs = "";
//    private String syncPromotionIDs = "";
//    private String syncPosIDs = "";
//    private String syncSmallSheetIDs = "";
//    private String syncVipCategoryIDs = "";
//    private String syncVipIDs = "";
//
//    private long lTimeOut = 50;
//
//    private SmallSheetFramePresenter smallSheetFramePresenter;
//    private SmallSheetTextPresenter smallSheetTextPresenter;
//
//    private static SmallSheetHttpBO smallSheetHttpBO = null;
//    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
//    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
//    private static SmallSheetHttpEvent smallSheetHttpEvent = null;
//    //
//    private static RetailTradeHttpBO retailTradeHttpBO = null;
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
//    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
//    //
//    private static NtpHttpEvent ntpHttpEvent = null;
//    private static NtpHttpBO ntpHttpBO = null;
//    //
//    private static PackageUnitSQLiteBO packageUnitSQLiteBO = null;
//    private static PackageUnitHttpBO packageUnitHttpBO = null;
//    private static PackageUnitSQLiteEvent packageUnitSQLiteEvent = null;
//    private static PackageUnitHttpEvent packageUnitHttpEvent = null;
//    //
//    private static BarcodesSQLiteBO barcodesSQLiteBO = null;
//    private static BarcodesHttpBO barcodesHttpBO = null;
//    private static BarcodesSQLiteEvent barcodesSQLiteEvent = null;
//    private static BarcodesHttpEvent barcodesHttpEvent = null;
//    //
//    private static BrandSQLiteBO brandSQLiteBO = null;
//    private static BrandHttpBO brandHttpBO = null;
//    private static BrandSQLiteEvent brandSQLiteEvent = null;
//    private static BrandHttpEvent brandHttpEvent = null;
//    //
//    private static CommodityCategorySQLiteBO commodityCategorySQLiteBO = null;
//    private static CommodityCategoryHttpBO commodityCategoryHttpBO = null;
//    private static CommodityCategorySQLiteEvent commodityCategorySQLiteEvent = null;
//    private static CommodityCategoryHttpEvent commodityCategoryHttpEvent = null;
//    //
//    private static CommoditySQLiteBO commoditySQLiteBO = null;
//    private static CommodityHttpBO commodityHttpBO = null;
//    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
//    private static CommodityHttpEvent commodityHttpEvent = null;
//    //
//    private static PosSQLiteBO posSQLiteBO = null;
//    private static PosHttpBO posHttpBO = null;
//    private static PosSQLiteEvent posSQLiteEvent = null;
//    private static PosHttpEvent posHttpEvent = null;
//    //
//    private static VipCategorySQLiteBO vipCategorySQLiteBO = null;
//    private static VipCategoryHttpBO vipCategoryHttpBO = null;
//    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = null;
//    private static VipCategoryHttpEvent vipCategoryHttpEvent = null;
//    //
//    private static VipSQLiteBO vipSQLiteBO = null;
//    private static VipHttpBO vipHttpBO = null;
//    private static VipSQLiteEvent vipSQLiteEvent = null;
//    private static VipHttpEvent viHttpEvent = null;
//    //
//    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
//    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
//    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
//    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;
//    //
//    private static BXConfigGeneralSQLiteBO bxConfigGeneralSQLiteBO = null;
//    private static BXConfigGeneralHttpBO bxConfigGeneralHttpBO = null;
//    private static BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent = null;
//    private static BXConfigGeneralHttpEvent bxConfigGeneralHttpEvent = null;
//    //
////    private ConfigCacheSizeHttpBO configCacheSizeHttpBO;
////    private ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent;
//    //
//    private static PromotionSQLiteBO promotionSQLiteBO = null;
//    private static PromotionHttpBO promotionHttpBO = null;
//    private static PromotionSQLiteEvent promotionSQLiteEvent = null;
//    private static PromotionHttpEvent promotionHttpEvent = null;
//    //
//    private static RetailTradeAggregationPresenter retailTradeAggregationPresenter = null;
//    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
//    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
//    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
//    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
//    //
//    private static StaffSQLiteBO staffSQLiteBO = null;
//    private static StaffHttpBO staffHttpBO = null;
//    private static StaffSQLiteEvent staffSQLiteEvent = null;
//    private static StaffHttpEvent staffHttpEvent = null;
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sync_data);
//        ButterKnife.bind(this);
//
//        syncLoadingProgress = (SyncLoadingProgress) findViewById(R.id.sync_progress);
//
//        initObject();
//        syncData();
//
////        // ?????????????????????????????????resume????????????
////        if (GlobalController.getInstance().getSessionID() != null) {
////            SyncThread.aiPause.set(SyncThread.RESUME);
////            log.info("??????????????????????????????????????????SQLite???????????????null???????????????????????????resume");
////        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if(event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Create  // ???????????????????????????????????????(C/U/D)
//                        || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Update
//                        || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Delete){
//                    if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) { // ?????????????????????????????????????????????
//                        tempSmallSheetFrameList.remove(0);
//                        if (tempSmallSheetFrameList.size() == 0) {
//                            //??????RetailTrade
//                            if (tempRetailTradeList != null) {
//                                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//                                if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
//                                    log.info("??????????????????????????????");
//                                }
//                            }
//                        } else {
//                            //??????tempSmallSheetFrameList??????????????????
//                            uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
//                        }
//                    } else {
//                        log.debug("??????????????????????????????????????????" + event.getBaseModel1());
//                    }
//                } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_RetrieveN) {
//                    log.debug("ERT_SmallSheet_RetrieveN????????????");
//                } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) { // ????????????
//                    afterFeedback(event, VipCategoryData);
//                } else {
//                    log.error("??????????????????,event=" + event);
//                }
//            } else {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            //????????????SmallSheet????????????
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done || event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done) {
//                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                    event.setEventTypeSQLite(null);
//                    tempSmallSheetFrameList.remove(0);
//                    if (tempSmallSheetFrameList.size() == 0) {
//                        //??????RetailTrade
//                        if (tempRetailTradeList != null) {
//                            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//                            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
//                                log.info("??????????????????????????????");
//                            }
//                        }
//                    } else {
//                        //??????tempSmallSheetFrameList??????????????????
//                        uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
//                    }
//                }
//            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {//????????????SmallSheet RetrieveN??????
//                beforeFeedback(event, syncSmallSheetIDs, SmallSheetData, VipCategoryData);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("???????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("SyncDataActivity.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
//            }
//            log.debug("???Event?????????SyncThread??????????????????SyncDataActivity.onRetailTradeHttpEvent()????????????");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            log.info("#########################################################SyncDataActivity onRetailTradeSQLiteEvent");
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                event.setEventTypeSQLite(null);
//                if (tempRetailTradeAggregationList.size() > 0) {
//                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
//                } else {
//                    syncTime();
//                }
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("SyncDataActivity.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
//            }
//            log.debug("???Event?????????SyncThread??????????????????SyncDataActivity.onRetailTradeSQLiteEvent()????????????");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            log.info("????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//                tempRetailTradeAggregationList.remove(0);
//                //
//                if (tempRetailTradeAggregationList.size() > 0) {
//                    //????????????????????????
//                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
//                } else {
//                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);//??????????????????????????????????????????????????????????????????????????????????????????????????????
//                    syncTime();
//                }
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onNtpHttpEvent(NtpHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_NtpSync) {
//                    event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//                    event.setRequestType(null);
//                    Ntp ntp = (Ntp) event.getBaseModel1();
//                    NtpHttpBO.TimeDifference = ((ntp.getT2() - ntp.getT1()) + (ntp.getT3() - ntp.getT4())) / 2;
//                    //
//                    PackageUnit packageUnit = new PackageUnit();
//                    packageUnit.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//                    packageUnit.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
//                    retrieveNCPackageUnit(packageUnit);
//                }
//            } else {
//                NtpHttpBO.TimeDifference = 0;
//                log.info("??????NTP????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("MainActivity.onRetailTradeSQLiteEvent1()?????????????????????????????????SyncThread????????????");
//            }
//            log.debug("???Event?????????SyncThread??????????????????MainActivity.onRetailTradeSQLiteEvent1()????????????");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                //??????Commodity
//                retrieveNBarcodes();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, BrandData);
//            } else {
//                log.info("???????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncBarcodesIDs, BarcodesData, BrandData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBrandHttpEvent(BrandHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, CommodityCategoryData);
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncBrandIDs, BrandData, CommodityCategoryData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, CommodityData);
//            } else {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncCommodityCategoryIDs, CommodityCategoryData, CommodityData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityHttpEvent(CommodityHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, PromotionData);
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncCommodityIDs, CommodityData, PromotionData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPromotionHttpEvent(PromotionHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, PosData);
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncPromotionIDs, PromotionData, PosData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPosHttpEvent(PosHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, SmallSheetData);
//            } else {
//                log.info("??????POS??????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPosSQLiteEvent(PosSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncPosIDs, PosData, SmallSheetData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onVipCategoryHttpEvent(VipCategoryHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
////                afterFeedback(event, VipData);
//                afterFeedback(event, ConfigGeneralData);
//                //
//            } else {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//
//    public void onVipCategorySQLiteEvent(VipCategorySQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncVipCategoryIDs, VipCategoryData, ConfigGeneralData);
////            beforeFeedback(event, syncVipCategoryIDs, VipCategoryData, VipData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onVipHttpEvent(VipHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//                afterFeedback(event, ConfigGeneralData);
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onVipSQLiteEvent(VipSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            beforeFeedback(event, syncVipIDs, VipData, ConfigGeneralData);
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("??????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                retrieveNBXConfigGeneral();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBXConfigGeneralHttpEvent(BXConfigGeneralHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBXConfigGeneralSQLiteEvent(BXConfigGeneralSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                //???????????????????????????
//                retrieveResigned();
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
////    @Subscribe(threadMode = ThreadMode.ASYNC)
////    public void onConfigCacheSizeHttpEvent(ConfigCacheSizeHttpEvent event) {
////        event.onEvent();
////    }
////
////    @Subscribe(threadMode = ThreadMode.ASYNC)
////    public void onConfigCacheSizeSQLiteEvent(ConfigCacheSizeSQLiteEvent event) {
////        event.onEvent();
////        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
////            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
////            //???????????????????????????
////            retrieveResigned();
////        }
////    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onStaffHttpEvent(StaffHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("????????????????????????????????????????????????????????????????????????");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataActivity) {
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                //?????????MainActivity
//                Intent intent = new Intent(SyncDataActivity.this, MainActivity.class);
//                SyncDataActivity.this.finish();
//                BaseActivity.lastRetailTradeAmount = 0.000000d;
//                BaseActivity.lastRetailTradeChangeMoney = 0.000000d;
//                BaseActivity.lastRetailTradePaymentType = "";
//                startActivity(intent);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    /**
//     * ???????????????
//     */
//    private void initObject() {
//        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//
//        if (smallSheetSQLiteEvent == null) {
//            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
//            smallSheetSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (smallSheetHttpEvent == null) {
//            smallSheetHttpEvent = new SmallSheetHttpEvent();
//            smallSheetHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (smallSheetHttpBO == null) {
//            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
//        }
//        if (smallSheetSQLiteBO == null) {
//            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
//        }
//        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
//        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
//        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
//        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
//        //
//        if (retailTradeSQLiteEvent == null) {
//            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (retailTradeHttpEvent == null) {
//            retailTradeHttpEvent = new RetailTradeHttpEvent();
//            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (retailTradeHttpBO == null) {
//            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        if (retailTradeSQLiteBO == null) {
//            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
//        //
//        if (ntpHttpEvent == null) {
//            ntpHttpEvent = new NtpHttpEvent();
//            ntpHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (ntpHttpBO == null) {
//            ntpHttpBO = new NtpHttpBO(GlobalController.getInstance().getContext(), ntpHttpEvent);
//        }
//        ntpHttpEvent.setHttpBO(ntpHttpBO);
//        //
//        if (packageUnitSQLiteEvent == null) {
//            packageUnitSQLiteEvent = new PackageUnitSQLiteEvent();
//            packageUnitSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (packageUnitHttpEvent == null) {
//            packageUnitHttpEvent = new PackageUnitHttpEvent();
//            packageUnitHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (packageUnitSQLiteBO == null) {
//            packageUnitSQLiteBO = new PackageUnitSQLiteBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
//        }
//        if (packageUnitHttpBO == null) {
//            packageUnitHttpBO = new PackageUnitHttpBO(GlobalController.getInstance().getContext(), packageUnitSQLiteEvent, packageUnitHttpEvent);
//        }
//        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
//        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
//        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);
//        //
//        if (barcodesSQLiteEvent == null) {
//            barcodesSQLiteEvent = new BarcodesSQLiteEvent();
//            barcodesSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (barcodesHttpEvent == null) {
//            barcodesHttpEvent = new BarcodesHttpEvent();
//            barcodesHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (barcodesSQLiteBO == null) {
//            barcodesSQLiteBO = new BarcodesSQLiteBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
//        }
//        if (barcodesHttpBO == null) {
//            barcodesHttpBO = new BarcodesHttpBO(GlobalController.getInstance().getContext(), barcodesSQLiteEvent, barcodesHttpEvent);
//        }
//        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
//        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
//        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
//        barcodesHttpEvent.setHttpBO(barcodesHttpBO);
//        //
//        if (brandSQLiteEvent == null) {
//            brandSQLiteEvent = new BrandSQLiteEvent();
//            brandSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (brandHttpEvent == null) {
//            brandHttpEvent = new BrandHttpEvent();
//            brandHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (brandSQLiteBO == null) {
//            brandSQLiteBO = new BrandSQLiteBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
//        }
//        if (brandHttpBO == null) {
//            brandHttpBO = new BrandHttpBO(GlobalController.getInstance().getContext(), brandSQLiteEvent, brandHttpEvent);
//        }
//        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
//        brandSQLiteEvent.setHttpBO(brandHttpBO);
//        brandHttpEvent.setSqliteBO(brandSQLiteBO);
//        brandHttpEvent.setHttpBO(brandHttpBO);
//        //
//        if (commodityCategorySQLiteEvent == null) {
//            commodityCategorySQLiteEvent = new CommodityCategorySQLiteEvent();
//            commodityCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (commodityCategoryHttpEvent == null) {
//            commodityCategoryHttpEvent = new CommodityCategoryHttpEvent();
//            commodityCategoryHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (commodityCategorySQLiteBO == null) {
//            commodityCategorySQLiteBO = new CommodityCategorySQLiteBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        }
//        if (commodityCategoryHttpBO == null) {
//            commodityCategoryHttpBO = new CommodityCategoryHttpBO(GlobalController.getInstance().getContext(), commodityCategorySQLiteEvent, commodityCategoryHttpEvent);
//        }
//        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
//        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
//        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
//        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);
//        //
//        if (commoditySQLiteEvent == null) {
//            commoditySQLiteEvent = new CommoditySQLiteEvent();
//            commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (commodityHttpEvent == null) {
//            commodityHttpEvent = new CommodityHttpEvent();
//            commodityHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (commoditySQLiteBO == null) {
//            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        if (commodityHttpBO == null) {
//            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
//        }
//        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
//        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
//        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
//        commodityHttpEvent.setHttpBO(commodityHttpBO);
//        //
//        if (posSQLiteEvent == null) {
//            posSQLiteEvent = new PosSQLiteEvent();
//            posSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (posHttpEvent == null) {
//            posHttpEvent = new PosHttpEvent();
//            posHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (posSQLiteBO == null) {
//            posSQLiteBO = new PosSQLiteBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
//        }
//        if (posHttpBO == null) {
//            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
//        }
//        posSQLiteEvent.setSqliteBO(posSQLiteBO);
//        posSQLiteEvent.setHttpBO(posHttpBO);
//        posHttpEvent.setSqliteBO(posSQLiteBO);
//        posHttpEvent.setHttpBO(posHttpBO);
//        //
//        if (vipCategorySQLiteEvent == null) {
//            vipCategorySQLiteEvent = new VipCategorySQLiteEvent();
//            vipCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (vipCategoryHttpEvent == null) {
//            vipCategoryHttpEvent = new VipCategoryHttpEvent();
//            vipCategoryHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (vipCategorySQLiteBO == null) {
//            vipCategorySQLiteBO = new VipCategorySQLiteBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        }
//        if (vipCategoryHttpBO == null) {
//            vipCategoryHttpBO = new VipCategoryHttpBO(GlobalController.getInstance().getContext(), vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        }
//        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
//        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
//        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
//        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);
//        //
//        if (vipSQLiteEvent == null) {
//            vipSQLiteEvent = new VipSQLiteEvent();
//            vipSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (viHttpEvent == null) {
//            viHttpEvent = new VipHttpEvent();
//            viHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (vipSQLiteBO == null) {
//            vipSQLiteBO = new VipSQLiteBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, viHttpEvent);
//        }
//        if (vipHttpBO == null) {
//            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), vipSQLiteEvent, viHttpEvent);
//        }
//        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
//        vipSQLiteEvent.setHttpBO(vipHttpBO);
//        viHttpEvent.setSqliteBO(vipSQLiteBO);
//        viHttpEvent.setHttpBO(vipHttpBO);
//        //
//        if (configGeneralSQLiteEvent == null) {
//            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
//            configGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (configGeneralHttpEvent == null) {
//            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
//            configGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (configGeneralSQLiteBO == null) {
//            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
//        }
//        if (configGeneralHttpBO == null) {
//            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
//        }
//        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
//        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
//        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
//        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
//        //
//        if (bxConfigGeneralSQLiteEvent == null) {
//            bxConfigGeneralSQLiteEvent = new BXConfigGeneralSQLiteEvent();
//            bxConfigGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (bxConfigGeneralHttpEvent == null) {
//            bxConfigGeneralHttpEvent = new BXConfigGeneralHttpEvent();
//            bxConfigGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (bxConfigGeneralSQLiteBO == null) {
//            bxConfigGeneralSQLiteBO = new BXConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
//        }
//        if (bxConfigGeneralHttpBO == null) {
//            bxConfigGeneralHttpBO = new BXConfigGeneralHttpBO(GlobalController.getInstance().getContext(), bxConfigGeneralSQLiteEvent, bxConfigGeneralHttpEvent);
//        }
//        bxConfigGeneralSQLiteEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
//        bxConfigGeneralSQLiteEvent.setHttpBO(bxConfigGeneralHttpBO);
//        bxConfigGeneralHttpEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
//        bxConfigGeneralHttpEvent.setHttpBO(bxConfigGeneralHttpBO);
//        //
////        configCacheSizeHttpBO = GlobalController.getInstance().getConfigCacheSizeHttpBO();
////        configCacheSizeSQLiteEvent = GlobalController.getInstance().getConfigCacheSizeSQLiteEvent();
//        //
//        if (promotionSQLiteEvent == null) {
//            promotionSQLiteEvent = new PromotionSQLiteEvent();
//            promotionSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (promotionHttpEvent == null) {
//            promotionHttpEvent = new PromotionHttpEvent();
//            promotionHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (promotionSQLiteBO == null) {
//            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
//        }
//        if (promotionHttpBO == null) {
//            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
//        }
//        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
//        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
//        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
//        promotionHttpEvent.setHttpBO(promotionHttpBO);
//        //
//        if (retailTradeAggregationSQLiteEvent == null) {
//            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
//            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (retailTradeAggregationHttpEvent == null) {
//            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
//            retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (retailTradeAggregationHttpBO == null) {
//            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        }
//        if (retailTradeAggregationSQLiteBO == null) {
//            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        }
//        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
//        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
//
//        retailTradeAggregationPresenter = GlobalController.getInstance().getRetailTradeAggregationPresenter();
//        //
//        if (staffSQLiteEvent == null) {
//            staffSQLiteEvent = new StaffSQLiteEvent();
//            staffSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (staffHttpEvent == null) {
//            staffHttpEvent = new StaffHttpEvent();
//            staffHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataActivity);
//        }
//        if (staffSQLiteBO == null) {
//            staffSQLiteBO = new StaffSQLiteBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
//        }
//        if (staffHttpBO == null) {
//            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
//        }
//        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
//        staffSQLiteEvent.setHttpBO(staffHttpBO);
//        staffHttpEvent.setSqliteBO(staffSQLiteBO);
//        staffHttpEvent.setHttpBO(staffHttpBO);
//    }
//
//    /**
//     * 1.????????????????????????
//     * 2.??????????????????????????????UI
//     * 2.1.???????????????????????????????????????????????????RetailTrade???SmallSheet???????????????????????????
//     * ( ??????????????????????????????List???????????????????????????httpBO??????????????????????????????SQLiteEvent???remowe???List????????????????????????????????????????????????
//     * ??????tempSmallSheetFrameList??????????????????????????????????????????????????????????????????????????????Retailtrade,?????????SmallSheet?????? )
//     * 2.2.??????App?????????????????????
//     * 2.3.??????RN syncAction?????????POS??????????????????????????????????????????
//     * 3?????????????????????????????????User??????????????????????????????????????????
//     */
//    private void syncData() {
//        if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(this.getApplicationContext())) {
//            //????????????????????????
//            createDialog();
//            NtpHttpBO.TimeDifference = 0;
//        } else {
//            syncLoadingProgress.start();
//
//            tempSmallSheetFrameList = retrieveNSmallSheetTempDataInSQLite();
//            tempRetailTradeAggregationList = retrieveNRetailTradeAggregationTempDataInSQLite();
//            if (tempSmallSheetFrameList.size() > 0) {
//                uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));//...
//            } else {
//                log.info("----------------------?????????????????????????????????????????????");
//                if (tempRetailTradeList != null) {
//                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//                    if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
//                        log.info("??????????????????????????????");
//                    }
//                } else if (tempRetailTradeAggregationList.size() > 0) {
//                    log.info("------------------------??????????????????????????????????????????");
//                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
//                } else {
//                    log.info("------------------------??????????????????????????????????????????");
//                    log.info("------------------------?????????????????????????????????????????????");
//                    syncTime();
//                }
//            }
//        }
//    }
//
//    /**
//     * ??????????????????????????????????????????????????????????????????????????????????????????
//     */
//    private void createDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("??????");
//        builder.setMessage("???????????????????????????????????????????????????????????????????????????????????????");
//        builder.setPositiveButton("??????", null);
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                Intent intent = new Intent(SyncDataActivity.this, MainActivity.class);
//                BaseActivity.lastRetailTradeAmount = 0.000000d;
//                BaseActivity.lastRetailTradeChangeMoney = 0.000000d;
//                BaseActivity.lastRetailTradePaymentType = "";
//                SyncDataActivity.this.finish();
//                startActivity(intent);
//            }
//        });
//        builder.create().show();
//    }
//
//    /**
//     * ????????????SQLite?????????SyncDatetime???1970???SmallSheet???????????????
//     *
//     * @return
//     */
//    private List<SmallSheetFrame> retrieveNSmallSheetTempDataInSQLite() {
//        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
//        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
//        smallSheetFrame.setSql("where F_SyncDatetime = ?");
//        smallSheetFrame.setConditions(new String[]{"0"});
//        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);//????????????List
//        if (frameList != null) {
//            for (int i = 0; i < frameList.size(); i++) {
//                smallSheetFrame.setSql("where F_FrameID = ?");
//                smallSheetFrame.setConditions(new String[]{String.valueOf(frameList.get(i).getID())});
//                List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetFrame);//?????????????????????????????????
//                frameList.get(i).setListSlave1(textList);
//            }
//        }
//        return frameList;
//    }
//
//    /**
//     * ????????????SQLite?????????SyncDatetime???1970???RetailTradeAggregation???????????????
//     */
//    private List<RetailTradeAggregation> retrieveNRetailTradeAggregationTempDataInSQLite() {
//        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//        List<RetailTradeAggregation> retailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
//        retailTradeAggregation.setSql("where F_SyncDatetime = ?");
//        retailTradeAggregation.setConditions(new String[]{"0"});
//        retailTradeAggregationList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation);
//        return retailTradeAggregationList;
//    }
//
//    /**
//     * ??????SQLite???SmallSheet???????????????
//     */
//    private void uploadSmallSheetTempData(SmallSheetFrame smallSheetFrame) {
//        smallSheetFrame.setReturnObject(1);
//        if (BasePresenter.SYNC_Type_C.equals(smallSheetFrame.getSyncType())) {
//            smallSheetSQLiteEvent.setTmpMasterTableObj(smallSheetFrame);
//            smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done);
//            if (!smallSheetHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Toast.makeText(this, "???????????????C?????????????????????!", Toast.LENGTH_SHORT).show();
//            }
//        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
//            if (!smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Toast.makeText(this, "???????????????U?????????????????????!", Toast.LENGTH_SHORT).show();
//            }
//        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
//            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
//            if (!smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
//                Toast.makeText(this, "???????????????D?????????????????????!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * ??????SQLite???RetailTradeAggregation???????????????
//     */
//    private void uploadRetailTradeAggregationTempData(RetailTradeAggregation retailTradeAggregation) {
//        retailTradeAggregation.setReturnObject(1);
//        if (BasePresenter.SYNC_Type_C.equals(retailTradeAggregation.getSyncType())) {
//            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//            retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation);
//            if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
//                Toast.makeText(appApplication, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * ??????APP??????????????????????????????
//     */
//    private void syncTime() {
//        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
//        if (!ntpHttpBO.syncTime(timeStamp)) {
//            log.error("POS?????????????????????????????????????????????");
//            Toast.makeText(appApplication, "POS?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void retrieveNCPackageUnit(PackageUnit packageUnit) {
//        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
//        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
//            log.error("POS????????????????????????????????????????????????");
//            Toast.makeText(appApplication, "POS????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void retrieveNBarcodes() {
//        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
//        if (!barcodesHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS???????????????????????????????????????");
//            Toast.makeText(appApplication, "POS???????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackBarcodes(String ids) {
//        if (!barcodesHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Barcodes?????????");
//        }
//    }
//
//    private void retrieveNBrand() {
//        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
//        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS????????????????????????????????????");
//            Toast.makeText(appApplication, "POS????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackBrand(String ids) {
//        if (!brandHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Brand?????????");
//        }
//    }
//
//    private void retrieveNCommodityCategory() {
//        commodityCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
//        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS??????????????????????????????????????????");
//            Toast.makeText(appApplication, "POS??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackCommodityCategory(String ids) {
//        if (!commodityCategoryHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback CommodityCategory?????????");
//        }
//    }
//
//    private void retrieveNCommodity() {
//        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
//        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS????????????????????????????????????");
//            Toast.makeText(appApplication, "POS????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackCommodity(String ids) {
//        if (!commodityHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Commodity?????????");
//        }
//    }
//
//    private void retrieveNPromotion() {
//        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
//        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS????????????????????????????????????");
//            Toast.makeText(appApplication, "POS????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackPromotion(String ids) {
//        if (!promotionHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Promotion?????????");
//        }
//    }
//
//    private void retrieveNPos() {
//        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
//        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS?????????????????????Pos?????????");
//            Toast.makeText(appApplication, "POS?????????????????????Pos?????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackPos(String ids) {
//        if (!posHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Pos?????????");
//        }
//    }
//
//    private void retrieveNSmallSheet() {
//        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
//        if (!smallSheetHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS??????????????????????????????????????????");
//            Toast.makeText(appApplication, "POS??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackSmallSheet(String ids) {
//        if (!smallSheetHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback SmallSheet?????????");
//        }
//    }
//
//    private void retrieveNVipCategory() {
//        vipCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsync_Done);
//        if (!vipCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS??????????????????????????????????????????");
//            Toast.makeText(appApplication, "POS??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackVipCategory(String ids) {
//        if (!vipCategoryHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback VipCategory?????????");
//        }
//    }
//
//    private void retrieveNVip() {
//        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
//        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS????????????????????????????????????");
//            Toast.makeText(appApplication, "POS????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void feedbackVip(String ids) {
//        if (!vipHttpBO.feedback(ids)) {
//            log.error("POS???????????????feedback Vip?????????");
//        }
//    }
//
//    private void retrieveNConfigGeneral() {
//        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
//        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS??????????????????????????????????????????");
//        }
//    }
//
//    //    private void retrieveNConfigCacheSize() {
////        configCacheSizeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigCacheSize_RefreshByServerDataAsync_Done);
////        if (!configCacheSizeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
////            Toast.makeText(appApplication, "POS?????????????????????ConfigCacheSize?????????", Toast.LENGTH_SHORT).show();
////        }
////    }
//    private void retrieveNBXConfigGeneral() {
//        bxConfigGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done);
//        if (!bxConfigGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
//            log.error("POS??????????????????????????????????????????");
//        }
//    }
//
//    private void retrieveResigned() {
//        if (!staffHttpBO.retrieveResigned(null)) {
//            Toast.makeText(appApplication, "POS???????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * ???feedback?????????????????????????????????feedback?????????????????????feedback???????????????retrieveN?????????model
//     *
//     * @param event             ??????sqliteEvent
//     * @param syncIDs
//     * @param feedbackDataCase  ??????????????????feedback???model
//     * @param retrieveNDataCase ???????????????RetrieveN???model
//     */
//    private void beforeFeedback(BaseSQLiteEvent event, String syncIDs, String feedbackDataCase, String retrieveNDataCase) {
//        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//            List<BaseModel> list = (List<BaseModel>) event.getListMasterTable();
//            //
//            if (list != null) {
//                if (list.size() > 0) {
//                    for (int i = 0; i < list.size(); i++) {
//                        syncIDs = syncIDs + "," + list.get(i).getID();
//                    }
//                    syncIDs = syncIDs.substring(1, syncIDs.length());
//                    //
//                    onFeedback(feedbackDataCase, syncIDs);
//                }
//            } else {
//                onRetrieveN(retrieveNDataCase);
//            }
//        }
//    }
//
//    /**
//     * feedback??????????????????httpEvent?????????????????????????????????event???Status???requestType,??????????????????model??????RetrieveN
//     *
//     * @param event             ?????????HttpEvent
//     * @param retrieveNDataCase feedback??????????????????retrieveN???model
//     */
//    private void afterFeedback(BaseHttpEvent event, String retrieveNDataCase) {
//        System.out.println(event.getStatus() + "**********" + event.getRequestType());
//        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) {
//            event.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//            event.setRequestType(null);
//            //feedback?????????????????????????????????
//            onRetrieveN(retrieveNDataCase);
//        }
//    }
//
//    private void onRetrieveN(String retrieveNDataCase) {
//        switch (retrieveNDataCase) {
//            case BarcodesData:
//                retrieveNBarcodes();
//                break;
//            case BrandData:
//                retrieveNBrand();
//                break;
//            case CommodityCategoryData:
//                retrieveNCommodityCategory();
//                break;
//            case CommodityData:
//                retrieveNCommodity();
//                break;
//            case PromotionData:
//                retrieveNPromotion();
//                break;
//            case PosData:
//                retrieveNPos();
//                break;
//            case SmallSheetData:
//                retrieveNSmallSheet();
//                break;
//            case VipCategoryData:
//                retrieveNVipCategory();
//                break;
//            case VipData:
//                retrieveNVip();
//                break;
//            case ConfigGeneralData:
//                retrieveNConfigGeneral();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void onFeedback(String feedbackDataCase, String ids) {
//        switch (feedbackDataCase) {
//            case BarcodesData:
//                feedbackBarcodes(ids);
//                break;
//            case BrandData:
//                feedbackBrand(ids);
//                break;
//            case CommodityCategoryData:
//                feedbackCommodityCategory(ids);
//                break;
//            case CommodityData:
//                feedbackCommodity(ids);
//                break;
//            case PromotionData:
//                feedbackPromotion(ids);
//                break;
//            case PosData:
//                feedbackPos(ids);
//                break;
//            case SmallSheetData:
//                feedbackSmallSheet(ids);
//                break;
//            case VipCategoryData:
//                feedbackVipCategory(ids);
//                break;
//            case VipData:
//                feedbackVip(ids);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    createDialog();
//                    break;
//            }
//        }
//    };
//}