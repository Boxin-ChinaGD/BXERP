package com.bx.erp.test.paySIT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCouponCodeTest;
import com.bx.erp.test.BaseCouponTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseVipTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.paySIT.dataProvider.CommodityDataProvider;
import com.bx.erp.test.paySIT.dataProvider.CouponCodeDataProvider;
import com.bx.erp.test.paySIT.dataProvider.CouponDataProvider;
import com.bx.erp.test.paySIT.dataProvider.DBDataLoader;
import com.bx.erp.test.paySIT.dataProvider.PromotionDataProvider;
import com.bx.erp.test.paySIT.dataProvider.PurchasingOrderDataProvider;
import com.bx.erp.test.paySIT.dataProvider.RetailTradeDataProvider;
import com.bx.erp.test.paySIT.dataProvider.VipDataProvider;
import com.bx.erp.test.paySIT.dataProvider.WarehousingDataProvider;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

/** 本测试用于模拟支付流程的所有可能性。 <br />
 * 除非人工停止，否则会永远不停地运行，用于发现全部测试用例。 <br />
 * 做结果验证时，如果不通过，代表有新的测试用例不能通过测试，会自动通知RD <br />
 */
@WebAppConfiguration
public class PaySITTest extends BaseActionTest {
	protected List<BaseModel> packageUnitList;
	protected List<BaseModel> brandList;
	protected List<BaseModel> categoryList;
	protected List<BaseModel> providerList;
	protected List<BaseModel> simpleCommodityList;

	@Resource
	private RetailTradeDataProvider retailTradeDataProvider;

	@BeforeClass
	public void setup() {
		super.setUp();

		// ... 如果一直跑。即使将这个缓存数目设置很大也是不够的
		ConfigCacheSize commodityConfigCacheSize = new ConfigCacheSize();
		commodityConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		commodityConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		commodityConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(commodityConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		ConfigCacheSize commodityConfigCacheSize = new ConfigCacheSize();
		commodityConfigCacheSize.setValue("100");
		commodityConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		commodityConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(commodityConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	private void pay() throws Exception {
		// 读取所有的商品类别和品牌、包装单位
		brandList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Brand.getIndex());
		packageUnitList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_PackageUnit.getIndex());
		categoryList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Category.getIndex());
		providerList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Provider.getIndex());
		simpleCommodityList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Commodity.getIndex());

		int i = 0; // 还未完成期间，先不死循环
		while (i < 50) {
			// 1、随机地创建信息随机的商品
			CommodityDataProvider commodityDataProvider = new CommodityDataProvider();
			commodityDataProvider.setBrandList(brandList);
			commodityDataProvider.setCategoryList(categoryList);
			commodityDataProvider.setPackageUnitList(packageUnitList);
			commodityDataProvider.setProviderList(providerList);
			commodityDataProvider.setSimpleCommodityList(simpleCommodityList);
			//
			List<Commodity> commodityList = createNCommodity(commodityDataProvider.generateRandomCommodities());

			// 获取到最新创建的商品
			simpleCommodityList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Commodity.getIndex());

			// 2、采购
			PurchasingOrderDataProvider purchasingOrderDataProvider = new PurchasingOrderDataProvider();
			purchasingOrderDataProvider.setCommodityList(simpleCommodityList);
			purchasingOrderDataProvider.setProviderList(providerList);
			//
			List<BaseModel> purchasingOrderList = createNPurchasingOrder(purchasingOrderDataProvider.generateRandomPurchasingOrders());
			// 随机进行审核
			List<BaseModel> approvePurchasingOrderList = randomApprovePurchasingOrder(purchasingOrderList);

			// 3、入库 (入库多少张采购订单？随机入库数量.入库商品)
			WarehousingDataProvider warehousingDataProvider = new WarehousingDataProvider();
			warehousingDataProvider.setCommodityList(simpleCommodityList);
			warehousingDataProvider.setProviderList(providerList);
			warehousingDataProvider.setPurchasingOrderList(approvePurchasingOrderList);
			List<BaseModel> warehousingList = createNWarehousing(warehousingDataProvider.generateRandomWarehousings());
			// 随机审核入库单
			randomApproveWarehousing(warehousingList);

			// 4、促销
			PromotionDataProvider promotionDataProvider = new PromotionDataProvider();
			promotionDataProvider.setCommodityList(simpleCommodityList);
			createNPromotion(promotionDataProvider.generateRandomPromotions());

			// 5、优惠券
			List<BaseModel> simpleCommodityListCreated = new ArrayList<>();
			for (Commodity commodity : commodityList) {
				if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
					simpleCommodityListCreated.add(commodity);
				}
			}
			CouponDataProvider couponDataProvider = new CouponDataProvider();
			// couponDataProvider.setNomalCommodityList(nomalCommodityList);
			couponDataProvider.setSimpleCommodityList(simpleCommodityListCreated);
			List<Coupon> couponList = createNCoupon(couponDataProvider.generateRandomCoupons());

			// 6、随机创建会员
			VipDataProvider vipDataProvider = new VipDataProvider();
			List<Vip> vipList = createNVip(vipDataProvider.generateRandomVips());
			// 手动增加会员的积分
			updateNBonus(vipList);

			// 7、随机领券
			List<Coupon> normalCouponListCreated = new ArrayList<>();
			for (Coupon coupon : couponList) {
				if (coupon.getStatus() == EnumCouponCodeStatus.ECCS_Normal.getIndex()) {
					normalCouponListCreated.add(coupon);
				}
			}
			CouponCodeDataProvider couponCodeDataProvider = new CouponCodeDataProvider();
			couponCodeDataProvider.setCouponList(normalCouponListCreated);
			couponCodeDataProvider.setVipList(vipList);
			//
			List<CouponCode> availableCouponCodeList = new ArrayList<>();
			List<CouponCode> couponCodes = couponCodeDataProvider.generateRandomCouponCodes();
			if (couponCodes.size() > 0) {
				List<CouponCode> couponCodeList = createNCouponCode(couponCodes);
				// 可用的优惠券
				// 此优惠券是否已到起用日期
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
				for (CouponCode cc : couponCodeList) {
					for (Coupon queryCoupon : couponList) {
						if (cc.getCouponID() == queryCoupon.getID()) {
							if (DatetimeUtil.isInDatetimeRange(queryCoupon.getBeginDateTime(), now, queryCoupon.getEndDateTime())) {
								// 判断此优惠券是否在使用星期中，如使用时间为星期一 9：00-11：00，那么星期一九点到11点是可用的，其他时间不可使用
								Date beginTime = simpleDateFormat.parse(queryCoupon.getBeginTime());
								Date endTime = simpleDateFormat.parse(queryCoupon.getEndTime());
								if (DatetimeUtil.isInWeekday(null, now, queryCoupon.getWeekDayAvailable()) && DatetimeUtil.isInTimeRange(beginTime, endTime, now)) {
									availableCouponCodeList.add(cc);
								}
							}
						}
					}
				}
			}

			// 正在促销的活动
			List<BaseModel> promotionList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Promotion.getIndex());

			// 8、随机零售和随机退货
			retailTradeDataProvider.setVipList(vipList);
			retailTradeDataProvider.setCommodityList(commodityList);
			retailTradeDataProvider.setCouponList(couponList);
			retailTradeDataProvider.setPromotionList(promotionList);
			retailTradeDataProvider.setAvailableCouponCodeList(availableCouponCodeList);
			List<RetailTrade> retailTradeList = createNRetailTrade(retailTradeDataProvider.generateRandomRetailTrades(), retailTradeDataProvider);
			i++;
		}
	}

	private void updateNBonus(List<Vip> vipList) throws Exception {
		for (Vip vip : vipList) {
			VipDataProvider.updateBonus(vip);
			BaseVipTest.updateBonusViaAction(vip, sessionBoss, mvc);
		}
	}

	private void createNPromotion(List<Promotion> generateRandomPromotions) throws Exception {
		for (Promotion promotion : generateRandomPromotions) {
			Promotion promotionInDB = createPromotionViaMapper(promotion);
			if (promotion.getListSlave1() != null && promotion.getStatus() == Promotion.EnumStatusPromotion.ESP_Active.getIndex()) {
				for (Object object : promotion.getListSlave1()) {
					PromotionScope promotionScope = (PromotionScope) object;
					promotionScope.setPromotionID(promotionInDB.getID());
					//
					BasePromotionTest.createPromotionScopeViaMapper(promotionScope, EnumErrorCode.EC_NoError);
				}
			}
		}
	}

	private Promotion createPromotionViaMapper(Promotion promotion) {
		Map<String, Object> params = promotion.getCreateParam(BaseBO.INVALID_CASE_ID, promotion);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion promotionInDB = (Promotion) promotionMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		promotion.setIgnoreIDInComparision(true);
		promotion.setIgnoreSlaveListInComparision(true); // promotionInDB此时还没从表
		if (promotion.compareTo(promotionInDB) != 0) {
			Assert.assertTrue(false, Shared.CompareToErrorMsg);
		}
		//
		return promotionInDB;
	}

	private void randomApproveWarehousing(List<BaseModel> warehousingList) throws Exception {
		Random n = new Random();
		for (BaseModel warehousing : warehousingList) {
			if (n.nextBoolean()) {
				((Warehousing) warehousing).setApproverID(Shared.BossID);
				BaseWarehousingTest.approveViaAction((Warehousing) warehousing, Shared.DBName_Test, mvc, sessionBoss, mapBO);
			}
		}
	}

	private List<BaseModel> createNWarehousing(List<Warehousing> warehousings) throws Exception {
		List<BaseModel> list = new ArrayList<BaseModel>();
		for (Warehousing warehousing : warehousings) {
			list.add(BaseWarehousingTest.createViaAction(warehousing, Shared.DBName_Test, mvc, sessionBoss, mapBO));
		}
		return list;
	}

	private List<BaseModel> randomApprovePurchasingOrder(List<BaseModel> purchasingOrderList) throws Exception {
		List<BaseModel> list = new ArrayList<BaseModel>();
		Random n = new Random();
		for (BaseModel purchasingOrder : purchasingOrderList) {
			if (n.nextBoolean()) {
				PurchasingOrder approverAfterPurchasingOrder = BasePurchasingOrderTest.approverViaAction((PurchasingOrder) purchasingOrder, Shared.DBName_Test, mvc, sessionBoss, mapBO);
				approverAfterPurchasingOrder.setListSlave1(BasePurchasingOrderTest.retrieveNPurchasingOrderCommodityListByPurchasingOrderIDViaMapper(approverAfterPurchasingOrder.getID()));
				list.add(approverAfterPurchasingOrder);
			}
		}
		return list;
	}

	private List<BaseModel> createNPurchasingOrder(List<PurchasingOrder> purchasingOrders) throws Exception {
		List<BaseModel> list = new ArrayList<BaseModel>();
		for (PurchasingOrder purchasingOrder : purchasingOrders) {
			list.add(BasePurchasingOrderTest.createViaAction(purchasingOrder, Shared.DBName_Test, mvc, sessionBoss, mapBO));
		}
		return list;
	}

	public List<Commodity> createNCommodity(List<Commodity> commodityList) throws Exception {
		List<Commodity> list = new ArrayList<Commodity>();
		for (Commodity commodity : commodityList) {
			list.add(BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test));
		}
		return list;
	}

	public List<Coupon> createNCoupon(List<Coupon> couponList) throws Exception {
		List<Coupon> list = new ArrayList<>();
		for (Coupon coupon : couponList) {
			list.add(BaseCouponTest.createViaAction(coupon, sessionBoss, mvc, EnumErrorCode.EC_NoError));
		}
		return list;
	}

	public List<Vip> createNVip(List<Vip> vipList) throws Exception {
		List<Vip> list = new ArrayList<>();
		for (Vip vip : vipList) {
			list.add(BaseVipTest.createViaAction(vip, sessionBoss, mvc, EnumErrorCode.EC_NoError));
		}
		return list;
	}

	public List<CouponCode> createNCouponCode(List<CouponCode> couponCodeList) throws Exception {
		List<CouponCode> list = new ArrayList<>();
		Vip vip = new Vip();
		Coupon coupon = new Coupon();
		for (CouponCode couponCode : couponCodeList) {
			coupon.setID(couponCode.getCouponID());
			coupon = (Coupon) BaseCouponTest.retrieve1ViaMapper(coupon);

			vip.setID(couponCode.getVipID());
			vip = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);
			if (vip.getBonus() > coupon.getBonus()) {
				list.add(BaseCouponCodeTest.createViaAction(couponCode, mvc, Shared.getVipLoginSession(mvc, vip, true), EnumErrorCode.EC_NoError));
			}
		}
		return list;
	}

	public List<RetailTrade> createNRetailTrade(List<RetailTrade> retailTradeList, RetailTradeDataProvider retailTradeDataProvider) throws Exception {
		List<RetailTrade> list = new ArrayList<>();
		for (RetailTrade retailTrade : retailTradeList) {
			RetailTrade retailTradeCreated = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTrade, Shared.DBName_Test);
			list.add(retailTradeCreated);
			if (retailTradeCreated.getSourceID() == BaseAction.INVALID_ID) {
				RetailTradeDataProvider.commonRetailTradeList.add(retailTradeCreated);
			}
		}
		return list;
	}
}
