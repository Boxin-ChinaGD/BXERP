package com.bx.erp.action;

import java.util.ArrayList;
import java.util.Date;
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

import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CouponBO;
import com.bx.erp.action.bo.CouponScopeBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponField;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CouponScope.EnumCouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/coupon")
@Scope("prototype")
public class CouponAction extends BaseAction {
	private Log logger = LogFactory.getLog(CouponAction.class);

	@Resource
	protected CouponBO couponBO;

	@Resource
	protected CouponScopeBO couponScopeBO;

	@Resource
	protected CommodityBO commodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	/*
	 * 优惠券管理页面跳转
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);

		List<BaseModel> categoryList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> categoryParentList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).readN(true, false);

		mm.put("CouponField", new CouponField());
		mm.put("CommodityField", new CommodityField());
		mm.put("categoryList", categoryList);
		mm.put("categoryParentList", categoryParentList);
		return COUPON_Manage;
	}

	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Coupon coupon, ModelMap model, HttpServletRequest request) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查找优惠券");

		if (coupon.getPosID() != Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram) {
			Pos pos = (Pos) request.getSession().getAttribute(EnumSession.SESSION_POS.getName());
			coupon.setPosID(pos.getID());
		}
		int staffID = 0;
		Vip vip = getVipFromSession(request.getSession());
		if (vip == null) {
			staffID = getStaffFromSession(request.getSession()).getID();
		}
		Map<String, Object> params = getDefaultParamToReturn(true);

		Company company = getCompanyFromSession(request.getSession());
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> couponList = null;
		if (vip != null) {
			couponList = couponBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, coupon);
		} else {
			couponList = couponBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, coupon);
		}
		switch (couponBO.getLastErrorCode()) {
		case EC_NoError:
			CouponScope couponScope = new CouponScope();
			for (Object object : couponList) {
				Coupon c = (Coupon) object;
				couponScope.setCouponID(c.getID());
				couponScope.setPageSize(BaseAction.PAGE_SIZE_Infinite);

				DataSourceContextHolder.setDbName(company.getDbName());
				List<CouponScope> couponScopeList = null;
				if (getVipFromSession(request.getSession()) != null) {
					couponScopeList = (List<CouponScope>) couponScopeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, couponScope);
				} else {
					couponScopeList = (List<CouponScope>) couponScopeBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, couponScope);
				}
				if (couponScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || couponScopeList == null) {
					logger.info("优惠券：" + c + ";查找其从表失败！" + couponScopeBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, couponScopeBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, couponScopeBO.getLastErrorMessage().toString());
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				// 设置商品的条码和零售价
				if (!setPriceRetailAndBarcode(request, couponScopeList, company, params, staffID)) {
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				// POS端需要知道优惠券的作用范围
				for (Object object2 : couponScopeList) {
					CouponScope cs = (CouponScope) object2;
					cs.setSyncDatetime(new Date());
					cs.setCreateDatetime(new Date());
				}
				c.setSyncDatetime(new Date());
				c.setCreateDatetime(new Date());
				c.setListSlave1(couponScopeList);
			}
			params.put(KEY_ObjectList, couponList);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, couponBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode().toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode().toString());
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage().toString());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Coupon coupon, ModelMap model, HttpServletRequest request) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一种优惠券" + coupon);
		//
		Company company = getCompanyFromSession(request.getSession());
		//
		int staffID = getStaffFromSession(request.getSession()).getID();
		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		List<Integer> listObjID = null;
		// TODO 将来可以考虑重构成call一个SP，节省CPU。促销范围那边也可以做类似的重构
		// 先检查前端传递过来的商品ID是否合法的商品：1、必须是单品。2、必须未被删除。3、必须至少有一个。如果非法，直接当黑客行为处理
		if (coupon.getScope() == EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
			Integer[] iArrCommID = GeneralUtil.toIntArray(coupon.getCommodityIDs());
			if (iArrCommID == null || iArrCommID.length <= 0) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("没有指定促销商品");
				}
				return null;
			} else {
				listObjID = GeneralUtil.sortAndDeleteDuplicated(iArrCommID);// 去重
				logger.info("参与的商品列表：iArrCommID=" + iArrCommID);
			}
			Commodity comm = new Commodity();
			for (int i = 0; i < iArrCommID.length; i++) {
				comm.setID(iArrCommID[i]);
				DataSourceContextHolder.setDbName(company.getDbName());
				List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(request.getSession()).getID(), BaseBO.INVALID_CASE_ID, comm);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("在确定前端传递的商品是否合法商品时出错： " + commodityBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				} else {
					comm = Commodity.fetchCommodityFromResultSet(commR1);
					if (comm.getType() != EnumCommodityType.ECT_Normal.getIndex() || comm.getStatus() == EnumStatusCommodity.ESC_Deleted.getIndex()) {
						logger.error("指定的促销商品不是单品或状态为已删除");
						return null;
					}
				}
			}
		}
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		Coupon couponCreated = (Coupon) couponBO.createObject(staffID, BaseBO.INVALID_CASE_ID, coupon);
		switch (couponBO.getLastErrorCode()) {
		case EC_NoError:
			if (coupon.getScope() == EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
				List<CouponScope> prList = new ArrayList<CouponScope>();
				// 判断是否为全场满减
				CouponScope couponScope = new CouponScope();
				couponScope.setCouponID(couponCreated.getID());
				for (int i = 0; i < listObjID.size(); i++) {
					couponScope.setCommodityID(listObjID.get(i));
					//
					DataSourceContextHolder.setDbName(company.getDbName());
					CouponScope ps = (CouponScope) couponScopeBO.createObject(getStaffFromSession(request.getSession()).getID(), BaseBO.INVALID_CASE_ID, couponScope);
					if (couponScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("Create Promotion error code = " + couponScopeBO.getLastErrorCode());
						params.put(BaseAction.JSON_ERROR_KEY, couponScopeBO.getLastErrorCode());
						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					} else {
						logger.info("创建促销商品成功，ps=" + ps);
						prList.add(ps);
					}
				}
				// 设置商品的条码和零售价
				if (!setPriceRetailAndBarcode(request, prList, company, params, getStaffFromSession(request.getSession()).getID())) {
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				couponCreated.setListSlave1(prList);
			}
			params.put(KEY_Object, couponCreated);
			params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode().toString());
			break;
		case EC_NoPermission:
			logger.error("无权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.error("其它错误");
			params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode());
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private boolean setPriceRetailAndBarcode(HttpServletRequest request, List<CouponScope> couponScopeList, Company company, Map<String, Object> params, int staffID) {
		for (int i = 0; i < couponScopeList.size(); i++) {
			CouponScope couponScope = couponScopeList.get(i);
			ErrorInfo ecOut = new ErrorInfo();
			Commodity commodity = null;
			if(getVipFromSession(request.getSession()) != null) {
				commodity = (Commodity) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Commodity).read1(couponScope.getCommodityID(), BaseBO.SYSTEM, ecOut, company.getDbName());
			}else {
				commodity = (Commodity) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Commodity).read1(couponScope.getCommodityID(), staffID, ecOut, company.getDbName());
			}
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				return false;
			}
			if (commodity == null) {
				return false;
			}
			//
			Barcodes b = new Barcodes();
			b.setCommodityID(couponScope.getCommodityID());
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Barcodes> barcodeList = null;
			if(getVipFromSession(request.getSession()) != null) {
				barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, b);
			} else {
				barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, b);
			}
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError || barcodeList == null || barcodeList.size() == 0) {
				logger.info("Retrieve N Barcodes error code=" + barcodesBO.getLastErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
				return false;
			}
			couponScope.setPriceRetail(commodity.getPriceRetail());
			couponScope.setBarcodes(barcodeList.get(0).getBarcode());
		}
		return true;
	}

	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Coupon coupon, ModelMap model, HttpServletRequest request) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("删除一种优惠券" + coupon);
		//
		Company company = getCompanyFromSession(request.getSession());
		Staff staff = getStaffFromSession(request.getSession());
		DataSourceContextHolder.setDbName(company.getDbName());
		//
		couponBO.deleteObject(staff.getID(), BaseBO.INVALID_CASE_ID, coupon);
		Map<String, Object> params = new HashMap<>();
		switch (couponBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("删除成功！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.error("字段验证不通过！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		case EC_NoPermission:
			logger.error("无权限");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.error("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Coupon coupon, ModelMap model, HttpServletRequest request) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询一种优惠券");
		//
		Map<String, Object> params = new HashMap<>();
		Company company = getCompanyFromSession(request.getSession());
		int staffID = getStaffFromSession(request.getSession()).getID();
		DataSourceContextHolder.setDbName(company.getDbName());
		Coupon couponRetrieve1 = (Coupon) couponBO.retrieve1Object(staffID, BaseBO.INVALID_CASE_ID, coupon);
		switch (couponBO.getLastErrorCode()) {
		case EC_NoError:
			if (couponRetrieve1 == null) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "不存在该优惠券");
				logger.error("返回的数据=" + params);
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			logger.info("查询一种优惠券成功 ");
			CouponScope couponScope = new CouponScope();
			couponScope.setCouponID(couponRetrieve1.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			List<CouponScope> couponScopeList = (List<CouponScope>) couponScopeBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, couponScope);
			if (couponScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("优惠券：" + couponRetrieve1 + ";查找其从表失败！" + couponScopeBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, couponScopeBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, couponScopeBO.getLastErrorMessage().toString());
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			// 设置商品的条码和零售价
			if (!setPriceRetailAndBarcode(request, couponScopeList, company, params, staffID)) {
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			//
			couponRetrieve1.setListSlave1(couponScopeList);
			params.put(KEY_Object, couponRetrieve1);
			params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode().toString());
			break;
		case EC_WrongFormatForInputField:
			logger.error("输入的格式不正确！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.error("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
