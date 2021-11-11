package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;

import org.apache.log4j.Logger;

import java.util.List;

import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_UpdateByServerDataNAsync_Done;

public class VipSQLiteBO extends BaseSQLiteBO {
    private Logger log = Logger.getLogger(this.getClass());

    public VipSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipSQLiteBO的createAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Vip_CreateAsync:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync);
                if (GlobalController.getInstance().getVipPresenter().createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("创建Vip失败！");
                }
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateReplacerAsync_Done);
        return GlobalController.getInstance().getVipPresenter().createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, sqLiteEvent.getBaseModel1(), bmNew, sqLiteEvent);
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        log.info("正在执行VipSQLiteBO的updateAsync，bm=" +(bm == null ? null : bm.toString()));

        switch (sqLiteEvent.getEventTypeSQLite()) {
            case ESET_Vip_UpdateAsync:
                if (GlobalController.getInstance().getVipPresenter().updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("修改Vip失败！");
                }
                break;
            case ESET_Vip_BeforeDeleteAsync_Done:
//                sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_BeforeDeleteAsync_Done);
                if (GlobalController.getInstance().getVipPresenter().updateMasterSlaveObjectAsync(iUseCaseID, bm, sqLiteEvent)) {
                    return true;
                } else {
                    log.info("删除Vip前设置数据失败！");
                }
                break;
            default:
                throw new RuntimeException("未定义的事件！");
        }
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
//        sqLiteEvent.setEventTypeSQLite(ESET_Vip_UpdateByServerDataAsync_Done);
        return GlobalController.getInstance().getVipPresenter().updateMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmNew, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        sqLiteEvent.setEventTypeSQLite(ESET_Vip_UpdateByServerDataNAsync_Done);
        return GlobalController.getInstance().getVipPresenter().updateNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return GlobalController.getInstance().getVipPresenter().deleteObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bmDelete, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        sqLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_RefreshByServerDataAsync_Done);
        return GlobalController.getInstance().getVipPresenter().refreshByServerDataObjectsAsync(BaseSQLiteBO.INVALID_CASE_ID, bmList, sqLiteEvent);
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }
}
