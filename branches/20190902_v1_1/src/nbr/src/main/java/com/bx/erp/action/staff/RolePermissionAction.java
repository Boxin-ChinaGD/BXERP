package com.bx.erp.action.staff;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.RolePermissionBO;

@Controller
@RequestMapping("/rolePermission")
@Scope("prototype")
public class RolePermissionAction extends BaseAction {
//	private Log logger = LogFactory.getLog(RolePermissionAction.class);

	@Resource
	private RolePermissionBO rolePermissionBO;

	/*Request Body in Fiddler:
	User-Agent: Fiddler
	Host: localhost:8080
	 *Content-Type: application/x-www-form-urlencoded
	* Request Body in Fiddler: URL:roleID=1
	* http://localhost:8080/nbr/rolePermission/deleteEx.bx
	*/
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String deleteEx(@ModelAttribute("SpringWeb") RolePermission rolePermission, ModelMap model) {
//		logger.info("删除一个角色权限，rolePermission=" + rolePermission.toString());
//
//		rolePermissionBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID, rolePermission);
//
//		logger.info("Delete RolePermission error code=" + rolePermissionBO.getLastErrorCode());
//
//		Map<String, Object> params = getDefaultParamToReturn(false);
//		switch (rolePermissionBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("删除成功");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, rolePermissionBO.getLastErrorMessage());
//		params.put(KEY_HTMLTable_Parameter_msg, rolePermissionBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
}
