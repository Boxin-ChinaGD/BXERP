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
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportByStaffBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeDailyReportByStaff")
@Scope("prototype")
public class RetailTradeDailyReportByStaffAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeDailyReportSummaryAction.class);

	@Resource
	private RetailTradeDailyReportByStaffBO retailTradeDailyReportByStaffBO;

	/*
	 * URL:http://localhost:8080/retailTradeDailyReportByStaff/retrieveNEx.bx 
	 * date1 = datetimeStart date2 = datetimeEnd string1 = staffName
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") RetailTradeDailyReportByStaff retailTradeDailyReportByStaff, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个时间段的店员业绩,retailTradeDailyReportByStaff=" + retailTradeDailyReportByStaff);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = new HashMap<String, Object>();
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<RetailTradeDailyReportByStaff> list = (List<RetailTradeDailyReportByStaff>) retailTradeDailyReportByStaffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);

		switch (retailTradeDailyReportByStaffBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("返回的数据条数：" + list.size());
			params.put("retailTradeDailyReportByStaff", list);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.error("未知错误，错误码：" +retailTradeDailyReportByStaffBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeDailyReportByStaffBO.getLastErrorMessage());

		logger.info("返回的数据=" + params.toString());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
