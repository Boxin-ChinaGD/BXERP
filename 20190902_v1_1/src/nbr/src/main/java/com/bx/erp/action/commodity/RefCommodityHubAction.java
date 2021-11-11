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
import com.bx.erp.action.bo.commodity.RefCommodityHubBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.RefCommodityHub;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/refCommodityHub")
@Scope("prototype")
public class RefCommodityHubAction extends BaseAction {
	private Log logger = LogFactory.getLog(RefCommodityHubAction.class);

	@Resource
	private RefCommodityHubBO refCommodityHubBO;

	/*
	 * Request Body in Fiddler:
	 * http://localhost:8080/nbr/refCommodityHub/retrieveNByBarcodeEx.bx?barcode=
	 * 123456789
	 */
	@RequestMapping(value = "/retrieveNByBarcodeEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNByBarcodeEx(@ModelAttribute("SpringWeb") RefCommodityHub refCommodityHub, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("根据条形码读取参照商品，refCommodityHub=" + refCommodityHub);

		// Company company = getCompanyFromSession(session);
		//
		// DataSourceContextHolder.setDbName(company.getDbName());
		DataSourceContextHolder.setDbName(BaseAction.DBName_StaticDB);
		List<?> list = refCommodityHubBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, refCommodityHub);

		Map<String, Object> params = new HashMap<String, Object>();

		switch (refCommodityHubBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功,list=" + list);
			params.put(KEY_ObjectList, list);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, refCommodityHubBO.getTotalRecord());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(KEY_HTMLTable_Parameter_code, "0");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		case EC_WrongFormatForInputField:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "条形码格式错误:" + Barcodes.FIELD_ERROR_barcodes);
			break;
		default:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error(refCommodityHubBO.printErrorInfo());
			}
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, refCommodityHubBO.getLastErrorMessage());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
