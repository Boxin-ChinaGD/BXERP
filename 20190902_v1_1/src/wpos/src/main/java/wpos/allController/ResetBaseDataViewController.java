package wpos.allController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.model.Message;
import wpos.utils.PlatForm;
import wpos.allEnum.ThreadMode;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.utils.NetworkUtils;
import wpos.utils.ResetBaseDataUtil;
import wpos.utils.ToastUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Component("resetBaseDataViewController")
public class ResetBaseDataViewController implements PlatFormHandlerMessage {

//    @BindView(R.id.sync_base_data)
//    Button sync;

    @FXML
    Button sync_base_data;
//    Unbinder unbinder;
    private Logger log = Logger.getLogger(this.getClass());
    private static NetworkUtils networkUtils = new NetworkUtils();
    AllFragmentViewController viewController;
    public static Thread resetThread;

    public ResetBaseDataViewController(){}

    public ResetBaseDataViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
    }

    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
    }

    @Resource
    private ResetBaseDataUtil resetBaseDataUtil;

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sync_base_data1, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        return view;
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
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
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                log.info("重置基础资料完成！");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

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
//        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(BaseEvent.EVENT_ID_ResetBaseDataStage);
        resetBaseDataUtil.setEventID(BaseEvent.EVENT_ID_ResetBaseDataStage);
        resetBaseDataUtil.initObject();
        try {
            if(!resetBaseDataUtil.ResetBaseData(false)) {
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
//        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setTitle("提示")
//                .setMessage("网络故障，待下次启动时再进行数据重置！")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Intent intent = new Intent(ResetBaseDataActivity.this, MainActivity.class);
////                        intent.putExtra("isShow", 1);
////                        ResetBaseDataActivity.this.finish();
////                        startActivity(intent);
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//        alertDialog.show();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("网络故障，待下次启动时再进行数据重置！");
        ButtonType buttonTypeConfirm = new ButtonType("确定");
        alert.getButtonTypes().setAll(buttonTypeConfirm);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeConfirm){
            alert.close();
        } else {
            throw new RuntimeException("黑客行为");
        }
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
////        unbinder.unbind();
//    }

//    @OnClick(R.id.sync_base_data)
//    public void onViewClicked() {
//        if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(getActivity().getApplicationContext())) {
//            //网络不可用的情况
//            createDialog();
//        } else {
//            loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    isSyncData();
//                }
//            }).start();
//        }
//    }

    public void sync_base_data() {
        if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible()) {
            //网络不可用的情况
            createDialog();
        } else {
            viewController.showLoadingDialog();
            resetThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    isSyncData();
                }
            });
            resetThread.start();
        }
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    Toast.makeText(getActivity(), "重置基础资料完成！", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//                case 2:
//                    Toast.makeText(getActivity(), "网络故障，请重新下载！", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//            }
//        }
//    };

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                ToastUtil.toast("重置基础资料完成！", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
                break;
            case 2:
                ToastUtil.toast("网络故障，请重新下载！", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
                break;
        }
    }
}
