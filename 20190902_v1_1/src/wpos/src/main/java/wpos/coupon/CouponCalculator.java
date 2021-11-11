package wpos.coupon;


import wpos.model.Commodity;
import wpos.model.Coupon;
import wpos.model.CouponScope;
import wpos.model.RetailTrade;
import wpos.utils.GeneralUtil;

import java.util.List;

public class CouponCalculator {
    /**
     * 计算零售单使用优惠券后的应付款,并设置rtSelling中优惠掉的金额
     */
    public static double calculateAmountUsingCoupon(Coupon couponSelected, double amountBeforeUsingCoupon, List<Commodity> commListToSell, RetailTrade rtSelling) {
        double amountToPay = 0.00000000d;
        if (couponSelected.getType() == Coupon.EnumCouponCardType.ECCT_CASH.getIndex()) {
            amountToPay = GeneralUtil.sub(amountBeforeUsingCoupon, couponSelected.getReduceAmount());
            rtSelling.setCouponAmount(couponSelected.getReduceAmount());
        } else {
            // 对于折扣券.如果有指定商品范围。那么仅是对范围内的商品总价进行打折
            if (couponSelected.getListSlave1() != null && couponSelected.getListSlave1().size() > 0) {
                double scopeCommodityTotal = 0.000000d;
                List<CouponScope> couponScopes = (List<CouponScope>) couponSelected.getListSlave1();
                for (int j = 0; j < commListToSell.size(); j++) {
                    Commodity commodity = commListToSell.get(j);
                    for (int k = 0; k < couponScopes.size(); k++) {
                        CouponScope couponScope = couponScopes.get(k);
                        if (commodity.getID() == couponScope.getCommodityID()) {
                            scopeCommodityTotal = GeneralUtil.sum(scopeCommodityTotal, commodity.getSubtotal());
                        }
                    }
                }
                double commodityTotal = GeneralUtil.sub(amountBeforeUsingCoupon, scopeCommodityTotal); //未参与优惠的商品总价
                amountToPay = GeneralUtil.sum(commodityTotal, GeneralUtil.mul(scopeCommodityTotal, couponSelected.getDiscount()));
            } else {
                amountToPay = GeneralUtil.mul(amountBeforeUsingCoupon, couponSelected.getDiscount());
            }
            rtSelling.setCouponAmount(GeneralUtil.sub(amountBeforeUsingCoupon, amountToPay));
        }
        return amountToPay;
    }
}
