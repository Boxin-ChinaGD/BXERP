package com.bx.erp.action;

import java.util.Date;
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

import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/barcodes")
@Scope("prototype")
public class BarcodesAction extends BaseAction {
	private Log logger = LogFactory.getLog(BarcodesAction.class);

	@Resource
	private BarcodesBO barcodesBO;

	/*
	 * Request Body in Fiddler: GET
	 * http://localhost:8080/nbr/barcodes/retrieveNEx.bx?commodityID=1
	 * http://localhost:8080/nbr/barcodes/retrieveNEx.bx
	 * http://localhost:8080/nbr/barcodes/retrieveNEx.bx?barcode=354
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询部分条形码，barcodes=" + barcodes);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Barcodes> list = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, barcodes);

		Map<String, Object> params = getDefaultParamToReturn(true);

		switch (barcodesBO.getLastErrorCode()) {
		case EC_NoError:
			for (Barcodes barcodes2 : list) {
				barcodes2.setSyncDatetime(new Date());
			}

			params.put("barcodesList", list);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, barcodesBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(KEY_HTMLTable_Parameter_TotalRecord, barcodesBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: POST
	 * http://localhost:8080/nbr/barcodes/createEx.bx?commodityID=40&barcode=354
	 */
//	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap mm, HttpSession session) {
//		logger.info("创建一个条形码barcodes=" + barcodes);
//
//		Company company = getCompanyFromSession(session);
//
//		Staff staff = getStaffFromSession(session);// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
//		//
//		barcodes.setOperatorStaffID(staff.getID());
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = barcodesBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, barcodes);
//		//
//		Map<String, Object> params = getDefaultParamToReturn(true);
//		//
//		switch (barcodesBO.getLastErrorCode()) {
//		case EC_NoError:
//			params.put(BaseAction.KEY_Object, bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_Duplicated:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			// params.put(KEY_HTMLTable_Parameter_TotalRecord, barcodesBO.getTotalRecord());
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		case EC_WrongFormatForInputField:
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
//			break;
//		default:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params.toString());
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}

	/*
	 * Request Body in Fiddler: POST
	 * http://localhost:8080/nbr/barcodes/deleteEx.bx?ID=1
	 */
//	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String deleteEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap mm, HttpSession session) {
//		logger.info("删除一个条形码,barcodes=" + barcodes);
//
//		Company company = getCompanyFromSession(session);
//
//		Staff staff = getStaffFromSession(session);
//		barcodes.setOperatorStaffID(staff.getID());
//		//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		barcodesBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, barcodes);
//		//
//		Map<String, Object> params = getDefaultParamToReturn(true);
//		switch (barcodesBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("删除成功");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			logger.info(barcodesBO.getLastErrorMessage());
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		case EC_NoSuchData:
//			logger.info(barcodesBO.getLastErrorMessage());
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
//			break;
//		case EC_WrongFormatForInputField:
//			logger.info("字段验证错误");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			params.put(KEY_HTMLTable_Parameter_TotalRecord, barcodesBO.getTotalRecord());
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params.toString());
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}

	/*
	 * Request Body in Fiddler: GET
	 * http://localhost:8080/nbr/barcodes/retrieve1Ex.bx?ID=7
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个条形码,barcodes=" + barcodes);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		Barcodes b = (Barcodes) barcodesBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, barcodes);

		Map<String, Object> params = getDefaultParamToReturn(true);

		switch (barcodesBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！b=" + b);
			params.put("barcodes", b == null ? "" : b);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证错误");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(KEY_HTMLTable_Parameter_TotalRecord, barcodesBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
