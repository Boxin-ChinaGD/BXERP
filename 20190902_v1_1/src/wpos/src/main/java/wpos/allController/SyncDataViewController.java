package wpos.allController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.FragmentActivity;
import wpos.application.LoginActivity;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradeAggregationPresenter;
import wpos.presenter.SmallSheetFramePresenter;
import wpos.presenter.SmallSheetTextPresenter;
import wpos.utils.EventBus;
import wpos.utils.NetworkUtils;
import wpos.utils.PlatForm;
import wpos.utils.ToastUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * 同步顺序：残留的小票格式（如果有）->残留的零售单（如果有）->收银汇总（如果有）->服务器时间->包装单位、商品、促销、POS、小票格式、会员类别、会员、配置,优惠券
 */
@Component("syncDataViewController")
public class SyncDataViewController extends BaseController implements PlatFormHandlerMessage {
    @Resource
    FragmentActivity fragmentActivity;

    @FXML
    private ImageView syncIcon;

    //todo 同步中的动画，需要在跳转页面之后，将其释放！
    Timer timer;
    TimerTask timerTask;
    private int i = 0;


    /**
     * 检查会话是否已经被踢出或过期。若然，UI需要跳回登录界面。一般来说，会话不会过期，因为timerTaskPing会定时ping服务器
     * 测试会话被踢出的方法：先后用同1个收银员帐号在2台Pos，登录到同1个NBR服务器。后登录的，会令先登录的跳回到登录界面
     */
    public static Timer timerCheckInvalidSession;
    /**
     * 参考timerCheckInvalidSession
     */

    public static TimerTask timerTaskCheckInvalidSession;

    @Resource
    private LoginActivity loginActivity;


    public SyncDataViewController(Intent intent) {

    }

    public SyncDataViewController() {

    }

    public void register() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
    }

    public void init() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (i > 360) {
                    i = 0;
                }
//                System.out.println("进入了");
                syncIcon.setRotate(i);
                i += 5;
            }
        };
        timer.schedule(timerTask, 50, 50);
        initObject();
        timerCheckInvalidSession = new Timer();
        //
        // 启动会话被踢出检测或会话过期检测。若被踢出或过期，UI自动跳到登录界面
        if (timerCheckInvalidSession != null) {
            timerTaskCheckInvalidSession = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (BaseHttpEvent.HttpRequestStatus == 1) {
                            Platform.runLater(() -> {
                                timerCheckInvalidSession.cancel(); //set null 放在onDestroy()中
                                timerTaskCheckInvalidSession.cancel();
                                timerCheckInvalidSession.purge();
                                BaseHttpEvent.HttpRequestStatus = 0;
                                //
                                if(ResetBaseDataViewController.resetThread != null && ResetBaseDataViewController.resetThread.isAlive()) {
                                    try {
                                        System.out.println("++++++++++++++退出旧的重置基础资料线程！！");
                                        ResetBaseDataViewController.resetThread.stop();
                                    } catch (Exception e) {
                                        //
                                    }
                                }
                                GlobalController.getInstance().setSessionID(null);
                                log.info("sessionID已清除");
                                Constants.setCurrentStaff(null); //稳健的做法
                                //
                                returnToLoginActivity();
//                                System.gc();
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timerCheckInvalidSession.schedule(timerTaskCheckInvalidSession, 0, 1000);
        }

        syncData();
    }

    private void returnToLoginActivity() {
        Main.exitFromSyncThread();
        closeLoadingDialog();
        //
        initialization();//点击登录后会调用，所以这里不call也行

        StageController.get().closeAllstages();
        log.info("已将所有的Activity finish()，接下来进入loginActivity");

        try {
            Intent intent = new Intent();
            intent.putExtra("LoginInterceptor", 1);
            intent.putExtra("LoginInterceptorWarn", BaseHttpEvent.HttpRequestWarnMsg);
            loginActivity.setIntent(intent);
            loginActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出登录后，重置firstTimeEnterMain1Activity
        MainController.firstTimeEnterMain1Activity = true;
    }

    private Logger log = Logger.getLogger(this.getClass());
//    @BindView(R.id.sync_progress)
//    SyncLoadingProgress syncLoadingProgress;
//    @BindView(R.id.show_details)
//    TextView showDetails;


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
    private static final String CouponData = "coupon";

    private static NetworkUtils networkUtils = new NetworkUtils();

    private List<SmallSheetFrame> tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();//用于保存POS机启动时在SQLite找到的需要上传的SmallSheet数据
    private List<RetailTrade> tempRetailTradeList = new ArrayList<RetailTrade>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTrade数据
    private List<RetailTradeAggregation> tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTradeAggregation数据

    private String syncBarcodesIDs = "";//保存同步的Barcodes的ID，用于feedback时传参    下面定义的变量也是一样的作用
    private String syncBrandIDs = "";
    private String syncCommodityCategoryIDs = "";
    private String syncCommodityIDs = "";
    private String syncPromotionIDs = "";
    private String syncPosIDs = "";
    private String syncSmallSheetIDs = "";
    private String syncVipCategoryIDs = "";
    private String syncVipIDs = "";

    private long lTimeOut = 50;
    @Resource
    public SmallSheetFramePresenter smallSheetFramePresenter;
    @Resource
    public SmallSheetTextPresenter smallSheetTextPresenter;
    @Resource
    public SmallSheetHttpBO smallSheetHttpBO;
    @Resource
    public SmallSheetSQLiteBO smallSheetSQLiteBO;
    @Resource
    public SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    @Resource
    public SmallSheetHttpEvent smallSheetHttpEvent;
    //
    @Resource
    public RetailTradeHttpBO retailTradeHttpBO;
    @Resource
    public RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    public RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    public RetailTradeHttpEvent retailTradeHttpEvent;
    //
    @Resource
    public NtpHttpEvent ntpHttpEvent;
    @Resource
    public NtpHttpBO ntpHttpBO;
    //
    @Resource
    public PackageUnitSQLiteBO packageUnitSQLiteBO;
    @Resource
    public PackageUnitHttpBO packageUnitHttpBO;
    @Resource
    public PackageUnitSQLiteEvent packageUnitSQLiteEvent;
    @Resource
    public PackageUnitHttpEvent packageUnitHttpEvent;
    //
    @Resource
    public BarcodesSQLiteBO barcodesSQLiteBO;
    @Resource
    public BarcodesHttpBO barcodesHttpBO;
    @Resource
    public BarcodesSQLiteEvent barcodesSQLiteEvent;
    @Resource
    public BarcodesHttpEvent barcodesHttpEvent;
    //
    @Resource
    public BrandSQLiteBO brandSQLiteBO;
    @Resource
    public BrandHttpBO brandHttpBO;
    @Resource
    public BrandSQLiteEvent brandSQLiteEvent;
    @Resource
    public BrandHttpEvent brandHttpEvent;
    //
    @Resource
    public CommodityCategorySQLiteBO commodityCategorySQLiteBO;
    @Resource
    public CommodityCategoryHttpBO commodityCategoryHttpBO;
    @Resource
    public CommodityCategorySQLiteEvent commodityCategorySQLiteEvent;
    @Resource
    public CommodityCategoryHttpEvent commodityCategoryHttpEvent;
    //
    @Resource
    public CommoditySQLiteBO commoditySQLiteBO;
    @Resource
    public CommodityHttpBO commodityHttpBO;
    @Resource
    public CommoditySQLiteEvent commoditySQLiteEvent;
    @Resource
    public CommodityHttpEvent commodityHttpEvent;
    //
    @Resource
    public PosSQLiteBO posSQLiteBO;
    @Resource
    public PosHttpBO posHttpBO;
    @Resource
    public PosSQLiteEvent posSQLiteEvent;
    @Resource
    public PosHttpEvent posHttpEvent;
    //
    // TODO
//    private static VipCategorySQLiteBO vipCategorySQLiteBO = null;
//    private static VipCategoryHttpBO vipCategoryHttpBO = null;
//    private static VipCategorySQLiteEvent vipCategorySQLiteEvent = null;
//    private static VipCategoryHttpEvent vipCategoryHttpEvent = null;
    //
    @Resource
    public VipSQLiteBO vipSQLiteBO;
    @Resource
    public VipHttpBO vipHttpBO;
    @Resource
    public VipSQLiteEvent vipSQLiteEvent;
    @Resource
    public VipHttpEvent viHttpEvent;
    //
    @Resource
    public ConfigGeneralSQLiteBO configGeneralSQLiteBO;
    @Resource
    public ConfigGeneralHttpBO configGeneralHttpBO;
    @Resource
    public ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    @Resource
    public ConfigGeneralHttpEvent configGeneralHttpEvent;
    //
    @Resource
    public BXConfigGeneralSQLiteBO bxConfigGeneralSQLiteBO;
    @Resource
    public BXConfigGeneralHttpBO bxConfigGeneralHttpBO;
    @Resource
    public BXConfigGeneralSQLiteEvent bxConfigGeneralSQLiteEvent;
    @Resource
    public BXConfigGeneralHttpEvent bxConfigGeneralHttpEvent;
    //
//    private ConfigCacheSizeHttpBO configCacheSizeHttpBO;
//    private ConfigCacheSizeSQLiteEvent configCacheSizeSQLiteEvent;
    //
    @Resource
    public PromotionSQLiteBO promotionSQLiteBO;
    @Resource
    public PromotionHttpBO promotionHttpBO;
    @Resource
    public PromotionSQLiteEvent promotionSQLiteEvent;
    @Resource
    public PromotionHttpEvent promotionHttpEvent;
    //
    @Resource
    public RetailTradeAggregationPresenter retailTradeAggregationPresenter;
    @Resource
    public RetailTradeAggregationHttpBO retailTradeAggregationHttpBO;
    @Resource
    public RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    @Resource
    public RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;
    @Resource
    public RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent;
    //
    @Resource
    public StaffSQLiteBO staffSQLiteBO;
    @Resource
    public StaffHttpBO staffHttpBO;
    @Resource
    public StaffSQLiteEvent staffSQLiteEvent;
    @Resource
    public StaffHttpEvent staffHttpEvent;

    @Resource
    public CouponSQLiteBO couponSQLiteBO;
    @Resource
    public CouponSQLiteEvent couponSQLiteEvent;
    @Resource
    public CouponHttpEvent couponHttpEvent;
    @Resource
    public CouponHttpBO couponHttpBO;

    // TODO 在SyncDataActivity进行onCreate
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
////        // 登录成功远程服务器后，resume同步线程
////        if (GlobalController.getInstance().getSessionID() != null) {
////            SyncThread.aiPause.set(SyncThread.RESUME);
////            log.info("用户登录远程服务器成功（登录SQLite成功会话为null），同步线程已经被resume");
////        }
//    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Create  // 区分是上传小票还是同步小票(C/U/D)
                        || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Update
                        || event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_Delete) {
                    if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) { // 如果上传失败，就跳过该小票格式
                        tempSmallSheetFrameList.remove(0);
                        if (tempSmallSheetFrameList.size() == 0) {
                            //上传RetailTrade
                            if (tempRetailTradeList != null) {
                                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                                if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                    log.info("查询临时零售单失败！");
                                }
                            }
                        } else {
                            //上传tempSmallSheetFrameList的第一个元素
                            uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                        }
                    } else {
                        log.debug("上传或同步小票成功，小票为：" + event.getBaseModel1());
                    }
                } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_SmallSheet_RetrieveNC) {
                    log.debug("ERT_SmallSheet_RetrieveN已完成后");
                } else if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_FeedBack) { // 同步小票
                    afterFeedback(event, VipCategoryData);
                } else {
                    log.error("未处理的情况,event=" + event);
                }
            } else {
                log.info("同步小票格式时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                // TODO
//                handler.sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            //开机同步SmallSheet上传部分
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
                                log.info("查询临时零售单失败！");
                            }
                        }
                    } else {
                        //上传tempSmallSheetFrameList的第一个元素
                        uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                    }
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {//开机同步SmallSheet RetrieveN部分
                retrieveNConfigGeneral();
//                beforeFeedback(event, syncSmallSheetIDs, SmallSheetData, VipCategoryData);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("上传零售单时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("SyncDataActivity.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处SyncDataActivity.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            log.info("#########################################################SyncDataActivity onRetailTradeSQLiteEvent");
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                event.setEventTypeSQLite(null);
                if (tempRetailTradeAggregationList.size() > 0) {
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    syncTime();
                }
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("SyncDataActivity.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处SyncDataActivity.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated) {
                log.info("上传收银汇总时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync_HttpDone && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                tempRetailTradeAggregationList.remove(0);
                //
                if (tempRetailTradeAggregationList.size() > 0) {
                    //继续上传收银汇总
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);//这一句是没有用的，只是为了让阅读者知道上传收银汇总已经完成了最后一传
                    syncTime();
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onNtpHttpEvent(NtpHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
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
                log.info("同步NTP时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("MainActivity.onRetailTradeSQLiteEvent1()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处MainActivity.onRetailTradeSQLiteEvent1()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("同步包装单位时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //重置Commodity
                retrieveNBarcodes();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, BrandData);
            } else {
                log.info("同步条形码时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncBarcodesIDs, BarcodesData, BrandData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, CommodityCategoryData);
            } else {
                log.info("同步品牌时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncBrandIDs, BrandData, CommodityCategoryData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, CommodityData);
            } else {
                log.info("同步商品类别时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncCommodityCategoryIDs, CommodityCategoryData, CommodityData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, PromotionData);
            } else {
                log.info("同步商品时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncCommodityIDs, CommodityData, PromotionData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, PosData);
            } else {
                log.info("同步促销时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncPromotionIDs, PromotionData, PosData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, SmallSheetData);
            } else {
                log.info("同步POS信息时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncPosIDs, PosData, SmallSheetData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    // TODO
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onVipCategoryHttpEvent(VipCategoryHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
//                event.onEvent();
//            } else {
//                log.info("同步会员类别时出现网络故障，下次有网开机登录时再同步");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)

    public void onVipCategorySQLiteEvent(VipCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            retrieveNConfigGeneral();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
                afterFeedback(event, ConfigGeneralData);
            } else {
                log.info("同步会员时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            beforeFeedback(event, syncVipIDs, VipData, ConfigGeneralData);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("同步配置信息时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retrieveNBXConfigGeneral();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBXConfigGeneralHttpEvent(BXConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("同步公共配置信息时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBXConfigGeneralSQLiteEvent(BXConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步删除已离职员工
                retrieveResigned();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigCacheSizeHttpEvent(ConfigCacheSizeHttpEvent event) {
//        event.onEvent();
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigCacheSizeSQLiteEvent(ConfigCacheSizeSQLiteEvent event) {
//        event.onEvent();
//        if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//            event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//            //同步删除已离职员工
//            retrieveResigned();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("同步员工时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //同步优惠券
                retrieveNCoupon();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            } else {
                log.info("同步优惠券时出现网络故障，下次有网开机登录时再同步");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SyncDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                //跳转到MainActivity
//                SyncData1Activity.this.finish();
                BaseController.lastRetailTradeAmount = 0.000000d;
                BaseController.lastRetailTradeChangeMoney = 0.000000d;
                BaseController.lastRetailTradePaymentType = "";
//                startActivity(intent);
                Message message = new Message();
                message.what = 2;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * 实例化对象
     */
    private void initObject() {
        smallSheetSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        smallSheetHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        smallSheetSQLiteBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetSQLiteBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetHttpBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetHttpBO.setHttpEvent(smallSheetHttpEvent);
        //
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);

        retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        //
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);

        ntpHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        ntpHttpBO.setHttpEvent(ntpHttpEvent);
        //
        ntpHttpEvent.setHttpBO(ntpHttpBO);

        packageUnitSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        packageUnitHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        packageUnitSQLiteBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitSQLiteBO.setHttpEvent(packageUnitHttpEvent);
        packageUnitHttpBO.setSqLiteEvent(packageUnitSQLiteEvent);
        packageUnitHttpBO.setHttpEvent(packageUnitHttpEvent);
        //
        packageUnitSQLiteEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitSQLiteEvent.setHttpBO(packageUnitHttpBO);
        packageUnitHttpEvent.setSqliteBO(packageUnitSQLiteBO);
        packageUnitHttpEvent.setHttpBO(packageUnitHttpBO);

        barcodesSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        barcodesHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        barcodesSQLiteBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesSQLiteBO.setHttpEvent(barcodesHttpEvent);
        barcodesHttpBO.setSqLiteEvent(barcodesSQLiteEvent);
        barcodesHttpBO.setHttpEvent(barcodesHttpEvent);
        //
        barcodesSQLiteEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesSQLiteEvent.setHttpBO(barcodesHttpBO);
        barcodesHttpEvent.setSqliteBO(barcodesSQLiteBO);
        barcodesHttpEvent.setHttpBO(barcodesHttpBO);

        brandSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        brandHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        brandSQLiteBO.setSqLiteEvent(brandSQLiteEvent);
        brandSQLiteBO.setHttpEvent(brandHttpEvent);
        brandHttpBO.setSqLiteEvent(brandSQLiteEvent);
        brandHttpBO.setHttpEvent(brandHttpEvent);
        //
        brandSQLiteEvent.setSqliteBO(brandSQLiteBO);
        brandSQLiteEvent.setHttpBO(brandHttpBO);
        brandHttpEvent.setSqliteBO(brandSQLiteBO);
        brandHttpEvent.setHttpBO(brandHttpBO);

        commodityCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        commodityCategoryHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        commodityCategorySQLiteBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategorySQLiteBO.setHttpEvent(commodityCategoryHttpEvent);
        commodityCategoryHttpBO.setSqLiteEvent(commodityCategorySQLiteEvent);
        commodityCategoryHttpBO.setHttpEvent(commodityCategoryHttpEvent);
        //
        commodityCategorySQLiteEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategorySQLiteEvent.setHttpBO(commodityCategoryHttpBO);
        commodityCategoryHttpEvent.setSqliteBO(commodityCategorySQLiteBO);
        commodityCategoryHttpEvent.setHttpBO(commodityCategoryHttpBO);

        commoditySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        commodityHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        commoditySQLiteBO.setSqLiteEvent(commoditySQLiteEvent);
        commoditySQLiteBO.setHttpEvent(commodityHttpEvent);
        commodityHttpBO.setSqLiteEvent(commoditySQLiteEvent);
        commodityHttpBO.setHttpEvent(commodityHttpEvent);
        //
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commodityHttpEvent.setHttpBO(commodityHttpBO);

        posSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        posHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        posSQLiteBO.setSqLiteEvent(posSQLiteEvent);
        posSQLiteBO.setHttpEvent(posHttpEvent);
        posHttpBO.setSqLiteEvent(posSQLiteEvent);
        posHttpBO.setHttpEvent(posHttpEvent);
        //
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        // TODO
//        if (vipCategorySQLiteEvent == null) {
//            vipCategorySQLiteEvent = new VipCategorySQLiteEvent();
//            vipCategorySQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
//        }
//        if (vipCategoryHttpEvent == null) {
//            vipCategoryHttpEvent = new VipCategoryHttpEvent();
//            vipCategoryHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
//        }
//        if (vipCategorySQLiteBO == null) {
//            vipCategorySQLiteBO = new VipCategorySQLiteBO(vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        }
//        if (vipCategoryHttpBO == null) {
//            vipCategoryHttpBO = new VipCategoryHttpBO(vipCategorySQLiteEvent, vipCategoryHttpEvent);
//        }
//        vipCategorySQLiteEvent.setSqliteBO(vipCategorySQLiteBO);
//        vipCategorySQLiteEvent.setHttpBO(vipCategoryHttpBO);
//        vipCategoryHttpEvent.setSqliteBO(vipCategorySQLiteBO);
//        vipCategoryHttpEvent.setHttpBO(vipCategoryHttpBO);

        vipSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        viHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        vipSQLiteBO.setSqLiteEvent(vipSQLiteEvent);
        vipSQLiteBO.setHttpEvent(viHttpEvent);
        vipHttpBO.setSqLiteEvent(vipSQLiteEvent);
        vipHttpBO.setHttpEvent(viHttpEvent);
        //
        vipSQLiteEvent.setSqliteBO(vipSQLiteBO);
        vipSQLiteEvent.setHttpBO(vipHttpBO);
        viHttpEvent.setSqliteBO(vipSQLiteBO);
        viHttpEvent.setHttpBO(vipHttpBO);

        configGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        configGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        configGeneralSQLiteBO.setSqLiteEvent(configGeneralSQLiteEvent);
        configGeneralSQLiteBO.setHttpEvent(configGeneralHttpEvent);
        configGeneralHttpBO.setSqLiteEvent(configGeneralSQLiteEvent);
        configGeneralHttpBO.setHttpEvent(configGeneralHttpEvent);
        //
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);

        bxConfigGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        bxConfigGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        bxConfigGeneralSQLiteBO.setSqLiteEvent(bxConfigGeneralSQLiteEvent);
        bxConfigGeneralSQLiteBO.setHttpEvent(bxConfigGeneralHttpEvent);
        bxConfigGeneralHttpBO.setSqLiteEvent(bxConfigGeneralSQLiteEvent);
        bxConfigGeneralHttpBO.setHttpEvent(bxConfigGeneralHttpEvent);
        //
        bxConfigGeneralSQLiteEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
        bxConfigGeneralSQLiteEvent.setHttpBO(bxConfigGeneralHttpBO);
        bxConfigGeneralHttpEvent.setSqliteBO(bxConfigGeneralSQLiteBO);
        bxConfigGeneralHttpEvent.setHttpBO(bxConfigGeneralHttpBO);


        promotionSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        promotionHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        promotionSQLiteBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteBO.setHttpEvent(promotionHttpEvent);
        promotionHttpBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionHttpBO.setHttpEvent(promotionHttpEvent);
        //
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionHttpEvent.setHttpBO(promotionHttpBO);

        retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        retailTradeAggregationSQLiteBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationSQLiteBO.setHttpEvent(retailTradeAggregationHttpEvent);
        retailTradeAggregationHttpBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationHttpBO.setHttpEvent(retailTradeAggregationHttpEvent);
        //
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);

        staffSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        staffHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        staffSQLiteBO.setSqLiteEvent(staffSQLiteEvent);
        staffSQLiteBO.setHttpEvent(staffHttpEvent);
        staffHttpBO.setSqLiteEvent(staffSQLiteEvent);
        staffHttpBO.setHttpEvent(staffHttpEvent);
        //
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);

        couponSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        couponHttpEvent.setId(BaseEvent.EVENT_ID_SyncDataStage);
        //
        couponSQLiteBO.setSqLiteEvent(couponSQLiteEvent);
        couponSQLiteBO.setHttpEvent(couponHttpEvent);
        couponHttpBO.setSqLiteEvent(couponSQLiteEvent);
        couponHttpBO.setHttpEvent(couponHttpEvent);
        //
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
    }

    /**
     * 1.判断网络是否可用
     * 2.若网络可用，启动等待UI
     * 2.1.先查找需要上传到服务器的残留数据（RetailTrade，SmallSheet）将其上传到服务器
     * ( 上传：先上传小票格式List的第一个元素，调用httpBO请求服务器，成功后到SQLiteEvent，remowe掉List的第一个元素（刚刚上传的元素），
     * 检查tempSmallSheetFrameList还有多少数据，有的话继续上传第一个，若没有，开始上传Retailtrade,操作与SmallSheet相似 )
     * 2.2.同步App和服务器的时间
     * 2.3.然后RN syncAction，拿到POS机需要同步的数据，进行同步。
     * 3、若网络不可用，则告知User下次启动项目的时候再进行同步
     */
    private void syncData() {
        if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible()) {
            //网络不可用的情况
            createDialog();
            NtpHttpBO.TimeDifference = 0;
        } else {
//            syncLoadingProgress.start();

            tempSmallSheetFrameList = retrieveNSmallSheetTempDataInSQLite();
            tempRetailTradeAggregationList = retrieveNRetailTradeAggregationTempDataInSQLite();
            if (tempSmallSheetFrameList.size() > 0) {
                uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));//...
            } else {
                log.info("----------------------本地没有需要上传的临时小票格式");
                if (tempRetailTradeList != null) {
                    retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                    if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                        log.info("查询临时零售单失败！");
                    }
                } else if (tempRetailTradeAggregationList.size() > 0) {
                    log.info("------------------------本地没有需要上传的临时零售单");
                    uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                } else {
                    log.info("------------------------本地没有需要上传的临时零售单");
                    log.info("------------------------本地没有需要上传的临时收银汇总");
                    syncTime();
                }
            }
        }
    }

    /**
     * 当网络不可用的时候创建对话框告诉用户，下次开机再进行同步数据
     */
    private void createDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("当前网络不可用，将进入离线模式，待下次启动时再进行数据同步");
        ButtonType buttonTypeConfirm = new ButtonType("确定");
        alert.getButtonTypes().setAll(buttonTypeConfirm);
        Optional<ButtonType> result = alert.showAndWait();
        try {
            if (result.get() == buttonTypeConfirm) {
                BaseController.lastRetailTradeAmount = 0.000000d;
                BaseController.lastRetailTradeChangeMoney = 0.000000d;
                BaseController.lastRetailTradePaymentType = "";
                fragmentActivity.setIntent(new Intent());
                startAllFragmentActivity();
            } else {
                throw new RuntimeException("黑客行为");
            }
        } catch(NoSuchElementException e) { // 关闭对话框,也继续跳转到主页面
            BaseController.lastRetailTradeAmount = 0.000000d;
            BaseController.lastRetailTradeChangeMoney = 0.000000d;
            BaseController.lastRetailTradePaymentType = "";
            fragmentActivity.setIntent(new Intent());
            startAllFragmentActivity();
        }
    }

    //跳转到FragmentActivity
    private void startAllFragmentActivity() {
        //将动画的timer释放
        if (timer!=null){
            timerTask.cancel();
            timer.cancel();
            timer.purge();
        }
        //取消注册plamForm
        PlatForm.get().unRegister();

        try {
            StageController.get().closeStge(StageType.SYNCDATA_STAGE.name()).unloadStage(StageType.SYNCDATA_STAGE.name());
            fragmentActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的SmallSheet的临时数据
     *
     * @return
     */
    private List<SmallSheetFrame> retrieveNSmallSheetTempDataInSQLite() {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        smallSheetFrame.setSql("where F_SyncDatetime = %s and F_SlaveCreated = %s");
        smallSheetFrame.setConditions(new String[]{"0", String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);//找到主表List
        if (frameList != null) {
            for (int i = 0; i < frameList.size(); i++) {
                smallSheetFrame.setSql("where F_FrameID = %s");
                smallSheetFrame.setConditions(new String[]{String.valueOf(frameList.get(i).getID())});
                List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetFrame);//找到主表对应的从表数据
                frameList.get(i).setListSlave1(textList);
            }
        }
        return frameList;
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
                ToastUtil.toast("启动时上传C型小票格式失败!", ToastUtil.LENGTH_SHORT);
            }
        } else if (BasePresenter.SYNC_Type_U.equals(smallSheetFrame.getSyncType())) {
            if (!smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                ToastUtil.toast("启动时上传U型小票格式失败!", ToastUtil.LENGTH_SHORT);
            }
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetFrame.getSyncType())) {
            smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_DeleteMasterSlaveAsync_Done);
            if (!smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame)) {
                ToastUtil.toast("启动时上传D型小票格式失败!", ToastUtil.LENGTH_SHORT);
            }
        }
    }

    /**
     * 找到本地SQLite中所有SyncDatetime为1970的RetailTradeAggregation的临时数据
     */
    private List<RetailTradeAggregation> retrieveNRetailTradeAggregationTempDataInSQLite() {
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        List<RetailTradeAggregation> retailTradeAggregationList = new ArrayList<RetailTradeAggregation>();
        retailTradeAggregation.setSql("where F_SyncDatetime = %s");
        retailTradeAggregation.setConditions(new String[]{"0"});
        retailTradeAggregationList = (List<RetailTradeAggregation>) retailTradeAggregationPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_RetailTradeAggregation_RetrieveNByConditions, retailTradeAggregation);
        return retailTradeAggregationList;
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
                ToastUtil.toast("启动时上传临时收银汇总失败！", ToastUtil.LENGTH_SHORT);
            }
        }
    }

    /**
     * 同步APP的时间和服务器的时间
     */
    private void syncTime() {
        long timeStamp = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference).getTime();
        if (!ntpHttpBO.syncTime(timeStamp)) {
            log.error("POS机启动时，同步服务器时间失败！");
            ToastUtil.toast("POS机启动时，同步服务器时间失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNCPackageUnit(PackageUnit packageUnit) {
        packageUnitSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_PackageUnit_RefreshByServerDataAsync_Done);
        if (!packageUnitHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, packageUnit)) {
            log.error("POS机启动时，同步商品包装单位失败！");
            ToastUtil.toast("POS机启动时，同步商品包装单位失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void retrieveNBarcodes() {
        barcodesSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
        if (!barcodesHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步条形码失败！");
            ToastUtil.toast("POS机启动时，同步条形码失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackBarcodes(String ids) {
        if (!barcodesHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Barcodes失败！");
        }
    }

    private void retrieveNBrand() {
        brandSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        if (!brandHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步品牌失败！");
            ToastUtil.toast("POS机启动时，同步品牌失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackBrand(String ids) {
        if (!brandHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Brand失败！");
        }
    }

    private void retrieveNCommodityCategory() {
        commodityCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        if (!commodityCategoryHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步商品类别失败！");
            ToastUtil.toast("POS机启动时，同步商品类别失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackCommodityCategory(String ids) {
        if (!commodityCategoryHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback CommodityCategory失败！");
        }
    }

    private void retrieveNCommodity() {
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RefreshByServerDataAsync_Done);
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步商品失败！");
            ToastUtil.toast("POS机启动时，同步商品失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackCommodity(String ids) {
        if (!commodityHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Commodity失败！");
        }
    }

    private void retrieveNPromotion() {
        promotionSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步促销失败！");
            ToastUtil.toast("POS机启动时，同步促销失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackPromotion(String ids) {
        if (!promotionHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Promotion失败！");
        }
    }

    private void retrieveNPos() {
        posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        if (!posHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步Pos失败！");
            ToastUtil.toast("POS机启动时，同步Pos失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackPos(String ids) {
        if (!posHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Pos失败！");
        }
    }

    private void retrieveNSmallSheet() {
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        ssf.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, ssf)) {
            log.error("POS机启动时，同步小票格式失败！");
            ToastUtil.toast("POS机启动时，同步小票格式失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackSmallSheet(String ids) {
        if (!smallSheetHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback SmallSheet失败！");
        }
    }

    // TODO
//    private void retrieveNVipCategory() {
//        vipCategorySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RefreshByServerDataAsync_Done);
//        VipCategory vipCategory = new VipCategory();
//        vipCategory.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//        vipCategory.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
//        if (!vipCategoryHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vipCategory)) {
//            log.error("POS机启动时，同步会员类别失败！");
//            Toast.makeText(this, "POS机启动时，同步会员类别失败！", Toast.LENGTH_SHORT).show();
//        }
//    }
    // TODO
//    private void feedbackVipCategory(String ids) {
//        if (!vipCategoryHttpBO.feedback(ids)) {
//            log.error("POS机启动时，feedback VipCategory失败！");
//        }
//    }

    private void retrieveNVip() {
        vipSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
        if (!vipHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步会员失败！");
            ToastUtil.toast("POS机启动时，同步会员失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    private void feedbackVip(String ids) {
        if (!vipHttpBO.feedback(ids)) {
            log.error("POS机启动时，feedback Vip失败！");
        }
    }

    private void retrieveNConfigGeneral() {
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done);
        if (!configGeneralHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, null)) {
            log.error("POS机启动时，同步私有配置失败！");
        }
    }

    private void retrieveNCoupon() {
        Coupon coupon = new Coupon();
        coupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        coupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        couponSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Coupon_RefreshByServerDataAsyncC);
        couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!couponHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, coupon)) {
            log.error("POS机启动时，同步优惠券失败！");
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
            log.error("POS机启动时，同步公共配置失败！");
        }
    }

    private void retrieveResigned() {
        if (!staffHttpBO.retrieveResigned(null)) {
            ToastUtil.toast("POS机启动时，同步删除已离职员工失败！", ToastUtil.LENGTH_SHORT);
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
        System.out.println(event.getStatus() + "**********" + event.getRequestType());
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
                // TODO
//                retrieveNVipCategory();
                break;
            case VipData:
                retrieveNVip();
                break;
            case ConfigGeneralData:
                retrieveNConfigGeneral();
                break;
            case CouponData:
                retrieveNCoupon();
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
                // TODO
//                feedbackVipCategory(ids);
                break;
            case VipData:
                feedbackVip(ids);
                break;
            default:
                break;
        }
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                createDialog();
                break;
            case 2:
                fragmentActivity.setIntent(new Intent());
                startAllFragmentActivity();
                break;
        }
    }


}
