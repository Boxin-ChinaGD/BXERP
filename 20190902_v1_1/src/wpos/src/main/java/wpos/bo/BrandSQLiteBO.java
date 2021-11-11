
package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.presenter.BasePresenter;
import wpos.presenter.BrandPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("brandSQLiteBO")
@Scope("prototype")
public class BrandSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public BrandSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public BrandSQLiteBO(){
    }

    @Resource
    private BrandPresenter brandPresenter;

    // 只在测试中使用，功能代码中不能使用
    public void setBrandPresenter(BrandPresenter brandPresenter) {
        this.brandPresenter = brandPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行BrandSQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Brand_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_CreateAsync);
                if (brandPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入品牌表数据失败!");
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
        log.info("正在执行BrandSQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Brand_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_UpdateAsync);
                if (brandPresenter.updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改品牌主从表失败");
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
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        return brandPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Brand_RefreshByServerDataAsync_Done);
        return brandPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        List<?> list = brandPresenter.retrieveNObjectSync(iUseCaseID, bm);
        return list;
    }

    @Override
    public void createTableSync() {
        brandPresenter.createTableSync();
    }
}
