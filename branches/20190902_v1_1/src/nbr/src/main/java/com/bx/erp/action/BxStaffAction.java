package com.bx.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.bo.BaseAuthenticationBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.BxStaffBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

import org.apache.commons.logging.LogFactory;

//@Transactional
@Controller
@RequestMapping("/bxStaff")
@Scope("prototype")
public class BxStaffAction extends AuthenticationAction {
	private Log logger = LogFactory.getLog(BxStaffAction.class);

	@Resource
	private BxStaffBO bxStaffBO;

	@Override
	protected BaseAuthenticationBO getBaseAuthenticationBO() {
		return bxStaffBO;
	}

	/** 实现getTokenEx，实现loginEx，实现logoutEx */
	@RequestMapping(value = "/getTokenEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String getTokenEx(@ModelAttribute("SpringWeb") BxStaff bxStaff, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		EnumSession.clearAllSession(session); // 清除所有会话，防止不同浏览器登录后混淆缓存

		logger.info("获取秘钥，bxStaff=" + bxStaff);

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("----- 查询一个bxStaff -----");
		logger.info("bxStaffBO hash1 = " + bxStaffBO.hashCode());
		RSAInfo rsa = generateRSA(bxStaff.getMobile());

		session.setAttribute(EnumSession.SESSION_BxStaffMobile.getName(), bxStaff.getMobile());
		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rsa", rsa);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		params.put(KEY_HTMLTable_Parameter_msg, bxStaffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Fiddler: moile=15016509167&salt=000000 User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded Request Body
	 * in Fiddler: URL:http://localhost:8080/nbr/bxStaff/loginEx.bx
	 */
	@RequestMapping(value = "/loginEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String loginEx(@ModelAttribute("SpringWeb") BxStaff bxStaff, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("业务经理服务器的登录,bxStaff=" + bxStaff);

		// DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("bxStaffBO hash2 = " + bxStaffBO.hashCode());
		Object phone = session.getAttribute(EnumSession.SESSION_BxStaffMobile.getName());
		if (phone == null) { // 检查之前此phone的bxstaff有无请求过验证
			logger.error("没有电话号码");
			return null; // Hacking
		} else {
			logger.info("得到的phone=" + phone);
		}

		ErrorInfo eo = new ErrorInfo();
		if (!checkLoginCount(phone, eo)) {
			return null;
		}
		//
		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		for (BaseModel bm : list) {
			Company com = (Company) bm;
			if (com.getDbName().equals(BaseAction.DBName_Public)) {
				setCompanyInSession(session, com); // 将dbName保存在会话中，后面用户的ACTION请求中将可以从会话得到DB名称，从而正确地读写DB
				break;
			}
		}
		if (getCompanyFromSession(session) == null) {
			logger.error("黑客行为xxx");
			return null;
		}

		//
		BxStaff bxStaffInOut = new BxStaff();
		bxStaffInOut.setMobile(bxStaff.getMobile());
		// bxStaffInOut.setInt1(bxStaff.getInt1());
		ErrorInfo ec = new ErrorInfo();
		bxStaffInOut = (BxStaff) authenticate(getCompanyFromSession(session).getDbName(), BaseBO.CASE_Login, bxStaffInOut, bxStaff.getPwdEncrypted(), ec);

		Map<String, Object> params = new HashMap<String, Object>();
		if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
			session.removeAttribute(EnumSession.SESSION_Staff.toString());// 普通staff与bxstaff互斥,只能同时一个登陆
			session.setAttribute(EnumSession.SESSION_BxStaff.getName(), bxStaffInOut);
			logger.info("BxSTAFF登录成功。");
			// BaseBO.CURRENT_BXSTAFF = bxStaffInOut;
			// logger.info("BxSTAFF登录成功。");
			if (session.getAttribute(EnumSession.SESSION_POS.getName()) != null) {
				logger.info("存在POS会话，POS=" + ((Pos) session.getAttribute(EnumSession.SESSION_POS.getName())).toString());
			} else {
				Pos pos = new Pos();
				pos.setID(BaseAction.INVALID_POS_ID);
				session.setAttribute(EnumSession.SESSION_POS.getName(), pos);
			}

			BxStaff bxStaffReturn = (BxStaff) bxStaffInOut.clone();
			bxStaffReturn.setSalt("");
			params.put(BaseAction.KEY_Object, bxStaffReturn);
			params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage());
			// if (bxStaffInOut.getIsFirstTimeLogin() != 1) {
			// session.removeAttribute(EnumSession.SESSION_BxStaffMoile.getName());
			// }
		} else {
			logger.error("服务器登录失败:" + ec.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "手机号码或密码错误");
		}

		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/bxStaff/logout.bx
	 */
	@RequestMapping(value = "/logoutEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String logout(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		BxStaff bxStaff = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.getName()); // ...定义常量
		logger.info("服务器的登出, bxStaff=" + bxStaff);
		Map<String, Object> params = new HashMap<String, Object>();
		if (bxStaff != null) {
			// 更新bxStaff登录次数
			updateLoginCount(bxStaff.getMobile());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} else {
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		}
		params.put(KEY_HTMLTable_Parameter_msg, bxStaffBO.getLastErrorMessage());
		params.put(KEY_HTMLTable_Parameter_msg, bxStaffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		EnumSession.clearAllSession(session);
		session.invalidate();

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * http://localhost:8080/nbr/bxStaff/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") BxStaff bxStaff, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询一个博欣内部员工，bxStaff=" + bxStaff);
		BxStaff bs = (BxStaff) bxStaffBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, bxStaff);

		Map<String, Object> params = new HashMap<String, Object>();
		switch (bxStaffBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功,bs=" + bs == null ? "" : bs);

			params.put(KEY_Object, bs);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, bxStaffBO.getLastErrorMessage().toString());
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * http://localhost:8080/nbr/bxStaff/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") BxStaff bxStaff, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询多个博欣内部员工，bxStaff=" + bxStaff);
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<BxStaff> bxStaffList = (List<BxStaff>) bxStaffBO.retrieveNObject(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, bxStaff);

		Map<String, Object> params = new HashMap<String, Object>();
		switch (bxStaffBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功,bxStaffList=" + bxStaffList);

			params.put(KEY_ObjectList, bxStaffList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, bxStaffBO.getLastErrorMessage().toString());

		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
