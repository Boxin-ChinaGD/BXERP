package com.bx.erp.action.wx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.action.bo.VipCardCodeBO;
import com.bx.erp.action.bo.VipSourceBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Company;
import com.bx.erp.model.Vip;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.MpQrCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.VipSource.EnumVipSourceCode;
import com.bx.erp.model.wx.WxAccessToken;
import com.bx.erp.util.AESUtil;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.WxUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/miniprogram")
@Scope("prototype")
public class MiniprogramAction extends BaseAction {
	private Log logger = LogFactory.getLog(MiniprogramAction.class);

	private final String PATH = BaseAction.QrCodePictureDir;

	@Resource
	private CompanyBO companyBO;

	@Resource
	private VipBO vipBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private VipCardCodeBO vipCardCodeBO;

	@Value("${mp.VipInfo}")
	private String vipInfo;

	@Value("${mp.CouponListBelowVipCard}")
	private String couponListBelowVipCard;

	@Value("${mp.CouponListStatus}")
	private String CouponListStatus;

	@Value("${mp.CouponListInfo}")
	private String CouponListInfo;

	@Value("${mp.SessionAndOpenid}")
	protected String SessionAndOpenid_URL;

	@Value("${mp.Ticket}")
	protected String Ticket_URL;

	@Value("${mp.DecryptCode}")
	protected String DecryptCode_URL;

	@Value("${mp.appid}")
	private String MP_APPID;

	@Value("${mp.secret}")
	private String MP_SECRET;

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${mp.mockWxServer}")
	private String mockWxServer;

	@Value("${mp.WxaCodeUnlimit}")
	private String wxaCodeUnlimit;

	@Value("${get.accesstoken.url}")
	private String accessTokenUrl;

	@Resource
	private VipSourceBO vipSourceBO;

	/** 会员属于多个商家，他要选择一个商家。 <br />
	 * 前置条件：会话中已经有 session.setAttribute(EnumSession.SESSION_VipMobile.getName());
	 * 
	 * @throws CloneNotSupportedException
	 */
	/** 登录成功后，vipSource为非null值，然后才可以选择商家。 */
	protected VipSource vipSource;

	/** 会员属于多个商家，他要选择一个商家 */

	@RequestMapping(value = "/selectMyCompanyEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String selectMyCompanyEx(@ModelAttribute("SpringWeb") Company company, HttpSession session) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Vip vipInSession = ((Vip) session.getAttribute(EnumSession.SESSION_Vip.getName()));
		if (vipInSession.getListSlave1() == null || vipInSession.getListSlave1().size() == 0) {
			return null;
		}
		VipSource vipSource = (VipSource) vipInSession.getListSlave1().get(0);
		if (vipSource == null) { // 证明登录有异常，禁止选择商家
			return null;
		}

		Map<String, Object> params = new HashMap<>();
		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		// 检查该公司中是否真的有此手机SESSION_VipMobile的会员。如果有，清除和设置vip会话和公司会话，返回OK（前端将设置相应的storage），否则是黑客行为。
		Company companyR1 = (Company) companyBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company);
		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询公司失败！错误信息：" + companyBO.printErrorInfo(BaseAction.DBName_Public, company));
			params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());

			logger.info("返回的数据=" + params);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		if (companyR1 == null) {
			return null;
		}
		// createVipSource(vipInSession.getID(), companyR1.getDbName(), vipSource); //
		// loginEx()时，必定已经创建了VipSource（极低几率失败）
		// 获取该公司下所有的门店
		Shop shop = new Shop();
		shop.setDistrictID(BaseAction.INVALID_ID);
		shop.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(companyR1.getDbName());
		List<List<BaseModel>> shopList = shopBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
		if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询公司门店失败！错误信息：" + shopBO.printErrorInfo(company.getName(), shop));
			params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());

			logger.info("返回的数据=" + params);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		// 根据手机号获取会员
		Vip vipRnCondition = new Vip();
		vipRnCondition.setMobile(vipInSession.getMobile());
		DataSourceContextHolder.setDbName(companyR1.getDbName());
		@SuppressWarnings("unchecked")
		List<Vip> vipList = (List<Vip>) vipBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vipRnCondition);
		if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipList == null || vipList.size() == 0) {
			logger.error("根据手机号查询VIP失败！错误信息：" + vipBO.printErrorInfo(companyR1.getDbName(), vipRnCondition));
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
			logger.info("返回的数据=" + params);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		VipCardCode vipCardCode = retreveNVipCardCodeByVipID(vipList.get(0).getID(), companyR1.getDbName(), params);
		if (vipCardCode == null) {
			params.put(KEY_HTMLTable_Parameter_msg, "没有查询到该会员的会员卡");
			return null;
		}
		vipList.get(0).setVipCardSN(vipCardCode.getSN());
		// 清除会话
		EnumSession.clearAllSession(session);
		// 暂时性地将VipSource放进Vip会话中，方便后面读取并创建被选择的公司的vip source
		List<VipSource> listSlave1 = new ArrayList<VipSource>();
		listSlave1.add(vipSource);
		vipList.get(0).setListSlave1(listSlave1);

		// 设置Vip会话和公司会话，但要去掉会员不能看到的敏感信息
		Company companyClone = (Company) companyR1.clone();
		companyClone.clearSensitiveInfo(EnumUserScope.VIP);
		session.setAttribute(EnumSession.SESSION_Company.getName(), companyR1);
		session.setAttribute(EnumSession.SESSION_Vip.getName(), vipList.get(0));

		params.put(BaseAction.KEY_Object, companyClone);
		params.put(BaseAction.KEY_Object2, vipList.get(0));
		params.put(BaseAction.KEY_ObjectList2, shopList.get(0));
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	private VipCardCode retreveNVipCardCodeByVipID(int vipID, String dbName, Map<String, Object> params) {
		VipCardCode queryVipCardCode = new VipCardCode();
		queryVipCardCode.setPageIndex(BaseAction.PAGE_StartIndex);
		queryVipCardCode.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		queryVipCardCode.setVipID(vipID);

		DataSourceContextHolder.setDbName(dbName);
		List<VipCardCode> vipCardCodeList = (List<VipCardCode>) vipCardCodeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, queryVipCardCode);
		if (vipCardCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipCardCodeList == null) {
			logger.error("查询会员卡！错误信息：" + vipCardCodeBO.printErrorInfo(dbName, queryVipCardCode));
			params.put(BaseAction.JSON_ERROR_KEY, vipCardCodeBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, vipCardCodeBO.getLastErrorMessage());
			logger.info("返回的数据=" + params);
			return null;
		}
		return (vipCardCodeList.size() == 0 ? null : vipCardCodeList.get(0));
	}

	/** 会员打开小程序，第1次传递密文手机，第2次及以后传递明文手机（会验证其是否与openid匹配）。无论什么时候，都会传递session_key过来。
	 * <br />
	 * 如果是会员，正常登录他所属的F_ID最大的商家。如果不是会员，创建会员并登录 object : 会员 object2 ： 当前公司信息
	 * objectList ： 公司列表 */
	@RequestMapping(value = "/loginEx", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String loginEx(@RequestBody Vip vip, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		logger.info("当前进行登录的vip=" + vip);

		String companySN = vip.getCompanySN();
		boolean needToRegister = false;

		if ("1".equals(mockWxServer)) { // 自动化测试时，根据companySN来判断是否需要创建会员
			if (StringUtils.isEmpty(companySN)) {
				companySN = SN_nbr;
				needToRegister = true;
			}
		} else {
			if (StringUtils.isEmpty(companySN)) {
				logger.error("黑客行为，没有传递公司的SN。Vip=" + vip);
				return null;
			}
		}

		Map<String, Object> params = new HashMap<>();

		if (!checkLoginParameter(vip, params)) {
			if ("0".equals(mockWxServer)) {
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
		}

		List<Company> companyList = null;
		VipSource vipSource = new VipSource();
		if ("1".equals(mockWxServer)) {
			generateVipSource(vipSource);

			companyList = queryVipCompany(vip, params);
			if (companyList == null) { // 不用判断 companyList.size() == 0
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
		} else {
			if (!resolveVipInfo(vip, params, vipSource)) {
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			companyList = queryVipCompany(vip, params);
			if (companyList == null) { // 不用判断 companyList.size() == 0
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			} else if (companyList.size() == 0) {
				logger.info("肯定是一个新会员，未注册过任何商家，可能是扫商家码，可能是搜索会员小程序直接进来，要为其注册");
				needToRegister = true;
			} else {
				logger.info("肯定是一个旧会员，但是他的openid可能有，可能没有（从NBR添加的会员是没有的）");
				needToRegister = false;
				// 检查这个会员是不是真的有这个手机和这个open id。如果不存在，当黑客行为处理。
				Company cpnQuery = new Company();
				cpnQuery.setMobile(vip.getMobile());
				cpnQuery.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex()); // TODO目前只有微信，未支持支付宝
				cpnQuery.setOpenID(vipSource.getID3());
				cpnQuery.setUnionID(vipSource.getID2());
				cpnQuery.setVipName(vip.getName());
				cpnQuery.setSex(vip.getSex());
				DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
				List<List<BaseModel>> list = companyBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.CASE_Company_matchVip, cpnQuery);
				if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error(companyBO.printErrorInfo(BaseAction.DBName_Public, cpnQuery));
					return null;
				}

				if (vipSource.getID2() != null) { // 首次登录
					for (Company company : companyList) {
						if (companySN.equals(company.getSN())) {
							// 首次登录可能更新了该会员在商家后台上创建时的信息
							CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(list.get(0).get(0), company.getDbName(), BaseBO.SYSTEM);
							break;
						}
					}
				}

				if (vip.getName() == null && vip.getSex() == EnumSexVip.ESV_Unknown.getIndex() && vipSource.getID2() == null) {// 会员A扫了商家 B的码（他不是B的会员），只发送了明文手机过来
					needToRegister = true;
					Vip matchVip = (Vip) list.get(0).get(0);
					VipSource matchVipSource = (VipSource) list.get(1).get(0);
					vip.setSex(matchVip.getSex());
					vip.setName(matchVip.getName());
					vipSource.setID2(matchVipSource.getID2());
				}

				boolean isVipOfCurrentCompany = false; // 如果是当前公司的会员，就不需要再注册
				for (Company company : companyList) {
					if (company.getSN().equals(companySN)) {
						needToRegister = false;
						isVipOfCurrentCompany = true;
						break;
					}
				}

				if (!isVipOfCurrentCompany) {
					needToRegister = true;
				}
			}
		}

		if (!doVipLogin(vip, session, params, companySN, companyList, vipSource, needToRegister)) {
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		VipSource vs = createVipSource(((Vip) session.getAttribute(EnumSession.SESSION_Vip.getName())).getID(), ((Company) session.getAttribute(EnumSession.SESSION_Company.getName())).getDbName(), vipSource);
		List<BaseModel> listSlave1 = new ArrayList<BaseModel>();
		if (vs != null) {
			listSlave1.add(vs);
			((Vip) session.getAttribute(EnumSession.SESSION_Vip.getName())).setListSlave1(listSlave1);
		} else {
			listSlave1.add(vipSource); // vs有F_ID，这个对象没有！ TODO 暂时这样做。一般不会跑进这里
			((Vip) session.getAttribute(EnumSession.SESSION_Vip.getName())).setListSlave1(listSlave1);
		}
		// 获取该公司下所有的门店
		Company company = getCompanyFromCompanyCacheBySN(companySN);
		Shop shop = new Shop();
		shop.setDistrictID(BaseAction.INVALID_ID);
		shop.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<List<BaseModel>> list = shopBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
		if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询公司门店失败！错误信息：" + shopBO.printErrorInfo(company.getName(), shop));
			params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		List<BaseModel> shopList = list.get(0);
		params.put(BaseAction.KEY_ObjectList2, shopList);
		params.put(Vip.field.getFIELD_NAME_mobile(), vip.getMobile());
		params.put(Vip.field.getFIELD_NAME_openid(), vipSource.getID3()); // 这里是小程序的openid
		params.put(Vip.field.getFIELD_NAME_unionid(), vipSource.getID2());
		params.put(KEY_HTMLTable_Parameter_msg, "");
		params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		logger.info("MiniprogramAction.loginEx() 返回的数据是：" + params);
		//
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 *小程序二维码页面跳转
	 */
	@RequestMapping(value = "/QRCodeToCreate", method = RequestMethod.GET)
	public String QRCodeToCreate(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		return MINIPROGRAM_QRCodeToCreate;
	}

	@RequestMapping(value = "/generateQRCode", produces = "plain/text; charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String generateQRCode(@ModelAttribute("SpringWeb") MpQrCode mpQrCode, ModelMap model, HttpSession session) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		Company company = getCompanyFromSession(session);
		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		mpQrCode.setScene(company.getSN());
		Map<String, Object> wxaCodeUnlimitParams = mpQrCode.getCreateParam(BaseBO.INVALID_CASE_ID, mpQrCode);
		JSONObject json = JSONObject.fromObject(wxaCodeUnlimitParams);
		//
		do {
			if (mpQrCode.getWidth() < 280 || mpQrCode.getWidth() > 1280) {
				logger.info("小程序码的宽度最小 280px，最大 1280px");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
				break;
			}
			WxAccessToken wxAccessToken = WxUtils.getAccessTokenFromWxServer(false, MP_APPID, MP_SECRET, accessTokenUrl);
			if (wxAccessToken == null || StringUtils.isEmpty(wxAccessToken.getAccessToken())) {
				logger.info("微信公众号向微信POST请求Token错误！！对应的appid为：" + MP_APPID);
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
				break;
			}
			HttpClient httpClient = HttpClientBuilder.create().build();
			// 创建POST请求
			HttpPost httpPost = new HttpPost(String.format(wxaCodeUnlimit, wxAccessToken.getAccessToken()));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			//
			StringEntity entity = new StringEntity(json.toString(), "UTF-8");
			httpPost.setEntity(entity);
			try {
				HttpResponse response = httpClient.execute(httpPost);
				StatusLine status = response.getStatusLine();
				int state = status.getStatusCode();
				logger.info("请求返回:" + state + "(" + wxaCodeUnlimit + ")");
				if (state != HttpStatus.SC_OK) {
					logger.info("微信公众号向微信POST请求小程序码错误！！对应的appid为：" + MP_APPID);
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
					break;
				}
				String path = PATH + "/" + company.getDbName() + "/mp";
				if (!GeneralUtil.checkDiskSpaceAndCreateFolder(params, path)) {
					break;
				}
				File qrCodePictureDestination = new File(PATH + "/" + company.getDbName() + "/mp/" + company.getDbName() + ".jpg");
				OutputStream os = new FileOutputStream(qrCodePictureDestination);
				HttpEntity reEntity = response.getEntity();
				reEntity.writeTo(os);
				//
				StringBuilder filePath = new StringBuilder(qrCodePictureDestination.getPath().replaceAll("\\\\", "/"));
				for (int i = 0; i < CommoditySyncAction.DIR.length(); i++) {
					filePath.setCharAt(i, Character.toLowerCase(filePath.charAt(i)));// 确保下面的替换真正替换到东西
				}
				String qrCodePictureDir = filePath.toString().replaceAll(CommoditySyncAction.DIR, "/p");
				params.put("qrCodeUrl", qrCodePictureDir);
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
				params.put(KEY_HTMLTable_Parameter_msg, "");//
				params.put(BaseAction.SP_OUT_PARAM_sErrorMsg, "");
			} catch (Exception ex) {
				logger.error("向微信服务器发送Post请求出错或其它错误：" + ex.getMessage());
			}
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// create vip source(已经存在的话，会在SP内更新)如果有错，不用中途返回。
	private VipSource createVipSource(int vipID, String dbName, VipSource vipSource) {
		vipSource.setVipID(vipID);
		vipSource.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
		DataSourceContextHolder.setDbName(dbName);
		VipSource vipSourceCreated = (VipSource) vipSourceBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vipSource);
		if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipSourceCreated == null) {
			logger.error("创建会员来源失败：" + vipBO.printErrorInfo(dbName, vipSource)); // 插vip source如果有错，不用中途返回。
			return null;
		}
		return vipSourceCreated;
	}

	/** 只有测试代码用到 */
	private void generateVipSource(VipSource vipSource) throws InterruptedException {
		vipSource.setID2(String.valueOf(System.currentTimeMillis()));
		Thread.sleep(1);
		vipSource.setID3(String.valueOf(System.currentTimeMillis()));
	}

	/** 根据手机，查询这个用户所属的公司。有可能没有公司 */
	@SuppressWarnings("unchecked")
	private List<Company> queryVipCompany(Vip vip, Map<String, Object> params) {
		Company companyRnCondition = new Company();
		companyRnCondition.setQueryKeyword(vip.getMobile());
		companyRnCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		companyRnCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		List<Company> companyList = (List<Company>) companyBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Company_retrieveNByVipMobile, companyRnCondition);
		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询所有公司失败！" + companyBO.printErrorInfo(BaseAction.DBName_Public, companyRnCondition));
			params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());

			logger.info("返回的数据=" + params);
			return null;
		}
		return companyList;
	}

	/** 处理会员登录。如果不是会员，令其成为会员
	 * 
	 * @param companyList
	 *            会员所属的公司。可能为空list，代表不是会员，而是普通顾客。
	 * @throws CloneNotSupportedException
	 * @needToRegister 代表需要注册到公司companySN。他有可能是其它公司的会员 */

	@SuppressWarnings("unchecked")
	private boolean doVipLogin(Vip vip, HttpSession session, Map<String, Object> params, String companySN, List<Company> companyList, VipSource vs, boolean needToRegister) throws CloneNotSupportedException {
		Company vip_company = getCompanyFromCompanyCacheBySN(companySN);
		if (vip_company == null) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError); // 黑客行为
			params.put(KEY_HTMLTable_Parameter_msg, "");
			return false;
		}
		if (!needToRegister) { // 该vip是会员
			// 根据手机号获取会员
			Vip vipRnCondition = new Vip();
			vipRnCondition.setMobile(vip.getMobile());
			DataSourceContextHolder.setDbName(vip_company.getDbName());
			List<Vip> vipList = (List<Vip>) vipBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vipRnCondition);
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipList == null || vipList.size() == 0) {
				logger.error("根据手机号查询VIP失败！" + vipBO.printErrorInfo(vip_company.getDbName(), vipRnCondition));
				params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				logger.info("返回的数据=" + params);
				return false;
			}
			if (companyList.size() == 1) {
				VipCardCode vipCardCode = retreveNVipCardCodeByVipID(vipList.get(0).getID(), vip_company.getDbName(), params);
				if (vipCardCode == null) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "没有查询到该会员的会员卡");
					return false;
				}
				vipList.get(0).setVipCardSN(vipCardCode.getSN());
				EnumSession.clearAllSession(session);// 清除会话

				// 设置Vip会话和公司会话，但要去掉会员不能看到的敏感信息
				Company companyCloned = (Company) vip_company.clone();
				companyCloned.clearSensitiveInfo(EnumUserScope.VIP);
				session.setAttribute(EnumSession.SESSION_Company.getName(), vip_company);
				session.setAttribute(EnumSession.SESSION_Vip.getName(), vipList.get(0));

				params.put(BaseAction.KEY_Object, vipList.get(0));
				params.put(BaseAction.KEY_Object2, companyCloned);
			} else { // 会员有多个商家，返回商家列表给前端放菜单中，让会员可以在前端切换商家
				session.setAttribute(EnumSession.SESSION_Company.getName(), vip_company.clone());
				// 暂时性地将VipSource放进Vip会话中，方便后面读取并创建被选择的公司的vip source
				// List<VipSource> listSlave1 = new ArrayList<VipSource>();
				// listSlave1.add(vs);
				// vipList.get(0).setListSlave1(listSlave1);
				//
				VipCardCode vipCardCode = retreveNVipCardCodeByVipID(vipList.get(0).getID(), vip_company.getDbName(), params);
				if (vipCardCode == null) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "没有查询到该会员的会员卡");
					return false;
				}
				vipList.get(0).setVipCardSN(vipCardCode.getSN());
				session.setAttribute(EnumSession.SESSION_Vip.getName(), vipList.get(0));
				for (Company cpn : companyList) {
					cpn.clearSensitiveInfo(EnumUserScope.VIP);
				}
				params.put(BaseAction.KEY_ObjectList, companyList); // 让前端显示N个公司给会员选择。会员选择一个公司后，就设置其vip会话和公司会话
				// 一会员多商家，前端会帮会员选择默认商家(ID最大的商家)，所以需要返回会员信息
				params.put(BaseAction.KEY_Object, vipList.get(0));
			}
		} else { // 处理不是会员的情况
			// 让此顾客成为公司companySN的会员。创建会员和会员 来源。
			Vip vipGet = new Vip();
			vipGet.setCardID(BaseAction.DEFAULT_VipCardID);
			vipGet.setMobile(vip.getMobile());
			vipGet.setName(vip.getName());
			vipGet.setSex(vip.getSex());
			vipGet.setCategory(BaseAction.DEFAULT_VipCategoryID);
			vipGet.setCreateDatetime(new Date());
			DataSourceContextHolder.setDbName(vip_company.getDbName());
			Vip vipCreate = (Vip) vipBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vipGet);
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建会员失败：" + vipBO.printErrorInfo(vip_company.getDbName(), vipGet));
				params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				return false;
			}
			CacheManager.getCache(vip_company.getDbName(), EnumCacheType.ECT_Vip).write1(vipCreate, vip_company.getDbName(), BaseBO.SYSTEM);// 加入到缓存中
			// 创建一张会员卡vipcardcode
			VipCardCode vipCardCode = new VipCardCode();
			vipCardCode.setVipID(vipCreate.getID());
			vipCardCode.setVipCardID(BaseAction.DEFAULT_VipCardID);
			vipCardCode.setCompanySN(vip_company.getSN());
			DataSourceContextHolder.setDbName(vip_company.getDbName());
			VipCardCode vipCardCodeCreated = (VipCardCode) vipCardCodeBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vipCardCode);
			if (vipCardCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipCardCodeCreated == null) {
				logger.error("创建一张会员卡失败：" + vipCardCodeBO.printErrorInfo(vip_company.getDbName(), vipCardCode));
				params.put(BaseAction.JSON_ERROR_KEY, vipCardCodeBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				return false;
			}
			vipCreate.setVipCardSN(vipCardCodeCreated.getSN());

			EnumSession.clearAllSession(session);// 清除会话

			// 设置Vip会话和公司会话，清除会员能看到的敏感信息
			Company companyClone = (Company) vip_company.clone();
			companyClone.clearSensitiveInfo(EnumUserScope.VIP);
			session.setAttribute(EnumSession.SESSION_Company.getName(), vip_company);
			session.setAttribute(EnumSession.SESSION_Vip.getName(), vipCreate);

			if (companyList.size() == 0) {
				params.put(BaseAction.KEY_Object, vipCreate);
				params.put(BaseAction.KEY_Object2, companyClone);
			} else {
				companyList.add(companyClone);
				for (Company cpn : companyList) {
					cpn.clearSensitiveInfo(EnumUserScope.VIP);
				}
				params.put(BaseAction.KEY_Object, vipCreate);
				params.put(BaseAction.KEY_ObjectList, companyList); // 让前端显示N个公司给会员选择。会员选择一个公司后，就设置其vip会话和公司会话
			}
		}

		// TODO 小程序需要查单，设置此会话可以模拟(假装)是从Web请求零售单
		Pos pos = new Pos();
		pos.setID(BaseAction.INVALID_POS_ID);
		session.setAttribute(EnumSession.SESSION_POS.getName(), pos);

		return true;
	}

	/** 会员第1次登录，要传递加密的手机号码过来。其它时候传递明文手机号码过来。如果传递的是明文，call完本函数后，需要确认该会员的确是这个手机号码 */
	private boolean resolveVipInfo(Vip vip, Map<String, Object> params, VipSource vipSource) {
		// 向微信请求sessionKey
		String sessionAndOpenidURL = String.format(SessionAndOpenid_URL, MP_APPID, MP_SECRET, vip.getDecryptSessionCode());
		JSONObject jsonObject = WxUtils.getDataFromWxServer(sessionAndOpenidURL);
		if (jsonObject == null) {
			logger.error("获取openid和session_key失败！！！");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "获取微信session_key失败!");
			return false;
		}
		String sessionKey = jsonObject.getString("session_key"); // ...
		if (StringUtils.isEmpty(sessionKey)) {
			logger.error("获取session_key失败!");
			params.put(KEY_HTMLTable_Parameter_msg, "获取session_key失败!");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		} else {
			logger.debug("获取的sessionKey:" + sessionKey);
		}

		// 获取openid
		String openid = jsonObject.getString("openid"); // ...
		if (StringUtils.isEmpty(openid)) {
			logger.error("获取openid失败!");
			params.put(KEY_HTMLTable_Parameter_msg, "获取openid失败!");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		} else {
			logger.debug("获取的openid:" + openid);
			vipSource.setID3(openid);
		}

		// 解密手机号
		if (vip.getEncryptedPhone() == null || vip.getMobile() == null) {
			logger.error("手机号码和加密的手机号码只需要发送一个，但都不能为null！");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		} else if (vip.getEncryptedPhone().length() > 0 && vip.getMobile().length() > 0) {
			logger.error("用户发送了手机号码，又发送了加密的手机号码！只需要发送一个！");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		} else {
			if (vip.getEncryptedPhone().length() > 0) {
				String mobile = decryptData(vip.getEncryptedPhone(), vip.getIvPhone(), sessionKey).getString("phoneNumber");
				if (StringUtils.isEmpty(mobile) || mobile.length() != FieldFormat.LENGTH_Mobile) {
					logger.error("解密手机号失败!");
					params.put(KEY_HTMLTable_Parameter_msg, "网络异常，请稍后再试！");
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					return false;
				} else {
					logger.debug("解密手机号成功!");
					vip.setMobile(mobile);

					// 解密UnionID
					JSONObject resultUnionID = decryptData(vip.getEncryptedUnionID(), vip.getIvUnionID(), sessionKey);
					vip.setName(resultUnionID.getString("nickName"));
					int sex = Integer.parseInt(resultUnionID.getString("gender"));
					if (sex == EnumSexVip.ESV_Female.getIndex()) {
						vip.setSex(EnumSexVip.ESV_Female.getIndex());
					} else if (sex == EnumSexVip.ESV_Male.getIndex()) {
						vip.setSex(EnumSexVip.ESV_Male.getIndex());
					} else {
						vip.setSex(EnumSexVip.ESV_Unknown.getIndex());
					}
					String mpAppID = JSONObject.fromObject(resultUnionID.get("watermark")).getString("appid");
					if (StringUtils.isEmpty(mpAppID) || !MP_APPID.equals(mpAppID)) {
						logger.error("解密获得的appID跟小程序的appID不一样");
						params.put(KEY_HTMLTable_Parameter_msg, "网络异常，请稍后再试！");
						params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
						return false;
					}
					String unionId = resultUnionID.getString("unionId");
					if (StringUtils.isEmpty(unionId)) {
						logger.error("解密unionId失败!");
						params.put(KEY_HTMLTable_Parameter_msg, "网络异常，请稍后再试！");
						params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
						return false;
					} else {
						logger.debug("解密unionId成功!");
						vipSource.setID2(unionId);
					}
				}
			} else { // if (vip.getMobile().length() > 0) // 会员A扫了商家 B的码，只发送了明文手机过来
				// 显式地设置值，在函数外面再搜索其它商家中此会员的信息，然后设置相应的值
				vip.setName(null);
				vip.setSex(EnumSexVip.ESV_Unknown.getIndex());
				vipSource.setID2(null);
			}
		}

		return true;
	}

	private boolean checkLoginParameter(Vip vip, Map<String, Object> params) {
		boolean parameterInvalid = false;
		do {
			if (StringUtils.isEmpty(vip.getDecryptSessionCode())) {
				parameterInvalid = true;
				break;
			}
			// 会员第1次之后登录
			if (StringUtils.isEmpty(vip.getEncryptedUnionID()) || StringUtils.isEmpty(vip.getIvUnionID()) || StringUtils.isEmpty(vip.getEncryptedPhone()) || StringUtils.isEmpty(vip.getIvPhone())) {
				if (StringUtils.isEmpty(vip.getMobile())) {
					parameterInvalid = true;
					break;
				}
			} else { // 会员第1次登录
				if ("1".equals(mockWxServer)) {
					if (!StringUtils.isEmpty(vip.getMobile())) {
						parameterInvalid = true;
						break;
					}
				}
			}
		} while (false);
		if (parameterInvalid) {
			params.put(KEY_HTMLTable_Parameter_msg, "无效的参数");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			return false;
		}

		if (StringUtils.isEmpty(MP_APPID) || StringUtils.isEmpty(MP_SECRET)) {
			params.put(KEY_HTMLTable_Parameter_msg, "appid或secret无效！请检查配置文件!");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			return false;
		}
		return true;
	}

	private JSONObject decryptData(String encryptedData, String ivData, String session_key) {
		byte[] encrypted = Base64.decodeBase64(encryptedData);
		byte[] iv = Base64.decodeBase64(ivData);
		byte[] key = Base64.decodeBase64(session_key);
		AESUtil aesUtil = new AESUtil(key, iv);
		return JSONObject.fromObject(aesUtil.decrypt(encrypted));
	}

	/** 用于微信小程序用户领取卡券 */
	// @RequestMapping(value = "/receiveCardEx", method = { RequestMethod.POST },
	// produces = "text/plain;charset=UTF-8")
	// @ResponseBody
	// public String receiveCardEx(@RequestBody Vip vip, HttpSession session) {
	// Map<String, Object> params = new HashMap<>();

	// // 获取accessToken
	// WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false,
	// PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// if (accessToken == null || StringUtils.isEmpty(accessToken.getAccessToken()))
	// {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取accessToken失败!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// // TODO
	// //
	// 这里需要处理api.ticket的存储问题，api.ticket跟accessToken一样的机制，微信服务器不会每次请求都返回给开发者，有效时间为2h，失效后在获取。
	// String ticketURL = String.format(Ticket_URL, accessToken.getAccessToken());
	// // 向微信端发送请求，并返回JSON数据
	// JSONObject jsonObject = WxUtils.getDataFromWxServer(ticketURL);
	// if (jsonObject == null) {
	// logger.error("获取Ticket失败！！！");
	// return null;
	// }
	// //
	// String ticket = jsonObject.getString("ticket");
	// Integer wxErrCode = (Integer) jsonObject.get(BaseWxModel.WX_ERRCODE);
	// if (wxErrCode != BaseModel.EnumBoolean.EB_NO.getIndex() ||
	// StringUtils.isEmpty(ticket)) {
	// logger.error("请求微信Ticket发生错误，错误码：" + wxErrCode);
	// params.put(KEY_HTMLTable_Parameter_msg, "获取ticket失败!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.debug("获取的ticket:->" + ticket);
	// }
	// //
	// String openid = (String)
	// session.getAttribute(EnumSession.SESSION_VipMiniProgramOpenID.getName());
	// if (StringUtils.isEmpty(openid)) {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取openid失败!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	//
	// String nonceStr = WXPayUtil.generateNonceStr();
	// String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
	// // 添加需要进行签名的参数
	// List<String> signParams = new ArrayList<String>();
	// signParams.add(vip.getMemberCardID()); // 会员卡ID
	// signParams.add(ticket); // api_ticket
	// signParams.add(nonceStr); // 随机字符
	// signParams.add(timestamp); // 领取会员卡的时间戳
	// signParams.add(openid); // 用户的openid
	// // 将signParams按字母重新排序
	// Collections.sort(signParams);
	// StringBuilder stringForSign = new StringBuilder();
	// for (String str : signParams) {
	// stringForSign.append(str);
	// }
	// // 获取签名
	// String signatureData = SHA1Util.SHA1(stringForSign.toString());
	//
	// params.put("ticket", ticket);
	// params.put("openid", openid);
	// params.put("nonceStr", nonceStr);
	// params.put("timestamp", timestamp);
	// params.put("signature", signatureData);
	// params.put(Vip.field.getFIELD_NAME_memberCardID(), vip.getMemberCardID()); //
	// 自动激活
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/** 用于小程序解密code */
	// @RequestMapping(value = "/decryptCodeEx", method = { RequestMethod.POST },
	// produces = "text/plain;charset=UTF-8")
	// @ResponseBody
	// public String decryptCodeEx(@RequestBody Vip vip, HttpSession session) {
	// Map<String, Object> params = new HashMap<>();
	// WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false,
	// PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// if (accessToken == null || StringUtils.isEmpty(accessToken.getAccessToken()))
	// {
	// params.put(KEY_HTMLTable_Parameter_msg, "获取accessToken失败!");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.debug("获取到的token:->" + accessToken.getAccessToken());
	// }
	//
	// JSONObject decryptedCodeParams = new JSONObject();
	// decryptedCodeParams.put("encrypt_code", vip.getEncryptedCode());
	// String decryptCodeURL = String.format(DecryptCode_URL,
	// accessToken.getAccessToken());
	// JSONObject jsonObject = WxUtils.postToWxServer(decryptCodeURL,
	// decryptedCodeParams.toString());
	// // ... TODO 让小程序把会员卡ID也传递过来,以确定我是属于哪个商家的会员(PEF-178实现后才能做）。
	// String code = jsonObject.getString("code");
	// Integer wxErrCode = (Integer) jsonObject.get(BaseWxModel.WX_ERRCODE);
	// if (jsonObject == null || wxErrCode != BaseModel.EnumBoolean.EB_NO.getIndex()
	// || StringUtils.isEmpty(code)) {
	// logger.error("向微信服务器请求会员卡的code发生错误，错误码：" + wxErrCode);
	// params.put(KEY_HTMLTable_Parameter_msg, "获取会员卡的code失败！！！");
	// params.put(JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// // 运行的情况可能是:1、当前用户不是会员并且通过分享的途径进入小程序，2、未知情况
	// // TODO
	// //
	// 当成功获取解密后的会员卡code时，需要往nbr插入会员信息(T_Vip、T_WxVip、T_WxVipCard、T_WxVipCardDetail、T_WxVipCardBaseInfo)
	// //
	// params.put("code", code);
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
}
