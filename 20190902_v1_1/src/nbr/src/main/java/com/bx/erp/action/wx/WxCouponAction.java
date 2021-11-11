//package com.bx.erp.action.wx;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.BaseWxBO;
//import com.bx.erp.action.bo.wx.WxCardCouponUserCodeBO;
//import com.bx.erp.action.bo.wx.WxDateInfoBO;
//import com.bx.erp.action.bo.wx.WxSkuBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponAbstractBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponAdvancedInfoBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponBaseInfoBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponDetailBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponTextImageBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponTimeLimitBO;
//import com.bx.erp.action.bo.wx.coupon.WxCouponUseConditionBO;
//import com.bx.erp.action.wx.wxopen.Wx3rdPartyCardAction;
//import com.bx.erp.cache.CacheManager;
//import com.bx.erp.cache.VipBelongingCache;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.Staff;
//import com.bx.erp.model.VipBelonging;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.commodity.CommodityField;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.QRCodeDistributionDetail;
//import com.bx.erp.model.wx.WXPayInfo;
//import com.bx.erp.model.wx.WxAccessToken;
//import com.bx.erp.model.wx.card.WxVip;
//import com.bx.erp.model.wx.WxDateInfo;
//import com.bx.erp.model.wx.WxSku;
//import com.bx.erp.model.wx.card.WxVipCard;
//import com.bx.erp.model.wx.coupon.CouponQRCodeDistribution;
//import com.bx.erp.model.wx.coupon.CouponStatistics;
//import com.bx.erp.model.wx.coupon.WxCoupon;
//import com.bx.erp.model.wx.coupon.WxCouponAbstract;
//import com.bx.erp.model.wx.coupon.WxCouponAdvancedInfo;
//import com.bx.erp.model.wx.coupon.WxCouponBaseInfo;
//import com.bx.erp.model.wx.coupon.WxCouponDetail;
//import com.bx.erp.model.wx.coupon.WxCouponTextImage;
//import com.bx.erp.model.wx.coupon.WxCouponTimeLimit;
//import com.bx.erp.model.wx.coupon.WxCouponUseCondition;
//import com.bx.erp.model.wx.wxopen.WxOpenAccessToken;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//import com.bx.erp.util.WxUtils;
//import com.github.wxpay.sdk.WXPay;
//import com.github.wxpay.sdk.WXPayConstants;
//import com.github.wxpay.sdk.WXPayConstants.SignType;
//import com.github.wxpay.sdk.WXPayUtil;
//import com.jayway.jsonpath.JsonPath;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wxCoupon")
//public class WxCouponAction extends BaseAction {
//	private static Log logger = LogFactory.getLog(Wx3rdPartyCardAction.class);
//
//	private final static String CARD_ID = "card_id";
//
//	@Resource
//	private WxCouponBO wxCouponBO;
//	@Resource
//	private WxCouponDetailBO wxCouponDetailBO;
//	@Resource
//	private WxCouponBaseInfoBO wxCouponBaseInfoBO;
//	@Resource
//	private WxCouponAdvancedInfoBO wxCouponAdvancedInfoBO;
//	@Resource
//	private WxCouponUseConditionBO wxCouponUseConditionBO;
//	@Resource
//	private WxCouponAbstractBO wxCouponAbstractBO;
//	@Resource
//	private WxCouponTextImageBO wxCouponTextImageBO;
//	@Resource
//	private WxCouponTimeLimitBO wxCouponTimeLimitBO;
//	@Resource
//	private WxCardCouponUserCodeBO wxCardCouponUserCodeBO;
//	@Resource
//	private WxSkuBO wxSkuBO;
//	@Resource
//	private WxDateInfoBO wxDateInfoBO;
//
//	@Value("${public.account.appid}")
//	private String publicAccountAppID;
//
//	@Value("${public.account.secret}")
//	private String publicAccountSecret;
//
//	@Value("${third.party.appid}")
//	protected String componentAppID;
//
//	@Value("${third.party.token}")
//	protected String token;
//
//	@Value("${third.party.encodingAESKey}")
//	protected String encodingAesKey;
//
//	@Value("${third.party.secret}")
//	protected String componentAppSecret;
//
//	@Value("${post.ComponentAccessToken}")
//	protected String componentAccessTokenUrl;
//
//	@Value("${post.PreAuthCode}")
//	protected String preAuthCodeUrl;
//
//	@Value("${post.AuthorizationPage}")
//	protected String authorizationPageUrl;
//
//	@Value("${post.AuthorizationInfo}")
//	protected String authorizationInfoUrl;
//
//	@Value("${post.AuthorizerRefreshToekn}")
//	protected String authorizerRefreshToeknUrl;
//
//	@Value("${post.AuthorizerInfo}")
//	protected String authorizerInfoUrl;
//
//	@Value("${post.AuthorizationCallBack}")
//	protected String authorizationCallBackUrl;
//
//	@Value("${post.message.CustomerService}")
//	protected String messageCustomerServiceUrl;
//
//	@Value("${post.card.uploadimg}")
//	protected String cardUploadimgUrl; // 上传卡券图片素材
//
//	@Value("${post.card.create}")
//	protected String cardCreateUrl; // 创建卡券
//
//	@Value("${post.card.paycell}")
//	protected String cardPaycellUrl; // 设置卡券买单
//
//	@Value("${post.card.qrcode}")
//	protected String cardQrcodeUrl; // 获取二维码ticket
//
//	@Value("${post.card.code}")
//	protected String cardCodeUrl; // 查询卡券code码
//
//	@Value("${post.card.consume}")
//	protected String cardConsumeUrl; // 核销卡券
//
//	@Value("${post.card.detail}")
//	protected String cardDetailUrl; // 卡券详情
//
//	@Value("${post.card.batchget}")
//	protected String cardRetrievelistUrl; // 批量查询卡券列表
//
//	@Value("${post.card.update}")
//	protected String cardUpdateUrl; // 更改卡券信息接口
//
//	@Value("${post.card.modifystock}")
//	protected String cardUpdatestockUrl; // 修改库存接口
//
//	@Value("${post.card.delete}")
//	protected String cardDeleteUrl; // 删除卡券接口
//
//	@Value("${post.card.unavailable}")
//	protected String cardUnavailableUrl; // 设置卡券失效接口
//
//	@Value("${post.card.bizuininfo}")
//	protected String cardSummarydataUrl; // 拉取卡券概况数据接口
//
//	@Value("${post.card.cardinfo}")
//	protected String cardCardinfoUrl; // 获取免费券数据接口
//
//	@Value("${post.user.getOpenID}")
//	protected String POST_GETOPENID_URL; // 根据授权码获取用户OpenID
//
//	@Value("${post.card.retrieveN}")
//	private String POST_CARDRETRIEVEN_URL; // 根据OpenID获取到用户所有的卡券
//
//	@Value("${public.account.submch.appid}")
//	private String PUBLIC_ACCOUNT_APPID;
//
//	@Value("${public.account.submch.secret}")
//	private String PUBLIC_ACCOUNT_SECRET;
//
//	@Value("${env.appid}")
//	private String appid;
//
//	@Value("${env.mchid}")
//	private String mchid;
//
//	@Value("${env.secret}")
//	private String secret;
//
//	@Value("${env.cert}")
//	private String cert;
//
//	// @Value("${post.BatchPullSubmchs}")
//	// protected String batchPullSubmchs_url; // 批量拉取子商户号的信息
//	//
//	// @Value("${post.PullSubmch}")
//	// protected String pullSubmch; // 批量拉取子商户号的信息
//
//	@Value("${get.accesstoken.url}")
//	private String accessTokenUrl; // 微信公众号获取访问微信端的token接口
//
//
//	@Value("${mp.customAppBrandUserName}")
//	private String custom_app_brand_user_name; // 自定义使用入口跳转小程序的user_name 
//	
//	@Value("${mp.customAppBrandPass}")
//	private String custom_app_brand_pass; // 自定义使用入口小程序页面地址
//	
//	/** 卡券图片类型 */
//	public static final String JPEGType = "jpg";
//	public static final String PNGType = "png";
//
//	public static final String CardStatus_Normal = "NORMAL";
//	
//	/*
//	 * 优惠券管理页面跳转
//	 */
//	@RequestMapping(value = "/manage", method = RequestMethod.GET)
//	public String manage(ModelMap mm, HttpSession session) {
//		Company company = getCompanyFromSession(session);
//
//		List<BaseModel> categoryList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).readN(true, false);
//		List<BaseModel> categoryParentList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).readN(true, false);
//
//		mm.put("CommodityField", new CommodityField());
//		mm.put("categoryList", categoryList);
//		mm.put("categoryParentList", categoryParentList);
//		return COUPON_Manage;
//	}
//
////	@RequestMapping(value = "/uploadPictureEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
////	@ResponseBody
////	private String uploadPicture(HttpSession session, @RequestParam("file") MultipartFile multipartFile) throws IOException {
////		Map<String, Object> map = new HashMap<>();
////		//
////		do {
////			Company company = getCompanyFromSession(session);
////			if (company.getAuthorizerAppid() == null || company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
////				logger.info("当前公司没有授权微信公众号！！");
////				return null;
////			}
////
////			WxOpenAccessToken wxOpenAccessToken = WxUtils.getWxAuthorizerAccessToken(company, componentAppID, componentAppSecret, componentAccessTokenUrl, authorizerRefreshToeknUrl);
////			if (wxOpenAccessToken == null || wxOpenAccessToken.getAuthorizerAccessToken() == null) {
////				logger.info("获取authorizer_access_token失败--> authorizer_access_token == null ");
////				break;
////			}
////
////			File file = multipartFileToFile(multipartFile, map);
////			if (file == null) {
////				break;
////			}
////
////			String url = String.format(cardUploadimgUrl, wxOpenAccessToken.getAuthorizerAccessToken());
////			JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
////
////			// 向微信上传文件请求后删除服务器上缓存的图片文件
////			file.delete();
////
////			if (response == null) {
////				logger.info("向微信POST请求上传卡券图片错误！！");
////				break;
////			} else {
////				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
////					logger.info("上传卡券图片成功！！");
////					//
////					map.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
////					map.put(KEY_Object, response);
////					//
////					return JSONObject.fromObject(map).toString();
////				} else {
////					logger.info("请求上传卡券图片错误：" + response);
////					map.put(KEY_HTMLTable_Parameter_msg, response);
////					break;
////				}
////			}
////		} while (false);
////
////		map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
////
////		return JSONObject.fromObject(map).toString();
////	}
//
//	/*
//	 * 创建卡卷页面跳转 User-Agent: Fiddler Host: localhost:8080 Content-Type:
//	 * application/x-www-form-urlencoded Fiddler URL:
//	 * http://localhost:8080/nbr/wx3rdPartyCard/create.bx
//	 */
//	@RequestMapping(value = "/create", method = { RequestMethod.GET })
//	public String create(HttpServletRequest req, ModelMap mm) { // TODO 这个可能需要重构。。。。。
//		mm.put("cardType", GetStringFromRequest(req, "cardType", ""));
//		return WX3RDPARTYCARD_Create;
//	}
//
//	/** 创建卡券 */
//	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createEx(HttpSession session, @RequestBody String postData) {
//		Map<String, Object> params = getDefaultParamToReturn(true);
//
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		//
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		WxCouponBaseInfo wxCouponBaseInfo = wxCoupon.getWxCouponDetail().getWxCouponDetailPartition().getBase_info();
//		wxCouponBaseInfo.setCustom_app_brand_pass(custom_app_brand_pass);
//		wxCouponBaseInfo.setCustom_app_brand_user_name(custom_app_brand_user_name);
//		
//		// 拿到需要的参数发送到WX
//		Map<String, Object> wxCouponParam = new HashMap<String, Object>();
//		if (wxCoupon.getWxCouponDetail() != null && wxCoupon.getWxCouponDetail().getCard_type().equals(WxCouponDetail.EnumCouponType.ECT_Cash.getName())) {
//			wxCouponParam = wxCoupon.getHttpCreateParam(BaseWxBO.CASE_WxCash, wxCoupon);
//		} else if (wxCoupon.getWxCouponDetail() != null && wxCoupon.getWxCouponDetail().getCard_type().equals(WxCouponDetail.EnumCouponType.ECT_Discount.getName())) {
//			wxCouponParam = wxCoupon.getHttpCreateParam(BaseWxBO.CASE_WxDiscount, wxCoupon);
//		} else {
//			logger.error("参数缺失，当黑客行为处理");
//			return null;
//		}
//		JSONObject json = JSONObject.fromObject(wxCouponParam);
//		// 去掉一些没有使用到的字段
//		collateJSONObject(json);
//		//
//		JSONObject wxResponse = WxUtils.publicAccountPostToWxServer(cardCreateUrl, json.toString(), "创建卡券", publicAccountAppID, publicAccountSecret, accessTokenUrl);
//
//		String error = wxResponse.getString(BaseAction.JSON_ERROR_KEY);
//		JSONObject wxResponseWithR1 = new JSONObject();
//		if (!error.isEmpty() && error.equals("EC_NoError")) {
//			// 将卡券归属写入缓存中，整个时候优惠卷已在WX中创建成功
//			VipBelonging vipBelonging = new VipBelonging();
//			vipBelonging.setCardID(wxResponse.getJSONObject(BaseAction.KEY_Object).getString(CARD_ID));
//			vipBelonging.setDbName(dbName);
//			CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_VipBelonging).write1(vipBelonging, BaseAction.DBName_Public, staff.getID());
//			//
//			WxCoupon wxCouponToRetrieve1 = new WxCoupon();
//			wxCouponToRetrieve1.setCardID(wxResponse.getJSONObject(BaseAction.KEY_Object).getString(CARD_ID));
//
//			// 拿到需要的参数发送到WX
//			Map<String, Object> paramsR1 = wxCoupon.getHttpRetrieve1Param(BaseAction.INVALID_ID, wxCouponToRetrieve1);
//			JSONObject jsonR1 = JSONObject.fromObject(paramsR1);
//
//			wxResponseWithR1 = WxUtils.publicAccountPostToWxServer(cardDetailUrl, jsonR1.toString(), "获取卡券详情", publicAccountAppID, publicAccountSecret, accessTokenUrl);
//		}
//		// 判断创建卡券是否成功
//		boolean isSuccess = false;
//		WxCoupon wxCouponToCreate = new WxCoupon();
//		wxCouponToCreate = (WxCoupon) wxCouponToCreate.parse1(BaseWxBO.CASE_Parse1FromWx, wxResponseWithR1.getJSONObject(BaseAction.KEY_Object).toString());
//		do {
//			// 创建WxCoupon到DB
//			WxCoupon wxCouponCreate = createWxCouponToDB(wxCouponToCreate, dbName, staff.getID());
//			if (wxCouponCreate == null) {
//				break;
//			}
//			// 创建WxCouponDetail到DB
//			WxCouponDetail wxCouponDetailToCreate = wxCouponToCreate.getWxCouponDetail();
//			WxCouponDetail wxCouponDetailCreate = createWxCouponDetailToDB(wxCouponDetailToCreate, wxCouponCreate.getID(), dbName, staff.getID());
//			if (wxCouponDetailCreate == null) {
//				break;
//			}
//			WxCouponBaseInfo wxCouponBaseInfoToCreate = wxCouponDetailToCreate.getWxCouponDetailPartition().getBase_info();
//			// 创建WxSku
//			WxSku wxSkuToCreate = wxCouponBaseInfoToCreate.getSku();
//			WxSku wxSkuCreate = createWxSkuToDB(wxSkuToCreate, dbName, staff.getID());
//			if (wxSkuCreate == null) {
//				break;
//			}
//			// 创建WxDateInfo
//			WxDateInfo wxDateInfo = wxCouponBaseInfoToCreate.getDate_info();
//			WxDateInfo wxDateInfoCreate = createWxDateInfoToDB(wxDateInfo, dbName, staff.getID());
//			if (wxDateInfoCreate == null) {
//				break;
//			}
//			// 创建WxCouponBaseInfo
//			WxCouponBaseInfo wxCouponBaseInfoCreate = createWxCouponBaseInfoToDB(wxCouponBaseInfoToCreate, wxCouponDetailCreate.getID(), wxSkuCreate.getID(), wxDateInfoCreate.getID(), dbName, staff.getID());
//			if (wxCouponBaseInfoCreate == null) {
//				break;
//			}
//			// 创建WxCouponAdvancedInfo
//			WxCouponAdvancedInfo wxCouponAdvancedInfoToCreate = wxCouponDetailToCreate.getWxCouponDetailPartition().getAdvanced_info();
//			WxCouponAdvancedInfo wxCouponAdvancedInfoCreate = createWxCouponAdvancedInfoToDB(wxCouponAdvancedInfoToCreate, wxCouponDetailCreate.getID(), dbName, staff.getID());
//			if (wxCouponAdvancedInfoCreate == null) {
//				break;
//			}
//			// 创建WxCouponUseCondition
//			WxCouponUseCondition wxCouponUseConditionToCreate = wxCouponAdvancedInfoToCreate.getUse_condition();
//			createWxCouponUseConditionToDB(wxCouponUseConditionToCreate, wxCouponAdvancedInfoCreate.getID(), dbName, staff.getID());
//			// 创建WxCouponAbstract
//			WxCouponAbstract wxCouponAbstractToCreate = wxCouponAdvancedInfoToCreate.getAbstractEx();
//			createWxCouponAbstractToDB(wxCouponAbstractToCreate, wxCouponAdvancedInfoCreate.getID(), dbName, staff.getID());
//			// 创建WxCouponTextImage
//			List<BaseWxModel> wxCouponTextImageToCreate = wxCouponAdvancedInfoToCreate.getText_image_list();
//			createWxCouponTextImageListToDB(wxCouponTextImageToCreate, wxCouponAdvancedInfoCreate.getID(), dbName, staff.getID());
//			// 创建WxCouponTimeLimit
//			List<BaseWxModel> wxCouponTimeLimitToCreate = wxCouponAdvancedInfoToCreate.getTime_limit();
//			createWxCouponTimeLimitListToDB(wxCouponTimeLimitToCreate, wxCouponAdvancedInfoCreate.getID(), dbName, staff.getID());
//			//
//			isSuccess = true;
//		} while (false);
//
//		params.put(KEY_HTMLTable_Parameter_TotalRecord, 1);
//		if (isSuccess) {
//			params.put("card_id", wxResponse.getJSONObject(BaseAction.KEY_Object).getString(CARD_ID));
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "创建优惠券成功");
//			params.put(BaseAction.KEY_Object, wxCouponToCreate);
//		} else {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "创建优惠券失败");
//		}
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	// 折扣卷应该在CreateEx中一起判断
//	// @RequestMapping(value = "/createWxDiscountCouponEx", produces = "plain/text;
//	// charset=UTF-8", method = RequestMethod.POST)
//	// @ResponseBody
//	// public String createWxDiscountCouponEx(@ModelAttribute("SpringWeb")
//	// WxCouponDiscount wxCouponDiscount, ModelMap mm, HttpSession session) {
//	// logger.info("创建折扣卷,baseModel=" + wxCouponDiscount);
//	//
//	// return "";
//	// }
//
//	/*
//	 * 卡卷详情页面跳转 User-Agent: Fiddler Host: localhost:8080 Content-Type:
//	 * application/x-www-form-urlencoded Fiddler URL:
//	 * http://localhost:8080/nbr/wx3rdPartyCard/couponDetail.bx
//	 */
//	@RequestMapping(value = "/couponDetail", method = { RequestMethod.GET })
//	public String couponDetail(HttpServletRequest req, ModelMap mm) {
//		// mm.put("cardType", GetStringFromRequest(req, "cardType", ""));
//		return WX3RDPARTYCARD_CouponDetail;
//	}
//
//	/** 设置买单 先注释，要使用的时候再放开 */
//	// @RequestMapping(value = "/updateToWxpayEx", produces = "plain/text;
//	// charset=UTF-8", method = { RequestMethod.POST })
//	// @ResponseBody
//	// private String updateToWxpayEx(HttpSession session, @RequestBody String
//	// postData) {
//	// Company company = getCompanyFromSession(session);
//	// if (company.getAuthorizerAppid() == null ||
//	// company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
//	// logger.info("当前公司没有授权微信公众号！！");
//	// return null;
//	// }
//	// return WxUtils.postToWxServer(cardPaycellUrl, postData, "设置买单", company,
//	// componentAppID, componentAppSecret, componentAccessTokenUrl,
//	// authorizerRefreshToeknUrl).toString();
//	// }
//
//	/** 获取卡券详情 */
//	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieve1Ex(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpRetrieve1Param(BaseAction.INVALID_ID, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//
//		return WxUtils.publicAccountPostToWxServer(cardDetailUrl, json.toString(), "获取卡券详情", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 创建领券二维码 */
//	@RequestMapping(value = "/createQRCodeEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createQRCodeEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		CouponQRCodeDistribution couponQRCodeDistribution = new CouponQRCodeDistribution();
//		couponQRCodeDistribution = (CouponQRCodeDistribution) couponQRCodeDistribution.parse1(postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = couponQRCodeDistribution.getHttpCreateParam(BaseAction.INVALID_ID, couponQRCodeDistribution);
//		JSONObject json = JSONObject.fromObject(params);
//		// 去掉一些没有使用到的字段
//		collateJSONObject(json);
//
//		return WxUtils.publicAccountPostToWxServer(cardQrcodeUrl, json.toString(), "创建领券二维码", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 核销优惠券 */
//	@RequestMapping(value = "/updateToConsumedEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateToConsumedEx(HttpSession session, @RequestBody String code) {
//		Map<String, Object> map = new HashMap<>();
//		logger.info("code=" + code);
//
//		JSONObject response = WxUtils.publicAccountPostToWxServer(cardCodeUrl, code, "查询优惠券code码", publicAccountAppID, publicAccountSecret, accessTokenUrl);
//		if (!EnumErrorCode.EC_NoError.toString().equals(response.getString(JSON_ERROR_KEY))) {
//			return response.toString();
//		}
//
//		String card_status = response.getJSONObject(KEY_Object).getString("user_card_status");
//		if (CardStatus_Normal.equals(card_status)) {
//			return WxUtils.publicAccountPostToWxServer(cardConsumeUrl, code, "核销优惠券", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//		} else {
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			map.put(KEY_HTMLTable_Parameter_msg, "优惠券无效！！");
//		}
//
//		return JSONObject.fromObject(map).toString();
//	}
//
////	/** 批量查询卡券列表 */
////	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
////	@ResponseBody
////	private String retrieveNEx(HttpSession session, @RequestBody String postData) {
////		Company company = getCompanyFromSession(session);
////		if (company.getAuthorizerAppid() == null || company.getAuthorizerRefreshToken() == null || company.getFuncInfo() == 0) {
////			logger.info("当前公司没有授权微信公众号！！");
////			return null;
////		}
////		// 解析前端传来的Json
////		WxCoupon wxCoupon = new WxCoupon();
////		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
////		// 拿到需要的参数发送到WX
////		Map<String, Object> paramsToRN = wxCoupon.getHttpRetrieveNParam(BaseAction.INVALID_ID, wxCoupon);
////		JSONObject json = JSONObject.fromObject(paramsToRN);
////		try {
////			JSONObject jsonCardIDs = WxUtils.publicAccountPostToWxServer(cardRetrievelistUrl, json.toString(), "批量查询卡券列表", publicAccountAppID, publicAccountSecret, accessTokenUrl);
////			if (!EnumErrorCode.EC_NoError.toString().equals(jsonCardIDs.getString(BaseAction.JSON_ERROR_KEY))) {
////				logger.error("批量查询卡券列表失败，微信服务器返回的数据： " + jsonCardIDs);
////				return "";
////			}
////			// 解析json,拿到ID,放入List数组
////			List<String> listIDs = new ArrayList<>();
////			JSONObject objectJson = jsonCardIDs.getJSONObject(BaseAction.KEY_Object);//
////			JSONArray jsonArr = objectJson.getJSONArray("card_id_list");//
////			for (int i = 0; i < jsonArr.size(); i++) {
////				String sID = (String) jsonArr.get(i);
////				listIDs.add(sID);
////			}
////			// 进行for循环，发送R1,得到每个优惠券的详情，放入ArrayList中
////			List<JSONObject> listCardInfo = new ArrayList<>();
////			for (int i = 0; i < listIDs.size(); i++) {
////				WxCoupon wxCouponToR1 = new WxCoupon();
////				wxCouponToR1.setCardID(listIDs.get(i));
////				//
////				Map<String, Object> params = wxCoupon.getHttpRetrieve1Param(BaseAction.INVALID_ID, wxCouponToR1);
////				JSONObject jsonToR1 = JSONObject.fromObject(params);
////				// 得到优惠券详情是JSONObject.toString()
////				String cardInfo = WxUtils.publicAccountPostToWxServer(cardDetailUrl, jsonToR1.toString(), "获取卡券详情", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
////				if (cardInfo == null) {
////					// json为空如何操作
////					logger.error("向微信服务器查询失败，失败card_id为：" + wxCouponToR1.getCardID());
////					continue; // 当查询一个失败时，是否让他继续查询其他卡卷？？？
////				}
////				// 只需要返回代金券，折扣券，其他的券不返回
////				String cardType = JSONObject.fromObject(cardInfo).getJSONObject(BaseAction.KEY_Object).getJSONObject(BaseWxModel.WXCard).getString(BaseWxModel.WXCard_type);
////				if (!(BaseWxModel.WXCardType_CASH.equals(cardType) || BaseWxModel.WXCardType_DISCOUNT.equals(cardType))) {
////					logger.info("非代金券，折扣券，不需要返回，此券详情为：" + cardInfo);
////					continue;
////				}
////				JSONObject jsonCardInfo = JSONObject.fromObject(cardInfo);
////				listCardInfo.add(jsonCardInfo);
////			}
////			// 根据时间降序
////			Collections.sort(listCardInfo, new Comparator<JSONObject>() {
////				@Override
////				public int compare(JSONObject o1, JSONObject o2) {
////					return getCardCreateTime(o2).compareTo(getCardCreateTime(o1));
////				}
////			});
////			//
////			Map<String, Object> params = new HashMap<>();
////			params.put(KEY_ObjectList, listCardInfo);
////			params.put("jsonCardIDs", jsonCardIDs);
////			logger.info("返回的数据params=" + params);
////
////			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
////		} catch (Exception e) {
////			logger.error("查询卡卷列表异常，异常信息为：" + e.getMessage());
////			return null;
////		}
////	}
//
//	/** 解析JSONObject中微信卡卷创建的时间 */
//	private String getCardCreateTime(JSONObject o) {
//		try {
//			JSONObject jSONObject = o.getJSONObject(BaseAction.KEY_Object).getJSONObject(BaseWxModel.WXCard);
//			String strCardCreateTime = "";
//			switch (jSONObject.getString(BaseWxModel.WXCard_type)) {
//			case BaseWxModel.WXCardType_DISCOUNT:
//				strCardCreateTime = jSONObject.getJSONObject(BaseWxModel.WXCardType_DISCOUNT.toLowerCase()).getJSONObject(BaseWxModel.WXCard_baseInfo).getString(BaseWxModel.WXCard_createTime);
//				break;
//			case BaseWxModel.WXCardType_CASH:
//				strCardCreateTime = jSONObject.getJSONObject(BaseWxModel.WXCardType_CASH.toLowerCase()).getJSONObject(BaseWxModel.WXCard_baseInfo).getString(BaseWxModel.WXCard_createTime);
//				break;
//			case BaseWxModel.WXCardType_GROUPON:
//			case BaseWxModel.WXCardType_GIFT:
//			case BaseWxModel.WXCardType_GENERAL_COUPON:
//			case BaseWxModel.WXCardType_MEMBER_CARD:
//			case BaseWxModel.WXCardType_SCENIC_TICKET:
//			case BaseWxModel.WXCardType_MOVIE_TICKET:
//			case BaseWxModel.WXCardType_BOARDING_PASS:
//			case BaseWxModel.WXCardType_MEETING_TICKET:
//			case BaseWxModel.WXCardType_BUS_TICKET:
//			default:
//				logger.error("异常券，异常对象为：" + o);
//				break;
//			}
//
//			return strCardCreateTime;
//		} catch (Exception e) {
//			logger.error("解析卡卷创建时间异常，异常信息为：" + e.getMessage());
//			return "";
//		}
//	}
//
//	/** 更改卡券信息接口 现在能修改的只有WX文档中演示的字段，后期如果有其他需要，可以自行增加并测试 */
//	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = new HashMap<String, Object>();
//		if (wxCoupon.getWxCouponDetail() != null && wxCoupon.getWxCouponDetail().getCard_type().equals(WxCouponDetail.EnumCouponType.ECT_Cash.getName())) {
//			params = wxCoupon.getHttpUpdateParam(BaseWxBO.CASE_WxCash, wxCoupon);
//		} else if (wxCoupon.getWxCouponDetail() != null && wxCoupon.getWxCouponDetail().getCard_type().equals(WxCouponDetail.EnumCouponType.ECT_Discount.getName())) {
//			params = wxCoupon.getHttpUpdateParam(BaseWxBO.CASE_WxDiscount, wxCoupon);
//		} else {
//			logger.error("参数缺失，当黑客行为处理");
//			return null;
//		}
//		JSONObject json = JSONObject.fromObject(params);
//		// 去掉一些没有使用到的字段
//		collateJSONObject(json);
//
//		return WxUtils.publicAccountPostToWxServer(cardUpdateUrl, json.toString(), "更改卡券信息", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 修改卡券库存 */
//	@RequestMapping(value = "/updateStockEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateStockEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpUpdateParam(BaseWxBO.CASE_UpdateStock, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//
//		return WxUtils.publicAccountPostToWxServer(cardUpdatestockUrl, json.toString(), "修改卡券库存", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 删除卡券 */
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String deleteEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpDeleteParam(BaseAction.INVALID_ID, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//		//
//		return WxUtils.publicAccountPostToWxServer(cardDeleteUrl, json.toString(), "删除卡券", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 设置卡券失效 */
//	@RequestMapping(value = "/updateToUnavailableEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateToUnavailableEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpUpdateParam(BaseWxBO.CASE_UpdateToUnavailable, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//		//
//		return WxUtils.publicAccountPostToWxServer(cardUnavailableUrl, json.toString(), "设置卡券失效", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 统计卡券概况数据 */
//	@RequestMapping(value = "/retrieveSummaryEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveSummaryEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpRetrieveNParam(BaseWxBO.CASE_RetrieveSummary, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//		// 可以不用进行解析，直接返回Wx返回的数据到前端
//		// JSONObject response = WxUtils.publicAccountPostToWxServer(cardSummarydataUrl,
//		// json.toString(), "统计卡券概况数据", publicAccountAppID, publicAccountSecret,
//		// accessTokenUrl);
//		// JSONObject list = JSONObject.fromObject(response.getString(KEY_Object));
//		// //
//		// CouponStatistics couponStatistics = new CouponStatistics();
//		// List<BaseWxModel> bwmList =
//		// WxUtils.publicAccountPostToWxServer(cardSummarydataUrl, json.toString(),
//		// "统计卡券概况数据", publicAccountAppID, publicAccountSecret, accessTokenUrl).to;
//
//		return WxUtils.publicAccountPostToWxServer(cardSummarydataUrl, json.toString(), "统计卡券概况数据", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 统计单张优惠券数据 */
//	@RequestMapping(value = "/retrieve1CardInfoEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveCardInfoEx(HttpSession session, @RequestBody String postData) {
//		// 解析前端传来的Json
//		WxCoupon wxCoupon = new WxCoupon();
//		wxCoupon = (WxCoupon) wxCoupon.parse1(BaseAction.INVALID_ID, postData);
//		// 拿到需要的参数发送到WX
//		Map<String, Object> params = wxCoupon.getHttpRetrieveNParam(BaseWxBO.CASE_RetrieveCardInfo, wxCoupon);
//		JSONObject json = JSONObject.fromObject(params);
//
//		return WxUtils.publicAccountPostToWxServer(cardCardinfoUrl, json.toString(), "统计单张优惠券数据", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 数据与对账页面跳转接口 */
//	@RequestMapping(value = "/reconciliation", method = RequestMethod.GET)
//	public String reconciliation() {
//		return wx3rdPartyCard_reconciliation;
//	}
//
//	/** 数据与对账页面（详情数据）接口 */
//	@RequestMapping(value = "/detail", method = RequestMethod.GET)
//	public String detail() {
//		return wx3rdPartyCard_detail;
//	}
//
//	/** 优惠券列表页面跳转接口 */
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public String cardList() {
//		return wx3rdPartyCard_cardList;
//	}
//
//	/*
//	 * 会员卡页面跳转
//	 */
//	@RequestMapping(value = "/vipCard", method = RequestMethod.GET)
//	public String vipCard() {
//		return WX3RDPARTYCARD_vipCard;
//	}
//
//	/*
//	 * 会员卡创建页面跳转
//	 */
//	@RequestMapping(value = "/vipCardToCreate", method = RequestMethod.GET)
//	public String vipCardToCreate() {
//		return WX3RDPARTYCARD_vipCardToCreate;
//	}
//
//	/*
//	 * 会员卡详情页面跳转
//	 */
//	@RequestMapping(value = "/vipCardDetail", method = RequestMethod.GET)
//	public String vipCardDetail() {
//		return WX3RDPARTYCARD_vipCardDetail;
//	}
//
//	/*
//	 * 会员详情页面跳转
//	 */
//	@RequestMapping(value = "/vipDetail", method = RequestMethod.GET)
//	public String vipDetail() {
//		return WX3RDPARTYCARD_vipDetail;
//	}
//
//	/** 创建会员卡 */
//	@RequestMapping(value = "/createWxVipCardEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createWxVIPEx(@RequestBody String postData, @ModelAttribute("SpringWeb") WxVipCard wxCard) {
//
//		return WxUtils.publicAccountPostToWxServer(cardCreateUrl, postData, "创建微信会员卡", publicAccountAppID, publicAccountSecret, accessTokenUrl).toString();
//	}
//
//	/** 查询用户可使用的优惠券 */
//	@RequestMapping(value = "/queryAvailableWxCouponEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	public String queryAvailableWxCoupon(HttpSession session, @RequestParam("authCode") String authCode) {
//		if (StringUtils.isEmpty(authCode)) {
//			return "";
//		}
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		do {
//			try {
//				Company company = getCompanyFromSession(session);
//				// 通过授权码获取到用户的openID
//				String openID = queryUserOpenID(authCode, company.getSubmchid());
//
//				WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(true, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, accessTokenUrl);
//				if (accessToken == null) {
//					params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//					params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//					break;
//				}
//
//				List<WxCoupon> availableWxCouponList = new ArrayList<WxCoupon>();
//				// 通过openID获取到用户拥有该公司下的所有卡券
//				List<WxCoupon> wxCouponList = retrieveNUserWxCoupon(openID, accessToken, company.getDbName());
//				if (wxCouponList != null && wxCouponList.size() > 0) {
//					// 查找卡券详情,并筛选出代金券和折扣券
//					wxCouponList = queryWxCouponList(wxCouponList);
//					// 查询哪些优惠券的状态可核销(微信文档中提到会返回正常和异常状态的.但是目前测试发现只会返回正常状态的，但是为了安全还是需要查询下是否可以进行核销)
//					availableWxCouponList = checkWxCouponListStatus(wxCouponList);
//				}
//
//				params.put(BaseAction.KEY_ObjectList, availableWxCouponList);
//				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//			} catch (Exception e) {
//				e.printStackTrace();
//				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "获取用户微信可核销卡券失败!");
//				break;
//			}
//		} while (false);
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	private List<WxCoupon> queryWxCouponList(List<WxCoupon> wxCouponList) {
//		List<WxCoupon> returnWxCouponList = new ArrayList<WxCoupon>();
//		for (int i = 0; i < wxCouponList.size(); i++) {
//			WxCoupon wxCoupon = wxCouponList.get(i);
//			Map<String, Object> params = wxCoupon.getHttpRetrieve1Param(BaseAction.INVALID_ID, wxCoupon);
//			JSONObject json = JSONObject.fromObject(params);
//			//
//			JSONObject jsonObject = WxUtils.publicAccountPostToWxServer(cardDetailUrl, json.toString(), "获取卡券详情", publicAccountAppID, publicAccountSecret, accessTokenUrl);
//			if (EnumErrorCode.EC_NoError.toString().equals(jsonObject.getString(BaseAction.JSON_ERROR_KEY))) {
//				JSONObject objectJson = jsonObject.getJSONObject(BaseAction.KEY_Object);
//				String cardType = objectJson.getJSONObject(WxCoupon.field.getFIELD_NAME_wxCouponDetail()).getString(CouponStatistics.field.getFIELD_NAME_card_type());
//				if (BaseWxModel.EnumWxCardAndCouponType.EWCACT_DISCOUNT.getName().equals(cardType) || BaseWxModel.EnumWxCardAndCouponType.EWCACT_CASH.getName().equals(cardType)) {
//					WxCoupon wxCouponR1 = new WxCoupon();
//					wxCouponR1 = (WxCoupon) wxCouponR1.parse1(BaseWxBO.CASE_Parse1FromWx, objectJson.toString());
//					if (wxCouponR1 != null) {
//						wxCouponR1.setCode(wxCoupon.getCode());
//						returnWxCouponList.add(wxCouponR1);
//					}
//				} else {
//					System.out.println("本接口是用于找到折扣券和代金券,所以其他类型的卡券不返回");
//				}
//			}
//		}
//		return returnWxCouponList;
//	}
//
//	private List<WxCoupon> checkWxCouponListStatus(List<WxCoupon> wxCouponList) {
//		List<WxCoupon> returnWxCouponList = new ArrayList<WxCoupon>();
//		for (int i = 0; i < wxCouponList.size(); i++) {
//			WxCoupon wxCoupon = wxCouponList.get(i);
//			Map<String, Object> retrieve1Param = wxCoupon.getHttpRetrieve1Param(BaseWxBO.CASE_WxCoupon_QueryStatus, wxCoupon);
//			JSONObject json = JSONObject.fromObject(retrieve1Param);
//			//
//			JSONObject response = WxUtils.publicAccountPostToWxServer(cardCodeUrl, json.toString(), "查询优惠券code码", publicAccountAppID, publicAccountSecret, accessTokenUrl);
//			if (EnumErrorCode.EC_NoError.toString().equals(response.getString(JSON_ERROR_KEY))) {
//				String card_status = response.getJSONObject(KEY_Object).getString(WxVip.field.getFIELD_NAME_user_card_status());
//				if (CardStatus_Normal.equals(card_status)) {
//					returnWxCouponList.add(wxCoupon);
//				}
//			}
//		}
//		return returnWxCouponList;
//	}
//
//	private List<WxCoupon> retrieveNUserWxCoupon(String openID, WxAccessToken accessToken, String dbName) {
//		// 读取用户所有的卡券
//		Map<String, String> reqData = new HashMap<String, String>();
//		reqData.put(Staff.field.getFIELD_NAME_openid(), openID);
//		//
//		String url = String.format(POST_CARDRETRIEVEN_URL, accessToken.getAccessToken());
//		JSONObject postToWxServer = WxUtils.postToWxServer(url, JSONObject.fromObject(reqData).toString());
//		if (postToWxServer != null && (postToWxServer.getInt(BaseWxModel.WX_ERRCODE) == 0)) {
//			// 从用户所有的卡卷中获取属于该公司的卡券(微信开发文档说会查询到所有的卡券，但是目前测试发现仅会返回相关子商户的卡券)
//			List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
//			JSONArray jsonArray = postToWxServer.getJSONArray("card_list");
//			for (int i = 0; i < jsonArray.size(); i++) {
//				JSONObject jo = jsonArray.getJSONObject(i);
//				String cardID = jo.getString(QRCodeDistributionDetail.field.getFIELD_NAME_card_id());
//				// 查询该卡券ID是否存在本公司中
//				ErrorInfo errorInfo = new ErrorInfo();
//				VipBelongingCache vipBelongingCache = (VipBelongingCache) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_VipBelonging);
//				VipBelonging vipBelonging = vipBelongingCache.read1(cardID, errorInfo);
//				if (vipBelonging != null && dbName.equals(vipBelonging.getDbName())) {
//					WxCoupon wxCoupon = new WxCoupon();
//					wxCoupon.setCardID(cardID);
//					wxCoupon.setCode(jo.getString(WxCoupon.field.getFIELD_NAME_code()));
//					wxCouponList.add(wxCoupon);
//				}
//			}
//			return wxCouponList;
//		} else {
//			logger.error("请求微信服务器查询顾客所有的卡券失败!");
//			return null;
//		}
//	}
//
//	protected String queryUserOpenID(String authCode, String submchid) throws Exception {
//		Map<String, String> httpRetrieveNParam = new HashMap<String, String>();
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_nonce_str(), WXPayUtil.generateNonceStr());
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_auth_code(), authCode);
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_sub_mch_id(), submchid);
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_appid(), appid);
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_mch_id(), mchid);
//		httpRetrieveNParam.put(WXPayInfo.field.getFIELD_NAME_sign(), WXPayUtil.generateSignature(httpRetrieveNParam, secret, SignType.MD5));
//
//		WXPayConfigImpl config = new WXPayConfigImpl(appid, mchid, secret, cert);
//		WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, UseSandBox);
//		JSONObject xmltoJson = WxUtils.xmltoJson(wxpay.requestWithoutCert(POST_GETOPENID_URL, httpRetrieveNParam, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs()));
//		//
//		return JsonPath.read(xmltoJson, "$.xml.openid");
//	}
//
//	/** MultipartFile转File */
//	private File multipartFileToFile(MultipartFile multiFile, Map<String, Object> map) {
//		File convFile = null;
//		try {
//			if (multiFile.isEmpty()) {
//				map.put(KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureEmpty);
//				return null;
//			}
//
//			if (multiFile.getSize() > 1024 * 1024) {
//				map.put(KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureSize);
//				return null;
//			}
//
//			String fileName = multiFile.getOriginalFilename();
//			String type = fileName.substring(fileName.lastIndexOf(".") + 1);
//
//			if (PNGType.equals(type) || JPEGType.equals(type)) {
//				convFile = new File("D:\\" + (System.currentTimeMillis() / 1000) + multiFile.getOriginalFilename());
//				multiFile.transferTo(convFile);
//			} else {
//				map.put(KEY_HTMLTable_Parameter_msg, BaseWxModel.WX_ERRMSG_WXCard_PictureType);
//				return null;
//			}
//		} catch (Exception e) {
//			logger.info("MultipartFile转File异常：" + e.getMessage());
//			return null;
//		}
//
//		return convFile;
//	}
//
//	/** 删除json对象中value值为null和""的K-V对 */
//	private void collateJSONObject(JSONObject jsonObject) {
//		Iterator<?> it = jsonObject.keys();
//		while (it.hasNext()) {
//			String key = (String) it.next();
//			String value = jsonObject.getString(key);
//
//			if (jsonObject.get(key) instanceof JSONObject) {
//				JSONObject subjson = (JSONObject) jsonObject.get(key);
//
//				collateJSONObject(subjson);
//			} else {
//				if ("".equals(value) || "null".equals(value) || "0".equals(value) || "[]".equals(value) || "false".equals(value)) {
//					jsonObject.discard(key);
//					it = jsonObject.keys();
//				}
//			}
//		}
//	}
//
//	private WxCoupon createWxCouponToDB(WxCoupon wxCoupon, String dbName, int staffID) {
//		if (wxCoupon == null) {
//			logger.error("解析的优惠卷对象为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		WxCoupon wxCouponCreate = (WxCoupon) wxCouponBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCoupon);
//		if (wxCouponBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券失败。" + wxCouponBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券成功" + wxCouponCreate);
//		}
//		return wxCouponCreate;
//	}
//
//	private WxCouponDetail createWxCouponDetailToDB(WxCouponDetail wxCouponDetail, int wxCouponID, String dbName, int staffID) {
//		if (wxCouponDetail == null) {
//			logger.error("解析的优惠卷结构对象为null");
//			return null;
//		}
//		//
//		wxCouponDetail.setCouponID(wxCouponID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCouponDetail wxCouponDetailCreate = (WxCouponDetail) wxCouponDetailBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponDetail);
//		if (wxCouponDetailBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券结构失败。" + wxCouponDetailBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券结构成功" + wxCouponDetailCreate);
//		}
//		return wxCouponDetailCreate;
//	}
//
//	private WxCouponBaseInfo createWxCouponBaseInfoToDB(WxCouponBaseInfo wxCouponBaseInfo, int couponDetailID, int skuID, int dateInfoID, String dbName, int staffID) {
//		if (wxCouponBaseInfo == null) {
//			logger.error("解析的优惠卷数据对象为null");
//			return null;
//		}
//		//
//		wxCouponBaseInfo.setCouponDetailID(couponDetailID);
//		wxCouponBaseInfo.setSkuID(skuID);
//		wxCouponBaseInfo.setDateInfoID(dateInfoID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCouponBaseInfo wxCouponBaseInfoCreate = (WxCouponBaseInfo) wxCouponBaseInfoBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponBaseInfo);
//		if (wxCouponBaseInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券数据失败。" + wxCouponBaseInfoBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券数据成功" + wxCouponBaseInfoCreate);
//		}
//		return wxCouponBaseInfoCreate;
//	}
//
//	private WxSku createWxSkuToDB(WxSku wxSku, String dbName, int staffID) {
//		if (wxSku == null) {
//			logger.error("解析的优惠库存对象为null");
//			return null;
//		}
//		//
//		DataSourceContextHolder.setDbName(dbName);
//		WxSku wxSkuBOCreate = (WxSku) wxSkuBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxSku);
//		if (wxSkuBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券库存失败。" + wxSkuBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券库存成功" + wxSkuBOCreate);
//		}
//		return wxSkuBOCreate;
//	}
//
//	private WxDateInfo createWxDateInfoToDB(WxDateInfo wxDateInfo, String dbName, int staffID) {
//		if (wxDateInfo == null) {
//			logger.error("解析的优惠卷有效期对象为null");
//			return null;
//		}
//		//
//		DataSourceContextHolder.setDbName(dbName);
//		WxDateInfo wxDateInfoCreate = (WxDateInfo) wxDateInfoBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxDateInfo);
//		if (wxDateInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券有效期失败。" + wxDateInfoBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券有效期成功" + wxDateInfoCreate);
//		}
//		return wxDateInfoCreate;
//	}
//
//	private WxCouponAdvancedInfo createWxCouponAdvancedInfoToDB(WxCouponAdvancedInfo wxCouponAdvancedInfo, int wxCouponDetailID, String dbName, int staffID) {
//		if (wxCouponAdvancedInfo == null) {
//			logger.error("解析的优惠卷高级信息对象为null");
//			return null;
//		}
//		//
//		wxCouponAdvancedInfo.setCouponDetailID(wxCouponDetailID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCouponAdvancedInfo wxCouponAdvancedInfoCreate = (WxCouponAdvancedInfo) wxCouponAdvancedInfoBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponAdvancedInfo);
//		if (wxCouponBaseInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券高级信息失败。" + wxCouponBaseInfoBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券高级信息成功" + wxCouponAdvancedInfoCreate);
//		}
//		return wxCouponAdvancedInfoCreate;
//	}
//
//	private WxCouponUseCondition createWxCouponUseConditionToDB(WxCouponUseCondition wxCouponUseCondition, int wxCouponAdvancedInfoID, String dbName, int staffID) {
//		if (wxCouponUseCondition == null) { // 由于这不是必要字段，所以log.info就行了
//			logger.info("解析的优惠卷使用条件对象为null");
//			return null;
//		}
//		//
//		wxCouponUseCondition.setAdvancedInfoID(wxCouponAdvancedInfoID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCouponUseCondition wxCouponUseConditionCreate = (WxCouponUseCondition) wxCouponUseConditionBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponUseCondition);
//		if (wxCouponUseConditionBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券使用条件失败。" + wxCouponUseConditionBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券使用条件成功" + wxCouponUseConditionCreate);
//		}
//		return wxCouponUseCondition;
//	}
//
//	private WxCouponAbstract createWxCouponAbstractToDB(WxCouponAbstract wxCouponAbstract, int wxCouponAdvancedInfoID, String dbName, int staffID) {
//		if (wxCouponAbstract == null) { // 由于这不是必要字段，所以log.info就行了
//			logger.info("解析的优惠卷摘要结构对象为null");
//			return null;
//		}
//		//
//		wxCouponAbstract.setAdvancedInfoID(wxCouponAdvancedInfoID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCouponAbstract wxCouponAbstractCreate = (WxCouponAbstract) wxCouponAbstractBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponAbstract);
//		if (wxCouponAbstractBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建优惠券摘要结构失败。" + wxCouponAbstractBO.printErrorInfo());
//			return null;
//		} else {
//			logger.info("创建优惠券摘要结构成功" + wxCouponAbstractCreate);
//		}
//		return wxCouponAbstract;
//	}
//
//	private List<WxCouponTextImage> createWxCouponTextImageListToDB(List<BaseWxModel> bmList, int wxCouponAdvancedInfoID, String dbName, int staffID) {
//		if (bmList.size() == 0) { // 由于这不是必要字段，所以log.info就行了
//			logger.info("解析的优惠卷图文列表List元素为0");
//			return null;
//		}
//		//
//		List<WxCouponTextImage> wxCouponTextImageList = new ArrayList<WxCouponTextImage>();
//		for (BaseModel bm : bmList) {
//			WxCouponTextImage wxCouponTextImage = (WxCouponTextImage) bm;
//			wxCouponTextImage.setAdvancedInfoID(wxCouponAdvancedInfoID);
//			DataSourceContextHolder.setDbName(dbName);
//			WxCouponTextImage wxCouponTextImageCreate = (WxCouponTextImage) wxCouponTextImageBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponTextImage);
//			if (wxCouponTextImageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.error("创建优惠券图文列表失败。" + wxCouponTextImageBO.printErrorInfo());
//				return null;
//			} else {
//				logger.info("创建优惠券图文列表成功" + wxCouponTextImageCreate);
//				wxCouponTextImageList.add(wxCouponTextImageCreate);
//			}
//		}
//		return wxCouponTextImageList;
//	}
//
//	private List<WxCouponTimeLimit> createWxCouponTimeLimitListToDB(List<BaseWxModel> bmList, int wxCouponAdvancedInfoID, String dbName, int staffID) {
//		if (bmList.size() == 0) { // 由于这不是必要字段，所以log.info就行了
//			logger.info("解析的优惠卷图文列表List元素为0");
//			return null;
//		}
//		//
//		List<WxCouponTimeLimit> wxCouponTimeLimitList = new ArrayList<WxCouponTimeLimit>();
//		for (BaseModel bm : bmList) {
//			WxCouponTimeLimit wxCouponTimeLimit = (WxCouponTimeLimit) bm;
//			wxCouponTimeLimit.setAdvancedInfoID(wxCouponAdvancedInfoID);
//			DataSourceContextHolder.setDbName(dbName);
//			WxCouponTimeLimit wxCouponTextImageCreate = (WxCouponTimeLimit) wxCouponTimeLimitBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCouponTimeLimit);
//			if (wxCouponTimeLimitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.error("创建优惠券使用时段限制失败。" + wxCouponTimeLimitBO.printErrorInfo());
//				return null;
//			} else {
//				logger.info("创建优惠券使用时段限制成功" + wxCouponTextImageCreate);
//				wxCouponTimeLimitList.add(wxCouponTextImageCreate);
//			}
//		}
//		return wxCouponTimeLimitList;
//	}
//}