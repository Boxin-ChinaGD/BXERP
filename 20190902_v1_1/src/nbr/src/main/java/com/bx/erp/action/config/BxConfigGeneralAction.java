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
import com.bx.erp.action.bo.config.BxConfigGeneralBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bxConfigGeneral")
@Scope("prototype")
public class BxConfigGeneralAction extends ConfigAction {
	private Log logger = LogFactory.getLog(BxConfigGeneralAction.class);

	@Resource
	private BxConfigGeneralBO bxConfigGeneralBO;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * bxCconfigGeneral.value=200
	 * Type=POST:http://localhost:8080/nbr/bxConfigGeneral/updateEx.bx
	 */
	// 公共DB配置表暂时不支持修改
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") BxConfigGeneral
	// bxConfigGeneral, ModelMap mm, HttpSession session) {
	// logger.info("修改一个普通配置表，bxCconfigGeneral=" + bxConfigGeneral.toString());
	//
	// BaseModel bm = bxConfigGeneralBO.updateObject(BaseBO.SYSTEM,
	// BaseBO.INVALID_CASE_ID, bxConfigGeneral);
	//
	// logger.info("Update bxCconfigGeneral error code=" +
	// bxConfigGeneralBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (bxConfigGeneralBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("修改成功! BXCconfigGeneral=" + bm);
	// params.put("bxCconfigGeneral", bm);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// params.put(KEY_HTMLTable_Parameter_msg,
	// bxConfigGeneralBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * Type=POST:http://localhost:8080/nbr/bxConfigGeneral/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") BxConfigGeneral bxConfigGeneral, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个普通配置表，bxCconfigGeneral=" + bxConfigGeneral);

		BaseModel bm = bxConfigGeneralBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bxConfigGeneral);

		logger.info("Retrieve1 bxCconfigGeneral error code=" + bxConfigGeneralBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (bxConfigGeneralBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + (bm == null ? "" : bm));
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证不通过 ");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, bxConfigGeneralBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * Type=POST:http://localhost:8080/nbr/bxConfigGeneral/retrieveNEx.bx?int1=1
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") BxConfigGeneral bxConfigGeneral, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex() | BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个普通配置表，bxCconfigGeneral=" + bxConfigGeneral);

		Map<String, Object> params = new HashMap<String, Object>();

		List<?> ls = bxConfigGeneralBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bxConfigGeneral);

		logger.info("RetrieveN bxCconfigGeneral error code=" + bxConfigGeneralBO.getLastErrorCode());
		switch (bxConfigGeneral.getIsRequestFromPos()) {
		case 1: // TODO ...hard code，当isRequestFromPos为1的时候，是pos机发送的请求，只需要返回需要的配置，这里需要声明一个常量。
			ErrorInfo ecOut = new ErrorInfo();
			List<BaseModel> ccsList = new ArrayList<BaseModel>();

			BaseModel smallSheetNumber = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MAX_SmallSheetNumber, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("从缓存中读取配置信息错误！(SmallSheetNumber),错误码=" + ecOut.getErrorCode());
				return null;
			} else {
				logger.info("查询成功!smallSheetNumber=" + (smallSheetNumber == null ? "" : smallSheetNumber));
			}
			smallSheetNumber.setSyncDatetime(new Date());
			ccsList.add(smallSheetNumber);

			BaseModel smallSheetLogolum = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MAX_SmallSheetLogoVolume, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("从缓存中读取配置信息错误！(SmallSheetLogoVolume),错误码=" + ecOut.getErrorCode());
				return null;
			} else {
				logger.info("查询成功!smallSheetLogolum=" + (smallSheetLogolum == null ? "" : smallSheetLogolum));
			}
			smallSheetLogolum.setSyncDatetime(new Date());
			ccsList.add(smallSheetLogolum);

			params.put(KEY_ObjectList, ccsList);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			switch (bxConfigGeneralBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("查询成功! BXCconfigGeneralList=" + ls);
				params.put("cgList", ls);
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				break;
			default:
				logger.info("其他错误！");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			params.put(KEY_HTMLTable_Parameter_msg, bxConfigGeneralBO.getLastErrorMessage());
			logger.info("返回的数据=" + params);
			break;
		}
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
