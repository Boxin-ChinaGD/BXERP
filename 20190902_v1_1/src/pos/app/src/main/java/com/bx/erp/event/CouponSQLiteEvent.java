package com.bx.erp.event;

import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;


public class CouponSQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public CouponSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Coupon_RefreshByServerDataAsyncC:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功将Coupon数据同步完成...");
                } else {
                    log.info("本地SQLite同步Coupon失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            default:
                log.error("CouponSQLiteEvent 未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
