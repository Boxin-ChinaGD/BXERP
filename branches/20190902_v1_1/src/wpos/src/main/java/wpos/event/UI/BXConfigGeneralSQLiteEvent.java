package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("bXConfigGeneralSQLiteEvent")
@Scope("prototype")
public class BXConfigGeneralSQLiteEvent extends BaseSQLiteEvent{
    private Log log = LogFactory.getLog(this.getClass());

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
