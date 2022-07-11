//package com.bx.erp.common;
//
//import android.app.Stage;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
//import android.widget.TextView;
//
//import com.bx.erp.R;
//import com.bx.erp.helper.Constants;
//import com.bx.erp.view.stage.MainStage;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * MainStage是栈底的Stage，负责发送广播，同时一定会收到收银汇总的广播并进行处理。其它Stage收到也会进行处理。
// */
//public class DayEndGenerateRetailTradeAggregationReceiver extends BroadcastReceiver {
//    private TextView tipMsg;
//    private AlertDialog showInDayEndAlertDialog;
//
//    public static final String ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation = "ResponseBroadcastDayEndGenerateRetailTradeAggregation";
//    public static final String ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation = "BroadcastDayEndGenerateRetailTradeAggregation";
//
//    /*
//    * 当倒计时完成后，往MainStage回应一条广播，所用到的组件
//    * */
//    private IntentFilter intentFilter;
//    private MainStage.ResponseDayEndGenerateRetailTradeAggregationReceiver responseDayEndGenerateRetailTradeAggregationReceiver;
//    private LocalBroadcastManager localBroadcastManager;
//
//    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//    @Override
//    public void onReceive(Context context, final Intent intent) {
//        Stage stage = StageStage.getCurrentStage();
//
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
//        responseDayEndGenerateRetailTradeAggregationReceiver = new MainStage.ResponseDayEndGenerateRetailTradeAggregationReceiver();
//        localBroadcastManager = LocalBroadcastManager.getInstance(stage);
//        localBroadcastManager.registerReceiver(responseDayEndGenerateRetailTradeAggregationReceiver,intentFilter);
//        final int MSG_CHANGE_TEXT_VIWE = 100001;
//
//        final Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (msg.what == MSG_CHANGE_TEXT_VIWE) {
//                    if (msg.arg1 > 0) {
//                        tipMsg.setText("请等待" + msg.arg1 + "秒...");//动态显示倒计时
//                    }
//                }
//                return false;
//            }
//        });
//
//
//        final Timer closeAlertDialogTimer = new Timer(true);
//        TimerTask closeAlertDialogTimerTask = new TimerTask() {
//            int closeAlertDialogSecond = Constants.WAITING_TIME_UploadRetailTradeAggregation;
//
//            @Override
//            public void run() {
//                if (closeAlertDialogSecond > 0) {
//                    Message msg = new Message();
//                    msg.what = MSG_CHANGE_TEXT_VIWE;
//                    msg.arg1 = closeAlertDialogSecond;
//                    handler.sendMessage(msg);
//                    closeAlertDialogSecond--;
//                } else {
//                    closeAlertDialogTimer.cancel();
//                    if (showInDayEndAlertDialog != null) {
//                        tipMsg.clearComposingText();
//                        showInDayEndAlertDialog.dismiss();
//                        Intent intent = new Intent(ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
//                        localBroadcastManager.sendBroadcast(intent);
//                    }
//                }
//            }
//        };
//        closeAlertDialogTimer.schedule(closeAlertDialogTimerTask, 0/*delay*/, 1000/*period*/); //0秒后，每隔1秒发送消息给UI，以刷新秒数
//
//        tipMsg = new TextView(stage);
//        tipMsg.setPadding(10, 10, 10, 10);
//        showInDayEndAlertDialog = new AlertDialog.Builder(stage)
//                .setTitle("系统当前正在统计当日数据，请稍等")
//                .setIcon(R.drawable.wait)
//                .setView(tipMsg)
//                .setCancelable(false)
//                .create();
//        showInDayEndAlertDialog.setCanceledOnTouchOutside(false);
//        showInDayEndAlertDialog.show();
//    }
//
//    public void unregisterReceiver(){
//        if (localBroadcastManager != null){
//            localBroadcastManager.unregisterReceiver(responseDayEndGenerateRetailTradeAggregationReceiver);
//        }
//    }
//}