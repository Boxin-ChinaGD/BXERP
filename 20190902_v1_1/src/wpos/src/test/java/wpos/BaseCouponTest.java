package wpos;


import org.junit.Assert;
import wpos.bo.BaseHttpBO;
import wpos.bo.CouponHttpBO;
import wpos.event.BaseEvent;
import wpos.event.CouponHttpEvent;
import wpos.event.CouponSQLiteEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.Coupon;
import wpos.model.ErrorInfo;

public class BaseCouponTest {

    /**
     * 同步服务器的Coupon到本地
     * 调用者必须实现@Subscriber onXXXXEvent()
     */
    public static void retrieveNSyncViaHttp(CouponSQLiteEvent couponSQLiteEvent, CouponHttpBO couponHttpBO, CouponHttpEvent couponHttpEvent) throws InterruptedException {
        Coupon coupon = new Coupon();
        coupon.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        coupon.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        couponSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Coupon_RefreshByServerDataAsyncC);
        couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!couponHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, coupon)) {
            Assert.fail("同步coupon失败");
        }

        long lTimeOut = 60;
        while (couponHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (couponHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && couponHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.fail("服务器返回的Coupon解析失败！");
                break;
            }
            Thread.sleep(1000);
        }

        lTimeOut = 10;
        while (couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if (couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && couponSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.fail("coupon插入本地数据库失败");
                break;
            }
            Thread.sleep(1000);
        }
    }


}
