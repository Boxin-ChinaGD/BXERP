package com.bx.erp.action.trade.promotion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.GeneralUtil;

/** 折扣促销基类 */
public abstract class Discount extends BasePromotion {
	private Log logger = LogFactory.getLog(Discount.class);

	/** 对指定的商品，计算零售单rt经过促销promotion计算后的优惠 */
	@Override
	protected double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion, String dbName) {
		double dLocalBalance = 0.000000d; // 指定商品的售价总和
		double dAmountTmp = rt.getAmount(); // 优惠后的应收款 暂时设置为原价
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
		for (Object o1 : promotion.getListSlave1()) {
			PromotionScope ps = (PromotionScope) o1;
			for (Object o : rt.getListSlave1()) {
				RetailTradeCommodity rtc = (RetailTradeCommodity) o;
				if (rtc.getCommodityID() == ps.getCommodityID()) {
					dLocalBalance = GeneralUtil.sum(dLocalBalance, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
					listRetailTradeCommodity.add(rtc);
					break;
				}
			}
		}
		logger.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalBalance + "，不满足条件的商品之和：" + (getOriginalTotalAmount(rt, dbName) - dLocalBalance) + " 。");
		sbP.append("<p>******此次活动名称为：<span class=\"text-warning\">" + promotion.getName() + "</span>，  满足此优惠活动的商品价格之和为：<span class=\"text-primary\">" + dLocalBalance + "</span>，  不满足条件的商品之和："
				+ (getOriginalTotalAmount(rt, dbName) - dLocalBalance + " 。</p>"));
		if (dLocalBalance >= promotion.getExcecutionThreshold()) { // >=阀值
			dAmountTmp = GeneralUtil.sum(GeneralUtil.mul(dLocalBalance, promotion.getExcecutionDiscount()), GeneralUtil.sub(getOriginalTotalAmount(rt, dbName), dLocalBalance));
			this.getMapCommodity().put(promotion.getID(), listRetailTradeCommodity);
			// 保存计算过程
			StringBuilder promotingFlow = new StringBuilder();
			promotingFlow.append("此单原价为： " + getOriginalTotalAmount(rt, dbName) + "\t")//
					.append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "打" + promotion.getExcecutionDiscount() + "折\t")//
					.append("满足此活动的商品零售价之和为： " + dLocalBalance + "\t")
					.append("此次计算后的应收款为： " + dLocalBalance + " * " + (promotion.getExcecutionDiscount()) + " + (" + getOriginalTotalAmount(rt, dbName) + " - " + dLocalBalance + ") = " + (dAmountTmp) + "\t");//
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setPromotionID(promotion.getID());
			retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
			this.getMapRetailTradePromotingFlow().put(promotion.getID(), retailTradePromotingFlow);

			setPromoted(true);
			logger.info("***********优惠后支付价格为：" + dAmountTmp);
			sbP.append("<p>***********优惠后支付价格为：" + dLocalBalance + " * " + (promotion.getExcecutionDiscount()) + " + " + (getOriginalTotalAmount(rt, dbName) - dLocalBalance) + " = " + "<span class=\"bg-danger\">" + dAmountTmp
					+ "</span> 。</p>");
		} else {// 小于阀值
			logger.info("此次购买指定商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。");
			sbP.append("<p>此次购买指定商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。</p>");
		}
		return dAmountTmp;
	}

	/** 对全场商品，计算零售单rt经过促销promotion计算后的优惠 */
	@Override
	protected double promoteAllCommodities(RetailTrade rt, final Promotion promotion, String dbName) {
		double dAmountTmp = rt.getAmount(); // 满足条件的商品总和 暂时设置为原价
		double dLocalBalance = rt.getAmount();
		logger.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalBalance + " 。");
		sbP.append("<p>******此次活动名称为：<span class=\"text-warning\">" + promotion.getName() + "</span>，  满足此优惠活动的商品价格之和为：<span class=\"text-primary\">" + dLocalBalance + "</span> 。</p>");
		if (dLocalBalance >= promotion.getExcecutionThreshold()) { // >=阀值
			dAmountTmp = GeneralUtil.mul(dLocalBalance, promotion.getExcecutionDiscount());
			//
			StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("参与此次计算的商品总价为： " + GeneralUtil.formatToCalculate(dLocalBalance) + "\t")//
                    .append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "打" + promotion.getExcecutionDiscount() + "折\t")//
                    .append("此次计算后的应收款为： " + GeneralUtil.formatToCalculate(dLocalBalance) + " * "
                            + promotion.getExcecutionDiscount() + " = "
                            + GeneralUtil.formatToCalculate(dAmountTmp) + "\t");//
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setPromotionID(promotion.getID());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            this.getMapRetailTradePromotingFlow().put(promotion.getID(), retailTradePromotingFlow);
			//
			setPromoted(true);
			logger.info("***********优惠后支付价格为：" + dAmountTmp);
			sbP.append("<p>***********优惠后支付价格为：" + dLocalBalance + " * " + (promotion.getExcecutionDiscount()) + " = " + "<span class=\"bg-danger\">" + dAmountTmp + "</span></p>");
		} else {// 小于阀值
			logger.info("此次购买全场商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。");
			sbP.append("<p>此次购买全场商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。</p>");
		}
		return dAmountTmp;
	}

	@Override
	public void calculateReturnPrice(RetailTrade rt, double dBestAmount, EnumPromotionScope eps) {
		// 判断是否是指定促销活动
		switch (eps) {
		case EPS_SpecifiedCommodities:
			List<RetailTradeCommodity> retailTradeCommodityList = getMapCommodity().get(promotionBest.getID());
			for (RetailTradeCommodity rtc : retailTradeCommodityList) {
				System.out.println(decimalFormatDB.format(rtc.getPriceOriginal() * promotionBest.getExcecutionDiscount()));
				rtc.setPriceReturn(Double.valueOf(decimalFormatDB.format(GeneralUtil.mul(rtc.getPriceReturn(), promotionBest.getExcecutionDiscount()))));
			}
			break;
		default:
			double dAmountTmp = 0.000000d;
			dAmountTmp = GeneralUtil.div(dBestAmount, promotionAllBest.getExcecutionDiscount(), 6);
			for (Object o : rt.getListSlave1()) {
				RetailTradeCommodity rtc = (RetailTradeCommodity) o;
				System.out.println(rtc.getPriceReturn());
				// rtc.setPriceReturn(Double.valueOf(decimalFormatDB.format(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()) / dAmountTmp, dBestAmount), rtc.getNO(), 6))));
				// 原来的计算过程是 rtc.getPriceReturn() * rtc.getNO() / dAmountTmp * dBestAmount / rtc.getNO()
				// 现改为 (rtc.getPriceReturn() * rtc.getNO() * dBestAmount) / (dAmountTmp * rtc.getNO())
				// 原因是，按照先乘后除的计算顺序可以减少计算误差
				rtc.setPriceReturn(Double.valueOf(decimalFormatDB.format(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dBestAmount), GeneralUtil.mul(dAmountTmp, rtc.getNO()), 6))));
				System.out.println(rtc.getPriceReturn());
				System.out.println("-----------------------");
			}
			break;
		}
	}
}
