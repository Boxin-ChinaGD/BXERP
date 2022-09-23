package com.bx.erp.view.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.AppApplication;
import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.common.ActivityController;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.RetailTradeAggregationEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.NetworkUtils;
import com.silencedut.taskscheduler.TaskScheduler;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetailTradeAggregation1Activity extends BaseActivity {
    @BindView(R.id.showAmount)
    TextView showAmount;
    @BindView(R.id.showTradeNo)
    TextView showTradeNo;
    @BindView(R.id.showReserveAmount)
    TextView showReserveAmount;
    @BindView(R.id.showCashAmount)
    TextView showCashAmount;
    @BindView(R.id.showWechatAmount)
    TextView showWechatAmount;
    @BindView(R.id.showWorkTimeEnd)
    TextView showWorkTimeEnd;
    @BindView(R.id.operator)
    TextView operator;
    @BindView(R.id.change_shifts_record)
    TextView change_shifts_record;
    @BindView(R.id.shift_schedule_number)
    Spinner shift_schedule_number;
    @BindView(R.id.change_shifts)
    TextView change_shifts;
    @BindView(R.id.back)
    ImageView back;

    private Logger log = Logger.getLogger(this.getClass());
    private String tradeNo;//交易单数
    private String amount;//营业额
    private String reserveAmount;//准备金
    private String cashAmount;//现金支付
    private String wechatAmount;//微信收入
    private String alipayAmount;//支付宝支付
    private String workTimeEnd;//交班时间

    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;

    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;
    private static RetailTradeAggregationEvent retailTradeAggregationEvent = null;
    //
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    //
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    //
    private static LogoutHttpBO logoutHttpBO = null;
    private static LogoutHttpEvent logoutHttpEvent = null;

    private static NetworkUtils networkUtils = new NetworkUtils();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailtradeaggregation_layout1);
        ButterKnife.bind(this);
        initObject();
        receiveValue();
        showValue();
        setSpinner();//设置打印数量

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRetailTradeAggregationHttpEvent(RetailTradeAggregationHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
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
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                returnToLoginActivity(); //收银汇总完毕后，必须返回到登录界面
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                Toast.makeText(getApplicationContext(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
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
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                Toast.makeText(getApplicationContext(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
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
        if (event.getId() == BaseEvent.EVENT_ID_RetailTradeAggregationActivity) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
        if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_SessionTimeout) {
            returnToLoginActivity();
        } else {
            //如果是会话过期,初始化常量即可，不在这里返回登录界面，而让定时器事件检测到会话过期并返回登录页面
            initialization();
            log.info("交班时会话过期,已将常量初始化");
        }
    }

    private void setSpinner() {
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item2, ListData());
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        shift_schedule_number.setAdapter(arrayAdapter);
        shift_schedule_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

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
        ((AppApplication) getApplication()).exitFromSyncThread();
        closeLoadingDialog(loadingDailog);
        //
        initialization();//点击登录后会调用，所以这里不call也行

        ActivityController.finishAll();
        log.info("已将所有的Activity finish()，接下来进入loginActivity");

        Intent logoutIntent = new Intent(RetailTradeAggregation1Activity.this, Login1Activity.class);
        startActivity(logoutIntent);
    }

    private void initObject() {
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }

        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
        //
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }

        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (retailTradeAggregationEvent == null) {
            retailTradeAggregationEvent = new RetailTradeAggregationEvent();
            retailTradeAggregationEvent.setId(BaseEvent.EVENT_ID_RetailTradeAggregationActivity);
        }
    }

    /**
     * 接收其他Activity传过来的值，为上传收银汇总需要用到的数据设值(订单数量，金额，现金支付金额，微信支付金额......)
     */
    private void receiveValue() {
        tradeNo = String.valueOf(BaseActivity.retailTradeAggregation.getTradeNO());
        amount = String.valueOf(BaseActivity.retailTradeAggregation.getAmount());
        reserveAmount = String.valueOf(BaseActivity.retailTradeAggregation.getReserveAmount());
        cashAmount = String.valueOf(BaseActivity.retailTradeAggregation.getCashAmount());
        wechatAmount = String.valueOf(BaseActivity.retailTradeAggregation.getWechatAmount());
        workTimeEnd = getIntent().getStringExtra(new RetailTradeAggregation().field.getFIELD_NAME_workTimeEnd());
    }

    /**
     * 在UI上展示设置好的值
     */
    private void showValue() {
        showTradeNo.setText("null".equals(tradeNo) ? "0" : tradeNo);
        showAmount.setText(GeneralUtil.formatToShow(amount == null ? 0.000000d : Double.valueOf(amount)));
        showReserveAmount.setText(reserveAmount);
        showCashAmount.setText(GeneralUtil.formatToShow(cashAmount == null ? 0.000000d : Double.valueOf(cashAmount)));
        showWechatAmount.setText(GeneralUtil.formatToShow(wechatAmount == null ? 0.000000d : Double.valueOf(wechatAmount)));
//        showAlipayAmount.setText(alipayAmount == null ? "0" : alipayAmount);
        showWorkTimeEnd.setText(workTimeEnd);
        operator.setText(Constants.getCurrentStaff().getName());
    }

    @OnClick({R.id.change_shifts, R.id.print_shift_schedule_number, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_shifts:
                change_shifts.setEnabled(false);
                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
                //打印收银汇总信息
                if (Constants.getCurrentStaff().getRoleID() != Constants.preSale_Role_ID) {
                    int print_number = Integer.valueOf(shift_schedule_number.getSelectedItemPosition() + 1);//getSelectedItemPosition为0时，获取到的数据是1；以此类推
                    for (int i = 0; i < print_number; i++) {
                        try {
                            AidlUtil.getInstance().printText("交班确认表", 30, false, 17, false);
                            AidlUtil.getInstance().printDivider("-");
                            AidlUtil.getInstance().printText("上班时间：" + new SimpleDateFormat(Constants.DATE_FORMAT_Default).format(BaseActivity.retailTradeAggregation.getWorkTimeStart()), 25, false, 51, false);
                            AidlUtil.getInstance().printText("下班时间：" + workTimeEnd, 25, false, 51, false);
                            AidlUtil.getInstance().printText("交班人：" + operator.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().linewrap(1);
                            AidlUtil.getInstance().printText("------收银汇总------", 35, false, 17, false);
                            AidlUtil.getInstance().printText("交易单数：" + showTradeNo.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().printText("营业额：" + showAmount.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().printText("准备金：" + showReserveAmount.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().printText("现金收入：" + showCashAmount.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().printText("微信收入：" + showWechatAmount.getText().toString(), 30, false, 51, false);
//                            AidlUtil.getInstance().printText("支付宝收入：" + showAlipayAmount.getText().toString(), 30, false, 51, false);
                            AidlUtil.getInstance().printDivider("-");
                            AidlUtil.getInstance().printText("交班人签名：", 30, false, 51, false);
                            AidlUtil.getInstance().linewrap(8);
                            AidlUtil.getInstance().cutPaper();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    // 不管有网没网，将当前时间设为零售汇总的下班时间,更新本地临时的收银汇总
                    BaseActivity.retailTradeAggregation.setWorkTimeEnd(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    updateRetailTradeAggregationInSqliteSync(BaseActivity.retailTradeAggregation);

                    if (GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(getApplicationContext())) {
                        //没网的时候,直接退出,回到登录页面
                        returnToLoginActivity();
                    } else {
                        //...以下2步，使用TaskScheduler完成。然后post事件XXX到本类
                        TaskScheduler.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    uploadTempRetailTrade();

                                    uploadRetailTradeAggregation(BaseActivity.retailTradeAggregation);

                                    if (GlobalController.getInstance().getSessionID() != null) {
                                        if (!logoutHttpBO.logoutAsync()) {
                                            log.error("退出登录失败! ");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.error("交班时出现异常：" + e.getMessage());
                                }
                                //...判断错误码2，MSG2。如果有错误，在这里POST EVENT
//                                EventBus.getDefault().post(retailTradeAggregationEvent);
                            }
                        });
                    }
                } else {
                    if (!logoutHttpBO.logoutAsync()) {
                        log.info("退出登录失败! ");
                    }
                }
                break;

            case R.id.back: //退出交班页面，返回销售页面
                finish();
                break;
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

    /**
     * 点击选择打印交班表数量
     */
//    private void clickPrintNumber() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenWidth = dm.widthPixels;//屏幕宽度
//        int screenHeight = dm.heightPixels;//屏幕高度
//        //
//        AlertDialog.Builder shift_schedule_number_builder = new AlertDialog.Builder(RetailTradeAggregation1Activity.this);
//        View shift_schedule_number_view = View.inflate(getApplicationContext(), R.layout.print_shift_schedule_number_dialog, null);
//        shift_schedule_number_builder.setCancelable(true);
//        shift_schedule_number_builder.setView(shift_schedule_number_view);
//        final AlertDialog shift_schedule_number_dialog = shift_schedule_number_builder.create();
//        shift_schedule_number_dialog.show();
//        WindowManager.LayoutParams params = shift_schedule_number_dialog.getWindow().getAttributes();
//        params.width = (int) (screenWidth * 0.3);
//        params.height = (int) (screenHeight * 0.52);
//        shift_schedule_number_dialog.getWindow().setAttributes(params);
//        //
//        RelativeLayout print_one = shift_schedule_number_view.findViewById(R.id.print_one);
//        RelativeLayout print_two = shift_schedule_number_view.findViewById(R.id.print_two);
//        RelativeLayout print_three = shift_schedule_number_view.findViewById(R.id.print_three);
//        RelativeLayout print_four = shift_schedule_number_view.findViewById(R.id.print_four);
//        TextView cancel = shift_schedule_number_view.findViewById(R.id.cancel);
//        final ImageView selected_one = shift_schedule_number_view.findViewById(R.id.selected_one);
//        final ImageView selected_two = shift_schedule_number_view.findViewById(R.id.selected_two);
//        final ImageView selected_three = shift_schedule_number_view.findViewById(R.id.selected_three);
//        final ImageView selected_four = shift_schedule_number_view.findViewById(R.id.selected_four);
//        print_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.VISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("1");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.VISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("2");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_three.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.VISIBLE);
//                selected_four.setVisibility(View.INVISIBLE);
//                shift_schedule_number.setText("3");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        print_four.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected_one.setVisibility(View.INVISIBLE);
//                selected_two.setVisibility(View.INVISIBLE);
//                selected_three.setVisibility(View.INVISIBLE);
//                selected_four.setVisibility(View.VISIBLE);
//                shift_schedule_number.setText("4");
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shift_schedule_number_dialog.dismiss();
//            }
//        });
//    }
}
