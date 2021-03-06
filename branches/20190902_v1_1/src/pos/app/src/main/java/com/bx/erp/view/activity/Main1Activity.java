package com.bx.erp.view.activity;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CouponCodeHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeCouponSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.coupon.CouponCalculator;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.CouponCodeHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.ReturnCommodityHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.helper.ScanGun;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.PackageUnit;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.model.PromotionShopScope;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.model.Vip;
import com.bx.erp.model.WXPayInfo;
import com.bx.erp.presenter.BarcodesPresenter;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.presenter.CouponPresenter;
import com.bx.erp.presenter.PackageUnitPresenter;
import com.bx.erp.presenter.PosPresenter;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.presenter.PromotionShopScopePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradeCouponPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.promotion.PromotionCalculator;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.utils.WXPayUtil;
import com.bx.erp.view.adapter.CommodityRecyclerViewAdapter1;
import com.bx.erp.view.adapter.DialogCommodityRecyclerViewAdapter1;
import com.bx.erp.view.component.CustomRadioButtom;
import com.bx.erp.view.component.CustomRaidoGroup;
import com.bx.erp.view.component.KeyBoard;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_InvalidSession;
import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_NoError;
import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_OtherError;
import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_WechatServerError;
import static com.bx.erp.utils.GeneralUtil.sub;

public class Main1Activity extends BaseFragment1 implements View.OnTouchListener, View.OnClickListener {
    private static Logger log = Logger.getLogger(Main1Activity.class);
    private static NetworkUtils networkUtils = new NetworkUtils();
    @BindView(R.id.balance_tv)
    TextView balanceTv;
    @BindView(R.id.list)
    TextView bill;
    @BindView(R.id.payment_close)
    TextView payment_close;
    @BindView(R.id.payment)
    FrameLayout payment;//????????????
    @BindView(R.id.favorites_page)
    RelativeLayout favorites_page;//???????????????
    @BindView(R.id.vip_page)
    RelativeLayout vip_page;//??????????????????
    @BindView(R.id.favoritesAndvip_page)
    RelativeLayout favoritesAndvip_page;//??????????????????????????????????????????
    Unbinder unbinder;
    @BindView(R.id.favorites1)
    LinearLayout favorites1;
    @BindView(R.id.favorites2)
    LinearLayout favorites2;
    @BindView(R.id.search_vip)
    TextView search_vip;
    @BindView(R.id.vipMobile)
    TextView vipMobile;
    @BindView(R.id.vipBonus)
    TextView vipBonus;
    @BindView(R.id.return_search_vip)
    TextView return_search_vip;
    @BindView(R.id.favorites_close)
    ImageView favoritesClose;
    //    @BindView(R.id.show_client_name)
//    TextView showClientName;
    @BindView(R.id.show_client_phone)
    EditText showClientPhone;
    @BindView(R.id.rv_commodity)
    RecyclerView rvCommodity;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.scan_barcode_text)
    TextView scan_barcode_text;
    @BindView(R.id.commodity_quantity)
    TextView commodityQuantity;//????????????????????????
    @BindView(R.id.totalpay_money)
    TextView totalpay_money;
    @BindView(R.id.total_money)
    TextView total_money;
    @BindView(R.id.scan_barcode_search)
    ImageView scan_barcode_search;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.choose_payment_method)
    CustomRaidoGroup choosePaymentType;
    @BindView(R.id.wechat_pay)
    CustomRadioButtom wechatPay;
    @BindView(R.id.cash_pay)
    CustomRadioButtom cashPay;
    @BindView(R.id.wechat_paying)
    TextView tvWechatPaying;
    @BindView(R.id.cash_paying)
    TextView tvCashPaying;
    @BindView(R.id.wechat_paying_prefix)
    TextView wechat_paying_prefix;
    @BindView(R.id.cash_paying_prefix)
    TextView cash_paying_prefix;
    @BindView(R.id.last_amount)
    TextView lastRetailTradeAmount;
    @BindView(R.id.last_paymenttype)
    TextView lastRetailTradePaymentType;
    @BindView(R.id.last_changemoney)
    TextView lastRetailTradeChangeMoney;
    /**
     * ????????????????????????????????????
     */
    @BindView(R.id.wechat_paying_amount)
    TextView etWechatPayingAmount;
    /**
     * ????????????????????????????????????
     */
    @BindView(R.id.cash_paying_amount)
    TextView etCashPayingAmount;
    @BindView(R.id.view)
    View viewMain1Activity;
    @BindView(R.id.keyboard_pay)
    KeyBoard keyBoard;
//    @BindView(R.id.remove_commodity)
//    TextView remove_commodity;

    List<Commodity> commodityListAfterQuery = new ArrayList<Commodity>();
    private List<Object> objectList = new ArrayList<Object>();
    int screenWidth;
    int screenHeight;
    private RecyclerView rv_alertdialog_commodity;
    TextView tv_inventory_search;
    TextView stock;
    EditText search_commodity_et;
    ImageView ivDelete_all;
    DialogCommodityRecyclerViewAdapter1 dialogCommodityRecyclerViewAdapter;
    CommodityRecyclerViewAdapter1 commodityRecyclerViewAdapter;

    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    private BarcodesPresenter barcodesPresenter;
    private RetailTradePresenter retailTradePresenter;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    private CommodityPresenter commodityPresenter;
    private PackageUnitPresenter packageUnitPresenter;
    private PromotionPresenter promotionPresenter;
    private PromotionShopScopePresenter promotionShopScopePresenter;
    private RetailTradePromotingPresenter retailTradePromotingPresenter;
    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
    private PosPresenter posPresenter;
    private final static String DEFAULT_changeAmount = "0.00"; //?????????????????????????????????0.00

    // ?????????????????????????????????????????????
    private String Default_CommodityMinNumber = "1";
    // ?????????????????????????????????????????????
    private int Default_CommodityMaxNumber = 9999;
    //???????????????????????????????????????????????????????????????
    private String lastQueryKeyWordBySearchButton = "";
    //???????????????????????????????????????????????????
    private Date lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
    //???????????????????????????????????????????????????
    private Date lastDatetimeEndBySearchButton = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);

    private List<RetailTradeCommodity> retailTradeCommodityAfterConfirmReturn = new ArrayList<RetailTradeCommodity>();//???????????????????????????????????????????????????List
    List<RetailTrade> showRetailTradeList = new ArrayList<>();//?????????????????????????????????
    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    // ?????????????????????????????????????????????????????????????????????????????????????????????retailTrade???????????????????????????????????????????????????????????????????????????
    RetailTrade retailTradeAfterConfirmReturn;
    private RetailTrade retailTradeSelected; //?????????????????????????????????
    private RetailTradePromoting retailTradePromoting;
    private int commNumber = 0;//?????????????????????????????????????????????????????????
    private int noCanReturn = 0;//???????????????????????????????????????????????????
    private int remainingQuantity;//??????????????????????????????????????????????????????????????????
    private double dAmountToReturnToCustomer = 0.000000d;
    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private static boolean isPayingViaCash = false;
    /**
     * ????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
    private boolean bLastWxPayIsNotYetDone = false;
    private boolean bCanPayViaCashOnly = false;
    private HandlerMessage handlerMessage = new HandlerMessage();
    /**
     * ??????????????????????????????????????????
     */
    private final int SubErrorCode_RetailTrade_OtherError = 1;
    /**
     * ????????????????????????????????????????????????????????????
     */
    private final int SubErrorCode_RetailTradePromoting_OtherError = 2;
    /**
     * ????????????????????????????????????????????????????????????
     */
    private final int SubErrorCode_RetailTradeCoupon_OtherError = 3;
    /**
     * ????????????????????????????????????????????????
     */
    private final int SubErrorCode_RetailTrade_VipUpload_OtherError = 4;
    private int paymentCode = 0b00000000;//?????????????????????????????????????????????paymentType?????????paymentCode + ????????????????????????????????????
    private final int wechatPayCode = 0b00000100;//?????????????????????????????????????????????
    private final int alipayPayCode = 0b00000010;
    private final int cashPayCode = 0b00000001;
    private String wxPayAuthCode = ""; // ?????????????????????
    // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    private boolean firstTimeEnterMain1Activity = true;
    private double total_money_double = 0.000000d;//???????????????double??????????????????????????????????????????????????????SQLite???????????????????????????????????????
    private String wxOutRefundNO;//??????????????????
    private String wxOutRefundErrorMsg;//????????????????????????
    public static boolean bPaying = false;
    public static final String MAX_CashPaymentAmount = "99999";
    private final static int LENGTH_WxPayAuthCode = 18;
    private final static double MIN_WechatPaymentAmount = 0.00; // ????????????????????????????????????
    /**
     * ???????????????????????????????????????????????????
     */
    private double wetchatAmount;//
    /**
     * ??????????????????????????????????????????????????????
     */
    private double alipayAmount;//
    /**
     * ???????????????????????????????????????????????????
     */
    private double cashAmount;//
    private Map<String, String> microPayResponse; // ??????????????????

    private double totalWechatAmount;

    private double totalCashAmount;

    private RetailTrade tempCalculatedRetailTrade = null;

    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;

    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;

    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;

    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;

    private static VipHttpBO vipHttpBO = null;
    private static VipHttpEvent vipHttpEvent = null;

    private static CouponCodeHttpBO couponCodeHttpBO = null;
    private static CouponCodeHttpEvent couponCodeHttpEvent = null;

    private static CouponPresenter couponPresenter = null;
    private static RetailTradeCouponPresenter retailTradeCouponPresenter = null;
    private static RetailTradeCouponSQLiteBO retailTradeCouponSQLiteBO = null;

    private static WXPayHttpEvent wxPayHttpEvent = null;
    private static WXPayHttpBO wxPayHttpBO = null;
    private ScanGun scanGun = null;

    //    /*
//     * ????????????(???APP?????????)???????????????
//     * */
//    private IntentFilter intentFilter;
//    private DayEndGenerateRetailTradeAggregationReceiver dayEndGenerateRetailTradeAggregationReceiver;
//    private LocalBroadcastManager localBroadcastManager;
    private AlertDialog cancelPayDialog;
    //??????Vip??????
    private static Vip vip = null;
    // ???Vip??????????????????
    private static List<CouponCode> couponCodeList = new ArrayList<CouponCode>();
    // UI????????????????????????
    private static List<CouponCode> showCouponCodeList = new ArrayList<CouponCode>();
    // ?????????????????????????????????
    private static CouponCode couponCode = null;
    // ????????????????????????????????????????????????
    private static Coupon coupon = null;
    // ????????????????????????????????????
    private static double beforeUsingCouponAmount = 0.0000000d;

    private final String COUPONTITLELIST_DEFAULT_UnavailableConpon = "?????????????????????";
    private final String COUPONTITLELIST_DEFAULT_AvailableConpon = "?????????????????????";

    Base1FragmentActivity activity;

    //    @BindView(R.id.cash_payment_money)
//    TextView cashPaymentMoney;
    @BindView(R.id.unpaid_balance)
    TextView unpaidBalance;
    @BindView(R.id.change_money)
    TextView changeMoney;

    private Date getDefaultSyncDatetime() {
        try {
            return Constants.getDefaultSyncDatetime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onBackFromBillFragment() {
        if (BaseActivity.retailTrade != null && BaseActivity.retailTrade.getVip() != null) {
            loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
            vip = BaseActivity.retailTrade.getVip();
            //
            showClientPhone.setVisibility(View.GONE);
            search_vip.setVisibility(View.GONE);
            //
            vipMobile.setText("??????????????????" + vip.getMobile());
            vipBonus.setText("?????????" + vip.getBonus());
            vipMobile.setVisibility(View.VISIBLE);
            vipBonus.setVisibility(View.VISIBLE);
            return_search_vip.setVisibility(View.VISIBLE);
            CouponCode couponCode = new CouponCode();
            couponCode.setVipID(vip.getID().intValue());
            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                showToastMessage("??????????????????????????????");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main1, container, false);

        activity = (Base1FragmentActivity) getActivity();//???????????????????????????Base1FragmentActivity????????????
        HideVirtualKeyBoard();
//        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());//??????localBroadcastManager??????
        unbinder = ButterKnife.bind(this, view);
        showClientPhone.setOnTouchListener(this);
        //??????EditText??????????????????????????????????????????
        scan_barcode_text.setShowSoftInputOnFocus(false);
        scan_barcode_text.setFocusableInTouchMode(false);
        etWechatPayingAmount.setShowSoftInputOnFocus(false);
        etWechatPayingAmount.setFocusableInTouchMode(false);
        etWechatPayingAmount.setEnabled(false);
        scan_barcode_search.setOnClickListener(this);
        scan_barcode_text.setOnClickListener(this);
        viewMain1Activity.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;//????????????
        screenHeight = dm.heightPixels;//????????????
        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        barcodesPresenter = GlobalController.getInstance().getBarcodesPresenter();
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        packageUnitPresenter = GlobalController.getInstance().getPackageUnitPresenter();
        promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        promotionShopScopePresenter = GlobalController.getInstance().getPromotionShopScopePresenter();
        retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
        retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
        posPresenter = GlobalController.getInstance().getPosPresenter();
        initEventAndBO();
        if (firstTimeEnterMain1Activity) {
            CustomDialog dialog = new CustomDialog(getActivity(), R.layout.reserve_dialog, false, 0.5, 0.65) {
                @Override
                public void do_sure(String s) {
                    Boolean DecimalPointOnErrorPosition = s.indexOf(".") == s.length() - 2
                            || s.indexOf(".") == s.length() - 1;
                    if (s.indexOf(".") != -1 && DecimalPointOnErrorPosition) {
                        reserve_et.setError("??????????????????????????????????????????????????????");
                    } else if ("".equals(s) || null == s) {
                        reserve_et.setError("????????????????????????");
                    } else if (Double.valueOf(s) > 10000d) {
                        reserve_et.setError("??????????????????????????????10000");
                    } else if (s.startsWith(".")) {
                        reserve_et.setError("????????????????????????????????????????????????????????????");
                    } else {
                        BaseActivity.retailTradeAggregation.setReserveAmount(Double.valueOf(s));
                        createRetailTradeAggregation(); //TODO ?????????????????????????????????
                        dismiss();
                    }
                }
            };
            dialog.show();
            firstTimeEnterMain1Activity = false;
        }
        initializeVipData();
        // ???????????????????????????
        onBackFromBillFragment();
        initScanGun();

        payment.setOnClickListener(new View.OnClickListener() {
            // ?????????????????????????????????????????????
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        favoritesAndvip_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????????????????????????????????????????????
            }
        });

        keyBoard.setOnNumberclickListener(new KeyBoard.OnNumberclickListener() {
            @Override
            public void onClick(String number) {
                getPayingMoney().setText(number);//???????????????edittext
            }
        });
        // Fragment?????????????????????????????????????????????
        if (BaseActivity.showCommList.size() > 0) {
            showCommodity(BaseActivity.showCommList);
        }
        return view;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
        } else {
            log.info("????????????Event???ID=" + event.getId() + "syncThread");
        }
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
        } else {
            log.info("????????????Event???ID=" + event.getId() + "syncThread");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBrandHttpEvent(BrandHttpEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetrieveNRerailTrade(RetailTradeEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipHttpEvent(VipHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            Message message = new Message();
            message.what = 5;
            handler.sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            if ("WXRefund".equals(event.getWxPayStatus())) {
                wxOutRefundNO = event.getRefundResponse().get("out_refund_no");
                event.setWxPayStatus("");
            } else if ("WXRefundFail".equals(event.getWxPayStatus())) {
                wxOutRefundErrorMsg = event.getLastErrorMessage(); // ???????????????????????????
                event.setWxPayStatus("");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReturnCommodityEvent(ReturnCommodityHttpEvent event) {
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponCodeHttpEvent(CouponCodeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            Message message = new Message();
            message.what = 6;
            handler.sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    private void setSpinner() {
        final List<String> couponCodeTitleList = getCouponCodeTitleList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2, couponCodeTitleList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HideVirtualKeyBoard();
                String couponTitle = couponCodeTitleList.get(i);
                if (!(COUPONTITLELIST_DEFAULT_AvailableConpon.equals(couponTitle) || COUPONTITLELIST_DEFAULT_UnavailableConpon.equals(couponTitle))) {
                    couponCode = showCouponCodeList.get(i - 1); // i-1?????????i???????????????"??????????????????"???"??????????????????".???showCouponCodeList????????????????????????????????????????????????????????????-1
                    System.out.println("???????????????????????????" + couponCode);
                    Coupon queryCoupon = new Coupon();
                    queryCoupon.setID(Long.valueOf(couponCode.getCouponID()));
                    coupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
                    if (couponPresenter.getLastErrorCode() != EC_NoError) {
                        couponCode = null;
                        log.error("????????????????????????????????????????????????????????????");
                        Toast.makeText(getActivity().getApplicationContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        double amountToPay = CouponCalculator.calculateAmountUsingCoupon(coupon, beforeUsingCouponAmount, BaseActivity.showCommList, BaseActivity.retailTrade);

                        totalpay_money.setText(GeneralUtil.formatToShow(amountToPay));
                        unpaidBalance.setText(GeneralUtil.formatToShow(amountToPay));
                        etWechatPayingAmount.setText(GeneralUtil.formatToShow(amountToPay));
                        etCashPayingAmount.setText(GeneralUtil.formatToShow(amountToPay));
                        BaseActivity.retailTrade.setAmount(amountToPay);
                    }
                } else {
                    couponCode = null;
                    coupon = null;
                    BaseActivity.retailTrade.setAmount(beforeUsingCouponAmount);
                    totalpay_money.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                    unpaidBalance.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                    etWechatPayingAmount.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                    etCashPayingAmount.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * TODO ?????????????????????????????????????????????????????????
     *
     * @return
     */
    private List<String> getCouponCodeTitleList() {
        List<String> couponCodeTitleList = new ArrayList<>();
        if (couponCodeList.size() == 0) {
            couponCodeTitleList.add(COUPONTITLELIST_DEFAULT_UnavailableConpon);
        } else {
            couponCodeTitleList.add(COUPONTITLELIST_DEFAULT_AvailableConpon);
            Coupon coupon = new Coupon();
            for (CouponCode couponCode : couponCodeList) {
                coupon.setID(Long.valueOf(couponCode.getCouponID()));
                Coupon queryCoupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, coupon);
                if (couponPresenter.getLastErrorCode() != EC_NoError) {
                    log.error("????????????????????????????????????????????????????????????");
                    continue;
                }
                if (queryCoupon != null) {
                    double amount = 0.000000d;
                    // ?????????????????????????????????,???????????????????????????????????????????????????????????????????????????
                    List<CouponScope> couponScopes = (List<CouponScope>) queryCoupon.getListSlave1();
                    if (couponScopes != null && couponScopes.size() > 0) {
                        for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
                            Commodity commodity = BaseActivity.showCommList.get(i);
                            for (int j = 0; j < couponScopes.size(); j++) {
                                CouponScope couponScope = couponScopes.get(j);
                                if (commodity.getID() == couponScope.getCommodityID()) {
                                    amount = GeneralUtil.sum(amount, commodity.getSubtotal());
                                }
                            }
                        }
                    } else {
                        amount = BaseActivity.retailTrade.getAmount();
                    }
                    // ?????????????????????????????????
                    if (amount >= queryCoupon.getLeastAmount() && isCouponInDatetimeScope(queryCoupon)) {
                        showCouponCodeList.add(couponCode);
                        couponCodeTitleList.add(queryCoupon.getTitle());
                    }
                }
            }

            // vip???????????????????????????????????????????????????????????????????????????????????????"??????????????????"
            if (showCouponCodeList.size() == 0) {
                couponCodeTitleList.clear();
                couponCodeTitleList.add(COUPONTITLELIST_DEFAULT_UnavailableConpon);
            }
        }
        return couponCodeTitleList;
    }

    private boolean isCouponInDatetimeScope(Coupon coupon) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_ConfigGeneral);
        Date now = new Date();
        try {
            // ????????????????????????????????????
            if (DatetimeUtil.isInDatetimeRange(coupon.getBeginDateTime(), now, coupon.getEndDateTime())) {
                // ??????????????????????????????????????????????????????????????? 9???00-11???00???????????????????????????11??????????????????
                if (coupon.getWeekDayAvailable() != 0) {
                    Date beginTime = simpleDateFormat.parse(coupon.getBeginTime());
                    Date endTime = simpleDateFormat.parse(coupon.getEndTime());
                    if (DatetimeUtil.isInWeekday(null, now, coupon.getWeekDayAvailable()) && DatetimeUtil.isInTime(beginTime, endTime, now)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static class ResponseDayEndGenerateRetailTradeAggregationReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            log.debug("??????????????????");
//            //?????????????????????????????????BaseActivity.retailTradeAggregation.getID() != null??????????????????????????????????????????????????????
//            //?????????????????????????????????????????????????????????
//            if (BaseActivity.retailTradeAggregation.getID() != null) {
//                if (intent.getAction().equals(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation)) {
//                    log.debug("?????????????????????" + DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
//                    createRetailTradeAggregationForNextDay();
//                }
//            }
//        }
//    }

//    /* *
//     * ???????????????????????????????????????????????????sqlite????????????????????????
//     */
//    private static void createRetailTradeAggregationForNextDay() {
//        Date tmr = DatetimeUtil.get2ndDayStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//        if (!DatetimeUtil.isAfterDate(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), tmr, 0)) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        //????????? BaseActivity.retailTradeAggregation???????????????
//        if (BaseActivity.retailTradeAggregation != null) {
//            BaseActivity.retailTradeAggregation.setWorkTimeStart(tmr);
//            BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
//            BaseActivity.retailTradeAggregation.setTradeNO(0);
//            BaseActivity.retailTradeAggregation.setAmount(0.000000d);
//            BaseActivity.retailTradeAggregation.setCashAmount(0.000000d);
//            BaseActivity.retailTradeAggregation.setWechatAmount(0.000000d);
//            //
//            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//            retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//            retailTradeAggregation.setPosID(Constants.posID);
//            retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//            retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
//            retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
//            //
//            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
//            if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
//                log.error("?????????????????????????????????????????????");//TODO ?????????????????????
//            }
//        }
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (dayEndGenerateRetailTradeAggregationReceiver != null) {
//            dayEndGenerateRetailTradeAggregationReceiver.unregisterReceiver();
//        }
        unbinder.unbind();
    }

    //????????????
    @OnClick({R.id.favorites1, R.id.favorites2, R.id.favorites_close, R.id.balance_tv, R.id.list, R.id.payment_close, R.id.vip_close, R.id.pay, R.id.search_vip, R.id.return_search_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_vip:// ????????????
                showClientPhone.clearFocus();//????????????????????????????????????????????????????????????
                //?????????????????????????????????????????????
                String queryKeyword = showClientPhone.getText().toString();
                //???????????????????????????
                initializeVipData();
                if (queryKeyword.length() == FieldFormat.LENGTH_Mobile || queryKeyword.length() == Vip.Max_LENGTH_VipCardSN) {
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);

                    Vip vip = new Vip();
                    if (queryKeyword.length() == FieldFormat.LENGTH_Mobile) {
                        vip.setMobile(queryKeyword);
                    } else {
                        vip.setVipCardSN(queryKeyword);
                    }
                    if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getActivity().getApplication())) { // ????????????
                        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
                            showToastMessage("?????????????????????????????????");
                            log.error("?????????????????????" + queryKeyword);
                        }
                    } else {   // ??????
                        showToastMessage(BaseHttpBO.ERROR_MSG_Network);
                        loadingDailog.dismiss();
                    }
                } else {
                    showToastMessage(Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);
                }

                break;
            case R.id.return_search_vip: // ????????????????????????
                initializeVipData();
                break;
            case R.id.favorites1://???????????????
                favorites_page.setVisibility(View.VISIBLE);//?????????????????????
                favorites2.setVisibility(View.GONE);//?????????????????????
                favoritesAndvip_page.setVisibility(View.VISIBLE);//???????????????????????????????????????
                setAnimation(favorites_page);
                break;
            case R.id.favorites2://??????????????????????????????
                favorites_page.setVisibility(View.VISIBLE);//?????????????????????
                favorites2.setVisibility(View.GONE);//?????????????????????
                favoritesAndvip_page.setVisibility(View.VISIBLE);//???????????????????????????????????????
                setAnimation(favorites_page);
                break;
            case R.id.favorites_close://?????????????????????
                favorites_page.setVisibility(View.GONE);//?????????????????????
                favorites2.setVisibility(View.VISIBLE);//?????????????????????
                if (favorites_page.getVisibility() == View.GONE & vip_page.getVisibility() == View.GONE) {//????????????????????????????????????????????????
                    favoritesAndvip_page.setVisibility(View.GONE);
                    setAnimation(favoritesAndvip_page);
                } else {
                    setAnimation(favorites_page);
                }

                break;
            case R.id.vip_close://????????????????????????
                vip_page.setVisibility(View.GONE);//??????????????????
                if (favorites_page.getVisibility() != View.VISIBLE) {
                    favorites2.setVisibility(View.VISIBLE);
                }
                if (favorites_page.getVisibility() == View.GONE & vip_page.getVisibility() == View.GONE) {//????????????????????????????????????????????????
                    favoritesAndvip_page.setVisibility(View.GONE);
                } else {
                    setAnimation(vip_page);
                }

                break;
            case R.id.balance_tv://????????????
                choosePaymentType.setOnCheckedChangeListener(null);
                showClientPhone.clearFocus(); //?????????????????????????????????????????????
                // ???????????????????????????
                if (GlobalController.getInstance().getSessionID() == null || (!NetworkUtils.isNetworkAvalible(getActivity().getApplicationContext()))) { // xxx ?????????????????????
                    changeRadioButtonToPayViaCashOnly();
                    paymentCode = 0b00000000;
                    pay.setText("???  ???");
                    pay.setBackgroundResource(R.drawable.button_bg_red);
                    pay.setEnabled(true);
                } else {
                    changeRadioButtonToPayViaWX();
                }
                balance();
                break;
            case R.id.list://????????????
                // ??????????????????????????????list??????????????????????????????????????????
                if (BaseActivity.retailTrade != null && BaseActivity.retailTrade.getListSlave1() != null && BaseActivity.retailTrade.getListSlave1().size() > 0) {
                    if (BaseActivity.retailTradeHoldBillList.size() >= BaseActivity.MAX_HOLD_BILL_NO) {
                        Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????" + BaseActivity.MAX_HOLD_BILL_NO + "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        RetailTrade retailTrade = (RetailTrade) BaseActivity.retailTrade.clone();
                        retailTrade.setHoldBillTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                        if (vip != null) {
                            retailTrade.setVip(vip);
                        }
                        //
                        List<Commodity> commodityList = new ArrayList<Commodity>();
                        try {
                            for (Commodity commodity : BaseActivity.showCommList) {
                                Commodity c = (Commodity) commodity.clone();
                                commodityList.add(c);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        retailTrade.setFirstCommodityName(commodityList.get(0).getName());
                        //
                        retailTrade.setCommodityListOfHeldBill(commodityList);
                        BaseActivity.retailTradeHoldBillList.add(retailTrade);
                        resetTradeAfterHoldBill();
                        initializeVipData();
                        Toast.makeText(getActivity().getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.payment_close://??????????????????
                confirmPaymentClose();
                break;
            case R.id.pay:
                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
                if (!isPayingViaCash && BaseActivity.retailTrade != null) {
                    // ???????????????????????????
                    if (!validateCouponCodeAvailable()) {
                        break;
                    }
                    if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) { // ????????????WX??????
                        if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || Double.valueOf(etWechatPayingAmount.getText().toString()) <= 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "??????????????????????????????0", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    payViaWX(Double.valueOf(getPayingMoney().getText().toString()));
                                }
                            }).start();
                        }
                    } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) { // ????????????
                        if (StringUtils.isEmpty(etCashPayingAmount.getText().toString()) || Double.valueOf(etCashPayingAmount.getText().toString()) < 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????0", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else if ((etCashPayingAmount.getText().toString().length() - etCashPayingAmount.getText().toString().indexOf(".")) == 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    payViaCash(Double.valueOf(getPayingMoney().getText().toString()));
                                }
                            }).start();
                        }
                    }
                }
                break;
        }
    }

    private void confirmPaymentClose() {
        final AlertDialog.Builder cancelPayBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("??????")
                .setMessage("??????????????????????????????,???????????????????????????")
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelPayment();
                        resetPayment();
                        showCommodity(BaseActivity.showCommList);
                        couponCode = null;
                        coupon = null;
                        showCouponCodeList.clear();
                    }
                });
        cancelPayDialog = cancelPayBuilder.create();
        cancelPayDialog.show();
    }

    private void cancelPayment() {
        if (Double.parseDouble(totalpay_money.getText().toString()) - Double.parseDouble(unpaidBalance.getText().toString()) > RetailTrade.TOLERANCE) { //????????????????????????
            // ??????????????????????????????????????????????????????????????????????????????????????????????????? ????????????????????????
            if (!NetworkUtils.isNetworkAvalible(getActivity().getApplicationContext()) || totalCashAmount - 0.000000d > RetailTrade.TOLERANCE) {
                Toast.makeText(getActivity().getApplicationContext(), "?????????????????????" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "???!", Toast.LENGTH_LONG).show();
            } else if (totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                // ????????????????????????????????????????????????????????????????????????????????????
                if (wxRefund()) {
                    Toast.makeText(getActivity().getApplicationContext(), "??????????????????, ??????????????????", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "??????????????????????????????????????????" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "???!", Toast.LENGTH_LONG).show();
                }
            } else {
                //????????????????????????0??????????????????0
                throw new RuntimeException("?????????????????????");
            }
            //
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //????????????
    private boolean wxRefund() {
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setAmount(WXPayUtil.formatAmount(totalWechatAmount));
        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
        retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc"));
        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
        log.info("???????????????????????????????????????" + retailTrade);
        //??????????????????
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        wxPayHttpBO.refundAsync(retailTrade);
        long lTimeOut = TIME_OUT;
        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        //
        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            log.info("??????????????????....");
            return false;
        }
        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.info("??????????????????....");
            return false;
        }

        return true;
    }

    //????????????
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
//            case R.id.show_client_name://?????????????????????
//                vip_page.setVisibility(View.VISIBLE);//??????????????????
//                setAnimation(vip_page);
//                favoritesAndvip_page.setVisibility(View.VISIBLE);//????????????
//                favorites2.setVisibility(View.GONE);//????????????????????????????????????
//                break;
            case R.id.show_client_phone://?????????(?????????)?????????
                if (vip_page.getVisibility() == View.GONE) {
                    vip_page.setVisibility(View.VISIBLE);//??????????????????
                    setAnimation(vip_page);
                    favoritesAndvip_page.setVisibility(View.VISIBLE);//????????????
                    favorites2.setVisibility(View.GONE);//????????????????????????????????????
                }
                //??????????????????
                DisplayCustomKeyBoard(view, false, false);
                return false;
        }
        return false;
    }

    private void initEventAndBO() {
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
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (retailTradePromotingHttpBO == null) {
            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        if (retailTradePromotingSQLiteBO == null) {
            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        //
        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
        }
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);

        if (vipHttpEvent == null) {
            vipHttpEvent = new VipHttpEvent();
            vipHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (vipHttpBO == null) {
            vipHttpBO = new VipHttpBO(GlobalController.getInstance().getContext(), null, vipHttpEvent);
        }
        vipHttpEvent.setHttpBO(vipHttpBO);

        if (couponCodeHttpEvent == null) {
            couponCodeHttpEvent = new CouponCodeHttpEvent();
            couponCodeHttpEvent.setId(BaseEvent.EVENT_ID_MainActivity);
        }
        if (couponCodeHttpBO == null) {
            couponCodeHttpBO = new CouponCodeHttpBO(GlobalController.getInstance().getContext(), null, couponCodeHttpEvent);
        }
        couponCodeHttpEvent.setHttpBO(couponCodeHttpBO);

        couponPresenter = GlobalController.getInstance().getCouponPresenter();

        retailTradeCouponPresenter = GlobalController.getInstance().getRetailTradeCouponPresenter();
        if (retailTradeCouponSQLiteBO == null) {
            retailTradeCouponSQLiteBO = new RetailTradeCouponSQLiteBO(GlobalController.getInstance().getContext(), null, null);
        }
    }

    /**
     * User-Agent: Fiddler
     * Host: 192.168.1.20:8080
     * Content-Type: application/x-www-form-ulencoded
     */
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_barcode_text:
            case R.id.scan_barcode_search:  //?????????????????????????????????????????????????????????????????????????????????
                searchCommodityByBarcode();
                break;
            case R.id.view:
                confirmPaymentClose();
            default:
                break;
        }
    }

    private void searchCommodityByBarcode() {
        log.debug("????????????????????????");
        commodityListAfterQuery.clear();

        AlertDialog.Builder commodity_builder = null;
        View commodity_view = null;
        AlertDialog commodity_dialog = null;
        objectList = createAlertDialog(commodity_builder, commodity_view, commodity_dialog, (int) (screenWidth * 0.75), (int) (screenHeight * 0.75), R.layout.choose_commodity_dialog1);
        commodity_view = (View) objectList.get(1);
        rv_alertdialog_commodity = commodity_view.findViewById(R.id.rv_alertdialog_commodity);
        rv_alertdialog_commodity.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));//??????????????????????????????????????????????????????

        tv_inventory_search = commodity_view.findViewById(R.id.tv_inventory_search);
//                search_view = commodity_view.findViewById(R.id.search_view);
        tv_inventory_search.setVisibility(View.GONE);
//                search_view.setVisibility(View.GONE);
        stock = commodity_view.findViewById(R.id.stock);
        search_commodity_et = commodity_view.findViewById(R.id.search_commodity_et);
        search_commodity_et.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DisplayCustomKeyBoard(search_commodity_et, true, false);
                return false;
            }
        });
        search_commodity_et.addTextChangedListener(new TextWatcher() {//?????????????????????????????????????????????????????????recyclerview?????????(??????)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(search_commodity_et.getText().toString())) {
                    ivDelete_all.setVisibility(View.INVISIBLE);
                } else if (!"".equals(search_commodity_et.getText().toString())) {
                    ivDelete_all.setVisibility(View.VISIBLE);
                }
                search_commodity_et.setSelection(search_commodity_et.getText().length());
                //???????????????????????????Barcode????????????????????????id?????????ID???Commodity????????????
                String commodityBarcodes = search_commodity_et.getText().toString();
                if (commodityBarcodes.length() > BaseActivity.FUZZY_QUERY_LENGTH) {
                    log.debug("?????????????????????????????????????????????" + commodityBarcodes);
                    commodityListAfterQuery = retrieveNCommodityInSQLite(true, commodityBarcodes);
                    dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter1(commodityListAfterQuery, getActivity().getApplicationContext());
                    rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
                    //
                    dialogCommodityRecyclerViewAdapter.setOnItemListener(new DialogCommodityRecyclerViewAdapter1.OnItemListener() {
                        @Override
                        public void onClick(DialogCommodityRecyclerViewAdapter1.MyViewHolder holder, int position) {
                            dialogCommodityRecyclerViewAdapter.setDefSelect(position);
                            dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                            if (commodityListAfterQuery.get(position).isSelect) {
                                //???????????????????????????????????????
                                commodityListAfterQuery.get(position).isSelect = false;
                                stock.setText("0");
                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                //???????????????????????????????????????
                                commodityListAfterQuery.get(position).isSelect = true;
                                CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityListAfterQuery.get(position).getListSlave2().get(0);
                                stock.setText(String.valueOf(commodityShopInfo.getNO()));
                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivDelete_all = commodity_view.findViewById(R.id.delete_all);
        ivDelete_all.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                search_commodity_et.setText("");
                commodityListAfterQuery.clear();
                dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter1(commodityListAfterQuery, getActivity().getApplicationContext());
                rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        search_commodity_et.setText(scan_barcode_text.getText().toString());
        TextView cancel_tv1 = commodity_view.findViewById(R.id.cancel_tv);
        cancel_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.debug("??????????????????");
                ((Dialog) objectList.get(2)).dismiss();
                commodityListAfterQuery.clear();
                HideVirtualKeyBoard();
            }
        });


        TextView add_tv1 = commodity_view.findViewById(R.id.add_tv);
        add_tv1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                if (commodityListAfterQuery.size() > 0) {
                    //?????????????????????
                    if (commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).isSelect) {
                        Commodity dialogCommodity = new Commodity();
                        dialogCommodity.setID(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getID());
                        dialogCommodity.setBarcode(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
                        //
                        isCommodityExistInUI(dialogCommodity);
                        scan_barcode_text.setHint("????????????" + commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
                        //
                        showCommodity(BaseActivity.showCommList);
                        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        commodityRecyclerViewAdapter.setDefSelect(POSITION_CommodityToRemove[0]);
                        commodityRecyclerViewAdapter.notifyDataSetChanged();
                        //
                        ((Dialog) objectList.get(2)).dismiss();
                    } else {
                        Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                HideVirtualKeyBoard();
            }
        });

        View chooseCommodityDialogView = commodity_view.findViewById(R.id.choose_commodity_dialog_view);
        chooseCommodityDialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    /**
     * ???????????????
     *
     * @param builder
     * @param view
     * @param dialog
     * @param width
     * @param height
     * @param layout
     */
    private List<Object> createAlertDialog(AlertDialog.Builder builder, View view, Dialog dialog, int width, int height, int layout) {
        List<Object> objectList = new ArrayList<>();
        builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getActivity().getApplicationContext(), layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext_rounded_background);
        objectList.add(builder);
        objectList.add(view);
        objectList.add(dialog);

        return objectList;
    }

    /**
     * ???????????????????????????
     *
     * @param barcode ?????????????????????
     * @return
     */
    private List<Commodity> retrieveNCommodityInSQLite(boolean isUseLike, String barcode) {
        List<Commodity> commodityList = new ArrayList<Commodity>();
        //???Barcodes??????????????????commodityID????????????commodityID???????????????commodity
        Barcodes barcodes = new Barcodes();
        if (isUseLike) {
            barcodes.setSql("where F_Barcode like ?");
            barcodes.setConditions(new String[]{"%" + barcode + "%"});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        } else {
            barcodes.setSql("where F_Barcode = ?");
            barcodes.setConditions(new String[]{barcode});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        }
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);

        if (barcodesList != null) {
            for (int i = 0; i < barcodesList.size(); i++) {
                Commodity commodity = new Commodity();
                commodity.setID(Long.valueOf(barcodesList.get(i).getCommodityID()));
                commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                if (commodity != null) {
                    commodity.setNumber(i + 1);
                    commodity.setBarcode(barcodesList.get(i).getBarcode());

                    PackageUnit packageUnit = new PackageUnit();
                    packageUnit.setID(Long.valueOf(commodity.getPackageUnitID()));
                    packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
                    commodity.setPackageUnit(packageUnit.getName());

                    commodityList.add(commodity);
                }
            }
        }
        log.info("commoditylist??? " + commodityList);
        return commodityList;
    }

    /**
     * ?????????Commodity?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param c
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void isCommodityExistInUI(Commodity c) {
        boolean isAddToCommList = true;//?????????????????????????????????????????????
        if (BaseActivity.showCommList.size() > 0) {//?????????????????????ID?????????????????????????????????????????????????????????????????????????????????????????????
            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
                if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() < Default_CommodityMaxNumber) {//????????????????????????????????????????????????????????????
                    BaseActivity.showCommList.get(i).setCommodityQuantity(Integer.valueOf(BaseActivity.showCommList.get(i).getCommodityQuantity()) + 1);
                    BaseActivity.showCommList.get(i).setSubtotal(BaseActivity.showCommList.get(i).getCommodityQuantity() * BaseActivity.showCommList.get(i).getAfter_discount());
                    isAddToCommList = false;
                    break;
                } else if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() >= Default_CommodityMaxNumber) {
                    Toast.makeText(appApplication, "???????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
                    isAddToCommList = false;
                    break;
                } else {
                    isAddToCommList = true;
                }
            }
        }
        if (isAddToCommList) {
            //????????????ID???????????????????????????
            String barcode = c.getBarcode();
            c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            //????????????ID???????????????barcodes
//            Barcodes barcodes = new Barcodes();
//            barcodes.setSql("where F_CommodityID = ?");
//            barcodes.setConditions(new String[]{String.valueOf(c.getID())});
//            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
            //????????????ID?????????????????????
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setID(Long.valueOf(c.getPackageUnitID()));
            packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);

            c.setBarcode(barcode);
            c.setCommodityQuantity(1);
            c.setPackageUnit(packageUnit.getName());
            c.setDiscount(0.000000d);// ??????
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) c.getListSlave2().get(0);
            c.setAfter_discount(commodityShopInfo.getPriceRetail());
            c.setPriceRetail(commodityShopInfo.getPriceRetail());
            c.setSubtotal(c.getAfter_discount() * c.getCommodityQuantity());
            System.out.println("????????????????????????comm" + c);
            BaseActivity.showCommList.add(c);
        }
        // ...????????????
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
        //...
        //????????????
        activity.isShowToCustomer(getActivity().getApplicationContext());
        //T1??????
        showInT1Host();
    }

    /**
     * ???????????????????????????
     */
    final int[] POSITION_CommodityToRemove = {BaseSQLiteBO.INVALID_INT_ID};

    public void showCommodity(final List<Commodity> list) {
        scan_barcode_text.setText("");
        scan_barcode_text.setHint("??????????????????Ctrl+Q???");
        //??????????????????????????????UI??????
        int iCommodityQuantity = 0;
        for (int i = 0; i < list.size(); i++) {
            iCommodityQuantity += list.get(i).getCommodityQuantity();
        }
        commodityQuantity.setText("???????????????" + iCommodityQuantity + " ???");

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNumber(i + 1);
        }
        rvCommodity.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvCommodity.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        commodityRecyclerViewAdapter = new CommodityRecyclerViewAdapter1(list, getActivity().getApplicationContext());

        //????????????RecyclerView???item???????????????????????????????????????????????????????????????
        RecyclerView.ItemDecoration itemDecoration = rvCommodity.getItemDecorationAt(0);
        rvCommodity.removeItemDecoration(itemDecoration);
        rvCommodity.setAdapter(commodityRecyclerViewAdapter);
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
        if (BaseActivity.retailTrade != null) {
            total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
        } else {
            total_money.setText("0.00");
        }

        commodityRecyclerViewAdapter.setOnItemListener(new CommodityRecyclerViewAdapter1.OnItemListener() {
            @Override
            public void onClick(CommodityRecyclerViewAdapter1.MyViewHolder holder, final int position) {
                commodityRecyclerViewAdapter.setDefSelect(position);
                commodityRecyclerViewAdapter.notifyDataSetChanged();
                if (list.get(position).isSelect) {
                    //???????????????????????????????????????
//                    list.get(position).isSelect = false;
//                    POSITION_CommodityToRemove[0] = BaseSQLiteBO.INVALID_INT_ID;
                    showInputNumberDialog(position);
                    commodityRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    //???????????????????????????????????????
                    list.get(position).isSelect = true;
                    commodityRecyclerViewAdapter.notifyDataSetChanged();
                    POSITION_CommodityToRemove[0] = position;
                    log.info("???????????????" + list.get(position).toString());
                    showInputNumberDialog(position);
                }

            }

            private void showInputNumberDialog(final int position) {
                CustomDialog dialog = new CustomDialog(getActivity(), R.layout.input_number_dialog, false, 0.35, 0.65) {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void do_sure(String s) {
                        // ??????????????????????????????
                        try {
                            Integer.valueOf(reserve_et.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
                            reserve_et.setText(Default_CommodityMinNumber);
                        }
                        list.get(position).setCommodityQuantity(Integer.valueOf(reserve_et.getText().toString()));
                        if (tempCalculatedRetailTrade != null) {
                            BaseActivity.retailTrade = (RetailTrade) tempCalculatedRetailTrade.clone();
                            tempCalculatedRetailTrade = null;
                        }
                        if ("".equals(reserve_et.getText().toString()) || Integer.valueOf(reserve_et.getText().toString()) <= 0) { // ??????????????????1??????????????????
                            Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
                            reserve_et.setText(Default_CommodityMinNumber);
                        }
                        if (Integer.valueOf(reserve_et.getText().toString()) > Default_CommodityMaxNumber) {
                            Toast.makeText(getActivity(), "??????????????????????????????" + Default_CommodityMaxNumber, Toast.LENGTH_SHORT).show();
                            reserve_et.setText(String.valueOf(Default_CommodityMaxNumber));
                        }
                        if (!"".equals(reserve_et.getText().toString()) && Integer.valueOf(reserve_et.getText().toString()) > 0) {
                            //?????????????????????????????????????????????????????????recyclerview,???????????????????????????????????????
                            list.get(position).setCommodityQuantity(Integer.valueOf(reserve_et.getText().toString()));
                            list.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
                            // ??????????????????????????????
                            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                            if(avaliablePromotionList == null) {
                                Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            retailTradePromoting = new RetailTradePromoting();
                            BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
                            if (BaseActivity.retailTrade != null) {
                                total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
                            } else {
                                total_money.setText("0.00");
                            }
                            //????????????
                            activity.isShowToCustomer(getActivity().getApplicationContext());
                            //T1??????
                            showInT1Host();
                            // ...?????????
                            //
                            commodityRecyclerViewAdapter.notifyDataSetChanged();
                            //??????????????????????????????UI??????
                            int iCommodityQuantity = 0;
                            for (int i = 0; i < list.size(); i++) {
                                iCommodityQuantity += list.get(i).getCommodityQuantity();
                            }
                            commodityQuantity.setText("???????????????" + iCommodityQuantity + "???");
                            dismiss();
                        } else {
                            Toast.makeText(getActivity(), "??????????????????0???????????????", Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }
                };
                dialog.show();
            }
        });
        mainCommodityRecyclerViewItemLongClick(commodityRecyclerViewAdapter, list);
        rvCommodity.scrollToPosition(list.size() - 1);
    }

    private void showInT1Host() {
        if (BaseActivity.showCommList.size() == 0) {
            activity.showTextInT1Host("", "????????????");
        } else {
            double totalMoney = 0;
            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
                totalMoney += Double.valueOf(BaseActivity.showCommList.get(i).getSubtotal()); // TODO
            }
            activity.showTextInT1Host("?????????", "???" + GeneralUtil.formatToShow(totalMoney));
        }
    }

    private void mainCommodityRecyclerViewItemLongClick(final CommodityRecyclerViewAdapter1 commodityRecyclerViewAdapter, final List<Commodity> list) {
        commodityRecyclerViewAdapter.setOnItemLongListener(new CommodityRecyclerViewAdapter1.OnItemLongListener() {
            @Override
            public void onLongClick(CommodityRecyclerViewAdapter1.MyViewHolder holder, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = View.inflate(getActivity().getApplicationContext(), R.layout.reoport_loss_or_collection_dialog, null);
                builder.setView(view);
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.out.println("showCommList:" + BaseActivity.showCommList);
                        BaseActivity.showCommList.get(position).isSelect = true;
                        commodityRecyclerViewAdapter.setDefSelect(position);
                        commodityRecyclerViewAdapter.notifyDataSetChanged();
                        POSITION_CommodityToRemove[0] = position;
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                //??????dialog??????
                WindowManager manager = getActivity().getWindowManager();
                Display display = manager.getDefaultDisplay();
                dialog.getWindow().setLayout((int) (display.getWidth() * 0.57), (int) (display.getHeight() * 0.2));
                TextView remove_commodity = view.findViewById(R.id.remove_commodity);
                commodityRecyclerViewAdapter.setDefSelect(position);
                commodityRecyclerViewAdapter.notifyDataSetChanged();
                if (list.get(position).isSelect == false) {
                    //???????????????????????????????????????
                    list.get(position).isSelect = true;
                    commodityRecyclerViewAdapter.notifyDataSetChanged();
                    POSITION_CommodityToRemove[0] = position;
                }
                remove_commodity.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(View view) {
                        if (POSITION_CommodityToRemove[0] != BaseSQLiteBO.INVALID_INT_ID) {
                            commodityRecyclerViewAdapter.removeItem(POSITION_CommodityToRemove[0]);
                            // ??????????????????????????????
                            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                            if(avaliablePromotionList == null) {
                                Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            retailTradePromoting = new RetailTradePromoting();
                            BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
                            if (BaseActivity.retailTrade != null) {
                                total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
                            } else {
                                total_money.setText("0.00");
                            }
                            final CommodityRecyclerViewAdapter1 mAdapter = new CommodityRecyclerViewAdapter1(list, getActivity().getApplicationContext());
                            rvCommodity.setAdapter(mAdapter);
//                            if (list.size() != POSITION_CommodityToRemove[0]) {
//                                list.get(POSITION_CommodityToRemove[0]).isSelect = true;
//                            }
                            mAdapter.notifyDataSetChanged();
                            //???remove????????????????????????????????????remove????????????????????????????????????????????????????????????????????????????????????
                            mAdapter.setOnItemListener(new CommodityRecyclerViewAdapter1.OnItemListener() {
                                @Override
                                public void onClick(CommodityRecyclerViewAdapter1.MyViewHolder holder, final int position) {
                                    mAdapter.setDefSelect(position);
                                    mAdapter.notifyDataSetChanged();
                                    if (list.get(position).isSelect) {
                                        //???????????????????????????????????????
                                        list.get(position).isSelect = false;
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        //???????????????????????????????????????
                                        list.get(position).isSelect = true;
                                        mAdapter.notifyDataSetChanged();
                                        POSITION_CommodityToRemove[0] = position;
//                                Toast.makeText(MainActivity.this, "brand:" + list.get(position).getBrandID(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            mainCommodityRecyclerViewItemLongClick(mAdapter, list);
                            //??????????????????????????????UI??????
                            int iCommodityQuantity = 0;
                            for (int i = 0; i < list.size(); i++) {
                                iCommodityQuantity += list.get(i).getCommodityQuantity();
                            }
                            commodityQuantity.setText("???????????????" + iCommodityQuantity + " ???");
                            POSITION_CommodityToRemove[0] = -1;
                        } else {
                            Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
                        }
                        // ??????????????????????????????
                        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                        if(avaliablePromotionList == null) {
                            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        retailTradePromoting = new RetailTradePromoting();
                        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
                        if (BaseActivity.retailTrade != null) {
                            total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
                        } else {
                            total_money.setText("0.00");
                        }
                        //????????????
                        activity.isShowToCustomer(getActivity().getApplicationContext());
                        //T1??????
                        showInT1Host();
                        // ...????????????
                        showCommodity(BaseActivity.showCommList);
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    /**
     * ????????????
     */
    private void payViaCash(Double payAmount) {
        isPayingViaCash = true;
        handlerMessage.setErrorCode(EC_NoError);// ???????????????ErrorCode
        //
        paymentCode = paymentCode | cashPayCode;
        BaseActivity.retailTrade.setPaymentType(paymentCode);
        cashAmount = payAmount;
        totalCashAmount += cashAmount;
//        cashAmount = GeneralUtil.sum(Double.valueOf(etCashPayingAmount.getText().toString()), payAmount);
        BaseActivity.retailTrade.setAmountCash(cashAmount); // ????????????????????????????????????????????????????????????????????????
        //????????????????????????????????????????????????
        Double returnMoney = GeneralUtil.sub(Double.valueOf(getPayingMoney().getText().toString()), Double.valueOf(unpaidBalance.getText().toString()));
        if (returnMoney > BaseModel.TOLERANCE) {
            handlerMessage.setMsg("????????????,?????????????????????" + GeneralUtil.formatToShow(returnMoney));
            keyBoard.cleanNumber();
        } else {
            handlerMessage.setMsg("????????????");
            keyBoard.cleanNumber();
        }
        if (totalCashAmount + totalWechatAmount >= BaseActivity.retailTrade.getAmount()) {
            if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //???????????????????????????????????????
                if (BaseActivity.retailTrade.getAmount() > RetailTrade.TOLERANCE) {
                    if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                        BaseActivity.retailTrade.setPaymentType(5);
                    } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseActivity.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                        BaseActivity.retailTrade.setPaymentType(4);
                    } else if (totalCashAmount - BaseActivity.retailTrade.getAmount() >= 0) {
                        BaseActivity.retailTrade.setPaymentType(1);
                    } else {
                        throw new RuntimeException("?????????????????????");
                    }
                }

                //???????????????????????????
                int paymentType = BaseActivity.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseActivity.lastRetailTradePaymentType = pay_cash;
                } else if (paymentType == 4) {
                    BaseActivity.lastRetailTradePaymentType = pay_wetchat;
                } else {
                    BaseActivity.lastRetailTradePaymentType = pay_combination;
                }
                //???????????????????????????
                BaseActivity.lastRetailTradeChangeMoney = sub(totalCashAmount + totalWechatAmount, BaseActivity.retailTrade.getAmount());
//                BaseActivity.lastRetailTradeChangeMoney = sub(GeneralUtil.round(getPayingMoney().getText().toString(), 6), GeneralUtil.round(unpaidBalance.getText().toString(), 6));
                //????????????????????????
                BaseActivity.lastRetailTradeAmount = BaseActivity.retailTrade.getAmount();
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????BaseActivity.lastRetailTradeChangeMoney?????????0
//                BaseActivity.retailTrade.setAmountCash(totalCashAmount);
                BaseActivity.retailTrade.setAmountWeChat(totalWechatAmount);
                BaseActivity.retailTrade.setAmountCash(sub(totalCashAmount, BaseActivity.lastRetailTradeChangeMoney));
                log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + BaseActivity.retailTrade.getAmountCash());
                // ????????????????????????
                if (couponCode != null) {
                    calculateReturnPrice(BaseActivity.retailTrade, BaseActivity.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseActivity.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseActivity.retailTrade);
                }
                appendRetailTradePromoting(BaseActivity.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) { //TODO
                }
                //  ???????????????????????????5?????????????????????????????????????????????????????????????????????????????????????????????????????????
//                if (vip != null) {
//                    if (!uploadRetailTrade(handlerMessage)) { //TODO
//                    }
//                }
                updateRetailTradeAggregation();
            }
        }
        Message message = new Message();
        message.what = 4;
        message.obj = handlerMessage;
        handler.sendMessage(message);
    }

    private void calculateReturnPrice(RetailTrade rt, double fBestAmount) {
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
        double originalTotalAmount = 0.000000d;
        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            originalTotalAmount += rtc.getPriceReturn() * rtc.getNO(); //????????????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????
        }

        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            // ?????????????????????????????????????????????????????????????????????
            rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), fBestAmount), GeneralUtil.mul(originalTotalAmount, rtc.getNO()), 6))));
        }
    }

    /**
     * String?????????double??????????????????
     * ????????????????????????d1<d2
     * ??????????????????d1>d2
     * ??????0????????????
     *
     * @param s1
     * @param s2
     * @return
     */
    private int compareString(Object s1, Object s2) {
        BigDecimal d1 = new BigDecimal(Double.valueOf(s1.toString()));
        BigDecimal d2 = new BigDecimal(Double.valueOf(s2.toString()));
        return d1.compareTo(d2);
    }

    /**
     * ?????????????????????????????????????????????
     */
    private boolean createRetailTradeLocally(BaseFragment1.HandlerMessage hm) {
        if (vip != null) {
            BaseActivity.retailTrade.setVipID(vip.getID());
        }
        // ??????shopID
        Pos posR1Condition = new Pos();
        posR1Condition.setID(Constants.posID);
        Pos pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posR1Condition);
        if(pos == null) {
            hm.setErrorCode(posPresenter.getLastErrorCode());
            hm.setMsg(posPresenter.getLastErrorMessage());
            return false;
        }
        BaseActivity.retailTrade.setShopID((int)pos.getShopID());

        log.info("???????????????????????????" + BaseActivity.retailTrade);
        //??????????????????????????????????????????(??????????????????RetailTrade??????????????????TradeID????????????retailTradePromoting???TradeID???????????????????????????retailTradePromoting??????????????????ID?????????RetailTrade???int2???vip?????????)????????????????????????c?????????????????????????????????????????????????????????
        RetailTrade rtCreate = (RetailTrade) retailTradeSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, BaseActivity.retailTrade);
        if (rtCreate == null) {
            log.error("???????????????????????????????????????");
            hm.setErrorCode(EC_OtherError);
            hm.setSubErrorCode(SubErrorCode_RetailTrade_OtherError);
            return false;
        }

        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradeSQLiteEvent.getLastErrorCode() != null) {
            log.error("???????????????????????????????????????" + retailTradeSQLiteEvent.getLastErrorMessage());
            hm.setErrorCode(retailTradeSQLiteEvent.getLastErrorCode());
            hm.setMsg(retailTradeSQLiteEvent.getLastErrorMessage());
            return false;
        }
        // ????????????????????????????????????
        if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
            RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting);
            if (rtpCreate == null) {
                log.error("??????????????????????????????????????????");
                hm.setErrorCode(EC_OtherError);
                hm.setSubErrorCode(SubErrorCode_RetailTradePromoting_OtherError);
                return false;
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradePromotingSQLiteEvent.getLastErrorCode() != null) {
                log.error("??????????????????????????????????????????" + retailTradePromotingSQLiteEvent.getLastErrorMessage());
                hm.setErrorCode(retailTradePromotingSQLiteEvent.getLastErrorCode());
                hm.setMsg(retailTradePromotingSQLiteEvent.getLastErrorMessage());
                return false;
            }
        }


        //?????????????????????????????????????????????
        if (BaseActivity.retailTrade.getListSlave3() != null) {
            List<BaseModel> retailTradeCouponList = (List<BaseModel>) BaseActivity.retailTrade.getListSlave3();
            RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradeCoupon_CreateSync, retailTradeCouponList.get(0));
            if (retailTradeCouponCreate == null) {
                log.error("?????????????????????????????????????????????????????????");
                hm.setErrorCode(EC_OtherError);
                hm.setSubErrorCode(SubErrorCode_RetailTradeCoupon_OtherError);
                hm.setMsg("????????????????????????????????????????????????"); //
                return false;
            }
        }
        // ?????????????????????????????????????????????????????????????????????????????????
        BaseActivity.lastRetailTrade = BaseActivity.retailTrade;
        return true;
    }

    private void updateRetailTradeAggregation() {
        log.info("??????????????????????????????,???????????????????????????????????????");

        // ??????????????????????????????:
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        //????????????????????????
        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        //????????????????????????
        retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTrade.getAmountCash()));/*????????????*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));/*????????????*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*?????????*/
        retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);/*????????????*/
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*?????????????????????????????????????????????????????????????????????*/
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("?????????????????????????????????");
        }
        long lTimeOut = TIME_OUT;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            log.error("Update?????????retailTradeAggregationSQLiteEvent???????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("Update?????????????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo());
        } else {
            // ???????????????????????????(??????????????????????????????)
            BaseActivity.retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTrade.getAmountCash()));
            BaseActivity.retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));
            BaseActivity.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTradeAggregation.getWechatAmount()));
            BaseActivity.retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);//??????????????????????????????BaseActivity.retailTradeAggregation???TradeNO+1
            BaseActivity.retailTradeAggregation.setWorkTimeEnd(date);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // ????????????????????????????????????????????????????????????????????????????????????post Event ???Activity?????????????????????????????????
//                    Intent mainActivityIntent = new Intent(PaymentActivity.this, MainActivity.class);
//                    mainActivityIntent.putExtra("isShow", 2);
//                    startActivity(mainActivityIntent);
//                    finish();
                    break;
                case 2:
                    closeLoadingDialog(loadingDailog);
                    unpaidBalance.setText(unpaidBalance.getText().toString());
                    getPayingMoney().setText(unpaidBalance.getText().toString());
                    changeMoney.setText(changeMoney.getText().toString());
                    Toast.makeText(getActivity().getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 3: // ??????????????????UI??????
                    Main1Activity.HandlerMessage mth = (Main1Activity.HandlerMessage) msg.obj;
                    switch (mth.getErrorCode()) {
                        case EC_NoError:
                            Toast.makeText(getActivity().getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                            //
//                            wechatPaymentMoney.setText(GeneralUtil.formatToShow(wetchatAmount)); // ????????????????????????
                            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //???????????????????????????????????????
                                pay.setText("????????????");
                                pay.setEnabled(false);
                                pay.setBackgroundResource(R.drawable.button_background_disable);
                                //????????????
                                try {
                                    printSmallSheet(BaseActivity.lastRetailTrade);
                                } catch (Exception e) {
                                    log.info("?????????????????????" + e.getMessage());
                                }
                                // TODO ?????????????????????
                                resetTradeAfterPay(1);
                                initializeVipData();
                            } else {
                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseActivity.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
                                getPayingMoney().setText(unpaidBalance.getText().toString());
                                //????????????????????????????????????????????????????????????????????????????????????
                                cashPay.setChecked(true);
                                wechatPay.setClickable(false);
                                spinner.setEnabled(false);
                            }
//                            } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) < 0) {
//                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(Double.valueOf(unpaidBalance.getText().toString()), Double.valueOf(etWechatPayingAmount.getText().toString()))));
//                                getPayingMoney().setText(unpaidBalance.getText().toString());
//                                etCashPayingAmount.setText(unpaidBalance.getText()); // ??????????????????????????????????????????????????????????????????????????????????????????????????????
//                            }
//                            changeRadioButtonToPayViaCashOnly();
                            break;
                        case EC_InvalidSession:
                            Toast.makeText(getActivity().getApplicationContext(), BaseHttpBO.ERROR_MSG_Network, Toast.LENGTH_SHORT).show();
                            changeRadioButtonToPayViaCashOnly();
                            break;
                        case EC_WechatServerError:
                            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????" + (mth.getMsg() == null ? "" : mth.getMsg()) + "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            break;
                        case EC_OtherError:
                            switch (mth.getSubErrorCode()) {
                                case SubErrorCode_RetailTrade_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradePromoting_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradeCoupon_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                        case EC_Timeout:
                            Toast.makeText(getActivity().getApplicationContext(), "??????????????????!", Toast.LENGTH_SHORT).show();
                            changeRadioButtonToPayViaCashOnly();
                            break;
                    }
                    bLastWxPayIsNotYetDone = false;
                    closeLoadingDialog(loadingDailog);
                    break;
                case 4: // ??????????????????UI??????
                    Main1Activity.HandlerMessage mthCash = (Main1Activity.HandlerMessage) msg.obj;
                    switch (mthCash.getErrorCode()) {
                        case EC_NoError:
                            Toast.makeText(getActivity().getApplicationContext(), mthCash.getMsg(), Toast.LENGTH_SHORT).show();
                            //
//                            cashPaymentMoney.setText(GeneralUtil.formatToShow(cashAmount));//?????????UI?????????????????????
                            if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //???????????????????????????????????????
                                pay.setText("????????????");
                                pay.setEnabled(false);
                                pay.setBackgroundResource(R.drawable.button_background_disable);
                                // ?????????????????????????????????????????????????????????????????????????????????
                                BaseActivity.lastRetailTrade = BaseActivity.retailTrade;
                                //????????????
                                try {
                                    printSmallSheet(BaseActivity.lastRetailTrade);
                                } catch (Exception e) {
                                    log.info("?????????????????????" + e.getMessage());
                                }
                                BaseActivity.retailTrade = null;
                                System.out.println("????????????????????????????????????");
                                // ???????????????????????????
                                // TODO ????????????
                                // ??????????????????????????????????????????????????????????????????????????????????????????
                                // ????????????????????????????????????????????????????????????????????????
                                // ??????????????????????????????????????????????????????????????????????????????BaseActivity.showCommodity
                                resetTradeAfterPay(1);
                                initializeVipData();
                            } else {
                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseActivity.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
//                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(Double.valueOf(unpaidBalance.getText().toString()), Double.valueOf(etCashPayingAmount.getText().toString()))));
                                getPayingMoney().setText(unpaidBalance.getText().toString());
                                spinner.setEnabled(false);
                            }
                            break;
                        case EC_OtherError:
                            switch (mthCash.getSubErrorCode()) {
                                case SubErrorCode_RetailTrade_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradePromoting_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradeCoupon_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                    }
                    isPayingViaCash = false;
                    closeLoadingDialog(loadingDailog);
                    break;
                case 5: // ????????????????????????
                    ErrorInfo errorInfo = new ErrorInfo();
                    do {
                        if (vipHttpEvent.getLastErrorCode() == EC_NoError) {
                            vip = (Vip) vipHttpEvent.getBaseModel1();
                            if (vip != null) {
                                // ...??????VIP??????
                                log.debug("?????????????????????:" + vip);
                                showClientPhone.setVisibility(View.GONE);
                                search_vip.setVisibility(View.GONE);
                                //
                                vipMobile.setText("??????????????????" + vip.getMobile());
                                vipBonus.setText("?????????" + vip.getBonus());
                                vipMobile.setVisibility(View.VISIBLE);
                                vipBonus.setVisibility(View.VISIBLE);
                                return_search_vip.setVisibility(View.VISIBLE);
                                // ?????????????????????????????????
                                CouponCode couponCode = new CouponCode();
                                couponCode.setVipID(vip.getID().intValue());
                                couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                                couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                                if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                                    errorInfo.setErrorMessage("??????????????????????????????????????????" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage());
                                    showToastMessage("??????????????????????????????");
                                    break;
                                }
                                showToastMessage("???????????????????????????????????????????????????...");
                            } else {
                                errorInfo.setErrorMessage("????????????????????????");
                                showToastMessage("????????????????????????");
                                break;
                            }
                        } else {
                            errorInfo.setErrorMessage("??????????????????");
                            showToastMessage("??????????????????");
                            break;
                        }
                    } while (false);
                    if (errorInfo.getErrorMessage() != null) {
                        closeLoadingDialog(loadingDailog);
                        Toast.makeText(getActivity().getApplicationContext(), errorInfo.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (couponCodeHttpEvent.getLastErrorCode() == EC_NoError) {
                        couponCodeList = (List<CouponCode>) couponCodeHttpEvent.getListMasterTable();
                        showToastMessage("??????????????????????????????");
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????????????????????????????????" + couponCodeHttpEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    closeLoadingDialog(loadingDailog);
                    break;
            }
        }
    };

    private void initializeVipData() {
        vip = null;
        couponCodeList.clear();
        showCouponCodeList.clear();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2, getCouponCodeTitleList());
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setEnabled(true);

        couponCode = null;
        coupon = null;
        beforeUsingCouponAmount = 0.0000000d;
        showClientPhone.setText("");
        vipMobile.setText("??????????????????");
        vipBonus.setText("?????????");
        //
        vipMobile.setVisibility(View.GONE);
        vipBonus.setVisibility(View.GONE);
        return_search_vip.setVisibility(View.GONE);
        //
        showClientPhone.setVisibility(View.VISIBLE);
        search_vip.setVisibility(View.VISIBLE);
    }


    /**
     * ??????????????????????????????????????????????????????UI
     */
    private void resetTradeAfterHoldBill() {
        BaseActivity.retailTrade = null;
        total_money.setText("0.00");
        BaseActivity.showCommList.clear();
        showCommodity(BaseActivity.showCommList);
        retailTradePromoting = new RetailTradePromoting();// ??????????????????
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param isPaid ??????????????????
     */
    private void resetTradeAfterPay(int isPaid) {
        payment.setVisibility(View.GONE);//??????????????????
        setAnimation(payment);
        viewMain1Activity.setVisibility(View.INVISIBLE);
        bPaying = false;
        //
        if (isPaid == 1) {
            BaseActivity.showCommList.clear();
            showCommodity(BaseActivity.showCommList);
            total_money_double = 0.000000d;
            commodityQuantity.setText("???????????????" + "0" + " ???");//?????????????????????UI
            retailTradePromoting = new RetailTradePromoting();// ??????????????????
            BaseActivity.retailTrade = null;
            total_money.setText("0.00");
            totalWechatAmount = 0.000000d;
            totalCashAmount = 0.000000d;
            etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));

            // ???????????????
            lastRetailTradeAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeAmount));
            lastRetailTradeChangeMoney.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeChangeMoney));
            lastRetailTradePaymentType.setText(BaseActivity.lastRetailTradePaymentType);
        } else {
            // ?????????Activity????????????????????????????????????????????????????????????????????????
            System.out.println("???????????????????????????");
        }
        HideVirtualKeyBoard();
    }

    /**
     * ??????????????????????????????????????????
     */
    private void resetPayment() {
        payment.setVisibility(View.GONE);//??????????????????
        setAnimation(payment);
        viewMain1Activity.setVisibility(View.INVISIBLE);
        bPaying = false;
        //
        total_money_double = 0.000000d;
        total_money.setText("0.00");
        totalWechatAmount = 0.000000d;
        totalCashAmount = 0.000000d;
        etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));
        HideVirtualKeyBoard();
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private TextView getPayingMoney() {
        if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
            return etWechatPayingAmount;
        } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) {
            return etCashPayingAmount;
        }
        return null;
    }

    private void changeRadioButtonToPayViaCashOnly() {
        log.info("??????RadioButton?????????,???????????????:" + bCanPayViaCashOnly);
        if (!bCanPayViaCashOnly) {
            cashPay.setChecked(true); // ???????????????????????????
//            wechatPay.setEnabled(false);// ???????????????????????????????????????
            wechatPay.setClickable(false);
            bCanPayViaCashOnly = true; // ??????????????????RadionButton
            tvWechatPaying.setVisibility(View.INVISIBLE);
            etWechatPayingAmount.setVisibility(View.INVISIBLE);
            tvCashPaying.setVisibility(View.VISIBLE);
            etCashPayingAmount.setVisibility(View.VISIBLE);
            wechatPay.setBackgroundResource(R.drawable.radiobutton_unselect);
            cashPay.setBackgroundResource(R.drawable.radiobutton_select);
        }
    }

    private void updateRetailTradePromoting(Coupon coupon, double payAmount, RetailTrade retailTrade) {
        try {
            long tmpRetailTradePromotingFlowIDInSQLite;
            if (retailTradePromoting.getListSlave1() == null) {  //getListSlave1???????????????????????????????????????????????????????????????????????????
                retailTradePromoting.setListSlave1(new ArrayList<BaseModel>());
            }
            if (retailTradePromoting == null || retailTradePromoting.getListSlave1().size() == 0) {
                long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                retailTradePromoting.setTradeID(retailTrade.getID().intValue());
                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());

                tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);

            } else {
                //?????????????????????????????????????????????????????????????????????
                tmpRetailTradePromotingFlowIDInSQLite = ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(retailTradePromoting.getListSlave1().size() - 1)).getID() + 1;
            }
            //
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("????????????????????????????????????:" + GeneralUtil.formatToCalculate(payAmount) + "??? \t")//
                    .append("??????????????????:???" + coupon.getTitle() + "???\t"); //
            if (coupon.getType() == Coupon.EnumCouponCardType.ECCT_CASH.getIndex()) {
                promotingFlow.append("???????????????" + coupon.getLeastAmount() + "??????" + coupon.getReduceAmount() + "\t")//
                        .append("????????????:" + retailTrade.getAmount());
            } else {
                promotingFlow.append("???????????????" + coupon.getLeastAmount() + "???????????????" + coupon.getDiscount() + "?????????") //
                        .append("??????????????????:" + retailTrade.getAmount());
            }
            //
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromoting.getID().intValue());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            retailTradePromotingFlow.setID(tmpRetailTradePromotingFlowIDInSQLite);
            ((List<RetailTradePromotingFlow>) retailTradePromoting.getListSlave1()).add(retailTradePromotingFlow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void appendRetailTradeCoupon(RetailTrade retailTrade) {
        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
        retailTradeCoupon.setRetailTradeID(retailTrade.getID().intValue());
        retailTradeCoupon.setID(retailTradeCouponPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
        retailTradeCoupon.setCouponCodeID(couponCode.getID().intValue());
        //
        List<BaseModel> list = new ArrayList<BaseModel>();
        list.add(retailTradeCoupon);
        //
        retailTrade.setListSlave3(list);
    }

    /**
     * ????????????
     */
    private void payViaWX(Double payAmount) {
        bLastWxPayIsNotYetDone = true;
        handlerMessage.setErrorCode(EC_NoError); // ???????????????ErrorCode
        //
        Message message = new Message();
        message.what = 3;
        // ????????????
        if (!wxPayment(payAmount, handlerMessage)) {
            message.obj = handlerMessage;
            handler.sendMessage(message);
            return; // ?????????????????????????????????????????????????????????????????????????????????
        }
        log.info("==unpaidBalance.toString():" + unpaidBalance.toString() + "=====wetchatAmount:" + wetchatAmount);
        log.info("?????????????????????");
        totalWechatAmount += payAmount;
        keyBoard.cleanNumber();

        if (totalCashAmount + totalWechatAmount >= BaseActivity.retailTrade.getAmount()) {
            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //???????????????????????????????????????
                if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                    BaseActivity.retailTrade.setPaymentType(5);
                } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseActivity.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                    BaseActivity.retailTrade.setPaymentType(4);
                } else if (totalCashAmount - BaseActivity.retailTrade.getAmount() >= 0) {
                    BaseActivity.retailTrade.setPaymentType(1);
                } else {
                    throw new RuntimeException("?????????????????????");
                }
                //??????????????????????????????
                int paymentType = BaseActivity.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseActivity.lastRetailTradePaymentType = pay_cash;
                } else if (paymentType == 4) {
                    BaseActivity.lastRetailTradePaymentType = pay_wetchat;
                } else {
                    BaseActivity.lastRetailTradePaymentType = pay_combination;
                }
                //????????????????????????
                BaseActivity.lastRetailTradeAmount = BaseActivity.retailTrade.getAmount();
                BaseActivity.retailTrade.setAmountCash(totalCashAmount);
                BaseActivity.retailTrade.setAmountWeChat(totalWechatAmount);
                log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + BaseActivity.retailTrade.getAmountCash());
                // ????????????????????????
                if (couponCode != null) {
                    calculateReturnPrice(BaseActivity.retailTrade, BaseActivity.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseActivity.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseActivity.retailTrade);
                }
                appendRetailTradePromoting(BaseActivity.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) {

                }
                // ???????????????????????????5?????????????????????????????????????????????????????????????????????????????????????????????????????????
//                if (vip != null) {
//                    if (!uploadRetailTrade(handlerMessage)) { //TODO
//                    }
//                }
                updateRetailTradeAggregation();

                BaseActivity.retailTrade = null;
                System.out.println("????????????????????????????????????");
            }
        }
        message.obj = handlerMessage;
        handler.sendMessage(message);
    }

    private void appendRetailTradePromoting(RetailTrade retailTrade) {
        if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
            List<BaseModel> retailTradePromotingList = new ArrayList<BaseModel>();
            retailTradePromotingList.add(retailTradePromoting);
            retailTrade.setListSlave2(retailTradePromotingList);
        }
    }

    // ???????????????????????????5?????????????????????????????????????????????????????????????????????????????????????????????????????????
//    private boolean uploadRetailTrade(BaseFragment1.HandlerMessage hm) {
//        hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        hm.setSubErrorCode(SubErrorCode_RetailTrade_VipUpload_OtherError);
//        do {
//            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//            retailTradeSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(BaseActivity.retailTrade);
//
//            if (!retailTradeHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTrade)) {
//                log.error("?????????????????????????????????.????????????" + BaseActivity.retailTrade);
//                if (retailTradeHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
//                    log.info("SessionID=null????????????????????????");
//                    return true;
//                }
//                return false;
//            }
//            //??????????????????
//            long lTimeout = TIME_OUT;
//            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (lTimeout == 0 && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                log.error("?????????????????????????????????");
//                return false;
//            }
//            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                log.error("?????????????????????????????????.???????????????" + retailTradeSQLiteBO.getSqLiteEvent().getLastErrorMessage());
//                return false;
//            }
//            hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//            hm.setSubErrorCode(ErrorInfo.EnumErrorCode.EC_NoError.getIndex());
//        } while (false);
//        return true;
//    }

    private boolean wxPayment(Double payAmount, BaseFragment1.HandlerMessage hm) {
        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(payAmount);
        System.out.println("-------------------------------------------------??????????????????,???????????????" + payAmount);
        WXPayInfo wxPayInfo = new WXPayInfo();
        wxPayInfo.setAuth_code(wxPayAuthCode);
        wxPayInfo.setTotal_fee(dPaymentMoney);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????session?????????null?????????????????????????????????????????????
        if (!NetworkUtils.isNetworkAvalible(getActivity())) {
            wxPayHttpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_InvalidSession);
            hm.setErrorCode(EC_InvalidSession);
            return false;
        }

        //
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!wxPayHttpBO.microPayAsync(wxPayInfo)) {
            switch (wxPayHttpEvent.getLastErrorCode()) {
                case EC_InvalidSession:
                    hm.setErrorCode(EC_InvalidSession);
                    break;
                case EC_OtherError:
                    hm.setErrorCode(EC_WechatServerError);
                    break;
            }
            return false;
        }
        long lTimeOut = WxPay_TIME_OUT;
        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            log.info("??????????????????....");
            hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
            return false;
        }
        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) { // ???????????????????????????session?????????null?????????????????????????????????????????????
            log.info("??????????????????....");
            hm.setErrorCode(EC_WechatServerError);
            hm.setMsg(wxPayHttpEvent.getLastErrorMessage());
            return false;
        }
        paymentCode = paymentCode | wechatPayCode;
        BaseActivity.retailTrade.setPaymentType(paymentCode);
        //
        // TODO ??????etWechatPayingAmount???????????????????????????
        wetchatAmount = Double.valueOf(etWechatPayingAmount.getText().toString());
//        wetchatAmount = GeneralUtil.sum(Double.valueOf(etWechatPayingAmount.getText().toString()), payAmount);
        BaseActivity.retailTrade.setAmountWeChat(wetchatAmount);
        //
        microPayResponse = wxPayHttpEvent.getMicroPayResponse();
        BaseActivity.retailTrade.setWxOrderSN(microPayResponse.get("transaction_id") == null ? "" : microPayResponse.get("transaction_id"));
        BaseActivity.retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no") == null ? "" : microPayResponse.get("out_trade_no"));
        BaseActivity.retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? "" : microPayResponse.get("sub_mch_id"));
        BaseActivity.retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc") == null ? "" : microPayResponse.get("refund_desc"));

        return true;
    }

    /**
     * ?????????????????????
     */
    private void initWidget() {
        totalpay_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
        unpaidBalance.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
        etWechatPayingAmount.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
//        etWechatPayingAmount.setSelection(etWechatPayingAmount.getText().length());
        etCashPayingAmount.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
//        etCashPayingAmount.setSelection(etCashPayingAmount.getText().length());
    }

    private void balance() {
        if (BaseActivity.showCommList != null && BaseActivity.showCommList.size() > 0) {
            //???RetailTrade??????????????????????????????
            long maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            RetailTrade retailTradeForPaymentActivity = BaseActivity.retailTrade;
            retailTradeForPaymentActivity.setID(maxRetailTradeIDInSQLite);
            retailTradeForPaymentActivity.setVipID(vip != null ? vip.getID() : 0);
            retailTradeForPaymentActivity.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailTradeForPaymentActivity.setSmallSheetID(1);//...
            retailTradeForPaymentActivity.setStatus(RetailTrade.EnumStatusRetailTrade.ESRT_Normal.getIndex());
            retailTradeForPaymentActivity.setLogo("");
            retailTradeForPaymentActivity.setSourceID(-1);
            retailTradeForPaymentActivity.setPaymentAccount("");
            retailTradeForPaymentActivity.setDatetimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            retailTradeForPaymentActivity.setDatetimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            retailTradeForPaymentActivity.setSaleDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            retailTradeForPaymentActivity.setPos_ID(Constants.posID);
            retailTradeForPaymentActivity.setSn(retailTradeForPaymentActivity.generateRetailTradeSN(Constants.posID));
            retailTradeForPaymentActivity.setLocalSN((int) retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
            retailTradeForPaymentActivity.setRemark("......");
            //
            long maxRetailtradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeForPaymentActivity.getListSlave1();
            List<RetailTradeCommodity> newRetailTradeCommodityList = new ArrayList<>();
            if (retailTradeCommodityList != null) {
                for (int i = 0; i < retailTradeCommodityList.size(); i++) {
                    RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                    //
                    for (Commodity commodity : BaseActivity.showCommList) {
                        if (commodity.getID() == retailTradeCommodityList.get(i).getCommodityID()) {
                            retailTradeCommodity.setID(maxRetailtradeCommodityIDInSQLite + i);
                            retailTradeCommodity.setTradeID(retailTradeForPaymentActivity.getID());
                            retailTradeCommodity.setCommodityID(commodity.getID().intValue());
                            retailTradeCommodity.setCommodityName(commodity.getName());
                            retailTradeCommodity.setNO(commodity.getCommodityQuantity());
                            retailTradeCommodity.setPriceOriginal(commodity.getPriceRetail());
                            retailTradeCommodity.setTradeID(retailTradeForPaymentActivity.getID());
                            retailTradeCommodity.setNOCanReturn(retailTradeCommodity.getNO());
                            retailTradeCommodity.setPriceReturn(retailTradeCommodityList.get(i).getPriceReturn());
//                        retailTradeCommodity.setDiscount(commodity.getDiscount());
                            //
                            Barcodes barcodes = new Barcodes();
                            barcodes.setSql("where F_CommodityID = ?");
                            barcodes.setConditions(new String[]{String.valueOf(commodity.getID())});
                            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
                            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);
                            retailTradeCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());//...?????????????????????????????????
                            //
                            newRetailTradeCommodityList.add(retailTradeCommodity);
                            break;
                        }
                    }
                }
            }
            retailTradeForPaymentActivity.setListSlave1(newRetailTradeCommodityList);

            if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
                if (retailTradePromoting.getListSlave1().size() > 0) {
                    //???RetailTradePromoting??????ID???tradeID???RetailTradePromotingFlow??????ID
                    long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                    long tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                    //
                    retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                    retailTradePromoting.setTradeID(retailTradeForPaymentActivity.getID().intValue());
                    for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
                        ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tmpRetailTradePromotingFlowIDInSQLite + i);
                    }
                }
            }
            BaseActivity.retailTrade = retailTradeForPaymentActivity;
            beforeUsingCouponAmount = retailTradeForPaymentActivity.getAmount();

            setSpinner();
            spinner.setEnabled(true);
            payment.setVisibility(View.VISIBLE);//??????????????????
            totalpay_money.setText(String.valueOf(BaseActivity.retailTrade.getAmount()));
            total_money.setText(String.valueOf(BaseActivity.retailTrade.getAmount()));
            setAnimation(payment);
            initWidget();
            //
            viewMain1Activity.setVisibility(View.VISIBLE);
            bPaying = true;
            //
            //?????????????????? ??????????????????????????????????????????????????????????????????????????????
            if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
                if (Configuration.bUseSandBox) {
                    pay.setBackgroundResource(R.drawable.button_bg_gray);
                    pay.setText("??? ??? ???");
                    pay.setEnabled(false);
                } else {
                    // pay.setBackgroundResource(R.drawable.button_background_disable);
                    pay.setText("????????????");//... ?????????????????????????????????????????????????????????????????????
                    pay.setEnabled(false);
                }
            }
            // ????????????????????????
            choosePaymentType.setOnCheckedChangeListener(new CustomRaidoGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View group, @IdRes int checkedId) {
                    System.out.println("-----------??????");
                    if (wechatPay.getId() == checkedId) {
                        wechatPay.setBackgroundResource(R.drawable.radiobutton_select);
                        cashPay.setBackgroundResource(R.drawable.radiobutton_unselect);
                        keyBoard.cleanNumber();
                        changeMoney.setText(DEFAULT_changeAmount);
                        etWechatPayingAmount.requestFocus();
                        tvWechatPaying.setVisibility(View.VISIBLE);
                        etWechatPayingAmount.setVisibility(View.VISIBLE);
                        wechat_paying_prefix.setVisibility(View.VISIBLE);
                        tvCashPaying.setVisibility(View.INVISIBLE);
                        cash_paying_prefix.setVisibility(View.INVISIBLE);
                        etCashPayingAmount.setVisibility(View.INVISIBLE);
                        if (Configuration.bUseSandBox) {
                            pay.setBackgroundResource(R.drawable.button_bg_gray);
                            pay.setText("??? ??? ???");
                            pay.setEnabled(false);
                        } else {
                            pay.setBackgroundResource(R.drawable.button_background_disable);
                            pay.setText("????????????");
                            pay.setEnabled(false);
                        }
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
//                        if (!StringUtils.isEmpty(etWechatPayingAmount.getText().toString())) {
//                            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                                Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
//                                etWechatPayingAmount.setText(unpaidBalance.getText().toString());
//                            }
//                        }
                    } else if (cashPay.getId() == checkedId) {
                        cashPay.setBackgroundResource(R.drawable.radiobutton_select);
                        wechatPay.setBackgroundResource(R.drawable.radiobutton_unselect);
                        keyBoard.cleanNumber();
                        etCashPayingAmount.requestFocus();
                        tvWechatPaying.setVisibility(View.INVISIBLE);
                        etWechatPayingAmount.setVisibility(View.INVISIBLE);
                        wechat_paying_prefix.setVisibility(View.INVISIBLE);
                        tvCashPaying.setVisibility(View.VISIBLE);
                        cash_paying_prefix.setVisibility(View.VISIBLE);
                        etCashPayingAmount.setVisibility(View.VISIBLE);
                        // ????????????????????????????????????????????????
                        if (Double.valueOf(unpaidBalance.getText().toString()) - Double.valueOf(MAX_CashPaymentAmount) > RetailTrade.TOLERANCE) {
                            etCashPayingAmount.setText(MAX_CashPaymentAmount);
                            changeMoney.setText(DEFAULT_changeAmount);
                        } else {
                            etCashPayingAmount.setText(unpaidBalance.getText().toString());
                            changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(etCashPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()))));
//                        if (!StringUtils.isEmpty(etCashPayingAmount.getText().toString()) && compareString(etCashPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                            changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(etCashPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()))));
//                        }
                        }
                        pay.setText("???  ???");
                        pay.setBackgroundResource(R.drawable.button_bg_red);
                        pay.setEnabled(true);

                    }
                }
            });
            etWechatPayingAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etWechatPayingAmount.getText().toString().startsWith(".")) {
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString())) {
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????????????????????0", Toast.LENGTH_SHORT).show();
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else {
                        double change = sub(Double.valueOf(etWechatPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()));
                        change = change > 0.00d ? change : 0.00d;
                        changeMoney.setText(GeneralUtil.formatToShow(change));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etCashPayingAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etCashPayingAmount.getText().toString().startsWith(".")) {
                        Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                        etCashPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (StringUtils.isEmpty(etCashPayingAmount.getText().toString()) || Double.valueOf(etCashPayingAmount.getText().toString()) < 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????0", Toast.LENGTH_SHORT).show();
                        etCashPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (compareString(etCashPayingAmount.getText().toString(), MAX_CashPaymentAmount) > 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????" + MAX_CashPaymentAmount, Toast.LENGTH_SHORT).show();
                        etCashPayingAmount.setText(MAX_CashPaymentAmount);
                        keyBoard.cleanNumber();
                    } else {
                        double change = sub(Double.valueOf(etCashPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()));
                        change = change > 0.00d ? change : 0.00d;
                        changeMoney.setText(GeneralUtil.formatToShow(change));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * ????????????ReserveAmount?????????????????????????????????sqlite???
     */
    private void createRetailTradeAggregation() {
        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
        //??????????????????????????????day end ???????????? dialog?????????????????????dialog???????????????????????????work time start??????2????????????????????????work time start????????????????????????
        //?????????????????????????????????????????????????????????day end dialog??????????????????????????????retailTradeAggregation???SQLite??????????????????????????????????????????????????????retailTradeAggregation??????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
        BaseActivity.retailTradeAggregation.setWorkTimeStart(DatetimeUtil.addSecond(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), 1));
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
        //
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("???????????????????????????");
        }
    }

    /* ?????????????????? */
    private void initScanGun() {
        // ??????key???????????????????????????20ms?????????????????????????????????
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScanFinish(String scanResult) {
                if (payment.getVisibility() != View.GONE) { // ?????????????????????
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
                    // ???????????????????????????
                    if (!validateCouponCodeAvailable()) {
                        return;
                    }
                    if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
                        if (scanResult != null && !"".equals(scanResult) && scanResult.length() == LENGTH_WxPayAuthCode) { //?????????????????????????????????
                            wxPayAuthCode = scanResult;
                            if (wxPayAuthCode != null && !"".equals(wxPayAuthCode)) {
                                if (!bLastWxPayIsNotYetDone && BaseActivity.retailTrade != null) {
                                    if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || sub(Double.valueOf(etWechatPayingAmount.getText().toString()), MIN_WechatPaymentAmount) < BaseModel.TOLERANCE) {
                                        closeLoadingDialog(loadingDailog);
                                        Toast.makeText(getActivity().getApplicationContext(), "????????????????????????0", Toast.LENGTH_SHORT).show();
                                    } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
                                        closeLoadingDialog(loadingDailog);
                                        Toast.makeText(getActivity().getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                                    } else {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                payViaWX(Double.valueOf(getPayingMoney().getText().toString()));
                                            }
                                        }).start();
                                    }
                                }
                            }
                        } else {
                            closeLoadingDialog(loadingDailog);
                            Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        closeLoadingDialog(loadingDailog);
                        if (totalWechatAmount > BaseModel.TOLERANCE) {
                            Toast.makeText(getActivity().getApplicationContext(), "?????????????????????????????????1???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "???????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //????????????
                    log.info("??????????????????????????????" + scanResult);
                    if (StringUtils.isEmpty(scanResult) || scanResult.length() <= BaseActivity.FUZZY_QUERY_LENGTH) {
                        Toast.makeText(getActivity(), "????????????????????????????????????????????????" + String.valueOf(BaseActivity.FUZZY_QUERY_LENGTH + 1) + "???", Toast.LENGTH_SHORT).show();
                        log.info("????????????????????????????????????????????????" + String.valueOf(BaseActivity.FUZZY_QUERY_LENGTH + 1) + "???");
                        return;
                    }

                    if (scanResult.length() == Vip.Max_LENGTH_VipCardSN && scanResult.substring(0, 6).equals(Constants.MyCompanySN)) {
                        showClientPhone.setText(scanResult);
                    } else {
                        commodityListAfterQuery.clear();
                        scan_barcode_text.setText(scanResult);
                        commodityListAfterQuery = retrieveNCommodityInSQLite(true, scanResult);
                        log.info("??????????????????????????????" + commodityListAfterQuery.size() + "??????????????????????????????" + scanResult);
                        log.info("showCommList???" + BaseActivity.showCommList);
                        if (commodityListAfterQuery == null || commodityListAfterQuery.size() == 0) {
                            Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
                            scan_barcode_text.setText("");
                        } else {
                            //??????????????????????????????????????????????????????scanResult???????????????
                            if (commodityListAfterQuery.size() > 1 || !commodityListAfterQuery.get(0).getBarcode().equals(scanResult)) {
                                AlertDialog.Builder commodity_builder = null;
                                View commodity_view = null;
                                AlertDialog commodity_dialog = null;
                                objectList = createAlertDialog(commodity_builder, commodity_view, commodity_dialog, 900, 500, R.layout.choose_commodity_dialog);
                                WindowManager.LayoutParams params = ((Dialog) objectList.get(2)).getWindow().getAttributes();
                                params.width = (int) (screenWidth * 0.5);
                                params.height = (int) (screenHeight * 0.68);
                                ((Dialog) objectList.get(2)).getWindow().setAttributes(params);

                                commodity_view = (View) objectList.get(1);
                                rv_alertdialog_commodity = commodity_view.findViewById(R.id.rv_alertdialog_commodity);
                                rv_alertdialog_commodity.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                                tv_inventory_search = commodity_view.findViewById(R.id.tv_inventory_search);
//                            search_view = commodity_view.findViewById(R.id.search_view);
                                tv_inventory_search.setVisibility(View.GONE);
//                            search_view.setVisibility(View.GONE);
                                stock = commodity_view.findViewById(R.id.stock);
                                search_commodity_et = commodity_view.findViewById(R.id.search_commodity_et);
                                search_commodity_et.setText(scanResult);
                                search_commodity_et.setEnabled(false);

                                dialogCommodityRecyclerViewAdapter = new DialogCommodityRecyclerViewAdapter1(commodityListAfterQuery, getActivity().getApplicationContext());
                                rv_alertdialog_commodity.setAdapter(dialogCommodityRecyclerViewAdapter);
                                //
                                dialogCommodityRecyclerViewAdapter.setOnItemListener(new DialogCommodityRecyclerViewAdapter1.OnItemListener() {
                                    @Override
                                    public void onClick(DialogCommodityRecyclerViewAdapter1.MyViewHolder holder, int position) {
                                        dialogCommodityRecyclerViewAdapter.setDefSelect(position);
                                        dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                                        if (commodityListAfterQuery.get(position).isSelect) {
                                            //???????????????????????????????????????
                                            commodityListAfterQuery.get(position).isSelect = false;
                                            stock.setText("0");
                                            dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                                        } else {
                                            //???????????????????????????????????????
                                            commodityListAfterQuery.get(position).isSelect = true;
                                            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityListAfterQuery.get(position).getListSlave2().get(0);
                                            stock.setText(String.valueOf(commodityShopInfo.getNO()));
                                            dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                                TextView cancel_tv = commodity_view.findViewById(R.id.cancel_tv);
                                cancel_tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        log.debug("??????????????????");
                                        ((Dialog) objectList.get(2)).dismiss();
                                        commodityListAfterQuery.clear();
                                    }
                                });

                                TextView add_tv = commodity_view.findViewById(R.id.add_tv);
                                add_tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        log.debug("????????????");
                                        Commodity dialogCommodity = new Commodity();
                                        dialogCommodity.setID(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getID());
                                        dialogCommodity.setBarcode(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
                                        //
                                        isCommodityExistInUI(dialogCommodity);
                                        //
                                        showCommodity(BaseActivity.showCommList);
                                        ((Dialog) objectList.get(2)).dismiss();
                                    }
                                });
                            } else { //????????????????????????????????????????????????scanResult????????????
                                Commodity commodity = new Commodity();
                                commodity.setID(commodityListAfterQuery.get(0).getID());
                                commodity.setBarcode(commodityListAfterQuery.get(0).getBarcode());
                                //
                                isCommodityExistInUI(commodity);
                                //
                                showCommodity(BaseActivity.showCommList);
//                            ((Dialog) objectList.get(2)).dismiss();
                            }
                        }
                    }
                }
            }
        });

//        discount_sp.setEnabled(false); // TODO ??????????????????????????????????????????Spinner????????????(?????????????????????)
//        discount_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                discount_String = (String) discount_sp.getSelectedItem();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        // ?????????Activity????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
        if (BaseActivity.retailTrade != null) {
            total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
        } else {
            total_money.setText("0.00");
        }
        lastRetailTradeAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeAmount));
        lastRetailTradeChangeMoney.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeChangeMoney));
        lastRetailTradePaymentType.setText(BaseActivity.lastRetailTradePaymentType);
    }

    //???????????????0.1?????????????????????
    public class TimeThread extends Thread {
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(100);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }

    }

    private void changeRadioButtonToPayViaWX() {
        paymentCode = 0b00000000;
        pay.setText("??? ??? ???");
        pay.setBackgroundResource(R.drawable.button_bg_gray);
        pay.setEnabled(false);
//        wechatPay.setEnabled(true);// ????????????????????????????????????
        wechatPay.setClickable(true);
        wechatPay.setChecked(true); // ???????????????????????????
        changeMoney.setText(DEFAULT_changeAmount);
        etWechatPayingAmount.requestFocus();
        tvWechatPaying.setVisibility(View.VISIBLE);
        etWechatPayingAmount.setVisibility(View.VISIBLE);
        wechat_paying_prefix.setVisibility(View.VISIBLE);
        tvCashPaying.setVisibility(View.INVISIBLE);
        cash_paying_prefix.setVisibility(View.INVISIBLE);
        etCashPayingAmount.setVisibility(View.INVISIBLE);
        wechatPay.setBackgroundResource(R.drawable.radiobutton_select);
        cashPay.setBackgroundResource(R.drawable.radiobutton_unselect);
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

    public void onKeyDownChild(int keyCode, KeyEvent event) {
        log.info("---------------------------------onKeyDown:    KeyCode:" + keyCode + "------event:" + event + "------------------------------------");
        scanGun.isMaybeScanning(keyCode, event);
    }

    public boolean validateCouponCodeAvailable() {
        // ???????????????????????????
        if (couponCode != null) {
            if (!isCouponInDatetimeScope(coupon)) {
                for (int i = 0; i < couponCodeList.size(); i++) {
                    if (couponCodeList.get(i).getCouponID() == coupon.getID()) {
                        couponCodeList.remove(i);
                        break;
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2, getCouponCodeTitleList());
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(arrayAdapter);
                Toast.makeText(getActivity().getApplicationContext(), "?????????????????????,????????????????????????", Toast.LENGTH_SHORT).show();
                closeLoadingDialog(loadingDailog);
                return false;
            }
        }
        return true;
    }

    private List<BaseModel> queryAvaliablePromotion(){
        Pos posR1Condition = new Pos();
        posR1Condition.setID(Constants.posID);
        Pos pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posR1Condition);
        if(pos == null) {
            return null;
        }
        List<BaseModel> avaliablePromotionList = new ArrayList<>();
        List<Promotion> promotionList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        for(Promotion promotion : promotionList) {
            boolean exist = false; // ????????????????????????????????????????????????
            PromotionShopScope pShopScopeRnCondition = new PromotionShopScope();
            pShopScopeRnCondition.setSql("where F_PromotionID = ?");
            pShopScopeRnCondition.setConditions(new String[]{String.valueOf(promotion.getID())});
            pShopScopeRnCondition.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Int);
            List<PromotionShopScope> listPromotionShopScope  = (List<PromotionShopScope>) promotionShopScopePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_PromotionShopScope_RetrieveNByConditions, pShopScopeRnCondition);
            if (promotionShopScopePresenter.getLastErrorCode() != EC_NoError) {
                return null;
            }
            if(listPromotionShopScope != null && listPromotionShopScope.size() > 0) {
                for (PromotionShopScope promotionShopScope : listPromotionShopScope) {
                    if (promotionShopScope.getShopID() == pos.getShopID()) {
                        exist = true;
                    }
                }
            } else {
                exist = true; // ????????????????????????????????????
            }
            if(exist) {
                avaliablePromotionList.add(promotion);
            }
        }
        return avaliablePromotionList;
    }
}
