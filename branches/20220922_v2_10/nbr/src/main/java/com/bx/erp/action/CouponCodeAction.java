package com.bx.erp.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CouponBO;
import com.bx.erp.action.bo.CouponCodeBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/couponCode")
@Scope("prototype")
public class CouponCodeAction extends BaseAction {
	private final Log logger = LogFactory.getLog(CouponCodeAction.class);

	@Resource
	protected CouponCodeBO couponCodeBO;

	@Resource
	protected CouponBO couponBO;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByVipIDEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByVipIDEx(@ModelAttribute("SpringWeb") CouponCode couponCode, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		logger.info("根据会员查询多张优惠券" + couponCode.toString());

		Vip vipInSession = getVipFromSession(session);
		if (vipInSession.getID() != couponCode.getVipID()) {
			if (BaseAction.ENV != BaseAction.EnumEnv.DEV) {
				logger.error("会员" + vipInSession.getID() + "试图查询会员" + couponCode.getVipID() + "的优惠券！");
			}
			return null;
		}

		Company company = getCompanyFromSession(session);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<CouponCode> couponCodeList = (List<CouponCode>) couponCodeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_CouponCode_retrieveNByVipID, couponCode);
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (couponCodeBO.getLastErrorCode()) {
		case EC_NoError:
			List<Integer> couponIDList = new ArrayList<Integer>();
			for (CouponCode couponCode2 : couponCodeList) {
				if (!couponIDList.contains(couponCode2.getCouponID())) {
					couponIDList.add(couponCode2.getCouponID());
				}
			}
			//
			List<Coupon> couponList = new ArrayList<Coupon>();
			Coupon coupon = new Coupon();
			for (Integer couponID : couponIDList) {
				coupon.setID(couponID);
				DataSourceContextHolder.setDbName(company.getDbName());
				Coupon couponInDB = (Coupon) couponBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, coupon);
				if (couponBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(BaseAction.JSON_ERROR_KEY, couponBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage());
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				couponList.add(couponInDB);
			}

			params.put(BaseAction.KEY_ObjectList2, couponList);
			params.put(BaseAction.KEY_ObjectList, couponCodeList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoSuchData:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponCodeBO.getLastErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/retrieveNTotalByVipIDEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNTotalByVipIDEx(@ModelAttribute("SpringWeb") CouponCode couponCode, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询会员可用优惠券总数" + couponCode.toString());

		Vip vipInSession = getVipFromSession(session);
		if (vipInSession.getID() != couponCode.getVipID()) {
			if (BaseAction.ENV != BaseAction.EnumEnv.DEV) {
				logger.error("会员" + vipInSession.getID() + "试图查询会员" + couponCode.getVipID() + "的优惠券！");
			}
			return null;
		}

		Company company = getCompanyFromSession(session);
		DataSourceContextHolder.setDbName(company.getDbName());
		couponCodeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_CouponCode_retrieveNTotalByVipID, couponCode);
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (couponCodeBO.getLastErrorCode()) {
		case EC_NoError:
			params.put(KEY_HTMLTable_Parameter_TotalRecord, couponCodeBO.getTotalRecord());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoSuchData:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponCodeBO.getLastErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * 本方法主要用于POS端查找会员的可用优惠券。
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") CouponCode couponCode, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		// 判断是Vip还是Staff请求，如果是Vip请求，则使用System权限
		int vipOrStaffID = 0;
		if (getVipFromSession(session) != null) {
			vipOrStaffID = BaseBO.SYSTEM;
		} else if (getStaffFromSession(session) != null) {
			vipOrStaffID = getStaffFromSession(session).getID();
		}

		logger.info("查询多张优惠券" + couponCode.toString());

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<CouponCode> couponCodeList = (List<CouponCode>) couponCodeBO.retrieveNObject(vipOrStaffID, BaseBO.INVALID_CASE_ID, couponCode);
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (couponCodeBO.getLastErrorCode()) {
		case EC_NoError:
			// TODO 新增1个SP，将会员有的优惠券返回，每种类型只返回1张（如果有）
			List<CouponCode> usableCouponCodeList = new ArrayList<CouponCode>();
			// 查找此张优惠券是属于哪种的优惠券
			Date now = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
			for (CouponCode cc : couponCodeList) {
				// 每种券只返回一张可用的就好了
				boolean exists = false;
				for (CouponCode usableCouponCode : usableCouponCodeList) {
					if (usableCouponCode.getCouponID() == cc.getCouponID()) {
						exists = true;
						break;
					}
				}
				if (exists) {
					continue;
				}

				Coupon coupon = new Coupon();
				coupon.setID(cc.getCouponID());
				// ... 将来从缓存中拿
				DataSourceContextHolder.setDbName(company.getDbName());
				Coupon queryCoupon = (Coupon) couponBO.retrieve1Object(vipOrStaffID, BaseBO.INVALID_CASE_ID, coupon);
				if (couponBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(KEY_HTMLTable_Parameter_code, couponBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, couponBO.getLastErrorMessage());
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				// 此优惠券是否已到起用日期
				if (DatetimeUtil.isInDatetimeRange(queryCoupon.getBeginDateTime(), now, queryCoupon.getEndDateTime())) {
					if (queryCoupon.getWeekDayAvailable() != 0) {
						// 判断此优惠券是否在可用星期中，如可用星期一 9：00-11：00，那么星期一九点到11点是可用的
						Date beginTime = simpleDateFormat.parse(queryCoupon.getBeginTime());
						Date endTime = simpleDateFormat.parse(queryCoupon.getEndTime());
						if ((DatetimeUtil.isInWeekday(null, now, queryCoupon.getWeekDayAvailable()) && DatetimeUtil.isInTimeRange(beginTime, endTime, now))) {
							usableCouponCodeList.add(cc);
						}
					} else {
						// 该券无限制星期
						usableCouponCodeList.add(cc);
					}
				}
			}
			params.put(KEY_ObjectList, usableCouponCodeList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, couponCodeBO.getLastErrorMessage());

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/createEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") CouponCode couponCode, ModelMap model, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一张优惠券" + couponCode.toString());

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			CouponCode couponCodeCreate = (CouponCode) couponCodeBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, couponCode);
			if (couponCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建一张优惠券失败：" + couponCodeBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, couponCodeBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, couponCodeBO.getLastErrorMessage());
				break;
			}
			
			// 清除Vip相应的缓存
            Vip vip = new Vip();
            vip.setID(couponCode.getVipID());
            CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).delete1(vip);

			params.put(BaseAction.KEY_Object, couponCodeCreate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
