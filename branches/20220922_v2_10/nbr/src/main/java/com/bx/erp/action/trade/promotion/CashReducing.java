package com.bx.erp.action.trade.promotion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.GeneralUtil;

/** 满减促销基类 */
public abstract class CashReducing extends BasePromotion {
	private Log logger = LogFactory.getLog(Discount.class);

	/** 对指定的商品，计算零售单rt经过促销promotion计算后的优惠 */
	@Override
	protected double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion, String dbName) {
		double dLocalbalance = 0.000000d; // 指定商品的售价总和
		double dAmountTmp = rt.getAmount(); // 优惠后的应收款 暂时设置为原价
		ErrorInfo ecOut = new ErrorInfo();
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
		for (Object o1 : promotion.getListSlave1()) {
			PromotionScope ps = (PromotionScope) o1;
			for (Object o : rt.getListSlave1()) {
				RetailTradeCommodity rtc = (RetailTradeCommodity) o;
				if (rtc.getCommodityID() == ps.getCommodityID()) {
					Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(ps.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
					dLocalbalance = GeneralUtil.sum(dLocalbalance, GeneralUtil.mul(commodity.getPriceRetail(), rtc.getNO()));
					// 保存这个零售单商品
					listRetailTradeCommodity.add(rtc);
				}
			}
		}
		logger.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalbalance + "，  不满足条件的商品之和：" + GeneralUtil.sub(getOriginalTotalAmount(rt, dbName), dLocalbalance)); // (getOriginalTotalAmount(rt, dbName) -
		sbP.append("<p>" + "******此次活动名称为：<span class=\"text-warning\">" + promotion.getName() + "</span>，  满足此优惠活动的商品价格之和为：<span class=\"text-primary\">" + dLocalbalance + "</span>，  不满足条件的商品之和："
				+ GeneralUtil.sub(getOriginalTotalAmount(rt, dbName), dLocalbalance) + " 。</p>");
		if (dLocalbalance >= promotion.getExcecutionThreshold()) {
			// 大于阙值
			dAmountTmp = GeneralUtil.sum(GeneralUtil.sub(dLocalbalance, promotion.getExcecutionAmount()), GeneralUtil.sub(getOriginalTotalAmount(rt, dbName), dLocalbalance));
			this.getMapCommodity().put(promotion.getID(), listRetailTradeCommodity);
			// 保存计算过程
			StringBuilder promotingFlow = new StringBuilder();
			promotingFlow.append("此单原价为： " + GeneralUtil.formatToCalculate(getOriginalTotalAmount(rt, dbName)) + "\t")//
					.append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "减" + promotion.getExcecutionAmount() + "\t")//
					.append("满足此活动的商品零售价之和为： " + GeneralUtil.formatToCalculate(dLocalbalance) + "\t")
					.append("此次计算后的应收款为： " + GeneralUtil.formatToCalculate(dLocalbalance) + " - " + GeneralUtil.formatToCalculate(promotion.getExcecutionAmount()) + " + (" + GeneralUtil.formatToCalculate(getOriginalTotalAmount(rt, dbName))
							+ " - " + GeneralUtil.formatToCalculate(dLocalbalance) + ") = " + GeneralUtil.formatToCalculate(dAmountTmp) + "\t");//
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setPromotionID(promotion.getID());
			retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
			this.getMapRetailTradePromotingFlow().put(promotion.getID(), retailTradePromotingFlow);

			logger.info("***********优惠后支付价格为：" + dAmountTmp);
			sbP.append("<p>***********优惠后支付价格为：" + "(" + dLocalbalance + " - " + +promotion.getExcecutionAmount() + ")" + " + " + (getOriginalTotalAmount(rt, dbName) - dLocalbalance) + " = " + "<span class=\"bg-danger\">" + dAmountTmp
					+ "</span> 。</p>");
			setPromoted(true);
		} else {// 小于阀值
			logger.info("此次购买指定商品总价为：" + dLocalbalance + "活动不满足活动" + promotion.getName());
			sbP.append("此次购买指定商品总价为：" + dLocalbalance + "活动不满足活动" + promotion.getName() + " 。</p>");
		}
		return dAmountTmp;

	}

	/** 对全场商品，计算零售单经过促销promotion计算后的优惠 */
	@Override
	protected double promoteAllCommodities(RetailTrade rt, final Promotion promotion, String dbName) {
		double fAmountTmp = rt.getAmount(); // 满足条件的商品总和 暂时设置为原价
		double fLocalbalance = rt.getAmount();
		logger.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + fLocalbalance);
		sbP.append("<p>******此次活动名称为：<span class=\"text-warning\">" + promotion.getName() + "</span>，  满足此优惠活动的商品价格之和为：<span class=\"text-primary\">" + fLocalbalance + "</span> 。</p>");
		if (fLocalbalance >= promotion.getExcecutionThreshold()) {// 大于阙值
			fAmountTmp = GeneralUtil.sub(fLocalbalance, promotion.getExcecutionAmount());
			
			StringBuilder promotingFlow = new StringBuilder();
			promotingFlow.append("参与此次计算的商品总价为： " + fLocalbalance + "\t")//
					.append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "减" + promotion.getExcecutionAmount() + "\t")//
					.append("此次计算后的应收款为： " + fLocalbalance + " - " + promotion.getExcecutionAmount() + " = " + fAmountTmp + "\t");//
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setPromotionID(promotion.getID());
			retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
			this.getMapRetailTradePromotingFlow().put(promotion.getID(), retailTradePromotingFlow);
			
			setPromoted(true);
			logger.info("*********************优惠后支付价格为：" + fAmountTmp);
			sbP.append("<p>*********************优惠后支付价格为：" + fLocalbalance + " - " + promotion.getExcecutionAmount() + " = " + "<span class=\"bg-danger\">" + fAmountTmp + "</span> 。</p>");
		} else {// 小于阙值
			logger.info("此次购买全场商品总价为：" + fLocalbalance + "条件不满足活动" + promotion.getName());
			sbP.append("<p>此次购买全场商品总价为：" + fLocalbalance + "条件不满足活动" + promotion.getName() + " 。</p>");
		}
		return fAmountTmp;
	}

	@Override
	public void calculateReturnPrice(RetailTrade rt, double dBestAmount, EnumPromotionScope eps) {
		// 判断是否是指定促销活动
		switch (eps) {
		case EPS_SpecifiedCommodities:
			dBestAmount = 0.000000d;
			// 将集合分为两种集合 一种指定促销活动商品集合 二种没参与活动的商品 集合 暂时不需要 不参与活动就是原价
			// 拿到最优惠活动的 所有指定商品 及数量
			List<RetailTradeCommodity> retailTradeCommodityList = getMapCommodity().get(promotionBest.getID());
			// 判断此次活动为必定为满减
			for (RetailTradeCommodity rtc : retailTradeCommodityList) {
				// fBestAmount 刚刚传入的金额组成（参与指定促销活动优惠后的价格，加上，没有参与过活动商品的价格）
				// 遍历出参与指定促销活动的 商品总金额
				dBestAmount = GeneralUtil.sum(dBestAmount, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
			}
			for (RetailTradeCommodity rtc : retailTradeCommodityList) {
				// 原来的计算方式是 rtc.getPriceOriginal() * rtc.getNO() / dBestAmount * (dBestAmount - promotionBest.getExcecutionAmount()) / rtc.getNO();
                // 现改为 rtc.getPriceOriginal() * rtc.getNO() * (dBestAmount - promotionBest.getExcecutionAmount()) / (dBestAmount * rtc.getNO())
                // 原因是 按照先乘后除的计算顺序可以减少计算误差
				rtc.setPriceReturn(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()), 
						GeneralUtil.sub(dBestAmount, promotionBest.getExcecutionAmount())), GeneralUtil.mul(dBestAmount, rtc.getNO()), 6));
			}
			break;
		default:
			// 参加过指定促销活动 fBestAmount 为全局优惠后的价格（不需要知道上一次有没有参与过活动）
			double dAmountTmp = 0.000000d;
			// 如果上一个的指定活动为满减(需不需要知道上一个活动是什么 ？？？？？)
			// 这一次进这个说明是全局满减优惠
			dAmountTmp = GeneralUtil.sum(dBestAmount, promotionAllBest.getExcecutionAmount());
			for (Object o : rt.getListSlave1()) {
				RetailTradeCommodity rtc = (RetailTradeCommodity) o;
				System.out.println(rtc.getPriceOriginal());
				// 如果该商品有参与过指定活动，那么它的退货价是经过一轮计算的，不等于原价。这时再算经过全场活动优惠的退货价就不能用原价来算。
				// rtc.setPriceReturn(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()) / dAmountTmp, GeneralUtil.div(dBestAmount, rtc.getNO(), 6)));//
				// rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate((rtc.getPriceReturn() * rtc.getNO() / dAmountTmp) * dBestAmount / rtc.getNO())));
				// 原来计算方式是 (rtc.getPriceReturn() * rtc.getNO() / dAmountTmp) * dBestAmount / rtc.getNO()
				// 现改为 (rtc.getPriceReturn() * rtc.getNO() * dBestAmount) / (dAmountTmp * rtc.getNO())
				// 原因是 按照先乘后除的计算顺序可以减少计算误差
				rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dBestAmount), GeneralUtil.mul(dAmountTmp, rtc.getNO()), 6))));
				System.out.println(rtc.getPriceReturn());
				System.out.println("--------");
			}

			break;
		}
	}

}
