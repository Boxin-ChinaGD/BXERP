package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("staffSQLiteEvent")
@Scope("prototype")
public class StaffSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

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
