package wpos.allController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.FragmentActivity;
import wpos.application.LoginActivity;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.RetailTradeAggregationSQLiteEvent;
import wpos.event.UI.RetailTradeSQLiteEvent;
import wpos.helper.Constants;
import wpos.listener.PlatFormHandlerMessage;
import wpos.listener.Subscribe;
import wpos.model.ErrorInfo;
import wpos.model.Intent;
import wpos.model.Message;
import wpos.model.RetailTradeAggregation;
import wpos.utils.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("retailTradeAggregationViewController")
public class RetailTradeAggregationViewController extends BaseController implements PlatFormHandlerMessage {
    @FXML
    Label showTradeNo;
    @FXML
    Label showReserveAmount;
    @FXML
    Label showCashAmount;
    @FXML
    Label showWechatAmount;
    @FXML
    Label showWorkTimeEnd;
    @FXML
    Label operator;
    @FXML
    ComboBox shift_schedule_number;
    @FXML
    Button change_shifts;

    @FXML
    Label showAmount;

    private Logger log = Logger.getLogger(this.getClass());
    private String tradeNo;//交易单数
    private String amount;//营业额
    private String reserveAmount;//准备金
    private String cashAmount;//现金支付
    private String wechatAmount;//微信收入
    private String alipayAmount;//支付宝支付
    private String workTimeEnd;//交班时间

    @Resource
    public RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO;
    @Resource
    public RetailTradeAggregationHttpBO retailTradeAggregationHttpBO;

    @Resource
    public RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;
    @Resource
    public RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent;
    @Resource
    public RetailTradeAggregationEvent retailTradeAggregationEvent;
    //
    @Resource
    public RetailTradeSQLiteBO retailTradeSQLiteBO;
    @Resource
    public RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    @Resource
    public RetailTradeHttpEvent retailTradeHttpEvent;
    @Resource
    public RetailTradeHttpBO retailTradeHttpBO;
    //
    @Resource
    public RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent;
    //
    @Resource
    public LogoutHttpBO logoutHttpBO = null;
    @Resource
    public LogoutHttpEvent logoutHttpEvent = null;

    @Resource
    public LoginActivity loginActivity;
    private Intent intent;

    @Resource
    FragmentActivity fragmentActivity;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
                //如果是会话过期,初始化常量即可，不在这里返回登录界面，而让定时器事件检测到会话过期并返回登录页面
                initialization();
                log.info("交班时会话过期,已将常量初始化");
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationSQLiteEvent(RetailTradeAggregationSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                Message message = new Message();
                message.what = 1;
                PlatForm.get().sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("RetailTradeAggregationActivity.onRetailTradeHttpEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处RetailTradeAggregationActivity.onRetailTradeHttpEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        log.info("#########################################################RetailTradeAggregationActivity onRetailTradeSQLiteEvent");
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationStage) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                ToastUtil.toast(event.getLastErrorMessage(), ToastUtil.LENGTH_SHORT);
            }
        } else if (event.getId() == BaseEvent.EVENT_ID_SyncThread) {
            if (!event.isEventProcessed()) {
                log.error("RetailTradeAggregationActivity.onRetailTradeSQLiteEvent()未处理的事件，但必须在SyncThread中处理！");
            }
            log.debug("该Event已经在SyncThread中处理，此处RetailTradeAggregationActivity.onRetailTradeSQLiteEvent()不用处理");
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
        if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
            Message message = new Message();
            message.what = 1;
            PlatForm.get().sendMessage(message);
        } else {
            //如果是会话过期,初始化常量即可，不在这里返回登录界面，而让定时器事件检测到会话过期并返回登录页面
            initialization();
            log.info("交班时会话过期,已将常量初始化");
        }
    }

    public RetailTradeAggregationViewController() {
    }

    public void onCreate(Intent intent) {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);
        this.intent = intent;
        initEventAndBO();
        receiveValue();
        showValue();
        setSpinner();//设置打印数量

    }

    @FXML   //返回图标点击事件
    private void backTo() {
        // eventbus这些都被注销了，应该不能简单的showStage
        try {
            fragmentActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        StageController.get().closeStge(StageType.RETAILTRADEAGGREGATION_STAGE.name()).unloadStage(StageType.RETAILTRADEAGGREGATION_STAGE.name());
    }

    private void setSpinner() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(ListData());
        shift_schedule_number.setItems(observableList);
    }

    private List<Integer> ListData() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        return list;
    }

    private void returnToLoginActivity() {
        // TODO
//        ((AppApplication) getApplication()).exitFromSyncThread();
        Main.exitFromSyncThread();
        closeLoadingDialog();
        //
        initialization();//点击登录后会调用，所以这里不call也行

        StageController.get().closeAllstages();
        log.info("已将所有的Activity finish()，接下来进入loginActivity");
        loginActivity.setIntent(new Intent()); // 重置intent，防止上一次的重复登录信息依然保留着
        try {
            loginActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出登录后，重置firstTimeEnterMain1Activity
        MainController.firstTimeEnterMain1Activity = true;
        if (AllFragmentViewController.timerPing != null) {
            AllFragmentViewController.timerTaskPing.cancel();
            AllFragmentViewController.timerPing.purge();
        }
        AllFragmentViewController.timeThreadCanRun = false;
    }

    private void initEventAndBO() {
        retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        retailTradeAggregationSQLiteBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationSQLiteBO.setHttpEvent(retailTradeAggregationHttpEvent);
        retailTradeAggregationHttpBO.setSqLiteEvent(retailTradeAggregationSQLiteEvent);
        retailTradeAggregationHttpBO.setHttpEvent(retailTradeAggregationHttpEvent);

        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        retailTradeSQLiteBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeSQLiteBO.setHttpEvent(retailTradeHttpEvent);

        retailTradeHttpBO.setSqLiteEvent(retailTradeSQLiteEvent);
        retailTradeHttpBO.setHttpEvent(retailTradeHttpEvent);

        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        //
        logoutHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        retailTradeAggregationEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationStage);
    }

    /**
     * 接收其他Activity传过来的值，为上传收银汇总需要用到的数据设值(订单数量，金额，现金支付金额，微信支付金额......)
     */
    private void receiveValue() {
        tradeNo = String.valueOf(BaseController.retailTradeAggregation.getTradeNO());
        amount = String.valueOf(BaseController.retailTradeAggregation.getAmount());
        reserveAmount = String.valueOf(BaseController.retailTradeAggregation.getReserveAmount());
        cashAmount = String.valueOf(BaseController.retailTradeAggregation.getCashAmount());
        wechatAmount = String.valueOf(BaseController.retailTradeAggregation.getWechatAmount());
        workTimeEnd = intent.getStringExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd());
    }

    /**
     * 在UI上展示设置好的值
     */
    private void showValue() {
        showTradeNo.setText("null".equals(tradeNo) ? "0" : tradeNo);
        showAmount.setText(GeneralUtil.formatToShow(amount == null ? 0.000000d : Double.parseDouble(amount)));
        showReserveAmount.setText(reserveAmount);
        showCashAmount.setText(GeneralUtil.formatToShow(cashAmount == null ? 0.000000d : Double.parseDouble(cashAmount)));
        showWechatAmount.setText(GeneralUtil.formatToShow(wechatAmount == null ? 0.000000d : Double.parseDouble(wechatAmount)));
        showWorkTimeEnd.setText(workTimeEnd);
        operator.setText(Constants.getCurrentStaff().getName());
    }

    //交班按钮点击事件
    @FXML
    public void change_shifts() {
        change_shifts.setDisable(true);
        showLoadingDialog();
        //打印收银汇总信息
        if (Constants.getCurrentStaff().getRoleID() != Constants.preSale_Role_ID) {
            int print_number = 0;
            if (shift_schedule_number.getSelectionModel().selectedIndexProperty().getValue() == -1) {
                print_number = 1;
            } else {
                print_number = shift_schedule_number.getSelectionModel().selectedIndexProperty().getValue() + 1;
            }
            for (int i = 0; i < print_number; i++) {
                try {
                    AidlUtil.getInstance().printText("交班确认表", 30, false, 17, false);
                    AidlUtil.getInstance().printDivider("-");
                    AidlUtil.getInstance().printText("上班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseController.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
                    AidlUtil.getInstance().printText("下班时间：" + workTimeEnd, 25, false, 51, false);
                    AidlUtil.getInstance().printText("交班人：" + operator.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().linewrap(1);
                    AidlUtil.getInstance().printText("------收银汇总------", 35, false, 17, false);
                    AidlUtil.getInstance().printText("交易单数：" + showTradeNo.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().printText("营业额：" + showAmount.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().printText("准备金：" + showReserveAmount.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().printText("现金收入：" + showCashAmount.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().printText("微信收入：" + showWechatAmount.getText().toString(), 30, false, 51, false);
                    AidlUtil.getInstance().printDivider("-");
                    AidlUtil.getInstance().printText("交班人签名：", 30, false, 51, false);
                    AidlUtil.getInstance().linewrap(8);
                    AidlUtil.getInstance().cutPaper();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 不管有网没网，将当前时间设为零售汇总的下班时间,更新本地临时的收银汇总
            BaseController.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            updateRetailTradeAggregationInSqliteSync(BaseController.retailTradeAggregation);

            if (GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible()) {
                //没网的时候,直接退出,回到登录页面
                returnToLoginActivity();
            } else {
                //...以下2步，使用TaskScheduler完成。然后post事件XXX到本类
                new Thread(() -> {
                    try {
                        uploadTempRetailTrade();

                        uploadRetailTradeAggregation(BaseController.retailTradeAggregation);

                        if (GlobalController.getInstance().getSessionID() != null) {
                            if (!logoutHttpBO.logoutAsync()) {
                                log.error("退出登录失败! ");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("交班时出现异常：" + e.getMessage());
                    }
                }).start();
            }
        } else {
            if (!logoutHttpBO.logoutAsync()) {
                log.info("退出登录失败! ");
            }
        }
    }

    /**
     * 上传残余零售单
     */
    private void uploadTempRetailTrade() throws InterruptedException {
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            log.error("查询临时零售单失败！");
        }

        long lTimeOut = TIME_OUT;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            log.error("上传临时零售单同步数据失败！原因：超时！");
        }
    }

    /**
     * TODO 其实直接call 同步的接口就行。
     */
    private void updateRetailTradeAggregationInSqliteSync(RetailTradeAggregation retailTradeAggregation) {
        log.debug("点击交班进行更新收银汇总retailTradeAggregation=" + retailTradeAggregation);
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
            log.error("更新收银汇总单失败！错误码为" + retailTradeAggregationSQLiteBO.getSqLiteEvent().getLastErrorCode());
            //...
        }
    }

    private void uploadRetailTradeAggregation(RetailTradeAggregation retailTradeAggregation) {
        log.debug("交班，上传的收银汇总=" + retailTradeAggregation);
        /*上传当前的零售汇总到服务器 TODO 下面几行代码互相有紧密的关系，但是看起来像没有关系 */
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        retailTradeAggregationSQLiteEvent.setBaseModel1(retailTradeAggregation); //为了在服务器返回对象后，还能知道原先的对象是什么，然后把它从SQLite中删除。目前SQLite不保存收银汇总。BaseModel1保存旧数据，BaseModel2保存新数据
        if (!retailTradeAggregationHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTradeAggregation)) {
            log.error("上传当前的零售汇总到服务器失败");
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

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (AllFragmentViewController.timerToStartUploadRetailTradeAggregation != null) {
                    System.out.println("退出登录，关闭跨天定时任务");
                    AllFragmentViewController.timerToStartUploadRetailTradeAggregation.cancel();
                    AllFragmentViewController.timerToStartUploadRetailTradeAggregation.purge();
                }
                returnToLoginActivity(); //收银汇总完毕后，必须返回到登录界面
                break;
        }
    }
}
