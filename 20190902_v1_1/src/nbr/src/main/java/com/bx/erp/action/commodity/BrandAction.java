package com.bx.erp.action.commodity;

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
import org.springframework.web.servlet.ModelAndView;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/brand")
@Scope("prototype")
public class BrandAction extends BaseAction {
	private Log logger = LogFactory.getLog(BrandAction.class);

	@Resource
	private BrandBO brandBO;

	/*
	 * User-Agent:Fiddler Host:localhost:8080
	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8 Request Body in
	 * Fiddler:ID=0&name=手表 Type=POST:http://localhost:8080/nbr/brand/createEx.bx
	 */
	// @RequestMapping(value = "/createEx", method = RequestMethod.POST)
	// @ResponseBody
	// public String createEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap mm)
	// {
	// BaseModel bm = brandBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, brand);
	//
	// logger.info("Create brand error code=" + brandBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (brandBO.getLastErrorCode()) {
	// case EC_WrongFormatForInputField:
	// // "", "输入的类别的格式不正确！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_WrongFormatForInputField.toString());
	// break;
	// case EC_Duplicated:
	// // "", "该类别已存在！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Brand).write1(bm);
	// // 将添加的品牌写入缓存
	//
	// logger.info("brand=" + bm);
	// mm.put("brand", bm);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/brand.bx
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") Brand brand, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入页面时加载所有的品牌,brand=" + brand);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = brandBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, brand);

		logger.info("RetrieveN brand error code=" + brandBO.getLastErrorCode());

		switch (brandBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("ls=" + ls);
			mm.put("brandList", ls);
			mm.put("brand", brand);
			break;
		default:
			logger.info("其他错误！");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		return new ModelAndView(BRAND_Index, "", new Brand());
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * 
	 */
	@RequestMapping(value = "/toUpdateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String toUpdateEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入修改页面时加载当前品牌,brand=" + brand);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = brandBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, brand);

		logger.info("toUpdate brand error code=" + brandBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (brandBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);

			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Brand).write1(bm, company.getDbName(), getStaffFromSession(session).getID()); // 将修改的品牌写入缓存

			logger.info("Brand=" + bm);
			params.put("brand", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
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
		params.put(KEY_HTMLTable_Parameter_msg, brandBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * brand.ID=2&brand.name=家具
	 * Type=POST:http://localhost:8080/nbr/brand/updateEx.bx
	 */
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap
	// model) {
	// BaseModel bm = brandBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, brand);
	//
	// logger.info("Update brand error code=" + brandBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (brandBO.getLastErrorCode()) {
	// case EC_Duplicated:
	// logger.info("修改失败，该品牌已经存在");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Brand).write1(bm);
	// // 将修改的品牌写入缓存
	//
	// logger.info("修改成功。Brand=" + bm);
	// params.put("brand", bm);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * User-Agent:Fiddler Host:localhost:8080
	 * Content-Type:application/x-www-form-urlencoded;charset=utf-8 Request Body in
	 * Fiddler:brand.ID=2 Type=POST:http://localhost:8080/nbr/brand/deleteEx.bx
	 */
	// @RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap
	// model) {
	// brandBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// brand);
	//
	// logger.info("Delete brand error code=" + brandBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (brandBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// // "", "想删除的品牌中仍存在商品，删除失败");
	// logger.info("删除失败");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Brand).delete1(brand);
	// //
	// 将删除的品牌在缓存里删除
	//
	// logger.info("删除成功");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/brand/retrieve1.bx
	 */
	@RequestMapping(value = "/retrieve1", method = RequestMethod.GET)
	public String retrieve1(@ModelAttribute("SpringWeb") Brand brand, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个品牌，brand=" + brand);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Brand).read1(brand.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName()); // 从缓存中拿到查询的品牌
		logger.info("retrieve Brand error code=" + ecOut.getErrorCode());

		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			return BRAND_Retrieve1;
		default:
			return BRAND_Retrieve1;
		}

	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/brand/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个品牌,brand=" + brand);

		Company company = getCompanyFromSession(session);
		ErrorInfo ecOut = new ErrorInfo();
		Map<String, Object> params = new HashMap<String, Object>();

		BaseModel bm = null;
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!brandBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {

			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Brand).read1(brand.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName()); // 从缓存中拿到查询的品牌
		}

		logger.info("retrieve brand error code=" + ecOut.getErrorCode());

		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);

			params.put("brand", bm == null ? "" : bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/brand/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个品牌，brand=" + brand);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = brandBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, brand);

		logger.info("retrieveAll Brands error code=" + brandBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (brandBO.getLastErrorCode()) {
		case EC_NoError:
			for (Object obj : ls) {
				BaseModel bm = (BaseModel) obj;
				bm.setSyncDatetime(new Date());
			}

			params.put("brandList", ls);
			params.put("count", brandBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
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
		params.put(KEY_HTMLTable_Parameter_msg, brandBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		logger.debug(JSONObject.fromObject(params, JsonUtil.jsonConfig).toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
