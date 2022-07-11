package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

public class VipCategorySQLiteBO extends BaseSQLiteBO{
    private Logger log = Logger.getLogger(this.getClass());

    public VipCategorySQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipCategorySQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_VipCategory_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_CreateAsync);
                if (GlobalController.getInstance().getVipCategoryPresenter().createObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("插入会员类别表数据失败!");
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
        log.info("正在执行VipCategorySQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        httpEvent.setEventProcessed(false);

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_VipCategory_UpdateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_UpdateAsync);
                if (GlobalController.getInstance().getVipCategoryPresenter().updateObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改会员类别主从表失败");
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
        return GlobalController.getInstance().getVipCategoryPresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return GlobalController.getInstance().getVipCategoryPresenter().refreshByServerDataObjectsAsyncC(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        List<?> list = GlobalController.getInstance().getVipCategoryPresenter().retrieveNObjectSync(iUseCaseID, bm);
        sqLiteEvent.setLastErrorCode(GlobalController.getInstance().getRetailTradePresenter().getLastErrorCode());
        sqLiteEvent.setLastErrorMessage(GlobalController.getInstance().getRetailTradePresenter().getLastErrorMessage());
        return list;
    }

    public boolean retrieveNAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipCategorySQLiteBO的retrieveNAsync，bm=" +(bm == null ? null : bm.toString()));
        
        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_VipCategory_RetrieveNAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_VipCategory_RetrieveNAsync);
                if (GlobalController.getInstance().getVipCategoryPresenter().retrieveNObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
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
