package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wpos.*;
import org.springframework.stereotype.Component;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.LoginActivity;
import wpos.application.RetailTradeAggregationActivity;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.presenter.*;
import wpos.utils.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static wpos.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions;

@Component("allFragmentViewController")
public class AllFragmentViewController extends BaseController {
    public static Timer timerToStartUploadRetailTradeAggregation;

    private static NetworkUtils networkUtils = new NetworkUtils();

    @Resource
    private LoginActivity loginActivity;

    @Resource
    public RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    @Resource
    public RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    @Resource
    public RetailTradeAggregationHttpBO retailTradeAggregationHttpBO;
    @Resource
    public RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent;

    private Intent intent;
    //    --------------------------销售页面-------------------------------------
    @FXML
    public StackPane sale_Pane;
    @FXML   //右侧支付页面
    public BorderPane payment;
    @FXML   //现金支付
    public FlowPane cashPay;
    @FXML   //微信支付
    public FlowPane wechatPay;
    @FXML   //微信支付字体
    public Label weChat_pay_text;
    @FXML   //现金支付字体
    public Label cash_pay_text;
    @FXML   //未付金额
    public Label unpaidBalance;
    @FXML   //微信支付金额
    public Label etWechatPayingAmount;
    @FXML   //现金支付金额
    public Label etCashPayingAmount;
    @FXML   //找零
    public Label changeMoney;
    @FXML   //主页显示商品数量
    public Label commodityQuantity;
    @FXML   //销售页面的总计金额
    public Label total_money;
    @FXML   //上一单支付金额
    public Label last_amount;
    @FXML   //上一单支付方式
    public Label last_paymenttype;
    @FXML   //上一单找零
    public Label last_changemoney;
    @FXML   //销售列表
    public ListView<Commodity> rvCommodity;
    @FXML
    public Label tvWechatPaying;
    @FXML
    public Label wechat_paying_prefix;
    @FXML
    public Label tvCashPaying;
    @FXML
    public Label cash_paying_prefix;
    @FXML
    public DatePicker check_list_startDate;
    @FXML
    public DatePicker check_list_endDate;
    @FXML
    public TextField returnAmount;
    @FXML
    protected Button confirm_returnCommodity;
    @FXML
    protected ListView<RetailTrade> check_list_order_rv;
    @FXML
    protected ListView<RetailTradeCommodity> check_list_commodity_rv;
    @FXML
    protected Label vipMobile;
    @FXML
    protected Label vipBonus;
    @FXML
    protected Button return_search_vip_button;
    @FXML
    protected Button search_vip_button;


    @FXML   //支付按钮
    public Button pay;
    @FXML   //优惠券列表
    public ComboBox spinner;
    @FXML   //会员手机号输入框
    public TextField showClientPhone;
    @FXML   //扫描条形码输入框
    public TextField scan_barcode_text;
    @FXML //右侧支付页面的总计金额
    public Label totalpay_money;

    @FXML   // 查单重置搜索
    protected Button check_list_clear;

    @FXML   //结账按钮
    public void balance_click() {
        mainController.balance_click();
    }

    @FXML   //挂单按钮
    public void holdBill_click() {
        mainController.holdBill_click();
    }

    @FXML   //取消支付按钮
    public void paymentClose_click() {
        mainController.paymentClose_click();
    }

    @FXML   //单选按钮，现金支付
    private void cashPay_click() {
        mainController.cashPay_click();
    }

    @FXML   //单选按钮，微信支付
    private void wechatPay_click() {
        mainController.wechatPay_click();
    }

    @FXML   //扫描条形码输入框
    private void scanBarcodeText_click() {
        mainController.scanBarcodeText_click();
    }

    @FXML   //扫描条形码输入框
    private void scan_barcode_search() {
        mainController.scan_barcode_search();
    }

    @FXML   //搜索vip按钮
    private void searchVip_click() {
        mainController.searchVip_click();
    }

    @FXML   //返回vip搜索按钮
    private void return_searchVip_click() {
        mainController.return_searchVip_click();
    }


    @FXML // 点击查单页面重置搜索
    private void check_list_clear_click() {
        checkListController.check_list_clear_click();
    }

    @FXML
    private void searchRci_click() {
        retrieveCommodityInventoryViewController.searchRci_click(1,RetrieveCommodityInventoryViewController.PAGE_SIZE_UI);
    }

    public AllFragmentViewController() {
    }

    @FXML   //支付按钮点击
    private void pay_click() {
        mainController.pay_click();
    }

    @FXML
    private void key10() {
        mainController.key10();
    }

    @FXML
    private void key20() {
        mainController.key20();
    }

    @FXML
    private void key50() {
        mainController.key50();
    }

    @FXML
    private void key100() {
        mainController.key100();
    }

    @FXML
    private void keyDelete() {
        mainController.keyDelete();
    }

    @FXML
    private void keyPoint() {
        mainController.keyPoint();
    }

    @FXML
    private void key0() {
        mainController.key0();
    }

    @FXML
    private void key1() {
        mainController.key1();
    }

    @FXML
    private void key2() {
        mainController.key2();
    }

    @FXML
    private void key3() {
        mainController.key3();
    }

    @FXML
    private void key4() {
        mainController.key4();
    }

    @FXML
    private void key5() {
        mainController.key5();
    }

    @FXML
    private void key6() {
        mainController.key6();
    }

    @FXML
    private void key7() {
        mainController.key7();
    }

    @FXML
    private void key8() {
        mainController.key8();
    }

    @FXML
    private void key9() {
        mainController.key9();
    }


    //--------------------------库存页面----------------------------------------------
    @FXML   //搜索按钮
    private Pane rci_search;
    @FXML   // 清空条形码按钮
    protected Pane rci_search1;
    @FXML   //输入框
    protected TextField rci_condition_input;
    @FXML   //同步所有数据按钮
    private Button sync;
    @FXML   //库存页面
    public BorderPane rci_Pane;
    @FXML
    public ProgressBar progress;

    @FXML   //同步所有数据按钮的点击事件
    private void rci_Sync() {
        retrieveCommodityInventoryViewController.rci_Sync();
    }

    //---------------------------重置基础资料页面---------------------------------------
    @FXML   //重置基础资料页面
    private Pane resetBaseData_Pane;


    //------------------------------查单页面--------------------------------------------
    @FXML   //退货页面
    protected BorderPane return_goods_pane;
    @FXML   //查单页面
    private BorderPane cl_Pane;
    @FXML   //取单查单页面
    private BorderPane bt_Pane;

    @FXML
    protected FlowPane bottomlinear;

    @FXML
    protected TextField totalPage;

    @FXML
    protected TextField checkListOriginalPrice;

    @FXML
    protected TextField checkListReceivable;

    @FXML
    protected TextField checkListNetReceipts;

    @FXML
    protected TextField checkListDiscount;

    @FXML
    protected TextField checkListPaymentMethod;

    @FXML
    protected TextField check_list_sn;

    @FXML
    protected ImageView lastPage;

    @FXML
    protected ImageView nextPage;

    @FXML
    protected TextField currentPage;

    @FXML
    protected Button tvQueryRetailTrade;

    @FXML
    protected Button reprintSmallSheet;
    @FXML
    protected TextField tv_returngoodsNumber;

    @FXML
    protected ImageView delete_all;

    @FXML
    protected FlowPane query_fail_view;

    @FXML
    protected Label tv_failtips;

    @FXML
    protected ListView<Commodity> commodityInfoListView;

    @FXML
    public FlowPane deleteAll;


    @FXML
    private Button sync_base_data;

    @FXML
    private void sync_base_data_click() {
        resetBaseDataViewController.sync_base_data();
    }

    @FXML
    private void deleteAll_click() {
        checkListController.deleteAll_click();
    }

    @FXML
    private void delete_all_click() {
        retrieveCommodityInventoryViewController.clear_all_click();
    }

    @FXML   //筛选按钮
    private void cl_search_click() {
        //显示出退货页面
//        return_goods_pane.setVisible(true);
//        return_goods_pane.setManaged(true);
        checkListController.cl_search_click();
    }

    @FXML
    private void cl_Pane_click() {
        checkListController.cl_Pane_click();
    }

    @FXML
    private void lastPage_click() {
        checkListController.lastPage_click();
    }

    @FXML
    private void nextPage_click() {
        checkListController.nextPage_click();
    }

    @FXML
    private void reprintSmallSheet_click() {
        checkListController.reprintSmallSheet_click();
    }

    @FXML
    private void confirm_returnCommodity_click() {
        checkListController.confirm_returnCommodity_click();
    }

    @FXML
    private void check_list_sn_click() {
        checkListController.check_list_sn_click();
    }

    //-----------------------------前台小票页面--------------------------------------------
    @FXML
    private BorderPane smallSheet_Pane;

    @FXML
    public BorderPane footer1Layout;
    @FXML
    public BorderPane bottomBlankLineLayout;
//    @FXML
//    RelativeLayout ticketBottomLayout;

    @FXML
    public Button smallsheetToUpdate;
    @FXML
    public Button cancelUpdate;
    //    public FlowPane Mask;   //...
    @FXML
    public Label templatePrinterSize;
    @FXML
    public BorderPane designTopLogo;
    @FXML
    public Label designIfTopLogo;
    @FXML
    public TextField designHeader;
    @FXML
    public TextField designFooter1;
    @FXML
    public BorderPane designFooter2_layout;
    @FXML
    public TextField designFooter2;
    @FXML
    public BorderPane designFooter3Layout;
    @FXML
    public TextField designFooter3;
    @FXML
    public BorderPane designFooter4Layout;
    @FXML
    public TextField designFooter4;
    @FXML
    public BorderPane designFooter5Layout;
    @FXML
    public TextField designFooter5;
    @FXML
    public BorderPane designFooter6Layout;
    @FXML
    public TextField designFooter6;
    @FXML
    public BorderPane designFooter7Layout;
    @FXML
    public TextField designFooter7;
    @FXML
    public BorderPane designFooter8Layout;
    @FXML
    public TextField designFooter8;
    @FXML
    public BorderPane designFooter9Layout;
    @FXML
    public TextField designFooter9;
    @FXML
    public BorderPane designFooter10Layout;
    @FXML
    public TextField designFooter10;
    @FXML
    public Label designBottomBlankLine;
    @FXML
    public Label addDesignBottomBlankLineNumber;
    @FXML
    public Label reduceDesignBottomBlankLineNumber;
    @FXML
    public FlowPane templateLogoLayout;
    @FXML
    public ImageView templateLogo;
    @FXML
    public Label templateHeader;
    @FXML
    public Label templateDocumentNumber;
    @FXML
    public Label templateDate;
    @FXML
    public Label templatePaymentMethod1;
    @FXML
    public Label templatePaymentMethod3;
    @FXML
    public Label templateFooter1;
    @FXML
    public Label templateFooter2;
    @FXML
    public Label templateFooter3;
    @FXML
    public Label templateFooter4;
    @FXML
    public Label templateFooter5;
    @FXML
    public Label templateFooter6;
    @FXML
    public Label templateFooter7;
    @FXML
    public Label templateFooter8;
    @FXML
    public Label templateFooter9;
    @FXML
    public Label templateFooter10;
    @FXML
    public FlowPane add_footer;
    @FXML
    public FlowPane controlTextSize;
    //    @FXML
//    public Label showControlTextSize; //...
    @FXML
    public Button controlBold;
    @FXML
    public Button controlKeepLeft;
    @FXML
    public Button controlKeepCenter;
    @FXML
    public Button controlKeepRight;
    @FXML
    public Label templateGoodsName;
    @FXML
    public Label templateGoodsNumber;
    @FXML
    public Label templateSubtotal;
    //    public Label templateGoodsNameLayout;  //...
//    @FXML
//    public Label templateGoodsNumberLayout; //...
//    @FXML
//    public Label templateSubtotalLayout; //...
    @FXML
    public Label templateFirstGoodsName;
    @FXML
    public Label templateSecondGoodsName;
    @FXML
    public Label templateThirdGoodsName;
    @FXML
    public Label templateFirstGoodsNumber;
    @FXML
    public Label templateSecondGoodsNumber;
    @FXML
    public Label templateThirdGoodsNumber;
    @FXML
    public Label templateFirstSubtotal;
    @FXML
    public Label templateSecondSubtotal;
    @FXML
    public Label templateThirdSubtotal;
    @FXML
    public Label templateTotalMoney;
    @FXML
    public Label templatePaymentMethod;
    @FXML
    public Label templateDiscount;
    @FXML
    public Label templatePayable;
    @FXML
    public Label templateTicketBottom;
    @FXML
    public Button print_test;
    @FXML
    public Button left_shift;
    @FXML
    public Button right_shift;
    @FXML
    public Button smallsheet_create;
    @FXML
    public ChoiceBox printFormatVersion;
    @FXML
    public Button smallsheet_update;
    @FXML
    public Button smallsheet_delete;
    @FXML
    public Button loadingConfigData;
    @FXML
    public Button createConfirm;
    @FXML
    public Button createCancle;
    @FXML
    public Button useForm;
    @FXML
    public TextField designDelimiterToRepeat;
    @FXML
    public Label delimiterDiy1;
    @FXML
    public Label delimiterDiy2;
    @FXML
    public Label delimiterDiy3;
    @FXML
    public Label delimiterDiy4;
    @FXML
    public Label delimiterDiy5;
    @FXML
    public Label delimiterDiy6;
    @FXML
    public Label showControlTextSize;
    @FXML
    public Button rci_loadmore;
    @FXML
    public TextField designTicketBottom;
    @FXML
    public Rectangle Mask;

    @FXML
    private void controlBold_click() {
//        smallSheetController.controlBold_click();
    }

    @FXML
    private void Mask_Click() {

    }

    @FXML
    private void designTopLogo_click() {
        smallSheetController.designTopLogo_click();
    }

    @FXML
    private void add_footer_click() {
        smallSheetController.add_footer_click();
    }

    @FXML
    private void controlTextSize_click() {
        smallSheetController.add_footer_click();
    }

    @FXML
    private void delete_footer2_click() {
        smallSheetController.designDeleteFooter2_click();
    }

    @FXML
    private void delete_footer3_click() {
        smallSheetController.designDeleteFooter3_click();
    }

    @FXML
    private void delete_footer4_click() {
        smallSheetController.designDeleteFooter4_click();
    }

    @FXML
    private void delete_footer5_click() {
        smallSheetController.designDeleteFooter5_click();
    }

    @FXML
    private void delete_footer6_click() {
        smallSheetController.designDeleteFooter6_click();
    }

    @FXML
    private void delete_footer7_click() {
        smallSheetController.designDeleteFooter7_click();
    }

    @FXML
    private void delete_footer8_click() {
        smallSheetController.designDeleteFooter8_click();
    }

    @FXML
    private void delete_footer9_click() {
        smallSheetController.designDeleteFooter9_click();
    }

    @FXML
    private void delete_footer10_click() {
        smallSheetController.designDeleteFooter10_click();
    }

    @FXML
    private void templateHeader_click() {
        smallSheetController.templateHeader_click();
    }

    @FXML
    private void templateDocumentNumber_click() {
        smallSheetController.templateDocumentNumber_click();
    }

    @FXML
    private void templateDate_click() {
        smallSheetController.templateDate_click();
    }

    @FXML
    private void templateGoodsName_click() {
        smallSheetController.templateGoodsName_click();
    }

    @FXML
    private void templateGoodsNumber_click() {
        smallSheetController.templateGoodsNumber_click();
    }

    @FXML
    private void templateSubtotal_click() {
        smallSheetController.templateSubtotal_click();
    }

//    @FXML
//    private void templateGoodsNameLayout_click() {
//        smallSheetController.templateGoodsNameLayout_click();
//    }
//
//    @FXML
//    private void templateGoodsNumberLayout_click() {
//        smallSheetController.templateGoodsNumberLayout_click();
//    }
//
//    @FXML
//    private void templateSubtotalLayout_click() {
//        smallSheetController.templateSubtotalLayout_click();
//    }

    @FXML
    private void templateTotalMoney_click() {
        smallSheetController.templateTotalMoney_click();
    }

    @FXML
    private void templatePaymentMethod_click() {
        smallSheetController.templatePaymentMethod_click();
    }

    @FXML
    private void templateDiscount_click() {
        smallSheetController.templateDiscount_click();
    }

    @FXML
    private void templatePayable_click() {
        smallSheetController.templatePayable_click();
    }

    @FXML
    private void templatePaymentMethod1_click() {
        smallSheetController.templatePaymentMethod1_click();
    }

    @FXML
    private void templatePaymentMethod3_click() {
        smallSheetController.templatePaymentMethod3_click();
    }

    @FXML
    private void templateFooter1_click() {
        smallSheetController.templateFooter1_click();
    }

    @FXML
    private void templateFooter2_click() {
        smallSheetController.templateFooter2_click();
    }

    @FXML
    private void templateFooter3_click() {
        smallSheetController.templateFooter3_click();
    }

    @FXML
    private void templateFooter4_click() {
        smallSheetController.templateFooter4_click();
    }

    @FXML
    private void templateFooter5_click() {
        smallSheetController.templateFooter5_click();
    }

    @FXML
    private void templateFooter6_click() {
        smallSheetController.templateFooter6_click();
    }

    @FXML
    private void templateFooter7_click() {
        smallSheetController.templateFooter7_click();
    }

    @FXML
    private void templateFooter8_click() {
        smallSheetController.templateFooter8_click();
    }

    @FXML
    private void templateFooter9_click() {
        smallSheetController.templateFooter9_click();
    }

    @FXML
    private void templateFooter10_click() {
        smallSheetController.templateFooter10_click();
    }

    @FXML
    private void templateLogo_click() {
        smallSheetController.templateLogo_click();
    }

    @FXML
    private void templateTicketBottom_click() {
        smallSheetController.templateTicketBottom_click();
    }

    @FXML
    private void print_test_click() {
        smallSheetController.print_test_click();
    }

    @FXML
    private void smallsheet_create_click() {
        smallSheetController.smallsheet_create_click();
    }

    @FXML
    private void smallsheet_update_click() {
        smallSheetController.smallsheet_update_click();
    }

    @FXML
    private void smallsheet_delete_click() {
        smallSheetController.smallsheet_delete_click();
    }

    @FXML
    private void loadingConfigData_click() {
        smallSheetController.loadingConfigData_click();
    }

    @FXML
    private void createConfirm_click() {
        smallSheetController.createConfirm_click();
    }

    @FXML
    private void createCancle_click() {
        smallSheetController.createCancle_click();
    }

    @FXML
    private void useForm_click() {
        smallSheetController.useForm_click();
    }

    @FXML
    private void addDesignBottomBlankLineNumber_click() {
        smallSheetController.addDesignBottomBlankLineNumber_click();
    }

    @FXML
    private void reduceDesignBottomBlankLineNumber_click() {
        smallSheetController.reduceDesignBottomBlankLineNumber_click();
    }

    @FXML
    private void smallsheetToUpdate_click() {
        smallSheetController.smallsheetToUpdate_click();
    }

    @FXML
    private void cancelUpdate_click() {
        smallSheetController.cancelUpdate_click();
    }

    @FXML
    private void printFormatVersion_click() {
        smallSheetController.onItemSelected();
    }

    //------------------------挂单页面--------------------------------------------------
    @FXML
    private BorderPane bill_Pane;
    @FXML
    protected ListView<RetailTrade> rv_retailtrade;
    @FXML
    protected ListView<Commodity> rv_commodity;

    @FXML   //删除按钮点击事件
    private void deleteBill_click() {
        billTradeController.delete_click();
    }

    @FXML   //继续结账按钮点击事件
    private void continueRetail_click() {
        billTradeController.continueRetail_click();
    }


    //-------------------------主页面左侧item点击----------------------------------------
    @FXML   //数据处理按钮
    private Button resetBaseDataItem;
    @FXML   //前台小票按钮
    private Button smallSheetItem;

    public static final String pay_cash = "现金支付 ";
    public static final String pay_combination = "组合支付 ";
    public static final String pay_wetchat = "微信支付 ";
    public static final String pay_aliPay = "支付宝支付 ";

    /**
     * WX支付时的超时值，和OKHttp的超时值一致
     */
    protected final long WxPay_TIME_OUT = 60;
    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;

    /**
     * 交班时间
     */
    private String workTimeEnd;

    /**
     * 定时ping nbr以保持会话不过期。服务器的过期时间是30分钟。App每隔25分钟ping一次
     */
    public static TimerTask timerTaskPing;
    /**
     * 参考timerTaskPing
     */
    public static Timer timerPing;

    @Resource
    private StaffHttpBO staffHttpBO;
    @Resource
    private StaffHttpEvent staffHttpEvent;

    @Resource
    MainController mainController;

    @Resource
    SmallSheetController smallSheetController;

    @Resource
    RetrieveCommodityInventoryViewController retrieveCommodityInventoryViewController;

    @Resource
    ResetBaseDataViewController resetBaseDataViewController;

    @Resource
    CheckListController checkListController;

    @Resource
    BillTradeController billTradeController;

    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    public SmallSheetFramePresenter smallSheetFramePresenter;
    @Resource
    public ConfigGeneralPresenter configGeneralPresenter;
    @Resource
    public ConfigGeneralSQLiteBO configGeneralSQLiteBO;
    @Resource
    public ConfigGeneralHttpBO configGeneralHttpBO;
    @Resource
    public ConfigGeneralSQLiteEvent configGeneralSQLiteEvent;
    @Resource
    public ConfigGeneralHttpEvent configGeneralHttpEvent;
    @FXML
    private Label staff_phone;

    public static int currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;

    AllFragmentViewController viewController;

    /**
     * 更新界面时间的线程
     */
    protected AppTimeThread appTimethread;
    public static boolean timeThreadCanRun = true;//标志时间线程是否可以运行
    @FXML
    private TextArea show_time;


    public void setMainController() {
        timeThreadCanRun = true;
        staff_phone.setText(Constants.getCurrentStaff().getPhone());
        mainController.setAllFragmentViewController(this);
        //开启App时间线程，以显示时间在UI上
        appTimethread = new AppTimeThread();
        appTimethread.start();

    }

    public void setSmallSheetController() {
        smallSheetController.setAllFragmentViewController(this);
    }

    public SmallSheetController getSmallSheetController() {
        return smallSheetController;
    }

    public void setRetrieveCommodityInventoryViewController() {
        retrieveCommodityInventoryViewController.setAllFragmentViewController(this);
    }

    public CheckListController getCheckListController() {
        return checkListController;
    }

    public RetrieveCommodityInventoryViewController getRetrieveCommodityInventoryViewController() {
        return retrieveCommodityInventoryViewController;
    }

    public void setResetBaseDataViewController() {
//        resetBaseDataViewController = new ResetBaseDataViewController(this);
        resetBaseDataViewController.setAllFragmentViewController(this);
    }

    public void setCheckListController() {
//        checkListController = new CheckListController(this);
        checkListController.setAllFragmentViewController(this);
    }

    public void setBillTradeController() {
        billTradeController.setAllFragmentViewController(this);
    }


    @FXML   //点击切换销售页面
    public void saleItem_click() {
        changeFragment(sale_Pane);
        setMainController();
    }

    private void onSwitchStage() {
        paymentClose_click();
    }

    @FXML   //点击切换库存页面
    private void retrieveCommodityInventoryItem_click() {
        if (payment.isVisible()) {
            onSwitchStage();
        } else {
            changeFragment(rci_Pane);
            setRetrieveCommodityInventoryViewController();
        }
    }

    @FXML
    public void rci_condition_input_click() {
        retrieveCommodityInventoryViewController.rci_condition_input_click();
    }

    @FXML   //点击设置按钮显示2级菜单
    private void settingItem_click() {
        if (payment.isVisible()) {
            onSwitchStage();
        } else {
            if (resetBaseDataItem.isVisible()) {
                resetBaseDataItem.setVisible(false);
                resetBaseDataItem.setManaged(false);
                smallSheetItem.setVisible(false);
                smallSheetItem.setManaged(false);
            } else {
                resetBaseDataItem.setVisible(true);
                resetBaseDataItem.setManaged(true);
                smallSheetItem.setVisible(true);
                smallSheetItem.setManaged(true);
            }
        }
    }

    @FXML   //点击切换数据处理页面
    private void syncDataItem_click() {
        changeFragment(resetBaseData_Pane);
        setResetBaseDataViewController();
    }

    @FXML   //点击切换前台小票页面
    private void smallSheetItem_click() {
        if (Constants.getCurrentStaff().getRoleID() != Constants.cashier_Role_ID) {
            changeFragment(smallSheet_Pane);
            setSmallSheetController();
        } else {
            showToastMessage("权限不足");
        }
    }

    @FXML   //点击切换查单页面
    private void checkListItem_click() {
        if (payment.isVisible()) {
            onSwitchStage();
        } else {
            changeFragment(cl_Pane);
            setCheckListController();
            //代替用户点击搜索按钮
            return_goods_pane.setVisible(false);
            return_goods_pane.setManaged(false);
            checkListController.cl_search_click();
        }
    }

    @FXML //点击切换取单页面
    private void billItem_click() {
        if (payment.isVisible()) {
            onSwitchStage();
        } else {
            changeFragment(bill_Pane);
            setBillTradeController();
            billTradeController.showRetailTrade();
        }
    }

    @FXML   //点击退出按钮
    private void logoutItem_click() {
        if (payment.isVisible()) {
            onSwitchStage();
        } else {
            if (BaseController.retailTradeHoldBillList.size() > 0) {
                ToastUtil.toast("存在挂起的未结零售单，请先处理", ToastUtil.LENGTH_SHORT);
                billItem_click();
            } else {
                show_RetailTradeAggregation_Dialog();
            }
        }
    }

    //切换界面的方法
    private void changeFragment(Pane toShow) {
        sale_Pane.setVisible(false);
        rci_Pane.setVisible(false);
        cl_Pane.setVisible(false);
        smallSheet_Pane.setVisible(false);
        resetBaseData_Pane.setVisible(false);
        bill_Pane.setVisible(false);
        //二级菜单隐藏且不占位置
        resetBaseDataItem.setVisible(false);
        resetBaseDataItem.setManaged(false);
        smallSheetItem.setVisible(false);
        smallSheetItem.setManaged(false);

        toShow.setVisible(true);
    }

    @Resource
    RetailTradeAggregationActivity retailTradeAggregationActivity;

    //交班提示框
    private void show_RetailTradeAggregation_Dialog() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/logoutDialog.fxml"));

        JFXAlert alert = new JFXAlert(StageController.get().getStageBy(StageType.FRAGMENT_STAGE.name()));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        try {
            alert.setContent((BorderPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        alert.show();

        LogoutDialogViewController dialogViewController = loader.getController();
        dialogViewController.setAlert(alert);
        dialogViewController.setSureClickListener(() -> {
            try {
                workTimeEnd = Constants.getSimpleDateFormat().format(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                Intent intent = new Intent();
                intent.putExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd(), workTimeEnd);
                StageController.get().closeStge(StageType.FRAGMENT_STAGE.name());
                retailTradeAggregationActivity.setIntent(intent);
                retailTradeAggregationActivity.start(new Stage());
                alert.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
    public void printSmallSheet(RetailTrade retailTrade) throws Exception {
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            ToastUtil.toast("无法打印小票，加载小票格式失败", ToastUtil.LENGTH_SHORT);
            return;
        }
        //
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID((currentSmallSheetID));
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
                    30,//字号30
                    true,//加粗
                    17,//居中
                    false);//无下划线
            AidlUtil.getInstance().linewrap(1);//分隔一行
        }

        //每一行判断控件的状态进行设置
        if (smallSheetFrame1.getLogo() != null && !smallSheetFrame1.getLogo().equals("")) {
            // TODO: 2020/6/29
//            AidlUtil.getInstance().printBitmap(GeneralUtil.stringToBitmap(smallSheetFrame1.getLogo()));
//            AidlUtil.getInstance().printDivider(smallSheetFrame1.getDelimiterToRepeat());
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
        int[] colsWidthArrc = new int[]{2, 1, 1};//每一列所占权重
        int[] colsAlign = new int[]{0, 1, 2};//每一列对齐方式
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
        configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
        if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralFromDB)) {
            log.error("更新默认小票格式失败！configGeneral=" + configGeneralFromDB);
        }
        return Integer.valueOf(String.valueOf(SmallSheetController.Default_SmallSheetID_INPos));
    }

    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    public void closeLoadingDialog() {
        super.closeLoadingDialog();
    }

    public void showToastMessage(String message) {
        super.showToastMessage(message);
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


    public void initTimers() {   //做一些timer初始化操作
        retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeAggregationSQLiteBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        //
        retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        retailTradeAggregationHttpBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationHttpBO.setHttpEvent(retailTradeAggregationHttpEvent);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        //
        staffHttpEvent.setId(BaseEvent.EVENT_ID_MainStage);
        staffHttpBO.setHttpEvent(staffHttpEvent);
        staffHttpEvent.setHttpBO(staffHttpBO);
        //
//        timerCheckInvalidSession = new Timer();
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
        //
//        // 启动会话被踢出检测或会话过期检测。若被踢出或过期，UI自动跳到登录界面
//        if (timerCheckInvalidSession != null) {
//            timerTaskCheckInvalidSession = new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        if (BaseHttpEvent.HttpRequestStatus == 1) {
//                            Platform.runLater(() -> {
//                                timerCheckInvalidSession.cancel(); //set null 放在onDestroy()中
//                                BaseHttpEvent.HttpRequestStatus = 0;
//                                GlobalController.getInstance().setSessionID(null);
//                                log.info("sessionID已清除");
//                                Constants.setCurrentStaff(null); //稳健的做法
//                                //
//                                Main.exitFromSyncThread();
//                                //
//                                returnToLoginActivity();
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            timerCheckInvalidSession.schedule(timerTaskCheckInvalidSession, 0, 1000);
//        }


        log.debug("正在初始化跨天上班的Timer");
        timerToStartUploadRetailTradeAggregation = new Timer(true);
        registerDayEndGenerateRetailTradeAggregationBroadcast(timerToStartUploadRetailTradeAggregation);

        //
    }


    /**
     * 收银员跨天上班时，在23:59:30上传今天的收银汇总，在第二天的00:00:00后，重新创建一张收银汇总到本地sqlite中
     */
    private void registerDayEndGenerateRetailTradeAggregationBroadcast(Timer timer) {
        Date targetTime = DatetimeUtil.mergeDateAndTime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), Constants.START_TIME_UploadRetailTradeAggregation, Constants.DATE_FORMAT_Default);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("跨天定时任务开始");
                BaseController.retailTradeHoldBillList.clear();
                //用户输入过准备金，才有BaseActivity.retailTradeAggregation.getID() != null，才算是在收银了，才可以上传收银汇总
                if (BaseController.retailTradeAggregation.getID() != null) {
                    // TODO 广播
//                    if(dayEndGenerateRetailTradeAggregationReceiver == null){
//                        intentFilter = new IntentFilter();
//                        intentFilter.addAction(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
//                        //
//                        dayEndGenerateRetailTradeAggregationReceiver = new DayEndGenerateRetailTradeAggregationReceiver();
//                        localBroadcastManager.registerReceiver(dayEndGenerateRetailTradeAggregationReceiver, intentFilter);//注册向本地所有activity发送弹出EndDayAlertDialog消息的本地广播监听器
//                    }
//                    Intent intent = new Intent(DayEndGenerateRetailTradeAggregationReceiver.ACTION_NAME_BroadcastDayEndGenerateRetailTradeAggregation);
//                    localBroadcastManager.sendBroadcast(intent);
//                    Message message = new Message();
//                    message.what = 10001;
//                    PlatForm.get().sendMessage(message);
                    Platform.runLater(() -> {
                        showDaySpanLoadingDialog();
                    });
                    BaseController.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    BaseController.retailTradeAggregation.setPosID(Constants.posID);
                    final Timer closeAlertDialogTimer = new Timer(true);
                    TimerTask closeAlertDialogTimerTask = new TimerTask() {
                        int closeAlertDialogSecond = Constants.WAITING_TIME_UploadRetailTradeAggregation;

                        @Override
                        public void run() {
                            if (closeAlertDialogSecond > 0) {
                                updateDaySpanCountDown(String.valueOf(closeAlertDialogSecond));
                                closeAlertDialogSecond--;
                            } else {
                                // 直接修改主线程的UI（关闭跨天系统结算对话框）
                                Platform.runLater(() -> {
                                    createRetailTradeAggregationForNextDay();
                                    closedaySpanLoadingDialog();
                                });
                                closeAlertDialogTimer.cancel();
                                closeAlertDialogTimer.purge();
                            }
                        }
                    };
                    closeAlertDialogTimer.schedule(closeAlertDialogTimerTask, 0/*delay*/, 1000/*period*/); //0秒后，每隔1秒发送消息给UI，以刷新秒数
                    //更新本地的临时收银汇总
                    if (updateRetailTradeAggregationAtDayEnd()) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible()) {
                            log.info("在接近第二天的时候且有网络，上传收银汇总");
                            uploadRetailTradeAggregation(BaseController.retailTradeAggregation);
                        } else {
                            log.info("无网络进行跨天上班，不上传收银汇总");
                        }
                        // 需要打印机
                        printRetailTradeAggregation();
                    } else {
                        log.error("跨天上班时，更新今天本地的收银汇总失败！");
                    }
                } else {
                    //现在本地中创建一张今日空的收银汇总如果有网则上传。
                    RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
                    retailTradeAggregation.setPosID(Constants.posID);
                    retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
                    retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
                    retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    if (createRetailTradeAggregationInSqlite(retailTradeAggregation)) {
                        if (GlobalController.getInstance().getSessionID() != null && networkUtils.isNetworkAvalible()) {
                            uploadRetailTradeAggregation(retailTradeAggregation);
                        } else {
                            log.info("没输入准备金且无网络进行跨天上班,不上传收银汇总");
                        }
                    } else {
                        log.error("用户不输入准备金进行跨天上班,自动在本地生成今日的收银汇总失败！");
                    }
                    //
                    Main.exitFromSyncThread();
                    GlobalController.getInstance().setSessionID(null);
                    log.info("sessionID已清除");
                    Platform.runLater(() -> {
                        if (AllFragmentViewController.timerToStartUploadRetailTradeAggregation != null) {
                            System.out.println("退出登录，关闭跨天定时任务");
                            AllFragmentViewController.timerToStartUploadRetailTradeAggregation.cancel();
                            AllFragmentViewController.timerToStartUploadRetailTradeAggregation.purge();
                        }
                        MainController.firstTimeEnterMain1Activity = true;
                        loginActivity.setIntent(new Intent());
                        try {
                            loginActivity.start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.info("关闭 AllFramentController 页面");
                        System.out.println("关闭 AllFramentController 页面");
                        StageController.get().closeStge(StageType.FRAGMENT_STAGE.name()).unloadStage(StageType.FRAGMENT_STAGE.name());
                    });
                }
            }
        }, targetTime, 24 * 60 * 60 * 1000); // 从 targetTime 时刻开始，每隔24小时执行一次。
    }

    private boolean updateRetailTradeAggregationAtDayEnd() {
        RetailTradeAggregation updateRetailTradeAggregation = null;
        try {
            updateRetailTradeAggregation = (RetailTradeAggregation) BaseController.retailTradeAggregation.clone();
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
            AidlUtil.getInstance().printText("上班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseController.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
            AidlUtil.getInstance().printText("下班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseController.retailTradeAggregation.getWorkTimeEnd()), 25, false, 51, false);
            AidlUtil.getInstance().printText("交班人：" + Constants.getCurrentStaff().getName(), 30, false, 51, false);
            AidlUtil.getInstance().linewrap(1);
            AidlUtil.getInstance().printText("------收银汇总------", 35, false, 17, false);
            AidlUtil.getInstance().printText("交易单数：" + BaseController.retailTradeAggregation.getTradeNO(), 30, false, 51, false);
            AidlUtil.getInstance().printText("营业额：" + BaseController.retailTradeAggregation.getAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("准备金：" + BaseController.retailTradeAggregation.getReserveAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("现金收入：" + BaseController.retailTradeAggregation.getCashAmount(), 30, false, 51, false);
            AidlUtil.getInstance().printText("微信收入：" + BaseController.retailTradeAggregation.getWechatAmount(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("支付宝收入：" + showAlipayAmount.getText().toString(), 30, false, 51, false);
            AidlUtil.getInstance().printDivider("-");
            AidlUtil.getInstance().printText("交班人签名：", 30, false, 51, false);
            AidlUtil.getInstance().linewrap(8);
            AidlUtil.getInstance().cutPaper();
        } catch (Exception e) {
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
     * 当收银员跨天上班，到了第二天后，往sqlite插入一条收银汇总
     */
    private void createRetailTradeAggregationForNextDay() {
        System.out.println("创建下一天的收银汇总");
        Date tmr = DatetimeUtil.get2ndDayStart(BaseController.retailTradeAggregation.getWorkTimeStart());
        if (!DatetimeUtil.isAfterDate(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference), tmr, 0)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //初始化 BaseActivity.retailTradeAggregation的部分数据
        if (BaseController.retailTradeAggregation != null) {
            BaseController.retailTradeAggregation.setWorkTimeStart(tmr);
            BaseController.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseController.retailTradeAggregation.getWorkTimeStart(), 1));
            BaseController.retailTradeAggregation.setTradeNO(0);
            BaseController.retailTradeAggregation.setAmount(0.000000d);
            BaseController.retailTradeAggregation.setCashAmount(0.000000d);
            BaseController.retailTradeAggregation.setWechatAmount(0.000000d);
            //
            RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
            retailTradeAggregation.setStaffID(BaseController.retailTradeAggregation.getStaffID());
            retailTradeAggregation.setPosID(Constants.posID);
            retailTradeAggregation.setWorkTimeStart(BaseController.retailTradeAggregation.getWorkTimeStart());
            retailTradeAggregation.setReserveAmount(BaseController.retailTradeAggregation.getReserveAmount());
            retailTradeAggregation.setWorkTimeEnd(BaseController.retailTradeAggregation.getWorkTimeEnd());
            //
            retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
            if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
                log.error("跨天上班时，创建零售单汇总失败");//TODO 未处理结果事件
            }
        }
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


    private void returnToLoginActivity() {
        Main.exitFromSyncThread();
        closeLoadingDialog();
        //
        initialization();//点击登录后会调用，所以这里不call也行

        StageController.get().closeAllstages();
        log.info("已将所有的Activity finish()，接下来进入loginActivity");

        try {
            Intent intent = new Intent();
            intent.putExtra("LoginInterceptor", 1);
            intent.putExtra("LoginInterceptorWarn", BaseHttpEvent.HttpRequestWarnMsg);
            loginActivity.setIntent(intent);
            loginActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出登录后，重置firstTimeEnterMain1Activity
        MainController.firstTimeEnterMain1Activity = true;
    }

    //通过线程每0.1秒发送一次消息
    public class AppTimeThread extends Thread {
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(100);
                    Platform.runLater(() -> {
                        long now = System.currentTimeMillis();
                        now = now + 100 + NtpHttpBO.TimeDifference;//当前的时间，加上sleep的0.1秒，再加上服务器与Pos机的时间差
                        if (show_time != null) {
                            show_time.setWrapText(true); // 自动换行
                            show_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEEEEEEE").format(new Date(now)));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (timeThreadCanRun);
        }
    }
}
