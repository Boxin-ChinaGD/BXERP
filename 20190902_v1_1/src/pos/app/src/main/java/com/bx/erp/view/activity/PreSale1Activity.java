package com.bx.erp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.CouponCodeHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.DownloadBaseDataMessageEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.ResetBaseDataUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreSale1Activity extends BaseActivity implements View.OnClickListener {
    private Logger log = Logger.getLogger(this.getClass());

    @BindView(R.id.download_basedata)
    TextView tvDownload;
    @BindView(R.id.logout)
    TextView tvLogout;
    @BindView(R.id.download_detail)
    TextView tvDownloadDetail;

    private static NetworkUtils networkUtils = new NetworkUtils();

    private static LogoutHttpBO logoutHttpBO = null;
    private static LogoutHttpEvent logoutHttpEvent = null;
    StringBuffer downloadDetail = new StringBuffer();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    @org.greenrobot.eventbus.Subscribe
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_sale);

        ButterKnife.bind(this);
        tvDownload.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(BaseEvent.EVENT_ID_PreSaleActivity);
        }
        //
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Message msg = new Message();
                msg.what = 2;
                Looper.prepare();
                handler.sendMessage(msg);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadBaseDataMessageEvent(DownloadBaseDataMessageEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            downloadDetail.append(String.valueOf(event.getMessage()));
            if (ResetBaseDataUtil.couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                ResetBaseDataUtil.couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                downloadDetail.append("基础资料下载完成");
                tvDownload.setEnabled(true);
                tvLogout.setEnabled(true);
            }
            log.info("------------------------------downloadDetail" + event.getMessage());
            tvDownloadDetail.setText(downloadDetail);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) throws Exception {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
            if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
                Intent intent = new Intent(PreSale1Activity.this, Login1Activity.class);
                PreSale1Activity.this.finish();
                startActivity(intent);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PreSaleActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_basedata:
                tvDownloadDetail.setText("");
                downloadDetail.delete(0, downloadDetail.length());
                if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(this.getApplicationContext())) {
                    //网络不可用的情况
                    createDialog();
                } else {
                    tvDownload.setEnabled(false);
                    tvLogout.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isSyncData();
                        }
                    }).start();
                }
                break;
            case R.id.logout:
                if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(this.getApplicationContext())) {
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);

                    logoutHttpBO.getHttpEvent().setId(BaseEvent.EVENT_ID_PreSaleActivity);
                    if (!logoutHttpBO.logoutAsync()) {
                        log.info("退出失败！");
                    }
                } else {
                    GlobalController.getInstance().setSessionID(null);
                    Intent intent = new Intent(PreSale1Activity.this, Login1Activity.class);
                    PreSale1Activity.this.finish();
                    startActivity(intent);
                }
                break;
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
        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(BaseEvent.EVENT_ID_PreSaleActivity);
        try {
            resetBaseDataUtil.ResetBaseData(true);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "下载基础资料失败！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当网络不可用的时候创建对话框告诉用户，下次开机再进行重置数据
     */
    private void createDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("当前网络不可用，请连接网络进行基础资料的下载！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(PreSale1Activity.this, "重置基础资料完成！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(PreSale1Activity.this, "网络故障，请重新下载！", Toast.LENGTH_SHORT).show();
                    //失败后，需要将按钮恢复至可点击
                    tvDownload.setEnabled(true);
                    tvLogout.setEnabled(true);
                    break;
            }
            return false;
        }
    });
}