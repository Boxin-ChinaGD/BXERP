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
//import com.bx.erp.action.bo.message.MessageHandlerSettingBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Company;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.message.MessageHandlerSetting;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/messageHandlerSetting")
//@Scope("prototype")
//public class MessageHandlerSettingAction extends BaseAction {
//	private Log logger = LogFactory.getLog(MessageHandlerSettingAction.class);
//
//	@Resource
//	private MessageHandlerSettingBO messageHandlerSettingBO;
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8 Request Body in
//	 * Fiddler:categoryID=1&template=消息10&link=http://www.bxit.vip/shop/order/ID/10
//	 * Type=POST:http://localhost:8080/nbr/messageHandlerSetting/createEx.bx
//	 */
//	@RequestMapping(value = "/createEx", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") MessageHandlerSetting mhs, ModelMap mm, HttpSession session) {
//		logger.info("创建一个消息处理配置表,mhs=" + mhs);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageHandlerSettingBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, mhs);
//
//		logger.info("Create MessageHandlerSetting error code=" + messageHandlerSettingBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageHandlerSettingBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("创建成功，mhs=" + bm);
//			mm.put("mhs", bm);
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
//		params.put(KEY_HTMLTable_Parameter_msg, messageHandlerSettingBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/messageHandlerSetting/retrieve1Ex.bx?ID=1
//	 */
//	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieve1Ex(@ModelAttribute("SpringWeb") MessageHandlerSetting mhs, ModelMap mm, HttpSession session) {
//		logger.info("读取一个消息处理配置表,mhs=" + mhs);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageHandlerSettingBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, mhs);
//
//		logger.info("Retrieve1 MessageHandlerSetting error code=" + messageHandlerSettingBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageHandlerSettingBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("查询成功！bm=" + bm);
//			params.put("mhs", bm);
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
//		params.put(KEY_HTMLTable_Parameter_msg, messageHandlerSettingBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/messageHandlerSetting/retrieveNEx.bx
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/retrieveNEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieveNEx(@ModelAttribute("SpringWeb") MessageHandlerSetting mhs, ModelMap mm, HttpSession session) {
//		logger.info("读取多个消息处理配置表,mhs=" + mhs);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<BaseModel> bmList = (List<BaseModel>) messageHandlerSettingBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, mhs);
//
//		logger.info("RetrieveN MessageHandlerSetting error code=" + messageHandlerSettingBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageHandlerSettingBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("查询成功，mhsList=" + bmList);
//			params.put("mhsList", bmList);
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
//		params.put(KEY_HTMLTable_Parameter_msg, messageHandlerSettingBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/messageHandlerSetting/updateEx.bx?ID=1&
//	 * categoryID=1&template=消息10&link=http://www.bxit.vip/shop/order/ID/10
//	 */
//	@RequestMapping(value = "/updateEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String updateEx(@ModelAttribute("SpringWeb") MessageHandlerSetting mhs, ModelMap mm, HttpSession session) {
//		logger.info("修改一个消息处理配置表,mhs=" + mhs);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = messageHandlerSettingBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, mhs);
//
//		logger.info("Update MessageHandlerSetting error code=" + messageHandlerSettingBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageHandlerSettingBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("修改成功，mhs=" + bm);
//			params.put("mhs", bm);
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
//		params.put(KEY_HTMLTable_Parameter_msg, messageHandlerSettingBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	/*
//	 * User-Agent:Fiddler Host:localhost:8080
//	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
//	 * Type=GET:http://localhost:8080/nbr/messageHandlerSetting/deleteEx.bx?ID=1
//	 */
//	@RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String deleteEx(@ModelAttribute("SpringWeb") MessageHandlerSetting mhs, ModelMap mm, HttpSession session) {
//		logger.info("删除一个消息处理配置表,mhs=" + mhs);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		messageHandlerSettingBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, mhs);
//
//		logger.info("Delete MessageHandlerSetting error code=" + messageHandlerSettingBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (messageHandlerSettingBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("删除成功");
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
//		params.put(KEY_HTMLTable_Parameter_msg, messageHandlerSettingBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//}
