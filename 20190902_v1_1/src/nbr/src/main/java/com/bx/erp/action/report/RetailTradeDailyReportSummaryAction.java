package com.bx.erp.action.report;

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
import com.bx.erp.action.RetailTradeAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportSummaryBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeDailyReportSummary")
@Scope("prototype")
public class RetailTradeDailyReportSummaryAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeDailyReportSummaryAction.class);

	@Resource
	private RetailTradeDailyReportSummaryBO retailTradeDailyReportSummaryBO;

	/*
	 Request Body in Fiddler:
	 User-Agent: Fiddler
	 Host: localhost:8080
	 Content-Type: application/x-www-form-urlencoded
	 Request Body in Fiddler: 
	 URL:http://localhost:8080/retailTradeDailyReportSummary/retrieve1Ex.bx?dateTime=2019/1/14
	*/
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") RetailTradeDailyReportSummary retailTradeDailyReportSummary, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("根据日期查询一个销售日报表。retailTradeDailyReportSummary=" + retailTradeDailyReportSummary);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = new HashMap<String, Object>();
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<List<BaseModel>> bm = retailTradeDailyReportSummaryBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReportSummary);// ...是否需要添加权限

		switch (retailTradeDailyReportSummaryBO.getLastErrorCode()) {
		case EC_NoError:
			params.put("retailTradeDailyReportSummary", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoSuchData:
			logger.info("此日期没有销售报表");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		default:
			logger.error("未知错误：" + retailTradeDailyReportSummaryBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeDailyReportSummaryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * URL:http://localhost:8080/retailTradeDailyReportSummary/retrieveNForChart.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNForChart", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNForChart(@ModelAttribute("SpringWeb") RetailTradeDailyReportSummary retailTradeDailyReportSummary, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个时间段的销售总额、销售毛利和日期。retailTradeDailyReportSummary=" + retailTradeDailyReportSummary);
		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<RetailTradeDailyReportSummary> bm = (List<RetailTradeDailyReportSummary>) retailTradeDailyReportSummaryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_RetailTradeDailyReportSummary_RetrieveNForChart,
				retailTradeDailyReportSummary);

		switch (retailTradeDailyReportSummaryBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("返回的数据条数：" + bm.size() + "--------------------------------------------------------------------");
			params.put("retailTradeDailyReportSummary", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(company.getDbName());
			if(realTimeSST == null) {
				logger.error("获取公司当天销售数据失败，dbName:" + company.getDbName());
				break;
			}
			//
			List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
			
			params.put("shopList", shopList);
			params.put("totalAmount", realTimeSST.getTotalAmountToday());
			params.put("totalNO", realTimeSST.getTotalNOToday());
			break;
		case EC_NoPermission:
			logger.error("权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.error("未知错误：" + retailTradeDailyReportSummaryBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeDailyReportSummaryBO.getLastErrorMessage());
		logger.info("返回的数据=" + params.toString());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
