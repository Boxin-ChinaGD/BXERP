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
     * UI?????????????????????????????????????????????WxPay_TIME_OUT??????60???????????????????????????????????????????????????????????????UI?????????????????????
     */
    protected final long TIME_OUT = 30;
    /**
     * WX???????????????????????????OKHttp??????????????????
     */
    protected final long WxPay_TIME_OUT = 60;

    private String currentActivity = "";

    public static int currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;

    /**
     * barcode????????????????????????????????????>6
     * TODO ??????????????????
     */
    public static int FUZZY_QUERY_LENGTH = 6; //??????????????????????????????
    /**
     * ??????????????????SQLite??????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????????????????SQLite????????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????null
     */
    public static RetailTradeAggregation retailTradeAggregation = null;

    /**
     * ??????????????????
     */
//    public static List<Commodity> showCommList = new ArrayList<>();
//    public static RetailTrade retailTrade = new RetailTrade();//?????????????????????
//    public static RetailTrade lastRetailTrade;//?????????????????????????????????
//    public static String lastRetailTradePaymentType;//????????????????????????
//    public static double lastRetailTradeChangeMoney;//????????????????????????
//    public static double lastRetailTradeAmount;//????????????????????????????????????????????????????????????
    public static final String pay_wetchat = "???????????? ";
    public static final String pay_aliPay = "??????????????? ";
    public static final String pay_cash = "???????????? ";
    public static final String pay_combination = "???????????? ";

    protected LoadingDailog loadingDailog;
    protected LoadingDailog loadingDailog1;
    protected LoadingDailog loadingDailog2;
    public static final String LOADING_MSG_General = "?????????...";
    public static final String LOADING_MSG_Paying = "?????????...";
    public static final String LOADING_MSG_UpdatingSmallSheet = "???????????????...";
    public static final String LOADING_MSG_DeletingSmallSheet = "?????????...";
    public static final String LOADING_MSG_CreatingSmallSheet = "?????????...";
    public static final String LOADING_MSG_SettingSmallSheetConfigGeneral = "?????????...";
    /*
    * ???????????????????????????????????????????????????????????????????????????????????????
    * */
    public static final double TORELANCE_ReturnWholeRetailTradeAllPaidByCash = 0.010000d;

    private ArrayList<BaseFragment> fragments;
    private BaseFragment fragment;
    protected Toast toast;

    //???????????????
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

        appApplication = (AppApplication) getActivity().getApplication();//???????????????
        View decorView = getActivity().getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    /**
     * ??????targetActivity???????????????activity?????????????????????????????????????????????activity????????????????????????????????????????????????????????????allSingleTaskActivityInApp???onBackPressed())???????????????????????????????????????
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
////      ???????????????????????????
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
            ViewServer.get(getContext()).removeWindow(getActivity()); // ????????????????????????HierarchyViewer ??????
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
                //Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @return currentSmallSheetID
     * BaseSQLiteBO.INVALID_INT_ID???DB??????????????????????????????????????????
     */
    public int retrieveCurrentSmallSheetIDFromConfig() {
        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setID(Long.valueOf(String.valueOf(ConfigGeneral.ACTIVE_SMALLSHEET_ID)));
        ConfigGeneral configGeneralFromDB = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        if (configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && configGeneralFromDB == null) {
            log.error("configGeneralPresenter.retrieve1ObjectSync()??????,?????????=" + configGeneralPresenter.getLastErrorCode());
//            Toast.makeText(getApplicationContext(), "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show(); //????????????UI??????

            return BaseSQLiteBO.INVALID_INT_ID;
        }

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != ?");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("???????????????????????????????????????");

            return BaseSQLiteBO.INVALID_INT_ID;
        }
        if (ssfList.size() == 0) { // ??????????????????
            return BaseSQLiteBO.INVALID_INT_ID;
        }

        for (SmallSheetFrame ssf : ssfList) {
            if (ssf.getID() == Long.valueOf(configGeneralFromDB.getValue())) {
                return Integer.valueOf(configGeneralFromDB.getValue());
            }
        }
        // ???????????????????????????????????????????????????????????????????????????????????????1???????????????????????????????????????????????????app???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        configGeneralFromDB.setValue(String.valueOf(SmallSheet1Activity.Default_SmallSheetID_INPos));
        configGeneralFromDB.setReturnObject(1); //...
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
            log.error("?????????????????????????????????configGeneral=" + configGeneralFromDB);
        }
        return Integer.valueOf(String.valueOf(SmallSheet1Activity.Default_SmallSheetID_INPos));
    }

    /**
     * ???????????????
     */
    public void printSmallSheet(RetailTrade retailTrade) throws Exception {
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(Long.valueOf(currentSmallSheetID));
        SmallSheetFrame smallSheetFrame1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && smallSheetFrame1 == null) {
            log.error("smallSheetFramePresenter.retrieve1ObjectSync??????,?????????=" + smallSheetFramePresenter.getLastErrorCode());
            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SmallSheetText> listSmallSheetText = (List<SmallSheetText>) smallSheetFrame1.getListSlave1();

        //???????????????????????????????????????  ?????????1.5?????????
        if (retailTrade.getSourceID() > 0) {
            AidlUtil.getInstance().printText("?????????",
                    30,//??????30
                    true,//??????
                    17,//??????
                    false);//????????????
            AidlUtil.getInstance().linewrap(1);//????????????
        }

        //??????????????????????????????????????????
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
        // ??????????????????
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getContent() + sdf.format(retailTrade.getSaleDatetime()), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        String[] saCommodityAttribute = new String[]{"????????????", "??????", "??????"};
        int[] colsWidthArrc = new int[]{2, 1, 1};//?????????????????????
        int[] colsAlign = new int[]{0, 1, 2};//?????????????????????
        String[][] listCommodity = null;// ?????????????????????
        //
        List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        if (listRtc == null) {
            Toast.makeText(getContext(), "????????????????????????????????????", Toast.LENGTH_LONG).show();
            return;
        }
        listCommodity = new String[listRtc.size()][3];
        int i = 0;//????????????????????????
        Double totalPriceOriginal = 0.000000d; // ??????????????????????????????????????????
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
     * ?????????????????????????????????????????????Handle??????UI??????
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
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     */
    protected void initialization() {
        retailTradeAggregation = null;
        BaseActivity.showCommList = new ArrayList<>();
        BaseActivity.retailTrade = new RetailTrade();//?????????????????????
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

    //    //Fragemnt???????????? onKeyDown????????????Activity?????????????????????fragment??????
//    public void onKeyDownChild(int keyCode, KeyEvent event) {
//
//    }
    //?????????????????????
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

    //??????????????????????????????
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


    //????????????
    EditText editText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void DisplayCustomKeyBoard(View view, final boolean canSeeEn, final boolean canSeeSymbol) {
        editText = (EditText) view;
        editText.setShowSoftInputOnFocus(false);
        int[] location = new int[2];
        editText.getLocationOnScreen(location);//?????????????????????????????????????????????????????????????????????
        int x = location[0];
        int y = location[1];
        //??????????????????
        Number_EnglishBoardDialog.createdialog(getActivity())
                .setCanSeeEn(canSeeEn)
                .setCanSeeSymbol(canSeeSymbol)
                .setDeviation(x, y)
                .setGettext(editText.getText().toString().trim())
                .setOutputListener(new Number_EnglishBoardDialog.outputStringListener() {
                    @Override
                    public void outputString(String output) {   //?????????????????????output???????????????
                        editText.setText(output);
                        editText.setSelection(editText.length());
                    }
                })
                .Showdialog();
    }

//    //??????tEditText????????????
//    public void setEditTextPaste(EditText editText) {
//        editText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
//        editText.setLongClickable(false);
//        editText.setTextIsSelectable(false);
//        editText.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());//????????????
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            editText.setCustomInsertionActionModeCallback(new ActionModeCallbackInterceptor()); //????????????
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


    //?????????????????????????????????   ---??????????????????????????????????????????
    abstract class CustomDialog extends Dialog {
        private Button sure, cancel;
        private int layout;//?????????????????????
        private boolean TouchOutside;
        private double width, height;
        private KeyBoard keyBoard; //??????
        EditText reserve_et;//????????????alertdialog????????????????????????


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

            sure = findViewById(R.id.confirm_reserve);
            cancel = findViewById(R.id.cancel);
            reserve_et = findViewById(R.id.reserve_et);
            reserve_et.setShowSoftInputOnFocus(false);

//            setEditTextPaste(reserve_et);
            // reserve_et.setFocusable(false);
            //   reserve_et.clearFocus();

            //??????dialog??????
            WindowManager manager = getActivity().getWindowManager();
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
                    reserve_et.setText(number);//???????????????edittext
                    reserve_et.setSelection(number.length());
                }
            });

        }

        public abstract void do_sure(String reserve);
    }
}
