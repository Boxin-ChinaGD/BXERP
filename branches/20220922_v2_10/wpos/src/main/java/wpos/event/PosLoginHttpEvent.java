package wpos.event;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("posLoginHttpEvent")
@Scope("prototype")
public class PosLoginHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isLoginStaffAfterLoginSuccess() {
        return loginStaffAfterLoginSuccess;
    }

    public void setLoginStaffAfterLoginSuccess(boolean loginStaffAfterLoginSuccess) {
        this.loginStaffAfterLoginSuccess = loginStaffAfterLoginSuccess;
    }

    /**
     * pos登录成功后，如果仍然要登录staff，则值要预先设为true，以便登录staff
     */
    protected boolean loginStaffAfterLoginSuccess;

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType", getRequestType());

        do {
            GlobalController.getInstance().setSessionID(null);//当且仅当登录成功后，才设为有效值  ...

            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("Pos网络请求错误，" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                switch (getRequestType()) {
                    case ERT_PosGetToken:
//                    setResponse(getResponse());
//                    setResponseData(rsp);
                        break;
                    case ERT_PosLogin:
                        log.info("POS登录成功。会话=" + getTempSessionID());
                        GlobalController.getInstance().setSessionID(getTempSessionID());
                        //解析服务器返回的POS信息，然后把POSID 放入  Constants.posID中即可
                        try {
                            jsonObject = JSONObject.parseObject(jsonObject.get(BaseModel.JSON_OBJECT_KEY).toString());
                            log.info("获取到服务器返回的POSID:" + jsonObject.getInteger("ID"));
                            Constants.posID = jsonObject.getInteger("ID");
                            Constants.shopID = jsonObject.getInteger("shopID");
                            log.info("Constants.posID的为：" + Constants.posID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            log.info("获取服务器返回的POSID失败！失败原因：" + e.getMessage());
                        }
                        break;
                    default:
                        log.info("Not yet implemented!");
                        throw new RuntimeException("Not yet implemented!");
                }
            } else {
                log.info("POS登录失败！");
            }
        } while (false);

        //让调用者不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }
}