//package wpos.helper;
//
//import com.github.wxpay.sdk.WXPay;
//import com.github.wxpay.sdk.WXPayConfig;
//import com.github.wxpay.sdk.WXPayUtil;
//
//import org.apache.log4j.Logger;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//public class WXPayConfigImpl implements WXPayConfig {
//    private Log log = LogFactory.getLog(this.getClass());
//
//// 支付服务商
//    /** 服务商 公众号APPID 防止泄露 */
//    private static String AppID = "";
//    /** 服务商 商户号 防止泄露 */
//    private static String MchID = "";
//    /** 服务商 API 密钥 防止泄露 */
//	private String KEY = "";
//
//    private byte[] certData;
//
//    public WXPayConfigImpl(final String appid , final String mchid, final String secret)  {
//        if ("".equals(appid) || "".equals(mchid) || "".equals(secret)) {
//            log.info("微信支付初始化配置错误，请检查appConfig文件配置是否正确！！");
//            log.info("appid=" + appid);
//            log.info("mchid=" + mchid);
//            log.info("secret=" + secret);
//            return;
//        }
//
//        // 必须放在KEY前面
//        AppID = appid;
//        MchID = mchid;
//
//        if (Configuration.bUseSandBox) {
//            KEY = getSandboxSignKey(secret);
//            try {
//                // 沙箱环境下让其每次请求微信时休眠5秒，不要频繁请求，否则可能发生意外情况
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else{
//            KEY = secret;
//        }
//    }
//
//    @Override
//    public String getAppID() {
//        return AppID;
//    }
//
//    @Override
//    public String getMchID() {
//        return MchID;
//    }
//
//    @Override
//    public String getKey() {
//        return KEY;
//    }
//
//    @Override
//    public InputStream getCertStream() {
//        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
//        return certBis;
//    }
//
//    @Override
//    public int getHttpConnectTimeoutMs() {
//        return 8000;
//    }
//
//    @Override
//    public int getHttpReadTimeoutMs() {
//        return 10000;
//    }
//
//    /**
//     * 根据真实环境的key获取沙箱环境下的KEY
//     */
//    public String getSandboxSignKey(String secert) {
//        WXPay wxPay = new WXPay(this);
//        try {
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("mch_id", this.getMchID());
//            params.put("nonce_str", WXPayUtil.generateNonceStr());
//            params.put("sign", WXPayUtil.generateSignature(params, secert));
//            String strXML = wxPay.requestWithoutCert("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey",
//                    params, this.getHttpConnectTimeoutMs(), this.getHttpReadTimeoutMs());
//
//            log.info("repXML=" + strXML);
//            Map<String, String> result = WXPayUtil.xmlToMap(strXML);
//
//            log.info("沙箱KEY：" + result);
//            if ("SUCCESS".equals(result.get("return_code"))) {
//                String rsp = result.get("sandbox_signkey");
//                return rsp;
//            }
//
//            return null;
//        } catch (Exception e) {
//            log.info("获取沙箱KEY异常：" + e.getMessage());
//            return null;
//        }
//    }
//
//}
