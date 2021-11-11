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
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//		logger.info("进入仓库主页时，加载多个的仓库，warehouse=" + warehouse);
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
//				logger.info("读取多个仓库失败，错误码=" + staffBO.getLastErrorCode());
//				mm.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode().toString());
//				break;
//			} else {
//				logger.info("读取多个仓库成功，staffList=" + staffList);
//			}
//
//			mm.put(KEY_ObjectList, staffList);
//			break;
//		default:
//			logger.info("其他错误");
//			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			break;
//		}
//		return new ModelAndView(WAREHOUSING_Index, "", null);
//	}

	@RequestMapping(value = "/warehouseStock", method = RequestMethod.GET) // ...warehouseStock可以简化命名
	public String warehouseStock(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入warehouse/warehouseStock页面时，加载所有大小类");

		Company company = getCompanyFromSession(session);

		Category c = new Category();
		c.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> categoryList = categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, c);
		if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多个类别失败,错误码=" + categoryBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, categoryBO.getLastErrorCode().toString());
			return "";
		} else {
			logger.info("读取多个类别成功，categoryList=" + categoryList);
		}

		CategoryParent cp = new CategoryParent();
		cp.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> categoryParentList = categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
		if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多个大类失败,错误码=" + categoryParentBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, categoryParentBO.getLastErrorCode().toString());
			return "";
		} else {
			logger.info("读取多个大类成功，categoryParentList=" + categoryParentList);
		}
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有员工
		// 去除虚拟总部
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
	 * sString1=库URL: http://localhost:8080/nbr/warehouse/retrieveNByFieldsEx.bx
	 * 根据商品的以下属性进行模糊搜索：ID,名称(id搜索只能是精确)
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
	// logger.info("其他错误！");
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
	 * Fiddler:BX_CustomerID=1&name=仓库1997&address=植物园&status=0&staffID=1 URL:
	 * http://localhost:8080/nbr/warehouse/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个仓库,warehouse=" + warehouse);

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
			logger.info("创建失败，修改的名称已存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoSuchData:
			logger.info("创建失败，创建的参数不存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("参数格式错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

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
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个仓库,warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		ErrorInfo ecOut = new ErrorInfo();
		Warehouse wh = null;
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(dbName);
		// ... TODO
		// 直接从普通缓存中拿数据，较为便利，但容易导致问题：比如，入库的ACTION修改了商品的库存，但是没有更新商品的普通缓存。这时，R1商品，永远不可能得到正确的库存。
		// 这对队友的要求较高，不适合现阶段用这个方法做
		if (!warehouseBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
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
					logger.info("查询一个员工失败，错误码=" + ecOut.getErrorCode().toString());
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
					params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
					break;
				} else {
					logger.info("查询一个员工成功！staff=" + staff);
					wh.setStaffName(staff == null ? "" : staff.getName());
				}
			}

			DataSourceContextHolder.setDbName(dbName);
			List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, new Staff());
			if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询多个员工失败，错误码=" + staffBO.getLastErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
				break;
			} else {
				logger.info("查询多个员工成功！staffList=" + staffList);
			}

			params.put(KEY_ObjectList, staffList);
			params.put(KEY_Object, wh);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);

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
//			logger.debug("无权访问本Action");
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
//			// "", "未知错误");
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
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取当前客户的多个仓库,warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Warehouse> warehouseList = (List<Warehouse>) warehouseBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询多个仓库成功，warehouseList=" + warehouseList);

			Staff staff = new Staff();
			for (int i = 0; i < warehouseList.size(); i++) {
				if (warehouseList.get(i).getStaffID() != 0) {
					staff.setID(warehouseList.get(i).getStaffID());
					DataSourceContextHolder.setDbName(company.getDbName());
					Staff s = (Staff) staffBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, staff);
					logger.info("retrieve1 staff error:" + staffBO.getLastErrorCode());
					if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("查询一个员工,错误码=" + staffBO.getLastErrorCode().toString());
						params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					} else {
						logger.info("查询一个员工成功！s=" + s);
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
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1&BX_CustomerID=1&name=仓库888&address=植物园2
	 * URL:http://localhost:8080/nbr/warehouse/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个仓库,warehouse=" + warehouse);

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = warehouseBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		logger.info("update warehouse error code=" + warehouseBO.getLastErrorCode());

		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).write1(bm, company.getDbName(), getStaffFromSession(session).getID());

			logger.info("修改成功，修改后的对象：" + bm);
			params.put("warehouse", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_Duplicated:
			logger.info("修改失败，修改的名称已存在");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoSuchData:
			logger.info("修改失败，修改的参数不正确");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("参数格式错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("默认的仓库不能被修改");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

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
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个仓库,warehouse=" + warehouse);

		Map<String, Object> params = new HashMap<String, Object>();

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		warehouseBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehouse);

		logger.info("delete warehouse error code=" + warehouseBO.getLastErrorCode());

		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).delete1(warehouse);

			logger.info("删除成功！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info(warehouseBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler:ID=1
	 * URL:http://localhost:8080/nbr/warehouse/retrieveInventoryEx.bx
	 * fMaxTotalInventory：库存总额最高的商品的库存总额(float1)
	 * fTotalInventory：本仓库的库存总额，按进货价(即采购价)计算(float2) string1：库存总额最高的商品
	 */
	@RequestMapping(value = "/retrieveInventoryEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveInventoryEx(@ModelAttribute("SpringWeb") Warehouse warehouse, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取最大的库存总额，库存总额，以及最大库存总额的商品，warehouse=" + warehouse);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = warehouseBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.CASE_Warehouse_RetrieveInventory, warehouse);
		logger.info("retrieveInventory  error code=" + warehouseBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (warehouseBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，bm=" + bm);
			params.put(BaseAction.KEY_Object, bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("没有权限进行操作");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
			break;
		default:
			logger.error("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehouseBO.getLastErrorMessage());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
