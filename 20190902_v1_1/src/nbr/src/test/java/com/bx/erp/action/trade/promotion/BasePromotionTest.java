package com.bx.erp.action.trade.promotion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.trade.promotion.PromotionCache;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class BasePromotionTest extends BaseTestNGSpringContextTest {

	CashReducingOfAmount coa = new CashReducingOfAmount();
	protected static HttpSession session;
	private Staff staff;
	@Resource
	private WebApplicationContext wac;

	protected MockMvc mvc;

	@Resource
	PromotionCache pp;

	public static class DataInput {
		private static Promotion promotion = null;
		private static PromotionScope promotionInfo = null;

		protected static final Promotion getPromotion() throws CloneNotSupportedException, InterruptedException {
			promotion = new Promotion();
			// promotion.setPromotionTypeID(11);
			// promotion.setProgramName("减现");
			// promotion.setConsumerType("会员");
			// promotion.setCreator(1);
			// promotion.setApprover(1);
			// promotion.setApproveDatetime(new Date());
			// promotion.setRemark("...........................");
			// promotion.setDiscountUponVIP(1);
			// promotion.setSpecialOfferInvolved(1);
			// promotion.setTimes(1.2f);
			// promotion.setVIPBornDayOrMonth(1);
			// promotion.setWholeShop(1);
			// promotion.setNOReached(3);
			// promotion.setAmountReached(30);
			// promotion.setCashExcluded(5);
			// promotion.setWholeTradeDiscount(1);
			// promotion.setDiscount(0.9f);
			// promotion.setNOComputationInvolved(1);
			// promotion.setGiftAll(1);
			// promotion.setNODoubled(1);
			// promotion.setAmountComputationInvolved(1);
			// promotion.setDiscountInvolvedForSpecialOffer(1);
			return (Promotion) promotion.clone();
		}

		public static final PromotionScope getPromotioninfo() throws CloneNotSupportedException, InterruptedException {
			promotionInfo = new PromotionScope();
			promotionInfo.setPromotionID(1);
			promotionInfo.setCommodityID(1);
			// promotionInfo.setNO(new Random().nextInt(200) + 1);
			// promotionInfo.setCategoryID(1);
			// promotionInfo.setBrandID(1);
			// promotionInfo.setSpecialOffer(12.5f);
			// promotionInfo.setMaxNOForVIPDaily(10);
			// promotionInfo.setMaxNOForVIPMonthly(100);
			// promotionInfo.setMaxNOPerTrade(10);
			// promotionInfo.setMaxNOForAllShops(10);
			// promotionInfo.setMinNOPerTrade(10);
			// promotionInfo.setDiscount(0.9f);
			return (PromotionScope) promotionInfo.clone();
		}

		// 给PromotionListTest使用

		// getPromotionListTest使用

		// 给PromotionListTest使用
		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Promotion p, String datetimeStart, String datetimeEnd,HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url)//
					.contentType(contentType)//
					.session((MockHttpSession) session)//
					.param(Promotion.field.getFIELD_NAME_name(), p.getName())//
					.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p.getStatus()))//
					.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p.getType()))//
					.param(Promotion.field.getFIELD_NAME_datetimeStart(), datetimeStart)//
					.param(Promotion.field.getFIELD_NAME_datetimeEnd(), datetimeEnd)//
					.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p.getExcecutionThreshold()))//
					.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p.getExcecutionAmount()))//
					.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p.getExcecutionDiscount()))//
					.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p.getScope()))//
					.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p.getStaff()))//
					.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p.getReturnObject()))//
					.param(Promotion.field.getFIELD_NAME_commodityIDs(), p.getCommodityIDs())//
			;

			return builder;
		}
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		try {
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

	// 20180701是星期日
	//
	final int YEAR = 2018;
	final int MONTH = 6; // Not 7
	final int DAY = 15;
	final int HOUR = 12;
	final int MINUTE = 30;
	final int SECOND = 30;

	@Test
	public void testStringBuilder() {
		Shared.printTestMethodStartInfo();

		StringBuilder sbTableInP = new StringBuilder();
		sbTableInP.append("xxxxxxxxxxxx");
		sbTableInP.setLength(0);
		final String s = "wwww";
		sbTableInP.append(s);
		// System.out.println(sbTableInP.toString());

		Assert.assertEquals(sbTableInP.length(), s.length());
	}

	@Test
	public void testDevideDouble() {
		Shared.printTestMethodStartInfo();

		double d = 0.000000d;
		double d2 = 23 / d;
		d = d2;

		Assert.assertTrue(true);
	}

	/*
	 * case1：创建一个减现， 数据库没有折扣，减现，可以直接添加 case2：创建一个减现， 数据库有折扣或减现，查询日期星期时段是否重合
	 * case3：创建一个折扣， 数据库没有折扣，减现，可以直接添加 case4：创建一个折扣， 数据库有折扣或减现，查询日期星期时段是否重合
	 * case5：有折扣或减现 查询日期 日期不重合 可以添加 case6：有折扣或减现 查询日期 日期重合 星期不重合 可以添加 case7：有折扣或减现
	 * 查询日期 日期重合 星期重合 时段不重合 可以添加 case8：有折扣或减现 查询日期 日期重合 星期重合 时段重合 不可以添加
	 */
	@Test
	public void runPromotion() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// case1：创建一个减现， 数据库没有折扣，减现，可以直接添加
		System.out.println("------------------ case1：创建一个减现， 数据库没有折扣，减现，可以直接添加 -----------------------");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存

		Calendar cal = Calendar.getInstance();

		cal.set(YEAR, MONTH - 2, DAY, HOUR, MINUTE, SECOND);
		Date startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		Date endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Date executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		Date executeEndTime = cal.getTime();

		Promotion promotion = DataInput.getPromotion();
		promotion.setID(1);
		// promotion.setStartDatetime(startDatetime);
		// promotion.setEndDatetime(endDatetime);
		// promotion.setExecuteWeekday(1);
		// promotion.setExecuteStartDatetime(executeStartTime);
		// promotion.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion, Shared.DBName_Test, staff.getID());// 将促销写入到缓存

		// case2：创建一个减现， 数据库有折扣或减现，查询日期星期时段是否重合
		System.out.println("------------------ case2：创建一个减现， 数据库有折扣或减现，查询日期星期时段是否重合 -----------------------");
		cal.set(YEAR, MONTH + 4, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 5, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion2 = DataInput.getPromotion();
		promotion2.setID(2);
		// promotion2.setStartDatetime(startDatetime);
		// promotion2.setEndDatetime(endDatetime);
		// promotion2.setExecuteWeekday(1);
		// promotion2.setExecuteStartDatetime(executeStartTime);
		// promotion2.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion, Shared.DBName_Test, staff.getID());// 将促销写入到缓存
		// case3：创建一个折扣， 数据库没有折扣，减现，可以直接添加
		System.out.println("------------------ case3：创建一个折扣， 数据库没有折扣，减现，可以直接添加 -----------------------");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存

		cal.set(YEAR, MONTH - 2, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion3 = DataInput.getPromotion();
		promotion3.setID(3);
		// promotion3.setPromotionTypeID(10);
		// promotion3.setProgramName("折扣");
		// promotion3.setStartDatetime(startDatetime);
		// promotion3.setEndDatetime(endDatetime);
		// promotion3.setExecuteWeekday(1);
		// promotion3.setExecuteStartDatetime(executeStartTime);
		// promotion3.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion3, Shared.DBName_Test, staff.getID());// 将促销写入到缓存
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));

		// case4：创建一个折扣， 数据库有折扣或减现，查询日期星期时段是否重合
		System.out.println("------------------ case4：创建一个折扣， 数据库有折扣或减现，查询日期星期时段是否重合 -----------------------");
		cal.set(YEAR, MONTH + 4, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 5, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion4 = DataInput.getPromotion();
		promotion4.setID(4);
		// promotion4.setPromotionTypeID(10);
		// promotion4.setProgramName("折扣");
		// promotion4.setStartDatetime(startDatetime);
		// promotion4.setEndDatetime(endDatetime);
		// promotion4.setExecuteWeekday(1);
		// promotion4.setExecuteStartDatetime(executeStartTime);
		// promotion4.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion4, Shared.DBName_Test, staff.getID());// 将促销写入到缓存
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));

		// case5：有折扣或减现 查询日期 日期不重合 可以添加
		System.out.println("------------------ case5：有折扣或减现 查询日期 日期不重合 可以添加 -----------------------");
		cal.set(YEAR, MONTH - 4, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH - 3, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion5 = DataInput.getPromotion();
		promotion5.setID(5);
		// promotion5.setStartDatetime(startDatetime);
		// promotion5.setEndDatetime(endDatetime);
		// promotion5.setExecuteWeekday(1);
		// promotion5.setExecuteStartDatetime(executeStartTime);
		// promotion5.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion5, Shared.DBName_Test, staff.getID());// 将促销写入到缓存
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));

		// case6：有折扣或减现 查询日期 日期重合 星期不重合 可以添加
		System.out.println("------------------ case6：有折扣或减现 查询日期 日期重合 星期不重合 可以添加 -----------------------");
		cal.set(YEAR, MONTH - 4, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH - 3, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion6 = DataInput.getPromotion();
		promotion6.setID(6);
		// promotion6.setStartDatetime(startDatetime);
		// promotion6.setEndDatetime(endDatetime);
		// promotion6.setExecuteWeekday(2);
		// promotion6.setExecuteStartDatetime(executeStartTime);
		// promotion6.setExecuteEndDatetime(executeEndTime);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion6, Shared.DBName_Test, staff.getID());// 将促销写入到缓存
		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));

		// case7：有折扣或减现 查询日期 日期重合 星期重合 时段不重合 可以添加
		System.out.println("------------------ case7：有折扣或减现 查询日期 日期重合 星期重合 时段不重合 可以添加 -----------------------");
		cal.set(YEAR, MONTH - 2, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR - 4, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR - 3, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Promotion promotion7 = DataInput.getPromotion();
		promotion7.setID(7);
		// promotion7.setStartDatetime(startDatetime);
		// promotion7.setEndDatetime(endDatetime);
		// promotion7.setExecuteWeekday(1);
		// promotion7.setExecuteStartDatetime(executeStartTime);
		// promotion7.setExecuteEndDatetime(executeEndTime);

		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));

		// case8：有折扣或减现 查询日期 日期重合 星期重合 时段重合 不可以添加
		System.out.println("------------------ case8：有折扣或减现 查询日期 日期重合 星期重合 时段重合 不可以添加 -----------------------");

		Promotion promotion8 = DataInput.getPromotion();
		promotion8.setID(8);
		// promotion8.setStartDatetime(startDatetime);
		// promotion8.setEndDatetime(endDatetime);
		// promotion8.setExecuteWeekday(1);
		// promotion8.setExecuteStartDatetime(executeStartTime);
		// promotion8.setExecuteEndDatetime(executeEndTime);

		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).readN(false, false));
	}

	/*
	 * Case1：当前(缓存和DB里)没有任何促销活动，允许创建针对商品I的特价促销活动A
	 * Case2：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动B。B的日期跟A的日期无交集，允许创建。
	 * Case3：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动C。C的日期跟A的日期有交集，但星期无交集，允许创建。
	 * Case4：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动D。D的日期跟A的日期有交集，星期有交集，但时段无交集，
	 * 允许创建。
	 * Case5：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动E。E的日期跟A的日期有交集，星期有交集，时段有交集，
	 * 不允许创建。
	 * 
	 * Case6： 当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品II的特价促销活动F。允许创建。 Case7：
	 * 当前(缓存和DB里)已有针对商品I的特价促销活动A和针对商品II的特价促销活动G，欲创建针对商品II的特价促销活动H。H的日期跟G的日期有交集，星期有交集
	 * ，时段有交集，不允许创建H。 Case8：
	 * 当前(缓存和DB里)已有针对商品I的特价促销活动A和针对商品II的特价促销活动G，欲创建针对商品II的特价促销活动H。H的日期跟G的日期有交集，星期有交集
	 * ，时段没有交集，允许创建H。
	 * 
	 * Case9：当前(缓存和DB里)已有针对商品I和商品II的特价促销活动A，欲创建针对商品II的特价促销活动I。I的日期跟A的日期无交集，允许创建。
	 * Case10：当前(缓存和DB里)已有针对商品I和商品II的特价促销活动A，欲创建针对商品II的特价促销活动J。J的日期跟A的日期有交集，星期有交集，
	 * 时段有交集，不允许创建。
	 */
	@Test
	public void runPromotionSpecial() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("------------------ Case1：当前(缓存和DB里)没有任何促销活动，允许创建针对商品I的特价促销活动A	 -----------------------");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).deleteAll();// 清空缓存

		Calendar cal = Calendar.getInstance();

		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Date startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		Date endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Date executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		Date executeEndTime = cal.getTime();

		List<PromotionScope> piList = new ArrayList<PromotionScope>();

		Promotion promotion = DataInput.getPromotion();
		promotion.setID(1);
		// promotion.setPromotionTypeID(1);
		// promotion.setProgramName("特价1");
		// promotion.setStartDatetime(startDatetime);
		// promotion.setEndDatetime(endDatetime);
		// promotion.setExecuteWeekday(1);
		// promotion.setExecuteStartDatetime(executeStartTime);
		// promotion.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo = DataInput.getPromotioninfo();
		promotionInfo.setID(1);

		piList.add(promotionInfo);

		promotion.setListSlave1(piList);
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(1, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion, Shared.DBName_Test, staff.getID());// 将促销写入到缓存

		System.out.println("------------------ Case2: 当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动B。B的日期跟A的日期无交集，允许创建。 -----------------------");
		cal.set(YEAR, MONTH + 3, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 4, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList2 = new ArrayList<PromotionScope>();
		Promotion promotion2 = DataInput.getPromotion();
		promotion2.setID(2);
		// promotion2.setPromotionTypeID(1);
		// promotion2.setProgramName("特价2");
		// promotion2.setStartDatetime(startDatetime);
		// promotion2.setEndDatetime(endDatetime);
		// promotion2.setExecuteWeekday(2);
		// promotion2.setExecuteStartDatetime(executeStartTime);
		// promotion2.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo2 = DataInput.getPromotioninfo();
		promotionInfo2.setID(2);

		piList2.add(promotionInfo2);

		promotion2.setListSlave1(piList2);

		System.out.println("------------------ Case3：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动C。C的日期跟A的日期有交集，但星期无交集，允许创建。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 3, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList3 = new ArrayList<PromotionScope>();
		Promotion promotion3 = DataInput.getPromotion();
		promotion3.setID(3);
		// promotion3.setPromotionTypeID(1);
		// promotion3.setProgramName("特价3");
		// promotion3.setStartDatetime(startDatetime);
		// promotion3.setEndDatetime(endDatetime);
		// promotion3.setExecuteWeekday(2);
		// promotion3.setExecuteStartDatetime(executeStartTime);
		// promotion3.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo3 = DataInput.getPromotioninfo();
		promotionInfo3.setID(2);

		piList3.add(promotionInfo3);

		promotion3.setListSlave1(piList3);

		System.out.println("------------------ Case4：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动D。D的日期跟A的日期有交集，星期有交集，但时段无交集，允许创建。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 3, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList4 = new ArrayList<PromotionScope>();
		Promotion promotion4 = DataInput.getPromotion();
		promotion4.setID(4);
		// promotion4.setPromotionTypeID(1);
		// promotion4.setProgramName("特价4");
		// promotion4.setStartDatetime(startDatetime);
		// promotion4.setEndDatetime(endDatetime);
		// promotion4.setExecuteWeekday(1);
		// promotion4.setExecuteStartDatetime(executeStartTime);
		// promotion4.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo4 = DataInput.getPromotioninfo();
		promotionInfo4.setID(4);

		piList4.add(promotionInfo4);

		promotion4.setListSlave1(piList4);

		System.out.println("------------------ Case5：当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品I的特价促销活动E。E的日期跟A的日期有交集，星期有交集，时段有交集，不允许创建。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList5 = new ArrayList<PromotionScope>();
		Promotion promotion5 = DataInput.getPromotion();
		promotion5.setID(5);
		// promotion5.setPromotionTypeID(1);
		// promotion5.setProgramName("特价5");
		// promotion5.setStartDatetime(startDatetime);
		// promotion5.setEndDatetime(endDatetime);
		// promotion5.setExecuteWeekday(1);
		// promotion5.setExecuteStartDatetime(executeStartTime);
		// promotion5.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo5 = DataInput.getPromotioninfo();
		promotionInfo5.setID(5);

		piList5.add(promotionInfo5);

		promotion5.setListSlave1(piList5);

		System.out.println("------------------ Case6： 当前(缓存和DB里)已有针对商品I的特价促销活动A，欲创建针对商品II的特价促销活动F。允许创建。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		Commodity commodity2 = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(2, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);

		List<PromotionScope> piList6 = new ArrayList<PromotionScope>();

		Promotion promotion6 = DataInput.getPromotion();
		promotion6.setID(6);
		// promotion6.setPromotionTypeID(1);
		// promotion6.setProgramName("特价6");
		// promotion6.setStartDatetime(startDatetime);
		// promotion6.setEndDatetime(endDatetime);
		// promotion6.setExecuteWeekday(1);
		// promotion6.setExecuteStartDatetime(executeStartTime);
		// promotion6.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo6 = DataInput.getPromotioninfo();
		promotionInfo6.setID(6);
		promotionInfo6.setCommodityID(commodity2.getID());
		piList6.add(promotionInfo6);

		promotion6.setListSlave1(piList6);

		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).write1(promotion6, Shared.DBName_Test, staff.getID());// 将促销写入到缓存

		System.out.println("------------------ Case7： 当前(缓存和DB里)已有针对商品I的特价促销活动A和针对商品II的特价促销活动G，欲创建针对商品II的特价促销活动H。H的日期跟G的日期有交集，星期有交集，时段有交集，不允许创建H。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList7 = new ArrayList<PromotionScope>();

		Promotion promotion7 = DataInput.getPromotion();
		promotion7.setID(7);
		// promotion7.setPromotionTypeID(1);
		// promotion7.setProgramName("特价7");
		// promotion7.setStartDatetime(startDatetime);
		// promotion7.setEndDatetime(endDatetime);
		// promotion7.setExecuteWeekday(1);
		// promotion7.setExecuteStartDatetime(executeStartTime);
		// promotion7.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo7 = DataInput.getPromotioninfo();
		promotionInfo7.setID(7);

		piList7.add(promotionInfo7);

		promotion7.setListSlave1(piList7);

		System.out.println("------------------ Case8： 当前(缓存和DB里)已有针对商品I的特价促销活动A和针对商品II的特价促销活动G，欲创建针对商品II的特价促销活动H。H的日期跟G的日期有交集，星期有交集，时段没有交集，允许创建H。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 3, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 4, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList8 = new ArrayList<PromotionScope>();

		Promotion promotion8 = DataInput.getPromotion();
		promotion8.setID(8);
		// promotion8.setPromotionTypeID(1);
		// promotion8.setProgramName("特价8");
		// promotion8.setStartDatetime(startDatetime);
		// promotion8.setEndDatetime(endDatetime);
		// promotion8.setExecuteWeekday(1);
		// promotion8.setExecuteStartDatetime(executeStartTime);
		// promotion8.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo8 = DataInput.getPromotioninfo();
		promotionInfo8.setID(8);
		promotionInfo8.setCommodityID(commodity2.getID());

		piList8.add(promotionInfo8);

		promotion8.setListSlave1(piList8);

		System.out.println("------------------ Case9：当前(缓存和DB里)已有针对商品I和商品II的特价促销活动A，欲创建针对商品II的特价促销活动I。I的日期跟A的日期无交集，允许创建。 -----------------------");
		cal.set(YEAR, MONTH + 3, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 4, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList9 = new ArrayList<PromotionScope>();

		Promotion promotion9 = DataInput.getPromotion();
		promotion9.setID(9);
		// promotion9.setPromotionTypeID(1);
		// promotion9.setProgramName("特价9");
		// promotion9.setStartDatetime(startDatetime);
		// promotion9.setEndDatetime(endDatetime);
		// promotion9.setExecuteWeekday(1);
		// promotion9.setExecuteStartDatetime(executeStartTime);
		// promotion9.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo9 = DataInput.getPromotioninfo();
		promotionInfo9.setID(9);
		promotionInfo9.setCommodityID(commodity2.getID());

		piList9.add(promotionInfo9);

		promotion9.setListSlave1(piList9);

		System.out.println("------------------ Case10：当前(缓存和DB里)已有针对商品I和商品II的特价促销活动A，欲创建针对商品II的特价促销活动J。J的日期跟A的日期有交集，星期有交集，时段有交集，不允许创建。 -----------------------");
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		startDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 2, DAY, HOUR, MINUTE, SECOND);
		endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		executeStartTime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		executeEndTime = cal.getTime();

		List<PromotionScope> piList10 = new ArrayList<PromotionScope>();

		Promotion promotion10 = DataInput.getPromotion();
		promotion10.setID(10);
		// promotion10.setPromotionTypeID(1);
		// promotion10.setProgramName("特价10");
		// promotion10.setStartDatetime(startDatetime);
		// promotion10.setEndDatetime(endDatetime);
		// promotion10.setExecuteWeekday(1);
		// promotion10.setExecuteStartDatetime(executeStartTime);
		// promotion10.setExecuteEndDatetime(executeEndTime);

		PromotionScope promotionInfo10 = DataInput.getPromotioninfo();
		promotionInfo10.setID(10);
		promotionInfo10.setCommodityID(commodity2.getID());

		piList10.add(promotionInfo10);

		promotion10.setListSlave1(piList10);

	}

}
