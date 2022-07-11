package com.bx.erp.event.UI;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class VipSQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public VipSQLiteEvent() {

    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Vip_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("Vip已经在本地插入临时数据，触发网络请求...");
                    setStatus(EnumEventStatus.EES_SQLite_Done);
                    if (httpBO != null) {
                        httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1());
                    }
                } else {
                    log.info("Vip在本地插入临时数据失败！不会触发网络请求...");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_Vip_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("Vip已经在本地完成设置SyncDatetime和SyncType，触发网络请求...");
                    setStatus(EnumEventStatus.EES_SQLite_Done);
                    if (httpBO != null) {
                        httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1());
                    }
                } else {
                    log.info("Vip设置SyncDatetime和SyncType失败！不会触发网络请求...");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_Vip_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("delete Vip成功");
                } else {
                    log.info("delete Vip失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Vip_CreateReplacerAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("create Vip成功");
                } else {
                    log.info("create Vip失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setEventProcessed(true);
                setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Vip_UpdateByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("update Vip成功");
                } else {
                    log.info("update Vip失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            case ESET_Vip_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("retrieveN Vip成功");
                } else {
                    log.info("retrieveN Vip失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setEventProcessed(true);
                setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Vip_BeforeDeleteAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("Vip已经在本地完成设置SyncDatetime和SyncType，触发网络请求...");
                    if (httpBO != null) {
                        httpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1());
                    }
                } else {
                    log.info("Vip设置SyncDatetime和SyncType失败！不会触发网络请求...");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                break;
            case ESET_Vip_UpdateByServerDataNAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("RetrieveNC服务器返回的Vip，进行更新本地SQLite已成功完成!");
                } else {
                    log.info("RetrieveNC服务器返回的Vip，进行更新本地SQLite未成功!");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_Vip_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("RetrieveNAsync vip成功！");
                } else {
                    log.info("RetrieveNAsync vip失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Vip_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("RetrieveNAsync vip成功！");
                } else {
                    log.info("RetrieveNAsync vip失败！");
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
