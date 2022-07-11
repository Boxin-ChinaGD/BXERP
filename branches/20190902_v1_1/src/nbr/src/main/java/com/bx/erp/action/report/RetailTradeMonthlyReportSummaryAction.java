package com.bx.erp.action.report;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.bx.erp.action.bo.report.RetailTradeMonthlyReportSummaryBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeMonthlyReportSummary")
@Scope("prototype")
public class RetailTradeMonthlyReportSummaryAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeReportByCommodityAction.class);

	@Resource
	private RetailTradeMonthlyReportSummaryBO reportBO;

	/*
	 * Request Body in Fiddler:  URL:
	 * POST: http://localhost:8080/nbr/retailTradeMonthlyReportSummary/retrieveN.bx
	 */
	@RequestMapping(value = "/retrieveN", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveN(@ModelAttribute("SpringWeb") RetailTradeMonthlyReportSummary report, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("加载销售数据，report=" + report);

		Company company = getCompanyFromSession(req.getSession());

		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> reportInfo = reportBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, report);
		switch (reportBO.getLastErrorCode()) {
		case EC_NoError:
			params.put("reportList", reportInfo);
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_OtherError:
			logger.error("数据库操作错误");
			break;
		default:
			logger.error("未知错误 ，错误码：" + reportBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, reportBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

}
