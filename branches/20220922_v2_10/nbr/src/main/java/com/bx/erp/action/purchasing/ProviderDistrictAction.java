package com.bx.erp.action.purchasing;

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
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/providerDistrict")
@Scope("prototype")
public class ProviderDistrictAction extends BaseAction {
	private Log logger = LogFactory.getLog(ProviderDistrictAction.class);

	@Resource
	private ProviderDistrictBO providerDistrictBO;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:name=昆明 URL:POST:
	 * http://localhost:8080/nbr/providerDistrict/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") ProviderDistrict providerDistrict, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		logger.info("创建一个供应商区域,providerDistrict=" + providerDistrict);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		DataSourceContextHolder.setDbName(dbName);
		BaseModel bm = providerDistrictBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);

		logger.info("Create providerDistrict error code=" + providerDistrictBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).write1(bm, dbName, getStaffFromSession(session).getID());

			logger.info("添加成功！bm=" + bm);
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		case EC_Duplicated:
			logger.error("添加失败,已有相同名称的供应商区域" + providerDistrictBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该供应商区域已存在，请重新修改");
			break;
		case EC_WrongFormatForInputField:
			logger.error("字段验证错误：" + providerDistrictBO.getLastErrorMessage());

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "输入数据的格式不正确");
			break;
		default:
			logger.error("其他错误：" + providerDistrictBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL:GET:
	 * http://localhost:8080/nbr/providerDistrict/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") ProviderDistrict providerDistrict, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		} 
		
		logger.info("创建一个供应商区域,providerDistrict=" + providerDistrict);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = new HashMap<String, Object>();
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!providerDistrictBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {

			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ProviderDistrict).read1(providerDistrict.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
		}

		logger.info("retrieve1 providerDistrict error code=" + providerDistrictBO.getLastErrorCode());

		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + (bm == null ? "NULL" : bm.toString()));

			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("供应商区域已经存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL:GET:
	 * http://localhost:8080/nbr/providerDistrict/retrieveNEx.bx?ID=1
	 */
	@RequestMapping(value = "/retrieveNEx", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") ProviderDistrict providerDistrict, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> pdList = providerDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);

		logger.info("retrieveN providerDistrict error code=" + providerDistrictBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功,pdList=" + pdList);
			params.put(KEY_ObjectList, pdList);
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
		params.put(KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL:GET:
	 * http://localhost:8080/nbr/providerDistrict/updateEx.bx?ID=5&name="丽江"
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") ProviderDistrict providerDistrict, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改多个供应商区域,providerDistrict" + providerDistrict.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerDistrictBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);
		logger.info("update providerDistrict error code=" + providerDistrictBO.getLastErrorCode());

		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ProviderDistrict).write1(bm, company.getDbName(), getStaffFromSession(session).getID());
			logger.info("修改成功,bm=" + bm);
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_Duplicated:
			logger.error("修改的供应商区域已存在：" + providerDistrictBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该供应商区域已存在，请重新修改");
			break;
		case EC_WrongFormatForInputField:
			logger.error("字段验证错误：" + providerDistrictBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "输入数据的格式不正确");
			break;
		case EC_Hack:
			logger.error("黑客行为");
			return null;
		case EC_NoPermission:
			logger.error("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		case EC_BusinessLogicNotDefined:
			logger.error(providerDistrictBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
			break;
		default:
			logger.error("其他错误：" + providerDistrictBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerDistrictBOCloned.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:name=昆明 URL:GET:
	 * http://localhost:8080/nbr/providerDistrict/deleteEx.bx?ID=9
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") ProviderDistrict providerDistrict, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个供应商区域，providerDistrict=" + providerDistrict);

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		providerDistrictBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);

		logger.info("delete providerDistrict error code=" + providerDistrictBO.getLastErrorCode());

		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ProviderDistrict).delete1(providerDistrict);

			logger.info("删除成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		case EC_BusinessLogicNotDefined:
			logger.error(providerDistrictBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
			break;
		case EC_WrongFormatForInputField:
			logger.error("字段验证错误：" + providerDistrictBO.getLastErrorMessage());

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "输入数据的格式不正确");
			break;
		default:
			logger.error("其他错误：" + providerDistrictBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerDistrictBOCloned.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
