package com.bx.erp.action.warehousing;

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
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.warehousing.WarehouseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/warehouse")
@Scope("prototype")
public class WarehouseAction extends BaseAction { // Warehouse>{
	private Log logger = LogFactory.getLog(WarehouseAction.class);

	@Resource
	private WarehouseBO warehouseBO;

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private CategoryParentBO categoryParentBO;

	@Resource
	private StaffBO staffBO;

//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView index(Warehouse warehouse, ModelMap mm, HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("???????????????Action");
//			return null;
//		}
//		
//		logger.info("????????????????????????????????????????????????warehouse=" + warehouse);
//
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<?> ls = warehouseBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);
//
//		logger.info("RetrieveN warehouse error code=" + warehouseBO.getLastErrorCode());
//
//		switch (warehouseBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info("ls=" + ls);
//			DataSourceContextHolder.setDbName(company.getDbName());
//			List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, new Staff());
//			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.info("????????????????????????????????????=" + staffBO.getLastErrorCode());
//				mm.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
//				break;
//			} else {
//				logger.info("???????????????????????????staffList=" + staffList);
//			}
//
//			mm.put(KEY_ObjectList, staffList);
//			break;
//		default:
//			logger.info("????????????");
//			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		return new ModelAndView(WAREHOUSING_Index, "", null);
//	}

	@RequestMapping(value = "/warehouseStock", method = RequestMethod.GET) // ...warehouseStock??????????????????
	public String warehouseStock(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????warehouse/warehouseStock?????????????????????????????????");

		Company company = getCompanyFromSession(session);

		Category c = new Category();
		c.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> categoryList = categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, c);
		if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("????????????????????????,?????????=" + categoryBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, categoryBO.getLastErrorCode().toString());
			return "";
		} else {
			logger.info("???????????????????????????categoryList=" + categoryList);
		}

		CategoryParent cp = new CategoryParent();
		cp.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> categoryParentList = categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
		if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("????????????????????????,?????????=" + categoryParentBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, categoryParentBO.getLastErrorCode().toString());
			return "";
		} else {
			logger.info("???????????????????????????categoryParentList=" + categoryParentList);
		}
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // ??????????????????????????????
		// ??????????????????
		for(int i = 0; i < shopList.size(); i++) {
			if(shopList.get(i).getID() == 1) {
				shopList.remove(i);
			}
		}
		mm.put("shopList", shopList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		mm.put("CommodityField", new CommodityField());
		return WAREHOUSING_warehouseStock;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * sString1=???URL: http://localhost:8080/nbr/warehouse/retrieveNByFieldsEx.bx
	 * ????????????????????????????????????????????????ID,??????(id?????????????????????)
	 */
	// @RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.POST)
	// @ResponseBody
	// public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") Warehouse
	// warehouse, ModelMap mm, HttpServletRequest req) {
	// String sValue = GetStringFromRequest(req, "sValue", null);
	// if (sValue == null) {
	// return "";
	// }
	//
	// warehouse.setString1(sValue);
	// warehouse.setBX_CustomerID(1);
	// List<?> warehouseList =
	// warehouseBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_RetrieveCommodityByNFields, warehouse);
	//
	// logger.info("retrieveNByFields warehouse error code=" +
	// warehouseBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("count", 0);
	// params.put("msg", "");
	// params.put("code", EnumErrorCode.EC_OtherError.toString());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// switch (warehouseBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("warehouseList=" + warehouseList);
	// params.put("warehouseList", warehouseList);
	// params.put("count", warehouseBO.getTotalRecord());
	// params.put("msg", "");
	// params.put("code", "0");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("???????????????");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// @Override
	// public Warehouse getModel() {
	// warehouseBO.setMapper();
	// if(warehouse == null) {
	// warehouse = new Warehouse();
	// }
	// return warehouse;
	// }

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in
	 * Fiddler:BX_CustomerID=1&name=??????1997&address=?????????&status=0&staffID=1 URL:
	 * http://localhost:8080/nbr/warehouse/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		Warehouse wh = (Warehouse) warehouseBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		logger.info("Create warehouse error code=" + warehouseBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).write1(wh, company.getDbName(), getStaffFromSession(session).getID());

			logger.info(wh);
			params.put("warehouse", wh);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			logger.info("???????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoSuchData:
			logger.info("???????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		case EC_NoPermission:
			logger.info("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("??????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1
	 * URL:http://localhost:8080/nbr/warehouse/retrieve1Ex.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		ErrorInfo ecOut = new ErrorInfo();
		Warehouse wh = null;
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(dbName);
		// ... TODO
		// ?????????????????????????????????????????????????????????????????????????????????????????????ACTION??????????????????????????????????????????????????????????????????????????????R1????????????????????????????????????????????????
		// ??????????????????????????????????????????????????????????????????
		if (!warehouseBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {
			params.put(KEY_HTMLTable_Parameter_msg, "????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			wh = (Warehouse) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).read1(warehouse.getID(), getStaffFromSession(session).getID(), ecOut, dbName);
			logger.info("retrieve1 warehouse result: " + ecOut.toString());
		}
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			if (warehouse.getStaffID() != 0) {
				Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(wh.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("????????????????????????????????????=" + ecOut.getErrorCode().toString());
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
					params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
					break;
				} else {
					logger.info("???????????????????????????staff=" + staff);
					wh.setStaffName(staff == null ? "" : staff.getName());
				}
			}

			DataSourceContextHolder.setDbName(dbName);
			List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, new Staff());
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("????????????????????????????????????=" + staffBO.getLastErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			} else {
				logger.info("???????????????????????????staffList=" + staffList);
			}

			params.put(KEY_ObjectList, staffList);
			params.put(KEY_Object, wh);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("???????????????");
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in
	 * Fiddler:BX_CustomerID=1 URL:http://localhost:8080/nbr/warehouse/retrieveN.bx
	 */
//	@RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
//	public String retrieveN(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("???????????????Action");
//			return null;
//		}
//		
//		Company company = getCompanyFromSession(session);
//
//		DataSourceContextHolder.setDbName(company.getDbName());
//		List<?> list = warehouseBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);
//
//		logger.info("retrieveN warehouse error code=" + warehouseBO.getLastErrorCode());
//
//		switch (warehouseBO.getLastErrorCode()) {
//		case EC_NoError:
//			logger.info(list);
//			mm.put("warehouseList", list);
//			return WAREHOUSE_RetrieveN;
//		default:
//			// "", "????????????");
//			break;
//		}
//
//		return "";// ...
//	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in
	 * Fiddler:BX_CustomerID=1
	 * URL:http://localhost:8080/nbr/warehouse/retrieveNEx.bx
	 */
	// FIXME:
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????,warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Warehouse> warehouseList = (List<Warehouse>) warehouseBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????????????????warehouseList=" + warehouseList);

			Staff staff = new Staff();
			for (int i = 0; i < warehouseList.size(); i++) {
				if (warehouseList.get(i).getStaffID() != 0) {
					staff.setID(warehouseList.get(i).getStaffID());
					DataSourceContextHolder.setDbName(company.getDbName());
					Staff s = (Staff) staffBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staff);
					logger.info("retrieve1 staff error:" + staffBO.getLastErrorCode());
					if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("??????????????????,?????????=" + staffBO.getLastErrorCode().toString());
						params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					} else {
						logger.info("???????????????????????????s=" + s);
					}
					warehouseList.get(i).setStaffName(s.getName());
				}
			}
			params.put("warehouseList", warehouseList);
			params.put("count", warehouseBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1&BX_CustomerID=1&name=??????888&address=?????????2
	 * URL:http://localhost:8080/nbr/warehouse/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,warehouse=" + warehouse);

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = warehouseBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		logger.info("update warehouse error code=" + warehouseBO.getLastErrorCode());

		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).write1(bm, company.getDbName(), getStaffFromSession(session).getID());

			logger.info("????????????????????????????????????" + bm);
			params.put("warehouse", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("???????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoSuchData:
			logger.info("???????????????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("??????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("??????????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1
	 * URL:http://localhost:8080/nbr/warehouse/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????,warehouse=" + warehouse);

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		warehouseBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		logger.info("delete warehouse error code=" + warehouseBO.getLastErrorCode());

		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).delete1(warehouse);

			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("????????????????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info(warehouseBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1
	 * URL:http://localhost:8080/nbr/warehouse/retrieveInventoryEx.bx
	 * fMaxTotalInventory?????????????????????????????????????????????(float1)
	 * fTotalInventory??????????????????????????????????????????(????????????)??????(float2) string1??????????????????????????????
	 */
	@RequestMapping(value = "/retrieveInventoryEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveInventoryEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????????????????????????????????????????????????????warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = warehouseBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.CASE_Warehouse_RetrieveInventory, warehouse);
		logger.info("retrieveInventory  error code=" + warehouseBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("???????????????bm=" + bm);
			params.put(BaseAction.KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("????????????????????????");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			break;
		default:
			logger.error("???????????????");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
			break;
		}
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
