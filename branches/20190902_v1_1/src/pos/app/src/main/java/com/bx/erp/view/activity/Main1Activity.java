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
    FrameLayout payment;//支付页面
    @BindView(R.id.favorites_page)
    RelativeLayout favorites_page;//收藏夹页面
    @BindView(R.id.vip_page)
    RelativeLayout vip_page;//会员信息页面
    @BindView(R.id.favoritesAndvip_page)
    RelativeLayout favoritesAndvip_page;//会员信息以及收藏夹的背景页面
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
    TextView commodityQuantity;//主页显示商品条数
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
    @BindView(R.id.last_PaidInAmount)
    TextView lastRetailTradePaidInAmount;
    @BindView(R.id.last_paymenttype)
    TextView lastRetailTradePaymentType;
    @BindView(R.id.last_changemoney)
    TextView lastRetailTradeChangeMoney;
    /**
     * 当前微信支付金额的文本框
     */
    @BindView(R.id.wechat_paying_amount)
    TextView etWechatPayingAmount;
    /**
     * 当前现金支付金额的文本框
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
    private final static String DEFAULT_changeAmount = "0.00"; //默认付全款时，找零应为0.00

    // 售卖商品列表中数量的默认最小值
    private String Default_CommodityMinNumber = "1";
    // 售卖商品列表中数量的默认最大值
    private int Default_CommodityMaxNumber = 9999;
    //上一次点击搜索按钮时输入的有效长度的单据号
    private String lastQueryKeyWordBySearchButton = "";
    //上一次点击搜索按钮时输入的起始时间
    private Date lastDatetimeStartBySearchButton = getDefaultSyncDatetime();
    //上一次点击搜索按钮时输入的结束时间
    private Date lastDatetimeEndBySearchButton = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);

    private List<RetailTradeCommodity> retailTradeCommodityAfterConfirmReturn = new ArrayList<RetailTradeCommodity>();//查单界面查询到的零售单商品保存在该List
    List<RetailTrade> showRetailTradeList = new ArrayList<>();//用于显示查找出的零售单
    //用于记录退货时候选中的零售单，因为如果不这么做，点击之后修改了数量，一种情况是点击另一张单，回到原来的零售单，数量是改变之后的值，
    // 还有一种就是，只是修改显示的值，但是没有办法拿到。所以需用一个retailTrade用于记录选中的单，当选择另一张单的时候就进行初始化
    RetailTrade retailTradeAfterConfirmReturn;
    private RetailTrade retailTradeSelected; //用于记录选中的零售单。
    private RetailTradePromoting retailTradePromoting;
    private int commNumber = 0;//用于记录退货时选中的零售单商品的原数量
    private int noCanReturn = 0;//用于记录每个零售单商品的可退货数量
    private int remainingQuantity;//当退货数量为最大可退货数量的时候，剩余的数量
    private double dAmountToReturnToCustomer = 0.000000d;
    /**
     * 标识用户当前正在用现金支付，用户已经点击一次现金支付，不能再处理他后续的点击事件
     */
    private static boolean isPayingViaCash = false;
    /**
     * 标识上一次扫码微信支付还未完成。
     * 扫码时检查此标记，决定是否执行微信支付，防止用户疯狂扫码！
     */
    private boolean bLastWxPayIsNotYetDone = false;
    private boolean bCanPayViaCashOnly = false;
    private HandlerMessage handlerMessage = new HandlerMessage();
    /**
     * 子错误码：本地创建零售单失败
     */
    private final int SubErrorCode_RetailTrade_OtherError = 1;
    /**
     * 子错误码：本地创建零售单促销计算过程失败
     */
    private final int SubErrorCode_RetailTradePromoting_OtherError = 2;
    /**
     * 子错误码：本地创建零售单优惠券使用表失败
     */
    private final int SubErrorCode_RetailTradeCoupon_OtherError = 3;
    /**
     * 子错误码：会员立刻上传优惠券失败
     */
    private final int SubErrorCode_RetailTrade_VipUpload_OtherError = 4;
    private int paymentCode = 0b00000000;//用于表示未进行支付，在支付后，paymentType的值为paymentCode + 对应的支付方式的二进制数
    private final int wechatPayCode = 0b00000100;//用于表示相应支付方式的二进制数
    private final int alipayPayCode = 0b00000010;
    private final int cashPayCode = 0b00000001;
    private String wxPayAuthCode = ""; // 微信付款授权码
    // 登录开机同步后才输入准备金，创建收银汇总，在首页点击其它选项不能重复输入准备金
    private boolean firstTimeEnterMain1Activity = true;
    private double total_money_double = 0.000000d;//总计金额的double数据（用于上传到服务器，和保存到本地SQLite，不需要对小数位进行操作）
    private String wxOutRefundNO;//商户退款单号
    private String wxOutRefundErrorMsg;//商户退款失败信息
    public static boolean bPaying = false;
    public static final String MAX_CashPaymentAmount = "99999";
    private final static int LENGTH_WxPayAuthCode = 18;
    private final static double MIN_WechatPaymentAmount = 0.00; // 微信支付金额必须大于该值
    /**
     * 用于记录本次结算使用微信支付了多少
     */
    private double wetchatAmount;//
    /**
     * 用于记录本次结算使用支付宝支付了多少
     */
    private double alipayAmount;//
    /**
     * 用于记录本次结算使用现金支付了多少
     */
    private double cashAmount;//
    private Map<String, String> microPayResponse; // 微信支付结果

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
//     * 本地广播(本APP范围内)使用的组件
//     * */
//    private IntentFilter intentFilter;
//    private DayEndGenerateRetailTradeAggregationReceiver dayEndGenerateRetailTradeAggregationReceiver;
//    private LocalBroadcastManager localBroadcastManager;
    private AlertDialog cancelPayDialog;
    //查询Vip部分
    private static Vip vip = null;
    // 此Vip拥有的优惠券
    private static List<CouponCode> couponCodeList = new ArrayList<CouponCode>();
    // UI展示的优惠券列表
    private static List<CouponCode> showCouponCodeList = new ArrayList<CouponCode>();
    // 收银员当前选择的优惠券
    private static CouponCode couponCode = null;
    // 当前选择优惠券是属于哪一种优惠券
    private static Coupon coupon = null;
    // 使用优惠券前的零售单金额
    private static double beforeUsingCouponAmount = 0.0000000d;

    private final String COUPONTITLELIST_DEFAULT_UnavailableConpon = "无可用的优惠券";
    private final String COUPONTITLELIST_DEFAULT_AvailableConpon = "有可用的优惠券";

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
            vipMobile.setText("会员手机号：" + vip.getMobile());
            vipBonus.setText("积分：" + vip.getBonus());
            vipMobile.setVisibility(View.VISIBLE);
            vipBonus.setVisibility(View.VISIBLE);
            return_search_vip.setVisibility(View.VISIBLE);
            CouponCode couponCode = new CouponCode();
            couponCode.setVipID(vip.getID().intValue());
            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                showToastMessage("查找会员的优惠券失败");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main1, container, false);

        activity = (Base1FragmentActivity) getActivity();//初始化变量用来调用Base1FragmentActivity的方法；
        HideVirtualKeyBoard();
//        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());//获取localBroadcastManager实例
        unbinder = ButterKnife.bind(this, view);
        showClientPhone.setOnTouchListener(this);
        //当此EditText获得焦点的时候，软键盘不弹出
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
        screenWidth = dm.widthPixels;//屏幕宽度
        screenHeight = dm.heightPixels;//屏幕高度
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
                        reserve_et.setError("请输入整数或者保留小数点后两位的小数");
                    } else if ("".equals(s) || null == s) {
                        reserve_et.setError("准备金不允许为空");
                    } else if (Double.valueOf(s) > 10000d) {
                        reserve_et.setError("输入的准备金不能大于10000");
                    } else if (s.startsWith(".")) {
                        reserve_et.setError("错误的金额格式，请重新输入正确的准备金额");
                    } else {
                        BaseActivity.retailTradeAggregation.setReserveAmount(Double.valueOf(s));
                        createRetailTradeAggregation(); //TODO 将来这里要加一个转圈圈
                        dismiss();
                    }
                }
            };
            dialog.show();
            firstTimeEnterMain1Activity = false;
        }
        initializeVipData();
        // 取单后恢复会员信息
        onBackFromBillFragment();
        initScanGun();

        payment.setOnClickListener(new View.OnClickListener() {
            // 点击支付页面空白处后将键盘关闭
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        favoritesAndvip_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 无需做任何操作，仅用来拦截监听
            }
        });

        keyBoard.setOnNumberclickListener(new KeyBoard.OnNumberclickListener() {
            @Override
            public void onClick(String number) {
                getPayingMoney().setText(number);//将数字填入edittext
            }
        });
        // Fragment之前切换，重新刷新待购商品列表
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
            log.info("未处理的Event，ID=" + event.getId() + "syncThread");
        }
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
        } else {
            log.info("未处理的Event，ID=" + event.getId() + "syncThread");
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
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
                wxOutRefundErrorMsg = event.getLastErrorMessage(); // 微信返回的错误信息
                event.setWxPayStatus("");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponCodeHttpEvent(CouponCodeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainActivity) {
            event.onEvent();
            Message message = new Message();
            message.what = 6;
            handler.sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
                    couponCode = showCouponCodeList.get(i - 1); // i-1是因为i的位置上有"有可用优惠券"或"无可用优惠券".而showCouponCodeList中没有，要正确获取收银员选择的优惠券需要-1
                    System.out.println("选择了一张优惠券：" + couponCode);
                    Coupon queryCoupon = new Coupon();
                    queryCoupon.setID(Long.valueOf(couponCode.getCouponID()));
                    coupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
                    if (couponPresenter.getLastErrorCode() != EC_NoError) {
                        couponCode = null;
                        log.error("通过优惠券查找它是属于那种优惠券时失败！");
                        Toast.makeText(getActivity().getApplicationContext(), "暂时无法使用该券", Toast.LENGTH_SHORT).show();
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
     * TODO 可否移动到类外，将来对其进行独立测试？
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
                    log.error("通过优惠券查找它是属于那种优惠券时失败！");
                    continue;
                }
                if (queryCoupon != null) {
                    double amount = 0.000000d;
                    // 是否有作用范围，如果有,则需要判断范围内的商品购买总金额是否达到了起用范围
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
                    // 判断是否已到了起用金额
                    if (amount >= queryCoupon.getLeastAmount() && isCouponInDatetimeScope(queryCoupon)) {
                        showCouponCodeList.add(couponCode);
                        couponCodeTitleList.add(queryCoupon.getTitle());
                    }
                }
            }

            // vip所拥有的优惠券全都不满足此单消费时。将显示的优惠券列表改为"无可用优惠券"
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
            // 此优惠券是否已到起用日期
            if (DatetimeUtil.isInDatetimeRange(coupon.getBeginDateTime(), now, coupon.getEndDateTime())) {
                // 判断此优惠券是否在限制星期中，如限制星期一 9：00-11：00，那么星期一九点到11点是不可用的
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
//            log.debug("处理广播事件");
//            //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
//            //这里不判断也可以，因为发广播前判断过了
//            if (BaseActivity.retailTradeAggregation.getID() != null) {
//                if (intent.getAction().equals(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation)) {
//                    log.debug("处理广播事件：" + DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_ResponseBroadcastDayEndGenerateRetailTradeAggregation);
//                    createRetailTradeAggregationForNextDay();
//                }
//            }
//        }
//    }

//    /* *
//     * 当收银员跨天上班，到了第二天后，往sqlite插入一条收银汇总
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
//        //初始化 BaseActivity.retailTradeAggregation的部分数据
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
//                log.error("跨天上班时，创建零售单汇总失败");//TODO 未处理结果事件
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

    //点击事件
    @OnClick({R.id.favorites1, R.id.favorites2, R.id.favorites_close, R.id.balance_tv, R.id.list, R.id.payment_close, R.id.vip_close, R.id.pay, R.id.search_vip, R.id.return_search_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_vip:// 搜索按钮
                showClientPhone.clearFocus();//清除焦点，以免影响扫描商品条形码添加商品
                //获取搜索框中的数据然后进行查询
                String queryKeyword = showClientPhone.getText().toString();
                //初始化会员相关信息
                initializeVipData();
                if (queryKeyword.length() == FieldFormat.LENGTH_Mobile || queryKeyword.length() == Vip.Max_LENGTH_VipCardSN) {
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);

                    Vip vip = new Vip();
                    if (queryKeyword.length() == FieldFormat.LENGTH_Mobile) {
                        vip.setMobile(queryKeyword);
                    } else {
                        vip.setVipCardSN(queryKeyword);
                    }
                    if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible(getActivity().getApplication())) { // 联网查询
                        if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
                            showToastMessage("搜索会员失败，请重试！");
                            log.error("搜索会员失败：" + queryKeyword);
                        }
                    } else {   // 无网
                        showToastMessage(BaseHttpBO.ERROR_MSG_Network);
                        loadingDailog.dismiss();
                    }
                } else {
                    showToastMessage(Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);
                }

                break;
            case R.id.return_search_vip: // 返回查询会员按钮
                initializeVipData();
                break;
            case R.id.favorites1://收藏夹按钮
                favorites_page.setVisibility(View.VISIBLE);//显示收藏夹页面
                favorites2.setVisibility(View.GONE);//隐藏收藏夹按钮
                favoritesAndvip_page.setVisibility(View.VISIBLE);//收藏夹以及会员页面背景显示
                setAnimation(favorites_page);
                break;
            case R.id.favorites2://支付页面的收藏夹按钮
                favorites_page.setVisibility(View.VISIBLE);//显示收藏夹页面
                favorites2.setVisibility(View.GONE);//隐藏收藏夹按钮
                favoritesAndvip_page.setVisibility(View.VISIBLE);//收藏夹以及会员页面背景显示
                setAnimation(favorites_page);
                break;
            case R.id.favorites_close://收藏夹关闭按钮
                favorites_page.setVisibility(View.GONE);//收藏夹页面关闭
                favorites2.setVisibility(View.VISIBLE);//收藏夹按钮显示
                if (favorites_page.getVisibility() == View.GONE & vip_page.getVisibility() == View.GONE) {//判断是否都已关闭，是则背景也关闭
                    favoritesAndvip_page.setVisibility(View.GONE);
                    setAnimation(favoritesAndvip_page);
                } else {
                    setAnimation(favorites_page);
                }

                break;
            case R.id.vip_close://会员信息关闭按钮
                vip_page.setVisibility(View.GONE);//会员页面关闭
                if (favorites_page.getVisibility() != View.VISIBLE) {
                    favorites2.setVisibility(View.VISIBLE);
                }
                if (favorites_page.getVisibility() == View.GONE & vip_page.getVisibility() == View.GONE) {//判断是否都已关闭，是则背景也关闭
                    favoritesAndvip_page.setVisibility(View.GONE);
                } else {
                    setAnimation(vip_page);
                }

                break;
            case R.id.balance_tv://结账按钮
                choosePaymentType.setOnCheckedChangeListener(null);
                showClientPhone.clearFocus(); //清除焦点，以免影响微信扫码支付
                // 进入结算页面时断网
                if (GlobalController.getInstance().getSessionID() == null || (!NetworkUtils.isNetworkAvalible(getActivity().getApplicationContext()))) { // xxx 是否需要放前面
                    changeRadioButtonToPayViaCashOnly();
                    paymentCode = 0b00000000;
                    pay.setText("支  付");
                    pay.setBackgroundResource(R.drawable.button_bg_red);
                    pay.setEnabled(true);
                } else {
                    changeRadioButtonToPayViaWX();
                }
                balance();
                break;
            case R.id.list://挂单按钮
                // 将零售单的数据存放到list中，需要包括会员以及促销信息
                if (BaseActivity.retailTrade != null && BaseActivity.retailTrade.getListSlave1() != null && BaseActivity.retailTrade.getListSlave1().size() > 0) {
                    if (BaseActivity.retailTradeHoldBillList.size() >= BaseActivity.MAX_HOLD_BILL_NO) {
                        Toast.makeText(getActivity().getApplicationContext(), "挂单失败！挂单列表已有" + BaseActivity.MAX_HOLD_BILL_NO + "张未结零售单，请先处理", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "挂单成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "挂单失败！还未添加商品", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.payment_close://取消支付按钮
                confirmPaymentClose();
                break;
            case R.id.pay:
                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
                if (!isPayingViaCash && BaseActivity.retailTrade != null) {
                    // 判断优惠券是否可用
                    if (!validateCouponCodeAvailable()) {
                        break;
                    }
                    if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) { // 沙箱环境WX支付
                        if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || Double.valueOf(etWechatPayingAmount.getText().toString()) <= 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "微信支付金额必须大于0", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    payViaWX(Double.valueOf(getPayingMoney().getText().toString()));
                                }
                            }).start();
                        }
                    } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) { // 现金支付
                        if (StringUtils.isEmpty(etCashPayingAmount.getText().toString()) || Double.valueOf(etCashPayingAmount.getText().toString()) < 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "支付金额必须大于或等于0", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                        } else if ((etCashPayingAmount.getText().toString().length() - etCashPayingAmount.getText().toString().indexOf(".")) == 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
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
                .setTitle("提示")
                .setMessage("是否确定返回到主页面,这会取消本次交易！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        if (Double.parseDouble(totalpay_money.getText().toString()) - Double.parseDouble(unpaidBalance.getText().toString()) > RetailTrade.TOLERANCE) { //已经支付了部分钱
            // 如果存在现金支付，则使用现金退款（存在混合支付和全现金支付的情况） 暂无支付宝的情况
            if (!NetworkUtils.isNetworkAvalible(getActivity().getApplicationContext()) || totalCashAmount - 0.000000d > RetailTrade.TOLERANCE) {
                Toast.makeText(getActivity().getApplicationContext(), "请使用现金退还" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "元!", Toast.LENGTH_LONG).show();
            } else if (totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                // 微信支付，优先使用微信退款，微信退款失败，则使用现金退款
                if (wxRefund()) {
                    Toast.makeText(getActivity().getApplicationContext(), "微信退款成功, 无需现金退还", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "微信退款失败，请使用现金退还" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "元!", Toast.LENGTH_LONG).show();
                }
            } else {
                //有网、现金支付是0、微信支付是0
                throw new RuntimeException("未处理的分支！");
            }
            //
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "还没有进行支付金额，无需退款！", Toast.LENGTH_SHORT).show();
        }
    }

    //微信退款
    private boolean wxRefund() {
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setAmount(WXPayUtil.formatAmount(totalWechatAmount));
        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
        retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc"));
        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
        log.info("准备发起微信退款的对象为：" + retailTrade);
        //调用退款接口
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
            log.info("微信退款超时....");
            return false;
        }
        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.info("微信退款失败....");
            return false;
        }

        return true;
    }

    //触摸事件
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
//            case R.id.show_client_name://会员名称输入框
//                vip_page.setVisibility(View.VISIBLE);//显示会员页面
//                setAnimation(vip_page);
//                favoritesAndvip_page.setVisibility(View.VISIBLE);//显示背景
//                favorites2.setVisibility(View.GONE);//隐藏支付页面的收藏夹按钮
//                break;
            case R.id.show_client_phone://会员号(手机号)输入框
                if (vip_page.getVisibility() == View.GONE) {
                    vip_page.setVisibility(View.VISIBLE);//显示会员页面
                    setAnimation(vip_page);
                    favoritesAndvip_page.setVisibility(View.VISIBLE);//显示背景
                    favorites2.setVisibility(View.GONE);//隐藏支付页面的收藏夹按钮
                }
                //展示浮动键盘
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
            case R.id.scan_barcode_search:  //点击打开输入条形码的对话框，输入条形码，查找对应的商品
                searchCommodityByBarcode();
                break;
            case R.id.view:
                confirmPaymentClose();
            default:
                break;
        }
    }

    private void searchCommodityByBarcode() {
        log.debug("弹出搜索商品窗口");
        commodityListAfterQuery.clear();

        AlertDialog.Builder commodity_builder = null;
        View commodity_view = null;
        AlertDialog commodity_dialog = null;
        objectList = createAlertDialog(commodity_builder, commodity_view, commodity_dialog, (int) (screenWidth * 0.75), (int) (screenHeight * 0.75), R.layout.choose_commodity_dialog1);
        commodity_view = (View) objectList.get(1);
        rv_alertdialog_commodity = commodity_view.findViewById(R.id.rv_alertdialog_commodity);
        rv_alertdialog_commodity.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));//设置布局管理器，这里选择用竖直的列表

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
        search_commodity_et.addTextChangedListener(new TextWatcher() {//只要搜索框不为空，就会把下面展示商品的recyclerview加一列(库存)
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
                //根据输入的条形码在Barcode查找对应的商品的id，根据ID在Commodity查找商品
                String commodityBarcodes = search_commodity_et.getText().toString();
                if (commodityBarcodes.length() > BaseActivity.FUZZY_QUERY_LENGTH) {
                    log.debug("根据条形进行商品搜索，条形码：" + commodityBarcodes);
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
                                //选中状态点击之后改为未选中
                                commodityListAfterQuery.get(position).isSelect = false;
                                stock.setText("0");
                                dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                //未选中状态点击之后给为选中
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
                log.debug("点击取消按钮");
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
                    //如果有选中商品
                    if (commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).isSelect) {
                        Commodity dialogCommodity = new Commodity();
                        dialogCommodity.setID(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getID());
                        dialogCommodity.setBarcode(commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
                        //
                        isCommodityExistInUI(dialogCommodity);
                        scan_barcode_text.setHint("条形码：" + commodityListAfterQuery.get(dialogCommodityRecyclerViewAdapter.getPos()).getBarcode());
                        //
                        showCommodity(BaseActivity.showCommList);
                        // 恢复原先选中的商品为选中状态。之前如果没有选中的话，下面的代码也不会有事
                        commodityRecyclerViewAdapter.setDefSelect(POSITION_CommodityToRemove[0]);
                        commodityRecyclerViewAdapter.notifyDataSetChanged();
                        //
                        ((Dialog) objectList.get(2)).dismiss();
                    } else {
                        Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "无可添加到待售列表的商品", Toast.LENGTH_SHORT).show();
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
     * 创建对话框
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
     * 根据条形码查找商品
     *
     * @param barcode 要搜索的条形码
     * @return
     */
    private List<Commodity> retrieveNCommodityInSQLite(boolean isUseLike, String barcode) {
        List<Commodity> commodityList = new ArrayList<Commodity>();
        //在Barcodes中找到对应的commodityID，再根据commodityID找到对应的commodity
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
        log.info("commoditylist： " + commodityList);
        return commodityList;
    }

    /**
     * 判断该Commodity是否已经在待购列表中存在，若存在则只增加商品的购买数量和修改商品总价，否则在待购列表增加该商品
     *
     * @param c
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void isCommodityExistInUI(Commodity c) {
        boolean isAddToCommList = true;//于判断是否在待购列表增加商品用
        if (BaseActivity.showCommList.size() > 0) {//判断是否有重复ID，若有，则增加该商品数量和改变总价，否则在待购列表增加商品总价
            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
                if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() < Default_CommodityMaxNumber) {//已经存在该商品，只增加数量和改变商品总价
                    BaseActivity.showCommList.get(i).setCommodityQuantity(Integer.valueOf(BaseActivity.showCommList.get(i).getCommodityQuantity()) + 1);
                    BaseActivity.showCommList.get(i).setSubtotal(BaseActivity.showCommList.get(i).getCommodityQuantity() * BaseActivity.showCommList.get(i).getAfter_discount());
                    isAddToCommList = false;
                    break;
                } else if (BaseActivity.showCommList.get(i).getID().equals(c.getID()) && BaseActivity.showCommList.get(i).getCommodityQuantity() >= Default_CommodityMaxNumber) {
                    Toast.makeText(appApplication, "一次零售的一种商品的数量已达到最大", Toast.LENGTH_LONG).show();
                    isAddToCommList = false;
                    break;
                } else {
                    isAddToCommList = true;
                }
            }
        }
        if (isAddToCommList) {
            //根据商品ID找对对应的商品信息
            String barcode = c.getBarcode();
            c = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, c);
            //根据商品ID找到对应的barcodes
//            Barcodes barcodes = new Barcodes();
//            barcodes.setSql("where F_CommodityID = ?");
//            barcodes.setConditions(new String[]{String.valueOf(c.getID())});
//            List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(CASE_Barcodes_RetrieveNByConditions, barcodes);
            //根据商品ID找到对应的单位
            PackageUnit packageUnit = new PackageUnit();
            packageUnit.setID(Long.valueOf(c.getPackageUnitID()));
            packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);

            c.setBarcode(barcode);
            c.setCommodityQuantity(1);
            c.setPackageUnit(packageUnit.getName());
            c.setDiscount(0.000000d);// 折扣
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) c.getListSlave2().get(0);
            c.setAfter_discount(commodityShopInfo.getPriceRetail());
            c.setPriceRetail(commodityShopInfo.getPriceRetail());
            c.setSubtotal(c.getAfter_discount() * c.getCommodityQuantity());
            System.out.println("要展示在页面上的comm" + c);
            BaseActivity.showCommList.add(c);
        }
        // ...等待完善
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
        //...
        //副屏部分
        activity.isShowToCustomer(getActivity().getApplicationContext());
        //T1副屏
        showInT1Host();
    }

    /**
     * 记录选中的待购商品
     */
    final int[] POSITION_CommodityToRemove = {BaseSQLiteBO.INVALID_INT_ID};

    public void showCommodity(final List<Commodity> list) {
        scan_barcode_text.setText("");
        scan_barcode_text.setHint("扫描条形码（Ctrl+Q）");
        //计算商品总数量，并在UI展示
        int iCommodityQuantity = 0;
        for (int i = 0; i < list.size(); i++) {
            iCommodityQuantity += list.get(i).getCommodityQuantity();
        }
        commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNumber(i + 1);
        }
        rvCommodity.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvCommodity.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        commodityRecyclerViewAdapter = new CommodityRecyclerViewAdapter1(list, getActivity().getApplicationContext());

        //每次刷新RecyclerView，item之间的间距都会增大，以下两行代码解决此问题
        RecyclerView.ItemDecoration itemDecoration = rvCommodity.getItemDecorationAt(0);
        rvCommodity.removeItemDecoration(itemDecoration);
        rvCommodity.setAdapter(commodityRecyclerViewAdapter);
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
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
                    //选中状态点击之后改为未选中
//                    list.get(position).isSelect = false;
//                    POSITION_CommodityToRemove[0] = BaseSQLiteBO.INVALID_INT_ID;
                    showInputNumberDialog(position);
                    commodityRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    //未选中状态点击之后改为选中
                    list.get(position).isSelect = true;
                    commodityRecyclerViewAdapter.notifyDataSetChanged();
                    POSITION_CommodityToRemove[0] = position;
                    log.info("商品信息：" + list.get(position).toString());
                    showInputNumberDialog(position);
                }

            }

            private void showInputNumberDialog(final int position) {
                CustomDialog dialog = new CustomDialog(getActivity(), R.layout.input_number_dialog, false, 0.35, 0.65) {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void do_sure(String s) {
                        // 验证输入是否是正整数
                        try {
                            Integer.valueOf(reserve_et.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "请输入正整数", Toast.LENGTH_SHORT).show();
                            reserve_et.setText(Default_CommodityMinNumber);
                        }
                        list.get(position).setCommodityQuantity(Integer.valueOf(reserve_et.getText().toString()));
                        if (tempCalculatedRetailTrade != null) {
                            BaseActivity.retailTrade = (RetailTrade) tempCalculatedRetailTrade.clone();
                            tempCalculatedRetailTrade = null;
                        }
                        if ("".equals(reserve_et.getText().toString()) || Integer.valueOf(reserve_et.getText().toString()) <= 0) { // 用户不能输入1个或多个空格
                            Toast.makeText(getActivity(), "请输入正整数", Toast.LENGTH_SHORT).show();
                            reserve_et.setText(Default_CommodityMinNumber);
                        }
                        if (Integer.valueOf(reserve_et.getText().toString()) > Default_CommodityMaxNumber) {
                            Toast.makeText(getActivity(), "售卖商品数量最大值为" + Default_CommodityMaxNumber, Toast.LENGTH_SHORT).show();
                            reserve_et.setText(String.valueOf(Default_CommodityMaxNumber));
                        }
                        if (!"".equals(reserve_et.getText().toString()) && Integer.valueOf(reserve_et.getText().toString()) > 0) {
                            //更新了某个字段之后，需要重新导入数据到recyclerview,由于是虚拟数据所以没有更新
                            list.get(position).setCommodityQuantity(Integer.valueOf(reserve_et.getText().toString()));
                            list.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
                            // 查找属于该门店的促销
                            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                            if(avaliablePromotionList == null) {
                                Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            retailTradePromoting = new RetailTradePromoting();
                            BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
                            if (BaseActivity.retailTrade != null) {
                                total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
                            } else {
                                total_money.setText("0.00");
                            }
                            //副屏部分
                            activity.isShowToCustomer(getActivity().getApplicationContext());
                            //T1副屏
                            showInT1Host();
                            // ...等待完
                            //
                            commodityRecyclerViewAdapter.notifyDataSetChanged();
                            //计算商品总数量，并在UI展示
                            int iCommodityQuantity = 0;
                            for (int i = 0; i < list.size(); i++) {
                                iCommodityQuantity += list.get(i).getCommodityQuantity();
                            }
                            commodityQuantity.setText("商品数量：" + iCommodityQuantity + "件");
                            dismiss();
                        } else {
                            Toast.makeText(getActivity(), "需要填写大于0的商品数量", Toast.LENGTH_SHORT).show();
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
            activity.showTextInT1Host("", "欢迎惠顾");
        } else {
            double totalMoney = 0;
            for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
                totalMoney += Double.valueOf(BaseActivity.showCommList.get(i).getSubtotal()); // TODO
            }
            activity.showTextInT1Host("合计：", "￥" + GeneralUtil.formatToShow(totalMoney));
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
                //设置dialog大小
                WindowManager manager = getActivity().getWindowManager();
                Display display = manager.getDefaultDisplay();
                dialog.getWindow().setLayout((int) (display.getWidth() * 0.57), (int) (display.getHeight() * 0.2));
                TextView remove_commodity = view.findViewById(R.id.remove_commodity);
                commodityRecyclerViewAdapter.setDefSelect(position);
                commodityRecyclerViewAdapter.notifyDataSetChanged();
                if (list.get(position).isSelect == false) {
                    //未选中状态点击之后给为选中
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
                            // 查找属于该门店的促销
                            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                            if(avaliablePromotionList == null) {
                                Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
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
                            //在remove商品后增加点击事件，因为remove后要改变编号，然后需要重新写一个点击事件，否则点击无效果
                            mAdapter.setOnItemListener(new CommodityRecyclerViewAdapter1.OnItemListener() {
                                @Override
                                public void onClick(CommodityRecyclerViewAdapter1.MyViewHolder holder, final int position) {
                                    mAdapter.setDefSelect(position);
                                    mAdapter.notifyDataSetChanged();
                                    if (list.get(position).isSelect) {
                                        //选中状态点击之后改为未选中
                                        list.get(position).isSelect = false;
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        //未选中状态点击之后给为选中
                                        list.get(position).isSelect = true;
                                        mAdapter.notifyDataSetChanged();
                                        POSITION_CommodityToRemove[0] = position;
//                                Toast.makeText(MainActivity.this, "brand:" + list.get(position).getBrandID(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            mainCommodityRecyclerViewItemLongClick(mAdapter, list);
                            //计算商品总数量，并在UI展示
                            int iCommodityQuantity = 0;
                            for (int i = 0; i < list.size(); i++) {
                                iCommodityQuantity += list.get(i).getCommodityQuantity();
                            }
                            commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");
                            POSITION_CommodityToRemove[0] = -1;
                        } else {
                            Toast.makeText(getActivity(), "未选中商品", Toast.LENGTH_SHORT).show();
                        }
                        // 查找属于该门店的促销
                        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                        if(avaliablePromotionList == null) {
                            Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        retailTradePromoting = new RetailTradePromoting();
                        BaseActivity.retailTrade = promotionCalculator.sell(BaseActivity.showCommList, avaliablePromotionList, retailTradePromoting);
                        if (BaseActivity.retailTrade != null) {
                            total_money.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
                        } else {
                            total_money.setText("0.00");
                        }
                        //副屏部分
                        activity.isShowToCustomer(getActivity().getApplicationContext());
                        //T1副屏
                        showInT1Host();
                        // ...等待完善
                        showCommodity(BaseActivity.showCommList);
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    /**
     * 现金支付
     */
    private void payViaCash(Double payAmount) {
        isPayingViaCash = true;
        handlerMessage.setErrorCode(EC_NoError);// 初始化消息ErrorCode
        //
        paymentCode = paymentCode | cashPayCode;
        BaseActivity.retailTrade.setPaymentType(paymentCode);
        cashAmount = payAmount;
        totalCashAmount += cashAmount;
//        cashAmount = GeneralUtil.sum(Double.valueOf(etCashPayingAmount.getText().toString()), payAmount);
        BaseActivity.retailTrade.setAmountCash(cashAmount); // 设置支付金额，如果有找零的情况，会在下面进行处理
        //计算出支付金额比未付余额大出多少
        Double returnMoney = GeneralUtil.sub(Double.valueOf(getPayingMoney().getText().toString()), Double.valueOf(unpaidBalance.getText().toString()));
        if (returnMoney > BaseModel.TOLERANCE) {
            handlerMessage.setMsg("支付成功,请使用现金退还" + GeneralUtil.formatToShow(returnMoney));
            keyBoard.cleanNumber();
        } else {
            handlerMessage.setMsg("支付成功");
            keyBoard.cleanNumber();
        }
        if (totalCashAmount + totalWechatAmount >= BaseActivity.retailTrade.getAmount()) {
            if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //判断是全额支付还是部分支付
                if (BaseActivity.retailTrade.getAmount() > RetailTrade.TOLERANCE) {
                    if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                        BaseActivity.retailTrade.setPaymentType(5);
                    } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseActivity.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                        BaseActivity.retailTrade.setPaymentType(4);
                    } else if (totalCashAmount - BaseActivity.retailTrade.getAmount() >= 0) {
                        BaseActivity.retailTrade.setPaymentType(1);
                    } else {
                        throw new RuntimeException("错误的支付方式");
                    }
                }

                //设置上一单支付方式
                int paymentType = BaseActivity.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseActivity.lastRetailTradePaymentType = pay_cash;
                } else if (paymentType == 4) {
                    BaseActivity.lastRetailTradePaymentType = pay_wetchat;
                } else {
                    BaseActivity.lastRetailTradePaymentType = pay_combination;
                }
                //设置上一单支付金额
                BaseActivity.lastRetailTradePaidInAmount = GeneralUtil.sum(totalCashAmount, totalWechatAmount);
                //设置上一单找零金额
                BaseActivity.lastRetailTradeChangeMoney = sub(totalCashAmount + totalWechatAmount, BaseActivity.retailTrade.getAmount());
//                BaseActivity.lastRetailTradeChangeMoney = sub(GeneralUtil.round(getPayingMoney().getText().toString(), 6), GeneralUtil.round(unpaidBalance.getText().toString(), 6));
                //设置上一单总金额
                BaseActivity.lastRetailTradeAmount = BaseActivity.retailTrade.getAmount();
                // 现金支付，存在支付金额大于零售单总金额的情况，这个时候需要减去找零金额，当不需要找零时，BaseActivity.lastRetailTradeChangeMoney应该为0
//                BaseActivity.retailTrade.setAmountCash(totalCashAmount);
                BaseActivity.retailTrade.setAmountWeChat(totalWechatAmount);
                BaseActivity.retailTrade.setAmountCash(sub(totalCashAmount, BaseActivity.lastRetailTradeChangeMoney));
                BaseActivity.retailTrade.setAmountPaidIn(GeneralUtil.sum(totalCashAmount, totalWechatAmount));
                BaseActivity.retailTrade.setAmountChange(sub(totalCashAmount + totalWechatAmount, BaseActivity.retailTrade.getAmount()));
                log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + BaseActivity.retailTrade.getAmountCash());
                // 是否使用了优惠券
                if (couponCode != null) {
                    calculateReturnPrice(BaseActivity.retailTrade, BaseActivity.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseActivity.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseActivity.retailTrade);
                }
                appendRetailTradePromoting(BaseActivity.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) { //TODO
                }
                //  由于现在同步线程是5秒上传一次，和立即上传的时间不会相差太多，所以不需要立即上传会员零售单
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
            originalTotalAmount += rtc.getPriceReturn() * rtc.getNO(); //使用退货价是因为该商品可能参与了促销。使用优惠券后重新计算退货价,需要使用促销后的价格
        }

        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            // 原因是，按照先乘后除的计算顺序可以减少计算误差
            rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), fBestAmount), GeneralUtil.mul(originalTotalAmount, rtc.getNO()), 6))));
        }
    }

    /**
     * String类型转double类型进行比较
     * 如果返回负数说明d1<d2
     * 返回正数说明d1>d2
     * 返回0说明相等
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
     * 全部支付后，在本地创建该零售单
     */
    private boolean createRetailTradeLocally(BaseFragment1.HandlerMessage hm) {
        if (vip != null) {
            BaseActivity.retailTrade.setVipID(vip.getID());
        }
        // 设置shopID
        Pos posR1Condition = new Pos();
        posR1Condition.setID(Constants.posID);
        Pos pos = (Pos) posPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, posR1Condition);
        if(pos == null) {
            hm.setErrorCode(posPresenter.getLastErrorCode());
            hm.setMsg(posPresenter.getLastErrorMessage());
            return false;
        }
        BaseActivity.retailTrade.setShopID((int)pos.getShopID());

        log.info("要创建的零售单为：" + BaseActivity.retailTrade);
        //如果不是会员消费，先存在本地(先在本地插入RetailTrade，得到临时的TradeID，设置到retailTradePromoting的TradeID中，然后在本地插入retailTradePromoting，得到的临时ID设置到RetailTrade的int2，vip也一样)，等到第一张单的c分钟后，查找本地临时零售单上传到服务器
        RetailTrade rtCreate = (RetailTrade) retailTradeSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, BaseActivity.retailTrade);
        if (rtCreate == null) {
            log.error("在本地插入临时零售单失败！");
            hm.setErrorCode(EC_OtherError);
            hm.setSubErrorCode(SubErrorCode_RetailTrade_OtherError);
            return false;
        }

        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradeSQLiteEvent.getLastErrorCode() != null) {
            log.error("在本地插入临时零售单失败：" + retailTradeSQLiteEvent.getLastErrorMessage());
            hm.setErrorCode(retailTradeSQLiteEvent.getLastErrorCode());
            hm.setMsg(retailTradeSQLiteEvent.getLastErrorMessage());
            return false;
        }
        // 判断是否需要上传计算过程
        if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
            RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting);
            if (rtpCreate == null) {
                log.error("在本地插入临时计算过程失败！");
                hm.setErrorCode(EC_OtherError);
                hm.setSubErrorCode(SubErrorCode_RetailTradePromoting_OtherError);
                return false;
            }
            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradePromotingSQLiteEvent.getLastErrorCode() != null) {
                log.error("在本地插入临时计算过程失败：" + retailTradePromotingSQLiteEvent.getLastErrorMessage());
                hm.setErrorCode(retailTradePromotingSQLiteEvent.getLastErrorCode());
                hm.setMsg(retailTradePromotingSQLiteEvent.getLastErrorMessage());
                return false;
            }
        }


        //判断是否需要创建零售单优惠券表
        if (BaseActivity.retailTrade.getListSlave3() != null) {
            List<BaseModel> retailTradeCouponList = (List<BaseModel>) BaseActivity.retailTrade.getListSlave3();
            RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradeCoupon_CreateSync, retailTradeCouponList.get(0));
            if (retailTradeCouponCreate == null) {
                log.error("在本地插入临时零售单优惠券使用表失败！");
                hm.setErrorCode(EC_OtherError);
                hm.setSubErrorCode(SubErrorCode_RetailTradeCoupon_OtherError);
                hm.setMsg("插入临时零售单优惠券使用表失败！"); //
                return false;
            }
        }
        // 保存最后一张非退货型零售单的数据，以便首页能打印上一单
        BaseActivity.lastRetailTrade = BaseActivity.retailTrade;
        return true;
    }

    private void updateRetailTradeAggregation() {
        log.info("本地零售单创建成功后,开始更新收银汇总的所有数据");

        // 更新收银汇的所有数据:
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        //未发生变化的数据
        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        //发生变化的数据：
        retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTrade.getAmountCash()));/*现金收入*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));/*微信收入*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
        retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);/*销售单数*/
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前零售单的生成时间*/
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("准备更新收银汇总失败！");
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
            log.error("Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("Update错误码不正确！" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo());
        } else {
            // 累计交班需要的数据(零售单创建成功后才做)
            BaseActivity.retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTrade.getAmountCash()));
            BaseActivity.retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));
            BaseActivity.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTradeAggregation.getWechatAmount()));
            BaseActivity.retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);//本地零售单创建成功后BaseActivity.retailTradeAggregation的TradeNO+1
            BaseActivity.retailTradeAggregation.setWorkTimeEnd(date);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 由于创建零售单和创建零售单促销过程都使用同步接口，不再会post Event 到Activity，所以也不会发送该消息
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
                    Toast.makeText(getActivity().getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 3: // 微信支付相关UI操作
                    Main1Activity.HandlerMessage mth = (Main1Activity.HandlerMessage) msg.obj;
                    switch (mth.getErrorCode()) {
                        case EC_NoError:
                            Toast.makeText(getActivity().getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                            //
//                            wechatPaymentMoney.setText(GeneralUtil.formatToShow(wetchatAmount)); // 设置微信支付金额
                            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //判断是全额支付还是部分支付
                                pay.setText("支付完成");
                                pay.setEnabled(false);
                                pay.setBackgroundResource(R.drawable.button_background_disable);
                                //打印小票
                                try {
                                    printSmallSheet(BaseActivity.lastRetailTrade);
                                } catch (Exception e) {
                                    log.info("打印小票异常：" + e.getMessage());
                                }
                                // TODO 支付完成。。。
                                resetTradeAfterPay(1);
                                initializeVipData();
                            } else {
                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseActivity.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
                                getPayingMoney().setText(unpaidBalance.getText().toString());
                                //一个订单不允许扫码俩次，所以支付部分金额后选为现金支付；
                                cashPay.setChecked(true);
                                wechatPay.setClickable(false);
                                spinner.setEnabled(false);
                            }
//                            } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) < 0) {
//                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(Double.valueOf(unpaidBalance.getText().toString()), Double.valueOf(etWechatPayingAmount.getText().toString()))));
//                                getPayingMoney().setText(unpaidBalance.getText().toString());
//                                etCashPayingAmount.setText(unpaidBalance.getText()); // 微信部分支付后将未付余额赋值给现金支付，当前系统中微信支付只能一次。
//                            }
//                            changeRadioButtonToPayViaCashOnly();
                            break;
                        case EC_InvalidSession:
                            Toast.makeText(getActivity().getApplicationContext(), BaseHttpBO.ERROR_MSG_Network, Toast.LENGTH_SHORT).show();
                            changeRadioButtonToPayViaCashOnly();
                            break;
                        case EC_WechatServerError:
                            Toast.makeText(getActivity().getApplicationContext(), "微信支付失败，" + (mth.getMsg() == null ? "" : mth.getMsg()) + "请稍后重试或者换其他的支付方式。", Toast.LENGTH_SHORT).show();
                            break;
                        case EC_OtherError:
                            switch (mth.getSubErrorCode()) {
                                case SubErrorCode_RetailTrade_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "创建零售单失败！", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradePromoting_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "创建促销计算过程失败！", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradeCoupon_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "创建零售单优惠券使用失败！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                        case EC_Timeout:
                            Toast.makeText(getActivity().getApplicationContext(), "微信支付超时!", Toast.LENGTH_SHORT).show();
                            changeRadioButtonToPayViaCashOnly();
                            break;
                    }
                    bLastWxPayIsNotYetDone = false;
                    closeLoadingDialog(loadingDailog);
                    break;
                case 4: // 现金支付相关UI操作
                    Main1Activity.HandlerMessage mthCash = (Main1Activity.HandlerMessage) msg.obj;
                    switch (mthCash.getErrorCode()) {
                        case EC_NoError:
                            Toast.makeText(getActivity().getApplicationContext(), mthCash.getMsg(), Toast.LENGTH_SHORT).show();
                            //
//                            cashPaymentMoney.setText(GeneralUtil.formatToShow(cashAmount));//显示在UI时保留两位小数
                            if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //判断是全额支付还是部分支付
                                pay.setText("支付完成");
                                pay.setEnabled(false);
                                pay.setBackgroundResource(R.drawable.button_background_disable);
                                // 保存最后一张非退货型零售单的数据，以便首页能打印上一单
                                BaseActivity.lastRetailTrade = BaseActivity.retailTrade;
                                //打印小票
                                try {
                                    printSmallSheet(BaseActivity.lastRetailTrade);
                                } catch (Exception e) {
                                    log.info("打印小票异常：" + e.getMessage());
                                }
                                BaseActivity.retailTrade = null;
                                System.out.println("零售单已结清，返回主页面");
                                // 支付完成，跳转页面
                                // TODO 支付完成
                                // 当次为现金支付，已支付金额为多少，未支付金额为多少，找零多少
                                // 如果已支付金额还小于应付金额，需要进行下一次支付
                                // 如果已支付金额已经等于应付金额，则关闭支付界面，清空BaseActivity.showCommodity
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
                                    Toast.makeText(getActivity().getApplicationContext(), "创建零售单失败！", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradePromoting_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "创建促销计算过程失败！", Toast.LENGTH_SHORT).show();
                                    break;
                                case SubErrorCode_RetailTradeCoupon_OtherError:
                                    Toast.makeText(getActivity().getApplicationContext(), "创建零售单优惠券使用失败！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                    }
                    isPayingViaCash = false;
                    closeLoadingDialog(loadingDailog);
                    break;
                case 5: // 查找会员后的处理
                    ErrorInfo errorInfo = new ErrorInfo();
                    do {
                        if (vipHttpEvent.getLastErrorCode() == EC_NoError) {
                            vip = (Vip) vipHttpEvent.getBaseModel1();
                            if (vip != null) {
                                // ...渲染VIP消息
                                log.debug("查找到的会员为:" + vip);
                                showClientPhone.setVisibility(View.GONE);
                                search_vip.setVisibility(View.GONE);
                                //
                                vipMobile.setText("会员手机号：" + vip.getMobile());
                                vipBonus.setText("积分：" + vip.getBonus());
                                vipMobile.setVisibility(View.VISIBLE);
                                vipBonus.setVisibility(View.VISIBLE);
                                return_search_vip.setVisibility(View.VISIBLE);
                                // 查找此会员相关的优惠券
                                CouponCode couponCode = new CouponCode();
                                couponCode.setVipID(vip.getID().intValue());
                                couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                                couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                                if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                                    errorInfo.setErrorMessage("查找会员的优惠券失败，原因：" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage());
                                    showToastMessage("查找会员的优惠券失败");
                                    break;
                                }
                                showToastMessage("查找会员成功，正在查找会员的优惠券...");
                            } else {
                                errorInfo.setErrorMessage("查找不到相关会员");
                                showToastMessage("查找不到相关会员");
                                break;
                            }
                        } else {
                            errorInfo.setErrorMessage("查找会员失败");
                            showToastMessage("查找会员失败");
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
                        showToastMessage("查找会员的优惠券成功");
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "查找会员的优惠券失败，原因：" + couponCodeHttpEvent.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
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
        vipMobile.setText("会员手机号：");
        vipBonus.setText("积分：");
        //
        vipMobile.setVisibility(View.GONE);
        vipBonus.setVisibility(View.GONE);
        return_search_vip.setVisibility(View.GONE);
        //
        showClientPhone.setVisibility(View.VISIBLE);
        search_vip.setVisibility(View.VISIBLE);
    }


    /**
     * 重置交易。挂单后，重新初始化零售单和UI
     */
    private void resetTradeAfterHoldBill() {
        BaseActivity.retailTrade = null;
        total_money.setText("0.00");
        BaseActivity.showCommList.clear();
        showCommodity(BaseActivity.showCommList);
        retailTradePromoting = new RetailTradePromoting();// 促销过程重置
    }

    /**
     * 重置交易，以免污染下一个交易
     *
     * @param isPaid 已经支付完毕
     */
    private void resetTradeAfterPay(int isPaid) {
        payment.setVisibility(View.GONE);//关闭结账页面
        setAnimation(payment);
        viewMain1Activity.setVisibility(View.INVISIBLE);
        bPaying = false;
        //
        if (isPaid == 1) {
            BaseActivity.showCommList.clear();
            showCommodity(BaseActivity.showCommList);
            total_money_double = 0.000000d;
            commodityQuantity.setText("商品数量：" + "0" + " 件");//初始话商品数量UI
            retailTradePromoting = new RetailTradePromoting();// 促销过程重置
            BaseActivity.retailTrade = null;
            total_money.setText("0.00");
            totalWechatAmount = 0.000000d;
            totalCashAmount = 0.000000d;
            etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));

            // 上一单信息
            lastRetailTradeAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeAmount));
            lastRetailTradeChangeMoney.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradeChangeMoney));
            lastRetailTradePaymentType.setText(BaseActivity.lastRetailTradePaymentType);
            lastRetailTradePaidInAmount.setText(GeneralUtil.formatToShow(BaseActivity.lastRetailTradePaidInAmount));
        } else {
            // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
            System.out.println("没支付就终止了交易");
        }
        HideVirtualKeyBoard();
    }

    /**
     * 重置交易，不清空待购商品列表
     */
    private void resetPayment() {
        payment.setVisibility(View.GONE);//关闭结账页面
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
     * 点击键盘，输入当前支付方式的支付金额时，需要确定当前输入的文本框是微信金额的文本框还是现金金额的文本框，返回相应的文本框
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
        log.info("设置RadioButton的属性,当前状态为:" + bCanPayViaCashOnly);
        if (!bCanPayViaCashOnly) {
            cashPay.setChecked(true); // 设置现金按钮被点击
//            wechatPay.setEnabled(false);// 设置微信支付按钮不给与点击
            wechatPay.setClickable(false);
            bCanPayViaCashOnly = true; // 设置是否改变RadionButton
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
            if (retailTradePromoting.getListSlave1() == null) {  //getListSlave1为空的情况下，那么该单是不参与促销，但是使用优惠券
                retailTradePromoting.setListSlave1(new ArrayList<BaseModel>());
            }
            if (retailTradePromoting == null || retailTradePromoting.getListSlave1().size() == 0) {
                long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                retailTradePromoting.setTradeID(retailTrade.getID().intValue());
                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());

                tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);

            } else {
                //从表有数据则说明参与了促销，需要在后面继续累加
                tmpRetailTradePromotingFlowIDInSQLite = ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(retailTradePromoting.getListSlave1().size() - 1)).getID() + 1;
            }
            //
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("此单使用优惠券前支付价为:" + GeneralUtil.formatToCalculate(payAmount) + "元 \t")//
                    .append("优惠券标题为:‘" + coupon.getTitle() + "’\t"); //
            if (coupon.getType() == Coupon.EnumCouponCardType.ECCT_CASH.getIndex()) {
                promotingFlow.append("支付金额满" + coupon.getLeastAmount() + "元减" + coupon.getReduceAmount() + "\t")//
                        .append("实际支付:" + retailTrade.getAmount());
            } else {
                promotingFlow.append("支付金额满" + coupon.getLeastAmount() + "元进行全场" + coupon.getDiscount() + "折优惠") //
                        .append("实际微信支付:" + retailTrade.getAmount());
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
     * 微信支付
     */
    private void payViaWX(Double payAmount) {
        bLastWxPayIsNotYetDone = true;
        handlerMessage.setErrorCode(EC_NoError); // 初始化消息ErrorCode
        //
        Message message = new Message();
        message.what = 3;
        // 微信支付
        if (!wxPayment(payAmount, handlerMessage)) {
            message.obj = handlerMessage;
            handler.sendMessage(message);
            return; // 方法中已经有提示信息，微信支付失败后，不再做其他操作。
        }
        log.info("==unpaidBalance.toString():" + unpaidBalance.toString() + "=====wetchatAmount:" + wetchatAmount);
        log.info("微信支付成功！");
        totalWechatAmount += payAmount;
        keyBoard.cleanNumber();

        if (totalCashAmount + totalWechatAmount >= BaseActivity.retailTrade.getAmount()) {
            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //判断是全额支付还是部分支付
                if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                    BaseActivity.retailTrade.setPaymentType(5);
                } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseActivity.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                    BaseActivity.retailTrade.setPaymentType(4);
                } else if (totalCashAmount - BaseActivity.retailTrade.getAmount() >= 0) {
                    BaseActivity.retailTrade.setPaymentType(1);
                } else {
                    throw new RuntimeException("错误的支付方式");
                }
                //设置上一单的支付方式
                int paymentType = BaseActivity.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseActivity.lastRetailTradePaymentType = pay_cash;
                } else if (paymentType == 4) {
                    BaseActivity.lastRetailTradePaymentType = pay_wetchat;
                } else {
                    BaseActivity.lastRetailTradePaymentType = pay_combination;
                }
                //设置上一单总金额
                BaseActivity.lastRetailTradeAmount = BaseActivity.retailTrade.getAmount();
                BaseActivity.retailTrade.setAmountCash(totalCashAmount);
                BaseActivity.retailTrade.setAmountWeChat(totalWechatAmount);
                log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + BaseActivity.retailTrade.getAmountCash());
                // 是否使用了优惠券
                if (couponCode != null) {
                    calculateReturnPrice(BaseActivity.retailTrade, BaseActivity.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseActivity.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseActivity.retailTrade);
                }
                appendRetailTradePromoting(BaseActivity.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) {

                }
                // 由于现在同步线程是5秒上传一次，和立即上传的时间不会相差太多，所以不需要立即上传会员零售单
//                if (vip != null) {
//                    if (!uploadRetailTrade(handlerMessage)) { //TODO
//                    }
//                }
                updateRetailTradeAggregation();

                BaseActivity.retailTrade = null;
                System.out.println("零售单已结清，返回主页面");
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

    // 由于现在同步线程是5秒上传一次，和立即上传的时间不会相差太多，所以不需要立即上传会员零售单
//    private boolean uploadRetailTrade(BaseFragment1.HandlerMessage hm) {
//        hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        hm.setSubErrorCode(SubErrorCode_RetailTrade_VipUpload_OtherError);
//        do {
//            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//            retailTradeSQLiteBO.getSqLiteEvent().setTmpMasterTableObj(BaseActivity.retailTrade);
//
//            if (!retailTradeHttpBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, BaseActivity.retailTrade)) {
//                log.error("会员立刻上传零售单失败.零售单为" + BaseActivity.retailTrade);
//                if (retailTradeHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
//                    log.info("SessionID=null，下次同步时上传");
//                    return true;
//                }
//                return false;
//            }
//            //等待处理完毕
//            long lTimeout = TIME_OUT;
//            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (lTimeout == 0 && retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                log.error("会员立刻上传零售单超时");
//                return false;
//            }
//            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
//                log.error("会员立刻上传零售单失败.失败原因：" + retailTradeSQLiteBO.getSqLiteEvent().getLastErrorMessage());
//                return false;
//            }
//            hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//            hm.setSubErrorCode(ErrorInfo.EnumErrorCode.EC_NoError.getIndex());
//        } while (false);
//        return true;
//    }

    private boolean wxPayment(Double payAmount, BaseFragment1.HandlerMessage hm) {
        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(payAmount);
        System.out.println("-------------------------------------------------开始微信支付,支付金额：" + payAmount);
        WXPayInfo wxPayInfo = new WXPayInfo();
        wxPayInfo.setAuth_code(wxPayAuthCode);
        wxPayInfo.setTotal_fee(dPaymentMoney);

        //结算时选择了微信支付，然后断网，在这里进行判断。支付一次失败后（短暂断网），不会将session设置为null，下次支付仍然可以使用微信支付
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
            log.info("微信支付超时....");
            hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
            return false;
        }
        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) { // 支付失败后，不会将session设置为null，下次支付仍然可以使用微信支付
            log.info("微信支付失败....");
            hm.setErrorCode(EC_WechatServerError);
            hm.setMsg(wxPayHttpEvent.getLastErrorMessage());
            return false;
        }
        paymentCode = paymentCode | wechatPayCode;
        BaseActivity.retailTrade.setPaymentType(paymentCode);
        //
        // TODO 这里etWechatPayingAmount是文本框输入的金额
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
     * 初始化部分控件
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
            //将RetailTrade的信息传递到支付界面
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
                            retailTradeCommodity.setBarcodeID(barcodesList.get(0).getID().intValue());//...暂未考虑一品多码的问题
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
                    //为RetailTradePromoting设置ID、tradeID和RetailTradePromotingFlow设置ID
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
            payment.setVisibility(View.VISIBLE);//显示结账页面
            totalpay_money.setText(String.valueOf(BaseActivity.retailTrade.getAmount()));
            total_money.setText(String.valueOf(BaseActivity.retailTrade.getAmount()));
            setAnimation(payment);
            initWidget();
            //
            viewMain1Activity.setVisibility(View.VISIBLE);
            bPaying = true;
            //
            //选择支付方式 （如果进支付页面时，微信支付可用，就会进入该分支）。
            if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
                if (Configuration.bUseSandBox) {
                    pay.setBackgroundResource(R.drawable.button_bg_gray);
                    pay.setText("请 扫 码");
                    pay.setEnabled(false);
                } else {
                    // pay.setBackgroundResource(R.drawable.button_background_disable);
                    pay.setText("支付完成");//... 不用沙箱支付却显示支付完成，是不是会引起误会？
                    pay.setEnabled(false);
                }
            }
            // 单选按钮发生改变
            choosePaymentType.setOnCheckedChangeListener(new CustomRaidoGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View group, @IdRes int checkedId) {
                    System.out.println("-----------改变");
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
                            pay.setText("请 扫 码");
                            pay.setEnabled(false);
                        } else {
                            pay.setBackgroundResource(R.drawable.button_background_disable);
                            pay.setText("支付完成");
                            pay.setEnabled(false);
                        }
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
//                        if (!StringUtils.isEmpty(etWechatPayingAmount.getText().toString())) {
//                            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                                Toast.makeText(getActivity().getApplicationContext(), "微信支付不能大于未付款金额", Toast.LENGTH_SHORT).show();
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
                        // 如果未付余额大于最大现金支付金额
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
                        pay.setText("支  付");
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
                        Toast.makeText(getActivity().getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString())) {
                        Toast.makeText(getActivity().getApplicationContext(), "微信支付金额必须大于0", Toast.LENGTH_SHORT).show();
                        etWechatPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "微信支付不能大于未付款金额", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
                        etCashPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (StringUtils.isEmpty(etCashPayingAmount.getText().toString()) || Double.valueOf(etCashPayingAmount.getText().toString()) < 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "支付金额必须大于或等于0", Toast.LENGTH_SHORT).show();
                        etCashPayingAmount.setText(unpaidBalance.getText().toString());
                        keyBoard.cleanNumber();
                    } else if (compareString(etCashPayingAmount.getText().toString(), MAX_CashPaymentAmount) > 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "现金支付的金额不能大于" + MAX_CashPaymentAmount, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "商品列表为空", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 用户设置ReserveAmount后，创建收银汇总到本地sqlite中
     */
    private void createRetailTradeAggregation() {
        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
        //准备金被点确定之前，day end 收银汇总 dialog已经弹出来。当dialog消失时，如果不设置work time start为第2天的时间，会导致work time start是前一天的时间。
        //这不符合我们的业务逻辑且在上传时，因为day end dialog结束时已经创建了一个retailTradeAggregation在SQLite中，现在用户点击确定后，又创建了一个retailTradeAggregation。这种情况无法避免但是暂时可以接受。
        //我们规定收银汇总必须是当天的，不可以跨天。
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
            log.error("创建零售单汇总失败");
        }
    }

    /* 初始化扫码枪 */
    private void initScanGun() {
        // 设置key事件最大间隔，默认20ms，部分低端扫码枪效率低
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScanFinish(String scanResult) {
                if (payment.getVisibility() != View.GONE) { // 支付页面没关闭
                    loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
                    // 判断优惠券是否可用
                    if (!validateCouponCodeAvailable()) {
                        return;
                    }
                    if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
                        if (scanResult != null && !"".equals(scanResult) && scanResult.length() == LENGTH_WxPayAuthCode) { //目前扫码仅支持微信支付
                            wxPayAuthCode = scanResult;
                            if (wxPayAuthCode != null && !"".equals(wxPayAuthCode)) {
                                if (!bLastWxPayIsNotYetDone && BaseActivity.retailTrade != null) {
                                    if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || sub(Double.valueOf(etWechatPayingAmount.getText().toString()), MIN_WechatPaymentAmount) < BaseModel.TOLERANCE) {
                                        closeLoadingDialog(loadingDailog);
                                        Toast.makeText(getActivity().getApplicationContext(), "支付金额必须大于0", Toast.LENGTH_SHORT).show();
                                    } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
                                        closeLoadingDialog(loadingDailog);
                                        Toast.makeText(getActivity().getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity().getApplicationContext(), "错误的付款码，请重新扫码！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        closeLoadingDialog(loadingDailog);
                        if (totalWechatAmount > BaseModel.TOLERANCE) {
                            Toast.makeText(getActivity().getApplicationContext(), "一张零售单只能微信收款1次，这张单已使用过微信收款", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "现金收款，扫码无效！请手工输入顾客付款金额", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //扫条形码
                    log.info("扫码枪扫描到的数据：" + scanResult);
                    if (StringUtils.isEmpty(scanResult) || scanResult.length() <= BaseActivity.FUZZY_QUERY_LENGTH) {
                        Toast.makeText(getActivity(), "扫描到的条码为空或者条码数字少于" + String.valueOf(BaseActivity.FUZZY_QUERY_LENGTH + 1) + "位", Toast.LENGTH_SHORT).show();
                        log.info("扫描到的条码为空或者条码数字少于" + String.valueOf(BaseActivity.FUZZY_QUERY_LENGTH + 1) + "位");
                        return;
                    }

                    if (scanResult.length() == Vip.Max_LENGTH_VipCardSN && scanResult.substring(0, 6).equals(Constants.MyCompanySN)) {
                        showClientPhone.setText(scanResult);
                    } else {
                        commodityListAfterQuery.clear();
                        scan_barcode_text.setText(scanResult);
                        commodityListAfterQuery = retrieveNCommodityInSQLite(true, scanResult);
                        log.info("根据条形码查出数据：" + commodityListAfterQuery.size() + "扫码枪扫描到的数据：" + scanResult);
                        log.info("showCommList：" + BaseActivity.showCommList);
                        if (commodityListAfterQuery == null || commodityListAfterQuery.size() == 0) {
                            Toast.makeText(getActivity(), "未找到商品", Toast.LENGTH_SHORT).show();
                            scan_barcode_text.setText("");
                        } else {
                            //搜到的商品多于一个，或只有一个但是和scanResult不完全一致
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
                                            //选中状态点击之后改为未选中
                                            commodityListAfterQuery.get(position).isSelect = false;
                                            stock.setText("0");
                                            dialogCommodityRecyclerViewAdapter.notifyDataSetChanged();
                                        } else {
                                            //未选中状态点击之后改为选中
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
                                        log.debug("点击取消按钮");
                                        ((Dialog) objectList.get(2)).dismiss();
                                        commodityListAfterQuery.clear();
                                    }
                                });

                                TextView add_tv = commodity_view.findViewById(R.id.add_tv);
                                add_tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        log.debug("增加商品");
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
                            } else { //只有一个候选商品，且它的条形码和scanResult完全一致
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

//        discount_sp.setEnabled(false); // TODO 设置界面中的自定义折扣的控件Spinner不可使用(该功能暂未实现)
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
        // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            Toast.makeText(getActivity(), "查询促销信息失败", Toast.LENGTH_SHORT).show();
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

    //通过线程每0.1秒发送一次消息
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
        pay.setText("请 扫 码");
        pay.setBackgroundResource(R.drawable.button_bg_gray);
        pay.setEnabled(false);
//        wechatPay.setEnabled(true);// 设置微信支付按钮给与点击
        wechatPay.setClickable(true);
        wechatPay.setChecked(true); // 设置微信按钮被点击
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

    public void onKeyDownChild(int keyCode, KeyEvent event) {
        log.info("---------------------------------onKeyDown:    KeyCode:" + keyCode + "------event:" + event + "------------------------------------");
        scanGun.isMaybeScanning(keyCode, event);
    }

    public boolean validateCouponCodeAvailable() {
        // 判断优惠券是否可用
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
                Toast.makeText(getActivity().getApplicationContext(), "此优惠券不可用,请选择其他优惠券", Toast.LENGTH_SHORT).show();
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
            boolean exist = false; // 记录本促销门店范围是否存在该门店
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
                exist = true; // 该促销门店范围为所有门店
            }
            if(exist) {
                avaliablePromotionList.add(promotion);
            }
        }
        return avaliablePromotionList;
    }
}
