package com.bx.erp.bo;


import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

public class PosSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public PosSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosSQLiteBO的createAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Pos_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                if (GlobalController.getInstance().getPosPresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入POS表数据失败!");
                }
                break;
            case ESET_Pos_Retrieve1Async:
                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateAsync);
                if (GlobalController.getInstance().getPosPresenter().createObjectAsync(BaseSQLiteBO.CASE_POS_Retrieve1ForIdentity, bm, sqLiteEvent)) {
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
        return GlobalController.getInstance().getPosPresenter().createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNew, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PosSQLiteBO的updateAsync，bm=" + (bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Pos_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_UpdateAsync);
                if (GlobalController.getInstance().getPosPresenter().updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
        return GlobalController.getInstance().getPosPresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_RefreshByServerDataAsync_Done);
        return GlobalController.getInstance().getPosPresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
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
        return GlobalController.getInstance().getPosPresenter().retrieve1ObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    @Override
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getPosPresenter().retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent);
    }

    public boolean createReplacerAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行PosSqLiteBO的createReplacerAsync方法，bm=" + bm);
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Pos_CreateReplacerAsync);
        return GlobalController.getInstance().getPosPresenter().createOrReplaceObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent);
    }
}
