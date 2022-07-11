package wpos.model.promotion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wpos.helper.Constants;
import wpos.model.*;
import wpos.utils.GeneralUtil;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PromotionCalculator {
    private Log log = LogFactory.getLog(this.getClass());
    private DiscountOfAmount doa;

    private CashReducingOfAmount coa;


    /**
     * 采用最优的促销方案卖出商品commList，计算出应收款、退货价
     * commList为null或者集合大小等于0的话返回的RetailTrade为null
     *
     * @param commList
     * @param promotionList
     * @param retailTradePromoting :促销详细计算信息必须提前初始化！
     * @return
     */
    public RetailTrade sell(List<Commodity> commList, List<BaseModel> promotionList, RetailTradePromoting retailTradePromoting) {
        //由于生成零售单之前就已经计算好促销，所以此处不用处理status = RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()的零售单，从表也不会为null
        // 开始计算促销优惠
        if (commList == null || commList.size() == 0) {
            return null;
        }

        // 创建一个临时订单
        RetailTrade retailTrade = new RetailTrade();
        retailTrade.setSn(retailTrade.generateRetailTradeSN(Constants.posID));
        retailTrade.setLogo("");
        retailTrade.setRemark("");

        // 创建一个临时订单商品表（普通商品，需要进行促销计算）
        List<RetailTradeCommodity> retailTradeNormalCommodityList = new ArrayList<RetailTradeCommodity>();
        // 创建一个临时订单商品表（多包装、组合商品、服务商品不能参与促销）
        List<RetailTradeCommodity> retailtradeOtherCommodityList = new ArrayList<RetailTradeCommodity>();

        for (Commodity commodity : commList) {
            RetailTradeCommodity rc = new RetailTradeCommodity();
            rc.setCommodityID(commodity.getID());
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodity.getListSlave2().get(0);
            rc.setPriceOriginal(commodityShopInfo.getPriceRetail());
            rc.setNO(commodity.getCommodityQuantity());
            rc.setNOCanReturn(rc.getNO());
            rc.setPriceVIPOriginal(1); // ...
            // 暂时将退货价设置成零售价
            rc.setPriceReturn(commodityShopInfo.getPriceRetail());

            if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
                retailTradeNormalCommodityList.add(rc);
            } else {
                retailtradeOtherCommodityList.add(rc);
            }
        }
        retailTrade.setListSlave1(retailTradeNormalCommodityList);
        // 创建一个保存优惠的商品订单
        RetailTrade calculate = null;
        try {
            calculate = calculateBestCase(retailTrade, promotionList, retailTradePromoting);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (calculate != null) {
            log.info("应付款为：" + calculate.getAmount());
            for (RetailTradeCommodity rtc : (List<RetailTradeCommodity>) calculate.getListSlave1()) {
                for (Commodity commodity : commList) {
                    if (rtc.getCommodityID() == commodity.getID()) {
                        commodity.setAfter_discount(rtc.getPriceReturn());//折后单价
                        commodity.setSubtotal(commodity.getAfter_discount() * commodity.getCommodityQuantity()); // 商品折后总价
                        CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodity.getListSlave2().get(0);
                        commodity.setDiscount(GeneralUtil.sub(commodityShopInfo.getPriceRetail() * commodity.getCommodityQuantity(), commodity.getSubtotal()));// 顾客节省的钱
                    }
                }
            }
        }

        // 计算未参加促销的零售单商品的金额
        double priceReturn = 0.000000d;
        for (RetailTradeCommodity retailTradeCommodity : retailtradeOtherCommodityList) {
            for (Commodity commodity : commList) {
                if (retailTradeCommodity.getCommodityID() == commodity.getID()) {
                    commodity.setAfter_discount(retailTradeCommodity.getPriceReturn());//折后单价
                    commodity.setSubtotal(commodity.getAfter_discount() * commodity.getCommodityQuantity()); // 商品折后总价
                    CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodity.getListSlave2().get(0);
                    commodity.setDiscount(GeneralUtil.sub(commodityShopInfo.getPriceRetail() * commodity.getCommodityQuantity(), commodity.getSubtotal()));// 顾客节省的钱
                }
            }
            priceReturn = GeneralUtil.sum(priceReturn, retailTradeCommodity.getPriceReturn() * retailTradeCommodity.getNO());
        }
        //
        double tempAmount = GeneralUtil.sum(priceReturn, calculate.getAmount());

        if (calculate != null && calculate.getListSlave1().size() != 0) {
            List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) calculate.getListSlave1();
            //
            // 将未参加促销的商品的价格和零售单商品添加到零售单中
            rtcList.addAll(retailtradeOtherCommodityList);
            calculate.setAmount(GeneralUtil.round(tempAmount, 2));
            calculate.setListSlave1(rtcList);
        } else {
            List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) retailTrade.getListSlave1();
            //
            rtcList.addAll(retailtradeOtherCommodityList);
            retailTrade.setAmount(GeneralUtil.round(tempAmount, 2));
            retailTrade.setListSlave1(rtcList);
        }

        //commodity.getAfter_discount();// 折后单价
        //commodity.getCommodityQuantity();// 商品数量
        return (calculate == null || calculate.getListSlave1().size() == 0) ? retailTrade : calculate;
    }


    /**
     * 计算零售单在特定促销活动listPromotion下的结果，包括应付款、退货价，找出最优惠的促销方案给顾客。 计算思路：
     * 1、计算满减或满折后最优惠的结果，促销范围为“指定商品”。 2、把1得到的结果，再计算满减或满折后最优惠的结果，促销范围为“全场”。
     *
     * @param rt                   其商品列表不能为null
     * @param listPromotion
     * @param retailTradePromoting :促销详细计算信息必须提前初始化！
     * @return 计算后的零售单，这个零售单和rt不是同一个对象。
     * @throws FileNotFoundException
     */
    public RetailTrade calculateBestCase(RetailTrade rt, List<BaseModel> listPromotion, RetailTradePromoting retailTradePromoting) throws FileNotFoundException {
        // 参与优惠的促销活动详细计算信息
        List<RetailTradePromotingFlow> retailTradePromotingFlowArrayList = new ArrayList<RetailTradePromotingFlow>();
        doa = new DiscountOfAmount();
        coa = new CashReducingOfAmount();
        // 计算指定商品，输入：rt1 = rt.clone()，得出最优的rt1
        List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
        if (retailTradeCommodityList.size() == 0) {
            return rt;
        }
        RetailTrade rtCoaCloned = (RetailTrade) rt.clone();
        RetailTrade rtDoaCloned = (RetailTrade) rt.clone();

        // 初始化零售单
        coa.setRetailTrade(rtCoaCloned);
        doa.setRetailTrade(rtDoaCloned);
        coa.prePromote();
        doa.prePromote();
        RetailTrade bestRt = null;
        // 计算促销
        coa.promote(rtCoaCloned, BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities, listPromotion);
        doa.promote(rtDoaCloned, BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities, listPromotion);
        coa.setPromoted(false);
        doa.setPromoted(false);
        // 找出优惠最大（应收款最小）的RetailTrade
        if (coa.getMapRetailTradePromotingFlow().size() != 0 || doa.getMapRetailTradePromotingFlow().size() != 0) {
            // 详细计算说明集合大小不等于0说明参与过活动
            if (rtCoaCloned.getAmount() > rtDoaCloned.getAmount()) {
                bestRt = rtDoaCloned;
                // doa 为最优
                if (doa.promotionBest != null) {
                    RetailTradePromotingFlow retailTradePromotingFlows = doa.getMapRetailTradePromotingFlow().get(doa.promotionBest.getID());
                    retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
                }
            } else {
                bestRt = rtCoaCloned;
                // coa 为最优
                if (coa.promotionBest != null) {
                    RetailTradePromotingFlow retailTradePromotingFlows = coa.getMapRetailTradePromotingFlow().get(coa.promotionBest.getID());
                    retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
                }
            }
        } else {
            // 没参与过活动 最优惠随便取一个 反正都相等
            bestRt = rtDoaCloned;
        }
        log.info("/////////////////////////////////////////////进行全场商品满减、满折计算////////////////////////////////////////////");
        rtCoaCloned = (RetailTrade) bestRt.clone();
        rtDoaCloned = (RetailTrade) bestRt.clone();
        //
        coa.promote(rtCoaCloned, BasePromotion.EnumPromotionScope.EPS_AllCommodities, listPromotion);
        doa.promote(rtDoaCloned, BasePromotion.EnumPromotionScope.EPS_AllCommodities, listPromotion);
        coa.setPromoted(false);
        doa.setPromoted(false);
        if (coa.getMapRetailTradePromotingFlow().size() != 0 || doa.getMapRetailTradePromotingFlow().size() != 0) {
            // 详细计算说明集合大小不等于0说明参与过活动

            if (rtCoaCloned.getAmount() > rtDoaCloned.getAmount()) {
                bestRt = rtDoaCloned;
                // doa 为最优
                if (doa.promotionAllBest != null) {
                    RetailTradePromotingFlow retailTradePromotingFlows = doa.getMapRetailTradePromotingFlow().get(doa.promotionAllBest.getID());
                    retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
                }
            } else {
                bestRt = rtCoaCloned;
                // coa 为最优
                if (coa.promotionAllBest != null) {
                    RetailTradePromotingFlow retailTradePromotingFlows = coa.getMapRetailTradePromotingFlow().get(coa.promotionAllBest.getID());
                    retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
                }
            }
        } else {
            // 没参与过活动 最优惠随便取一个 反正都相等
            bestRt = rtDoaCloned;
        }
        retailTradePromoting.setListSlave1(retailTradePromotingFlowArrayList);
        log.info("***此单原价: " + coa.getOriginalTotalAmount());
        log.info("***实际支付价格: " + bestRt.getAmount());
        log.info("--------------------------------------------------------------------");
//        log.info("商品名称\t\t单价\t\t数量\t\t小计\t\t退货价");
//        for (RetailTradeCommodity rCommodity : bestRt.getRetailTradeCommodityList()) {
//            ErrorCode ecOut = new ErrorCode();
//            Commodity commodity = (Commodity) CacheManager.getCache(EnumCacheType.ECT_Commodity).read1(rCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut);
//
//            log.info(commodity.getName() + "\t\t" + //
//                    rCommodity.getPriceOriginal() + "\t\t" + //
//                    rCommodity.getNO() + "\t\t" + //
//                    (rCommodity.getPriceOriginal() * rCommodity.getNO()) + "\t\t" + //
//                    rCommodity.getPriceReturn()//
//            );
//        }
        return bestRt;
    }
}

