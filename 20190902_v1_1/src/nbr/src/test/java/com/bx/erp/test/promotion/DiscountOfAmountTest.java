package com.bx.erp.test.promotion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.DiscountOfAmount;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.trade.promotion.PromotionCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class DiscountOfAmountTest extends BaseActionTest {

	private Staff staff;
	@Resource
	PromotionCache pp;

	@Resource
	DiscountOfAmount doa;

	final int YEAR = 2018;
	final int MONTH = 6; // Not 7
	final int DAY = 15;
	final int HOUR = 12;
	final int MINUTE = 30;
	final int SECOND = 30;

	public static class DataInput {
		private static RetailTradeCommodity rtcInput = null;

		protected static final RetailTradeCommodity getRetailTradeCommodity() throws CloneNotSupportedException, InterruptedException {
			rtcInput = new RetailTradeCommodity();
			rtcInput.setNO(1);
			rtcInput.setOperatorStaffID(STAFF_ID1);

			return (RetailTradeCommodity) rtcInput.clone();
		}

	}

	@BeforeClass
	public void setup() {
		super.setUp();
		staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();
	}

	@Test
	public void promoteNewTest() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存

		Calendar cal = Calendar.getInstance();

		cal.set(2007, 12 - 1, 12);
		Date startDatetime = cal.getTime();
		cal.set(2019, 12 - 1, 12);
		Date endDatetime = cal.getTime();
		System.out.println("-------------------------------Case1：三个活动都为指定满折------------------------------------");
		// 创建一个折扣满减活动A, 放到缓存里
		Promotion promotionA = BasePromotionTest.DataInput.getPromotion();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(1);
		promotionA.setStatus(0);
		promotionA.setName("指定商品满10-9折");
		promotionA.setExcecutionThreshold(10);
		promotionA.setExcecutionDiscount(0.9d);
		promotionA.setDatetimeStart(startDatetime);
		promotionA.setDatetimeEnd(endDatetime);

		List<PromotionScope> piList = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(1);
			piList.add(promotionScope);
		}

		promotionA.setListSlave1(piList);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionA, Shared.DBName_Test, staff.getID());
		// 创建一个满折活动B
		Promotion promotionB = BasePromotionTest.DataInput.getPromotion();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(1);
		promotionB.setStatus(0);
		promotionB.setName("指定商品满100-8折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8d);
		promotionB.setDatetimeStart(startDatetime);
		promotionB.setDatetimeEnd(endDatetime);

		List<PromotionScope> piList2 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 8; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(2);
			piList2.add(promotionScope);
		}
		promotionB.setListSlave1(piList2);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionB, Shared.DBName_Test, staff.getID());
		// 创建一个满折活动C
		Promotion promotionC = BasePromotionTest.DataInput.getPromotion();
		promotionC.setID(3);
		promotionC.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionC.setScope(1);
		promotionC.setStatus(0);
		promotionC.setName("指定商品满500-6折");
		promotionC.setExcecutionThreshold(500);
		promotionC.setExcecutionAmount(1000);
		promotionC.setExcecutionDiscount(0.6d);
		promotionC.setDatetimeStart(startDatetime);
		promotionC.setDatetimeEnd(endDatetime);

		List<PromotionScope> piList3 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(3);
			piList3.add(promotionScope);
		}
		promotionC.setListSlave1(piList3);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionC, Shared.DBName_Test, staff.getID());

		// 创建一个零售单
		List<RetailTradeCommodity> rtclist = new ArrayList<RetailTradeCommodity>();

		Random ran = new Random();
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setLocalSN(ran.nextInt(1000));
		Thread.sleep(1);
		rt.setPos_ID(ran.nextInt(5) + 1);
		rt.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSaleDatetime(new Date());
		rt.setStaffID(ran.nextInt(5) + 1);
		rt.setPaymentType(ran.nextInt(5) + 1);
		rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSourceID(BaseAction.INVALID_ID);
		rt.setAmount(1540);

		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);
		
		// 添加商品到零售单
		RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
		rtc2.setTradeID(rtCreate.getID());
		rtc2.setCommodityID(4);
		rtc2.setPriceOriginal(168);
		rtc2.setBarcodeID(1);
		rtc2.setNO(5);

		Map<String, Object> rec2Params = rtc2.getCreateParam(BaseBO.INVALID_CASE_ID, rtc2);
		RetailTradeCommodity rtc2Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec2Params);
		rtclist.add(rtc2Create);

		Assert.assertTrue(rtc2Create != null && EnumErrorCode.values()[Integer.parseInt(rec2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc2Create == null ? "providerCreate == null" : rtc2Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc3 = DataInput.getRetailTradeCommodity();
		rtc3.setTradeID(rtCreate.getID());
		rtc3.setCommodityID(5);
		rtc3.setPriceOriginal(158);
		rtc3.setBarcodeID(1);
		rtc3.setNO(4);

		Map<String, Object> rec3Params = rtc3.getCreateParam(BaseBO.INVALID_CASE_ID, rtc3);
		RetailTradeCommodity rtc3Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec3Params);
		rtclist.add(rtc3Create);

		Assert.assertTrue(rtc3Create != null && EnumErrorCode.values()[Integer.parseInt(rec3Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc3Create == null ? "providerCreate == null" : rtc3Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc4 = DataInput.getRetailTradeCommodity();
		rtc4.setTradeID(rtCreate.getID());
		rtc4.setCommodityID(6);
		rtc4.setPriceOriginal(178);
		rtc4.setBarcodeID(1);
		rtc4.setNO(5);

		Map<String, Object> rec4Params = rtc4.getCreateParam(BaseBO.INVALID_CASE_ID, rtc4);
		RetailTradeCommodity rtc4Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec4Params);
		rtclist.add(rtc4Create);

		Assert.assertTrue(rtc4Create != null && EnumErrorCode.values()[Integer.parseInt(rec4Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc4Create == null ? "providerCreate == null" : rtc4Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc5 = DataInput.getRetailTradeCommodity();
		rtc5.setTradeID(rtCreate.getID());
		rtc5.setCommodityID(7);
		rtc5.setPriceOriginal(98);
		rtc5.setBarcodeID(1);
		rtc5.setNO(10);

		Map<String, Object> rec5Params = rtc5.getCreateParam(BaseBO.INVALID_CASE_ID, rtc5);
		RetailTradeCommodity rtc5Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec5Params);
		rtclist.add(rtc5Create);

		Assert.assertTrue(rtc5Create != null && EnumErrorCode.values()[Integer.parseInt(rec5Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc5Create == null ? "providerCreate == null" : rtc5Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc6 = DataInput.getRetailTradeCommodity();
		rtc6.setTradeID(rtCreate.getID());
		rtc6.setCommodityID(8);
		rtc6.setPriceOriginal(138);
		rtc6.setBarcodeID(1);
		rtc6.setNO(2);

		Map<String, Object> rec6Params = rtc6.getCreateParam(BaseBO.INVALID_CASE_ID, rtc6);
		RetailTradeCommodity rtc6Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec6Params);
		rtclist.add(rtc6Create);

		System.out.println(EnumErrorCode.values()[Integer.parseInt(rec6Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(rtc6Create != null && EnumErrorCode.values()[Integer.parseInt(rec6Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc6Create == null ? "providerCreate == null" : rtc6Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc7 = DataInput.getRetailTradeCommodity();
		rtc7.setTradeID(rtCreate.getID());
		rtc7.setCommodityID(9);
		rtc7.setPriceOriginal(178);
		rtc7.setBarcodeID(1);
		rtc7.setNO(14);

		Map<String, Object> rec7Params = rtc7.getCreateParam(BaseBO.INVALID_CASE_ID, rtc7);
		RetailTradeCommodity rtc7Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec7Params);
		rtclist.add(rtc7Create);

		Assert.assertTrue(rtc7Create != null && EnumErrorCode.values()[Integer.parseInt(rec7Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc7Create == null ? "providerCreate == null" : rtc7Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc8 = DataInput.getRetailTradeCommodity();
		rtc8.setTradeID(rtCreate.getID());
		rtc8.setCommodityID(10);
		rtc8.setPriceOriginal(168);
		rtc8.setBarcodeID(1);

		Map<String, Object> rec8Params = rtc8.getCreateParam(BaseBO.INVALID_CASE_ID, rtc8);
		RetailTradeCommodity rtc8Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec8Params);
		rtclist.add(rtc8Create);

		Assert.assertTrue(rtc8Create != null && EnumErrorCode.values()[Integer.parseInt(rec8Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc8Create == null ? "providerCreate == null" : rtc8Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc9 = DataInput.getRetailTradeCommodity();
		rtc9.setTradeID(rtCreate.getID());
		rtc9.setCommodityID(11);
		rtc9.setPriceOriginal(188);
		rtc9.setBarcodeID(1);

		Map<String, Object> rec9Params = rtc9.getCreateParam(BaseBO.INVALID_CASE_ID, rtc9);
		RetailTradeCommodity rtc9Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec9Params);
		rtclist.add(rtc9Create);

		Assert.assertTrue(rtc9Create != null && EnumErrorCode.values()[Integer.parseInt(rec9Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc9Create == null ? "providerCreate == null" : rtc9Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc10 = DataInput.getRetailTradeCommodity();
		rtc10.setTradeID(rtCreate.getID());
		rtc10.setCommodityID(12);
		rtc10.setPriceOriginal(128);
		rtc10.setBarcodeID(1);
		rtc10.setNO(11);

		Map<String, Object> rec10Params = rtc10.getCreateParam(BaseBO.INVALID_CASE_ID, rtc10);
		RetailTradeCommodity rtc10Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec10Params);
		rtclist.add(rtc10Create);

		Assert.assertTrue(rtc10Create != null && EnumErrorCode.values()[Integer.parseInt(rec10Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc10Create == null ? "providerCreate == null" : rtc10Create);

		// 将零售单和零售单商品set到CashReducingOfAmount
		rtCreate.setListSlave1(rtclist);
		doa.setRetailTrade(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false);
		for (BaseModel baseModel : readN) {
			Promotion promotion = (Promotion) baseModel;
			System.out.println("**********************////////////" + promotion);
		}

		rtCreate.setAmount(doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test));
		// doa.prePromote(Shared.DBName_Test);
		// 调用CashReducingOfAmount.promote方法去计算促销
		System.out.println("-----------------------------商品原价-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--退货价：" + rtc.getPriceReturn());
		}
		doa.promote(rtCreate, EnumPromotionScope.EPS_SpecifiedCommodities, CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false), Shared.DBName_Test);
		System.out.println("-----------------------------确认支付价格后-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--退货价：" + rtc.getPriceReturn());
		}
		RetailTrade rtNew = doa.getRetailTrade();
		System.out.println(rtNew.toString());
	}

	@Test
	public void promoteNewTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存

		Calendar cal = Calendar.getInstance();

		cal.set(2007, 12 - 1, 12);
		Date startDatetime = cal.getTime();
		cal.set(2019, 12 - 1, 12);
		Date endDatetime = cal.getTime();
		System.out.println("-------------------------------Case2：三个活动都为全场满折------------------------------------");
		// 创建一个折扣满减活动A, 放到缓存里
		Promotion promotionA = BasePromotionTest.DataInput.getPromotion();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(0);
		promotionA.setStatus(0);
		promotionA.setName("全场商品满10-9折");
		promotionA.setExcecutionThreshold(10);
		promotionA.setExcecutionDiscount(0.9d);
		promotionA.setDatetimeStart(startDatetime);
		promotionA.setDatetimeEnd(endDatetime);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionA, Shared.DBName_Test, staff.getID());
		// 创建一个满折活动B
		Promotion promotionB = BasePromotionTest.DataInput.getPromotion();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(0);
		promotionB.setStatus(0);
		promotionB.setName("全场商品满100-8折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8d);
		promotionB.setDatetimeStart(startDatetime);
		promotionB.setDatetimeEnd(endDatetime);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionB, Shared.DBName_Test, staff.getID());
		// 创建一个满折活动C
		Promotion promotionC = BasePromotionTest.DataInput.getPromotion();
		promotionC.setID(3);
		promotionC.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionC.setScope(0);
		promotionC.setStatus(0);
		promotionC.setName("全场商品满500-6折");
		promotionC.setExcecutionThreshold(500);
		promotionC.setExcecutionAmount(1000);
		promotionC.setExcecutionDiscount(0.6d);
		promotionC.setDatetimeStart(startDatetime);
		promotionC.setDatetimeEnd(endDatetime);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionC, Shared.DBName_Test, staff.getID());

		// 创建一个零售单
		List<RetailTradeCommodity> rtclist = new ArrayList<RetailTradeCommodity>();

		Random ran = new Random();
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setLocalSN(ran.nextInt(1000));
		Thread.sleep(1);
		rt.setPos_ID(ran.nextInt(5) + 1);
		rt.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSaleDatetime(new Date());
		rt.setStaffID(ran.nextInt(5) + 1);
		rt.setPaymentType(ran.nextInt(5) + 1);
		rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSourceID(BaseAction.INVALID_ID);
		rt.setAmount(1540);

		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);

		// 添加商品到零售单
		RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
		rtc2.setTradeID(rtCreate.getID());
		rtc2.setCommodityID(4);
		rtc2.setPriceOriginal(168);
		rtc2.setBarcodeID(1);
		rtc2.setNO(5);

		Map<String, Object> rec2Params = rtc2.getCreateParam(BaseBO.INVALID_CASE_ID, rtc2);
		RetailTradeCommodity rtc2Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec2Params);
		rtclist.add(rtc2Create);

		Assert.assertTrue(rtc2Create != null && EnumErrorCode.values()[Integer.parseInt(rec2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc2Create == null ? "providerCreate == null" : rtc2Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc3 = DataInput.getRetailTradeCommodity();
		rtc3.setTradeID(rtCreate.getID());
		rtc3.setCommodityID(5);
		rtc3.setPriceOriginal(158);
		rtc3.setBarcodeID(1);
		rtc3.setNO(4);

		Map<String, Object> rec3Params = rtc3.getCreateParam(BaseBO.INVALID_CASE_ID, rtc3);
		RetailTradeCommodity rtc3Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec3Params);
		rtclist.add(rtc3Create);

		Assert.assertTrue(rtc3Create != null && EnumErrorCode.values()[Integer.parseInt(rec3Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc3Create == null ? "providerCreate == null" : rtc3Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc4 = DataInput.getRetailTradeCommodity();
		rtc4.setTradeID(rtCreate.getID());
		rtc4.setCommodityID(6);
		rtc4.setPriceOriginal(178);
		rtc4.setBarcodeID(1);
		rtc4.setNO(5);

		Map<String, Object> rec4Params = rtc4.getCreateParam(BaseBO.INVALID_CASE_ID, rtc4);
		RetailTradeCommodity rtc4Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec4Params);
		rtclist.add(rtc4Create);

		Assert.assertTrue(rtc4Create != null && EnumErrorCode.values()[Integer.parseInt(rec4Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc4Create == null ? "providerCreate == null" : rtc4Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc5 = DataInput.getRetailTradeCommodity();
		rtc5.setTradeID(rtCreate.getID());
		rtc5.setCommodityID(7);
		rtc5.setPriceOriginal(98);
		rtc5.setBarcodeID(1);
		rtc5.setNO(10);

		Map<String, Object> rec5Params = rtc5.getCreateParam(BaseBO.INVALID_CASE_ID, rtc5);
		RetailTradeCommodity rtc5Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec5Params);
		rtclist.add(rtc5Create);

		Assert.assertTrue(rtc5Create != null && EnumErrorCode.values()[Integer.parseInt(rec5Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc5Create == null ? "providerCreate == null" : rtc5Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc6 = DataInput.getRetailTradeCommodity();
		rtc6.setTradeID(rtCreate.getID());
		rtc6.setCommodityID(8);
		rtc6.setPriceOriginal(138);
		rtc6.setBarcodeID(1);
		rtc6.setNO(2);

		Map<String, Object> rec6Params = rtc6.getCreateParam(BaseBO.INVALID_CASE_ID, rtc6);
		RetailTradeCommodity rtc6Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec6Params);
		rtclist.add(rtc6Create);

		System.out.println(EnumErrorCode.values()[Integer.parseInt(rec6Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(rtc6Create != null && EnumErrorCode.values()[Integer.parseInt(rec6Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc6Create == null ? "providerCreate == null" : rtc6Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc7 = DataInput.getRetailTradeCommodity();
		rtc7.setTradeID(rtCreate.getID());
		rtc7.setCommodityID(9);
		rtc7.setPriceOriginal(178);
		rtc7.setBarcodeID(1);
		rtc7.setNO(14);

		Map<String, Object> rec7Params = rtc7.getCreateParam(BaseBO.INVALID_CASE_ID, rtc7);
		RetailTradeCommodity rtc7Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec7Params);
		rtclist.add(rtc7Create);

		Assert.assertTrue(rtc7Create != null && EnumErrorCode.values()[Integer.parseInt(rec7Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc7Create == null ? "providerCreate == null" : rtc7Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc8 = DataInput.getRetailTradeCommodity();
		rtc8.setTradeID(rtCreate.getID());
		rtc8.setCommodityID(10);
		rtc8.setPriceOriginal(168);
		rtc8.setBarcodeID(1);

		Map<String, Object> rec8Params = rtc8.getCreateParam(BaseBO.INVALID_CASE_ID, rtc8);
		RetailTradeCommodity rtc8Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec8Params);
		rtclist.add(rtc8Create);

		Assert.assertTrue(rtc8Create != null && EnumErrorCode.values()[Integer.parseInt(rec8Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc8Create == null ? "providerCreate == null" : rtc8Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc9 = DataInput.getRetailTradeCommodity();
		rtc9.setTradeID(rtCreate.getID());
		rtc9.setCommodityID(11);
		rtc9.setPriceOriginal(188);
		rtc9.setBarcodeID(1);

		Map<String, Object> rec9Params = rtc9.getCreateParam(BaseBO.INVALID_CASE_ID, rtc9);
		RetailTradeCommodity rtc9Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec9Params);
		rtclist.add(rtc9Create);

		Assert.assertTrue(rtc9Create != null && EnumErrorCode.values()[Integer.parseInt(rec9Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc9Create == null ? "providerCreate == null" : rtc9Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc10 = DataInput.getRetailTradeCommodity();
		rtc10.setTradeID(rtCreate.getID());
		rtc10.setCommodityID(12);
		rtc10.setPriceOriginal(128);
		rtc10.setBarcodeID(1);
		rtc10.setNO(11);

		Map<String, Object> rec10Params = rtc10.getCreateParam(BaseBO.INVALID_CASE_ID, rtc10);
		RetailTradeCommodity rtc10Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec10Params);
		rtclist.add(rtc10Create);

		Assert.assertTrue(rtc10Create != null && EnumErrorCode.values()[Integer.parseInt(rec10Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc10Create == null ? "providerCreate == null" : rtc10Create);

		// 将零售单和零售单商品set到CashReducingOfAmount
		rtCreate.setListSlave1(rtclist);
		doa.setRetailTrade(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false);
		for (BaseModel baseModel : readN) {
			Promotion promotion = (Promotion) baseModel;
			System.out.println("**********************////////////" + promotion);
		}

		rtCreate.setAmount(doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test));
		// doa.prePromote(Shared.DBName_Test);
		// 调用CashReducingOfAmount.promote方法去计算促销
		System.out.println("-----------------------------商品原价-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--商品数量：" + rtc.getNO() + "--退货价：" + rtc.getPriceReturn());
		}
		doa.promote(rtCreate, EnumPromotionScope.EPS_AllCommodities, CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false), Shared.DBName_Test);
		System.out.println("-----------------------------确认支付价格后-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--商品数量：" + rtc.getNO() + "--退货价：" + rtc.getPriceReturn());
		}
		RetailTrade rtNew = doa.getRetailTrade();
		System.out.println(rtNew.toString());
	}

	@Test
	public void promoteNewTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case3：没有活动------------------------------------");

		// 创建一个零售单
		List<RetailTradeCommodity> rtclist = new ArrayList<RetailTradeCommodity>();

		Random ran = new Random();
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setLocalSN(ran.nextInt(1000));
		Thread.sleep(1);
		rt.setPos_ID(ran.nextInt(5) + 1);
		rt.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSaleDatetime(new Date());
		rt.setStaffID(ran.nextInt(5) + 1);
		rt.setPaymentType(ran.nextInt(5) + 1);
		rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		rt.setSourceID(BaseAction.INVALID_ID);
		rt.setAmount(1540);

		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);

		// -------------------------------------------------
		RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
		rtc2.setTradeID(rtCreate.getID());
		rtc2.setCommodityID(4);
		rtc2.setPriceOriginal(168);
		rtc2.setBarcodeID(1);
		rtc2.setNO(2);

		Map<String, Object> rec2Params = rtc2.getCreateParam(BaseBO.INVALID_CASE_ID, rtc2);
		RetailTradeCommodity rtc2Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec2Params);
		rtclist.add(rtc2Create);

		Assert.assertTrue(rtc2Create != null && EnumErrorCode.values()[Integer.parseInt(rec2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc2Create == null ? "providerCreate == null" : rtc2Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc3 = DataInput.getRetailTradeCommodity();
		rtc3.setTradeID(rtCreate.getID());
		rtc3.setCommodityID(5);
		rtc3.setPriceOriginal(158);
		rtc3.setBarcodeID(1);
		rtc3.setNO(5);

		Map<String, Object> rec3Params = rtc3.getCreateParam(BaseBO.INVALID_CASE_ID, rtc3);
		RetailTradeCommodity rtc3Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec3Params);
		rtclist.add(rtc3Create);

		Assert.assertTrue(rtc3Create != null && EnumErrorCode.values()[Integer.parseInt(rec3Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc3Create == null ? "providerCreate == null" : rtc3Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc4 = DataInput.getRetailTradeCommodity();
		rtc4.setTradeID(rtCreate.getID());
		rtc4.setCommodityID(6);
		rtc4.setPriceOriginal(178);
		rtc4.setBarcodeID(1);

		Map<String, Object> rec4Params = rtc4.getCreateParam(BaseBO.INVALID_CASE_ID, rtc4);
		RetailTradeCommodity rtc4Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec4Params);
		rtclist.add(rtc4Create);

		Assert.assertTrue(rtc4Create != null && EnumErrorCode.values()[Integer.parseInt(rec4Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc4Create == null ? "providerCreate == null" : rtc4Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc5 = DataInput.getRetailTradeCommodity();
		rtc5.setTradeID(rtCreate.getID());
		rtc5.setCommodityID(7);
		rtc5.setPriceOriginal(98);
		rtc5.setBarcodeID(1);

		Map<String, Object> rec5Params = rtc5.getCreateParam(BaseBO.INVALID_CASE_ID, rtc5);
		RetailTradeCommodity rtc5Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec5Params);
		rtclist.add(rtc5Create);

		Assert.assertTrue(rtc5Create != null && EnumErrorCode.values()[Integer.parseInt(rec5Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc5Create == null ? "providerCreate == null" : rtc5Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc6 = DataInput.getRetailTradeCommodity();
		rtc6.setTradeID(rtCreate.getID());
		rtc6.setCommodityID(8);
		rtc6.setPriceOriginal(138);
		rtc6.setBarcodeID(1);
		rtc6.setNO(8);

		Map<String, Object> rec6Params = rtc6.getCreateParam(BaseBO.INVALID_CASE_ID, rtc6);
		RetailTradeCommodity rtc6Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec6Params);
		rtclist.add(rtc6Create);

		Assert.assertTrue(rtc6Create != null && EnumErrorCode.values()[Integer.parseInt(rec6Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc6Create == null ? "providerCreate == null" : rtc6Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc7 = DataInput.getRetailTradeCommodity();
		rtc7.setTradeID(rtCreate.getID());
		rtc7.setCommodityID(9);
		rtc7.setPriceOriginal(178);
		rtc7.setBarcodeID(1);

		Map<String, Object> rec7Params = rtc7.getCreateParam(BaseBO.INVALID_CASE_ID, rtc7);
		RetailTradeCommodity rtc7Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec7Params);
		rtclist.add(rtc7Create);

		Assert.assertTrue(rtc7Create != null && EnumErrorCode.values()[Integer.parseInt(rec7Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc7Create == null ? "providerCreate == null" : rtc7Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc8 = DataInput.getRetailTradeCommodity();
		rtc8.setTradeID(rtCreate.getID());
		rtc8.setCommodityID(10);
		rtc8.setPriceOriginal(168);
		rtc8.setBarcodeID(1);

		Map<String, Object> rec8Params = rtc8.getCreateParam(BaseBO.INVALID_CASE_ID, rtc8);
		RetailTradeCommodity rtc8Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec8Params);
		rtclist.add(rtc8Create);

		Assert.assertTrue(rtc8Create != null && EnumErrorCode.values()[Integer.parseInt(rec8Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc8Create == null ? "providerCreate == null" : rtc8Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc9 = DataInput.getRetailTradeCommodity();
		rtc9.setTradeID(rtCreate.getID());
		rtc9.setCommodityID(11);
		rtc9.setPriceOriginal(188);
		rtc9.setBarcodeID(1);

		Map<String, Object> rec9Params = rtc9.getCreateParam(BaseBO.INVALID_CASE_ID, rtc9);
		RetailTradeCommodity rtc9Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec9Params);
		rtclist.add(rtc9Create);

		Assert.assertTrue(rtc9Create != null && EnumErrorCode.values()[Integer.parseInt(rec9Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc9Create == null ? "providerCreate == null" : rtc9Create);
		// -------------------------------------------------
		RetailTradeCommodity rtc10 = DataInput.getRetailTradeCommodity();
		rtc10.setTradeID(rtCreate.getID());
		rtc10.setCommodityID(12);
		rtc10.setPriceOriginal(128);
		rtc10.setBarcodeID(1);

		Map<String, Object> rec10Params = rtc10.getCreateParam(BaseBO.INVALID_CASE_ID, rtc10);
		RetailTradeCommodity rtc10Create = (RetailTradeCommodity) retailTradeCommodityMapper.create(rec10Params);
		rtclist.add(rtc10Create);

		Assert.assertTrue(rtc10Create != null && EnumErrorCode.values()[Integer.parseInt(rec10Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc10Create == null ? "providerCreate == null" : rtc10Create);

		// 将零售单和零售单商品set到CashReducingOfAmount
		rtCreate.setListSlave1(rtclist);
		doa.setRetailTrade(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false);
		for (BaseModel baseModel : readN) {
			Promotion promotion = (Promotion) baseModel;
			System.out.println("**********************////////////" + promotion);
		}
		doa.setPromoted(false);

		rtCreate.setAmount(doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test));
		// doa.prePromote(Shared.DBName_Test);
		// 调用CashReducingOfAmount.promote方法去计算促销
		System.out.println("-----------------------------商品原价-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--商品数量：" + rtc.getNO() + "--退货价：" + rtc.getPriceReturn());
		}
		doa.promote(rtCreate, EnumPromotionScope.EPS_SpecifiedCommodities, CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false), Shared.DBName_Test);
		System.out.println("-----------------------------确认支付价格后-----------------------------------------------------------------------------------------------------------");
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			System.out.println("此单原价：" + doa.getOriginalTotalAmount(rtCreate, Shared.DBName_Test) + "--实际支付价格：" + rtCreate.getAmount() + "--商品原价：" + rtc.getPriceOriginal() + "--商品数量：" + rtc.getNO() + "--退货价：" + rtc.getPriceReturn());
		}
		RetailTrade rtNew = doa.getRetailTrade();
		System.out.println(rtNew.toString());
	}
}
