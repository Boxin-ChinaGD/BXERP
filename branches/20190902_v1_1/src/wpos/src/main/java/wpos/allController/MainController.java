package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.adapter.CommodityListCellAdapter;
import wpos.adapter.DialogCommodityListCellAdapter;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.LoginActivity;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.coupon.CouponCalculator;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.helper.ScanGun;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.listener.radioButtonSelectLinstener;
import wpos.model.*;
import wpos.model.promotion.Promotion;
import wpos.model.promotion.PromotionCalculator;
import wpos.model.promotion.PromotionShopScope;
import wpos.presenter.*;
import wpos.utils.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static wpos.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions;
import static wpos.model.ErrorInfo.EnumErrorCode.*;
import static wpos.model.ErrorInfo.EnumErrorCode.EC_OtherError;
import static wpos.utils.GeneralUtil.sub;

@Component("MainController")
public class MainController implements radioButtonSelectLinstener, PlatFormHandlerMessage {

    private final String weChatPayString = "微信支付";
    private final String cashPayString = "现金支付";
    private String choosePaymentType;
    //键盘输入的值
    private String keyBoard = "";
    private final HandlerMessage handlerMessage = new HandlerMessage();
    private final EnlargeHandler enlargeHandler = new EnlargeHandler();

    AllFragmentViewController viewController;

    private ScanGun scanGun = null;

    private Timer timerToStartUploadRetailTradeAggregation;

    private static NetworkUtils networkUtils = new NetworkUtils();

    @Resource
    private LoginActivity loginActivity;

    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;

    public MainController() {

    }

    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        onCreate();
    }

    //    @FXML   //结账按钮
    public void balance_click() {
        // 进入结算页面时断网
        if (GlobalController.getInstance().getSessionID() == null || (!NetworkUtils.isNetworkAvalible())) { // xxx 是否需要放前面
            changeRadioButtonToPayViaCashOnly();
            paymentCode = 0b00000000;
            viewController.pay.setText("支  付");
            viewController.pay.setDisable(false);
        } else {
            changeRadioButtonToPayViaWX();
        }
        balance();
    }

    // 挂单按钮
    public void holdBill_click() {
        // 将零售单的数据存放到list中，需要包括会员以及促销信息
        if (BaseController.retailTrade != null && BaseController.retailTrade.getListSlave1() != null && BaseController.retailTrade.getListSlave1().size() > 0) {
            if (BaseController.retailTradeHoldBillList.size() >= BaseController.MAX_HOLD_BILL_NO) {
                ToastUtil.toast("挂单失败！挂单列表已有" + BaseController.MAX_HOLD_BILL_NO + "张未结零售单，请先处理", ToastUtil.LENGTH_SHORT);
            } else {
                RetailTrade retailTrade = (RetailTrade) BaseController.retailTrade.clone();
                retailTrade.setHoldBillTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                if (vip != null) {
                    retailTrade.setVip(vip);
                }
                //
                List<Commodity> commodityList = new ArrayList<Commodity>();
                try {
                    for (Commodity commodity : BaseController.showCommList) {
                        Commodity c = (Commodity) commodity.clone();
                        commodityList.add(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                retailTrade.setFirstCommodityName(commodityList.get(0).getName());
                //
                retailTrade.setCommodityListOfHeldBill(commodityList);
                BaseController.retailTradeHoldBillList.add(retailTrade);
                resetTradeAfterHoldBill();
                initializeVipData();
                ToastUtil.toast("挂单成功", ToastUtil.LENGTH_SHORT);
            }
        } else {
            ToastUtil.toast("挂单失败！还未添加商品", ToastUtil.LENGTH_SHORT);
        }
    }

    //    @FXML   //取消支付按钮
    public void paymentClose_click() {
        confirmPaymentClose();
    }

    //    @FXML   //单选按钮，现金支付
    public void cashPay_click() {
        viewController.cashPay.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3;");
        viewController.cash_pay_text.setStyle("-fx-text-fill: #2196F3;-fx-background-color: #E3F2FD;");
        viewController.wechatPay.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #9E9E9E;");
        viewController.weChat_pay_text.setStyle("-fx-text-fill: #263238;-fx-background-color: #F5F5F5;");
        //监听回调
        this.selectItem(cashPayString);
    }

    //    @FXML   //单选按钮，微信支付
    public void wechatPay_click() {
        viewController.cashPay.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #9E9E9E;");
        viewController.cash_pay_text.setStyle("-fx-text-fill: #263238;-fx-background-color: #F5F5F5;");
        viewController.wechatPay.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3;");
        viewController.weChat_pay_text.setStyle("-fx-text-fill: #2196F3;-fx-background-color: #E3F2FD;");
        //
        this.selectItem(weChatPayString);
    }

    //    @FXML   //扫描条形码输入框
    public void scanBarcodeText_click() {
        //显示搜索对话框
        searchCommodityByBarcode();
    }

    //    @FXML   //扫描条形码输入框
    public void scan_barcode_search() {
        //显示搜索对话框
        searchCommodityByBarcode();
    }

    //搜索vip按钮
    public void searchVip_click() {
        //获取搜索框中的数据然后进行查询
        String queryKeyword = viewController.showClientPhone.getText();
        System.out.println("会员号：" + queryKeyword);
        //初始化会员相关信息
        initializeVipData();
        if (queryKeyword.length() == FieldFormat.LENGTH_Mobile || queryKeyword.length() == Vip.Max_LENGTH_VipCardSN) {
            viewController.showLoadingDialog();

            Vip vip = new Vip();
            vip.setCategory(1);
            if (queryKeyword.length() == FieldFormat.LENGTH_Mobile) {
                vip.setMobile(queryKeyword);
            } else {
                vip.setVipCardSN(queryKeyword);
            }
            if (GlobalController.getInstance().getSessionID() != null && NetworkUtils.isNetworkAvalible()) { // 联网查询
                if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast("搜索会员失败，请重试！", ToastUtil.LENGTH_SHORT);
                    log.error("搜索会员失败：" + queryKeyword);
                }
            } else {   // 无网
                ToastUtil.toast(BaseHttpBO.ERROR_MSG_Network, ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
            }
        } else {
            System.out.println("执行");
            ToastUtil.toast(Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN, ToastUtil.LENGTH_SHORT);
        }
    }

    //返回vip搜索按钮
    public void return_searchVip_click() {
        initializeVipData();
    }

    //    @FXML   //支付按钮点击
    public void pay_click() {
        viewController.showLoadingDialog();
        if (!isPayingViaCash && BaseController.retailTrade != null) {
            // 判断优惠券是否可用
            if (!validateCouponCodeAvailable()) {
                return;
            }
            if (choosePaymentType.equals(weChatPayString)) { // 沙箱环境WX支付
                if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText()) || Double.parseDouble(viewController.etWechatPayingAmount.getText()) <= 0) {
                    ToastUtil.toast("微信支付金额必须大于0");
                    viewController.closeLoadingDialog();
                } else if ((viewController.etWechatPayingAmount.getText().length() - viewController.etWechatPayingAmount.getText().indexOf(".")) == 1) {
                    ToastUtil.toast("输入金额无效");
                    viewController.closeLoadingDialog();
                } else {
                    //todo 此处需要加入线程池管理
                    new Thread(() -> payViaWX(Double.valueOf(getPayingMoney().getText()))).start();
                }
            } else if (choosePaymentType.equals(cashPayString)) { // 现金支付
                if (StringUtils.isEmpty(viewController.etCashPayingAmount.getText()) || Double.valueOf(viewController.etCashPayingAmount.getText()) < 0) {
                    ToastUtil.toast("支付金额必须大于或等于0");
                    viewController.closeLoadingDialog();
                } else if ((viewController.etCashPayingAmount.getText().length() - viewController.etCashPayingAmount.getText().indexOf(".")) == 1) {
                    ToastUtil.toast("输入金额无效");
                    viewController.closeLoadingDialog();
                } else {
                    //todo 此处需要加入线程池管理
                    new Thread(() -> payViaCash(Double.valueOf(getPayingMoney().getText().toString()))).start();
                }
            }
        }
    }

    //    @FXML
    public void key10() {
        keyBoard = "10";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key20() {
        keyBoard = "20";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key50() {
        keyBoard = "50";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key100() {
        keyBoard = "100";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void keyDelete() {
        if (keyBoard != null && !keyBoard.equals("")) {
            keyBoard = keyBoard.substring(0, keyBoard.length() - 1);
            getPayingMoney().setText(keyBoard);
        }
    }

    //    @FXML
    public void keyPoint() {
        keyBoard += ".";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key0() {
        keyBoard += "0";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key1() {
        keyBoard += "1";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key2() {
        keyBoard += "2";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key3() {
        keyBoard += "3";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key4() {
        keyBoard += "4";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key5() {
        keyBoard += "5";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key6() {
        keyBoard += "6";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key7() {
        keyBoard += "7";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key8() {
        keyBoard += "8";
        getPayingMoney().setText(keyBoard);
    }

    //    @FXML
    public void key9() {
        keyBoard += "9";
        getPayingMoney().setText(keyBoard);
    }


    private final Log log = LogFactory.getLog(this.getClass());
    private PromotionCalculator promotionCalculator = new PromotionCalculator();
    @Resource
    private BarcodesPresenter barcodesPresenter;
    @Resource
    private RetailTradePresenter retailTradePresenter;
    @Resource
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    @Resource
    private CommodityPresenter commodityPresenter;
    @Resource
    private PackageUnitPresenter packageUnitPresenter;
    @Resource
    private PromotionPresenter promotionPresenter;
    @Resource
    private PromotionShopScopePresenter promotionShopScopePresenter;
    @Resource
    private RetailTradePromotingPresenter retailTradePromotingPresenter;
    @Resource
    private ConfigGeneralPresenter configGeneralPresenter;
    @Resource
    private SmallSheetFramePresenter smallSheetFramePresenter;
    @Resource
    private RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter;
    private final static String DEFAULT_changeAmount = "0.00"; //默认付全款时，找零应为0.00

    private RetailTrade tempCalculatedRetailTrade = null;

    @Resource
    public RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    public RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    public RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    public RetailTradeHttpBO retailTradeHttpBO;

    @Resource
    public RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO;
    @Resource
    public RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
    @Resource
    public RetailTradePromotingHttpEvent retailTradePromotingHttpEvent;
    @Resource
    public RetailTradePromotingHttpBO retailTradePromotingHttpBO;

    @Resource
    public RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    @Resource
    public RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    @Resource
    public RetailTradeAggregationHttpBO retailTradeAggregationHttpBO;
    @Resource
    public RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent;
    @Resource
    public VipHttpBO vipHttpBO;
    @Resource
    public VipHttpEvent vipHttpEvent;
    @Resource
    public CouponCodeHttpBO couponCodeHttpBO;
    @Resource
    public CouponCodeHttpEvent couponCodeHttpEvent;
    @Resource
    public CouponPresenter couponPresenter;
    @Resource
    public RetailTradeCouponPresenter retailTradeCouponPresenter;
    @Resource
    public PosPresenter posPresenter;
    @Resource
    public RetailTradeCouponSQLiteBO retailTradeCouponSQLiteBO;
    @Resource
    public WXPayHttpEvent wxPayHttpEvent;
    @Resource
    public WXPayHttpBO wxPayHttpBO;

    List<Commodity> commodityListAfterQuery = new ArrayList<Commodity>();
    private List<Object> objectList = new ArrayList<Object>();
    // 登录开机同步后才输入准备金，创建收银汇总，在首页点击其它选项不能重复输入准备金
    public static boolean firstTimeEnterMain1Activity = true;
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
    private double total_money_double = 0.000000d;//总计金额的double数据（用于上传到服务器，和保存到本地SQLite，不需要对小数位进行操作）
    private RetailTradePromoting retailTradePromoting;
    //商户退款单号
    private String wxOutRefundNO;
    //商户退款失败信息
    private String wxOutRefundErrorMsg;
    public static boolean bPaying = false;
    public static final String MAX_CashPaymentAmount = "99999";
    private final static int LENGTH_WxPayAuthCode = 18;
    private final static double MIN_WechatPaymentAmount = 0.00; // 微信支付金额必须大于该值
    // 售卖商品列表中数量的默认最大值
    private int Default_CommodityMaxNumber = 9999;
    private int paymentCode = 0b00000000;//用于表示未进行支付，在支付后，paymentType的值为paymentCode + 对应的支付方式的二进制数
    private final int wechatPayCode = 0b00000100;//用于表示相应支付方式的二进制数
    private final int cashPayCode = 0b00000001;
    private CommodityListCellAdapter commodityRecyclerViewAdapter;
    private DialogCommodityListCellAdapter dialogCommodityRecyclerViewAdapter;

    private double totalWechatAmount;
    private double totalCashAmount;
    private boolean bCanPayViaCashOnly = false;

    /**
     * 用于记录本次结算使用微信支付了多少
     */
    private double wetchatAmount;//
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

    /**
     * 标识上一次扫码微信支付还未完成。
     * 扫码时检查此标记，决定是否执行微信支付，防止用户疯狂扫码！
     */
    private boolean bLastWxPayIsNotYetDone = false;

    /**
     * 标识用户当前正在用现金支付，用户已经点击一次现金支付，不能再处理他后续的点击事件
     */
    private static boolean isPayingViaCash = false;

    private Map<String, String> microPayResponse; // 微信支付结果
    /**
     * 用于记录本次结算使用现金支付了多少
     */
    private double cashAmount;//

    private final String COUPONTITLELIST_DEFAULT_UnavailableConpon = "无可用的优惠券";
    private final String COUPONTITLELIST_DEFAULT_AvailableConpon = "有可用的优惠券";

    private String wxPayAuthCode = ""; // 微信付款授权码

    //搜索商品的弹窗
    private JFXAlert chooseCommodityDialog;
    private ChooseCommodityDialogViewController chooseCommodityDialogViewController;

    /**
     * 记录选中的待购商品
     */
    final int[] POSITION_CommodityToRemove = {BaseSQLiteBO.INVALID_INT_ID};

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
        } else {
            log.info("未处理的Event，ID=" + event.getId() + "syncThread");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            Message message = new Message();
            message.what = 5;
            PlatForm.get().sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onWXPayHttpEvent(WXPayHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseController.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("创建临时收银汇总成功！临时收银汇总=" + BaseController.retailTradeAggregation);
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            Message message = new Message();
            message.what = 6;
            PlatForm.get().sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    public void onBackFromBillFragment() {
        if (BaseController.retailTrade != null && BaseController.retailTrade.getVip() != null) {
            viewController.showLoadingDialog();
            vip = BaseController.retailTrade.getVip();
            //
            viewController.showClientPhone.setVisible(false);
            viewController.showClientPhone.setManaged(false);
            viewController.search_vip_button.setVisible(false);
            viewController.search_vip_button.setManaged(false);
            //
            viewController.vipMobile.setText("会员手机号：" + vip.getMobile());
            viewController.vipBonus.setText("积分：" + vip.getBonus());
            viewController.vipMobile.setVisible(true);
            viewController.vipMobile.setManaged(true);
            viewController.vipBonus.setVisible(true);
            viewController.vipBonus.setManaged(true);
            viewController.return_search_vip_button.setVisible(true);
            viewController.return_search_vip_button.setManaged(true);
            // 查找此会员相关的优惠券
            CouponCode couponCode = new CouponCode();
            couponCode.setVipID(vip.getID().intValue());
            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                ToastUtil.toast("查找会员的优惠券失败", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
            }
        }
    }

    private void onCreate() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);

        initEventAndBO();

        if (firstTimeEnterMain1Activity) {
            //弹出输入准备金
            showReserveDialog();
            viewController.initTimers();
            firstTimeEnterMain1Activity = false;
        }

        //初始化扫码枪前，先进行键盘监听
        initScanGunListener(viewController.sale_Pane);

        initializeVipData();
        // 取单后恢复会员信息
        onBackFromBillFragment();
        initScanGun();

        // Fragment之前切换，重新刷新待购商品列表
        if (BaseController.showCommList.size() > 0) {
            showCommodity(BaseController.showCommList);
        }

        viewController.rvCommodity.requestFocus();
        System.out.println("销售地址：" + BaseController.retailTradeAggregation.hashCode());
    }

    //注册监听事件
    private void initScanGunListener(Pane pane) {
        for (Node sale_paneChild : pane.getChildren()) {
            if (sale_paneChild instanceof Pane) {
                initScanGunListener((Pane) sale_paneChild);
            } else {
                if (!(sale_paneChild instanceof TextField)) {
                    sale_paneChild.setOnKeyPressed(enlargeHandler);
                }
            }

        }
    }

    private Date getDefaultSyncDatetime() {
        try {
            return Constants.getDefaultSyncDatetime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initEventAndBO() {
        retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeAggregationSQLiteBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);

        retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeAggregationHttpBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationHttpBO.setHttpEvent(retailTradeAggregationHttpEvent);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);

        retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        //
        retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradePromotingHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradePromotingHttpBO.setSqLiteEvent(retailTradePromotingSQLiteEvent);
        retailTradePromotingHttpBO.setHttpEvent(retailTradePromotingHttpEvent);
        retailTradePromotingSQLiteBO.setSqLiteEvent(retailTradePromotingSQLiteEvent);
        retailTradePromotingSQLiteBO.setHttpEvent(retailTradePromotingHttpEvent);
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
        //
        wxPayHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        wxPayHttpBO.setHttpEvent(wxPayHttpEvent);
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);

        vipHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        vipHttpBO.setHttpEvent(vipHttpEvent);
        vipHttpEvent.setHttpBO(vipHttpBO);

        couponCodeHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        couponCodeHttpBO.setHttpEvent(couponCodeHttpEvent);
        couponCodeHttpEvent.setHttpBO(couponCodeHttpBO);
    }

    //输入准备金提示框
    private void showReserveDialog() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/reserveDialog.fxml"));
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFXAlert alert = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setContent(borderPane);
        alert.show();

        ReserveDialogViewController reserveDialogViewController = loader.getController();
        reserveDialogViewController.setAlert(alert);
        reserveDialogViewController.setListener(s -> {
            boolean DecimalPointOnErrorPosition = s.indexOf(".") == s.length() - 2
                    || s.indexOf(".") == s.length() - 1;
            if (s.contains(".") && DecimalPointOnErrorPosition) {
                ToastUtil.toast("请输入整数或者保留小数点后两位的小数");
            } else if ("".equals(s)) {
                ToastUtil.toast("准备金不允许为空");
            } else if (Double.parseDouble(s) > 10000d) {
                ToastUtil.toast("输入的准备金不能大于10000");
            } else if (s.startsWith(".")) {
                ToastUtil.toast("错误的金额格式，请重新输入正确的准备金额");
            } else {
                BaseController.retailTradeAggregation.setReserveAmount(Double.parseDouble(s));
                createRetailTradeAggregation(); //TODO 将来这里要加一个转圈圈
                alert.close();
                viewController.rvCommodity.requestFocus();
            }
        });
    }

    private void initializeVipData() {
        vip = null;
        couponCodeList.clear();
        showCouponCodeList.clear();

//        ObservableList<String> observableList = FXCollections.observableArrayList(getCouponCodeTitleList());
        if (changeListener != null) {
            viewController.spinner.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        }
//        viewController.spinner.setItems(observableList);
        viewController.spinner.setDisable(false);
        if (changeListener == null) {
            setSpinner();
        }
//        viewController.spinner.getSelectionModel().selectedItemProperty().addListener(changeListener);

        couponCode = null;
        coupon = null;
        beforeUsingCouponAmount = 0.0000000d;
        viewController.showClientPhone.setText("");
        viewController.vipMobile.setText("会员手机号：");
        viewController.vipBonus.setText("积分：");
        //
        viewController.vipMobile.setVisible(false);
        viewController.vipMobile.setManaged(false);
        viewController.vipBonus.setVisible(false);
        viewController.vipBonus.setManaged(false);
        viewController.return_search_vip_button.setVisible(false);
        viewController.return_search_vip_button.setManaged(false);
        //
        viewController.showClientPhone.setVisible(true);
        viewController.showClientPhone.setManaged(true);
        viewController.search_vip_button.setVisible(true);
        viewController.search_vip_button.setManaged(true);
    }

    /**
     * TODO 可否移动到类外，将来对其进行独立测试？
     *
     * @return
     */
    private List<String> getCouponCodeTitleList() {
        List<String> couponCodeTitleList = new ArrayList<String>();
        if (couponCodeList.size() == 0) {
            couponCodeTitleList.add(COUPONTITLELIST_DEFAULT_UnavailableConpon);
        } else {
            couponCodeTitleList.add(COUPONTITLELIST_DEFAULT_AvailableConpon);
            Coupon coupon = new Coupon();
            for (CouponCode couponCode : couponCodeList) {
                coupon.setID((couponCode.getCouponID()));
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
                        for (int i = 0; i < BaseController.showCommList.size(); i++) {
                            Commodity commodity = BaseController.showCommList.get(i);
                            for (int j = 0; j < couponScopes.size(); j++) {
                                CouponScope couponScope = couponScopes.get(j);
                                if (commodity.getID() == couponScope.getCommodityID()) {
                                    amount = GeneralUtil.sum(amount, commodity.getSubtotal());
                                }
                            }
                        }
                    } else {
                        amount = BaseController.retailTrade.getAmount();
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
     * 点击键盘，输入当前支付方式的支付金额时，需要确定当前输入的文本框是微信金额的文本框还是现金金额的文本框，返回相应的文本框
     */
    private Label getPayingMoney() {
        if (choosePaymentType.equals(weChatPayString)) {
            return viewController.etWechatPayingAmount;
        } else if (choosePaymentType.equals(cashPayString)) {
            return viewController.etCashPayingAmount;
        }
        return null;
    }

    /**
     * 重置交易。挂单后，重新初始化零售单和UI
     */
    private void resetTradeAfterHoldBill() {
        BaseController.showCommList.clear();
        showCommodity(BaseController.showCommList);
        total_money_double = 0.000000d;
        viewController.commodityQuantity.setText("商品数量：" + "0" + " 件");//初始话商品数量UI
        retailTradePromoting = new RetailTradePromoting();// 促销过程重置
        BaseController.retailTrade = null;

        // 上一单信息
        viewController.last_amount.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeAmount));
        viewController.last_changemoney.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeChangeMoney));
        viewController.last_paymenttype.setText(BaseController.lastRetailTradePaymentType);
    }

    /**
     * 重置交易，以免污染下一个交易
     *
     * @param isPaid 已经支付完毕
     */
    private void resetTradeAfterPay(int isPaid) {
        //关闭结账页面
        viewController.payment.setVisible(false);
        viewController.payment.setManaged(false);
        bPaying = false;
        //
        if (isPaid == 1) {
            BaseController.showCommList.clear();
            showCommodity(BaseController.showCommList);
            total_money_double = 0.000000d;
            viewController.commodityQuantity.setText("商品数量：" + "0" + " 件");//初始话商品数量UI
            retailTradePromoting = new RetailTradePromoting();// 促销过程重置
            BaseController.retailTrade = null;
            viewController.total_money.setText("0.00");
            totalWechatAmount = 0.000000d;
            totalCashAmount = 0.000000d;
            viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));

            // 上一单信息
            viewController.last_amount.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeAmount));
            viewController.last_changemoney.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeChangeMoney));
            viewController.last_paymenttype.setText(BaseController.lastRetailTradePaymentType);
        } else {
            // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
            System.out.println("没支付就终止了交易");
        }
    }

    public void showCommodity(final List<Commodity> list) {
        viewController.scan_barcode_text.setText("");
        viewController.scan_barcode_text.setPromptText("扫描条形码（Ctrl+Q）");
        //计算商品总数量，并在UI展示
        int iCommodityQuantity = 0;
        for (Commodity commodity : list) {
            iCommodityQuantity += commodity.getCommodityQuantity();
        }
        viewController.commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNumber(i + 1);
        }
        //
        viewController.rvCommodity.setItems(null);
        viewController.rvCommodity.setItems(FXCollections.observableList(list));

        viewController.rvCommodity.setCellFactory((Callback) e -> {
            commodityRecyclerViewAdapter = new CommodityListCellAdapter();
            mainCommodityRecyclerViewItemClick(commodityRecyclerViewAdapter, list);
            return commodityRecyclerViewAdapter;
        });
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
        if (BaseController.retailTrade != null) {
            viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        } else {
            viewController.total_money.setText("0.00");
        }
    }

    //该方法内包含左键点击事件和右键点击事件
    private void mainCommodityRecyclerViewItemClick(final CommodityListCellAdapter commodityRecyclerViewAdapter, final List<Commodity> list) {
        commodityRecyclerViewAdapter.setOnClickListener(new CommodityListCellAdapter.onClickListener() {
            @Override
            public void onRightClick() {
                POSITION_CommodityToRemove[0] = viewController.rvCommodity.getSelectionModel().getSelectedIndex();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ui/reportLossOrCollectionDialog.fxml"));

                JFXAlert reportLossOrCollectionDialog = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
                reportLossOrCollectionDialog.initModality(Modality.APPLICATION_MODAL);
                reportLossOrCollectionDialog.setOverlayClose(true);
                try {
                    reportLossOrCollectionDialog.setContent((FlowPane) loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reportLossOrCollectionDialog.show();

                ReportLossOrCollectionDialogViewController controller = loader.getController();
                //移除商品按钮点击事件
                controller.setListener(() -> {
                    if (POSITION_CommodityToRemove[0] != BaseSQLiteBO.INVALID_INT_ID) {
                        list.remove(POSITION_CommodityToRemove[0]);
                        // 查找属于该门店的促销
                        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                        if(avaliablePromotionList == null) {
                            ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
                            return;
                        }
                        retailTradePromoting = new RetailTradePromoting();
                        BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
                        if (BaseController.retailTrade != null) {
                            viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
                        } else {
                            viewController.total_money.setText("0.00");
                        }
                        final CommodityListCellAdapter mAdapter = new CommodityListCellAdapter();
                        viewController.rvCommodity.setCellFactory(param -> mAdapter);
                        viewController.rvCommodity.setItems(null);
                        viewController.rvCommodity.setItems(FXCollections.observableList(list));
                        //在remove商品后增加点击事件，因为remove后要改变编号，然后需要重新写一个点击事件，否则点击无效果
                        mAdapter.setOnClickListener(new CommodityListCellAdapter.onClickListener() {
                            @Override
                            public void onRightClick() {
                                //todo 点击item背景变色未完成
                            }

                            @Override
                            public void onLeftClick(Commodity newValue) {

                            }
                        });
                        mainCommodityRecyclerViewItemClick(mAdapter, list);
                        //计算商品总数量，并在UI展示
                        int iCommodityQuantity = 0;
                        for (int i = 0; i < list.size(); i++) {
                            iCommodityQuantity += list.get(i).getCommodityQuantity();
                        }
                        viewController.commodityQuantity.setText("商品数量：" + iCommodityQuantity + " 件");
                        POSITION_CommodityToRemove[0] = -1;
                    } else {
                        ToastUtil.toast("未选中商品");
                    }
                    // 查找属于该门店的促销
                    List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                    if(avaliablePromotionList == null) {
                        ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
                        return;
                    }
                    //
                    retailTradePromoting = new RetailTradePromoting();
                    BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
                    if (BaseController.retailTrade != null) {
                        viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
                    } else {
                        viewController.total_money.setText("0.00");
                    }
                    showCommodity(BaseController.showCommList);
                    reportLossOrCollectionDialog.close();
                });
            }

            @Override
            public void onLeftClick(Commodity newValue) {
                //todo 点击item背景变色未完成
                log.info("商品信息：" + newValue.toString());
                showInputNumberDialog(newValue.getNumber() - 1, list);
            }
        });
    }

    private void showInputNumberDialog(final int position, final List<Commodity> list) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/inputNumberDialog.fxml"));

        JFXAlert inputNumberDialog = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        inputNumberDialog.initModality(Modality.APPLICATION_MODAL);
        inputNumberDialog.setOverlayClose(true);
        try {
            inputNumberDialog.setContent((BorderPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputNumberDialogViewController controller = loader.getController();
        controller.setAlert(inputNumberDialog);
        controller.setInputNumberDialogListener(commodityNum -> {
            //更新了某个字段之后，需要重新导入数据到recyclerview,由于是虚拟数据所以没有更新
            list.get(position).setCommodityQuantity(Integer.parseInt(commodityNum));
            list.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
            // 查找属于该门店的促销
            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
            if(avaliablePromotionList == null) {
                ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
                return;
            }
            retailTradePromoting = new RetailTradePromoting();
            BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
            if (BaseController.retailTrade != null) {
                viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
            } else {
                viewController.total_money.setText("0.00");
            }
            //todo 副屏部分

            //todo 数据刷新，因为没得测试，以后可能会有问题
            viewController.rvCommodity.setItems(null);
            viewController.rvCommodity.setItems(FXCollections.observableList(list));

            //计算商品总数量，并在UI展示
            int iCommodityQuantity = 0;
            for (int i = 0; i < list.size(); i++) {
                iCommodityQuantity += list.get(i).getCommodityQuantity();
            }
            viewController.commodityQuantity.setText("商品数量：" + iCommodityQuantity + "件");
        });
        inputNumberDialog.show();
    }

    private void changeRadioButtonToPayViaCashOnly() {
        log.info("设置RadioButton的属性,当前状态为:" + bCanPayViaCashOnly);
        if (!bCanPayViaCashOnly) {
            cashPay_click(); // 设置现金按钮被点击
            viewController.wechatPay.setDisable(true);// 设置微信支付按钮不给与点击
            bCanPayViaCashOnly = true; // 设置是否改变RadionButton

            viewController.tvWechatPaying.setVisible(false);
            viewController.etWechatPayingAmount.setManaged(false);

            viewController.tvCashPaying.setVisible(true);
            viewController.etCashPayingAmount.setVisible(true);
        }
    }

    //显示搜索对话框
    private void searchCommodityByBarcode() {
        log.debug("弹出搜索商品窗口");
        if (chooseCommodityDialog == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/chooseCommodityDialog.fxml"));

            chooseCommodityDialog = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
            chooseCommodityDialog.initModality(Modality.APPLICATION_MODAL);
            chooseCommodityDialog.setOverlayClose(true);
            try {
                chooseCommodityDialog.setContent((BorderPane) loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

            chooseCommodityDialogViewController = loader.getController();
            chooseCommodityDialogViewController.setAlert(chooseCommodityDialog);

            chooseCommodityDialogViewController.rv_alertdialog_commodity.setCellFactory(e -> {
                dialogCommodityRecyclerViewAdapter = new DialogCommodityListCellAdapter();
                return dialogCommodityRecyclerViewAdapter;
            });
            //输入框监听
            chooseCommodityDialogViewController.setTextChangeListener(commodityBarcodes -> {
                log.debug("根据条形进行商品搜索，条形码：" + commodityBarcodes);
                commodityListAfterQuery.clear();
                commodityListAfterQuery = retrieveNCommodityInSQLite(true, commodityBarcodes);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                //选择列表item点击事件
                chooseCommodityDialogViewController.rv_alertdialog_commodity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    dialogCommodityRecyclerViewAdapter.setSelectCommody(newValue);
                    //todo 点击item背景变色未完成
                });
            });
            //输入框后面的删除按钮监听以及取消按钮监听
            chooseCommodityDialogViewController.setDeleteAndCancelListener(new ChooseCommodityDialogViewController.DeleteAndCancelListener() {
                @Override   //输入框后面的删除按钮监听
                public void deleteAll() {
                    commodityListAfterQuery.clear();
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                }

                @Override   //点击了取消按钮
                public void cancel() {
                    chooseCommodityDialogViewController.search_commodity_et.setText("");
                    commodityListAfterQuery.clear();
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                    //将焦点移动至列表，防止扫码枪失效
                    viewController.rvCommodity.requestFocus();
                }

                @Override   //点击了加入按钮
                public void add() {
                    if (commodityListAfterQuery.size() > 0) {
                        //如果有选中商品
                        if (dialogCommodityRecyclerViewAdapter.getSelectCommody() != null) {
                            Commodity dialogCommodity = new Commodity();
                            dialogCommodity.setID(dialogCommodityRecyclerViewAdapter.getSelectCommody().getID());
                            dialogCommodity.setBarcode(dialogCommodityRecyclerViewAdapter.getSelectCommody().getBarcode());
                            //
                            isCommodityExistInUI(dialogCommodity);
                            viewController.scan_barcode_text.setPromptText("条形码：" + dialogCommodityRecyclerViewAdapter.getSelectCommody().getBarcode());
                            //
                            chooseCommodityDialog.hideWithAnimation();
                            //
                            showCommodity(BaseController.showCommList);
                            // 恢复原先选中的商品为选中状态。之前如果没有选中的话，下面的代码也不会有事
                            // TODO: 2020/6/16 选中状态

                            //重置列表为null,否则下次进入还存在上次的搜索数据
                            chooseCommodityDialogViewController.search_commodity_et.setText("");
                            commodityListAfterQuery.clear();
                            chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                            chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                            //将焦点移动至列表，防止扫码枪失效
                            viewController.rvCommodity.requestFocus();
                        } else {
                            ToastUtil.toast("请选择商品", ToastUtil.LENGTH_SHORT);
                        }
                    } else {
                        ToastUtil.toast("无可添加到待售列表的商品", ToastUtil.LENGTH_SHORT);
                    }
                }
            });
            chooseCommodityDialogViewController.setSearchCommodityEt(viewController.scan_barcode_text.getText());
        }
        chooseCommodityDialog.show();
        chooseCommodityDialogViewController.requestFocus();
    }

    /**
     * 判断该Commodity是否已经在待购列表中存在，若存在则只增加商品的购买数量和修改商品总价，否则在待购列表增加该商品
     *
     * @param c
     */
    private void isCommodityExistInUI(Commodity c) {
        boolean isAddToCommList = true;//用于判断是否在待购列表增加商品用
        if (BaseController.showCommList.size() > 0) {//判断是否有重复ID，若有，则增加该商品数量和改变总价，否则在待购列表增加商品总价
            for (int i = 0; i < BaseController.showCommList.size(); i++) {
                if (BaseController.showCommList.get(i).getID().equals(c.getID()) && BaseController.showCommList.get(i).getCommodityQuantity() < Default_CommodityMaxNumber) {//已经存在该商品，只增加数量和改变商品总价
                    BaseController.showCommList.get(i).setCommodityQuantity(Integer.valueOf(BaseController.showCommList.get(i).getCommodityQuantity()) + 1);
                    BaseController.showCommList.get(i).setSubtotal(BaseController.showCommList.get(i).getCommodityQuantity() * BaseController.showCommList.get(i).getAfter_discount());
                    isAddToCommList = false;
                    break;
                } else if (BaseController.showCommList.get(i).getID().equals(c.getID()) && BaseController.showCommList.get(i).getCommodityQuantity() >= Default_CommodityMaxNumber) {
                    ToastUtil.toast("一次零售的一种商品的数量已达到最大", ToastUtil.LENGTH_SHORT);
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
            packageUnit.setID(c.getPackageUnitID());
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
            BaseController.showCommList.add(c);
        }
        // ...等待完善
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
        //...
        //todo 副屏部分

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
            barcodes.setSql("where F_Barcode like %s");
            barcodes.setConditions(new String[]{"'%" + barcode + "%'"});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Ignore);
        } else {
            barcodes.setSql("where F_Barcode = %s");
            barcodes.setConditions(new String[]{barcode});
            barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        }
        List<Barcodes> barcodesList = (List<Barcodes>) barcodesPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions, barcodes);

        if (barcodesList != null) {
            for (int i = 0; i < barcodesList.size(); i++) {
                Commodity commodity = new Commodity();
                commodity.setID(barcodesList.get(i).getCommodityID());
                commodity = (Commodity) commodityPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, commodity);
                if (commodity != null) {
                    commodity.setNumber(i + 1);
                    commodity.setBarcode(barcodesList.get(i).getBarcode());

                    PackageUnit packageUnit = new PackageUnit();
                    packageUnit.setID(commodity.getPackageUnitID());
                    packageUnit = (PackageUnit) packageUnitPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, packageUnit);
                    commodity.setPackageUnit(packageUnit.getName());

                    commodityList.add(commodity);
                }
            }
        }
        log.info("commoditylist： " + commodityList);
        return commodityList;
    }

    private void changeRadioButtonToPayViaWX() {
        paymentCode = 0b00000000;
        viewController.pay.setText("请 扫 码");
        viewController.pay.setDisable(true);
//        wechatPay.setEnabled(true);// 设置微信支付按钮给与点击
        viewController.wechatPay.setDisable(false);
        wechatPay_click(); // 设置微信按钮被点击
        viewController.changeMoney.setText(DEFAULT_changeAmount);
        viewController.etWechatPayingAmount.requestFocus();
        viewController.tvWechatPaying.setVisible(true);
        viewController.etWechatPayingAmount.setVisible(true);
        viewController.wechat_paying_prefix.setVisible(true);
        viewController.tvCashPaying.setVisible(false);
        viewController.cash_paying_prefix.setVisible(false);
        viewController.etCashPayingAmount.setVisible(false);
    }

    private void balance() {
        if (BaseController.showCommList != null && BaseController.showCommList.size() > 0) {
            //将RetailTrade的信息传递到支付界面
            int maxRetailTradeIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class);
            RetailTrade retailTradeForPaymentActivity = BaseController.retailTrade;
            retailTradeForPaymentActivity.setID(maxRetailTradeIDInSQLite);
            retailTradeForPaymentActivity.setVipID(vip != null ? vip.getID() : 0);
            retailTradeForPaymentActivity.setStaffID(BaseController.retailTradeAggregation.getStaffID());
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
            retailTradeForPaymentActivity.setLocalSN(retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTrade.class));
            retailTradeForPaymentActivity.setRemark("......");
            //
            int maxRetailtradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCommodity.class);
            List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeForPaymentActivity.getListSlave1();
            List<RetailTradeCommodity> newRetailTradeCommodityList = new ArrayList<>();
            if (retailTradeCommodityList != null) {
                for (int i = 0; i < retailTradeCommodityList.size(); i++) {
                    RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
                    //
                    for (Commodity commodity : BaseController.showCommList) {
                        if (commodity.getID() == retailTradeCommodityList.get(i).getCommodityID()) {
                            retailTradeCommodity.setID(maxRetailtradeCommodityIDInSQLite + i);
                            retailTradeCommodity.setTradeID(Long.valueOf(retailTradeForPaymentActivity.getID()));
                            retailTradeCommodity.setCommodityID(commodity.getID().intValue());
                            retailTradeCommodity.setCommodityName(commodity.getName());
                            retailTradeCommodity.setNO(commodity.getCommodityQuantity());
                            retailTradeCommodity.setPriceOriginal(commodity.getPriceRetail());
                            retailTradeCommodity.setNOCanReturn(retailTradeCommodity.getNO());
                            retailTradeCommodity.setPriceReturn(retailTradeCommodityList.get(i).getPriceReturn());
                            //
                            Barcodes barcodes = new Barcodes();
                            barcodes.setSql("where F_CommodityID = %s");
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
                    int tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromoting.class);
                    int tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromotingFlow.class);
                    //
                    retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                    retailTradePromoting.setTradeID(retailTradeForPaymentActivity.getID().intValue());
                    for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
                        ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tmpRetailTradePromotingFlowIDInSQLite + i);
                    }
                }
            }
            BaseController.retailTrade = retailTradeForPaymentActivity;
            beforeUsingCouponAmount = retailTradeForPaymentActivity.getAmount();

            setSpinner();
            viewController.spinner.setDisable(false);
            viewController.payment.setVisible(true);//显示结账页面
            viewController.totalpay_money.setText(String.valueOf(BaseController.retailTrade.getAmount()));
            viewController.total_money.setText(String.valueOf(BaseController.retailTrade.getAmount()));
            initWidget();
            //
            bPaying = true;
            //
            //选择支付方式 （如果进支付页面时，微信支付可用，就会进入该分支）。
            if (choosePaymentType.equals(weChatPayString)) {
                if (Configuration.bUseSandBox) {
                    viewController.pay.setText("请 扫 码");
                    viewController.pay.setDisable(true);
                } else {
                    // pay.setBackgroundResource(R.drawable.button_background_disable);
                    viewController.pay.setText("支付完成");//... 不用沙箱支付却显示支付完成，是不是会引起误会？
                    viewController.pay.setDisable(true);
                }
            }

            viewController.etWechatPayingAmount.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!FieldFormat.checkPayAmount(viewController.etWechatPayingAmount.getText())) {
//                    if (viewController.etWechatPayingAmount.getText().startsWith(".")) {
                        ToastUtil.toast("输入金额无效", ToastUtil.LENGTH_SHORT);
                        viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText())) {
                        ToastUtil.toast("微信支付金额必须大于0", ToastUtil.LENGTH_SHORT);
                        viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) > 0) {
                        ToastUtil.toast("微信支付不能大于未付款金额", ToastUtil.LENGTH_SHORT);
                        viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else {
                        double change = sub(Double.valueOf(viewController.etWechatPayingAmount.getText()), Double.valueOf(viewController.unpaidBalance.getText()));
                        change = change > 0.00d ? change : 0.00d;
                        viewController.changeMoney.setText(GeneralUtil.formatToShow(change));
                    }
                }
            });

            viewController.etCashPayingAmount.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!FieldFormat.checkPayAmount(viewController.etCashPayingAmount.getText())) {
                        ToastUtil.toast("输入金额无效", ToastUtil.LENGTH_SHORT);
                        viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (StringUtils.isEmpty(viewController.etCashPayingAmount.getText()) || Double.valueOf(viewController.etCashPayingAmount.getText()) < 0) {
                        ToastUtil.toast("支付金额必须大于或等于0", ToastUtil.LENGTH_SHORT);
                        viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (compareString(viewController.etCashPayingAmount.getText(), MAX_CashPaymentAmount) > 0) {
                        ToastUtil.toast("现金支付的金额不能大于" + MAX_CashPaymentAmount, ToastUtil.LENGTH_SHORT);
                        viewController.etCashPayingAmount.setText(MAX_CashPaymentAmount);
                        keyBoard = "";
                    } else {
                        double change = sub(Double.valueOf(viewController.etCashPayingAmount.getText()), Double.valueOf(viewController.unpaidBalance.getText()));
                        change = change > 0.00d ? change : 0.00d;
                        viewController.changeMoney.setText(GeneralUtil.formatToShow(change));
                    }
                }
            });
        } else {
            ToastUtil.toast("商品列表为空", ToastUtil.LENGTH_SHORT);
        }
    }

    public void selectItem(String radioButton) {
        /**
         * 单选按钮被选中后，调用
         */
        choosePaymentType = radioButton;
        System.out.println("-----------改变");
        if (choosePaymentType.equals(weChatPayString)) {
            keyBoard = "";
            viewController.changeMoney.setText(DEFAULT_changeAmount);
            viewController.etWechatPayingAmount.requestFocus();
            viewController.tvWechatPaying.setVisible(true);
            viewController.etWechatPayingAmount.setVisible(true);
            viewController.wechat_paying_prefix.setVisible(true);
            viewController.tvCashPaying.setVisible(false);
            viewController.cash_paying_prefix.setVisible(false);
            viewController.etCashPayingAmount.setVisible(false);
            if (Configuration.bUseSandBox) {
                viewController.pay.setText("请 扫 码");
                viewController.pay.setDisable(true);
            } else {
                viewController.pay.setText("支付完成");
                viewController.pay.setDisable(true);
            }
            viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
        } else if (choosePaymentType.equals(cashPayString)) {
            keyBoard = "";
            viewController.etCashPayingAmount.requestFocus();
            viewController.tvWechatPaying.setVisible(false);
            viewController.etWechatPayingAmount.setVisible(false);
            viewController.wechat_paying_prefix.setVisible(false);
            viewController.tvCashPaying.setVisible(true);
            viewController.cash_paying_prefix.setVisible(true);
            viewController.etCashPayingAmount.setVisible(true);
            // 如果未付余额大于最大现金支付金额
            if (Double.valueOf(viewController.unpaidBalance.getText()) - Double.valueOf(MAX_CashPaymentAmount) > RetailTrade.TOLERANCE) {
                viewController.etCashPayingAmount.setText(MAX_CashPaymentAmount);
                viewController.changeMoney.setText(DEFAULT_changeAmount);
            } else {
                viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText().toString());
                viewController.changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(viewController.etCashPayingAmount.getText().toString()), Double.valueOf(viewController.unpaidBalance.getText().toString()))));
            }
            viewController.pay.setText("支  付");
            viewController.pay.setDisable(false);
        }
    }

    private ChangeListener changeListener;

    private void setSpinner() {
        List<String> couponCodeTitleList = getCouponCodeTitleList();
        ObservableList<String> observableList = FXCollections.observableArrayList(couponCodeTitleList);
        if (changeListener != null) {
            System.out.println("移除");
            viewController.spinner.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        }

        viewController.spinner.setItems(observableList);
        viewController.spinner.setVisibleRowCount(8);//可见行数为8，超出则滑动
        viewController.spinner.getSelectionModel().selectFirst();//选中第一个

        changeListener = (observable, oldValue, newValue) -> {
            String couponTitle = (String) newValue;
            if (!(COUPONTITLELIST_DEFAULT_AvailableConpon.equals(couponTitle) || COUPONTITLELIST_DEFAULT_UnavailableConpon.equals(couponTitle))) {
                for (int i = 0; i < couponCodeTitleList.size(); i++) {
                    if (couponCodeTitleList.get(i).equals(couponTitle)) {
                        // i-1是因为i的位置上有"有可用优惠券"或"无可用优惠券".而showCouponCodeList中没有，要正确获取收银员选择的优惠券需要-1
                        couponCode = showCouponCodeList.get(i - 1);
                    }
                }
                System.out.println("选择了一张优惠券：" + couponCode);
                Coupon queryCoupon = new Coupon();
                queryCoupon.setID(couponCode.getCouponID());
                coupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
                if (couponPresenter.getLastErrorCode() != EC_NoError) {
                    couponCode = null;
                    log.error("通过优惠券查找它是属于那种优惠券时失败！");
                    ToastUtil.toast("暂时无法使用该券", ToastUtil.LENGTH_SHORT);
                } else {
                    double amountToPay = CouponCalculator.calculateAmountUsingCoupon(coupon, beforeUsingCouponAmount, BaseController.showCommList, BaseController.retailTrade);

                    viewController.totalpay_money.setText(GeneralUtil.formatToShow(amountToPay));
                    viewController.unpaidBalance.setText(GeneralUtil.formatToShow(amountToPay));
                    viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(amountToPay));
                    viewController.etCashPayingAmount.setText(GeneralUtil.formatToShow(amountToPay));
                    BaseController.retailTrade.setAmount(amountToPay);
                }

            } else {
                couponCode = null;
                coupon = null;
                BaseController.retailTrade.setAmount(beforeUsingCouponAmount);
                viewController.totalpay_money.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                viewController.unpaidBalance.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
                viewController.etCashPayingAmount.setText(GeneralUtil.formatToShow(beforeUsingCouponAmount));
            }
        };
        viewController.spinner.getSelectionModel().selectedItemProperty().addListener(changeListener);

    }

    /**
     * 初始化部分控件
     */
    private void initWidget() {
        viewController.totalpay_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.unpaidBalance.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.etCashPayingAmount.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
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
                ObservableList<String> observableList = FXCollections.observableArrayList(getCouponCodeTitleList());
                viewController.spinner.setItems(observableList);
                viewController.spinner.getSelectionModel().selectFirst();//选中第一个
                ToastUtil.toast("此优惠券不可用,请选择其他优惠券");
                viewController.closeLoadingDialog();
                return false;
            }
        }
        return true;
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
            PlatForm.get().sendMessage(message);
            return; // 方法中已经有提示信息，微信支付失败后，不再做其他操作。
        }
        log.info("==unpaidBalance.toString():" + viewController.unpaidBalance.getText() + "=====wetchatAmount:" + wetchatAmount);
        log.info("微信支付成功！");
        totalWechatAmount += payAmount;
        keyBoard = "";

        if (totalCashAmount + totalWechatAmount >= BaseController.retailTrade.getAmount()) {
            if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) == 0) { //判断是全额支付还是部分支付
                if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                    BaseController.retailTrade.setPaymentType(5);
                } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseController.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                    BaseController.retailTrade.setPaymentType(4);
                } else if (totalCashAmount - BaseController.retailTrade.getAmount() >= 0) {
                    BaseController.retailTrade.setPaymentType(1);
                } else {
                    throw new RuntimeException("错误的支付方式");
                }
                //设置上一单的支付方式
                int paymentType = BaseController.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_cash;
                } else if (paymentType == 4) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_wetchat;
                } else {
                    BaseController.lastRetailTradePaymentType = viewController.pay_combination;
                }
                //设置上一单总金额
                BaseController.lastRetailTradeAmount = BaseController.retailTrade.getAmount();
                BaseController.retailTrade.setAmountCash(totalCashAmount);
                BaseController.retailTrade.setAmountWeChat(totalWechatAmount);
                log.info("===lastRetailTradeAmount:" + BaseController.lastRetailTradeAmount + "===AmountCash:" + BaseController.retailTrade.getAmountCash());
                // 是否使用了优惠券
                if (couponCode != null) {
                    calculateReturnPrice(BaseController.retailTrade, BaseController.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseController.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseController.retailTrade);
                }
                appendRetailTradePromoting(BaseController.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) {

                }
                // 由于现在同步线程是5秒上传一次，和立即上传的时间不会相差太多，所以不需要立即上传会员零售单
//                if (vip != null) {
//                    if (!uploadRetailTrade(handlerMessage)) { //TODO
//                    }
//                }
                updateRetailTradeAggregation();

                BaseController.retailTrade = null;
                System.out.println("零售单已结清，返回主页面");
            }
        }
        message.obj = handlerMessage;
        PlatForm.get().sendMessage(message);
    }

    private boolean wxPayment(Double payAmount, HandlerMessage hm) {
        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(payAmount);
        System.out.println("-------------------------------------------------开始微信支付,支付金额：" + payAmount);
        WXPayInfo wxPayInfo = new WXPayInfo();
        wxPayInfo.setAuth_code(wxPayAuthCode);
        wxPayInfo.setTotal_fee(dPaymentMoney);

        //结算时选择了微信支付，然后断网，在这里进行判断。支付一次失败后（短暂断网），不会将session设置为null，下次支付仍然可以使用微信支付
        if (!NetworkUtils.isNetworkAvalible()) {
            wxPayHttpEvent.setLastErrorCode(EC_InvalidSession);
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
        long lTimeOut = viewController.WxPay_TIME_OUT;
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
        BaseController.retailTrade.setPaymentType(paymentCode);
        //
        // TODO 这里etWechatPayingAmount是文本框输入的金额
        wetchatAmount = Double.parseDouble(viewController.etWechatPayingAmount.getText());
        BaseController.retailTrade.setAmountWeChat(wetchatAmount);
        //
        microPayResponse = wxPayHttpEvent.getMicroPayResponse();
        BaseController.retailTrade.setWxOrderSN(microPayResponse.get("transaction_id") == null ? "" : microPayResponse.get("transaction_id"));
        BaseController.retailTrade.setWxTradeNO(microPayResponse.get("out_trade_no") == null ? "" : microPayResponse.get("out_trade_no"));
        BaseController.retailTrade.setWxRefundSubMchID(microPayResponse.get("sub_mch_id") == null ? "" : microPayResponse.get("sub_mch_id"));
        BaseController.retailTrade.setWxRefundDesc(microPayResponse.get("refund_desc") == null ? "" : microPayResponse.get("refund_desc"));

        return true;
    }

    private void calculateReturnPrice(RetailTrade rt, double fBestAmount) {
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
        double originalTotalAmount = 0.000000d;
        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            originalTotalAmount += rtc.getPriceReturn() * rtc.getNO(); //使用退货价是因为该商品可能参与了促销。使用优惠券后重新计算退货价,需要使用促销后的价格
        }

        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            // 原因是，按照先乘后除的计算顺序可以减少计算误差
            rtc.setPriceReturn(Double.parseDouble(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), fBestAmount), GeneralUtil.mul(originalTotalAmount, rtc.getNO()), 6))));
        }
    }

    private void appendRetailTradeCoupon(RetailTrade retailTrade) {
        RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
        retailTradeCoupon.setRetailTradeID(retailTrade.getID());
        retailTradeCoupon.setID(retailTradeCouponPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradeCoupon.class));
        retailTradeCoupon.setCouponCodeID(couponCode.getID());
        //
        List<BaseModel> list = new ArrayList<BaseModel>();
        list.add(retailTradeCoupon);
        //
        retailTrade.setListSlave3(list);
    }

    private void updateRetailTradePromoting(Coupon coupon, double payAmount, RetailTrade retailTrade) {
        try {
            int tmpRetailTradePromotingFlowIDInSQLite;
            if (retailTradePromoting.getListSlave1() == null) {  //getListSlave1为空的情况下，那么该单是不参与促销，但是使用优惠券
                retailTradePromoting.setListSlave1(new ArrayList<BaseModel>());
            }
            if (retailTradePromoting == null || retailTradePromoting.getListSlave1().size() == 0) {
                int tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromoting.class);
                retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                retailTradePromoting.setTradeID(retailTrade.getID());
                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());

                tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromotingFlow.class);

            } else {
                //从表有数据则说明参与了促销，需要在后面继续累加
                tmpRetailTradePromotingFlowIDInSQLite = ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(retailTradePromoting.getListSlave1().size() - 1)).getID() + 1;
            }
            //
            StringBuilder promotingFlow = new StringBuilder();
            //
            promotingFlow.append("此单使用优惠券前支付价为:")
                    .append(GeneralUtil.formatToCalculate(payAmount))
                    .append("元 \t")
                    .append("优惠券标题为:‘")
                    .append(coupon.getTitle())
                    .append("’\t"); //
            if (coupon.getType() == Coupon.EnumCouponCardType.ECCT_CASH.getIndex()) {
                promotingFlow.append("支付金额满")
                        .append(coupon.getLeastAmount())
                        .append("元减")
                        .append(coupon.getReduceAmount())
                        .append("\t").append("实际支付:")
                        .append(retailTrade.getAmount());
            } else {
                promotingFlow.append("支付金额满")
                        .append(coupon.getLeastAmount())
                        .append("元进行全场")
                        .append(coupon.getDiscount())
                        .append("折优惠")
                        .append("实际微信支付:")
                        .append(retailTrade.getAmount());
            }
            //
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromoting.getID());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            retailTradePromotingFlow.setID(tmpRetailTradePromotingFlowIDInSQLite);
            ((List<RetailTradePromotingFlow>) retailTradePromoting.getListSlave1()).add(retailTradePromotingFlow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void appendRetailTradePromoting(RetailTrade retailTrade) {
        if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
            List<BaseModel> retailTradePromotingList = new ArrayList<BaseModel>();
            retailTradePromotingList.add(retailTradePromoting);
            retailTrade.setListSlave2(retailTradePromotingList);
        }
    }

    /**
     * 全部支付后，在本地创建该零售单
     */
    private boolean createRetailTradeLocally(HandlerMessage hm) {
        if (vip != null) {
            BaseController.retailTrade.setVipID(vip.getID());
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
        BaseController.retailTrade.setShopID((int)pos.getShopID());

        log.info("要创建的零售单为：" + BaseController.retailTrade);
        //如果不是会员消费，先存在本地(先在本地插入RetailTrade，得到临时的TradeID，设置到retailTradePromoting的TradeID中，然后在本地插入retailTradePromoting，得到的临时ID设置到RetailTrade的int2，vip也一样)，等到第一张单的c分钟后，查找本地临时零售单上传到服务器
        RetailTrade rtCreate = (RetailTrade) retailTradeSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, BaseController.retailTrade);
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
        if (BaseController.retailTrade.getListSlave3() != null) {
            List<BaseModel> retailTradeCouponList = (List<BaseModel>) BaseController.retailTrade.getListSlave3();
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
        BaseController.lastRetailTrade = BaseController.retailTrade;
        return true;
    }

    private void updateRetailTradeAggregation() {
        log.info("本地零售单创建成功后,开始更新收银汇总的所有数据");

        // 更新收银汇的所有数据:
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        //未发生变化的数据
        retailTradeAggregation.setID(BaseController.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseController.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseController.retailTradeAggregation.getReserveAmount());
        //发生变化的数据：
        retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTrade.getAmountCash()));/*现金收入*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getWechatAmount(), wetchatAmount));/*微信收入*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*营业额*/
        retailTradeAggregation.setTradeNO(BaseController.retailTradeAggregation.getTradeNO() + 1);/*销售单数*/
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*零售汇总里记录的下班时间为目前零售单的生成时间*/
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("准备更新收银汇总失败！");
        }
        long lTimeOut = viewController.TIME_OUT;
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
            BaseController.retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTrade.getAmountCash()));
            BaseController.retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getWechatAmount(), BaseController.retailTrade.getAmountWeChat()));
            BaseController.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTradeAggregation.getWechatAmount()));
            BaseController.retailTradeAggregation.setTradeNO(BaseController.retailTradeAggregation.getTradeNO() + 1);//本地零售单创建成功后BaseActivity.retailTradeAggregation的TradeNO+1
            BaseController.retailTradeAggregation.setWorkTimeEnd(date);
        }
    }

    /**
     * 现金支付
     */
    private void payViaCash(Double payAmount) {
        isPayingViaCash = true;
        handlerMessage.setErrorCode(EC_NoError);// 初始化消息ErrorCode
        //
        paymentCode = paymentCode | cashPayCode;
        BaseController.retailTrade.setPaymentType(paymentCode);
        cashAmount = payAmount;
        totalCashAmount += cashAmount;
//        cashAmount = GeneralUtil.sum(Double.valueOf(etCashPayingAmount.getText().toString()), payAmount);
        BaseController.retailTrade.setAmountCash(cashAmount); // 设置支付金额，如果有找零的情况，会在下面进行处理
        //计算出支付金额比未付余额大出多少
        Double returnMoney = GeneralUtil.sub(Double.valueOf(getPayingMoney().getText()), Double.valueOf(viewController.unpaidBalance.getText()));
        if (returnMoney > BaseModel.TOLERANCE) {
            handlerMessage.setMsg("支付成功,请使用现金退还" + GeneralUtil.formatToShow(returnMoney));
            keyBoard = "";
        } else {
            handlerMessage.setMsg("支付成功");
            keyBoard = "";
        }
        if (totalCashAmount + totalWechatAmount >= BaseController.retailTrade.getAmount()) {
            if (compareString(getPayingMoney().getText(), viewController.unpaidBalance.getText()) >= 0) { //判断是全额支付还是部分支付
                if (BaseController.retailTrade.getAmount() > RetailTrade.TOLERANCE) {
                    if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                        BaseController.retailTrade.setPaymentType(5);
                    } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseController.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                        BaseController.retailTrade.setPaymentType(4);
                    } else if (totalCashAmount - BaseController.retailTrade.getAmount() >= 0) {
                        BaseController.retailTrade.setPaymentType(1);
                    } else {
                        throw new RuntimeException("错误的支付方式");
                    }
                }

                //设置上一单支付方式
                int paymentType = BaseController.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_cash;
                } else if (paymentType == 4) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_wetchat;
                } else {
                    BaseController.lastRetailTradePaymentType = viewController.pay_combination;
                }
                //设置上一单找零金额
                BaseController.lastRetailTradeChangeMoney = sub(totalCashAmount + totalWechatAmount, BaseController.retailTrade.getAmount());
                //设置上一单总金额
                BaseController.lastRetailTradeAmount = BaseController.retailTrade.getAmount();
                // 现金支付，存在支付金额大于零售单总金额的情况，这个时候需要减去找零金额，当不需要找零时，BaseActivity.lastRetailTradeChangeMoney应该为0
                BaseController.retailTrade.setAmountWeChat(totalWechatAmount);
                BaseController.retailTrade.setAmountCash(sub(totalCashAmount, BaseController.lastRetailTradeChangeMoney));
                log.info("===lastRetailTradeAmount:" + BaseController.lastRetailTradeAmount + "===AmountCash:" + BaseController.retailTrade.getAmountCash());
                // 是否使用了优惠券
                if (couponCode != null) {
                    calculateReturnPrice(BaseController.retailTrade, BaseController.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseController.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseController.retailTrade);
                }
                appendRetailTradePromoting(BaseController.retailTrade);
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
        PlatForm.get().sendMessage(message);
    }

    private void cancelPayment() {
        if (Double.parseDouble(viewController.totalpay_money.getText()) - Double.parseDouble(viewController.unpaidBalance.getText()) > RetailTrade.TOLERANCE) { //已经支付了部分钱
            // 如果存在现金支付，则使用现金退款（存在混合支付和全现金支付的情况） 暂无支付宝的情况
            if (!NetworkUtils.isNetworkAvalible() || totalCashAmount - 0.000000d > RetailTrade.TOLERANCE) {
                ToastUtil.toast("请使用现金退还" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "元!");
            } else if (totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                // 微信支付，优先使用微信退款，微信退款失败，则使用现金退款
                if (wxRefund()) {
                    ToastUtil.toast("微信退款成功, 无需现金退还");
                } else {
                    ToastUtil.toast("微信退款失败，请使用现金退还" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "元!");
                }
            } else {
                //有网、现金支付是0、微信支付是0
                throw new RuntimeException("未处理的分支！");
            }
            //
        } else {
            ToastUtil.toast("还没有进行支付金额，无需退款！");
        }
    }

    private void confirmPaymentClose() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/closePaymentDialog.fxml"));

        JFXAlert alert = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        try {
            alert.setContent((BorderPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        alert.show();

        ClosePaymentDialogController closePaymentDialogController = loader.getController();
        closePaymentDialogController.setAlert(alert);
        closePaymentDialogController.setListener(() -> {
            //弹窗确认按钮点击事件
            cancelPayment();
            resetPayment();
            showCommodity(BaseController.showCommList);
            couponCode = null;
            coupon = null;
            showCouponCodeList.clear();
            alert.close();
        });
    }

    /**
     * 重置交易，不清空待购商品列表
     */
    private void resetPayment() {
        viewController.payment.setVisible(false);//关闭结账页面
        // TODO: 2020/6/19 这个得再布局一下了
        //viewMain1Activity.setVisibility(View.INVISIBLE);
        bPaying = false;
        //
        total_money_double = 0.000000d;
        viewController.total_money.setText("0.00");
        totalWechatAmount = 0.000000d;
        totalCashAmount = 0.000000d;
        viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));
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
        long lTimeOut = viewController.TIME_OUT;
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

    /**
     * 用户设置ReserveAmount后，创建收银汇总到本地sqlite中
     */
    private void createRetailTradeAggregation() {
        BaseController.retailTradeAggregation.setPosID(Constants.posID);
        //准备金被点确定之前，day end 收银汇总 dialog已经弹出来。当dialog消失时，如果不设置work time start为第2天的时间，会导致work time start是前一天的时间。
        //这不符合我们的业务逻辑且在上传时，因为day end dialog结束时已经创建了一个retailTradeAggregation在SQLite中，现在用户点击确定后，又创建了一个retailTradeAggregation。这种情况无法避免但是暂时可以接受。
        //我们规定收银汇总必须是当天的，不可以跨天。
        BaseController.retailTradeAggregation.setWorkTimeStart(DatetimeUtil.addSecond(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), 1));
        BaseController.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseController.retailTradeAggregation.getWorkTimeStart(), 1));
        //
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseController.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseController.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setWorkTimeEnd(BaseController.retailTradeAggregation.getWorkTimeEnd());
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
            @Override
            public void onScanFinish(String scanResult) {
                System.out.println("扫码枪扫到的数据是：" + scanResult);
                if (viewController.payment.isVisible()) { // 支付页面没关闭
                    viewController.showLoadingDialog();
                    // 判断优惠券是否可用
                    if (!validateCouponCodeAvailable()) {
                        return;
                    }
                    if (choosePaymentType.equals(weChatPayString)) {
                        if (scanResult != null && !"".equals(scanResult) && scanResult.length() == LENGTH_WxPayAuthCode) { //目前扫码仅支持微信支付
                            wxPayAuthCode = scanResult;
                            if (wxPayAuthCode != null && !"".equals(wxPayAuthCode)) {
                                if (!bLastWxPayIsNotYetDone && BaseController.retailTrade != null) {
                                    if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText()) || sub(Double.valueOf(viewController.etWechatPayingAmount.getText()), MIN_WechatPaymentAmount) < BaseModel.TOLERANCE) {
                                        viewController.closeLoadingDialog();
                                        ToastUtil.toast("支付金额必须大于0");
                                    } else if ((viewController.etWechatPayingAmount.getText().length() - viewController.etWechatPayingAmount.getText().indexOf(".")) == 1) {
                                        viewController.closeLoadingDialog();
                                        ToastUtil.toast("输入金额无效");
                                    } else {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                payViaWX(Double.valueOf(getPayingMoney().getText()));
                                            }
                                        }).start();
                                    }
                                }
                            }
                        } else {
                            viewController.closeLoadingDialog();
                            ToastUtil.toast("错误的付款码，请重新扫码！");
                        }
                    } else {
                        viewController.closeLoadingDialog();
                        if (totalWechatAmount > BaseModel.TOLERANCE) {
                            ToastUtil.toast("一张零售单只能微信收款1次，这张单已使用过微信收款");
                        } else {
                            ToastUtil.toast("现金收款，扫码无效！请手工输入顾客付款金额");
                        }
                    }
                } else {
                    //扫条形码
                    log.info("扫码枪扫描到的数据：" + scanResult);
                    if (StringUtils.isEmpty(scanResult) || scanResult.length() <= BaseController.FUZZY_QUERY_LENGTH) {
                        ToastUtil.toast("扫描到的条码为空或者条码数字少于" + (BaseController.FUZZY_QUERY_LENGTH + 1) + "位");
                        log.info("扫描到的条码为空或者条码数字少于" + (BaseController.FUZZY_QUERY_LENGTH + 1) + "位");
                        return;
                    }

                    if (scanResult.length() == Vip.Max_LENGTH_VipCardSN && scanResult.substring(0, 6).equals(Constants.MyCompanySN)) {
                        viewController.showClientPhone.setText(scanResult);
                    } else {
                        commodityListAfterQuery.clear();
                        viewController.scan_barcode_text.setText(scanResult);
                        commodityListAfterQuery = retrieveNCommodityInSQLite(true, scanResult);
                        log.info("根据条形码查出数据：" + commodityListAfterQuery.size() + "扫码枪扫描到的数据：" + scanResult);
                        log.info("showCommList：" + BaseController.showCommList);
                        if (commodityListAfterQuery == null || commodityListAfterQuery.size() == 0) {
                            ToastUtil.toast("未找到商品");
                            viewController.scan_barcode_text.setText("");
                        } else {
                            //搜到的商品多于一个，或只有一个但是和scanResult不完全一致
                            if (commodityListAfterQuery.size() > 1 || !commodityListAfterQuery.get(0).getBarcode().equals(scanResult)) {

                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/ui/chooseCommodityDialog.fxml"));

                                JFXAlert alert = new JFXAlert();
                                alert.initModality(Modality.APPLICATION_MODAL);
                                alert.setOverlayClose(true);
                                try {
                                    alert.setContent((BorderPane) loader.load());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                alert.show();

                                ChooseCommodityDialogViewController controller = loader.getController();
                                controller.setSearchCommodityEt(scanResult);
                                controller.setAlert(alert);
                                controller.setDeleteAndCancelListener(new ChooseCommodityDialogViewController.DeleteAndCancelListener() {
                                    @Override
                                    public void deleteAll() {

                                    }

                                    @Override
                                    public void cancel() {
                                        commodityListAfterQuery.clear();
                                    }

                                    @Override
                                    public void add() {
                                        Commodity dialogCommodity = new Commodity();
                                        dialogCommodity.setID(dialogCommodityRecyclerViewAdapter.getSelectCommody().getID());
                                        dialogCommodity.setBarcode(dialogCommodityRecyclerViewAdapter.getSelectCommody().getBarcode());
                                        //
                                        isCommodityExistInUI(dialogCommodity);
                                        //
                                        showCommodity(BaseController.showCommList);
                                    }
                                });
                                controller.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                                controller.rv_alertdialog_commodity.setCellFactory(param -> {
                                    dialogCommodityRecyclerViewAdapter = new DialogCommodityListCellAdapter();
                                    return dialogCommodityRecyclerViewAdapter;
                                });
                                controller.rv_alertdialog_commodity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                                controller.rv_alertdialog_commodity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                                    dialogCommodityRecyclerViewAdapter.setSelectCommody(newValue);
                                    //todo 点击变色
                                });

                            } else { //只有一个候选商品，且它的条形码和scanResult完全一致
                                Commodity commodity = new Commodity();
                                commodity.setID(commodityListAfterQuery.get(0).getID());
                                commodity.setBarcode(commodityListAfterQuery.get(0).getBarcode());
                                //
                                isCommodityExistInUI(commodity);
                                //
                                showCommodity(BaseController.showCommList);
                            }
                        }
                    }
                }
            }
        });
        // 查找属于该门店的促销
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("查询促销信息失！", ToastUtil.LENGTH_SHORT);
            return;
        }
        // 从其它Activity退回来时，或许需要重新计算结算金额（包含优惠）。
        retailTradePromoting = new RetailTradePromoting();
        BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
        if (BaseController.retailTrade != null) {
            viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        } else {
            viewController.total_money.setText("0.00");
        }
        viewController.last_amount.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeAmount));
        viewController.last_changemoney.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeChangeMoney));
        viewController.last_paymenttype.setText(BaseController.lastRetailTradePaymentType);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                break;
            case 2:
                viewController.closeLoadingDialog();
                viewController.unpaidBalance.setText(viewController.unpaidBalance.getText());
                getPayingMoney().setText(viewController.unpaidBalance.getText());
                viewController.changeMoney.setText(viewController.changeMoney.getText());
                ToastUtil.toast("支付失败！", ToastUtil.LENGTH_SHORT);
                break;
            case 3: // 微信支付相关UI操作
                HandlerMessage mth = (HandlerMessage) msg.obj;
                switch (mth.getErrorCode()) {
                    case EC_NoError:
                        ToastUtil.toast("支付成功", ToastUtil.LENGTH_SHORT);
                        if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) == 0) { //判断是全额支付还是部分支付
                            viewController.pay.setText("支付完成");
                            viewController.pay.setDisable(true);
                            viewController.pay.setStyle("-fx-background-color: #888888;");
                            //打印小票
                            try {
                                printSmallSheet(BaseController.lastRetailTrade);
                            } catch (Exception e) {
                                log.info("打印小票异常：" + e.getMessage());
                            }
                            // TODO 支付完成。。。
                            resetTradeAfterPay(1);
                            initializeVipData();
                        } else {
                            viewController.unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseController.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
                            getPayingMoney().setText(viewController.unpaidBalance.getText());
                            //一个订单不允许扫码俩次，所以支付部分金额后选为现金支付；
                            cashPay_click();
                            viewController.wechatPay.setDisable(true);
                            viewController.spinner.setDisable(true);
                        }
                        break;
                    case EC_InvalidSession:
                        ToastUtil.toast(BaseHttpBO.ERROR_MSG_Network, ToastUtil.LENGTH_SHORT);
                        changeRadioButtonToPayViaCashOnly();
                        break;
                    case EC_WechatServerError:
                        ToastUtil.toast("微信支付失败，" + (mth.getMsg() == null ? "" : mth.getMsg()) + "请稍后重试或者换其他的支付方式。", ToastUtil.LENGTH_SHORT);
                        break;
                    case EC_OtherError:
                        switch (mth.getSubErrorCode()) {
                            case SubErrorCode_RetailTrade_OtherError:
                                ToastUtil.toast("创建零售单失败！", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradePromoting_OtherError:
                                ToastUtil.toast("创建促销计算过程失败！", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradeCoupon_OtherError:
                                ToastUtil.toast("创建零售单优惠券使用失败！", ToastUtil.LENGTH_SHORT);
                                break;
                        }
                        break;
                    case EC_Timeout:
                        ToastUtil.toast("微信支付超时!", ToastUtil.LENGTH_SHORT);
                        changeRadioButtonToPayViaCashOnly();
                        break;
                }
                bLastWxPayIsNotYetDone = false;
                viewController.closeLoadingDialog();
                break;
            case 4: // 现金支付相关UI操作
                HandlerMessage mthCash = (HandlerMessage) msg.obj;
                switch (mthCash.getErrorCode()) {
                    case EC_NoError:
                        ToastUtil.toast(mthCash.getMsg(), ToastUtil.LENGTH_SHORT);
                        if (compareString(getPayingMoney().getText(), viewController.unpaidBalance.getText()) >= 0) { //判断是全额支付还是部分支付
                            viewController.pay.setText("支付完成");
                            viewController.pay.setDisable(true);
                            // 保存最后一张非退货型零售单的数据，以便首页能打印上一单
                            BaseController.lastRetailTrade = BaseController.retailTrade;
                            //打印小票
                            try {
                                printSmallSheet(BaseController.lastRetailTrade);
                            } catch (Exception e) {
                                log.info("打印小票异常：" + e.getMessage());
                            }
                            BaseController.retailTrade = null;
                            System.out.println("零售单已结清，返回主页面");
                            // 支付完成，跳转页面
                            // TODO 支付完成
                            // 当次为现金支付，已支付金额为多少，未支付金额为多少，找零多少
                            // 如果已支付金额还小于应付金额，需要进行下一次支付
                            // 如果已支付金额已经等于应付金额，则关闭支付界面，清空BaseActivity.showCommodity
                            resetTradeAfterPay(1);
                            initializeVipData();
                        } else {
                            viewController.unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseController.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
                            getPayingMoney().setText(viewController.unpaidBalance.getText());
                            viewController.spinner.setDisable(true);
                        }
                        break;
                    case EC_OtherError:
                        switch (mthCash.getSubErrorCode()) {
                            case SubErrorCode_RetailTrade_OtherError:
                                ToastUtil.toast("创建零售单失败！", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradePromoting_OtherError:
                                ToastUtil.toast("创建促销计算过程失败！", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradeCoupon_OtherError:
                                ToastUtil.toast("创建零售单优惠券使用失败！", ToastUtil.LENGTH_SHORT);
                                break;
                        }
                        break;
                }
                isPayingViaCash = false;
                viewController.closeLoadingDialog();
                break;
            case 5: // 查找会员后的处理
                ErrorInfo errorInfo = new ErrorInfo();
                do {
                    if (vipHttpEvent.getLastErrorCode() == EC_NoError) {
                        vip = (Vip) vipHttpEvent.getBaseModel1();
                        if (vip != null) {
                            // ...渲染VIP消息
                            log.debug("查找到的会员为:" + vip);
                            viewController.showClientPhone.setVisible(false);
                            viewController.showClientPhone.setManaged(false);
                            viewController.search_vip_button.setVisible(false);
                            viewController.search_vip_button.setManaged(false);
                            //
                            viewController.vipMobile.setText("会员手机号：" + vip.getMobile());
                            viewController.vipBonus.setText("积分：" + vip.getBonus());
                            viewController.vipMobile.setVisible(true);
                            viewController.vipMobile.setManaged(true);
                            viewController.vipBonus.setVisible(true);
                            viewController.vipBonus.setManaged(true);
                            viewController.return_search_vip_button.setVisible(true);
                            viewController.return_search_vip_button.setManaged(true);

                            // 查找此会员相关的优惠券
                            CouponCode couponCode = new CouponCode();
                            couponCode.setVipID(vip.getID().intValue());
                            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                                errorInfo.setErrorMessage("查找会员的优惠券失败，原因：" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage());
                                ToastUtil.toast("查找会员的优惠券失败", ToastUtil.LENGTH_SHORT);
                                break;
                            }
                            ToastUtil.toast("查找会员成功，正在查找会员的优惠券...", ToastUtil.LENGTH_SHORT);
                        } else {
                            errorInfo.setErrorMessage("查找不到相关会员");
                            ToastUtil.toast("查找不到相关会员", ToastUtil.LENGTH_SHORT);
                            break;
                        }
                    } else {
                        errorInfo.setErrorMessage("查找会员失败");
                        ToastUtil.toast("查找会员失败", ToastUtil.LENGTH_SHORT);
                        break;
                    }
                } while (false);
                if (errorInfo.getErrorMessage() != null) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast(errorInfo.getErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
                break;
            case 6:
                if (couponCodeHttpEvent.getLastErrorCode() == EC_NoError) {
                    couponCodeList = (List<CouponCode>) couponCodeHttpEvent.getListMasterTable();
                    ToastUtil.toast("查找会员的优惠券成功", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("查找会员的优惠券失败，原因：" + couponCodeHttpEvent.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
                viewController.closeLoadingDialog();
                break;
        }
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
     * 打印小票。
     */
    public void printSmallSheet(RetailTrade retailTrade) {
        if (viewController.currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            viewController.currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (viewController.currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            ToastUtil.toast("无法打印小票，加载小票格式失败", ToastUtil.LENGTH_SHORT);
            return;
        }
        //
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID((viewController.currentSmallSheetID));
        SmallSheetFrame smallSheetFrame1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && smallSheetFrame1 == null) {
            log.error("smallSheetFramePresenter.retrieve1ObjectSync失败,错误码=" + smallSheetFramePresenter.getLastErrorCode());
            ToastUtil.toast("无法打印小票，加载小票格式失败", ToastUtil.LENGTH_SHORT);
            return;
        }

        List<SmallSheetText> listSmallSheetText = (List<SmallSheetText>) smallSheetFrame1.getListSlave1();

        //退货单需要在小票上进行标注  居中，1.5倍字号
        if (retailTrade.getSourceID() > 0) {
            AidlUtil.getInstance().printText("退货单",
                    1,//字号30
                    true,//加粗
                    17,//居中
                    false);//无下划线
            AidlUtil.getInstance().linewrap(1);//分隔一行
        }

        //每一行判断控件的状态进行设置
        if (smallSheetFrame1.getLogo() != null && !smallSheetFrame1.getLogo().equals("")) {
            // TODO: 2020/6/22 打印图片需要地址
//            AidlUtil.getInstance().printBitmap(GeneralUtil.stringToBitmap(smallSheetFrame1.getLogo()));
            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        }

        if (!"".equals(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent())) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getContent(), //
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getSize(), //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getBold() == 1, //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getGravity(), //
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()).getFrameId() == 1);
            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        }
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getContent() + retailTrade.getSn(), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()).getFrameId() == 1);
        // 时间格式转换
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getContent() + sdf.format(retailTrade.getSaleDatetime()), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        String[] saCommodityAttribute = new String[]{"商品名称", "数量", "小计"};
        String[][] listCommodity = null;// 商品的信息列表
        //
        List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        if (listRtc == null) {
            ToastUtil.toast("零售单异常，没有商品信息", ToastUtil.LENGTH_SHORT);
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
        AidlUtil.getInstance().printTable(saCommodityAttribute);
        for (int j = 0; j < i; j++) {
            AidlUtil.getInstance().printTable(listCommodity[j]);
        }
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getContent() + GeneralUtil.formatToShow(totalPriceOriginal), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()).getFrameId() == 1);
        //
        String retailTradePaymentType = null;
        if (retailTrade.getPaymentType() == 1) {
            retailTradePaymentType = viewController.pay_cash;
        } else if (retailTrade.getPaymentType() == 2) {
            retailTradePaymentType = viewController.pay_aliPay;
        } else if (retailTrade.getPaymentType() == 4) {
            retailTradePaymentType = viewController.pay_wetchat;
        } else {
            retailTradePaymentType = viewController.pay_combination;
        }
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getContent() + retailTradePaymentType, //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getContent() + GeneralUtil.formatToShow(GeneralUtil.sub(totalPriceOriginal, retailTrade.getAmount())), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getBold() == 1,//
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmount()), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountWeChat()),
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getContent() + GeneralUtil.formatToShow(retailTrade.getAmountCash()),
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getSize(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getBold() == 1,
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getGravity(),
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()).getFrameId() == 1);
        //
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()).getFrameId() == 1);
        }
        if (listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent() != null && !listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent().equals("")) {
            AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getContent(),
                    (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getSize(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getBold() == 1,
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getGravity(),
                    listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()).getFrameId() == 1);
        }

        AidlUtil.getInstance().linewrap(smallSheetFrame1.getCountOfBlankLineAtBottom());
        AidlUtil.getInstance().cutPaper();
    }

    /**
     * 获取本地当前小票格式，如果没有，则使用默认的小票格式。
     *
     * @return currentSmallSheetID
     * BaseSQLiteBO.INVALID_INT_ID，DB异常；其它值，默认的小票格式
     */
    public int retrieveCurrentSmallSheetIDFromConfig() {
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setID(ConfigGeneral.ACTIVE_SMALLSHEET_ID);
        ConfigGeneral configGeneralFromDB = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        if (configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && configGeneralFromDB == null) {
            log.error("configGeneralPresenter.retrieve1ObjectSync()失败,错误码=" + configGeneralPresenter.getLastErrorCode());
//            Toast.makeText(getApplicationContext(), "无法查找当前小票格式的配置，读取配置失败", Toast.LENGTH_SHORT).show(); //运行在非UI线程

            return BaseSQLiteBO.INVALID_INT_ID;
        }

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != '%s'");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("查找本地的小票格式失败！！");

            return BaseSQLiteBO.INVALID_INT_ID;
        }
        if (ssfList.size() == 0) { // 没有小票格式
            return BaseSQLiteBO.INVALID_INT_ID;
        }

        for (SmallSheetFrame ssf : ssfList) {
            if (ssf.getID() == Integer.valueOf(configGeneralFromDB.getValue())) {
                return Integer.valueOf(configGeneralFromDB.getValue());
            }
        }
        // 如果断网情况下删除当前小票，本地的当前小票会更新成默认的（1），但是不会上传到服务器，如果关掉app，下次登录同步服务器的配置，那么当前小票还是原来的，但是原来的已经被删除了，所以需要更新配置的当前小票格式为默认的
        configGeneralFromDB.setValue(String.valueOf(SmallSheetController.Default_SmallSheetID_INPos));
        configGeneralFromDB.setReturnObject(1); //...
        viewController.configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!viewController.configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
            log.error("更新默认小票格式失败！configGeneral=" + configGeneralFromDB);
        }
        return Integer.valueOf(String.valueOf(SmallSheetController.Default_SmallSheetID_INPos));
    }

    class EnlargeHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            scanGun.isMaybeScanning(event);
        }
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
            pShopScopeRnCondition.setSql("where F_PromotionID = %s");
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
