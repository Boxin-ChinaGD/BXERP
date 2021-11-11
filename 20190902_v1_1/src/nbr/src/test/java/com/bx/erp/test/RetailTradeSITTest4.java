package com.bx.erp.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.PromotionCalculator;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

/** 该测试主要是针对会员积分计算的验证。要配合RetailTradeSITTest4.xlsx来看 */
@WebAppConfiguration
public class RetailTradeSITTest4 extends BaseActionTest {
	@Resource
	private PromotionCalculator promotionCalculator;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void runRetailTradeProcess() throws Exception {
		// 创建会员
		Vip vipGet = BaseVipTest.DataInput.getVip();
		vipGet.setSex(EnumSexVip.ESV_Unknown.getIndex());
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 创建正常商品
		Commodity commodity1Get = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity1Get.setPriceRetail(10.550000d);
		Commodity commodity1Create = BaseCommodityTest.createCommodityViaAction(commodity1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1Create.getID(), Shared.DBName_Test);
		// 创建0元商品
		Commodity commodity2Get = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity2Get.setPriceRetail(0.000000d);
		Commodity commodity2Create = BaseCommodityTest.createCommodityViaAction(commodity2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(commodity2Create.getID(), Shared.DBName_Test);

		// Case1:会员正常进行消费，增加相应积分
		RetailTrade retailTrade1Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade1Get.setVipID(vipCreate.getID());
		retailTrade1Get.setAmount(commodity1Create.getPriceRetail());
		retailTrade1Get.setAmountCash(commodity1Create.getPriceRetail());
		//
		List<BaseModel> retailtradeCommodityList = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(commodity1Create.getID());
		retailTradeCommodity.setBarcodeID(barcodes1.getID());
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setPriceOriginal(commodity1Create.getPriceRetail());
		retailTradeCommodity.setOperatorStaffID(4);
		retailtradeCommodityList.add(retailTradeCommodity);
		retailTrade1Get.setListSlave1(retailtradeCommodityList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTrade1Get, Shared.DBName_Test);
		Vip VipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(VipR1.getBonus() == Math.round(retailTrade1Get.getAmount() * 10), "会员积分计算错误");
		Assert.assertTrue(VipR1.getBonus() == 106, "会员积分计算错误");

		// Case2：会员消费0元，不增加积分
		RetailTrade retailTrade2Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade2Get.setVipID(vipCreate.getID());
		retailTrade2Get.setAmount(commodity2Create.getPriceRetail());
		retailTrade2Get.setAmountCash(commodity2Create.getPriceRetail());
		//
		List<BaseModel> retailtradeCommodityList2 = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(commodity2Create.getID());
		retailTradeCommodity2.setBarcodeID(barcodes2.getID());
		retailTradeCommodity2.setNO(1);
		retailTradeCommodity2.setPriceOriginal(commodity2Create.getPriceRetail());
		retailTradeCommodity2.setOperatorStaffID(4);
		retailtradeCommodityList2.add(retailTradeCommodity2);
		retailTrade2Get.setListSlave1(retailtradeCommodityList2);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTrade2Get, Shared.DBName_Test);
		Vip Vip2R1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(Vip2R1.getBonus() == VipR1.getBonus(), "会员积分计算错误");
		Assert.assertTrue(Vip2R1.getBonus() == 106, "会员积分计算错误");

		// Case3：会员消费单次增加积分超过积分规则设定的积分
		RetailTrade retailTrade3Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade3Get.setVipID(vipCreate.getID());
		retailTrade3Get.setAmount(commodity1Create.getPriceRetail() * 10);
		retailTrade3Get.setAmountCash(commodity1Create.getPriceRetail() * 10);
		//
		List<BaseModel> retailtradeCommodityList3 = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity3 = new RetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(commodity1Create.getID());
		retailTradeCommodity3.setBarcodeID(barcodes1.getID());
		retailTradeCommodity3.setNO(10);
		retailTradeCommodity3.setPriceOriginal(commodity1Create.getPriceRetail() * 10);
		retailTradeCommodity3.setOperatorStaffID(4);
		retailtradeCommodityList3.add(retailTradeCommodity3);
		retailTrade3Get.setListSlave1(retailtradeCommodityList3);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTrade3Get, Shared.DBName_Test);
		Vip Vip3R1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		// Assert.assertTrue(Vip3R1.getBonus() == Vip2R1.getBonus() + 1000, "会员积分计算错误");
		Assert.assertTrue(Vip3R1.getBonus() == 1106, "会员积分计算错误");

		// Case4：会员消费时，参与促销活动
		// 创建促销 满50减30
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		promotionGet.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		promotionGet.setDatetimeStart(DatetimeUtil.getDays(new Date(), -2)); // 活动开始时间
		promotionGet.setExcecutionThreshold(50);
		promotionGet.setExcecutionAmount(30);
		//
		Map<String, Object> paramsCreate = promotionGet.getCreateParam(BaseBO.INVALID_CASE_ID, promotionGet);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion promotionCreate = (Promotion) promotionMapper.create(paramsCreate);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		List<BaseModel> promotionList = new ArrayList<BaseModel>();
		promotionList.add(promotionCreate);
		//
		RetailTrade retailTrade4Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade4Get.setVipID(vipCreate.getID());
		retailTrade4Get.setAmount(commodity1Create.getPriceRetail() * 5);
		retailTrade4Get.setAmountCash(commodity1Create.getPriceRetail() * 5);
		//
		List<BaseModel> retailtradeCommodityList4 = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity4 = new RetailTradeCommodity();
		retailTradeCommodity4.setCommodityID(commodity1Create.getID());
		retailTradeCommodity4.setBarcodeID(barcodes1.getID());
		retailTradeCommodity4.setNO(5);
		retailTradeCommodity4.setPriceOriginal(commodity1Create.getPriceRetail());
		retailTradeCommodity4.setOperatorStaffID(4);
		retailtradeCommodityList4.add(retailTradeCommodity4);
		retailTrade4Get.setListSlave1(retailtradeCommodityList4);
		//

		RetailTrade bestRt = promotionCalculator.calculateBestCase(retailTrade4Get, promotionList, Shared.DBName_Test);
		bestRt.setAmountCash(bestRt.getAmount());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, bestRt, Shared.DBName_Test);
		Vip Vip4R1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(Vip4R1.getBonus() == Vip3R1.getBonus() + Math.round(bestRt.getAmount() * 10), "会员积分计算错误");
		Assert.assertTrue(Vip4R1.getBonus() == 1334, "会员积分计算错误");

		// Case5：会员消费时，使用优惠券
		// 创建优惠券 满10减5
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setLeastAmount(10.000000d);
		couponGet.setReduceAmount(5.000000d);
		couponGet.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 会员领取该优惠券
		CouponCode couponCodeGet = new CouponCode();
		couponCodeGet.setVipID(vipCreate.getID());
		couponCodeGet.setCouponID(couponCreate.getID());
		couponCodeGet.setStatus(EnumCouponCodeStatus.ECCS_Normal.getIndex());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCodeGet);
		//
		RetailTrade retailTrade5Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade5Get.setVipID(vipCreate.getID());
		retailTrade5Get.setAmount(commodity1Create.getPriceRetail() - couponCreate.getReduceAmount());
		retailTrade5Get.setAmountCash(commodity1Create.getPriceRetail() - couponCreate.getReduceAmount());
		//
		List<BaseModel> retailtradeCommodityList5 = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity5 = new RetailTradeCommodity();
		retailTradeCommodity5.setCommodityID(commodity1Create.getID());
		retailTradeCommodity5.setBarcodeID(barcodes1.getID());
		retailTradeCommodity5.setNO(1);
		retailTradeCommodity5.setPriceOriginal(commodity1Create.getPriceRetail() - couponCreate.getReduceAmount());
		retailTradeCommodity5.setOperatorStaffID(4);
		retailtradeCommodityList5.add(retailTradeCommodity5);
		retailTrade5Get.setListSlave1(retailtradeCommodityList5);
		//
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
		retailTradeCoupon.setCouponCodeID(couponCodeCreate.getID());
		retailTradeCoupon.setSyncDatetime(new Date());
		retailTradeCouponList.add(retailTradeCoupon);
		retailTrade5Get.setListSlave3(retailTradeCouponList);
		// 生成零售单促销
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(0);
		retailTradePromotingFlow.setProcessFlow("使用了优惠券");
		//
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		//
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		//
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		retailTradePromotingList.add(retailTradePromoting);
		//
		retailTrade5Get.setListSlave2(retailTradePromotingList);
		retailTrade5Get.setCouponAmount(couponCreate.getReduceAmount());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTrade5Get, Shared.DBName_Test);
		Vip Vip5R1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(Vip5R1.getBonus() == Vip4R1.getBonus() + Math.round(retailTrade5Get.getAmount() * 10), "会员积分计算错误");
		Assert.assertTrue(Vip5R1.getBonus() == 1390, "会员积分计算错误");

		// Case6：会员消费时，使用优惠券并参与促销
		RetailTrade retailTrade6Get = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade6Get.setVipID(vipCreate.getID());
		retailTrade6Get.setAmount(commodity1Create.getPriceRetail() * 5);
		retailTrade6Get.setAmountCash(commodity1Create.getPriceRetail() * 5);
		//
		List<BaseModel> retailtradeCommodityList6 = new ArrayList<BaseModel>();
		RetailTradeCommodity retailTradeCommodity6 = new RetailTradeCommodity();
		retailTradeCommodity6.setCommodityID(commodity1Create.getID());
		retailTradeCommodity6.setBarcodeID(barcodes1.getID());
		retailTradeCommodity6.setNO(5);
		retailTradeCommodity6.setPriceOriginal(commodity1Create.getPriceRetail());
		retailTradeCommodity6.setOperatorStaffID(4);
		retailtradeCommodityList6.add(retailTradeCommodity6);
		retailTrade6Get.setListSlave1(retailtradeCommodityList6);
		//
		RetailTrade bestRt6 = promotionCalculator.calculateBestCase(retailTrade6Get, promotionList, Shared.DBName_Test);
		bestRt6.setAmountCash(bestRt6.getAmount());
		// 会员领取优惠券
		CouponCode couponCodeGet6 = new CouponCode();
		couponCodeGet6.setVipID(vipCreate.getID());
		couponCodeGet6.setCouponID(couponCreate.getID());
		couponCodeGet6.setStatus(EnumCouponCodeStatus.ECCS_Normal.getIndex());
		CouponCode couponCodeCreate6 = BaseCouponCodeTest.createViaMapper(couponCodeGet6);
		//
		List<BaseModel> retailTradeCouponList6 = new ArrayList<BaseModel>();
		RetailTradeCoupon retailTradeCoupon6 = new RetailTradeCoupon();
		retailTradeCoupon6.setCouponCodeID(couponCodeCreate6.getID());
		retailTradeCoupon6.setSyncDatetime(new Date());
		retailTradeCouponList6.add(retailTradeCoupon6);
		retailTrade5Get.setListSlave3(retailTradeCouponList6);
		// 生成零售单促销
		RetailTradePromotingFlow retailTradePromotingFlow6 = new RetailTradePromotingFlow();
		retailTradePromotingFlow6.setPromotionID(0);
		retailTradePromotingFlow6.setProcessFlow("使用了优惠券");
		List<RetailTradePromoting> retailTradePromotingList6 = (List<RetailTradePromoting>) bestRt6.getListSlave2();
		List<RetailTradePromotingFlow> list = (List<RetailTradePromotingFlow>) retailTradePromotingList6.get(0).getListSlave1();
		list.add(retailTradePromotingFlow6);
		//
		bestRt6.setCouponAmount(couponCreate.getReduceAmount());
		bestRt6.setAmount(bestRt6.getAmount() - couponCreate.getReduceAmount());
		bestRt6.setAmountCash(bestRt6.getAmountCash() - couponCreate.getReduceAmount());
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, bestRt6, Shared.DBName_Test);
		Vip Vip6R1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(Vip6R1.getBonus() == Vip5R1.getBonus() + Math.round(bestRt6.getAmount() * 10), "会员积分计算错误");
		Assert.assertTrue(Vip6R1.getBonus() == 1568, "会员积分计算错误");
	}

}
