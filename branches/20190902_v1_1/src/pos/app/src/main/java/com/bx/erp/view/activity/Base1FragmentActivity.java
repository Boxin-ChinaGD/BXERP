package com.bx.erp.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.AppApplication;
import com.bx.erp.BuildConfig;
import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.common.ActivityController;
import com.bx.erp.common.DayEndGenerateRetailTradeAggregationReceiver;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.service.ViewServer;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.NetworkStatusService;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.ScreenManager;
import com.bx.erp.utils.UPacketFactory;
import com.bx.erp.view.component.Number_EnglishBoardDialog;
import com.bx.erp.view.presentation.CustomerCommodityListPresentation;
import com.bx.erp.view.presentation.PaymentSuccessPresentation;
import com.bx.erp.view.presentation.WelcomePresentation;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import sunmi.ds.data.DataPacket;

public class Base1FragmentActivity extends FragmentActivity implements View.OnClickListener {
    private static Logger log = Logger.getLogger(FragmentActivity.class);
    private static NetworkUtils networkUtils = new NetworkUtils();
    @BindViews({R.id.sale_linear, R.id.inventory_linear, R.id.set_up_linear, R.id.btnQueryRetailTradeList_linear, R.id.bill_linear,
            R.id.report_loss_linear, R.id.logout_linear, R.id.printer_linear, R.id.desk_ticket_linear, R.id.data_processing_linear})
    List<LinearLayout> linearLayouts;
    @BindViews({R.id.sale, R.id.inventory, R.id.set_up, R.id.btnQueryRetailTradeList, R.id.bill, R.id.report_loss, R.id.logout, R.id.printer, R.id.desk_ticket, R.id.data_processing})
    List<TextView> textviewlist;
    @BindView(R.id.setting_item)
    LinearLayout settingItem;
    @BindView(R.id.show_time)
    TextView show_time;
    @BindView(R.id.staff_phone)
    TextView staffPhone;
    @BindView(R.id.sale_linear)
    LinearLayout sale_linear;

    private Fragment1Adapter fragment1Adapter;
//    private NetworkBroadcastReceiverHelper networkBroadcastReceiverHelper;
    /**
     * ????????????
     */
    private String workTimeEnd;

    /**
     * ??????ping nbr??????????????????????????????????????????????????????30?????????App??????25??????ping??????
     */
    private TimerTask timerTaskPing;
    /**
     * ??????timerTaskPing
     */
    private Timer timerPing;

    /**
     * ??????????????????
     */
    private CustomerScreenHandler handlerCustomerScreen;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;

    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    /**
     * ???????????????????????????
     */
    protected AppTimeThread appTimethread;
    protected boolean timeThreadCanRun = true;//????????????????????????????????????
    private DSKernel mDSKernel = null;
    protected CustomerCommodityListPresentation customerCommodityListPresentation = null;
    protected WelcomePresentation welcomePresentation = null;
    protected PaymentSuccessPresentation paymentSuccessPresentation = null;
    protected ScreenManager screenManager = ScreenManager.getInstance();

    private static StaffHttpBO staffHttpBO = null;
    private static StaffHttpEvent staffHttpEvent = null;


    /**
     * UI?????????????????????????????????????????????WxPay_TIME_OUT??????60???????????????????????????????????????????????????????????????UI?????????????????????
     */
    protected final long TIME_OUT = 30;
    /*
     * ????????????(???APP?????????)???????????????
     * */
    private IntentFilter intentFilter;
    private DayEndGenerateRetailTradeAggregationReceiver dayEndGenerateRetailTradeAggregationReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private AlertDialog cancelPayDialog;

    private Timer timerToStartUploadRetailTradeAggregation;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base1fragment);
        ButterKnife.bind(this);
        ActivityController.addActivity(this);
        ActivityController.setCurrentActivity(this);
        //????????????
        setLinstener();
        fragment1Adapter = new Fragment1Adapter(getSupportFragmentManager());
        replace_Fragment(0);//???????????????

//        registerBroadcast();
        Intent intent = new Intent(this, NetworkStatusService.class);
        startService(intent);
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, null);
            retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);

        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), null, staffHttpEvent);
        }
        staffHttpEvent.setHttpBO(staffHttpBO);

        initSdk();

        handlerCustomerScreen = new CustomerScreenHandler(this);

        //??????App?????????????????????????????????UI???
        appTimethread = new AppTimeThread();
        appTimethread.start();

        initTimers();

        log.debug("????????????????????????");
        ((AppApplication) getApplication()).startSyncThread();//??????????????????
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).addWindow(this);  // ????????????????????????HierarchyViewer ??????
        }

        //???????????????????????????????????????????????????????????????????????????
        getWindow().getAttributes().systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        staffPhone.setText(Constants.getCurrentStaff().getPhone());//?????????????????????
        localBroadcastManager = LocalBroadcastManager.getInstance(this);//??????localBroadcastManager??????
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initTimers() {   //?????????timer???????????????

        timerPing = new Timer();//????????????session?????????
        log.debug("???????????????ping nbr???Timer");
        /**
         * ??????ping nbr??????????????????????????????????????????????????????30?????????App??????25??????ping??????
         */
        timerTaskPing = new TimerTask() {
            @Override
            public void run() {
                try {
                    staffHttpBO.pingEx();
                    System.out.println("xxxxxxxxxxxxxx Ping NBR?????????????????????");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug(e.getMessage());
                }
            }
        };
        timerPing.schedule(timerTaskPing, 0, 25 * 60 * 1000);

        log.debug("??????????????????????????????Timer");
        timerToStartUploadRetailTradeAggregation = new Timer(true);
        registerDayEndGenerateRetailTradeAggregationBroadcast(timerToStartUploadRetailTradeAggregation);
    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback2);
        mDSKernel.removeReceiveCallback(mIReceiveCallback);
        mDSKernel.removeReceiveCallback(mIReceiveCallback2);
    }

    private IReceiveCallback mIReceiveCallback = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {

        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {

        }
    };
    private IReceiveCallback mIReceiveCallback2 = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {

        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        ActivityController.setCurrentActivity(this);
//        // ???????????????????????????????????????????????????????????????????????????UI????????????????????????
//        if (timerCheckInvalidSession != null) {
//            timerTaskCheckInvalidSession = new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        if (BaseHttpEvent.HttpRequestStatus == 1) {
//                            Message message = new Message();
//                            message.what = 1;
//                            listenerHandler.sendMessage(message);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            timerCheckInvalidSession.schedule(timerTaskCheckInvalidSession, 0, 1000);
//        }

        Number_EnglishBoardDialog.createdialog(this);//????????????????????????????????????????????????
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        //????????????
        if (BaseActivity.showCommList != null) {
            if (BaseActivity.showCommList.size() > 0) {
                if (customerCommodityListPresentation != null) {
                    customerCommodityListPresentation.show();
                }
            } else {
                if (welcomePresentation != null) {
                    welcomePresentation.show();
                }
            }
        } else {
            if (welcomePresentation != null) {
                welcomePresentation.show();
            }
        }

        //T1 host??????
        if (mDSKernel != null) {
            mDSKernel.checkConnection();
        } else {
            System.out.println("xxxxxxxxxxx??????initSdk");
            initSdk();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //???????????????
    @Override
    public void onBackPressed() {
        return;
    }

    //??????Fragment?????????
    protected void replace_Fragment(int item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.all_fragment, fragment1Adapter.getItem(item));
        transaction.commit();
    }

    private void setLinstener() {
        //????????????item????????????
        for (LinearLayout layout : linearLayouts) {
            layout.setOnClickListener(this);
        }
    }

    //????????????
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (!Main1Activity.bPaying) {
            switch (view.getId()) {
                case R.id.sale_linear://??????
                    setLeftItem_bg_And_textBold(view);//??????????????????
                    textviewlist.get(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//??????????????????
                    replace_Fragment(0);//??????Fragment
                    break;
                case R.id.inventory_linear://??????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(1);
                    break;
                case R.id.set_up_linear://??????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    settingItem.setVisibility(View.VISIBLE);//??????????????????
                    break;
                case R.id.btnQueryRetailTradeList_linear://??????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(3).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(2);
                    break;
                case R.id.bill_linear://??????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(4).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(5);
                    break;
                case R.id.report_loss_linear://??????
                    // TODO: 2020/4/17 ???????????????????????????????????????????????? 
//                    setLeftItem_bg_And_textBold(view);
//                    textviewlist.get(5).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                    replace_Fragment(4);
                    break;
                case R.id.logout_linear://??????
                    if (BaseActivity.retailTradeHoldBillList.size() > 0) {
                        Toast.makeText(this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        view.setId(R.id.bill_linear);
                        this.onClick(view);
                    } else {
                        setLeftItem_bg_And_textBold(view);
                        textviewlist.get(6).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                        CustomDialog dialog = new CustomDialog(this, R.layout.logout_dialog, true, 0.45, 0.33) {
                            @Override
                            public void do_sure() {
                                // ??????????????????
                                Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
                                workTimeEnd = Constants.getSimpleDateFormat().format(date);
                                Intent change_shifts_intent = new Intent(Base1FragmentActivity.this, RetailTradeAggregation1Activity.class);
                                change_shifts_intent.putExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd(), workTimeEnd);
                                startActivity(change_shifts_intent);
                                dismiss();
                            }
                        };
                        dialog.show();
                    }
                    break;
                case R.id.printer_linear://?????????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//????????????????????????
                    linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));
                    textviewlist.get(7).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                    break;
                case R.id.desk_ticket_linear://????????????
                    if (Constants.getCurrentStaff().getRoleID() != Constants.cashier_Role_ID) {
                        setLeftItem_bg_And_textBold(view);
                        textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//????????????????????????
                        linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));//??????????????????bg
                        textviewlist.get(7).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        replace_Fragment(3);
                    } else {
                        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.data_processing_linear://????????????
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//????????????????????????
                    linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));//??????????????????bg
                    textviewlist.get(7).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(4);
                    break;
            }
        }

    }

//    private void registerBroadcast() {
//        networkBroadcastReceiverHelper = new NetworkBroadcastReceiverHelper(this, new NetworkBroadcastReceiverHelper.OnNetworkStateChangedListener() {
//            @Override
//            public void onConnected() {
//                //????????????????????????
//                log.info("?????????????????????");
//            }
//
//            @Override
//            public void onDisConnected() {
//                //????????????????????????
//                log.info("?????????????????????");
//            }
//        });
//        networkBroadcastReceiverHelper.register();
//    }
//

    /**
     * ????????????
     */
    private void unregisterBroadcast() {
//        networkBroadcastReceiverHelper.unregister();
        if (dayEndGenerateRetailTradeAggregationReceiver != null) {
            dayEndGenerateRetailTradeAggregationReceiver.unregisterReceiver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        timeThreadCanRun = false;//???????????????????????????

        if (timerPing != null) {
            timerTaskPing.cancel();
            timerPing.purge();
        }

        timerToStartUploadRetailTradeAggregation.cancel();
        timerToStartUploadRetailTradeAggregation.purge();

        if (Number_EnglishBoardDialog.getdialog() != null) {
            Number_EnglishBoardDialog.getdialog().setdialogcancel();//activity??????????????????????????????????????????????????????activity
        }
        ActivityController.removeActivity(this);
    }

    EditText editText = null;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.all_fragment);
        if (fragment instanceof SmallSheet1Activity) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, event)) {//??????editText????????????
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        assert v != null;
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (editText != null) {
                            editText.clearFocus();
                        }
                    }
                }
                return super.dispatchTouchEvent(event);
            }
            // ????????????????????????????????????????????????TouchEvent???
            return getWindow().superDispatchTouchEvent(event) || onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.all_fragment);
        if (fragment instanceof RetrieveCommodityInventory1Activity) {
            //        //???????????????????????????6?????????????????????????????????????????????????????????????????????????????????????????????????????????
            log.info(event.getDevice().getName());

//        if (!"Virtual".equals(event.getDevice().getName())) { //?????????????????????????????????????????????????????????????????????
            if (event.getKeyCode() > 6) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {//?????????????????????????????????
                        return super.dispatchKeyEvent(event);
                    }
                    if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {//??????????????????????????????????????????
                        return super.dispatchKeyEvent(event);
                    }
                    this.onKeyDown(event.getKeyCode(), event);
                }
            } else {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//????????????????????????????????????
                    return super.dispatchKeyEvent(event);
                }
            }
            log.info("keycode:" + event.getKeyCode() + "  action" + event.getAction());

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.all_fragment);
        if (fragment instanceof RetrieveCommodityInventory1Activity) {
            ((RetrieveCommodityInventory1Activity) fragment1Adapter.getItem(1)).onKeyDownChild(keyCode, event);//????????????
        } else if (fragment instanceof Main1Activity) {
            ((Main1Activity) fragment1Adapter.getItem(0)).onKeyDownChild(keyCode, event);//????????????
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //????????????????????????location??????
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    //???????????????0.1?????????????????????
    public class AppTimeThread extends Thread {
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(100);
                    Message msg = new Message();
                    msg.what = 2;
                    listenerHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (timeThreadCanRun);
        }
    }

    //????????????????????????????????????????????????
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLeftItem_bg_And_textBold(View layout) {
        for (LinearLayout linearLayout : linearLayouts) {//??????????????????
            linearLayout.setBackground(getResources().getDrawable(R.drawable.left_item_unselect, null));
        }
        for (TextView textView : textviewlist) {//??????????????????
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        settingItem.setVisibility(View.INVISIBLE);//???????????????????????????
        layout.setBackground(getResources().getDrawable(R.drawable.left_item_select, null));
    }

    //???????????????
    abstract class CustomDialog extends Dialog {
        private Button sure, cancel;
        private int layout;//?????????????????????
        private boolean TouchOutside;
        private double width, height;

        public CustomDialog(@NonNull Context context, int layout, boolean TouchOutside, double width, double height) {
            super(context, layout);
            this.layout = layout;
            this.TouchOutside = TouchOutside;
            this.width = width;
            this.height = height;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //??????????????????
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.color.transparent);//????????????
            View decorView = window.getDecorView();      //??????????????????????????????
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            setContentView(layout);

            sure = findViewById(R.id.sure);
            cancel = findViewById(R.id.cancel);

            //??????dialog??????
            WindowManager manager = getWindowManager();
            Display display = manager.getDefaultDisplay();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            //  params.alpha = 0.9f;//??????????????????
            params.dimAmount = 0.42f;//??????????????????????????????
            params.width = (int) (display.getWidth() * width);//?????????
            params.height = (int) (display.getHeight() * height);//?????????
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            setCanceledOnTouchOutside(TouchOutside);//??????????????????

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    do_sure();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        public abstract void do_sure();
    }

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj = "?????????????????????";
            handlerCustomerScreen.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = "?????????????????????";
                    break;
                case VICE_SERVICE_CONN:
                    message.obj = "?????????????????????";
                    break;
                case VICE_APP_CONN:
                    message.obj = "?????????????????????";
                    break;
                default:
                    break;
            }
            handlerCustomerScreen.sendMessage(message);
        }
    };

    private static class CustomerScreenHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public CustomerScreenHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://??????????????????
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private Handler listenerHandler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BaseHttpEvent.HttpRequestStatus = 0;
                    GlobalController.getInstance().setSessionID(null);
                    log.info("sessionID?????????");
                    Constants.setCurrentStaff(null); //???????????????
                    //
                    ((AppApplication) getApplication()).exitFromSyncThread();
                    //
                    Intent intent = new Intent(Base1FragmentActivity.this, Login1Activity.class);
                    intent.putExtra("LoginInterceptor", 1);
                    intent.putExtra("LoginInterceptorWarn", BaseHttpEvent.HttpRequestWarnMsg);
                    ActivityController.finishAll();
                    startActivity(intent);
                    break;
                case 2:
                    long now = System.currentTimeMillis();
                    now = now + 100 + NtpHttpBO.TimeDifference;//????????????????????????sleep???0.1???????????????????????????Pos???????????????
                    if (show_time != null) {
                        show_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEEEEEEE").format(new Date(now)));
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    /**
     * T1 host??????????????????
     *
     * @param title
     * @param content
     */
    public void showTextInT1Host(String title, String content) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("content", content);
            String jsonStr = data.toString();
            DataPacket packet;
            if (!"".equals(title)) {
                packet = UPacketFactory.buildShowText(DSKernel.getDSDPackageName(), jsonStr, new ISendCallback() {
                    @Override
                    public void onSendSuccess(long taskId) {

                    }

                    @Override
                    public void onSendFail(int errorId, String errorInfo) {

                    }

                    @Override
                    public void onSendProcess(long totle, long sended) {

                    }
                });
            } else {
                packet = UPacketFactory.buildShowSingleText(DSKernel.getDSDPackageName(), jsonStr, new ISendCallback() {
                    @Override
                    public void onSendSuccess(long taskId) {

                    }

                    @Override
                    public void onSendFail(int errorId, String errorInfo) {

                    }

                    @Override
                    public void onSendProcess(long totle, long sended) {

                    }
                });
            }
            mDSKernel.sendData(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????presentation???????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void isShowToCustomer(Context context) {
        initPresentationData(context);
        if (BaseActivity.showCommList.size() > 0) {
            if (customerCommodityListPresentation != null) {
                customerCommodityListPresentation.show();
            }
        } else {
            if (welcomePresentation != null) {
                welcomePresentation.show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void initPresentationData(Context context) {
        screenManager.init(context);
        Display[] displays = screenManager.getDisplays();
        log.info("????????????" + displays.length);
        for (int i = 0; i < displays.length; i++) {
            log.info("??????" + displays[i]);
        }
        Display display = screenManager.getPresentationDisplays();
        if (display != null && !BaseActivity.isVertical) {
            welcomePresentation = new WelcomePresentation(this, display);
            customerCommodityListPresentation = new CustomerCommodityListPresentation(this, display);
            paymentSuccessPresentation = new PaymentSuccessPresentation(this, display);
        }
    }

    public static class ResponseDayEndGenerateRetailTradeAggregationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            log.debug("??????????????????");
            //?????????????????????????????????BaseActivity.retailTradeAggregation.getID() != null??????????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????
            if (BaseActivity.retailTradeAggregation.getID() != null) {
                if (intent.getAction().equals(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation)) {
                    log.debug("?????????????????????" + DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
                    createRetailTradeAggregationForNextDay();
                }
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????sqlite????????????????????????
     */
    private static void createRetailTradeAggregationForNextDay() {
        Date tmr = DatetimeUtil.get2ndDayStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        if (!DatetimeUtil.isAfterDate(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), tmr, 0)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //????????? BaseActivity.retailTradeAggregation???????????????
        if (BaseActivity.retailTradeAggregation != null) {
            BaseActivity.retailTradeAggregation.setWorkTimeStart(tmr);
            BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
            BaseActivity.retailTradeAggregation.setTradeNO(0);
            BaseActivity.retailTradeAggregation.setAmount(0.000000d);
            BaseActivity.retailTradeAggregation.setCashAmount(0.000000d);
            BaseActivity.retailTradeAggregation.setWechatAmount(0.000000d);
            //
            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailTradeAggregation.setPosID(Constants.posID);
            retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
            retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
            retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
            //
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
            if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
                log.error("?????????????????????????????????????????????");//TODO ?????????????????????
            }
        }
    }

    /**
     * ??????????????????????????????23:59:30?????????????????????????????????????????????00:00:00?????????????????????????????????????????????sqlite???
     */
    private void registerDayEndGenerateRetailTradeAggregationBroadcast(Timer timer) {
        Date targetTime = DatetimeUtil.mergeDateAndTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), Constants.START_TIME_UploadRetailTradeAggregation, Constants.DATE_FORMAT_Default);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //?????????????????????????????????BaseActivity.retailTradeAggregation.getID() != null??????????????????????????????????????????????????????
                if (BaseActivity.retailTradeAggregation.getID() != null) {
                    if (dayEndGenerateRetailTradeAggregationReceiver == null) {
                        intentFilter = new IntentFilter();
                        intentFilter.addAction(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
                        //
                        dayEndGenerateRetailTradeAggregationReceiver = new DayEndGenerateRetailTradeAggregationReceiver();
                        localBroadcastManager.registerReceiver(dayEndGenerateRetailTradeAggregationReceiver, intentFilter);//?????????????????????activity????????????EndDayAlertDialog??????????????????????????????
                    }
                    Intent intent = new Intent(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
                    localBroadcastManager.sendBroadcast(intent);

                    BaseActivity.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
                    //?????????????????????????????????
                    if (updateRetailTradeAggregationAtDayEnd()) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
                            log.info("????????????????????????????????????????????????????????????");
                            uploadRetailTradeAggregation(BaseActivity.retailTradeAggregation);
                        } else {
                            log.info("???????????????????????????????????????????????????");
                        }
                        //
                        printRetailTradeAggregation();
                    } else {
                        log.error("????????????????????????????????????????????????????????????");
                    }
                } else {
                    //???????????????????????????????????????????????????????????????????????????
                    RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
                    retailTradeAggregation.setPosID(Constants.posID);
                    retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                    retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
                    retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    if (createRetailTradeAggregationInSqlite(retailTradeAggregation)) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
                            uploadRetailTradeAggregation(retailTradeAggregation);
                        } else {
                            log.info("????????????????????????????????????????????????,?????????????????????");
                        }
                    } else {
                        log.error("??????????????????????????????????????????,???????????????????????????????????????????????????");
                    }
                    //
                    GlobalController.getInstance().setSessionID(null);
                    log.info("sessionID?????????");
                    //
                    ((AppApplication) getApplication()).exitFromSyncThread();
                    //
                    initialization();//??????????????????????????????????????????call??????
                    //
                    ActivityController.finishAll();
                    //
                    Intent intent = new Intent(getApplicationContext(), Login1Activity.class);
                    startActivity(intent);
                }
            }
        }, targetTime, 24 * 60 * 60 * 1000);
    }

    private boolean updateRetailTradeAggregationAtDayEnd() {
        RetailTradeAggregation updateRetailTradeAggregation = null;
        try {
            updateRetailTradeAggregation = (RetailTradeAggregation) BaseActivity.retailTradeAggregation.clone();
            updateRetailTradeAggregation.setAmount(GeneralUtil.sum(updateRetailTradeAggregation.getCashAmount(), updateRetailTradeAggregation.getWechatAmount()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            log.error("?????????????????????????????????????????????????????????????????????,????????????=" + e.getMessage());
            return false;
        }
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        return retailTradeAggregationSQLiteBO.updateSync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradeAggregation);
    }

    private void uploadRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
        /*???????????????????????????????????????*/
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        //
        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.info("?????????????????????????????????????????????");
        }
        long lTimeOut = TIME_OUT;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) { //
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) { //
            log.error("????????????????????????????????????????????????");
            //...
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("??????????????????????????????????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
            //...
        }
    }

    private void printRetailTradeAggregation() {
        //??????????????????????????????
        try {
            AidlUtil.getInstance().printText("???????????????", 35, false, 17, false);
            AidlUtil.getInstance().printText("?????????????????????????????????????????????????????????????????????????????????????????????", 20, false, 17, false);
            AidlUtil.getInstance().printDivider("-");
            AidlUtil.getInstance().printText("???????????????" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
            AidlUtil.getInstance().printText("???????????????" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeEnd()), 25, false, 51, false);
            AidlUtil.getInstance().printText("????????????" + Constants.getCurrentStaff().getName(), 30, false, 51, false);
            AidlUtil.getInstance().linewrap(1);
            AidlUtil.getInstance().printText("------????????????------", 35, false, 17, false);
            AidlUtil.getInstance().printText("???????????????" + BaseActivity.retailTradeAggregation.getTradeNO(), 30, false, 51, false);
            AidlUtil.getInstance().printText("????????????" + BaseActivity.retailTradeAggregation.getAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("????????????" + BaseActivity.retailTradeAggregation.getReserveAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("???????????????" + BaseActivity.retailTradeAggregation.getCashAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("???????????????" + BaseActivity.retailTradeAggregation.getWechatAmount(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("??????????????????" + showAlipayAmount.getText().toString(), 30, false, 51, false);
            AidlUtil.getInstance().printDivider("-");
            AidlUtil.getInstance().printText("??????????????????", 30, false, 51, false);
            AidlUtil.getInstance().linewrap(8);
            AidlUtil.getInstance().cutPaper();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean createRetailTradeAggregationInSqlite(RetailTradeAggregation retailTradeAggregation) {
        RetailTradeAggregation retailTradeAggregationCreate = (RetailTradeAggregation) retailTradeAggregationSQLiteBO.createSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation);
        if (retailTradeAggregationCreate != null && retailTradeAggregationCreate.getID() != null) {
            return true;
        }
        return false;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     */
    protected void initialization() {
        BaseActivity.retailTradeAggregation = null;
        BaseActivity.showCommList = new ArrayList<>();
        BaseActivity.retailTrade = new RetailTrade();//?????????????????????
        BaseActivity.lastRetailTrade = null;
        BaseActivity.lastRetailTradePaymentType = "";
        BaseActivity.lastRetailTradeChangeMoney = 0.000000d;
        BaseActivity.lastRetailTradeAmount = 0.000000d;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                System.out.println("???????????????????????????????????????");
            } else {
                System.out.println("???????????????????????????????????????");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseActivity.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("???????????????????????????????????????????????????=" + BaseActivity.retailTradeAggregation);
                } else {
                    log.error("?????????????????????????????????");
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    System.out.println("???????????????????????????????????????=" + event.getBaseModel1());
                } else {
                    log.error("???????????????????????????");
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }
}
