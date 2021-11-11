package wpos.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.presenter.*;

import javax.annotation.Resource;

@Component("GlobalController")
public class GlobalController {
    private static GlobalController globalStage = null;

    /**
     * 在微信支付的时候，这个时间要>=nbr请求微信支付的超时时间+10秒。如果这里是60s，nbr和微信通讯的超时时间是50s或以下
     */
    public static final int HTTP_REQ_Timeout = 60;

    //设置OKHttp的超时时间
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .writeTimeout(HTTP_REQ_Timeout, TimeUnit.SECONDS)
            .build();

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getPwdEncrypted() {
        return pwdEncrypted;
    }

    public void setPwdEncrypted(String pwdEncrypted) {
        this.pwdEncrypted = pwdEncrypted;
    }

    public static List<BaseModel> getList() {
        return list;
    }

    public static void setList(List<BaseModel> list) {
        GlobalController.list = list;
    }

    /**
     * 保存POS/Saff和服务器通信的会话
     */
    protected String sessionID;

    protected String pwdEncrypted;

    protected static List<BaseModel> list = new ArrayList<BaseModel>(); //用于装载配置性数据的

    protected GlobalController() {}

    //    TODO
    public static GlobalController init() {
        if (globalStage == null) {
            globalStage = new GlobalController();
            return globalStage;
        }
        return null;
    }

    public static GlobalController getInstance() {
        return globalStage;
    }


}
