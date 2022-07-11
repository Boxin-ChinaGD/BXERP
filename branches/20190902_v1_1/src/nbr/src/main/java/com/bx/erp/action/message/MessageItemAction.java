package com.bx.erp.action.message;

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
import com.bx.erp.action.bo.message.MessageItemBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/messageItem")
@Scope("prototype")
public class MessageItemAction extends BaseAction{
	private Log logger = LogFactory.getLog(MessageItemAction.class);

	@Resource
	private MessageItemBO messageItemBO;
	
	/*
	 * User-Agent:Fiddler Host:localhost:8080
	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8 Request Body in
	 * Fiddler:categoryID=1&isRead=1&Parameter=Json&senderID=1&receiverID=2
	 * Type=POST:http://localhost:8080/nbr/messageItem/createEx.bx
	 */
	@RequestMapping(value = "/createEx", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") MessageItem messageItem, ModelMap mm, HttpSession session) {
		logger.info("创建一个信息项,messageItem=" + messageItem);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = messageItemBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, messageItem);

		logger.info("Create messageItem error code=" + messageItemBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (messageItemBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建成功，messageItem=" + bm);
			params.put(BaseAction.KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, messageItemBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	
	/*
	 * User-Agent:Fiddler Host:localhost:8080
	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8
	 * Type=GET:http://localhost:8080/nbr/messageItem/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") MessageItem messageItem, ModelMap mm, HttpSession session) {
		logger.info("读取多个信息项，messageItem=" + messageItem);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<BaseModel> bmList = (List<BaseModel>) messageItemBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, messageItem);

		logger.info("retrieveN message error code=" + messageItemBO.printErrorInfo());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (messageItemBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("messageItemList=" + bmList);
			params.put(BaseAction.KEY_ObjectList, bmList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_TotalRecord, messageItemBO.getTotalRecord());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("传入的消息分类ID不正确");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, messageItemBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
