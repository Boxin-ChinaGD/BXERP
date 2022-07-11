//package com.bx.erp.action.wx.wxopen;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.CompanyBO;
//import com.bx.erp.action.wx.wxopen.aes.WXBizMsgCrypt;
//import com.bx.erp.cache.CacheManager;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.BaseModel.EnumBoolean;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.wxopen.WxOpenAccessToken;
//import com.bx.erp.util.DatetimeUtil;
//import com.bx.erp.util.WxUtils;
//import com.github.wxpay.sdk.WXPayUtil;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wx3rdPartyAuthorization")
//@Scope("prototype")
//public class Wx3rdPartyAuthorizationAction extends WxOpenAction {
//	private static Log logger = LogFactory.getLog(Wx3rdPartyAuthorizationAction.class);
//	
//	private final static String InfoType_ComponentVerifyTicket = "component_verify_ticket"; //
//	private final static String InfoType_Authorized = "authorized"; //授权成功通知
//	private final static String InfoType_Unauthorized = "unauthorized"; //取消授权通知
//	private final static String InfoType_UpdateAuthorized = "updateauthorized"; //授权更新通知
//	
//	@Resource
//	CompanyBO companyBO;
//	
//	@RequestMapping(value = "/getComponentVerifyTicket", method = { RequestMethod.POST })
//	@ResponseBody
//	public String getComponentVerifyTicket(HttpSession session, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("msg_signature") String msgSignature, @RequestBody String postData) {
//		String ticket = null;
//		String decryptMsgXML = null;
//		try {
//			logger.info("接收微信服务器发来的ticket（每10分钟发送一个component_verify_ticket）");
//			logger.info("接收数据为：\n" + "timestamp:" + timestamp + "\n" + "nonce:" + nonce + "\n" + "msgSignature:" + msgSignature + "\n" + "postData:" + postData.toString());
//
//			// 根据微信发送的ticket进行解密，并将解密后的ticket保存在map中
//			WXBizMsgCrypt pc = new WXBizMsgCrypt(TOKEN, EncodingAesKey, ComponentAppID);
//			decryptMsgXML = pc.decryptMsg(msgSignature, timestamp, nonce, postData.toString(), false);
//			logger.info("decryptMsgXML: " + decryptMsgXML);
//			//
//			Map<String, String> response = WXPayUtil.xmlToMap(decryptMsgXML);
//			//
//			String infoType = response.get("InfoType");
//			switch (infoType) {
//			case InfoType_ComponentVerifyTicket:
//				ticket = response.get("ComponentVerifyTicket");
//				logger.info("componentVerifyTicket : " + ticket);
//				componentVerifyTicketMap.put(ComponentVerifyTicket, ticket);
//				
//				break;
//			case InfoType_Authorized:
//				// TODO 绑定公众号信息到公司表
//				logger.info("授权成功信息：" );
//				
//				break;
//			case InfoType_Unauthorized:
//				String appID = response.get("AppId");
//				logger.info("取消授权公众号AppID：" + appID);
//				logger.info("公众号取消授权，更新session和缓存！！");
//				
//				Company company = getCompanyFromSession(session);
//				logger.info("company: " + company);
//				if (company.getAuthorizerAppid().equals(appID)) {
//					company.setAuthorizerAppid("");
//					company.setAuthorizerRefreshToken("");
//					company.setFuncInfo(0);
//					
//					Company companyUpdated = (Company) companyBO.updateObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company);
//					if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						logger.error("更新公司失败，请手动删除公司授权信息！！");
//					}
//					//
//					CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdated, BaseAction.DBName_Public, BaseBO.SYSTEM);
//					//
//					setCompanyInSession(session, companyUpdated);
//					
//				} else {
//					logger.error("公司授权公众号APPID与当前取消授权的公众号APPID不同！！！");
//				}
//				
//				break;
//			case InfoType_UpdateAuthorized:
//				// TODO
//				logger.info("授权更新信息：" + response);
//				
//				break;
//			default:
//				
//				logger.info("其它信息：" + response);
//				break;
//			}
//			
//		} catch (Exception e) {
//			logger.info("获取component_verify_ticket异常：" + e.getMessage());
//		}
//
//		return "redirect:/home.bx";
//	}
//	
//	/**
//	 * 获取授权页面
//	 * @param request
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getAuthOrizationPage", method = { RequestMethod.GET })
//	public String getAuthOrizationPage(ModelMap model, HttpServletResponse response, HttpSession session) throws IOException {
//		String preAuthCode = getPreAuthCode();
//		if (preAuthCode != null) {
//			String url = String.format(AuthorizationPage_URL, ComponentAppID, preAuthCode, AuthorizationCallBack_URL);
//			model.addAttribute("authOrizationURL", url);
//		} else {
//			logger.info("添加授权页面失败 --> preAuthCode==null");
//		}
//		//从session获取公司信息
//		Company company = getCompanyFromSession(session);
//		//该公司的F_FuncInfo是否为空，返回相应的状态码
//		//已公众号授权
//		if(company.getFuncInfo() != 0) {
//			model.addAttribute(BaseWxModel.WX_ERRCODE, EnumBoolean.EB_Yes.getName());
//		}
//		//未公众号授权
//		else {
//			model.addAttribute(BaseWxModel.WX_ERRCODE, EnumBoolean.EB_NO.getName());
//		}
//		return "wx/wx_authorization";
//	}
//	
//	
//	/**
//	 * 授权后的回调url，微信第三方授权跳转页面，使用授权码换取公众号或小程序的接口调用凭据和授权信息
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/getAuthCode", method = { RequestMethod.GET })
//	public String getAuthCode(@RequestParam(name = "auth_code") String authCode, @RequestParam(name = "expires_in") Integer expiresIn, HttpSession session) {
//		logger.info("授权码：" + authCode);
//
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken();
//		if (wxOpenAccessToken != null) {
//			// 通过授权后的回调URL向微信请求获取授权方信息并将其保存在数据库中
//			String url = String.format(AuthorizationInfo_URL, wxOpenAccessToken.getComponentAccessToken());
//			//
//			JSONObject params = new JSONObject();
//			params.put("component_appid", ComponentAppID);
//			params.put("authorization_code", authCode);
//			//
//			JSONObject response = WxUtils.postToWxServer(url, params.toString());
//			if (response == null) {
//				logger.info("向微信POST请求获取换取授权公众号授权信息出错！！！");
//			} else {
//				logger.info("获取换取授权公众号授权信息response：" + response);
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("获取公众号授权信息成功！！！");
//					//
//					JSONObject jsonObject = response.getJSONObject("authorization_info");
//					String authorizerAccessToken = jsonObject.getString("authorizer_access_token");
//					int expiresIn2 = jsonObject.getInt("expires_in");
//					String authorizerAppid = jsonObject.getString("authorizer_appid");
//					String authorizerRefreshToken = jsonObject.getString("authorizer_refresh_token");
//					//
//					JSONArray jsonArray = jsonObject.getJSONArray("func_info");
//					long funcInfo = getFuncInfo(jsonArray);
//					//
//					// 将首次获取的authorizer_access_token保存在缓存中
//					wxOpenAccessToken.setAuthorizerAccessToken(authorizerAccessToken);
//					wxOpenAccessToken.setExpiresin(expiresIn2);
//					wxOpenAccessToken.setDate2(DatetimeUtil.getDate(new Date(), wxOpenAccessToken.getExpiresin()));// 将authorizerAccessToken过期时间设在date2
//					wxOpenAccessTokenMap.put(OpenAccessToken, wxOpenAccessToken);
//					//
//					// 将授权信息保存在数据库中，并刷新缓存和session中的公司信息
//					Company company = getCompanyFromSession(session);
//					company.setAuthorizerAppid(authorizerAppid);
//					company.setAuthorizerRefreshToken(authorizerRefreshToken);
//					company.setFuncInfo(funcInfo);
//					Company companyUpdated = (Company) companyBO.updateObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company);
//					if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						// ...若此时更新公司表失败，则需要重新授权以获取对应的授权信息
//						logger.error("更新公司失败,请重新授权！！");
//						return "redirect:/home.bx";
//					}
//					//
//					CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdated, BaseAction.DBName_Public, BaseBO.SYSTEM);
//					//
//					setCompanyInSession(session, companyUpdated);
//				} else {
//					logger.info("在回调的URL中获取授权公众号错误！！");
//				}
//			}
//
//		} else {
//			logger.info("在回调的URL中获取授权公众号错误  --> wxOpenAccessToken == null");
//		}
//
//		return "redirect:/home.bx";
//	}
//	
//	/**
//	 * 获取授权方的帐号基本信息
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping(value = "/getAuthorizerInfo", method = { RequestMethod.GET })
//	@ResponseBody
//	public JSONObject getAuthorizerInfo(HttpSession session) {
//		Company company = getCompanyFromSession(session);
//		if (company.getAuthorizerAppid() == null || company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
//			logger.info("当前公司没有授权微信公众号！！");
//			return null;
//		}
//		
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken();
//		if (wxOpenAccessToken != null) {
//			String url = String.format(AuthorizerInfo_URL, wxOpenAccessToken.getComponentAccessToken());
//			
//			JSONObject params = new JSONObject();
//			params.put("component_appid", ComponentAppID);
//			params.put("authorizer_appid", company.getAuthorizerAppid());
//			
//			JSONObject response = WxUtils.postToWxServer(url, params.toString());
//			if (response == null) {
//				logger.info("向微信POST请求获取授权方账号基本信息错误！！！");
//			} else {
//				logger.info("获取授权方的帐号基本信息response：" + response);
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("获取授权方账号基本信息成功！！！");
//				} else {
//					logger.info("获取授权方账号基本信息错误！！！");
//				}
//			}
//			
//			return response;
//			
//		} else {
//			logger.info("获取授权方的帐号基本信息错误 --> wxOpenAccessToken == null");
//			return null;
//		}
//	}
//	
//	
//	/**
//	 * 获取公众号授权信息
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/getAuthorizationInfo", method = { RequestMethod.GET })
//	@ResponseBody
//	public JSONObject getAuthorizationInfo(HttpSession session) {
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken();
//		if (wxOpenAccessToken != null) {
//			// 通过授权后的回调URL向微信请求获取授权方信息
//			String url = String.format(AuthorizationInfo_URL, wxOpenAccessToken.getComponentAccessToken());
//			//
//			JSONObject params = new JSONObject();
//			params.put("component_appid", ComponentAppID);
//			params.put("authorization_code", wxOpenAccessToken);
//			//
//			JSONObject response = WxUtils.postToWxServer(url, params.toString());
//			if (response == null) {
//				logger.info("向微信POST请求获取公众号授权信息错误！！");
//			} else {
//				logger.info("获取公众号授权信息response: " + response);
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("获取授权公众号成功！！！");
//				} else {
//					logger.info("获取公众号授权信息错误！！！");
//				}
//			}
//
//			return response;
//
//		} else {
//			logger.info("获取公众号授权信息错误 --> wxOpenAccessToken == null");
//			return null;
//		}
//	}
//	
//	/**
//	 * 将获取到的权限集数组转为10进制
//	 * @param jsonArray
//	 * @return
//	 */
//	private long getFuncInfo(JSONArray jsonArray ){
//		long[] ls = new long[64];
//		for (int i = 0; i < jsonArray.size(); i++) {
//			int id = jsonArray.getJSONObject(i).getJSONObject("funcscope_category").getInt("id");
//			ls[id - 1] = id;
//		}
//		
//		for (long i : ls) {
//			if (i != 0) {
//				ls[(int) (i - 1)] = 1;
//			} 
//		}
//		
//		StringBuilder stringBuilder = new StringBuilder();
//		for(long i : ls) {
//			stringBuilder.append(String.valueOf(i));
//		}
//		
//		return Long.parseLong(stringBuilder.reverse().toString(),2);
//	}
//	
//	
//	
//	
//	/**
//	 * 获取预授权码 pre_auth_code 有效期10minute
//	 * @return pre_auth_code
//	 */
//	private String getPreAuthCode() {
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken();
//		
//		if(wxOpenAccessToken != null){
//			logger.info("根据component_access_token获取预授权码pre_auto_code");
//			//
//			JSONObject params = new JSONObject();
//			params.put("component_appid", ComponentAppID);
//			//
//			String url =  String.format(PreAuthCode_URL, wxOpenAccessToken.getComponentAccessToken());
//			JSONObject response = WxUtils.postToWxServer(url, params.toString());
//			//
//			if (response == null) {
//				logger.info("向微信请求获取预授权码错误！！");
//				return null;
//			} else {
//				logger.info("获取预授权码response：" + response);
//				//
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("获取预授权码成功！！！ ");
//					return response.getString("pre_auth_code");
//				} else {
//					logger.info("获取预授权码错误！！！");
//					return null;
//				}
//			}
//			
//		} else {
//			logger.info("获取预授权码失败 --> component_access_token==null");
//			return null;
//		}
//	}
//	
//}
