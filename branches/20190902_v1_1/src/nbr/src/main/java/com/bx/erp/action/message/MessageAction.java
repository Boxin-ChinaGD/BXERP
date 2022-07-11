//package com.bx.erp.action.message;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.message.MessageBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.message.Message;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/message")
//@Scope("prototype")
//public class MessageAction extends BaseAction {
//	private Log logger = LogFactory.getLog(MessageAction.class);
//
//	@Resource
//	private MessageBO messageBO;
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8 Request Body in
//	 * Fiddler:categoryID=1&isRead=1&Parameter=Json&senderID=1&receiverID=2
//	 * Type=POST:http://localhost:8080/nbr/message/createEx.bx
//	 */
//	@RequestMapping(value = "/createEx", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") Message message, ModelMap mm, HttpSession session) {
//		logger.info("创建一个信息,message=" + message);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, message);
//
//		logger.info("Create message error code=" + messageBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("创建成功，message=" + bm);
//			params.put("message", bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_BusinessLogicNotDefined:
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, messageBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/message/retrieve1Ex.bx?ID=1
//	 */
//	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieve1Ex(@ModelAttribute("SpringWeb") Message message, ModelMap mm, HttpSession session) {
//		logger.info("读取一个信息，message=" + message);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, message);
//
//		logger.info("retrieve1 message error code=" + messageBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("查询成功，message=" + bm);
//			params.put("message", bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, messageBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/message/retrieveNEx.bx
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/retrieveNEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieveNEx(@ModelAttribute("SpringWeb") Message message, ModelMap mm, HttpSession session) {
//		logger.info("读取多个信息，message=" + message);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<BaseModel> bmList = (List<BaseModel>) messageBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, message);
//
//		logger.info("retrieveN message error code=" + messageBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("messageList=" + bmList);
//			params.put("messageList", bmList);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, messageBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/message/updateEx.bx?ID=1&isRead=1
//	 */
//	@RequestMapping(value = "/updateEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String updateEx(@ModelAttribute("SpringWeb") Message message, ModelMap mm, HttpSession session) {
//		logger.info("修改一个信息，message=" + message);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, message);
//
//		logger.info("update message error code=" + messageBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("修改成功，message=" + bm);
//			params.put("message", bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限进行操作");
//
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		default:
//			logger.info("其他错误！");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, messageBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//}
