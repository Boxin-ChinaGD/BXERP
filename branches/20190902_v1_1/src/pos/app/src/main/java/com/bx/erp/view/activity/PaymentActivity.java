//package com.bx.erp.view.activity;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.IdRes;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Display;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TableLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bx.erp.R;
//import com.bx.erp.bo.BaseHttpBO;
//import com.bx.erp.bo.BaseSQLiteBO;
//import com.bx.erp.bo.NtpHttpBO;
//import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
//import com.bx.erp.bo.RetailTradeCouponSQLiteBO;
//import com.bx.erp.bo.RetailTradeHttpBO;
//import com.bx.erp.bo.RetailTradePromotingHttpBO;
//import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
//import com.bx.erp.bo.RetailTradeSQLiteBO;
//import com.bx.erp.bo.WXPayHttpBO;
//import com.bx.erp.bo.WxCouponHttpBO;
//import com.bx.erp.common.GlobalController;
//import com.bx.erp.event.BaseEvent;
//import com.bx.erp.event.RetailTradeHttpEvent;
//import com.bx.erp.event.RetailTradePromotingHttpEvent;
//import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
//import com.bx.erp.event.UI.BaseSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
//import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
//import com.bx.erp.event.WXPayHttpEvent;
//import com.bx.erp.event.WxCouponHttpEvent;
//import com.bx.erp.helper.Configuration;
//import com.bx.erp.helper.Constants;
//import com.bx.erp.helper.ScanGun;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.RetailTrade;
//import com.bx.erp.model.RetailTradeAggregation;
//import com.bx.erp.model.RetailTradeCommodity;
//import com.bx.erp.model.RetailTradeCoupon;
//import com.bx.erp.model.RetailTradePromoting;
//import com.bx.erp.model.RetailTradePromotingFlow;
//import com.bx.erp.model.WXPayInfo;
//import com.bx.erp.model.wx.coupon.WxCoupon;
//import com.bx.erp.model.wx.coupon.WxCouponDetail;
//import com.bx.erp.model.wx.coupon.WxCouponDetailPartition;
//import com.bx.erp.presenter.RetailTradeCouponPresenter;
//import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
//import com.bx.erp.presenter.RetailTradePromotingPresenter;
//import com.bx.erp.utils.GeneralUtil;
//import com.bx.erp.utils.NetworkUtils;
//import com.bx.erp.utils.StringUtils;
//import com.bx.erp.utils.WXPayUtil;
//import com.bx.erp.view.presentation.CustomerCommodityListPresentation;
//import com.bx.erp.view.presentation.PaymentSuccessPresentation;
//import com.bx.erp.view.presentation.WelcomePresentation;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;
//
//import org.apache.log4j.Logger;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_InvalidSession;
//import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_NoError;
//import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_OtherError;
//import static com.bx.erp.model.ErrorInfo.EnumErrorCode.EC_WechatServerError;
//import static com.bx.erp.utils.GeneralUtil.sub;
//
///**
// * 不管哪种支付方式，只要修改了“支付金额”，“未付余额”和“找零”都会相应改变，当输入的为负数或者0时有提示，
// */
//public class PaymentActivity extends BaseActivity implements View.OnClickListener {
//    private Logger log = Logger.getLogger(this.getClass());
//
//    @BindView(R.id.payment_back)
//    TextView paymentBack;
//    /**
//     * 总价
//     */
//    @BindView(R.id.total_money)
//    TextView totalMoney;
//    @BindView(R.id.cash_payment)
//    TextView cashPayment;
//    @BindView(R.id.cash_payment_money)
//    TextView cashPaymentMoney;
//    @BindView(R.id.wechat_payment)
//    TextView wechatPayment;
//    @BindView(R.id.wechat_payment_money)
//    TextView wechatPaymentMoney;
//    /**
//     * 未付余额
//     */
//    @BindView(R.id.unpaid_balance)
//    TextView unpaidBalance;
//    @BindView(R.id.change_money)
//    TextView changeMoney;
//    @BindView(R.id.choose_payment_method)
//    RadioGroup choosePaymentType;
//    @BindView(R.id.wechat_pay)
//    RadioButton wechatPay;
//    @BindView(R.id.cash_pay)
//    RadioButton cashPay;
//    @BindView(R.id.wechat_paying)
//    TextView tvWechatPaying;
//    @BindView(R.id.cash_paying)
//    TextView tvCashPaying;
//    /**
//     * 当前微信支付金额的文本框
//     */
//    @BindView(R.id.wechat_paying_amount)
//    EditText etWechatPayingAmount;
//    /**
//     * 当前现金支付金额的文本框
//     */
//    @BindView(R.id.cash_paying_amount)
//    EditText etCashPayingAmount;
//    @BindView(R.id.pay)
//    TextView tvPay;
//    @BindView(R.id.cancel_pay)
//    TextView cancelPay;
//    @BindView(R.id.keyboard_layout)
//    TableLayout keyboardLayout;
//    @BindView(R.id.number_0_pay)
//    TextView number0;
//    @BindView(R.id.number_1_pay)
//    TextView number1;
//    @BindView(R.id.number_2_pay)
//    TextView number2;
//    @BindView(R.id.number_3_pay)
//    TextView number3;
//    @BindView(R.id.number_4_pay)
//    TextView number4;
//    @BindView(R.id.number_5_pay)
//    TextView number5;
//    @BindView(R.id.number_6_pay)
//    TextView number6;
//    @BindView(R.id.number_7_pay)
//    TextView number7;
//    @BindView(R.id.number_8_pay)
//    TextView number8;
//    @BindView(R.id.number_9_pay)
//    TextView number9;
//    @BindView(R.id.number_point_pay)
//    TextView numberPoint;
//    @BindView(R.id.number_delete_pay)
//    ImageView numberDelete;
//
//    private String recordkeyNumbers = "";//当付款的时候，点击按键，付款金额中清空，然后再填写实际付款的金额（比如应该付款金额为50，虚拟键盘已经出来了，当点击按键想要更改金额的时候，付款金额清空，被手动输入的金额取代）
//
//    private RetailTrade retailTrade = null;//创建一个新的RetailTrade，用于保存MainActivity传过来的零售单信息，还有设置支付方式，支付金额，上传零售单
//    private RetailTradePromoting retailTradePromoting = null;
//    //微信支付时,顾客有wx优惠券，那么会更新reateilTrade和RetailTradePromoting,但是如果支付失败了，那么需要将零售单和零售促销数据还原
//    private RetailTrade originalRetailTrade = null;
//    private RetailTradePromoting originalRetailTradePromoting = null;
//
//    private int paymentCode = 0b00000000;//用于表示未进行支付，在支付后，paymentType的值为paymentCode + 对应的支付方式的二进制数
//    private final int wechatPayCode = 0b00000100;//用于表示相应支付方式的二进制数
//    private final int alipayPayCode = 0b00000010;
//    private final int cashPayCode = 0b00000001;
//    private ScanGun scanGun = null;
//    private String wxPayAuthCode = ""; // 微信付款授权码
//    private int wxPayAuthCodeLength = 18;
//    //    private WXPayInfo microPayResponse;
//    private Map<String, String> microPayResponse; // 微信支付结果
//    private final static double WECHATPAYMENT_MIN_AMOUNT = 0.00; // 微信支付金额必须大于该值
//    private final static String DEFAULT_changeAmount = "0.00"; //默认付全款时，找零应为0.00
//
//    private static WxCouponHttpBO wxCouponHttpBO = null;
//    private static WxCouponHttpEvent wxCouponHttpEvent = null;
//    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
//    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
//    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
//    private static RetailTradeHttpBO retailTradeHttpBO = null;
//
//    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
//    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
//    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
//    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
//
//    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
//    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
//
//    private static RetailTradeCouponSQLiteBO retailTradeCouponSQLiteBO = null;
//    private static RetailTradeCouponPresenter retailTradeCouponPresenter = null;
//
//    private RetailTradePromotingPresenter retailTradePromotingPresenter;
//    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
//
//    private static final int WXPAYMESSAGE = 3;
//    private static final int CASHPAYMESSAGE = 4;
//
//    /**
//     * 用于记录本次结算使用微信支付了多少
//     */
//    private double wetchatAmount;//
//    /**
//     * 用于记录本次结算使用支付宝支付了多少
//     */
//    private double alipayAmount;//
//    /**
//     * 用于记录本次结算使用现金支付了多少
//     */
//    private double cashAmount;//
//    private static WXPayHttpEvent wxPayHttpEvent = null;
//    private static WXPayHttpBO wxPayHttpBO = null;
//
//    public static final String MAX_CashPaymentAmount = "99999";
//
//    /**
//     * 标识用户当前正在用现金支付，用户已经点击一次现金支付，不能再处理他后续的点击事件
//     */
//    private static boolean isPayingViaCash = false;
//    private AlertDialog cancelPayDialog;
//
//    /**
//     * 标识上一次扫码微信支付还未完成。
//     * 扫码时检查此标记，决定是否执行微信支付，防止用户疯狂扫码！
//     */
//    private boolean bLastWxPayIsNotYetDone = false;
//    private boolean bCanPayViaCashOnly = false;
//    private static final int Default_RetailTradePaymentType = 00000000;
//
//    private HandlerMessage handlerMessage = new HandlerMessage();
//    /**
//     * 子错误码：本地创建零售单失败
//     */
//    private final int SubErrorCode_RetailTrade_OtherError = 1;
//    /**
//     * 子错误码：本地创建零售单促销计算过程失败
//     */
//    private final int SubErrorCode_RetailTradePromoting_OtherError = 2;
//    /**
//     * 子错误码：本地创建零售单促销计算过程失败
//     */
//    private final int SubErrorCode_RetailTradeCoupon_OtherError = 3;
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    private GoogleApiClient client;
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment);
//
//        initPresentationData(getApplicationContext());
//
//        ButterKnife.bind(this);
//
//        double totalMoney = 0.000000d;
//        for (int i = 0; i < BaseActivity.showCommList.size(); i++) {
//            totalMoney += Double.valueOf(BaseActivity.showCommList.get(i).getSubtotal());
//        }
//        showTextInT1Host("应付：", "￥" + GeneralUtil.formatToShow(totalMoney));
//
//        //接收MainActivity传递的零售单信息,且设置支付方式为未知。结算界面已经计算出应付款
//        retailTrade = (RetailTrade) getIntent().getSerializableExtra("RetailTradeData");
//        retailTradePromoting = (RetailTradePromoting) getIntent().getSerializableExtra("RetailTradePromoting");
//        retailTrade.setPaymentType(0);
//
//        initEventAndBO();
//
//        initWidget();
//
//        bLastWxPayIsNotYetDone = false;
//
//        // 进入结算页面时断网
//        if (GlobalController.getInstance().getSessionID() == null || (!NetworkUtils.isNetworkAvalible(this.getApplicationContext()))) { // xxx 是否需要放前面
//            changeRadioButtonToPayViaCashOnly();
//        }
//
//        //当此EditText获得焦点的时候，软键盘不弹出
//        etWechatPayingAmount.setShowSoftInputOnFocus(false);
//        //  如果不获得焦点，会导致EditText的error属性无法正常展示
////        etWechatPayingAmount.setFocusableInTouchMode(false);
//        etWechatPayingAmount.setEnabled(false);
//
//        etCashPayingAmount.setShowSoftInputOnFocus(false);
//        etCashPayingAmount.setFocusableInTouchMode(false);
//        etCashPayingAmount.setEnabled(false);
//
//        paymentBack.setOnClickListener(this);
//        tvPay.setOnClickListener(this);
//        cancelPay.setOnClickListener(this);
//        number0.setOnClickListener(this);
//        number1.setOnClickListener(this);
//        number2.setOnClickListener(this);
//        number3.setOnClickListener(this);
//        number4.setOnClickListener(this);
//        number5.setOnClickListener(this);
//        number6.setOnClickListener(this);
//        number7.setOnClickListener(this);
//        number8.setOnClickListener(this);
//        number9.setOnClickListener(this);
//        numberPoint.setOnClickListener(this);
//        numberDelete.setOnClickListener(this);
//
//        //选择支付方式 （如果进支付页面时，微信支付可用，就会进入该分支）。
//        if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
//            if (Configuration.bUseSandBox) {
//                tvPay.setBackgroundResource(R.drawable.button_background);
//                tvPay.setText("支  付");
//                tvPay.setEnabled(true);
//            } else {
//                tvPay.setBackgroundResource(R.drawable.button_background_disable);
//                tvPay.setText("支付完成");//... 不用沙箱支付却显示支付完成，是不是会引起误会？
//                tvPay.setEnabled(false);
//            }
//        }
//        // 单选按钮发生改变
//        choosePaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (wechatPay.getId() == checkedId) {
//                    changeMoney.setText(DEFAULT_changeAmount);
//                    etWechatPayingAmount.requestFocus();
//                    tvWechatPaying.setVisibility(View.VISIBLE);
//                    etWechatPayingAmount.setVisibility(View.VISIBLE);
//                    tvCashPaying.setVisibility(View.INVISIBLE);
//                    etCashPayingAmount.setVisibility(View.INVISIBLE);
//                    if (Configuration.bUseSandBox) {
//                        tvPay.setBackgroundResource(R.drawable.button_background);
//                        tvPay.setText("支  付");
//                        tvPay.setEnabled(true);
//                    } else {
//                        tvPay.setBackgroundResource(R.drawable.button_background_disable);
//                        tvPay.setText("支付完成");
//                        tvPay.setEnabled(false);
//                    }
//                    if (!StringUtils.isEmpty(etWechatPayingAmount.getText().toString())) {
//                        if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                            Toast.makeText(getApplicationContext(), "微信支付不能大于未付款金额", Toast.LENGTH_SHORT).show();
//                            etWechatPayingAmount.setText(unpaidBalance.getText().toString());
//                        }
//                    }
//                } else if (cashPay.getId() == checkedId) {
//                    etCashPayingAmount.requestFocus();
//                    tvWechatPaying.setVisibility(View.INVISIBLE);
//                    etWechatPayingAmount.setVisibility(View.INVISIBLE);
//                    tvCashPaying.setVisibility(View.VISIBLE);
//                    etCashPayingAmount.setVisibility(View.VISIBLE);
//                    if (!StringUtils.isEmpty(etCashPayingAmount.getText().toString()) && compareString(etCashPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                        changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(etCashPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()))));
//                    }
//                    tvPay.setText("支  付");
//                    tvPay.setBackgroundResource(R.drawable.button_background);
//                    tvPay.setEnabled(true);
//                }
//            }
//        });
//
//        //扫码枪扫付款码
//        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
//            @Override
//            public void onScanFinish(String scanResult) {
//                log.info("scanResult=" + scanResult);
//
//                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
//                if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
//                    if (scanResult != null && !"".equals(scanResult) && scanResult.length() == wxPayAuthCodeLength) { //目前扫码仅支持微信支付
//                        wxPayAuthCode = scanResult;
//                        Toast.makeText(getApplicationContext(), "扫码成功！", Toast.LENGTH_SHORT).show();
//                        if (wxPayAuthCode != null && !"".equals(wxPayAuthCode)) {
//                            if (!bLastWxPayIsNotYetDone && retailTrade != null) {
//                                if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || sub(Double.valueOf(etWechatPayingAmount.getText().toString()), WECHATPAYMENT_MIN_AMOUNT) < BaseModel.TOLERANCE) {
//                                    closeLoadingDialog(loadingDailog);
//                                    Toast.makeText(getApplicationContext(), "支付金额必须大于0", Toast.LENGTH_SHORT).show();
//                                } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
//                                    closeLoadingDialog(loadingDailog);
//                                    Toast.makeText(getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            payWX(Double.valueOf(getPayingMoney().getText().toString()));
//                                        }
//                                    }).start();
//                                }
//                            }
//                        }
//                    } else {
//                        closeLoadingDialog(loadingDailog);
//                        Toast.makeText(getApplicationContext(), "错误的付款码，请重新扫码！", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    closeLoadingDialog(loadingDailog);
//                    Toast.makeText(getApplicationContext(), "非微信支付，扫码无效！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        etWechatPayingAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { // 当支付金额输入框发生改变时
//                //微信支付不允许输入金额为0，但现金是允许支付0的情况
//                if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || sub(Double.valueOf(etWechatPayingAmount.getText().toString()), WECHATPAYMENT_MIN_AMOUNT) < BaseModel.TOLERANCE) {
//                    etWechatPayingAmount.requestFocus();
//                    etWechatPayingAmount.setError("支付金额必须大于0");
//                } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                    Toast.makeText(getApplicationContext(), "微信支付不能大于未付款金额", Toast.LENGTH_SHORT).show();
//                    etWechatPayingAmount.setText(unpaidBalance.getText().toString());
//                } else {
//                    etWechatPayingAmount.setError(null);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        etCashPayingAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { // 当支付金额输入框发生改变时
//                if (!StringUtils.isEmpty(etCashPayingAmount.getText().toString()) && compareString(etCashPayingAmount.getText().toString(), MAX_CashPaymentAmount) > 0) {
//                    Toast.makeText(getApplicationContext(), "现金支付的金额不能大于" + MAX_CashPaymentAmount, Toast.LENGTH_SHORT).show();
//                    etCashPayingAmount.setText(MAX_CashPaymentAmount);
//                }
//                if (!StringUtils.isEmpty(etCashPayingAmount.getText().toString()) && compareString(etCashPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                    changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(etCashPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()))));
//                } else {
//                    changeMoney.setText(DEFAULT_changeAmount);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }
//
//    private void calculateReturnPrice(RetailTrade rt, double fBestAmount) {
//        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
//        double originalTotalAmount = 0.000000d;
//        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
//            originalTotalAmount += rtc.getPriceReturn() * rtc.getNO(); //使用退货价是因为该商品可能参与了促销。使用优惠券后重新计算退货价,需要使用促销后的价格
//        }
//
//        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
//            // 原因是，按照先乘后除的计算顺序可以减少计算误差
//            rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), fBestAmount), GeneralUtil.mul(originalTotalAmount, rtc.getNO()), 6))));
//        }
//    }
//
//    private void payWX(Double payAmount) {
//        String couponCode = "";
//        //先向服务器请求查询顾客是否有该店的可核销微信优惠券。
//        List<WxCoupon> wxCouponList = queryWxCoupon(wxPayAuthCode);
//        if (wxCouponHttpBO.getHttpEvent().getLastErrorCode() == EC_InvalidSession) {
//            Message message = new Message();
//            message.what = WXPAYMESSAGE;
//            handlerMessage.setErrorCode(EC_InvalidSession);
//            message.obj = handlerMessage;
//            handler.sendMessage(message);
//            return;
//        }
//
//        double wxPayMoney = payAmount;
//        if (wxCouponList != null && wxCouponList.size() > 0) {
//            double money = wxPayMoney;
//            //计算哪个优惠券的优惠力度最大。
//            Map<String, Object> params = WXPayUtil.sell(wxCouponList, money);
//            double fBestWxAmount = (double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount());
//            double reduceAmount = GeneralUtil.sub(money, fBestWxAmount);
//            if (reduceAmount > BaseModel.TOLERANCE) {
//                //备份对象，支付失败则恢复
//                originalRetailTrade = (RetailTrade) retailTrade.clone();
//                originalRetailTradePromoting = (RetailTradePromoting) retailTradePromoting.clone();
//                //重新计算退货价需要整单最优惠的价格(使用优惠券后的微信支付金额 + 现金支付金额)
//                retailTrade.setCouponAmount(reduceAmount);
//                retailTrade.setAmount(GeneralUtil.sub(retailTrade.getAmount(), reduceAmount));
//                calculateReturnPrice(retailTrade, GeneralUtil.sum(fBestWxAmount, retailTrade.getAmountCash()));
//                //因使用优惠券也算促销，所以需要更新零售促销表
//                updateRetailTradePromoting((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon()), wxPayMoney, retailTrade);
//                //生成零售单优惠券使用表对象
//                generteRetailTradeCoupon((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon()), retailTrade);
//
//                wxPayMoney = fBestWxAmount;
//                couponCode = ((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode();
//            }
//        }
//        //进行微信支付,如果有使用优惠券，那么支付完成后还会将券进行核销
//        payViaWX(wxPayMoney, couponCode);
//    }
//
//    // 支付流程需要重构，不再是自动使用优惠券
//    private void generteRetailTradeCoupon(WxCoupon wxCoupon, RetailTrade retailTrade) {
//        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
//        retailTradeCoupon.setRetailTradeID(retailTrade.getID().intValue());
//        retailTradeCoupon.setID(retailTradeCouponPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
//
//        List<BaseModel> list = new ArrayList<BaseModel>();
//        list.add(retailTradeCoupon);
//
//        retailTrade.setListSlave3(list);
//    }
//
//
//    /**
//     * 微信支付
//     */
//    private void payViaWX(Double payAmount, String couponCode) {
//        bLastWxPayIsNotYetDone = true;
//        handlerMessage.setErrorCode(EC_NoError); // 初始化消息ErrorCode
//        //
//        Message message = new Message();
//        message.what = WXPAYMESSAGE;
//        // 微信支付
//        if (!wxPayment(payAmount, couponCode, handlerMessage)) {
//            //支付失败后如果有使用优惠券,那么需要恢复零售单和零售促销的数据
//            if (retailTrade.getCouponAmount() > BaseModel.TOLERANCE) {
//                retailTrade = (RetailTrade) originalRetailTrade.clone();
//                retailTradePromoting = (RetailTradePromoting) originalRetailTradePromoting.clone();
//            }
//            message.obj = handlerMessage;
//            handler.sendMessage(message);
//            return; // 方法中已经有提示信息，微信支付失败后，不再做其他操作。
//        }
//        log.info("==unpaidBalance.toString():" + unpaidBalance.toString() + "=====wetchatAmount:" + wetchatAmount);
//        log.info("微信支付成功！");
//
//        if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //判断是全额支付还是部分支付
//            //设置上一单的支付方式
//            int paymentType = retailTrade.getPaymentType();
//            if (paymentType == 1) {
//                BaseActivity.lastRetailTradePaymentType = pay_cash;
//            } else if (paymentType == 4) {
//                BaseActivity.lastRetailTradePaymentType = pay_wetchat;
//            } else {
//                BaseActivity.lastRetailTradePaymentType = pay_combination;
//            }
//            //设置上一单总金额
//            BaseActivity.lastRetailTradeAmount = retailTrade.getAmount();
//            log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + retailTrade.getAmountCash());
//            if (!createRetailTradeLocally(handlerMessage)) {
//
//            } else {
//                updateRetailTradeAggregation();
//            }
//            retailTrade = null;
//            System.out.println("零售单已结清，返回主页面");
//        }
//        message.obj = handlerMessage;
//        handler.sendMessage(message);
//    }
//
//    /**
//     * 现金支付
//     */
//    private void payViaCash(Double payAmount) {
//        isPayingViaCash = true;
//        handlerMessage.setErrorCode(EC_NoError);// 初始化消息ErrorCode
//        //
//        paymentCode = paymentCode | cashPayCode;
//        retailTrade.setPaymentType(paymentCode);
//        cashAmount = GeneralUtil.sum(Double.valueOf(cashPaymentMoney.getText().toString()), payAmount);
//        retailTrade.setAmountCash(cashAmount); // 设置支付金额，如果有找零的情况，会在下面进行处理
//        //计算出支付金额比未付余额大出多少
//        Double returnMoney = GeneralUtil.sub(Double.valueOf(getPayingMoney().getText().toString()), Double.valueOf(unpaidBalance.getText().toString()));
//        if (returnMoney > BaseModel.TOLERANCE) {
//            handlerMessage.setMsg("支付成功,请使用现金退还" + GeneralUtil.formatToShow(returnMoney));
//        } else {
//            handlerMessage.setMsg("支付成功");
//        }
//        if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //判断是全额支付还是部分支付
//            //设置上一单支付方式
//            int paymentType = retailTrade.getPaymentType();
//            if (paymentType == 1) {
//                BaseActivity.lastRetailTradePaymentType = pay_cash;
//            } else if (paymentType == 4) {
//                BaseActivity.lastRetailTradePaymentType = pay_wetchat;
//            } else {
//                BaseActivity.lastRetailTradePaymentType = pay_combination;
//            }
//            //设置上一单找零金额
//            BaseActivity.lastRetailTradeChangeMoney = sub(GeneralUtil.round(getPayingMoney().getText().toString(), 6), GeneralUtil.round(unpaidBalance.getText().toString(), 6));
//            //设置上一单总金额
//            BaseActivity.lastRetailTradeAmount = retailTrade.getAmount();
//            // 现金支付，存在支付金额大于零售单总金额的情况，这个时候需要减去找零金额，当不需要找零时，BaseActivity.lastRetailTradeChangeMoney应该为0
//            retailTrade.setAmountCash(sub(cashAmount, BaseActivity.lastRetailTradeChangeMoney));
//            log.info("===lastRetailTradeAmount:" + BaseActivity.lastRetailTradeAmount + "===AmountCash:" + retailTrade.getAmountCash());
//            if (!createRetailTradeLocally(handlerMessage)) { //TODO
//            }
//            updateRetailTradeAggregation();
//        }
//        Message message = new Message();
//        message.what = CASHPAYMESSAGE;
//        message.obj = handlerMessage;
//        handler.sendMessage(message);
//    }
//
//    /**
//     * 全部支付后，在本地创建该零售单
//     */
//    private boolean createRetailTradeLocally(HandlerMessage hm) {
//        log.info("要创建的零售单为：" + retailTrade);
//        //如果不是会员消费，先存在本地(先在本地插入RetailTrade，得到临时的TradeID，设置到retailTradePromoting的TradeID中，然后在本地插入retailTradePromoting，得到的临时ID设置到RetailTrade的int2，vip也一样)，等到第一张单的c分钟后，查找本地临时零售单上传到服务器
//        RetailTrade rtCreate = (RetailTrade) retailTradeSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, retailTrade);
//        if (rtCreate == null) {
//            log.error("在本地插入临时零售单失败！");
//            hm.setErrorCode(EC_OtherError);
//            hm.setSubErrorCode(SubErrorCode_RetailTrade_OtherError);
//            return false;
//        }
//
//        if (retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradeSQLiteEvent.getLastErrorCode() != null) {
//            log.error("在本地插入临时零售单失败：" + retailTradeSQLiteEvent.getLastErrorMessage());
//            hm.setErrorCode(retailTradeSQLiteEvent.getLastErrorCode());
//            hm.setMsg(retailTradeSQLiteEvent.getLastErrorMessage());
//            return false;
//        }
//        // 判断是否需要上传计算过程
//        if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
//            RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting);
//            if (rtpCreate == null) {
//                log.error("在本地插入临时计算过程失败！");
//                hm.setErrorCode(EC_OtherError);
//                hm.setSubErrorCode(SubErrorCode_RetailTradePromoting_OtherError);
//                return false;
//            }
//            //同步操作并不会使用到SQLiteEvent。
//            if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradePromotingSQLiteEvent.getLastErrorCode() != null) {
//                log.error("在本地插入临时计算过程失败：" + retailTradePromotingSQLiteEvent.getLastErrorMessage());
//                hm.setErrorCode(retailTradePromotingSQLiteEvent.getLastErrorCode());
//                hm.setMsg(retailTradePromotingSQLiteEvent.getLastErrorMessage());
//                return false;
//            }
//        }
//        //判断是否需要创建零售单优惠券表
//        if (retailTrade.getListSlave3() != null) {
//            List<BaseModel> retailTradeCouponList = (List<BaseModel>) retailTrade.getListSlave3();
//            RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradeCoupon_CreateSync, retailTradeCouponList.get(0));
//            if (retailTradeCouponCreate == null){
//                log.error("在本地插入临时零售单优惠券使用表失败！");
//                hm.setErrorCode(EC_OtherError);
//                hm.setSubErrorCode(SubErrorCode_RetailTradeCoupon_OtherError);
//                hm.setMsg("插入临时零售单优惠券使用表失败！"); //
//                return false;
//            }
//        }
//
//        // 保存最后一张非退货型零售单的数据，以便首页能打印上一单
//        lastRetailTrade = retailTrade;
//        return true;
//    }
//
//    private void updateRetailTradeAggregation() {
//        log.info("本地零售单创建成功后,开始更新收银汇总的所有数据");
//
//        // 更新收银汇的所有数据:
//        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
//        //未发生变化的数据
//        retailTradeAggregation.setID(BaseActivity.retailTradeAggregation.getID());
//        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
//        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
//        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
//        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
//        //发生变化的数据：
//        retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), retailTrade.getAmountCash()));/*现金收入*/
//        retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));/*微信收入*/
//        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
//        retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);/*销售单数*/
//        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
//        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前零售单的生成时间*/
//        //
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
//        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
//        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
//            log.error("准备更新收银汇总失败！");
//        }
//        long lTimeOut = TIME_OUT;
//        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && //
//                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
//            log.error("Update超时！retailTradeAggregationSQLiteEvent的状态为：" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
//        }
//        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.error("Update错误码不正确！" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo());
//        } else {
//            // 累计交班需要的数据(零售单创建成功后才做)
//            BaseActivity.retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), retailTrade.getAmountCash()));
//            BaseActivity.retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getWechatAmount(), wetchatAmount));
//            BaseActivity.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseActivity.retailTradeAggregation.getCashAmount(), BaseActivity.retailTradeAggregation.getWechatAmount()));
//            BaseActivity.retailTradeAggregation.setTradeNO(BaseActivity.retailTradeAggregation.getTradeNO() + 1);//本地零售单创建成功后BaseActivity.retailTradeAggregation的TradeNO+1
//            BaseActivity.retailTradeAggregation.setWorkTimeEnd(date);
//        }
//    }
//
//    private boolean wxPayment(Double payAmount, String couponCode, HandlerMessage hm) {
//        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(payAmount);
//        System.out.println("-------------------------------------------------开始微信支付,支付金额：" + payAmount);
//        WXPayInfo wxPayInfo = new WXPayInfo();
//        wxPayInfo.setAuth_code(wxPayAuthCode);
//        wxPayInfo.setTotal_fee(dPaymentMoney);
//        wxPayInfo.setCouponCode(couponCode);
//        //
//        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        if (!wxPayHttpBO.microPayAsync(wxPayInfo)) {
//            switch (wxPayHttpEvent.getLastErrorCode()) {
//                case EC_InvalidSession:
//                    hm.setErrorCode(EC_InvalidSession);
//                    break;
//                case EC_OtherError:
//                    hm.setErrorCode(EC_WechatServerError);
//                    break;
//            }
//            return false;
//        }
//        long lTimeOut = WxPay_TIME_OUT;
//        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("微信支付超时....");
//            hm.setErrorCode(ErrorInfo.EnumErrorCode.EC_Timeout);
//            return false;
//        }
//        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.info("微信支付失败....");
//            hm.setErrorCode(EC_WechatServerError);
//            hm.setMsg(wxPayHttpEvent.getLastErrorMessage());
//            return false;
//        }
//        paymentCode = paymentCode | wechatPayCode;
//        retailTrade.setPaymentType(paymentCode);
//        //
//        wetchatAmount = GeneralUtil.sum(Double.valueOf(wechatPaymentMoney.getText().toString()), payAmount);
//        retailTrade.setAmountWeChat(wetchatAmount);
//        //
//        microPayResponse = wxPayHttpEvent.getMicroPayResponse();
//        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id") == null ? "" : microPayResponse.get("transaction_id"));
//        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no") == null ? "" : microPayResponse.get("out_trade_no"));
//        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? "" : microPayResponse.get("sub_mch_id"));
//        retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc") == null ? "" : microPayResponse.get("refund_desc"));
//
//        return true;
//    }
//
//    private void initEventAndBO() {
//        if (retailTradeAggregationSQLiteEvent == null) {
//            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
//            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainActivity);
//        }
//        if (retailTradeAggregationSQLiteBO == null) {
//            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, null);
//            retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
//        }
//        if (retailTradeSQLiteEvent == null) {
//            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
//            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (retailTradeHttpEvent == null) {
//            retailTradeHttpEvent = new RetailTradeHttpEvent();
//            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (retailTradeHttpBO == null) {
//            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        if (retailTradeSQLiteBO == null) {
//            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
//        }
//        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
//        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
//        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
//        //
//        if (retailTradePromotingSQLiteEvent == null) {
//            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
//            retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (retailTradePromotingHttpEvent == null) {
//            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
//            retailTradePromotingHttpEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (retailTradePromotingHttpBO == null) {
//            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
//        }
//        if (retailTradePromotingSQLiteBO == null) {
//            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
//        }
//        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
//        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
//        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
//        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
//        //
//        if (wxPayHttpEvent == null) {
//            wxPayHttpEvent = new WXPayHttpEvent();
//            wxPayHttpEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (wxPayHttpBO == null) {
//            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
//        }
//        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
//        //
//        if (wxCouponHttpEvent == null) {
//            wxCouponHttpEvent = new WxCouponHttpEvent();
//            wxCouponHttpEvent.setId(BaseEvent.EVENT_ID_PaymentActivity);
//        }
//        if (wxCouponHttpBO == null) {
//            wxCouponHttpBO = new WxCouponHttpBO(GlobalController.getInstance().getContext(), null, wxCouponHttpEvent);
//        }
//        wxCouponHttpEvent.setHttpBO(wxCouponHttpBO);
//
//        if (retailTradePromotingPresenter == null) {
//            retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
//        }
//        if (retailTradePromotingFlowPresenter == null) {
//            retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
//        }
//
//        if (retailTradeCouponSQLiteBO == null) {
//            retailTradeCouponSQLiteBO = new RetailTradeCouponSQLiteBO(GlobalController.getInstance().getContext(), null, null);
//        }
//        retailTradeCouponPresenter = GlobalController.getInstance().getRetailTradeCouponPresenter();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWxCouponHttpEvent(RetailTradeHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("PayMentActivity.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，此处PayMentActivity.onRetailTradeHttpEvent()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            log.info("#########################################################PaymentActivity onRetailTradeSQLiteEvent");
////            event.onEvent();
////            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RefreshMasterSlaveAsyncSQLite_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
////                //当第一次启动pos的时候，该值为0，到后来都是要使用批量上传完成的时间，这个是时间间隔10分钟的起点，会在10分钟上传之后重置为0(非VIP)
//////                if (SyncThread.lastCreateNRetailtradeTime == 0) {
//////                    SyncThread.lastCreateNRetailtradeTime = new Date().getTime();
//////                }
////                //将RetailTradePromoting插入本地SQLite作为临时数据(非VIP)
////                if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
////                    retailTradePromotingSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done);
////                    if (!retailTradePromotingSQLiteBO.createAsync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting)) { //... 应该call sync()接口。call async()接口，很难保证不出问题，虽然多数情况下不会出现问题
////                        log.info("在本地插入RetailTradePromoting临时数据失败！");
////                    }
////                } else {
////                    //同步线程立即上传零售单
////                    SyncThread.executeInstantly(true);
////
////                    Message message = new Message();
////                    message.what = 1;
////                    handler.sendMessage(message);
////                }
////            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("PayMentActivity.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，此处PayMentActivity.onRetailTradeSQLiteEvent()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradePromotingHttpEvent(RetailTradePromotingHttpEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("PaymentActivity.onRetailTradePromotingHttpEvent()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，PaymentActivity.onRetailTradePromotingHttpEvent()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRetailTradePromotingSQLiteEvent(RetailTradePromotingSQLiteEvent event) {
//        log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&        PaymentActivity  onRetailTradePromotingSQLiteEvent");
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
////            event.onEvent();
////
////            if ((event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_RefreshMasterSlaveAsyncSQLite_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) ||
////                    (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradePromoting_CreateReplacerAsync_Done && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData)) {
////                //同步线程立即上传零售单及其计算过程
////                SyncThread.executeInstantly(true);
////
////                Message message = new Message();
////                message.what = 1;
////                handler.sendMessage(message);
////            }
//        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
//            if (!event.isEventProcessed()) {
//                log.error("PaymentActivity.onRetailTradePromotingSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
//            }
//            log.debug("该Event已经在SyncThread中处理，PaymentActivity.onRetailTradePromotingSQLiteEvent()不用处理");
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onWxCouponHttpEvent(WxCouponHttpEvent event) {
//        log.info("///////////////////////////////////////////onWxCouponHttpEvent");
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.ASYNC)
//    public void onWXPayHttpEvent(WXPayHttpEvent event) {
//        log.info("///////////////////////////////////////////onWXPayHttpE1vent1");
//        if (event.getId() == BaseEvent.EVENT_ID_PaymentActivity) {
//            event.onEvent();
////            microPayResponse = event.getMicroPayResponse();
////            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_WXPay_MicroPay) {
////                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
////                    log.info("WX支付成功");
////                } else if (event.getLastErrorCode() == EC_InvalidSession) { // 断网情况
////                    log.info("isPayingViaCash:" + isPayingViaCash);
//////                    Toast.makeText(getApplicationContext(), "网络错误！！！", Toast.LENGTH_SHORT).show();
////                    handlerMessage.setErrorCode(EC_InvalidSession);
////                    Message message = new Message();
////                    message.what = 3;
////                    message.obj = handlerMessage;
////                    handler.sendMessage(message);
////                } else {
////                    log.info("非网络故障导致的WX支付错误");
////                    Message message = new Message();
////                    message.what = 2;
////                    handler.sendMessage(message);
////                }
////            }
//        } else {
//            StackTraceElement ste = new Exception().getStackTrace()[1];
//            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.payment_back:
//                AlertDialog.Builder cancelTheSettlement = new AlertDialog.Builder(this)
//                        .setTitle("提示")
//                        .setMessage("是否确定取消结算,这样会取消本次支付！")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                cancelPayment();
//                                Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
//                                PaymentActivity.this.finish();
//                                startActivity(intent2);
//                            }
//                        });
//                cancelPayDialog = cancelTheSettlement.create();
//                cancelPayDialog.show();
//                break;
//            case R.id.pay:
//                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_Paying);
//                if (!isPayingViaCash && retailTrade != null) {
//                    if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) { // 沙箱环境WX支付
//                        if (StringUtils.isEmpty(etWechatPayingAmount.getText().toString()) || Double.valueOf(etWechatPayingAmount.getText().toString()) < 0) {
//                            Toast.makeText(getApplicationContext(), "支付金额必须大于或等于0", Toast.LENGTH_SHORT).show();
//                            closeLoadingDialog(loadingDailog);
//                        } else if ((etWechatPayingAmount.getText().toString().length() - etWechatPayingAmount.getText().toString().indexOf(".")) == 1) {
//                            Toast.makeText(getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
//                            closeLoadingDialog(loadingDailog);
//                        } else {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    payWX(Double.valueOf(getPayingMoney().getText().toString()));
//                                }
//                            }).start();
//                        }
//                    } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) { // 现金支付
//                        if (StringUtils.isEmpty(etCashPayingAmount.getText().toString()) || Double.valueOf(etCashPayingAmount.getText().toString()) < 0) {
//                            Toast.makeText(getApplicationContext(), "支付金额必须大于或等于0", Toast.LENGTH_SHORT).show();
//                            closeLoadingDialog(loadingDailog);
//                        } else if ((etCashPayingAmount.getText().toString().length() - etCashPayingAmount.getText().toString().indexOf(".")) == 1) {
//                            Toast.makeText(getApplicationContext(), "输入金额无效", Toast.LENGTH_SHORT).show();
//                            closeLoadingDialog(loadingDailog);
//                        } else {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    payViaCash(Double.valueOf(getPayingMoney().getText().toString()));
//                                }
//                            }).start();
//                        }
//                    }
//                }
//                break;
//            case R.id.cancel_pay:
//                AlertDialog.Builder cancelPayBuilder = new AlertDialog.Builder(this)
//                        .setTitle("提示")
//                        .setMessage("是否确定取消支付！")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                cancelPayment();
//
//                                Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
//                                PaymentActivity.this.finish();
//                                startActivity(intent2);
//                            }
//                        });
//                cancelPayDialog = cancelPayBuilder.create();
//                cancelPayDialog.show();
//                break;
//            case R.id.number_0_pay:
//                if (!getPayingMoney().getText().toString().equals("0")) {
//                    clickKeyboardNumber("0", getPayingMoney());
//                } else {
//                    getPayingMoney().setText(unpaidBalance.getText().toString());
////                    recordkeyNumbers = etWechatPayingAmount.getText().toString();
//                    changeMoney.setText(DEFAULT_changeAmount);
//                    Toast.makeText(this, "输入金额无效", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case R.id.number_1_pay:
//                clickKeyboardNumber("1", getPayingMoney());
//                break;
//            case R.id.number_2_pay:
//                clickKeyboardNumber("2", getPayingMoney());
//                break;
//            case R.id.number_3_pay:
//                clickKeyboardNumber("3", getPayingMoney());
//                break;
//            case R.id.number_4_pay:
//                clickKeyboardNumber("4", getPayingMoney());
//                break;
//            case R.id.number_5_pay:
//                clickKeyboardNumber("5", getPayingMoney());
//                break;
//            case R.id.number_6_pay:
//                clickKeyboardNumber("6", getPayingMoney());
//                break;
//            case R.id.number_7_pay:
//                clickKeyboardNumber("7", getPayingMoney());
//                break;
//            case R.id.number_8_pay:
//                clickKeyboardNumber("8", getPayingMoney());
//                break;
//            case R.id.number_9_pay:
//                clickKeyboardNumber("9", getPayingMoney());
//                break;
//            case R.id.number_point_pay:
//                if (getPayingMoney().getText().toString().indexOf(numberPoint.getText().toString()) == -1) {
//                    if ("".equals(getPayingMoney().getText().toString()) || null == getPayingMoney().getText().toString()) {
//                        getPayingMoney().setText("0.");
////                        recordkeyNumbers = "0.";
//                    } else {
//                        clickKeyboardNumber(".", getPayingMoney());
//                    }
//                } else {
//                    getPayingMoney().setText(unpaidBalance.getText().toString());
////                    recordkeyNumbers = etWechatPayingAmount.getText().toString();
//                    changeMoney.setText(DEFAULT_changeAmount);
//                    Toast.makeText(this, "输入金额无效", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.number_delete_pay:
////                recordkeyNumbers = etWechatPayingAmount.getText().toString();
//                if ("".equals(getPayingMoney().getText().toString()) || null == getPayingMoney().getText().toString()) {
//                    return;
//                } else {
//                    getPayingMoney().setText(getPayingMoney().getText().toString().substring(0, getPayingMoney().getText().length() - 1));
////                    recordkeyNumbers = recordkeyNumbers.substring(0, recordkeyNumbers.length() - 1);
//                }
//                getPayingMoney().setSelection(getPayingMoney().getText().length());
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    /**
//     * 微信支付使用优惠券后需要更新零售单促销过程
//     *
//     * @param wxCoupon
//     */
//    private void updateRetailTradePromoting(WxCoupon wxCoupon, double payAmount, RetailTrade retailTrade) {
//        try {
//            long tmpRetailTradePromotingFlowIDInSQLite;
//            if (retailTradePromoting.getListSlave1().size() == 0) {
//                long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
//                retailTradePromoting.setTradeID(retailTrade.getID().intValue());
//                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());
//
//                tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//            } else {
//                //从表有数据则说明参与了促销，需要在后面继续累加
//                tmpRetailTradePromotingFlowIDInSQLite = ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(retailTradePromoting.getListSlave1().size() - 1)).getID() + 1;
//            }
//            //
//            StringBuilder promotingFlow = new StringBuilder();
//            promotingFlow.append("此单微信支付原价为:" + GeneralUtil.formatToCalculate(payAmount) + "元 \t")//
//                    .append("优惠券标题为:‘" + wxCoupon.getWxCouponDetail().getWxCouponDetailPartition().getBase_info().getTitle() + "’\t"); //
//            WxCouponDetail wxCouponDetail = wxCoupon.getWxCouponDetail();
//            WxCouponDetailPartition wxCouponDetailPartition = wxCouponDetail.getWxCouponDetailPartition();
//            double fen = 100.000000d; //微信存储优惠金额都是以分为单位
//            if (WxCouponDetail.EnumCouponType.ECT_Cash.getName().equals(wxCouponDetail.getCard_type())) {
//                promotingFlow.append("微信支付金额满" + GeneralUtil.div(wxCouponDetailPartition.getLeast_cost(), fen, 6) + "元减" + GeneralUtil.div(wxCouponDetailPartition.getReduce_cost(), fen, 6) + "\t")//
//                        .append("实际微信支付:" + GeneralUtil.sub(payAmount, GeneralUtil.div(wxCouponDetailPartition.getReduce_cost(), fen, 6)));
//            } else {
//                promotingFlow.append("全场进行" + ((100 - wxCouponDetailPartition.getDiscount()) / 10) + "折优惠") //
//                        .append("实际微信支付:" + GeneralUtil.sub(payAmount, GeneralUtil.mul(payAmount, GeneralUtil.div(wxCouponDetailPartition.getDiscount(), fen, 6))));
//            }
//            //
//            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
//            retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromoting.getID().intValue());
//            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
//            retailTradePromotingFlow.setID(tmpRetailTradePromotingFlowIDInSQLite);
//            ((List<RetailTradePromotingFlow>) retailTradePromoting.getListSlave1()).add(retailTradePromotingFlow);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<WxCoupon> queryWxCoupon(String wxPayAuthCode) {
//        do {
//            WxCoupon wxCoupon = new WxCoupon();
//            wxCoupon.setAuthCode(wxPayAuthCode);
//            wxCoupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
//            wxCoupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
//            //
//            wxCouponHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//            if (!wxCouponHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, wxCoupon)) {
//                log.error("请求服务器获取可核销优惠券失败！");
//                break;
//            }
//
//            long timeout = TIME_OUT;
//            while (wxCouponHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && (timeout-- > 0)) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (wxCouponHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//                log.info("超时！");
//                break;
//            }
//            if (wxCouponHttpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                log.info("获取可核销优惠券失败");
//                break;
//            }
//
//            return (List<WxCoupon>) wxCouponHttpBO.getHttpEvent().getListMasterTable();
//        } while (false);
//        return null;
//    }
//
//    /**
//     * 取消支付
//     */
//
//    private void cancelPayment() {
//        if (Double.parseDouble(totalMoney.getText().toString()) - Double.parseDouble(unpaidBalance.getText().toString()) > RetailTrade.TOLERANCE) { //已经支付了部分钱
//            // 如果存在现金支付，则使用现金退款（存在混合支付和全现金支付的情况） 暂无支付宝的情况
//            if (!NetworkUtils.isNetworkAvalible(this.getApplicationContext()) || cashAmount - 0.000000d > RetailTrade.TOLERANCE) {
//                Toast.makeText(getApplicationContext(), "请使用现金退还" + GeneralUtil.formatToShow(GeneralUtil.sum(cashAmount, wetchatAmount)) + "元!", Toast.LENGTH_LONG).show();
//            } else if (wetchatAmount - 0.000000d > RetailTrade.TOLERANCE) {
//                // 微信支付，优先使用微信退款，微信退款失败，则使用现金退款
//                if (wxRefund()) {
//                    Toast.makeText(getApplicationContext(), "微信退款成功", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "微信退款失败，请使用现金退还" + wetchatAmount + "元!", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                //有网、现金支付是0、微信支付是0
//                throw new RuntimeException("未处理的分支！");
//            }
//            //
//        } else {
//            Toast.makeText(getApplicationContext(), "还没有进行支付金额，无需退款！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //微信退款
//    private boolean wxRefund() {
//        RetailTrade retailTrade = new RetailTrade();
//        retailTrade.setAmount(WXPayUtil.formatAmount(wetchatAmount));
//        retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id"));
//        retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
//        retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc"));
//        retailTrade.setWxOrderSN(microPayResponse.get("transaction_id"));
//        log.info("准备发起微信退款的对象为：" + retailTrade);
//        //调用退款接口
//        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//        wxPayHttpBO.refundAsync(retailTrade);
//        long lTimeOut = TIME_OUT;
//        while (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        //
//        if (wxPayHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            log.info("微信退款超时....");
//            return false;
//        }
//        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            log.info("微信退款失败....");
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        log.info("---------------------------------onKeyDown:    KeyCode:\" + keyCode + \"------event:\" + event + \"------------------------------------");
//        scanGun.isMaybeScanning(keyCode, event);
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        //拦截物理键盘事件
//        log.info("---------------------------------dispatchKeyEvent:  KeyCode:" + event.getKeyCode() + "---Action:" + event.getAction() + "------------------------------------");
//        if (event.getKeyCode() > 6) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {//将键盘的删除键传递下去
//                    return super.dispatchKeyEvent(event);
//                }
//                this.onKeyDown(event.getKeyCode(), event);
//            }
//        } else {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//将键盘的返回事件传递下去
//                return super.dispatchKeyEvent(event);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 初始化部分控件
//     */
//    private void initWidget() {
//        totalMoney.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//        unpaidBalance.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
//        etWechatPayingAmount.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
////        etWechatPayingAmount.setSelection(etWechatPayingAmount.getText().length());
//        etCashPayingAmount.setText(GeneralUtil.formatToShow(retailTrade.getAmount()));
////        etCashPayingAmount.setSelection(etCashPayingAmount.getText().length());
//    }
//
//    /**
//     * 在付款的时候点击数字，显示在付款金额
//     *
//     * @param number 点击的数字
//     */
//    private void clickKeyboardNumber(String number, EditText et) {
//        //当金额为0并且输入非0非小数点时，直接用新输入的数字代替0
//        if ("0".equals(et.getText().toString()) && !".".equals(number)) {
//            et.setText(number);
//        } else {
//            //限制只能输两位小数
//            if ((et.getText().toString().indexOf(".") > -1
//                    && (et.getText().toString().length() - et.getText().toString().indexOf(".")) == 3)) {
//                et.setText(unpaidBalance.getText().toString());
////                recordkeyNumbers = etWechatPayingAmount.getText().toString();
//                changeMoney.setText(DEFAULT_changeAmount);
////                et.setSelection(et.getText().length());
//                Toast.makeText(this, "收款金额仅支持两位小数", Toast.LENGTH_SHORT).show();
//            }
//            if (et.getText().toString().indexOf(".") == -1
//                    || (et.getText().toString().indexOf(".") > -1
//                    && (et.getText().length() - et.getText().toString().indexOf(".")) < 3)) {
////                etWechatPayingAmount.setText("");
////                recordkeyNumbers += number;
//                et.setText(et.getText().toString() + number);
////                et.setSelection(et.getText().length());
//            }
//        }
//    }
//
//    /**
//     * String类型转double类型进行比较
//     * 如果返回负数说明d1<d2
//     * 返回正数说明d1>d2
//     * 返回0说明相等
//     *
//     * @param s1
//     * @param s2
//     * @return
//     */
//    private int compareString(Object s1, Object s2) {
//        BigDecimal d1 = new BigDecimal(Double.valueOf(s1.toString()));
//        BigDecimal d2 = new BigDecimal(Double.valueOf(s2.toString()));
//        return d1.compareTo(d2);
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    // 由于创建零售单和创建零售单促销过程都使用同步接口，不再会post Event 到Activity，所以也不会发送该消息
////                    Intent mainActivityIntent = new Intent(PaymentActivity.this, MainActivity.class);
////                    mainActivityIntent.putExtra("isShow", 2);
////                    startActivity(mainActivityIntent);
////                    finish();
//                    break;
//                case 2:
//                    closeLoadingDialog(loadingDailog);
//                    unpaidBalance.setText(unpaidBalance.getText().toString());
//                    getPayingMoney().setText(unpaidBalance.getText().toString());
//                    changeMoney.setText(changeMoney.getText().toString());
//                    Toast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT).show();
//                    break;
//                case WXPAYMESSAGE: // 微信支付相关UI操作
//                    HandlerMessage mth = (HandlerMessage) msg.obj;
//                    switch (mth.getErrorCode()) {
//                        case EC_NoError:
//                            Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
//                            //
//                            wechatPaymentMoney.setText(GeneralUtil.formatToShow(wetchatAmount)); // 设置微信支付金额
//                            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) == 0) { //判断是全额支付还是部分支付
//                                tvPay.setText("支付完成");
//                                tvPay.setEnabled(false);
//                                tvPay.setBackgroundResource(R.drawable.button_background_disable);
//                                //打印小票
//                                try {
//                                    printSmallSheet(lastRetailTrade);
//                                } catch (Exception e) {
//                                    log.info("打印小票异常：" + e.getMessage());
//                                }
//                                // 支付完成，跳转页面
//                                Intent mainActivityIntent = new Intent(PaymentActivity.this, MainActivity.class);
//                                mainActivityIntent.putExtra("isPaid", 1);
//                                PaymentActivity.this.finish();
//                                startActivity(mainActivityIntent);
//                            } else if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) < 0) {
//                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(Double.valueOf(unpaidBalance.getText().toString()), Double.valueOf(etWechatPayingAmount.getText().toString()))));
//                                getPayingMoney().setText(unpaidBalance.getText().toString());
//                                etCashPayingAmount.setText(unpaidBalance.getText()); // 微信部分支付后将未付余额赋值给现金支付，当前系统中微信支付只能一次。
//                            }
//                            changeRadioButtonToPayViaCashOnly();
//                            break;
//                        case EC_InvalidSession:
//                            Toast.makeText(getApplicationContext(), BaseHttpBO.ERROR_MSG_Network, Toast.LENGTH_SHORT).show();
//                            changeRadioButtonToPayViaCashOnly();
//                            break;
//                        case EC_WechatServerError:
//                            Toast.makeText(getApplicationContext(), "微信支付失败，"+ (mth.getMsg() == null ? "" : mth.getMsg()) +"请稍后重试或者换其他的支付方式。", Toast.LENGTH_SHORT).show();
//                            break;
//                        case EC_OtherError:
//                            switch (mth.getSubErrorCode()) {
//                                case SubErrorCode_RetailTrade_OtherError:
//                                    Toast.makeText(getApplicationContext(), "创建零售单失败！", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case SubErrorCode_RetailTradePromoting_OtherError:
//                                    Toast.makeText(getApplicationContext(), "创建促销计算过程失败！", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                            break;
//                        case EC_Timeout:
//                            Toast.makeText(getApplicationContext(), "微信支付超时!", Toast.LENGTH_SHORT).show();
//                            changeRadioButtonToPayViaCashOnly();
//                            break;
//                    }
//                    bLastWxPayIsNotYetDone = false;
//                    closeLoadingDialog(loadingDailog);
//                    break;
//                case CASHPAYMESSAGE: // 现金支付相关UI操作
//                    HandlerMessage mthCash = (HandlerMessage) msg.obj;
//                    switch (mthCash.getErrorCode()) {
//                        case EC_NoError:
//                            Toast.makeText(getApplicationContext(), mthCash.getMsg(), Toast.LENGTH_SHORT).show();
//                            //
//                            cashPaymentMoney.setText(GeneralUtil.formatToShow(cashAmount));//显示在UI时保留两位小数
//                            if (compareString(getPayingMoney().getText().toString(), unpaidBalance.getText().toString()) >= 0) { //判断是全额支付还是部分支付
//                                tvPay.setText("支付完成");
//                                tvPay.setEnabled(false);
//                                tvPay.setBackgroundResource(R.drawable.button_background_disable);
//                                // 保存最后一张非退货型零售单的数据，以便首页能打印上一单
//                                lastRetailTrade = retailTrade;
//                                //打印小票
//                                try {
//                                    printSmallSheet(lastRetailTrade);
//                                } catch (Exception e) {
//                                    log.info("打印小票异常：" + e.getMessage());
//                                }
//                                retailTrade = null;
//                                System.out.println("零售单已结清，返回主页面");
//                                // 支付完成，跳转页面
//                                Intent mainActivityIntent = new Intent(PaymentActivity.this, MainActivity.class);
//                                mainActivityIntent.putExtra("isPaid", 1);
//                                PaymentActivity.this.finish();
//                                startActivity(mainActivityIntent);
//                            } else {
//                                unpaidBalance.setText(GeneralUtil.formatToShow(sub(Double.valueOf(unpaidBalance.getText().toString()), Double.valueOf(etCashPayingAmount.getText().toString()))));
//                                getPayingMoney().setText(unpaidBalance.getText().toString());
//                            }
//                            break;
//                        case EC_OtherError:
//                            switch (mthCash.getSubErrorCode()) {
//                                case SubErrorCode_RetailTrade_OtherError:
//                                    Toast.makeText(getApplicationContext(), "创建零售单失败！", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case SubErrorCode_RetailTradePromoting_OtherError:
//                                    Toast.makeText(getApplicationContext(), "创建促销计算过程失败！", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                            break;
//                    }
//                    isPayingViaCash = false;
//                    closeLoadingDialog(loadingDailog);
//                    break;
//            }
//        }
//    };
//
////    @Override
////    protected void onPause() {
////        super.onPause();
////        if (dialog != null) {
////            dialog.dismiss();
////        }
////        EventBus.getDefault().unregister(this);
////    }
//
//    /**
//     * 初始化副屏
//     */
//    @TargetApi(Build.VERSION_CODES.N)
//    @Override
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    protected void initPresentationData(Context context) {
//        screenManager.init(context);
//        Display[] displays = screenManager.getDisplays();
//        log.info("屏幕数量" + displays.length);
//        for (int i = 0; i < displays.length; i++) {
//            log.info("屏幕" + displays[i]);
//        }
//        Display display = screenManager.getPresentationDisplays();
//        if (display != null && !isVertical) {
//            welcomePresentation = new WelcomePresentation(this, display);
//            customerCommodityListPresentation = new CustomerCommodityListPresentation(this, display, true);
//            paymentSuccessPresentation = new PaymentSuccessPresentation(this, display);
//        }
//    }
//
//
//
///*    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if ("".equals(etWechatPayingAmount.getText().toString()) || null == etWechatPayingAmount.getText().toString() || "0".equals(etWechatPayingAmount.getText().toString())) {
//            etWechatPayingAmount.setFocusable(true);
//            etWechatPayingAmount.setFocusableInTouchMode(true);
//            etWechatPayingAmount.requestFocus();
//            etWechatPayingAmount.setError("支付金额必须大于0");
//        } else {
//            etWechatPayingAmount.setError(null);
//            if (compareString(etWechatPayingAmount.getText().toString(), unpaidBalance.getText().toString()) > 0) {
//                changeMoney.setText(GeneralUtil.formatToShow(GeneralUtil.sub(Double.valueOf(etWechatPayingAmount.getText().toString()), Double.valueOf(unpaidBalance.getText().toString()))));
//            } else {
//                changeMoney.setText(DEFAULT_changeAmount);
//            }
//        }
//    }*/
//
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//
//        final AlertDialog.Builder cancelPayBuilder = new AlertDialog.Builder(this)
//                .setTitle("提示")
//                .setMessage("是否确定返回到主页面,这会取消本次交易！")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        cancelPayment();
//                        Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
//                        PaymentActivity.this.finish();
//                        startActivity(intent2);
//                    }
//                });
//        cancelPayDialog = cancelPayBuilder.create();
//        cancelPayDialog.show();
//    }
//
//    private void changeRadioButtonToPayViaCashOnly() {
//        log.info("设置RadioButton的属性,当前状态为:" + bCanPayViaCashOnly);
//        if (!bCanPayViaCashOnly) {
//            cashPay.setChecked(true); // 设置现金按钮被点击
//            wechatPay.setEnabled(false);// 设置微信支付按钮不给与点击
//            bCanPayViaCashOnly = true; // 设置是否改变RadionButton
//            tvWechatPaying.setVisibility(View.INVISIBLE);
//            etWechatPayingAmount.setVisibility(View.INVISIBLE);
//            tvCashPaying.setVisibility(View.VISIBLE);
//            etCashPayingAmount.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     * 点击键盘，输入当前支付方式的支付金额时，需要确定当前输入的文本框是微信金额的文本框还是现金金额的文本框，返回相应的文本框
//     */
//    private EditText getPayingMoney() {
//        if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
//            return etWechatPayingAmount;
//        } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) {
//            return etCashPayingAmount;
//        }
//        return null;
//    }
//
////    private TextView getPayingMethod() {
////        if (choosePaymentType.getCheckedRadioButtonId() == wechatPay.getId()) {
////            return tvWechatPaying;
////        } else if (choosePaymentType.getCheckedRadioButtonId() == cashPay.getId()) {
////            return tvCashPaying;
////        }
////        return null;
////    }
//}