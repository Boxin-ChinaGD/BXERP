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
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/providerCommodity")
@Scope("prototype")
public class ProviderCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(ProviderCommodityAction.class);

	@Resource
	private ProviderCommodityBO providerCommodityBO;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * commodityID=5&providerID=6 URL: POST:
	 * http://localhost:8080/nbr/providerCommodity/createEx.bx
	 */
	@RequestMapping(value = "/createEx", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") ProviderCommodity providerCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个供应商商品表,providerCommodity=" + providerCommodity);

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerCommodityBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerCommodity);

		logger.info("Create providerCommodity error code=" + providerCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);
			params.put("providerCommodity", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			logger.info("组合商品无法添加！");
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, providerCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:commodityID=1 URL:
	 * POST: http://localhost:8080/nbr/providerCommodity/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") ProviderCommodity providerCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> pcList = providerCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerCommodity);

		logger.info("retrieve N providerCommodity error code=" + providerCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，pcList=" + pcList);
			params.put("pcList", pcList);
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
		params.put(KEY_HTMLTable_Parameter_msg, providerCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:CommodityID=6 URL: POST:
	 * http://localhost:8080/nbr/providerCommodity/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") ProviderCommodity providerCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		providerCommodityBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerCommodity);

		logger.info("delete providerCommodity error code=" + providerCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, providerCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
