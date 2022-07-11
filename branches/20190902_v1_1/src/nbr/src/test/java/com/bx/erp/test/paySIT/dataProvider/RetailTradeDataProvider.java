package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CouponScopeBO;
import com.bx.erp.action.trade.promotion.PromotionCalculator;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Vip;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.Coupon.EnumCouponType;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.CouponScope.EnumCouponScope;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

@Component("retailTradeDataProvider")
public class RetailTradeDataProvider {
	@Resource
	private PromotionCalculator promotionCalculator;

	@Resource
	private CouponScopeBO couponScopeBO;

	private static int MAX_RetailTradeCreateNums = 15;

	private final static int commonRetailtrade = 0;
	private final static int returnRetailTrade = 1;

	private static final int MAX_retailTradeCommodityNO = 100;

	private static final int CASE_cashPay = 0;

	private static final int CASE_wechatPay = 1;

	private static final int CASE_cashAndWechatPay = 2;

	private static final int cashPaymentType = 1;
	private static final int wechatPaymentType = 4;
	private static final int cashAndWechatPaymentType = 5;

	private static final int default_positive = 1;

	private static final int operatorStaffID = 3;

	public static List<RetailTrade> commonRetailTradeList = new ArrayList<RetailTrade>();
	List<RetailTrade> returnRetailTradeList = new ArrayList<RetailTrade>();
	public List<BaseModel> promotionList = new ArrayList<BaseModel>();

	private List<Commodity> commodityList;

	private List<CouponCode> availableCouponCodeList;

	private List<Coupon> couponList;

	public List<BaseModel> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<BaseModel> promotionList) {
		this.promotionList = promotionList;
	}

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}

	public List<CouponCode> getAvailableCouponCodeList() {
		return availableCouponCodeList;
	}

	public void setAvailableCouponCodeList(List<CouponCode> couponCodeList) {
		this.availableCouponCodeList = couponCodeList;
	}

	public List<Commodity> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<Commodity> commodityList) {
		this.commodityList = commodityList;
	}

	private List<Vip> vipList;

	public List<Vip> getVipList() {
		return vipList;
	}

	public void setVipList(List<Vip> vipList) {
		this.vipList = vipList;
	}

	public List<RetailTrade> generateRandomRetailTrades() throws Exception {
		List<RetailTrade> retailTradeList = new ArrayList<>();
		Random random = new Random();
		int count = random.nextInt(MAX_RetailTradeCreateNums) + default_positive;
		for (int i = 0; i < count; i++) {
			retailTradeList.add(generateRandomRetailTrade());
		}
		return retailTradeList;
	}

	public RetailTrade generateRandomRetailTrade() throws Exception {
		Random random = new Random();
		RetailTrade retailTradeInput = new RetailTrade();
		setCommonProperty(retailTradeInput);
		switch (random.nextInt(2)) {
		case commonRetailtrade:
			setCommonRetailtrade(retailTradeInput);
			break;
		case returnRetailTrade:
			setReturnRetailtrade(retailTradeInput);
			break;
		default:
			break;
		}
		return retailTradeInput;
	}

	private void setCommonProperty(RetailTrade retailTradeInput) throws InterruptedException {
		Random random = new Random();
		retailTradeInput.setPos_ID(1); // TODO 是否要有个随机创建POS机的JIRA
		retailTradeInput.setSn(Shared.generateRetailTradeSN(retailTradeInput.getPos_ID()));
		retailTradeInput.setVipID(vipList.get(random.nextInt(vipList.size())).getID());
		retailTradeInput.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
		retailTradeInput.setSaleDatetime(new Date());
		retailTradeInput.setLogo("11" + random.nextInt(1000));
		retailTradeInput.setStaffID(2); // TODO 随机创建staff?
		retailTradeInput.setPaymentAccount("12");
		retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTradeInput.setRemark("11111");
		retailTradeInput.setSyncDatetime(new Date());
		retailTradeInput.setSmallSheetID(1); // TODO 随机创建smallsheet?
		retailTradeInput.setSyncDatetime(new Date());
		retailTradeInput.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setWxRefundNO(String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setDatetimeStart(new Date());
		retailTradeInput.setDatetimeEnd(new Date());
		retailTradeInput.setOperatorStaffID(operatorStaffID);
	}

	private void setCommonRetailtrade(RetailTrade retailTradeInput) throws Exception {
		retailTradeInput.setSourceID(BaseAction.INVALID_ID);
		Random random = new Random();
		List<RetailTradeCommodity> retailTradeCommodityList = new ArrayList<>();
		int saleCommodityNumber = random.nextInt(commodityList.size());
		saleCommodityNumber = saleCommodityNumber == 0 ? default_positive : saleCommodityNumber;
		HashSet<Integer> commodityIdSet = new HashSet<>();
		Commodity commodity = null;
		while (commodityIdSet.size() < saleCommodityNumber) {
			commodity = (Commodity) commodityList.get(random.nextInt(commodityList.size()));
			commodityIdSet.add(commodity.getID());
		}
		Integer[] commodityIdArr = new Integer[commodityIdSet.size()];
		commodityIdSet.toArray(commodityIdArr);
		// 随机零售商品数量，退货价
		int retailTradeCommodityNO;
		Double totalAmount = 0.0;
		for (int i = 0; i < commodityIdArr.length; i++) {
			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setCommodityID(commodityIdArr[i]);
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodityIdArr[i], Shared.DBName_Test);
			retailTradeCommodity.setBarcodeID(barcodes.getID());
			retailTradeCommodityNO = random.nextInt(MAX_retailTradeCommodityNO);
			retailTradeCommodityNO = retailTradeCommodityNO == 0 ? default_positive : retailTradeCommodityNO;
			retailTradeCommodity.setNO(retailTradeCommodityNO);
			retailTradeCommodity.setNOCanReturn(retailTradeCommodityNO);
			retailTradeCommodity.setPriceVIPOriginal(10); //
			ErrorInfo ecOut = new ErrorInfo();
			Commodity commodityR1 = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityIdArr[i], BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commodityR1 == null) {
				Assert.assertTrue(false, "沒有找到该商品");
			}
			retailTradeCommodity.setPriceOriginal(commodityR1.getPriceRetail());
			retailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceOriginal()); //
			totalAmount += retailTradeCommodity.getPriceReturn() * retailTradeCommodityNO;
			retailTradeCommodityList.add(retailTradeCommodity);
		}
		retailTradeInput.setAmount(totalAmount);
		retailTradeInput.setListSlave1(retailTradeCommodityList);
		System.out.println("促销前的零售单为=" + retailTradeInput);
		// 计算促销
		RetailTrade bestRt = promotionCalculator.calculateBestCase(retailTradeInput, promotionList, Shared.DBName_Test);
		retailTradeInput.setAmount(bestRt.getAmount());
		retailTradeInput.setListSlave2(bestRt.getListSlave2());
		retailTradeInput.setListSlave1(bestRt.getListSlave1());
		
		System.out.println("促销后的零售单为=" + retailTradeInput);

		// 是否使用优惠券
		if (random.nextBoolean()) {
			consumeCoupons(retailTradeInput);
		}
		// 支付方式
		switch (random.nextInt(3)) {
		case CASE_cashPay:
			retailTradeInput.setPaymentType(cashPaymentType);
			retailTradeInput.setAmountCash(retailTradeInput.getAmount());
			break;
		case CASE_wechatPay:
			retailTradeInput.setPaymentType(wechatPaymentType);
			retailTradeInput.setAmountWeChat(retailTradeInput.getAmount());
			break;
		case CASE_cashAndWechatPay:
			retailTradeInput.setPaymentType(cashAndWechatPaymentType);
			double cashAmount = retailTradeInput.getAmount() < 1 ? retailTradeInput.getAmount() : random.nextInt((int) retailTradeInput.getAmount());
			retailTradeInput.setAmountCash(GeneralUtil.div(cashAmount, 2, 6));
			retailTradeInput.setAmountWeChat(GeneralUtil.sub(retailTradeInput.getAmount(), retailTradeInput.getAmountCash()));
			break;
		default:
			break;
		}
		System.out.println("最终生成的零售单为：" + retailTradeInput);
	}

	@SuppressWarnings("unchecked")
	private Coupon getCoupon(RetailTrade retailTradeInput) throws CloneNotSupportedException {
		CouponCode couponCode = null;
		Coupon coupon = null;
		for (int i = 0; i < availableCouponCodeList.size(); i++) {
			if (availableCouponCodeList.get(i).getVipID() == retailTradeInput.getVipID()) {
				// 获取到使用的这张券是属于那种优惠券
				for (int j = 0; j < couponList.size(); j++) {
					if (availableCouponCodeList.get(i).getCouponID() == couponList.get(j).getID()) {
						coupon = (Coupon) couponList.get(j).clone();
						break;
					}
				}
				//
				if (coupon != null) {
					// 判断此券对于这张零售单是否可用
					Double amount = 0.000000d;
					if (coupon.getScope() == CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
						// 计算零售单商品满足优惠券范围的金额
						List<CouponScope> couponScopeList = (List<CouponScope>) coupon.getListSlave1();
						List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeInput.getListSlave1();
						for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
							for (CouponScope couponScope : couponScopeList) {
								if (retailTradeCommodity.getCommodityID() == couponScope.getCommodityID()) {
									amount = GeneralUtil.sum(amount, GeneralUtil.mul(retailTradeCommodity.getNO(), retailTradeCommodity.getPriceReturn())); // 使用PirceReturn是因为有可能使用优惠券前次单已进行了促销活动
								}
							}
						}
					} else {
						amount = retailTradeInput.getAmount();
					}
					//
					if (amount < coupon.getLeastAmount()) {
						continue;
					}
					couponCode = availableCouponCodeList.get(i);
					availableCouponCodeList.remove(i);

					List<BaseModel> couponCodes = new ArrayList<BaseModel>();
					couponCodes.add(couponCode);
					coupon.setListSlave1(couponCodes);
					break;
				} else {
					System.out.println("优惠券种类列表中没找到要消费的这种优惠券");
				}
			}
		}
		//
		if (couponCode == null) {
			coupon = null;
		}
		//
		return coupon;
	}
	
	@SuppressWarnings("unchecked")
	private void appendRetailTradePromoting(RetailTrade retailTradeInput, RetailTradePromotingFlow retailTradePromotingFlow) {
		if (retailTradeInput.getListSlave2() == null) {
			List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
			retailTradePromotingFlowList.add(retailTradePromotingFlow);
			//
			RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
			retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
			//
			List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
			retailTradePromotingList.add(retailTradePromoting);
			//
			retailTradeInput.setListSlave2(retailTradePromotingList);
		} else {
			List<RetailTradePromoting> retailTradePromotingList = (List<RetailTradePromoting>) retailTradeInput.getListSlave2();
			List<RetailTradePromotingFlow> list = (List<RetailTradePromotingFlow>) retailTradePromotingList.get(0).getListSlave1();
			list.add(retailTradePromotingFlow);
		}
	}

	private void consumeCoupons(RetailTrade retailTradeInput) throws CloneNotSupportedException { // TODO 尚未模拟到优惠券过期而会员手中持有1张未使用的券的情况
		Coupon coupon = getCoupon(retailTradeInput);
		if (coupon != null) {
			CouponCode couponCode = (CouponCode) coupon.getListSlave1().get(0);
			System.out.println("当前使用的优惠券：" + coupon);
			List<RetailTradeCoupon> retailTradeCouponList = new ArrayList<RetailTradeCoupon>();
			RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
			retailTradeCoupon.setCouponCodeID(couponCode.getID());
			retailTradeCoupon.setSyncDatetime(new Date());
			retailTradeCouponList.add(retailTradeCoupon);
			retailTradeInput.setListSlave3(retailTradeCouponList);
			// 生成零售单促销
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setPromotionID(0);
			retailTradePromotingFlow.setProcessFlow("使用了优惠券");
			appendRetailTradePromoting(retailTradeInput, retailTradePromotingFlow);
			
			// 获取初始零售总额
			double totalAmount = retailTradeInput.getAmount();
			//
			if (coupon.getType() == Coupon.EnumCouponType.ECT_Cash.getIndex()) {
				retailTradeInput.setAmount(GeneralUtil.sub(retailTradeInput.getAmount(), coupon.getReduceAmount()));
				retailTradeInput.setCouponAmount(coupon.getReduceAmount());
			} else {
				Double amountToPay = GeneralUtil.mul(retailTradeInput.getAmount(), coupon.getDiscount());
				retailTradeInput.setCouponAmount(GeneralUtil.sub(retailTradeInput.getAmount(), amountToPay));
				retailTradeInput.setAmount(amountToPay);
			}
			System.out.println("零售单使用优惠券节省=" + retailTradeInput.getCouponAmount());
			// 重新计算零售单商品的退货价
			calculateReturnPrice(coupon, retailTradeInput, totalAmount, retailTradeInput.getAmount());
			System.out.println("此零售单为：" + retailTradeInput);
		}
	}

	private void setReturnRetailtrade(RetailTrade retailTradeInput) throws Exception {
		Random random = new Random();
		if (commonRetailTradeList.size() > 0) {
			int randomIndex = random.nextInt(commonRetailTradeList.size());
			RetailTrade retailTradeToReturn = commonRetailTradeList.get(randomIndex);
			commonRetailTradeList.remove(randomIndex);
			retailTradeInput.setSourceID(retailTradeToReturn.getID());
			// 随机退商品种数，商品个数，退款方式
			Double totalReturnAmount = 0.0;
			List<RetailTradeCommodity> returnRetailTradeCommodityList = new ArrayList<>();
			int returnCommodityTypeNO = random.nextInt(retailTradeToReturn.getListSlave1().size());
			returnCommodityTypeNO = returnCommodityTypeNO == 0 ? default_positive : returnCommodityTypeNO;
			for (int i = 0; i < returnCommodityTypeNO; i++) {
				RetailTradeCommodity returnRetailTradeCommodity = new RetailTradeCommodity();
				RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) retailTradeToReturn.getListSlave1().get(i);
				int returnCommNO = random.nextInt(retailTradeCommodity.getNO());
				returnCommNO = returnCommNO == 0 ? default_positive : returnCommNO;
				returnRetailTradeCommodity.setNO(returnCommNO);
				returnRetailTradeCommodity.setCommodityID(retailTradeCommodity.getCommodityID());
				returnRetailTradeCommodity.setBarcodeID(retailTradeCommodity.getBarcodeID());
				returnRetailTradeCommodity.setPriceReturn(retailTradeCommodity.getPriceReturn());
				totalReturnAmount += returnRetailTradeCommodity.getNO() * returnRetailTradeCommodity.getPriceReturn();
				returnRetailTradeCommodityList.add(returnRetailTradeCommodity);
			}
			// 关于混合支付（微信+现金）的零售单的退货：
			// 1、断网情况下，均以现金退货；
			// 2、有网情况下，退货金额如果小于微信金额，直接用现金退，退货金额如果大于等于微信金额，
			// 此时现金够退便直接以现金退，如果现金不够先把微信部分全部退出剩余的再用现金退；
			retailTradeInput.setAmount(totalReturnAmount);
			if (retailTradeToReturn.getPaymentType() == cashPaymentType) {
				retailTradeInput.setPaymentType(cashPaymentType);
				retailTradeInput.setAmountCash(totalReturnAmount);
			} else if (retailTradeToReturn.getPaymentType() == wechatPaymentType) {
				switch (random.nextInt(3)) {
				case CASE_cashPay: //
					retailTradeInput.setPaymentType(cashPaymentType);
					retailTradeInput.setAmountCash(totalReturnAmount);
					break;
				case CASE_wechatPay:
					retailTradeInput.setPaymentType(wechatPaymentType);
					retailTradeInput.setAmountWeChat(totalReturnAmount);
					break;
				case CASE_cashAndWechatPay:
					retailTradeInput.setPaymentType(cashAndWechatPaymentType);
					if (totalReturnAmount < 1) {
						retailTradeInput.setAmountWeChat(GeneralUtil.div(totalReturnAmount, 2, 6));
					} else {
						double wechatAmount = random.nextInt(totalReturnAmount.intValue());
						wechatAmount = Math.floor((random.nextDouble() + wechatAmount) * 100) / 100;
						if (wechatAmount > retailTradeToReturn.getAmountWeChat()) { // 微信退款金额不能比零售时微信支付的金额多
							wechatAmount = retailTradeToReturn.getAmountWeChat();
						}
						retailTradeInput.setAmountWeChat(wechatAmount);
						retailTradeInput.setAmountCash(totalReturnAmount - retailTradeInput.getAmountWeChat());
					}
					break;
				default:
					break;
				}
			} else if (retailTradeToReturn.getPaymentType() == cashAndWechatPaymentType) {
				switch (random.nextInt(2)) {
				case CASE_cashPay: //
					retailTradeInput.setPaymentType(cashPaymentType);
					retailTradeInput.setAmountCash(totalReturnAmount);
					break;
				default:
					retailTradeInput.setPaymentType(cashAndWechatPaymentType);
					double wechatAmount = random.nextInt(totalReturnAmount.intValue());
					wechatAmount = Math.floor((random.nextDouble() + wechatAmount) * 100) / 100;
					if (wechatAmount > retailTradeToReturn.getAmountWeChat()) { // 微信退款金额不能比零售时微信支付的金额多
						wechatAmount = retailTradeToReturn.getAmountWeChat();
					}
					retailTradeInput.setAmountWeChat(wechatAmount);
					retailTradeInput.setAmountCash(totalReturnAmount - retailTradeInput.getAmountWeChat());
					break;
				}
			} else {
				throw new RuntimeException("错误的支付方式");
			}
			retailTradeInput.setListSlave1(returnRetailTradeCommodityList);
		} else { // 没有零售单，先创建
			setCommonRetailtrade(retailTradeInput);
		}
	}

	/** @param coupon
	 *            使用的优惠券
	 * @param retailTradeInput
	 *            计算退货价的零售单
	 * @param totalAmount
	 *            未进行促销的零售总额
	 * @param totalAmountofCoupon
	 *            使用优惠券后的零售总额 */
	public void calculateReturnPrice(Coupon coupon, RetailTrade retailTradeInput, double totalAmount, double totalAmountofCoupon) {
		if (coupon.getScope() == EnumCouponScope.ECS_AllCommodities.getIndex()) {
			if (coupon.getType() == EnumCouponType.ECT_Cash.getIndex()) { // 现金券
				for (Object o : retailTradeInput.getListSlave1()) {
					RetailTradeCommodity rtc = (RetailTradeCommodity) o;
					rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), totalAmountofCoupon), GeneralUtil.mul(totalAmount, rtc.getNO()), 6))));
				}
			} else {
				for (Object o : retailTradeInput.getListSlave1()) { // 折扣券
					RetailTradeCommodity rtc = (RetailTradeCommodity) o;
					rtc.setPriceReturn(GeneralUtil.mul(rtc.getPriceReturn(), coupon.getDiscount()));
				}
			}
		} else {
			CouponScope couponScope = new CouponScope();
			couponScope.setCouponID(coupon.getID());
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<?> couponScopeList = couponScopeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, couponScope);
			if (couponScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				Assert.assertTrue(false, "查找优惠券作用范围失败！");
			}
			// 参与优惠券零售商品
			List<RetailTradeCommodity> retailTradeCommoditiyToCouponList = new ArrayList<RetailTradeCommodity>();
			// 不参与优惠券零售商品
			List<RetailTradeCommodity> retailTradeCommoditiyList = new ArrayList<RetailTradeCommodity>();
			double couponCommodityAmount = 0.000000d;
			// 是否参与优惠券
			boolean isCouponCommodity;
			for (Object o : retailTradeInput.getListSlave1()) {
				isCouponCommodity = false;
				RetailTradeCommodity rtc = (RetailTradeCommodity) o;
				for (Object o2 : couponScopeList) {
					CouponScope cs = (CouponScope) o2;
					if (rtc.getCommodityID() == cs.getCommodityID()) {
						// 计算出参与优惠券的商品总价
						couponCommodityAmount += GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO());
						retailTradeCommoditiyToCouponList.add(rtc);
						isCouponCommodity = true;
						break;
					}
				}
				if (!isCouponCommodity) {
					retailTradeCommoditiyList.add(rtc);
				}
			}
			if (coupon.getType() == EnumCouponType.ECT_Cash.getIndex()) { // 现金券
				for (Object o : retailTradeCommoditiyToCouponList) {
					RetailTradeCommodity rtc = (RetailTradeCommodity) o;
					rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), //
							GeneralUtil.sub(couponCommodityAmount, coupon.getReduceAmount())), GeneralUtil.mul(couponCommodityAmount, rtc.getNO()), 6))));
				}
			} else {
				for (Object o : retailTradeCommoditiyToCouponList) { // 折扣券
					RetailTradeCommodity rtc = (RetailTradeCommodity) o;
					rtc.setPriceReturn(GeneralUtil.mul(rtc.getPriceReturn(), coupon.getDiscount()));
				}
			}
			retailTradeCommoditiyToCouponList.addAll(retailTradeCommoditiyList);
			retailTradeInput.setListSlave1(retailTradeCommoditiyToCouponList);
		}
	}
}
