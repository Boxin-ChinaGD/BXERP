package com.bx.erp.promotion;

import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.PromotionScope;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.bx.erp.util.StringUitl;

public abstract class BasePromotion {
    private Logger log = Logger.getLogger(this.getClass());

    public enum EnumPromotionScope {
        EPS_AllCommodities("All Commodities", 0), EPS_SpecifiedCommodities("Specified Commodities", 1);

        private String name;
        private int index;

        private EnumPromotionScope(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static boolean inBound(int index) {
            if (index < EPS_AllCommodities.getIndex() || index > EPS_SpecifiedCommodities.getIndex()) {
                return false;
            }
            return true;
        }

        public static String getName(int index) {
            for (EnumPromotionScope c : EnumPromotionScope.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * 标识零售单是否经过了促销计算得到了优惠
     */
    protected boolean promoted;

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    // protected final int SPECIAL_OFFER_Involved = 1;
    // 流程
    protected StringBuilder promotionFlow;
    protected StringBuilder sbTableCommodity;
    protected StringBuilder sbP; // HTML的P标签
    // protected StringBuilder commodityPriceTable;
    // protected StringBuilder sbTableInP;
    protected StringBuilder sbPromotionTable;
    protected StringBuilder sbPromotionScopeTable;
    protected static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";

    protected StringBuilder promotionFlowHTML;
    //protected final Calendar cal = Calendar.getInstance();

    //public StringBuilder getPromotionFlowHTML() {return promotionFlowHTML;}

    // public StringBuilder getPromotionflow() {return promotionFlow;}

    /**
     * 保存每个指定促销活动参与的商品，key=Promotion ID， value=参与此促销的零售单商品
     */
    protected Map<Integer, List<RetailTradeCommodity>> mapCommodity = new HashMap<>();

    public Map<Integer, List<RetailTradeCommodity>> getMapCommodity() {
        return mapCommodity;
    }

//    public void setMapCommodity(Map<Integer, List<RetailTradeCommodity>> mapCommodity) {
//        this.mapCommodity = mapCommodity;
//    }

    // 保存每个促销活动 的详细计算过程
    protected Map<Integer, RetailTradePromotingFlow> mapRetailTradePromotingFlow = new HashMap<>();

    public Map<Integer, RetailTradePromotingFlow> getMapRetailTradePromotingFlow() {
        return mapRetailTradePromotingFlow;
    }

    public void setMapRetailTradePromotingFlow(Map<Integer, RetailTradePromotingFlow> mapRetailTradePromotingFlow) {
        this.mapRetailTradePromotingFlow = mapRetailTradePromotingFlow;
    }

    // 保存最优惠的指定促销活动
    protected Promotion promotionBest;

    public Promotion getPromotionBest() {
        return promotionBest;
    }

    public void setPromotionBest(Promotion promotionBest) {
        this.promotionBest = promotionBest;
    }

    // 保存最优惠的全局促销活动
    protected Promotion promotionAllBest;

    public void setPromotionAllBest(Promotion promotionAllBest) {
        this.promotionAllBest = promotionAllBest;
    }

    public Promotion getPromotionAllBest() {
        return promotionAllBest;
    }

    /**
     * 保存此次活动为满减还是满折 还是 优惠卷 或者其他
     */
    protected String promotionType;

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    /**
     * 不算促销不算优惠券的前提下，零售单里的所有的商品的原价的总和
     */
    protected double originalTotalAmount;

    public double getOriginalTotalAmount() {
        return originalTotalAmount;
    }

    public void setOriginalTotalAmount(double originalTotalAmount) {
        this.originalTotalAmount = originalTotalAmount;
    }

    protected RetailTrade retailTrade;

    public RetailTrade getRetailTrade() {
        return retailTrade;
    }

    public void setRetailTrade(RetailTrade retailTrade) throws FileNotFoundException {
        this.retailTrade = retailTrade;
    }

    protected void appendText(StringBuilder sb, String text) {
        sb.append(text);
    }

    /**
     * 准备促销计算的数据，包括
     * 1、商品原零售价
     * 2、应收款总和（未计促销优惠在内）
     */
    public void prePromote() {
        List<RetailTradeCommodity> commodityList = (List<RetailTradeCommodity>) this.getRetailTrade().getListSlave1();
        originalTotalAmount = 0.000000d;
        for (RetailTradeCommodity rtc : commodityList) {
//           -- 原价
//            rtc.setPriceOriginal(commodity.getPriceRetail());
//           -- 退货价 暂时设置为零售价
//            rtc.setPriceReturn(commodity.getPriceRetail());
            originalTotalAmount += rtc.getPriceOriginal() * rtc.getNO();
        }
        // 暂时将原价总和设置为实际支付价格
        this.getRetailTrade().setAmount(originalTotalAmount);
    }

    /**
     * 按照设定的促销活动计算当前零售单的促销优惠，并将计算过程保存在promotionFlow和HTML中。如果计算过程中能确定退货价，则确定之
     *
     * @param rt            本次计算的零售单。它一般是原零售单的副本
     * @param listPromotion 所有当前有效的（不在删除状态）活动的（在促销时间中）促销活动
     * @param eps           促销范围：指定的商品、全场。
     */
    public void promote(RetailTrade rt, EnumPromotionScope eps, final List<BaseModel> listPromotion) {
        Promotion proBestPromotion = null;
        double dBestAmount = rt.getAmount();
        double dAmountTmp = 0.000000d;
        Date nowData = new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference);
        if (listPromotion == null || listPromotion.size() == 0) {
            return;
        }
        for (Object o : listPromotion) {
            final Promotion promotion = (Promotion) o;
            if (isTargetPromotion(promotion) && DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), nowData, promotion.getDatetimeEnd())) {
                // ...采用promotion计算this.retailTrade得出proBestPromotion和fBestAmount
                switch (eps) {
                    case EPS_SpecifiedCommodities:
                        // 判断是全部商品还是指定商品参与的促销
                        if (promotion.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
                            dAmountTmp = promoteSpecifiedCommodities(rt, promotion);
                        }
                        break;
                    default: // EPS_AllCommodities
                        if (promotion.getScope() == EnumPromotionScope.EPS_AllCommodities.getIndex()) {
                            dAmountTmp = promoteAllCommodities(rt, promotion);
                        }
                        break;
                }
                // 记录最优惠的活动和售价
                if (promoted && dAmountTmp < dBestAmount) {
                    proBestPromotion = promotion;
                    dBestAmount = dAmountTmp;
                }
            }
        }
        if (!promoted) {
            log.info("范围为" + eps.toString() + "时没有找到有" + getPromotionType() + "的促销活动或该促销活动已删除！");
        } else {
            // 有参与过活动mn
            if (proBestPromotion.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
                // 只保存指定促销活动
                promotionBest = proBestPromotion;
            }
            if (proBestPromotion.getScope() == EnumPromotionScope.EPS_AllCommodities.getIndex()) {
                // 只保存全局促销活动
                promotionAllBest = proBestPromotion;
            }
            rt.setAmount(dBestAmount);
            calculateReturnPrice(rt, dBestAmount, eps);
        }
    }

    protected abstract double promoteAllCommodities(RetailTrade rt, final Promotion promotion);

    protected abstract double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion);

    /**
     * 计算出此单(rt)对应的商品退货价格
     */
    public void calculateReturnPrice(RetailTrade rt, double fBestAmount, EnumPromotionScope eps) {
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
        for (RetailTradeCommodity rtc : retailTradeCommodityList) {
            // 原来的计算过程是 rtc.getPriceReturn() * rtc.getNO() / getOriginalTotalAmount() * fBestAmount / rtc.getNO()，即rtc.getPriceReturn() / getOriginalTotalAmount() * fBestAmount
            // 现改为 (rtc.getPriceReturn() * fBestAmount) / getOriginalTotalAmount()
            // 原因是，按照先乘后除的计算顺序可以减少计算误差
            double dReturnPrice = GeneralUtil.div(GeneralUtil.mul(rtc.getPriceReturn(), fBestAmount), getOriginalTotalAmount(), 6);
            rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(dReturnPrice)));
        }
    }


    public BasePromotion() {
        promoted = false;
        promotionFlow = new StringBuilder();
        promotionFlowHTML = new StringBuilder();
        sbTableCommodity = new StringBuilder();
        sbP = new StringBuilder();
        // sbTableInP = new StringBuilder();
        sbPromotionTable = new StringBuilder();
        sbPromotionScopeTable = new StringBuilder();
        // commodityPriceTable = new StringBuilder();
    }

    /**
     * 生成促销Promotion p的表格
     *
     * @param p 促销的主表信息
     */
    protected void generatePromotionTable(final Promotion p) {
    }

    /**
     * 生成促销PromotionScope ps的表格
     *
     * @param piSc 促销的从表信息
     */
    protected void generatePromotionScopeTable(List<PromotionScope> piSc) {
    }

    /**
     * 生成零售单里的所有商品的表格，其中包括所有商品的原零售价的总和
     */
    protected void generateCommodityTable(List<RetailTradeCommodity> retailTradeCommodityList) {
    }

    /**
     * 是否目标类型的促销
     */
    public abstract boolean isTargetPromotion(final Promotion pt);
}