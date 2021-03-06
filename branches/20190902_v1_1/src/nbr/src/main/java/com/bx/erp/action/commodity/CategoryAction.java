package com.bx.erp.action.commodity;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/category")
@Scope("prototype")
public class CategoryAction extends BaseAction { // Category> {
	private Log logger = LogFactory.getLog(CommodityAction.class);

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 */

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private CategoryParentBO categoryParentBO;
	/*
	 * Request Body in Fiddler: URL: http://localhost:8080/nbr/category.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") Category category, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????????????????????????????,category=" + category);

		Company company = getCompanyFromSession(session);

		category.setPageIndex(PAGE_StartIndex);
		category.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<Category> ls = (List<Category>) categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, category);

		logger.info("RetrieveN category error code=" + categoryBO.getLastErrorCode());

		switch (categoryBO.getLastErrorCode()) {
		case EC_NoError:
			CategoryParent categoryParent = new CategoryParent();
			categoryParent.setPageIndex(PAGE_StartIndex);
			categoryParent.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(company.getDbName());
			List<CategoryParent> categoryParentList = (List<CategoryParent>) categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, categoryParent);
			if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("????????????????????????????????????=" + categoryParentBO.getLastErrorCode());
				break;
			} else {
				logger.info("???????????????????????????categoryParentList=" + categoryParentList);
			}

			logger.info("???????????????ls:" + ls);
			mm.put("categoryList", ls);
			mm.put("categoryParentList", categoryParentList);
			break;
		default:
			logger.info("???????????????");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		return new ModelAndView(COMMODITY_CATEGORY_Index, "", new Category());
	}

	/*
	 * Request Body in Fiddler:
	 * URL:GET=http://localhost:8080/nbr/category/retrieveAllEx.bx
	 */
	@RequestMapping(value = "/retrieveAllEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveAllEx(@ModelAttribute("SpringWeb") Category category, ModelMap model, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,category=" + category);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		category.setPageIndex(PAGE_StartIndex);
		category.setPageSize(PAGE_SIZE_Infinite);
		List<?> ls = categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, category);

		logger.info("RetrieveN category error code=" + categoryBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (categoryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????????????????,ls=" + ls);
			params.put("categoryList", ls);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????" + categoryBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, categoryBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:
	 * URL:GET=http://localhost:8080/nbr/category/retrieveNByParent.bx
	 */
	@RequestMapping(value = "/retrieveNByParent", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByParent(@ModelAttribute("SpringWeb") Category category, ModelMap model, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????,category=" + category.toString());

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_Category_RetrieveNByParent, category);
		logger.info("retrieveNByParent category error code=" + categoryBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (categoryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("??????????????????,ls=" + ls.toString());
			params.put("categoryList", ls);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("????????????????????????");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????" + categoryBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, categoryBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/category/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Category category, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);

		// ???????????????ID??????????????????string1???fieldToCheckUnique
		// ??????????????????????????????,??????????????????ID??????????????????????????????????????????????????????????????????????????????
		// string1????????????????????????????????????fieldToCheckUnique????????????
		// fieldToCheckUnique=1????????????????????????????????????
		return doRetrieveNToCheckUniqueFieldEx(false, category, company.getDbName(),session);
	}

	@Override
	protected BaseBO getBO() {
		return categoryBO;
	}
}