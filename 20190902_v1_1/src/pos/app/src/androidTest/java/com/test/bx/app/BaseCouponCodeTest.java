package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.CouponCodeHttpBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CouponCodeHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.ErrorInfo;

import junit.framework.Assert;

import java.util.List;

public class BaseCouponCodeTest {
    /**
     * 查询会员所拥有的优惠券
     * return List<CouponCode>:返回所拥有的优惠券的集合
     */
    public static List<CouponCode> retrieveNSync(CouponCode couponCode, CouponCodeHttpEvent couponCodeHttpEvent, CouponCodeHttpBO couponCodeHttpBO) throws InterruptedException {
        couponCodeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
            Assert.assertTrue("查找会员的优惠券失败，原因：" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage(), couponCodeHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }

        int lTimeOut = 60;
        while (couponCodeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (couponCodeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && couponCodeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("服务器返回的coupon解析失败！", false);
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.assertTrue("查询CouponCode ：" + couponCode + "超时！", false);
        }

       return (List<CouponCode>) couponCodeHttpEvent.getListMasterTable();
    }

}
