package com.bx.erp.action.config;

import java.util.ArrayList;
import java.util.Date;
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
import com.bx.erp.action.bo.config.ConfigGeneralBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/configGeneral")
@Scope("prototype")
public class ConfigGeneralAction extends ConfigAction {
	private Log logger = LogFactory.getLog(ConfigGeneralAction.class);

	@Resource
	private ConfigGeneralBO configGeneralBO;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * configGeneral.value=200
	 * Type=POST:http://localhost:8080/nbr/configGeneral/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") ConfigGeneral configGeneral, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个普通配置表，configGeneral=" + configGeneral);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = configGeneralBO.updateObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, configGeneral);

		logger.info("Update configGeneral error code=" + configGeneralBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (configGeneralBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("修改成功! ConfigGeneral=" + bm);
			params.put(BaseAction.KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			// 更新缓存
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ConfigGeneral).delete1(configGeneral);
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, configGeneralBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * Type=POST:http://localhost:8080/nbr/configGeneral/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") ConfigGeneral configGeneral, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个普通配置表，configGeneral=" + configGeneral);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = configGeneralBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, configGeneral);

		logger.info("Retrieve1 configGeneral error code=" + configGeneralBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (configGeneralBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);
			params.put("configGeneral", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, configGeneralBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * Type=POST:http://localhost:8080/nbr/configGeneral/retrieveNEx.bx?int1=1
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") ConfigGeneral configGeneral, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个普通配置表，configGeneral=" + configGeneral);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		Map<String, Object> params = new HashMap<String, Object>();
		List<BaseModel> cgList = new ArrayList<BaseModel>();

		
		switch (configGeneral.getIsRequestFromPos()) {
		case 1: // TODO ...hard code，当isRequestFromPos为1的时候，是pos机发送的请求，只需要返回需要的配置，这里需要声明一个常量。
			BaseModel activeSmallsheetID = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.ACTIVE_SMALLSHEET_ID, getStaffFromSession(session).getID(), ecOut, company.getDbName());
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("从缓存中读取配置信息错误！(activeSmallsheetID),错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return null;
			} else {
				logger.info("查询成功。activeSmallsheetID=" + activeSmallsheetID);
			}
			activeSmallsheetID.setSyncDatetime(new Date());
			cgList.add(activeSmallsheetID);

			params.put(KEY_ObjectList, cgList);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			
			DataSourceContextHolder.setDbName(company.getDbName());
			List<?> ls = configGeneralBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, configGeneral);

			logger.info("RetrieveN configGeneral error code=" + configGeneralBO.getLastErrorCode());

			switch (configGeneralBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("查询成功! ConfigGeneralList=" + ls);
				params.put("cgList", ls);
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				break;
			default:
				logger.info("其他错误！");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, configGeneralBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
