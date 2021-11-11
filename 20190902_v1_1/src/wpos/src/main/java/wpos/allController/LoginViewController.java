package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.*;
import wpos.allEnum.StageType;
import wpos.allEnum.ThreadMode;
import wpos.application.PreSaleActivity;
import wpos.application.SyncDataActivity;
import wpos.bo.*;
import wpos.common.GlobalController;
import wpos.event.*;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CompanySQLiteEvent;
import wpos.event.UI.StaffSQLiteEvent;
import wpos.helper.Constants;
import wpos.http.HttpRequestUnit;
import wpos.listener.*;
import wpos.model.*;
import wpos.presenter.PosPresenter;
import wpos.presenter.StaffPresenter;
import wpos.utils.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.util.*;

import static wpos.bo.BaseSQLiteBO.CASE_Staff_RetrieveNByConditions;

@Component("loginViewController")
public class LoginViewController extends BaseController implements resetPasswordWaringDialogListener, resetPasswordDialogListener, InputCompanySNDialogListener, PlatFormHandlerMessage {
    @FXML   //关闭窗口
    private ImageView close;
    @FXML   //复选框
    private CheckBox cbRememberPassword;
    @FXML   //轮播图
    private ImageView banner;
    @FXML   //登录按钮
    private Button login;
    @FXML   //手机号输入框
    private TextField username;
    @FXML   //密码输入框
    private PasswordField password;
    @FXML   //已就绪图标
    private ImageView iv_poslogin_tip;
    @FXML   //已就绪文字
    private Label lb_posLogin_tip;
    //显示公司编号文本框
    @FXML
    private Labeled showCompanySN;

    private Intent intent;

    private final Staff staff = new Staff();
    private static final NetworkUtils networkUtils = new NetworkUtils();
    private final Log log = LogFactory.getLog(this.getClass());
    private int isCreateInterceptorDialog;
    private Pos pos = null;
    private Company company = new Company();
    @Resource
    SyncDataActivity syncDataActivity;
    @Resource
    private PreSaleActivity preSaleActivity;
    @Resource
    public CompanySQLiteEvent companySQLiteEvent;
    @Resource
    public CompanyHttpEvent companyHttpEvent;
    @Resource
    public CompanySQLiteBO companySQLiteBO;
    @Resource
    public CompanyHttpBO companyHttpBO;
    @Resource
    public StaffPresenter staffPresenter;
    @Resource
    public PosHttpBO posHttpBO;
    @Resource
    public PosHttpEvent posHttpEvent;
    @Resource
    public PosSQLiteBO posSQLiteBO;
    @Resource
    public PosSQLiteEvent posSQLiteEvent;
    @Resource
    public PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    public PosLoginHttpBO posLoginHttpBO;
    @Resource
    public StaffLoginHttpEvent staffLoginHttpEvent;
    @Resource
    public StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    public StaffSQLiteEvent staffSQLiteEvent;
    @Resource
    public StaffSQLiteBO staffSQLiteBO;
    @Resource
    public StaffHttpEvent staffHttpEvent;
    @Resource
    public StaffHttpBO staffHttpBO;
    @Resource
    public RememberLoginStaffSQLiteBO rememberLoginStaffSQLiteBO;
    @Resource
    public PosPresenter posPresenter;

    //提示首次必须修改密码的提示弹框
    JFXAlert resetPasswordWarningDialog;
    //重置密码的弹窗
    JFXAlert resetPasswordDialog;
    ResetPasswordDialogViewController resetPasswordDialogViewController;
    //输入公司编号弹窗
    private JFXAlert inputCompanySNDialog;
    InputCompanySNDialogViewController inputCompanySNDialogViewController;
    //
    private Alert loginInterceptorDialog;

    private Timer bannerTimer;
    private TimerTask bannerTimerTask;

    /**
     * 检查会话是否已经被踢出或过期。若然，UI需要跳回登录界面。一般来说，会话不会过期，因为timerTaskPing会定时ping服务器
     * 测试会话被踢出的方法：先后用同1个收银员帐号在2台Pos，登录到同1个NBR服务器。后登录的，会令先登录的跳回到登录界面
     */
    private Timer timerCheckInvalidSession;

    /**
     * 参考timerTaskPing
     */
    private Timer timerPing;

    /**
     * 定时ping nbr以保持会话不过期。服务器的过期时间是30分钟。App每隔25分钟ping一次
     */
    private TimerTask timerTaskPing;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();
            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_POS_Retrieve1BySN && event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && event.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done) {
                //在本地插入公司信息
                Company c = (Company) posHttpEvent.getBaseModel2();
                companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                companySQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, c);
            } else {
                closeLoadingDialog();
                ToastUtil.toast("该公司编号不存在！", ToastUtil.LENGTH_SHORT);
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosSQLiteEvent(PosSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
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
                    //没有Pos的身份证（明文密码），将上一次请求返回的明文密码放到本地SQLite
                    Pos p = (Pos) posHttpEvent.getBaseModel1();
                    if (p == null) {
                        closeLoadingDialog();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("提示");
                        alert.setContentText("数据损坏，请联系客服重装");
                        alert.showAndWait();
                    } else {
                        posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                        posSQLiteBO.createReplacerAsync(BaseSQLiteBO.INVALID_CASE_ID, p);
                    }
                }
            }
            //将明文密码插入本地已完成，可以进行登录操作
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Pos p = (Pos) event.getBaseModel1();
                p.setResetPasswordInPos(1);//...
//                posLoginHttpEvent.setLoginStaffAfterLoginSuccess(false);
                posLoginHttpBO.setPos(p);
                posLoginHttpBO.loginAsync();
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * Caution! PosLoginHttpEvent is not PosHttpEvent!!!
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosGetToken) {
                return;
            }

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_PosLogin) {
                if (inputCompanySNDialog != null) {
                    inputCompanySNDialog.close();
                }
                closeLoadingDialog();

                switch (event.getLastErrorCode()) {
                    case EC_NoError: //设置UI后，触发staff Login
                        Message message1 = new Message();
                        message1.what = 1;
                        PlatForm.get().sendMessage(message1);
                        if (event.isLoginStaffAfterLoginSuccess()) {
                            event.setLoginStaffAfterLoginSuccess(false);
                            staffLoginHttpBO.setStaff(Constants.getCurrentStaff());
                            staffLoginHttpBO.loginAsync(); // 8..
                        }
                        break;
                    case EC_Timeout:
//                        Looper.prepare();
                        ToastUtil.toast("Pos登录超时！", ToastUtil.LENGTH_SHORT);
                        //把网络图标替换成红色图片,需要Post一个Runnable，否则获取不到控件
                        Message message2 = new Message();
                        message2.what = 2;
                        PlatForm.get().sendMessage(message2);
//                        Looper.loop();
                        break;
                    case EC_OtherError:
//                        Looper.prepare();
                        ToastUtil.toast("Pos登录失败！", ToastUtil.LENGTH_SHORT);
                        //把网络图标替换成红色图片
                        Message message3 = new Message();
                        message3.what = 2;
                        PlatForm.get().sendMessage(message3);
//                        Looper.loop();
                        break;
                    case EC_WrongFormatForInputField:
//                        Looper.prepare();
                        ToastUtil.toast("Pos密码不正确！", ToastUtil.LENGTH_SHORT);
                        //把网络图标替换成红色图片
                        Message message4 = new Message();
                        message4.what = 2;
                        PlatForm.get().sendMessage(message4);
//                        Looper.loop();
                        break;
                    default:
                        //网络图标替换成红色图片
                        Message message5 = new Message();
                        message5.what = 2;
                        PlatForm.get().sendMessage(message5);
                        break;
                }
            } else if (event.getRequestType() == null) {
//                Looper.prepare();
//                ToastUtil.toast("Pos登录失败！", ToastUtil.LENGTH_SHORT);// 不是在主线程，不能toast
                Platform.runLater(() -> {
                    // 不是在主线程，不能toast,用这种方式操作UI
                    showToastMessage("Pos登录失败！");
                });
                login.setDisable(false);
                //把网络图标替换成红色图片
                Message message3 = new Message();
                message3.what = 2;
                PlatForm.get().sendMessage(message3);
//                Looper.loop();
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();

            if (event.getRequestType() == HttpRequestUnit.EnumRequestType.ERT_StaffGetToken) {
                return;
            }

            if (Staff.LOGIN_SUCCESS.equals(event.getStaffLoginStatus())) {
                event.setStaffLoginStatus("");
                Message message1 = new Message();
                message1.what = 3;
                PlatForm.get().sendMessage(message1);
            } else if (Staff.LOGIN_FAILURE.equals(event.getStaffLoginStatus())) {
                // 判断错误码是否是noSuchData
                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData) {
                    Staff staffRnCondition = new Staff();
                    String passwordMd5 = MD5Util.MD5(Constants.getCurrentStaff().getPasswordInPOS() + Constants.SHADOW);
                    staffRnCondition.setSql("where F_Phone = '%s' and F_Salt = '%s'");
                    staffRnCondition.setConditions(new String[]{Constants.getCurrentStaff().getPhone(), passwordMd5});
                    staffRnCondition.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_String);
                    List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staffRnCondition);
                    if (staffPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                        log.error("RetrieveNSync测试失败,原因:返回错误码不正确!" + staffPresenter.getLastErrorCode());
                    }
                    if (staffList != null && staffList.size() > 0) {
                        staffPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, staffList.get(0));
                        if (staffPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                            log.error("删除员工失败：" + staffPresenter.getLastErrorCode());
                        }
                    }
                }
                //
                event.setStaffLoginStatus("");
                Message message2 = new Message();
                message2.what = 4;
                PlatForm.get().sendMessage(message2);
            } else if (Staff.FIRST_LOGIN.equals(event.getStaffLoginStatus())) {
                event.setStaffLoginStatus("");
                Message message3 = new Message();
                message3.what = 5;
                PlatForm.get().sendMessage(message3);
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();
            if (event.getRequestType() != HttpRequestUnit.EnumRequestType.ERT_Staff_Ping) {
                closeLoadingDialog();
                if (event.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    ToastUtil.toast("修改密码失败，请重试！", ToastUtil.LENGTH_SHORT);
                } else {
                    ToastUtil.toast("修改成功，请重新登录！", ToastUtil.LENGTH_SHORT);
                    if (resetPasswordDialog != null) {
                        resetPasswordDialog.close();
                    }
                }
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffSQLiteEvent(StaffSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompanyHttpEvent(CompanyHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    //todo 该方法原本是一个粘性事件，但在pos中并未找到有发送粘性事件，所以这里被我删了，而且自己写的eventbus也没有写粘性事件
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCompanySQLiteEvent(CompanySQLiteEvent event) throws InterruptedException {
        if (event.getId() == BaseEvent.EVENT_ID_LoginStage) {
            event.onEvent();
            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                company = (Company) event.getBaseModel1();
                //如果本地没有Company，或者Company的SN为null，就需要出现弹框填写公司SN
                if (company == null) {
                    Message message = new Message();
                    message.what = 6;
                    PlatForm.get().sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 8;
                    message.obj = company.getSN();
                    PlatForm.get().sendMessage(message);
                    Constants.MyCompanySN = company.getSN();
                    log.info("开始查找Pos明文密码");
                    posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                    posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                    posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
                }
            }

            if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync && event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                Message message = new Message();
                message.what = 7;
                PlatForm.get().sendMessage(message);
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    public LoginViewController() {
    }

    public void register() {
        EventBus.getDefault().register(this);
        PlatForm.get().setHandlerMessage(this);

        initEventAndBO();

        createResetPasswordWarningDialog();

        retrieveNRememberPasswordStaff();//获取记住的用户密码填入文本框

        isCreateInterceptorDialog = intent.getIntExtra("LoginInterceptor", 0);
        loginInterceptorDialog = new Alert(Alert.AlertType.WARNING);
        loginInterceptorDialog.setTitle("提示");
        loginInterceptorDialog.setContentText(intent.getStringExtra("LoginInterceptorWarn"));
        //
        posLoginHttpEvent.setLoginStaffAfterLoginSuccess(false);
        posLogin();
        initBanner();

        username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > FieldFormat.MAX_LENGTH_Phone) {
                    username.setText(oldValue);
                    showToastMessage("手机号码太长！");
                }
            }
        });

        password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > FieldFormat.MAX_LENGTH_Password) {
                    password.setText(oldValue);
                    showToastMessage("密码太长！");
                }
            }
        });

    }

    private int index = 0;//轮播图使用

    private void initBanner() {
        LinkedList<Image> list = new LinkedList<>();
        list.add(new Image("/image/viewpager1.jpg"));
        list.add(new Image("/image/viewpager2.jpg"));
        list.add(new Image("/image/viewpager3.jpg"));
        list.add(new Image("/image/viewpager4.jpg"));
        bannerTimer = new Timer();
        bannerTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (list.size() != 0) {
                    banner.setImage(list.get(index));
                    if (index == list.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                }
            }
        };
        bannerTimer.schedule(bannerTimerTask, 2000, 2000);
    }

    /**
     * 获取所有的勾选记住密码的账号和密码
     */
    private void retrieveNRememberPasswordStaff() {
        List<RememberLoginStaff> list = (List<RememberLoginStaff>) rememberLoginStaffSQLiteBO.retrieveNSync(BaseSQLiteBO.INVALID_INT_ID, null);
        if (list.size() != 0) {
            username.setText(list.get(0).getPhone());
            password.setText(list.get(0).getPassword());
            cbRememberPassword.setSelected(list.get(0).getRemembered());
        }
    }

    private void initEventAndBO() {
        companySQLiteEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        companyHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        //
        companySQLiteBO.setSqLiteEvent(companySQLiteEvent);
        companySQLiteBO.setHttpEvent(companyHttpEvent);
        //
        companySQLiteEvent.setSqliteBO(companySQLiteBO);
        companySQLiteEvent.setHttpBO(companyHttpBO);
        companyHttpEvent.setSqliteBO(companySQLiteBO);
        companyHttpEvent.setHttpBO(companyHttpBO);

        posSQLiteEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        posHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        //
        posSQLiteBO.setSqLiteEvent(posSQLiteEvent);
        posSQLiteBO.setHttpEvent(posHttpEvent);
        //
        posHttpBO.setSqLiteEvent(posSQLiteEvent);
        posHttpBO.setHttpEvent(posHttpEvent);
        //
        posSQLiteEvent.setSqliteBO(posSQLiteBO);
        posSQLiteEvent.setHttpBO(posHttpBO);
        posHttpEvent.setSqliteBO(posSQLiteBO);
        posHttpEvent.setHttpBO(posHttpBO);
        //
        posLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        //
        staffSQLiteEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        staffHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        staffSQLiteEvent.setHttpBO(staffHttpBO);
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffHttpEvent.setSqliteBO(staffSQLiteBO);
        staffHttpBO.setHttpEvent(staffHttpEvent);
        staffHttpBO.setSqLiteEvent(staffSQLiteEvent);
        staffSQLiteBO.setHttpEvent(staffHttpEvent);
        staffSQLiteBO.setSqLiteEvent(staffSQLiteEvent);
        posLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        //
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        //
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);

        staffLoginHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        //
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        //
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        staffSQLiteEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        staffHttpEvent.setId(BaseEvent.EVENT_ID_LoginStage);
        //
        staffSQLiteBO.setSqLiteEvent(staffSQLiteEvent);
        staffSQLiteBO.setHttpEvent(staffHttpEvent);
        staffHttpBO.setSqLiteEvent(staffSQLiteEvent);
        staffHttpBO.setHttpEvent(staffHttpEvent);
        //
        staffHttpEvent.setSqliteBO(staffSQLiteBO);
        staffHttpEvent.setHttpBO(staffHttpBO);
        staffSQLiteEvent.setSqliteBO(staffSQLiteBO);
        staffSQLiteEvent.setHttpBO(staffHttpBO);

//
////        if (rememberLoginStaffSQLiteBO == null) {
////            rememberLoginStaffSQLiteBO = new RememberLoginStaffSQLiteBO(null);
////        }
    }

    //创建提示首次必须修改密码的提示弹框
    private void createResetPasswordWarningDialog() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/resetPasswordWaringDialog.fxml"));
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetPasswordWarningDialog = new JFXAlert(StageController.get().getStageBy(StageType.LOGIN_STAGE.name()));
        resetPasswordWarningDialog.initModality(Modality.APPLICATION_MODAL);
        resetPasswordWarningDialog.setOverlayClose(false);
        resetPasswordWarningDialog.setContent(borderPane);

        ResetPasswordWaringDialogViewController resetPasswordWaringDialogViewController = loader.getController();
        resetPasswordWaringDialogViewController.setListener(this);
    }

    @Override   //提示首次必须修改密码的提示弹框的确认按钮点击事件
    public void resetPasswordWaringDialogComfirmClick() {
        createResetPasswordDialog();
    }

    //创建重置密码的弹窗
    private void createResetPasswordDialog() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/resetPasswordDialog.fxml"));
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetPasswordDialog = new JFXAlert(StageController.get().getStageBy(StageType.LOGIN_STAGE.name()));
        resetPasswordDialog.initModality(Modality.APPLICATION_MODAL);
        resetPasswordDialog.setOverlayClose(false);
        resetPasswordDialog.setContent(borderPane);
        resetPasswordDialog.show();

        resetPasswordDialogViewController = loader.getController();
        resetPasswordDialogViewController.setAlert(resetPasswordDialog);
        resetPasswordDialogViewController.setListener(this);
        resetPasswordDialogViewController.addPasswordFieldListener();

        if (resetPasswordDialog != null) {
            resetPasswordWarningDialog.close();
        }


    }

    @Override   //重置密码提交按钮的点击事件
    public void resetPasswordDialogConfirmClick(String oldPasswordString, String newPassword, String confirmPassword) {
        if (!FieldFormat.checkRawPassword(oldPasswordString) || !FieldFormat.checkRawPassword(newPassword) || !FieldFormat.checkRawPassword(confirmPassword)) {
            ToastUtil.toast(Pos.FIELD_ERROR_RawPassword, ToastUtil.LENGTH_SHORT);
            return;
        }
        if (newPassword.equals(confirmPassword)) {
            if (oldPasswordString.equals(newPassword)) {
                ToastUtil.toast("新密码不能与原密码相同", ToastUtil.LENGTH_SHORT);
                resetPasswordDialogViewController.cleanTextFiled();
                return;
            } else {
                try {
                    showLoadingDialog();
                    //
                    staff.setPasswordInPOS(oldPasswordString);
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
        } else {
            resetPasswordDialogViewController.cleanTextFiled();
            ToastUtil.toast("两次输入密码不一致！", ToastUtil.LENGTH_SHORT);
            log.info("newPassword：" + newPassword + ",confirmPassword：" + confirmPassword);
        }
    }

    // 创建输入公司SN弹窗
    private void createInputCompanySNDialog() {
        // 获取本机MAC地址
        try {
            InetAddress ia = InetAddress.getLocalHost();
            Constants.MyPosSN = getLocalMac(ia);
        } catch (Exception e) {
            log.error(e.getMessage());
            ToastUtil.toast("网络连接失败", ToastUtil.LENGTH_SHORT);
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/inputCompanySNDialog.fxml"));
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputCompanySNDialog = new JFXAlert(StageController.get().getStageBy(StageType.LOGIN_STAGE.name()));
        inputCompanySNDialog.initModality(Modality.APPLICATION_MODAL);
        inputCompanySNDialog.setOverlayClose(false);
        inputCompanySNDialog.setContent(borderPane);
        inputCompanySNDialog.show();

        inputCompanySNDialogViewController = loader.getController();
        inputCompanySNDialogViewController.setAlert(inputCompanySNDialog);
        inputCompanySNDialogViewController.setListener(this);
    }

    @Override
    public void inputCompanySNDialogListenerConfirmClick(String companySN) {
        if (StringUtils.isEmpty(GlobalController.getInstance().getSessionID()) && !networkUtils.isNetworkAvalible()) {
            // 断网状态，设置SessionID为NULL
            GlobalController.getInstance().setSessionID(null);
            //
            ToastUtil.toast("网络连接失败", ToastUtil.LENGTH_SHORT);
            iv_poslogin_tip.setImage(new Image(getClass().getResource("/image/posloginred.png").toString()));
            lb_posLogin_tip.setText("未就绪");
            closeLoadingDialog();
        } else {
            if (!StringUtils.isEmpty(companySN)) {
                if (companySN.length() <= 8) {
                    log.info("发送请求到nbr，返回公司信息和明文密码，同步到本地。");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Pos pos = new Pos();
                            pos.setCompanySN(companySN);
                            posHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                            posHttpBO.retrieve1AsyncC(BaseHttpBO.INVALID_CASE_ID, pos);
                        }
                    }).start();
                } else {
                    ToastUtil.toast("输入的公司编号太长", ToastUtil.LENGTH_SHORT);
                    closeLoadingDialog();
                }
            } else {
                ToastUtil.toast("请输入公司编号", ToastUtil.LENGTH_SHORT);
                closeLoadingDialog();
            }
        }
    }

    //登录按钮事件
    @FXML
    public void login() throws UnknownHostException, SocketException {
        Date date = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        if (DatetimeUtil.isAfterDate(date, DatetimeUtil.mergeDateAndTime(date, Constants.LOGIN_NotAllowed_TimeStart, Constants.DATE_FORMAT_Default), 0)
                && !DatetimeUtil.isAfterDate(date, DatetimeUtil.mergeDateAndTime(date, Constants.LOGIN_NotAllowed_TimeEnd, Constants.DATE_FORMAT_Default), 0)) {
            ToastUtil.toast("系统结算中，请稍后再登录", ToastUtil.LENGTH_SHORT);
            return;
        }
        if (!checkLoginInfo(username.getText(), password.getText())) {//若不通过帐号密码格式检验，直接跳出
            return;
        }
        login.setDisable(true);
        initialization();
        if (StringUtils.isEmpty(GlobalController.getInstance().getSessionID()) && networkUtils.isNetworkAvalible()) {
            // TODO: 2020/6/10 网络ping是否能通，这里是否应当阻塞线程
            //断网情况下来到登录页面，再恢复网络，跑进这里
            staff.setPhone(username.getText());//...get value from UI
            staff.setPasswordInPOS(password.getText());//...get value from UI
            staff.setIsLoginFromPos(1);
            Constants.setCurrentStaff(staff);
            posLoginHttpEvent.setLoginStaffAfterLoginSuccess(true);
            posLogin();
        } else {
            //联网到登录再断网、完全断网和完全联网3种情况下，跑进这里
            onClickLoginButton();
        }

    }


    private String getLocalMac(InetAddress ia) throws SocketException {
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        System.out.println("mac数组长度：" + mac.length);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            System.out.println("每8位:" + str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    @FXML   //忘记密码点击事件
    public void forgetPassword() {
        ToastUtil.toast("请联系管理员或客服", ToastUtil.LENGTH_SHORT);
    }

    //关闭窗口
    @FXML
    private void closeWindows() {
        StageController.get().closeStge(StageType.LOGIN_STAGE.name());
        Platform.exit();
        System.exit(0);
    }

    /**
     * 联网到登录再断网、完全断网和完全联网3种情况
     */
    private void onClickLoginButton() {
        showLoadingDialog();
        staff.setPhone(username.getText().toString());
        staff.setPasswordInPOS(password.getText().toString());
        staff.setIsLoginFromPos(BaseModel.EnumBoolean.EB_Yes.getIndex());
        do {
            if ("".equals(staff.getPhone()) || "".equals(staff.getPasswordInPOS())) {
                ToastUtil.toast("手机号码或密码不能为空", ToastUtil.LENGTH_SHORT);
                closeLoadingDialog();
                login.setDisable(false);
                break;
            }
            Constants.setCurrentStaff(staff);
            if (!networkUtils.isNetworkAvalible()) {
                // TODO: 2020/6/10  网络ping是否能通，这里是否应当阻塞线程
                // 断网状态，设置SessionID为NULL
                GlobalController.getInstance().setSessionID(null);
                //
                staff.setSql("where F_Phone = %s");
                staff.setConditions(new String[]{staff.getPhone()});
                staff.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
                List<Staff> staffList = (List<Staff>) staffPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Staff_RetrieveNByConditions, staff);
                if (staffList != null && staffList.size() > 0) {
                    Staff s = staffList.get(0);
                    if (s.getStatus() != Staff.EnumStatusStaff.ESS_Resigned.getIndex()) {
                        if ("".equals(s.getSalt())) {
                            ToastUtil.toast("请连接网络进行登录！！", ToastUtil.LENGTH_SHORT);
                            closeLoadingDialog();
                            login.setDisable(false);
                            break;
                        }
                        String md5 = MD5Utils.MD5(staff.getPasswordInPOS() + Constants.SHADOW);
                        if (!md5.equals(s.getSalt())) {
                            ToastUtil.toast("输入的密码不正确！！", ToastUtil.LENGTH_SHORT);
                            closeLoadingDialog();
                            login.setDisable(false);
                            break;
                        } else {
                            initRetailTradeAggregation(s);
                            rememberPassword();//记住密码
                            username.setText("");
                            password.setText("");
                            Constants.setCurrentStaff(s); // 断网情况下，设置登陆者的数据
                            login.setDisable(false);
                            StageController.get().closeStge(StageType.LOGIN_STAGE.name()).unloadStage(StageType.LOGIN_STAGE.name());
                            toSyncDataActivity();
                        }
                    } else {
                        ToastUtil.toast("该员工已经离职！", ToastUtil.LENGTH_SHORT);
                        closeLoadingDialog();
                        login.setDisable(false);
                        break;
                    }
                } else {
                    ToastUtil.toast("未找到该用户，请重新输入！！", ToastUtil.LENGTH_SHORT);
                    closeLoadingDialog();
                    login.setDisable(false);
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
                    ToastUtil.toast("POS机登录超时，请重新打开APP登录！", ToastUtil.LENGTH_SHORT);
                    closeLoadingDialog();
                    login.setDisable(false);
                    break;
                }
                //判断本地是否存在Company,没有则不进行处理。
                if (!StringUtils.isEmpty(Constants.MyCompanySN)) {
                    log.info("目前有有效的会话。不需要重新登录pos，只需要登录Staff");
                    staffLoginHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    staffLoginHttpBO.setStaff(Constants.getCurrentStaff());
                    staffLoginHttpBO.loginAsync();
                }
            }
            break;
        } while (false);
    }

    private void toSyncDataActivity() {
        try {
            bannerTimerTask.cancel();
            bannerTimer.cancel();
            bannerTimer.purge();
            syncDataActivity.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断网情况下登录成功，初始化收银汇总的部分字段
     */
    private void initRetailTradeAggregation(Staff s) {
        BaseController.retailTradeAggregation = new RetailTradeAggregation();
        BaseController.retailTradeAggregation.setStaffID(Integer.parseInt(String.valueOf(s.getID())));
        BaseController.retailTradeAggregation.setWorkTimeStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        log.info("收银员登录成功，该收银员开始工作的时间是" + retailTradeAggregation.getWorkTimeStart().toString());
        BaseController.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseController.retailTradeAggregation.getWorkTimeStart(), 1));//下班时间的初始值比上班时间多一秒
    }

    private boolean checkLoginInfo(String username, String password) {
        if (!FieldFormat.checkMobile(username)) {
            ToastUtil.toast("请输入正确的手机号码", ToastUtil.LENGTH_SHORT);
            return false;
        } else if (!FieldFormat.checkPassword(password)) {
            ToastUtil.toast("密码格式不正确,请输入6-16位的字符,首尾不能有空格", ToastUtil.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    /**
     * 勾选/未勾选记住密码后，则往SQLite中存入该用户
     */
    private void rememberPassword() {
        if (cbRememberPassword.isSelected()) {  //勾选了记住密码
            rememberLoginStaffSQLiteBO.createAsync(BaseSQLiteBO.INVALID_INT_ID, new RememberLoginStaff(username.getText().toString(), password.getText().toString(), true));
        } else { //未勾选记住密码，只记住账号
            rememberLoginStaffSQLiteBO.createAsync(BaseSQLiteBO.INVALID_INT_ID, new RememberLoginStaff(username.getText().toString(), "", false));
        }
    }

    /**
     * 动态申请权限结束后，不管是否允许权限，接下来做的事情都一样，都要判断网络是否可用，进行本地SQLite Pos明文密码和公司SN的查找，没有这两项，需要请求服务器返回，若有，直接进行POS登录
     */
    private void posLogin() {
        try {
            if (!networkUtils.isNetworkAvalible()) {
                // TODO: 2020/6/10 网络ping是否能通，这里是否应当阻塞线程
                // 断网状态，设置SessionID为NULL
                GlobalController.getInstance().setSessionID(null);
                closeLoadingDialog();
                //
                Platform.runLater(() -> {
                    showToastMessage("网络连接失败！");
                });
                iv_poslogin_tip.setImage(new Image(getClass().getResource("/image/posloginred.png").toString()));
                lb_posLogin_tip.setText("未就绪");
                //BaseActivity.posID=0则需要查询本地POS数据，找到POS机自己的身份
                if (Constants.posID == 0) {
                    retrieve1Pos();
                }
            } else {
                showLoadingDialog();
                //查找本地的Company，如果Company中没有SN，发送请求到服务器获取
                log.info(Thread.currentThread());
                retrieveCompanyInSQLite();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新初始化用户的会话信息，防止污染下一个上班的收银员的会话信息
     */
    protected void initialization() {
        retailTradeAggregation = null;
        showCommList = new ArrayList<Commodity>();
        retailTrade = new RetailTrade();//用于记录零售单
        lastRetailTrade = null;
        lastRetailTradePaymentType = "";
        lastRetailTradeChangeMoney = 0.000000d;
        lastRetailTradeAmount = 0.000000d;
    }

    private void retrieve1Pos() throws SocketException, UnknownHostException {
        List<Pos> poses = null;
        Pos pos = new Pos();
        pos.setShopID(BaseSQLiteBO.INVALID_INT_ID);
        if (!networkUtils.isNetworkAvalible()) {
            // 这里原本是根据POS_SN进行查询的，但由于wpos这边断网时无法获取MAC地址，并且POS表中只会存在一条pos数据，所以这里直接RN查出pos的信息
            poses = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, pos);
        } else {
            InetAddress ia = InetAddress.getLocalHost();
            Constants.MyPosSN = getLocalMac(ia);
            pos.setSql("where F_POS_SN = '%s'");
            pos.setConditions(new String[]{Constants.MyPosSN});
            poses = (List<Pos>) posPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_Pos_RetrieveNByConditions, pos);
        }
        if (posPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("查找本地POS失败，错误码：" + posPresenter.getLastErrorCode());
            ToastUtil.toast("POS机登录失败,请联网登录！", ToastUtil.LENGTH_SHORT);
            return;
        }
        if (poses != null && poses.size() == 1) {
            pos = poses.get(0);
            log.info("查找本地POS成功，POS机为：" + pos);
            Constants.posID = pos.getID();
        } else {
            log.error("本地SQLite中POS机信息为空或多个，无法确定POS的身份。poses=" + poses);
            ToastUtil.toast("POS机登录失败，请联网登录！", ToastUtil.LENGTH_SHORT);
        }
    }

    /**
     * 在本地查找Company，POS和Staff登录都需要用到Company的SN
     *
     * @throws
     */
    private void retrieveCompanyInSQLite() throws InterruptedException {
        showLoadingDialog();
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companySQLiteBO.retrieveNASync(BaseSQLiteBO.INVALID_CASE_ID, null);
        long lTimeOut = 1000;
        while (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            log.info("本地查找Company超时！");
        }
    }

    public void initTimers() {   //做一些timer初始化操作
        // TODO: 2020/6/10 应该在window消失后，将timer释放;但是现在不知道在哪释放合适
        timerCheckInvalidSession = new Timer();

        timerPing = new Timer();//开启保持session的线程

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
    }

    //在这里执行主线程事件
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                closeLoadingDialog();
                if (inputCompanySNDialog != null) {
                    inputCompanySNDialog.close();
                }
                if (isCreateInterceptorDialog == 1) {
                    loginInterceptorDialog.show();
                }

                initTimers();
                break;
            case 2:
                closeLoadingDialog();
                login.setDisable(false);
                //更新界面
                iv_poslogin_tip.setImage(new Image(getClass().getResource("/image/posloginred.png").toString()));
                lb_posLogin_tip.setText("未就绪");
                ToastUtil.toast("Pos登录失败！", ToastUtil.LENGTH_SHORT);
                break;
            case 3:
                closeLoadingDialog();
                login.setDisable(false);
                if (Constants.getCurrentStaff().getRoleID() == Constants.preSale_Role_ID) {
                    username.setText("");
                    password.setText("");
                    try {
                        preSaleActivity.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StageController.get().closeStge(StageType.LOGIN_STAGE.name()).unloadStage(StageType.LOGIN_STAGE.name());
                } else {
                    rememberPassword();//记住密码
                    username.setText("");
                    password.setText("");
                    toSyncDataActivity();
                    StageController.get().closeStge(StageType.LOGIN_STAGE.name()).unloadStage(StageType.LOGIN_STAGE.name());
                    log.info("已经finish了LoginActivity");
                }
                break;
            case 4:
                closeLoadingDialog();
                login.setDisable(false);
                ToastUtil.toast("帐号或密码错误，请重新输入", ToastUtil.LENGTH_SHORT);
                break;
            case 5:
                login.setDisable(false);
                resetPasswordWarningDialog.show();
                break;
            case 6:
                createInputCompanySNDialog();
                break;
            case 7:
                Company c = (Company) companySQLiteEvent.getBaseModel1();
                showCompanySN.setText("公司编号：" + c.getSN());
                Constants.MyCompanySN = c.getSN();
                //在查看本地SQLite中是否存在公司SN之后，需要进行查看本地SQLite中是否存在POS的明文密码
                log.info("开始获取Pos明文密码");
                posSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_Retrieve1Async);
                posSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                posSQLiteBO.retrieve1ASync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, null);
                break;
            case 8:
                showCompanySN.setText("公司编号：" + msg.obj);
            default:
                break;
        }
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
