package com.bx.erp.event.UI;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class BXConfigGeneralSQLiteEvent extends BaseSQLiteEvent{
    private Logger log = Logger.getLogger(this.getClass());

    public BXConfigGeneralSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功同步BXConfigGeneral！");
                } else {
                    log.info("同步BXConfigGeneral失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
    }
}
