package com.bx.erp.action.wx;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.model.wx.BaseWxModel;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;

public class WXPayConfigImpl implements WXPayConfig {
	private static Log logger = LogFactory.getLog(WXPayConfigImpl.class);

	// 支付服务商
	/** 服务商 公众号APPID 防止泄露 */
	private static String AppID = null;
	/** 服务商 商户号 防止泄露 */
	private static String MchID = null;
	/** 服务商 API 密钥 防止泄露 */
	private static String KEY = null;

	private byte[] certData;

	/** 初始化配置
	 * 
	 * @param env
	 * @param appid
	 * @param mchid
	 * @param secret
	 */
	public WXPayConfigImpl(final String appid, final String mchid, final String secret, final String cert) {
		try {
			if (StringUtils.isEmpty(BaseAction.ENV.getName()) || StringUtils.isEmpty(appid) || StringUtils.isEmpty(mchid) || StringUtils.isEmpty(secret) || StringUtils.isEmpty(cert)) {
				logger.info("微信支付初始化配置错误，请检查env.properties文件配置是否正确！！");
				logger.info("env=" + BaseAction.ENV);
				logger.info("appid=" + appid);
				logger.info("mchid=" + mchid);
				logger.info("secret=" + secret);
				logger.info("cert=" + cert);
				return;
			}

			// 必须放在KEY前面
			AppID = appid;
			MchID = mchid;

			if (EnumEnv.DEV.getName().equals(BaseAction.ENV.getName())) {
				if (BaseAction.UseSandBox) {
					KEY = getSandboxSignKey(secret);
					// 沙箱环境下让其每次请求微信时休眠5秒，不要频繁请求，否则可能发生意外情况
					Thread.sleep(5000);
				} else {
					KEY = secret;
				}
			} else if (EnumEnv.SIT.getName().equals(BaseAction.ENV.getName()) || EnumEnv.UAT.getName().equals(BaseAction.ENV.getName()) || EnumEnv.PROD.getName().equals(BaseAction.ENV.getName())) {
				KEY = secret;
				BaseAction.UseSandBox = false;
			} else {
				logger.info("env=" + BaseAction.ENV);
				logger.info("运行环境配置错误，请检查env.properties配置是否正确！！");
				return;
			}

			File file = new File(cert); // TODO 证书需要放在安全的地方！！
			InputStream certStream = new FileInputStream(file);
			this.certData = new byte[(int) file.length()];
			certStream.read(this.certData);
			certStream.close();
		} catch (Exception e) {
			logger.info("微信支付初始化配置异常：" + e.getMessage());
		}
	}

	public String getAppID() {
		return AppID;
	}

	public String getMchID() {
		return MchID;
	}

	public String getKey() {
		return KEY;
	}

	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 10000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	/** @param secret
	 *            真实环境的key <br>
	 *            根据真实环境的key获取沙箱环境下的KEY */
	public String getSandboxSignKey(String secret) {
		WXPay wxPay = new WXPay(this);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mch_id", this.getMchID());
			params.put("nonce_str", WXPayUtil.generateNonceStr());
			params.put("sign", WXPayUtil.generateSignature(params, secret));
			String strXML = wxPay.requestWithoutCert("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", params, this.getHttpConnectTimeoutMs(), this.getHttpReadTimeoutMs());

			Map<String, String> result = WXPayUtil.xmlToMap(strXML);
			logger.info("沙箱KEY：" + result);
			if ("SUCCESS".equals(result.get(BaseWxModel.WXPay_RETURN))) {
				return result.get("sandbox_signkey");
			}

			return null;
		} catch (Exception e) {
			System.out.println("获取沙箱KEY异常：" + e.getMessage());
			return null;
		}
	}
}
