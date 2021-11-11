package com.bx.erp.action;

import java.text.SimpleDateFormat;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CouponBO;
import com.bx.erp.action.bo.CouponCodeBO;
import com.bx.erp.action.bo.CouponScopeBO;
import com.bx.erp.action.bo.RetailTradeBO;
import com.bx.erp.action.bo.RetailTradeCommodityBO;
import com.bx.erp.action.bo.RetailTradeCouponBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.warehousing.WarehousingCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Controller
@RequestMapping("/retailTrade")
@Scope("prototype")
public class RetailTradeAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradeAction.class);

	@Resource
	private RetailTradeBO retailTradeBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private RetailTradeCommodityBO retailTradeCommodityBO;

	@Resource
	private SubCommodityBO subCommodityBO;

	@Resource
	private RetailTradePromotingBO retailTradePromotingBO;

	@Resource
	private RetailTradePromotingFlowBO retailTradePromotingFlowBO;

	@Resource
	private WarehousingCache warehousingCache;

	@Resource
	private RetailTradeCouponBO retailTradeCouponBO;

	@Resource
	private CouponCodeBO couponCodeBO;

	@Resource
	private CouponBO couponBO;

	@Resource
	private CouponScopeBO couponScopeBO;

	@Resource
	private VipBO vipBO;

	public static final Map<String, RealTimeSalesStatisticsToday> todaysSaleCache = new HashMap<>();

	// Fiddler URL: http://localhost:8080/nbr/retailTrade.bx
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") RetailTrade retailTrade, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// ...将来重构为使用缓存。次次都从DB中读取，代价太大
		Company company = getCompanyFromSession(session);

		logger.info("进入销售记录页面");
		List<BaseModel> listBM = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).readN(false, false); // 从缓存中拿到所有员工

		if (listBM != null) {
			for (int i = 0; i < listBM.size(); i++) {
				Staff staff = (Staff) listBM.get(i);
				staff.clearSensitiveInfo();
			}
		}
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店

		mm.put("shopList", shopList);
		mm.put(KEY_ObjectList, listBM);
		return new ModelAndView(RETAILTRADE_Index, "", null);
	}

	@SuppressWarnings("unchecked")
	private String checkCreate(RetailTrade rt, HttpServletRequest req, String dbName) throws Exception {
		if (rt == null) {
			return "主表为空！";
		}

		String err = checkListSlave(rt);
		if (!"".equals(err)) {
			return err;
		}

		err = checkCommodityAndBarcodesExistsInDB((List<RetailTradeCommodity>) rt.getListSlave1(), req, dbName);
		if (!"".equals(err)) {
			return err;
		}

		if (rt.getListSlave3() != null && rt.getListSlave3().size() > 0) {
			err = checkCoupon(rt, dbName);
			if (!"".equals(err)) {
				return err;
			}
		}

		return "";
	}

	private String checkCoupon(RetailTrade rt, String dbName) throws Exception {
		logger.debug("检查优惠券使用的合法性前：" + rt);
		List<?> listSlave3 = rt.getListSlave3();

		// 找到相关的优惠券
		RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) listSlave3.get(0);
		CouponCode couponCode = new CouponCode();
		couponCode.setID(retailTradeCoupon.getCouponCodeID());
		//
		DataSourceContextHolder.setDbName(dbName);
		CouponCode couponCodeInDB = (CouponCode) couponCodeBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, couponCode);
		if (couponCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || couponCodeInDB == null) {
			return "查询零售单使用的这张优惠券失败或不存在,错误信息：" + couponCodeBO.getLastErrorMessage();
		}

		Coupon coupon = new Coupon();
		coupon.setID(couponCodeInDB.getCouponID());
		//
		DataSourceContextHolder.setDbName(dbName);
		Coupon couponInDB = (Coupon) couponBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, coupon);
		if (couponBO.getLastErrorCode() != EnumErrorCode.EC_NoError || couponInDB == null) {
			return "查询优惠券种类失败或者不存在,错误信息：" + couponBO.getLastErrorMessage();
		}

		// 检查金额是否满足优惠券阀值
		RetailTrade cloneRetailTrade = (RetailTrade) rt.clone();
		double amount = 0.000000d;
		if (couponInDB.getScope() == CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
			cloneRetailTrade.setAmount(GeneralUtil.sum(cloneRetailTrade.getAmount(), cloneRetailTrade.getCouponAmount())); // 获取到使用优惠券前的金额
			cloneRetailTrade.calculateReturnPrice(cloneRetailTrade); // 使用优惠券前有参与促销，那么退货价为参与促销后的价格，没参与促销则退货价为原价

			// 获取到优惠券范围的指定商品
			CouponScope couponScope = new CouponScope();
			couponScope.setCouponID(couponInDB.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<?> couponScopeList = couponScopeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, couponScope);
			if (couponScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || couponScopeList.size() == 0) {
				return "查找优惠券的范围失败或为空，错误信息：" + couponScopeBO.getLastErrorMessage();
			}

			// 遍历,计算出指定的商品一共花费了多少钱
			for (Object object : cloneRetailTrade.getListSlave1()) {
				RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) object;
				for (Object o : couponScopeList) {
					CouponScope cs = (CouponScope) o;
					if (cs.getCommodityID() == retailTradeCommodity.getCommodityID()) {
						amount = GeneralUtil.sum(amount, GeneralUtil.mul(retailTradeCommodity.getPriceReturn(), retailTradeCommodity.getNO()));
						break;
					}
				}
			}
		} else {
			amount = GeneralUtil.sum(cloneRetailTrade.getAmount(), cloneRetailTrade.getCouponAmount()); // 用使用优惠券前的金额进行判断
		}
		logger.debug("使用优惠券前的总金额：" + amount + "\t使用的优惠券的起用金额：" + couponInDB.getLeastAmount());
		//
		if (amount < couponInDB.getLeastAmount()) {
			return "计算出零售单的金额不满足优惠券的阀值";
		}

		// 检查零售单使用优惠券的时候是否在此优惠券的可用范围中
		if (!DatetimeUtil.isInDatetimeRange(couponInDB.getBeginDateTime(), cloneRetailTrade.getSaleDatetime(), couponInDB.getEndDateTime())) {
			return "使用此优惠券时。该优惠券还没到起用时间";
		}
		//
		if (couponInDB.getWeekDayAvailable() != 0) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
			Date beginTime = simpleDateFormat.parse(couponInDB.getBeginTime());
			Date endTime = simpleDateFormat.parse(couponInDB.getEndTime());
			if (!((DatetimeUtil.isInWeekday(null, cloneRetailTrade.getSaleDatetime(), couponInDB.getWeekDayAvailable()) && DatetimeUtil.isInTimeRange(beginTime, endTime, cloneRetailTrade.getSaleDatetime())))) {
				return "使用此优惠券时，该优惠券还没在使用时间内";
			}
		}

		return "";
	}

	private String checkListSlave(RetailTrade rt) {
		if (rt.getListSlave1() == null) {
			return "从表为空！";
		}

		List<?> listSlave1 = rt.getListSlave1();
		Integer[] iCommodityID = new Integer[rt.getListSlave1().size()];
		for (int i = 0; i < listSlave1.size(); i++) {
			RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) listSlave1.get(i);
			iCommodityID[i] = retailTradeCommodity.getCommodityID();
		}
		// 一旦发现iaCommID内部有重复元素，当黑客行为处理
		if (GeneralUtil.hasDuplicatedElement(iCommodityID)) {
			return "黑客传递的参数有重复！";
		}

		if (rt.getSourceID() > 0) {
			if (rt.getListSlave2() != null || rt.getListSlave3() != null) {
				return "退货型零售单不应该有促销计算过程和优惠券";
			}
		}

		if (rt.getListSlave2() != null && rt.getListSlave2().size() > 0) {
			if (rt.getListSlave2().size() != 1) {
				return "->每张单只会有一个零售单促销表!";
			}
			if (((RetailTradePromoting) rt.getListSlave2().get(0)).getListSlave1().size() == 0) {
				return "计算过程中的描述并没有记录";
			}
		}

		if (rt.getListSlave3() != null && rt.getListSlave3().size() > 0) {
			if (rt.getListSlave3().size() == 1) {
				if (rt.getListSlave2() == null || rt.getListSlave2().get(0) == null) {
					return "此零售单使用了优惠券，但是并没有生成相关的零售单促销";
				} else {

				}
			} else {
				return "-> 每张单只能使用一张优惠券";
			}
		}

		return "";
	}

	private String checkCommodityAndBarcodesExistsInDB(List<RetailTradeCommodity> retailTradeCommodityList, HttpServletRequest req, String dbName) {
		Commodity commRetrieved = null;
		Barcodes barcodesRetrieve = null;
		int staffID = getStaffFromSession(req.getSession()).getID();
		for (RetailTradeCommodity rtc : retailTradeCommodityList) {
			ErrorInfo eiOut = new ErrorInfo();
			// 检查商品是否存在
			DataSourceContextHolder.setDbName(dbName);
			commRetrieved = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(), staffID, eiOut, dbName);
			if (eiOut.getErrorCode() == EnumErrorCode.EC_NoSuchData) {
				logger.info("此商品是删除商品，但POS机还未同步，允许售卖");
				continue;
			}
			if (eiOut.getErrorCode() != EnumErrorCode.EC_NoError || commRetrieved == null) {// 有可能该商品是存在的，只是读取时发生了DB异常，但这种情况几乎不会出现，所以认为是商品不存在
				return "不能零售一个不存在的商品";
			}
			// 检查条形码是否存在
			DataSourceContextHolder.setDbName(dbName);
			barcodesRetrieve = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(rtc.getBarcodeID(), staffID, eiOut, dbName);
			if (eiOut.getErrorCode() != EnumErrorCode.EC_NoError || barcodesRetrieve == null) {
				return "零售单商品的商品条码不存在";
			}
		}

		return "";
	}

	/*
	 * pos_SN=1244465224&pos_ID=5&logo=asha42soadigmnalskd&staffID=2&paymentType=1&
	 * paymentAccount=Q123456&remark=...............&sourceID=-1&amount=285.1 POST
	 * URL:http://localhost:8080/nbr/retailTrade/createEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") RetailTrade retailTrade, ModelMap model, HttpServletRequest req) throws Exception {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		String str = GetStringFromRequest(req, "retailTrade", String.valueOf(BaseAction.INVALID_ID));
		if (str == null || str.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.error("->零售单Json格式有误!");
			return null;
		}

		RetailTrade rt = (RetailTrade) retailTrade.parse1(str);
		if (rt == null || rt.getListSlave1() == null) {
			logger.error("->零售单或零售单商品解析失败!");
			return null;
		}
		logger.info("零售单的创建，rt=" + rt);

		// 检查输入合法性
		Company company = getCompanyFromSession(req.getSession());
		String err = checkCreate(rt, req, company.getDbName());
		if (!"".equals(err)) {
			logger.error(err + "\tretailTrade=" + rt);
			return null;
		}

		Boolean hasDBError = false;
		RetailTradePromoting rtp = null;
		if (rt.getListSlave2() != null && rt.getListSlave2().size() > 0) {
			rtp = (RetailTradePromoting) rt.getListSlave2().get(0);
		}
		RetailTradeCoupon rtc = null;
		if (rt.getListSlave3() != null && rt.getListSlave3().size() > 0) {
			rtc = (RetailTradeCoupon) rt.getListSlave3().get(0);
		}
		//
		Map<String, Object> params = getDefaultParamToReturn(true);
		params = handleCreateEx(rt, (List<RetailTradeCommodity>) rt.getListSlave1(), rtp, rtc, company.getDbName(), req);

		RetailTrade rt2 = (RetailTrade) params.get(KEY_Object);
		if (rt2 != null) {
			if (rt2.getErrorInfo() == null) {
				ErrorInfo ei = new ErrorInfo();
				ei.setErrorCode(EnumErrorCode.EC_NoError);
				ei.setErrorMessage("");
				rt2.setErrorInfo(ei);
			}
			if (rt2.getErrorInfo().getErrorCode() != EnumErrorCode.EC_NoError) {
				hasDBError = true;
			}
		}
		if (hasDBError) {
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString()); // 有2种含义，一种是创建失败有返回对象
		}

		logger.info("返回的数据=" + JSON.toJSONStringWithDateFormat(params, DATETIME_FORMAT_Default1, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue));
		return JSON.toJSONStringWithDateFormat(params, DATETIME_FORMAT_Default1, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
	}

	private void onCreateSuccess(double totalAmount, int totalNO, String dbName) {
		RealTimeSalesStatisticsToday realTimeSST = todaysSaleCache.get(dbName);
		if (realTimeSST == null) {
			logger.error("获取公司当天销售数据失败，dbName:" + dbName);
			return;
		}
		//
		realTimeSST.setTotalAmountToday(GeneralUtil.sum(realTimeSST.getTotalAmountToday(), totalAmount));
		realTimeSST.setTotalNOToday(realTimeSST.getTotalNOToday() + totalNO);

		// bInTestNGMode = false; //防止测试代码没有恢复这个值，影响其它测试的结果
	}

	// 核销优惠券
	private boolean consume1CouponCode(int couponCodeID, int staffID, String dbName, Map<String, Object> params) {
		CouponCode couponCode = new CouponCode();
		couponCode.setID(couponCodeID);

		DataSourceContextHolder.setDbName(dbName);
		couponCodeBO.updateObject(staffID, BaseBO.CASE_CouponCode_Consume, couponCode);
		if (couponCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			params.put(JSON_ERROR_KEY, couponCodeBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, couponCodeBO.getLastErrorMessage());
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createNEx(@ModelAttribute("SpringWeb") RetailTrade retailTrade, ModelMap model, HttpServletRequest req) throws Exception {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建多个零售单");

		Company company = getCompanyFromSession(req.getSession());

		String str = GetStringFromRequest(req, KEY_ObjectList, String.valueOf(BaseAction.INVALID_ID));

		if (str == null || str.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.error("->数据格式不正确!");
			return null;
		}

		Map<String, Object> params = getDefaultParamToReturn(true);
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		Boolean hasDBError = false;

		TypeUtils.compatibleWithJavaBean = true; // 解析时将key值的解析的开头首字母的解析保留大写
		JSONArray aliJsonArray = JSON.parseArray(str);
		for (int i = 0; i < aliJsonArray.size(); i++) {
			retailTrade = (RetailTrade) new RetailTrade().parse1(aliJsonArray.getJSONObject(i).toString());
			if (retailTrade == null || retailTrade.getListSlave1() == null) {
				logger.error("->零售单或零售单商品为空!");
				return null;
			}

			String err = checkCreate(retailTrade, req, company.getDbName());
			if (!"".equals(err)) {
				logger.error(err + "\tretailTrade=" + retailTrade);
				return null;
			}
			//
			try {
				params = handleCreateEx(retailTrade, (List<RetailTradeCommodity>) retailTrade.getListSlave1(),
						(retailTrade.getListSlave2() == null || retailTrade.getListSlave2().size() == 0 ? null : (RetailTradePromoting) retailTrade.getListSlave2().get(0)),
						(retailTrade.getListSlave3() == null || retailTrade.getListSlave3().size() == 0 ? null : (RetailTradeCoupon) retailTrade.getListSlave3().get(0)), company.getDbName(), req);

				RetailTrade rt = (RetailTrade) params.get(KEY_Object);
				if (rt != null) {
					if (rt.getErrorInfo() == null) {
						ErrorInfo ei = new ErrorInfo();
						ei.setErrorCode(EnumErrorCode.EC_NoError);
						ei.setErrorMessage("");
						rt.setErrorInfo(ei);
					}
					if (rt.getErrorInfo().getErrorCode() != EnumErrorCode.EC_NoError) {
						hasDBError = true;
					}
					retailTradeList.add(rt);
				} else {
					if (!params.get(BaseAction.JSON_ERROR_KEY).equals(EnumErrorCode.EC_NoError.toString())) {
						hasDBError = true;
					}
				}
			} catch (Exception e) {
				params.put(KEY_HTMLTable_Parameter_msg, "仅上传了一部分零售单!"); // ...
			}
		}

		if (hasDBError) { // 如果查单后发现有问题，设为部分成功，让成功的单可以在pos插入替换
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString()); // 有2种 含义，一种 是创建失败有返回用来创建的对象，一种是创建失败但是没有用来创建的对象
		}

		params.put(KEY_ObjectList, retailTradeList);
		logger.info("返回的数据=" + JSON.toJSONStringWithDateFormat(params, DATETIME_FORMAT_Default1, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue));
		return JSON.toJSONStringWithDateFormat(params, DATETIME_FORMAT_Default1, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> handleCreateEx(BaseModel retailTrade, List<RetailTradeCommodity> retailTradeCommodityList, RetailTradePromoting retailTradePromoting, RetailTradeCoupon retaiTradeCoupon, String dbName,
			HttpServletRequest req) {
		boolean bReturnObject = (retailTrade.getReturnObject() == 1);
		Map<String, Object> params = new HashMap<String, Object>();
		Staff staff = getStaffFromSession(req.getSession());
		RetailTrade rtBeforeCreate = (RetailTrade) retailTrade;
		rtBeforeCreate.setStaffID(staff.getID());
		logger.info("retailTrade=" + rtBeforeCreate);
		// 检查1：如果零售单存在微信支付，那么要查询公司是否存在子商户号
		Company company = (Company) req.getSession().getAttribute(EnumSession.SESSION_Company.getName());
		if ((rtBeforeCreate.getPaymentType() & 4) == 4 && company.getSubmchid() == null) {
			logger.info("支付失败，公司的子商户号未设置！不能进行微信支付");
			params.put(KEY_HTMLTable_Parameter_msg, "支付失败，公司的子商户号未设置！不能进行微信支付");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
			return params;
		}
		// 检查2：如果零售单商品包含已删除商品，则需要设置零售单的状态为包含已删除商品的零售单
		Commodity comm = new Commodity();
		for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
			comm.setID(retailTradeCommodity.getCommodityID());
			comm.setIncludeDeleted(Commodity.IsIncludeDeleted);
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = commodityBO.retrieve1ObjectEx(staff.getID(), BaseBO.INVALID_CASE_ID, comm);
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询商品失败：" + commodityBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
				return params;
			}
			// 若商品是组合商品，commodityBO.retrieve1ObjectEx返回的结果集就有组合商品和其子商品，即返回多个结果集。
			Commodity commodity = new Commodity();
			if (bmList.get(0) instanceof BaseModel) { // 普通商品只有一个结果集
				commodity = (Commodity) bmList.get(0);
			} else { // 组合商品有两个结果集
				commodity = (Commodity) bmList.get(0).get(0);
			}
			// 若商品是已删除商品，则把零售单的状态设置为2(包含已删除商品)
			if (commodity.getStatus() == EnumStatusCommodity.ESC_Deleted.getIndex()) {
				rtBeforeCreate.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
				break;
			}
		}

		RetailTrade createRetailTrade = null;
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> createObjectEx = retailTradeBO.createObjectEx(staff.getID(), BaseBO.INVALID_CASE_ID, rtBeforeCreate);
		switch (retailTradeBO.getLastErrorCode()) {
		case EC_Duplicated: // 该零售单已存在。查询出相应的从表.并返回
			createRetailTrade = (RetailTrade) createObjectEx.get(0);

			RetailTradeCommodity retailTradeComm = new RetailTradeCommodity();
			retailTradeComm.setTradeID(createRetailTrade.getID());
			Staff staff1 = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
			retailTradeComm.setOperatorStaffID(staff1.getID()); // 需要指定修改商品的staff的ID
			DataSourceContextHolder.setDbName(dbName);
			List<RetailTradeCommodity> listRTC = (List<RetailTradeCommodity>) retailTradeCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradeComm);
			if (retailTradeCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取零售单商品表失败，错误码=" + retailTradeCommodityBO.getLastErrorCode() + ",错误信息=" + retailTradeCommodityBO.getLastErrorMessage());
				params.put(KEY_HTMLTable_Parameter_msg, "零售单商品读取，失败原因：" + retailTradeCommodityBO.getLastErrorMessage());
				params.put(JSON_ERROR_KEY, retailTradeCommodityBO.getLastErrorCode());
				return params;
			}
			// 设置从表的同步时间。用于pos同步数据
			for (RetailTradeCommodity retailTradeCommodity : listRTC) {
				retailTradeCommodity.setSyncDatetime(new Date());
			}
			logger.info("读取零售单商品表成功，listRTC=" + listRTC);
			createRetailTrade.setListSlave1(listRTC);
			// 查询零售单促销表
			List<RetailTradePromoting> retailTradePromotingList = queryRetailTradePromoting(createRetailTrade, dbName, req);
			createRetailTrade.setListSlave2(retailTradePromotingList);
			// 查询零售单优惠券使用表
			appendRetailTradeCoupon(createRetailTrade, req, dbName);
			break;
		case EC_NoError:
			if (rtBeforeCreate.getVipID() > 0) {
				Vip vip = (Vip) createObjectEx.get(1).get(0);
				CacheManager.getCache(dbName, EnumCacheType.ECT_Vip).write1(vip, dbName, staff.getID());

				createRetailTrade = (RetailTrade) createObjectEx.get(0).get(0);
			} else {
				createRetailTrade = (RetailTrade) createObjectEx.get(0);
			}

			// 存放创建后的零售单商品
			appendRetailTradeCommodity(createRetailTrade, retailTradeCommodityList, dbName, req);

			// 创建零售单促销，如果有
			if (retailTradePromoting != null) {
				appendRetailTradePromoting(createRetailTrade, retailTradePromoting, dbName, req);
			}

			// 创建零售单优惠券使用表，如果有
			if (retailTrade.getListSlave3() != null && retailTrade.getListSlave3().size() > 0) {
				appendRetailTradeCoupon(createRetailTrade, retaiTradeCoupon, req, dbName);
				// 核销优惠券
				if (createRetailTrade.getListSlave3() != null && !consume1CouponCode(retaiTradeCoupon.getCouponCodeID(), staff.getID(), dbName, params)) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
					if (bReturnObject) {
						params.put(KEY_Object, createRetailTrade);
					}
					return params;
				}
			}

			// 成功创建零售单后,更新今日的销售金额，销售单数
			if (createRetailTrade.getSourceID() == -1) {
				onCreateSuccess(createRetailTrade.getAmount(), 1, dbName);
			}

			warehousingCache.load(dbName); // 重置缓存，因为不能确定具体修改的入库单是哪个
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, retailTradeBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, retailTradeBO.getLastErrorMessage().toString());

			return params;
		}

		params.put(BaseAction.JSON_ERROR_KEY, retailTradeBO.getLastErrorCode().toString());
		params.put(KEY_HTMLTable_Parameter_msg, retailTradeBO.getLastErrorMessage().toString());
		if (bReturnObject) {
			params.put(KEY_Object, createRetailTrade);
		}

		return params;
	}

	@SuppressWarnings("unchecked")
	protected List<RetailTradePromoting> queryRetailTradePromoting(RetailTrade createRetailTrade, String dbName, HttpServletRequest req) {
		logger.info("准备进行查找与零售单ID=" + createRetailTrade.getID() + "有关联的零售单促销");
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		retailTradePromoting.setTradeID(createRetailTrade.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) retailTradePromotingBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradePromoting);
		if (retailTradePromotingBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			for (RetailTradePromoting rtp : retailTradePromotingList) {
				RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
				rtpf.setRetailTradePromotingID(rtp.getID());
				//
				DataSourceContextHolder.setDbName(dbName);
				List<RetailTradePromotingFlow> retailTradePromotingFlowList = (List<RetailTradePromotingFlow>) retailTradePromotingFlowBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, rtpf);
				if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("零售单促销过程表查询失败，" + retailTradePromotingFlowBO.printErrorInfo());
					String errorMessage = "/t 零售单促销过程表查询失败，失败原因：" + retailTradePromotingFlowBO.getLastErrorMessage();
					setErrorInfo(createRetailTrade, errorMessage);
					continue;
				}
				for (RetailTradePromotingFlow retailTradePromotingFlow : retailTradePromotingFlowList) {
					retailTradePromotingFlow.setSyncDatetime(new Date());
				}
				rtp.setListSlave1(retailTradePromotingFlowList);
				rtp.setSyncDatetime(new Date());
			}
		} else {
			logger.error("零售单促销表查询失败，" + retailTradePromotingBO.printErrorInfo());
			String errorMessage = "/t 零售单促销表查询失败，失败原因：" + retailTradePromotingBO.getLastErrorMessage();
			setErrorInfo(createRetailTrade, errorMessage);
		}

		return retailTradePromotingList;
	}

	protected void appendRetailTradePromoting(RetailTrade createRetailTrade, RetailTradePromoting retailTradePromoting, String dbName, HttpServletRequest req) {
		logger.info("准备进行创建零售单促销");
		retailTradePromoting.setTradeID(createRetailTrade.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		RetailTradePromoting createRetailTradePromoting = (RetailTradePromoting) retailTradePromotingBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradePromoting);
		if (retailTradePromotingBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			if (retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
				List<RetailTradePromotingFlow> list = new ArrayList<RetailTradePromotingFlow>();
				for (Object o : retailTradePromoting.getListSlave1()) {
					RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) o;
					// 给从表引用ID赋值
					rtpf.setRetailTradePromotingID(createRetailTradePromoting.getID());
					DataSourceContextHolder.setDbName(dbName);
					RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) retailTradePromotingFlowBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, rtpf);
					logger.info("Create RetailTradePromotingFlow error code = " + retailTradePromotingFlowBO.getLastErrorCode());
					if (retailTradePromotingFlowBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
						retailTradePromotingFlow.setSyncDatetime(new Date());
						list.add(retailTradePromotingFlow);
					} else {
						logger.error("创建零售单促销过程表失败" + retailTradePromotingFlowBO.printErrorInfo());
						String errorMessage = "/t 零售单促销从表创建失败，失败原因：" + retailTradePromotingFlowBO.getLastErrorMessage();
						setErrorInfo(createRetailTrade, errorMessage);
					}
				}
				createRetailTradePromoting.setSyncDatetime(new Date());
				createRetailTradePromoting.setListSlave1(list);
				// 将创建成功的促销从表放入零售单中返回POS
				List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
				retailTradePromotingList.add(createRetailTradePromoting);
				createRetailTrade.setListSlave2(retailTradePromotingList);
			}
		} else {
			logger.error("创建零售单促销表失败" + retailTradePromotingBO.printErrorInfo());
			String errorMessage = "/t 零售单促销表创建失败，失败原因：" + retailTradePromotingBO.getLastErrorMessage();
			setErrorInfo(createRetailTrade, errorMessage);
		}
	}

	private void setErrorInfo(RetailTrade retailTrade, String errorMessage) {
		ErrorInfo errorInfo = retailTrade.getErrorInfo();
		if (errorInfo == null) {
			errorInfo = new ErrorInfo();
			errorInfo.setErrorCode(EnumErrorCode.EC_PartSuccess);
		}
		errorInfo.setErrorMessage((errorInfo.getErrorMessage() == null ? "" : errorInfo.getErrorMessage()) + errorMessage);
		retailTrade.setErrorInfo(errorInfo);
	}

	protected void appendRetailTradeCommodity(RetailTrade createRetailTrade, List<RetailTradeCommodity> retailTradeCommodityList, String dbName, HttpServletRequest req) {
		List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
		for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
			retailTradeCommodity.setTradeID(createRetailTrade.getID());
			Staff staff2 = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
			retailTradeCommodity.setOperatorStaffID(staff2.getID()); // 需要指定修改商品的staff的ID
			DataSourceContextHolder.setDbName(dbName);
			RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradeCommodity);
			if (retailTradeCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("->创建从表失败。" + retailTradeCommodityBO.printErrorInfo());
				String errorMessage = "/t 零售单商品创建失败，创建对象：" + retailTradeCommodity + ",失败原因：" + retailTradeCommodityBO.getLastErrorMessage();
				setErrorInfo(createRetailTrade, errorMessage);
				continue;// 继续进入下个循环，继续创建后面的零售单商品
			}
			rtc.setSyncDatetime(new Date());
			logger.info("->从表信息：零售单第" + retailTradeCommodity.getCommodityID() + "个商品创建成功：" + rtc);
			rtcList.add(rtc);

			logger.info("当零售单商品创建成功后，商品的库存被改变，这时应创建相应的商品同存，并清除相应的商品缓存");
			// 清除相应的商品缓存
			updateCommodityCache(rtc.getCommodityID(), dbName, req);
		}
		createRetailTrade.setListSlave1(rtcList);
	}

	protected boolean updateCommodityCache(int commodityID, String dbName, HttpServletRequest req) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() == EnumErrorCode.EC_NoError && comm != null) {
			CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(comm);
		}
		return true;
	}

	private void appendRetailTradeCoupon(RetailTrade retailTrade, RetailTradeCoupon retailTradecoupon, HttpServletRequest req, String dbName) {
		logger.info("准备进行创建与零售单有关联的零售单优惠券");

		retailTradecoupon.setRetailTradeID(retailTrade.getID());
		DataSourceContextHolder.setDbName(dbName);
		RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradecoupon);
		if (retailTradeCouponBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			list.add(retailTradeCouponCreate);
			retailTrade.setListSlave3(list);
		} else {
			logger.error("创建零售单优惠券使用表失败" + retailTradeCouponBO.printErrorInfo());
			String errorMessage = "/t 零售单优惠券使用表创建失败，失败原因：" + retailTradeCouponBO.getLastErrorMessage();
			setErrorInfo(retailTrade, errorMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private void appendRetailTradeCoupon(RetailTrade retailTrade, HttpServletRequest req, String dbName) {
		logger.info("准备进行查找与零售单ID=" + retailTrade.getID() + "有关联的零售单优惠券");
		RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
		retailTradeCoupon.setRetailTradeID(retailTrade.getID());

		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = (List<BaseModel>) retailTradeCouponBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradeCoupon);
		if (retailTradeCouponBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			retailTrade.setListSlave3(list);
		} else {
			logger.error("查询零售单优惠券使用表失败" + retailTradeCouponBO.printErrorInfo());
			String errorMessage = "/t 零售单优惠券使用表查询失败，失败原因：" + retailTradeCouponBO.getLastErrorMessage();
			setErrorInfo(retailTrade, errorMessage);
		}
	}

	/*
	 * 
	 * GET URL:http://localhost:8080/nbr/retailTrade/retrieveNEx.bx?ID=1
	 * http://localhost:8080/nbr/retailTrade/retrieveNEx.bx?pos_ID=1
	 * http://localhost:8080/nbr/retailTrade/retrieveNEx.bx?pos_SN=PS123465464
	 * http://localhost:8080/nbr/retailTrade/retrieveNEx.bx
	 * http://localhost:8080/nbr/retailTrade/retrieveNEx.bx?pos_SN=PS123465464&
	 * pos_ID=1
	 * http://localhost:8080/nbr/retailTrade/retrieveNEx.bx?startDate=2017/8/6&
	 * endDate=2018/8/31
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") RetailTrade retailTrade, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		// 判断是Vip还是Staff请求，如果是Vip请求，则使用System权限
		int vipOrStaffID = 0;
		if (getVipFromSession(session) != null) {
			vipOrStaffID = BaseBO.SYSTEM;
		} else {// if (getStaffFromSession(session) != null) {
			vipOrStaffID = getStaffFromSession(session).getID();
		}

		logger.info("查询多个零售单，retailTrade=" + retailTrade);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			int iUseCaseID = BaseBO.CASE_RetailTrade_RetrieveNFromApp;
			Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
			if (pos.getID() == BaseAction.INVALID_POS_ID) { // 此操作是web前端发起的
				iUseCaseID = BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb;
				// 是否是按商品关键字查询，商品的名称有可能大于10位,所以需要判断是否按SN查询
				if (!FieldFormat.checkRetailTradeRetrieveNBySN(retailTrade.getQueryKeyword())) {
					iUseCaseID = BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb;
				}
			}
			//
			DataSourceContextHolder.setDbName(dbName);
			List<RetailTrade> list = (List<RetailTrade>) retailTradeBO.retrieveNObject(vipOrStaffID, iUseCaseID, retailTrade);
			logger.info("retrieveN RetailTrade error code=" + retailTradeBO.getLastErrorCode());
			if (retailTradeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询失败：" + retailTradeBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, retailTradeBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, retailTradeBO.getLastErrorMessage());
				break;
			} else {
				logger.info("查询成功");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
				params.put(KEY_HTMLTable_Parameter_msg, "");
			}

			// 遍历所有的零售单，设置每个零售单的同步时间。并查找相对应的零售单商品表(设置同步时间).
			RetailTradeCommodity rtc = new RetailTradeCommodity();
			for (RetailTrade retailTrade2 : list) {
				retailTrade2.setSyncDatetime(new Date());

				rtc.setTradeID(retailTrade2.getID());
				rtc.setPageIndex(BaseAction.PAGE_StartIndex);
				rtc.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				DataSourceContextHolder.setDbName(dbName);
				List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityBO.retrieveNObject(vipOrStaffID, BaseBO.INVALID_CASE_ID, rtc);
				logger.info("retrieveN RetailTradeCommodity error code=" + retailTradeCommodityBO.getLastErrorCode());
				if (retailTradeCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(BaseAction.JSON_ERROR_KEY, retailTradeCommodityBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, retailTradeCommodityBO.getLastErrorMessage());
					break;
				}
				//

				ErrorInfo ecOut = new ErrorInfo();
				boolean hasDBError = false;
				for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
					retailTradeCommodity.setSyncDatetime(new Date());

					if (retailTradeCommodity.getBarcodeID() != 0) { // barcodeID = 0 说明在DB中其BarcodeID为null，有可能是已被删除的商品
						Barcodes barcodes = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(retailTradeCommodity.getBarcodeID(), vipOrStaffID, ecOut, dbName);
						if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
							logger.error("查询一个条形码失败：" + ecOut.toString());

							hasDBError = true;
							params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
							params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
							break;
						} else {
							logger.info("查询一个条形码成功！barcodes=" + barcodes);
							retailTradeCommodity.setBarcodes(barcodes == null ? "" : barcodes.getBarcode());
						}
					}
				}
				if (hasDBError) {
					break;
				}

				Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(retailTrade2.getStaffID(), vipOrStaffID, ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个操作人员失败：" + ecOut.toString());
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询一个操作人员成功！staff=" + staff);
					retailTrade2.setStaffName(staff == null ? "" : staff.getName());
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "");
				}
				//
				retailTrade2.setListSlave1(retailTradeCommodityList);
			} // for

			// 是否是按商品关键字查询
			if (iUseCaseID == BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb) {
				params.put("retailAmount", retailTradeBO.getRetailAmount());
				params.put("totalGross", retailTradeBO.getTotalGross());
				params.put("totalCommNO", retailTradeBO.getTotalCommNO());
			}
			params.put(KEY_HTMLTable_Parameter_TotalRecord, retailTradeBO.getTotalRecord());
			params.put(KEY_ObjectList, list);
		} while (false);
		logger.info("返回的数据=" + params);
		return JSON.toJSONStringWithDateFormat(params, DATETIME_FORMAT_Default1, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
	}
}
