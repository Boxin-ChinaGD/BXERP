package com.bx.erp.action;

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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ShopDistrictBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/shopDistrict")
@Scope("prototype")
public class ShopDistrictAction extends BaseAction {
	private Log logger = LogFactory.getLog(ShopDistrictAction.class);

	@Resource
	private ShopDistrictBO shopDistrictBO;
//
//	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String createEx(@ModelAttribute("SpringWeb") ShopDistrict shopDistrict, ModelMap model, HttpSession session) {
//		logger.info("创建一个门店区域,shopDistrict=" + shopDistrict);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		BaseModel bm = shopDistrictBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, shopDistrict);
//
//		logger.info("Create shopDistrict error code=" + shopDistrictBO.getLastErrorCode());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		switch (shopDistrictBO.getLastErrorCode()) {
//		case EC_NoError:
////			CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).write1(bm, dbName);
//			logger.info("添加成功！bm=" + bm);
//			params.put("shopDistrict", bm);
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//			break;
//		case EC_NoPermission:
//			logger.info("没有权限");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
//			break;
//		case EC_Duplicated:
//			logger.info("添加失败,已有相同名称的区域");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
//			break;
//		default:
//			logger.info("其他错误");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
//			break;
//		}
//		params.put(KEY_HTMLTable_Parameter_msg, shopDistrictBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//
//	
	@RequestMapping(value = "/retrieveNEx",produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") ShopDistrict shopDistrict, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询所有门店区域，shop=" + shopDistrict.toString());
		//
		Company company = getCompanyFromSession(req.getSession());
		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			List<?> sdList = shopDistrictBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, shopDistrict);
			 if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("搜索门店区域失败，错误信息：" + shopDistrictBO.printErrorInfo());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopDistrictBO.getLastErrorMessage());
					params.put(BaseAction.JSON_ERROR_KEY, shopDistrictBO.getLastErrorCode().toString());
					break;
			}
			logger.info("retrieveN shopDistrict error code=" + shopDistrictBO.getLastErrorCode());
			//
			params.put(KEY_ObjectList, sdList);
			params.put(BaseAction.JSON_ERROR_KEY, shopDistrictBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, shopDistrictBO.getTotalRecord());
			logger.info("返回的数据=" + params.toString());
		}while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
