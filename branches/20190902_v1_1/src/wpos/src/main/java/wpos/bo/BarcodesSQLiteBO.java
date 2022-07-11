package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BarcodesPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("barcodesSQLiteBO")
@Scope("prototype")
public class BarcodesSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public BarcodesSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public BarcodesSQLiteBO() {

    }

    @Resource
    private BarcodesPresenter barcodesPresenter;

    //此方法在测试代码中使用，不可用于功能代码
    public void setBarcodesPresenter(BarcodesPresenter barcodesPresenter) {
        this.barcodesPresenter = barcodesPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行BarcodesSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Barcodes_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_CreateAsync);
                if (barcodesPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.error("插入商品条形码表数据失败!");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
        return false;
    }


    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行BarcodesSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Barcodes_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_UpdateAsync);
                if (barcodesPresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改商品条形码主从表失败");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsync_Done);
        return barcodesPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RefreshByServerDataAsyncC_Done);
        return barcodesPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        List<?> list = barcodesPresenter.retrieveNObjectSync(iUseCaseID, bm);
        return list;
    }

    @Override
    public void createTableSync() {
        barcodesPresenter.createTableSync();
    }

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行BarcodesSQLiteBO的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Barcodes_RetrieveNAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Barcodes_RetrieveNAsync);
                if (barcodesPresenter.retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("retrieveN失败!");
                }
                break;
            default:
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件!");
        }
        return false;
    }
}
