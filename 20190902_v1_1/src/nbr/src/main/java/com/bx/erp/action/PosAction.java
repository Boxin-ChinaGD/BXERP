package com.bx.erp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.bo.BaseAuthenticationBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.MD5Util;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/pos")
@Scope("prototype")
public class PosAction extends AuthenticationAction {
	private final Log logger = LogFactory.getLog(PosAction.class);

	@Resource
	private PosBO posBO;

	@Resource
	private CompanyBO companyBO;

	@Override
	protected BaseAuthenticationBO getBaseAuthenticationBO() {
		return posBO;
	}

	@RequestMapping(value = "/getTokenEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String getTokenEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("====Pos token SessionID===" + session.getId());
		if (pos.getOperationType() == 0) { // int3为0代表是login操作，需要清空会话，1为其他操作，不需要清空会话
			EnumSession.clearAllSession(session);
		}
		logger.info("POS获取登录密码的公钥,pos" + pos);

		logger.info("posBO hash1 = " + posBO.hashCode());
		RSAInfo rsa = generateRSA(String.valueOf(pos.getID()));
		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
		session.setAttribute(EnumSession.SESSION_POS_ID.getName(), String.valueOf(pos.getID()));
		Map<String, Object> params = getDefaultParamToReturn(true);
		params.put("rsa", rsa);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
		logger.info("getTokenEx返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/** string1:公司编号 */
	@RequestMapping(value = "/loginEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String loginEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("POS机的登录,pos=" + pos);

		logger.info("posBO hash2 = " + posBO.hashCode());
		Object posID = session.getAttribute(EnumSession.SESSION_POS_ID.getName());
		if (posID == null) { // 检查之前此ID的POS机有无请求过验证
			logger.info("没有POSID");
			return ""; // Hacking
		} else {
			logger.info("得到的posID=" + posID);
		}

		Pos posInOut = new Pos();
		posInOut.setID(pos.getID());
		posInOut.setResetPasswordInPos(pos.getResetPasswordInPos());

		ErrorInfo ec = new ErrorInfo();
		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromCompanyCacheBySN(pos.getCompanySN());
		if (company == null) {
			return null; // 黑客行为
		}
		DataSourceContextHolder.setDbName(company.getDbName());
		posInOut = (Pos) authenticate(company.getDbName(), BaseBO.INVALID_CASE_ID, posInOut, pos.getPwdEncrypted(), ec);
		if (ec.getErrorCode() == EnumErrorCode.EC_NoError && posInOut != null) {
			session.setAttribute(EnumSession.SESSION_POS.getName(), posInOut);
			Pos posReturn = (Pos) posInOut.clone();
			posReturn.clearSensitiveInfo();// 盐值不返回给请求者以免引起安全问题

			params.put(KEY_Object, posReturn);
		} else {
			logger.info("pos登录失败，错误码=" + ec.getErrorCode());
		}

		session.removeAttribute(EnumSession.SESSION_POS_ID.getName());
		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// /*
	// Request Body in Fiddler:
	// pos_SN=SN989899&shopID=1&pwdEncrypted=123456&status=0
	// POST URL:http://localhost:8080/nbr/pos/createEx.bx
	// */
	// @RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String createEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model)
	// {
	// String md5 = MD5Util.MD5(pos.getPwdEncrypted() + BaseAction.SHADOW);
	// pos.setSalt(md5);
	//
	// Pos posCreate = (Pos) posBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pos);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (posBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).write1(posCreate);
	//
	// logger.info("创建成功！！");
	// logger.info(posCreate);
	//
	// // 更新POS缓存
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).write1(posCreate);
	// ErrorCode ecOut = new ErrorCode();
	// logger.info(CacheManager.getCache(dbName,EnumCacheType.ECT_POS).read1(posCreate.getID(),
	// ecOut));
	//
	// params.put("pos", posCreate);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// logger.info("----- 正常添加pos -----");
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// case EC_Duplicated:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	//
	// logger.info("----- 重复添加pos -----");
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// break;
	// }
	//
	// return null;
	// }

	// /*
	// Request Body in Fiddler: ID=1&pos_SN=SN7777799&shopID=1
	// POST URL:http://localhost:8080/nbr/pos/updateEx.bx
	// */
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model)
	// {
	// Pos posUpdate = (Pos) posBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pos);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (posBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).write1(posUpdate);
	//
	// logger.info("修改成功！！");
	// logger.info(posUpdate);
	//
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).write1(posUpdate);
	// ErrorCode ecOut = new ErrorCode();
	// logger.info(CacheManager.getCache(dbName,EnumCacheType.ECT_POS).read1(posUpdate.getID(),
	// ecOut));
	//
	// params.put("pos", posUpdate);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// break;
	// }
	//
	// return null;
	// }

	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个POS机,pos=" + pos);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		Map<String, Object> params = getDefaultParamToReturn(true);
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!posBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_POS).read1(pos.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
		}
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，bm=" + (bm == null ? "" : bm));
			params.put("pos", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: GET
	 * URL:http://localhost:8080/nbr/pos/retrieve1BySNEx.bx?pos_SN= xxx
	 */
	@RequestMapping(value = "/retrieve1BySNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1BySNEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个POS机,pos=" + pos);

		Company company = new Company();
		company.setSN(pos.getCompanySN());

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		List<?> companyList = companyBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company);

		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询公司失败！" + companyBO.printErrorInfo());
			return "";
		}

		Map<String, Object> params = getDefaultParamToReturn(true);
		if (companyList.size() == 0) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "该公司不存在");
		} else {
			company = (Company) companyList.get(0);
			if (company.getDbName().equals(BaseAction.DBName_Public)) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "该公司不存在");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			DataSourceContextHolder.setDbName(company.getDbName());
			Pos posR1 = (Pos) posBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.CASE_Pos_Retrieve1BySN, pos);
			if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("查询的POS_SN不存在,错误码：" + companyBO.getLastErrorCode());
				}
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "该POS_SN不存在");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			if (posR1 == null) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "公司不存在该POS机");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			if (StringUtils.isEmpty(posR1.getPasswordInPOS())) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("黑客行为！");
				}
				return "";
			}
			// 不能返回盐值到POS端
			posR1.setSalt("");
			params.put("pos", posR1);
			company.clearSensitiveInfo(EnumUserScope.STAFF);
			params.put("company", company);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/retrieveNEx.bx?ID=1
	 * http://localhost:8080/nbr/pos/retrieveNEx.bx?shopID=1
	 * http://localhost:8080/nbr/pos/retrieveNEx.bx?pos_SN=SN
	 * http://localhost:8080/nbr/pos/retrieveNEx.bx?status=1
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex() | BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("RN pos，pos=" + pos);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Pos> posRetrieveN = (List<Pos>) posBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pos);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (posBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("posRetrieveN=" + posRetrieveN);
			for (Pos pos2 : posRetrieveN) {
				pos2.setSyncDatetime(new Date());
			}
			params.put(KEY_ObjectList, posRetrieveN);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/recycleApp.bx
	 * 传ID = 1, companySN = 668866, returnSalt = 1
	 */
	@RequestMapping(value = "/recycleApp", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String recycleApp(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("重新注册APP，pos=" + pos.toString());
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("非OP帐号不能操作重新注册App！");
			return null;
		}
		//
		Map<String, Object> params = getDefaultParamToReturn(true);
		Company companyToOperate = getCompanyFromCompanyCacheBySN(pos.getCompanySN());// 要做操作的公司
		if (companyToOperate == null) {
			logger.debug(CompanyAction.ERROR_MSG_CompanyNotExist);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(KEY_HTMLTable_Parameter_msg, CompanyAction.ERROR_MSG_CompanyNotExist);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		//
		pos.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17)); // 生成16位的随机密码
		String md5 = MD5Util.MD5(pos.getPasswordInPOS() + BaseAction.SHADOW);
		pos.setSalt(md5);
		//
		DataSourceContextHolder.setDbName(companyToOperate.getDbName());
		BaseModel bm = posBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_POS_RecycleApp, pos); // TODO 重构OP权限后BaseBO.SYSTEM改成BXStaffID
		switch (posBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("重新注册APP成功，bm=" + (bm == null ? "" : bm));
			((Pos)bm).setSalt("");
			((Pos)bm).setPasswordInPOS("");
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/deleteEx.bx?ID=5
	 * 
	 * http://localhost:8080/nbr/pos/deleteEx.bx?ID=6 (刚刷新完数据库是没有ID为6)
	 */
	// @RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model)
	// {
	// posBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// pos);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (posBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).delete1(pos);
	// logger.info("删除成功");
	//
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).delete1(pos);
	// logger.info(CacheManager.getCache(dbName,EnumCacheType.ECT_POS).readN());
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// case EC_BusinessLogicNotDefined:
	// logger.info("有其他使用这台POS");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// break;
	// }
	//
	// return null;
	// }
}
