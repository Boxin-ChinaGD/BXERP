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
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.CompanyBO;
//import com.bx.erp.action.bo.wx.WxPrimaryCategoryBO;
//import com.bx.erp.action.bo.wx.WxSecondaryCategoryBO;
//import com.bx.erp.action.bo.wx.WxSubMchBO;
//import com.bx.erp.cache.CacheManager;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.BaseModel.EnumBoolean;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.BaseWxModel;
//import com.bx.erp.model.wx.WxAccessToken;
//import com.bx.erp.model.wx.WxPrimaryCategory;
//import com.bx.erp.model.wx.WxSecondaryCategory;
//import com.bx.erp.model.wx.WxSubMch;
//import com.bx.erp.model.wx.WxSubMchField;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//import com.bx.erp.util.WxUtils;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

//@Controller
//@RequestMapping("/wxSubMch")
//public class WxSubMchAction extends BaseAction {
//	private static Log logger = LogFactory.getLog(WxSubMchAction.class);
//
//	public static final String JPEGType = "jpg";
//	public static final String PNGType = "png";
//
//	@Resource
//	private WxPrimaryCategoryBO wxPrimaryCategoryBO;
//
//	@Resource
//	private WxSecondaryCategoryBO wxSecondaryCategoryBO;
//
//	@Resource
//	private WxSubMchBO wxSubMchBO;
//
//	@Resource
//	private CompanyBO companyBO;
//
//	@Value("${public.account.submch.appid}")
//	private String PUBLIC_ACCOUNT_APPID;
//
//	@Value("${public.account.submch.secret}")
//	private String PUBLIC_ACCOUNT_SECRET;
//
//	@Value("${get.accesstoken.url}")
//	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口
//
//	@Value("${post.submch.create}")
//	private String POST_CREATE_URL; // 创建子商户接口的URL
//
//	@Value("${post.submch.update}")
//	private String POST_UPDATE_URL; // 创建子商户接口的URL
//
//	@Value("${get.wxCategory.retrieveN}")
//	private String GET_RETRIEVEN_URL; // 查询一二级类目接口的URL
//
//	@Value("${post.submch.upload}")
//	private String POST_UPLOAD_URL; // 上传临时素材接口的URL(注意该url默认设置上传类型为image)
//
//	@Value("${post.submch.uploadimg}")
//	protected String POST_UPLOADIMG_URL; // 上传图片素材的URL
//
//	@Value("${post.submch.retrieve1}")
//	protected String POST_RETRIEVE1_URL;// 查询单个子商户的URL
//	
//	/*
//	 * 商户信息详情页面跳转
//	 */
//	@RequestMapping(value = "/merchantDetail", method = RequestMethod.GET)
//	public String merchantDetail(ModelMap mm) {
//		mm.put("WxSubMchField", new WxSubMchField());
//		return wxSubMch_MerchantDetail;
//	}
//
//	@RequestMapping(value = "/createEx", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") WxSubMch wxSubMch, ModelMap mm, HttpSession session, HttpServletRequest req) {
//		Map<String, Object> params = new HashMap<>();
//		boolean bDeleteOldToken = false;
//		do {
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//			
//			String protocol_type = req.getParameter(WxSubMch.field.getFIELD_NAME_protocol_type());
//			//无公章授权函需要后端代替商户向微信提交营业执照
//			if(Integer.valueOf(protocol_type) == EnumBoolean.EB_NO.getIndex()) {
//				Company company = getCompanyFromSession(session);
//				if(StringUtils.isEmpty(company.getBusinessLicensePicture())){
//					params.put(KEY_HTMLTable_Parameter_msg, "未能找到营业执照,请联系博昕科技并提供相关的营业执照照片！");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
//					break;
//				}
//				
//				File file = new File(company.getBusinessLicensePicture());
//				if(!file.exists()) {
//					params.put(KEY_HTMLTable_Parameter_msg, "营业执照查找失败，请联系博昕科技！");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
//					break;
//				}
//				
//				String fileName = file.getName();
//				String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
//				if(!JPEGType.equals(fileType)) {
//					params.put(KEY_HTMLTable_Parameter_msg, "创建子商户需要的营业执照图片不符合规定，请联系博昕科技！");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//					break;
//				}
//				
//				String url = String.format(POST_UPLOAD_URL, accessToken.getAccessToken(), fileType);
//				JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
//				file.delete();
//				if (response == null) {
//					logger.info("向微信POST请求上传营业执照错误！！");
//					params.put(KEY_HTMLTable_Parameter_msg, "创建子商户所需的营业执照上传失败");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//					break;
//				}
//				wxSubMch.setAgreement_media_id(response.getString(WxSubMch.field.getFIELD_NAME_ALIAS_protocol()));
//			}
//			
//			//请求创建子商户
//			String url = String.format(POST_CREATE_URL, accessToken.getAccessToken());
//			Map<String, Object> httpCreateParam = wxSubMch.getHttpCreateParam(BaseBO.INVALID_CASE_ID, wxSubMch);
//			JSONObject postToWxServer = WxUtils.postToWxServer(url, JSONObject.fromObject(httpCreateParam).toString());
//			if (postToWxServer == null) {
//				params.put(KEY_HTMLTable_Parameter_msg, "向wx服务器发送请求失败！");
//				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//				break;
//			}
//
//			try {
//				// 5、检查返回的结果
//				int errcode = (int) postToWxServer.get(BaseWxModel.WX_ERRCODE);
//				if (errcode == BaseWxModel.WX_ERRCODE_Success) {
//					WxSubMch wsm = new WxSubMch();
//					wsm = (WxSubMch) wsm.parse1(postToWxServer.getString(WxSubMch.field.getFIELD_NAME_info()));
//					if (wsm == null) {
//						logger.error("解析微信响应的数据失败,数据=" + postToWxServer.getString(WxSubMch.field.getFIELD_NAME_info()));
//						return "";
//					}
//					Company companyInSession = getCompanyFromSession(session);
//					wsm.setCompany_id(companyInSession.getID());
//					wsm.setProtocol(wxSubMch.getProtocol());
//					wsm.setAgreement_media_id(wxSubMch.getAgreement_media_id());
//					wsm.setOperator_media_id(wxSubMch.getOperator_media_id());
//					//
//					DataSourceContextHolder.setDbName(companyInSession.getDbName());
//					BaseModel createWxSubMch = wxSubMchBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wsm);
//					params.put(JSON_ERROR_KEY, wxSubMchBO.getLastErrorCode().toString());
//					params.put(KEY_HTMLTable_Parameter_msg, wxSubMchBO.getLastErrorMessage());
//					if (wxSubMchBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						break;
//					}
//					params.put(KEY_Object, createWxSubMch);
//
//					// 更新Company的MerchantID
//					Company company = new Company();
//					company.setID(companyInSession.getID());
//					company.setMerchantID(wsm.getMerchant_id());
//					DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
//					Company updateCompany = (Company) companyBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_Company_UpdateMerchantID, company);
//					if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						params.put(JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
//						params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
//						break;
//					}
//					CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(updateCompany, BaseAction.DBName_Public, getStaffFromSession(session).getID());
//					
//					//更新Company会话的MerchantID
//					companyInSession.setMerchantID(wsm.getMerchant_id());
//				} else {
//					String errmsg = (String) postToWxServer.get(BaseWxModel.WX_ERRMSG);
//					logger.error("发送失败!!!错误码：" + errcode + "---错误信息：" + errmsg);
//					params.put(KEY_HTMLTable_Parameter_msg, "向wx服务器发送请求失败！");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//					break;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("创建子商户时解析wx返回的数据失败！");
//				return "";
//			}
//		} while (false);
//		return JSONObject.fromObject(params).toString();
//	}
//
//	@RequestMapping(value = "/updateEx", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
//	@ResponseBody
//	public String updateEx(@ModelAttribute("SpringWeb") WxSubMch wxSubMch, ModelMap mm, HttpSession session) {
//		Map<String, Object> params = new HashMap<>();
//		boolean bDeleteOldToken = false;
//		do {
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//
//			String url = String.format(POST_UPDATE_URL, accessToken.getAccessToken());
//			Map<String, Object> httpUpdateParam = wxSubMch.getHttpUpdateParam(BaseBO.INVALID_CASE_ID, wxSubMch);
//			JSONObject postToWxServer = WxUtils.postToWxServer(url, JSONObject.fromObject(httpUpdateParam).toString());
//			if (postToWxServer == null) {
//				params.put(KEY_HTMLTable_Parameter_msg, "向wx服务器发送请求失败！");
//				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//				break;
//			}
//
//			try {
//				// 5、检查返回的结果
//				int errcode = (int) postToWxServer.get(BaseWxModel.WX_ERRCODE);
//				if (errcode == BaseWxModel.WX_ERRCODE_Success) {
//					WxSubMch wsm = new WxSubMch();
//					wsm = (WxSubMch) wsm.parse1(postToWxServer.getString(WxSubMch.field.getFIELD_NAME_info()));
//					wsm.setApp_id(wxSubMch.getApp_id());
//					wsm.setProtocol(wxSubMch.getProtocol());
//					wsm.setAgreement_media_id(wxSubMch.getAgreement_media_id());
//					wsm.setOperator_media_id(wxSubMch.getOperator_media_id());
//					//
//					Company companyInSession = getCompanyFromSession(session);
//					DataSourceContextHolder.setDbName(companyInSession.getDbName());
//					BaseModel updateWxSubMch = wxSubMchBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wsm);
//					params.put(JSON_ERROR_KEY, wxSubMchBO.getLastErrorCode().toString());
//					params.put(KEY_HTMLTable_Parameter_msg, wxSubMchBO.getLastErrorMessage());
//					if (wxSubMchBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						break;
//					}
//					params.put(KEY_Object, updateWxSubMch);
//				} else {
//					String errmsg = (String) postToWxServer.get(BaseWxModel.WX_ERRMSG);
//					logger.error("发送失败!!!错误码：" + errcode + "---错误信息：" + errmsg);
//					params.put(KEY_HTMLTable_Parameter_msg, "向wx服务器发送请求失败！");
//					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//					break;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("修改子商户时解析wx返回的数据失败！");
//				return "";
//			}
//		} while (false);
//		return JSONObject.fromObject(params).toString();
//	}
//
//	/**
//	 * 用于上传授权函，个体工商户营业执照彩照或扫描件，营业执照内登记的经营者身份证彩照或扫码件
//	 * @param session
//	 * @param multipartFile
//	 * @return
//	 */
//	@RequestMapping(value = "/uploadEx", method = RequestMethod.POST, produces = "plain/text; charset=UTF-8")
//	@ResponseBody
//	public String uploadEx(HttpSession session, @RequestParam("file") MultipartFile multipartFile) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		boolean bDeleteOldToken = false;
//		//
//		do {
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//
//			List<String> fileTypeList = new ArrayList<String>();
//			fileTypeList.add(JPEGType);
//			File file = WxUtils.multipartFileToFile(multipartFile, WxSubMch.protocolSizeMax, fileTypeList, params);
//			if (file == null) {
//				break;
//			}
//
//			String fileName = multipartFile.getOriginalFilename();
//			String type = fileName.substring(fileName.lastIndexOf(".") + 1);
//			String url = String.format(POST_UPLOAD_URL, accessToken.getAccessToken(), type);
//			JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
//
//			// 向微信上传文件请求后删除服务器上缓存的图片文件
//			file.delete();
//			if (response == null) {
//				logger.info("向微信POST请求上传临时素材错误！！");
//				break;
//			} else {
//				logger.info("上传临时素材成功！！");
//				//
//				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//				params.put(KEY_Object, response);
//				//
//				return JSONObject.fromObject(params).toString();
//			}
//		} while (false);
//
//		params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//
//		return JSONObject.fromObject(params).toString();
//	}
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/retrieveNCategoryEx", method = RequestMethod.GET, produces = "plain/text; charset=UTF-8")
//	@ResponseBody
//	public String retrieveNCategoryEx(@ModelAttribute("SpringWeb") WxPrimaryCategory wxPrimaryCategory, ModelMap mm, HttpSession session) {
//		logger.info("正在刷新一级，二级类目");
//		boolean bDeleteOldToken = false;
//		Map<String, Object> params = new HashMap<>();
//		do {
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				params.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				params.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//			JSONObject data = WxUtils.getDataFromWxServer(String.format(GET_RETRIEVEN_URL, accessToken.getAccessToken()));
//			if (data == null) {
//				logger.error("向wx服务器查询一级，二级类目请求失败");
//				return "";
//			}
//			// 解析wx服务器返回的数据。获取到一级二级类目。
//			JSONArray jsonArray = data.getJSONArray("category");
//			WxPrimaryCategory category = new WxPrimaryCategory();
//			List<BaseModel> categoryList = category.parseN(jsonArray);
//			if (categoryList == null) {
//				// 解析失败
//				logger.error("->类目数据格式解析错误!");
//				break;
//			}
//
//			// 将从微信方获取到的一级，二级类目插入DB
//			String dbName = getCompanyFromSession(session).getDbName();
//			List<BaseModel> returnWxPrimaryCategoryList = new ArrayList<BaseModel>();
//			for (BaseModel baseModel : categoryList) {
//				WxPrimaryCategory wxCategory = (WxPrimaryCategory) baseModel;
//				DataSourceContextHolder.setDbName(dbName);
//				BaseModel createWxCategory = wxPrimaryCategoryBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxCategory);
//				if (wxPrimaryCategoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//					params.put(BaseAction.JSON_ERROR_KEY, wxPrimaryCategoryBO.getLastErrorCode());
//					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxPrimaryCategoryBO.getLastErrorMessage());
//					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//				}
//				// 创建从表
//				List<BaseModel> returnWxSecondaryCategoryList = new ArrayList<BaseModel>();
//				List<WxSecondaryCategory> wxSecondaryCategoryList = (List<WxSecondaryCategory>) wxCategory.getListSlave1();
//				for (WxSecondaryCategory wxSecondaryCategory : wxSecondaryCategoryList) {
//					wxSecondaryCategory.setPrimaryCategoryID(createWxCategory.getID());
//					DataSourceContextHolder.setDbName(dbName);
//					BaseModel createWxSecondaryCategory = wxSecondaryCategoryBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxSecondaryCategory);
//					if (wxSecondaryCategoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//						params.put(BaseAction.JSON_ERROR_KEY, wxSecondaryCategoryBO.getLastErrorCode());
//						params.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxSecondaryCategoryBO.getLastErrorMessage());
//						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//					}
//					returnWxSecondaryCategoryList.add(createWxSecondaryCategory);
//				}
//				createWxCategory.setListSlave1(returnWxSecondaryCategoryList);
//				//
//				returnWxPrimaryCategoryList.add(createWxCategory);
//			}
//
//			params.put(KEY_ObjectList, returnWxPrimaryCategoryList);
//			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, wxPrimaryCategoryBO.getLastErrorMessage());
//		} while (false);
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	@RequestMapping(value = "/uploadPictureEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	public String uploadPicture(HttpSession session, @RequestParam("file") MultipartFile multipartFile) throws IOException {
//		Map<String, Object> map = new HashMap<>();
//		boolean bDeleteOldToken = false;
//		//
//		do {
//			WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(bDeleteOldToken, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//			if (accessToken == null) {
//				map.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//				map.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//				break;
//			}
//
//			List<String> fileTypeList = new ArrayList<String>();
//			fileTypeList.add(JPEGType);
//			fileTypeList.add(PNGType);
//			File file = WxUtils.multipartFileToFile(multipartFile, WxSubMch.logoSizeMax, fileTypeList, map);
//			if (file == null) {
//				break;
//			}
//
//			String url = String.format(POST_UPLOADIMG_URL, accessToken.getAccessToken());
//			JSONObject response = WxUtils.postFileToWxServer(url, "buffer", file);
//
//			// 向微信上传文件请求后删除服务器上缓存的图片文件
//			file.delete();
//			if (response == null) {
//				logger.info("向微信POST请求上传子商户LOGO错误！！");
//				break;
//			} else {
//				logger.info("上传子商户LOGO成功！！");
//				map.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
//				map.put(KEY_Object, response);
//				return JSONObject.fromObject(map).toString();
//			}
//		} while (false);
//		map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
//		return JSONObject.fromObject(map).toString();
//	}
//
//	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = { RequestMethod.GET })
//	@ResponseBody
//	public String retrieve1Ex(@ModelAttribute("SpringWeb") WxSubMch wxSubMch, ModelMap mm, HttpSession session) {
//		Map<String, Object> map = new HashMap<>();
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//		do {
//			// 因为该接口是网页一打开就要显示子商户详情，页面并不知道传哪个ID，所以从公司中的merchantID中查询子商户
//			WxSubMch wxSubMchQuery = new WxSubMch();
//			wxSubMchQuery.setMerchant_id(company.getMerchantID());
//			DataSourceContextHolder.setDbName(dbName);
//			WxSubMch WxSubMchR1 = (WxSubMch) wxSubMchBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxSubMchQuery);
//			if (wxSubMchBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				map.put(BaseAction.JSON_ERROR_KEY, wxSubMchBO.getLastErrorCode());
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxSubMchBO.getLastErrorMessage());
//				break;
//			}
//			if (WxSubMchR1 == null) {
//				return "";
//			}
//			map.put(KEY_Object, WxSubMchR1);
//
//			WxPrimaryCategory wxPrimaryCategory = new WxPrimaryCategory();
//			wxPrimaryCategory.setID(WxSubMchR1.getPrimary_category_id());
//			DataSourceContextHolder.setDbName(dbName);
//			WxPrimaryCategory wxPrimaryCategoryR1 = (WxPrimaryCategory) wxPrimaryCategoryBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxPrimaryCategory);
//			if (wxPrimaryCategoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				map.put(BaseAction.JSON_ERROR_KEY, wxPrimaryCategoryBO.getLastErrorCode());
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxPrimaryCategoryBO.getLastErrorMessage());
//				break;
//			}
//			map.put(KEY_Object2, wxPrimaryCategoryR1);
//
//			WxSecondaryCategory wxSecondaryCategory = new WxSecondaryCategory();
//			wxSecondaryCategory.setID(WxSubMchR1.getSecondary_category_id());
//			DataSourceContextHolder.setDbName(dbName);
//			WxSecondaryCategory wxSecondaryCategoryR1 = (WxSecondaryCategory) wxSecondaryCategoryBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxSecondaryCategory);
//			if (wxSecondaryCategoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				map.put(BaseAction.JSON_ERROR_KEY, wxSecondaryCategoryBO.getLastErrorCode());
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxSecondaryCategoryBO.getLastErrorMessage());
//				break;
//			}
//			map.put(KEY_Object3, wxSecondaryCategoryR1);
//			map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		} while (false);
//		return JSONObject.fromObject(map).toString();
//	}
//
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
//	@ResponseBody
//	public String deleteEx(@ModelAttribute("SpringWeb") WxSubMch wxSubMch, ModelMap mm, HttpSession session) {
//		Map<String, Object> map = new HashMap<>();
//		do {
//			// 查询是否已经取消授权
//			if (!checkWxSubMchExists(wxSubMch, map)) {
//				return JSONObject.fromObject(map).toString();
//			}
//
//			Company company = getCompayByMerchantID(wxSubMch, session);
//			if (company == null) {
//				return "";
//			}
//			// 删除子商户
//			DataSourceContextHolder.setDbName(company.getDbName());
//			wxSubMchBO.deleteObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, wxSubMch);
//			if (wxSubMchBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				map.put(BaseAction.JSON_ERROR_KEY, wxSubMchBO.getLastErrorCode());
//				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, wxSubMchBO.getLastErrorMessage());
//				break;
//			}
//
//			// 清空公司表中子商户ID
//			company.setMerchantID(0);
//			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
//			Company updateCompany = (Company) companyBO.updateObject(getBxStaffFromSession(session).getID(), BaseBO.CASE_Company_UpdateMerchantID, company);
//			if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				map.put(JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
//				map.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
//				break;
//			}
//			CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(updateCompany, BaseAction.DBName_Public, getBxStaffFromSession(session).getID());
//
//			map.put(BaseAction.JSON_ERROR_KEY, wxSubMchBO.getLastErrorCode());
//		} while (false);
//		return JSONObject.fromObject(map).toString();
//	}
//
//	private boolean checkWxSubMchExists(WxSubMch wxSubMch, Map<String, Object> map) {
//		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
//		if (accessToken == null) {
//			map.put(BaseWxModel.WX_ERRCODE, -1); // ...hardcode
//			map.put(BaseWxModel.WX_ERRMSG, "向微信服务器发送请求获取accessToken失败！");
//			return false;
//		}
//
//		String url = String.format(POST_RETRIEVE1_URL, accessToken.getAccessToken());
//		Map<String, Object> httpRetrieve1Param = wxSubMch.getHttpRetrieve1Param(BaseBO.INVALID_CASE_ID, wxSubMch);
//		JSONObject postToWxServer = WxUtils.postToWxServer(url, JSONObject.fromObject(httpRetrieve1Param).toString());
//		if (postToWxServer == null) {
//			map.put(KEY_HTMLTable_Parameter_msg, "向wx服务器发送请求失败！");
//			map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			return false;
//		}
//
//		try {
//			// 5、检查返回的结果
//			int errcode = (int) postToWxServer.get(BaseWxModel.WX_ERRCODE);
//			if (errcode == BaseWxModel.WX_ERRCODE_InvalidMerchantID) {
//				return true;
//			} else {
//				if (errcode == BaseWxModel.WX_ERRCODE_Success) {
//					//OP先登录微信公众平台进行移除子商户后才能删除DB数据
//					map.put(KEY_HTMLTable_Parameter_msg, "需要先登录微信公众平台进行移除该子商户！");
//					map.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//				} else {
//					map.put(KEY_HTMLTable_Parameter_msg, "请求微信服务器失败！！");
//					map.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//				}
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("创建子商户时解析wx返回的数据失败！");
//			return false;
//		}
//	}
//
//	private Company getCompayByMerchantID(WxSubMch wxSubMch, HttpSession session) {
//		Company company = new Company();
//		company.setMerchantID(wxSubMch.getMerchant_id());
//		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
//		Company c = (Company) companyBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.CASE_Company_Retrieve1ByMerchantID, company);
//		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			return null;
//		}
//		return c;
//	}
//}
