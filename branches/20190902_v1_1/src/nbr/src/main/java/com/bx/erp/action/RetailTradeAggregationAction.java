package com.bx.erp.action;

import java.util.Date;
import java.util.HashMap;
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
import com.bx.erp.action.bo.RetailTradeAggregationBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeAggregation")
@Scope("prototype")
public class RetailTradeAggregationAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeAggregationAction.class);

	@Resource
	private RetailTradeAggregationBO retailTradeAggregationBO;

	/*
	 * Request Body in Fiddler:
	 * staffID=1&workTimeStart=2018-10-11&workTimeEnd=2018-11-18&tradeNO=186&amout=
	 * 1586&reserveAmount=500&cashAmount=1000&wechatAmount=1000&alipayAmount=1000
	 * POST URL:http://localhost:8080/nbr/retailTradeAggregation/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") RetailTradeAggregation retailTradeAggregation, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个收银汇总，retailTradeAggregation=" + retailTradeAggregation);

		Company company = getCompanyFromSession(session);
		DataSourceContextHolder.setDbName(company.getDbName());

		retailTradeAggregation.setStaffID(getStaffFromSession(session).getID());
		RetailTradeAggregation rta = (RetailTradeAggregation) retailTradeAggregationBO.createObject(retailTradeAggregation.getStaffID(), BaseBO.INVALID_CASE_ID, retailTradeAggregation);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (retailTradeAggregationBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建成功！！rta=" + rta);
			rta.setSyncDatetime(new Date());
			rta.setSyncType(SyncCache.SYNC_Type_C);

			params.put("rta", rta);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			logger.info("重复创建收银汇总");
			rta.setSyncDatetime(new Date());
			rta.setSyncType(SyncCache.SYNC_Type_C);
			
			params.put("rta", rta);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeAggregationBO.getLastErrorMessage());
		logger.info("返回的对象=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	
	/*
	 * Request Body in Fiddler: pricePurchase=0.04&ID=1 URL:
	 * http://localhost:8080/nbr/retailTradeAggregation/retrieve1Ex.bx
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") RetailTradeAggregation retailTradeAggregation, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个收银汇总,retailTradeAggregation=" + retailTradeAggregation);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		RetailTradeAggregation retailTradeAggregationDB = (RetailTradeAggregation) retailTradeAggregationBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, retailTradeAggregation);
		logger.info("Retrieve1 retailTradeAggregationBO error code=" + retailTradeAggregationBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();

		switch (retailTradeAggregationBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！sf=" + retailTradeAggregationDB);
			params.put(BaseAction.KEY_Object, retailTradeAggregationDB);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;// ...
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeAggregationBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
