package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;
@Component("vipCategorySQLiteEvent")
@Scope("prototype")
public class VipCategorySQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public VipCategorySQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_VipCategory_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入VipCategory的List!");
                } else {
                    log.info("插入VipCategory的List失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                setEventProcessed(true);
                break;
            case ESET_VipCategory_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入VipCategory");
                } else {
                    log.info("插入VipCategory失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                setEventProcessed(true);
                break;
            case ESET_VipCategory_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的需要同步的VipCategory数据同步成功...");
                } else {
                    log.info("本地SQLite同步VipCategory失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_VipCategory_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功将VipCategory数据同步完成...");
                } else {
                    log.info("本地SQLite同步VipCategory失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                setEventProcessed(true);
                break;
            case ESET_VipCategory_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功更新VipCategory");
                } else {
                    log.info("更新VipCategory失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_VipCategory_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieveN");
                } else {
                    log.info("retrieveN失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
