package com.test.bx.app.util;

import com.base.BaseAndroidTestCase;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.wx.coupon.WxCoupon;
import com.bx.erp.model.wx.coupon.WxCouponDetail;
import com.bx.erp.model.wx.coupon.WxCouponDetailPartition;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.WXPayUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXPayUtilTest extends BaseAndroidTestCase {

    @Test
    public void test_formatAmount() {
        caseLog("WxPayUtilTest_formatAmount");
        System.out.println("很大数值：--------" + WXPayUtil.formatAmount(2147483647.00));
        System.out.println("科学计数法：--------" + WXPayUtil.formatAmount(2.147483647E11));
        System.out.println("正常的整数：--------" + WXPayUtil.formatAmount(3.00));
        System.out.println("多位小数：--------" + WXPayUtil.formatAmount(0.024512345543));
        System.out.println("负数-------*" + WXPayUtil.formatAmount(-1.231));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmount(5.23125155455511));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmount(0.01551));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmount(5.68));
        System.out.println("正整数--------" + WXPayUtil.formatAmount(1));
    }

    @Test
    public void test_formatAmountToPayViaWX() {
        caseLog("WxPayUtilTest_formatAmountToPayViaWX");
        System.out.println("很大数值：--------" + WXPayUtil.formatAmountToPayViaWX(2147483647.00));
        System.out.println("科学计数法：--------" + WXPayUtil.formatAmountToPayViaWX(2.147483647E11));
        System.out.println("正常的整数：--------" + WXPayUtil.formatAmountToPayViaWX(3.00));
        System.out.println("多位小数：--------" + WXPayUtil.formatAmountToPayViaWX(0.024512345543));
        System.out.println("负数-------*" + WXPayUtil.formatAmountToPayViaWX(-1.231));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmountToPayViaWX(5.23125155455511));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmountToPayViaWX(0.01551));
        System.out.println("正数加多位小数--------" + WXPayUtil.formatAmountToPayViaWX(5.68));
        System.out.println("正整数--------" + WXPayUtil.formatAmountToPayViaWX(1));
    }

    @Test
    public void test_sellCase1() {
        caseLog("case1:没有可核销的优惠券");
        double wxPayAmount = 10.000000d;
        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", ((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()) - wxPayAmount) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", null == params.get(WxCoupon.field.getFIELD_NAME_wxCoupon()));
    }

    @Test
    public void test_sellCase2() {
        caseLog("case2:消费金额未达到满减券的起用金额");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponDetailPartitionA = new WxCouponDetailPartition();
        wxCouponDetailPartitionA.setLeast_cost(1100);
        wxCouponDetailPartitionA.setReduce_cost(100);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponDetailPartitionA);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), wxPayAmount) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", null == params.get(WxCoupon.field.getFIELD_NAME_wxCoupon()));
    }

    @Test
    public void test_sellCase3() {
        caseLog("case3:消费10元,优惠券有 (满5-2)，(满11-4),返回出的优惠金额是8元");
        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponDetailPartitionA = new WxCouponDetailPartition();
        wxCouponDetailPartitionA.setLeast_cost(500);
        wxCouponDetailPartitionA.setReduce_cost(200);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponDetailPartitionA);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("1234567");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponDetailPartitionB = new WxCouponDetailPartition();
        wxCouponDetailPartitionB.setLeast_cost(1100);
        wxCouponDetailPartitionB.setReduce_cost(400);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponDetailPartitionB);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("7894566");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 8.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase4() {
        caseLog("case4：消费10元,优惠券有（满11-4），（十折优惠券），返回的优惠金额是10元");
        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1100);
        wxCouponCashADetailPartition.setReduce_cost(400);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(0);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("1234567");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponDiscountA);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", (GeneralUtil.sub(wxPayAmount, (double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()))) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", null == params.get(WxCoupon.field.getFIELD_NAME_wxCoupon()));
    }

    @Test
    public void test_sellCase5() {
        caseLog("case5：消费满10元,优惠券有(满10-3)，满(10-4)，返回的优惠金额是6元");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(400);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(300);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("12345678");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 6.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase6() {
        caseLog("case6:消费满10元,优惠券有(5折优惠券),（9折优惠券）。返回的优惠金额是5块");
        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(50);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("1234567");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(10);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 5.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponDiscountA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase7() {
        caseLog("case7：消费金额满10元，优惠券有(满10-4)，（8折优惠券）。返回的优惠金额是6元");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(400);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(20);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 6.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase8() {
        caseLog("case8：消费金额满10元，优惠券有(满10-4)，（1折优惠券）。返回的优惠金额是1元");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(400);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(90);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 1.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponDiscountB.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase9() {
        caseLog("case9:消费金额满10元，优惠券有(满10-2)，（满10-7），（8折优惠券），（6折优惠券）。返回的优惠金额是3元");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(200);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(700);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("7894566");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(20);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("987894561");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(40);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561186");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 3.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashB.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase10() {
        caseLog("case10:消费金额满10元，优惠券有(满10-1)，（满10-7），（8折优惠券），（1折优惠券）。返回的优惠金额是1元");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(100);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(700);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("7894566");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(20);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("987894561");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(90);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561186");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 1.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponDiscountB.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase11() {
        caseLog("case11:消费金额满10元，优惠券有(满10-1)，（满10-1）。返回的优惠金额是9元");
        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(100);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("1234567");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(100);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("7894566");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 9.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase12() {
        caseLog("case12:消费金额满10元，优惠券有(8折优惠券)，（8折优惠券）。返回的优惠金额是8元");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(20);
        //
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("1234567");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(20);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.000000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 8.000000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponDiscountA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase13() {
        caseLog("case13:消费金额满10.8元，优惠券有(8折优惠券)，（8折优惠券）,(满10-3)，（满10-3）。返回的优惠金额是7.8元");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(20);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("987894561");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(20);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561186");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(300);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("7894566");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(300);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("78945676");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.800000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 7.800000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponCashA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }

    @Test
    public void test_sellCase14() {
        caseLog("case14:消费金额满10.8元，优惠券有(8折优惠券)，（8折优惠券）,(满10-1)，（满10-1）。返回的优惠金额是8.64元");

        WxCoupon wxCouponDiscountA = new WxCoupon();
        WxCouponDetail wxCouponDiscountA_Detail = new WxCouponDetail();
        wxCouponDiscountA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountADetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountADetailPartition.setDiscount(20);
        wxCouponDiscountA_Detail.setWxCouponDetailPartition(wxCouponDiscountADetailPartition);
        //
        wxCouponDiscountA.setWxCouponDetail(wxCouponDiscountA_Detail);
        wxCouponDiscountA.setCode("987894561");

        WxCoupon wxCouponDiscountB = new WxCoupon();
        WxCouponDetail wxCouponDiscountB_Detail = new WxCouponDetail();
        wxCouponDiscountB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Discount.getName());
        //
        WxCouponDetailPartition wxCouponDiscountBDetailPartition = new WxCouponDetailPartition();
        wxCouponDiscountBDetailPartition.setDiscount(20);
        wxCouponDiscountB_Detail.setWxCouponDetailPartition(wxCouponDiscountBDetailPartition);
        //
        wxCouponDiscountB.setWxCouponDetail(wxCouponDiscountB_Detail);
        wxCouponDiscountB.setCode("987894561186");

        WxCoupon wxCouponCashA = new WxCoupon();
        WxCouponDetail wxCouponCashA_Detail = new WxCouponDetail();
        wxCouponCashA_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashADetailPartition = new WxCouponDetailPartition();
        wxCouponCashADetailPartition.setLeast_cost(1000);
        wxCouponCashADetailPartition.setReduce_cost(100);
        wxCouponCashA_Detail.setWxCouponDetailPartition(wxCouponCashADetailPartition);
        //
        wxCouponCashA.setWxCouponDetail(wxCouponCashA_Detail);
        wxCouponCashA.setCode("78945667");

        WxCoupon wxCouponCashB = new WxCoupon();
        WxCouponDetail wxCouponCashB_Detail = new WxCouponDetail();
        wxCouponCashB_Detail.setCard_type(WxCouponDetail.EnumCouponType.ECT_Cash.getName());
        //
        WxCouponDetailPartition wxCouponCashBDetailPartition = new WxCouponDetailPartition();
        wxCouponCashBDetailPartition.setLeast_cost(1000);
        wxCouponCashBDetailPartition.setReduce_cost(100);
        wxCouponCashB_Detail.setWxCouponDetailPartition(wxCouponCashBDetailPartition);
        //
        wxCouponCashB.setWxCouponDetail(wxCouponCashB_Detail);
        wxCouponCashB.setCode("7894566");

        List<WxCoupon> wxCouponList = new ArrayList<WxCoupon>();
        wxCouponList.add(wxCouponCashA);
        wxCouponList.add(wxCouponCashB);
        wxCouponList.add(wxCouponDiscountA);
        wxCouponList.add(wxCouponDiscountB);
        double wxPayAmount = 10.800000d;

        Map<String, Object> params = WXPayUtil.sell(wxCouponList, wxPayAmount);
        Assert.assertTrue("计算出的金额和预期的不一样", GeneralUtil.sub((double) params.get(WxCoupon.field.getFIELD_NAME_minWechatAmount()), 8.640000d) <= BaseModel.TOLERANCE);
        Assert.assertTrue("使用的优惠券码和预期的不一样", wxCouponDiscountA.getCode().equals(((WxCoupon) params.get(WxCoupon.field.getFIELD_NAME_wxCoupon())).getCode()));
    }
}