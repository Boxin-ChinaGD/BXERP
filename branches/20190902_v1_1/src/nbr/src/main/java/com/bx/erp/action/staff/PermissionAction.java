package com.bx.erp.action.staff;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PermissionBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Permission;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/permission")
@Scope("prototype")
public class PermissionAction extends BaseAction {
	private Log logger = LogFactory.getLog(PermissionAction.class);

	@Resource
	private PermissionBO permissionBO;

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/permission/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Permission permission, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取所有的权限，permission=" + permission);

		Company company = getCompanyFromSession(session);

		permission.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = permissionBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, permission);

		logger.info("retrieveN vipCategory error code=" + permissionBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (permissionBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，查询的对象为：" + ls);
			params.put("permissionList", ls);
			params.put("count", permissionBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, permissionBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:name=kkk&&Remark=13213216
	 * http://localhost:8080/nbr/permission/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Permission permission, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个权限，permission=" + permission);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = permissionBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, permission);

		logger.info("Create permission error code=" + permissionBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (permissionBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建对象成功，创建的对象为：" + bm);
			params.put("permissionList", bm);
			params.put("count", permissionBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("创建的权限已经存在，错误码=" + EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, permissionBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:ID=6&int1=0 http://localhost:8080/nbr/permission/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Permission permission, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个权限，permission=" + permission);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		permissionBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, permission);

		logger.info("Delete vipCategory error code=" + permissionBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(false);
		switch (permissionBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("该权限已经在使用，无法删除");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, permissionBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:
	 * http://localhost:8080/nbr/permission/retrieveAlsoRoleStaffEx.bx
	 */
	@RequestMapping(value = "/retrieveAlsoRoleStaffEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveAlsoRoleStaffEx(@ModelAttribute("SpringWeb") Permission permission, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询所有跟该权限相关的角色，员工");
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		permissionBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_Permission_RetrieveAlsoRoleStaff, permission);

		logger.info("RetrieveAlsoRoleStaff Permission error code=" + permissionBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(false);
		switch (permissionBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, permissionBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
