package com.bx.erp.event;

import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;


public class PromotionSQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public PromotionSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Promotion_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Promotion的List!");
                } else {
                    log.info("插入Promotion的List失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Promotion_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Promotion");
                } else {
                    log.info("插入Promotion失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setEventProcessed(true);
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_Promotion_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的需要同步的Promotion数据同步成功...");
                } else {
                    log.info("本地SQLite同步Promotion失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Promotion_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功将Promotion数据同步完成...");
                } else {
                    log.info("本地SQLite同步Promotion失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Promotion_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功更新Promotion");
                } else {
                    log.info("更新Promotion失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Promotion_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieveN");
                } else {
                    log.info("retrieveN失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Promotion_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功删除");
                } else {
                    log.info("删除失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
