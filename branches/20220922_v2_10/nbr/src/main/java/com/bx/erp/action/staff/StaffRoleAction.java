package com.bx.erp.action.staff;

import java.util.HashMap;
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
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.model.StaffRole;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/staffRole")
@Scope("prototype")
public class StaffRoleAction extends BaseAction {
	private Log logger = LogFactory.getLog(StaffRoleAction.class);

	@Resource
	private StaffRoleBO staffRoleBO;

	@Resource
	private StaffBO staffBO;

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/staffRole/retrieveNEx.bx
	 * Content-Type: application/x-www-form-urlencoded
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") StaffRole staffRole, ModelMap model, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个员工角色中间表，staffRole=" + staffRole);
		Company company = getCompanyFromSession(session);
		// TODO 判断是否OP登录从而拥有查出售前的权限应该从登录信息中拿，但是现在staff.retrieveNEx接口只支持非OP帐号登录，所以先注释。
		// if (getBxStaffFromSession(session) != null) {
		// s.setOperator(EnumBoolean.EB_Yes.getIndex());
		// }
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		List<StaffRole> ls = (List<StaffRole>) staffRoleBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staffRole);

		logger.info("retrieve N Roles error code=" + staffRoleBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (staffRoleBO.getLastErrorCode()) {
		case EC_NoError:
			// List<Staff> list = new ArrayList<Staff>();
			// Staff s = new Staff();
			//// s.setInt1(1);
			// boolean bGetStaffError = false;
			// for (StaffRole sr : ls) {
			// s.setID(sr.getStaffID());
			// DataSourceContextHolder.setDbName(dbName);
			// Staff retrieve1Staff = (Staff)
			// staffBO.retrieve1Object(getStaffFromSession(session).getID(),
			// BaseBO.INVALID_CASE_ID, s);
			// if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			// logger.error("查询一个员工信息失败：" + staffBO.getLastErrorCode());
			// params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
			// params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
			// bGetStaffError = true;
			// break;
			// }
			// retrieve1Staff.setInt1(Staff.DEFINE_SET_RoleID(sr.getRoleID()));
			// list.add(retrieve1Staff);
			// }
			//
			// if (bGetStaffError) {
			// break;
			// }
			logger.info("查询成功，ls=" + ls);
			params.put(KEY_ObjectList, ls);
			// params.put(KEY_HTMLTable_Parameter_TotalRecord, staffBO.getTotalRecord());
			params.put(KEY_HTMLTable_Parameter_TotalRecord, staffRoleBO.getTotalRecord());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(KEY_HTMLTable_Parameter_code, "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("没有权限进行操作");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		case EC_WrongFormatForInputField:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("字段格式错误");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		default:
			logger.error("其他错误！" + staffBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
