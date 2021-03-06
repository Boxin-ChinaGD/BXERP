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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataStage) {
            event.onEvent();
            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                log.info("???????????????????????????");
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    /**
     * 1.????????????????????????
     * 2.???????????????????????????UI
     * 3.?????????????????????????????????????????????RetailTrade???SmallSheet???????????????????????????
     * ( ??????????????????????????????List???????????????????????????httpBO??????????????????????????????SQLiteEvent???remowe???List????????????????????????????????????????????????
     * ??????tempSmallSheetFrameList??????????????????????????????????????????????????????????????????????????????Retailtrade,?????????SmallSheet?????? )
     * 4.??????RN syncAction?????????POS??????????????????????????????????????????
     * 5.??????????????????????????????User??????????????????????????????????????????
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
            ToastUtil.toast("???????????????????????????", ToastUtil.LENGTH_SHORT);
        }
    }


    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     */
    private void createDialog() {
//        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setTitle("??????")
//                .setMessage("?????????????????????????????????????????????????????????")
//                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
        alert.setTitle("??????");
        alert.setHeaderText(null);
        alert.setContentText("?????????????????????????????????????????????????????????");
        ButtonType buttonTypeConfirm = new ButtonType("??????");
        alert.getButtonTypes().setAll(buttonTypeConfirm);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeConfirm){
            alert.close();
        } else {
            throw new RuntimeException("????????????");
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
//            //????????????????????????
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
            //????????????????????????
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
//                    Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//                case 2:
//                    Toast.makeText(getActivity(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//            }
//        }
//    };

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                ToastUtil.toast("???????????????????????????", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
                break;
            case 2:
                ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
                break;
        }
    }
}
