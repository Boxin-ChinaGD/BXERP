package com.bx.erp.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bx.erp.AppApplication;
import com.bx.erp.BuildConfig;
import com.bx.erp.R;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.di.components.ApplicationComponent;
import com.bx.erp.di.modules.ActivityModule;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.helper.NetworkBroadcastReceiverHelper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.service.ViewServer;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.view.component.KeyBoard;
import com.bx.erp.view.component.LoadingDialog;
import com.bx.erp.view.component.Number_EnglishBoardDialog;
import com.bx.erp.view.fragment.BaseFragment;
import com.bx.erp.view.presentation.CustomerCommodityListPresentation;
import com.bx.erp.view.presentation.PaymentSuccessPresentation;
import com.bx.erp.view.presentation.WelcomePresentation;
import com.sunmi.printerhelper.utils.AidlUtil;
import com.sunmi.printerhelper.utils.BytesUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions;

/**
 * Created by WPNA on 2020/2/28.
 */

public class BaseFragment1 extends Fragment {

    private Logger log = Logger.getLogger(this.getClass());

    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;
    /**
     * WX支付时的超时值，和OKHttp的超时值一致
     */
    protected final long WxPay_TIME_OUT = 60;

    private String currentActivity = "";

    public static int currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;

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
//    public static List<Commodity> showCommList = new ArrayList<>();
//    public static RetailTrade retailTrade = new RetailTrade();//用于记录零售单
//    public static RetailTrade lastRetailTrade;//用于记录刚卖出的零售单
//    public static String lastRetailTradePaymentType;//上一单的支付方式
//    public static double lastRetailTradeChangeMoney;//上一单的找零金额
//    public static double lastRetailTradeAmount;//上一单的总金额，用于结算完成时在首页展示
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
    protected Toast toast;

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

    private String[] allSingleTaskActivityInApp = {
            "Login1Activity",
            "SyncData1Activity",
            "RetailTradeAggregation1Activity"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventAndBO();

        appApplication = (AppApplication) getActivity().getApplication();//打印机部分
        View decorView = getActivity().getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

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

    protected Timer getTimer() {
        return new Timer();
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

    @Override
    public void onStart() {
        super.onStart();
//        if (timer != null) {
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        if (BaseHttpEvent.HttpRequestStatus == 1) {
//                            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxbaseFragment1");
//                            Message message = new Message();
//                            message.what = 1;
//                            listenerHandler.sendMessage(message);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
////      开启检测错误码线程
//            timer.schedule(timerTask, 0, 1000);
//        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dismissLoading();
        if (BuildConfig.DEBUG) {
            ViewServer.get(getContext()).removeWindow(getActivity()); // 使得真机也能使用HierarchyViewer 工具
        }

        //    ActivityController.removeActivity(getActivity());
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * Shows a {@link android.widget.Toast} msgResId.
     *
     * @param msgResId An string representing a msgResId to be shown.
     */
    protected void showToastMessage(@StringRes int msgResId) {
        Toast.makeText(getContext(), msgResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Get the Main Application component for dependency injection.
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getActivity().getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(getActivity());
    }

    protected LoadingDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }


    EditTextDialog mEditTextDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print:
                //Toast.makeText(this, "将实现十六进制指令发送", Toast.LENGTH_SHORT).show();
                mEditTextDialog = DialogCreater.createEditTextDialog(getActivity(), getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_order), new View.OnClickListener() {
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

        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        Toast.makeText(getContext(), msg, duration).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onResume() {
        super.onResume();
    }

    protected LoadingDailog createWaitingUI(LoadingDailog loadingDailog, String msg) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(getContext())
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
     * 获取本地当前小票格式，如果没有，则使用默认的小票格式。
     *
     * @return currentSmallSheetID
     * BaseSQLiteBO.INVALID_INT_ID，DB异常；其它值，默认的小票格式
     */
    public int retrieveCurrentSmallSheetIDFromConfig() {
        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setID(Long.valueOf(String.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID)));
        ConfigGeneral configGeneralFromDB = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        if (configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && configGeneralFromDB == null) {
            log.error("configGeneralPresenter.retrieve1ObjectSync()失败,错误码=" + configGeneralPresenter.getLastErrorCode());
//            Toast.makeText(getApplicationContext(), "无法查找当前小票格式的配置，读取配置失败", Toast.LENGTH_SHORT).show(); //运行在非UI线程

            return BaseSQLiteBO.INVALID_INT_ID;
        }

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != ?");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("查找本地的小票格式失败！！");

            return BaseSQLiteBO.INVALID_INT_ID;
        }
        if (ssfList.size() == 0) { // 没有小票格式
            return BaseSQLiteBO.INVALID_INT_ID;
        }

        for (SmallSheetFrame ssf : ssfList) {
            if (ssf.getID() == Long.valueOf(configGeneralFromDB.getValue())) {
                return Integer.valueOf(configGeneralFromDB.getValue());
            }
        }
        // 如果断网情况下删除当前小票，本地的当前小票会更新成默认的（1），但是不会上传到服务器，如果关掉app，下次登录同步服务器的配置，那么当前小票还是原来的，但是原来的已经被删除了，所以需要更新配置的当前小票格式为默认的
        configGeneralFromDB.setValue(String.valueOf(SmallSheet1Activity.Default_SmallSheetID_INPos));
        configGeneralFromDB.setReturnObject(1); //...
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
            log.error("更新默认小票格式失败！configGeneral=" + configGeneralFromDB);
        }
        return Integer.valueOf(String.valueOf(SmallSheet1Activity.Default_SmallSheetID_INPos));
    }

    /**
     * 打印小票。
     */
    public void printSmallSheet(RetailTrade retailTrade) throws Exception {
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            Toast.makeText(getActivity().getApplicationContext(), "无法打印小票，加载小票格式失败", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(Long.valueOf(currentSmallSheetID));
        SmallSheetFrame smallSheetFrame1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && smallSheetFrame1 == null) {
            log.error("smallSheetFramePresenter.retrieve1ObjectSync失败,错误码=" + smallSheetFramePresenter.getLastErrorCode());
            Toast.makeText(getActivity().getApplicationContext(), "无法打印小票，加载小票格式失败", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SmallSheetText> listSmallSheetText = (List<SmallSheetText>) smallSheetFrame1.getListSlave1();

        //退货单需要在小票上进行标注  居中，1.5倍字号
        if (retailTrade.getSourceID() > 0) {
            AidlUtil.getInstance().printText("退货单",
                    30,//字号30
                    true,//加粗
                    17,//居中
                    false);//无下划线
            AidlUtil.getInstance().linewrap(1);//分隔一行
        }

        //每一行判断控件的状态进行设置
        if (smallSheetFrame1.getLogo() != null && !smallSheetFrame1.getLogo().equals("")) {
            AidlUtil.getInstance().printBitmap(GeneralUtil.stringToBitmap(smallSheetFrame1.getLogo()));
            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        }

        if (!"".equals(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent())) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent(), //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getSize(), //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getBold() == 1, //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getGravity(), //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getFrameId() == 1);
            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        }
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getContent() + retailTrade.getSn(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getFrameId() == 1);
        // 时间格式转换
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getContent() + sdf.format(retailTrade.getSaleDatetime()), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        String[] saCommodityAttribute = new String[]{"商品名称", "数量", "小计"};
        int[] colsWidthArrc = new int[]{2, 1, 1};//每一列所占权重
        int[] colsAlign = new int[]{0, 1, 2};//每一列对齐方式
        String[][] listCommodity = null;// 商品的信息列表
        //
        List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        if (listRtc == null) {
            Toast.makeText(getContext(), "零售单异常，没有商品信息", Toast.LENGTH_LONG).show();
            return;
        }
        listCommodity = new String[listRtc.size()][3];
        int i = 0;//判断遍历到多少了
        Double totalPriceOriginal = 0.000000d; // 零售的所有商品的原零售价总和
        for (BaseModel bm : listRtc) {
            RetailTradeCommodity rtc = (RetailTradeCommodity) bm;
            listCommodity[i][0] = rtc.getCommodityName();
            listCommodity[i][1] = String.valueOf(rtc.getNO());
            listCommodity[i][2] = String.valueOf(GeneralUtil.formatToShow(GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO())));
            totalPriceOriginal = GeneralUtil.sum(totalPriceOriginal, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
            i++;
        }
        //
        AidlUtil.getInstance().printTable(saCommodityAttribute, colsWidthArrc, colsAlign);
        for (int j = 0; j < i; j++) {
            AidlUtil.getInstance().printTable(listCommodity[j], colsWidthArrc, colsAlign);
        }
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getContent() + GeneralUtil.formatToShow(totalPriceOriginal), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getFrameId() == 1);
        //
        String retailTradePaymentType = null;
        if (retailTrade.getPaymentType() == 1) {
            retailTradePaymentType = pay_cash;
        } else if (retailTrade.getPaymentType() == 2) {
            retailTradePaymentType = pay_aliPay;
        } else if (retailTrade.getPaymentType() == 4) {
            retailTradePaymentType = pay_wetchat;
        } else {
            retailTradePaymentType = pay_combination;
        }
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getContent() + retailTradePaymentType, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getContent() + GeneralUtil.formatToShow(GeneralUtil.sub(totalPriceOriginal, retailTrade.getAmount())), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getBold() == 1,//
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmount()), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountWeChat()),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountCash()),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getFrameId() == 1);
        // 打印支付金额
        AidlUtil.getInstance().printText("支付金额：￥" + GeneralUtil.formatToShow(retailTrade.getAmountPaidIn()),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getFrameId() == 1);
        // 打印找零金额
        AidlUtil.getInstance().printText("找零：￥" + GeneralUtil.formatToShow(retailTrade.getAmountChange()),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getFrameId() == 1);
        //
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent().equals("")) {
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getFrameId() == 1);
        }

        AidlUtil.getInstance().linewrap(smallSheetFrame1.getCountOfBlankLineAtBottom());
        AidlUtil.getInstance().cutPaper();
    }

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
        BaseActivity.showCommList = new ArrayList<>();
        BaseActivity.retailTrade = new RetailTrade();//用于记录零售单
        BaseActivity.lastRetailTrade = null;
        BaseActivity.lastRetailTradePaymentType = "";
        BaseActivity.lastRetailTradeChangeMoney = 0.000000d;
        BaseActivity.lastRetailTradeAmount = 0.000000d;
    }

    public void closeLoadingDialog(LoadingDailog loadingDailog) {
        if (loadingDailog != null && loadingDailog.isShowing()) {
            loadingDailog.dismiss();
        }
    }

    //    //Fragemnt无法监听 onKeyDown，由宿主Activity传回这里，再由fragment调用
//    public void onKeyDownChild(int keyCode, KeyEvent event) {
//
//    }
    //弹出和收回动画
    protected void setAnimation(View view) {
        TranslateAnimation translateAnimation;
        if (view.getVisibility() == View.VISIBLE) {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        translateAnimation.setDuration(200);
        view.setAnimation(translateAnimation);
    }

    //隐藏虚拟按键全屏显示
    protected void HideVirtualKeyBoard() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    //浮动键盘
    EditText editText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void DisplayCustomKeyBoard(View view, final boolean canSeeEn, final boolean canSeeSymbol) {
        editText = (EditText) view;
        editText.setShowSoftInputOnFocus(false);
        int[] location = new int[2];
        editText.getLocationOnScreen(location);//获取需要输入字符的控件的位置，让键盘在控件之上
        int x = location[0];
        int y = location[1];
        //展示浮动键盘
        Number_EnglishBoardDialog.createdialog(getActivity())
                .setCanSeeEn(canSeeEn)
                .setCanSeeSymbol(canSeeSymbol)
                .setDeviation(x, y)
                .setGettext(editText.getText().toString().trim())
                .setOutputListener(new Number_EnglishBoardDialog.outputStringListener() {
                    @Override
                    public void outputString(String output) {   //点击了数字将从output中输出出来
                        editText.setText(output);
                        editText.setSelection(editText.length());
                    }
                })
                .Showdialog();
    }

//    //禁止tEditText复制粘贴
//    public void setEditTextPaste(EditText editText) {
//        editText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
//        editText.setLongClickable(false);
//        editText.setTextIsSelectable(false);
//        editText.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());//禁止复制
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            editText.setCustomInsertionActionModeCallback(new ActionModeCallbackInterceptor()); //禁止粘贴
//        }
//    }
//
//    private class ActionModeCallbackInterceptor implements ActionMode.Callback {
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            return false;
//        }
//
//
//        public void onDestroyActionMode(ActionMode mode) {
//        }
//    }


    //自定义输入准备金的弹窗   ---所有带键盘的弹窗都可用这个类
    abstract class CustomDialog extends Dialog {
        private Button sure, cancel;
        private int layout;//需要加载的布局
        private boolean TouchOutside;
        private double width, height;
        private KeyBoard keyBoard; //键盘
        EditText reserve_et;//准备金的alertdialog的准备金额编辑框


        public CustomDialog(@NonNull Context context, int layout, boolean TouchOutside, double width, double height) {
            super(context, layout);
            this.layout = layout;
            this.TouchOutside = TouchOutside;
            this.width = width;
            this.height = height;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

            sure = findViewById(R.id.confirm_reserve);
            cancel = findViewById(R.id.cancel);
            reserve_et = findViewById(R.id.reserve_et);
            reserve_et.setShowSoftInputOnFocus(false);

//            setEditTextPaste(reserve_et);
            // reserve_et.setFocusable(false);
            //   reserve_et.clearFocus();

            //设置dialog大小
            WindowManager manager = getActivity().getWindowManager();
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
                    do_sure(reserve_et.getText().toString());
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            keyBoard = findViewById(R.id.keyboard);
            keyBoard.setOnNumberclickListener(new KeyBoard.OnNumberclickListener() {
                @Override
                public void onClick(String number) {
                    reserve_et.setText(number);//将数字填入edittext
                    reserve_et.setSelection(number.length());
                }
            });

        }

        public abstract void do_sure(String reserve);
    }
}
