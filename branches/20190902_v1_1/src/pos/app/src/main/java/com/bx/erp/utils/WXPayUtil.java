package com.bx.erp.utils;

import com.bx.erp.model.wx.coupon.WxCoupon;
import com.bx.erp.model.wx.coupon.WxCouponDetail;
import com.bx.erp.model.wx.coupon.WxCouponDetailPartition;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXPayUtil {
    private static Logger log = Logger.getLogger(WXPayUtil.class);

    public static double formatAmount(double amount) {
        try {
            BigDecimal fen = new BigDecimal(100);
            BigDecimal yuan = new BigDecimal(amount);

            return fen.multiply(yuan).doubleValue();
        } catch (Exception e) {
            log.info("金额转换失败，错误信息：" + e.getMessage());
            return 0;
        }
    }

    /**
     * 当amount非常大时，为了不让传递给WX的金额因为科学记数法出现较大误差，要使用本接口而非formatAmount(double amount)
     */
    public static String formatAmountToPayViaWX(double amount) {
        BigDecimal fen = new BigDecimal(100);
        BigDecimal yuan = new BigDecimal(amount);
        //
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2); // 四舍五入到小数点两位

        return df.format(fen.multiply(yuan).doubleValue()).replaceAll(",", "");
    }

    public static Map<String, Object> sell(List<WxCoupon> wxCouponList, double wechatAmount) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(WxCoupon.field.getFIELD_NAME_minWechatAmount(), wechatAmount);
        params.put(WxCoupon.field.getFIELD_NAME_wxCoupon(), null);

        double minWechatAmount = wechatAmount;
        for (int i = 0; i < wxCouponList.size(); i++) {
            WxCoupon wxCoupon = wxCouponList.get(i);
            WxCouponDetail wxCouponDetail = wxCoupon.getWxCouponDetail();
            WxCouponDetailPartition wxCouponDetailPartition = wxCouponDetail.getWxCouponDetailPartition();
            //
            double fen = 100.000000d;
            double yuan = wechatAmount;
            double preferentialAmount = (double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount());
            //优惠券优惠金额都是以分为单位
            if (WxCouponDetail.EnumCouponType.ECT_Cash.getName().equals(wxCouponDetail.getCard_type())) {
                if (wechatAmount >= GeneralUtil.div(wxCouponDetailPartition.getLeast_cost(), fen, 6)) {
                    preferentialAmount = GeneralUtil.sub(yuan, GeneralUtil.div(wxCouponDetailPartition.getReduce_cost(), fen, 6));
                }
            } else {
                //表示打折额度（百分比）。填30就是七折。
                preferentialAmount = GeneralUtil.sub(yuan, GeneralUtil.mul(yuan, GeneralUtil.div(wxCouponDetailPartition.getDiscount(), fen, 6)));
            }
            //
            if (preferentialAmount < minWechatAmount) {
                minWechatAmount = preferentialAmount;
                params.put(WxCoupon.field.getFIELD_NAME_minWechatAmount(), preferentialAmount);
                params.put(WxCoupon.field.getFIELD_NAME_wxCoupon(), wxCoupon);
            }
        }
        return params;
    }
}
