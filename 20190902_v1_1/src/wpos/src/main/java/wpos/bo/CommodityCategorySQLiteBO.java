package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.CommodityCategoryPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("commodityCategorySQLiteBO")
@Scope("prototype")
public class CommodityCategorySQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public CommodityCategorySQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public CommodityCategorySQLiteBO(){

    }
    
    @Resource
    private CommodityCategoryPresenter commodityCategoryPresenter;

    public void setCommodityCategoryPresenter(CommodityCategoryPresenter commodityCategoryPresenter) {
        this.commodityCategoryPresenter = commodityCategoryPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityCategorySQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_CommodityCategory_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_CreateAsync);
                if (commodityCategoryPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入商品类别表数据失败!");
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
        log.info("正在执行CommodityCategorySQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_CommodityCategory_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_UpdateAsync);
                if (commodityCategoryPresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改商品类别主从表失败");
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
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsync_Done);
        return commodityCategoryPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RefreshByServerDataAsyncC_Done);
        return commodityCategoryPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        List<?> list = commodityCategoryPresenter.retrieveNObjectSync(iUseCaseID, bm);
        return list;
    }

    @Override
    public void createTableSync() {
        commodityCategoryPresenter.createTableSync();
    }

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行CommodityCategorySQLiteBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_CommodityCategory_RetrieveNAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_CommodityCategory_RetrieveNAsync);
                if (commodityCategoryPresenter.retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
