package com.bx.erp.event.UI;

import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class CommodityCategorySQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());
    public CommodityCategorySQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_CommodityCategory_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Category的List!");
                } else {
                    log.info("插入Category的List失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setEventProcessed(true);
                break;
            case ESET_CommodityCategory_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Category");
                } else {
                    log.info("插入Category失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setEventProcessed(true);
                break;
            case ESET_CommodityCategory_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的需要同步的Category数据同步成功...");
                } else {
                    log.info("本地SQLite同步Category失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                }
                break;
            case ESET_CommodityCategory_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功将Category数据同步完成...");
                } else {
                    log.info("本地SQLite同步Category失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_CommodityCategory_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功更新Category");
                } else {
                    log.info("更新Category失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setEventProcessed(true);
                break;
            case ESET_CommodityCategory_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieveN");
                } else {
                    log.info("retrieveN失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
     }
}
