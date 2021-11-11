//package com.bx.erp.action.wx;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.wx.WxSecondaryCategoryBO;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.WxSecondaryCategory;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wxSecondaryCategory")
//public class WxSecondaryCategoryAction extends BaseAction {
//	private Log logger = LogFactory.getLog(WxSecondaryCategoryAction.class);
//
//	@Resource
//	private WxSecondaryCategoryBO wxSecondaryCategoryBO;
//
//	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") WxSecondaryCategory wxSecondaryCategory, ModelMap mm, HttpSession session) {
//		logger.info("创建一个二级类目,WxSecondaryCategory=" + wxSecondaryCategory.toString());
//
//		DataSourceContextHolder.setDbName(getCompanyFromSession(session).getDbName());
//		BaseModel bm = wxSecondaryCategoryBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxSecondaryCategory);
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (wxSecondaryCategoryBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("创建成功,bm=" + bm);
//			params.put(KEY_Object, bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoSuchData:
//			logger.info("数据不存在");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
//			break;
//		case EC_WrongFormatForInputField:
//			logger.info("字段验证错误");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
//			break;
//		default:
//			logger.info("其他错误");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, wxSecondaryCategoryBO.getLastErrorMessage().toString());
//		logger.info("返回的数据=" + params.toString());
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieve1Ex(@ModelAttribute("SpringWeb") WxSecondaryCategory wxSecondaryCategory, ModelMap mm, HttpSession session) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		DataSourceContextHolder.setDbName(getCompanyFromSession(session).getDbName());
//		BaseModel bm = wxSecondaryCategoryBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wxSecondaryCategory);
//		switch (wxSecondaryCategoryBO.getLastErrorCode()) {
//		case EC_NoError:
//			params.put(KEY_Object, bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		default:
//			logger.info("其他错误");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, wxSecondaryCategoryBO.getLastErrorMessage().toString());
//		logger.info("返回的数据=" + params.toString());
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//}
