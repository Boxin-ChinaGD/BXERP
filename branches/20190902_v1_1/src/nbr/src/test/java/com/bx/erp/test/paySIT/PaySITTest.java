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

/** ?????????????????????????????????????????????????????? <br />
 * ??????????????????????????????????????????????????????????????????????????????????????? <br />
 * ??????????????????????????????????????????????????????????????????????????????????????????????????????RD <br />
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

		// ... ????????????????????????????????????????????????????????????????????????
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
		// ???????????????????????????????????????????????????
		brandList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Brand.getIndex());
		packageUnitList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_PackageUnit.getIndex());
		categoryList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Category.getIndex());
		providerList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Provider.getIndex());
		simpleCommodityList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Commodity.getIndex());

		int i = 0; // ????????????????????????????????????
		while (i < 50) {
			// 1???????????????????????????????????????
			CommodityDataProvider commodityDataProvider = new CommodityDataProvider();
			commodityDataProvider.setBrandList(brandList);
			commodityDataProvider.setCategoryList(categoryList);
			commodityDataProvider.setPackageUnitList(packageUnitList);
			commodityDataProvider.setProviderList(providerList);
			commodityDataProvider.setSimpleCommodityList(simpleCommodityList);
			//
			List<Commodity> commodityList = createNCommodity(commodityDataProvider.generateRandomCommodities());

			// ??????????????????????????????
			simpleCommodityList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Commodity.getIndex());

			// 2?????????
			PurchasingOrderDataProvider purchasingOrderDataProvider = new PurchasingOrderDataProvider();
			purchasingOrderDataProvider.setCommodityList(simpleCommodityList);
			purchasingOrderDataProvider.setProviderList(providerList);
			//
			List<BaseModel> purchasingOrderList = createNPurchasingOrder(purchasingOrderDataProvider.generateRandomPurchasingOrders());
			// ??????????????????
			List<BaseModel> approvePurchasingOrderList = randomApprovePurchasingOrder(purchasingOrderList);

			// 3????????? (????????????????????????????????????????????????.????????????)
			WarehousingDataProvider warehousingDataProvider = new WarehousingDataProvider();
			warehousingDataProvider.setCommodityList(simpleCommodityList);
			warehousingDataProvider.setProviderList(providerList);
			warehousingDataProvider.setPurchasingOrderList(approvePurchasingOrderList);
			List<BaseModel> warehousingList = createNWarehousing(warehousingDataProvider.generateRandomWarehousings());
			// ?????????????????????
			randomApproveWarehousing(warehousingList);

			// 4?????????
			PromotionDataProvider promotionDataProvider = new PromotionDataProvider();
			promotionDataProvider.setCommodityList(simpleCommodityList);
			createNPromotion(promotionDataProvider.generateRandomPromotions());

			// 5????????????
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

			// 6?????????????????????
			VipDataProvider vipDataProvider = new VipDataProvider();
			List<Vip> vipList = createNVip(vipDataProvider.generateRandomVips());
			// ???????????????????????????
			updateNBonus(vipList);

			// 7???????????????
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
				// ??????????????????
				// ????????????????????????????????????
				Date now = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
				for (CouponCode cc : couponCodeList) {
					for (Coupon queryCoupon : couponList) {
						if (cc.getCouponID() == queryCoupon.getID()) {
							if (DatetimeUtil.isInDatetimeRange(queryCoupon.getBeginDateTime(), now, queryCoupon.getEndDateTime())) {
								// ???????????????????????????????????????????????????????????????????????? 9???00-11???00???????????????????????????11??????????????????????????????????????????
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

			// ?????????????????????
			List<BaseModel> promotionList = DBDataLoader.queryAllData(DBDataLoader.EnumQueryDataName.EQDN_Promotion.getIndex());

			// 8??????????????????????????????
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
		promotion.setIgnoreSlaveListInComparision(true); // promotionInDB??????????????????
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
