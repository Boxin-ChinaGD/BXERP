package wpos.event;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.http.HttpRequestUnit;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Ntp;
import wpos.utils.StringUtils;

@Component("ntpHttpEvent")
@Scope("prototype")
public class NtpHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求出错：" + lastErrorCode);
                break;
            }
            String rsp = getResponseData();
            log.info("同步时间，服务器返回的数据：" + rsp);
            JSONObject jsonObject = null;
            try {
                jsonObject = parseError(getResponseData());
            } catch (Exception e) {
                e.printStackTrace();
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                break;
            }
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_NtpSync:
                    handleNetSync(jsonObject);
                    break;
            }
        } while (false);

        //让调用者不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleNetSync(JSONObject jsonObject) {
        try {
            JSONObject joNtp = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Ntp ntp = new Ntp();
            if (ntp.parse1(joNtp.toString()) == null) {
                log.info("Sync:Failed to parse Ntp!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                long t4 = (Long) getData(); // 在同步的时候设置POS接收到答复的时间,用于POS机与服务器时间同步，获取的是时间戳
                log.info("ERT_NtpSync得到的对象是：" + ntp.toString());
                ntp.setT4(t4);
                setBaseModel1(ntp);
                setRequestType(HttpRequestUnit.EnumRequestType.ERT_NtpSync);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析ntp失败，错误消息：" + e.getMessage());
        }
    }
}
