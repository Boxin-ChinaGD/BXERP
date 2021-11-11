package com.bx.erp.event.UI;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class SmallSheetSQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public SmallSheetSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_SmallSheet_CreateMasterSlaveAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主表和从表都已经插入成功，触发网络同步请求...");
                    if (httpBO != null) {
                        if (!httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, getTmpMasterTableObj())) {
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_InvalidSession;
                            break;
                        }
                    } else {
                        log.info("httpBO为null，无法发送请求");
                    }
                } else {
                    log.info("主表和从表插入失败!不会触发网络同步请求。");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                break;
            case ESET_SmallSheet_UpdateMasterSlaveAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                    log.info("主表和从表都已经修改成功，触发网络同步请求...");
                    if (httpBO != null) {
                        if (!httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, getTmpMasterTableObj())) {
                            lastErrorCode = httpBO.getHttpEvent().getLastErrorCode();
                            lastErrorMessage = httpBO.getHttpEvent().getLastErrorMessage();
                            break;
                        }
                    }
                } else {
                    log.info("SQLite修改小票格式失败!不会触发网络同步请求");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            case ESET_SmallSheet_CreateReplacerAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功同步了服务器返回的同步数据块(C型)");
                } else {
                    log.info("本地SQLite同步服务器返回的同步数据块(C型)失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_SmallSheet_CreateReplacerNAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经全部成功同步了服务器返回的N个同步数据块");
                } else {
                    log.info("本地SQLite同步服务器返回的N个同步数据块失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_SmallSheet_DeleteMasterSlaveAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主表和从表都已经删除成功");
                } else {
                    log.info("本地SQLite删除主表和从表失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在删除小票格式之前，设置SyncType和SyncDatetime成功，触发网络同步请求...");
                    if (httpBO != null) {
                        if (!httpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1())) {
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_InvalidSession;
                            break;
                        }
                    }
                } else {
                    log.info("本地SQLite删除主表和从表失败,不会再触发网络请求");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheet_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主从表的数据都已经根据服务器返回的数据(CUD)同步成功...");
                } else {
                    log.info("本地SQLite同步小票格式(CUD)失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setEventProcessed(true);
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("update操作之后，主从表都已经成功更新了数据库返回的数据...");
                } else {
                    log.info("update操作之后，主从表更新数据库返回的数据失败...");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_SmallSheetFrame_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地创建临时SmallSheet成功，不会上传到服务器！");
                } else {
                    log.info("在本地创建临时SmallSheet失败，不会上传到服务器！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheetFrame_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地创建U型临时SmallSheet成功，不会上传到服务器！");
                } else {
                    log.info("在本地创建U型临时SmallSheet失败，不会上传到服务器！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheetFrame_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地创建D型临时SmallSheet成功，不会上传到服务器");
                } else {
                    log.info("在本地创建D型临时SmallSheet失败，不会上传到服务器");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheetFrame_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地创建C型临时SmallSheet成功，不会上传到服务器");
                } else {
                    log.info("在本地创建C型临时SmallSheet失败，不会上传到服务器");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheetFrame_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地查询临时SmallSheet成功，不会上传到服务器");
                } else {
                    log.info("在本地查询临时SmallSheet失败，不会上传到服务器");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_SmallSheet_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("将SmallSheet同步到本地成功");
                } else {
                    log.info("将SmallSheet同步到本地失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            default:
                log.info("为定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
