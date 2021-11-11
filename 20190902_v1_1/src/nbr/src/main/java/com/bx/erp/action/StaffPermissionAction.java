package com.bx.erp.action;

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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffPermissionBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.Company;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/staffPermission")
@Scope("prototype")
public class StaffPermissionAction extends BaseAction {
	private Log logger = LogFactory.getLog(StaffPermissionAction.class);

	@Resource
	private StaffPermissionBO staffPermissionBO;

	/*
	User-Agent:Fiddler Host:localhost:8080
	Content-Type:application/x-www-form-urlencoded;charset=utf-8 
	Request Body in Fiddler: 
	GET http://localhost:8080/nbr/staffPermission/retrieveNEx.bx
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") StaffPermission staffPermission, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取多个角色权限,staffPermission=" + staffPermission);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Barcodes> list = (List<Barcodes>) staffPermissionBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staffPermission);

		Map<String, Object> params = getDefaultParamToReturn(true);

		switch (staffPermissionBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info(list);

			params.put("staffPermission", list);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, staffPermissionBO.getLastErrorMessage());
		logger.info("返回的对象=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
