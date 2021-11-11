package com.bx.erp.action.wx;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.wx.WXPayInfo;
import com.bx.erp.util.JsonUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;

import net.sf.json.JSONObject;

import com.github.wxpay.sdk.WXPayUtil;

@Controller
@RequestMapping("/wxpay")
@Scope("prototype")
public class WXPayAction extends BaseAction {
	private static Log logger = LogFactory.getLog(WXPayAction.class);

	@Resource
	private VipBO vipBO;

//	@Resource
//	private WxVipBO wxVipBO;

//	@Resource
//	private WxBonusUsedDetailsBO wxBonusUsedDetailsBO;

	@Value("${env.appid}")
	private String appid;

	@Value("${env.mchid}")
	private String mchid;

	@Value("${env.secret}")
	private String secret;

	@Value("${env.cert}")
	private String cert;

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 微信公众号获取访问微信端的token接口

//	@Value("${post.card.updateVipUser}")
//	private String post_card_updateVipUser;// 更新会员信息

//	@Value("${post.card.consume}")
//	protected String cardConsumeUrl; // 核销卡券

	@RequestMapping(value = "/microPayEx", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String microPayEx(@ModelAttribute("SpringWeb") WXPayInfo wxPayInfo, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Map<String, Object> params = new HashMap<>();

		Double mount = BaseModel.TOLERANCE;
		do {
			try {
				mount = Double.parseDouble(wxPayInfo.getTotal_fee());
				logger.debug("App传递的金额=" + mount);
				if (wxPayInfo.getTotal_fee().length() - wxPayInfo.getTotal_fee().indexOf(".") == 3) { // App传递的金额一定有2位小数
					break;
				}
			} catch (Exception ex) {
			}
			logger.info("订单金额格式错误");
			return null;
		} while (false);

		if (mount <= BaseModel.TOLERANCE) {
			if (!StringUtils.isEmpty(wxPayInfo.getCouponCode())) {
				// 商家创建满10-10的优惠券，顾客有此优惠券,并且购买商品的金额为10元，那么优惠后则为0元，微信不支持支付0元
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "此单优惠后价格为 0 元，不支持微信支付");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			} else {
				return "";
			}
		}

		WXPayConfigImpl config = new WXPayConfigImpl(appid, mchid, secret, cert);

		WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, UseSandBox);
		Map<String, String> microPayData = new HashMap<String, String>();

		String nonce_str = WXPayUtil.generateNonceStr();
		// String sign = WXPayUtil.generateSignature(microPayData, config.getKey(),
		// WXPayConstants.SignType.MD5);
		String out_trade_no = String.valueOf(System.currentTimeMillis() % 1000000) + nonce_str.substring(6);

		// 从公司的缓存拿到子商户号
		Company company = getCompanyFromSession(session);
		microPayData.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(), company.getSubmchid());
		microPayData.put(WXPayInfo.field.getFIELD_NAME_body(), "博昕-微信付款码支付-测试"); // 商品描述
		microPayData.put(WXPayInfo.field.getFIELD_NAME_out_trade_no(), out_trade_no); // 交易单号
		microPayData.put(WXPayInfo.field.getFIELD_NAME_spbill_create_ip(), "127.0.0.1"); // 终端IP
		if (BaseAction.UseSandBox) {
			microPayData.put(WXPayInfo.field.getFIELD_NAME_total_fee(), "1"); // 订单总金额，单位为分，只能为整数
			microPayData.put(WXPayInfo.field.getFIELD_NAME_auth_code(), "134526050509995238"); // 沙箱环境下的支付授权码
		} else {
			String amount = wxPayInfo.getTotal_fee().substring(0, wxPayInfo.getTotal_fee().indexOf("."));
			microPayData.put(WXPayInfo.field.getFIELD_NAME_total_fee(), amount); // 订单总金额，单位为分，只能为整数
			microPayData.put(WXPayInfo.field.getFIELD_NAME_auth_code(), wxPayInfo.getAuth_code()); // 授权码
		}

		// microPayData.put("scene_info", "{\"store_info\" : {\r\n" + "\"id\":
		// \"15403437\",\r\n" + "\"name\": \"月荷居\",\r\n" +
		// "\"area_code\":\"440305\",\r\n" + "\"address\": \"科学家之家F栋\" }}\r\n" + "");
		try {
			// 扫码支付
			Map<String, String> resp1 = wxpay.microPay(microPayData);
			params.putAll(resp1);
			logger.info("microPayData : " + resp1);
			if (resp1.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp1.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_SUCCESS)) {
				logger.info("付款码支付成功！！！");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			} else if (resp1.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp1.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_FAIL) && resp1.get("err_code").equals("USERPAYING")) {
				// 用户支付中，等待10秒，然后调用被扫订单结果查询API，查询当前订单的不同状态，决定下一步的操作
				Thread.sleep(10 * 1000);
				// 从公司的缓存拿到子商户号
				// 查询订单
				Map<String, String> orderQueryData = new HashMap<String, String>();
				orderQueryData.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(), company.getSubmchid());
				// orderQueryData.put(WXPayInfo.field.getFIELD_NAME_nonce_str(), nonce_str);
				// orderQueryData.put(WXPayInfo.field.getFIELD_NAME_sign(), sign);
				orderQueryData.put(WXPayInfo.field.getFIELD_NAME_out_trade_no(), out_trade_no);
				//
				int timeOut = 30; // 官方建议30秒
				Map<String, String> resp2 = wxpay.orderQuery(orderQueryData);
				logger.info("orderQueryData : " + resp2);
				while (resp2.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp2.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_SUCCESS) && resp2.get("trade_state").equals("USERPAYING") && timeOut-- > 0) {
					int timespan = 5;
					Thread.sleep(timespan * 1000);
					timeOut -= timespan;
					if (timeOut <= 0) {
						break;
					}
					
					resp2 = wxpay.orderQuery(orderQueryData);
					logger.info("orderQuerOyData : " + resp2);
				}

				params.putAll(resp2);
				if (resp2.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp2.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_SUCCESS) && resp2.get("trade_state").equals(BaseWxModel.WXPay_SUCCESS)) {
					logger.info("查询订单 --> 付款码支付成功！！！");
					params.put(KEY_HTMLTable_Parameter_msg, resp2.get(BaseWxModel.WXPAY_ERR_CODE_DES));
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				} else {
					logger.info("查询订单 --> 付款码支付失败！！！");
					logger.info("用户支付未完成，请撤销订单！！！");
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					params.put(KEY_HTMLTable_Parameter_msg, resp2.get(BaseWxModel.WXPAY_ERR_CODE_DES));
				}
			} else {
				logger.info("付款码支付失败！！！");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器异常");
			}

			// 携带优惠券码并支付成功后核销优惠券
//			if (!StringUtils.isEmpty(wxPayInfo.getCouponCode()) && EnumErrorCode.EC_NoError.toString().equals(params.get(JSON_ERROR_KEY))) {
//				JSONObject consumeCouponJSONObject = new JSONObject();
//				consumeCouponJSONObject.put(WxCoupon.field.getFIELD_NAME_code(), wxPayInfo.getCouponCode());
//				//
//				JSONObject jsonObject = WxUtils.publicAccountPostToWxServer(cardConsumeUrl, consumeCouponJSONObject.toString(), "核销优惠券", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//				if (!EnumErrorCode.EC_NoError.toString().equals(jsonObject.getString(JSON_ERROR_KEY))) {
//					logger.error("支付成功,核销优惠券失败！优惠券码=" + wxPayInfo.getCouponCode());
//					params.put(KEY_HTMLTable_Parameter_msg, "支付成功，核销优惠券失败！");
//				}
//			}

			// 进行积分的增减
//			if (wxPayInfo.getBonusIsChanged() == EnumBoolean.EB_Yes.getIndex()) {
//				do {
//					String dbName = company.getDbName();
//
//					WxCard wxCard = new WxCard();
//					wxCard.setCard_id(wxPayInfo.getWxVipCardID());
//					wxCard.setCode(wxPayInfo.getWxVipCardCode());
//					wxCard.setAdd_bonus(wxPayInfo.getAddBonus());
//
//					// 拿到需要的参数发送到WX
//					Map<String, Object> paramsToUpdateBonus = wxCard.getHttpUpdateParam(BaseWxBO.CASE_UpdateVipUser, wxCard);
//					JSONObject jsonFromUpdateBonus = JSONObject.fromObject(paramsToUpdateBonus);
//
//					JSONObject wxResponseWithUpdateBonus = WxUtils.publicAccountPostToWxServer(post_card_updateVipUser, jsonFromUpdateBonus.toString(), "进行积分变动", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//
//					if (wxResponseWithUpdateBonus.getString(BaseAction.JSON_ERROR_KEY) == EnumErrorCode.EC_NoError.toString()) {
//						int bonus = wxResponseWithUpdateBonus.getJSONObject(BaseAction.KEY_Object).getInt("result_bonus");
//
//						//TODO 以下三个DB操作可以放在一个SP当中完成，需要使用DB的事务
//						
//						Vip vip = new Vip();
//						vip.setID(wxPayInfo.getVipID());
//						vip.setAmountOnAddBonus(Integer.valueOf(wxPayInfo.getTotal_fee().substring(0, wxPayInfo.getTotal_fee().indexOf("."))));
//						vip.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
//						DataSourceContextHolder.setDbName(dbName);
//						Vip vipUpdate = (Vip) vipBO.updateObject(BaseWxBO.SYSTEM, BaseWxBO.CASE_Vip_UpdateBonus, vip);
//						if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//							logger.error("修改会员积分失败，错误码=" + vipBO.getLastErrorCode()); // TODO
//							break;
//						} else {
//							logger.info("修改会员积分成功！vip=" + vipUpdate);
//						}
//
//						if (vipUpdate.getBonus() != bonus) { // TODO 后面需要同步
//							logger.error("vip中的积分和Wx返回的积分不相等，vip的积分：" + vip.getBonus() + ", wx的积分：" + bonus);
//							break;
//						}
//
//						WxVip wxVip = new WxVip();
//						wxVip.setID(wxPayInfo.getWxVipID());
//						wxVip.setAmountOnAddBonus(Integer.valueOf(wxPayInfo.getTotal_fee().substring(0, wxPayInfo.getTotal_fee().indexOf("."))));
//						wxVip.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
//						DataSourceContextHolder.setDbName(dbName);
//						WxVip wxVipUpdate = (WxVip) wxVipBO.updateObject(BaseWxBO.SYSTEM, BaseWxBO.CASE_WxVip_UpdateBonus, wxVip);
//						if (wxVipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//							logger.error("修改微信会员积分失败，错误码=" + wxVipBO.getLastErrorCode()); // TODO
//							break;
//						} else {
//							logger.info("修改微信会员积分成功！wxVip=" + wxVipUpdate);
//						}
//
//						if (wxVip.getBonus() != bonus) {// TODO 后面需要同步
//							logger.error("wxVip中的积分和Wx返回的积分不相等，wxVip的积分：" + wxVip.getBonus() + ", wx的积分：" + bonus);
//							break;
//						}
//
//						WxBonusUsedDetails wxBonusUsedDetails = new WxBonusUsedDetails();
//						wxBonusUsedDetails.setWxVipCardDetailID(wxPayInfo.getWxVipCardDetailID());
//						wxBonusUsedDetails.setBonus(wxVipUpdate.getBonus()); // TODO 这里暂时使用本地数据的bonus来插入积分明细表
//						wxBonusUsedDetails.setAdd_bonus(wxPayInfo.getAddBonus());
//						DataSourceContextHolder.setDbName(dbName);
//						WxBonusUsedDetails wxBonusUsedDetailsCreate = (WxBonusUsedDetails) wxBonusUsedDetailsBO.createObject(BaseWxBO.SYSTEM, BaseWxBO.INVALID_CASE_ID, wxBonusUsedDetails);
//						if (wxBonusUsedDetailsCreate == null) {
//							logger.error("微信积分使用明细创建失败，WxVipCardDetailID：" + wxPayInfo.getWxVipCardDetailID());
//							break;
//						}
//					}
//				} while (false);
//			}
		} catch (Exception e) {
			logger.info("microPayAsync异常：" + e.getMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器异常");
			e.printStackTrace();
		}
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/refundEx", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String refundEx(@ModelAttribute("SpringWeb") RetailTrade rt, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("退货零售单: " + rt);
		WXPayConfigImpl config = new WXPayConfigImpl(appid, mchid, secret, cert);
		WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, UseSandBox);

		Map<String, Object> params = new HashMap<>();
		Map<String, String> refundData = new HashMap<String, String>();
		String nonce_str = WXPayUtil.generateNonceStr();
		String sign = WXPayUtil.generateSignature(refundData, config.getKey(), WXPayConstants.SignType.MD5);
		String out_refund_no = String.valueOf(System.currentTimeMillis() % 1000000) + nonce_str.substring(12);

		// 检查：拒绝状态为2(包含已删除商品)的零售单
		if (rt.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			logger.info("POS端已经阻止status=2的零售单退款，除非黑客穿透，否则不可能发生这样的请求，所以一律拒绝！！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		// 申请退款
		// 从公司的缓存拿到子商户号
		Company company = getCompanyFromSession(session);
		refundData.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(), company.getSubmchid());
		// refundData.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(),
		// rt.getWxRefundSubMchID());// 子商户号
		refundData.put(WXPayInfo.field.getFIELD_NAME_nonce_str(), nonce_str); // 随机字符串
		refundData.put(WXPayInfo.field.getFIELD_NAME_sign(), sign); // 签名
		refundData.put(WXPayInfo.field.getFIELD_NAME_transaction_id(), rt.getWxOrderSN()); // 微信订单号
		refundData.put(WXPayInfo.field.getFIELD_NAME_out_trade_no(), rt.getWxTradeNO()); // 商户订单号
		refundData.put(WXPayInfo.field.getFIELD_NAME_refund_desc(), rt.getWxRefundDesc() == null ? "" : rt.getWxRefundDesc()); // 退款原因 非必填
		refundData.put(WXPayInfo.field.getFIELD_NAME_out_refund_no(), out_refund_no); // 商户退款单号 自定义
		//
		if (UseSandBox) {
			refundData.put(WXPayInfo.field.getFIELD_NAME_total_fee(), "502"); // 订单金额
			refundData.put(WXPayInfo.field.getFIELD_NAME_refund_fee(), "502"); // 申请退款金额
		} else {
			// 需要对金额进行转换。
			String amount = String.valueOf(rt.getAmount()).substring(0, String.valueOf(rt.getAmount()).indexOf("."));
			refundData.put(WXPayInfo.field.getFIELD_NAME_total_fee(), amount); // 订单金额
			refundData.put(WXPayInfo.field.getFIELD_NAME_refund_fee(), amount); // 申请退款金额
		}

		//
		Map<String, String> resp = wxpay.refund(refundData);
		logger.info("refundData：" + resp);

		Thread.sleep(1000);

		if (resp.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_SUCCESS)) {
			logger.info("退款成功！！！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} else {
			logger.info("退款失败！！！");
			params.put(KEY_HTMLTable_Parameter_msg, resp.get(BaseWxModel.WXPAY_ERR_CODE_DES)); // 微信返回的错误信息
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		}
		params.putAll(resp);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/reverseEx", method = RequestMethod.POST)
	@ResponseBody
	public String reverseEx(@ModelAttribute("SpringWeb") WXPayInfo wpi, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Map<String, Object> params = new HashMap<>();

		WXPayConfigImpl config = new WXPayConfigImpl(appid, mchid, secret, cert);
		WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, UseSandBox);

		Map<String, String> reverseData = new HashMap<String, String>();
		String nonce_str = WXPayUtil.generateNonceStr();
		String sign = WXPayUtil.generateSignature(reverseData, config.getKey(), WXPayConstants.SignType.MD5);

		// transaction_id与out_trade_no二选一，transaction_id优先级较高
		// reverseData.put("transaction_id", "");
		reverseData.put(WXPayInfo.field.getFIELD_NAME_out_trade_no(), System.currentTimeMillis() % 1000000 + nonce_str.substring(12));
		reverseData.put(WXPayInfo.field.getFIELD_NAME_nonce_str(), nonce_str);
		reverseData.put(WXPayInfo.field.getFIELD_NAME_sign(), sign);
		//
		Company company = getCompanyFromSession(session);
		reverseData.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(), company.getSubmchid());

		Map<String, String> resp = wxpay.reverse(reverseData);
		logger.info("reverseData：" + resp);

		if (resp.get(BaseWxModel.WXPay_RETURN).equals(BaseWxModel.WXPay_SUCCESS) && resp.get(BaseWxModel.WXPay_RESULT).equals(BaseWxModel.WXPay_SUCCESS)) {
			logger.info("撤销订单成功！！！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} else {
			logger.info("撤销订单失败！！！！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

//	@RequestMapping(value = "/payViaBonusEx", method = RequestMethod.POST)
//	@ResponseBody
//	public String payViaBonusEx(@ModelAttribute("SpringWeb") WXPayInfo wxPayInfo, ModelMap mm, HttpSession session) {
//		logger.info("微信积分消费 ：" + wxPayInfo);
//
//		Map<String, Object> params = new HashMap<>();
//		// 检查积分是否符合规范
//		if (wxPayInfo.getAddBonus() >= 0) {
//			logger.error("需要扣除的微信积分为非负数!" + wxPayInfo);
//			return "";
//		}
//		//
//		WxCard wxCard = new WxCard();
//		wxCard.setAdd_bonus(wxPayInfo.getAddBonus());
//		wxCard.setCode(wxPayInfo.getWxVipCardCode());
//		wxCard.setCard_id(wxPayInfo.getWxVipCardID());
//		Map<String, Object> paramsForWx = wxCard.getHttpUpdateParam(BaseWxBO.CASE_UpdateVipUser, wxCard);
//		// 向微信发送请求更新会员积分
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(post_card_updateVipUser, JSONObject.fromObject(paramsForWx).toString(), "积分消费", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.error("积分消费时,向微信请求更新会员积分失败：" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "使用微信会员积分消费失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		// 获取到微信端拿到的剩余总积分
//		int bonus = jsonObjectFromWx.getJSONObject(BaseAction.KEY_Object).getInt("result_bonus");
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		do {
//			//TODO 以下三个DB操作可以放在一个SP当中完成，需要使用DB的事务
//			// 更新vip表
//			Vip updateVipToDB = updateVipToDB(dbName, staff.getID(), wxPayInfo.getVipID(), wxPayInfo.getAddBonus());
//			if (updateVipToDB == null) {
//				break;
//			}
//			if (updateVipToDB.getBonus() != bonus) { // TODO 后面需要同步
//				logger.error("vip中的积分和Wx返回的积分不相等，vip的积分：" + updateVipToDB.getBonus() + ", wx的积分：" + bonus);
////				break; //TODO 本地的积分和WX服务器的积分有可能不是同步的
//			}
//			// 更新wxvip表
//			WxVip updateWxVipToDB = updateWxVipToDB(dbName, staff.getID(), wxPayInfo.getWxVipID(), wxPayInfo.getAddBonus());
//			if (updateWxVipToDB == null) {
//				break;
//			}
//			if (updateWxVipToDB.getBonus() != bonus) {// TODO 后面需要同步
//				logger.error("wxVip中的积分和Wx返回的积分不相等，wxVip的积分：" + updateWxVipToDB.getBonus() + ", wx的积分：" + bonus);
////				break;
//			}
//			// 创建wxBonusUsedDetails表
//			WxBonusUsedDetails updateWxBonusUsedDetails = createWxBonusUsedDetailsToDB(dbName, staff.getID(), wxPayInfo.getWxVipCardDetailID(), wxPayInfo.getAddBonus(), updateWxVipToDB.getBonus());
//			if (updateWxBonusUsedDetails == null) {
//				break;
//			}
//		} while (false);
//
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString()); //TODO 以后使用DB的事务后，这里可能需要重构
//		params.put(KEY_HTMLTable_Parameter_msg, "扣除会员积分成功");
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}

//	private WxBonusUsedDetails createWxBonusUsedDetailsToDB(String dbName, int staffID, int wxVipCardDetailID, int reduceBonus, int bonus) {
//		logger.info("积分消费时,创建微信积分明细表，reduceBonus:" + reduceBonus + "; bonus:" + bonus + ";wxVipCardDetailID:" + wxVipCardDetailID);
//
//		WxBonusUsedDetails createWxBonusUsedDetails = new WxBonusUsedDetails();
//		createWxBonusUsedDetails.setWxVipCardDetailID(wxVipCardDetailID);
//		createWxBonusUsedDetails.setBonus(bonus);
//		createWxBonusUsedDetails.setAdd_bonus(reduceBonus); //
//		DataSourceContextHolder.setDbName(dbName);
//		WxBonusUsedDetails wxBonusUsedDetailsCreate = (WxBonusUsedDetails) wxBonusUsedDetailsBO.createObject(BaseWxBO.SYSTEM, BaseWxBO.INVALID_CASE_ID, createWxBonusUsedDetails);
//		if (wxBonusUsedDetailsCreate == null || wxBonusUsedDetailsBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("微信积分使用明细创建失败，WxVipCardDetailID：" + wxVipCardDetailID);
//			return null;
//		}
//		logger.info("积分消费时,创建微信积分明细表成功" + wxBonusUsedDetailsCreate);
//
//		return wxBonusUsedDetailsCreate;
//	}

//	private WxVip updateWxVipToDB(String dbName, int staffID, int wxVipID, int wxVipBonus) {
//		logger.info("积分消费时,更新本地的wxvip的ID为:" + wxVipID);
//
//		WxVip updateWxVipBonus = new WxVip();
//		updateWxVipBonus.setID(wxVipID);
//		updateWxVipBonus.setBonusOnMinusAmount(wxVipBonus);
//		updateWxVipBonus.setIsIncreaseBonus(EnumBoolean.EB_NO.getIndex());
//		DataSourceContextHolder.setDbName(dbName);
//		WxVip wxVip = (WxVip) wxVipBO.updateObject(staffID, BaseWxBO.CASE_WxVip_UpdateBonus, updateWxVipBonus);
//		if (wxVip == null || wxVipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("积分消费时,更新本地WxVip失败：" + wxVipBO.printErrorInfo());
//			return null;
//		}
//		logger.info("积分消费时,更新本地的WxVip成功:" + wxVip);
//
//		return wxVip;
//	}

//	private Vip updateVipToDB(String dbName, int staffID, int vipID, int vipBonus) {
//		logger.info("积分消费时,更新本地的vip的ID为:" + vipID);
//
//		Vip updateVipBonus = new Vip();
//		updateVipBonus.setID(vipID);
//		updateVipBonus.setBonusOnMinusAmount(vipBonus);
//		updateVipBonus.setIsIncreaseBonus(EnumBoolean.EB_NO.getIndex());
//		DataSourceContextHolder.setDbName(dbName);
//		Vip vipFromDB = (Vip) vipBO.updateObject(staffID, BaseWxBO.CASE_Vip_UpdateBonus, updateVipBonus);
//		if (vipFromDB == null || vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("积分消费时,更新本地VIP失败：" + vipBO.printErrorInfo());
//			return null;
//		}
//		logger.info("积分消费时,更新本地的vip成功,vip" + updateVipBonus);
//
//		return vipFromDB;
//	}
}
