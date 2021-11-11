package com.bx.erp.promotion;

import com.bx.erp.model.Promotion;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.utils.GeneralUtil;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Discount extends BasePromotion {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 对指定的商品，计算零售单rt经过促销promotion计算后的优惠
     */
    @Override
    protected double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion) {
        double dLocalBalance = 0.000000d; // 指定商品的售价总和
        double dAmountTmp = rt.getAmount(); // 优惠后的应收款 暂时设置为原价
        //  有可能添加指定指定优惠活动没添加指定的商品.

        if (!(promotion.getListSlave1() != null && promotion.getListSlave1().size() > 0)) {
            return dLocalBalance;
        }
        List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
        for (PromotionScope ps : (List<PromotionScope>) promotion.getListSlave1()) {
            for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) rt.getListSlave1()) {
                if (rtc.getCommodityID() == ps.getCommodityID()) {
                    dLocalBalance = GeneralUtil.sum(dLocalBalance, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()));
                    listRetailTradeCommodity.add(rtc);
                }
            }
        }
        log.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalBalance + "，不满足条件的商品之和：" + (getOriginalTotalAmount() - dLocalBalance) + " 。");
        if (dLocalBalance >= promotion.getExcecutionThreshold()) { // >=阀值
            //...创建RetailTradePromotingFlow对象计算方式保存进  retailTradePromoting 的 list属性中
            dAmountTmp = GeneralUtil.sum(GeneralUtil.mul(dLocalBalance, promotion.getExcecutionDiscount()), GeneralUtil.sub(getOriginalTotalAmount(), dLocalBalance));
            log.info("***********优惠后支付价格为：" + dAmountTmp);
            this.getMapCommodity().put(promotion.getID().intValue(), listRetailTradeCommodity);
            // ...保存计算过程
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("此单原价为： " + getOriginalTotalAmount() + "\t")//
                    .append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "打" + promotion.getExcecutionDiscount() + "折\t")//
                    .append("满足此活动的商品零售价之和为： " + dLocalBalance + "\t")
                    .append("此次计算后的应收款为： " + dLocalBalance + " * " + (promotion.getExcecutionDiscount()) + " + (" + getOriginalTotalAmount() + " - " + dLocalBalance + ") = " + (dAmountTmp) + "\t");//
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setPromotionID(promotion.getID().intValue());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            this.getMapRetailTradePromotingFlow().put(promotion.getID().intValue(), retailTradePromotingFlow);
            setPromoted(true);
        } else {// 小于阀值
            log.info("此次购买指定商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。");
        }
        return dAmountTmp;
    }

    /**
     * 对全场商品，计算零售单rt经过促销promotion计算后的优惠
     */
    @Override
    protected double promoteAllCommodities(RetailTrade rt, final Promotion promotion) {
        double dAmountTmp = rt.getAmount(); // 满足条件的商品总和 暂时设置为原价
        double dLocalBalance = rt.getAmount();
        log.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalBalance + " 。");
        if (dLocalBalance >= promotion.getExcecutionThreshold()) { // >=阀值
            //...创建RetailTradePromotingFlow对象计算方式保存进  retailTradePromoting 的 list属性中
            dAmountTmp = GeneralUtil.mul(dLocalBalance, promotion.getExcecutionDiscount());
            // ...保存计算过程
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("参与此次计算的商品总价为： " + GeneralUtil.formatToCalculate(dLocalBalance) + "\t")//
                    .append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "打" + promotion.getExcecutionDiscount() + "折\t")//
                    .append("此次计算后的应收款为： " + GeneralUtil.formatToCalculate(dLocalBalance) + " * "
                            + promotion.getExcecutionDiscount() + " = "
                            + GeneralUtil.formatToCalculate(dAmountTmp) + "\t");//
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setPromotionID(promotion.getID().intValue());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            this.getMapRetailTradePromotingFlow().put(promotion.getID().intValue(), retailTradePromotingFlow);
            setPromoted(true);
            log.info("***********优惠后支付价格为：" + dAmountTmp);
        } else {// 小于阀值
            log.info("此次购买全场商品总价为：" + dLocalBalance + ",不满足促销活动的条件:" + promotion.getName() + " 。");
        }
        return dAmountTmp;
    }

    @Override
    public void calculateReturnPrice(RetailTrade rt, double dBestAmount, EnumPromotionScope eps) {
        // 判断是否是指定促销活动
        switch (eps) {
            case EPS_SpecifiedCommodities:
                List<RetailTradeCommodity> retailTradeCommodityList = getMapCommodity().get(promotionBest.getID().intValue());
                for (RetailTradeCommodity rtc : retailTradeCommodityList) {
                    rtc.setDiscount(promotionBest.getExcecutionDiscount()); // 设置商品的折扣
                    rtc.setPriceReturn(GeneralUtil.mul(rtc.getPriceOriginal(), promotionBest.getExcecutionDiscount()));
                }
                break;
            default:
                double dAmountTmp = 0.000000d;
                dAmountTmp = GeneralUtil.div(dBestAmount, promotionAllBest.getExcecutionDiscount(), 6);
                for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) rt.getListSlave1()) {
                    rtc.setDiscount(promotionAllBest.getExcecutionDiscount());
                    // rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate((rtc.getPriceReturn() * rtc.getNO() / dAmountTmp) * dBestAmount / rtc.getNO())));
                    // 原来的计算过程是 rtc.getPriceReturn() * rtc.getNO() / dAmountTmp * dBestAmount / rtc.getNO()
                    // 现改为 (rtc.getPriceReturn() * rtc.getNO() * dBestAmount) / (dAmountTmp * rtc.getNO())
                    // 原因是，按照先乘后除的计算顺序可以减少计算误差
                    rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dBestAmount), GeneralUtil.mul(dAmountTmp, rtc.getNO()), 6))));
                    System.out.println("折扣计算后的退货价：" + rtc.getPriceReturn());
                }
                break;
        }
    }

}
