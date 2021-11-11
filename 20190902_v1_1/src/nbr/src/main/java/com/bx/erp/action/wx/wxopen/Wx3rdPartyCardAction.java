//package com.bx.erp.action.wx.wxopen;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
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
//import com.bx.erp.action.bo.wx.coupon.WxCouponBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.WxAccessToken;
//import com.bx.erp.model.wx.card.WxVipCard;
//import com.bx.erp.model.wx.coupon.CouponStatistics;
//import com.bx.erp.model.wx.coupon.CouponStatistics.EnumCondSource;
//import com.bx.erp.model.wx.coupon.WxCoupon;
//import com.bx.erp.model.wx.wxopen.WxOpenAccessToken;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.DatetimeUtil;
//import com.bx.erp.util.JsonUtil;
//import com.bx.erp.util.WxUtils;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wx3rdPartyCard")
//@Scope("prototype")
//public class Wx3rdPartyCardAction extends WxOpenAction {
//	private static Log logger = LogFactory.getLog(Wx3rdPartyCardAction.class);
//
//	@Value("${public.account.appid}")
//	private String PUBLIC_ACCOUNT_APPID;
//
//	@Value("${public.account.secret}")
//	private String PUBLIC_ACCOUNT_SECRET;
//
//	/** 卡券图片类型 */
//	public static final String JPEGType = "jpg";
//	public static final String PNGType = "png";
//
//	public static final String CardStatus_Normal = "NORMAL";
//
//	@Resource
//	private WxCouponBO wxCouponBO;
//
//	@RequestMapping(value = "/uploadPictureEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String uploadPicture(HttpSession session, @RequestParam("file") MultipartFile multipartFile) throws IOException {
//		Map<String, Object> map = new HashMap<>();
//		//
//		do {
//			WxOpenAccessToken wxOpenAccessToken = getWxAuthorizerAccessToken(session);
//			if (wxOpenAccessToken == null || wxOpenAccessToken.getAuthorizerAccessToken() == null) {
//				logger.info("获取authorizer_access_token失败--> authorizer_access_token == null ");
//				break;
//			}
//
//			List<String> fileTypeList = new ArrayList<String>();
//			fileTypeList.add(JPEGType);
//			fileTypeList.add(PNGType);
//			int fileSizeMax = 1024 * 1024;
//			File file = WxUtils.multipartFileToFile(multipartFile, fileSizeMax, fileTypeList, map);
//			if (file == null) {
//				break;
//			}
//
//			String url = String.format(card_uploadimg_url, wxOpenAccessToken.getAuthorizerAccessToken());
//			JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
//
//			// 向微信上传文件请求后删除服务器上缓存的图片文件
//			file.delete();
//
//			if (response == null) {
//				logger.info("向微信POST请求上传卡券图片错误！！");
//				break;
//			} else {
//				if (response.get(BaseWxModel.WX_ERRCODE) == null) {
//					logger.info("上传卡券图片成功！！");
//					//
//					map.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//					map.put(KEY_Object, response);
//					//
//					return JSONObject.fromObject(map).toString();
//				} else {
//					logger.info("请求上传卡券图片错误：" + response);
//					map.put(KEY_HTMLTable_Parameter_msg, response);
//					break;
//				}
//			}
//		} while (false);
//
//		map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//
//		return JSONObject.fromObject(map).toString();
//	}
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
//		// logger.info("card=" + card);
//		// JSONObject wxCashCard = JSONObject.fromObject(card);
//		// card.parse1(wxCashCard);
//
//		return publicAccountPostToWxServer(card_create_url, postData, "创建卡券", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET).toString();
//	}
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
//	/** 设置买单 */
//	@RequestMapping(value = "/updateToWxpayEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateToWxpayEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_paycell_url, postData, "设置买单", session).toString();
//	}
//
//	/** 获取卡券详情 */
//	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieve1Ex(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_detail_url, postData, "获取卡券详情", session).toString();
//	}
//
//	/** 创建领券二维码 */
//	@RequestMapping(value = "/createQRCodeEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String createQRCodeEx(HttpSession session, @RequestBody String qrCode) {
//		return postToWxServer(card_qrcode_url, qrCode, "创建领券二维码", session).toString();
//	}
//
//	/** 核销优惠券 */
//	@RequestMapping(value = "/updateToConsumedEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateToConsumedEx(HttpSession session, @RequestBody String code) {
//		Map<String, Object> map = new HashMap<>();
//		logger.info("code=" + code);
//
//		JSONObject response = postToWxServer(card_code_url, code, "查询优惠券code码", session);
//		if (!EnumErrorCode.EC_NoError.toString().equals(response.getString(JSON_ERROR_KEY))) {
//			return response.toString();
//		}
//
//		String card_status = response.getJSONObject(KEY_Object).getString("user_card_status");
//		if (CardStatus_Normal.equals(card_status)) {
//			return postToWxServer(card_consume_url, code, "核销优惠券", session).toString();
//		} else {
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//			map.put(KEY_HTMLTable_Parameter_msg, "优惠券无效！！");
//		}
//
//		return JSONObject.fromObject(map).toString();
//	}
//
//	/** 批量查询卡券列表 */
//	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveNEx(HttpSession session, @RequestBody String postData) {
//		try {
//			JSONObject jsonCardIDs = postToWxServer(card_retrievelist_url, postData, "批量查询卡券列表", session);
//			if (!EnumErrorCode.EC_NoError.toString().equals(jsonCardIDs.getString(BaseAction.JSON_ERROR_KEY))) {
//				logger.error("批量查询卡券列表失败，微信服务器返回的数据： " + jsonCardIDs);
//				return "";
//			}
//			// 解析json,拿到ID,放入List数组
//			List<String> listIDs = new ArrayList<>();
//			JSONObject objectJson = jsonCardIDs.getJSONObject(BaseAction.KEY_Object);//
//			JSONArray jsonArr = objectJson.getJSONArray("card_id_list");//
//			for (int i = 0; i < jsonArr.size(); i++) {
//				String sID = (String) jsonArr.get(i);
//				listIDs.add(sID);
//			}
//			// 进行for循环，发送R1,得到每个优惠券的详情，放入ArrayList中
//			List<JSONObject> listCardInfo = new ArrayList<>();
//			for (int i = 0; i < listIDs.size(); i++) {
//				String postDataID = listIDs.get(i);
//				JSONObject paramsInfo = new JSONObject();
//				paramsInfo.put("card_id", postDataID);
//				// 得到优惠券详情是JSONObject.toString()
//				String cardInfo = retrieve1Ex(session, paramsInfo.toString());
//				if (cardInfo == null) {
//					// json为空如何操作
//					logger.error("向微信服务器查询失败，失败card_id为：" + paramsInfo.toString());
//					continue; // 当查询一个失败时，是否让他继续查询其他卡卷？？？
//				}
//				// 只需要返回代金券，折扣券，其他的券不返回
//				String cardType = JSONObject.fromObject(cardInfo).getJSONObject(BaseAction.KEY_Object).getJSONObject(BaseWxModel.WXCard).getString(BaseWxModel.WXCard_type);
//				if (!(BaseWxModel.WXCardType_CASH.equals(cardType) || BaseWxModel.WXCardType_DISCOUNT.equals(cardType))) {
//					logger.info("非代金券，折扣券，不需要返回，此券详情为：" + cardInfo);
//					continue;
//				}
//				JSONObject jsonCardInfo = JSONObject.fromObject(cardInfo);
//				listCardInfo.add(jsonCardInfo);
//			}
//			// 根据时间降序
//			Collections.sort(listCardInfo, new Comparator<JSONObject>() {
//				@Override
//				public int compare(JSONObject o1, JSONObject o2) {
//					return getCardCreateTime(o2).compareTo(getCardCreateTime(o1));
//				}
//			});
//			//
//			Map<String, Object> params = new HashMap<>();
//			params.put(KEY_ObjectList, listCardInfo);
//			params.put("jsonCardIDs", jsonCardIDs);
//			logger.info("返回的数据params=" + params);
//
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		} catch (Exception e) {
//			logger.error("查询卡卷列表异常，异常信息为：" + e.getMessage());
//			return null;
//		}
//	}
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
//	/** 更改卡券信息接口 */
//	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_update_url, postData, "更改卡券信息", session).toString();
//	}
//
//	/** 修改卡券库存 */
//	@RequestMapping(value = "/updateStockEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateStockEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_updatestock_url, postData, "修改卡券库存", session).toString();
//	}
//
//	/** 删除卡券 */
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String deleteEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_delete_url, postData, "删除卡券", session).toString();
//	}
//
//	/** 设置卡券失效 */
//	@RequestMapping(value = "/updateToUnavailableEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String updateToUnavailableEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_unavailable_url, postData, "设置卡券失效", session).toString();
//	}
//
//	/** 统计卡券概况数据 */
//	@RequestMapping(value = "/retrieveSummaryEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveSummaryEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_summarydata_url, postData, "统计卡券概况数据", session).toString();
//	}
//
//	/** 统计单张优惠券数据 */
//	@RequestMapping(value = "/retrieve1CardInfoEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveCardInfoEx(HttpSession session, @RequestBody String postData) {
//		return postToWxServer(card_cardinfo_url, postData, "统计单张优惠券数据", session).toString();
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
//		return publicAccountPostToWxServer(card_create_url, postData, "创建微信会员卡", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET).toString();
//	}
//
//	/** 拉取卡券概况数据
//	 * 
//	 * @throws ParseException
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/retrieveStatisticsEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	private String retrieveStatisticsEx(HttpSession session, @ModelAttribute("SpringWeb") CouponStatistics couponStatistics) {
//		logger.debug("couponStatistics:" + couponStatistics);
//		
//		couponStatistics.setCond_source(CouponStatistics.CONDITION_Source);
//
//		String checkFieldResult = couponStatistics.checkRetrieveNFromWxServer();
//		if (!"".equals(checkFieldResult)) {
//			logger.error("黑客行为。前端不会将非法参数传递过来！err=" + checkFieldResult);
//			return null;
//		}
//
//		Map<String, Object> params = new HashMap<>();
//		//
//		WxCoupon wxCounponRetrieveNCondition = new WxCoupon();
//		wxCounponRetrieveNCondition.setCard_type(couponStatistics.getCardType());
//		wxCounponRetrieveNCondition.setPageIndex(couponStatistics.getPageIndex());
//		wxCounponRetrieveNCondition.setPageSize(couponStatistics.getPageSize());
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		DataSourceContextHolder.setDbName(dbName);
//		List<BaseModel> bmListRetrieveN = (List<BaseModel>) wxCouponBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxCounponRetrieveNCondition);
//		if (wxCouponBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("查询多个优惠券失败，" + wxCouponBO.printErrorInfo());
//			params.put(BaseAction.JSON_ERROR_KEY, wxCouponBO.getLastErrorCode());
//			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxCouponBO.getLastErrorMessage());
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		} else {
//			logger.info("查询多个优惠券成功！bmListRetrieveN=" + bmListRetrieveN);
//		}
//		// get accessToken //...TODO 这里应该是拿 三方平台的Token而不是支付的TOKEN
//		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		if (accessToken == null) {
//			params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//			params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//			logger.error("向微信服务器发送请求获取accessToken失败！");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//
//		List<CouponStatistics> wxCouponStatisticsList = new ArrayList<>();
//		for (int i = 0; i < bmListRetrieveN.size(); i++) {
//			WxCoupon wxCoupon = (WxCoupon) bmListRetrieveN.get(i);
//			// 拼接json数据格式
//			JSONObject json = new JSONObject();
//			json.put("begin_date", couponStatistics.getBegin_date());
//			json.put("end_date", couponStatistics.getEnd_date());
//			json.put("cond_source", couponStatistics.getCond_source());
//			json.put("card_id", wxCoupon.getCardID());
//			JSONObject wxData = publicAccountPostToWxServer(card_retrieveStatistics_url, json.toString(), "拉取卡券概况数据", PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET);
//			String err = wxData.getString(BaseAction.JSON_ERROR_KEY);
//			if (EnumErrorCode.EC_NoError.toString().compareTo(err) != 0) {
//				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.valueOf(err));
//				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "向微信服务器发送请求失败");
//				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//			}
//			String response = wxData.getString(BaseAction.KEY_Object);
//			logger.info("response:" + response);
//			JSONObject jsonOuter = JSONObject.fromObject(response.toString());
//			JSONArray jsonArray = jsonOuter.getJSONArray("list");
//			// 根据card_id查询，list大小应为1
//			if (jsonArray.isEmpty()) {
//				logger.info("从微信查询不到卡,card_id=" + wxCoupon.getCardID());
//				continue;
//			}
//			JSONObject jsonInner = jsonArray.getJSONObject(0);
//			CouponStatistics couponStatisticsReturn = new CouponStatistics();
//			if (couponStatisticsReturn.parse1(jsonInner) != null) {
//				wxCouponStatisticsList.add(couponStatisticsReturn);
//			} else {
//				logger.error("Parse CouponStatistics出错!");
//			}
//		}
//		params.put(BaseAction.KEY_ObjectList, wxCouponStatisticsList);
//		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//}
