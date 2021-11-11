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
     * 交班时间
     */
    private String workTimeEnd;

    /**
     * 定时ping nbr以保持会话不过期。服务器的过期时间是30分钟。App每隔25分钟ping一次
     */
    private TimerTask timerTaskPing;
    /**
     * 参考timerTaskPing
     */
    private Timer timerPing;

    /**
     * 客显，即副屏
     */
    private CustomerScreenHandler handlerCustomerScreen;
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;

    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    /**
     * 更新界面时间的线程
     */
    protected AppTimeThread appTimethread;
    protected boolean timeThreadCanRun = true;//标志时间线程是否可以运行
    private DSKernel mDSKernel = null;
    protected CustomerCommodityListPresentation customerCommodityListPresentation = null;
    protected WelcomePresentation welcomePresentation = null;
    protected PaymentSuccessPresentation paymentSuccessPresentation = null;
    protected ScreenManager screenManager = ScreenManager.getInstance();

    private static StaffHttpBO staffHttpBO = null;
    private static StaffHttpEvent staffHttpEvent = null;


    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;
    /*
     * 本地广播(本APP范围内)使用的组件
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
        //设置监听
        setLinstener();
        fragment1Adapter = new Fragment1Adapter(getSupportFragmentManager());
        replace_Fragment(0);//展示销售页

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

        //开启App时间线程，以显示时间在UI上
        appTimethread = new AppTimeThread();
        appTimethread.start();

        initTimers();

        log.debug("正在开启同步线程");
        ((AppApplication) getApplication()).startSyncThread();//开启同步线程
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).addWindow(this);  // 使得真机也能使用HierarchyViewer 工具
        }

        //隐藏底部导航栏，并且不会获得焦点导致第一次点击失效
        getWindow().getAttributes().systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        staffPhone.setText(Constants.getCurrentStaff().getPhone());//设置显示手机号
        localBroadcastManager = LocalBroadcastManager.getInstance(this);//获取localBroadcastManager实例
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initTimers() {   //做一些timer初始化操作

        timerPing = new Timer();//开启保持session的线程
        log.debug("正在初始化ping nbr的Timer");
        /**
         * 定时ping nbr以保持会话不过期。服务器的过期时间是30分钟。App每隔25分钟ping一次
         */
        timerTaskPing = new TimerTask() {
            @Override
            public void run() {
                try {
                    staffHttpBO.pingEx();
                    System.out.println("xxxxxxxxxxxxxx Ping NBR以让其保持会话");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug(e.getMessage());
                }
            }
        };
        timerPing.schedule(timerTaskPing, 0, 25 * 60 * 1000);

        log.debug("正在初始化跨天上班的Timer");
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
//        // 启动会话被踢出检测或会话过期检测。若被踢出或过期，UI自动跳到登录界面
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

        Number_EnglishBoardDialog.createdialog(this);//一进入页面就创建好浮动键盘的实例
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        //副屏显示
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

        //T1 host副屏
        if (mDSKernel != null) {
            mDSKernel.checkConnection();
        } else {
            System.out.println("xxxxxxxxxxx成功initSdk");
            initSdk();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //返回键拦截
    @Override
    public void onBackPressed() {
        return;
    }

    //切换Fragment的事务
    protected void replace_Fragment(int item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.all_fragment, fragment1Adapter.getItem(item));
        transaction.commit();
    }

    private void setLinstener() {
        //设置左边item点击监听
        for (LinearLayout layout : linearLayouts) {
            layout.setOnClickListener(this);
        }
    }

    //点击事件
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (!Main1Activity.bPaying) {
            switch (view.getId()) {
                case R.id.sale_linear://销售
                    setLeftItem_bg_And_textBold(view);//设置背景变换
                    textviewlist.get(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置字体变粗
                    replace_Fragment(0);//切换Fragment
                    break;
                case R.id.inventory_linear://库存
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(1);
                    break;
                case R.id.set_up_linear://设置
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    settingItem.setVisibility(View.VISIBLE);//显示附属按钮
                    break;
                case R.id.btnQueryRetailTradeList_linear://查单
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(3).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(2);
                    break;
                case R.id.bill_linear://取单
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(4).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    replace_Fragment(5);
                    break;
                case R.id.report_loss_linear://报损
                    // TODO: 2020/4/17 此处暂时没有功能代码，不允许点击 
//                    setLeftItem_bg_And_textBold(view);
//                    textviewlist.get(5).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                    replace_Fragment(4);
                    break;
                case R.id.logout_linear://退出
                    if (BaseActivity.retailTradeHoldBillList.size() > 0) {
                        Toast.makeText(this, "存在挂起的未结零售单，请先处理。", Toast.LENGTH_SHORT).show();
                        view.setId(R.id.bill_linear);
                        this.onClick(view);
                    } else {
                        setLeftItem_bg_And_textBold(view);
                        textviewlist.get(6).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                        CustomDialog dialog = new CustomDialog(this, R.layout.logout_dialog, true, 0.45, 0.33) {
                            @Override
                            public void do_sure() {
                                // 获取当前时间
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
                case R.id.printer_linear://打印机
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置按钮保持高亮
                    linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));
                    textviewlist.get(7).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                    break;
                case R.id.desk_ticket_linear://前台小票
                    if (Constants.getCurrentStaff().getRoleID() != Constants.cashier_Role_ID) {
                        setLeftItem_bg_And_textBold(view);
                        textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置按钮保持高亮
                        linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));//设置按钮保持bg
                        textviewlist.get(7).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        replace_Fragment(3);
                    } else {
                        Toast.makeText(this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.data_processing_linear://数据处理
                    setLeftItem_bg_And_textBold(view);
                    textviewlist.get(2).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//设置按钮保持高亮
                    linearLayouts.get(2).setBackground(getResources().getDrawable(R.drawable.left_item_select, null));//设置按钮保持bg
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
//                //连接成功时的操作
//                log.info("网络连接成功！");
//            }
//
//            @Override
//            public void onDisConnected() {
//                //连接失败时的操作
//                log.info("网络连接失败！");
//            }
//        });
//        networkBroadcastReceiverHelper.register();
//    }
//

    /**
     * 注销广播
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
        timeThreadCanRun = false;//关闭时间线程的运行

        if (timerPing != null) {
            timerTaskPing.cancel();
            timerPing.purge();
        }

        timerToStartUploadRetailTradeAggregation.cancel();
        timerToStartUploadRetailTradeAggregation.purge();

        if (Number_EnglishBoardDialog.getdialog() != null) {
            Number_EnglishBoardDialog.getdialog().setdialogcancel();//activity被杀死之后，浮动键盘必须重新绑定新的activity
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
                if (isShouldHideInput(v, event)) {//点击editText控件外部
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
            // 必不可少，否则所有的组件都不会有TouchEvent了
            return getWindow().superDispatchTouchEvent(event) || onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.all_fragment);
        if (fragment instanceof RetrieveCommodityInventory1Activity) {
            //        //在输入键的键值大于6的情况下，除了删除键，返回键以及非数字键盘的数字键，其余按键均被拦截，
            log.info(event.getDevice().getName());

//        if (!"Virtual".equals(event.getDevice().getName())) { //这里使得键盘输入后不能一个一个回删，所以注释掉
            if (event.getKeyCode() > 6) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {//将键盘的删除键传递下去
                        return super.dispatchKeyEvent(event);
                    }
                    if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {//将非数字键盘的数字键传递下去
                        return super.dispatchKeyEvent(event);
                    }
                    this.onKeyDown(event.getKeyCode(), event);
                }
            } else {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//将键盘的返回事件传递下去
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
            ((RetrieveCommodityInventory1Activity) fragment1Adapter.getItem(1)).onKeyDownChild(keyCode, event);//传递过去
        } else if (fragment instanceof Main1Activity) {
            ((Main1Activity) fragment1Adapter.getItem(0)).onKeyDownChild(keyCode, event);//传递过去
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
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

    //通过线程每0.1秒发送一次消息
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

    //设置左侧边栏的背景变换和字体加粗
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLeftItem_bg_And_textBold(View layout) {
        for (LinearLayout linearLayout : linearLayouts) {//设置背景为空
            linearLayout.setBackground(getResources().getDrawable(R.drawable.left_item_unselect, null));
        }
        for (TextView textView : textviewlist) {//设置字体正常
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        settingItem.setVisibility(View.INVISIBLE);//设置的附属选项消失
        layout.setBackground(getResources().getDrawable(R.drawable.left_item_select, null));
    }

    //自定义弹窗
    abstract class CustomDialog extends Dialog {
        private Button sure, cancel;
        private int layout;//需要加载的布局
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

            //去除局部背景
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.color.transparent);//透明背景
            View decorView = window.getDecorView();      //隐藏虚拟按键全屏显示
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

            //设置dialog大小
            WindowManager manager = getWindowManager();
            Display display = manager.getDefaultDisplay();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            //  params.alpha = 0.9f;//设置背景变暗
            params.dimAmount = 0.42f;//设置背景有蒙版灰一点
            params.width = (int) (display.getWidth() * width);//设置长
            params.height = (int) (display.getHeight() * height);//设置宽
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            setCanceledOnTouchOutside(TouchOutside);//点击外部消失

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
            message.obj = "与副屏连接中断";
            handlerCustomerScreen.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = "与副屏绑定成功";
                    break;
                case VICE_SERVICE_CONN:
                    message.obj = "与副屏通讯正常";
                    break;
                case VICE_APP_CONN:
                    message.obj = "与副屏通讯正常";
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
                    case 1://消息提示用途
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
                    log.info("sessionID已清除");
                    Constants.setCurrentStaff(null); //稳健的做法
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
                    now = now + 100 + NtpHttpBO.TimeDifference;//当前的时间，加上sleep的0.1秒，再加上服务器与Pos机的时间差
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
     * T1 host副屏显示内容
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
     * 当增加，移除待售列表中的商品和修改商品数量时，会调用该函数，判断商品数量和状态，选择哪个presentation展示给客户
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
        log.info("屏幕数量" + displays.length);
        for (int i = 0; i < displays.length; i++) {
            log.info("屏幕" + displays[i]);
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
            log.debug("处理广播事件");
            //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
            //这里不判断也可以，因为发广播前判断过了
            if (BaseActivity.retailTradeAggregation.getID() != null) {
                if (intent.getAction().equals(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation)) {
                    log.debug("处理广播事件：" + DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
                    createRetailTradeAggregationForNextDay();
                }
            }
        }
    }

    /**
     * 当收银员跨天上班，到了第二天后，往sqlite插入一条收银汇总
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
        //初始化 BaseActivity.retailTradeAggregation的部分数据
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
                log.error("跨天上班时，创建零售单汇总失败");//TODO 未处理结果事件
            }
        }
    }

    /**
     * 收银员跨天上班时，在23:59:30上传今天的收银汇总，在第二天的00:00:00后，重新创建一张收银汇总到本地sqlite中
     */
    private void registerDayEndGenerateRetailTradeAggregationBroadcast(Timer timer) {
        Date targetTime = DatetimeUtil.mergeDateAndTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), Constants.START_TIME_UploadRetailTradeAggregation, Constants.DATE_FORMAT_Default);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
                if (BaseActivity.retailTradeAggregation.getID() != null) {
                    if (dayEndGenerateRetailTradeAggregationReceiver == null) {
                        intentFilter = new IntentFilter();
                        intentFilter.addAction(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
                        //
                        dayEndGenerateRetailTradeAggregationReceiver = new DayEndGenerateRetailTradeAggregationReceiver();
                        localBroadcastManager.registerReceiver(dayEndGenerateRetailTradeAggregationReceiver, intentFilter);//注册向本地所有activity发送弹出EndDayAlertDialog消息的本地广播监听器
                    }
                    Intent intent = new Intent(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
                    localBroadcastManager.sendBroadcast(intent);

                    BaseActivity.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
                    //更新本地的临时收银汇总
                    if (updateRetailTradeAggregationAtDayEnd()) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
                            log.info("在接近第二天的时候且有网络，上传收银汇总");
                            uploadRetailTradeAggregation(BaseActivity.retailTradeAggregation);
                        } else {
                            log.info("无网络进行跨天上班，不上传收银汇总");
                        }
                        //
                        printRetailTradeAggregation();
                    } else {
                        log.error("跨天上班时，更新今天本地的收银汇总失败！");
                    }
                } else {
                    //现在本地中创建一张今日空的收银汇总如果有网则上传。
                    RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
                    retailTradeAggregation.setPosID(Constants.posID);
                    retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                    retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
                    retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    if (createRetailTradeAggregationInSqlite(retailTradeAggregation)) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getApplicationContext())) {
                            uploadRetailTradeAggregation(retailTradeAggregation);
                        } else {
                            log.info("没输入准备金且无网络进行跨天上班,不上传收银汇总");
                        }
                    } else {
                        log.error("用户不输入准备金进行跨天上班,自动在本地生成今日的收银汇总失败！");
                    }
                    //
                    GlobalController.getInstance().setSessionID(null);
                    log.info("sessionID已清除");
                    //
                    ((AppApplication) getApplication()).exitFromSyncThread();
                    //
                    initialization();//点击登录后会调用，所以这里不call也行
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
            log.error("进行跨天上班时，更新本地收银汇总前获取数据失败,错误消息=" + e.getMessage());
            return false;
        }
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        return retailTradeAggregationSQLiteBO.updateSync(BaseSQLiteBO.INVALID_CASE_ID, updateRetailTradeAggregation);
    }

    private void uploadRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
        /*上传当前的零售汇总到服务器*/
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation);
        retailTradeAggregationSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        //
        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.info("上传当前的零售汇总到服务器失败");
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
            log.error("上传收银汇总单失败！原因：超时！");
            //...
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("上传收银汇总单失败！错误码为" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
            //...
        }
    }

    private void printRetailTradeAggregation() {
        //打印跨天上班汇总小票
        try {
            AidlUtil.getInstance().printText("交班确认表", 35, false, 17, false);
            AidlUtil.getInstance().printText("注意：此交班小票为跨日期上班系统自动生成，请妥善保管以便对账。", 20, false, 17, false);
            AidlUtil.getInstance().printDivider("-");
            AidlUtil.getInstance().printText("上班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
            AidlUtil.getInstance().printText("下班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeEnd()), 25, false, 51, false);
            AidlUtil.getInstance().printText("交班人：" + Constants.getCurrentStaff().getName(), 30, false, 51, false);
            AidlUtil.getInstance().linewrap(1);
            AidlUtil.getInstance().printText("------收银汇总------", 35, false, 17, false);
            AidlUtil.getInstance().printText("交易单数：" + BaseActivity.retailTradeAggregation.getTradeNO(), 30, false, 51, false);
            AidlUtil.getInstance().printText("营业额：" + BaseActivity.retailTradeAggregation.getAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("准备金：" + BaseActivity.retailTradeAggregation.getReserveAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("现金收入：" + BaseActivity.retailTradeAggregation.getCashAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("微信收入：" + BaseActivity.retailTradeAggregation.getWechatAmount(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("支付宝收入：" + showAlipayAmount.getText().toString(), 30, false, 51, false);
            AidlUtil.getInstance().printDivider("-");
            AidlUtil.getInstance().printText("交班人签名：", 30, false, 51, false);
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
     * 重新初始化用户的会话信息，防止污染下一个上班的收银员的会话信息
     */
    protected void initialization() {
        BaseActivity.retailTradeAggregation = null;
        BaseActivity.showCommList = new ArrayList<>();
        BaseActivity.retailTrade = new RetailTrade();//用于记录零售单
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
                System.out.println("向服务器上传收收银汇总成功");
            } else {
                System.out.println("向服务器上传收收银汇总失败");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseActivity.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseActivity.retailTradeAggregation);
                } else {
                    log.error("创建临时收银汇总失败！");
                }
            } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    System.out.println("更新收银汇总成功！收银汇总=" + event.getBaseModel1());
                } else {
                    log.error("更新收银汇总失败！");
                }
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}
