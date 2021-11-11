//package com.bx.erp.view.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.R;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BarcodesHttpEvent;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.BrandHttpEvent;
//import com.bx.erp.event.CommodityCategoryHttpEvent;
//import com.bx.erp.event.CommodityHttpEvent;
//import com.bx.erp.event.ConfigGeneralHttpEvent;
//import com.bx.erp.event.PackageUnitHttpEvent;
//import com.bx.erp.event.PromotionHttpEvent;
//import com.bx.erp.event.PromotionSQLiteEvent;
//import com.bx.erp.event.SmallSheetHttpEvent;
//import com.bx.erp.event.UI.BarcodesSQLiteEvent;
//import com.bx.erp.event.UI.BrandSQLiteEvent;
//import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
//import com.bx.erp.event.UI.CommoditySQLiteEvent;
//import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
//import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
//import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.utils.NetworkUtils;
//import com.bx.erp.utils.ResetBaseDataUtil;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class ResetBaseDataActivity extends BaseActivity implements View.OnClickListener {
//    private Logger log = Logger.getLogger(this.getClass());
//    @BindView(R.id.sync_base_data)
//    Button sync;
//    @BindView(R.id.reset_back)
//    TextView resetBack;
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sync_base_data);
//
//        ButterKnife.bind(this);
//
//        sync.setOnClickListener(this);
//        resetBack.setOnClickListener(this);
//    }
//
//    private static NetworkUtils networkUtils = new NetworkUtils();
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBrandHttpEvent(BrandHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommodityHttpEvent(CommodityHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPromotionHttpEvent(PromotionHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                Message msg = new Message();
//                msg.what = 2;
//                handler.sendMessage(msg);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_ResetBaseDataActivity) {
//            event.onEvent();
//            if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                log.info("重置基础资料完成！");
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    /**
//     * 1.判断网络是否可用
//     * 2.网络可用，启动等待UI
//     * 3.先查找需要上传到服务器的数据（RetailTrade，SmallSheet）将其上传到服务器
//     * ( 上传：先上传小票格式List的第一个元素，调用httpBO请求服务器，成功后到SQLiteEvent，remowe掉List的第一个元素（刚刚上传的元素），
//     * 检查tempSmallSheetFrameList还有多少数据，有的话继续上传第一个，若没有，开始上传Retailtrade,操作与SmallSheet相似 )
//     * 4.然后RN syncAction，拿到POS机需要重置的数据，进行重置。
//     * 5.若网络不可用，则告知User下次启动项目的时候再进行重置
//     */
//    private void isSyncData() {
//        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(BaseEvent.EVENT_ID_ResetBaseDataActivity);
//        try {
//            resetBaseDataUtil.ResetBaseData(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(appApplication, "重置基础资料失败！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    /**
//     * 当网络不可用的时候创建对话框告诉用户，下次开机再进行重置数据
//     */
//    private void createDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("提示")
//                .setMessage("网络故障，待下次启动时再进行数据重置！")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(ResetBaseDataActivity.this, MainActivity.class);
//                        intent.putExtra("isShow", 1);
//                        ResetBaseDataActivity.this.finish();
//                        startActivity(intent);
//                    }
//                }).create().show();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.sync_base_data:
//                if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(this.getApplicationContext())) {
//                    //网络不可用的情况
//                    createDialog();
//                } else {
//                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            isSyncData();
//                        }
//                    }).start();
//                }
//                break;
//            case R.id.reset_back:
//                Intent intent = new Intent(ResetBaseDataActivity.this, SetupActivity.class);
//                intent.putExtra("isShow", 1);
//                ResetBaseDataActivity.this.finish();
//                startActivity(intent);
//                break;
//        }
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    Toast.makeText(ResetBaseDataActivity.this, "重置基础资料完成！", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//                case 2:
//                    Toast.makeText(ResetBaseDataActivity.this, "网络故障，请重新下载！", Toast.LENGTH_SHORT).show();
//                    closeLoadingDialog(loadingDailog);
//                    break;
//            }
//        }
//    };
//}
