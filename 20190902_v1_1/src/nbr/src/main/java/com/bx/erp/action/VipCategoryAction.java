package com.bx.erp.action;

import java.util.Date;
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

import com.bx.erp.model.VipCategory;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipCategoryBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/vipCategory")
@Scope("prototype")
public class VipCategoryAction extends BaseAction {
	private Log logger = LogFactory.getLog(VipCategoryAction.class);

	@Resource
	private VipCategoryBO vipCategoryBO;

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/vipCategory/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") VipCategory vipCategory, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个供应商,vipCategory=" + vipCategory);
		
		Company company = getCompanyFromSession(session);
		
		Map<String, Object> params = getDefaultParamToReturn(true);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!vipCategoryBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {

			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_VipCategory).read1(vipCategory.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
		}

		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);
			params.put("vipCategory", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/vipCategory/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") VipCategory vipCategory, ModelMap mm, HttpSession session){
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询所有的会员类别，vipCategory" + vipCategory);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		vipCategory.setPageSize(PAGE_SIZE_Infinite);
		List<?> ls = vipCategoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCategory);

		logger.info("retrieveN vipCategory error code=" + vipCategoryBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipCategoryBO.getLastErrorCode()) {
		case EC_NoError:
			for (Object object : ls) {
				BaseModel bm = (BaseModel) object;
				bm.setSyncDatetime(new Date());
			}
			params.put("vipCategoryList", ls);
			params.put("count", vipCategoryBO.getTotalRecord());
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
		params.put(KEY_HTMLTable_Parameter_msg, vipCategoryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:name=kkk http://localhost:8080/nbr/vipCategory/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") VipCategory vipCategory, ModelMap mm, HttpSession session){
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个会员类别，vipCategory=" + vipCategory);
		
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = vipCategoryBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCategory);

		logger.info("Create vipCategory error code=" + vipCategoryBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipCategoryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建会员类别成功，创建的会员类别为：" + bm);
			params.put("vipCategoryList", bm);
			params.put("count", vipCategoryBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("创建会员类别失败，已经存在相同的会员类别");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, vipCategoryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:ID=1&name=kkk1 http://localhost:8080/nbr/vipCategory/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") VipCategory vipCategory, ModelMap mm, HttpSession session){
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个会员类别,vipCategory=" + vipCategory);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = vipCategoryBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCategory);

		logger.info("Update vipCategory error code=" + vipCategoryBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipCategoryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("修改成功，修改的对象为：" + bm);
			params.put("vipCategoryList", bm);
			params.put("count", vipCategoryBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("修改会员类别失败，已经存在相同的会员类别");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, vipCategoryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:ID=6 http://localhost:8080/nbr/vipCategory/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") VipCategory vipCategory, ModelMap mm, HttpSession session){
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个会员类别，vipCategory" + vipCategory);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		vipCategoryBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCategory);
		logger.info("Delete vipCategory error code=" + vipCategoryBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(false);
		switch (vipCategoryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除会员成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("删除会员失败，错误码=" + EnumErrorCode.EC_BusinessLogicNotDefined);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, vipCategoryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
