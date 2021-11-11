package wpos.allController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.LoginActivity;
import wpos.bo.LogoutHttpBO;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.Intent;
import wpos.model.Message;
import wpos.utils.*;

import javax.annotation.Resource;
import java.util.Optional;

@Component("preSaleViewController")
public class PreSaleViewController extends BaseController implements PlatFormHandlerMessage {
    private Logger log = Logger.getLogger(this.getClass());
    // 下载按钮
    @FXML
    Button tvDownload;
    @FXML
    Button tvLogout;
    @FXML
    Label tvDownloadDetail;

    @Resource
    private LoginActivity loginActivity;


    private static NetworkUtils networkUtils = new NetworkUtils();
    @Resource
    private LogoutHttpBO logoutHttpBO;
    @Resource
    private LogoutHttpEvent logoutHttpEvent;
    StringBuffer downloadDetail = new StringBuffer();

    @Resource
    private ResetBaseDataUtil resetBaseDataUtil;

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    @org.greenrobot.eventbus.Subscribe
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pre_sale);
//
//        ButterKnife.bind(this);
//        tvDownload.setOnClickListener(this);
//        tvLogout.setOnClickListener(this);
//
//        if (logoutHttpEvent == null) {
//            logoutHttpEvent = new LogoutHttpEvent();
//            logoutHttpEvent.setId(BaseEvent.EVENT_ID_PreSaleStage);
//        }
//        //
//        if (logoutHttpBO == null) {
//            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
//        }
//        logoutHttpEvent.setHttpBO(logoutHttpBO);
//    }

    public PreSaleViewController() {
    }

    public void onCreate() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
        }
        logoutHttpEvent.setId(BaseEvent.EVENT_ID_PreSaleStage);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
//                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadBaseDataMessageEvent(DownloadBaseDataMessageEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            // TODO couponSQLiteEvent未创建
            downloadDetail.append(String.valueOf(event.getMessage()));
            if (resetBaseDataUtil.couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                resetBaseDataUtil.couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                downloadDetail.append("基础资料下载完成");
//                tvDownload.setEnabled(true);
//                tvLogout.setEnabled(true);
                tvDownload.setDisable(false);
                tvLogout.setDisable(false);
            }
            log.info("------------------------------downloadDetail" + event.getMessage());
            String detail = new String(downloadDetail);
            tvDownloadDetail.setText(detail);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) throws Exception {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
//                Intent intent = new Intent(PreSale1Activity.this, Login1Activity.class);
//                PreSale1Activity.this.finish();
//                startActivity(intent);
//                LoginActivity.startLoginActivity(new Intent());
                Message message3 = new Message();
                message3.what = 3;
                PlatForm.get().sendMessage(message3);
//                StageController.get().closeStge(StageType.PRESALE_STAGE.name()).unloadStage(StageType.PRESALE_STAGE.name());
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @FXML
    public void tvDownload() {
        tvDownloadDetail.setText("");
        downloadDetail.delete(0, downloadDetail.length());
        if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible()) {
            //网络不可用的情况
            createDialog();
        } else {
//            tvDownload.setEnabled(false);
//            tvLogout.setEnabled(false);
            tvDownload.setDisable(true);
            tvLogout.setDisable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSyncData();
                }
            }).start();
        }
    }

    @FXML
    public void tvLogout() {
        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible()) {
//            loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
            showLoadingDialog();

            logoutHttpBO.getHttpEvent().setId(BaseEvent.EVENT_ID_PreSaleStage);
            if (!logoutHttpBO.logoutAsync()) {
                log.info("退出失败！");
            }
        } else {
            GlobalController.getInstance().setSessionID(null);
//            Intent intent = new Intent(PreSale1Activity.this, Login1Activity.class);
//            PreSale1Activity.this.finish();
//            startActivity(intent);
//            LoginActivity.startLoginActivity(new Intent());
            loginActivity.setIntent(new Intent());
            try {
                loginActivity.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            StageController.get().closeStge(StageType.PRESALE_STAGE.name()).unloadStage(StageType.PRESALE_STAGE.name());
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.download_basedata:
//                tvDownloadDetail.setText("");
//                downloadDetail.delete(0, downloadDetail.length());
//                if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(this.getApplicationContext())) {
//                    //网络不可用的情况
//                    createDialog();
//                } else {
//                    tvDownload.setEnabled(false);
//                    tvLogout.setEnabled(false);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            isSyncData();
//                        }
//                    }).start();
//                }
//                break;
//            case R.id.logout:
//                if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(this.getApplicationContext())) {
//                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//
//                    logoutHttpBO.getHttpEvent().setId(BaseEvent.EVENT_ID_PreSaleStage);
//                    if (!logoutHttpBO.logoutAsync()) {
//                        log.info("退出失败！");
//                    }
//                } else {
//                    GlobalController.getInstance().setSessionID(null);
//                    Intent intent = new Intent(PreSale1Activity.this, Login1Activity.class);
//                    PreSale1Activity.this.finish();
//                    startActivity(intent);
//                }
//                break;
//        }
//    }

    /**
     * 1.判断网络是否可用
     * 2.网络可用，启动等待UI
     * 3.先查找需要上传到服务器的数据（RetailTrade，SmallSheet）将其上传到服务器
     * ( 上传：先上传小票格式List的第一个元素，调用httpBO请求服务器，成功后到SQLiteEvent，remowe掉List的第一个元素（刚刚上传的元素），
     * 检查tempSmallSheetFrameList还有多少数据，有的话继续上传第一个，若没有，开始上传Retailtrade,操作与SmallSheet相似 )
     * 4.然后RN syncAction，拿到POS机需要重置的数据，进行重置。
     * 5.若网络不可用，则告知User下次启动项目的时候再进行重置
     */
    private void isSyncData() {
//        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(BaseEvent.EVENT_ID_PreSaleStage);
//        try {
//            resetBaseDataUtil.ResetBaseData(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastUtil.toast("下载基础资料失败！", ToastUtil.LENGTH_SHORT);
//        }
        resetBaseDataUtil.setEventID(BaseEvent.EVENT_ID_PreSaleStage);
        resetBaseDataUtil.initObject();
        try {
            if(!resetBaseDataUtil.ResetBaseData(true)) {
                Message msg = new Message();
                msg.what = 2;
                PlatForm.get().sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toast("重置基础资料失败！", ToastUtil.LENGTH_SHORT);
        }
    }

    /**
     * 当网络不可用的时候创建对话框告诉用户，下次开机再进行重置数据
     */
    private void createDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("提示")
//                .setMessage("当前网络不可用，请连接网络进行基础资料的下载！")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create().show();
//
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("当前网络不可用，请连接网络进行基础资料的下载！");
        ButtonType buttonTypeConfirm = new ButtonType("确定");
        alert.getButtonTypes().setAll(buttonTypeConfirm);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeConfirm){
        } else {
            throw new RuntimeException("黑客行为");
        }
    }

//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    Toast.makeText(PreSale1Activity.this, "重置基础资料完成！", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    Toast.makeText(PreSale1Activity.this, "网络故障，请重新下载！", Toast.LENGTH_SHORT).show();
//                    //失败后，需要将按钮恢复至可点击
//                    tvDownload.setEnabled(true);
//                    tvLogout.setEnabled(true);
//                    break;
//            }
//            return false;
//        }
//    });

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                ToastUtil.toast("重置基础资料完成！", ToastUtil.LENGTH_SHORT);
                break;
            case 2:
                ToastUtil.toast("网络故障，请重新下载！", ToastUtil.LENGTH_SHORT);
                //失败后，需要将按钮恢复至可点击
//                tvDownload.setEnabled(true);
//                tvLogout.setEnabled(true);
                tvDownload.setDisable(false);
                tvLogout.setDisable(false);
                break;
            case 3:
                loginActivity.setIntent(new Intent());
                try {
                    loginActivity.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("关闭售前页面");
                System.out.println("关闭售前页面");
                StageController.get().closeStge(StageType.PRESALE_STAGE.name()).unloadStage(StageType.PRESALE_STAGE.name());
                break;
        }
    }
}
