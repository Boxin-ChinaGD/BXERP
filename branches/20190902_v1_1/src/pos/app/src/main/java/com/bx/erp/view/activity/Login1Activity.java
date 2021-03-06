package com.bx.erp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CompanyHttpBO;
import com.bx.erp.bo.CompanySQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.PosHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PosSQLiteBO;
import com.bx.erp.bo.RememberLoginStaffSQLiteBO;
import com.bx.erp.bo.StaffHttpBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.StaffSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CompanyHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PosSQLiteEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CompanySQLiteEvent;
import com.bx.erp.event.UI.StaffSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RememberLoginStaff;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.Staff;
import com.bx.erp.presenter.PosPresenter;
import com.bx.erp.presenter.StaffPresenter;
import com.bx.erp.utils.ConfigureLog4J;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.MD5Util;
import com.bx.erp.utils.MD5Utils;
import com.bx.erp.utils.NetworkUtils;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.view.component.TranslationViewPager;
import com.mj.permission.DynamicPermissionEmitter;
import com.mj.permission.DynamicPermissionEntity;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * ???????????????
 * POS????????? 1.????????????SQLite??????Company?????????Company???????????????????????????????????????SN????????????????????????????????????SN??????????????????????????????SN
 * 2.???????????????SN???????????????SN??????????????????????????????????????????POS??????????????????
 * 3.??????POS?????????????????????????????????????????????????????????????????????????????????????????????SQLite
 * 4.??????POS???????????????????????????SN????????????????????????????????????
 * <p>
 * POS???STAFF???????????????????????????SN???
 */
public class Login1Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.remember_password)
    CheckBox cbRememberPassword;
    @BindView(R.id.translation_viewpager)
    ViewPager translationViewpager;
    @BindView(R.id.point_linear)
    LinearLayout pointLinear;
    private Logger log = Logger.getLogger(this.getClass());
    private static CompanySQLiteEvent companySQLiteEvent = null;
    private static CompanyHttpEvent companyHttpEvent = null;
    private static CompanySQLiteBO companySQLiteBO = null;
    private static CompanyHttpBO companyHttpBO = null;

    private StaffPresenter staffPresenter;

    private static PosHttpBO posHttpBO = null;
    private static PosHttpEvent posHttpEvent = null;
    private static PosSQLiteBO posSQLiteBO = null;
    private static PosSQLiteEvent posSQLiteEvent = null;

    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static PosLoginHttpBO posLoginHttpBO = null;

    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;

    private static StaffSQLiteEvent staffSQLiteEvent = null;
    private static StaffSQLiteBO staffSQLiteBO = null;
    private static StaffHttpEvent staffHttpEvent = null;
    private static StaffHttpBO staffHttpBO = null;

    private RememberLoginStaffSQLiteBO rememberLoginStaffSQLiteBO = null;

    /**
     * ??????????????????????????????????????????????????????UI?????????????????????????????????????????????????????????????????????timerTaskPing?????????ping?????????
     * ?????????????????????????????????????????????1?????????????????????2???Pos???????????????1???NBR??????????????????????????????????????????????????????????????????
     */
    private Timer timerCheckInvalidSession;

    /**
     * ??????timerTaskPing
     */
    private Timer timerPing;

    /**
     * ??????ping nbr??????????????????????????????????????????????????????30?????????App??????25??????ping??????
     */
    private TimerTask timerTaskPing;

    @BindView(R.id.login)
    protected TextView tvLogin;
    @BindView(R.id.iv_poslogin_tip)
    protected ImageView iv_poslogin_tip;
    @BindView(R.id.tv_posLogin_tip)
    protected TextView tv_posLogin_tip;
    @BindView(R.id.username)
    protected EditText username;
    @BindView(R.id.password)
    protected EditText password;
    @BindView(R.id.show_companysn)
    protected TextView showCompanySN;
    @BindView(R.id.username_linear)
    protected LinearLayout username_linear;
    @BindView(R.id.password_linear)
    protected LinearLayout password_linear;

    private EditText oldpasswordET;
    private EditText newPasswordET;
    private EditText confirmPasswordET;
    private EditText companySNET;

    private static NetworkUtils networkUtils = new NetworkUtils();

    private LoadingDailog loadingDailogFromCompanySN;
    private AlertDialog resetPasswordWarningDialog;
    private AlertDialog resetPasswordDialog;
    private AlertDialog inputCompanySNDialog;
    private AlertDialog loginInterceptorDialog;

    private int isCreateInterceptorDialog;

    private Staff staff = new Staff();
    private Company company = new Company();
    private Pos pos = null;

    /**
     * ????????????????????????
     */
    private List<Integer> ViewPagerImageId = new ArrayList<>();
    /**
     * ???????????????????????????
     */
    private List<Integer> PointCheck_unCheckId = new ArrayList<>();
    /**
     * ????????????viewpager
     */
    private TranslationViewPager ViewPager;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();
            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_POS_Retrieve1BySN && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done) {
                //???????????????????????????
                Company c = (Company) posHttpEvent.getBaseModel2();
                companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                companySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, c);
            } else {
                closeLoadingDialog(loadingDailogFromCompanySN);
                Toast.makeText(appApplication, (event.getData() == null ? "???????????????????????????" : (String) event.getData()), Toast.LENGTH_SHORT).show();
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                pos = (Pos) event.getBaseModel1();
                if (pos != null) {
                    //...Pos doLoginAsync
                    pos.setPasswordInPOS(pos.getPasswordInPOS());
//                    posLoginHttpEvent.setLoginStaffAfterLoginSuccess(false);
                    posLoginHttpBO.setPos(pos);
                    posLoginHttpBO.loginAsync();
                } else {
                    //??????Pos????????????????????????????????????????????????????????????????????????????????????SQLite
                    Pos p = (Pos) posHttpEvent.getBaseModel1();
                    if (p == null) {
                        closeLoadingDialog(loadingDailog);
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("????????????????????????????????????")
                                .setTitle("??????")
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    } else {
                        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                        posSQLiteBO.createReplacerAsync(BaseSQLiteBO.INVALID_CASE_ID, p);
                    }
                }
            }
            //???????????????????????????????????????????????????????????????
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Pos p = (Pos) event.getBaseModel1();
                p.setResetPasswordInPos(1);//...
//                posLoginHttpEvent.setLoginStaffAfterLoginSuccess(false);
                posLoginHttpBO.setPos(p);
                posLoginHttpBO.loginAsync();
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Override
    protected Timer getTimer() {
        return null;
    }

    /**
     * Caution! PosLoginHttpEvent is not PosHttpEvent!!!
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosGetToken) {
                return;
            }

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                closeLoadingDialog(loadingDailogFromCompanySN);
                closeLoadingDialog(loadingDailog);

                switch (event.getLastErrorCode()) {
                    case EC_NoError: //??????UI????????????staff Login
                        Message message1 = new Message();
                        message1.what = 1;
                        handler.sendMessage(message1);
                        if (event.isLoginStaffAfterLoginSuccess()) {
                            event.setLoginStaffAfterLoginSuccess(false);
                            staffLoginHttpBO.setStaff(Constants.getCurrentStaff());
                            staffLoginHttpBO.loginAsync(); // 8..
                        }
                        break;
                    case EC_Timeout:
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Pos???????????????", Toast.LENGTH_SHORT).show();
                        //????????????????????????????????????,??????Post??????Runnable???????????????????????????
                        Message message2 = new Message();
                        message2.what = 2;
                        handler.sendMessage(message2);
                        Looper.loop();
                        break;
                    case EC_OtherError:
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Pos???????????????", Toast.LENGTH_SHORT).show();
                        //????????????????????????????????????
                        Message message3 = new Message();
                        message3.what = 2;
                        handler.sendMessage(message3);
                        Looper.loop();
                        break;
                    case EC_WrongFormatForInputField:
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Pos??????????????????", Toast.LENGTH_SHORT).show();
                        //????????????????????????????????????
                        Message message4 = new Message();
                        message4.what = 2;
                        handler.sendMessage(message4);
                        Looper.loop();
                        break;
                    default:
                        //?????????????????????????????????
                        Message message5 = new Message();
                        message5.what = 2;
                        handler.sendMessage(message5);
                        break;
                }
            } else if (event.getRequestType() == null) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Pos???????????????", Toast.LENGTH_SHORT).show();
                tvLogin.setClickable(true);
                //????????????????????????????????????
                Message message3 = new Message();
                message3.what = 2;
                handler.sendMessage(message3);
                Looper.loop();
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffGetToken) {
                return;
            }

            if (Staff.LOGIN_SUCCESS.equals(event.getStaffLoginStatus())) {
                event.setStaffLoginStatus("");
                Message message1 = new Message();
                message1.what = 3;
                handler.sendMessage(message1);
            } else if (Staff.LOGIN_FAILURE.equals(event.getStaffLoginStatus())) {
                // ????????????????????????noSuchData
                if(event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData) {
                    Staff staffRnCondition = new Staff();
                    String passwordMd5 = MD5Util.MD5(Constants.getCurrentStaff().getPasswordInPOS() + Constants.SHADOW);
                    staffRnCondition.setSql("where F_Phone = ? and F_Salt = ?");
                    staffRnCondition.setConditions(new String[]{Constants.getCurrentStaff().getPhone(), passwordMd5});
                    staffRnCondition.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_String);
                    List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staffRnCondition);
                    if(staffPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                        log.error("RetrieveNSync????????????,??????:????????????????????????!" + staffPresenter.getLastErrorCode());
                    }
                    if(staffList != null && staffList.size() > 0) {
                        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffList.get(0));
                        if(staffPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                            log.error("?????????????????????" + staffPresenter.getLastErrorCode());
                        }
                    }
                }
                //
                event.setStaffLoginStatus("");
                Message message2 = new Message();
                message2.what = 4;
                handler.sendMessage(message2);
            } else if (Staff.FIRST_LOGIN.equals(event.getStaffLoginStatus())) {
                event.setStaffLoginStatus("");
                Message message3 = new Message();
                message3.what = 5;
                handler.sendMessage(message3);
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();
            if (event.getRequestType() != HttpRequestUnit.EnumRequestType.ERT_Staff_Ping){
                closeLoadingDialog(loadingDailog);
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    Toast.makeText(getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    if (resetPasswordDialog != null) {
                        resetPasswordDialog.dismiss();
                    }
                }
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompanyHttpEvent(CompanyHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void onCompanySQLiteEvent(CompanySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_LoginActivity) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                company = (Company) event.getBaseModel1();
                //??????????????????Company?????????Company???SN???null????????????????????????????????????SN
                if (company == null) {
                    Message message = new Message();
                    message.what = 6;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 8;
                    message.obj = company.getSN();
                    handler.sendMessage(message);
//                    showCompanySN.setText("???????????????" +  company.getSN());
                    Constants.MyCompanySN = company.getSN();
                    log.info("????????????Pos????????????");
                    posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                    posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
                }
            }

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Message message = new Message();
                message.what = 7;
                handler.sendMessage(message);
            }
        } else {
            log.info("????????????Event???ID=" + event.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);

        //??????????????????????????????????????????????????????????????????????????????
//        new ProperTies().getProperties(getApplicationContext());

        ButterKnife.bind(this);
        EditTextFocusable();

        showTextInT1Host("", "????????????");

        staffPresenter = GlobalController.getInstance().getStaffPresenter();

        initEventAndBO();

        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
        //
        AlertDialog.Builder resetPasswordBuilder = new AlertDialog.Builder(this)
                .setTitle("??????")
                .setMessage("??????????????????????????????????????????")
                .setCancelable(false)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //??????dialog
                        createResetPasswordDialog();
                    }
                });
        resetPasswordWarningDialog = resetPasswordBuilder.create();
        //
        isCreateInterceptorDialog = getIntent().getIntExtra("LoginInterceptor", 0);
        AlertDialog.Builder loginInterceptorBuilder = new AlertDialog.Builder(this)
                .setTitle("??????")
                .setMessage(getIntent().getStringExtra("LoginInterceptorWarn"))
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        loginInterceptorDialog = loginInterceptorBuilder.create();
        username.setSelection(username.getText().toString().length());
        tvLogin.setOnClickListener(this);
        //Android6.0????????????????????????????????????
        configLog4J();

        forgetPassword.setOnClickListener(this);
        retrieveNRememberPasswordStaff();//??????????????????????????????????????????

        setTranslationViewPager();//???????????????
    }

    //???????????????
    private void setTranslationViewPager() {
        ViewPagerImageId.add(R.drawable.viewpager2);
        ViewPagerImageId.add(R.drawable.viewpager1);
        ViewPagerImageId.add(R.drawable.viewpager4);
        ViewPagerImageId.add(R.drawable.viewpager3);
        ViewPagerImageId.add(R.drawable.viewpager2);
        ViewPagerImageId.add(R.drawable.viewpager1);
        PointCheck_unCheckId.add(R.drawable.viewpager_unselect);
        PointCheck_unCheckId.add(R.drawable.viewpager_select);
        ViewPager = new TranslationViewPager(this, translationViewpager, ViewPagerImageId, PointCheck_unCheckId, pointLinear);
        ViewPager.Create_Translation_Image();
    }

    private void initEventAndBO() {
        if (companySQLiteEvent == null) {
            companySQLiteEvent = new CompanySQLiteEvent();
            companySQLiteEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (companyHttpEvent == null) {
            companyHttpEvent = new CompanyHttpEvent();
            companyHttpEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (companySQLiteBO == null) {
            companySQLiteBO = new CompanySQLiteBO(GlobalController.getInstance().getContext(), companySQLiteEvent, companyHttpEvent);
        }
        if (companyHttpBO == null) {
            companyHttpBO = new CompanyHttpBO(GlobalController.getInstance().getContext(), companySQLiteEvent, companyHttpEvent);
        }
        companySQLiteEvent.setSqliteBO(companySQLiteBO);
        companySQLiteEvent.setHttpBO(companyHttpBO);
        companyHttpEvent.setSqliteBO(companySQLiteBO);
        companyHttpEvent.setHttpBO(companyHttpBO);

        if (posSQLiteEvent == null) {
            posSQLiteEvent = new PosSQLiteEvent();
            posSQLiteEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (posHttpEvent == null) {
            posHttpEvent = new PosHttpEvent();
            posHttpEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (posSQLiteBO == null) {
            posSQLiteBO = new PosSQLiteBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        if (posHttpBO == null) {
            posHttpBO = new PosHttpBO(GlobalController.getInstance().getContext(), posSQLiteEvent, posHttpEvent);
        }
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);

        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (staffSQLiteEvent == null) {
            staffSQLiteEvent = new StaffSQLiteEvent();
            staffSQLiteEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (staffHttpEvent == null) {
            staffHttpEvent = new StaffHttpEvent();
            staffHttpEvent.setId(BaseEvent.EVENT_ID_LoginActivity);
        }
        if (staffSQLiteBO == null) {
            staffSQLiteBO = new StaffSQLiteBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        if (staffHttpBO == null) {
            staffHttpBO = new StaffHttpBO(GlobalController.getInstance().getContext(), staffSQLiteEvent, staffHttpEvent);
        }
        staffHttpEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);

        if (rememberLoginStaffSQLiteBO == null) {
            rememberLoginStaffSQLiteBO = new RememberLoginStaffSQLiteBO(GlobalController.getInstance().getContext(), null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: //??????????????????
                Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
                if (DatetimeUtil.isAfterDate(date, DatetimeUtil.mergeDateAndTime(date, Constants.LOGIN_NotAllowed_TimeStart, Constants.DATE_FORMAT_Default), 0)
                        && !DatetimeUtil.isAfterDate(date, DatetimeUtil.mergeDateAndTime(date, Constants.LOGIN_NotAllowed_TimeEnd, Constants.DATE_FORMAT_Default), 0)) {
                    Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!checkLoginInfo(username.getText().toString(), password.getText().toString())) {//???????????????????????????????????????????????????
                    break;
                }
                tvLogin.setClickable(false);
                initialization();
                if (StringUtils.isEmpty(GlobalController.getInstance().getSessionID()) && networkUtils.isNetworkAvalible(this.getApplicationContext())) {
                    //??????????????????????????????????????????????????????????????????
                    staff.setPhone(username.getText().toString());//...get value from UI
                    staff.setPasswordInPOS(password.getText().toString());//...get value from UI
                    staff.setIsLoginFromPos(1);
                    Constants.setCurrentStaff(staff);
                    posLoginHttpEvent.setLoginStaffAfterLoginSuccess(true);
                    posLogin();
                } else {
                    //??????????????????????????????????????????????????????3???????????????????????????
                    onClickLoginButton();
                }
                break;
            case R.id.reset_password_submit:
//                Toast.makeText(getApplicationContext(), "click submit", Toast.LENGTH_SHORT).show();
                String oldPassword = oldpasswordET.getText().toString();
                String newPassword = newPasswordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();
                if (!FieldFormat.checkRawPassword(oldPassword) || !FieldFormat.checkRawPassword(newPassword) || !FieldFormat.checkRawPassword(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), Pos.FIELD_ERROR_RawPassword, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (newPassword.equals(confirmPassword)) {
                    if(oldPassword.equals(newPassword)) {
                        Toast.makeText(getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                        oldpasswordET.setText("");
                        newPasswordET.setText("");
                        confirmPasswordET.setText("");
                        break;
                    } else {
                        try {
                            loadingDailog.show();
                            //
                            staff.setPasswordInPOS(oldPassword);
                            Map map = staffLoginHttpBO.encryptedPassword(staff, false, confirmPassword);
                            String pwdEncrypted = (String) map.get("OriginalPassword");
                            String newPwdEncrypted = (String) map.get("NewPassword");
                            staff.setOldPwdEncrypted(pwdEncrypted);
                            staff.setNewPwdEncrypted(newPwdEncrypted);
                            staffHttpBO.resetMyPassword(staff);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    else {
//                        oldpasswordET.setText("");
//                        newPasswordET.setText("");
//                        confirmPasswordET.setText("");
//                        Toast.makeText(getApplicationContext(), "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    oldpasswordET.setText("");
                    newPasswordET.setText("");
                    confirmPasswordET.setText("");
                    Toast.makeText(getApplicationContext(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    log.info("newPassword???" + newPassword + ",confirmPassword???" + confirmPassword);
                    newPassword = "";
                    confirmPassword = "";
                }
                break;
            case R.id.input_companysn_submit:
                loadingDailogFromCompanySN = createWaitingUI(loadingDailogFromCompanySN, LOADING_MSG_General);
                selectCompany(); // ????????????SN???????????????????????????????????????????????????POS???????????????
                break;
            case R.id.forget_password:
                showToastMessage("???????????????????????????");
                break;
            default:
                break;
        }
    }

    private void selectCompany() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(companySNET.getWindowToken(), 0);

        if (!networkUtils.isNetworkAvalible(this.getApplicationContext())) {
            // ?????????????????????SessionID???NULL
            GlobalController.getInstance().setSessionID(null);
            //
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            iv_poslogin_tip.setImageResource(R.drawable.posloginred);
            tv_posLogin_tip.setText("?????????");
            closeLoadingDialog(loadingDailogFromCompanySN);
        } else {
            if (!"".equals(companySNET.getText().toString().trim())) {
                if (companySNET.getText().toString().length() <= 8) {
                    log.info("???????????????nbr?????????????????????????????????????????????????????????");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Pos pos = new Pos();
                            pos.setCompanySN(companySNET.getText().toString());
                            posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                            posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, pos);
                        }
                    }).start();
                } else {
                    Toast.makeText(appApplication, "???????????????????????????", Toast.LENGTH_SHORT).show();
                    closeLoadingDialog(loadingDailogFromCompanySN);
                }
            } else {
                Toast.makeText(appApplication, "?????????????????????", Toast.LENGTH_SHORT).show();
                closeLoadingDialog(loadingDailogFromCompanySN);
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????3?????????
     */
    private void onClickLoginButton() {
        loadingDailog.show();
        staff.setPhone(username.getText().toString());
        staff.setPasswordInPOS(password.getText().toString());
        staff.setIsLoginFromPos(BaseModel.EnumBoolean.EB_Yes.getIndex());
//                currentStaff.setPhone("15854320895");
//                currentStaff.setPasswordInPOS("000000");
        do {
            if ("".equals(staff.getPhone()) || "".equals(staff.getPasswordInPOS())) {
                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                closeLoadingDialog(loadingDailog);
                tvLogin.setClickable(true);
                break;
            }
            Constants.setCurrentStaff(staff);
            if (!networkUtils.isNetworkAvalible(this.getApplicationContext())) {
                // ?????????????????????SessionID???NULL
                GlobalController.getInstance().setSessionID(null);
                //
                staff.setSql("where F_Phone = ?");
                staff.setConditions(new String[]{staff.getPhone()});
                staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
                List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff);
                if (staffList != null && staffList.size() > 0) {
                    Staff s = staffList.get(0);
                    if (s.getStatus() != Staff.EnumStatusStaff.ESS_Resigned.getIndex()) {
                        if ("".equals(s.getSalt())) {
                            Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                            tvLogin.setClickable(true);
                            break;
                        }
                        String md5 = MD5Utils.MD5(staff.getPasswordInPOS() + Constants.SHADOW);
                        if (!md5.equals(s.getSalt())) {
                            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                            closeLoadingDialog(loadingDailog);
                            tvLogin.setClickable(true);
                            break;
                        } else {
                            initRetailTradeAggregation(s);
                            rememberPassword();//????????????
                            username.setText("");
                            password.setText("");
                            Constants.setCurrentStaff(s); // ??????????????????????????????????????????
                            tvLogin.setClickable(true);
                            Intent noNet_login_intent = new Intent(Login1Activity.this, SyncData1Activity.class);
                            Login1Activity.this.finish();
                            startActivity(noNet_login_intent);
                        }
                    } else {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                        closeLoadingDialog(loadingDailog);
                        tvLogin.setClickable(true);
                        break;
                    }
                } else {
                    Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    closeLoadingDialog(loadingDailog);
                    tvLogin.setClickable(true);
                    break;
                }
            } else {
                long time_out = 20;
                while (time_out-- > 0) {
                    if (posLoginHttpEvent.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin && !StringUtils.isEmpty(GlobalController.getInstance().getSessionID())) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (StringUtils.isEmpty(GlobalController.getInstance().getSessionID())) {
                    Toast.makeText(getApplicationContext(), "POS?????????????????????????????????APP?????????", Toast.LENGTH_SHORT).show();
                    closeLoadingDialog(loadingDailog);
                    tvLogin.setClickable(true);
                    break;
                }
                //????????????????????????Company,???????????????????????????
                if (!StringUtils.isEmpty(Constants.MyCompanySN)) {
//                    if (StringUtils.isEmpty(GlobalController.getInstance().getSessionID()) && posLoginHttpEvent.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
//                        log.info("?????????????????????????????????????????????POS?????????Staff?????????");
//                        posLoginHttpEvent.setLoginStaffAfterLoginSuccess(true);
//                        posLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
//                        posLoginHttpBO.setPos(pos);
//                        posLoginHttpBO.loginAsync();
//                    } else {
                    log.info("????????????????????????????????????????????????pos??????????????????Staff");
                    staffLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    staffLoginHttpBO.setStaff(Constants.getCurrentStaff());
                    staffLoginHttpBO.loginAsync();
//                    }
                }
            }
            break;
        } while (false);
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    private void initRetailTradeAggregation(Staff s) {
        BaseActivity.retailTradeAggregation = new RetailTradeAggregation();
        BaseActivity.retailTradeAggregation.setStaffID(Integer.parseInt(String.valueOf(s.getID())));
        BaseActivity.retailTradeAggregation.setWorkTimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        log.info("????????????????????????????????????????????????????????????" + BaseActivity.retailTradeAggregation.getWorkTimeStart().toString());
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//????????????????????????????????????????????????
    }

    //??????????????????????????????????????????????????????????????????????????????activity???????????????
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    closeLoadingDialog(loadingDailog);
                    if (inputCompanySNDialog != null) {
                        inputCompanySNDialog.dismiss();
                    }
                    if (isCreateInterceptorDialog == 1) {
                        loginInterceptorDialog.show();
                    }

                    initTimers();
                    break;
                case 2:
                    closeLoadingDialog(loadingDailog);
                    tvLogin.setClickable(true);
                    //????????????
                    iv_poslogin_tip.setImageResource(R.drawable.posloginred);
                    tv_posLogin_tip.setText("?????????");
                    break;
                case 3:
                    closeLoadingDialog(loadingDailog);
                    tvLogin.setClickable(true);
                    if (Constants.getCurrentStaff().getRoleID() == Constants.preSale_Role_ID) {
                        username.setText("");
                        password.setText("");
                        Intent login_intent = new Intent(Login1Activity.this, PreSale1Activity.class);
                        Login1Activity.this.finish();
                        startActivity(login_intent);
                    } else {
                        rememberPassword();//????????????
                        username.setText("");
                        password.setText("");
                        Intent login_intent = new Intent(Login1Activity.this, SyncData1Activity.class);
                        log.info("??????finish???LoginActivity");
                        Login1Activity.this.finish();
                        startActivity(login_intent);
                    }
                    break;
                case 4:
                    closeLoadingDialog(loadingDailog);
                    tvLogin.setClickable(true);
                    Toast.makeText(getApplicationContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    tvLogin.setClickable(true);
                    resetPasswordWarningDialog.show();
                    break;
                case 6:
                    createInputCompanySNDialog();
                    break;
                case 7:
                    Company c = (Company) companySQLiteEvent.getBaseModel1();
                    showCompanySN.setText("???????????????" + c.getSN());
                    Constants.MyCompanySN = c.getSN();
                    //???????????????SQLite?????????????????????SN?????????????????????????????????SQLite???????????????POS???????????????
                    log.info("????????????Pos????????????");
                    posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                    posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
                    break;
                case 8:
                    showCompanySN.setText("???????????????" + msg.obj);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewPager.Destory();//????????????????????????
    }

    //??????Edittext?????????????????????
    private void EditTextFocusable() {
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    username_linear.setBackground(getResources().getDrawable(R.drawable.linearlayout_rounded_selected, null));
                } else {
                    username_linear.setBackground(getResources().getDrawable(R.drawable.linearlayout_rounded_unselected, null));
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    password_linear.setBackground(getResources().getDrawable(R.drawable.linearlayout_rounded_selected, null));
                } else {
                    password_linear.setBackground(getResources().getDrawable(R.drawable.linearlayout_rounded_unselected, null));
                }
            }
        });
    }

    /**
     * ??????/?????????????????????????????????SQLite??????????????????
     */
    private void rememberPassword() {
        if (cbRememberPassword.isChecked()) {  //?????????????????????
            rememberLoginStaffSQLiteBO.createAsync(BaseSQLiteBO.INVALID_INT_ID, new RememberLoginStaff(username.getText().toString(), password.getText().toString(), true));
        } else { //???????????????????????????????????????
            rememberLoginStaffSQLiteBO.createAsync(BaseSQLiteBO.INVALID_INT_ID, new RememberLoginStaff(username.getText().toString(), "", false));
        }
    }

    /**
     * ???????????????????????????????????????????????????
     */
    private void retrieveNRememberPasswordStaff() {
        List<RememberLoginStaff> list = (List<RememberLoginStaff>) rememberLoginStaffSQLiteBO.retrieveNSync(BaseSQLiteBO.INVALID_INT_ID, null);
        if (list.size() != 0) {
            username.setText(list.get(0).getPhone());
            username.setSelection(username.getText().length());
            password.setText(list.get(0).getPassword());
            cbRememberPassword.setChecked(list.get(0).getRemembered());
        }
    }

    private void createResetPasswordDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;//????????????
        int screenHeight = dm.heightPixels;//????????????
        AlertDialog.Builder builder = new AlertDialog.Builder(Login1Activity.this);
        View view = View.inflate(getApplicationContext(), R.layout.reset_password_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        resetPasswordDialog = builder.create();
        resetPasswordDialog.show();
        //
        if (resetPasswordDialog != null) {
            resetPasswordWarningDialog.dismiss();
        }
        //
        WindowManager.LayoutParams params = resetPasswordDialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.5);
        params.height = (int) (screenHeight * 0.7);
        resetPasswordDialog.getWindow().setAttributes(params);

        oldpasswordET = view.findViewById(R.id.oldpassword);
        newPasswordET = view.findViewById(R.id.newpassword);
        confirmPasswordET = view.findViewById(R.id.confirmpassword);
        TextView submit = view.findViewById(R.id.reset_password_submit);
        submit.setOnClickListener(this);
    }

    private void createInputCompanySNDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        AlertDialog.Builder builder = new AlertDialog.Builder(Login1Activity.this);
        View view = View.inflate(getApplicationContext(), R.layout.input_companysn_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        inputCompanySNDialog = builder.create();
        inputCompanySNDialog.show();
        //
        WindowManager.LayoutParams params = inputCompanySNDialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.5);
        params.height = (int) (screenHeight * 0.45);
        inputCompanySNDialog.getWindow().setAttributes(params);

        companySNET = view.findViewById(R.id.companysn);
        TextView submit = view.findViewById(R.id.input_companysn_submit);

        submit.setOnClickListener(this);
    }

    /**
     * ???????????????Company???POS???Staff?????????????????????Company???SN
     *
     * @throws
     */
    private void retrieveCompanyInSQLite() throws InterruptedException {
        loadingDailog.show();
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companySQLiteBO.retrieveNASync(BaseSQLiteBO.INVALID_CASE_ID, null);
        long lTimeOut = 1000;
        while (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            log.info("????????????Company?????????");
        }
    }

    private void configLog4J() {
        final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        final DynamicPermissionEmitter permissionEmitter = new DynamicPermissionEmitter(this);
        permissionEmitter.emitterPermission(new DynamicPermissionEmitter.ApplyPermissionsCallback() {
            @Override
            public void applyPermissionResult(Map<String, DynamicPermissionEntity> permissionEntityMap) {

                DynamicPermissionEntity dynamicPermissionEntity = permissionEntityMap.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (dynamicPermissionEntity.isGranted()) {
                    // ????????????
                    log.info("???????????????????????????");
                    //????????????
                    ConfigureLog4J configureLog4J = new ConfigureLog4J();
                    configureLog4J.configure();
                    //????????? log
                    Logger log = Logger.getLogger(this.getClass());

                } else if (dynamicPermissionEntity.shouldShowRequestPermissionRationable()) {
                    // ?????????????????????????????????
                    log.info("??????????????????????????????");
                } else {
                    // ??????
                    log.info("????????????");
                }
                posLoginHttpEvent.setLoginStaffAfterLoginSuccess(false);
                posLogin();
            }
        }, permissions);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????SQLite Pos?????????????????????SN?????????????????????????????????????????????????????????????????????????????????POS??????
     */
    private void posLogin() {
        try {
            if (!networkUtils.isNetworkAvalible(this.getApplicationContext())) {
                // ?????????????????????SessionID???NULL
                GlobalController.getInstance().setSessionID(null);
                closeLoadingDialog(loadingDailog);
                //
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
                iv_poslogin_tip.setImageResource(R.drawable.posloginred);
                tv_posLogin_tip.setText("?????????");
                //BaseActivity.posID=0?????????????????????POS???????????????POS??????????????????
                if (Constants.posID == 0) {
                    retrieve1Pos();
                }
            } else {
                loadingDailog.show();
                //???????????????Company?????????Company?????????SN?????????????????????????????????
                log.info(Thread.currentThread());
                retrieveCompanyInSQLite();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieve1Pos() {
        Pos pos = new Pos();
        pos.setShopID(BaseSQLiteBO.INVALID_INT_ID);
        pos.setSql("where F_POS_SN = ?");
        pos.setConditions(new String[]{Constants.MyPosSN});
        PosPresenter posPresenter = GlobalController.getInstance().getPosPresenter();
        List<Pos> poses = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        if (posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("????????????POS?????????????????????" + posPresenter.getLastErrorCode());
            Toast.makeText(getApplicationContext(), "POS???????????????,??????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poses != null && poses.size() == 1) {
            pos = poses.get(0);
            log.info("????????????POS?????????POS?????????" + pos);
            Constants.posID = pos.getID().intValue();
        } else {
            log.error("??????SQLite???POS???????????????????????????????????????POS????????????poses=" + poses);
            Toast.makeText(getApplicationContext(), "POS????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLoginInfo(String username, String password) {
        if (!FieldFormat.checkMobile(username)) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!FieldFormat.checkPassword(password)) {
            Toast.makeText(this, "?????????????????????,?????????6-16????????????,?????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void initTimers() {   //?????????timer???????????????
        timerCheckInvalidSession = new Timer();

        timerPing = new Timer();//????????????session?????????

        /**
         * ??????ping nbr??????????????????????????????????????????????????????30?????????App??????25??????ping??????
         */
        timerTaskPing = new TimerTask() {
            @Override
            public void run() {
                try {
                    staffHttpBO.pingEx();
                    System.out.println("xxxxxxxxxxxxxx Ping NBR?????????????????????");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug(e.getMessage());
                }
            }
        };
        timerPing.schedule(timerTaskPing, 0, 25 * 60 * 1000);
    }
}