package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.ErrorInfo;

import org.junit.Assert;

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
            Assert.assertTrue("同步coupon失败", false);
        }

        long lTimeOut = 60;
        while (couponHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            if (couponHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && couponHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("服务器返回的Coupon解析失败！", false);
                break;
            }
            Thread.sleep(1000);
        }

        lTimeOut = 10;
        while (couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if (couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && couponSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                Assert.assertTrue("coupon插入本地数据库失败", false);
                break;
            }
            Thread.sleep(1000);
        }
    }


}
