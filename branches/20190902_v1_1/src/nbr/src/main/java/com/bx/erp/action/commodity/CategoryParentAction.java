package com.bx.erp.action.commodity;

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
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/categoryParent")
@Scope("prototype")
public class CategoryParentAction extends BaseAction {
	private Log logger = LogFactory.getLog(CategoryParentAction.class);

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private CategoryParentBO categoryParentBO;

	/*
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 
	 * POST URL:http://localhost:8080/nbr/categoryParent/createEx.bx name=lllll
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") CategoryParent categoryParent, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,categoryParent=" + categoryParent);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel createObject = categoryParentBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);

		logger.info("Create categoryParent error code = " + categoryParentBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (categoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("????????????????????????????????????" + createObject);

			params.put("categoryParent", createObject);
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).write1(createObject, company.getDbName(),getStaffFromSession(session).getID());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");

			break;
		case EC_Duplicated:
			logger.error("??????????????????????????????=" + EnumErrorCode.EC_Duplicated);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????????????????");

			break;
		case EC_NoPermission:
			logger.error("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		case EC_WrongFormatForInputField:
			logger.error("??????????????????????????????=" + EnumErrorCode.EC_WrongFormatForInputField);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????");

			break;
		default:
			logger.error("????????????:" + categoryParentBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 
	 * POST URL:http://localhost:8080/nbr/categoryParent/updateEx.bx ID=1&name=llllx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") CategoryParent categoryParent, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,categoryParent=" + categoryParent);

		Map<String, Object> params = new HashMap<String, Object>();
		
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel updateObject = categoryParentBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);

		logger.info("Update categoryParent error code = " + categoryParentBO.getLastErrorCode());

		switch (categoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????updateObject=" + updateObject);
			params.put("categoryParent", updateObject);
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).write1(updateObject, company.getDbName(),getStaffFromSession(session).getID());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");

			break;
		case EC_Duplicated:
			logger.error("????????????????????????????????????=" + EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????????????????");

			break;
		case EC_NoPermission:
			logger.error("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			
			break;
		case EC_WrongFormatForInputField:
			logger.error("??????????????????????????????=" + EnumErrorCode.EC_WrongFormatForInputField);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????");

			break;
		case EC_BusinessLogicNotDefined:
			logger.error("??????????????????????????????=" + EnumErrorCode.EC_BusinessLogicNotDefined);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());

			break;
		default:
			logger.error("???????????????" + categoryParentBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 
	 * GET URL:http://localhost:8080/nbr/categoryParent/deleteEx.bx?ID=12
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") CategoryParent categoryParent, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,categoryParent=" + categoryParent);

		Map<String, Object> params = new HashMap<String, Object>();
		
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		categoryParentBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);

		logger.info("Delete categoryParent error code = " + categoryParentBO.getLastErrorCode());

		switch (categoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).delete1(categoryParent);// ????????????????????????????????????????????????????????????
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).load(company.getDbName());// ???nbr??????????????????????????????
			SyncCacheManager.getCache(company.getDbName(), EnumSyncCacheType.ESCT_CategorySyncInfo).load(company.getDbName());// ???????????????????????????
			// 
			break;
		case EC_BusinessLogicNotDefined:
			logger.error("??????????????????????????????????????????" + categoryParentBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());

			break;
		case EC_NoPermission:
			logger.error("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????" + categoryParentBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 
	 * GET URL:http://localhost:8080/nbr/categoryParent/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") CategoryParent categoryParent, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,categoryParent=" + categoryParent);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel retrieve1Object = categoryParentBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);

		logger.info("Delete categoryParent error code = " + categoryParentBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (categoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????retrieve1Object=" + (retrieve1Object == null ? "null" : retrieve1Object));
			params.put("categoryParent", retrieve1Object);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

			break;
		case EC_NoPermission:
			logger.info("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, categoryParentBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 
	 * GET URL:http://localhost:8080/nbr/categoryParent/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") CategoryParent categoryParent, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????categoryParent=" + categoryParent);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		categoryParent.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<CategoryParent> retrieveNObject = (List<CategoryParent>) categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);

		logger.info("RetrieveN categoryParent error code = " + categoryParentBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (categoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			Category c = new Category();
			c.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Category> retrieveNObject2 = (List<Category>) categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, c);
			if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("RetrieveN category error code = " + categoryBO.getLastErrorCode());
				return "";
			} else {
				logger.info("???????????????retrieveNObject2=" + retrieveNObject2);
			}

			logger.info("???????????????retrieveNObject=" + retrieveNObject);
			params.put("categoryParentList", retrieveNObject);
			params.put("categoryList", retrieveNObject2);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

			break;
		case EC_NoPermission:
			logger.info("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, categoryBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
