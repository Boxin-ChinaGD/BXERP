
package wpos.bo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseEvent;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.http.HttpRequestManager;
import wpos.http.HttpRequestUnit;
import wpos.http.request.PosGetToken;
import wpos.http.request.PosLogin;
import wpos.model.ErrorInfo;
import wpos.model.Pos;
import wpos.utils.RSAUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component("posLoginHttpBO")
@Scope("prototype")
public class PosLoginHttpBO extends LoginHttpBO {
    private Log log = LogFactory.getLog(this.getClass());

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    protected Pos pos;
    protected String pwdEncrypted;

    public PosLoginHttpBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        super(sEvent, hEvent);
    }

    public PosLoginHttpBO() {
    }

    @Override
    protected String getKey() {
        return String.valueOf(pos.getID());
    }

    /**
     * 先get token后login
     * 本函数阻塞线程
     */
    @Override
    protected void doLoginAsync() {
        log.info("正在执行PosLoginHttpBO的doLoginAsync");

        try {
            //pos GetToken
            RequestBody body = new FormBody.Builder()
                    .add(pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID()))
                    .build();
            Request request = new Request.Builder()
                    .url(Configuration.HTTP_IP + "pos/getTokenEx.bx")
                    .post(body)
                    .build();
            httpEvent.setEventProcessed(false);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            HttpRequestUnit hru1 = new PosGetToken();
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
                log.info("pos getToken超时！");
                return;
            }
            if (httpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                return;
            }

            //pos Login
            httpEvent.setEventProcessed(false);
            Response response = httpEvent.getResponse();
            //解析modulus和exponent //...model.parse1
            String responseData = httpEvent.getResponseData();
            JSONObject jsonObject = JSONObject.parseObject(responseData);
            String str = jsonObject.getString("rsa");
            JSONObject rsaData = JSONObject.parseObject(str);
            String modulus = rsaData.getString("modulus");
            String exponent = rsaData.getString("exponent");
            //生成公钥
            modulus = new BigInteger(modulus, 16).toString();
            exponent = new BigInteger(exponent, 16).toString();
            //
            RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
            //...加密密码
            pwdEncrypted = RSAUtils.encryptByPublicKey(pos.getPasswordInPOS(), publicKey);
            //解析Session
            Headers headers = response.headers();
            List<String> cookies = headers.values("Set-Cookie");
            String cookie = cookies.get(0);
            GlobalController.getInstance().setSessionID(cookie.substring(0, cookie.indexOf(";")));
            log.info("tempSessionID: " + GlobalController.getInstance().getSessionID());
            httpEvent.setTempSessionID(GlobalController.getInstance().getSessionID());
            httpEvent.setPwdEncrypted(pwdEncrypted);
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            //
            RequestBody loginBody = new FormBody.Builder()
                    .add(pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID()))
                    .add(pos.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)
                    .add(pos.field.getFIELD_NAME_companySN(), Constants.MyCompanySN) //... TODO
                    //这里的int1注释掉测试类也能登陆成功，不知道什么作用
                    .add(pos.field.getFIELD_NAME_resetPasswordInPos(), String.valueOf(pos.getResetPasswordInPos()))
                    .build();
            //
            Request requestLogin = new Request.Builder()
                    .url(Configuration.HTTP_IP + "pos/loginEx.bx")
                    .addHeader(BaseHttpBO.COOKIE, GlobalController.getInstance().getSessionID())
                    .post(loginBody)
                    .build();
            //
            httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
            HttpRequestUnit hru = new PosLogin();
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
                log.info("pos login超时！");
                return;
            }
            if (httpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError); //
            log.info("POS登录失败，错误信息：" + e.getMessage());
        }
    }

    public void posGetToken(final Pos pos) {
        log.info("正在执行PosLoginHttpBO的posGetToken");

        httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        httpEvent.setRequestType(HttpRequestUnit.EnumRequestType.ERT_PosGetToken);

        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = GlobalController.client;

                try {
                    RequestBody body = new FormBody.Builder()
                            .add(pos.field.getFIELD_NAME_ID(), String.valueOf(pos.getID()))
                            .build();
                    Request request = new Request.Builder()
                            .url(Configuration.HTTP_IP + "pos/getTokenEx.bx")
                            .post(body)
                            .build();

                    httpEvent.setEventProcessed(false);
                    httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
                    HttpRequestUnit hru1 = new PosGetToken();
                    hru1.setRequest(request);
                    hru1.setTimeout(TIME_OUT);
                    hru1.setbPostEventToUI(true);
                    hru1.setEvent(httpEvent);
                    HttpRequestManager.getCache(HttpRequestManager.EnumDomainType.EDT_Communication).pushHttpRequest(hru1);
                    //
                    long lTimeOut = 50;
                    while (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
                        Thread.sleep(1000);
                    }
                    if (httpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
                        log.info("pos getToken超时！");
                    }

                    Response response = null;
                    response = client.newCall(request).execute();
                    //解析modulus和exponent
                    String responseData = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    String str = jsonObject.getString("rsa");
                    JSONObject rsaData = JSONObject.parseObject(str);
                    String modulus = rsaData.getString("modulus");
                    String exponent = rsaData.getString("exponent");
                    //生成公钥
                    modulus = new BigInteger(modulus, 16).toString();
                    exponent = new BigInteger(exponent, 16).toString();

                    RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

                    // ..加密密码
                    pwdEncrypted = RSAUtils.encryptByPublicKey(pos.getPasswordInPOS(), publicKey);
                    pos.setPwdEncrypted(pwdEncrypted);

                    //解析session
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    String cookie = cookies.get(0);
//                    GlobalController.getInstance().setSessionID(cookie.substring(0, cookie.indexOf(";")));
//                    httpEvent.setTempSessionID(GlobalController.getInstance().getSessionID());

                    httpEvent.setPwdEncrypted(pwdEncrypted);

                    httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                    httpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
//                    EventBus.getDefault().post(httpEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                    httpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                    log.info("pos登录发生意外，错误信息：" + e.getMessage());
                }
            }
        }.start();
    }
}
