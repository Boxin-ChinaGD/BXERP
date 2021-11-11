package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.promotion.BasePromotion;
import com.bx.erp.utils.DatetimeUtil;

import org.junit.Assert;

import java.util.Date;
import java.util.Random;

public class BasePrommotionTest {
    public static class DataInput {
        private static Promotion promotionInput = null;

        /**
         * 指定商品满减促销
         *
         * @param excecutionThreshold 满足金额
         * @param ExcecutionAmount    减免金额
         * @param CommodityIDs        指定的商品id
         * @return
         * @throws CloneNotSupportedException
         */
        public static final Promotion getCashReducingOfAmountPromotionOnSpecifiedCommodity(Double excecutionThreshold, Double ExcecutionAmount, String CommodityIDs) throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场满" + excecutionThreshold + "减" + ExcecutionAmount + " " + System.currentTimeMillis() % 100000);
            promotionInput.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            promotionInput.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(excecutionThreshold);
            promotionInput.setExcecutionAmount(ExcecutionAmount);
            promotionInput.setExcecutionDiscount(1);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            promotionInput.setCommodityIDs(CommodityIDs);
            return (Promotion) promotionInput.clone();
        }

        /**
         * 全场商品满减促销
         *
         * @param excecutionThreshold 满足金额
         * @param ExcecutionAmount    减免金额
         * @return
         * @throws CloneNotSupportedException
         */
        public static final Promotion getCashReducingOfAmountPromotionOnAllCommodity(Double excecutionThreshold, Double ExcecutionAmount) throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场满" + excecutionThreshold + "减" + ExcecutionAmount + " " + System.currentTimeMillis() % 100000);
            promotionInput.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            promotionInput.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(excecutionThreshold);
            promotionInput.setExcecutionAmount(ExcecutionAmount);
            promotionInput.setExcecutionDiscount(1);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            return (Promotion) promotionInput.clone();
        }

        /**
         * 全场满折促销
         *
         * @param excecutionThreshold 满足金额
         * @param ExcecutionDiscount  打几折
         * @return
         * @throws CloneNotSupportedException
         */
        public static final Promotion getDiscountOfAmountPromotionOnAllCommodity(Double excecutionThreshold, Double ExcecutionDiscount) throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("全场满" + excecutionThreshold + "打" + ExcecutionDiscount + "折 " + System.currentTimeMillis() % 100000);
            promotionInput.setType(Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(excecutionThreshold);
            promotionInput.setExcecutionAmount(1);
            promotionInput.setExcecutionDiscount(ExcecutionDiscount);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);

            return (Promotion) promotionInput.clone();
        }

        /**
         * 指定商品满折促销
         *
         * @param excecutionThreshold 满足金额
         * @param ExcecutionDiscount  打几折
         * @param CommodityIDs        指定商品的id
         * @return
         * @throws CloneNotSupportedException
         */
        public static final Promotion getDiscountOfAmountPromotionOnSpecifiedCommodity(Double excecutionThreshold, Double ExcecutionDiscount, String CommodityIDs) throws CloneNotSupportedException {
            Date date = new Date();
            Date dt1 = new Date(date.getTime() + 100000000L);//得到明天的日期
            Date dt2 = new Date(date.getTime() + 200000000L);//得到结束的的日期

            promotionInput = new Promotion();
            promotionInput.setName("指定商品" + CommodityIDs + " 满" + excecutionThreshold + "打" + ExcecutionDiscount + "折 " + System.currentTimeMillis() % 100000);
            promotionInput.setType(Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
            promotionInput.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
            promotionInput.setDatetimeStart(dt1);
            promotionInput.setDatetimeEnd(dt2);
            promotionInput.setExcecutionThreshold(excecutionThreshold);
            promotionInput.setExcecutionAmount(1);
            promotionInput.setExcecutionDiscount(ExcecutionDiscount);
            promotionInput.setScope(BasePromotion.EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
            promotionInput.setStaff(1);
            promotionInput.setPageIndex(String.valueOf(1));
            promotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            promotionInput.setReturnObject(1);
            promotionInput.setCommodityIDs(CommodityIDs);

            return (Promotion) promotionInput.clone();
        }
    }

    /**
     * 模拟网页创建单个促销
     */
    public static Promotion createSyncViaHttp(Promotion promotion, PromotionHttpEvent promotionHttpEvent, PromotionHttpBO promotionHttpBO) throws InterruptedException {
        promotion.setReturnObject(1);
        promotionHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue("创建Promotion：" + promotion + "失败!", false);
        }
        long lTimeOut = 60;
        while (promotionHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (promotionHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && promotionHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("服务器返回的Promotion解析失败！", false);
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("创建Promotion：" + promotion + "超时！", false);
        }
        Promotion p = (Promotion) promotionHttpEvent.getBaseModel1();
        Assert.assertTrue("服务器返回的创建对象是空，或者解析为空！", p != null);
        Date d1 = DatetimeUtil.addDays(new Date(), -1);//得到昨天的日期
        p.setDatetimeStart(d1);
        return p;
    }

}
