package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

@Component("brandSQLiteEvent")
@Scope("prototype")
public class BrandSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public BrandSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_Brand_CreateAsync:
                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("品牌数据已经成功插入数据表!触发网络请求上传品牌");
                    if (httpBO != null) {
                        httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, getBaseModel1());
                    } else {
                        setStatus(EnumEventStatus.EES_SQLite_Done);
                    }

                } else {
                    log.info("品牌数据插入数据表失败!不会触发网络请求。");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                break;
            case ESET_Brand_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主从表的数据都已经根据服务器返回的数据(CUD)同步成功...");
                } else {
                    log.info("本地SQLite同步品牌(CUD)失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Brand_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主从表的数据都已经根据服务器返回的数据(CUD)同步成功...");
                } else {
                    log.info("本地SQLite同步品牌(CUD)失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Brand_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地创建N个Brand成功！");
                } else {
                    log.info("在本地创建N个Brand失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Brand_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地删除Brand成功！");
                } else {
                    log.info("在本地删除Brand失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Brand_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("在本地Update Brand成功！");
                } else {
                    log.info("在本地Update Brand失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                setEventProcessed(true);
                break;
            case ESET_Barcodes_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("同步服务器数据 Brand成功！");
                } else {
                    log.error("同步服务器数据 Brand失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Barcodes_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("重置服务器数据 Brand成功！");
                } else {
                    log.error("重置服务器数据 Brand失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
