//package com.bx.erp.view.activity;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.AppApplication;
//import com.bx.erp.R;
//import com.bx.erp.bo.BaseHttpBO;
//import com.bx.erp.bo.BaseSQLiteBO;
//import com.bx.erp.bo.LogoutHttpBO;
//import com.bx.erp.bo.NtpHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
//import com.bx.erp.bo.RetailTradeHttpBO;
//import com.bx.erp.bo.RetailTradePromotingHttpBO;
//import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
//import com.bx.erp.bo.RetailTradeSQLiteBO;
//import com.bx.erp.common.ActivityController;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.LogoutHttpEvent;
//import com.bx.erp.event.RetailTradeAggregationEvent;
//import com.bx.erp.event.RetailTradeAggregationHttpEvent;
//import com.bx.erp.event.RetailTradeHttpEvent;
//import com.bx.erp.event.RetailTradePromotingHttpEvent;
//import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
//import com.bx.erp.event.UI.BaseSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
//import com.bx.erp.helper.Constants;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.RetailTradeAggregation;
//import com.bx.erp.utils.GeneralUtil;
//import com.bx.erp.utils.NetworkUtils;
//import com.silencedut.taskscheduler.TaskScheduler;
//import com.sunmi.printerhelper.utils.AidlUtil;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class RetailTradeAggregationActivity extends BaseActivity implements View.OnClickListener {
//    private Logger log = Logger.getLogger(this.getClass());
//    @BindView(R.id.change_shifts_back)
//    TextView change_shifts_back;
//    @BindView(R.id.change_shifts_record)
//    TextView change_shifts_record;
//    @BindView(R.id.change_shifts)
//    TextView change_shifts;
//    @BindView(R.id.tradeNo_layout)
//    LinearLayout tradeNo_layout;
//    @BindView(R.id.amount_layout)
//    LinearLayout amount_layout;
//    @BindView(R.id.reserveAmount_layout)
//    LinearLayout reserveAmount_layout;
//    @BindView(R.id.cashAmount_layout)
//    LinearLayout cashAmount_layout;
//    @BindView(R.id.wechatAmount_layout)
//    LinearLayout wechatAmount_layout;
//    //    @BindView(R.id.alipayAmount_layout)
////    LinearLayout alipayAmount_layout;
//    @BindView(R.id.print_shift_schedule_number)
//    LinearLayout print_shift_schedule_number;
//    @BindView(R.id.shift_schedule_number)
//    TextView shift_schedule_number;
//    @BindView(R.id.showTradeNo)
//    TextView showTradeNo;
//    @BindView(R.id.showAmount)
//    TextView showAmount;
//    @BindView(R.id.showReserveAmount)
//    TextView showReserveAmount;
//    @BindView(R.id.showCashAmount)
//    TextView showCashAmount;
//    @BindView(R.id.showWechatAmount)
//    TextView showWechatAmount;
//    //    @BindView(R.id.showAlipayAmount)
////    TextView showAlipayAmount;
//    @BindView(R.id.showWorkTimeEnd)
//    TextView showWorkTimeEnd;
//    @BindView(R.id.operator)
//    TextView operator;
//    private String tradeNo;//????????????
//    private String amount;//?????????
//    private String reserveAmount;//?????????
//    private String cashAmount;//????????????
//    private String wechatAmount;//????????????
//    private String alipayAmount;//???????????????
//    private String workTimeEnd;//????????????
//
//    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
//    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
//
//    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
//    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
//    private static RetailTradeAggregationEvent retailTradeAggregationEvent = null;
//    //
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
//    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
//    private static RetailTradeHttpBO retailTradeHttpBO = null;
//    //
//    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
//    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
//    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
//    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
//    //
//    private static LogoutHttpBO logoutHttpBO = null;
//    private static LogoutHttpEvent logoutHttpEvent = null;
//
//    private static NetworkUtils networkUtils = new NetworkUtils();
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.retailtradeaggregation_layout);
//
//        ButterKnife.bind(this);
//
//        initObject();
//        setClickEvent();
//        receiveValue();
//        showValue();
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
//                //?????????????????????,????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                initialization();
//                log.info("?????????????????????,?????????????????????");
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
//                returnToLoginActivity(); //???????????????????????????????????????????????????
//            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
//                Toast.makeText(getApplicationContext(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("RetailTradeAggregationActivity.onRetailTradeHttpEvent()?????????????????????????????????SyncThread????????????");
//            }
//            log.debug("???Event?????????SyncThread??????????????????RetailTradeAggregationActivity.onRetailTradeHttpEvent()????????????");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
//        log.info("#########################################################RetailTradeAggregationActivity onRetailTradeSQLiteEvent");
//        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
//            event.onEvent();
//            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
//                Toast.makeText(getApplicationContext(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("RetailTradeAggregationActivity.onRetailTradeSQLiteEvent()?????????????????????????????????SyncThread????????????");
//            }
//            log.debug("???Event?????????SyncThread??????????????????RetailTradeAggregationActivity.onRetailTradeSQLiteEvent()????????????");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onLogoutHttpEvent(LogoutHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
//        }
//        if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
//            returnToLoginActivity();
//        } else {
//            //?????????????????????,????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//            initialization();
//            log.info("?????????????????????,?????????????????????");
//        }
//    }
//
//    private void returnToLoginActivity() {
//        ((AppApplication) getApplication()).exitFromSyncThread();
//        closeLoadingDialog(loadingDailog);
//        //
//        initialization();//??????????????????????????????????????????call??????
//
//        ActivityController.finishAll();
//        log.info("???????????????Activity finish()??????????????????loginActivity");
//
//        Intent logoutIntent = new Intent(RetailTradeAggregationActivity.this, LoginActivity.class);
//        startActivity(logoutIntent);
//    }
//
//    private void initObject() {
//        if (retailTradeAggregationSQLiteEvent == null) {
//            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
//            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        if (retailTradeAggregationHttpEvent == null) {
//            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
//            retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        if (retailTradeAggregationSQLiteBO == null) {
//            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        }
//        if (retailTradeAggregationHttpBO == null) {
//            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
//        }
//
//        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
//        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
//        //
//        if (retailTradeSQLiteEvent == null) {
//            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        if (retailTradeHttpEvent == null) {
//            retailTradeHttpEvent = new RetailTradeHttpEvent();
//            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        if (retailTradeSQLiteBO == null) {
//            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        if (retailTradeHttpBO == null) {
//            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//
//        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        //
//        if (retailTradePromotingSQLiteEvent == null) {
//            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
//            retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        //
//        if (logoutHttpEvent == null) {
//            logoutHttpEvent = new LogoutHttpEvent();
//            logoutHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//        if (logoutHttpBO == null) {
//            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
//        }
//        logoutHttpEvent.setHttpBO(logoutHttpBO);
//        //
//        if (retailTradeAggregationEvent == null) {
//            retailTradeAggregationEvent = new RetailTradeAggregationEvent();
//            retailTradeAggregationEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
//        }
//    }
//
//    /**
//     * ???????????????????????????
//     */
//    private void setClickEvent() {
//        change_shifts_back.setOnClickListener(this);
//        change_shifts_record.setOnClickListener(this);
//        change_shifts.setOnClickListener(this);
//        tradeNo_layout.setOnClickListener(this);
//        amount_layout.setOnClickListener(this);
//        reserveAmount_layout.setOnClickListener(this);
//        cashAmount_layout.setOnClickListener(this);
//        wechatAmount_layout.setOnClickListener(this);
////        alipayAmount_layout.setOnClickListener(this);
//        print_shift_schedule_number.setOnClickListener(this);
//    }
//
//    /**
//     * ????????????Activity??????????????????????????????????????????????????????????????????(???????????????????????????????????????????????????????????????......)
//     */
//    private void receiveValue() {
//        tradeNo = String.valueOf(BaseActivity.retailTradeAggregation.getTradeNO());
//        amount = String.valueOf(BaseActivity.retailTradeAggregation.getAmount());
//        reserveAmount = String.valueOf(BaseActivity.retailTradeAggregation.getReserveAmount());
//        cashAmount = String.valueOf(BaseActivity.retailTradeAggregation.getCashAmount());
//        wechatAmount = String.valueOf(BaseActivity.retailTradeAggregation.getWechatAmount());
//        workTimeEnd = getIntent().getStringExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd());
//    }
//
//    /**
//     * ???UI????????????????????????
//     */
//    private void showValue() {
//        showTradeNo.setText("null".equals(tradeNo) ? "0" : tradeNo);
//        showAmount.setText(GeneralUtil.formatToShow(amount == null ? 0.000000d : Double.valueOf(amount)));
//        showReserveAmount.setText(reserveAmount);
//        showCashAmount.setText(GeneralUtil.formatToShow(cashAmount == null ? 0.000000d : Double.valueOf(cashAmount)));
//        showWechatAmount.setText(GeneralUtil.formatToShow(wechatAmount == null ? 0.000000d : Double.valueOf(wechatAmount)));
////        showAlipayAmount.setText(alipayAmount == null ? "0" : alipayAmount);
//        showWorkTimeEnd.setText(workTimeEnd);
//        operator.setText(Constants.getCurrentStaff().getName());
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.change_shifts_back:
//                break;
//            case R.id.change_shifts_record:
//                break;
//            case R.id.change_shifts: //?????????????????????????????????????????????????????????????????????UI???,????????????SQLite???????????????????????????????????????????????????????????????????????????
//                change_shifts.setEnabled(false);
//                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
//                //????????????????????????
//                if (Constants.getCurrentStaff().getRoleID() != Constants.preSale_Role_ID) {
//                    int print_number = Integer.valueOf(shift_schedule_number.getText().toString());
//                    for (int i = 0; i < print_number; i++) {
//                        try {
//                            AidlUtil.getInstance().printText("???????????????", 30, false, 17, false);
//                            AidlUtil.getInstance().printDivider("-");
//                            AidlUtil.getInstance().printText("???????????????" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
//                            AidlUtil.getInstance().printText("???????????????" + workTimeEnd, 25, false, 51, false);
//                            AidlUtil.getInstance().printText("????????????" + operator.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().linewrap(1);
//                            AidlUtil.getInstance().printText("------????????????------", 35, false, 17, false);
//                            AidlUtil.getInstance().printText("???????????????" + showTradeNo.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("????????????" + showAmount.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("????????????" + showReserveAmount.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("???????????????" + showCashAmount.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("???????????????" + showWechatAmount.getText().toString(), 30, false, 51, false);
////                            AidlUtil.getInstance().printText("??????????????????" + showAlipayAmount.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printDivider("-");
//                            AidlUtil.getInstance().printText("??????????????????", 30, false, 51, false);
//                            AidlUtil.getInstance().linewrap(8);
//                            AidlUtil.getInstance().cutPaper();
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(getApplicationContext())) {
//                        //???????????????,????????????,??????????????????
//                        returnToLoginActivity();
//                    } else {
//                        //...??????2????????????TaskScheduler???????????????post??????XXX?????????
//                        TaskScheduler.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    //?????????????????????
//                                    retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//                                    retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
//                                    if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
//                                        log.error("??????????????????????????????");
//                                    }
//
//                                    long lTimeOut = TIME_OUT;
//                                    while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                                            retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//                                        Thread.sleep(1000);
//                                    }
//                                    if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                                            retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//                                        log.info("????????????????????????????????????????????????????????????");
//                                    }
//
//                                    //????????????????????????????????????????????????,???????????????????????????????????????
//                                    log.info("??????????????????????????????=" + BaseActivity.retailTradeAggregation);
//                                    BaseActivity.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
//                                    uploadRetailTradeAggregation(BaseActivity.retailTradeAggregation);
//                                    if (GlobalController.getInstance().getSessionID() != null) {
//                                        if (!logoutHttpBO.logoutAsync()) {
//                                            log.error("??????????????????! ");
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    log.error("????????????????????????" + e.getMessage());
//                                }
//                                //...???????????????2???MSG2??????????????????????????????POST EVENT
////                                EventBus.getDefault().post(retailTradeAggregationEvent);
//                            }
//                        });
//                    }
//                } else {
//                    if (!logoutHttpBO.logoutAsync()) {
//                        log.info("??????????????????! ");
//                    }
//                }
//                break;
//            case R.id.tradeNo_layout:
//                break;
//            case R.id.amount_layout:
//                break;
//            case R.id.reserveAmount_layout:
//                break;
//            case R.id.cashAmount_layout:
//                break;
//            case R.id.wechatAmount_layout:
//                break;
////            case R.id.alipayAmount_layout:
////                break;
//            case R.id.print_shift_schedule_number:
//                clickPrintNumber();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void uploadRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
//        log.info("createRetailTradeAggregationToSQL++++retailTradeAggregation+" + retailTradeAggregation);
//        /*??????????????????????????????????????? TODO ??????????????????????????????????????????????????????????????????????????? */
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation); //??????????????????????????????????????????????????????????????????????????????????????????SQLite??????????????????SQLite????????????????????????BaseModel1??????????????????BaseModel2???????????????
//        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
//            log.info("?????????????????????????????????????????????");
//        }
//        long lTimeOut = TIME_OUT;
//        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) { //
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) { //
//            log.error("????????????????????????????????????????????????");
//            //...
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("??????????????????????????????????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
//            //...
//        }
//    }
//
//    /**
//     * ?????????????????????????????????
//     */
//    private void clickPrintNumber() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenWidth = dm.widthPixels;//????????????
//        int screenHeight = dm.heightPixels;//????????????
//        //
//        AlertDialog.Builder shift_schedule_number_builder = new AlertDialog.Builder(RetailTradeAggregationActivity.this);
//        View shift_schedule_number_view = View.inflate(getApplicationContext(), R.layout.print_shift_schedule_number_dialog, null);
//        shift_schedule_number_builder.setCancelable(true);
//        shift_schedule_number_builder.setView(shift_schedule_number_view);
//        final AlertDialog shift_schedule_number_dialog = shift_schedule_number_builder.create();
//        shift_schedule_number_dialog.show();
//        WindowManager.LayoutParams params = shift_schedule_number_dialog.getWindow().getAttributes();
//        params.width = (int) (screenWidth * 0.3);
//        params.height = (int) (screenHeight * 0.52);
//        shift_schedule_number_dialog.getWindow().setAttributes(params);
//        //
//        RelativeLayout print_one = shift_schedule_number_view.findViewById(R.id.print_one);
//        RelativeLayout print_two = shift_schedule_number_view.findViewById(R.id.print_two);
//        RelativeLayout print_three = shift_schedule_number_view.findViewById(R.id.print_three);
//        RelativeLayout print_four = shift_schedule_number_view.findViewById(R.id.print_four);
//        TextView cancel = shift_schedule_number_view.findViewById(R.id.cancel);
//        final ImageView selected_one = shift_schedule_number_view.findViewById(R.id.selected_one);
//        final ImageView selected_two = shift_schedule_number_view.findViewById(R.id.selected_two);
//        final ImageView selected_three = shift_schedule_number_view.findViewById(R.id.selected_three);
//        final ImageView selected_four = shift_schedule_number_view.findViewById(R.id.selected_four);
//        print_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.VISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("1");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.VISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("2");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_three.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.VISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("3");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_four.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.VISIBLE);
//                shift_schedule_number.setText("4");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//    }
//}
