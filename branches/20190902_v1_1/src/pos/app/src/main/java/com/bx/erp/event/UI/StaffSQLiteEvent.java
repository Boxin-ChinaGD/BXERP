package com.bx.erp.event.UI;

import com.bx.erp.helper.Constants;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;

public class StaffSQLiteEvent extends BaseSQLiteEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public StaffSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Staff_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("Staff数据都已经根据服务器返回的数据(CUD)同步成功...");
                } else {
                    log.info("本地SQLite同步staff(CUD)失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_Staff_RetrieveResigned:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功删除服务器已离职的员工...");
                } else {
                    log.info("删除服务器已离职的员工失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
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
