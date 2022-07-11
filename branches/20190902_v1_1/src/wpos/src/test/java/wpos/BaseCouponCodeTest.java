package wpos;

import org.testng.Assert;
import wpos.bo.BaseHttpBO;
import wpos.bo.CouponCodeHttpBO;
import wpos.event.BaseEvent;
import wpos.event.CouponCodeHttpEvent;
import wpos.model.CouponCode;
import wpos.model.ErrorInfo;

import java.util.List;

public class BaseCouponCodeTest {
    /**
     * 查询会员所拥有的优惠券
     * return List<CouponCode>:返回所拥有的优惠券的集合
     */
    public static List<CouponCode> retrieveNSync(CouponCode couponCode, CouponCodeHttpEvent couponCodeHttpEvent, CouponCodeHttpBO couponCodeHttpBO) throws InterruptedException {
        couponCodeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (!couponCodeHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, couponCode)) {
            Assert.assertSame(couponCodeHttpBO.getHttpEvent().getLastErrorCode(), ErrorInfo.EnumErrorCode.EC_NoError, "查找会员的优惠券失败，原因：" + couponCodeHttpBO.getHttpEvent().getLastErrorMessage());
        }

        int lTimeOut = 60;
        while (couponCodeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (couponCodeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && couponCodeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.fail("服务器返回的coupon解析失败！");
                break;
            }
            Thread.sleep(1000);
        }
        if (lTimeOut <= 0) {
            Assert.fail("查询CouponCode ：" + couponCode + "超时！");
        }

       return (List<CouponCode>) couponCodeHttpEvent.getListMasterTable();
    }

}
