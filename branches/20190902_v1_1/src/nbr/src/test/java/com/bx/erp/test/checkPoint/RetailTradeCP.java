package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.RetailTradeCommodityBO;
import com.bx.erp.action.bo.RetailTradeCommoditySourceBO;
import com.bx.erp.action.bo.RetailTradeCouponBO;
import com.bx.erp.action.bo.ReturnRetailtradeCommoditydDestinationBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCommoditySource;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.ReturnRetailtradeCommoditydDestination;
import com.bx.erp.model.Vip;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseBonusConsumeHistoryTest;
import com.bx.erp.test.BaseBonusRuleTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCouponCodeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.BaseVipTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

public class RetailTradeCP extends BaseTestNGSpringContextTest {

	// verifyCreate 能检查全部创建成功和部分创建成功
	// param isNoError 如果测试结果是EC_NoError 则传递true，EC_PartSuccess 则传递false
	// 1、零售单A普通缓存是否创建，零售单A的ListSlave1()中零售商品数据是否正确。
	// 2、零售单A中售出的商品普通缓存是否刷新。
	// 3、检查数据库T_RetailTrade是否有创建零售单A的数据。
	// 4、检查数据库T_RetailTradeCommodity是否有创建零售单A相关的零售单商品数据。
	// 5、检查数据库T_RetailTradeCommoditySource，查看零售单A售出商品的来源信息是否正确。
	// 6、检查数据库T_Commodity，查看零售单A中出售的商品的F_NO是否正确计算，F_CurrentWarehousingID(当值入库ID)是否更新。
	// 7、检查数据库T_WarehousingCommodity，查看零售单A售出商品的入库单商品，F_NOSaleable(可售数量)是否正确计算。（可能没有入库单）
	// 8、检查数据库T_RetailTradePromoting，如果该零售单有参与促销，那就要检查有没有生成零售单A的零售单促销。
	// 9、检查数据库T_RetailTradePromotingFlow，如果该零售单有参与促销，那就要检查有没有生成零售单A的促销计算过程。如果有参与多条促销，则会生成多条计算过程。
	public static boolean verifyCreate(RetailTrade bmOfDB, BaseModel bmCreateObjet, Map<Integer, List<Warehousing>> mapBeforeSale, Map<Integer, List<Warehousing>> mapAfterSale, Map<Integer, Commodity> simpleCommodityList,
			Map<String, BaseBO> mapBO, String dbName, boolean isNoError) throws Exception {

		// 设置对象默认值后进行比较
		RetailTrade retailTradeDefalutValue = (RetailTrade) bmCreateObjet.clone();
		retailTradeDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);
		bmOfDB.setIgnoreIDInComparision(true);
		bmOfDB.setIgnoreSlaveListInComparision(!isNoError); // 部分成功就不需要比较从表
		Assert.assertTrue(bmOfDB.compareTo(retailTradeDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法," + error);

		boolean isReturn = false;
		if (bmOfDB.getSourceID() > 0) { // 大于0就是退货单
			isReturn = true;
		}

		RetailTradeCommodityBO retailTradeCommodityBO = (RetailTradeCommodityBO) mapBO.get(RetailTradeCommodityBO.class.getSimpleName());
		ReturnRetailtradeCommoditydDestinationBO returnRetailtradeCommoditydDestinationBO = (ReturnRetailtradeCommoditydDestinationBO) mapBO.get(ReturnRetailtradeCommoditydDestinationBO.class.getSimpleName());
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		SubCommodityBO subCommodityBO = (SubCommodityBO) mapBO.get(SubCommodityBO.class.getSimpleName());
		RetailTradeCommoditySourceBO retailTradeCommoditySourceBO = (RetailTradeCommoditySourceBO) mapBO.get(RetailTradeCommoditySourceBO.class.getSimpleName());
		RetailTradePromotingBO retailTradePromotingBO = (RetailTradePromotingBO) mapBO.get(RetailTradePromotingBO.class.getSimpleName());
		RetailTradePromotingFlowBO retailTradePromotingFlowBO = (RetailTradePromotingFlowBO) mapBO.get(RetailTradePromotingFlowBO.class.getSimpleName());
		RetailTradeCouponBO retailTradeCouponBO = (RetailTradeCouponBO) mapBO.get(RetailTradeCouponBO.class.getSimpleName());

		if (isReturn) { // 退货单
			verifyReturnRetailtradeCommoditydDestination(simpleCommodityList, bmOfDB, retailTradeCommodityBO, returnRetailtradeCommoditydDestinationBO, commodityBO, dbName, isNoError);
		} else { // 零售单
			verifyRetailTradeCommoditySource(bmOfDB, retailTradeCommoditySourceBO, subCommodityBO, dbName, commodityBO);

			if (isNoError && retailTradeDefalutValue.getListSlave2() != null && retailTradeDefalutValue.getListSlave2().size() > 0) {
				assertTrue(bmOfDB.getListSlave2() != null && bmOfDB.getListSlave2().size() > 0, "创建零售单促销失败");
			}
			if (bmOfDB.getListSlave2() != null && bmOfDB.getListSlave2().size() > 0) {
				BaseRetailTradePromotingTest.verifyRetailTradePromoting(bmOfDB, retailTradeDefalutValue, retailTradePromotingBO, retailTradePromotingFlowBO, dbName, isNoError);
			}
			// 促销不在nbr这边做检查，因为促销计算过程是在POS端生成的，以后在POS端的测试中再增加该检查点

			// 合并时，1.1使用这部分代码
			if (isNoError && retailTradeDefalutValue.getListSlave3() != null && retailTradeDefalutValue.getListSlave3().size() > 0) {
				assertTrue(bmOfDB.getListSlave3() != null && bmOfDB.getListSlave3().size() > 0, "创建零售单优惠券使用表失败");
			}
			if (bmOfDB.getListSlave3() != null && bmOfDB.getListSlave3().size() > 0) {
				verifyCreateRetailTradeCoupon(retailTradeDefalutValue, bmOfDB.getID(), retailTradeCouponBO, dbName);
			}
		}

		verifyCommodityNO(simpleCommodityList, bmOfDB, commodityBO, subCommodityBO, isReturn, dbName);

		verifyWarehousingCommodityNoSalableAndCurrentWarehousingID(commodityBO, subCommodityBO, dbName, simpleCommodityList, mapBeforeSale, mapAfterSale, isReturn, bmOfDB.getListSlave1());

		// 如果是会员消费:需要检查会员消费记录，积分变动,会员缓存是否更新，优惠券是否核销等
		if(isNoError && bmOfDB.getVipID() > 0) {
			//  增长的积分，通过积分规则算出来
			BonusRule bonusRuleR1 = BaseBonusRuleTest.DataInput.getBonusRule(1);
			bonusRuleR1.setID(1);
			BonusRule bonusRuleR1DB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRuleR1, dbName);
			double expectedIncreaseBonus = bmOfDB.getAmount() * 100 / bonusRuleR1DB.getAmountUnit() * bonusRuleR1DB.getIncreaseBonus();
			expectedIncreaseBonus = expectedIncreaseBonus > bonusRuleR1DB.getMaxIncreaseBonus() ? bonusRuleR1DB.getMaxIncreaseBonus() : expectedIncreaseBonus;
			expectedIncreaseBonus = Math.round(expectedIncreaseBonus); // 积分bonus是int类型，所以需要四舍五入
			// sp判断零售金额大于0时，才需要增加积分、插入积分历史
			if(bmOfDB.getAmount() > 0) {
				// 消费记录是否创建
				BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
				bonusConsumeHistoryToRN.setVipID(bmOfDB.getVipID());
				List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, dbName, EnumErrorCode.EC_NoError);
				Assert.assertTrue(bonusConsumeHistoryRetrieveN != null && bonusConsumeHistoryRetrieveN.size() > 0, "会员的消费记录没有创建");
				BonusConsumeHistory bonusConsumeHistoryCreated = (BonusConsumeHistory) bonusConsumeHistoryRetrieveN.get(0);
				Assert.assertTrue(Math.abs(GeneralUtil.sub(expectedIncreaseBonus, bonusConsumeHistoryCreated.getAddedBonus())) < RetailTrade.TOLERANCE, "增加的积分不对, expectedIncreaseBonus:" + expectedIncreaseBonus + ",addedBonus:" + bonusConsumeHistoryCreated.getAddedBonus());
			}
			// 积分变动   TODO 是否需要传入创建零售单前会员的对象，得到修改前的积分。所有创建零售单的地方都要传这个对象，影响较大
			Vip vipGet = BaseVipTest.DataInput.getVip();
			vipGet.setID(bmOfDB.getVipID());
			Vip vipDB = BaseVipTest.retrieve1ViaMapper(vipGet, dbName);
			Assert.assertTrue(vipDB.getBonus() >= expectedIncreaseBonus, "会员积分没有变动");
			//
			ErrorInfo ecOut = new ErrorInfo();
			Vip vipCache = (Vip) CacheManager.getCache(dbName, EnumCacheType.ECT_Vip).read1(bmOfDB.getVipID(), BaseBO.SYSTEM, ecOut, dbName);
			Assert.assertTrue(vipDB.compareTo(vipCache) == 0, "会员缓存没有更新");
			if (bmOfDB.getListSlave3() != null && bmOfDB.getListSlave3().size() > 0) {
				// 1、优惠券是否核销
				RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) bmOfDB.getListSlave3().get(0);
				CouponCode couponCode = new CouponCode();
				couponCode.setID(retailTradeCoupon.getCouponCodeID());
				CouponCode couponCodeDB = (CouponCode) BaseCouponCodeTest.retrieve1ViaMapper(couponCode);
				Assert.assertTrue(couponCodeDB.getStatus() == EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "优惠券没有核销成功");
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static void verifyCreateRetailTradeCoupon(RetailTrade retailTrade, int retailTradeIDInDB, RetailTradeCouponBO retailTradeCouponBO, String dbName) {
		RetailTradeCoupon retailTraddeCupon = new RetailTradeCoupon();
		retailTraddeCupon.setRetailTradeID(retailTradeIDInDB);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> retailTraddeCuponList = (List<BaseModel>) retailTradeCouponBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTraddeCupon);
		if (retailTradeCouponBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "根据零售单查找零售优惠券使用表失败," + retailTradeCouponBO.printErrorInfo());
		}
		assertTrue((retailTraddeCuponList != null && retailTraddeCuponList.size() == 1), "零售单优惠券使用表为空或者数目不正确");
		RetailTradeCoupon retailTraddeCuponInDB = (RetailTradeCoupon) retailTraddeCuponList.get(0);
		//
		retailTraddeCuponInDB.setIgnoreIDInComparision(true);
		((RetailTradeCoupon) retailTrade.getListSlave3().get(0)).setRetailTradeID(retailTraddeCuponInDB.getRetailTradeID()); // 生成数据时并不知道真实的零售单ID
		if (retailTraddeCuponInDB.compareTo((RetailTradeCoupon) retailTrade.getListSlave3().get(0)) != 0) {
			assertTrue(false, Shared.CompareToErrorMsg);
		}

		Assert.assertTrue(retailTrade.getCouponAmount() <= 0, "使用了优惠券但是没将优惠金额放入CouponAmount字段中");
	}

	// @SuppressWarnings("unchecked")
	// private static void verifyRetailTradePromoting(RetailTrade bmOfDB,
	// RetailTradePromotingBO retailTradePromotingBO, RetailTradePromotingFlowBO
	// retailTradePromotingFlowBO, String dbName) {
	// boolean isPromoting = false;
	// for (Object o : bmOfDB.getListSlave1()) {
	// RetailTradeCommodity rtc = (RetailTradeCommodity) o;
	// if (Math.abs(rtc.getPriceOriginal() - rtc.getPriceReturn()) >
	// BaseModel.TOLERANCE) {
	// isPromoting = true;
	// break;
	// }
	// }
	//
	// if (isPromoting) {
	// RetailTradePromoting rp = new RetailTradePromoting();
	// rp.setTradeID(bmOfDB.getID());
	// //
	// DataSourceContextHolder.setDbName(dbName);
	// List<RetailTradePromoting> rpList = (List<RetailTradePromoting>)
	// retailTradePromotingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
	// rp);
	// if (retailTradePromotingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// assertTrue(false, "查询零售促销计算表失败");
	// }
	// //
	// assertTrue(rpList != null && rpList.size() > 0, "零售促销计算表没有正确插入到数据库中");
	//
	// RetailTradePromotingFlow rpf = new RetailTradePromotingFlow();
	// rpf.setRetailTradePromotingID(rpList.get(0).getID());
	// //
	// DataSourceContextHolder.setDbName(dbName);
	// List<RetailTradePromotingFlow> rpfList = (List<RetailTradePromotingFlow>)
	// retailTradePromotingFlowBO.retrieveNObject(BaseBO.SYSTEM,
	// BaseBO.INVALID_CASE_ID, rpf);
	// if (retailTradePromotingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// assertTrue(false, "查询零售促销计算表失败");
	// }
	// assertTrue(rpfList != null && rpfList.size() > 0, "零售促销计算表没有正确插入到数据库中");
	// }
	// }

	/** 检查商品库存是否正确增减 
	 * @throws CloneNotSupportedException */
	private static void verifyCommodityNO(Map<Integer, Commodity> simpleCommodityList, RetailTrade retailTrade, CommodityBO commodityBO, SubCommodityBO subCommodityBO, boolean isReturn/* 是否退货 */, String dbName) throws CloneNotSupportedException {
		List<?> retailTradeCommodityList = retailTrade.getListSlave1();
		int iNOVariation = 0; // 记录零售或退货的数量
		int oldNOInWarehouse = 0;
		int newNOInWarehouse = 0;
		for (Iterator<Integer> it = simpleCommodityList.keySet().iterator(); it.hasNext();) {
			Integer commID = it.next();
			Commodity commodity = simpleCommodityList.get(commID);
			iNOVariation = commodity.getSaleNO(); // 得到此单品的操作数量
			List<CommodityShopInfo> commodityShopInfos = (List<CommodityShopInfo>) commodity.getListSlave2();
			for(CommodityShopInfo commodityShopInfo : commodityShopInfos) {
				if(commodityShopInfo.getShopID() == retailTrade.getShopID()) {
					oldNOInWarehouse = commodityShopInfo.getNO();
				}
			}
//			List<List<BaseModel>> commList = getCommodityInfo(commID, commodityBO, dbName, true);
			List<BaseModel> commodityShopInfoList = BaseCommodityTest.getListCommodityShopInfoByCommID(commodity, dbName, retailTrade.getShopID());
			CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfoList.get(0);
//			newNOInWarehouse = ((Commodity) commList.get(0)).getNO();
			newNOInWarehouse = commodityShopInfo.getNO();
			if (checkIfIsSuccessfullySold(retailTradeCommodityList, commodityBO, subCommodityBO, dbName, commID)) {
				// 此零售单商品创建成功，商品库存应该减少了（售出）或增加了（退货）
				if (isReturn) {
					assertTrue(oldNOInWarehouse + iNOVariation == newNOInWarehouse, "零售后库存计算有误");
				} else {
					assertTrue(oldNOInWarehouse - iNOVariation == newNOInWarehouse, "零售后库存计算有误");
				}
			} else {
				// 此零售单商品创建失败，导致了部分成功。其商品库存不应该改变
				assertTrue(newNOInWarehouse == oldNOInWarehouse, "零售单商品未创建成功，但是修改了商品的库存");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean checkIfIsSuccessfullySold(List<?> retailTradeCommodityList, CommodityBO commodityBO, SubCommodityBO subCommodityBO, String dbName, Integer commID) {
		// 检查simpleCommodityList中的商品是否真的被卖掉了。如果成功地卖掉了，就检查其库存变化是否正确
		for (int i = 0; i < retailTradeCommodityList.size(); i++) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeCommodityList.get(i);
			List<List<BaseModel>> commList = getCommodityInfo(rtc.getCommodityID(), commodityBO, dbName, true);
			Commodity comm = Commodity.fetchCommodityFromResultSet(commList);
			if (comm.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(comm.getID());
				//
				DataSourceContextHolder.setDbName(dbName);
				List<SubCommodity> subCommodityList = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				Assert.assertTrue(subCommodityBO.getLastErrorCode() == EnumErrorCode.EC_NoError && subCommodityList != null && subCommodityList.size() > 0, subCommodityBO.getLastErrorMessage());
				//
				for (SubCommodity sc : subCommodityList) {
					if (sc.getSubCommodityID() == commID) {
						return true;
					}
				}
			} else if (comm.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				if (comm.getRefCommodityID() == commID) {
					return true;
				}
			} else {
				if (comm.getID() == commID) {
					return true;
				}
			}

		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static void verifyRetailTradeCommoditySource(RetailTrade retailTrade, RetailTradeCommoditySourceBO retailTradeCommoditySourceBO, SubCommodityBO subCommodityBO, String dbName, CommodityBO commodityBO) {
		RetailTradeCommoditySource retailTradeCommoditySource = new RetailTradeCommoditySource();
		Commodity commodity = new Commodity();

		for (Object o : retailTrade.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			retailTradeCommoditySource.setRetailTradeCommodityID(rtc.getID());
			retailTradeCommoditySource.setPageSize(BaseAction.PAGE_SIZE_MAX);
			//
			DataSourceContextHolder.setDbName(dbName);
			List<RetailTradeCommoditySource> rtcsList = (List<RetailTradeCommoditySource>) retailTradeCommoditySourceBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeCommoditySource);
			if (retailTradeCommoditySourceBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "根据零售商品表查询零售单来源失败，错误码：" + retailTradeCommoditySourceBO.getLastErrorCode());
			}
			assertTrue(rtcsList != null && rtcsList.size() > 0, "零售单来源有误！");

			// 当商品缓存查询不到，则会使用DB查询，当DB查询不到，则会出现错误，此时如果是删除商品则会有问题（pos未同步的情况），这里改为从DB获取
			// commodity = (Commodity) CacheManager.getCache(dbName,
			// EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(), BaseBO.SYSTEM,
			// ecOut, dbName);
			// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			// assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
			// }
			// 删除商品需要从DB获取
			List<List<BaseModel>> commodityList = getCommodityInfo(rtc.getCommodityID(), commodityBO, dbName, true);
			if (commodityList.get(0) instanceof BaseModel) {
				commodity = (Commodity) commodityList.get(0);
			} else {
				commodity = (Commodity) commodityList.get(0).get(0);
			}
			int saleNO = 0;
			// 普通商品和服务类商品一样
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex() && commodity.getType() == CommodityType.EnumCommodityType.ECT_Service.getIndex()) {
				for (RetailTradeCommoditySource rtcs : rtcsList) {
					saleNO += rtcs.getNO();
				}
				assertTrue(saleNO == rtc.getNO(), "零售商品来源中的数量和零售商品表数量不相等");
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {// 多包装商品
				for (RetailTradeCommoditySource rtcs : rtcsList) {
					assertTrue(rtcs.getReducingCommodityID() == commodity.getRefCommodityID());
					saleNO += rtcs.getNO();
				}
				// 售出数量 * 多包装倍数
				assertTrue(saleNO == rtc.getNO() * commodity.getRefCommodityMultiple(), "零售商品来源中的数量和零售商品表数量不相等");
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {// 组合商品
				// 获取组合商品子商品
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(commodity.getID());
				//
				DataSourceContextHolder.setDbName(dbName);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询子商品失败，错误码：" + retailTradeCommoditySourceBO.getLastErrorCode());
				}
				for (SubCommodity sc : listSubCommodity) {
					saleNO = 0;
					for (RetailTradeCommoditySource rtcs : rtcsList) {
						if (sc.getSubCommodityID() == rtcs.getReducingCommodityID()) {
							saleNO += rtcs.getNO();
						}
					}
					// 售出数量 * 组合商品的倍数
					assertTrue(saleNO == rtc.getNO() * sc.getSubCommodityNO(), "零售商品来源中的数量和零售商品表数量不相等");
				}
			}
		}

	}

	/***
	 * 退货时，检查退货商品去向表插入某商品的数量是否和卖出的商品对应的单品的数量一致
	 * 
	 * @param bmOfDB
	 * @param retailTradeBO
	 * @param retailTradeCommodityBO
	 * @param retailTradeCommoditySourceBO
	 * @param subCommodityBO
	 * @param dbName
	 */
	@SuppressWarnings("unchecked")
	private static void verifyReturnRetailtradeCommoditydDestination(Map<Integer, Commodity> simpleCommodityList, RetailTrade bmOfDB, RetailTradeCommodityBO retailTradeCommodityBO,
			ReturnRetailtradeCommoditydDestinationBO returnRetailtradeCommoditydDestinationBO, CommodityBO commodityBO, String dbName, boolean isNoError) {
		Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();// key：退货单品的ID,value:退货单品的数量

		// 通过零售单查询零售单商品表
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setTradeID(bmOfDB.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradeCommodity> returnRetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeCommodity);
		if (retailTradeCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询零售单商品表失败，错误信息：" + retailTradeCommodityBO.printErrorInfo());
		}
		if (isNoError) {
			assertTrue(returnRetailTradeCommodityList.size() > 0, "查询零售单商品有误！");
		}

		for (RetailTradeCommodity rtCommodity : returnRetailTradeCommodityList) {
			ReturnRetailtradeCommoditydDestination returnRetailtradeCommoditydDestination = new ReturnRetailtradeCommoditydDestination();
			returnRetailtradeCommoditydDestination.setRetailTradeCommodityID(rtCommodity.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<ReturnRetailtradeCommoditydDestination> tmpList = (List<ReturnRetailtradeCommoditydDestination>) returnRetailtradeCommoditydDestinationBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID,
					returnRetailtradeCommoditydDestination);
			if (returnRetailtradeCommoditydDestinationBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查询退货商品去向表失败，错误信息：" + returnRetailtradeCommoditydDestinationBO.printErrorInfo());
			}
			assertTrue(tmpList.size() > 0, "查询退货商品去向表有误！");
			// 进行退货的单品数量统计
			for (ReturnRetailtradeCommoditydDestination rrcd : tmpList) {
				List<List<BaseModel>> bmList = getCommodityInfo(rrcd.getIncreasingCommodityID(), commodityBO, dbName, true);
				if (((Commodity) bmList.get(0)).getType() == EnumCommodityType.ECT_Service.getIndex()) {
					System.out.println("进行退货的单品数量统计时服务商品不进行统计！！！");
					continue;
				}
				//
				int noVariation = rrcd.getNO();
				Integer returnNOFromMap = tmpMap.get(rrcd.getIncreasingCommodityID());
				if (returnNOFromMap != null) {
					noVariation += returnNOFromMap;
				}
				tmpMap.put(rrcd.getIncreasingCommodityID(), noVariation);
			}
		}
		// simpleCommodityList由退货单进行统计出的单品，key：simpleCommodityID,
		// value:simpleCommodity;tmpMap是根据退货单去DB中获取操作的单品
		// key:simpleCommodityID,value:noVariation
		for (Iterator<Integer> it = simpleCommodityList.keySet().iterator(); it.hasNext();) {
			int simpleCommodityID = it.next();
			for (Iterator<Integer> tmpIt = tmpMap.keySet().iterator(); tmpIt.hasNext();) {
				int tmpSimpleCommodityID = tmpIt.next();
				Integer tmpSimpleCommodityNO = tmpMap.get(tmpSimpleCommodityID);
				if (tmpSimpleCommodityID == simpleCommodityID) {
					Commodity simpleCommodity = simpleCommodityList.get(simpleCommodityID);
					assertTrue(simpleCommodity.getSaleNO() == tmpSimpleCommodityNO, "退货商品去向表中的数量没有正确计算！！！");
				} else {
					System.out.println("..........用于调试：商品ID不一致！！！...........");
				}
			}
		}
	}

	/**
	 * 检查入库单的可售数量和商品的当值入库ID是否符合期望
	 * 
	 * @param simpleCommodityList
	 *            零售单实际操作的单品，不包括服务型商品
	 * @param mapBeforeSale
	 *            创建零售单前预测的可售数量和当值入库ID
	 * @param mapAfterSale
	 *            创建零售单后由SP计算出来的可售数量和当值入库ID
	 * @param isReturn
	 *            是否为退货
	 */
	@SuppressWarnings("unchecked")
	private static void verifyWarehousingCommodityNoSalableAndCurrentWarehousingID(CommodityBO commodityBO, SubCommodityBO subCommodityBO, String dbName, Map<Integer, Commodity> simpleCommodityList,
			Map<Integer, List<Warehousing>> mapBeforeSale, Map<Integer, List<Warehousing>> mapAfterSale, boolean isReturn, List<?> retailTradeCommodityList) {
		// boolean isMapSorted = false;
		for (Iterator<Integer> it = simpleCommodityList.keySet().iterator(); it.hasNext();) {
			int warehousingChangeNO = 0;
			Integer commID = it.next();
			// 检查此商品是否真的被卖出，如果没有则不需要检查入库单的可售数量和商品的当值入库ID
			if (!checkIfIsSuccessfullySold(retailTradeCommodityList, commodityBO, subCommodityBO, dbName, commID)) {
				continue;
			}
			// 进行计算入库单操作数量
			List<Warehousing> wsListAfterSale = mapAfterSale.get(commID);
			if (CollectionUtils.isEmpty(wsListAfterSale)) { // 未入库的情况
				continue;
			}
			List<Warehousing> wsListBeforeSale = mapBeforeSale.get(commID);

			// if (!isMapSorted) {
			// isMapSorted = true;
			assertTrue(wsListAfterSale.size() == wsListBeforeSale.size());
			// 排序只排 一次，为下面的从表比较作准备
			WarehousingCommodity wc = new WarehousingCommodity();
			wc.setIsASC(EnumBoolean.EB_NO.getIndex());
			for (int i = 0; i < wsListBeforeSale.size(); i++) {
				Collections.sort((List<BaseModel>) wsListBeforeSale.get(i).getListSlave1(), wc);
				Collections.sort((List<BaseModel>) wsListAfterSale.get(i).getListSlave1(), wc);
			}

			List<List<BaseModel>> list = getCommodityInfo(commID, commodityBO, dbName, true);
			Commodity simpleComm = (Commodity) list.get(0);
			//
			Warehousing ws = new Warehousing();
			ws.setIsASC(EnumBoolean.EB_NO.getIndex());
			// 排序的意图为：wsListAfterSale是记录着此商品卖出或退货跨了多少个入库单.排序一下，将最大ID的放到最首个,最大ID的入库单，则说明商品已经使用到此入库单了。下面就通过get(0).getID()去喝商品的当值入库单ID进行对比.
			Collections.sort(wsListBeforeSale, ws);
			Collections.sort(wsListAfterSale, ws);
			//
			if (isReturn) {
				assertTrue(wsListAfterSale.get(wsListAfterSale.size() - 1).getID() == simpleComm.getCurrentWarehousingID(),
						"当值入库ID验证不通过, simpleComm =" + simpleComm.getCurrentWarehousingID() + "预期的：" + wsListAfterSale.get(wsListAfterSale.size() - 1).getID());
			} else {
				assertTrue(wsListAfterSale.get(0).getID() == simpleComm.getCurrentWarehousingID(), "当值入库ID验证不通过，simpleComm =" + simpleComm.getCurrentWarehousingID() + "预期的：" + wsListAfterSale.get(0).getID());
			}
			// }
			for (int i = 0; i < wsListBeforeSale.size(); i++) {
				for (int k = 0; k < wsListBeforeSale.get(i).getListSlave1().size(); k++) {
					List<WarehousingCommodity> listWCBeforeSale = (List<WarehousingCommodity>) wsListBeforeSale.get(i).getListSlave1();
					List<WarehousingCommodity> listWCAfterSale = (List<WarehousingCommodity>) wsListAfterSale.get(i).getListSlave1();
					if (listWCBeforeSale.get(k).getCommodityID() == commID) {
						if (isReturn) {
							warehousingChangeNO += listWCAfterSale.get(k).getNoSalable() - listWCBeforeSale.get(k).getNoSalable();
						} else {
							warehousingChangeNO += listWCBeforeSale.get(k).getNoSalable() - listWCAfterSale.get(k).getNoSalable();
						}
					}
				}
			}
			assertTrue(warehousingChangeNO == simpleCommodityList.get(commID).getSaleNO(), "入库单可售数量计算有误！");
		}
	}
}
