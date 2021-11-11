package com.bx.erp.event.UI;

import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class CompanySQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public CompanySQLiteEvent() {

    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Company_RetrieveNAsync:
                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地查找所有的Company成功");
                    if (getListMasterTable().size() > 0) {
                        log.info("本地存在Company数据");
                    } else {
                        log.info("本地没有Company数据");
                    }
                } else {
                    log.info("本地查找所有的Company失败");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Company_CreateAsync:
                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地插入Company成功！");
                } else {
                    log.info("本地插入Company失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
    }
}
