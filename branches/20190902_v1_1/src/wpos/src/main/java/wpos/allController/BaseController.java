package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import wpos.StageController;
import wpos.model.Commodity;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeAggregation;
import wpos.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseController {
    /**
     * 保存最近写进SQLite的收银汇总。交易单数在退货时不需要更改，只记录零售的交易单数
     * 生：当用户登录成功并输入准备金后，会向SQLite插入一个最初的收银汇总。
     * 死：交班时，会上传收银汇总。上传后，本变量设置为null
     */
    public static RetailTradeAggregation retailTradeAggregation = null;

    public static final int MAX_HOLD_BILL_NO = 10;

    /**
     * UI层操作等待的时间，以秒为单位。WxPay_TIME_OUT设为60是因为微信支付较为特殊，超时时间较长，普通UI操作不会这么久
     */
    protected final long TIME_OUT = 30;

    /**
     * barcode模糊搜索的长度限制：必须>6
     * TODO 重命名此变量
     */
    public static int FUZZY_QUERY_LENGTH = 6; //模糊查询的长度条件；

    /**
     * 待售商品列表
     */
    public static List<Commodity> showCommList = new ArrayList<Commodity>();
    public static RetailTrade retailTrade = new RetailTrade();//用于记录零售单
    public static List<RetailTrade> retailTradeHoldBillList = new ArrayList<RetailTrade>();//用于存放挂单
    public static RetailTrade lastRetailTrade;//用于记录刚卖出的零售单
    public static String lastRetailTradePaymentType;//上一单的支付方式
    public static double lastRetailTradeChangeMoney;//上一单的找零金额
    public static double lastRetailTradeAmount;//上一单的总金额，用于结算完成时在首页展示

    /*
     * 纯现金支付的零售，进行全部商品退货，允许存在一分钱的误差：
     * */
    public static final double TORELANCE_ReturnWholeRetailTradeAllPaidByCash = 0.010000d;

    static JFXAlert loadingDialog;
    static LoadingDialogViewController loadingDialogViewController;
    FXMLLoader loadingDialogLoader;

    // 跨天结算对话框
    JFXAlert daySpanloadingDialog;
    DaySpanLoadingDialogViewController daySpanLoadingDialogViewController;
    FXMLLoader daySpanloadingDialogLoader;

    public BaseController() {

    }

    public void showDaySpanLoadingDialog() {
        if (daySpanloadingDialog == null) {
            daySpanloadingDialogLoader = new FXMLLoader();
            daySpanloadingDialogLoader.setLocation(getClass().getResource("/ui/daySpanLoadingDialog.fxml"));
            daySpanloadingDialog = new JFXAlert(StageController.get().getShowingStage());
            daySpanloadingDialog.initModality(Modality.APPLICATION_MODAL);
            daySpanloadingDialog.setOverlayClose(false);

            try {
                daySpanloadingDialog.setContent((FlowPane) daySpanloadingDialogLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            daySpanLoadingDialogViewController = daySpanloadingDialogLoader.getController();
            daySpanLoadingDialogViewController.setAlert(daySpanloadingDialog);
        }
        daySpanloadingDialog.show();
//        daySpanLoadingDialogViewController.startAnimotion();
    }

    public void updateDaySpanCountDown(String text) {
        if (daySpanloadingDialog == null) {
            return;
        }
        if (daySpanloadingDialog.isShowing()) {
            Platform.runLater(() -> {
                daySpanLoadingDialogViewController.daySpanCountDown.setText("请等待" + text + "秒...");

            });
        }
    }

    public void closedaySpanLoadingDialog() {
        if (daySpanloadingDialog == null) {
            return;
        }
        if (daySpanloadingDialog.isShowing()) {
//            daySpanLoadingDialogViewController.endAnimotion();
            daySpanloadingDialog.hideWithAnimation();
        }
    }

    public void closeLoadingDialog() {
        if (loadingDialog == null) {
            return;
        }
        if (loadingDialog.isShowing()) {
            loadingDialogViewController.endAnimotion();
            loadingDialog.hideWithAnimation();
        }
        // 不设为null，断网登录交班，再登录会出现加载中对话框没关闭
        loadingDialog = null;
    }

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialogLoader = new FXMLLoader();
            loadingDialogLoader.setLocation(getClass().getResource("/ui/loadingDialog.fxml"));
            loadingDialog = new JFXAlert(StageController.get().getShowingStage());
            loadingDialog.initModality(Modality.APPLICATION_MODAL);
            loadingDialog.setOverlayClose(false);

            try {
                loadingDialog.setContent((FlowPane) loadingDialogLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadingDialogViewController = loadingDialogLoader.getController();
            loadingDialogViewController.setAlert(loadingDialog);
        }
        loadingDialog.show();
        loadingDialogViewController.startAnimotion();
    }

    protected void showToastMessage(String message) {
        ToastUtil.toast(message, ToastUtil.LENGTH_SHORT);
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

}
