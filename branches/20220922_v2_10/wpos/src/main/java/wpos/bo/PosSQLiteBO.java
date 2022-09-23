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
import wpos.presenter.PosPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("posSQLiteBO")
@Scope("prototype")
public class PosSQLiteBO extends BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public PosSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public PosSQLiteBO(){

    }
    
    @Resource
    private PosPresenter posPresenter;

    // 只在测试中使用，功能代码不使用
    public void setPosPresenter(PosPresenter posPresenter) {
        this.posPresenter = posPresenter;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Pos_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                if (posPresenter.createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入POS表数据失败!");
                }
                break;
            case ESET_Pos_Retrieve1Async:
                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                if (posPresenter.createObjectAsync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, bm, sqLiteEvent)) {
                    return true;
                }
            default:
                log.info("未定义的事件!");
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
        log.info("正在进行PosSqLiteBO的applyServerDataAsync方法，bm=" + bmNew);
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        return posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNew, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Pos_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
                if (posPresenter.updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("Update Pos失败!");
                }
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
        return posPresenter.refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        return posPresenter.refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }

    @Override
    public boolean retrieve1ASync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosSqLiteBO的retrieve1ASync方法，bm=" + bm);
        return posPresenter.retrieve1ObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    @Override
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {
        return posPresenter.retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    @Override
    public void createTableSync() {
        posPresenter.createTableSync();
    }

    public boolean createReplacerAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosSqLiteBO的createReplacerAsync方法，bm=" + bm);
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        return posPresenter.createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent);
    }
}
