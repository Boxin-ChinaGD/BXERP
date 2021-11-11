package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.Barcodes;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.List;

@Component("barcodesSQLiteEvent")
@Scope("prototype")
public class BarcodesSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());
    List<Barcodes> barcodesList;

    public BarcodesSQLiteEvent() {

    }

    public BarcodesSQLiteEvent(List<Barcodes> barcodesList) {
        this.barcodesList = barcodesList;
    }

    public List<Barcodes> getBarcodesList() {
        return barcodesList;
    }

    public void setBarcodesList(List<Barcodes> barcodesList) {
        this.barcodesList = barcodesList;
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);
        switch (eventTypeSQLite) {
            case ESET_Barcodes_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Barcodes的List!");
                } else {
                    log.info("插入Barcodes的List失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Barcodes_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功插入Barcodes");
                } else {
                    log.info("插入Barcodes失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Barcodes_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经根据服务器返回的需要同步的Barcodes数据同步成功...");
                } else {
                    log.info("本地SQLite同步Barcodes失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_Barcodes_RefreshByServerDataAsyncC_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经将所有的Barcodes数据同步完成...");
                } else {
                    log.info("本地SQLite同步Barcodes失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                setEventProcessed(true);
                break;
            case ESET_Barcodes_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功更新Barcodes");
                } else {
                    log.info("更新Barcodes失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    status = EnumEventStatus.EES_SQLite_Done;
                }
                setEventProcessed(true);
                break;
            case ESET_Barcodes_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功retrieveN");
                } else {
                    log.info("retrieveN失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            case ESET_Barcodes_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("成功delete");
                } else {
                    log.info("delete失败!");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                status = EnumEventStatus.EES_SQLite_Done;
                setEventProcessed(true);
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
    }
}
