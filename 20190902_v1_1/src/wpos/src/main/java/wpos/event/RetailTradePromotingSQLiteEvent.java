package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component("retailTradePromotingSQLiteEvent")
@Scope("prototype")
public class RetailTradePromotingSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public RetailTradePromotingSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主表和从表都已经插入成功，触发网络同步请求...");
                    if (httpBO != null) {
                        httpBO.createAsyncC(BaseHttpBO.INVALID_CASE_ID, getTmpMasterTableObj());
                    }
                } else {
                    log.info("主表和从表插入失败!不会触发网络同步请求。");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_CreateReplacerAsync_Done:
                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("根据服务器返回的促销活动详细计算过程插入数据库成功！");
                } else {
                    log.info("根据服务器返回的促销活动详细计算插入服务器失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_RefreshMasterSlaveAsyncSQLite_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("已经在本地插入零售单计算过程成功，暂时不会上传到服务器！");
                } else {
                    log.info("在本地插入零售单计算过程失败，不会上传到服务器！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_RetrieveNByIDs:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地根据ID查找临时数据成功！");
                } else {
                    log.info("在本地根据ID查找临时数据失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_CreateReplacerNAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地插入多个服务器返回的RetailTradePromoting成功！");
                } else {
                    log.info("在本地插入多个服务器返回的RetailTradePromoting失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地查找临时RetailTradePromoting成功！");
                } else {
                    log.info("在本地查找临时RetailTradePromoting失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_UpdateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("更新本地RetailTradePromoting成功！");
                } else {
                    log.info("更新本地RetailTradePromoting失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_RetailTradePromoting_Retrieve1ByTradeID:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地查找临时RetailTradePromoting成功！");
                } else {
                    log.info("在本地查找临时RetailTradePromoting失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                break;
        }
    }
}
