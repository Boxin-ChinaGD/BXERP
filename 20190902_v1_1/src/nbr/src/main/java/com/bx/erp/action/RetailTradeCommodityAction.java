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
import com.bx.erp.action.bo.RetailTradeCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeCommodity")
@Scope("prototype")
public class RetailTradeCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeCommodityAction.class);

	@Resource
	private RetailTradeCommodityBO retailTradeCommodityBO;

	/*
	 * tradeID=5&commodityID=1&NO=20&priceOriginal=222.6&discount=0.9&NOCanReturn=0&
	 * priceReturn=0&priceSpecialOffer=0&giftCommodityID=-1&priceVIPOriginal=0 POST
	 * URL:http://localhost:8080/nbr/retailTradeCommodity/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") RetailTradeCommodity retailTradeCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);

		Staff staff = getStaffFromSession(session);// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
		retailTradeCommodity.setOperatorStaffID(staff.getID()); // 需要指定修改商品的staff的ID
		DataSourceContextHolder.setDbName(company.getDbName());
		RetailTradeCommodity bm = (RetailTradeCommodity) retailTradeCommodityBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		logger.info("Create RetailTradeCommodity error code=" + retailTradeCommodityBO.getLastErrorCode());
		Map<String, Object> params = getDefaultParamToReturn(true);

		switch (retailTradeCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info(bm);
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			Commodity comm = new Commodity();
			comm.setID(bm.getCommodityID());
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Commodity).delete1(comm);
			break;
		case EC_Duplicated:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;// ...
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeCommodityBO.getLastErrorMessage());
		logger.info("返回的对象=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * GET
	 * URL:http://localhost:8080/nbr/retailTradeCommodity/retrieveNEx.bx?tradeID=1
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") RetailTradeCommodity retailTradeCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<RetailTradeCommodity> retrieveNObject = (List<RetailTradeCommodity>) retailTradeCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		logger.info("RetrieveN RetailTradeCommodity error code=" + retailTradeCommodityBO.getLastErrorCode());
		Map<String, Object> params = getDefaultParamToReturn(true);

		switch (retailTradeCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info(retrieveNObject);
			params.put("retailTradeCommodityList", retrieveNObject);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;// ...
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeCommodityBO.getLastErrorMessage());
		logger.info("返回的对象=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * GET URL:http://localhost:8080/nbr/retailTradeCommodity/deleteEx.bx?tradeID=1
	 */
	// @RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") RetailTradeCommodity
	// retailTradeCommodity, ModelMap mm, HttpSession session){
	// logger.info("删除一个零售单商品，retailTradeCommodity=" +
	// retailTradeCommodity.toString());
	//
	// String dbName = getDBNameFromSession(session);
	//
	// DataSourceContextHolder.setDbName(dbName);
	// retailTradeCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, retailTradeCommodity);
	//
	// logger.info("Delete RetailTradeCommodity error code=" +
	// retailTradeCommodityBO.getLastErrorCode());
	// Map<String, Object> params = getDefaultParamToReturn(true);
	//
	// switch (retailTradeCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("删除成功！");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;// ...
	// }
	// params.put(KEY_HTMLTable_Parameter_msg,
	// retailTradeCommodityBO.getLastErrorMessage());
	// logger.info("返回的对象=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

}
