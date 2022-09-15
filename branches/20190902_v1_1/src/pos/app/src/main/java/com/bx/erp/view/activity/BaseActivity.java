package com.bx.erp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bx.erp.AppApplication;
import com.bx.erp.BuildConfig;
import com.bx.erp.R;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.common.ActivityController;
import com.bx.erp.common.GlobalController;
import com.bx.erp.di.components.ApplicationComponent;
import com.bx.erp.di.modules.ActivityModule;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.helper.NetworkBroadcastReceiverHelper;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.service.ViewServer;
import com.bx.erp.utils.NetworkStatusService;
import com.bx.erp.utils.ScreenManager;
import com.bx.erp.utils.UPacketFactory;
import com.bx.erp.view.component.LoadingDialog;
import com.bx.erp.view.fragment.BaseFragment;
import com.bx.erp.view.presentation.CustomerCommodityListPresentation;
import com.bx.erp.view.presentation.PaymentSuccessPresentation;
import com.bx.erp.view.presentation.WelcomePresentation;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BytesUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import sunmi.ds.data.DataPacket;
import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;

public abstract class BaseActivity extends AppCompatActivity {
    private Logger log = Logger.getLogger(this.getClass());

    public static final int MAX_HOLD_BILL_NO = 10;
    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;
    /**
     * WX支付时的超时值，和OKHttp的超时值一致
     */
    protected final long WxPay_TIME_OUT = 60;

    private String currentActivity = "";

    protected int currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;

    /**
     * barcode模糊搜索的长度限制：必须>6
     * TODO 重命名此变量
     */
    public static int FUZZY_QUERY_LENGTH = 6; //模糊查询的长度条件；
    /**
     * 保存最近写进SQLite的收银汇总。交易单数在退货时不需要更改，只记录零售的交易单数
     * 生：当用户登录成功并输入准备金后，会向SQLite插入一个最初的收银汇总。
     * 死：交班时，会上传收银汇总。上传后，本变量设置为null
     */
    public static RetailTradeAggregation retailTradeAggregation = null;

    /**
     * 待售商品列表
     */
    public static List<Commodity> showCommList = new ArrayList<>();
    public static RetailTrade retailTrade = new RetailTrade();//用于记录零售单
    public static List<RetailTrade> retailTradeHoldBillList = new ArrayList<RetailTrade>();//用于记录零售单
    public static RetailTrade lastRetailTrade;//用于记录刚卖出的零售单
    public static String lastRetailTradePaymentType;//上一单的支付方式
    public static double lastRetailTradeChangeMoney;//上一单的找零金额
    public static double lastRetailTradeAmount;//上一单的总金额，用于结算完成时在首页展示
    public static double lastRetailTradePaidInAmount;//上一单实收金额
    public static final String pay_wetchat = "微信支付 ";
    public static final String pay_aliPay = "支付宝支付 ";
    public static final String pay_cash = "现金支付 ";
    public static final String pay_combination = "组合支付 ";

    protected LoadingDailog loadingDailog;
    protected LoadingDailog loadingDailog1;
    protected LoadingDailog loadingDailog2;
    public static final String LOADING_MSG_General = "加载中...";
    public static final String LOADING_MSG_Paying = "支付中...";
    public static final String LOADING_MSG_UpdatingSmallSheet = "保存修改中...";
    public static final String LOADING_MSG_DeletingSmallSheet = "删除中...";
    public static final String LOADING_MSG_CreatingSmallSheet = "创建中...";
    public static final String LOADING_MSG_SettingSmallSheetConfigGeneral = "设置中...";
    /*
    * 纯现金支付的零售，进行全部商品退货，允许存在一分钱的误差：
    * */
    public static final double TORELANCE_ReturnWholeRetailTradeAllPaidByCash = 0.010000d;

    private ArrayList<BaseFragment> fragments;
    private BaseFragment fragment;

    //打印机部分
    AppApplication appApplication;

    private NetworkBroadcastReceiverHelper networkBroadcastReceiverHelper;

    protected WelcomePresentation welcomePresentation = null;
    protected CustomerCommodityListPresentation customerCommodityListPresentation = null;
    protected PaymentSuccessPresentation paymentSuccessPresentation = null;
    protected SmallSheetFramePresenter smallSheetFramePresenter = null;

    protected ConfigGeneralPresenter configGeneralPresenter = null;
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;

    protected ScreenManager screenManager = ScreenManager.getInstance();
    protected static boolean isVertical = false;

    private DSKernel mDSKernel = null;
    private MyHandler myHandler;
    private Timer timer;
    private TimerTask timerTask;
    private Toast toast;

    private String[] allSingleTaskActivityInApp = {
            "LoginActivity",
            "SyncDataActivity",
            "SetupActivity",
            "MainActivity",
            "SmallSheetActivity",
            "RetailTradeAggregationActivity",
            "RetrieveCommodityInventoryActivity",
            "ResetBaseDataActivity",
            "PaymentActivity",
            "PreSaleActivity",
            "Login1Activity",
            "Base1FragmentActivity",
            "SyncData1Activity",
            "PreSale1Activity",
            "RetailTradeAggregation1Activity"};

    /**
     * 检查targetActivity是否在目标activity列表中。我们将来可能会增加新的activity，但是会忘记要改动某些相应的地方（目前是allSingleTaskActivityInApp和onBackPressed())，导致不能第一时间发现错误
     */
    private boolean isInScope(String targetActivity) {
        boolean inScope = false;
        for (int i = 0; i < allSingleTaskActivityInApp.length; i++) {
            if (allSingleTaskActivityInApp[i].equals(targetActivity)) {
                inScope = true;
            }
        }

        return inScope;
    }

    private void initEventAndBO() {
        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (configGeneralSQLiteBO == null) {
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
        initEventAndBO();

        currentActivity = getClass().getSimpleName();
        if (!isInScope(currentActivity)) {
            throw new RuntimeException("当前Activity不在既定范围中。请将其名称加入到allActivityInApp[]中！");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//屏幕宽度
        int height = dm.heightPixels;//屏幕高度
        isVertical = height > width;
        initPresentationData(getApplication());

        //由于设置页面不需要接收任何Event，所以设置页面不需要注册EventBus事件  TODO 如以后无有任何响应事件交互的活动，那么请在这里加上条件判断
        if (!"SetupActivity".equals(currentActivity)) {
            EventBus.getDefault().register(this);
        }

        appApplication = (AppApplication) getApplication();//打印机部分
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        registerBroadcast();
        Intent intent = new Intent(this, NetworkStatusService.class);
        startService(intent);

        myHandler = new MyHandler(this);
        initSdk();

        timer = getTimer();
        //
        //TODO 如果以后有创建其他的Activity，要视情况在此判断添加Activity
        if (isInScope(currentActivity)) {
            ActivityController.addActivity(this);
            ActivityController.setCurrentActivity(this);
        }
    }

    protected Timer getTimer() {
        return new Timer();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        if (BuildConfig.DEBUG) ViewServer.get(this).addWindow(this);  // 使得真机也能使用HierarchyViewer 工具
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    EditText editText = null;

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

    @Override
    protected void onStart() {
        super.onStart();
        if (timer != null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (BaseHttpEvent.HttpRequestStatus == 1) {
                            Message message = new Message();
                            message.what = 1;
                            listenerHandler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
//      开启检测错误码线程
            timer.schedule(timerTask, 0, 1000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timerTask.cancel();//终止该timertask
            timer.purge();//释放对cancel的timertask的引用
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer = null;
        EventBus.getDefault().unregister(this);
        dismissLoading();
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).removeWindow(this); // 使得真机也能使用HierarchyViewer 工具
        }
        unregisterBroadcast();

        ActivityController.removeActivity(this);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        if (toast == null){
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        }
        toast.show();
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a {@link android.widget.Toast} msgResId.
     *
     * @param msgResId An string representing a msgResId to be shown.
     */
    protected void showToastMessage(@StringRes int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected LoadingDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

    //设置全屏显示。隐藏通知栏和底部虚拟按键，不需要考虑到activity的生命周期
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 以下都是打印机部分的代码
     */
    public void setBack() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    EditTextDialog mEditTextDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print:
                //Toast.makeText(this, "将实现十六进制指令发送", Toast.LENGTH_SHORT).show();
                mEditTextDialog = DialogCreater.createEditTextDialog(this, getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_order), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditTextDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = mEditTextDialog.getEditText().getText().toString();
                        AidlUtil.getInstance().sendRawData(BytesUtil.getBytesFromHexString(text));
                        mEditTextDialog.cancel();
                    }
                }, null);
                mEditTextDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void registerBroadcast() {
        networkBroadcastReceiverHelper = new NetworkBroadcastReceiverHelper(getApplicationContext(), new NetworkBroadcastReceiverHelper.OnNetworkStateChangedListener() {
            @Override
            public void onConnected() {
                //连接成功时的操作
                log.info("网络连接成功！");
            }

            @Override
            public void onDisConnected() {
                //连接失败时的操作
                log.info("网络连接失败！");
            }
        });
        networkBroadcastReceiverHelper.register();
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        networkBroadcastReceiverHelper.unregister();
    }

    public void backToFragment() {
        if (fragments != null && fragments.size() > 1) {
            removeContent();
            backToFragment();
        }
    }

    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    public BaseFragment getFirstFragment() {
        return fragment;
    }

    public int getFragmentNum() {
        return fragments != null ? fragments.size() : 0;
    }

    protected void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }

    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }

    private void initFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    public void removeContent() {
        removePrevious();
        setFragment();

        getSupportFragmentManager().popBackStackImmediate();
    }

    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public void showLongToast(@StringRes int resId) {
        showLongToast(getResources().getString(resId));
    }

    public void showLongToast(String msg, int code) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(int code, String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(@StringRes int resId) {
        showToast(getResources().getString(resId));
    }

    public void showToast(@StringRes int resId, int code) {
        showToast(code, getResources().getString(resId));
    }

    private void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
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
            initSdk();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 当增加，移除待售列表中的商品和修改商品数量时，会调用该函数，判断商品数量和状态，选择哪个presentation展示给客户
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void isShowToCustomer(Context context) {
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
        if (display != null && !isVertical) {
            welcomePresentation = new WelcomePresentation(this, display);
            customerCommodityListPresentation = new CustomerCommodityListPresentation(this, display);
            paymentSuccessPresentation = new PaymentSuccessPresentation(this, display);
        }
    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback2);
        mDSKernel.removeReceiveCallback(mIReceiveCallback);
        mDSKernel.removeReceiveCallback(mIReceiveCallback2);
    }

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj = "与远程服务连接中断";
            myHandler.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = "与远程服务绑定成功";
                    break;
                case VICE_SERVICE_CONN:
                    message.obj = "与副屏服务通讯正常";
                    break;
                case VICE_APP_CONN:
                    message.obj = "与副屏app通讯正常";
                    break;
                default:
                    break;
            }
            myHandler.sendMessage(message);
        }
    };

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

    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://消息提示用途
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        }

    }

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

    private Handler listenerHandler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timer.cancel(); //set null 放在onDestroy()中
                    BaseHttpEvent.HttpRequestStatus = 0;
                    GlobalController.getInstance().setSessionID(null);
                    log.info("sessionID已清除");
                    //
                    ((AppApplication) getApplication()).exitFromSyncThread();
                    //
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("LoginInterceptor", 1);
//                    intent.putExtra("LoginInterceptorWarn", BaseHttpEvent.HttpRequestWarnMsg);
//                    ActivityController.finishAll();
//                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    protected LoadingDailog createWaitingUI(LoadingDailog loadingDailog, String msg) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setCancelOutside(true);
        loadingDailog = loadBuilder.create();
        loadingDailog.setCancelable(false);
        //
        if (!loadingDailog.isShowing()) {
            loadingDailog.show();
        }

        return loadingDailog;
    }

    /**
     * 监听系统返回按钮
     */
    @Override
    public void onBackPressed() {
        //如果是登录界面或主界面或者某些不允许点击系统返回键的情况，点击返回按钮无效果。
        if ("Login1Activity".equals(currentActivity) || "Main1Activity".equals(currentActivity) || "SyncData1Activity".equals(currentActivity) || "PreSale1Activity".equals(currentActivity)) {
            return;
        }
        super.onBackPressed();
//        if ("PaymentActivity".equals(currentActivity) || "RetrieveCommodityInventoryActivity".equals(currentActivity) || "SetupActivity".equals(currentActivity) || "RetailTradeAggregationActivity".equals(currentActivity)) {
////            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////            startActivity(intent);
////            finish();
//        } else if ("PrinterActivity".equals(currentActivity) || "ResetBaseDataActivity".equals(currentActivity) || "SmallSheetActivity".equals(currentActivity)) {
////            Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
////            finish();
////            startActivity(intent);
//        }
    }

    /**
     * 获取本地当前小票格式，如果没有，则使用默认的小票格式。
     *
     * @return currentSmallSheetID
     * BaseSQLiteBO.INVALID_INT_ID，DB异常；其它值，默认的小票格式
     */
//    public int retrieveCurrentSmallSheetIDFromConfig() {
//        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
//        ConfigGeneral configGeneral = new ConfigGeneral();
//        configGeneral.setID(Long.valueOf(String.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID)));
//        ConfigGeneral configGeneralFromDB = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
//        if (configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && configGeneralFromDB == null) {
//            log.error("configGeneralPresenter.retrieve1ObjectSync()失败,错误码=" + configGeneralPresenter.getLastErrorCode());
////            Toast.makeText(getApplicationContext(), "无法查找当前小票格式的配置，读取配置失败", Toast.LENGTH_SHORT).show(); //运行在非UI线程
//
//            return BaseSQLiteBO.INVALID_INT_ID;
//        }
//
//        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
//        smallSheetFrame.setSql("where F_SyncType != ?");
//        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
//        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
//        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("查找本地的小票格式失败！！");
//
//            return BaseSQLiteBO.INVALID_INT_ID;
//        }
//        if (ssfList.size() == 0) { // 没有小票格式
//            return BaseSQLiteBO.INVALID_INT_ID;
//        }
//
//        for (SmallSheetFrame ssf : ssfList) {
//            if (ssf.getID() == Long.valueOf(configGeneralFromDB.getValue())) {
//                return Integer.valueOf(configGeneralFromDB.getValue());
//            }
//        }
//        // 如果断网情况下删除当前小票，本地的当前小票会更新成默认的（1），但是不会上传到服务器，如果关掉app，下次登录同步服务器的配置，那么当前小票还是原来的，但是原来的已经被删除了，所以需要更新配置的当前小票格式为默认的
//        configGeneralFromDB.setValue(String.valueOf(SmallSheetActivity.Default_SmallSheetID_INPos));
//        configGeneralFromDB.setReturnObject(1); //...
//        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
//        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
//            log.error("更新默认小票格式失败！configGeneral=" + configGeneralFromDB);
//        }
//        return Integer.valueOf(String.valueOf(SmallSheetActivity.Default_SmallSheetID_INPos));
//    }

    /**
     * 打印小票。
     */
//    public void printSmallSheet(RetailTrade retailTrade) throws Exception {
//        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
//            currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
//        }
//        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
//            Toast.makeText(getApplicationContext(), "无法打印小票，加载小票格式失败", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //
//        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
//        smallSheetFrame.setID(Long.valueOf(currentSmallSheetID));
//        SmallSheetFrame smallSheetFrame1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
//        if (smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && smallSheetFrame1 == null) {
//            log.error("smallSheetFramePresenter.retrieve1ObjectSync失败,错误码=" + smallSheetFramePresenter.getLastErrorCode());
//            Toast.makeText(getApplicationContext(), "无法打印小票，加载小票格式失败", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        List<SmallSheetText> listSmallSheetText = (List<SmallSheetText>) smallSheetFrame1.getListSlave1();
//        //每一行判断控件的状态进行设置
//        if (smallSheetFrame1.getLogo() != null && !smallSheetFrame1.getLogo().equals("")) {
//            AidlUtil.getInstance().printBitmap(GeneralUtil.stringToBitmap(smallSheetFrame1.getLogo()));
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//        }
//        if (!"".equals(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent())) {
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent(), //
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getSize(), //
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getBold() == 1, //
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getGravity(), //
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getFrameId() == 1);
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//        }
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getContent() + retailTrade.getSn(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getBold() == 1, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getFrameId() == 1);
//        // 时间格式转换
//        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getContent() + sdf.format(retailTrade.getSaleDatetime()), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getBold() == 1, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getFrameId() == 1);
//        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//        //
//        String[] saCommodityAttribute = new String[]{"商品名称", "数量", "小计"};
//        int[] colsWidthArrc = new int[]{2, 1, 1};//每一列所占权重
//        int[] colsAlign = new int[]{0, 1, 2};//每一列对齐方式
//        String[][] listCommodity = null;// 商品的信息列表
//        //
//        List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
//        if (listRtc == null) {
//            Toast.makeText(this, "零售单异常，没有商品信息", Toast.LENGTH_LONG).show();
//            return;
//        }
//        listCommodity = new String[listRtc.size()][3];
//        int i = 0;//判断遍历到多少了
//        Double totalPriceOriginal = 0.000000d; // 零售的所有商品的原零售价总和
//        for (BaseModel bm : listRtc) {
//            RetailTradeCommodity rtc = (RetailTradeCommodity) bm;
//            listCommodity[i][0] = rtc.getCommodityName();
//            listCommodity[i][1] = String.valueOf(rtc.getNO());
//            listCommodity[i][2] = String.valueOf(GeneralUtil.formatToShow(GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO())));
//            totalPriceOriginal = GeneralUtil.sum(totalPriceOriginal, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
//            i++;
//        }
//        //
//        AidlUtil.getInstance().printTable(saCommodityAttribute, colsWidthArrc, colsAlign);
//        for (int j = 0; j < i; j++) {
//            AidlUtil.getInstance().printTable(listCommodity[j], colsWidthArrc, colsAlign);
//        }
//        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//        //
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getContent() + GeneralUtil.formatToShow(totalPriceOriginal), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getBold() == 1, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getFrameId() == 1);
//        //
//        String retailTradePaymentType = null;
//        if (retailTrade.getPaymentType() == 1) {
//            retailTradePaymentType = pay_cash;
//        } else if (retailTrade.getPaymentType() == 2) {
//            retailTradePaymentType = pay_aliPay;
//        } else if (retailTrade.getPaymentType() == 4) {
//            retailTradePaymentType = pay_wetchat;
//        } else {
//            retailTradePaymentType = pay_combination;
//        }
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getContent() + retailTradePaymentType, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getBold() == 1, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getFrameId() == 1);
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getContent() + GeneralUtil.formatToShow(GeneralUtil.sub(totalPriceOriginal, retailTrade.getAmount())), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getBold() == 1,//
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getFrameId() == 1);
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmount()), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getSize(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getBold() == 1, //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getGravity(), //
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getFrameId() == 1);
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountWeChat()),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getSize(),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getBold() == 1,
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getGravity(),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getFrameId() == 1);
//        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountCash()),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getSize(),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getBold() == 1,
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getGravity(),
//                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getFrameId() == 1);
//        //
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getFrameId() == 1);
//        }
//        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent().equals("")) {
////            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
//            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getSize(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getBold() == 1,
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getGravity(),
//                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getFrameId() == 1);
//        }
//
//        AidlUtil.getInstance().linewrap(smallSheetFrame1.getCountOfBlankLineAtBottom());
//        AidlUtil.getInstance().cutPaper();
//    }

    /**
     * 用于在子线程中，发送一个消息到Handle进行UI操作
     */
    protected class HandlerMessage {
        protected String msg;
        protected ErrorInfo.EnumErrorCode errorCode;
        protected int subErrorCode;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public ErrorInfo.EnumErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorInfo.EnumErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public int getSubErrorCode() {
            return subErrorCode;
        }

        public void setSubErrorCode(int subErrorCode) {
            this.subErrorCode = subErrorCode;
        }
    }

    /**
     * 重新初始化用户的会话信息，防止污染下一个上班的收银员的会话信息
     */
    protected void initialization() {
        retailTradeAggregation = null;
        showCommList = new ArrayList<>();
        retailTrade = new RetailTrade();//用于记录零售单
        lastRetailTrade = null;
        lastRetailTradePaymentType = "";
        lastRetailTradeChangeMoney = 0.000000d;
        lastRetailTradeAmount = 0.000000d;
    }

    public void closeLoadingDialog(LoadingDailog loadingDailog) {
        if (loadingDailog != null && loadingDailog.isShowing()) {
            loadingDailog.dismiss();
        }
    }
}