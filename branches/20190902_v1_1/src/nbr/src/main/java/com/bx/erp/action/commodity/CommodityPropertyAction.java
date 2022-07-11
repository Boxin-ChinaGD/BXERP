package com.bx.erp.action.commodity;

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
import com.bx.erp.action.bo.commodity.CommodityPropertyBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/commodityProperty")
@Scope("prototype")
public class CommodityPropertyAction extends BaseAction {
	private Log logger = LogFactory.getLog(CommodityPropertyAction.class);

	@Resource
	private CommodityPropertyBO commodityPropertyBO;

	// Fiddler URL: http://localhost:8080/nbr/commodityProperty.bx
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") CommodityProperty commodityProperty, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入商品属性的主页时获取到商品属性,commodityProperty=" + commodityProperty);

		Company company = getCompanyFromSession(session);

		commodityProperty.setID(1);
		DataSourceContextHolder.setDbName(company.getDbName());
		CommodityProperty cp = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodityProperty);
		if (commodityPropertyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取商品属性失败，错误码=" + commodityPropertyBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, commodityPropertyBO.getLastErrorCode().toString());
			return null;
		} else {
			logger.info("查询成功，cp=" + cp);
		}

		mm.put("commodityProperty", cp);
		return new ModelAndView(COMMODITYProperty_Index, "", null);
	}

	/*
	 * Request Body in Fiddler:
	 * http://localhost:8080/nbr/commodityProperty/retrieve1Ex.bx
	 */
	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") CommodityProperty commodityProperty, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个商品属性，commodityProperty=" + commodityProperty);

		Company company = getCompanyFromSession(session);

		commodityProperty.setID(1);
		DataSourceContextHolder.setDbName(company.getDbName());
		CommodityProperty cp = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodityProperty);
		logger.info("retrieve CommodityProperty error code=" + commodityPropertyBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (commodityPropertyBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("读取成功,cp=" + cp);
			params.put("commodityProperty", cp);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有读取商品属性权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, commodityPropertyBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:
	 * http://localhost:8080/nbr/commodityProperty/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") CommodityProperty commodityProperty, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个商品属性,commodityProperty=" + commodityProperty);

		Company company = getCompanyFromSession(session);
		
		Map<String, Object> params = getDefaultParamToReturn(true);
		commodityProperty.setID(1);
		DataSourceContextHolder.setDbName(company.getDbName());
		CommodityProperty cp = (CommodityProperty) commodityPropertyBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodityProperty);
		switch (commodityPropertyBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("修改成功,cp=" + cp);
			params.put("commodityProperty", cp);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有修改商品属性权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + commodityPropertyBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, commodityPropertyBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
