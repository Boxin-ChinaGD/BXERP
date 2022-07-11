package com.bx.erp.action.commodity;

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
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/packageUnit")
@Scope("prototype")
public class PackageUnitAction extends BaseAction {
	private Log logger = LogFactory.getLog(PackageUnitAction.class);

	@Resource
	private PackageUnitBO packageUnitBO;

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/packageUnit.bx
	 */
//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView index(HttpSession session,@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap mm) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//
//		return new ModelAndView(PACKAGEUNIT_Index, "", new PackageUnit());
//	}

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/packageUnit/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个包装单位，packageUnit=" + packageUnit);

		Company company = getCompanyFromSession(session);
		
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);

		logger.info("retrieve N PackageUnits error code=" + packageUnitBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (packageUnitBO.getLastErrorCode()) {
		case EC_NoError:
			params.put(KEY_ObjectList, ls);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, packageUnitBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:name=11 http://localhost:8080/nbr/packageUnit/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个包装单位,packageUnit=" + packageUnit);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = packageUnitBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);

		logger.info("Create PackageUnit error code=" + packageUnitBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (packageUnitBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建对象成功，创建的对象为：" + bm);
			params.put(KEY_Object, bm);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, packageUnitBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_Duplicated:
			logger.error("创建失败，创建的包装单位已存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该商品单位已存在，请重新修改");
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + packageUnitBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:ID=6 http://localhost:8080/nbr/packageUnit/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个包装单位,packageUnit=" + packageUnit);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		packageUnitBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);

		logger.info("Delete PackageUnit error code=" + packageUnitBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(false);
		switch (packageUnitBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除包装单位成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_BusinessLogicNotDefined:
			logger.error("删除包装单位失败，错误信息：" + packageUnitBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + packageUnitBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/packageUnit/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个包装单位,packageUnit=" + packageUnit);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);

		logger.info("Update PackageUnit error code=" + packageUnitBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (packageUnitBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);
			params.put(KEY_Object, bm);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, packageUnitBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:ID=3&name=33 http://localhost:8080/nbr/packageUnit/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") PackageUnit packageUnit, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个包装单位,packageUnit=" + packageUnit);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = packageUnitBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);

		logger.info("Update Role error code=" + packageUnitBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (packageUnitBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("修改对象成功，修改的对象=" + bm);
			params.put(KEY_Object, bm);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, packageUnitBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + packageUnitBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
