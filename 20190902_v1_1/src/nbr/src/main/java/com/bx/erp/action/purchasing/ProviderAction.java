package com.bx.erp.action.purchasing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/provider")
@Scope("prototype")
public class ProviderAction extends BaseAction {
	private Log logger = LogFactory.getLog(ProviderAction.class);

	@Resource
	private ProviderBO providerBO;
	@Resource
	private ProviderDistrictBO providerDistrictBO;

	// Fiddler URL: http://localhost:8080/nbr/provider.bx
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入供应商主页时，读取所有的供应商区域");

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		ProviderDistrict pd = new ProviderDistrict();
		pd.setPageIndex(PAGE_StartIndex);
		pd.setPageSize(PAGE_SIZE_Infinite);
		//
		List<?> ls = providerDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pd);

		logger.info("RetrieveN provider error code=" + providerDistrictBO.getLastErrorCode());
		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，ls=" + ls);
			mm.put("pdList", ls);
			mm.put("pd", pd);
			mm.put("provider", provider);
			break;
		default:
			logger.info("其他错误！");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		return new ModelAndView(PROVIDER_Index, "", null);
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/provider/toUpdate.bx
	 */
	@RequestMapping(value = "/toUpdate", method = RequestMethod.GET)
	public String toUpdate(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入供应商修改界面时带入数据");

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);
		if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询失败，错误码=" + providerBO.getLastErrorCode());
		} else {
			logger.info("读取成功，bm=" + bm);
		}

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		switch (providerBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，ls=" + ls);
			mm.put("provider", bm);
			mm.put("pdList", ls);
			return PROVIDER_ToUpdate;
		default:
			return PROVIDER_ToUpdate;
		}
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * ID=1&Name=华夏供应商&DistrictID=1&Address=广州方法&ContactName=kim12&Mobile=
	 * 11112123122312&providerDistrict=1 Request Body in Fiddler: URL: POST:
	 * http://localhost:8080/nbr/provider/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个供应商,provider=" + provider);
		
		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		do {
			String sProviderDistrict = GetStringFromRequest(req, "providerDistrict", null);
			//
			if (sProviderDistrict != null) {
				ProviderDistrict pd = new ProviderDistrict();
				pd.setName(sProviderDistrict);
				//
				DataSourceContextHolder.setDbName(dbName);
				ProviderDistrict providerDistrict = (ProviderDistrict) providerDistrictBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pd);
				logger.info("Create ProviderDistrict error code=" + providerBO.getLastErrorCode());
				if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
					break;
				}
				logger.info("创建成功，providerDistrict=" + providerDistrict);
				CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).write1(providerDistrict, dbName, getStaffFromSession(req.getSession()).getID());
				//
				provider.setDistrictID(providerDistrict.getID());
			}

			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = providerBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, provider);
			logger.info("Update Provider error code=" + providerBO.getLastErrorCode());
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				// throw new RuntimeException("对数据库进行错误的操作");
				if (sProviderDistrict != null) {
					logger.error("修改供应商信息失败。请运营删除失效数据,供应商地址(ProviderDistrict)ID=" + provider.getDistrictID());
				}
				//
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				break;
			}
			logger.info("修改成功，bm=" + bm);
			CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).write1(bm, dbName, getStaffFromSession(req.getSession()).getID());

			logger.info("修改成功！");
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:ID=2URL:
	 * http://localhost:8080/nbr/provider/deleteEx.bx?ID=8
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个供应商,provider=" + provider.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		DataSourceContextHolder.setDbName(dbName);
		providerBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		logger.info("Delete provider error code = " + providerBO.getLastErrorCode());

		switch (providerBO.getLastErrorCode()) {
		case EC_BusinessLogicNotDefined:
			logger.error("商品仍存在该供应商供应的商品，删除失败");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
			break;
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).delete1(provider);

			logger.info("删除成功！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限!");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + providerBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	// * http://localhost:8080/nbr/provider/deleteListEx.bx?providerListID=8,9
	// */
	// @RequestMapping(value = "/deleteListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") Provider provider,
	// ModelMap model, HttpServletRequest req) {
	// logger.info("删除多个供应商,provider=" + provider.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sProviderIDs = GetStringFromRequest(req, "providerListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sProviderIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("sProviderIDs为空");
	// return "";
	// } else {
	// logger.info("sProviderIDs=" + sProviderIDs);
	// }
	//
	// int[] iArrProviderID = toIntArray(sProviderIDs);
	// if (iArrProviderID == null) {
	// logger.info("iArrProviderID数据格式不匹配");
	// return "";
	// } else {
	// logger.info("iArrProviderID=" + iArrProviderID);
	// }
	//
	// boolean hasDBError = false;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iID : iArrProviderID) {
	// Provider providers = new Provider();
	// providers.setID(iID);
	// DataSourceContextHolder.setDbName(dbName);
	// providerBO.deleteObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, providers);
	// switch (providerBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// sbErrorMsg.append("供应商" + iID + "删除失败，该供应商已有商品。<br />");
	// hasDBError = true;
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).delete1(providers);
	// break;
	// case EC_NoPermission:
	// sbErrorMsg.append("你没有权限进行删除供应商。<br />");
	// hasDBError = true;
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("供应商" + iID + "删除失败，数据库发生错误。<br />");
	// hasDBError = true;
	// break;
	// }
	// }
	// //
	// params.put("msg", sbErrorMsg.toString());
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// 前端必须显示msg给用户看
	// break;
	// }
	//
	// if (sbErrorMsg.length() == 0) {
	// logger.info("全部供应商删除成功");
	// }
	// //
	// logger.info(sbErrorMsg.toString());
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());//
	// 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/provider/toCreate.bx
	 */
	@RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	public String toCreate(Provider provider, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入供应商修改界面时带入供应商区域数据");

		Company company = getCompanyFromSession(session);

		ProviderDistrict pd = new ProviderDistrict();
		pd.setPageIndex(PAGE_StartIndex);
		pd.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = providerDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pd);

		logger.info("Create Provider error code = " + providerDistrictBO.getLastErrorCode());

		switch (providerDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			mm.put("pdList", ls);
			logger.info("ls:" + ls);
			break;
		default:
			logger.info("未知错误！");
			break;
		}
		return PROVIDER_ToCreate;
	}

	/*
	 * Request Body in Fiddler:
	 * ID=0&name=淘宝1&districtID=1&address=广州市天河区二十八中学&contactName=ccc&mobile=
	 * 13125844692 POST: URL:http://localhost:8080/nbr/provider/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个供应商,provider=" + provider);

		Company company = getCompanyFromSession(session);

		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		logger.info("Create Provider error code = " + providerBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Provider).write1(bm, company.getDbName(), getStaffFromSession(session).getID());
			
			logger.info("新增成功！bm=" + bm);
			
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_WrongFormatForInputField:
			logger.error("新增失败，输入的供应商的格式不正确！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "输入的数据格式不正确");
			break; // ...
		case EC_BusinessLogicNotDefined:
			logger.error("新增失败，该供应商区域不存在：" + providerBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该供应商区域不存在，请重新选择");
			break; // ...
		case EC_Duplicated:
			logger.error("新增失败，该供应商已存在！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该供应商已存在，请重新修改");
			break;
		case EC_NoPermission:
			logger.error("没有权限！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误：" + providerBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/provider/retrieveN.bx
	 */
	@RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	public String retrieveN(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个供应商,provider=" + provider);

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		logger.info("RetrieveN Provider error code=" + providerBO.getLastErrorCode());

		switch (providerBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("ls:" + ls);
			return PROVIDER_RetrieveN;
		default:
			// "", "未知错误！");
			break;
		}
		return PROVIDER_ToRetrieveN;
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/provider/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个供应商,provider=" + provider);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Provider).read1(provider.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bm=" + bm);
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		default:
			logger.error("其他错误：" + ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL: POST:
	 * http://localhost:8080/nbr/provider/retrieveNEx.bx
	 */
	// FIXME
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("provider") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询多个供应商,provider=" + provider);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Provider> ls = (List<Provider>) providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询多个供应商失败：" + providerBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				break;
			}
			logger.info("查询成功,ls=" + ls);

			if (ls != null) {
				ProviderDistrict pd = new ProviderDistrict();
				boolean bHasDBError = false;
				for (Provider p : ls) {
					pd.setID(p.getDistrictID());
					//
					DataSourceContextHolder.setDbName(company.getDbName());
					ProviderDistrict providerDistrict = (ProviderDistrict) providerDistrictBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pd);// ...目前1个供应商只有1个区域，不需要使用retrieveNObject
					if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("查询一个供应商区域失败：" + providerDistrictBO.printErrorInfo());
						params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
						params.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode().toString());
						bHasDBError = true;
						break;
					}
					logger.info("查询成功,providerDistrict=" + providerDistrict);

					p.setProviderDistrictName(providerDistrict.getName());
				}
				if (bHasDBError) {
					break;
				}
			} else {
				logger.info("ls=" + ls);
			}

			params.put(KEY_ObjectList, ls);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, providerBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * sString1=华 URL: http://localhost:8080/nbr/provider/retrieveNByFieldsEx.bx
	 * 根据供应商的以下属性进行模糊搜索：名称、联系方式、联系人
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("模糊搜索供应商,provider=" + provider);

		Company company = getCompanyFromSession(req.getSession());

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			String queryKeyword = GetStringFromRequest(req, Provider.field.getFIELD_NAME_queryKeyword(), null);
			if (queryKeyword == null) {
				logger.info(Provider.field.getFIELD_NAME_queryKeyword() + "值缺失");
				return "";
			} else {
				logger.info(Provider.field.getFIELD_NAME_queryKeyword() + "=" + queryKeyword);
			}

			provider.setQueryKeyword(queryKeyword);
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Provider> providerList = (List<Provider>) providerBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Provider_RetrieveNByFields, provider);
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("模糊搜索供应商失败，错误信息：" + providerBO.printErrorInfo());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				break;
			}
			logger.info("查询成功，providerList=" + providerList);

			ProviderDistrict pd = new ProviderDistrict();
			boolean bHasDBError = false;
			for (Provider p : providerList) {
				pd.setID(p.getDistrictID());
				//
				DataSourceContextHolder.setDbName(company.getDbName());
				ProviderDistrict providerDistrict = (ProviderDistrict) providerDistrictBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pd);
				if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个供应商区域失败：" + providerDistrictBO.printErrorInfo());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
					params.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode().toString());
					bHasDBError = true;
					break;
				} else {
					logger.info("查询成功，providerDistrict=" + providerDistrict);
				}

				p.setProviderDistrictName(providerDistrict == null ? "" : providerDistrict.getName());
			}
			if (bHasDBError) {
				break;
			}

			logger.info("providerList=" + providerList);
			params.put(BaseAction.KEY_ObjectList, providerList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, providerBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");// ...分页
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/provider/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		

		Company company = getCompanyFromSession(session);
		// 选传参数：ID，必传参数：string1和int1
		// 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
		// string1存储的是要检查的字段值，int1如下描述
		// int1=1，代表要检查供应商名称
		// int1=2，代表要检查联系人电话
		return doRetrieveNToCheckUniqueFieldEx(false, provider, company.getDbName(), session);
	}

	@Override
	protected BaseBO getBO() {
		return providerBO;
	}
}
