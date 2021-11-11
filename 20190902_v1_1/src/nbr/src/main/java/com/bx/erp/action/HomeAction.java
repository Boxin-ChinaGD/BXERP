package com.bx.erp.action;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.BxStaffField;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffField;

@Controller
@RequestMapping("/home")
@Scope("prototype")
public class HomeAction extends BaseAction {
	/** 代表用户是首次登录跳转到本页面的 */
	private final int LAST_Location_FirstTimeLogin = 1;
	/** 代表用户是从员工管理跳转到本页面的 */
	private final int LAST_Location_StaffManagement = 2;
	/** 代表用户是点击导航跳转到本页面的 */
	private final int LAST_Location_Navigation = 3;
	
	private Log logger = LogFactory.getLog(HomeAction.class);
	// value = "/xx/{name}",
	// @RequestMapping(method = RequestMethod.GET)
	// @ResponseBody

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		// logger.info(name);
		logger.info("index");

		return HOME_Index;
	}

	@RequestMapping(value = "/header", method = RequestMethod.GET)
	public String header(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("header");

		return HOME_Header;
	}

	@RequestMapping(value = "/sidebar", method = RequestMethod.GET)
	public String sidebar(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("sidebar");

		return HOME_Sidebar;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("home");

		return HOME_Home;
	}

	@RequestMapping(value = "/adminLogin", method = RequestMethod.GET)
	public String adminLogin(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("home");
		mm.put("staffField", new StaffField());
		logger.info(mm);
		return ADMIN_ToLogin;
	}

	@RequestMapping(value = "/updateMyPwd", method = RequestMethod.GET)
	public String updateMyPwd(HttpSession session, ModelMap mm, Staff staff) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
//		logger.info("home");
		if (staff.getLastLocation()== LAST_Location_FirstTimeLogin) {
			mm.put("identify", "LAST_Location_FirstTimeLogin");
		} else if (staff.getLastLocation()== LAST_Location_StaffManagement) {
			mm.put("identify", "LAST_Location_StaffManagement");
		} else if (staff.getLastLocation() == LAST_Location_Navigation) {
			mm.put("identify", "LAST_Location_Navigation");
		}
		return ADMIN_FirstLogin;
	}

	@RequestMapping(value = "/bxAdminLogin", method = RequestMethod.GET)
	public String bxAdminLogin(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("home");
		mm.put("BxStaffField", new BxStaffField());

		return BX_Admin_ToLogin;
	}

	@RequestMapping(value = "/company", method = RequestMethod.GET)
	public String company(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.ANYONE.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		// logger.info(name);
		logger.info("home");
		mm.put("BxStaff", new BxStaff());

		return BX_Admin_Company;
	}

}
