package com.bx.erp.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.multipart.MultipartFile;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.WxAccessToken;
import com.bx.erp.model.wx.WxTemplateData;
import com.bx.erp.model.wx.wxopen.WxOpenAccessToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class WxUtils {
	private static Log logger = LogFactory.getLog(WxUtils.class);
	/** Key: appID Value: AccessToken */
	protected static ConcurrentMap<String, WxAccessToken> mapAccessToken = new ConcurrentHashMap<String, WxAccessToken>();
	protected final static String SUCCESS = "success";
	protected final static String ComponentVerifyTicket = "ComponentVerifyTicket";
	protected final static String OpenAccessToken = "OpenAccessToken"; // TODO 多个公司共用一个KEY，存在覆盖的情况。后期应将key与公司绑定
	protected static Map<String, String> componentVerifyTicketMap = new HashMap<String, String>();
	protected static Map<String, WxOpenAccessToken> wxOpenAccessTokenMap = new HashMap<String, WxOpenAccessToken>(); // 可能需要定义在BaseAciton中 TODO

	/** 获取微信服务器返回的AccessToken,有效期为两小时,需自己储存。
	 * 在缓存中保存AccessToken的有效期，如果超时，则重新申请一个，并刷新缓存。否则，继续重用上次申请的
	 * 由于测试场使用的公众号正是BX的公众号，测试代码频繁（其实不算太频繁）刷新token，将来可能会令微信服务器禁止这种行为 //... */
	public static synchronized WxAccessToken getAccessTokenFromWxServer(boolean bDeleteOldToken, String appid, String secret, String url) {
		if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(secret)) {
			logger.info("appid or secret is null");
			return null;
		}

		if (bDeleteOldToken) {
			mapAccessToken.clear();
		}

		// 判断缓存中有无AccessToken，如果有，直接返回
		if (mapAccessToken.containsKey(appid)) {
			Date expireDatetime = ((WxAccessToken) mapAccessToken.get(appid)).getDate1();
			if (!DatetimeUtil.isAfterDate(new Date(), expireDatetime, 0)) {
				return mapAccessToken.get(appid);
			}
		}

		// 缓存中无AccessToken或过期
		//
		// 将发送请求的必要信息替换成已注册的开发者的信息
		String newurl = String.format(url, appid, secret);
		// 向微信端发送请求，并返回JSON数据
		JSONObject jsonObject = getDataFromWxServer(newurl);
		if (jsonObject == null) {
			logger.info("获取AccessToken失败！！！"); // getDataFromWxServer()已经logger.error，这里不再需要报警
			return null;
		} else {
			Object wxErrCode = jsonObject.get(BaseWxModel.WX_ERRCODE);
			if (wxErrCode == null) {
				WxAccessToken accessToken = null;
				if (mapAccessToken.containsKey(appid)) {
					accessToken = mapAccessToken.get(appid);
				} else {
					accessToken = new WxAccessToken();
				}
				accessToken.setAccessToken(jsonObject.getString(WxAccessToken.field.getFIELD_NAME_accessToken()));
				accessToken.setExpiresin(jsonObject.getInt(WxAccessToken.field.getFIELD_NAME_expiresin()));
				accessToken.setDate1(DatetimeUtil.getDate(new Date(), accessToken.getExpiresin()));
				mapAccessToken.put(appid, accessToken);

				return accessToken;
			} else {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("请求微信Token发生错误，错误码：" + wxErrCode);
				}
				return null;
			}
		}
	}

	/** 通用函数。 向微信服务器发送Get请求，返回JSON数据。 */
	public static JSONObject getDataFromWxServer(String url) {
		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);// 接收client执行的结果
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				return JSONObject.fromObject(result);
			} else {
				logger.error("向微信服务器发送Get请求发生错误！");
				return null;
			}
		} catch (Exception e) {
			logger.error("向微信服务器发送Get请求发生错误：" + e.getMessage());
			return null;
		}
	}

	/** 向微信服务器发送Post请求，返回JSON数据 */
	public static JSONObject postToWxServer(String url, String params) {// ...
		HttpClient httpClient = HttpClientBuilder.create().build();
		// 创建POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");

		StringEntity entity = new StringEntity(params, "UTF-8");
		httpPost.setEntity(entity);

		try {
			HttpResponse response = httpClient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			logger.info("请求返回:" + state + "(" + url + ")");

			if (state == HttpStatus.SC_OK) {
				HttpEntity reEntity = response.getEntity();
				String jsonString = EntityUtils.toString(reEntity);
				return JSONObject.fromObject(jsonString);
			}
		} catch (Exception ex) {
			logger.error("向微信服务器发送Post请求出错：" + ex.getMessage());
		}
		return null;
	}

	public static JSONObject postFileToWxServer(String url, String params, File file) {
		CloseableHttpClient client = HttpClients.createDefault();
		//
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "text/plain;charset=utf-8");
		//
		FileBody fileBody = new FileBody(file);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart(params, fileBody);
		httpPost.setEntity(builder.build());
		//
		try (CloseableHttpResponse response = client.execute(httpPost)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return JSONObject.fromObject(EntityUtils.toString(entity));
			}
		} catch (Exception e) {
			logger.info("向微信服务器上传文件请求出错：" + e.getMessage());
		}

		return null;
	}

	/** 封装模板的详细信息
	 * 
	 * @param Map<String,
	 *            TemplateData> param
	 * @return */
	public static JSONObject toJsonObject(Map<String, Object> param) {
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			JSONObject keyJson = new JSONObject();
			WxTemplateData templateData = (WxTemplateData) entry.getValue();
			keyJson.put(WxTemplateData.field.getFIELD_NAME_value(), templateData.getValue());
			keyJson.put(WxTemplateData.field.getFIELD_NAME_color(), templateData.getColor());
			jsonObject.put(entry.getKey(), keyJson);
		}
		return jsonObject;
	}

	/** 查询用户是否关注该公众号 */
	public static ErrorInfo.EnumErrorCode subscribeState(String url, String openid, String accessToken) {
		String tmpurl = String.format(url, accessToken, openid);
		JSONObject jsonObject = getDataFromWxServer(tmpurl);
		if (jsonObject == null) {
			return EnumErrorCode.EC_OtherError;
		}
		String errmsg = (String) jsonObject.get(BaseWxModel.WX_ERRMSG);
		if (errmsg == null) {
			Integer subscribe = jsonObject.getInt(BaseWxModel.WX_Subscribe); // -1查询失败，0已关注，1未关注
			if (subscribe == 0) {
				return EnumErrorCode.EC_NoError; // 已关注
			} else {
				return EnumErrorCode.EC_NoSuchData; // 未关注
			}
		} else {
			logger.error(errmsg + "====根据微信openId 获取用户是否订阅失败！！！");
			return EnumErrorCode.EC_OtherError;
		}
	}

	/** 获取模板列表 */
	public static JSONObject getTemplateMsgList(String url, String accessToken) {
		String tmpurl = String.format(url, accessToken);
		JSONObject jsonObject = getDataFromWxServer(tmpurl);
		if (jsonObject == null) {
			logger.info("获取模板列表失败！！");
			return null;
		} else {
			return jsonObject;
		}
	}

	/** 获取模板id
	 * 
	 * @param url
	 * @param id
	 *            行业模板相应的编号如：OPENTM402203820(关注成功通知)
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static JSONObject getWXTemplateMsgId(String url, String id) throws ClientProtocolException, IOException {
		JSONObject jsonObject = postToWxServer(url, id);
		return jsonObject;
	}

	/** 因用户昵称可能存在特殊符号以及表情，将昵称进行编码,
	 * 
	 * @param nickname
	 * @return */
	public static String encodeNickName(String nickname) {
		String result = null;
		try {
			result = Base64.encodeBase64String(nickname.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		return result;
	}

	/** 将编码后的昵称进行解码
	 * 
	 * @param nickname
	 * @return */
	public static String decodeNickName(String nickname) {

		String result = null;
		try {
			result = new String(Base64.decodeBase64(nickname), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/** 将JSONObject转为xml字符串
	 * 
	 * @param jsonString
	 * @return */
	public static String jsonToXML(String jsonString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
	}

	/** xml转为json
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmltoJson(String xml) throws Exception {
		JSONObject jsonObject = new JSONObject();
		Document document = DocumentHelper.parseText(xml);
		// 获取根节点元素对象
		Element root = document.getRootElement();
		iterateNodes(root, jsonObject);

		return jsonObject;
	}

	/** 遍历元素
	 * 
	 * @param node
	 *            元素
	 * @param json
	 *            将元素遍历完成之后放的JSON对象 */
	@SuppressWarnings("unchecked")
	public static void iterateNodes(Element node, JSONObject json) {
		// 1、获取当前元素的名称
		String nodeName = node.getName();
		// 2、判断已遍历的JSON中是否已经有了该元素的名称
		if (json.containsKey(nodeName)) {
			// 3、该元素在同级下有多个
			Object Object = json.get(nodeName);
			JSONArray array = null;
			if (Object instanceof JSONArray) {
				array = (JSONArray) Object;
			} else {
				array = new JSONArray();
				array.add(Object);
			}
			// 4、获取该元素下所有子元素
			List<Element> listElement = node.elements();
			if (listElement.isEmpty()) {
				// 5、该元素无子元素，获取元素的值
				String nodeValue = node.getTextTrim();
				array.add(nodeValue);
				json.put(nodeName, array);
				return;
			}
			// 6、有子元素
			JSONObject newJson = new JSONObject();
			// 7、遍历所有子元素
			for (Element e : listElement) {
				// 递归
				iterateNodes(e, newJson);
			}
			array.add(newJson);
			json.put(nodeName, array);
			return;
		}
		// 该元素同级下第一次遍历
		// 获取该元素下所有子元素
		List<Element> listElement = node.elements();
		if (listElement.isEmpty()) {
			// 该元素无子元素，获取元素的值
			String nodeValue = node.getTextTrim();
			json.put(nodeName, nodeValue);
			return;
		}
		// 有子节点，新建一个JSONObject来存储该节点下子节点的值
		JSONObject object = new JSONObject();
		// 遍历所有一级子节点
		for (Element e : listElement) {
			// 递归
			iterateNodes(e, object);
		}
		json.put(nodeName, object);
	}

	/** MultipartFile转File */
//	public static File multipartFileToFile(MultipartFile multiFile, int fileSize, List<String> fileTypeList, Map<String, Object> map) {
//		File convFile = null;
//		try {
//			if (multiFile.isEmpty()) {
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureEmpty);
//				return null;
//			}
//
//			if (multiFile.getSize() > fileSize) {
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureSize);
//				return null;
//			}
//
//			String fileName = multiFile.getOriginalFilename();
//			String type = fileName.substring(fileName.lastIndexOf(".") + 1);
//
//			if (fileTypeList.contains(type)) {
//				convFile = new File("D:\\" + (System.currentTimeMillis() / 1000) + multiFile.getOriginalFilename());
//				multiFile.transferTo(convFile);
//			} else {
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureType);
//				return null;
//			}
//		} catch (Exception e) {
//			logger.info("MultipartFile转File异常：" + e.getMessage());
//			return null;
//		}
//
//		return convFile;
//	}

	/** 获取component_access_token 有效期2hour
	 * 
	 * @return WxOpenAccessToken */
	protected static WxOpenAccessToken getWxComponentAccessToken(String componentAppID, String componentAppSecret, String componentAccessToken_URL) {
		String componentVerifyTicket = componentVerifyTicketMap.get(ComponentVerifyTicket);
		if (componentVerifyTicket == null) {
			logger.info("获取component_access_token失败  --> component_verify_ticket == null");
			return null;
		}

		// 判断缓存中是否存在componentAccessToken，存在是否过期
		if (wxOpenAccessTokenMap.containsKey(OpenAccessToken)) {
			Date expireDatetime = ((WxOpenAccessToken) wxOpenAccessTokenMap.get(OpenAccessToken)).getDate1();
			if (!DatetimeUtil.isAfterDate(new Date(), expireDatetime, 0)) {// TODO 微信官方建议1h50m刷新一次token
				logger.info("Component_AccessToken:" + wxOpenAccessTokenMap.get(OpenAccessToken).getComponentAccessToken());
				return wxOpenAccessTokenMap.get(OpenAccessToken);
			}
		}

		// 若缓存中没有Component_AccessToken或过期则重新请求
		JSONObject params = new JSONObject();
		params.put("component_appid", componentAppID);
		params.put("component_appsecret", componentAppSecret);
		params.put("component_verify_ticket", componentVerifyTicket);
		//
		JSONObject response = WxUtils.postToWxServer(componentAccessToken_URL, params.toString());
		if (response == null) {
			logger.info("向微信POST请求获取component_access_token错误！！");
			return null;
		} else {
			logger.info("component_access_token -> response：" + response);
			//
			if (response.get(BaseWxModel.WX_ERRCODE) == null) {
				WxOpenAccessToken wxOpenAccessToken = new WxOpenAccessToken();
				wxOpenAccessToken.setComponentAccessToken(response.getString(WxOpenAccessToken.field.getFIELD_NAME_componentAccessToken()));
				wxOpenAccessToken.setExpiresin(response.getInt(WxOpenAccessToken.field.getFIELD_NAME_expiresin()));
				wxOpenAccessToken.setDate1(DatetimeUtil.getDate(new Date(), wxOpenAccessToken.getExpiresin()));// 将component_access_token过期时间设在date1
				//
				logger.info("wxOpenAccessToken:" + wxOpenAccessToken);
				wxOpenAccessTokenMap.put(OpenAccessToken, wxOpenAccessToken);

				return wxOpenAccessToken;
			} else {
				logger.info("请求获取component_access_token错误:" + response.getString(BaseWxModel.WX_ERRMSG));
				return null;
			}
		}
	}

//	/** 获取（刷新）授权公众号的接口调用凭据（令牌）
//	 * 
//	 * @param
//	 * @return */
//	public static WxOpenAccessToken getWxAuthorizerAccessToken(Company company, String componentAppID, String componentAppSecret, String componentAccessToken_URL, String authorizerRefreshToekn_URL) {
//		// Company company = getCompanyFromSession(session);
//		if (company.getAuthorizerAppid() == null || company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
//			logger.info("当前公司没有授权微信公众号！！");
//			return null;
//		}
//
//		WxOpenAccessToken wxOpenAccessToken = getWxComponentAccessToken(componentAppID, componentAppSecret, componentAccessToken_URL);
//		if (wxOpenAccessToken.getComponentAccessToken() == null) {
//			logger.info("获取component_access_token失败 --> component_access_token == null ");
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
//		String url = String.format(authorizerRefreshToekn_URL, wxOpenAccessToken.getComponentAccessToken());
//		//
//		JSONObject params = new JSONObject();
//		params.put("component_appid", componentAppID);
//		params.put("authorizer_appid", company.getAuthorizerAppid());
//		params.put("authorizer_refresh_token", company.getAuthorizerRefreshToken());
//		//
//		JSONObject response = WxUtils.postToWxServer(url, params.toString());
//		if (response == null) {
//			logger.info("向微信POST请求刷新AuthorizerAccessToken错误！！");
//			return null;
//		} else {
//			logger.info("authorizer_access_token -> response：" + response);
//			//
//			if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//				String authorizerAccessToken = response.getString("authorizer_access_token");
//				int expiresIn = response.getInt("expires_in");
//
//				wxOpenAccessToken.setAuthorizerAccessToken(authorizerAccessToken);
//				wxOpenAccessToken.setExpiresin(expiresIn);
//				wxOpenAccessToken.setDate2(DatetimeUtil.getDate(new Date(), wxOpenAccessToken.getExpiresin()));// 将authorizerAccessToken过期时间设在date2
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

//	/** 使用微信第三方开放平台 向微信服务器POST请求，返回JSONObject */
//	public static JSONObject postToWxServer(String url, String params, String msg, Company company, String componentAppID, String componentAppSecret, String componentAccessToken_URL, String authorizerRefreshToekn_URL) {
//		Map<String, Object> map = new HashMap<>();
//		logger.info("params：" + params);
//
//		WxOpenAccessToken wxOpenAccessToken = getWxAuthorizerAccessToken(company, componentAppID, componentAppSecret, componentAccessToken_URL, authorizerRefreshToekn_URL);
//		if (wxOpenAccessToken == null || wxOpenAccessToken.getAuthorizerAccessToken() == null) {
//			logger.info("获取authorizer_access_token失败--> authorizer_access_token == null ");
//			map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		}
//
//		JSONObject response = WxUtils.postToWxServer(String.format(url, wxOpenAccessToken.getAuthorizerAccessToken()), params);
//		if (response == null) {
//			logger.info("向微信POST请求" + msg + "错误！！");
//			map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			return JSONObject.fromObject(map);
//		} else {
//			if (response.get(BaseWxModel.WX_ERRCODE) == null || response.getInt(BaseWxModel.WX_ERRCODE) == 0) {
//				logger.info("请求" + msg + "成功！！");
//				map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//				map.put(BaseAction.KEY_Object, response);
//			} else {
//				logger.info("请求" + msg + "错误：" + response);
//				map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, response);
//			}
//			return JSONObject.fromObject(map);
//		}
//	}

	/** 使用微信公众号 向微信服务器POST请求，返回JSONObject */
	public static JSONObject publicAccountPostToWxServer(String url, String params, String msg, String appid, String secret, String accessTokenUrl) {
		Map<String, Object> map = new HashMap<>();
		logger.info("params：" + params);

		WxAccessToken wxAccessToken = getAccessTokenFromWxServer(false, appid, secret, accessTokenUrl);
		if (wxAccessToken == null || StringUtils.isEmpty(wxAccessToken.getAccessToken())) {
			logger.info("微信公众号向微信POST请求" + msg + "错误！！对应的appid为：" + appid);
			map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			return JSONObject.fromObject(map);
		}
		JSONObject response = WxUtils.postToWxServer(String.format(url, wxAccessToken.getAccessToken()), params);
		if (response == null) {
			logger.info("微信公众号向微信POST请求" + msg + "错误！！对应的appid为：" + appid);
			map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			return JSONObject.fromObject(map);
		} else {
			if (response.get(BaseWxModel.WX_ERRCODE) == null || response.getInt(BaseWxModel.WX_ERRCODE) == 0) {
				logger.info("微信公众号请求" + msg + "成功！！");
				map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
				map.put(BaseAction.KEY_Object, response);
			} else {
				logger.info("请求" + msg + "错误：" + response + ";对应的appid为：" + appid);
				map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, response);
			}
			return JSONObject.fromObject(map);
		}
	}
}
