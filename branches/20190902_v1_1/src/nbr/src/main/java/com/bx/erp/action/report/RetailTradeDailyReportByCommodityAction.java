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
import com.bx.erp.action.bo.report.RetailTradeDailyReportByCommodityBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeDailyReportByCommodity")
@Scope("prototype")
public class RetailTradeDailyReportByCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeReportByCommodityAction.class);
	
	@Resource
	private RetailTradeDailyReportByCommodityBO reportBO;
	
	
	/*
	 * Request Body in Fiddler:  URL:
	 * POST: http://localhost:8080/nbr/retailTradeDailyReportByCommodityAction/retrieveN.bx
	 * isASC=0&iOrderBy=0&stirng1=&bIgnoreZeroNO=0&int3=-1
	 */
	
	@RequestMapping(value = "/retrieveN", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveN(@ModelAttribute("SpringWeb") RetailTradeDailyReportByCommodity report, ModelMap model, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("加载销售表格和表头数据，report=" + report);
		
		Company company = getCompanyFromSession(req.getSession());
		
		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> reportInfo = reportBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, report);
		switch (reportBO.getLastErrorCode()) {
		case EC_NoError:
			params.put("reportList", reportInfo);
			params.put("count", reportBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_OtherError:
			logger.info("数据库操作错误");
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, reportBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	
}
