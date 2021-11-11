package wpos.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import okhttp3.Response;

@Component("posGetTokenEvent")
@Scope("prototype")
public class PosGetTokenEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("Pos getToken网络请求错误，" + lastErrorCode);
                break;
            }
            String rsp = getResponseData();
            log.info("pos getToken服务器返回的数据：" + rsp);

            Response response = getResponse();

            setResponseData(rsp);
            setResponse(response);

//            //
//            try {
//                JSONObject jsonObject = new JSONObject(rsp);
//                String str = jsonObject.getString("rsa");
//                JSONObject rsaData = new JSONObject(str);
//                String modulus = rsaData.getString("modulus");
//                String exponent = rsaData.getString("exponent");
//                //生成公钥
//                modulus = new BigInteger(modulus, 16).toString();
//                exponent = new BigInteger(modulus, 16).toString();
//                //
//                RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
//                //加密密码
//                setString1(RSAUtils.encryptByPublicKey());
//
//                setResponse();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } while (false);

        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }
}
