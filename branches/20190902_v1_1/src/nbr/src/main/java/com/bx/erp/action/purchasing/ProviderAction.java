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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????????????????????????????");

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
			logger.info("???????????????ls=" + ls);
			mm.put("pdList", ls);
			mm.put("pd", pd);
			mm.put("provider", provider);
			break;
		default:
			logger.info("???????????????");
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????????????????????????????");

		Company company = getCompanyFromSession(session);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);
		if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("????????????????????????=" + providerBO.getLastErrorCode());
		} else {
			logger.info("???????????????bm=" + bm);
		}

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		switch (providerBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????ls=" + ls);
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
	 * ID=1&Name=???????????????&DistrictID=1&Address=????????????&ContactName=kim12&Mobile=
	 * 11112123122312&providerDistrict=1 Request Body in Fiddler: URL: POST:
	 * http://localhost:8080/nbr/provider/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);
		
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
				logger.info("???????????????providerDistrict=" + providerDistrict);
				CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).write1(providerDistrict, dbName, getStaffFromSession(req.getSession()).getID());
				//
				provider.setDistrictID(providerDistrict.getID());
			}

			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = providerBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, provider);
			logger.info("Update Provider error code=" + providerBO.getLastErrorCode());
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				// throw new RuntimeException("?????????????????????????????????");
				if (sProviderDistrict != null) {
					logger.error("?????????????????????????????????????????????????????????,???????????????(ProviderDistrict)ID=" + provider.getDistrictID());
				}
				//
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				break;
			}
			logger.info("???????????????bm=" + bm);
			CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).write1(bm, dbName, getStaffFromSession(req.getSession()).getID());

			logger.info("???????????????");
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("???????????????=" + params);

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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		DataSourceContextHolder.setDbName(dbName);
		providerBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		logger.info("Delete provider error code = " + providerBO.getLastErrorCode());

		switch (providerBO.getLastErrorCode()) {
		case EC_BusinessLogicNotDefined:
			logger.error("?????????????????????????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
			break;
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).delete1(provider);

			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("????????????!");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????" + providerBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

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
	// logger.info("?????????????????????,provider=" + provider.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sProviderIDs = GetStringFromRequest(req, "providerListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sProviderIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("sProviderIDs??????");
	// return "";
	// } else {
	// logger.info("sProviderIDs=" + sProviderIDs);
	// }
	//
	// int[] iArrProviderID = toIntArray(sProviderIDs);
	// if (iArrProviderID == null) {
	// logger.info("iArrProviderID?????????????????????");
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
	// sbErrorMsg.append("?????????" + iID + "??????????????????????????????????????????<br />");
	// hasDBError = true;
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).delete1(providers);
	// break;
	// case EC_NoPermission:
	// sbErrorMsg.append("???????????????????????????????????????<br />");
	// hasDBError = true;
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("?????????" + iID + "???????????????????????????????????????<br />");
	// hasDBError = true;
	// break;
	// }
	// }
	// //
	// params.put("msg", sbErrorMsg.toString());
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// ??????????????????msg????????????
	// break;
	// }
	//
	// if (sbErrorMsg.length() == 0) {
	// logger.info("???????????????????????????");
	// }
	// //
	// logger.info(sbErrorMsg.toString());
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());//
	// ?????????????????????msg???????????????????????????????????????msg????????????????????????msg????????????
	// } while (false);
	// logger.info("???????????????=" + params);
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????????????????????????????");

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
			logger.info("???????????????");
			break;
		}
		return PROVIDER_ToCreate;
	}

	/*
	 * Request Body in Fiddler:
	 * ID=0&name=??????1&districtID=1&address=?????????????????????????????????&contactName=ccc&mobile=
	 * 13125844692 POST: URL:http://localhost:8080/nbr/provider/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);

		Company company = getCompanyFromSession(session);

		//
		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = providerBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);

		logger.info("Create Provider error code = " + providerBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (providerBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Provider).write1(bm, company.getDbName(), getStaffFromSession(session).getID());
			
			logger.info("???????????????bm=" + bm);
			
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_WrongFormatForInputField:
			logger.error("??????????????????????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????");
			break; // ...
		case EC_BusinessLogicNotDefined:
			logger.error("?????????????????????????????????????????????" + providerBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "?????????????????????????????????????????????");
			break; // ...
		case EC_Duplicated:
			logger.error("???????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????????????????????????????");
			break;
		case EC_NoPermission:
			logger.error("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????" + providerBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/provider/retrieveN.bx
	 */
	@RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	public String retrieveN(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);

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
			// "", "???????????????");
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Provider).read1(provider.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			logger.info("???????????????bm=" + bm);
			params.put(KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		default:
			logger.error("???????????????" + ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????");
			break;
		}
//		params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
		logger.info("???????????????=" + params);

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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Provider> ls = (List<Provider>) providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, provider);
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("??????????????????????????????" + providerBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				break;
			}
			logger.info("????????????,ls=" + ls);

			if (ls != null) {
				ProviderDistrict pd = new ProviderDistrict();
				boolean bHasDBError = false;
				for (Provider p : ls) {
					pd.setID(p.getDistrictID());
					//
					DataSourceContextHolder.setDbName(company.getDbName());
					ProviderDistrict providerDistrict = (ProviderDistrict) providerDistrictBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pd);// ...??????1??????????????????1???????????????????????????retrieveNObject
					if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("????????????????????????????????????" + providerDistrictBO.printErrorInfo());
						params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
						params.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode().toString());
						bHasDBError = true;
						break;
					}
					logger.info("????????????,providerDistrict=" + providerDistrict);

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
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * sString1=??? URL: http://localhost:8080/nbr/provider/retrieveNByFieldsEx.bx
	 * ????????????????????????????????????????????????????????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????,provider=" + provider);

		Company company = getCompanyFromSession(req.getSession());

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			String queryKeyword = GetStringFromRequest(req, Provider.field.getFIELD_NAME_queryKeyword(), null);
			if (queryKeyword == null) {
				logger.info(Provider.field.getFIELD_NAME_queryKeyword() + "?????????");
				return "";
			} else {
				logger.info(Provider.field.getFIELD_NAME_queryKeyword() + "=" + queryKeyword);
			}

			provider.setQueryKeyword(queryKeyword);
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Provider> providerList = (List<Provider>) providerBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Provider_RetrieveNByFields, provider);
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("?????????????????????????????????????????????" + providerBO.printErrorInfo());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
				break;
			}
			logger.info("???????????????providerList=" + providerList);

			ProviderDistrict pd = new ProviderDistrict();
			boolean bHasDBError = false;
			for (Provider p : providerList) {
				pd.setID(p.getDistrictID());
				//
				DataSourceContextHolder.setDbName(company.getDbName());
				ProviderDistrict providerDistrict = (ProviderDistrict) providerDistrictBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pd);
				if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("????????????????????????????????????" + providerDistrictBO.printErrorInfo());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
					params.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode().toString());
					bHasDBError = true;
					break;
				} else {
					logger.info("???????????????providerDistrict=" + providerDistrict);
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
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");// ...??????
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/provider/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Provider provider, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		

		Company company = getCompanyFromSession(session);
		// ???????????????ID??????????????????string1???int1
		// ??????????????????????????????,??????????????????ID??????????????????????????????????????????????????????????????????????????????
		// string1????????????????????????????????????int1????????????
		// int1=1?????????????????????????????????
		// int1=2?????????????????????????????????
		return doRetrieveNToCheckUniqueFieldEx(false, provider, company.getDbName(), session);
	}

	@Override
	protected BaseBO getBO() {
		return providerBO;
	}
}
