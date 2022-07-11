//package com.bx.erp.action.wx;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.BaseWxBO;
//import com.bx.erp.action.bo.wx.WxDateInfoBO;
//import com.bx.erp.action.bo.wx.WxSkuBO;
//import com.bx.erp.action.bo.wx.card.WxBonusRuleBO;
//import com.bx.erp.action.bo.wx.card.WxCardBO;
//import com.bx.erp.action.bo.wx.card.WxCardCustomCellBO;
//import com.bx.erp.action.bo.wx.card.WxCardCustomFieldBO;
//import com.bx.erp.action.bo.wx.card.WxCardDetailBO;
//import com.bx.erp.action.bo.wx.card.WxVipCardBaseInfoBO;
//import com.bx.erp.cache.CacheManager;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.Staff;
//import com.bx.erp.model.VipBelonging;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.BaseWxModel.EnumCardAndCouponStatus;
//import com.bx.erp.model.wx.BaseWxModel.EnumWxCardAndCouponType;
//import com.bx.erp.model.wx.QRCodeDistribution;
//import com.bx.erp.model.wx.WxAccessToken;
//import com.bx.erp.model.wx.WxDateInfo;
//import com.bx.erp.model.wx.WxSku;
//import com.bx.erp.model.wx.card.WxBonusRule;
//import com.bx.erp.model.wx.card.WxCard;
//import com.bx.erp.model.wx.card.WxCardCustomCell;
//import com.bx.erp.model.wx.card.WxCardCustomField;
//import com.bx.erp.model.wx.card.WxCardDetail;
//import com.bx.erp.model.wx.card.WxVipCardBaseInfo;
//import com.bx.erp.model.wx.card.WxVipCardRuleInfo;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//import com.bx.erp.util.WxUtils;
//
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wxVipCard")
//public class WxVipCardAction extends BaseAction {
//	private static Log logger = LogFactory.getLog(WxVipCardAction.class);
//
//	@Value("${public.account.appid}")
//	private String PUBLIC_ACCOUNT_APPID;
//
//	@Value("${public.account.secret}")
//	private String PUBLIC_ACCOUNT_SECRET;
//
//	@Value("${post.card.create}")
//	protected String card_create_url; // 创建会员卡
//
//	@Value("${post.card.detail}")
//	protected String card_detail_url; // 会员卡详情
//
//	@Value("${post.card.batchget}")
//	protected String card_retrievelist_url; // 批量查询会员卡列表
//
//	@Value("${post.card.update}")
//	protected String card_update_url; // 更改会员卡信息接口
//
//	@Value("${post.card.delete}")
//	protected String card_delete_url; // 删除会员卡接口
//
//	@Value("${post.card.qrcode}")
//	protected String card_qrcode_url; // 获取二维码ticket
//
//	@Value("${get.accesstoken.url}")
//	private String GET_ACCESSTOKEN_URL; // 微信公众号获取访问微信端的token接口
//
//	@Value("${post.card.integral}")
//	private String card_integral_url; // 查询会员积分接口
//
//	@Value("${post.card.promoteToVipAfterPaying}")
//	private String card_promoteToVipAfterPaying_url;// 支付即会员接口
//
//	// @Value("${post.card.updateVipUser}")
//	private String post_card_updateVipUser;// 更新会员信息
//
//	@Value("${post.card.uploadimg}")
//	protected String card_uploadimg_url; // 上传卡券图片素材
//
//	@Value("${third.party.appid}")
//	protected String componentAppID;
//
//	@Value("${third.party.secret}")
//	protected String componentAppSecret;
//
//	@Value("${post.ComponentAccessToken}")
//	protected String componentAccessTokenUrl;
//
//	@Value("${post.AuthorizerRefreshToekn}")
//	protected String authorizerRefreshToeknUrl;
//
//	@Value("${mp.customAppBrandUserName}")
//	protected String custom_app_brand_user_name; // 卡券跳转的小程序的user_name
//
//	@Value("${mp.customAppBrandPass}")
//	protected String custom_app_brand_pass; // 卡券跳转的小程序的path
//
//	@Resource
//	private WxCardBO wxCardBO;
//	@Resource
//	private WxCardDetailBO wxCardDetailBO;
//	@Resource
//	private WxVipCardBaseInfoBO wxVipCardBaseInfoBO;
//	@Resource
//	private WxDateInfoBO wxDateInfoBO;
//	@Resource
//	private WxSkuBO wxSkuBO;
//	@Resource
//	private WxCardCustomFieldBO wxCardCustomFieldBO;
//	@Resource
//	private WxCardCustomCellBO wxCardCustomCellBO;
//	@Resource
//	private WxBonusRuleBO wxBonusRuleBO;
//
//	public static final String JPEGType = "jpg";
//	public static final String PNGType = "png";
//	public static final int FileSizeMax = 1024 * 1024;
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
//	@RequestMapping(value = "/uploadPictureEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String uploadPicture(HttpSession session, @RequestParam("file") MultipartFile multipartFile) throws IOException {
//		Map<String, Object> params = new HashMap<String, Object>();
//		boolean bDeleteOldToken = false;
//		//
//		do {
//
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//			List<String> fileTypeList = new ArrayList<String>();
//			fileTypeList.add(JPEGType);
//			fileTypeList.add(PNGType);
//			File file = WxUtils.multipartFileToFile(multipartFile, FileSizeMax, fileTypeList, params);
//			if (file == null) {
//				break;
//			}
//
//			String url = String.format(card_uploadimg_url, accessToken.getAccessToken());
//			JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
//
//			// 向微信上传文件请求后删除服务器上缓存的图片文件
//			file.delete();
//
//			if (response == null) {
//				logger.info("向微信POST请求上传会员卡图片错误！！");
//				break;
//			} else {
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("上传会员卡图片成功！！");
//					//
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//					params.put(KEY_Object, response);
//					//
//					return JSONObject.fromObject(params).toString();
//				} else {
//					logger.info("请求上传会员卡图片错误：" + response);
//					params.put(KEY_HTMLTable_Parameter_msg, response);
//					break;
//				}
//			}
//		} while (false);
//
//		params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//
//		return JSONObject.fromObject(params).toString();
//	}
//
//	/** 创建会员卡 */
//	@RequestMapping(value = "/createWxVipCardEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createWxVIPEx(@RequestBody String postData, HttpSession session) throws CloneNotSupportedException {
//		logger.info("向微信服务器请求创建会员卡; postData:" + postData);
//
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		// 从DB中获取是否已存在wxCard数据，存在则不再给予创建(在微信端会员卡的类型都为MEMBER_CARD)
//		WxCard wxCardRetrieveNToDB = new WxCard();
//		wxCardRetrieveNToDB.setCard_type(EnumWxCardAndCouponType.EWCACT_MEMBER_CARD.getName());
//		wxCardRetrieveNToDB.setStatus(EnumCardAndCouponStatus.EWCACS_CARD_STATUS_VERIFY_OK.getName());
//		DataSourceContextHolder.setDbName(dbName);
//		List<?> wxCardList = wxCardBO.retrieveNObject(staff.getID(), BaseBO.INVALID_CASE_ID, wxCardRetrieveNToDB);
//		Map<String, Object> params = new HashMap<String, Object>();
//		if (!CollectionUtils.isEmpty(wxCardList)) {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡已存在,不可重复创建");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		//
//		WxCard wxCard = new WxCard();
//		wxCard = (WxCard) wxCard.parse1(BaseAction.INVALID_ID, postData);
//		if (wxCard == null) {
//			logger.error("解析前端传来的postData数据失败！");
//			return "";
//		}
//		WxVipCardBaseInfo wxVipCardBaseInfoFrom = wxCard.getWxCardDetail().getBase_info();
//		wxVipCardBaseInfoFrom.setCustom_app_brand_user_name(custom_app_brand_user_name);
//		wxVipCardBaseInfoFrom.setCustom_app_brand_pass(custom_app_brand_pass);
//		//
//		JSONObject jsonObject = JSONObject.fromObject(wxCard.getHttpCreateParam(BaseBO.INVALID_CASE_ID, wxCard));
//		// 除去不必要的字段
//		wxCard.cleanInvalidJSONValue(jsonObject);
//		// 向微信发送创建会员卡请求
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(card_create_url, jsonObject.toString(), "创建微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		// 判断在微信端是否创建成功
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.info("向微信发送会员卡创建失败，" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡创建失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//
//		String cardID = jsonObjectFromWx.getJSONObject(BaseAction.KEY_Object).getString(WxCard.field.getFIELD_NAME_card_id());
//
//		// 将卡券归属写入缓存中，这个时候会员卡已在WX中创建成功
//		VipBelonging vipBelonging = new VipBelonging();
//		vipBelonging.setCardID(cardID);
//		vipBelonging.setDbName(dbName);
//		CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_VipBelonging).write1(vipBelonging, BaseAction.DBName_Public, staff.getID());
//
//		// 向微信查询会员卡数据
//		WxCard wxCardToRetrieve1 = new WxCard();
//		wxCardToRetrieve1.setCard_id(cardID);
//		Map<String, Object> paramsR1 = wxCardToRetrieve1.getHttpRetrieve1Param(BaseBO.INVALID_CASE_ID, wxCardToRetrieve1);
//		JSONObject jsonFromWxR1 = WxUtils.publicAccountPostToWxServer(card_detail_url, JSONObject.fromObject(paramsR1).toString(), "查询微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		logger.info("创建微信会员卡后根据cardID查询:" + cardID + ";向微信查询的数据且为准备插入数据库的值:" + jsonFromWxR1);
//		// 在微信端创建成功获取到cardID并将微信会员卡数据写入DB
//		boolean isSuccessToCreateCard = false;
//		WxCard wxCardToDB = new WxCard();
//		wxCardToDB = (WxCard) wxCardToDB.parse1(BaseWxBO.CASE_Parse1FromWx, jsonFromWxR1.getJSONObject(BaseAction.KEY_Object).getString(WxCard.field.getFIELD_NAME_card()));
//		do {
//			wxCardToDB.setCard_id(cardID);
//			// 创建wxCard数据
//			WxCard createWxCard = createWxCardToDB(wxCardToDB, dbName, staff.getID());
//			if (createWxCard == null) {
//				break;
//			}
//			wxCardToDB.setID(createWxCard.getID());
//			// 创建微信会员卡结构体
//			WxCardDetail createWxCardDetail = createWxCardDetailToDB(wxCardToDB.getWxCardDetail(), dbName, staff.getID(), createWxCard.getID());
//			if (createWxCardDetail == null) {
//				break;
//			}
//			// 创建微信会员卡积分规则表
//			WxBonusRule createWxBonusRule = createWxBonusRuleToDB(wxCardToDB.getWxCardDetail().getBonus_rule(), dbName, staff.getID(), createWxCardDetail.getID());
//			if (createWxBonusRule == null) {
//				break;
//			}
//			// 创建微信会员卡自定义信息类目
//			WxCardCustomCell createWxCardCustomCell = createWxCardCustomCellToDB(wxCardToDB.getWxCardDetail().getCustom_cell1(), dbName, staff.getID(), createWxCardDetail.getID());
//			if (createWxCardCustomCell == null) {
//				break;
//			}
//			// 创建微信会员卡自定义信息类目
//			WxCardCustomField createWxCardCustomField = createWxCardCustomFieldToDB(wxCardToDB.getWxCardDetail().getCustom_field1(), dbName, staff.getID(), createWxCardDetail.getID());
//			if (createWxCardCustomField == null) {
//				break;
//			}
//			// 创建微信会员卡库存数据
//			WxSku createWxSku = createWxSkuToDB(wxCardToDB.getWxCardDetail().getBase_info().getSku(), dbName, staff.getID());
//			if (createWxSku == null) {
//				break;
//			}
//			// 创建微信会员卡有效期数据
//			WxDateInfo createWxDateInfo = createWxDateInfoToDB(wxCardToDB.getWxCardDetail().getBase_info().getDate_info(), dbName, staff.getID());
//			if (createWxDateInfo == null) {
//				break;
//			}
//			// 创建微信会员卡基本数据
//			WxVipCardBaseInfo wxVipCardBaseInfo = wxCardToDB.getWxCardDetail().getBase_info();
//			wxVipCardBaseInfo.setWxCardDetailID(createWxCardDetail.getID());
//			wxVipCardBaseInfo.setSkuID(createWxSku.getID());
//			wxVipCardBaseInfo.setDateInfoID(createWxDateInfo.getID());
//			WxVipCardBaseInfo wxVipCardBaseInfoToDB = createWxVipCardBaseInfoToDB(wxVipCardBaseInfo, dbName, staff.getID());
//			if (wxVipCardBaseInfoToDB == null) {
//				break;
//			}
//			isSuccessToCreateCard = true;
//		} while (false);
//
//		if (isSuccessToCreateCard) {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡创建成功");
//			params.put("card_id", cardID);
//			params.put(KEY_Object, wxCardToDB);
//		} else {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡创建失败");
//			params.put("card_id", cardID);
//		}
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/** 查询会员卡详情 */
//	@RequestMapping(value = "/retrive1WxVipCardEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrive1WxVipCardEx(@ModelAttribute("SpringWeb") WxCard wxCard, HttpSession session) {
//		logger.info("向微信服务器请求查询会员卡详情; card_id:" + wxCard.getCard_id());
//
//		if (StringUtils.isEmpty(wxCard.getCard_id())) {
//			logger.info("查询微信会员卡的card_id为空");
//			return "";
//		}
//		//
//		JSONObject jsonData = JSONObject.fromObject(wxCard.getHttpRetrieve1Param(BaseBO.INVALID_CASE_ID, wxCard));
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(card_detail_url, jsonData.toString(), "查询微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		Map<String, Object> params = new HashMap<>();
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.error("向微信请求查询微信会员卡失败：" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		WxCard wxCardToDB = new WxCard();
//		wxCardToDB = (WxCard) wxCardToDB.parse1(BaseWxBO.CASE_Parse1FromWx, jsonObjectFromWx.getJSONObject(BaseAction.KEY_Object).getString(WxCard.field.getFIELD_NAME_card()));
//		//
//		params.put(KEY_Object, wxCardToDB);
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/** 更新会员卡 */
//	@RequestMapping(value = "/updateWxVipCardEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateWxVipCardEx(@RequestBody String postData, HttpSession session) throws CloneNotSupportedException {
//		logger.info("向微信服务器请求更新会员卡; postData:" + postData);
//
//		if (StringUtils.isEmpty(postData)) {
//			logger.info("更新会员卡的数据为空");
//			return "";
//		}
//		WxCard wxCard = new WxCard();
//		wxCard = (WxCard) wxCard.parse1(BaseBO.INVALID_CASE_ID, postData);
//		if (wxCard == null) {
//			logger.error("解析前端传来的wxCard数据失败！");
//			return "";
//		}
//		// clone出一个数据以便下面插入数据库时使用
//		WxCard wxCardClone = (WxCard) wxCard.clone();
//		//
//		JSONObject jsonObjectToWx = JSONObject.fromObject(wxCard.getHttpUpdateParam(BaseBO.INVALID_CASE_ID, wxCard));
//		// 去除发向微信端不必要的字段
//		wxCard.cleanInvalidJSONValue(jsonObjectToWx);
//		// 向微信发送更新会员卡的请求
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(card_update_url, jsonObjectToWx.toString(), "更新微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		// 判断是否在微信端是否更新成功
//		Map<String, Object> params = new HashMap<>();
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.info("向微信请求更新微信会员卡失败，" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡更新失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		boolean isSuccessToUpdate = false;
//		do {
//			// 更新微信会员卡结构数据
//			WxCardDetail updateWxCardDetail = updateWxCardDetailToDB(wxCardClone.getWxCardDetail(), dbName, staff.getID(), wxCardClone.getID());
//			if (updateWxCardDetail == null) {
//				break;
//			}
//			// 更新微信会员卡的基本信息
//			WxVipCardBaseInfo updateWxVipCardBaseInfo = updateWxVipCardBaseInfoToDB(wxCardClone.getWxCardDetail().getBase_info(), dbName, staff.getID(), updateWxCardDetail.getID());
//			if (updateWxVipCardBaseInfo == null) {
//				break;
//			}
//			// 更新微信会员卡有效期数据
//			WxDateInfo updateWxDateInfo = updateWxDateInfoToDB(wxCardClone.getWxCardDetail().getBase_info().getDate_info(), dbName, staff.getID(), updateWxVipCardBaseInfo.getDateInfoID());
//			if (updateWxDateInfo == null) {
//				break;
//			}
//			isSuccessToUpdate = true;
//		} while (false);
//
//		if (isSuccessToUpdate) {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡更新成功");
//			params.put(KEY_Object, wxCard);
//		} else {
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡更新失败");
//		}
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	private WxVipCardBaseInfo updateWxVipCardBaseInfoToDB(WxVipCardBaseInfo base_info, String dbName, int staffID, int wxCardDetailID) {
//		if (base_info == null) {
//			logger.info("解析微信会员卡的基本信息数据为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		base_info.setWxCardDetailID(wxCardDetailID);
//		WxVipCardBaseInfo wxVipCardBaseInfo = (WxVipCardBaseInfo) wxVipCardBaseInfoBO.updateObject(staffID, BaseBO.INVALID_CASE_ID, base_info);
//		if (wxVipCardBaseInfo == null) {
//			logger.error("更新微信会员卡基本信息数据失败:" + wxVipCardBaseInfoBO.printErrorInfo());
//			return null;
//		}
//		logger.info("更新微信会员卡基本信息数据成功");
//		return wxVipCardBaseInfo;
//	}
//
//	private WxDateInfo updateWxDateInfoToDB(WxDateInfo date_info, String dbName, int staffID, int dateInfoID) {
//		if (date_info == null) {
//			logger.info("解析的微信会员卡有效期数据为null");
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		date_info.setID(dateInfoID);
//		WxDateInfo wxDateInfo = (WxDateInfo) wxDateInfoBO.updateObject(staffID, BaseBO.INVALID_CASE_ID, date_info);
//		if (wxDateInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("更新微信会员卡有效期数据失败:" + wxDateInfoBO.printErrorInfo());
//			return null;
//		}
//		logger.info("更新微信会员卡有效期数据成功");
//		return wxDateInfo;
//	}
//
//	private WxCardDetail updateWxCardDetailToDB(WxCardDetail member_card, String dbName, int staffID, int wxCardID) {
//		if (member_card == null) {
//			logger.info("解析微信会员卡的结构体为null");
//			return null;
//		}
//		member_card.setVipCardID(wxCardID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCardDetail wxCardDetail = (WxCardDetail) wxCardDetailBO.updateObject(staffID, BaseBO.INVALID_CASE_ID, member_card);
//		if (wxCardDetailBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("");
//			return null;
//		}
//		logger.info("更新微信会员卡结构体成功！");
//		return wxCardDetail;
//	}
//
//	/** 创建会员卡二维码 */
//	@RequestMapping(value = "/createQRCodeEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createQRCodeEx(@RequestBody String qrCode, HttpSession session) {
//		logger.info("向微信服务器请求创建会员卡二维码; qrCode:" + qrCode);
//
//		QRCodeDistribution qrCodeDistribution = new QRCodeDistribution();
//		qrCodeDistribution = (QRCodeDistribution) qrCodeDistribution.parse1(qrCode);
//		if (qrCodeDistribution == null) {
//			logger.error("解析前端传来的qrCode数据失败！");
//			return "";
//		}
//		JSONObject jsonObject = JSONObject.fromObject(qrCodeDistribution.getHttpCreateParam(BaseBO.INVALID_CASE_ID, qrCodeDistribution));
//		qrCodeDistribution.cleanInvalidJSONValue(jsonObject);
//		//
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(card_qrcode_url, jsonObject.toString(), "创建会员卡二维码", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		Map<String, Object> params = new HashMap<>();
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.info("向微信请求创建会员卡二维码失败，" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "创建微信会员卡二维码失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		params.put(KEY_HTMLTable_Parameter_msg, "创建微信会员卡二维码删成功");
//		params.put(KEY_Object, jsonObjectFromWx.getJSONObject(BaseAction.KEY_Object));
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/** 删除会员卡 */
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String deleteEx(@ModelAttribute("SpringWeb") WxCard wxCard, HttpSession session) {
//		logger.info("向微信服务器请求删除会员卡; card_id:" + wxCard.getCard_id());
//
//		if (StringUtils.isEmpty(wxCard.getCard_id())) {
//			logger.info("删除微信会员卡的card_id为空");
//			return "";
//		}
//		//
//		JSONObject jsonData = JSONObject.fromObject(wxCard.getHttpDeleteParam(BaseBO.INVALID_CASE_ID, wxCard));
//		JSONObject jsonObjectFromWx = WxUtils.publicAccountPostToWxServer(card_delete_url, jsonData.toString(), "删除会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		Map<String, Object> params = new HashMap<>();
//		if (!EnumErrorCode.EC_NoError.toString().equals(jsonObjectFromWx.get(BaseAction.JSON_ERROR_KEY))) {
//			logger.info("向微信请求删除会员卡失败，" + jsonObjectFromWx);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WechatServerError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡删除失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		DataSourceContextHolder.setDbName(dbName);
//		// 删除DB中的数据（只是修改状态）
//		wxCardBO.deleteObject(staff.getID(), BaseBO.INVALID_CASE_ID, wxCard);
//		if (wxCardBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("在微信端删除会员卡成功，在DB中删除失败：" + wxCardBO.printErrorInfo());
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡删除失败");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		//
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡删除成功");
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	@RequestMapping("/promoteToVipAfterPaying")
//	@ResponseBody
//	private String promoteToVipAfterPaying(@RequestBody String postBody) {
//		logger.info("向微信服务器请求设置支付即会员; postBody:" + postBody);
//
//		WxVipCardRuleInfo ruleInfo = new WxVipCardRuleInfo();
//		ruleInfo = (WxVipCardRuleInfo) ruleInfo.parse1(postBody);
//		if (ruleInfo == null) {
//			logger.error("解析前端传来的postBody数据失败！");
//			return "";
//		}
//
//		Map<String, Object> params = ruleInfo.getHttpCreateParam(BaseBO.INVALID_CASE_ID, ruleInfo);
//		JSONObject jsonObject = JSONObject.fromObject(params);
//		//
//		System.out.println("after clean invalid json:" + jsonObject);
//		return WxUtils.publicAccountPostToWxServer(card_promoteToVipAfterPaying_url, jsonObject.toString(), "设置支付即会员", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL).toString();
//	}
//
//	@RequestMapping(value = "/retriveNWxVipCardEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retriveNWxVipCardEx(@ModelAttribute("SpringWeb") WxCard wxCard, HttpSession session) {
//		logger.info("查询微信会员卡的信息; wxCard" + wxCard);
//
//		if (wxCard == null) {
//			logger.info("根据查询的wxCard对象为null");
//			return "";
//		}
//		// 获取dbName
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		// 获取用户信息
//		Staff staff = getStaffFromSession(session);
//		Map<String, Object> params = new HashMap<>();
//		DataSourceContextHolder.setDbName(dbName);
//		// 从DB中获取wxCard数据
//		List<?> wxCardList = wxCardBO.retrieveNObject(staff.getID(), BaseBO.INVALID_CASE_ID, wxCard);
//		if(wxCardBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			params.put(JSON_ERROR_KEY, wxCardBO.getLastErrorCode());
//			params.put(KEY_HTMLTable_Parameter_msg, wxCardBO.getLastErrorMessage());
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		
//		List<WxCard> wxCardFromWxList = new ArrayList<WxCard>();
//		if (CollectionUtils.isEmpty(wxCardList)) {
//			logger.info("未查询到微信会员卡数据");
//		} else {
//			// 向微信查询微信会员卡数据
//			for (Object object : wxCardList) {
//				WxCard wxCardToRetrieve1 = (WxCard) object;
//				// 向微信查询微信会员卡数据
//				Map<String, Object> paramsR1 = wxCardToRetrieve1.getHttpRetrieve1Param(BaseBO.INVALID_CASE_ID, wxCardToRetrieve1);
//				JSONObject jsonFromWxR1 = WxUtils.publicAccountPostToWxServer(card_detail_url, JSONObject.fromObject(paramsR1).toString(), "查询微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//				if (!EnumErrorCode.EC_NoError.toString().equals(jsonFromWxR1.getString(BaseAction.JSON_ERROR_KEY))) {
//					logger.info("出现错误的wxCard对象，" + wxCardToRetrieve1);
//					continue;
//				}
//				// 解析微信会员卡数据
//				WxCard wxCardFromWx = new WxCard();
//				wxCardFromWx = (WxCard) wxCardFromWx.parse1(BaseWxBO.CASE_Parse1FromWx, jsonFromWxR1.getJSONObject(BaseAction.KEY_Object).getString(WxCard.field.getFIELD_NAME_card()));
//				if (wxCardFromWx != null) {
//					wxCardFromWxList.add(wxCardFromWx);
//				}
//			}
//		}
//		//
//		params.put(KEY_ObjectList, wxCardFromWxList);
//		params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		params.put(KEY_HTMLTable_Parameter_msg, "微信会员卡查询成功");
//		params.put(KEY_HTMLTable_Parameter_TotalRecord, wxCardFromWxList.size());
//		params.put(KEY_HTMLTable_Parameter_code, 0);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	@RequestMapping(value = "/updateVipUser", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateVipUser(@RequestBody String postBody) {
//		logger.info("向微信服务器请求更新会员信息; postBody:" + postBody);
//
//		// WxVipCardRuleInfo ruleInfo = new WxVipCardRuleInfo();
//		// ruleInfo = (WxVipCardRuleInfo) ruleInfo.parse1(postBody);
//		// if (ruleInfo == null) {
//		// logger.error("解析前端传来的postBody数据失败！");
//		// return "";
//		// }
//		//
//		// Map<String, Object> params =
//		// ruleInfo.getHttpCreateParam(BaseBO.INVALID_CASE_ID, ruleInfo);
//		// JSONObject jsonObject = JSONObject.fromObject(params);
//		//
//		// System.out.println("after clean invalid json:" + jsonObject);
//		return WxUtils.publicAccountPostToWxServer(post_card_updateVipUser, postBody, "设置支付即会员", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL).toString();
//	}
//
//	private WxVipCardBaseInfo createWxVipCardBaseInfoToDB(WxVipCardBaseInfo base_info, String dbName, int staffID) {
//		if (base_info == null) {
//			logger.info("解析微信会员卡的基本信息对象为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		WxVipCardBaseInfo wxVipCardBaseInfo = (WxVipCardBaseInfo) wxVipCardBaseInfoBO.createObject(staffID, BaseBO.INVALID_CASE_ID, base_info);
//		if (wxVipCardBaseInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡的基本信息失败：" + wxVipCardBaseInfoBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡的基本信息成功");
//		return wxVipCardBaseInfo;
//	}
//
//	private WxDateInfo createWxDateInfoToDB(WxDateInfo date_info, String dbName, int staffID) {
//		if (date_info == null) {
//			logger.info("解析微信会员卡的有效期表对象为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		WxDateInfo wxDateInfo = (WxDateInfo) wxDateInfoBO.createObject(staffID, BaseBO.INVALID_CASE_ID, date_info);
//		if (wxDateInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡有效期表数据失败：" + wxDateInfoBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡有效期表数据成功");
//		return wxDateInfo;
//	}
//
//	private WxSku createWxSkuToDB(WxSku sku, String dbName, int staffID) {
//		if (sku == null) {
//			logger.info("解析微信会员卡的库存对象为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		WxSku wxSku = (WxSku) wxSkuBO.createObject(staffID, BaseBO.INVALID_CASE_ID, sku);
//		if (wxSkuBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡库存对象失败:" + wxSkuBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡库存对象成功");
//		return wxSku;
//	}
//
//	private WxCardCustomField createWxCardCustomFieldToDB(WxCardCustomField custom_field1, String dbName, int staffID, int wxCardDetailID) {
//		if (custom_field1 == null) {
//			logger.info("解析的微信会员卡自定义会员信息类目对象为null");
//			return null;
//		}
//		custom_field1.setMemberCardID(wxCardDetailID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCardCustomField wxCardCustomField = (WxCardCustomField) wxCardCustomFieldBO.createObject(staffID, BaseBO.INVALID_CASE_ID, custom_field1);
//		if (wxCardCustomFieldBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡自定义会员信息类目数据失败:" + wxCardCustomFieldBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡自定义会员信息类目数据成功");
//		return wxCardCustomField;
//	}
//
//	private WxCardCustomCell createWxCardCustomCellToDB(WxCardCustomCell custom_cell1, String dbName, int staffID, int wxCardDetailID) {
//		if (custom_cell1 == null) {
//			logger.info("解析的微信会员卡自定义会员信息类目表对象为null");
//			return null;
//		}
//		custom_cell1.setMemberCardID(wxCardDetailID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCardCustomCell wxCardCustomCell = (WxCardCustomCell) wxCardCustomCellBO.createObject(staffID, BaseBO.INVALID_CASE_ID, custom_cell1);
//		if (wxCardCustomCellBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡自定义会员信息类别表失败:" + wxCardCustomCellBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡自定义会员信息类别表成功");
//		return wxCardCustomCell;
//	}
//
//	private WxBonusRule createWxBonusRuleToDB(WxBonusRule wxBonusRuleToDB, String dbName, int staffID, int wxCardDetailID) {
//		if (wxBonusRuleToDB == null) {
//			logger.info("解析的微信会员卡积分规则对象为null");
//			return null;
//		}
//		wxBonusRuleToDB.setMemberCardID(wxCardDetailID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxBonusRule wxBonusRule = (WxBonusRule) wxBonusRuleBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxBonusRuleToDB);
//		if (wxBonusRuleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡积分规则失败");
//			return null;
//		}
//		logger.info("创建微信会员卡积分规则成功");
//		return wxBonusRule;
//	}
//
//	private WxCardDetail createWxCardDetailToDB(WxCardDetail wxCardDetailToDB, String dbName, int staffID, int vipCardID) {
//		if (wxCardDetailToDB == null) {
//			logger.info("解析的微信会员卡结构对象为null");
//			return null;
//		}
//		wxCardDetailToDB.setVipCardID(vipCardID);
//		DataSourceContextHolder.setDbName(dbName);
//		WxCardDetail wxCardDetail = (WxCardDetail) wxCardDetailBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCardDetailToDB);
//		if (wxCardDetailBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("创建微信会员卡结构数据失败," + wxCardDetailBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建微信会员卡结构数据成功");
//		return wxCardDetail;
//	}
//
//	private WxCard createWxCardToDB(WxCard wxCardToDB, String dbName, int staffID) {
//		if (wxCardToDB == null) {
//			logger.info("解析的微信会员卡为null");
//			return null;
//		}
//		DataSourceContextHolder.setDbName(dbName);
//		WxCard wxCard = (WxCard) wxCardBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wxCardToDB);
//		if (wxCardBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			System.out.println(wxCardBO.printErrorInfo());
//			logger.error("创建会员卡BD数据失败:" + wxCardBO.printErrorInfo());
//			return null;
//		}
//		logger.info("创建会员卡DB数据成功");
//		return wxCard;
//	}
//}
