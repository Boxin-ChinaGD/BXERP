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
import com.bx.erp.action.bo.report.RetailTradeDailyReportByCategoryParentBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeDailyReportByCategoryParent")
@Scope("prototype")
public class RetailTradeDailyReportByCategoryParentAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeDailyReportByCategoryParentAction.class);

	@Resource
	private RetailTradeDailyReportByCategoryParentBO reportByCategoryParentBO;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") RetailTradeDailyReportByCategoryParent report, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询时间段内大类名称和大类销售总数,RetailTradeDailyReportByCategoryParent=" + report);

		Company company = getCompanyFromSession(req.getSession());
		
		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<RetailTradeDailyReportByCategoryParent> reportList = (List<RetailTradeDailyReportByCategoryParent>) reportByCategoryParentBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, report);
		switch (reportByCategoryParentBO.getLastErrorCode()) {
		case EC_NoError:
			params.put("reportList", reportList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.error("未知错误，错误码："+reportByCategoryParentBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, reportByCategoryParentBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();

	}
}
