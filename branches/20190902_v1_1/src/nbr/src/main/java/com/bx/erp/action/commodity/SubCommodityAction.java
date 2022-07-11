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
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/subCommodity")
@Scope("prototype")
public class SubCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(SubCommodityAction.class);

	@Resource
	private SubCommodityBO subCommodityBO;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * commodityID=5&subCommodityID=6 URL: POST:
	 * http://localhost:8080/nbr/subCommodity/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") SubCommodity subCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个子商品,subCommodity=" + subCommodity);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = subCommodityBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, subCommodity);

		logger.info("Create subCommodity error code=" + subCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (subCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("bm=" + bm);// ...
			mm.put("subCommodity", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			logger.info("该组合已存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("将普通商品当做组合商品插入子商品");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("checkCreate错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, subCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:commodityID=49 URL:
	 * POST: http://localhost:8080/nbr/subCommodity/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") SubCommodity subCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个子商品，subCommodity=" + subCommodity);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> scList = subCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, subCommodity);

		logger.info("update subCommodity error code=" + subCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (subCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("读取成功，scList=" + scList);// ...
			mm.put("subCommodity", scList);
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
		params.put(KEY_HTMLTable_Parameter_msg, subCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:CommodityID=6 URL: POST:
	 * http://localhost:8080/nbr/subCommodity/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") SubCommodity subCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个子商品,subCommodity=" + subCommodity);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		subCommodityBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, subCommodity);

		logger.info("update subCommodity error code=" + subCommodityBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (subCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除成功");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, subCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
