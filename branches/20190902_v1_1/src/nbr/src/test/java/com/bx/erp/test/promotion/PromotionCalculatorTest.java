package com.bx.erp.test.promotion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.PromotionCalculator;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.RetailTradeCommodityMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.GeneralUtil;

@WebAppConfiguration
public class PromotionCalculatorTest extends BaseMapperTest {

	@Resource
	private PromotionCalculator promotionCalculator;
	private HttpSession session;
	private Staff staff;
	private MockMvc mvc;
	@Resource
	private WebApplicationContext wac;

	List<RetailTradeCommodity> rtclist;
	

	public static class DataInput {
		private static RetailTradeCommodity rtcInput = null;

		protected static final RetailTradeCommodity getRetailTradeCommodity() throws CloneNotSupportedException, InterruptedException {
			rtcInput = new RetailTradeCommodity();
			rtcInput.setNO(1);
			// rtcInput.setIsGift(0);
			rtcInput.setOperatorStaffID(STAFF_ID1);

			return (RetailTradeCommodity) rtcInput.clone();
		}

	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
		try {
			CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
			encodingFilter.setEncoding("utf-8");
			encodingFilter.setForceEncoding(true);
			mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
			session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
			staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();

		Shared.printTestClassEndInfo();
	}

	@Test
	public void promoteNewTest() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case1：三个活动都为指定满折------------------------------------");
		// 创建一个折扣满减活动A, 放到缓存里
		Promotion promotionA = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(1);
		promotionA.setStatus(0);
		promotionA.setName("指定商品满10打9折");
		promotionA.setExcecutionThreshold(10);
		promotionA.setExcecutionDiscount(0.9);

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
		Promotion promotionB = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(1);
		promotionB.setStatus(0);
		promotionB.setName("指定商品满100打8折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8);

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
		Promotion promotionC = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionC.setID(3);
		promotionC.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionC.setScope(1);
		promotionC.setStatus(0);
		promotionC.setName("指定商品满500打6折");
		promotionC.setExcecutionThreshold(500);
		promotionC.setExcecutionAmount(1000);
		promotionC.setExcecutionDiscount(0.6);

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
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false,false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		// 由于所有原价都乘以0.6的优惠，所有要还原不打优惠的两个商品的价格
		double specifiedDiscountPrice = GeneralUtil.sumN(GeneralUtil.mul(retailTradeTotal, 0.6), GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 1).getPriceOriginal(), rtclist.get(rtclist.size() - 1).getNO()), 0.4),
				GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 2).getPriceOriginal(), rtclist.get(rtclist.size() - 2).getNO()), 0.4));
		Assert.assertTrue(Math.abs(GeneralUtil.sub(rTrade.getAmount(), specifiedDiscountPrice)) < BaseModel.TOLERANCE);
	}

	@Test
	public void promoteNewTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case2：三个指定满折 两个指定满减 ------------------------------------");
		// 创建一个折扣满减活动A, 放到缓存里
		Promotion promotionA = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(1);
		promotionA.setStatus(0);
		promotionA.setName("指定商品满10打9折");
		promotionA.setExcecutionThreshold(10);
		promotionA.setExcecutionDiscount(0.9);

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
		Promotion promotionB = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(1);
		promotionB.setStatus(0);
		promotionB.setName("指定商品满100打8折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8);

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
		Promotion promotionC = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionC.setID(3);
		promotionC.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionC.setScope(1);
		promotionC.setStatus(0);
		promotionC.setName("指定商品满500打6折");
		promotionC.setExcecutionThreshold(500);
		promotionC.setExcecutionAmount(1000);
		promotionC.setExcecutionDiscount(0.6);

		List<PromotionScope> piList3 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(3);
			piList3.add(promotionScope);
		}
		promotionC.setListSlave1(piList3);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionC, Shared.DBName_Test, staff.getID());
		// 创建一个满减活动D
		Promotion promotionD = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionD.setID(4);
		promotionD.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionD.setScope(1);
		promotionD.setStatus(0);
		promotionD.setName("指定商品满100-50");
		promotionD.setExcecutionThreshold(100);
		promotionD.setExcecutionAmount(50);
		promotionD.setExcecutionDiscount(0.8);

		List<PromotionScope> piList4 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 8; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(4);
			piList4.add(promotionScope);
		}
		promotionD.setListSlave1(piList4);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionD, Shared.DBName_Test, staff.getID());
		// 创建一个满减活动E
		Promotion promotionE = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionE.setID(5);
		promotionE.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionE.setScope(1);
		promotionE.setStatus(0);
		promotionE.setName("指定商品满500-456");
		promotionE.setExcecutionThreshold(500);
		promotionE.setExcecutionAmount(456);
		promotionE.setExcecutionDiscount(0.6);

		List<PromotionScope> piList5 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(5);
			piList5.add(promotionScope);
		}
		promotionE.setListSlave1(piList5);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionE, Shared.DBName_Test, staff.getID());
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		// 从缓存中拿取所有的活动
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false,false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		// 由于所有原价都乘以0.6的优惠，所有要还原不打优惠的两个商品的价格
		double specifiedDiscountPrice = GeneralUtil.sumN(GeneralUtil.mul(retailTradeTotal, 0.6), GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 1).getPriceOriginal(), rtclist.get(rtclist.size() - 1).getNO()), 0.4),
				GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 2).getPriceOriginal(), rtclist.get(rtclist.size() - 2).getNO()), 0.4));
		Assert.assertTrue(Math.abs(GeneralUtil.sub(rTrade.getAmount(), specifiedDiscountPrice)) < BaseModel.TOLERANCE);
	}

	@Test
	public void promoteNewTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case3：三个指定满折 两个指定满减 两个全场满减 两个全场满折 ------------------------------------");
		// 创建一个折扣满减活动A, 放到缓存里
		Promotion promotionA = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(1);
		promotionA.setStatus(0);
		promotionA.setName("指定商品满10打9折");
		promotionA.setExcecutionThreshold(10);
		promotionA.setExcecutionDiscount(0.9);

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
		Promotion promotionB = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(1);
		promotionB.setStatus(0);
		promotionB.setName("指定商品满100打8折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8);

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
		Promotion promotionC = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionC.setID(3);
		promotionC.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionC.setScope(1);
		promotionC.setStatus(0);
		promotionC.setName("指定商品满500打6折");
		promotionC.setExcecutionThreshold(500);
		promotionC.setExcecutionAmount(1000);
		promotionC.setExcecutionDiscount(0.6);

		List<PromotionScope> piList3 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(3);
			piList3.add(promotionScope);
		}
		promotionC.setListSlave1(piList3);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionC, Shared.DBName_Test, staff.getID());
		// 创建一个满减活动D
		Promotion promotionD = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionD.setID(4);
		promotionD.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionD.setScope(1);
		promotionD.setStatus(0);
		promotionD.setName("指定商品满100-50");
		promotionD.setExcecutionThreshold(100);
		promotionD.setExcecutionAmount(50);
		promotionD.setExcecutionDiscount(0.8);

		List<PromotionScope> piList4 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 8; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(4);
			piList4.add(promotionScope);
		}
		promotionD.setListSlave1(piList4);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionD, Shared.DBName_Test, staff.getID());
		// 创建一个满减活动E
		Promotion promotionE = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionE.setID(5);
		promotionE.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionE.setScope(1);
		promotionE.setStatus(0);
		promotionE.setName("指定商品满500-456");
		promotionE.setExcecutionThreshold(500);
		promotionE.setExcecutionAmount(456);
		promotionE.setExcecutionDiscount(0.6);

		List<PromotionScope> piList5 = new ArrayList<PromotionScope>();
		for (int i = 1; i <= 10; i++) {
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setCommodityID(i);
			promotionScope.setPromotionID(5);
			piList5.add(promotionScope);
		}
		promotionE.setListSlave1(piList5);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionE, Shared.DBName_Test, staff.getID());
		// 创建一个全场满减活动F
		Promotion promotionF = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionF.setID(6);
		promotionF.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionF.setScope(0);
		promotionF.setStatus(0);
		promotionF.setName("全场商品满1000-500");
		promotionF.setExcecutionThreshold(1000);
		promotionF.setExcecutionAmount(500);
		promotionF.setExcecutionDiscount(0.6);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionF, Shared.DBName_Test, staff.getID());

		// 创建一个全场满减活动G
		Promotion promotionG = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionG.setID(7);
		promotionG.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		promotionG.setScope(0);
		promotionG.setStatus(0);
		promotionG.setName("全场商品满2000-1050");
		promotionG.setExcecutionThreshold(2000);
		promotionG.setExcecutionAmount(1050);
		promotionG.setExcecutionDiscount(0.6);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionG, Shared.DBName_Test, staff.getID());

		// 创建一个全场满折活动H
		Promotion promotionH = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionH.setID(8);
		promotionH.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionH.setScope(0);
		promotionH.setStatus(0);
		promotionH.setName("全场商品满1000打9折");
		promotionH.setExcecutionThreshold(1000);
		promotionH.setExcecutionAmount(1050);
		promotionH.setExcecutionDiscount(0.9);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionH, Shared.DBName_Test, staff.getID());

		// 创建一个全场满折活动I
		Promotion promotionI = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionI.setID(9);
		promotionI.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionI.setScope(0);
		promotionI.setStatus(0);
		promotionI.setName("全场商品满2000打85折");
		promotionI.setExcecutionThreshold(2000);
		promotionI.setExcecutionAmount(1050);
		promotionI.setExcecutionDiscount(0.85d);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionI, Shared.DBName_Test, staff.getID());
		// 创建零售单 和零售单的商品
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		// 从缓存中拿取所有的活动
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false,false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		// 由于所有原价都乘以0.6的优惠，所有要还原不打优惠的两个商品的价格
		double specifiedDiscountPrice = GeneralUtil.sumN(GeneralUtil.mul(retailTradeTotal, 0.6), GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 1).getPriceOriginal(), rtclist.get(rtclist.size() - 1).getNO()), 0.4),
				GeneralUtil.mul(GeneralUtil.mul(rtclist.get(rtclist.size() - 2).getPriceOriginal(), rtclist.get(rtclist.size() - 2).getNO()), 0.4));
		double specifiedAfterOverallDiscountPrice = GeneralUtil.sub(specifiedDiscountPrice, 1050);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(rTrade.getAmount(), specifiedAfterOverallDiscountPrice)) < BaseModel.TOLERANCE);
	}

	@Test
	public void promoteNewTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case4：没有活动------------------------------------");
		// 创建一个零售单
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false,false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(rTrade.getAmount(), retailTradeTotal) < BaseModel.TOLERANCE);
	}

	@Test
	public void promoteNewTest5() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case5：有一个全场满折活动------------------------------------");
		Promotion promotionA = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(0);
		promotionA.setStatus(0);
		promotionA.setName("全场满2000打8折");
		promotionA.setExcecutionThreshold(2000);
		promotionA.setExcecutionDiscount(0.8);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionA, Shared.DBName_Test, staff.getID());
		// 创建一个零售单
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false,false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		// Assert.assertTrue(Float.compare(rTrade.getAmount(), 4398.4f) <
		// BaseModel.TOLERANCE);
		// Assert.assertTrue(GeneralUtil.sub(rTrade.getAmount(), 5964.7998046875d) ==
		// 0);
		Assert.assertTrue(GeneralUtil.sub(rTrade.getAmount(), GeneralUtil.mul(retailTradeTotal, 0.8)) < BaseModel.TOLERANCE);
	}

	@Test
	public void promoteNewTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存
		System.out.println("-------------------------------Case6：有一个全场还有一个过期的活动满折活动------------------------------------");
		Promotion promotionA = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionA.setID(1);
		promotionA.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionA.setScope(0);
		promotionA.setStatus(0);
		promotionA.setName("全场满2000打8折");
		promotionA.setExcecutionThreshold(2000);
		promotionA.setExcecutionDiscount(0.8);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionA, Shared.DBName_Test, staff.getID());
		// 创建过期活动B
		Promotion promotionB = BasePromotionTest.DataInput.getPromotionInSpecialTime();
		promotionB.setID(2);
		promotionB.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		promotionB.setScope(0);
		promotionB.setStatus(0);
		promotionB.setName("指定商品满100打5折");
		promotionB.setExcecutionThreshold(100);
		promotionB.setExcecutionAmount(4);
		promotionB.setExcecutionDiscount(0.8);
		String dataStart = "2008-08-08";
		String dataEnd = "2010-08-08";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = simpleDateFormat.parse(dataStart);
		Date parse2 = simpleDateFormat.parse(dataEnd);
		promotionB.setDatetimeStart(parse);
		promotionB.setDatetimeEnd(parse2);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotionB, Shared.DBName_Test, staff.getID());
		// 创建一个零售单
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTradeViaMapper();
		double retailTradeTotal = createRetailTradeCommodity(rtCreate);
		List<BaseModel> readN = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false);
		RetailTrade rTrade = promotionCalculator.calculateBestCase(rtCreate, readN, Shared.DBName_Test);
		// Assert.assertTrue(Float.compare(rTrade.getAmount(), 4398.4f) <
		// BaseModel.TOLERANCE);
		// Assert.assertTrue(GeneralUtil.sub(rTrade.getAmount(), 5964.7998046875d) ==
		// 0);
		Assert.assertTrue(GeneralUtil.sub(rTrade.getAmount(), GeneralUtil.mul(retailTradeTotal, 0.8)) < BaseModel.TOLERANCE);
	}

	private double createRetailTradeCommodity(RetailTrade rtCreate) throws Exception {
		RetailTradeCommodityMapper rtcMapper = (RetailTradeCommodityMapper) applicationContext.getBean("retailTradeCommodityMapper");
		rtclist = new ArrayList<RetailTradeCommodity>(); // 初始化，防止多个测试一起运行会干扰到
		RetailTradeCommodity rtc2 = DataInput.getRetailTradeCommodity();
		rtc2.setTradeID(rtCreate.getID());
		rtc2.setCommodityID(4);
		rtc2.setPriceOriginal(168);
		rtc2.setBarcodeID(1);
		rtc2.setNO(5);

		Map<String, Object> rec2Params = rtc2.getCreateParam(BaseBO.INVALID_CASE_ID, rtc2);
		RetailTradeCommodity rtc2Create = (RetailTradeCommodity) rtcMapper.create(rec2Params);
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
		RetailTradeCommodity rtc3Create = (RetailTradeCommodity) rtcMapper.create(rec3Params);
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
		RetailTradeCommodity rtc4Create = (RetailTradeCommodity) rtcMapper.create(rec4Params);
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
		RetailTradeCommodity rtc5Create = (RetailTradeCommodity) rtcMapper.create(rec5Params);
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
		RetailTradeCommodity rtc6Create = (RetailTradeCommodity) rtcMapper.create(rec6Params);
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
		RetailTradeCommodity rtc7Create = (RetailTradeCommodity) rtcMapper.create(rec7Params);
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
		RetailTradeCommodity rtc8Create = (RetailTradeCommodity) rtcMapper.create(rec8Params);
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
		RetailTradeCommodity rtc9Create = (RetailTradeCommodity) rtcMapper.create(rec9Params);
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
		RetailTradeCommodity rtc10Create = (RetailTradeCommodity) rtcMapper.create(rec10Params);
		rtclist.add(rtc10Create);

		Assert.assertTrue(rtc10Create != null && EnumErrorCode.values()[Integer.parseInt(rec10Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println(rtc10Create == null ? "providerCreate == null" : rtc10Create);
		rtCreate.setListSlave1(rtclist);

		double dLocalBalance = 0.000000d; // 指定商品的售价总和
		for (Object o : rtCreate.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			dLocalBalance = GeneralUtil.sum(dLocalBalance, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
		}
		rtCreate.setAmount(dLocalBalance);
		rtCreate.setAmount1(dLocalBalance);
		return dLocalBalance;
	}

}
