package com.bx.erp.action.wx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bx.erp.action.AuthenticationAction;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseAuthenticationBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.action.interceptor.StaffLoginInterceptor;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffBelonging;
import com.bx.erp.model.StaffField;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.Role;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.Button;
import com.bx.erp.model.wx.WxAccessToken;
import com.bx.erp.model.wx.WxCustomMenu;
import com.bx.erp.model.wx.WxTemplate;
import com.bx.erp.model.wx.WxTemplateData;
import com.bx.erp.model.wx.WxUser;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.RSAUtils;
import com.bx.erp.util.SHA1Util;
import com.bx.erp.util.WxUtils;
import net.sf.json.JSONObject;

/** 类职责：将老板的微信号和他的公司绑定起来。要点：<br />
 * 1、生成微信公众号本身的菜单“绑定公司”，以让老板可以点击。<br />
 * 2、老板输入公司编号、手机号和密码，验证自己的身份。验证成功后，在T_staff设置老板的微信<br />
 * 3、通过静默授权，得到当前微信使用者即老板的微信号。<br />
 * 4、转发用户在公众号的请求到微信服务器 。<br />
 * 5、维护微信服务器给的Token。<br />
 * 6、用户收到诸如商品滞销消息后，点击消息，响应函数在本类内。<br />
 * 7、请求微信服务器信任当前nbr服务器 */
@DependsOn({ "companyCache" })
@Controller
@RequestMapping("/wx")
@Scope("prototype")
public class WxAction extends AuthenticationAction {
	private static Log logger = LogFactory.getLog(WxAction.class);

	@Resource
	private StaffBO staffBO;

	@Resource
	private RoleBO roleBO;

	@Resource
	private CompanyBO companyBO;

	@Override
	protected BaseAuthenticationBO getBaseAuthenticationBO() {
		return staffBO;
	}

	// 与微信接口配置信息（可以由我们博昕配置）中的Token要一致。从PublicAccount.properties文件中加载。
	@Value("${public.account.token}")
	private String TOKEN;

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	// 微信官方的接口调用地址，从PublicAccount.properties文件中加载。
	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.code.url}")
	private String GET_CODE_URL; // 获取code的接口

	@Value("${get.openid.url}")
	private String GET_OPENID_URL;// 获取openid的接口

	@Value("${get.userinfo.url}")
	private String GET_USERINFO_URL;// 获取用户详细信息的接口

	@Value("${get.user.url}")
	private String GET_USER_URL; // 获取用户基本信息的接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	/** 微信会回调本ACTION，获取用户OpenId(是自己定义的一个url地址，微信服务器会callback到这个地址返回相应的信息) */
	private String callback_url = BaseAction.DOMAIN + "wx/callBack.bx";

	@Value("${wx.templateid.followsuccess}")
	private String WXMSG_TEMPLATEID_FOLLOWSUCCESS;

	@Value("${wx.templateid.bindSuccess}")
	private String WXMSG_TEMPLATEID_BINDSUCCESS;

	/** 之所以放在项目外，是因为部署到了Tomcat后，路径发生变化导致找不到本文件 */
	private final static String PATH_CustomMenuAlreadyCreated = "D:/NbrConfigFile/PublicAccountMenu.data";

	/** 建立微信服务器与NBR服务器的安全信道，让它们建立信任 请求由微信服务器发送到NBR。 */
	@RequestMapping(method = { RequestMethod.GET })
	public void connectWxServerToNBR(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.info("微信服务器开始建立与NBR的可信信道...");

		// 获取WX服务端Get的值
		String signature = request.getParameter(BaseWxModel.WX_SIGNATURE);// 微信加密的签名
		String timestamp = request.getParameter(BaseWxModel.WX_TIMESTAMP);// 时间戳
		String nonce = request.getParameter(BaseWxModel.WX_NONCE);// 随机值
		String echostr = request.getParameter(BaseWxModel.WX_ECHOSTR);// 随机字符串
		if (signature == null || timestamp == null || nonce == null || echostr == null) {
			logger.info("非法的微信公众号服务器请求！");
			return;
		}

		// 1）将token、timestamp、nonce三个参数进行字典序排序
		String[] array = new String[] { TOKEN, timestamp, nonce };
		Arrays.sort(array);

		// 2）将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuffer content = new StringBuffer();
		for (String b : array) {
			content.append(b);
		}
		String encrypted = SHA1Util.SHA1(content.toString());

		// 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		if (encrypted.equals(signature)) {
			out.print(echostr);
			out.flush();
			out.close();
			logger.info("接入微信公众号服务器成功");
		} else {
			logger.info("接入微信公众号服务器失败");
		}

	}

	// wx老板信息绑定跳转
	@RequestMapping(value = "/bind", method = RequestMethod.GET)
	public String bind(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// logger.info(name);
		logger.info("WX绑定信息");
		mm.put("staffField", new StaffField());
		logger.info("发送的数据" + mm);
		return WX_Bind;
	}

	// WX采购订单审核
	@RequestMapping(value = "/purchasingOrderApproval", method = RequestMethod.GET)
	public String purchasingOrderApproval(HttpSession session, ModelMap mm, PurchasingOrder purchasingOrder) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// logger.info(name);
		logger.info("WX采购订单审核");
		mm.put(PurchasingOrder.field.getFIELD_NAME_ID(), purchasingOrder.getID());
		return WX_PurchasingOrderApproval;
	}

	// WX滞销商品
	@RequestMapping(value = "/unsalableCommodity", method = RequestMethod.GET)
	public String unsalableCommodity(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// logger.info(name);
		logger.info("WX滞销商品信息");
		mm.put("categoryID", EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		return WX_UnsalableCommodity;
	}

	/** 响应公众号的关注者向公众号发送的请求 微信服务器转发关注者的请求到NBR
	 * 
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(method = { RequestMethod.POST })
	public void processRequest(@RequestBody(required = false) String body, HttpServletResponse response, HttpServletRequest request) throws CloneNotSupportedException {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		if (body != null) {
			// 1、解析微信发送过来的post请求数据
			JSONObject jsonObject = null;
			try {
				jsonObject = (JSONObject) WxUtils.xmltoJson(body).get(BaseWxModel.WX_XML);
				String MsgType = jsonObject.getString(BaseWxModel.WX_MSGTYPE);
				logger.info("接收到wx的响应事件,响应类型为：" + MsgType);
				// 3、判断微信返回的是否 是事件类型
				if (MsgType.equals(BaseWxModel.WX_EVENT)) {
					String evenType = jsonObject.getString(BaseWxModel.WX_EVENTS);
					// 4、判断是否是关注事件
					switch (evenType) {
					case BaseWxModel.WX_Subscribe: // 事件类型为:关注
//						handleSubscribeEvent(jsonObject);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("解析微信发送过来的post请求数据失败，错误信息：" + e.getMessage());
			}
		} else {
			logger.info("响应公众号的关注者向公众号发送的请求:接收到的POST请求无数据！！！");
		}
	}

	/** 响应用户关注博昕公众号 */
//	protected void handleSubscribeEvent(JSONObject jsonObject) {
//		WxAccessToken token = null;
//		String FromUserName = jsonObject.getString(BaseWxModel.WX_FROMUSERNAME);
//		logger.info(FromUserName);
//		// 2、获取用户信息并保存到DB中
//		if ((token = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL)) == null) {
//			logger.error("无法获取微信token！");
//			return;
//		}
//		String getUserUrl = String.format(GET_USER_URL, token.getAccessToken(), FromUserName);
//		JSONObject jsons = null;
//		try {
//			jsons = WxUtils.getDataFromWxServer(getUserUrl);
//		} catch (Exception e) {
//			logger.error("获取用户详细信息失败:" + e.getMessage());
//			return;
//		}
//		logger.info("用户详细信息数据：" + jsons);
//		// WxUser wxUser = new WxUser();
//		logger.info("=====subscribe:" + jsons.get(BaseWxModel.WX_Subscribe));
//		logger.info("=====关注者：" + jsons.get(WxUser.field.getFIELD_NAME_nickname()));

//		try {
//			token = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		} catch (Exception e1) {
//			logger.error("解析微信发送过来的post请求数据失败,错误信息:" + e1.getMessage());
//		} finally {
//			if (token == null) {
//				return;
//			}
//		}
//		String newurl = String.format(GET_SENDTEMPLATEMSG_URL, token.getAccessToken());
//		Date date = new Date();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT_Default_Chinese);
		// 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
//		Map<String, Object> param = new HashMap<>();
//		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("您好，您已成功关注博昕科技", WxTemplateData.backgroundColor));
//		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(jsons.getString(WxUser.field.getFIELD_NAME_nickname()), WxTemplateData.backgroundColor));
//		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(simpleDateFormat.format(date).toString(), WxTemplateData.backgroundColor));
//		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("博昕科技", WxTemplateData.backgroundColor));
//		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("祝生活愉快！ ​​​", WxTemplateData.backgroundColor));

		// 2、封装详细信息模板
//		JSONObject jsData = WxUtils.toJsonObject(param);
		// 3、拼接json数据格式
//		JSONObject json = new JSONObject();
//		json.put(WxTemplate.field.getFIELD_NAME_touser(), FromUserName);
//		json.put(WxTemplate.field.getFIELD_NAME_template_id(), WXMSG_TEMPLATEID_FOLLOWSUCCESS);
//		json.put(WxTemplate.field.getFIELD_NAME_url(), "#");
//		json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
//		json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());
		// 4、向微信端发送POST请求，然后公众号会显示消息给用户看
//		JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
//		if (result == null) {
//			return;
//		}
		// 5、检查返回的结果
//		int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
//		String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
//		if (errcode == 0) {
//			logger.info("发送成功!!!");
//		} else {
//			logger.info("发送失败!!!错误码：" + errcode + "---错误信息：" + errmsg);
//		}
//	}

	/** 用户请求页面(snsapi_base静默授权，不需要用户点击授权) */
	@RequestMapping(value = "/wxBaseLogin") // ...
	public void toAutoGetAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.info("用户访问服务器！！！静态授权！！！");

		// 检查Cookie值是否存在
		// checkCookie(request, response);// ...可能在这个函数里面已经跳转，下面的代码不需要再跳转
		String backUrl = callback_url; // ...配置文件
		String url = String.format(GET_CODE_URL, PUBLIC_ACCOUNT_APPID, URLEncoder.encode(backUrl, "UTF-8"), "snsapi_base");
		response.sendRedirect(url);
	}

	/** 用户请求页面(snsapi_userinfo页面授权) */
	@RequestMapping(value = "/wxLogin") // ...
	public void toGetAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.info("用户访问服务器！！！网页授权！！！");

		// 检查Cookie值是否存在
		// checkCookie(request, response);// ...可能在这个函数里面已经跳转，下面的代码不需要再跳转
		String backUrl = callback_url;
		String url = String.format(GET_CODE_URL, PUBLIC_ACCOUNT_APPID, URLEncoder.encode(backUrl, "UTF-8"), "snsapi_userinfo");// ...
		// 页面重定向
		response.sendRedirect(url);
	}

	/** 静默授权后，微信会回调本ACTION，获取用户OpenId 参考微信官方文档：XXXXXXX */
	@RequestMapping(value = "/callBack")
	public String getOpenId(HttpServletRequest request, HttpServletResponse response, ModelMap mm) throws ClientProtocolException, IOException, ServletException {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("callBack===接受到微信服务器发来的请求！！！");
		String code = request.getParameter(BaseWxModel.WX_CODE);// 微信会返回code值，用code获取openid

		logger.info("code=========" + code);
		// 1.拼接URL
		String url = String.format(GET_OPENID_URL, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, code); // ...
		// 2.向微信端发送请求，并返回JSON数据
		JSONObject jsonObject = WxUtils.getDataFromWxServer(url);

		if (jsonObject.get(BaseWxModel.WX_ERRMSG) != null) {
			request.setAttribute("msg", "授权失败！！！请重新登录!");
			response.sendRedirect("/WEB-INF/wx/wx_loginFinished.jsp");
		}

		logger.info("微信端返回的数据：" + jsonObject);

		// WxUser wxUser = new WxUser();
		// WxAccessToken accessToken = new WxAccessToken();
		// 3.获取微信端返回的数据
		String openid = jsonObject.getString(WxUser.field.getFIELD_NAME_openid()); // 用户唯一标识
		String token = jsonObject.getString(WxAccessToken.field.getFIELD_NAME_accessToken()); // 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
		String expires_in = request.getParameter(WxAccessToken.field.getFIELD_NAME_accessToken()); // access_token接口调用凭证超时时间，单位（秒）
		String refresh_token = request.getParameter(BaseWxModel.WX_REFRESH_TOKEN); // 用户刷新access_token

		logger.info("===openid====" + openid + "=====access_token====" + token + "===expires_in===" + expires_in + "===refresh_token===" + refresh_token);
		//
		// // 4.拼接URL以便发送请求拉取用户信息
		// String userInfoUrl = String.format(GET_USERINFO_URL, token, openid); // ...
		// // 5.向微信端发送请求拉取用户信息，返回数据为JSON。
		// JSONObject userInfo = WxUtils.getDataFromWxServer(userInfoUrl);
		// logger.info("userInfo==================" + userInfo); //
		//
		// // 6.将拉取的用户信息展示在页面以便查看（前期测试所需，后期可直接将页面跳转在业务页面）
		// request.setAttribute("info", userInfo);

		mm.put(Staff.field.getFIELD_NAME_openid(), openid);
		mm.put("staffField", new StaffField());
		return WX_Bind;
	}

	// /** 微信小程序获取会员信息 url:https://localhost:8888/wx/queryVipInfoEX.bx */
	// @RequestMapping(value = "/queryVipInfoEx", method = { RequestMethod.GET })
	// @ResponseBody
	// public Map<String, Object> queryVipInfoEx(HttpServletRequest request) {
	// Map<String, Object> params = new HashMap<>();
	// WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false,
	// PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// if (accessToken == null) {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取accessToken失败！！！");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// String card_id = request.getParameter("card_id");// ...
	// String code = request.getParameter("code");
	// String unionID = request.getParameter("unionID");
	// // 获取会员信息: 跳转型会员卡类型的会员卡不会返回userInfo，非跳转型会员卡会返回userInfo
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("card_id", card_id);
	// jsonObject.put("code", code);
	// String vipInfoUrl = String.format(vipInfo, accessToken.getAccessToken());
	// JSONObject vipInfo = WxUtils.postToWxServer(vipInfoUrl,
	// jsonObject.toString());
	// if (vipInfo.getInt(BaseWxModel.WX_ERRCODE) != EnumBoolean.EB_NO.getIndex()) {
	// params.put(KEY_HTMLTable_Parameter_msg, "查询会员信息异常！！！");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// //
	// ErrorInfo errorInfo = new ErrorInfo();
	// VipBelongingCache vipBelongingCache = (VipBelongingCache)
	// CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_VipBelonging);
	// VipBelonging vipBelongingVipCard = vipBelongingCache.read1(card_id,
	// errorInfo);
	// if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError ||
	// vipBelongingVipCard == null) {
	// logger.error("查询缓存异常,异常信息:" + errorInfo.getErrorCode().toString() + ",会员卡ID为"
	// + card_id);
	// params.put(KEY_HTMLTable_Parameter_msg, "查询缓存异常！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return params;
	// }
	// // 当前公司的私有DB名称
	// String dbName = vipBelongingVipCard.getDbName();
	// // 查询积分总额
	// WxVip wxVip = new WxVip();
	// wxVip.setUnionID(unionID);
	// DataSourceContextHolder.setDbName(dbName);
	// WxVip wxVipFromDB = (WxVip) wxVipBO.retrieve1Object(BaseBO.SYSTEM,
	// BaseBO.INVALID_CASE_ID, wxVip);
	// if (wxVipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || wxVipFromDB ==
	// null) {
	// logger.error("查询DB会员信息失败：" + wxVipBO.printErrorInfo());
	// params.put(BaseAction.JSON_ERROR_KEY, wxVipBO.getLastErrorCode().toString());
	// params.put(BaseAction.KEY_HTMLTable_Parameter_msg,
	// wxVipBO.getLastErrorMessage());
	// return params;
	// }
	//
	// params.put("vipInfo", vipInfo);
	// params.put("bounsTotal", wxVipFromDB.getBonus());
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return params;
	// }

	// /** 微信小程序获取会员卡下的优惠券
	// * url:https://localhost:8888/wx/queryCouponListBelowVipCardEX.bx */
	// @RequestMapping(value = "/queryCouponListBelowVipCardEx", method = {
	// RequestMethod.GET })
	// @ResponseBody
	// public Map<String, Object> queryCouponListBelowVipCard(HttpServletRequest
	// request) throws Exception {
	// // 获取openid
	// Map<String, Object> params = new HashMap<String, Object>();
	// WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false,
	// PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// if (accessToken == null) {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取accessToken失败!!!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// String openid = request.getParameter("openid");// ...
	// String card_id = request.getParameter("card_id");
	// if (openid.isEmpty()) {
	// logger.error("必填项openid为空,请求异常!");
	// params.put(KEY_HTMLTable_Parameter_msg, "请求异常!!!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// // 根据openid再向wx服务器发送请求，查找到相关的卡券
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("openid", openid);
	// jsonObject.put("card_id", card_id); // card_id：卡券ID。不填写时默认查询当前appid下的卡券。
	// String url = String.format(couponListBelowVipCard,
	// accessToken.getAccessToken());
	// JSONObject cardList = WxUtils.postToWxServer(url, jsonObject.toString());
	// if (cardList.getInt(BaseWxModel.WX_ERRCODE) != EnumBoolean.EB_NO.getIndex())
	// {
	// logger.error("查询卡券信息异常, 错误信息为:" + cardList);
	// params.put(KEY_HTMLTable_Parameter_msg, "查询卡券信息异常！！！");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// params.put("cardList", cardList);
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return params;
	// }

	// /** 查询优惠券列表信息 url:https://localhost:8888/wx/queryCouponListInfoEx.bx */
	// @RequestMapping(value = "/queryCouponListInfoEx", method = {
	// RequestMethod.GET })
	// @ResponseBody
	// public Map<String, Object> queryCouponListInfo(HttpServletRequest request)
	// throws Exception {
	// Map<String, Object> params = new HashMap<String, Object>();
	// WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false,
	// PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// if (accessToken == null) {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取accessToken失败!!!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return params;
	// }
	// String cardList = request.getParameter("cardList");
	// if (StringUtils.isEmpty(cardList)) {
	// params.put(KEY_HTMLTable_Parameter_msg, "您还未领取会员卡和优惠券！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return params;
	// }
	// String vipCardID = request.getParameter("vipCardID");
	// if (StringUtils.isEmpty(vipCardID)) {
	// logger.error("会员卡为空!!!");
	// params.put(KEY_HTMLTable_Parameter_msg, "会员卡为空！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return params;
	// }
	// // 从缓存中查询卡券所归属的公司
	// // TODO 目前只支持从会员卡跳到小程序，所以暂时通过会员卡ID拿到DBName
	// ErrorInfo errorInfo = new ErrorInfo();
	// VipBelongingCache vipBelongingCache = (VipBelongingCache)
	// CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_VipBelonging);
	// VipBelonging vipBelongingVipCard = vipBelongingCache.read1(vipCardID,
	// errorInfo);
	// if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError ||
	// vipBelongingVipCard == null) {
	// logger.error("查询缓存异常,异常信息:" + errorInfo.getErrorCode().toString() + ",会员卡ID为"
	// + vipCardID);
	// params.put(KEY_HTMLTable_Parameter_msg, "查询缓存异常！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return params;
	// }
	// // 当前公司的私有DB名称
	// String dbName = vipBelongingVipCard.getDbName();
	// //
	// JSONObject jSONObject = JSONObject.fromObject(cardList);
	// JSONArray jSONArray = JSONArray.fromObject(jSONObject.getString("cardList"));
	// List<JSONObject> couponList = new ArrayList<>();
	// VipBelonging vipBelongingCoupon = null;
	// for (int i = 0; i < jSONArray.size(); i++) {
	// JSONObject jObject = jSONArray.getJSONObject(i);
	// String cardID = jObject.getString("card_id");
	// // 判断是否为当前公司的会员卡或者优惠券
	// vipBelongingCoupon = vipBelongingCache.read1(cardID, errorInfo);
	// if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError ||
	// vipBelongingVipCard == null) {
	// logger.error("查询缓存异常,异常信息:" + errorInfo.getErrorCode().toString() + ",卡券ID为"
	// + cardID);
	// continue;
	// }
	// // 如果不是当前公司的券，则不做查询。或者是会员卡，则不做查询 //TODO 目前一个用户在一家店只能有一张会员卡
	// if (!dbName.equals(vipBelongingCoupon.getDbName()) ||
	// vipCardID.equals(cardID)) {
	// continue;
	// }
	// // 请求微信服务器得到一张券的信息 //TODO wxCoupon中写一个httpRetrieve1Param为了查询一张优惠券
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("card_id", cardID);
	// jsonObject.put("code", jObject.getString("code"));
	// jsonObject.put("check_consume", false);
	// //
	// String couponListStatusUrl = String.format(CouponListStatus,
	// accessToken.getAccessToken());
	// JSONObject singleCouponInfo = WxUtils.postToWxServer(couponListStatusUrl,
	// jsonObject.toString());
	// //
	// if (!"0".equals(singleCouponInfo.getString("errcode"))) {
	// logger.error("请求微信卡券信息异常！！！" + singleCouponInfo);
	// params.put(KEY_HTMLTable_Parameter_msg, "请求微信卡券信息异常！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return params;
	// }
	// String couponStatus = singleCouponInfo.getString("user_card_status");
	// SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
	// Calendar cal = Calendar.getInstance();
	// cal.setTimeInMillis(singleCouponInfo.getJSONObject("card").getLong("begin_time")
	// * 1000);
	// String beginTime = sdf.format(cal.getTime());
	// cal.setTimeInMillis(singleCouponInfo.getJSONObject("card").getLong("end_time")
	// * 1000);
	// String endTime = sdf.format(cal.getTime());
	// // 通过券ID查询一种券的信息
	// WxCoupon wxCoupon = new WxCoupon();
	// wxCoupon.setCardID(cardID);
	// Map<String, Object> couponRetrieve1Params =
	// wxCoupon.getHttpRetrieve1Param(BaseBO.INVALID_CASE_ID, wxCoupon);
	// //
	// String couponListInfoUrl = String.format(CouponListInfo,
	// accessToken.getAccessToken());
	// JSONObject couponTypeInfo = WxUtils.postToWxServer(couponListInfoUrl,
	// JSONObject.fromObject(couponRetrieve1Params).toString());
	// //
	// WxCoupon wxCouponFromParse = (WxCoupon)
	// wxCoupon.parse1(BaseWxBO.CASE_Parse1FromWx, couponTypeInfo.toString());
	// if (wxCouponFromParse == null) {
	// logger.error("解析异常，当前优惠券不进行操作，优惠券信息为：" + couponTypeInfo);
	// continue;
	// }
	// String couponTitle =
	// wxCouponFromParse.getWxCouponDetail().getWxCouponDetailPartition().getBase_info().getTitle();
	// int leastCost = wxCouponFromParse.getWxCouponDetail().getLeast_cost();
	// int reduceCost = wxCouponFromParse.getWxCouponDetail().getReduce_cost();
	//
	// JSONObject returnJSONObject = new JSONObject();
	// returnJSONObject.put("couponStatus", couponStatus);
	// returnJSONObject.put("couponTitle", couponTitle);
	// returnJSONObject.put("beginTime", beginTime);
	// returnJSONObject.put("endTime", endTime);
	// returnJSONObject.put("leastCost", leastCost);
	// returnJSONObject.put("reduceCost", reduceCost);
	//
	// couponList.add(returnJSONObject);
	// }
	// if (CollectionUtils.isEmpty(couponList)) {
	// params.put(KEY_HTMLTable_Parameter_msg, "您还未领取优惠券！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
	// return params;
	// }
	// // 根据起始时间（begin_time）降序排序
	// Collections.sort(couponList, new Comparator<JSONObject>() {
	// @Override
	// public int compare(JSONObject o1, JSONObject o2) {
	// String beginTime1 = o1.getString("beginTime");
	// String beginTime2 = o2.getString("beginTime");
	// return beginTime2.compareTo(beginTime1);
	// }
	// });
	//
	// params.put("couponList", couponList);
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return params;
	// }

	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/queryBounsChangeInfoEx", method = {
	// RequestMethod.GET })
	// @ResponseBody
	// public Map<String, Object> queryBounsChangeInfoEx(HttpServletRequest request)
	// throws Exception {
	// Map<String, Object> params = new HashMap<String, Object>();
	//
	// String vipCardID = request.getParameter("vipCardID");
	// if (StringUtils.isEmpty(vipCardID)) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
	// params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "会员卡ID为空");
	// return params;
	// }
	// //
	// ErrorInfo errorInfo = new ErrorInfo();
	// VipBelongingCache vipBelongingCache = (VipBelongingCache)
	// CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_VipBelonging);
	// VipBelonging vipBelongingVipCard = vipBelongingCache.read1(vipCardID,
	// errorInfo);
	// if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError ||
	// vipBelongingVipCard == null) {
	// logger.error("查询缓存异常,异常信息:" + errorInfo.getErrorCode().toString() + ",会员卡ID为"
	// + vipCardID);
	// params.put(KEY_HTMLTable_Parameter_msg, "查询缓存异常！！！");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return params;
	// }
	// // 当前公司的私有DB名称
	// String dbName = vipBelongingVipCard.getDbName();
	// //
	// WxVipCardDetail wxVipCardDetail = new WxVipCardDetail();
	// wxVipCardDetail.setCard_id(vipCardID);
	// DataSourceContextHolder.setDbName(dbName);
	// List<WxBonusUsedDetails> bonusUsedDetailsList = (List<WxBonusUsedDetails>)
	// wxBonusUsedDetailsBO.retrieveNObject(BaseBO.SYSTEM,
	// BaseWxBO.CASE_RetrieveNByVipCardID, wxVipCardDetail);
	// if (wxBonusUsedDetailsBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.error("查询积分变动信息失败：" + wxBonusUsedDetailsBO.printErrorInfo());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// wxBonusUsedDetailsBO.getLastErrorCode().toString());
	// params.put(BaseAction.KEY_HTMLTable_Parameter_msg,
	// wxBonusUsedDetailsBO.getLastErrorMessage());
	// return params;
	// }
	// if (!CollectionUtils.isEmpty(bonusUsedDetailsList)) {
	// // 根据时间进行降序排列
	// Collections.sort(bonusUsedDetailsList, new Comparator<WxBonusUsedDetails>() {
	// @Override
	// public int compare(WxBonusUsedDetails wxBonusUsedDetails1, WxBonusUsedDetails
	// wxBonusUsedDetails2) {
	// return
	// wxBonusUsedDetails2.getCreateDatetime().compareTo(wxBonusUsedDetails1.getCreateDatetime());
	// }
	// });
	// }
	//
	// params.put("bonusUsedDetailsList", bonusUsedDetailsList);
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return params;
	// }

	// 以下3个action已弃用
	// // 发送入库审核提醒消息
	// @RequestMapping(value = "/approve", method = RequestMethod.GET)
	// public void sendApprove(HttpServletRequest request, HttpServletResponse
	// response) throws ClientProtocolException, IOException, ServletException {
	// String id = request.getParameter("ID");
	// String int1 = request.getParameter("int1");
	// logger.info("======================发送审核消息！！");
	// logger.info("===id:" + id + "====int1:" + int1);
	// Map<String, Object> params = new HashMap<>();
	// params.put("ID", id);
	// params.put("int1", int1);
	// // sendTemplateMsg("入库审核", " 入库审核", params);
	// }
	//
	// // 跳转到入库审核页面
	// @RequestMapping("/toWarehouse")
	// public void toWarehouse(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// String ID = request.getParameter("ID");
	// String int1 = request.getParameter("int1");
	// System.out.println("===" + ID);
	// System.out.println("===" + int1);
	// Map<String, Object> params = new HashMap<>();
	// params.put("ID", ID);
	// params.put("int1", int1);
	// // JSONObject jsonObject = JSONObject.fromObject(params);
	// request.setAttribute("warehouse", params);
	// request.getRequestDispatcher("/wx_warehouse.jsp").forward(request, response);
	// }
	//
	// // 处理审核响应
	// @RequestMapping(value = "/getWarehouse")
	// public void getWarehouse(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// System.out.println("===" + request.getParameter("ID"));
	//
	// Map<String, Object> params = new HashMap<>();
	// params.put("int1", request.getAttribute("int1"));
	// params.put("ID", request.getAttribute("ID"));
	// request.setAttribute("warehouse", params);
	// request.getRequestDispatcher("/wx_warehouse.jsp").forward(request, response);
	// }

	@RequestMapping(value = "/getTokenEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String getTokenEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		logger.info("获取秘钥，staff=" + staff);

		logger.info("staffBO hash1 = " + staffBO.hashCode());
		RSAInfo rsa = generateRSA(staff.getPhone());

		session.setAttribute(EnumSession.SESSION_StaffPhone.getName(), staff.getPhone());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rsa", rsa);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/** 绑定老板的微信号到这个公司，以接收公众号消息，如采购订单待审核消息 */
	@RequestMapping(value = "/userBind", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String userBind(@ModelAttribute("SpringWeb") Staff staff, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		logger.info("用户正在进行绑定操作....");
		//
		Map<String, Object> params = new HashMap<String, Object>();
		logger.info("staff==:" + staff + "***" + staff.getOpenid());
		if (StringUtils.isEmpty(staff.getPhone()) || StringUtils.isEmpty(staff.getOpenid()) || StringUtils.isEmpty(staff.getCompanySN()) || StringUtils.isEmpty(staff.getPwdEncrypted())) {
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
			params.put(KEY_HTMLTable_Parameter_msg, "用户绑定失败:必填项为空！");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		Object phone = session.getAttribute(EnumSession.SESSION_StaffPhone.getName());
		if (phone == null) {
			logger.info("没有电话号码");
			return null; // Hacking
		} else {
			logger.debug("得到的phone=" + phone);
		}

		// 获取到相应的公司
		Company company = getCompanyFromCompanyCacheBySN(staff.getCompanySN());
		if (company == null) {
			params.put(KEY_HTMLTable_Parameter_msg, "用户绑定失败:公司不存在,请输入正确的公司编号！");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		logger.info("当前使用的DBName为：" + company.getDbName());

		Staff staffInOut = new Staff();
		staffInOut.setPhone(staff.getPhone());
		staffInOut.setInvolvedResigned(staff.getInvolvedResigned());
		ErrorInfo ec = new ErrorInfo();
		staffInOut = (Staff) authenticate(company.getDbName(), BaseBO.CASE_Login, staffInOut, staff.getPwdEncrypted(), ec);
		if (staffInOut == null) {
			params.put(JSON_ERROR_KEY, ec.getErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "绑定失败,手机号码或密码错误!");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		//
		if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
			staffInOut.clearSensitiveInfo();
			staffInOut.setOpenid(staff.getOpenid());
			logger.info("待更新的Staff:" + staffInOut);
			DataSourceContextHolder.setDbName(company.getDbName());
			Staff updatedStaff = (Staff) staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, staffInOut);

			logger.info("更新后的Staff:" + updatedStaff);
			if (staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
				// // 设置Cookie
				// Cookie phoneCookie = new Cookie(Staff.field.getFIELD_NAME_phone(),
				// staff.getPhone());
				// Cookie passwordCookie = new Cookie(Staff.field.getFIELD_NAME_salt(),
				// request.getParameter(Staff.field.getFIELD_NAME_salt()));
				//
				// logger.info("获取到的登录密码：===" +
				// request.getParameter(Staff.field.getFIELD_NAME_salt()));
				// // Cookie的有效时长
				// phoneCookie.setMaxAge(60 * 60 * 24 * 3); // ...Cookie的时间期限为：三天
				// passwordCookie.setMaxAge(60 * 60 * 24 * 3);
				//
				// response.addCookie(phoneCookie);
				// response.addCookie(passwordCookie);
				// logger.info("添加Cookie!");

				// Cookie[] cookies = request.getCookies();
				// for (Cookie cookie : cookies) {
				// String name = cookie.getName();
				// if (name.equals(Staff.field.getFIELD_NAME_phone())) { // ...
				// cookie.setMaxAge(0);
				// response.addCookie(cookie);
				// }
				// if (name.equals(Staff.field.getFIELD_NAME_salt())) {
				// cookie.setMaxAge(0);
				// response.addCookie(cookie);
				// }
				// }
				// logger.info("清除Cookies!!!");
				params.put(JSON_ERROR_KEY, staffBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, "用户绑定成功");
				// 绑定成功后，删除旧的cookie,防止继续登陆旧的公司
				Cookie phoneCookie = new Cookie(Staff.field.getFIELD_NAME_phone(), null);// cookie名字要相同
				phoneCookie.setMaxAge(0); //
				phoneCookie.setPath(request.getContextPath()); // 相同路径
				response.addCookie(phoneCookie);
				//
				Cookie passwordCookie = new Cookie(Staff.field.getFIELD_NAME_salt(), null);// cookie名字要相同
				passwordCookie.setMaxAge(0); //
				passwordCookie.setPath(request.getContextPath()); // 相同路径
				response.addCookie(passwordCookie);
				//
				Cookie companySnCookie = new Cookie(Staff.field.getFIELD_NAME_companySN(), null);// cookie名字要相同
				companySnCookie.setMaxAge(0); //
				companySnCookie.setPath(request.getContextPath()); // 相同路径
				response.addCookie(companySnCookie);
				// ... 发送绑定成功的模板消息
				Staff staffRN = new Staff();
				staffRN.setQueryKeyword(updatedStaff.getPhone());
				DataSourceContextHolder.setDbName(company.getDbName());
				List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staffRN);
				if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError || CollectionUtils.isEmpty(staffList)) {
					logger.error("获取员工信息为空或者失败：" + staffBO.printErrorInfo() + "!当前操作用户为：" + staff.getName());
				} else {
					Map<String, Object> templateMap = new HashMap<>();
					templateMap.put(WxUser.field.getFIELD_NAME_openid(), staff.getOpenid());
					templateMap.put(WxTemplate.field.getFIELD_NAME_template_id(), WXMSG_TEMPLATEID_BINDSUCCESS);
					templateMap.put(WxTemplate.field.getFIELD_NAME_url(), "#");
					SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
					String date = sdf.format(new Date());
					sendTemplateMsg(WxTemplate.Bind_FirstData, company.getName(), staffList.get(0).getRoleName(), date, WxTemplate.Bind_Remark, templateMap);
				}
				// 2、更新普通缓存
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).write1(updatedStaff, company.getDbName(), BaseBO.SYSTEM); // 手动登录NBR，查看OPENID。检查点/结果验证
				// 3、更新openID-Staff映射的缓存
				StaffBelonging staffBelonging = new StaffBelonging();
				staffBelonging.setID(updatedStaff.getID());
				staffBelonging.setOpenId(updatedStaff.getOpenid());
				staffBelonging.setDbName(company.getDbName());
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffBelonging).write1(staffBelonging, company.getDbName(), BaseBO.SYSTEM); // 登录CMS，去检查缓存是否更新了
			} else {
				params.put(JSON_ERROR_KEY, staffBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				// Cookie[] cookies = request.getCookies();
				// for (Cookie cookie : cookies) {
				// String name = cookie.getName();
				// if (name.equals(Staff.field.getFIELD_NAME_phone())) { // ...
				// cookie.setMaxAge(0);
				// response.addCookie(cookie);
				// }
				// if (name.equals(Staff.field.getFIELD_NAME_salt())) {
				// cookie.setMaxAge(0);
				// response.addCookie(cookie);
				// }
				// }
				// logger.info("清除Cookies!!!");
			}
		} else {
			params.put(JSON_ERROR_KEY, staffBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "用户绑定失败!");
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/getPageBusiness", method = RequestMethod.GET)
	public void loginAfter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("Msg", request.getSession().getAttribute("Msg"));
		params.put("Warehousing", request.getSession().getAttribute("Warehousing"));
		request.setAttribute("Info", params);
		logger.info("Msg:" + request.getSession().getAttribute("Msg"));
		logger.info("Warehousing:" + request.getSession().getAttribute("Warehousing"));
		request.getRequestDispatcher("/WEB-INF/wx/wx_loginFinished.jsp").forward(request, response);

	}

	/** 发送模板消息
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	// ...该方法需重构，以便适应各种场景
	public Integer sendTemplateMsg(String first, String keyword1, String keyword2, String keyword3, String remark, Map<String, Object> params) throws ClientProtocolException, IOException {

		// ...
		WxAccessToken token = null;
		if ((token = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL)) == null) {
			return null;
		}

		String newurl = String.format(GET_SENDTEMPLATEMSG_URL, token.getAccessToken());

		// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
		Map<String, Object> templateParam = new HashMap<>();
		templateParam.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData(first, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(keyword1, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(keyword2, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(keyword3, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData(remark, WxTemplateData.backgroundColor));

		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(templateParam);
		// 3、拼接json数据格式
		JSONObject json = new JSONObject();
		json.put(WxTemplate.field.getFIELD_NAME_touser(), params.get(WxUser.field.getFIELD_NAME_openid()));// openid
		json.put(WxTemplate.field.getFIELD_NAME_template_id(), params.get(WxTemplate.field.getFIELD_NAME_template_id()));// 模板id
		json.put(WxTemplate.field.getFIELD_NAME_url(), params.get(WxTemplate.field.getFIELD_NAME_url()));// 跳转页面
		json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
		json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
		// 4、向微信端发送POST请求
		JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
		// 5、检查返回的结果
		int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
		String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
		if (errcode == BaseWxModel.WX_ERRCODE_Success) {
			logger.info("发送成功!!!");
			return errcode;
		} else {
			logger.error("发送失败!!!错误码：" + errcode + "---错误信息：" + errmsg);
			return errcode;
		}
	}

	// 入库价格变动消息
	public Map<String, Object> sendWarehousingMsg(Warehousing warehousing, String msg, String openid, String templateID) throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			Map<String, Object> param = new HashMap<>();
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你好，该单据入库价与采购价不符：", WxTemplateData.backgroundColor));
			// param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new
			// WxTemplateData("采购单号:" + inventorySheet.getSn(),
			// WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("入库单号:" + warehousing.getSn(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(new Date().toString(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("操作人:", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("请前往“博销宝管理后台”查看具体信息", WxTemplateData.backgroundColor));

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(GET_SENDTEMPLATEMSG_URL, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + msg);
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	// 采购超时消息
	public static Map<String, Object> sendPurchasingOrderOverTimeMsg(String appID, String secret, String accessTokenUrl, String templateUrl, PurchasingOrderCommodityBO purchasingOrderCommodityBO, int days, Company company,
			PurchasingOrder purchasingOrder, String msg, String openid, String templateID) throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			String dbName = company.getDbName();
			String purchasingOrderSn = purchasingOrder.getSn();
			PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
			poc.setPurchasingOrderID(purchasingOrder.getID());
			poc.setPageIndex(PAGE_StartIndex);
			poc.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> listTmp = purchasingOrderCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, poc);
			if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取多个采购订单商品失败，错误码=" + purchasingOrderCommodityBO.getLastErrorCode());
				params.put(BaseWxModel.WX_ERRCODE, purchasingOrderCommodityBO.getLastErrorCode());
				params.put(BaseWxModel.WX_ERRMSG, purchasingOrderCommodityBO.getLastErrorMessage());
				return params;
			} else {
				logger.info("读取多个采购订单商品成功，listTmp=" + listTmp.toString());
			}
			int commNOs = 0;
			double commPrices = 0.000000d;
			for (Object o : listTmp) {
				PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) o;
				commNOs += pOrderComm.getCommodityNO();
				commPrices = GeneralUtil.sum(commPrices, pOrderComm.getPriceSuggestion());
			}
			Map<String, Object> param = new HashMap<>();
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】" + purchasingOrderSn + "采购超时", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(company.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(commNOs + "", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(GeneralUtil.formatToShow(commPrices), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("本次采购订单已超时未处理，请及时处理，可登录博销宝管理后台进行处理。", WxTemplateData.backgroundColor)); // ...

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面 TODO 这里是否需要提供一个URL？
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + msg);
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	// ...检查Cookie是否存在
	protected void checkCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("CheckCookie中的ID：" + request.getParameter("ID"));

		Cookie[] cookies = request.getCookies();
		Map<String, Object> params = new HashMap<>();
		logger.info("获取到的Cookie:" + cookies);
		String phone = null;
		String salt = null;
		// Staff s = new Staff();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				logger.info("Cookie获取name:" + name);
				if (name.equals(Staff.field.getFIELD_NAME_phone())) {
					phone = cookie.getValue();
				}
				if (name.equals(Staff.field.getFIELD_NAME_phone())) {
					salt = cookie.getValue();
				}
			}
		}

		if (phone != null && salt != null) {
			// ...手机号码，密码的验证登录
			Staff staff = new Staff();
			staff.setPhone(phone);
			staff.setSalt(salt);
			RSAInfo rsaInfo = generateRSA(staff.getPhone());
			logger.info("getToken的获取的值：" + rsaInfo);
			String modulus = new BigInteger(rsaInfo.getModulus(), 16).toString();
			String exponent = new BigInteger(rsaInfo.getExponent(), 16).toString();

			RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
			String pwdEncrypted = RSAUtils.encryptByPublicKey(salt, publicKey);
			staff.setPwdEncrypted(pwdEncrypted);

			Staff staffInOut = new Staff();
			staffInOut.setPhone(staff.getPhone());
			staffInOut.setRoleID(staff.getRoleID());
			ErrorInfo ec = new ErrorInfo();
			// ...根据传递来的公司编号找到DB名称
			Company company = getCompanyFromCompanyCacheBySN(staff.getCompanySN()); // ...这里似乎不可以拿到公司编号，或许可以通过会话拿到。需要与Giggs讨论
			staffInOut = (Staff) authenticate(company.getDbName(), BaseBO.CASE_Login, staffInOut, staff.getPwdEncrypted(), ec);

			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				logger.info("STAFF登录成功!");
				params.put("msg", "Cookie值存在且验证成功，进行自动登录！！！");
				if (request.getParameter("ID") != null) {
					request.setAttribute("ID", request.getParameter("ID"));
				}
				request.setAttribute("Msg", params);
				request.getRequestDispatcher("/WEB-INF/wx/wx_loginFinished.jsp").forward(request, response);
			} else {
				logger.info("用户登录失败，错误码=" + ec.getErrorCode());
				params.put("msg", "用户登录失败，错误码=" + ec.getErrorCode());
				request.setAttribute("Msg", params);
				request.getRequestDispatcher("/WEB-INF/wx/wx_loginFinished.jsp").forward(request, response);
			}
		}
	}

	public static Map<String, Object> sendLoginToWorkMsg(String appID, String secret, String accessTokenUrl, String templateUrl, Staff staff, String openid, String templateID) throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// // ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			// Map<String, Object> param = new HashMap<>();
			// param.put(WxTemplateData.field.getFIELD_NAME_first(), new
			// WxTemplateData("业务消息", WxTemplateData.backgroundColor));
			// param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new
			// WxTemplateData(msg, WxTemplateData.backgroundColor));
			// param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new
			// WxTemplateData(new Date().toString(), WxTemplateData.backgroundColor));
			// param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new
			// WxTemplateData("博昕IT", WxTemplateData.backgroundColor));
			// param.put(WxTemplateData.field.getFIELD_NAME_remark(), new
			// WxTemplateData("祝生活愉快！", WxTemplateData.backgroundColor));

			Map<String, Object> param = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_Default_Chinese);
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("登录上班成功，登录消息如下:", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(staff.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("******", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(sdf.format(new Date()), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("如非本人操作，请前往博销宝商家后台网站修改密码", WxTemplateData.backgroundColor));

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + "登录上班成功消息");
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	public static Map<String, Object> sendLogoutToWorkMsg(String appID, String secret, String accessTokenUrl, String templateUrl, RetailTradeAggregation retailTradeAggregation, Staff staff, String openid, String templateID, Company company)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}
			//
			Map<String, Object> param = new HashMap<>();
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】今天你的店面已交班", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(company.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(GeneralUtil.formatToShow(retailTradeAggregation.getAmount()) + "元", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(retailTradeAggregation.getTradeNO() + "单", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("请与   " + staff.getName() + "  核对交班情况。", WxTemplateData.backgroundColor));
			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + "登录上班成功消息");
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	public static Map<String, Object> sendApproveWarehousingMsg(String appID, String secret, String accessTokenUrl, String templateUrl, Staff staff, Company company, String openid, String templateID)
			throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			Map<String, Object> param = new HashMap<>();
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】入库价与采购价不符", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(company.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(staff.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("入库价与采购价不符", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("详情请登录博销宝管理后台查看", WxTemplateData.backgroundColor)); // ...

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + "入库价与采购价不符");
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	public static Map<String, Object> sendApproveInventorySheetMsg(String appID, String secret, String accessTokenUrl, String templateUrl, Staff staff, Company company, String openid, String templateID)
			throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			Map<String, Object> param = new HashMap<>();
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】盘点数量与系统库存不符", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(company.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(staff.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("盘点数量与系统数量不符", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("详情请登录博销宝管理后台查看 ", WxTemplateData.backgroundColor));

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + "盘点数量与系统数量不符");
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	// 判断微信是否已经登录,判断cookie是否有登录信息，有的话直接登录，然后跳转到相应页面，没有的话跳转到登录页面
	@RequestMapping(value = "/isLogin", method = RequestMethod.GET, produces = "plain/text; charset=UTF-8")
	public String isLogin(HttpServletRequest request, HttpServletResponse response, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		// 前端需要用到ID，ID不能为null, 给个默认值0
		mm.put(PurchasingOrder.field.getFIELD_NAME_ID(), 0);
		logger.info("获取到的URL类型：" + request.getParameter("identify"));
		String identify = request.getParameter("identify");
		String ID = null;
		String categoryID = null;
		if (identify.equals("purchasingOrder")) {
			logger.info("purchasingOrder的ID" + request.getParameter("ID"));
			ID = request.getParameter("ID");
		} else if (identify.equals("unSalableCommodity")) {
			logger.info("unSalableCommodity的categoryID" + request.getParameter("categoryID"));
			categoryID = request.getParameter("categoryID");
		}
		Cookie[] cookies = request.getCookies();
		Map<String, Object> params = new HashMap<>();
		logger.info("获取到的Cookie:" + cookies);
		String phone = null;
		String salt = null;
		String companySN = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				logger.info("Cookie获取name:" + name);
				if (name.equals(Staff.field.getFIELD_NAME_phone())) {
					phone = cookie.getValue();
					logger.info("Cookie获取name的Value值:" + cookie.getValue());
				}
				if (name.equals(Staff.field.getFIELD_NAME_salt())) {
					salt = cookie.getValue();
					logger.info("Cookie获取name的Value值:" + cookie.getValue());
				}
				if (name.equals(Staff.field.getFIELD_NAME_companySN())) {
					companySN = cookie.getValue();
					logger.info("Cookie获取name的Value值:" + cookie.getValue());
				}
			}
		}
		if (phone != null && salt != null && companySN != null) {
			// ...手机号码，密码的验证登录
			Staff staff = new Staff();
			staff.setPhone(phone);
			staff.setSalt(salt);
			staff.setCompanySN(companySN);
			RSAInfo rsaInfo = generateRSA(staff.getPhone());
			logger.info("getToken的获取的值：" + rsaInfo);
			String modulus = new BigInteger(rsaInfo.getModulus(), 16).toString();
			String exponent = new BigInteger(rsaInfo.getExponent(), 16).toString();

			RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
			String pwdEncrypted = RSAUtils.encryptByPublicKey(salt, publicKey);
			staff.setPwdEncrypted(pwdEncrypted);

			Staff staffInOut = new Staff();
			staffInOut.setPhone(staff.getPhone());
			staffInOut.setRoleID(staff.getRoleID());
			ErrorInfo ec = new ErrorInfo();
			// ...根据传递来的公司编号找到DB名称
			Company company = getCompanyFromCompanyCacheBySN(staff.getCompanySN()); // ...这里似乎不可以拿到公司编号，或许可以通过会话拿到。需要与Giggs讨论
			if (company == null) {
				logger.info("公司不存在：即将跳转到登录页面重新登录...");
				mm.put("Staff", new StaffField());
				return WX_Login;
			}
			setCompanyInSession(session, company); // 将dbName保存在会话中，后面用户的ACTION请求中将可以从会话得到DB名称，从而正确地读写DB

			staffInOut = (Staff) authenticate(company.getDbName(), BaseBO.CASE_Login, staffInOut, staff.getPwdEncrypted(), ec);
			if (staffInOut != null) {

				StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission);
				ErrorInfo ecOut = new ErrorInfo();
				StaffPermission sp = spc.read1(staffInOut.getID(), StaffBO.SP_Staff_Retrieve1, ecOut);
				if (ecOut.getErrorCode() == EnumErrorCode.EC_NoError && sp != null) {
					staffInOut.setRoleID(sp.getRoleID());
					logger.info("已经获取staff对应的roleID将其设置在int1：" + staffInOut);
				}
			}
			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				session.removeAttribute(EnumSession.SESSION_BxStaff.toString());// 普通staff与bxstaff互斥,只能同时一个登陆
				staffInOut.setIsLoginFromPos(staff.getIsLoginFromPos());// ...WX发送消息测试，将int3标识为POS机登录
				session.setAttribute(EnumSession.SESSION_Staff.getName(), staffInOut);
				logger.info("STAFF登录成功。设置进会话：" + staffInOut);
				//
				Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
				if (pos == null) {
					pos = new Pos();
					pos.setID(BaseAction.INVALID_POS_ID);
					session.setAttribute(EnumSession.SESSION_POS.getName(), pos);
				}
				StaffLoginInterceptor.loginGuard.login(staff.getCompanySN(), staffInOut.getID(), pos.getID(), session);
				//
				Staff staffReturn = (Staff) staffInOut.clone();
				staffReturn.setSalt("");
				params.put(BaseAction.KEY_Object, staffReturn);
				params.put(BaseAction.KEY_Object2, company); // pos需要用到公司的子商户号
				if (staffInOut.getIsFirstTimeLogin() != EnumBoolean.EB_Yes.getIndex()) {
					session.removeAttribute(EnumSession.SESSION_StaffPhone.getName());
				}

				// 当登录身份为员工,经理,副店长时，发送登录消息给门店老板
				// if (Staff.DEFINE_GET_RoleID(staffInOut.getInt1()) ==
				// Role.EnumTypeRole.ETR_Staff.getIndex() //
				// || Staff.DEFINE_GET_RoleID(staffInOut.getInt1()) ==
				// Role.EnumTypeRole.ETR_Manager.getIndex() //
				// || Staff.DEFINE_GET_RoleID(staffInOut.getInt1()) ==
				// Role.EnumTypeRole.ETR_DeputyShopowner.getIndex()) {//
				// sendLoginMsgToWx(staffInOut, company.getDbName());
				// }
				// 所有人在pos端登录都要发送微信消息，在nbr端登录不需要发送微信消息
				// Pos pos2 = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
				// if (pos2 != null && pos2.getID() != BaseAction.INVALID_POS_ID) {
				// sendLoginMsgToWx(staffInOut, company.getDbName());
				// }
				//
				logger.info("staff对应的roleID,staffInOut.getInt1()=" + staffInOut.getRoleID());

				if ((staffInOut.getIsLoginFromPos() == Role.EnumTypeRole.ETR_Cashier.getIndex() && staffInOut.getIsFirstTimeLogin() == EnumBoolean.EB_Yes.getIndex())) {
					ec.setErrorCode(EnumErrorCode.EC_Redirect);
					ec.setErrorMessage("收银员通过POS机进行首次登录，需要进行重置密码！");
				}
				logger.info("STAFF登录成功!");
				params.put("msg", "Cookie值存在且验证成功，进行自动登录！！！");
				if (request.getParameter("ID") != null) {
					request.setAttribute("ID", request.getParameter("ID"));
				}
				request.setAttribute("Msg", params);
				if (ID != null && identify.equals("purchasingOrder")) {
					mm.put("ID", ID);
					logger.info("即将跳转到采购审核页面...");
					return WX_PurchasingOrderApproval;

				} else if (identify.equals("unSalableCommodity")) {
					mm.put("categoryID", categoryID);
					logger.info("即将跳转到滞销页面...");
					return WX_UnsalableCommodity;
				} else {
					logger.info("即将跳转到登录页面...");
					mm.put("Staff", new StaffField());
					return WX_Login;
				}
			} else {
				logger.info("用户登录失败，错误码=" + ec.getErrorCode());
				params.put("msg", "用户登录失败，错误码=" + ec.getErrorCode());
				request.setAttribute("Msg", params);
				mm.put("Staff", new StaffField());
				mm.put("identify", identify);
				if (ID != null) {
					mm.put("ID", ID);
				}
				return WX_Login;
			}
		}
		mm.put("Staff", new StaffField());
		mm.put("identify", identify);
		if (ID != null) {
			mm.put("ID", ID);
		}
		return WX_Login;
	}

	public static Map<String, Object> sendCreatePurchasingOrderMsg(String appID, String secret, String accessTokenUrl, String templateUrl, Staff staff, Company company, PurchasingOrder purchasingOrder, String msg, String openid,
			String templateID) throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}
			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			Map<String, Object> param = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_Default_Chinese);
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你好，你有新的采购订单需要审核", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(staff.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(company.getName(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(sdf.format(new Date()), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword4(), new WxTemplateData("采购审核", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("请及时审批！", WxTemplateData.backgroundColor));

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), BaseAction.DOMAIN + "wx/isLogin.bx?identify=purchasingOrder&ID=" + purchasingOrder.getID());// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + msg);
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	// 处理微信登录后的接口
	// 需要传的参数：phone,密码,公司编号(string1),角色(int1),ID(采购ID),identify
	@RequestMapping(value = "/afterWxLogin", method = RequestMethod.GET, produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String afterWxLogin(@ModelAttribute("SpringWeb") Staff staff, HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap mm) throws Exception {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("微信登录：");
		logger.info("跳转URL类型" + request.getParameter("identify"));
		Map<String, Object> params = getDefaultParamToReturn(true);
		if (staff == null) {
			mm.put(BaseWxModel.WX_ERRMSG, "手机号或密码不能为空");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			logger.info("返回的数据=" + params);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		//
		String phone = staff.getPhone();
		String salt = staff.getSalt();
		if (phone != null && salt != null) {
			// ...手机号码，密码的验证登录
			RSAInfo rsaInfo = generateRSA(staff.getPhone());
			logger.info("getToken的获取的值：" + rsaInfo);
			String modulus = new BigInteger(rsaInfo.getModulus(), 16).toString();
			String exponent = new BigInteger(rsaInfo.getExponent(), 16).toString();
			RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);
			String pwdEncrypted = RSAUtils.encryptByPublicKey(salt, publicKey);
			staff.setPwdEncrypted(pwdEncrypted);
			Staff staffInOut = new Staff();
			staffInOut.setPhone(staff.getPhone());
			staffInOut.setRoleID(staff.getRoleID());
			ErrorInfo ec = new ErrorInfo();
			Company company = getCompanyFromCompanyCacheBySN(staff.getCompanySN());
			if (company == null) {
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "公司编号错误");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
				logger.info("返回的数据=" + params);
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			setCompanyInSession(session, company); // 将dbName保存在会话中，后面用户的ACTION请求中将可以从会话得到DB名称，从而正确地读写DB
			staffInOut = (Staff) authenticate(company.getDbName(), BaseBO.CASE_Login, staffInOut, staff.getPwdEncrypted(), ec);

			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				// 可能用户已经在网页端登录过，如果登录过，踢出网页端的会话，保留公众号登录页面的会话
				session.removeAttribute(EnumSession.SESSION_BxStaff.toString());// 普通staff与bxstaff互斥,只能同时一个登陆
				staffInOut.setIsLoginFromPos(EnumBoolean.EB_NO.getIndex());
				session.setAttribute(EnumSession.SESSION_Staff.getName(), staffInOut);
				logger.info("STAFF(老板)登录成功。设置进会话：" + staffInOut);
				StaffLoginInterceptor.loginGuard.login(staff.getCompanySN(), staffInOut.getID(), BaseAction.INVALID_POS_ID, session);
				// 设置Cookie
				Cookie phoneCookie = new Cookie(Staff.field.getFIELD_NAME_phone(), staff.getPhone());
				Cookie passwordCookie = new Cookie(Staff.field.getFIELD_NAME_salt(), request.getParameter(Staff.field.getFIELD_NAME_salt()));
				Cookie companySnCookie = new Cookie(Staff.field.getFIELD_NAME_companySN(), request.getParameter(Staff.field.getFIELD_NAME_companySN()));
				logger.info("获取到的登录密码：===" + request.getParameter(Staff.field.getFIELD_NAME_salt()));
				// Cookie的有效时长
				phoneCookie.setMaxAge(60 * 60 * 24 * 180); // ...Cookie的时间期限为：半年
				passwordCookie.setMaxAge(60 * 60 * 24 * 180);
				companySnCookie.setMaxAge(60 * 60 * 24 * 180);
				response.addCookie(phoneCookie);
				response.addCookie(passwordCookie);
				response.addCookie(companySnCookie);
				logger.info("添加Cookie!");
				String identify = request.getParameter("identify");
				if (identify.equals("purchasingOrder")) {
					mm.put(PurchasingOrder.field.getFIELD_NAME_ID(), staff.getID());
					request.setAttribute(PurchasingOrder.field.getFIELD_NAME_ID(), staff.getID());
					logger.info("即将跳转到采购审核页面...");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
					logger.info("返回的数据=" + params);
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				} else if (identify.equals("unSalableCommodity")) {
					logger.info("即将跳转到滞销页面...");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
					logger.info("返回的数据=" + params);
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				} else {
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "identify错误");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
					logger.info("返回的数据=" + params);
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
			} else {
				setCompanyInSession(session, null);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "手机号或密码错误");
				params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode());
				logger.info("返回的数据=" + params);
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
		} else {
			mm.put(BaseWxModel.WX_ERRMSG, "手机号或密码不能为空");
			return WX_Login;
		}
	}

	public static Map<String, Object> sendUnsalableCommodityMsg(String appID, String secret, String accessTokenUrl, String templateUrl, Set<Commodity> commList, Message msg, String openid, String templateID)
			throws ClientProtocolException, IOException {
		Map<String, Object> params = new HashMap<>();
		boolean bDeleteOldToken = false;
		final int MAX_retryCount = 2;
		int retryCount = 0;
		do {
			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, appID, secret, accessTokenUrl);
			if (accessToken == null) {
				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
				return params;
			}

			// ... 1、传入信息模板参数(以下均为假数据，之后改为业务传过来的数据)
			Map<String, Object> param = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_Default_Chinese);
			String commNames = commList.iterator().next().getName() + " •••";
			String commNOs = commList.iterator().next().getNO() + " •••";
			param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你有" + commList.size() + "款商品可能滞销", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("默认仓库", WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(commNames, WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(commNOs, WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_keyword4(), new WxTemplateData(sdf.format(new Date()).toString(), WxTemplateData.backgroundColor));
			param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("点击查看详情", WxTemplateData.backgroundColor));

			// 2、封装详细信息模板
			JSONObject jsData = WxUtils.toJsonObject(param);
			// 3、拼接json数据格式
			JSONObject json = new JSONObject();
			json.put(WxTemplate.field.getFIELD_NAME_touser(), openid);// openid
			json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
			json.put(WxTemplate.field.getFIELD_NAME_url(), BaseAction.DOMAIN + "wx/isLogin.bx?identify=unSalableCommodity&categoryID=" + msg.getCategoryID());// 跳转页面
			json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
			json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据
			// 4、向微信端发送POST请求
			String newurl = String.format(templateUrl, accessToken.getAccessToken());
			JSONObject result = WxUtils.postToWxServer(newurl, json.toString());
			// 5、检查返回的结果
			int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
			if (errcode == BaseWxModel.WX_ERRCODE_InvalidToken && MAX_retryCount >= retryCount) {
				logger.error("发送消息到WX服务器时发现Token失效，正在重新申请Token和发送...\r\n此消息为：\r\n" + msg);
				bDeleteOldToken = true;
				retryCount++;
				continue;
			} else if (MAX_retryCount < retryCount) {
				logger.error("发送消息到WX服务器重试次数过大，终止重试");
			}
			String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
			params.put(BaseWxModel.WX_ERRCODE, errcode);
			params.put(BaseWxModel.WX_ERRMSG, errmsg);
			return params;
		} while (true);
	}

	private WxCustomMenu getWxCustomMenu() {
		Button button = new Button();
		button.setName("绑定博销宝");
		button.setType("view");
		button.setKey("text");
		button.setUrl(BaseAction.DOMAIN + "wx/wxBaseLogin.bx");
		// 子菜单
		// Button subButton = new Button();
		// subButton.setName("二级菜单");
		// subButton.setType("view");
		// subButton.setKey("text");
		// subButton.setUrl("http://dev.wx.bxit.vip/wx/wxBaseLogin.bx");
		// button.setSub_button(new Button[] {subButton});
		WxCustomMenu wxCustomMenu = new WxCustomMenu();
		wxCustomMenu.setButton(new Button[] { button });
		return wxCustomMenu;
	}

	/**
	 * 要更新菜单，应该要把PATH_CustomMenuAlreadyCreated文件删除掉，重新生成
	 * */
	@PostConstruct
	private void createCustomMenu() { // 目前只有“绑定公司”一个菜单
		File file = new File(PATH_CustomMenuAlreadyCreated);
		if (file.exists()) {
			logger.info("公众号自定义菜单已经创建过，不需要再创建");
			return;
		}

		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		logger.info("获取到的accessToken为" + accessToken);
		if (accessToken == null) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("获取到的accessToken为null，将导致微信相关功能不正常！");
			}
			return;
		}

		String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", accessToken.getAccessToken());
		// 将菜单对象转换成json字符串
		WxCustomMenu wxCustomMenu = getWxCustomMenu();
		String jsonMenu = JSONObject.fromObject(wxCustomMenu).toString();
		JSONObject jsonObject = WxUtils.postToWxServer(url, jsonMenu);
		logger.debug("createCustomMenu():" + jsonObject);

		Object errCode = jsonObject.get(BaseWxModel.WX_ERRCODE);
		Object errMsg = jsonObject.get(BaseWxModel.WX_ERRMSG);
		if ("0".equals(String.valueOf(errCode)) && "ok".equals(String.valueOf(errMsg))) {
			logger.info("生成公众号自定义菜单成功！");

			try {
				if (!file.createNewFile()) {
					logger.error("无法创建文件：" + PATH_CustomMenuAlreadyCreated);
					return;
				}
				String content = "CustomMenuAlreadyCreated=1";
				FileOutputStream fos = new FileOutputStream(PATH_CustomMenuAlreadyCreated, false);
				fos.write(content.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("生成公众号自定义菜单失败!无法向文件" + PATH_CustomMenuAlreadyCreated + "写入内容。异常=" + e.getMessage());
				return;
			}
			logger.info("标记成功生成公众号自定义菜单成功！");
		} else {
			logger.error("生成公众号自定义菜单失败!WX服务器返回错误码：" + errCode + "，错误信息：" + errMsg);
		}
	}
}
