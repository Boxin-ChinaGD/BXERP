package com.bx.erp.action.staff;

//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;

import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.RoleBO;
//import com.bx.erp.model.Staff;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.Role;
//
//import net.sf.json.JSONObject;

@Controller
@RequestMapping("/role")
@Scope("prototype")
public class RoleAction extends BaseAction {
//	private Log logger = LogFactory.getLog(RoleAction.class);
//
//	@Resource
//	private RoleBO roleBO;
//
//	/*
//	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
//	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
//	 * http://localhost:8080/nbr/role.bx
//	 */
//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView index(@ModelAttribute("SpringWeb") Role role, ModelMap mm) {
//
//		return new ModelAndView(ROLE_Index, "", new Staff());
//	}

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/role/retrieveNEx.bx
	 */
//	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String retrieveNEx(@ModelAttribute("SpringWeb") Role role, ModelMap mm, HttpSession session) {
//		logger.info("查询多个角色，role=" + role);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<?> ls = roleBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, role);
//
//		logger.info("retrieve N Roles error code=" + roleBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(true);
//		switch (roleBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("查询成功,ls=" + ls);
//			params.put("roleList", ls);
//			params.put("count", roleBO.getTotalRecord());
//			params.put("msg", "");
//			params.put("code", "0");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, roleBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
//	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
//	 * URL:name=kkk http://localhost:8080/nbr/role/createEx.bx
//	 */
//	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") Role role, ModelMap mm, HttpSession session) {
//		logger.info("创建一个角色，role=" + role);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = roleBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, role);
//
//		logger.info("Create Role error code=" + roleBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(true);
//		switch (roleBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("创建角色成功，创建的角色为：" + bm);
//			params.put(BaseAction.KEY_Object, bm);
//			params.put("count", roleBO.getTotalRecord());
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		case EC_Duplicated:
//			logger.info("创建的角色已经存在");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, roleBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
//	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
//	 * URL:ID=6&bForceDelete=0 http://localhost:8080/nbr/role/deleteEx.bx
//	 */
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	public String deleteEx(@ModelAttribute("SpringWeb") Role role, ModelMap mm, HttpSession session) {
//		logger.info("删除一个角色，role=" + role);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		roleBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, role);
//
//		logger.info("Delete Role error code=" + roleBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(false);
//		switch (roleBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("删除一个角色成功");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, roleBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
////	/*
////	 * Request Body in Fiddler:
////	 * http://localhost:8080/nbr/role/deleteListEx.bx?roleListID=1,2,3
////	 */
////	@RequestMapping(value = "/deleteListEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
////	@ResponseBody
////	public String deleteListEx(@ModelAttribute("SpringWeb") Role role, ModelMap model, HttpServletRequest req) {
////		logger.info("删除多个角色，role=" + role);
////
////		Company company = getCompanyFromSession(req.getSession());
////		
////		Map<String, Object> params = new HashMap<String, Object>();
////		do {
////			String sRoleIDs = GetStringFromRequest(req, "roleListID", String.valueOf(BaseAction.INVALID_ID)).trim();
////			logger.info("sRoleIDs=" + sRoleIDs);
////			if (sRoleIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
////				return "";
////			}
////
////			int[] iArrRoleID = toIntArray(sRoleIDs);
////			if (iArrRoleID == null) {
////				return "";
////			}
////
////			boolean bAtLeastDelete1Success = false;
////
////			for (int i = 0; i < iArrRoleID.length; i++) {
////				role.setID(iArrRoleID[i]);
////
////				DataSourceContextHolder.setDbName(company.getDbName());
////				roleBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, role);
////				if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
////					bAtLeastDelete1Success = true;
////				}
////			}
////
////			if (bAtLeastDelete1Success) {
////				params.put("msg", "选中的角色只有部分删除成功，请重试");
////				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
////				break;
////			}
////
////			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
////		} while (false);
////		logger.info("返回的数据=" + params);
////
////		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
////	}
//
//	/*
//	 * Request Body in Fiddler:
//	 * http://localhost:8080/nbr/role/retrieveAlsoStaffEx.bx
//	 */
//	@RequestMapping(value = "/retrieveAlsoStaffEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String retrieveAlsoStaffEx(@ModelAttribute("SpringWeb") Role role, ModelMap mm, HttpSession session) {
//		logger.info("读取多个多个员工和角色，role=" + role);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<?> Is = roleBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_Role_RetrieveAlsoStaff, role);
//
//		logger.info("RetrieveAlsoStaff Permission error code=" + roleBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(false);
//		switch (roleBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("读取成功，Is=" + Is);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, roleBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
//	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
//	 * URL:ID=3&int1=2 http://localhost:8080/nbr/role/updateEx.bx
//	 */
//	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String updateEx(@ModelAttribute("SpringWeb") Role role, ModelMap mm, HttpSession session) {
//		logger.info("修改一个角色，role=" + role);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = roleBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, role);
//
//		logger.info("Update Role error code=" + roleBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(true);
//		switch (roleBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("修改对象成功，修改后对象为：" + bm);
//			params.put(BaseAction.KEY_Object, bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		case EC_Duplicated:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, roleBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
}
