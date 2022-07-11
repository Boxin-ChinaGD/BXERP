package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

public class PromotionSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public PromotionSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行PromotionSQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));
        
        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Promotion_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_CreateAsync);
                if (GlobalController.getInstance().getPromotionPresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入促销表数据失败!");
                }
                break;
            default:
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
        log.info("正在执行PromotionSQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Promotion_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_UpdateAsync);
                if (GlobalController.getInstance().getPromotionPresenter().updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改促销主从表失败");
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
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsync_Done);
        return GlobalController.getInstance().getPromotionPresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Promotion_RefreshByServerDataAsyncC_Done);
        return GlobalController.getInstance().getPromotionPresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return GlobalController.getInstance().getPromotionPresenter().retrieveNObjectSync(iUseCaseID, bm);
    }
}
