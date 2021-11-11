package com.bx.erp.action.trade;

import java.util.Date;
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

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ShopDistrictBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CommodityShopInfoBO;
import com.bx.erp.action.bo.trade.PromotionBO;
import com.bx.erp.action.bo.trade.PromotionScopeBO;
import com.bx.erp.action.bo.trade.PromotionShopScopeBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.model.ShopField;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionField;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.PromotionShopScope;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/promotion")
@Scope("prototype")
public class PromotionAction extends BaseAction {
	private Log logger = LogFactory.getLog(PromotionAction.class);

	@Resource
	private PromotionBO promotionBO;

	@Resource
	private PromotionScopeBO promotionScopeBO;
	
	@Resource
	private PromotionShopScopeBO promotionShopScopeBO;

	@Resource
	private BarcodesBO barcodesBO;
	
	@Resource
	private ShopDistrictBO shopDistrictBO;
	
	@Resource
	private CommodityShopInfoBO commodityShopInfoBO;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);

		List<BaseModel> categoryList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> categoryParentList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).readN(true, false);
		ShopDistrict shopDistrictRnCondition = new ShopDistrict();
		shopDistrictRnCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		shopDistrictRnCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> shopDistrictList = shopDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, shopDistrictRnCondition);
		 if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("搜索门店区域失败，错误信息：" + shopDistrictBO.printErrorInfo());
		}
		mm.put("ShopField", new ShopField());
		mm.put("shopDistrictList", shopDistrictList);
		mm.put("PromotionField", new PromotionField());
		mm.put("CommodityField", new CommodityField());
		mm.put("categoryList", categoryList);
		mm.put("categoryParentList", categoryParentList);
		return PROMOTION_Index;
	}

	@RequestMapping(value = "/coupon", method = RequestMethod.GET)
	public String coupon(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		return PROMOTION_Coupon;
	}

	@RequestMapping(value = "/cashReducing", method = RequestMethod.GET)
	public String cashReducing(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		return PROMOTION_Index;
	}

	@RequestMapping(value = "/sheet", method = RequestMethod.GET)
	public String sheet(HttpSession session, ModelMap mm) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		return PROMOTION_Sheet;
	}

	@Resource
	private StaffBO staffBO;

	// /*
	// POST: URL:http://localhost:8080/nbr/promotion/create.bx
	// */
	// @RequestMapping(value = "/create", method = RequestMethod.POST)
	// public String create(@ModelAttribute("SpringWeb") Promotion promotiona,
	// String[] commodityIds, ModelMap model) {
	// String str = promotiona.checkCreate(BaseBO.INVALID_CASE_ID);
	// // 创建一个Set集合保存表单传过来的CommodityIds
	// Set<Integer> commodityIdSet = new HashSet<Integer>();
	// if (!("".equals(str))) {
	// model.put("errorMessage", str);
	// return PROMOTION_Create;
	// }
	// if (promotiona.getScope() == 1) {
	// for (String comdityId : commodityIds) {
	// try {
	// int commodtiyId = Integer.parseInt(comdityId);
	// commodityIdSet.add(commodtiyId);
	// } catch (Exception e) {
	// model.put("errorMessage", "商品ID有误！");
	// return PROMOTION_Create;
	// }
	// }
	// }
	// Promotion bm = (Promotion)
	// promotionBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, promotiona);
	// logger.info("Create Promotion error code = " +
	// promotionBO.getLastErrorCode());
	//
	// switch (promotionBO.getLastErrorCode()) {
	// case EC_NoError:
	// // 判断是否为全场满减
	// if (bm.getScope() == 1 && !commodityIdSet.isEmpty()) {
	// PromotionScope promotionScope = new PromotionScope();
	// promotionScope.setPromotionID(bm.getID());
	// Iterator<Integer> iterator = commodityIdSet.iterator();
	// while (iterator.hasNext()) {
	// Integer id = iterator.next();
	// promotionScope.setCommodityID(id);
	// promotionScopeBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, promotionScope);
	// logger.info("Create Promotion error code = " +
	// promotionScopeBO.getLastErrorCode());
	// }
	// }
	// return PROMOTION_Create;
	// default:
	// break;
	// }
	//
	// return "";// ...
	// }

	// /*
	// GET: URL:http://localhost:8080/nbr/promotion/delete.bx?ID=1
	// */
	// @RequestMapping(value = "/delete", method = RequestMethod.GET)
	// public String delete(@ModelAttribute("SpringWeb") Promotion promotiona,
	// ModelMap model) {
	// promotionBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, promotiona);
	//
	// logger.info("Delete Promotion error code = " +
	// promotionBO.getLastErrorCode());
	//
	// switch (promotionBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("删除成功！");
	// // 触发缓存更新
	// //
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Promotion).writeN(null);
	// return PROMOTION_Delete;
	// default:
	// break;
	// }
	//
	// return "";
	// }

	/*
	 * GET: URL: http://localhost:8080/nbr/promotion/retrieveNEx
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	public String retrieveNEx(@ModelAttribute("SpringWeb") Promotion promotion, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("根据iActive读取多个促销,promotion" + promotion);

		Company company = getCompanyFromSession(req.getSession());

		Pos pos = (Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName());
		promotion.setPosID(pos.getID());

		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<Promotion> pl = (List<Promotion>) promotionBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, promotion);
		switch (promotionBO.getLastErrorCode()) {
		case EC_NoError:
			PromotionScope pm = new PromotionScope();
			PromotionShopScope pShopScope = new PromotionShopScope();

			for (Promotion p : pl) {
				BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;
				pm.setPromotionID(p.getID());
				pm.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				DataSourceContextHolder.setDbName(company.getDbName());
				List<PromotionScope> listPS = (List<PromotionScope>) promotionScopeBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pm);
				BaseAction.PAGE_SIZE_MAX = 50;
				if (promotionScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询促销范围失败： " + promotionScopeBO.printErrorInfo());
					params.put(JSON_ERROR_KEY, promotionScopeBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, promotionScopeBO.getLastErrorMessage());
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString(); // ...
				}
				// 如果促销范围表不为空
				if (listPS.size() > 0) {
					ErrorInfo errorInfo = new ErrorInfo();
					// 查询该促销商品的商品名称，条形码，售价等
					for (PromotionScope ps : listPS) {
						ps.setSyncDatetime(new Date());
						// 获取商品信息
						Commodity commodity = getCommodity(ps.getCommodityID(), errorInfo, company.getDbName(), req, barcodesBO);
						if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError) {
							params.put(JSON_ERROR_KEY, errorInfo.getErrorCode().toString());
							params.put(KEY_HTMLTable_Parameter_msg, errorInfo.getErrorMessage());
							return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString(); // ...
						}
						ps.setCommodity(commodity);
					}
				}
				p.setListSlave1(listPS);
				// 促销门店范围
				BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;
				pShopScope.setPromotionID(p.getID());
				pShopScope.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				DataSourceContextHolder.setDbName(company.getDbName());
				List<PromotionShopScope> listPromotionShopScope = (List<PromotionShopScope>) promotionShopScopeBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pShopScope);
				BaseAction.PAGE_SIZE_MAX = 50;
				if (promotionScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询促销门店范围失败： " + promotionShopScopeBO.printErrorInfo());
					params.put(JSON_ERROR_KEY, promotionShopScopeBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, promotionShopScopeBO.getLastErrorMessage());
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString(); // ...
				}

				p.setListSlave2(listPromotionShopScope);
			}
			params.put(KEY_ObjectList, pl);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, promotionBO.getTotalRecord());
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_BusinessLogicNotDefined:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(KEY_HTMLTable_Parameter_msg, promotionBO.getLastErrorMessage());
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足！");
			break;
		default:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误！");
			break;
		}
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	protected Commodity getCommodity(int commodityID, ErrorInfo eInfo, String dbName, HttpServletRequest req, BarcodesBO barcodesBO) {
		StringBuffer sBuffer = new StringBuffer();
		Barcodes barcodes = new Barcodes();

		Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, getStaffFromSession(req.getSession()).getID(), eInfo, dbName);
		if (eInfo.getErrorCode() != EnumErrorCode.EC_NoError) {
			return null;
		}
		// 获取商品相对应的条形码
		barcodes.setCommodityID(commodity.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<Barcodes> listBC = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, barcodes);
		if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			eInfo.setErrorCode(barcodesBO.getLastErrorCode());
			eInfo.setErrorMessage(barcodesBO.getLastErrorMessage());
			return null;
		}
		for (Barcodes bc : listBC) {
			sBuffer.append(bc.getBarcode() + ",");
		}
		commodity.setBarcodes(sBuffer.toString());
		// 查询商品门店信息
		CommodityShopInfo commodityShopInfoRnCondition = new CommodityShopInfo();
		commodityShopInfoRnCondition.setCommodityID(commodity.getID());
//		commodityShopInfoRnCondition.setShopID(commodity.getShopID());
		DataSourceContextHolder.setDbName(dbName);
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, commodityShopInfoRnCondition);
		if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询商品门店信息对象失败，错误码=" + commodityShopInfoBO.getLastErrorCode());
		}
		else{
			commodity.setListSlave2(listCommodityShopInfo);
		}
		return commodity;
	}

	// /*
	// POST: URL:http://localhost:8080/nbr/promotion/update.bx
	// */
	// @RequestMapping(value = "/update", method = RequestMethod.POST)
	// public String update(@ModelAttribute("SpringWeb") Promotion promotiona,
	// String[] commodityIds, ModelMap model) {
	// logger.info("update model ： " + promotiona);
	// // 创建一个Set集合保存表单传过来的CommodityIds
	// Set<Integer> commodityIdSet = new HashSet<Integer>();
	// // 检查传过来的更新对象是否合理
	// String str = promotiona.checkUpdate(BaseBO.INVALID_CASE_ID);
	// if (!("".equals(str))) {
	// model.put("errorMessage", str);
	// return PROMOTION_Create;
	// }
	// if (promotiona.getScope() == 1) {
	// for (String comdityId : commodityIds) {
	// try {
	// int commodtiyId = Integer.parseInt(comdityId);
	// commodityIdSet.add(commodtiyId);
	// } catch (Exception e) {
	// model.put("errorMessage", "商品ID有误！");
	// return PROMOTION_Create;
	// }
	// }
	// }
	// Promotion bm = (Promotion)
	// promotionBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, promotiona);
	// logger.info("Create Promotion error code = " +
	// promotionBO.getLastErrorCode());
	// switch (promotionBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info(bm);
	// // 判断是否为指定满减
	// if (bm.getScope() == 1 && !commodityIdSet.isEmpty()) {
	// PromotionScope promotionScope = new PromotionScope();
	// promotionScope.setPromotionID(bm.getID());
	// Iterator<Integer> iterator = commodityIdSet.iterator();
	// while (iterator.hasNext()) {
	// Integer id = iterator.next();
	// promotionScope.setCommodityID(id);
	// promotionScopeBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, promotionScope);
	// logger.info("Update Promotion error code = " +
	// promotionScopeBO.getLastErrorCode());
	// }
	// }
	// return PROMOTION_Create;
	// default:
	// break;
	// }
	//
	// return "";// ...
	// }
}
