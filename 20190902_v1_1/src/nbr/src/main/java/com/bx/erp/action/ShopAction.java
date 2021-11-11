package com.bx.erp.action;

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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.BxStaffBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.ShopDistrictBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/shop")
@Scope("prototype")
public class ShopAction extends BaseAction {
	private Log logger = LogFactory.getLog(ShopAction.class);
	@Resource
	private ShopBO shopBO;

	@Resource
	private PosBO posBO;

	@Resource
	private BxStaffBO bxStaffBO;

	@Resource
	protected com.bx.erp.cache.PosSyncCache posSyncCache;

	@Resource
	private ShopDistrictBO shopDistrictBO;

	@Resource
	private CompanyBO companyBO;

	@SuppressWarnings("unchecked")
	@RequestMapping(produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		//
		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		List<BxStaff> bxStaffList = (List<BxStaff>) bxStaffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);

		ShopDistrict shopDistrict = new ShopDistrict();
		shopDistrict.setPageIndex(PAGE_StartIndex);
		shopDistrict.setPageSize(PAGE_SIZE_Infinite);
		Company company = getCompanyFromSession(session);
		DataSourceContextHolder.setDbName(company.getDbName());
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		List<?> shopDistrictList = shopDistrictBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shopDistrict);
		logger.info("retrieveN shopDistrict error code=" + shopDistrictBO.getLastErrorCode());

		switch (shopDistrictBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功,bxStaffList=" + bxStaffList);
			mm.put("shopDistrictList", shopDistrictList);
			mm.put("shopDistrict", shopDistrict);
			mm.put("bxStaffList", bxStaffList);
			mm.put("shop", new Shop());
			mm.put("company", new Company());
			mm.put("pos", new Pos());
			break;
		default:
			logger.info("其他错误");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		return new ModelAndView(COMPANY_Index, "", "");
	}

	/*
	 * Request Body in Fiddler:
	 * name=博昕&address=岗顶&status=1&longitude=12.12&latitude=123.123&key=123456 POST:
	 * URL:http://localhost:8080/nbr/shop/createEx.bx
	 */
	// @RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String createEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm,
	// HttpSession session) {
	// logger.info("创建一个门店,shop=" + shop.toString());
	//
	// String dbName = getDBNameFromSession(session);
	//
	// DataSourceContextHolder.setDbName(dbName);
	// BaseModel bm = shopBO.createObject(getBxStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, shop);
	// logger.info("Create Shop error code =" + shopBO.getLastErrorCode());
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// switch (shopBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// logger.info("新增失败，该公司不存在！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// break;
	// case EC_Duplicated:
	// logger.info("新增失败，该公司该门店已存在！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	// break;
	// case EC_NoError:
	// logger.info("新增成功！bm=" + bm == null ? "null" : bm.toString());
	//
	// params.put("shop", bm);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// logger.info("没有权限");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// logger.info("未知错误");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: POST http://localhost:8080/nbr/shop/deleteEx.bx
	 */
	// @RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap
	// model, HttpSession session) {
	// logger.info("删除一个门店，shop=" + shop.toString());
	// String dbName = getDBNameFromSession(session);
	//
	// DataSourceContextHolder.setDbName(dbName);
	// List<List<BaseModel>> listbm =
	// shopBO.deleteObjectEx(getBxStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, shop);
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// switch (shopBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("删除门店成功");
	//
	// // 删除pos的普通缓存
	// List<BaseModel> posList = (List<BaseModel>) listbm.get(0);
	// for (int i = 0; i < posList.size(); i++) {
	// CacheManager.getCache(dbName, EnumCacheType.ECT_POS).delete1(posList.get(i));
	// }
	// // 删除staff的普通缓存
	// List<BaseModel> staffList = (List<BaseModel>) listbm.get(1);
	// for (int i = 0; i < staffList.size(); i++) {
	// CacheManager.getCache(dbName,
	// EnumCacheType.ECT_Staff).delete1(staffList.get(i));
	// }
	//
	// // 重新加载pos和staff的同步块缓存
	// if (!posSyncCache.loadAll(dbName)) {
	// logger.error("重新加载pos的同步块缓存失败，传入的shop=" + shop.toString());
	// // ...
	// }
	// if (!staffSyncCache.loadAll(dbName)) {
	// logger.error("重新加载staff的同步块缓存失败，传入的shop=" + shop.toString());
	// // ...
	// }
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// logger.info("没有权限进行删除");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// logger.info("其他错误");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler:
	 * name=博昕&address=岗顶&ID=1&longitude=12.12&latitude=123.123&key=123456 POST:
	 * URL:http://localhost:8080/nbr/shop/updateEx.bx
	 */
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm,
	// HttpSession session) {
	// logger.info("修改一个门店，shop=" + shop.toString());
	//
	// String dbName = getDBNameFromSession(session);
	//
	// DataSourceContextHolder.setDbName(dbName);
	// Shop s = (Shop) shopBO.updateObject(getBxStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, shop);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (shopBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// logger.info("新增失败，该公司不存在！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// break;
	// case EC_Duplicated:
	// logger.info("创建失败，该公司该门店名称重复");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	// break;
	// case EC_NoError:
	// logger.info("修改成功,s=" + s == null ? "" : s);
	//
	// params.put(KEY_ObjectList, s);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// logger.info("没有权限");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// logger.info("其他错误");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// logger.info("返回的数据=" + params.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: POST
	 * http://localhost:8080/nbr/shop/retrieve1Ex.bx?ID=1 string1 为前端传递过来的DB name;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个门店以及对应的pos和公司，shop=" + shop);
		// 从缓存中读取所有company
		List<BaseModel> bmList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		boolean bDBNameValid = false;
		for (BaseModel bm : bmList) {
			Company c = (Company) bm;
			if (c.getDbName().equals(shop.getDbName())) {
				bDBNameValid = true;
				break;
			}
		}
		if (!bDBNameValid) {
			return null;
		}

		DataSourceContextHolder.setDbName(shop.getDbName());
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		// 前端需要传值回去，第一个list是门店，第二个list是对应的公司
		List<List<BaseModel>> listBM = shopBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);

		Map<String, Object> params = new HashMap<String, Object>();
		switch (shopBO.getLastErrorCode()) {
		case EC_NoError:
			// 读取出相关的门店
			Shop s = (listBM.get(0).size() > 0 ? (Shop) listBM.get(0).get(0) : new Shop());
			logger.info("查询一个门店查询成功,s=" + s);

//			// 根据门店表里面的门店区域id查出门店区域名称
//			ShopDistrict sd = new ShopDistrict();
//			sd.setID(s.getDistrictID());
//			DataSourceContextHolder.setDbName(shop.getDbName());
//			// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
//			ShopDistrict shopDistrict = (ShopDistrict) shopDistrictBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sd);// ...目前1个供应商只有1个区域，不需要使用retrieveNObject
//			if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.info("Retrieve 1 shopDistricts error code=" + shopDistrictBO.getLastErrorCode());
//				params.put(KEY_HTMLTable_Parameter_msg, shopDistrictBO.getLastErrorMessage());
//				params.put(BaseAction.JSON_ERROR_KEY, shopDistrictBO.getLastErrorCode().toString());
//				break;
//			} else {
//				logger.info("查询成功shopDistrict=" + shopDistrict);
//			}
			//
			// 根据门店表里面的门店区域id查出门店区域名称
			ShopDistrict sd = new ShopDistrict();
			sd.setPageIndex(BaseAction.PAGE_StartIndex);
			sd.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(shop.getDbName());
			// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
			List<ShopDistrict> shopDistrictList = (List<ShopDistrict>) shopDistrictBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sd);// ...目前1个供应商只有1个区域，不需要使用retrieveNObject
			if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("Retrieve 1 shopDistricts error code=" + shopDistrictBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, shopDistrictBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, shopDistrictBO.getLastErrorCode().toString());
				break;
			} else {
				logger.info("查询成功shopDistrict=" + shopDistrictList);
			}
			//
			for(ShopDistrict shopDistrict : shopDistrictList) {
				if(shopDistrict.getID() == s.getDistrictID()) {
					s.setDistrictName(shopDistrict.getName());
				}
			}

			// 读取出门店相关的公司
			Company company = (listBM.get(1).size() > 0 ? (Company) listBM.get(1).get(0) : null);
			if (company != null) {
				company.setKey("");
				company.setDbUserPassword("");
				company.setDbUserName("");
			}
			s.setCompany(company);

			// 读取相应的业务经理名称
			BxStaff bxStaff = (listBM.get(2).size() > 0 ? (BxStaff) listBM.get(2).get(0) : new BxStaff());
			s.setBxStaffName(bxStaff.getName());

			// 查出所有对应的POS机
			Pos pos = new Pos();
			pos.setShopID(s.getID());
			pos.setStatus(EnumStatusPos.ESP_Active.getIndex());
			DataSourceContextHolder.setDbName(shop.getDbName());
			// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
			List<Pos> posList = (List<Pos>) posBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pos);
			if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取相应的门店下的所有POS机失败，错误码=" + posBO.getLastErrorCode().toString());

				params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, posBO.getLastErrorCode().toString());
				break;
			}
			s.setListPos(posList);

			// 将敏感信息隐藏
//			if (s != null) {
//				s.setKey("");
//			}
			params.put(KEY_Object, s);
			params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put("shopDistrictList", shopDistrictList);
			break;
		case EC_NoPermission:
			logger.error("没有权限");
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.error("其他错误");
			params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	 @RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	 @ResponseBody
	 public String retrieveNEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpServletRequest req) {
		 if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
				logger.debug("无权访问本Action");
				return null;
		 }
		 
		 logger.info("查询多个门店，shop=" + shop.toString());
		 //
		 Company company = getCompanyFromSession(req.getSession());
		 Map<String, Object> params = getDefaultParamToReturn(true);
		 //
		 do {
			 DataSourceContextHolder.setDbName(company.getDbName());
			 List<List<BaseModel>> listBM = shopBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
			 if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("搜索门店失败，错误信息：" + shopBO.printErrorInfo());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
					params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode().toString());
					break;
			 }
			 //
			 List<BaseModel> listShop = listBM.get(0);
			 for (BaseModel bm : listShop) {
					Shop s = (Shop) bm;
					// 将shop的敏感信息隐藏
					s.setKey("");
			 }
			params.put(KEY_ObjectList, listShop);
			params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, shopBO.getTotalRecord());
			logger.info("返回的数据=" + params.toString());
		 } while (false);
		 return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	 }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("模糊搜索门店,shop=" + shop);

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

			shop.setQueryKeyword(queryKeyword);
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Shop> shopList = (List<Shop>) shopBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Shop_RetrieveNByFields, shop);
			if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("模糊搜索门店失败，错误信息：" + shopBO.printErrorInfo());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode().toString());
				break;
			}
			logger.info("查询成功，shopList=" + shopList);

			params.put(BaseAction.KEY_ObjectList, shopList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, shopBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");// ...分页
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@Override
	protected BaseBO getBO() {
		return shopBO;
	}
}
