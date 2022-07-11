
package com.bx.erp.event.UI;

import com.bx.erp.event.BaseEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class CommoditySQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public CommoditySQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Commodity_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入commodity的List!");
                } else {
                    log.info("插入commodity的List失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Commodity_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入commodity");
                } else {
                    log.info("插入commodity失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Commodity_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功更新commodity");
                } else {
                    log.info("更新commodity失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Commodity_Retrieve1Async:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieve1");
                } else {
                    log.info("retrieve1失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Commodity_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieveN");
                } else {
                    log.info("retrieveN失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Commodity_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的需要同步的Commodity数据同步成功...");
                } else {
                    log.info("本地SQLite同步Commodity失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Commodity_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的Commodity数据同步成功...");
                } else {
                    log.info("本地SQLite同步Commodity失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Commodity_UpdateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功根据服务器返回的数据更新商品库存...");
                } else {
                    log.info("本地SQLite更新商品库存失败...");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
    }
}