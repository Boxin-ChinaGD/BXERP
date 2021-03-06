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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("====Pos token SessionID===" + session.getId());
		if (pos.getOperationType() == 0) { // int3???0?????????login??????????????????????????????1???????????????????????????????????????
			EnumSession.clearAllSession(session);
		}
		logger.info("POS???????????????????????????,pos" + pos);

		logger.info("posBO hash1 = " + posBO.hashCode());
		RSAInfo rsa = generateRSA(String.valueOf(pos.getID()));
		logger.info("????????????????????????" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler???http??????cookie???????????????\nCookie: JSESSIONID=" + session.getId());
		session.setAttribute(EnumSession.SESSION_POS_ID.getName(), String.valueOf(pos.getID()));
		Map<String, Object> params = getDefaultParamToReturn(true);
		params.put("rsa", rsa);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
		logger.info("getTokenEx???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/** string1:???????????? */
	@RequestMapping(value = "/loginEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String loginEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("POS????????????,pos=" + pos);

		logger.info("posBO hash2 = " + posBO.hashCode());
		Object posID = session.getAttribute(EnumSession.SESSION_POS_ID.getName());
		if (posID == null) { // ???????????????ID???POS????????????????????????
			logger.info("??????POSID");
			return ""; // Hacking
		} else {
			logger.info("?????????posID=" + posID);
		}

		Pos posInOut = new Pos();
		posInOut.setID(pos.getID());
		posInOut.setResetPasswordInPos(pos.getResetPasswordInPos());

		ErrorInfo ec = new ErrorInfo();
		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromCompanyCacheBySN(pos.getCompanySN());
		if (company == null) {
			return null; // ????????????
		}
		DataSourceContextHolder.setDbName(company.getDbName());
		posInOut = (Pos) authenticate(company.getDbName(), BaseBO.INVALID_CASE_ID, posInOut, pos.getPwdEncrypted(), ec);
		if (ec.getErrorCode() == EnumErrorCode.EC_NoError && posInOut != null) {
			session.setAttribute(EnumSession.SESSION_POS.getName(), posInOut);
			Pos posReturn = (Pos) posInOut.clone();
			posReturn.clearSensitiveInfo();// ???????????????????????????????????????????????????

			params.put(KEY_Object, posReturn);
		} else {
			logger.info("pos????????????????????????=" + ec.getErrorCode());
		}

		session.removeAttribute(EnumSession.SESSION_POS_ID.getName());
		params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		logger.info("???????????????=" + params);

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
	// logger.info("??????????????????");
	// logger.info(posCreate);
	//
	// // ??????POS??????
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).write1(posCreate);
	// ErrorCode ecOut = new ErrorCode();
	// logger.info(CacheManager.getCache(dbName,EnumCacheType.ECT_POS).read1(posCreate.getID(),
	// ecOut));
	//
	// params.put("pos", posCreate);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// logger.info("----- ????????????pos -----");
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// case EC_Duplicated:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	//
	// logger.info("----- ????????????pos -----");
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
	// logger.info("??????????????????");
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????POS???,pos=" + pos);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		Map<String, Object> params = getDefaultParamToReturn(true);
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!posBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {
			params.put(KEY_HTMLTable_Parameter_msg, "????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_POS).read1(pos.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
		}
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("???????????????bm=" + (bm == null ? "" : bm));
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

		logger.info("???????????????=" + params);
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????POS???,pos=" + pos);

		Company company = new Company();
		company.setSN(pos.getCompanySN());

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		List<?> companyList = companyBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company);

		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("?????????????????????" + companyBO.printErrorInfo());
			return "";
		}

		Map<String, Object> params = getDefaultParamToReturn(true);
		if (companyList.size() == 0) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "??????????????????");
		} else {
			company = (Company) companyList.get(0);
			if (company.getDbName().equals(BaseAction.DBName_Public)) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "??????????????????");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			DataSourceContextHolder.setDbName(company.getDbName());
			Pos posR1 = (Pos) posBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.CASE_Pos_Retrieve1BySN, pos);
			if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("?????????POS_SN?????????,????????????" + companyBO.getLastErrorCode());
				}
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "???POS_SN?????????");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			if (posR1 == null) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "??????????????????POS???");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			if (StringUtils.isEmpty(posR1.getPasswordInPOS())) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("???????????????");
				}
				return "";
			}
			// ?????????????????????POS???
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("RN pos???pos=" + pos);

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

		logger.info("???????????????=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/recycleApp.bx
	 * ???ID = 1, companySN = 668866, returnSalt = 1
	 */
	@RequestMapping(value = "/recycleApp", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String recycleApp(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????APP???pos=" + pos.toString());
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("???OP??????????????????????????????App???");
			return null;
		}
		//
		Map<String, Object> params = getDefaultParamToReturn(true);
		Company companyToOperate = getCompanyFromCompanyCacheBySN(pos.getCompanySN());// ?????????????????????
		if (companyToOperate == null) {
			logger.debug(CompanyAction.ERROR_MSG_CompanyNotExist);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(KEY_HTMLTable_Parameter_msg, CompanyAction.ERROR_MSG_CompanyNotExist);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		//
		pos.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17)); // ??????16??????????????????
		String md5 = MD5Util.MD5(pos.getPasswordInPOS() + BaseAction.SHADOW);
		pos.setSalt(md5);
		//
		DataSourceContextHolder.setDbName(companyToOperate.getDbName());
		BaseModel bm = posBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_POS_RecycleApp, pos); // TODO ??????OP?????????BaseBO.SYSTEM??????BXStaffID
		switch (posBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("????????????APP?????????bm=" + (bm == null ? "" : bm));
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

		logger.info("???????????????=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	/*
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/pos/deleteEx.bx?ID=5
	 * 
	 * http://localhost:8080/nbr/pos/deleteEx.bx?ID=6 (??????????????????????????????ID???6)
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
	// logger.info("????????????");
	//
	// CacheManager.getCache(dbName,EnumCacheType.ECT_POS).delete1(pos);
	// logger.info(CacheManager.getCache(dbName,EnumCacheType.ECT_POS).readN());
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// case EC_BusinessLogicNotDefined:
	// logger.info("?????????????????????POS");
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
