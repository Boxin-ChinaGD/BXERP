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


public abstract class CashReducing extends BasePromotion {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 对指定的商品，计算零售单rt经过促销promotion计算后的优惠
     */
    @Override
    protected double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion) {
        double dLocalbalance = 0.000000d; // 指定商品的售价总和
        double dAmountTmp = rt.getAmount(); // 优惠后的应收款 暂时设置为原价
        //  有可能添加指定指定优惠活动没添加指定的商品
        if (!(promotion.getListSlave1() != null && promotion.getListSlave1().size() > 0)) {
            return dLocalbalance;
        }
        List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<>();
        for (PromotionScope ps : (List<PromotionScope>) promotion.getListSlave1()) {
            for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) rt.getListSlave1()) {
                if (rtc.getCommodityID() == ps.getCommodityID()) {
                    dLocalbalance = dLocalbalance + rtc.getPriceOriginal() * rtc.getNO();
                    // 保存这个零售单商品
                    listRetailTradeCommodity.add(rtc);
                }
            }
        }
        log.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + dLocalbalance + "，  不满足条件的商品之和：" + (getOriginalTotalAmount() - dLocalbalance));
        if (dLocalbalance >= promotion.getExcecutionThreshold()) {
            // 大于阙值
            //...创建RetailTradePromotingFlow对象计算方式保存进  retailTradePromoting 的 list属性中
            dAmountTmp = GeneralUtil.sum(
                    GeneralUtil.sub(dLocalbalance, promotion.getExcecutionAmount()), GeneralUtil.sub(getOriginalTotalAmount(), dLocalbalance)
            );
            log.info("***********优惠后支付价格为：" + dAmountTmp);
            this.getMapCommodity().put(promotion.getID().intValue(), listRetailTradeCommodity);
            // ...保存计算过程
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("此单原价为： " + GeneralUtil.formatToCalculate(getOriginalTotalAmount()) + "\t")//
                    .append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "减" + promotion.getExcecutionAmount() + "\t")//
                    .append("满足此活动的商品零售价之和为： " + GeneralUtil.formatToCalculate(dLocalbalance) + "\t")
                    .append("此次计算后的应收款为： " + GeneralUtil.formatToCalculate(dLocalbalance) + " - "
                            + GeneralUtil.formatToCalculate(promotion.getExcecutionAmount())
                            + " + (" + GeneralUtil.formatToCalculate(getOriginalTotalAmount())
                            + " - " + GeneralUtil.formatToCalculate(dLocalbalance) + ") = "
                            + GeneralUtil.formatToCalculate(dAmountTmp) + "\t");//
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setPromotionID(promotion.getID().intValue());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            this.getMapRetailTradePromotingFlow().put(promotion.getID().intValue(), retailTradePromotingFlow);
            setPromoted(true);
        } else {// 小于阀值
            log.info("此次购买指定商品总价为：" + dLocalbalance + "活动不满足活动" + promotion.getName());
        }
        return dAmountTmp;

    }

    /**
     * 对全场商品，计算零售单经过促销promotion计算后的优惠
     */
    @Override
    protected double promoteAllCommodities(RetailTrade rt, final Promotion promotion) {
        double fAmountTmp = rt.getAmount(); // 满足条件的商品总和 暂时设置为原价
        double fLocalbalance = rt.getAmount();
        log.info("******此次活动名称为：" + promotion.getName() + "，  满足此优惠活动的商品价格之和为：" + fLocalbalance);
        if (fLocalbalance >= promotion.getExcecutionThreshold()) {// 大于阙值
            //...创建RetailTradePromotingFlow对象计算方式保存进  retailTradePromoting 的 list属性中
            fAmountTmp = fLocalbalance - promotion.getExcecutionAmount();
            // ...保存计算过程
            StringBuilder promotingFlow = new StringBuilder();
            promotingFlow.append("参与此次计算的商品总价为： " + fLocalbalance + "\t")//
                    .append("此次促销活动为满：" + promotion.getExcecutionThreshold() + "减" + promotion.getExcecutionAmount() + "\t")//
                    .append("此次计算后的应收款为： " + fLocalbalance + " - " + promotion.getExcecutionAmount() + " = " + fAmountTmp + "\t");//
            RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
            retailTradePromotingFlow.setPromotionID(promotion.getID().intValue());
            retailTradePromotingFlow.setProcessFlow(promotingFlow.toString());
            this.getMapRetailTradePromotingFlow().put(promotion.getID().intValue(), retailTradePromotingFlow);
            setPromoted(true);
            log.info("*********************优惠后支付价格为：" + fAmountTmp);
        } else {// 小于阙值
            log.info("此次购买全场商品总价为：" + fLocalbalance + "条件不满足活动" + promotion.getName());
        }
        return fAmountTmp;
    }

    @Override
    public void calculateReturnPrice(RetailTrade rt, double dBestAmount, EnumPromotionScope eps) {
        // 判断是否是指定促销活动
        switch (eps) {
            // 将集合分为两种集合 一种指定促销活动商品集合  二种没参与活动的商品 集合 暂时不需要  不参与活动就是原价
            case EPS_SpecifiedCommodities:
                dBestAmount = 0.000000d;
                // 拿到最优惠活动的 所有指定商品 及数量
                List<RetailTradeCommodity> retailTradeCommodityList = getMapCommodity().get(promotionBest.getID().intValue());
                // 判断此次活动为必定为满减
//                if(retailTradeCommodityList != null){
                for (RetailTradeCommodity rtc : retailTradeCommodityList) {
                    //fBestAmount 刚刚传入的金额组成（参与指定促销活动优惠后的价格，加上，没有参与过活动商品的价格）
                    // 遍历出参与指定促销活动的 商品总金额
                    dBestAmount = GeneralUtil.sum(
                            dBestAmount, GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO())
                    );
                }
                for (RetailTradeCommodity rtc : retailTradeCommodityList) {
                    // rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate((rtc.getPriceOriginal() * rtc.getNO() / dBestAmount) * ((dBestAmount - promotionBest.getExcecutionAmount()) / rtc.getNO()))));
                    // 原来的计算方式是 rtc.getPriceOriginal() * rtc.getNO() / dBestAmount * (dBestAmount - promotionBest.getExcecutionAmount()) / rtc.getNO();
                    // 现改为 rtc.getPriceOriginal() * rtc.getNO() * (dBestAmount - promotionBest.getExcecutionAmount()) / (dBestAmount * rtc.getNO())
                    // 原因是 按照先乘后除的计算顺序可以减少计算误差
                    rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceOriginal(), rtc.getNO()),
                            GeneralUtil.sub(dBestAmount, promotionBest.getExcecutionAmount())), GeneralUtil.mul(dBestAmount, rtc.getNO()), 6))));
                }
//                }
                break;
            default:
                // 参加过指定促销活动 fBestAmount 为全局优惠后的价格（不需要知道上一次有没有参与过活动）
                double dAmountTmp = 0.000000d;
                // 如果上一个的指定活动为满减(需不需要知道上一个活动是什么 ？？？？？)
                // 这一次进这个说明是全局满减优惠
                dAmountTmp = GeneralUtil.sum(dBestAmount, promotionAllBest.getExcecutionAmount()); // 折扣前金额
                for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) rt.getListSlave1()) {
                    // rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate((rtc.getPriceReturn() * rtc.getNO() / dAmountTmp) * dBestAmount / rtc.getNO())));
                    // 原来计算方式是 (rtc.getPriceReturn() * rtc.getNO() / dAmountTmp) * dBestAmount / rtc.getNO()
                    // 现改为 (rtc.getPriceReturn() * rtc.getNO() * dBestAmount) / (dAmountTmp * rtc.getNO())
                    // 原因是 按照先乘后除的计算顺序可以减少计算误差
                    rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.div(GeneralUtil.mul(GeneralUtil.mul(rtc.getPriceReturn(), rtc.getNO()), dBestAmount), GeneralUtil.mul(dAmountTmp, rtc.getNO()), 6))));
                    System.out.println("满减计算后的退货价：" + rtc.getPriceReturn());
                }

                break;
        }
    }

}
