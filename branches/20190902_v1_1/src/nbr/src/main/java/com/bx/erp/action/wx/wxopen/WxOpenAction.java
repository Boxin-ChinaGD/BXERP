//package com.bx.erp.action.wx.wxopen;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.WxAccessToken;
//import com.bx.erp.model.wx.wxopen.WxOpenAccessToken;
//import com.bx.erp.util.DatetimeUtil;
//import com.bx.erp.util.WxUtils;
//
//import net.sf.json.JSONObject;
//
//@Controller
//public class WxOpenAction extends BaseAction{
//	private Log logger = LogFactory.getLog(WxOpenAction.class);
//	
//	protected final static String SUCCESS = "success"; 
//	
//	protected final static String ComponentVerifyTicket = "ComponentVerifyTicket";
//	protected final static String OpenAccessToken = "OpenAccessToken"; //TODO 多个公司共用一个KEY，存在覆盖的情况。后期应将key与公司绑定
//	
//	protected static Map<String, String> componentVerifyTicketMap = new HashMap<String, String>();
//	protected static Map<String, WxOpenAccessToken> wxOpenAccessTokenMap = new HashMap<String, WxOpenAccessToken>(); // 可能需要定义在BaseAciton中 TODO
//	
//	@Value("${third.party.appid}")
//	protected String ComponentAppID;
//	
//	@Value("${third.party.token}")
//	protected String TOKEN;
//	
//	@Value("${third.party.encodingAESKey}")
//	protected String EncodingAesKey;
//	
//	@Value("${third.party.secret}")
//	protected String ComponentAppSecret;
//	
//	@Value("${post.ComponentAccessToken}")
//	protected String ComponentAccessToken_URL;
//	
//	@Value("${post.PreAuthCode}")
//	protected String PreAuthCode_URL;
//	
//	@Value("${post.AuthorizationPage}")
//	protected String AuthorizationPage_URL;
//	
//	@Value("${post.AuthorizationInfo}")
//	protected String AuthorizationInfo_URL;
//	
//	@Value("${post.AuthorizerRefreshToekn}")
//	protected String AuthorizerRefreshToekn_URL;
//	
//	@Value("${post.AuthorizerInfo}")
//	protected String AuthorizerInfo_URL;
//	
//	@Value("${post.AuthorizationCallBack}")
//	protected String AuthorizationCallBack_URL;
//	
//	@Value("${post.message.CustomerService}")
//	protected String Message_CustomerService_URL;
//	
//	@Value("${post.card.uploadimg}")
//	protected String card_uploadimg_url; //上传卡券图片素材
//	
//	@Value("${post.card.create}")
//	protected String card_create_url; //创建卡券
//	
//	@Value("${post.card.paycell}")
//	protected String card_paycell_url; // 设置卡券买单
//	
//	@Value("${post.card.qrcode}")
//	protected String card_qrcode_url; //获取二维码ticket
//	
//	@Value("${post.card.code}")
//	protected String card_code_url; // 查询卡券code码
//	
//	@Value("${post.card.consume}")
//	protected String card_consume_url; // 核销卡券
//	
//	@Value("${post.card.detail}")
//	protected String card_detail_url; //卡券详情
//	
//	@Value("${post.card.batchget}")
//	protected String card_retrievelist_url; //批量查询卡券列表
//	
//	@Value("${post.card.update}")
//	protected String card_update_url; //更改卡券信息接口
//	
//	@Value("${post.card.modifystock}")
//	protected String card_updatestock_url; //修改库存接口
//	
//	@Value("${post.card.delete}")
//	protected String card_delete_url; //删除卡券接口
//	
//	@Value("${post.card.unavailable}")
//	protected String card_unavailable_url; //设置卡券失效接口
//	
//	@Value("${post.card.bizuininfo}")
//	protected String card_summarydata_url; //拉取卡券概况数据接口
//	
//	@Value("${post.card.cardinfo}")
//	protected String card_cardinfo_url; //获取免费券数据接口
//	
////	@Value("${post.BatchPullSubmchs}")
////	protected String batchPullSubmchs_url; // 批量拉取子商户号的信息
////	
////	@Value("${post.PullSubmch}")
////	protected String pullSubmch; // 批量拉取子商户号的信息
//	
//	@Value("${get.accesstoken.url}")
//	protected String GET_ACCESSTOKEN_URL; // 微信公众号获取访问微信端的token接口
//	
//	@Value("${post.card.retrieveStatistics}")
//	protected String card_retrieveStatistics_url; // 拉取卡券概况数据接口
//	
//	/**
//	 * 获取component_access_token 有效期2hour
//	 * @return WxOpenAccessToken
//	 */
//	protected WxOpenAccessToken getWxComponentAccessToken() {
//		String componentVerifyTicket = componentVerifyTicketMap.get(ComponentVerifyTicket);
//		if (componentVerifyTicket == null) {
//			logger.info("获取component_access_token失败  --> component_verify_ticket == null");
//			return null;
//		}
//		
//		// 判断缓存中是否存在componentAccessToken，存在是否过期
//		if (wxOpenAccessTokenMap.containsKey(OpenAccessToken)) {
//			Date expireDatetime = ((WxOpenAccessToken) wxOpenAccessTokenMap.get(OpenAccessToken)).getDate1();
//			if (!DatetimeUtil.isAfterDate(new Date(), expireDatetime, 0)) {//TODO 微信官方建议1h50m刷新一次token
//				logger.info("Component_AccessToken:" + wxOpenAccessTokenMap.get(OpenAccessToken).getComponentAccessToken());
//				return wxOpenAccessTokenMap.get(OpenAccessToken);
//			}
//		}
//		
//		// 若缓存中没有Component_AccessToken或过期则重新请求
//		JSONObject params = new JSONObject();
//		params.put("component_appid", ComponentAppID);
//		params.put("component_appsecret", ComponentAppSecret);
//		params.put("component_verify_ticket", componentVerifyTicket);
//		//
//		JSONObject response = WxUtils.postToWxServer(ComponentAccessToken_URL, params.toString());
//		if (response == null) {
//			logger.info("向微信POST请求获取component_access_token错误！！");
//			 return null;
//		} else {
//			logger.info("component_access_token -> response：" + response);
//			//
//			if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//				WxOpenAccessToken wxOpenAccessToken = new WxOpenAccessToken();
//				wxOpenAccessToken.setComponentAccessToken(response.getString(WxOpenAccessToken.field.getFIELD_NAME_componentAccessToken()));
//				wxOpenAccessToken.setExpiresin(response.getInt(WxOpenAccessToken.field.getFIELD_NAME_expiresin()));
//				wxOpenAccessToken.setDate1(DatetimeUtil.getDate(new Date(), wxOpenAccessToken.getExpiresin()));// 将component_access_token过期时间设在date1
//				//
//				logger.info("wxOpenAccessToken:" + wxOpenAccessToken);
//				wxOpenAccessTokenMap.put(OpenAccessToken, wxOpenAccessToken);
//				
//				return wxOpenAccessToken;
//			} else {
//				logger.info("请求获取component_access_token错误:" + response.getString(BaseWxModel.WX_ERRMSG));
//				return null;
//			}
//		}
//	}
//	
//	/**
//	 * 获取（刷新）授权公众号的接口调用凭据（令牌）
//	 * @param 
//	 * @return
//	 */
//	protected WxOpenAccessToken getWxAuthorizerAccessToken(HttpSession session) {
//		Company company = getCompanyFromSession(session); // TODO ..
////		if (company.getAuthorizerAppid() == null || company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
////			logger.info("当前公司没有授权微信公众号！！");
////			return null;
////		}
//		
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken();
//		if (wxOpenAccessToken == null || wxOpenAccessToken.getComponentAccessToken() == null) {
//			logger.info("获取wxOpenAccessToken或component_access_token失败 --> wxOpenAccessToken == null 或 component_access_token == null ");
//			return null;
//		}
//		
//		// 判断缓存中是否存在AuthorizerAccessToken，存在时是否过期
//		if (wxOpenAccessTokenMap.get(OpenAccessToken).getAuthorizerAccessToken() != null) {
//			Date expireDatetime = wxOpenAccessTokenMap.get(OpenAccessToken).getDate2();
//			if (!DatetimeUtil.isAfterDate(new Date(), expireDatetime, 0)) {
//				logger.info("wxOpenAccessToken:" + wxOpenAccessTokenMap.get(OpenAccessToken));
//				return wxOpenAccessTokenMap.get(OpenAccessToken);
//			}
//		}
//		
//		// 若缓存中没有authorizer_access_token或过期则重新请求获取authorizer_access_token
//		String url = String.format(AuthorizerRefreshToekn_URL, wxOpenAccessToken.getComponentAccessToken());
//		//
//		JSONObject params = new JSONObject();
//		params.put("component_appid", ComponentAppID);
////		params.put("authorizer_appid", company.getAuthorizerAppid());
////		params.put("authorizer_refresh_token", company.getAuthorizerRefreshToken());
//		//
//		JSONObject response = WxUtils.postToWxServer(url, params.toString());
//		if (response == null) {
//			logger.info("向微信POST请求刷新AuthorizerAccessToken错误！！");
//			 return null;
//		} else {
//			logger.info("authorizer_access_token -> response：" + response);
//			//
//			if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//				String authorizerAccessToken = response.getString("authorizer_access_token");
//				int expiresIn = response.getInt("expires_in");
//				
//				wxOpenAccessToken.setAuthorizerAccessToken(authorizerAccessToken);
//				wxOpenAccessToken.setExpiresin(expiresIn);
//				wxOpenAccessToken.setDate2(DatetimeUtil.getDate(new Date(), wxOpenAccessToken.getExpiresin()));//将authorizerAccessToken过期时间设在date2
//				
//				wxOpenAccessTokenMap.put(OpenAccessToken, wxOpenAccessToken);
//				
//				return wxOpenAccessToken;
//			} else {
//				logger.info("刷新authorizer_access_token错误: " + response.getString(BaseWxModel.WX_ERRMSG));
//				return null;
//			}
//		}
//	}
//	
//	/** 使用微信第三方开放平台 向微信服务器POST请求，返回JSONObject */
//	protected JSONObject postToWxServer(String url, String params, String msg, HttpSession session) {
//		Map<String, Object> map = new HashMap<>();
//		logger.info("params：" + params);
//		
//		WxOpenAccessToken wxOpenAccessToken = getWxAuthorizerAccessToken(session);
//		if (wxOpenAccessToken == null || wxOpenAccessToken.getAuthorizerAccessToken() == null) {
//			logger.info("获取authorizer_access_token失败--> authorizer_access_token == null ");
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		}
//		
//		JSONObject response = WxUtils.postToWxServer(String.format(url, wxOpenAccessToken.getAuthorizerAccessToken()),  params);
//		if (response == null) {
//			logger.info("向微信POST请求" + msg + "错误！！");
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		} else {
//			if (response.get(BaseWxModel.WX_ERRCODE) == null || response.getInt(BaseWxModel.WX_ERRCODE) == 0) {
//				logger.info("请求" + msg + "成功！！");
//				map.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//				map.put(KEY_Object, response);
//			} else {
//				logger.info("请求" + msg + "错误：" + response);
//				map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//				map.put(KEY_HTMLTable_Parameter_msg, response);
//			}
//			return JSONObject.fromObject(map);
//		}
//	}
//	
//	/** 使用微信公众号 向微信服务器POST请求，返回JSONObject */
//	protected JSONObject publicAccountPostToWxServer(String url, String params, String msg, String appid, String secret) {
//		Map<String, Object> map = new HashMap<>();
//		logger.info("params：" + params);
//		
//		WxAccessToken wxAccessToken = WxUtils.getAccessTokenFromWxServer(false, appid, secret, GET_ACCESSTOKEN_URL);
//		if (wxAccessToken == null || StringUtils.isEmpty(wxAccessToken.getAccessToken())) {
//			logger.info("微信公众号向微信POST请求" + msg + "错误！！对应的appid为：" + appid );
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		}
//		JSONObject response = WxUtils.postToWxServer(String.format(url, wxAccessToken.getAccessToken()),  params);
//		if (response == null) {
//			logger.info("微信公众号向微信POST请求" + msg + "错误！！对应的appid为：" + appid );
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		} else {
//			if (response.get(BaseWxModel.WX_ERRCODE) == null || response.getInt(BaseWxModel.WX_ERRCODE) == 0) {
//				logger.info("微信公众号请求" + msg + "成功！！");
//				map.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//				map.put(KEY_Object, response);
//			} else {
//				logger.info("请求" + msg + "错误：" + response + ";对应的appid为：" + appid);
//				map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//				map.put(KEY_HTMLTable_Parameter_msg, response);
//			}
//			return JSONObject.fromObject(map);
//		}
//	}
//}
