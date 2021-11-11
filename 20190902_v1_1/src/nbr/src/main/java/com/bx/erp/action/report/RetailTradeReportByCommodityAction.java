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
import com.bx.erp.action.bo.report.RetailTradeReportByCommodityBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeReportByCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/retailTradeReportByCommodityAction")
@Scope("prototype")
public class RetailTradeReportByCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeReportByCommodityAction.class);

	protected final String SESSION_reportSummaryRetailTrade = "SESSION_reportSummaryRetailTrade";

	@Resource
	private RetailTradeReportByCommodityBO reportBO;

//	@RequestMapping(method = RequestMethod.GET, value = "stock")
//	public String stock(ModelMap mm, HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//		logger.info("进入销售报表页面时，传入商品的大小类");
//		Company company = getCompanyFromSession(session);
//
//		List<BaseModel> categoryList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).readN(true, false);
//		List<BaseModel> categoryParentList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).readN(true, false);
//
//		mm.put("categoryList", categoryList);
//		mm.put("categoryParentList", categoryParentList);
//		return WAREHOUSINGREPORT_Stock;
//	}

//	@RequestMapping(method = RequestMethod.GET, value = "status")
//	public String status(HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//
//		return WAREHOUSINGREPORT_Status;
//	}
//
//	@RequestMapping(method = RequestMethod.GET)
//	public String index(HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//
//		return WAREHOUSINGREPORT_Index;
//	}

	/*
	 * Request Body in Fiddler: URL: POST:
	 * http://localhost:8080/nbr/warehousingReport/retrieveNEx.bx
	 * isASC=0&iOrderBy=0&stirng1=&bIgnoreZeroNO=0&int3=-1
	 */

	@RequestMapping(value = "/retrieveN", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveN(@ModelAttribute("SpringWeb") RetailTradeReportByCommodity report, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("加载销售表格和表头数据，report=" + report);

		Company company = getCompanyFromSession(req.getSession());

		Map<String, Object> params = getDefaultParamToReturn(true);

		boolean bRequestTable = (report.getRequestNO() == 1); // 第1次请求的是表格内容，第2次请求的是页头的report summary
		if (!bRequestTable) {
			logger.info("加载表头数据成功，表头数据" + req.getSession().getAttribute(SESSION_reportSummaryRetailTrade).toString());
			params.put("reportSummaryRetailTrade", req.getSession().getAttribute(SESSION_reportSummaryRetailTrade));
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> reportInfo = reportBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, report);
		switch (reportBO.getLastErrorCode()) {
		case EC_NoError:
			// logger.info("加载表格成功，表头数据=" + reportInfo.get(1).toString() + ",表格数据=" +
			// reportInfo.get(0).toString());
			// req.getSession().setAttribute(SESSION_reportSummaryRetailTrade,
			// reportInfo.get(1));
			//
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
