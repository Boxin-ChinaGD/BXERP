package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.http.HttpRequestManager;
import com.bx.erp.http.HttpRequestUnit;
import com.bx.erp.http.request.StaffGetToken;
import com.bx.erp.http.request.StaffLogin;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.utils.RSAUtils;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffLoginHttpBO extends LoginHttpBO {
    private Logger log = Logger.getLogger(this.getClass());

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    protected Staff staff;
    private String pwdEncrypted;
    private RSAPublicKey publicKey;

    public StaffLoginHttpBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        super(ctx, sEvent, hEvent);
    }

    @Override
    protected String getKey() {
        return staff.getPhone();
    }

    /**
     * 先get token后login
     * 本函数阻塞线程
     */
    @Override
    protected void doLoginAsync() {
        log.info("正在执行StaffLoginHttpBO的doLoginAsync");

        try {
            //currentStaff getToken
            RequestBody body = new FormBody.Builder()
                    .add((staff.getIsBXStaff() == 0 ? staff.field.getFIELD_NAME_phone() : staff.mobile), staff.getPhone())
                    .build();
            Request request = new Request.Builder()
                    .url(Configuration.HTTP_IP + (staff.getIsBXStaff() == 0 ? staff.HTTP_STAFF_getToken : staff.HTTP_BXSTAFF_getToken))
                    .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                    .post(body)
                    .build();
            httpEvent.setEventProcessed(false);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            HttpRequestUnit hru1 = new StaffGetToken();
            hru1.setRequest(request);
            hru1.setTimeout(TIME_OUT);
            hru1.setbPostEventToUI(true);
            hru1.setEvent(httpEvent);
            HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru1);
            //
            long lTimeOut = TIME_OUT;
            while (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                log.info("Staff getToken超时！");
                return;
            }

            //currentStaff doLoginAsync
            httpEvent.setEventProcessed(false);
            String responseData = httpEvent.getResponseData();
            //解析modulus和exponent    //...model.parse1
            JSONObject jsonObject = new JSONObject(responseData);
            String str = jsonObject.getString("rsa");
            JSONObject rsaData = new JSONObject(str);
            String modulus = rsaData.getString("modulus");
            String exponent = rsaData.getString("exponent");
            //生成公钥
            modulus = new BigInteger(modulus, 16).toString();
            exponent = new BigInteger(exponent, 16).toString();
            //
            RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
            //...加密密码
            pwdEncrypted = RSAUtils.encryptByPublicKey(staff.getPasswordInPOS(), publicKey);
            httpEvent.setPwdEncrypted(pwdEncrypted);
            //解析Session,
            Response response = httpEvent.getResponse();
            Headers headers = response.headers();
            List<String> cookies = headers.values("Set-Cookie");
            //旧会话过期的话，不能再使用旧的会话去请求。getToken后，如果服务器返回新的会话，则cookies.size() 会 > 0，这时将服务器返回的sessionID记录起来，以备将来发请求使用
            if (cookies.size() > 0) {
                String cookie = cookies.get(0);
                GlobalController.getInstance().setSessionID(cookie.substring(0, cookie.indexOf(";")));
            }
            //
            RequestBody loginBody = new FormBody.Builder()
                    .add((staff.getIsBXStaff() == 0 ? staff.field.getFIELD_NAME_phone() : staff.mobile), getKey())
                    .add(staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)
                    .add(staff.field.getFIELD_NAME_companySN(), Constants.MyCompanySN)//... TODO
                    .add(staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(staff.getIsLoginFromPos()))
                    .build();
            //
            Request requestLogin = new Request.Builder()
                    .url(Configuration.HTTP_IP + (staff.getIsBXStaff() == 0 ? staff.HTTP_STAFF_login : staff.HTTP_BXSTAFF_login))
                    .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                    .post(loginBody)
                    .build();
            //
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
            httpEvent.setTempSessionID(GlobalController.getInstance().getSessionID());
            //
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            HttpRequestUnit hru = new StaffLogin();
            hru.setRequest(requestLogin);
            hru.setTimeout(TIME_OUT);
            hru.setbPostEventToUI(true);
            hru.setEvent(httpEvent);
            HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru);
            //
            while (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                log.info("Staff login超时！");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError); //
            log.info("Staff登录失败，错误信息：" + e.getMessage());
        }
    }

    public void staffGetToken(final Staff staff, final boolean isPost) throws Exception {
        log.info("正在执行StaffLoginHttpBO的staffGetToken");

        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setRequestType(HttpRequestUnit.EnumRequestType.ERT_StaffGetToken);
        try {
            httpEvent.setEventProcessed(false);
            RequestBody body = new FormBody.Builder()
                    .add(staff.field.getFIELD_NAME_phone(), staff.getPhone())
                    .build();
            Request request = new Request.Builder()
                    .url(Configuration.HTTP_IP + "staff/getTokenEx.bx")
                    .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                    .post(body)
                    .build();
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            HttpRequestUnit hru1 = new StaffGetToken();
            hru1.setRequest(request);
            hru1.setTimeout(TIME_OUT);
            hru1.setbPostEventToUI(true);
            hru1.setEvent(httpEvent);
            HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru1);
            //
            long lTimeOut = TIME_OUT;
            while (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                log.info("Staff getToken超时！");
            }

            String responseData = httpEvent.getResponseData();
            //解析modulus和exponent
            JSONObject jsonObject = new JSONObject(responseData);
            String str = jsonObject.getString("rsa");
            JSONObject rsaData = new JSONObject(str);
            String modulus = rsaData.getString("modulus");
            String exponent = rsaData.getString("exponent");
            //生成公钥
            modulus = new BigInteger(modulus, 16).toString();
            exponent = new BigInteger(exponent, 16).toString();
            //
            publicKey = RSAUtils.getPublicKey(modulus, exponent);
            //...加密密码
            pwdEncrypted = RSAUtils.encryptByPublicKey(staff.getPasswordInPOS(), publicKey);
            staff.setPwdEncrypted(pwdEncrypted); //???
            setPwdEncrypted(pwdEncrypted);
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
            httpEvent.setPwdEncrypted(pwdEncrypted);

            if (isPost) {
                httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
                EventBus.getDefault().post(httpEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map encryptedPassword(Staff staff, boolean isPost, String newPassword) throws Exception {
        log.info("正在执行StaffLoginHttpBO的encryptedPassword");

        staffGetToken(staff, isPost);
        String newPwdEncrypted = RSAUtils.encryptByPublicKey(newPassword, publicKey);
        Map passwordMap = new HashMap();
        passwordMap.put("OriginalPassword", pwdEncrypted);
        passwordMap.put("NewPassword", newPwdEncrypted);
        return passwordMap;
    }
}
