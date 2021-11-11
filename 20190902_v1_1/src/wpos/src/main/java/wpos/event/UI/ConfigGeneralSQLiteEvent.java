package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("configGeneralSQLiteEvent")
@Scope("prototype")
public class ConfigGeneralSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public ConfigGeneralSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功同步ConfigGeneral！");
                } else {
                    log.info("同步ConfigGeneral失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                setEventProcessed(true);
                break;
            case ESET_ConfigGeneral_UpdateAsync:
                setStatus(EnumEventStatus.EES_SQLite_Done);//用于Presenter测试
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    if (httpBO != null) {
                        if (!httpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1())) {
                            lastErrorCode = ErrorInfo.EnumErrorCode.EC_InvalidSession;
                            break;
                        }
                    }
                } else {
                    log.info("Update ConfigGeneral前设置SyncDatetime失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            case ESET_ConfigGeneral_UpdateByServerDataAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功Update ConfigGeneral！");
                } else {
                    log.info("Update ConfigGeneral失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
    }
}
