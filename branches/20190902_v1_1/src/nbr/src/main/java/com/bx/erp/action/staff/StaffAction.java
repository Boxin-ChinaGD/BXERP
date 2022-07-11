package com.bx.erp.action.staff;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.bx.erp.action.AuthenticationAction;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseAuthenticationBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.DepartmentBO;
import com.bx.erp.action.bo.RetailTradeAggregationBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.interceptor.StaffLoginInterceptor;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.model.BaseAuthenticationModel;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.Department;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.Role;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.StaffField;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.model.StaffRole;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.MD5Util;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/staff")
@Scope("prototype")
public class StaffAction extends AuthenticationAction {
	private static Log logger = LogFactory.getLog(StaffAction.class);

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Resource
	private StaffBO staffBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private RoleBO roleBO;

	@Resource
	private MessageBO messageBO;

	@Resource
	private DepartmentBO departmentBO;

	@Resource
	private StaffRoleBO staffRoleBO;

	@Value("${wx.templateid.LoginSuccess}")
	private String WSMSG_TEMPLATEID_LoginSuccess;

	@Value("${wx.templateid.LogoutSuccess}")
	private String WXMSG_TEMPLATEID_LogoutSuccess;

	@Override
	protected BaseAuthenticationBO getBaseAuthenticationBO() {
		return staffBO;
	}

	@Resource
	private RetailTradeAggregationBO retailTradeAggregationBO;

	/** 代表查询的对象要返回salt */
	public static final int RETURN_SALT = 1;

	@RequestMapping(value = "/getTokenEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String getTokenEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// 检查是否有了pos会话或是否是修改密码（修改密码则不清空会话），如果有，证明staff准备登录或允许staff登录，不需要清除所有会话
		// session中的Pos可能是代表网页登录的，即ID=-1，所以要加个判断
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		if (pos == null ? true
				: pos.getID() == BaseAction.INVALID_ID // pos = null，没有任何pos会话，是为登录。登录需要清空旧的会话，避免混淆。pos
														// ID为BaseAction.INVALID_ID，网页已经登录，现在是重新登录（可能是拿自己帐号也可能是拿别人帐号），需要清空旧的会话
						&& staff.getForModifyPassword() != Staff.FOR_ModifyPassword // 用户准备修改密码，不用清空会话
						&& staff.getCreateNewStaff() != Staff.FOR_CreateNewStaff // 用户准备创建staff，不用清空会话
		) {
			EnumSession.clearAllSession(session); // 清除所有会话，防止不同浏览器登录后混淆缓存
		}

		logger.info("获取秘钥，staff=" + staff);

		logger.info("----- 查询一个staff -----");
		logger.info("staffBO hash1 = " + staffBO.hashCode());
		RSAInfo rsa = generateRSA(staff.getPhone());

		session.setAttribute(EnumSession.SESSION_StaffPhone.getName(), staff.getPhone());
		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rsa", rsa);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Fiddler: phone=15016509167&salt=000000 User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded Request Body
	 * in Fiddler: URL:http://localhost:8080/nbr/staff/loginEx.bx
	 * 
	 * int3代表是否在Pos端登录staff roleID(int1)的值为登录者的角色ID string1的值为公司编号
	 */
	@RequestMapping(value = "/loginEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String loginEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("staff登录, staff=" + staff);

		logger.info("staffBO hash2 = " + staffBO.hashCode());
		Object phone = session.getAttribute(EnumSession.SESSION_StaffPhone.getName());
		if (phone == null) { // 检查之前此phone的staff有无请求过验证
			logger.info("没有电话号码");
			return ""; // Hacking
		} else {
			logger.info("得到的phone=" + phone);
		}

		ErrorInfo eo = new ErrorInfo();
		if (!checkLoginCount(phone, eo)) {
			return null;
		}

		// ...TODO 根据传递过来的公司编号拿到公司的私有DB的名字
		Company company = getCompanyFromCompanyCacheBySN(staff.getCompanySN());
		if (company == null) {
			return null; // 黑客行为
		}
		setCompanyInSession(session, company); // 将dbName保存在会话中，后面用户的ACTION请求中将可以从会话得到DB名称，从而正确地读写DB

		Staff staffInOut = new Staff();
		staffInOut.setPhone(staff.getPhone());
		staffInOut.setInvolvedResigned(staff.getInvolvedResigned());
		ErrorInfo ec = new ErrorInfo();
		staffInOut = (Staff) authenticate(company.getDbName(), BaseBO.CASE_Login, staffInOut, staff.getPwdEncrypted(), ec);
		if (staffInOut != null) {
			StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission);
			ErrorInfo ecOut = new ErrorInfo();
			StaffPermission sp = spc.read1(staffInOut.getID(), StaffBO.SP_Staff_Retrieve1, ecOut);
			if (ecOut.getErrorCode() == EnumErrorCode.EC_NoError && sp != null) {
				staffInOut.setRoleID(sp.getRoleID());
				logger.info("已经获取并设置staff对应的roleID：" + staffInOut);
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();

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
			staffReturn.clearSensitiveInfo();
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
			// 所有人在pos端登录都要发送微信消息，在nbr端登录不需要发送微信消息// 首次登录的情况不发送登录消息
			// Pos pos2 = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
			// if (pos2 != null && pos2.getID() != BaseAction.INVALID_POS_ID &&
			// staffInOut.getIsFirstTimeLogin() == EnumBoolean.EB_NO.getIndex() &&
			// staff.getIsLoginFromPos() == EnumBoolean.EB_Yes.getIndex()) {
			// sendLoginMsgToWx(staffInOut, company);
			// }
			// //
			logger.info("staff对应的roleID,staffInOut.getRoleID()=" + staffInOut.getRoleID());

			if ((staffInOut.getRoleID() == Role.EnumTypeRole.ETR_Cashier.getIndex() || staffInOut.getRoleID() == Role.EnumTypeRole.ETR_Boss.getIndex()) && staffInOut.getIsFirstTimeLogin() == EnumBoolean.EB_Yes.getIndex()
					&& staff.getIsLoginFromPos() == EnumBoolean.EB_Yes.getIndex()) {
				ec.setErrorCode(EnumErrorCode.EC_Redirect);
				ec.setErrorMessage("收银员通过POS机进行首次登录，需要进行重置密码！"); // ...
			}
			params.put(JSON_ERROR_KEY, ec.getErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
		} else {
			logger.info("登录失败:" + ec.toString());
			params.put(JSON_ERROR_KEY, ec.getErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, "手机号码或密码错误");
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// 方法cjs改动中
	@SuppressWarnings("unchecked")
	public void sendLoginMsgToWx(Staff staff, Company company) {
		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // ...查询门店老板
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> list = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			logger.error("创建登录上班消息失败；查询门店老板失败！");
			return;
		}

		List<Staff> slist = (List<Staff>) list.get(1);
		boolean hasStaffBindWx = false;
		for (Staff staffBoss : slist) {
			if (staffBoss.getOpenid() == null || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行登录，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			hasStaffBindWx = true;
			break;
		}
		//
		if (staff.getOpenid() != null) {
			hasStaffBindWx = true;
		}

		if (hasStaffBindWx) {
			Message m = new Message();
			m.setCategoryID(6);
			m.setParameter("[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"交班消息生成," + staff.getName() + " 登录上班\"}]");
			m.setCompanyID(company.getID());
			m.setReceiverID(staff.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			Message msg = (Message) messageBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, m);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建登录上班消息失败!" + messageBO.printErrorInfo());
				return;
			} else {
				logger.info("创建出来的Message：" + msg == null ? "" : msg);
			}
			//
			if (msg.getParameter() == null || "".equals(msg.getParameter())) {
				logger.error("创建登录上班消息失败,创建Message的内容失败!" + messageBO.printErrorInfo());
				return;
			}
			logger.info("需要发送的消息： " + msg.getParameter());

			try {
				boolean bSendWxMsgSuccess = false;
				for (Staff staffBoss : slist) {
					if (staffBoss.getOpenid() == null || "".equals(staffBoss.getOpenid()) || staff.getShopID() != staffBoss.getShopID()) {
						logger.debug("用户是在哪个门店进行登录，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
						continue;
					}

					Map<String, Object> params = WxAction.sendLoginToWorkMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, staff, staffBoss.getOpenid(), WSMSG_TEMPLATEID_LoginSuccess);
					if ((int) params.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
						bSendWxMsgSuccess = true;
					} else {
						logger.info("向" + staffBoss + "发送员工上班的微信消息失败！错误消息=" + params.get(BaseWxModel.WX_ERRMSG));
					}
				}

				// 判断登录者是否需要发送微信消息,如果登录者是老板则不需要重复发送消息
				if (staff.getOpenid() != null && !"".equals(staff.getOpenid()) && staff.getRoleID() != EnumTypeRole.ETR_Boss.getIndex()) {
					Map<String, Object> params = WxAction.sendLoginToWorkMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, staff, staff.getOpenid(), WSMSG_TEMPLATEID_LoginSuccess);
					if ((int) params.get(BaseWxModel.WX_ERRCODE) != BaseWxModel.WX_ERRCODE_Success) {
						bSendWxMsgSuccess = true;
					} else {
						logger.info("向" + staff + "发送员工上班的微信消息失败！错误消息=" + params.get(BaseWxModel.WX_ERRMSG));
					}
				}

				if (bSendWxMsgSuccess) { // ... 暂时来说，有一个发送成功都update消息了
					updateMessage(msg, company.getDbName());
				}
			} catch (Exception e) {
				logger.error("向门店绑定微信的员工发送上班微信消息失败，错误信息=" + e.getMessage());
				return;
			}
		} else {
			logger.debug("该门店的老板和登录者都没有绑定微信,不需要创建微信消息并发送");
		}
	}

	private void updateMessage(Message msg, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
		if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("更新消息的状态失败：" + messageBO.printErrorInfo() + "。请OP手动更新本条消息的状态为已读。消息ID=" + msg.getID());
		}
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/staff/logout.bx
	 */
	@RequestMapping(value = "/logoutEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String logout(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);

		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName()); // ...定义常量
		logger.info("服务器的登出,staff=" + staff);
		Map<String, Object> params = new HashMap<String, Object>();
		if (staff != null) {
			// 更新Staff登录次数
			updateLoginCount(staff.getPhone());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
			if (pos != null && pos.getID() != BaseAction.INVALID_POS_ID) {
				sendLogoutMsgToWx(staff, company);
			}
			// if (staff.getInt3() == 1 || Staff.DEFINE_GET_RoleID(staff.getInt1()) == 1) {
			// sendMsgToWx(staff, company);
			// }
		} else {
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		}
		params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		EnumSession.clearAllSession(session);
		session.invalidate();

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	private void sendLogoutMsgToWx(Staff staff, Company company) {
		String dbName = company.getDbName();
		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // ...查询门店老板
		DataSourceContextHolder.setDbName(dbName);
		List<?> list = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			logger.error("创建员工交班的消息生成失败；查询门店老板失败或老板未绑定微信公众号！错误码=" + roleBO.getLastErrorCode().toString());
			return;
		}

		List<Staff> slist = (List<Staff>) list.get(1);
		boolean hasStaffBindWx = false;
		for (Staff staffBoss : slist) {
			if (staffBoss.getOpenid() == null || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行退出，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			hasStaffBindWx = true;
			break;
		}
		//
		if (staff.getOpenid() != null) {
			hasStaffBindWx = true;
		}

		if (hasStaffBindWx) {
			Message m = new Message();
			m.setCategoryID(7);
			m.setParameter("[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"交班消息," + staff.getName() + " 登出下班\"}]");
			m.setCompanyID(company.getID());
			m.setReceiverID(staff.getID());
			DataSourceContextHolder.setDbName(dbName);
			Message msg = (Message) messageBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, m);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建员工交班的消息生成失败,创建Message失败!" + messageBO.printErrorInfo());
				return;
			}
			logger.info("创建出来的Message：" + msg);
			if (msg.getParameter() == null || "".equals(msg.getParameter())) {
				logger.error("创建员工交班的消息生成失败,创建Message的内容失败!" + messageBO.printErrorInfo());
				return;
			}
			logger.info("需要发送的消息： " + msg.getParameter());

			RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
			retailTradeAggregation.setStaffID(staff.getID());
			DataSourceContextHolder.setDbName(dbName);
			RetailTradeAggregation retailTradeAggregationDB = (RetailTradeAggregation) retailTradeAggregationBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeAggregation); // 特殊的R1。查找的是这个staff的最后一张收银汇总
			if (retailTradeAggregationBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("Retrieve1 retailTradeAggregationBO error code=" + retailTradeAggregationBO.getLastErrorCode());
			}
			if (retailTradeAggregationDB != null) {
				try {
					boolean bSendWxMsgSuccess = false;
					for (Staff staffBoss : slist) {
						if (staffBoss.getOpenid() == null || "".equals(staffBoss.getOpenid()) || staff.getShopID() != staffBoss.getShopID()) {
							logger.debug("用户是在哪个门店进行退出，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
							continue;
						}

						Map<String, Object> wxMap = WxAction.sendLogoutToWorkMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, retailTradeAggregationDB, staff, staffBoss.getOpenid(),
								WXMSG_TEMPLATEID_LogoutSuccess, company);
						if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
							bSendWxMsgSuccess = true;
						} else {
							logger.error("向" + staffBoss.getName() + "发送员工交班的微信消息失败！错误消息=" + wxMap.get(BaseWxModel.WX_ERRMSG));
						}
					}

					// 判断退出者是否需要发送微信消息,如果退出者是老板则不需要重复发送消息
					if (staff.getOpenid() != null && !"".equals(staff.getOpenid()) && staff.getRoleID() != EnumTypeRole.ETR_Boss.getIndex()) {
						Map<String, Object> wxMap = WxAction.sendLogoutToWorkMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, retailTradeAggregationDB, staff, staff.getOpenid(),
								WXMSG_TEMPLATEID_LogoutSuccess, company);
						if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
							bSendWxMsgSuccess = true;
						} else {
							logger.error("向" + staff.getName() + "发送员工交班的微信消息失败！错误消息=" + wxMap.get(BaseWxModel.WX_ERRMSG));
						}
					}

					if (bSendWxMsgSuccess) { // ... 暂时来说，有一个发送成功都update消息了
						updateMessage(msg, company.getDbName());
					}
				} catch (Exception e) {
					logger.error("向门店绑定微信的员工发送交班微信消息失败，错误信息=" + e.getMessage());
					return;
				}
			} else {
				logger.error("发送交班消息失败;查询交班人员的收银汇总失败！" + retailTradeAggregationBO.printErrorInfo());
			}
		} else {
			logger.debug("该门店的老板和退出者都没有绑定微信,就不需要发送微信消息");
		}
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/staff.bx
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") Staff staff, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("进入staff的页面时加载相关数据,staff=" + staff);

		Company company = getCompanyFromSession(session);

		Map<String, Object> load = load(company.getDbName(), session);

		mm.put("shopList", load.get("shopList"));
		mm.put("departmentList", load.get("departmentList"));
		mm.put("roleList", load.get("roleList"));
		mm.put("StaffField", new StaffField());
		mm.put(BaseAction.JSON_ERROR_KEY, load.get(BaseAction.JSON_ERROR_KEY));

		return new ModelAndView(STAFF_Index, "", new Staff());
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/staff/toCreate.bx
	 */
	// @RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	// public ModelAndView toCreate(@ModelAttribute("SpringWeb") Staff staff,
	// ModelMap mm, HttpSession session) {
	// if (!canCallCurrentAction(session,
	// BaseAction.EnumUserScope.STAFF.getIndex())) {
	// logger.debug("无权访问本Action");
	// return null;
	// }
	//
	// logger.info("进入创建页面时加载相关数据,staff=" + staff);
	// Company company = getCompanyFromSession(session);
	//
	// Map<String, Object> load = load(company.getDbName(), session);
	//
	// mm.put("shopList", load.get("shopList"));
	// mm.put("departmentList", load.get("departmentList"));
	// mm.put("roleList", load.get("roleList"));
	// mm.put(BaseAction.JSON_ERROR_KEY, load.get(BaseAction.JSON_ERROR_KEY));
	//
	// return new ModelAndView(STAFF_ToCreate, "", new Staff());
	// }

	// ...需要重构的名字和逻辑
	public Map<String, Object> load(String dbName, HttpSession session) {
		Map<String, Object> param = new HashMap<String, Object>();
		ErrorInfo ecOut = new ErrorInfo();
		do {
			Shop s = new Shop();// ...令系统启动时就加载全部
			s.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> retrieveNShopObject = shopBO.retrieveNObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, s);
			if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询shop失败，错误码=" + shopBO.getLastErrorCode());
				ecOut.setErrorCode(shopBO.getLastErrorCode());
				break;
			} else {
				logger.info("查询成功！retrieveNShopObject=" + retrieveNShopObject);
			}

			Department d = new Department();// ...令系统启动时就加载全部
			d.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> retrieveNDepartmentObject = departmentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, d);
			if (departmentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询department失败，错误码=" + departmentBO.getLastErrorCode());
				ecOut.setErrorCode(departmentBO.getLastErrorCode());
				break;
			} else {
				logger.info("查询成功！retrieveNDepartmentObject=" + retrieveNDepartmentObject);
			}

			Role r = new Role();// ...系统启动时已经加载全部
			r.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> retrieveNRoleObject = roleBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, r);
			if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询role失败，错误码=" + roleBO.getLastErrorCode());
				ecOut.setErrorCode(roleBO.getLastErrorCode());
				break;
			} else {
				logger.info("查询成功！retrieveNRoleObject=" + retrieveNRoleObject);
			}

			param.put("shopList", retrieveNShopObject.get(0));// retrieveNShopObject当中有ShopList，CompanyList，BxStaffList。前端仅需要ShopList。
			param.put("departmentList", retrieveNDepartmentObject);
			param.put("roleList", retrieveNRoleObject);
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		param.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
		param.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
		logger.info("返回的数据=" + param);
		return param;
	}

	/*
	 * phone=1234xx7&IDInPOS=1&POS_SN=12346xx&ICID=1323f&WeChat=qqqqqq&Password=
	 * 12346&PasswordExpireDate=2028/07/20%2010:54:00&IsFirstTimeLogin=1
	 * &Q1=111&A1=11&Q2=111&A2=11&Q3=111&A3=11&shopID=1&departmentID=1&Status=0 POST
	 * URL:http://localhost:8080/nbr/staff/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个员工,staff=" + staff);

		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromSession(req.getSession());
		do {
			// 非OP帐号，不能创建售前帐号
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staff.getPhone())) {
				if (!BaseAction.DBName_Public.equals(company.getDbName())) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "非OP帐号不能创建售前帐号");

					break;
				}
			}

			String pwdNew = staffBO.decrypt(((BaseAuthenticationModel) staff).getKey(), staff.getPwdEncrypted());
			if (staffBO.getLastErrorCode() == EnumErrorCode.EC_WrongFormatForInputField) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("创建员工失败：" + staffBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			}

			if (staffBO.getLastErrorCode() == EnumErrorCode.EC_Hack) {
				return null;
			}

			String md5 = MD5Util.MD5(pwdNew + BaseAction.SHADOW);
			staff.setSalt(md5);
			staff.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex());
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			Staff bm = (Staff) staffBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staff);

			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("创建员工失败：" + staffBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			}
			logger.info("创建员工成功，staff=" + bm);

			// 2、更新普通缓存
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).write1(bm, company.getDbName(), getStaffFromSession(req.getSession()).getID());
			// 3、更新权限视图
			StaffPermissionCache staffPermissionCache = (StaffPermissionCache) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission);
			staffPermissionCache.load(company.getDbName());
			//
			// 删除敏感信息
			Staff staffCloned = (Staff) bm.clone();
			staffCloned.clearSensitiveInfo();
			params.put(KEY_Object, staffCloned);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "创建员工成功");
			break;
		} while (false);

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * ID=1&Phone=1234567&WeChat=11111111&ICID=12344444 URL
	 * :http://localhost:8080/nbr/staff/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap mm, HttpSession session, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("更新一个员工,staff=" + staff);

		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromSession(req.getSession());
		do {
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staff.getPhone()) && EnumBoolean.EB_NO.getIndex() == staff.getOperator()) {
				if (!BaseAction.DBName_Public.equals(company.getDbName())) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "不能修改成售前帐号的号码");
				}

				break;
			}

			Staff staffSession = getStaffFromSession(session);
			if (staff.getID() == staffSession.getID() && staffSession.getRoleID() != staff.getRoleID()) { // session中RoleID是设置在int1中，而修改的RoleID是设置在int1中
				String errMsg = "登录者不能修改自己的角色！";
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("修改员工失败： " + errMsg);
				}
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, errMsg);
				break;
			}
			DataSourceContextHolder.setDbName(company.getDbName());
			Staff staffUpdated = (Staff) staffBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staff);
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("修改员工失败： " + staffBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			}
			logger.info("更新员工成功，staff=" + staffUpdated);
			// 如果修改的是当前登录者的信息则更新相应会话
			if (staffSession.getID() == staffUpdated.getID()) {
				session.setAttribute(EnumSession.SESSION_Staff.getName(), staffUpdated);
			}
			// 2、更新普通缓存
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).write1(staffUpdated, company.getDbName(), getStaffFromSession(req.getSession()).getID());
			// 3、更新权限视图
			StaffPermissionCache staffPermissionCache = (StaffPermissionCache) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission);
			staffPermissionCache.load(company.getDbName());

			staffUpdated = (Staff) staffUpdated.clone();
			staffUpdated.clearSensitiveInfo();

			params.put(KEY_Object, staffUpdated);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * GET URL:http://localhost:8080/nbr/staff/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET, produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Staff s, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// BaseModel bm =
		// CacheManager.getCache(dbName,EnumCacheType.ECT_Staff).read1(s.getID(),
		// BaseBO.CURRENT_STAFF.getID(), eecOut);
		// 由于CacheManager.getCache(dbName,EnumCacheType.ECT_Staff).read1()只支持根据ID搜索STAFF。而本函数需要根据PHONE搜索STAFF，故此处直接call
		// BO的方法查询STAFF
		logger.info("查询一个Staff，s=" + s);
		Map<String, Object> params = new HashMap<String, Object>();
		Staff sf = new Staff();

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		// 如果不是OP账号想查询售前账号，当成查询不出处理
		if (BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())) {
			if (!BaseAction.DBName_Public.equals(dbName)) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "");
				logger.info("返回的数据=" + params);

				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
		}

		DataSourceContextHolder.setDbName(dbName);
		sf = (Staff) staffBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, s);
		logger.info("Retrieve1 staffBO error code=" + staffBO.getLastErrorCode());

		switch (staffBO.getLastErrorCode()) {
		case EC_NoError:
			if (sf == null) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "不存在该用户");
				logger.info("返回的数据=" + params);

				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			sf.clearSensitiveInfo();
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(sf.getPhone())) {
				if (!BaseAction.DBName_Public.equals(dbName)) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "");
					logger.info("返回的数据=" + params);

					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
			}
			StaffRole sr = new StaffRole();
			sr.setStaffID(sf.getID());
			DataSourceContextHolder.setDbName(dbName);
			StaffRole retrieve1StaffRole = (StaffRole) staffRoleBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, sr);
			if (staffRoleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个StaffRole失败，错误码=" + staffRoleBO.getLastErrorCode().toString());
				return "";
			} else {
				logger.info("retrieve1StaffRole=" + retrieve1StaffRole);
			}
			sf.setRoleID((retrieve1StaffRole == null ? 0 : retrieve1StaffRole.getRoleID()));

			logger.info("查询成功！sf=" + sf);
			params.put("staff", sf);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;// ...
		}
		params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * GET URL:http://localhost:8080/nbr/staff/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Staff s, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询多个Staff，s=" + s);

		Company company = getCompanyFromSession(session);
		// TODO 判断是否OP登录从而拥有查出售前的权限应该从登录信息中拿，但是现在staff.retrieveNEx接口只支持普通登录，所以先注释。
		// if (getBxStaffFromSession(session) != null) {
		// s.setOperator(EnumBoolean.EB_Yes.getIndex());
		// }

		String dbName = company.getDbName();
		DataSourceContextHolder.setDbName(dbName);
		List<Staff> ls = (List<Staff>) staffBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, s);

		logger.info("RetrieveN staff error code=" + staffBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (staffBO.getLastErrorCode()) {
		case EC_NoError:
			StaffRole sr = new StaffRole();
			boolean bGetStaffRoleError = false;
			for (Staff staff : ls) {
				if (staff.getStatus() == 0) {
					sr.setStaffID(staff.getID());
					staff.setSyncDatetime(new Date());
					DataSourceContextHolder.setDbName(dbName);
					StaffRole retrieve1StaffRole = (StaffRole) staffRoleBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, sr);
					if (staffRoleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("查询一个StaffRole失败：" + staffBO.printErrorInfo());
						params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
						params.put(BaseAction.JSON_ERROR_KEY, staffRoleBO.getLastErrorCode().toString());
						bGetStaffRoleError = true;
						break;
					} else {
						logger.info("retrieve1StaffRole=" + retrieve1StaffRole);
					}

					staff.setRoleID((retrieve1StaffRole == null ? 0 : retrieve1StaffRole.getRoleID()));
					staff.clearSensitiveInfo();
				}
			}

			if (bGetStaffRoleError) {
				break;
			}
			logger.info("查询成功，ls=" + ls);
			params.put(KEY_ObjectList, ls);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, staffBO.getTotalRecord());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(KEY_HTMLTable_Parameter_code, "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("没有权限");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足!");
			break;
		default:
			logger.error("其他错误!" + staffBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误!");
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/staff/deleteEx.bx?ID=1
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap mm, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromSession(req.getSession());

		do {
			if (staff.getID() == getStaffFromSession(req.getSession()).getID()) {
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "禁止当前用户删除自己！");// 不允许删除当前登录人员！
				break;
			}
			DataSourceContextHolder.setDbName(company.getDbName());
			Staff staffOut = (Staff) staffBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, staff);
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("删除员工失败：" + staffBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			}
			logger.info("删除员工成功，staff=" + staff);
			// 更新普通缓存，不能清除普通缓存，因为有的地方（比如退货单，它可能是这个staff创建的）如果清除普通缓存，会导致找不到这个staff。
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).write1(staffOut, company.getDbName(), BaseBO.SYSTEM);

			// 更新权限视图
			StaffPermissionCache staffPermissionCache = (StaffPermissionCache) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission);
			staffPermissionCache.load(company.getDbName());
			//
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "删除员工成功");
			break;
		} while (false);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/staff/deleteListEx.bx?staffListID=2,5
	 */
	// @RequestMapping(value = "/deleteListEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") Staff s, ModelMap
	// model, HttpServletRequest req) {
	// String sStaffIDs = GetStringFromRequest(req, "staffListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// logger.info("sStaffIDs=" + sStaffIDs);
	// if (sStaffIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// return "";
	// }
	//
	// int[] iArrStaffID = toIntArray(sStaffIDs);
	// if (iArrStaffID == null) {
	// return "";
	// }
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// Staff staff = new Staff();
	// for (int iID : iArrStaffID) {
	// staff.setID(iID);
	// staffBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// staff);
	// logger.info("Delete staff error code=" + staffBO.getLastErrorCode());
	// if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString(); //
	// ...部分成功、部分失败？
	// }
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Staff).delete1(staff);
	// }
	// logger.info("删除成功！");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * GET URL:http://localhost:8080/nbr/staff/toResetPwd.bx
	 */
	// @RequestMapping(value = "/toResetPwd", method = RequestMethod.GET)
	// public ModelAndView toResetPwd(@ModelAttribute("SpringWeb") Staff staff,
	// ModelMap mm, HttpSession session) {
	// if (!canCallCurrentAction(session,
	// BaseAction.EnumUserScope.STAFF.getIndex())) {
	// logger.debug("无权访问本Action");
	// return null;
	// }
	//
	// return new ModelAndView(STAFF_ToResetPwd, "", new Staff());
	// }

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/staff/resetPasswordEx.bx 重置他人密码不需要知道旧密码
	 */
	@RequestMapping(value = "/resetOtherPasswordEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String resetOtherPasswordEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap mm, HttpServletRequest request, HttpSession session) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改他人的密码，staff=" + staff);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			String sPasswordEncryptedNew = GetStringFromRequest(request, "sPasswordEncryptedNew", null);
			if (sPasswordEncryptedNew == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("sPasswordEncryptedNew参数缺失");
				}
				return null;
			}

			if (getStaffFromSession(session).getPhone().equals(staff.getPhone())) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("不可对自己进行重置密码");
				}

				params.put(KEY_HTMLTable_Parameter_msg, "不可对自己进行重置密码！！");
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				break;
			}
			// 获取修改密码所需要的数据
			ErrorInfo ecOut = new ErrorInfo();
			staff.setIsResetOtherPassword(EnumBoolean.EB_Yes.getIndex());
			Staff staffInDB = (Staff) preparePasswordToCompare(staff, null, sPasswordEncryptedNew, request.getSession(), dbName, ecOut);
			if (staffInDB == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("解密新旧密码失败！");
				}
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode());
				break;
			}
			staff.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex()); // 被重置密码后需要此账号人员登录时强制修改密码
			DataSourceContextHolder.setDbName(dbName);
			Staff updateStaff = (Staff) staffBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_ResetOtherPassword, staff);
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			}
			logger.info("操作成功！bm=" + updateStaff);
			// 不返回盐值给浏览器
			CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).write1(updateStaff, dbName, getStaffFromSession(session).getID());

			updateStaff = (Staff) updateStaff.clone();
			updateStaff.clearSensitiveInfo();

			params.put(KEY_Object, updateStaff);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
			params.put(KEY_HTMLTable_Parameter_msg, "重置密码成功");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/staff/resetMyPasswordEx.bx
	 */
	@RequestMapping(value = "/resetMyPasswordEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String resetMyPasswordEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap mm, HttpServletRequest request, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改自己的密码，staff=" + staff.toString());

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();

		do {
			if (!staff.getPhone().equals(getStaffFromSession(session).getPhone())) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("只能对自己的密码进行修改，黑客行为！");
				}
				return null;
			}

			String sPasswordEncryptedNew = GetStringFromRequest(request, "sPasswordEncryptedNew", null);// TODO hardcode 需要重构
			if (sPasswordEncryptedNew == null) {
				logger.error("获取加密后的新密码失败！");
				return null;
			}
			String sPasswordEncryptedOld = GetStringFromRequest(request, "sPasswordEncryptedOld", null);
			if (sPasswordEncryptedOld == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("获取加密后的旧密码失败！");
				}
				return null;
			}

			// 获取修改密码所需要的数据
			ErrorInfo ecOut = new ErrorInfo();
			staff.setIsResetOtherPassword(EnumBoolean.EB_NO.getIndex());
			Staff staffInDB = (Staff) preparePasswordToCompare(staff, sPasswordEncryptedOld, sPasswordEncryptedNew, request.getSession(), dbName, ecOut);
			if (staffInDB == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("解密新旧密码失败！");
				}
				params.put(KEY_HTMLTable_Parameter_msg, "修改密码失败，请重试");
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode());
				break;
			}
			// 修改自己密码，默认系统权限
			DataSourceContextHolder.setDbName(dbName);
			staff.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex());// 修改自己的密码后说明已经进行首次登录了
			Staff updateStaff = (Staff) staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_ResetMyPassword, staff);
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改密码失败：" + staffBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
				break;
			} else {
				logger.info("操作成功！bm=" + updateStaff);
				updateStaff.clearSensitiveInfo();
				CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).write1(updateStaff, dbName, getStaffFromSession(session).getID());
				params.put(KEY_Object, updateStaff);
				// ...
				StaffPermission s = new StaffPermission();
				s.setStaffID(updateStaff.getID());
				StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(dbName, EnumCacheType.ECT_StaffPermission);
				// 如果查询权限视图为空，则证明没有配置该角色权限，直接终止程序
				StaffPermission sp = spc.read1(updateStaff.getID(), StaffBO.SP_Staff_Retrieve1, ecOut);
				if (sp == null) {
					logger.error("没有配置当前角色的权限,updateStaff.getID()=" + updateStaff.getID());
					return null;
				}
				if (sp.getRoleID() == EnumTypeRole.ETR_Boss.getIndex() && staffInDB.getIsFirstTimeLogin() == EnumBoolean.EB_Yes.getIndex()) { // ...TODO 店长是老板？
					// ...删除售前帐号 是否需要通过查询得到staff的ID？
					Staff preSaleStaff = new Staff();
					preSaleStaff.setPhone(BaseAction.ACCOUNT_Phone_PreSale);// 该值为默认手机号码
					preSaleStaff.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
					DataSourceContextHolder.setDbName(dbName);
					Staff retrieveStaff = (Staff) staffBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, preSaleStaff);
					if (retrieveStaff == null || staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						if (BaseAction.ENV != EnumEnv.DEV) {
							logger.error("删除售前帐号前查询售前帐号失败！！要让OP手动删除售前帐号retrieveStaff=" + retrieveStaff); // 不用返回错误给前端，让OP手动删除售前帐号即可
						}
					} else {
						logger.info("查询售前帐号成功! 查询出的对象为：" + retrieveStaff);
						if (retrieveStaff.getStatus() == EnumStatusStaff.ESS_Incumbent.getIndex()) {
							DataSourceContextHolder.setDbName(dbName); // ..切换数据源
							staffBO.deleteObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retrieveStaff);
							if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
								logger.error("删除售前帐号失败！！要让OP手动删除售前帐号retrieveStaff=" + retrieveStaff);// 不用返回错误给前端，让OP手动删除售前帐号即可
							} else {
								logger.info("删除售前帐号成功!");
							}
						} else {
							logger.info("售前账号已被删除，不用重复删除!");
						}
					}
				}
			}

			// 更新会话的关键字段
			Staff staffFromSession = getStaffFromSession(session);
			staffFromSession.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex()); // 此后用户再次发送请求时，服务器将可以判断出该用户不是第一次登录

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
			params.put(KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/staff/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);
		// 选传参数：ID，必传参数：string1和int1
		// 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
		// string1存储的是要检查的字段值，int1如下描述
		// int1=1，代表要检查手机号
		// int1=2，代表要检查身份证号
		// int1=3，代表要检查微信号
		return doRetrieveNToCheckUniqueFieldEx(false, staff, company.getDbName(), session);
	}

	/** 解密修改前和修改后的密码，存放到staffToBeModifiedPwd.oldMD5和staffToBeModifiedPwd.newMD5里，用以在checkUpdate()中判断新旧密码是否相同
	 * <br/>
	 * staffToBeModifiedPwd.getIsResetOtherPassword()为0代表是修改自己密码的接口调用该方法。 <br/>
	 * staffToBeModifiedPwd.getIsResetOtherPassword()为1代表是重置他人密码的接口调用该方法。 <br/>
	 * 
	 * @param staffToBeModifiedPwd
	 *            被修改密码的STAFF
	 * @param sPasswordEncryptedOld
	 *            旧的密码密文，从前端传递过来的
	 * @param sPasswordEncryptedNew
	 *            新的密码密文，从前端传递过来的
	 * @return staffToBeModifiedPwd对应的STAFF在DB中是什么值，以让外面的代码知道DB中的值，不需要重复查询。这看起来有点奇怪 */
	protected BaseModel preparePasswordToCompare(Staff staffToBeModifiedPwd, String sPasswordEncryptedOld, String sPasswordEncryptedNew, HttpSession session, String dbName, ErrorInfo ecOut) {
		Staff loginStaff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		staffToBeModifiedPwd.setReturnSalt(RETURN_SALT);

		Staff staffR1 = null;
		// 从DB查询出staffToBeModifiedPwd的信息。如果是自己修改自己密码，则默认系统权限
		DataSourceContextHolder.setDbName(dbName);
		if (loginStaff.getPhone().equals(staffToBeModifiedPwd.getPhone())) {
			staffR1 = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staffToBeModifiedPwd);
		} else {
			staffR1 = (Staff) staffBO.retrieve1Object(loginStaff.getID(), BaseBO.INVALID_CASE_ID, staffToBeModifiedPwd);
		}
		if (staffR1 == null || staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			ecOut.setErrorCode(staffBO.getLastErrorCode());
			return null;
		}

		String md5Old = null;
		if (staffToBeModifiedPwd.getIsResetOtherPassword() == EnumBoolean.EB_NO.getIndex()) {// 老板重置他人密码时，不需要解密此人的旧密码
			String pwdOld = getBaseAuthenticationBO().decrypt(((Staff) staffToBeModifiedPwd).getKey(), sPasswordEncryptedOld);
			if (pwdOld == null) {
				ecOut.setErrorCode(staffBO.getLastErrorCode());
				ecOut.setErrorMessage(staffBO.getLastErrorMessage());
				return null;
			}

			md5Old = MD5Util.MD5(pwdOld + BaseAction.SHADOW);
			if (md5Old == null) {
				ecOut.setErrorCode(EnumErrorCode.EC_Hack);
				logger.error("黑客行为!");
				return null;
			}
			if (!md5Old.equals(((BaseAuthenticationModel) staffR1).getSalt())) {
				ecOut.setErrorCode(EnumErrorCode.EC_NoSuchData);
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("旧密码不正确!可能不是本人在修改密码！");
				}
				return null;
			}
		}
		staffToBeModifiedPwd.setOldMD5(md5Old);

		String pwdNew = getBaseAuthenticationBO().decrypt(((BaseAuthenticationModel) staffToBeModifiedPwd).getKey(), sPasswordEncryptedNew);
		if (pwdNew == null) {
			ecOut.setErrorCode(staffBO.getLastErrorCode());
			ecOut.setErrorMessage(staffBO.getLastErrorMessage());
			return null;
		}

		String md5New = MD5Util.MD5(pwdNew + BaseAction.SHADOW);
		if (md5New == null) {
			ecOut.setErrorCode(EnumErrorCode.EC_Hack);
			logger.error("黑客行为!");
			return null;
		}
		staffToBeModifiedPwd.setNewMD5(md5New);
		staffToBeModifiedPwd.setSalt(md5New);

		return staffR1;
	}

	/*
	 * GET URL:http://localhost:8080/nbr/staff/retrieveResigned.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveResigned", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveResigned(@ModelAttribute("SpringWeb") Staff staff, ModelMap model, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询已离职员工");

		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromSession(req.getSession());
		//
		staff.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		String IDs = "";
		List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staff);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询离职员工失败：" + staffBO.printErrorInfo());
		} else {
			for (Staff s : staffList) {
				IDs += s.getID() + ",";
			}
			logger.info("查询离职员工成功：" + staffList);
		}

		params.put(BaseAction.RESIGNED_ID, IDs);
		params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/pingEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public void pingEx(HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.POS.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.debug("Pinging:SessionId=" + session.getId() + "/tLastAccessTime=" + session.getLastAccessedTime() + "/tMaxInactiveInterval=" + session.getMaxInactiveInterval());
	}

	@Override
	protected BaseBO getBO() {
		return staffBO;
	}
}
