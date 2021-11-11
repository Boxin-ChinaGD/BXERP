package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("retailTradeAggregationSQLiteEvent")
@Scope("prototype")
public class RetailTradeAggregationSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public RetailTradeAggregationSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_RetailTradeAggregation_CreateAsync:
                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    System.out.println("收银汇总数据已经成功插入数据表!");
                } else {
                    log.error("收银汇总数据插入数据表失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                break;
            case ESET_RetailTradeAggregation_UpdateAsync:
                if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.error("收银汇总更新SQLITE失败！");
                }
                break;
            case ESET_RetailTradeAggregation_CreateAsync_HttpDone:
                if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.error("收银汇总上传到服务器后，删除本地收银汇总失败");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
        status = EnumEventStatus.EES_SQLite_Done;
        isEventProcessed = true;
    }
}
