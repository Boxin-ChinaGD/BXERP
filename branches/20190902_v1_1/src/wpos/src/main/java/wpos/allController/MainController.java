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

    private final String weChatPayString = "????????????";
    private final String cashPayString = "????????????";
    private String choosePaymentType;
    //??????????????????
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
     * UI?????????????????????????????????????????????WxPay_TIME_OUT??????60???????????????????????????????????????????????????????????????UI?????????????????????
     */
    protected final long TIME_OUT = 30;

    public MainController() {

    }

    public void setAllFragmentViewController(BaseController c) {
        viewController = (AllFragmentViewController) c;
        onCreate();
    }

    //    @FXML   //????????????
    public void balance_click() {
        // ???????????????????????????
        if (GlobalController.getInstance().getSessionID() == null || (!NetworkUtils.isNetworkAvalible())) { // xxx ?????????????????????
            changeRadioButtonToPayViaCashOnly();
            paymentCode = 0b00000000;
            viewController.pay.setText("???  ???");
            viewController.pay.setDisable(false);
        } else {
            changeRadioButtonToPayViaWX();
        }
        balance();
    }

    // ????????????
    public void holdBill_click() {
        // ??????????????????????????????list??????????????????????????????????????????
        if (BaseController.retailTrade != null && BaseController.retailTrade.getListSlave1() != null && BaseController.retailTrade.getListSlave1().size() > 0) {
            if (BaseController.retailTradeHoldBillList.size() >= BaseController.MAX_HOLD_BILL_NO) {
                ToastUtil.toast("?????????????????????????????????" + BaseController.MAX_HOLD_BILL_NO + "?????????????????????????????????", ToastUtil.LENGTH_SHORT);
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
                ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
            }
        } else {
            ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    //    @FXML   //??????????????????
    public void paymentClose_click() {
        confirmPaymentClose();
    }

    //    @FXML   //???????????????????????????
    public void cashPay_click() {
        viewController.cashPay.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3;");
        viewController.cash_pay_text.setStyle("-fx-text-fill: #2196F3;-fx-background-color: #E3F2FD;");
        viewController.wechatPay.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #9E9E9E;");
        viewController.weChat_pay_text.setStyle("-fx-text-fill: #263238;-fx-background-color: #F5F5F5;");
        //????????????
        this.selectItem(cashPayString);
    }

    //    @FXML   //???????????????????????????
    public void wechatPay_click() {
        viewController.cashPay.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #9E9E9E;");
        viewController.cash_pay_text.setStyle("-fx-text-fill: #263238;-fx-background-color: #F5F5F5;");
        viewController.wechatPay.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #2196F3;");
        viewController.weChat_pay_text.setStyle("-fx-text-fill: #2196F3;-fx-background-color: #E3F2FD;");
        //
        this.selectItem(weChatPayString);
    }

    //    @FXML   //????????????????????????
    public void scanBarcodeText_click() {
        //?????????????????????
        searchCommodityByBarcode();
    }

    //    @FXML   //????????????????????????
    public void scan_barcode_search() {
        //?????????????????????
        searchCommodityByBarcode();
    }

    //??????vip??????
    public void searchVip_click() {
        //?????????????????????????????????????????????
        String queryKeyword = viewController.showClientPhone.getText();
        System.out.println("????????????" + queryKeyword);
        //???????????????????????????
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
            if (GlobalController.getInstance().getSessionID() != null && NetworkUtils.isNetworkAvalible()) { // ????????????
                if (!vipHttpBO.retrieveNByMobileOrVipCardSNEx(vip)) {
                    viewController.closeLoadingDialog();
                    ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
                    log.error("?????????????????????" + queryKeyword);
                }
            } else {   // ??????
                ToastUtil.toast(BaseHttpBO.ERROR_MSG_Network, ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
            }
        } else {
            System.out.println("??????");
            ToastUtil.toast(Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN, ToastUtil.LENGTH_SHORT);
        }
    }

    //??????vip????????????
    public void return_searchVip_click() {
        initializeVipData();
    }

    //    @FXML   //??????????????????
    public void pay_click() {
        viewController.showLoadingDialog();
        if (!isPayingViaCash && BaseController.retailTrade != null) {
            // ???????????????????????????
            if (!validateCouponCodeAvailable()) {
                return;
            }
            if (choosePaymentType.equals(weChatPayString)) { // ????????????WX??????
                if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText()) || Double.parseDouble(viewController.etWechatPayingAmount.getText()) <= 0) {
                    ToastUtil.toast("??????????????????????????????0");
                    viewController.closeLoadingDialog();
                } else if ((viewController.etWechatPayingAmount.getText().length() - viewController.etWechatPayingAmount.getText().indexOf(".")) == 1) {
                    ToastUtil.toast("??????????????????");
                    viewController.closeLoadingDialog();
                } else {
                    //todo ?????????????????????????????????
                    new Thread(() -> payViaWX(Double.valueOf(getPayingMoney().getText()))).start();
                }
            } else if (choosePaymentType.equals(cashPayString)) { // ????????????
                if (StringUtils.isEmpty(viewController.etCashPayingAmount.getText()) || Double.valueOf(viewController.etCashPayingAmount.getText()) < 0) {
                    ToastUtil.toast("?????????????????????????????????0");
                    viewController.closeLoadingDialog();
                } else if ((viewController.etCashPayingAmount.getText().length() - viewController.etCashPayingAmount.getText().indexOf(".")) == 1) {
                    ToastUtil.toast("??????????????????");
                    viewController.closeLoadingDialog();
                } else {
                    //todo ?????????????????????????????????
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
    private final static String DEFAULT_changeAmount = "0.00"; //?????????????????????????????????0.00

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
    // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    public static boolean firstTimeEnterMain1Activity = true;
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
    private double total_money_double = 0.000000d;//???????????????double??????????????????????????????????????????????????????SQLite???????????????????????????????????????
    private RetailTradePromoting retailTradePromoting;
    //??????????????????
    private String wxOutRefundNO;
    //????????????????????????
    private String wxOutRefundErrorMsg;
    public static boolean bPaying = false;
    public static final String MAX_CashPaymentAmount = "99999";
    private final static int LENGTH_WxPayAuthCode = 18;
    private final static double MIN_WechatPaymentAmount = 0.00; // ????????????????????????????????????
    // ?????????????????????????????????????????????
    private int Default_CommodityMaxNumber = 9999;
    private int paymentCode = 0b00000000;//?????????????????????????????????????????????paymentType?????????paymentCode + ????????????????????????????????????
    private final int wechatPayCode = 0b00000100;//?????????????????????????????????????????????
    private final int cashPayCode = 0b00000001;
    private CommodityListCellAdapter commodityRecyclerViewAdapter;
    private DialogCommodityListCellAdapter dialogCommodityRecyclerViewAdapter;

    private double totalWechatAmount;
    private double totalCashAmount;
    private boolean bCanPayViaCashOnly = false;

    /**
     * ???????????????????????????????????????????????????
     */
    private double wetchatAmount;//
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

    /**
     * ????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
    private boolean bLastWxPayIsNotYetDone = false;

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private static boolean isPayingViaCash = false;

    private Map<String, String> microPayResponse; // ??????????????????
    /**
     * ???????????????????????????????????????????????????
     */
    private double cashAmount;//

    private final String COUPONTITLELIST_DEFAULT_UnavailableConpon = "?????????????????????";
    private final String COUPONTITLELIST_DEFAULT_AvailableConpon = "?????????????????????";

    private String wxPayAuthCode = ""; // ?????????????????????

    //?????????????????????
    private JFXAlert chooseCommodityDialog;
    private ChooseCommodityDialogViewController chooseCommodityDialogViewController;

    /**
     * ???????????????????????????
     */
    final int[] POSITION_CommodityToRemove = {BaseSQLiteBO.INVALID_INT_ID};

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
        } else {
            log.info("????????????Event???ID=" + event.getId() + "syncThread");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            Message message = new Message();
            message.what = 5;
            PlatForm.get().sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVipSQLiteEvent(VipSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync) {
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    BaseController.retailTradeAggregation.setID(event.getBaseModel1().getID());
                    System.out.println("???????????????????????????????????????????????????=" + BaseController.retailTradeAggregation);
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
        if (event.getId() == BaseEvent.EVENT_ID_MainStage) {
            event.onEvent();
            Message message = new Message();
            message.what = 6;
            PlatForm.get().sendMessage(message);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            viewController.vipMobile.setText("??????????????????" + vip.getMobile());
            viewController.vipBonus.setText("?????????" + vip.getBonus());
            viewController.vipMobile.setVisible(true);
            viewController.vipMobile.setManaged(true);
            viewController.vipBonus.setVisible(true);
            viewController.vipBonus.setManaged(true);
            viewController.return_search_vip_button.setVisible(true);
            viewController.return_search_vip_button.setManaged(true);
            // ?????????????????????????????????
            CouponCode couponCode = new CouponCode();
            couponCode.setVipID(vip.getID().intValue());
            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                ToastUtil.toast("??????????????????????????????", ToastUtil.LENGTH_SHORT);
                viewController.closeLoadingDialog();
            }
        }
    }

    private void onCreate() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);

        initEventAndBO();

        if (firstTimeEnterMain1Activity) {
            //?????????????????????
            showReserveDialog();
            viewController.initTimers();
            firstTimeEnterMain1Activity = false;
        }

        //?????????????????????????????????????????????
        initScanGunListener(viewController.sale_Pane);

        initializeVipData();
        // ???????????????????????????
        onBackFromBillFragment();
        initScanGun();

        // Fragment?????????????????????????????????????????????
        if (BaseController.showCommList.size() > 0) {
            showCommodity(BaseController.showCommList);
        }

        viewController.rvCommodity.requestFocus();
        System.out.println("???????????????" + BaseController.retailTradeAggregation.hashCode());
    }

    //??????????????????
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

    //????????????????????????
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
                ToastUtil.toast("??????????????????????????????????????????????????????");
            } else if ("".equals(s)) {
                ToastUtil.toast("????????????????????????");
            } else if (Double.parseDouble(s) > 10000d) {
                ToastUtil.toast("??????????????????????????????10000");
            } else if (s.startsWith(".")) {
                ToastUtil.toast("????????????????????????????????????????????????????????????");
            } else {
                BaseController.retailTradeAggregation.setReserveAmount(Double.parseDouble(s));
                createRetailTradeAggregation(); //TODO ?????????????????????????????????
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
        viewController.vipMobile.setText("??????????????????");
        viewController.vipBonus.setText("?????????");
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
     * TODO ?????????????????????????????????????????????????????????
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
                    log.error("????????????????????????????????????????????????????????????");
                    continue;
                }
                if (queryCoupon != null) {
                    double amount = 0.000000d;
                    // ?????????????????????????????????,???????????????????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
     * ??????????????????????????????????????????????????????UI
     */
    private void resetTradeAfterHoldBill() {
        BaseController.showCommList.clear();
        showCommodity(BaseController.showCommList);
        total_money_double = 0.000000d;
        viewController.commodityQuantity.setText("???????????????" + "0" + " ???");//?????????????????????UI
        retailTradePromoting = new RetailTradePromoting();// ??????????????????
        BaseController.retailTrade = null;

        // ???????????????
        viewController.last_amount.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeAmount));
        viewController.last_changemoney.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeChangeMoney));
        viewController.last_paymenttype.setText(BaseController.lastRetailTradePaymentType);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param isPaid ??????????????????
     */
    private void resetTradeAfterPay(int isPaid) {
        //??????????????????
        viewController.payment.setVisible(false);
        viewController.payment.setManaged(false);
        bPaying = false;
        //
        if (isPaid == 1) {
            BaseController.showCommList.clear();
            showCommodity(BaseController.showCommList);
            total_money_double = 0.000000d;
            viewController.commodityQuantity.setText("???????????????" + "0" + " ???");//?????????????????????UI
            retailTradePromoting = new RetailTradePromoting();// ??????????????????
            BaseController.retailTrade = null;
            viewController.total_money.setText("0.00");
            totalWechatAmount = 0.000000d;
            totalCashAmount = 0.000000d;
            viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));

            // ???????????????
            viewController.last_amount.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeAmount));
            viewController.last_changemoney.setText(GeneralUtil.formatToShow(BaseController.lastRetailTradeChangeMoney));
            viewController.last_paymenttype.setText(BaseController.lastRetailTradePaymentType);
        } else {
            // ?????????Activity????????????????????????????????????????????????????????????????????????
            System.out.println("???????????????????????????");
        }
    }

    public void showCommodity(final List<Commodity> list) {
        viewController.scan_barcode_text.setText("");
        viewController.scan_barcode_text.setPromptText("??????????????????Ctrl+Q???");
        //??????????????????????????????UI??????
        int iCommodityQuantity = 0;
        for (Commodity commodity : list) {
            iCommodityQuantity += commodity.getCommodityQuantity();
        }
        viewController.commodityQuantity.setText("???????????????" + iCommodityQuantity + " ???");

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
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
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

    //?????????????????????????????????????????????????????????
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
                //??????????????????????????????
                controller.setListener(() -> {
                    if (POSITION_CommodityToRemove[0] != BaseSQLiteBO.INVALID_INT_ID) {
                        list.remove(POSITION_CommodityToRemove[0]);
                        // ??????????????????????????????
                        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                        if(avaliablePromotionList == null) {
                            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
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
                        //???remove????????????????????????????????????remove????????????????????????????????????????????????????????????????????????????????????
                        mAdapter.setOnClickListener(new CommodityListCellAdapter.onClickListener() {
                            @Override
                            public void onRightClick() {
                                //todo ??????item?????????????????????
                            }

                            @Override
                            public void onLeftClick(Commodity newValue) {

                            }
                        });
                        mainCommodityRecyclerViewItemClick(mAdapter, list);
                        //??????????????????????????????UI??????
                        int iCommodityQuantity = 0;
                        for (int i = 0; i < list.size(); i++) {
                            iCommodityQuantity += list.get(i).getCommodityQuantity();
                        }
                        viewController.commodityQuantity.setText("???????????????" + iCommodityQuantity + " ???");
                        POSITION_CommodityToRemove[0] = -1;
                    } else {
                        ToastUtil.toast("???????????????");
                    }
                    // ??????????????????????????????
                    List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
                    if(avaliablePromotionList == null) {
                        ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
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
                //todo ??????item?????????????????????
                log.info("???????????????" + newValue.toString());
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
            //?????????????????????????????????????????????????????????recyclerview,???????????????????????????????????????
            list.get(position).setCommodityQuantity(Integer.parseInt(commodityNum));
            list.get(position).setSubtotal(list.get(position).getCommodityQuantity() * list.get(position).getAfter_discount());
            // ??????????????????????????????
            List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
            if(avaliablePromotionList == null) {
                ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                return;
            }
            retailTradePromoting = new RetailTradePromoting();
            BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
            if (BaseController.retailTrade != null) {
                viewController.total_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
            } else {
                viewController.total_money.setText("0.00");
            }
            //todo ????????????

            //todo ????????????????????????????????????????????????????????????
            viewController.rvCommodity.setItems(null);
            viewController.rvCommodity.setItems(FXCollections.observableList(list));

            //??????????????????????????????UI??????
            int iCommodityQuantity = 0;
            for (int i = 0; i < list.size(); i++) {
                iCommodityQuantity += list.get(i).getCommodityQuantity();
            }
            viewController.commodityQuantity.setText("???????????????" + iCommodityQuantity + "???");
        });
        inputNumberDialog.show();
    }

    private void changeRadioButtonToPayViaCashOnly() {
        log.info("??????RadioButton?????????,???????????????:" + bCanPayViaCashOnly);
        if (!bCanPayViaCashOnly) {
            cashPay_click(); // ???????????????????????????
            viewController.wechatPay.setDisable(true);// ???????????????????????????????????????
            bCanPayViaCashOnly = true; // ??????????????????RadionButton

            viewController.tvWechatPaying.setVisible(false);
            viewController.etWechatPayingAmount.setManaged(false);

            viewController.tvCashPaying.setVisible(true);
            viewController.etCashPayingAmount.setVisible(true);
        }
    }

    //?????????????????????
    private void searchCommodityByBarcode() {
        log.debug("????????????????????????");
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
            //???????????????
            chooseCommodityDialogViewController.setTextChangeListener(commodityBarcodes -> {
                log.debug("?????????????????????????????????????????????" + commodityBarcodes);
                commodityListAfterQuery.clear();
                commodityListAfterQuery = retrieveNCommodityInSQLite(true, commodityBarcodes);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                //????????????item????????????
                chooseCommodityDialogViewController.rv_alertdialog_commodity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                chooseCommodityDialogViewController.rv_alertdialog_commodity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    dialogCommodityRecyclerViewAdapter.setSelectCommody(newValue);
                    //todo ??????item?????????????????????
                });
            });
            //????????????????????????????????????????????????????????????
            chooseCommodityDialogViewController.setDeleteAndCancelListener(new ChooseCommodityDialogViewController.DeleteAndCancelListener() {
                @Override   //????????????????????????????????????
                public void deleteAll() {
                    commodityListAfterQuery.clear();
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                }

                @Override   //?????????????????????
                public void cancel() {
                    chooseCommodityDialogViewController.search_commodity_et.setText("");
                    commodityListAfterQuery.clear();
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                    chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                    //????????????????????????????????????????????????
                    viewController.rvCommodity.requestFocus();
                }

                @Override   //?????????????????????
                public void add() {
                    if (commodityListAfterQuery.size() > 0) {
                        //?????????????????????
                        if (dialogCommodityRecyclerViewAdapter.getSelectCommody() != null) {
                            Commodity dialogCommodity = new Commodity();
                            dialogCommodity.setID(dialogCommodityRecyclerViewAdapter.getSelectCommody().getID());
                            dialogCommodity.setBarcode(dialogCommodityRecyclerViewAdapter.getSelectCommody().getBarcode());
                            //
                            isCommodityExistInUI(dialogCommodity);
                            viewController.scan_barcode_text.setPromptText("????????????" + dialogCommodityRecyclerViewAdapter.getSelectCommody().getBarcode());
                            //
                            chooseCommodityDialog.hideWithAnimation();
                            //
                            showCommodity(BaseController.showCommList);
                            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            // TODO: 2020/6/16 ????????????

                            //???????????????null,????????????????????????????????????????????????
                            chooseCommodityDialogViewController.search_commodity_et.setText("");
                            commodityListAfterQuery.clear();
                            chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(null);
                            chooseCommodityDialogViewController.rv_alertdialog_commodity.setItems(FXCollections.observableList(commodityListAfterQuery));
                            //????????????????????????????????????????????????
                            viewController.rvCommodity.requestFocus();
                        } else {
                            ToastUtil.toast("???????????????", ToastUtil.LENGTH_SHORT);
                        }
                    } else {
                        ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                    }
                }
            });
            chooseCommodityDialogViewController.setSearchCommodityEt(viewController.scan_barcode_text.getText());
        }
        chooseCommodityDialog.show();
        chooseCommodityDialogViewController.requestFocus();
    }

    /**
     * ?????????Commodity?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param c
     */
    private void isCommodityExistInUI(Commodity c) {
        boolean isAddToCommList = true;//????????????????????????????????????????????????
        if (BaseController.showCommList.size() > 0) {//?????????????????????ID?????????????????????????????????????????????????????????????????????????????????????????????
            for (int i = 0; i < BaseController.showCommList.size(); i++) {
                if (BaseController.showCommList.get(i).getID().equals(c.getID()) && BaseController.showCommList.get(i).getCommodityQuantity() < Default_CommodityMaxNumber) {//????????????????????????????????????????????????????????????
                    BaseController.showCommList.get(i).setCommodityQuantity(Integer.valueOf(BaseController.showCommList.get(i).getCommodityQuantity()) + 1);
                    BaseController.showCommList.get(i).setSubtotal(BaseController.showCommList.get(i).getCommodityQuantity() * BaseController.showCommList.get(i).getAfter_discount());
                    isAddToCommList = false;
                    break;
                } else if (BaseController.showCommList.get(i).getID().equals(c.getID()) && BaseController.showCommList.get(i).getCommodityQuantity() >= Default_CommodityMaxNumber) {
                    ToastUtil.toast("???????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
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
            packageUnit.setID(c.getPackageUnitID());
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
            BaseController.showCommList.add(c);
        }
        // ...????????????
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
            return;
        }
        retailTradePromoting = new RetailTradePromoting();
        BaseController.retailTrade = promotionCalculator.sell(BaseController.showCommList, avaliablePromotionList, retailTradePromoting);
        //...
        //todo ????????????

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
        log.info("commoditylist??? " + commodityList);
        return commodityList;
    }

    private void changeRadioButtonToPayViaWX() {
        paymentCode = 0b00000000;
        viewController.pay.setText("??? ??? ???");
        viewController.pay.setDisable(true);
//        wechatPay.setEnabled(true);// ????????????????????????????????????
        viewController.wechatPay.setDisable(false);
        wechatPay_click(); // ???????????????????????????
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
            //???RetailTrade??????????????????????????????
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
            viewController.payment.setVisible(true);//??????????????????
            viewController.totalpay_money.setText(String.valueOf(BaseController.retailTrade.getAmount()));
            viewController.total_money.setText(String.valueOf(BaseController.retailTrade.getAmount()));
            initWidget();
            //
            bPaying = true;
            //
            //?????????????????? ??????????????????????????????????????????????????????????????????????????????
            if (choosePaymentType.equals(weChatPayString)) {
                if (Configuration.bUseSandBox) {
                    viewController.pay.setText("??? ??? ???");
                    viewController.pay.setDisable(true);
                } else {
                    // pay.setBackgroundResource(R.drawable.button_background_disable);
                    viewController.pay.setText("????????????");//... ?????????????????????????????????????????????????????????????????????
                    viewController.pay.setDisable(true);
                }
            }

            viewController.etWechatPayingAmount.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!FieldFormat.checkPayAmount(viewController.etWechatPayingAmount.getText())) {
//                    if (viewController.etWechatPayingAmount.getText().startsWith(".")) {
                        ToastUtil.toast("??????????????????", ToastUtil.LENGTH_SHORT);
                        viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText())) {
                        ToastUtil.toast("??????????????????????????????0", ToastUtil.LENGTH_SHORT);
                        viewController.etWechatPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) > 0) {
                        ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
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
                        ToastUtil.toast("??????????????????", ToastUtil.LENGTH_SHORT);
                        viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (StringUtils.isEmpty(viewController.etCashPayingAmount.getText()) || Double.valueOf(viewController.etCashPayingAmount.getText()) < 0) {
                        ToastUtil.toast("?????????????????????????????????0", ToastUtil.LENGTH_SHORT);
                        viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText());
                        keyBoard = "";
                    } else if (compareString(viewController.etCashPayingAmount.getText(), MAX_CashPaymentAmount) > 0) {
                        ToastUtil.toast("?????????????????????????????????" + MAX_CashPaymentAmount, ToastUtil.LENGTH_SHORT);
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
            ToastUtil.toast("??????????????????", ToastUtil.LENGTH_SHORT);
        }
    }

    public void selectItem(String radioButton) {
        /**
         * ?????????????????????????????????
         */
        choosePaymentType = radioButton;
        System.out.println("-----------??????");
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
                viewController.pay.setText("??? ??? ???");
                viewController.pay.setDisable(true);
            } else {
                viewController.pay.setText("????????????");
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
            // ????????????????????????????????????????????????
            if (Double.valueOf(viewController.unpaidBalance.getText()) - Double.valueOf(MAX_CashPaymentAmount) > RetailTrade.TOLERANCE) {
                viewController.etCashPayingAmount.setText(MAX_CashPaymentAmount);
                viewController.changeMoney.setText(DEFAULT_changeAmount);
            } else {
                viewController.etCashPayingAmount.setText(viewController.unpaidBalance.getText().toString());
                viewController.changeMoney.setText(GeneralUtil.formatToShow(sub(Double.valueOf(viewController.etCashPayingAmount.getText().toString()), Double.valueOf(viewController.unpaidBalance.getText().toString()))));
            }
            viewController.pay.setText("???  ???");
            viewController.pay.setDisable(false);
        }
    }

    private ChangeListener changeListener;

    private void setSpinner() {
        List<String> couponCodeTitleList = getCouponCodeTitleList();
        ObservableList<String> observableList = FXCollections.observableArrayList(couponCodeTitleList);
        if (changeListener != null) {
            System.out.println("??????");
            viewController.spinner.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        }

        viewController.spinner.setItems(observableList);
        viewController.spinner.setVisibleRowCount(8);//???????????????8??????????????????
        viewController.spinner.getSelectionModel().selectFirst();//???????????????

        changeListener = (observable, oldValue, newValue) -> {
            String couponTitle = (String) newValue;
            if (!(COUPONTITLELIST_DEFAULT_AvailableConpon.equals(couponTitle) || COUPONTITLELIST_DEFAULT_UnavailableConpon.equals(couponTitle))) {
                for (int i = 0; i < couponCodeTitleList.size(); i++) {
                    if (couponCodeTitleList.get(i).equals(couponTitle)) {
                        // i-1?????????i???????????????"??????????????????"???"??????????????????".???showCouponCodeList????????????????????????????????????????????????????????????-1
                        couponCode = showCouponCodeList.get(i - 1);
                    }
                }
                System.out.println("???????????????????????????" + couponCode);
                Coupon queryCoupon = new Coupon();
                queryCoupon.setID(couponCode.getCouponID());
                coupon = (Coupon) couponPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, queryCoupon);
                if (couponPresenter.getLastErrorCode() != EC_NoError) {
                    couponCode = null;
                    log.error("????????????????????????????????????????????????????????????");
                    ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
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
     * ?????????????????????
     */
    private void initWidget() {
        viewController.totalpay_money.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.unpaidBalance.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
        viewController.etCashPayingAmount.setText(GeneralUtil.formatToShow(BaseController.retailTrade.getAmount()));
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
                ObservableList<String> observableList = FXCollections.observableArrayList(getCouponCodeTitleList());
                viewController.spinner.setItems(observableList);
                viewController.spinner.getSelectionModel().selectFirst();//???????????????
                ToastUtil.toast("?????????????????????,????????????????????????");
                viewController.closeLoadingDialog();
                return false;
            }
        }
        return true;
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
            PlatForm.get().sendMessage(message);
            return; // ?????????????????????????????????????????????????????????????????????????????????
        }
        log.info("==unpaidBalance.toString():" + viewController.unpaidBalance.getText() + "=====wetchatAmount:" + wetchatAmount);
        log.info("?????????????????????");
        totalWechatAmount += payAmount;
        keyBoard = "";

        if (totalCashAmount + totalWechatAmount >= BaseController.retailTrade.getAmount()) {
            if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) == 0) { //???????????????????????????????????????
                if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                    BaseController.retailTrade.setPaymentType(5);
                } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseController.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                    BaseController.retailTrade.setPaymentType(4);
                } else if (totalCashAmount - BaseController.retailTrade.getAmount() >= 0) {
                    BaseController.retailTrade.setPaymentType(1);
                } else {
                    throw new RuntimeException("?????????????????????");
                }
                //??????????????????????????????
                int paymentType = BaseController.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_cash;
                } else if (paymentType == 4) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_wetchat;
                } else {
                    BaseController.lastRetailTradePaymentType = viewController.pay_combination;
                }
                //????????????????????????
                BaseController.lastRetailTradeAmount = BaseController.retailTrade.getAmount();
                BaseController.retailTrade.setAmountCash(totalCashAmount);
                BaseController.retailTrade.setAmountWeChat(totalWechatAmount);
                log.info("===lastRetailTradeAmount:" + BaseController.lastRetailTradeAmount + "===AmountCash:" + BaseController.retailTrade.getAmountCash());
                // ????????????????????????
                if (couponCode != null) {
                    calculateReturnPrice(BaseController.retailTrade, BaseController.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseController.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseController.retailTrade);
                }
                appendRetailTradePromoting(BaseController.retailTrade);
                if (!createRetailTradeLocally(handlerMessage)) {

                }
                // ???????????????????????????5?????????????????????????????????????????????????????????????????????????????????????????????????????????
//                if (vip != null) {
//                    if (!uploadRetailTrade(handlerMessage)) { //TODO
//                    }
//                }
                updateRetailTradeAggregation();

                BaseController.retailTrade = null;
                System.out.println("????????????????????????????????????");
            }
        }
        message.obj = handlerMessage;
        PlatForm.get().sendMessage(message);
    }

    private boolean wxPayment(Double payAmount, HandlerMessage hm) {
        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(payAmount);
        System.out.println("-------------------------------------------------??????????????????,???????????????" + payAmount);
        WXPayInfo wxPayInfo = new WXPayInfo();
        wxPayInfo.setAuth_code(wxPayAuthCode);
        wxPayInfo.setTotal_fee(dPaymentMoney);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????session?????????null?????????????????????????????????????????????
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
        BaseController.retailTrade.setPaymentType(paymentCode);
        //
        // TODO ??????etWechatPayingAmount???????????????????????????
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
            originalTotalAmount += rtc.getPriceReturn() * rtc.getNO(); //????????????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????
        }

        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            // ?????????????????????????????????????????????????????????????????????
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
            if (retailTradePromoting.getListSlave1() == null) {  //getListSlave1???????????????????????????????????????????????????????????????????????????
                retailTradePromoting.setListSlave1(new ArrayList<BaseModel>());
            }
            if (retailTradePromoting == null || retailTradePromoting.getListSlave1().size() == 0) {
                int tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromoting.class);
                retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                retailTradePromoting.setTradeID(retailTrade.getID());
                retailTradePromoting.setSyncDatetime(Constants.getDefaultSyncDatetime2());

                tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, RetailTradePromotingFlow.class);

            } else {
                //?????????????????????????????????????????????????????????????????????
                tmpRetailTradePromotingFlowIDInSQLite = ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(retailTradePromoting.getListSlave1().size() - 1)).getID() + 1;
            }
            //
            StringBuilder promotingFlow = new StringBuilder();
            //
            promotingFlow.append("????????????????????????????????????:")
                    .append(GeneralUtil.formatToCalculate(payAmount))
                    .append("??? \t")
                    .append("??????????????????:???")
                    .append(coupon.getTitle())
                    .append("???\t"); //
            if (coupon.getType() == Coupon.EnumCouponCardType.ECCT_CASH.getIndex()) {
                promotingFlow.append("???????????????")
                        .append(coupon.getLeastAmount())
                        .append("??????")
                        .append(coupon.getReduceAmount())
                        .append("\t").append("????????????:")
                        .append(retailTrade.getAmount());
            } else {
                promotingFlow.append("???????????????")
                        .append(coupon.getLeastAmount())
                        .append("???????????????")
                        .append(coupon.getDiscount())
                        .append("?????????")
                        .append("??????????????????:")
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
     * ?????????????????????????????????????????????
     */
    private boolean createRetailTradeLocally(HandlerMessage hm) {
        if (vip != null) {
            BaseController.retailTrade.setVipID(vip.getID());
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
        BaseController.retailTrade.setShopID((int)pos.getShopID());

        log.info("???????????????????????????" + BaseController.retailTrade);
        //??????????????????????????????????????????(??????????????????RetailTrade??????????????????TradeID????????????retailTradePromoting???TradeID???????????????????????????retailTradePromoting??????????????????ID?????????RetailTrade???int2???vip?????????)????????????????????????c?????????????????????????????????????????????????????????
        RetailTrade rtCreate = (RetailTrade) retailTradeSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTrade_CreateMasterSlaveSQLite, BaseController.retailTrade);
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
        if (BaseController.retailTrade.getListSlave3() != null) {
            List<BaseModel> retailTradeCouponList = (List<BaseModel>) BaseController.retailTrade.getListSlave3();
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
        BaseController.lastRetailTrade = BaseController.retailTrade;
        return true;
    }

    private void updateRetailTradeAggregation() {
        log.info("??????????????????????????????,???????????????????????????????????????");

        // ??????????????????????????????:
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        //????????????????????????
        retailTradeAggregation.setID(BaseController.retailTradeAggregation.getID());
        retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseController.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseController.retailTradeAggregation.getReserveAmount());
        //????????????????????????
        retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTrade.getAmountCash()));/*????????????*/
        retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getWechatAmount(), wetchatAmount));/*????????????*/
        retailTradeAggregation.setAmount(GeneralUtil.sum(retailTradeAggregation.getCashAmount(), retailTradeAggregation.getWechatAmount()));/*?????????*/
        retailTradeAggregation.setTradeNO(BaseController.retailTradeAggregation.getTradeNO() + 1);/*????????????*/
        final Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        retailTradeAggregation.setWorkTimeEnd(date);/*?????????????????????????????????????????????????????????????????????*/
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_UpdateAsync);
        if (!retailTradeAggregationSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("?????????????????????????????????");
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
            log.error("Update?????????retailTradeAggregationSQLiteEvent???????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus());
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("Update?????????????????????" + retailTradeAggregationSQLiteBO.getSqLiteEvent().printErrorInfo());
        } else {
            // ???????????????????????????(??????????????????????????????)
            BaseController.retailTradeAggregation.setCashAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTrade.getAmountCash()));
            BaseController.retailTradeAggregation.setWechatAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getWechatAmount(), BaseController.retailTrade.getAmountWeChat()));
            BaseController.retailTradeAggregation.setAmount(GeneralUtil.sum(BaseController.retailTradeAggregation.getCashAmount(), BaseController.retailTradeAggregation.getWechatAmount()));
            BaseController.retailTradeAggregation.setTradeNO(BaseController.retailTradeAggregation.getTradeNO() + 1);//??????????????????????????????BaseActivity.retailTradeAggregation???TradeNO+1
            BaseController.retailTradeAggregation.setWorkTimeEnd(date);
        }
    }

    /**
     * ????????????
     */
    private void payViaCash(Double payAmount) {
        isPayingViaCash = true;
        handlerMessage.setErrorCode(EC_NoError);// ???????????????ErrorCode
        //
        paymentCode = paymentCode | cashPayCode;
        BaseController.retailTrade.setPaymentType(paymentCode);
        cashAmount = payAmount;
        totalCashAmount += cashAmount;
//        cashAmount = GeneralUtil.sum(Double.valueOf(etCashPayingAmount.getText().toString()), payAmount);
        BaseController.retailTrade.setAmountCash(cashAmount); // ????????????????????????????????????????????????????????????????????????
        //????????????????????????????????????????????????
        Double returnMoney = GeneralUtil.sub(Double.valueOf(getPayingMoney().getText()), Double.valueOf(viewController.unpaidBalance.getText()));
        if (returnMoney > BaseModel.TOLERANCE) {
            handlerMessage.setMsg("????????????,?????????????????????" + GeneralUtil.formatToShow(returnMoney));
            keyBoard = "";
        } else {
            handlerMessage.setMsg("????????????");
            keyBoard = "";
        }
        if (totalCashAmount + totalWechatAmount >= BaseController.retailTrade.getAmount()) {
            if (compareString(getPayingMoney().getText(), viewController.unpaidBalance.getText()) >= 0) { //???????????????????????????????????????
                if (BaseController.retailTrade.getAmount() > RetailTrade.TOLERANCE) {
                    if (totalCashAmount - 0.000000d > RetailTrade.TOLERANCE && totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                        BaseController.retailTrade.setPaymentType(5);
                    } else if (Math.abs(GeneralUtil.sub(totalWechatAmount, BaseController.retailTrade.getAmount())) < RetailTrade.TOLERANCE) {
                        BaseController.retailTrade.setPaymentType(4);
                    } else if (totalCashAmount - BaseController.retailTrade.getAmount() >= 0) {
                        BaseController.retailTrade.setPaymentType(1);
                    } else {
                        throw new RuntimeException("?????????????????????");
                    }
                }

                //???????????????????????????
                int paymentType = BaseController.retailTrade.getPaymentType();
                if (paymentType == 1) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_cash;
                } else if (paymentType == 4) {
                    BaseController.lastRetailTradePaymentType = viewController.pay_wetchat;
                } else {
                    BaseController.lastRetailTradePaymentType = viewController.pay_combination;
                }
                //???????????????????????????
                BaseController.lastRetailTradeChangeMoney = sub(totalCashAmount + totalWechatAmount, BaseController.retailTrade.getAmount());
                //????????????????????????
                BaseController.lastRetailTradeAmount = BaseController.retailTrade.getAmount();
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????BaseActivity.lastRetailTradeChangeMoney?????????0
                BaseController.retailTrade.setAmountWeChat(totalWechatAmount);
                BaseController.retailTrade.setAmountCash(sub(totalCashAmount, BaseController.lastRetailTradeChangeMoney));
                log.info("===lastRetailTradeAmount:" + BaseController.lastRetailTradeAmount + "===AmountCash:" + BaseController.retailTrade.getAmountCash());
                // ????????????????????????
                if (couponCode != null) {
                    calculateReturnPrice(BaseController.retailTrade, BaseController.retailTrade.getAmount());
                    appendRetailTradeCoupon(BaseController.retailTrade);
                    updateRetailTradePromoting(coupon, beforeUsingCouponAmount, BaseController.retailTrade);
                }
                appendRetailTradePromoting(BaseController.retailTrade);
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
        PlatForm.get().sendMessage(message);
    }

    private void cancelPayment() {
        if (Double.parseDouble(viewController.totalpay_money.getText()) - Double.parseDouble(viewController.unpaidBalance.getText()) > RetailTrade.TOLERANCE) { //????????????????????????
            // ??????????????????????????????????????????????????????????????????????????????????????????????????? ????????????????????????
            if (!NetworkUtils.isNetworkAvalible() || totalCashAmount - 0.000000d > RetailTrade.TOLERANCE) {
                ToastUtil.toast("?????????????????????" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "???!");
            } else if (totalWechatAmount - 0.000000d > RetailTrade.TOLERANCE) {
                // ????????????????????????????????????????????????????????????????????????????????????
                if (wxRefund()) {
                    ToastUtil.toast("??????????????????, ??????????????????");
                } else {
                    ToastUtil.toast("??????????????????????????????????????????" + GeneralUtil.formatToShow(GeneralUtil.sum(totalCashAmount, totalWechatAmount)) + "???!");
                }
            } else {
                //????????????????????????0??????????????????0
                throw new RuntimeException("?????????????????????");
            }
            //
        } else {
            ToastUtil.toast("?????????????????????????????????????????????");
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
            //??????????????????????????????
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
     * ??????????????????????????????????????????
     */
    private void resetPayment() {
        viewController.payment.setVisible(false);//??????????????????
        // TODO: 2020/6/19 ???????????????????????????
        //viewMain1Activity.setVisibility(View.INVISIBLE);
        bPaying = false;
        //
        total_money_double = 0.000000d;
        viewController.total_money.setText("0.00");
        totalWechatAmount = 0.000000d;
        totalCashAmount = 0.000000d;
        viewController.etWechatPayingAmount.setText(GeneralUtil.formatToShow(0.0d));
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
            log.info("??????????????????....");
            return false;
        }
        if (wxPayHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.info("??????????????????....");
            return false;
        }
        return true;
    }

    /**
     * ????????????ReserveAmount?????????????????????????????????sqlite???
     */
    private void createRetailTradeAggregation() {
        BaseController.retailTradeAggregation.setPosID(Constants.posID);
        //??????????????????????????????day end ???????????? dialog?????????????????????dialog???????????????????????????work time start??????2????????????????????????work time start????????????????????????
        //?????????????????????????????????????????????????????????day end dialog??????????????????????????????retailTradeAggregation???SQLite??????????????????????????????????????????????????????retailTradeAggregation??????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
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
            log.error("???????????????????????????");
        }
    }

    /* ?????????????????? */
    private void initScanGun() {
        // ??????key???????????????????????????20ms?????????????????????????????????
        ScanGun.setMaxKeysInterval(50);
        scanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
            @Override
            public void onScanFinish(String scanResult) {
                System.out.println("??????????????????????????????" + scanResult);
                if (viewController.payment.isVisible()) { // ?????????????????????
                    viewController.showLoadingDialog();
                    // ???????????????????????????
                    if (!validateCouponCodeAvailable()) {
                        return;
                    }
                    if (choosePaymentType.equals(weChatPayString)) {
                        if (scanResult != null && !"".equals(scanResult) && scanResult.length() == LENGTH_WxPayAuthCode) { //?????????????????????????????????
                            wxPayAuthCode = scanResult;
                            if (wxPayAuthCode != null && !"".equals(wxPayAuthCode)) {
                                if (!bLastWxPayIsNotYetDone && BaseController.retailTrade != null) {
                                    if (StringUtils.isEmpty(viewController.etWechatPayingAmount.getText()) || sub(Double.valueOf(viewController.etWechatPayingAmount.getText()), MIN_WechatPaymentAmount) < BaseModel.TOLERANCE) {
                                        viewController.closeLoadingDialog();
                                        ToastUtil.toast("????????????????????????0");
                                    } else if ((viewController.etWechatPayingAmount.getText().length() - viewController.etWechatPayingAmount.getText().indexOf(".")) == 1) {
                                        viewController.closeLoadingDialog();
                                        ToastUtil.toast("??????????????????");
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
                            ToastUtil.toast("???????????????????????????????????????");
                        }
                    } else {
                        viewController.closeLoadingDialog();
                        if (totalWechatAmount > BaseModel.TOLERANCE) {
                            ToastUtil.toast("?????????????????????????????????1???????????????????????????????????????");
                        } else {
                            ToastUtil.toast("???????????????????????????????????????????????????????????????");
                        }
                    }
                } else {
                    //????????????
                    log.info("??????????????????????????????" + scanResult);
                    if (StringUtils.isEmpty(scanResult) || scanResult.length() <= BaseController.FUZZY_QUERY_LENGTH) {
                        ToastUtil.toast("????????????????????????????????????????????????" + (BaseController.FUZZY_QUERY_LENGTH + 1) + "???");
                        log.info("????????????????????????????????????????????????" + (BaseController.FUZZY_QUERY_LENGTH + 1) + "???");
                        return;
                    }

                    if (scanResult.length() == Vip.Max_LENGTH_VipCardSN && scanResult.substring(0, 6).equals(Constants.MyCompanySN)) {
                        viewController.showClientPhone.setText(scanResult);
                    } else {
                        commodityListAfterQuery.clear();
                        viewController.scan_barcode_text.setText(scanResult);
                        commodityListAfterQuery = retrieveNCommodityInSQLite(true, scanResult);
                        log.info("??????????????????????????????" + commodityListAfterQuery.size() + "??????????????????????????????" + scanResult);
                        log.info("showCommList???" + BaseController.showCommList);
                        if (commodityListAfterQuery == null || commodityListAfterQuery.size() == 0) {
                            ToastUtil.toast("???????????????");
                            viewController.scan_barcode_text.setText("");
                        } else {
                            //??????????????????????????????????????????????????????scanResult???????????????
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
                                    //todo ????????????
                                });

                            } else { //????????????????????????????????????????????????scanResult????????????
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
        // ??????????????????????????????
        List<BaseModel> avaliablePromotionList = queryAvaliablePromotion();
        if(avaliablePromotionList == null) {
            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
            return;
        }
        // ?????????Activity????????????????????????????????????????????????????????????????????????
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
                ToastUtil.toast("???????????????", ToastUtil.LENGTH_SHORT);
                break;
            case 3: // ??????????????????UI??????
                HandlerMessage mth = (HandlerMessage) msg.obj;
                switch (mth.getErrorCode()) {
                    case EC_NoError:
                        ToastUtil.toast("????????????", ToastUtil.LENGTH_SHORT);
                        if (compareString(viewController.etWechatPayingAmount.getText(), viewController.unpaidBalance.getText()) == 0) { //???????????????????????????????????????
                            viewController.pay.setText("????????????");
                            viewController.pay.setDisable(true);
                            viewController.pay.setStyle("-fx-background-color: #888888;");
                            //????????????
                            try {
                                printSmallSheet(BaseController.lastRetailTrade);
                            } catch (Exception e) {
                                log.info("?????????????????????" + e.getMessage());
                            }
                            // TODO ?????????????????????
                            resetTradeAfterPay(1);
                            initializeVipData();
                        } else {
                            viewController.unpaidBalance.setText(GeneralUtil.formatToShow(sub(BaseController.retailTrade.getAmount(), totalWechatAmount + totalCashAmount)));
                            getPayingMoney().setText(viewController.unpaidBalance.getText());
                            //????????????????????????????????????????????????????????????????????????????????????
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
                        ToastUtil.toast("?????????????????????" + (mth.getMsg() == null ? "" : mth.getMsg()) + "????????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                        break;
                    case EC_OtherError:
                        switch (mth.getSubErrorCode()) {
                            case SubErrorCode_RetailTrade_OtherError:
                                ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradePromoting_OtherError:
                                ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradeCoupon_OtherError:
                                ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                        }
                        break;
                    case EC_Timeout:
                        ToastUtil.toast("??????????????????!", ToastUtil.LENGTH_SHORT);
                        changeRadioButtonToPayViaCashOnly();
                        break;
                }
                bLastWxPayIsNotYetDone = false;
                viewController.closeLoadingDialog();
                break;
            case 4: // ??????????????????UI??????
                HandlerMessage mthCash = (HandlerMessage) msg.obj;
                switch (mthCash.getErrorCode()) {
                    case EC_NoError:
                        ToastUtil.toast(mthCash.getMsg(), ToastUtil.LENGTH_SHORT);
                        if (compareString(getPayingMoney().getText(), viewController.unpaidBalance.getText()) >= 0) { //???????????????????????????????????????
                            viewController.pay.setText("????????????");
                            viewController.pay.setDisable(true);
                            // ?????????????????????????????????????????????????????????????????????????????????
                            BaseController.lastRetailTrade = BaseController.retailTrade;
                            //????????????
                            try {
                                printSmallSheet(BaseController.lastRetailTrade);
                            } catch (Exception e) {
                                log.info("?????????????????????" + e.getMessage());
                            }
                            BaseController.retailTrade = null;
                            System.out.println("????????????????????????????????????");
                            // ???????????????????????????
                            // TODO ????????????
                            // ??????????????????????????????????????????????????????????????????????????????????????????
                            // ????????????????????????????????????????????????????????????????????????
                            // ??????????????????????????????????????????????????????????????????????????????BaseActivity.showCommodity
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
                                ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradePromoting_OtherError:
                                ToastUtil.toast("?????????????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                            case SubErrorCode_RetailTradeCoupon_OtherError:
                                ToastUtil.toast("???????????????????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                        }
                        break;
                }
                isPayingViaCash = false;
                viewController.closeLoadingDialog();
                break;
            case 5: // ????????????????????????
                ErrorInfo errorInfo = new ErrorInfo();
                do {
                    if (vipHttpEvent.getLastErrorCode() == EC_NoError) {
                        vip = (Vip) vipHttpEvent.getBaseModel1();
                        if (vip != null) {
                            // ...??????VIP??????
                            log.debug("?????????????????????:" + vip);
                            viewController.showClientPhone.setVisible(false);
                            viewController.showClientPhone.setManaged(false);
                            viewController.search_vip_button.setVisible(false);
                            viewController.search_vip_button.setManaged(false);
                            //
                            viewController.vipMobile.setText("??????????????????" + vip.getMobile());
                            viewController.vipBonus.setText("?????????" + vip.getBonus());
                            viewController.vipMobile.setVisible(true);
                            viewController.vipMobile.setManaged(true);
                            viewController.vipBonus.setVisible(true);
                            viewController.vipBonus.setManaged(true);
                            viewController.return_search_vip_button.setVisible(true);
                            viewController.return_search_vip_button.setManaged(true);

                            // ?????????????????????????????????
                            CouponCode couponCode = new CouponCode();
                            couponCode.setVipID(vip.getID().intValue());
                            couponCode.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                            couponCode.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
                            if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
                                errorInfo.setErrorMessage("??????????????????????????????????????????" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage());
                                ToastUtil.toast("??????????????????????????????", ToastUtil.LENGTH_SHORT);
                                break;
                            }
                            ToastUtil.toast("???????????????????????????????????????????????????...", ToastUtil.LENGTH_SHORT);
                        } else {
                            errorInfo.setErrorMessage("????????????????????????");
                            ToastUtil.toast("????????????????????????", ToastUtil.LENGTH_SHORT);
                            break;
                        }
                    } else {
                        errorInfo.setErrorMessage("??????????????????");
                        ToastUtil.toast("??????????????????", ToastUtil.LENGTH_SHORT);
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
                    ToastUtil.toast("??????????????????????????????", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("??????????????????????????????????????????" + couponCodeHttpEvent.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
                }
                viewController.closeLoadingDialog();
                break;
        }
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
     * ???????????????
     */
    public void printSmallSheet(RetailTrade retailTrade) {
        if (viewController.currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            viewController.currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (viewController.currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            ToastUtil.toast("?????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            return;
        }
        //
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID((viewController.currentSmallSheetID));
        SmallSheetFrame smallSheetFrame1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && smallSheetFrame1 == null) {
            log.error("smallSheetFramePresenter.retrieve1ObjectSync??????,?????????=" + smallSheetFramePresenter.getLastErrorCode());
            ToastUtil.toast("?????????????????????????????????????????????", ToastUtil.LENGTH_SHORT);
            return;
        }

        List<SmallSheetText> listSmallSheetText = (List<SmallSheetText>) smallSheetFrame1.getListSlave1();

        //???????????????????????????????????????  ?????????1.5?????????
        if (retailTrade.getSourceID() > 0) {
            AidlUtil.getInstance().printText("?????????",
                    1,//??????30
                    true,//??????
                    17,//??????
                    false);//????????????
            AidlUtil.getInstance().linewrap(1);//????????????
        }

        //??????????????????????????????????????????
        if (smallSheetFrame1.getLogo() != null && !smallSheetFrame1.getLogo().equals("")) {
            // TODO: 2020/6/22 ????????????????????????
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
        // ??????????????????
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        AidlUtil.getInstance().printText(listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getContent() + sdf.format(retailTrade.getSaleDatetime()), //
                (int) listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getSize(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getBold() == 1, //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getGravity(), //
                listSmallSheetText.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()).getFrameId() == 1);
        AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
        //
        String[] saCommodityAttribute = new String[]{"????????????", "??????", "??????"};
        String[][] listCommodity = null;// ?????????????????????
        //
        List<RetailTradeCommodity> listRtc = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
        if (listRtc == null) {
            ToastUtil.toast("????????????????????????????????????", ToastUtil.LENGTH_SHORT);
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
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @return currentSmallSheetID
     * BaseSQLiteBO.INVALID_INT_ID???DB??????????????????????????????????????????
     */
    public int retrieveCurrentSmallSheetIDFromConfig() {
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setID(ConfigGeneral.ACTIVE_SMALLSHEET_ID);
        ConfigGeneral configGeneralFromDB = (ConfigGeneral) configGeneralPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral);
        if (configGeneralPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && configGeneralFromDB == null) {
            log.error("configGeneralPresenter.retrieve1ObjectSync()??????,?????????=" + configGeneralPresenter.getLastErrorCode());
//            Toast.makeText(getApplicationContext(), "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show(); //????????????UI??????

            return BaseSQLiteBO.INVALID_INT_ID;
        }

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != '%s'");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("???????????????????????????????????????");

            return BaseSQLiteBO.INVALID_INT_ID;
        }
        if (ssfList.size() == 0) { // ??????????????????
            return BaseSQLiteBO.INVALID_INT_ID;
        }

        for (SmallSheetFrame ssf : ssfList) {
            if (ssf.getID() == Integer.valueOf(configGeneralFromDB.getValue())) {
                return Integer.valueOf(configGeneralFromDB.getValue());
            }
        }
        // ???????????????????????????????????????????????????????????????????????????????????????1???????????????????????????????????????????????????app???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        configGeneralFromDB.setValue(String.valueOf(SmallSheetController.Default_SmallSheetID_INPos));
        configGeneralFromDB.setReturnObject(1); //...
        viewController.configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!viewController.configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
            log.error("?????????????????????????????????configGeneral=" + configGeneralFromDB);
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
            boolean exist = false; // ????????????????????????????????????????????????
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
                exist = true; // ????????????????????????????????????
            }
            if(exist) {
                avaliablePromotionList.add(promotion);
            }
        }
        return avaliablePromotionList;
    }
}
