package com.bx.erp.test.promotion.hybrid;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.PromotionCalculator;
import com.bx.erp.dao.RetailTradeCommodityMapper;
import com.bx.erp.dao.RetailTradeMapper;
import com.bx.erp.dao.trade.PromotionMapper;
import com.bx.erp.dao.trade.PromotionScopeMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

public class BasePromotionCalculatingTest extends BaseTestNGSpringContextTest {
	protected RetailTradeMapper retailTradeMapper;
	protected PromotionMapper promotionMapper;
	protected RetailTradeCommodityMapper retailTradeCommodityMapper;
	protected PromotionScopeMapper promotionScopeMapper;
	@Resource
	private PromotionCalculator promotionCalculator;

	protected RetailTrade rtOriginal = null;
	protected RetailTrade rtBeforePromotion = null;
	protected List<BaseModel> listPromotion = null;

	/** 从DB中加载ID为retailTradeID的零售单的数据，和ID为iaPromotionID的促销活动 */
	protected boolean loadDataFromDB(String dbName, int retailTradeID, int[] iaPromotionID) {
		// R1 by retailTradeID
		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setID(retailTradeID);
		Map<String, Object> retailTradeR1 = retailTrade.getRetrieve1Param(BaseBO.INVALID_CASE_ID, retailTrade);
		DataSourceContextHolder.setDbName(dbName);
		rtOriginal = (RetailTrade) retailTradeMapper.retrieve1(retailTradeR1);
		Assert.assertTrue(rtOriginal != null && EnumErrorCode.values()[Integer.parseInt(retailTradeR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTradeR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询从表
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setTradeID(rtOriginal.getID());
		retailTradeCommodity.setPageSize(BaseAction.PAGE_SIZE_MAX);
		Map<String, Object> retailTradeCommodityRN = retailTradeCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> retailTradeCommodityList = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(retailTradeCommodityRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retailTradeCommodityRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTradeCommodityRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		rtOriginal.setListSlave1(retailTradeCommodityList);
		//
		// 得到未经过促销的零售单
		rtBeforePromotion = (RetailTrade) rtOriginal.clone();
		List<?> rtcBeforePromotionList = rtBeforePromotion.getListSlave1();
		double rtAmountBeforePromotion = 0.00d;
		for (int i = 0; i < rtcBeforePromotionList.size(); i++) {
			RetailTradeCommodity rtcBeforePromotion = (RetailTradeCommodity) rtcBeforePromotionList.get(i);
			rtcBeforePromotion.setPriceReturn(rtcBeforePromotion.getPriceOriginal());// 重置零售单商品的退货价 = 原价
			rtAmountBeforePromotion = GeneralUtil.sum(rtAmountBeforePromotion, GeneralUtil.mul(rtcBeforePromotion.getPriceOriginal(), rtcBeforePromotion.getNO()));
		}
		rtBeforePromotion.setAmount(rtAmountBeforePromotion);
		//
		// 根据传入的促销ID加载促销活动
		listPromotion = new ArrayList<BaseModel>();
		for (int i = 0; i < iaPromotionID.length; i++) {
			// R1 promotion ID
			Promotion promotion = new Promotion();
			promotion.setID(iaPromotionID[i]);
			Map<String, Object> promotionR1 = promotion.getRetrieve1Param(BaseBO.INVALID_CASE_ID, promotion);
			//
			DataSourceContextHolder.setDbName(dbName);
			Promotion pR1 = (Promotion) promotionMapper.retrieve1(promotionR1);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(promotionR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, promotionR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			// 查询从表
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setPromotionID(pR1.getID());
			promotionScope.setPageSize(BaseAction.PAGE_SIZE_MAX);
			Map<String, Object> promotionScopeRN = promotionScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, promotionScope);
			//
			DataSourceContextHolder.setDbName(dbName);
			List<BaseModel> psRN = promotionScopeMapper.retrieveN(promotionScopeRN);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(promotionScopeRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, promotionScopeRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			pR1.setListSlave1(psRN);
			listPromotion.add(pR1);
		}

		return true;
	}

	protected RetailTrade calculate(String dbName) throws FileNotFoundException {
		// PromotionCalculator pc = new PromotionCalculator();
		return promotionCalculator.calculateBestCase(rtBeforePromotion, listPromotion, dbName);
	}

	protected void verifyCalculateResult(RetailTrade retailTrade) {
		Assert.assertTrue(Math.abs(GeneralUtil.sub(retailTrade.getAmount(), rtOriginal.getAmount())) < BaseModel.TOLERANCE, "ID：" + retailTrade.getID() + "应收款不一致"); // 对比零售单应收款
		//
		List<?> rtcListOriginal = rtOriginal.getListSlave1(); // 拿出数据库原零售单的从表
		List<?> rtcListAfterPromotion = retailTrade.getListSlave1(); // 经过促销优惠计算的零售单从表
		for (int i = 0; i < rtcListOriginal.size(); i++) { // 遍历原零售单的从表
			RetailTradeCommodity rtcOriginal = (RetailTradeCommodity) rtcListOriginal.get(i);
			for (int j = 0; j < rtcListAfterPromotion.size(); j++) { // 遍历经过促销优惠计算的零售单从表
				RetailTradeCommodity rtcAfterPromotion = (RetailTradeCommodity) rtcListAfterPromotion.get(j);
				if (rtcOriginal.getID() == rtcAfterPromotion.getID()) { // 零售单从表的各商品的退货对比
					Assert.assertTrue(Math.abs(GeneralUtil.sub(Double.valueOf(String.format("%.2f", rtcOriginal.getPriceReturn())), Double.valueOf(String.format("%.2f", rtcAfterPromotion.getPriceReturn())))) < BaseModel.TOLERANCE,
							"ID：" + rtcAfterPromotion.getID() + "的零售商品的退货价有问题！");
				}
			}
		}
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		retailTradeMapper = (RetailTradeMapper) applicationContext.getBean("retailTradeMapper");
		promotionMapper = (PromotionMapper) applicationContext.getBean("promotionMapper");
		retailTradeCommodityMapper = (RetailTradeCommodityMapper) applicationContext.getBean("retailTradeCommodityMapper");
		promotionScopeMapper = (PromotionScopeMapper) applicationContext.getBean("promotionScopeMapper");

	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
}
